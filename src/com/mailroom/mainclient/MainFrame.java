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
import javafx.scene.image.Image;
import javafx.stage.*;
import javafx.stage.FileChooser.ExtensionFilter;

import com.mailroom.common.*;
import com.panemu.tiwulfx.dialog.*;

public class MainFrame extends Application
{
	public static DatabaseManager dbManager;
	public static Stage stage;
	public static User cUser;
	public static Properties properties = null;
	public static Image imageLogo;
	
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
			properties = new Properties();
			File propFile = new File("./configuration.properties");
			
			if(propFile.exists())
			{
				FileInputStream file = new FileInputStream(propFile);
				properties.load(file);
				
//				for(String key : properties.stringPropertyNames())
//				{
//					String value = properties.getProperty(key);
//					System.out.println(key + "=>" + value);
//				}
				
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
						fChooser.setSelectedExtensionFilter(new ExtensionFilter("Database", "*.db"));
						fChooser.setTitle("Select Database File");
						
						FileOutputStream oStream = new FileOutputStream(propFile);
						
						properties.setProperty("SQLITE", Boolean.toString(true));
						properties.setProperty("MYSQL", Boolean.toString(false));
						properties.setProperty("AUTOUPDATE", Boolean.toString(false));
						File dbFile = fChooser.showOpenDialog(stage);
						properties.setProperty("DATABASE", dbFile.getAbsolutePath());
						properties.store(oStream, "System Configuration");
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
		catch(IOException e)
		{
			System.err.println("ERROR: " + e.getMessage());
			System.exit(-1);
		}
		catch(ConfigException e)
		{
			System.err.println("Error: " + e.getMessage());
			MessageDialog.Answer a = MessageDialogBuilder.error().message(e.getMessage() + "\nRevert to local Database Configuration?").buttonType(MessageDialog.ButtonType.YES_NO).yesOkButtonText("Yes").show(null);
			if(a == MessageDialog.Answer.YES_OK)
			{
				properties.setProperty("MYSQL", Boolean.toString(false));
				saveProperties();
				dbManager = new SQLiteManager(properties.getProperty("DATABASE"));
			}
			else
			{
				MessageDialog.Answer b = MessageDialogBuilder.warning().message("System will now Exit").buttonType(MessageDialog.ButtonType.OK).show(null);
				System.exit(-1);
			}
		}
		
		this.imageLogo = new Image(getClass().getResourceAsStream("/com/mailroom/resources/Logo.jpg"));
		
		this.stage = stage;
		this.stage.setResizable(false);
		this.stage.centerOnScreen();
		Parent root = FXMLLoader.load(getClass().getResource("/com/mailroom/fxml/mainclient/LoginFx.fxml"));
		Scene scene = new Scene(root, 800, 600);
		this.stage.setScene(scene);
		this.stage.setTitle("Login");
		this.stage.show();
	}
	
	public static void saveProperties()
	{
		if(properties != null)
		{
			try
			{
				File propFile = new File("./configuration.properties");
				FileOutputStream oStream = new FileOutputStream(propFile);
				properties.store(oStream, "System Configuration");
				oStream.close();
			}
			catch(IOException e)
			{
				System.err.println("Error: " + e.getMessage());
			}
		}
	}
}
