package corypgr.project.euler.problems;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PE0023Test {
    @Test
    void solutionIsCorrect() {
        PE0023 problem = new PE0023();

        assertEquals(4179871L, problem.solve().getSolution());
    }
}
