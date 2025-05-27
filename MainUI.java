import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;

public class MainUI {
    public static void main(String[] args) {
        new StartMenuUI();
    }
}

// ...existing code...

class BackgroundPanel extends JPanel {
    private Image backgroundImage;

    public BackgroundPanel(String path) {
        try {
            backgroundImage = new ImageIcon(path).getImage();
        } catch (Exception e) {
            System.out.println("Gagal load gambar: " + path);
        }
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Resize gambar agar memenuhi panel
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}

class DarkBackgroundPanel extends JPanel {
    private Image backgroundImage;

    public DarkBackgroundPanel(String path) {
        try {
            ImageIcon icon = new ImageIcon(path);
            Image img = icon.getImage();

            // Konversi ke BufferedImage
            BufferedImage buffered = new BufferedImage(
                    img.getWidth(null),
                    img.getHeight(null),
                    BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = buffered.createGraphics();
            g2d.drawImage(img, 0, 0, null);
            g2d.dispose();

            // Gelapkan gambar (misal 0.4f = 40% brightness)
            RescaleOp op = new RescaleOp(0.4f, 0, null);
            buffered = op.filter(buffered, null);

            backgroundImage = buffered;
        } catch (Exception e) {
            System.out.println("Gagal load gambar: " + path);
        }
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}

class StartMenuUI extends JFrame {
    MusicPlayer musicPlayer;
    List<Pokemon> allPokemons;
    ArrayList<Pokemon> availablePokemon = new ArrayList<>();
    Pokemon playerPokemon;
    Pokemon enemyPokemon;
    FadeEffectPanel fadeEffectPanel;
    CardLayout cardLayout = new CardLayout();
    JPanel wrapperPanel;
    // _____________________________________-
    JPanel startMenuPanel;
    JPanel settingMenuPanel;

    // _____________________________________-
    // tampilin stats pemain di sisi kanan, arahin ke arena dan shop di sisi kiri
    JPanel mainMenuPanel;
    JPanel arenaPanel;
    JPanel pokemonSelectionPanel;
    JPanel pokemonShopPanel;

    // Global Variable
    Color textColor = new Color(21, 22, 21);
    Color textColor2 = new Color(255, 255, 255);
    Color backgroundColor = new Color(181, 163, 91);
    Font headerFont;
    Font paragraphFont;

    StartMenuUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        setLocationRelativeTo(null);
        setAssets();
        setJPanel();
        setStartMenuPanel();
        setSettingMenuPanel();
        setMainMenuPanel();
        setPokemonSelectionPanel();
        setShopPanel();
        add(wrapperPanel);
        setResizable(false);
        fadeEffectPanel = new FadeEffectPanel();
        fadeEffectPanel.setFadeColor(Color.BLACK);
        fadeEffectPanel.setCurrentAlpha(1.0f);
        setGlassPane(fadeEffectPanel);
        fadeEffectPanel.setVisible(true);

        musicPlayer.playMusic(MusicPlayer.MusicType.START_MENU);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (musicPlayer != null) {
                    musicPlayer.stopMusic();
                }
                super.windowClosing(e);
            }
        });

