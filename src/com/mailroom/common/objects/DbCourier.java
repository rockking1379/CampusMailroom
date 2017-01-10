package com.mailroom.common.objects;

/**
 * Represents A DbCourier
 *
 * @author James rockking1379@gmail.com
 */
public class DbCourier
{
    private int courierId;
    private String courierName;

    /**
     * Constructs a new DbCourier
     *
     * @param courierId ID of DbCourier in Database
     * @param courierName Name of DbCourier
     */
    public DbCourier(int courierId, String courierName)
    {
        this.courierId = courierId;
        this.courierName = courierName;
    }

    /**
     * Gets DbCourier ID
     *
     * @return ID of DbCourier
     */
    public int getCourierId()
    {
        return courierId;
    }

    /**
     * Gets DbCourier Name
     *
     * @return Name of DbCourier
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
