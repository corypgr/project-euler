package corypgr.project.euler.problems;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PE0028Test {
    @Test
    void solutionIsCorrect() {
        PE0028 problem = new PE0028();

        assertEquals(669171001L, problem.solve().getSolution());
    }
}
