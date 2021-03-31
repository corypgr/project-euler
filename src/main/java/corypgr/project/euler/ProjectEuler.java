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
import corypgr.project.euler.problems.PE0067;
import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;

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

        ProblemSolution solution = numberToProblem.get(problemNumber).solve();
        System.out.println("Solution: " + solution.getSolution());
        System.out.println("Descriptive Solution: " + solution.getDescriptiveSolution());
    }
}
