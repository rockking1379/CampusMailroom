package com.mailroom.common;

public class Stop 
{
	private int stopId;
	private String stopName;
	private String routeName;
	private int routeOrder;
	private boolean student;
	
	public Stop(int stopId, String stopName, String routeName, int routeOrder, boolean student)
	{
		this.stopId = stopId;
		this.stopName = stopName;
		this.routeName = routeName;
		this.routeOrder = routeOrder;
		this.student = student;
	}
	
	public int getStopId()
	{
		return stopId;
	}
	
	public String getStopName()
	{
		return stopName;
	}
	
	public String getRouteName()
	{
		return routeName;
	}
	
	public int getRouteOrder()
	{
		return routeOrder;
	}
	
	public boolean getStudent()
	{
		return student;
	}
	
	public void setStudent(boolean student)
	{
		this.student = student;
	}
}
