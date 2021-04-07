package corypgr.project.euler.problems.util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Set;

class PermutationUtilTest {
    private PermutationUtil<Character> util;

    @BeforeEach
    void setup() {
        util = new PermutationUtil<>();
    }

    @Test
    void nullInputReturnsEmpty() {
        assertThat(util.getAllPermutations(null), is(empty()));
    }

    @Test
    void emptyInputReturnsEmpty() {
        assertThat(util.getAllPermutations(Collections.emptyList()), is(empty()));
    }

    @Test
    void singletonInputReturnsSingleton() {
        assertEquals(Collections.singletonList(Collections.singletonList('a')),
                util.getAllPermutations(Collections.singletonList('a')));
    }

    @Test
    void normalInputReturnsExpected() {
        List<Character> input = List.of('a', 'b', 'c');
        Set<List<Character>> expectedOutput = Set.of(
                List.of('a', 'b', 'c'),
                List.of('a', 'c', 'b'),
                List.of('b', 'a', 'c'),
                List.of('b', 'c', 'a'),
                List.of('c', 'a', 'b'),
                List.of('c', 'b', 'a'));

        assertThat(util.getAllPermutations(input), containsInAnyOrder(expectedOutput.toArray()));
    }
}
