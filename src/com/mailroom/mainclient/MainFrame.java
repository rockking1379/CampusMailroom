package com.mailroom.mainclient;

import java.util.prefs.Preferences;

import javafx.application.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.stage.*;
import com.mailroom.common.*;

public class MainFrame extends Application
{
	public static DatabaseManager dbManager;
	public static Stage stage;
	public static User cUser;
	
	public static void main(String[] args)
	{
		//read settings file
		
		dbManager = new SQLiteManager("F:/owncloud/sql/mailroom/test.db");
		launch(args);
	}

	@SuppressWarnings("static-access")
	@Override
	public void start(Stage stage) throws Exception 
	{
		this.stage = stage;
		Parent root = FXMLLoader.load(getClass().getResource("/com/mailroom/fxml/LoginFx.fxml"));
		Scene scene = new Scene(root, 1024, 768);
		
		this.stage.setScene(scene);
		this.stage.setTitle("Login");
		this.stage.show();
	}
}
