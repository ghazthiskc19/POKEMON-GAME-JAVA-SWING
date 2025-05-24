
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import javax.swing.*;
import javax.swing.border.Border;

public class Belajar extends JFrame {

    Belajar() {
        ImageIcon plate = new ImageIcon("./Assets/Pokemon/Pikachu/raichu_front3.gif");
        Border border = BorderFactory.createLineBorder(Color.BLACK, 5);

        JLabel plateWrapper = new JLabel("Tempat Pokemon");
        plateWrapper.setIcon(plate);
        // posisi teks
        plateWrapper.setHorizontalTextPosition(JLabel.CENTER);
        plateWrapper.setVerticalTextPosition(JLabel.BOTTOM);
        plateWrapper.setBackground(Color.black);
        plateWrapper.setForeground(new Color(123, 50, 255)); // atur warna tulisan
        plateWrapper.setFont(new Font("MV Boli", Font.PLAIN, 20));
        plateWrapper.setIconTextGap(-10); // gap text image
        plateWrapper.setOpaque(true); // bg color
        plateWrapper.setBackground(Color.red);
        plateWrapper.setBorder(border);

        // ubah teks dan image sesuai dengan posisi (icon + text) di dalam label
        // plateWrapper.setVerticalAlignment(JLabel.TOP);
        plateWrapper.setHorizontalAlignment(JLabel.CENTER);
        // x and y pos, dan ukurannya
        // plateWrapper.setBounds(10, 10, 250, 250);

        setTitle("Coba");
        // setSize(420, 420);
        // setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setForeground(Color.BLACK);
        add(plateWrapper);
        pack(); // nanti biar
    }

    public static void main(String[] args) {
        // new Belajar();
        // new belajarJPanel();
        // new belajarButtonSwing();
        // new BelajarBorderLayout();
        // new BelajarFlow();
        // new BelajarGridLayout();
        // new BelajarJLayeredPane();
        // new BelajarOpenNewWindow();
        // new BelajarJOptionPane();
        // new BelajarTextField();
        // new BelajarCheckBoxes();
        // new BelajarRadioButton();
        // new BelajarComboBoxes();
        // new BelajarJSlider();
        // new BelajarJProgressBar();
        // new BelajarMenuBar();
        // new BelajarSelectFile();
        // new BelajarJChooseColor();
        // new BelajarKeyListener();
        new BelajarMouseListener();
    }
}

class belajarJPanel extends JFrame {

    public belajarJPanel() {

        ImageIcon icon = new ImageIcon("./Assets/Pokemon/Pikachu/raichu_front3.gif");
        JLabel label = new JLabel("Hi");
        label.setIcon(icon);
        // Dipake kalau layout --> BorderLayout
        // label.setVerticalAlignment(JLabel.BOTTOM);
        // label.setHorizontalAlignment(JLabel.RIGHT);
        label.setBounds(0, 0, 75, 75);

        JPanel bluePanel = new JPanel();
        bluePanel.setBackground(Color.blue);
        bluePanel.setBounds(0, 0, 200, 200);
        bluePanel.setLayout(null);

        JPanel redPanel = new JPanel();
        redPanel.setBackground(Color.red);
        redPanel.setBounds(200, 0, 200, 200);
        redPanel.setLayout(null);

        JPanel greenPanel = new JPanel();
        greenPanel.setBackground(Color.green);
        greenPanel.setBounds(0, 200, 400, 200);
        greenPanel.setLayout(null);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setSize(750, 750);
        greenPanel.add(label);
        add(bluePanel);
        add(redPanel);
        add(greenPanel);
        // panel itu pake flow layout, sedangkan defaultnya frame itu pake border layout
        setVisible(true);
    }
}

class belajarButtonSwing extends JFrame {
    JButton button;
    JLabel label;

    public belajarButtonSwing() {

        ImageIcon icon = new ImageIcon("./Assets/Pokemon/Pikachu/front.gif");
        ImageIcon icon2 = new ImageIcon("./Assets/Pokemon/Pikachu/raichu_front3.gif");

        button = new JButton("Hello Guys");
        label = new JLabel(icon2);
        label.setBounds(15, 150, 150, 150);
        label.setVisible(false);

        button.setFocusable(false);

        button.setBounds(50, 50, 200, 100);
        // Lambda expression
        button.addActionListener(e -> {
            System.out.println("Boo");
            button.setEnabled(false);
            label.setVisible(true);
        });
        button.setIcon(icon);
        button.setHorizontalTextPosition(JButton.CENTER);
        button.setVerticalTextPosition(JButton.BOTTOM);
        button.setFont(new Font("Comic Sans", Font.BOLD, 20));
        button.setIconTextGap(-10);
        button.setForeground(Color.red);
        button.setBackground(Color.gray);
        button.setBorder(BorderFactory.createLineBorder(Color.black, 3));

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setSize(500, 500);
        setVisible(true);
        add(button);
        add(label);
    }
}

