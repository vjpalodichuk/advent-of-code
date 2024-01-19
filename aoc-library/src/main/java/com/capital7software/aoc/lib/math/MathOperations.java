package com.capital7software.aoc.lib.math;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
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
        T result;

        if (first instanceof Integer a && second instanceof Integer b) {
            result = (T) ((Integer) (a + b));
        } else if (first instanceof Long a && second instanceof Long b) {
            result = (T) ((Long) (a + b));
        } else if (first instanceof Double a && second instanceof Double b) {
            result = (T) ((Double) (a + b));
        } else if (first instanceof Float a && second instanceof Float b) {
            result = (T) ((Float) (a + b));
        } else if (first instanceof BigInteger a && second instanceof BigInteger b) {
            result = (T) a.add(b);
        } else if (first instanceof BigDecimal a && second instanceof BigDecimal b) {
            result = (T) a.add(b);
        } else if (first instanceof AtomicInteger a && second instanceof AtomicInteger b) {
            result = (T) (new AtomicInteger(a.get() + b.get()));
        } else if (first instanceof AtomicLong a && second instanceof AtomicLong b) {
            result = (T) (new AtomicLong(a.get() + b.get()));
        } else {
            throw new IllegalArgumentException("first and second are of an unsupported type!");
        }

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

    /**
     * Returns the absolute value of the specified value.
     *
     * @param value The value to get the absolute value of.
     * @param <T>   The type of the parameters. See the class comments on
     *              supported types.
     * @return The absolute value of the specified value.
     */
    @SuppressFBWarnings
    public static <T extends Number & Comparable<T>> @NotNull T abs(@NotNull T value) {
        T result;

        if (value instanceof Integer a) {
            result = (T) ((Integer) (a < 0 ? -a : a));
        } else if (value instanceof Long a) {
            result = (T) ((Long) (a < 0 ? -a : a));
        } else if (value instanceof Double a) {
            result = (T) ((Double) (Math.abs(a)));
        } else if (value instanceof Float a) {
            result = (T) ((Float) (Math.abs(a)));
        } else if (value instanceof BigInteger a) {
            result = (T) a.abs();
        } else if (value instanceof BigDecimal a) {
            result = (T) a.abs();
        } else if (value instanceof AtomicInteger a) {
            result = (T) (new AtomicInteger(Math.abs(a.get())));
        } else if (value instanceof AtomicLong a) {
            result = (T) (new AtomicLong(Math.abs(a.get())));
        } else {
            throw new IllegalArgumentException("value is of an unsupported type!");
        }

        return result;
    }

    /**
     * Calculates and returns the Greatest Common Divisor of two generics of the same type and
     * returns the result.<br>
     * The general formula is: <br><br>
     * if (first == 0)
     * return second
     * else
     * gcd(second mod first, first)<br><br>
     * <b>Please note that for BigDecimal the result may be negative.</b>
     *
     * @param first  The first Number.
     * @param second The second Number.
     * @param <T>    The type of the parameters. See the class comments on
     *               supported types.
     * @return The Greatest Common Divisor.
     * @throws IllegalArgumentException If first and second are not of a supported type.
     */
    public static <T extends Number & Comparable<T>> @NotNull T gcd(@NotNull T first, @NotNull T second) {
        if (first instanceof Integer a && second instanceof Integer) {
            if (a == 0) {
                return second;
            }
            return gcd(mod(second, first), first);
        } else if (first instanceof Long a && second instanceof Long) {
            if (a == 0L) {
                return second;
            }
            return gcd(mod(second, first), first);
        } else if (first instanceof Double a && second instanceof Double) {
            if (a == 0) {
                return second;
            }
            return gcd(mod(second, first), first);
        } else if (first instanceof Float a && second instanceof Float) {
            if (a == 0) {
                return second;
            }
            return gcd(mod(second, first), first);
        } else if (first instanceof BigInteger a && second instanceof BigInteger b) {
            return (T) a.gcd(b);
        } else if (first instanceof BigDecimal a && second instanceof BigDecimal) {
            if (a.equals(BigDecimal.ZERO)) {
                return second;
            }
            return gcd(mod(second, first), first);
        } else if (first instanceof AtomicInteger a && second instanceof AtomicInteger) {
            if (a.get() == 0) {
                return second;
            }
            return gcd(mod(second, first), first);
        } else if (first instanceof AtomicLong a && second instanceof AtomicLong) {
            if (a.get() == 0L) {
                return second;
            }
            return gcd(mod(second, first), first);
        } else {
            throw new IllegalArgumentException("first and second are of an unsupported type!");
        }
    }

    /**
     * Calculates and returns the Least Common Multiple of two generics of the same type and
     * returns the result.<br>
     * The general formula is: <br><br>
     * (first * second) / gcd(first, second)<br><br>
     * <b>Please note that for BigDecimal the result may be negative.</b>
     *
     * @param first  The first Number.
     * @param second The second Number.
     * @param <T>    The type of the parameters. See the class comments on
     *               supported types.
     * @return The Least Common Multiple.
     * @throws IllegalArgumentException If first and second are not of a supported type.
     */
    public static <T extends Number & Comparable<T>> @NotNull T lcm(@NotNull T first, @NotNull T second) {
        return divide(multiply(first, second), gcd(first, second));
    }
}
