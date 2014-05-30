package com.mailroom.mainclient;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.*;

import com.mailroom.common.*;
import com.mailroom.common.Package;
import com.panemu.tiwulfx.dialog.MessageDialog;
import com.panemu.tiwulfx.dialog.MessageDialogBuilder;
import com.panemu.tiwulfx.table.*;

public class OpenPageController implements Initializable
{
	private DatabaseManager dbManager;
	private User cUser;
	
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
	private Label lblAutoUpdate;
	@FXML
	private TableView<Package> tblViewTable;
	@FXML
	private Label lblTickCount;
	
	//Columns
	private TickColumn<Package> clmnDelivered;
	private TextColumn<Package> clmnFirstName;
	private TextColumn<Package> clmnLastName;
	private TextColumn<Package> clmnStop;
	private TextColumn<Package> clmnTrackingNumber;
	private TextColumn<Package> clmnCourier;
	private TextColumn<Package> clmnDateReceived;
	private TextColumn<Package> clmnUserName;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) 
	{
		MainFrame.stage.setTitle("Main Page");
		
		cUser = MainFrame.cUser;
		dbManager = MainFrame.dbManager;
		
		String name = "Welcome " + cUser.getFirstName() + " " + cUser.getLastName();
		lblUserLabel.setText(name);
		
		tblViewTable.setEditable(false);
		
		//Create Columns
		clmnDelivered = new TickColumn<Package>();
		clmnFirstName = new TextColumn<Package>("firstName");
		clmnLastName = new TextColumn<Package>("lastName");
		clmnStop = new TextColumn<Package>("stopName");
		clmnTrackingNumber = new TextColumn<Package>("trackingNumber");
		clmnCourier = new TextColumn<Package>("courierName");
		clmnDateReceived = new TextColumn<Package>("receivedDate");
		clmnUserName = new TextColumn<Package>("userName");
		
		//Set Resizable False
		clmnDelivered.setResizable(false);
		clmnFirstName.setResizable(false);
		clmnLastName.setResizable(false);
		clmnStop.setResizable(false);
		clmnTrackingNumber.setResizable(false);
		clmnCourier.setResizable(false);
		clmnDateReceived.setResizable(false);
		clmnUserName.setResizable(false);
		
		//Set Titles
		clmnFirstName.setText("First");
		clmnLastName.setText("Last");
		clmnStop.setText("Stop");
		clmnTrackingNumber.setText("Tracking");
		clmnCourier.setText("Carrier");
		clmnDateReceived.setText("Date");
		clmnUserName.setText("User");
		
		//Define Max Width
		clmnDelivered.setMaxWidth(30);
		clmnFirstName.setMaxWidth(70);
		clmnLastName.setMaxWidth(70);
		clmnStop.setMaxWidth(100); //wider because of data contained
		clmnTrackingNumber.setMaxWidth(70);
		clmnCourier.setMaxWidth(70);
		clmnDateReceived.setMaxWidth(100); //wider because of data contained
		clmnUserName.setMaxWidth(75);
		
		//Add Columns
		tblViewTable.getColumns().add(clmnDelivered);
		tblViewTable.getColumns().add(clmnFirstName);
		tblViewTable.getColumns().add(clmnLastName);
		tblViewTable.getColumns().add(clmnStop);
		tblViewTable.getColumns().add(clmnTrackingNumber);
		tblViewTable.getColumns().add(clmnCourier);
		tblViewTable.getColumns().add(clmnDateReceived);
		tblViewTable.getColumns().add(clmnUserName);
		
		lblTickCount.setText(clmnDelivered.getTickedRecords().size() + " Selected");
		
		dbManager.loadAllPackages();
		
		if(dbManager.getPackages().size() == 0)
		{
			Package p = new Package(-1, "", "", "", "", "", "", "", "", "", false, false, "", false);
			tblViewTable.getItems().add(p);
		}
		else
		{
			tblViewTable.getItems().addAll(dbManager.getPackages());
		}
		
		if(Boolean.valueOf(MainFrame.properties.getProperty("AUTOUPDATE")))
		{
			lblAutoUpdate.setText("Auto Update Enabled");
		}
		else
		{
			lblAutoUpdate.setText("Auto Update Disabled");
		}
	}
	
	public void btnScanPackageAction(ActionEvent ae)
	{
		try
		{
			Parent root = FXMLLoader.load(getClass().getResource("/com/mailroom/fxml/mainclient/ScanPageFx.fxml"));
			Scene scene = new Scene(root);
			MainFrame.stage.setScene(scene);
		}
		catch(IOException e)
		{
			System.err.println("Error: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void btnPrintAction(ActionEvent ae)
	{
		//open Print Scene
	}
	
	public void btnSearchAction(ActionEvent ae)
	{
		//open Search Scene
	}
	
	public void btnRefreshAction(ActionEvent ae)
	{
		//refresh current scene
		//update packages
		//reload packages
		
		ObservableList<Package> delivered = (ObservableList<Package>)clmnDelivered.getTickedRecords();
		
		for(Package p : delivered)
		{
			dbManager.updatePackage(p.getPackageId(), true, true);
		}
		
		dbManager.loadAllPackages();
		
		tblViewTable.getItems().clear();
		
		if(dbManager.getPackages().size() == 0)
		{
			Package p = new Package(-1, "", "", "", "", "", "", "", "", "", false, false, "", false);
			tblViewTable.getItems().add(p);
		}
		else
		{
			tblViewTable.getItems().addAll(dbManager.getPackages());
		}
	}
	
	public void btnSettingsAction(ActionEvent ae)
	{
		try
		{
			Parent root = FXMLLoader.load(getClass().getResource("/com/mailroom/fxml/mainclient/SettingsPageFx.fxml"));
			Scene scene = new Scene(root);
			MainFrame.stage.setScene(scene);
		}
		catch(IOException e)
		{
			System.err.println("Error: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void btnLogoutAction(ActionEvent ae)
	{		
		MessageDialog.Answer a = MessageDialogBuilder.confirmation().message("Confirm Logout?").title("Logout").buttonType(MessageDialog.ButtonType.YES_NO).yesOkButtonText("Yes").noButtonText("No").show(MainFrame.stage.getScene().getWindow());
		
		if(a == MessageDialog.Answer.YES_OK)
		{
			try
			{
				Parent root = FXMLLoader.load(getClass().getResource("/com/mailroom/fxml/mainclient/LoginFx.fxml"));
				Scene scene = new Scene(root);
				MainFrame.stage.setScene(scene);
			}
			catch(IOException e)
			{
				System.err.println("Error: " + e.getMessage());
			}
		}
	}
	
	public void btnQuitAction(ActionEvent ae)
	{		
		MessageDialog.Answer a = MessageDialogBuilder.confirmation().message("Confirm Quit?").title("Quit").buttonType(MessageDialog.ButtonType.YES_NO).yesOkButtonText("Yes").noButtonText("No").show(MainFrame.stage.getScene().getWindow());
		
		if(a == MessageDialog.Answer.YES_OK)
		{
			MainFrame.saveProperties();
			
			dbManager.dispose();
			
			System.exit(0);
		}
	}
	
	public void keyPressedAction(KeyEvent ke)
	{
		if(ke.getCode() == KeyCode.Q)
		{
			if(!ke.getSource().equals(tblViewTable))
			{
				btnLogout.fire();
			}
		}
		if(ke.getCode() == KeyCode.ESCAPE)
		{
			btnQuit.fire();
		}
		if(ke.getCode() == KeyCode.R)
		{
			btnRefresh.fire();
		}
	}
}