class BelajarBorderLayout extends JFrame {
    public BelajarBorderLayout() {

        // BorderLayout bisa naruh component di 5 area : NORTH, SOUT, WEST, EAST, CENTER
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        // Kita bisa kasih margin untuk horizontal dan vertical gap.
        setLayout(new BorderLayout(10, 10));
        setVisible(true);

        // layout pake FlowLayout
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        JPanel panel3 = new JPanel();
        JPanel panel4 = new JPanel();
        JPanel panel5 = new JPanel();

        panel1.setBackground(Color.red);
        panel2.setBackground(Color.green);
        panel3.setBackground(Color.cyan);
        panel4.setBackground(Color.magenta);
        panel5.setBackground(Color.blue);

        panel1.setPreferredSize(new Dimension(100, 100));
        panel2.setPreferredSize(new Dimension(100, 100));
        panel3.setPreferredSize(new Dimension(100, 100));
        panel4.setPreferredSize(new Dimension(100, 100));
        panel5.setPreferredSize(new Dimension(100, 100));

        // ________________________SUB PANELS__________________________
        JPanel panel6 = new JPanel();
        JPanel panel7 = new JPanel();
        JPanel panel8 = new JPanel();
        JPanel panel9 = new JPanel();
        JPanel panel10 = new JPanel();

        panel6.setBackground(Color.black);
        panel7.setBackground(Color.darkGray);
        panel8.setBackground(Color.gray);
        panel9.setBackground(Color.lightGray);
        panel10.setBackground(Color.white);

        panel5.setLayout(new BorderLayout());

        panel6.setPreferredSize(new Dimension(50, 50));
        panel7.setPreferredSize(new Dimension(50, 50));
        panel8.setPreferredSize(new Dimension(50, 50));
        panel9.setPreferredSize(new Dimension(50, 50));
        panel10.setPreferredSize(new Dimension(50, 50));

        panel5.add(panel6, BorderLayout.NORTH);
        panel5.add(panel7, BorderLayout.SOUTH);
        panel5.add(panel8, BorderLayout.WEST);
        panel5.add(panel9, BorderLayout.EAST);
        panel5.add(panel10, BorderLayout.CENTER);

        add(panel1, BorderLayout.NORTH);
        add(panel2, BorderLayout.WEST);
        add(panel3, BorderLayout.EAST);
        add(panel4, BorderLayout.SOUTH);
        add(panel5, BorderLayout.CENTER);
    }
}

class BelajarFlow extends JFrame {

    public BelajarFlow() {
        // Sama kaya flexbox.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);

        // FlowLayout.LEADING , diem di TOP LEFT
        // FlowLayout.TRAILING, diem di TOP RIGHT
        // default --> FlowLayout.CENTER
        // bisa kasih vertical dan horizontal gap
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(250, 250));
        panel.setBackground(Color.lightGray);
        panel.setLayout(new FlowLayout());

        panel.add(new JButton("1"));
        panel.add(new JButton("2"));
        panel.add(new JButton("3"));
        panel.add(new JButton("4"));
        panel.add(new JButton("5"));
        panel.add(new JButton("6"));
        panel.add(new JButton("7"));
        panel.add(new JButton("8"));
        panel.add(new JButton("9"));

        add(panel);
        setVisible(true);
    }
}

class BelajarGridLayout extends JFrame {

    public BelajarGridLayout() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        // Parameter : rows, cols, horizontal dan vertical gapm
        setLayout(new GridLayout(3, 3, 5, 5));

        add(new Button("1"));
        add(new Button("2"));
        add(new Button("3"));
        add(new Button("4"));
        add(new Button("5"));
        add(new Button("6"));
        add(new Button("7"));
        add(new Button("8"));
        add(new Button("9"));
        add(new Button("10"));
        setVisible(true);
    }
}

class BelajarJLayeredPane extends JFrame {
    public BelajarJLayeredPane() {
        // Swing container, ada depth, dan z-index bro, sama seperti JPanel, tapi stiap
        // element bisa di stack satu sama lain dan bisa diatur z-indexnya.
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, 500, 500);