        Runnable whenFadeComplete = () -> {
        };
        setVisible(true);
        fadeEffectPanel.startFade(0.0f, 1500, whenFadeComplete);
    }

    private void setAssets() {
        try {
            File fontFile1 = new File("Assets/Font/Jersey10-Regular.ttf");
            File fontFile2 = new File("Assets/Font/PixelifySans-Medium.ttf");
            headerFont = Font.createFont(Font.TRUETYPE_FONT, fontFile1);
            paragraphFont = Font.createFont(Font.TRUETYPE_FONT, fontFile2);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(headerFont);
            ge.registerFont(paragraphFont);
        } catch (IOException | java.awt.FontFormatException e) {
            e.printStackTrace();
        }
        musicPlayer = new MusicPlayer(
                "./Assets/Sound/BGM/start_menu_music.wav",
                "./Assets/Sound/BGM/main_menu_music.wav",
                "./Assets/Sound/BGM/arena_music.wav");
    }

    private void setJPanel() {
        wrapperPanel = new JPanel(cardLayout);

        startMenuPanel = new BackgroundPanel("./Assets/bg-start.jpeg");
        settingMenuPanel = new DarkBackgroundPanel("./Assets/bg-start.jpeg");
        mainMenuPanel = new BackgroundPanel("./Assets/bg-main.jpg");
        pokemonSelectionPanel = new BackgroundPanel("./Assets/bg-selection.jpg");
        // pokemonSelectionPanel = new JPanel();
        pokemonSelectionPanel.setBackground(Color.RED);
        arenaPanel = new BackgroundPanel("./Assets/bg-selection.jpg");
        pokemonShopPanel = new JPanel();

        wrapperPanel.add(startMenuPanel, "StartMenu");
        wrapperPanel.add(settingMenuPanel, "SettingMenu");
        wrapperPanel.add(mainMenuPanel, "MainMenu");
        wrapperPanel.add(pokemonSelectionPanel, "PokemonSelection");
        wrapperPanel.add(arenaPanel, "Arena");
        wrapperPanel.add(pokemonShopPanel, "Shop");
    }

    private void setStartMenuPanel() {
        startMenuPanel.setLayout(new BoxLayout(startMenuPanel, BoxLayout.Y_AXIS));
        // Bikin tombol-tombol
        JButton startButton = new JButton("Start Game");
        JButton settingButton = new JButton("Settings");
        JButton exitButton = new JButton("Exit");
        JButton[] buttons = { startButton, settingButton, exitButton };
        editButtonAll(buttons);
        editButtonStart(buttons);

        JPanel buttonGroup = new JPanel();
        buttonGroup.setLayout(new BoxLayout(buttonGroup, BoxLayout.Y_AXIS));
        buttonGroup.setOpaque(false);

        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        settingButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        buttonGroup.add(startButton);
        buttonGroup.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonGroup.add(settingButton);
        buttonGroup.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonGroup.add(exitButton);

        String clickSfxPath = "./Assets/Sound/SFX/Button - Sound effect.wav";

        // Set ActionListener
        startButton.addActionListener(e -> {
            SFXPlayer.playSound(clickSfxPath);
            fadeEffectPanel.setFadeColor(Color.black);
            fadeEffectPanel.setCurrentAlpha(0.0f);
            fadeEffectPanel.setVisible(true);
            Runnable H_afterFadeOut = () -> {
                cardLayout.show(wrapperPanel, "MainMenu");
                musicPlayer.playMusic(MusicPlayer.MusicType.MAIN_MENU);
                Runnable H_afterFadeIn = () -> {
                };
                fadeEffectPanel.startFade(0.0f, 700, H_afterFadeIn);
            };
            fadeEffectPanel.startFade(1.0f, 700, H_afterFadeOut);
        });
        // ...existing code...
        settingButton.addActionListener(e -> {
            SFXPlayer.playSound(clickSfxPath);
            fadeEffectPanel.setFadeColor(Color.black);
            fadeEffectPanel.setCurrentAlpha(0.0f);
            fadeEffectPanel.setVisible(true);
            Runnable H_afterFadeOut = () -> {
                cardLayout.show(wrapperPanel, "SettingMenu");
                Runnable H_afterFadeIn = () -> {
                };
                fadeEffectPanel.startFade(0.0f, 200, H_afterFadeIn);
            };
            fadeEffectPanel.startFade(1.0f, 500, H_afterFadeOut);
        });
        // ...existing code...

        exitButton.addActionListener(e -> {
            SFXPlayer.playSound(clickSfxPath);
            fadeEffectPanel.setFadeColor(Color.black);
            fadeEffectPanel.setCurrentAlpha(0.0f);
            fadeEffectPanel.setVisible(true);
            Runnable H_afterFadeOut = () -> {
                if (musicPlayer != null) {
                    musicPlayer.stopMusic();
                }
                System.exit(0);
                Runnable H_afterFadeIn = () -> {
                };
                fadeEffectPanel.startFade(0.0f, 700, H_afterFadeIn);
            };
            fadeEffectPanel.startFade(1.0f, 700, H_afterFadeOut);
        });

        // Glue atas bawah untuk vertical center
        startMenuPanel.add(Box.createVerticalStrut(250));
        startMenuPanel.add(Box.createVerticalGlue());
        startMenuPanel.add(buttonGroup);
        startMenuPanel.add(Box.createVerticalGlue());
    }

    private void setMainMenuPanel() {
        mainMenuPanel.setLayout(new GridLayout(1, 2, 50, 50));
        JPanel left = new JPanel();
        JPanel right = new JPanel();
        left.setOpaque(false);
        right.setOpaque(false);

        setLeftMainMenu(left);
        setRightMainMenu(right);

        mainMenuPanel.add(left);
        mainMenuPanel.add(right);
    }

    private void setLeftMainMenu(JPanel left) {
        left.setLayout(null);

        left.setBackground(new Color(0, 0, 0, 127)); // alpha 127 = 50%
        left.setOpaque(true);
        // left.add(Box.createVerticalGlue());

        String clickSfxPath = "./Assets/Sound/SFX/Button - Sound effect.wav";

        JLabel playerName1 = new JLabel("Halo, ");
        JLabel playerName2 = new JLabel("Siap bertarung?");
        JLabel currentMoney = new JLabel("Uang yang dimiliki : 0");
        JLabel currentCountPokemon = new JLabel("Jumlah Pokemon yang dimiliki : 0");
        JButton enterArena = new JButton("Let's Fight");
        JButton enterShop = new JButton("Shop");
        JButton exitToStartMenu = new JButton("Exit");

        JLabel[] Allteks = { playerName1, playerName2, currentMoney, currentCountPokemon };
        JButton[] buttons = { enterArena, enterShop, exitToStartMenu };
        editButtonAll(buttons);
        editButtonMain(buttons);

        for (JLabel teks : Allteks) {
            left.add(teks);
            teks.setForeground(Color.white);
            teks.setFont(headerFont);
        }
        for (JButton jButton : buttons) {
            left.add(jButton);
            jButton.setForeground(Color.white);
        }

        playerName1.setBounds(20, 0, 350, 150);
        playerName2.setBounds(20, 30, 350, 150);
        currentMoney.setBounds(20, 30, 250, 250);
        currentCountPokemon.setBounds(20, 50, 250, 250);
        enterArena.setBounds(50, 250, 250, 50);
        enterShop.setBounds(50, 325, 250, 50);
        exitToStartMenu.setBounds(50, 400, 250, 50);
        playerName1.setFont(headerFont.deriveFont(30f));
        playerName2.setFont(headerFont.deriveFont(50f));
        currentMoney.setFont(headerFont.deriveFont(18f));
        currentCountPokemon.setFont(headerFont.deriveFont(18f));

        left.add(Box.createVerticalGlue());
        enterArena.addActionListener(e -> {
            SFXPlayer.playSound(clickSfxPath);
            fadeEffectPanel.setFadeColor(Color.black);
            fadeEffectPanel.setCurrentAlpha(0.0f);
            fadeEffectPanel.setVisible(true);
            Runnable H_afterFadeOut = () -> {
                cardLayout.show(wrapperPanel, "PokemonSelection");
                Runnable H_afterFadeIn = () -> {

                };
                fadeEffectPanel.startFade(0.0f, 700, H_afterFadeIn);
            };
            fadeEffectPanel.startFade(1.0f, 700, H_afterFadeOut);
        });
        enterShop.addActionListener(e -> {
            SFXPlayer.playSound(clickSfxPath);
            cardLayout.show(wrapperPanel, "Shop");
            musicPlayer.playMusic(MusicPlayer.MusicType.MAIN_MENU);
        });
        exitToStartMenu.addActionListener(e -> {
            SFXPlayer.playSound(clickSfxPath);
            fadeEffectPanel.setFadeColor(Color.black);
            fadeEffectPanel.setCurrentAlpha(0.0f);
            fadeEffectPanel.setVisible(true);
            Runnable H_afterFadeOut = () -> {
                musicPlayer.playMusic(MusicPlayer.MusicType.START_MENU);
                cardLayout.show(wrapperPanel, "StartMenu");

                Runnable H_afterFadeIn = () -> {

                };
                fadeEffectPanel.startFade(0.0f, 700, H_afterFadeIn);
            };
            fadeEffectPanel.startFade(1.0f, 700, H_afterFadeOut);
        });
    }

    private void setRightMainMenu(JPanel right) {
        right.setLayout(new OverlayLayout(right));

        JPanel pokemonWrapper = new JPanel(new FlowLayout());
        pokemonWrapper.setOpaque(false);
        int panelWidth = 300;
        int panelHeight = 300;
        JLabel imageBig = getScaledImageLabel("./Assets/Pokemon/Pikachu/raichu.png", panelWidth, panelHeight);
        imageBig.setText("Pokemon favorit adalah");
        imageBig.setFont(headerFont.deriveFont(18f));
        imageBig.setVerticalTextPosition(JLabel.BOTTOM);
        imageBig.setHorizontalTextPosition(JLabel.CENTER);
        imageBig.setForeground(Color.white);

        ImageIcon frontIcon = new ImageIcon("./Assets/Pokemon/Pikachu/raichu_front.gif");
        ImageIcon backIcon = new ImageIcon("./Assets/Pokemon/Pikachu/raichu_back.gif");
        JLabel image1 = new JLabel(frontIcon);
        JLabel image2 = new JLabel(backIcon);
        // JPanel pokemonWrapper = new JPanel(new BorderLayout());
        pokemonWrapper.add(image1);
        pokemonWrapper.add(image2);
        pokemonWrapper.add(imageBig);

        JPanel overlay = new JPanel();
        overlay.setBackground(new Color(0, 0, 0, 127));
        overlay.setOpaque(true);

        right.add(pokemonWrapper); // gambar paling bawah
        right.add(overlay); // transparan di atas gambar
    }

    private void setPokemonSelectionPanel() {
        allPokemons = PokemonFactory.createAllPokemons();
        pokemonSelectionPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        // gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;

        JPanel pokemonImage = new JPanel();
        JPanel pokemonButton = new JPanel();
        JPanel playExitButton = new JPanel();
        pokemonImage.setBackground(new Color(0, 0, 0, 127));
        pokemonButton.setOpaque(false);
        playExitButton.setOpaque(false);

        // Panel 1: Gambar (50%)
        gbc.gridy = 0;
        gbc.weighty = 0.5;
        pokemonSelectionPanel.add(pokemonImage, gbc);

        // Panel 2: Tombol Pokemon (30%)
        gbc.gridy = 1;
        gbc.weighty = 0.3;
        pokemonSelectionPanel.add(pokemonButton, gbc);

        // Panel 3: Play & Exit (20%)
        gbc.gridy = 2;
        gbc.weighty = 0.2;
        pokemonSelectionPanel.add(playExitButton, gbc);

        setPokemonButton(pokemonButton, pokemonImage);
        setPlayExitButton(playExitButton);
    }

    private void setPokemonImage(JPanel pokemonImage, Pokemon pokemon) {
        pokemonImage.removeAll();
        pokemonImage.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        // gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1;

        // Panel kiri (gambar)
        JPanel left = new JPanel(new GridBagLayout()); // Tengahin gambar
        JLabel label = getScaledImageLabel(pokemon.getImagePath(), 300, 300);
        label.setVerticalAlignment(JLabel.CENTER);
        left.add(label);

        gbc.gridx = 0;
        gbc.weightx = 0.7;
        pokemonImage.add(left, gbc);

        // Panel kanan (statistik)
        JPanel right = new JPanel();
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        right.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel nameLabel = new JLabel("Nama: " + pokemon.getName());
        JLabel typeLabel = new JLabel("Tipe: " + pokemon.getType());
        JLabel hpLabel = new JLabel("HP: " + pokemon.getMaxHp());
        JLabel attackLabel = new JLabel("Attack: " + pokemon.getAttack());
        JLabel defenseLabel = new JLabel("Defense: " + pokemon.getDefense());
        JLabel moveLabels = new JLabel("Moves:");
        JLabel[] labels = { nameLabel, typeLabel, hpLabel, attackLabel, defenseLabel, moveLabels };
        for (JLabel jLabel : labels) {
            jLabel.setFont(headerFont.deriveFont(20f));
            jLabel.setForeground(Color.white);
        }

        left.setOpaque(false);
        right.setOpaque(false);
        right.add(nameLabel);
        right.add(Box.createRigidArea(new Dimension(0, 5)));
        right.add(typeLabel);
        right.add(Box.createRigidArea(new Dimension(0, 5)));
        right.add(hpLabel);
        right.add(Box.createRigidArea(new Dimension(0, 5)));
        right.add(attackLabel);
        right.add(Box.createRigidArea(new Dimension(0, 5)));
        right.add(defenseLabel);
        right.add(Box.createRigidArea(new Dimension(0, 10)));
        right.add(moveLabels);
        for (Move move : pokemon.getMoves()) {
            JLabel moveLabel = new JLabel("- " + move.getName());
            moveLabel.setForeground(Color.white);
            moveLabel.setBackground(Color.red);
            right.add(moveLabel);
        }

        // Bungkus right panel dengan panel vertikal-center
        JPanel rightWrapper = new JPanel(new GridBagLayout());
        rightWrapper.setOpaque(false);
        GridBagConstraints gbcRight = new GridBagConstraints();
        gbcRight.anchor = GridBagConstraints.CENTER;
        gbcRight.gridy = 0;
        gbcRight.weighty = 1;
        gbcRight.fill = GridBagConstraints.NONE;
        rightWrapper.add(right, gbcRight);

        // Tambahkan ke pokemonImage
        gbc.gridx = 1;
        gbc.weightx = 0.3;
        pokemonImage.add(rightWrapper, gbc);

        // Refresh
        pokemonImage.revalidate();
        pokemonImage.repaint();
    }

    private void setPokemonButton(JPanel pokemonButton, JPanel pokemonImage) {
        int i = 1;
        for (Pokemon pokemon : allPokemons) {
            if (i % 3 == 1) {
                JButton btn = new JButton();
                ImageIcon icon = new ImageIcon(pokemon.getFrontGifPath());
                btn.setIcon(icon);
                btn.setFocusable(false);
                btn.setContentAreaFilled(false);
                btn.setBorderPainted(false);
                btn.setOpaque(false);
                btn.setPreferredSize(new Dimension(175, 175));
                pokemonButton.add(btn);
                btn.addActionListener(e -> {
                    setPokemonImage(pokemonImage, pokemon);
                    playerPokemon = pokemon;
                });
                availablePokemon.add(pokemon);
            }
            i++;
        }
    }

    private void setPlayExitButton(JPanel playExitButton) {
        String clickSfxPath = "./Assets/Sound/SFX/Button - Sound effect.wav";
        JButton fight = new JButton("Let's Go!!");
        JButton exit = new JButton("Exit");
        JButton[] buttons = { fight, exit };
        for (JButton jButton : buttons) {
            jButton.setForeground(Color.white);
        }
        editButtonAll(buttons);

        playExitButton.add(exit);
        playExitButton.add(fight);
        // Button actions
        fight.addActionListener(e -> {
            SFXPlayer.playSound(clickSfxPath);
            fadeEffectPanel.setFadeColor(Color.black);
            fadeEffectPanel.setCurrentAlpha(0.0f);
            fadeEffectPanel.setVisible(true);
            Runnable H_afterFadeOut = () -> {
                if (playerPokemon == null) {
                    JOptionPane.showMessageDialog(this, "Pilih Pokemon dulu, bro!", "Pokemon Belum Dipilih",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }
                cardLayout.show(wrapperPanel, "Arena");
                musicPlayer.playMusic(MusicPlayer.MusicType.ARENA);
                setArenaPanel();
                Runnable H_afterFadeIn = () -> {
                };
                fadeEffectPanel.startFade(0.0f, 700, H_afterFadeIn);
            };
            fadeEffectPanel.startFade(1.0f, 700, H_afterFadeOut);
        });
        exit.addActionListener(e -> {
            SFXPlayer.playSound(clickSfxPath);
            fadeEffectPanel.setFadeColor(Color.black);
            fadeEffectPanel.setCurrentAlpha(0.0f);
            fadeEffectPanel.setVisible(true);
            Runnable H_afterFadeOut = () -> {
                cardLayout.show(wrapperPanel, "MainMenu");
                musicPlayer.playMusic(MusicPlayer.MusicType.MAIN_MENU);
                Runnable H_afterFadeIn = () -> {
                };
                fadeEffectPanel.startFade(0.0f, 700, H_afterFadeIn);
            };
            fadeEffectPanel.startFade(1.0f, 700, H_afterFadeOut);
        });
    }

    private void setArenaPanel() {
        Random rand = new Random();
        enemyPokemon = availablePokemon.get(rand.nextInt(availablePokemon.size()));
        if (playerPokemon == null || enemyPokemon == null) {
            cardLayout.show(wrapperPanel, "PokemonSelection");
            return;
        }
        new BattleUI(playerPokemon, enemyPokemon, arenaPanel, headerFont, cardLayout, wrapperPanel, musicPlayer,
                fadeEffectPanel);
    }

    private void setSettingMenuPanel() {
        String clickSfxPath = "./Assets/Sound/SFX/Button - Sound effect.wav";
        settingMenuPanel.removeAll();
        settingMenuPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        if (!(settingMenuPanel instanceof BackgroundPanel)) {
            settingMenuPanel.setBackground(backgroundColor);
        }

        JLabel titeLable = new JLabel("Pengaturan volume");
        titeLable.setFont(headerFont.deriveFont(40f));
        titeLable.setForeground(textColor2);
        titeLable.setHorizontalAlignment(SwingConstants.CENTER);
        titeLable.setAlignmentX(Component.CENTER_ALIGNMENT);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 20, 40, 20);
        gbc.anchor = GridBagConstraints.CENTER;
        settingMenuPanel.add(titeLable, gbc);

        int initialVolume = 70;
        if (musicPlayer != null && musicPlayer.isVolumeControlSupported()) {
            initialVolume = musicPlayer.getVolumePercent();
        }

        JSlider volumeSlider = new JSlider(0, 100, initialVolume);
        volumeSlider.setMajorTickSpacing(20);
        volumeSlider.setMinorTickSpacing(5);
        volumeSlider.setPaintTicks(true);
        volumeSlider.setPaintLabels(true);
        volumeSlider.setOpaque(false);
        volumeSlider.setForeground(textColor2);
        volumeSlider.setPreferredSize(new Dimension(350, 60));
        volumeSlider.setAlignmentX(Component.CENTER_ALIGNMENT);
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 20, 20, 20);
        settingMenuPanel.add(volumeSlider, gbc);

        JLabel volumeLabel = new JLabel("Volume: " + initialVolume + "%");
        volumeLabel.setFont(headerFont.deriveFont(20f));
        volumeLabel.setForeground(textColor2);
        volumeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        volumeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 20, 20, 20);
        settingMenuPanel.add(volumeLabel, gbc);

        JButton backButton = new JButton("Kembali");
        backButton.setFont(headerFont.deriveFont(24f));
        backButton.setContentAreaFilled(false);
        backButton.setOpaque(false);
        backButton.setBorderPainted(true);
        backButton.setForeground(textColor2);
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.insets = new Insets(30, 20, 20, 20);
        settingMenuPanel.add(backButton, gbc);

        volumeSlider.addChangeListener(e -> {
            int value = volumeSlider.getValue();
            volumeLabel.setText("Volume: " + value + "%");
            if (musicPlayer != null && musicPlayer.isVolumeControlSupported()) {
                musicPlayer.setVolumePercent(value);
            }
        });

        backButton.addActionListener(e -> {
            SFXPlayer.playSound(clickSfxPath);
            cardLayout.show(wrapperPanel, "StartMenu");
            if (musicPlayer != null) {
                musicPlayer.playMusic(MusicPlayer.MusicType.START_MENU);
            }
        });

        settingMenuPanel.revalidate();
        settingMenuPanel.repaint();
    }

    private void setShopPanel() {

    }

    private void editButtonStart(JButton[] buttons) {
        for (JButton jButton : buttons) {
            jButton.setPreferredSize(new Dimension(150, 40));
            jButton.setBackground(backgroundColor);
            jButton.setForeground(textColor);
        }
    }

    private void editButtonAll(JButton[] buttons) {
        for (JButton jButton : buttons) {
            jButton.setFocusable(false);
            jButton.setFont(headerFont.deriveFont(30f));
            jButton.setContentAreaFilled(false);
            jButton.setBorderPainted(false);
            jButton.setOpaque(false);
        }
    }

    private void editButtonMain(JButton[] buttons) {
        for (JButton jButton : buttons) {
            jButton.setPreferredSize(new Dimension(150, 40));
        }
    }

    private JLabel getScaledImageLabel(String imagePath, int panelWidth, int panelHeight) {
        ImageIcon icon = new ImageIcon(imagePath);
        Image image = icon.getImage();

        // Resize gambar
        Image scaledImage = image.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);

        // Bungkus ke JLabel
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        JLabel label = new JLabel(scaledIcon);

        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);

        return label;
    }

}

