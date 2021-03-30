package corypgr.project.euler.problems;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PE0029Test {
    @Test
    void solutionIsCorrect() {
        PE0029 problem = new PE0029();

        assertEquals(9183, problem.solve().getSolution());
    }
}
