/**
 * 
 */
package com.nitya.accounter.web.client.ui.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import com.nitya.accounter.web.client.AccounterAsyncCallback;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.ValueCallBack;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.AddNewButton;
import com.nitya.accounter.web.client.core.ClientAccount;
import com.nitya.accounter.web.client.core.ClientAccounterClass;
import com.nitya.accounter.web.client.core.ClientAddress;
import com.nitya.accounter.web.client.core.ClientAttachment;
import com.nitya.accounter.web.client.core.ClientCashPurchase;
import com.nitya.accounter.web.client.core.ClientCompanyPreferences;
import com.nitya.accounter.web.client.core.ClientContact;
import com.nitya.accounter.web.client.core.ClientCurrency;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientJob;
import com.nitya.accounter.web.client.core.ClientLocation;
import com.nitya.accounter.web.client.core.ClientPayBill;
import com.nitya.accounter.web.client.core.ClientPaymentTerms;
import com.nitya.accounter.web.client.core.ClientRecurringTransaction;
import com.nitya.accounter.web.client.core.ClientTAXCode;
import com.nitya.accounter.web.client.core.ClientTAXReturn;
import com.nitya.accounter.web.client.core.ClientTransaction;
import com.nitya.accounter.web.client.core.ClientTransactionDepositItem;
import com.nitya.accounter.web.client.core.ClientTransactionItem;
import com.nitya.accounter.web.client.core.ClientTransactionLog;
import com.nitya.accounter.web.client.core.ClientUserPermissions;
import com.nitya.accounter.web.client.core.ClientVendor;
import com.nitya.accounter.web.client.core.ClientWriteCheck;
import com.nitya.accounter.web.client.core.Features;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.core.Utility;
import com.nitya.accounter.web.client.core.ValidationResult;
import com.nitya.accounter.web.client.core.ValidationResult.Error;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.exception.AccounterExceptions;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.CustomMenuBar;
import com.nitya.accounter.web.client.ui.CustomMenuItem;
import com.nitya.accounter.web.client.ui.ImageButton;
import com.nitya.accounter.web.client.ui.StyledPanel;
import com.nitya.accounter.web.client.ui.TransactionHistoryTable;
import com.nitya.accounter.web.client.ui.UIUtils;
import com.nitya.accounter.web.client.ui.combo.AddressCombo;
import com.nitya.accounter.web.client.ui.combo.ClassListCombo;
import com.nitya.accounter.web.client.ui.combo.ContactCombo;
import com.nitya.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.nitya.accounter.web.client.ui.combo.JobCombo;
import com.nitya.accounter.web.client.ui.combo.LocationCombo;
import com.nitya.accounter.web.client.ui.combo.PayFromAccountsCombo;
import com.nitya.accounter.web.client.ui.combo.PaymentTermsCombo;
import com.nitya.accounter.web.client.ui.combo.SelectCombo;
import com.nitya.accounter.web.client.ui.combo.VendorCombo;
import com.nitya.accounter.web.client.ui.customers.QuoteView;
import com.nitya.accounter.web.client.ui.customers.RecurringTransactionDialog;
import com.nitya.accounter.web.client.ui.forms.AmountLabel;
import com.nitya.accounter.web.client.ui.forms.CheckboxItem;
import com.nitya.accounter.web.client.ui.forms.TextAreaItem;
import com.nitya.accounter.web.client.ui.forms.TextItem;
import com.nitya.accounter.web.client.ui.reports.ReportsRPC;
import com.nitya.accounter.web.client.ui.settings.RolePermissions;
import com.nitya.accounter.web.client.ui.widgets.CurrencyChangeListener;
import com.nitya.accounter.web.client.ui.widgets.CurrencyComboWidget;
import com.nitya.accounter.web.client.ui.widgets.CurrencyFactorWidget;
import com.nitya.accounter.web.client.ui.widgets.DateValueChangeHandler;

/**
 * @author Fernandez This Class serves as the Base Class for All Transactions
 * 
 */
