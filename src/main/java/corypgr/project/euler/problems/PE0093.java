package corypgr.project.euler.problems;

import corypgr.project.euler.problems.util.CombinationUtil;
import corypgr.project.euler.problems.util.PermutationUtil;
import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

/**
 * Problem 93
 *
 * https://projecteuler.net/problem=93
 *
 * This problem requires a few different things:
 *  * We need to know all possible arithmetic expressions, with parens.
 *  * We need to know all combinations of digits to insert into those expressions.
 *  * We need to be able to solve those expressions.
 *
 * For determining all arithmetic expressions, I've separately generated all operation permutations (with repeats) and
 * all paren permutations, then combined those together. There's some extra logic to avoid duplicate expressions caused
 * by unnecessary parens, but this doesn't go too far in that regard. We're ok with some duplicates there since the
 * number won't be too large.
 *
 * I've updated the CombinationUtil class to also generate combinations without repeats, and used that to determine all
 * combinations of digits.
 *
 * Finally, I've written an Expression class which will parse a String expression into a Node Tree which will solve
 * each expression for a given input a, b, c, and d values.
 *
 * Bringing it all together, for each combination of digits:
 *  * Generate all permutations of those digits.
 *  * Run all of those permutations through all expressions, storing all results.
 *  * Determine the max consecutive value.
 *  * If the max consecutive value is higher than the previously found max consecutive value, record the new max and the
 *    corresponding digits.
 */
public class PE0093 implements Problem {
    private static final List<Character> ARITHMETIC_OPERATIONS = List.of('+', '-', '*', '/');
    private static final List<Integer> DIGITS = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
    private static final String NO_PAREN_PERMUTATION = "a^b^c^d";

    // All combinations of using parens. Form is which of the 4 numbers to put the parens around, where '^' is an
    // arithmetic operation. i.e. (a^b)^c^d with only + would be (a+b)+c+d.
    private static final List<String> PAREN_PERMUTATIONS = List.of(
            "(a^b)^c^d",
            "a^(b^c)^d",
            "a^b^(c^d)",
            "(a^b)^(c^d)",
            "(a^b^c)^d",
            "a^(b^c^d)",
            "((a^b)^c)^d",
            "(a^(b^c))^d",
            "a^((b^c)^d)",
            "a^(b^(c^d))",
            NO_PAREN_PERMUTATION
    );

    @Override
    public ProblemSolution solve() {
        CombinationUtil<Integer> combinationUtil = new CombinationUtil<>();
        PermutationUtil<Integer> permutationUtil = new PermutationUtil<>();

        Set<Expression> expressions = getAllExpressions();
        List<List<Integer>> digitCombinations = combinationUtil.getAllCombinationsWithoutRepeats(DIGITS, 4);

        int bestMaxConsecutiveNumber = 0;
        List<Integer> bestDigits = null;
        for (List<Integer> digitCombination : digitCombinations) {
            int maxConsecutiveNumber = getMaxConsecutiveNumberForDigits(digitCombination, expressions, permutationUtil);
            if (maxConsecutiveNumber > bestMaxConsecutiveNumber) {
                bestMaxConsecutiveNumber = maxConsecutiveNumber;
                bestDigits = digitCombination;
            }
        }

        int solution = 0;
        for (int digit : bestDigits) {
            solution *= 10;
            solution += digit;
        }

        return ProblemSolution.builder()
                .solution(solution)
                .descriptiveSolution("The digits which result in the best max consecutive number are: " + bestDigits +
                        " with a max of " + bestMaxConsecutiveNumber)
                .build();
    }

