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
	
	public Route(int routeId, String routeName)
	{
		this.routeId = routeId;
		this.routeName = routeName;
	}
	
	public int getRouteId()
	{
		return routeId;
	}
	
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
