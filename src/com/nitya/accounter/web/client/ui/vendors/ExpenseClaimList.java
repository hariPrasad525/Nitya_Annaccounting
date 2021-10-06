package com.nitya.accounter.web.client.ui.vendors;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.nitya.accounter.web.client.AccounterAsyncCallback;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.ClientCashPurchase;
import com.nitya.accounter.web.client.core.ClientTransactionItem;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.core.Lists.BillsList;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.StyledPanel;
import com.nitya.accounter.web.client.ui.core.Action;
import com.nitya.accounter.web.client.ui.core.BaseView;
import com.nitya.accounter.web.client.ui.core.ButtonBar;
import com.nitya.accounter.web.client.ui.core.EditMode;
import com.nitya.accounter.web.client.ui.forms.DynamicForm;

/**
 * 
 * @author Uday Kumar
 * 
 */
public class ExpenseClaimList extends BaseView {

	ExpenseClaimGrid grid;
	public boolean isProcessingAdded;

	public ExpenseClaimList() {
		init();
	}

	@Override
	public void init() {
		super.init();
		this.getElement().setId("ExpenseClaimList");
		createControls();
	}

	private void createControls() {

		StyledPanel panel = new StyledPanel("panel");
		HTML addNew = new HTML(messages.addNewEmployeeExpense());
		addNew.setStyleName("add-new-expense");
		addNew.getElement().getStyle().setMarginBottom(10, Unit.PX);
		addNew.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				new EmployeeExpenseAction().run(null, false);
			}
		});
		initGrid();

		StyledPanel buttonPanel = new StyledPanel("buttonPanel");
		buttonPanel.setStyleName("button-expense");
		Button submitApproval = new Button(messages.submitForApproval());
		submitApproval.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				isProcessingAdded = false;
				setAction(new ExpenseClaimsAction(0));

				List<BillsList> records = grid.getSelectedRecords();
				if (records.size() > 0) {
					updateSelectedRecords(
							records,
							ClientCashPurchase.EMPLOYEE_EXPENSE_STATUS_SUBMITED_FOR_APPROVAL);
				} else {
					Accounter.showInformation(messages.noRecordsToShow());
				}

			}
		});

		Button deleteButton = new Button(messages.delete());
		deleteButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				isProcessingAdded = false;
				setAction(new ExpenseClaimsAction(0));
				List<BillsList> records = grid.getSelectedRecords();
				if (records.size() > 0) {
					updateSelectedRecords(records,
							ClientCashPurchase.EMPLOYEE_EXPENSE_STATUS_DELETE);
				} else {
					Accounter.showInformation(messages.noRecordsToShow());
				}
			}
		});
		buttonPanel.add(submitApproval);
		buttonPanel.add(deleteButton);
		panel.add(addNew);
		panel.add(grid);
		panel.add(buttonPanel);
		this.add(panel);
		this.removeStyleName("main-class-pannel");
	}

	private void initGrid() {
		grid = new ExpenseClaimGrid(true);
		grid.isEnable = false;
		grid.init();
		grid.setView(this);
		// grid.setSize("100%", "100%");
	}

	protected void updateSelectedRecords(List<BillsList> records,
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
							updateTransactionItems(result);
							setAction(new ExpenseClaimsAction(0));
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
		Accounter.createHomeService().getEmployeeExpensesByStatus(userName,
				ClientCashPurchase.EMPLOYEE_EXPENSE_STATUS_SAVE,
				new AccounterAsyncCallback<ArrayList<BillsList>>() {

					@Override
					public void onResultSuccess(ArrayList<BillsList> result) {
						if (result.size() > 0) {
							for (BillsList list : result)
								grid.addData(list);
						} else {
							grid.addEmptyMessage(messages.noRecordsToShow());
						}

					}

					@Override
					public void onException(AccounterException caught) {

					}
				});
	}

	@Override
	public void setAction(Action action) {
		// action = ActionFactory.getExpenseClaimListAction();
		super.setAction(action);
	}

	/**
	 * THIS METHOD DID N'T USED ANY WHERE IN THE PROJECT.
	 */
	@Override
	public List<DynamicForm> getForms() {
		return null;
	}

	@Override
	public void onEdit() {
		setMode(EditMode.EDIT);
	}

	@Override
	public void print() {
		// TODO Auto-generated method stub
	}

	@Override
	public void printPreview() {
		// NOTHING TO DO
	}

	@Override
	public void deleteFailed(AccounterException caught) {
		// TODO Auto-generated method stub
	}

	@Override
	public void deleteSuccess(IAccounterCore result) {

	}

	@Override
	protected String getViewTitle() {
		return messages.expenseClaims();
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
