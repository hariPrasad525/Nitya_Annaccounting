package com.nitya.accounter.web.client.ui.vendors;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.Label;
import com.nitya.accounter.web.client.core.ClientAccount;
import com.nitya.accounter.web.client.core.ClientAccounterClass;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientPayTAXEntries;
import com.nitya.accounter.web.client.core.ClientPayTDS;
import com.nitya.accounter.web.client.core.ClientTAXAgency;
import com.nitya.accounter.web.client.core.ClientTransaction;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.ui.StyledPanel;
import com.nitya.accounter.web.client.ui.UIUtils;
import com.nitya.accounter.web.client.ui.combo.AccountCombo;
import com.nitya.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.nitya.accounter.web.client.ui.combo.PayFromAccountsCombo;
import com.nitya.accounter.web.client.ui.combo.TAXAgencyCombo;
import com.nitya.accounter.web.client.ui.core.AbstractTransactionBaseView;
import com.nitya.accounter.web.client.ui.core.AmountField;
import com.nitya.accounter.web.client.ui.core.DateField;
import com.nitya.accounter.web.client.ui.core.DecimalUtil;
import com.nitya.accounter.web.client.ui.forms.DynamicForm;
import com.nitya.accounter.web.client.ui.forms.TextItem;
import com.nitya.accounter.web.client.ui.grids.TransactionPayTDSGrid;

public class PayTDSView extends AbstractTransactionBaseView<ClientPayTDS> {

	private ArrayList<DynamicForm> listforms;
	private DateField date;
	private PayFromAccountsCombo payFromAccCombo;
	private TAXAgencyCombo taxAgencyCombo;
	private DynamicForm mainform;
	private AmountField amountText;
	private AmountField endingBalanceText;
	private DynamicForm balForm;
	protected ClientAccount selectedPayFromAccount;
	protected double initialEndingBalance;
	protected ClientTAXAgency selectedVATAgency;
	private StyledPanel gridLayout;
	private TransactionPayTDSGrid grid;
	private Double totalAmount = 0.0D;
	private String transactionNumber;
	protected List<ClientPayTAXEntries> entries;
	private ClientTAXAgency selectedTaxAgency;
	private double endingBalance;
	private ArrayList<ClientPayTAXEntries> filterList;
	private ArrayList<ClientPayTAXEntries> tempList;
	private ClientFinanceDate dueDateOnOrBefore;
	private DynamicForm fileterForm;
	private TextItem transNumber;

