package com.nitya.accounter.web.client.ui.payroll;

import java.util.List;

import com.google.gwt.user.cellview.client.TextColumn;
import com.nitya.accounter.web.client.core.ClientTAXAgency;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.company.NewTAXAgencyAction;
import com.nitya.accounter.web.client.ui.core.ActionCallback;
import com.nitya.accounter.web.client.ui.edittable.AbstractDropDownTable;

public class TaxAgencyTable extends AbstractDropDownTable<ClientTAXAgency> {

	public TaxAgencyTable() {
		super(Accounter.getCompany().getTaxAgencies(), true);
	}

	@Override
	protected ClientTAXAgency getAddNewRow() {
		ClientTAXAgency payhead = new ClientTAXAgency();
		return payhead;
	}

	@Override
	public void initColumns() {
		TextColumn<ClientTAXAgency> textColumn = new TextColumn<ClientTAXAgency>() {

			@Override
			public String getValue(ClientTAXAgency object) {
				return object.getDisplayName();
			}
		};
		this.addColumn(textColumn);
	}

	@Override
	protected void addNewItem() {
		NewTAXAgencyAction action = new NewTAXAgencyAction();
		action.setCallback(new ActionCallback<ClientTAXAgency>() {

			@Override
			public void actionResult(ClientTAXAgency result) {
				if (result.getName() != null)
					selectRow(result);

			}
		});
		action.run(null, true);
	}

	@Override
	protected boolean filter(ClientTAXAgency t, String string) {
		return getDisplayValue(t).toLowerCase().startsWith(string);
	}

	@Override
	protected String getDisplayValue(ClientTAXAgency value) {
		return value.getDisplayName();
	}

	@Override
	protected void addNewItem(String text) {
		addNewItem();
	}

	@Override
	public List<ClientTAXAgency> getTotalRowsData() {
		return Accounter.getCompany().getTaxAgencies();
	}

}
