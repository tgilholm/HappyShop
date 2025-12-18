package ci553.happyshop.client.customer.basket;

import ci553.happyshop.data.repository.BasketRepository;

public class BasketModel
{
    private final BasketRepository basketRepository;

    public BasketModel(BasketRepository basketRepository)
    {
        this.basketRepository = basketRepository;
    }
}
