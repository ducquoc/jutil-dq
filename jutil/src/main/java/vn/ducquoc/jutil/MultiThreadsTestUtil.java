package vn.ducquoc.jutil;

import java.util.concurrent.*;
import java.util.stream.IntStream;

/**
 * Test utilities for multi-thread running.
 *
 * @author ducquoc
 * @see ConcurrentUnit
 * @see Cactoos
 */
public class MultiThreadsTestUtil {

    public static final int DEFAULT_THREAD_COUNT = 2;
    public static final int DEFAULT_LOOP_COUNT = 1;

    private final int threadCount;
    private final int loopCount;
    private final ExecutorService executor;

    public MultiThreadsTestUtil() {
        this(DEFAULT_THREAD_COUNT);
    }

    public MultiThreadsTestUtil(int threadCount) {
        this(threadCount, DEFAULT_LOOP_COUNT);
    }

    public MultiThreadsTestUtil(int threadCount, int loopCount) {
        this(threadCount, loopCount, Executors.newCachedThreadPool()); //newFixedThreadPool(threadCount));
    }

    public MultiThreadsTestUtil(int threadCount, int loopCount, ExecutorService executor) {
        this.threadCount = threadCount;
        this.loopCount = loopCount;
        this.executor = executor;
    }

    public MultiThreadsTestUtil(int threadCount, int loopCount, ThreadFactory threadFactory) {
        this(threadCount, loopCount, Executors.newCachedThreadPool(threadFactory));
    }


    public int totalRunCount() {
        return threadCount * loopCount;
    }

    public void spamRun(final Runnable runAction) throws InterruptedException {
        spawnThreads(runAction).await();
    }

    public void blitzRun(long timeoutMs, final Runnable runAction) throws InterruptedException, TimeoutException {
        if (!spawnThreads(runAction).await(timeoutMs, TimeUnit.MILLISECONDS)) {
            throw new TimeoutException("timed out waiting for actions complete blitz");
        }
    }

    private CountDownLatch spawnThreads(final Runnable action) {
        final CountDownLatch finished = new CountDownLatch(threadCount);

        IntStream.range(0, threadCount).forEach((i) ->
                executor.execute(() -> {
                    try {
                        repeatRun(action);
                    } finally {
                        finished.countDown();
                    }
                })
        );
        return finished;
    }

    private void repeatRun(Runnable runAction) {
        IntStream.range(0, loopCount).forEach((i) -> runAction.run());
    }

    public void shutdown() {
        executor.shutdown();
    }

    public void shutdownNow() {
        executor.shutdownNow();
    }

    public static void main(String args[]) throws Exception {
        final java.text.DateFormat df = new java.text.SimpleDateFormat("dd-MMM-yyyy");
        final String[] testdata = { "01-Jan-1999", "14-Feb-2001", "31-Dec-2007" };
        final int loopCount = 7; //7 enough with CountDownLatch, without it would be 12->120 depending on your computer

        Runnable runAction = () -> {
//            IntStream.range(0, loopCount).forEach((i) -> {
                try {
                    String str = testdata[new java.util.Random().nextInt(testdata.length)];
                    String str2 = null;
                    /* synchronized(df) */
                    {
                        java.util.Date d = df.parse(str);
                        str2 = df.format(d);
                    }
                    System.out.println("EXPECTED " + str + " ACTUAL " + str2);
                } catch (Exception ex) {
                    throw new RuntimeException("Parse failed: " + ex);
                }
//            });
        };

        //Callable<java.util.Date> task = () -> df.parse("20101010");

        MultiThreadsTestUtil multiThreadsTestUtil = new MultiThreadsTestUtil(4, loopCount); //4 thread
        multiThreadsTestUtil.spamRun(runAction);

        multiThreadsTestUtil.shutdownNow(); //shutdownNow();
        System.exit(0);
    }

}
