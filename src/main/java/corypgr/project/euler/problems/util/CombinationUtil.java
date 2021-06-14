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
            currentIndices = getNextWithRepeatsIndices(currentIndices, maxIndex);
        }
        return result;
    }

    /**
     * Returns all combinations of values with the specified number of elements. Unlike permutations, a combination
     * is unordered. The resulting List is sorted in ascending order based on the order of passed in values.
     */
    public List<List<T>> getAllCombinationsWithoutRepeats(List<T> values, int numElements) {
        if (values == null || values.size() == 0 || numElements <= 0 || values.size() < numElements) {
            return Collections.emptyList();
        }

        List<Integer> currentIndices = new ArrayList<>(numElements);
        for (int i = 0; i < numElements; i++) {
            currentIndices.add(i); // Since no repeats, fill with different indices.
        }

        List<List<T>> result = new LinkedList<>();
        int maxIndex = values.size() - 1;
        while (currentIndices != null) {
            result.add(convertIndices(currentIndices, values));
            currentIndices = getNextWithoutRepeatsIndices(currentIndices, maxIndex);
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
    private List<Integer> getNextWithRepeatsIndices(List<Integer> currentIndices, int maxIndex) {
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

    /**
     * Basically tick up last index until it gets to maxIndex, then "roll over" to the next index. Since there are no
     * repeats, we fill the elements after the rollover with incrementing values.
     * Example with 9 as the maxIndex:
     *   012
     *   013 - 019 ? Increment the last element.
     *   023       ? Roll over the the 1 digit, and increment remaining.
     *   023 - 029
     *   034       ? Same rollover idea.
     *   035 - 089
     *   123
     *   123 - 789
     */
    private List<Integer> getNextWithoutRepeatsIndices(List<Integer> currentIndices, int maxIndex) {
        for (int i = currentIndices.size() - 1, remaining = 0; i >= 0; i--, remaining++) {
            if (currentIndices.get(i) < maxIndex - remaining) {
                int newIndexStart = currentIndices.get(i) + 1;

                // Fill all elements from incremented spot with increasing values.
                for (int j = 0; j + i < currentIndices.size(); j++) {
                    currentIndices.set(j + i, newIndexStart + j);
                }
                return currentIndices;
            }
        }

        // Max values with no repeats have already been hit.
        return null;
    }
}