class BattleUI {
    private MusicPlayer musicPlayer;
    private Pokemon playerPokemon;
    private Pokemon enemyPokemon;
    private JPanel arenaPanel;
    private JLabel playerLabel;
    private JLabel enemyLabel;
    private JLabel statusLabel;
    private JPanel movesPanel;
    private Font headerFont;
    private JLabel playerHPLabel;
    private JLabel enemyHPLabel;
    private JButton backToMenuButton; // Tombol ini sudah ada dari saran sebelumnya
    private JButton restartGameButton; // Tombol baru untuk "Main Lagi"

    private CardLayout cardLayoutInstance;
    private JPanel wrapperPanelInstance;
    private FadeEffectPanel fadeEffectPanel;

    public BattleUI(Pokemon playerPokemon, Pokemon enemyPokemon, JPanel arenaPanel, Font headerFont,
            CardLayout cardLayout, JPanel wrapperPanel, MusicPlayer musicPlayer, FadeEffectPanel fadeEffectPanel) {
        this.playerPokemon = playerPokemon;
        this.enemyPokemon = enemyPokemon;
        this.arenaPanel = arenaPanel;
        this.headerFont = headerFont;
        this.cardLayoutInstance = cardLayout; // Penting
        this.wrapperPanelInstance = wrapperPanel; // Penting
        this.musicPlayer = musicPlayer;
        this.fadeEffectPanel = fadeEffectPanel;
        this.playerPokemon.resetHp(); // Pastikan method resetHp() ada di Pokemon.java
        this.enemyPokemon.resetHp(); // Pastikan method resetHp() ada di Pokemon.java

        initializeUI();
    }

