package com.nitya.accounter.web.client.ui.vat;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Label;
import com.nitya.accounter.web.client.AccounterAsyncCallback;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.ClientTAXAgency;
import com.nitya.accounter.web.client.core.ClientTAXCode;
import com.nitya.accounter.web.client.core.ClientTAXGroup;
import com.nitya.accounter.web.client.core.ClientTAXItem;
import com.nitya.accounter.web.client.core.ClientTAXItemGroup;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.core.ValidationResult;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.exception.AccounterExceptions;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.StyledPanel;
import com.nitya.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.nitya.accounter.web.client.ui.combo.VATItemCombo;
import com.nitya.accounter.web.client.ui.core.BaseView;
import com.nitya.accounter.web.client.ui.core.EditMode;
import com.nitya.accounter.web.client.ui.forms.CheckboxItem;
import com.nitya.accounter.web.client.ui.forms.DynamicForm;
import com.nitya.accounter.web.client.ui.forms.RadioGroupItem;
import com.nitya.accounter.web.client.ui.forms.TextAreaItem;
import com.nitya.accounter.web.client.ui.forms.TextItem;

/**
 * @author Murali.A
 * 
 */
public class NewTAXCodeView extends BaseView<ClientTAXCode> {

	private TextItem vatCodeTxt;
	private TextAreaItem description;
	private CheckboxItem isActive;
	private RadioGroupItem taxableGroupRadio;
	private VATItemCombo vatItemComboForPurchases;
	private VATItemCombo vatItemComboForSales;
	private DynamicForm vatNameForm;
	public long selectedVATPurchaseAcc;
	public long selectedVATSAlesAcc;
	protected boolean isComboDisabled = false;
	private String vatCode;
	private ClientTAXCode vat;
	private ArrayList<DynamicForm> listforms;
	private String taxCodeName;

	public NewTAXCodeView() {

		super();
		this.getElement().setId("NewTAXCodeView");
	}

	@Override
	public void init() {
		super.init();
		createControls();
		// setSize("100%", "");

	}

	@Override
	public void initData() {
		if (getData() != null) {
			vat = (ClientTAXCode) getData();
			vatCodeTxt.setValue(vat.getName() != null ? vat.getName() : "");
			vatCode = vat.getName() != null ? vat.getName() : "";
			description.setValue(vat.getDescription() != null ? vat
					.getDescription() : "");
			isActive.setValue(vat.isActive());
			taxableGroupRadio.setValue(vat.isTaxable() ? messages.taxable()
					: messages.taxExempt());

			if (getCompany().getTAXItemGroup(vat.getTAXItemGrpForPurchases()) != null) {
				selectedVATPurchaseAcc = vat.getTAXItemGrpForPurchases();
				vatItemComboForPurchases.setComboItem(Accounter.getCompany()
						.getTAXItemGroup(vat.getTAXItemGrpForPurchases()));
			} else
				vatItemComboForPurchases.setSelected("");

			if (getCompany().getTAXItemGroup(vat.getTAXItemGrpForSales()) != null) {
				selectedVATSAlesAcc = vat.getTAXItemGrpForSales();
				vatItemComboForSales.setComboItem(Accounter.getCompany()
						.getTAXItemGroup(vat.getTAXItemGrpForSales()));
			} else
				vatItemComboForSales.setSelected("");

			if (!vat.isTaxable()) {
				vatItemComboForPurchases.setValue("");
				vatItemComboForSales.setValue("");
				vatItemComboForPurchases.setEnabled(!isInViewMode());
				vatItemComboForSales.setEnabled(!isInViewMode());
			}
			// vatCodeTxt.setValue(data.getName() != null ? data.getName() :
			// "");
			// description.setValue(data.getDescription() != null ? data
			// .getDescription() : "");
			// isActive.setValue(data.isActive());
			// taxableGroupRadio.setValue(data.isTaxable() ? messages.taxable()
			// : messages.taxExempt());
			// vatItemComboForPurchases
			// .setValue(data.getTAXItemGrpForPurchases() != 0 ? Accounter
			// .getCompany()
			// .getTAXItemGroup(data.getTAXItemGrpForPurchases())
			// .getName() : "");
			// vatItemComboForSales
			// .setValue(data.getTAXItemGrpForSales() != 0 ? Accounter
			// .getCompany()
			// .getTAXItemGroup(data.getTAXItemGrpForSales())
			// .getName() : "");
		} else {
			setData(new ClientTAXCode());
		}

	}

