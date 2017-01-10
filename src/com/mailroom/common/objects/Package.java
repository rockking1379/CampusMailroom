package com.mailroom.common.objects;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Represents A Received Package
 *
 * @author James rockking1379@gmail.com
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
    private DbStop dbStop;
    private DbCourier dbCourier;
    private DbUser dbUser;
    private boolean atStop;
    private boolean pickedUp;
    private String pickUpDate;
    private boolean returned;

    /**
     * Constructs a new Package
     *
     * @param packageId id of package(-1 for one to be inserted)
     * @param trackingNumber tracking number of package
     * @param receivedDate Date package was scanned into system
     * @param emailAddress email address of recipient
     * @param firstName first name of recipient
     * @param lastName last name of recipient
     * @param boxOffice box/office number of recipient
     * @param dbStop DbStop package to be delivered to
     * @param dbCourier DbCourier who brought the package
     * @param dbUser DbUser who scanned it in
     * @param atStop wether package is at its dbStop waiting for pick up or not
     * @param pickedUp wether package has been received by recipient or not
     * @param pickUpDate Date package was picked up by recipient
     * @param returned wether package was returned
     */
    public Package(int packageId, String trackingNumber, String receivedDate,
                   String emailAddress, String firstName, String lastName,
                   String boxOffice, DbStop dbStop, DbCourier dbCourier, DbUser dbUser,
                   boolean atStop, boolean pickedUp, String pickUpDate,
                   boolean returned)
    {
        this.packageId = packageId;
        this.trackingNumber = trackingNumber;
        this.receivedDate = receivedDate;
        this.emailAddress = emailAddress;
        this.firstName = firstName;
        this.lastName = lastName;
        this.boxOffice = boxOffice;
        this.dbStop = dbStop;
        this.dbCourier = dbCourier;
        this.dbUser = dbUser;
        this.atStop = atStop;
        this.pickedUp = pickedUp;
        this.pickUpDate = pickUpDate;
        this.returned = returned;
    }

    /**
     * Sets DbStop package is to be delivered to
     *
     * @param s new DbStop to be delivered to
     */
    public void setDbStop(DbStop s)
    {
        this.dbStop = s;
    }

    /**
     * Sets DbCourier who brought the package
     *
     * @param c DbCourier who brought the package
     */
    public void setDbCourier(DbCourier c)
    {
        this.dbCourier = c;
    }

    /**
     * Sets wether Package is at DbStop or not
     *
     * @param atStop wether package is at dbStop or not
     */
    public void setAtStop(boolean atStop)
    {
        this.atStop = atStop;
    }

    /**
     * Sets wether Package is picked up or not
     *
     * @param pickedUp wether package is picked up or not
     */
    public void setPickedUp(boolean pickedUp)
    {
        this.pickedUp = pickedUp;
        if (pickedUp)
        {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date now = new Date();
            this.pickUpDate = format.format(now);
        }
    }

    /**
     * Gets Package ID
     *
     * @return package ID
     */
    public int getPackageId()
    {
        return packageId;
    }

    /**
     * Gets Full Tracking Number
     *
     * @return full package Tracking Number
     */
    public String getFullTrackingNumber()
    {
        return trackingNumber;
    }

    /**
     * Gets last four digits of tracking number with ... in front
     *
     * @return ...xxxx
     */
    public String getTrackingNumber()
    {
        String strReturn = null;

        if (trackingNumber.length() < 4)
        {
            strReturn = "";
        }
        if (trackingNumber.length() == 4)
        {
            strReturn = "..." + trackingNumber;
        }
        if (trackingNumber.length() > 4)
        {
            strReturn = "..."
                    + trackingNumber.substring(trackingNumber.length() - 4,
                    trackingNumber.length());
        }

        return strReturn;
    }

    /**
     * Gets Date Package was scanned into system
     *
     * @return Date Received
     */
    public String getReceivedDate()
    {
        return receivedDate;
    }

    /**
     * Gets Email Address of Recipient
     *
     * @return Recipient's Email Address
     */
    public String getEmailAddress()
    {
        return emailAddress;
    }

    /**
     * Gets First Name of Recipient
     *
     * @return Recipeint's First Name
     */
    public String getFirstName()
    {
        return firstName;
    }

    /**
     * Gets Last Name of Recipient
     *
     * @return Recipient's Last Name
     */
    public String getLastName()
    {
        return lastName;
    }

    /**
     * Gets Box/Office Number of Recipient
     *
     * @return Recipient's Box/Office Number
     */
    public String getBoxOffice()
    {
        return boxOffice;
    }

    /**
     * Gets Package delivery DbStop
     *
     * @return DbStop package to be delivered to
     */
    public DbStop getDbStop()
    {
        return dbStop;
    }

    /**
     * Gets Package DbCourier
     *
     * @return DbCourier who brought the Package
     */
    public DbCourier getDbCourier()
    {
        return dbCourier;
    }

    /**
     * Gets DbUser who scanned in package
     *
     * @return DbUser who scanned in the package
     */
    public DbUser getDbUser()
    {
        return dbUser;
    }

    /**
     * Gets if Package is at dbStop
     *
     * @return Wether Recipient is picked up or not
     */
    public boolean isAtStop()
    {
        return atStop;
    }

    /**
     * Gets if package is picked up
     *
     * @return Whether Recipient is picked up or not
     */
    public boolean isPickedUp()
    {
        return pickedUp;
    }

    /**
     * Gets Date Recipient Picked Up Package
     *
     * @return Date Package was picked up by Recipient
     */
    public String getDatePickedUp()
    {
        return pickUpDate;
    }

    /**
     * Gets Return Status
     *
     * @return Returned Status of Package
     */
    public boolean isReturned()
    {
        return returned;
    }
}
