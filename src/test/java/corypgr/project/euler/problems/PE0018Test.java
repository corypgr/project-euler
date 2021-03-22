package corypgr.project.euler.problems;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PE0018Test {
    @Test
    void solutionIsCorrect() {
        PE0018 problem = new PE0018();

        assertEquals(1074L, problem.solve().getSolution());
    }
}
