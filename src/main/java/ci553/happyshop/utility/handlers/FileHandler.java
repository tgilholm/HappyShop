package ci553.happyshop.utility.handlers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.net.URL;

/**
 * Final utility class handling file operations
 */
public final class FileHandler
{
    private FileHandler() {}    // Final class, no instantiation

    private static final Logger logger = LogManager.getLogger();

    /**
     * Attempts to parse a <code>String</code> into a <code>URL</code> object
     *
     * @param stringURL the location of a file
     * @return t
     */
    @Nullable
    public static URL parseURL(String stringURL)
    {
        logger.debug("Parsing string {} to URL", stringURL);
        URL url = FileHandler.class.getResource(stringURL);

        if (url == null)
        {
            logger.error("File not found at location {}, Ensure that locations have preceding backslashes: /fxml/FXMLFile.fxml or /css/CSSFile.css", stringURL);
            return null;
        } else
        {
            return url;
        }
    }
}
