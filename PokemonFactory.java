import java.util.ArrayList;
import java.util.List;

public class PokemonFactory {
        public static List<Pokemon> createAllPokemons() {
                List<Pokemon> allPokemons = new ArrayList<>();

                // Pikachu Line - HP & Defense Ditingkatkan
                Pokemon pichu = new Pokemon("Pichu", Type.ELECTRIC, 95, 40, 35, // HP: 70->95, Def: 25->35
                                "./Assets/Pokemon/Pikachu/pichu.png",
                                "./Assets/Pokemon/Pikachu/pichu_back.gif",
                                "./Assets/Pokemon/Pikachu/pichu_front.gif");
                pichu.addMove(new Move("Thunder Shock", Type.ELECTRIC, 40,
                                "./Assets/Pokemon/Pikachu/attack/attack1.gif",
                                "./Assets/Sound/SFX/Pikachu/Thunder_Shock.wav"));
                pichu.addMove(new Move("Charm", Type.FAIRY, 30,
                                "./Assets/Pokemon/Pikachu/attack/attack2.gif",
                                "./Assets/Sound/SFX/Pikachu/Quick_Attack.wav"));
                // efek khusus
                pichu.addMove(new Move("Buff Attack", Type.BUFF, 15, "./Assets/buff.gif",
                                "./Assets/Sound/buff.wav")); // Power Buff disesuaikan,
                pichu.addMove(new Move("Heal", Type.HEAL, 25, "./Assets/Sound/heal.gif", "./Assets/Sound/heal.wav"));

                Pokemon pikachu = new Pokemon("Pikachu", Type.ELECTRIC, 130, 55, 50, // HP: 100->130, Def: 40->50
                                "./Assets/Pokemon/Pikachu/pikachu.png",
                                "./Assets/Pokemon/Pikachu/pikachu_back.gif",
                                "./Assets/Pokemon/Pikachu/pikachu_front.gif");
                pikachu.addMove(new Move("Charge Beam", Type.ELECTRIC, 50,
                                "./Assets/Pokemon/Pikachu/attack/attack1.gif"));
                pikachu.addMove(new Move("Quick Attack", Type.NORMAL, 40,
                                "./Assets/Pokemon/Pikachu/attack/attack2.gif"));
                pikachu.addMove(new Move("Buff Attack", Type.BUFF, 20, "./Assets/buff.gif")); // Power Buff disesuaikan
                pikachu.addMove(new Move("Heal", Type.HEAL, 35, "./Assets/heal.gif")); // Power Heal disesuaikan

                Pokemon raichu = new Pokemon("Raichu", Type.ELECTRIC, 160, 90, 65, // HP: 120->160, Def: 55->65
                                "./Assets/Pokemon/Pikachu/raichu.png",
                                "./Assets/Pokemon/Pikachu/raichu_back.gif",
                                "./Assets/Pokemon/Pikachu/raichu_front.gif");
                raichu.addMove(new Move("Thunderbolt", Type.ELECTRIC, 90,
                                "./Assets/Pokemon/Pikachu/attack/attack1.gif"));
                raichu.addMove(new Move("Iron Tail", Type.STEEL, 75,
                                "./Assets/Pokemon/Pikachu/attack/attack2.gif"));
                raichu.addMove(new Move("Buff Attack", Type.BUFF, 25, "./Assets/buff.gif")); // Power Buff disesuaikan
                raichu.addMove(new Move("Heal", Type.HEAL, 45, "./Assets/heal.gif")); // Power Heal disesuaikan

                // Squirtle Line - HP & Defense sudah cukup baik, sedikit penyesuaian
                Pokemon squirtle = new Pokemon("Squirtle", Type.WATER, 110, 48, 70, // HP: 100->110, Def: 65->70
                                "./Assets/Pokemon/Squirtle/squirtle.png",
                                "./Assets/Pokemon/Squirtle/squirtle_back.gif",
                                "./Assets/Pokemon/Squirtle/squirtle_front.gif");
                squirtle.addMove(new Move("Water Gun", Type.WATER, 40,
                                "./Assets/Pokemon/Squirtle/attack/attack1.gif"));
                squirtle.addMove(new Move("Bite", Type.DARK, 60,
                                "./Assets/Pokemon/Squirtle/attack/attack2.gif"));
                squirtle.addMove(new Move("Buff Attack", Type.BUFF, 15, "./Assets/buff.gif"));
                squirtle.addMove(new Move("Heal", Type.HEAL, 30, "./Assets/heal.gif"));

                Pokemon wartortle = new Pokemon("Wartortle", Type.WATER, 140, 63, 85, // HP: 110->140, Def: 80->85
                                "./Assets/Pokemon/Squirtle/wartortle.png",
                                "./Assets/Pokemon/Squirtle/wartortle_back.gif",
                                "./Assets/Pokemon/Squirtle/wartortle_front.gif");
                wartortle.addMove(new Move("Aqua Tail", Type.WATER, 70,
                                "./Assets/Pokemon/Squirtle/attack/attack1.gif"));
                // Withdraw diubah jadi Defense Buff yang jelas
                wartortle.addMove(new Move("Withdraw", Type.BUFF, 20, // Type jadi BUFF, Power = defense increase
                                "./Assets/Pokemon/Squirtle/attack/attack2.gif")); // GIF mungkin perlu diganti jika ini
                                                                                  // buff
                wartortle.addMove(new Move("Buff Attack", Type.BUFF, 20, "./Assets/buff.gif"));
                wartortle.addMove(new Move("Heal", Type.HEAL, 40, "./Assets/heal.gif"));

                Pokemon blastoise = new Pokemon("Blastoise", Type.WATER, 170, 83, 105, // HP: 130->170, Def: 100->105
                                "./Assets/Pokemon/Squirtle/blastoise.png",
                                "./Assets/Pokemon/Squirtle/blastoise_back.gif",
                                "./Assets/Pokemon/Squirtle/blastoise_front.gif");
                blastoise.addMove(new Move("Hydro Pump", Type.WATER, 110,
                                "./Assets/Pokemon/Squirtle/attack/attack1.gif"));
                blastoise.addMove(new Move("Skull Bash", Type.NORMAL, 85,
                                "./Assets/Pokemon/Squirtle/attack/attack2.gif"));
                blastoise.addMove(new Move("Buff Attack", Type.BUFF, 25, "./Assets/buff.gif"));
                blastoise.addMove(new Move("Heal", Type.HEAL, 50, "./Assets/heal.gif"));

                // Charmander Line - HP & Defense Ditingkatkan
                Pokemon charmander = new Pokemon("Charmander", Type.FIRE, 105, 52, 50, // HP: 100->105, Def: 43->50
                                "./Assets/Pokemon/Charizard/charmander.png",
                                "./Assets/Pokemon/Charizard/charmander_back.gif",
                                "./Assets/Pokemon/Charizard/charmander_front.gif");
                charmander.addMove(new Move("Ember", Type.FIRE, 40,
                                "./Assets/Pokemon/Charizard/attack/attack1.gif"));
                charmander.addMove(new Move("Scratch", Type.NORMAL, 40,
                                "./Assets/Pokemon/Charizard/attack/attack2.gif"));
                charmander.addMove(new Move("Buff Attack", Type.BUFF, 15, "./Assets/buff.gif"));
                charmander.addMove(new Move("Heal", Type.HEAL, 25, "./Assets/heal.gif"));

                Pokemon charmeleon = new Pokemon("Charmeleon", Type.FIRE, 135, 64, 65, // HP: 110->135, Def: 58->65
                                "./Assets/Pokemon/Charizard/charmeleon.png",
                                "./Assets/Pokemon/Charizard/charmeleon_back.gif",
                                "./Assets/Pokemon/Charizard/charmeleon_front.gif");
                charmeleon.addMove(new Move("Flame Burst", Type.FIRE, 70,
                                "./Assets/Pokemon/Charizard/attack/attack1.gif"));
                charmeleon.addMove(new Move("Dragon Rage", Type.FIRE, 60, // Dragon Rage biasanya fixed damage, tapi di
                                                                          // sini power
                                "./Assets/Pokemon/Charizard/attack/attack2.gif"));
                charmeleon.addMove(new Move("Buff Attack", Type.BUFF, 20, "./Assets/buff.gif"));
                charmeleon.addMove(new Move("Heal", Type.HEAL, 35, "./Assets/heal.gif"));

                Pokemon charizard = new Pokemon("Charizard", Type.FIRE, 165, 84, 80, // HP: 120->165, Def: 78->80
                                "./Assets/Pokemon/Charizard/charizard.png",
                                "./Assets/Pokemon/Charizard/charizard_back.gif",
                                "./Assets/Pokemon/Charizard/charizard_front.gif");
                charizard.addMove(new Move("Flamethrower", Type.FIRE, 90,
                                "./Assets/Pokemon/Charizard/attack/attack1.gif"));
                charizard.addMove(new Move("Wing Attack", Type.FLYING, 60,
                                "./Assets/Pokemon/Charizard/attack/attack2.gif"));
                charizard.addMove(new Move("Buff Attack", Type.BUFF, 25, "./Assets/buff.gif"));
                charizard.addMove(new Move("Heal", Type.HEAL, 45, "./Assets/heal.gif"));

                // Ralts Line - HP & Defense Ditingkatkan secara signifikan karena sangat rapuh
                Pokemon ralts = new Pokemon("Ralts", Type.PSYCHIC, 85, 25, 40, // HP: 60->85, Def: 25->40
                                "./Assets/Pokemon/Gardevoir/ralts.png",
                                "./Assets/Pokemon/Gardevoir/ralts_back.gif",
                                "./Assets/Pokemon/Gardevoir/ralts_front.gif");
                ralts.addMove(new Move("Confusion", Type.PSYCHIC, 50,
                                "./Assets/Pokemon/Gardevoir/attack/attack1.gif"));
                ralts.addMove(new Move("Disarming Voice", Type.FAIRY, 40,
                                "./Assets/Pokemon/Gardevoir/attack/attack2.gif"));
                ralts.addMove(new Move("Buff Attack", Type.BUFF, 15, "./Assets/buff.gif")); // Ralts lebih ke Sp.Atk,
                                                                                            // tapi Buff ini general
                ralts.addMove(new Move("Heal", Type.HEAL, 25, "./Assets/heal.gif"));

                Pokemon kirlia = new Pokemon("Kirlia", Type.PSYCHIC, 115, 35, 55, // HP: 80->115, Def: 35->55
                                "./Assets/Pokemon/Gardevoir/kirlia.png",
                                "./Assets/Pokemon/Gardevoir/kirlia_back.gif",
                                "./Assets/Pokemon/Gardevoir/kirlia_front.gif");
                kirlia.addMove(new Move("Psybeam", Type.PSYCHIC, 65,
                                "./Assets/Pokemon/Gardevoir/attack/attack1.gif"));
                kirlia.addMove(new Move("Magical Leaf", Type.GRASS, 60,
                                "./Assets/Pokemon/Gardevoir/attack/attack2.gif"));
                kirlia.addMove(new Move("Buff Attack", Type.BUFF, 20, "./Assets/buff.gif"));
                kirlia.addMove(new Move("Heal", Type.HEAL, 35, "./Assets/heal.gif"));

                Pokemon gardevoir = new Pokemon("Gardevoir", Type.PSYCHIC, 150, 65, 75, // HP: 110->150, Def: 65->75
                                // Gardevoir biasanya Sp.Atk tinggi, Atk fisik rendah. Attack 65 di sini bisa
                                // dianggap Sp.Atk.
                                "./Assets/Pokemon/Gardevoir/gardevoir.png",
                                "./Assets/Pokemon/Gardevoir/gardevoir_back.gif",
                                "./Assets/Pokemon/Gardevoir/gardevoir_front.gif");
                gardevoir.addMove(new Move("Psychic", Type.PSYCHIC, 90,
                                "./Assets/Pokemon/Gardevoir/attack/attack1.gif"));
                gardevoir.addMove(new Move("Dazzling Gleam", Type.FAIRY, 80,
                                "./Assets/Pokemon/Gardevoir/attack/attack2.gif"));
                gardevoir.addMove(new Move("Buff Attack", Type.BUFF, 25, "./Assets/buff.gif")); // Buff untuk "Special
                                                                                                // Attack"
                gardevoir.addMove(new Move("Heal", Type.HEAL, 45, "./Assets/heal.gif"));

                allPokemons.add(pichu);
                allPokemons.add(pikachu);
                allPokemons.add(raichu);
                allPokemons.add(squirtle);
                allPokemons.add(wartortle);
                allPokemons.add(blastoise);
                allPokemons.add(charmander);
                allPokemons.add(charmeleon);
                allPokemons.add(charizard);
                allPokemons.add(ralts);
                allPokemons.add(kirlia);
                allPokemons.add(gardevoir);

                return allPokemons;
        }
}