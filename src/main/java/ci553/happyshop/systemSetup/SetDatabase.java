package ci553.happyshop.systemSetup;

import ci553.happyshop.data.database.DatabaseConnection;
import ci553.happyshop.data.database.DatabaseException;
import ci553.happyshop.storageAccess.DatabaseRWFactory;
import ci553.happyshop.utility.StorageLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.*;

/**
 * The setDB class is responsible for resetting the database when the system is
 * first initialized. This class performs operations that delete and recreate
 * the database tables, as well as insert default values for a fresh start.
 * Ensuring that everything is properly set up for the fresh database state
 * <p>
 * WARNING: This class should only be used once when starting the system for the
 * first time. It will wipe all current data in the database and replace it with
 * a fresh, predefined structure and data.
 * <p>
 * Key operations: 1. Deletes all existing tables in the database. 2. Recreates
 * the database tables based on the initial schema. 3. Inserts default values
 * into the newly created tables. 4. Deletes all existing image files from the
 * working image folder (images/). 5. Copies all image files from the backup
 * folder (images_resetDB/) into the working image folder.
 */


public class SetDatabase
{
    private static final Logger logger = LogManager.getLogger();

    // Create the database if it doesn't exist
    private static final String dbURL = DatabaseRWFactory.dbURL + ";create=true";

    // Get a dbConnection
    private static final DatabaseConnection dbConnection = new DatabaseConnection(dbURL);

    private static final Path imageWorkingFolderPath = StorageLocation.imageFolderPath;
    private static final Path imageBackupFolderPath = StorageLocation.imageResetFolderPath;


    // Table Schema Definition
    private static final String[] CREATE_TABLES = {

            // Each table has a BIGINT primary key that is automatically generated
            """
            CREATE TABLE CategoryTable (
                id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                name VARCHAR(50) NOT NULL UNIQUE,
                description VARCHAR(255)
            )
            """,

            // Product Table has a foreign key connecting to CategoryTable
            """
            CREATE TABLE ProductTable (
                id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                name VARCHAR(100) NOT NULL,
                imageName VARCHAR(100),
                unitPrice DOUBLE NOT NULL,
                stockQuantity INT NOT NULL DEFAULT 100,
                categoryID BIGINT,
                CHECK(stockQuantity >= 0),
                FOREIGN KEY (categoryID) REFERENCES CategoryTable(id)
            )
            """,

            // LoginTable
            """
            CREATE TABLE LoginTable (
                id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                username VARCHAR(32) NOT NULL UNIQUE,
                password VARCHAR(255) NOT NULL
            )
            """,

            // BasketTable has a composite primary key of the user ID, and product ID
            // Date added is set to the current time
            """
            CREATE TABLE BasketTable (
                customerID BIGINT NOT NULL,
                productID BIGINT NOT NULL,
                quantity INT NOT NULL DEFAULT 0,
                PRIMARY KEY (customerID, productID),
                FOREIGN KEY (customerID) REFERENCES LoginTable(id) ON DELETE CASCADE,
                FOREIGN KEY (productID) REFERENCES ProductTable(id) ON DELETE CASCADE
            )
            
            """
    };
    // Default categories
    private static final String[] DEFAULT_CATEGORIES = {
            "INSERT INTO CategoryTable(name, description) VALUES ('Electronics', 'Electronic devices')",
            "INSERT INTO CategoryTable(name, description) VALUES ('Audio', 'Audio equipment')",
            "INSERT INTO CategoryTable(name, description) VALUES ('Kitchen', 'Kitchen appliances')",
            "INSERT INTO CategoryTable(name, description) VALUES ('Storage', 'Data storage')",
            "INSERT INTO CategoryTable(name, description) VALUES ('Wearables', 'Wearable devices')",

    };

    // Default products with categoryID added
    // Product IDs do not need to be specified because they are automatically generated
    private static final String[] DEFAULT_PRODUCTS = new String[]{
            "INSERT INTO ProductTable(name, imageName, unitPrice, stockQuantity, categoryID) VALUES('40 inch TV', '0001.jpg', 269.00, 100, 1)",
            "INSERT INTO ProductTable(name, imageName, unitPrice, stockQuantity, categoryID) VALUES('DAB Radio', '0002.jpg', 29.99, 100, 2)",
            "INSERT INTO ProductTable(name, imageName, unitPrice, stockQuantity, categoryID) VALUES('Toaster', '0003.jpg', 19.99, 100, 3)",
            "INSERT INTO ProductTable(name, imageName, unitPrice, stockQuantity, categoryID) VALUES('Watch', '0004.jpg', 29.99, 100, 5)",
            "INSERT INTO ProductTable(name, imageName, unitPrice, stockQuantity, categoryID) VALUES('Digital Camera', '0005.jpg', 89.99, 100, 1)",
            "INSERT INTO ProductTable(name, imageName, unitPrice, stockQuantity, categoryID) VALUES('MP3 player', '0006.jpg', 7.99, 100, 2)",
            "INSERT INTO ProductTable(name, imageName, unitPrice, stockQuantity, categoryID) VALUES('USB drive', '0007.jpg', 6.99, 100, 4)",
            "INSERT INTO ProductTable(name, imageName, unitPrice, stockQuantity, categoryID) VALUES('USB2 drive', '0008.jpg', 7.99, 100, 4)",
            "INSERT INTO ProductTable(name, imageName, unitPrice, stockQuantity, categoryID) VALUES('USB3 drive', '0009.jpg', 8.99, 100, 4)",
            "INSERT INTO ProductTable(name, imageName, unitPrice, stockQuantity, categoryID) VALUES('USB4 drive', '0010.jpg', 9.99, 100, 4)",
            "INSERT INTO ProductTable(name, imageName, unitPrice, stockQuantity, categoryID) VALUES('USB5 drive', '0011.jpg', 10.99, 100, 4)",
            "INSERT INTO ProductTable(name, imageName, unitPrice, stockQuantity, categoryID) VALUES('USB6 drive', '0012.jpg', 10.99, 100, 4)",
    };

