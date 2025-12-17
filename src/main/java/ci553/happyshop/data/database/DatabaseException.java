package ci553.happyshop.data.database;

/**
 * Extends RuntimeException to provide more detail to SQLExceptions
 */
public class DatabaseException extends RuntimeException
{
    public DatabaseException(String message) {super(message);}

    public DatabaseException(String message, Throwable cause) {super(message, cause);}
}
