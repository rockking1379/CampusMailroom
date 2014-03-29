package com.mailroom.common;

import java.sql.ResultSet;
import java.util.List;

public class DatabaseManager 
{
	//User Actions
	 public User login(String userName, int password)
	 {
		 return null;
	 }
	 public boolean addUser(String userName, String firstName, String lastName, int password, boolean admin)
	 {
		 return false;
	 }
	 public boolean changePassword(User u, int oldPassword, int newPassword)
	 {
		 return false;
	 }
	 public boolean deleteUser(String username, User admin, int password)
	 {
		 return false;
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
	 public List<Stop> getUnassignedstops()
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
	 public boolean updatePackage(Package p)
	 {
		 return false;
	 }
	 public boolean addPackage(Package p, int userId)
	 {
		 return false;
	 }
	 List<Package> processPackageResult(ResultSet rs)
	 {
		 return null;
	 }
	 
	 //Normal Actions
	 public void dispose()
	 {
		 return;
	 }
}
