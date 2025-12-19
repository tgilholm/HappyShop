package ci553.happyshop.client.customer.basket;

import ci553.happyshop.catalogue.Customer;
import ci553.happyshop.data.repository.RepositoryFactory;
import ci553.happyshop.domain.service.BasketService;
import ci553.happyshop.domain.service.ServiceFactory;
import javafx.application.Application;
import javafx.stage.Stage;

public class BasketClient extends Application
{
    private final Customer customer;

    public BasketClient(Customer customer)
    {
        this.customer = customer;
    }

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
        BasketService basketService = ServiceFactory.getBasketService();

        BasketModel model = new BasketModel(basketService, customer);
        BasketController controller = new BasketController(model);
        BasketView view = new BasketView(controller);
        view.start(primaryStage);
    }

}
