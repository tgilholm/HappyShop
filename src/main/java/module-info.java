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
	requires java.smartcardio;

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
	exports ci553.happyshop.data.repository;
	exports ci553.happyshop.service;
	exports ci553.happyshop.client;
	exports ci553.happyshop.utility;
	exports ci553.happyshop.catalogue;
	exports ci553.happyshop.client.customer;
	exports ci553.happyshop.client.customer.basket;
	exports ci553.happyshop.client.orderTracker;
	exports ci553.happyshop.client.emergency;
	exports ci553.happyshop.client.login;
	exports ci553.happyshop.systemSetup;
	exports ci553.happyshop.catalogue.DTO;
	exports ci553.happyshop.data.repository.types;
	exports ci553.happyshop.utility.enums;
	opens ci553.happyshop.utility.enums to javafx.fxml;
    exports ci553.happyshop.utility.listCell;
    opens ci553.happyshop.utility.listCell to javafx.fxml;
	exports ci553.happyshop.utility.handlers;
	opens ci553.happyshop.utility.handlers to javafx.fxml;
    opens ci553.happyshop.catalogue to javafx.fxml;
	exports ci553.happyshop.data;

}