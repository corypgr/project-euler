package corypgr.project.euler.problems;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import corypgr.project.euler.problems.util.Problem;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Base64;
import java.util.stream.Stream;

public class ProblemSolutionTest {

    @ParameterizedTest
    @MethodSource("problemAndExpectedSolution")
    void solutionIsCorrect(Problem problem, String expectedSolution) {
        assertEquals(expectedSolution, problem.solve().getSolution().toString());
    }

    /**
     * Base64 encoding these so that the solutions aren't posted directly online in an attempt to honor the Project
     * Euler guidelines while still maintaining test cases for the solutions.
     */
    static Stream<Arguments> problemAndExpectedSolution() {
        Stream<Arguments> argsWithEncodedSolution = Stream.of(
                arguments(new PE0001(), "MjMzMTY4"),
                arguments(new PE0002(), "NDYxMzczMg=="),
                arguments(new PE0003(), "Njg1Nw=="),
                arguments(new PE0004(), "OTA2NjA5"),
                arguments(new PE0005(), "MjMyNzkyNTYw"),
                arguments(new PE0006(), "MjUxNjQxNTA="),
                arguments(new PE0007(), "MTA0NzQz"),
                arguments(new PE0008(), "MjM1MTQ2MjQwMDA="),
                arguments(new PE0009(), "MzE4NzUwMDA="),
                arguments(new PE0010(), "MTQyOTEzODI4OTIy"),
                arguments(new PE0011(), "NzA2MDA2NzQ="),
                arguments(new PE0012(), "NzY1NzY1MDA="),
                arguments(new PE0013(), "NTUzNzM3NjIzMA=="),
                arguments(new PE0014(), "ODM3Nzk5"),
                arguments(new PE0015(), "MTM3ODQ2NTI4ODIw"),
                arguments(new PE0016(), "MTM2Ng=="),
                arguments(new PE0017(), "MjExMjQ="),
                arguments(new PE0018(), "MTA3NA=="),
                arguments(new PE0019(), "MTcx"),
                arguments(new PE0020(), "NjQ4"),
                arguments(new PE0021(), "MzE2MjY="),
                arguments(new PE0022(), "ODcxMTk4Mjgy"),
                arguments(new PE0023(), "NDE3OTg3MQ=="),
                arguments(new PE0024(), "Mjc4MzkxNTQ2MA=="),
                arguments(new PE0025(), "NDc4Mg=="),
                arguments(new PE0026(), "OTgz"),
                arguments(new PE0027(), "LTU5MjMx"),
                arguments(new PE0028(), "NjY5MTcxMDAx"),
                arguments(new PE0029(), "OTE4Mw=="),
                arguments(new PE0030(), "NDQzODM5"),
                arguments(new PE0031(), "NzM2ODI="),
                arguments(new PE0032(), "NDUyMjg="),
                arguments(new PE0033(), "MTAw"),
                arguments(new PE0034(), "NDA3MzA="),
                arguments(new PE0035(), "NTU="),
                arguments(new PE0036(), "ODcyMTg3"),
                arguments(new PE0037(), "NzQ4MzE3"),
                arguments(new PE0038(), "OTMyNzE4NjU0"),
                arguments(new PE0039(), "ODQw"),
                arguments(new PE0040(), "MjEw"),
                arguments(new PE0041(), "NzY1MjQxMw=="),
                arguments(new PE0042(), "MTYy"),
                arguments(new PE0043(), "MTY2OTUzMzQ4OTA="),
                arguments(new PE0044(), "NTQ4MjY2MA=="),
                arguments(new PE0045(), "MTUzMzc3NjgwNQ=="),
                arguments(new PE0046(), "NTc3Nw=="),
                arguments(new PE0047(), "MTM0MDQz"),
                arguments(new PE0048(), "OTExMDg0NjcwMA=="),
                arguments(new PE0049(), "Mjk2OTYyOTk5NjI5"),
                arguments(new PE0050(), "OTk3NjUx"),
                arguments(new PE0051(), "MTIxMzEz"),
                arguments(new PE0052(), "MTQyODU3"),
                arguments(new PE0053(), "NDA3NQ=="),
                arguments(new PE0054(), "Mzc2"),
                arguments(new PE0055(), "MjQ5"),
                arguments(new PE0056(), "OTcy"),
                arguments(new PE0057(), "MTUz"),
                arguments(new PE0058(), "MjYyNDE="),
                arguments(new PE0059(), "MTI5NDQ4"),
                arguments(new PE0060(), "MjYwMzM="),
                arguments(new PE0061(), "Mjg2ODQ="),
                arguments(new PE0062(), "MTI3MDM1OTU0Njgz"),
                arguments(new PE0063(), "NDk="),
                arguments(new PE0064(), "MTMyMg=="),
                arguments(new PE0065(), "Mjcy"),
                arguments(new PE0066(), "NjYx"),
                arguments(new PE0067(), "NzI3Mw=="),
                arguments(new PE0068(), "NjUzMTAzMTkxNDg0MjcyNQ=="),
                arguments(new PE0069(), "NTEwNTEw"),
                arguments(new PE0070(), "ODMxOTgyMw=="),
                arguments(new PE0071(), "NDI4NTcw"),
                arguments(new PE0072(), "MzAzOTYzNTUyMzkx"),
                arguments(new PE0073(), "NzI5NTM3Mg=="),
                arguments(new PE0074(), "NDAy"),
                arguments(new PE0075(), "MTYxNjY3"),
                arguments(new PE0076(), "MTkwNTY5Mjkx"),
                arguments(new PE0077(), "NzE="),
                arguments(new PE0078(), "NTUzNzQ="),
                arguments(new PE0079(), "NzMxNjI4OTA="),
                arguments(new PE0080(), "NDA4ODY="),
                arguments(new PE0081(), "NDI3MzM3"),
                arguments(new PE0082(), "MjYwMzI0"),
                arguments(new PE0083(), "NDI1MTg1"),
                arguments(new PE0084(), "MTAxNTI0"),
                arguments(new PE0085(), "Mjc3Mg=="),
                arguments(new PE0086(), "MTgxOA=="),
                arguments(new PE0087(), "MTA5NzM0Mw=="),
                arguments(new PE0088(), "NzU4NzQ1Nw=="),
                arguments(new PE0089(), "NzQz"),
                arguments(new PE0091(), "MTQyMzQ="),
                arguments(new PE0092(), "ODU4MTE0Ng=="),
                arguments(new PE0093(), "MTI1OA=="),
                arguments(new PE0094(), "NTE4NDA4MzQ2"),
                arguments(new PE0095(), "MTQzMTY="),
                arguments(new PE0096(), "MjQ3MDI="),
                arguments(new PE0097(), "ODczOTk5MjU3Nw=="),
                arguments(new PE0098(), "MTg3Njk="),
                arguments(new PE0099(), "NzA5"));

        return argsWithEncodedSolution.map(args -> arguments(args.get()[0], decode((String) args.get()[1])));
    }

    private static String decode(String encodedVal) {
        return new String(Base64.getDecoder().decode(encodedVal));
    }
}
