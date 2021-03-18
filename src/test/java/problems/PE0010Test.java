package problems;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PE0010Test {
    @Test
    void solutionIsCorrect() {
        PE0010 problem = new PE0010();

        assertEquals(142913828922L, problem.solve().getSolution());
    }
}
