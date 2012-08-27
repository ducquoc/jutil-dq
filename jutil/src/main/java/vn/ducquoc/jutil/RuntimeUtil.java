package vn.ducquoc.jutil;

import java.lang.management.ClassLoadingMXBean;
import java.lang.management.CompilationMXBean;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadMXBean;
import java.util.List;

/**
 * Utility class for Java runtime management/metric.
 * 
 * @author ducquoc
 * @see java.com.schlimm.java7.benchmark.addon.SystemInformation
 * @see com.espertech.esper.util.MetricUtil.java
 */
public class RuntimeUtil {

    public static void printThreadInfo(boolean includeThreadNames) {
        System.out.println("Total started thread count: " + threadMXBean().getTotalStartedThreadCount());
        System.out.println("Peak thread count: " + threadMXBean().getPeakThreadCount());
        System.out.println("Deamon thread count: " + threadMXBean().getDaemonThreadCount());
        System.out.println("Thread count: " + threadMXBean().getThreadCount());
    }

    public static RuntimeMXBean runtimeMXBean() {
        return ManagementFactory.getRuntimeMXBean();
    }

    public static ThreadMXBean threadMXBean() {
        return ManagementFactory.getThreadMXBean();
    }

    public static CompilationMXBean compilationMXBean() {
        return ManagementFactory.getCompilationMXBean();
    }

    public static MemoryMXBean memoryMXBean() {
        return ManagementFactory.getMemoryMXBean();
    }

    public static List<GarbageCollectorMXBean> getGarbageCollectorMXBeans() {
        return ManagementFactory.getGarbageCollectorMXBeans();
    }

    public static ClassLoadingMXBean classLoadingMXBean() {
        return ManagementFactory.getClassLoadingMXBean();
    }

    public static OperatingSystemMXBean operatingSystemMXBean() {
        return ManagementFactory.getOperatingSystemMXBean();
    }

}
