package ci553.happyshop.catalogue;


// The class for Customer objects. Holds their username & password
public class Customer
{
	private String userID;
	private String username;
	private String password;
	
	public Customer(String uid, String u, String p)
	{
		userID = uid;
		username = u;
		password = p;
	}
	
	public String getUsername()
	{
		return username;
	}
	
	public String getPassword()
	{
		return password;
	}

	public String getID()
	{
		return userID;
	}
}
