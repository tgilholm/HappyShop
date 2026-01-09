package ci553.happyshop.base_mvm;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Abstract Class from which Views derive shared behaviour.
 * Avoids repeated declaration of loggers. Connects to a controller extending AbstractController
 */
public class AbstractView<C extends AbstractController<? extends AbstractModel>>
{
    protected final Logger logger = LogManager.getLogger();
    protected final C controller;


    public AbstractView(C controller)
    {
        this.controller = controller;
    }
}