    private void initializeUI() {
        arenaPanel.removeAll();
        arenaPanel.setLayout(null);
        backToMenuButton = new JButton("Kembali ke Menu Utama");
        backToMenuButton.setFont(headerFont.deriveFont(16f));
        backToMenuButton.setBounds(50, 50, 250, 40);
        backToMenuButton.setVisible(false);
        backToMenuButton.addActionListener(e -> {
            if (cardLayoutInstance != null && wrapperPanelInstance != null) {
                fadeEffectPanel.setFadeColor(Color.black);
                fadeEffectPanel.setCurrentAlpha(0.0f);
                fadeEffectPanel.setVisible(true);
                Runnable H_afterFadeOut = () -> {
                    cardLayoutInstance.show(wrapperPanelInstance, "MainMenu");
                    musicPlayer.playMusic(MusicPlayer.MusicType.MAIN_MENU);
                    Runnable H_afterFadeIn = () -> {
                    };
                    fadeEffectPanel.startFade(0.0f, 700, H_afterFadeIn);
                };
                fadeEffectPanel.startFade(1.0f, 700, H_afterFadeOut);
            }
        });
        restartGameButton = new JButton("Main Lagi (Pilih Ulang)");
        if (headerFont != null)
            restartGameButton.setFont(headerFont.deriveFont(16f));
        restartGameButton.setBounds(backToMenuButton.getX(),
                backToMenuButton.getY() + backToMenuButton.getHeight() + 10, 250, 40); // Y disesuaikan
        restartGameButton.setVisible(false);
        restartGameButton.addActionListener(e -> {
            if (cardLayoutInstance != null && wrapperPanelInstance != null) {
                fadeEffectPanel.setFadeColor(Color.black);
                fadeEffectPanel.setCurrentAlpha(0.0f);
                fadeEffectPanel.setVisible(true);
                Runnable H_afterFadeOut = () -> {
                    cardLayoutInstance.show(wrapperPanelInstance, "PokemonSelection");
                    musicPlayer.playMusic(MusicPlayer.MusicType.MAIN_MENU);
                    Runnable H_afterFadeIn = () -> {
                    };
                    fadeEffectPanel.startFade(0.0f, 700, H_afterFadeIn);
                };
                fadeEffectPanel.startFade(1.0f, 700, H_afterFadeOut);
            }
        });
        arenaPanel.add(restartGameButton);
        arenaPanel.add(backToMenuButton);
        // Player Pokemon GIF
        ImageIcon playerIcon = new ImageIcon(playerPokemon.getBackGifPath());
        playerLabel = new JLabel(playerIcon);
        playerLabel.setBounds(50, 350, 200, 200);
        arenaPanel.add(playerLabel);

        // Enemy Pokemon GIF
        ImageIcon enemyIcon = new ImageIcon(enemyPokemon.getFrontGifPath());
        enemyLabel = new JLabel(enemyIcon);
        enemyLabel.setBounds(500, 50, 200, 200);
        arenaPanel.add(enemyLabel);

        // Player HP Label
        playerHPLabel = new JLabel();
        playerHPLabel.setFont(headerFont.deriveFont(20f));
        playerHPLabel.setForeground(Color.WHITE); // Warna teks HP
        playerHPLabel.setBounds(75, 350, 200, 30);
        arenaPanel.add(playerHPLabel);

        // Enemy HP Label
        enemyHPLabel = new JLabel();
        enemyHPLabel.setFont(headerFont.deriveFont(20f));
        enemyHPLabel.setForeground(Color.WHITE); // Warna teks HP
        enemyHPLabel.setBounds(550, 50, 200, 30);
        arenaPanel.add(enemyHPLabel);

        updateHealthDisplay();

        // Status Label
        statusLabel = new JLabel("Battle Start!");
        statusLabel.setFont(headerFont.deriveFont(20f));
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setBounds(arenaPanel.getWidth() / 2 - 125, 10, 250, 30); // Tengah atas
        arenaPanel.add(statusLabel);

        // Moves Panel
        movesPanel = new JPanel();
        movesPanel.setBounds(arenaPanel.getWidth() / 2 - 125, arenaPanel.getHeight() - 120, 250, 100);
        movesPanel.setLayout(new GridLayout(2, 2, 5, 5));
        movesPanel.setOpaque(false);
        arenaPanel.add(movesPanel);

        for (Move move : playerPokemon.getMoves()) {
            JButton moveButton = new JButton(move.getName());
            moveButton.setFont(headerFont.deriveFont(18f));
            moveButton.addActionListener(e -> performPlayerMove(move));
            movesPanel.add(moveButton);
        }

        JLabel enemyPlace = getScaledImageLabel("./Assets/pixil-frame.png", 200, 200);
        JLabel playerPlace = getScaledImageLabel("./Assets/pixil-frame.png", 200, 200);
        enemyPlace.setBounds(500, 100, 200, 200);
        playerPlace.setBounds(50, 400, 200, 200);
        arenaPanel.add(enemyPlace);
        arenaPanel.add(playerPlace);

        arenaPanel.revalidate();
        arenaPanel.repaint();
    }

