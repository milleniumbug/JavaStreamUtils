package milleniumbug;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.DoubleSupplier;
import java.util.function.IntSupplier;
import java.util.function.Supplier;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class StreamUtils {

    public static IntStream concat(IntStream... streams) {
        return Arrays.stream(streams).reduce(IntStream::concat).orElse(IntStream.empty());
    }

    public static DoubleStream concat(DoubleStream... streams) {
        return Arrays.stream(streams).reduce(DoubleStream::concat).orElse(DoubleStream.empty());
    }

    public static <T> Stream<T> concat(Stream<? extends T>... streams) {
        return (Stream<T>)Arrays.stream(streams).reduce(Stream::concat).orElse(Stream.empty());
    }

    public static IntStream iota() {
        return iota(1);
    }

    public static IntStream iota(int startValue) {
        return IntStream.iterate(startValue, (x) -> x + 1);
    }

    public static <T> Stream<T> cycle(List<T> list) {
        if (list.isEmpty()) {
            throw new IllegalArgumentException("cycled list empty");
        }
        return Stream.generate(new Supplier<T>() {
            Iterator<T> begin = list.iterator();
            Iterator<T> i = list.iterator();

            @Override
            public T get() {
                if (!i.hasNext()) {
                    i = begin;
                }
                return i.next();
            }
        });
    }

    public static <T> Stream<T> cycle(T[] array) {
        if (array.length == 0) {
            throw new IllegalArgumentException("cycled array empty");
        }
        return Stream.generate(new Supplier<T>() {
            int i = 0;

            @Override
            public T get() {
                T value = array[i];
                i = (i + 1) % array.length;
                return value;
            }
        });
    }

    public static IntStream cycle(int[] array) {
        if (array.length == 0) {
            throw new IllegalArgumentException("cycled array empty");
        }
        return IntStream.generate(new IntSupplier() {
            int i = 0;

            @Override
            public int getAsInt() {
                int value = array[i];
                i = (i + 1) % array.length;
                return value;
            }
        });
    }

    public static DoubleStream cycle(double[] array) {
        if (array.length == 0) {
            throw new IllegalArgumentException("cycled array empty");
        }
        return DoubleStream.generate(new DoubleSupplier() {
            int i = 0;

            @Override
            public double getAsDouble() {
                double value = array[i];
                i = (i + 1) % array.length;
                return value;
            }
        });
    }

    private static class ZipSpliterator<T, U> implements Spliterator<Pair<T, U>> {

        Spliterator<? extends T> left;
        Spliterator<? extends U> right;
        int characteristic;

        public ZipSpliterator(Spliterator<? extends T> left, Spliterator<? extends U> right) {
            this.left = left;
            this.right = right;
            characteristic = 0;
            characteristic |= (left.hasCharacteristics(ORDERED) && right.hasCharacteristics(ORDERED)) ? ORDERED : 0;
            // not distinct, as pairs are not comparable (at least, not currently)
            // not sorted (see above)
            characteristic |= (left.hasCharacteristics(SIZED) && right.hasCharacteristics(SIZED)) ? SIZED : 0;
            characteristic |= NONNULL;
            characteristic |= (left.hasCharacteristics(IMMUTABLE) && right.hasCharacteristics(IMMUTABLE)) ? IMMUTABLE : 0;
            characteristic |= (left.hasCharacteristics(CONCURRENT) && right.hasCharacteristics(CONCURRENT)) ? CONCURRENT : 0;
            // subsized? dunno
        }

        @Override
        public boolean tryAdvance(Consumer<? super Pair<T, U>> cnsmr) {
            // mutable booleans
            boolean[] left_not_finished = new boolean[1], right_not_finished = new boolean[1];
            left_not_finished[0] = left.tryAdvance((x) -> {
                right_not_finished[0] = right.tryAdvance((y) -> {
                    cnsmr.accept(new Pair<>(x, y));
                });
            });
            return left_not_finished[0] && right_not_finished[0];
        }

        @Override
        public ZipSpliterator<T, U> trySplit() {
            Spliterator<? extends T> left_split = left.trySplit();
            Spliterator<? extends U> right_split = right.trySplit();
            if (Stream.of(left_split, right_split).allMatch(Objects::nonNull)) {
                return new ZipSpliterator<>(left_split, right_split);
            }
            return null;
        }

        @Override
        public long estimateSize() {
            return Math.min(left.estimateSize(), right.estimateSize());
        }

        @Override
        public int characteristics() {
            return characteristic;
        }

    }

    public static <T, U> Stream<Pair<T, U>> zip(Stream<? extends T> left, Stream<? extends U> right) {
        return StreamSupport.stream(new ZipSpliterator<T, U>(left.spliterator(), right.spliterator()), false);
    }
    
    public static <T, U, V> Stream<V> zipMap(Stream<? extends T> left, Stream<? extends U> right, BiFunction<? super T, ? super U, ? extends V> fun) {
        return zip(left, right).map(pair -> fun.apply(pair.first, pair.second));
    }
    
    public static <T> Stream<Pair<Long, T>> enumerate(Stream<? extends T> stream)
    {
        return zip(iota(0).mapToLong(Long::valueOf).boxed(), stream);
    }
}
