package problems;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PE0013Test {
    @Test
    void solutionIsCorrect() {
        PE0013 problem = new PE0013();

        assertEquals(5537376230L, problem.solve().getSolution());
    }
}
