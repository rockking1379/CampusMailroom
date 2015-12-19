package com.mailroom.common.database;

import com.mailroom.common.exceptions.ConfigException;
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

                    switch (Integer.valueOf(properties.getProperty("DBTYPE")))
                    {
                        case SQLiteManager.dbId:
                        {
                            currentInstance = new SQLiteManager(
                                    properties.getProperty("DATABASE"));
                            break;
                        }
                        case MysqlManager.dbId:
                        {
                            currentInstance = new MysqlManager(
                                    properties.getProperty("DATABASE"),
                                    properties.getProperty("USERNAME"),
                                    properties.getProperty("PASSWORD"),
                                    properties.getProperty("DBNAME"));
                            break;
                        }
                        case PostgreSQLManager.dbId:
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
                                    "Configuration Error\nUnknown Database Type");
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
