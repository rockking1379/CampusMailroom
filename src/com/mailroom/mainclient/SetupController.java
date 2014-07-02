package com.mailroom.mainclient;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;

import com.mailroom.common.*;
import com.panemu.tiwulfx.dialog.MessageDialog;
import com.panemu.tiwulfx.dialog.MessageDialogBuilder;

public class SetupController implements Initializable
{	
	//Tabs//
	@FXML
	private Tab tabDatabaseSetup;
	@FXML
	private Tab tabAccountSetup;
	@FXML
	private Tab tabCourierSetup;
	@FXML
	private Tab tabStopSetup;
	
	//Database Setup//
	@FXML
	private ComboBox<String> cboxDbSetupDbType;
	@FXML
	private TextField txtDbSetupDbLocation;
	@FXML
	private TextField txtDbSetupDbName;
	@FXML
	private TextField txtDbSetupDbUsername;
	@FXML
	private PasswordField pwdDbSetupDbPassword;
	@FXML
	private Button btnDbSetupBrowse;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) 
	{
		cboxDbSetupDbType.getItems().clear();
		cboxDbSetupDbType.getItems().add(SQLiteManager.dbName);
		cboxDbSetupDbType.getItems().add(MysqlManager.dbName);
		cboxDbSetupDbType.getItems().add(PostgreSQLManager.dbName);
	}
	
	public void cboxDbSetupDbTypeAction(ActionEvent ae)
	{
		txtDbSetupDbLocation.setText("");
		txtDbSetupDbName.setText("");
		txtDbSetupDbUsername.setText("");
		pwdDbSetupDbPassword.setText("");
		
		switch(Integer.valueOf(cboxDbSetupDbType.getValue().charAt(0)))
		{
			case 0:
			{
				txtDbSetupDbName.setDisable(true);
				txtDbSetupDbUsername.setDisable(true);
				pwdDbSetupDbPassword.setDisable(true);
				break;
			}
			case 1:
			{
				txtDbSetupDbName.setDisable(false);
				txtDbSetupDbUsername.setDisable(false);
				pwdDbSetupDbPassword.setDisable(false);
				break;
			}
			case 2:
			{
				txtDbSetupDbName.setDisable(false);
				txtDbSetupDbUsername.setDisable(false);
				pwdDbSetupDbPassword.setDisable(false);
				break;
			}
			default:
			{
				MessageDialogBuilder.error().message("Unknown Database Type Selected").show(MainFrame.stage.getScene().getWindow());
				break;
			}
		}
	}
	
	public void btnSaveAction(ActionEvent ae)
	{
		//verify settings
		//save settings
	}
}
