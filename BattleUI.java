import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BattleUI extends JFrame {
    private Pokemon playerPokemon;
    private Pokemon enemyPokemon;
    private JLabel playerLabel;
    private JLabel enemyLabel;
    private JLabel statusLabel;
    private JPanel movesPanel;

    public BattleUI(Pokemon playerPokemon, Pokemon enemyPokemon) {
        this.playerPokemon = playerPokemon;
        this.enemyPokemon = enemyPokemon;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Pokemon Battle");
        setSize(800, 600);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        ImageIcon playerIcon = new ImageIcon(playerPokemon.getBackGifPath());
        playerLabel = new JLabel(playerIcon);
        playerLabel.setBounds(50, 350, 200, 200);
        add(playerLabel);

        // Menampilkan GIF tampak depan untuk musuh
        ImageIcon enemyIcon = new ImageIcon(enemyPokemon.getFrontGifPath());
        enemyLabel = new JLabel(enemyIcon);
        enemyLabel.setBounds(500, 0, 200, 200);
        add(enemyLabel);

        // Label untuk status
        statusLabel = new JLabel("Battle Start!");
        statusLabel.setBounds(300, 10, 200, 30);
        add(statusLabel);

        // Panel untuk tombol moves
        movesPanel = new JPanel();
        movesPanel.setBounds(250, 400, 300, 150);
        movesPanel.setLayout(new GridLayout(2, 2));
        add(movesPanel);

        // Menambahkan tombol untuk setiap move
        for (Move move : playerPokemon.getMoves()) {
            JButton moveButton = new JButton(move.getName());
            moveButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    performPlayerMove(move);
                }
            });
            movesPanel.add(moveButton);
        }

        setVisible(true);
    }

    private void performPlayerMove(Move move) {
        // Menampilkan animasi move
        showMoveAnimation(move.getGifPath(), enemyLabel.getX(), enemyLabel.getY());

        // Menghitung dan menerapkan damage
        int damage = Battle.calculateDamage(playerPokemon, move, enemyPokemon);
        enemyPokemon.takeDamage(damage);
        statusLabel.setText(playerPokemon.getName() + " used " + move.getName() + "!");

        // Cek apakah musuh kalah
        if (enemyPokemon.isFainted()) {
            statusLabel.setText(enemyPokemon.getName() + " fainted! You win!");
            disableMoveButtons();
            return;
        }

        // Giliran musuh
        performEnemyMove();
    }

    private void performEnemyMove() {
        // Musuh memilih move secara acak
        Move move = enemyPokemon.getMoves().get((int) (Math.random() * enemyPokemon.getMoves().size()));

        // Menampilkan animasi move
        showMoveAnimation(move.getGifPath(), playerLabel.getX(), playerLabel.getY());

        // Menghitung dan menerapkan damage
        int damage = Battle.calculateDamage(enemyPokemon, move, playerPokemon);
        playerPokemon.takeDamage(damage);
        statusLabel.setText(enemyPokemon.getName() + " used " + move.getName() + "!");

        // Cek apakah player kalah
        if (playerPokemon.isFainted()) {
            statusLabel.setText(playerPokemon.getName() + " fainted! You lose!");
            disableMoveButtons();
        }
    }

    private void showMoveAnimation(String gifPath, int x, int y) {
        JLabel moveLabel = new JLabel(new ImageIcon(gifPath));
        moveLabel.setBounds(x, y, 100, 100);
        add(moveLabel);
        moveLabel.repaint();

        // Timer untuk menghapus animasi setelah 1 detik
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                remove(moveLabel);
                repaint();
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void disableMoveButtons() {
        for (Component comp : movesPanel.getComponents()) {
            if (comp instanceof JButton) {
                comp.setEnabled(false);
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

    // if (lockedInfoLabel != null) lockedInfoLabel.setVisible(isPokemonLocked);
}
