package corypgr.project.euler.problems;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PE0014Test {
    @Test
    void solutionIsCorrect() {
        PE0014 problem = new PE0014();

        assertEquals(837799L, problem.solve().getSolution());
    }
}
