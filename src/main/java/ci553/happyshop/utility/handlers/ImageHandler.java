package ci553.happyshop.utility.handlers;

import ci553.happyshop.catalogue.Product;
import ci553.happyshop.utility.StorageLocation;
import javafx.scene.image.Image;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URL;

public final class ImageHandler
{
    private static final Logger logger = LogManager.getLogger();


    /**
     * Gets the location of the product image from a product
     * @param product the <code>Product</code> to get the image from
     * @return the path to the product image
     */
    private static String getImageURI(@NotNull Product product)
    {
        return StorageLocation.imageFolderPath
                .resolve(product.getImageName())
                .toAbsolutePath()
                .toUri()
                .toString();
    }


    /**
     * Gets the image associated with a product
     * @param product the <code>Product</code> to get the image from
     * @return the <code>Image</code>
     */
    public static @NotNull Image getImageFromProduct(Product product)
    {
        return new Image(getImageURI(product));
    }

    /**
     * Loads an <code>Image</code> from its file location. Never returns null-falls back to a
     * placeholder image if the loading failed.
     * @param stringUrl the location of the image
     * @return an <code>Image</code>
     */
    public static @Nullable Image loadFromString(String stringUrl)
    {

        URL url = FileHandler.parseURL(stringUrl);
        if (url != null)
        {
            try
            {
                return new Image(url.toExternalForm());


            } catch (Exception e)
            {
                logger.warn("Failed to load image, falling back to placeholder", e);

                try
                {
                    URL placeholderURL = FileHandler.parseURL("/images/imageHolder.jpg");

                    if (placeholderURL != null)
                    {
                        return new Image(placeholderURL.toExternalForm());
                    }
                } catch (NullPointerException x)
                {
                    logger.warn("Failed to load placeholder, returning empty image");
                }
            }
        } else
        {
            logger.warn("Could not find image at: {}", stringUrl);
        }
        try
        {
            URL placeholderURL = FileHandler.parseURL("/images/imageHolder.jpg");

            if (placeholderURL != null)
            {
                return new Image(placeholderURL.toExternalForm());
            }
        } catch (NullPointerException x)
        {
            logger.warn("Failed to load placeholder, returning null");

        }
        return null;
    }
}
