package com.mailroom.mainclient;

import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

import com.mailroom.common.*;
import com.mailroom.common.Package;
import com.panemu.tiwulfx.dialog.MessageDialog;
import com.panemu.tiwulfx.dialog.MessageDialogBuilder;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.FlowPane;

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
	
	private String strReport;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{
		this.dbManager = MainFrame.dbManager;
		
		dbManager.loadRoutes();
		
		routeBoxes = new ArrayList<CheckBox>();
		
		for(Route r : dbManager.getRoutes())
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
		
		for(Node c : flowScrollRoutes.getChildren())
		{
			c.setLayoutX(c.getLayoutX() + 10);
		}
		
		flowScrollRoutes.getChildren().get(0).requestFocus();
	}
	
	public void keyPressedAction(KeyEvent ke)
	{
		if(ke.getCode() == KeyCode.ESCAPE)
		{
			boolean verified = true;
			
			for(CheckBox c : routeBoxes)
			{
				if(c.isSelected())
				{
					verified = false;
				}
			}
			
			if(!verified)
			{
				MessageDialog.Answer exit = MessageDialogBuilder.confirmation().message("Exit to Open Screen?").title("Confirm").buttonType(MessageDialog.ButtonType.YES_NO).yesOkButtonText("Yes").noButtonText("No").show(MainFrame.stage.getScene().getWindow());
				
				if(exit == MessageDialog.Answer.YES_OK)
				{
					try
					{
						Parent root = FXMLLoader.load(getClass().getResource("/com/mailroom/fxml/mainclient/OpenPageFx.fxml"));
						Scene scene = new Scene(root);
						MainFrame.stage.setScene(scene);
					}
					catch(IOException e)
					{
						System.err.println("Error: " + e.getMessage());
					}
				}
			}
			else
			{
				try
				{
					Parent root = FXMLLoader.load(getClass().getResource("/com/mailroom/fxml/mainclient/OpenPageFx.fxml"));
					Scene scene = new Scene(root);
					MainFrame.stage.setScene(scene);
				}
				catch(IOException e)
				{
					System.err.println("Error: " + e.getMessage());
				}
			}
		}
	}

	public void btnCreateReportAction(ActionEvent ae)
	{
		txtAreaReport.setText("");
		strReport = "";
		
		for(CheckBox c : routeBoxes)
		{
			if(c.isSelected())
			{
				String head = "";
				
				for(int i=0; i<=25;i++)
				{
					head += " ";
				}
				
				head += "Route: ";
				head += c.getText() + " ";
				head += "Date: ";
				
				Date d = new Date();
				SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
				head += format.format(d).toString();
				head += "\n";
				
				txtAreaReport.setText(txtAreaReport.getText() + head);
				
				for(Route r : dbManager.getRoutes())
				{
					if(r.getRouteName().equals(c.getText()))
					{
						for(Stop s : dbManager.getStopsOnRoute(r))
						{
							ArrayList<Package> packages = (ArrayList<Package>) dbManager.getPackagesForStop(s);
							
							if(packages.size() > 0)
							{
								String stopHead = "";
								String last = "Last          ";
								String first = "First         ";
								String box = "Box#          ";
								String track = "Track#        ";
								String sign = "Sign Here\n";
								
								stopHead += "Package Delivery for ";
								stopHead += s.getStopName();
								stopHead += "\n";
								stopHead += last;
								stopHead += first;
								stopHead += box;
								stopHead += track;
								stopHead += sign;
								
								strReport += stopHead;
								
								for(Package p : packages)
								{
									String strPackage = "";
									strPackage += p.getLastName();
									for(int i=0; i<last.length()-p.getLastName().length();i++)
									{
										strPackage += " ";
									}
									strPackage += p.getFirstName();
									for(int i=0; i<first.length()-p.getFirstName().length();i++)
									{
										strPackage += " ";
									}
									strPackage += p.getBoxOffice();
									for(int i=0; i<box.length()-p.getBoxOffice().length();i++)
									{
										strPackage += " ";
									}
									strPackage += p.getTrackingNumber();
									for(int i=0;i<track.length()-p.getTrackingNumber().length();i++)
									{
										strPackage += " ";
									}
									strPackage += "_____________________________________\n";
									
									strReport += strPackage;
								}
							}
						}
					}
				}
			}
		}
		
		txtAreaReport.setText(strReport);
		btnPrintReport.setDisable(false);
	}
	
	public void btnPrintReportAction(ActionEvent ae)
	{
		Date d = new Date();
		SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy");
		String fileName = "";
		
		fileName += "./Prints/";
		fileName += format.format(d).toString();
		fileName += "-";
		fileName += String.valueOf(d.getTime());
		fileName += ".txt";
		
		File f = new File(fileName);
		
		if(!f.exists())
		{
			try
			{
				f.createNewFile();
				
				FileOutputStream ostream = new FileOutputStream(f.getAbsolutePath());
				OutputStreamWriter owriter = new OutputStreamWriter(ostream, "UTF-8");
				
				owriter.write(strReport);
				
				owriter.close();
				ostream.close();
				
				PrinterJob job = PrinterJob.getPrinterJob();
				
			}
			catch(IOException e)
			{
				//System.err.println("Error: " + e.getMessage());
				e.printStackTrace();
			}
		}
		
	}
}
