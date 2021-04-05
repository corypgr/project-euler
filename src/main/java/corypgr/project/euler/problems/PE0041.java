package corypgr.project.euler.problems;

import corypgr.project.euler.problems.prime.PrimeGenerator;
import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Problem 41
 *
 * https://projecteuler.net/problem=41
 *
 * Hmm, the solution isn't hard to write here. Generate all primes <= 987654321, the max pandigital number. Then check
 * all of them in reverse to find the first one that is pandigital.
 *
 * I expect this is going to be very slow though... Generating prime numbers isn't exactly a fast operation. Finding
 * them up through 1 billion is going to take a while.
 *
 * Cheating a little bit here. 987654321 and 87654321 both resulted in OutOfMemory errors :). When running with 7654321
 * as the max we get the result we're looking for. Still takes 5-6 seconds to run everything, but I can't get that
 * shorter without figuring out a more efficient way to generate the large prime numbers.
 */
public class PE0041 implements Problem {
    private static final long MAX_PANDIGITAL = 7654321L;
    @Override
    public ProblemSolution solve() {
        PrimeGenerator primeGenerator = new PrimeGenerator();
        List<Long> primes = primeGenerator.generatePrimesList(MAX_PANDIGITAL);

        long largestPandigitalPrime = -1L;
        for (int i = primes.size() - 1; largestPandigitalPrime < 0; i--) {
            if (isPandigital(primes.get(i))) {
                largestPandigitalPrime = primes.get(i);
            }
        }

        return ProblemSolution.builder()
                .solution(largestPandigitalPrime)
                .descriptiveSolution("Largest pandigital prime number: " + largestPandigitalPrime)
                .build();
    }

    private boolean isPandigital(long prime) {
        String primeStr = String.valueOf(prime);
        int primeStrLength = primeStr.length();

        Set<Integer> uniqueDigits = primeStr.chars()
                .mapToObj(Character::getNumericValue)
                .filter(v -> v <= primeStrLength)
                .collect(Collectors.toSet());

        // 0 is not a number in the pandigital sequence.
        return uniqueDigits.size() == primeStr.length() && !uniqueDigits.contains(0);
    }
}
