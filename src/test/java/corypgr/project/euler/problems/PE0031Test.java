package corypgr.project.euler.problems;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PE0031Test {
    @Test
    void solutionIsCorrect() {
        PE0031 problem = new PE0031();

        assertEquals(73682, problem.solve().getSolution());
    }
}
