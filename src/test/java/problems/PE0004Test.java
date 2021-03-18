package problems;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PE0004Test {
    @Test
    void solutionIsCorrect() {
        PE0004 problem = new PE0004();

        assertEquals(906609L, problem.solve().getSolution());
    }
}
