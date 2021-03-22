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
            Map.entry(17, new PE0017())
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
