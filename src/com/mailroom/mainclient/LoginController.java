package com.mailroom.mainclient;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import com.mailroom.common.*;
import com.panemu.tiwulfx.*;

public class LoginController implements Initializable 
{
	private DatabaseManager dbManager;
	
	@FXML
	private TextField txtUserName;
	@FXML
	private PasswordField pwdPassword;
	@FXML
	private Label lblLoginError;
	@FXML
	private Button btnLogin;
	@FXML
	private Button btnQuit;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) 
	{
		dbManager = MainFrame.dbManager;
	}
	
	public void btnLoginAction(ActionEvent ae)
	{
		String pwd = pwdPassword.getText();
		int hash = (txtUserName.getText() + pwd).hashCode();
		
		User u = dbManager.login(txtUserName.getText(), hash);
		
		if(u.getUserId() > 0)
		{
			try
			{
				MainFrame.cUser = u;
				Parent root = FXMLLoader.load(getClass().getResource("/com/mailroom/fxml/OpenPageFx.fxml"));
				Scene scene = new Scene(root);
				MainFrame.stage.setScene(scene);
			}
			catch(IOException e)
			{
				System.err.println("Error: " + e.getMessage());
			}
		}
		else
		{
			lblLoginError.setVisible(true);
		}
	}
	
	public void btnQuitAction(ActionEvent ae)
	{
		dbManager.dispose();
		
		System.exit(0);
	}
	
	public void keyPressedAction(KeyEvent ke)
	{
		if(ke.getCode() == KeyCode.ENTER)
		{
			btnLogin.fire();
		}
		if(ke.getCode() == KeyCode.ESCAPE)
		{
			btnQuit.fire();
		}
	}
	
	
}
