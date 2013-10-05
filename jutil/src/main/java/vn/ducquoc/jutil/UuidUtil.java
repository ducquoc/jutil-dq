package vn.ducquoc.jutil;

import java.io.Serializable;
import java.util.UUID;

/**
 * Helper class for UUID operations.
 * 
 * @author ducquoc
 * @see http://tools.ietf.org/html/rfc4122
 * @see com.eaio.uuid.UUID
 * @see com.fasterxml.uuid.impl.UUIDUtil
 */
public class UuidUtil {

    public interface Uuid {
        public String toString();
    }

    public static Uuid newUuid() {
        return new UuidV4();
    }

    public static Uuid toUuid(String uuidStr) {
        if (uuidStr != null) {
            return new UuidV4(uuidStr);
        } else {
            return null;
        }
    }

    public static String toHexString(Uuid uuid) {
        return (new UuidV4(uuid.toString())).toHexString();
    }

    /**
     * Implementation based on v4 specs: 128 bits, with 60 bits of timestamp, 48
     * bits of computer identifier, 14 bits of uniquifier, and six bits are
     * fixed.<br/>
     * Sample representations: 550e8400-e29b-41d4-a716-446655440000,
     * f47ac10b-58cc-4372-a567-0e02b2c3d479
     * 
     * <pre>
     * http://en.wikipedia.org/wiki/Universally_unique_identifier#Version_4_.28random.29
     * </pre>
     */
    private static final class UuidV4 implements Uuid, Serializable, Cloneable {

        private static final long serialVersionUID = 1L;

        private final UUID javaUuid;

        private UuidV4() {
            // this.javaUuid = UUID.randomUUID();
            // TODO: optimize speed by new UUID(msb, lsb) with AtomicLong CAS
            java.util.Random random = new java.util.Random();
            this.javaUuid = new UUID(random.nextLong(), random.nextLong());
        }

        private UuidV4(UUID javaUuid) {
            this.javaUuid = javaUuid;
        }

        private UuidV4(String uuidStrValue) {
            String normalizedUuidStr = uuidStrValue;
            // expecting 36 characters
            if (uuidStrValue != null && uuidStrValue.length() == 38) {
                normalizedUuidStr = normalizedUuidStr.replace("{", "").replace("}", "");
            }
            if (uuidStrValue != null && uuidStrValue.length() == 32) {
                normalizedUuidStr = normalizedUuidStr.substring(0, 8) + "-" + normalizedUuidStr.substring(8, 12) + "-"
                        + normalizedUuidStr.substring(12, 16) + "-" + normalizedUuidStr.substring(16, 20) + "-"
                        + normalizedUuidStr.substring(20, 32);
            }
            this.javaUuid = UUID.fromString(normalizedUuidStr);
        }

        public String toHexString() {
            return toString().replaceAll("[-{}]", ""); // ("(-|{|})", "");
        }

        /**
         * @override
         */
        public String toString() {
            return javaUuid.toString();
        }

        /**
         * @override
         */
        public Uuid clone() {
            return new UuidV4(javaUuid);
        }

        /**
         * @override
         */
        public int hashCode() {
            final int PRIME = 31;
            final int PRIME_SEED = 1; // 7
            int result = PRIME_SEED;
            result = PRIME * result + ((javaUuid == null) ? 0 : javaUuid.hashCode());
            return result;
        }

        /**
         * @override
         */
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || !(obj instanceof Uuid)) {
                return false;
            }
            // prefer compareTo() over equals() for this implementation
            if (obj instanceof UuidV4) {
                return (((UuidV4) obj).javaUuid).compareTo(this.javaUuid) == 0;
            }
            return obj.toString().compareTo((this.javaUuid.toString())) == 0;
        }

    }

}
