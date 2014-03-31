package com.mailroom.mainclient;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import com.mailroom.common.*;

public class SettingsController implements Initializable
{
	@FXML
	private Tab tabStopManagement;
	@FXML
	private Tab tabRouteManagement;
	@FXML
	private Tab tabCourierManagement;
	@FXML
	private Tab tabAbout;

	//General Settings//
	@FXML
	private Tab tabGeneral;
	@FXML
	private RadioButton rbtnSqlite;
	@FXML
	private RadioButton rbtnMysql;
	@FXML
	private TextField txtDatabaseLocation;
	@FXML
	private TextField txtDatabaseName;
	@FXML
	private TextField txtDatabaseUserName;
	@FXML
	private PasswordField pwdDatabasePassword;
	@FXML
	private ComboBox<Boolean> cboxAutoUpdate;
	@FXML
	private Button btnBrowse;
	@FXML
	private Button btnSave;
	
	//Account Management//
	@FXML
	private Tab tabAccountManagement;
	@FXML
	private Tab tabEditAccount;
	@FXML
	private Tab tabCreateAccount;
	@FXML
	private Tab tabAdministrative;
	
	private DatabaseManager dbManager;
	private Properties prefs;
	
	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		MainFrame.stage.setTitle("Settings");
		
		this.dbManager = MainFrame.dbManager;
		this.prefs = MainFrame.properties;
		
		if(Boolean.valueOf(prefs.getProperty("SQLITE")))
		{
			rbtnMysql.setSelected(false);
			rbtnSqlite.setSelected(true);
			txtDatabaseLocation.setText(prefs.getProperty("DATABASE"));
			txtDatabaseName.setDisable(true);
			txtDatabaseUserName.setDisable(true);
			pwdDatabasePassword.setDisable(true);
		}
		if(Boolean.valueOf(prefs.getProperty("MYSQL")))
		{			
			rbtnMysql.setSelected(true);
			rbtnSqlite.setSelected(false);
			txtDatabaseLocation.setText(prefs.getProperty("DATABASE"));
			txtDatabaseName.setDisable(false);
			txtDatabaseUserName.setDisable(false);
			pwdDatabasePassword.setDisable(false);
			
			txtDatabaseName.setText(prefs.getProperty("DBNAME"));
			txtDatabaseUserName.setText(prefs.getProperty("USERNAME"));
			pwdDatabasePassword.setText(prefs.getProperty("PASSWORD"));
		}
		
		if(MainFrame.cUser.getAdmin())
		{
			tabGeneral.setDisable(false);
			tabCreateAccount.setDisable(false);
			tabAdministrative.setDisable(false);
			tabStopManagement.setDisable(false);
			tabRouteManagement.setDisable(false);
			tabCourierManagement.setDisable(false);
		}
		else
		{
			tabGeneral.setDisable(true);
			tabCreateAccount.setDisable(true);
			tabAdministrative.setDisable(true);
			tabStopManagement.setDisable(true);
			tabRouteManagement.setDisable(true);
			tabCourierManagement.setDisable(true);
		}
		
		cboxAutoUpdate.getItems().clear();
		cboxAutoUpdate.getItems().add(true);
		cboxAutoUpdate.getItems().add(false);
		cboxAutoUpdate.setValue(Boolean.valueOf(prefs.getProperty("AUTOUPDATE")));
	}
	
	public void keyPressedAction(KeyEvent ke)
	{
		if(ke.getCode() == KeyCode.ESCAPE)
		{
			try
			{
				Parent root = FXMLLoader.load(getClass().getResource("/com/mailroom/fxml/mainclient/OpenPageFx.fxml"));
				Scene scene = new Scene(root);
				MainFrame.stage.setScene(scene);
			}
			catch(IOException e)
			{
				System.err.println("Error: " + e.getMessage());
				e.printStackTrace();
			}
		}
	}
	
	public void cboxAutoUpdateAction(ActionEvent ae)
	{
		prefs.setProperty("AUTOUPDATE", cboxAutoUpdate.getValue().toString());
		MainFrame.properties.setProperty("AUTOUPDATE", cboxAutoUpdate.getValue().toString());
	}
	
	public void rbtnMysqlAction(ActionEvent ae)
	{
		rbtnMysql.setSelected(true);
		rbtnSqlite.setSelected(false);
		txtDatabaseName.setDisable(false);
		txtDatabaseUserName.setDisable(false);
		pwdDatabasePassword.setDisable(false);
		
		txtDatabaseLocation.setText("");
		txtDatabaseUserName.setText("");
		txtDatabaseName.setText("");
		pwdDatabasePassword.setText("");
	}
	
	public void rbtnSqliteAction(ActionEvent ae)
	{
		rbtnMysql.setSelected(false);
		rbtnSqlite.setSelected(true);
		txtDatabaseName.setDisable(true);
		txtDatabaseUserName.setDisable(true);
		pwdDatabasePassword.setDisable(true);
		
		txtDatabaseLocation.setText("");
		txtDatabaseUserName.setText("");
		txtDatabaseName.setText("");
		pwdDatabasePassword.setText("");
	}
	
	public void btnSaveAction(ActionEvent ae)
	{
		if(rbtnSqlite.selectedProperty().getValue())
		{
			MainFrame.properties.setProperty("SQLITE", Boolean.toString(true));
			MainFrame.properties.setProperty("MYSQL", Boolean.toString(false));
			MainFrame.properties.setProperty("DATABASE", txtDatabaseLocation.getText());
			MainFrame.properties.setProperty("USERNAME", "");
			MainFrame.properties.setProperty("PASSWORD", "");
			MainFrame.properties.setProperty("DBNAME", "");
			MainFrame.properties.setProperty("AUTOUPDATE", Boolean.toString(cboxAutoUpdate.getValue()));
		}
		if(rbtnMysql.selectedProperty().getValue())
		{
			MainFrame.properties.setProperty("SQLITE", Boolean.toString(false));
			MainFrame.properties.setProperty("MYSQL", Boolean.toString(true));
			MainFrame.properties.setProperty("DATABASE", txtDatabaseLocation.getText());
			MainFrame.properties.setProperty("USERNAME", txtDatabaseUserName.getText());
			MainFrame.properties.setProperty("PASSWORD", pwdDatabasePassword.getText());
			MainFrame.properties.setProperty("DBNAME", txtDatabaseName.getText());
			MainFrame.properties.setProperty("AUTOUPDATE", Boolean.toString(cboxAutoUpdate.getValue()));
		}
		
		MainFrame.saveProperties();
		
		JOptionPane.showMessageDialog(null, "Program Must Be Restarted");		
		dbManager.dispose();
		System.exit(0);
	}
}