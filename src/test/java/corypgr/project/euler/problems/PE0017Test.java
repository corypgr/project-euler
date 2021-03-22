package corypgr.project.euler.problems;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PE0017Test {
    @Test
    void solutionIsCorrect() {
        PE0017 problem = new PE0017();

        assertEquals(21124, problem.solve().getSolution());
    }
}
