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
		Preferences prefs = Preferences.userRoot().node("/com/mailroom/config/configuration.conf");		
		
		if(prefs.getBoolean("SQLite", true))
		{
			dbManager = new SQLiteManager(prefs.get("DatabaseLocation", null));
		}
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
