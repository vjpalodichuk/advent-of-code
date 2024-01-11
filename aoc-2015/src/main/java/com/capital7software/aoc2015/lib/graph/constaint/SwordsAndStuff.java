package com.capital7software.aoc2015.lib.graph.constaint;

import com.capital7software.aoc2015.lib.util.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p><br>
 * In this game, the mage (you) and the enemy (the boss) take turns attacking.
 * The mage always goes first. Each attack reduces the opponent's hit points by at least 1.
 * The first character at or below 0 hit points loses.
 * <p><br>
 * Damage dealt by an attacker each turn is equal to the attacker's damage score minus the
 * defender's armor score. An attacker always does at least 1 damage. So, if the attacker
 * has a damage score of 8, and the defender has an armor score of 3, the defender loses
 * 5 hit points. If the defender had an armor score of 300, the defender would still lose 1 hit point.
 * <p><br>
 * Your damage score and armor score both start at zero. They can be increased by buying
 * items in exchange for gold. You start with no items and have as much gold as you need.
 * Your total damage or armor is equal to the sum of those stats from all of your items.
 * You have 100 hit points.
 * <p><br>
 * Here is what the item shop is selling:
 * <p><br>
 * Weapons:    Cost  Damage  Armor<br>
 * Dagger        8     4       0<br>
 * Shortsword   10     5       0<br>
 * Warhammer    25     6       0<br>
 * Longsword    40     7       0<br>
 * Greataxe     74     8       0<br>
 * <p>
 * Armor:      Cost  Damage  Armor<br>
 * Leather      13     0       1<br>
 * Chainmail    31     0       2<br>
 * Splintmail   53     0       3<br>
 * Bandedmail   75     0       4<br>
 * Platemail   102     0       5<br>
 * <p>
 * Rings:      Cost  Damage  Armor<br>
 * Damage +1    25     1       0<br>
 * Damage +2    50     2       0<br>
 * Damage +3   100     3       0<br>
 * Defense +1   20     0       1<br>
 * Defense +2   40     0       2<br>
 * Defense +3   80     0       3<br>
 * <p><br>
 * You must buy exactly one weapon; no dual-wielding. Armor is optional, but you can't use more than one.
 * You can buy 0-2 rings (at most one for each hand). You must use any items you buy.
 * The shop only has one of each item, so you can't buy, for example, two rings of Damage +3.
 * <p><br>
 * For example, suppose you have 8 hit points, 5 damage, and 5 armor,
 * and that the boss has 12 hit points, 7 damage, and 2 armor:
 * <p><br>
 * The mage deals 5-2 = 3 damage; the boss goes down to 9 hit points.<br>
 * The boss deals 7-5 = 2 damage; the mage goes down to 6 hit points.<br>
 * The mage deals 5-2 = 3 damage; the boss goes down to 6 hit points.<br>
 * The boss deals 7-5 = 2 damage; the mage goes down to 4 hit points.<br>
 * The mage deals 5-2 = 3 damage; the boss goes down to 3 hit points.<br>
 * The boss deals 7-5 = 2 damage; the mage goes down to 2 hit points.<br>
 * The mage deals 5-2 = 3 damage; the boss goes down to 0 hit points.<br>
 * <p><br>
 * In this scenario, the mage wins! (Barely.)
 * <p>
 *
 * @param shop   The ItemShop that this simulator will use.
 * @param player You are the mage!!
 * @param boss   The Boss is a mean one!
 */
public record SwordsAndStuff(ItemShop shop, Player player, Player boss) {

    public static final int MAX_ITERATIONS = 1_000;

    public enum ItemType {
        WEAPON(1, 1),
        ARMOR(0, 1),
        RING(0, 2);

        private final int minimum;
        private final int maximum;

        ItemType(int minimum, int maximum) {
            this.minimum = minimum;
            this.maximum = maximum;
        }

        public int getMinimum() {
            return minimum;
        }

        public int getMaximum() {
            return maximum;
        }
    }

    public record Item(ItemType type, String name, int cost, int damage, int armor) {
    }

    public record ItemShop(List<Item> weapons, List<Item> armor, List<Item> rings) { }

    public record Player(String name, AtomicInteger hitPoints, List<Item> items) {
        public Player(String name, int hitPoints) {
            this(name, new AtomicInteger(hitPoints), new ArrayList<>());
        }

        public boolean add(Item item) {
            var count = countItems(item.type());

            if (count < item.type().getMaximum() && !items.contains(item)) {
                return items.add(item);
            } else {
                return false;
            }
        }

