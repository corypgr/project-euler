package corypgr.project.euler.problems;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PE0019Test {
    @Test
    void solutionIsCorrect() {
        PE0019 problem = new PE0019();

        assertEquals(171, problem.solve().getSolution());
    }
}
