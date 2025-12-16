package ci553.happyshop.data.repository;

import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Defines the CRUD operations implemented by each of the data repositories.
 * @param <Type> Entity type (Product, Category, User, etc.)
 * @param <ID> Primary key type (String, int, etc.)
 */
public interface DataRepository<Type, ID>
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
    Type getById(ID id);


    /**
     * Inserts a new entity
     * @param entity the entity to insert
     */
    void insert(Type entity);


    /**
     * Updates an existing entity
     * @param entity the entity to update
     */
    void update(Type entity);


    /**
     * Delete an entity by its ID
     * @param id The primary key of the entity
     */
    void delete(ID id);


    /**
     * Check if an entity exists
     * @param id The primary key of the entity
     * @return true if exists, false otherwise
     */
    boolean exists(ID id);
}
