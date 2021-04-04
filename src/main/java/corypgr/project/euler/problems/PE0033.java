package corypgr.project.euler.problems;

import corypgr.project.euler.problems.prime.PrimeGenerator;
import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;
import lombok.Value;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Problem 33
 *
 * https://projecteuler.net/problem=33
 *
 * We're not dealing with that many numbers. Let's try all of the combinations, check if this "incorrect" cancelling
 * produces the same result as if we did the division normally.
 */
public class PE0033 implements Problem {
    @Override
    public ProblemSolution solve() {
        Set<Fraction> strangeFractions = new HashSet<>();
        for (int numerator = 10; numerator <= 99; numerator++) {
            for (int denominator = numerator + 1; denominator <= 99; denominator++) {
                if (isStrangeFraction(numerator, denominator)) {
                    strangeFractions.add(new Fraction(numerator, denominator));
                }
            }
        }

        if (strangeFractions.size() != 4) {
            throw new IllegalStateException("Should be exactly 4 of these strange fractions. Found "
                    + strangeFractions.size());
        }

        Fraction fractionProduct = strangeFractions.stream()
                .reduce(new Fraction(1, 1), (v1, v2) ->
                        new Fraction(v1.getNumerator() * v2.getNumerator(), v1.getDenominator() * v2.getDenominator()));

        Fraction simplifiedFraction = simplifyFraction(fractionProduct);

        return ProblemSolution.builder()
                .solution(simplifiedFraction.getDenominator())
                .descriptiveSolution("simplified denominators of the strange fractions product: " +
                        simplifiedFraction.getDenominator())
                .build();
    }

    private boolean isStrangeFraction(int numerator, int denominator) {
        int numeratorTens = numerator / 10;
        int numeratorOnes = numerator % 10;
        int denominatorTens = denominator / 10;
        int denominatorOnes = denominator % 10;
        double divisionResult = (double) numerator / denominator;

        if (numeratorTens == denominatorTens && divisionResult == (double) numeratorOnes / denominatorOnes) {
            return true;
        }

        // 0 check is to avoid the "trivial" cases. THe tens spot cannot be 0, so no similar check is done above.
        if (numeratorOnes == denominatorOnes && numeratorOnes != 0 &&
                divisionResult == (double) numeratorTens / denominatorTens) {
            return true;
        }

        if (numeratorTens == denominatorOnes && divisionResult == (double) numeratorOnes / denominatorTens) {
            return true;
        }

        return numeratorOnes == denominatorTens && divisionResult == (double) numeratorTens / denominatorOnes;
    }

    private Fraction simplifyFraction(Fraction fraction) {
        PrimeGenerator primeGenerator = new PrimeGenerator();
        // We know that none of the prime divisors are greater than 100.
        List<Long> primes = primeGenerator.generatePrimesList(100);

        int numerator = fraction.getNumerator();
        int denominator = fraction.getDenominator();

        Iterator<Long> primeIter = primes.iterator();
        for (long prime = primeIter.next(); primeIter.hasNext() && prime <= numerator; prime = primeIter.next()) {
            while (numerator % prime == 0 && denominator % prime == 0) {
                numerator /= prime;
                denominator /= prime;
            }
        }

       return new Fraction(numerator, denominator);
    }

    @Value
    private class Fraction {
        private final int numerator;
        private final int denominator;
    }
}
