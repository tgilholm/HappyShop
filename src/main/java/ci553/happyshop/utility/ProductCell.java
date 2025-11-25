package ci553.happyshop.utility;

import java.io.IOException;

import ci553.happyshop.catalogue.Product;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;

public class ProductCell extends ListCell<Product>
{
	@FXML
	private Label label1;
	
	@FXML 
	private AnchorPane anchorPane;
	
	protected void updateItem(Product product, Boolean empty)
	{
		super.updateItem(product, empty);
		System.out.println("not empty");

		// TODO set layout with xml

		if (!empty)
		{
			System.out.println("not empty");

			// Load the fxml

			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/layout/list_cell_layout.fxml"));
			fxmlLoader.setController(this);
			
			try
			{
				fxmlLoader.load();
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			setText(product.getProductDescription());
			
			// Set the layout elements
			label1.setText(product.getProductDescription());
			setGraphic(anchorPane);
		} else
		{
			setText("empty");
			System.out.println("empty");
		}
	}

}
