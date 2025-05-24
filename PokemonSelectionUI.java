import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import javax.swing.*;

public class PokemonSelectionUI extends JFrame {
    private List<Pokemon> allPokemons;

    public PokemonSelectionUI() {
        List<Pokemon> allPokemons = PokemonFactory.createAllPokemons(); // Ambil list dari factory
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Select Your Pokemon");
        setSize(800, 600);
        setLayout(new GridLayout(2, 2));
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        for (Pokemon p : allPokemons) {
            JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout());

            // Gambar Pokémon
            ImageIcon icon = new ImageIcon(p.getImagePath());
            Image img = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            JLabel imgLabel = new JLabel(new ImageIcon(img));
            imgLabel.setHorizontalAlignment(SwingConstants.CENTER);
            panel.add(imgLabel, BorderLayout.CENTER);

            // Nama Pokémon
            JLabel nameLabel = new JLabel(p.getName());
            nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
            panel.add(nameLabel, BorderLayout.SOUTH);

            // Highlight saat hover
            panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
            panel.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    onPokemonSelected(p);
                }

                public void mouseEntered(MouseEvent e) {
                    panel.setBorder(BorderFactory.createLineBorder(Color.BLUE, 3));
                }

                public void mouseExited(MouseEvent e) {
                    panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
                }
            });

            add(panel);
        }

        setVisible(true);
    }

    private void onPokemonSelected(Pokemon selected) {
        List<Pokemon> enemies = new ArrayList<>(allPokemons);
        enemies.remove(selected);
        Pokemon enemy = enemies.get(new Random().nextInt(enemies.size()));
        dispose();
    }
}
