package com.mailroom.mainclient;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import com.mailroom.common.Package;

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
	
	private DatabaseManager dbManager;
	private User cUser;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{
		DateFormat format = new SimpleDateFormat("yyy-MM-dd");
		Date now = new Date();
		lblDate.setText(format.format(now).toString());
		
		cUser = MainFrame.cUser;
		dbManager = MainFrame.dbManager;
	}
	
	public void btnExitAction(ActionEvent ae)
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
	
	public void btnClearAction(ActionEvent ae)
	{
		txtTrackingNumber.setText("");
		txtFirstName.setText("");
		txtLastName.setText("");
		txtBoxOffice.setText("");
		txtEmailAddress.setText("");
		cboxStops.setValue(cboxStops.getItems().get(0));
		cboxCourier.setValue(cboxCourier.getItems().get(0));
	}
	
	public void btnSaveAction(ActionEvent ae)
	{
		if(txtTrackingNumber.getText().length() < 4)
		{
			JOptionPane.showMessageDialog(null, "Tracking Number Not Long Enough", "Error", JOptionPane.ERROR_MESSAGE);
		}
		if(txtFirstName.getText() == "" && txtLastName.getText() == "")
		{
			txtFirstName.setText("DEPARTMENT");
			txtLastName.setText("DEPARTMENT");
		}
		else
		{
			if(txtFirstName.getText() == "" && txtLastName.getText() != "")
			{
				JOptionPane.showMessageDialog(null, "No Last Name Specified", "Error", JOptionPane.ERROR_MESSAGE);
			}
			else
			{
				if(txtFirstName.getText() != "" && txtLastName.getText() == "")
				{
					JOptionPane.showMessageDialog(null, "No First Name Specified", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
		
		if(txtBoxOffice.getText() == "")
		{
			JOptionPane.showMessageDialog(null, "No Box/Suite Number Set", "Error", JOptionPane.ERROR_MESSAGE);
		}
		
		if(txtEmailAddress.getText() == "")
		{
			txtEmailAddress.setText("unknown@");
		}
		
		Package p = new Package(-1, txtTrackingNumber.getText(), lblDate.getText(), txtEmailAddress.getText(), txtFirstName.getText(), txtLastName.getText(), txtBoxOffice.getText(), cboxStops.getValue().getStopName(), cboxCourier.getValue().getCourierName(), cUser.getUserName(), false, false, null, false);
		dbManager.addPackage(p, cUser.getUserId());
	}

	public void keyPressAction(KeyEvent ke)
	{
		if(ke.getCode() == KeyCode.ESCAPE)
		{
			btnExit.fire();
		}
		if(ke.getCode() == KeyCode.C)
		{
			btnClear.fire();
		}
		if(ke.getCode() == KeyCode.ENTER)
		{
			if(txtBoxOffice.focusedProperty().get())
			{
				//auto complete
			}
			else
			{
				btnSave.fire();
			}
		}
	}
}
