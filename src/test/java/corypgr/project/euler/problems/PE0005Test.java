package corypgr.project.euler.problems;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PE0005Test {
    @Test
    void solutionIsCorrect() {
        PE0005 problem = new PE0005();

        assertEquals(232792560L, problem.solve().getSolution());
    }
}
