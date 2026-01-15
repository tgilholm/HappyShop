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
     * Initializes the Warehouse client's Model, View, and Controller, and links them together for communication.
     * It also creates the DatabaseRW instance via the DatabaseRWFactory and injects it into the Model.
     * Once the components are linked, the warehouse interface (view) is started.
     * <p>
     * Also creates the dependent HistoryWindow and AlertSimulator,
     * which track the position of the Warehouse window and are triggered by the Model when needed.
     * These components are linked after launching the Warehouse interface.
     */
    @Override
    public void start(Stage window)
    {
        startWarehouseClient(window);
    }


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


        //HistoryWindow historyWindow = new HistoryWindow();
        //AlertSimulator alertSimulator = new AlertSimulator();

        // Link after start warehouse interface
//        model.historyWindow = historyWindow;
//        model.alertSimulator = alertSimulator;
//        historyWindow.warehouseView = view;
//        alertSimulator.warehouseView = view;
    }
}
