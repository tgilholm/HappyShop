package ci553.happyshop.data.repository.types;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Defines base CRUD operations to be implemented by more specific repositories.
 * Does not implement <code>getAll</code> methods.
 *
 * @param <Type> Entity type (Product, Category, User, etc.)
 * @param <ID>   Primary key type (String, int, etc.)
 */
public interface CommonRepository<Type, ID>
{
    /**
     * Creates a new entity
     *
     * @param entity the entity to insert
     */
    void insert(@NotNull Type entity);

    /**
     * Retrieves an entity by its ID
     *
     * @param id The primary key of the entity
     * @return The entity or null
     */
    @Nullable
    Type getById(@NotNull ID id);


    /**
     * Updates an existing entity
     *
     * @param entity the entity to update
     */
    void update(@NotNull Type entity);


    /**
     * Delete an entity by its ID
     *
     * @param id The primary key of the entity
     */
    void delete(@NotNull ID id);
}
