package problems;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PE0002Test {
    @Test
    void solutionIsCorrect() {
        PE0002 problem = new PE0002();

        assertEquals(4613732, problem.solve().getSolution());
    }
}
