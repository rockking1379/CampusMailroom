package com.mailroom.common;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A Database Manager for SQLite Database
 * <br>
 * Extends DatabaseManager
 * @author James sitzja@grizzlies.adams.edu
 *
 */
public class SQLiteManager extends DatabaseManager
{
	private static final String createString = "CREATE TABLE Route(route_id INTEGER PRIMARY KEY AUTOINCREMENT,route_name varchar(50) NOT NULL,is_used BOOLEAN NOT NULL);CREATE TABLE Stop(stop_id INTEGER PRIMARY KEY AUTOINCREMENT,stop_name varchar(50) NOT NULL,route_id int,is_used BOOLEAN NOT NULL,route_order int,Student BOOLEAN,FOREIGN KEY(route_id) REFERENCES Route(route_id));CREATE TABLE Courier(courier_id INTEGER PRIMARY KEY AUTOINCREMENT,courier_name varchar(50) NOT NULL,is_used BOOLEAN NOT NULL);CREATE TABLE Package(package_id INTEGER PRIMARY KEY AUTOINCREMENT,tracking_number varchar(50) NOT NULL,Date DATE NOT NULL,email_address varchar(50) NOT NULL,first_name varchar(50) NOT NULL,	last_name varchar(50) NOT NULL,box_number varchar(50) NOT NULL,at_stop BOOLEAN NOT NULL,picked_up BOOLEAN NOT NULL,pick_up_date DATE,stop_id int,courier_id int,user_id int,returned BOOLEAN,FOREIGN KEY(stop_id) REFERENCES Stop(stop_id),FOREIGN KEY(courier_id) REFERENCES Courier(courier_id)FOREIGN KEY(user_id) REFERENCES Users(user_id));CREATE TABLE Person(id INTEGER PRIMARY KEY AUTOINCREMENT,id_number varchar(50),email_address varchar(50),first_name varchar(50) NOT NULL,last_name varchar(50) NOT NULL,Number varchar(50),stop_id int,FOREIGN KEY(stop_id) REFERENCES Stop(stop_id));CREATE TABLE Users(user_id INTEGER PRIMARY KEY AUTOINCREMENT,user_name varchar(50) NOT NULL,first_name varchar(50) NOT NULL,last_name varchar(50) NOT NULL,password INTEGER NOT NULL,administrator BOOLEAN NOT NULL,active BOOLEAN);insert into Route(Name, is_used) values('unassigned', 1);insert into Stop(Name,route_id,is_used,route_order,Student) values('unassigned',1,1,0,0);";
	private static final String devString = "insert into Users(user_name, first_name, last_name, password, administrator, active) values('DEV', 'Developer', 'Access', 2145483,1,1);";

