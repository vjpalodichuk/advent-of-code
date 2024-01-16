package com.capital7software.aoc.lib.math;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * A Utility class for performing basic arithmetic operations on generic types
 * that are Numbers.
 * <br><br>
 * Supported types are: Integer, Long, Double, Float, BigInteger, BigDecimal,
 * AtomicInteger, and AtomicLong.
 * <br>
 * Please note that any other types will result in an IllegalArgumentException
 * being thrown from the methods.
 */
@SuppressWarnings("unchecked")
public class MathOperations {
    private MathOperations() {

    }

    /**
     * Add two generics of the same type and return the result.
     *
     * @param first  The first Number.
     * @param second The second Number.
     * @param <T>    The type of the parameters. See the class comments on
     *               supported types.
     * @return The result of the addition.
     * @throws IllegalArgumentException If first and second are not of a supported type.
     */
    public static <T extends Number & Comparable<T>> @NotNull T add(@NotNull T first, @NotNull T second) {
        T result = switch (first) {
            case Integer a when second instanceof Integer b -> (T) ((Integer) (a + b));
            case Long a when second instanceof Long b -> (T) ((Long) (a + b));
            case Double a when second instanceof Double b -> (T) ((Double) (a + b));
            case Float a when second instanceof Float b -> (T) ((Float) (a + b));
            case BigInteger a when second instanceof BigInteger b -> (T) a.add(b);
            case BigDecimal a when second instanceof BigDecimal b -> (T) a.add(b);
            case AtomicInteger a when second instanceof AtomicInteger b -> (T) (new AtomicInteger(a.get() + b.get()));
            case AtomicLong a when second instanceof AtomicLong b -> (T) (new AtomicLong(a.get() + b.get()));
            default -> throw new IllegalArgumentException("first and second are of an unsupported type!");
        };

        return result;
    }

    /**
     * Subtract two generics of the same type and return the result.<br>
     * first - second<br>
     * Is how the operation is performed.
     *
     * @param first  The first Number.
     * @param second The second Number.
     * @param <T>    The type of the parameters. See the class comments on
     *               supported types.
     * @return The result of the subtraction.
     * @throws IllegalArgumentException If first and second are not of a supported type.
     */
    public static <T extends Number & Comparable<T>> @NotNull T subtract(@NotNull T first, @NotNull T second) {
        T result;

        if (first instanceof Integer a && second instanceof Integer b) {
            result = (T) ((Integer) (a - b));
        } else if (first instanceof Long a && second instanceof Long b) {
            result = (T) ((Long) (a - b));
        } else if (first instanceof Double a && second instanceof Double b) {
            result = (T) ((Double) (a - b));
        } else if (first instanceof Float a && second instanceof Float b) {
            result = (T) ((Float) (a - b));
        } else if (first instanceof BigInteger a && second instanceof BigInteger b) {
            result = (T) a.subtract(b);
        } else if (first instanceof BigDecimal a && second instanceof BigDecimal b) {
            result = (T) a.subtract(b);
        } else if (first instanceof AtomicInteger a && second instanceof AtomicInteger b) {
            result = (T) (new AtomicInteger(a.get() - b.get()));
        } else if (first instanceof AtomicLong a && second instanceof AtomicLong b) {
            result = (T) (new AtomicLong(a.get() - b.get()));
        } else {
            throw new IllegalArgumentException("first and second are of an unsupported type!");
        }

        return result;
    }

