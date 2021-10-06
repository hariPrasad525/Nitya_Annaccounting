package com.nitya.accounter.web.client.ui.edittable;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.Column;
import com.nitya.accounter.web.client.core.ClientCompany;
import com.nitya.accounter.web.client.core.ClientItem;
import com.nitya.accounter.web.client.core.ClientTransaction;
import com.nitya.accounter.web.client.core.ListFilter;
import com.nitya.accounter.web.client.core.Utility;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.company.NewItemAction;
import com.nitya.accounter.web.client.ui.core.ActionCallback;

public class ItemsDropDownTable extends AbstractDropDownTable<ClientItem> {
	private ListFilter<ClientItem> filter;
	private int type;
	private boolean isForCustomer = true;
	private int transactionType = 0;

	public ItemsDropDownTable(ListFilter<ClientItem> filter) {
		super(getItems(filter), true);
		this.filter = filter;
		this.getElement().setId("ItemsDropDownTable");
	}

	private static List<ClientItem> getItems(ListFilter<ClientItem> filter) {
		ArrayList<ClientItem> filteredList = Utility.filteredList(filter,
				Accounter.getCompany().getActiveItems());
		ArrayList<ClientItem> classes = new ArrayList<ClientItem>();
		// dividing child's and add
		addChilds(classes, filteredList, 0, 0);
		return classes;
	}

	/**
	 * 
	 * @param classes
	 * @param accounterClasses
	 * @param depth
	 * @param parent
	 */
	private static void addChilds(ArrayList<ClientItem> classes,
			ArrayList<ClientItem> accounterClasses, int depth, long parent) {
		ArrayList<ClientItem> childs = getChild(accounterClasses, parent);
		for (ClientItem ch : childs) {
			ch.setDepth(depth);
			classes.add(ch);
			addChilds(classes, accounterClasses, depth + 1, ch.getID());
		}
	}

	/**
	 * 
	 * @param accounterClasses
	 * @param parent
	 * @return
	 */
	private static ArrayList<ClientItem> getChild(
			List<ClientItem> accounterClasses, long p) {
		ArrayList<ClientItem> childs = new ArrayList<ClientItem>();
		for (ClientItem c : accounterClasses) {
			if (c.getParentItem() == p) {
				childs.add(c);
			}
		}
		return childs;
	}

	@Override
	public void initColumns() {
		Column<ClientItem, SafeHtml> itemColumn = new Column<ClientItem, SafeHtml>(
				new SafeHtmlCell()) {

			@Override
			public SafeHtml getValue(ClientItem object) {
				String name = null;
				if ((transactionType == ClientTransaction.TYPE_PURCHASE_ORDER
						|| transactionType == ClientTransaction.TYPE_CASH_PURCHASE || transactionType == ClientTransaction.TYPE_ENTER_BILL)
						&& object.getVendorItemNumber() != null
						&& !object.getVendorItemNumber().isEmpty()) {
					name = object.getDisplayName() + "("
							+ object.getVendorItemNumber() + ")";
				} else {
					name = object.getDisplayName();
				}
				SafeHtmlBuilder builder = new SafeHtmlBuilder();
				builder.append(SafeHtmlUtils.fromSafeConstant("<div class="
						+ "depth" + object.getDepth() + ">" + name + "</div>"));
				return builder.toSafeHtml();
			}
		};
		this.addColumn(itemColumn);
	}

	@Override
	protected boolean filter(ClientItem t, String string) {
		return getDisplayValue(t).toLowerCase().startsWith(string);
	}

	@Override
	protected String getDisplayValue(ClientItem object) {
		if ((transactionType == ClientTransaction.TYPE_PURCHASE_ORDER
				|| transactionType == ClientTransaction.TYPE_CASH_PURCHASE || transactionType == ClientTransaction.TYPE_ENTER_BILL)
				&& object.getVendorItemNumber() != null
				&& !object.getVendorItemNumber().isEmpty()) {
			return object.getDisplayName() + "(" + object.getVendorItemNumber()
					+ ")";
		} else {
			return setSelectedItem(object);
		}

	}

	protected String setSelectedItem(ClientItem object) {
		if (object == null) {
			return " ";
		}
		StringBuffer buffer = new StringBuffer();
		ClientItem parentClass = object;
		while (parentClass.getParentItem() != 0) {
			buffer.append(parentClass.getName());
			parentClass = Accounter.getCompany().getItem(
					parentClass.getParentItem());
			buffer.append(":");
		}
		buffer.append(parentClass.getName());
		buffer = getReverseBuffer(buffer.toString());
		return buffer.toString();
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

	@Override
	protected ClientItem getAddNewRow() {
		ClientCompany company = Accounter.getCompany();
		ClientItem clientItem = new ClientItem();
		boolean sellServices = company.getPreferences().isSellServices();
		boolean sellProducts = company.getPreferences().isSellProducts();
		if (sellServices && sellProducts) {
			clientItem.setName(messages.comboDefaultAddNew(messages.item()));
		} else if (sellServices) {
			clientItem.setName(messages.comboDefaultAddNew(messages
					.serviceItem()));
		} else if (sellProducts) {
			clientItem.setName(messages.comboDefaultAddNew(messages
					.productItem()));
		}
		return clientItem;
	}

	@Override
	public void addNewItem(String text) {
		NewItemAction action;
		action = new NewItemAction(isForCustomer());
		action.setFrmAnyView(true);
		action.setCallback(new ActionCallback<ClientItem>() {

			@Override
			public void actionResult(ClientItem result) {
				if (result.isActive() && filter.filter(result)) {
					selectRow(result);
				}

			}
		});
		action.setType(getItemType());
		action.setItemText(text);
		action.run(null, true);
	}

	@Override
	public void addNewItem() {
		addNewItem("");
	}

	public int getItemType() {
		return type;
	}

	public void setItemType(int type) {
		this.type = type;
	}

	@Override
	protected Class<?> getType() {
		return ClientItem.class;
	}

	@Override
	public List<ClientItem> getTotalRowsData() {
		return getItems(filter);
	}

	public boolean isForCustomer() {
		return isForCustomer;
	}

	public void setForCustomer(boolean isForCustomer) {
		this.isForCustomer = isForCustomer;
	}

	public void setTransactionType(int transactionType) {
		this.transactionType = transactionType;
	}
}
