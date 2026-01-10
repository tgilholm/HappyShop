package ci553.happyshop.utility.alerts;

import ci553.happyshop.utility.FileHandler;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;

/**
 * Base class for Alerts. Extends <code>Alert</code> base class in JavaFX to use basic functionality,
 * while supporting custom CSS and layout with <code>Nodes</code>
 */
public class BaseAlert extends Alert
{
    /**
     * Creates an alert with the given AlertType (refer to the {@link AlertType}
     * documentation for clarification over which one is most appropriate).
     *
     * <p>By passing in an AlertType, default values for the
     * {@link #titleProperty() title}, {@link #headerTextProperty() headerText},
     * and {@link #graphicProperty() graphic} properties are set, as well as the
     * relevant {@link #getButtonTypes() buttons} being installed. Once the Alert
     * is instantiated, developers are able to modify the values of the alert as
     * desired.
     *
     * <p>It is important to note that the one property that does not have a
     * default value set, and which therefore the developer must set, is the
     * {@link #contentTextProperty() content text} property (or alternatively,
     * the developer may call {@code alert.getDialogPane().setContent(Node)} if
     * they want a more complex alert). If the contentText (or content) properties
     * are not set, there is no useful information presented to end users.
     *
     * @param type an alert with the given AlertType
     */
    public BaseAlert(AlertType type, String title, String headerText, String contentText, String cssPath)
    {
        super(type);    // Set the type of the alert
        setTitle(title);
        setHeaderText(headerText);
        setContentText(contentText);
        Logger logger = LogManager.getLogger();

        // Add the CSS if present
        if (cssPath != null)
        {
            URL cssURL = FileHandler.parseURL(cssPath);
            if (cssURL != null)
            {
                getDialogPane().getStylesheets().add(cssURL.toExternalForm());
                getDialogPane().getStylesheets().add("custom-alert");
            } else
            {
                logger.debug("Failed to get css for alert: {}", this);
            }
        } else
        {
            logger.debug("No css specified for alert: {}", this);
        }

    }


    /**
     * Sets the content of this alert to a specified JavaFX node. Allows custom alert designs
     * @param node a JavaFX node
     */
    public void setContentNode(Node node)
    {
        getDialogPane().setContent(node);
    }
}
