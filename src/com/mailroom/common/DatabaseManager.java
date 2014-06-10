package com.mailroom.common;

import java.sql.ResultSet;
import java.util.List;

public class DatabaseManager 
{
	public final int SEARCH_BEGINS_WITH = 0;
	public final int SEARCH_CONTAINS = 1;
	public final int SEARCH_ENDS_WITH = 2;
	
	//User Actions
	 public User login(String userName, int password)
	 {
		 return null;
	 }
	 public boolean addUser(User u, int password)
	 {
		 return false;
	 }
	 public boolean changePassword(User u, int oldPassword, int newPassword)
	 {
		 return false;
	 }
	 public boolean deleteUser(User u)
	 {
		 return false;
	 }
	public boolean setUserAdmin(User u, boolean status)
	{
		return false;
	}
	public boolean reactivateUser(User u, int password)
	{
		return false;
	}
	public List<User> getDeactivatedUsers()
	{
		return null;
	}
	public List<User> getActiveUsers()
	{
		return null;
	}
	
	//Stop Actions
	 public void loadStops()
	 {
		 return;
	 }
	 public boolean updateStop(Stop s)
	 {
		 return false;
	 }
	 public boolean addStopToRoute(Stop s, Route r)
	 {
		 return false;
	 }
	 public boolean addStop(Stop s)
	 {
		 return false;
	 }
	 public boolean deleteStop(Stop s)
	 {
		 return false;
	 }
	 public List<Stop> getStops()
	 {
		 return null;
	 }
	 public List<Stop> getUnassignedStops()
	 {
		 return null;
	 }
	 public List<Stop> getStopsOnRoute(Route r)
	 {
		 return null;
	 }
	 List<Stop> processStopResult(ResultSet rs, String routeName)
	 {
		 return null;
	 }
	
	//Route Actions
	 public void loadRoutes()
	 {
		 return;
	 }
	 public List<Route> getRoutes()
	 {
		 return null;
	 }
	 public boolean updateRoute(Route r)
	 {
		 return false;
	 }
	 public boolean addRoute(String routeName)
	 {
		 return false;
	 }
	 public boolean deleteRoute(Route r)
	 {
		 return false;
	 }
	
	//Courier Actions
	 public void loadCouriers()
	 {
		 return;
	 }
	 public List<Courier> getCouriers()
	 {
		 return null;
	 }
	 public boolean addCourier(String courierName)
	 {
		 return false;
	 }
	 public boolean updateCourier(Courier c)
	 {
		 return false;
	 }
	 public boolean deleteCourier(Courier c)
	 {
		 return false;
	 }
	
	//Package Actions
	 public List<Package> getPackages()
	 {
		 return null;
	 }
	 public void loadAllPackages()
	 {
		 return;
	 }
	 public void loadPackages(int stopId)
	 {
		 return;
	 }
	 public boolean updatePackage(int packageId, boolean atStop, boolean pickedUp)
	 {
		 return false;
	 }
	 public boolean addPackage(Package p)
	 {
		 return false;
	 }
	 public List<Package> getPackagesForStop(Stop s)
	 {
		 return null;
	 }
	 List<Package> processPackageResult(ResultSet rs)
	 {
		 return null;
	 }
	 
	 //Search Actions
	 public List<Person> findPerson(String firstName, String lastName, String boxOffice)
	 {
		 return null;
	 }
	 
	 public List<Package> searchPackages(String search, int location)
	 {
		 return null;
	 }
	 
	 public List<Package> searchPackages(String search, String startDate, String endDate, int location)
	 {
		 return null;
	 }
	 
	 //Normal Actions
	 public void connect()
	 {
		 return;
	 }
	 public void disconnect()
	 {
		 return;
	 }
	 public void dispose()
	 {
		 return;
	 }
	 public boolean create(boolean insertDev)
	 {
		 return false;
	 }
}
