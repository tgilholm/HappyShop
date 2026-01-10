package ci553.happyshop.base_mvm;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Abstract Class from which Models derive shared behaviour.
 * Avoids repeated declaration of loggers and other tools.
 */
public abstract class BaseModel
{
    protected final Logger logger = LogManager.getLogger();
}
