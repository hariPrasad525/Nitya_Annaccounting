package com.vimukti.accounter.web.client.ui.grids;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientMessageOrTask;
import com.vimukti.accounter.web.client.core.ClientUserInfo;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;

public class MessagesAndTasksGrid extends BaseListGrid<ClientMessageOrTask> {

	public MessagesAndTasksGrid() {
		super(false);
		this.getElement().setId("MessagesAndTasksGrid");
	}

	@Override
	public void init() {
		super.init();
	}

	@Override
	protected Object getColumnValue(ClientMessageOrTask obj, int index) {
		switch (index) {
		case 0:
			if (obj.getType() == 1) {
				return Accounter.getFinanceImages().message();
			} else if (obj.getType() == 2) {
				return Accounter.getFinanceImages().task();
			} else {
				return Accounter.getFinanceImages().warningicon();
			}
		case 1:
			if (obj.isSystemCreated()) {
				return messages.accounter();
			} else {
				return Accounter.getCompany().getUserById(obj.getCreatedBy())
						.getFirstName();
			}

		case 2:
			return getToUserName(obj);
		case 3:
			return obj.getContent();
		case 4:
			return UIUtils.getDateByCompanyType(new ClientFinanceDate(obj
					.getDate()));
		case 5:
			return Accounter.getFinanceMenuImages().delete();
		}
		return null;
	}

	private String getToUserName(ClientMessageOrTask obj) {
		long toUser = obj.getToUser();
		if (toUser == ClientMessageOrTask.TO_USER_TYPE_ALL) {
			return messages.allUsers();
		}
		ClientUserInfo userById = Accounter.getCompany().getUserById(toUser);
		return userById.getFirstName();
	}

	@Override
	protected String[] getSelectValues(ClientMessageOrTask obj, int index) {
		return null;
	}

	@Override
	protected boolean isEditable(ClientMessageOrTask obj, int row, int index) {
		return false;
	}

	@Override
	protected void onClick(ClientMessageOrTask obj, int row, int index) {
		if (index == 2) {
			Accounter.getMainFinanceWindow().historyChanged(
					obj.getActionToken());
		} else if (index == 5) {
			if (obj != null)
				showWarnDialog(obj);
		}
	}

	@Override
	protected int getCellWidth(int index) {

		switch (index) {
		case 0:
			return 40;
		case 1:
			return 60;
		case 2:
			return 60;
		case 3:
			return 143;
		case 4:
			return 100;
		case 5:
			if (UIUtils.isMSIEBrowser()) {
				return 25;
			} else {
				return 20;
			}
		}
		return -1;
	}

	@Override
	protected String[] setHeaderStyle() {
		return new String[] { "type", "from", "to", "item", "date", "delete" };
	}

	@Override
	protected String[] getColumns() {
		return new String[] { messages.type(), messages.from(), messages.to(),
				messages.item(), messages.date(), "" };
	}

	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_IMAGE,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_DATE,
				ListGrid.COLUMN_TYPE_IMAGE };
	}

	@Override
	protected void executeDelete(ClientMessageOrTask data) {
		deleteObject(data);
	}

	@Override
	protected void onValueChange(ClientMessageOrTask obj, int index,
			Object value) {

	}

	@Override
	public void onDoubleClick(ClientMessageOrTask obj) {
		// TODO Auto-generated method stub

	}

	@Override
	protected String[] setRowElementsStyle() {
		return new String[] { "type-value", "from-value", "to-value",
				"item-value", "date-value", "delete-value" };
	}

}
