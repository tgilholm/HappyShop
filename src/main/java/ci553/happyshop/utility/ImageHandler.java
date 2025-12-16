package ci553.happyshop.utility;

import ci553.happyshop.catalogue.Product;
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;

public final class ImageHandler
{
    private static String getImageURI(@NotNull Product product)
    {
        return StorageLocation.imageFolderPath
                .resolve(product.getProductImageName())
                .toAbsolutePath()
                .toUri()
                .toString();
    }


    public static @NotNull Image getImageFromProduct(Product product)
    {
        return new Image(getImageURI(product));
    }
}
