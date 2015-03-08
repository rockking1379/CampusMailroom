package com.mailroom.mainclient;

import java.awt.Font;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

import javax.swing.JTextArea;

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

/**
 * Controls PrintPageFx.fxml in com.mailroom.fxml.mainclient
 * @author James sitzja@grizzlies.adams.edu
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
		this.dbManager = MainFrame.dbManager;

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
						Logger.log(e);
					}
				}
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
					Logger.log(e);
				}
			}
		}
	}

	/**
	 * Creates Report for Printing
	 * @param ae ActionEvent from OS
	 */
	public void btnCreateReportAction(ActionEvent ae)
	{
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
								stopHead += format.format(d).toString();
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
	 * @param ae ActionEvent from OS
	 */
	public void btnPrintReportAction(ActionEvent ae)
	{
		File dir = new File("./Prints");

		if (!dir.exists())
		{
			dir.mkdir();
		}

		Date d = new Date();
		SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy");
		String fileName = "";

		fileName += "./Prints/";
		fileName += format.format(d).toString();
		fileName += "-";
		fileName += String.valueOf(d.getTime());
		fileName += ".txt";

		File f = new File(fileName);

		if (!f.exists())
		{
			try
			{
				f.createNewFile();

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
					jtext.print();
				}
				catch (PrinterException e)
				{
					Logger.log(e);
				}
			}
			catch (IOException e)
			{
				Logger.log(e);
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
				Logger.log(e);
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
}
