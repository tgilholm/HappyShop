package ci553.happyshop.utility.handlers;

import javafx.scene.control.Label;

public final class StockDisplayHelper
{
    /**
     * Sets the text colour of a <code>label</code> to red, orange, or green depending on <code>stockQuantity</code>
     * @param label a label element
     * @param stockQuantity an integer to calculate the text colour with
     */
    public static void updateStockLabel(Label label, int stockQuantity)
    {
        if (stockQuantity == 0)
        {
            // Red "out of stock" if none left
            label.setStyle("-fx-text-fill: red;");
            label.setText("Out of Stock");
        } else if (stockQuantity < 10)
        {
            // Orange "... left" if only a few left
            label.setStyle("-fx-text-fill: orange;");
            label.setText(stockQuantity + " left");
        } else
        {
            // Green "... in stock" if plentiful
            label.setStyle("-fx-text-fill: green;");
            label.setText(stockQuantity + " in stock");
        }
    }
}
