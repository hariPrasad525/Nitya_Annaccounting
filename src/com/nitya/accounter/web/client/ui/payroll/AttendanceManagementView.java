package com.nitya.accounter.web.client.ui.payroll;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Label;
import com.nitya.accounter.web.client.core.AddNewButton;
import com.nitya.accounter.web.client.core.ClientAttendanceManagement;
import com.nitya.accounter.web.client.core.ClientAttendanceManagementItem;
import com.nitya.accounter.web.client.core.ClientCurrency;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.ui.StyledPanel;
import com.nitya.accounter.web.client.ui.core.BaseView;
import com.nitya.accounter.web.client.ui.core.ICurrencyProvider;
import com.nitya.accounter.web.client.ui.forms.DynamicForm;

public class AttendanceManagementView extends
		BaseView<ClientAttendanceManagement> {

	AttendanceManagementTable table;
	private AddNewButton itemTableButton;
	private StyledPanel mainVLay;

	public AttendanceManagementView() {
		this.getElement().setId("AttendanceManagementView");
	}

	@Override
	public void init() {
		super.init();
		createControls();
		setSize("100%", "100%");
	}

	private void createControls() {
		Label lab1 = new Label(messages.attendanceManagement());
		lab1.setStyleName("label-title");

		table = new AttendanceManagementTable(new ICurrencyProvider() {

			@Override
			public ClientCurrency getTransactionCurrency() {
				return getCompany().getPrimaryCurrency();
			}

			@Override
			public Double getCurrencyFactor() {
				return 1.0;
			}

			@Override
			public Double getAmountInBaseCurrency(Double amount) {
				return amount;
			}
		});
		table.setEnabled(!isInViewMode());

		this.mainVLay = new StyledPanel("mainVLay");
		mainVLay.add(lab1);
		mainVLay.add(table);

		this.add(mainVLay);
	}

	protected void addItem() {
		ClientAttendanceManagementItem transactionItem = new ClientAttendanceManagementItem();
		table.add(transactionItem);

	}

	@Override
	public void initData() {
		if (getData() == null) {
			setData(new ClientAttendanceManagement());
		} else {
			initViewData(getData());
		}
		super.initData();
	}

	private void initViewData(ClientAttendanceManagement data) {

		table.setAllRows(data.getItems());
	}

	@Override
	public void deleteFailed(AccounterException caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {
		// TODO Auto-generated method stub

	}

	@Override
	protected String getViewTitle() {
		return messages.attendanceManagement();
	}

	@Override
	public List<DynamicForm> getForms() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveAndUpdateView() {
		updateData();
	}

	private void updateData() {
		data.setItems(table.getAllRows());
	}

	@Override
	protected void createButtons() {
		super.createButtons();
		itemTableButton = new AddNewButton();
		itemTableButton.setEnabled(!isInViewMode());
		itemTableButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				addItem();
			}
		});

		addButton(mainVLay, itemTableButton);
	}

	@Override
	protected void clearButtons() {
		super.clearButtons();
		removeButton(mainVLay, itemTableButton);
	}
}
