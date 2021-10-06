package com.vimukti.accounter.web.client.ui.combo;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.core.ClientPayHead;
import com.vimukti.accounter.web.client.core.PaginationList;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.forms.FormItem;

public class PayheadCombo extends CustomCombo<ClientPayHead> {

	private long selectedId;

	boolean isItemsAdded;

	protected ArrayList<ClientPayHead> list = new ArrayList<ClientPayHead>();

	private FormItem payheadCombo;

	public PayheadCombo(String title) {
		super(title, false, 1, "payheadcombo");
		getPayHeads();

	}

	private void getPayHeads() {
		isItemsAdded = false;
		Accounter.createPayrollService().getPayheads(0, 0,
				new AsyncCallback<PaginationList<ClientPayHead>>() {

					@Override
					public void onSuccess(PaginationList<ClientPayHead> result) {
						PayheadCombo.this.filterResults(result);
						list = result;
						initCombo(result);
						PayheadCombo.this.isItemsAdded = true;
						setComboItem();
					}
                    @Override
					public void onFailure(Throwable caught) {

					}
				});
	}

	@Override
	protected String getDisplayName(ClientPayHead object) {
		if (object != null)
			return object.getName() != null ? object.getName() : "";
		else
			return "";
	}

	@Override
	protected String getColumnData(ClientPayHead object, int col) {
		switch (col) {
		case 0:
			return object.getName();
		}
		return null;
	}

	@Override
	public String getDefaultAddNewCaption() {
		return null;
	}

	@Override
	public void onAddNew() {

	}

	public void setSelectedId(long payhead) {
		this.selectedId = payhead;
		if (isItemsAdded) {
			setComboItem();
		}
	}

	protected void setComboItem() {
		if (selectedId != 0) {
			List<ClientPayHead> comboItems2 = getComboItems();
			for (ClientPayHead item : comboItems2) {
				if (selectedId == item.getID()) {
					setComboItem(item);
				}
			}
		}
	}

	public ClientPayHead getPayHead(long payHead) {
		for (ClientPayHead ph : list) {
			if (ph.getID() == payHead) {
				return ph;
			}
		}
		return null;
	}

	public static int getPayHead() {
		return 0;
	}

	public void setComboItem(String calculationType) {
		
	}
	
	public void filterResults(ArrayList<ClientPayHead> list) {
		for (ClientPayHead ph : list) {
			if (ph.isEarning()) {
				list.remove(ph);
			}
		}
	}
}
