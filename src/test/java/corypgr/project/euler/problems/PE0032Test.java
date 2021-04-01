package corypgr.project.euler.problems;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PE0032Test {
    @Test
    void solutionIsCorrect() {
        PE0032 problem = new PE0032();

        assertEquals(45228, problem.solve().getSolution());
    }
}
