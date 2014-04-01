package com.mailroom.common;

public class Person 
{
	private String idNumber;
	private String emailAddress;
	private String firstName;
	private String lastName;
	private String boxOffice;
	private String stopName;
	
	public Person(String idNumber, String emailAddress, String firstName, 
			String lastName, String boxOffice, String stopName)
	{
		this.idNumber = idNumber;
		this.emailAddress = emailAddress;
		this.firstName = firstName;
		this.lastName = lastName;
		this.boxOffice = boxOffice;
		this.stopName = stopName;
	}
	
	public String getIdNumber()
	{
		return idNumber;
	}
	
	public String getEmailAddress()
	{
		return emailAddress;
	}
	
	public String getFirstName()
	{
		return firstName;
	}
	
	public String getLastName()
	{
		return lastName;
	}
	
	public String getBoxOffice()
	{
		return boxOffice;
	}
	
	public String getStopName()
	{
		return stopName;
	}
}
