package vn.ducquoc.jutil;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for Math operations.
 * 
 * @author ducquoc
 * @see org.apache.commons.math.util.MathUtils
 * @see com.google.common.MathUtil
 */
public class MathUtil {

  public static long sum1ToN(long number) {
    return number * (number - 1) / 2;
  }

  public static long sumMultiples(long divisor, long upperBound) {
    return divisor * sum1ToN(upperBound / divisor);
  }

  public static boolean isPrime(long number) {
    if (number < 2)
      return false;

    double sqrtNumber = Math.sqrt((double) number);
    for (long i = 2; i <= sqrtNumber; i++) {
      if (number % i == 0) {
        return false;
      }
    }
    return true;
  }

  public static List<Long> primeFactors(long number) {
    List<Long> factors = new ArrayList<Long>();
    for (long i = 2; i <= number; i++) {
      while (number % i == 0) {
        factors.add(i);
        number = number / i;
      }
    }
    return factors;
  }

  public static long greatestCommonDivisor(long a, long b) {
    if (b == 0)
      return a;
    return greatestCommonDivisor(b, a % b);
  }

  public static long lowestCommonMultiple(long a, long b) {
    return a * b / greatestCommonDivisor(a, b);
  }

  public static long phiTotient(long number) {
    long result = number;
    for (int i = 2; i * i <= number; ++i) {
      if (number % i == 0) {
        result -= result / i;
      }
      while (number % i == 0)
        number /= i;
    }
    if (number > 1) {
      result -= result / number;
    }
    return result;
  }

}
