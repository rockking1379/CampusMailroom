package com.mailroom.otherclient;

import com.mailroom.common.*;
import com.mailroom.common.Package;
import com.mailroom.mainclient.PackageEditWindow;
import com.panemu.tiwulfx.dialog.MessageDialog;
import com.panemu.tiwulfx.dialog.MessageDialogBuilder;
import com.panemu.tiwulfx.table.TextColumn;
import com.panemu.tiwulfx.table.TickColumn;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

/**
 * Controls MainPageFx.fxml in com.mailroom.fxml.otherclient
 * @author James sitzja@grizzlies.adams.edu
 */
public class MainPageController implements Initializable
{
	private DatabaseManager dbManager;
	private AutoUpdater au = null;
	private PackageEditWindow editWindow = null;

	@FXML
	private TextField txtQuickSearch;
	@FXML
	private Button btnAdvSearch;
	@FXML
	private Button btnExit;
	@FXML
	private Button btnRefresh;
	@FXML
	private TableView<Package> tblViewTable;
	@FXML
	private ComboBox<Stop> cboxStopSelect;

	// Columns
	private TickColumn<Package> clmnDelivered;
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
		dbManager = OtherMainFrame.dbManager;

		// Create Columns
		clmnDelivered = new TickColumn<Package>();
		clmnFirstName = new TextColumn<Package>("firstName");
		clmnLastName = new TextColumn<Package>("lastName");
		clmnStop = new TextColumn<Package>("stop");
		clmnTrackingNumber = new TextColumn<Package>("trackingNumber");
		clmnCourier = new TextColumn<Package>("courier");
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
		clmnFirstName.setText("First");
		clmnLastName.setText("Last");
		clmnStop.setText("Stop");
		clmnTrackingNumber.setText("Tracking");
		clmnCourier.setText("Carrier");
		clmnDateReceived.setText("Date");
		clmnUserName.setText("User");

		// Define Max Width
		clmnDelivered.setMaxWidth(30);
		clmnFirstName.setMaxWidth(100);
		clmnLastName.setMaxWidth(100);
		clmnStop.setMaxWidth(150); // wider because of data contained
		clmnTrackingNumber.setMaxWidth(100);
		clmnCourier.setMaxWidth(100);
		clmnDateReceived.setMaxWidth(150); // wider because of data contained
		clmnUserName.setMaxWidth(100);

		// Add Columns
		tblViewTable.getColumns().add(clmnDelivered);
		tblViewTable.getColumns().add(clmnFirstName);
		tblViewTable.getColumns().add(clmnLastName);
		tblViewTable.getColumns().add(clmnStop);
		tblViewTable.getColumns().add(clmnTrackingNumber);
		tblViewTable.getColumns().add(clmnCourier);
		tblViewTable.getColumns().add(clmnDateReceived);
		tblViewTable.getColumns().add(clmnUserName);

		Package p = new Package(-1, "", "", "", "", "", "", null, null, null,
				false, false, "", false);
		tblViewTable.getItems().add(p);

		boolean found = false;
		cboxStopSelect.getItems().clear();
		cboxStopSelect.getItems().add(new Stop(-1, "ALL", "nil", 0, false, false, "no-email"));
		for (Stop s : dbManager.getStops())
		{
			cboxStopSelect.getItems().add(s);
		}
		for (Stop s : cboxStopSelect.itemsProperty().get())
		{
			if (s.getStudent())
			{
				cboxStopSelect.setValue(s);
				found = true;
				break;
			}
		}

		if (!found)
		{
			cboxStopSelect
					.setValue(cboxStopSelect.itemsProperty().get().get(0));
		}

		if (Boolean
				.valueOf(OtherMainFrame.properties.getProperty("AUTOUPDATE")))
		{
			au = new AutoUpdater(btnRefresh);
		}
		else
		{
			au = null;
		}

		editWindow = OtherMainFrame.editWindow;

