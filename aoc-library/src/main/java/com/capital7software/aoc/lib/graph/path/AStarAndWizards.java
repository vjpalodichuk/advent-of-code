package com.capital7software.aoc.lib.graph.path;

import com.capital7software.aoc.lib.collection.PriorityQueue;
import com.capital7software.aoc.lib.graph.constaint.SwordsAndStuff;
import com.capital7software.aoc.lib.util.Pair;
import com.capital7software.aoc.lib.util.Triple;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The solution uses an A* Shortest Path algorithm to quickly and greedily find the minimum
 * amount of Mana that the mage has to spend in order to defeat the boss.
 * <p><br>
 * Combat proceeds with the mage and the boss taking alternating turns.
 * The mage still goes first. Now, however, you don't get any equipment; instead,
 * you must choose one of your spells to cast. The first character at or below 0 hit points loses.
 * <p><br>
 * Since you're a wizard, you don't get to wear armor, and you can't attack normally. However,
 * since you do magic damage, your opponent's armor is ignored, and so the boss effectively
 * has zero armor as well. As before, if armor (from a spell, in this case) would reduce damage below 1,
 * it becomes 1 instead - that is, the boss' attacks always deal at least 1 damage.
 * <p><br>
 * On each of your turns, you must select one of your spells to cast. If you cannot afford to
 * cast any spell, you lose. Spells cost mana; you start with 500 mana, but have no maximum limit.
 * You must have enough mana to cast a spell, and its cost is immediately deducted when you cast it.<br>
 * <p><br>
 * Your spells are Magic Missile, Drain, Shield, Poison, and Recharge.
 * <p><br>
 * Magic Missile costs 53 mana. It instantly does 4 damage.<br>
 * Drain costs 73 mana. It instantly does 2 damage and heals you for 2 hit points.<br><br>
 * Shield costs 113 mana. It starts an effect that lasts for 6 turns.
 * While it is active, your armor is increased by 7.<br><br>
 * Poison costs 173 mana. It starts an effect that lasts for 6 turns.
 * At the start of each turn while it is active, it deals the boss 3 damage.<br><br>
 * Recharge costs 229 mana. It starts an effect that lasts for 5 turns.
 * At the start of each turn while it is active, it gives you 101 new mana.<br><br>
 * <p><br>
 * Effects all work the same way. Effects apply at the start of both the mage's turns and the boss' turns.
 * Effects are created with a timer (the number of turns they last); at the start of each turn,
 * after they apply any effect they have, their timer is decreased by one.
 * If this decreases the timer to zero, the effect ends. You cannot cast a spell that would start an
 * effect which is already active. However, effects can be started on the same turn they end.<br><br>
 * <p><br>
 * For example, suppose the mage has 10 hit points and 250 mana, and that the boss has 13 hit points and 8 damage:
 * <p><br>
 * -- Mage turn --<br>
 * - Mage has 10 hit points, 0 armor, 250 mana<br>
 * - Boss has 13 hit points<br>
 * Mage casts Poison.<br>
 * <p><br>
 * -- Boss turn --<br>
 * - Mage has 10 hit points, 0 armor, 77 mana<br>
 * - Boss has 13 hit points<br>
 * Poison deals 3 damage; its timer is now 5.<br>
 * Boss attacks for 8 damage.<br>
 * <p><br>
 * -- Mage turn --<br>
 * - Mage has 2 hit points, 0 armor, 77 mana<br>
 * - Boss has 10 hit points<br>
 * Poison deals 3 damage; its timer is now 4.<br>
 * Mage casts Magic Missile, dealing 4 damage.<br>
 * <p><br>
 * -- Boss turn --<br>
 * - Mage has 2 hit points, 0 armor, 24 mana<br>
 * - Boss has 3 hit points<br>
 * Poison deals 3 damage. This kills the boss, and the mage wins.<br>
 * <p><br>
 * Now, suppose the same initial conditions, except that the boss has 14 hit points instead:
 * <p><br>
 * -- Mage turn --<br>
 * - Mage has 10 hit points, 0 armor, 250 mana<br>
 * - Boss has 14 hit points<br>
 * Mage casts Recharge.<br>
 * <p><br>
 * -- Boss turn --<br>
 * - Mage has 10 hit points, 0 armor, 21 mana<br>
 * - Boss has 14 hit points<br>
 * Recharge provides 101 mana; its timer is now 4.<br>
 * Boss attacks for 8 damage!<br>
 * <p><br>
 * -- Mage turn --<br>
 * - Mage has 2 hit points, 0 armor, 122 mana<br>
 * - Boss has 14 hit points<br>
 * Recharge provides 101 mana; its timer is now 3.<br>
 * Mage casts Shield, increasing armor by 7.<br>
 * <p><br>
 * -- Boss turn --<br>
 * - Mage has 2 hit points, 7 armor, 110 mana<br>
 * - Boss has 14 hit points<br>
 * Shield's timer is now 5.<br>
 * Recharge provides 101 mana; its timer is now 2.<br>
 * Boss attacks for 8 - 7 = 1 damage!<br>
 * <p><br>
 * -- Mage turn --<br>
 * - Mage has 1 hit point, 7 armor, 211 mana<br>
 * - Boss has 14 hit points<br>
 * Shield's timer is now 4.<br>
 * Recharge provides 101 mana; its timer is now 1.<br>
 * Mage casts Drain, dealing 2 damage, and healing 2 hit points.<br>
 * <p><br>
 * -- Boss turn --<br>
 * - Mage has 3 hit points, 7 armor, 239 mana<br>
 * - Boss has 12 hit points<br>
 * Shield's timer is now 3.<br>
 * Recharge provides 101 mana; its timer is now 0.<br>
 * Recharge wears off.<br>
 * Boss attacks for 8 - 7 = 1 damage!<br>
 * <p><br>
 * -- Mage turn --<br>
 * - Mage has 2 hit points, 7 armor, 340 mana<br>
 * - Boss has 12 hit points<br>
 * Shield's timer is now 2.<br>
 * Mage casts Poison.<br>
 * <p><br>
 * -- Boss turn --<br>
 * - Mage has 2 hit points, 7 armor, 167 mana<br>
 * - Boss has 12 hit points<br>
 * Shield's timer is now 1.<br>
 * Poison deals 3 damage; its timer is now 5.<br>
 * Boss attacks for 8 - 7 = 1 damage!<br>
 * <p><br>
 * -- Mage turn --<br>
 * - Mage has 1 hit point, 7 armor, 167 mana<br>
 * - Boss has 9 hit points<br>
 * Shield's timer is now 0.<br>
 * Shield wears off, decreasing armor by 7.<br>
 * Poison deals 3 damage; its timer is now 4.<br>
 * Mage casts Magic Missile, dealing 4 damage.<br>
 * <p><br>
 * -- Boss turn --<br>
 * - Mage has 1 hit point, 0 armor, 114 mana<br>
 * - Boss has 2 hit points<br>
 * Poison deals 3 damage. This kills the boss, and the mage wins.<br>
 * <p><br>
 * You start with 50 hit points and 500 mana points. The boss's actual stats are in the input.
 * What is the least amount of mana you can spend and still win the fight?
 * (Do not include mana recharge effects as "spending" negative mana.)
 * <p><br>
 * On the next run through the game, you increase the difficulty to hard.
 * <p><br>
 * At the start of each player turn (before any other effects apply),
 * you lose 1 hit point. If this brings you to or below 0 hit points, you lose.
 *
 * @param shop The SpellShop that this simulator will use.
 * @param mage You are the mage!!
 * @param boss The Boss is a mean one!
 */
