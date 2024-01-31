package com.capital7software.aoc.lib.math;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import org.jetbrains.annotations.NotNull;

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
  public static <T extends Number & Comparable<T>> @NotNull T add(
      @NotNull T first,
      @NotNull T second
  ) {
    return switch (first) {
      case Integer a when second instanceof Integer b -> (T) ((Integer) (a + b));
      case Long a when second instanceof Long b -> (T) ((Long) (a + b));
      case Double a when second instanceof Double b -> (T) ((Double) (a + b));
      case Float a when second instanceof Float b -> (T) ((Float) (a + b));
      case BigInteger a when second instanceof BigInteger b -> (T) a.add(b);
      case BigDecimal a when second instanceof BigDecimal b -> (T) a.add(b);
      case AtomicInteger a when second instanceof AtomicInteger b ->
          (T) (new AtomicInteger(a.get() + b.get()));
      case AtomicLong a when second instanceof AtomicLong b ->
          (T) (new AtomicLong(a.get() + b.get()));
      default -> throw new IllegalArgumentException("first and second are of an unsupported type!");
    };
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
  public static <T extends Number & Comparable<T>> @NotNull T subtract(
      @NotNull T first,
      @NotNull T second
  ) {
    return switch (first) {
      case Integer a when second instanceof Integer b -> (T) ((Integer) (a - b));
      case Long a when second instanceof Long b -> (T) ((Long) (a - b));
      case Double a when second instanceof Double b -> (T) ((Double) (a - b));
      case Float a when second instanceof Float b -> (T) ((Float) (a - b));
      case BigInteger a when second instanceof BigInteger b -> (T) a.subtract(b);
      case BigDecimal a when second instanceof BigDecimal b -> (T) a.subtract(b);
      case AtomicInteger a when second instanceof AtomicInteger b ->
          (T) (new AtomicInteger(a.get() - b.get()));
      case AtomicLong a when second instanceof AtomicLong b ->
          (T) (new AtomicLong(a.get() - b.get()));
      default -> throw new IllegalArgumentException("first and second are of an unsupported type!");
    };
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
  public static <T extends Number & Comparable<T>> @NotNull T multiply(
      @NotNull T first,
      @NotNull T second
  ) {
    return switch (first) {
      case Integer a when second instanceof Integer b -> (T) ((Integer) (a * b));
      case Long a when second instanceof Long b -> (T) ((Long) (a * b));
      case Double a when second instanceof Double b -> (T) ((Double) (a * b));
      case Float a when second instanceof Float b -> (T) ((Float) (a * b));
      case BigInteger a when second instanceof BigInteger b -> (T) a.multiply(b);
      case BigDecimal a when second instanceof BigDecimal b -> (T) a.multiply(b);
      case AtomicInteger a when second instanceof AtomicInteger b ->
          (T) (new AtomicInteger(a.get() * b.get()));
      case AtomicLong a when second instanceof AtomicLong b ->
          (T) (new AtomicLong(a.get() * b.get()));
      default -> throw new IllegalArgumentException("first and second are of an unsupported type!");
    };
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
  public static <T extends Number & Comparable<T>> @NotNull T divide(
      @NotNull T first,
      @NotNull T second
  ) {
    return switch (first) {
      case Integer a when second instanceof Integer b -> (T) ((Integer) (a / b));
      case Long a when second instanceof Long b -> (T) ((Long) (a / b));
      case Double a when second instanceof Double b -> (T) ((Double) (a / b));
      case Float a when second instanceof Float b -> (T) ((Float) (a / b));
      case BigInteger a when second instanceof BigInteger b -> (T) a.divide(b);
      case BigDecimal a when second instanceof BigDecimal b ->
          (T) a.divide(b, RoundingMode.HALF_UP);
      case AtomicInteger a when second instanceof AtomicInteger b ->
          (T) (new AtomicInteger(a.get() / b.get()));
      case AtomicLong a when second instanceof AtomicLong b ->
          (T) (new AtomicLong(a.get() / b.get()));
      default -> throw new IllegalArgumentException("first and second are of an unsupported type!");
    };
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
  public static <T extends Number & Comparable<T>> @NotNull T mod(
      @NotNull T first,
      @NotNull T second
  ) {
    return switch (first) {
      case Integer a when second instanceof Integer b -> (T) ((Integer) (a % b));
      case Long a when second instanceof Long b -> (T) ((Long) (a % b));
      case Double a when second instanceof Double b -> (T) ((Double) (a % b));
      case Float a when second instanceof Float b -> (T) ((Float) (a % b));
      case BigInteger a when second instanceof BigInteger b -> (T) a.mod(b);
      case BigDecimal a when second instanceof BigDecimal b -> (T) a.remainder(b);
      case AtomicInteger a when second instanceof AtomicInteger b ->
          (T) (new AtomicInteger(a.get() % b.get()));
      case AtomicLong a when second instanceof AtomicLong b ->
          (T) (new AtomicLong(a.get() % b.get()));
      default -> throw new IllegalArgumentException("first and second are of an unsupported type!");
    };
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
    return switch (value) {
      case Integer a -> (T) ((Integer) (a < 0 ? -a : a));
      case Long a -> (T) ((Long) (a < 0 ? -a : a));
      case Double a -> (T) ((Double) (Math.abs(a)));
      case Float a -> (T) ((Float) (Math.abs(a)));
      case BigInteger a -> (T) a.abs();
      case BigDecimal a -> (T) a.abs();
      case AtomicInteger a -> (T) (new AtomicInteger(Math.abs(a.get())));
      case AtomicLong a -> (T) (new AtomicLong(Math.abs(a.get())));
      default -> throw new IllegalArgumentException("value is of an unsupported type!");
    };
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
  public static <T extends Number & Comparable<T>> @NotNull T gcd(
      @NotNull T first,
      @NotNull T second
  ) {
    switch (first) {
      case Integer a when second instanceof Integer -> {
        if (a == 0) {
          return second;
        }
        return gcd(mod(second, first), first);
      }
      case Long a when second instanceof Long -> {
        if (a == 0L) {
          return second;
        }
        return gcd(mod(second, first), first);
      }
      case Double a when second instanceof Double -> {
        if (a == 0) {
          return second;
        }
        return gcd(mod(second, first), first);
      }
      case Float a when second instanceof Float -> {
        if (a == 0) {
          return second;
        }
        return gcd(mod(second, first), first);
      }
      case BigInteger a when second instanceof BigInteger b -> {
        return (T) a.gcd(b);
      }
      case BigDecimal a when second instanceof BigDecimal -> {
        if (a.equals(BigDecimal.ZERO)) {
          return second;
        }
        return gcd(mod(second, first), first);
      }
      case AtomicInteger a when second instanceof AtomicInteger -> {
        if (a.get() == 0) {
          return second;
        }
        return gcd(mod(second, first), first);
      }
      case AtomicLong a when second instanceof AtomicLong -> {
        if (a.get() == 0L) {
          return second;
        }
        return gcd(mod(second, first), first);
      }
      default -> throw new IllegalArgumentException("first and second are of an unsupported type!");
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
  public static <T extends Number & Comparable<T>> @NotNull T lcm(
      @NotNull T first,
      @NotNull T second
  ) {
    return divide(multiply(first, second), gcd(first, second));
  }
}