        JLabel label1 = new JLabel();
        label1.setOpaque(true);
        label1.setBackground(Color.red);
        label1.setBounds(50, 50, 200, 200);

        JLabel label2 = new JLabel();
        label2.setOpaque(true);
        label2.setBackground(Color.green);
        label2.setBounds(100, 100, 200, 200);

        JLabel label3 = new JLabel();
        label3.setOpaque(true);
        label3.setBackground(Color.blue);
        label3.setBounds(150, 150, 200, 200);

        layeredPane.add(label1, Integer.valueOf(0));
        layeredPane.add(label2, Integer.valueOf(WIDTH));
        layeredPane.add(label3, Integer.valueOf(1));

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(500, 500));
        setLayout(null);

        add(layeredPane);
        setVisible(true);
    }
}

class BelajarOpenNewWindow {

    public BelajarOpenNewWindow() {
        LaunchPage launchPage = new LaunchPage();
    }
}

class LaunchPage extends JFrame {
    JButton myButton = new JButton("New Windows");

    public LaunchPage() {

        myButton.setBounds(100, 160, 150, 60);
        myButton.setFocusable(false);
        myButton.addActionListener(e -> {
            NewWindow myWindow = new NewWindow();
            dispose();
            // biar window yang sekarnag close, dibuka yang baru
        });
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        setLayout(null);

        add(myButton);
        setVisible(true);
    }
}

class NewWindow extends JFrame {
    public NewWindow() {
        JLabel label = new JLabel("Hello guys this is New Windows");
        label.setFont(new Font("Arial Narrow", Font.BOLD, 20));
        label.setHorizontalAlignment(JLabel.CENTER);

        JLabel label2 = new JLabel("Hello guys");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // setLayout(new FlowLayout(FlowLayout.));
        setSize(420, 420);
        add(label);
        add(label2);
        setVisible(true);
    }
}

class BelajarJOptionPane extends JFrame {

    public BelajarJOptionPane() {
        // JOptionPane.showMessageDialog(null, "YOU OPEN THIS!!!!", "RAWR",
        // JOptionPane.PLAIN_MESSAGE);
        // JOptionPane.showMessageDialog(null, "YOU OPEN THIS!!!!", "RAWR",
        // JOptionPane.INFORMATION_MESSAGE);
        // JOptionPane.showMessageDialog(null, "YOU OPEN THIS!!!!", "RAWR",
        // JOptionPane.QUESTION_MESSAGE);
        // JOptionPane.showMessageDialog(null, "YOU OPEN THIS!!!!", "RAWR",
        // JOptionPane.WARNING_MESSAGE);
        // JOptionPane.showMessageDialog(null, "YOU OPEN THIS!!!!", "RAWR",
        // JOptionPane.ERROR_MESSAGE);
        // System.out.println(
        // JOptionPane.showConfirmDialog(null, "Are you sure?", "REAL!!",
        // JOptionPane.YES_NO_CANCEL_OPTION));
        // String name = JOptionPane.showInputDialog("What do you want?");
        // System.out.println(name);

        String[] responses = { "No", "Perhaps, TY", "Maybe No" };
        ImageIcon icon = new ImageIcon("./Assets/Pokemon/Pikachu/front.gif");
        JOptionPane.showOptionDialog(null, "You are handsome",
                "Secret Message",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.ERROR_MESSAGE,
                icon,
                responses,
                1);
        // setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // setSize(500, 500);
        // setVisible(true);
    }
}

class BelajarTextField extends JFrame {

    public BelajarTextField() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        JTextField textField = new JTextField();
        textField.setPreferredSize(new Dimension(250, 40));
        textField.setFont(new Font("Arial Narrow", Font.BOLD, 20));
        textField.setForeground(Color.lightGray);
        textField.setBackground(Color.red);
        textField.setCaretColor(Color.white);

        JButton button = new JButton("Submit");
        button.addActionListener(e -> {
            System.out.println(textField.getText());
            button.setEnabled(false);
            textField.setEditable(false);
        });

        add(button);
        add(textField);
        pack();
        setVisible(true);
    }
}

class BelajarCheckBoxes extends JFrame {