		txtQuickSearch.requestFocus();
	}

	/**
	 * Refreshes table on screen
	 * Applies changes to database
	 * @param ae ActionEvent from OS
	 */
	public void btnRefreshAction(ActionEvent ae)
	{
		ObservableList<Package> delivered = (ObservableList<Package>) clmnDelivered
				.getTickedRecords();

		for (Package p : delivered)
		{
			dbManager.updatePackage(p.getPackageId(), true, true);
		}

		if (cboxStopSelect.getValue() != null
				&& cboxStopSelect.getValue().getStopId() != -1)
		{
			dbManager.loadPackages(cboxStopSelect.getValue().getStopId());

			tblViewTable.getItems().clear();

			if (dbManager.getPackages().size() == 0)
			{
				Package p = new Package(-1, "", "", "", "", "", "", null, null,
						null, false, false, "", false);
				tblViewTable.getItems().add(p);
			}
			else
			{
				tblViewTable.getItems().addAll(dbManager.getPackages());
			}
		}
		if (cboxStopSelect.getValue().getStopId() == -1)
		{
			dbManager.loadAllPackages();

			tblViewTable.getItems().clear();

			if (dbManager.getPackages().size() == 0)
			{
				Package p = new Package(-1, "", "", "", "", "", "", null, null,
						null, false, false, "", false);
				tblViewTable.getItems().add(p);
			}
			else
			{
				tblViewTable.getItems().addAll(dbManager.getPackages());
			}
		}
	}

	/**
	 * Exits Application
	 * @param ae ActionEvent from OS
	 */
	public void btnExitAction(ActionEvent ae)
	{
		if (clmnDelivered.getTickedRecords().size() > 0)
		{
			MessageDialog.Answer a = MessageDialogBuilder.confirmation()
					.message("Confirm Quit?").title("Quit")
					.buttonType(MessageDialog.ButtonType.YES_NO)
					.yesOkButtonText("Yes").noButtonText("No")
					.show(OtherMainFrame.stage.getScene().getWindow());

			if (a == MessageDialog.Answer.YES_OK)
			{
				if (au != null)
				{
					au.stop();
				}

				dbManager.dispose();

				System.exit(0);
			}
		}
		else
		{
			if (au != null)
			{
				au.stop();
			}

			dbManager.dispose();

			System.exit(0);
		}
	}

	/**
	 * Opens Advanced Search Screen
	 * @param ae ActionEvent from OS
	 */
	public void btnAdvSearchAction(ActionEvent ae)
	{
		try
		{
			Parent root = FXMLLoader.load(getClass().getResource(
					"/com/mailroom/fxml/common/AdvSearchFx.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add("tiwulfx.css");
			OtherMainFrame.stage.setScene(scene);
		}
		catch (IOException e)
		{
			Logger.log(e);
		}
	}

	/**
	 * Processes stop selection changing
	 * @param ae ActionEvent from OS
	 */
	public void cboxStopSelectAction(ActionEvent ae)
	{
		tblViewTable.getItems().clear();

		if (cboxStopSelect.getValue().getStopId() == -1)
		{
			dbManager.loadAllPackages();
		}
		else
		{
			dbManager.loadPackages(cboxStopSelect.getValue().getStopId());
		}

		if (dbManager.getPackages().size() == 0)
		{
			Package p = new Package(-1, "", "", "", "", "", "", null, null,
					null, false, false, "", false);
			tblViewTable.getItems().add(p);
		}
		else
		{
			tblViewTable.getItems().addAll(dbManager.getPackages());
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
		if (ke.getCode() == KeyCode.R)
		{
			btnRefresh.fire();
		}
	}

	/**
	 * Processes mouse clicks for table on screen
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

	/**
	 * Auto updates table on screen
	 * frequency configurable in settings
	 * @author James
	 *
	 */
	private class AutoUpdater implements Runnable
	{
		Button btnRefresh = null;
		private boolean running;

		/**
		 * Constructor
		 * @param btn Button to be fired during thread
		 */
		public AutoUpdater(Button btn)
		{
			this.btnRefresh = btn;
			new Thread(this).start();
			running = true;
		}

		/**
		 * main logic loop for thread
		 */
		public void run()
		{
			while (running)
			{
				try
				{
					Thread.sleep((long) (Double
							.valueOf(OtherMainFrame.properties
									.getProperty("AUFREQ")) * 1000));
					btnRefresh.fire();
				}
				catch (NumberFormatException e)
				{
					Logger.log(e);
				}
				catch (InterruptedException e)
				{
					Logger.log(e);
				}
			}
		}

		/**
		 * Stops thread from continuing run loop
		 */
		public void stop()
		{
			running = false;
		}
	}
}