    private void updateHealthDisplay() {
        if (playerPokemon != null && playerHPLabel != null) {
            playerHPLabel.setText(
                    playerPokemon.getName() + ": " + playerPokemon.getCurrentHp() + "/" + playerPokemon.getMaxHp());
        }
        if (enemyPokemon != null && enemyHPLabel != null) {
            enemyHPLabel.setText(
                    enemyPokemon.getName() + ": " + enemyPokemon.getCurrentHp() + "/" + enemyPokemon.getMaxHp());
        }
    }

    private void performPlayerMove(Move move) {
        setMoveButtonsEnabled(false);
        statusLabel.setText(playerPokemon.getName() + " used " + move.getName() + "!");

        if (move.getSfxPath() != null && !move.getSfxPath().isEmpty()) {
            SFXPlayer.playSound(move.getSfxPath()); // <-- PANGGIL SFX DI SINI
        }
        // Tentukan target animasi dan Pokemon defender
        JLabel targetLabelForAnimation = enemyLabel; // Default untuk serangan
        Pokemon defender = enemyPokemon;
        Pokemon attacker = playerPokemon;

        // Jika move adalah HEAL atau BUFF yang targetnya diri sendiri
        if (move.getType() == Type.HEAL || move.getType() == Type.BUFF) {
            targetLabelForAnimation = playerLabel; // Animasi di dekat pengguna move
        }

        int animX = targetLabelForAnimation.getX();
        int animY = targetLabelForAnimation.getY();

        showMoveAnimation(move.getGifPath(), animX, animY, () -> {
            boolean turnEnds = true; // Asumsi giliran berakhir setelah aksi
            if (move.getType() == Type.HEAL) {
                attacker.heal(move.getPower());
                statusLabel.setText(attacker.getName() + " healed itself for " + move.getPower() + " HP!");
                updateHealthDisplay();
            } else if (move.getType() == Type.BUFF) {
                // Untuk Buff, kita tentukan efeknya berdasarkan nama move (untuk sementara)
                // Atau bisa juga berdasarkan sub-tipe buff jika ada
                if (move.getName().equalsIgnoreCase("Buff")) { // Asumsi "Buff" meningkatkan Attack
                    attacker.increaseAttackBonus(move.getPower());
                    statusLabel.setText(attacker.getName() + "'s Attack rose by " + move.getPower() + "!");
                } else if (move.getName().equalsIgnoreCase("Withdraw")) { // Contoh untuk Defense Buff
                    // Jika "Withdraw" adalah Type.BUFF dan powernya untuk defense
                    attacker.increaseDefenseBonus(move.getPower());
                    statusLabel.setText(attacker.getName() + "'s Defense rose by " + move.getPower() + "!");
                } else {
                    // Default buff (misalnya, jika ada move buff lain tanpa nama spesifik)
                    attacker.increaseAttackBonus(move.getPower()); // Default ke attack
                    statusLabel.setText(attacker.getName() + " powered up!");
                }
                // Tidak ada updateHealthDisplay() kecuali buff juga heal
            } else { // Ini adalah move serangan (damaging move)
                int damage = Battle.calculateDamage(attacker, move, defender);
                defender.takeDamage(damage);
                statusLabel.setText(attacker.getName() + " dealt " + damage + " damage to " + defender.getName() + "!");
                updateHealthDisplay();

                if (defender.isFainted()) {
                    statusLabel.setText(defender.getName() + " fainted! " + attacker.getName() + " wins!");
                    setMoveButtonsEnabled(false);
                    backToMenuButton.setVisible(true);
                    restartGameButton.setVisible(true);
                    turnEnds = false; // Pertarungan selesai, tidak ada giliran musuh
                }
            }

            if (turnEnds && !defender.isFainted()) { // Hanya lanjut ke giliran musuh jika battle belum berakhir
                Timer enemyTurnTimer = new Timer(1500, ae -> performEnemyMove()); // Tambah delay sedikit
                enemyTurnTimer.setRepeats(false);
                enemyTurnTimer.start();
            }
        });
    }

