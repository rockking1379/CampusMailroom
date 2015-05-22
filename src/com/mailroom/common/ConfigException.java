package com.mailroom.common;

/**
 * Represents a Configuration Error In Configuration File <br>
 * Configuration Error Means Multiple Database Types are set to True
 *
 * @author James rockking1379@gmail.com
 */
public class ConfigException extends Exception
{
    private static final long serialVersionUID = -787120505265326744L;

    /**
     * Constructs New ConfigException
     *
     * @param message Exception Message
     */
    public ConfigException(String message)
    {
        super(message);
    }
}
