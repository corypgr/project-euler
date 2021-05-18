package corypgr.project.euler.problems;

import corypgr.project.euler.problems.prime.PrimeGenerator;
import corypgr.project.euler.problems.util.CountMap;
import corypgr.project.euler.problems.util.DivisorsUtil;
import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

/**
 * Problem 75
 *
 * https://projecteuler.net/problem=75
 *
 * This is a pretty interesting question. I see two general approaches to use. First, we could go through every length,
 * and try to create Pythagorean Triples out of sides that add up to that length. We would count the number of
 * configurations we get, keeping track of the single count ones. The other approach would be to try to generate all
 * Pythagorean Triples whose sides are less than our max. In this case, we would maintain a CountMap<Long> for our
 * different lengths.
 *
 * The first option can be optimized. Various properties of Pyhtagorean Triples can be leveraged to reduce our search
 * space significantly. See https://en.wikipedia.org/wiki/Pythagorean_triple#General_properties for things like a, b,
 * and c are all even or only 2 are odd (meaning the perimeter is always even) and only one of a and b is divisible by
 * 3.
 *
 * On the other hand, generating all Pythagorean Triples can be done efficiently using Euclid's formula. I will attempt
 * this option first. For max m, n values, we can take the perimeter sum of a = m^2 - n^2, b = 2*m*n, c = m^2 + n^2:
 * perimeter = m^2 - n^2 + 2*m*n + m^2 + n^2
 * perimeter = 2*m^2 + 2*m*n
 * Assuming the smallest possible n (n = 1), we can put in our max and see what m comes out at:
 * 1,500,000 = 2*m^2 + 2*m
 * 750,000 = m^2 + m
 * m = 865.526 (865 integer value) is our max m.
 *
 * Since we only care about the perimeters, we can stick with the simplified 2*m^2 + 2*m*n formula derived above and
 * avoid determining the actual Pythagorean Triplets. We'll try all coprime combinations (m, n) of 865 >= m > n > 0,
 * which gives us all primitive Pythagorean Triplet values. Then we'll check each multiple of the perimeters of those
 * triplets up to 1.5 million to create all triplet values.
 */
public class PE0075 implements Problem {
    private static final long MAX_PERIMETER = 1_500_000;
    private static final long MAX_M = 865;

    @Override
    public ProblemSolution solve() {
        Map<Long, List<Long>> valToPrimeDivisors = getPrimeDivisorsMap();

        CountMap<Long> perimeterCountMap = new CountMap<>();
        for (long m = 1; m <= MAX_M; m++) {
            long mSquared = m * m;
            // One of m or n must be even for the perimeter to be primitive.
            for (long n = m % 2 == 0 ? 1 : 2; n < m; n += 2) {
                if (areCoprime(valToPrimeDivisors.get(m), valToPrimeDivisors.get(n))) {
                    List<Long> perimeters = getAllPerimetersForMAndN(m, n, mSquared);
                    perimeters.forEach(perimeterCountMap::addCount);
                }
            }
        }

        long countOfOnly1Triplet = perimeterCountMap.values().stream()
                .filter(count -> count == 1)
                .count();
        return ProblemSolution.builder()
                .solution(countOfOnly1Triplet)
                .descriptiveSolution("Number of Perimeters with exactly 1 Pythagorean Triplet: " + countOfOnly1Triplet)
                .build();
    }

    private Map<Long, List<Long>> getPrimeDivisorsMap() {
        PrimeGenerator primeGenerator = new PrimeGenerator();
        List<Long> primes = primeGenerator.generatePrimesList(MAX_M);
        DivisorsUtil divisorsUtil = new DivisorsUtil();

        return LongStream.rangeClosed(1, MAX_M)
                .boxed()
                .collect(Collectors.toMap(Function.identity(), d -> getSortedPrimeDivisors(d, divisorsUtil, primes)));
    }

    private List<Long> getSortedPrimeDivisors(long d, DivisorsUtil divisorsUtil, List<Long> primes) {
        return divisorsUtil.getPrimeDivisors(d, primes).stream()
                .sorted()
                .collect(Collectors.toList());
    }

    /**
     * Lists are sorted. We can iterate through to find any intersection.
     */
    private boolean areCoprime(List<Long> aPrimeDivisors, List<Long> bPrimeDivisors) {
        int aIndex = 0;
        int bIndex = 0;
        while (aIndex < aPrimeDivisors.size() && bIndex < bPrimeDivisors.size()) {
            long aVal = aPrimeDivisors.get(aIndex);
            long bVal = bPrimeDivisors.get(bIndex);

            if (aVal == bVal) {
                return false;
            } else if (aVal < bVal) {
                aIndex++;
            } else {
                bIndex++;
            }
        }

        // no matches found.
        return true;
    }

    /**
     * Calculates the primitive perimeter for the given m and n, then multiplies that perimeter until we pass the max
     * perimeter.
     */
    private List<Long> getAllPerimetersForMAndN(long m, long n, long mSquared) {
        long primitivePerimeter = 2 * (mSquared + (m * n));
        return LongStream.iterate(primitivePerimeter, perimeter -> perimeter <= MAX_PERIMETER,
                    perimeter -> perimeter += primitivePerimeter)
                .boxed()
                .collect(Collectors.toCollection(LinkedList::new));
    }
}
