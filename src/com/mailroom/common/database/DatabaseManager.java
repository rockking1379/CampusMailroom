package com.mailroom.common.database;

import com.mailroom.common.objects.*;
import com.mailroom.common.objects.Package;

import java.sql.ResultSet;
import java.util.List;

/**
 * Database Interface. All methods need overwridden and logic implemented
 * All methods are called at some point in the program
 *
 * @author James rockking1379@gmail.com
 */
public interface DatabaseManager
{

    /**
     * Type of Search Query
     */
    enum SearchType
    {
        /**
         * Declares That Field(s) begin with Query
         */
        SEARCH_BEGINS_WITH,
        /**
         * Declares That Field(s) contain Query <br>
         * Default Option
         */
        SEARCH_CONTAINS,
        /**
         * Delcares That Field(s) end with Query
         */
        SEARCH_ENDS_WITH
    }

    /**
     * Used in Settings database to identify database manager type to create
     */
    int dbId = -1;
    /**
     * Used in Settings for displaying name
     */
    String dbName = "Base Class";

    // DbUser Actions//

    /**
     * Allows DbUser to Login into System Only called from MainClient
     *
     * @param userName username of user
     * @param password integer hash of username and password added together
     * @return returns a valid user on success or user with user_id=-1 on
     * failure
     */
    @Deprecated
    DbUser login(String userName, int password);

    /**
     * Allows a DbUser to Login into System
     * Only called from MainClient
     *
     * @param userName username of user
     * @param password MessageDigest used for password
     * @return returns a valud user on success or with user_id=-1 on failure
     */
    DbUser login(String userName, byte[] password);

    /**
     * Adds DbUser to System
     *
     * @param u DbUser to be added
     * @param password integer hash of username and password added together
     * @return status of SQL Execution
     */
    boolean addUser(DbUser u, byte[] password);

    /**
     * Changes Password of DbUser
     *
     * @param u DbUser requesting password change
     * @param oldPassword old password hash
     * @param newPassword new password hash
     * @return status of SQL Execution
     */
    boolean changePassword(DbUser u, byte[] oldPassword, byte[] newPassword);

    /**
     * Temporary method for converting between password schemes
     *
     * @param u DbUser needing changed
     * @param oldPassword old password hash
     * @param newPassword new MessageDigest
     * @return status of SQL Execution
     */
    @Deprecated
    boolean changePassword(DbUser u, int oldPassword, byte[] newPassword);

    /**
     * Deletes DbUser from System
     *
     * @param u DbUser to Delete
     * @return status of SQL Execution
     */
    boolean deleteUser(DbUser u);

    /**
     * Sets Administrator status of DbUser
     *
     * @param u DbUser to set Administrator status of
     * @param status true/false
     * @return status of SQL Execution
     */
    boolean setUserAdmin(DbUser u, boolean status);

    /**
     * Reactivates a Deleted DbUser
     *
     * @param u DbUser to Reactivate
     * @param password integer hash of DbUser's username and password added
     * together
     * @return status of SQL Exectuion
     */
    boolean reactivateUser(DbUser u, byte[] password);

    /**
     * Gets List of Deactivated Users
     *
     * @return List of Deactivate Users
     */
    List<DbUser> getDeactivatedUsers();

    /**
     * Gets List of Active Users
     *
     * @return List of Active Users
     */
    List<DbUser> getActiveUsers();

    // DbStop Actions//

    /**
     * Loads Stops from Database
     */
    void loadStops();

    /**
     * Updates DbStop
     *
     * @param s DbStop to be Updated
     * @return Status of SQL Execution
     */
    boolean updateStop(DbStop s);

    /**
     * Adds DbStop to DbRoute
     *
     * @param s DbStop be Added to DbRoute
     * @param r DbRoute DbStop is being Added To
     * @return status of SQL Execution
     */
    boolean addStopToRoute(DbStop s, DbRoute r);

    /**
     * Adds new DbStop
     *
     * @param s DbStop to be Added
     * @return status of SQL Execution
     */
    boolean addStop(DbStop s);

    /**
     * "Deletes" DbStop Really just flags it as not used by setting 'is_used' to
     * 0/false (depending on DB Type)
     *
     * @param s DbStop to be Deleted
     * @return Status of SQL Execution
     */
    boolean deleteStop(DbStop s);

    /**
     * Sets DbStop Position on DbRoute
     *
     * @param s DbStop to be positioned
     * @param pos position on route
     * @return SQL execution status
     */
    boolean setRoutePosition(DbStop s, int pos);

    /**
     * Gets Loaded Stops
     *
     * @return Loaded Stops
     */
    List<DbStop> getDbStops();

    /**
     * Gets Stops that are on the Unassigned DbRoute Unassigned DbRoute cannot be
     * deleted in software and is a default DbRoute inserted at Table Creation
     *
     * @return Stops on Unassigned DbRoute
     */
    List<DbStop> getUnassignedStops();

    /**
     * Gets Stops on DbRoute
     *
     * @param r DbRoute Stops should be On
     * @return List of Stops on specified DbRoute
     */
    List<DbStop> getStopsOnRoute(DbRoute r);

    /**
     * Processes DbStop Results Used to centralize repetitive code
     *
     * @param rs ResultSet to be processed
     * @param routeName Name of DbRoute these Stops should be using
     * @return List of Stops
     */
    List<DbStop> processStopResult(ResultSet rs, String routeName);

    // DbRoute Actions//