    public BelajarCheckBoxes() {
        ImageIcon front = new ImageIcon("./Assets/Pokemon/Pikachu/front.gif");
        ImageIcon back = new ImageIcon("./Assets/Pokemon/Pikachu/back.gif");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        JCheckBox checkBox = new JCheckBox("I'm not a robot");
        checkBox.setFocusable(false);
        checkBox.setFont(new Font("Consolas", Font.PLAIN, 25));
        checkBox.setPreferredSize(new Dimension(100, 100));
        checkBox.setIcon(back);
        checkBox.setSelectedIcon(front);

        JButton button = new JButton("Submit");
        button.addActionListener(e -> {

        });

        add(button);
        add(checkBox);
        pack();
        setVisible(true);
    }
}

class BelajarRadioButton extends JFrame {

    public BelajarRadioButton() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        JRadioButton pizzaButton = new JRadioButton("pizza");
        JRadioButton hamButton = new JRadioButton("hamburger");
        JRadioButton hotdogButton = new JRadioButton("hotdog");
        pizzaButton.addActionListener(e -> {
            System.out.println("Pizza");
        });
        hamButton.addActionListener(e -> {
            System.out.println("Hamburger");
        });
        hotdogButton.addActionListener(e -> {
            System.out.println("Hotdog");
        });

        pizzaButton.setFocusable(false);
        hamButton.setFocusable(false);
        hotdogButton.setFocusable(false);
        ButtonGroup group = new ButtonGroup();
        group.add(pizzaButton);
        group.add(hamButton);
        group.add(hotdogButton);

        add(pizzaButton);
        add(hamButton);
        add(hotdogButton);
        pack();
        setVisible(true);
    }
}

class BelajarComboBoxes extends JFrame {
    public BelajarComboBoxes() {
        // editable list atau dropdown list weh anu kitu dah pokokna mah
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        String[] animals2 = { "Lion", "Dog", "Dog2" };
        Integer[] animals = { 1, 2, 3 };
        JComboBox<Integer> comboBox = new JComboBox<>(animals);
        // comboBox.setEditable(true);
        // comboBox.getItemCount();
        // comboBox.addItem("Horse");
        // comboBox.insertItemAt("Pig", 0);
        // comboBox.setSelectedIndex(0);
        // comboBox.removeItem("Dog2");
        // comboBox.removeItemAt(0);
        // comboBox.removeAllItems();

        comboBox.addActionListener(e -> {
            System.out.println(comboBox.getSelectedItem());
        });
        add(comboBox);
        pack();
        setVisible(true);
    }
}

class BelajarJSlider extends JFrame {

    public BelajarJSlider() {
        setTitle("Slider Demo");
        JPanel panel = new JPanel();
        JLabel label = new JLabel();

        // min, max, starting point
        JSlider slider = new JSlider(0, 100, 50);
        slider.setPreferredSize(new Dimension(400, 200));
        slider.setPaintTicks(true);
        slider.setMinorTickSpacing(10);

        slider.setPaintTrack(true);
        slider.setMajorTickSpacing(25);

        slider.setPaintLabels(true);
        slider.setFont(new Font("Arial Narrow", Font.PLAIN, 12));
        slider.setOrientation(SwingConstants.VERTICAL);

        label.setText("*C = " + slider.getValue());
        slider.addChangeListener(e -> {
            label.setText("*C = " + slider.getValue());
        });

        panel.add(slider);
        panel.add(label);
        add(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setVisible(true);
    }
}

class BelajarJProgressBar extends JFrame {

