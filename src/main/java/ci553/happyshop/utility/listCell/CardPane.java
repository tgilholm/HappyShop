package ci553.happyshop.utility.listCell;

import ci553.happyshop.utility.handlers.FileHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URL;

/**
 * The base class for Card objects to be displayed in TilePanes. Extends VBox
 * to be recognized by JavaFX views
 */
public class CardPane extends VBox
{
    protected static final Logger logger = LogManager.getLogger();

    /**
     * Constructor for base CardPane classes. Initializes provided FXML to clean up inheritors
     */
    public CardPane(String fxmlLocation)
    {
        FXMLLoader loader = new FXMLLoader();

        try
        {
            URL fxmlURL = FileHandler.parseURL(fxmlLocation);
            if (fxmlURL != null)
            {
                loader.setLocation(fxmlURL);
                loader.setController(this);
                loader.setRoot(this);

                loader.load();
            }
        } catch (IOException e)
        {
            logger.error("Failed to load FXML for CardPane", e);
        }
    }
}
