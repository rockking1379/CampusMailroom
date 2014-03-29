package com.mailroom.mainclient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JOptionPane;

import javafx.application.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.stage.*;
import javafx.stage.FileChooser.ExtensionFilter;

import com.mailroom.common.*;

public class MainFrame extends Application
{
	public static DatabaseManager dbManager;
	public static Stage stage;
	public static User cUser;
	
	public static void main(String[] args)
	{
		launch(args);
	}

	@SuppressWarnings("static-access")
	@Override
	public void start(Stage stage) throws Exception 
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
				//temporary//
				switch(JOptionPane.showConfirmDialog(null, "Use SQLite Database?", "Setup", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null))
				{
					case JOptionPane.YES_OPTION:
					{
						propFile.createNewFile();
						
						FileChooser fChooser = new FileChooser();
						fChooser.setSelectedExtensionFilter(new ExtensionFilter("Database", ".db"));
						fChooser.setTitle("Select Database File");
						
						FileOutputStream oStream = new FileOutputStream(propFile);
						
						properties.setProperty("SQLITE", "true");
						properties.setProperty("MYSQL", "false");
						File dbFile = fChooser.showOpenDialog(stage);
						properties.setProperty("DATABASE", dbFile.getAbsolutePath());
						properties.store(oStream, "No Comments");
						oStream.close();
						
						dbManager = new SQLiteManager(dbFile.getAbsolutePath());
						
						break;
					}
					case JOptionPane.NO_OPTION:
					{
						//load new UI for setting up MYSQL
						break;
					}
					default:
					{
						break;
					}
				}
			}
		}
		catch(IOException | ConfigException e)
		{
			System.err.println("Error: " + e.getMessage());
			dbManager = new SQLiteManager(":memory:");
		}
		
		this.stage = stage;
		Parent root = FXMLLoader.load(getClass().getResource("../fxml/LoginFx.fxml"));
		Scene scene = new Scene(root, 1024, 768);
		
		this.stage.setScene(scene);
		this.stage.setTitle("Login");
		this.stage.show();
	}
}
