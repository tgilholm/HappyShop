package ci553.happyshop.utility;

import ci553.happyshop.catalogue.Product;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

// ItemCellFactory class to create custom ListCells for Product items
public class ProductCellFactory implements Callback<ListView<Product>, ListCell<Product>>
{

	
	// TODO Load xml


	
	// Override the existing ListCell method to create custom cells
	@Override
	public ListCell<Product> call(ListView<Product> param)
	{
		return new ProductCell();
	}

}
