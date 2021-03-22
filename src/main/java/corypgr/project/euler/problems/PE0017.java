package corypgr.project.euler.problems;

import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;

import java.util.stream.IntStream;

/**
 * Problem 17
 *
 * https://projecteuler.net/problem=17
 *
 * Pretty straightforward question. Generate all of the numbers in words, count the number of characters. I'll take a
 * shortcut and just add up the word sizes without actually generating the strings.
 */
public class PE0017 implements Problem {
    private static final int THOUSAND = 8; // "THOUSAND"
    private static final int HUNDRED = 7;  // "HUNDRED"
    private static final int AND = 3;      // "AND"
    private static final int[] LOWER_NUMBERS = {
            0, // Zero isn't said when writing out in words.
            3, // "ONE"
            3, // "TWO"
            5, // "THREE"
            4, // "FOUR"
            4, // "FIVE"
            3, // "SIX"
            5, // "SEVEN"
            5, // "EIGHT"
            4, // "NINE"
            3, // "TEN"
            6, // "ELEVEN"
            6, // "TWELVE"
            8, // "THIRTEEN"
            8, // "FOURTEEN"
            7, // "FIFTEEN"
            7, // "SIXTEEN"
            9, // "SEVENTEEN"
            8, // "EIGHTEEN"
            8  // "NINETEEN"
    };
    private static final int[] TENS = {
            0, // Noughts are included above
            0, // Up through 19 are included above
            6, // "TWENTY"
            6, // "THIRTY"
            5, // "FORTY"
            5, // "FIFTY"
            5, // "SIXTY"
            7, // "SEVENTY"
            6, // "EIGHTY"
            6  // "NINETY"
    };

    @Override
    public ProblemSolution solve() {
        int wordSum = IntStream.rangeClosed(1, 1000)
                .map(this::wordLetterCountOf)
                .sum();

        return ProblemSolution.builder()
                .solution(wordSum)
                .descriptiveSolution("Count of letters: " + wordSum)
                .build();
    }

    private int wordLetterCountOf(int num) {
        if (num >= 1000 && num % 1000 == 0) { // If exactly divisible by 1000, no AND needed
            return wordLetterCountOf(num / 1000) + THOUSAND;
        }
        if (num >= 1000) {
            return wordLetterCountOf(num / 1000) + wordLetterCountOf(num % 1000) + THOUSAND + AND;
        }
        if (num >= 100 && num % 100 == 0) { // If exactly divisible y 100, no AND needed
            return wordLetterCountOf(num / 100) + HUNDRED;
        }
        if (num >= 100) {
            return wordLetterCountOf(num / 100) + wordLetterCountOf(num % 100) + HUNDRED + AND;
        }
        if (num < 20) {
            return LOWER_NUMBERS[num];
        }
        return TENS[num / 10] + wordLetterCountOf(num % 10);
    }
}
