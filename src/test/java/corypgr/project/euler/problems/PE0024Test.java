package corypgr.project.euler.problems;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PE0024Test {
    @Test
    void solutionIsCorrect() {
        PE0024 problem = new PE0024();

        assertEquals(2783915460L, problem.solve().getSolution());
    }
}
