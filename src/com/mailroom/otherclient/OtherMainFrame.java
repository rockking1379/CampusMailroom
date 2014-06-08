package com.mailroom.otherclient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import com.mailroom.common.ConfigException;
import com.mailroom.common.DatabaseManager;
import com.mailroom.common.SQLiteManager;
import com.panemu.tiwulfx.dialog.MessageDialog;
import com.panemu.tiwulfx.dialog.MessageDialogBuilder;

public class OtherMainFrame extends Application
{
	public static DatabaseManager dbManager;
	public static Stage stage;
	public static String[] pubArgs;
	public static Properties properties;
	
	public static void main(String[] args)
	{
		pubArgs = args;
		launch(args);
	}
	
	@SuppressWarnings("static-access")
	@Override
	public void start(Stage stage) throws Exception
	{
		try
		{
			properties = new Properties();
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
				MessageDialogBuilder.error().message("No Config Found!\nPlease Run Other Program First").buttonType(MessageDialog.ButtonType.OK).show(null);
				System.exit(-1);
			}
		}
		catch(IOException e)
		{
			System.err.println("Error: " + e.getMessage());
			System.exit(-1);
		}
		catch(ConfigException e)
		{
			System.err.println("Error: " + e.getMessage());
		}
		
		this.stage = stage;
		this.stage.getIcons().add(new Image(getClass().getResourceAsStream("/com/mailroom/resources/Icon.png")));
		this.stage.setResizable(false);
		this.stage.centerOnScreen();
		Parent root = FXMLLoader.load(getClass().getResource("/com/mailroom/fxml/otherclient/MainPageFx.fxml"));
		Scene scene = new Scene(root, 800, 600);
		this.stage.setScene(scene);
		this.stage.setTitle("Main Page");
		this.stage.show();
	}
}
