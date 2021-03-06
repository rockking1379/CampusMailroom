package com.mailroom.mainclient;

import com.mailroom.common.database.DatabaseManager;
import com.mailroom.common.database.DatabaseManagerFactory;
import com.mailroom.common.objects.Package;
import com.mailroom.common.objects.Route;
import com.mailroom.common.objects.Stop;
import com.mailroom.common.utils.Logger;
import com.panemu.tiwulfx.dialog.MessageDialog;
import com.panemu.tiwulfx.dialog.MessageDialogBuilder;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.FlowPane;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.*;
import java.awt.*;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * Controls PrintPageFx.fxml in com.mailroom.fxml.mainclient
 *
 * @author James rockking1379@gmail.com
 */
public class PrintPageController implements Initializable
{
    private DatabaseManager dbManager;
    private ArrayList<CheckBox> routeBoxes;

    @FXML
    private FlowPane flowScrollRoutes;
    @FXML
    private TextArea txtAreaReport;
    @FXML
    private Button btnPrintReport;
    private ArrayList<String> strReport;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1)
    {
        this.dbManager = DatabaseManagerFactory.getInstance();
        Logger.logEvent("Loading Printing Screen", "SYSTEM");
        dbManager.loadRoutes();

        routeBoxes = new ArrayList<CheckBox>();

        for (Route r : dbManager.getRoutes())
        {
            CheckBox c = new CheckBox();

            c.setText(r.getRouteName());
            c.setSelected(false);
            c.setVisible(true);

            c.setMinWidth(150);
            c.setMinHeight(50);

            c.setFocusTraversable(true);

            routeBoxes.add(c);
        }

        flowScrollRoutes.getChildren().clear();
        flowScrollRoutes.getChildren().addAll(routeBoxes);

        for (Node c : flowScrollRoutes.getChildren())
        {
            c.setLayoutX(c.getLayoutX() + 10);
        }

        flowScrollRoutes.getChildren().get(0).requestFocus();
    }

    /**
     * Processes keyboard input
     *
     * @param ke KeyEvent from OS
     */
    public void keyPressedAction(KeyEvent ke)
    {
        if (ke.getCode() == KeyCode.ESCAPE)
        {
            boolean verified = true;

            for (CheckBox c : routeBoxes)
            {
                if (c.isSelected())
                {
                    verified = false;
                }
            }

            if (!verified)
            {
                MessageDialog.Answer exit = MessageDialogBuilder.confirmation()
                        .message("Exit to Open Screen?").title("Confirm")
                        .buttonType(MessageDialog.ButtonType.YES_NO)
                        .yesOkButtonText("Yes").noButtonText("No")
                        .show(MainFrame.stage.getScene().getWindow());

                if (exit == MessageDialog.Answer.YES_OK)
                {
                    Logger.logEvent("Exiting Printing Screen", "SYSTEM");
                    try
                    {
                        Parent root = FXMLLoader
                                .load(getClass()
                                        .getResource(
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
            else
            {
                Logger.logEvent("Exiting Printing Screen", "SYSTEM");
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
    }

    /**
     * Creates Report for Printing
     *
     * @param ae ActionEvent from OS
     */
    public void btnCreateReportAction(ActionEvent ae)
    {
        ae.consume();
        Logger.logEvent("Generating Package Report", MainFrame.cUser.getUserName());
        txtAreaReport.setText("");
        strReport = new ArrayList<String>();

        for (CheckBox c : routeBoxes)
        {
            if (c.isSelected())
            {
                String head = "";

                for (int i = 0; i <= 15; i++)
                {
                    head += " ";
                }

                head += "Route: ";
                head += c.getText() + " ";

                for (Route r : dbManager.getRoutes())
                {
                    boolean added = false;
                    if (r.getRouteName().equals(c.getText()))
                    {
                        for (Stop s : dbManager.getStopsOnRoute(r))
                        {
                            ArrayList<Package> packages = (ArrayList<Package>) dbManager
                                    .printPackagesForStop(s);

                            if (packages.size() > 0)
                            {
                                if (!added)
                                {
                                    strReport.add(head);
                                    added = true;
                                }

                                String stopHead = "";
                                String last = "Last      ";
                                String first = "First     ";
                                String box = "Box#      ";
                                String track = "Track#    ";
                                String sign = "Sign Here";

                                stopHead += "\nPackage Delivery for ";
                                stopHead += s.getStopName();
                                stopHead += "	Date: ";

                                Date d = new Date();
                                SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
                                stopHead += format.format(d);
                                strReport.add(stopHead);
                                stopHead = "";
                                stopHead += last;
                                stopHead += first;
                                stopHead += box;
                                stopHead += track;
                                stopHead += sign;
                                strReport.add(stopHead);

                                for (Package p : packages)
                                {
                                    String strPackage = "";
                                    strPackage += p.getLastName();
                                    for (int i = 0; i < last.length()
                                            - p.getLastName().length(); i++)
                                    {
                                        strPackage += " ";
                                    }
                                    strPackage += p.getFirstName();
                                    for (int i = 0; i < first.length()
                                            - p.getFirstName().length(); i++)
                                    {
                                        strPackage += " ";
                                    }
                                    strPackage += p.getBoxOffice();
                                    for (int i = 0; i < box.length()
                                            - p.getBoxOffice().length(); i++)
                                    {
                                        strPackage += " ";
                                    }
                                    strPackage += p.getTrackingNumber()
                                            .substring(
                                                    3,
                                                    p.getTrackingNumber()
                                                            .length());
                                    for (int i = 0; i < track.length()
                                            - p.getTrackingNumber().length(); i++)
                                    {
                                        strPackage += " ";
                                    }
                                    strPackage += "_____________________________________";

                                    strReport.add(strPackage);
                                }
                            }
                        }
                    }
                }
            }
        }

        for (String s : strReport)
        {
            txtAreaReport.setText(txtAreaReport.getText() + s + "\n");
        }

        btnPrintReport.setDisable(false);
    }

    /**
     * Queues job for printing
     * and then prints report
     *
     * @param ae ActionEvent from OS
     */
    public void btnPrintReportAction(ActionEvent ae)
    {
        ae.consume();
        Logger.logEvent("Printing Package Report", MainFrame.cUser.getUserName());
        File dir = new File("./Prints");

        if (!dir.exists())
        {
            boolean dirResult = dir.mkdir();

            if (dirResult)
            {
                System.out.println("Directory Created");
            }
        }

        Date d = new Date();
        SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy");
        String fileName = "";

        fileName += "./Prints/";
        fileName += format.format(d);
        fileName += "-";
        fileName += String.valueOf(d.getTime());
        fileName += ".txt";

        File f = new File(fileName);

        if (!f.exists())
        {
            try
            {
                boolean fileResult = f.createNewFile();

                if (fileResult)
                {
                    FileOutputStream ostream = new FileOutputStream(
                            f.getAbsolutePath());
                    OutputStreamWriter owriter = new OutputStreamWriter(ostream,
                            "UTF-8");

                    for (String s : strReport)
                    {
                        owriter.write(s + "\n");
                    }

                    owriter.close();
                    ostream.close();

                    JTextArea jtext = new JTextArea();
                    jtext.setSize(470, 277);
                    jtext.setText("");
                    jtext.setFont(new Font("Monospaced", Font.PLAIN, 12));
                    for (String s : strReport)
                    {
                        jtext.setText(jtext.getText() + s + "\n");
                    }

                    try
                    {
                        if (jtext.print())
                        {
                            //Email Stuff....damn
                            if (Boolean.valueOf(MainFrame.properties.getProperty("EMAILENABLE")))
                            {
                                if (MainFrame.properties.getProperty("EMAILSEND").equals("PRINT"))
                                {
                                    Logger.logEvent("Email Notification Enabled!\nSending Now!", "SYSTEM");
                                    try
                                    {
                                        Properties props = new Properties();
                                        props.put("mail.smtp.starttls.enable", "true");
                                        props.put("mail.smtp.host", MainFrame.properties.getProperty("EMAILHOST"));
                                        props.put("mail.smtp.port", MainFrame.properties.getProperty("EMAILPORT"));
                                        Session sess;

                                        if (MainFrame.properties.getProperty("EMAILAUTHREQ").equalsIgnoreCase("false"))
                                        {
                                            props.put("mail.smtp.auth", "false");
                                            sess = Session.getDefaultInstance(props);
                                        }
                                        else
                                        {
                                            props.put("mail.smtp.auth", "true");
                                            sess = Session.getInstance(props, new Authenticator()
                                            {
                                                protected PasswordAuthentication getPasswordAuthentication()
                                                {
                                                    return new PasswordAuthentication(MainFrame.properties.getProperty("EMAILUSERNAME"), MainFrame.properties.getProperty("EMAILPASSWORD"));
                                                }
                                            });
                                        }
                                        MimeMessage message = new MimeMessage(sess);
                                        boolean sendEmail = false;

                                        Address[] a = new Address[1];
                                        a[0] = new InternetAddress(MainFrame.properties.getProperty("EMAILREPLYTO"));
                                        message.setReplyTo(a);
                                        message.setFrom(new InternetAddress(MainFrame.properties.getProperty("EMAILREPLYTO")));
                                        message.setSubject("Package Delivery Notice");
                                        message.setText(MainFrame.properties.getProperty("EMAILMESSAGE"));

                                        for (CheckBox c : routeBoxes)
                                        {
                                            if (c.isSelected())
                                            {
                                                for (Route r : dbManager.getRoutes())
                                                {
                                                    if (r.getRouteName().equals(c.getText()))
                                                    {
                                                        for (Stop s : dbManager.getStopsOnRoute(r))
                                                        {
                                                            if (dbManager.getPackagesForStop(s).size() > 0)
                                                            {
                                                                ArrayList<String> addresses = (ArrayList<String>) dbManager.getEmailAddress(s);
                                                                if (addresses.size() > 0)
                                                                {
                                                                    sendEmail = true;
                                                                    for (String str : addresses)
                                                                    {
                                                                        message.addRecipients(Message.RecipientType.BCC, str);
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }

                                        if (sendEmail)
                                        {
                                            Transport.send(message);
                                        }
                                    }
                                    catch (MessagingException e)
                                    {
                                        Logger.logException(e);
                                        MessageDialogBuilder.error().message("Error Sending Email").title("ERROR").buttonType(MessageDialog.ButtonType.OK).show(MainFrame.stage.getScene().getWindow());
                                    }
                                }
                            }
                            Logger.logEvent("Auto Removing Packages", "SYSTEM");
                            //auto remove packages...oh boy
                            for (CheckBox c : routeBoxes)
                            {
                                if (c.isSelected())
                                {
                                    for (Route r : dbManager.getRoutes())
                                    {
                                        if (r.getRouteName().equals(c.getText()))
                                        {
                                            for (Stop s : dbManager.getStopsOnRoute(r))
                                            {
                                                if (s.getAutoRemove())
                                                {
                                                    for (Package p : dbManager.getPackagesForStop(s))
                                                    {
                                                        dbManager.updatePackage(p.getPackageId(), true, true);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    catch (PrinterException e)
                    {
                        Logger.logException(e);
                        MessageDialogBuilder.error().message("Error Printing").title("ERROR").buttonType(MessageDialog.ButtonType.OK).show(MainFrame.stage.getScene().getWindow());
                    }
                }
            }
            catch (IOException e)
            {
                Logger.logException(e);
            }
        }

        if (MessageDialogBuilder.confirmation().message("Exit to Open Screen?")
                .title("Confirm").buttonType(MessageDialog.ButtonType.YES_NO)
                .yesOkButtonText("Yes")
                .show(MainFrame.stage.getScene().getWindow()) == MessageDialog.Answer.YES_OK)
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
        else
        {
            for (CheckBox c : routeBoxes)
            {
                c.setSelected(false);
            }
        }
    }

    public static boolean printReport(ObservableList<Package> toPrint)
    {
        Logger.logEvent("Special Report Generating", MainFrame.cUser.getUserName());
        ArrayList<String> strReport = new ArrayList<String>();

        String head = "";

        for (int i = 0; i <= 15; i++)
        {
            head += " ";
        }

        head += "Route: ";
        head += "Custom Print" + " ";
        strReport.add(head);

        String stopHead = "";
        String last = "Last      ";
        String first = "First     ";
        String box = "Box#      ";
        String track = "Track#    ";
        String sign = "Sign Here";

        stopHead += "\nPackage Delivery for ";
        stopHead += "Special";
        stopHead += "	Date: ";

        Date d = new Date();
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        stopHead += format.format(d);
        strReport.add(stopHead);
        stopHead = "";
        stopHead += last;
        stopHead += first;
        stopHead += box;
        stopHead += track;
        stopHead += sign;
        strReport.add(stopHead);

        for (Package p : toPrint)
        {
            String strPackage = "";
            strPackage += p.getLastName();
            for (int i = 0; i < last.length()
                    - p.getLastName().length(); i++)
            {
                strPackage += " ";
            }
            strPackage += p.getFirstName();
            for (int i = 0; i < first.length()
                    - p.getFirstName().length(); i++)
            {
                strPackage += " ";
            }
            strPackage += p.getBoxOffice();
            for (int i = 0; i < box.length()
                    - p.getBoxOffice().length(); i++)
            {
                strPackage += " ";
            }
            strPackage += p.getTrackingNumber()
                    .substring(
                            3,
                            p.getTrackingNumber()
                                    .length());
            for (int i = 0; i < track.length()
                    - p.getTrackingNumber().length(); i++)
            {
                strPackage += " ";
            }
            strPackage += "_____________________________________";

            strReport.add(strPackage);
        }

        JTextArea txtArea = new JTextArea();
        txtArea.setSize(470, 277);
        txtArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        txtArea.setText("");

        for (String s : strReport)
        {
            txtArea.setText(txtArea.getText() + s + "\n");
        }
        Logger.logEvent("Sending Special Report to Printer", "SYSTEM");
        try
        {
            return txtArea.print();
        }
        catch (PrinterException e)
        {
            Logger.logException(e);
            return false;
        }
    }
}
