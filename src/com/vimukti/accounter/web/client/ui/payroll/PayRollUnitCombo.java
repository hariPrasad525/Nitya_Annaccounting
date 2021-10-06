package com.vimukti.accounter.web.client.ui.payroll;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.core.ClientPayrollUnit;
import com.vimukti.accounter.web.client.core.PaginationList;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.combo.CustomCombo;
import com.vimukti.accounter.web.client.ui.core.ActionCallback;
import com.vimukti.accounter.web.client.ui.core.PayRollActions;

public class PayRollUnitCombo extends CustomCombo<ClientPayrollUnit> {

	private boolean isAddNew;
	protected ArrayList<ClientPayrollUnit> list = new ArrayList<ClientPayrollUnit>();
	private long payRollId;

	public PayRollUnitCombo(String title, String styleName, long payRollId) {
		super(title, "payRollUnitCombo");
		getPayrollUnits();
		this.payRollId = payRollId;
	}

	private void getPayrollUnits() {
		Accounter.createPayrollService().getPayrollUnitsList(0, -1,
				new AsyncCallback<PaginationList<ClientPayrollUnit>>() {

					@Override
					public void onSuccess(
							PaginationList<ClientPayrollUnit> result) {
						list = result;
						initCombo(result);
						if (payRollId != 0) {
							setComboItem();
						}
					}

					@Override
					public void onFailure(Throwable caught) {
					}
				});

	}

	protected void setComboItem() {
		List<ClientPayrollUnit> comboItems2 = getComboItems();
		for (ClientPayrollUnit clientPayrollUnit : comboItems2) {
			if (payRollId == clientPayrollUnit.getID()) {
				setComboItem(clientPayrollUnit);
			}
		}
	}

	@Override
	protected String getDisplayName(ClientPayrollUnit object) {
		if (object != null)
			return object.getName() != null ? object.getName() : "";
		else
			return "";
	}

	@Override
	public void onAddNew() {

		isAddNew = true;
		PayRollActions action = PayRollActions.newPayRollUnitAction();
		action.setFromEmployeeView(true);
		action.setCallback(new ActionCallback<ClientPayrollUnit>() {

			@Override
			public void actionResult(ClientPayrollUnit result) {
				if (result.getDisplayName() != null)
					isAddNew = false;
				list.add(result);
				addItemThenfireEvent(result);
			}
		});

		action.run(null, true);
	}

	@Override
	protected String getColumnData(ClientPayrollUnit object, int col) {
		switch (col) {
		case 0:
			return object.getName();
		}
		return null;
	}

	@Override
	protected void selectionFaildOnClose() {
		if (isAddNew) {
			return;
		}
	}

	@Override
	public String getDefaultAddNewCaption() {
		return messages.payrollUnit();
	}

}
