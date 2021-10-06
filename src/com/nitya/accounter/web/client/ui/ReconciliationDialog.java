package com.nitya.accounter.web.client.ui;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.nitya.accounter.web.client.ValueCallBack;
import com.nitya.accounter.web.client.core.ClientAccount;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientReconciliation;
import com.nitya.accounter.web.client.core.ValidationResult;
import com.nitya.accounter.web.client.ui.banking.NewReconcileAccountAction;
import com.nitya.accounter.web.client.ui.combo.ReconciliationAccountCombo;
import com.nitya.accounter.web.client.ui.core.AccounterValidator;
import com.nitya.accounter.web.client.ui.core.AmountField;
import com.nitya.accounter.web.client.ui.core.BaseDialog;
import com.nitya.accounter.web.client.ui.core.DateField;
import com.nitya.accounter.web.client.ui.core.InvalidEntryException;
import com.nitya.accounter.web.client.ui.forms.DynamicForm;

/**
 * 
 * @author Lingarao.R
 * 
 */
public class ReconciliationDialog extends BaseDialog<ClientReconciliation>
		implements WidgetWithErrors {

	private ReconciliationAccountCombo reconcileAccountCombo;
	private DateField startDate;
	private DateField endDate;
	private AmountField closingBalance;
	private final ClientReconciliation reconcilition;
	private ClientAccount account;
	private final ValueCallBack<ClientReconciliation> reconciliationCallback;

	public ReconciliationDialog(String reconciliation,
			ClientReconciliation reconcilition) {
		this(reconciliation, reconcilition, null);
		this.getElement().setId("ReconciliationDialog");
	}

	public ReconciliationDialog(String reconciliation,
			ClientReconciliation reconcilition,
			ValueCallBack<ClientReconciliation> callback) {
		super(reconciliation, "");
		this.reconciliationCallback = callback;
		this.reconcilition = reconcilition;
		this.getElement().setId("ReconciliationDialog");
		// setWidth("400px");
		createControls();
	}

	private void createControls() {

		StyledPanel mainpanel = new StyledPanel("mainpanel");

		reconcileAccountCombo = new ReconciliationAccountCombo(
				messages.bankAccount());

		reconcileAccountCombo.select(reconcilition.getAccount());

		startDate = new DateField(messages.startDate(), "startDate");
		endDate = new DateField(messages.endDate(), "endDate");
		// set the month start date and end date
		// Calendar calendar = Calendar.getInstance();
		// ClientFinanceDate clientFinanceDate = new ClientFinanceDate();
		// clientFinanceDate.setDay(1);
		startDate.setValue(new ClientFinanceDate());
		// clientFinanceDate.setDay(calendar
		// .getActualMaximum(Calendar.DAY_OF_MONTH));
		endDate.setValue(new ClientFinanceDate());

		closingBalance = new AmountField(messages.ClosingBalance(), this,
				getCompany().getCurrency(
						reconcilition.getAccount().getCurrency()),
				"closingBalance") {
			@Override
			protected BlurHandler getBlurHandler() {
				return new BlurHandler() {
					Object value = null;

					@Override
					public void onBlur(BlurEvent event) {
						try {
							clearError(this);
							value = getValue();

							if (value == null)
								return;
							Double amount = DataUtils
									.getAmountStringAsDouble(value.toString());
							if (!AccounterValidator.isAmountTooLarge(amount)) {
								setAmount(DataUtils.isValidAmount(amount + "") ? amount
										: 0.00);
							}
						} catch (Exception e) {
							if (e instanceof InvalidEntryException) {
								addError(this, e.getMessage());
							}

							setAmount(0.00);
						}
					}
				};
			}
		};
		if (reconcilition.getStartDate() != null)
			startDate.setValue(reconcilition.getStartDate().getDateAsObject());
		if (reconcilition.getEndDate() != null)
			endDate.setValue(reconcilition.getEndDate().getDateAsObject());
		closingBalance.setAmount(reconcilition.getClosingBalance());

		DynamicForm form = new DynamicForm("form");
		// form.setWidth("100%");
		form.add(reconcileAccountCombo, closingBalance, startDate, endDate);

		// DynamicForm datesForm = new DynamicForm();
		// form.setWidth("100%");
		// datesForm.setFields(startDate, endDate);

		// StyledPanel hPanel = new StyledPanel();
		// hPanel.setWidth("100%");
		// hPanel.add(form);
		// hPanel.add(datesForm);

		mainpanel.add(form);
		setBodyLayout(mainpanel);
		// okbtn.setWidth("130px");
		okbtn.setText(messages.startReconciliation());

	}

	/**
	 * Create the Reconciliation Object.
	 * 
	 * @return {@link ClientReconciliation}
	 */
	private ClientReconciliation createReconciliation() {
		ClientReconciliation reconciliation = new ClientReconciliation();
		reconciliation.setClosingBalance(closingBalance.getAmount());
		reconciliation.setStartDate(startDate.getDate());
		reconciliation.setEndDate(endDate.getDate());
		reconciliation.setReconcilationDate(new ClientFinanceDate());
		reconciliation.setAccount(reconcileAccountCombo.getSelectedValue());
		return reconciliation;
	}

	@Override
	protected ValidationResult validate() {
		ValidationResult result = new ValidationResult();
		ClientFinanceDate sDate = startDate.getDate();
		ClientFinanceDate eDate = endDate.getDate();
		if (eDate.before(sDate)) {
			result.addError(endDate, messages.validateEndAndStartDate());
		}
		if (reconcileAccountCombo.getSelectedValue() == null) {
			result.addError(reconcileAccountCombo,
					messages.pleaseSelect(messages.account()));
		}
		return result;
	}

	@Override
	protected boolean onOK() {
		if (reconciliationCallback != null) {
			reconciliationCallback.execute(createReconciliation());
		} else {
			new NewReconcileAccountAction().run(createReconciliation(), false);
		}
		return true;
	}

	@Override
	public void setFocus() {
		reconcileAccountCombo.setFocus();

	}

	@Override
	protected boolean onCancel() {
		return true;
	}
}
