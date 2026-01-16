// language: java
package ci553.happyshop.utility.handlers;

import ci553.happyshop.catalogue.Product;

import javafx.embed.swing.JFXPanel;
import javafx.scene.image.Image;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.net.URL;


import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the ImageHandler utility class
 */
class ImageHandlerTest
{
    @BeforeAll
    static void initialiseJFX()
    {
        // JFXPanel is a javafx toolkit that allows using images in tests
        new JFXPanel();
    }


    @Test
    @DisplayName("An image should be returned even if the parseURL operation failed in FileHandler")
    void testLoadFromString()
    {
        // Mockito can create mockups of static/final classes
        try (MockedStatic<FileHandler> fileHandlerMockedStatic = Mockito.mockStatic(FileHandler.class))
        {
            // real URL for the placeholder
            URL placeholderURL = ImageHandlerTest.class.getResource("/images/imageHolder.jpg");
            fileHandlerMockedStatic.when(() -> FileHandler.parseURL("/nonexistent.png")).thenReturn(null);
            fileHandlerMockedStatic.when(() -> FileHandler.parseURL("/images/imageHolder.jpg")).thenReturn(placeholderURL);

            Image img = ImageHandler.loadFromString("/nonexistent.png");
            assertNotNull(img, "Should return an Image even when parseURL returns null");
        }
    }


    @Test
    @DisplayName("getImageFromProduct should return a non null image")
    void testGetImageFromProduct()
    {
        // image may be empty/error but should not be null
        Product product = new Product(1L, "Test", "no-image.png", 1.0, 0, 1L);
        Image image = ImageHandler.getImageFromProduct(product);
        assertNotNull(image, "getImageFromProduct should return a non-null Image");
    }
}
