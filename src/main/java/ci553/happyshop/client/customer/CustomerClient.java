package ci553.happyshop.client.customer;

import ci553.happyshop.catalogue.Category;
import ci553.happyshop.data.repository.BasketRepository;
import ci553.happyshop.data.repository.CategoryRepository;
import ci553.happyshop.data.repository.ProductRepository;
import ci553.happyshop.data.repository.RepositoryFactory;
import javafx.application.Application;
import javafx.stage.Stage;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * A stand-alone Customer Client that can be run independently without launching the full system.
 * Designed for early-stage testing, though full functionality may require other clients to be active.
 */

public class CustomerClient extends Application
{
	private static final Logger logger = LogManager.getLogger();

	public static void main(String[] args)
	{
		launch(args);
	}

	/**
	 * Injects dependencies into the MVC and launches the customer view
	 */
	@Override
	public void start(Stage window)
	{
		logger.info("Launching CustomerClient");
		// Starts the view, model and controller for the customer client and connects to the database read/writer

		// Get instances of the repositories needed
		ProductRepository productRepository = RepositoryFactory.getProductRepository();
		CategoryRepository categoryRepository = RepositoryFactory.getCategoryRepository();
		BasketRepository basketRepository = RepositoryFactory.getBasketRepository();

		CustomerModel cusModel = new CustomerModel(productRepository, categoryRepository, basketRepository);
		CustomerController cusController = new CustomerController(cusModel);
		CustomerView cusView = new CustomerView(cusController);
		cusView.start(window);

		//RemoveProductNotifier removeProductNotifier = new RemoveProductNotifier();
		//removeProductNotifier.cusView = cusView;
		//cusModel.removeProductNotifier = removeProductNotifier;
	}
}
