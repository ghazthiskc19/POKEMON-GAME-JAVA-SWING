public class Move {
    private String name;
    private Type type;
    private int power;
    private String gifPath;
    private String sfxPath;

    public Move(String name, Type type, int power, String gifPath, String sfxPath) {
        this.name = name;
        this.type = type;
        this.power = power;
        this.gifPath = gifPath;
        this.sfxPath = sfxPath;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public int getPower() {
        return power;
    }

    public String getGifPath() {
        return gifPath;
    }

    public String getSfxPath() {
        return sfxPath;
    }
}
