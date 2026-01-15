package ci553.happyshop.client.customer;

import ci553.happyshop.base_mvm.BaseView;
import ci553.happyshop.catalogue.User;
import ci553.happyshop.domain.service.BasketService;
import ci553.happyshop.domain.service.CategoryService;
import ci553.happyshop.domain.service.ProductService;
import ci553.happyshop.domain.service.ServiceFactory;
import javafx.application.Application;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * A stand-alone Customer Client that can be run independently without launching the full system.
 * Designed for early-stage testing, though full functionality may require other clients to be active.
 */

public final class CustomerClient extends Application
{
    private static final Logger logger = LogManager.getLogger();
    private static final String customerFXML = "/fxml/CustomerView.fxml";
    private static final String customerCSS = "/css/styles.css";


    public static void main(String[] args)
    {
        launch(args);
    }


    @Override
    public void start(Stage window)
    {
        //startCustomerClient(window);

        //RemoveProductNotifier removeProductNotifier = new RemoveProductNotifier();
        //removeProductNotifier.cusView = cusView;
        //cusModel.removeProductNotifier = removeProductNotifier;
    }


    /**
     * injects dependencies into the Model, View, and Controller
     *
     * @param window the <code>Stage</code> object to run
     */
    public static void startCustomerClient(Stage window, User user)
    {
        logger.info("Launching CustomerClient");

        // Get service instances & inject in constructor
        BasketService basketService = ServiceFactory.getBasketService();
        ProductService productService = ServiceFactory.getProductService();
        CategoryService categoryService = ServiceFactory.getCategoryService();

        CustomerModel cusModel = new CustomerModel(user, basketService, productService, categoryService);
        CustomerController cusController = new CustomerController(cusModel);
        BaseView<CustomerController, GridPane> cusView = new BaseView<>(cusController, customerFXML, customerCSS, "Customer Client");
        cusView.start(window);

    }
}
