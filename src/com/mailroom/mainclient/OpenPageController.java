package com.mailroom.mainclient;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.*;

import com.mailroom.common.*;
import com.mailroom.common.Package;
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
	private CheckBox chkAutoUpdate;
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
	
	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) 
	{
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
		clmnFirstName.setText("First Name");
		clmnLastName.setText("Last Name");
		clmnStop.setText("Stop");
		clmnTrackingNumber.setText("Tracking #");
		clmnCourier.setText("Carrier");
		clmnDateReceived.setText("Date");
		clmnUserName.setText("User");
		
		//Define Max Width
		clmnFirstName.setMaxWidth(75);
		clmnLastName.setMaxWidth(75);
		clmnStop.setMaxWidth(100); //wider because of data contained
		clmnTrackingNumber.setMaxWidth(75);
		clmnCourier.setMaxWidth(75);
		clmnDateReceived.setMaxWidth(75);
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
		
		clmnDelivered.addEventHandler(null, new customHandler());
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
		
		if(chkAutoUpdate.selectedProperty().getValue())
		{
			chkAutoUpdate.fire();
		}
	}
	
	public void btnScanPackageAction(ActionEvent ae)
	{
		//open Scan Scene
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
		//open Settings Scene
	}
	
	public void btnLogoutAction(ActionEvent ae)
	{
		switch(JOptionPane.showConfirmDialog(null, "Confirm Logout?", "Logout?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE))
		{
			case JOptionPane.YES_OPTION:
			{
				try
				{
					Parent root = FXMLLoader.load(getClass().getResource("/com/mailroom/fxml/LoginFx.fxml"));
					Scene scene = new Scene(root);
					MainFrame.stage.setScene(scene);
				}
				catch(IOException e)
				{
					System.err.println("Error: " + e.getMessage());
				}
				break;
			}
			default:
			{
				break;
			}
		}
	}
	
	public void btnQuitAction(ActionEvent ae)
	{
		switch(JOptionPane.showConfirmDialog(null, "Confirm Quit?", "Quit", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE))
		{
			case JOptionPane.YES_OPTION:
			{
				MainFrame.saveProperties();
				
				dbManager.dispose();
				
				System.exit(0);
				break;
			}
			default:
			{
				break;
			}
		}
	}
	
	public void chkAutoUpdateAction(ActionEvent ae)
	{
		if(chkAutoUpdate.selectedProperty().getValue())
		{
			MainFrame.properties.setProperty("AUTOUPDATE", Boolean.toString(true));
		}
		else
		{
			MainFrame.properties.setProperty("AUTOUPDATE", Boolean.toString(false));
		}
	}
	
	public void keyPressedAction(KeyEvent ke)
	{
		if(ke.getCode() == KeyCode.Q)
		{
			btnLogout.fire();
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

	@SuppressWarnings("rawtypes")
	private class customHandler implements EventHandler
	{

		@Override
		public void handle(Event event)
		{
			lblTickCount.setText(clmnDelivered.getTickedRecords().size() + " Selected");
		}
	}
}
