package ci553.happyshop.catalogue;


// The class for Customer objects. Holds their username & password
public class Customer
{
	private final long id;
	private final String username;
	private final String password;
	
	public Customer(long id, String username, String password)
	{
		this.id = id;
		this.username = username;
		this.password = password;
	}
	
	public String getUsername()
	{
		return username;
	}
	
	public String getPassword()
	{
		return password;
	}

	public long getId()
	{
		return id;
	}
}
