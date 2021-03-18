package problems;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PE0001Test {
    @Test
    void solutionIsCorrect() {
        PE0001 problem = new PE0001();

        assertEquals(233168, problem.solve().getSolution());
    }
}
