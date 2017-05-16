package vn.ducquoc.jutil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for Math operations.
 * 
 * @author ducquoc
 * @see org.apache.commons.math.util.MathUtils
 * @see com.google.common.MathUtil
 */
public class MathUtil {

  public static long sum1ToN(long number) {
    return number * (number + 1) / 2;
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

  public static boolean isPrime(String numberText) {
    if (numberText == null) return false;

    //Long.MAX_VALUE==649657L*92737*337*127*73*7*7;//+30
    return new java.math.BigInteger(numberText.trim()).isProbablePrime(64);
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

  /**
   * @param num
   * @return isPrime[i] indicates whether i is prime, for 0 <= i <= n.
   */
  public static boolean[] primeProbArr(Number num) {
    long n = num.longValue();
    if (n < 0)
      throw new IllegalArgumentException("Negative array size");
    boolean[] result = new boolean[(int) (n + 1)];
    if (n >= 2)
      result[2] = true;
    for (int i = 3; i <= n; i += 2)
      result[i] = true;
    // Sieve of Eratosthenes
    for (int i = 3, end = (int) Math.sqrt(n); i <= end; i += 2) {
      if (result[i]) {
        // Note: i * i does not overflow
        for (int j = i * i; j <= n; j += i << 1)
          result[j] = false;
      }
    }
    return result;
  }

  /**
   * @param n
   * @return prime numbers less than or equal to n, in ascending order.
   */
  public static int[] primeIntTo(int n) {
    boolean[] primeCheckArr = primeProbArr(n);
    int count = 0;
    for (boolean b : primeCheckArr) {
      if (b) {
        count++;
      }
    }

    int[] result = new int[count];
    for (int i = 0, j = 0; i < primeCheckArr.length; i++) {
      if (primeCheckArr[i]) {
        result[j] = i;
        j++;
      }
    }
    return result;
  }

  /**
   * @param n
   * @return prime numbers less than or equal to n, in ascending order.
   */
  public static long[] primeLongTo(long n) {
    boolean[] primeCheckArr = primeProbArr(n);
    int count = 0;
    for (boolean b : primeCheckArr) {
      if (b) {
        count++;
      }
    }

    long[] result = new long[count];
    for (int i = 0, j = 0; i < primeCheckArr.length; i++) {
      if (primeCheckArr[i]) {
        result[j] = i;
        j++;
      }
    }
    return result;
  }

  public static long[] primeSumTo(int n) {
    boolean[] primeCheckArr = primeProbArr(n);

    long[] primeSum = new long[primeCheckArr.length];
    for (int i = 2, s = primeCheckArr.length; i < s; i++) {
      primeSum[i] = primeSum[i - 1];
      if (primeCheckArr[i]) {
        primeSum[i] += i;
      }
    }
    return primeSum;
  }

  /**
   * @param primeOrder
   * @return prime numbers at the order (-th) up to primeOrder
   */
  public static int[] primeNth(Number primeOrder) {
    int n = primeOrder.intValue();
    int[] primesNth = new int [n+1];
    primesNth[0] = 1;// natural index: primesNth[1]=2;
    for (int i = 1, num=2; i <= n; num++) {
      if (isPrime(num)) {
        primesNth[i] = num;
        i++;
      }
    }
    return primesNth;
  }

  public static int countPrimeFactors(Number num, int p) {
    long n = num.longValue();
    if (n == 0)
      return 0;
    else
      return (int) (n / p + countPrimeFactors(n / p, p));
  }

  public static int countFactors(Number num) {
    int n = num.intValue();
    int factors = 0;
    for (int i = 1; i <= Math.sqrt(n); i++) {
      if (n % i == 0)
        factors += 2;
      if (i * i == n)
        factors -= 1;
    }

    return factors;
  }

  public static int factorize(Number number) {// faster with big num
    Integer n = number.intValue();
    int exponentProduct = 1;
    try {
      List<Integer> list = new ArrayList<Integer>();
      HashMap<Integer, Integer> exponentCount = new HashMap<Integer, Integer>();

      for (int i = 2; i <= n / i; i++) {
        while (n % i == 0) {
          list.add(i);
          n /= i;
        }
      }
      if (n > 1)
        list.add(n);

      //System.out.println(Arrays.toString(list.toArray()));
      for (int i = 0, s = list.size(); i < s; i++) {
        if (!exponentCount.containsKey(list.get(i))) {
          exponentCount.put(list.get(i), 1);
        } else {
          int currentCount = exponentCount.get(list.get(i));
          exponentCount.put(list.get(i), currentCount + 1);
        }
      }

      for (Map.Entry<Integer, Integer> entry : exponentCount.entrySet()) {
        exponentProduct *= entry.getValue() + 1;
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return exponentProduct;
  }

  public static String decToHex(Number num) {
    int halfByte = 0x0F;
    int sizeOfIntInHalfBytes = Integer.SIZE / 4;// 8
    int numberOfBitsInAHalfByte = sizeOfIntInHalfBytes / 2;//4
    char[] hexDigits = "0123456789ABCDEF".toCharArray();

    int dec = num.intValue();//Integer.toHexString();
    StringBuilder hexBuilder = new StringBuilder(sizeOfIntInHalfBytes);
    hexBuilder.setLength(sizeOfIntInHalfBytes);
    for (int i = sizeOfIntInHalfBytes - 1; i >= 0; --i) {
      int j = dec & halfByte;
      hexBuilder.setCharAt(i, hexDigits[j]);
      dec >>= numberOfBitsInAHalfByte;
    }
    return hexBuilder.toString();
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

  public static void main(String[] args) {long u = System.nanoTime();
    for (int i = 0; i < 400000; i++) {
      countFactors(i);//factorize(i);
    }
    System.out.printf("Elapsed: %f ms", (System.nanoTime() - u) / 1000000.0);
  }

}
