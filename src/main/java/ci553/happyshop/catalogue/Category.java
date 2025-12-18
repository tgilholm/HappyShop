package ci553.happyshop.catalogue;

/**
 * Holds information about a category
 */
public class Category
{
    private final long id;
    private final String name;
    private final String description;


    /**
     * Constructs a new <code>Category</code> object
     * @param id the unique <code>long</code> id
     * @param name a <code>String</code> name for the category
     * @param description a longer <code>String</code> description
     */
    public Category(long id, String name, String description)
    {
        this.id = id;
        this.name = name;
        this.description = description;
    }
}
