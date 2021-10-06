package com.vimukti.accounter.web.client.ui.fixedassets;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Label;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientFixedAsset;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.ImageButton;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.combo.DepreciationAccountCombo;
import com.vimukti.accounter.web.client.ui.combo.DepreciationMethodCombo;
import com.vimukti.accounter.web.client.ui.combo.FixedAssetAccountCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.CancelButton;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.core.PercentageField;
import com.vimukti.accounter.web.client.ui.core.SaveAndCloseButton;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.SelectItem;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.ui.widgets.DateUtills;
import com.vimukti.accounter.web.client.ui.widgets.DateValueChangeHandler;

/**
 * @author Murali.A
 * 
 */
public class NewFixedAssetView extends BaseView<ClientFixedAsset> {

	private TextItem newItemTxt, assetType, assetNumberTxt;
	private FixedAssetAccountCombo accountCombo;
	private DateField purchaseDateTxt;
	private AmountField purchasePriceTxt, accmulatdDepreciationTxt;
	private Label descrptionLabel, infoLabel1, infoLabl2, labl;
	private TextAreaItem descriptionTxtArea;
	private DynamicForm itemInfoForm, purchaseInfoForm, descriptionForm,
			itmNameForm, assetTypeForm, depreciationForm, acumulatedDeprcForm,
			accumulatedDepreciationAccountForm;
	private StyledPanel accumltdAccVPanel, mainVPanel, accumulateDeprctVPanel;
	private StyledPanel itemHPanel, depreciationHPanel, lablHPanel;
	private DepreciationMethodCombo depreciationMethod;
	private DepreciationAccountCombo depreciationAccount;
	private PercentageField depreciationRate;
	// private String financialYrStartDate = "1st Jan 2010",
	// deprciatedDate = "1st Jan 2009";

	private ClientAccount selectedAssetAccount;

	// private double givenDepRate;
	private boolean isAssetAccumulated;
	private FixedAssetAccountCombo accumulatedDepreciationAccount;
	private boolean isAccumltd;
	private SelectItem assetOptions;
	private NoteDialog noteDialog;

	private ArrayList<DynamicForm> listforms;
	private double depAmount;
	private String selectedOption;
	private boolean isVerified = false;
	private ImageButton registerButton;

	public NewFixedAssetView() {
		super();
	}

	@Override
	public void init() {
		super.init();
		this.getElement().setId("NewFixedAssetView");
		createControls();
	}

