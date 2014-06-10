package com.mailroom.mainclient;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.URL;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

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
import com.panemu.tiwulfx.dialog.MessageDialog;
import com.panemu.tiwulfx.dialog.MessageDialogBuilder;

public class SettingsController implements Initializable
{
	//Tab Views//
	@FXML
	private Tab tabStopManagement;
	@FXML
	private Tab tabRouteManagement;
	@FXML
	private Tab tabCourierManagement;
	@FXML
	private Tab tabAbout;
	@FXML
	private TabPane tabpaneMainPane;

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
	@FXML
	private Button btnCancel;
	@FXML
	private Slider sldrAutoUpdateFreq;
	
	//Account Management//
	@FXML
	private Tab tabAccountManagement;
	@FXML
	private Tab tabEditAccount;
	@FXML
	private Tab tabCreateAccount;
	@FXML
	private Tab tabAdministrative;
	
	//Edit Account//
	@FXML
	private Label lblUserId;
	@FXML
	private PasswordField pwdChangePwdOld;
	@FXML
	private PasswordField pwdChangePwdNew;
	@FXML
	private PasswordField pwdChangePwdConfirm;
	@FXML
	private Button btnChangePwd;

	//Create Account//
	@FXML
	private TextField txtCreateFirstName;
	@FXML
	private TextField txtCreateLastName;
	@FXML
	private TextField txtCreateUserName;
	@FXML
	private PasswordField pwdCreatePwd;
	@FXML
	private PasswordField pwdCreateConfirm;
	@FXML
	private CheckBox cboxCreateAdmin;
	@FXML
	private Button btnCreateAccount;
	@FXML
	private Button btnCreateCancel;

	//Administrative//
	@FXML
	private ComboBox<User> cboxAdminChange;
	@FXML
	private ComboBox<User> cboxAdminReactivate;
	@FXML
	private ComboBox<User> cboxAdminDeactivate;
	@FXML
	private CheckBox cboxAdminAdminStatus;
	@FXML
	private Button btnAdminChangeSave;
	@FXML
	private PasswordField pwdAdminRePwd;
	@FXML
	private PasswordField pwdAdminReConfirm;
	@FXML
	private Button btnAdminReactivate;
	@FXML
	private Button btnAdminDeactivate;
	
	//Stop Management//
	@FXML
	private ComboBox<Stop> cboxStopDelete;
	@FXML
	private Button btnStopDelete;
	@FXML
	private TextField txtStopName;
	@FXML
	private ComboBox<Route> cboxStopRoute;
	@FXML
	private CheckBox cboxStopCreateStudent;
	@FXML
	private Button btnStopCreate;
	@FXML
	private Button btnStopClear;
	@FXML
	private ComboBox<Stop> cboxStopUpdate;
	@FXML
	private CheckBox cboxStopUpdateStudent;
	@FXML
	private Button btnStopUpdateSave;
	
	//Route Management//
	@FXML
	private ComboBox<Route> cboxRouteSelect;
	@FXML
	private ListView<Stop> lviewRouteUnassigned;
	@FXML
	private ListView<Stop> lviewRouteOnRoute;
	@FXML
	private Button btnRouteAdd;
	@FXML
	private Button btnRouteRemove;
	@FXML
	private ComboBox<Route> cboxRouteDelete;
	@FXML
	private Button btnRouteDelete;
	@FXML
	private TextField txtRouteName;
	@FXML
	private Button btnRouteCreate;
	
	//Courier Management//
	@FXML
	private ComboBox<Courier> cboxCourierDelete;
	@FXML
	private Button btnCourierDelete;
	@FXML
	private TextField txtCourierName;
	@FXML
	private Button btnCourierCreate;
	
