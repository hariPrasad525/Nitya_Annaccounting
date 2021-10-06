package com.nitya.accounter.web.client.ui.company;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.nitya.accounter.web.client.core.ClientActivity;
import com.nitya.accounter.web.client.ui.StyledPanel;
import com.nitya.accounter.web.client.ui.combo.UsersCombo;
import com.nitya.accounter.web.client.ui.core.BaseDialog;
import com.nitya.accounter.web.client.ui.forms.DynamicForm;

public class ActivityCustomizationDialog extends BaseDialog {

	private StyledPanel mainPanel, actiPanel, allCheckBoxPanel;
	private Label showLabel;
	private Label activityLabel;
	private UsersCombo usersCombo;
	DynamicForm form;
	private RadioButton showAllActivitesButton, showFewActivitesButton;
	private CheckBox logoutOrLoginBox, transactionsBox, budgetsBox,
			prferencesBox, reconciliationBox;
	// recurringTransactionsBox,statementBox, salesCustomizationBox, listsBox,
	// dataExchangeBox;

	private long result;

	public ActivityCustomizationDialog(String string) {
		super(string);
		this.getElement().setId("ActivityCustomizationDialog");
		createControl();
	}

	private void createControl() {

		mainPanel = new StyledPanel("mainPanel");
		form = new DynamicForm("form");
		showLabel = new Label(messages.charactersticsToShow());
		showLabel.setStyleName("charaterisitc_label");
		usersCombo = new UsersCombo(messages.showActivitiesFor(), false);
		activityLabel = new Label(messages.activity());
		showAllActivitesButton = new RadioButton(messages.activity(),
				messages.showAllActivities());
		showFewActivitesButton = new RadioButton(messages.activity(),
				messages.showOnlyTheseActivities());
		showAllActivitesButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				allCheckBoxPanel.removeFromParent();
			}
		});
		showAllActivitesButton.setValue(true);
		showFewActivitesButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				mainPanel.add(allCheckBoxPanel);
				allCheckBoxPanel.setStyleName("customise-checkbox-align");
				// allCheckBoxPanel.setSpacing(5);
			}
		});
		allCheckBoxPanel = new StyledPanel("allCheckBoxPanel");
		logoutOrLoginBox = new CheckBox(messages.logutOrLogin());
		logoutOrLoginBox.setValue(true);
		transactionsBox = new CheckBox(messages.transactions());
		transactionsBox.setValue(true);

		budgetsBox = new CheckBox(messages.budgets());
		reconciliationBox = new CheckBox(messages.Reconciliation());

		prferencesBox = new CheckBox(messages.preferences());
		prferencesBox.setValue(true);
		// salesCustomizationBox = new CheckBox(messages.salesCustomization());
		// dataExchangeBox = new CheckBox(messages.dataExchange());
		// listsBox = new CheckBox("Lists");
		// recurringTransactionsBox = new CheckBox(
		// messages.recurringTransactions());
		// statementBox = new CheckBox(messages.statements());

		allCheckBoxPanel.add(logoutOrLoginBox);
		allCheckBoxPanel.add(transactionsBox);
		allCheckBoxPanel.add(budgetsBox);
		allCheckBoxPanel.add(prferencesBox);
		allCheckBoxPanel.add(reconciliationBox);
		// allCheckBoxPanel.add(dataExchangeBox);
		// allCheckBoxPanel.add(listsBox);
		// allCheckBoxPanel.add(recurringTransactionsBox);
		// allCheckBoxPanel.add(statementBox);
		// allCheckBoxPanel.add(salesCustomizationBox);
		actiPanel = new StyledPanel("actiPanel");
		actiPanel.add(activityLabel);
		actiPanel.add(showAllActivitesButton);
		actiPanel.add(showFewActivitesButton);
		form.add(usersCombo);
		mainPanel.add(showLabel);
		mainPanel.add(form);
		mainPanel.add(actiPanel);
		// actiPanel.setSpacing(5);
		setBodyLayout(mainPanel);
	}

	@Override
	protected boolean onOK() {
		if (showAllActivitesButton.getValue() == true) {
			this.result = 0;
		} else {
			setValue(ClientActivity.LOGIN_LOGOUT, logoutOrLoginBox.getValue());
			setValue(ClientActivity.BUDGETS, budgetsBox.getValue());
			setValue(ClientActivity.PREFERENCES, prferencesBox.getValue());
			setValue(ClientActivity.RECONCILIATIONS,
					reconciliationBox.getValue());
			// setValue(ClientActivity.RECURRING_TRNASACTIONS,
			// recurringTransactionsBox.getValue());
			setValue(ClientActivity.TRNASACTIONS, transactionsBox.getValue());
		}
		getCallback().actionResult(result);
		return true;
	}

	@Override
	protected boolean onCancel() {
		hide();
		return true;
	}

	private void setValue(long flag, Boolean value) {
		if (value) {
			this.result |= flag;
		} else {
			this.result &= ~flag;
		}

	}

	private boolean get(long flag, long value) {
		return (value & flag) == flag;

	}

	@Override
	public void setFocus() {

	}

	public void setCustomiseValue(long value) {
		this.result = value;
		if (!showAllActivitesButton.getValue()) {
			logoutOrLoginBox.setValue(get(ClientActivity.LOGIN_LOGOUT, result));
			budgetsBox.setValue(get(ClientActivity.BUDGETS, result));
			prferencesBox.setValue(get(ClientActivity.PREFERENCES, result));
			reconciliationBox.setValue(get(ClientActivity.RECONCILIATIONS,
					result));
			transactionsBox.setValue(get(ClientActivity.TRNASACTIONS, result));

		} else
			showAllActivitesButton.setValue(true);
	}
}
