package com.mailroom.common;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Custom Logger Class for logging Errors and Exceptions
 * <br>
 * Will put them into a database located in logs
 * @author James
 *
 */
public class Logger
{
	/**
	 * Create Statement for Error File
	 * <br>
	 * Could make a single database file but this seems better to have one per day
	 */
	private final static String create = "CREATE TABLE IF NOT EXISTS Error(error_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,error_message VARCHAR(100) NOT NULL,error_stacktrace TEXT NOT NULL)";
	
	/**
	 * Logs data from Exception
	 * @param ex Exception caught
	 * @return status of logging success
	 */
	public static boolean log(Exception ex)
	{
		boolean retVal = true;
		
		Date d = new Date();
		String sDate = new SimpleDateFormat("yyyy-MM-dd").format(d).toString();
		File f = new File("./Logs/" + sDate + ".err");
		
		try
		{
			Class.forName("com.sqlite.JDBC");
		}
		catch (ClassNotFoundException e)
		{
			System.err.println("Logging Error");
			retVal = false;
		}
		
		if(!f.exists())
		{
			try
			{
				f.createNewFile();
				
				Connection con = DriverManager.getConnection("jdbc:sqlite:" + f.getAbsolutePath());
				Statement stmnt = con.createStatement();
				
				stmnt.executeQuery(create);
				
				stmnt.close();
				con.close();
			}
			catch (IOException | SQLException e)
			{
				System.err.println("Logging Error");
				retVal = false;
			}
		}
		
		try
		{
			Connection con = DriverManager.getConnection("jdbc:sqlite:" + f.getAbsolutePath());
			java.sql.PreparedStatement stmnt = con.prepareStatement("insert into Error(error_messsage, error_stacktrace) values(?,?)");
			
			stmnt.setString(1, ex.getMessage());
			stmnt.setString(2, ex.getStackTrace().toString());
			
			if(!stmnt.execute())
			{
				retVal = false;
			}
			
			stmnt.close();
			con.close();
		}
		catch (SQLException e)
		{
			System.err.println("Logging Error");
			retVal = false;
		}
		
		return retVal;
	}
}
