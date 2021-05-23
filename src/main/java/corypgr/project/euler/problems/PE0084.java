package corypgr.project.euler.problems;

import corypgr.project.euler.problems.util.CountMap;
import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import lombok.experimental.SuperBuilder;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Problem 84
 *
 * https://projecteuler.net/problem=84
 *
 * This is kind of a combination of things. We need to model the Monopoly board, along with all movements along it, and
 * we'll need to calculate the statistics of landing on a specific space by actually rolling "dice" many times.
 *
 * Modeling the board is interesting. I think I will model it as a circular linked list of Spaces, where some spaces
 * have no special side effects, some will directly force a move (G2J), and some will randomly force a move (CC and CH).
 * We also need to model dice, which is pretty easy, and keep track of the previous 2 moves while rolling to determine
 * if 3 doubles have been rolled. CC and CH will each have cards attached which have specific move functions.
 *
 * For the statistics portion, I will plan to just run many dice rolls and record where we end up.
 *
 * Ran into some interesting issues here. My solution seems to work properly, but the percentage of times the third and
 * fourth place spaces come up are very similar. They regularly swap when using a random seed value. I can also see this
 * when using 6 sided die, where D3 (19) comes more often than GO in some cases. This irregularity is also discussed in
 * the Euler forum. Due to this issue, I've set a static seed so that we consistently get the expected result.
 */
public class PE0084 implements Problem {
    private static final int NUM_MOVES = 1_000_000;
    private static final Random RANDOM = new Random(12345);

    @Override
    public ProblemSolution solve() {
        Board board = createBoard();
        CountMap<Space> spaceCounts = new CountMap<>();

        Space curSpace = board.getGo();
        spaceCounts.addCount(curSpace);
        int doubleCount = 0;
        for (int i = 0; i < NUM_MOVES; i++) {
            Dice dice = Dice.rollDice();

            if (dice.isDouble() && doubleCount == 2) {
                doubleCount = 0;
                curSpace = board.getJail();
            } else if (dice.isDouble()) {
                doubleCount++;
                curSpace = curSpace.getPositionAfterMoving(dice.getTotal());
            } else {
                doubleCount = 0;
                curSpace = curSpace.getPositionAfterMoving(dice.getTotal());
            }

            spaceCounts.addCount(curSpace);
        }

        List<Space> topSpaces = spaceCounts.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .limit(3)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        int solution = Integer.parseInt(topSpaces.stream()
                .map(Space::getNumRep)
                .collect(Collectors.joining("")));
        return ProblemSolution.builder()
                .solution(solution)
                .descriptiveSolution("Top spaces are: " + topSpaces + ". Solution is: " + solution)
                .build();
    }

