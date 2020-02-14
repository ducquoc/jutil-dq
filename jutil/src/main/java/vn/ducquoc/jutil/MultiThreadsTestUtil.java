package vn.ducquoc.jutil;

import java.util.concurrent.*;
import java.util.stream.IntStream;

/**
 * Test utilities for multi-thread running.
 *
 * @author ducquoc
 * @see ConcurrentUnit
 * @see Cactoos
 * @see //ducquoc.wordpress.com/2012/04/07/java-thread-safe-example/
 */
public class MultiThreadsTestUtil {

    public static final int DEFAULT_THREAD_COUNT = 4;
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

    //exercise for readers: write main()/unitTest method to run DateFormat.parse() as in the link "example" above

}