    /**
     * Multiply two generics of the same type and return the result.<br>
     * first * second<br>
     * Is how the operation is performed.
     *
     * @param first  The first Number.
     * @param second The second Number.
     * @param <T>    The type of the parameters. See the class comments on
     *               supported types.
     * @return The result of the multiplication.
     * @throws IllegalArgumentException If first and second are not of a supported type.
     */
    public static <T extends Number & Comparable<T>> @NotNull T multiply(@NotNull T first, @NotNull T second) {
        T result;

        if (first instanceof Integer a && second instanceof Integer b) {
            result = (T) ((Integer) (a * b));
        } else if (first instanceof Long a && second instanceof Long b) {
            result = (T) ((Long) (a * b));
        } else if (first instanceof Double a && second instanceof Double b) {
            result = (T) ((Double) (a * b));
        } else if (first instanceof Float a && second instanceof Float b) {
            result = (T) ((Float) (a * b));
        } else if (first instanceof BigInteger a && second instanceof BigInteger b) {
            result = (T) a.multiply(b);
        } else if (first instanceof BigDecimal a && second instanceof BigDecimal b) {
            result = (T) a.multiply(b);
        } else if (first instanceof AtomicInteger a && second instanceof AtomicInteger b) {
            result = (T) (new AtomicInteger(a.get() * b.get()));
        } else if (first instanceof AtomicLong a && second instanceof AtomicLong b) {
            result = (T) (new AtomicLong(a.get() * b.get()));
        } else {
            throw new IllegalArgumentException("first and second are of an unsupported type!");
        }

        return result;
    }

    /**
     * Divide two generics of the same type and return the result.<br>
     * first / second<br>
     * Is how the operation is performed.
     *
     * @param first  The first Number.
     * @param second The second Number.
     * @param <T>    The type of the parameters. See the class comments on
     *               supported types.
     * @return The result of the division.
     * @throws IllegalArgumentException If first and second are not of a supported type.
     */
    public static <T extends Number & Comparable<T>> @NotNull T divide(@NotNull T first, @NotNull T second) {
        T result;

        if (first instanceof Integer a && second instanceof Integer b) {
            result = (T) ((Integer) (a / b));
        } else if (first instanceof Long a && second instanceof Long b) {
            result = (T) ((Long) (a / b));
        } else if (first instanceof Double a && second instanceof Double b) {
            result = (T) ((Double) (a / b));
        } else if (first instanceof Float a && second instanceof Float b) {
            result = (T) ((Float) (a / b));
        } else if (first instanceof BigInteger a && second instanceof BigInteger b) {
            result = (T) a.divide(b);
        } else if (first instanceof BigDecimal a && second instanceof BigDecimal b) {
            result = (T) a.divide(b, RoundingMode.HALF_UP);
        } else if (first instanceof AtomicInteger a && second instanceof AtomicInteger b) {
            result = (T) (new AtomicInteger(a.get() / b.get()));
        } else if (first instanceof AtomicLong a && second instanceof AtomicLong b) {
            result = (T) (new AtomicLong(a.get() / b.get()));
        } else {
            throw new IllegalArgumentException("first and second are of an unsupported type!");
        }

        return result;
    }

    /**
     * Modulo two generics of the same type and return the result.<br>
     * first % second<br>
     * Is how the operation is performed.<br><br>
     * <b>Please note that for BigDecimal the result may be negative.</b>
     *
     * @param first  The first Number.
     * @param second The second Number.
     * @param <T>    The type of the parameters. See the class comments on
     *               supported types.
     * @return The result of the modulo.
     * @throws IllegalArgumentException If first and second are not of a supported type.
     */
    public static <T extends Number & Comparable<T>> @NotNull T mod(@NotNull T first, @NotNull T second) {
        T result;

        if (first instanceof Integer a && second instanceof Integer b) {
            result = (T) ((Integer) (a % b));
        } else if (first instanceof Long a && second instanceof Long b) {
            result = (T) ((Long) (a % b));
        } else if (first instanceof Double a && second instanceof Double b) {
            result = (T) ((Double) (a % b));
        } else if (first instanceof Float a && second instanceof Float b) {
            result = (T) ((Float) (a % b));
        } else if (first instanceof BigInteger a && second instanceof BigInteger b) {
            result = (T) a.mod(b);
        } else if (first instanceof BigDecimal a && second instanceof BigDecimal b) {
            result = (T) a.remainder(b);
        } else if (first instanceof AtomicInteger a && second instanceof AtomicInteger b) {
            result = (T) (new AtomicInteger(a.get() % b.get()));
        } else if (first instanceof AtomicLong a && second instanceof AtomicLong b) {
            result = (T) (new AtomicLong(a.get() % b.get()));
        } else {
            throw new IllegalArgumentException("first and second are of an unsupported type!");
        }

        return result;
    }

}