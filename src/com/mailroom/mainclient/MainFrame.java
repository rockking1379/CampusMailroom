package com.mailroom.mainclient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
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
		try
		{
			Properties properties = new Properties();
			File propFile = new File("./configuration.properties");
			
			if(propFile.exists())
			{
				FileInputStream file = new FileInputStream(propFile);
				properties.load(file);
				
				for(String key : properties.stringPropertyNames())
				{
					String value = properties.getProperty(key);
					System.out.println(key + "=>" + value);
				}
				
				boolean sqlite = Boolean.valueOf(properties.getProperty("SQLITE"));
				boolean mysql = Boolean.valueOf(properties.getProperty("MYSQL"));
				
				if(sqlite && !mysql)
				{
					dbManager = new SQLiteManager(properties.getProperty("DATABASE"));
				}
				if(mysql && !sqlite)
				{
					//create DBM with mysql info
				}
				if(mysql && sqlite)
				{
					throw new ConfigException("Invalid Database Configuration");
				}
				
				file.close();
			}
			else
			{
				propFile.createNewFile();
				properties.setProperty("SQLITE", "true");
				properties.setProperty("MYSQL", "false");
				properties.setProperty("DATABASE", "F:\\owncloud\\sql\\mailroom\\test.db");
			}
		}
		catch(IOException | ConfigException e)
		{
			System.err.println("Error: " + e.getMessage());
			dbManager = new SQLiteManager(":memory:");
		}
		launch(args);
	}

	@SuppressWarnings("static-access")
	@Override
	public void start(Stage stage) throws Exception 
	{
		this.stage = stage;
		Parent root = FXMLLoader.load(getClass().getResource("../fxml/LoginFx.fxml"));
		Scene scene = new Scene(root, 1024, 768);
		
		this.stage.setScene(scene);
		this.stage.setTitle("Login");
		this.stage.show();
	}
}
