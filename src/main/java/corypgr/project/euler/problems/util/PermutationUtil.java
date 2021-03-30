package corypgr.project.euler.problems.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class PermutationUtil<T> {

    /**
     * Return all permutations of the passed in List of values.
     *
     * Uses List to avoid the equals() check when inserting into a Set. Saves a significant amount of time.
     */
    public List<List<T>> getAllPermutations(List<T> values) {
        if (values == null || values.size() == 0) {
            return Collections.emptyList();
        }
        if (values.size() == 1) {
            return Collections.singletonList(values);
        }

        T insertElement = values.get(0);
        List<List<T>> permutationsOfOtherElements = getAllPermutations(values.subList(1, values.size()));
        List<List<T>> allPermutations = new LinkedList<>();
        for (List<T> otherElementsPermutation : permutationsOfOtherElements) {
            for (int i = 0; i <= otherElementsPermutation.size(); i++) {
                List<T> permutation = new ArrayList<>(values.size());
                permutation.addAll(otherElementsPermutation.subList(0, i));
                permutation.add(insertElement);
                permutation.addAll(otherElementsPermutation.subList(i, otherElementsPermutation.size()));

                allPermutations.add(permutation);
            }
        }
        return allPermutations;
    }
}
