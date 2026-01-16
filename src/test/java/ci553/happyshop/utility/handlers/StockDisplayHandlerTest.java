// language: java
package ci553.happyshop.utility.handlers;

import javafx.embed.swing.JFXPanel;
import javafx.scene.control.Label;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the StockDisplayHandler utility class
 */
class StockDisplayHandlerTest
{
    @BeforeAll
    static void initialiseJFX() {
        // initialize JFXPanel to use labels
        new JFXPanel();
    }

    @Test
    @DisplayName("The text should appear red when the product is out of stock")
    void testUpdateStockLabelNoStock()
    {
        Label label = new Label();
        StockDisplayHandler.updateStockLabel(label, 0);
        assertEquals("Out of Stock", label.getText());
        assertTrue(label.getStyle().contains("red"));
    }

    @Test
    @DisplayName("The text should appear orange when the product is low in stock")
    void testUpdateStockLabelLowStock()
    {
        Label label = new Label();
        StockDisplayHandler.updateStockLabel(label, 5);
        assertEquals("5 left", label.getText());
        assertTrue(label.getStyle().contains("orange"));
    }

    @Test
    @DisplayName("The text should appear green when there is plenty in stock")
    void testUpdateStockLabelEnoughStock()
    {
        Label label = new Label();
        StockDisplayHandler.updateStockLabel(label, 50);
        assertEquals("50 in stock", label.getText());
        assertTrue(label.getStyle().contains("green"));
    }
}
