package ci553.happyshop.data.repository.impl;

import ci553.happyshop.catalogue.Category;
import ci553.happyshop.data.repository.CategoryRepository;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CategoryRepositoryImpl implements CategoryRepository
{

    /**
     * Gets the list of all <code>Category</code> entities
     *
     * @return the <code>List</code> of categories
     */
    @Override
    public List<Category> getAll()
    {
        return List.of();
    }

    /**
     * Gets a specific <code>Category</code> by its ID
     *
     * @param id The primary key of the category
     * @return the category selected, or null
     */
    @Override
    public @Nullable Category getById(@NotNull Long id)
    {
        return null;
    }

    /**
     * Adds a new <code>Category</code> to the table
     *
     * @param category the category to insert
     */
    @Override
    public void insert(@NotNull Category category)
    {

    }

    /**
     * Updates the details of an existing <code>Category</code> in the table
     *
     * @param category the category to update
     */
    @Override
    public void update(@NotNull Category category)
    {

    }

    /**
     * Removes a <code>Category</code> from the table
     *
     * @param id The primary key of the category
     */
    @Override
    public void delete(@NotNull Long id)
    {

    }
}
