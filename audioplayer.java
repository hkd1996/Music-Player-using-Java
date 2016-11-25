import java.awt.event.*;
import javax.swing.*;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.FlowLayout;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.*;
import javax.swing.*;
import java.util.Vector;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.Component;
import java.awt.Color;

public class audioplayer
{
    static JFileChooser browse;
    static SourceDataLine audioSource;
    static int BUFFER_SIZE;
    public static String[] selections = new String[10];
    public static String temp;
    static JFrame window;
    static JPanel player;
    static JButton play;
    static JButton stop;
    static JButton open;
    public static File file;
    static private javax.swing.JList jList1;

    public static void guii()
    {
        JFrame player=new JFrame("SIMPLE AUDIO PLAYER");
        JFrame chooser=new JFrame("File chooser");
    
        JButton open=new JButton(new ImageIcon("Icons/3.png"));
        open.setBounds(150,37,50,47);

        open.addMouseListener(new MouseListener()
        {
            @Override
            public void mouseReleased(MouseEvent arg0)
            {

            }

            @Override
            public void mousePressed(MouseEvent arg0)
            {

            }

            @Override
            public void mouseExited(MouseEvent arg0)
            {

            }

            @Override
            public void mouseEntered(MouseEvent arg0)
            {

            }

            @Override
            public void mouseClicked(MouseEvent arg0)
            {

                Thread thread = new Thread(new Runnable()
                {

                    @Override
                    public void run()
                    {
                        {
                            browse = new JFileChooser();
                            int ret = browse.showOpenDialog(player);

                            if (ret == JFileChooser.APPROVE_OPTION)
                            {
                                file = browse.getSelectedFile();
                                System.out.println("Opening: " + file.getName());
                                try {
                                    String content = file.getAbsolutePath();
                                    File fp = new File("path.txt");
                                    if (!fp.exists()) {
                                        fp.createNewFile();
                                    }
                                    FileWriter fw = new FileWriter(fp.getAbsoluteFile(),true);
                                    BufferedWriter bw = new BufferedWriter(fw);
                                    bw.write(content+"\n");
                                    bw.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            else
                            {
                                System.out.println("Open command cancelled by user.");
                            }

                        }
                    }
                });

                thread.start();
            }
        });

        player.add(open);

        JButton stop=new JButton(new ImageIcon("Icons/1.png"));
        stop.setBounds(215,37,50,47);

        stop.addMouseListener(new MouseListener()
        {

            @Override
            public void mouseReleased(MouseEvent arg0)
            {

            }

            @Override
            public void mousePressed(MouseEvent arg0)
            {

            }

            @Override
            public void mouseExited(MouseEvent arg0)
            {

            }

            @Override
            public void mouseEntered(MouseEvent arg0)
            {

            }

            @Override
            public void mouseClicked(MouseEvent arg0)
            {

                Thread thread = new Thread(new Runnable()
                {

                    @Override
                    public void run()
                    {

                        if (audioSource.isOpen())
                        {
                            System.out.println("File has been stopped");
                            audioSource.stop();
                        }
                    }
                });

                thread.start();
            }
        });

        player.add(stop);

        JButton play=new JButton(new ImageIcon("Icons/2.png"));
        play.setBounds(280,37,50,47);

        play.addMouseListener(new MouseListener()
        {

            @Override
            public void mouseReleased(MouseEvent arg0)
            {

            }

            @Override
            public void mousePressed(MouseEvent arg0)
            {

            }

            @Override
            public void mouseExited(MouseEvent arg0)
            {

            }

            @Override
            public void mouseEntered(MouseEvent arg0)
            {

            }

            @Override
            public void mouseClicked(MouseEvent arg0)
            {

                Thread thread = new Thread(new Runnable()
                {

                    @Override
                    public void run()
                    {
                        try
                        {
                            AudioInputStream stream = AudioSystem.getAudioInputStream(file);
                            AudioFormat audioFormat = stream.getFormat();
                            DataLine.Info info = new DataLine.Info(SourceDataLine.class,
                                                                   audioFormat);

                            audioSource = (SourceDataLine) AudioSystem.getLine(info);
                            audioSource.open(audioFormat);

                            audioSource.start();

                            int readBytes = 0;
                            BUFFER_SIZE = audioSource.getBufferSize();
                            byte[] audioBuffer = new byte[BUFFER_SIZE];

                            while (readBytes != -1) {
                                readBytes = stream.read(audioBuffer, 0, audioBuffer.length);
                                if (readBytes >= 0) {
                                    audioSource.write(audioBuffer, 0, readBytes);
                                }
                            }

                            audioSource.drain();
                            audioSource.stop();
                            audioSource.close();

                            System.out.println("Finished playing : " + file.getName());
                        }
                        catch (UnsupportedAudioFileException e)
                        {
                            System.out.println("Unsupported File Format");
                        }
                        catch (IOException e)
                        {
                            System.out.println("ERROR :(");
                        }
                        catch (LineUnavailableException e) {
                            System.out.println("ERROR :(");
                        }

                    }
                });

                thread.start();
            }
        });

        player.add(play);

        player.setSize(500,120);
        player.setLayout(null);
        player.setVisible(true);

        player.setMinimumSize(new Dimension(480, 100));

        player.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        String strLine;

        DefaultListModel listModel = new DefaultListModel();

        JList<String> list = new JList<>( listModel );

        chooser.add(new JScrollPane(list));

        list.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent arg0) {
                if (!arg0.getValueIsAdjusting()) {
                    temp=list.getSelectedValue().toString();
                    System.out.println("temp="+temp);
                    File newfile = new File(temp);
                    file=newfile;
                }
            }
        });
        
        try {
            BufferedReader br = new BufferedReader(new FileReader("path.txt"));
            int n=0;

            while ((strLine = br.readLine()) != null)
            {
                selections[n] = strLine;
                n++;
                listModel.addElement(strLine);
                System.out.println(strLine);
            }

        } catch (Exception e) {
        }

        chooser.setLayout(new FlowLayout());
        chooser.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        chooser.pack();
        chooser.setVisible(true);
        player.getContentPane().setBackground(new Color(53, 75, 109));
    }

    public static void main(String[] args)
    {
        guii();
    }
}



