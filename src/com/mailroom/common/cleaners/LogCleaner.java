package com.mailroom.common.cleaners;

import com.mailroom.common.utils.Logger;
import com.mailroom.mainclient.MainFrame;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
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
    static final int maxAge = 3;
    static final String devEmail = "rockking1379@gmail.com";

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
        Date toOld = new Date(today.getTime() - TimeUnit.DAYS.toMillis(maxAge));

        try
        {
            Properties emailProps = new Properties();
            emailProps.put("mail.smtp.starttls.enable", "true");
            emailProps.put("mail.smtp.host", MainFrame.properties.get("EMAILHOST"));
            emailProps.put("mail.smtp.port", MainFrame.properties.get("EMAILPORT"));
            Session session;

            if (MainFrame.properties.getProperty("EMAILAUTHREQ").equalsIgnoreCase("false"))
            {
                emailProps.put("mail.smtp.auth", "false");
                session = Session.getDefaultInstance(emailProps);
            }
            else
            {
                emailProps.put("mail.smtp.auth", "true");
                session = Session.getInstance(emailProps, new Authenticator()
                {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication()
                    {
                        return new PasswordAuthentication(MainFrame.properties.getProperty("EMAILUSERNAME"), MainFrame.properties.getProperty("EMAILPASSWORD"));
                    }
                });
            }

            MimeMessage message = new MimeMessage(session);
            Address[] a = {new InternetAddress(MainFrame.properties.getProperty("EMAILREPLYTO"))};
            message.setReplyTo(a);
            message.setSubject("Mailroom Errors Occurred");
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(devEmail));

            MimeMultipart multipart = new MimeMultipart();
            MimeBodyPart bodyPart = new MimeBodyPart();
            bodyPart.setText("Attached is a log file containing errors");

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
                            if (MainFrame.properties != null)
                            {
                                if (Boolean.valueOf(MainFrame.properties.getProperty("EMAILENABLE")))
                                {
                                    try
                                    {
                                        Connection connection = DriverManager.getConnection("jdbc:sqlite:"
                                                + f.getCanonicalPath());

                                        Statement stmnt = connection.createStatement();
                                        ResultSet rs = stmnt.executeQuery("SELECT count(error_id) AS c FROM Error");

                                        rs.next();
                                        if (rs.getInt("c") > 0)
                                        {
                                            DataSource source = new FileDataSource(f.getCanonicalPath());
                                            bodyPart.setDataHandler(new DataHandler(source));
                                            bodyPart.setFileName(f.getName());
                                            multipart.addBodyPart(bodyPart);

                                            message.setContent(multipart);
                                        }

                                        toDelete.add(f);
                                    }
                                    catch (SQLException e)
                                    {
                                        Logger.logException(e);
                                    }
                                    catch (IOException e)
                                    {
                                        Logger.logException(e);
                                    }
                                }
                            }
                        }
                    }
                }

                Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
                Transport.send(message);
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
        catch (AddressException e)
        {
            Logger.logException(e);
        }
        catch (MessagingException e)
        {
            Logger.logException(e);
        }
    }
}