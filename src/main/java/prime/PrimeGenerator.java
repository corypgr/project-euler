package prime;

import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Utilities for generating prime numbers.
 *
 * Uses the sieve of Eratosthenes algorithm to generate prime numbers. I have
 * some modifications to the basic algorithm though, to improve generation
 * time a bit. Basically, this modifies the list of known prime numbers
 * continuously and reuses the list when generating non-prime numbers.
 */
public class PrimeGenerator {
    public Set<Long> generatePrimesSet(long maxVal) {
        Iterable<Long> primes = generatePrimes(maxVal);

        Set<Long> primeSet = new LinkedHashSet<>();
        for (Long prime : primes) {
            primeSet.add(prime);
        }
        return primeSet;
    }

    public List<Long> generatePrimesList(long maxVal) {
        LinkedHashSetWithModifiableIteration<Long> primes = generatePrimes(maxVal);

        List<Long> primeList = new ArrayList<>(primes.size());
        for (Long prime : primes) {
            primeList.add(prime);
        }
        return primeList;
    }

    private LinkedHashSetWithModifiableIteration<Long> generatePrimes(long maxVal) {
        LinkedHashSetWithModifiableIteration<Long> primes = new LinkedHashSetWithModifiableIteration<>();

        // Add all numbers up to the maxVal starting with 2, the first (and best) prime number.
        for (long i = 2; i <= maxVal; i++) {
            primes.add(i);
        }

        // Remove from the Primes list all multiples of the currently seen prime value.
        // Here is where the modifications affect the values we actually see. For
        // example, even though 4 was added above, we won't process 4 at this level because
        // it will be removed.
        for (Long prime : primes) {

            // The innerIterator here is determining what values to multiply our prime
            // against to produce the products we remove.
            long lastProduct = -1;
            Set<Long> valuesToRemove = new HashSet<>();
            for (Iterator<Long> innerIterator = primes.iteratorStartingWith(prime); innerIterator.hasNext() && lastProduct < maxVal; ) {
                Long multiplier = innerIterator.next();

                lastProduct = prime * multiplier;
                valuesToRemove.add(lastProduct);
            }
            primes.removeAll(valuesToRemove);
        }
        return primes;
    }

    /**
     *  I would rather use LinkedHashSet here, but you can't iterate through
     *  the LinkedHashSet and modify it at the same time. So, we roll our
     *  own version to get around that. It isn't good practice to return a
     *  modifiable Iterator for the general case, but it's self-contained here.
     *
     *  Doesn't implement Set<T> since we don't need all of the Set
     *  functionality and this is only used here.
     */
    private static class LinkedHashSetWithModifiableIteration<T> implements Iterable<T> {
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

        public int size() {
            return map.size();
        }

        public boolean isEmpty() {
            return map.isEmpty();
        }

        public boolean contains(T val) {
            return map.containsKey(val);
        }

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

        public boolean remove(T val) {
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

        public void removeAll(Collection<T> values) {
            values.forEach(this::remove);
        }

        @Data
        private static class LinkedListNode<T> {
            private final T val;
            private LinkedListNode<T> next;
            private LinkedListNode<T> previous;
        }
    }
}
