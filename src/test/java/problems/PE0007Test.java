package problems;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PE0007Test {
    @Test
    void solutionIsCorrect() {
        PE0007 problem = new PE0007();

        assertEquals(104743L, problem.solve().getSolution());
    }
}
