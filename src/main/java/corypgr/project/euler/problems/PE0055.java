package corypgr.project.euler.problems;

import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.LongStream;

/**
 * Problem 55
 *
 * https://projecteuler.net/problem=55
 *
 * Seems pretty straightforward. For each number we can repeatedly do this addition and palindrome check, keeping track
 * of all of the numbers that create a palindrome.
 *
 * Not a lot of optimizations here. We can track all the numbers we've seen so far under 10 thousand and return early if
 * we see a repeat. This probably won't save us a ton of time, but it's something.
 */
public class PE0055 implements Problem {
    private static final long MAX_NUM = 10_000;
    private static final BigInteger MAX_NUM_BIG_INTEGER = BigInteger.valueOf(MAX_NUM);
    private static final int MAX_ITERATION = 50;

    @Override
    public ProblemSolution solve() {
        Set<Long> lychrelNumbersUnderMaxNum = new HashSet<>();
        Set<Long> nonLychrelNumbersUnderMaxNum = new HashSet<>();

        long countOfLychrelNumbersUnderMaxNum = LongStream.range(1, MAX_NUM)
                .filter(num -> isLychrelNumber(num, lychrelNumbersUnderMaxNum, nonLychrelNumbersUnderMaxNum))
                .count();

        return ProblemSolution.builder()
                .solution(countOfLychrelNumbersUnderMaxNum)
                .descriptiveSolution("Number of Lychrel numbers under 10 thousand: " + countOfLychrelNumbersUnderMaxNum)
                .build();
    }

    private boolean isLychrelNumber(long num, Set<Long> lychrelNumbers, Set<Long> nonLychrelNumbers) {
        if (lychrelNumbers.contains(num)) {
            return true;
        }
        if (nonLychrelNumbers.contains(num)) {
            return false;
        }

        Set<BigInteger> numbersSeen = new HashSet<>();

        BigInteger curNum = BigInteger.valueOf(num);
        numbersSeen.add(curNum);
        String curNumStr = curNum.toString();
        String curNumStrReversed = reverseString(curNumStr);
        for (int i = 0; i < MAX_ITERATION; i++) {
            BigInteger reversedNum = new BigInteger(curNumStrReversed);
            curNum = reversedNum.add(curNum);

            curNumStr = curNum.toString();
            curNumStrReversed = reverseString(curNumStr);
            if (isPalindrome(curNumStr, curNumStrReversed)) {
                numbersSeen.stream()
                        .filter(val -> val.compareTo(MAX_NUM_BIG_INTEGER) < 0)
                        .map(BigInteger::longValueExact)
                        .forEach(nonLychrelNumbers::add);

                return false;
            }

            // Adding to numbersSeen here so that we don't include the last sum when returning false.
            // That sum is a palindrome already, but wasn't constructed using repeated reversing and
            // addition so we don't know if it is a Lychrel number.
            numbersSeen.add(curNum);
        }

        numbersSeen.stream()
                .filter(val -> val.compareTo(MAX_NUM_BIG_INTEGER) < 0)
                .map(BigInteger::longValueExact)
                .forEach(lychrelNumbers::add);
        return true;
    }

    private String reverseString(String val) {
        StringBuilder sb = new StringBuilder();
        for (char c : val.toCharArray()) {
            sb.insert(0, c);
        }
        return sb.toString();
    }

    private boolean isPalindrome(String val, String reversed) {
        int maxSizeToCheck = val.length() / 2; // Only need to go up to the half way point.
                                               // If those match we know the rest matches.
        for (int i = 0; i < maxSizeToCheck; i++) {
            if (val.charAt(i) != reversed.charAt(i)) {
                return false;
            }
        }
        return true;
    }
}