	/**
	 * Used in config file to identify database manager type to create
	 */
	public static final int dbId = 0;
	/**
	 * Used in Settings for displaying name
	 */
	public static final String dbName = "0:SQLite";
	
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
			Logger.log(e);
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
			PreparedStatement stmt = connection.prepareStatement("select * from Users where user_name=? and password=? and active=1");
			stmt.setQueryTimeout(5);
			stmt.setString(1, userName);
			stmt.setInt(2, password);
			
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next())
			{
				u = new User(rs.getInt("user_id"), rs.getString("user_name"), rs.getString("first_name"), rs.getString("last_name"), rs.getBoolean("administrator"));
			}
		}
		catch(SQLException e)
		{
			Logger.log(e);
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
			PreparedStatement stmnt = connection.prepareStatement("insert into Users(user_name, first_name, last_name, password, administrator, active) values(?,?,?,?,?,1)");
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
			Logger.log(e);
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
			PreparedStatement stmnt = connection.prepareStatement("update Users set password=? where user_name=? and password=?");
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
			Logger.log(e);
			return false;
		}
		finally
		{
			disconnect();
		}
	}
	@Override
	public boolean deleteUser(User u) 
	{
		boolean retValue = false;
		try
		{
			connect();
			PreparedStatement stmnt = connection.prepareStatement("update Users set active=0 where user_id=?");
			
			stmnt.setInt(1,u.getUserId());
			
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
			Logger.log(e);
			retValue = false;
		}
		finally
		{
			disconnect();
		}
		
		return retValue;
	}
	@Override
	public boolean setUserAdmin(User u, boolean status)
	{
		boolean retValue = false;
		try
		{
			connect();
			
			PreparedStatement stmnt = connection.prepareStatement("update Users set administrator=1 where user_id=?");
			
			stmnt.setInt(1, u.getUserId());
			
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
			Logger.log(e);
			retValue = false;
		}
		finally
		{
			disconnect();
		}
		
		return retValue;
	}
	@Override
	public boolean reactivateUser(User u, int password)
	{
		boolean retValue = false;
		try
		{
			connect();
			
			PreparedStatement stmnt = connection.prepareStatement("update Users set active=1, password=? where user_id=?");
			
			stmnt.setInt(1, password);
			stmnt.setInt(2, u.getUserId());
			
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
			Logger.log(e);
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
			
			ResultSet rs = stmnt.executeQuery("select * from Users where active=0");
			
			while(rs.next())
			{
				result.add(new User(rs.getInt("user_id"), rs.getString("user_name"), rs.getString("first_name"), rs.getString("last_name"), rs.getBoolean("administrator")));
			}
		}
		catch(SQLException e)
		{
			Logger.log(e);
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
			
			ResultSet rs = stmnt.executeQuery("select * from Users where active=1");
			
			while(rs.next())
			{
				result.add(new User(rs.getInt("user_id"), rs.getString("user_name"), rs.getString("first_name"), rs.getString("last_name"), rs.getBoolean("administrator")));
			}
		}
		catch(SQLException e)
		{
			Logger.log(e);
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
			
			ResultSet rs = stmnt.executeQuery("select * from Stop where is_used=1");
			
			while(rs.next())
			{
				for(int i=0; i<routes.size(); i++)
				{
					if(routes.get(i).getRouteId() == rs.getInt("route_id"))
					{
						stops.add(new Stop(rs.getInt("stop_id"), rs.getString("stop_name"), routes.get(i).getRouteName(), rs.getInt("route_order"), rs.getBoolean("Student")));
					}
				}
			}
		}
		catch(SQLException e)
		{
			Logger.log(e);
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
			Logger.log(e);
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
			Logger.log(e);
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
			PreparedStatement stmnt = connection.prepareStatement("insert into Stop(stop_name, route_id, is_used, route_order, Student) values(?,?,1,?,?)");
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
			Logger.log(e);
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
			PreparedStatement stmnt = connection.prepareStatement("update Stop set is_used=0 where stop_id=?");
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
			Logger.log(e);
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
			
			ResultSet rs = stmnt.executeQuery("select * from Stop where route_id=1 and is_used=1");
			
			return processStopResult(rs, "unassigned");
		}
		catch(SQLException e)
		{
			Logger.log(e);
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
		if(r == null)
		{
			return null;
		}
		
		try
		{
			connect();
			PreparedStatement stmnt = connection.prepareStatement("select * from Stop where route_id=? and is_used=1");
			stmnt.setQueryTimeout(5);
			
			stmnt.setInt(1, r.getRouteId());
			
			ResultSet rs = stmnt.executeQuery();
			
			return processStopResult(rs, r.getRouteName());
		}
		catch(SQLException e)
		{
			Logger.log(e);
			return null;
		}
		finally
		{
			disconnect();
		}
	}
	@Override
	List<Stop> processStopResult(ResultSet rs, String routestop_name)
	{
		List<Stop> results = new ArrayList<Stop>();
		
		try
		{
			while(rs.next())
			{
				results.add(new Stop(rs.getInt("stop_id"), rs.getString("stop_name"), routestop_name, rs.getInt("route_order"), rs.getBoolean("Student")));
			}
		}
		catch(SQLException e)
		{
			Logger.log(e);
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
			
			ResultSet rs = stmnt.executeQuery("select * from Route where is_used=1;");
			
			while(rs.next())
			{
				routes.add(new Route(rs.getInt("route_id"), rs.getString("route_name")));
			}
		}
		catch(SQLException e)
		{
			Logger.log(e);
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
			PreparedStatement stmnt = connection.prepareStatement("update Route set route_name=? where route_id=?");
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
			Logger.log(e);
			return false;
		}
		finally
		{
			disconnect();
		}
	}
	@Override
	public boolean addRoute(String route_name) 
	{
		//Assume since route is being added that it is to be used
		try
		{
			connect();
			PreparedStatement stmnt = connection.prepareStatement("insert into Route(route_name, is_used) values(?,1)");
			stmnt.setQueryTimeout(5);
			
			stmnt.setString(1, route_name);
			
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
			Logger.log(e);
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
			PreparedStatement stmnt = connection.prepareStatement("update Route set is_used=0 where route_id=?");
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
			Logger.log(e);
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
		
			ResultSet rs = stmnt.executeQuery("select * from Courier where is_used=1");
		
			while(rs.next())
			{
				couriers.add(new Courier(rs.getInt("courier_id"), rs.getString("courier_name")));
			}
		}
		catch(SQLException e)
		{
			Logger.log(e);
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
	public boolean addCourier(String couriercourier_name)
	{
		boolean retValue = false;
		
		try
		{
			connect();
			
			PreparedStatement stmnt = connection.prepareStatement("insert into Courier(courier_name, is_used) values(?,1)");
			
			stmnt.setString(1, couriercourier_name);
			
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
			Logger.log(e);
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
			PreparedStatement stmnt = connection.prepareStatement("update Courier set courier_name=? where courier_id=?");
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
			Logger.log(e);
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
			PreparedStatement stmnt = connection.prepareStatement("update Courier set is_used=0 where courier_id=?");
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
			Logger.log(e);
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
			
			ResultSet rs = stmnt.executeQuery("select * from Package where picked_up=0 and returned=0");
			
			packages = null;
			packages = processPackageResult(rs);
		}
		catch(SQLException e)
		{
			Logger.log(e);
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
			
			stmnt = connection.prepareStatement("select * from Package where picked_up=0 and returned=0 and stop_id=?");
			stmnt.setInt(1, stopId);

			stmnt.setQueryTimeout(5);
			
			ResultSet rs = stmnt.executeQuery();
			
			packages = processPackageResult(rs);
		}
		catch(SQLException e)
		{
			Logger.log(e);
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
			
			stmnt = connection.prepareStatement("update Package set at_stop=? where package_id=?");
			stmnt.setQueryTimeout(5);
			stmnt.setBoolean(1, atStop);
			stmnt.setInt(2, packageId);
			stmnt.executeUpdate();
			
			stmnt = connection.prepareStatement("update Package set picked_up=? where package_id=?");
			stmnt.setBoolean(1, pickedUp);
			stmnt.setInt(2, packageId);
			stmnt.executeUpdate();
			
			stmnt = connection.prepareStatement("update Package set pick_up_date=? where package_id=?");
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
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
	 public boolean updatePackage(Package p)
	 {
		 try
		 {
			 connect();
			 
			 PreparedStatement stmnt = null;
			 
			 stmnt = connection.prepareStatement("update Package set tracking_number=?, email_address=?, first_name=?, last_name=?, box_number=?, at_stop=?, picked_up=?, stop_id=?, courier_id=?, returned=? where package_id=?");
			 stmnt.setQueryTimeout(5);
			 stmnt.setString(1, p.getFullTrackingNumber());
			 stmnt.setString(2, p.getEmailAddress());
			 stmnt.setString(3, p.getFirstName());
			 stmnt.setString(4, p.getLastName());
			 stmnt.setString(5, p.getBoxOffice());
			 stmnt.setBoolean(6, p.isAtStop());
			 stmnt.setBoolean(7, p.isPickedUp());
			 stmnt.setInt(8, p.getStop().getStopId());
			 stmnt.setInt(9, p.getCourier().getCourierId());
			 stmnt.setBoolean(10, p.isReturned());
			 stmnt.setInt(11, p.getPackageId());
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
	public boolean addPackage(Package p) 
	{
		try
		{
			connect();
			PreparedStatement stmnt = connection.prepareStatement("insert into Package(tracking_number, receive_date, email_address, first_name, last_name, box_number, at_stop, picked_up, stop_id, courier_id, user_id, returned)"
					+ " values(?,?,?,?,?,?,0,0,?,?,?,0)");
			stmnt.setQueryTimeout(5);
			
			stmnt.setString(1, p.getFullTrackingNumber());
			stmnt.setString(2, p.getReceivedDate());
			stmnt.setString(3, p.getEmailAddress());
			stmnt.setString(4, p.getFirstName());
			stmnt.setString(5, p.getLastName());
			stmnt.setString(6, p.getBoxOffice());
			stmnt.setInt(7, p.getStop().getStopId());
			stmnt.setInt(8, p.getCourier().getCourierId());
			stmnt.setInt(9, p.getUser().getUserId());
			
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
			Logger.log(e);
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
			
			PreparedStatement stmnt = connection.prepareStatement("select * from Package where receive_date=? and stop_id=? and at_stop=0");
			
			Date d = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			
			stmnt.setString(1, format.format(d).toString());
			stmnt.setInt(2, s.getStopId());
			
			ResultSet rs = stmnt.executeQuery();
			
			result = (ArrayList<Package>) processPackageResult(rs);
		}
		catch(SQLException e)
		{
			Logger.log(e);
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
				Stop stop = null;
				Courier courier = null;
				User user = null;
				PreparedStatement stmnt = null;
				
				for(Stop s : stops)
				{
					if(s.getStopId() == rs.getInt("stop_id"))
					{
						stop = s;
						break;
					}
				}
				if(stop == null)
				{
					stmnt = connection.prepareStatement("select * from Stop where stop_id=?");
					stmnt.setInt(1, rs.getInt("stop_id"));
					ResultSet s = stmnt.executeQuery();
					if(s.next())
					{
						stop = new Stop(s.getInt("stop_id"), s.getString("stop_name"),"Unknown", 0, s.getBoolean("student"));
					}
				}
				
				stmnt = connection.prepareStatement("select * from Courier where courier_id=?");
				stmnt.setInt(1, rs.getInt("courier_id"));
				ResultSet c = stmnt.executeQuery();
				if(c.next())
				{
					courier = new Courier(c.getInt("courier_id"), c.getString("courier_name"));
				}
				c.close();
				
				stmnt = connection.prepareStatement("select * from Users where user_id=?");
				stmnt.setInt(1, rs.getInt("user_id"));
				ResultSet u = stmnt.executeQuery();
				if(u.next())
				{
					user = new User(u.getInt("user_id"), u.getString("user_name"), u.getString("first_name"), u.getString("last_name"), u.getBoolean("administrator"));
				}
				u.close();
				
				result.add(new Package(rs.getInt("package_id"), rs.getString("tracking_number"), rs.getString("receive_date"),
						rs.getString("email_address"), rs.getString("first_name"), rs.getString("last_name"),
						rs.getString("box_number"), stop, courier,
						user, rs.getBoolean("at_stop"), rs.getBoolean("picked_up"), 
						rs.getString("pick_up_date"), rs.getBoolean("returned")));
			}
		}
		catch(SQLException e)
		{
			Logger.log(e);
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
			PreparedStatement stmnt = connection.prepareStatement("select * from Person where first_name like ? and last_name like ? and box_number=?");
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
						results.add(new Person(rs.getString("id_number"), rs.getString("email_address"), rs.getString("first_name"), 
								rs.getString("last_name"), rs.getString("box_number"), s.getStopName()));
						break;
					}
				}
			}
		}
		catch(SQLException e)
		{
			Logger.log(e);
		}
		finally
		{
			disconnect();
		}
		
		return results;
	}	
	@Override
	public List<Package> searchPackages(String search, SearchType sType)
	{
		List<Package> results = null;
		try
		{
			connect();
			
			switch(sType)
			{
				case SEARCH_BEGINS_WITH:
				{
					search = search + "%";
					break;
				}
				case SEARCH_CONTAINS:
				{
					search = "%" + search + "%";
					break;
				}
				case SEARCH_ENDS_WITH:
				{
					search = "%" + search;
					break;
				}
			}
			
			PreparedStatement stmnt = connection.prepareStatement("select * from Package where tracking_number like ? or first_name like ? or last_name like ? order by package_id desc");
			
			stmnt.setString(1, search);
			stmnt.setString(2, search);
			stmnt.setString(3, search);
			
			ResultSet rs = stmnt.executeQuery();
			
			results = processPackageResult(rs);
		}
		catch(SQLException e)
		{
			Logger.log(e);
		}
		finally
		{
			disconnect();
		}
		
		return results;
	}
	@Override
	public List<Package> searchPackages(String search, String startDate, String endDate, SearchType sType)
	{
		List<Package> results = null;
		try
		{
			connect();
			
			switch(sType)
			{
				case SEARCH_BEGINS_WITH:
				{
					search = search + "%";
					break;
				}
				case SEARCH_CONTAINS:
				{
					search = "%" + search + "%";
					break;
				}
				case SEARCH_ENDS_WITH:
				{
					search = "%" + search;
					break;
				}
			}
			
			PreparedStatement stmnt = connection.prepareStatement("select * from Package where receive_date between ? and ? and tracking_number like ? or first_name like ? or last_name like ?");
			
			stmnt.setString(1, startDate);
			stmnt.setString(2, endDate);
			stmnt.setString(3, search);
			stmnt.setString(4, search);
			stmnt.setString(5, search);
			
			ResultSet rs = stmnt.executeQuery();
			
			results = processPackageResult(rs);
		}
		catch(SQLException e)
		{
			Logger.log(e);
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
			Logger.log(e);
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
				Logger.log(e);
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
			Logger.log(e);
			retValue = false;
		}
		finally
		{
			disconnect();
		}
		
		return retValue;
	}
	@Override
	public boolean verify()
	{
		try
		{
			connect();
			if(!connection.isClosed())
			{
				disconnect();
				return true;
			}
			else
			{
				disconnect();
				return false;
			}
		}
		catch (SQLException e)
		{
			Logger.log(e);
			return false;
		}
	}
}