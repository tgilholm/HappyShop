package ci553.happyshop.client;

import ci553.happyshop.client.emergency.EmergencyExit;
import ci553.happyshop.client.orderTracker.OrderTracker;
import ci553.happyshop.client.picker.PickerController;
import ci553.happyshop.client.picker.PickerModel;
import ci553.happyshop.client.picker.PickerView;

import ci553.happyshop.client.warehouse.*;
import ci553.happyshop.orderManagement.OrderHub;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The Main JavaFX application class. The Main class is executable directly.
 * It serves as a foundation for UI logic and starts all the interfaces
 * <p>
 * This class launches all standalone clients (Customer, Picker, OrderTracker,
 * Warehouse, EmergencyExit) and links them together into a fully working
 * system.
 * This class launches all clients (Customer, Picker, OrderTracker, Warehouse, EmergencyExit)
 * and links them together
 * <p>
 * It performs essential setup tasks, such as initialising the order map in the OrderHub
 * and registering observers (viewing orders in progress).
 * <p>
 * Note: Each client type can be instantiated multiple times (e.g., calling startCustomerClient() as many times as needed)
 * to simulate a multi-user environment, where multiple clients of the same type interact with the system concurrently.
 * This will be modified to support a user login system requiring a user-name and password so that each client has a separate account.
 *
 * @author Shine Shan University of Brighton
 * @version 1.0
 */

/*
 * Edited by Thomas Gilholm
 *
 * To facilitate the login page separating the program into a user side and
 * warehouse side, it was necessary to change the start() method to open only
 * the login page, and move the methods opening the different clients to the
 * LoginModel
 */
public class Main extends Application
{
    public static void main(String[] args)
    {

    }


    public void start(Stage window) throws IOException
    {
        // When the program starts, open the login window only
        // Initialises the order map for the OrderHub. This must be called after starting the observer clients
        // (such as OrderTracker and Picker clients) to ensure they are properly registered for receiving updates.

        //startLoginWindow();
    }


    /**
     * Customer GUI
     * <p>
     * Initialises the Model, View, and Controller objects & links them together.
     * Also creates the DatabaseRW instance via the DatabaseRWFactory and injects it into the CustomerModel.
     * Starts the customer interface.
     * <p>
     * Also creates the RemoveProductNotifier, which tracks the position of the Customer View
     * and is triggered by the Customer Model when needed.
     */
    public void startCustomerClient()
    {
//		CustomerView cusView = new CustomerView();
//		CustomerController cusController = new CustomerController();
//		CustomerModel cusModel = new CustomerModel();
//		DatabaseRW databaseRW = DatabaseRWFactory.createDatabaseRW();

        // Initialises Customer, Picker and Order Windows
//		startCustomerClient();
//		startPickerClient();
//		startOrderTracker();
//		cusView.cusController = cusController;
//		cusController.cusModel = cusModel;
//		cusModel.cusView = cusView;
//		cusModel.databaseRW = databaseRW;
        //cusView.start(new Stage());

        // Initializes the order map for the OrderHub. This must be called after
        // starting the observer clients
        // (such as OrderTracker and Picker clients) to ensure they are properly
        // registered for receiving updates.
//		initializeOrderMap();
//		startWarehouseClient();
//		startEmergencyExit();
        //RemoveProductNotifier removeProductNotifier = new RemoveProductNotifier();
        //removeProductNotifier.cusView = cusView;
        //cusModel.removeProductNotifier = removeProductNotifier;
    }


