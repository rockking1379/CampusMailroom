package com.mailroom.mainclient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javafx.application.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.stage.*;

import com.mailroom.common.*;
import com.panemu.tiwulfx.dialog.*;

/**
 * Main Entry Point for MainClient
 * 
 * @author James
 */
public class MainFrame extends Application
{
	/**
	 * Database Manaager for Instance of Main Client
	 */
	public static DatabaseManager dbManager;
	/**
	 * Sate for Instance of Main Client
	 */
	public static Stage stage;
	/**
	 * Currently Logged In User
	 */
	public static User cUser;
	/**
	 * Software Configuration Properties
	 */
	public static Properties properties = null;
	/**
	 * Logo Image to be displayed in Login
	 */
	public static Image imageLogo;
	/**
	 * Command Line arguments
	 * <br>
	 * Used to Restart in Settings
	 */
	public static String[] pubArgs;
	
	/**
	 * Main Entry Point for Main Client
	 * @param args Command Line Arguments
	 */
	public static void main(String[] args)
	{
		pubArgs = args;
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
				
				switch(Integer.valueOf(properties.getProperty("DBTYPE")))
				{
					case SQLiteManager.dbId:
					{
						dbManager = new SQLiteManager(properties.getProperty("DATABASE"));
						break;
					}
					case MysqlManager.dbId:
					{
						dbManager = new MysqlManager(properties.getProperty("DATABASE"), properties.getProperty("USERNAME"), properties.getProperty("PASSWORD"), properties.getProperty("DBNAME"));
						break;
					}
					case PostgreSQLManager.dbId:
					{
						dbManager = new PostgreSQLManager(properties.getProperty("DATABASE"), properties.getProperty("USERNAME"), properties.getProperty("PASSWORD"), properties.getProperty("DBNAME"));
						break;
					}
					default:
					{
						throw new ConfigException("Configuration Error\nUnknown Database Type");
					}
				}
				
				file.close();
			}
			else
			{
				//temporary//	
				MessageDialog.Answer create = MessageDialogBuilder.confirmation().message("Create New Database?").title("Setup").buttonType(MessageDialog.ButtonType.YES_NO).yesOkButtonText("Yes").noButtonText("No").show(null);
				
				if(create == MessageDialog.Answer.YES_OK)
				{
					MessageDialog.Answer sqlite = MessageDialogBuilder.confirmation().message("Use SQLite Database?").title("Setup").buttonType(MessageDialog.ButtonType.YES_NO).yesOkButtonText("Yes").noButtonText("No").show(null);
					
					if(sqlite == MessageDialog.Answer.YES_OK)
					{
						propFile.createNewFile();
						
						FileChooser fChooser = new FileChooser();
						fChooser.setTitle("Select Database File");
						
						FileOutputStream oStream = new FileOutputStream(propFile);
						
						properties.setProperty("SQLITE", Boolean.toString(true));
						properties.setProperty("MYSQL", Boolean.toString(false));
						properties.setProperty("AUTOUPDATE", Boolean.toString(false));
						File dbFile = fChooser.showOpenDialog(stage);
						properties.setProperty("DATABASE", dbFile.getAbsolutePath());
						properties.setProperty("AUFREQ", "10.0");
						properties.store(oStream, "System Configuration");
						oStream.close();
						
						dbManager = new SQLiteManager(dbFile.getAbsolutePath());
					}
					else
					{
						//load new UI for setting up MYSQL
						//TO BE DONE LATER
					}
				}
				else
				{
					//Maybe ask to be pointed towards the configuration file (might be an old one from previous setup)
					//Then on exit, save them in working directory???
				}
			}
		}
		catch(IOException e)
		{
			Logger.log(e);
			System.exit(-1);
		}
		catch(ConfigException e)
		{
			Logger.log(e);
			MessageDialog.Answer a = MessageDialogBuilder.error().message(e.getMessage() + "\nRevert to SQLite Database Configuration?").buttonType(MessageDialog.ButtonType.YES_NO).yesOkButtonText("Yes").show(null);
			if(a == MessageDialog.Answer.YES_OK)
			{
				properties.setProperty("MYSQL", Boolean.toString(false));
				saveProperties();
				dbManager = new SQLiteManager(properties.getProperty("DATABASE"));
			}
			else
			{
				MessageDialog.Answer b = MessageDialogBuilder.warning().message("Bad Configuration\nSystem will now Exit").buttonType(MessageDialog.ButtonType.OK).show(null);
				if(b == MessageDialog.Answer.YES_OK)
				{
					System.exit(-1);
				}
				else
				{
					System.exit(-1);
				}
			}
		}
		
		this.imageLogo = new Image(getClass().getResourceAsStream("/com/mailroom/resources/Logo.png"));
		this.stage = stage;
		this.stage.getIcons().add(new Image(getClass().getResourceAsStream("/com/mailroom/resources/Icon.png")));
		this.stage.setResizable(false);
		this.stage.centerOnScreen();
		Parent root = FXMLLoader.load(getClass().getResource("/com/mailroom/fxml/mainclient/LoginFx.fxml"));
		Scene scene = new Scene(root, 800, 600);
		this.stage.setScene(scene);
		this.stage.setTitle("Login");
		this.stage.show();
	}
	
	/**
	 * Saves Properties out to file
	 * <br>
	 * Called from various locations throughout Main Client
	 */
	public static void saveProperties()
	{
		if(properties != null)
		{
			try
			{
				File propFile = new File("./configuration.properties");
				if(!propFile.exists())
				{
					propFile.createNewFile();
				}
				FileOutputStream oStream = new FileOutputStream(propFile);
				properties.store(oStream, "System Configuration");
				oStream.close();
			}
			catch(IOException e)
			{
				Logger.log(e);
			}
		}
	}
}
