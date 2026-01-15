package ci553.happyshop.domain.service.impl;

import ci553.happyshop.catalogue.Category;
import ci553.happyshop.data.repository.CategoryRepository;
import ci553.happyshop.data.repository.RepositoryFactory;
import ci553.happyshop.domain.service.CategoryService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class CategoryServiceImpl implements CategoryService
{
    // Get repo instances
    CategoryRepository categoryRepository = RepositoryFactory.getCategoryRepository();
    Logger logger = LogManager.getLogger();

    /**
     * Gets the list of all categories
     *
     * @return a list of <code>Category</code> objects
     */
    @Override
    public List<Category> getAll()
    {
        return categoryRepository.getAll();
    }
}
