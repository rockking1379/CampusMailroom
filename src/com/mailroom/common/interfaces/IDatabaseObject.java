package com.mailroom.common.interfaces;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * Interface for any object that can be stored in the Database.
 * any object that can be stored needs to implement this interface
 * in order to be correctly used by a IDatabaseManager.
 * Created by James on 1/25/2016.
 */
public interface IDatabaseObject extends IJSONable
{
    /**
     * This is to get the statement for retrieving objects
     * from the database
     * @param params any params that may be necessary for the command
     * @return PreparedStatement that can be executed by a SQL connection
     */
    PreparedStatement getCommand(Connection connection, Object... params);

    /**
     * This is to get the statement for retrieving all objects
     * from the database for a specific type. Most likely use case for this
     * is retrieving packages that are delivered, users that are not active, etc
     * @param params any params that may be necessary for the command
     * @return PreparedStatement that can be executed by a SQL connection
     */
    PreparedStatement getAllCommand(Connection connection, Object... params);

    /**
     * This is to get the statement for deleting objects
     * from the database
     * @param params any params that may be necessary for the command
     * @return PreparedStatement that can be executed by a SQL connection
     */
    PreparedStatement deleteCommand(Connection connection, Object... params);

    /**
     * This is to get the statement for searching objects
     * in the database
     * @param params any params that may be necessary for the command
     * @return PreparedStatement that can be executed by a SQL connection
     */
    PreparedStatement searchCommand(Connection connection, Object... params);

    /**
     * This is to get the statement for updating objects
     * in the database
     * @param params any params that may be necessary for the command
     * @return PreparedStatement that can be executed by a SQL connection
     */
    PreparedStatement updateCommand(Connection connection, Object... params);

    /**
     * This is to get the statement for inserting new objects
     * into the database
     * @param params any params that may be necessary for the command.
     * this will likely be null for insertion except in the case of DbUser.
     * then the param will be the password for the specific DbUser since that
     * is not stored in the memory of the DbUser instance.
     * @return PreparedStatement that can be executed by a SQL connection
     */
    PreparedStatement insertCommand(Connection connection, Object... params);
}
