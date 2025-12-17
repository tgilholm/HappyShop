package ci553.happyshop.data.repository;

import ci553.happyshop.catalogue.Category;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface CategoryRepository extends CommonRepository<Category, Long>
{

    /**
     * Gets the list of all <code>Category</code> entities
     *
     * @return the <code>List</code> of categories
     */
    List<Category> getAll();


    /**
     * Gets a specific <code>Category</code> by its ID
     *
     * @param id The primary key of the category
     * @return the category selected, or null
     */
    @Nullable
    Category getById(@NotNull Long id);


    /**
     * Adds a new <code>Category</code> to the table
     *
     * @param category the category to insert
     */
    void insert(@NotNull Category category);


    /**
     * Updates the details of an existing <code>Category</code> in the table
     *
     * @param category the category to update
     */
    void update(@NotNull Category category);


    /**
     * Removes a <code>Category</code> from the table
     *
     * @param id The primary key of the category
     */
    void delete(@NotNull Long id);
}
