package com.mailroom.common.interfaces;

import org.json.simple.JSONObject;

/**
 * Classes that implement this interface can be easily converted
 * to and from JSON objects. Primarily useful for dealing with the AppServer or
 * HostedAppServer configurations since they will operate of HTTP(S)
 * Created by James on 1/25/2016.
 */
public interface IJSONable
{
    /**
     * Converts a Class to a JSON object
     * @return JSON Object representing current instance
     */
    JSONObject toJSON();

    /**
     * Converts a Class from a JSON Object
     * @param jsonObject JSON Object to convert from
     * @return instance of an IDatabaseObject. SysLog returns null since it doesn't
     * implement IDatabaseObject.
     */
    IDatabaseObject fromJSON(JSONObject jsonObject);
}
