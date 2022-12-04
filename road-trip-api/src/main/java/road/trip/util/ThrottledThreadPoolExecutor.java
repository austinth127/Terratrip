package road.trip.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.concurrent.*;

import static java.lang.System.currentTimeMillis;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;

@Log4j2
public class ThrottledThreadPoolExecutor {
    private static final int POOL_SIZE = 50;
    private ScheduledExecutorService es;
    private boolean initialized;
    private final double frequency;     // Hz
    private long lastRequestTime;       // milliseconds since epoch

    public ThrottledThreadPoolExecutor(double frequency) {
        this.frequency = frequency;
        lastRequestTime = 0;
        es = Executors.newScheduledThreadPool(POOL_SIZE);
        initialized = true;
    }

    public void execute(final Runnable task) {
        if (!initialized) {
            es = Executors.newScheduledThreadPool(POOL_SIZE);
            initialized = true;
        }
        long curTime = currentTimeMillis();
        long requestTime = Math.max(lastRequestTime + Math.round((1 / frequency) * 1000 + 1), curTime);
        es.schedule(task, requestTime - curTime, MILLISECONDS);
        lastRequestTime = Math.max(currentTimeMillis(), requestTime);
    }

    public void joinAll() throws InterruptedException {
        es.shutdown();
        es.awaitTermination(10, MINUTES);
        initialized = false;
    }
}