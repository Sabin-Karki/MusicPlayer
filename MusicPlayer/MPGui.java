package MusicPlayer;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Hashtable;

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
    private JSlider playbackSlider;

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

        musicPlayer = new MusicPlayerr(this);

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
  playbackSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 0);
        playbackSlider.setBounds(getWidth() / 2 - 300 / 2, 365, 300, 40);
        playbackSlider.setBackground(null);
        playbackSlider.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e){
                //when user is holding tick we want to pause the song
                 musicPlayer.pauseSong();
            }
            @Override
            public void mouseReleased(MouseEvent e){
                //when user drops the tick
                JSlider source= (JSlider) e.getSource();

                //get the frame value from where user wants to playback to
                int frame= source.getValue(); 

                //update current frame  in music player to this frame
                musicPlayer.setCurrentFrame(frame);

                //update current time in milli as well
                musicPlayer.setcurrentTimeinMilli((int)(frame/(2.08 * musicPlayer.getCurrentSong().getFrameRatePerMilliseconds())));

                //resume song

                musicPlayer.playCurrentSong();

                //toggle on pause button and toggle off play button

                enablePauseButtonDisablePlayButton();

            }
        });
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

                        //update playback slider
                        UpdatePlaybackSlider(song);

                        //toggle on pause and toggle off play button

                        enablePauseButtonDisablePlayButton();
                       
                    }
                }
            }
        });
        songMenu.add(loadSong);

     

        // Add the playlist menu
        JMenu playJMenu = new JMenu("Playlist");
        menuBar.add(playJMenu);




        // Adding items to playlist menu
        JMenuItem createPlaylist = new JMenuItem("Create Playlist");
        createPlaylist.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                //load music playlist dialog
                new MusicPlaylistDialog(MPGui.this).setVisible(true);;
                    

            }
        });
        playJMenu.add(createPlaylist);





        JMenuItem loadPlaylist = new JMenuItem("Load Playlist");
        loadPlaylist.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e){
           JFileChooser jFileChooser=new JFileChooser();
           jFileChooser.setFileFilter(new FileNameExtensionFilter("TravisPlayList", "txt"));
           jFileChooser.setCurrentDirectory(new File("C:\\Users\\sabin\\OneDrive\\Documents\\SideProject\\MusicPlayer\\assets"));

           
           int result=jFileChooser.showOpenDialog(MPGui.this);
           File selectedFile=jFileChooser.getSelectedFile();

           if(result==JFileChooser.APPROVE_OPTION && selectedFile!=null){
          //stop music
          musicPlayer.stopSong();

          //load playlist
          musicPlayer.loadPlaylist(selectedFile);

           }


         }   
        });
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
        prevButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                musicPlayer.prevSong();
                
            }
        });

        // Play button
        JButton playButton = new JButton(loadImage("MusicPlayer\\assets\\play.png"));
        playButton.setBorderPainted(false);
        playButton.setBackground(null);
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed (ActionEvent e){
                //toggle on pause and toggle off play
                enablePauseButtonDisablePlayButton();

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
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                
                //go to next song 
             musicPlayer.nextSong();


            }
        });
        add(playbackBtn);
    }

    //this will be used to update our slider from music player class
    public void setPlaybackSliderValue(int frame){
        playbackSlider.setValue(frame);
    }

    public  void updateSongTitleAndArtist(Song song) {
        System.out.println("Updating song title and artist");
        System.out.println("Title: " + song.getSongTitle());
        System.out.println("Artist: " + song.getSongArtist());

        songTitle.setText(song.getSongTitle());
        songArtist.setText(song.getSongArtist());
    }

 public void UpdatePlaybackSlider(Song song){

        //update max count for slider
        playbackSlider.setMaximum(song.getMp3File().getFrameCount());

        //create the song lenght label
        Hashtable<Integer,JLabel> labelTable=new Hashtable<>();

        //initially,it will be 00:00
        JLabel labelBeginning=new JLabel("00:00");
        labelBeginning.setFont(new Font("Dialog",Font.BOLD,18));
        labelBeginning.setForeground((TEXT_COLOR));

        //end will vary depending on song 
        JLabel labelEnd=new JLabel(song.getSongLength());
        labelEnd.setFont(new Font("Dialog",Font.BOLD,18));
        labelEnd.setForeground((TEXT_COLOR));
        labelTable.put(0,labelBeginning);
        labelTable.put(song.getMp3File().getFrameCount(),labelEnd);
        playbackSlider.setLabelTable(labelTable);
        playbackSlider.setPaintLabels(true);



    }

  public  void enablePauseButtonDisablePlayButton(){
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
    
   public void enablePlayButtonDisablePauseButton(){
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
