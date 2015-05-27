package com.mailroom.mainclient;

import com.mailroom.common.database.DatabaseManager;
import com.mailroom.common.database.MysqlManager;
import com.mailroom.common.database.PostgreSQLManager;
import com.mailroom.common.database.SQLiteManager;
import com.mailroom.common.objects.Courier;
import com.mailroom.common.objects.Route;
import com.mailroom.common.objects.Stop;
import com.mailroom.common.objects.User;
import com.mailroom.common.utils.Logger;
import com.panemu.tiwulfx.dialog.MessageDialog;
import com.panemu.tiwulfx.dialog.MessageDialogBuilder;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * Controls SettingsPageFx.fxml in com.mailroom.fxml.mainclient
 *
 * @author James rockking1379@gmail.com
 */
public class SettingsController implements Initializable
{
    // Tab Views//
    @FXML
    private Tab tabGeneral;
    @FXML
    private Tab tabStopManagement;
    @FXML
    private Tab tabRouteManagement;
    @FXML
    private Tab tabCourierManagement;
    @FXML
    private Tab tabEmailManagement;
    @FXML
    private Tab tabAbout;
    @FXML
    private TabPane tabpaneMainPane;

    // General Settings//
    @FXML
    private ComboBox<String> cboxDatabaseType;
    @FXML
    private TextField txtDatabaseLocation;
    @FXML
    private TextField txtDatabaseName;
    @FXML
    private TextField txtDatabaseUserName;
    @FXML
    private PasswordField pwdDatabasePassword;
    @FXML
    private Button btnBrowse;
    @FXML
    private CheckBox cboxGeneralEmailEnabled;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnCancel;

    // Account Management//
    @FXML
    private Tab tabAccountManagement;
    @FXML
    private Tab tabEditAccount;
    @FXML
    private Tab tabCreateAccount;
    @FXML
    private Tab tabAdministrative;

    // Edit Account//
    @FXML
    private Label lblUserId;
    @FXML
    private PasswordField pwdChangePwdOld;
    @FXML
    private PasswordField pwdChangePwdNew;
    @FXML
    private PasswordField pwdChangePwdConfirm;
    @FXML
    private Button btnChangePwd;

    // Create Account//
    @FXML
    private TextField txtCreateFirstName;
    @FXML
    private TextField txtCreateLastName;
    @FXML
    private TextField txtCreateUserName;
    @FXML
    private PasswordField pwdCreatePwd;
    @FXML
    private PasswordField pwdCreateConfirm;
    @FXML
    private CheckBox cboxCreateAdmin;
    @FXML
    private Button btnCreateAccount;
    @FXML
    private Button btnCreateCancel;

    // Administrative//
    @FXML
    private ComboBox<User> cboxAdminChange;
    @FXML
    private ComboBox<User> cboxAdminReactivate;
    @FXML
    private ComboBox<User> cboxAdminDeactivate;
    @FXML
    private CheckBox cboxAdminAdminStatus;
    @FXML
    private Button btnAdminChangeSave;
    @FXML
    private PasswordField pwdAdminRePwd;
    @FXML
    private PasswordField pwdAdminReConfirm;
    @FXML
    private Button btnAdminReactivate;
    @FXML
    private Button btnAdminDeactivate;

    // Stop Management//
    @FXML
    private ComboBox<Stop> cboxStopDelete;
    @FXML
    private Button btnStopDelete;
    @FXML
    private TextField txtStopName;
    @FXML
    private ComboBox<Route> cboxStopRoute;
    @FXML
    private CheckBox cboxStopCreateStudent;
    @FXML
    private Button btnStopCreate;
    @FXML
    private Button btnStopClear;
    @FXML
    private ComboBox<Stop> cboxStopUpdate;
    @FXML
    private CheckBox cboxStopUpdateStudent;
    @FXML
    private CheckBox cboxStopUpdateAutoRemove;
    @FXML
    private Button btnStopUpdateSave;

    // Route Management//
    @FXML
    private ComboBox<Route> cboxRouteSelect;
    @FXML
    private ListView<Stop> lviewRouteUnassigned;
    @FXML
    private ListView<Stop> lviewRouteOnRoute;
    @FXML
    private Button btnRouteAdd;
    @FXML
    private Button btnRouteRemove;
    @FXML
    private ComboBox<Route> cboxRouteDelete;
    @FXML
    private Button btnRouteDelete;
    @FXML
    private TextField txtRouteName;
    @FXML
    private Button btnRouteCreate;
    @FXML
    private ComboBox<Route> cboxDesignRoute;
    @FXML
    private ListView<Stop> lviewDesignStops;
    @FXML
    private Button btnDesignUp;
    @FXML
    private Button btnDesignFirst;
    @FXML
    private Button btnDesignLast;
    @FXML
    private Button btnDesignDown;

    // Courier Management//
    @FXML
    private ComboBox<Courier> cboxCourierDelete;
    @FXML
    private Button btnCourierDelete;
    @FXML
    private TextField txtCourierName;
    @FXML
    private Button btnCourierCreate;

    // Email Management //

    // Message //
    @FXML
    private TextField txtEmailMessageReplyTo;
    @FXML
    private ComboBox<String> cboxEmailMessageSendTime;
    @FXML
    private TextArea txtEmailMessageContent;
    @FXML
    private Button btnEmailMessageSave;

    // Sending //
    @FXML
    private TextField txtEmailSendingHost;
    @FXML
    private TextField txtEmailSendingPort;
    @FXML
    private ComboBox<Boolean> cboxEmailSendingAuthReq;
    @FXML
    private TextField txtEmailSendingAuthUserName;
    @FXML
    private TextField txtEmailSendingAuthPassword;
    @FXML
    private Button btnEmailSendingSave;

