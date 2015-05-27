package com.mailroom.common.objects;

/**
 * Represents A Courier
 *
 * @author James rockking1379@gmail.com
 */
public class Courier
{
    private int courierId;
    private String courierName;

    /**
     * Constructs a new Courier
     *
     * @param courierId ID of Courier in Database
     * @param courierName Name of Courier
     */
    public Courier(int courierId, String courierName)
    {
        this.courierId = courierId;
        this.courierName = courierName;
    }

    /**
     * Gets Courier ID
     *
     * @return ID of Courier
     */
    public int getCourierId()
    {
        return courierId;
    }

    /**
     * Gets Courier Name
     *
     * @return Name of Courier
     */
    public String getCourierName()
    {
        return courierName;
    }

    @Override
    public String toString()
    {
        return courierName;
    }
}
