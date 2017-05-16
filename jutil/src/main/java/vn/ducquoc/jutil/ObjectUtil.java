package vn.ducquoc.jutil;

/**
 * Helper class for Operations on Object, handling <i>null</i> input gracefully.
 * 
 * @author ducquoc
 * @see org.springframework.util.ObjectUtils
 * @see org.apache.commons.lang3.ObjectUtils
 */
public class ObjectUtil {

    public static Integer getInt(Object obj) {
        if (Number.class.isInstance(obj)) { // isAssignableFrom(obj.getClass())
            return ((Number) obj).intValue();
        }
        if (obj instanceof String) {
            return Integer.valueOf((String) obj);
        }
        return (Integer) obj;
    }

    public static Long getLong(Object obj) {
        if (Number.class.isInstance(obj)) { // isAssignableFrom(obj.getClass())
            return ((Number) obj).longValue();
        }
        if (obj instanceof String) {
            return Long.valueOf((String) obj);
        }
        return (Long) obj;
    }

    public static boolean equals(Object object1, Object object2) {
        if (object1 == object2) {
            return true;
        }
        if ((object1 == null) || (object2 == null)) {
            return false;
        }
        return object1.equals(object2);
    }

  public static int hashMurmur3(int x) {
    x ^= x >>> 16;
    x *= 0x85ebca6b;
    x ^= x >>> 13;
    x *= 0xc2b2ae35;
    x ^= x >>> 16;
    return x;
  }

  public final static long murmurHash3(long x) {
    x ^= x >>> 33;
    x *= 0xff51afd7ed558ccdL;
    x ^= x >>> 33;
    x *= 0xc4ceb9fe1a85ec53L;
    x ^= x >>> 33;
    return x;
  }

    //
    // Java 7+ methods (Eclipse/Maven may require JDK instead of JRE)
    //
    public static boolean equalsJ7(Object obj1, Object obj2) {
      return java.util.Objects.equals(obj1, obj2);
    }

    public static boolean equalsJ7Deep(Object obj1, Object obj2) {
      return java.util.Objects.deepEquals(obj1, obj2);
    }

}
