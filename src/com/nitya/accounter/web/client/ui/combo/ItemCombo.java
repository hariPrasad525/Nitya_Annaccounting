package com.nitya.accounter.web.client.ui.combo;

import java.util.ArrayList;
import java.util.List;

import com.nitya.accounter.web.client.core.ClientItem;
import com.nitya.accounter.web.client.ui.UIUtils;
import com.nitya.accounter.web.client.ui.company.NewItemAction;
import com.nitya.accounter.web.client.ui.core.ActionCallback;

public class ItemCombo extends CustomCombo<ClientItem> {
	// Type For Checking Request is from Customer View Or Vendor View
	private int type;
	private boolean isGeneratedFromCustomer;

	// private final int TYPE_CUSTOMER = 1;
	// private final int TYPE_VENDOR = 2;

	public ItemCombo(String title, int type) {
		super(title, "ItemCombo");
		this.type = type;
		initCombo(getCompany().getItems());
	}

	public ItemCombo(String title, int type, boolean isAddNewRequired) {
		super(title, isAddNewRequired, 3, "itemCombo");
		this.type = type;
		ArrayList<ClientItem> items = getCompany().getItems();
		// dividing child's and add
		initCombo(items);
	}

	public ItemCombo(String title, int type, boolean isAddNewRequired,
			boolean service, boolean isGenerateForCustomers) {
		super(title, isAddNewRequired, 1, "itemCombo");
		this.type = type;
		this.isGeneratedFromCustomer = isGenerateForCustomers;
		setComboItems();
	}

	private void setComboItems() {
		ArrayList<ClientItem> initParentComboItems = initParentCombo();
		ArrayList<ClientItem> filterItems = UIUtils.filterItems(
				initParentComboItems, isGeneratedFromCustomer ? true : false,
				isGeneratedFromCustomer ? false : true);
		if (filterItems != null && !(filterItems.isEmpty())) {
			ArrayList<ClientItem> classes = new ArrayList<ClientItem>();
			// dividing child's and add.
			UIUtils.getFilteredListByDepth(classes, filterItems, 0, 0);
			initCombo(classes);
		}
	}

	@Override
	protected void setSelectedItem(ClientItem object, int row) {
		if (object == null) {
			setValue("");
			return;
		}
		setValue(getDisplayName(object));
	}

	/**
	 * get the reverse of StringBuffer
	 *
	 * @param actvalString
	 * @return {@link StringBuffer} Reverse
	 */
	private StringBuffer getReverseBuffer(String actvalString) {
		String[] split = actvalString.split(":");
		StringBuffer buffer = new StringBuffer();
		for (int i = split.length - 1; i > 0; --i) {
			buffer.append(split[i]);
			buffer.append(':');
		}
		buffer.append(split[0]);
		return buffer;
	}

	/**
	 *
	 * @param object
	 *            ClientAccounterClass
	 * @return {@link String}
	 */
	private String getspaces(ClientItem object) {
		StringBuffer buffer = new StringBuffer();
		if (object.getDepth() != 0) {
			for (int i = 0; i < object.getDepth(); i++) {
				buffer.append("     ");
			}
		}
		buffer.append(object.getName());
		return buffer.toString();
	}

    private ArrayList<ClientItem> initParentCombo() {
        switch (type) {
            case ClientItem.TYPE_SERVICE:
                return getCompany().getServiceItems();
            case ClientItem.TYPE_NON_INVENTORY_PART:
                return getCompany().getProductItems();
            case ClientItem.TYPE_INVENTORY_PART:
                return getCompany().getInventoryItems();
            case ClientItem.TYPE_INVENTORY_ASSEMBLY:
                return getCompany().getInventoryAssemblyItems();
        }
        return null;
    }

	public void changeComboItems(boolean iSellThis, boolean iBuyThis) {
		isGeneratedFromCustomer = iSellThis;
		ArrayList<ClientItem> initParentComboItems = initParentCombo();
		ArrayList<ClientItem> filterItems = UIUtils.filterItems(
				initParentComboItems, iSellThis, iBuyThis);
		if (filterItems != null && !(filterItems.isEmpty())) {
			ArrayList<ClientItem> classes = new ArrayList<ClientItem>();
			// dividing child's and add
			UIUtils.getFilteredListByDepth(classes, filterItems, 0, 0);
			initCombo(classes);
		} else {
			initCombo(filterItems);
		}
	}

	public void initCombo(boolean isService) {
		List<ClientItem> items = getCompany().getActiveItems();
		List<ClientItem> serviceitems = new ArrayList<ClientItem>();
		List<ClientItem> productitems = new ArrayList<ClientItem>();
		for (ClientItem item : items) {
			if (item.getType() == ClientItem.TYPE_SERVICE)
				serviceitems.add(item);
			else
				productitems.add(item);
		}
		if (isService)
			initCombo(serviceitems);
		else
			initCombo(productitems);
	}

	@Override
	public String getDefaultAddNewCaption() {
		return messages.item();
	}

	@Override
	protected String getDisplayName(ClientItem object) {

		if (object == null) {
			return null;
		}

		StringBuffer buffer = new StringBuffer();
		ClientItem parentClass = object;
		while (parentClass.getParentItem() != 0) {
			buffer.append(parentClass.getName());
			parentClass = getCompany().getItem(parentClass.getParentItem());
			buffer.append(":");
		}
		buffer.append(parentClass.getName());
		buffer = getReverseBuffer(buffer.toString());
		return buffer.toString();
	}

	@Override
	public void onAddNew() {
		NewItemAction action = new NewItemAction(isGeneratedFromCustomer, type);
		action.setCallback(new ActionCallback<ClientItem>() {

			@Override
			public void actionResult(ClientItem result) {
				if (result != null) {
					addItemThenfireEvent(result);
					setComboItems();
				}
			}
		});
		action.run(null, true);
	}

	@Override
	protected String getColumnData(ClientItem object, int col) {
		switch (col) {
		case 0:
			return getspaces(object);
		}
		return "";
	}

	// @Override
	// protected String getColumnData(ClientItem object, int row, int col) {
	// switch (col) {
	// case 0:
	// return object.getName();
	// case 1:
	// return object.getType() == ClientItem.TYPE_SERVICE ?
	// FinanceApplication.getCustomersMessages().service()
	// : .getCustomersMessages().PRoduct();
	// case 2:
	// return DataUtils.getAmountAsString(object.getSalesPrice());
	// default:
	// break;
	// }
	// // return null;
	// // }

}
