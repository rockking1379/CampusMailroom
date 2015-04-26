package com.mailroom.common;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Used to clean print outs
 * Created by James on 4/25/2015.
 */
public class PrintCleaner implements Runnable
{
    private String directoryLocation;
    private int maxAge;

    /**
     * Creates new cleaner
     * @param directoryLocation Location of directory to search in
     * @param maxAge maximum age of a file measured in days
     */
    public PrintCleaner(String directoryLocation, int maxAge)
    {
        this.directoryLocation = directoryLocation;
        this.maxAge = maxAge;
        new Thread(this).start();
    }

    @Override
    public void run()
    {
        Lock l = new ReentrantLock();

        try
        {
            l.lock();
            File d = new File(directoryLocation);
            File[] files = d.listFiles();
            Date now = new Date();
            Date old = new Date(now.getTime() - (maxAge * 24 * 60 * 60 * 1000));
            ArrayList<File> toDelete = new ArrayList<File>();

            if(files != null)
            {
                for (File f : files)
                {
                    if (f.lastModified() < old.getTime())
                    {
                        toDelete.add(f);
                    }
                }
            }

            for (File f : toDelete)
            {
                f.delete();
            }
        }
        catch(NullPointerException npe)
        {
            Logger.logException(npe);
        }
        finally
        {
            l.unlock();
        }
    }
}
