package com.mailroom.common.cleaners;

import com.mailroom.common.utils.Logger;

/**
 * Created by James on 1/18/2017.
 */
public class PackageCleaner implements Runnable
{
    public PackageCleaner()
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

    }
}
