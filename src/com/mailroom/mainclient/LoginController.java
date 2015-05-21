package com.mailroom.mainclient;

import com.mailroom.common.DatabaseManager;
import com.mailroom.common.Logger;
import com.mailroom.common.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;

/**
 * Controls LoginFx.fxml in com.mailroom.fxml.mainclient
 *
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
     *
     * @param ae ActionEvetn from OS
     */
    public void btnLoginAction(ActionEvent ae)
    {
        ae.consume();

        String pwd = pwdPassword.getText();
        int hash = txtUserName.getText().hashCode() + pwd.hashCode();

        User u = dbManager.login(txtUserName.getText(), hash);

        if(u.getUserId() < 0)
        {
            try
            {
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] pwdOutput = digest.digest(pwd.getBytes());
                byte[] userOutput = digest.digest(txtUserName.getText().getBytes());
                String combine = new HexBinaryAdapter().marshal(pwdOutput) + new HexBinaryAdapter().marshal(userOutput);
                byte[] byteHash = digest.digest(combine.getBytes());

                u = dbManager.login(txtUserName.getText(), byteHash);
            }
            catch(NoSuchAlgorithmException nsae)
            {
                Logger.logException(nsae);
            }
        }
        else
        {
            try
            {
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] pwdOutput = digest.digest(pwd.getBytes());
                byte[] userOutput = digest.digest(txtUserName.getText().getBytes());
                String combine = new HexBinaryAdapter().marshal(pwdOutput) + new HexBinaryAdapter().marshal(userOutput);
                byte[] byteHash = digest.digest(combine.getBytes());

                dbManager.changePassword(u, hash, byteHash);
            }
            catch(NoSuchAlgorithmException nsae)
            {
                Logger.logException(nsae);
            }
        }

        if (u.getUserId() > 0)
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
                Logger.logException(e);
            }
        }
        else
        {
            lblLoginError.setVisible(true);
        }
    }

    /**
     * Quits Application
     *
     * @param ae ActionEvent from OS
     */
    public void btnQuitAction(ActionEvent ae)
    {
        ae.consume();

        MainFrame.saveProperties();

        dbManager.dispose();

        System.exit(0);
    }

    /**
     * Processes keyboard input while on login screen
     *
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
