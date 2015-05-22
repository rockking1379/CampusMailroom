package com.mailroom.mainclient;

import com.mailroom.common.*;
import com.mailroom.common.Package;
import com.panemu.tiwulfx.dialog.MessageDialog;
import com.panemu.tiwulfx.dialog.MessageDialogBuilder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.ResourceBundle;

//import java.util.ArrayList;

/**
 * Controls ScanPageFx.fxml in com.mailroom.fxml.mainclient
 *
 * @author James rockking1379@gmail.com
 */
public class ScanPageController implements Initializable
{
    @FXML
    private TextField txtTrackingNumber;
    @FXML
    private TextField txtFirstName;
    @FXML
    private TextField txtLastName;
    @FXML
    private TextField txtBoxOffice;
    @FXML
    private TextField txtEmailAddress;
    @FXML
    private Label lblDate;
    @FXML
    private ComboBox<Stop> cboxStops;
    @FXML
    private ComboBox<Courier> cboxCourier;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnClear;
    @FXML
    private Button btnExit;
    @FXML
    private Button btnRandomGenerate;

    private DatabaseManager dbManager;
    private User cUser;
    private String stopSearch = "";
    private String courierSearch = "";

    @Override
    public void initialize(URL arg0, ResourceBundle arg1)
    {
        Logger.logEvent("Loading ScanPage", "SYSTEM");
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date now = new Date();
        lblDate.setText(format.format(now));

        cUser = MainFrame.cUser;
        dbManager = MainFrame.dbManager;

        cboxStops.getItems().clear();
        cboxCourier.getItems().clear();

        for (Stop s : dbManager.getStops())
        {
            cboxStops.getItems().add(s);
        }
        for (Courier c : dbManager.getCouriers())
        {
            cboxCourier.getItems().add(c);
        }
    }

    /**
     * Exits Package Scanning Screen
     * Goes back to OpenPage
     *
     * @param ae ActionEvent from OS
     */
    public void btnExitAction(ActionEvent ae)
    {
        ae.consume();
        Logger.logEvent("Exit ScanPage Requested", cUser.getUserName());
        try
        {
            Parent root = FXMLLoader.load(getClass().getResource(
                    "/com/mailroom/fxml/mainclient/OpenPageFx.fxml"));
            Scene scene = new Scene(root);
            MainFrame.stage.setScene(scene);
        }
        catch (IOException e)
        {
            Logger.logException(e);
            e.printStackTrace();
        }
    }

    /**
     * Clears all text fields in scan page
     *
     * @param ae ActionEvent from OS
     */
    public void btnClearAction(ActionEvent ae)
    {
        ae.consume();
        Logger.logEvent("Cleared All Fields", cUser.getUserName());
        txtTrackingNumber.setText("");
        txtFirstName.setText("");
        txtLastName.setText("");
        txtBoxOffice.setText("");
        txtEmailAddress.setText("");
        stopSearch = "";
        courierSearch = "";
        txtTrackingNumber.requestFocus();
    }

