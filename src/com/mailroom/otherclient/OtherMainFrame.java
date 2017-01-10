package com.mailroom.otherclient;

import com.mailroom.common.factories.DatabaseManagerFactory;
import com.mailroom.common.gui.PackageEditWindow;
import com.panemu.tiwulfx.dialog.MessageDialog;
import com.panemu.tiwulfx.dialog.MessageDialogBuilder;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Properties;

/**
 * Defines Main Entry Point for OtherClient
 *
 * @author James rockking1379@gmail.com
 */
public class OtherMainFrame extends Application
{
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
        if(DatabaseManagerFactory.getInstance() == null)
        {
            MessageDialogBuilder
                    .error()
                    .message(
                            "No Configuration or Bad Configuration Found!\nPlease Run Main Program First")
                    .buttonType(MessageDialog.ButtonType.OK).show(null);
            System.exit(-1);
        }
        else
        {
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
}
