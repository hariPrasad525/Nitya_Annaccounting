package com.vimukti.accounter.web.client.ui.vat;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.Label;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientTAXAgency;
import com.vimukti.accounter.web.client.core.ClientTAXItem;
import com.vimukti.accounter.web.client.core.ClientVATReturnBox;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.TAXAgencyCombo;
import com.vimukti.accounter.web.client.ui.combo.VatReturnBoxCombo;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.core.PercentageField;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.util.Countries;

/**
 * 
 * @author Raj Vimal
 * 
 */

public class NewVATItemView extends BaseView<ClientTAXItem> {

	private TextItem vatItemNameText;
	private TextAreaItem descriptionText;
	private AmountField vatRateText;
	private PercentageField vatRateTextPerT;
	private TAXAgencyCombo vatAgencyCombo;
	private CheckboxItem statusCheck;
	private VatReturnBoxCombo vatReturnBoxCombo;
	protected ClientVATReturnBox selectedBox;
	protected ClientTAXAgency selectedVATAgency;
	// private CheckboxItem isPercentatateAmtCheck;
	private String vatName;

	private ArrayList<DynamicForm> listforms;
	final DynamicForm form1 = UIUtils.form(messages.type());

	public NewVATItemView() {
		super();
		this.getElement().setId("NewVATItemView");
	}

	private void createControls() {
		Label infolabel1 = null;

		infolabel1 = new Label(messages.taxItem());

		infolabel1.setStyleName("label-title");
		// infolabel1.setHeight("50px");

		listforms = new ArrayList<DynamicForm>();

		vatItemNameText = new TextItem(messages.taxItemName(),
				"vatItemNameText");
		// vatItemNameText.setHelpInformation(true);
		// vatItemNameText.setWidth(80);
		vatItemNameText.setRequired(true);
		vatItemNameText.setEnabled(!isInViewMode());

		descriptionText = new TextAreaItem(messages.description(),
				"descriptionText");
		// descriptionText.setHelpInformation(true);
		// descriptionText.setWidth(80);
		descriptionText.setDisabled(isInViewMode());

		vatRateText = new AmountField(messages.taxAmount(), this,
				getBaseCurrency(), "vatRateText");
		// vatRateText.setWidth(80);
		vatRateText.setRequired(true);
		vatRateText.setEnabled(!isInViewMode());

		vatRateTextPerT = new PercentageField(this, messages.taxRateP());
		// vatRateTextPerT.setHelpInformation(true);
		// vatRateTextPerT.setWidth(80);
		vatRateTextPerT.setRequired(true);
		vatRateTextPerT.setEnabled(!isInViewMode());

		vatAgencyCombo = new TAXAgencyCombo(messages.taxAgency());
		// vatAgencyCombo.setHelpInformation(true);
		vatAgencyCombo.setEnabled(!isInViewMode());
		vatAgencyCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientTAXAgency>() {

					@Override
					public void selectedComboBoxItem(ClientTAXAgency selectItem) {
						selectedVATAgency = (ClientTAXAgency) selectItem;
						loadVATReturnBoxes(selectedVATAgency);
					}
				});
		// vatAgencyCombo.setWidth(80);
		vatAgencyCombo.setRequired(true);

		// Label label = new Label(
		// "Assign This item to box of VAT agency's VAT Return");

