package com.mailroom.common.utils;

/**
 * Used to replace keeping the version in configuration file
 * Created by James on 8/21/2015.
 */
public class Version
{
    public static final String VERSION_MAJOR = "2";
    public static final String VERSION_MINOR = "8";
    public static final String VERSION_REVISION = "0";

    public String getMajorVersion()
    {
        return VERSION_MAJOR;
    }

    public String getMinorVersion()
    {
        return VERSION_MINOR;
    }

    public String getVersionRevision()
    {
        return VERSION_REVISION;
    }

    public String getVersion()
    {
        return getMajorVersion() + "." + getMinorVersion() + "." + getVersionRevision();
    }
}
