package MusicPlayer;

import javax.swing.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

public class MusicPlaylistDialog extends JDialog {
   public  MPGui musicPlayerGUI;

    //store all paths to be written to a txt file(When we load a playlist)
    private ArrayList<String> songPaths;
  
    public MusicPlaylistDialog(MPGui musicPlayerGUI){
        this.musicPlayerGUI=musicPlayerGUI;
        songPaths=new ArrayList<>();

        //configure dialog
        setTitle("Create Playlist");
        setSize(400,400);
        setResizable(false);
        getContentPane().setBackground(MPGui.FRAME_COLOR);
        setLayout(null);
        setModal(true);//this property makes it that dialog has to be closed to give focus
      
        setLocationRelativeTo(musicPlayerGUI);
        
        addDialogComp();



    }
    private void addDialogComp(){
        //container to hold each song path
        JPanel songContainer=new JPanel();
        songContainer.setLayout(new BoxLayout(songContainer,BoxLayout.Y_AXIS));
        songContainer.setBounds((int)(getWidth()*0.025),10,(int)(getWidth()*0.90),(int)(getHeight()*0.75));
        add(songContainer);

        //add song button
        JButton addSongButton=new JButton("Add");
        addSongButton.setBounds(60,(int)(getHeight()*0.80),100,25);
        addSongButton.setFont(new Font("Dialog",Font.BOLD,14));
        


        addSongButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed (ActionEvent e){
                JFileChooser jFileChooser=new JFileChooser();
                jFileChooser.setFileFilter(new FileNameExtensionFilter("mp3", "MP3"));
                jFileChooser.setCurrentDirectory(new File("C:\\Users\\sabin\\OneDrive\\Documents\\SideProject\\MusicPlayer\\assets"));
                int result=jFileChooser.showOpenDialog(MusicPlaylistDialog.this);
                File selectedFile=jFileChooser.getSelectedFile();
                if(result==JFileChooser.APPROVE_OPTION && selectedFile!=null){
                    JLabel filePathLabel=new JLabel(selectedFile.getPath());
                    filePathLabel.setFont(new Font("Dialog",Font.BOLD,12));
                    filePathLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

                    //add to list
                    songPaths.add(filePathLabel.getText());

                    //add to container

                    songContainer.add(filePathLabel);
                    
                    //refreshes dialog to show newly added JLabel
                    songContainer.revalidate();


                }

            }
        });
        add(addSongButton);

        //save playlist button
        JButton savePlaylistButton=new JButton("Save");
        savePlaylistButton.setBounds(200,(int)(getHeight()*0.80),100,25);
      savePlaylistButton.setFont(new Font("Dialog",Font.BOLD,14));
      savePlaylistButton.addActionListener((new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e){
            try{
                JFileChooser jFileChooser=new JFileChooser();
            jFileChooser.setCurrentDirectory(new File("C:\\Users\\sabin\\OneDrive\\Documents\\SideProject\\MusicPlayer\\assets"));
            int result= jFileChooser.showSaveDialog(MusicPlaylistDialog.this);
            if(result==JFileChooser.APPROVE_OPTION){

                //we use getSelectedFile to get reference to file that we are about to save
                File selectedFile=jFileChooser.getSelectedFile();

                if(!selectedFile.getName().substring(selectedFile.getName().length()-4).equalsIgnoreCase(".txt")){
               selectedFile=new File(selectedFile.getAbsoluteFile()+".txt");
                }

                //create the new file at destinated directory
                selectedFile.createNewFile();

                //now we will write all of song paths into this file
                FileWriter fileWriter=new FileWriter(selectedFile);
                BufferedWriter bw=new BufferedWriter(fileWriter);

                //iterate through our song paths list and write each string into file
                //each song will be written in their own row
                for(String songPath:songPaths){
                    bw.write(songPath + "\n");
                }
                bw.close();
               
                
                //display success dialog
                JOptionPane.showMessageDialog(MusicPlaylistDialog.this,"Successfully created!!!");


                MusicPlaylistDialog.this.dispose();

            }
            }catch(Exception e1){
                e1.printStackTrace();
            }

        }
      }));
        add(savePlaylistButton);
        



    }

}
 