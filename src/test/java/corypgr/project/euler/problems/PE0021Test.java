package corypgr.project.euler.problems;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PE0021Test {
    @Test
    void solutionIsCorrect() {
        PE0021 problem = new PE0021();

        assertEquals(31626L, problem.solve().getSolution());
    }
}
