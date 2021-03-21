package corypgr.project.euler.problems;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PE0016Test {
    @Test
    void solutionIsCorrect() {
        PE0016 problem = new PE0016();

        assertEquals(1366, problem.solve().getSolution());
    }
}
