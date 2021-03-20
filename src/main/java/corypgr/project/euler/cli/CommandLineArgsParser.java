package corypgr.project.euler.cli;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class CommandLineArgsParser {
    private static final String PROBLEM_NUMBER_NAME = "problemNumber";
    private static final Options OPTIONS = createOptions();

    private static Options createOptions() {
        Options options = new Options();
        options.addOption(Option.builder()
                .argName(PROBLEM_NUMBER_NAME)
                .longOpt(PROBLEM_NUMBER_NAME)
                .hasArg()
                .desc("Problem number to run the solver for")
                .required(false)
                .type(Number.class)
                .build());

        return options;
    }

    public CommandLineArgs parseArgs(String[] args) {
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine commandLine = parser.parse(OPTIONS, args);

            if (commandLine.hasOption(PROBLEM_NUMBER_NAME)) {
                return CommandLineArgs.builder()
                        .problemNumber(((Long) commandLine.getParsedOptionValue(PROBLEM_NUMBER_NAME)).intValue())
                        .build();
            }
            return CommandLineArgs.builder().build(); // null ProblemNumber. Run for all corypgr.project.euler.problems.
        } catch (ParseException e) {
            throw new IllegalArgumentException("Error parsing command line options: " + e.getMessage());
        }
    }
}
