package corypgr.project.euler.problems;

import corypgr.project.euler.problems.util.CountMap;
import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;
import lombok.SneakyThrows;
import lombok.Value;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

/**
 * Problem 98
 *
 * https://projecteuler.net/problem=98
 *
 * Interesting. I think I'm going to approach this in phases. First, we want to find all anagram "groups" of words. Then
 * we'll want to find all anagram "groups" of squares, with squares up to the same length (digit-wise) as the largest
 * anagram word. We could generate all number combinations and check if they are squares, but it should be easier to
 * generate all squares and only keep the ones that have other squares as anagrams.
 *
 * Once we have the two sets of of anagram groups, I'm thinking we can try to sort of map the number anagrams onto the
 * word anagrams. If a mapping fits (same length, duplicates in same places, etc..) then we keep that mapping of
 * character to number and test if the same char->num mapping works for other sets in the same groupings.
 */
public class PE0098 implements Problem {
    private static final String FILE_PATH = "src/main/java/corypgr/project/euler/problems/resources/PE0098_words";

    @Override
    public ProblemSolution solve() {
        long solution = findLargestSquareAnagramWordPairSquareValue();
        return ProblemSolution.builder()
                .solution(solution)
                .descriptiveSolution("Largest square from an anagram square pair: " + solution)
                .build();
    }

    private long findLargestSquareAnagramWordPairSquareValue() {
        List<String> words = getWords();
        Map<Integer, List<AnagramSet>> wordAnagramSets = getAnagramSetsAsLengthToSets(words);

        int maxWordAnagramLength = wordAnagramSets.keySet().stream()
                .mapToInt(Integer::intValue)
                .max()
                .getAsInt();
        List<String> squaresAsStrings = getSquaresAsStrings(maxWordAnagramLength);
        Map<Integer, List<AnagramSet>> squaresAnagramSets = getAnagramSetsAsLengthToSets(squaresAsStrings);

        // We return when we've found a match for the longest anagram length so far. All squares under that anagram
        // length will be smaller numbers since their length is smaller.
        for (int anagramLength = maxWordAnagramLength; anagramLength > 0; anagramLength--) {
            List<AnagramSet> wordSetForLength = wordAnagramSets.getOrDefault(anagramLength, Collections.emptyList());
            List<AnagramSet> squareSetForLength = squaresAnagramSets.getOrDefault(anagramLength, Collections.emptyList());

            long bestMatchingSquare = 0;

            for (AnagramSet wordSet : wordSetForLength) {
                for (AnagramSet squareSet : squareSetForLength) {
                    Long maxSquareForSets = maxMatchingSquareIfContainsSquareAnagramWordPair(wordSet, squareSet);
                    if (maxSquareForSets != null && maxSquareForSets > bestMatchingSquare) {
                        bestMatchingSquare = maxSquareForSets;
                    }
                }
            }

            if (bestMatchingSquare > 0) {
                return bestMatchingSquare;
            }
        }

        // Should be unreachable if there is a solution.
        return -1;
    }

    private Long maxMatchingSquareIfContainsSquareAnagramWordPair(AnagramSet wordSet, AnagramSet squareSet) {
        // Contains different number of unique characters, so cannot match.
        if (wordSet.getCountMap().keySet().size() != squareSet.getCountMap().keySet().size()) {
            return null;
        }

        List<String> matchingSquares = new LinkedList<>();

        List<String> words = new ArrayList<>(wordSet.getValues());
        // Don't need to process the last word. Any found pairs would already consider the last word.
        for (int i = 0; i < words.size() - 1; i++) {
            String word = words.get(i);

            for (String square : squareSet.getValues()) {
                Map<Character, Character> charToNumberMapping = getCharToNumberMappingIfFits(word, square);

                // If we find a valid mapping, check if that mapping applied to the rest of the words produces
                // any of the anagram squares.
                if (charToNumberMapping != null) {
                    for (int j = i + 1; j < words.size(); j++) {
                        String convertedWord = applyCharToNumberMapping(words.get(j), charToNumberMapping);

                        if (squareSet.getValues().contains(convertedWord)) {
                            matchingSquares.add(square);
                            matchingSquares.add(convertedWord);
                        }
                    }
                }
            }
        }

        return matchingSquares.stream()
                .map(Long::parseLong)
                .max(Long::compareTo)
                .orElse(null);
    }

    private Map<Character, Character> getCharToNumberMappingIfFits(String word, String square) {
        Map<Character, Character> map = new HashMap<>();
        for (int i = 0; i < word.length(); i++) {
            char wordChar = word.charAt(i);
            char squareChar = square.charAt(i);

            // Creates a mapping, returning null if there is a conflict. Only works because we know the number of unique
            // chars is the same between the two strings.
            if (!map.containsKey(wordChar)) {
                map.put(wordChar, squareChar);
            } else if (map.containsKey(wordChar) && map.get(wordChar) != squareChar) {
                return null;
            }
        }
        return map;
    }

    private String applyCharToNumberMapping(String word, Map<Character, Character> charToNumberMapping) {
        StringBuilder sb = new StringBuilder();
        for (char wordChar : word.toCharArray()) {
            sb.append(charToNumberMapping.get(wordChar));
        }
        return sb.toString();
    }

    private List<String> getSquaresAsStrings(int maxLength) {
        long maxSquareExclusive = (long) Math.pow(10, maxLength);
        return LongStream.iterate(0, i -> i + 1)
                .map(v -> v * v)
                .takeWhile(v -> v < maxSquareExclusive)
                .mapToObj(Long::toString)
                .collect(Collectors.toList());
    }

    private Map<Integer, List<AnagramSet>> getAnagramSetsAsLengthToSets(Collection<String> values) {
        Map<CountMap<Character>, Set<String>> countMapToAnagrams = new HashMap<>();
        for (String value : values) {
            CountMap<Character> countMap = new CountMap<>();
            value.chars()
                    .mapToObj(c -> (char) c)
                    .forEach(countMap::addCount);

            Set<String> existingSet = countMapToAnagrams.getOrDefault(countMap, null);
            if (existingSet == null) {
                existingSet = new HashSet<>();
                countMapToAnagrams.put(countMap, existingSet);
            }
            existingSet.add(value);
        }

        Map<Integer, List<AnagramSet>> anagramLengthToSets = new HashMap<>();
        for (Map.Entry<CountMap<Character>, Set<String>> entry : countMapToAnagrams.entrySet()) {
            // If only 1 value, then there are no anagrams of the word.
            if (entry.getValue().size() > 1) {
                AnagramSet anagramSet = new AnagramSet(entry.getKey(), entry.getValue());
                int anagramLength = anagramSet.getAnagramLength();

                List<AnagramSet> existingList = anagramLengthToSets.getOrDefault(anagramLength, null);
                if (existingList == null) {
                    existingList = new LinkedList<>();
                    anagramLengthToSets.put(anagramLength, existingList);
                }
                existingList.add(anagramSet);
            }
        }

        return anagramLengthToSets;
    }

    @SneakyThrows
    private List<String> getWords() {
        String fileContents = Files.readString(Paths.get(FILE_PATH));
        return Arrays.stream(fileContents.split(","))
                .map(name -> name.replaceAll("[\" \n]", "")) // remove random quotes, spaces, and newlines
                .map(String::toUpperCase) // Make sure we're only dealing with uppercase
                .collect(Collectors.toList());
    }

    @Value
    private static final class AnagramSet {
        private final CountMap<Character> countMap;
        private final Set<String> values;

        public int getAnagramLength() {
            return values.iterator().next().length();
        }
    }
}
