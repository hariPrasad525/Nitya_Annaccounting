package com.vimukti.accounter.web.client.ui.company;

/*
 * Modified by Murali A
 */

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientAccounterClass;
import com.vimukti.accounter.web.client.core.ClientBrandingTheme;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientJournalEntry;
import com.vimukti.accounter.web.client.core.ClientPayee;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.Accounter.AccounterType;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.AbstractTransactionBaseView;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.core.ErrorDialogHandler;
import com.vimukti.accounter.web.client.ui.core.IPrintableView;
import com.vimukti.accounter.web.client.ui.customers.NewCustomerAction;
import com.vimukti.accounter.web.client.ui.edittable.tables.TransactionJournalEntryTable;
import com.vimukti.accounter.web.client.ui.forms.AmountLabel;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.ui.settings.RolePermissions;
import com.vimukti.accounter.web.client.ui.vendors.NewVendorAction;
import com.vimukti.accounter.web.client.ui.widgets.AddButton;
import com.vimukti.accounter.web.client.ui.widgets.CurrencyChangeListener;
import com.vimukti.accounter.web.client.ui.widgets.CurrencyComboWidget;

public class JournalEntryView extends
		AbstractTransactionBaseView<ClientJournalEntry> implements
		IPrintableView {

	private TransactionJournalEntryTable grid;
	double debit, credit;
	CurrencyComboWidget currencyCombo;
	Label lab1;
	DynamicForm memoForm, totalForm, dateForm;
	TextItem jourNoText;
	TextAreaItem memoText;
	protected boolean isClose;
	AmountLabel creditTotalText, deditTotalText;

	// private StyledPanel lablPanel;
	private StyledPanel gridPanel;

	private ArrayList<DynamicForm> listforms;
	private AddButton addButton;
	private final boolean locationTrackingEnabled;
	private StyledPanel mainVLay;

	public JournalEntryView() {
		super(ClientTransaction.TYPE_JOURNAL_ENTRY);
		this.getElement().setId("JournalEntryView");
		locationTrackingEnabled = Global.get().preferences()
				.isLocationTrackingEnabled();
	}

	@Override
	public ClientJournalEntry saveView() {
		ClientJournalEntry saveView = super.saveView();
		if (saveView != null) {
			updateTransaction();
		}
		return saveView;
	}

	@Override
	public void saveAndUpdateView() {
		updateTransaction();
		super.saveAndUpdateView();
		saveOrUpdate(transaction);
	}

	@Override
	protected boolean canAddDraftButton() {
		return false;
	}

	@Override
	public ValidationResult validate() {

		// No need of super class validations.. Because required validations are
		// doing here only..
		ValidationResult result = new ValidationResult();

		if (this.transactionDateItem != null
				&& this.transactionDateItem.getDate().getDate() == 0) {
			result.addError(transactionDateItem,
					messages.pleaseEnter(messages.transactionDate()));
			return result;
		}

		// Validations
		// 1. is valid memo?
		// 2. is valid transaction date?
		// 3. is in prevent posting before date?
		// 4. date form valid?
		// 5. is blank transaction?
		// 6. is valid grid?
		// 7. is valid total?

		List<ClientTransactionItem> allEntries = grid.getAllRows();
		for (ClientTransactionItem entry : allEntries) {
			if (entry.isEmpty()) {
				continue;
			}
			if (entry.getLineTotal() == null || entry.getLineTotal() == 0) {
				result.addError(this,
						messages.valueCannotBe0orlessthan0(messages.amount()));
			}
		}
		if (memoText.getValue().toString() != null
				&& memoText.getValue().toString().length() >= 256) {
			result.addError(memoText,
					messages.memoCannotExceedsmorethan255Characters());

		}
		// if (!AccounterValidator.isValidTransactionDate(getTransactionDate()))
		// {
		// result.addError(transactionDateItem,
		// messages.invalidateTransactionDate());
		// } else
		if (AccounterValidator
				.isInPreventPostingBeforeDate(getTransactionDate())) {
			result.addError(transactionDateItem, messages.invalidateDate());
		}
		result.add(dateForm.validate());
		// if (AccounterValidator.isBlankTransaction(grid)) {
		// result.addError(grid, messages.blankTransaction());
		// } else
		result.add(grid.validateGrid());
		if (!grid.isValidTotal()) {
			result.addError(grid, messages.totalMustBeSame());
		}
		return result;

	}

	// protected boolean validateForm() {
	//
	// return dateForm.validate(false);
	// }

	public void initListGrid() {
		grid = new TransactionJournalEntryTable(this) {

			@Override
			public void updateNonEditableItems() {
				JournalEntryView.this.updateNonEditableItems();
			}

			@Override
			protected boolean isInViewMode() {
				return JournalEntryView.this.isInViewMode();
			}
		};
		grid.setEnabled(!isInViewMode());
		grid.getElement().getStyle().setMarginTop(10, Unit.PX);
		addEmptRecords();
	}

	@Override
	public void saveSuccess(IAccounterCore result) {
		if (result != null) {
			// if (takenJournalEntry != null)
			// Accounter.showInformation(FinanceApplication
			// .constants().journalUpdatedSuccessfully());
			super.saveSuccess(result);
			// if (saveAndClose) {
			// save();
			// } else {

			// clearFields();

			// }
			// if (callback != null) {
			// callback.onSuccess(result);
			// }
		} else {
			saveFailed(new AccounterException(messages.imfailed()));
		}

	}

	//
	// protected void save() {
	// MainFinanceWindow.removeFromTab(this);
	//
	// }

	@Override
	public List<ClientTransactionItem> getAllTransactionItems() {
		return grid.getAllRows();
	}

	@Override
	protected void updateTransaction() {
		super.updateTransaction();
		if (isInViewMode()) {
			jourNoText.setEnabled(false);
			memoText.setDisabled(true);
			// memoText.setDisabled(true);
			// FIXME--need to implement this feature
			// grid.setEnableMenu(false);

			grid.setEnabled(false);
			// Disabling the cells
			// FIXME
			// grid.setEditDisableCells(0, 1, 2, 3, 4, 5, 6, 7);
		}
		if (isTrackClass() && classListCombo.getSelectedValue() != null) {
			transaction.setAccounterClass(classListCombo.getSelectedValue()
					.getID());
		}
		transaction.setNumber(jourNoText.getValue().toString());
		transaction.setDate(transactionDateItem.getEnteredDate().getDate());
		transaction.setMemo(memoText.getValue().toString() != null ? memoText
				.getValue().toString() : "");
		// initMemo(transaction);
		if (DecimalUtil.isEquals(grid.getTotalDebittotal(),
				grid.getTotalCredittotal())) {
			transaction.setDebitTotal(grid.getTotalDebittotal());
			transaction.setCreditTotal(grid.getTotalCredittotal());
			transaction.setTotal(grid.getTotalDebittotal());
		}
		if (isMultiCurrencyEnabled()) {
			transaction
					.setCurrency(currencyCombo.getSelectedCurrency().getID());
		}
		transaction.setCurrencyFactor(currencyCombo.getCurrencyFactor());
	}

	private void initMemo(ClientJournalEntry journalEntry) {
		if (memoText.getValue().toString() != null
				&& memoText.getValue().toString().length() >= 255) {
			addError(this, messages.iamHere());

		} else
			journalEntry.setMemo(memoText.getValue() != null ? memoText
					.getValue().toString() : "");

	}

	protected void clearFields() {
		// FIXME-- The form values need to be reset
		// jourForm.resetValues();
		grid.clear();

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
				deditTotalText.setTitle(messages2
						.debitTotalColonSymbol(currency.getSymbol()));
				creditTotalText.setTitle(messages2
						.creditTotalColonSymbol(currency.getSymbol()));

			}
		});
		widget.setEnabled(!isInViewMode());
		return widget;
	}

	@Override
	protected void createControls() {
		listforms = new ArrayList<DynamicForm>();

		lab1 = new Label(messages.journalEntry());
		lab1.removeStyleName("gwt-Label");
		lab1.addStyleName("label-title");
		currencyCombo = createCurrencyComboWidget();
		currencyCombo.setEnabled(!isInViewMode());
		// lab1.setHeight("35px");
		transactionDateItem = createTransactionDateItem();
		jourNoText = new TextItem(messages.no(), "jourNoText");
		jourNoText
				.setToolTip(messages.giveNoTo(this.getAction().getViewName()));
		jourNoText.setRequired(true);
		jourNoText.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				String str = jourNoText.getValue().toString();
				// if (!UIUtils.isNumber(str)) {
				// Accounter
				// .showError(AccounterErrorType.INCORRECTINFORMATION);
				// jourNoText.setValue("");
				// }
			}
		});

		memoText = createMemoTextAreaItem();

		initListGrid();
		// grid.initTransactionData();
		gridPanel = new StyledPanel("gridPanel");
		// addButton = new AddButton(messages.transaction());
		// addButton.addClickHandler(new ClickHandler() {
		//
		// @Override
		// public void onClick(ClickEvent event) {
		// addEmptRecords();
		// }
		// });
		Label tableTitle = new Label(messages2.table(messages.journalEntries()));
		tableTitle.addStyleName("editTableTitle");
		gridPanel.add(tableTitle);
		gridPanel.add(grid);

		// StyledPanel hPanel = new StyledPanel("hPanel");
		// hPanel.add(addButton);
		// hPanel.getElement().getStyle().setMarginTop(8, Unit.PX);
		// hPanel.getElement().getStyle().setFloat(Float.LEFT);

		// gridPanel.add(hPanel);

		// addButton.setEnabled(!isInViewMode());
		dateForm = new DynamicForm("dateForm");
		dateForm.setStyleName("datenumber-panel");

		locationCombo = createLocationCombo();
		dateForm.add(transactionDateItem, jourNoText);

		classListCombo = createAccounterClassListCombo();

		StyledPanel datepannel = new StyledPanel("datepannel");
		// datepannel.setWidth("100%");
		datepannel.add(dateForm);
		// datepannel.setCellHorizontalAlignment(dateForm, ALIGN_RIGHT);

		memoForm = new DynamicForm("memoForm");
		// memoForm.setWidth("100%");
		memoForm.add(memoText);
		// memoForm.getCellFormatter().addStyleName(0, 0, "memoFormAlign");

		deditTotalText = new AmountLabel(
				messages2.debitTotalColonSymbol(getCompany()
						.getPrimaryCurrency().getSymbol()));
		// deditTotalText.setWidth("180px");
		((Label) deditTotalText.getMainWidget())
				.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		deditTotalText.setDefaultValue("" + UIUtils.getCurrencySymbol()
				+ "0.00");
		deditTotalText.setEnabled(false);

		creditTotalText = new AmountLabel(
				messages2.creditTotalColonSymbol(getCompany()
						.getPrimaryCurrency().getSymbol()));
		// creditTotalText.setWidth("180px");
		((Label) creditTotalText.getMainWidget())
				.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		creditTotalText.setDefaultValue("" + UIUtils.getCurrencySymbol()
				+ "0.00");
		creditTotalText.setEnabled(false);

		totalForm = new DynamicForm("totalForm");
		// totalForm.setWidth("78%");
		totalForm.addStyleName("textbold");
		totalForm.add(deditTotalText, creditTotalText);

		StyledPanel bottomPanel = new StyledPanel("bottomPanel");
		// bottomPanel.setWidth("100%");
		bottomPanel.add(memoForm);
		// bottomPanel.setCellHorizontalAlignment(memoForm, ALIGN_LEFT);
		bottomPanel.add(totalForm);
		// bottomPanel.setCellHorizontalAlignment(totalForm, ALIGN_RIGHT);

		// addButton.getElement().getParentElement().addClassName("add-button");

		// ThemesUtil
		// .addDivToButton(addButton, FinanceApplication.getThemeImages()
		// .button_right_blue_image(), "blue-right-image");

		// gridPanel.add(labelPanel);

		if (isInViewMode()) {
			jourNoText.setValue(transaction.getNumber());
			memoText.setValue(transaction.getMemo());

			// journalEntry.setEntry(getallEntries(journalEntry));
			// journalEntry.setDebitTotal(totalDebittotal);
			// journalEntry.setCreditTotal(totalCredittotal);

		}
		StyledPanel verticalPanel = new StyledPanel("verticalPanel");

		StyledPanel leftVLay = new StyledPanel("leftVLay");
		StyledPanel rightVLay = new StyledPanel("rightVLay");
		if (getPreferences().isClassTrackingEnabled()) {
			rightVLay.add(classListCombo);
		}
		if (locationTrackingEnabled) {
			rightVLay.add(locationCombo);
		}
		verticalPanel.add(datepannel);
		if (isMultiCurrencyEnabled())
			leftVLay.add(currencyCombo);

		mainVLay = new StyledPanel("mainVLay");

		mainVLay.add(lab1);
		mainVLay.add(voidedPanel);
		mainVLay.add(verticalPanel);
		StyledPanel topHLay = getTopLayOut();
		if (topHLay != null) {
			topHLay.add(leftVLay);
			topHLay.add(rightVLay);
			mainVLay.add(topHLay);
		} else {
			mainVLay.add(leftVLay);
			mainVLay.add(rightVLay);
		}
		mainVLay.add(gridPanel);
		mainVLay.add(bottomPanel);
		// mainVLay.add(labelPane);

		this.add(mainVLay);

		listforms.add(dateForm);
		listforms.add(memoForm);
		listforms.add(totalForm);

		/* Adding dynamic forms in list */

	}

	protected StyledPanel getTopLayOut() {
		return new StyledPanel("topHLay");
	}

	@Override
	protected void initTransactionViewData() {
		if (transaction != null) {
			jourNoText.setValue(transaction.getNumber());
			transactionDateItem.setEnteredDate(transaction.getDate());
			// grid.setVoucherNumber(transaction.getNumber());
			if (isMultiCurrencyEnabled()) {
				if (transaction.getCurrency() > 0) {
					this.currency = getCompany().getCurrency(
							transaction.getCurrency());
				} else {
					this.currency = getCompany().getPreferences()
							.getPrimaryCurrency();
				}
				this.currencyFactor = transaction.getCurrencyFactor();
				if (this.currency != null) {
					currencyCombo.setSelectedCurrency(currency);
				}
				currencyCombo
						.setCurrencyFactor(transaction.getCurrencyFactor());
				currencyCombo.setEnabled(!isInViewMode());
				deditTotalText.setTitle(messages2
						.debitTotalColonSymbol(currency.getSymbol()));
				creditTotalText.setTitle(messages2
						.creditTotalColonSymbol(currency.getSymbol()));
			}
			List<ClientTransactionItem> entries = transaction
					.getTransactionItems();

			grid.setAllRows(entries);
			if (transaction.getMemo() != null)
				memoText.setValue(transaction.getMemo());
			updateTransaction();
		} else {
			setData(new ClientJournalEntry());
		}
		if (isTrackClass()) {
			classListCombo.setComboItem(getCompany().getAccounterClass(
					transaction.getAccounterClass()));
		}
		initJournalNumber();
		if (locationTrackingEnabled)
			locationSelected(getCompany()
					.getLocation(transaction.getLocation()));

	}

	private void initJournalNumber() {
		if (isInViewMode()) {
			jourNoText.setValue(transaction.getNumber());
			if (transaction.getNumber().isEmpty()) {
				setTransactionNumber();
			}
		} else {
			setTransactionNumber();
		}

	}

	private void setTransactionNumber() {
		rpcUtilService.getNextTransactionNumber(
				ClientTransaction.TYPE_JOURNAL_ENTRY,
				new AccounterAsyncCallback<String>() {

					@Override
					public void onException(AccounterException caught) {
						Accounter.showError(messages
								.failedToGetTransactionNumber());
					}

					@Override
					public void onResultSuccess(String result) {
						if (result == null) {
							onFailure(new Exception());
						}
						jourNoText.setValue(String.valueOf(result));

					}
				});
	}

	protected void addEmptRecords() {
		ClientTransactionItem entry = new ClientTransactionItem();
		ClientTransactionItem entry1 = new ClientTransactionItem();
		entry.setAccount(0);
		entry.setDescription("");
		entry1.setAccount(0);
		entry1.setDescription("");
		entry.setType(ClientTransactionItem.TYPE_ACCOUNT);
		entry1.setType(ClientTransactionItem.TYPE_ACCOUNT);

		grid.add(entry);
		if (grid.getAllRows().size() < 2) {
			grid.add(entry1);
		}

	}

	@Override
	public void updateNonEditableItems() {
		if (grid == null)
			return;
		deditTotalText.setAmount(grid.getTotalDebittotal());
		creditTotalText.setAmount(grid.getTotalCredittotal());

	}

	@Override
	public List<DynamicForm> getForms() {

		return listforms;
	}

	/**
	 * call this method to set focus in View
	 */
	@Override
	public void setFocus() {
		this.jourNoText.setFocus();
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
	public void fitToSize(int height, int width) {
		super.fitToSize(height, width);

	}

	protected void enableFormItems() {
		setMode(EditMode.EDIT);
		jourNoText.setEnabled(!isInViewMode());
		transactionDateItem.setEnabled(!isInViewMode());
		grid.setEnabled(!isInViewMode());
		memoText.setDisabled(isInViewMode());
		// grid.setCanEdit(true);
		addButton.setEnabled(!isInViewMode());
		if (locationTrackingEnabled)
			locationCombo.setEnabled(!isInViewMode());
		if (isTrackClass())
			classListCombo.setEnabled(!isInViewMode());
		currencyCombo.setEnabled(!isInViewMode());
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		// addButton.setType(Button.ADD_BUTTON);
	}

	@Override
	public void print() {
		updateTransaction();
		ArrayList<ClientBrandingTheme> themesList = Accounter.getCompany()
				.getBrandingTheme();
		// if there is only one branding theme
		ClientBrandingTheme clientBrandingTheme = themesList.get(0);
		UIUtils.downloadAttachment(transaction.getID(),
				ClientTransaction.TYPE_JOURNAL_ENTRY,
				clientBrandingTheme.getID());
	}

	@Override
	public void printPreview() {
		// NOTHING TO DO.
	}

	@Override
	protected String getViewTitle() {
		return messages.journalEntry();
	}

	@Override
	protected void refreshTransactionGrid() {

	}

	@Override
	public void updateAmountsFromGUI() {
	}

	@Override
	public void onEdit() {
		if (transaction.getInvolvedPayee() != 0) {
			ClientPayee payee = getCompany().getPayee(
					transaction.getInvolvedPayee());
			showEditWarnDialog(payee);
		} else if (transaction.getInvolvedAccount() != 0) {
			ClientAccount account = getCompany().getAccount(
					transaction.getInvolvedAccount());
			showEditWarnDialog();
		} else {
			AsyncCallback<Boolean> editCallBack = new AsyncCallback<Boolean>() {

				@Override
				public void onFailure(Throwable caught) {
					if (caught instanceof InvocationException) {
						Accounter.showMessage(messages.sessionExpired());
					} else {
						int errorCode = ((AccounterException) caught)
								.getErrorCode();
						Accounter.showError(AccounterExceptions
								.getErrorString(errorCode));

					}
				}

				@Override
				public void onSuccess(Boolean result) {
					if (result)
						enableFormItems();
				}

			};

			AccounterCoreType type = UIUtils.getAccounterCoreType(transaction
					.getType());
			this.rpcDoSerivce.canEdit(type, transaction.id, editCallBack);
		}
	}

	private void showEditWarnDialog(ClientPayee payee) {
		String warning;
		if (payee.getType() == ClientPayee.TYPE_CUSTOMER) {
			warning = messages.W_113();
		} else {
			warning = messages.W_114();
		}
		Accounter.showWarning(warning, AccounterType.WARNING,
				new ErrorDialogHandler() {

					@Override
					public boolean onCancelClick() {
						return true;
					}

					@Override
					public boolean onNoClick() {
						return true;
					}

					@Override
					public boolean onYesClick() {
						editPayee();
						return true;
					}
				});
	}

	protected void editPayee() {
		AccounterAsyncCallback<ClientPayee> callback = new AccounterAsyncCallback<ClientPayee>() {

			@Override
			public void onException(AccounterException caught) {
			}

			@Override
			public void onResultSuccess(ClientPayee result) {
				if (result != null) {
					if (result instanceof ClientCustomer) {
						new NewCustomerAction().run((ClientCustomer) result,
								false);
					} else if (result instanceof ClientVendor) {
						new NewVendorAction().run((ClientVendor) result, false);
					}
				}
			}

		};
		AccounterCoreType type;
		ClientPayee payee = getCompany().getPayee(
				transaction.getInvolvedPayee());
		if (payee instanceof ClientCustomer) {
			type = AccounterCoreType.CUSTOMER;
		} else {
			type = AccounterCoreType.VENDOR;
		}
		Accounter.createGETService().getObjectById(type,
				transaction.getInvolvedPayee(), callback);
	}

	private void showEditWarnDialog() {
		String warning = messages2.W_118();
		Accounter.showWarning(warning, AccounterType.WARNING,
				new ErrorDialogHandler() {

					@Override
					public boolean onCancelClick() {
						return true;
					}

					@Override
					public boolean onNoClick() {
						return true;
					}

					@Override
					public boolean onYesClick() {
						editAccount();
						return true;
					}
				});
	}

	protected void editAccount() {
		AccounterAsyncCallback<ClientAccount> callback = new AccounterAsyncCallback<ClientAccount>() {

			@Override
			public void onException(AccounterException caught) {
			}

			@Override
			public void onResultSuccess(ClientAccount result) {
				if (result != null) {
					new NewAccountAction(result.getType()).run(result, false);
				}
			}

		};
		Accounter.createGETService().getObjectById(AccounterCoreType.ACCOUNT,
				transaction.getInvolvedAccount(), callback);
	}

	@Override
	protected ValidationResult validateBaseRequirement() {
		ValidationResult result = new ValidationResult();
		if (transactionItems == null || transactionItems.isEmpty()) {
			result.addError(this,
					messages.youCannotSaveAblankRecurringTemplate());
		}
		return result;
	}

	@Override
	public boolean canEdit() {
		if (Accounter.getUser().getPermissions().getTypeOfManageAccounts() == RolePermissions.TYPE_YES) {
			return super.canEdit();
		}
		return false;
	}

	protected void updateDiscountValues() {
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean canRecur() {
		if (transaction != null
				&& (transaction.getInvolvedAccount() != 0 || transaction
						.getInvolvedPayee() != 0)) {
			return false;
		}

		return super.canRecur();

	}

	@Override
	protected void classSelected(ClientAccounterClass clientAccounterClass) {
		this.accounterClass = clientAccounterClass;
		if (accounterClass != null) {
			classListCombo.setComboItem(accounterClass);
		} else {
			classListCombo.setValue("");
		}
	}

	@Override
	public boolean canPrint() {
		EditMode mode = getMode();
		if (mode == EditMode.CREATE || mode == EditMode.EDIT
				|| data.getSaveStatus() == ClientTransaction.STATUS_DRAFT) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public boolean canExportToCsv() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean allowEmptyTransactionItems() {
		return false;
	}

	@Override
	protected void createButtons() {
		super.createButtons();
		addButton = getAddNewButton();
		addButton(gridPanel, addButton);
	}

	private AddButton getAddNewButton() {
		AddButton addButton = new AddButton(messages.addNew(messages.Account()));
		addButton.setEnabled(!isInViewMode());
		addButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				addEmptRecords();
			}
		});
		return addButton;
	}

	@Override
	protected void clearButtons() {
		super.clearButtons();
		removeButton(gridPanel, addButton);
	}
}
