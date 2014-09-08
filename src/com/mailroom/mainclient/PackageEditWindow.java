package com.mailroom.mainclient;

import java.io.IOException;

import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import com.mailroom.common.*;
import com.mailroom.common.Package;
import com.panemu.tiwulfx.dialog.*;

/**
 * Used for Editing Package Data that has already
 * been saved in the database
 * 
 * @author James sitzja@grizzlies.adams.edu
 */
public class PackageEditWindow extends Window
{
	Stage stg;
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
	
	/**
	 * Constructs new Edit Window for a Package
	 */
	public PackageEditWindow()
	{
		stg = new Stage();
		stg.setResizable(false);
		
		try 
		{
			Parent root;
			root = FXMLLoader.load(getClass().getResource("/com/mailroom/fxml/mainclient/PackageEditFx.fxml"));
			Scene scene = new Scene(root, 256, 600);
			stg.setScene(scene);
		} 
		catch (IOException e) 
		{
			Logger.log(e);
		}
		
		EventHandler<ActionEvent> saveHandler = new EventHandler<ActionEvent>()
				{
			public void handle(ActionEvent ae)
			{
				ae.consume();
			}
				};
		btnSave.setOnAction(saveHandler);
		
		EventHandler<ActionEvent> cancelHandler = new EventHandler<ActionEvent>()
				{
			public void handle(ActionEvent ae)
			{
				lblPackageId.setText(String.valueOf(curPackage.getPackageId()));
				txtTrackingNumber.setText(curPackage.getFullTrackingNumber());
				txtFirstName.setText(curPackage.getFirstName());
				txtLastName.setText(curPackage.getLastName());
				txtBoxOffice.setText(curPackage.getBoxOffice());
				txtEmailAddress.setText(curPackage.getEmailAddress());
				cboxStops.setValue(curPackage.getStop());
				cboxCourier.setValue(curPackage.getCourier());
				
				hide();
				ae.consume();
			}
				};
		btnCancel.setOnAction(cancelHandler);
	}
	
	/**
	 * Shows the Window with old Package data
	 */
	public void show()
	{
		MainFrame.stage.setX(MainFrame.stage.getX() - stg.getWidth());
		
		stg.show();
		stg.setX(MainFrame.stage.getX() + MainFrame.stage.getScene().getWidth());
		stg.setY(MainFrame.stage.getY());
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
		
		show();
	}
	
	/**
	 * Hided the Window
	 */
	public void hide()
	{
		MainFrame.stage.centerOnScreen();
		
		stg.hide();
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
			hide();
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
		lblPackageId.setText(String.valueOf(curPackage.getPackageId()));
		txtTrackingNumber.setText(curPackage.getFullTrackingNumber());
		txtFirstName.setText(curPackage.getFirstName());
		txtLastName.setText(curPackage.getLastName());
		txtBoxOffice.setText(curPackage.getBoxOffice());
		txtEmailAddress.setText(curPackage.getEmailAddress());
		cboxStops.setValue(curPackage.getStop());
		cboxCourier.setValue(curPackage.getCourier());
		
		hide();
	}
}
