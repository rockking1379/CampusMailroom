package com.mailroom.common;

/**
 * Represents A Stop for Packages
 * @author James sitzja@grizzlies.adams.edu
 *
 */
public class Stop 
{
	private int stopId;
	private String stopName;
	private String routeName;
	private int routeOrder;
	private boolean student;
	
	/**
	 * Constructs new Stop
	 * @param stopId ID of Stop in Database
	 * @param stopName Name of Stop
	 * @param routeName Name of Route that Stop is on
	 * @param routeOrder Position on Route that Stop is on
	 * @param student Wehter Stop is for Students or not
	 */
	public Stop(int stopId, String stopName, String routeName, int routeOrder, boolean student)
	{
		this.stopId = stopId;
		this.stopName = stopName;
		this.routeName = routeName;
		this.routeOrder = routeOrder;
		this.student = student;
	}
	
	/**
	 * Gets ID of Stop
	 * @return ID of Stop
	 */
	public int getStopId()
	{
		return stopId;
	}
	
	/**
	 * Gets Name of Stop
	 * @return Name of Stop
	 */
	public String getStopName()
	{
		return stopName;
	}
	
	/**
	 * Gets Name of Route
	 * @return Name of Route that Stop is on
	 */
	public String getRouteName()
	{
		return routeName;
	}
	
	/**
	 * Gets Order of Stop on Route
	 * <br>
	 * Unused for now
	 * @return Order of Stop on Route
	 */
	public int getRouteOrder()
	{
		return routeOrder;
	}
	
	/**
	 * Gets Student Status of Stop
	 * <br>
	 * Used mainly for College Campuses
	 * @return Wether or not Stop is for Students or not
	 */
	public boolean getStudent()
	{
		return student;
	}
	
	/**
	 * Sets Student Status of Stop
	 * <br>
	 * Used mainly for College Campuses
	 * @param student Wether or not Stop is for Students or not
	 */
	public void setStudent(boolean student)
	{
		this.student = student;
	}
	
	@Override
	public String toString()
	{
		return stopName;
	}
}
