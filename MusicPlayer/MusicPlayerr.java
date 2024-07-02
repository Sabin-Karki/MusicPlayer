package MusicPlayer;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.ArrayList;

import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;


public class MusicPlayerr extends PlaybackListener {

    //this will be used to update isPaused more synchornously
    private static final Object playSignal=new Object();
 
private MPGui musicPlayerGUI;
    private Song currentSong;
    public Song getCurrentSong(){
        return currentSong;
    }
private ArrayList<Song> playlist;



    private AdvancedPlayer advancedPlayer;

    //pause boolean flag to indicate whether the player has been paused or not
     private boolean isPaused;

     //stores in the last frame when playback is finished(used for pausing and resuming)
     private int  currentFrame;
     public void setCurrentFrame(int frame){
        currentFrame=frame;
     }

     //track how many milliseconds has passed since playing song(used for updating slider)
     
     private int currentTimeinMilli;
     public void setcurrentTimeinMilli(int timeInMilli){
        currentTimeinMilli=timeInMilli;
     }
     
     public MusicPlayerr(MPGui musicPlayerGUI){
        this.musicPlayerGUI=musicPlayerGUI;
     }



    public void loadSong(Song song) {
        currentSong = song;

        // Play current song if not null
        if (currentSong != null) {
            playCurrentSong();
        }
       
    }




    public void loadPlaylist(File playlistFile){
        playlist=new ArrayList<>();

        //store the paths from text file into playlist array list
        try{
           FileReader fr=new FileReader(playlistFile);
           BufferedReader br=new BufferedReader(fr);
        
           //reach each line from text file and store text into songPath variable 
           String songPath;
           while((songPath= br.readLine())!=null){
Song song=new Song(songPath);

         //add to playlist arraylist
         playlist.add(song);
           }
        }catch(Exception e){
            e.printStackTrace();
            }

            if(playlist.size()>0){

                //reset playback slider
           musicPlayerGUI.setPlaybackSliderValue(0);
           currentTimeinMilli=0;

                //update current song to first song in the playlist
                currentSong=playlist.get(0);

                //start from beginning frame
                currentFrame=0;

                //update gui
                musicPlayerGUI.enablePauseButtonDisablePlayButton();
                musicPlayerGUI.updateSongTitleAndArtist(currentSong);
                musicPlayerGUI.UpdatePlaybackSlider(currentSong);

                //start song
                playCurrentSong();

            }
    }




    

 public void pauseSong(){
    if(advancedPlayer !=null){
        isPaused=true;

        //then we have to stop the player
        stopSong();
    }

 }

 public void stopSong(){
    if(advancedPlayer !=null){
        advancedPlayer.stop();
        advancedPlayer.close();
        advancedPlayer=null;
    }
 }
    public void playCurrentSong() {
        if(currentSong==null){
            return;
        }
        try {
            // Read mp3 audio data
            FileInputStream fileInputStream = new FileInputStream(currentSong.getFilePath());
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);

            // Create a new advanced player
            advancedPlayer = new AdvancedPlayer(bufferedInputStream);
            advancedPlayer.setPlayBackListener(this);

            // Start music
            startMusicThread();

            //start playback slider thread
            startPlaybacksliderThread();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Create a thread that will handle playing the music
    public void startMusicThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if(isPaused){
                         synchronized(playSignal){
                            //update flag
                            isPaused=false;

                            //notify the other thread to continue(make sure that isPaused is updated to false properly)
                            playSignal.notify();
                         }
                    //resume music from last frame
                    advancedPlayer.play(currentFrame,Integer.MAX_VALUE);
                    }else{
                   // Play music from beginning
                    advancedPlayer.play();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //create a thread that will update slider
    private void startPlaybacksliderThread(){
        new Thread(new Runnable(){
            @Override
            public void run(){

                if(isPaused){
                    try{
                        //wait till it gets notified by other thread to continue
                        //make sure isPaused boolean flag updates to false before continuing
                        synchronized(playSignal){
                            playSignal.wait();
                        }

                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
               while(!isPaused){
                try{
                //increment current time
                currentTimeinMilli++;

                //calculate into frame value
                int calculatedFrame=(int)((double) currentTimeinMilli*2.08*currentSong.getFrameRatePerMilliseconds());
               
                //update gui
                musicPlayerGUI.setPlaybackSliderValue(calculatedFrame);

                //mimic 1 ms  using thread.sleep
                Thread.sleep(1);

                }catch(Exception e){
                    e.printStackTrace();
                }
                
            }
        }
    }).start();
    }

     
@Override
public void playbackStarted(PlaybackEvent evt){
//this methods get called int he beginning of the song
System.out.println("playback started");
}
@Override
public void playbackFinished(PlaybackEvent evt){
    //this methid gets called when thw song finished or if the player gets called
    System.out.println("playback finished");

    if(isPaused){
        currentFrame +=(int) ((double)evt.getFrame()   
        *  currentSong.getFrameRatePerMilliseconds());

    }

}

}
