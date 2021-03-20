package corypgr.project.euler.problems;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PE0012Test {
    @Test
    void solutionIsCorrect() {
        PE0012 problem = new PE0012();

        assertEquals(76576500L, problem.solve().getSolution());
    }
}
