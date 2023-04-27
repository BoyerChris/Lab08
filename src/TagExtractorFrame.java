import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static java.nio.file.StandardOpenOption.CREATE;


public class TagExtractorFrame extends JFrame {
    JPanel mainPnl, midPnl, quitPnl;
    JButton quitBtn;
    JTextArea tagTA;
    JScrollPane tagPane;
    Set<String> et;



    public TagExtractorFrame() {
        createGUI();
        setTitle("Invoice");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);


    }

    private void createGUI() {
        mainPnl = new JPanel();
        midPnl = new JPanel();
        quitPnl = new JPanel();


        mainPnl.setLayout(new BorderLayout());

        mainPnl.add(midPnl, BorderLayout.CENTER);
        mainPnl.add(quitPnl, BorderLayout.SOUTH);

        add(mainPnl);

        createMidPnl();
        createQuitPnl();

        et = runTagStop();
        runTag();
    }


    private void createMidPnl()
    {
        tagTA = new JTextArea(25, 50);
        tagPane = new JScrollPane(tagTA);

        midPnl.setBorder(new TitledBorder(new EtchedBorder(), "Tag Screen"));
        midPnl.add(tagPane);
    }

    private void createQuitPnl()
    {
        quitBtn = new JButton("Quit");
        quitBtn.setPreferredSize(new Dimension(150, 50));

        quitBtn.addActionListener((ActionEvent ae) ->
        {
            if(JOptionPane.showConfirmDialog(null, "Are you sure?", "Exit", JOptionPane.YES_NO_OPTION) == 0)
            {
                System.exit(0);
            }
        });
        quitPnl.add(quitBtn);
    }



    private Set<String> runTagStop() {

        JFileChooser chooser = new JFileChooser();
        File selectedFile;
        String data = "";


        Set<String> frequencies = new HashSet<>();


        JOptionPane.showMessageDialog(null,"Please Input The Filter File");

        try {
            File PersonData = new File(System.getProperty("user.dir"));
            chooser.setCurrentDirectory(PersonData);

            if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                selectedFile = chooser.getSelectedFile();
                Path file = selectedFile.toPath();

                InputStream in =
                        new BufferedInputStream(Files.newInputStream(file, CREATE));
                BufferedReader reader =
                        new BufferedReader(new InputStreamReader(in));



                while (reader.ready())
                {
                    data = reader.readLine();
                    String[] arrOfdata = data.split(" ");


                    for (int i = 0; i < arrOfdata.length; i++) {
                        String word = clean(arrOfdata[i]);

                        frequencies.add(word);
                    }
                }
                reader.close();
                return frequencies;
            }
        } catch (FileNotFoundException e) {
            tagTA.append("file not found");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return frequencies;
    }

    private void runTag() {

            JFileChooser chooser = new JFileChooser();
            File selectedFile;
            String data = "";

            JOptionPane.showMessageDialog(null,"Please Input the text file to be filtered");


            Map<String, Integer> frequencies = new TreeMap<>();


            try {
                File PersonData = new File(System.getProperty("user.dir"));
                chooser.setCurrentDirectory(PersonData);

                if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    selectedFile = chooser.getSelectedFile();
                    Path file = selectedFile.toPath();

                    InputStream in =
                            new BufferedInputStream(Files.newInputStream(file, CREATE));
                    BufferedReader reader =
                            new BufferedReader(new InputStreamReader(in));



                    while (reader.ready())
                    {
                        data = reader.readLine();
                        String[] arrOfdata = data.split(" ");


                        for (int i = 0; i < arrOfdata.length; i++) {
                            String word = clean(arrOfdata[i]);

                            if (et.contains(word))
                            {

                            }
                            else
                            {
                                Integer count = frequencies.get(word);

                                if (count == null) {
                                    count = 1;
                                } else {
                                    count = count + 1;
                                }
                                frequencies.put(word, count);
                            }
                        }
                    }
                    for (String key : frequencies.keySet())
                    {
                        tagTA.append(String.format("%-20s%10d%n", key, frequencies.get(key)));

                    }

                    reader.close();
                }
            } catch (FileNotFoundException e) {
                tagTA.append("file not found");
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }




        File PersonData = new File(System.getProperty("user.dir"));
        Path file = Paths.get (PersonData.getPath() + "\\src\\Tags.txt");

        try
        {
            OutputStream out =
                    new BufferedOutputStream(Files.newOutputStream(file, CREATE));
            BufferedWriter writer =
                    new BufferedWriter(new OutputStreamWriter(out));


            for (String key : frequencies.keySet()) {
                writer.write(String.format("%-20s%10d%n", key, frequencies.get(key)));
                writer.newLine();
            }
            writer.close();

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }


        }

    public static String clean (String s)
        {
            String r = "";
            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);
                if (Character.isLetter(c)) {
                    r = r + c;
                }
            }
            return r.toLowerCase();
        }
}


