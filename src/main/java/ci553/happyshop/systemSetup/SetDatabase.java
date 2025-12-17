package ci553.happyshop.systemSetup;

import ci553.happyshop.storageAccess.DatabaseRWFactory;
import ci553.happyshop.utility.StorageLocation;

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
    // Create the database if it doesn't exist
    private static final String dbURL = DatabaseRWFactory.dbURL + ";create=true";

    private static Path imageWorkingFolderPath = StorageLocation.imageFolderPath;
    private static Path imageBackupFolderPath = StorageLocation.imageResetFolderPath;

    // Array of table names
    private String[] tables =
            {"ProductTable", "LoginTable", "BasketTable", "CategoryTable"};

    // Table Creation SQL
    private static final String[] CREATE_TABLES = {

            // Each table has a BIGINT primary key that is automatically generated
            """
            CREATE TABLE CategoryTable (
                id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                name VARCHAR(50) NOT NULL UNIQUE,
                description VARCHAR(255),
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
                userID BIGINT NOT NULL,
                productID BIGINT NOT NULL,
                quantity INT NOT NULL DEFAULT 0,
                dateAdded TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                PRIMARY KEY (userID, productID),
                FOREIGN KEY (userID) REFERENCES LoginTable(id) ON DELETE CASCADE,
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
    // IDs do not need to be specified because they are automatically generated
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
        SetDatabase setDB = new SetDatabase();
        setDB.clearTables();
        setDB.initializeTable();
        setDB.queryTableAfterInitilization();
        deleteFilesInFolder(imageWorkingFolderPath);
        copyFolderContents(imageBackupFolderPath, imageWorkingFolderPath);

    }

    // Reset tables
    private void clearTables() throws SQLException
    {
        try (Connection con = DriverManager.getConnection(dbURL); Statement statement = con.createStatement())
        {
            System.out.println("Database happyShopDB is connected successfully!");
            for (String table : tables)
            {
                try
                {
                    // Try to drop table directly
                    statement.executeUpdate("DROP TABLE " + table.toUpperCase());
                    System.out.println("Dropped table: " + table);
                } catch (SQLException e)
                {
                    if ("42Y55".equals(e.getSQLState()))
                    { // 42Y55 = Table does not exist
                        System.out.println("Table " + table + " does not exist. Skipping...");
                    }
                }
            }
        } finally
        {
        }
    }

    // Creates tables and initializes them with data
    private void initializeTable() throws SQLException
    {
        // Run the SQL statements
        try (Connection connection = DriverManager.getConnection(dbURL))
        {
            System.out.println("Database happyShopDB is created successfully!");
            connection.setAutoCommit(false); // Disable auto-commit for the batch

            try (Statement statement = connection.createStatement())
            {
                // First, create the table (DDL) - Execute this one separately from DML
                statement.executeUpdate(iniTableSQL[0]); // Execute Create Table SQL

                // Prepare and execute the insert operations (DML)
                for (int i = 1; i < iniTableSQL.length; i++)
                {
                    statement.addBatch(iniTableSQL[i]); // Add insert queries to batch
                }

                // Execute all the insert statements in the batch
                statement.executeBatch();
                connection.commit(); // Commit the transaction if everything was successful

                System.out.println("Table and data initialized successfully.");

            } catch (SQLException e)
            {
                connection.rollback(); // Rollback the transaction in case of an error
                System.err.println("Transaction rolled back due to an error!");
                e.printStackTrace();
            }
        } finally
        {
        }
    }


    // Output all the data in the tables after initialising them
    private void queryTableAfterInitilization() throws SQLException
    {
        // Query ProductTable
        String sqlQuery = "SELECT * FROM ProductTable";

        System.out.println("------------- ProductTable -----------------");
        String title = String.format("%-12s %-20s %-10s %-10s %s", "productID", "description", "unitPrice", "inStock",
                "image");
        System.out.println(title); // Print formatted output


        try (Connection connection = DriverManager.getConnection(dbURL); Statement stat = connection.createStatement())
        {
            ResultSet resultSet = stat.executeQuery(sqlQuery);
            while (resultSet.next())
            {
                String productID = resultSet.getString("productID");
                String description = resultSet.getString("description");
                double unitPrice = resultSet.getDouble("unitPrice");
                String image = resultSet.getString("image");
                int inStock = resultSet.getInt("inStock");
                String record = String.format("%-12s %-20s %-10.2f %-10d %s", productID, description, unitPrice,
                        inStock, image);
                System.out.println(record); // Print formatted output
            }


            sqlQuery = "SELECT * From LoginTable";
            System.out.println("------------- LoginTable -----------------");

            try
            {
                resultSet = stat.executeQuery(sqlQuery);
                while (resultSet.next())
                {
                    String userID = resultSet.getString("userID");
                    String username = resultSet.getString("username");
                    String password = resultSet.getString("password");
                    String record = String.format("%s %20s %20s", userID, username,
                            password);
                    System.out.println(record); // Print formatted output
                }
            } finally
            {

            }
        }
    }

    // Recursively deletes all files in a folder
    public static void deleteFilesInFolder(Path folder) throws IOException
    {
        if (Files.exists(folder))
        {

            try
            {
                Files.walkFileTree(folder, new SimpleFileVisitor<>()
                {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException
                    {
                        Files.delete(file); // delete individual files
                        return FileVisitResult.CONTINUE;
                    }
                });
                System.out.println("Deleted files in folder: " + folder);
            } finally
            {

            }
        } else
        {
            System.out.println("Folder " + folder + " does not exist");
        }
    }

    /**
     * The method Files.walkFileTree(Path, FileVisitor) traverses (or "walks
     * through") a directory and all of its subdirectories. It accepts two
     * arguments: 1. directory (Path or folder) path from which the traversal begins
     * (the starting point of the walk). 2. A FileVisitor object that defines the
     * actions to be performed when a file or directory is visited. The visitor is
     * an instance of the FileVisitor interface, which provides methods for handling
     * different events during the traversal.
     * <p>
     * Here, we use an anonymous class to create the second argument - the instance
     * (object) – An anonymous class allows you to extend a superclass (or implement
     * an interface) and instantiate it in a single, concise step, without needing
     * to define a separate named class. It combines both class extension and object
     * creation into one operation, typically used when you need a one-off
     * implementation of a class or interface. (Note: the object is the anonymous
     * class's)
     * <p>
     * We did not use Files.walkFileTree(folder, new FileVisitor<>()) because
     * FileVisitor is an interface, and we would need to implement all of its
     * methods ourselves. Instead, we use Files.walkFileTree(folder, new
     * SimpleFileVisitor<>()) because: - SimpleFileVisitor<> is an abstract class
     * that implements the FileVisitor interface with default method
     * implementations. - We only need to override the methods (visitFile,
     * postVisitDirectory) that we're interested in, which simplifies our code.
     */

    // Copies all files from source folder to destination folder
    public static void copyFolderContents(Path source, Path destination) throws IOException
    {

        if (!Files.exists(source))
        {
            throw new IOException("Source folder does not exist: " + source);
        }

        // Create destination folder if it doesn't exist
        if (!Files.exists(destination))
        {
            Files.createDirectories(destination);
        }

        // Copy files from source folder to destination folder
        // Files.newDirectoryStream(source): list all entries (files and folders)
        // directly in the source directory
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(source))
        {
            for (Path file : stream)
            {
                if (Files.isRegularFile(file))
                {
                    Path targetFile = destination.resolve(file.getFileName());
                    Files.copy(file, targetFile, StandardCopyOption.REPLACE_EXISTING);
                }
            }
        } finally
        {

        }
        System.out.println("Copied files from: " + source + " → " + destination);
    }

}
