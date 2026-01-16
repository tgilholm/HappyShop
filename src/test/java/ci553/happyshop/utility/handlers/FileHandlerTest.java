// language: java
package ci553.happyshop.utility.handlers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the FileHandler utility class
 */
class FileHandlerTest
{
    @Test
    @DisplayName("Using parseURl should result in a URL that is not null")
    void testParseURL()
    {
        // Use the class file path
        URL url = FileHandler.parseURL("/ci553/happyshop/utility/handlers/FileHandler.class");
        assertNotNull(url, "Existing resource should return a non-null URL");
    }

    @Test
    @DisplayName("Attempting to parse a URL on a non-existent resource should return null")
    void testParseURLWithMissingResource()
    {
        URL url = FileHandler.parseURL("/no/resource.file");
        assertNull(url, "Missing resource should return null");
    }
}
