package ci553.happyshop.base_mvm;

import javafx.fxml.FXML;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Abstract Class from which Controllers derive shared behaviour.
 * Avoids repeated declaration of loggers. Connects to a model extending AbstractModel
 */
public abstract class AbstractController<M extends AbstractModel>
{
    protected final Logger logger = LogManager.getLogger();
    protected final M model;


    protected AbstractController(M model)
    {
        this.model = model;
    }

    /**
     * Initializes the controller after the root element is finished processing.
     */
    @FXML
    protected abstract void initialize();

}
