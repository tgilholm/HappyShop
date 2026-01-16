package ci553.happyshop.utility.handlers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Final utility class defining concurrency methods
 */
public final class ExecutorHandler
{
    private static final Logger logger = LogManager.getLogger();


    private ExecutorHandler()
    {
    }
    // Final class, no instantiation


    /**
     * The ExecutorService used for background DB queries. One thread per usage.
     * This allows access to the database without slowing down the system. Note that this means
     * Platform.runLater() is used to update JavaFX elements on the main thread, as is required.
     *
     * @param threadName the name of the thread to execute db queries on
     * @return a single-thread <code>ExecutorService</code>
     */
    @Contract("_ -> new")
    public static @NotNull ExecutorService getExecutorService(@NotNull String threadName)
    {
        return Executors.newSingleThreadExecutor(runnable ->
                {
                    // SingleThreadExecutors execute tasks one at a time
                    // The executorService will re-use this thread and execute the provided runnable.
                    Thread thread = new Thread(runnable, threadName);
                    thread.setDaemon(true); // daemon = true so that the JVM doesn't get stuck on background threads that won't end
                    logger.debug("Starting runnable");
                    return thread;
                }
        );
    }
}
