package com.mailroom.common;

/**
 * Represents a Route For Stops and Packages
 * @author James
 *
 */
public class Route 
{
	private int routeId;
	private String routeName;
	
	/**
	 * Constructs new Route
	 * @param routeId ID of Route in Database
	 * @param routeName Name of Route
	 */
	public Route(int routeId, String routeName)
	{
		this.routeId = routeId;
		this.routeName = routeName;
	}
	
	/**
	 * Gets ID of Route
	 * @return ID of Route
	 */
	public int getRouteId()
	{
		return routeId;
	}
	
	/**
	 * Gets Name of Route
	 * @return Name of Route
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
