//package com.vimukti.accounter.web.client.ui.customers;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import com.google.gwt.user.client.rpc.IsSerializable;
//import com.google.gwt.user.client.ui.CheckBox;
//import com.google.gwt.user.client.ui.StyledPanel;
//import com.vimukti.accounter.web.client.core.ClientCreditsAndPayments;
//import com.vimukti.accounter.web.client.core.ClientCurrency;
//import com.vimukti.accounter.web.client.core.ClientCustomer;
//import com.vimukti.accounter.web.client.core.ClientTransactionCreditsAndPayments;
//import com.vimukti.accounter.web.client.core.ClientTransactionPayBill;
//import com.vimukti.accounter.web.client.core.ClientTransactionReceivePayment;
//import com.vimukti.accounter.web.client.core.IAccounterCore;
//import com.vimukti.accounter.web.client.ui.DataUtils;
//import com.vimukti.accounter.web.client.ui.UIUtils;
//import com.vimukti.accounter.web.client.ui.core.AmountField;
//import com.vimukti.accounter.web.client.ui.core.BaseDialog;
//import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
//import com.vimukti.accounter.web.client.ui.edittable.tables.TransactionPayBillTable;
//import com.vimukti.accounter.web.client.ui.edittable.tables.TransactionReceivePaymentTable;
//import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
//import com.vimukti.accounter.web.client.ui.grids.CreditsandPaymentsGrid;
//import com.vimukti.accounter.web.client.ui.grids.ListGrid;
//
///**
// * 
// * @author Rajesh Akkineni
// * 
// * 
// */
//
//public class CustomerCreditsAndPaymentsDialiog extends
//		BaseDialog<ClientCreditsAndPayments> {
//
//	AmountField amtDueText, totAmtUseText;
//
//	public CreditsandPaymentsGrid grid;
//
//	public Double totalBalances = 0.0D;
//	public Double totalAmountToUse = 0.0D;
//
//	private boolean canEdit;
//
//	public double adjustPayment = 0.0D;
//
//	private double initialAdjustPayment = 0.0D;
//
//	private List<ClientCreditsAndPayments> updatedCreditsAndPayments;
//
//	private ClientCurrency currency;
//
//	private double maxAmountRequired;
//
//	public CustomerCreditsAndPaymentsDialiog(
//			List<ClientCreditsAndPayments> updatedCustomerCreditsAndPayments,
//			ClientCurrency currency, double maxAmountRequired, boolean canEdit) {
//		super(messages.applyCreditsAndPaymentsFor(), messages
//				.applyCreditsandPayments());
//		this.maxAmountRequired = maxAmountRequired;
//		this.canEdit = canEdit;
//		updatedCreditsAndPayments = updatedCustomerCreditsAndPayments;
//		this.currency = currency;
//		createControls();
//		grid.initialCreditsAndPayments(updatedCustomerCreditsAndPayments);
//		addGridRecordsToListGrid(updatedCreditsAndPayments);
//
//	}
//
//	public double getTotalCreditAmount() {
//		double totalCreditApplied = 0.0;
//		for (ClientCreditsAndPayments crd : grid.getSelectedRecords()) {
//			totalCreditApplied += crd.getAmtTouse();
//		}
//		return totalCreditApplied;
//	}
//
//	public double getTotalUnuseCreditAmount() {
//		double totalUnusedCreditAmount = 0.0;
//		for (ClientCreditsAndPayments crd : grid.getSelectedRecords()) {
//			totalUnusedCreditAmount += crd.getBalance();
//		}
//		return totalUnusedCreditAmount;
//	}
//
//	public void setCanEdit(boolean canEdit) {
//		this.canEdit = canEdit;
//	}
//
//	public void setUpdatedCreditsAndPayments(
//			List<ClientCreditsAndPayments> updatedCreditsAndPayments) {
//		this.updatedCreditsAndPayments = updatedCreditsAndPayments;
//		grid.initialCreditsAndPayments(updatedCreditsAndPayments);
//		addGridRecordsToListGrid(updatedCreditsAndPayments);
//
//	}
//
//	/*
//	 * This method invoked when record values are changed.It updates the
//	 * non-editable fields
//	 */
//	public void updateFields() {
//		setTotalAmountToUse(totalAmountToUse);
//
//		setTotalBalance(totalBalances);
//
//		setAdjustPaymentText(totalAmountToUse);
//
//	}
//
//	/*
//	 * This method invoked to set the records to grid and it also updates the
//	 * non-editable fileds
//	 */
//	public void addGridRecordsToListGrid(
//			List<ClientCreditsAndPayments> creditsAndPayments) {
//		if (creditsAndPayments != null && creditsAndPayments.size() != 0) {
//			grid.removeAllRecords();
//			grid.setRecords(creditsAndPayments);
//		} else {
//			grid.addEmptyMessage(messages.therearenocreditstoshow());
//		}
//		for (ClientCreditsAndPayments cr : creditsAndPayments) {
//			int row = grid.indexOf(cr);
//			if (!DecimalUtil.isEquals(cr.getAmtTouse(), 0.0))
//				((CheckBox) grid.getWidget(row, 0)).setValue(true);
//		}
//
//		double toBeSetTotalBalance = 0.0;
//		double toBeSetTotalAmtTouse = 0.0d;
//		for (ClientCreditsAndPayments cpRecord : creditsAndPayments) {
//			toBeSetTotalBalance += cpRecord.getBalance();
//		}
//
//		totalBalances = toBeSetTotalBalance;
//
//		if (!canEdit) {
//			totalAmountToUse = toBeSetTotalAmtTouse;
//			totalBalances = toBeSetTotalBalance;
//			updateFields();
//		}
//
//	}
//
//	private void createControls() {
//		mainPanel.setSpacing(5);
//
//		amtDueText = new AmountField(messages.amountDue(), this,
//				getBaseCurrency());
//		amtDueText.setColSpan(1);
//		amtDueText.setValue(amountAsString(maxAmountRequired));
//		amtDueText.setDisabled(true);
//
//		if (canEdit) {
//			adjustPayment = maxAmountRequired;
//
//			initialAdjustPayment = adjustPayment;
//
//		}
//		totAmtUseText = new AmountField(messages.totalAmountToUse(), this,
//				getBaseCurrency());
//		totAmtUseText.setColSpan(1);
//		totAmtUseText.setDisabled(true);
//		totAmtUseText.setValue("" + UIUtils.getCurrencySymbol() + "0.00");
//
//		DynamicForm form = new DynamicForm();
//		form.setNumCols(4);
//		form.setWidth("100%");
//		if (canEdit)
//			form.setFields(amtDueText, totCredAmtText, cashDiscText,
//					totBalText, adjPayText, totAmtUseText);
//		else
//			form.setFields(totCredAmtText, cashDiscText, totBalText,
//					adjPayText, totAmtUseText);
//
//		grid = new CreditsandPaymentsGrid(true, this, currency, this.record);
//		grid.isEnable = false;
//		grid.init();
//		grid.setWidth("100%");
//		grid.setHeight("200px");
//		grid.setCanEdit(canEdit);
//		grid.setEditEventType(ListGrid.EDIT_EVENT_CLICK);
//
//		if (canEdit) {
//			okbtn.setTitle(messages.adjust());
//		} else {
//			cancelBtn.setTitle(messages.close());
//			okbtn.setVisible(false);
//		}
//
//		StyledPanel mainVLay = new StyledPanel();
//		mainVLay.setWidth("100%");
//		mainVLay.add(form);
//		mainVLay.add(grid);
//
//		setBodyLayout(mainVLay);
//		setWidth("600px");
//		center();
//	}
//
//	// public void validateTransaction() throws Exception {
//	// if (!grid.validateGrid()) {
//	// throw new Exception(messages
//	// .selectatleastSelectRecord());
//	// }
//	// }
//
//	private void setAdjustPaymentText(double totalAmountToUse) {
//		adjustPayment = initialAdjustPayment - totalAmountToUse;
//		if (adjPayText == null)
//			return;
//		if (!DecimalUtil.isLessThan(adjustPayment, 0))
//			this.adjPayText.setValue(amountAsString(adjustPayment));
//		else
//			this.adjPayText.setValue("0.0");
//
//	}
//
//	private void setTotalBalance(Double totalBalances) {
//		if (totalBalances == null)
//			totalBalances = 0.0D;
//
//		totBalText.setValue(amountAsString(totalBalances));
//
//		this.totalBalances = totalBalances;
//
//	}
//
//	private void setTotalAmountToUse(Double totalAmountToUse) {
//		if (totalAmountToUse == null)
//			totalAmountToUse = 0.0;
//
//		totAmtUseText.setValue(amountAsString(getTotalCreditAmount()));
//
//		this.totalAmountToUse = totalAmountToUse;
//
//	}
//
//	// public void refreshValues() {
//	// grid.refreshValues();
//	// }
//
//	@Override
//	public Object getGridColumnValue(ClientCustomer obj, int index) {
//		return null;
//	}
//
//	/*
//	 * This method invoked when "ok" button clicked.It'll set all the applied
//	 * credits to the record using setAttribute() in PayBill/RecievPayment Grid
//	 */
//	public List<ClientTransactionCreditsAndPayments> getAppliedCredits() {
//		List<ClientTransactionCreditsAndPayments> clientTransactionCreditsAndPayments = new ArrayList<ClientTransactionCreditsAndPayments>();
//		for (IsSerializable obj : grid.getSelectedRecords()) {
//			ClientCreditsAndPayments crdPayment = (ClientCreditsAndPayments) obj;
//			crdPayment.setBalance(crdPayment.getBalance());
//			ClientTransactionCreditsAndPayments creditsAndPayments = new ClientTransactionCreditsAndPayments();
//			try {
//				creditsAndPayments.setAmountToUse(DataUtils
//						.getAmountStringAsDouble(totAmtUseText.getValue()
//								.toString()));
//			} catch (Exception e) {
//			}
//			creditsAndPayments.setCreditsAndPayments(crdPayment);
//			clientTransactionCreditsAndPayments.add(creditsAndPayments);
//		}
//		return clientTransactionCreditsAndPayments;
//	}
//
//	/*
//	 * This method returns final records for saving and is called from the
//	 * transaction when it is going to save
//	 */
//
//	public List<ClientTransactionCreditsAndPayments> getTransactionCredits(
//			IAccounterCore transctn) {
//
//		List<ClientTransactionCreditsAndPayments> clientTransactionCreditsAndPayments = new ArrayList<ClientTransactionCreditsAndPayments>();
//		if (transctn instanceof ClientTransactionPayBill) {
//			ClientTransactionPayBill trPayBill = (ClientTransactionPayBill) transctn;
//			if (trPayBill.getTempCredits() != null) {
//				for (Integer indx : trPayBill.getTempCredits().keySet()) {
//					ClientCreditsAndPayments crdPayment = grid.getRecords()
//							.get(indx);
//					crdPayment.setBalance(crdPayment.getActualAmt());
//					crdPayment
//							.setAmtTouse(((TransactionPayBillTable.TempCredit) trPayBill
//									.getTempCredits().get(indx))
//									.getAmountToUse());
//					// }
//					// for (IsSerializable obj : grid.getSelectedRecords()) {
//					// ClientCreditsAndPayments crdPayment =
//					// (ClientCreditsAndPayments)
//					// obj;
//					/*
//					 * For backnd purpose,they need the original dueamount(the
//					 * calculations(decreasing balance by 'amountToUse') are
//					 * done at backend side)
//					 */
//					// crdPayment.setBalance(crdPayment.getActualAmt());
//					ClientTransactionCreditsAndPayments creditsAndPayments = new ClientTransactionCreditsAndPayments();
//					try {
//						creditsAndPayments.setAmountToUse(crdPayment
//								.getAmtTouse());
//					} catch (Exception e) {
//					}
//					creditsAndPayments.setDate(crdPayment.getTransactionDate()
//							.getDate());
//					creditsAndPayments.setMemo(crdPayment.getMemo());
//					creditsAndPayments.setCreditsAndPayments(crdPayment);
//					clientTransactionCreditsAndPayments.add(creditsAndPayments);
//				}
//			}
//		} else {
//			ClientTransactionReceivePayment rcvPaymnt = (ClientTransactionReceivePayment) transctn;
//			for (Integer indx : rcvPaymnt.getTempCredits().keySet()) {
//				ClientCreditsAndPayments crdPayment = grid.getRecords().get(
//						indx);
//				crdPayment.setBalance(crdPayment.getActualAmt());
//				crdPayment
//						.setAmtTouse(((TransactionReceivePaymentTable.TempCredit) (rcvPaymnt
//								.getTempCredits().get(indx))).getAmountToUse());
//				// }
//				// for (IsSerializable obj : grid.getSelectedRecords()) {
//				// ClientCreditsAndPayments crdPayment =
//				// (ClientCreditsAndPayments)
//				// obj;
//				/*
//				 * For backnd purpose,they need the original dueamount(the
//				 * calculations(decreasing balance by 'amountToUse') are done at
//				 * backend side)
//				 */
//				// crdPayment.setBalance(crdPayment.getActualAmt());
//				ClientTransactionCreditsAndPayments creditsAndPayments = new ClientTransactionCreditsAndPayments();
//				try {
//					creditsAndPayments.setAmountToUse(crdPayment.getAmtTouse());
//				} catch (Exception e) {
//				}
//				creditsAndPayments.setDate(crdPayment.getTransactionDate()
//						.getDate());
//				creditsAndPayments.setMemo(crdPayment.getMemo());
//				creditsAndPayments.setCreditsAndPayments(crdPayment);
//				clientTransactionCreditsAndPayments.add(creditsAndPayments);
//			}
//		}
//		return clientTransactionCreditsAndPayments;
//	}
//
//	@Override
//	protected boolean onOK() {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public void setFocus() {
//		// TODO Auto-generated method stub
//
//	}
//}