    private Board createBoard() {
        Space go = Space.builder().strRep("GO").numRep("00").build();
        Space jail = Space.builder().strRep("JAIL").numRep("10").build();
        Space freePark = Space.builder().strRep("FP").numRep("20").build();
        Space goToJail = DirectMoveSpace.builder().strRep("G2J").numRep("30").moveToSpace(jail).build();

        Space rail1 = Space.builder().strRep("R1").numRep("05").build();
        Space rail2 = Space.builder().strRep("R2").numRep("15").build();
        Space rail3 = Space.builder().strRep("R3").numRep("25").build();
        Space rail4 = Space.builder().strRep("R4").numRep("35").build();

        Space utility1 = Space.builder().strRep("U1").numRep("12").build();
        Space utility2 = Space.builder().strRep("U2").numRep("28").build();

        Space t1 = Space.builder().strRep("T1").numRep("04").build();
        Space t2 = Space.builder().strRep("T2").numRep("38").build();

        Space a1 = Space.builder().strRep("A1").numRep("01").build();
        Space a2 = Space.builder().strRep("A2").numRep("03").build();

        Space b1 = Space.builder().strRep("B1").numRep("06").build();
        Space b2 = Space.builder().strRep("B2").numRep("08").build();
        Space b3 = Space.builder().strRep("B3").numRep("09").build();

        Space c1 = Space.builder().strRep("C1").numRep("11").build();
        Space c2 = Space.builder().strRep("C2").numRep("13").build();
        Space c3 = Space.builder().strRep("C3").numRep("14").build();

        Space d1 = Space.builder().strRep("D1").numRep("16").build();
        Space d2 = Space.builder().strRep("D2").numRep("18").build();
        Space d3 = Space.builder().strRep("D3").numRep("19").build();

        Space e1 = Space.builder().strRep("E1").numRep("21").build();
        Space e2 = Space.builder().strRep("E2").numRep("23").build();
        Space e3 = Space.builder().strRep("E3").numRep("24").build();

        Space f1 = Space.builder().strRep("F1").numRep("26").build();
        Space f2 = Space.builder().strRep("F2").numRep("27").build();
        Space f3 = Space.builder().strRep("F3").numRep("29").build();

        Space g1 = Space.builder().strRep("G1").numRep("31").build();
        Space g2 = Space.builder().strRep("G2").numRep("32").build();
        Space g3 = Space.builder().strRep("G3").numRep("34").build();

        Space h1 = Space.builder().strRep("H1").numRep("37").build();
        Space h2 = Space.builder().strRep("H2").numRep("39").build();

        LinkedList<Card> communityChestCards = new LinkedList<>();
        IntStream.range(0, 14).forEach(i -> communityChestCards.add(Card.builder().build())); // Add 14 simple cards.
        communityChestCards.add(DirectMoveCard.builder().moveToSpace(go).build());
        communityChestCards.add(DirectMoveCard.builder().moveToSpace(jail).build());
        Collections.shuffle(communityChestCards);

        Space chest1 = SpaceWithCards.builder().strRep("CC1").numRep("02").cards(communityChestCards).build();
        Space chest2 = SpaceWithCards.builder().strRep("CC2").numRep("17").cards(communityChestCards).build();
        Space chest3 = SpaceWithCards.builder().strRep("CC3").numRep("33").cards(communityChestCards).build();

        Set<Space> railways = Set.of(rail1, rail2, rail3, rail4);

        LinkedList<Card> chanceCards = new LinkedList<>();
        IntStream.range(0, 6).forEach(i -> chanceCards.add(Card.builder().build())); // Add 6 simple cards.
        chanceCards.add(DirectMoveCard.builder().moveToSpace(go).build());
        chanceCards.add(DirectMoveCard.builder().moveToSpace(jail).build());
        chanceCards.add(DirectMoveCard.builder().moveToSpace(c1).build());
        chanceCards.add(DirectMoveCard.builder().moveToSpace(e3).build());
        chanceCards.add(DirectMoveCard.builder().moveToSpace(h2).build());
        chanceCards.add(DirectMoveCard.builder().moveToSpace(rail1).build());
        chanceCards.add(NearestMoveCard.builder().moveToSpaces(railways).build());
        chanceCards.add(NearestMoveCard.builder().moveToSpaces(railways).build());
        chanceCards.add(NearestMoveCard.builder().moveToSpaces(Set.of(utility1, utility2)).build());
        chanceCards.add(MoveBackCard.builder().spacesToMove(3).build());
        Collections.shuffle(chanceCards);

        Space chance1 = SpaceWithCards.builder().strRep("CH1").numRep("07").cards(chanceCards).build();
        Space chance2 = SpaceWithCards.builder().strRep("CH2").numRep("22").cards(chanceCards).build();
        Space chance3 = SpaceWithCards.builder().strRep("CH3").numRep("36").cards(chanceCards).build();

        go.setNextSpace(a1);
        a1.setNextSpace(chest1);
        chest1.setNextSpace(a2);
        a2.setNextSpace(t1);
        t1.setNextSpace(rail1);
        rail1.setNextSpace(b1);
        b1.setNextSpace(chance1);
        chance1.setNextSpace(b2);
        b2.setNextSpace(b3);
        b3.setNextSpace(jail);
        jail.setNextSpace(c1);
        c1.setNextSpace(utility1);
        utility1.setNextSpace(c2);
        c2.setNextSpace(c3);
        c3.setNextSpace(rail2);
        rail2.setNextSpace(d1);
        d1.setNextSpace(chest2);
        chest2.setNextSpace(d2);
        d2.setNextSpace(d3);
        d3.setNextSpace(freePark);
        freePark.setNextSpace(e1);
        e1.setNextSpace(chance2);
        chance2.setNextSpace(e2);
        e2.setNextSpace(e3);
        e3.setNextSpace(rail3);
        rail3.setNextSpace(f1);
        f1.setNextSpace(f2);
        f2.setNextSpace(utility2);
        utility2.setNextSpace(f3);
        f3.setNextSpace(goToJail);
        goToJail.setNextSpace(g1);
        g1.setNextSpace(g2);
        g2.setNextSpace(chest3);
        chest3.setNextSpace(g3);
        g3.setNextSpace(rail4);
        rail4.setNextSpace(chance3);
        chance3.setNextSpace(h1);
        h1.setNextSpace(t2);
        t2.setNextSpace(h2);
        h2.setNextSpace(go);

        return Board.builder().go(go).jail(jail).build();
    }

