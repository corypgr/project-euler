package problems;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PE0003Test {
    @Test
    void solutionIsCorrect() {
        PE0003 problem = new PE0003();

        assertEquals(6857L, problem.solve().getSolution());
    }
}
