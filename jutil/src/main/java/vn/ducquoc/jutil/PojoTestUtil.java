package vn.ducquoc.jutil;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Test utilities. Some basic consts, singletons, and value fillers (POJO Factory) for tests.
 *
 * @author ducquoc
 * @see uk.co.jemos.podam.api.PodamFactoryImpl
 * @see com.github.javafaker.Faker
 */
public class PojoTestUtil {

    public static final String CAFEBABE = "cafebabe-f00d-4d1e-beef-c0dedeadbeef";
    public static final UUID CAFEBABE_UUID = UUID.fromString(CAFEBABE);
    public static final String PREFIX = "DT-";//DataTest
    public static final int LIMITED_INT = 2900;

    public static final Random RAND = new Random();

    /**
     * Factory method - Inject values by RandomValue strategy.
     */
    public static <T> T manufacturePojo(Class<T> clazz) throws Exception {
        if (clazz.isArray()) {
            return (T) manufactureArray(clazz);
        }
        T instance = clazz.newInstance();

        for (Field field : getDeclaredAndInheritedFields(clazz, null)) {
            field.setAccessible(true);
            Object value = provideRandomValueForField(field);
            field.set(instance, value);
        }
        return instance;
    }

    public static Object manufactureArray(Class<?> clazz) throws Exception {
        if (clazz.isArray()) {//avoid circular loop
            Class<?> cType = clazz.getComponentType();
            Object arr = Array.newInstance(cType, 1);
            Array.set(arr, 0, provideRandomValueForType(cType, cType.getTypeName()));
            return arr;
        }
        return manufacturePojo(clazz);//null;
    }

    /**
     * <b>clazz.getDeclaredFields()</b> and <b>clazz.getFields()</b> are not enough.
     */
    public static Iterable<Field> getDeclaredAndInheritedFields(Class<?> clazz, Class<?> exclusiveSuperClazz) {

        List<Field> currentClassFields = new ArrayList<Field>(Arrays.asList(clazz.getDeclaredFields()));
        Class<?> parentClass = clazz.getSuperclass();

        if (parentClass != null &&
                (exclusiveSuperClazz == null || !(parentClass.equals(exclusiveSuperClazz)))) {
            List<Field> parentClassFields =
                    (List<Field>) getDeclaredAndInheritedFields(parentClass, exclusiveSuperClazz);
            currentClassFields.addAll(parentClassFields);
        }

        return currentClassFields;
    }

    public static Object provideRandomValueForField(Field field) throws Exception {
        /*
        if (field.getAnnotation(javax.persistence.Version.class) != null) {
            return null;
        }
        */
        Class<?> type = field.getType();
        String signature = field.getGenericType().getTypeName();//getGenericSignature();

        return provideRandomValueForType(type, signature);
    }

    public static Object provideRandomValueForType(Class<?> type, String signature) throws Exception {
        if (type.isEnum()) {
            Object[] enumValues = type.getEnumConstants();
            return enumValues[RAND.nextInt(enumValues.length)];
        } else if (type.equals(String.class)) {
            return PREFIX + UUID.randomUUID().toString();
        } else if (type.equals(Integer.TYPE) || type.equals(Integer.class)) {
            return RAND.nextInt(LIMITED_INT);
        } else if (type.equals(Long.TYPE) || type.equals(Long.class)) {
            return RAND.nextLong();
        } else if (type.equals(Double.TYPE) || type.equals(Double.class)) {
            return RAND.nextDouble();
        } else if (type.equals(Float.TYPE) || type.equals(Float.class)) {
            return RAND.nextFloat();
        } else if (type.equals(BigInteger.class)) {
            return BigInteger.valueOf(RAND.nextInt());
        } else if (type.equals(BigDecimal.class)) {
            return BigDecimal.valueOf(RAND.nextDouble());
        } else if (type.equals(UUID.class)) {
            return UUID.randomUUID();
        } else if (type.equals(Timestamp.class)) {
            return new Timestamp(System.currentTimeMillis());
        } else if (type.equals(java.sql.Date.class)) {
            return new java.sql.Date(System.currentTimeMillis());
        } else if (type.equals(Date.class)) {
            return new Date();
        } else if (type.equals(LocalDateTime.class)) {
            return LocalDateTime.now();
        } else if (type.equals(LocalDate.class)) {
            return LocalDate.now();
        } else if (type.isAssignableFrom(Map.class)) {
            if (signature != null ) {
                String eSign = signature.replaceAll("java.util.Map<", "").replaceAll(">", "");
                try {
                    Class<?> kType = Class.forName(eSign.split(", ")[0]);
                    Class<?> vType = Class.forName(eSign.split(", ")[1]);
                    return Collections.singletonMap(provideRandomValueForType(kType, null),
                            provideRandomValueForType(vType, null));
                } catch (ClassNotFoundException cnfEx) {
                    //ignore
                }
            }
            return new HashMap<Object, Object>();//type erasure - might attempt to parse signature but not reliable
        } else if (type.isAssignableFrom(List.class)) {
            if (signature != null ) {
                String eSign = signature.replaceAll("java.util.List<", "").replaceAll(">", "");
                try {
                    Class<?> eType = Class.forName(eSign);
                    return Collections.singletonList(provideRandomValueForType(eType, null));
                } catch (ClassNotFoundException cnfEx) {
                    //ignore
                }
            }
            return new ArrayList<Object>();//type erasure - might attempt to parse signature but not reliable
        } else if (type.isAssignableFrom(Set.class)) {
            if (signature != null ) {
                String eSign = signature.replaceAll("java.util.Set<", "").replaceAll(">", "");
                try {
                    Class<?> eType = Class.forName(eSign);
                    return Collections.singleton(provideRandomValueForType(eType, null));
                } catch (ClassNotFoundException cnfEx) {
                    //ignore
                }
            }
            return new HashSet<Object>();//type erasure - might attempt to parse signature but not reliable
        } else if (type.isArray()) {//last due to lack of actual usage
            return manufactureArray(type);
        }
        //not take into account byte[].class and Char.class for now - Y.A.G.N.I

        return manufacturePojo(type);
    }

}
