package ci553.happyshop.client.customer;

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

	@Override
	public void start(Stage window)
	{
		startCustomerClient(window);

		//RemoveProductNotifier removeProductNotifier = new RemoveProductNotifier();
		//removeProductNotifier.cusView = cusView;
		//cusModel.removeProductNotifier = removeProductNotifier;
	}

	/**
	 * Gets repository instances and injects dependencies into the Model, View, and Controller
	 * @param window the <code>Stage</code> object to run
	 */
	public static void startCustomerClient(Stage window)
	{
		logger.info("Launching CustomerClient");

		// Get instances of the repositories needed
		ProductRepository productRepository = RepositoryFactory.getProductRepository();
		CategoryRepository categoryRepository = RepositoryFactory.getCategoryRepository();
		BasketRepository basketRepository = RepositoryFactory.getBasketRepository();

		CustomerModel cusModel = new CustomerModel(productRepository, categoryRepository, basketRepository);
		CustomerController cusController = new CustomerController(cusModel);
		CustomerView cusView = new CustomerView(cusController);
		cusView.start(window);
	}
}
