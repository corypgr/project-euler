package corypgr.project.euler;

import corypgr.project.euler.cli.CommandLineArgs;
import corypgr.project.euler.cli.CommandLineArgsParser;
import corypgr.project.euler.problems.PE0001;
import corypgr.project.euler.problems.PE0002;
import corypgr.project.euler.problems.PE0003;
import corypgr.project.euler.problems.PE0004;
import corypgr.project.euler.problems.PE0005;
import corypgr.project.euler.problems.PE0006;
import corypgr.project.euler.problems.PE0007;
import corypgr.project.euler.problems.PE0008;
import corypgr.project.euler.problems.PE0009;
import corypgr.project.euler.problems.PE0010;
import corypgr.project.euler.problems.PE0011;
import corypgr.project.euler.problems.PE0012;
import corypgr.project.euler.problems.PE0013;
import corypgr.project.euler.problems.PE0014;
import corypgr.project.euler.problems.PE0015;
import corypgr.project.euler.problems.PE0016;
import corypgr.project.euler.problems.PE0017;
import corypgr.project.euler.problems.PE0018;
import corypgr.project.euler.problems.PE0019;
import corypgr.project.euler.problems.PE0020;
import corypgr.project.euler.problems.PE0021;
import corypgr.project.euler.problems.PE0022;
import corypgr.project.euler.problems.PE0023;
import corypgr.project.euler.problems.PE0024;
import corypgr.project.euler.problems.PE0025;
import corypgr.project.euler.problems.PE0026;
import corypgr.project.euler.problems.PE0027;
import corypgr.project.euler.problems.PE0028;
import corypgr.project.euler.problems.PE0029;
import corypgr.project.euler.problems.PE0030;
import corypgr.project.euler.problems.PE0031;
import corypgr.project.euler.problems.PE0032;
import corypgr.project.euler.problems.PE0033;
import corypgr.project.euler.problems.PE0034;
import corypgr.project.euler.problems.PE0035;
import corypgr.project.euler.problems.PE0036;
import corypgr.project.euler.problems.PE0037;
import corypgr.project.euler.problems.PE0038;
import corypgr.project.euler.problems.PE0039;
import corypgr.project.euler.problems.PE0040;
import corypgr.project.euler.problems.PE0041;
import corypgr.project.euler.problems.PE0042;
import corypgr.project.euler.problems.PE0043;
import corypgr.project.euler.problems.PE0044;
import corypgr.project.euler.problems.PE0045;
import corypgr.project.euler.problems.PE0046;
import corypgr.project.euler.problems.PE0047;
import corypgr.project.euler.problems.PE0048;
import corypgr.project.euler.problems.PE0049;
import corypgr.project.euler.problems.PE0050;
import corypgr.project.euler.problems.PE0051;
import corypgr.project.euler.problems.PE0052;
import corypgr.project.euler.problems.PE0053;
import corypgr.project.euler.problems.PE0067;
import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

public class ProjectEuler {
    private static final Map<Integer, Problem> numberToProblem = Map.ofEntries(
            Map.entry(1, new PE0001()),
            Map.entry(2, new PE0002()),
            Map.entry(3, new PE0003()),
            Map.entry(4, new PE0004()),
            Map.entry(5, new PE0005()),
            Map.entry(6, new PE0006()),
            Map.entry(7, new PE0007()),
            Map.entry(8, new PE0008()),
            Map.entry(9, new PE0009()),
            Map.entry(10, new PE0010()),
            Map.entry(11, new PE0011()),
            Map.entry(12, new PE0012()),
            Map.entry(13, new PE0013()),
            Map.entry(14, new PE0014()),
            Map.entry(15, new PE0015()),
            Map.entry(16, new PE0016()),
            Map.entry(17, new PE0017()),
            Map.entry(18, new PE0018()),
            Map.entry(19, new PE0019()),
            Map.entry(20, new PE0020()),
            Map.entry(21, new PE0021()),
            Map.entry(22, new PE0022()),
            Map.entry(23, new PE0023()),
            Map.entry(24, new PE0024()),
            Map.entry(25, new PE0025()),
            Map.entry(26, new PE0026()),
            Map.entry(27, new PE0027()),
            Map.entry(28, new PE0028()),
            Map.entry(29, new PE0029()),
            Map.entry(30, new PE0030()),
            Map.entry(31, new PE0031()),
            Map.entry(32, new PE0032()),
            Map.entry(33, new PE0033()),
            Map.entry(34, new PE0034()),
            Map.entry(35, new PE0035()),
            Map.entry(36, new PE0036()),
            Map.entry(37, new PE0037()),
            Map.entry(38, new PE0038()),
            Map.entry(39, new PE0039()),
            Map.entry(40, new PE0040()),
            Map.entry(41, new PE0041()),
            Map.entry(42, new PE0042()),
            Map.entry(43, new PE0043()),
            Map.entry(44, new PE0044()),
            Map.entry(45, new PE0045()),
            Map.entry(46, new PE0046()),
            Map.entry(47, new PE0047()),
            Map.entry(48, new PE0048()),
            Map.entry(49, new PE0049()),
            Map.entry(50, new PE0050()),
            Map.entry(51, new PE0051()),
            Map.entry(52, new PE0052()),
            Map.entry(53, new PE0053()),
            Map.entry(67, new PE0067())
    );

    public static void main(String[] args) {
        CommandLineArgsParser argsParser = new CommandLineArgsParser();
        CommandLineArgs parsedArgs = argsParser.parseArgs(args);

        if (parsedArgs.getProblemNumber() == null) {
            System.out.println("Running all Problems.");
            numberToProblem.keySet().stream()
                    .mapToInt(Integer::intValue)
                    .sorted()
                    .forEach(ProjectEuler::runProblem);
        } else if (numberToProblem.containsKey(parsedArgs.getProblemNumber())) {
            runProblem(parsedArgs.getProblemNumber());
        } else {
            System.out.println("Problem Number provided is not known or is not solved yet.");
        }
    }

    private static void runProblem(int problemNumber) {
        System.out.println();
        System.out.println("Problem " + problemNumber + ":");

        Problem problem = numberToProblem.get(problemNumber);
        Instant startTime = Instant.now();
        ProblemSolution solution = problem.solve();
        Instant endTime = Instant.now();

        System.out.println("Solution: " + solution.getSolution());
        System.out.println("Descriptive Solution: " + solution.getDescriptiveSolution());
        System.out.println("Execution time: " + getExecutionTime(startTime, endTime));
    }

    /**
     * Calculate execution time printed wtih seconds and either milliseconds or nanoseconds. Seconds are only displayed
     * if the execution lasted longer than 1 second. When seconds are displayed, they are always accompanied by the
     * milliseconds part.
     *
     * If the execution is less than 1 second and longer than 1 millisecond, then milliseconds are displayed. Otherwise,
     * nanoseconds are displayed.
     */
    private static String getExecutionTime(Instant start, Instant end) {
        Duration duration = Duration.between(start, end);

        // All seconds, not just the ones less than 1 minute.
        long seconds = duration.getSeconds();
        if (seconds > 0) {
            return seconds + "s " + duration.toMillisPart() + "ms";
        }

        long millis = duration.toMillisPart();
        if (millis > 0) {
            return millis + "ms";
        }
        return duration.toNanosPart() + "ns";
    }
}