public record AStarAndWizards(SpellShop shop, Mage mage, SwordsAndStuff.Player boss) {

    /**
     * Specifies the available Spell types.
     */
    public enum SpellType {
        /**
         * Instant spells fire and take effect immediately.
         */
        INSTANT,
        /**
         * Effects take effect the following turn.
         */
        EFFECT
    }

    /**
     * The Mage must cast one Spell each turn.
     *
     * @param type The type of Spell to cast.
     * @param name The name of the Spell to cast.
     * @param cost The mana cost of the Spell.
     * @param damage The amount of damage the Spell does.
     * @param armor The amount of protection the Spell offers.
     * @param health The amount of health the Spell provides.
     * @param mana The amount of mana the Spell provides.
     * @param turns If the Spell is an effect, how many turns it lasts.
     */
    public record Spell(
            @NotNull SpellType type,
            @NotNull String name,
            int cost,
            int damage,
            int armor,
            int health,
            int mana,
            int turns
    ) {
    }

    /**
     * Holds all the available Spells in the game.
     *
     * @param spells The List of spells to make available to the Mage.
     */
    public record SpellShop(@NotNull List<Spell> spells) {
        /**
         * Instantiates a new Shop and ensures the List of Spells is owned by
         * this instance.
         *
         * @param spells The List of spells to make available to the Mage.
         */
        public SpellShop(@NotNull List<Spell> spells) {
            this.spells = new ArrayList<>(spells);
        }

