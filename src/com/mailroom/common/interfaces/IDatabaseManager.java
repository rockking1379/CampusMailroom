package com.mailroom.common.interfaces;

import java.util.List;

/**
 * This is an interface for dealing with any type of Database. The system will work with any manager that implements
 * this specific interface. This interface primarily deals with another interface which is for the objects stored in
 * the database itself.
 * Created by James on 1/25/2016.
 */
public interface IDatabaseManager
{
    /**
     * Different types of queries that are allowed with searching
     */
    enum EQueryType {BEGINS_WITH, CONTAINS, ENDS_WITH}

    /**
     * This is used in the configuration file (may require migration from V2 file).
     */
    enum EDatabaseManagerType {SQLITE, POSTGRESQL, MYSQL, APPSERVER, HOSTEDDAPP}

    /**
     * Adds new IDatabaseObject to the database
     * @param object IDatabaseObject to be added to the database
     * @param params any params that may be needed for the adding. generally this will be null.
     * in the case of DbUser, this will contain the password
     * @return status of insertion
     */
    boolean addDatabaseObject(IDatabaseObject object, Object... params);

    /**
     * Deletes an IDatabaseObject from the database
     * @param object IDatabaseObject to be deleted from the database
     * @param params any params that may be needed for the deletion. generally this will be null.
     * @return status of deletion
     */
    boolean deleteDatabaseObject(IDatabaseObject object, Object... params);

    /**
     * Updates an IDatabaseObject already in the database
     * @param object IDatabaseObject to be updated in the database
     * @param params any params that may be needed for the update. generally this will be null
     * @return status of update
     */
    boolean updateDatabaseObject(IDatabaseObject object, Object... params);

    /**
     * Searches the Database for an IDatabaseObject instance
     * @param object IDatabaseObject that has the necessary methods for creating the proper statements
     * @param queryType Type of query. This is used for dealing with strings. typically this is EQueryType.CONTAINS
     * @param params any params that are needed for the search.
     * @return List of IDatabaseObjects that were found in the database matching a criteria
     */
    List<IDatabaseObject> searchDatabaseObject(IDatabaseObject object, EQueryType queryType, Object... params);

    /**
     * Gets instances of IDatabaseObject. this will be most widely used variation as opposed to getAllDatabaseObjects.
     * This one is intended to be the general use variation
     * @param object IDatabaseObject that has necessary methods for getting the PreparedStatement
     * @param params any params that may be needed for the retrieval. generally will be null.
     * in the case of DbUser, this will have the password
     * @return List of IDatabaseObjects that were found in the database. Generally will be length of 1
     */
    List<IDatabaseObject> getDatabaseObject(IDatabaseObject object, Object... params);

    /**
     * Gets more instances of IDatabaseObject. searchDatabaseObject might be a more suitable method, so method may be
     * removed later.
     * @param object IDatabaseObject that has necessary methods for getting the PreparedStatement
     * @param params any params that may be needed for the retrieval. generally will contain boolean values that
     * coordinate with different flags in the database
     * @return List of IDatabaseObjects that were found in the database.
     */
    List<IDatabaseObject> getAllDatabaseObjects(IDatabaseObject object, Object... params);

    /**
     * Verifies database. checks tables are there and that some default
     * data is present. also checks that the parameters given are valid.
     * @return status of verification
     */
    boolean verifyDatabase();

    /**
     * Creates the schema for the Database
     * @param devInsert whether or not to insert the default DEV user
     * @return status of creation
     */
    boolean createDatabase(boolean devInsert);

    /**
     * Disposes of IDatabaseManager. not generally useful though
     */
    void dispose();

    /**
     * Disconnects IDatabaseManager from the database
     */
    void disconnectFromDatabase();

    /**
     * Connects to IDatabaseManager to database
     */
    void connectToDatabase();
}
