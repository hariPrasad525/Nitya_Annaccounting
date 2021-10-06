package com.nitya.accounter.web.client.ui;

import com.nitya.accounter.web.client.core.ClientItem;
import com.nitya.accounter.web.client.core.ValidationResult;
import com.nitya.accounter.web.client.ui.company.InventoryActions;
import com.nitya.accounter.web.client.ui.company.NewItemAction;
import com.nitya.accounter.web.client.ui.core.ActionFactory;
import com.nitya.accounter.web.client.ui.core.BaseDialog;
import com.nitya.accounter.web.client.ui.core.ViewManager;
import com.nitya.accounter.web.client.ui.forms.DynamicForm;
import com.nitya.accounter.web.client.ui.forms.RadioGroupItem;

/**
 * 
 * @author Mandeep Singh
 * 
 */

public class SelectItemTypeDialog extends BaseDialog<ClientItem> {
	RadioGroupItem typeRadio;
	public static final int TYPE_SERVICE = 1;
	public static final int TYPE_NON_INVENTORY_PART = 3;
	// private ViewConfiguration configuration;
	boolean forCustomer;
	private DynamicForm typeForm;
	private boolean isDependent = true;
	private String itemname;
	private boolean frmAnyView;

	public SelectItemTypeDialog(boolean isGeneratedFromCustomer) {
		super(messages.selectItemType(), messages.selectOneOfItem());
		this.forCustomer = isGeneratedFromCustomer;
		this.getElement().setId("SelectItemTypeDialog");
		createControls();
	}

	private void createControls() {

		typeRadio = new RadioGroupItem();
		typeRadio.setShowTitle(false);
		typeRadio.setRequired(true);

		// LinkedHashMap<String, String> typeMap = new LinkedHashMap<String,
		// String>();
		// typeMap.put("service", "Service");
		// typeMap.put("non-inventory", "Non-Inventory&nbsp;Item");
		boolean sellServices = company.getPreferences().isSellServices();
		boolean sellProducts = company.getPreferences().isSellProducts();
		if (getPreferences().isInventoryEnabled()) {
			if (sellProducts && sellServices) {
				typeRadio.setValue(messages.serviceItem(),
						messages.inventoryItem(), messages.nonInventoryItem(),
						messages.inventoryAssembly(),messages.addNew(messages2.itConsultant()));
				typeRadio.setDefaultValue(messages.serviceItem());
			} else if (sellProducts) {
				typeRadio.setValueMap(messages.inventoryItem(),
						messages.nonInventoryItem());
				typeRadio.setDefaultValue(messages.inventoryItem());
			}
		} else {
			typeRadio.setValueMap(messages.serviceItem(),
					messages.productItem());
			typeRadio.setDefaultValue(messages.serviceItem());
		}

		typeForm = new DynamicForm("typeForm");
		typeForm.add(typeRadio);
		// typeForm.setWidth("100%");

		StyledPanel mainVLay = new StyledPanel("mainVLay");
		mainVLay.add(typeForm);

		setBodyLayout(mainVLay);
		// setWidth("320px");
		ViewManager.getInstance().showDialog(this);
	}

	@Override
	protected ValidationResult validate() {
		return typeForm.validate();
	}

	@Override
	protected boolean onOK() {
		if (typeRadio.getValue() != null) {
			String radio = typeRadio.getValue().toString();

			if (radio.equals(messages.serviceItem())) {
				NewItemAction action = new NewItemAction(forCustomer);
				action.setDependent(isDependent);
				action.setType(ClientItem.TYPE_SERVICE);
				action.setCallback(getCallback());
				action.setItemText(itemname);
				action.run();
			} else if (radio.equals(messages.inventoryItem())) {
				NewItemAction action = new NewItemAction(forCustomer);
				action.setDependent(isDependent);
				action.setType(ClientItem.TYPE_INVENTORY_PART);
				action.setCallback(getCallback());
				action.setItemText(itemname);
				action.run();
			} else if (radio.equals(messages.nonInventoryItem())) {
				NewItemAction action = new NewItemAction(forCustomer);
				action.setDependent(isDependent);
				action.setType(ClientItem.TYPE_NON_INVENTORY_PART);
				action.setCallback(getCallback());
				action.setItemText(itemname);
				action.run();
			} else if (radio.equals(messages.productItem())) {
				NewItemAction action = new NewItemAction(forCustomer);
				action.setDependent(isDependent);
				action.setType(ClientItem.TYPE_NON_INVENTORY_PART);
				action.setCallback(getCallback());
				action.setItemText(itemname);
				action.run();
			} else if (radio.equals(messages.addNew(messages2.itConsultant()))) {
				  ActionFactory.getNewConsultantAction().run();
			} else if (radio.equals(messages.inventoryAssembly())) {
				InventoryActions action = InventoryActions.newAssembly();
				action.setDependent(isDependent);
				action.setItemText(itemname);
				action.run();
			}
			// UIUtils.setCanvas(itemView, configuration);
		}

		return true;
	}

	public boolean isFrmAnyView() {
		return frmAnyView;
	}

	public void setFrmAnyView(boolean frmAnyView) {
		this.frmAnyView = frmAnyView;
	}

	@Override
	protected boolean onCancel() {
		if (isFrmAnyView()) {
			return true;
		} else {
			return super.onCancel();
		}
	}

	public void setDependent(boolean isDependent) {
		this.isDependent = isDependent;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	public void setItemname(String itemname) {
		this.itemname = itemname;
	}

	@Override
	public boolean isViewDialog() {
		// TODO Auto-generated method stub
		return false;
	}
}
