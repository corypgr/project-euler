package corypgr.project.euler.problems.util;

import java.util.HashMap;
import java.util.Map;

public class DivisionUtil {
    /**
     * Returns the decimal representation of a fraction where the repeating decimal values are wrapped by
     * "(" and ")". Ex:
     *  * 1/2 = "0.5"
     *  * 1/3 = "0.(3)"
     *  * 1/4 = "0.25"
     *  * 1/5 = "0.2"
     *  * 1/6 = "0.1(6)"
     */
    public String divideWithRepeatingDecimalRepresentation(int numerator, int denominator) {
        if (denominator == 0) {
            throw new IllegalArgumentException("0 cannot be passed as the denominator");
        }

        StringBuilder sb = new StringBuilder();
        int integerPart = numerator / denominator;
        sb.append(integerPart);

        int remainder = numerator % denominator;
        if (remainder == 0) {
            return sb.toString();
        }
        sb.append(".");

        Map<Integer, Integer> remainderToIndex = new HashMap<>();
        int insertIndex = 2; // Index of next location to append the character.
        while (remainder > 0 && !remainderToIndex.containsKey(remainder)) {
            remainderToIndex.put(remainder, insertIndex);
            int newNumerator = remainder * 10;
            int decimalPart = newNumerator / denominator;
            sb.append(decimalPart);

            remainder = newNumerator % denominator;
            insertIndex++;
        }

        if (remainder != 0) {
            sb.insert(remainderToIndex.get(remainder), "(");
            sb.append(")");
        }
        return sb.toString();
    }
}
