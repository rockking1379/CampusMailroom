package com.mailroom.mainclient;

import com.mailroom.common.*;
import com.panemu.tiwulfx.dialog.MessageDialog;
import com.panemu.tiwulfx.dialog.MessageDialogBuilder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * Controls SetupFx.fxml in com.mailroom.fxml.mainclient
 *
 * @author James sitzja@grizzlies.adams.edu
 */
public class SetupController implements Initializable
{
    // Tabs//
    @FXML
    private Tab tabDatabaseSetup;
    @FXML
    private Tab tabAccountSetup;
    @FXML
    private Tab tabCourierSetup;
    @FXML
    private Tab tabStopSetup;
    @FXML
    private TabPane tabpaneMainPane;

    // Common Elements//
    @FXML
    private ProgressBar pbarProgress;

    // Database Setup//
    @FXML
    private ComboBox<String> cboxDbSetupDbType;
    @FXML
    private TextField txtDbSetupDbLocation;
    @FXML
    private TextField txtDbSetupDbName;
    @FXML
    private TextField txtDbSetupDbUsername;
    @FXML
    private TextField txtDbSetupPrefix;
    @FXML
    private PasswordField pwdDbSetupDbPassword;
    @FXML
    private Button btnDbSetupBrowse;
    @FXML
    private Button btnDbSetupVerify;
    @FXML
    private Button btnDbSetupNext;

    // Account Setup//
    @FXML
    private TextField txtAccSetupFirstName;
    @FXML
    private TextField txtAccSetupLastName;
    @FXML
    private TextField txtAccSetupUserName;
    @FXML
    private PasswordField pwdAccSetupPwd;
    @FXML
    private PasswordField pwdAccSetupConfPwd;
    @FXML
    private CheckBox cboxAccSetupAdmin;
    @FXML
    private Button btnAccSetupAddUser;
    @FXML
    private Button btnAccSetupNext;

    // Courier Setup//
    @FXML
    private TextField txtCourierSetupCourierName;
    @FXML
    private Button btnCourierSetupAddCourier;
    @FXML
    private Button btnCourierSetupNext;

    // Stop Setup//
    @FXML
    private TextField txtStopSetupStopName;
    @FXML
    private Button btnStopSetupAddStop;
    @FXML
    private Button btnStopSetupFinish;