	public PayTDSView(int transactionType) {
		super(ClientTransaction.TYPE_PAY_TAX);
		this.getElement().setId("paytdsview");
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
	protected void createControls() {
		listforms = new ArrayList<DynamicForm>();

		transactionDateItem = createTransactionDateItem();

		transNumber = createTransactionNumberItem();
		transNumber.setTitle(messages.no());
		transNumber.setToolTip(messages
				.giveNoTo(this.getAction().getViewName()));

		payFromAccCombo = new PayFromAccountsCombo(messages.payFrom());
		// payFromAccCombo.setHelpInformation(true);
		payFromAccCombo.setAccountTypes(UIUtils
				.getOptionsByType(AccountCombo.PAY_FROM_COMBO));
		payFromAccCombo.setRequired(true);
		payFromAccCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {

					public void selectedComboBoxItem(ClientAccount selectItem) {
						selectedPayFromAccount = selectItem;
						// initialEndingBalance = selectedPayFromAccount
						// .getTotalBalance() != 0 ? selectedPayFromAccount
						// .getTotalBalance()
						// : 0D;

						initialEndingBalance = !DecimalUtil.isEquals(
								selectedPayFromAccount.getTotalBalance(), 0) ? selectedPayFromAccount
								.getTotalBalance() : 0D;

						// calculateEndingBalance();
					}

				});

		payFromAccCombo.setEnabled(!isInViewMode());
		payFromAccCombo.setPopupWidth("500px");
		paymentMethodCombo = createPaymentMethodSelectItem();
		paymentMethodCombo.setRequired(true);
		// paymentMethodCombo.setWidth(100);

		// vatAgencyCombo = new VATAgencyCombo("Filter By "
		// + companyConstants.vatAgency());
		// vatAgencyCombo.setDisabled(isEdit);
		// vatAgencyCombo
		// .addSelectionChangeHandler(new
		// IAccounterComboSelectionChangeHandler<ClientVATAgency>() {
		//
		// public void selectedComboBoxItem(ClientVATAgency selectItem) {
		//
		// selectedVATAgency = selectItem;
		// if (selectedVATAgency != null)
		// filterlistbyVATAgency(selectedVATAgency);
		//
		// }
		//
		// });

		DynamicForm dateForm = new DynamicForm("datenumber-panel");
		dateForm.add(transactionDateItem, transNumber);
		StyledPanel datepanel = new StyledPanel("datepanel");
		datepanel.add(dateForm);

		mainform = new DynamicForm("mainform");
		mainform = UIUtils.form(messages.filter());
		mainform.add(payFromAccCombo, paymentMethodCombo);
		amountText = new AmountField(messages.amount(), this,
				getBaseCurrency(), "amountText");
		amountText.setValue("" + UIUtils.getCurrencySymbol() + " 0.00");
		amountText.setEnabled(false);

		endingBalanceText = new AmountField(messages.bankBalance(), this,
				getBaseCurrency(), "endingBalanceText");
		// endingBalanceText.setHelpInformation(true);
		endingBalanceText.setValue("" + UIUtils.getCurrencySymbol() + " 0.00");
		endingBalanceText.setEnabled(false);

		balForm = new DynamicForm("balForm");
		balForm = UIUtils.form(messages.balances());
		balForm.add(amountText, endingBalanceText);
		// balForm.getCellFormatter().setWidth(0, 0, "197px");

		classListCombo = createAccounterClassListCombo();
		if (getPreferences().isClassTrackingEnabled()) {
			balForm.add(classListCombo);
		}

		StyledPanel leftVLay = new StyledPanel("leftVLay");
		leftVLay.add(mainform);
		// leftVLay.add(fileterForm);

		StyledPanel rightVlay = new StyledPanel("rightVlay");
		rightVlay.add(balForm);

		StyledPanel topHLay = new StyledPanel("topHLay");
		topHLay.addStyleName("fields-panel");
		topHLay.add(leftVLay);
		topHLay.add(rightVlay);

		Label lab1 = new Label("" + messages.billsToPay() + "");

		initListGrid();

		StyledPanel mainVLay = new StyledPanel("mainVLay");
		mainVLay.add(voidedPanel);
		mainVLay.add(datepanel);
		mainVLay.add(mainform);
		mainVLay.add(balForm);
		mainVLay.add(lab1);
		mainVLay.add(gridLayout);
		this.add(mainVLay);

		/* Adding dynamic forms in list */
		listforms.add(mainform);
		listforms.add(balForm);

		selectedPayFromAccount = payFromAccCombo.getSelectedValue();
		initialEndingBalance = selectedPayFromAccount == null ? 0D
				: !DecimalUtil.isEquals(
						selectedPayFromAccount.getTotalBalance(), 0) ? selectedPayFromAccount
						.getTotalBalance() : 0D;

		// calculateEndingBalance();

	}

	@Override
	protected void initTransactionViewData() {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNonEditableItems() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void refreshTransactionGrid() {
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

	// initializes the grid.
	private void initListGrid() {

		gridLayout = new StyledPanel("gridLayout");
		grid = new TransactionPayTDSGrid(!isInViewMode(), true);
		grid.setCanEdit(!isInViewMode());
		grid.isEnable = false;
		grid.init();
		// grid.setPayVATView(this);
		grid.setEnabled(!isInViewMode());
		// grid.setHeight("200px");
		if (!isInViewMode()) {
			// grid.addFooterValue("Total", 1);
			// grid
			// .updateFooterValues(DataUtils
			// .getAmountAsString(totalAmount), 2);
		}
		gridLayout.add(grid);

	}

	@Override
	public void setFocus() {
		this.payFromAccCombo.setFocus();

	}

	@Override
	public void updateAmountsFromGUI() {
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

	protected void updateDiscountValues() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void classSelected(ClientAccounterClass clientAccounterClass) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean allowEmptyTransactionItems() {
		return false;
	}
}
