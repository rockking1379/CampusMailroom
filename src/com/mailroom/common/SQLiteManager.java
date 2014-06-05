package com.mailroom.common;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SQLiteManager extends DatabaseManager
{
	private static final String createString = "CREATE TABLE Route(route_id INTEGER PRIMARY KEY AUTOINCREMENT,Name varchar(50) NOT NULL,Is_Used BOOLEAN NOT NULL);CREATE TABLE Stop(stop_id INTEGER PRIMARY KEY AUTOINCREMENT,Name varchar(50) NOT NULL,route_id int,Is_Used BOOLEAN NOT NULL,route_order int,Student BOOLEAN,FOREIGN KEY(route_id) REFERENCES Route(route_id));CREATE TABLE Courier(courier_id INTEGER PRIMARY KEY AUTOINCREMENT,Name varchar(50) NOT NULL,Is_used BOOLEAN NOT NULL);CREATE TABLE Package(package_id INTEGER PRIMARY KEY AUTOINCREMENT,Tracking_Number varchar(50) NOT NULL,Date DATE NOT NULL,ASU_Email varchar(50) NOT NULL,First_Name varchar(50) NOT NULL,	Last_Name varchar(50) NOT NULL,Box_Number varchar(50) NOT NULL,At_Stop BOOLEAN NOT NULL,Picked_Up BOOLEAN NOT NULL,Pick_Up_Date DATE,stop_id int,courier_id int,processor int,Returned BOOLEAN,FOREIGN KEY(stop_id) REFERENCES Stop(stop_id),FOREIGN KEY(courier_id) REFERENCES Courier(courier_id)FOREIGN KEY(processor) REFERENCES User(user_id));CREATE TABLE Person(id INTEGER PRIMARY KEY AUTOINCREMENT,ID_Number varchar(50),ASU_Email varchar(50),First_Name varchar(50) NOT NULL,Last_Name varchar(50) NOT NULL,Number varchar(50),stop_id int,Forward_Address varchar(150),FOREIGN KEY(stop_id) REFERENCES Stop(stop_id));CREATE TABLE User(user_id INTEGER PRIMARY KEY AUTOINCREMENT,User_Name varchar(50) NOT NULL,First_Name varchar(50) NOT NULL,Last_Name varchar(50) NOT NULL,Password INTEGER NOT NULL,Admin BOOLEAN NOT NULL,Active BOOLEAN);insert into Route(Name, Is_Used) values('unassigned', 1);insert into Stop(Name,route_id,Is_Used,route_order,Student) values('unassigned',1,1,0,0);";
	private static final String devString = "insert into User(User_Name, First_Name, Last_Name, Password, Admin, Active) values('DEV', 'Developer', 'Access', -2017346997,1,1);";

	private Connection connection;
	private String dbLocation;
	private List<Courier> couriers;
	private List<Route> routes;
	private List<Stop> stops;
	private List<Package> packages;
	
	public SQLiteManager(String dbLocation)
	{
		dbLocation.replace('\\', '/');
		this.dbLocation = dbLocation;
		try
		{
			Class.forName("org.sqlite.JDBC");
		}
		catch(ClassNotFoundException e)
		{
			System.err.println("Could Not Load SQLite JDBC Driver");
		}
		
		loadRoutes();
		loadStops();
		loadCouriers();
	}

	//User Actions//
	@Override
	public User login(String userName, int password) 
	{
		User u = new User(-1, null, null, null, false);
		
		//conduct login if successful recreate 'u' to valid user or return with nulls and show GUI error
		try
		{
			connect();
			PreparedStatement stmt = connection.prepareStatement("select * from User where User_Name=? and Password=? and Active=1");
			stmt.setQueryTimeout(5);
			stmt.setString(1, userName);
			stmt.setInt(2, password);
			
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next())
			{
				u = new User(rs.getInt("user_id"), rs.getString("User_Name"), rs.getString("First_Name"), rs.getString("Last_Name"), rs.getBoolean("Admin"));
			}
		}
		catch(SQLException e)
		{
			System.err.println("Error Logging In: " + e.getMessage());
		}
		finally
		{
			disconnect();
		}
		
		return u;
	}
	@Override
	public boolean addUser(User u, int password)
	{
		//conduct insert into user table here
		//settings option should only be available to admin
		try 
		{
			connect();
			PreparedStatement stmnt = connection.prepareStatement("insert into User(User_Name, First_Name, Last_Name, Password, Admin, Active) values(?,?,?,?,?,1)");
			stmnt.setQueryTimeout(5);
			
			stmnt.setString(1, u.getUserName());
			stmnt.setString(2, u.getFirstName());
			stmnt.setString(3, u.getLastName());
			stmnt.setInt(4, password);
			stmnt.setBoolean(5, u.getAdmin());
			
			if(stmnt.executeUpdate() > 0)
			{
				return true;
			}
			else
			{
				return false;
			}
		} 
		catch (SQLException e) 
		{
			System.err.println("Error: " + e.getMessage());
			return false;
		}
		finally
		{
			disconnect();
		}
	}
	@Override
	public boolean changePassword(User u, int oldPassword, int newPassword) 
	{
		//allow users to change their password
		try
		{
			connect();
			PreparedStatement stmnt = connection.prepareStatement("update User set Password=? where User_Name=? and Password=?");
			stmnt.setQueryTimeout(5);
			
			stmnt.setInt(1, newPassword);
			stmnt.setString(2, u.getUserName());
			stmnt.setInt(3, oldPassword);
			
			if(stmnt.executeUpdate() > 0)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		catch(SQLException e)
		{
			System.err.println("Error: " + e.getMessage());
			return false;
		}
		finally
		{
			disconnect();
		}
	}
	@Override
	public boolean deleteUser(String username) 
	{
		boolean retValue = false;
		try
		{
			connect();
			PreparedStatement stmnt = connection.prepareStatement("update User set Active=0 where User_Name=?");
			
			stmnt.setString(1,username);
			
			if(stmnt.executeUpdate() > 0)
			{
				retValue = true;
			}
			else
			{
				retValue = false;
			}
		}
		catch(SQLException e)
		{
			System.err.println("Error: " + e.getMessage());
			retValue = false;
		}
		finally
		{
			disconnect();
		}
		
		return retValue;
	}
	@Override
	public boolean setUserAdmin(String username, boolean status)
	{
		boolean retValue = false;
		try
		{
			connect();
			
			PreparedStatement stmnt = connection.prepareStatement("update User set Admin=1 where User_Name=?");
			
			stmnt.setString(1, username);
			
			if(stmnt.executeUpdate() > 0)
			{
				retValue = true;
			}
			else
			{
				retValue = false;
			}
		}
		catch(SQLException e)
		{
			System.err.println("Error: " + e.getMessage());
			retValue = false;
		}
		finally
		{
			disconnect();
		}
		
		return retValue;
	}
	@Override
	public boolean reactivateUser(String username, int password)
	{
		boolean retValue = false;
		try
		{
			connect();
			
			PreparedStatement stmnt = connection.prepareStatement("update User set Active=1, Password=? where User_Name=?");
			
			stmnt.setInt(1, password);
			stmnt.setString(2, username);
			
			if(stmnt.executeUpdate() > 0)
			{
				retValue = true;
			}
			else
			{
				retValue = false;
			}
		}
		catch(SQLException e)
		{
			System.err.println("Error: " + e.getMessage());
			retValue = false;
		}
		finally
		{
			disconnect();
		}
		
		return retValue;
	}
	@Override
	public List<User> getDeactivatedUsers()
	{
		List<User> result = new ArrayList<User>();
		
		try
		{
			connect();
			
			Statement stmnt = connection.createStatement();
			
			ResultSet rs = stmnt.executeQuery("select * from User where Active=0");
			
			while(rs.next())
			{
				result.add(new User(rs.getInt("user_id"), rs.getString("User_Name"), rs.getString("First_Name"), rs.getString("Last_Name"), rs.getBoolean("Admin")));
			}
		}
		catch(SQLException e)
		{
			System.err.println("Error: " + e .getMessage());
		}
		finally
		{
			disconnect();
		}
		
		return result;
	}
	@Override
	public List<User> getActiveUsers()
	{
		List<User> result = new ArrayList<User>();
		
		try
		{
			connect();
			
			Statement stmnt = connection.createStatement();
			
			ResultSet rs = stmnt.executeQuery("select * from User where Active=1");
			
			while(rs.next())
			{
				result.add(new User(rs.getInt("user_id"), rs.getString("User_Name"), rs.getString("First_Name"), rs.getString("Last_Name"), rs.getBoolean("Admin")));
			}
		}
		catch(SQLException e)
		{
			System.err.println("Error: " + e .getMessage());
		}
		finally
		{
			disconnect();
		}
		
		return result;
	}
	
	//Stop Actions//
	@Override
	public void loadStops() 
	{
		try
		{
			connect();
			stops = new ArrayList<Stop>();
			
			Statement stmnt = connection.createStatement();
			stmnt.setQueryTimeout(5);
			
			ResultSet rs = stmnt.executeQuery("select * from Stop where Is_Used=1");
			
			while(rs.next())
			{
				for(int i=0; i<routes.size(); i++)
				{
					if(routes.get(i).getRouteId() == rs.getInt("route_id"))
					{
						stops.add(new Stop(rs.getInt("stop_id"), rs.getString("Name"), routes.get(i).getRouteName(), rs.getInt("route_order"), rs.getBoolean("Student")));
					}
				}
			}
		}
		catch(SQLException e)
		{
			System.err.println("Error: " + e.getMessage());
		}
		finally
		{
			disconnect();
		}
	}
	@Override
	public boolean updateStop(Stop s) 
	{
		try
		{
			connect();
			PreparedStatement stmnt = connection.prepareStatement("update Stop set Student=? where stop_id=?");
			stmnt.setQueryTimeout(5);
			
			stmnt.setBoolean(1, s.getStudent());
			stmnt.setInt(2, s.getStopId());
			
			if(stmnt.executeUpdate() > 0)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		catch(SQLException e)
		{
			System.err.println("Error: " + e.getMessage());
			return false;
		}
		finally
		{
			disconnect();
		}
	}
	@Override
	public boolean addStopToRoute(Stop s, Route r)
	{
		boolean retValue = false;
		
		try
		{
			connect();
			PreparedStatement stmnt = connection.prepareStatement("update Stop set route_id=? where stop_id=?");
			
			stmnt.setInt(1, r.getRouteId());
			stmnt.setInt(2, s.getStopId());
			
			if(stmnt.executeUpdate() > 0)
			{
				retValue = true;
			}
			else
			{
				retValue = false;
			}
		}
		catch(SQLException e)
		{
			System.err.println("Error: " + e.getMessage());
			retValue = false;
		}
		finally
		{
			disconnect();
		}
		
		return retValue;
	}
	@Override
	public boolean addStop(Stop s) 
	{
		//the passed in stop should receive a stopId of -1(ignored on insert anyway)
		try
		{
			connect();
			PreparedStatement stmnt = connection.prepareStatement("insert into Stop(Name, route_id, Is_Used, route_order, Student) values(?,?,1,?,?)");
			stmnt.setQueryTimeout(5);
			
			stmnt.setString(1, s.getStopName());
			for(Route r : routes)
			{
				if(r.getRouteName().equals(s.getRouteName()))
				{
					stmnt.setInt(2, r.getRouteId());
					break;
				}
			}
			stmnt.setInt(3, s.getRouteOrder());
			stmnt.setBoolean(4, s.getStudent());
			
			if(stmnt.executeUpdate() > 0)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		catch(SQLException e)
		{
			System.err.println("Error: " + e.getMessage());
			return false;
		}
		finally
		{
			disconnect();
		}
	}
	@Override
	public boolean deleteStop(Stop s) 
	{
		try
		{
			connect();
			PreparedStatement stmnt = connection.prepareStatement("update Stop set Is_Used=0 where stop_id=?");
			stmnt.setQueryTimeout(5);
			
			stmnt.setInt(1, s.getStopId());
			
			if(stmnt.executeUpdate() > 0)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		catch(SQLException e)
		{
			System.err.println("Error: " + e.getMessage());
			return false;
		}
		finally
		{
			disconnect();
		}
	}
	@Override
	public List<Stop> getStops() 
	{
		return stops;
	}
	@Override
	public List<Stop> getUnassignedStops() 
	{
		try
		{
			connect();
			Statement stmnt = connection.createStatement();
			stmnt.setQueryTimeout(5);
			
			ResultSet rs = stmnt.executeQuery("select * from Stop where route_id=1 and Is_Used=1");
			
			return processStopResult(rs, "unassigned");
		}
		catch(SQLException e)
		{
			System.err.println("Error: " + e.getMessage());
			return null;
		}
		finally
		{
			disconnect();
		}
	}
	@Override
	public List<Stop> getStopsOnRoute(Route r) 
	{
		try
		{
			connect();
			PreparedStatement stmnt = connection.prepareStatement("select * from Stop where route_id=? and Is_Used=1");
			stmnt.setQueryTimeout(5);
			
			stmnt.setInt(1, r.getRouteId());
			
			ResultSet rs = stmnt.executeQuery();
			
			return processStopResult(rs, r.getRouteName());
		}
		catch(SQLException e)
		{
			System.err.println("Error: " + e.getMessage());
			return null;
		}
		finally
		{
			disconnect();
		}
	}
	@Override
	List<Stop> processStopResult(ResultSet rs, String routeName)
	{
		List<Stop> results = new ArrayList<Stop>();
		
		try
		{
			while(rs.next())
			{
				results.add(new Stop(rs.getInt("stop_id"), rs.getString("Name"), routeName, rs.getInt("route_order"), rs.getBoolean("Student")));
			}
		}
		catch(SQLException e)
		{
			System.err.println("Error: " + e.getMessage());
			return null;
		}
		
		return results;
	}

	//Route Actions//
	@Override
	public void loadRoutes() 
	{
		try
		{
			connect();
			routes = new ArrayList<Route>();
			
			Statement stmnt = connection.createStatement();
			stmnt.setQueryTimeout(5);
			
			ResultSet rs = stmnt.executeQuery("select * from Route where Is_Used=1;");
			
			while(rs.next())
			{
				routes.add(new Route(rs.getInt("route_id"), rs.getString("Name")));
			}
		}
		catch(SQLException e)
		{
			System.err.println("Error: " + e.getMessage());
		}
		finally
		{
			disconnect();
		}
	}
	@Override
	public List<Route> getRoutes() 
	{
		return routes;
	}
	@Override
	public boolean updateRoute(Route r) 
	{
		try
		{
			connect();
			PreparedStatement stmnt = connection.prepareStatement("update Route set Name=? where route_id=?");
			stmnt.setQueryTimeout(5);
			
			stmnt.setString(1, r.getRouteName());
			stmnt.setInt(2, r.getRouteId());
			
			if(stmnt.executeUpdate() > 0)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		catch(SQLException e)
		{
			System.err.println("Erorr: " + e.getMessage());
			return false;
		}
		finally
		{
			disconnect();
		}
	}
	@Override
	public boolean addRoute(String routeName) 
	{
		//Assume since route is being added that it is to be used
		try
		{
			connect();
			PreparedStatement stmnt = connection.prepareStatement("insert into Route(Name, Is_Used) values(?,1)");
			stmnt.setQueryTimeout(5);
			
			stmnt.setString(1, routeName);
			
			if(stmnt.executeUpdate() > 0)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		catch(SQLException e)
		{
			System.err.println("Error: " + e.getMessage());
			return false;
		}
		finally
		{
			disconnect();
		}
	}
	@Override
	public boolean deleteRoute(Route r) 
	{
		try
		{
			connect();
			PreparedStatement stmnt = connection.prepareStatement("update Route set Is_Used=0 where route_id=?");
			stmnt.setQueryTimeout(5);
			
			stmnt.setInt(1, r.getRouteId());
			
			if(stmnt.executeUpdate() > 0)
			{
				stmnt = connection.prepareStatement("update Stop set route_id=1 where route_id=?");
				stmnt.setInt(1, r.getRouteId());
				
				stmnt.executeUpdate();
				return true;
			}
			else
			{
				return false;
			}
		}
		catch(SQLException e)
		{
			System.err.println("Error: " + e.getMessage());
			return false;
		}
		finally
		{
			disconnect();
		}
	}

	//Courier Actions//
	@Override
	public void loadCouriers() 
	{
		try
		{
			connect();
			couriers = new ArrayList<Courier>();
		
			Statement stmnt = connection.createStatement();
			stmnt.setQueryTimeout(5);
		
			ResultSet rs = stmnt.executeQuery("select * from Courier where Is_used=1");
		
			while(rs.next())
			{
				couriers.add(new Courier(rs.getInt("courier_id"), rs.getString("Name")));
			}
		}
		catch(SQLException e)
		{
			System.err.println("Error: " + e.getMessage());
		}
		finally
		{
			disconnect();
		}
	}
	@Override
	public List<Courier> getCouriers() 
	{
		return couriers;
	}
	@Override
	public boolean addCourier(String courierName)
	{
		boolean retValue = false;
		
		try
		{
			connect();
			
			PreparedStatement stmnt = connection.prepareStatement("insert into Courier(Name, Is_used) values(?,1)");
			
			stmnt.setString(1, courierName);
			
			if(stmnt.execute())
			{
				retValue = true;
			}
			else
			{
				retValue = false;
			}
		}
		catch(SQLException e)
		{
			System.err.println("Error: " + e.getMessage());
			retValue = false;
		}
		finally
		{
			disconnect();
		}
		
		return retValue;
	}
	@Override
	public boolean updateCourier(Courier c) 
	{
		try
		{
			connect();
			PreparedStatement stmnt = connection.prepareStatement("update Courier set Name=? where courier_id=?");
			stmnt.setQueryTimeout(5);
			
			stmnt.setString(1, c.getCourierName());
			stmnt.setInt(2, c.getCourierId());
			
			if(stmnt.executeUpdate() > 0)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		catch(SQLException e)
		{
			System.err.println("Error: " + e.getMessage());
			return false;
		}
		finally
		{
			disconnect();
		}
	}
	@Override
	public boolean deleteCourier(Courier c) 
	{
		try
		{
			connect();
			PreparedStatement stmnt = connection.prepareStatement("update Courier set Is_used=0 where courier_id=?");
			stmnt.setQueryTimeout(5);
			
			stmnt.setInt(1, c.getCourierId());
			
			if(stmnt.executeUpdate() > 0)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		catch(SQLException e)
		{
			System.err.println("Error: " + e.getMessage());
			return false;
		}
		finally
		{
			disconnect();
		}
	}

	
	//Package Actions//
	@Override
	public List<Package> getPackages() 
	{
		return packages;
	}
	@Override
	public void loadAllPackages()
	{
		try
		{
			connect();
			Statement stmnt = connection.createStatement();
			stmnt.setQueryTimeout(5);
			
			ResultSet rs = stmnt.executeQuery("select * from Package where Picked_Up=0");
			
			packages = null;
			packages = processPackageResult(rs);
		}
		catch(SQLException e)
		{
			System.err.println("Error: " + e.getMessage());
		}
		finally
		{
			disconnect();
		}
	}
	@Override
	public void loadPackages(int stopId) 
	{
		try
		{
			connect();
			PreparedStatement stmnt = null;;
			
			stmnt = connection.prepareStatement("select * from Package where Picked_Up=0 and stop_id=?");
			stmnt.setInt(1, stopId);

			stmnt.setQueryTimeout(5);
			
			ResultSet rs = stmnt.executeQuery();
			
			packages = processPackageResult(rs);
		}
		catch(SQLException e)
		{
			System.err.println("Error: " + e.getMessage());
		}
		finally
		{
			disconnect();
		}
	}
	@Override
	public boolean updatePackage(int packageId, boolean atStop, boolean pickedUp) 
	{
		try
		{
			connect();
			PreparedStatement stmnt = null;
			
			stmnt = connection.prepareStatement("update Package set At_Stop=? where package_id=?");
			stmnt.setQueryTimeout(5);
			stmnt.setBoolean(1, atStop);
			stmnt.setInt(2, packageId);
			stmnt.executeUpdate();
			
			stmnt = connection.prepareStatement("update Package set Picked_Up=? where package_id=?");
			stmnt.setBoolean(1, pickedUp);
			stmnt.setInt(2, packageId);
			stmnt.executeUpdate();
			
			stmnt = connection.prepareStatement("update Package set Pick_Up_Date=? where package_id=?");
			DateFormat format = new SimpleDateFormat("yyy-MM-dd");
			Date now = new Date();
			String date = format.format(now).toString();
			stmnt.setString(1, date);
			stmnt.setInt(2, packageId);
			stmnt.executeUpdate();
			
			return true;
		}
		catch(SQLException e)
		{
			return false;
		}
		finally
		{
			disconnect();
		}
	}
	@Override
	public boolean addPackage(Package p, int userId) 
	{
		try
		{
			connect();
			PreparedStatement stmnt = connection.prepareStatement("insert into Package(Tracking_Number, Date, ASU_Email, First_Name, Last_Name, Box_Number, At_Stop, Picked_Up, stop_id, courier_id, processor)"
					+ " values(?,?,?,?,?,?,0,0,?,?,?)");
			stmnt.setQueryTimeout(5);
			
			stmnt.setString(1, p.getFullTrackingNumber());
			stmnt.setString(2, p.getReceivedDate());
			stmnt.setString(3, p.getEmailAddress());
			stmnt.setString(4, p.getFirstName());
			stmnt.setString(5, p.getLastName());
			stmnt.setString(6, p.getBoxOffice());
			for(int i=0; i<stops.size(); i++)
			{
				if(stops.get(i).getStopName() == p.getStopName())
				{
					stmnt.setInt(7, stops.get(i).getStopId());
					break;
				}
			}
			for(int i=0; i<couriers.size(); i++)
			{
				if(couriers.get(i).getCourierName() == p.getCourierName())
				{
					stmnt.setInt(8, couriers.get(i).getCourierId());
					break;
				}
			}
			stmnt.setInt(9, userId);
			
			if(stmnt.executeUpdate() > 0)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		catch(SQLException e)
		{
			System.err.println("Error: " + e.getMessage());
			return false;
		}
		finally
		{
			disconnect();
		}
	}
	@Override
	public List<Package> getPackagesForStop(Stop s)
	{
		ArrayList<Package> result = null;
		try
		{
			connect();
			
			PreparedStatement stmnt = connection.prepareStatement("select * from Package where Date=? and stop_id=? and At_Stop=0");
			
			Date d = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			
			stmnt.setString(1, format.format(d).toString());
			stmnt.setInt(2, s.getStopId());
			
			ResultSet rs = stmnt.executeQuery();
			
			result = (ArrayList<Package>) processPackageResult(rs);
		}
		catch(SQLException e)
		{
			System.err.println("Error: " + e.getMessage());
			result = null;
		}
		finally
		{
			disconnect();
		}
		
		return result;
	}
	@Override
	List<Package> processPackageResult(ResultSet rs) 
	{
		List<Package> result = new ArrayList<Package>();
		try
		{
			while(rs.next())
			{
				String stopName = "";
				String courierName = "";
				String uName = "";
				
				PreparedStatement stmnt = connection.prepareStatement("select Name from Stop where stop_id=?");
				stmnt.setInt(1, rs.getInt("stop_id"));
				ResultSet stop = stmnt.executeQuery();
				if(stop.next())
				{
					stopName = stop.getString("Name");
				}
				stop.close();
				
				stmnt = connection.prepareStatement("select Name from Courier where courier_id=?");
				stmnt.setInt(1, rs.getInt("courier_id"));
				ResultSet courier = stmnt.executeQuery();
				if(courier.next())
				{
					courierName = courier.getString("Name");
				}
				courier.close();
				
				stmnt = connection.prepareStatement("select User_Name from User where user_id=?");
				stmnt.setInt(1, rs.getInt("processor"));
				ResultSet username = stmnt.executeQuery();
				if(username.next())
				{
					uName = username.getString("User_Name");
				}
				username.close();
				
				result.add(new Package(rs.getInt("package_id"), rs.getString("Tracking_Number"), rs.getString("Date"),
						rs.getString("ASU_Email"), rs.getString("First_Name"), rs.getString("Last_Name"),
						rs.getString("Box_Number"), stopName, courierName,
						uName, rs.getBoolean("At_Stop"), rs.getBoolean("Picked_Up"), 
						rs.getString("Pick_Up_Date"), rs.getBoolean("Returned")));
			}
		}
		catch(SQLException e)
		{
			System.err.println("Error: " + e.getMessage());
		}
		
		return result;
	}

	//Search Actions//
	@Override
	public List<Person> findPerson(String firstName, String lastName, String boxOffice)
	{
		List<Person> results = new ArrayList<Person>();
		
		try
		{
			connect();
			PreparedStatement stmnt = connection.prepareStatement("select * from Person where First_Name like ? and Last_Name like ? and Number=?");
			stmnt.setQueryTimeout(5);
			stmnt.setString(1, firstName);
			stmnt.setString(2, lastName);
			stmnt.setString(3, boxOffice);
			
			ResultSet rs = stmnt.executeQuery();
			
			//process ResultSet
			while(rs.next())
			{
				for(Stop s : stops)
				{
					if(s.getStopId() == rs.getInt("stop_id"))
					{
						results.add(new Person(rs.getString("ID_Number"), rs.getString("ASU_Email"), rs.getString("First_Name"), 
								rs.getString("Last_Name"), rs.getString("Number"), s.getStopName()));
						break;
					}
				}
			}
		}
		catch(SQLException e)
		{
			System.err.println("Error: " + e.getMessage());
		}
		finally
		{
			disconnect();
		}
		
		return results;
	}
	
	//Normal Actions//
	@Override
	public void connect()
	{
		try
		{
			connection = DriverManager.getConnection("jdbc:sqlite:" + dbLocation);
		}
		catch(SQLException e)
		{
			System.err.println("Error Occured: " + e.getMessage());
		}
	}
	@Override
	public void disconnect()
	{
		if(connection != null)
		{
			try
			{
				connection.close();
			}
			catch(SQLException e)
			{
				System.err.println("Error: " + e.getMessage());
			}
			finally
			{
				connection = null;
			}
		}
	}
	@Override
	public void dispose()
	{
		couriers = null;
		routes = null;
		stops = null;
		packages = null;
		
		return;
	}
	@Override
	public boolean create(boolean insertDev)
	{
		boolean retValue = false;
		try
		{
			connect();
			
			Statement stmnt = connection.createStatement();
			if(stmnt.execute(createString))
			{
				retValue = true;
			}
			
			if(insertDev)
			{
				if(stmnt.execute(devString))
				{
					retValue = true;
				}
			}
		}
		catch(SQLException e)
		{
			System.err.println("Error: " + e.getMessage());
			retValue = false;
		}
		finally
		{
			disconnect();
		}
		
		return retValue;
	}
}