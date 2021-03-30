package corypgr.project.euler.problems;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PE0026Test {
    @Test
    void solutionIsCorrect() {
        PE0026 problem = new PE0026();

        assertEquals(983, problem.solve().getSolution());
    }
}
