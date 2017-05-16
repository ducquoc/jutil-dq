package vn.ducquoc.jutil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for simple multi-thread.
 * 
 * @see https://ducquoc.wordpress.com/2012/04/07/java-thread-safe-example/
 * @see vn.ducquoc.util.DateFormatterTest
 */
public class SimpleThreadSafeTest {

  public void executeThreads(final int threadCount) throws InterruptedException, ExecutionException {
    // replace with SyncCounter to check thread-safe compliant
    final UnsafeCounter testTarget = new UnsafeCounter();
    //final SyncCounter testTarget = new SyncCounter();

    Callable<Long> task = new Callable<Long>() {
      /** @override */
      public Long call() throws Exception {
        return testTarget.nextCount();
      }
    };
    List<Callable<Long>> tasks = Collections.nCopies(threadCount, task);
    ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
    List<Future<Long>> futures = executorService.invokeAll(tasks);

    List<Long> resultList = new ArrayList<Long>(futures.size());
    // Check for exceptions
    for (Future<Long> future : futures) {
      // Throws an exception if an exception was thrown by the task.
      resultList.add(future.get());
    }

    // Validate the IDs
    Assert.assertEquals(threadCount, futures.size());
    List<Long> expectedList = new ArrayList<Long>(threadCount);
    for (long i = 1; i <= threadCount; i++) {
      expectedList.add(i);
    }
    Collections.sort(resultList);
    Assert.assertEquals(expectedList, resultList);
  }

  @Test
  public void test01() throws InterruptedException, ExecutionException {
    executeThreads(1*16);
  }

  @Test
  public void test02() throws InterruptedException, ExecutionException {
    executeThreads(2*16);
  }

  @Test
  public void test04() throws InterruptedException, ExecutionException {
    executeThreads(4*16);
  }

  @Test
  public void test08() throws InterruptedException, ExecutionException {
    executeThreads(8*16);
  }

  @Test
  public void test16() throws InterruptedException, ExecutionException {
    executeThreads(16*16);
  }

  @Test
  public void test32() throws InterruptedException, ExecutionException {
    executeThreads(32*16);
  }

  static class UnsafeCounter {
    private long counter = 0;

    public long nextCount() {
      return ++counter;
    }
  }

  static class SyncCounter {
    private long counter = 0;

    public synchronized long nextCount() {
      return ++counter;
    }
  }

  static class UnsafeDelayedCounter {
    private long counter = 0;

    public long nextCount() throws Exception {
      Thread.sleep(15);
      return ++counter;
    }
  }

  static class SyncBlockCounter {
    private long counter = 0;

    public long nextCount() {
      synchronized(this) {
        return ++counter;
      }
    }
  }

}
