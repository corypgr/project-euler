package corypgr.project.euler.problems;

import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Problem 61
 *
 * https://projecteuler.net/problem=61
 *
 * Looks like a chain type of problem. I'm thinking I'll generate all of the 6 different figurate number sets, create a
 * mapping from the first 2 digits to all figurate numbers with those 2 digits, then try to generate the 6 link chain.
 */
public class PE0061 implements Problem {
    @Override
    public ProblemSolution solve() {
        List<FourDigitFigurate> allFourDigitFigurates = Arrays.stream(Type.values())
                .map(this::getFigurateListOf)
                .flatMap(List::stream)
                .collect(Collectors.toList());

        Map<Integer, List<FourDigitFigurate>> firstTwoDigitsToFigurates = IntStream.range(0, 100)
                .boxed()
                .collect(Collectors.toMap(Function.identity(), v -> new LinkedList<>()));
        allFourDigitFigurates.forEach(v -> addElementToMap(firstTwoDigitsToFigurates, v));

        LinkedList<FourDigitFigurate> sixFigureChain = allFourDigitFigurates.stream()
                .map(figurate -> findSixFigurateChain(firstTwoDigitsToFigurates, new LinkedList<>(List.of(figurate))))
                .filter(Objects::nonNull)
                .findFirst()
                .get();

        int sum = sixFigureChain.stream()
                .mapToInt(FourDigitFigurate::getVal)
                .sum();
        return ProblemSolution.builder()
                .solution(sum)
                .descriptiveSolution("Sum of the only 6 Figurate chain: " + sum)
                .build();
    }

    private LinkedList<FourDigitFigurate> findSixFigurateChain(Map<Integer, List<FourDigitFigurate>> firstTwoDigitsToFigurates,
            LinkedList<FourDigitFigurate> chainSoFar) {
        if (chainSoFar.size() == 6) {
            // Last element has to chain to the first element for the chain to be complete.
            return chainSoFar.getLast().getLast2Digits() == chainSoFar.getFirst().getFirst2Digits() ? chainSoFar : null;
        }

        Set<Type> typesAlreadyInChain = chainSoFar.stream()
                .map(FourDigitFigurate::getType)
                .collect(Collectors.toSet());
        Set<FourDigitFigurate> valsAlreadyInChain = new HashSet<>(chainSoFar); // Want to avoid loops

        for (FourDigitFigurate figurate : firstTwoDigitsToFigurates.get(chainSoFar.getLast().getLast2Digits())) {
            if (!valsAlreadyInChain.contains(figurate) && !typesAlreadyInChain.contains(figurate.getType())) {
                LinkedList<FourDigitFigurate> newChain = new LinkedList<>(chainSoFar);
                newChain.add(figurate);

                LinkedList<FourDigitFigurate> recursiveResult = findSixFigurateChain(firstTwoDigitsToFigurates, newChain);
                if (recursiveResult != null) {
                    return recursiveResult;
                }
            }
        }
        return null;
    }

    private List<FourDigitFigurate> getFigurateListOf(Type type) {
        return Stream.iterate(1, i -> i + 1)
                .mapToInt(type.getGenerator()::apply)
                .dropWhile(v -> v < 1000)
                .takeWhile(v -> v < 10000)
                .mapToObj(v -> new FourDigitFigurate(v, type))
                .collect(Collectors.toList());
    }

    private void addElementToMap(Map<Integer, List<FourDigitFigurate>> firstTwoDigitsToFigurates, FourDigitFigurate figurate) {
        List<FourDigitFigurate> valueList = firstTwoDigitsToFigurates.get(figurate.getFirst2Digits());
        valueList.add(figurate);
    }

    @Value
    private static class FourDigitFigurate {
        private final int val;
        private final Type type;
        private final int first2Digits;
        private final int last2Digits;

        public FourDigitFigurate(int val, Type type) {
            if (val < 1000 || val >= 10000) {
                throw new IllegalArgumentException("Only 4 digit values allowed here.");
            }

            this.val = val;
            this.type = type;
            this.first2Digits = val / 100;
            this.last2Digits = val % 100;
        }
    }

    @Getter
    @RequiredArgsConstructor
    private static enum Type {
        TRIANGLE(n -> (n * (n + 1)) / 2),
        SQUARE(n -> n * n),
        PENTAGONAL(n -> (n * (3 * n - 1)) / 2),
        HEXAGONAL(n -> n * (2 * n - 1)),
        HEPTAGONAL(n -> (n * (5 * n - 3)) / 2),
        OCTAGONAL(n -> n * (3 * n - 2));

        private final UnaryOperator<Integer> generator;
    }
}
