package ci553.happyshop.data.repository.types;

import java.util.List;

/**
 * Designed to be used with CommonRepository. Defines one method- <code>getAll()</code>.
 * This is to avoid repositories implementing methods they will not use.
 */
public interface ListableRepository<Type>
{
    /**
     * Retrieve all entities of the type <code>Type</code>
     * @return List of all entities
     */
    List<Type> getAll();
}
