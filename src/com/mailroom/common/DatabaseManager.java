package com.mailroom.common;

import java.sql.ResultSet;
import java.util.List;

/**
 * Database Interface. All methods need overwridden and logic implemented
 * All methods are called at some point in the program
 *
 * @author James sitzja@grizzlies.adams.edu
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

    // User Actions//

    /**
     * Allows User to Login into System Only called from MainClient
     *
     * @param userName username of user
     * @param password integer hash of username and password added together
     * @return returns a valid user on success or user with user_id=-1 on
     * failure
     */
    User login(String userName, int password);

    /**
     * Adds User to System
     *
     * @param u User to be added
     * @param password integer hash of username and password added together
     * @return status of SQL Execution
     */
    boolean addUser(User u, int password);

    /**
     * Changes Password of User
     *
     * @param u User requesting password change
     * @param oldPassword old password hash
     * @param newPassword new password hash
     * @return status of SQL Execution
     */
    boolean changePassword(User u, int oldPassword, int newPassword);

    /**
     * Deletes User from System
     *
     * @param u User to Delete
     * @return status of SQL Execution
     */
    boolean deleteUser(User u);

    /**
     * Sets Administrator status of User
     *
     * @param u User to set Administrator status of
     * @param status true/false
     * @return status of SQL Execution
     */
    boolean setUserAdmin(User u, boolean status);

    /**
     * Reactivates a Deleted User
     *
     * @param u User to Reactivate
     * @param password integer hash of User's username and password added
     * together
     * @return status of SQL Exectuion
     */
    boolean reactivateUser(User u, int password);

    /**
     * Gets List of Deactivated Users
     *
     * @return List of Deactivate Users
     */
    List<User> getDeactivatedUsers();

    /**
     * Gets List of Active Users
     *
     * @return List of Active Users
     */
    List<User> getActiveUsers();

    // Stop Actions//

    /**
     * Loads Stops from Database
     */
    void loadStops();

    /**
     * Updates Stop
     *
     * @param s Stop to be Updated
     * @return Status of SQL Execution
     */
    boolean updateStop(Stop s);

    /**
     * Adds Stop to Route
     *
     * @param s Stop be Added to Route
     * @param r Route Stop is being Added To
     * @return status of SQL Execution
     */
    boolean addStopToRoute(Stop s, Route r);

    /**
     * Adds new Stop
     *
     * @param s Stop to be Added
     * @return status of SQL Execution
     */
    boolean addStop(Stop s);

    /**
     * "Deletes" Stop Really just flags it as not used by setting 'is_used' to
     * 0/false (depending on DB Type)
     *
     * @param s Stop to be Deleted
     * @return Status of SQL Execution
     */
    boolean deleteStop(Stop s);

    /**
     * Sets Stop Position on Route
     *
     * @param s Stop to be positioned
     * @param pos position on route
     * @return SQL execution status
     */
    boolean setRoutePosition(Stop s, int pos);

    /**
     * Gets Loaded Stops
     *
     * @return Loaded Stops
     */
    List<Stop> getStops();

    /**
     * Gets Stops that are on the Unassigned Route Unassigned Route cannot be
     * deleted in software and is a default Route inserted at Table Creation
     *
     * @return Stops on Unassigned Route
     */
    List<Stop> getUnassignedStops();

    /**
     * Gets Stops on Route
     *
     * @param r Route Stops should be On
     * @return List of Stops on specified Route
     */
    List<Stop> getStopsOnRoute(Route r);

    /**
     * Processes Stop Results Used to centralize repetitive code
     *
     * @param rs ResultSet to be processed
     * @param routeName Name of Route these Stops should be using
     * @return List of Stops
     */
    List<Stop> processStopResult(ResultSet rs, String routeName);

    // Route Actions//

    /**
     * Loads Routes from Database
     */
    void loadRoutes();

    /**
     * Gets Loaded Routes
     *
     * @return List of Routes Loaded
     */
    List<Route> getRoutes();

    /**
     * Updates A Route
     *
     * @param r Route to be updated
     * @return status of SQL Execution
     */
    boolean updateRoute(Route r);

    /**
     * Adds New Route to Database
     *
     * @param routeName Name of Route
     * @return status of SQL Execution
     */
    boolean addRoute(String routeName);

    /**
     * Deletes Route Flags Route as not used
     *
     * @param r Route to be Deleted
     * @return Status of SQL Execution
     */
    boolean deleteRoute(Route r);

    // Courier Actions//

    /**
     * Loads Couriers from Database
     */
    void loadCouriers();

    /**
     * Gets List of Loaded Couriers
     *
     * @return Returns List of Loaded Couriers
     */
    List<Courier> getCouriers();

    /**
     * Adds new Courier
     *
     * @param courierName Name of Courier
     * @return Status of Courier Execution
     */
    boolean addCourier(String courierName);

    /**
     * Updates Courier
     *
     * @param c Courier to be Updated
     * @return Status of SQL Execution
     */
    boolean updateCourier(Courier c);

    /**
     * Deletes Courier
     *
     * @param c Courier to be Deleted
     * @return Status of SQL Execution
     */
    boolean deleteCourier(Courier c);

    // Package Actions//

    /**
     * Gets List of Loaded Packages
     *
     * @return List of Loaded Packages
     */
    List<Package> getPackages();

    /**
     * Loads All Packages Packages should not be delivered nor picked up
     */
    void loadAllPackages();

    /**
     * Loads Packages on Stop where stop_id=stopId
     *
     * @param stopId stop_id of Stop
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
     * Gets Packages on Stop
     *
     * @param s Stop packages should be on
     * @return List of Packages on Stop
     */
    List<Package> getPackagesForStop(Stop s);

    /**
     * Used for printing packages at a Stop
     *
     * @param s Stop to get packages
     * @return list of packages
     */
    List<Package> printPackagesForStop(Stop s);

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
     * Retrieves All Email Address for Stop
     *
     * @param s Stop used for getting Email Addresses
     * @return List of Email Addresses
     */
    List<String> getEmailAddress(Stop s);

    /**
     * Adds new Email Address to Stop Contact List
     *
     * @param s Stop used for Contact List
     * @param address Address to be Added
     * @return Status of SQL Execution
     */
    boolean addEmailAddress(Stop s, String address);

    /**
     * Removes Email Address from Stop Contact List
     *
     * @param s Stop used for Contact List
     * @param address Address to be Removed
     * @return Status of SQL Execution
     */
    boolean deleteEmailAddress(Stop s, String address);
}