    // Addresses //
    @FXML
    private ComboBox<Stop> cboxEmailAddressStop;
    @FXML
    private ListView<String> lviewEmailAddresses;
    @FXML
    private Button btnEmailAddressRemove;
    @FXML
    private Button btnEmailAddressAdd;
    @FXML
    private TextField txtEmailAddressAdd;

    // About Tab//
    @FXML
    private Label lblAboutVersion;

    private DatabaseManager dbManager;
    private Properties prefs;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        MainFrame.stage.setTitle("Settings");

        this.dbManager = MainFrame.dbManager;
        this.prefs = MainFrame.properties;

        cboxDatabaseType.getItems().clear();

        switch (Integer.valueOf(prefs.getProperty("DBTYPE")))
        {
            case SQLiteManager.dbId:
            {
                cboxDatabaseType.getItems().add(SQLiteManager.dbName);
                cboxDatabaseType.getItems().add(MysqlManager.dbName);
                cboxDatabaseType.getItems().add(PostgreSQLManager.dbName);

                cboxDatabaseType.setValue(cboxDatabaseType.getItems().get(0));

                break;
            }
            case MysqlManager.dbId:
            {
                cboxDatabaseType.getItems().add(MysqlManager.dbName);
                cboxDatabaseType.getItems().add(SQLiteManager.dbName);
                cboxDatabaseType.getItems().add(PostgreSQLManager.dbName);

                cboxDatabaseType.setValue(cboxDatabaseType.getItems().get(0));

                break;
            }
            case PostgreSQLManager.dbId:
            {
                cboxDatabaseType.getItems().add(PostgreSQLManager.dbName);
                cboxDatabaseType.getItems().add(SQLiteManager.dbName);
                cboxDatabaseType.getItems().add(MysqlManager.dbName);

                cboxDatabaseType.setValue(cboxDatabaseType.getItems().get(0));

                break;
            }
        }

        switch (Integer.valueOf(prefs.getProperty("DBTYPE")))
        {
            case SQLiteManager.dbId:
            {
                txtDatabaseLocation.setText(prefs.getProperty("DATABASE"));
                txtDatabaseName.setDisable(true);
                txtDatabaseUserName.setDisable(true);
                pwdDatabasePassword.setDisable(true);

                break;
            }
            case MysqlManager.dbId:
            {
                txtDatabaseLocation.setText(prefs.getProperty("DATABASE"));
                txtDatabaseName.setText(prefs.getProperty("DBNAME"));
                txtDatabaseUserName.setText(prefs.getProperty("USERNAME"));
                pwdDatabasePassword.setText(prefs.getProperty("PASSWORD"));

                break;
            }
            case PostgreSQLManager.dbId:
            {
                txtDatabaseLocation.setText(prefs.getProperty("DATABASE"));
                txtDatabaseName.setText(prefs.getProperty("DBNAME"));
                txtDatabaseUserName.setText(prefs.getProperty("USERNAME"));
                pwdDatabasePassword.setText(prefs.getProperty("PASSWORD"));

                break;
            }
        }

        if (MainFrame.cUser.getAdmin())
        {
            tabGeneral.setDisable(false);
            tabCreateAccount.setDisable(false);
            tabAdministrative.setDisable(false);
            tabStopManagement.setDisable(false);
            tabRouteManagement.setDisable(false);
            tabCourierManagement.setDisable(false);
        }
        else
        {
            tabGeneral.setDisable(true);
            tabCreateAccount.setDisable(true);
            tabAdministrative.setDisable(true);
            tabStopManagement.setDisable(true);
            tabRouteManagement.setDisable(true);
            tabCourierManagement.setDisable(true);
            tabpaneMainPane.getSelectionModel().select(tabAccountManagement);
        }

        lblUserId.textProperty().set(
                "User ID: " + String.valueOf(MainFrame.cUser.getUserId()));

        lblAboutVersion.setText("Version: " + prefs.getProperty("VERSION")
                + " Build " + prefs.getProperty("BUILD"));

        lviewRouteOnRoute.getSelectionModel().setSelectionMode(
                SelectionMode.MULTIPLE);
        lviewRouteUnassigned.getSelectionModel().setSelectionMode(
                SelectionMode.MULTIPLE);

        cboxGeneralEmailEnabled.setSelected(Boolean.valueOf(prefs.getProperty("EMAILENABLE")));

        if (!cboxGeneralEmailEnabled.isSelected())
        {
            tabpaneMainPane.getTabs().remove(tabEmailManagement);
        }
        else
        {
            cboxEmailMessageSendTime.getItems().clear();

//            cboxEmailMessageSendTime.getItems().add("START UP");
            cboxEmailMessageSendTime.getItems().add("PRINT");
//            cboxEmailMessageSendTime.getItems().add("CLOSING");

            cboxEmailMessageSendTime.setValue(MainFrame.properties.getProperty("EMAILSEND"));

            cboxEmailSendingAuthReq.getItems().clear();

            cboxEmailSendingAuthReq.getItems().add(true);
            cboxEmailSendingAuthReq.getItems().add(false);

            cboxEmailSendingAuthReq.setValue(Boolean.valueOf(MainFrame.properties.getProperty("EMAILAUTHREQ")));

            if (cboxEmailSendingAuthReq.getValue())
            {
                txtEmailSendingAuthUserName.setText(MainFrame.properties.getProperty("EMAILUSERNAME"));
                txtEmailSendingAuthPassword.setText(MainFrame.properties.getProperty("EMAILPASSWORD"));
            }

            txtEmailSendingHost.setText(MainFrame.properties.getProperty("EMAILHOST"));
            txtEmailSendingPort.setText(MainFrame.properties.getProperty("EMAILPORT"));
            txtEmailMessageReplyTo.setText(MainFrame.properties.getProperty("EMAILREPLYTO"));
            txtEmailMessageContent.setText(MainFrame.properties.getProperty("EMAILMESSAGE"));
        }