    /**
     * Loads Routes from Database
     */
    void loadRoutes();

    /**
     * Gets Loaded Routes
     *
     * @return List of Routes Loaded
     */
    List<DbRoute> getDbRoutes();

    /**
     * Updates A DbRoute
     *
     * @param r DbRoute to be updated
     * @return status of SQL Execution
     */
    boolean updateRoute(DbRoute r);

    /**
     * Adds New DbRoute to Database
     *
     * @param routeName Name of DbRoute
     * @return status of SQL Execution
     */
    boolean addRoute(String routeName);

    /**
     * Deletes DbRoute Flags DbRoute as not used
     *
     * @param r DbRoute to be Deleted
     * @return Status of SQL Execution
     */
    boolean deleteRoute(DbRoute r);

    // DbCourier Actions//

    /**
     * Loads Couriers from Database
     */
    void loadCouriers();

    /**
     * Gets List of Loaded Couriers
     *
     * @return Returns List of Loaded Couriers
     */
    List<DbCourier> getDbCouriers();

    /**
     * Adds new DbCourier
     *
     * @param courierName Name of DbCourier
     * @return Status of DbCourier Execution
     */
    boolean addCourier(String courierName);

    /**
     * Updates DbCourier
     *
     * @param c DbCourier to be Updated
     * @return Status of SQL Execution
     */
    boolean updateCourier(DbCourier c);

    /**
     * Deletes DbCourier
     *
     * @param c DbCourier to be Deleted
     * @return Status of SQL Execution
     */
    boolean deleteCourier(DbCourier c);

    // Package Actions//

    /**
     * Gets List of Loaded Packages
     *
     * @return List of Loaded Packages
     */
    List<com.mailroom.common.objects.Package> getPackages();

    /**
     * Loads All Packages Packages should not be delivered nor picked up
     */
    void loadAllPackages();

    /**
     * Loads Packages on DbStop where stop_id=stopId
     *
     * @param stopId stop_id of DbStop
     */
    void loadPackages(int stopId);

    /**
     * Checks if tracking number exists in database already
     *
     * @param trackingNumber tracking number to check
     * @return package id if tracking number exists in database
     */
    int checkTrackingNumber(String trackingNumber);

    /**
     * Updates Package with package_id=packageId
     *
     * @param packageId package_id of Package
     * @param atStop true/false either at stop or not
     * @param pickedUp true/false either picked up by recipient or not
     * @return Status of SQL Execution
     */
    boolean updatePackage(int packageId, boolean atStop, boolean pickedUp);

    /**
     * Updates Package with data in p
     *
     * @param p Package Data to Update with
     * @return Status of SQL Execution
     */
    boolean updatePackage(Package p);

    /**
     * Adds new Package to Database
     *
     * @param p Package to be Added
     * @return Status of SQL Execution
     */
    boolean addPackage(Package p);

    /**
     * Gets Packages on DbStop
     *
     * @param s DbStop packages should be on
     * @return List of Packages on DbStop
     */
    List<Package> getPackagesForStop(DbStop s);

    /**
     * Used for printing packages at a DbStop
     *
     * @param s DbStop to get packages
     * @return list of packages
     */
    List<Package> printPackagesForStop(DbStop s);

    /**
     * Processes Package Results
     *
     * @param rs Result Set to be Processed
     * @return List of Processed Packages
     */
    List<Package> processPackageResult(ResultSet rs);

    // Search Actions//

    /**
     * Searches for Person in Database
     *
     * @param firstName First Name of Person
     * @param lastName Last Name of Person
     * @param boxOffice Box/Office/Suite Number of Person
     * @return List of People Found
     */
    List<Person> findPerson(String firstName, String lastName,
                            String boxOffice);

    /**
     * Searchs Packages
     *
     * @param search Search Query
     * @param sType Where Query is in Field(s)
     * @return List of Packages Found
     */
    List<Package> searchPackages(String search, SearchType sType);

    /**
     * Searches Packages
     *
     * @param search Search Query
     * @param startDate Date to Start From
     * @param endDate Date to End At
     * @param sType Where Query is in field(s)
     * @return List of Packages Found
     */
    List<Package> searchPackages(String search, String startDate,
                                 String endDate, SearchType sType);

    // Normal Actions//

    /**
     * Opens Connection to Database
     */
    void connect();

    /**
     * Closes Connection to Database
     */
    void disconnect();

    /**
     * Disposes of Database Manager Should always be called before closing
     * program to release resources
     */
    void dispose();

    /**
     * Creates Database Tables Database should be created already but be empty
     * Will Update Statements to provide appropriate DROP commands
     *
     * @param insertDev Insert the Developer Access ability
     * @return Status of SQL Execution
     */
    boolean create(boolean insertDev);

    /**
     * Verifies status of database
     *
     * @return Verification Status
     */
    boolean verify();

    /**
     * Retrieves All Email Address for DbStop
     *
     * @param s DbStop used for getting Email Addresses
     * @return List of Email Addresses
     */
    List<String> getEmailAddress(DbStop s);

    /**
     * Adds new Email Address to DbStop Contact List
     *
     * @param s DbStop used for Contact List
     * @param address Address to be Added
     * @return Status of SQL Execution
     */
    boolean addEmailAddress(DbStop s, String address);

    /**
     * Removes Email Address from DbStop Contact List
     *
     * @param s DbStop used for Contact List
     * @param address Address to be Removed
     * @return Status of SQL Execution
     */
    boolean deleteEmailAddress(DbStop s, String address);
}