	@Override
	public void initData() {
		if (getData() == null) {
			setData(new ClientFixedAsset());
			labl.setText(messages.newAsset());
			labl.setStyleName("label-title");
			initAssetNumber();
			return;
		} else {
			switch (data.getStatus()) {
			case 0:
				labl.setText(messages.newAsset());
				labl.setStyleName("label-title");
				break;
			case ClientFixedAsset.STATUS_PENDING:
				labl.setText(messages.pendingAsset());
				labl.setStyleName("label-title");
				break;
			case ClientFixedAsset.STATUS_REGISTERED:
				labl.setText(messages.registeredAsset());
				labl.setStyleName("label-title");
				setRequiredFields();
				break;
			case ClientFixedAsset.STATUS_SOLD_OR_DISPOSED:
				labl.setText(messages.assetSold());
				labl.setStyleName("label-title");
			}
			// Need to write
			assetOptions = new SelectItem(messages.assetOptions(),
					"assetOptions");
			if (data.getStatus() == ClientFixedAsset.STATUS_REGISTERED) {
				assetOptions.setValueMap("", messages.edit(), messages.sell(),
						messages.dispose());
			} else if (data.getStatus() == ClientFixedAsset.STATUS_PENDING) {
				assetOptions.setValueMap("", messages.edit());
			} else {
				assetOptions.setTitle("");
				assetOptions.setVisible(false);
			}
			assetOptions.addChangeHandler(new ChangeHandler() {

				@Override
				public void onChange(ChangeEvent event) {
					selectedOption = assetOptions.getValue().toString();
					assetOptions.setValue("");
					if (selectedOption.equalsIgnoreCase(messages.sell())) {
						Action action = new SellingRegisteredItemAction();
						action.catagory = messages.fixedAssetsNewFixedAsset();
						action.run(data, true);
					} else if (selectedOption.equalsIgnoreCase(messages
							.dispose())) {
						Action action = new DisposingRegisteredItemAction();
						action.catagory = messages.fixedAssetsNewFixedAsset();
						action.run(data, true);
					} else if (selectedOption.equalsIgnoreCase(messages
							.showHistory())) {
						Action action = new HistoryListAction();
						action.catagory = messages.fixedAssetsNewFixedAsset();
						action.run(data, true);
					} else if (selectedOption.equalsIgnoreCase(messages.edit())) {
						assetOptions.setValue(selectedOption);
						onEdit();
					}
				}
			});
			DynamicForm assetOptionsForm = new DynamicForm("assetOptionsForm");
			listforms.add(assetOptionsForm);
			assetOptionsForm.add(assetOptions);
			// lablHPanel.setHorizontalAlignment(ALIGN_RIGHT);
			// lablHPanel.setCellHorizontalAlignment(assetOptionsForm,
			// HasHorizontalAlignment.ALIGN_RIGHT);
			StyledPanel assetOptionsHPanel = new StyledPanel(
					"assetOptionsHPanel");
			// assetOptionsHPanel.setHorizontalAlignment(ALIGN_RIGHT);
			// assetOptionsHPanel.setCellHorizontalAlignment(assetOptionsForm,
			assetOptionsHPanel.add(assetOptionsForm);
			lablHPanel.add(assetOptionsHPanel);

			// populating the data into respective fields in edit mode

			newItemTxt.setValue(data.getName() != null ? data.getName() : "");
			assetNumberTxt.setValue(data.getAssetNumber());
			ClientAccount assetAccount = getCompany().getAccount(
					data.getAssetAccount());
			if (assetAccount != null) {
				accountCombo.setComboItem(assetAccount);
				if (assetAccount.getLinkedAccumulatedDepreciationAccount() == 0) {
					showAccumltdAccountForm();
				}
			}
			purchaseDateTxt.setEnteredDate(new ClientFinanceDate(data
					.getPurchaseDate()));
			purchasePriceTxt.setAmount(data.getPurchasePrice());
			descriptionTxtArea.setValue(data.getDescription());
			assetType.setValue(data.getAssetType());
			depreciationRate.setPercentage(data.getDepreciationRate());
			depreciationMethod.setSelected(depreciationMethod
					.getNameByType(data.getDepreciationMethod()));
			depreciationMethod.setSelectedValue(data.getDepreciationMethod());
			if (data.getDepreciationExpenseAccount() != 0) {
				if (getCompany().getAccount(
						data.getDepreciationExpenseAccount()) != null) {
					depreciationAccount.setComboItem(Accounter.getCompany()
							.getAccount(data.getDepreciationExpenseAccount()));
				}
			} else
				depreciationAccount.setSelected("");
			if (!DecimalUtil.isEquals(data.getAccumulatedDepreciationAmount(),
					0)) {
				showAccumultdDepAmountForm(new ClientFinanceDate(
						data.getPurchaseDate()));
			}
			Label bookValueLbl = new Label();
			bookValueLbl.setText(messages.bookValue() + data.getBookValue());
			Label accumDepLbl = new Label();
			accumDepLbl.setText(messages.accumulatedDepreciation()
					+ data.getAccumulatedDepreciationAmount());
			ClientAccount assetAcc = accountCombo.getSelectedValue() != null ? accountCombo
					.getSelectedValue() : null;
			if (assetAcc != null) {
				long strID = assetAcc.getLinkedAccumulatedDepreciationAccount();
				if (strID != 0)
					data.setAccumulatedDepreciationAccount(strID);
			}
			// if (data.getAccumulatedDepreciationAccount() == 0) {
			// showAccumltdAccountForm();
			// }
			showAccumultdDepAmountForm(purchaseDateTxt.getEnteredDate());
			StyledPanel bookValueVPanel = new StyledPanel("bookValueVPanel");
			mainVPanel.add(bookValueVPanel);

			selectedAssetAccount = getCompany().getAccount(
					data.getAssetAccount());
			disableFields(isInViewMode());
		}

	}

	private void disableFields(boolean disable) {
		setMode(EditMode.EDIT);
		itmNameForm.setEnabled(!disable);
		itemInfoForm.setEnabled(!disable);
		purchaseInfoForm.setEnabled(!disable);
		assetTypeForm.setEnabled(!disable);
		depreciationForm.setEnabled(!disable);
		descriptionForm.setEnabled(!disable);
		saveAndCloseButton.setEnabled(!disable);
		registerButton.setEnabled(!disable);
		if (acumulatedDeprcForm != null)
			acumulatedDeprcForm.setEnabled(!disable);
		if (accumulatedDepreciationAccountForm != null
				&& accumulatedDepreciationAccount != null)
			accumulatedDepreciationAccountForm.setEnabled(!disable);

		/*
		 * If an asset saved as a pending item,the linkedaccount might be
		 * null.But while registering,this asset must hav a linked account.So,we
		 * need to display when "edit" is Selected
		 */

		if (selectedOption != null
				&& selectedOption.equalsIgnoreCase(messages.edit())
				&& isInViewMode() && data.getAssetAccount() != 0
				&& accumltdAccVPanel != null
				&& mainVPanel.getWidgetIndex(accumltdAccVPanel) == -1) {
			showAccumltdAccountForm();
		}
	}