    // Default users
    private static final String[] DEFAULT_USERS = {
            "INSERT INTO LoginTable (username, password) VALUES ('admin', 'password')"
    };

    public static void main(String[] args) throws SQLException, IOException
    {
        clearTables();          // Delete tables
        createTables();         // Add tables
        initializeTables();     // Populate tables
        deleteFilesInFolder(imageWorkingFolderPath);
        copyFolderContents(imageBackupFolderPath, imageWorkingFolderPath);
    }

    /**
     * Calls <code>DROP TABLE</code> on all tables and handles non-existent tables
     */
    private static void clearTables()
    {
        // Drop tables in reverse to avoid foreign keys being null
        String[] tables = {"BasketTable", "ProductTable", "LoginTable", "CategoryTable"};

        try (Connection connection = dbConnection.getConnection();
             Statement statement = connection.createStatement())
        {
            for (String table : tables)
            {
                try
                {
                    statement.executeUpdate("DROP TABLE " + table.toUpperCase());
                    logger.info("Dropped table {} ", table);
                } catch (SQLException e)    // throws SQLException if the table doesn't exist
                {
                    if ("42Y55".equals(e.getSQLState()))
                    { // 42Y55 = Table does not exist
                        logger.info("{} does not exist", table);
                    }
                }
            }
        } catch (SQLException e)
        {
            throw new DatabaseException("Failed to clear tables", e);
        }
    }

    /**
     * Creates new database tables from the schema
     */
    private static void createTables()
    {

        try (Connection connection = dbConnection.getConnection();
             Statement statement = connection.createStatement())
        {
            for (String createTable : CREATE_TABLES)
            {
                statement.executeUpdate(createTable);
            }
            logger.info("Created tables");

        } catch (SQLException e)
        {
            throw new DatabaseException("Failed to create new tables", e);
        }
    }


    /**
     * Initializes the tables with the pre-set data
     */
    private static void initializeTables()
    {
        try (Connection connection = dbConnection.getConnection())
        {
            try (Statement statement = connection.createStatement())
            {
                connection.setAutoCommit(false); // Disable auto-commit for the batch

                // Insert categories first, referenced by Products
                for (String sql : DEFAULT_CATEGORIES)
                {
                    statement.addBatch(sql);    // Add to the statement
                }
                statement.executeBatch();       // Execute the statements
                logger.info("Populated category table");
                statement.clearBatch();         // Reset the statement

                // Insert users
                for (String sql : DEFAULT_USERS)
                {
                    statement.addBatch(sql);
                }
                statement.executeBatch();
                logger.info("Populated user table");
                statement.clearBatch();

                // Insert products
                for (String sql : DEFAULT_PRODUCTS)
                {
                    statement.addBatch(sql);
                }
                statement.executeBatch();
                logger.info("Populated product table");
                statement.clearBatch();
                connection.commit(); // Commit the transaction if everything was successful

            } catch (SQLException e)
            {
                connection.rollback(); // Roll back the connection if there was an error
                logger.error("Failed to populate tables, rolling back {}", e.getMessage());
            }
        } catch (SQLException e)
        {
            throw new DatabaseException("Failed to add transactions", e);
        }
    }

    // Recursively deletes all files in a folder
    public static void deleteFilesInFolder(Path folder)
    {
        if (Files.exists(folder))
        {
            try
            {
                Files.walkFileTree(folder, new SimpleFileVisitor<>()
                {
                    @Override
                    public @NotNull FileVisitResult visitFile(@NotNull Path file,
                            @NotNull BasicFileAttributes attrs) throws IOException
                    {
                        Files.delete(file); // delete individual files
                        return FileVisitResult.CONTINUE;
                    }
                });

                logger.info("Deleted files in folder {}", folder);
            } catch (IOException e)
            {
                throw new DatabaseException("Failed to delete files", e);
            }
        } else
        {
            logger.info("Folder {} does not exist", folder);
        }
    }


    /**
     * Copies all the files from the <code>source</code> (backup) directory to the <code>destination</code> (working) directory
     * @param source the <code>Path</code> to the directory to copy from
     * @param destination the <code>Path</code> to the directory to copy to
     */
    public static void copyFolderContents(Path source, Path destination)
    {
        // If the source folder does not exist, throw an error
        if (!Files.exists(source))
        {
            throw new DatabaseException("Source folder does not exist: " + source);
        }
        // Copy files from the source folder to destination folder
        // Files.newDirectoryStream(source): list all entries (files and folders)
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(source))
        {

            // Create the destination folder if it doesn't exist
            if (!Files.exists(destination))
            {
                Files.createDirectories(destination);
            }


            for (Path file : stream)
            {
                if (Files.isRegularFile(file))
                {
                    Path targetFile = destination.resolve(file.getFileName());
                    Files.copy(file, targetFile, StandardCopyOption.REPLACE_EXISTING);
                }
            }

            logger.info("Copied all files from {} to {}", source, destination);
        } catch (IOException e)
        {
            throw new DatabaseException("Failed to copy files from " + source + " to " + destination);
        }
    }
}