	private void createControls() {
		Label infoLabel = new Label(messages.taxCode());
		infoLabel.setStyleName("label-title");
		// infoLabel.setHeight("35px");
		listforms = new ArrayList<DynamicForm>();

		vatCodeTxt = new TextItem(messages.taxCode(), "vatCodeTxt");
		vatCodeTxt.setValue(taxCodeName);
		vatCodeTxt.setRequired(true);
		vatCodeTxt.setEnabled(!isInViewMode());
		description = new TextAreaItem(messages.description(), "description");
		description.setToolTip(messages.writeCommentsForThis(
				this.getAction().getViewName()).replace(messages.comments(),
				messages.description()));
		description.setDisabled(isInViewMode());

		isActive = new CheckboxItem(messages.isActive(), "status");
		isActive.setValue((Boolean) true);
		isActive.setEnabled(!isInViewMode());

		taxableGroupRadio = new RadioGroupItem(messages.tax());
		taxableGroupRadio.setValues(getClickHandler(), messages.taxable(),
				messages.taxExempt());
		taxableGroupRadio.setDefaultValue(messages.taxable());
		taxableGroupRadio.setEnabled(!isInViewMode());

		vatItemComboForPurchases = new VATItemCombo(
				messages.taxItemForPurchases());
		vatItemComboForPurchases.initCombo(vatItemComboForPurchases
				.getPurchaseWithPrcntVATItems());
		vatItemComboForPurchases.setEnabled(!isInViewMode());
		vatItemComboForPurchases
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientTAXItemGroup>() {
					@Override
					public void selectedComboBoxItem(
							ClientTAXItemGroup selectItem) {
						if (selectItem != null) {
							selectedVATPurchaseAcc = selectItem.getID();
							vatItemComboForSales.syncronize(selectItem);
						}
					}
				});

