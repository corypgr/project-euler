package corypgr.project.euler.problems.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class CombinationUtil<T> {
    /**
     * Returns all combinations of values with the specified number of elements. Unlike permutations, a combination
     * is unordered. The resulting List is sorted in ascending order based on the order of passed in values.
     */
    public List<List<T>> getAllCombinationsWithRepeats(List<T> values, int numElements) {
        if (values == null || values.size() == 0 || numElements <= 0) {
            return Collections.emptyList();
        }

        List<Integer> currentIndices = new ArrayList<>(numElements);
        for (int i = 0; i < numElements; i++) {
            currentIndices.add(0); // fill with 0s to start.
        }

        List<List<T>> result = new LinkedList<>();
        int maxIndex = values.size() - 1;
        while (currentIndices != null) {
            result.add(convertIndices(currentIndices, values));
            currentIndices = getNextIndices(currentIndices, maxIndex);
        }
        return result;
    }

    private List<T> convertIndices(List<Integer> indices, List<T> values) {
        return indices.stream()
                .map(values::get)
                .collect(Collectors.toList());
    }

    /**
     * Basically tick up last index until it gets to maxIndex, then "roll over" to the next index.
     * Example with 9 as the maxIndex:
     *   000
     *   001 - 009 ? Increment the last element.
     *   011       ? Skip 0 in the last digit. 010 is equal to 001.
     *   012 - 019
     *   022       ? Same skip idea. 021 and 020 are covered earlier.
     *   023 - 099
     *   111
     *   112 - 999
     */
    private List<Integer> getNextIndices(List<Integer> currentIndices, int maxIndex) {
        for (int i = currentIndices.size() - 1; i >= 0; i--) {
            if (currentIndices.get(i) < maxIndex) {
                int newIndexVal = currentIndices.get(i) + 1;

                // Fill all elements from incremented spot with the same value.
                for (int j = i; j < currentIndices.size(); j++) {
                    currentIndices.set(j, newIndexVal);
                }
                return currentIndices;
            }
        }

        // All spots are set to the max already.
        return null;
    }
}
