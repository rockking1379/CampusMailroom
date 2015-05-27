package com.mailroom.updater;

import com.mailroom.common.gui.UpdateChecker;
import com.mailroom.common.utils.Logger;
import com.panemu.tiwulfx.dialog.MessageDialog;
import com.panemu.tiwulfx.dialog.MessageDialogBuilder;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * Controls UpdaterFx.fxml in com.mailroom.fxml.updater
 *
 * @author James rockking1379@gmail.com
 */
public class UpdaterController implements Initializable
{
    Properties properties;

    @FXML
    private ProgressIndicator pindicatorProgress;
    @FXML
    private Label lblDownload;
    @FXML
    private Label lblWaiting;
    @FXML
    private Label lblFinished;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnExit;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1)
    {
    }

    /**
     * Action Handler for Update Button
     *
     * @param ae Action Event passed by Button
     */
    public void btnUpdateAction(ActionEvent ae)
    {
        ae.consume();
//        pindicatorProgress.setProgress(-1);
        lblDownload.setVisible(true);
        lblWaiting.setVisible(true);
        lblFinished.setVisible(false);
        btnUpdate.setVisible(false);
        btnExit.setVisible(false);

        Platform.runLater(new Updater());
    }

    /**
     * Action Handler for Exit Button
     *
     * @param ae Action Event passed by Button
     */
    public void btnExitAction(ActionEvent ae)
    {
        ae.consume();
        pindicatorProgress.setProgress(1);
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

        System.exit(0);
    }

    /**
     * Handles all the updating logic <br>
     * Spins off to sperate thread so it doesnt disrupt UI
     *
     * @author James
     */
    private class Updater implements Runnable
    {
        /**
         * Main Updater Method <br>
         * Where all real logic happens
         */
        @Override
        public void run()
        {
            try
            {
                properties = new Properties();
                File prop = new File("./configuration.properties");

                if (prop.exists())
                {
                    // Load the Configuration
                    FileInputStream fStream = new FileInputStream(prop);
                    properties.load(fStream);

                    // Once done, move on to remote file retrieval/processing
                    try
                    {
                        String sUrl = UpdateChecker.getServerAddress()
                                + ":" + String.valueOf(UpdateChecker.getServerPort());

                        URL url = new URL(sUrl + "/products/" + UpdateChecker.getProductGuid());
                        HttpURLConnection con = (HttpURLConnection) url
                                .openConnection();
                        con.setRequestMethod("GET");
                        con.setRequestProperty("Accept", "application/json");
                        con.connect();

                        int response = con.getResponseCode();

                        // used to make it easy to handle other response codes
                        // only one really care about is OK(200)
                        switch (response)
                        {
                            case HttpURLConnection.HTTP_OK:
                            {
                                InputStreamReader isr = new InputStreamReader(
                                        con.getInputStream());
                                BufferedReader br = new BufferedReader(isr);
                                String json = br.readLine();
                                JSONParser parser = new JSONParser();
                                Object obj = parser.parse(json);
                                JSONObject jObject = (JSONObject) obj;
                                JSONObject data = (JSONObject) jObject.get("data");
                                JSONObject version = (JSONObject) data.get("version");

                                Object[] objects = {version.get("major"), version.get("minor"), version.get("revision")};
                                String availVersion = UpdateChecker.versionMaker(objects);
                                br.close();
                                isr.close();
                                con.disconnect();

                                url = new URL(sUrl + "/files/" + UpdateChecker.getProductGuid()
                                        + "/" + availVersion);
                                con = (HttpURLConnection) url.openConnection();
                                con.setRequestMethod("GET");
                                con.setRequestProperty("Accept", "application/json");
                                con.connect();

                                isr = new InputStreamReader(
                                        con.getInputStream());
                                br = new BufferedReader(isr);
                                json = br.readLine();

                                obj = parser.parse(json);
                                jObject = (JSONObject) obj;
                                data = (JSONObject) jObject.get("data");
                                JSONArray files = (JSONArray) data.get("files");
                                for (int i = 0; i < files.size(); i++)
                                {
                                    String fileName = files.get(i).toString();
                                    String urlFileName = fileName.replace(" ", "%20");
                                    URL jar = new URL(sUrl + "/files/" + UpdateChecker.getProductGuid() + "/" + availVersion + "/" + urlFileName);
                                    ReadableByteChannel rbc = Channels
                                            .newChannel(jar.openStream());
                                    FileOutputStream fos = new FileOutputStream(
                                            "./" + fileName);
                                    fos.getChannel().transferFrom(rbc, 0,
                                            Long.MAX_VALUE);
                                    fos.close();
                                    rbc.close();

                                    pindicatorProgress.setProgress((double) i / (double) files.size());
                                }

                                properties.setProperty("VERSION", availVersion);

                                FileOutputStream oStream = new FileOutputStream(
                                        prop);
                                properties.store(oStream,
                                        "System Configuration");
                                oStream.close();

                                lblDownload.setVisible(false);
                                lblWaiting.setVisible(false);
                                lblFinished.setVisible(true);
                                btnExit.setVisible(true);
                                btnUpdate.setVisible(false);
                                try
                                {
                                    pindicatorProgress.setVisible(false);
                                    pindicatorProgress.setProgress(1.0);
                                }
                                catch (IllegalStateException e)
                                {
                                    Logger.logException(e);
                                }

                                break;
                            }
                            case HttpURLConnection.HTTP_NOT_FOUND:
                            {
                                MessageDialogBuilder
                                        .error()
                                        .message(
                                                "Error Connecting to Update Server")
                                        .buttonType(MessageDialog.ButtonType.OK)
                                        .show(null);
                                System.exit(-1);
                                break;
                            }
                        }
                    }
                    catch (MalformedURLException e)
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
                }
                else
                {
                    MessageDialogBuilder
                            .error()
                            .message(
                                    "No Config Found!\nPlease Run MainClient Program First")
                            .buttonType(MessageDialog.ButtonType.OK).show(null);
                    System.exit(-1);
                }
            }
            catch (IOException e)
            {
                Logger.logException(e);
                System.exit(-1);
            }
        }
    }
}
