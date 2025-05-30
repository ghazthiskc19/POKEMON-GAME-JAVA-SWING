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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;
import java.util.HashMap;

public class MainUI {
    public static void main(String[] args) {
        new StartMenuUI();
    }
}

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
    JPanel startMenuPanel;
    JPanel settingMenuPanel;

    JPanel mainMenuPanel;
    JPanel arenaPanel;
    JPanel pokemonSelectionPanel;
    JPanel pokemonShopPanel;
    JPanel nameInputPanel;
    ArrayList<Pokemon> ownedPokemons = new ArrayList<>();

    boolean isPokemonLocked = false; // true jika sudah pernah pilih pokemon
    // false jika belum pernah pilih pokemon, jadi bisa pilih pokemon lain

    // Global Variable
    Color textColor = new Color(21, 22, 21);
    Color textColor2 = new Color(255, 255, 255);
    Color backgroundColor = new Color(181, 163, 91);
    Font headerFont;
    int coins = PlayerData.getCoins(); // Initialize coins from PlayerData
    JLabel currentMoney; // label untuk menampilkan koin
    int ownedPokemonCount = 0; // jumlah pokemon yang dimiliki
    JLabel currentCountPokemon; // label untuk menampilkan jumlah pokemon

    private JButton fightButton; // Tambahkan ini!

    private JLabel lockedInfoLabel; // Tambahkan di class StartMenuUI

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
        setNameInputPanel();
        wrapperPanel.add(nameInputPanel, "NameInput");
        add(wrapperPanel);
        setResizable(false);
        fadeEffectPanel = new FadeEffectPanel();
        fadeEffectPanel.setFadeColor(Color.BLACK);
        fadeEffectPanel.setCurrentAlpha(1.0f);
        setGlassPane(fadeEffectPanel);
        fadeEffectPanel.setVisible(true);

        // Load saved Pokemon data
        loadSavedPokemonData();

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
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(headerFont);
        } catch (IOException | java.awt.FontFormatException e) {
            e.printStackTrace();
        }
        musicPlayer = new MusicPlayer(
                "./Assets/Sound/BGM/start_menu_music.wav",
                "./Assets/Sound/BGM/main_menu_music.wav",
                "./Assets/Sound/BGM/arena_music.wav");

        // Initialize volumes from PlayerData
        int musicVolume = PlayerData.getMusicVolume();
        int sfxVolume = PlayerData.getSFXVolume();

        // Set initial volumes
        if (musicPlayer != null && musicPlayer.isVolumeControlSupported()) {
            musicPlayer.setVolumePercent(musicVolume);
        }
        SFXPlayer.setVolume(sfxVolume);
    }

    private void setJPanel() {
        wrapperPanel = new JPanel(cardLayout);

        startMenuPanel = new BackgroundPanel("./Assets/bg-start.jpeg");
        settingMenuPanel = new DarkBackgroundPanel("./Assets/bg-start.jpeg");
        mainMenuPanel = new BackgroundPanel("./Assets/bg-main.jpg");
        pokemonSelectionPanel = new BackgroundPanel("./Assets/bg-selection.jpg");
        arenaPanel = new BackgroundPanel("./Assets/bg-selection.jpg");
        pokemonShopPanel = new BackgroundPanel("./Assets/forest.jpg");

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

        MouseAdapter buttonHover = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                JButton btn = (JButton) e.getSource();
                btn.setForeground(textColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                JButton btn = (JButton) e.getSource();
                btn.setForeground(Color.white);
            }
        };
        for (JButton btn : buttons) {
            btn.addMouseListener(buttonHover);
        }

        startButton.addActionListener(e -> {
            SFXPlayer.playSound(clickSfxPath);
            fadeEffectPanel.setFadeColor(Color.black);
            fadeEffectPanel.setCurrentAlpha(0.0f);
            fadeEffectPanel.setVisible(true);
            Runnable H_afterFadeOut = () -> {
                if (PlayerData.hasPlayerName()) {
                    cardLayout.show(wrapperPanel, "MainMenu");
                    musicPlayer.playMusic(MusicPlayer.MusicType.MAIN_MENU);
                } else {
                    cardLayout.show(wrapperPanel, "NameInput");
                }
                fadeEffectPanel.startFade(0.0f, 700, () -> {
                });
            };
            fadeEffectPanel.startFade(1.0f, 700, H_afterFadeOut);
        });

        settingButton.addActionListener(e -> {
            SFXPlayer.playSound(clickSfxPath);
            fadeEffectPanel.setFadeColor(Color.black);
            fadeEffectPanel.setCurrentAlpha(0.0f);
            fadeEffectPanel.setVisible(true);
            Runnable H_afterFadeOut = () -> {
                cardLayout.show(wrapperPanel, "SettingMenu");
                fadeEffectPanel.startFade(0.0f, 700, () -> {
                });
            };
            fadeEffectPanel.startFade(1.0f, 700, H_afterFadeOut);
        });

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

        // Add a listener to refresh Pokemon selection when entering arena
        for (Component comp : left.getComponents()) {
            if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                if (button.getText().equals("Let's Fight")) {
                    button.addActionListener(e -> {
                        // Refresh Pokemon selection panel before showing it
                        refreshPokemonSelectionPanel();
                    });
                }
            }
        }
    }

    private void setNameInputPanel() {
        nameInputPanel = new BackgroundPanel("./Assets/bg-start.jpeg");
        nameInputPanel.setLayout(new BoxLayout(nameInputPanel, BoxLayout.Y_AXIS));

        // Spacer atas besar (sesuaikan dengan posisi logo)
        nameInputPanel.add(Box.createVerticalStrut(350));

        // Title
        JLabel titleLabel = new JLabel("Fill Your Name!");
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setFont(headerFont.deriveFont(24f));
        titleLabel.setForeground(Color.BLACK);
        nameInputPanel.add(titleLabel);

        // Spacer antara title dan textfield
        nameInputPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // TextField dengan center alignment
        JTextField nameField = new JTextField(15);
        nameField.setMaximumSize(new Dimension(300, 30));
        nameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameField.setFont(headerFont.deriveFont(24f));
        nameInputPanel.add(nameField);

        // Spacer antara textfield dan button
        nameInputPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Submit Button
        JButton submitButton = new JButton("Submit");
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        submitButton.setPreferredSize(new Dimension(200, 30));
        submitButton.setFont(headerFont.deriveFont(24f));
        submitButton.setBackground(new Color(0, 20, 20, 20));
        submitButton.setForeground(Color.BLACK);

        // Membuat border transparan
        submitButton.setBorderPainted(false);
        submitButton.setFocusPainted(false);
        submitButton.setContentAreaFilled(false);
        submitButton.setOpaque(true);
        submitButton.setBorderPainted(true);
        nameInputPanel.add(submitButton);

        // Spacer bawah
        nameInputPanel.add(Box.createVerticalGlue());

        submitButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            if (!name.isEmpty()) {
                PlayerData.savePlayerName(name);
                proceedToMainMenu();
            }
        });
    }

    private void proceedToMainMenu() {
        fadeEffectPanel.setFadeColor(Color.black);
        fadeEffectPanel.setCurrentAlpha(0.0f);
        fadeEffectPanel.setVisible(true);
        Runnable afterFadeOut = () -> {
            cardLayout.show(wrapperPanel, "MainMenu");
            musicPlayer.playMusic(MusicPlayer.MusicType.MAIN_MENU);
            updatePlayerNameInUI();
            fadeEffectPanel.startFade(0.0f, 700, () -> {
            });
        };
        fadeEffectPanel.startFade(1.0f, 700, afterFadeOut);
    }

    private void updatePlayerNameInUI() {
        Component[] components = mainMenuPanel.getComponents();
        for (Component comp : components) {
            if (comp instanceof JPanel) {
                Component[] subComps = ((JPanel) comp).getComponents();
                for (Component subComp : subComps) {
                    if (subComp instanceof JLabel && ((JLabel) subComp).getText().startsWith("Halo, ")) {
                        String savedName = PlayerData.getPlayerName();
                        ((JLabel) subComp).setText("Halo, " + (savedName != null ? savedName : "Trainer"));
                    }
                }
            }
        }
    }

    private void setLeftMainMenu(JPanel left) {
        left.setLayout(null);
        left.setBackground(new Color(0, 0, 0, 127)); // alpha 127 = 50%
        left.setOpaque(true);

        String clickSfxPath = "./Assets/Sound/SFX/Button - Sound effect.wav";
        String savedName = PlayerData.getPlayerName();
        JLabel playerName1 = new JLabel("Halo, " + (savedName != null ? savedName : "Trainer"));
        JLabel playerName2 = new JLabel("Siap bertarung?");
        currentMoney = new JLabel("Uang yang dimiliki : " + PlayerData.getCoins() + " koin");
        currentCountPokemon = new JLabel("Jumlah Pokemon yang dimiliki : " + ownedPokemonCount);
        JButton enterArena = new JButton("Let's Fight");
        JButton enterShop = new JButton("Shop");
        JButton exitToStartMenu = new JButton("Exit");
        JButton logoutButton = new JButton("Change Name");

        JLabel[] Allteks = { playerName1, playerName2, currentMoney, currentCountPokemon };
        JButton[] buttons = { enterArena, enterShop, exitToStartMenu, logoutButton };
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
        logoutButton.setBounds(50, 475, 250, 50); // Tombol Logout paling bawah
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
                fadeEffectPanel.startFade(0.0f, 700, () -> {
                });
            };
            fadeEffectPanel.startFade(1.0f, 700, H_afterFadeOut);
        });

        logoutButton.addActionListener(e -> {
            PlayerData.clearPlayerName();
            musicPlayer.playMusic(MusicPlayer.MusicType.START_MENU);
            cardLayout.show(wrapperPanel, "NameInput");
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

    private ArrayList<JButton> pokemonButtons = new ArrayList<>(); // Tambahkan ini di class

    private void setPokemonButton(JPanel pokemonButton, JPanel pokemonImage) {
        pokemonButtons.clear();
        String clickSfxPath = "./Assets/Sound/SFX/Button - Sound effect.wav";

        // Load owned Pokemon from PlayerData
        List<String> ownedPokemonNames = PlayerData.getOwnedPokemon();

        // If we have saved Pokemon, show those as buttons
        for (Pokemon pokemon : allPokemons) {
            if (ownedPokemonNames.contains(pokemon.getName())) {
                JButton btn = new JButton();
                ImageIcon icon = new ImageIcon(pokemon.getFrontGifPath());
                btn.setIcon(icon);
                btn.setFocusable(false);
                btn.setContentAreaFilled(false);
                btn.setBorderPainted(false);
                btn.setOpaque(false);
                btn.setPreferredSize(new Dimension(175, 175));
                pokemonButton.add(btn);
                pokemonButtons.add(btn);

                btn.addActionListener(e -> {
                    SFXPlayer.playSound(clickSfxPath);
                    setPokemonImage(pokemonImage, pokemon);
                    playerPokemon = pokemon;
                    if (fightButton != null)
                        fightButton.setEnabled(true);
                });
                availablePokemon.add(pokemon);
            }
        }

        // If no Pokemon are owned, show all basic Pokemon
        if (ownedPokemonNames.isEmpty()) {
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
                    pokemonButtons.add(btn);

                    btn.addActionListener(e -> {
                        SFXPlayer.playSound(clickSfxPath);
                        setPokemonImage(pokemonImage, pokemon);
                        if (isPokemonLocked && playerPokemon != pokemon) {
                            if (fightButton != null)
                                fightButton.setEnabled(false);
                            return;
                        }
                        if (playerPokemon == null) {
                            ownedPokemonCount++;
                            updateOwnedPokemonLabel();
                            List<String> newOwnedPokemon = new ArrayList<>();
                            newOwnedPokemon.add(pokemon.getName());
                            PlayerData.saveOwnedPokemon(newOwnedPokemon);
                            PlayerData.saveCurrentPokemon(pokemon.getName());
                            ownedPokemons.add(pokemon);
                        }
                        playerPokemon = pokemon;
                        if (fightButton != null)
                            fightButton.setEnabled(true);
                        if (lockedInfoLabel != null)
                            lockedInfoLabel.setVisible(isPokemonLocked);
                    });
                    availablePokemon.add(pokemon);
                }
                i++;
            }
        }
    }

    private void setPlayExitButton(JPanel playExitButton) {
        String clickSfxPath = "./Assets/Sound/SFX/Button - Sound effect.wav";
        fightButton = new JButton("Let's Go!!");
        JButton exit = new JButton("Exit");
        JButton[] buttons = { fightButton, exit };
        for (JButton jButton : buttons) {
            jButton.setForeground(Color.white);
        }
        editButtonAll(buttons);

        playExitButton.add(exit);
        playExitButton.add(fightButton);
        fightButton.addActionListener(e -> {
            SFXPlayer.playSound(clickSfxPath);
            // Jika belum pilih pokemon, tombol tidak melakukan apa-apa
            if (playerPokemon == null) {
                JOptionPane.showMessageDialog(this, "Pilih Pokemon dulu, bro!", "Pokemon Belum Dipilih",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            fadeEffectPanel.setFadeColor(Color.black);
            fadeEffectPanel.setCurrentAlpha(0.0f);
            fadeEffectPanel.setVisible(true);
            Runnable H_afterFadeOut = () -> {
                isPokemonLocked = true; // Kunci pilihan
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
        // Get available Pokemon from shop
        List<Pokemon> availableEnemies = new ArrayList<>();
        List<String> currentOwnedNames = PlayerData.getOwnedPokemon();
        Random rand = new Random(); // Add Random variable

        // Basic Pokemon that can be enemies
        Pokemon[] basicPokemon = {
                allPokemons.get(0), // Pichu
                allPokemons.get(3), // Squirtle
                allPokemons.get(6), // Charmander
                allPokemons.get(9) // Ralts
        };

        // Add basic Pokemon and their evolutions to available enemies
        for (Pokemon basic : basicPokemon) {
            availableEnemies.add(basic);
            Pokemon nextEvo = PokemonFactory.getNextEvolution(basic);
            if (nextEvo != null) {
                availableEnemies.add(nextEvo);
            }
        }

        // Filter out player's Pokemon and its evolution
        availableEnemies.removeIf(p -> p.getName().equals(playerPokemon.getName()));
        Pokemon playerEvo = PokemonFactory.getNextEvolution(playerPokemon);
        if (playerEvo != null) {
            availableEnemies.removeIf(p -> p.getName().equals(playerEvo.getName()));
        }

        // Randomly select enemy from available Pokemon
        if (!availableEnemies.isEmpty()) {
            enemyPokemon = availableEnemies.get(rand.nextInt(availableEnemies.size()));
        } else {
            // Fallback to random selection if no suitable enemies found
            ArrayList<Pokemon> enemyCandidates = new ArrayList<>();
            for (Pokemon p : allPokemons) {
                if (p != playerPokemon) {
                    enemyCandidates.add(p);
                }
            }
            if (!enemyCandidates.isEmpty()) {
                enemyPokemon = enemyCandidates.get(rand.nextInt(enemyCandidates.size()));
            }
        }

        new BattleUI(playerPokemon, enemyPokemon, arenaPanel, headerFont, cardLayout, wrapperPanel, musicPlayer,
                fadeEffectPanel, this);
    }

    private void setSettingMenuPanel() {
        String clickSfxPath = "./Assets/Sound/SFX/Button - Sound effect.wav";
        settingMenuPanel.removeAll();
        settingMenuPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        if (!(settingMenuPanel instanceof BackgroundPanel)) {
            settingMenuPanel.setBackground(backgroundColor);
        }

        JLabel titleLabel = new JLabel("Pengaturan volume");
        titleLabel.setFont(headerFont.deriveFont(40f));
        titleLabel.setForeground(textColor2);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 20, 40, 20);
        gbc.anchor = GridBagConstraints.CENTER;
        settingMenuPanel.add(titleLabel, gbc);

        // Music Volume Slider
        JLabel musicLabel = new JLabel("Music Volume");
        musicLabel.setFont(headerFont.deriveFont(20f));
        musicLabel.setForeground(textColor2);
        musicLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 20, 10, 20);
        settingMenuPanel.add(musicLabel, gbc);

        int initialMusicVolume = PlayerData.getMusicVolume();
        JSlider musicSlider = new JSlider(0, 100, initialMusicVolume);
        musicSlider.setMajorTickSpacing(20);
        musicSlider.setMinorTickSpacing(5);
        musicSlider.setPaintTicks(true);
        musicSlider.setPaintLabels(true);
        musicSlider.setOpaque(false);
        musicSlider.setForeground(textColor2);
        musicSlider.setPreferredSize(new Dimension(350, 60));
        gbc.gridy = 2;
        settingMenuPanel.add(musicSlider, gbc);

        JLabel musicVolumeLabel = new JLabel("Volume: " + initialMusicVolume + "%");
        musicVolumeLabel.setFont(headerFont.deriveFont(16f));
        musicVolumeLabel.setForeground(textColor2);
        musicVolumeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 20, 30, 20);
        settingMenuPanel.add(musicVolumeLabel, gbc);

        // SFX Volume Slider
        JLabel sfxLabel = new JLabel("SFX Volume");
        sfxLabel.setFont(headerFont.deriveFont(20f));
        sfxLabel.setForeground(textColor2);
        sfxLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 4;
        gbc.insets = new Insets(0, 20, 10, 20);
        settingMenuPanel.add(sfxLabel, gbc);

        int initialSFXVolume = PlayerData.getSFXVolume();
        JSlider sfxSlider = new JSlider(0, 100, initialSFXVolume);
        sfxSlider.setMajorTickSpacing(20);
        sfxSlider.setMinorTickSpacing(5);
        sfxSlider.setPaintTicks(true);
        sfxSlider.setPaintLabels(true);
        sfxSlider.setOpaque(false);
        sfxSlider.setForeground(textColor2);
        sfxSlider.setPreferredSize(new Dimension(350, 60));
        gbc.gridy = 5;
        settingMenuPanel.add(sfxSlider, gbc);

        JLabel sfxVolumeLabel = new JLabel("Volume: " + initialSFXVolume + "%");
        sfxVolumeLabel.setFont(headerFont.deriveFont(16f));
        sfxVolumeLabel.setForeground(textColor2);
        sfxVolumeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 6;
        gbc.insets = new Insets(0, 20, 30, 20);
        settingMenuPanel.add(sfxVolumeLabel, gbc);

        // Back Button
        JButton backButton = new JButton("Kembali");
        backButton.setFont(headerFont.deriveFont(24f));
        backButton.setContentAreaFilled(false);
        backButton.setOpaque(false);
        backButton.setBorderPainted(true);
        backButton.setForeground(textColor2);
        gbc.gridy = 7;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.insets = new Insets(30, 20, 20, 20);
        settingMenuPanel.add(backButton, gbc);

        // Music Volume Slider Listener
        musicSlider.addChangeListener(e -> {
            int value = musicSlider.getValue();
            musicVolumeLabel.setText("Volume: " + value + "%");
            if (musicPlayer != null && musicPlayer.isVolumeControlSupported()) {
                musicPlayer.setVolumePercent(value);
                PlayerData.saveMusicVolume(value);
            }
        });

        // SFX Volume Slider Listener
        sfxSlider.addChangeListener(e -> {
            int value = sfxSlider.getValue();
            sfxVolumeLabel.setText("Volume: " + value + "%");
            SFXPlayer.setVolume(value);
            PlayerData.saveSFXVolume(value);
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
        pokemonShopPanel.removeAll();
        pokemonShopPanel.setLayout(new BoxLayout(pokemonShopPanel, BoxLayout.Y_AXIS));
        pokemonShopPanel.setBackground(new Color(240, 240, 240));
        pokemonShopPanel.setOpaque(false);

        // Panel header koin
        JPanel coinPanel = new JPanel();
        coinPanel.setOpaque(false);
        JLabel coinLabel = new JLabel("Koin Anda: " + coins);
        coinLabel.setFont(headerFont.deriveFont(28f));
        coinLabel.setForeground(new Color(30, 30, 30));
        coinPanel.add(coinLabel);

        pokemonShopPanel.add(Box.createVerticalStrut(20));
        pokemonShopPanel.add(coinPanel);
        pokemonShopPanel.add(Box.createVerticalStrut(10));

        // Panel untuk 4 Pokemon yang bisa dibeli
        JPanel shopGrid = new JPanel(new GridLayout(1, 4, 10, 10));
        shopGrid.setOpaque(false);
        shopGrid.setMaximumSize(new Dimension(700, 500));

        // Dapatkan Pokemon yang bisa dibeli
        List<Pokemon> availableForPurchase = new ArrayList<>();
        List<String> currentOwnedNames = PlayerData.getOwnedPokemon();

        // Define evolution chains
        String[][] evolutionChains = {
                { "Pichu", "Pikachu", "Raichu" },
                { "Squirtle", "Wartortle", "Blastoise" },
                { "Charmander", "Charmeleon", "Charizard" },
                { "Ralts", "Kirlia", "Gardevoir" }
        };

        // Check each evolution chain
        for (String[] chain : evolutionChains) {
            boolean foundNextEvolution = false;

            // Check if we have any Pokemon in this chain
            for (int i = 0; i < chain.length; i++) {
                String pokemonName = chain[i];

                // If we have this Pokemon and there's a next evolution
                if (currentOwnedNames.contains(pokemonName) && i < chain.length - 1) {
                    String nextEvolution = chain[i + 1];

                    // If we don't have the next evolution, add it to available Pokemon
                    if (!currentOwnedNames.contains(nextEvolution)) {
                        for (Pokemon p : allPokemons) {
                            if (p.getName().equals(nextEvolution)) {
                                availableForPurchase.add(p);
                                foundNextEvolution = true;
                                break;
                            }
                        }
                    }
                }
            }

            // If we don't have any Pokemon in this chain, add the basic form
            if (!foundNextEvolution) {
                for (Pokemon p : allPokemons) {
                    if (p.getName().equals(chain[0])) {
                        availableForPurchase.add(p);
                        break;
                    }
                }
            }
        }

        for (Pokemon pokemon : availableForPurchase) {
            JPanel pokemonCard = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    // Panel agak dihitamkan transparan
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setColor(new Color(0, 0, 0, 120)); // Semitransparan hitam
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                    g2d.dispose();
                }
            };
            pokemonCard.setOpaque(false);
            pokemonCard.setLayout(new BoxLayout(pokemonCard, BoxLayout.Y_AXIS));
            pokemonCard.setBackground(new Color(255, 255, 255, 200));
            pokemonCard.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2, true));
            pokemonCard.setPreferredSize(new Dimension(300, 400));

            // Pokemon GIF
            JLabel pokemonGif = new JLabel(new ImageIcon(pokemon.getFrontGifPath()));
            pokemonGif.setAlignmentX(Component.CENTER_ALIGNMENT);
            pokemonGif.setPreferredSize(new Dimension(150, 150));
            pokemonCard.add(Box.createVerticalStrut(10));
            pokemonCard.add(pokemonGif);

            // Pokemon Name
            JLabel nameLabel = new JLabel(pokemon.getName());
            nameLabel.setFont(headerFont.deriveFont(20f));
            nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            nameLabel.setForeground(textColor2);
            pokemonCard.add(Box.createVerticalStrut(10));
            pokemonCard.add(nameLabel);

            // Pokemon Type
            JLabel typeLabel = new JLabel("Type: " + pokemon.getType());
            typeLabel.setFont(headerFont.deriveFont(16f));
            typeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            typeLabel.setForeground(textColor2);
            pokemonCard.add(typeLabel);

            // Pokemon Stats
            JLabel statsLabel = new JLabel(String.format("HP: %d | ATK: %d | DEF: %d",
                    pokemon.getMaxHp(), pokemon.getAttack(), pokemon.getDefense()));
            statsLabel.setFont(headerFont.deriveFont(14f));
            statsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            statsLabel.setForeground(textColor2);
            pokemonCard.add(statsLabel);

            // Evolution Requirements
            boolean isEvolution = !pokemon.getName().equals("Pichu") &&
                    !pokemon.getName().equals("Squirtle") &&
                    !pokemon.getName().equals("Charmander") &&
                    !pokemon.getName().equals("Ralts");

            // Determine price based on evolution stage
            int cost;
            if (!isEvolution) {
                cost = 200; // Basic form
            } else {
                // Check if it's the final evolution
                boolean isFinalEvolution = false;
                for (String[] chain : evolutionChains) {
                    if (chain[chain.length - 1].equals(pokemon.getName())) {
                        isFinalEvolution = true;
                        break;
                    }
                }
                cost = isFinalEvolution ? 500 : 300; // 500 for final evolution, 300 for middle evolution
            }

            if (isEvolution) {
                // Find the previous evolution
                String prevEvoName = null;
                for (String[] chain : evolutionChains) {
                    for (int i = 1; i < chain.length; i++) {
                        if (chain[i].equals(pokemon.getName())) {
                            prevEvoName = chain[i - 1];
                            break;
                        }
                    }
                }

                if (prevEvoName != null) {
                    JLabel reqLabel = new JLabel("Requires: " + prevEvoName);
                    reqLabel.setFont(headerFont.deriveFont(14f));
                    reqLabel.setForeground(new Color(100, 100, 100));
                    reqLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                    reqLabel.setForeground(textColor2);
                    pokemonCard.add(reqLabel);
                }
            }

            // Buy Button
            JButton buyButton = new JButton("Buy (" + cost + " coins)");
            buyButton.setFont(headerFont.deriveFont(16f));
            buyButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            buyButton.setPreferredSize(new Dimension(200, 40));
            buyButton.addActionListener(e -> {
                if (coins >= cost) {
                    coins -= cost;
                    PlayerData.spendCoins(cost);
                    updateCoinLabel();

                    // Jika ini evolusi, hapus Pokemon sebelumnya
                    if (isEvolution) {
                        String prevEvoName = null;
                        for (String[] chain : evolutionChains) {
                            for (int i = 1; i < chain.length; i++) {
                                if (chain[i].equals(pokemon.getName())) {
                                    prevEvoName = chain[i - 1];
                                    break;
                                }
                            }
                        }

                        if (prevEvoName != null) {
                            for (Pokemon p : allPokemons) {
                                if (p.getName().equals(prevEvoName)) {
                                    ownedPokemons.remove(p);
                                    List<String> updatedOwnedNames = new ArrayList<>(PlayerData.getOwnedPokemon());
                                    updatedOwnedNames.remove(prevEvoName);
                                    PlayerData.saveOwnedPokemon(updatedOwnedNames);
                                    break;
                                }
                            }
                        }
                    }

                    // Tambahkan Pokemon baru
                    ownedPokemons.add(pokemon);
                    List<String> updatedOwnedNames = new ArrayList<>(PlayerData.getOwnedPokemon());
                    updatedOwnedNames.add(pokemon.getName());
                    PlayerData.saveOwnedPokemon(updatedOwnedNames);

                    JOptionPane.showMessageDialog(this, "Berhasil membeli " + pokemon.getName() + "!");
                    setShopPanel(); // Refresh shop
                } else {
                    JOptionPane.showMessageDialog(this, "Koin tidak cukup!");
                }
            });
            pokemonCard.add(Box.createVerticalStrut(10));
            pokemonCard.add(buyButton);
            pokemonCard.add(Box.createVerticalStrut(10));

            shopGrid.add(pokemonCard);
        }

        pokemonShopPanel.add(shopGrid);
        pokemonShopPanel.add(Box.createVerticalStrut(20));

        // Tombol kembali ke MainMenu
        JButton backBtn = new JButton("Kembali ke Menu");
        backBtn.setFont(headerFont.deriveFont(16f));
        backBtn.setBackground(new Color(220, 220, 220));
        backBtn.setForeground(Color.BLACK);
        backBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        backBtn.setPreferredSize(new Dimension(200, 40));
        backBtn.addActionListener(e -> {
            cardLayout.show(wrapperPanel, "MainMenu");
            musicPlayer.playMusic(MusicPlayer.MusicType.MAIN_MENU);
        });

        pokemonShopPanel.add(backBtn);
        pokemonShopPanel.add(Box.createVerticalStrut(20));

        pokemonShopPanel.revalidate();
        pokemonShopPanel.repaint();
    }

    private void editButtonStart(JButton[] buttons) {
        Color normal = new Color(220, 220, 220, 180); // abu/transparan seperti gambar 1
        Color text = Color.WHITE;
        for (JButton jButton : buttons) {
            jButton.setPreferredSize(new Dimension(200, 50));
            jButton.setBackground(normal);
            jButton.setForeground(text);
            jButton.setFont(headerFont.deriveFont(30f));
            jButton.setBorderPainted(false);
            jButton.setOpaque(true);
            jButton.setContentAreaFilled(true);
        }
    }

    private void editButtonAll(JButton[] buttons) {
        Color normal = new Color(220, 220, 220, 180); // abu/transparan seperti gambar 1
        Color hover = Color.WHITE;
        Color text = Color.WHITE;

        for (JButton jButton : buttons) {
            jButton.setFocusable(false);
            jButton.setFont(headerFont.deriveFont(30f));
            jButton.setContentAreaFilled(true);
            jButton.setBorderPainted(false);
            jButton.setOpaque(true);
            jButton.setBackground(normal);
            jButton.setForeground(text);

            jButton.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    jButton.setBackground(hover);
                    jButton.setForeground(Color.BLACK);
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    jButton.setBackground(normal);
                    jButton.setForeground(text);
                }
            });
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
        Image scaledImage = image.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        JLabel label = new JLabel(scaledIcon);

        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);

        return label;
    }

    private void updateCoinLabel() {
        if (currentMoney != null) {
            currentMoney.setText("Uang yang dimiliki : " + PlayerData.getCoins() + " koin");
        }
    }

    private void updateOwnedPokemonLabel() {
        if (currentCountPokemon != null) {
            currentCountPokemon.setText("Jumlah Pokemon yang dimiliki : " + ownedPokemonCount);
        }
    }

    public void addCoins(int amount) {
        coins += amount;
        PlayerData.addCoins(amount); // Save to PlayerData
        updateCoinLabel();
    }

    // Add this method to load saved Pokemon data when the game starts
    private void loadSavedPokemonData() {
        List<String> ownedPokemonNames = PlayerData.getOwnedPokemon();
        if (!ownedPokemonNames.isEmpty()) {
            ownedPokemonCount = ownedPokemonNames.size();
            updateOwnedPokemonLabel();

            // Find and set current Pokemon
            String currentPokemonName = PlayerData.getCurrentPokemon();
            if (!currentPokemonName.isEmpty()) {
                for (Pokemon pokemon : allPokemons) {
                    if (pokemon.getName().equals(currentPokemonName)) {
                        playerPokemon = pokemon;
                        ownedPokemons.add(pokemon);
                        break;
                    }
                }
            }

            // Add all owned Pokemon to ownedPokemons list
            for (String pokemonName : ownedPokemonNames) {
                for (Pokemon pokemon : allPokemons) {
                    if (pokemon.getName().equals(pokemonName) && !ownedPokemons.contains(pokemon)) {
                        ownedPokemons.add(pokemon);
                    }
                }
            }
        }
    }

    private void refreshPokemonSelectionPanel() {
        // Clear existing Pokemon buttons
        pokemonButtons.clear();
        availablePokemon.clear();

        // Get current owned Pokemon from PlayerData
        List<String> ownedPokemonNames = PlayerData.getOwnedPokemon();

        // Update owned Pokemon count
        ownedPokemonCount = ownedPokemonNames.size();
        updateOwnedPokemonLabel();

        // Clear and reset the Pokemon selection panel
        pokemonSelectionPanel.removeAll();
        pokemonSelectionPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
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

        // Set up the Pokemon buttons with current owned Pokemon
        setPokemonButton(pokemonButton, pokemonImage);
        setPlayExitButton(playExitButton);

        // Refresh the panel
        pokemonSelectionPanel.revalidate();
        pokemonSelectionPanel.repaint();
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
    private JProgressBar playerHealthBar;
    private JProgressBar enemyHealthBar;
    private JButton backToMenuButton;
    private JButton restartGameButton;
    private Map<Move, Integer> moveCooldowns; // Track cooldowns for each move
    private int turnCount; // Track number of turns
    private Map<Move, JLabel> moveCooldownLabels; // Add this field

    private CardLayout cardLayoutInstance;
    private JPanel wrapperPanelInstance;
    private FadeEffectPanel fadeEffectPanel;
    private StartMenuUI parentUI;

    public BattleUI(Pokemon playerPokemon, Pokemon enemyPokemon, JPanel arenaPanel, Font headerFont,
            CardLayout cardLayout, JPanel wrapperPanel, MusicPlayer musicPlayer, FadeEffectPanel fadeEffectPanel,
            StartMenuUI parentUI) {
        this.playerPokemon = playerPokemon;
        this.enemyPokemon = enemyPokemon;
        this.arenaPanel = arenaPanel;
        this.headerFont = headerFont;
        this.cardLayoutInstance = cardLayout;
        this.wrapperPanelInstance = wrapperPanel;
        this.musicPlayer = musicPlayer;
        this.fadeEffectPanel = fadeEffectPanel;
        this.playerPokemon.resetHp();
        this.enemyPokemon.resetHp();
        this.parentUI = parentUI;
        this.moveCooldowns = new HashMap<>();
        this.moveCooldownLabels = new HashMap<>();
        this.turnCount = 0;

        // Initialize cooldowns for all moves
        for (Move move : playerPokemon.getMoves()) {
            moveCooldowns.put(move, 0);
        }

        initializeUI();
    }

    private void initializeUI() {
        arenaPanel.removeAll();
        arenaPanel.setLayout(null);
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

        // Player HP Label and Health Bar
        playerHPLabel = new JLabel();
        playerHPLabel.setFont(headerFont.deriveFont(16f));
        playerHPLabel.setForeground(Color.WHITE);
        playerHPLabel.setBounds(75, 320, 200, 20);
        arenaPanel.add(playerHPLabel);

        playerHealthBar = new JProgressBar(0, playerPokemon.getMaxHp());
        playerHealthBar.setValue(playerPokemon.getCurrentHp());
        playerHealthBar.setStringPainted(true);
        playerHealthBar.setForeground(new Color(0, 255, 0)); // Green color for health
        playerHealthBar.setBackground(new Color(100, 100, 100)); // Dark gray for background
        playerHealthBar.setBounds(75, 340, 200, 20);
        arenaPanel.add(playerHealthBar);

        // Enemy HP Label and Health Bar
        enemyHPLabel = new JLabel();
        enemyHPLabel.setFont(headerFont.deriveFont(16f));
        enemyHPLabel.setForeground(Color.WHITE);
        enemyHPLabel.setBounds(550, 20, 200, 20);
        arenaPanel.add(enemyHPLabel);

        enemyHealthBar = new JProgressBar(0, enemyPokemon.getMaxHp());
        enemyHealthBar.setValue(enemyPokemon.getCurrentHp());
        enemyHealthBar.setStringPainted(true);
        enemyHealthBar.setForeground(new Color(0, 255, 0)); // Green color for health
        enemyHealthBar.setBackground(new Color(100, 100, 100)); // Dark gray for background
        enemyHealthBar.setBounds(550, 40, 200, 20);
        arenaPanel.add(enemyHealthBar);

        updateHealthDisplay();

        // Status Label
        statusLabel = new JLabel("Battle Start!");
        statusLabel.setFont(headerFont.deriveFont(20f));
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setBounds(arenaPanel.getWidth() / 2 - 125, 10, 250, 30);
        arenaPanel.add(statusLabel);

        // Moves Panel with cooldown labels
        movesPanel = new JPanel();
        movesPanel.setBounds(arenaPanel.getWidth() / 2 - 125, arenaPanel.getHeight() - 120, 250, 100);
        movesPanel.setLayout(new GridLayout(2, 2, 5, 5));
        movesPanel.setOpaque(false);
        arenaPanel.add(movesPanel);

        // Create a panel for each move that contains both the cooldown label and button
        for (Move move : playerPokemon.getMoves()) {
            JPanel movePanel = new JPanel();
            movePanel.setLayout(new BoxLayout(movePanel, BoxLayout.Y_AXIS));
            movePanel.setOpaque(false);

            // Cooldown label
            JLabel cooldownLabel = new JLabel("");
            cooldownLabel.setFont(headerFont.deriveFont(14f));
            cooldownLabel.setForeground(Color.WHITE);
            cooldownLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            moveCooldownLabels.put(move, cooldownLabel);
            movePanel.add(cooldownLabel);

            // Move button
            JButton moveButton = new JButton(move.getName());
            moveButton.setFont(headerFont.deriveFont(16f));
            moveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            moveButton.addActionListener(e -> performPlayerMove(move));
            movePanel.add(moveButton);

            movesPanel.add(movePanel);
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
        if (playerPokemon != null && playerHPLabel != null && playerHealthBar != null) {
            playerHPLabel.setText(
                    playerPokemon.getName() + ": " + playerPokemon.getCurrentHp() + "/" + playerPokemon.getMaxHp());
            playerHealthBar.setMaximum(playerPokemon.getMaxHp());
            playerHealthBar.setValue(playerPokemon.getCurrentHp());

            // Update health bar color based on percentage
            int healthPercentage = (playerPokemon.getCurrentHp() * 100) / playerPokemon.getMaxHp();
            if (healthPercentage > 50) {
                playerHealthBar.setForeground(new Color(0, 255, 0)); // Green
            } else if (healthPercentage > 25) {
                playerHealthBar.setForeground(new Color(255, 255, 0)); // Yellow
            } else {
                playerHealthBar.setForeground(new Color(255, 0, 0)); // Red
            }
        }

        if (enemyPokemon != null && enemyHPLabel != null && enemyHealthBar != null) {
            enemyHPLabel.setText(
                    enemyPokemon.getName() + ": " + enemyPokemon.getCurrentHp() + "/" + enemyPokemon.getMaxHp());
            enemyHealthBar.setMaximum(enemyPokemon.getMaxHp());
            enemyHealthBar.setValue(enemyPokemon.getCurrentHp());

            // Update health bar color based on percentage
            int healthPercentage = (enemyPokemon.getCurrentHp() * 100) / enemyPokemon.getMaxHp();
            if (healthPercentage > 50) {
                enemyHealthBar.setForeground(new Color(0, 255, 0)); // Green
            } else if (healthPercentage > 25) {
                enemyHealthBar.setForeground(new Color(255, 255, 0)); // Yellow
            } else {
                enemyHealthBar.setForeground(new Color(255, 0, 0)); // Red
            }
        }
    }

    private void updateMoveButtons() {
        for (Component comp : movesPanel.getComponents()) {
            if (comp instanceof JPanel) {
                JPanel movePanel = (JPanel) comp;
                JButton moveButton = null;
                JLabel cooldownLabel = null;

                // Find the button and label in the panel
                for (Component c : movePanel.getComponents()) {
                    if (c instanceof JButton) {
                        moveButton = (JButton) c;
                    } else if (c instanceof JLabel) {
                        cooldownLabel = (JLabel) c;
                    }
                }

                if (moveButton != null && cooldownLabel != null) {
                    Move move = findMoveByName(moveButton.getText());
                    if (move != null) {
                        int cooldown = moveCooldowns.get(move);
                        if (cooldown > 0) {
                            moveButton.setEnabled(false);
                            cooldownLabel.setText("Cooldown: " + cooldown + " turns");
                            cooldownLabel.setForeground(Color.RED);
                        } else {
                            moveButton.setEnabled(true);
                            cooldownLabel.setText("Ready!");
                            cooldownLabel.setForeground(Color.GREEN);
                        }
                    }
                }
            }
        }
    }

    private void performPlayerMove(Move move) {
        // Disable all move buttons during player's turn
        disableAllMoveButtons();

        // Play move sound effect
        SFXPlayer.playSound(move.getSfxPath());

        statusLabel.setText(playerPokemon.getName() + " used " + move.getName() + "!");

        // Check if move is on cooldown
        if (moveCooldowns.get(move) > 0) {
            statusLabel.setText(move.getName() + " is on cooldown for " + moveCooldowns.get(move) + " more turns!");
            return;
        }

        // Immediately disable ALL move buttons when a move is selected
        disableAllMoveButtons();

        // Set cooldown based on move position in the moves list
        List<Move> moves = playerPokemon.getMoves();
        int moveIndex = moves.indexOf(move);

        if (moveIndex == 0) { // First attack move
            moveCooldowns.put(move, 4); // 4-turn cooldown
        } else if (moveIndex == 1) { // Second attack move
            moveCooldowns.put(move, 2); // 2-turn cooldown
        } else { // Buff and Heal moves (index 2 and 3)
            moveCooldowns.put(move, 4); // 4-turn cooldown
        }

        // Update move buttons to show cooldowns
        updateMoveButtons();

        if (move.getSfxPath() != null && !move.getSfxPath().isEmpty()) {
            try {
                SFXPlayer.playSound(move.getSfxPath());
            } catch (Exception e) {
                System.out.println("Error playing sound: " + e.getMessage());
            }
        }

        JLabel targetLabelForAnimation = enemyLabel;
        Pokemon defender = enemyPokemon;
        Pokemon attacker = playerPokemon;

        if (move.getType() == Type.HEAL || move.getType() == Type.BUFF) {
            targetLabelForAnimation = playerLabel;
        }

        int animX = targetLabelForAnimation.getX();
        int animY = targetLabelForAnimation.getY();

        showMoveAnimation(move.getGifPath(), animX, animY, () -> {
            boolean turnEnds = true;
            if (move.getType() == Type.HEAL) {
                attacker.heal(move.getPower() * 2); // Multiply heal power by 2
                statusLabel.setText(attacker.getName() + " healed itself for " + (move.getPower() * 2) + " HP!");
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
            } else {
                int damage = Battle.calculateDamage(attacker, move, defender);
                defender.takeDamage(damage);
                statusLabel.setText(attacker.getName() + " dealt " + damage + " damage to " + defender.getName() + "!");
                updateHealthDisplay();

                if (defender.isFainted()) {
                    statusLabel.setText(defender.getName() + " fainted! " + attacker.getName() + " wins!");
                    disableAllMoveButtons();
                    if (defender == playerPokemon) {
                        showDefeatPanel(parentUI.coins);
                    } else {
                        if (parentUI != null)
                            parentUI.addCoins(100);
                        showVictoryPanel(parentUI.coins - 1000, 100);
                    }
                    turnEnds = false;
                }
            }

            if (turnEnds && !defender.isFainted()) {
                // Update cooldowns
                for (Move m : moveCooldowns.keySet()) {
                    if (moveCooldowns.get(m) > 0) {
                        moveCooldowns.put(m, moveCooldowns.get(m) - 1);
                    }
                }
                turnCount++;
                updateMoveButtons();

                // Enable all move buttons after player's turn
                enableAvailableMoveButtons();

                // Enemy's turn
                Timer enemyTurnTimer = new Timer(1500, ae -> performEnemyMove());
                enemyTurnTimer.setRepeats(false);
                enemyTurnTimer.start();
            }
        });
    }

    private void disableAllMoveButtons() {
        for (Component comp : movesPanel.getComponents()) {
            if (comp instanceof JPanel) {
                JPanel movePanel = (JPanel) comp;
                for (Component c : movePanel.getComponents()) {
                    if (c instanceof JButton) {
                        c.setEnabled(false);
                    }
                }
            }
        }
    }

    private void enableAvailableMoveButtons() {
        for (Component comp : movesPanel.getComponents()) {
            if (comp instanceof JPanel) {
                JPanel movePanel = (JPanel) comp;
                for (Component c : movePanel.getComponents()) {
                    if (c instanceof JButton) {
                        JButton button = (JButton) c;
                        Move move = findMoveByName(button.getText());
                        if (move != null) {
                            // Only enable if not on cooldown
                            button.setEnabled(moveCooldowns.get(move) == 0);
                        }
                    }
                }
            }
        }
    }

    private void performEnemyMove() {
        // Disable all move buttons during enemy's turn
        disableAllMoveButtons();

        statusLabel.setText(enemyPokemon.getName() + "'s turn...");
        Timer actionDelayTimer = new Timer(1000, ae -> {
            Move move = enemyPokemon.getMoves().get((int) (Math.random() * enemyPokemon.getMoves().size()));
            statusLabel.setText(enemyPokemon.getName() + " used " + move.getName() + "!");

            // Play move sound effect
            SFXPlayer.playSound(move.getSfxPath());

            JLabel targetLabelForAnimation = playerLabel;
            Pokemon defender = playerPokemon;
            Pokemon attacker = enemyPokemon;

            if (move.getType() == Type.HEAL || move.getType() == Type.BUFF) {
                targetLabelForAnimation = enemyLabel;
            }

            int animX = targetLabelForAnimation.getX();
            int animY = targetLabelForAnimation.getY();

            showMoveAnimation(move.getGifPath(), animX, animY, () -> {
                boolean turnEnds = true;

                if (move.getType() == Type.HEAL) {
                    attacker.heal(move.getPower() * 2); // Multiply heal power by 2
                    statusLabel.setText(attacker.getName() + " healed itself for " + (move.getPower() * 2) + " HP!");
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
                } else {
                    int damage = Battle.calculateDamage(attacker, move, defender);
                    defender.takeDamage(damage);
                    statusLabel.setText(
                            attacker.getName() + " dealt " + damage + " damage to " + defender.getName() + "!");
                    updateHealthDisplay();

                    if (defender.isFainted()) {
                        statusLabel.setText(defender.getName() + " fainted! " + attacker.getName() + " wins!");
                        disableAllMoveButtons();
                        if (defender == playerPokemon) {
                            showDefeatPanel(parentUI.coins);
                        } else {
                            if (parentUI != null)
                                parentUI.addCoins(100);
                            showVictoryPanel(parentUI.coins - 1000, 1000);
                        }
                        turnEnds = false;
                    }
                }

                if (turnEnds && !defender.isFainted()) {
                    // Enable available move buttons for player's next turn
                    enableAvailableMoveButtons();
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

    private Move findMoveByName(String name) {
        for (Move move : playerPokemon.getMoves()) {
            if (move.getName().equals(name)) {
                return move;
            }
        }
        return null;
    }

    private JLabel getScaledImageLabel(String imagePath, int panelWidth, int panelHeight) {
        ImageIcon icon = new ImageIcon(imagePath);
        Image image = icon.getImage();
        Image scaledImage = image.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        JLabel label = new JLabel(scaledIcon);

        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);

        return label;
    }

    private void showVictoryPanel(int coinsBefore, int coinsAdded) {
        JPanel victoryPanel = new JPanel();
        victoryPanel.setLayout(new BoxLayout(victoryPanel, BoxLayout.Y_AXIS));
        victoryPanel.setBackground(new Color(0, 0, 0, 200));
        victoryPanel.setBounds(arenaPanel.getWidth() / 2 - 300, arenaPanel.getHeight() / 2 - 200, 600, 400);

        JLabel victoryLabel = new JLabel("VICTORY!");
        victoryLabel.setFont(headerFont.deriveFont(32f));
        victoryLabel.setForeground(Color.YELLOW);
        victoryLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel coinsLabel = new JLabel("Koin sekarang: " + parentUI.coins);
        coinsLabel.setFont(headerFont.deriveFont(20f));
        coinsLabel.setForeground(Color.WHITE);
        coinsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel plusLabel = new JLabel("(+" + coinsAdded + " koin)");
        plusLabel.setFont(headerFont.deriveFont(18f));
        plusLabel.setForeground(Color.GREEN);
        plusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Create buttons
        JButton backToMenuButton = new JButton("Kembali ke Menu Utama");
        backToMenuButton.setFont(headerFont.deriveFont(16f));
        backToMenuButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backToMenuButton.setPreferredSize(new Dimension(250, 40));
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

        JButton restartGameButton = new JButton("Main Lagi (Pilih Ulang)");
        restartGameButton.setFont(headerFont.deriveFont(16f));
        restartGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        restartGameButton.setPreferredSize(new Dimension(250, 40));
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

        victoryPanel.add(Box.createVerticalGlue());
        victoryPanel.add(victoryLabel);
        victoryPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        victoryPanel.add(coinsLabel);
        victoryPanel.add(plusLabel);
        victoryPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        victoryPanel.add(backToMenuButton);
        victoryPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        victoryPanel.add(restartGameButton);
        victoryPanel.add(Box.createVerticalGlue());

        victoryPanel.setOpaque(true);
        victoryPanel.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3));
        victoryPanel.setVisible(true);

        arenaPanel.add(victoryPanel, 0);
        arenaPanel.repaint();
    }

    private void showDefeatPanel(int coinsBefore) {
        JPanel defeatPanel = new JPanel();
        defeatPanel.setLayout(new BoxLayout(defeatPanel, BoxLayout.Y_AXIS));
        defeatPanel.setBackground(new Color(0, 0, 0, 200));
        defeatPanel.setBounds(arenaPanel.getWidth() / 2 - 300, arenaPanel.getHeight() / 2 - 200, 600, 400);

        JLabel defeatLabel = new JLabel("DEFEAT!");
        defeatLabel.setFont(headerFont.deriveFont(32f));
        defeatLabel.setForeground(Color.RED);
        defeatLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel coinsLabel = new JLabel("Koin sekarang: " + parentUI.coins);
        coinsLabel.setFont(headerFont.deriveFont(20f));
        coinsLabel.setForeground(Color.WHITE);
        coinsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel plusLabel = new JLabel("(+0 koin)");
        plusLabel.setFont(headerFont.deriveFont(18f));
        plusLabel.setForeground(Color.GRAY);
        plusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Create buttons
        JButton backToMenuButton = new JButton("Kembali ke Menu Utama");
        backToMenuButton.setFont(headerFont.deriveFont(16f));
        backToMenuButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backToMenuButton.setPreferredSize(new Dimension(250, 40));
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

        JButton restartGameButton = new JButton("Main Lagi (Pilih Ulang)");
        restartGameButton.setFont(headerFont.deriveFont(16f));
        restartGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        restartGameButton.setPreferredSize(new Dimension(250, 40));
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

        defeatPanel.add(Box.createVerticalGlue());
        defeatPanel.add(defeatLabel);
        defeatPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        defeatPanel.add(coinsLabel);
        defeatPanel.add(plusLabel);
        defeatPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        defeatPanel.add(backToMenuButton);
        defeatPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        defeatPanel.add(restartGameButton);
        defeatPanel.add(Box.createVerticalGlue());

        defeatPanel.setOpaque(true);
        defeatPanel.setBorder(BorderFactory.createLineBorder(Color.RED, 3));
        defeatPanel.setVisible(true);

        arenaPanel.add(defeatPanel, 0);
        arenaPanel.repaint();
    }
}