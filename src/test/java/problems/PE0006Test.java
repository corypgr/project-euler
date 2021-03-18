package problems;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PE0006Test {
    @Test
    void solutionIsCorrect() {
        PE0006 problem = new PE0006();

        assertEquals(25164150L, problem.solve().getSolution());
    }
}
