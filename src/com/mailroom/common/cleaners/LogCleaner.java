package com.mailroom.common.cleaners;

import com.mailroom.common.database.LogManager;
import com.mailroom.common.utils.Logger;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Cleans Log files
 * Sends away to dev IF there are entries in Error table
 * <p/>
 * Created by james on 5/28/15.
 */
public class LogCleaner implements Runnable
{
    /**
     * Maximum Age of a Log file <br>
     * Measured in Days
     */
    static final int MAX_AGE = 3;
    static final String ERROR_SERVER_ADDRESS = "http://error.codegeekhosting.me";
    static final int ERROR_SERVER_PORT = 65000;

    public LogCleaner()
    {
        try
        {
            Class.forName("org.sqlite.JDBC");
        }
        catch (ClassNotFoundException e)
        {
            Logger.logException(e);
        }

        new Thread(this).start();
    }

    @Override
    public void run()
    {
        File logDir = new File("./Logs");
        Date today = new Date();
        today = new Date(today.getTime() - TimeUnit.DAYS.toMillis(1));
        Date toOld = new Date(today.getTime() - TimeUnit.DAYS.toMillis(MAX_AGE));

        try
        {
            Logger.logEvent("Reading Log Directory", "LOGCLEANER");
            ArrayList<File> toDelete = new ArrayList<File>();

            File[] files = logDir.listFiles();

            if (files != null)
            {
                for (File f : files)
                {
                    if (!today.before(new Date(f.lastModified())))
                    {
                        if (toOld.after(new Date(f.lastModified())))
                        {
                            toDelete.add(f);
                        }
                        else
                        {
                            try
                            {
                                Logger.logEvent("Found Log Needs Sending", "LOGCLEANER");
                                //create a manager for this log
                                LogManager logManager = new LogManager(f.getCanonicalPath());
                                //check if errors exist in log (events will always exist)
                                if(logManager.countErrors() > 0)
                                {
                                    new LogSender(logManager);
                                }
                            }
                            catch (IOException ioe)
                            {
                                Logger.logException(ioe);
                            }
                        }
                    }
                }
            }
            for (File f : toDelete)
            {
                if (f.delete())
                {
                    Logger.logEvent("Log " + f.getName() + " Deleted", "LOGCLEANER");
                }
            }
        }
        catch (NullPointerException npe)
        {
            Logger.logException(npe);
        }
    }

    private class LogSender implements Runnable
    {
        LogManager logManager;
        public LogSender(LogManager logManager)
        {
            this.logManager = logManager;
            new Thread(this).start();
        }

        @Override
        public void run()
        {
            try
            {
                JSONObject log = logManager.toJSON();URL url = new URL(ERROR_SERVER_ADDRESS + ":" + ERROR_SERVER_PORT);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Content-Length", String.valueOf(log.toString().length()));
                OutputStream os = conn.getOutputStream();
                os.write(log.toString().getBytes());
                os.flush();
                os.close();

                if(conn.getResponseCode() != 200)
                {
                    Logger.logException(new Exception("Error Submitting Log to Server\nReceived Status Code: " + String.valueOf(conn.getResponseCode())));
                }
                else
                {
                    Logger.logEvent("Log Submitted! Deleting Log!", "LOGCLEANER");
                    new File(logManager.getLogLocation()).delete();
                }
            }
            catch(IOException ioe)
            {
                Logger.logException(ioe);
            }
        }
    }
}