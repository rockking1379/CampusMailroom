package com.mailroom.common;

import java.sql.*;

/**
 * Handles all settings connections for database managers
 * @author James
 *
 */
public class SettingsManager
{
	Connection con;
	
	/**
	 * Constructs new SettingsManager
	 */
	public SettingsManager()
	{
		try
		{
			Class.forName("org.sqlite.JDBC");
		}
		catch(ClassNotFoundException e)
		{
			Logger.log(e);
		}
	}
	
	/**
	 * Gets Property Value
	 * @param id id of Property
	 * @return Property Value
	 */
	public Object getProperty(int id)
	{	
		String value = null;
		try
		{
			connect();
			
			PreparedStatement stmnt = con.prepareStatement("select * from Settings where setting_id=?");
			
			stmnt.setInt(1, id);
			
			ResultSet rs = stmnt.executeQuery();
			
			value = "";
			
			while(rs.next())
			{
				value = rs.getString("setting_value");
			}
			
			rs.close();
			stmnt.close();
		}
		catch(SQLException e)
		{
			Logger.log(e);
		}
		finally
		{
			disconnect();
		}
		return (Object)value;
	}
	
	/**
	 * Gets Property Value
	 * @param key key of Property
	 * @return Property Value
	 */
	public Object getProperty(String key)
	{
		String value = null;
		try
		{
			connect();
			
			PreparedStatement stmnt = con.prepareStatement("select * from Settings where setting_key=?");
			
			stmnt.setString(1, key);
			
			ResultSet rs = stmnt.executeQuery();
			
			value = "";
			
			while(rs.next())
			{
				value = rs.getString("setting_value");
			}
			
			rs.close();
			stmnt.close();
		}
		catch(SQLException e)
		{
			Logger.log(e);
		}
		finally
		{
			disconnect();
		}
		return (Object)value;
	}
	
	/**
	 * Sets Property Value
	 * @param id id of Property
	 * @param value Value to be set to
	 * @return status of update
	 */
	public boolean setProperty(int id, Object value)
	{
		boolean retVal = true;
		
		try
		{
			connect();
			
			PreparedStatement stmnt = con.prepareStatement("update Settings set setting_value=? where setting_id=?");
			
			stmnt.setString(1, value.toString());
			stmnt.setInt(2, id);
			
			if(!stmnt.execute())
			{
				retVal = false;
			}
			
			stmnt.close();
		}
		catch(SQLException e)
		{
			Logger.log(e);
			retVal = false;
		}
		finally
		{
			disconnect();
		}
		
		return retVal;
	}
	
	/**
	 * Sets Property Value
	 * @param key key of Property
	 * @param value Value to be set to
	 * @return status of update
	 */
	public boolean setProperty(String key, Object value)
	{
		boolean retVal = true;
		
		try
		{
			connect();
			
			PreparedStatement stmnt = con.prepareStatement("update Settings set setting_value=? where setting_key=?");
			
			stmnt.setString(1, value.toString());
			stmnt.setString(2, key);
			
			if(!stmnt.execute())
			{
				retVal = false;
			}
			
			stmnt.close();
		}
		catch(SQLException e)
		{
			Logger.log(e);
			retVal = false;
		}
		finally
		{
			disconnect();
		}
		
		return retVal;
	}
	
	/**
	 * Opens Connection to Settings Database
	 */
	private void connect()
	{
		try
		{
			con = DriverManager.getConnection("jdbc:sqlite:./configuration");
		}
		catch(SQLException e)
		{
			Logger.log(e);
		}
	}
	
	/**
	 * Closes Connection to Settings Database
	 */
	private void disconnect()
	{
		try
		{
			con.close();
		}
		catch(SQLException e)
		{
			Logger.log(e);
		}
	}
}