        public long countItems(ItemType type) {
            return items.stream().filter(it -> it.type() == type).count();
        }

        public int getHitPoints() {
            return hitPoints.get();
        }

        public int getDamage() {
            return items.stream().mapToInt(Item::damage).sum();
        }

        public int getArmor() {
            return items.stream().mapToInt(Item::armor).sum();
        }

        public int getCost() {
            return items.stream().mapToInt(Item::cost).sum();
        }
    }

    /**
     * Builds a simulator instance from the specified input
     *
     * @param input The lines of input
     * @return A new SwordsAndStuff instance populated with the data from the input.
     */
    public static SwordsAndStuff buildSimulator(List<String> input) {
        var inWeapons = new AtomicBoolean(false);
        var inArmor = new AtomicBoolean(false);
        var inRings = new AtomicBoolean(false);
        var inPlayers = new AtomicBoolean(false);
        List<Item> weapons = new ArrayList<>();
        List<Item> armor = new ArrayList<>();
        List<Item> rings = new ArrayList<>();
        Player player = null;
        Player boss = null;

        for (var line : input) {
            if (line == null || line.isBlank()) {
                continue;
            }
            if (line.startsWith("Weapons")) {
                inWeapons.set(true);
                inArmor.set(false);
                inRings.set(false);
                inPlayers.set(false);
                continue;
            } else if (line.startsWith("Armor")) {
                inWeapons.set(false);
                inArmor.set(true);
                inRings.set(false);
                inPlayers.set(false);
                continue;
            } else if (line.startsWith("Rings")) {
                inWeapons.set(false);
                inArmor.set(false);
                inRings.set(true);
                inPlayers.set(false);
                continue;
            } else if (line.startsWith("Players")) {
                inWeapons.set(false);
                inArmor.set(false);
                inRings.set(false);
                inPlayers.set(true);
                continue;
            }

            if (inWeapons.get()) {
                weapons.add(parseItem(line, ItemType.WEAPON));
            } else if (inArmor.get()) {
                armor.add(parseItem(line, ItemType.ARMOR));
            } else if (inRings.get()) {
                rings.add(parseItem(line, ItemType.RING));
            } else if (inPlayers.get()) {
                var p = parsePlayer(line);

                if ("You".equals(p.name())) {
                    player = p;
                } else {
                    boss = p;
                }
            }
        }

        return new SwordsAndStuff(new ItemShop(weapons, armor, rings), player, boss);
    }

    private static Item parseItem(String line, ItemType type) {
        var split = line.split("\\s+");
        var name = type == ItemType.RING ? split[0].trim() + " " + split[1].trim() : split[0].trim();
        var cost = type == ItemType.RING ? Integer.parseInt(split[2].trim()) : Integer.parseInt(split[1].trim());
        var damage = type == ItemType.RING ? Integer.parseInt(split[3].trim()) : Integer.parseInt(split[2].trim());
        var armor = type == ItemType.RING ? Integer.parseInt(split[4].trim()) : Integer.parseInt(split[3].trim());

        return new Item(type, name, cost, damage, armor);
    }

    private static Player parsePlayer(String line) {
        var split = line.split("\\s+");
        var name = split[0].trim();
        var hitPoints = Integer.parseInt(split[1].trim());

        var player = new Player(name, hitPoints);

        if ("Boss".equals(player.name())) {
            var damage = Integer.parseInt(split[2].trim());
            var armor = Integer.parseInt(split[3].trim());

            player.add(new Item(ItemType.WEAPON, "Sword of Deception", 1_000_000, damage, 0));
            player.add(new Item(ItemType.ARMOR, "Shield of Deceit", 1_000_000, 0, armor));
        }

        return player;
    }

    /**
     * Finds the least expensive Items You can buy to win this game against the Boss.<p><br>This method returns
     * a Pair where the first property is the amount of gold spent to win the game and the second
     * property is a copy of the Mage and the items it carries.<p><br>The sum of the cost of the items in the Mage
     * is the same as that found in the first property of the Pair.<p><br>
     * When this method returns the simulation is returned to its starting state.
     *
     * @return A Pair where the first property is the amount of gold spent to win the game and the second
     * property is a copy of the Mage and the items it carries.
     */
    public Pair<Integer, Player> calculateLeastGoldSpentToWinTheGame() {
        var solver = createSolver(true);
        var min = solver.min(MAX_ITERATIONS);

        player.items.clear();

        return new Pair<>(min.first(), newPlayerFromUnknown(min.second(), player));
    }

