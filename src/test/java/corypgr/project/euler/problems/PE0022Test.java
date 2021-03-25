package corypgr.project.euler.problems;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PE0022Test {
    @Test
    void solutionIsCorrect() {
        PE0022 problem = new PE0022();

        assertEquals(871198282L, problem.solve().getSolution());
    }
}
