package corypgr.project.euler.problems;

import corypgr.project.euler.problems.util.CountMap;
import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.Value;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Problem 54
 *
 * https://projecteuler.net/problem=54
 *
 * A bit of a change of pace here. This problem is more of a chance to put some object oriented structure around things.
 * No tricks to getting this right.
 */
public class PE0054 implements Problem {
    private static final String FILE_PATH = "src/main/java/corypgr/project/euler/problems/resources/PE0054_hands";

    @Override
    @SneakyThrows
    public ProblemSolution solve() {
        long player1WinCount = getPlayerRounds().stream()
                .filter(round -> round.getPlayer1().compareTo(round.getPlayer2()) > 0)
                .count();

        return ProblemSolution.builder()
                .solution(player1WinCount)
                .descriptiveSolution("Number of times player 1 wins: " + player1WinCount)
                .build();
    }

    @SneakyThrows
    private List<TwoPlayerRound> getPlayerRounds() {
        return Files.lines(Paths.get(FILE_PATH))
                .map(line -> Arrays.stream(line.split(" ")).collect(Collectors.toList()))
                .map(this::getRoundFromCardList)
                .collect(Collectors.toList());
    }

    private TwoPlayerRound getRoundFromCardList(List<String> cards) {
        Hand player1 = HandFactory.parseHand(cards.subList(0, cards.size() / 2));
        Hand player2 = HandFactory.parseHand(cards.subList(cards.size() / 2, cards.size()));
        return TwoPlayerRound.builder()
                .player1(player1)
                .player2(player2)
                .build();
    }

    @Value
    @Builder
    private static class TwoPlayerRound {
        private final Hand player1;
        private final Hand player2;
    }

    private static class HandFactory {
        public static Hand parseHand(List<String> cardsAsStrings) {
            List<Card> cards = cardsAsStrings.stream()
                    .map(HandFactory::stringToCard)
                    .collect(Collectors.toList());

            if (RoyalFlush.canCreateFrom(cards)) {
                return new RoyalFlush(cards);
            }
            if (StraightFlush.canCreateFrom(cards)) {
                return new StraightFlush(cards);
            }
            if (FourOfAKind.canCreateFrom(cards)) {
                return new FourOfAKind(cards);
            }
            if (FullHouse.canCreateFrom(cards)) {
                return new FullHouse(cards);
            }
            if (Flush.canCreateFrom(cards)) {
                return new Flush(cards);
            }
            if (Straight.canCreateFrom(cards)) {
                return new Straight(cards);
            }
            if (ThreeOfAKind.canCreateFrom(cards)) {
                return new ThreeOfAKind(cards);
            }
            if (TwoPairs.canCreateFrom(cards)) {
                return new TwoPairs(cards);
            }
            if (OnePair.canCreateFrom(cards)) {
                return new OnePair(cards);
            }
            return new HighCard(cards);
        }

        private static Card stringToCard(String stringVal) {
            if (stringVal.length() != 2) {
                throw new IllegalArgumentException("String representation of cards should be 2 chars. " +
                        "Invalid val: " + stringVal);
            }

            Rank rank = Rank.fromRankChar(stringVal.charAt(0));
            Suit suit = Suit.fromSuitChar(stringVal.charAt(1));
            return new Card(suit, rank);
        }
    }

    private static abstract class Hand implements Comparable<Hand> {
        protected Hand(List<Card> cards) {
            this.cards = cards;
        }

        /**
         * The weight of each hand type. i.e. flush vs straight.
         */
        abstract int getHandTypeWeight();

        /**
         * Within a hand type, the rank determines which of two hands are better. This returns the relevant ranks in
         * order of importance. For example, in Two Pair, this would have the higher rank of the 2 pairs, then the lower
         * rank of the pairs, then the remaining card. In High Card, this is the sorted list of cards where the highest
         * rank is first.
         * @return
         */
        abstract List<Rank> getRelevantRanksInOrder();

        private final List<Card> cards;

        @Override
        public int compareTo(Hand other) {
            int handTypeDifference = this.getHandTypeWeight() - other.getHandTypeWeight();
            if (handTypeDifference != 0) {
                return handTypeDifference;
            }

            // If handTypeWeights are equal then the rank weights are the same length;
            for (int i = 0; i < this.getRelevantRanksInOrder().size(); i++) {
                int rankDifference = this.getRelevantRanksInOrder().get(i).getWeightVal() -
                        other.getRelevantRanksInOrder().get(i).getWeightVal();
                if (rankDifference != 0) {
                    return rankDifference;
                }
            }

            // Effectively the same hand.
            return 0;
        }
    }

