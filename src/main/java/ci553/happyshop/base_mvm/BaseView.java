package ci553.happyshop.base_mvm;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.URL;


/**
 * Class from which Views derive shared behaviour. Handles loading FXML/CSS, connects to a controller, and sets the title.
 *
 * @param <C> a class <code>C</code> extending <code>BaseController</code>
 * @param <P> a root JavaFX element extending from the base class <code>Parent</code>
 */
public class BaseView<C extends BaseController<? extends BaseModel>, P extends Parent>
{
    private final Logger logger = LogManager.getLogger();
    private final C controller;
    private final String FXML_URL;     // The location of the fxml for this view
    private final String CSS_URL;
    private final String title;


    public BaseView(@NotNull C controller, @NotNull String fxmlUrl, @Nullable String cssURL, @NotNull String title)
    {
        this.controller = controller;
        this.FXML_URL = fxmlUrl;
        this.CSS_URL = cssURL;
        this.title = title;
    }


    /**
     * Base method. Loads the root element and any CSS, then starts this window.
     * Extending classes should prepend functionality to a super.start() call after defining
     * the window bounds.
     *
     * @param window the <code>Stage</code> to launch
     */
    public void start(@NotNull Stage window)
    {
        // Load the root element
        P root = loadFXML();
        Scene scene = new Scene(root);

        // Load the CSS if present
        loadCSS(scene);
        window.setScene(scene);
        window.setTitle(title);
        window.show();        // Start the window
    }


    /**
     * Attempts to parse a <code>String</code> into a <code>URL</code> object
     *
     * @param stringURL the location of a file
     * @return t
     */
    @Nullable
    private URL parseURL(String stringURL)
    {
        logger.debug("Parsing string {} to URL", stringURL);
        URL url = getClass().getResource(stringURL);

        if (url == null)
        {
            logger.error("File not found at location {}, Ensure that locations have preceding backslashes: /fxml/FXMLFile.fxml or /css/CSSFile.css", stringURL);
            return null;
        } else
        {
            return url;
        }
    }


    /**
     * Creates an FXML loader from a URL and binds the controller to it. <br>
     * Note- a constructor MUST be defined in the FXML file.
     *
     * @return an FXML loader set to the location specified by <code>fxmlURL</code>
     */
    private @NotNull FXMLLoader getFxmlLoader()
    {
        logger.debug("Attempting to create FXMLLoader from FXML");

        URL fxmlLocation = parseURL(FXML_URL);
        if (fxmlLocation == null)
        {
            logger.error("Failed to load FXML");
            return new FXMLLoader();
        } else
        {
            logger.debug("Created FXMLLoader from FXML");
            FXMLLoader loader = new FXMLLoader(fxmlLocation);        // Load the FXML

            // Set the controller factory
            loader.setControllerFactory(controllerFactory ->
            {
                if (controllerFactory == controller.getClass())
                {
                    return controller;
                }

                try
                {
                    return controllerFactory.getDeclaredConstructor().newInstance();
                } catch (Exception e)
                {
                    throw new RuntimeException("Failed to set controllerFactory");
                }
            });
            return loader;
        }
    }


    /**
     * Loads the provided FXML file into the root element <code>Parent</code>
     *
     * @return an object of type <code>P</code>, where "P" is the type parameter for the root object
     */
    private P loadFXML()
    {
        try
        {
            return getFxmlLoader().load();
        } catch (IOException e)
        {
            throw new RuntimeException("Failed to load FXML: " + e);
        }
    }


    /**
     * Loads the CSS, if present. If not present, carries on without loading stylesheets
     */
    private void loadCSS(Scene scene)
    {
        if (CSS_URL == null)
        {
            logger.debug("No CSS file provided, aborting loadCSS()");
        } else
        {
            URL cssLocation = parseURL(CSS_URL);
            if (cssLocation == null)
            {
                logger.warn("Failed to load CSS file");
            } else
            {
                scene.getStylesheets().add(cssLocation.toExternalForm());
                logger.debug("Loaded CSS file {}", cssLocation);
            }
        }
    }
}
