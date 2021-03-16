package problems;

import prime.PrimeGenerator;

import java.util.List;

/**
 * Problem 10
 *
 * https://projecteuler.net/problem=10
 *
 * With our PrimeGenerator this problem is pretty easy.
 */
public class PE0010 {
    private static final long MAX_PRIME = 2_000_000;
    
    public static void main(String[] args) {
        PrimeGenerator primeGenerator = new PrimeGenerator();

        List<Long> primes = primeGenerator.generatePrimesList(MAX_PRIME);
        long sum = primes.stream()
                .mapToLong(Long::longValue)
                .sum();
        System.out.println("Prime Sum: " + sum);
    }
}
