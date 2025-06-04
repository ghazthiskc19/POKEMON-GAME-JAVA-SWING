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
import java.util.HashSet;
import java.util.Set;

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
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}

class DarkBackgroundPanel extends JPanel {
    private Image backgroundImage;

    public DarkBackgroundPanel(String path) {
        try {
            ImageIcon icon = new ImageIcon(path);
            Image img = icon.getImage();

            BufferedImage buffered = new BufferedImage(
                    img.getWidth(null),
                    img.getHeight(null),
                    BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = buffered.createGraphics();
            g2d.drawImage(img, 0, 0, null);
            g2d.dispose();

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
    String clickSfxPath = "./Assets/Sound/SFX/Button - Sound effect.wav";
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

    boolean isPokemonLocked = false; 
    

    
    Color textColor = new Color(21, 22, 21);
    Color textColor2 = new Color(255, 255, 255);
    Color backgroundColor = new Color(181, 163, 91);
    Font headerFont;
    int coins = PlayerData.getCoins(); 
    JLabel currentMoney; 
    int ownedPokemonCount = 0; 
    JLabel currentCountPokemon; 

    private JButton fightButton; 

    private JLabel lockedInfoLabel; 

    StartMenuUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        setLocationRelativeTo(null);
        setAssets();
        
        allPokemons = PokemonFactory.createAllPokemons();
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

        
        int musicVolume = PlayerData.getMusicVolume();
        int sfxVolume = PlayerData.getSFXVolume();

        
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
        
        JButton startButton = new JButton("Start Game");
        JButton settingButton = new JButton("Settings");
        JButton resetButton = new JButton("Reset Game");
        JButton exitButton = new JButton("Exit");
        JButton[] buttons = { startButton, settingButton, resetButton, exitButton };
        editButtonAll(buttons);
        editButtonStart(buttons);

        JPanel buttonGroup = new JPanel();
        buttonGroup.setLayout(new BoxLayout(buttonGroup, BoxLayout.Y_AXIS));
        buttonGroup.setOpaque(false);

        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        settingButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        resetButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        buttonGroup.add(startButton);
        buttonGroup.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonGroup.add(settingButton);
        buttonGroup.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonGroup.add(resetButton);
        buttonGroup.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonGroup.add(exitButton);

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

        resetButton.addActionListener(e -> {
            SFXPlayer.playSound(clickSfxPath);
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to reset the game? This will delete all progress and return to the initial state.",
                    "Confirm Reset",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                
                PlayerData.clearAllData();

                
                ownedPokemonCount = 0;
                ownedPokemons.clear();
                availablePokemon.clear();
                playerPokemon = null;
                isPokemonLocked = false;
                coins = PlayerData.getCoins(); 

                
                updateOwnedPokemonLabel();
                updateCoinLabel();

                
                setShopPanel();

                
                for (Component comp : mainMenuPanel.getComponents()) {
                    if (comp instanceof JPanel) {
                        JPanel rightPanel = (JPanel) comp;
                        if (rightPanel.getComponentCount() > 0 && rightPanel.getComponent(0) instanceof JPanel) {
                            reloadFavoritePokemon(rightPanel);
                        }
                    }
                }

                
                JOptionPane.showMessageDialog(
                        this,
                        "Game has been reset successfully!",
                        "Reset Complete",
                        JOptionPane.INFORMATION_MESSAGE);
            }
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

        
        for (Component comp : left.getComponents()) {
            if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                if (button.getText().equals("Let's Fight")) {
                    button.addActionListener(e -> {
                        
                        refreshPokemonSelectionPanel();
                    });
                }
            }
        }
    }

    public void refreshMainMenuPanel() {
        
        for (Component comp : mainMenuPanel.getComponents()) {
            if (comp instanceof JPanel) {
                JPanel rightPanel = (JPanel) comp;
                if (rightPanel.getComponentCount() > 0 && rightPanel.getComponent(0) instanceof JPanel) {
                    reloadFavoritePokemon(rightPanel);
                }
            }
        }
    }

    private void setNameInputPanel() {
        nameInputPanel = new BackgroundPanel("./Assets/bg-start.jpeg");
        nameInputPanel.setLayout(new BoxLayout(nameInputPanel, BoxLayout.Y_AXIS));

        
        nameInputPanel.add(Box.createVerticalStrut(350));

        
        JLabel titleLabel = new JLabel("Fill Your Name!");
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setFont(headerFont.deriveFont(24f));
        titleLabel.setForeground(Color.BLACK);
        nameInputPanel.add(titleLabel);

        
        nameInputPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        
        JTextField nameField = new JTextField(15);
        nameField.setMaximumSize(new Dimension(300, 30));
        nameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameField.setFont(headerFont.deriveFont(24f));
        nameInputPanel.add(nameField);

        
        nameInputPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        
        JButton submitButton = new JButton("Submit");
        editButtonAll(new JButton[] { submitButton });
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        submitButton.setPreferredSize(new Dimension(300, 60));
        submitButton.setFont(headerFont.deriveFont(24f));
        submitButton.setBackground(new Color(0, 20, 20, 20));
        submitButton.setForeground(Color.BLACK);

        
        submitButton.setBorderPainted(false);
        submitButton.setFocusPainted(false);
        submitButton.setContentAreaFilled(false);
        submitButton.setOpaque(true);
        submitButton.setBorderPainted(true);
        nameInputPanel.add(submitButton);

        
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
        left.setBackground(new Color(0, 0, 0, 127)); 
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

        
        List<String> ownedPokemon = PlayerData.getOwnedPokemon();
        enterShop.setEnabled(!ownedPokemon.isEmpty());

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
        logoutButton.setBounds(50, 475, 250, 50); 
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
            fadeEffectPanel.setFadeColor(Color.black);
            fadeEffectPanel.setCurrentAlpha(0.0f);
            fadeEffectPanel.setVisible(true);
            Runnable H_afterFadeOut = () -> {
                refreshShopPanel(); 
                cardLayout.show(wrapperPanel, "Shop");
                fadeEffectPanel.startFade(0.0f, 700, () -> {
                });
            };
            fadeEffectPanel.startFade(1.0f, 700, H_afterFadeOut);
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
            fadeEffectPanel.setCurrentAlpha(0.0f);
            fadeEffectPanel.setVisible(true);
            Runnable H_afterFadeOut = () -> {
                musicPlayer.playMusic(MusicPlayer.MusicType.START_MENU);
                cardLayout.show(wrapperPanel, "NameInput");
                fadeEffectPanel.startFade(0.0f, 700, () -> {
                });
            };
            fadeEffectPanel.startFade(1.0f, 700, H_afterFadeOut);
        });
    }

    
    private void refreshShopPanel() {
        
        List<String> ownedPokemon = PlayerData.getOwnedPokemon();

        
        for (Component comp : mainMenuPanel.getComponents()) {
            if (comp instanceof JPanel) {
                JPanel left = (JPanel) comp;
                for (Component button : left.getComponents()) {
                    if (button instanceof JButton && ((JButton) button).getText().equals("Shop")) {
                        ((JButton) button).setEnabled(!ownedPokemon.isEmpty());
                        break;
                    }
                }
            }
        }

        
        setShopPanel();
    }

