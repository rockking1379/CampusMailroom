package com.mailroom.mainclient;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
//import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import com.mailroom.common.*;
import com.mailroom.common.Package;
import com.panemu.tiwulfx.dialog.MessageDialog;
import com.panemu.tiwulfx.dialog.MessageDialogBuilder;

/**
 * Controls ScanPageFx.fxml in com.mailroom.fxml.mainclient
 * @author James sitzja@grizzlies.adams.edu
 */
public class ScanPageController implements Initializable
{
	@FXML
	private TextField txtTrackingNumber;
	@FXML
	private TextField txtFirstName;
	@FXML
	private TextField txtLastName;
	@FXML
	private TextField txtBoxOffice;
	@FXML
	private TextField txtEmailAddress;
	@FXML
	private Label lblDate;
	@FXML
	private ComboBox<Stop> cboxStops;
	@FXML
	private ComboBox<Courier> cboxCourier;
	@FXML
	private Button btnSave;
	@FXML
	private Button btnClear;
	@FXML
	private Button btnExit;
	@FXML
	private Button btnRandomGenerate;

	private DatabaseManager dbManager;
	private User cUser;
	private String stopSearch = "";
	private String courierSearch = "";

	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date now = new Date();
		lblDate.setText(format.format(now).toString());

		cUser = MainFrame.cUser;
		dbManager = MainFrame.dbManager;

		cboxStops.getItems().clear();
		cboxCourier.getItems().clear();

