package com.nitya.accounter.web.client.ui.settings;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientCurrency;
import com.nitya.accounter.web.client.core.ClientItem;
import com.nitya.accounter.web.client.core.ClientItemGroup;
import com.nitya.accounter.web.client.core.ClientUnit;
import com.nitya.accounter.web.client.core.ClientWarehouse;
import com.nitya.accounter.web.client.externalization.AccounterMessages;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.StyledPanel;
import com.nitya.accounter.web.client.ui.core.DecimalUtil;
import com.nitya.accounter.web.client.ui.forms.AmountLabel;
import com.nitya.accounter.web.client.ui.forms.DynamicForm;
import com.nitya.accounter.web.client.ui.forms.LabelItem;

public class ItemDetailsPanel extends FlowPanel {
	ClientItem item;
	LabelItem name, availableQty, itemGroup, wareHouse, vendorProNo;
	AmountLabel standardCost, salesPrice;
	Label heading, itemName;
	private DynamicForm leftform, rightform;
	protected static final AccounterMessages messages = Global.get().messages();

	public ItemDetailsPanel(ClientItem item) {
		this.getElement().setId("ItemDetailsPanel");
		this.item = item;
		createControls();
		if (item != null) {
			showItemDetails(item);
		}

	}

	private void createControls() {

		name = new LabelItem(messages.name(), "name");

		availableQty = new LabelItem(messages.availableQty(), "availableQty");

		ClientCurrency baseCurrency = Accounter.getCompany()
				.getPrimaryCurrency();

		standardCost = new AmountLabel(messages.nameWithCurrency(
				messages.standardCost(), baseCurrency.getFormalName()),
				baseCurrency);

		salesPrice = new AmountLabel(messages.nameWithCurrency(
				messages.salesPrice(), baseCurrency.getFormalName()),
				baseCurrency);

		itemGroup = new LabelItem(messages.itemGroup(), "itemGroup");

		wareHouse = new LabelItem(messages.wareHouse(), "wareHouse");

		vendorProNo = new LabelItem(messages.vendorProductNo(Global.get()
				.Vendor()), "vendorProNo");

		leftform = new DynamicForm("leftform");
		rightform = new DynamicForm("rightform");

		leftform.add(name, availableQty, standardCost, salesPrice);

		rightform.add(itemGroup, wareHouse, vendorProNo);
		rightform.addStyleName("customers_detail_rightpanel");

		StyledPanel hp = new StyledPanel("hp");
		StyledPanel headingPanel = new StyledPanel("headingPanel");
		headingPanel.addStyleName("customers_detail_panel");
		heading = new Label(messages.payeeDetails(messages.inventoryItem())
				+ " :");
		headingPanel.add(heading);
		itemName = new Label();
		itemName.setText(messages.noItemSelected());
		headingPanel.add(heading);
		headingPanel.add(itemName);
		// headingPanel.setCellWidth(heading, "50%");
		// headingPanel.setCellWidth(itemName, "50%");
		add(headingPanel);
		hp.add(leftform);
		hp.add(rightform);
		// hp.setCellWidth(leftform, "50%");
		// hp.setCellWidth(rightform, "50%");

		add(hp);
		// headingPanel.setWidth("100%");
		// hp.setWidth("100%");
		// this.setWidth("100%");
		hp.getElement().getParentElement().addClassName("details-Panel");
	}

	protected void showItemDetails(ClientItem item) {
		if (item != null) {
			if (item.getType() == ClientItem.TYPE_INVENTORY_PART) {
				heading.setText(messages.payeeDetails(messages.inventoryItem()));
			} else if (item.getType() == ClientItem.TYPE_INVENTORY_ASSEMBLY) {
				heading.setText(messages.payeeDetails(messages
						.inventoryAssemblyItem()));
			}
			itemName.setText(item.getName());
			name.setValue(item.getName());

			availableQty.setValue(getAvailableQty(item));

			standardCost.setAmount(item.getPurchasePrice());

			salesPrice.setAmount(item.getSalesPrice());

			ClientItemGroup group = Accounter.getCompany().getItemGroup(
					item.getItemGroup());
			if (group != null) {
				itemGroup.setValue(group.getName());
			}

			ClientWarehouse wareHouse = Accounter.getCompany().getWarehouse(
					item.getWarehouse());
			if (wareHouse != null) {
				this.wareHouse.setValue(wareHouse.getName());
			}

			vendorProNo.setValue(item.getVendorItemNumber());

		} else {
			name.setValue("");
			availableQty.setValue("");
			standardCost.setAmount(0.00);
			salesPrice.setAmount(0.00);
			itemGroup.setValue("");
			wareHouse.setValue("");
			vendorProNo.setValue("");
		}
	}

	private String getAvailableQty(ClientItem item) {
		StringBuffer result = new StringBuffer();
		ClientUnit unit = Accounter.getCompany().getUnitById(
				item.getOnhandQty().getUnit());
		result.append(DecimalUtil.round(item.getOnhandQty().getValue()));
		result.append(" ");
		result.append(unit.getName());
		return result.toString();
	}
}
