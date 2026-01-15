package ci553.happyshop.client.login;

import ci553.happyshop.base_mvm.BaseView;
import javafx.application.Application;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Class to link together the Login MVC and launch the login system.
 */
public class LoginClient extends Application
{
    public static void main(String[] args)
    {
        launch(args);
    }

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
        // Link together Model, View, Controller
        LoginModel model = new LoginModel();
        LoginController controller = new LoginController(model);
        BaseView<LoginController, HBox> view = new BaseView<>(controller, "/fxml/LoginView.fxml", null, "Login Client");
        view.start(primaryStage);
    }
}
