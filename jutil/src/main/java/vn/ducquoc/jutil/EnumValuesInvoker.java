package vn.ducquoc.jutil;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Helper class for invoking trivial enum methods (to increase coverage).
 * 
 * @author ducquoc
 * @see vn.ducquoc.jutil.SetterGetterInvoker
 * @see org.reflection.Reflections
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class EnumValuesInvoker<T> {

  private T testTarget;

  public EnumValuesInvoker(T targetClass) {
    super();
    this.testTarget = targetClass;
  }

  public T[] invokeEnumValues() {
    T[] returnedObjects = null;
    try {
      Class<?> enumClass = Class.forName(testTarget.toString().replaceAll("class ", ""));
      if (enumClass.isEnum()) {
//        Class<?> magicClass = Class.forName(enumClass.toString().replaceAll("class ", ""));
//        Method method = magicClass.Method("values()");
//        returnedObjects = (T[]) method.invoke(testTarget); // .invoke(magicClass.newInstance());
        returnedObjects = (T[]) enumClass.getEnumConstants();
      }
    } catch (Exception ex) {
      throw new RuntimeException("Failed to invoke enum values()", ex);
    }
    return returnedObjects;
  }

  public static List getEnumClasses(String packageName) throws ClassNotFoundException, IOException {
    List enumClasses = new ArrayList();
    for (Class clazz : getClasses(packageName)) {
      if (clazz.isEnum()) {
        enumClasses.add(clazz);
      }
    }
    return enumClasses;
  }

  /**
   * Scans all classes accessible from the context class loader which belong to
   * the given package and subpackages.
   * 
   * @param packageName
   *          The base package
   * @return list of classes
   * @throws ClassNotFoundException
   * @throws IOException
   */
  public static List<Class> getClasses(String packageName) throws ClassNotFoundException, IOException {
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    assert classLoader != null;
    String path = packageName.replace('.', '/');
    Enumeration<URL> resources = classLoader.getResources(path);
    List<File> dirs = new ArrayList<File>();
    while (resources.hasMoreElements()) {
      URL resource = resources.nextElement();
      String fileName = resource.getFile();
      String fileNameDecoded = URLDecoder.decode(fileName, "UTF-8");
      dirs.add(new File(fileNameDecoded));
    }
    ArrayList<Class> classes = new ArrayList<Class>();
    for (File directory : dirs) {
      classes.addAll(findClasses(directory, packageName));
    }
    return classes;
  }

  /**
   * Finds all classes in a given directory and subdirs, using recursion. <br/>
   * This method is not supposed to work with JAR files, but possible to do so
   * if you customize it using some unzip classes, such as ZipInputStream.
   * 
   * @param directory
   *          The base directory
   * @param packageName
   *          The package name for classes found inside the base directory
   * @return The classes
   * @throws ClassNotFoundException
   */
  private static List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
    List<Class> classes = new ArrayList<Class>();
    if (!directory.exists()) {
      return classes;
    }
    File[] files = directory.listFiles();
    for (File file : files) {
      String fileName = file.getName();
      if (file.isDirectory()) {
        assert !fileName.contains(".");
        classes.addAll(findClasses(file, packageName + "." + fileName));
      } else if (fileName.endsWith(".class")) { // && !fileName.contains("$")
        Class clazz;
        try {
          clazz = Class.forName(packageName + '.' + fileName.substring(0, fileName.length() - 6));
        } catch (ExceptionInInitializerError e) { // may happen with injections
          clazz = Class.forName(packageName + '.' + fileName.substring(0, fileName.length() - 6), false, Thread
              .currentThread().getContextClassLoader());
        }
        classes.add(clazz);
      }
    }
    return classes;
  }

}
