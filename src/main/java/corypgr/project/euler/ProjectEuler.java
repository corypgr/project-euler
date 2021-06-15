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
import corypgr.project.euler.problems.PE0054;
import corypgr.project.euler.problems.PE0055;
import corypgr.project.euler.problems.PE0056;
import corypgr.project.euler.problems.PE0057;
import corypgr.project.euler.problems.PE0058;
import corypgr.project.euler.problems.PE0059;
import corypgr.project.euler.problems.PE0060;
import corypgr.project.euler.problems.PE0061;
import corypgr.project.euler.problems.PE0062;
import corypgr.project.euler.problems.PE0063;
import corypgr.project.euler.problems.PE0064;
import corypgr.project.euler.problems.PE0065;
import corypgr.project.euler.problems.PE0066;
import corypgr.project.euler.problems.PE0067;
import corypgr.project.euler.problems.PE0068;
import corypgr.project.euler.problems.PE0069;
import corypgr.project.euler.problems.PE0070;
import corypgr.project.euler.problems.PE0071;
import corypgr.project.euler.problems.PE0072;
import corypgr.project.euler.problems.PE0073;
import corypgr.project.euler.problems.PE0074;
import corypgr.project.euler.problems.PE0075;
import corypgr.project.euler.problems.PE0076;
import corypgr.project.euler.problems.PE0077;
import corypgr.project.euler.problems.PE0078;
import corypgr.project.euler.problems.PE0079;
import corypgr.project.euler.problems.PE0080;
import corypgr.project.euler.problems.PE0081;
import corypgr.project.euler.problems.PE0082;
import corypgr.project.euler.problems.PE0083;
import corypgr.project.euler.problems.PE0084;
import corypgr.project.euler.problems.PE0085;
import corypgr.project.euler.problems.PE0086;
import corypgr.project.euler.problems.PE0087;
import corypgr.project.euler.problems.PE0088;
import corypgr.project.euler.problems.PE0089;
import corypgr.project.euler.problems.PE0091;
import corypgr.project.euler.problems.PE0092;
import corypgr.project.euler.problems.PE0093;
import corypgr.project.euler.problems.PE0094;
import corypgr.project.euler.problems.PE0095;
import corypgr.project.euler.problems.PE0096;
import corypgr.project.euler.problems.PE0097;
import corypgr.project.euler.problems.PE0098;
import corypgr.project.euler.problems.PE0099;
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
            Map.entry(54, new PE0054()),
            Map.entry(55, new PE0055()),
            Map.entry(56, new PE0056()),
            Map.entry(57, new PE0057()),
            Map.entry(58, new PE0058()),
            Map.entry(59, new PE0059()),
            Map.entry(60, new PE0060()),
            Map.entry(61, new PE0061()),
            Map.entry(62, new PE0062()),
            Map.entry(63, new PE0063()),
            Map.entry(64, new PE0064()),
            Map.entry(65, new PE0065()),
            Map.entry(66, new PE0066()),
            Map.entry(67, new PE0067()),
            Map.entry(68, new PE0068()),
            Map.entry(69, new PE0069()),
            Map.entry(70, new PE0070()),
            Map.entry(71, new PE0071()),
            Map.entry(72, new PE0072()),
            Map.entry(73, new PE0073()),
            Map.entry(74, new PE0074()),
            Map.entry(75, new PE0075()),
            Map.entry(76, new PE0076()),
            Map.entry(77, new PE0077()),
            Map.entry(78, new PE0078()),
            Map.entry(79, new PE0079()),
            Map.entry(80, new PE0080()),
            Map.entry(81, new PE0081()),
            Map.entry(82, new PE0082()),
            Map.entry(83, new PE0083()),
            Map.entry(84, new PE0084()),
            Map.entry(85, new PE0085()),
            Map.entry(86, new PE0086()),
            Map.entry(87, new PE0087()),
            Map.entry(88, new PE0088()),
            Map.entry(89, new PE0089()),
            Map.entry(91, new PE0091()),
            Map.entry(92, new PE0092()),
            Map.entry(93, new PE0093()),
            Map.entry(94, new PE0094()),
            Map.entry(95, new PE0095()),
            Map.entry(96, new PE0096()),
            Map.entry(97, new PE0097()),
            Map.entry(98, new PE0098()),
            Map.entry(99, new PE0099())
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
