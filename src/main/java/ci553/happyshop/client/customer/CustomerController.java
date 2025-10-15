package ci553.happyshop.client.customer;

import java.io.IOException;
import java.sql.SQLException;

// Controller class for the customer client

public class CustomerController
{
	public CustomerModel cusModel;

	public void doAction(String action) throws SQLException, IOException
	{
		switch (action)
		{
		case "Search":
			cusModel.search();
			break;
		case "Add to Trolley":
			cusModel.addToTrolley();
			break;
		case "Cancel":
			cusModel.cancel();
			break;
		case "Check Out":
			cusModel.checkOut();
			break;
		case "OK & Close":
			cusModel.closeReceipt();
			break;
		}
	}

}
