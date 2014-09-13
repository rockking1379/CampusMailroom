package com.mailroom.mainclient;

import java.net.URL;
import java.util.ResourceBundle;

import com.mailroom.common.Courier;
import com.mailroom.common.Package;
import com.mailroom.common.Stop;
import com.panemu.tiwulfx.dialog.MessageDialog;
import com.panemu.tiwulfx.dialog.MessageDialogBuilder;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

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

	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{
		curPackage = PackageEditWindow.getPackage();
		
		if(curPackage != null)
		{
			lblPackageId.setText("Package #" + String.valueOf(curPackage.getPackageId()));
			txtTrackingNumber.setText(curPackage.getFullTrackingNumber());
			txtFirstName.setText(curPackage.getFirstName());
			txtLastName.setText(curPackage.getLastName());
			txtBoxOffice.setText(curPackage.getBoxOffice());
			txtEmailAddress.setText(curPackage.getEmailAddress());
		}
		//need to fill combo boxes
	}
	
	/**
	 * Performed on Save Button Click
	 * @param ae Event from OS
	 */
	public void btnSaveAction(ActionEvent ae)
	{
		if(MainFrame.dbManager.updatePackage(new Package(curPackage.getPackageId(), txtTrackingNumber.getText(), curPackage.getReceivedDate(), txtEmailAddress.getText(), txtFirstName.getText(), txtLastName.getText(), txtBoxOffice.getText(), cboxStops.getValue(), cboxCourier.getValue(), curPackage.getUser(), curPackage.isAtStop(), curPackage.isPickedUp(), curPackage.getDatePickedUp(), curPackage.isReturned())))
		{
			MessageDialogBuilder.info().title("Success").buttonType(MessageDialog.ButtonType.OK).message("Package Updated").show(MainFrame.stage.getScene().getWindow());
		}
		else
		{
			MessageDialogBuilder.error().title("Error").buttonType(MessageDialog.ButtonType.OK).message("Could Not Update Package").show(MainFrame.stage.getScene().getWindow());
		}
	}
	
	/**
	 * Performed on Cancel Button Click
	 * @param ae Event from OS
	 */
	public void btnCancelAction(ActionEvent ae)
	{
		if(curPackage != null)
		{
			lblPackageId.setText("Package #" + String.valueOf(curPackage.getPackageId()));
			txtTrackingNumber.setText(curPackage.getFullTrackingNumber());
			txtFirstName.setText(curPackage.getFirstName());
			txtLastName.setText(curPackage.getLastName());
			txtBoxOffice.setText(curPackage.getBoxOffice());
			txtEmailAddress.setText(curPackage.getEmailAddress());
			cboxStops.setValue(curPackage.getStop());
			cboxCourier.setValue(curPackage.getCourier());
		}
	}
	
	/**
	 * Shows the Window with new Package Data
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