    private Set<Expression> getAllExpressions() {
        List<String> strResult = new LinkedList<>();

        List<List<Character>> arithmeticPermutations = getArithmeticPermutations();
        for (List<Character> arithmeticPermutation : arithmeticPermutations) {
            if (noParensNeeded(arithmeticPermutation)) {
                strResult.add(createArithmeticExpression(NO_PAREN_PERMUTATION, arithmeticPermutation));
            } else {
                for (String parenPermutation : PAREN_PERMUTATIONS) {
                    strResult.add(createArithmeticExpression(parenPermutation, arithmeticPermutation));
                }
            }
        }

        // A lot of the other parens cases are unnecessary, and will always produce the same result as another expression
        // without those parens. Due to how we create the Expression object, the same object structure is created for
        // many of those cases. We can rely on equality checks to remove a number of these.
        return strResult.stream()
                .map(Expression::new)
                .collect(Collectors.toSet());
    }

    private List<List<Character>> getArithmeticPermutations() {
        List<List<Character>> result = new LinkedList<>();
        for (char first : ARITHMETIC_OPERATIONS) {
            for (char second : ARITHMETIC_OPERATIONS) {
                for (char third : ARITHMETIC_OPERATIONS) {
                    result.add(List.of(first, second, third));
                }
            }
        }
        return result;
    }

    /**
     * If the permutation is only + and - or only * and /, then adding parens will not make any difference.
     */
    private boolean noParensNeeded(List<Character> arithmeticPermutation) {
        return arithmeticPermutation.stream().allMatch(c -> c == '+' || c == '-') ||
                arithmeticPermutation.stream().allMatch(c -> c == '*' || c == '/');
    }

    private String createArithmeticExpression(String parenPermutation, List<Character> arithmeticPermutation) {
        StringBuilder sb = new StringBuilder();
        String[] expressionParts = parenPermutation.split("\\^");
        if (expressionParts.length != 4) {
            throw new IllegalArgumentException("Expecting parenPermutation to split into 4 pieces: " + parenPermutation);
        }

        for (int i = 0; i < 3; i++) {
            sb.append(expressionParts[i]);
            sb.append(arithmeticPermutation.get(i));
        }
        sb.append(expressionParts[3]);
        return sb.toString();
    }

    private int getMaxConsecutiveNumberForDigits(List<Integer> digits, Set<Expression> expressions,
                                                 PermutationUtil<Integer> permutationUtil) {
        Set<Integer> expressionResults = new HashSet<>();

        List<List<Integer>> digitPermutations = permutationUtil.getAllPermutations(digits);
        for (List<Integer> digitPermutation : digitPermutations) {
            for (Expression expression : expressions) {
                int expressionResult = expression.solveFor(digitPermutation.get(0), digitPermutation.get(1),
                        digitPermutation.get(2), digitPermutation.get(3));
                expressionResults.add(expressionResult);
            }
        }

        List<Integer> sortedPositives = expressionResults.stream()
                .filter(v -> v > 0)
                .sorted()
                .collect(Collectors.toList());

        int lastElement = 0;
        for (int sortedElement : sortedPositives) {
            if (sortedElement - 1 != lastElement) {
                // Isn't consecutive any longer. Return the last consecutive value we saw.
                return lastElement;
            }
            lastElement = sortedElement;
        }

        // Likely unreachable, but if we get here all of the elements are consecutive.
        return lastElement;
    }

    @Data
    private static final class Expression {
        // Can be used for debugging purposes.
        @EqualsAndHashCode.Exclude
        private final String strRep;

        // These are wrapper objects so we can modify them while solving, without touching the Node structure.
        private final IntWrapper a;
        private final IntWrapper b;
        private final IntWrapper c;
        private final IntWrapper d;

        private Node root;

        public Expression(String expression) {
            this.strRep = expression;
            this.a = new IntWrapper();
            this.b = new IntWrapper();
            this.c = new IntWrapper();
            this.d = new IntWrapper();
            this.root = parseString(expression);
        }

