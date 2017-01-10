package com.mailroom.common.factories;

import com.mailroom.common.database.DatabaseManager;
import com.mailroom.common.database.MySQLManager;
import com.mailroom.common.database.PostgreSQLManager;
import com.mailroom.common.database.SQLiteManager;
import com.mailroom.common.exceptions.ConfigException;
import com.mailroom.common.interfaces.IDatabaseManager;
import com.mailroom.common.utils.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Used to read properties for DatabaseManager configuration
 * Creates appropriate DatabaseManager
 * Created by James on 12/19/2015.
 */
public class DatabaseManagerFactory
{
    private static DatabaseManager currentInstance = null;

    public static DatabaseManager getInstance()
    {
        if(currentInstance == null)
        {
            //read properties
            //make new instance
            //assign instance
            try
            {
                Properties properties = new Properties();
                File propFile = new File("./configuration.properties");

                if(propFile.exists())
                {
                    FileInputStream fileInputStream = new FileInputStream(propFile);
                    properties.load(fileInputStream);

                    switch (IDatabaseManager.EDatabaseManagerType.valueOf(properties.getProperty("DBTYPE")))
                    {
                        case SQLITE:
                        {
                            currentInstance = new SQLiteManager(
                                    properties.getProperty("DATABASE"));
                            break;
                        }
                        case MYSQL:
                        {
                            currentInstance = new MySQLManager(
                                    properties.getProperty("DATABASE"),
                                    properties.getProperty("USERNAME"),
                                    properties.getProperty("PASSWORD"),
                                    properties.getProperty("DBNAME"));
                            break;
                        }
                        case POSTGRESQL:
                        {
                            currentInstance = new PostgreSQLManager(
                                    properties.getProperty("DATABASE"),
                                    properties.getProperty("USERNAME"),
                                    properties.getProperty("PASSWORD"),
                                    properties.getProperty("DBNAME"));
                            break;
                        }
                        default:
                        {
                            throw new ConfigException(
                                    "Configuration Error\nUnknown DatabaseManager Type");
                        }
                    }
                }
                else
                {
                    currentInstance = null;
                }
            }
            catch(IOException ioe)
            {
                Logger.logException(ioe);
                currentInstance = null;
            }
            catch (ConfigException ce)
            {
                Logger.logException(ce);
                currentInstance = null;
            }
        }
        return currentInstance;
    }
}