    /**
     * Saves all entered data to a new package
     * then saves to database
     *
     * @param ae ActionEvent from OS
     */
    public void btnSaveAction(ActionEvent ae)
    {
        ae.consume();
        Logger.logEvent("Verifying Package Data", "SYSTEM");
        boolean verified = true;

        if (txtTrackingNumber.getText().length() < 4)
        {
            MessageDialogBuilder
                    .error()
                    .message(
                            "Tracking Number Not Long Enough\nMust Be 4 Characters or Longer")
                    .title("Error").buttonType(MessageDialog.ButtonType.OK)
                    .show(MainFrame.stage.getScene().getWindow());
            verified = false;
        }
        if (txtFirstName.getText().equals("")
                && txtLastName.getText().equals(""))
        {
            txtFirstName.setText("DEPT");
            txtLastName.setText("DEPT");
        }
        else
        {
            if (txtFirstName.getText().equals("")
                    && !txtLastName.getText().equals(""))
            {
                MessageDialogBuilder.error().message("No Last Name Specified")
                        .title("Error").buttonType(MessageDialog.ButtonType.OK)
                        .show(MainFrame.stage.getScene().getWindow());
                verified = false;
            }
            else
            {
                if (!txtFirstName.getText().equals("")
                        && txtLastName.getText().equals(""))
                {
                    MessageDialogBuilder.error()
                            .message("No First Name Specified").title("Error")
                            .buttonType(MessageDialog.ButtonType.OK)
                            .show(MainFrame.stage.getScene().getWindow());
                    verified = false;
                }
            }
        }

        if (txtBoxOffice.getText().equals(""))
        {
            txtBoxOffice.setText("---");
        }

        if (txtEmailAddress.getText().equals(""))
        {
            txtEmailAddress.setText("Unknown@");
        }
        if (verified)
        {
            Logger.logEvent("Checking if Tracking Number Exists\n" +
                    "Tracking Number: " + txtTrackingNumber.getText(), "SYSTEM");
            int pid = dbManager
                    .checkTrackingNumber(txtTrackingNumber.getText());
            if (pid == -1)
            {
                Package p = new Package(-1, txtTrackingNumber.getText(),
                        lblDate.getText(), txtEmailAddress.getText(),
                        txtFirstName.getText(), txtLastName.getText(),
                        txtBoxOffice.getText(), cboxStops.getValue(),
                        cboxCourier.getValue(), cUser, false, false, null,
                        false);
                Logger.logEvent("Submitting Package to Database", cUser.getUserName());
                if (dbManager.addPackage(p))
                {
                    btnClear.fire();
                }
                else
                {
                    MessageDialogBuilder.error()
                            .message("Error Adding Package").title("Error")
                            .buttonType(MessageDialog.ButtonType.OK)
                            .show(MainFrame.stage.getScene().getWindow());
                }
            }
            else
            {
                Logger.logEvent("Tracking Number Already Exists", "SYSTEM");
                MessageDialog.Answer ans = MessageDialogBuilder
                        .error()
                        .title("Tracking Number Found")
                        .message(
                                "Tracking Number already exists\nOverwrite or Create New Record?")
                        .buttonType(MessageDialog.ButtonType.YES_NO)
                        .yesOkButtonText("New Record")
                        .noButtonText("Overwrite")
                        .show(MainFrame.stage.getScene().getWindow());

                if (ans == MessageDialog.Answer.YES_OK)
                {
                    Logger.logEvent("User Selected to Create New Record", cUser.getUserName());
                    Package p = new Package(-1, txtTrackingNumber.getText(),
                            lblDate.getText(), txtEmailAddress.getText(),
                            txtFirstName.getText(), txtLastName.getText(),
                            txtBoxOffice.getText(), cboxStops.getValue(),
                            cboxCourier.getValue(), cUser, false, false, null,
                            false);

                    if (dbManager.addPackage(p))
                    {
                        btnClear.fire();
                    }
                    else
                    {
                        MessageDialogBuilder.error()
                                .message("Error Adding Package").title("Error")
                                .buttonType(MessageDialog.ButtonType.OK)
                                .show(MainFrame.stage.getScene().getWindow());
                    }
                }
                if (ans == MessageDialog.Answer.NO)
                {
                    Logger.logEvent("User Selected to Overwrite Existing Record", cUser.getUserName());
                    Package p = new Package(pid, txtTrackingNumber.getText(),
                            lblDate.getText(), txtEmailAddress.getText(),
                            txtFirstName.getText(), txtLastName.getText(),
                            txtBoxOffice.getText(), cboxStops.getValue(),
                            cboxCourier.getValue(), cUser, false, false, null,
                            false);

                    if (dbManager.updatePackage(p))
                    {
                        btnClear.fire();
                    }
                    else
                    {
                        MessageDialogBuilder.error()
                                .message("Error Adding Package").title("Error")
                                .buttonType(MessageDialog.ButtonType.OK)
                                .show(MainFrame.stage.getScene().getWindow());
                    }
                }
            }
        }
    }

