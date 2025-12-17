package ci553.happyshop.data.repository;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Defines the CRUD operations to be implemented by more specific repositories.
 * @param <Type> Entity type (Product, Category, User, etc.)
 * @param <ID> Primary key type (String, int, etc.)
 */
public interface CommonRepository<Type, ID>
{

    /**
     * Retrieve all entities of the type <code>Type</code>
     * @return List of all entities
     */
    List<Type> getAll();


    /**
     * Retrieves an entity by its ID
     * @param id The primary key of the entity
     * @return The entity or null
     */
    @Nullable
    Type getById(@NotNull ID id);


    /**
     * Inserts a new entity
     * @param entity the entity to insert
     */
    void insert(@NotNull Type entity);


    /**
     * Updates an existing entity
     * @param entity the entity to update
     */
    void update(@NotNull Type entity);


    /**
     * Delete an entity by its ID
     * @param id The primary key of the entity
     */
    void delete(@NotNull ID id);
}