        /**
         * Returns an unmodifiable List of Spells available in this Shop.
         * @return An unmodifiable List of Spells available in this Shop.
         */
        @Override
        public @NotNull List<Spell> spells() {
            return Collections.unmodifiableList(spells);
        }
    }

    /**
     * The Mage is you! You play as the Mage to take on the Boss!
     *
     * @param name The name of the Mage.
     * @param hitPoints How many hit-points the Mage has.
     * @param mana How much mana the Mage has available.
     * @param spells The Spells this Mage has cast this game.
     */
    public record Mage(@NotNull String name, int hitPoints, int mana, @NotNull List<Spell> spells) {
        /**
         * Instantiates a new Mage and ensures that it owns the List of Spells.
         *
         * @param name The name of the Mage.
         * @param hitPoints How many hit-points the Mage has.
         * @param mana How much mana the Mage has available.
         * @param spells The Spells this Mage has cast this game.
         */
        public Mage(@NotNull String name, int hitPoints, int mana, @NotNull List<Spell> spells) {
            this.name = name;
            this.hitPoints = hitPoints;
            this.mana = mana;
            this.spells = new ArrayList<>(spells);
        }

        /**
         * Instantiates a new Mage with an empty List of Spells cast.
         *
         * @param name The name of the Mage.
         * @param hitPoints How many hit-points the Mage has.
         * @param mana How much mana the Mage has available.
         */
        public Mage(String name, int hitPoints, int mana) {
            this(name, hitPoints, mana, new ArrayList<>());
        }

        /**
         * Returns an unmodifiable List of Spells this Mage has cast.
         * @return An unmodifiable List of Spells this Mage has cast.
         */
        @Override
        public List<Spell> spells() {
            return Collections.unmodifiableList(spells);
        }

        /**
         * Adds a new Spell to the list of cast Spells.
         *
         * @param spell The Spell the Mage has cast.
         */
        public void add(Spell spell) {
            spells.add(spell);
        }
    }

    private static class GameState {
        private record EffectsState(
                int playerHitPoints, int playerMana, int bossHitPoints, int armor, List<Pair<Integer, Spell>> effects
        ) {
            /**
             * Returns true if the Mage is unable to cast the specified Spell.
             *
             * @param spell The Spell to check.
             * @return True if the Mage is unable to cast the specified Spell.
             */
            public boolean cannotCastSpell(Spell spell) {
                if (playerMana >= spell.cost()) {
                    if (spell.type() == SpellType.EFFECT) {
                        return effects.stream().anyMatch(it -> it.second().name().equals(spell.name()));
                    } else {
                        return false;
                    }
                } else {
                    return true;
                }
            }

        }

