package com.mailroom.common;

/**
 * Represents A Stop for Packages
 *
 * @author James rockking1379@gmail.com
 */
public class Stop
{
    private int stopId;
    private String stopName;
    private String routeName;
    private int routeOrder;
    private boolean student;
    private boolean autoRemove;

    /**
     * Constructs new Stop
     *
     * @param stopId ID of Stop in Database
     * @param stopName Name of Stop
     * @param routeName Name of Route that Stop is on
     * @param routeOrder Position on Route that Stop is on
     * @param student Wehter Stop is for Students or not
     */
    @Deprecated
    public Stop(int stopId, String stopName, String routeName, int routeOrder,
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
     * Constructs new Stop
     *
     * @param stopId ID of Stop in Database
     * @param stopName Name of Stop
     * @param routeName Name of Route that Stop is on
     * @param routeOrder Position on Route that Stop is on
     * @param student Wehter Stop is for Students or not
     * @param autoRemove Wether or not to Auto Remove Stop Packages
     */
    public Stop(int stopId, String stopName, String routeName, int routeOrder,
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
     * Gets ID of Stop
     *
     * @return ID of Stop
     */
    public int getStopId()
    {
        return stopId;
    }

    /**
     * Gets Name of Stop
     *
     * @return Name of Stop
     */
    public String getStopName()
    {
        return stopName;
    }

    /**
     * Gets Name of Route
     *
     * @return Name of Route that Stop is on
     */
    public String getRouteName()
    {
        return routeName;
    }

    /**
     * Gets Order of Stop on Route
     *
     * @return Order of Stop on Route
     */
    public int getRouteOrder()
    {
        return routeOrder;
    }

    /**
     * Gets Student Status of Stop <br>
     * Used mainly for College Campuses
     *
     * @return Wether or not Stop is for Students or not
     */
    public boolean getStudent()
    {
        return student;
    }

    /**
     * Gets Auto Remove status of Stop
     *
     * @return Wether or not to Auto Remove Stop's Packages
     */
    public boolean getAutoRemove()
    {
        return autoRemove;
    }

    /**
     * Sets Student Status of Stop <br>
     * Used mainly for College Campuses
     *
     * @param student Wether or not Stop is for Students or not
     */
    public void setStudent(boolean student)
    {
        this.student = student;
    }

    /**
     * Sets Auto Removal of Stop <br>
     * Used for lazy mailrooms :)
     *
     * @param autoRemove Wether or not to Auto Remove packages for this Stop
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
