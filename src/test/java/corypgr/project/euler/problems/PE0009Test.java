package corypgr.project.euler.problems;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PE0009Test {
    @Test
    void solutionIsCorrect() {
        PE0009 problem = new PE0009();

        assertEquals(31875000L, problem.solve().getSolution());
    }
}
