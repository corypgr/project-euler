package corypgr.project.euler.problems;

import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;

import java.time.DayOfWeek;
import java.time.LocalDate;

/**
 * Problem 19
 *
 * https://projecteuler.net/problem=19
 *
 * It's clear how you're expected to solve this. We're given calculations that tell us how many months are in a year,
 * and a starting point to determine what the day of the week was on certain days. You would use those to run through
 * all of the months in the twentieth century to see when the first of the month fell on a Sunday. I could do that, but
 * I think there's a lot of room for error there. Normally, if I were to write something that relied on dates being
 * correct, I wouldn't try to calculate those dates manually. I would use a date library. That's what I'm going to do
 * here as well, basically cycling through all of the months and plugging the first of the month into the Java time
 * library to see if that date falls on a Sunday.
 */
public class PE0019 implements Problem {
    @Override
    public ProblemSolution solve() {
        LocalDate firstOfMonth = LocalDate.parse("1901-01-01");
        int sundayCount = 0;
        for (; firstOfMonth.getYear() < 2001; firstOfMonth = firstOfMonth.plusMonths(1)) {
            if (firstOfMonth.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
                sundayCount++;
            }
        }

        return ProblemSolution.builder()
                .solution(sundayCount)
                .descriptiveSolution("Number of first days of the month that is a Sunday: " + sundayCount)
                .build();
    }
}
