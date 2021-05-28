package corypgr.project.euler.problems.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

class RomanNumeralTest {
    @ParameterizedTest
    @MethodSource("invalidStringInputs")
    void parseString_invalidInputs_throws(String input) {
        assertThrows(IllegalArgumentException.class, () -> RomanNumeral.parseString(input));
    }

    static Stream<String> invalidStringInputs() {
        return Stream.of(
                null,
                "",
                " MDC", // no spaces allowed.
                "mDC", // numeral values must be all uppercase.
                "M/C", // non-numeral characters not allowed.

                // Numerals must be in strictly descending order.
                // The following all break that requirement.
                "CMM", "DM", "CDM", "XCM", "LM", "XLM", "XM", "IXM", "VM", "IVM", "IM",
                "DCM", "CDCM", "CCM", "XCCM", "LCM", "XLCM", "XCM", "IXCM", "VCM", "IVCM", "ICM",
                "CDD", "XCD", "LD", "XLD", "XD", "IXD", "VD", "IVD", "ID",
                "CCD", "XCCD", "LCD", "XLCD", "XCD", "IXCD", "VCD", "IVCD", "ICD",
                "XCC", "LC", "XLC", "IXC", "VC", "IVC", "IC",
                "LXC", "XLXC", "XXC", "IXC", "VXC", "IVXC", "IXC",
                "XLL", "IXL", "VL", "IVL", "IL",
                "XXL", "IXXL", "VXL", "IVXL", "IXL",
                "IXX", "VX", "IVX",
                "VIX", "IVIX", "IIX",
                "IVV",
                "IIV",

                // Many numerals are only allowed to appear a single time.
                "CMCM",
                "DD",
                "CDCD",
                "XCXC",
                "LL",
                "XLXL",
                "IXIX",
                "VV",
                "IVIV");
    }

    @ParameterizedTest
    @MethodSource("parseStringValidInputs")
    void parseString_validInputs_expectedResult(RomanNumeral expected) {
        RomanNumeral numeral = RomanNumeral.parseString(expected.getOriginalString());
        assertEquals(expected, numeral);
    }

    @ParameterizedTest
    @ValueSource(longs = {0, -1, -10})
    void fromLong_invalidInputs_throws(long input) {
        assertThrows(IllegalArgumentException.class, () -> RomanNumeral.fromLong(input));
    }

    @ParameterizedTest
    @MethodSource("fromLongValidInputs")
    void fromLong_validInputs_expectedResult(RomanNumeral expected) {
        RomanNumeral numeral = RomanNumeral.fromLong(expected.getLongVal());
        assertEquals(expected, numeral);
    }

    static Stream<RomanNumeral> parseStringValidInputs() {
        return validInputs()
                .map(RomanNumeral.RomanNumeralBuilder::build);
    }

    static Stream<RomanNumeral> fromLongValidInputs() {
        return validInputs()
                .map(builder -> builder.originalString(null))
                .map(RomanNumeral.RomanNumeralBuilder::build);
    }

    private static Stream<RomanNumeral.RomanNumeralBuilder> validInputs() {
        return Stream.of(
                // Simplest conversions
                RomanNumeral.builder().longVal(1).originalString("I").minimalString("I"),
                RomanNumeral.builder().longVal(4).originalString("IV").minimalString("IV"),
                RomanNumeral.builder().longVal(5).originalString("V").minimalString("V"),
                RomanNumeral.builder().longVal(9).originalString("IX").minimalString("IX"),
                RomanNumeral.builder().longVal(10).originalString("X").minimalString("X"),
                RomanNumeral.builder().longVal(40).originalString("XL").minimalString("XL"),
                RomanNumeral.builder().longVal(50).originalString("L").minimalString("L"),
                RomanNumeral.builder().longVal(90).originalString("XC").minimalString("XC"),
                RomanNumeral.builder().longVal(100).originalString("C").minimalString("C"),
                RomanNumeral.builder().longVal(400).originalString("CD").minimalString("CD"),
                RomanNumeral.builder().longVal(500).originalString("D").minimalString("D"),
                RomanNumeral.builder().longVal(900).originalString("CM").minimalString("CM"),
                RomanNumeral.builder().longVal(1000).originalString("M").minimalString("M"),

                // Non-minimal simple cases
                RomanNumeral.builder().longVal(1).originalString("I").minimalString("I"),
                RomanNumeral.builder().longVal(4).originalString("IIII").minimalString("IV"),
                RomanNumeral.builder().longVal(5).originalString("IIIII").minimalString("V"),
                RomanNumeral.builder().longVal(9).originalString("VIIII").minimalString("IX"),
                RomanNumeral.builder().longVal(10).originalString("VIIIII").minimalString("X"),
                RomanNumeral.builder().longVal(40).originalString("XXXX").minimalString("XL"),
                RomanNumeral.builder().longVal(50).originalString("XXXXX").minimalString("L"),
                RomanNumeral.builder().longVal(90).originalString("LXXXX").minimalString("XC"),
                RomanNumeral.builder().longVal(100).originalString("LXXXXX").minimalString("C"),
                RomanNumeral.builder().longVal(400).originalString("CCCC").minimalString("CD"),
                RomanNumeral.builder().longVal(500).originalString("CCCCC").minimalString("D"),
                RomanNumeral.builder().longVal(900).originalString("DCCCC").minimalString("CM"),
                RomanNumeral.builder().longVal(1000).originalString("DCCCCC").minimalString("M"),

                // Large non-minimal case
                RomanNumeral.builder().longVal(2111).originalString("MDCCCCCLXXXXXVIIIIII").minimalString("MMCXI"));
    }
}