        private Node parseString(String expression) {
            // Can strip out the parens when they fully encompass the expression.
            if (isWrappedInParens(expression)) {
                return parseString(expression.substring(1, expression.length() - 1));
            }

            // Single character only happens when we have a literal.
            if (expression.length() == 1) {
                return buildLiteralNode(expression);
            }

            Node multiplyNode = buildNodeIfTargetFound(expression, '*', (lhs, rhs) -> lhs * rhs);
            if (multiplyNode != null) {
                return multiplyNode;
            }

            Node divideNode = buildNodeIfTargetFound(expression, '/', (lhs, rhs) -> lhs / rhs);
            if (divideNode != null) {
                return divideNode;
            }

            Node addNode = buildNodeIfTargetFound(expression, '+', (lhs, rhs) -> lhs + rhs);
            if (addNode != null) {
                return addNode;
            }

            Node subtractNode = buildNodeIfTargetFound(expression, '-', (lhs, rhs) -> lhs - rhs);
            if (subtractNode != null) {
                return subtractNode;
            }

            throw new IllegalArgumentException("Invalid expression found: " + expression);
        }

        private boolean isWrappedInParens(String expression) {
            if (!expression.startsWith("(")) {
                return false;
            }

            int openParens = 1;
            for (int i = 1; i < expression.length() - 1; i++) {
                char charAtIndex = expression.charAt(i);

                if (charAtIndex == '(') {
                    openParens++;
                } else if (charAtIndex == ')') {
                    openParens--;
                }

                // Since we aren't looking at the last character, if we fully close our initial parens within the loop,
                // then the expression is not fully wrapped by parens.
                if (openParens == 0) {
                    return false;
                }
            }

            // openParens is still 1, so the last char must be closeParens.
            return true;
        }

        private Node buildLiteralNode(String expression) {
            if (expression.equals("a")) {
                return new LiteralNode('a', this.a);
            } else if (expression.equals("b")) {
                return new LiteralNode('b', this.b);
            } else if (expression.equals("c")) {
                return new LiteralNode('c', this.c);
            } else if (expression.equals("d")){
                return new LiteralNode('d', this.d);
            } else {
                throw new IllegalArgumentException("Invalid Literal value: " + expression);
            }
        }

        private Node buildNodeIfTargetFound(String expression, char target, BinaryOperator<Double> operationFunction) {
            OperationParts parts = splitWithoutUnpairingParens(expression, target);
            if (parts != null) {
                return OperationNode.builder()
                        .lhs(parseString(parts.getLhs()))
                        .rhs(parseString(parts.getRhs()))
                        .function(operationFunction)
                        .build();
            }
            return null;
        }

        private OperationParts splitWithoutUnpairingParens(String expression, char target) {
            int openParens = 0;
            for (int i = 0; i < expression.length(); i++) {
                char charAtIndex = expression.charAt(i);

                if (charAtIndex == '(') {
                    openParens++;
                } else if (charAtIndex == ')') {
                    openParens--;
                } else if (charAtIndex == target && openParens == 0) {
                    return new OperationParts(expression.substring(0, i), expression.substring(i + 1));
                }
            }
            return null;
        }

        public int solveFor(int a, int b, int c, int d) {
            this.a.setVal(a);
            this.b.setVal(b);
            this.c.setVal(c);
            this.d.setVal(d);

            try {
                double doubleSolution = root.solve();
                // If not an integer, return -1. All negatives are dropped later.
                return isIntegerVal(doubleSolution) ? (int) doubleSolution : -1;
            } catch (ArithmeticException e) {
                // Some invalid case has happened. Probably division by 0. Return a number which will be thrown away.
                return -1;
            }
        }

        private boolean isIntegerVal(double val) {
            return (val % 1) == 0;
        }

        @Value
        @Builder
        private static final class OperationNode implements Node {
            private final Node lhs;
            private final Node rhs;
            private final BinaryOperator<Double> function;

            @Override
            public double solve() {
                double lhsResult = lhs.solve();
                double rhsResult = rhs.solve();
                return function.apply(lhsResult, rhsResult);
            }
        }

        @Value
        private static final class LiteralNode implements Node {
            private final char charRep;
            private final IntWrapper val;

            @Override
            public double solve() {
                return val.getVal();
            }
        }

        private interface Node {
            double solve();
        }

        @Data
        private static class IntWrapper {
            private int val;
        }

        @Value
        private static class OperationParts {
            private final String lhs;
            private final String rhs;
        }
    }
}