    /**
     * Picker GUI
     * <p>
     * Creates the Model, View, and Controller objects for the Picker client & links them together
     * Starts the Picker interface.
     * <p>
     * Also registers the PickerModel with the OrderHub to receive order notifications.
     */
    private void startPickerClient()
    {
        PickerModel pickerModel = new PickerModel();
        PickerView pickerView = new PickerView();
        PickerController pickerController = new PickerController();
        pickerView.pickerController = pickerController;
        pickerController.pickerModel = pickerModel;
        pickerModel.pickerView = pickerView;
        pickerModel.registerWithOrderHub();
        pickerView.start(new Stage());
    }


//    // Link together the model, view and controller objects for the login window & start the interface
//    private void startLoginWindow()
//    {
//        LoginView lView = new LoginView(new Stage()); // Launches the login window
//        LoginController lController = new LoginController();
//        LoginModel lModel = new LoginModel();
//        //DatabaseRW _databaseRW = DatabaseRWFactory.createDatabaseRW();
//        //lModel.databaseRW = _databaseRW;
//
//        lView.lController = lController;
//        lController.lModel = lModel;
//        lModel.lView = lView;
//
//    }


    /**
     * The customer GUI -search prodduct, add to trolley, cancel/submit trolley,
     * view receipt
     * <p>
     * Creates the Model, View, and Controller objects, links them together so they
     * can communicate with each other. Also creates the DatabaseRW instance via the
     * DatabaseRWFactory and injects it into the CustomerModel. Starts the customer
     * interface.
     * <p>
     * Also creates the RemoveProductNotifier, which tracks the position of the
     * Customer View and is triggered by the Customer Model when needed.
     */


    // OrderTracker GUI
    // This client does not follow the MVC pattern, as it only registers with the OrderHub
    // to receive order status notifications. All logic is handled internally within the OrderTracker.
    // Initialises the orderMap<orderId, orderState> for OrderHub during system startup
    private void initializeOrderMap()
    {
        OrderHub orderHub = OrderHub.getOrderHub();
        orderHub.initializeOrderMap();
        // RemoveProductNotifier removeProductNotifier = new RemoveProductNotifier();
        // removeProductNotifier.cusView = cusView;
        // cusModel.removeProductNotifier = removeProductNotifier;
    }


    // The OrderTracker GUI - for customer to track their order's state(Ordered,
    // Progressing, Collected)
    // This client is simple and does not follow the MVC pattern, as it only
    // registers with the OrderHub
    // to receive order status notifications. All logic is handled internally within
    // the OrderTracker.
    private void startOrderTracker()
    {
        OrderTracker orderTracker = new OrderTracker();
        orderTracker.registerWithOrderHub();
    }


    // Create dependent views that need window info
//    HistoryWindow historyWindow = new HistoryWindow();
//    AlertSimulator alertSimulator = new AlertSimulator();


    /**
     * The Warehouse GUI- for warehouse staff to manage stock Initializes the
     * Warehouse client's Model, View, and Controller,and links them together for
     * communication. It also creates the DatabaseRW instance via the
     * DatabaseRWFactory and injects it into the Model. Once the components are
     * linked, the warehouse interface (view) is started.
     * <p>
     * Also creates the dependent HistoryWindow and AlertSimulator, which track the
     * position of the Warehouse window and are triggered by the Model when needed.
     * These components are linked after launching the Warehouse interface.
     */
//    private void startWarehouseClient()
//    {
//        WarehouseView view = new WarehouseView();
//        WarehouseController controller = new WarehouseController();
//        WarehouseModel model = new WarehouseModel();
//        //DatabaseRW databaseRW = DatabaseRWFactory.createDatabaseRW();
//
//        // Link controller, model, and view and start view
//        view.controller = controller;
//        controller.model = model;
//        model.view = view;
//        //model.databaseRW = databaseRW;
//        view.start(new Stage());
//
//        // create dependent views that need window info
//        HistoryWindow historyWindow = new HistoryWindow();
//        AlertSimulator alertSimulator = new AlertSimulator();
//
//        // Link after start
//        model.historyWindow = historyWindow;
//        model.alertSimulator = alertSimulator;
//        historyWindow.warehouseView = view;
//        alertSimulator.warehouseView = view;
//    }


    // starts the EmergencyExit GUI, - used to close the entire application
    // immediatelly
    private void startEmergencyExit()
    {
        EmergencyExit.getEmergencyExit();
    }

}