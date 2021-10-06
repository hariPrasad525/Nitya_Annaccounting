/**
 * 
 */
package com.vimukti.accounter.web.client.uibinder.setup;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.StyledPanel;

/**
 * @author Administrator
 * 
 */
public class SetupTrackEmployeesPage extends AbstractSetupPage {

	private static SetupTrackEmployeesPageUiBinder uiBinder = GWT
			.create(SetupTrackEmployeesPageUiBinder.class);

	@UiField
	StyledPanel mainViewPanel;
	@UiField
	StyledPanel viewPanel;
	@UiField
	CheckBox w2Employees;
	@UiField
	CheckBox contractors;
	@UiField
	Label trackExpenses;
	@UiField
	RadioButton trackEmployeeYes;
	@UiField
	RadioButton trackEmployeeNo;
	@UiField
	RadioButton trackEmployeeExpenseYes;
	@UiField
	RadioButton trackEmployeeExpenseNo;
	@UiField
	StyledPanel trackPanel;
	@UiField
	Label headerLabel;

	interface SetupTrackEmployeesPageUiBinder extends
			UiBinder<Widget, SetupTrackEmployeesPage> {
	}

	public SetupTrackEmployeesPage() {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
	}

	@Override
	protected void createControls() {
		headerLabel.setText(messages.doyouHaveEmployees());

		w2Employees.setText(messages.wehavW2Employes());
		contractors.setText(messages.wehavContractors());
		trackExpenses.setText(messages.trackEmployeeExpenses());
		trackEmployeeYes.setText(messages.yes());
		trackEmployeeNo.setText(messages.no());
		trackEmployeeExpenseYes.setText(messages.yes());
		trackEmployeeExpenseNo.setText(messages.no());

		if (!trackEmployeeExpenseYes.getValue()) {
			// if (trackPanel.isAttached())
			mainViewPanel.remove(trackPanel);
		}

	}

	@Override
	public void onLoad() {
		if (preferences.isHaveEpmloyees()) {
			trackEmployeeYes.setValue(true);
		} else {
			trackEmployeeNo.setValue(true);
		}

		if (preferences.isTrackEmployeeExpenses()) {
			trackEmployeeExpenseYes.setValue(true);
		} else {
			trackEmployeeExpenseNo.setValue(true);
		}
		if (preferences.isHaveW_2Employees()) {
			w2Employees.setValue(true);
		} else {
			w2Employees.setValue(false);
		}

		if (preferences.isHave1099contractors()) {
			contractors.setValue(true);
		} else {
			contractors.setValue(false);
		}

	}

	@Override
	public void onSave() {
		if (trackEmployeeYes.getValue()) {
			if ((w2Employees.getValue() || contractors.getValue())) {
				preferences.setHaveEpmloyees(trackEmployeeYes.getValue());
				preferences.setTrackEmployeeExpenses(trackEmployeeExpenseYes
						.getValue());
				preferences.setHaveW_2Employees(w2Employees.getValue());
				preferences.setHave1099contractors(contractors.getValue());
			}

		} else {
			preferences.setHaveEpmloyees(trackEmployeeYes.getValue());
			preferences.setTrackEmployeeExpenses(trackEmployeeExpenseYes
					.getValue());
			preferences.setHaveW_2Employees(w2Employees.getValue());
			preferences.setHave1099contractors(contractors.getValue());
		}

	}

	@UiHandler("trackEmployeeYes")
	void onTrackEmployeeYesClick(ClickEvent event) {
		if (!trackPanel.isAttached())
			mainViewPanel.add(trackPanel);
	}

	@UiHandler("trackEmployeeNo")
	void onTrackEmployeeNoClick(ClickEvent event) {
		if (trackPanel.isAttached())
			mainViewPanel.remove(trackPanel);

	}

	@Override
	protected boolean validate() {
		if (trackEmployeeYes.getValue()) {
			if (!(w2Employees.getValue() || contractors.getValue())) {
				Accounter.showError(messages
						.pleaseselectvalidtransactionGrid(messages
								.employeeType()));
				return false;
			} else if (!(trackEmployeeExpenseYes.getValue() || trackEmployeeExpenseNo
					.getValue())) {
				Accounter.showError(messages.trackEmployeeExpenses());
				return false;
			} else {
				return true;
			}
		} else {
			return true;
		}
	}

	@Override
	public String getViewName() {
		return messages.trackEmployeeExpenses();
	}
	
	@Override
	public boolean canShow() {
		return isPayrollSetup || super.canShow();
	}
}