        private int playerHitPoints;
        private int playerMana;
        private int bossHitPoints;
        private final int bossDamage;
        private final int spentMana;
        private List<Pair<Integer, Spell>> effects;
        private final boolean hardMode;
        /**
         * -- GETTER --
         *  Returns the parent GameState or null if there is no parent.
         *
         */
        @Getter
        private final GameState parent;
        /**
         * -- GETTER --
         *  Returns the Spell that was cast during this GameState.
         *
         */
        @Getter
        private final Spell spell;

        /**
         * Instantiates a new GameState with the specified values.
         *
         * @param playerHitPoints The hit-points the Mage has left.
         * @param playerMana The mana the Mage has left.
         * @param bossHitPoints The hit-points the Boss has left.
         * @param bossDamage The amount of damage the Boss does on his turn.
         * @param spentMana The amount of the mana the Mage has spent casting Spells.
         * @param effects The currently active Effects.
         * @param hardMode If Hard mode is enabled.
         * @param parent A reference to the Parent GameState.
         * @param spell A reference to the Spell that was cast during this GameState.
         */
        public GameState(
                int playerHitPoints,
                int playerMana,
                int bossHitPoints,
                int bossDamage,
                int spentMana,
                List<Pair<Integer, Spell>> effects,
                boolean hardMode,
                GameState parent,
                Spell spell
        ) {
            this.playerHitPoints = playerHitPoints;
            this.playerMana = playerMana;
            this.bossHitPoints = bossHitPoints;
            this.bossDamage = bossDamage;
            this.spentMana = spentMana;
            this.effects = new ArrayList<>(effects);
            this.hardMode = hardMode;
            this.parent = parent;
            this.spell = spell;
        }

        /**
         * Returns true if the Boss is dead.
         * @return True if the Boss is dead.
         */
        public boolean isBossDead() {
            return bossHitPoints <= 0;
        }

        /**
         * Returns true if the Mage is still alive.
         * @return True if the Mage is still alive.
         */
        public boolean isPlayerAlive() {
            return playerHitPoints > 0;
        }

        /**
         * Casts the specified spell and applies its effects.
         *
         * @param spell The Spell to cast.
         */
        public void applySpell(Spell spell) {
            if (spell.type == SpellType.INSTANT) {
                playerHitPoints += spell.health();
                bossHitPoints -= spell.damage();
            } else {
                effects.add(new Pair<>(spell.turns(), spell));
            }
        }

        /**
         * Applies the specified EffectState and returns a new EffectState with the changes.
         *
         * @param effectsState The EffectsState to apply.
         * @return A new EffectState with the changes.
         */
        public static EffectsState applyEffects(EffectsState effectsState) {
            var armor = 0;
            var effects = new ArrayList<Pair<Integer, Spell>>(effectsState.effects.size());
            var playerHitPoints = effectsState.playerHitPoints();
            var playerMana = effectsState.playerMana();
            var bossHitPoints = effectsState.bossHitPoints();

            for (var effect : effectsState.effects) {
                bossHitPoints -= effect.second().damage();
                playerMana += effect.second().mana();
                playerHitPoints += effect.second().health();
                armor += effect.second().armor();
                if (effect.first() > 1) {
                    effects.add(new Pair<>(effect.first() - 1, effect.second()));
                }
            }
            return new EffectsState(playerHitPoints, playerMana, bossHitPoints, armor, effects);
        }

        /**
         * Applies the effects and Boss damage to this GameState.
         */
        public void bossTurn() {
            var updates = applyEffects(new EffectsState(playerHitPoints, playerMana, bossHitPoints, 0, effects));
            playerHitPoints = updates.playerHitPoints();
            playerMana = updates.playerMana();
            bossHitPoints = updates.bossHitPoints();
            effects = updates.effects();

            if (!isBossDead() && isPlayerAlive()) {
                playerHitPoints -= Math.max(1, bossDamage - updates.armor());
            }
        }

