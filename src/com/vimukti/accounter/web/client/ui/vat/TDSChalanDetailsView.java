package com.vimukti.accounter.web.client.ui.vat;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.ui.Label;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientAccounterClass;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTDSChalanDetail;
import com.vimukti.accounter.web.client.core.ClientTDSTransactionItem;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.AccountCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.PayFromAccountsCombo;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.AbstractTransactionBaseView;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.core.IntegerField;
import com.vimukti.accounter.web.client.ui.edittable.tables.TdsChalanTransactionItemsTable;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class TDSChalanDetailsView extends
		AbstractTransactionBaseView<ClientTDSChalanDetail> {

	public TDSChalanDetailsView() {
		super(ClientTransaction.TYPE_TDS_CHALLAN);
		this.getElement().setId("TDSChalanDetailsView");
	}

	private AmountField incomeTaxAmount;
	private AmountField surchargePaidAmount;
	private AmountField eduCessAmount;
	private AmountField interestPaidAmount;
	private AmountField penaltyPaidAmount;
	private AmountField otherAmountPaid;
	private AmountField totalAmountPaid;
	private SelectCombo natureOfPaymentCombo;
	private SelectCombo modeOFPaymentCombo;
	private IntegerField checkNumber;
	private DateField dateItem2;
	private TextItem bankBsrCode;
	private DynamicForm taxDynamicForm;
	private DynamicForm otherDynamicForm;
	TdsChalanTransactionItemsTable table;
	private SelectCombo chalanQuarterPeriod;
	private DateField fromDateField;
	private DateField toDateField;
	private IntegerField chalanSerialNumber;
	private SelectCombo tdsDepositedBY;
	private SelectCombo selectFormTypeCombo;
	private SelectCombo slectAssecementYear;
	protected ArrayList<ClientTDSTransactionItem> items;

	public static final int Form26Q = 1;
	public static final int Form27Q = 2;
	public static final int Form27EQ = 3;

	private int formTypeSeclected = 1;
	String assessmentYear;
	String paymentSectionSelected = null;
	// private SelectCombo natureOfPaymentCombo27Q;
	// private SelectCombo natureOfPaymentCombo27EQ;
	int modeOfPayment = 1;
	boolean bookEntry = false;
	private SelectCombo financialYearCombo;
	private DynamicForm belowForm2;
	private DynamicForm belowForm1;
	private PayFromAccountsCombo payFromAccCombo;
	protected ClientAccount selectedPayFromAccount;
	private AmountField endingBalanceText;
	protected boolean financialYearSelected = false;

	@Override
	protected void createControls() {
		Label label = new Label(messages.tdsChallan());

		transactionDateItem = createTransactionDateItem();

		label.setStyleName("label-title");
		selectFormTypeCombo = new SelectCombo(messages.formType());
		// selectFormTypeCombo.setHelpInformation(true);
		selectFormTypeCombo.initCombo(getFormTypes());
		selectFormTypeCombo.setDefaultToFirstOption(true);
		selectFormTypeCombo.setEnabled(false);
		selectFormTypeCombo.setRequired(true);
		// selectFormTypeCombo
		// .addSelectionChangeHandler(new
		// IAccounterComboSelectionChangeHandler<String>() {
		//
		// @Override
		// public void selectedComboBoxItem(String selectItem) {
		//
		// if (selectItem.equals(getFormTypes().get(0))) {
		// formTypeSeclected = Form26Q;
		// changeFormTypeStatus(formTypeSeclected);
		// } else if (selectItem.equals(getFormTypes().get(1))) {
		// formTypeSeclected = Form27Q;
		// changeFormTypeStatus(formTypeSeclected);
		// } else if (selectItem.equals(getFormTypes().get(2))) {
		// formTypeSeclected = Form27EQ;
		// changeFormTypeStatus(formTypeSeclected);
		// }
		//
		// }
		// });

		slectAssecementYear = new SelectCombo(messages.assessmentYear());
		// slectAssecementYear.setHelpInformation(true);
		slectAssecementYear.initCombo(getAssessmentYearList());
		slectAssecementYear.setEnabled(false);

		financialYearCombo = new SelectCombo(messages.financialYear());
		// financialYearCombo.setHelpInformation(true);
		financialYearCombo.initCombo(getFinancialYearList());
		financialYearCombo.setEnabled(!isInViewMode());
		financialYearCombo.setRequired(true);
		financialYearCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						slectAssecementYear.setSelected(getAssessmentYearList()
								.get(financialYearCombo.getSelectedIndex() + 1));
						assessmentYear = slectAssecementYear.getSelectedValue();

						financialYearSelected = true;

					}
				});

		incomeTaxAmount = new AmountField(messages.incomeTax(), this,
				getBaseCurrency(), "incomeTaxAmount");
		// incomeTaxAmount.setHelpInformation(true);
		incomeTaxAmount.setValue("0.00");
		incomeTaxAmount.setEnabled(false);
		incomeTaxAmount.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				updateTotalTaxCollected();
			}
		});

		surchargePaidAmount = new AmountField(messages.surchargePaid(), this,
				getBaseCurrency(), "surchargePaidAmount");
		// surchargePaidAmount.setHelpInformation(true);
		surchargePaidAmount.setValue("0.00");
		surchargePaidAmount.setEnabled(false);
		surchargePaidAmount.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				updateTotalTaxCollected();
			}
		});

		eduCessAmount = new AmountField(messages.educationCess(), this,
				getBaseCurrency(), "eduCessAmount");
		// eduCessAmount.setHelpInformation(true);
		eduCessAmount.setValue("0.00");
		eduCessAmount.setEnabled(false);
		eduCessAmount.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				updateTotalTaxCollected();
			}
		});

		interestPaidAmount = new AmountField(messages.interestPaid(), this,
				getBaseCurrency(), "interestPaidAmount");
		// interestPaidAmount.setHelpInformation(true);
		interestPaidAmount.setValue("0.00");
		interestPaidAmount.setEnabled(!isInViewMode());
		interestPaidAmount.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				updateTotalTaxCollected();
			}
		});

		penaltyPaidAmount = new AmountField(messages.penaltyPaid(), this,
				getBaseCurrency(), "penaltyPaidAmount");
		// penaltyPaidAmount.setHelpInformation(true);
		penaltyPaidAmount.setValue("0.00");
		penaltyPaidAmount.setEnabled(!isInViewMode());
		penaltyPaidAmount.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				updateTotalTaxCollected();
			}
		});

		otherAmountPaid = new AmountField(messages.otherAmountPaid(), this,
				getBaseCurrency(), "otherAmountPaid");
		// otherAmountPaid.setHelpInformation(true);
		otherAmountPaid.setValue("0.00");
		otherAmountPaid.setEnabled(!isInViewMode());
		otherAmountPaid.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				updateTotalTaxCollected();
			}
		});

		totalAmountPaid = new AmountField(messages.totalAmountPaid(), this,
				getBaseCurrency(), "totalAmountPaid");
		totalAmountPaid.setRequired(true);
		totalAmountPaid.setValue("0.00");
		totalAmountPaid.setEnabled(false);

		natureOfPaymentCombo = new SelectCombo(messages.natureOfPayment());
		if (getFormTypeSeclected() == Form26Q) {
			natureOfPaymentCombo.initCombo(get26QSectionsList());
		} else if (getFormTypeSeclected() == Form27Q) {
			natureOfPaymentCombo.initCombo(get27QSectionsList());
		} else if (getFormTypeSeclected() == Form27EQ) {
			natureOfPaymentCombo.initCombo(get27EQSectionsList());
		}

		natureOfPaymentCombo.setRequired(true);
		natureOfPaymentCombo.setEnabled(!isInViewMode());
		natureOfPaymentCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						paymentSectionSelected = selectItem;
					}
				});

		modeOFPaymentCombo = new SelectCombo(messages.paymentMethod());
		modeOFPaymentCombo.initCombo(getPaymentItems());
		modeOFPaymentCombo.setEnabled(!isInViewMode());
		modeOFPaymentCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {

						if (selectItem.equals(getPaymentItems().get(0))) {
							modeOfPayment = 1;

						} else if (selectItem.equals(getPaymentItems().get(1))) {
							modeOfPayment = 2;
						}
					}
				});

		chalanSerialNumber = new IntegerField(this, messages.challanSerialNo(),
				9);
		chalanSerialNumber.setRequired(true);
		chalanSerialNumber.setEnabled(!isInViewMode());

		chalanQuarterPeriod = new SelectCombo(messages.challanPeriod());
		chalanQuarterPeriod.initCombo(getFinancialQuatersList());
		chalanQuarterPeriod.setRequired(true);
		chalanQuarterPeriod.setSelectedItem(0);
		chalanQuarterPeriod.setEnabled(!isInViewMode());
		chalanQuarterPeriod
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						quarterChanged(selectItem);
					}
				});

		ClientFinanceDate[] dates = Utility.getFinancialQuarter(1);

		fromDateField = new DateField(messages.fromDate(), "dateItem2");
		fromDateField.setEnteredDate(dates[0]);
		fromDateField.setEnabled(false);

		toDateField = new DateField(messages.toDate(), "dateItem2");
		toDateField.setEnteredDate(dates[1]);
		toDateField.setEnabled(false);

		checkNumber = new IntegerField(this, messages.chequeOrRefNo(), 15);
		checkNumber.setEnabled(!isInViewMode());

		tdsDepositedBY = new SelectCombo(messages.tdsDepositedByBookEntry());
		tdsDepositedBY.initCombo(getYESNOList());
		tdsDepositedBY.setRequired(true);
		tdsDepositedBY.setEnabled(!isInViewMode());
		tdsDepositedBY.setSelectedItem(1);
		tdsDepositedBY
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {

						if (selectItem.equals(getYESNOList().get(0))) {
							bookEntry = true;

						} else if (selectItem.equals(getYESNOList().get(1))) {
							bookEntry = false;
						}
					}
				});

		dateItem2 = new DateField(messages.date(), "dateItem2");
		dateItem2.setToolTip(messages.selectDateUntilDue(this.getAction()
				.getViewName()));
		dateItem2.setEnteredDate(new ClientFinanceDate());
		dateItem2.setTitle(messages.dateOnTaxPaid());
		dateItem2.setEnabled(!isInViewMode());

		bankBsrCode = new TextItem(messages.bankBSRCode(), "bankBsrCode", 7);
		bankBsrCode.setEnabled(!isInViewMode());
		bankBsrCode.setRequired(true);

		payFromAccCombo = new PayFromAccountsCombo(messages.payFrom());
		payFromAccCombo.setAccountTypes(UIUtils
				.getOptionsByType(AccountCombo.PAY_FROM_COMBO));
		payFromAccCombo.setRequired(true);
		payFromAccCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {

					public void selectedComboBoxItem(ClientAccount selectItem) {
						selectedPayFromAccount = selectItem;
						endingBalanceText.setAmount(selectedPayFromAccount
								.getTotalBalanceInAccountCurrency());
					}

				});

		payFromAccCombo.setEnabled(!isInViewMode());

		endingBalanceText = new AmountField(messages.bankBalance(), this,
				getBaseCurrency(), "endingBalanceText");
		endingBalanceText.setValue("" + UIUtils.getCurrencySymbol() + " 0.00");
		endingBalanceText.setEnabled(false);

		taxDynamicForm = new DynamicForm("taxDynamicForm");
		taxDynamicForm.add(selectFormTypeCombo, financialYearCombo,
				chalanSerialNumber, chalanQuarterPeriod, fromDateField,
				toDateField, modeOFPaymentCombo, tdsDepositedBY);

		otherDynamicForm = new DynamicForm("otherDynamicForm");
		otherDynamicForm.add(natureOfPaymentCombo, slectAssecementYear,
				checkNumber, dateItem2, bankBsrCode, payFromAccCombo,
				endingBalanceText);

		belowForm1 = new DynamicForm("belowForm1");
		belowForm1.add(incomeTaxAmount, surchargePaidAmount, eduCessAmount,
				totalAmountPaid);

		belowForm2 = new DynamicForm("belowForm2");
		belowForm2.add(interestPaidAmount, otherAmountPaid);

		// grid = new TDSTransactionItemGrid(this);
		// grid.setCanEdit(true);
		// grid.init();

		table = new TdsChalanTransactionItemsTable(getFormTypeSeclected()) {

			@Override
			public void updateNonEditableFields() {
				TDSChalanDetailsView.this.updateNonEditableItems();
			}

			@Override
			protected boolean isInViewMode() {
				return TDSChalanDetailsView.this.isInViewMode();
			}

		};
		table.setEnabled(!isInViewMode());

		DynamicForm dateNoForm = new DynamicForm("dateNoForm");
		dateNoForm.add(transactionDateItem);
		dateNoForm.setStyleName("datenumber-panel");

		StyledPanel datepanel = new StyledPanel("datepanel");
		datepanel.add(dateNoForm);

		StyledPanel labeldateNoLayout = new StyledPanel("labeldateNoLayout");
		labeldateNoLayout.add(datepanel);

		StyledPanel verticalPanel = new StyledPanel("verticalPanel");
		verticalPanel.add(label);
		verticalPanel.add(voidedPanel);
		verticalPanel.add(labeldateNoLayout);
		StyledPanel horizontalPanel1 = getFirstPanel();
		if (horizontalPanel1 != null) {
			horizontalPanel1.add(taxDynamicForm);
			horizontalPanel1.add(otherDynamicForm);
			verticalPanel.add(horizontalPanel1);
		} else {
			verticalPanel.add(taxDynamicForm);
			verticalPanel.add(otherDynamicForm);
		}
		Label tableTitle = new Label(messages2.table(messages.tdsChallan()));
		tableTitle.addStyleName("editTableTitle");
		StyledPanel tablePanel = new StyledPanel("tdschalantable");
		tablePanel.add(tableTitle);
		tablePanel.add(table);
		verticalPanel.add(tablePanel);
		StyledPanel horizontalPanel2 = getSecondPanel();
		if (horizontalPanel2 != null) {
			horizontalPanel2.add(belowForm1);
			horizontalPanel2.add(belowForm2);
			verticalPanel.add(horizontalPanel2);
		} else {
			verticalPanel.add(belowForm1);
			verticalPanel.add(belowForm2);
		}

		this.add(verticalPanel);
		//
		// natureOfPaymentCombo27Q.hide();
		// natureOfPaymentCombo27EQ.hide();
	}

	protected StyledPanel getFirstPanel() {
		return new StyledPanel("horizontalPanel1");
	}

	protected StyledPanel getSecondPanel() {
		return new StyledPanel("horizontalPanel2");
	}

	protected void quarterChanged(String selectdQuarter) {
		if (selectdQuarter.equalsIgnoreCase(messages.custom())) {
			fromDateField.setEnabled(true);
			toDateField.setEnabled(true);
		} else {
			int quarter = getFinancialQuatersList().indexOf(selectdQuarter);
			ClientFinanceDate[] dates = Utility
					.getFinancialQuarter(quarter + 1);
			fromDateField.setEnteredDate(dates[0]);
			toDateField.setEnteredDate(dates[1]);
			fromDateField.setEnabled(false);
			toDateField.setEnabled(false);
		}
	}

	// protected void changeFormTypeStatus(int formTypeSeclected) {
	//
	// if (formTypeSeclected == 1) {
	// natureOfPaymentCombo26Q.show();
	// natureOfPaymentCombo27Q.hide();
	// natureOfPaymentCombo27EQ.hide();
	//
	// natureOfPaymentCombo26Q.setRequired(true);
	// natureOfPaymentCombo27Q.setRequired(false);
	// natureOfPaymentCombo27EQ.setRequired(false);
	// } else if (formTypeSeclected == 2) {
	// natureOfPaymentCombo26Q.hide();
	// natureOfPaymentCombo27Q.show();
	// natureOfPaymentCombo27EQ.hide();
	//
	// natureOfPaymentCombo26Q.setRequired(false);
	// natureOfPaymentCombo27Q.setRequired(true);
	// natureOfPaymentCombo27EQ.setRequired(false);
	// } else if (formTypeSeclected == 3) {
	//
	// natureOfPaymentCombo26Q.hide();
	// natureOfPaymentCombo27Q.hide();
	// natureOfPaymentCombo27EQ.show();
	//
	// natureOfPaymentCombo26Q.setRequired(false);
	// natureOfPaymentCombo27Q.setRequired(false);
	// natureOfPaymentCombo27EQ.setRequired(true);
	// }
	//
	// }

	private void updateControls() {
		incomeTaxAmount.setAmount(transaction.getIncomeTaxAmount());
		surchargePaidAmount.setAmount(transaction.getSurchangePaidAmount());
		eduCessAmount.setAmount(transaction.getEducationCessAmount());
		interestPaidAmount.setAmount(transaction.getInterestPaidAmount());
		penaltyPaidAmount.setAmount(transaction.getPenaltyPaidAmount());
		otherAmountPaid.setAmount(transaction.getOtherAmount());
		totalAmountPaid.setAmount(transaction.getIncomeTaxAmount()
				+ transaction.getSurchangePaidAmount()
				+ transaction.getEducationCessAmount()
				+ transaction.getInterestPaidAmount()
				+ transaction.getPenaltyPaidAmount()
				+ transaction.getOtherAmount());

		modeOFPaymentCombo.setComboItem(transaction.getPaymentMethod());
		checkNumber.setNumber(transaction.getCheckNumber());
		dateItem2.setValue(new ClientFinanceDate(transaction.getDateTaxPaid()));
		bankBsrCode.setValue(transaction.getBankBsrCode());

		if (transaction.getChalanPeriod() == 1) {
			chalanQuarterPeriod.setSelected(getFinancialQuatersList().get(0));
		} else if (transaction.getChalanPeriod() == 2) {
			chalanQuarterPeriod.setSelected(getFinancialQuatersList().get(1));
		} else if (transaction.getChalanPeriod() == 3) {
			chalanQuarterPeriod.setSelected(getFinancialQuatersList().get(2));
		} else if (transaction.getChalanPeriod() == 4) {
			chalanQuarterPeriod.setSelected(getFinancialQuatersList().get(3));
		} else if (transaction.getChalanPeriod() == 5) {
			chalanQuarterPeriod.setSelected(getFinancialQuatersList().get(4));
		}

		fromDateField.setEnteredDate(new ClientFinanceDate(transaction
				.getFromDate()));
		toDateField.setEnteredDate(new ClientFinanceDate(transaction
				.getToDate()));

		payFromAccCombo.setComboItem(getCompany().getAccount(
				transaction.getPayFrom()));

		if (transaction.getPayFrom() != 0) {
			endingBalanceText.setAmount(getCompany().getAccount(
					transaction.getPayFrom())
					.getTotalBalanceInAccountCurrency());
		}
		chalanSerialNumber.setNumber(transaction.getChalanSerialNumber());

		if (transaction.isBookEntry()) {
			tdsDepositedBY.setSelected(getYESNOList().get(0));
		} else {
			tdsDepositedBY.setSelected(getYESNOList().get(1));
		}
		bookEntry = transaction.isBookEntry();

		slectAssecementYear.setSelected(transaction.getAssesmentYearStart()
				+ "-" + transaction.getAssessmentYearEnd());

		natureOfPaymentCombo.setSelected(transaction.getPaymentSection());

		financialYearCombo.setSelected(Integer.toString(transaction
				.getAssesmentYearStart() - 1)
				+ "-"
				+ Integer.toString(transaction.getAssessmentYearEnd() - 1));
		financialYearSelected = true;
		table.setAllRows(transaction.getTdsTransactionItems());

		transactionDateItem.setEnteredDate(transaction.getDate());
		setTransactionDate(transaction.getDate());

	}

	protected void updateTotalTaxCollected() {
		double totalTax = incomeTaxAmount.getAmount()
				+ surchargePaidAmount.getAmount() + eduCessAmount.getAmount()
				+ interestPaidAmount.getAmount()
				+ penaltyPaidAmount.getAmount() + otherAmountPaid.getAmount();
		totalAmountPaid.setAmount(totalTax);

	}

	private List<String> getFormTypes() {
		ArrayList<String> list = new ArrayList<String>();

		if (getFormTypeSeclected() == Form26Q) {
			list.add("26Q");
		} else if (getFormTypeSeclected() == Form27Q) {
			list.add("27Q");
		} else if (getFormTypeSeclected() == Form27EQ) {
			list.add("27EQ");
		}
		return list;
	}

	private List<String> get26QSectionsList() {

		ArrayList<String> list = new ArrayList<String>();

		list.add("193");
		list.add("194");
		list.add("94A");
		list.add("94B");
		list.add("4BB");
		list.add("94C");
		list.add("94D");
		list.add("4EE");
		list.add("94F");
		list.add("94G");
		list.add("94H");
		list.add("94I");
		list.add("94J");
		list.add("94L");

		// list.add("193" + "-10%");
		// list.add("194" + "-10%");
		// list.add("194A" + "-10%");
		// list.add("194B" + "-30%");
		// list.add("194BB" + "-30%");
		// list.add("194C" + "-0%");
		// list.add("194C" + "-1%");
		// list.add("194C" + "-2%");
		// list.add("194D" + "-10%");
		// list.add("194EE" + "-20%");
		// list.add("194F" + "-20%");
		// list.add("194G" + "-10%");
		// list.add("194H" + "-10%");
		// list.add("194I" + "-2%");
		// list.add("194I" + "-10%");
		// list.add("194J" + "-10%");
		// list.add("194LA" + "-10%");

		return list;
	}

	private List<String> get27QSectionsList() {

		ArrayList<String> list = new ArrayList<String>();
		list.add("94E");
		list.add("195");
		list.add("96A");
		list.add("96B");
		list.add("96C");
		list.add("96D");
		// list.add("194E" + "-" + "10%");
		// list.add("195(a)" + "-" + "20%");
		// list.add("195(b)" + "-" + "10%");
		// list.add("195(c)" + "-" + "15%");
		// list.add("195(d)" + "-" + "20%");
		// list.add("195(e)" + "-" + "20%");
		// list.add("195(f)" + "-" + "30%");
		// list.add("195(f)" + "-" + "20%");
		// list.add("195(f)" + "-" + "10%");
		// list.add("195(g)" + "-" + "50%");
		// list.add("195(g)" + "-" + "30%");
		// list.add("195(g)" + "-" + "20%");
		// list.add("195(g)" + "-" + "10%");
		// list.add("195(h)" + "-" + "50%");
		// list.add("195(h)" + "-" + "30%");
		// list.add("195(h)" + "-" + "20%");
		// list.add("195(h)" + "-" + "10%");
		// list.add("195(i)" + "-" + "40%");
		// list.add("195(i)" + "-" + "30%");
		// list.add("196A" + "-" + "20%");
		// list.add("196B" + "-" + "10%");
		// list.add("196C" + "-" + "10%");
		// list.add("196D" + "-" + "20%");

		return list;
	}

	private List<String> get27EQSectionsList() {

		ArrayList<String> list = new ArrayList<String>();
		list.add("A");
		list.add("B");
		list.add("C");
		list.add("D");
		list.add("E");
		list.add("F");
		list.add("G");
		list.add("H");
		list.add("I");

		// list.add("6CA" + "-" + "1%");
		// list.add("6CB" + "-" + "2.5%");
		// list.add("6CC" + "-" + "2.5%");
		// list.add("6CD" + "-" + "2.5%");
		// list.add("6CE" + "-" + "1%");
		// list.add("6CF" + "-" + "2%");
		// list.add("6CG" + "-" + "2%");
		// list.add("6CH" + "-" + "2%");
		// list.add("6CI" + "-" + "5%");

		return list;
	}

	private List<String> getPaymentItems() {
		ArrayList<String> list = new ArrayList<String>();

		list.add(messages.cheque());
		list.add(messages.cash());
		list.add(messages.onlineBanking());
		return list;
	}

	@Override
	public boolean isInViewMode() {
		// TODO Auto-generated method stub
		return super.isInViewMode();
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
		return "Chalan Details";
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
	public ValidationResult validate() {
		ValidationResult result = new ValidationResult();

		result.add(taxDynamicForm.validate());
		result.add(otherDynamicForm.validate());

		List<ClientTDSTransactionItem> allRows = table.getSelectedRecords();
		if (allRows.size() < 1) {
			result.addError(table, "No transaction added to chalan details");
		}
		ClientAccount selectedValue = payFromAccCombo.getSelectedValue();
		if (selectedValue != null
				&& selectedValue.getCurrency() != getCompany()
						.getPrimaryCurrency().getID()) {
			result.addError(payFromAccCombo,
					"Selected pay from account must be in base currency");
		}
		return result;

	}

	@Override
	public void saveAndUpdateView() {
		updateTransaction();
		saveOrUpdate(getData());

	}

	@Override
	protected void updateTransaction() {
		// super.updateTransaction();

		transaction.setDate(getTransactionDate().getDate());
		transaction.setType(ClientTransaction.TYPE_TDS_CHALLAN);

		transaction.setFormType(getFormTypeSeclected());

		if (slectAssecementYear.getSelectedValue() != null) {
			String delims = "-";
			String[] tokens = slectAssecementYear.getSelectedValue().split(
					delims);
			transaction.setAssesmentYearStart(Integer.parseInt(tokens[0]));
			transaction.setAssessmentYearEnd(Integer.parseInt(tokens[1]));
		}

		transaction.setIncomeTaxAmount(incomeTaxAmount.getAmount());
		transaction.setSurchangePaidAmount(surchargePaidAmount.getAmount());
		transaction.setEducationCessAmount(eduCessAmount.getAmount());
		transaction.setInterestPaidAmount(interestPaidAmount.getAmount());
		transaction.setPenaltyPaidAmount(penaltyPaidAmount.getAmount());
		transaction.setOtherAmount(otherAmountPaid.getAmount());

		transaction.setPaymentSection(paymentSectionSelected);
		transaction.setPaymentMethod(modeOFPaymentCombo.getSelectedValue());
		if (chalanSerialNumber.getNumber() != null) {
			transaction.setBankChalanNumber(chalanSerialNumber.getNumber());
			transaction.setChalanSerialNumber(chalanSerialNumber.getNumber());
		}
		transaction.setChalanPeriod(chalanQuarterPeriod.getSelectedIndex() + 1);
		transaction.setFromDate(fromDateField.getEnteredDate().getDate());
		transaction.setToDate(toDateField.getEnteredDate().getDate());

		if (checkNumber.getNumber() != null) {
			transaction.setCheckNumber(checkNumber.getNumber());
		} else {
			transaction.setCheckNumber(0);
		}
		transaction.setBookEntry(bookEntry);

		transaction.setDateTaxPaid(dateItem2.getTime());

		if (bankBsrCode.getValue().length() > 0) {
			transaction.setBankBsrCode(bankBsrCode.getValue());
		} else {
			transaction.setBankBsrCode("");
		}

		if (payFromAccCombo.getSelectedValue() != null) {
			transaction.setPayFrom(payFromAccCombo.getSelectedValue().getID());
		}

		transaction.setTotal(totalAmountPaid.getAmount());

		transaction.setTdsTransactionItems(table.getSelectedRecords(0));

	}

	private void initCallBack() {
		table.removeAllRows();
		Accounter
				.createHomeService()
				.getTDSTransactionItemsList(
						getFormTypeSeclected(),
						new AccounterAsyncCallback<ArrayList<ClientTDSTransactionItem>>() {

							@Override
							public void onException(AccounterException exception) {
								exception.getStackTrace();

							}

							@Override
							public void onResultSuccess(
									ArrayList<ClientTDSTransactionItem> result) {
								if (transaction.getID() == 0) {
									if (result.size() > 0) {
										items = result;
										for (ClientTDSTransactionItem clientTDSTransactionItem : result) {
											clientTDSTransactionItem.setTdsAmount(clientTDSTransactionItem
													.getTdsAmount()
													+ clientTDSTransactionItem
															.getSurchargeAmount()
													+ clientTDSTransactionItem
															.getEduCess());
											table.add(clientTDSTransactionItem);
										}
									} else {
										table.addEmptyMessage(messages
												.noRecordsToShow());
									}

								}
							}
						});
	}

	@Override
	public void onEdit() {
		AccounterAsyncCallback<Boolean> editCallBack = new AccounterAsyncCallback<Boolean>() {

			@Override
			public void onException(AccounterException caught) {
				Accounter.showError(caught.getMessage());
			}

			@Override
			public void onResultSuccess(Boolean result) {
				if (result)
					enableFormItems();
			}

		};

		this.rpcDoSerivce.canEdit(AccounterCoreType.TDSCHALANDETAIL,
				transaction.getID(), editCallBack);
	}

	protected void enableFormItems() {
		setMode(EditMode.EDIT);
		// incomeTaxAmount.setDisabled(false);
		// surchargePaidAmount.setDisabled(false);
		// eduCessAmount.setDisabled(false);
		interestPaidAmount.setEnabled(true);
		penaltyPaidAmount.setEnabled(true);
		otherAmountPaid.setEnabled(true);
		// totalAmountPaid.setDisabled(false);
		natureOfPaymentCombo.setEnabled(true);
		modeOFPaymentCombo.setEnabled(true);
		checkNumber.setEnabled(true);
		dateItem2.setEnabled(true);
		bankBsrCode.setEnabled(true);
		table.setEnabled(true);
		chalanQuarterPeriod.setEnabled(true);
		chalanSerialNumber.setEnabled(true);
		tdsDepositedBY.setEnabled(true);
		// selectFormTypeCombo.setDisabled(false);
		// slectAssecementYear.setDisabled(false);
		financialYearCombo.setEnabled(true);
		// endingBalanceText.setEnabled(false);
		payFromAccCombo.setEnabled(true);
		transactionDateItem.setEnabled(!isInViewMode());

		super.onEdit();
	}

	@Override
	protected void initTransactionViewData() {
		if (transaction == null) {
			setData(new ClientTDSChalanDetail());
		} else {
			updateControls();
		}
		if (transaction.getID() == 0) {
			initCallBack();
		}
	}

	@Override
	public void updateNonEditableItems() {
		double surchargeAmount = 0, eduCessAmount = 0, incomeTaxAmount = 0;
		for (ClientTDSTransactionItem item : table.getSelectedRecords(0)) {
			incomeTaxAmount += item.getTdsAmount();
			surchargeAmount += item.getSurchargeAmount();
			eduCessAmount += item.getEduCess();
		}
		this.incomeTaxAmount.setAmount(incomeTaxAmount);
		this.surchargePaidAmount.setAmount(surchargeAmount);
		this.eduCessAmount.setAmount(eduCessAmount);
		updateTotalTaxCollected();
	}

	@Override
	protected void updateDiscountValues() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void refreshTransactionGrid() {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateAmountsFromGUI() {
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean canRecur() {
		return false;
	}

	@Override
	protected void classSelected(ClientAccounterClass clientAccounterClass) {
		// TODO Auto-generated method stub

	}

	@Override
	public ClientTDSChalanDetail saveView() {
		ClientTDSChalanDetail saveView = super.saveView();
		if (saveView != null) {
			updateTransaction();
		}
		return saveView;
	}

	@Override
	public boolean allowEmptyTransactionItems() {
		return false;
	}

	public int getFormTypeSeclected() {
		return formTypeSeclected;
	}

	public void setFormTypeSeclected(int formTypeSeclected) {
		this.formTypeSeclected = formTypeSeclected;
	}
}
