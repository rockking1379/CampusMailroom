package com.mailroom.common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Represents A Received Package
 * @author James sitzja@grizzlies.adams.edu
 *
 */
public class Package 
{
	private int packageId;
	private String trackingNumber;
	private String receivedDate;
	private String emailAddress;
	private String firstName;
	private String lastName;
	private String boxOffice;
	private Stop stop;
	private Courier courier;
	private User user;
	private boolean atStop;
	private boolean pickedUp;
	private String pickUpDate;
	private boolean returned;
	
	/**
	 * Constructs a new Package
	 * @param packageId id of package(-1 for one to be inserted)
	 * @param trackingNumber tracking number of package
	 * @param receivedDate Date package was scanned into system
	 * @param emailAddress email address of recipient
	 * @param firstName first name of recipient
	 * @param lastName last name of recipient
	 * @param boxOffice box/office number of recipient
	 * @param stop Stop package to be delivered to
	 * @param courier Courier who brought the package
	 * @param user User who scanned it in
	 * @param atStop wether package is at its stop waiting for pick up or not
	 * @param pickedUp wether package has been received by recipient or not
	 * @param pickUpDate Date package was picked up by recipient
	 * @param returned wether package was returned
	 */
	public Package(int packageId, String trackingNumber, String receivedDate,
			String emailAddress, String firstName, String lastName,
			String boxOffice, Stop stop, Courier courier,
			User user, boolean atStop, boolean pickedUp, 
			String pickUpDate, boolean returned)
	{
		this.packageId = packageId;
		this.trackingNumber = trackingNumber;
		this.receivedDate = receivedDate;
		this.emailAddress = emailAddress;
		this.firstName = firstName;
		this.lastName = lastName;
		this.boxOffice = boxOffice;
		this.stop = stop;
		this.courier = courier;
		this.user = user;
		this.atStop = atStop;
		this.pickedUp = pickedUp;
		this.pickUpDate = pickUpDate;
		this.returned = returned;
	}
	
	/**
	 * Sets Stop package is to be delivered to
	 * @param s new Stop to be delivered to
	 */
	public void setStop(Stop s)
	{
		this.stop = s;
	}
	
	/**
	 * Sets Courier who brought the package
	 * @param c Courier who brought the package
	 */
	public void setCourier(Courier c)
	{
		this.courier = c;
	}
	
	/**
	 * Sets wether Package is at Stop or not
	 * @param atStop wether package is at stop or not
	 */
	public void setAtStop(boolean atStop)
	{
		this.atStop = atStop;
	}
	
	/**
	 * Sets wether Package is picked up or not
	 * @param pickedUp wether package is picked up or not
	 */
	public void setPickedUp(boolean pickedUp)
	{
		this.pickedUp = pickedUp;
		if(pickedUp)
		{
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date now = new Date();
			this.pickUpDate = format.format(now).toString();
		}
	}
	
	/**
	 * Gets Package ID
	 * @return package ID
	 */
	public int getPackageId()
	{
		return packageId;
	}
	
	/**
	 * Gets Full Tracking Number
	 * @return full package Tracking Number
	 */
	public String getFullTrackingNumber()
	{
		return trackingNumber;
	}
	
	/**
	 * Gets last four digits of tracking number with ... in front
	 * @return ...xxxx
	 */
	public String getTrackingNumber()
	{
		String strReturn = null;
		
		if(trackingNumber.length() < 4)
		{
			strReturn = "";
		}
		if(trackingNumber.length() == 4)
		{
			strReturn = "..." + trackingNumber;
		}
		if(trackingNumber.length() > 4)
		{
			strReturn = "..." + trackingNumber.substring(trackingNumber.length() - 4, trackingNumber.length());
		}
		
		return strReturn;
	}
	
	/**
	 * Gets Date Package was scanned into system
	 * @return Date Received
	 */
	public String getReceivedDate()
	{
		return receivedDate;
	}
	
	/**
	 * Gets Email Address of Recipient
	 * @return Recipient's Email Address
	 */
	public String getEmailAddress()
	{
		return emailAddress;
	}
	
	/**
	 * Gets First Name of Recipient
	 * @return Recipeint's First Name
	 */
	public String getFirstName()
	{
		return firstName;
	}
	
	/**
	 * Gets Last Name of Recipient
	 * @return Recipient's Last Name
	 */
	public String getLastName()
	{
		return lastName;
	}
	
	/**
	 * Gets Box/Office Number of Recipient
	 * @return Recipient's Box/Office Number
	 */
	public String getBoxOffice()
	{
		return boxOffice;
	}
	
	/**
	 * Gets Package delivery Stop
	 * @return Stop package to be delivered to
	 */
	public Stop getStop()
	{
		return stop;
	}
	
	/**
	 * Gets Package Courier
	 * @return Courier who brought the Package
	 */
	public Courier getCourier()
	{
		return courier;
	}
	
	/**
	 * Gets User who scanned in package
	 * @return User who scanned in the package
	 */
	public User getUser()
	{
		return user;
	}
	
	/**
	 * Gets if Package is at stop
	 * @return Wether Recipient is picked up or not
	 */
	public boolean isAtStop()
	{
		return atStop;
	}
	
	/**
	 * Gets if package is picked up
	 * @return Whether Recipient is picked up or not
	 */
	public boolean isPickedUp()
	{
		return pickedUp;
	}
	
	/**
	 * Gets Date Recipient Picked Up Package
	 * @return Date Package was picked up by Recipient
	 */
	public String getDatePickedUp()
	{
		return pickUpDate;
	}
	
	/**
	 * Gets Return Status
	 * @return Returned Status of Package
	 */
	public boolean isReturned()
	{
		return returned;
	}
}
