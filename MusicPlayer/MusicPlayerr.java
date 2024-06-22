package MusicPlayer;
// import MusicPlayer.MPGui;

import java.io.BufferedInputStream;
import java.io.FileInputStream;

import javazoom.jl.player.advanced.AdvancedPlayer;

public class MusicPlayerr {

    //need to store our song details,so will be creating song class

    private Song currentSong;


    //using JLayer library to create an AdvancedPlayer obj which will handle playing music

    private AdvancedPlayer advancedPlayer;

    public MusicPlayerr(){

         
    }
    
    public void loadSonh(Song song){
        currentSong=song;

        //play current song if not null
        if(currentSong!=null){
            playCurrentSong();
        }
    }
    public void playCurrentSong(){
        try{
            //read mp3 audio data
            FileInputStream fileInputStream= new FileInputStream(currentSong.getFilePath());
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);

            //create a new advanced player
            advancedPlayer = new AdvancedPlayer(bufferedInputStream);

             //start music

             startMusicThread();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    //create a thread that will handle playing the music 

    public void startMusicThread(){
        new Thread(new Runnable(){
            @Override
            public void run(){
                try{
                    //play music
                    advancedPlayer.play();

                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
}