	//About Tab//
	@FXML
	private Label lblAboutVersion;
	
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
			tabpaneMainPane.getSelectionModel().select(tabAccountManagement);
		}
		
		lblUserId.textProperty().set("User ID: " + String.valueOf(MainFrame.cUser.getUserId()));
		
		cboxAutoUpdate.getItems().clear();
		cboxAutoUpdate.getItems().add(true);
		cboxAutoUpdate.getItems().add(false);
		cboxAutoUpdate.setValue(Boolean.valueOf(prefs.getProperty("AUTOUPDATE")));
		sldrAutoUpdateFreq.valueProperty().set(Double.valueOf(prefs.getProperty("AUFREQ")));
		
		lblAboutVersion.setText("Version: " + prefs.getProperty("VERSION"));
		
		lviewRouteOnRoute.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		lviewRouteUnassigned.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		loadAdminComboBoxes();
		loadRouteComboBoxes();
		loadStopComboBoxes();
		loadCourierComboBoxes();
	}
	
	public void keyPressedAction(KeyEvent ke)
	{
		if(ke.getCode() == KeyCode.ESCAPE)
		{
			btnCancel.fire();
		}
	}
	
	//General Settings//
	public void cboxAutoUpdateAction(ActionEvent ae)
	{
		if(cboxAutoUpdate.getValue())
		{
			sldrAutoUpdateFreq.disableProperty().set(false);
		}
		else
		{
			sldrAutoUpdateFreq.disableProperty().set(true);
		}
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
			MainFrame.properties.setProperty("AUFREQ", sldrAutoUpdateFreq.valueProperty().getValue().toString());
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
			MainFrame.properties.setProperty("AUFREQ", sldrAutoUpdateFreq.valueProperty().getValue().toString());
		}
		
		MainFrame.saveProperties();
		
		MessageDialog.Answer restart = MessageDialogBuilder.info().message("Program Must Be Restart!\nRestart Now?").title("Save Complete").buttonType(MessageDialog.ButtonType.YES_NO).yesOkButtonText("Yes").noButtonText("No").show(MainFrame.stage.getScene().getWindow());
		
		if(restart == MessageDialog.Answer.YES_OK)
		{
	        StringBuilder cmd = new StringBuilder();
	        cmd.append(System.getProperty("java.home") + File.separator + "bin" + File.separator + "java ");
	        for (String jvmArg : ManagementFactory.getRuntimeMXBean().getInputArguments()) 
	        {
	            cmd.append(jvmArg + " ");
	        }
	        cmd.append("-cp ").append(ManagementFactory.getRuntimeMXBean().getClassPath()).append(" ");
	        cmd.append(MainFrame.class.getName()).append(" ");
	        for (String arg : MainFrame.pubArgs)
	        {
	            cmd.append(arg).append(" ");
	        }
	        try
			{
				Runtime.getRuntime().exec(cmd.toString());
			}
			catch (IOException e)
			{
				System.err.println("Error: " + e.getMessage());
			}
	        dbManager.dispose();
	        System.exit(0);
		}
		else
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
	
	public void btnCancelAction(ActionEvent ae)
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
		}
	}
	
	//Account Management//
	public void btnChangePwdAction(ActionEvent ae)
	{
		int oldPassword = 0;
		int newPassword = 0;
		String username = MainFrame.cUser.getUserName();
		String oldPwd = pwdChangePwdOld.getText();
		String newPwd = pwdChangePwdNew.getText();
		String pwdConfirm = pwdChangePwdConfirm.getText();
		boolean verified = true;
		
		if(pwdChangePwdOld.getText().equals("") || pwdChangePwdNew.getText().equals("") || pwdChangePwdConfirm.getText().equals(""))
		{
			MessageDialogBuilder.error().message("Cannot have Empty Fields").title("Error").buttonType(MessageDialog.ButtonType.OK).show(MainFrame.stage.getScene().getWindow());
		}
		else
		{
			if(oldPwd.equals(newPwd))
			{
				MessageDialogBuilder.error().message("New Password cannot be same as Old Password").title("Password Error").buttonType(MessageDialog.ButtonType.OK).show(MainFrame.stage.getScene().getWindow());
				verified = false;
				pwdChangePwdOld.textProperty().set("");
				pwdChangePwdNew.textProperty().set("");
				pwdChangePwdConfirm.textProperty().set("");
			}
			if(!newPwd.equals(pwdConfirm))
			{
				MessageDialogBuilder.error().message("New Password and Confirm Password are not the same").title("Password Error").buttonType(MessageDialog.ButtonType.OK).show(MainFrame.stage.getScene().getWindow());
				verified = false;
				pwdChangePwdNew.textProperty().set("");
				pwdChangePwdConfirm.textProperty().set("");
			}
			
			if(verified)
			{
				oldPassword = (username + oldPwd).hashCode();
				newPassword = (username + newPwd).hashCode();
				
				if(dbManager.changePassword(MainFrame.cUser, oldPassword, newPassword))
				{
					MessageDialogBuilder.info().message("Password Changed Successfully\nYou will now be logged out").title("Success").buttonType(MessageDialog.ButtonType.OK).show(MainFrame.stage.getScene().getWindow());
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
				else
				{
					MessageDialogBuilder.error().message("Password Change Unsuccessful").title("Error").buttonType(MessageDialog.ButtonType.OK).show(MainFrame.stage.getScene().getWindow());
				}
			}
		}
	}
	
	public void btnCreateAccountAction(ActionEvent ae)
	{
		int password;
		String pwd = pwdCreatePwd.getText();
		String pwdConfirm = pwdCreateConfirm.getText();
		if(txtCreateFirstName.getText().equals("") || txtCreateLastName.getText().equals("") || txtCreateUserName.getText().equals("") || pwdCreatePwd.getText().equals("") || pwdCreateConfirm.getText().equals(""))
		{
			MessageDialogBuilder.error().message("Cannot have Empty Fields").title("Error").buttonType(MessageDialog.ButtonType.OK).show(MainFrame.stage.getScene().getWindow());
		}
		else
		{
			if(pwd.equals(pwdConfirm))
			{
				password = (txtCreateUserName.getText() + pwd).hashCode();
				if(dbManager.addUser(new User(-1,txtCreateUserName.getText(),txtCreateFirstName.getText(),txtCreateLastName.getText(),cboxCreateAdmin.selectedProperty().get()), password))
				{
					MessageDialogBuilder.info().message("User " + txtCreateUserName.getText() + " Added").title("Success").buttonType(MessageDialog.ButtonType.OK).show(MainFrame.stage.getScene().getWindow());
					btnCreateCancel.fire();
					loadAdminComboBoxes();
				}
				else
				{
					MessageDialogBuilder.error().message("Error Adding User " + txtCreateUserName.getText()).title("Error").buttonType(MessageDialog.ButtonType.OK).show(MainFrame.stage.getScene().getWindow());
				}
			}
			else
			{
				MessageDialogBuilder.error().message("Passwords Do Not Match").title("Error").buttonType(MessageDialog.ButtonType.OK).show(MainFrame.stage.getScene().getWindow());
				pwdCreatePwd.textProperty().set("");
				pwdCreateConfirm.textProperty().set("");
			}
		}
	}
	
	public void btnCreateCancelAction(ActionEvent ae)
	{
		txtCreateFirstName.textProperty().set("");
		txtCreateLastName.textProperty().set("");
		txtCreateUserName.textProperty().set("");
		pwdCreatePwd.textProperty().set("");
		pwdCreateConfirm.textProperty().set("");
		cboxCreateAdmin.selectedProperty().set(false);
	}
	
	public void btnAdminChangeSaveAction(ActionEvent ae)
	{
		dbManager.setUserAdmin(cboxAdminChange.getValue(), cboxAdminAdminStatus.isSelected());
		loadAdminComboBoxes();
	}
	
	public void btnAdminReactivateAction(ActionEvent ae)
	{
		int password = 0;
		String pwd = pwdAdminRePwd.getText();
		String pwdConfirm = pwdAdminReConfirm.getText();
		
		if(pwd.equals(pwdConfirm))
		{
			password = (cboxAdminReactivate.getValue() + pwd).hashCode();
			if(dbManager.reactivateUser(cboxAdminReactivate.getValue(), password))
			{
				MessageDialogBuilder.info().message("User " + cboxAdminReactivate.getValue() + " reactivated").title("Success").buttonType(MessageDialog.ButtonType.OK).show(MainFrame.stage.getScene().getWindow());
			}
			else
			{
				MessageDialogBuilder.error().message("Error Reactivating User: " + cboxAdminReactivate.getValue()).title("Error").buttonType(MessageDialog.ButtonType.OK).show(MainFrame.stage.getScene().getWindow());
			}
			pwdAdminRePwd.setText("");
			pwdAdminReConfirm.setText("");
			loadAdminComboBoxes();
		}
		else
		{
			MessageDialogBuilder.error().message("Password and Confirm Password do not match").title("Error").buttonType(MessageDialog.ButtonType.OK).show(MainFrame.stage.getScene().getWindow());
			pwdAdminRePwd.setText("");
			pwdAdminReConfirm.setText("");
		}
	}
	
	public void btnAdminDeactivateAction(ActionEvent ae)
	{
		if(!MainFrame.cUser.getUserName().equals(cboxAdminDeactivate.getValue()))
		{
			MessageDialog.Answer del = MessageDialogBuilder.warning().message("Delete " + cboxAdminDeactivate.getValue() + "?").title("Confirm").buttonType(MessageDialog.ButtonType.YES_NO_CANCEL).yesOkButtonText("Yes").noButtonText("No").cancelButtonText("Cancel").show(MainFrame.stage.getScene().getWindow());
		
			if(del == MessageDialog.Answer.YES_OK)
			{
				dbManager.deleteUser(cboxAdminDeactivate.getValue());
				loadAdminComboBoxes();
			}
		}
		else
		{
			MessageDialogBuilder.warning().message("Cannot Deactivate Yourself").title("Error").buttonType(MessageDialog.ButtonType.OK).show(MainFrame.stage.getScene().getWindow());
		}
	}
	
	private void loadAdminComboBoxes()
	{
		cboxAdminChange.itemsProperty().get().clear();
		cboxAdminReactivate.itemsProperty().get().clear();
		cboxAdminDeactivate.itemsProperty().get().clear();
		
		for(User u : dbManager.getActiveUsers())
		{
			cboxAdminChange.itemsProperty().get().add(u);
			cboxAdminDeactivate.itemsProperty().get().add(u);
		}
		for(User u : dbManager.getDeactivatedUsers())
		{
			cboxAdminReactivate.itemsProperty().get().add(u);
		}
		
		cboxAdminChange.setValue(cboxAdminChange.itemsProperty().get().get(0));
		cboxAdminDeactivate.setValue(cboxAdminDeactivate.itemsProperty().get().get(0));
		cboxAdminReactivate.setValue(cboxAdminReactivate.itemsProperty().get().get(0));
	}

	//Stop Management//
	public void btnStopDeleteAction(ActionEvent ae)
	{
		MessageDialog.Answer del = MessageDialogBuilder.confirmation().message("Delete " + cboxStopDelete.getValue() + " Stop").title("Confirm").buttonType(MessageDialog.ButtonType.YES_NO).yesOkButtonText("Yes").noButtonText("No").show(MainFrame.stage.getScene().getWindow());
		
		if(del == MessageDialog.Answer.YES_OK)
		{
			dbManager.deleteStop(cboxStopDelete.getValue());
		}
		
		loadStopComboBoxes();
		loadRouteComboBoxes();
	}
	
	public void btnStopCreateAction(ActionEvent ae)
	{
		if(txtStopName.getText().equals(""))
		{
			MessageDialogBuilder.error().message("Cannot have Empty Stop Name").title("Error").buttonType(MessageDialog.ButtonType.OK).show(MainFrame.stage.getScene().getWindow());
		}
		else
		{
			dbManager.addStop(new Stop(-1,txtStopName.getText(),cboxStopRoute.getValue().getRouteName(),1,cboxStopCreateStudent.isSelected()));
			btnStopClear.fire();
			loadStopComboBoxes();
			loadRouteComboBoxes();
		}
	}
	
	public void btnStopClearAction(ActionEvent ae)
	{
		cboxStopCreateStudent.setSelected(false);
		txtStopName.setText("");
	}
	
	public void btnStopUpdateSaveAction(ActionEvent ae)
	{
		dbManager.updateStop(cboxStopUpdate.getValue());
		loadRouteComboBoxes();
	}
	
	private void loadStopComboBoxes()
	{
		cboxStopDelete.getItems().clear();
		cboxStopUpdate.getItems().clear();
		
		dbManager.loadStops();
		
		for(Stop s : dbManager.getStops())
		{
			if(s.getStopId() != 1)
			{
				cboxStopDelete.getItems().add(s);
				cboxStopUpdate.getItems().add(s);
			}
		}
		
		cboxStopDelete.setValue(cboxStopDelete.itemsProperty().get().get(0));
		cboxStopUpdate.setValue(cboxStopUpdate.itemsProperty().get().get(0));
	}
	
	//Route Management//
	public void cboxRouteSelectAction(ActionEvent ae)
	{		
		loadRouteListViews();
	}
	
	public void btnRouteDeleteAction(ActionEvent ae)
	{
		MessageDialog.Answer del = MessageDialogBuilder.confirmation().message("Delete " + cboxRouteDelete.getValue() + " Route").title("Confirm").buttonType(MessageDialog.ButtonType.YES_NO).yesOkButtonText("Yes").noButtonText("No").show(MainFrame.stage.getScene().getWindow());
		
		if(del == MessageDialog.Answer.YES_OK)
		{
			for(Route r : dbManager.getRoutes())
			{
				if(r.getRouteName().equals(cboxRouteDelete.getValue()))
				{
					dbManager.deleteRoute(r);
					break;
				}
			}
		}
		
		loadRouteComboBoxes();
	}
	
	public void btnRouteCreateAction(ActionEvent ae)
	{
		if(txtRouteName.getText().equals(""))
		{
			MessageDialogBuilder.error().message("Cannot have Empty Route Name").title("Error").buttonType(MessageDialog.ButtonType.OK).show(MainFrame.stage.getScene().getWindow());
		}
		else
		{
			if(dbManager.addRoute(txtRouteName.getText()))
			{
				loadRouteComboBoxes();
				txtRouteName.setText("");
			}
		}
	}
	
	public void btnRouteAddAction(ActionEvent ae)
	{
		List<Stop> selected = lviewRouteUnassigned.selectionModelProperty().getValue().getSelectedItems(); 
		
		for(Stop s : selected)
		{
			dbManager.addStopToRoute(s, cboxRouteSelect.getValue());
		}
		
		loadRouteListViews();
	}
	
	public void btnRouteRemoveAction(ActionEvent ae)
	{
		List<Stop> selected = lviewRouteOnRoute.selectionModelProperty().get().getSelectedItems();
		
		for(Stop s : selected)
		{
			dbManager.addStopToRoute(s, new Route(1,"unassigned"));
		}
		
		loadRouteListViews();
	}
	
	private void loadRouteListViews()
	{
		dbManager.loadStops();
		
		lviewRouteUnassigned.getItems().clear();
		lviewRouteOnRoute.getItems().clear();
		
		for(Stop s : dbManager.getUnassignedStops())
		{
			if(s.getStopId() != 1)
			{
				lviewRouteUnassigned.getItems().add(s);
			}
		}
		
		if(cboxRouteSelect.getValue() != null)
		{
			List<Stop> stops = dbManager.getStopsOnRoute(cboxRouteSelect.getValue());
		
			for(Stop s1 : stops)
			{
				lviewRouteOnRoute.getItems().add(s1);
			}	
		}
		else
		{
			cboxRouteSelect.setValue(cboxRouteSelect.itemsProperty().get().get(0));
		}
	}
	
	private void loadRouteComboBoxes()
	{
		dbManager.loadRoutes();
		
		cboxRouteSelect.getItems().clear();
		cboxRouteDelete.getItems().clear();
		cboxStopRoute.getItems().clear();
		
		for(Route r : dbManager.getRoutes())
		{
			cboxStopRoute.getItems().add(r);
			if(r.getRouteId() != 1)
			{
				cboxRouteSelect.getItems().add(r);
				cboxRouteDelete.getItems().add(r);
			}
		}
		
		cboxRouteSelect.setValue(cboxRouteSelect.itemsProperty().get().get(0));
		cboxRouteDelete.setValue(cboxRouteDelete.itemsProperty().get().get(0));
		cboxStopRoute.setValue(cboxStopRoute.itemsProperty().get().get(0));
	}
	
	//Courier Management//
	public void btnCourierDeleteAction(ActionEvent ae)
	{
		MessageDialog.Answer del = MessageDialogBuilder.confirmation().message("Delete " + cboxCourierDelete.getValue() + " Courier").title("Confirm").buttonType(MessageDialog.ButtonType.YES_NO).yesOkButtonText("Yes").noButtonText("No").show(MainFrame.stage.getScene().getWindow());
		
		if(del == MessageDialog.Answer.YES_OK)
		{
			for(Courier c : dbManager.getCouriers())
			{
				if(c.getCourierName().equals(cboxCourierDelete.getValue()))
				{
					dbManager.deleteCourier(c);
					break;
				}
			}
		}
		
		loadCourierComboBoxes();
	}
	
	public void btnCourierCreateAction(ActionEvent ae)
	{
		if(txtCourierName.getText().equals(""))
		{
			MessageDialogBuilder.error().message("Cannot have Empty Courier Name").title("Error").buttonType(MessageDialog.ButtonType.OK).show(MainFrame.stage.getScene().getWindow());
		}
		else
		{
			dbManager.addCourier(txtCourierName.getText());
			loadCourierComboBoxes();
			txtCourierName.setText("");
		}
	}
	
	private void loadCourierComboBoxes()
	{
		dbManager.loadCouriers();
		cboxCourierDelete.getItems().clear();
		
		for(Courier c : dbManager.getCouriers())
		{
			cboxCourierDelete.getItems().add(c);
		}
		
		cboxCourierDelete.setValue(cboxCourierDelete.itemsProperty().get().get(0));
	}
}