    private static class RoyalFlush extends StraightFlush {
        public RoyalFlush(List<Card> cards) {
            super(cards);

            if (!RoyalFlush.canCreateFrom(cards)) {
                throw new IllegalArgumentException("Cannot create from given cards.");
            }
        }

        public static boolean canCreateFrom(List<Card> cards) {
            Set<Rank> ranks = cards.stream()
                    .map(Card::getRank)
                    .collect(Collectors.toSet());

            return StraightFlush.canCreateFrom(cards) && ranks.contains(Rank.ACE) && ranks.contains(Rank.KING);
        }

        @Override
        int getHandTypeWeight() {
            return 10;
        }
    }

    private static class StraightFlush extends Straight {
        public StraightFlush(List<Card> cards) {
            super(cards);

            // Only check flush here. The call to super checks that is a valid straight.
            if (!Flush.canCreateFrom(cards)) {
                throw new IllegalArgumentException("Cannot create from given cards.");
            }
        }

        public static boolean canCreateFrom(List<Card> cards) {
            return Straight.canCreateFrom(cards) && Flush.canCreateFrom(cards);
        }

        @Override
        int getHandTypeWeight() {
            return 9;
        }
    }

    private static class FourOfAKind extends Hand {
        public FourOfAKind(List<Card> cards) {
            super(cards);
            if (!FourOfAKind.canCreateFrom(cards)) {
                throw new IllegalArgumentException("Cannot create from given cards.");
            }

            CountMap<Rank> rankCounts = getCountMapFrom(cards);
            // Guaranteed only 2 entries.
            Iterator<Map.Entry<Rank, Integer>> rankEntryIter = rankCounts.entrySet().iterator();
            Map.Entry<Rank, Integer> rankEntryA = rankEntryIter.next();
            Map.Entry<Rank, Integer> rankEntryB = rankEntryIter.next();
            if (rankEntryA.getValue() == 4) {
                this.relevantRanksInOrder = List.of(rankEntryA.getKey(), rankEntryB.getKey());
            } else {
                this.relevantRanksInOrder = List.of(rankEntryB.getKey(), rankEntryA.getKey());
            }
        }

        public static boolean canCreateFrom(List<Card> cards) {
            CountMap<Rank> rankCounts = getCountMapFrom(cards);

            return rankCounts.containsValue(4);
        }

        private static CountMap<Rank> getCountMapFrom(List<Card> cards) {
            CountMap<Rank> rankCounts = new CountMap<>();
            cards.stream()
                    .map(Card::getRank)
                    .forEach(rankCounts::addCount);
            return rankCounts;
        }

        @Getter
        private final List<Rank> relevantRanksInOrder;

        @Override
        int getHandTypeWeight() {
            return 8;
        }
    }

    private static class FullHouse extends Hand {
        public FullHouse(List<Card> cards) {
            super(cards);
            if (!FullHouse.canCreateFrom(cards)) {
                throw new IllegalArgumentException("Cannot create from given cards.");
            }

            CountMap<Rank> rankCounts = getCountMapFrom(cards);
            // Guaranteed only 2 entries.
            Iterator<Map.Entry<Rank, Integer>> rankEntryIter = rankCounts.entrySet().iterator();
            Map.Entry<Rank, Integer> rankEntryA = rankEntryIter.next();
            Map.Entry<Rank, Integer> rankEntryB = rankEntryIter.next();
            if (rankEntryA.getValue() == 3) {
                this.relevantRanksInOrder = List.of(rankEntryA.getKey(), rankEntryB.getKey());
            } else {
                this.relevantRanksInOrder = List.of(rankEntryB.getKey(), rankEntryA.getKey());
            }
        }

        public static boolean canCreateFrom(List<Card> cards) {
            CountMap<Rank> rankCounts = getCountMapFrom(cards);

            return rankCounts.containsValue(3) && rankCounts.containsValue(2);
        }

        private static CountMap<Rank> getCountMapFrom(List<Card> cards) {
            CountMap<Rank> rankCounts = new CountMap<>();
            cards.stream()
                    .map(Card::getRank)
                    .forEach(rankCounts::addCount);
            return rankCounts;
        }

        @Getter
        private final List<Rank> relevantRanksInOrder;

        @Override
        int getHandTypeWeight() {
            return 7;
        }
    }

