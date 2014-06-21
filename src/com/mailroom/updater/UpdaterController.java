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

import com.panemu.tiwulfx.dialog.MessageDialog;
import com.panemu.tiwulfx.dialog.MessageDialogBuilder;
import com.mailroom.common.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;

/**
 * Controls UpdaterFx.fxml in com.mailroom.fxml.updater
 * @author James
 *
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
	 * @param ae Action Event passed by Button
	 */
	public void btnUpdateAction(ActionEvent ae)
	{		
		pindicatorProgress.setProgress(-1);
		lblDownload.setVisible(true);
		lblWaiting.setVisible(true);
		lblFinished.setVisible(false);
		btnExit.setVisible(false);
		
		new updater();
	}
	
	/**
	 * Action Handler for Exit Button
	 * @param ae Action Event passed by Button
	 */
	public void btnExitAction(ActionEvent ae)
	{
		pindicatorProgress.setProgress(1);
		System.exit(1);
	}
	
	/**
	 * Handles all the updating logic
	 * <br>
	 * Spins off to sperate thread so it doesnt disrupt UI
	 * @author James
	 *
	 */
	private class updater implements Runnable
	{
		private Thread t;
		
		/**
		 * Constructor
		 */
		public updater()
		{
			t = new Thread(this);
			t.start();
		}
		
		/**
		 * Main Updater Method
		 * <br>
		 * Where all real logic happens
		 */
		@Override
		public void run()
		{
			try
			{
				properties = new Properties();
				File prop = new File("./configuration.properties");
				
				if(prop.exists())
				{
					//Load the Configuration
					FileInputStream fStream = new FileInputStream(prop);
					properties.load(fStream);
					
					//Once done, move on to remote file retrieval/processing
					try
					{
						URL url = new URL("http://minecraft.math.adams.edu/ptracker/version.php");
						HttpURLConnection con = (HttpURLConnection) url.openConnection();
						con.setRequestMethod("GET");
						con.setRequestProperty("Accept", "txt/plain");
						con.connect();
						
						int response = con.getResponseCode();
						
						//used to make it easy to handle other response codes
						//only one really care about is OK(200)
						switch(response)
						{
							case HttpURLConnection.HTTP_OK:
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
								
								url = new URL("http://minecraft.math.adams.edu/ptracker/" + availVersion + "/files.php");
								con = (HttpURLConnection) url.openConnection();
								con.setRequestMethod("GET");
								con.setRequestProperty("Accept", "txt/plain");
								con.connect();
								
								isr = new InputStreamReader(con.getInputStream());
								br = new BufferedReader(isr);
								json = br.readLine();
								
								obj = parser.parse(json);
								JSONObject files = (JSONObject) obj;
								
								int fileCount = Integer.valueOf(files.get("filecount").toString());
								
								for(int i=0; i<fileCount; i++)
								{
									String fileName = files.get(String.valueOf(i)).toString();
									URL jar = new URL("http://minecraft.math.adams.edu/ptracker/" + availVersion + "/" + fileName);									
									ReadableByteChannel rbc = Channels.newChannel(jar.openStream());
									FileOutputStream fos = new FileOutputStream("./" + fileName);
									fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
									fos.close();
									rbc.close();
								}
								
								properties.setProperty("VERSION", availVersion);
								properties.setProperty("BUILD", version.get("build").toString());
								
								FileOutputStream oStream = new FileOutputStream(prop);
								properties.store(oStream, "System Configuration");
								oStream.close();
								
								try
								{
									lblDownload.setVisible(false);
								}
								catch(IllegalStateException ise)
								{
									Logger.log(ise);
								}
								try
								{
									lblWaiting.setVisible(false);
								}
								catch(IllegalStateException ise)
								{
									Logger.log(ise);
								}
								try
								{
									lblFinished.setVisible(true);
								}
								catch(IllegalStateException ise)
								{
									Logger.log(ise);
								}
								try
								{
									pindicatorProgress.setVisible(false);
								}
								catch(IllegalStateException ise)
								{
									Logger.log(ise);
								}
								try
								{	
									btnExit.setVisible(true);
								}
								catch(IllegalStateException ise)
								{
									Logger.log(ise);
								}
								try
								{
									btnUpdate.setVisible(false);
								}
								catch(IllegalStateException ise)
								{
									Logger.log(ise);
								}
								break;
							}
							case HttpURLConnection.HTTP_NOT_FOUND:
							{
								MessageDialogBuilder.error().message("Error Connecting to Update Server").buttonType(MessageDialog.ButtonType.OK).show(null);
								System.exit(-1);
								break;
							}
						}
					}
					catch(MalformedURLException e)
					{
						Logger.log(e);
					}
					catch(IOException e)
					{
						Logger.log(e);
					}
					catch(ParseException e)
					{
						Logger.log(e);
					}
				}
				else
				{
					MessageDialogBuilder.error().message("No Config Found!\nPlease Run MainClient Program First").buttonType(MessageDialog.ButtonType.OK).show(null);
					System.exit(-1);
				}
			}
			catch(IOException e)
			{
				Logger.log(e);
				System.exit(-1);
			}
		}
	}

}