		vatReturnBoxCombo = new VatReturnBoxCombo(messages.vatReturnBox());
		// vatReturnBoxCombo.setHelpInformation(true);
		vatReturnBoxCombo.setRequired(true);
		vatReturnBoxCombo.setEnabled(!isInViewMode());
		vatReturnBoxCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientVATReturnBox>() {

					@Override
					public void selectedComboBoxItem(
							ClientVATReturnBox selectItem) {
						selectedBox = (ClientVATReturnBox) selectItem;
					}

				});
		// vatReturnBoxCombo.setWidth(80);

		statusCheck = new CheckboxItem(messages.itemIsActive(), "status");
		statusCheck.setValue(true);
		statusCheck.setEnabled(!isInViewMode());

		// form1.setWidth("80%");
		form1.addStyleName("new_vat_item fields-panel");
		// form1.setIsGroup(true);

		// form1.getCellFormatter().addStyleName(1, 0, "memoFormAlign");

		form1.add(vatItemNameText, descriptionText, vatRateTextPerT,
				vatAgencyCombo);
		if (getCountryPreferences().isVatAvailable()
				&& getCompany().getCountry().equals(Countries.UNITED_KINGDOM)) {
			form1.add(vatReturnBoxCombo);
		}
		form1.add(statusCheck);

		StyledPanel mainPanel = new StyledPanel("mainPanel");

		// mainPanel.setSpacing(25);
		// mainPanel.setWidth("100%");
		mainPanel.add(infolabel1);
		mainPanel.add(form1);

		this.add(mainPanel);

		/* Adding dynamic forms in list */
		listforms.add(form1);

	}

	protected void loadVATReturnBoxes(ClientTAXAgency vatAgency) {
		List<ClientVATReturnBox> vatBoxes = getCompany().getVatReturnBoxes();
		List<ClientVATReturnBox> vatBoxes2 = new ArrayList<ClientVATReturnBox>();
		for (ClientVATReturnBox vatBox : vatBoxes) {
			if (vatAgency.getVATReturn() == vatBox.getVatReturnType())
				vatBoxes2.add(vatBox);
		}

		vatReturnBoxCombo.initCombo(vatBoxes2);
	}

	@Override
	public void init() {
		super.init();
		createControls();
		// setSize("100%", "100%");
	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = new ValidationResult();

		/*
		 * result.add(DynamicForm.validate(this.getForms().toArray( new
		 * DynamicForm[getForms().size()])));
		 */
		// already exists?
		// form validation

		String name = vatItemNameText.getValue().toString() != null ? vatItemNameText
				.getValue().toString() : "";

		ClientTAXItem taxItemByName = getCompany().getTaxItemByName(name);

		// if ((!isEdit && taxItemByName != null)){
		// result.addError(vatItemNameText, messages
		// .alreadyExist());
		// }
		// if (!((!isEdit && taxItemByName != null))
		// || !(isEdit ? (data.getName().equalsIgnoreCase(name) ? true
		// : (taxItemByName != null
		// || taxItemByName.getID() == this.getData()
		// .getID() ? false : true)) : true)) {
		// result.addError(vatItemNameText, messages
		// .alreadyExist());
		// }

		if (isInViewMode()) {
			if (taxItemByName != null) {
				result.addError(vatItemNameText, messages.alreadyExist());
				return result;
			}
		}
		result.add(DynamicForm.validate(form1));
		return result;
	}

	public ClientTAXItem saveView() {
		ClientTAXItem saveView = super.saveView();
		if (saveView != null) {
			updateObject();
		}
		return saveView;
	}

	@Override
	public void saveAndUpdateView() {
		updateObject();
		saveOrUpdate(data);
	}

	@Override
	public void saveSuccess(IAccounterCore result) {
		if (result != null) {

			// if (takenVATItem == null) {
			// Accounter.showInformation(FinanceApplication.constants()
			// .newVATItemCreated());
			//
			// } else {
			// Accounter.showInformation(FinanceApplication.constants()
			// .VATItemUpdatedSuccessfully());
			//
			// }
			super.saveSuccess(result);

		} else {
			saveFailed(new AccounterException());
		}
	}

	@Override
	public void saveFailed(AccounterException exception) {
		super.saveFailed(exception);
		String exceptionMessage = exception.getMessage();
		// addError(this,
		// "A Vat Name already exists with this name");
		// BaseView.errordata.setHTML(exception.getMessage());
		// BaseView.commentPanel.setVisible(true);
		// this.errorOccured = true;
		// addError(this, exception.getMessage());
		AccounterException accounterException = (AccounterException) exception;
		String errorString = AccounterExceptions
				.getErrorString(accounterException);
		Accounter.showError(errorString);
		updateObject();
		if (exceptionMessage != null && exceptionMessage.contains("name")) {
			data.setName(vatName);
		}

	}

	private void updateObject() {

		data.setName(vatItemNameText.getValue().toString() != null ? vatItemNameText
				.getValue().toString() : "");
		data.setDescription(descriptionText.getValue().toString() != null ? descriptionText
				.getValue().toString() : "");
		data.setVatReturnBox(selectedBox != null ? selectedBox.getID()
				: data != null ? data.getVatReturnBox() : null);
		data.setTaxAgency(selectedVATAgency != null ? selectedVATAgency.getID()
				: data != null ? data.getTaxAgency() : null);
		data.setActive(statusCheck.getValue() != null ? (Boolean) statusCheck
				.getValue() : Boolean.FALSE);

		data.setTaxRate(vatRateTextPerT.getPercentage());
		data.setPercentage(true);

	}

	public List<DynamicForm> getForms() {

		return listforms;
	}

	/**
	 * call this method to set focus in View
	 */
	@Override
	public void setFocus() {
		this.vatItemNameText.setFocus();
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

	@Override
	public void onEdit() {

		AccounterAsyncCallback<Boolean> editCallBack = new AccounterAsyncCallback<Boolean>() {

			@Override
			public void onException(AccounterException caught) {
				int errorCode = ((AccounterException) caught).getErrorCode();
				Accounter.showError(AccounterExceptions
						.getErrorString(errorCode));

			}

			@Override
			public void onResultSuccess(Boolean result) {
				if (result)
					enableFormItems();
			}

		};

		this.rpcDoSerivce.canEdit(AccounterCoreType.TAXITEM, data.getID(),
				editCallBack);
	}

	protected void enableFormItems() {
		setMode(EditMode.EDIT);
		vatItemNameText.setEnabled(!isInViewMode());
		descriptionText.setDisabled(isInViewMode());
		vatRateTextPerT.setEnabled(!isInViewMode());
		vatReturnBoxCombo.setEnabled(!isInViewMode());
		vatAgencyCombo.setEnabled(!isInViewMode());
		statusCheck.setEnabled(!isInViewMode());
		// isPercentatateAmtCheck.setDisabled(isInViewMode());
		super.onEdit();

	}

	@Override
	public void print() {

	}

	@Override
	public void printPreview() {
		// NOTHING TO DO.
	}

	@Override
	protected String getViewTitle() {
		return messages.newTaxItem();
	}

	@Override
	public void initData() {
		if (data == null) {
			setData(new ClientTAXItem());
		}

		vatItemNameText.setValue(data.getName() != null ? data.getName() : "");
		vatName = data.getName() != null ? data.getName() : "";
		descriptionText.setValue(data.getDescription() != null ? data
				.getDescription() : "");
		// isPercentatateAmtCheck.setValue(data.isPercentage());

		if (data.getTaxAgency() != 0) {
			selectedVATAgency = getCompany().getTaxAgency(data.getTaxAgency());
			vatAgencyCombo.setComboItem(selectedVATAgency);
			loadVATReturnBoxes(selectedVATAgency);
		}

		if (data.getVatReturnBox() != 0) {
			selectedBox = getCompany().getVatReturnBox(data.getVatReturnBox());
			vatReturnBoxCombo.setComboItem(selectedBox);
		}
		if (data.getID() != 0)
			statusCheck.setValue(data.isActive());
		else
			statusCheck.setValue(true);

		vatRateTextPerT.setPercentage(data.getTaxRate());

		vatRateText.setAmount(data.getTaxRate());

	}

	@Override
	protected boolean canVoid() {
		return false;
	}
}
