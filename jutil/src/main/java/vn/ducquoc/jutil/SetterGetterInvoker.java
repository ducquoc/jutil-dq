package vn.ducquoc.jutil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for invoking trivial setters/getters (to increase coverage).
 * 
 * @author ducquoc
 * @see net.kazed.nowhere.SetterGetterInvoker
 * @see com.express.testutils.SetterGetterInvoker
 */
public class SetterGetterInvoker<T> {

  private T testTarget;

  private ValueFactory valueFactory;

  public SetterGetterInvoker(T testTarget) {
    super();
    this.testTarget = testTarget;
    this.valueFactory = new ValueFactory();
  }

  /**
   * Invoke all setters and getters of the class.
   */
  public void invokeSettersAndGetters() {
    invokeSettersAndGetters(new ArrayList<String>());
  }

  /**
   * Invokes setters and getters except the ones in the list.
   * 
   * @param excludedMethodNames
   */
  public void invokeSettersAndGetters(List<String> excludedMethodNames) {
    Class<? extends Object> targetClass = testTarget.getClass();
    Method[] methods = targetClass.getMethods();
    for (int i = 0; i < methods.length; i++) {
      Method method = methods[i];
      if (excludedMethodNames.contains(method.getName())) {
        continue;
      }
      if (method.getName().startsWith("set")) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length == 1) {
          Object testValue = valueFactory.createValue(parameterTypes[0]);
          try {
//            System.out.println("Invoking set method: " + method.toString() + " with argument: " + testValue);

            method.invoke(testTarget, testValue);
            if (testValue instanceof Boolean) {
              invokeGetter(targetClass, testValue, "is" + method.getName().substring(3));
            } else {
              invokeGetter(targetClass, testValue, "get" + method.getName().substring(3));
            }
          } catch (IllegalAccessException ex) {
            throw new RuntimeException("Failed to access setter method: " + method.toString(), ex);
          } catch (InvocationTargetException ex) {
            throw new RuntimeException("Failed to invoke setter method: " + method.toString(), ex);
          }
        }
      }
    }
  }

  /**
   * @param targetClass
   *          Class of target object.
   * @param expectedValue
   *          Expected value.
   * @param getterName
   *          Name of getter.
   */
  private void invokeGetter(Class<?> targetClass, Object expectedValue, String getterName) {
    try {
      Method getterMethod = targetClass.getMethod(getterName);
//      System.out.println("Invoking get method: " + getterMethod.toString());

      getterMethod.invoke(testTarget);
    } catch (NoSuchMethodException ignore) {
      System.out.println("[can be ignored] Getter does not exist: " + getterName + "() of " + targetClass.getSimpleName());
    } catch (IllegalAccessException ex) {
      throw new RuntimeException("Failed to access getter method: " + getterName, ex);
    } catch (InvocationTargetException ex) {
      throw new RuntimeException("Failed to invoke getter method: " + getterName, ex);
    }
  }

}

// This class can be public, but I prefer not expose it, for simplicity's sake
class ValueFactory {

  private Map<Class<?>, Object> typeValues;

  public ValueFactory() {
    typeValues = new HashMap<Class<?>, Object>();
    typeValues.put(String.class, "test");
    typeValues.put(Integer.class, Integer.valueOf(42));
    typeValues.put(Integer.TYPE, Integer.valueOf(42));
    typeValues.put(Long.class, Long.valueOf(42L + Integer.MAX_VALUE));
    typeValues.put(Long.TYPE, Long.valueOf(42L + Integer.MAX_VALUE));
    typeValues.put(Boolean.class, Boolean.TRUE);
    typeValues.put(Boolean.TYPE, Boolean.TRUE);
  }

  /**
   * Create a test value.
   * 
   * @param type
   *          Desired type of value.
   * @return Created value.
   */
  public Object createValue(Class<?> type) {
    Object value = null;
    if (typeValues.containsKey(type)) {
      value = typeValues.get(type);
    } else if (type.isInterface()) {
      value = Proxy.newProxyInstance(type.getClassLoader(), new Class[] { type },
          new TestInvocationHandler(type.getName()));
    }
    return value;
  }

  private static class TestInvocationHandler implements InvocationHandler {

    private String name;

    public TestInvocationHandler(String name) {
      super();
      this.name = name;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
      Object result = null;
      if ("toString".equals(method.getName())) {
        result = "proxy for " + name;
      }
      return result;
    }

  }

}
