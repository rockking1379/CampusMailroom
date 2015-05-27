package com.mailroom.common.database;

import com.mailroom.common.objects.*;
import com.mailroom.common.objects.Package;
import com.mailroom.common.utils.Logger;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Database Manager for MySQL Database <br>
 * Extends DatabaseManager
 *
 * @author James rockking1379@gmail.com
 */
public class MysqlManager implements DatabaseManager
{
    private static final String packageDrop = "DROP TABLE IF EXISTS Package";
    private static final String userDrop = "DROP TABLE IF EXISTS Users";
    private static final String personDrop = "DROP TABLE IF EXISTS Person";
    private static final String courierDrop = "DROP TABLE IF EXISTS Courier";
    private static final String stopDrop = "DROP TABLE IF EXISTS Stop";
    private static final String routeDrop = "DROP TABLE IF EXISTS Route";
    private static final String routeString = "CREATE TABLE Route(route_id INTEGER PRIMARY KEY AUTO_INCREMENT,route_name VARCHAR(50) NOT NULL,is_used BOOLEAN)";
    private static final String stopString = "CREATE TABLE Stop(stop_id INTEGER PRIMARY KEY AUTO_INCREMENT,stop_name VARCHAR(50) NOT NULL,route_id INTEGER,is_used BOOLEAN NOT NULL,route_order INTEGER,student BOOLEAN, auto_remove BOoLEAN,FOREIGN KEY(route_id) REFERENCES Route(route_id))";
    private static final String courierString = "CREATE TABLE Courier(courier_id INTEGER PRIMARY KEY AUTO_INCREMENT,courier_name VARCHAR(50) NOT NULL,is_used BOOLEAN NOT NULL)";
    private static final String personString = "CREATE TABLE Person(person_id INTEGER PRIMARY KEY AUTO_INCREMENT,id_number VARCHAR(50),email_address VARCHAR(50),first_name VARCHAR(50) NOT NULL,last_name VARCHAR(50) NOT NULL,box_number VARCHAR(50),stop_id INTEGER,FOREIGN KEY(stop_id) REFERENCES Stop(stop_id))";
    private static final String userString = "CREATE TABLE Users(user_id INTEGER PRIMARY KEY AUTO_INCREMENT,user_name VARCHAR(50) NOT NULL,first_name VARCHAR(50) NOT NULL,last_name VARCHAR(50) NOT NULL,password INTEGER NOT NULL,administrator BOOLEAN NOT NULL,active BOOLEAN)";
    private static final String packageString = "CREATE TABLE Package(package_id INTEGER PRIMARY KEY AUTO_INCREMENT,tracking_number VARCHAR(50) NOT NULL,receive_date DATE NOT NULL,email_address VARCHAR(50) NOT NULL,first_name VARCHAR(50) NOT NULL,last_name VARCHAR(50) NOT NULL,box_number VARCHAR(50) NOT NULL,at_stop BOOLEAN NOT NULL,picked_up BOOLEAN NOT NULL,pick_up_date DATE,stop_id INTEGER,courier_id INTEGER,user_id INTEGER,returned BOOLEAN,FOREIGN KEY(stop_id) REFERENCES Stop(stop_id),FOREIGN KEY(courier_id) REFERENCES Courier(courier_id),FOREIGN KEY(user_id) REFERENCES Users(user_id))";
    private static final String routeInsert = "INSERT INTO Route(route_name, is_used) VALUES('unassigned', 1)";
    private static final String stopInsert = "INSERT INTO Stop(stop_name,route_id,is_used,route_order,student,auto_remove) VALUES('unassigned',1,1,0,0,0)";
    private static final String devString = "INSERT INTO Users(user_name, first_name, last_name, password, administrator, active) VALUES('DEV', 'Developer', 'Access', 2145483,1,1);";

    /**
     * Used in Settings database to identify database manager type to create
     */
    public static final int dbId = 1;
    /**
     * Used in Settings for displaying name
     */
    public static final String dbName = "1:MySQL";

    private Connection connection;
    private String conString;

    private List<Courier> couriers;
    private List<Route> routes;
    private List<Stop> stops;
    private List<com.mailroom.common.objects.Package> packages;

