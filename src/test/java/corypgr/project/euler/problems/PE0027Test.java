package corypgr.project.euler.problems;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PE0027Test {
    @Test
    void solutionIsCorrect() {
        PE0027 problem = new PE0027();

        assertEquals(-59231L, problem.solve().getSolution());
    }
}
