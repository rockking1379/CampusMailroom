package com.mailroom.otherclient;

import com.mailroom.common.*;
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
import java.io.IOException;
import java.util.Properties;

/**
 * Defines Main Entry Point for OtherClient
 *
 * @author James sitzja@grizzlies.adams.edu
 */
public class OtherMainFrame extends Application
{
    public static DatabaseManager dbManager;
    /**
     * Package Edit Window for Program Instance
     */
    public static PackageEditWindow editWindow;
    public static Stage stage;
    public static String[] pubArgs;
    public static Properties properties;

    /**
     * Main Entry Point for Other Client
     *
     * @param args Command Line Arguments
     */
    public static void main(String[] args)
    {
        pubArgs = args;
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception
    {
        try
        {
            properties = new Properties();
            File propFile = new File("./configuration.properties");

            if (propFile.exists())
            {
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
                MessageDialogBuilder
                        .error()
                        .message(
                                "No Config Found!\nPlease Run Other Program First")
                        .buttonType(MessageDialog.ButtonType.OK).show(null);
                System.exit(-1);
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
        }

        editWindow = new PackageEditWindow();

        OtherMainFrame.stage = stage;
        OtherMainFrame.stage.getIcons().add(
                new Image(getClass().getResourceAsStream(
                        "/com/mailroom/resources/Icon.png")));
        OtherMainFrame.stage.setResizable(false);
        OtherMainFrame.stage.centerOnScreen();
        Parent root = FXMLLoader.load(getClass().getResource(
                "/com/mailroom/fxml/otherclient/MainPageFx.fxml"));
        Scene scene = new Scene(root, 800, 600);
        OtherMainFrame.stage.setScene(scene);
        OtherMainFrame.stage.setTitle("Main Page");

        editWindow.show(null, null);
        editWindow.hide();

        OtherMainFrame.stage.show();
    }
}