    private static class Flush extends Hand {
        public Flush(List<Card> cards) {
            super(cards);
            if (!Flush.canCreateFrom(cards)) {
                throw new IllegalArgumentException("Cannot create from given cards.");
            }

            this.relevantRanksInOrder = cards.stream()
                    .sorted(Comparator.reverseOrder())
                    .map(Card::getRank)
                    .collect(Collectors.toList());
        }

        public static boolean canCreateFrom(List<Card> cards) {
            long suitCount = cards.stream()
                    .map(Card::getSuit)
                    .distinct()
                    .count();

            return suitCount == 1L;
        }

        @Getter
        private final List<Rank> relevantRanksInOrder;

        @Override
        int getHandTypeWeight() {
            return 6;
        }
    }

    private static class Straight extends Hand {
        public Straight(List<Card> cards) {
            super(cards);
            if (!Straight.canCreateFrom(cards)) {
                throw new IllegalArgumentException("Cannot create from given cards.");
            }

            List<Card> sorted = cards.stream()
                    .sorted(Comparator.reverseOrder())
                    .collect(Collectors.toList());

            if (sorted.get(0).getRank().equals(Rank.ACE) && sorted.get(1).getRank().equals(Rank.FIVE)) {
                // Ace through 5 straight has 5 as the top card.
                this.topRankInStraight = Rank.FIVE;
            } else {
                this.topRankInStraight = sorted.get(0).getRank();
            }
        }

        public static boolean canCreateFrom(List<Card> cards) {
            List<Card> sorted = cards.stream()
                    .sorted()
                    .collect(Collectors.toList());

            boolean isIncreasingBy1 = isIncreasingBy1(sorted);
            if (isIncreasingBy1) {
                return true;
            }

            // Check for Ace through 5 straight
            return sorted.get(0).getRank().equals(Rank.TWO) &&
                    sorted.get(1).getRank().equals(Rank.THREE) &&
                    sorted.get(2).getRank().equals(Rank.FOUR) &&
                    sorted.get(3).getRank().equals(Rank.FIVE) &&
                    sorted.get(4).getRank().equals(Rank.ACE);
        }

        private static boolean isIncreasingBy1(List<Card> sortedCards) {
            Card lastCard = sortedCards.get(0);
            for (int i = 1; i < sortedCards.size(); i++) {
                if(sortedCards.get(i).getRank().getWeightVal() - lastCard.getRank().getWeightVal() != 1) {
                    return false;
                }
                lastCard = sortedCards.get(i);
            }
            return true;
        }

        private final Rank topRankInStraight;

        @Override
        int getHandTypeWeight() {
            return 5;
        }

        @Override
        List<Rank> getRelevantRanksInOrder() {
            return Collections.singletonList(topRankInStraight);
        }
    }

    private static class ThreeOfAKind extends Hand {
        public ThreeOfAKind(List<Card> cards) {
            super(cards);
            if (!ThreeOfAKind.canCreateFrom(cards)) {
                throw new IllegalArgumentException("Cannot create from given cards.");
            }

            CountMap<Rank> rankCounts = getCountMapFrom(cards);
            Rank threeOfAKindRank = rankCounts.entrySet().stream()
                    .filter(entry -> entry.getValue() == 3)
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .get();

            this.relevantRanksInOrder = new ArrayList<>();
            this.relevantRanksInOrder.add(threeOfAKindRank);
            cards.stream()
                    .filter(card -> !card.getRank().equals(threeOfAKindRank))
                    .sorted(Comparator.reverseOrder())
                    .map(Card::getRank)
                    .forEach(this.relevantRanksInOrder::add);
        }

        public static boolean canCreateFrom(List<Card> cards) {
            CountMap<Rank> rankCounts = getCountMapFrom(cards);

            return rankCounts.containsValue(3);
        }

        private static CountMap<Rank> getCountMapFrom(List<Card> cards) {
            CountMap<Rank> rankCounts = new CountMap<>();
            cards.stream()
                    .map(Card::getRank)
                    .forEach(rankCounts::addCount);
            return rankCounts;
        }

        @Getter
        private final List<Rank> relevantRanksInOrder;

        @Override
        int getHandTypeWeight() {
            return 4;
        }
    }

    private static class TwoPairs extends Hand {
        public TwoPairs(List<Card> cards) {
            super(cards);
            if (!TwoPairs.canCreateFrom(cards)) {
                throw new IllegalArgumentException("Cannot create from given cards.");
            }

            this.relevantRanksInOrder = new ArrayList<>();

            CountMap<Rank> rankCounts = getCountMapFrom(cards);
            rankCounts.entrySet().stream()
                    .filter(entry -> entry.getValue() == 2)
                    .map(Map.Entry::getKey)
                    .sorted(Comparator.reverseOrder())
                    .forEach(this.relevantRanksInOrder::add);

            // Should only be 1 element.
            rankCounts.entrySet().stream()
                    .filter(entry -> entry.getValue() == 1)
                    .map(Map.Entry::getKey)
                    .forEach(this.relevantRanksInOrder::add);
        }

