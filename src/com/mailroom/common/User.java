package com.mailroom.common;

public class User 
{
	private int userId;
	private String userName;
	private String firstName;
	private String lastName;
	private boolean admin;
	
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
	
	public int getUserId()
	{
		return userId;
	}
	
	public String getUserName()
	{
		return userName;
	}
	
	public String getFirstName()
	{
		return firstName;
	}
	
	public String getLastName()
	{
		return lastName;
	}
	
	public String getName()
	{
		return lastName + ", " + firstName;
	}
	
	public boolean getAdmin()
	{
		return admin;
	}
}
