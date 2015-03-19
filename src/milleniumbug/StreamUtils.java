package milleniumbug;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.DoubleSupplier;
import java.util.function.IntSupplier;
import java.util.function.Supplier;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class StreamUtils {

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

    public static void main(String[] args) {
        System.out.println(Arrays.equals(cycle(new int[]{1, 2, 3, 4, 5}).limit(8).toArray(), new int[]{1, 2, 3, 4, 5, 1, 2, 3}) ? "PASS" : "FAIL");
        System.out.println(Arrays.equals(cycle(new int[]{1}).limit(3).toArray(), new int[]{1, 1, 1}) ? "PASS" : "FAIL");
        System.out.println(Arrays.equals(cycle(new double[]{1, 2, 3, 4, 5}).limit(8).toArray(), new double[]{1, 2, 3, 4, 5, 1, 2, 3}) ? "PASS" : "FAIL");
        System.out.println(Arrays.equals(cycle(new double[]{1}).limit(3).toArray(), new double[]{1, 1, 1}) ? "PASS" : "FAIL");
        System.out.println(Arrays.equals(cycle(new double[]{1, 2, 3, 4, 5}).limit(8).toArray(), new double[]{1, 2, 3, 4, 5, 1, 2, 3}) ? "PASS" : "FAIL");
        System.out.println(Arrays.equals(cycle(new double[]{1}).limit(3).toArray(), new double[]{1, 1, 1}) ? "PASS" : "FAIL");
        try
        {
            cycle(new int[0]);
            System.out.println("FAIL");
        } catch(IllegalArgumentException ex)
        {
            System.out.println("PASS");
        }
    }
}
