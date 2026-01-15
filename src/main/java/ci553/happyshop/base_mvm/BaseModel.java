package ci553.happyshop.base_mvm;


import ci553.happyshop.utility.handlers.ExecutorHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ExecutorService;

/**
 * Abstract Class from which Models derive shared behaviour.
 * Avoids repeated declaration of loggers and other tools.
 */
public abstract class BaseModel
{
    protected final Logger logger = LogManager.getLogger();
    protected final ExecutorService executorService;    // Each Model has its own separate executor thread for db queries


    /**
     * Creates a new BaseModel. Delegates to ExecutorHandler to create an executor service with the class name
     * of the inheritor: <code>getClass().getSimpleName()</code> executed on child classes will retrieve their name instead.
     * <p>
     * For example, CustomerModel inherits BaseModel. Its executor will be named CustomerModel-Executor
     */
    protected BaseModel()
    {
        this.executorService = ExecutorHandler.getExecutorService(getClass().getSimpleName() + "-Executor");
    }
}
