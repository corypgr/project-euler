package corypgr.project.euler.problems.util;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public
class ProblemSolution {
    private final String descriptiveSolution;
    private final Number solution;
}
