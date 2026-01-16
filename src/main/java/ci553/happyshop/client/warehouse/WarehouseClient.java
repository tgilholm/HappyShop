package ci553.happyshop.client.warehouse;

import ci553.happyshop.base_mvm.BaseView;
import ci553.happyshop.service.CategoryService;
import ci553.happyshop.service.ProductService;
import ci553.happyshop.service.ServiceFactory;
import javafx.application.Application;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A standalone Warehouse client that can be run independently without launching the full system.
 * It is fully functional on its own.
 */
public class WarehouseClient extends Application
{
    private static final Logger logger = LogManager.getLogger();
    private static final String warehouseFXML = "/fxml/WarehouseView.fxml";
    private static final String warehouseCSS = "/css/styles.css";


    public static void main(String[] args)
    {
        launch(args);
    }


    /**
     * Starts the warehouse
     * @param window the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     */
    @Override
    public void start(Stage window)
    {
        startWarehouseClient(window);
    }

    /**
     * Injects dependencies into MVC and launches view
     */
    public static void startWarehouseClient(Stage window)
    {
        logger.info("Launching WarehouseClient");

        // Create service instances
        final ProductService productService = ServiceFactory.getProductService();
        final CategoryService categoryService = ServiceFactory.getCategoryService();


        WarehouseModel model = new WarehouseModel(productService, categoryService);
        WarehouseController controller = new WarehouseController(model);
        BaseView<WarehouseController, GridPane> view = new BaseView<>(controller, warehouseFXML, warehouseCSS, "Warehouse");
        view.start(window);

    }
}
