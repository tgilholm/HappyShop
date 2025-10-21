package ci553.happyshop.client.picker;

import ci553.happyshop.utility.UIStyle;
import ci553.happyshop.utility.WinPosManager;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The Order Picker window is for staff to prepare customer's order.
 * It contains two root views:
 * 1. vbOrderMapRoot - the default view, displaying available orders awaiting assignment.
 * 2. vbOrderDetailRoot - displayed once a picker is assigned an order, allowing them to view
 *    and prepare the order.
 *
 * The window initially shows the orderMapRoot.
 * Once an order is assigned to a picker,the view switches to orderDetailToot.
 * The view switches to orderMapRoot for the next task after the order is prepared and collected by customer.
 */

public class PickerView
{
	public PickerController pickerController;

	private final int WIDTH = UIStyle.pickerWinWidth;
	private final int HEIGHT = UIStyle.pickerWinHeight;

	private Scene scene;
	private VBox vbOrderMapRoot;
	private VBox vbOrderDetailRoot;

	//Three controllers needs updating when program going on
	private TextArea taOrderMap = new TextArea();
	// TextArea for displaying a list of orders and their states(orderId → state)
	private TextArea taOrderDetail = new TextArea();
	// TextArea for displaying detailed information about the selected order after it is assigned to the picker.
	private Label laDetailRootTitle;
	// Label used as the title for the Order Detail section.
	// Reminds the picker not to close the window if the order hasn't been collected by the customer.

	public void start(Stage window)
	{
		vbOrderMapRoot = createOrderMapRoot();
		vbOrderDetailRoot = createOrderDetailRoot();
		scene = new Scene(vbOrderMapRoot, WIDTH, HEIGHT);
		window.setScene(scene);
		window.setTitle("🛒 HappyShop Order Picker");
		WinPosManager.registerWindow(window, WIDTH, HEIGHT); //calculate position x and y for this window
		window.show();

		// Set the window close request to prevent closing if the order is not collected
		window.setOnCloseRequest(event -> {
			if (!taOrderDetail.getText().equals(""))
			{
				event.consume(); // Prevent window from closing
				laDetailRootTitle.setText("Pls complete the order before closing.");
			}
		});
	}

	private VBox createOrderMapRoot()
	{
		Label laOrderMapRootTitle = new Label("Orders Waiting for Processing");
		laOrderMapRootTitle.setStyle(UIStyle.labelTitleStyle);

		taOrderMap.setEditable(false);
		taOrderMap.setPrefSize(WIDTH, HEIGHT - 100);
		taOrderMap.setStyle(UIStyle.textFiledStyle);

		Button btnProgressing = new Button("Progressing");
		btnProgressing.setOnAction(this::buttonClicked);
		btnProgressing.setStyle(UIStyle.buttonStyle);

		VBox vbOrdersListRoot = new VBox(15, laOrderMapRootTitle, taOrderMap, btnProgressing);
		vbOrdersListRoot.setAlignment(Pos.TOP_CENTER);
		vbOrdersListRoot.setStyle(UIStyle.rootStyleYellow);

		return vbOrdersListRoot;
	}

	private VBox createOrderDetailRoot()
	{
		laDetailRootTitle = new Label("Progressing Order Details");
		laDetailRootTitle.setStyle(UIStyle.labelTitleStyle);

		taOrderDetail.setEditable(false);
		taOrderDetail.setPrefSize(WIDTH, HEIGHT - 100);
		taOrderDetail.setText("Order details");
		taOrderDetail.setStyle(UIStyle.textFiledStyle);

		Button btnCollected = new Button("Customer Collected");
		btnCollected.setOnAction(this::buttonClicked);
		btnCollected.setStyle(UIStyle.buttonStyle);

		VBox vbOrderDetailsRoot = new VBox(15, laDetailRootTitle, taOrderDetail, btnCollected);
		vbOrderDetailsRoot.setAlignment(Pos.TOP_CENTER);
		vbOrderDetailsRoot.setStyle(UIStyle.rootStyleBlue);

		return vbOrderDetailsRoot;
	}

	private void buttonClicked(ActionEvent event)
	{
		Button button = (Button) event.getSource();
		String btnText = button.getText();
		try
		{
			// Based on the button's text, performs the appropriate action and switches the displayed root.
			switch (btnText)
			{
			case "Progressing":
				scene.setRoot(vbOrderDetailRoot); // switch to OrderDetailRoot
				pickerController.doProgressing();
				break;

			case "Customer Collected":
				pickerController.doCollected();
				scene.setRoot(vbOrderMapRoot); // switch back to orderMapRoot
				break;
			}
		} catch (IOException e)
		{
			throw new RuntimeException("Failed to handle button action: " + btnText, e);
		}
	}

	void update(String strOrderMap, String strOrderDetail)
	{
		taOrderMap.setText(strOrderMap);
		taOrderDetail.setText(strOrderDetail);
		laDetailRootTitle.setText("Progressing Order Details");
	}
}
