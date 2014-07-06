package com.mailroom.common;

/**
 * Represents A User
 * <br>
 * Used for Checking In Packages, Settings Permissions, etc.
 * @author James sitzja@grizzlies.adams.edu
 *
 */
public class User 
{
	private int userId;
	private String userName;
	private String firstName;
	private String lastName;
	private boolean admin;
	
	/**
	 * Constructs New User
	 * @param userId ID of User in Database
	 * @param userName Username of User
	 * @param firstName First Name of User
	 * @param lastName Last Name of User
	 * @param admin Administrative status of User
	 */
	public User(int userId, String userName, String firstName, 
			String lastName, boolean admin)
	{
		this.userId = userId;
		this.userName = userName;
		this.firstName = firstName;
		this.lastName = lastName;
		this.admin = admin;
	}
	
	//All get methods for variables
	
	/**
	 * Gets ID of User
	 * @return ID of User
	 */
	public int getUserId()
	{
		return userId;
	}
	
	/**
	 * Gets Username of User
	 * @return Username of User
	 */
	public String getUserName()
	{
		return userName;
	}
	
	/**
	 * Gets First Name of User
	 * @return First Name of User
	 */
	public String getFirstName()
	{
		return firstName;
	}
	
	/**
	 * Gets Last Name of User
	 * @return Last Name of User
	 */
	public String getLastName()
	{
		return lastName;
	}
	
	/**
	 * Gets First and Last Name added together
	 * @return 'Last Name', 'First Name'
	 */
	public String getName()
	{
		return lastName + ", " + firstName;
	}
	
	/**
	 * Gets Admin Status of User
	 * @return Wether User is an Administrator or not
	 */
	public boolean getAdmin()
	{
		return admin;
	}
	
	@Override
	public String toString()
	{
		return userName;
	}
}
