package ci553.happyshop.catalogue;

import ci553.happyshop.utility.enums.UserType;

/**
 * Record class to carry the username and password of a customer
 * @param username a <code>String</code> username
 * @param password a <code>String</code> password
 */
public record LoginCredentials(String username, String password, UserType userType)
{
}
