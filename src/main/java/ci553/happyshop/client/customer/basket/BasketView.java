package ci553.happyshop.client.customer.basket;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;

public class BasketView
{
    private final BasketController controller;

    public BasketView(BasketController controller)
    {
        this.controller = controller;
    }

    public void start(@NotNull Stage window)
    {
        try
        {
            // load fxml and start the window
            FXMLLoader loader = getFxmlLoader(getClass().getResource("/fxml/BasketView.fxml"));
            window.setScene(new Scene(loader.load()));
            window.show();
        } catch (IOException e)
        {
            throw new RuntimeException("Failed to start Basket Client", e);
        }
    }

    /**
     * Creates an FXML loader from a URL and binds the controller to it
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
            if (controllerFactory == BasketController.class)
            {
                return controller;
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
}
