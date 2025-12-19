package ci553.happyshop.utility;

import ci553.happyshop.catalogue.Product;

/**
 * Declares methods called by buttons on cards (ListCell, TilePane etc)
 */
public interface ButtonActionCallback
{
    void onAddItem(Product product);        // Add an item to the basket

    void onRemoveItem(Product product);     // Remove the item from the basket

    int getBasketQuantity(Product product); // Get the amount of the item in the basket
}