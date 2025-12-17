package ci553.happyshop.data.repository.impl;

import ci553.happyshop.data.database.DatabaseConnection;
import ci553.happyshop.data.repository.BasketRepository;

public class BasketRepositoryImpl implements BasketRepository
{
    private final DatabaseConnection dbConnection;

    public BasketRepositoryImpl(DatabaseConnection dbConnection)
    {
        this.dbConnection = dbConnection;
    }
}
