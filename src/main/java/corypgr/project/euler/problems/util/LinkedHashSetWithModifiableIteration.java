package corypgr.project.euler.problems.util;

import lombok.Data;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * I would rather use LinkedHashSet here, but you can't iterate through
 * the LinkedHashSet and modify it at the same time. So, we roll our
 * own version to get around that.
 */
public class LinkedHashSetWithModifiableIteration<T> extends AbstractSet<T> implements Set<T>, Iterable<T> {
    private final Map<T, LinkedListNode<T>> map;

    // Dummies let us not worry about edge cases so much. Our actual nodes
    // will always have previous and next values.
    private final LinkedListNode<T> dummyHead;
    private final LinkedListNode<T> dummyTail;


    public LinkedHashSetWithModifiableIteration() {
        this.dummyHead = new LinkedListNode<>(null);
        this.dummyTail = new LinkedListNode<>(null);
        this.dummyHead.setNext(dummyTail);
        this.dummyTail.setPrevious(dummyHead);

        this.map = new HashMap<>();
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean contains(Object o) {
        return map.containsKey(o);
    }

    @Override
    public Iterator<T> iterator() {
        return iteratorStartingWith(dummyHead.getNext().getVal());
    }

    public Iterator<T> iteratorStartingWith(T val) {
        // Default gives us an empty iterator, unless it is modified before next() is called.
        LinkedListNode<T> startingNode = val == null ? dummyTail : map.get(val);

        return new Iterator<T>() {
            // Set to previous because the iterator has to call next() to
            // get an element.
            private LinkedListNode<T> currentNode = startingNode.getPrevious();

            @Override
            public boolean hasNext() {
                return currentNode.getNext() != dummyTail;
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new IndexOutOfBoundsException("No more elements!");
                }

                currentNode = currentNode.getNext();
                return currentNode.getVal();
            }
        };
    }

    public boolean add(T val) {
        if (this.contains(val)) {
            // Already present. Don't try to add again.
            return false;
        }

        // Link the new node at the end.
        LinkedListNode<T> newEnd = new LinkedListNode<>(val);
        LinkedListNode<T> curEnd = dummyTail.getPrevious();
        curEnd.setNext(newEnd);
        newEnd.setPrevious(curEnd);
        newEnd.setNext(dummyTail);
        dummyTail.setPrevious(newEnd);

        map.put(val, newEnd);
        return true;
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public boolean remove(Object val) {
        if (!this.contains(val)) {
            // Not present. Can't remove.
            return false;
        }

        // Unlink the node.
        LinkedListNode<T> removeNode = map.get(val);
        LinkedListNode<T> previousNode = removeNode.getPrevious();
        LinkedListNode<T> nextNode = removeNode.getNext();
        previousNode.setNext(nextNode);
        nextNode.setPrevious(previousNode);

        return map.remove(val) != null;
    }

    @Override
    public boolean removeAll(Collection<?> values) {
        Objects.requireNonNull(values);
        Set<Boolean> removeResults = values.stream()
                .map(this::remove)
                .collect(Collectors.toSet());
        return !removeResults.contains(false);
    }

    @Override
    public boolean retainAll(Collection<?> values) {
        Objects.requireNonNull(values);
        Set<T> valuesToRemove = new HashSet<>();
        for (T key : this) {
            if (!values.contains(key)) {
                valuesToRemove.add(key);
            }
        }
        return this.removeAll(valuesToRemove);
    }

    @Data
    private static class LinkedListNode<T> {
        private final T val;
        private LinkedListNode<T> next;
        private LinkedListNode<T> previous;
    }
}
