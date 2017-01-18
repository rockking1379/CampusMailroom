package com.mailroom.common.utils;

import com.mailroom.mainclient.MainFrame;
import com.panemu.tiwulfx.dialog.MessageDialog;
import com.panemu.tiwulfx.dialog.MessageDialogBuilder;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.net.ssl.HttpsURLConnection;
import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Properties;

/**
 * Checks against update server to see if newer version is available
 * Created by James on 5/26/2015.
 */
public class UpdateChecker implements Runnable
{
    private static final String SERVER_ADDRESS = "https://update.codegeekhosting.me";
    private static final int SERVER_PORT = 3000;
    private static final String PRODUCT_GUID = "ADAMSMAIL";
    Version version = new Version();
    private Stage stg;

    public UpdateChecker(Stage stg)
    {
        this.stg = stg;
    }

    @Override
    public void run()
    {
        Logger.logEvent("Checking For Updates", "UPDATER");
        try
        {
            String address = SERVER_ADDRESS + ":" + String.valueOf(SERVER_PORT) + "/products/" + PRODUCT_GUID;

            URL url = new URL(address);
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Accept", "application/json");
            Logger.logEvent("Connecting to Update Server", "UPDATER");
            con.connect();

            int response = con.getResponseCode();

            switch (response)
            {
                case HttpsURLConnection.HTTP_OK:
                {
                    String curVersion = version.getVersion();

                    InputStreamReader isr = new InputStreamReader(con.getInputStream());
                    BufferedReader br = new BufferedReader(isr);
                    String json = br.readLine();
                    JSONParser parser = new JSONParser();
                    Object obj = parser.parse(json);
                    JSONObject jObject = (JSONObject) obj;
                    JSONObject data = (JSONObject) jObject.get("data");
                    JSONObject version = (JSONObject) data.get("version");

                    Object[] objects = {version.get("major"), version.get("minor"), version.get("revision")};

                    String newVersion = versionMaker(objects);

                    if (!curVersion.equals(newVersion))
                    {
                        Logger.logEvent("Update Checking Complete! Updates Available", "UPDATER");

                        Notifications notificationBuilder = Notifications.create();
                        notificationBuilder.title("Upgrade Available");
                        notificationBuilder.text("A Newer Version is available.\nClick To Upgrade Now");
                        notificationBuilder.position(Pos.TOP_CENTER);
                        notificationBuilder.hideAfter(new Duration(5000));
                        notificationBuilder.onAction(new EventHandler<ActionEvent>()
                        {
                            @Override
                            public void handle(ActionEvent ae)
                            {
                                ae.consume();

                                launchUpdater();
                            }
                        });
                        notificationBuilder.owner(stg);
                        notificationBuilder.darkStyle();
                        notificationBuilder.showInformation();
                    }
                    else
                    {
                        Logger.logEvent("Update Checking Complete! No Updates Available", "UPDATER");
                    }

                    break;
                }
                case HttpsURLConnection.HTTP_NOT_FOUND:
                {
                    Logger.logException(new Exception("Error Connecting to Update Server"));
                    break;
                }
                default:
                {
                    Logger.logException(new Exception("HTTP Error Checking for Updates"));
                    break;
                }
            }
        }
        catch (MalformedURLException mue)
        {
            Logger.logException(mue);
        }
        catch (ProtocolException e)
        {
            Logger.logException(e);
        }
        catch (IOException e)
        {
            Logger.logException(e);
        }
        catch (ParseException e)
        {
            Logger.logException(e);
        }
        catch (UnsupportedClassVersionError error)
        {
            if (MainFrame.stage != null)
            {
                MessageDialog.Answer answer = MessageDialogBuilder.info()
                        .message("A Newer Version is Availabe\nUpgrade Now?")
                        .title("Upgrade Available")
                        .buttonType(MessageDialog.ButtonType.YES_NO)
                        .yesOkButtonText("Yes")
                        .show(MainFrame.stage.getScene().getWindow());

                if (answer == MessageDialog.Answer.YES_OK)
                {
                    launchUpdater();
                }
            }
        }
        catch (IllegalStateException e)
        {
            //ignore this
        }
        catch (NullPointerException e)
        {
            //ignore this too
        }
    }

    private void launchUpdater()
    {
        try
        {
            File f = new File("./Updater.jar");
            String systemName = System.getProperty("os.name");
            MainFrame.saveProperties();

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

            System.exit(0);
        }
        catch (IOException e)
        {
            Logger.logException(e);
            MessageDialogBuilder.error()
                    .title("Error")
                    .message("Error Starting Updater")
                    .buttonType(MessageDialog.ButtonType.OK)
                    .show(MainFrame.stage.getScene().getWindow());
        }
    }

    public static String versionMaker(Object[] values)
    {
        String res = "";

        for (int i = 0; i < values.length; i++)
        {
            if (i < values.length - 1)
            {
                res += String.valueOf(values[i]) + ".";
            }
            else
            {
                res += String.valueOf(values[i]);
            }
        }

        return res;
    }

    public static String getProductGuid()
    {
        return PRODUCT_GUID;
    }

    public static String getServerAddress()
    {
        return SERVER_ADDRESS;
    }

    public static int getServerPort()
    {
        return SERVER_PORT;
    }
}
