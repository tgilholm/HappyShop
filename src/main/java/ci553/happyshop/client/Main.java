package ci553.happyshop.client;


import ci553.happyshop.client.login.LoginClient;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Launches the HappyShop application via the Login Client
 */
public class Main extends Application
{
    /**
     * The main entry point for all JavaFX applications.
     * The start method is called after the init method has returned,
     * and after the system is ready for the application to begin running.
     *
     * <p>
     * NOTE: This method is called on the JavaFX Application Thread.
     * </p>
     *
     * @param primaryStage the primary stage for this application, onto which
     *                     the application scene can be set.
     *                     Applications may create other stages, if needed, but they will not be
     *                     primary stages.
     * @throws Exception if something goes wrong
     */
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        LoginClient.startLoginClient(new Stage());
    }
}