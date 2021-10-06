package com.vimukti.accounter.web.client.ui.fixedassets;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientFixedAsset;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.core.Lists.FixedAssetSellOrDisposeReviewJournal;
import com.vimukti.accounter.web.client.core.Lists.TempFixedAsset;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.combo.DebitAccountCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.Calendar;
import com.vimukti.accounter.web.client.ui.core.CancelButton;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.core.InputDialogHandler;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.RadioGroupItem;
import com.vimukti.accounter.web.client.ui.forms.SelectItem;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.widgets.DateUtills;
import com.vimukti.accounter.web.client.ui.widgets.DateValueChangeHandler;

/**
 * 
 * @author B.Anjaneyulu
 * @author B.Srinivasa Rao
 * 
 */
public class SellingRegisteredItemView extends BaseView<ClientFixedAsset> {
	protected DateField datesold;
	protected SelectItem dateItemCombo;
	protected DebitAccountCombo accountCombo;
	protected AmountField salepriceText;
	protected Label depriciationForFinancialyearLabel, detailsLabel,
			QuestionLabel;
	protected DynamicForm dateForm, detailsForm, textAreaForm;
	protected String yearValue, noDepOption, allDepOption, changedValue;
	protected RadioGroupItem QuestionItem;
	protected TextAreaItem notesArea;
	protected JournalViewDialog dialog;
	private ClientAccount accountForSale;

	private ArrayList<DynamicForm> listforms;
	private Button reviewJournal;

	public SellingRegisteredItemView() {
		super();
	}

	@Override
	public void init() {
		super.init();
		this.getElement().setId("SellingRegisteredItemView");
		createControls();
		dateSelected();
	}

	/**
	 * This method is for creating common controls for both selling and
	 * Disposing views
	 */

