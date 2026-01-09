module ci553.happyshop
{
	requires javafx.controls;
	requires javafx.fxml;
	requires java.sql;
	requires javafx.graphics;
	requires javafx.base;
	requires java.base;
	requires java.xml;
    requires org.jetbrains.annotations;
	requires java.desktop;
    requires org.apache.logging.log4j;

    opens ci553.happyshop to javafx.fxml;
	opens ci553.happyshop.client to javafx.fxml;
	opens ci553.happyshop.client.customer.basket to javafx.fxml;
    opens ci553.happyshop.utility to javafx.fxml;
	opens ci553.happyshop.base_mvm to javafx.fxml;
	opens ci553.happyshop.client.customer;
	opens ci553.happyshop.client.picker;
	opens ci553.happyshop.client.orderTracker;
	opens ci553.happyshop.client.warehouse;
	opens ci553.happyshop.client.emergency;

	exports ci553.happyshop;
	exports ci553.happyshop.data.database;
	exports ci553.happyshop.data.repository;
	exports ci553.happyshop.domain.service;
	exports ci553.happyshop.client;
	exports ci553.happyshop.utility;
	exports ci553.happyshop.catalogue;
	exports ci553.happyshop.client.customer;
	exports ci553.happyshop.client.customer.basket;
	exports ci553.happyshop.client.orderTracker;
	exports ci553.happyshop.client.emergency;
	exports ci553.happyshop.systemSetup;
    exports ci553.happyshop.storageAccess;
	exports ci553.happyshop.catalogue.DTO;

}