package problems;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PE0011Test {
    @Test
    void solutionIsCorrect() {
        PE0011 problem = new PE0011();

        assertEquals(70600674L, problem.solve().getSolution());
    }
}
