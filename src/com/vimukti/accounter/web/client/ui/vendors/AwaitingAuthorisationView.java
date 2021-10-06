package com.vimukti.accounter.web.client.ui.vendors;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientCashPurchase;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Lists.BillsList;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.ButtonBar;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;

public class AwaitingAuthorisationView extends BaseView {
	AwaitingAuthorisationgrid grid;
	public boolean isProcessingAdded;

	public AwaitingAuthorisationView() {
		init();
	}

	@Override
	public void init() {
		super.init();
		this.getElement().setId("AwaitingAuthorisationView");
		createControls();
	}

	private void createControls() {
		StyledPanel panel = new StyledPanel("panel");

		initGrid();

		StyledPanel buttonPanel = new StyledPanel("buttonPanel");
		// buttonPanel.getElement().getStyle().setMarginTop(15, Unit.PX);

		Button approve = new Button(messages.approve());
		approve.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				isProcessingAdded = false;
				// setAction(ActionFactory.getAwaitingAuthorisationAction());
				// boolean isErrorOccured = checkPayFromAccount();
				List<BillsList> records = getRecordsToApprove();
				// if (!isErrorOccured)
				if (records.size() > 0) {
					updateRecords(records,
							ClientCashPurchase.EMPLOYEE_EXPENSE_STATUS_APPROVED);
					// else
					if (records.size() != grid.getSelectedRecords().size())
						Accounter.showError(messages
								.pleaseSelectPayFromAccount());
				} else {
					Accounter.showInformation(messages.noRecordsToShow());
				}

			}
		});

		Button decline = new Button(messages.decline());
		decline.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				isProcessingAdded = false;
				// setAction(ActionFactory.getAwaitingAuthorisationAction());
				List<BillsList> records = getRecordsToApprove();
				if (records.size() > 0) {
					updateRecords(grid.getSelectedRecords(),
							ClientCashPurchase.EMPLOYEE_EXPENSE_STATUS_DECLINED);
				} else {
					Accounter.showInformation(messages.noRecordsToShow());
				}
			}
		});

		Button delete = new Button(messages.delete());
		delete.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				isProcessingAdded = false;
				// setAction(ActionFactory.getAwaitingAuthorisationAction());
				List<BillsList> records = getRecordsToApprove();
				if (records.size() > 0) {
					updateRecords(grid.getSelectedRecords(),
							ClientCashPurchase.EMPLOYEE_EXPENSE_STATUS_DELETE);
				} else {
					Accounter.showInformation(messages.noRecordsToShow());
				}
			}
		});

		buttonPanel.add(approve);
		buttonPanel.add(decline);
		buttonPanel.add(delete);
		approve.getElement().getStyle().setMarginLeft(25, Unit.PX);
//		approve.setWidth("105px");

		panel.add(grid);
		panel.add(buttonPanel);
		this.add(panel);
		// mainPanel.removeStyleName("main-class-pannel");
	}

	protected List<BillsList> getRecordsToApprove() {
		List<BillsList> records = new ArrayList<BillsList>();
		for (BillsList r : grid.getSelectedRecords()) {
			if (r.getPayFrom() != 0)
				records.add(r);
		}
		return records;
	}

	protected boolean checkPayFromAccount() {
		boolean isErrorOccured = false;
		List<BillsList> selectedRecords = grid.getSelectedRecords();
		for (BillsList record : selectedRecords) {
			if (record.getPayFrom() == 0) {
				isErrorOccured = true;
			}
		}
		return isErrorOccured;
	}

	private void initGrid() {
		grid = new AwaitingAuthorisationgrid(true);
		grid.init();
//		grid.setSize("100%", "100%");
	}

	protected void updateRecords(List<BillsList> records,
			final int expenceStatus) {

		for (BillsList record : records) {
			Accounter.createGETService().getObjectById(
					AccounterCoreType.CASHPURCHASE, record.getTransactionId(),
					new AccounterAsyncCallback<ClientCashPurchase>() {

						@Override
						public void onException(AccounterException caught) {

						}

						@Override
						public void onResultSuccess(ClientCashPurchase result) {
							result.setExpenseStatus(expenceStatus);
							// setAction(ActionFactory.getExpenseClaimsAction(2));
							updateTransactionItems(result);
							saveOrUpdate(result);
						}
					});
		}
	}

	void updateTransactionItems(ClientCashPurchase result) {
		for (ClientTransactionItem item : result.getTransactionItems()) {
			item.setID(0);
		}
	}

	@Override
	protected void initRPCService() {
		super.initRPCService();
		String userName = null;
		if (!Accounter.getUser().isAdminUser()) {
			userName = Accounter.getUser().getName();
		} else {
			userName = null;
		}

		Accounter
				.createHomeService()
				.getEmployeeExpensesByStatus(
						userName,
						ClientCashPurchase.EMPLOYEE_EXPENSE_STATUS_SUBMITED_FOR_APPROVAL,
						new AccounterAsyncCallback<ArrayList<BillsList>>() {

							@Override
							public void onResultSuccess(
									ArrayList<BillsList> result) {
								for (BillsList list : result)
									grid.addData(list);
							}

							@Override
							public void onException(AccounterException caught) {

							}
						});
	}

	@Override
	public List<DynamicForm> getForms() {

		return null;
	}

	@Override
	public void deleteFailed(AccounterException caught) {

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {

	}

	@Override
	protected String getViewTitle() {
		return messages.awaitingAuthorisation();
	}

	@Override
	protected void createButtons() {
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean canDelete() {
		return false;
	}

	@Override
	protected boolean canVoid() {
		return false;
	}
}