    /**
     * Generates Random Tracking Number
     *
     * @param ae ActionEvent from OS
     */
    public void btnRandomGenerateAction(ActionEvent ae)
    {
        ae.consume();
        Logger.logEvent("Generating Random Tracking Number", cUser.getUserName());
        String tnum = MainFrame.properties.getProperty("TNUMPREFIX");

        for (int i = 0; i < 16; i++)
        {
            tnum += generate();
        }

        txtTrackingNumber.setText(tnum);
    }

    /**
     * Processes keyboard input
     *
     * @param ke KeyEvent from OS
     */
    public void keyPressAction(KeyEvent ke)
    {
        if (ke.getCode() == KeyCode.ESCAPE)
        {
            btnExit.fire();
        }
        if (ke.getCode() == KeyCode.ENTER)
        {
            if (txtBoxOffice.focusedProperty().get())
            {

                MessageDialogBuilder.info().message("Auto Fill Disabled")
                        .title("Error").buttonType(MessageDialog.ButtonType.OK)
                        .show(MainFrame.stage.getScene().getWindow());
            }
            else
            {
                if (txtTrackingNumber.focusedProperty().get())
                {
                    txtFirstName.requestFocus();
                }
                else
                {
                    if (txtFirstName.focusedProperty().get()
                            || txtLastName.focusedProperty().get()
                            || txtEmailAddress.focusedProperty().get())
                    {
                        // do nothing
                    }
                    else
                    {
                        btnSave.fire();
                    }
                }
            }
        }
    }

    /**
     * Processes keyboard typing on combobox
     *
     * @param ke KeyEvent from OS
     */
    public void cboxStopsKeyPressAction(KeyEvent ke)
    {
        if (ke.getCode() == KeyCode.BACK_SPACE)
        {
            stopSearch = "";
            courierSearch = "";
        }
        else
        {
            if (ke.getCode() == KeyCode.TAB && ke.isShiftDown())
            {
                txtEmailAddress.requestFocus();
            }
            else
            {
                if (ke.getCode() == KeyCode.TAB && !ke.isShiftDown())
                {
                    stopSearch = "";
                    courierSearch = "";
                    cboxCourier.requestFocus();
                }
                else
                {
                    stopSearch += ke.getCode().toString();

                    for (Stop s : cboxStops.getItems())
                    {
                        if (s.getStopName().toUpperCase()
                                .startsWith(stopSearch.toUpperCase()))
                        {
                            cboxStops.setValue(s);
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * Processes keyboard typing on combobox
     *
     * @param ke KeyEvent from OS
     */
    public void cboxCourierKeyPressAction(KeyEvent ke)
    {
        if (ke.getCode() == KeyCode.BACK_SPACE)
        {
            stopSearch = "";
            courierSearch = "";
        }
        else
        {
            if (ke.getCode() == KeyCode.TAB && !ke.isShiftDown())
            {
                btnSave.requestFocus();
            }
            else
            {
                if (ke.getCode() == KeyCode.TAB && ke.isShiftDown())
                {
                    stopSearch = "";
                    courierSearch = "";
                    cboxStops.requestFocus();
                }
                else
                {
                    courierSearch += ke.getCode().toString();

                    for (Courier c : cboxCourier.getItems())
                    {
                        if (c.getCourierName().toUpperCase()
                                .startsWith(courierSearch.toUpperCase()))
                        {
                            cboxCourier.setValue(c);
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * Generates Random Character
     *
     * @return random character
     */
    private char generate()
    {
        int val;

        Random r = new Random();

        val = r.nextInt(74) + 48;

        if (val >= 58 && val <= 64)
        {
            val = (int) generate();
        }
        if (val >= 91 && val <= 96)
        {
            val = (int) generate();
        }

        return (char) val;
    }
}
