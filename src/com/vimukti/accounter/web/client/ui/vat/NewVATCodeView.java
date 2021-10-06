//package com.vimukti.accounter.web.client.ui.vat;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import com.google.gwt.event.dom.client.ClickEvent;
//import com.google.gwt.event.dom.client.ClickHandler;
//import com.google.gwt.user.client.ui.Label;
//import com.google.gwt.user.client.ui.StyledPanel;
//import com.vimukti.accounter.web.client.core.AccounterCommand;
//import com.vimukti.accounter.web.client.core.AccounterCoreType;
//import com.vimukti.accounter.web.client.core.ClientVATCode;
//import com.vimukti.accounter.web.client.core.ClientTAXItem;
//import com.vimukti.accounter.web.client.core.IAccounterCore;
//import com.vimukti.accounter.web.client.core.Utility;
//import com.vimukti.accounter.web.client.ui.FinanceApplication;
//import com.vimukti.accounter.web.client.ui.UIUtils;
//import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
//import com.vimukti.accounter.web.client.ui.combo.VATItemCombo;
//import com.vimukti.accounter.web.client.ui.core.Accounter;
//import com.vimukti.accounter.web.client.ui.core.AccounterErrorType;
//import com.vimukti.accounter.web.client.ui.core.BaseView;
//import com.vimukti.accounter.web.client.ui.core.InvalidEntryException;
//import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
//import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
//import com.vimukti.accounter.web.client.ui.forms.RadioGroupItem;
//import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
//import com.vimukti.accounter.web.client.ui.forms.TextItem;
//
///**
// * @author Murali.A
// * 
// */
//public class NewVATCodeView extends BaseView<ClientVATCode> {
//
//	private TextItem vatCodeTxt;
//	private TextAreaItem description;
//	private CheckboxItem isActive;
//	private RadioGroupItem taxableGroupRadio;
//	private VATItemCombo vatItemComboForPurchases;
//	private VATItemCombo vatItemComboForSales;
//	private DynamicForm vatNameForm;
//	public String selectedVATPurchaseAcc = "";
//	public String selectedVATSAlesAcc = "";
//	private ClientVATCode editableVATCode;
//	protected boolean isComboDisabled = false;
//
//	private ArrayList<DynamicForm> listforms;
//
//	public NewVATCodeView() {
//		super();
//		this.validationCount = 4;
//	}
//
//	@Override
//	public void init() {
//		super.init();
//		editableVATCode = (ClientVATCode) this.data;
//		createControls();
//		setSize("100%", "");
//
//	}
//
//	@Override
//	public void initData() {
//		ClientVATCode vat = (ClientVATCode) getData();
//		if (vat != null) {
//			vatCodeTxt.setValue(vat.getName() != null ? vat.getName() : "");
//			description.setValue(vat.getDescription() != null ? vat
//					.getDescription() : "");
//			isActive.setValue(vat.isActive());
//			taxableGroupRadio.setValue(vat.isTaxable() ? FinanceApplication
//					.constants().taxable() : FinanceApplication
//					.constants().taxExempt());
//
//			if (FinanceApplication.getCompany().getVATItem(
//					vat.getVATItemGrpForPurchases()) != null) {
//				selectedVATPurchaseAcc = vat.getVATItemGrpForPurchases();
//				vatItemComboForPurchases.setComboItem(FinanceApplication
//						.getCompany().getVATItem(
//								vat.getVATItemGrpForPurchases()));
//			} else
//				vatItemComboForPurchases.setSelected("");
//
//			if (FinanceApplication.getCompany().getVATItem(
//					vat.getVATItemGrpForSales()) != null) {
//				selectedVATSAlesAcc = vat.getVATItemGrpForSales();
//				vatItemComboForSales.setComboItem(FinanceApplication
//						.getCompany().getVATItem(vat.getVATItemGrpForSales()));
//			} else
//				vatItemComboForSales.setSelected("");
//
//			if (!vat.isTaxable()) {
//				vatItemComboForPurchases.setDisabled(true);
//				vatItemComboForSales.setDisabled(true);
//			}
//		}
//	}
//
//	private void createControls() {
//		Label infoLabel = new Label(FinanceApplication.constants()
//				.newVATCode());
//		infoLabel.setStyleName(FinanceApplication.constants()
//				.lableTitle());
//
//		listforms = new ArrayList<DynamicForm>();
//
//		vatCodeTxt = new TextItem(vatMessages.vatCode());
//		vatCodeTxt.setRequired(true);
//		vatCodeTxt.setWidth(100);
//		description = new TextAreaItem();
//		description.setWidth(100);
//		description.setTitle(FinanceApplication.constants().description());
//
//		isActive = new CheckboxItem(FinanceApplication.constants()
//				.isActive());
//		isActive.setValue((Boolean) true);
//
//		taxableGroupRadio = new RadioGroupItem(FinanceApplication
//				.constants().tax());
//		taxableGroupRadio.setWidth(100);
//		taxableGroupRadio.setValues(getClickHandler(), FinanceApplication
//				.constants().taxable(), FinanceApplication
//				.constants().taxExempt());
//		taxableGroupRadio.setDefaultValue(FinanceApplication.constants()
//				.taxable());
//
//		vatItemComboForPurchases = new VATItemCombo(FinanceApplication
//				.constants().VATItemForPurchases());
//		vatItemComboForPurchases.initCombo(vatItemComboForPurchases
//				.getPurchaseWithPrcntVATItems());
//		vatItemComboForPurchases.setRequired(true);
//		vatItemComboForPurchases.setWidth(100);
//		vatItemComboForPurchases
//				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientTAXItem>() {
//					@Override
//					public void selectedComboBoxItem(ClientTAXItem selectItem) {
//						if (selectItem != null)
//							selectedVATPurchaseAcc = selectItem.getID();
//					}
//				});
//
//		vatItemComboForSales = new VATItemCombo(FinanceApplication
//				.constants().VATItemForSales());
//		vatItemComboForSales.initCombo(vatItemComboForSales
//				.getSalesWithPrcntVATItems());
//		vatItemComboForSales.setRequired(true);
//		vatItemComboForSales
//				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientTAXItem>() {
//
//					@Override
//					public void selectedComboBoxItem(ClientTAXItem selectItem) {
//						if (selectItem != null)
//							selectedVATSAlesAcc = selectItem.getID();
//					}
//				});
//		vatNameForm = new DynamicForm();
//		vatNameForm.setWidth("80%");
//		vatNameForm.setFields(vatCodeTxt, description, taxableGroupRadio,
//				isActive, vatItemComboForSales, vatItemComboForPurchases);
//
//		if (editableVATCode != null) {
//			vatCodeTxt
//					.setValue(editableVATCode.getName() != null ? editableVATCode
//							.getName()
//							: "");
//			description
//					.setValue(editableVATCode.getDescription() != null ? editableVATCode
//							.getDescription()
//							: "");
//			isActive.setValue(editableVATCode.isActive());
//			taxableGroupRadio
//					.setValue(editableVATCode.isTaxable() ? FinanceApplication
//							.constants().taxable() : FinanceApplication
//							.constants().taxExempt());
//			vatItemComboForPurchases.setValue(editableVATCode
//					.getVATItemGrpForPurchases() != null ? FinanceApplication
//					.getCompany().getVATItemGroup(
//							editableVATCode.getVATItemGrpForPurchases())
//					.getName() : "");
//			vatItemComboForSales.setValue(editableVATCode
//					.getVATItemGrpForSales() != null ? FinanceApplication
//					.getCompany().getVATItemGroup(
//							editableVATCode.getVATItemGrpForSales()).getName()
//					: "");
//		}
//
//		StyledPanel mainVPanel = new StyledPanel();
//		mainVPanel.setSpacing(25);
//		mainVPanel.setWidth("100%");
//		mainVPanel.add(infoLabel);
//		mainVPanel.add(vatNameForm);
//
//		if (UIUtils.isMSIEBrowser()) {
//			vatNameForm.getCellFormatter().setWidth(0, 1, "270px");
//			vatNameForm.setWidth("50%");
//		}
//		canvas.add(mainVPanel);
//
//		/* Adding dynamic forms in list */
//		listforms.add(vatNameForm);
//
//	}
//
//	private ClickHandler getClickHandler() {
//		ClickHandler hanler = new ClickHandler() {
//
//			@Override
//			public void onClick(ClickEvent event) {
//				vatCodeTxt.getMainWidget().removeStyleName(
//						"highlightedFormItem");
//				vatItemComboForPurchases.getMainWidget().removeStyleName(
//						"highlightedFormItem");
//				vatItemComboForSales.getMainWidget().removeStyleName(
//						"highlightedFormItem");
//				String taxbl = taxableGroupRadio.getValue().toString();
//				if (taxbl.equalsIgnoreCase(FinanceApplication.constants()
//						.taxable())) {
//					isComboDisabled = false;
//					vatItemComboForPurchases.setDisabled(false);
//					vatItemComboForSales.setDisabled(false);
//					vatItemComboForPurchases.setRequired(true);
//					vatItemComboForSales.setRequired(true);
//				} else {
//					isComboDisabled = true;
//					vatItemComboForPurchases.setRequired(false);
//					vatItemComboForSales.setRequired(false);
//					vatItemComboForSales.setDisabled(true);
//					vatItemComboForPurchases.setDisabled(true);
//				}
//			}
//		};
//		return hanler;
//	}
//
//	@Override
//	public void saveAndUpdateView() throws Exception {
//
//		ClientVATCode vatCode = getVATCode();
//
//		if (editableVATCode == null)
//		saveOrUpdate(vatCode);
//		else
//			alterObject(vatCode);
//
//	}
//
//	@Override
//	public void saveFailed(AccounterException exception) {
//		super.saveFailed(exception);
//		Accounter.showError(exception.getMessage());
//	}
//
//	@Override
//	public void saveSuccess(IAccounterCore result) {
//		if (result != null) {
//			// if (editableVATCode == null) {
//			// Accounter.showInformation(FinanceApplication.constants()
//			// .newVATCodeCreated());
//			//
//			// } else {
//			// Accounter.showInformation(FinanceApplication.constants()
//			// .VATCodeUpdatedSuccessfully());
//			//
//			// }
//			super.saveSuccess(result);
//
//		} else {
//			saveFailed(new Exception());
//		}
//	}
//
//	protected ClientVATCode getVATCode() {
//		ClientVATCode vatCode;
//		if (editableVATCode != null) {
//			vatCode = editableVATCode;
//		} else
//			vatCode = new ClientVATCode();
//
//		vatCode.setName(vatCodeTxt.getValue() != null ? vatCodeTxt.getValue()
//				.toString() : "");
//		vatCode.setDescription(description.getValue() != null ? description
//				.getValue().toString() : "");
//		vatCode.setActive((Boolean) isActive.getValue());
//		if (taxableGroupRadio.getValue() != null) {
//			if (taxableGroupRadio.getValue().toString().equalsIgnoreCase(
//					"Taxable"))
//				vatCode.setTaxable(true);
//			else
//				vatCode.setTaxable(false);
//		} else
//			vatCode.setTaxable(false);
//
//		vatCode.setVATItemGrpForPurchases(selectedVATPurchaseAcc);
//		vatCode.setVATItemGrpForSales(selectedVATSAlesAcc);
//
//		return vatCode;
//	}
//
//	@Override
//	public boolean validate() throws Exception {
//
//		switch (this.validationCount) {
//		case 4:
//			List<DynamicForm> forms = this.getForms();
//			for (DynamicForm form : forms) {
//				if (form != null) {
//					form.validate();
//				}
//			}
//			return true;
//		case 3:
//			String name = vatCodeTxt.getValue() != null ? vatCodeTxt.getValue()
//					.toString() : "";
//			if(name==null || name.equals(""))
//						throw new InvalidEntryException(AccounterErrorType.REQUIRED_FIELDS);
//			if (((editableVATCode == null && Utility.isObjectExist(
//					FinanceApplication.getCompany().getVatCodes(), name)) ? false
//					: true)
//					|| (editableVATCode != null ? (editableVATCode.getName()
//							.equalsIgnoreCase(name) ? true : (Utility
//							.isObjectExist(FinanceApplication.getCompany()
//									.getVatCodes(), name) ? false : true))
//							: true)) {
//				return true;
//			} else
//				throw new InvalidEntryException(AccounterErrorType.ALREADYEXIST);
//		case 2:
//			if (!isComboDisabled && !vatItemComboForPurchases.validate()) {
//				if (!vatItemComboForSales.validate()) {
//					throw new InvalidEntryException(
//							AccounterErrorType.REQUIRED_FIELDS
//					// + " AnyOne Of This Sales Or Purchase"
//					);
//				}
//			}
//			return true;
//
//		case 1:
//			if (!isComboDisabled && !vatItemComboForSales.validate()) {
//				if (!vatItemComboForPurchases.validate()) {
//					throw new InvalidEntryException(
//							AccounterErrorType.REQUIRED_FIELDS
//					// + " AnyOne Of This Sales Or Purchase"
//					);
//				}
//			}
//			return true;
//
//		default:
//			return false;
//
//		}
//	}
//
//	public List<DynamicForm> getForms() {
//
//		return listforms;
//	}
//
//	/**
//	 * call this method to set focus in View
//	 */
//	@Override
//	public void setFocus() {
//		this.vatCodeTxt.setFocus();
//	}
//
//	@Override
//	public void deleteFailed(AccounterException caught) {
//
//	}
//
//	@Override
//	public void deleteSuccess(IAccounterCore result){
//
//	}
//
//	@Override
//	public void fitToSize(int height, int width) {
//		super.fitToSize(height,width);
//	}
//
//	@Override
//	public void processupdateView(IAccounterCore core, int command) {
//
//		switch (command) {
//
//		case AccounterCommand.CREATION_SUCCESS:
//
//			if (core.getObjectType() == AccounterCoreType.TAXITEM)
//				this.vatItemComboForPurchases
//						.addComboItem((ClientTAXItem) core);
//
//			if (core.getObjectType() == AccounterCoreType.TAXITEM)
//				this.vatItemComboForSales.addComboItem((ClientTAXItem) core);
//
//			break;
//		case AccounterCommand.DELETION_SUCCESS:
//
//			if (core.getObjectType() == AccounterCoreType.TAXITEM)
//				this.vatItemComboForPurchases
//						.removeComboItem((ClientTAXItem) core);
//
//			if (core.getObjectType() == AccounterCoreType.TAXITEM)
//				this.vatItemComboForSales.removeComboItem((ClientTAXItem) core);
//			break;
//
//		case AccounterCommand.UPDATION_SUCCESS:
//
//			if (core.getObjectType() == AccounterCoreType.TAXITEM)
//				this.vatItemComboForPurchases
//						.updateComboItem((ClientTAXItem) core);
//
//			if (core.getObjectType() == AccounterCoreType.TAXITEM)
//				this.vatItemComboForSales.updateComboItem((ClientTAXItem) core);
//			break;
//
//		}
//	}
//
//	@Override
//	public void onEdit() {
//
//	}
//
//	@Override
//	public void print() {
//
//	}
//
//	@Override
//	public void printPreview() {
//// NOTHING TO DO.
//	}
// }
