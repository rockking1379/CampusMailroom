package com.mailroom.common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Package 
{
	private int packageId;
	private String trackingNumber;
	private String receivedDate;
	private String emailAddress;
	private String firstName;
	private String lastName;
	private String boxOffice;
	private String stopName;
	private String courierName;
	private String userName;
	private boolean atStop;
	private boolean pickedUp;
	private String pickUpDate;
	private boolean returned;
	
	public Package(int packageId, String trackingNumber, String receivedDate,
			String emailAddress, String firstName, String lastName,
			String boxOffice, String stopName, String courierName,
			String userName, boolean atStop, boolean pickedUp, 
			String pickUpDate, boolean returned)
	{
		this.packageId = packageId;
		this.trackingNumber = trackingNumber;
		this.receivedDate = receivedDate;
		this.emailAddress = emailAddress;
		this.firstName = firstName;
		this.lastName = lastName;
		this.boxOffice = boxOffice;
		this.stopName = stopName;
		this.courierName = courierName;
		this.atStop = atStop;
		this.pickedUp = pickedUp;
		this.pickUpDate = pickUpDate;
		this.returned = returned;
	}
	
	public void setAtStop(boolean atStop)
	{
		this.atStop = atStop;
	}
	
	public void setPickedUp(boolean pickedUp)
	{
		this.pickedUp = pickedUp;
		if(pickedUp)
		{
			DateFormat format = new SimpleDateFormat("yyy-MM-dd");
			Date now = new Date();
			this.pickUpDate = format.format(now).toString();
		}
	}
	
	public int getPackageId()
	{
		return packageId;
	}
	
	public String getFullTrackingNumber()
	{
		return trackingNumber;
	}
	
	public String getTrackingNumber()
	{
		return "..." + trackingNumber.substring(trackingNumber.length() - 4, trackingNumber.length());
	}
	
	public String getDateReceived()
	{
		return receivedDate;
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
	
	public String getCourierName()
	{
		return courierName;
	}
	
	public String getUserName()
	{
		return userName;
	}
	
	public boolean isAtStop()
	{
		return atStop;
	}
	
	public boolean isPickedUp()
	{
		return pickedUp;
	}
	
	public String getDatePickedUp()
	{
		return pickUpDate;
	}
	
	public boolean isReturned()
	{
		return returned;
	}
}
