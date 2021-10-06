package com.nitya.accounter.web.client.ui.company;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.nitya.accounter.web.client.AccounterAsyncCallback;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.ClientFiscalYear;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.ui.AddEditFiscalYearDialog;
import com.nitya.accounter.web.client.ui.core.GroupDialog;
import com.nitya.accounter.web.client.ui.core.GroupDialogButtonsHandler;
import com.nitya.accounter.web.client.ui.core.ViewManager;

public class FiscalYearListDialog extends GroupDialog<ClientFiscalYear> {

	protected GroupDialogButtonsHandler groupDialogButtonHandler;
	protected List<ClientFiscalYear> savedFiscalYear;
	private AddEditFiscalYearDialog addEditFiscalYear;
	private Button closeButton, openButton, changeButton;

	public FiscalYearListDialog(String title, String descript) {
		super(title, descript);
		this.getElement().setId("FiscalYearListDialog");
		initialise();
		getListFiscalYear();
		ViewManager.getInstance().showDialog(this);
	}

	private void getListFiscalYear() {

		AccounterAsyncCallback<ArrayList<ClientFiscalYear>> callback = new AccounterAsyncCallback<ArrayList<ClientFiscalYear>>() {

			public void onException(AccounterException caught) {

			}

			public void onResultSuccess(ArrayList<ClientFiscalYear> result) {
				savedFiscalYear = result;
				fillFiscalYear(result);
			}

		};

		// FinanceApplication.createGETService().getCom
	}

	protected void fillFiscalYear(List<ClientFiscalYear> result) {

		String period, status;

	}

	public void initialise() {
		// setHeight(450);
		// setWidth(450);
		// ListGridField period = new ListGridField("period",
		// messages
		// .period());
		// period.setWidth(90);
		// addField(period);
		// ListGridField status = new ListGridField("status",
		// messages
		// .status());
		// status.setWidth(130);
		// addField(status);
		getGrid().setType(AccounterCoreType.CREDIT_RATING);
		closeButton = new Button();
		openButton = new Button();
		changeButton = new Button();
		closeButton.setTitle(messages.closeFiscalYear());
		addButton(closeButton);
		closeButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {

			}
		});
		openButton.setTitle(messages.openFiscalYear());
		addButton(openButton);
		openButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

			}
		});
		changeButton.setTitle(messages.changeStartDate());
		addButton(changeButton);
		changeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				ViewManager.getInstance().showDialog(
						new ChangeStartDateDialog(messages.title(), messages
								.description()));
			}
		});
		// setHeight(250);

		groupDialogButtonHandler = new GroupDialogButtonsHandler() {

			public void onCloseButtonClick() {

			}

			public void onFirstButtonClick() {
				showAddEditFiscalYear(messages.createFascalYear(),
						messages.description(), null);
			}

			public void onSecondButtonClick() {
				showAddEditFiscalYear(messages.editFiscalYear(),
						messages.description(), null);
			}

			public void onThirdButtonClick() {

			}

		};
		addGroupButtonsHandler(groupDialogButtonHandler);
	}// initialise

	protected void showAddEditFiscalYear(String title, String Description,
			ClientFiscalYear fiscalYear) {
		addEditFiscalYear = new AddEditFiscalYearDialog(title, Description,
				fiscalYear);
		ViewManager.getInstance().showDialog(addEditFiscalYear);

	}

	@Override
	public Object getGridColumnValue(ClientFiscalYear obj, int index) {
		return null;
	}

	@Override
	public String[] setColumns() {
		return null;
	}

	@Override
	public String getHeaderStyle(int index) {
		return "fiscalyear";
	}

	@Override
	public String getRowElementsStyle(int index) {
		return "fiscalyear";
	}

	@Override
	protected List<ClientFiscalYear> getRecords() {
		return null;
	}

	public void deleteCallBack() {
	}

	public void addCallBack() {
	}

	public void editCallBack() {
	}

	@Override
	protected boolean onOK() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}
}