        loadAdminComboBoxes();
        loadRouteComboBoxes();
        loadStopComboBoxes();
        loadCourierComboBoxes();
    }

    /**
     * Process keyboard input
     *
     * @param ke KeyEvent from OS
     */
    public void keyPressedAction(KeyEvent ke)
    {
        if (ke.getCode() == KeyCode.ESCAPE)
        {
            btnCancel.fire();
        }
    }

    // General Settings//

    /**
     * Processes Change in Database Type
     *
     * @param ae ActionEvent from OS
     */
    public void cboxDatabaseTypeAction(ActionEvent ae)
    {
        ae.consume();
        switch (((int) cboxDatabaseType.getValue().charAt(0)) - 48)
        {
            case SQLiteManager.dbId:
            {
                btnBrowse.setDisable(false);
                txtDatabaseLocation.setText("");
                txtDatabaseName.setText("");
                txtDatabaseUserName.setText("");
                pwdDatabasePassword.setText("");
                txtDatabaseName.setDisable(true);
                txtDatabaseUserName.setDisable(true);
                pwdDatabasePassword.setDisable(true);

                break;
            }
            case MysqlManager.dbId:
            {
                btnBrowse.setDisable(true);
                txtDatabaseLocation.setText("");
                txtDatabaseName.setText("");
                txtDatabaseUserName.setText("");
                pwdDatabasePassword.setText("");

                txtDatabaseName.setDisable(false);
                txtDatabaseUserName.setDisable(false);
                pwdDatabasePassword.setDisable(false);

                break;
            }
            case PostgreSQLManager.dbId:
            {
                btnBrowse.setDisable(true);
                txtDatabaseLocation.setText("");
                txtDatabaseName.setText("");
                txtDatabaseUserName.setText("");
                pwdDatabasePassword.setText("");

                txtDatabaseName.setDisable(false);
                txtDatabaseUserName.setDisable(false);
                pwdDatabasePassword.setDisable(false);
                break;
            }
            default:
            {
                MessageDialogBuilder.error()
                        .message("Unsupported Database Selected")
                        .buttonType(MessageDialog.ButtonType.OK)
                        .show(MainFrame.stage.getScene().getWindow());
                break;
            }
        }
    }

    /**
     * Browses for Database File
     * Used only for SQLite configuration
     *
     * @param ae ActionEvent from OS
     */
    public void btnBrowseAction(ActionEvent ae)
    {
        ae.consume();
        FileChooser fChooser = new FileChooser();
        fChooser.setTitle("Select Database File");

        File dbFile = fChooser.showOpenDialog(MainFrame.stage);

        if (dbFile != null)
        {
            txtDatabaseLocation.setText(dbFile.getAbsolutePath());
        }
        else
        {
            MessageDialogBuilder.error().message("No File Selected")
                    .buttonType(MessageDialog.ButtonType.OK)
                    .show(MainFrame.stage.getScene().getWindow());
        }
    }

    /**
     * Saves Settings to settings file
     *
     * @param ae ActionEvent from OS
     */
    public void btnSaveAction(ActionEvent ae)
    {
        ae.consume();
        switch (((int) cboxDatabaseType.getValue().charAt(0)) - 48)
        {
            case SQLiteManager.dbId:
            {
                MainFrame.properties.setProperty("DBTYPE", String.valueOf(0));
                MainFrame.properties.setProperty("DATABASE",
                        txtDatabaseLocation.getText());
                MainFrame.properties.setProperty("USERNAME", "");
                MainFrame.properties.setProperty("PASSWORD", "");
                MainFrame.properties.setProperty("DBNAME", "");

                break;
            }
            case MysqlManager.dbId:
            {
                MainFrame.properties.setProperty("DBTYPE", String.valueOf(1));
                MainFrame.properties.setProperty("DATABASE",
                        txtDatabaseLocation.getText());
                MainFrame.properties.setProperty("USERNAME",
                        txtDatabaseUserName.getText());
                MainFrame.properties.setProperty("PASSWORD",
                        pwdDatabasePassword.getText());
                MainFrame.properties.setProperty("DBNAME",
                        txtDatabaseName.getText());
                break;
            }
            case PostgreSQLManager.dbId:
            {
                MainFrame.properties.setProperty("DBTYPE", String.valueOf(2));
                MainFrame.properties.setProperty("DATABASE",
                        txtDatabaseLocation.getText());
                MainFrame.properties.setProperty("USERNAME",
                        txtDatabaseUserName.getText());
                MainFrame.properties.setProperty("PASSWORD",
                        pwdDatabasePassword.getText());
                MainFrame.properties.setProperty("DBNAME",
                        txtDatabaseName.getText());
                break;
            }
            default:
            {
                MessageDialogBuilder.error()
                        .message("Unsupported Database Selected")
                        .buttonType(MessageDialog.ButtonType.OK)
                        .show(MainFrame.stage.getScene().getWindow());
                break;
            }
        }

        MainFrame.saveProperties();

        MessageDialog.Answer restart = MessageDialogBuilder.info()
                .message("Program Must Be Restart!\nRestart Now?")
                .title("Save Complete")
                .buttonType(MessageDialog.ButtonType.YES_NO)
                .yesOkButtonText("Yes").noButtonText("No")
                .show(MainFrame.stage.getScene().getWindow());

        if (restart == MessageDialog.Answer.YES_OK)
        {
            try
            {
                File f = new File("./Richardson Hall.jar");
                String systemName = System.getProperty("os.name");

                if (systemName.equalsIgnoreCase("linux"))
                {
                    ProcessBuilder pb = new ProcessBuilder("java", "-jar", f.getCanonicalPath());
                    pb.directory(new File("."));
                    pb.start();
                }
                else
                {
                    Desktop.getDesktop().open(f);
                }
            }
            catch (IOException e)
            {
                Logger.logException(e);
            }
            dbManager.dispose();
            System.exit(0);
        }
        else
        {
            try
            {
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
    }

    /**
     * Cancels settings changes
     *
     * @param ae ActionEvent from OS
     */
    public void btnCancelAction(ActionEvent ae)
    {
        ae.consume();
        try
        {
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

    // Account Management//

    /**
     * Changes account Password
     *
     * @param ae ActionEvent from OS
     */
    public void btnChangePwdAction(ActionEvent ae)
    {
        ae.consume();

        String username = MainFrame.cUser.getUserName();
        String oldPwd = pwdChangePwdOld.getText();
        String newPwd = pwdChangePwdNew.getText();
        String pwdConfirm = pwdChangePwdConfirm.getText();
        boolean verified = true;

        if (pwdChangePwdOld.getText().equals("")
                || pwdChangePwdNew.getText().equals("")
                || pwdChangePwdConfirm.getText().equals(""))
        {
            MessageDialogBuilder.error().message("Cannot have Empty Fields")
                    .title("Error").buttonType(MessageDialog.ButtonType.OK)
                    .show(MainFrame.stage.getScene().getWindow());
        }
        else
        {
            if (oldPwd.equals(newPwd))
            {
                MessageDialogBuilder.error()
                        .message("New Password cannot be same as Old Password")
                        .title("Password Error")
                        .buttonType(MessageDialog.ButtonType.OK)
                        .show(MainFrame.stage.getScene().getWindow());
                verified = false;
                pwdChangePwdOld.textProperty().set("");
                pwdChangePwdNew.textProperty().set("");
                pwdChangePwdConfirm.textProperty().set("");
            }
            if (!newPwd.equals(pwdConfirm))
            {
                MessageDialogBuilder
                        .error()
                        .message(
                                "New Password and Confirm Password are not the same")
                        .title("Password Error")
                        .buttonType(MessageDialog.ButtonType.OK)
                        .show(MainFrame.stage.getScene().getWindow());
                verified = false;
                pwdChangePwdNew.textProperty().set("");
                pwdChangePwdConfirm.textProperty().set("");
            }

            if (verified)
            {
                try
                {
                    MessageDigest digest = MessageDigest.getInstance("SHA-256");
                    byte[] oldPwdOutput = digest.digest(oldPwd.getBytes());
                    byte[] newPwdOutput = digest.digest(newPwd.getBytes());
                    byte[] userOutput = digest.digest(username.getBytes());

                    String oldCombine = new HexBinaryAdapter().marshal(userOutput) + new HexBinaryAdapter().marshal(oldPwdOutput);
                    String newCombine = new HexBinaryAdapter().marshal(userOutput) + new HexBinaryAdapter().marshal(newPwdOutput);

                    byte[] oldPassword = digest.digest(oldCombine.getBytes());
                    byte[] newPassword = digest.digest(newCombine.getBytes());

                    if (dbManager.changePassword(MainFrame.cUser, oldPassword,
                            newPassword))
                    {
                        MessageDialogBuilder
                                .info()
                                .message(
                                        "Password Changed Successfully\nYou will now be logged out")
                                .title("Success")
                                .buttonType(MessageDialog.ButtonType.OK)
                                .show(MainFrame.stage.getScene().getWindow());
                        try
                        {
                            Parent root = FXMLLoader.load(getClass().getResource(
                                    "/com/mailroom/fxml/mainclient/LoginFx.fxml"));
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
                        MessageDialogBuilder.error()
                                .message("Password Change Unsuccessful")
                                .title("Error")
                                .buttonType(MessageDialog.ButtonType.OK)
                                .show(MainFrame.stage.getScene().getWindow());
                    }
                }
                catch (NoSuchAlgorithmException nsae)
                {
                    Logger.logException(nsae);
                }
            }
        }
    }

    /**
     * Creates new Account
     *
     * @param ae ActionEvent from OS
     */
    public void btnCreateAccountAction(ActionEvent ae)
    {
        ae.consume();
        byte[] password;
        String pwd = pwdCreatePwd.getText();
        String pwdConfirm = pwdCreateConfirm.getText();
        if (txtCreateFirstName.getText().equals("")
                || txtCreateLastName.getText().equals("")
                || txtCreateUserName.getText().equals("")
                || pwdCreatePwd.getText().equals("")
                || pwdCreateConfirm.getText().equals(""))
        {
            MessageDialogBuilder.error().message("Cannot have Empty Fields")
                    .title("Error").buttonType(MessageDialog.ButtonType.OK)
                    .show(MainFrame.stage.getScene().getWindow());
        }
        else
        {
            if (pwd.equals(pwdConfirm))
            {
                try
                {
                    MessageDigest digest = MessageDigest.getInstance("SHA-256");
                    byte[] pwdOutput = digest.digest(pwd.getBytes());
                    byte[] userOutput = digest.digest(cboxAdminReactivate.getValue().toString().getBytes());
                    String combineOutput = new HexBinaryAdapter().marshal(userOutput) + new HexBinaryAdapter().marshal(pwdOutput);
                    password = digest.digest(combineOutput.getBytes());
                    if (dbManager.addUser(
                            new User(-1, txtCreateUserName.getText(),
                                    txtCreateFirstName.getText(), txtCreateLastName
                                    .getText(), cboxCreateAdmin
                                    .selectedProperty().get()), password))
                    {
                        MessageDialogBuilder
                                .info()
                                .message(
                                        "User " + txtCreateUserName.getText()
                                                + " Added").title("Success")
                                .buttonType(MessageDialog.ButtonType.OK)
                                .show(MainFrame.stage.getScene().getWindow());
                        btnCreateCancel.fire();
                        loadAdminComboBoxes();
                    }
                    else
                    {
                        MessageDialogBuilder
                                .error()
                                .message(
                                        "Error Adding User "
                                                + txtCreateUserName.getText())
                                .title("Error")
                                .buttonType(MessageDialog.ButtonType.OK)
                                .show(MainFrame.stage.getScene().getWindow());
                    }
                }
                catch (NoSuchAlgorithmException nsae)
                {
                    Logger.logException(nsae);
                }
            }
            else
            {
                MessageDialogBuilder.error().message("Passwords Do Not Match")
                        .title("Error").buttonType(MessageDialog.ButtonType.OK)
                        .show(MainFrame.stage.getScene().getWindow());
                pwdCreatePwd.textProperty().set("");
                pwdCreateConfirm.textProperty().set("");
            }
        }
    }

    /**
     * Cancels Creating a new Account
     *
     * @param ae ActionEvent from OS
     */
    public void btnCreateCancelAction(ActionEvent ae)
    {
        ae.consume();
        txtCreateFirstName.textProperty().set("");
        txtCreateLastName.textProperty().set("");
        txtCreateUserName.textProperty().set("");
        pwdCreatePwd.textProperty().set("");
        pwdCreateConfirm.textProperty().set("");
        cboxCreateAdmin.selectedProperty().set(false);
    }

    /**
     * Changes Admin Rights for Account
     *
     * @param ae ActionEvent from OS
     */
    public void btnAdminChangeSaveAction(ActionEvent ae)
    {
        ae.consume();
        dbManager.setUserAdmin(cboxAdminChange.getValue(),
                cboxAdminAdminStatus.isSelected());
        loadAdminComboBoxes();
    }

    /**
     * Re-enables login for Account
     *
     * @param ae ActionEvent from OS
     */
    public void btnAdminReactivateAction(ActionEvent ae)
    {
        ae.consume();
        byte[] password;
        String pwd = pwdAdminRePwd.getText();
        String pwdConfirm = pwdAdminReConfirm.getText();

        if (pwd.equals(pwdConfirm))
        {
            try
            {
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] pwdOutput = digest.digest(pwd.getBytes());
                byte[] userOutput = digest.digest(cboxAdminReactivate.getValue().toString().getBytes());
                String combineOutput = new HexBinaryAdapter().marshal(userOutput) + new HexBinaryAdapter().marshal(pwdOutput);
                password = digest.digest(combineOutput.getBytes());


                if (dbManager.reactivateUser(cboxAdminReactivate.getValue(),
                        password))
                {
                    MessageDialogBuilder
                            .info()
                            .message(
                                    "User " + cboxAdminReactivate.getValue()
                                            + " reactivated").title("Success")
                            .buttonType(MessageDialog.ButtonType.OK)
                            .show(MainFrame.stage.getScene().getWindow());
                }
                else
                {
                    MessageDialogBuilder
                            .error()
                            .message(
                                    "Error Reactivating User: "
                                            + cboxAdminReactivate.getValue())
                            .title("Error").buttonType(MessageDialog.ButtonType.OK)
                            .show(MainFrame.stage.getScene().getWindow());
                }
            }
            catch (NoSuchAlgorithmException nsae)
            {
                Logger.logException(nsae);
            }
            pwdAdminRePwd.setText("");
            pwdAdminReConfirm.setText("");
            loadAdminComboBoxes();
        }
        else
        {
            MessageDialogBuilder.error()
                    .message("Password and Confirm Password do not match")
                    .title("Error").buttonType(MessageDialog.ButtonType.OK)
                    .show(MainFrame.stage.getScene().getWindow());
            pwdAdminRePwd.setText("");
            pwdAdminReConfirm.setText("");
        }
    }

    /**
     * Disables login for Account
     *
     * @param ae ActionEvent from OS
     */
    public void btnAdminDeactivateAction(ActionEvent ae)
    {
        ae.consume();
        if (!MainFrame.cUser.getUserName().equals(
                cboxAdminDeactivate.getValue().getUserName()))
        {
            MessageDialog.Answer del = MessageDialogBuilder.warning()
                    .message("Delete " + cboxAdminDeactivate.getValue() + "?")
                    .title("Confirm")
                    .buttonType(MessageDialog.ButtonType.YES_NO_CANCEL)
                    .yesOkButtonText("Yes").noButtonText("No")
                    .cancelButtonText("Cancel")
                    .show(MainFrame.stage.getScene().getWindow());

            if (del == MessageDialog.Answer.YES_OK)
            {
                dbManager.deleteUser(cboxAdminDeactivate.getValue());
                loadAdminComboBoxes();
            }
        }
        else
        {
            MessageDialogBuilder.warning()
                    .message("Cannot Deactivate Yourself").title("Error")
                    .buttonType(MessageDialog.ButtonType.OK)
                    .show(MainFrame.stage.getScene().getWindow());
        }
    }

    public void cboxGeneralEmailEnabledAction(ActionEvent ae)
    {
        ae.consume();

        tabpaneMainPane.getTabs().remove(tabAbout);
        tabpaneMainPane.getTabs().add(tabEmailManagement);
        tabpaneMainPane.getTabs().add(tabAbout);
    }

    /**
     * Populates Admin Comboboxes with data from Database
     */
    private void loadAdminComboBoxes()
    {
        cboxAdminChange.itemsProperty().get().clear();
        cboxAdminReactivate.itemsProperty().get().clear();
        cboxAdminDeactivate.itemsProperty().get().clear();

        for (User u : dbManager.getActiveUsers())
        {
            cboxAdminChange.itemsProperty().get().add(u);
            cboxAdminDeactivate.itemsProperty().get().add(u);
        }
        for (User u : dbManager.getDeactivatedUsers())
        {
            cboxAdminReactivate.itemsProperty().get().add(u);
        }

        if (cboxAdminChange.getItems().size() > 0)
        {
            cboxAdminChange.setValue(cboxAdminChange.itemsProperty().get()
                    .get(0));
        }
        if (cboxAdminDeactivate.getItems().size() > 0)
        {
            cboxAdminDeactivate.setValue(cboxAdminDeactivate.itemsProperty()
                    .get().get(0));
        }
        if (cboxAdminReactivate.getItems().size() > 0)
        {
            cboxAdminReactivate.setValue(cboxAdminReactivate.itemsProperty()
                    .get().get(0));
        }
    }

    // Stop Management//

    /**
     * Deletes Stop
     *
     * @param ae ActionEvent from OS
     */
    public void btnStopDeleteAction(ActionEvent ae)
    {
        ae.consume();
        MessageDialog.Answer del = MessageDialogBuilder.confirmation()
                .message("Delete " + cboxStopDelete.getValue() + " Stop")
                .title("Confirm").buttonType(MessageDialog.ButtonType.YES_NO)
                .yesOkButtonText("Yes").noButtonText("No")
                .show(MainFrame.stage.getScene().getWindow());

        if (del == MessageDialog.Answer.YES_OK)
        {
            dbManager.deleteStop(cboxStopDelete.getValue());
        }

        loadStopComboBoxes();
        loadRouteComboBoxes();
    }

    /**
     * Creates new Stop
     *
     * @param ae ActionEvent from OS
     */
    public void btnStopCreateAction(ActionEvent ae)
    {
        ae.consume();
        if (txtStopName.getText().equals(""))
        {
            MessageDialogBuilder.error().message("Cannot have Empty Stop Name")
                    .title("Error").buttonType(MessageDialog.ButtonType.OK)
                    .show(MainFrame.stage.getScene().getWindow());
        }
        else
        {
            dbManager.addStop(new Stop(-1, txtStopName.getText(), cboxStopRoute
                    .getValue().getRouteName(), 0, cboxStopCreateStudent
                    .isSelected(), false));
            btnStopClear.fire();
            loadStopComboBoxes();
            loadRouteComboBoxes();
        }
    }

    /**
     * Clears Data entries from Create A Stop
     *
     * @param ae ActionEvent from OS
     */
    public void btnStopClearAction(ActionEvent ae)
    {
        ae.consume();
        cboxStopCreateStudent.setSelected(false);
        txtStopName.setText("");
    }

    /**
     * Updates selected Stop
     *
     * @param ae ActionEvent from OS
     */
    public void btnStopUpdateSaveAction(ActionEvent ae)
    {
        ae.consume();
        cboxStopUpdate.getValue()
                .setStudent(cboxStopUpdateStudent.isSelected());
        cboxStopUpdate.getValue().setAutoRemove(cboxStopUpdateAutoRemove.isSelected());
        dbManager.updateStop(cboxStopUpdate.getValue());
        loadRouteComboBoxes();
    }

    public void cboxStopUpdateAction(ActionEvent ae)
    {
        ae.consume();

        cboxStopUpdateStudent.setSelected(cboxStopUpdate.getValue().getStudent());
        cboxStopUpdateAutoRemove.setSelected(cboxStopUpdate.getValue().getAutoRemove());
    }

    /**
     * Populates Stop comboboxes with data from Database
     */
    private void loadStopComboBoxes()
    {
        cboxStopDelete.getItems().clear();
        cboxStopUpdate.getItems().clear();
        cboxEmailAddressStop.getItems().clear();

        dbManager.loadStops();

        for (Stop s : dbManager.getStops())
        {
            if (s.getStopId() != 1)
            {
                cboxStopDelete.getItems().add(s);
                cboxStopUpdate.getItems().add(s);
                cboxEmailAddressStop.getItems().add(s);
            }
        }

        if (cboxStopDelete.getItems().size() > 0)
        {
            cboxStopDelete
                    .setValue(cboxStopDelete.itemsProperty().get().get(0));
        }
        if (cboxStopUpdate.getItems().size() > 0)
        {
            cboxStopUpdate
                    .setValue(cboxStopUpdate.itemsProperty().get().get(0));
        }
    }

    // Route Management//

    /**
     * Processes Route selection changing
     *
     * @param ae ActionEvent from OS
     */
    public void cboxRouteSelectAction(ActionEvent ae)
    {
        ae.consume();
        loadRouteListViews();
    }

    /**
     * Deletes Route
     * Moves all Stops on Route to unassigned
     *
     * @param ae ActionEvent from OS
     */
    public void btnRouteDeleteAction(ActionEvent ae)
    {
        ae.consume();
        MessageDialog.Answer del = MessageDialogBuilder.confirmation()
                .message("Delete " + cboxRouteDelete.getValue() + " Route")
                .title("Confirm").buttonType(MessageDialog.ButtonType.YES_NO)
                .yesOkButtonText("Yes").noButtonText("No")
                .show(MainFrame.stage.getScene().getWindow());

        if (del == MessageDialog.Answer.YES_OK)
        {
            dbManager.deleteRoute(cboxRouteDelete.getValue());
        }

        loadRouteComboBoxes();
    }

    /**
     * Creates new Route
     *
     * @param ae ActionEvent from OS
     */
    public void btnRouteCreateAction(ActionEvent ae)
    {
        ae.consume();
        if (txtRouteName.getText().equals(""))
        {
            MessageDialogBuilder.error()
                    .message("Cannot have Empty Route Name").title("Error")
                    .buttonType(MessageDialog.ButtonType.OK)
                    .show(MainFrame.stage.getScene().getWindow());
        }
        else
        {
            if (dbManager.addRoute(txtRouteName.getText()))
            {
                loadRouteComboBoxes();
                txtRouteName.setText("");
            }
        }
    }

    /**
     * Adds Stop to selected Route
     *
     * @param ae ActionEvent from OS
     */
    public void btnRouteAddAction(ActionEvent ae)
    {
        ae.consume();
        List<Stop> selected = lviewRouteUnassigned.selectionModelProperty()
                .getValue().getSelectedItems();

        for (Stop s : selected)
        {
            dbManager.addStopToRoute(s, cboxRouteSelect.getValue());
        }

        loadRouteListViews();
    }

    /**
     * Removes stop from selected Route
     *
     * @param ae ActionEvent from OS
     */
    public void btnRouteRemoveAction(ActionEvent ae)
    {
        ae.consume();
        List<Stop> selected = lviewRouteOnRoute.selectionModelProperty().get()
                .getSelectedItems();

        for (Stop s : selected)
        {
            dbManager.addStopToRoute(s, new Route(1, "unassigned"));
        }

        loadRouteListViews();
    }

    /**
     * Process Route Selection Changing
     *
     * @param ae ActionEvent from OS
     */
    public void cboxDesignRouteAction(ActionEvent ae)
    {
        ae.consume();
        lviewDesignStops.getItems().clear();

        if (cboxDesignRoute.getValue() != null)
        {
            for (Stop s : dbManager.getStopsOnRoute(cboxDesignRoute.getValue()))
            {
                lviewDesignStops.getItems().add(s);
            }
        }
    }

    /**
     * Puts Stop at beginning of selected Route
     *
     * @param ae ActionEvent from OS
     */
    public void btnDesignFirstAction(ActionEvent ae)
    {
        ae.consume();
        if (lviewDesignStops.selectionModelProperty().get().getSelectedItem() != null)
        {
            Stop selected = lviewDesignStops.selectionModelProperty().get()
                    .getSelectedItem();
            int order = 0;

            dbManager.setRoutePosition(selected, order);
            order++;

            for (Stop s : lviewDesignStops.getItems())
            {
                if (s != selected)
                {
                    dbManager.setRoutePosition(s, order);
                    order++;
                }
            }

            lviewDesignStops.getItems().clear();
            for (Stop s : dbManager.getStopsOnRoute(cboxDesignRoute.getValue()))
            {
                lviewDesignStops.getItems().add(s);
            }
        }
    }

    /**
     * Moves Stop up one position on Selected Route
     *
     * @param ae ActionEvent from OS
     */
    public void btnDesignUpAction(ActionEvent ae)
    {
        ae.consume();
        if (lviewDesignStops.selectionModelProperty().get().getSelectedItem() != null)
        {
            Stop selected = lviewDesignStops.selectionModelProperty().get()
                    .getSelectedItem();

            for (Stop s : lviewDesignStops.getItems())
            {
                if (s.getRouteOrder() == selected.getRouteOrder() - 1)
                {
                    dbManager.setRoutePosition(s, selected.getRouteOrder());
                    dbManager.setRoutePosition(selected, s.getRouteOrder());
                }
            }

            lviewDesignStops.getItems().clear();
            for (Stop s : dbManager.getStopsOnRoute(cboxDesignRoute.getValue()))
            {
                lviewDesignStops.getItems().add(s);
            }
        }
    }

    /**
     * Puts stop at End of selected Route
     *
     * @param ae ActionEvent from OS
     */
    public void btnDesignLastAction(ActionEvent ae)
    {
        ae.consume();
        if (lviewDesignStops.selectionModelProperty().get().getSelectedItem() != null)
        {
            Stop selected = lviewDesignStops.selectionModelProperty().get()
                    .getSelectedItem();
            int order = 0;

            for (Stop s : lviewDesignStops.getItems())
            {
                if (s != selected)
                {
                    dbManager.setRoutePosition(s, order);
                    order++;
                }
            }

            dbManager.setRoutePosition(selected, order);

            lviewDesignStops.getItems().clear();
            for (Stop s : dbManager.getStopsOnRoute(cboxDesignRoute.getValue()))
            {
                lviewDesignStops.getItems().add(s);
            }
        }
    }

    /**
     * Moves Stop down one position on Selected Route
     *
     * @param ae ActionEvent from OS
     */
    public void btnDesignDownAction(ActionEvent ae)
    {
        ae.consume();
        if (lviewDesignStops.selectionModelProperty().get().getSelectedItem() != null)
        {
            Stop selected = lviewDesignStops.selectionModelProperty().get()
                    .getSelectedItem();

            for (Stop s : lviewDesignStops.getItems())
            {
                if (s.getRouteOrder() == selected.getRouteOrder() + 1)
                {
                    dbManager.setRoutePosition(s, selected.getRouteOrder());
                    dbManager.setRoutePosition(selected, s.getRouteOrder());
                }
            }

            lviewDesignStops.getItems().clear();
            for (Stop s : dbManager.getStopsOnRoute(cboxDesignRoute.getValue()))
            {
                lviewDesignStops.getItems().add(s);
            }
        }
    }

    /**
     * Populates Route listviews with data from Database
     */
    private void loadRouteListViews()
    {
        dbManager.loadStops();

        lviewRouteUnassigned.getItems().clear();
        lviewRouteOnRoute.getItems().clear();

        for (Stop s : dbManager.getUnassignedStops())
        {
            if (s.getStopId() != 1)
            {
                lviewRouteUnassigned.getItems().add(s);
            }
        }

        if (cboxRouteSelect.getValue() != null)
        {
            List<Stop> stops = dbManager.getStopsOnRoute(cboxRouteSelect
                    .getValue());

            for (Stop s : stops)
            {
                lviewRouteOnRoute.getItems().add(s);
            }
        }
        else
        {
            if (cboxRouteSelect.getItems().size() > 0)
            {
                cboxRouteSelect.setValue(cboxRouteSelect.itemsProperty().get()
                        .get(0));
            }
        }
    }

    /**
     * Populates Route comboboxes with data from Database
     */
    private void loadRouteComboBoxes()
    {
        dbManager.loadRoutes();

        cboxRouteSelect.getItems().clear();
        cboxRouteDelete.getItems().clear();
        cboxStopRoute.getItems().clear();
        cboxDesignRoute.getItems().clear();

        for (Route r : dbManager.getRoutes())
        {
            cboxStopRoute.getItems().add(r);
            if (r.getRouteId() != 1)
            {
                cboxRouteSelect.getItems().add(r);
                cboxRouteDelete.getItems().add(r);
                cboxDesignRoute.getItems().add(r);
            }
        }

        if (cboxRouteSelect.getItems().size() > 0)
        {
            cboxRouteSelect.setValue(cboxRouteSelect.itemsProperty().get()
                    .get(0));
        }
        if (cboxRouteDelete.getItems().size() > 0)
        {
            cboxRouteDelete.setValue(cboxRouteDelete.itemsProperty().get()
                    .get(0));
        }
        if (cboxStopRoute.getItems().size() > 0)
        {
            cboxStopRoute.setValue(cboxStopRoute.itemsProperty().get().get(0));
        }
        if (cboxDesignRoute.getItems().size() > 0)
        {
            cboxDesignRoute.setValue(cboxDesignRoute.itemsProperty().get()
                    .get(0));
        }
    }

    // Courier Management//

    /**
     * Deletes Courier from Database
     *
     * @param ae ActionEvent from OS
     */
    public void btnCourierDeleteAction(ActionEvent ae)
    {
        ae.consume();
        MessageDialog.Answer del = MessageDialogBuilder.confirmation()
                .message("Delete " + cboxCourierDelete.getValue() + " Courier")
                .title("Confirm").buttonType(MessageDialog.ButtonType.YES_NO)
                .yesOkButtonText("Yes").noButtonText("No")
                .show(MainFrame.stage.getScene().getWindow());

        if (del == MessageDialog.Answer.YES_OK)
        {
            dbManager.deleteCourier(cboxCourierDelete.getValue());
        }

        loadCourierComboBoxes();
    }

    /**
     * Creates new Courier in Databse
     *
     * @param ae ActionEvent from OS
     */
    public void btnCourierCreateAction(ActionEvent ae)
    {
        ae.consume();
        if (txtCourierName.getText().equals(""))
        {
            MessageDialogBuilder.error()
                    .message("Cannot have Empty Courier Name").title("Error")
                    .buttonType(MessageDialog.ButtonType.OK)
                    .show(MainFrame.stage.getScene().getWindow());
        }
        else
        {
            dbManager.addCourier(txtCourierName.getText());
            loadCourierComboBoxes();
            txtCourierName.setText("");
        }
    }

    /**
     * Populates Courier Comboboxes with data from Database
     */
    private void loadCourierComboBoxes()
    {
        dbManager.loadCouriers();
        cboxCourierDelete.getItems().clear();

        for (Courier c : dbManager.getCouriers())
        {
            cboxCourierDelete.getItems().add(c);
        }

        if (cboxCourierDelete.getItems().size() > 0)
        {
            cboxCourierDelete.setValue(cboxCourierDelete.itemsProperty().get()
                    .get(0));
        }
    }

    // Email Management //

    /**
     * Removes Email from Contact List for Stop
     *
     * @param ae ActionEvent from OS
     */
    public void btnEmailAddressRemoveAction(ActionEvent ae)
    {
        ae.consume();

        ObservableList<String> toRemove = lviewEmailAddresses.getSelectionModel().getSelectedItems();

        for (String str : toRemove)
        {
            dbManager.deleteEmailAddress(cboxEmailAddressStop.getValue(), str);
        }
    }

    /**
     * Adds new Email Address to Contact List for Stop
     *
     * @param ae ActionEvent from OS
     */
    public void btnEmailAddressAddAction(ActionEvent ae)
    {
        ae.consume();

        dbManager.addEmailAddress(cboxEmailAddressStop.getValue(), txtEmailAddressAdd.getText());
        txtEmailAddressAdd.setText("");
        cboxEmailAddressStop.setValue(cboxEmailAddressStop.getValue());
    }

    /**
     * ComboBox controlling what Stop's Contact List is being Viewed
     *
     * @param ae ActionEvent from OS
     */
    public void cboxEmailAddressStopAction(ActionEvent ae)
    {
        ae.consume();

        lviewEmailAddresses.getItems().clear();

        for (String s : dbManager.getEmailAddress(cboxEmailAddressStop.getValue()))
        {
            lviewEmailAddresses.getItems().add(s);
        }
    }

    /**
     * Saves Settings for Email Message
     *
     * @param ae ActionEvent from OS
     */
    public void btnEmailMessageSaveAction(ActionEvent ae)
    {
        ae.consume();

        MainFrame.properties.setProperty("EMAILREPLYTO", txtEmailMessageReplyTo.getText());
        MainFrame.properties.setProperty("EMAILMESSAGE", txtEmailMessageContent.getText());
        MainFrame.properties.setProperty("EMAILSEND", cboxEmailMessageSendTime.getValue());

        MainFrame.saveProperties();
    }

    /**
     * Saves Settings for Email Sending
     *
     * @param ae ActionEvent from OS
     */
    public void btnEmailSendingSaveAction(ActionEvent ae)
    {
        ae.consume();

        MainFrame.properties.setProperty("EMAILHOST", txtEmailSendingHost.getText());
        MainFrame.properties.setProperty("EMAILPORT", txtEmailSendingPort.getText());
        MainFrame.properties.setProperty("EMAILAUTHREQ", cboxEmailSendingAuthReq.getValue().toString());

        if (cboxEmailSendingAuthReq.getValue())
        {
            MainFrame.properties.setProperty("EMAILUSERNAME", txtEmailSendingAuthUserName.getText());
            MainFrame.properties.setProperty("EMAILPASSWORD", txtEmailSendingAuthPassword.getText());
        }

        MainFrame.saveProperties();
    }

    /**
     * Controls Email Authentication Credential usability
     *
     * @param ae ActionEvent from OS
     */
    public void cboxEmailSendingAuthReqAction(ActionEvent ae)
    {
        ae.consume();

        if (cboxEmailSendingAuthReq.getValue())
        {
            txtEmailSendingAuthUserName.setDisable(false);
            txtEmailSendingAuthPassword.setDisable(false);
        }
        if (!cboxEmailSendingAuthReq.getValue())
        {
            txtEmailSendingAuthUserName.setDisable(true);
            txtEmailSendingAuthPassword.setDisable(true);
        }
    }
}