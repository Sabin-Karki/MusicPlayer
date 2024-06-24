package MusicPlayer;

import java.io.BufferedInputStream;
import java.io.FileInputStream;

import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

public class MusicPlayerr extends PlaybackListener {

    private Song currentSong;
    private AdvancedPlayer advancedPlayer;

    //pause boolean flag to indicate whether the player has been paused or not
     private boolean isPaused;

     //stores in the last frame when playback is finished
     private int  currentFrame;
     

    public MusicPlayerr() {
    }

    public void loadSong(Song song) {
        currentSong = song;

        // Play current song if not null
        if (currentSong != null) {
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
