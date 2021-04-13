package corypgr.project.euler.problems;

import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;
import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Problem 59
 *
 * https://projecteuler.net/problem=59
 *
 * Cool problem. There's not that many character combinations, so we can try them all. I can't think of a trick for this.
 * The main decision is what english words we'll look for. I'll check for decrypted texts containing all of the first 10
 * words at https://en.wikipedia.org/wiki/Most_common_words_in_English that are larger than 1 char.
 *
 * This works, but is a little slow. Initially, I was using CombinationUtil and PermutationUtil to generate all passphrases.
 * I swapped this out for a simple generator, but things still run a little slowly.
 */
public class PE0059 implements Problem {
    private static final String FILE_PATH = "src/main/java/corypgr/project/euler/problems/resources/PE0059_cipher";
    private static final List<Character> LOWERCASE_CHARS = List.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
            'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z');
    private static final List<String> COMMON_WORDS = List.of("the", "be", "to", "of","and", "in", "that", "have");

    @Override
    public ProblemSolution solve() {
        List<Integer> cipher = getCipher();
        List<String> possibleValidDecryptions = getAllPassphrases().stream()
                .map(phrase -> decrypt(cipher, phrase))
                .filter(this::containsCommonWords)
                .collect(Collectors.toList());

        if (possibleValidDecryptions.size() != 1) {
            throw new IllegalStateException("Expected only a single valid decryption, but found "
                    + possibleValidDecryptions.size());
        }

        int sumOfAsciiValues = possibleValidDecryptions.stream()
                .flatMapToInt(String::chars)
                .sum();
        return ProblemSolution.builder()
                .solution(sumOfAsciiValues)
                .descriptiveSolution("Sum of the ascii values of our decrypted string: " + sumOfAsciiValues)
                .build();
    }

    private List<List<Character>> getAllPassphrases() {
        List<List<Character>> passphrases = new LinkedList<>();
        for (char a = 'a'; a <= 'z'; a++) {
            for (char b = 'a'; b <= 'z'; b++) {
                for (char c = 'a'; c <= 'z'; c++) {
                    passphrases.add(List.of(a, b, c));
                }
            }
        }
        return passphrases;
    }

    private String decrypt(List<Integer> cipher, List<Character> passphrase) {
        List<Integer> passphraseAsciiCode = List.of((int) passphrase.get(0), (int) passphrase.get(1),
                (int) passphrase.get(2));

        List<Integer> decryptedAsciiCode = new LinkedList<>();
        for (int i = 0; i < cipher.size(); i += 3) {
            decryptedAsciiCode.add(passphraseAsciiCode.get(0) ^ cipher.get(i));
            decryptedAsciiCode.add(passphraseAsciiCode.get(1) ^ cipher.get(i + 1));
            decryptedAsciiCode.add(passphraseAsciiCode.get(2) ^ cipher.get(i + 2));
        }

        // If Cipher isn't exactly divisible by 3 then we do a partial decryption of the end here.
        int amountToDecryptAtEnd = cipher.size() % 3;
        for (int i = 0; i < amountToDecryptAtEnd; i++) {
            decryptedAsciiCode.add(passphraseAsciiCode.get(i) ^ cipher.get(cipher.size() - amountToDecryptAtEnd - 1 + i));
        }

        String result = decryptedAsciiCode.stream()
                .map(val -> (char) val.intValue())
                .map(String::valueOf)
                .collect(Collectors.joining());
        return result;
    }

    private boolean containsCommonWords(String decrypted) {
        return COMMON_WORDS.stream()
                .allMatch(decrypted.toLowerCase()::contains);
    }

    @SneakyThrows
    private List<Integer> getCipher() {
        String fileContents = Files.readString(Paths.get(FILE_PATH));
        return Arrays.stream(fileContents.split(","))
                .map(val -> val.replaceAll("[\n]", "")) // replace random garbage chars.
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }
}
