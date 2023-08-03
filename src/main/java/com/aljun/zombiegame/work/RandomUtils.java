package com.aljun.zombiegame.work;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomUtils {

    public static final Random RANDOM = new Random();

    public static boolean nextBoolean(double chance) {
        return RANDOM.nextDouble(0d, 1d) <= chance;
    }

    public static double nextDouble(double k, double b) {
        double x = RANDOM.nextDouble(0d, 1d);
        return b * (1 - x) / (k * x + b);
    }

    public static class RandomHelper<T> {
        private final List<T> VALUE = new ArrayList<>();
        private final List<Double> RIGHT = new ArrayList<>();
        private double rightTotal = 0d;
        private boolean done = false;

        private RandomHelper() {
        }

        public static <E> RandomHelper<E> create() {
            return new RandomHelper<>();
        }

        public RandomHelper<T> add(T value, double right) {
            if (this.done) return this;
            if (right > 0) {
                if (VALUE.contains(value)) {
                    RIGHT.set(VALUE.indexOf(value), RIGHT.get(VALUE.indexOf(value)) + right);
                } else {
                    VALUE.add(value);
                    RIGHT.add(right);
                }
                rightTotal += right;
            } else if (right < 0) {
                throw new IndexOutOfBoundsException("\"right\" > 0d, but :" + right);
            }
            return this;
        }

        public void done() {
            this.done = true;
        }

        public T nextValue() {
            double random = RandomUtils.RANDOM.nextDouble(0d, rightTotal);
            double before = 0d;
            double after = 0d;
            int i = -1;
            for (double j : RIGHT) {
                i++;
                after += j;
                if (before <= random && random <= after) return VALUE.get(i);
                before += j;
            }
            return VALUE.get(0);
        }
    }
}
