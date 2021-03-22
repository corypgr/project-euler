package corypgr.project.euler.problems;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PE0020Test {
    @Test
    void solutionIsCorrect() {
        PE0020 problem = new PE0020();

        assertEquals(648, problem.solve().getSolution());
    }
}
