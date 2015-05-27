package com.mailroom.mainclient;

import com.mailroom.common.cleaners.PrintCleaner;
import com.mailroom.common.database.DatabaseManager;
import com.mailroom.common.database.MysqlManager;
import com.mailroom.common.database.PostgreSQLManager;
import com.mailroom.common.database.SQLiteManager;
import com.mailroom.common.exceptions.ConfigException;
import com.mailroom.common.gui.PackageEditWindow;
import com.mailroom.common.objects.User;
import com.mailroom.common.utils.Logger;
import com.panemu.tiwulfx.dialog.MessageDialog;
import com.panemu.tiwulfx.dialog.MessageDialogBuilder;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Main Entry Point for MainClient
 *
 * @author James rockking1379@gmail.com
 */
public class MainFrame extends Application
{
    /**
     * Database Manager for Instance of Main Client
     */
    public static DatabaseManager dbManager;
    /**
     * Package Edit Window for Program Instance
     */
    public static PackageEditWindow editWindow;
    /**
     * Sate for Instance of Main Client
     */
    public static Stage stage;
    /**
     * Currently Logged In User
     */
    public static User cUser;
    /**
     * Software Configuration Properties
     */
    public static Properties properties = null;
    /**
     * Logo Image to be displayed in Login
     */
    public static Image imageLogo;
    /**
     * Command Line arguments <br>
     * Used to Restart in Settings
     */
    public static String[] pubArgs;

    /**
     * Main Entry Point for Main Client
     *
     * @param args Command Line Arguments
     */
    public static void main(String[] args)
    {
        new PrintCleaner("./Prints", 30);

        pubArgs = args;
        launch(args);
    }

    @SuppressWarnings("static-access")
    @Override
    public void start(Stage stage) throws Exception
    {
        Logger.logEvent("Starting System", "SYSTEM");
        boolean setup = true;
        // read settings file
        try
        {
            properties = new Properties();
            File propFile = new File("./configuration.properties");

            if (propFile.exists())
            {
                setup = false;

                FileInputStream file = new FileInputStream(propFile);
                properties.load(file);

                switch (Integer.valueOf(properties.getProperty("DBTYPE")))
                {
                    case SQLiteManager.dbId:
                    {
                        dbManager = new SQLiteManager(
                                properties.getProperty("DATABASE"));
                        break;
                    }
                    case MysqlManager.dbId:
                    {
                        dbManager = new MysqlManager(
                                properties.getProperty("DATABASE"),
                                properties.getProperty("USERNAME"),
                                properties.getProperty("PASSWORD"),
                                properties.getProperty("DBNAME"));
                        break;
                    }
                    case PostgreSQLManager.dbId:
                    {
                        dbManager = new PostgreSQLManager(
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

                file.close();
            }
            else
            {
                setup = true;
            }
        }
        catch (IOException e)
        {
            Logger.logException(e);
            System.exit(-1);
        }
        catch (ConfigException e)
        {
            Logger.logException(e);
            MessageDialogBuilder
                    .warning()
                    .message(
                            "Bad Configuration Found!\nYou Will Be Directed to the Setup Page")
                    .buttonType(MessageDialog.ButtonType.OK)
                    .yesOkButtonText("OK").show(null);
            this.imageLogo = new Image(getClass().getResourceAsStream(
                    "/com/mailroom/resources/Logo.png"));
            this.stage = stage;
            this.stage.getIcons().add(
                    new Image(getClass().getResourceAsStream(
                            "/com/mailroom/resources/Icon.png")));
            this.stage.setResizable(false);
            this.stage.centerOnScreen();
            Parent root = FXMLLoader.load(getClass().getResource(
                    "/com/mailroom/fxml/mainclient/SetupFx.fxml"));
            Scene scene = new Scene(root, 800, 600);
            this.stage.setScene(scene);
            this.stage.setTitle("Setup");
            this.stage.show();
        }

        if (setup)
        {
            MessageDialogBuilder
                    .warning()
                    .message(
                            "No Configuration Found!\nYou Will Be Directed to the Setup Page")
                    .buttonType(MessageDialog.ButtonType.OK)
                    .yesOkButtonText("OK").show(null);
            this.imageLogo = new Image(getClass().getResourceAsStream(
                    "/com/mailroom/resources/Logo.png"));
            this.stage = stage;
            this.stage.getIcons().add(
                    new Image(getClass().getResourceAsStream(
                            "/com/mailroom/resources/Icon.png")));
            this.stage.setResizable(false);
            this.stage.centerOnScreen();
            Parent root = FXMLLoader.load(getClass().getResource(
                    "/com/mailroom/fxml/mainclient/SetupFx.fxml"));
            Scene scene = new Scene(root, 800, 600);
            this.stage.setScene(scene);
            this.stage.setTitle("Setup");
            this.stage.show();
        }
        else
        {
            this.imageLogo = new Image(getClass().getResourceAsStream(
                    "/com/mailroom/resources/Logo.png"));
            this.stage = stage;
            this.stage.getIcons().add(
                    new Image(getClass().getResourceAsStream(
                            "/com/mailroom/resources/Icon.png")));
            this.stage.setResizable(false);
            this.stage.centerOnScreen();
            Parent root = FXMLLoader.load(getClass().getResource(
                    "/com/mailroom/fxml/mainclient/LoginFx.fxml"));
            Scene scene = new Scene(root, 800, 600);
            this.stage.setScene(scene);
            this.stage.setTitle("Login");
            this.stage.show();

            editWindow = new PackageEditWindow();
            editWindow.show(null, null);
            editWindow.hide();
        }
    }

    /**
     * Saves Properties out to file <br>
     * Called from various locations throughout Main Client
     */
    public static void saveProperties()
    {
        if (properties != null)
        {
            Logger.logEvent("Saving Properties", "SYSTEM");
            try
            {
                File propFile = new File("./configuration.properties");
                if (!propFile.exists())
                {
                    propFile.createNewFile();
                }
                FileOutputStream oStream = new FileOutputStream(propFile);
                properties.store(oStream, "System Configuration");
                oStream.close();
            }
            catch (IOException e)
            {
                Logger.logException(e);
            }
        }
    }
}
