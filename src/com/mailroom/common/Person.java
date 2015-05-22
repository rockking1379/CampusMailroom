package com.mailroom.common;

/**
 * Represents a Person <br>
 * Used for AutoFill
 *
 * @author James rockking1379@gmail.com
 */
public class Person
{
    private String idNumber;
    private String emailAddress;
    private String firstName;
    private String lastName;
    private String boxOffice;
    private String stopName;

    /**
     * Constructs new Person
     *
     * @param idNumber Person ID Number(ssn, employee id, student id, etc)
     * @param emailAddress Person's Email Address(personal, company, school,
     * etc)
     * @param firstName First Name of Person
     * @param lastName Last Name of Person
     * @param boxOffice Box/Office Number of Person
     * @param stopName Name of Stop person is at
     */
    public Person(String idNumber, String emailAddress, String firstName,
                  String lastName, String boxOffice, String stopName)
    {
        this.idNumber = idNumber;
        this.emailAddress = emailAddress;
        this.firstName = firstName;
        this.lastName = lastName;
        this.boxOffice = boxOffice;
        this.stopName = stopName;
    }

    /**
     * Gets Person's ID Number
     *
     * @return Peron's ID Number
     */
    public String getIdNumber()
    {
        return idNumber;
    }

    /**
     * Gets Person's Email Address
     *
     * @return Person's Email Address
     */
    public String getEmailAddress()
    {
        return emailAddress;
    }

    /**
     * Gets Person's First Name
     *
     * @return Person's First Name
     */
    public String getFirstName()
    {
        return firstName;
    }

    /**
     * Gets Person's Last Name
     *
     * @return Person's Last Name
     */
    public String getLastName()
    {
        return lastName;
    }

    /**
     * Gets Box/Office Number of Person
     *
     * @return Box/Office Number
     */
    public String getBoxOffice()
    {
        return boxOffice;
    }

    /**
     * Gets Name of Stop Person is at
     *
     * @return Name of Stop
     */
    public String getStopName()
    {
        return stopName;
    }
}
