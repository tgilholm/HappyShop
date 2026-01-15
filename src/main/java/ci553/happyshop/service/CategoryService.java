package ci553.happyshop.service;

import ci553.happyshop.catalogue.Category;

import java.util.List;

public interface CategoryService
{

    /**
     * Gets the list of all categories
     * @return a list of <code>Category</code> objects
     */
    List<Category> getAll();
}
