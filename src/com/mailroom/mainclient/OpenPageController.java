package com.mailroom.mainclient;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.*;

import com.mailroom.common.*;
import com.mailroom.common.Package;

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
	private TableColumn<Package, Boolean> clmnDelivered;
	@FXML
	private TableColumn<Package, String> clmnTrackingNumber;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) 
	{
		cUser = MainFrame.cUser;
		dbManager = MainFrame.dbManager;
		
		String name = "Welcome " + cUser.getFirstName() + " " + cUser.getLastName();
		lblUserLabel.setText(name);
		
		tblViewTable.setEditable(false);
		
		clmnDelivered.setCellValueFactory(new PropertyValueFactory<Package, Boolean>("atStop"));
		clmnTrackingNumber.setCellValueFactory(new PropertyValueFactory<Package, String>("trackingNumber"));
		
		dbManager.loadAllPackages();
		
		if(dbManager.getPackages().size() == 0)
		{
			tblViewTable.getItems().add(new Package(-1, "1z54165412198", "", "", "", "", "", "", "", "", false, false, "", false));
		}
		else
		{
			tblViewTable.getItems().addAll(dbManager.getPackages());
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
		dbManager.dispose();
		
		System.exit(0);
	}
	
	public void chkAutoUpdateAction(ActionEvent ae)
	{
		//change auto update preferences
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
}
