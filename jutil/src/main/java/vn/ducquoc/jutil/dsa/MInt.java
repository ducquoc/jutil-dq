package vn.ducquoc.jutil.dsa;

/**
 * Medium integer. (not luxury as java.math.BigInteger)
 * <p>
 * Some people are rich and can spend big. Some others can barely afford medium.
 * </p>
 * (actually just simple, temp sub for BInt - created in 2018 Mar)
 * 
 * @see http://gimbo.org.uk/texts/ten_thousand_factorial.txt
 * @see projecteuler #495, ...
 */
public class MInt {

  private static final long mod = 100000000L;

  private final int[] digits;
  private final int digitsLength;

  public static final MInt ZERO = new MInt(0);
  public static final MInt ONE = new MInt(1L);

  public MInt(long value) {
    digits = new int[] { (int) value, (int) (value >>> 32) };
    digitsLength = 2;
  }

  private MInt(int[] digits, int length) {
    this.digits = digits;
    digitsLength = length;
  }

  /**
   * Multiply (3x7=21)
   */
  public MInt x(MInt b) {
    int aLen = this.digitsLength, bLen = b.digitsLength;
    int cLen = aLen + bLen;
    int[] digit = new int[cLen];

    for (int i = 0; i < aLen; i++) {
      long temp = 0;
      for (int j = 0; j < bLen; j++) {
        temp = temp + ((long) this.digits[i]) * ((long) b.digits[j])
            + digit[i + j];
        digit[i + j] = (int) (temp % mod);
        temp = temp / mod;
      }
      digit[i + bLen] = (int) temp;
    }

    int k = cLen - 1;
    while (digit[k] == 0) {
      k--;
    }

    return new MInt(digit, k + 1);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder(digitsLength * 10);
    sb = sb.append(digits[digitsLength - 1]);
    for (int j = digitsLength - 2; j >= 0; j--) {
      sb = sb.append(Integer.toString(digits[j] + (int) mod).substring(1));
    }
    return sb.toString();
  }

  //
  // helpers
  //
  public static MInt pow2(Number n) {
    int e = n.intValue();
    if (e < 31) {
      return new MInt((int) Math.pow(2, e));
    }
    return pow2(e / 2).x(pow2(e - e / 2));
  }

  public static String factorial(Number n) {
    long e = n.longValue();
    MInt result = fastFactorial(e);

    return result.toString();
  }

  private static long factorialHolder;

  public static MInt fastFactorial(Number n) {// fast enough n<10000
    long e = n.longValue();
    if (e < 0) {
      throw new ArithmeticException("Should be >= 0, but " + e);
    }

    if (e < 2) {
      return new MInt(1);
    }

    MInt p = new MInt(1);
    MInt r = new MInt(1);

    long h = 0, shift = 0, high = 1;
    int log2n = (int) Math.floor(Math.log(e) / Math.log(2));

    factorialHolder = 1;
    while (h != e) {
      shift += h;
      h = e >> log2n--;
      long len = high;
      high = (h & 1) == 1 ? h : h - 1;
      len = (high - len) / 2;

      if (len > 0) {
        p = p.x(product(len));
        r = r.x(p);
      }
    }

    r = r.x(MInt.pow2(shift));
    return r;
  }

  public static MInt product(Number num) {
    int n = num.intValue();
    int m = n / 2;
    if (m == 0) {
      return new MInt(factorialHolder += 2);
    }
    if (n == 2) {
      return new MInt((factorialHolder += 2) * (factorialHolder += 2));
    }
    return product(n - m).x(product(m));
  }

  //public String toHex() { return new java.math.BigInteger(toString()).toString(16);}

}
