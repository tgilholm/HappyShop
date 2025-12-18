package ci553.happyshop.client;


import ci553.happyshop.client.customer.*;
import ci553.happyshop.client.warehouse.*;
import ci553.happyshop.storageAccess.DatabaseRW;
import ci553.happyshop.storageAccess.DatabaseRWFactory;
import javafx.stage.Stage;


// Class to contain all the implementations for opening windows
// This allows the code to be re-used across multiple classes
public class OpenWindows
{
	// Open the customer client window when the user is logged in
//	public void startCustomerClient()
//	{
//		// Initialise Model, View, Controller
//		CustomerView cusView = new CustomerView();
//		CustomerController cusController = new CustomerController();
//		CustomerModel cusModel = new CustomerModel();
//		DatabaseRW databaseRW = DatabaseRWFactory.createDatabaseRW();
//
//		cusView.cusController = cusController;
//		cusController.cusModel = cusModel;
//		cusModel.cusView = cusView;
//		cusModel.databaseRW = databaseRW;
//		cusView.start(new Stage());
//	}
	
	// Open the warehouse client window on login
	public void startWarehouseClient()
	{
		WarehouseView view = new WarehouseView();
		WarehouseController controller = new WarehouseController();
		WarehouseModel model = new WarehouseModel();
		//DatabaseRW databaseRW = DatabaseRWFactory.createDatabaseRW();

		// Link controller, model, and view and start view
		view.controller = controller;
		controller.model = model;
		model.view = view;
		//model.databaseRW = databaseRW;
		view.start(new Stage());

		// create dependent views that need window info
		HistoryWindow historyWindow = new HistoryWindow();
		AlertSimulator alertSimulator = new AlertSimulator();

		// Link after start
		model.historyWindow = historyWindow;
		model.alertSimulator = alertSimulator;
		historyWindow.warehouseView = view;
		alertSimulator.warehouseView = view;
	}
}
