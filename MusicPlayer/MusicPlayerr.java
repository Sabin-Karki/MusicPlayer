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

    // this will be used to update isPaused more synchronously
    private static final Object playSignal = new Object();

    private MPGui musicPlayerGUI;
    private Song currentSong;

    public Song getCurrentSong() {
        return currentSong;
    }

    private ArrayList<Song> playlist;
    private int currentListIndex;

    private AdvancedPlayer advancedPlayer;

    // pause boolean flag to indicate whether the player has been paused or not
    private boolean isPaused;
    // boolean flag to call when song is finished
    private boolean songFinished;

    public boolean pressedNext, pressedPrev;

    // stores in the last frame when playback is finished (used for pausing and resuming)
    private int currentFrame;
    public void setCurrentFrame(int frame) {
        currentFrame = frame;
    }

    // track how many milliseconds has passed since playing song (used for updating slider)
    private int currentTimeInMilli;
    public void setcurrentTimeinMilli(int timeInMilli) {
        currentTimeInMilli = timeInMilli;
    }

    public MusicPlayerr(MPGui musicPlayerGUI) {
        this.musicPlayerGUI = musicPlayerGUI;
    }

    public void loadSong(Song song) {
        currentSong = song;
        playlist = null;

        // stop song if possible
        if (!songFinished)
            stopSong();

        // Play current song if not null
        if (currentSong != null) {
            // reset current frame
            currentFrame = 0;

            // reset current time in milli
            currentTimeInMilli = 0;

            // update gui
            musicPlayerGUI.setPlaybackSliderValue(0);

            playCurrentSong();
        }
    }

    public void loadPlaylist(File playlistFile) {
        playlist = new ArrayList<>();

        // store the paths from text file into playlist array list
        try {
            FileReader fr = new FileReader(playlistFile);
            BufferedReader br = new BufferedReader(fr);

            // reach each line from text file and store text into songPath variable
            String songPath;
            while ((songPath = br.readLine()) != null) {
                Song song = new Song(songPath);

                // add to playlist arraylist
                playlist.add(song);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (playlist.size() > 0) {
            // reset playback slider
            musicPlayerGUI.setPlaybackSliderValue(0);
            currentTimeInMilli = 0;

            // update current song to first song in the playlist
            currentSong = playlist.get(0);

            // start from beginning frame
            currentFrame = 0;

            // update gui
            musicPlayerGUI.enablePauseButtonDisablePlayButton();
            musicPlayerGUI.updateSongTitleAndArtist(currentSong);
            musicPlayerGUI.UpdatePlaybackSlider(currentSong);

            // start song
            playCurrentSong();
        }
    }

    public void pauseSong() {
        if (advancedPlayer != null) {
            isPaused = true;

            // then we have to stop the player
            stopSong();
        }
    }

    public void stopSong() {
        if (advancedPlayer != null) {
            advancedPlayer.stop();
            advancedPlayer.close();
            advancedPlayer = null;
        }
    }

    public void nextSong() {
        if (playlist == null) {
            return;
        }

        // check to see if we have reached end of playlist, if so then don't do anything
        if (currentListIndex + 1 > playlist.size() - 1) return;
        pressedNext = true;

        // stop song if possible
        if (!songFinished)
            stopSong();

        // increase current playlist index
        currentListIndex++;

        // update current song
        currentSong = playlist.get(currentListIndex);

        // reset frame
        currentFrame = 0;

        // reset time in milli
        currentTimeInMilli = 0;

        // update GUI
        musicPlayerGUI.enablePauseButtonDisablePlayButton();
        musicPlayerGUI.UpdatePlaybackSlider(currentSong);
        musicPlayerGUI.updateSongTitleAndArtist(currentSong);
        musicPlayerGUI.setPlaybackSliderValue(0);

        // play the song
        playCurrentSong();
    }

    public void prevSong() {
        if (playlist == null) return;

        // check to see if we can go to previous song
        if (currentListIndex - 1 < 0) return;
        pressedPrev = true;
        // stop song if possible
        if (!songFinished)
            stopSong();

        // decrease current index
        currentListIndex--;

        // update current song
        currentSong = playlist.get(currentListIndex);

        // update current frame
        currentFrame = 0;

        // update current time in milli
        currentTimeInMilli = 0;

        // update GUI
        musicPlayerGUI.UpdatePlaybackSlider(currentSong);
        musicPlayerGUI.enablePauseButtonDisablePlayButton();
        musicPlayerGUI.updateSongTitleAndArtist(currentSong);
        musicPlayerGUI.setPlaybackSliderValue(0);

        // play the song
        playCurrentSong();
    }

    public void playCurrentSong() {
        if (currentSong == null) {
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

            // start playback slider thread
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
                    if (isPaused) {
                        synchronized (playSignal) {
                            // update flag
                            isPaused = false;

                            // notify the other thread to continue (make sure that isPaused is updated to false properly)
                            playSignal.notify();
                        }
                        // resume music from last frame
                        advancedPlayer.play(currentFrame, Integer.MAX_VALUE);
                    } else {
                        // Play music from beginning
                        advancedPlayer.play();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    // create a thread that will update slider
    private void startPlaybacksliderThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (isPaused) {
                    try {
                        // wait till it gets notified by other thread to continue
                        // make sure isPaused boolean flag updates to false before continuing
                        synchronized (playSignal) {
                            playSignal.wait();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                while (!isPaused && !songFinished && !pressedNext && !pressedPrev) {
                    try {
                        // increment current time
                        currentTimeInMilli++;

                        // calculate into frame value
                        int calculatedFrame = (int) ((double) currentTimeInMilli * 2.08 * currentSong.getFrameRatePerMilliseconds());

                        // update gui
                        musicPlayerGUI.setPlaybackSliderValue(calculatedFrame);

                        // mimic 1 ms using thread.sleep
                        Thread.sleep(1);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        }).start();
    }

    @Override
    public void playbackStarted(PlaybackEvent evt) {
        // this methods get called in the beginning of the song
        System.out.println("playback started");
        songFinished = false;
        pressedNext = false;
        pressedPrev = false;
    }

    @Override
    public void playbackFinished(PlaybackEvent evt) {
        // this method gets called when the song finishes or if the player gets called
        System.out.println("playback finished");

        if (isPaused) {
            currentFrame += (int) ((double) evt.getFrame()
                    * currentSong.getFrameRatePerMilliseconds());

        } else {
            // if user pressed next or prev we don't need to execute rest of code
            if (pressedNext || pressedPrev) return;

            // when song ends
            songFinished = true;
            if (playlist == null) {
                // update gui
                musicPlayerGUI.enablePlayButtonDisablePauseButton();
            }

            // last song in playlist
            else {
                if (currentListIndex == playlist.size() - 1) {
                    musicPlayerGUI.enablePlayButtonDisablePauseButton();
                } else {
                    // go to next song
                    nextSong();
                }
            }
        }
    }
}