    private void performEnemyMove() {
        statusLabel.setText(enemyPokemon.getName() + "'s turn...");
        Move move = enemyPokemon.getMoves().get((int) (Math.random() * enemyPokemon.getMoves().size()));

        // Mirip dengan player, tentukan target animasi
        JLabel targetLabelForAnimation = playerLabel; // Default untuk serangan
        Pokemon defender = playerPokemon;
        Pokemon attacker = enemyPokemon;

        if (move.getType() == Type.HEAL || move.getType() == Type.BUFF) {
            targetLabelForAnimation = enemyLabel; // Animasi di dekat pengguna move
        }

        int animX = targetLabelForAnimation.getX();
        int animY = targetLabelForAnimation.getY();
        Timer actionDelayTimer = new Timer(1000, actionEvent -> {
            statusLabel.setText(attacker.getName() + " used " + move.getName() + "!");
            if (move.getSfxPath() != null && !move.getSfxPath().isEmpty()) {
                SFXPlayer.playSound(move.getSfxPath()); // <-- PANGGIL SFX DI SINI
            }
            showMoveAnimation(move.getGifPath(), animX, animY, () -> {
                boolean turnEnds = true;

                if (move.getType() == Type.HEAL) {
                    attacker.heal(move.getPower());
                    statusLabel.setText(attacker.getName() + " healed itself for " + move.getPower() + " HP!");
                    updateHealthDisplay();
                } else if (move.getType() == Type.BUFF) {
                    if (move.getName().equalsIgnoreCase("Buff")) {
                        attacker.increaseAttackBonus(move.getPower());
                        statusLabel.setText(attacker.getName() + "'s Attack rose by " + move.getPower() + "!");
                    } else if (move.getName().equalsIgnoreCase("Withdraw")) {
                        attacker.increaseDefenseBonus(move.getPower());
                        statusLabel.setText(attacker.getName() + "'s Defense rose by " + move.getPower() + "!");
                    } else {
                        attacker.increaseAttackBonus(move.getPower());
                        statusLabel.setText(attacker.getName() + " powered up!");
                    }
                } else { // Damaging move
                    int damage = Battle.calculateDamage(attacker, move, defender);
                    defender.takeDamage(damage);
                    statusLabel.setText(
                            attacker.getName() + " dealt " + damage + " damage to " + defender.getName() + "!");
                    updateHealthDisplay();

                    if (defender.isFainted()) {
                        statusLabel.setText(defender.getName() + " fainted! " + attacker.getName() + " wins!");
                        setMoveButtonsEnabled(false); // Tombol player tetap mati
                        backToMenuButton.setVisible(true);
                        restartGameButton.setVisible(true);
                        turnEnds = false;
                    }
                }

                if (turnEnds && !defender.isFainted()) {
                    setMoveButtonsEnabled(true); // Aktifkan tombol player untuk giliran berikutnya
                    statusLabel.setText("Your turn!");
                }
            });
        });
        actionDelayTimer.setRepeats(false);
        actionDelayTimer.start();
    }

