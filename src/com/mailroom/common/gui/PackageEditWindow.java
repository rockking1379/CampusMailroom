package com.mailroom.common.gui;

import com.mailroom.common.*;
import com.mailroom.common.Package;
import com.mailroom.mainclient.MainFrame;
import com.mailroom.otherclient.OtherMainFrame;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

import java.io.IOException;

/**
 * Used for Editing Package Data that has already been saved in the database
 *
 * @author James sitzja@grizzlies.adams.edu
 */
public class PackageEditWindow extends Window
{
    Stage stg;
    static com.mailroom.common.Package curPackage;
    static Button actionButton;
    static Window win;

    /**
     * Constructs new Edit Window for a Package
     */
    public PackageEditWindow()
    {
        stg = new Stage(StageStyle.UNDECORATED);
        stg.setResizable(false);
        win = this;
    }

    /**
     * Shows the Window with old Package data
     *
     * @param p Package to be Shown
     */
    public void show(Package p, Button actBtn)
    {
        curPackage = p;
        actionButton = actBtn;

        try
        {
            Parent root;
            root = FXMLLoader.load(getClass().getResource(
                    "/com/mailroom/fxml/common/PackageEditFx.fxml"));

            if (MainFrame.stage != null)
            {
                Scene scene = new Scene(root, 256, MainFrame.stage.getScene().getWindow().getHeight() + 10);
                stg.setScene(scene);

                MainFrame.stage.setX(MainFrame.stage.getX()
                        - stg.getScene().getWindow().getWidth());

                stg.show();
                stg.setX(MainFrame.stage.getX()
                        + MainFrame.stage.getScene().getWindow().getWidth());
                stg.setY(MainFrame.stage.getY() - 5);
            }
            if (OtherMainFrame.stage != null)
            {
                Scene scene = new Scene(root, 256, OtherMainFrame.stage.getScene().getWindow().getHeight());
                stg.setScene(scene);

                OtherMainFrame.stage.setX(OtherMainFrame.stage.getX()
                        - stg.getScene().getWindow().getWidth());

                stg.show();
                stg.setX(OtherMainFrame.stage.getX()
                        + OtherMainFrame.stage.getScene().getWindow()
                        .getWidth());
                stg.setY(OtherMainFrame.stage.getY() - 5);
            }
        }
        catch (IOException e)
        {
            Logger.logException(e);
        }
    }

    /**
     * Hided the Window
     */
    public void hide()
    {
        if (MainFrame.stage != null)
        {
            MainFrame.stage.centerOnScreen();
        }
        if (OtherMainFrame.stage != null)
        {
            OtherMainFrame.stage.centerOnScreen();
        }

        stg.hide();
    }

    /**
     * Gets package that needs edited
     *
     * @return Package that needs edited
     */
    public static Package getPackage()
    {
        return curPackage;
    }

    public static Button getActionButton()
    {
        return actionButton;
    }

    /**
     * Returns current window
     *
     * @return current window
     */
    public static Window getWindow()
    {
        return win;
    }
}
