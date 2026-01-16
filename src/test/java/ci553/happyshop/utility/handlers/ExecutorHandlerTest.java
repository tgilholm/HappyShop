package ci553.happyshop.utility.handlers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the ExecutorHandler utility class
 */
class ExecutorHandlerTest
{
    @Test
    @DisplayName("Test creating a new ExecutorService")
    void testGetExecutorService() throws Exception
    {
        ExecutorService executor = ExecutorHandler.getExecutorService("MyTest-Executor");
        try {
            /*
            This test creates the executor service with the utility class, then creates a "future".
            This executes concurrently and gets the name of the newly created thread.

            The test asserts that the thread created properly, was created with the correct name and is marked as a daemon
             */
            Future<String> future = executor.submit(() -> Thread.currentThread().getName() + ":" + Thread.currentThread().isDaemon());
            String result = future.get(2, TimeUnit.SECONDS);
            assertNotNull(result);
            assertTrue(result.contains("MyTest-Executor"), "Executor thread name should contain provided name");
            assertTrue(result.endsWith(":true"), "Executor thread should be daemon");
        } finally {
            executor.shutdownNow();
        }
    }
}
