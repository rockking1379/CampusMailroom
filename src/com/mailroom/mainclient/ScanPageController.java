package com.mailroom.mainclient;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import com.mailroom.common.*;

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
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{
		DateFormat format = new SimpleDateFormat("yyy-MM-dd");
		Date now = new Date();
		lblDate.setText(format.format(now).toString());
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

}
