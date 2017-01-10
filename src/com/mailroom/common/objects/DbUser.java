package com.mailroom.common.objects;

import com.mailroom.common.interfaces.IDatabaseObject;
import com.mailroom.common.utils.Logger;
import org.json.simple.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Represents A DbUser <br>
 * Used for Checking In Packages, Settings Permissions, etc.
 *
 * @author James rockking1379@gmail.com
 */
public class DbUser implements IDatabaseObject
{
    private static final String LOGIN_SQL = "SELECT * FROM Users WHERE active=1 AND user_name=? AND password=?";
    private static final String ALL_SQL = "SELECT * FROM Users WHERE active=?";
    private static final String DELETE_SQL = "UPDATE Users SET active=0 WHERE user_id=?";
    private static final String CHANGE_PASSWORD_SQL = "UPDATE Users SET password=? WHERE user_name=? AND password=?";
    private static final String INSERT_SQL = "INSERT INTO Users(user_name, first_name, last_name, password, administrator, active) VALUES(?,?,?,?,?,1)";
    private static final String CHANGE_ADMIN_SQL = "UPDATE Users SET administrator=? WHERE user_id=?";

    private int userId;
    private String userName;
    private String firstName;
    private String lastName;
    private boolean administrator;

    /**
     * Constructs New DbUser
     *
     * @param userId ID of DbUser in Database
     * @param userName Username of DbUser
     * @param firstName First Name of DbUser
     * @param lastName Last Name of DbUser
     * @param administrator Administrative status of DbUser
     */
    public DbUser(int userId, String userName, String firstName, String lastName,
                  boolean administrator)
    {
        this.userId = userId;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.administrator = administrator;
    }

    // All get methods for variables \\

    /**
     * Gets ID of DbUser
     *
     * @return ID of DbUser
     */
    public int getUserId()
    {
        return userId;
    }

    /**
     * Gets Username of DbUser
     *
     * @return Username of DbUser
     */
    public String getUserName()
    {
        return userName;
    }

    /**
     * Gets First Name of DbUser
     *
     * @return First Name of DbUser
     */
    public String getFirstName()
    {
        return firstName;
    }

    /**
     * Gets Last Name of DbUser
     *
     * @return Last Name of DbUser
     */
    public String getLastName()
    {
        return lastName;
    }

    /**
     * Gets First and Last Name added together
     *
     * @return 'Last Name', 'First Name'
     */
    public String getName()
    {
        return lastName + ", " + firstName;
    }

    /**
     * Gets Admin Status of DbUser
     *
     * @return Wether DbUser is an Administrator or not
     */
    public boolean isAdministrator()
    {
        return administrator;
    }

    @Override
    public String toString()
    {
        return userName;
    }

    //Interface Methods\\

    @Override
    public IDatabaseObject fromJSON(JSONObject jsonObject)
    {
        userId = Integer.valueOf(jsonObject.get("user_id").toString());
        userName = jsonObject.get("user_name").toString();
        firstName = jsonObject.get("first_name").toString();
        lastName = jsonObject.get("last_name").toString();
        administrator = Boolean.valueOf(jsonObject.get("administrator").toString());

        return this;
    }

    @Override
    public PreparedStatement getCommand(Connection connection, Object... params)
    {
        try
        {
            PreparedStatement statement = connection.prepareStatement(LOGIN_SQL);

            statement.setString(1, userName);
            byte[] password = (byte[])params[0];
            statement.setBytes(2, password);

            return statement;
        }
        catch(SQLException sqle)
        {
            Logger.logException(sqle);
            return null;
        }
    }

    @Override
    public PreparedStatement getAllCommand(Connection connection, Object... params)
    {
        try
        {
            PreparedStatement statement = connection.prepareStatement(ALL_SQL);

            Boolean active = (Boolean)params[0];
            statement.setBoolean(1, active);

            return statement;
        }
        catch(SQLException sqle)
        {
            Logger.logException(sqle);
            return null;
        }
    }

    @Override
    public PreparedStatement deleteCommand(Connection connection, Object... params)
    {
        try
        {
            PreparedStatement statement = connection.prepareStatement(DELETE_SQL);

            statement.setInt(1, userId);

            return statement;
        }
        catch(SQLException sqle)
        {
            Logger.logException(sqle);
            return null;
        }
    }

    @Override
    public PreparedStatement searchCommand(Connection connection, Object... params)
    {
        return null;
    }

    @Override
    public PreparedStatement updateCommand(Connection connection, Object... params)
    {

        if(params.length == 2)
        {
            byte[] newPassword = (byte[]) params[0];
            byte[] oldPassword = (byte[]) params[1];

            try
            {
                PreparedStatement statement = connection.prepareStatement(CHANGE_PASSWORD_SQL);

                statement.setBytes(1, newPassword);
                statement.setString(2, userName);
                statement.setBytes(3, oldPassword);

                return statement;
            }
            catch (SQLException sqle)
            {
                Logger.logException(sqle);
                return null;
            }
        }
        if(params.length == 1)
        {
            try
            {
                PreparedStatement statement = connection.prepareStatement(CHANGE_ADMIN_SQL);

                Boolean admin = (Boolean)params[0];
                statement.setBoolean(1, admin);

                return statement;
            }
            catch (SQLException sqle)
            {
                Logger.logException(sqle);
                return null;
            }
        }

        return null;
    }

    @Override
    public PreparedStatement insertCommand(Connection connection, Object... params)
    {
        try
        {
            PreparedStatement statement = connection.prepareStatement(INSERT_SQL);

            statement.setString(1, userName);
            statement.setString(2, firstName);
            statement.setString(3, lastName);
            byte[] password = (byte[])params[0];
            statement.setBytes(4, password);
            statement.setBoolean(5, administrator);

            return statement;
        }
        catch(SQLException sqle)
        {
            Logger.logException(sqle);
            return null;
        }
    }

    @Override
    public JSONObject toJSON()
    {
        JSONObject retObj = new JSONObject();

        retObj.put("user_id", userId);
        retObj.put("user_name", userName);
        retObj.put("first_name", firstName);
        retObj.put("last_name", lastName);
        retObj.put("administrator", administrator);

        return retObj;
    }
}
