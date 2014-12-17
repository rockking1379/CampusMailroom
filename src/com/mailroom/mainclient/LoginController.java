package com.mailroom.mainclient;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import com.mailroom.common.*;

/**
 * Controls LoginFx.fxml in com.mailroom.fxml.mainclient
 * @author James sitzja@grizzlies.adams.edu
 */
public class LoginController implements Initializable
{
	private DatabaseManager dbManager;

	@FXML
	private AnchorPane anchorPane;
	@FXML
	public ImageView imgLogo;
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

		imgLogo.setImage(MainFrame.imageLogo);
	}

	/**
	 * Performs Login Action
	 * @param ae ActionEvetn from OS
	 */
	public void btnLoginAction(ActionEvent ae)
	{
		String pwd = pwdPassword.getText();
		int hash = txtUserName.getText().hashCode() + pwd.hashCode();

		User u = dbManager.login(txtUserName.getText(), hash);

		if (u.getUserId() > 0 && u != null)
		{
			try
			{
				MainFrame.cUser = u;
				Parent root = FXMLLoader.load(getClass().getResource(
						"/com/mailroom/fxml/mainclient/OpenPageFx.fxml"));
				Scene scene = new Scene(root);
				MainFrame.stage.setScene(scene);
			}
			catch (IOException e)
			{
				Logger.log(e);
			}
		}
		else
		{
			lblLoginError.setVisible(true);
		}
	}

	/**
	 * Quits Application
	 * @param ae ActionEvent from OS
	 */
	public void btnQuitAction(ActionEvent ae)
	{
		MainFrame.saveProperties();

		dbManager.dispose();

		System.exit(0);
	}

	/**
	 * Processes keyboard input while on login screen
	 * @param ke KeyEvent from OS
	 */
	public void keyPressedAction(KeyEvent ke)
	{
		if (ke.getCode() == KeyCode.ENTER)
		{
			btnLogin.fire();
		}
		if (ke.getCode() == KeyCode.ESCAPE)
		{
			btnQuit.fire();
		}
	}

}
