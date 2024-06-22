package MusicPlayer;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.*;

public class MPGui extends JFrame {
   //color configurations

   public static final Color FRAME_COLOR= Color.GRAY;
   public static final Color TEXT_COLOR= Color.BLACK;


    public MPGui(){
        //creating title
       super("Music Player");

       //creating height and width
       setSize(400, 570);

       //closes the gui
       setDefaultCloseOperation(EXIT_ON_CLOSE);

       setLocationRelativeTo(null);

      //prevents frame from being resized
       setResizable(false);


       setLayout(null);

       //change the frame color

       getContentPane().setBackground(FRAME_COLOR);


       addGUIcomponents();
       
    }


    private void addGUIcomponents(){
        addToolbar();




        //load record image

        JLabel songImage = new JLabel(loadImage("MusicPlayer\\assets\\record.png"));
        songImage.setBounds(0, 50, getWidth()-20, 228);
        add(songImage);

        //song Title
        JLabel songTitle=new JLabel("Song Title");
        songTitle.setBounds(0, 285, getWidth()-10, 30);
        songTitle.setFont(new Font("Dialog",Font.BOLD,19));
        songTitle.setForeground(TEXT_COLOR);
        songTitle.setHorizontalAlignment(SwingConstants.CENTER);
        add(songTitle);

        
        //song artist
        JLabel songArtist=new JLabel("Artist");
        songArtist.setBounds(0, 315, getWidth()-10, 30);
        songArtist.setFont(new Font("Dialog",Font.BOLD,19));
        songArtist.setForeground(TEXT_COLOR);
        songArtist.setHorizontalAlignment(SwingConstants.CENTER);
        add(songArtist);

        //playback slider
        JSlider playbackSlider=new JSlider(JSlider.HORIZONTAL,0,100,0);
        playbackSlider.setBounds(getWidth()/2-300/2, 365, 300, 40);
        playbackSlider.setBackground(null);
        add(playbackSlider);
      
        //playback buttons
        addPlaybackButtons();


    }
    private void addToolbar(){
        JToolBar tool=new JToolBar();
        tool.setBounds(0, 0, getWidth(),25);

        //prvent tool bar from being moved
        tool.setFloatable(false);

        //add drop menu
        JMenuBar menuBar = new JMenuBar();
        tool.add(menuBar);

        //now we will add a song menu where we will place the loading song option
       JMenu songMenu=new JMenu("Song");
       menuBar.add(songMenu);


       //add the load song item in the songmenu
       JMenuItem loadSong=new JMenuItem("Load Song");
       songMenu.add(loadSong);

       //add the liked saved song in the songmenu

       JMenuItem savedSong = new JMenuItem("Saved Song");
       songMenu.add(savedSong);
          
       //now this adds the playlist menu
       JMenu playJMenu=new JMenu("Playlist");
       menuBar.add(playJMenu);

      //adding items to playlist menu 
      JMenuItem createPlaylist=new JMenuItem("Create Playlist");
      playJMenu.add(createPlaylist);

      JMenuItem loadPlaylist=new JMenuItem("Load Playlist");
      playJMenu.add(loadPlaylist);
      
        add(tool);


    }
 
    private void addPlaybackButtons(){
     JPanel playbackBtn=new JPanel(); 
     playbackBtn.setBounds(0, 435,getWidth()-10,56); 
     playbackBtn.setBackground(null);


     //prev button
    JButton prevButton=new JButton(loadImage("MusicPlayer\\assets\\previous.png"));
   prevButton.setBorderPainted(false);
   prevButton.setBackground(null);
   playbackBtn.add(prevButton);


   //play button
   JButton playButton=new JButton(loadImage("MusicPlayer\\assets\\play.png"));
   playButton.setBorderPainted(false);
   playButton.setBackground(null);
   playbackBtn.add(playButton);



   //pause button
   JButton pauseButton = new JButton(loadImage("MusicPlayer\\assets\\pause.png"));
   pauseButton.setBorderPainted(false);
   pauseButton.setBackground(null);
   pauseButton.setVisible(false);
   playbackBtn.add(pauseButton);

   //next button
   JButton nextButton=new JButton(loadImage("MusicPlayer\\assets\\next.png"));
   nextButton.setBorderPainted(false);
   nextButton.setBackground(null);
   playbackBtn.add(nextButton);
 
   

   add(playbackBtn);

    }
    private ImageIcon loadImage(String imagePath){
        try {
            //read image file from given path
            BufferedImage image=ImageIO.read(new File(imagePath));
             
            //returns an image icon so that our component can render the image
            return new ImageIcon(image);
            
        } catch (Exception e) {
          e.printStackTrace();
        }

        //could not find resoucrce
        return null;
    }
}