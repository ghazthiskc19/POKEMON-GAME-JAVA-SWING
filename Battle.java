public class Battle {
    public static int calculateDamage(Pokemon attacker, Move move, Pokemon defender) {
        // Increase base damage to account for 5x health
        int baseDamage = (move.getPower() + attacker.getAttack() - defender.getDefense()) * 2;
        return Math.max(baseDamage, 1);
    }
}
