package com.capital7software.aoc.lib.math;

import com.capital7software.aoc.lib.util.Triple;
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
  public static <T extends Number> @NotNull T add(
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
  public static <T extends Number> @NotNull T subtract(
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
  public static <T extends Number> @NotNull T multiply(
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
  public static <T extends Number> @NotNull T divide(
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
   * Floor Modulo two generics of the same type and return the result.<br>
   * <pre><code>(first % second + second) % second</code></pre>
   * Is how the operation is performed for Integer and Longs (including Atomic).<br>
   * <pre><code>first % second</code></pre>
   * Is how the operation is performed for Double and Floats.<br><br>
   * <b>Please note that for BigDecimal the result is the remainder.</b><br>
   * <b>Please note that for BigInteger the result is from a call to BigInteger.mod
   * (which performs a floor mod).</b>
   *
   * @param first  The first Number.
   * @param second The second Number.
   * @param <T>    The type of the parameters. See the class comments on
   *               supported types.
   * @return The result of the modulo.
   * @throws IllegalArgumentException If first and second are not of a supported type.
   */
  public static <T extends Number> @NotNull T mod(
      @NotNull T first,
      @NotNull T second
  ) {
    return switch (first) {
      case Integer a when second instanceof Integer b -> (T) ((Integer) ((a % b + b) % b));
      case Long a when second instanceof Long b -> (T) ((Long) ((a % b + b) % b));
      case Double a when second instanceof Double b -> (T) ((Double) (a % b));
      case Float a when second instanceof Float b -> (T) ((Float) (a % b));
      case BigInteger a when second instanceof BigInteger b -> (T) a.mod(b);
      case BigDecimal a when second instanceof BigDecimal b -> (T) a.remainder(b);
      case AtomicInteger a when second instanceof AtomicInteger b ->
          (T) (new AtomicInteger((a.get() % b.get() + b.get()) % b.get()));
      case AtomicLong a when second instanceof AtomicLong b ->
          (T) (new AtomicLong((a.get() % b.get() + b.get()) % b.get()));
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
  public static <T extends Number> @NotNull T abs(@NotNull T value) {
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
   * Returns the specified value incremented by one (1).
   *
   * @param value The value to increment by one (1).
   * @param <T>   The type of the parameters. See the class comments on
   *              supported types.
   * @return The specified value incremented by one (1).
   */
  public static <T extends Number> @NotNull T inc(@NotNull T value) {
    return switch (value) {
      case Integer a -> (T) ((Integer) (a + 1));
      case Long a -> (T) ((Long) (a + 1L));
      case Double a -> (T) ((Double) (a + 1.0));
      case Float a -> (T) ((Float) (a + 1f));
      case BigInteger a -> (T) a.add(BigInteger.ONE);
      case BigDecimal a -> (T) a.add(BigDecimal.ONE);
      case AtomicInteger a -> (T) (new AtomicInteger(a.get() + 1));
      case AtomicLong a -> (T) (new AtomicLong(a.get() + 1L));
      default -> throw new IllegalArgumentException("value is of an unsupported type!");
    };
  }

  /**
   * Returns the specified value decremented by one (1).
   *
   * @param value The value to decrement by one (1).
   * @param <T>   The type of the parameters. See the class comments on
   *              supported types.
   * @return The specified value decremented by one (1).
   */
  public static <T extends Number> @NotNull T dec(@NotNull T value) {
    return switch (value) {
      case Integer a -> (T) ((Integer) (a - 1));
      case Long a -> (T) ((Long) (a - 1L));
      case Double a -> (T) ((Double) (a - 1.0));
      case Float a -> (T) ((Float) (a - 1f));
      case BigInteger a -> (T) a.subtract(BigInteger.ONE);
      case BigDecimal a -> (T) a.subtract(BigDecimal.ONE);
      case AtomicInteger a -> (T) (new AtomicInteger(a.get() - 1));
      case AtomicLong a -> (T) (new AtomicLong(a.get() - 1L));
      default -> throw new IllegalArgumentException("value is of an unsupported type!");
    };
  }

  /**
   * Calculates and returns the Greatest Common Divisor of two generics of the same type and
   * returns the result.<br><br>
   * The general formula is: <br><br>
   * <pre><code>
   * if (second == 0) {
   *    return abs(first)
   * } else {
   *    return gcd(second, first floor-mod second)
   * }
   * </code></pre>
   *
   * @param first  The first Number.
   * @param second The second Number.
   * @param <T>    The type of the parameters. See the class comments on
   *               supported types.
   * @return The Greatest Common Divisor.
   * @throws IllegalArgumentException If first and second are not of a supported type.
   */
  public static <T extends Number> @NotNull T gcd(
      @NotNull T first,
      @NotNull T second
  ) {
    switch (first) {
      case Integer ignored when second instanceof Integer b -> {
        if (b == 0) {
          return abs(first);
        }
        return gcd(second, mod(first, second));
      }
      case Long ignored when second instanceof Long b -> {
        if (b == 0L) {
          return abs(first);
        }
        return gcd(second, mod(first, second));
      }
      case Double ignored when second instanceof Double b -> {
        if (b == 0) {
          return abs(first);
        }
        return gcd(second, mod(first, second));
      }
      case Float ignored when second instanceof Float b -> {
        if (b == 0) {
          return abs(first);
        }
        return gcd(second, mod(first, second));
      }
      case BigInteger a when second instanceof BigInteger b -> {
        return (T) a.gcd(b);
      }
      case BigDecimal ignored when second instanceof BigDecimal b -> {
        if (b.equals(BigDecimal.ZERO)) {
          return abs(first);
        }
        return gcd(second, mod(first, second));
      }
      case AtomicInteger ignored when second instanceof AtomicInteger b -> {
        if (b.get() == 0) {
          return abs(first);
        }
        return gcd(second, mod(first, second));
      }
      case AtomicLong ignored when second instanceof AtomicLong b -> {
        if (b.get() == 0L) {
          return abs(first);
        }
        return gcd(second, mod(first, second));
      }
      default -> throw new IllegalArgumentException("first and second are of an unsupported type!");
    }
  }

  /**
   * Calculates and returns the Greatest Common Divisor of two generics of the same type and
   * returns the result; which includes s and t. This uses the Extended Euclidean Algorithm<br><br>
   * The general formula is: <br><br>
   * <pre><code>
   * if (second == 0) {
   *    return Triple(abs(first), s, t)
   * } else {
   *    return egcd(second, first floor-mod second)
   * }
   * </code></pre>
   *
   * <p><br>
   * The first element of the returned Triple is the GCD, the second element is s and the
   * third element is t such that: s * first + t * second = gcd(first, second)
   *
   * <p><br>
   * Please note that s and t are only accurate if the types of the parameters are Integer or Long
   * types. This does include AtomicInteger, AtomicLong, and BigInteger.
   *
   * <p><br>
   * For Float, Double, and BigDecimal, the GCD will be accurate but s and t will not be correct.
   *
   * @param first  The first Number.
   * @param second The second Number.
   * @param <T>    The type of the parameters. See the class comments on
   *               supported types.
   * @return The Greatest Common Divisor.
   * @throws IllegalArgumentException If first and second are not of a supported type.
   */
  public static <T extends Number> @NotNull Triple<T, T, T> egcd(
      @NotNull T first,
      @NotNull T second
  ) {
    switch (first) {
      case Integer ignored when second instanceof Integer b -> {
        return b == 0 ? new Triple<>(abs(first), (T) ((Integer) 1), (T) ((Integer) 0)) : egcd(
            first,
            second,
            (T) ((Integer) 1),
            (T) ((Integer) 0),
            (T) ((Integer) 0),
            (T) ((Integer) 1)
        );
      }
      case Long ignored when second instanceof Long b -> {
        return b == 0 ? new Triple<>(abs(first), (T) ((Long) 1L), (T) ((Long) 0L)) : egcd(
            first, second, (T) ((Long) 1L), (T) ((Long) 0L), (T) ((Long) 0L), (T) ((Long) 1L)
        );
      }
      case Double ignored when second instanceof Double b -> {
        return b == 0 ? new Triple<>(abs(first), (T) ((Double) 1.0), (T) ((Double) 0.0)) : egcd(
            first,
            second,
            (T) ((Double) 1.0),
            (T) ((Double) 0.0),
            (T) ((Double) 1.0),
            (T) ((Double) 0.0)
        );
      }
      case Float ignored when second instanceof Float b -> {
        return b == 0 ? new Triple<>(abs(first), (T) ((Float) 1f), (T) ((Float) 0f)) : egcd(
            first, second, (T) ((Float) 1f), (T) ((Float) 0f), (T) ((Float) 1f), (T) ((Float) 0f)
        );
      }
      case BigInteger ignored when second instanceof BigInteger b -> {
        return b.equals(BigInteger.ZERO) || b.equals(BigInteger.valueOf(0))
            ? new Triple<>(abs(first), (T) BigInteger.ONE, (T) BigInteger.ZERO)
            : egcd(
            first,
            second,
            (T) BigInteger.ONE,
            (T) BigInteger.ZERO,
            (T) BigInteger.ZERO,
            (T) BigInteger.ONE
        );
      }
      case BigDecimal ignored when second instanceof BigDecimal b -> {
        return b.equals(BigDecimal.ZERO) || b.equals(BigDecimal.valueOf(0.0))
            ? new Triple<>(abs(first), (T) BigDecimal.ONE, (T) BigDecimal.ZERO)
            : egcd(
            first,
            second,
            (T) BigDecimal.ONE,
            (T) BigDecimal.ZERO,
            (T) BigDecimal.ZERO,
            (T) BigDecimal.ONE
        );
      }
      case AtomicInteger ignored when second instanceof AtomicInteger b -> {
        return b.get() == 0
            ? new Triple<>(
            abs(first), (T) (new AtomicInteger(1)), (T) (new AtomicInteger(0))
        )
            : egcd(
            first,
            second,
            (T) (new AtomicInteger(1)),
            (T) (new AtomicInteger(0)),
            (T) (new AtomicInteger(0)),
            (T) (new AtomicInteger(1))
        );
      }
      case AtomicLong ignored when second instanceof AtomicLong b -> {
        return b.get() == 0
            ? new Triple<>(
            abs(first), (T) (new AtomicLong(1)), (T) (new AtomicLong(0))
        )
            : egcd(
            first,
            second,
            (T) (new AtomicLong(1)),
            (T) (new AtomicLong(0)),
            (T) (new AtomicLong(0)),
            (T) (new AtomicLong(1))
        );
      }
      default -> throw new IllegalArgumentException("first and second are of an unsupported type!");
    }
  }

  private static <T extends Number> @NotNull Triple<T, T, T> egcd(
      @NotNull T first,
      @NotNull T second,
      @NotNull T s1,
      @NotNull T s2,
      @NotNull T t1,
      @NotNull T t2
  ) {
    T q = divide(first, second);
    T r = mod(first, second);
    T s3 = subtract(s1, multiply(q, s2));
    T t3 = subtract(t1, multiply(q, t2));

    switch (r) {
      case Integer remainder -> {
        if (remainder == 0) {
          return new Triple<>(abs(second), s2, t2);
        }
        return egcd(second, r, s2, s3, t2, t3);
      }
      case Long remainder -> {
        if (remainder == 0) {
          return new Triple<>(abs(second), s2, t2);
        }
        return egcd(second, r, s2, s3, t2, t3);
      }
      case Double remainder -> {
        if (remainder == 0) {
          return new Triple<>(abs(second), s2, t2);
        }
        return egcd(second, r, s2, s3, t2, t3);
      }
      case Float remainder -> {
        if (remainder == 0) {
          return new Triple<>(abs(second), s2, t2);
        }
        return egcd(second, r, s2, s3, t2, t3);
      }
      case BigInteger remainder -> {
        if (remainder.equals(BigInteger.ZERO) || remainder.equals(BigInteger.valueOf(0))) {
          return new Triple<>(abs(second), s2, t2);
        }
        return egcd(second, r, s2, s3, t2, t3);
      }
      case BigDecimal remainder -> {
        if (remainder.equals(BigDecimal.ZERO) || remainder.equals(BigDecimal.valueOf(0.0))) {
          return new Triple<>(abs(second), s2, t2);
        }
        return egcd(second, r, s2, s3, t2, t3);
      }
      case AtomicInteger remainder -> {
        if (remainder.get() == 0) {
          return new Triple<>(abs(second), s2, t2);
        }
        return egcd(second, r, s2, s3, t2, t3);
      }
      case AtomicLong remainder -> {
        if (remainder.get() == 0L) {
          return new Triple<>(abs(second), s2, t2);
        }
        return egcd(second, r, s2, s3, t2, t3);
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
  public static <T extends Number> @NotNull T lcm(
      @NotNull T first,
      @NotNull T second
  ) {
    return divide(multiply(first, second), gcd(first, second));
  }

  /**
   * Returns the Multiplicative Inverse of b. In Zn, two numbers a and b are multiplicative
   * inverses of each other if:
   * <pre><code>a × b ≡ 1 (mod n).</code></pre>
   * <b>Important:</b> Not every integer has a multiplicative inverse!
   * Only if gcd(b, n) == 1<br>
   *
   * <p><br>
   * The returned value can be checked by applying this formula:
   * <pre><code>
   *   i * b (mod n) = 1 (mod n)
   * </code></pre>
   * Where i is the Multiplicative Inverse.<br>
   *
   * <p><br>
   * Example:<br><br>
   * Find the modular multiplicative inverse of 11 in ℤ26.
   * <pre><code>
   *   i = 19
   *   b = 11
   *   n = 26
   *   19 * 11 (mod 26) = 1 (mod 26)
   *   209 (mod 26) = 1 (mod 26)
   *   1 = 1
   * </code></pre>
   *
   * @param b The value to get the Multiplicative Inverse of.
   * @param n The size of the set of numbers that b is from.
   * @param <T>   The type of the parameters. See the class comments on
   *              supported types.
   * @return The absolute value of the specified value.
   */
  @SuppressWarnings("unused")
  public static <T extends Number> @NotNull T inv(
      @NotNull T b,
      @NotNull T n
  ) {
    return mod(MathOperations.egcd(n, b).third(), n);
  }
}
