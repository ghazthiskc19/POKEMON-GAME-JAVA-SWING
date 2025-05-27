import java.util.ArrayList;
import java.util.List;

public class Pokemon {
    private String name;
    private Type type;
    private int maxHp;
    private int currentHp;
    private int attack;
    private int defense;
    private String imagePath;
    private String backImagePath;
    private String frontImagePath;
    private List<Move> moves;
    private int attackBonus;
    private int defenseBonus;

    private Pokemon nextEvolution;

    public Pokemon(String name, Type type, int hp, int attack, int defense,
            String imagePath, String backImagePath, String frontImagePath) {
        this.name = name;
        this.type = type;
        this.maxHp = hp;
        this.currentHp = hp;
        this.attack = attack;
        this.defense = defense;
        this.imagePath = imagePath;
        this.backImagePath = backImagePath;
        this.frontImagePath = frontImagePath;
        this.moves = new ArrayList<>();
        this.attackBonus = 0; // Bonus attack awal = 0
        this.defenseBonus = 0; // Bonus defense awal = 0
    }

    public void evolve() {
        if (nextEvolution != null) {
            this.name = nextEvolution.name;
            this.type = nextEvolution.type;
            this.maxHp = nextEvolution.maxHp;
            this.currentHp = nextEvolution.maxHp;
            this.attack = nextEvolution.attack;
            this.defense = nextEvolution.defense;
            this.imagePath = nextEvolution.imagePath;
            this.backImagePath = nextEvolution.backImagePath;
            this.frontImagePath = nextEvolution.frontImagePath;
            this.moves = new ArrayList<>(nextEvolution.moves);
            this.nextEvolution = nextEvolution.nextEvolution;
        }
    }

    public void setNextEvolution(Pokemon nextEvolution) {
        this.nextEvolution = nextEvolution;
    }

    public boolean canEvolve() {
        return nextEvolution != null;
    }

    public void addMove(Move move) {
        moves.add(move);
    }

    public List<Move> getMoves() {
        return moves;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    // Mengembalikan HP saat ini
    public int getCurrentHp() {
        return currentHp;
    }

    // Mengembalikan HP maksimal
    public int getMaxHp() {
        return maxHp;
    }

    public int getAttack() {
        return attack + attackBonus;
    }

    public int getDefense() {
        return defense + defenseBonus;
    }

    // Method untuk menambahkan bonus attack
    public void increaseAttackBonus(int amount) {
        this.attackBonus += amount;
        if (this.attackBonus < 0)
            this.attackBonus = 0; // Bonus tidak boleh minus (opsional)
        System.out.println(this.name + " base attack: " + this.attack + ", bonus: " + this.attackBonus + ", total: "
                + getAttack()); // Debug
    }

    // Method untuk menambahkan bonus defense
    public void increaseDefenseBonus(int amount) {
        this.defenseBonus += amount;
        if (this.defenseBonus < 0)
            this.defenseBonus = 0; // Bonus tidak boleh minus (opsional)
        System.out.println(this.name + " base defense: " + this.defense + ", bonus: " + this.defenseBonus + ", total: "
                + getDefense()); // Debug
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getBackGifPath() {
        return backImagePath;
    }

    public String getFrontGifPath() {
        return frontImagePath;
    }

    public void takeDamage(int damage) {
        currentHp -= damage;
        if (currentHp < 0) {
            currentHp = 0;
        }
    }

    public boolean isFainted() {
        return currentHp <= 0;
    }

    public void heal(int amount) {
        currentHp += amount;
        if (currentHp > maxHp) {
            currentHp = maxHp;
        }
    }

    public void resetHp() {
        this.currentHp = this.maxHp;
        this.attackBonus = 0; // Reset bonus attack
        this.defenseBonus = 0; // Reset bonus defense
        this.currentHp = this.maxHp;
    }

    public void increaseMaxHp(int i) {
        throw new UnsupportedOperationException("Unimplemented method 'increaseMaxHp'");
    }
}