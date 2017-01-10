package com.mailroom.common.objects;

/**
 * Represents A DbStop for Packages
 *
 * @author James rockking1379@gmail.com
 */
public class DbStop
{
    private int stopId;
    private String stopName;
    private String routeName;
    private int routeOrder;
    private boolean student;
    private boolean autoRemove;

    /**
     * Constructs new DbStop
     *
     * @param stopId ID of DbStop in Database
     * @param stopName Name of DbStop
     * @param routeName Name of DbRoute that DbStop is on
     * @param routeOrder Position on DbRoute that DbStop is on
     * @param student Wehter DbStop is for Students or not
     */
    @Deprecated
    public DbStop(int stopId, String stopName, String routeName, int routeOrder,
                  boolean student)
    {
        this.stopId = stopId;
        this.stopName = stopName;
        this.routeName = routeName;
        this.routeOrder = routeOrder;
        this.student = student;
        this.autoRemove = false;
    }

    /**
     * Constructs new DbStop
     *
     * @param stopId ID of DbStop in Database
     * @param stopName Name of DbStop
     * @param routeName Name of DbRoute that DbStop is on
     * @param routeOrder Position on DbRoute that DbStop is on
     * @param student Wehter DbStop is for Students or not
     * @param autoRemove Wether or not to Auto Remove DbStop Packages
     */
    public DbStop(int stopId, String stopName, String routeName, int routeOrder,
                  boolean student, boolean autoRemove)
    {
        this.stopId = stopId;
        this.stopName = stopName;
        this.routeName = routeName;
        this.routeOrder = routeOrder;
        this.student = student;
        this.autoRemove = autoRemove;
    }

    /**
     * Gets ID of DbStop
     *
     * @return ID of DbStop
     */
    public int getStopId()
    {
        return stopId;
    }

    /**
     * Gets Name of DbStop
     *
     * @return Name of DbStop
     */
    public String getStopName()
    {
        return stopName;
    }

    /**
     * Gets Name of DbRoute
     *
     * @return Name of DbRoute that DbStop is on
     */
    public String getRouteName()
    {
        return routeName;
    }

    /**
     * Gets Order of DbStop on DbRoute
     *
     * @return Order of DbStop on DbRoute
     */
    public int getRouteOrder()
    {
        return routeOrder;
    }

    /**
     * Gets Student Status of DbStop <br>
     * Used mainly for College Campuses
     *
     * @return Wether or not DbStop is for Students or not
     */
    public boolean getStudent()
    {
        return student;
    }

    /**
     * Gets Auto Remove status of DbStop
     *
     * @return Wether or not to Auto Remove DbStop's Packages
     */
    public boolean getAutoRemove()
    {
        return autoRemove;
    }

    /**
     * Sets Student Status of DbStop <br>
     * Used mainly for College Campuses
     *
     * @param student Wether or not DbStop is for Students or not
     */
    public void setStudent(boolean student)
    {
        this.student = student;
    }

    /**
     * Sets Auto Removal of DbStop <br>
     * Used for lazy mailrooms :)
     *
     * @param autoRemove Wether or not to Auto Remove packages for this DbStop
     */
    public void setAutoRemove(boolean autoRemove)
    {
        this.autoRemove = autoRemove;
    }

    @Override
    public String toString()
    {
        return stopName;
    }
}