        public static boolean canCreateFrom(List<Card> cards) {
            CountMap<Rank> rankCounts = getCountMapFrom(cards);
            long numWith2Cards = rankCounts.entrySet().stream()
                    .filter(entry -> entry.getValue() == 2)
                    .count();

            return numWith2Cards == 2L;
        }

        private static CountMap<Rank> getCountMapFrom(List<Card> cards) {
            CountMap<Rank> rankCounts = new CountMap<>();
            cards.stream()
                    .map(Card::getRank)
                    .forEach(rankCounts::addCount);
            return rankCounts;
        }

        @Getter
        private final List<Rank> relevantRanksInOrder;

        @Override
        int getHandTypeWeight() {
            return 3;
        }
    }

    private static class OnePair extends Hand {
        public OnePair(List<Card> cards) {
            super(cards);
            if (!OnePair.canCreateFrom(cards)) {
                throw new IllegalArgumentException("Cannot create from given cards.");
            }

            CountMap<Rank> rankCounts = getCountMapFrom(cards);
            Rank pairRank = rankCounts.entrySet().stream()
                    .filter(entry -> entry.getValue() == 2)
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .get();

            this.relevantRanksInOrder = new ArrayList<>();
            this.relevantRanksInOrder.add(pairRank);
            cards.stream()
                    .filter(card -> !card.getRank().equals(pairRank))
                    .sorted(Comparator.reverseOrder())
                    .map(Card::getRank)
                    .forEach(this.relevantRanksInOrder::add);
        }

        public static boolean canCreateFrom(List<Card> cards) {
            CountMap<Rank> rankCounts = getCountMapFrom(cards);

            return rankCounts.containsValue(2);
        }

        private static CountMap<Rank> getCountMapFrom(List<Card> cards) {
            CountMap<Rank> rankCounts = new CountMap<>();
            cards.stream()
                    .map(Card::getRank)
                    .forEach(rankCounts::addCount);
            return rankCounts;
        }

        @Getter
        private final List<Rank> relevantRanksInOrder;

        @Override
        int getHandTypeWeight() {
            return 2;
        }
    }

    private static class HighCard extends Hand {
        public HighCard(List<Card> cards) {
            super(cards);
            this.relevantRanksInOrder = cards.stream()
                    .sorted(Comparator.reverseOrder())
                    .map(Card::getRank)
                    .collect(Collectors.toList());
        }

        @Getter
        private final List<Rank> relevantRanksInOrder;

        @Override
        int getHandTypeWeight() {
            return 1;
        }
    }

    @Value
    private static class Card implements Comparable<Card> {
        private final Suit suit;
        private final Rank rank;


        /**
         * Only returns the result of comparing ranks. Suits by themselves don't count toward a card's value.
         */
        @Override
        public int compareTo(Card other) {
            return this.getRank().getWeightVal() - other.getRank().getWeightVal();
        }
    }

    @Getter
    @RequiredArgsConstructor
    private static enum Suit {
        CLUBS('C'),
        SPADES('S'),
        HEARTS('H'),
        DIAMONDS('D');

        private final char charRepresentation;

        public static Suit fromSuitChar(char val) {
            for (Suit suit : Suit.values()) {
                if (suit.getCharRepresentation() == val) {
                    return suit;
                }
            }
            throw new IllegalArgumentException("No valid suit for " + val);
        }
    }

    @Getter
    @RequiredArgsConstructor
    private static enum Rank {
        TWO('2', 2),
        THREE('3', 3),
        FOUR('4', 4),
        FIVE('5', 5),
        SIX('6', 6),
        SEVEN('7', 7),
        EIGHT('8', 8),
        NINE('9', 9),
        TEN('T', 10),
        JACK('J', 11),
        QUEEN('Q', 12),
        KING('K', 13),
        ACE('A', 14);

        private final char charRepresentation;
        private final int weightVal;

        public static Rank fromRankChar(char val) {
            for (Rank rank : Rank.values()) {
                if (rank.getCharRepresentation() == val) {
                    return rank;
                }
            }
            throw new IllegalArgumentException("No valid rank for " + val);
        }
    }
}
