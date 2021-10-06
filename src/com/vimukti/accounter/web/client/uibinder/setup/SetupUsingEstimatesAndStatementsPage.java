package com.vimukti.accounter.web.client.uibinder.setup;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.ui.Accounter;

public class SetupUsingEstimatesAndStatementsPage extends AbstractSetupPage {

	private static SetupUsingEstimatesAndStatementsPageUiBinder uiBinder = GWT
			.create(SetupUsingEstimatesAndStatementsPageUiBinder.class);
	@UiField
	FlowPanel viewPanel;
	@UiField
	RadioButton estimatesYes;
	@UiField
	RadioButton estimatesNo;
	// @UiField
	Label someExampleText;
	// @UiField
	Label someExampleList;
	// @UiField
	Label billingQuestion;
	// @UiField
	RadioButton statementYes;
	// @UiField
	RadioButton statementsNo;
	@UiField
	Label headerLabel;
	// @UiField
	Label billingStatements;
	// @UiField
	Label stmt_header;

	interface SetupUsingEstimatesAndStatementsPageUiBinder extends
			UiBinder<Widget, SetupUsingEstimatesAndStatementsPage> {
	}

	public SetupUsingEstimatesAndStatementsPage() {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
	}

	@Override
	protected void createControls() {
		headerLabel.setText(messages.wanttoCreateEstimatesInAccounter());
		estimatesYes.setText(messages.yes());
		estimatesNo.setText(messages.no());
		preferences.setDoyouwantEstimates(false);
		/*
		 * billingStatements.setHTML(accounterMessages.statementDescription());
		 * someExampleText.setText(messages.statementSomeExample());
		 * someExampleList.setHTML(accounterMessages.statementlist());
		 * billingQuestion.setText(accounterMessages.statementQuestion());
		 * statementYes.setText(messages.yes());
		 * statementsNo.setText(messages.no());
		 * stmt_header.setText(messages.doyouWantToUseStatements());
		 */
	}

	@Override
	public void onLoad() {
		// estimates
		if (preferences.isDoyouwantEstimates()) {
			estimatesYes.setValue(true);
		} else {
			estimatesNo.setValue(true);
		}
		// statements
		// statementYes.setValue(preferences.isDoyouwantstatements());
	}

	@Override
	protected void onAttach() {
		preferences.setDoyouwantEstimates(true);
		super.onAttach();
	}

	@Override
	public void onSave() {

		// Estimates
		preferences.setDoyouwantEstimates(estimatesYes.getValue());
		// Statements
		// preferences.setDoyouwantstatements(statementYes.getValue());
	}

	@Override
	protected boolean validate() {
		if ((!(estimatesYes.getValue() || estimatesNo.getValue()))
		/* && (!(statementYes.getValue() || statementsNo.getValue())) */) {
			Accounter.showError(messages.pleaseEnter(messages.details()));
			return false;
		} else if (!(estimatesYes.getValue() || estimatesNo.getValue())) {
			Accounter.showError(messages.pleaseEnter(messages
					.wanttoCreateEstimatesInAccounter()));
			return false;
		}/*
		 * else if (!(statementYes.getValue() || statementsNo.getValue())) {
		 * Accounter .showError(accounterMessages.pleaseEnter(messages
		 * .doyouWantToUseStatements())); return false; }
		 */else {
			return true;
		}
	}

	@Override
	public String getViewName() {
		return messages.setEstimatesAndStatements();
	}
	
}
