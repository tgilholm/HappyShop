package ci553.happyshop.catalogue;


import ci553.happyshop.utility.UserType;

/**
 * The class holding users' passwords, usernames, and their userType.
 * UserType can be either CUSTOMER or STAFF. Staff can access the entire system and customers
 * can only access the frontend.
 */
public record User(long id, String username, String password, UserType userType)
{
}