    private void reloadFavoritePokemon(JPanel right) {
        right.removeAll();
        right.setLayout(new OverlayLayout(right));

        JPanel pokemonWrapper = new JPanel();
        pokemonWrapper.setLayout(new BoxLayout(pokemonWrapper, BoxLayout.Y_AXIS));
        pokemonWrapper.setOpaque(false);
        int panelWidth = 300;
        int panelHeight = 300;

        
        String mostUsedPokemon = PlayerData.getMostUsedPokemon();
        String savedName = PlayerData.getPlayerName();

        if (mostUsedPokemon == null || mostUsedPokemon.isEmpty()) {
            
            JLabel noFavoriteLabel = new JLabel("<html><center>" + (savedName != null ? savedName : "Trainer") +
                    " belum memiliki<br>pokemon favorit</center></html>");
            noFavoriteLabel.setFont(headerFont.deriveFont(24f));
            noFavoriteLabel.setForeground(Color.WHITE);
            noFavoriteLabel.setHorizontalAlignment(SwingConstants.CENTER);
            noFavoriteLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            pokemonWrapper.add(Box.createVerticalGlue());
            pokemonWrapper.add(noFavoriteLabel);
            pokemonWrapper.add(Box.createVerticalGlue());
        } else {
            
            Pokemon favoritePokemon = null;
            for (Pokemon pokemon : allPokemons) {
                if (pokemon.getName().equals(mostUsedPokemon)) {
                    favoritePokemon = pokemon;
                    break;
                }
            }

            if (favoritePokemon != null) {
                
                JLabel imageBig = getScaledImageLabel(favoritePokemon.getImagePath(), panelWidth, panelHeight);
                imageBig.setAlignmentX(Component.CENTER_ALIGNMENT);
                pokemonWrapper.add(Box.createVerticalStrut(20));
                pokemonWrapper.add(imageBig);

                
                JLabel nameLabel = new JLabel(favoritePokemon.getName());
                nameLabel.setFont(headerFont.deriveFont(24f));
                nameLabel.setForeground(Color.WHITE);
                nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                pokemonWrapper.add(Box.createVerticalStrut(10));
                pokemonWrapper.add(nameLabel);

                JLabel typeLabel = new JLabel("Type: " + favoritePokemon.getType());
                typeLabel.setFont(headerFont.deriveFont(18f));
                typeLabel.setForeground(Color.WHITE);
                typeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                pokemonWrapper.add(typeLabel);

                
                JLabel statsLabel = new JLabel(String.format("HP: %d | ATK: %d | DEF: %d",
                        favoritePokemon.getMaxHp(), favoritePokemon.getAttack(), favoritePokemon.getDefense()));
                statsLabel.setFont(headerFont.deriveFont(16f));
                statsLabel.setForeground(Color.WHITE);
                statsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                pokemonWrapper.add(Box.createVerticalStrut(10));
                pokemonWrapper.add(statsLabel);

                
                JLabel movesLabel = new JLabel("Moves:");
                movesLabel.setFont(headerFont.deriveFont(16f));
                movesLabel.setForeground(Color.WHITE);
                movesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                pokemonWrapper.add(Box.createVerticalStrut(5));
                pokemonWrapper.add(movesLabel);

                for (Move move : favoritePokemon.getMoves()) {
                    JLabel moveLabel = new JLabel("- " + move.getName());
                    moveLabel.setFont(headerFont.deriveFont(14f));
                    moveLabel.setForeground(Color.WHITE);
                    moveLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                    pokemonWrapper.add(moveLabel);
                }
            }
        }

        JPanel overlay = new JPanel();
        overlay.setBackground(new Color(0, 0, 0, 127));
        overlay.setOpaque(true);

        right.add(pokemonWrapper);
        right.add(overlay);
        right.revalidate();
        right.repaint();
    }

    private void setRightMainMenu(JPanel right) {
        reloadFavoritePokemon(right);
    }

    private void setPokemonSelectionPanel() {
        pokemonSelectionPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1;

        
        JLabel infoLabel = new JLabel();
        infoLabel.setFont(headerFont.deriveFont(20f));
        infoLabel.setForeground(Color.WHITE);
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);

        
        List<String> ownedPokemonNames = PlayerData.getOwnedPokemon();
        if (ownedPokemonNames.isEmpty()) {
            infoLabel.setText(
                    "<html><center>Welcome to Pokemon Battle!<br>Choose your first Pokemon to begin your adventure.<br>This Pokemon will be your companion for future battles.</center></html>");
        } else {
            infoLabel.setText("<html><center>Select your Pokemon for battle!</center></html>");
        }

