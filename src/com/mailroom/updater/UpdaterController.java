package com.mailroom.updater;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Properties;
import java.util.ResourceBundle;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.mailroom.mainclient.MainFrame;
import com.panemu.tiwulfx.dialog.MessageDialog;
import com.panemu.tiwulfx.dialog.MessageDialogBuilder;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;

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
	
	public void btnUpdateAction(ActionEvent ae)
	{		
		pindicatorProgress.setProgress(-1);
		lblDownload.setVisible(true);
		lblWaiting.setVisible(true);
		
		new updater();
	}
	
	public void btnExitAction(ActionEvent ae)
	{
		pindicatorProgress.setProgress(1);
		System.exit(1);
	}
	
	private class updater implements Runnable
	{
		private Thread t;
		
		public updater()
		{
			t = new Thread(this);
			t.start();
		}
		
		@Override
		public void run()
		{
			try
			{
				properties = new Properties();
				File propFile = new File("./configuration.properties");
				
				if(propFile.exists())
				{
					FileInputStream file = new FileInputStream(propFile);
					properties.load(file);
					
					file.close();
					
					try
					{
						URL url = new URL("http://minecraft.math.adams.edu/ptracker/version.php");
						HttpURLConnection con = (HttpURLConnection) url.openConnection();
						con.setRequestMethod("GET");
						con.setRequestProperty("Accept",  "txt/plain");
						con.connect();
						
						int statusCode = con.getResponseCode();
						
						if(statusCode == HttpURLConnection.HTTP_OK)
						{
							InputStreamReader isr = new InputStreamReader(con.getInputStream());
							BufferedReader br = new BufferedReader(isr);
							String json = br.readLine();
							
							JSONParser parser = new JSONParser();
							
							Object obj = parser.parse(json);
							JSONObject version = (JSONObject) obj;
							
							String availVersion = version.get("major") + "." + version.get("minor") + "." + version.get("revision");
							br.close();
							isr.close();
							con.disconnect();
							
							URL mainclient = new URL("http://minecraft.math.adams.edu/ptracker/" + availVersion + "/MainClient.jar");
							ReadableByteChannel rbc = Channels.newChannel(mainclient.openStream());
							FileOutputStream fos = new FileOutputStream("./MainClient.jar");
							fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
							fos.close();
							rbc.close();
							
							URL otherclient = new URL("http://minecraft.math.adams.edu/ptracker/" + availVersion + "/OtherClient.jar");
							rbc = Channels.newChannel(otherclient.openStream());
							fos = new FileOutputStream("./OtherClient.jar");
							fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
							fos.close();
							rbc.close();
							
							properties.setProperty("VERSION", availVersion);
							
							try
							{
								FileOutputStream oStream = new FileOutputStream(propFile);
								properties.store(oStream, "System Configuration");
								oStream.close();
							}
							catch(IOException e)
							{
								System.err.println("Error: " + e.getMessage());
							}
						}
						else
						{
							MessageDialogBuilder.error().message("Error Reaching Update Server").title("Error").buttonType(MessageDialog.ButtonType.OK).show(MainFrame.stage.getScene().getWindow());
						}
					}
					catch (MalformedURLException e)
					{
						System.err.println("Error: " + e.getMessage());
					}
					catch(IOException e)
					{
						System.err.println("Error: " + e.getMessage());
					}
					catch (ParseException e)
					{
						System.err.println("Error: " + e.getMessage());
					}
				}
				else
				{
					MessageDialogBuilder.error().message("No Config Found!\nPlease Run Other Program First").buttonType(MessageDialog.ButtonType.OK).show(null);
					System.exit(-1);
				}
			}
			catch(IOException e)
			{
				System.err.println("Error: " + e.getMessage());
				System.exit(-1);
			}
			
			lblDownload.setVisible(false);
			lblWaiting.setVisible(false);
			lblFinished.setVisible(true);
			pindicatorProgress.setVisible(false);
			btnExit.setVisible(true);
		}
	}

}
