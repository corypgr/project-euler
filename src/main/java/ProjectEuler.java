import cli.CommandLineArgs;
import cli.CommandLineArgsParser;
import problems.PE0001;
import problems.PE0002;
import problems.PE0003;
import problems.PE0004;
import problems.PE0005;
import problems.PE0006;
import problems.PE0007;
import problems.PE0008;
import problems.PE0009;
import problems.PE0010;
import problems.PE0011;
import problems.PE0012;
import util.Problem;
import util.ProblemSolution;

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
            Map.entry(12, new PE0012())
    );

    public static void main(String[] args) {
        CommandLineArgsParser argsParser = new CommandLineArgsParser();
        CommandLineArgs parsedArgs = argsParser.parseArgs(args);

        if (parsedArgs.getProblemNumber() == null) {
            System.out.println("Running all Problems.");
            for (int i = 1; numberToProblem.containsKey(i); i++) {
                runProblem(i);
            }
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
