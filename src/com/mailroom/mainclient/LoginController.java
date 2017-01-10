package com.mailroom.mainclient;

import com.google.zxing.Result;
import com.mailroom.common.database.DatabaseManager;
import com.mailroom.common.factories.DatabaseManagerFactory;
import com.mailroom.common.objects.DbUser;
import com.mailroom.common.utils.Logger;
import com.mailroom.common.utils.UpdateChecker;
import com.mailroom.common.utils.WebCamera;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
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
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;

/**
 * Controls LoginFx.fxml in com.mailroom.fxml.mainclient
 *
 * @author James rockking1379@gmail.com
 */
public class LoginController implements Initializable
{
    private DatabaseManager dbManager;

    @FXML
    public ImageView imgLogo;
    @FXML
    public ImageView imgCameraPreview;
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
        Logger.logEvent("Loading Login Screen", "SYSTEM");
        dbManager = DatabaseManagerFactory.getInstance();
        Platform.runLater(new UpdateChecker(MainFrame.stage));
        imgLogo.setImage(MainFrame.imageLogo);

        if(WebCamera.getInstance().getCamera() == null)
        {
            imgCameraPreview.setVisible(false);
        }
        else
        {
            new AutoLogin();
        }
    }

    /**
     * Performs Login Action
     *
     * @param ae ActionEvetn from OS
     */
    public void btnLoginAction(ActionEvent ae)
    {
        ae.consume();
        Logger.logEvent("Login Requested", "SYSTEM");

        String pwd = pwdPassword.getText();
        int hash = txtUserName.getText().hashCode() + pwd.hashCode();

        Logger.logEvent("Attempting Login Using Old Hashing", txtUserName.getText());
        DbUser u = dbManager.login(txtUserName.getText(), hash);

        if (u.getUserId() < 0)
        {
            Logger.logEvent("Attempting Login Using New Hashing", txtUserName.getText());
            try
            {
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] pwdOutput = digest.digest(pwd.getBytes());
                byte[] userOutput = digest.digest(txtUserName.getText().getBytes());
                String combine = new HexBinaryAdapter().marshal(pwdOutput) + new HexBinaryAdapter().marshal(userOutput);
                byte[] byteHash = digest.digest(combine.getBytes());

                u = dbManager.login(txtUserName.getText(), byteHash);
            }
            catch (NoSuchAlgorithmException nsae)
            {
                Logger.logException(nsae);
            }
        }
        else
        {
            Logger.logEvent("Switching UserName Hashing", txtUserName.getText());
            try
            {
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] pwdOutput = digest.digest(pwd.getBytes());
                byte[] userOutput = digest.digest(txtUserName.getText().getBytes());
                String combine = new HexBinaryAdapter().marshal(pwdOutput) + new HexBinaryAdapter().marshal(userOutput);
                byte[] byteHash = digest.digest(combine.getBytes());

                dbManager.changePassword(u, hash, byteHash);
            }
            catch (NoSuchAlgorithmException nsae)
            {
                Logger.logException(nsae);
            }
        }

        if (u.getUserId() > 0)
        {
            Logger.logEvent("DbUser Login Successful", u.getUserName());
            try
            {
                MainFrame.cDbUser = u;
                Parent root = FXMLLoader.load(getClass().getResource(
                        "/com/mailroom/fxml/mainclient/OpenPageFx.fxml"));
                Scene scene = new Scene(root);
                scene.getStylesheets().add(getClass().getResource("/com/mailroom/resources/default.css").toString());
                imgCameraPreview.setVisible(false);
                MainFrame.stage.setScene(scene);
            }
            catch (IOException e)
            {
                Logger.logException(e);
            }
        }
        else
        {
            Logger.logEvent("DbUser Login Failed", txtUserName.getText());
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
        Logger.logEvent("System Exit Requested", "SYSTEM");
        MainFrame.saveProperties();

        imgCameraPreview.setVisible(false);

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

    private class AutoLogin implements Runnable
    {
        public AutoLogin()
        {
            new Thread(this).start();
        }

        @Override
        public void run()
        {
            while(imgCameraPreview.isVisible())
            {
                imgCameraPreview.setImage(SwingFXUtils.toFXImage(WebCamera.getInstance().getCamera().getImage(), null));

                Result result = WebCamera.getInstance().getResult();

                if(result != null)
                {
                    //attempt processing
                    JSONObject obj = (JSONObject) JSONValue.parse(result.getText());

                    txtUserName.setText(obj.get("username").toString());
                    pwdPassword.setText(obj.get("password").toString());

                    //well we got some data so...lets generate the enter key to login
                    try
                    {
                        Robot myRobot = new Robot();
                        myRobot.keyPress(java.awt.event.KeyEvent.VK_ENTER);
                        myRobot.keyRelease(java.awt.event.KeyEvent.VK_ENTER);
                    }
                    catch(AWTException aex)
                    {
                        Logger.logException(aex);
                    }
                }
            }
        }
    }
}