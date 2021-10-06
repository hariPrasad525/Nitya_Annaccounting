/**
 * 
 */
package com.vimukti.accounter.web.client.ui.banking;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.view.client.Range;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ValueCallBack;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientReconciliation;
import com.vimukti.accounter.web.client.core.ClientReconciliationItem;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.ReconciliationDialog;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.core.SelectionChangedHandler;
import com.vimukti.accounter.web.client.ui.core.ViewManager;
import com.vimukti.accounter.web.client.ui.forms.AmountLabel;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.LabelItem;
import com.vimukti.accounter.web.client.ui.widgets.DateUtills;

/**
 * @author Prasanna Kumar G
 * 
 */
public class ReconciliationView extends BaseView<ClientReconciliation> {

	private StyledPanel mainPanel;
	private ReconciliationTransactionsGrid grid;
	private Set<ClientReconciliationItem> clearedTransactions = new HashSet<ClientReconciliationItem>();
	private LabelItem bankaccountLabel, startdateLable, enddateLable;
	AmountLabel closebalanceLable, openingBalance, closingBalance,
			clearedAmount, difference;

	public ReconciliationView() {
		super();
		this.getElement().setId("ReconciliationView");
	}

	public void createControls() {
		// Creating Title for View
		Label label = new Label();
		label.removeStyleName("gwt-Label");
		label.addStyleName("label-title");
		label.setText(messages.Reconciliation());

		bankaccountLabel = new LabelItem(messages.Account(), "bankaccountLabel");
		bankaccountLabel.setValue(data.getAccount().getName());

		closebalanceLable = new AmountLabel(messages.ClosingBalance());
		closebalanceLable.setTitle(messages.ClosingBalance());
		closebalanceLable.setAmount(data.getClosingBalance());

		startdateLable = new LabelItem(messages.startDate(), "startdateLable");
		startdateLable.setValue(DateUtills.getDateAsString(data.getStartDate()
				.getDateAsObject()));

		enddateLable = new LabelItem(messages.endDate(), "enddateLable");
		enddateLable.setValue(DateUtills.getDateAsString(data.getEndDate()
				.getDateAsObject()));

		// // Creating Input Fields to Start Reconciliation of an Account
		// bankAccountsField = new SelectCombo(messages.bankAccount(Global.get()
		// .Account()));
		//
		// bankAccountsField
		// .addSelectionChangeHandler(new
		// IAccounterComboSelectionChangeHandler<String>() {
		//
		// @Override
		// public void selectedComboBoxItem(String selectItem) {
		// // TODO Auto-generated method stub
		//
		// }
		// });
		//
		// startDate = new DateField(constants.startDate());
		// endDate = new DateField(constants.endDate());
		// closingBalance = new TextItem(constants.ClosingBalance());

		DynamicForm form = new DynamicForm("form");
		form.add(bankaccountLabel, closebalanceLable);
		form.setStyleName("recouncilation_value");

		DynamicForm datesForm = new DynamicForm("datesForm");
		datesForm.add(startdateLable, enddateLable);

		StyledPanel hPanel = new StyledPanel("hPanel");
		// hPanel.setWidth("100%");
		hPanel.add(form);
		hPanel.add(datesForm);
		// hPanel.setCellWidth(form, "40%");
		Button changeBtn = new Button(messages.change());
		changeBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				ReconciliationDialog dialog = new ReconciliationDialog(Global
						.get().messages().Reconciliation(), data,
						new ValueCallBack<ClientReconciliation>() {

							@Override
							public void execute(ClientReconciliation value) {
								updateData(value);
							}
						});
				ViewManager.getInstance().showDialog(dialog);
			}
		});

		StyledPanel btnPanel = new StyledPanel("btnPanel");
		// btnPanel.setWidth("100%");
		btnPanel.add(changeBtn);
		btnPanel.setStyleName("recoucilation_change");
		// btnPanel.setCellHorizontalAlignment(changeBtn,
		// HasHorizontalAlignment.ALIGN_RIGHT);

		// Creating Amount Fields
		StyledPanel amountsPanel = new StyledPanel("amountsPanel");

		openingBalance = new AmountLabel(messages.openingBalance());
		openingBalance.setEnabled(false);
		openingBalance.setAmount(data.getOpeningBalance());

		closingBalance = new AmountLabel(messages.ClosingBalance());
		closingBalance.setEnabled(true);
		closingBalance.setAmount(data.getClosingBalance());

		clearedAmount = new AmountLabel(messages.ClearedAmount());
		clearedAmount.setEnabled(true);

		difference = new AmountLabel(messages.Difference());
		difference.setEnabled(false);
		DynamicForm amountsForm = new DynamicForm("amountsForm");
		// amountsForm.setWidth("50%");
		amountsForm.add(openingBalance, closingBalance, clearedAmount,
				difference);
		amountsPanel.add(amountsForm);
		amountsPanel.setStyleName("bottom_total_view");

		this.grid = new ReconciliationTransactionsGrid(this,
				new SelectionChangedHandler<ClientReconciliationItem>() {

					@Override
					public void selectionChanged(ClientReconciliationItem obj,
							boolean isSelected) {
						if (isCreating()) {
							clearTransaction(obj, isSelected);
						}
					}
				});
		// grid.setWidth("100%");
		// grid.setHeight("200px");

		this.mainPanel = new StyledPanel("mainPanel");
		StyledPanel gridPanel = new StyledPanel("reconcillation_grid_panel");
		mainPanel.add(label);
		mainPanel.add(hPanel);
		if (isCreating()) {
			mainPanel.add(btnPanel);
		}
		gridPanel.add(grid);
		mainPanel.add(gridPanel);
		// mainPanel.setCellHeight(grid, "200px");
		grid.getElement().getParentElement()
				.addClassName("recounciliation_grid");
		mainPanel.add(amountsPanel);
		this.add(mainPanel);
	}

	private void updateData(ClientReconciliation reconciliation) {
		clearedTransactions.clear();
		bankaccountLabel.setValue(reconciliation.getAccount().getName());
		closebalanceLable.setAmount(reconciliation.getClosingBalance());
		startdateLable.setValue(DateUtills.getDateAsString(reconciliation
				.getStartDate().getDateAsObject()));
		enddateLable.setValue(DateUtills.getDateAsString(reconciliation
				.getEndDate().getDateAsObject()));
		closingBalance.setAmount(reconciliation.getClosingBalance());
		startdateLable.setValue(reconciliation.getStartDate().toString());
		enddateLable.setValue(reconciliation.getEndDate().toString());
		bankaccountLabel.setValue(reconciliation.getAccount().getName());
		setData(reconciliation);
		initData();
	}

	/**
	 * @return
	 */
	private boolean isCreating() {
		return data.getID() == 0;
	}

	/**
	 * @param value
	 */
	protected void clearTransaction(ClientReconciliationItem value,
			boolean isClear) {
		if (isClear) {
			clearedTransactions.add(value);
		} else {
			clearedTransactions.remove(value);
		}
		double transactionAmount = value.getAmount();

		if (isClear) {
			double differenceAmount = closingBalance.getAmount()
					- (openingBalance.getAmount() + clearedAmount.getAmount() + transactionAmount);
			difference.setAmount(DecimalUtil.round(differenceAmount));
			clearedAmount.setAmount(clearedAmount.getAmount()
					+ transactionAmount);
		} else {
			difference.setAmount(difference.getAmount() + transactionAmount);
			clearedAmount.setAmount(clearedAmount.getAmount()
					- transactionAmount);
		}
	}

	/**
	 * Gets All the Transaction in between StartDate and EndDate
	 */

	private void getTransactions() {
		rpcGetService
				.getAllTransactionsOfAccount(
						data.getAccount().getID(),
						data.getStartDate(),
						data.getEndDate(),
						new AccounterAsyncCallback<ArrayList<ClientReconciliationItem>>() {

							@Override
							public void onException(AccounterException exception) {
								Accounter.showError(messages
										.unableToGet(messages.transactions()));
							}

							@Override
							public void onResultSuccess(
									ArrayList<ClientReconciliationItem> result) {
								if (result == null) {
									result = new ArrayList<ClientReconciliationItem>();
								}
								grid.setVisibleRangeAndClearData(new Range(0,
										50), true);
								grid.setData(result);
							}
						});
	}

	private void setOpeningBalance() {
		clearedAmount.setAmount(0.00D);
		rpcGetService.getOpeningBalanceforReconciliation(data.getAccount()
				.getID(), new AccounterAsyncCallback<Double>() {

			@Override
			public void onException(AccounterException exception) {
				Accounter.showError(messages.unableToGet(messages.openBalance()));
			}

			@Override
			public void onResultSuccess(Double result) {
				if (result == 0.0) {
					openingBalance.setAmount(data.getAccount()
							.getOpeningBalance());
				} else {
					openingBalance.setAmount(result);
				}
				difference.setAmount(closingBalance.getAmount()
						- openingBalance.getAmount()
						- clearedAmount.getAmount());
			}
		});
	}

	@Override
	public void init() {
		super.init();
		this.getElement().setId("reconcilationView");
		createControls();
	}

	@Override
	public ValidationResult validate() {
		clearAllErrors();
		ValidationResult result = new ValidationResult();
		if (!DecimalUtil.isEquals(difference.getAmount(), 0.00D)) {
			result.addError(difference, messages.differenceValidate());
		}
		if (clearedTransactions.isEmpty()) {
			result.addError(clearedTransactions,
					messages.thereIsNoTransactionsToReconcile());
		}
		return result;
	}

	@Override
	public void saveAndUpdateView() {
		this.data.setOpeningBalance(openingBalance.getAmount());
		this.data.setReconcilationDate(new ClientFinanceDate());
		data.setItems(this.clearedTransactions);
		saveOrUpdate(data);
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DynamicForm> getForms() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onEdit() {
		// TODO Auto-generated method stub
		super.onEdit();
	}

	@Override
	public boolean canEdit() {
		return false;
	}

	@Override
	public void initData() {
		if (!isCreating()) {
			ClientReconciliation data = this.getData();
			grid.setData(new ArrayList<ClientReconciliationItem>(data
					.getTransactions()));
			clearedAmount.setAmount(data.getClosingBalance());
		} else {
			getTransactions();
			setOpeningBalance();
		}
		super.initData();
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean canVoid() {
		return false;
	}
}