    /**
     * Finds the most expensive Items You can buy and still Lose this game against the Boss.<p><br>This method returns
     * a Pair where the first property is the amount of gold spent to lose the game and the second
     * property is a copy of the Mage and the items it carries.<p><br>The sum of the cost of the items in the Mage
     * is the same as that found in the first property of the Pair.<p><br>
     * When this method returns the simulation is returned to its starting state.
     *
     * @return A Pair where the first property is the amount of gold spent to lose the game and the second
     * property is a copy of the Mage and the items it carries.
     */
    public Pair<Integer, Player> calculateMostAmountOfGoldAndStillLose() {
        var solver = createSolver(false);

        var max = solver.max(MAX_ITERATIONS);
        player.items.clear();

        return new Pair<>(max.first(), newPlayerFromUnknown(max.second(), player));
    }

    private Player newPlayerFromUnknown(Map<String, Integer> unknowns, Player source) {
        var newPlayer = new Player(source.name(), source.getHitPoints());
        assignItemsToPlayer(unknowns, newPlayer);
        return newPlayer;
    }

    private void assignItemsToPlayer(Map<String, Integer> unknowns, Player newPlayer) {
        newPlayer.items.add(shop.weapons().get(unknowns.get("weapon")));
        if (unknowns.get("armor") != null) {
            newPlayer.items.add(shop.armor().get(unknowns.get("armor")));
        }
        if (unknowns.get("ring1") != null) {
            newPlayer.items.add(shop.rings().get(unknowns.get("ring1")));
        }
        if (unknowns.get("ring2") != null) {
            newPlayer.items.add(shop.rings().get(unknowns.get("ring2")));
        }
    }

    private Solver<Integer> createSolver(boolean playerWins) {
        var solver = getIntegerSimpleSolver(playerWins);
        solver.setScoreFunction((items, variables) -> player.getCost());

        final Random random = new Random(System.nanoTime());
        solver.setValueDomain(new ValueDomain<>() {
            @Override
            public @NotNull Integer getRandomValue() {
                return -1;
            }

            @Override
            public @NotNull List<Integer> getRandomValues(int count) {
                var result = new ArrayList<Integer>(count);
                result.add(random.nextInt(0, shop.weapons().size()));
                if (random.nextBoolean()) {
                    result.add(random.nextInt(0, shop.armor().size()));
                } else {
                    result.add(null);
                }
                if (random.nextBoolean()) {
                    var ring1 = random.nextInt(0, shop.rings().size());
                    result.add(ring1);

                    if (random.nextBoolean()) {
                        var ring2 = random.nextInt(0, shop.rings().size());
                        if (ring1 == ring2) {
                            result.add(null);
                        } else {
                            result.add(ring2);
                        }
                    } else {
                        result.add(null);
                    }
                } else {
                    result.add(null);
                    result.add(null);
                }
                return result;
            }
        });

        return solver;
    }

    @NotNull
    private SimpleSolver<Integer> getIntegerSimpleSolver(boolean playerWins) {
        SimpleSolver<Integer> solver = new SimpleSolver<>();
        solver.addUnknown("weapon");
        solver.addUnknown("armor");
        solver.addUnknown("ring1");
        solver.addUnknown("ring2");
        solver.addVariable("playerTurnsLeft", (items) -> {
            player.items.clear();
            assignItemsToPlayer(items, player);
            return turnsTillPlayerDies(player, boss);
        });
        solver.addVariable("bossTurnsLeft", (items) -> turnsTillPlayerDies(boss, player));
        if (playerWins) {
            solver.addConstraint("playerWins", (items, variables) -> willPlayerWin());
        } else {
            solver.addConstraint("playerLoses", (items, variables) -> !willPlayerWin());
        }
        return solver;
    }

    private boolean willPlayerWin() {
        var bossTurnsLeft = turnsTillPlayerDies(boss, player);
        var playerTurnsLeft = turnsTillPlayerDies(player, boss);

        return playerTurnsLeft >= bossTurnsLeft;
    }

    private static int turnsTillPlayerDies(Player defender, Player attacker) {
        // turnsTillDeath = hit-points / damage-taken-each-turn + 1 if there is any remainder.
        // If the mage and boss both have the same turnsTillDeath the mage will win as they go first!
        var damageDealt = calculateDamageDealt(defender, attacker);
        var turnsTillDeath = defender.getHitPoints() / damageDealt;

        if (defender.getHitPoints() % damageDealt != 0) {
            turnsTillDeath++;
        }
        return turnsTillDeath;
    }

    private static int calculateDamageDealt(Player defender, Player attacker) {
        return Math.max(1, attacker.getDamage() - defender.getArmor());
    }
}
