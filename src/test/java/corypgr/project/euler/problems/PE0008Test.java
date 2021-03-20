package corypgr.project.euler.problems;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PE0008Test {
    @Test
    void solutionIsCorrect() {
        PE0008 problem = new PE0008();

        assertEquals(23514624000L, problem.solve().getSolution());
    }
}
