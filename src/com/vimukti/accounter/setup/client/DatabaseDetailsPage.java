package com.vimukti.accounter.setup.client;

import com.google.gwt.core.client.Callback;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLTable.RowFormatter;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.setup.client.core.DatabaseConnection;

public class DatabaseDetailsPage extends AbstractPage {

	private FlexTable table;
	private TextBox portText;
	private RowFormatter rowFormatter;
	private TextBox schemaText;
	private ListBox dbTypes;
	private TextBox hostText;
	private TextBox dbText;
	private TextBox usernameText;
	private PasswordTextBox pwdText;
	private RadioButton internal;

	protected Widget createControls() {
		this.table = new FlexTable();

		VerticalPanel dbOptions = getDatabaseOptions();

		this.dbTypes = getDbTypesListBox();
		this.hostText = new TextBox();
		this.portText = new TextBox();
		this.dbText = new TextBox();
		this.usernameText = new TextBox();
		this.pwdText = new PasswordTextBox();
		this.schemaText = new TextBox();

		table.setWidget(0, 0, createTitle("Database Connection"));
		table.setWidget(0, 1, dbOptions);
		table.setWidget(1, 0, createTitle("Database Type", true));
		table.setWidget(1, 1, dbTypes);
		table.setWidget(2, 0, createTitle("Hostname", true));
		table.setWidget(
				2,
				1,
				addTag(hostText,
						"Hostname or IP address of the database server"));
		table.setWidget(3, 0, createTitle("Port", true));
		table.setWidget(3, 1,
				addTag(portText, "TCP Port Number for the database server"));
		table.setWidget(4, 0, createTitle("Database", true));
		table.setWidget(4, 1, addTag(dbText, "Database to connect to"));
		table.setWidget(5, 0, createTitle("Username", true));
		table.setWidget(5, 1,
				addTag(usernameText, "The username to access database"));
		table.setWidget(6, 0, createTitle("Password"));
		table.setWidget(6, 1,
				addTag(pwdText, "The password to access database"));
		table.setWidget(7, 0, createTitle("Schema"));
		table.setWidget(7, 1,
				addTag(schemaText, "Specify the schema name for your database"));

		this.rowFormatter = table.getRowFormatter();
		rowFormatter.setVisible(7, false);
		hideExternal();
		internal.setValue(true);
		return table;
	}

	private VerticalPanel getDatabaseOptions() {
		VerticalPanel dbOptions = new VerticalPanel();
		this.internal = new RadioButton("dbType", "Internal");
		internal.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				hideExternal();
			}
		});
		RadioButton external = new RadioButton("dbType", "External");
		external.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				showExternal();
			}
		});
		dbOptions.add(internal);
		dbOptions.add(external);
		return dbOptions;
	}

	private ListBox getDbTypesListBox() {
		final ListBox dbTypes = new ListBox();
		dbTypes.addItem("-- Select Database Type --");
		dbTypes.addItem("PostgreSQL");
		dbTypes.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				int selectedIndex = dbTypes.getSelectedIndex();
				rowFormatter.setVisible(7, false);
				switch (selectedIndex) {
				case 0:
					portText.setText("");
					break;
				case 1:
					portText.setText("5432");
					rowFormatter.setVisible(7, true);
					schemaText.setText("public");
					break;
				case 2:
					portText.setText("8082");
					break;
				default:
					rowFormatter.setVisible(7, false);
					break;
				}

			}
		});
		return dbTypes;
	}

	protected void showExternal() {
		for (int index = 1; index < table.getRowCount(); index++) {
			rowFormatter.setVisible(index, true);
		}
		setupHome.showOrHideTestConnection(true);
	}

	protected void hideExternal() {
		for (int index = 1; index < table.getRowCount(); index++) {
			rowFormatter.setVisible(index, false);
		}
		setupHome.showOrHideTestConnection(false);
	}

	@Override
	public int getPageNo() {
		return 1;
	}

	@Override
	public String getPageTitle() {
		return "Database Settings";
	}

	@Override
	public void validate(ValidationResult result) {
		if (internal.getValue()) {
			return;
		}
		if (dbTypes.getSelectedIndex() < 0 || dbTypes.getSelectedIndex() == 0) {
			result.addError(dbTypes, "Please select database type");
		}

		if (hostText.getText() == null || hostText.getText().trim().isEmpty()) {
			result.addError(hostText, "Please enter hostname");
		}

		if (portText.getText() == null || portText.getText().trim().isEmpty()) {
			result.addError(portText, "Please enter portno.");
		}

		if (dbText.getText() == null || dbText.getText().trim().isEmpty()) {
			result.addError(dbText, "Please enter database name");
		}

		if (usernameText.getText() == null
				|| usernameText.getText().trim().isEmpty()) {
			result.addError(usernameText, "Please enter username");
		}

	}

	@Override
	protected void savePage(final Callback<Boolean, Throwable> callback) {
		SetupHome.getSetupService().saveDBConnection(getDatabaseDetails(),
				new AsyncCallback<Boolean>() {

					@Override
					public void onSuccess(Boolean result) {
						setupHome.showOrHideTestConnection(!internal.getValue());
						callback.onSuccess(result);
					}

					@Override
					public void onFailure(Throwable caught) {
						callback.onFailure(caught);
					}
				});
	}

	public DatabaseConnection getDatabaseDetails() {
		if (internal.getValue()) {
			return null;
		}
		DatabaseConnection details = new DatabaseConnection();
		details.setDbType(dbTypes.getSelectedIndex());
		details.setDbName(dbText.getText());
		details.setHostname(hostText.getText());
		details.setPort(portText.getText());
		details.setUsername(usernameText.getText());
		details.setPassword(pwdText.getText());
		details.setSchema(schemaText.getText());
		return details;
	}
}
