package corypgr.project.euler.stats;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Generates stats for problem solving goals and progress, generating a new README.md file.
 */
public class StatsGenerator {
    public static void main(String[] args) throws Exception {
        LocalDate projectStartDate = LocalDate.parse("2021-03-15");
        LocalDate currentDate = LocalDate.now();

        // Add 1 so that the goal includes the current day.
        long problemsSolvedGoal = projectStartDate.until(currentDate, ChronoUnit.DAYS) + 1;
        long problemsSolvedActual = Files.list(Path.of("src/main/java/corypgr/project/euler/problems"))
                .map(Path::getFileName)
                .map(Path::toString)
                .filter(name -> name.matches("PE[\\d]{4}\\.java"))
                .count();

        // If actual is behind the goal, then we need to catch up.
        // Add 1 so that the due date is the day after when our actual matches the goal.
        LocalDate nextProblemDueDate = currentDate.plusDays(problemsSolvedActual - problemsSolvedGoal + 1);

        String readmeTemplate = Files.readString(Path.of("src/main/java/corypgr/project/euler/stats/ReadmeTemplate.md"));
        String readmeWithFieldsFilledIn = readmeTemplate
                .replace("<DateStartedSolving>", projectStartDate.toString())
                .replace("<ProblemsSolvedGoal>", Long.toString(problemsSolvedGoal))
                .replace("<ProblemsSolvedActual>", Long.toString(problemsSolvedActual))
                .replace("<NextSolutionDueDate>", nextProblemDueDate.toString());
        Files.writeString(Path.of("README.md"), readmeWithFieldsFilledIn);
    }
}
