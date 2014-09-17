package com.mailroom.mainclient;

import java.io.IOException;

import javafx.fxml.*;
import javafx.scene.*;
import javafx.stage.*;

import com.mailroom.common.*;
import com.mailroom.common.Package;
import com.mailroom.otherclient.OtherMainFrame;

/**
 * Used for Editing Package Data that has already
 * been saved in the database
 * 
 * @author James sitzja@grizzlies.adams.edu
 */
public class PackageEditWindow extends Window
{
	Stage stg;
	static Package curPackage;
	static Window win;
	
	/**
	 * Constructs new Edit Window for a Package
	 */
	public PackageEditWindow()
	{
		stg = new Stage();
		stg.setResizable(false);
		win = this;
	}
	
	/**
	 * Shows the Window with old Package data
	 * @param p Package to be Shown
	 */
	public void show(Package p)
	{
		curPackage = p;
		
		try 
		{
			Parent root;
			root = FXMLLoader.load(getClass().getResource("/com/mailroom/fxml/mainclient/PackageEditFx.fxml"));
			Scene scene = new Scene(root, 256, 600);
			stg.setScene(scene);
			
			if(MainFrame.stage != null)
			{
				MainFrame.stage.setX(MainFrame.stage.getX() - stg.getScene().getWindow().getWidth());
				
				stg.show();
				stg.setX(MainFrame.stage.getX() + MainFrame.stage.getScene().getWindow().getWidth());
				stg.setY(MainFrame.stage.getY());
			}
			if(OtherMainFrame.stage != null)
			{
				OtherMainFrame.stage.setX(OtherMainFrame.stage.getX() - stg.getScene().getWindow().getWidth());
				
				stg.show();
				stg.setX(OtherMainFrame.stage.getX() + OtherMainFrame.stage.getScene().getWindow().getWidth());
				stg.setY(OtherMainFrame.stage.getY());
			}
		} 
		catch (IOException e) 
		{
			Logger.log(e);
		}
	}
	
	/**
	 * Hided the Window
	 */
	public void hide()
	{
		if(MainFrame.stage != null)
		{
			MainFrame.stage.centerOnScreen();
		}
		if(OtherMainFrame.stage != null)
		{
			OtherMainFrame.stage.centerOnScreen();
		}
		
		stg.hide();
	}
	
	public static Package getPackage()
	{
		return curPackage;
	}
	public static Window getWindow()
	{
		return win;
	}
}
