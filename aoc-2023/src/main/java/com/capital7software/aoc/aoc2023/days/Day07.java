package com.capital7software.aoc.aoc2023.days;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Day07 {

    public enum Card {
        ACE('A', 13),
        KING('K', 12),
        QUEEN('Q', 11),
        JACK('J', 10),
        TEN('T', 9),
        NINE('9', 8),
        EIGHT('8', 7),
        SEVEN('7', 6),
        SIX('6', 5),
        FIVE('5', 4),
        FOUR('4', 3),
        THREE('3', 2),
        TWO('2', 1),
        JOKER('J', 0);

        private final char label;
        private final int value;

        Card(char label, int value) {
            this.label = label;
            this.value = value;
        }

        public char getLabel() {
            return label;
        }

        public int getValue() {
            return value;
        }

        @Override
        public String toString() {
            return "Card{" +
                    "name=" + name() +
                    ", label=" + label +
                    ", value=" + value +
                    '}';
        }

        public static Card fromLabel(char label) {
            return Arrays
                    .stream(values())
                    .filter(it -> it.label == label)
                    .findFirst()
                    .orElse(null);
        }

        public static Card fromLabel(char label, boolean joker) {
            if (joker) {
                return Arrays
                        .stream(values())
                        .filter(it -> !it.name().equals("JACK"))
                        .filter(it -> it.label == label)
                        .findFirst()
                        .orElse(null);
            }
            return fromLabel(label);
        }
    }

    public enum HandType {
        FIVE_OF_A_KIND(7) {
            @Override
            public boolean isHandType(Map<Integer, Integer> frequencyMap) {
                return frequencyMap.containsKey(5);
            }
        },
        FOUR_OF_A_KIND(6) {
            @Override
            public boolean isHandType(Map<Integer, Integer> frequencyMap) {
                return frequencyMap.containsKey(4);
            }
        },
        FULL_HOUSE(5) {
            @Override
            public boolean isHandType(Map<Integer, Integer> frequencyMap) {
                return frequencyMap.containsKey(3) && frequencyMap.containsKey(2);
            }
        },
        THREE_OF_A_KIND(4) {
            @Override
            public boolean isHandType(Map<Integer, Integer> frequencyMap) {
                return frequencyMap.containsKey(3) && !frequencyMap.containsKey(2);
            }
        },
        TWO_PAIR(3) {
            @Override
            public boolean isHandType(Map<Integer, Integer> frequencyMap) {
                return frequencyMap.getOrDefault(2, 0) == 2;
            }
        },
        ONE_PAIR(2) {
            @Override
            public boolean isHandType(Map<Integer, Integer> frequencyMap) {
                return frequencyMap.getOrDefault(2, 0) == 1 && frequencyMap.getOrDefault(3, 0) == 0;
            }
        },
        HIGH_CARD(1) {
            @Override
            public boolean isHandType(Map<Integer, Integer> frequencyMap) {
                return frequencyMap.getOrDefault(1, 0) == 5;
            }
        };

        private final int strength;

        HandType(int strength) {
            this.strength = strength;
        }

        public static HandType calculateHandType(List<Card> cards) {
            return calculateHandType(cards, false);
        }

        public static HandType calculateHandType(List<Card> cards, boolean joker) {
            var cardMap = buildCardMap(cards);

            if (joker && cardMap.containsKey(Card.JOKER)) {
                // If we are playing with Jokers, then we remove the Jokers from the map and add their count to
                // the highest frequency card in the map!
                var jokerCount = cardMap.get(Card.JOKER);

                cardMap.remove(Card.JOKER);
                Card maxCard = Card.JOKER;
                int maxCount = 0;

                for (var card : cardMap.keySet()) {
                    var count = cardMap.get(card);
                    if (count > maxCount || (count == maxCount && card.getValue() > maxCard.getValue())) {
                        maxCount = count;
                        maxCard = card;
                    }
                }

                cardMap.put(maxCard, maxCount + jokerCount);
            }

            var map = buildFrequencyMap(cardMap);
            for (var value : values()) {
                if (value.isHandType(map)) {
                    return value;
                }
            }

            return null;
        }

        public static Map<Card, Integer> buildCardMap(List<Card> cards) {
            return cards.stream()
                    .distinct()
                    .collect(Collectors.toMap(
                            Function.identity(),
                            it -> Collections.frequency(cards, it)
                    ));
        }

        public static Map<Integer, Integer> buildFrequencyMap(Map<Card, Integer> cardMap) {
            var frequencies = cardMap.values();
            return frequencies.stream()
                    .distinct()
                    .collect(Collectors.toMap(
                            Function.identity(),
                            it -> Collections.frequency(frequencies, it)
                    ));
        }

        public boolean isHandType(Map<Integer, Integer> frequencyMap) { return false; }

        public int getStrength() {
            return strength;
        }

        @Override
        public String toString() {
            return "HandType{" +
                    "name=" + name() +
                    ", strength=" + strength +
                    '}';
        }
    }

    private static class Hand implements Comparable<Hand> {
        private final List<Card> cards;
        private final int bid;

        private final HandType handType;

        private final boolean joker;

        public Hand(List<Card> cards, int bid) {
            this(cards, bid, false);
        }

        public Hand(List<Card> cards, int bid, boolean joker) {
            this.cards = cards;
            this.bid = bid;
            this.joker = joker;
            this.handType = HandType.calculateHandType(this.cards, this.joker);
        }

        public List<Card> getCards() {
            return cards;
        }

        public int getBid() {
            return bid;
        }

        public boolean getJoker() {
            return joker;
        }

        public HandType getHandType() {
            return handType;
        }

        @Override
        public int compareTo(Hand o) {
            var result = 0;

            if (handType.getStrength() > o.handType.getStrength()) {
                result = 1;
            } else if (handType.getStrength() < o.handType.getStrength()) {
                result = -1;
            } else {
                // Must compare the individual cards!
                for (int i = 0; i < cards.size(); i++) {
                    if (cards.get(i).getValue() > o.cards.get(i).getValue()) {
                        result = 1;
                        break;
                    } else if (cards.get(i).getValue() < o.cards.get(i).getValue()) {
                        result = -1;
                        break;
                    }
                }
            }
            return result;
        }

        @Override
        public String toString() {
            return "Hand{" +
                    "cards=" + cards +
                    ", bid=" + bid +
                    ", joker=" + joker +
                    ", handType=" + handType +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Hand hand = (Hand) o;
            return getBid() == hand.getBid() && getJoker() == hand.getJoker() && Objects.equals(getCards(), hand.getCards()) && getHandType() == hand.getHandType();
        }

        @Override
        public int hashCode() {
            return Objects.hash(getCards(), getBid(), getJoker(), getHandType());
        }

        public static Hand from(String handWithBid) {
            return from(handWithBid, false);
        }

        public static Hand from(String handWithBid, boolean joker) {
            if (handWithBid == null || handWithBid.isBlank()) {
                throw new RuntimeException("Invalid handWithBid String: " + handWithBid);
            }

            var split = handWithBid.split(VALUE_SPLIT);

            return new Hand(parseCards(split[0], joker), Integer.parseInt(split[1]), joker);
        }

        public static List<Card> parseCards(String cards, boolean joker) {
            if (joker) {
                return cards
                        .chars()
                        .mapToObj(it -> Card.fromLabel((char)it, joker))
                        .filter(Objects::nonNull)
                        .toList();
            }
            return cards
                    .chars()
                    .mapToObj(it -> Card.fromLabel((char)it))
                    .filter(Objects::nonNull)
                    .toList();
        }
    }

    private static class CamelCards {
        private final Queue<Hand> hands = new PriorityQueue<>();

        public void addHand(Hand hand) {
            hands.add(hand);
        }

        public long calculateTotalWinnings() {
            long sum = 0;
            int currentRank = 1;
            while (hands.peek() != null) {
                var hand = hands.poll();
                sum += ((long) currentRank * hand.getBid());
                currentRank++;
            }
            return sum;
        }

        public int getHandCount() {
            return hands.size();
        }
    }

    private static final String inputFilename = "inputs/input_day_07-01.txt";
    private static final String VALUE_SPLIT = " ";

    public static void main(String[] args) throws URISyntaxException {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        var url = classloader.getResource(inputFilename);
        assert url != null;
        var path = Paths.get(url.toURI());
        var camelCards = new CamelCards();

        try (var stream = Files.lines(path)) {
            stream.forEach(it -> {
                camelCards.addHand(Hand.from(it));
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Played with a total of " + camelCards.getHandCount() + " hands");
        System.out.println("Total winnings: " + camelCards.calculateTotalWinnings());

        // Part 2
        var camelCardsWithJoker = new CamelCards();

        try (var stream = Files.lines(path)) {
            stream.forEach(it -> {
                camelCardsWithJoker.addHand(Hand.from(it, true));
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Played with a total of " + camelCardsWithJoker.getHandCount() + " hands");
        System.out.println("Total winnings: " + camelCardsWithJoker.calculateTotalWinnings());

    }
}
