package com.mailroom.common.objects;

import com.mailroom.common.interfaces.IDatabaseObject;
import com.mailroom.common.interfaces.IJSONable;
import com.mailroom.common.utils.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.sql.*;

/**
 * Similar to the SQLiteManager however only works for opening the log files
 * Contains logic for converting log file to JSON
 * TODO: add XML?
 *
 * Created by james on 7/16/15.
 * Updated by james on 1/25/16
 */
public class SysLog implements IJSONable
{
    private String logLocation;

    /**
     * Creates new SysLog
     * @param logLocation location of the log file. these are SQLite databases, and have a .db extension and found in the Log directory
     */
    public SysLog(String logLocation)
    {
        logLocation = logLocation.replace('\\', '/');

        this.logLocation = logLocation;

        try
        {
            Class.forName("org.sqlite.JDBC");
        }
        catch(ClassNotFoundException e)
        {
            Logger.logException(e);
        }
    }

    /**
     * Counts number of errors in log
     * @return number of errors in log
     */
    public int countErrors()
    {
        Connection connection = connect();
        try
        {
            Statement stmnt = connection.createStatement();
            ResultSet rs = stmnt.executeQuery("SELECT count(error_id) AS c FROM Error");

            rs.next();

            return rs.getInt("c");
        }
        catch(NullPointerException npe)
        {
            Logger.logException(npe);
        }
        catch (SQLException sqle)
        {
            Logger.logException(sqle);
        }
        finally
        {
            disconnect(connection);
        }

        return 0;
    }

    /**
     * Gets all Errors in Log
     * @return JSON encoded array of errors
     */
    public JSONArray getErrors()
    {
        Connection connection = connect();

        try
        {
            Statement stmnt = connection.createStatement();
            ResultSet rs = stmnt.executeQuery("SELECT * from Error");

            JSONArray errorArray = new JSONArray();

            while(rs.next())
            {
                JSONObject obj = new JSONObject();

                obj.put("error_id", rs.getInt("error_id"));
                obj.put("error_time", rs.getLong("error_time"));
                obj.put("error_message", rs.getString("error_message"));
                obj.put("error_stacktrace", rs.getString("error_stacktrace"));

                errorArray.add(obj);
            }

            return errorArray;
        }
        catch(SQLException sqle)
        {
            Logger.logException(sqle);
            return null;
        }
        finally
        {
            disconnect(connection);
        }
    }

    /**
     * Gets all Events in Log
     * @return JSON encoded array of events
     */
    public JSONArray getEvents()
    {
        Connection connection = connect();

        try
        {
            Statement stmnt = connection.createStatement();
            ResultSet rs = stmnt.executeQuery("SELECT * from Event");

            JSONArray eventArray = new JSONArray();

            while(rs.next())
            {
                JSONObject obj = new JSONObject();

                obj.put("event_id", rs.getInt("event_id"));
                obj.put("event_time", rs.getLong("event_time"));
                obj.put("event_message", rs.getString("event_message"));
                obj.put("event_user_name", rs.getString("event_user_name"));

                eventArray.add(obj);
            }
            return eventArray;
        }
        catch(SQLException sqle)
        {
            Logger.logException(sqle);
            return null;
        }
        finally
        {
            disconnect(connection);
        }
    }

    /**
     * Converts Log to JSON
     * @return JSON Ojbect version of Log
     */
    public JSONObject toJSON()
    {
        JSONObject logObj = new JSONObject();

        logObj.put("log_date", new File(logLocation).getName().replace(".db", ""));
        logObj.put("errors", getErrors());
        logObj.put("events", getEvents());

        return logObj;
    }

    @Override
    public IDatabaseObject fromJSON(JSONObject jsonObject)
    {
        return null;
    }

    /**
     * Converts Log to JSON String
     * @return JSON String of Log
     */
    public String toJSONString()
    {
        return toJSON().toString();
    }

    @Override
    public String toString()
    {
        return toJSONString();
    }

    /**
     * Opens Connection to Log
     * @return new Connection
     */
    public Connection connect()
    {
        try
        {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:" + logLocation);

            if(connection != null)
            {
                return connection;
            }
            else
            {
                return null;
            }
        }
        catch(SQLException sqle)
        {
            Logger.logException(sqle);
            return null;
        }
    }

    /**
     * Gets location of Log
     * @return location of Log
     */
    public String getLogLocation()
    {
        return logLocation;
    }

    /**
     * Closes connection to Log
     * @param connection current connection to the Log
     */
    private void disconnect(Connection connection)
    {
        try
        {
            connection.close();
        }
        catch(SQLException sqle)
        {
            Logger.logException(sqle);
        }
    }
}
