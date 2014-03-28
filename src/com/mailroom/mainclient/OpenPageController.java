package com.mailroom.mainclient;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import com.mailroom.common.*;

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

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) 
	{
		cUser = MainFrame.cUser;
		dbManager = MainFrame.dbManager;
		
		String name = "Welcome " + cUser.getFirstName() + " " + cUser.getLastName();
		lblUserLabel.setText(name);
	}

}