    public BelajarJProgressBar() {
        ImageIcon front = new ImageIcon("./Assets/Pokemon/Pikachu/front.gif");
        JProgressBar progressBar = new JProgressBar(0, 500, 250);
        progressBar.setValue(0);
        progressBar.setBounds(0, 0, 420, 50);
        progressBar.setStringPainted(true); // add percentage to progress bar
        progressBar.setFont(new Font("Times New Roman", Font.BOLD, 10));
        progressBar.setForeground(Color.red);
        progressBar.setBackground(Color.black);

        add(progressBar);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setSize(420, 420);
        setVisible(true);

        int count = 500;
        while (count > 0) {
            progressBar.setValue(count);
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            count -= 5;
        }
        progressBar.setString("Done!!!");
    }
}

class BelajarMenuBar extends JFrame {
    BelajarMenuBar() {
        ImageIcon back = new ImageIcon("./Assets/Pokemon/Pikachu/back.gif");
        ImageIcon front = new ImageIcon("./Assets/Pokemon/Pikachu/front.gif");

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenu editMenu = new JMenu("Edit");
        JMenu helpMenu = new JMenu("Help");

        JMenuItem saveFile = new JMenuItem("Save file");
        JMenuItem loadFile = new JMenuItem("Load file");
        JMenuItem exitApp = new JMenuItem("Exit Apps");
        saveFile.setIcon(front);
        loadFile.setIcon(back);
        exitApp.setIcon(front);

        // Lambda expression
        saveFile.addActionListener(e -> {
            System.out.println("You save a file");
        });
        loadFile.addActionListener(e -> {
            System.out.println("You load a file");
        });
        exitApp.addActionListener(e -> {
            System.out.println("You exit the app ");
            System.exit(0);
        });

        fileMenu.setMnemonic(KeyEvent.VK_F); // harus pake alt + F
        saveFile.setMnemonic(KeyEvent.VK_S); // S for load
        loadFile.setMnemonic(KeyEvent.VK_L); // L for load
        exitApp.setMnemonic(KeyEvent.VK_T); // S for load

        fileMenu.add(saveFile);
        fileMenu.add(loadFile);
        fileMenu.add(exitApp);

        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        setLayout(new FlowLayout());
        setVisible(true);
    }
}

class BelajarSelectFile extends JFrame {

    public BelajarSelectFile() {
        // JFileChooser = Opening and saving files
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton button = new JButton("Save button");
        button.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            // int value = fileChooser.showOpenDialog(null); // select file to open retrun
            fileChooser.setCurrentDirectory(new File(". "));
            int res = fileChooser.showSaveDialog(null);

            if (res == JFileChooser.APPROVE_OPTION) {
                File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                System.out.println(file);
            }

        });
        add(button);
        pack();
        setVisible(true);
    }
}

class BelajarJChooseColor extends JFrame {
    public BelajarJChooseColor() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        JButton button = new JButton("Pick Color");
        JLabel label = new JLabel("This is some text!!");
        label.setOpaque(true);
        label.setBackground(Color.white);
        label.setFont(new Font("Times New Roman", Font.BOLD, 50));

        button.addActionListener(e -> {
            JColorChooser colorChooser = new JColorChooser();
            Color color = JColorChooser.showDialog(null, "Selet a color", Color.black);
            label.setForeground(color);
        });

        add(button);
        add(label);
        pack();
        setVisible(true);
    }
}

class BelajarKeyListener extends JFrame implements KeyListener {
    JLabel label;

    public BelajarKeyListener() {

        label = new JLabel();
        label.setBounds(0, 0, 100, 100);
        label.setOpaque(true);
        label.setBackground(Color.red);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        addKeyListener(this);
        getContentPane().setBackground(Color.blue);
        setLayout(null);
        add(label);
        setVisible(true);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // when key typed, use keyChar, char output
        switch (e.getKeyChar()) {
            case 'a':
                label.setLocation(label.getX() - 2, label.getY());
                break;
            case 'w':
                label.setLocation(label.getX(), label.getY() - 2);
                break;
            case 'd':
                label.setLocation(label.getX() + 2, label.getY());
                break;
            case 's':
                label.setLocation(label.getX(), label.getY() + 2);
                break;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // key is press down, use keyCode, int output
        switch (e.getKeyChar()) {
            case 'a':
                label.setLocation(label.getX() - 2, label.getY());
                break;
            case 'w':
                label.setLocation(label.getX(), label.getY() - 2);
                break;
            case 'd':
                label.setLocation(label.getX() + 2, label.getY());
                break;
            case 's':
                label.setLocation(label.getX(), label.getY() + 2);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // called when button is released
    }
}

class BelajarMouseListener extends JFrame implements MouseListener {
    JLabel label;

    public BelajarMouseListener() {
        label = new JLabel();
        label.setBounds(0, 0, 100, 100);
        label.setOpaque(true);
        label.setBackground(Color.red);
        label.addMouseListener(this);

        add(label);
        // addMouseListener(this);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setLayout(null);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // press and released a component
        // System.out.println("You click the mouse");
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // ketika mouse di press a component
        System.out.println("You press the mouse");
        label.setBackground(Color.YELLOW);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // mouse released a component
        // System.out.println("You released the mouse");
        label.setBackground(Color.green);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        label.setBackground(Color.blue);
        // mouse entered an area on the component
        // System.out.println("You entered / hover the mouse");
    }

    @Override
    public void mouseExited(MouseEvent e) {
        label.setBackground(Color.red);
        // when exit a component
    }
}
