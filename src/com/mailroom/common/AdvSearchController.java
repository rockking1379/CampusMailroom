package com.mailroom.common;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

import com.mailroom.mainclient.MainFrame;
import com.mailroom.mainclient.PackageEditWindow;
import com.mailroom.otherclient.OtherMainFrame;
import com.panemu.tiwulfx.dialog.MessageDialog;
import com.panemu.tiwulfx.dialog.MessageDialogBuilder;
import com.panemu.tiwulfx.table.CheckBoxColumn;
import com.panemu.tiwulfx.table.TextColumn;

import extfx.scene.control.DatePicker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/**
 * Controls AdvSearchFx.fxml in com.mailroom.fxml.common
 * @author James sitzja@grizzlies.adams.edu
 */
public class AdvSearchController implements Initializable
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
	private TableView<Package> tblViewTable;
	@FXML
	private Button btnExit;
	@FXML
	private Button btnSearch;
	@FXML
	private AnchorPane apaneAnchor;
	@FXML
	private CheckBox cboxDateSearch;

	private DatePicker startDate;
	private DatePicker endDate;

	private DatabaseManager dbManager;
	private PackageEditWindow editWindow;

	// Columns//
	private CheckBoxColumn<Package> clmnDelivered;
	private TextColumn<Package> clmnFirstName;
	private TextColumn<Package> clmnLastName;
	private TextColumn<Package> clmnStop;
	private TextColumn<Package> clmnTrackingNumber;
	private TextColumn<Package> clmnCourier;
	private TextColumn<Package> clmnDateReceived;
	private TextColumn<Package> clmnUserName;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{
		// Create Columns
		clmnDelivered = new CheckBoxColumn<Package>("pickedUp");
		clmnFirstName = new TextColumn<Package>("firstName");
		clmnLastName = new TextColumn<Package>("lastName");
		clmnStop = new TextColumn<Package>("stop");
		clmnTrackingNumber = new TextColumn<Package>("trackingNumber");
		clmnCourier = new TextColumn<Package>("courier");
		// clmnCourier = new ComboBoxColumn<Package, String>("courier");
		clmnDateReceived = new TextColumn<Package>("receivedDate");
		clmnUserName = new TextColumn<Package>("user");

		// Set Resizable False
		clmnDelivered.setResizable(false);
		clmnFirstName.setResizable(false);
		clmnLastName.setResizable(false);
		clmnStop.setResizable(false);
		clmnTrackingNumber.setResizable(false);
		clmnCourier.setResizable(false);
		clmnDateReceived.setResizable(false);
		clmnUserName.setResizable(false);

		// Set Titles
		clmnDelivered.setText("");
		clmnFirstName.setText("First");
		clmnLastName.setText("Last");
		clmnStop.setText("Stop");
		clmnTrackingNumber.setText("Tracking");
		clmnCourier.setText("Carrier");
		clmnDateReceived.setText("Date");
		clmnUserName.setText("User");

		// Define Max Width
		clmnDelivered.setMaxWidth(50);
		clmnFirstName.setMaxWidth(70);
		clmnLastName.setMaxWidth(70);
		clmnStop.setMaxWidth(100); // wider because of data contained
		clmnTrackingNumber.setMaxWidth(70);
		clmnCourier.setMaxWidth(70);
		clmnDateReceived.setMaxWidth(100); // wider because of data contained
		clmnUserName.setMaxWidth(75);

		// Add Columns
		tblViewTable.getColumns().add(clmnDelivered);
		tblViewTable.getColumns().add(clmnFirstName);
		tblViewTable.getColumns().add(clmnLastName);
		tblViewTable.getColumns().add(clmnStop);
		tblViewTable.getColumns().add(clmnTrackingNumber);
		tblViewTable.getColumns().add(clmnCourier);
		tblViewTable.getColumns().add(clmnDateReceived);
		tblViewTable.getColumns().add(clmnUserName);

		startDate = new DatePicker(Locale.US);
		startDate.setLayoutX(14);
		startDate.setLayoutY(204);
		startDate.setMaxWidth(137);
		startDate.setMinHeight(25);
		startDate.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
		startDate.promptTextProperty().set("Start Date");
		startDate.setDisable(true);
		apaneAnchor.getChildren().add(startDate);

		endDate = new DatePicker(Locale.US);
		endDate.setLayoutX(14);
		endDate.setLayoutY(239);
		endDate.setMaxWidth(137);
		endDate.setMinHeight(25);
		endDate.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
		endDate.setMaxDate(new Date());
		endDate.promptTextProperty().set("End Date");
		endDate.setDisable(true);
		apaneAnchor.getChildren().add(endDate);

		if (MainFrame.dbManager != null)
		{
			this.dbManager = MainFrame.dbManager;
		}
		else
		{
			this.dbManager = OtherMainFrame.dbManager;
		}

		if (MainFrame.stage != null)
		{
			editWindow = MainFrame.editWindow;
		}
		if (OtherMainFrame.stage != null)
		{
			editWindow = OtherMainFrame.editWindow;
		}
	}

	/**
	 * Performs Search based on params given
	 * @param ae ActionEvent from OS
	 */
	public void btnSearchAction(ActionEvent ae)
	{
		ArrayList<Package> results = new ArrayList<Package>();

		tblViewTable.getItems().clear();

		try
		{
			if (!cboxDateSearch.isSelected())
			{
				if (!txtTrackingNumber.getText().equals(""))
				{
					for (Package p : dbManager.searchPackages(
							txtTrackingNumber.getText(),
							DatabaseManager.SearchType.SEARCH_CONTAINS))
					{
						if (!results.contains(p))
						{
							results.add(p);
						}
						else
						{
							System.out.println("Found Previous");
						}
					}
				}
				if (!txtFirstName.getText().equals(""))
				{
					for (Package p : dbManager.searchPackages(
							txtFirstName.getText(),
							DatabaseManager.SearchType.SEARCH_CONTAINS))
					{
						if (!results.contains(p))
						{
							results.add(p);
						}
						else
						{
							System.out.println("Found Previous");
						}
					}
				}
				if (!txtLastName.getText().equals(""))
				{
					for (Package p : dbManager.searchPackages(
							txtLastName.getText(),
							DatabaseManager.SearchType.SEARCH_CONTAINS))
					{
						if (!results.contains(p))
						{
							results.add(p);
						}
						else
						{
							System.out.println("Found Previous");
						}
					}
				}
				if (!txtBoxOffice.getText().equals(""))
				{
					for (Package p : dbManager.searchPackages(
							txtBoxOffice.getText(),
							DatabaseManager.SearchType.SEARCH_CONTAINS))
					{
						if (!results.contains(p))
						{
							results.add(p);
						}
						else
						{
							System.out.println("Found Previous");
						}
					}
				}
			}
			else
			{
				String start = new SimpleDateFormat("yyyy-MM-dd").format(
						startDate.getValue()).toString();
				String end = new SimpleDateFormat("yyyy-MM-dd").format(
						endDate.getValue()).toString();

				if (endDate.getValue().before(startDate.getValue()))
				{
					MessageDialogBuilder.error()
							.message("Start Date MUST BE BEFORE End Date")
							.title("Error")
							.buttonType(MessageDialog.ButtonType.OK)
							.show(MainFrame.stage.getScene().getWindow());
					return;
				}

				if (txtTrackingNumber.getText().equals("")
						&& txtFirstName.getText().equals("")
						&& txtLastName.getText().equals(""))
				{
					txtFirstName.setText(" ");
				}

				if (!txtTrackingNumber.getText().equals(""))
				{
					for (Package p : dbManager.searchPackages(
							txtTrackingNumber.getText(), start, end,
							DatabaseManager.SearchType.SEARCH_CONTAINS))
					{
						if (!results.contains(p))
						{
							results.add(p);
						}
						else
						{
							System.out.println("Found Previous");
						}
					}
				}
				if (!txtFirstName.getText().equals(""))
				{
					for (Package p : dbManager.searchPackages(
							txtFirstName.getText(), start, end,
							DatabaseManager.SearchType.SEARCH_CONTAINS))
					{
						if (!results.contains(p))
						{
							results.add(p);
						}
						else
						{
							System.out.println("Found Previous");
						}
					}
				}
				if (!txtLastName.getText().equals(""))
				{
					for (Package p : dbManager.searchPackages(
							txtLastName.getText(), start, end,
							DatabaseManager.SearchType.SEARCH_CONTAINS))
					{
						if (!results.contains(p))
						{
							results.add(p);
						}
						else
						{
							System.out.println("Found Previous");
						}
					}
				}
			}

			for (Package p : results)
			{
				tblViewTable.getItems().add(p);
			}

			if (txtFirstName.getText().equals(" "))
			{
				txtFirstName.setText("");
			}
		}
		catch (NullPointerException e)
		{
			System.err.println("Error: " + e.getMessage());
		}
	}

	/**
	 * Exits to Main Screen for that client
	 * @param ae ActionEvent from OS
	 */
	public void btnExitAction(ActionEvent ae)
	{
		if (MainFrame.dbManager != null)
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
				System.err.println("Error: " + e.getMessage());
			}
		}
		else
		{
			try
			{
				Parent root = FXMLLoader.load(getClass().getResource(
						"/com/mailroom/fxml/otherclient/MainPageFx.fxml"));
				Scene scene = new Scene(root);
				OtherMainFrame.stage.setScene(scene);
			}
			catch (IOException e)
			{
				System.err.println("Error: " + e.getMessage());
			}
		}
	}

	/**
	 * Processes keyboard input
	 * @param ke KeyEvent from OS
	 */
	public void keyPressAction(KeyEvent ke)
	{
		if (ke.getCode() == KeyCode.ESCAPE)
		{
			btnExit.fire();
		}
	}

	/**
	 * Processes Checkbox input
	 * @param ae ActionEvent from OS
	 */
	public void cboxDateSearchAction(ActionEvent ae)
	{
		if (cboxDateSearch.isSelected())
		{
			startDate.setDisable(false);
			endDate.setDisable(false);
		}
		else
		{
			startDate.setDisable(true);
			endDate.setDisable(true);

			startDate.setValue(null);
			endDate.setValue(null);
		}
	}

	/**
	 * Hanldes MouseClicks for table on screen
	 * @param me MouseEvent from OS
	 */
	public void tblMouseClickAction(MouseEvent me)
	{
		if (me.getClickCount() >= 2)
		{
			editWindow.show(tblViewTable.getItems().get(
					tblViewTable.getSelectionModel().getSelectedIndex()));
		}
	}
}
