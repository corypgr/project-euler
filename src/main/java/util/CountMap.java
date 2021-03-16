package util;

import lombok.experimental.Delegate;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility for tracking the number of occurrences of something.
 */
public class CountMap<T> implements Map<T, Integer> {
    @Delegate
    private final Map<T, Integer> map;

    public CountMap() {
        map = new HashMap<>();
    }

    public Integer addCount(T val) {
        Integer curCount = map.getOrDefault(val, 0);
        map.put(val, curCount + 1);
        return curCount + 1;
    }

    public Integer subtractCount(T val) {
        if (!map.containsKey(val)) {
            return 0;
        } else if (map.get(val) == 1) {
            map.remove(val);
            return 0;
        } else {
            Integer curCount = map.get(val);
            map.put(val, curCount - 1);
            return curCount - 1;
        }
    }
}