	private void initAssetNumber() {
		this.rpcUtilService
				.getNextFixedAssetNumber(new AccounterAsyncCallback<String>() {

					@Override
					public void onException(AccounterException caught) {
						Accounter.showError(caught.getMessage());
					}

					@Override
					public void onResultSuccess(String result) {
						if (result != null)
							assetNumberTxt.setValue(result);

					}

				});

	}

	private void createControls() {

		listforms = new ArrayList<DynamicForm>();
		// View title name label
		labl = new Label(messages.newAsset());
		labl.setStyleName("Required field");
		labl.setStyleName("label-title");
		// H'panel for view title.
		lablHPanel = new StyledPanel("lablHPanel");

		labl = new Label();
		lablHPanel.add(labl);
		// new item
		newItemTxt = new TextItem(messages.newItem(), "newItemTxt");
		newItemTxt.setRequired(true);
		// asset number
		assetNumberTxt = new TextItem(messages.assetNumber(), "assetNumberTxt");
		assetNumberTxt.setRequired(true);
		// Asset account.
		accountCombo = new FixedAssetAccountCombo(messages.Account());

		accountCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {

					@Override
					public void selectedComboBoxItem(ClientAccount selectItem) {
						accountCombo.getMainWidget().removeStyleName(
								messages.highlightedFormItem());
						if (accountCombo.getSelectedValue() != null) {
							selectedAssetAccount = accountCombo
									.getSelectedValue();
							// if (givenDepRate != 0.0) {
							if (accumltdAccVPanel != null) {
								if (accumulatedDepreciationAccountForm != null) {
									accumulatedDepreciationAccountForm.clear();
								}
								accumltdAccVPanel.clear();
								mainVPanel.remove(accumltdAccVPanel);
							}
							if (selectedAssetAccount
									.getLinkedAccumulatedDepreciationAccount() == 0) {
								showAccumltdAccountForm();
							}
							// NativeEvent ne =
							// Document.get().createChangeEvent();
							// DomEvent.fireNativeEvent(ne,
							// purchaseDateTxt.getMainWidget());
							// DomEvent.fireNativeEvent(ne,
							// purchasePriceTxt.getMainWidget());

						}
					}
				});
		accountCombo.initCombo(getFixedAssetAccounts());
		// purchase date of an asset
		purchaseDateTxt = new DateField(messages.purchaseDate(),
				"purchaseDateTxt");
		purchaseDateTxt.setRequired(true);
		purchaseDateTxt.setDatethanFireEvent(new ClientFinanceDate());
		purchaseDateTxt.addDateValueChangeHandler(new DateValueChangeHandler() {

			@Override
			public void onDateValueChange(ClientFinanceDate date) {
				showAccumultdDepAmountForm(purchaseDateTxt.getEnteredDate());
			}
		});
		// purchase price of an asset
		purchasePriceTxt = new AmountField(messages.purchasePrice(), this,
				getBaseCurrency(), "purchasePriceTxt");
		purchasePriceTxt.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				getDepreciationAmount();

			}
		});
		// description label & text
		descrptionLabel = new Label(messages.description());
		descriptionTxtArea = new TextAreaItem(messages.description(),
				"descriptionTxtArea");
		descriptionTxtArea.setToolTip(messages.writeCommentsForThis(
				this.getAction().getViewName()).replace(messages.comments(),
				messages.description()));
		// new item form
		itmNameForm = new DynamicForm("itmNameForm");
		itmNameForm.add(newItemTxt);

		// This form for asset number & asset account
		itemInfoForm = new DynamicForm("itemInfoForm");
		itemInfoForm.add(assetNumberTxt, accountCombo);
		// This form for purchase date & purchase price
		purchaseInfoForm = new DynamicForm("purchaseInfoForm");
		purchaseInfoForm.add(purchaseDateTxt, purchasePriceTxt);
		// form for description.
		descriptionForm = new DynamicForm("descriptionForm");
		descriptionForm.add(descriptionTxtArea);
		// V'panel for purchase form
		StyledPanel purchaseInfoVPanel = new StyledPanel("purchaseInfoVPanel");
		purchaseInfoVPanel.add(purchaseInfoForm);

		itemHPanel = new StyledPanel("itemHPanel");
		itemHPanel.add(itemInfoForm);

		DynamicForm emptyPanel = new DynamicForm("emptyPanel");
		itemHPanel.add(emptyPanel);
		itemHPanel.add(purchaseInfoVPanel);

		StyledPanel itmInfoVPanel = new StyledPanel("itmInfoVPanel");
		itmInfoVPanel.add(itmNameForm);
		itmInfoVPanel.add(itemHPanel);

		StyledPanel descriptionVPanel = new StyledPanel("descriptionVPanel");
		descriptionVPanel.add(descriptionForm);

		assetType = new TextItem(messages.assetType(), "assetType");

		depreciationRate = new PercentageField(this,
				messages.depreciationRate());
		depreciationRate.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				getDepreciationAmount();

			}
		});

		depreciationMethod = new DepreciationMethodCombo(
				messages.depreciationMethod());
		depreciationMethod.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				getDepreciationAmount();

			}
		});
		depreciationMethod.setView(this);

		depreciationAccount = new DepreciationAccountCombo(
				messages.depreciationAccount());

		assetTypeForm = new DynamicForm("assetTypeForm");
		assetTypeForm.add(assetType, depreciationRate);

		depreciationForm = new DynamicForm("depreciationForm");
		depreciationForm.add(depreciationMethod, depreciationAccount);

		depreciationHPanel = new StyledPanel("depreciationHPanel");
		depreciationHPanel.add(assetTypeForm);
		depreciationHPanel.add(depreciationForm);

		accumulateDeprctVPanel = new StyledPanel("accumulateDeprctVPanel");
		accumltdAccVPanel = new StyledPanel("accumltdAccVPanel");

		mainVPanel = new StyledPanel("mainVPanel");

		mainVPanel.add(lablHPanel);
		mainVPanel.add(itmInfoVPanel);
		mainVPanel.add(descriptionVPanel);
		mainVPanel.add(depreciationHPanel);

		showAccumultdDepAmountForm(purchaseDateTxt.getEnteredDate());

		this.add(mainVPanel);

		// setSize("100%", "100%");

		/* Adding dynamic forms in list */
		listforms.add(itmNameForm);
		listforms.add(itemInfoForm);
		listforms.add(purchaseInfoForm);
		listforms.add(depreciationForm);
		listforms.add(emptyPanel);
		listforms.add(assetTypeForm);
		if (accumulatedDepreciationAccountForm != null) {
			listforms.add(accumulatedDepreciationAccountForm);
		}
	}

	private void setRequiredFields() {
		purchaseDateTxt.setRequired(true);
		purchasePriceTxt.setRequired(true);
		accountCombo.setRequired(true);
		assetType.setRequired(true);
		depreciationAccount.setRequired(true);
		depreciationMethod.setRequired(true);
		depreciationRate.setRequired(true);
		if (!isInViewMode() && accumulatedDepreciationAccount != null) {
			accumulatedDepreciationAccount.setRequired(true);
		}
	}

	private List<ClientAccount> getFixedAssetAccounts() {
		List<ClientAccount> fixedAssetAccountsList = new ArrayList<ClientAccount>();
		for (ClientAccount account : getCompany().getAccounts())
			if (account.getType() == ClientAccount.TYPE_FIXED_ASSET) {
				fixedAssetAccountsList.add(account);
			}
		return fixedAssetAccountsList;
	}

	@Override
	public ClientFixedAsset saveView() {
		ClientFixedAsset saveView = super.saveView();
		if (saveView != null) {
			updateAssetObject();
		}
		return saveView;
	}

	/*
	 * This method adds the AccumulatedDepreciationAccount combo to mainPanel
	 * This combo should be displayed only if the selected AssetAccount doesn't
	 * contains Linked Account. This account will updated in the database with
	 * the selected LinkedAccount after saving the FixedAsset
	 */
	private void showAccumltdAccountForm() {
		isAccumltd = true;
		infoLabl2 = new Label(
				messages.assetAccountYouHaveSelectedNeedsLinkedAccumulatedDepreciationAccount());
		infoLabl2.addStyleName("requiredField");

		// accumulatedDepreciationAccount = new FixedAssetAccountCombo(Accounter
		// .messages().accumulatedDepreciationAccount(
		// Global.get().account()));
		accumulatedDepreciationAccount = new FixedAssetAccountCombo(
				messages.accumulatedDepreciationAccount());
		accumulatedDepreciationAccount
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {

					@Override
					public void selectedComboBoxItem(ClientAccount selectItem) {
						validateAccount();

					}

				});
		/*
		 * An AccumulatedDepreciation account can be an account which is not
		 * used as an Asset Acount in existing Fixed Assets.And AssetAccount and
		 * AccumulatedDep.Account must not be same.
		 */
		List<ClientAccount> accumulatedAccounts = accumulatedDepreciationAccount
				.getAccounts();
		if (selectedAssetAccount == null && isInViewMode()) {
			selectedAssetAccount = getCompany().getAccount(
					data.getAssetAccount());
		}
		// List<ClientFixedAsset> fixedAssets = getCompany().getFixedAssets();
		// for (ClientFixedAsset asset : fixedAssets) {
		// for (ClientAccount accumulatedAccount : accumulatedAccounts) {
		// if (selectedAssetAccount != null) {
		// if (asset.getAssetAccount() != accumulatedAccount.getID()
		// && selectedAssetAccount.getID() != accumulatedAccount
		// .getID())
		// accumulatedDepreciationAccount
		// .setValue(accumulatedAccount);
		// // data.setAccumulatedDepreciationAmount(accumulatedAccount.getID());
		// }
		// }
		// }

		/*
		 * setting the linked Account(AccumulatedDep.Account) for the selected
		 * AssetAccount in edit mode
		 */
		if (isInViewMode() && data.getAssetAccount() != 0) {
			if (getCompany().getAccount(data.getAssetAccount())
					.getLinkedAccumulatedDepreciationAccount() != 0) {
				long accumulatedAccountID = getCompany().getAccount(
						data.getAssetAccount())
						.getLinkedAccumulatedDepreciationAccount();
				ClientAccount accumultdAcc = getCompany().getAccount(
						accumulatedAccountID);
				accumulatedDepreciationAccount
						.setSelected(accumultdAcc != null ? accumultdAcc
								.getName() : "");

			} else
				accumulatedDepreciationAccount.setSelected("");

		}
		if (isInViewMode()) {
			accumulatedDepreciationAccount.setComboItem(getCompany()
					.getAccount(data.getAccumulatedDepreciationAccount()));

		}
		accumulatedDepreciationAccountForm = new DynamicForm(
				"accumulatedDepreciationAccountForm");
		accumulatedDepreciationAccountForm.add(accumulatedDepreciationAccount);
		accumltdAccVPanel.add(infoLabl2);
		accumltdAccVPanel.add(accumulatedDepreciationAccountForm);
		mainVPanel.remove(accumltdAccVPanel);
		mainVPanel.add(accumltdAccVPanel);

	}

	protected boolean validateAccount() {
		ClientAccount assetAccount = accountCombo.getSelectedValue();
		ClientAccount selectItem = accumulatedDepreciationAccount
				.getSelectedValue();
		if (assetAccount != null && selectItem != null
				&& assetAccount.getID() == selectItem.getID()) {
			return true;
		}
		return false;

	}

	/*
	 * The AccumulatedDepreciationAmount field need to be added when given date
	 * is before the depreciation startdate
	 */

	private void showAccumultdDepAmountForm(ClientFinanceDate enteredDate) {
		if (getDepreciationStartDate() != null) {
			if (!enteredDate.equals(getDepreciationStartDate())
					&& enteredDate.before(getDepreciationStartDate())) {
				isAssetAccumulated = true;
				infoLabel1 = new Label(
						messages.purchaseDatePriorToFixedAssetsStartDate()
								+ DateUtills
										.getDateAsString(getDepreciationStartDate()));
				infoLabel1.setStyleName("requiredField");
				accmulatdDepreciationTxt = new AmountField(
						messages.accumulatedDepreciationTo() + " "
								+ DateUtills.getDateAsString(enteredDate),
						this, getBaseCurrency(), "accmulatdDepreciationTxt");
				accmulatdDepreciationTxt
						.setValue(DataUtils
								.getAmountAsStringInPrimaryCurrency(getDepreciationAmount()));

				if (isInViewMode())
					accmulatdDepreciationTxt.setValue(DataUtils
							.getAmountAsStringInPrimaryCurrency(data
									.getAccumulatedDepreciationAmount()));

				acumulatedDeprcForm = new DynamicForm("acumulatedDeprcForm");
				listforms.add(acumulatedDeprcForm);
				acumulatedDeprcForm.add(accmulatdDepreciationTxt);
				accumulateDeprctVPanel.clear();
				accumulateDeprctVPanel.add(infoLabel1);
				accumulateDeprctVPanel.add(acumulatedDeprcForm);
				accumulateDeprctVPanel.removeFromParent();
				mainVPanel.remove(accumulateDeprctVPanel);
				mainVPanel.add(accumulateDeprctVPanel);
			} else if (enteredDate.after(getDepreciationStartDate())
					|| enteredDate.equals(getDepreciationStartDate())) {
				isAssetAccumulated = false;
				accumulateDeprctVPanel.clear();
				accumulateDeprctVPanel.removeFromParent();
				mainVPanel.remove(accumulateDeprctVPanel);
				// Need to update the conversion balances.
				// See http://help.xero.com/uk/#Settings_ConversionDate for
				// reference
			}
		}
	}

	public double getDepreciationAmount() {
		try {

			if (isInViewMode() && accmulatdDepreciationTxt.isEnabled()) {
				accmulatdDepreciationTxt.setAmount(data
						.getAccumulatedDepreciationAmount());
			} else {
				ClientFinanceDate purchaseDate = purchaseDateTxt
						.getEnteredDate();
				int depMethod = depreciationMethod.getSelectedValue() != 0 ? depreciationMethod
						.getSelectedValue() : 0;
				double depRate = depreciationRate.getPercentage();
				double purchasePrice = purchasePriceTxt.getAmount();
				ClientFinanceDate depStartDate = new ClientFinanceDate(
						getCompany().getPreferences()
								.getDepreciationStartDate());
				depAmount = 0.0;

				/*
				 * Make an rpc call to get the calculatedDepreciation amount if
				 * the following condtions are satisfy
				 */
				if (depMethod != 0 && !DecimalUtil.isEquals(depRate, 0)
						&& !DecimalUtil.isEquals(purchasePrice, 0)
						&& purchaseDate.before(depStartDate)) {
					if ((purchaseDate.getDate() == 0)) {
						purchaseDate = new ClientFinanceDate();
					}
					Accounter.createHomeService()
							.getAccumulatedDepreciationAmount(depMethod,
									depRate, purchasePrice,
									purchaseDate.getDate(),
									depStartDate.getDate(),
									new AccounterAsyncCallback<Double>() {

										@Override
										public void onException(
												AccounterException caught) {

										}

										@Override
										public void onResultSuccess(
												Double result) {
											depAmount = result;
											if (accmulatdDepreciationTxt != null) {
												accmulatdDepreciationTxt
														.setAmount(depAmount);
											}

										}
									});
				}
			}

		} catch (Exception e) {

		}
		return depAmount;
	}

	public ClientFinanceDate getDepreciationStartDate() {
		return new ClientFinanceDate(getCompany().getPreferences()
				.getDepreciationStartDate());
	}

	@Override
	public void saveAndUpdateView() {
		updateAssetObject();
		setStatus();
		saveOrUpdate(data);
	}

	private void setStatus() {
		/* while registering the data from viewmode or updating a registeritem */
		if ((false && data != null)
				|| (data != null && data.getStatus() == ClientFixedAsset.STATUS_REGISTERED)) {
			data.setStatus(ClientFixedAsset.STATUS_REGISTERED);
		} else if (false) {
			/* while creating a registeritem */
			data.setStatus(ClientFixedAsset.STATUS_REGISTERED);
		} else {
			/* while updating/creating a pending item */
			data.setStatus(ClientFixedAsset.STATUS_PENDING);
		}
	}

	@Override
	public void saveSuccess(IAccounterCore result) {
		ClientFixedAsset createdAsset = (ClientFixedAsset) result;
		if (createdAsset.getID() != 0) {
			// if (fixedAsset == null)
			// Accounter.showInformation(FinanceApplication
			// .constants().newAssetwithName()
			// + ((ClientFixedAsset) result).getName()
			// + FinanceApplication.constants()
			// .isCreated());
			// else
			// Accounter.showInformation(((ClientFixedAsset) result).getName()
			// + FinanceApplication.constants()
			// .IsUpdatedSuccessfully());
			super.saveSuccess(result);
		} else
			saveFailed(new AccounterException(messages.failed()));

	}

	@Override
	public void saveFailed(AccounterException exception) {
		super.saveFailed(exception);
		// if (!isEdit)
		// BaseView.errordata.setHTML(FinanceApplication
		// .constants().duplicationOfAssets());
		// addError(this, messages.duplicationOfAssets());
		// BaseView.errordata.setHTML(FinanceApplication
		// else
		// .constants().assetApdationFailed());
		// addError(this, messages.accountUpdationFailed());
		// BaseView.commentPanel.setVisible(true);
		// this.errorOccured = true;
		AccounterException accounterException = exception;
		int errorCode = accounterException.getErrorCode();
		String errorString = AccounterExceptions.getErrorString(errorCode);
		Accounter.showError(errorString);
	}

	private void updateAssetObject() {
		data.setName(newItemTxt.getValue() != null ? newItemTxt.getValue()
				.toString() : "");
		data.setAssetNumber(assetNumberTxt.getValue() != null ? assetNumberTxt
				.getValue().toString() : "");
		if (selectedAssetAccount != null) {
			selectedAssetAccount = getCompany().getAccount(
					selectedAssetAccount.getID());
			if (selectedAssetAccount.getLinkedAccumulatedDepreciationAccount() == 0) {
				if (accumulatedDepreciationAccount != null
						&& accumulatedDepreciationAccount.getSelectedValue() != null
						&& selectedAssetAccount.getID() != accumulatedDepreciationAccount
								.getSelectedValue().getID()) {
					data.setAccumulatedDepreciationAccount(accumulatedDepreciationAccount
							.getSelectedValue() != null ? accumulatedDepreciationAccount
							.getSelectedValue().getID() : 0);
					selectedAssetAccount
							.setLinkedAccumulatedDepreciationAccount(accumulatedDepreciationAccount
									.getSelectedValue() != null ? accumulatedDepreciationAccount
									.getSelectedValue().getID() : 0);
				}
			}
			data.setAssetAccount(selectedAssetAccount.getID());

		}
		data.setPurchaseDate(purchaseDateTxt.getEnteredDate().getDate());
		data.setPurchasePrice(purchasePriceTxt.getAmount());
		data.setDescription(descriptionTxtArea.getValue() != null ? descriptionTxtArea
				.getValue().toString() : "");

		data.setAssetType(assetType.getValue() != null ? assetType.getValue()
				.toString() : "");
		data.setDepreciationRate(depreciationRate.getPercentage() != null ? depreciationRate
				.getPercentage() : 0.0);
		data.setDepreciationMethod(depreciationMethod.getSelectedValue());
		data.setDepreciationExpenseAccount(depreciationAccount
				.getSelectedValue() != null ? depreciationAccount
				.getSelectedValue().getID() : 0);
		data.setBookValue(purchasePriceTxt.getAmount());

		// if (isAssetAccumulated || fixedAsset != null
		// && accmulatdDepreciationTxt != null) {
		// data.setAccumulatedDepreciationAmount(accmulatdDepreciationTxt
		// .getAmount());
		// }

		if (isAssetAccumulated)
			data.setAccumulatedDepreciationAmount(accmulatdDepreciationTxt
					.getAmount());
		else
			data.setAccumulatedDepreciationAmount(0.0);

	}

	/*
	 * --------- I think TODO No Need
	 */

	@Override
	protected void onLoad() {
		// int assetNumberWidth = itemInfoForm.getCellFormatter().getElement(0,
		// 0)
		// .getOffsetWidth();
		// adjustFormWidths(assetNumberWidth);
		// super.onLoad();
	}

	@Override
	protected void onAttach() {
		// int assetNumberWidth = itemInfoForm.getCellFormatter().getElement(0,
		// 0).getOffsetWidth();
		// adjustFormWidths(assetNumberWidth);
		super.onAttach();
	};

	/*---------------------------up to this ------------*/

	/*
	 * If "Save" button clicked, only itemname & assetnumber fields are
	 * mandatory. If "Register" button clicked,all the fileds are mandatory
	 */
	@Override
	public ValidationResult validate() {
		ValidationResult result = new ValidationResult();
		result.add(itmNameForm.validate());
		if (data != null
				&& data.getStatus() == ClientFixedAsset.STATUS_REGISTERED) {
			result.add(itemInfoForm.validate());
			// result.add(itmNameForm.validate());
			result.add(purchaseInfoForm.validate());
			result.add(validatePurchaseAmount());
			result.add(assetTypeForm.validate());
			result.add(validateDepreciationRate());
			result.add(depreciationForm.validate());
			result.add(validateAccountsForSame());
			if (isInViewMode() && accumltdAccVPanel.getWidgetCount() != 0) {
				result.add(accumulatedDepreciationAccountForm.validate());
			} else {
				if (accumulatedDepreciationAccountForm != null
						&& accumltdAccVPanel.getWidgetCount() != 0) {
					result.add(accumulatedDepreciationAccountForm.validate());
				}
			}

		}
		// if (data != null
		// && data.getStatus() == ClientFixedAsset.STATUS_REGISTERED) {
		// if (result.haveErrors()) {
		// data.setStatus(ClientFixedAsset.STATUS_PENDING);
		// }
		// }
		if (purchaseDateTxt.getEnteredDate().isEmpty()) {
			result.addError(purchaseDateTxt,
					messages.pleaseSelect(messages.purchaseDate()));
		}
		if (accountCombo != null && accumulatedDepreciationAccount != null) {
			if (validateAccount()) {
				result.addError(accountCombo,
				/*
				 * messages .accandaccumulatedDepreciationAccShouldnotbesame(
				 * Global.get().account())
				 */messages.accandaccumulatedDepreciationAccShouldnotbesame());
			}
		}
		if (data != null
				&& data.getStatus() == ClientFixedAsset.STATUS_REGISTERED) {
			if (result.haveErrors() && registerButton.isAttached()) {
				data.setStatus(ClientFixedAsset.STATUS_PENDING);
			}
		}
		return result;
	}

	private ValidationResult validateAccountsForSame() {
		ValidationResult result = new ValidationResult();
		if (accountCombo != null) {
			ClientAccount assetAcc = accountCombo.getSelectedValue();
			if (assetAcc != null) {
				if (assetAcc.getLinkedAccumulatedDepreciationAccount() != 0
						&& assetAcc.getID() == assetAcc
								.getLinkedAccumulatedDepreciationAccount()) {
					result.addError(accumulatedDepreciationAccount, messages
							.accandaccumulatedDepreciationAccShouldnotbesame());
				}
			}

		}
		return result;
	}

	// private ValidationResult validateAccumulatedDepreciationAccount() {
	// ValidationResult result = new ValidationResult();
	// if (accumulatedDepreciationAccount != null) {
	// if (accumulatedDepreciationAccount.getSelectedValue() == null) {
	// result.addError(accumulatedDepreciationAccount,
	// messages.pleaseChooseAnAccount());
	// }
	// }
	// return result;
	// }

	private ValidationResult validateDepreciationRate() {
		ValidationResult result = new ValidationResult();
		if (depreciationRate != null) {
			double rateOfDepreciation = depreciationRate.getPercentage();
			if (rateOfDepreciation <= 0) {
				depreciationRate.highlight();
				result.addError(depreciationRate,
						messages.pleaseEnter(messages.depreciationRate()));
			}
		}
		return result;
	}

	private ValidationResult validatePurchaseAmount() {
		ValidationResult result = new ValidationResult();
		if (purchasePriceTxt != null) {
			Double amount = purchasePriceTxt.getAmount();
			if (amount <= 0) {
				purchasePriceTxt.highlight();
				result.addError(
						purchasePriceTxt,
						messages.pleaseEnter(messages.purchase()
								+ messages.amount()));
			}
		}
		return result;
	}

	// private boolean validateName(String name) {
	// if (!(!isInViewMode() && getCompany().getFixedAssetByName(name) != null ?
	// true
	// : false)
	// || !(isInViewMode() && !(data.getName().equalsIgnoreCase(name) ? true
	// : getCompany().getFixedAssetByName(name) != null ? false
	// : true))) {
	// isVerified = true;
	// return false;
	// }
	// return true;
	//
	// }

	@Override
	public List<DynamicForm> getForms() {

		return listforms;
	}

	/**
	 * call this method to set focus in View
	 */
	@Override
	public void setFocus() {
		this.newItemTxt.setFocus();
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

		AccounterAsyncCallback<Boolean> editCallBack = new AccounterAsyncCallback<Boolean>() {

			@Override
			public void onException(AccounterException caught) {
				int errorCode = caught.getErrorCode();
				Accounter.showError(AccounterExceptions
						.getErrorString(errorCode));
			}

			@Override
			public void onResultSuccess(Boolean result) {
				if (result.booleanValue()) {
					disableFields(isInViewMode());
				}
			}

		};

		this.rpcDoSerivce.canEdit(AccounterCoreType.FIXEDASSET, data.getID(),
				editCallBack);
	}

	@Override
	public boolean canEdit() {
		return false;
	}

	@Override
	public void print() {
		// TODO Auto-generated method stub

	}

	@Override
	public void printPreview() {
		// NOTHING TO DO.
	}

	@Override
	protected String getViewTitle() {
		return messages.newFixedAsset();
	}

	@Override
	protected void createButtons() {
		this.saveAndCloseButton = new SaveAndCloseButton(this);
		addButton(this.saveAndCloseButton);

		registerButton = new ImageButton(messages.register(), Accounter
				.getFinanceImages().register(), "accept");
		registerButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				registerAsset();
			}
		});
		addButton(registerButton);
		this.cancelButton = new CancelButton(this);
		cancelButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
			}
		});
		addButton(this.cancelButton);

		/*
		 * In edit mode,if the asset is other than the pending item,"Register"
		 * button should be hide
		 */
		if (data != null && isInViewMode()) {
			if (data.getStatus() == ClientFixedAsset.STATUS_REGISTERED
					|| data.getStatus() == ClientFixedAsset.STATUS_REGISTERED) {
				registerButton.setVisible(false);
			} else if (data.getStatus() == ClientFixedAsset.STATUS_SOLD_OR_DISPOSED) {
				registerButton.setVisible(false);
				saveAndCloseButton.setVisible(false);
			}
		}
	}

	protected void registerAsset() {
		data.setStatus(ClientFixedAsset.STATUS_REGISTERED);
		setRequiredFields();
		updateAssetObject();
		setStatus();
		this.onSave(false);
	}

	@Override
	protected boolean canDelete() {
		return true;
	}

}
