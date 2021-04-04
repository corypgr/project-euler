package corypgr.project.euler.problems;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import corypgr.project.euler.problems.util.Problem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class ProblemSolutionTest {

    @ParameterizedTest
    @MethodSource("problemAndExpectedSolution")
    void solutionIsCorrect(Problem problem, Number expectedSolution) {
        assertEquals(expectedSolution, problem.solve().getSolution());
    }

    static Stream<Arguments> problemAndExpectedSolution() {
        return Stream.of(
                arguments(new PE0001(), 233168),
                arguments(new PE0002(), 4613732),
                arguments(new PE0003(), 6857L),
                arguments(new PE0004(), 906609L),
                arguments(new PE0005(), 232792560L),
                arguments(new PE0006(), 25164150L),
                arguments(new PE0007(), 104743L),
                arguments(new PE0008(), 23514624000L),
                arguments(new PE0009(), 31875000L),
                arguments(new PE0010(), 142913828922L),
                arguments(new PE0011(), 70600674L),
                arguments(new PE0012(), 76576500L),
                arguments(new PE0013(), 5537376230L),
                arguments(new PE0014(), 837799L),
                arguments(new PE0015(), 137846528820L),
                arguments(new PE0016(), 1366),
                arguments(new PE0017(), 21124),
                arguments(new PE0018(), 1074L),
                arguments(new PE0019(), 171),
                arguments(new PE0020(), 648),
                arguments(new PE0021(), 31626L),
                arguments(new PE0022(), 871198282L),
                arguments(new PE0023(), 4179871L),
                arguments(new PE0024(), 2783915460L),
                arguments(new PE0025(), 4782),
                arguments(new PE0026(), 983),
                arguments(new PE0027(), -59231L),
                arguments(new PE0028(), 669171001L),
                arguments(new PE0029(), 9183),
                arguments(new PE0030(), 443839L),
                arguments(new PE0031(), 73682),
                arguments(new PE0032(), 45228),
                arguments(new PE0067(), 7273L));
    }
}