    private void showMoveAnimation(String gifPath, int x, int y, Runnable onAnimationEnd) {
        if (gifPath == null || gifPath.isEmpty()) {
            System.out.println("Move GIF path is empty, skipping animation.");
            if (onAnimationEnd != null) {
                onAnimationEnd.run();
            }
            return;
        }

        ImageIcon moveIcon = new ImageIcon(gifPath);
        JLabel moveLabel = new JLabel(moveIcon);
        moveLabel.setBounds(x, y, moveIcon.getIconWidth(), moveIcon.getIconHeight());

        arenaPanel.add(moveLabel);
        arenaPanel.setComponentZOrder(moveLabel, 0);
        arenaPanel.repaint();

        Timer timer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                arenaPanel.remove(moveLabel);
                arenaPanel.repaint();
                if (onAnimationEnd != null) {
                    onAnimationEnd.run();
                }
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void setMoveButtonsEnabled(boolean enabled) {
        for (Component comp : movesPanel.getComponents()) {
            if (comp instanceof JButton) {
                comp.setEnabled(enabled);
            }
        }
    }

    private JLabel getScaledImageLabel(String imagePath, int panelWidth, int panelHeight) {
        ImageIcon icon = new ImageIcon(imagePath);
        Image image = icon.getImage();

        // Resize gambar
        Image scaledImage = image.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);

        // Bungkus ke JLabel
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        JLabel label = new JLabel(scaledIcon);

        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);

        return label;
    }
}