public abstract class AbstractTransactionBaseView<T extends ClientTransaction>
		extends BaseView<T> implements ICurrencyProvider, IButtonContainer {

	protected int transactionType;

	@Override
	public ClientCurrency getTransactionCurrency() {
		return this.currency != null ? this.currency : getCompany()
				.getPrimaryCurrency();
	}

	public ClientAccounterClass accounterClass;
	protected T transaction;

	private StyledPanel addNotesPanel;

	private TransactionHistoryTable historyTable;

	private HTML lastActivityHTML, noteHTML;

	protected PercentageField discountField;

	// public static final int CUSTOMER_TRANSACTION_GRID = 1;
	// public static final int VENDOR_TRANSACTION_GRID = 2;
	// public static final int BANKING_TRANSACTION_GRID = 3;
	// public static final int MAKEDEPOSIT_TRANSACTION_GRID = 4;
	// public static final int PAYBILL_TRANSACTION_GRID = 5;
	// public static final int RECIEVEPAYMENT_TRANSACTION_GRID = 6;
	// public static final int TAXAGENCY_TRANSACTION_GRID = 7;
	// public static final int PAYSALESTAX_TRANSACTION_GRID = 8;
	// public static final int JOURNALENTRY_TRANSACTION_GRID = 9;
	// public static final int PAYVAT_TRANSACTION_GRID = 10;

	protected String checkNumber = ClientWriteCheck.IS_TO_BE_PRINTED;

	protected ClientFinanceDate transactionDate;
	protected TextItem transactionNumber;
	protected TextItem checkNo;
	protected DateField transactionDateItem;
	protected TextAreaItem memoTextAreaItem;
	protected TextItem phoneSelect;
	// protected TextItem refText;
	// protected AddNewButton menuButton;
	private PopupPanel popupPanel;
	private CustomMenuBar popupMenuBar;

	protected Set<ClientAddress> addressListOfVendor;
	protected Set<ClientContact> contacts;
	protected ClientContact contact;

	protected ClientAccount payFromAccount;
	protected ClientAddress billingAddress;
	protected AddressCombo billToCombo;
	protected ContactCombo contactCombo;
	protected VendorCombo vendorCombo;
	protected PayFromAccountsCombo payFromCombo;

	protected AmountLabel foreignCurrencyamountLabel,
			transactionTotalBaseCurrencyText, roundAmountinBaseCurrenctText,
			roundAmountinforeignCurrencyLabel;

	private boolean isMenuRequired = true;

	protected List<ClientTransactionItem> transactionItems = new ArrayList<ClientTransactionItem>();

	protected List<ClientTransactionDepositItem> transactionDepositItems = new ArrayList<ClientTransactionDepositItem>();

	private List<String> payVatMethodList;

	protected String paymentMethod;
	protected String phoneNo;

	protected SelectCombo paymentMethodCombo;

	protected PaymentTermsCombo payTermsSelect;

	protected ClientPaymentTerms paymentTerm;

	// /**
	// * // * The Transaction Grid meant to Serve in all Transactions //
	// */
	// protected AbstractTransactionGrid<ClientTransactionItem>
	// vendorTransactionGrid,
	// customerTransactionGrid;
	// /**
	// * // * The Transaction Grid meant to Serve in all Transactions //
	// */
	// protected AbstractTransactionGrid<ClientTransactionItem>
	// vendorTransactionGrid,

	protected boolean showPriceWithVat;

	protected boolean EUVatExempt = false;

	protected boolean isVATInclusive;

	public CheckboxItem vatinclusiveCheck;

	protected ClientVendor vendor;

	// protected CurrencyWidget currencyWidget;
	private ClientLocation location;

	protected LocationCombo locationCombo;
	protected int gridType;

	protected Button recurringButton;

	// Class Tracking
	protected ClassListCombo classListCombo;

	protected ClientAccounterClass clientAccounterClass;

	protected ClientCurrency currency;

	protected double currencyFactor = 1.0;

	protected CurrencyFactorWidget currencyWidget;

	private ArrayList<ClientAccounterClass> clientAccounterClasses = new ArrayList<ClientAccounterClass>();

	public RecurringTransactionDialog recurringDialog;
	// For location Tracking
	protected final boolean locationTrackingEnabled;

	protected boolean isTemplate;

	protected StyledPanel voidedPanel;

	private Label voidedLabel;

	private DraftsButton draftsButton;

	protected JobCombo jobListCombo;

	private ClientJob job;

	public boolean isVatInclusive() {
		return isVATInclusive;
	}

	/**
	 * @param transactionType
	 */
	public AbstractTransactionBaseView(int transactionType) {
		super();
		this.transactionType = transactionType;
		getVoidedPanel();

		// Getting the location tracking is enable or not.
		locationTrackingEnabled = Global.get().preferences()
				.isLocationTrackingEnabled();
	}

	protected abstract void createControls();

	// protected abstract void initTransactionViewData(
	// ClientTransaction transactionObject);

	protected abstract void initTransactionViewData();

	public abstract void updateNonEditableItems();

	protected final String getTransactionStatus() {
		return Utility.getStatus(transactionType,
				transaction != null ? transaction.getStatus() : 0);
	}


	protected ClientTAXCode getTaxCodeForTransactionItems(
			List<ClientTransactionItem> transactionItems) {
		ClientTAXCode taxCode = null;

		for (ClientTransactionItem clientTransactionItem : transactionItems) {
			if (clientTransactionItem.getTaxCode() != 0) {

				taxCode = getCompany().getTAXCode(
						clientTransactionItem.getTaxCode());

				if (taxCode != null)
					break;
				else
					continue;
			}

		}
		return taxCode;
	}

	/**
	 * 
	 * @param transactionItems
	 * @return
	 */
	protected ClientAccounterClass getClassForTransactionItem(
			List<ClientTransactionItem> transactionItems) {
		ClientAccounterClass accounterClass = null;
		for (ClientTransactionItem clientTransactionItem : transactionItems) {
			if (clientTransactionItem.getAccounterClass() != 0
					&& clientTransactionItem.getReferringTransactionItem() == 0) {
				accounterClass = getCompany().getAccounterClass(
						clientTransactionItem.getAccounterClass());
				if (accounterClass != null) {
					break;
				} else {
					continue;
				}
			}
		}
		return accounterClass;
	}

	protected StyledPanel getVoidedPanel() {
		voidedPanel = new StyledPanel("voidedPanel");
		voidedLabel = new Label();
		voidedPanel.add(voidedLabel);
		return voidedPanel;
	}

	public double getVATRate(long VATCodeID) {
		return 0.0;
	}

	// public abstract AbstractTransactionGrid<ClientTransactionItem> getGrid();
	// public AbstractTransactionGrid<ClientTransactionItem> getGrid() {
	// // if (getCompany().getAccountingType() ==
	// // ClientCompany.ACCOUNTING_TYPE_US) {
	// switch (gridType) {
	// case JOURNALENTRY_TRANSACTION_GRID:
	// break;
	// case PAYSALESTAX_TRANSACTION_GRID:
	// break;
	// case TAXAGENCY_TRANSACTION_GRID:
	// break;
	// case RECIEVEPAYMENT_TRANSACTION_GRID:
	// break;
	// case PAYBILL_TRANSACTION_GRID:
	// break;
	// case MAKEDEPOSIT_TRANSACTION_GRID:
	// case VENDOR_TRANSACTION_GRID:
	// return new VendorTransactionGrid();
	// case CUSTOMER_TRANSACTION_GRID:
	// return new CustomerTransactionGrid();
	// }
	// // } else {
	// // switch (gridType) {
	// // case JOURNALENTRY_TRANSACTION_GRID:
	// // break;
	// // case PAYSALESTAX_TRANSACTION_GRID:
	// // break;
	// // case TAXAGENCY_TRANSACTION_GRID:
	// // break;
	// // case RECIEVEPAYMENT_TRANSACTION_GRID:
	// // break;
	// // case PAYBILL_TRANSACTION_GRID:
	// // break;
	// // case MAKEDEPOSIT_TRANSACTION_GRID:
	// // break;
	// // case VENDOR_TRANSACTION_GRID:
	// // return new VendorTransactionUKGrid();
	// // case CUSTOMER_TRANSACTION_GRID:
	// // return new CustomerTransactionUKGrid();
	// // }
	// // }
	// return null;
	// }

	public void setAmountIncludeChkValue(boolean isAmountIncludedVAT) {
		if (vatinclusiveCheck != null) {
			vatinclusiveCheck.setValue(isAmountIncludedVAT);
			isVATInclusive = isAmountIncludedVAT;
		}
	}

	public CheckboxItem getVATInclusiveCheckBox() {
		vatinclusiveCheck = new CheckboxItem(messages.amountIncludesVat(),
				"vatinclusiveCheck");
		vatinclusiveCheck.addChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				isVATInclusive = event.getValue();
				List<ClientTransactionItem> allTransactionItems = getAllTransactionItems();
				if (allTransactionItems != null) {
					for (ClientTransactionItem item : allTransactionItems) {
						if (!item.isEmpty()) {
							item.setAmountIncludeTAX(isVATInclusive);
						}
					}
				}
				updateAmountsFromGUI();
				refreshTransactionGrid();
			}
		});
		vatinclusiveCheck.setEnabled(!isInViewMode());
		return vatinclusiveCheck;
	}

	protected boolean isAmountIncludeTAX() {
		if (data == null || data.getTransactionItems() == null) {
			return false;
		}
		for (ClientTransactionItem item : data.getTransactionItems()) {
			return item.isAmountIncludeTAX();
		}
		return false;
	}

	protected abstract void refreshTransactionGrid();

	protected PercentageField getDiscountField() {
		discountField = new PercentageField(this, messages.discount());
		discountField.setEnabled(!isInViewMode());
		discountField.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				updateDiscountValues();
			}
		});
		return discountField;

	}

	protected abstract void updateDiscountValues();

	protected void initTransactionNumber() {

		if (transactionNumber == null)
			return;

		if (transaction != null && transaction.getID() != 0) {

			transactionNumber.setValue(transaction.getNumber());
			return;
		}

		AccounterAsyncCallback<String> transactionNumberCallback = new AccounterAsyncCallback<String>() {

			@Override
			public void onException(AccounterException caught) {
				Accounter.showError(messages.failedToGetTransactionNumber());

			}

			@Override
			public void onResultSuccess(String result) {
				if (result == null) {
					onException(null);
				}

				// transactionNumber.setValue(String.valueOf(result));
				transactionNumber.setValue(result);

			}

		};

		this.rpcUtilService.getNextTransactionNumber(transactionType,
				transactionNumberCallback);

	}

	protected void resetForms() {
		// // FIX ME
		//
		// // for (DynamicForm form : forms) {
		// // form.reset();
		// // }
		// //
		// // for (FormItem item : formItems) {
		// // item.setDisabled(isEdit);
		// // }

	}

	protected DateField createTransactionDateItem() {

		final DateField dateItem = new DateField(messages.date(), "dateItem");
		dateItem.setToolTip(messages.selectDateWhenTransactioCreated(this
				.getAction().getViewName()));
		dateItem.setShowTitle(false);

		dateItem.addDateValueChangeHandler(new DateValueChangeHandler() {

			@Override
			public void onDateValueChange(ClientFinanceDate date) {
				if (date != null) {
					try {
						ClientFinanceDate newDate = dateItem.getValue();
						if (newDate != null)
							setTransactionDate(newDate);
					} catch (Exception e) {
						Accounter.showError(messages.invalidTransactionDate());
						setTransactionDate(new ClientFinanceDate());
						dateItem.setEnteredDate(getTransactionDate());
					}

				}
			}
		});

		if (transaction != null && transaction.getDate() != null) {
			dateItem.setEnteredDate(transaction.getDate());
			setTransactionDate(transaction.getDate());

		} else {
			setTransactionDate(new ClientFinanceDate());
			dateItem.setEnteredDate(new ClientFinanceDate());

		}

		dateItem.setEnabled(!isInViewMode());

		// formItems.add(dateItem);

		return dateItem;

	}

	public void setTransactionDate(long date) {

	}

	protected TextItem createTransactionNumberItem() {

		final TextItem item = new TextItem(messages.no(), "item");
		item.setToolTip(messages.giveNoTo(this.getAction().getViewName()));

		item.setEnabled(!isInViewMode());

		// formItems.add(item);

		// if (UIUtils.isMSIEBrowser())
		// item.setWidth("150px");

		return item;

	}

	protected TextItem createRefereceText() {

		TextItem refText = new TextItem(messages.reference(), "refText");
		return refText;

	}

	protected AmountField createNetAmountField() {
		AmountField netAmountField = new AmountField(messages.netAmount(),
				this, getBaseCurrency(), "netAmountField");
		netAmountField.setDefaultValue("£0.00");
		netAmountField.setEnabled(false);
		return netAmountField;
	}

	protected AmountLabel createNetAmountLabel() {
		AmountLabel netAmountLabel = new AmountLabel(
				messages.currencyNetAmount(getBaseCurrency().getFormalName()),
				getBaseCurrency());
		netAmountLabel.setTitle(messages.currencyNetAmount(getBaseCurrency()
				.getFormalName()));
		netAmountLabel.setCurrency(getBaseCurrency());
		netAmountLabel.setDefaultValue("£0.00");
		return netAmountLabel;
	}

	protected AmountLabel createForeignCurrencyAmountLable(
			ClientCurrency currency) {

		foreignCurrencyamountLabel = new AmountLabel(
				messages.currencyTotal(currency.getFormalName()), currency);

		return foreignCurrencyamountLabel;
	}

	protected AmountLabel createRoundingAmountForeignCurrencyAmountLable(
			ClientCurrency currency) {

		roundAmountinforeignCurrencyLabel = new AmountLabel(
				messages2.currencyRoundingTotal(currency.getFormalName()),
				currency);

		return roundAmountinforeignCurrencyLabel;
	}

	protected void changeForeignCurrencyTotalText(String string) {

		foreignCurrencyamountLabel.setTitle(messages.currencyTotal(string));
	}

	protected AmountLabel createTransactionTotalNonEditableLabelforPurchase() {

		AmountLabel amountLabel = new AmountLabel(messages.total());

		return amountLabel;

	}

	protected AmountLabel createVATTotalNonEditableLabelforPurchase() {
		AmountLabel amountLabel = new AmountLabel(messages.vat());

		return amountLabel;
	}

	protected TextAreaItem createMemoTextAreaItem() {

		TextAreaItem memoArea = new TextAreaItem("", "memoArea");
		memoArea.setTitle(messages.writeCommentsForThis(getAction()
				.getViewName()));
		return memoArea;

	}

	protected final void transactionSuccess(Object result) {

		try {
			if (result == null)
				throw new Exception();
			if (!saveAndClose) {
				if (!History.getToken().equals(getAction().getHistoryToken())) {

				}
				getManager().closeCurrentView(false);
				getAction().run(null, getAction().isDependent());

			} else {

				getManager().closeCurrentView();
			}

		} catch (Exception e) {
			// SC.logWarn(String.valueOf(result));
			e.printStackTrace();
		}

	}

	protected void reload() {
		initTransactionViewData();
	}

	public final void transactionFailed(Throwable caught) {
		String transName = Utility.getTransactionName(transactionType);
		AccounterException exception = (AccounterException) caught;
		String message = null;
		if (caught.getMessage() != null) {
			message = caught.getMessage();
		}
		String errMsg = null;
		if (exception.getErrorCode() != 0) {
			errMsg = AccounterExceptions.getErrorString(exception);
		} else {
			errMsg = messages.failedTransaction(transName);
		}

		if (message == null) {
			message = "";
		}
		if (errMsg != null) {
			message = errMsg + "<br/>" + message;
		}

		Accounter.showError(message);
		// SC
		// .logWarn("Failed Transaction" + transName + " " + caught != null ?
		// caught
		// .getMessage()
		// : "Cause UnKnown");
		if (caught != null)
			caught.printStackTrace();

	}

	@Override
	public void saveAndUpdateView() {

	}

	// public AddNewButton createAddNewButton() {
	// // TODO make this button to Image button
	// menuButton = new AddNewButton(this);
	// return menuButton;
	// }

	@Override
	protected void createButtons() {
		if (isTemplate) {
			if (saveAndCloseButton != null) {
				saveAndCloseButton.setText(messages.saveTemplate());
				addButton(saveAndCloseButton);
			}
			return;
		}
		super.createButtons();
		// FIXME > Need to complete Recurring transaction feature.
		if (canRecur()
				&& Accounter.hasPermission(Features.RECURRING_TRANSACTIONS)) {
			recurringButton = new RecurringButton(this, "clock");
			if (!isTemplate) {
				if (getCompany().getLoggedInUser().getPermissions()
						.getTypeOfInvoicesBills() == RolePermissions.TYPE_YES)
					addButton(recurringButton);
			}
		}
		if (Accounter.hasPermission(Features.DRAFTS)) {
			draftsButton = new DraftsButton(messages.Saveasdraft(), this,
					"save");
			draftsButton.setVisible(!isInViewMode() && canAddDraftButton());
			addButton(draftsButton);
		}

	}

	protected boolean canAddDraftButton() {
		ClientUserPermissions permissions = getCompany().getLoggedInUser()
				.getPermissions();
		return Utility.isUserHavePermissions(transactionType) && canRecur() ? (transaction == null ? true
				: transaction.getID() == 0)
				: (!canRecur() && transaction != null && transaction.isDraft());
	}

	// for new recurring
	public void openRecurringDialog() {
		openRecurringDialog(null);
	}

	// for editing existing recurring
	public void openRecurringDialog(ClientRecurringTransaction result) {

		if (recurringDialog == null) {
			recurringDialog = new RecurringTransactionDialog(this, result);

			recurringDialog
					.setCallback(new ActionCallback<ClientRecurringTransaction>() {

						@Override
						public void actionResult(
								ClientRecurringTransaction result) {
							System.out.println("Recurring result" + result);
						}
					});
		}
		ViewManager.getInstance().showDialog(recurringDialog);
	}

	@Override
	public void onAddNew() {
		// TODO Auto-generated method stub
		super.onAddNew();
	}

	public void setTransactionDate(ClientFinanceDate transactionDate) {
		this.transactionDate = transactionDate;
	}

	public ClientFinanceDate getTransactionDate() {
		return transactionDate;
	}

	public ClientTransaction getTransactionObject() {
		return transaction;
	}

	public void setTransactionObject(T transactionObject) {
		this.transaction = transactionObject;
	}

	// public String getRefText() {
	// return refText != null && refText.getValue().toString() != null ? refText
	// .getValue().toString()
	// : "";
	// }
	//
	// public void setRefText(String reference) {
	// this.refText.setValue(reference != null ? reference : "");
	// }

	public String getMemoTextAreaItem() {
		return memoTextAreaItem != null
				&& memoTextAreaItem.getValue().toString() != null ? memoTextAreaItem
				.getValue().toString() : "";
	}

	public void setMemoTextAreaItem(String memo) {
		this.memoTextAreaItem.setValue(memo != null ? memo : "");
	}

	public SelectCombo createPaymentMethodSelectItem() {
		payVatMethodList = new ArrayList<String>();
		// paymentType = UIUtils.getpaymentMethodCheckBy_CompanyType(Accounter
		// .constants().check());
		String payVatMethodArray[] = new String[] { messages.cash(),
				messages.cheque(), messages.creditCard(),
				messages.directDebit(), messages.masterCard(),
				messages.onlineBanking(), messages.standingOrder(),
				messages.switchMaestro(), messages.paypal() };

		for (int i = 0; i < payVatMethodArray.length; i++) {
			payVatMethodList.add(payVatMethodArray[i]);
		}

		final SelectCombo paymentMethodSelect = new SelectCombo(
				messages.paymentMethod());

		paymentMethodSelect.setRequired(true);
		paymentMethodSelect.initCombo(payVatMethodList);
		paymentMethodSelect.setDefaultToFirstOption(true);
		paymentMethod = messages.cash();

		paymentMethodSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						paymentMethodSelected(paymentMethodSelect
								.getSelectedValue());

					}
				});
		paymentMethodSelect.setEnabled(!isInViewMode());
		// formItems.add(paymentMethodSelect);

		return paymentMethodSelect;

	}

	protected void paymentMethodSelected(String paymentmethod) {
		this.paymentMethod = paymentmethod;
	}

	@Override
	public void init() {
		if (data != null) {
			this.isTemplate = data.getSaveStatus() == ClientTransaction.STATUS_TEMPLATE;
		}
		super.init();
		if (isTemplate) {
			createRecurringPanel();
		}
		getVoidedPanel();
		createControls();
	}

	private void createRecurringPanel() {
		StyledPanel panel = new StyledPanel("recurring_message_panel");

		HTML text = new HTML();
		text.setHTML("<a>" + messages.ThisisatemplateusedinRecurring() + "</a>");
		Anchor click = new Anchor();
		click.setHTML(messages.Clickhere());
		click.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				getAndOpenRecurringDialog();
			}
		});
		HTML recur = new HTML();
		recur.setHTML("<a>" + messages.tochangetherecurringschedule());

		panel.add(text);
		panel.add(click);
		panel.add(recur);
		super.insert(panel, 0);
	}

	protected void getAndOpenRecurringDialog() {
		Accounter.createGETService().getObjectById(
				AccounterCoreType.RECURRING_TRANSACTION,
				transaction.getRecurringTransaction(),
				new AsyncCallback<ClientRecurringTransaction>() {

					@Override
					public void onFailure(Throwable caught) {
						Accounter.showError(messages
								.Unabletoopenrecurringtransactio() + caught);
					}

					@Override
					public void onSuccess(ClientRecurringTransaction result) {
						openRecurringDialog(result);
					}
				});
	}

	@Override
	public void initData() {

		initTransactionViewData();
		if (transaction.isVoid()) {
			voidedLabel.setText(messages.voided());
			voidedPanel.addStyleName("title_voided_panel");
			voidedLabel.addStyleName("title_voided_label");
		}
		if (canAddAttachmentPanel()) {

			addAttachments(new ArrayList<ClientAttachment>(
					transaction.getAttachments()));
		}
		super.initData();
		initTransactionNumber();
		refreshTransactionGrid();

	}

	protected boolean checkOpen(Collection<ClientTransactionItem> items,
			int type, boolean defaultValue) {
		if (items.isEmpty()) {
			return defaultValue;
		}
		for (ClientTransactionItem item : items) {
			if (item.getType() == type) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void setData(T data) {
		super.setData(data);
		transaction = data;
	}

	@Override
	public void disableUserEntry() {

		// if (this.formItems != null) {
		//
		// for (FormItem item : formItems) {
		// // item.disable();
		// }
		// }
	}

	// public void setShowPriceWithVat(boolean showPriceWithVat) {
	// this.showPriceWithVat = showPriceWithVat;
	// }

	public boolean isShowPriceWithVat() {
		return isVATInclusive;
	}

	public void setMenuItems(Widget button, Map<String, ImageResource> items) {
		createPopupMenu(button);
		popupMenuBar.clearItems();
		for (final String itm : items.keySet()) {
			ImageResource imgSrc = items.get(itm);
			Command cmd = new Command() {

				@Override
				public void execute() {
					menuItemClicked(itm);
					popupPanel.removeFromParent();
				}
			};
			CustomMenuItem item = new CustomMenuItem(itm, cmd);
			item.addStyleName(itm);
			item.setIcon(imgSrc);
			popupMenuBar.addItem(item);
		}
	}

	public void setMenuItems(Widget button, String... items) {
		createPopupMenu(button);
		popupMenuBar.clearItems();

		ClientCompanyPreferences preferences = getCompany().getPreferences();
		boolean sellProducts = preferences.isSellProducts();
		boolean sellServices = preferences.isSellServices();

		for (final String itm : items) {
			Command cmd = new Command() {

				@Override
				public void execute() {
					menuItemClicked(itm);
					popupPanel.removeFromParent();
				}
			};

			CustomMenuItem item = new CustomMenuItem(itm, cmd);
			item.addStyleName(itm);
			ImageResource image = null;
			if (itm.equalsIgnoreCase(messages.Accounts())) {
				image = Accounter.getFinanceMenuImages().Accounts();
			} else if (itm.equals(messages.productOrServiceItem())) {
				if (sellProducts) {
					image = Accounter.getFinanceMenuImages().items();
				} else {
					continue;
				}
			} else if (itm.equals(messages.comment())) {
				image = Accounter.getFinanceMenuImages().comments();
			} else if (itm.equals(messages.salesTax())
					|| (itm.equals(messages.serviceItem()))
					|| (itm.equals(messages.service()))) {
				if (sellServices) {
					image = Accounter.getFinanceMenuImages().salestax();
				} else {
					continue;
				}
			}

			item.setIcon(image);

			// item.getElement().getStyle().setProperty("background",
			// "url(" + image + ") no-repeat scroll 0 0 transparent");

			popupMenuBar.addItem(item);
		}
	}

	private void createPopupMenu(Widget button) {
		if (popupPanel == null) {
			popupPanel = new PopupPanel(true);
			popupMenuBar = new CustomMenuBar(Accounter.getFinanceMenuImages());
			popupMenuBar.getElement().setAttribute("id", "addnewpopumenu");

			popupPanel.setStyleName("popup");
			popupPanel.getElement().setAttribute("id", "addnewpopuppanel");
			popupMenuBar.setVisible(true);
			popupPanel.add(popupMenuBar);
			// popupPanel.setPopupPosition(button.getAbsoluteLeft(),
			// button.getAbsoluteTop() + button.getOffsetHeight());
		}
	}

	protected void menuItemClicked(String item) {
		onAddNew(item);
	}

	/**
	 * Override this method to do anything you want when add new button is
	 * clicked. Default implementation will show a
	 */
	protected void onAddNew(String item) {

	}

	protected void addAccount() {

	}

	protected void addItem() {

	}

	@Override
	public void showMenu(Event event) {
		// int menuTop = menuButton.getAbsoluteTop()
		// - (popupMenuBar.numberOfItems * 43);
		//
		// int left = menuButton.getAbsoluteLeft() - 5;

		// popupPanel.setPopupPosition(left, menuTop + 1);
		// popupPanel.show();
	}

	@Override
	public boolean isMenuRequired() {
		return isMenuRequired;
	}

	public void setMenuRequired(boolean isMenuRequired) {
		this.isMenuRequired = isMenuRequired;
	}

	@Override
	public void saveSuccess(IAccounterCore result) {
		transactionSuccess(result);
	}

	@Override
	public void saveFailed(AccounterException exception) {
		super.saveFailed(exception);
		transactionFailed(exception);
	}

	@Override
	public void fitToSize(int height, int width) {
		super.fitToSize(height, width);
	}

	@Override
	public void onEdit() {
		if (vatinclusiveCheck != null)
			vatinclusiveCheck.setEnabled(!isInViewMode());
		// if (menuButton != null)
		// menuButton.setEnabled(!isInViewMode());
		setMode(EditMode.EDIT);

		// if (getPreferences().isClassTrackingEnabled()
		// /* && getPreferences().isClassOnePerTransaction() */
		// && classListCombo != null) {
		// classListCombo.setDisabled(isInViewMode());
		// }
	}

	public boolean isEdit() {
		return isInViewMode();
	}

	@Override
	public ValidationResult validate() {

		updateTransaction();

		ValidationResult result = new ValidationResult();

		if (this.transactionDateItem != null
				&& this.transactionDateItem.getDate().getDate() == 0) {
			result.addError(transactionDateItem,
					messages.pleaseEnter(messages.transactionDate()));
			return result;
		}
		isValidCurrencyFactor(result);

		if (needTransactionItems() && !allowEmptyTransactionItems()) {
			if (transactionItems == null && transactionItems.size() == 0) {
				result.addError("TransactionItem",
						messages.thereAreNoTransactionItemsToSave());
			}
		}
		return result;

	}

	/**
	 * For allowing transaction line total is zero
	 * 
	 * @return {@link Boolean}
	 */
	protected boolean allowEmptyTransactionItems() {
		return false;
	}

	/**
	 * For Location Combo
	 * 
	 * @return
	 */
	protected LocationCombo createLocationCombo() {
		LocationCombo locationCombo = new LocationCombo(Global.get().Location());
		locationCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientLocation>() {

					@Override
					public void selectedComboBoxItem(ClientLocation selectItem) {
						locationSelected(selectItem);
					}
				});

		locationCombo
				.addNewLocationHandler(new ValueCallBack<ClientLocation>() {
					@Override
					public void execute(ClientLocation value) {
						addLoactionToTransaction(value);

					}

				});
		locationCombo.setEnabled(!isInViewMode());
		return locationCombo;

	}

	/**
	 * add location to transaction.
	 * 
	 * @param location
	 */
	private void addLoactionToTransaction(final ClientLocation location) {

		// transaction.setLocation(location.getID());
		AccounterAsyncCallback<Long> asyncallBack = new AccounterAsyncCallback<Long>() {

			@Override
			public void onException(AccounterException caught) {
				caught.printStackTrace();
			}

			@Override
			public void onResultSuccess(Long result) {
				Utility.updateClientList(location, getCompany().getLocations());
				location.setID(result);
				locationSelected(location);
			}

		};
		Accounter.createCRUDService().create(location, asyncallBack);
	}

	/**
	 * 
	 * @param selectItem
	 */
	protected void locationSelected(ClientLocation selectItem) {
		this.location = selectItem;
		if (location != null)
			locationCombo.setComboItem(this.location);
	}

	/**
	 * Updates the Transaction Obejct from the GUI Fields before saving.
	 */
	protected void updateTransaction() {

		if (transaction != null) {
			transaction.setDate(transactionDate.getDate());
			transaction.setType(transactionType);

			if (transactionNumber != null)
				transaction.setNumber(transactionNumber.getValue().toString());
			transactionItems.clear();
			List<ClientTransactionItem> list = getAllTransactionItems();
			if (list != null) {
				for (ClientTransactionItem item : list) {
					if (!item.isEmpty()) {
						transactionItems.add(item);
					}
				}
				transaction.setTransactionItems(transactionItems);
			}
			transactionDepositItems.clear();
			if (location != null)
				transaction.setLocation(location.getID());

			if (currency == null) {
				currency = getCompany().getPrimaryCurrency();
			}

			if (canAddAttachmentPanel()) {
				Set<ClientAttachment> attachments = transaction
						.getAttachments();
				attachments.clear();
				for (ClientAttachment attachment : getAttachments()) {
					attachments.add(attachment);
				}
			}
		}
	}

	protected void setAmountIncludeTAX() {
		if (vatinclusiveCheck == null || transaction == null
				|| transaction.getTransactionItems() == null) {
			return;
		}
		for (ClientTransactionItem item : transaction.getTransactionItems()) {
			item.setAmountIncludeTAX(vatinclusiveCheck.getValue());
		}
	}

	public List<ClientTransactionItem> getAllTransactionItems() {
		return null;
	}

	public List<ClientTransactionDepositItem> getAllTransactionDepositItems() {
		return null;
	}

	protected CurrencyComboWidget createCurrencyComboWidget() {
		ArrayList<ClientCurrency> currenciesList = getCompany().getCurrencies();
		ClientCurrency baseCurrency = getCompany().getPrimaryCurrency();

		CurrencyComboWidget widget = new CurrencyComboWidget(currenciesList,
				baseCurrency);
		widget.setListener(new CurrencyChangeListener() {

			@Override
			public void currencyChanged(ClientCurrency currency, double factor) {
				setCurrency(currency);
				setCurrencyFactor(factor);
				updateAmountsFromGUI();
			}
		});
		widget.setEnabled(!isInViewMode());
		return widget;
	}

	protected CurrencyFactorWidget createCurrencyFactorWidget() {
		ClientCurrency baseCurrency = getCompany().getPrimaryCurrency();
		CurrencyFactorWidget widget = new CurrencyFactorWidget(baseCurrency);
		widget.setListener(new CurrencyChangeListener() {

			@Override
			public void currencyChanged(ClientCurrency currency, double factor) {
				setCurrency(currency);
				setCurrencyFactor(factor);
				updateAmountsFromGUI();
			}
		});
		widget.setEnabled(!isInViewMode());
		return widget;
	}

	public void setVendor(ClientVendor vendor) {
		this.vendor = vendor;
	}

	public ClientVendor getVendor() {
		return vendor;
	}

	protected void billToaddressSelected(ClientAddress selectItem) {

		this.billingAddress = selectItem;
		if (this.billingAddress != null && billToCombo != null)
			billToCombo.setComboItem(this.billingAddress);
		else
			billToCombo.setValue("");

	}

	protected void initPayFromAccounts() {
		// getPayFromAccounts();
		// payFromCombo.initCombo(payFromAccounts);
		// payFromCombo.setAccountTypes(UIUtils
		// .getOptionsByType(AccountCombo.payFromCombo));
		payFromCombo.setAccounts();
		payFromCombo.setEnabled(!isInViewMode());
		payFromAccount = payFromCombo.getSelectedValue();
		if (payFromAccount != null)
			payFromCombo.setComboItem(payFromAccount);
	}

	public VendorCombo createVendorComboItem(String title) {

		VendorCombo vendorCombo = new VendorCombo(title != null ? title
				: Global.get().Vendor());
		vendorCombo.setRequired(true);
		vendorCombo.setEnabled(!isInViewMode());
		// vendorCombo.setShowDisabled(false);
		vendorCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientVendor>() {

					@Override
					public void selectedComboBoxItem(ClientVendor selectItem) {
						currency = getCurrency(selectItem.getCurrency());
						vendorSelected(selectItem);

					}

				});

		// vendorCombo.setShowDisabled(false);
		return vendorCombo;

	}

	protected void vendorSelected(ClientVendor vendor) {

		if (vendor == null)
			return;
		this.setVendor(vendor);
		initContacts(vendor);
		// initPhones(vendor);
		paymentMethodSelected(vendor.getPaymentMethod());
		addressListOfVendor = vendor.getAddress();
		initBillToCombo();

	}

	protected void initBillToCombo() {

		if (billToCombo == null || addressListOfVendor == null)
			return;

		Set<ClientAddress> tempSet = new HashSet<ClientAddress>();
		ClientAddress clientAddress = null;
		for (ClientAddress address : addressListOfVendor) {

			if (address.getType() == ClientAddress.TYPE_BILL_TO) {
				if (address != null) {
					tempSet.add(address);
					clientAddress = address;
					break;
				}
			}

		}
		List<ClientAddress> adressList = new ArrayList<ClientAddress>();
		adressList.addAll(tempSet);
		billToCombo.initCombo(adressList);
		if (adressList == null || adressList.size() == 0)
			billToCombo.setEnabled(false);
		else
			billToCombo.setEnabled(!isInViewMode());
		billToCombo.setDefaultToFirstOption(false);

		if (isInViewMode() && billingAddress != null) {
			billToCombo.setComboItem(billingAddress);
			return;
		}
		if (clientAddress != null) {
			billToCombo.setComboItem(clientAddress);
			billToaddressSelected(clientAddress);

		} else {
			billToCombo.setValue(null);
			// billToaddressSelected(clientAddress);
		}
	}

	public void initContacts(ClientVendor vendor) {
		if (contactCombo == null)
			return;
		this.contacts = vendor.getContacts();

		this.contact = vendor.getPrimaryContact();

		if (contacts != null && contacts.size() > 0) {
			List<ClientContact> contactList = new ArrayList<ClientContact>();
			contactList.addAll(contacts);
			contactCombo.initCombo(contactList);
			contactCombo.setEnabled(!isInViewMode());

			if (contact != null && contacts.contains(contact)) {
				contactCombo.setComboItem(contact);
				contactSelected(contact);

			}
		} else {
			contactCombo.initCombo(null);
			contactCombo.setValue("");
		}
	}

	protected void contactSelected(ClientContact contact) {
		if (contact == null)
			return;
		this.contact = contact;
		this.phoneNo = contact.getBusinessPhone();
		if (this.phoneNo != null && phoneSelect != null) {
			phoneSelect.setValue(this.phoneNo);

		}
		contactCombo.setComboItem(contact);
		// contactCombo.setDisabled(isEdit);

	}

	public AddressCombo createBillToComboItem() {

		AddressCombo addressCombo = new AddressCombo(messages.billTo(), false);
		addressCombo.setDefaultToFirstOption(false);
		addressCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAddress>() {

					@Override
					public void selectedComboBoxItem(ClientAddress selectItem) {

						billToaddressSelected(selectItem);

					}

				});

		addressCombo.setEnabled(!isInViewMode());
		// addressCombo.setShowDisabled(false);

		return addressCombo;

	}

	public PayFromAccountsCombo createPayFromCombo(String title) {

		PayFromAccountsCombo payFromCombo = new PayFromAccountsCombo(title);
		payFromCombo.setRequired(true);
		payFromCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {

					@Override
					public void selectedComboBoxItem(ClientAccount selectItem) {
						accountSelected(selectItem);
						// selectedAccount = (Account) selectItem;
						// adjustBalance();

					}

				});
		payFromCombo.setEnabled(!isInViewMode());
		// payFromCombo.setShowDisabled(false);
		// formItems.add(payFromCombo);
		return payFromCombo;
	}

	protected void accountSelected(ClientAccount account) {
		if (account == null)
			return;
		this.payFromAccount = account;
	}

	protected TextItem createCheckNumberItem() {

		final TextItem checkNo = new TextItem(messages.chequeNo(), "checkNo");
		checkNo.setEnabled(!isInViewMode());
		// checkNo.setShowDisabled(false);
		if (transaction != null) {
			if (transactionType == ClientTransaction.TYPE_CASH_PURCHASE) {
				ClientCashPurchase clientCashPurchase = (ClientCashPurchase) transaction;
				checkNo.setValue(clientCashPurchase.getCheckNumber());
			}
		}
		return checkNo;

	}

	protected void initMemoAndReference() {

		if (this.isInViewMode()) {

			ClientPayBill payBill = (ClientPayBill) transaction;

			if (payBill != null) {
				memoTextAreaItem.setDisabled(isInViewMode());
				setMemoTextAreaItem(payBill.getMemo());
				// setRefText(payBill.getReference());

			}
		}
	}

	/**
	 * Decides whether to add the "make it recurring" button to this view.
	 * Default value is <b>true</b>.
	 * 
	 * If you don't need recurring button then override this method in that
	 * specific view and return <b>false</b>.
	 * 
	 * @return
	 */
	protected boolean canRecur() {
		return transaction == null ? true
				: transaction.getSaveStatus() != ClientTransaction.STATUS_DRAFT;
	}

	/**
	 * create the job combo
	 * 
	 * @param customer
	 * 
	 * @return
	 */
	public JobCombo createJobListCombo() {

		jobListCombo = new JobCombo(messages.job(), true);
		jobListCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientJob>() {

					@Override
					public void selectedComboBoxItem(ClientJob selectItem) {
						jobSelected(selectItem);
					}
				});
		jobListCombo.addNewJobHandler(new ValueCallBack<ClientJob>() {

			@Override
			public void execute(final ClientJob value) {
				Accounter.createCRUDService().create(value,
						new AsyncCallback<Long>() {

							@Override
							public void onSuccess(Long result) {
								value.setID(result);
								jobSelected(value);
								getCompany().processUpdateOrCreateObject(value);
							}

							@Override
							public void onFailure(Throwable caught) {
								caught.printStackTrace();
							}
						});

			}
		});
		jobListCombo.setEnabled(!isInViewMode());
		return jobListCombo;

	}

	/**
	 * select job set in the combo
	 * 
	 * @param selectItem
	 */
	protected void jobSelected(ClientJob selectItem) {
		if (selectItem != null) {
			this.job = selectItem;
			jobListCombo.setComboItem(selectItem);
		}
	}

	public List<ClientTransactionItem> getAccountTransactionItems(
			List<ClientTransactionItem> transactionItems) {
		List<ClientTransactionItem> list = new ArrayList<ClientTransactionItem>();
		for (ClientTransactionItem item : transactionItems) {
			if (item.getType() == ClientTransactionItem.TYPE_ACCOUNT) {
				list.add(item);
			}
		}
		return list;
	}

	public List<ClientTransactionItem> getItemTransactionItems(
			List<ClientTransactionItem> transactionItems) {
		List<ClientTransactionItem> list = new ArrayList<ClientTransactionItem>();
		for (ClientTransactionItem item : transactionItems) {
			if (item.getType() == ClientTransactionItem.TYPE_ITEM) {
				list.add(item);
			}
		}
		return list;
	}

	public boolean isTrackTax() {
		if (transaction != null && transaction.haveTax()) {
			return true;
		} else {
			return getPreferences().isTrackTax();
		}
	}

	/**
	 * 
	 * @return
	 */
	public boolean isTrackClass() {
		if (transaction != null && transaction.haveClass()) {
			return true;
		} else {
			return getPreferences().isClassTrackingEnabled();
		}
	}

	public boolean isTrackJob() {
		if (transaction != null && transaction.getID() != 0
				&& transaction.hasJob()) {
			return true;
		} else {
			return getPreferences().isJobTrackingEnabled();
		}
	}

	/**
	 * 
	 * @return
	 */
	public boolean isClassPerDetailLine() {
		if (transaction != null && transaction.usesDifferentclasses()) {
			return true;
		} else {
			return getPreferences().isClassPerDetailLine();
		}
	}

	public boolean isTaxPerDetailLine() {
		if (transaction != null && transaction.usesDifferentTaxCodes()) {
			return true;
		} else {
			return getPreferences().isTaxPerDetailLine();
		}
	}

	public boolean isTrackPaidTax() {
		if (transaction != null && transaction.haveTax()) {
			return true;
		} else {
			return getPreferences().isTrackPaidTax();
		}
	}

	/**
	 * Update all the amounts in the Data by taking them from the GUI This
	 * method is only called when currency is changed, it will make all the
	 * amounts to be treated in selected currency
	 * 
	 * @param fromGUI
	 */
	public abstract void updateAmountsFromGUI();

	// @Override
	// public Double getAmountInTransactionCurrency(Double amount) {
	// if (currency != null && amount != null) {
	// if (currencyFactor < 1.0) {
	// currencyFactor = 1.0;
	// }
	// return amount / currencyFactor;
	// } else {
	// return amount;
	// }
	// }

	@Override
	public Double getAmountInBaseCurrency(Double amount) {
		if (currency != null && amount != null) {
			if (currencyFactor < 0.0) {
				currencyFactor = 1.0;
			}
			return amount * currencyFactor;
		} else {
			return amount;
		}
	}

	@Override
	public Double getCurrencyFactor() {
		return currencyFactor;
	}

	public void setCurrencyFactor(Double currencyFactor) {
		this.currencyFactor = currencyFactor;
	}

	public ClientCurrency getCurrencycode() {
		return currency;
	}

	public void setCurrency(ClientCurrency currencycode) {
		this.currency = currencycode;
		if (this.currency == getBaseCurrency()) {
			this.currencyFactor = 1.0;
		}
	}

	/**
	 * 
	 * @return
	 */
	public boolean isTrackDiscounts() {
		if (transaction != null && transaction.haveDiscount()) {
			return true;
		} else {
			return getPreferences().isTrackDiscounts();
		}
	}

	/**
	 * 
	 * @return
	 */
	public boolean isDiscountPerDetailLine() {
		if (transaction != null && transaction.usesDifferentDiscounts()) {
			return true;
		} else {
			return getPreferences().isDiscountPerDetailLine();
		}
	}

	@Override
	protected StyledPanel createHistoryView() {
		StyledPanel historyNotesPanel = new StyledPanel("historyNotesPanel");

		Label headerLabel = new Label(messages.historyAndNotes());
		headerLabel.addStyleName("history_notes_label");

		StyledPanel lastActivityPanel = new StyledPanel("lastActivityPanel");
		lastActivityHTML = new HTML();
		noteHTML = new HTML();
		lastActivityHTML.addStyleName("bold_HTML");
		noteHTML.addStyleName("bold_HTML");
		lastActivityPanel.add(lastActivityHTML);
		lastActivityPanel.add(noteHTML);
		lastActivityPanel.addStyleName("last_activity");

		historyNotesPanel.add(headerLabel);
		historyNotesPanel.add(lastActivityPanel);

		StyledPanel tablesPanel = new StyledPanel("tablesPanel");
		StyledPanel headersPanel = new StyledPanel("headersPanel");

		final Anchor historyLink = new Anchor(messages.showHistory());
		Anchor addNotesLink = new Anchor(messages.addNote());
		historyLink.addStyleName("history_notes_link");
		addNotesLink.addStyleName("history_notes_link");

		addNotesPanel = getNotesPanel();
		addNotesPanel.setVisible(false);

		headersPanel.add(historyLink);
		headersPanel.add(addNotesLink);
		headersPanel.addStyleName("history_links");

		tablesPanel.add(headersPanel);
		tablesPanel.add(addNotesPanel);

		final StyledPanel historyPanel = getHistoryPanel(data.getID());
		historyPanel.setVisible(false);
		tablesPanel.add(historyPanel);
		historyLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				historyPanel.setVisible(!historyPanel.isVisible());
				if (historyPanel.isVisible())
					historyLink.setHTML(messages.hideHistory());
				else
					historyLink.setHTML(messages.showHistory());
			}
		});

		historyPanel.addStyleName("history_notes_view");

		addNotesLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				addNotesPanel.setVisible(!addNotesPanel.isVisible());
			}
		});

		historyNotesPanel.add(tablesPanel);

		// Setting widths to all panels
		historyNotesPanel.addStyleName("history_notes_view");
		lastActivityPanel.addStyleName("history_notes_view");
		tablesPanel.addStyleName("history_notes_view");

		return historyNotesPanel;
	}

	@SuppressWarnings("unchecked")
	private StyledPanel getHistoryPanel(long Id) {
		StyledPanel historyPanel = new StyledPanel("historyPanel");
		historyTable = new TransactionHistoryTable(Id,
				(AbstractTransactionBaseView<ClientTransaction>) this);
		historyTable.addStyleName("user_activity_log");
		historyPanel.add(historyTable);
		return historyPanel;
	}

	private StyledPanel getNotesPanel() {
		StyledPanel notesPanel = new StyledPanel("notesPanel");

		Label noteLabel = new Label(messages.note());
		// text area....
		final TextArea notesArea = new TextArea();
		notesArea.removeStyleName("gwt-TextArea");
		notesArea.addStyleName("memoTextArea");
		notesArea.setHeight("85px");

		// buttons...
		StyledPanel buttonPanel = new StyledPanel("buttonPanel");

		final SaveAndCloseButton saveButton = new SaveAndCloseButton(
				messages.save());
		CancelButton cancelButton = new CancelButton();

		saveButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Accounter.createCRUDService().createNote(data.getID(),
						notesArea.getText(), new AsyncCallback<Long>() {

							@Override
							public void onSuccess(Long result) {
								historyTable.updateColumnsData();
								notesArea.setText("");
							}

							@Override
							public void onFailure(Throwable caught) {
								notesArea.setText("");
								Accounter.showError(messages
										.unableToSaveNote(caught.toString()));
							}
						});
				addNotesPanel.setVisible(false);
			}
		});
		cancelButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				addNotesPanel.setVisible(false);
			}
		});

		buttonPanel.add(saveButton);
		buttonPanel.add(cancelButton);

		notesPanel.add(noteLabel);
		notesPanel.add(notesArea);
		notesPanel.add(buttonPanel);
		buttonPanel.addStyleName("notes_button_panel");

		notesPanel.addStyleName("notes_Panel");
		return notesPanel;
	}

	public void updateLastActivityPanel(ClientTransactionLog transactionLog) {

		if (transactionLog.getType() != ClientTransactionLog.TYPE_NOTE) {
			lastActivityHTML.setHTML(messages.lastActivityMessages(
					historyTable.getActivityType(transactionLog.getType()),
					transactionLog.getUserName(),
					new Date(transactionLog.getTime()).toString()));
			noteHTML.setVisible(false);
		} else {

			lastActivityHTML.setHTML(messages.lastActivityMessageForNote(
					new Date(transactionLog.getTime()).toString(),
					transactionLog.getUserName()));
			noteHTML.setVisible(true);
			noteHTML.setHTML(transactionLog.getDescription());
		}
	}

	protected ClientFinanceDate getLastTaxReturnEndDate(long taxAgency) {
		ClientFinanceDate lastTaxReturnDate = null;
		for (ClientTAXReturn taxReturn : getCompany().getTAXReturns()) {
			if (taxReturn.getTaxAgency() != taxAgency) {
				continue;
			}
			ClientFinanceDate clientFinanceDate = new ClientFinanceDate(
					taxReturn.getPeriodEndDate());
			if (lastTaxReturnDate == null) {
				lastTaxReturnDate = clientFinanceDate;
			}
			if (lastTaxReturnDate.after(clientFinanceDate)) {
				lastTaxReturnDate = clientFinanceDate;
			}
		}
		return lastTaxReturnDate;
	}

	@Override
	protected ClientCurrency getBaseCurrency() {
		return getCompany().getPrimaryCurrency();
	}

	@Override
	protected ClientCurrency getCurrency(long currency) {
		return getCompany().getCurrency(currency);
	}

	protected void isValidCurrencyFactor(ValidationResult result) {
		if (currencyWidget != null && !currencyWidget.isShowFactorField()) {
			if (currencyWidget.getCurrencyFactor() <= 0) {
				result.addError(currencyWidget,
						messages.pleaseEntervalidCurrencyFactor());
			}
		}
	}

	public boolean validateAndUpdateTransaction(boolean shouldValidateAll) {
		for (Object errorSource : lastErrorSourcesFromValidation) {
			clearError(errorSource);
		}
		lastErrorSourcesFromValidation.clear();
		ValidationResult validationResult = null;
		if (shouldValidateAll) {
			validationResult = this.validate();
		} else {
			validationResult = this.validateBaseRequirement();
		}
		if (validationResult.haveErrors()) {
			for (Error error : validationResult.getErrors()) {
				addError(error.getSource(), error.getMessage());
				lastErrorSourcesFromValidation.add(error.getSource());
			}
			return false;
		}
		return true;
	}

	@Override
	public void saveAsDrafts() {
		transaction.setSaveStatus(ClientTransaction.STATUS_DRAFT);
		boolean isValidate = validate().getErrors().isEmpty();
		transaction.setValidated(isValidate);
		saveAndUpdateView();
	}

	protected ValidationResult validateBaseRequirement() {

		updateTransaction();

		ValidationResult result = new ValidationResult();

		if (needTransactionItems() && !allowEmptyTransactionItems()) {
			if (transactionItems == null || transactionItems.isEmpty()) {
				result.addError("TransactionItem",
						messages.thereAreNoTransactionItemsToSave());
			}
		}
		return result;
	}

	protected boolean needTransactionItems() {
		return true;
	}

	protected double getdiscount(List<ClientTransactionItem> transactionItems) {
		double discount = 0.0D;

		for (ClientTransactionItem clientTransactionItem : transactionItems) {
			if (clientTransactionItem != null
					&& clientTransactionItem.getReferringTransactionItem() == 0) {
				Double discountValue = clientTransactionItem.getDiscount();
				if (discountValue != null) {
					discount = discountValue.doubleValue();
					if (discount != 0.0D)
						break;
					else
						continue;
				}
			}
		}
		return discount;

	}

	/**
	 * Create for class Tracking
	 * 
	 * @return
	 */
	public ClassListCombo createAccounterClassListCombo() {
		classListCombo = new ClassListCombo(messages.accounterClass(), true);
		classListCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccounterClass>() {

					@Override
					public void selectedComboBoxItem(
							ClientAccounterClass selectItem) {
						accounterClass = selectItem;
						classSelected(selectItem);
					}
				});

		classListCombo
				.addNewAccounterClassHandler(new ValueCallBack<ClientAccounterClass>() {

					@Override
					public void execute(final ClientAccounterClass accouterClass) {
						accounterClass = accouterClass;
						Accounter.createCRUDService().create(accounterClass,
								new AsyncCallback<Long>() {

									@Override
									public void onSuccess(Long result) {
										accounterClass.setID(result);
										getCompany()
												.processUpdateOrCreateObject(
														accouterClass);
										classSelected(accounterClass);
									}

									@Override
									public void onFailure(Throwable caught) {
										caught.printStackTrace();
									}
								});
					}
				});

		classListCombo.setEnabled(!isInViewMode());

		return classListCombo;
	}

	protected abstract void classSelected(
			ClientAccounterClass clientAccounterClass);

	@Override
	protected boolean canDelete() {
		if (getMode() == null || getMode() == EditMode.CREATE) {
			return false;
		}
		if (transaction != null && transaction.isDraft()) {
			ClientUserPermissions permissions = getCompany().getLoggedInUser()
					.getPermissions();
			return permissions.getTypeOfInvoicesBills() == RolePermissions.TYPE_YES
					|| permissions.getTypeOfSaveasDrafts() == RolePermissions.TYPE_YES;
		}
		return super.canDelete();
	}

	@Override
	protected boolean isSaveButtonAllowed() {
		return Utility.isUserHavePermissions(transactionType);
	}


	public void addButtons(ButtonGroup group) {
		if (Accounter.hasPermission(Features.TRANSACTION_NAVIGATION)) {
			ImageButton prev;

			if (Accounter.isIpadApp()) {
				prev = new ImageButton(Accounter.getFinanceImages()
						.previousIcon(), "previous");
			} else {
				prev = new ImageButton(messages.previous(), Accounter
						.getFinanceImages().previous(), "previous");
			}

			prev.getElement().setId("previous");
			prev.setTitle(messages.clickThisToOpen(messages
					.previousTransaction()));
			prev.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					getTransaction(true);
				}
			});

			ImageButton next;
			if (Accounter.isIpadApp()) {
				next = new ImageButton(Accounter.getFinanceImages().nextIcon(),
						"next");
			} else {
				next = new ImageButton(messages.next1(), Accounter
						.getFinanceImages().next(), "next");
			}
			next.getElement().setId("next");
			next.setTitle(messages.clickThisToOpen(messages.nextTransaction()));
			next.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					getTransaction(false);
				}
			});

			prev.getElement().setAttribute("data-icon", "previous");
			next.getElement().setAttribute("data-icon", "next");
			addButton(group, prev);
			addButton(group, next);
		}
	}

	protected void getTransaction(boolean isPrev) {
		if (!isPrev && (transaction == null || transaction.id == 0)) {
			return;
		}
		final int subType = (transactionType == 7) ? ((QuoteView) this)
				.getType() : 0;
		AsyncCallback<Long> callback = new AsyncCallback<Long>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(Long result) {
				getManager().closeCurrentView();
				if (result == null) {
					openNewView(subType);
					return;
				}
				ReportsRPC.openTransactionView(transactionType, result);
			}
		};
		Accounter.createHomeService().getTransaction(isPrev, transaction.id,
				transactionType, subType, callback);
	}

	protected void openNewView(int subType) {
		Action action = UIUtils.getAddNewAction(transactionType, subType);
		if (action != null) {
			action.run(null, false);
		}
	}

	protected AddNewButton getItemAddNewButton() {
		AddNewButton itemTableButton = null;
		if(Global.get().preferences().getIndustryType() == 33)
		{
			 itemTableButton = new AddNewButton(
					messages.addNew(messages2.itConsultant()));
		}
		else 
		{
			 itemTableButton = new AddNewButton(messages.addNewItem());
		}
		itemTableButton.setEnabled(!isInViewMode());
		itemTableButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				addItem();
//				if(Global.get().preferences().getIndustryType() == 33)
//				{
//					ActionFactory.getNewConsultantAction().run();
//				}
//				else 
//				{
//					addItem();
//				}
			}
		});
		return itemTableButton;
	}

	protected AddNewButton getAccountAddNewButton() {
		AddNewButton accountTableButton = new AddNewButton(
				messages.addNew(messages.Account()));
		accountTableButton.setEnabled(!isInViewMode());
		accountTableButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				addAccount();
			}
		});
		return accountTableButton;
	}
}
