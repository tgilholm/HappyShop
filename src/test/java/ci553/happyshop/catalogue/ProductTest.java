package ci553.happyshop.catalogue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests Product methods
 */
class ProductTest
{

    @Test
    @DisplayName("Products with different data should not be equal to one another")
    void testCompareTo()
    {
        // The compareTO method compares by ID primarily
        Product p1 = new Product(1L, "A", "a.png", 1.0, 1, 1L);
        Product p2 = new Product(2L, "B", "b.png", 2.0, 2, 1L);
        Product p1Copy = new Product(1L, "A2", "a2.png", 1.5, 3, 1L);

        assertEquals(-1, p1.compareTo(p2));
        assertEquals(1, p2.compareTo(p1));
        assertEquals(0, p1.compareTo(p1Copy));
    }


    @Test
    @DisplayName("Test that a Product string is formatted correctly")
    void testToString()
    {
        Product p = new Product(5L, "HappyShop", "HappyShop.png", 9.99, 3, 10L);
        String s = p.toString();
        assertTrue(s.contains("Id: 5"));
        assertTrue(s.contains("£9.99"));
        assertTrue(s.contains("stock: 3"));
        assertTrue(s.contains("HappyShop"));
    }
}