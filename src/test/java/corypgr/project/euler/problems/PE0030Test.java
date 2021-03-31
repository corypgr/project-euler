package corypgr.project.euler.problems;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PE0030Test {
    @Test
    void solutionIsCorrect() {
        PE0030 problem = new PE0030();

        assertEquals(443839L, problem.solve().getSolution());
    }
}
