package corypgr.project.euler.problems;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PE0025Test {
    @Test
    void solutionIsCorrect() {
        PE0025 problem = new PE0025();

        assertEquals(4782, problem.solve().getSolution());
    }
}
