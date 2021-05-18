package nl.jdriven.mayhem.util;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * @see <a href="http://blog.codefx.org/java/stream-findfirst-findany-reduce/">Beware Of findFirst() And findAny()</a>
 */
public final class Streams {
    private Streams() {
        // util doesn't need public constructor
    }

    public static <T> BinaryOperator<T> toOnlyElement() {
        return toOnlyElementThrowing(IllegalArgumentException::new);
    }

    public static <T, E extends RuntimeException> BinaryOperator<T>
    toOnlyElementThrowing(Supplier<E> exception) {
        return (element, otherElement) -> {
            throw exception.get();
        };
    }

    /**
     * Collector die een fout geeft bij meer dan 1 resultaat.
     *
     * @param <T> element type voor in- en output
     * @return een {@link Optional} met geen of 1 element T
     * @throws IllegalStateException wanneer er meer dan een element wordt aangeboden aan de reduceer operatie.
     */
    @SuppressWarnings("squid:S1452")
    public static <T> Collector<T, ?, Optional<T>> zeroOrOne() {
        return Collectors.reducing(toOnlyElementThrowing(() -> new IllegalStateException("More than one value was returned")));
    }

    /**
     * Collector die een fout geeft als er niet exact 1 resultaat is.
     *
     * @param <T> element type voor in- en output
     * @return 1 object van type T
     * @throws IllegalStateException  wanneer er meer dan een element wordt aangeboden aan de reduceer operatie.
     * @throws NoSuchElementException wanneer er geen element is na de reduceer operatie.
     */
    @SuppressWarnings("squid:S1452")
    public static <T> Collector<T, ?, T> onlyOne() {
        return Collectors.collectingAndThen(zeroOrOne(), Optional::get);
    }

    /**
     * @param it
     * @param <T>
     * @return
     * @see <a href="https://kingsfleet.blogspot.com/2018/11/in-jdk-9-and-well-8-and-above.html">G's Blog</a>
     */
    public static <T> Stream<T> asStream(Iterator<T> it) {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(it, Spliterator.IMMUTABLE | Spliterator.ORDERED), false);
    }

    /**
     * @deprecated use Java 9, {@link Stream#iterate(Object, Predicate, UnaryOperator)}.
     */
    @Deprecated
    public static <T> Stream<T> iterateFinite(T seed, Predicate<? super T> hasNext, UnaryOperator<T> next) {
        return asStream(new Iterator<T>() {
            T current = seed;

            @Override
            public boolean hasNext() {
                return hasNext.test(current);
            }

            @Override
            public T next() {
                if (current == null) {
                    throw new NoSuchElementException();
                }
                try {
                    return current;
                } finally {
                    current = next.apply(current);
                }
            }
        });
    }

    /**
     * Zips the specified stream with its indices.
     *
     * @see <a href="https://stackoverflow.com/a/23051268">iterate with index</a>
     */
    public static <T> Stream<Map.Entry<Integer, T>> zipWithIndex(Stream<? extends T> stream) {
        return Streams.asStream(new Iterator<>() {
            private final Iterator<? extends T> streamIterator = stream.iterator();
            private int index = 0;

            @Override
            public boolean hasNext() {
                return streamIterator.hasNext();
            }

            @Override
            public Map.Entry<Integer, T> next() {
                return Map.entry(index++, streamIterator.next());
            }
        });
    }

    /**
     * Returns a stream consisting of the results of applying the given two-arguments function to the elements of this stream.
     * The first argument of the function is the element index and the second one - the element value.
     *
     * @see <a href="https://stackoverflow.com/a/23051268">iterate with index</a>
     */
    public static <T, R> Stream<R> mapWithIndex(Stream<? extends T> stream, BiFunction<Integer, ? super T, ? extends R> mapper) {
        return zipWithIndex(stream).map(entry -> mapper.apply(entry.getKey(), entry.getValue()));
    }
}