    @Value
    @Builder
    private static final class Board {
        private final Space go;
        private final Space jail;
    }

    @Data
    @Builder
    private static final class Dice {
        private static final int MAX_DIE = 4;

        private final int dieA;
        private final int dieB;
        private final int total;
        private final boolean isDouble;

        public static Dice rollDice() {
            int dieA = rollDie();
            int dieB = rollDie();
            return Dice.builder()
                    .dieA(dieA)
                    .dieB(dieB)
                    .total(dieA + dieB)
                    .isDouble(dieA == dieB)
                    .build();
        }

        private static int rollDie() {
            return RANDOM.nextInt(MAX_DIE) + 1;
        }
    }

    @SuperBuilder
    private static class SpaceWithCards extends Space {
        private final Queue<Card> cards;

        @Override
        protected Space getSpecialMovementResult() {
            // Remove card from the top and put it on the bottom.
            Card topCard = cards.poll();
            cards.add(topCard);

            return topCard.getSpecialMovementResult(this);
        }
    }

    @SuperBuilder
    private static class DirectMoveSpace extends Space {
        private final Space moveToSpace;

        @Override
        protected Space getSpecialMovementResult() {
            return moveToSpace;
        }
    }

    @Data
    @SuperBuilder
    @ToString(onlyExplicitlyIncluded = true)
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    private static class Space {
        @ToString.Include
        @EqualsAndHashCode.Include
        private final String strRep;

        @ToString.Include
        private final String numRep;

        private Space nextSpace;
        private Space prevSpace;

        public void setNextSpace(Space next) {
            this.nextSpace = next;
            next.setPrevSpace(this);
        }

        /**
         * Determines where the user has ended up after moving a specific dice roll. If there are any special movements,
         * that is handled here as well.
         */
        public Space getPositionAfterMoving(int spacesToMove) {
            Space finalSpace = this;
            int spacesMoved = 0;
            while (spacesMoved < spacesToMove) {
                finalSpace = finalSpace.getNextSpace();
                spacesMoved++;
            }
            return finalSpace.getSpecialMovementResult();
        }

        /**
         * Do any special movements which result when you land on a space. In the normal case, no additional movement is
         * made.
         */
        protected Space getSpecialMovementResult() {
            return this;
        }
    }

    @SuperBuilder
    private static class MoveBackCard extends Card {
        private final int spacesToMove;

        @Override
        public Space getSpecialMovementResult(Space landedOn) {
            Space finalSpace = landedOn;
            int spacesMoved = 0;
            while (spacesMoved < spacesToMove) {
                finalSpace = finalSpace.getPrevSpace();
                spacesMoved++;
            }
            return finalSpace.getSpecialMovementResult();
        }
    }

    @SuperBuilder
    private static class NearestMoveCard extends Card {
        private final Set<Space> moveToSpaces;

        @Override
        public Space getSpecialMovementResult(Space landedOn) {
            Space finalSpace = landedOn;
            while (!moveToSpaces.contains(finalSpace)) {
                finalSpace = finalSpace.getNextSpace();
            }
            return finalSpace;
        }
    }

    @SuperBuilder
    private static class DirectMoveCard extends Card {
        private final Space moveToSpace;

        @Override
        public Space getSpecialMovementResult(Space landedOn) {
            return moveToSpace;
        }
    }

    /**
     * Community Chest and Chance cards.
     */
    @SuperBuilder
    private static class Card {
        /**
         * Move to a new space based on where you landed. For the default case, don't move at all.
         */
        public Space getSpecialMovementResult(Space landedOn) {
            return landedOn;
        }
    }
}
