package MusicPlayer;

import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MPGui().setVisible(true);
                // Song song = new Song("MusicPlayer\\assets\\Stargazing.mp3");
                // System.out.println((song.getSongTitle()));
                // System.out.println(song.getSongArtist());
            }
        });
    }
} 