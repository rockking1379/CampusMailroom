package com.mailroom.updater;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class UpdaterMainFrame extends Application
{
	public static Stage stage;
	
	public static void main(String[] args)
	{
		launch(args);
	}

	@SuppressWarnings("static-access")
	@Override
	public void start(Stage stage) throws Exception
	{		
		this.stage = stage;
		this.stage.getIcons().add(new Image(getClass().getResourceAsStream("/com/mailroom/resources/Icon.png")));
		this.stage.setResizable(false);
		this.stage.centerOnScreen();
		Parent root = FXMLLoader.load(getClass().getResource("/com/mailroom/fxml/updater/UpdaterFx.fxml"));
		Scene scene = new Scene(root, 300, 200);
		this.stage.setScene(scene);
		this.stage.setTitle("Updating");
		this.stage.show();
	}
}