    /*
     * Constructs new Database Manager configured for MySQL
     *
     * @param dbLocation server address of database
     *
     * @param dbUsername username for database server
     *
     * @param dbPassword password for database server
     *
     * @param dbName name of database
     */
    public MysqlManager(String dbLocation, String dbUsername,
                        String dbPassword, String dbName)
    {

        try
        {
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch (ClassNotFoundException e)
        {
            Logger.logException(e);
            e.printStackTrace();
        }

        conString = "jdbc:mysql://" + dbLocation + "/" + dbName + "?user="
                + dbUsername + "&password=" + dbPassword;
        loadRoutes();
        loadStops();
        loadCouriers();
    }

    // User Actions//
    /*
     * (non-Javadoc)
	 * 
	 * @see com.mailroom.common.database.DatabaseManager#login(java.lang.String, int)
	 */
    @Override
    public User login(String userName, int password)
    {
        User u = new User(-1, null, null, null, false);

        // conduct login if successful recreate 'u' to valid user or return with
        // nulls and show GUI error
        try
        {
            connect();
            PreparedStatement stmt = connection
                    .prepareStatement("SELECT * FROM Users WHERE user_name=? AND password=? AND active=1");
            stmt.setQueryTimeout(5);
            stmt.setString(1, userName);
            stmt.setInt(2, password);

            ResultSet rs = stmt.executeQuery();

            while (rs.next())
            {
                u = new User(rs.getInt("user_id"), rs.getString("user_name"),
                        rs.getString("first_name"), rs.getString("last_name"),
                        rs.getBoolean("administrator"));
            }
        }
        catch (SQLException e)
        {
            Logger.logException(e);
        }
        finally
        {
            disconnect();
        }

        return u;
    }

    @Override
    public User login(String userName, byte[] password)
    {
        User u = new User(-1, null, null, null, false);

        // conduct login if successful recreate 'u' to valid user or return with
        // nulls and show GUI error
        try
        {
            connect();
            PreparedStatement stmt = connection
                    .prepareStatement("SELECT * FROM Users WHERE user_name=? AND password=? AND active=1");
            stmt.setQueryTimeout(5);
            stmt.setString(1, userName);
            stmt.setBytes(2, password);

            ResultSet rs = stmt.executeQuery();

            while (rs.next())
            {
                u = new User(rs.getInt("user_id"), rs.getString("user_name"),
                        rs.getString("first_name"), rs.getString("last_name"),
                        rs.getBoolean("administrator"));
            }
        }
        catch (SQLException e)
        {
            Logger.logException(e);
        }
        finally
        {
            disconnect();
        }

        return u;
    }

    @Override
    public boolean addUser(User u, byte[] password)
    {
        // conduct insert into user table here
        // settings option should only be available to admin
        try
        {
            connect();
            PreparedStatement stmnt = connection
                    .prepareStatement("INSERT INTO Users(user_name, first_name, last_name, password, administrator, active) VALUES(?,?,?,?,?,1)");
            stmnt.setQueryTimeout(5);

            stmnt.setString(1, u.getUserName());
            stmnt.setString(2, u.getFirstName());
            stmnt.setString(3, u.getLastName());
            stmnt.setBytes(4, password);
            stmnt.setBoolean(5, u.getAdmin());

            return stmnt.executeUpdate() > 0;
        }
        catch (SQLException e)
        {
            Logger.logException(e);
            return false;
        }
        finally
        {
            disconnect();
        }
    }

    @Override
    public boolean changePassword(User u, byte[] oldPassword, byte[] newPassword)
    {
        // allow users to change their password
        try
        {
            connect();
            PreparedStatement stmnt = connection
                    .prepareStatement("UPDATE Users SET password=? WHERE user_name=? AND password=?");
            stmnt.setQueryTimeout(5);

            stmnt.setBytes(1, newPassword);
            stmnt.setString(2, u.getUserName());
            stmnt.setBytes(3, oldPassword);

            return stmnt.executeUpdate() > 0;
        }
        catch (SQLException e)
        {
            Logger.logException(e);
            return false;
        }
        finally
        {
            disconnect();
        }
    }

    @Override
    public boolean changePassword(User u, int oldPassword, byte[] newPassword)
    {
        // allow users to change their password
        try
        {
            connect();
            PreparedStatement stmnt = connection
                    .prepareStatement("UPDATE Users SET password=? WHERE user_name=? AND password=?");
            stmnt.setQueryTimeout(5);

            stmnt.setBytes(1, newPassword);
            stmnt.setString(2, u.getUserName());
            stmnt.setInt(3, oldPassword);

            return stmnt.executeUpdate() > 0;
        }
        catch (SQLException e)
        {
            Logger.logException(e);
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
            PreparedStatement stmnt = connection
                    .prepareStatement("UPDATE Users SET active=0 WHERE user_id=?");

            stmnt.setInt(1, u.getUserId());

            retValue = stmnt.executeUpdate() > 0;
        }
        catch (SQLException e)
        {
            Logger.logException(e);
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

            PreparedStatement stmnt = connection
                    .prepareStatement("UPDATE Users SET administrator=? WHERE user_id=?");

            stmnt.setBoolean(1, status);
            stmnt.setInt(2, u.getUserId());

            retValue = stmnt.executeUpdate() > 0;
        }
        catch (SQLException e)
        {
            Logger.logException(e);
            retValue = false;
        }
        finally
        {
            disconnect();
        }

        return retValue;
    }

    @Override
    public boolean reactivateUser(User u, byte[] password)
    {
        boolean retValue = false;
        try
        {
            connect();

            PreparedStatement stmnt = connection
                    .prepareStatement("UPDATE Users SET active=1, password=? WHERE user_id=?");

            stmnt.setBytes(1, password);
            stmnt.setInt(2, u.getUserId());

            retValue = stmnt.executeUpdate() > 0;
        }
        catch (SQLException e)
        {
            Logger.logException(e);
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

            ResultSet rs = stmnt
                    .executeQuery("SELECT * FROM Users WHERE active=0");

            while (rs.next())
            {
                result.add(new User(rs.getInt("user_id"), rs
                        .getString("user_name"), rs.getString("first_name"), rs
                        .getString("last_name"), rs.getBoolean("administrator")));
            }
        }
        catch (SQLException e)
        {
            Logger.logException(e);
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

            ResultSet rs = stmnt
                    .executeQuery("SELECT * FROM Users WHERE active=1");

            while (rs.next())
            {
                result.add(new User(rs.getInt("user_id"), rs
                        .getString("user_name"), rs.getString("first_name"), rs
                        .getString("last_name"), rs.getBoolean("administrator")));
            }
        }
        catch (SQLException e)
        {
            Logger.logException(e);
        }
        finally
        {
            disconnect();
        }

        return result;
    }

    // Stop Actions//
    @Override
    public void loadStops()
    {
        try
        {
            connect();
            stops = new ArrayList<Stop>();

            Statement stmnt = connection.createStatement();
            stmnt.setQueryTimeout(5);

            ResultSet rs = stmnt
                    .executeQuery("SELECT * FROM Stop WHERE is_used=1 ORDER BY stop_name ASC");

            while (rs.next())
            {
                for (Route route : routes)
                {
                    if (route.getRouteId() == rs.getInt("route_id"))
                    {
                        stops.add(new Stop(rs.getInt("stop_id"), rs
                                .getString("stop_name"), route
                                .getRouteName(), rs.getInt("route_order"), rs
                                .getBoolean("Student"), rs.getBoolean("auto_remove")));
                    }
                }
            }
        }
        catch (SQLException e)
        {
            Logger.logException(e);
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
            PreparedStatement stmnt = connection
                    .prepareStatement("UPDATE Stop SET student=? WHERE stop_id=?");
            stmnt.setQueryTimeout(5);

            stmnt.setBoolean(1, s.getStudent());
            stmnt.setInt(2, s.getStopId());

            return stmnt.executeUpdate() > 0;
        }
        catch (SQLException e)
        {
            Logger.logException(e);
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
            PreparedStatement stmnt = connection
                    .prepareStatement("UPDATE Stop SET route_id=? WHERE stop_id=?");

            stmnt.setInt(1, r.getRouteId());
            stmnt.setInt(2, s.getStopId());

            retValue = stmnt.executeUpdate() > 0;
        }
        catch (SQLException e)
        {
            Logger.logException(e);
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
        // the passed in stop should receive a stopId of -1(ignored on insert
        // anyway)
        try
        {
            connect();
            PreparedStatement stmnt = connection
                    .prepareStatement("INSERT INTO Stop(stop_name, route_id, is_used, route_order, student) VALUES(?,?,1,?,?)");
            stmnt.setQueryTimeout(5);

            stmnt.setString(1, s.getStopName());
            for (Route r : routes)
            {
                if (r.getRouteName().equals(s.getRouteName()))
                {
                    stmnt.setInt(2, r.getRouteId());
                    break;
                }
            }
            stmnt.setInt(3, s.getRouteOrder());
            stmnt.setBoolean(4, s.getStudent());

            return stmnt.executeUpdate() > 0;
        }
        catch (SQLException e)
        {
            Logger.logException(e);
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
            PreparedStatement stmnt = connection
                    .prepareStatement("UPDATE Stop SET is_used=0 WHERE stop_id=?");
            stmnt.setQueryTimeout(5);

            stmnt.setInt(1, s.getStopId());

            return stmnt.executeUpdate() > 0;
        }
        catch (SQLException e)
        {
            Logger.logException(e);
            return false;
        }
        finally
        {
            disconnect();
        }
    }

    @Override
    public boolean setRoutePosition(Stop s, int pos)
    {
        try
        {
            connect();

            PreparedStatement stmnt = connection
                    .prepareStatement("UPDATE Stop SET route_order=? WHERE stop_id=?");

            stmnt.setInt(1, pos);
            stmnt.setInt(2, s.getStopId());

            return stmnt.executeUpdate() > 0;
        }
        catch (SQLException e)
        {
            Logger.logException(e);
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

            ResultSet rs = stmnt
                    .executeQuery("SELECT * FROM Stop WHERE route_id=1 AND is_used=1");

            return processStopResult(rs, "unassigned");
        }
        catch (SQLException e)
        {
            Logger.logException(e);
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
        if (r == null)
        {
            return null;
        }

        try
        {
            connect();
            PreparedStatement stmnt = connection
                    .prepareStatement("SELECT * FROM Stop WHERE route_id=? AND is_used=1");
            stmnt.setQueryTimeout(5);

            stmnt.setInt(1, r.getRouteId());

            ResultSet rs = stmnt.executeQuery();

            return processStopResult(rs, r.getRouteName());
        }
        catch (SQLException e)
        {
            Logger.logException(e);
            return null;
        }
        finally
        {
            disconnect();
        }
    }

    @Override
    public List<Stop> processStopResult(ResultSet rs, String route_name)
    {
        List<Stop> results = new ArrayList<Stop>();

        try
        {
            while (rs.next())
            {
                results.add(new Stop(rs.getInt("stop_id"), rs
                        .getString("stop_name"), route_name, rs
                        .getInt("route_order"), rs.getBoolean("Student")));
            }
        }
        catch (SQLException e)
        {
            Logger.logException(e);
            return null;
        }

        return results;
    }

    // Route Actions//
    @Override
    public void loadRoutes()
    {
        try
        {
            connect();
            routes = new ArrayList<Route>();

            Statement stmnt = connection.createStatement();
            stmnt.setQueryTimeout(5);

            ResultSet rs = stmnt
                    .executeQuery("SELECT * FROM Route WHERE is_used=1 ORDER BY route_name ASC;");

            while (rs.next())
            {
                routes.add(new Route(rs.getInt("route_id"), rs
                        .getString("route_name")));
            }
        }
        catch (SQLException e)
        {
            Logger.logException(e);
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
            PreparedStatement stmnt = connection
                    .prepareStatement("UPDATE Route SET route_name=? WHERE route_id=?");
            stmnt.setQueryTimeout(5);

            stmnt.setString(1, r.getRouteName());
            stmnt.setInt(2, r.getRouteId());

            return stmnt.executeUpdate() > 0;
        }
        catch (SQLException e)
        {
            Logger.logException(e);
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
        // Assume since route is being added that it is to be used
        try
        {
            connect();
            PreparedStatement stmnt = connection
                    .prepareStatement("INSERT INTO Route(route_name, is_used) VALUES(?,1)");
            stmnt.setQueryTimeout(5);

            stmnt.setString(1, route_name);

            return stmnt.executeUpdate() > 0;
        }
        catch (SQLException e)
        {
            Logger.logException(e);
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
            PreparedStatement stmnt = connection
                    .prepareStatement("UPDATE Route SET is_used=0 WHERE route_id=?");
            stmnt.setQueryTimeout(5);

            stmnt.setInt(1, r.getRouteId());

            if (stmnt.executeUpdate() > 0)
            {
                stmnt = connection
                        .prepareStatement("UPDATE Stop SET route_id=1 WHERE route_id=?");
                stmnt.setInt(1, r.getRouteId());

                stmnt.executeUpdate();
                return true;
            }
            else
            {
                return false;
            }
        }
        catch (SQLException e)
        {
            Logger.logException(e);
            return false;
        }
        finally
        {
            disconnect();
        }
    }

    // Courier Actions//
    @Override
    public void loadCouriers()
    {
        try
        {
            connect();
            couriers = new ArrayList<Courier>();

            Statement stmnt = connection.createStatement();
            stmnt.setQueryTimeout(5);

            ResultSet rs = stmnt
                    .executeQuery("SELECT * FROM Courier WHERE is_used=1 ORDER BY courier_name ASC");

            while (rs.next())
            {
                couriers.add(new Courier(rs.getInt("courier_id"), rs
                        .getString("courier_name")));
            }
        }
        catch (SQLException e)
        {
            Logger.logException(e);
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
    public boolean addCourier(String courier_name)
    {
        boolean retValue = false;

        try
        {
            connect();

            PreparedStatement stmnt = connection
                    .prepareStatement("INSERT INTO Courier(courier_name, is_used) VALUES(?,1)");

            stmnt.setString(1, courier_name);

            retValue = stmnt.execute();
        }
        catch (SQLException e)
        {
            Logger.logException(e);
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
            PreparedStatement stmnt = connection
                    .prepareStatement("UPDATE Courier SET courier_name=? WHERE courier_id=?");
            stmnt.setQueryTimeout(5);

            stmnt.setString(1, c.getCourierName());
            stmnt.setInt(2, c.getCourierId());

            return stmnt.executeUpdate() > 0;
        }
        catch (SQLException e)
        {
            Logger.logException(e);
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
            PreparedStatement stmnt = connection
                    .prepareStatement("UPDATE Courier SET is_used=0 WHERE courier_id=?");
            stmnt.setQueryTimeout(5);

            stmnt.setInt(1, c.getCourierId());

            return stmnt.executeUpdate() > 0;
        }
        catch (SQLException e)
        {
            Logger.logException(e);
            return false;
        }
        finally
        {
            disconnect();
        }
    }

    // Package Actions//
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

            ResultSet rs = stmnt
                    .executeQuery("SELECT * FROM Package WHERE picked_up=0 AND returned=0");

            packages = null;
            packages = processPackageResult(rs);
        }
        catch (SQLException e)
        {
            Logger.logException(e);
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
            PreparedStatement stmnt;

            stmnt = connection
                    .prepareStatement("SELECT * FROM Package WHERE picked_up=0 AND returned=0 AND stop_id=?");
            stmnt.setInt(1, stopId);

            stmnt.setQueryTimeout(5);

            ResultSet rs = stmnt.executeQuery();

            packages = processPackageResult(rs);
        }
        catch (SQLException e)
        {
            Logger.logException(e);
        }
        finally
        {
            disconnect();
        }
    }

    @Override
    public int checkTrackingNumber(String trackingNumber)
    {
        int packageId = -1;

        try
        {
            connect();

            PreparedStatement stmnt = connection
                    .prepareStatement("SELECT package_id FROM Package WHERE tracking_number=?");
            stmnt.setString(1, trackingNumber);

            ResultSet rs = stmnt.executeQuery();

            while (rs.next())
            {
                packageId = rs.getInt("package_id");
            }
        }
        catch (SQLException e)
        {
            Logger.logException(e);
        }
        finally
        {
            disconnect();
        }

        return packageId;
    }

    @Override
    public boolean updatePackage(int packageId, boolean atStop, boolean pickedUp)
    {
        try
        {
            connect();
            PreparedStatement stmnt;

            stmnt = connection
                    .prepareStatement("UPDATE Package SET at_stop=? WHERE package_id=?");
            stmnt.setQueryTimeout(5);
            stmnt.setBoolean(1, atStop);
            stmnt.setInt(2, packageId);
            stmnt.executeUpdate();

            stmnt = connection
                    .prepareStatement("UPDATE Package SET picked_up=? WHERE package_id=?");
            stmnt.setBoolean(1, pickedUp);
            stmnt.setInt(2, packageId);
            stmnt.executeUpdate();

            stmnt = connection
                    .prepareStatement("UPDATE Package SET pick_up_date=? WHERE package_id=?");
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date now = new Date();
            String date = format.format(now);
            stmnt.setString(1, date);
            stmnt.setInt(2, packageId);
            stmnt.executeUpdate();

            return true;
        }
        catch (SQLException e)
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

            PreparedStatement stmnt;

            stmnt = connection
                    .prepareStatement("UPDATE Package SET tracking_number=?, email_address=?, first_name=?, last_name=?, box_number=?, at_stop=?, picked_up=?, stop_id=?, courier_id=?, returned=? WHERE package_id=?");
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
        catch (SQLException e)
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
            PreparedStatement stmnt = connection
                    .prepareStatement("INSERT INTO Package(tracking_number, receive_date, email_address, first_name, last_name, box_number, at_stop, picked_up, stop_id, courier_id, user_id, returned)"
                            + " VALUES(?,?,?,?,?,?,0,0,?,?,?,0)");
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

            return stmnt.executeUpdate() > 0;
        }
        catch (SQLException e)
        {
            Logger.logException(e);
            return false;
        }
        finally
        {
            disconnect();
        }
    }

    @Override
    public List<Package> printPackagesForStop(Stop s)
    {
        ArrayList<Package> result = null;

        try
        {
            connect();

            PreparedStatement stmnt;

            if (s.getStudent())
            {
                stmnt = connection
                        .prepareStatement("SELECT * FROM Package WHERE receive_date=? AND stop_id=? AND at_stop=0 ORDER BY box_number ASC");
            }
            else
            {
                stmnt = connection
                        .prepareStatement("SELECT * FROM Package WHERE receive_date=? AND stop_id=? AND at_stop=0 ORDER BY last_name ASC, first_name ASC");
            }

            Date d = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            stmnt.setString(1, format.format(d));
            stmnt.setInt(2, s.getStopId());

            ResultSet rs = stmnt.executeQuery();

            result = (ArrayList<Package>) processPackageResult(rs);
        }
        catch (SQLException ex)
        {
            Logger.logException(ex);
        }
        finally
        {
            disconnect();
        }

        return result;
    }

    @Override
    public List<Package> getPackagesForStop(Stop s)
    {
        ArrayList<Package> result = null;
        try
        {
            connect();

            PreparedStatement stmnt = connection
                    .prepareStatement("SELECT * FROM Package WHERE receive_date=? AND stop_id=? AND at_stop=0");

            Date d = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            stmnt.setString(1, format.format(d));
            stmnt.setInt(2, s.getStopId());

            ResultSet rs = stmnt.executeQuery();

            result = (ArrayList<Package>) processPackageResult(rs);
        }
        catch (SQLException e)
        {
            Logger.logException(e);
            result = null;
        }
        finally
        {
            disconnect();
        }

        return result;
    }

    @Override
    public List<Package> processPackageResult(ResultSet rs)
    {
        List<Package> result = new ArrayList<Package>();
        try
        {
            while (rs.next())
            {
                Stop stop = null;
                Courier courier = null;
                User user = null;
                PreparedStatement stmnt;

                for (Stop s : stops)
                {
                    if (s.getStopId() == rs.getInt("stop_id"))
                    {
                        stop = s;
                        break;
                    }
                }
                if (stop == null)
                {
                    stmnt = connection
                            .prepareStatement("SELECT * FROM Stop WHERE stop_id=?");
                    stmnt.setInt(1, rs.getInt("stop_id"));
                    ResultSet s = stmnt.executeQuery();
                    if (s.next())
                    {
                        stop = new Stop(s.getInt("stop_id"),
                                s.getString("stop_name"), "Unknown", 0,
                                s.getBoolean("student"));
                    }
                }

                stmnt = connection
                        .prepareStatement("SELECT * FROM Courier WHERE courier_id=?");
                stmnt.setInt(1, rs.getInt("courier_id"));
                ResultSet c = stmnt.executeQuery();
                if (c.next())
                {
                    courier = new Courier(c.getInt("courier_id"),
                            c.getString("courier_name"));
                }
                c.close();

                stmnt = connection
                        .prepareStatement("SELECT * FROM Users WHERE user_id=?");
                stmnt.setInt(1, rs.getInt("user_id"));
                ResultSet u = stmnt.executeQuery();
                if (u.next())
                {
                    user = new User(u.getInt("user_id"),
                            u.getString("user_name"),
                            u.getString("first_name"),
                            u.getString("last_name"),
                            u.getBoolean("administrator"));
                }
                u.close();

                result.add(new Package(rs.getInt("package_id"), rs
                        .getString("tracking_number"), rs
                        .getString("receive_date"), rs
                        .getString("email_address"),
                        rs.getString("first_name"), rs.getString("last_name"),
                        rs.getString("box_number"), stop, courier, user, rs
                        .getBoolean("at_stop"), rs
                        .getBoolean("picked_up"), rs
                        .getString("pick_up_date"), rs
                        .getBoolean("returned")));
            }
        }
        catch (SQLException e)
        {
            Logger.logException(e);
        }

        return result;
    }

    // Search Actions//
    @Override
    public List<Person> findPerson(String firstName, String lastName,
                                   String boxOffice)
    {
        List<Person> results = new ArrayList<Person>();

        try
        {
            connect();
            PreparedStatement stmnt = connection
                    .prepareStatement("SELECT * FROM Person WHERE first_name LIKE ? AND last_name LIKE ? AND box_number=?");
            stmnt.setQueryTimeout(5);
            stmnt.setString(1, firstName);
            stmnt.setString(2, lastName);
            stmnt.setString(3, boxOffice);

            ResultSet rs = stmnt.executeQuery();

            // process ResultSet
            while (rs.next())
            {
                for (Stop s : stops)
                {
                    if (s.getStopId() == rs.getInt("stop_id"))
                    {
                        results.add(new Person(rs.getString("id_number"), rs
                                .getString("email_address"), rs
                                .getString("first_name"), rs
                                .getString("last_name"), rs
                                .getString("box_number"), s.getStopName()));
                        break;
                    }
                }
            }
        }
        catch (SQLException e)
        {
            Logger.logException(e);
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

            switch (sType)
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

            PreparedStatement stmnt = connection
                    .prepareStatement("SELECT * FROM Package WHERE tracking_number LIKE ? OR first_name LIKE ? OR last_name LIKE ?  OR box_number LIKE ? ORDER BY package_id DESC");

            stmnt.setString(1, search);
            stmnt.setString(2, search);
            stmnt.setString(3, search);
            stmnt.setString(4, search);

            ResultSet rs = stmnt.executeQuery();

            results = processPackageResult(rs);
        }
        catch (SQLException e)
        {
            Logger.logException(e);
        }
        finally
        {
            disconnect();
        }

        return results;
    }

    @Override
    public List<Package> searchPackages(String search, String startDate,
                                        String endDate, SearchType sType)
    {
        List<Package> results = null;
        try
        {
            connect();

            switch (sType)
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

            PreparedStatement stmnt = connection
                    .prepareStatement("SELECT * FROM Package WHERE receive_date BETWEEN ? AND ? AND tracking_number LIKE ?");

            stmnt.setString(1, startDate);
            stmnt.setString(2, endDate);
            stmnt.setString(3, search);

            ResultSet rs = stmnt.executeQuery();
            results = new ArrayList<Package>();

            for (Package p : processPackageResult(rs))
            {
                if (!results.contains(p))
                {
                    results.add(p);
                }
            }

            stmnt = connection
                    .prepareStatement("SELECT * FROM Package WHERE receive_date BETWEEN ? AND ? AND first_name LIKE ?");

            stmnt.setString(1, startDate);
            stmnt.setString(2, endDate);
            stmnt.setString(3, search);

            rs = stmnt.executeQuery();

            for (Package p : processPackageResult(rs))
            {
                if (!results.contains(p))
                {
                    results.add(p);
                }
            }

            stmnt = connection
                    .prepareStatement("SELECT * FROM Package WHERE receive_date BETWEEN ? AND ? AND last_name LIKE ?");

            stmnt.setString(1, startDate);
            stmnt.setString(2, endDate);
            stmnt.setString(3, search);

            rs = stmnt.executeQuery();

            for (Package p : processPackageResult(rs))
            {
                if (!results.contains(p))
                {
                    results.add(p);
                }
            }

            stmnt = connection
                    .prepareStatement("SELECT * FROM Package WHERE receive_date BETWEEN ? AND ? AND box_number LIKE ?");

            stmnt.setString(1, startDate);
            stmnt.setString(2, startDate);
            stmnt.setString(3, search);

            rs = stmnt.executeQuery();

            for (Package p : processPackageResult(rs))
            {
                if (!results.contains(p))
                {
                    results.add(p);
                }
            }
        }
        catch (SQLException e)
        {
            Logger.logException(e);
        }
        finally
        {
            disconnect();
        }

        return results;
    }

    // Normal Actions//
    @Override
    public void connect()
    {
        try
        {
            connection = DriverManager.getConnection(conString);
        }
        catch (SQLException e)
        {
            Logger.logException(e);
        }
    }

    @Override
    public void disconnect()
    {
        if (connection != null)
        {
            try
            {
                connection.close();
            }
            catch (SQLException e)
            {
                Logger.logException(e);
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
    }

    @Override
    public boolean create(boolean insertDev)
    {
        boolean retValue = false;
        try
        {
            connect();

            Statement stmnt = connection.createStatement();

            stmnt.execute(packageDrop);
            stmnt.execute(userDrop);
            stmnt.execute(personDrop);
            stmnt.execute(courierDrop);
            stmnt.execute(stopDrop);
            stmnt.execute(routeDrop);
            stmnt.execute(routeString);
            stmnt.execute(stopString);
            stmnt.execute(courierString);
            stmnt.execute(personString);
            stmnt.execute(userString);
            stmnt.execute(packageString);
            stmnt.execute(routeInsert);
            stmnt.execute(stopInsert);

            if (insertDev)
            {
                stmnt.execute(devString);
            }

            retValue = true;
        }
        catch (SQLException e)
        {
            Logger.logException(e);
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
            if (!connection.isClosed())
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
            Logger.logException(e);
            return false;
        }
    }

    public List<String> getEmailAddress(Stop s)
    {
        List<String> results = new ArrayList<String>();

        try
        {
            connect();

            PreparedStatement stmnt = connection.prepareStatement("SELECT address_string FROM EmailAddress WHERE stop_id=?");
            stmnt.setInt(1, s.getStopId());
            ResultSet rs = stmnt.executeQuery();

            while (rs.next())
            {
                String address = rs.getString("address_string");
                if (!results.contains(address))
                {
                    results.add(address);
                }
            }

            stmnt.close();
        }
        catch (SQLException e)
        {
            Logger.logException(e);
        }
        finally
        {
            disconnect();
        }

        return results;
    }

    public boolean addEmailAddress(Stop s, String address)
    {
        try
        {
            connect();

            PreparedStatement stmnt = connection.prepareStatement("INSERT INTO EmailAddress(address_string, is_used, stop_id) VALUES (?,?,?)");

            stmnt.setString(1, address);
            stmnt.setBoolean(2, true);
            stmnt.setInt(3, s.getStopId());

            return stmnt.execute();
        }
        catch (SQLException e)
        {
            Logger.logException(e);
            return false;
        }
        finally
        {
            disconnect();
        }
    }

    public boolean deleteEmailAddress(Stop s, String address)
    {
        try
        {
            connect();

            PreparedStatement stmnt = connection.prepareStatement("UPDATE EmailAddress SET is_used=0 WHERE address_string=? AND stop_id=?");

            stmnt.setString(1, address);
            stmnt.setInt(2, s.getStopId());

            return stmnt.executeUpdate() > 0;
        }
        catch (SQLException e)
        {
            Logger.logException(e);
            return false;
        }
        finally
        {
            disconnect();
        }
    }
}
