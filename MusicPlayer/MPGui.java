package MusicPlayer;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class MPGui extends JFrame {
    // Color configurations
    public static final Color FRAME_COLOR = Color.GRAY;
    public static final Color TEXT_COLOR = Color.BLACK;

    private MusicPlayerr musicPlayer;
    private JFileChooser jFileChooser;

    private JLabel songTitle, songArtist;
    private JPanel playbackBtn;

    public MPGui() {
        // Creating title
        super("Music Player");

        // Creating height and width
        setSize(400, 570);

        // Closes the GUI
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLocationRelativeTo(null);

        // Prevents frame from being resized
        setResizable(false);

        setLayout(null);

        // Change the frame color
        getContentPane().setBackground(FRAME_COLOR);

        musicPlayer = new MusicPlayerr();

        jFileChooser = new JFileChooser();

        // Set a default path for file explorer
        jFileChooser.setCurrentDirectory(new File("C:\\Users\\sabin\\OneDrive\\Documents\\SideProject\\MusicPlayer\\assets"));

        //filter to see only .mp3 files

        jFileChooser.setFileFilter(new FileNameExtensionFilter("mp3","MP3"));

        addGUIComponents();
    }

    private void addGUIComponents() {
        addToolbar();

        // Load record image
        JLabel songImage = new JLabel(loadImage("MusicPlayer\\assets\\record.png"));
        songImage.setBounds(0, 50, getWidth() - 20, 228);
        add(songImage);

        // Song Title
        songTitle = new JLabel("Song Title");
        songTitle.setBounds(0, 285, getWidth() - 10, 30);
        songTitle.setFont(new Font("Dialog", Font.BOLD, 19));
        songTitle.setForeground(TEXT_COLOR);
        songTitle.setHorizontalAlignment(SwingConstants.CENTER);
        add(songTitle);

        // Song Artist
        songArtist = new JLabel("Artist");
        songArtist.setBounds(0, 315, getWidth() - 10, 30);
        songArtist.setFont(new Font("Dialog", Font.BOLD, 19));
        songArtist.setForeground(TEXT_COLOR);
        songArtist.setHorizontalAlignment(SwingConstants.CENTER);
        add(songArtist);

        // Playback slider
        JSlider playbackSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 0);
        playbackSlider.setBounds(getWidth() / 2 - 300 / 2, 365, 300, 40);
        playbackSlider.setBackground(null);
        add(playbackSlider);

        // Playback buttons
        addPlaybackButtons();
    }

    private void addToolbar() {
        JToolBar tool = new JToolBar();
        tool.setBounds(0, 0, getWidth(), 25);

        // Prevent toolbar from being moved
        tool.setFloatable(false);

        // Add drop menu
        JMenuBar menuBar = new JMenuBar();
        tool.add(menuBar);

        // Add a song menu where we will place the loading song option
        JMenu songMenu = new JMenu("Song");
        menuBar.add(songMenu);

        // Add the load song item in the song menu
        JMenuItem loadSong = new JMenuItem("Load Song");
        loadSong.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int returnValue = jFileChooser.showOpenDialog(MPGui.this);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = jFileChooser.getSelectedFile();

                    //this means that we are also checking if the user pressed the open button
                    if (returnValue== JFileChooser.APPROVE_OPTION && selectedFile != null) {
                        // Create a song object based on selected file
                        Song song = new Song(selectedFile.getPath());

                        // Load song in music player
                        musicPlayer.loadSong(song);

                        // Update song title and artist
                        updateSongTitleAndArtist(song);

                        //toggle on pause and toggle off play button

                        enablePauseButtonDisableButton();
                       
                    }
                }
            }
        });
        songMenu.add(loadSong);

        // Add the liked saved song in the song menu
        JMenuItem savedSong = new JMenuItem("Saved Song");
        songMenu.add(savedSong);

        // Add the playlist menu
        JMenu playJMenu = new JMenu("Playlist");
        menuBar.add(playJMenu);

        // Adding items to playlist menu
        JMenuItem createPlaylist = new JMenuItem("Create Playlist");
        playJMenu.add(createPlaylist);

        JMenuItem loadPlaylist = new JMenuItem("Load Playlist");
        playJMenu.add(loadPlaylist);

        add(tool);
    }

    private void addPlaybackButtons() {
    playbackBtn = new JPanel();
        playbackBtn.setBounds(0, 435, getWidth() - 10, 56);
        playbackBtn.setBackground(null);

        // Prev button
        JButton prevButton = new JButton(loadImage("MusicPlayer\\assets\\previous.png"));
        prevButton.setBorderPainted(false);
        prevButton.setBackground(null);
        playbackBtn.add(prevButton);

        // Play button
        JButton playButton = new JButton(loadImage("MusicPlayer\\assets\\play.png"));
        playButton.setBorderPainted(false);
        playButton.setBackground(null);
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed (ActionEvent e){
                //toggle on pause and toggle off play
                enablePauseButtonDisableButton();

                //play or resume song
                musicPlayer.playCurrentSong();
            }
        });
        playbackBtn.add(playButton);

        // Pause button
        JButton pauseButton = new JButton(loadImage("MusicPlayer\\assets\\pause.png"));
        pauseButton.setBorderPainted(false);
        pauseButton.setBackground(null);
        pauseButton.setVisible(false);
        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
            //toggle off pause and toggle on play
                enablePlayButtonDisablePauseButton();
            
            // pause the song
              musicPlayer.pauseSong();
            }

            
        });
        playbackBtn.add(pauseButton);

        // Next button
        JButton nextButton = new JButton(loadImage("MusicPlayer\\assets\\next.png"));
        nextButton.setBorderPainted(false);
        nextButton.setBackground(null);
        playbackBtn.add(nextButton);

        add(playbackBtn);
    }

    private void updateSongTitleAndArtist(Song song) {
        System.out.println("Updating song title and artist");
        System.out.println("Title: " + song.getSongTitle());
        System.out.println("Artist: " + song.getSongArtist());

        songTitle.setText(song.getSongTitle());
        songArtist.setText(song.getSongArtist());
    }

    private void enablePauseButtonDisableButton(){
    //retrieve reference to play and playbackbtn panel
    JButton playButton=(JButton) playbackBtn.getComponent(1);

    JButton pauseButton=(JButton) playbackBtn.getComponent(2);


    //turn off play button
    playButton.setVisible(false);
    playButton.setEnabled(false);

    //turn on pause button
    pauseButton.setVisible(true);
    pauseButton.setEnabled(true);
    }
    
    private void enablePlayButtonDisablePauseButton(){
        //retrieve reference to play and playbackbtn panel
        JButton playButton=(JButton) playbackBtn.getComponent(1);
    
        JButton pauseButton=(JButton) playbackBtn.getComponent(2);
    
    
        //turn on  play button
        playButton.setVisible(true);
        playButton.setEnabled(true);
    
        //turn off pause button
        pauseButton.setVisible(false);
        pauseButton.setEnabled(false);
        }
        

    private ImageIcon loadImage(String imagePath) {
        try {
            // Read image file from given path
            BufferedImage image = ImageIO.read(new File(imagePath));

            // Return an image icon so that our component can render the image
            return new ImageIcon(image);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Could not find resource
        return null;
    }

   
}
