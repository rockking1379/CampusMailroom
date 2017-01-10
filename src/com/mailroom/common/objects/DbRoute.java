package com.mailroom.common.objects;

/**
 * Represents a DbRoute For Stops and Packages
 *
 * @author James rockking1379@gmail.com
 */
public class DbRoute
{
    private int routeId;
    private String routeName;

    /**
     * Constructs new DbRoute
     *
     * @param routeId ID of DbRoute in Database
     * @param routeName Name of DbRoute
     */
    public DbRoute(int routeId, String routeName)
    {
        this.routeId = routeId;
        this.routeName = routeName;
    }

    /**
     * Gets ID of DbRoute
     *
     * @return ID of DbRoute
     */
    public int getRouteId()
    {
        return routeId;
    }

    /**
     * Gets Name of DbRoute
     *
     * @return Name of DbRoute
     */
    public String getRouteName()
    {
        return routeName;
    }

    @Override
    public String toString()
    {
        return routeName;
    }
}
