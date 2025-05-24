public class Battle {
    public static int calculateDamage(Pokemon attacker, Move move, Pokemon defender) {
        int baseDamage = move.getPower() + attacker.getAttack() - defender.getDefense();
        return Math.max(baseDamage, 1);
    }
}
