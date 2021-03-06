package com.mailroom.common.gui;

import com.mailroom.common.database.DatabaseManagerFactory;
import com.mailroom.common.objects.Courier;
import com.mailroom.common.objects.Package;
import com.mailroom.common.objects.Stop;
import com.mailroom.mainclient.MainFrame;
import com.mailroom.otherclient.OtherMainFrame;
import com.panemu.tiwulfx.dialog.MessageDialog;
import com.panemu.tiwulfx.dialog.MessageDialogBuilder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for com.mailroom.fxml.common.PackageEditFx
 *
 * @author James rockking1379@gmail.com
 */
public class PackageEditController implements Initializable
{
    Package curPackage;

    @FXML
    private Label lblPackageId;
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
    private ComboBox<Stop> cboxStops;
    @FXML
    private ComboBox<Courier> cboxCourier;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnCancel;
    @FXML
    private CheckBox cboxAtStop;
    @FXML
    private CheckBox cboxPickedUp;
    @FXML
    private CheckBox cboxReturned;

    /**
     * Initializes the controller
     *
     * @param arg0 not sure
     * @param arg1 still not sure
     */
    @Override
    public void initialize(URL arg0, ResourceBundle arg1)
    {
        curPackage = PackageEditWindow.getPackage();

        cboxStops.getItems().clear();
        for (Stop s : DatabaseManagerFactory.getInstance().getStops())
        {
            cboxStops.getItems().add(s);
        }

        cboxCourier.getItems().clear();
        for (Courier c : DatabaseManagerFactory.getInstance().getCouriers())
        {
            cboxCourier.getItems().add(c);
        }

        if (curPackage != null)
        {
            lblPackageId.setText("Package #"
                    + String.valueOf(curPackage.getPackageId()));
            txtTrackingNumber.setText(curPackage.getFullTrackingNumber());
            txtFirstName.setText(curPackage.getFirstName());
            txtLastName.setText(curPackage.getLastName());
            txtBoxOffice.setText(curPackage.getBoxOffice());
            txtEmailAddress.setText(curPackage.getEmailAddress());
            cboxAtStop.setSelected(curPackage.isAtStop());
            cboxPickedUp.setSelected(curPackage.isPickedUp());
            cboxReturned.setSelected(curPackage.isReturned());
            cboxStops.setValue(curPackage.getStop());
            cboxCourier.setValue(curPackage.getCourier());
        }
    }

    /**
     * Performed on Save Button Click
     *
     * @param ae Event from OS
     */
    public void btnSaveAction(ActionEvent ae)
    {
        ae.consume();
        if (DatabaseManagerFactory.getInstance().updatePackage(new Package(curPackage
                .getPackageId(), txtTrackingNumber.getText(), curPackage
                .getReceivedDate(), txtEmailAddress.getText(), txtFirstName
                .getText(), txtLastName.getText(), txtBoxOffice.getText(),
                cboxStops.getValue(), cboxCourier.getValue(), curPackage
                .getUser(), cboxAtStop.isSelected(), cboxPickedUp
                .isSelected(), curPackage.getDatePickedUp(),
                cboxReturned.isSelected())))
        {
            if (MainFrame.stage != null)
            {
                MessageDialogBuilder.info().title("Success")
                        .buttonType(MessageDialog.ButtonType.OK)
                        .message("Package Updated")
                        .show(MainFrame.stage.getScene().getWindow());
            }
            if (OtherMainFrame.stage != null)
            {
                MessageDialogBuilder.info().title("Success")
                        .buttonType(MessageDialog.ButtonType.OK)
                        .message("Package Updated")
                        .show(OtherMainFrame.stage.getScene().getWindow());
            }
            PackageEditWindow.getWindow().hide();
        }
        else
        {
            if (MainFrame.stage != null)
            {
                MessageDialogBuilder.error().title("Error")
                        .buttonType(MessageDialog.ButtonType.OK)
                        .message("Could Not Update Package")
                        .show(MainFrame.stage.getScene().getWindow());
            }
            if (OtherMainFrame.stage != null)
            {
                MessageDialogBuilder.error().title("Error")
                        .buttonType(MessageDialog.ButtonType.OK)
                        .message("Could Not Update Package")
                        .show(OtherMainFrame.stage.getScene().getWindow());
            }
        }

        if (PackageEditWindow.getActionButton() != null)
        {
            PackageEditWindow.getActionButton().fire();
        }
    }

    /**
     * Performed on Cancel Button Click
     *
     * @param ae Event from OS
     */
    public void btnCancelAction(ActionEvent ae)
    {
        ae.consume();
        if (curPackage != null)
        {
            lblPackageId.setText("Package #"
                    + String.valueOf(curPackage.getPackageId()));
            txtTrackingNumber.setText(curPackage.getFullTrackingNumber());
            txtFirstName.setText(curPackage.getFirstName());
            txtLastName.setText(curPackage.getLastName());
            txtBoxOffice.setText(curPackage.getBoxOffice());
            txtEmailAddress.setText(curPackage.getEmailAddress());
            cboxStops.setValue(curPackage.getStop());
            cboxCourier.setValue(curPackage.getCourier());
        }
        PackageEditWindow.getWindow().hide();
    }

    /**
     * Process keyboard input
     * required to exit this screen
     * should probably add second way of exiting
     *
     * @param ke KeyEvent from OS
     */
    public void keyPressedAction(KeyEvent ke)
    {
        if (ke.getCode() == KeyCode.ESCAPE)
        {
            btnCancel.fire();
        }
    }

    /**
     * Shows the Window with new Package Data
     *
     * @param p Package Data to be shown
     */
    public void show(Package p)
    {
        curPackage = p;

        lblPackageId.setText("Package #" + String.valueOf(p.getPackageId()));
        txtTrackingNumber.setText(p.getFullTrackingNumber());
        txtFirstName.setText(p.getFirstName());
        txtLastName.setText(p.getLastName());
        txtBoxOffice.setText(p.getBoxOffice());
        txtEmailAddress.setText(p.getEmailAddress());
        cboxStops.setValue(p.getStop());
        cboxCourier.setValue(p.getCourier());
    }
}