        /**
         * Returns a List of the next GameStates that are possible from this GameState.
         *
         * @param shop The SpellShop with the Spells that are available to the Mage.
         * @return A List of the next GameStates that are possible from this GameState.
         */
        public @NotNull List<GameState> getNextStates(@NotNull SpellShop shop) {
            var updates = applyEffects(
                    new EffectsState(
                            playerHitPoints - (hardMode ? 1 : 0),
                            playerMana,
                            bossHitPoints,
                            0,
                            effects
                    )
            );

            var answer = new ArrayList<GameState>(shop.spells().size());

            for (var spell : shop.spells()) {
                if (updates.cannotCastSpell(spell)) {
                    continue;
                }

                var newState = getGameState(spell, updates);

                if (newState.isPlayerAlive()) {
                    answer.add(newState);
                }
            }

            return answer;
        }

        @NotNull
        private GameState getGameState(Spell spell, EffectsState updates) {
            var newState = new GameState(
                    updates.playerHitPoints(),
                    updates.playerMana() - spell.cost(),
                    updates.bossHitPoints(),
                    bossDamage,
                    spentMana + spell.cost(),
                    updates.effects(),
                    hardMode,
                    this,
                    spell
            );
            newState.applySpell(spell);
            newState.bossTurn();
            return newState;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof GameState gameState)) {
                return false;
            }
            return playerHitPoints == gameState.playerHitPoints &&
                    playerMana == gameState.playerMana &&
                    bossHitPoints == gameState.bossHitPoints &&
                    bossDamage == gameState.bossDamage &&
                    spentMana == gameState.spentMana &&
                    hardMode == gameState.hardMode &&
                    Objects.equals(effects, gameState.effects);
        }

        @Override
        public int hashCode() {
            return Objects.hash(playerHitPoints, playerMana, bossHitPoints, bossDamage, spentMana, effects, hardMode);
        }

        @Override
        public String toString() {
            return "GameState{" +
                    "spell=" + spell +
                    ", spentMana=" + spentMana +
                    ", playerHitPoints=" + playerHitPoints +
                    ", bossHitPoints=" + bossHitPoints +
                    ", playerMana=" + playerMana +
                    ", bossDamage=" + bossDamage +
                    ", effects=" + effects +
                    ", hardMode=" + hardMode +
                    ", parent=" + parent +
                    '}';
        }

        /**
         * Traverses the parent GameStates and returns a List of all Spells cast by the Mage.
         * @return A List of all Spells cast by the Mage.
         */
        public List<Spell> getSpells() {
            var castedSpells = new ArrayList<Spell>();

            collectSpells(castedSpells);

            return castedSpells;
        }

        private void collectSpells(List<Spell> spells) {
            if (getParent() != null) {
                getParent().collectSpells(spells);
                if (getSpell() != null) {
                    spells.add(getSpell());
                }
            }
        }
    }

    /**
     * Builds a simulator instance from the specified input.
     *
     * @param input The lines of input
     * @return A new SwordsAndStuff instance populated with the data from the input.
     */
    public static AStarAndWizards buildSimulator(List<String> input) {
        var inInstants = new AtomicBoolean(false);
        var inEffects = new AtomicBoolean(false);
        var inMages = new AtomicBoolean(false);
        List<Spell> spells = new ArrayList<>();
        Mage mage = null;
        SwordsAndStuff.Player boss = null;

        for (var line : input) {
            if (line == null || line.isBlank()) {
                continue;
            }
            if (line.startsWith("Instants")) {
                inInstants.set(true);
                inEffects.set(false);
                inMages.set(false);
                continue;
            } else if (line.startsWith("Effects")) {
                inInstants.set(false);
                inEffects.set(true);
                inMages.set(false);
                continue;
            } else if (line.startsWith("Players")) {
                inInstants.set(false);
                inEffects.set(false);
                inMages.set(true);
                continue;
            }

            if (inInstants.get()) {
                spells.add(parseSpell(line, SpellType.INSTANT));
            } else if (inEffects.get()) {
                spells.add(parseSpell(line, SpellType.EFFECT));
            } else if (inMages.get()) {
                var p = parseMage(line);

                if (p.first() != null) {
                    mage = p.first();
                } else {
                    boss = p.second();
                }
            }
        }

        return new AStarAndWizards(new SpellShop(spells), mage, boss);
    }

    private static Spell parseSpell(String line, SpellType type) {
        var split = line.split("\\s+");
        var name = split[0].trim();
        var cost = Integer.parseInt(split[1].trim());
        var damage = Integer.parseInt(split[2].trim());
        var armor = Integer.parseInt(split[3].trim());
        var health = Integer.parseInt(split[4].trim());
        var mana = Integer.parseInt(split[5].trim());
        var turns = Integer.parseInt(split[6].trim());

        return new Spell(type, name, cost, damage, armor, health, mana, turns);
    }

    private static Pair<Mage, SwordsAndStuff.Player> parseMage(String line) {
        var split = line.split("\\s+");
        var name = split[0].trim();
        var hitPoints = Integer.parseInt(split[1].trim());
        var damage = Integer.parseInt(split[2].trim());
        var mana = Integer.parseInt(split[3].trim());

        if ("Boss".equals(name)) {
            var boss = new SwordsAndStuff.Player(name, hitPoints);
            boss.add(new SwordsAndStuff.Item(SwordsAndStuff.ItemType.WEAPON,
                    "Sword of Deception", 1_000_000, damage, 0));
            return new Pair<>(null, boss);
        } else {
            return new Pair<>(new Mage(name, hitPoints, mana), null);
        }
    }

    /**
     * Uses A* Shortest Path to find the minimum amount of Mana needed in order to defeat the boss.<br>
     * The primary key for selecting the next state to explore is the least amount of Mana that has currently
     * been spent and if two states share the same key, then the second key is used. The second key is the
     * order in which the states were created and so the earliest created state would be selected in cases of a tie.<br>
     *
     * @param hard If true, the mage will lose one point of damage before each turn.
     * @return A Pair where the first property is the amount of mana spent to win the game and the second
     * property is a copy of the Mage and the spells it cast.
     */
    public Pair<Integer, Mage> calculateLeastAmountOfManaAndStillWin(boolean hard) {
        var start = new GameState(
                mage.hitPoints(),
                mage.mana(),
                boss.getHitPoints(),
                boss.getDamage(),
                0,
                new ArrayList<>(),
                hard,
                null,
                null
        );

        var minimumManaSpend = findMinimumManaSpend(start);
        var newMage = new Mage(
                mage.name(),
                minimumManaSpend.playerHitPoints,
                minimumManaSpend.playerMana,
                minimumManaSpend.getSpells()
        );

        return new Pair<>(minimumManaSpend.spentMana, newMage);
    }

    private GameState findMinimumManaSpend(GameState start) {
        var gameStates = new PriorityQueue<>(50,
                Comparator
                        .comparingInt((Triple<Integer, Integer, GameState> o) -> o.first())
                        .thenComparingInt(Triple::second));

        var openStates = new HashSet<GameState>();

        openStates.add(start);

        var closedStates = new HashSet<GameState>();

        gameStates.add(new Triple<>(0, 0, start));

        var stateId = 0;

        while (!gameStates.isEmpty()) {
            var current = Objects.requireNonNull(gameStates.poll()).third();

            if (current.isBossDead()) {
                return current; // We are greedy here since we are performing a shortest-path search
            }

            openStates.remove(current); // Moving from Open to closed
            closedStates.add(current);

            for (var state : current.getNextStates(shop)) {
                if (closedStates.contains(state) || openStates.contains(state)) {
                    continue; // We already have this state covered so don't waste time doing it again!
                }
                openStates.add(state);
                gameStates.offer(new Triple<>(state.spentMana, stateId++, state));
            }
        }

        // Should never get here
        throw new IllegalStateException("Unable to find the minimum spend to still win the game!");
    }
}