	private void createControls() {

		listforms = new ArrayList<DynamicForm>();
		detailsLabel = new Label(messages.details());
		detailsLabel.setStyleName("label-title");
		detailsForm = getDetailForm();

		depriciationForFinancialyearLabel = new Label();
		depriciationForFinancialyearLabel.setStyleName("label-title");
		depriciationForFinancialyearLabel.setText(messages.depriciationForThe()
				+ yearValue + messages.financialYear());

		StyledPanel topPanel = new StyledPanel("topPanel");
		topPanel.add(detailsForm);

		QuestionLabel = new Label();
		QuestionLabel.setText(messages
				.howMuchDepriciationShouldBeIcludedInThisFinancialYear());
		QuestionItem = new RadioGroupItem();
		noDepOption = messages.noDepreciationThisFinancialYear();
		allDepOption = messages.allDepreciationUpToIncluding();
		QuestionItem.setValues(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				changedValue = QuestionItem.getValue().toString();
				changeDateCombo(changedValue);
			}
		}, noDepOption, allDepOption);
		QuestionItem.setValue(noDepOption);
		DynamicForm radioForm = new DynamicForm("radioForm");
		radioForm.add(QuestionItem);
		dateItemCombo = new SelectItem(messages.date(), "dateItemCombo");
		dateForm = new DynamicForm("dateForm");
		// dateForm.setWidth("50%");
		dateForm.add(dateItemCombo);
		changeDateCombo(noDepOption);

		StyledPanel radioVlayout = new StyledPanel("radioVlayout");
		radioVlayout.add(QuestionLabel);
		radioVlayout.add(radioForm);
		radioVlayout.add(dateForm);

		notesArea = new TextAreaItem("", "notesArea");
		// notesArea.setWidth(100);
		notesArea.setToolTip(messages.writeCommentsForThis(
				this.getAction().getViewName()).replace(messages.comments(),
				messages.notes()));
		notesArea.setTitle(messages.notes());

		textAreaForm = new DynamicForm("textAreaForm");
		// textAreaForm.setWidth("100%");
		textAreaForm.add(notesArea);
		textAreaForm.setStyleName("align-form");

		StyledPanel mainLayout = new StyledPanel("mainLayout");
		mainLayout.add(detailsLabel);
		mainLayout.add(topPanel);
		mainLayout.add(depriciationForFinancialyearLabel);
		mainLayout.add(radioVlayout);
		mainLayout.add(textAreaForm);

		add(mainLayout);

		/* Adding dynamic forms in list */
		listforms.add(detailsForm);

		listforms.add(radioForm);
		listforms.add(dateForm);
		listforms.add(textAreaForm);

	}

	/**
	 * This method is using for getting DynamicForm of Selling View
	 * 
	 * @return
	 */
	protected DynamicForm getDetailForm() {
		datesold = new DateField(messages.datesold(), "datesold");
		datesold.setEnteredDate(new ClientFinanceDate());
		yearValue = String.valueOf(datesold.getYear());
		datesold.addDateValueChangeHandler(new DateValueChangeHandler() {

			@Override
			public void onDateValueChange(ClientFinanceDate date) {
				dateSelected();

			}
		});
		accountCombo = createDebitAccountCombo();
		accountCombo.setRequired(true);

		salepriceText = new AmountField(messages.salepriceExcludingTax(), this,
				getBaseCurrency(), "salepriceText");
		salepriceText.setRequired(true);
		// salepriceText.setWidth(100);
		DynamicForm detailForm = new DynamicForm("detailForm");
		detailForm.add(datesold, accountCombo, salepriceText);
		return detailForm;
	}

	/**
	 * This method is for Opening ReviewJournal Dialog Based on that
	 * FixedAssetSellOrDisposeReviewJournal
	 */

	protected void openJournalDialog(
			FixedAssetSellOrDisposeReviewJournal journalObject) {
		dialog = new JournalViewDialog(messages.journalView(), "",
				journalObject);
		dialog.addInputDialogHandler(new InputDialogHandler() {

			@Override
			public boolean onOK() {
				okClicked();
				changeButtonBarMode(false);
				return true;
			}

			@Override
			public void onCancel() {
				changeButtonBarMode(false);
			}
		});
	}

	protected void okClicked() {
		createSellingObject();
	}

	protected void createSellingObject() {
		ClientFixedAsset sellingAsset = getSellingObject();
		saveOrUpdate(sellingAsset);
	}

	/**
	 * this method is for setting the salesprice and Salesaccount to FixedAsset
	 * Object
	 */

	private ClientFixedAsset getSellingObject() {
		ClientFixedAsset sellingAsset = getSellOrDisposeObject();
		sellingAsset.setSalePrice(salepriceText.getAmount());
		if (this.accountForSale != null)
			sellingAsset.setAccountForSale(this.accountForSale.getID());
		return sellingAsset;
	}

	/**
	 * This method is for setting entered common values in both selling and
	 * disposing views to FixedAsset Object
	 */

	protected ClientFixedAsset getSellOrDisposeObject() {
		ClientFixedAsset sellorDisposeAsset = data;
		sellorDisposeAsset.setSoldOrDisposedDate(getSoldorDisposedDateField()
				.getEnteredDate().getDate());
		ClientFinanceDate date = DateUtills.getDateFromString(dateItemCombo
				.getValue());
		if (date != null)
			sellorDisposeAsset.setDepreciationTillDate(date.getDate());

		if (sellorDisposeAsset != null) {
			sellorDisposeAsset
					.setStatus(ClientFixedAsset.STATUS_SOLD_OR_DISPOSED);
		}
		sellorDisposeAsset.setNoDepreciation(QuestionItem.getValue().equals(
				noDepOption) ? true : false);
		if (notesArea.getValue() != null)
			sellorDisposeAsset.setNotes(notesArea.getValue().toString());
		if (dialog.totalCapitalGainAccount != null) {
			sellorDisposeAsset
					.setTotalCapitalGain(dialog.totalCapitalGainAccount.getID());
			sellorDisposeAsset.setTotalCapitalGainAmount(dialog
					.getTotalCapitalGainAmount());
		}
		if (dialog.LossorGainOnDisposalAccount != null) {
			sellorDisposeAsset
					.setLossOrGainOnDisposalAccount(dialog.LossorGainOnDisposalAccount
							.getID());
			sellorDisposeAsset.setLossOrGain(dialog.getLossorGainAmount());
		}
		return sellorDisposeAsset;
	}

	/**
	 * This method Calls when the sold or Disposed date is changed
	 */

	protected void dateSelected() {
		yearValue = String.valueOf(getSoldorDisposedDateField().getYear());
		depriciationForFinancialyearLabel.setText(messages.depriciationForThe()
				+ yearValue + "     " + messages.financialYear());
		initDateCombo();
	}

	/**
	 * Intilising Date Values in Date Combo in both Selling and Disposing views
	 */

	private void initDateCombo() {
		ArrayList<String> dateList = getLastDates();
		dateList.add(0, " ");
		dateItemCombo
				.setValueMap(dateList.toArray(new String[dateList.size()]));

	}

	/**
	 * Getting last dates of every month of selected Financial Year
	 * 
	 * @param year
	 * @return yearlist
	 */

	public ArrayList<String> getLastDates() {
		ArrayList<String> dateList = new ArrayList<String>();
		Calendar cal = Calendar.getInstance();
		ClientFinanceDate startDate = new ClientFinanceDate(getPreferences()
				.getStartOfFiscalYear());
		cal.setTime(startDate.getDateAsObject());
		for (int x = 0; x < 12; x++) {
			cal.set(Calendar.DAY_OF_MONTH,
					cal.getActualMaximum(Calendar.DAY_OF_MONTH));
			dateList.add(DateUtills.getDateAsString(cal.getTime()));
			startDate.setMonth(startDate.getMonth() + 1);
			cal.setTime(startDate.getDateAsObject());
		}
		return dateList;
	}

	/**
	 * This is for validating Required input fields in this both selling and
	 * Disposing Views
	 */

	@Override
	public ValidationResult validate() {
		ValidationResult result = new ValidationResult();

		// form validation
		// valid amount?
		// valid radio value
		// is valid sell or dispose date?

		result.add(detailsForm.validate());
		if (AccounterValidator.validate_ZeroAmount(salepriceText.getAmount())) {
			result.addError(salepriceText, messages.zeroAmount());
		}
		if (!AccounterValidator.validate_Radiovalue(QuestionItem.getValue())) {
			result.addError(QuestionItem, messages.shouldSelectRadio());
		}

		if (QuestionItem.getValue().equals(allDepOption)) {
			if (!AccounterValidator.isNullValue(dateItemCombo.getValue())) {
				result.addError(dateItemCombo, messages.pleaseSelectDate());
			}
		}
		ClientFixedAsset asset = data;
		if (asset != null)
			if (AccounterValidator.isValidSellorDisposeDate(
					new ClientFinanceDate(asset.getPurchaseDate()),
					getSoldorDisposedDateField().getEnteredDate())) {
				result.addError(
						dateItemCombo,
						messages.datesold()
								+ " "
								+ messages.conditionalMsg(messages
										.purchaseDate())
								+ "  ("
								+ DateUtills
										.getDateAsString(new ClientFinanceDate(
												asset.getPurchaseDate()))
								+ "  )");
			}
		if (asset != null)
			if (AccounterValidator.isValidSellorDisposeDate(
					new ClientFinanceDate(getPreferences()
							.getStartOfFiscalYear()),
					getSoldorDisposedDateField().getEnteredDate())) {
				result.addError(
						dateItemCombo,
						messages.datesold()
								+ " "
								+ messages.conditionalMsg(messages.fiscalYear())
								+ "  ("
								+ DateUtills
										.getDateAsString(new ClientFinanceDate(
												getPreferences()
														.getStartOfFiscalYear()))
								+ "  )");
			}
		return result;
	}

	protected DateField getSoldorDisposedDateField() {
		return datesold;
	}

	@Override
	public void saveAndUpdateView() {
		reviewJournal();
	}

	/**
	 * This method is for getting ReviewJournalObject for displaying in JOurnal
	 * Dialog by passing TempFixedAsset Object
	 * 
	 * @param tempObject
	 */

	protected void getJournalViewObjet(TempFixedAsset tempObject) {
		AccounterAsyncCallback<FixedAssetSellOrDisposeReviewJournal> callback = new AccounterAsyncCallback<FixedAssetSellOrDisposeReviewJournal>() {

			@Override
			public void onException(AccounterException caught) {
				Accounter.showError(messages.receiveJournalisFailed());
			}

			@Override
			public void onResultSuccess(
					FixedAssetSellOrDisposeReviewJournal result) {
				if (result == null) {
					onFailure(new Exception());
				}
				if (result != null) {
					FixedAssetSellOrDisposeReviewJournal journalObject = result;
					openJournalDialog(journalObject);
				}

			}
		};
		this.rpcUtilService.getReviewJournal(tempObject, callback);

	}

	/**
	 * Setting the salesprice and salesaccount for selling Item to
	 * TempFixedAssetObject
	 * 
	 * @return
	 */

	protected TempFixedAsset getTempFixedAssetObject() {
		TempFixedAsset tempFixedAsset = getPreparedTempAssetObject();
		if (this.salepriceText != null) {
			tempFixedAsset.setSalesPrice(salepriceText.getAmount());
			if (this.accountForSale != null)
				tempFixedAsset.setSalesAccountName(this.accountForSale
						.getName());
		}
		return tempFixedAsset;
	}

	/**
	 * This method for creating TempObject for getting Review Journal Object and
	 * common method for selling and Disposing Views.
	 * 
	 * @return
	 */

	protected TempFixedAsset getPreparedTempAssetObject() {
		TempFixedAsset tempFixedAsset = new TempFixedAsset();
		ClientFixedAsset asset = data;
		tempFixedAsset.setFixedAssetID(asset.getID());
		tempFixedAsset.setPurchaseDate(new ClientFinanceDate(asset
				.getPurchaseDate()));
		tempFixedAsset.setNoDepreciation(QuestionItem.getValue().equals(
				noDepOption) ? true : false);
		tempFixedAsset.setSoldOrDisposedDate(getSoldorDisposedDateField()
				.getEnteredDate());
		ClientFinanceDate date = DateUtills.getDateFromString(dateItemCombo
				.getValue());
		if (date != null)
			tempFixedAsset.setDepreciationTillDate(date);
		tempFixedAsset.setPurchasePrice(asset.getPurchasePrice());
		tempFixedAsset.setBookValue(asset.getBookValue());
		ClientAccount assetAccount = getCompany().getAccount(
				asset.getAssetAccount());
		ClientAccount linkdAccount = getCompany().getAccount(
				assetAccount.getLinkedAccumulatedDepreciationAccount());
		tempFixedAsset
				.setLinkedAccumulatedDepreciatedAccountName(linkdAccount != null ? linkdAccount
						.getName() : "");
		tempFixedAsset.setAssetAccountName(assetAccount != null ? assetAccount
				.getName() : "");
		tempFixedAsset.setExpenseAccountName(getCompany().getAccount(
				asset.getDepreciationExpenseAccount()).getName());
		tempFixedAsset.setDepreciationMethod(asset.getDepreciationMethod());
		tempFixedAsset.setDepreciationRate(asset.getDepreciationRate());
		return tempFixedAsset;
	}

	@Override
	public void saveSuccess(IAccounterCore result) {
		ClientFixedAsset createdAsset = (ClientFixedAsset) result;
		if (createdAsset.getID() != 0) {
			// Accounter.showInformation(FinanceApplication
			// .constants().fixedAssetItemHasBeenSold());
			saveAndClose = true;
			super.saveSuccess(result);
			History.newItem(new SoldDisposedFixedAssetsListAction()
					.getHistoryToken());
			// ActionFactory.getSoldDisposedListAction().run(null,
			// false);
		} else
			saveFailed(new AccounterException(messages.failed()));

	}

	@Override
	public void saveFailed(AccounterException exception) {
		super.saveFailed(exception);
		AccounterException accounterException = exception;
		int errorCode = accounterException.getErrorCode();
		String errorString = AccounterExceptions.getErrorString(errorCode);
		Accounter.showError(errorString);
	}

	/**
	 * setting the Date Item whether required or not depends on the RadioButton
	 * Option
	 * 
	 * @param value
	 */
	protected void changeDateCombo(String value) {
		if (value.equals(allDepOption)) {
			dateForm.clear();
			dateItemCombo.setEnabled(true);
			dateItemCombo.setRequired(true);
			dateForm.add(dateItemCombo);

		} else {
			dateForm.clear();
			initDateCombo();
			dateItemCombo.setEnabled(false);
			dateItemCombo.setRequired(false);
			dateForm.add(dateItemCombo);
		}
	}

	private DebitAccountCombo createDebitAccountCombo() {
		DebitAccountCombo accountCombo = new DebitAccountCombo(
				messages.accountToDebitForSale());

		accountCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {

					@Override
					public void selectedComboBoxItem(ClientAccount selectItem) {
						setAccountforsale(selectItem);
					}
				});
		return accountCombo;
	}

	protected void setAccountforsale(ClientAccount account) {
		if (account != null)
			this.accountForSale = account;
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
		this.salepriceText.setFocus();

	}

	@Override
	public void deleteFailed(AccounterException caught) {

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {

	}

	@Override
	public void fitToSize(int height, int width) {
		super.fitToSize(height, width);

	}

	@Override
	public void onEdit() {
		setMode(EditMode.EDIT);
	}

	@Override
	public void print() {

	}

	@Override
	public void printPreview() {

	}

	@Override
	protected String getViewTitle() {
		return messages.sellingRegisteredItem();
	}

	@Override
	protected void createButtons() {
		reviewJournal = new Button(messages.reviewJournal());

		reviewJournal.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				onSave(false);
			}
		});

		this.cancelButton = new CancelButton(this);

		reviewJournal.getElement().setAttribute("data-icon", "view");
		addButton(reviewJournal);
		addButton(cancelButton);
	}

	protected void reviewJournal() {
		getJournalViewObjet(getTempFixedAssetObject());
	}

	@Override
	public boolean canEdit() {
		return false;
	}
}
