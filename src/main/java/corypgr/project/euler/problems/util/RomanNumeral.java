package corypgr.project.euler.problems.util;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.util.List;

/**
 * Contains a Roman Numeral representation of a number.
 */
@Value
@Builder(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class RomanNumeral {
    private static final List<NumeralDetails> NUMERAL_DETAILS_ORDERED = List.of(
            new NumeralDetails("M", 1000, false),
            new NumeralDetails("CM", 900, true),
            new NumeralDetails("D", 500, true),
            new NumeralDetails("CD", 400, true),
            new NumeralDetails("C", 100, false),
            new NumeralDetails("XC", 90, true),
            new NumeralDetails("L", 50, true),
            new NumeralDetails("XL", 40, true),
            new NumeralDetails("X", 10, false),
            new NumeralDetails("IX", 9, true),
            new NumeralDetails("V", 5, true),
            new NumeralDetails("IV", 4, true),
            new NumeralDetails("I", 1, false));

    private String originalString;
    private String minimalString;
    private long longVal;

    public static RomanNumeral parseString(String val) {
        if (val == null || val.isEmpty()) {
            throw new IllegalArgumentException("Input must be non-null and non-empty.");
        }

        long longVal = parseNumeral(val);

        return RomanNumeral.builder()
                .originalString(val)
                .minimalString(getMinimalForm(longVal))
                .longVal(longVal)
                .build();
    }

    public static RomanNumeral fromLong(long val) {
        if (val <= 0) {
            throw new IllegalArgumentException("Input must be a positive number.");
        }

        return RomanNumeral.builder()
                .originalString(null)
                .minimalString(getMinimalForm(val))
                .longVal(val)
                .build();
    }

    private static long parseNumeral(String val) {
        char[] strChars = val.toCharArray();
        int strCharsCurIndex = 0;

        long result = 0;
        for (NumeralDetails detail : NUMERAL_DETAILS_ORDERED) {
            if (detail.isAppearsOnlyOnce() && nextCharsMatch(strChars, strCharsCurIndex, detail.getChars())) {
                strCharsCurIndex += detail.getChars().length;
                result += detail.getVal();
            } else {
                while (nextCharsMatch(strChars, strCharsCurIndex, detail.getChars())) {
                    strCharsCurIndex += detail.getChars().length;
                    result += detail.getVal();
                }
            }
        }

        // Means we either have non-numeral characters, numerals are not in descending order, or a numeral which should
        // only appear once has appeared multiple times.
        if (strCharsCurIndex < strChars.length) {
            throw new IllegalArgumentException("Invalid Roman Numeral passed.");
        }

        return result;
    }

    private static boolean nextCharsMatch(char[] outer, int outerBeginIndex, char[] inner) {
        // Check if there is enough space left in the string for our inner chars.
        if (outerBeginIndex + inner.length > outer.length) {
            return false;
        }

        for (int i = 0; i < inner.length; i++) {
            if (outer[outerBeginIndex + i] != inner[i]) {
                return false;
            }
        }

        return true;
    }

    private static String getMinimalForm(long val) {
        long remaining = val;
        StringBuilder sb = new StringBuilder();

        // Don't need to worry about the 'onlyAppearsOnce' rules here. That is automatically taken care of by
        // subtracting off larger values before smaller values. i.e. 'V' would appear twice in 10 or 9, but only if we
        // did not already subtract 'X' or 'IX'.
        for (NumeralDetails detail : NUMERAL_DETAILS_ORDERED) {
            while (detail.getVal() <= remaining) {
                sb.append(detail.getChars());
                remaining -= detail.getVal();
            }
        }

        return sb.toString();
    }

    @Value
    @RequiredArgsConstructor
    private static final class NumeralDetails {
        private final char[] chars;
        private final int val;

        // Means this Numeral can only appear a single time in a String representation of the Numeral.
        private final boolean appearsOnlyOnce;

        public NumeralDetails(String strRep, int val, boolean appearsOnlyOnce) {
            this(strRep.toCharArray(), val, appearsOnlyOnce);
        }
    }
}