		for (Stop s : dbManager.getStops())
		{
			cboxStops.getItems().add(s);
		}
		for (Courier c : dbManager.getCouriers())
		{
			cboxCourier.getItems().add(c);
		}
	}

	public void btnExitAction(ActionEvent ae)
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
			e.printStackTrace();
		}
	}

	public void btnClearAction(ActionEvent ae)
	{
		txtTrackingNumber.setText("");
		txtFirstName.setText("");
		txtLastName.setText("");
		txtBoxOffice.setText("");
		txtEmailAddress.setText("");
		stopSearch = "";
		courierSearch = "";
	}

	public void btnSaveAction(ActionEvent ae)
	{
		boolean verified = true;

		if (txtTrackingNumber.getText().length() < 4)
		{
			MessageDialogBuilder
					.error()
					.message(
							"Tracking Number Not Long Enough\nMust Be 4 Characters or Longer")
					.title("Error").buttonType(MessageDialog.ButtonType.OK)
					.show(MainFrame.stage.getScene().getWindow());
			verified = false;
		}
		if (txtFirstName.getText().equals("")
				&& txtLastName.getText().equals(""))
		{
			txtFirstName.setText("DEPARTMENT");
			txtLastName.setText("DEPARTMENT");
		}
		else
		{
			if (txtFirstName.getText().equals("")
					&& !txtLastName.getText().equals(""))
			{
				MessageDialogBuilder.error().message("No Last Name Specified")
						.title("Error").buttonType(MessageDialog.ButtonType.OK)
						.show(MainFrame.stage.getScene().getWindow());
				verified = false;
			}
			else
			{
				if (!txtFirstName.getText().equals("")
						&& txtLastName.getText().equals(""))
				{
					MessageDialogBuilder.error()
							.message("No First Name Specified").title("Error")
							.buttonType(MessageDialog.ButtonType.OK)
							.show(MainFrame.stage.getScene().getWindow());
					verified = false;
				}
			}
		}

		if (txtBoxOffice.getText().equals(""))
		{
			txtBoxOffice.setText("---");
		}
		// else
		// {
		// MessageDialogBuilder.error().message("No Box/Suite Number Set").title("Error").buttonType(MessageDialog.ButtonType.OK).show(MainFrame.stage.getScene().getWindow());
		// verified = false;
		// }

		if (txtEmailAddress.getText().equals(""))
		{
			txtEmailAddress.setText("Unknown@");
		}
		if (verified)
		{
			int pid = dbManager
					.checkTrackingNumber(txtTrackingNumber.getText());
			if (pid == -1)
			{
				Package p = new Package(-1, txtTrackingNumber.getText(),
						lblDate.getText(), txtEmailAddress.getText(),
						txtFirstName.getText(), txtLastName.getText(),
						txtBoxOffice.getText(), cboxStops.getValue(),
						cboxCourier.getValue(), cUser, false, false, null,
						false);

				if (dbManager.addPackage(p))
				{
					btnClear.fire();
				}
				else
				{
					MessageDialogBuilder.error()
							.message("Error Adding Package").title("Error")
							.buttonType(MessageDialog.ButtonType.OK)
							.show(MainFrame.stage.getScene().getWindow());
				}
			}
			else
			{
				MessageDialog.Answer ans = MessageDialogBuilder
						.error()
						.title("Tracking Number Found")
						.message(
								"Tracking Number already exists\nOverwrite or Create New Record?")
						.buttonType(MessageDialog.ButtonType.YES_NO)
						.yesOkButtonText("New Record")
						.noButtonText("Overwrite")
						.show(MainFrame.stage.getScene().getWindow());

				if (ans == MessageDialog.Answer.YES_OK)
				{
					Package p = new Package(-1, txtTrackingNumber.getText(),
							lblDate.getText(), txtEmailAddress.getText(),
							txtFirstName.getText(), txtLastName.getText(),
							txtBoxOffice.getText(), cboxStops.getValue(),
							cboxCourier.getValue(), cUser, false, false, null,
							false);

					if (dbManager.addPackage(p))
					{
						btnClear.fire();
					}
					else
					{
						MessageDialogBuilder.error()
								.message("Error Adding Package").title("Error")
								.buttonType(MessageDialog.ButtonType.OK)
								.show(MainFrame.stage.getScene().getWindow());
					}
				}
				else if (ans == MessageDialog.Answer.NO)
				{
					Package p = new Package(pid, txtTrackingNumber.getText(),
							lblDate.getText(), txtEmailAddress.getText(),
							txtFirstName.getText(), txtLastName.getText(),
							txtBoxOffice.getText(), cboxStops.getValue(),
							cboxCourier.getValue(), cUser, false, false, null,
							false);

					if (dbManager.updatePackage(p))
					{
						btnClear.fire();
					}
					else
					{
						MessageDialogBuilder.error()
								.message("Error Adding Package").title("Error")
								.buttonType(MessageDialog.ButtonType.OK)
								.show(MainFrame.stage.getScene().getWindow());
					}
				}
			}
		}
	}

	public void btnRandomGenerateAction(ActionEvent ae)
	{
		String tnum = MainFrame.properties.getProperty("TNUMPREFIX");

		for (int i = 0; i < 16; i++)
		{
			tnum += generate();
		}

		txtTrackingNumber.setText(tnum);
	}

	public void keyPressAction(KeyEvent ke)
	{
		if (ke.getCode() == KeyCode.ESCAPE)
		{
			btnExit.fire();
		}
		if (ke.getCode() == KeyCode.ENTER)
		{
			if (txtBoxOffice.focusedProperty().get())
			{

				MessageDialogBuilder.info().message("Auto Fill Disabled")
						.title("Error").buttonType(MessageDialog.ButtonType.OK)
						.show(MainFrame.stage.getScene().getWindow());
				// ArrayList<Person> people = (ArrayList<Person>) dbManager
				// .findPerson(txtFirstName.getText(),
				// txtLastName.getText(), txtBoxOffice.getText());
				//
				// switch (people.size())
				// {
				// case 0:
				// {
				// MessageDialogBuilder.info().message("No Results Found")
				// .title("Info")
				// .buttonType(MessageDialog.ButtonType.OK)
				// .show(MainFrame.stage.getScene().getWindow());
				// txtEmailAddress.setText("Unknown@");
				// break;
				// }
				// case 1:
				// {
				// Person p = people.get(0);
				//
				// txtEmailAddress.setText(p.getEmailAddress());
				// txtFirstName.setText(p.getFirstName());
				// txtLastName.setText(p.getLastName());
				// for (Stop s : cboxStops.getItems())
				// {
				// if (s.getStopName().equals(p.getStopName()))
				// {
				// cboxStops.setValue(s);
				// break;
				// }
				// }
				// break;
				// }
				// default:
				// {
				// MessageDialogBuilder.info()
				// .message("Multiple Results Found").title("Info")
				// .buttonType(MessageDialog.ButtonType.OK)
				// .show(MainFrame.stage.getScene().getWindow());
				// break;
				// }
				// }
			}
			else
			{
				if(txtTrackingNumber.focusedProperty().get())
				{
					txtFirstName.requestFocus();
				}
				else
				{
					if (txtFirstName.focusedProperty().get()
							|| txtLastName.focusedProperty().get()
							|| txtEmailAddress.focusedProperty().get())
					{
						// do nothing
					}
					else
					{
						btnSave.fire();
					}
				}
			}
		}
	}

	public void cboxStopsKeyPressAction(KeyEvent ke)
	{
		if (ke.getCode() == KeyCode.BACK_SPACE)
		{
			stopSearch = "";
			courierSearch = "";
		}
		else
		{
			if (ke.getCode() == KeyCode.TAB && ke.isShiftDown())
			{
				txtEmailAddress.requestFocus();
			}
			else
			{
				if (ke.getCode() == KeyCode.TAB && !ke.isShiftDown())
				{
					stopSearch = "";
					courierSearch = "";
					cboxCourier.requestFocus();
				}
				else
				{
					stopSearch += ke.getCode().toString();

					for (Stop s : cboxStops.getItems())
					{
						if (s.getStopName().toUpperCase()
								.startsWith(stopSearch.toUpperCase()))
						{
							cboxStops.setValue(s);
							break;
						}
					}
				}
			}
		}
	}

	public void cboxCourierKeyPressAction(KeyEvent ke)
	{
		if (ke.getCode() == KeyCode.BACK_SPACE)
		{
			stopSearch = "";
			courierSearch = "";
		}
		else
		{
			if (ke.getCode() == KeyCode.TAB && !ke.isShiftDown())
			{
				btnSave.requestFocus();
			}
			else
			{
				if (ke.getCode() == KeyCode.TAB && ke.isShiftDown())
				{
					stopSearch = "";
					courierSearch = "";
					cboxStops.requestFocus();
				}
				else
				{
					courierSearch += ke.getCode().toString();

					for (Courier c : cboxCourier.getItems())
					{
						if (c.getCourierName().toUpperCase()
								.startsWith(courierSearch.toUpperCase()))
						{
							cboxCourier.setValue(c);
							break;
						}
					}
				}
			}
		}
	}

	private char generate()
	{
		int val;

		Random r = new Random();

		val = r.nextInt(74) + 48;

		if (val >= 58 && val <= 64)
		{
			val = (int) generate();
		}
		if (val >= 91 && val <= 96)
		{
			val = (int) generate();
		}

		return (char) val;
	}
}
