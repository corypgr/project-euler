package corypgr.project.euler.problems;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PE0015Test {
    @Test
    void solutionIsCorrect() {
        PE0015 problem = new PE0015();

        assertEquals(137846528820L, problem.solve().getSolution());
    }
}
