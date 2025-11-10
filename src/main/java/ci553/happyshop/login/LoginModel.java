package ci553.happyshop.login;

import java.sql.SQLException;

import ci553.happyshop.storageAccess.DatabaseRW;
import ci553.happyshop.storageAccess.DatabaseRWFactory;
import javafx.stage.Stage;
import ci553.happyshop.client.customer.*;
import ci553.happyshop.client.warehouse.*;

/**
 * The LoginModel class provides the functionality for users and warehouse staff to log into the application
 * It checks the database to see if the user already has an account, and if not, asks them to create one.
 *
 *
 * TODO add option to go back to login screen
 *
 * @author Thomas Gilholm, University of Brighton
 * @version 1.0
 */
public class LoginModel
{
	// TODO refactor to handle both popups with one method each
	// LoginModel connects to the LoginView, which displays GUI items to the user
	public LoginView lView;
	public DatabaseRW databaseRW;

	private LoginPopup cusLoginPopup, warLoginPopup; 
	

	public void openCusLoginWindow()
	{
		// Opens either a customer or warehouse login window
		cusLoginPopup = new CustomerLoginPopup(lView, this);
		cusLoginPopup.showLoginWindow(); 
	}
	
	public void openWarLoginWindow()
	{
		// Opens either a customer or warehouse login window
		warLoginPopup = new WarehouseLoginPopup(lView, this);
		warLoginPopup.showLoginWindow(); 
	}
	
	
	// Customer login method. Checks the login table and opens the customer window if correct
	// Contact the database & check login details
	public void cusLogin(String username, String password) throws SQLException
	{
		if (databaseRW.checkLoginDetails(username, password))
		{
			// Handle what happens when the user is logged in successfully
			System.out.println(String.format("Logging in... username: %s, password: %s", 
					username, password));
			
			// Invoke the startCustomerClient() and close the LoginPopup and LoginView
			startCustomerClient();
			cusLoginPopup.closePopup();
			lView.hideWindow();	// Hide is used instead of close so we can return to it later
		}
		else {
			System.out.print(String.format("Login failed... username: %s, password: %s", 
					username, password));
		}
	}
	
	// Warehouse login method. Checks the login table and opens the customer window if correct
	// Contact the database & check login details
	public void warLogin(String username, String password) throws SQLException
	{
		if (databaseRW.checkLoginDetails(username, password))
		{
			// Handle what happens when the user is logged in successfully
			System.out.println(String.format("Logging in... username: %s, password: %s", 
					username, password));
			
			// Invoke the startCustomerClient() and close the LoginPopup and LoginView
			startWarehouseClient();
			warLoginPopup.closePopup();
			lView.hideWindow();	// Hide is used instead of close so we can return to it later
		}
		else {
			System.out.print(String.format("Login failed... username: %s, password: %s", 
					username, password));
		}
	}


	
	// Open the customer client window when the user is logged in
	private void startCustomerClient()
	{
		// Initialise Model, View, Controller
		CustomerView cusView = new CustomerView();
		CustomerController cusController = new CustomerController();
		CustomerModel cusModel = new CustomerModel();
		DatabaseRW databaseRW = DatabaseRWFactory.createDatabaseRW();

		cusView.cusController = cusController;
		cusController.cusModel = cusModel;
		cusModel.cusView = cusView;
		cusModel.databaseRW = databaseRW;
		cusView.start(new Stage());
	}
	
	
	// Open the warehouse client window on login
	private void startWarehouseClient()
	{
		WarehouseView view = new WarehouseView();
		WarehouseController controller = new WarehouseController();
		WarehouseModel model = new WarehouseModel();
		DatabaseRW databaseRW = DatabaseRWFactory.createDatabaseRW();

		// Link controller, model, and view and start view
		view.controller = controller;
		controller.model = model;
		model.view = view;
		model.databaseRW = databaseRW;
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
	
//	System.out.println(String.format("Logging in to customer %s, username: %s, password: %s", 
//			i.getID(), i.getUsername(), i.getPassword()));
	
	
	
	
}
