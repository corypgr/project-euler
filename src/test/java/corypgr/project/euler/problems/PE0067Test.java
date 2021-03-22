package corypgr.project.euler.problems;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PE0067Test {
    @Test
    void solutionIsCorrect() {
        PE0067 problem = new PE0067();

        assertEquals(7273L, problem.solve().getSolution());
    }
}