        gbc.gridy = 0;
        gbc.weighty = 0.1;
        gbc.insets = new Insets(10, 10, 10, 10);
        pokemonSelectionPanel.add(infoLabel, gbc);

        JPanel pokemonImage = new JPanel();
        JPanel pokemonButton = new JPanel();
        JPanel playExitButton = new JPanel();
        pokemonImage.setBackground(new Color(0, 0, 0, 127));
        pokemonButton.setOpaque(false);
        playExitButton.setOpaque(false);

        
        gbc.gridy = 1;
        gbc.weighty = 0.5;
        gbc.insets = new Insets(0, 0, 0, 0);
        pokemonSelectionPanel.add(pokemonImage, gbc);

        
        gbc.gridy = 2;
        gbc.weighty = 0.3;
        pokemonSelectionPanel.add(pokemonButton, gbc);

        
        gbc.gridy = 3;
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
        
        gbc.weighty = 1;

        
        JPanel left = new JPanel(new GridBagLayout()); 
        JLabel label = getScaledImageLabel(pokemon.getImagePath(), 300, 300);
        label.setVerticalAlignment(JLabel.CENTER);
        left.add(label);

        gbc.gridx = 0;
        gbc.weightx = 0.7;
        pokemonImage.add(left, gbc);

        
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

        
        JPanel rightWrapper = new JPanel(new GridBagLayout());
        rightWrapper.setOpaque(false);
        GridBagConstraints gbcRight = new GridBagConstraints();
        gbcRight.anchor = GridBagConstraints.CENTER;
        gbcRight.gridy = 0;
        gbcRight.weighty = 1;
        gbcRight.fill = GridBagConstraints.NONE;
        rightWrapper.add(right, gbcRight);

        
        gbc.gridx = 1;
        gbc.weightx = 0.3;
        pokemonImage.add(rightWrapper, gbc);

        
        pokemonImage.revalidate();
        pokemonImage.repaint();
    }

    private ArrayList<JButton> pokemonButtons = new ArrayList<>(); 

    private void setPokemonButton(JPanel pokemonButton, JPanel pokemonImage) {
        pokemonButtons.clear();
        
        
        String clickSfxPath = "./Assets/Sound/SFX/Button - Sound effect.wav";

        
        
        if (!ownedPokemons.isEmpty()) {
            for (Pokemon pokemon : ownedPokemons) {
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
                
            }
        } else {
            
            int i = 1;
            String[][] evolutionChains = {
                    { "Pichu", "Pikachu", "Raichu" },
                    { "Squirtle", "Wartortle", "Blastoise" },
                    { "Charmander", "Charmeleon", "Charizard" },
                    { "Ralts", "Kirlia", "Gardevoir" }
            };

            for (String[] chain : evolutionChains) {
                String basicPokemonName = chain[0];
                for (Pokemon pokemon : allPokemons) {
                    if (pokemon.getName().equals(basicPokemonName)) {
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
                            
                            if (playerPokemon == null) { 
                                ownedPokemonCount++;
                                updateOwnedPokemonLabel();
                                List<String> newOwnedPokemon = new ArrayList<>();
                                newOwnedPokemon.add(pokemon.getName());
                                PlayerData.saveOwnedPokemon(newOwnedPokemon);
                                PlayerData.saveCurrentPokemon(pokemon.getName());
                                ownedPokemons.add(pokemon); 

                                
                                for (Component comp : mainMenuPanel.getComponents()) {
                                    if (comp instanceof JPanel) {
                                        JPanel left = (JPanel) comp;
                                        for (Component button : left.getComponents()) {
                                            if (button instanceof JButton
                                                    && ((JButton) button).getText().equals("Shop")) {
                                                ((JButton) button).setEnabled(true);
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                            playerPokemon = pokemon; 
                            if (fightButton != null)
                                fightButton.setEnabled(true);
                            if (lockedInfoLabel != null)
                                lockedInfoLabel.setVisible(isPokemonLocked);

                            
                            
                            
                        });
                        
                        break; 
                    }
                }
            }
        }
    }

    private void setPlayExitButton(JPanel playExitButton) {
        String clickSfxPath = "./Assets/Sound/SFX/Button - Sound effect.wav";
        fightButton = new JButton("Let's Go!!");
        JButton exit = new JButton("Exit");
        JButton[] buttons = { fightButton, exit };
        editButtonBattle(fightButton);
        editButtonBattle(exit);

        playExitButton.add(exit);
        playExitButton.add(fightButton);
        fightButton.addActionListener(e -> {
            SFXPlayer.playSound(clickSfxPath);
            
            if (playerPokemon == null) {
                JOptionPane.showMessageDialog(this, "Pilih Pokemon dulu, bro!", "Pokemon Belum Dipilih",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            fadeEffectPanel.setFadeColor(Color.black);
            fadeEffectPanel.setCurrentAlpha(0.0f);
            fadeEffectPanel.setVisible(true);
            Runnable H_afterFadeOut = () -> {
                isPokemonLocked = true; 
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
                refreshMainMenuPanel(); 
                Runnable H_afterFadeIn = () -> {
                };
                fadeEffectPanel.startFade(0.0f, 700, H_afterFadeIn);
            };
            fadeEffectPanel.startFade(1.0f, 700, H_afterFadeOut);
        });
    }

    private void setArenaPanel() {
        
        List<Pokemon> availableEnemies = new ArrayList<>();
        List<String> currentOwnedNames = PlayerData.getOwnedPokemon();
        Random rand = new Random();

        
        String[][] evolutionChains = {
                { "Pichu", "Pikachu", "Raichu" },
                { "Squirtle", "Wartortle", "Blastoise" },
                { "Charmander", "Charmeleon", "Charizard" },
                { "Ralts", "Kirlia", "Gardevoir" }
        };

        
        int playerEvolutionStage = 0;
        String playerPokemonName = playerPokemon.getName();

        
        for (String[] chain : evolutionChains) {
            for (int i = 0; i < chain.length; i++) {
                if (chain[i].equals(playerPokemonName)) {
                    playerEvolutionStage = i;
                    break;
                }
            }
        }

        
        for (String[] chain : evolutionChains) {
            
            boolean isPlayerChain = false;
            for (String pokemon : chain) {
                if (pokemon.equals(playerPokemonName)) {
                    isPlayerChain = true;
                    break;
                }
            }
            if (isPlayerChain)
                continue;

            
            if (playerEvolutionStage < chain.length) {
                String enemyName = chain[playerEvolutionStage];
                for (Pokemon pokemon : allPokemons) {
                    if (pokemon.getName().equals(enemyName)) {
                        availableEnemies.add(pokemon);
                        break;
                    }
                }
            }
        }

        
        if (!availableEnemies.isEmpty()) {
            enemyPokemon = availableEnemies.get(rand.nextInt(availableEnemies.size()));
        } else {
            
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

        
        PlayerData.incrementPokemonUsage(playerPokemon.getName());

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

        
        JButton backButton = new JButton("Kembali");
        backButton.setFont(headerFont.deriveFont(24f));
        backButton.setPreferredSize(new Dimension(250, 50));
        backButton.setBackground(new Color(220, 220, 220, 180));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusable(false);
        backButton.setBorderPainted(false);
        backButton.setOpaque(true);
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        
        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                backButton.setBackground(Color.WHITE);
                backButton.setForeground(Color.BLACK);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                backButton.setBackground(new Color(220, 220, 220, 180));
                backButton.setForeground(Color.WHITE);
            }
        });

        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.insets = new Insets(30, 20, 20, 20);
        settingMenuPanel.add(backButton, gbc);

        
        musicSlider.addChangeListener(e -> {
            int value = musicSlider.getValue();
            musicVolumeLabel.setText("Volume: " + value + "%");
            if (musicPlayer != null && musicPlayer.isVolumeControlSupported()) {
                musicPlayer.setVolumePercent(value);
                PlayerData.saveMusicVolume(value);
            }
        });

        
        sfxSlider.addChangeListener(e -> {
            int value = sfxSlider.getValue();
            sfxVolumeLabel.setText("Volume: " + value + "%");
            SFXPlayer.setVolume(value);
            PlayerData.saveSFXVolume(value);
        });

        backButton.addActionListener(e -> {
            SFXPlayer.playSound(clickSfxPath);
            fadeEffectPanel.setFadeColor(Color.black);
            fadeEffectPanel.setCurrentAlpha(0.0f);
            fadeEffectPanel.setVisible(true);
            Runnable H_afterFadeOut = () -> {
                cardLayout.show(wrapperPanel, "StartMenu");
                if (musicPlayer != null) {
                    musicPlayer.playMusic(MusicPlayer.MusicType.START_MENU);
                }
                fadeEffectPanel.startFade(0.0f, 700, () -> {
                });
            };
            fadeEffectPanel.startFade(1.0f, 700, H_afterFadeOut);
        });

        settingMenuPanel.revalidate();
        settingMenuPanel.repaint();
    }

    private void setShopPanel() {
        pokemonShopPanel.removeAll();
        pokemonShopPanel.setLayout(new BoxLayout(pokemonShopPanel, BoxLayout.Y_AXIS));
        pokemonShopPanel.setBackground(new Color(240, 240, 240));
        pokemonShopPanel.setOpaque(false);

        
        JPanel coinPanel = new JPanel();
        coinPanel.setOpaque(false);
        JLabel coinLabel = new JLabel("Koin Anda: " + coins);
        coinLabel.setFont(headerFont.deriveFont(28f));
        coinLabel.setForeground(new Color(30, 30, 30));
        coinPanel.add(coinLabel);

        pokemonShopPanel.add(Box.createVerticalStrut(20));
        pokemonShopPanel.add(coinPanel);
        pokemonShopPanel.add(Box.createVerticalStrut(10));

        
        JPanel shopGrid = new JPanel(new GridLayout(1, 4, 10, 10));
        shopGrid.setOpaque(false);
        shopGrid.setMaximumSize(new Dimension(700, 500));

        
        List<Pokemon> availableForPurchase = new ArrayList<>();
        List<String> currentOwnedNames = PlayerData.getOwnedPokemon();

        
        String[][] evolutionChains = {
                { "Pichu", "Pikachu", "Raichu" },
                { "Squirtle", "Wartortle", "Blastoise" },
                { "Charmander", "Charmeleon", "Charizard" },
                { "Ralts", "Kirlia", "Gardevoir" }
        };

        
        Set<String> completeChains = new HashSet<>();
        for (String[] chain : evolutionChains) {
            String finalEvolution = chain[chain.length - 1];
            if (currentOwnedNames.contains(finalEvolution)) {
                completeChains.add(finalEvolution);
            }
        }

        
        List<String> pokemonToShowInShop = new ArrayList<>();

        if (currentOwnedNames.isEmpty()) {
            
            for (String[] chain : evolutionChains) {
                pokemonToShowInShop.add(chain[0]);
            }
        } else {
            
            for (String[] chain : evolutionChains) {
                
                if (completeChains.contains(chain[chain.length - 1])) {
                    continue;
                }

                String nextEvoToShow = null;
                
                
                for (int i = 0; i < chain.length - 1; i++) {
                    if (currentOwnedNames.contains(chain[i]) && !currentOwnedNames.contains(chain[i + 1])) {
                        nextEvoToShow = chain[i + 1];
                        break; 
                    }
                }
                
                if (nextEvoToShow == null && !currentOwnedNames.contains(chain[0])) {
                    pokemonToShowInShop.add(chain[0]);
                } else if (nextEvoToShow != null) {
                    pokemonToShowInShop.add(nextEvoToShow);
                }
            }
        }

        
        for (String pokemonName : pokemonToShowInShop) {
            for (Pokemon p : allPokemons) {
                if (p.getName().equals(pokemonName)) {
                    availableForPurchase.add(p);
                    break;
                }
            }
        }

        
        if (availableForPurchase.isEmpty()) {
            JLabel noPokemonLabel = new JLabel("No Pokemon available for purchase!");
            noPokemonLabel.setFont(headerFont.deriveFont(24f));
            noPokemonLabel.setForeground(textColor2);
            noPokemonLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            pokemonShopPanel.add(noPokemonLabel);
        } else {
            for (Pokemon pokemon : availableForPurchase) {
                JPanel pokemonCard = new JPanel() {
                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        
                        Graphics2D g2d = (Graphics2D) g.create();
                        g2d.setColor(new Color(0, 0, 0, 120)); 
                        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                        g2d.dispose();
                    }
                };
                pokemonCard.setOpaque(false);
                pokemonCard.setLayout(new BoxLayout(pokemonCard, BoxLayout.Y_AXIS));
                pokemonCard.setBackground(new Color(255, 255, 255, 200));
                pokemonCard.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2, true));
                pokemonCard.setPreferredSize(new Dimension(300, 400));

                
                JLabel pokemonGif = new JLabel(new ImageIcon(pokemon.getFrontGifPath()));
                pokemonGif.setAlignmentX(Component.CENTER_ALIGNMENT);
                pokemonGif.setPreferredSize(new Dimension(150, 150));
                pokemonCard.add(Box.createVerticalStrut(10));
                pokemonCard.add(pokemonGif);

                
                JLabel nameLabel = new JLabel(pokemon.getName());
                nameLabel.setFont(headerFont.deriveFont(20f));
                nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                nameLabel.setForeground(textColor2);
                pokemonCard.add(Box.createVerticalStrut(10));
                pokemonCard.add(nameLabel);

                
                JLabel typeLabel = new JLabel("Type: " + pokemon.getType());
                typeLabel.setFont(headerFont.deriveFont(16f));
                typeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                typeLabel.setForeground(textColor2);
                pokemonCard.add(typeLabel);

                
                JLabel statsLabel = new JLabel(String.format("HP: %d | ATK: %d | DEF: %d",
                        pokemon.getMaxHp(), pokemon.getAttack(), pokemon.getDefense()));
                statsLabel.setFont(headerFont.deriveFont(14f));
                statsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                statsLabel.setForeground(textColor2);
                pokemonCard.add(statsLabel);

                
                boolean isEvolution = !pokemon.getName().equals("Pichu") &&
                        !pokemon.getName().equals("Squirtle") &&
                        !pokemon.getName().equals("Charmander") &&
                        !pokemon.getName().equals("Ralts");

                
                int cost;
                if (!isEvolution) {
                    cost = 200; 
                } else {
                    
                    boolean isFinalEvolution = false;
                    for (String[] chain : evolutionChains) {
                        if (chain[chain.length - 1].equals(pokemon.getName())) {
                            isFinalEvolution = true;
                            break;
                        }
                    }
                    cost = isFinalEvolution ? 500 : 300; 
                }

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
                        JLabel reqLabel = new JLabel("Requires: " + prevEvoName);
                        reqLabel.setFont(headerFont.deriveFont(14f));
                        reqLabel.setForeground(new Color(100, 100, 100));
                        reqLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                        reqLabel.setForeground(textColor2);
                        pokemonCard.add(reqLabel);
                    }
                }

                
                JButton buyButton = new JButton("Buy (" + cost + " coins)");
                editButtonShop(buyButton);
                buyButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                pokemonCard.add(Box.createVerticalStrut(10));
                pokemonCard.add(buyButton);
                pokemonCard.add(Box.createVerticalStrut(10));

                
                buyButton.addActionListener(e -> {
                    SFXPlayer.playSound(clickSfxPath);
                    if (coins >= cost) {
                        
                        boolean canBuy = true;
                        String prevEvoName = null;
                        if (isEvolution) {
                            
                            for (String[] chain : evolutionChains) {
                                for (int i = 1; i < chain.length; i++) {
                                    if (chain[i].equals(pokemon.getName())) {
                                        prevEvoName = chain[i - 1];
                                        break;
                                    }
                                }
                            }
                            
                            if (prevEvoName != null) {
                                canBuy = PlayerData.getOwnedPokemon().contains(prevEvoName);
                            }
                        }

                        if (canBuy) {
                            
                            coins -= cost;
                            PlayerData.addCoins(-cost);
                            updateCoinLabel();

                            
                            List<String> ownedPokemonNamesList = new ArrayList<>(PlayerData.getOwnedPokemon());
                            ownedPokemonNamesList.add(pokemon.getName());

                            
                            if (isEvolution && prevEvoName != null) {
                                ownedPokemonNamesList.remove(prevEvoName);
                            }
                            PlayerData.saveOwnedPokemon(ownedPokemonNamesList);

                            
                            loadSavedPokemonData(); 

                            
                            JOptionPane.showMessageDialog(
                                    this,
                                    "Successfully purchased " + pokemon.getName() + "!",
                                    "Purchase Successful",
                                    JOptionPane.INFORMATION_MESSAGE);

                            
                            refreshShopPanel();
                        } else {
                            JOptionPane.showMessageDialog(
                                    this,
                                    "You need to own the previous evolution first!",
                                    "Cannot Purchase",
                                    JOptionPane.WARNING_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(
                                this,
                                "Not enough coins!",
                                "Cannot Purchase",
                                JOptionPane.WARNING_MESSAGE);
                    }
                });

                shopGrid.add(pokemonCard);
            }
        }

        pokemonShopPanel.add(shopGrid);
        pokemonShopPanel.add(Box.createVerticalStrut(20));

        
        JButton backBtn = new JButton("Kembali ke Menu");
        editButtonShop(backBtn);
        backBtn.addActionListener(e -> {
            SFXPlayer.playSound(clickSfxPath);
            fadeEffectPanel.setFadeColor(Color.black);
            fadeEffectPanel.setCurrentAlpha(0.0f);
            fadeEffectPanel.setVisible(true);
            Runnable H_afterFadeOut = () -> {
                if (PlayerData.hasPlayerName()) {
                    cardLayout.show(wrapperPanel, "MainMenu");
                    musicPlayer.playMusic(MusicPlayer.MusicType.MAIN_MENU);
                    refreshMainMenuPanel(); 
                } else {
                    cardLayout.show(wrapperPanel, "NameInput");
                }
                fadeEffectPanel.startFade(0.0f, 700, () -> {
                });
            };
            fadeEffectPanel.startFade(1.0f, 700, H_afterFadeOut);
        });

        pokemonShopPanel.add(backBtn);
        pokemonShopPanel.add(Box.createVerticalStrut(20));
        pokemonShopPanel.revalidate();
        pokemonShopPanel.repaint();
    }

    private void loadSavedPokemonData() {
        List<String> ownedPokemonNames = PlayerData.getOwnedPokemon();
        ownedPokemons.clear(); 

        if (!ownedPokemonNames.isEmpty()) {
            
            for (String pokemonName : ownedPokemonNames) {
                for (Pokemon pokemon : allPokemons) {
                    if (pokemon.getName().equals(pokemonName)) {
                        ownedPokemons.add(pokemon);
                        break;
                    }
                }
            }

            ownedPokemonCount = ownedPokemons.size();
            updateOwnedPokemonLabel();

            
            String currentPokemonName = PlayerData.getCurrentPokemon();
            if (currentPokemonName != null && !currentPokemonName.isEmpty()) {
                for (Pokemon pokemon : ownedPokemons) {
                    if (pokemon.getName().equals(currentPokemonName)) {
                        playerPokemon = pokemon;
                        break;
                    }
                }
            } else if (!ownedPokemons.isEmpty()) {
                
                playerPokemon = ownedPokemons.get(0);
                PlayerData.saveCurrentPokemon(playerPokemon.getName());
            }
        } else {
            
            ownedPokemons.clear();
            ownedPokemonCount = 0;
            updateOwnedPokemonLabel();
            playerPokemon = null;
        }
    }

    private void refreshPokemonSelectionPanel() {

        pokemonSelectionPanel.removeAll();
        pokemonSelectionPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1;

        
        
        
        
        
        

        
        List<String> ownedPokemonNames = PlayerData.getOwnedPokemon();
        
        
        
        
        
        
        
        
        

        
        
        
        

        JPanel pokemonImage = new JPanel();
        JPanel pokemonButton = new JPanel();
        JPanel playExitButton = new JPanel();
        pokemonImage.setBackground(new Color(0, 0, 0, 127));
        pokemonButton.setOpaque(false);
        playExitButton.setOpaque(false);

        
        gbc.gridy = 1;
        gbc.weighty = 0.5;
        gbc.insets = new Insets(0, 0, 0, 0);
        pokemonSelectionPanel.add(pokemonImage, gbc);

        
        gbc.gridy = 2;
        gbc.weighty = 0.3;
        pokemonSelectionPanel.add(pokemonButton, gbc);

        
        gbc.gridy = 3;
        gbc.weighty = 0.2;
        pokemonSelectionPanel.add(playExitButton, gbc);

        
        setPokemonButton(pokemonButton, pokemonImage);
        setPlayExitButton(playExitButton);

        
        pokemonSelectionPanel.revalidate();
        pokemonSelectionPanel.repaint();
    }

    private void editButtonAll(JButton[] buttons) {
        Color normal = new Color(220, 220, 220, 180);
        Color hover = Color.WHITE;
        Color text = Color.WHITE;

        for (JButton jButton : buttons) {
            jButton.setFocusable(false);
            jButton.setFont(headerFont.deriveFont(28f));
            jButton.setContentAreaFilled(true);
            jButton.setBorderPainted(false);
            jButton.setOpaque(true);
            jButton.setBackground(normal);
            jButton.setForeground(text);
            jButton.setPreferredSize(new Dimension(200, 50));

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

    private void editButtonStart(JButton[] buttons) {
        for (JButton jButton : buttons) {
            jButton.setPreferredSize(new Dimension(200, 50));
        }
    }

    private void editButtonMain(JButton[] buttons) {
        for (JButton jButton : buttons) {
            jButton.setPreferredSize(new Dimension(250, 50));
            jButton.setFont(headerFont.deriveFont(24f));
        }
    }

    private void editButtonShop(JButton button) {
        button.setFont(headerFont.deriveFont(20f));
        button.setPreferredSize(new Dimension(200, 40));
        button.setBackground(new Color(220, 220, 220, 180));
        button.setForeground(Color.WHITE);
        button.setFocusable(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.WHITE);
                button.setForeground(Color.BLACK);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(220, 220, 220, 180));
                button.setForeground(Color.WHITE);
            }
        });
    }

    private void editButtonBattle(JButton button) {
        button.setFont(headerFont.deriveFont(22f));
        button.setPreferredSize(new Dimension(180, 40));
        button.setBackground(new Color(220, 220, 220, 180));
        button.setForeground(Color.WHITE);
        button.setFocusable(false);
        button.setBorderPainted(false);
        button.setOpaque(true);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.WHITE);
                button.setForeground(Color.BLACK);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(220, 220, 220, 180));
                button.setForeground(Color.WHITE);
            }
        });
    }

    private void editButtonVictoryDefeat(JButton button) {
        button.setFont(headerFont.deriveFont(20f));
        button.setPreferredSize(new Dimension(250, 40));
        button.setBackground(new Color(220, 220, 220, 180));
        button.setForeground(Color.WHITE);
        button.setFocusable(false);
        button.setBorderPainted(false);
        button.setOpaque(true);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.WHITE);
                button.setForeground(Color.BLACK);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(220, 220, 220, 180));
                button.setForeground(Color.WHITE);
            }
        });
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
        PlayerData.addCoins(amount); 
        updateCoinLabel();
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
    private Map<Move, Integer> moveCooldowns;
    private int turnCount;
    private Map<Move, JLabel> moveCooldownLabels;

    private CardLayout cardLayoutInstance;
    private JPanel wrapperPanelInstance;
    private FadeEffectPanel fadeEffectPanel;
    private StartMenuUI parentUI;

    private void editButtonVictoryDefeat(JButton button) {
        button.setFont(headerFont.deriveFont(20f));
        button.setPreferredSize(new Dimension(250, 40));
        button.setBackground(new Color(220, 220, 220, 180));
        button.setForeground(Color.WHITE);
        button.setFocusable(false);
        button.setBorderPainted(false);
        button.setOpaque(true);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.WHITE);
                button.setForeground(Color.BLACK);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(220, 220, 220, 180));
                button.setForeground(Color.WHITE);
            }
        });
    }

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

        
        for (Move move : playerPokemon.getMoves()) {
            moveCooldowns.put(move, 0);
        }

        initializeUI();
    }

    private void initializeUI() {
        arenaPanel.removeAll();
        arenaPanel.setLayout(null);
        
        ImageIcon playerIcon = new ImageIcon(playerPokemon.getBackGifPath());
        playerLabel = new JLabel(playerIcon);
        playerLabel.setBounds(50, 350, 200, 200);
        arenaPanel.add(playerLabel);

        
        ImageIcon enemyIcon = new ImageIcon(enemyPokemon.getFrontGifPath());
        enemyLabel = new JLabel(enemyIcon);
        enemyLabel.setBounds(500, 50, 200, 200);
        arenaPanel.add(enemyLabel);

        
        playerHPLabel = new JLabel();
        playerHPLabel.setFont(headerFont.deriveFont(16f));
        playerHPLabel.setForeground(Color.WHITE);
        playerHPLabel.setBounds(75, 320, 200, 20);
        arenaPanel.add(playerHPLabel);

        playerHealthBar = new JProgressBar(0, playerPokemon.getMaxHp());
        playerHealthBar.setValue(playerPokemon.getCurrentHp());
        playerHealthBar.setStringPainted(true);
        playerHealthBar.setForeground(new Color(0, 255, 0)); 
        playerHealthBar.setBackground(new Color(100, 100, 100)); 
        playerHealthBar.setBounds(75, 340, 200, 20);
        arenaPanel.add(playerHealthBar);

        
        enemyHPLabel = new JLabel();
        enemyHPLabel.setFont(headerFont.deriveFont(16f));
        enemyHPLabel.setForeground(Color.WHITE);
        enemyHPLabel.setBounds(550, 20, 200, 20);
        arenaPanel.add(enemyHPLabel);

        enemyHealthBar = new JProgressBar(0, enemyPokemon.getMaxHp());
        enemyHealthBar.setValue(enemyPokemon.getCurrentHp());
        enemyHealthBar.setStringPainted(true);
        enemyHealthBar.setForeground(new Color(0, 255, 0)); 
        enemyHealthBar.setBackground(new Color(100, 100, 100)); 
        enemyHealthBar.setBounds(550, 40, 200, 20);
        arenaPanel.add(enemyHealthBar);

        updateHealthDisplay();

        
        statusLabel = new JLabel("Battle Start!");
        statusLabel.setFont(headerFont.deriveFont(20f));
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setBounds(arenaPanel.getWidth() / 2 - 125, 10, 250, 30);
        arenaPanel.add(statusLabel);

        
        movesPanel = new JPanel();
        movesPanel.setBounds(arenaPanel.getWidth() / 2 - 125, arenaPanel.getHeight() - 120, 250, 100);
        movesPanel.setLayout(new GridLayout(2, 2, 5, 5));
        movesPanel.setOpaque(false);
        arenaPanel.add(movesPanel);

        
        for (Move move : playerPokemon.getMoves()) {
            JPanel movePanel = new JPanel();
            movePanel.setLayout(new BoxLayout(movePanel, BoxLayout.Y_AXIS));
            movePanel.setOpaque(false);

            
            JLabel cooldownLabel = new JLabel("");
            cooldownLabel.setFont(headerFont.deriveFont(14f));
            cooldownLabel.setForeground(Color.WHITE);
            cooldownLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            moveCooldownLabels.put(move, cooldownLabel);
            movePanel.add(cooldownLabel);

            
            Color normal = new Color(220, 220, 220, 180);
            Color hover = Color.WHITE;
            Color text = Color.WHITE;
            JButton moveButton = new JButton(move.getName());
            moveButton.setFocusable(false);
            moveButton.setFont(headerFont.deriveFont(28f));
            moveButton.setContentAreaFilled(true);
            moveButton.setBorderPainted(false);
            moveButton.setOpaque(true);
            moveButton.setBackground(normal);
            moveButton.setForeground(text);
            moveButton.setPreferredSize(new Dimension(200, 40));

            moveButton.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    moveButton.setBackground(hover);
                    moveButton.setForeground(Color.BLACK);
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    moveButton.setBackground(normal);
                    moveButton.setForeground(text);
                }
            });
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

            
            int healthPercentage = (playerPokemon.getCurrentHp() * 100) / playerPokemon.getMaxHp();
            if (healthPercentage > 50) {
                playerHealthBar.setForeground(new Color(0, 255, 0)); 
            } else if (healthPercentage > 25) {
                playerHealthBar.setForeground(new Color(255, 255, 0)); 
            } else {
                playerHealthBar.setForeground(new Color(255, 0, 0)); 
            }
        }

        if (enemyPokemon != null && enemyHPLabel != null && enemyHealthBar != null) {
            enemyHPLabel.setText(
                    enemyPokemon.getName() + ": " + enemyPokemon.getCurrentHp() + "/" + enemyPokemon.getMaxHp());
            enemyHealthBar.setMaximum(enemyPokemon.getMaxHp());
            enemyHealthBar.setValue(enemyPokemon.getCurrentHp());

            
            int healthPercentage = (enemyPokemon.getCurrentHp() * 100) / enemyPokemon.getMaxHp();
            if (healthPercentage > 50) {
                enemyHealthBar.setForeground(new Color(0, 255, 0)); 
            } else if (healthPercentage > 25) {
                enemyHealthBar.setForeground(new Color(255, 255, 0)); 
            } else {
                enemyHealthBar.setForeground(new Color(255, 0, 0)); 
            }
        }
    }

    private void updateMoveButtons() {
        for (Component comp : movesPanel.getComponents()) {
            if (comp instanceof JPanel) {
                JPanel movePanel = (JPanel) comp;
                JButton moveButton = null;
                JLabel cooldownLabel = null;

                
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
        
        disableAllMoveButtons();

        
        SFXPlayer.playSound(move.getSfxPath());

        statusLabel.setText(playerPokemon.getName() + " used " + move.getName() + "!");

        
        if (moveCooldowns.get(move) > 0) {
            statusLabel.setText(move.getName() + " is on cooldown for " + moveCooldowns.get(move) + " more turns!");
            return;
        }

        
        disableAllMoveButtons();

        
        List<Move> moves = playerPokemon.getMoves();
        int moveIndex = moves.indexOf(move);

        if (moveIndex == 0) { 
            moveCooldowns.put(move, 4); 
        } else if (moveIndex == 1) { 
            moveCooldowns.put(move, 2); 
        } else { 
            moveCooldowns.put(move, 4); 
        }

        
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
                attacker.heal(move.getPower() * 2); 
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
                
                for (Move m : moveCooldowns.keySet()) {
                    if (moveCooldowns.get(m) > 0) {
                        moveCooldowns.put(m, moveCooldowns.get(m) - 1);
                    }
                }
                turnCount++;
                updateMoveButtons();

                
                enableAvailableMoveButtons();

                
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
                            
                            button.setEnabled(moveCooldowns.get(move) == 0);
                        }
                    }
                }
            }
        }
    }

    private void performEnemyMove() {
        
        disableAllMoveButtons();

        statusLabel.setText(enemyPokemon.getName() + "'s turn...");
        Timer actionDelayTimer = new Timer(1000, ae -> {
            Move move = enemyPokemon.getMoves().get((int) (Math.random() * enemyPokemon.getMoves().size()));
            statusLabel.setText(enemyPokemon.getName() + " used " + move.getName() + "!");

            
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
                    attacker.heal(move.getPower() * 2); 
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

        
        JButton backToMenuButton = new JButton("Kembali ke Menu Utama");
        editButtonVictoryDefeat(backToMenuButton);
        backToMenuButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton restartGameButton = new JButton("Main Lagi (Pilih Ulang)");
        editButtonVictoryDefeat(restartGameButton);
        restartGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        
        backToMenuButton.addActionListener(e -> {
            cardLayoutInstance.show(wrapperPanelInstance, "MainMenu");
            musicPlayer.playMusic(MusicPlayer.MusicType.MAIN_MENU);
            parentUI.refreshMainMenuPanel(); 
        });

        restartGameButton.addActionListener(e -> {
            cardLayoutInstance.show(wrapperPanelInstance, "PokemonSelection");
        });

        victoryPanel.add(Box.createVerticalGlue());
        victoryPanel.add(victoryLabel);
        victoryPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        victoryPanel.add(coinsLabel);
        victoryPanel.add(plusLabel);
        victoryPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        victoryPanel.add(backToMenuButton);
        victoryPanel.add(Box.createRigidArea(new Dimension(0, 20)));
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

        
        JButton backToMenuButton = new JButton("Kembali ke Menu Utama");
        editButtonVictoryDefeat(backToMenuButton);
        backToMenuButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton restartGameButton = new JButton("Main Lagi (Pilih Ulang)");
        editButtonVictoryDefeat(restartGameButton);
        restartGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        
        backToMenuButton.addActionListener(e -> {
            cardLayoutInstance.show(wrapperPanelInstance, "MainMenu");
            musicPlayer.playMusic(MusicPlayer.MusicType.MAIN_MENU);
            parentUI.refreshMainMenuPanel(); 
        });

        restartGameButton.addActionListener(e -> {
            cardLayoutInstance.show(wrapperPanelInstance, "PokemonSelection");
        });

        defeatPanel.add(Box.createVerticalGlue());
        defeatPanel.add(defeatLabel);
        defeatPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        defeatPanel.add(coinsLabel);
        defeatPanel.add(plusLabel);
        defeatPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        defeatPanel.add(backToMenuButton);
        defeatPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        defeatPanel.add(restartGameButton);
        defeatPanel.add(Box.createVerticalGlue());

        defeatPanel.setOpaque(true);
        defeatPanel.setBorder(BorderFactory.createLineBorder(Color.RED, 3));
        defeatPanel.setVisible(true);

        arenaPanel.add(defeatPanel, 0);
        arenaPanel.repaint();
    }
}
