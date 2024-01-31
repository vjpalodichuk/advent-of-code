package com.capital7software.aoc.lib.graph.constraint;

import com.capital7software.aoc.lib.util.Pair;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

/**
 * In this game, the mage (you) and the enemy (the boss) take turns attacking.
 * The mage always goes first. Each attack reduces the opponent's hit points by at least 1.
 * The first character at or below 0 hit points loses.
 *
 * <p><br>
 * Damage dealt by an attacker each turn is equal to the attacker's damage score minus the
 * defender's armor score. An attacker always does at least 1 damage. So, if the attacker
 * has a damage score of 8, and the defender has an armor score of 3, the defender loses
 * 5 hit points. If the defender had an armor score of 300, the defender would still lose
 * 1 hit point.
 *
 * <p><br>
 * Your damage score and armor score both start at zero. They can be increased by buying
 * items in exchange for gold. You start with no items and have as much gold as you need.
 * Your total damage or armor is equal to the sum of those stats from all of your items.
 * You have 100 hit points.
 *
 * <p><br>
 * Here is what the item shop is selling:
 *
 * <p><br>
 * <code>
 * Weapons: &nbsp;&nbsp;    Cost  Damage  Armor<br>
 * Dagger &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 8&nbsp;&nbsp;&nbsp; 4 &nbsp;&nbsp;&nbsp; 0<br>
 * Shortsword &nbsp;&nbsp;10 &nbsp;&nbsp;&nbsp;5 &nbsp;&nbsp;&nbsp; 0<br>
 * Warhammer &nbsp;&nbsp; 25 &nbsp;&nbsp;&nbsp;6 &nbsp;&nbsp;&nbsp; 0<br>
 * Longsword &nbsp;&nbsp; 40 &nbsp;&nbsp;&nbsp;7 &nbsp;&nbsp;&nbsp; 0<br>
 * Greataxe &nbsp;&nbsp;&nbsp; 74 &nbsp;&nbsp; 8 &nbsp;&nbsp;&nbsp; 0<br>
 * <br><br>
 * Armor: &nbsp;&nbsp;   Cost  Damage  Armor<br>
 * Leather &nbsp;&nbsp;&nbsp; 13 &nbsp;&nbsp; 0 &nbsp;&nbsp;&nbsp; 1<br>
 * Chainmail &nbsp;&nbsp;31 &nbsp;&nbsp; 0 &nbsp;&nbsp;&nbsp; 2<br>
 * Splintmail &nbsp;53 &nbsp;&nbsp; 0 &nbsp;&nbsp;&nbsp; 3<br>
 * Bandedmail &nbsp;75 &nbsp;&nbsp; 0 &nbsp;&nbsp;&nbsp; 4<br>
 * Platemail &nbsp;102 &nbsp;&nbsp; 0 &nbsp;&nbsp;&nbsp; 5<br>
 * <br><br>
 * Rings: &nbsp;&nbsp;&nbsp;&nbsp; Cost  Damage  Armor<br>
 * Damage +1 &nbsp;&nbsp; 25 &nbsp;&nbsp; 1 &nbsp;&nbsp;&nbsp; 0<br>
 * Damage +2 &nbsp;&nbsp; 50 &nbsp;&nbsp; 2 &nbsp;&nbsp;&nbsp; 0<br>
 * Damage +3 &nbsp;&nbsp;100 &nbsp;&nbsp; 3 &nbsp;&nbsp;&nbsp; 0<br>
 * Defense +1 &nbsp;&nbsp;20 &nbsp;&nbsp; 0 &nbsp;&nbsp;&nbsp; 1<br>
 * Defense +2 &nbsp;&nbsp;40 &nbsp;&nbsp; 0 &nbsp;&nbsp;&nbsp; 2<br>
 * Defense +3 &nbsp;&nbsp;80 &nbsp;&nbsp; 0 &nbsp;&nbsp;&nbsp; 3<br>
 * </code>
 *
 * <p><br>
 * You must buy exactly one weapon; no dual-wielding. Armor is optional, but you can't
 * use more than one. You can buy 0-2 rings (at most one for each hand). You must use
 * any items you buy. The shop only has one of each item, so you can't buy, for example,
 * two rings of Damage +3.
 *
 * <p><br>
 * For example, suppose you have 8 hit points, 5 damage, and 5 armor, and that the boss
 * has 12 hit points, 7 damage, and 2 armor:
 *
 * <p><br>
 * <ul>
 *     <li>
 *         The mage deals 5-2 = 3 damage; the boss goes down to 9 hit points.
 *     </li>
 *     <li>
 *         The boss deals 7-5 = 2 damage; the mage goes down to 6 hit points.
 *     </li>
 *     <li>
 *         The mage deals 5-2 = 3 damage; the boss goes down to 6 hit points.
 *     </li>
 *     <li>
 *         The boss deals 7-5 = 2 damage; the mage goes down to 4 hit points.
 *     </li>
 *     <li>
 *         The mage deals 5-2 = 3 damage; the boss goes down to 3 hit points.
 *     </li>
 *     <li>
 *         The boss deals 7-5 = 2 damage; the mage goes down to 2 hit points.
 *     </li>
 *     <li>
 *         The mage deals 5-2 = 3 damage; the boss goes down to 0 hit points.
 *     </li>
 * </ul>
 * In this scenario, the mage wins! (Barely.)
 *
 * <p><br>
 * You have 100 hit points. The boss's actual stats are in your puzzle input. What is the
 * least amount of gold you can spend and still win the fight?
 *
 * <p><br>
 * Turns out the shopkeeper is working with the boss, and can persuade you to buy whatever
 * items he wants. The other rules still apply, and he still only has one of each item.
 *
 * <p><br>
 * What is the most amount of gold you can spend and still lose the fight?
 *
 * @param shop   The ItemShop that this simulator will use.
 * @param player You are the mage!!
 * @param boss   The Boss is a mean one!
 */
