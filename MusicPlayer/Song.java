package MusicPlayer;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

import com.mpatric.mp3agic.Mp3File;

import java.io.File;

public class Song {
    private String songTitle;
    private String songArtist;
    private String songLength;
    private String filePath;
    private Mp3File mp3File;
    private double frameRatePerMilliseconds;

    public Song(String filePath) {
        this.filePath = filePath;
        try {
            mp3File= new Mp3File(filePath);
            frameRatePerMilliseconds = (double)mp3File.getFrameCount()/mp3File.getLengthInMilliseconds();
            File file = new File(filePath);
            if (file.exists()) {
                AudioFile audioFile = AudioFileIO.read(file);
                Tag tag = audioFile.getTag();
                System.out.println("Reading file: " + filePath);
                if (tag != null) {
                    songTitle = tag.getFirst(FieldKey.TITLE);
                    songArtist = tag.getFirst(FieldKey.ARTIST);
                  
                } else {
                    System.out.println("Tag is null");
                    songTitle = "Unknown Title";
                    songArtist = "Unknown Artist";
                }
            } else {
                System.out.println("File does not exist: " + filePath);
                songTitle = "File Not Found";
                songArtist = "File Not Found";
            }
        } catch (Exception e) {
            e.printStackTrace();
            songTitle = "Error";
            songArtist = "Error";
        }
    }

    public String getSongTitle() {
        return songTitle;
    }

    public String getSongArtist() {
        return songArtist;
    }

    public String getFilePath() {
        return filePath;
    }
    public Mp3File getMp3File(){
        return mp3File;
    }
    public double getFrameRatePerMilliseconds(){
        return frameRatePerMilliseconds;
    }
}
