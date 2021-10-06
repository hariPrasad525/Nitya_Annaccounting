package com.vimukti.accounter.web.client.ui.combo;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.core.ClientAttendanceOrProductionType;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.core.PaginationList;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.ActionCallback;
import com.vimukti.accounter.web.client.ui.core.PayRollActions;

public class AttendanceOrProductionTypeCombo extends
		CustomCombo<ClientAttendanceOrProductionType> {

	private int type;
	protected ArrayList<ClientAttendanceOrProductionType> list;
	private long selectedId;

	private boolean isItemsAdded = false;
	private long productType;

	public AttendanceOrProductionTypeCombo(int type, String title,
			String styleName) {
		super(title, true, 1, styleName);
		this.type = type;
		getAttendanceProductionTypes();
	}

	private void initCombo(ArrayList<ClientAttendanceOrProductionType> result) {
		List<ClientAttendanceOrProductionType> list = Utility.filteredList(
				new ListFilter<ClientAttendanceOrProductionType>() {

					@Override
					public boolean filter(ClientAttendanceOrProductionType e) {
						return e.getType() == type;
					}
				}, result);
		initCombo(list);
	}

	private void getAttendanceProductionTypes() {
		isItemsAdded = false;
		Accounter
				.createPayrollService()
				.getAttendanceProductionTypes(
						0,
						-1,
						new AsyncCallback<PaginationList<ClientAttendanceOrProductionType>>() {

							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onSuccess(
									PaginationList<ClientAttendanceOrProductionType> result) {
								list = result;
								initCombo(result);
								isItemsAdded = true;
								setComboItem();
							}
						});
	}

	public void setSelectedId(long selectedId) {
		this.selectedId = selectedId;
		if (isItemsAdded) {
			setComboItem();
		}
	}

	protected void setComboItem() {
		if (selectedId != 0) {
			List<ClientAttendanceOrProductionType> comboItems2 = getComboItems();
			for (ClientAttendanceOrProductionType item : comboItems2) {
				if (selectedId == item.getID()) {
					setComboItem(item);
					selectedId = 0;
					break;
				}
			}
		}
	}

	@Override
	protected String getDisplayName(ClientAttendanceOrProductionType object) {
		return object.getDisplayName();
	}

	@Override
	protected String getColumnData(ClientAttendanceOrProductionType object,
			int col) {
		if (col == 0) {
			return object.getName();
		}
		return null;
	}

	@Override
	public String getDefaultAddNewCaption() {
		return messages.attendanceOrProductionType();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onAddNew() {
		PayRollActions action = PayRollActions
				.newAttendanceProductionTypeAction();
		action.setCallback(new ActionCallback<ClientAttendanceOrProductionType>() {

			@Override
			public void actionResult(ClientAttendanceOrProductionType result) {
				if (type == result.getType()) {
					list.add(result);
					addItemThenfireEvent(result);
				}

				itemAdded(result);
			}
		});

		ClientAttendanceOrProductionType clientAttendanceOrProductionType = new ClientAttendanceOrProductionType();
		clientAttendanceOrProductionType.setType(type);
		action.run(clientAttendanceOrProductionType, true);
	}

	protected void itemAdded(ClientAttendanceOrProductionType obj) {
		// TODO Auto-generated method stub

	}
}