public record SwordsAndStuff(
    @NotNull ItemShop shop,
    @NotNull Player player,
    @NotNull Player boss
) {

  /**
   * The default maximum number of iterations to have the Solver run for.
   */
  public static final int MAX_ITERATIONS = 100_000;

  /**
   * The different types of Items in the game that the Player can carry and equip.
   */
  @Getter
  public enum ItemType {
    /**
     * The Weapon type. The Player must carry exactly one of these types of Items.
     */
    WEAPON(1, 1),
    /**
     * The Armor type. The Player can carry zero or one of these types of Items.
     */
    ARMOR(0, 1),
    /**
     * The Ring type. The Player can carry zero to two of these types of Items.
     */
    RING(0, 2);

    /**
     * -- GETTER --
     * Returns the minimum number of Items of this type the Player must carry at once.
     */
    private final int minimum;
    /**
     * -- GETTER --
     * Returns the maximum number of Items of this type the Player can carry at once.
     */
    private final int maximum;

    /**
     * Instantiates a new ItemType with the specified minimum and maximum carry values.
     *
     * @param minimum The minimum number of Items of this type the Player must carry at once.
     * @param maximum The maximum number of Items of this type the Player can carry at once.
     */
    ItemType(int minimum, int maximum) {
      this.minimum = minimum;
      this.maximum = maximum;
    }

  }

  /**
   * A physical Item that the Player can equip.
   *
   * @param type   The type of this Item.
   * @param name   The name of this Item.
   * @param cost   The cost in gold of this Item.
   * @param damage The damage this Item does.
   * @param armor  The armor this item provides.
   */
  public record Item(@NotNull ItemType type, @NotNull String name, int cost, int damage,
                     int armor) {
  }

  /**
   * Instantiates a new ItemShop with the specified Lists of weapons, armor, and rings that
   * this ItemShop owns.
   *
   * @param weapons The list of Weapons.
   * @param armor   The List of Armor.
   * @param rings   The List of Rings.
   */
  public record ItemShop(@NotNull List<Item> weapons, @NotNull List<Item> armor,
                         @NotNull List<Item> rings) {
    /**
     * Instantiates a new ItemShop with the specified Lists of weapons, armor, and rings that
     * this ItemShop owns.
     *
     * @param weapons The list of Weapons.
     * @param armor   The List of Armor.
     * @param rings   The List of Rings.
     */
    public ItemShop(
        @NotNull List<Item> weapons,
        @NotNull List<Item> armor,
        @NotNull List<Item> rings
    ) {
      this.weapons = new ArrayList<>(weapons);
      this.armor = new ArrayList<>(armor);
      this.rings = new ArrayList<>(rings);
    }

    /**
     * Returns an unmodifiable List of Armor this Shop has.
     *
     * @return An unmodifiable List of Armor this Shop has.
     */
    @Override
    public @NotNull List<Item> armor() {
      return Collections.unmodifiableList(armor);
    }

    /**
     * Returns an unmodifiable List of Rings this Shop has.
     *
     * @return An unmodifiable List of Rings this Shop has.
     */
    @Override
    public @NotNull List<Item> rings() {
      return Collections.unmodifiableList(rings);
    }

    /**
     * Returns an unmodifiable List of Weapons this Shop has.
     *
     * @return An unmodifiable List of Weapons this Shop has.
     */
    @Override
    public @NotNull List<Item> weapons() {
      return Collections.unmodifiableList(weapons);
    }
  }

  /**
   * A data class to hold this Player's information.
   *
   * @param name      The name of this Player.
   * @param hitPoints The hit-points of this Player.
   * @param items     The items for this Player that it owns.
   */
  public record Player(@NotNull String name, @NotNull AtomicInteger hitPoints,
                       @NotNull List<Item> items) {
    /**
     * Instantiates a new Player instance with the specified name, hit-points, and Items.
     *
     * @param name      The name of this Player.
     * @param hitPoints The hit-points of this Player.
     * @param items     The items for this Player that it owns.
     */
    public Player(
        @NotNull String name,
        @NotNull AtomicInteger hitPoints,
        @NotNull List<Item> items
    ) {
      this.name = name;
      this.hitPoints = new AtomicInteger(hitPoints.get());
      this.items = new ArrayList<>(items);
    }

    /**
     * Instantiates a new Player instance with the specified name and hit-points and no Items.
     *
     * @param name      The name of this Player.
     * @param hitPoints The hit-points of this Player.
     */
    public Player(@NotNull String name, int hitPoints) {
      this(name, new AtomicInteger(hitPoints), new ArrayList<>());
    }

    /**
     * Returns the hit-points this Player has.
     *
     * @return The hit-points this Player has.
     */
    @Override
    public AtomicInteger hitPoints() {
      return new AtomicInteger(hitPoints.get());
    }

    /**
     * Returns an unmodifiable List of the Items this Player has.
     *
     * @return An unmodifiable List of the Items this Player has.
     */
    @Override
    public List<Item> items() {
      return Collections.unmodifiableList(items);
    }

    /**
     * Returns true if the specified Item was successfully added to this Player's inventory.
     *
     * @param item The Item to add.
     * @return True if the specified Item was successfully added to this Player's inventory.
     */
    public boolean add(Item item) {
      var count = countItems(item.type());

      if (count < item.type().getMaximum() && !items.contains(item)) {
        return items.add(item);
      } else {
        return false;
      }
    }

    /**
     * Returns the number of items this Player has of the specified type.
     *
     * @param type The type of Items to get the count of.
     * @return The number of items this Player has of the specified type.
     */
    public long countItems(ItemType type) {
      return items.stream().filter(it -> it.type() == type).count();
    }

    /**
     * Returns the total amount of hit-points this Player has left.
     *
     * @return The total amount of hit-points this Player has left.
     */
    public int getHitPoints() {
      return hitPoints.get();
    }

    /**
     * Returns the total amount of Damage this Player does.
     *
     * @return The total amount of Damage this Player does.
     */
    public int getDamage() {
      return items.stream().mapToInt(Item::damage).sum();
    }

    /**
     * Returns The total amount of Armor this Player has.
     *
     * @return The total amount of Armor this Player has.
     */
    public int getArmor() {
      return items.stream().mapToInt(Item::armor).sum();
    }

    /**
     * Returns the total gold spent by this Player.
     *
     * @return The total gold spent by this Player.
     */
    public int getCost() {
      return items.stream().mapToInt(Item::cost).sum();
    }
  }

  /**
   * Builds a simulator instance from the specified input.
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
    var name = type == ItemType.RING
        ? split[0].trim() + " " + split[1].trim() : split[0].trim();
    var cost = type == ItemType.RING
        ? Integer.parseInt(split[2].trim()) : Integer.parseInt(split[1].trim());
    var damage = type == ItemType.RING
        ? Integer.parseInt(split[3].trim()) : Integer.parseInt(split[2].trim());
    var armor = type == ItemType.RING
        ? Integer.parseInt(split[4].trim()) : Integer.parseInt(split[3].trim());

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

      player.add(
          new Item(ItemType.WEAPON, "Sword of Deception", 1_000_000, damage, 0)
      );
      player.add(
          new Item(ItemType.ARMOR, "Shield of Deceit", 1_000_000, 0, armor)
      );
    }

    return player;
  }

  /**
   * Finds the least expensive Items You can buy to win this game against the Boss.
   *
   * <p><br>
   * This method returns a Pair where the first property is the amount of gold spent to win the
   * game and the second property is a copy of the Mage and the items it carries.
   *
   * <p><br>
   * The sum of the cost of the items in the Mage is the same as that found in the first property
   * of the Pair.
   *
   * <p><br>
   * When this method returns the simulation is returned to its starting state.
   *
   * @return A Pair where the first property is the amount of gold spent to win the game and
   *     the second property is a copy of the Mage and the items it carries.
   */
  public Pair<Integer, Player> calculateLeastGoldSpentToWinTheGame() {
    var solver = createSolver(true);
    var min = solver.min(MAX_ITERATIONS);

    player.items.clear();

    return new Pair<>(min.first(), newPlayerFromUnknown(min.second(), player));
  }

  /**
   * Finds the most expensive Items You can buy and still Lose this game against the Boss.
   *
   * <p><br>
   * This method returns a Pair where the first property is the amount of gold spent to lose the
   * game and the second property is a copy of the Mage and the items it carries.
   *
   * <p><br>
   * The sum of the cost of the items in the Mage is the same as that found in the first property
   * of the Pair.
   *
   * <p><br>
   * When this method returns the simulation is returned to its starting state.
   *
   * @return A Pair where the first property is the amount of gold spent to lose the game and
   *     the second property is a copy of the Mage and the items it carries.
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
    solver.setValueDomain(count -> {
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
