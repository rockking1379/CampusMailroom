package com.mailroom.mainclient;

import com.mailroom.common.DatabaseManager;
import com.mailroom.common.Logger;
import com.mailroom.common.Package;
import com.mailroom.common.User;
import com.panemu.tiwulfx.dialog.MessageDialog;
import com.panemu.tiwulfx.dialog.MessageDialogBuilder;
import com.panemu.tiwulfx.table.TextColumn;
import com.panemu.tiwulfx.table.TickColumn;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controls OpenPageFx.fxml in com.mailroom.fxml.mainclient
 *
 * @author James sitzja@grizzlies.adams.edu
 */
public class OpenPageController implements Initializable
{
    // Misc//
    private DatabaseManager dbManager;
    private User cUser;
    private PackageEditWindow editWindow;

    // UI Elements//
    @FXML
    private AnchorPane apaneAnchor;
    @FXML
    private Label lblUserLabel;
    @FXML
    private Button btnScanPackage;
    @FXML
    private Button btnPrint;
    @FXML
    private Button btnSearch;
    @FXML
    private Button btnSettings;
    @FXML
    private Button btnRefresh;
    @FXML
    private Button btnQuit;
    @FXML
    private Button btnLogout;
    @FXML
    private TableView<Package> tblViewTable;

    // Columns//
    private TickColumn<Package> clmnDelivered;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1)
    {
        MainFrame.stage.setTitle("Main Page");

        cUser = MainFrame.cUser;
        dbManager = MainFrame.dbManager;

        String name = "Welcome " + cUser.getFirstName() + " "
                + cUser.getLastName();
        lblUserLabel.setText(name);

        // Columns
        TextColumn<Package> clmnFirstName;
        TextColumn<Package> clmnLastName;
        TextColumn<Package> clmnStop;
        TextColumn<Package> clmnTrackingNumber;
        TextColumn<Package> clmnCourier;
        TextColumn<Package> clmnDateReceived;
        TextColumn<Package> clmnUserName;

        // Create Columns
        clmnDelivered = new TickColumn<Package>();
        clmnFirstName = new TextColumn<Package>("firstName");
        clmnLastName = new TextColumn<Package>("lastName");
        clmnStop = new TextColumn<Package>("stop");
        clmnTrackingNumber = new TextColumn<Package>("trackingNumber");
        clmnCourier = new TextColumn<Package>("courier");
        clmnDateReceived = new TextColumn<Package>("receivedDate");
        clmnUserName = new TextColumn<Package>("user");

        // Set Resizable False
        clmnDelivered.setResizable(false);
        clmnFirstName.setResizable(false);
        clmnLastName.setResizable(false);
        clmnStop.setResizable(false);
        clmnTrackingNumber.setResizable(false);
        clmnCourier.setResizable(false);
        clmnDateReceived.setResizable(false);
        clmnUserName.setResizable(false);

        // Set Titles
        clmnFirstName.setText("First");
        clmnLastName.setText("Last");
        clmnStop.setText("Stop");
        clmnTrackingNumber.setText("Tracking");
        clmnCourier.setText("Carrier");
        clmnDateReceived.setText("Date");
        clmnUserName.setText("User");

        // Define Max Width
        clmnDelivered.setMaxWidth(30);
        clmnFirstName.setMaxWidth(70);
        clmnLastName.setMaxWidth(70);
        clmnStop.setMaxWidth(100); // wider because of data contained
        clmnTrackingNumber.setMaxWidth(70);
        clmnCourier.setMaxWidth(70);
        clmnDateReceived.setMaxWidth(100); // wider because of data contained
        clmnUserName.setMaxWidth(75);

        // Add Columns
        tblViewTable.getColumns().add(clmnDelivered);
        tblViewTable.getColumns().add(clmnFirstName);
        tblViewTable.getColumns().add(clmnLastName);
        tblViewTable.getColumns().add(clmnStop);
        tblViewTable.getColumns().add(clmnTrackingNumber);
        tblViewTable.getColumns().add(clmnCourier);
        tblViewTable.getColumns().add(clmnDateReceived);
        tblViewTable.getColumns().add(clmnUserName);

        dbManager.loadAllPackages();

        if (dbManager.getPackages().size() == 0)
        {
            Package p = new Package(-1, "", "", "", "", "", "", null, null,
                    null, false, false, "", false);
            tblViewTable.getItems().add(p);
        }
        else
        {
            tblViewTable.getItems().addAll(dbManager.getPackages());
        }

        editWindow = MainFrame.editWindow;
    }

    /**
     * Opens Package Scanning screen
     *
     * @param ae ActionEvent from OS
     */
    public void btnScanPackageAction(ActionEvent ae)
    {
        ae.consume();
        try
        {
            Parent root = FXMLLoader.load(getClass().getResource(
                    "/com/mailroom/fxml/mainclient/ScanPageFx.fxml"));
            Scene scene = new Scene(root);
            MainFrame.stage.setScene(scene);
        }
        catch (IOException e)
        {
            Logger.log(e);
            e.printStackTrace();
        }
    }

    /**
     * Opens Print Screen
     *
     * @param ae ActionEvent from OS
     */
    public void btnPrintAction(ActionEvent ae)
    {
        ae.consume();

        ObservableList<Package> packages = (ObservableList<Package>) clmnDelivered.getTickedRecords();

        if (packages.size() > 0)
        {
            if (PrintPageController.printReport(packages))
            {
                btnRefresh.fire();
            }
            else
            {
                MessageDialogBuilder.error().message("Error Printing").show(MainFrame.stage.getScene().getWindow());
            }
        }
        else
        {
            try
            {
                Parent root = FXMLLoader.load(getClass().getResource(
                        "/com/mailroom/fxml/mainclient/PrintPageFx.fxml"));
                Scene scene = new Scene(root);
                MainFrame.stage.setScene(scene);
            }
            catch (IOException e)
            {
                Logger.log(e);
            }
        }
    }

    /**
     * Opens Advanced Search Screen
     *
     * @param ae ActionEvent from OS
     */
    public void btnSearchAction(ActionEvent ae)
    {
        ae.consume();
        try
        {
            Parent root = FXMLLoader.load(getClass().getResource(
                    "/com/mailroom/fxml/common/AdvSearchFx.fxml"));
            Scene scene = new Scene(root);
            MainFrame.stage.setScene(scene);
        }
        catch (IOException e)
        {
            Logger.log(e);
        }
    }

    /**
     * Refreshes table on screen
     * Applies any updates from the table to database
     *
     * @param ae ActionEvent from OS
     */
    public void btnRefreshAction(ActionEvent ae)
    {
        ae.consume();
        // refresh current scene
        // update packages
        // reload packages

        ObservableList<Package> delivered = (ObservableList<Package>) clmnDelivered
                .getTickedRecords();

        for (Package p : delivered)
        {
            dbManager.updatePackage(p.getPackageId(), true, true);
        }

        dbManager.loadAllPackages();

        tblViewTable.getItems().clear();

        if (dbManager.getPackages().size() == 0)
        {
            Package p = new Package(-1, "", "", "", "", "", "", null, null,
                    null, false, false, "", false);
            tblViewTable.getItems().add(p);
        }
        else
        {
            tblViewTable.getItems().addAll(dbManager.getPackages());
        }
    }

    /**
     * Opens Settings Screen
     *
     * @param ae ActionEvent from OS
     */
    public void btnSettingsAction(ActionEvent ae)
    {
        ae.consume();
        try
        {
            Parent root = FXMLLoader.load(getClass().getResource(
                    "/com/mailroom/fxml/mainclient/SettingsPageFx.fxml"));
            Scene scene = new Scene(root);
            MainFrame.stage.setScene(scene);
        }
        catch (IOException e)
        {
            Logger.log(e);
            e.printStackTrace();
        }
    }

    /**
     * Returns Program to the Login Screen
     *
     * @param ae ActionEvent from OS
     */
    public void btnLogoutAction(ActionEvent ae)
    {
        ae.consume();
        MessageDialog.Answer a = MessageDialogBuilder.confirmation()
                .message("Confirm Logout?").title("Logout")
                .buttonType(MessageDialog.ButtonType.YES_NO)
                .yesOkButtonText("Yes").noButtonText("No")
                .show(MainFrame.stage.getScene().getWindow());

        if (a == MessageDialog.Answer.YES_OK)
        {
            try
            {
                Parent root = FXMLLoader.load(getClass().getResource(
                        "/com/mailroom/fxml/mainclient/LoginFx.fxml"));
                Scene scene = new Scene(root);
                MainFrame.stage.setScene(scene);
            }
            catch (IOException e)
            {
                Logger.log(e);
            }
        }
    }

    /**
     * Quits Application
     *
     * @param ae ActionEvent from OS
     */
    public void btnQuitAction(ActionEvent ae)
    {
        ae.consume();
        MessageDialog.Answer a = MessageDialogBuilder.confirmation()
                .message("Confirm Quit?").title("Quit")
                .buttonType(MessageDialog.ButtonType.YES_NO)
                .yesOkButtonText("Yes").noButtonText("No")
                .show(MainFrame.stage.getScene().getWindow());

        if (a == MessageDialog.Answer.YES_OK)
        {
            MainFrame.saveProperties();

            dbManager.dispose();

            System.exit(0);
        }
    }

    /**
     * Process Keyboard Input
     *
     * @param ke KeyEvent from OS
     */
    public void keyPressedAction(KeyEvent ke)
    {
        if (ke.getCode() == KeyCode.Q)
        {
            if (!ke.getSource().equals(tblViewTable))
            {
                btnLogout.fire();
            }
        }
        if (ke.getCode() == KeyCode.ESCAPE)
        {
            btnQuit.fire();
        }
        if (ke.getCode() == KeyCode.R)
        {
            btnRefresh.fire();
        }
    }

    /**
     * Processes mouse clicks for table on screen
     *
     * @param me MouseEvent from OS
     */
    public void tblMouseClickAction(MouseEvent me)
    {
        if (me.getClickCount() >= 2)
        {
            editWindow.show(tblViewTable.getItems().get(
                    tblViewTable.getSelectionModel().getSelectedIndex()));
        }
    }
}
