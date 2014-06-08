package com.mailroom.common;

public class Courier 
{
	private int courierId;
	private String courierName;
	
	public Courier(int courierId, String courierName)
	{
		this.courierId = courierId;
		this.courierName = courierName;
	}
	
	public int getCourierId()
	{
		return courierId;
	}
	
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
