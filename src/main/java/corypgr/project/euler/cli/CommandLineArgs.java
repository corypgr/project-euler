package corypgr.project.euler.cli;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CommandLineArgs {
    private final Integer problemNumber;
}