		vatItemComboForSales = new VATItemCombo(messages.taxItemForSales());
		vatItemComboForSales.initCombo(vatItemComboForSales
				.getSalesWithPrcntVATItems());
		vatItemComboForSales.setEnabled(!isInViewMode());
		vatItemComboForSales
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientTAXItemGroup>() {

					@Override
					public void selectedComboBoxItem(
							ClientTAXItemGroup selectItem) {
						if (selectItem != null) {
							selectedVATSAlesAcc = selectItem.getID();
							vatItemComboForPurchases.syncronize(selectItem);
						}
					}
				});
		vatNameForm = new DynamicForm("fields-panel");
		// vatNameForm.setWidth("80%");
		// vatNameForm.getCellFormatter().setWidth(0, 0, "225px");
		// vatNameForm.getCellFormatter().addStyleName(1, 0, "memoFormAlign");
		// vatNameForm.getCellFormatter().addStyleName(2, 0, "memoFormAlign");
		if (getPreferences().isTrackPaidTax()) {
			vatNameForm.add(vatCodeTxt, description, taxableGroupRadio,
					isActive, vatItemComboForSales, vatItemComboForPurchases);
		} else {
			vatNameForm.add(vatCodeTxt, description, taxableGroupRadio,
					isActive, vatItemComboForSales);
		}

		StyledPanel mainVPanel = new StyledPanel("mainVPanel");
		mainVPanel.add(infoLabel);
		mainVPanel.add(vatNameForm);

		// if (UIUtils.isMSIEBrowser()) {
		// vatNameForm.getCellFormatter().setWidth(0, 1, "270px");
		// vatNameForm.setWidth("50%");
		// }
		this.add(mainVPanel);

		/* Adding dynamic forms in list */
		listforms.add(vatNameForm);

	}

	private ClickHandler getClickHandler() {
		ClickHandler hanler = new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				vatCodeTxt.getMainWidget().removeStyleName(
						"highlightedFormItem");
				vatItemComboForPurchases.getMainWidget().removeStyleName(
						"highlightedFormItem");
				vatItemComboForSales.getMainWidget().removeStyleName(
						"highlightedFormItem");
				String taxbl = taxableGroupRadio.getValue().toString();
				taxableGroupRadio.setValue(taxbl);
				if (taxbl.equalsIgnoreCase(messages.taxable())) {
					isComboDisabled = false;
					vatItemComboForPurchases.setEnabled(true);
					vatItemComboForSales.setEnabled(true);
					if (vat != null) {
						vatItemComboForSales.setSelected(Accounter.getCompany()
								.getTaxItem(vat.getTAXItemGrpForSales())
								.getName());
						vatItemComboForPurchases.setSelected(Accounter
								.getCompany()
								.getTaxItem(vat.getTAXItemGrpForPurchases())
								.getName());
					}
				} else {
					isComboDisabled = true;
					vatItemComboForSales.setValue("");
					vatItemComboForPurchases.setValue("");
					vatItemComboForSales.setEnabled(false);
					vatItemComboForPurchases.setEnabled(false);
				}
			}
		};
		return hanler;
	}

	@Override
	public ClientTAXCode saveView() {
		ClientTAXCode saveView = super.saveView();
		if (saveView != null) {
			updateVATCode();
		}
		return saveView;
	}

	@Override
	public void saveAndUpdateView() {

		updateVATCode();

		saveOrUpdate(getData());

	}

	@Override
	public void saveFailed(AccounterException exception) {
		super.saveFailed(exception);
		String exceptionMessage = exception.getMessage();
		// addError(this, exception.getMessage());
		AccounterException accounterException = (AccounterException) exception;
		int errorCode = accounterException.getErrorCode();
		String errorString = AccounterExceptions.getErrorString(errorCode);
		Accounter.showError(errorString);
		updateVATCode();
		if (exceptionMessage != null && exceptionMessage.contains("name")) {
			data.setName(vatCode);

		}
	}

	@Override
	public void saveSuccess(IAccounterCore result) {
		if (result != null) {
			// if (editableVATCode == null) {
			// Accounter.showInformation(FinanceApplication.messages()
			// .newVATCodeCreated());
			//
			// } else {
			// Accounter.showInformation(FinanceApplication.messages()
			// .VATCodeUpdatedSuccessfully());
			//
			// }
			super.saveSuccess(result);

		} else {
			saveFailed(new AccounterException());
		}
	}

	protected void updateVATCode() {

		data.setName(vatCodeTxt.getValue() != null ? vatCodeTxt.getValue()
				.toString() : "");
		data.setDescription(description.getValue() != null ? description
				.getValue().toString() : "");
		data.setActive((Boolean) isActive.getValue());
		if (taxableGroupRadio.getValue() != null) {
			if (taxableGroupRadio.getValue().toString()
					.equalsIgnoreCase("Taxable")) {
				data.setTaxable(true);
				data.setTAXItemGrpForPurchases(selectedVATPurchaseAcc);
				data.setTAXItemGrpForSales(selectedVATSAlesAcc);
			} else {
				data.setTaxable(false);
			}
		} else {
			data.setTaxable(false);
		}

		if (!data.isTaxable()) {
			data.setTAXItemGrpForPurchases(0);
			data.setTAXItemGrpForSales(0);
		}
	}

	@Override
	public ValidationResult validate() {

		ValidationResult result = new ValidationResult();
		// already exists?
		// validate form

		String name = vatCodeTxt.getValue() != null ? vatCodeTxt.getValue()
				.toString() : "";

		ClientTAXCode taxCodeByName = getCompany().getTAXCodeByName(name);

		if ((isInViewMode() && taxCodeByName != null)) {
			result.addError(vatCodeTxt, messages.alreadyExist());
			return result;
		}
		result.add(DynamicForm.validate(this.getForms().toArray(
				new DynamicForm[getForms().size()])));

		// if (!((!isEdit && taxCodeByName != null))
		// || (isEdit ? (data.getName().equalsIgnoreCase(name) ? true
		// : (taxCodeByName != null
		// || taxCodeByName.getID() == this.getData()
		// .getID() ? false : true)) : true)) {
		// result.addError(vatCodeTxt, messages.alreadyExist());
		// }
		String taxbl = taxableGroupRadio.getValue().toString();
		taxableGroupRadio.setValue(taxbl);
		if (taxbl.equalsIgnoreCase(messages.taxable())) {
			if (vatItemComboForSales.getSelectedValue() == null
					&& vatItemComboForPurchases.getSelectedValue() == null) {
				result.addError(vatItemComboForSales,
						messages.enterSalesORpurchaseItem());
			}
		}

		ClientTAXItemGroup selectedValue = vatItemComboForSales
				.getSelectedValue();
		String validationResult = validateTAXItem(selectedValue, true);
		if (validationResult != null) {
			result.addError(vatItemComboForSales, validationResult);
		}

		selectedValue = vatItemComboForPurchases.getSelectedValue();
		validationResult = validateTAXItem(selectedValue, false);
		if (validationResult != null) {
			result.addError(vatItemComboForPurchases, validationResult);
		}

		return result;
	}

	private String validateTAXItem(ClientTAXItemGroup selectedValue,
			boolean isSales) {
		if (selectedValue != null) {
			if (selectedValue instanceof ClientTAXItem) {
				long taxAgencyId = ((ClientTAXItem) selectedValue)
						.getTaxAgency();
				ClientTAXAgency taxAgency = getCompany().getTaxAgency(
						taxAgencyId);

				if (taxAgency != null) {
					if (isSales) {
						if (taxAgency.getSalesLiabilityAccount() == 0) {
							return messages.pleaseSelectAnotherSalesTAXItem();
						}
					} else if (taxAgency.getPurchaseLiabilityAccount() == 0) {
						return messages.pleaseSelectAnotherPurchaseTAXItem();
					}
				}
			} else {
				List<ClientTAXItem> taxItems = ((ClientTAXGroup) selectedValue)
						.getTaxItems();
				for (ClientTAXItem item : taxItems) {
					ClientTAXAgency taxAgency = getCompany().getTaxAgency(
							item.getTaxAgency());
					if (taxAgency != null) {
						if (isSales) {
							if (taxAgency.getSalesLiabilityAccount() == 0) {
								return messages
										.pleaseSelectAnotherSalesTAXItem();
							}
						} else if (taxAgency.getPurchaseLiabilityAccount() == 0) {
							return messages
									.pleaseSelectAnotherPurchaseTAXItem();
						}
					}
				}
			}
		}
		return null;
	}

	public List<DynamicForm> getForms() {

		return listforms;
	}

	/**
	 * call this method to set focus in View
	 */
	@Override
	public void setFocus() {
		this.vatCodeTxt.setFocus();
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
				Accounter.showError(caught.getMessage());
			}

			@Override
			public void onResultSuccess(Boolean result) {
				if (result)
					enableFormItems();
			}

		};

		this.rpcDoSerivce.canEdit(AccounterCoreType.TAX_CODE, data.getID(),
				editCallBack);
	}

	protected void enableFormItems() {
		setMode(EditMode.EDIT);
		vatCodeTxt.setEnabled(!isInViewMode());
		description.setEnabled(!isInViewMode());
		isActive.setEnabled(!isInViewMode());
		taxableGroupRadio.setEnabled(!isInViewMode());
		if (vat.isTaxable()) {
			vatItemComboForPurchases.setEnabled(!isInViewMode());
			vatItemComboForSales.setEnabled(!isInViewMode());
		}
		super.onEdit();

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
		return messages.taxCode();
	}

	public void setTaxCodeName(String taxCodeName) {
		this.taxCodeName = taxCodeName;
	}

	@Override
	protected boolean canVoid() {
		return false;
	}
}