    private Properties prefs;
    private DatabaseManager dbManager;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1)
    {
        cboxDbSetupDbType.getItems().clear();
        cboxDbSetupDbType.getItems().add(SQLiteManager.dbName);
        cboxDbSetupDbType.getItems().add(MysqlManager.dbName);
        cboxDbSetupDbType.getItems().add(PostgreSQLManager.dbName);

        pbarProgress.setProgress(0.25);

        setupPreferences();
    }

    // Database Setup//

    /**
     * Fired on selection Change for Combo Box
     *
     * @param ae ActionEvent from OS
     */
    public void cboxDbSetupDbTypeAction(ActionEvent ae)
    {
        ae.consume();
        txtDbSetupDbLocation.setText("");
        txtDbSetupDbName.setText("");
        txtDbSetupDbUsername.setText("");
        pwdDbSetupDbPassword.setText("");

        switch ((int) cboxDbSetupDbType.getValue().charAt(0) - 48)
        {
            case 0:
            {
                txtDbSetupDbName.setDisable(true);
                txtDbSetupDbUsername.setDisable(true);
                pwdDbSetupDbPassword.setDisable(true);
                btnDbSetupBrowse.setDisable(false);
                break;
            }
            case 1:
            {
                txtDbSetupDbName.setDisable(false);
                txtDbSetupDbUsername.setDisable(false);
                pwdDbSetupDbPassword.setDisable(false);
                btnDbSetupBrowse.setDisable(true);
                break;
            }
            case 2:
            {
                txtDbSetupDbName.setDisable(false);
                txtDbSetupDbUsername.setDisable(false);
                pwdDbSetupDbPassword.setDisable(false);
                btnDbSetupBrowse.setDisable(true);
                break;
            }
            default:
            {
                MessageDialogBuilder.error()
                        .message("Unknown Database Type Selected")
                        .buttonType(MessageDialog.ButtonType.OK)
                        .yesOkButtonText("OK")
                        .show(MainFrame.stage.getScene().getWindow());
                break;
            }
        }
    }

    /**
     * Allows User to Browse for SQLite Database File
     *
     * @param ae ActionEvent from OS
     */
    public void btnDbSetupBrowseAction(ActionEvent ae)
    {
        ae.consume();
        File f = new File("./mailroom.db");
        if (!f.exists())
        {
            try
            {
                f.createNewFile();
            }
            catch (IOException e)
            {
                Logger.log(e);
            }
        }

        FileChooser fChooser = new FileChooser();
        fChooser.setTitle("Select Database File");

        File dbFile = fChooser.showOpenDialog(MainFrame.stage);

        if (dbFile != null)
        {
            txtDbSetupDbLocation.setText(dbFile.getAbsolutePath());
        }
        else
        {
            MessageDialogBuilder.error().message("No File Selected")
                    .buttonType(MessageDialog.ButtonType.OK)
                    .show(MainFrame.stage.getScene().getWindow());
        }
    }

    /**
     * Verifies Database Information Entered
     *
     * @param ae ActionEvent from OS
     */
    public void btnDbSetupVerifyAction(ActionEvent ae)
    {
        ae.consume();
        switch ((int) cboxDbSetupDbType.getValue().charAt(0) - 48)
        {
            case 0:
            {
                if (txtDbSetupDbLocation.getText().equals(""))
                {
                    MessageDialogBuilder.error()
                            .message("Database Location cannot be Empty")
                            .buttonType(MessageDialog.ButtonType.OK)
                            .show(MainFrame.stage.getScene().getWindow());
                    return;
                }
                dbManager = new SQLiteManager(txtDbSetupDbLocation.getText());
                break;
            }
            case 1:
            {
                if (txtDbSetupDbLocation.getText().equals(""))
                {
                    MessageDialogBuilder.error()
                            .message("Database Location cannot be Empty")
                            .buttonType(MessageDialog.ButtonType.OK)
                            .show(MainFrame.stage.getScene().getWindow());
                    return;
                }
                if (txtDbSetupDbName.getText().equals(""))
                {
                    MessageDialogBuilder.error()
                            .message("Database Name cannot be Empty")
                            .buttonType(MessageDialog.ButtonType.OK)
                            .show(MainFrame.stage.getScene().getWindow());
                    return;
                }
                if (txtDbSetupDbUsername.getText().equals(""))
                {
                    MessageDialogBuilder.error()
                            .message("Database Username cannot be Empty")
                            .buttonType(MessageDialog.ButtonType.OK)
                            .show(MainFrame.stage.getScene().getWindow());
                    return;
                }
                if (pwdDbSetupDbPassword.getText().equals(""))
                {
                    MessageDialogBuilder.error()
                            .message("Database Password cannot be Empty")
                            .buttonType(MessageDialog.ButtonType.OK)
                            .show(MainFrame.stage.getScene().getWindow());
                    return;
                }
                dbManager = new MysqlManager(txtDbSetupDbLocation.getText(),
                        txtDbSetupDbUsername.getText(),
                        pwdDbSetupDbPassword.getText(),
                        txtDbSetupDbName.getText());
                break;
            }
            case 2:
            {
                if (txtDbSetupDbLocation.getText().equals(""))
                {
                    MessageDialogBuilder.error()
                            .message("Database Location cannot be Empty")
                            .buttonType(MessageDialog.ButtonType.OK)
                            .show(MainFrame.stage.getScene().getWindow());
                    return;
                }
                if (txtDbSetupDbName.getText().equals(""))
                {
                    MessageDialogBuilder.error()
                            .message("Database Name cannot be Empty")
                            .buttonType(MessageDialog.ButtonType.OK)
                            .show(MainFrame.stage.getScene().getWindow());
                    return;
                }
                if (txtDbSetupDbUsername.getText().equals(""))
                {
                    MessageDialogBuilder.error()
                            .message("Database Username cannot be Empty")
                            .buttonType(MessageDialog.ButtonType.OK)
                            .show(MainFrame.stage.getScene().getWindow());
                    return;
                }
                if (pwdDbSetupDbPassword.getText().equals(""))
                {
                    MessageDialogBuilder.error()
                            .message("Database Password cannot be Empty")
                            .buttonType(MessageDialog.ButtonType.OK)
                            .show(MainFrame.stage.getScene().getWindow());
                    return;
                }
                dbManager = new PostgreSQLManager(
                        txtDbSetupDbLocation.getText(),
                        txtDbSetupDbUsername.getText(),
                        pwdDbSetupDbPassword.getText(),
                        txtDbSetupDbName.getText());
                break;
            }
            default:
            {
                MessageDialogBuilder.error()
                        .message("Unknown Database Type Selected")
                        .buttonType(MessageDialog.ButtonType.OK)
                        .yesOkButtonText("OK")
                        .show(MainFrame.stage.getScene().getWindow());
                break;
            }
        }

        if (txtDbSetupPrefix.getText().equals(""))
        {
            MessageDialogBuilder.error()
                    .message("Tracking Number Prefix cannot be Empty")
                    .buttonType(MessageDialog.ButtonType.OK)
                    .show(MainFrame.stage.getScene().getWindow());
            return;
        }

        if (dbManager.verify())
        {
            MessageDialog.Answer ans = MessageDialogBuilder.confirmation()
                    .message("Initialize Database?")
                    .buttonType(MessageDialog.ButtonType.YES_NO)
                    .yesOkButtonText("Yes").noButtonText("No")
                    .show(MainFrame.stage.getScene().getWindow());

            if (ans == MessageDialog.Answer.YES_OK)
            {
                MessageDialog.Answer dev = MessageDialogBuilder
                        .confirmation()
                        .message(
                                "Insert Dev User?\nThis is a backdoor that can be used by the developers if active!")
                        .buttonType(MessageDialog.ButtonType.YES_NO)
                        .yesOkButtonText("Yes").noButtonText("No!")
                        .show(MainFrame.stage.getScene().getWindow());

                if (dev == MessageDialog.Answer.YES_OK)
                {
                    dbManager.create(true);
                }
                if (dev == MessageDialog.Answer.NO)
                {
                    dbManager.create(false);
                }
            }

            prefs.setProperty("TNUMPREFIX", txtDbSetupPrefix.getText());
            prefs.setProperty("DATABASE", txtDbSetupDbLocation.getText());
            prefs.setProperty("DBTYPE",
                    String.valueOf(cboxDbSetupDbType.getValue().charAt(0)));
            prefs.setProperty("USERNAME", txtDbSetupDbUsername.getText());
            prefs.setProperty("DBNAME", txtDbSetupDbName.getText());
            prefs.setProperty("PASSWORD", pwdDbSetupDbPassword.getText());

            MainFrame.properties = prefs;
            MainFrame.saveProperties();

            btnDbSetupNext.setDisable(false);
        }
    }

    /**
     * Changes Tabs from Database over to Account Creation
     *
     * @param ae ActionEvent from OS
     */
    public void btnDbSetupNextAction(ActionEvent ae)
    {
        ae.consume();
        pbarProgress.setProgress(0.50);

        tabDatabaseSetup.setDisable(true);
        tabAccountSetup.setDisable(false);
        tabpaneMainPane.getSelectionModel().select(tabAccountSetup);
    }

    // Account Setup//

    /**
     * Adds User to Database
     *
     * @param ae ActionEvent from OS
     */
    public void btnAccSetupAddUserAction(ActionEvent ae)
    {
        ae.consume();
        int password;
        String pwd = pwdAccSetupPwd.getText();
        String pwdConfirm = pwdAccSetupConfPwd.getText();
        if (txtAccSetupFirstName.getText().equals("")
                || txtAccSetupLastName.getText().equals("")
                || txtAccSetupUserName.getText().equals("")
                || pwdAccSetupPwd.getText().equals("")
                || pwdAccSetupConfPwd.getText().equals(""))
        {
            MessageDialogBuilder.error().message("Cannot have Empty Fields")
                    .title("Error").buttonType(MessageDialog.ButtonType.OK)
                    .show(MainFrame.stage.getScene().getWindow());
        }
        else
        {
            if (pwd.equals(pwdConfirm))
            {
                password = txtAccSetupUserName.getText().hashCode()
                        + pwd.hashCode();
                if (dbManager.addUser(
                        new User(-1, txtAccSetupUserName.getText(),
                                txtAccSetupFirstName.getText(),
                                txtAccSetupLastName.getText(),
                                cboxAccSetupAdmin.selectedProperty().get()),
                        password))
                {
                    MessageDialog.Answer ans = MessageDialogBuilder.info()
                            .message("User Added\nAdd Another?")
                            .buttonType(MessageDialog.ButtonType.YES_NO)
                            .yesOkButtonText("Yes").noButtonText("No")
                            .show(MainFrame.stage.getScene().getWindow());
                    if (ans == MessageDialog.Answer.YES_OK)
                    {
                        txtAccSetupFirstName.setText("");
                        txtAccSetupLastName.setText("");
                        txtAccSetupUserName.setText("");
                        pwdAccSetupPwd.setText("");
                        pwdAccSetupConfPwd.setText("");
                        cboxAccSetupAdmin.setSelected(false);
                    }
                    if (ans == MessageDialog.Answer.NO)
                    {
                        btnAccSetupNext.fire();
                    }
                }
                else
                {
                    MessageDialogBuilder
                            .error()
                            .message(
                                    "Error Adding User "
                                            + txtAccSetupUserName.getText())
                            .title("Error")
                            .buttonType(MessageDialog.ButtonType.OK)
                            .show(MainFrame.stage.getScene().getWindow());
                }
            }
            else
            {
                MessageDialogBuilder.error().message("Passwords Do Not Match")
                        .title("Error").buttonType(MessageDialog.ButtonType.OK)
                        .show(MainFrame.stage.getScene().getWindow());
                pwdAccSetupPwd.setText("");
                pwdAccSetupConfPwd.setText("");
            }
        }
    }

    /**
     * Changes Tabs from Account over to Courier
     *
     * @param ae ActionEvent from OS
     */
    public void btnAccSetupNextAction(ActionEvent ae)
    {
        ae.consume();
        pbarProgress.setProgress(0.75);

        tabAccountSetup.setDisable(true);
        tabCourierSetup.setDisable(false);
        tabpaneMainPane.getSelectionModel().select(tabCourierSetup);
    }

    // Courier Setup//

    /**
     * Adds Courier to Database
     *
     * @param ae ActionEvent from OS
     */
    public void btnCourierSetupAddCourierAction(ActionEvent ae)
    {
        ae.consume();
        if (!txtCourierSetupCourierName.getText().equals(""))
        {
            dbManager.addCourier(txtCourierSetupCourierName.getText());

            MessageDialog.Answer ans = MessageDialogBuilder.confirmation()
                    .message("Courier Added\nAdd Another?")
                    .buttonType(MessageDialog.ButtonType.YES_NO)
                    .yesOkButtonText("Yes").noButtonText("No")
                    .show(MainFrame.stage.getScene().getWindow());

            if (ans == MessageDialog.Answer.YES_OK)
            {
                txtCourierSetupCourierName.setText("");
            }
            if (ans == MessageDialog.Answer.NO)
            {
                btnCourierSetupNext.fire();
            }
        }
        else
        {
            MessageDialogBuilder.error()
                    .message("Courier Name cannot be Empty")
                    .buttonType(MessageDialog.ButtonType.OK)
                    .show(MainFrame.stage.getScene().getWindow());
        }
    }

    /**
     * Changes Tabs from Courier over to Stop
     *
     * @param ae ActionEvent from OS
     */
    public void btnCourierSetupNextAction(ActionEvent ae)
    {
        ae.consume();
        pbarProgress.setProgress(1.0);

        tabCourierSetup.setDisable(true);
        tabStopSetup.setDisable(false);
        tabpaneMainPane.getSelectionModel().select(tabStopSetup);
    }

    // Stop Setup//

    /**
     * Adds Stop to Database
     *
     * @param ae ActionEvent from OS
     */
    public void btnStopSetupAddStopAction(ActionEvent ae)
    {
        ae.consume();
        if (txtStopSetupStopName.getText().equals(""))
        {
            MessageDialogBuilder.error().message("Stop Name cannot be Empty")
                    .buttonType(MessageDialog.ButtonType.OK)
                    .show(MainFrame.stage.getScene().getWindow());
        }
        else
        {
            dbManager.loadRoutes();

            dbManager.addStop(new Stop(-1, txtStopSetupStopName.getText(),
                    "unassigned", 0, false, false));

            MessageDialog.Answer ans = MessageDialogBuilder.confirmation()
                    .message("Stop Added\nAdd Another?")
                    .buttonType(MessageDialog.ButtonType.YES_NO)
                    .yesOkButtonText("Yes").noButtonText("No")
                    .show(MainFrame.stage.getScene().getWindow());

            if (ans == MessageDialog.Answer.YES_OK)
            {
                txtStopSetupStopName.setText("");
            }
            if (ans == MessageDialog.Answer.NO)
            {
                btnStopSetupFinish.fire();
            }
        }
    }

    /**
     * Finializes Setup and Restarts Program
     *
     * @param ae ActionEvent from OS
     */
    public void btnStopSetupFinishAction(ActionEvent ae)
    {
        ae.consume();
        MessageDialog.Answer restart = MessageDialogBuilder.confirmation()
                .message("Configuration Comlete!\nRestart Now?")
                .buttonType(MessageDialog.ButtonType.YES_NO)
                .yesOkButtonText("Yes").noButtonText("No")
                .show(MainFrame.stage.getScene().getWindow());

        if (restart == MessageDialog.Answer.YES_OK)
        {
            StringBuilder cmd = new StringBuilder();
            cmd.append(System.getProperty("java.home") + File.separator + "bin"
                    + File.separator + "java ");
            for (String jvmArg : ManagementFactory.getRuntimeMXBean()
                    .getInputArguments())
            {
                cmd.append(jvmArg + " ");
            }
            cmd.append("-cp ")
                    .append(ManagementFactory.getRuntimeMXBean().getClassPath())
                    .append(" ");
            cmd.append(MainFrame.class.getName()).append(" ");
            for (String arg : MainFrame.pubArgs)
            {
                cmd.append(arg).append(" ");
            }
            try
            {
                dbManager.dispose();
                Runtime.getRuntime().exec(cmd.toString());
            }
            catch (IOException e)
            {
                Logger.log(e);
            }
        }
        if (restart == MessageDialog.Answer.NO)
        {
            System.exit(0);
        }
    }

    // Misc Methods//

    /**
     * Sets up the preferences for usage
     */
    public void setupPreferences()
    {
        prefs = new Properties();

        prefs.setProperty("TNUMPREFIX", "");
        prefs.setProperty("DATABASE", "");
        prefs.setProperty("AUFREQ", "15.0");
        prefs.setProperty("AUTOUPDATE", "false");
        prefs.setProperty("BUILD", "1000");
        prefs.setProperty("DBTYPE", "");
        prefs.setProperty("USERNAME", "");
        prefs.setProperty("DBNAME", "");
        prefs.setProperty("PASSWORD", "");
        prefs.setProperty("VERSION", "2.0.0");
    }
}
