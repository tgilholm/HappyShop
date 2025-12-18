package ci553.happyshop.client.customer;

import ci553.happyshop.utility.UIStyle;
import ci553.happyshop.utility.WinPosManager;
import ci553.happyshop.utility.WindowBounds;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;

/**
 * The CustomerView is separated into two sections by a line :
 * <p>
 * 1. Search Page – Always visible, allowing customers to browse and search for
 * products. 2. the second page – display either the Trolley Page or the Receipt
 * Page depending on the current context. Only one of these is shown at a time.
 */


public class CustomerView
{
    private final Logger logger = LogManager.getLogger();
    private final CustomerController cusController;

    public CustomerView(CustomerController cusController)
    {
        this.cusController = cusController;
    }

    // Holds a reference to this CustomerView window for future access and
    // management
    // (e.g. positioning the removeProductNotifier when needed).
    private Stage viewWindow;


    /**
     * Loads the FXML and starts a new Stage
     *
     * @param window the Stage to start
     */
    public void start(Stage window)
    {
        GridPane gridPane;                    // Root attribute of CustomerView.fxml

        // Null-check the FXML file
        URL fxmlURL = getClass().getResource("/fxml/CustomerView.fxml");
        if (fxmlURL == null)
        {
            throw new IllegalStateException("CustomerView.fxml not found");
        }
        FXMLLoader loader = getFxmlLoader(fxmlURL);

        try
        {
            gridPane = loader.load();
        } catch (IOException e)
        {

            e.printStackTrace();
            throw new RuntimeException("Failed to load FXML: " + e);
        }
        logger.info("Loaded FXML file {}", fxmlURL);

        int WIDTH = UIStyle.customerWinWidth;
        int HEIGHT = UIStyle.customerWinHeight;

        Scene scene = new Scene(gridPane, WIDTH, HEIGHT);

        // Load the CSS
        URL resource = getClass().getResource("/css/styles.css");
        if (resource != null)
        {
            scene.getStylesheets().add(resource.toExternalForm());
            logger.info("Loaded CSS file {}", resource);
        } else
        {
            logger.warn("Failed to load CSS");
        }

        window.setScene(scene);
        window.setTitle("🛒 HappyShop Customer Client");
        WinPosManager.registerWindow(window, WIDTH, HEIGHT); // calculate position x and y for this window

        window.show();        // Start the window
        viewWindow = window;// Sets viewWindow to this window for future reference and management.
    }

    /**
     * Creates an FXML loader from a URL and binds <code>CustomerController</code> to it
     *
     * @param fxmlURL the location of the fxml file to load
     * @return an FXML loader set to the location specified by <code>fxmlURL</code>
     */
    private @NotNull FXMLLoader getFxmlLoader(URL fxmlURL)
    {
        FXMLLoader loader = new FXMLLoader(fxmlURL);        // Loads the FXML

        // Set the ControllerFactory (controller is already defined in FXML)
        loader.setControllerFactory(controllerFactory ->
        {
            if (controllerFactory == CustomerController.class)
            {
                return cusController;
            }

            try
            {
                return controllerFactory.getDeclaredConstructor().newInstance();
            } catch (Exception e)
            {
                throw new RuntimeException("Cannot instantiate Controller");
            }
        });
        return loader;
    }


    // Retrieve the X, Y coordinates of the extreme corners of this window
    WindowBounds getWindowBounds()
    {
        return new WindowBounds(viewWindow.getX(), viewWindow.getY(), viewWindow.getWidth(), viewWindow.getHeight());
    }
}
