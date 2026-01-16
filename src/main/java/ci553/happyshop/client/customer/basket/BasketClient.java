package ci553.happyshop.client.customer.basket;

import ci553.happyshop.base_mvm.BaseView;
import ci553.happyshop.catalogue.User;
import ci553.happyshop.service.BasketService;
import ci553.happyshop.service.ProductService;
import ci553.happyshop.service.ServiceFactory;
import javafx.application.Application;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * The BasketClient links together the MVC for the customer basket using dependency injection
 * The "start" method can then be used to launch the basket.
 * <p>
 * Note that the basket requires a logged in <code>User</code>
 */
public class BasketClient extends Application
{
    private final User user;

    private static final String basketFXML = "/fxml/BasketView.fxml";


    public BasketClient(User user)
    {
        this.user = user;
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
        // Get service instances & inject in constructor
        BasketService basketService = ServiceFactory.getBasketService();
        ProductService productService = ServiceFactory.getProductService();

        BasketModel model = new BasketModel(user, basketService, productService);
        BasketController controller = new BasketController(model);
        BaseView<BasketController, VBox> view = new BaseView<>(controller, basketFXML, null, "Basket Client");
        view.start(primaryStage);
    }

}
