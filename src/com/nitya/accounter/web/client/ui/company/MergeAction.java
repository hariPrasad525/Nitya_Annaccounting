package com.nitya.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.Features;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.CustomerMergeView;
import com.nitya.accounter.web.client.ui.HistoryTokens;
import com.nitya.accounter.web.client.ui.MainFinanceWindow;
import com.nitya.accounter.web.client.ui.core.Action;

public class MergeAction extends Action {

	public static final int TYPE_CUSTOMERS = 1;
	public static final int TYPE_VENDORS = 2;
	public static final int TYPE_ITEMS = 3;
	public static final int TYPE_ACCOUNTS = 4;
	public static final int TYPE_CLASSES = 5;
	public static final int TYPE_LOCATIONS = 6;

	private int type;

	public MergeAction(int type) {
		super();
		this.type = type;
	}

	@Override
	public void run() {
		if (!Accounter.hasPermission(Features.MERGING) && !isCalledFromHistory) {
			Accounter.showSubscriptionWarning();
			return;
		}

		switch (type) {
		case TYPE_CUSTOMERS:

			CustomerMergeView customerView = new CustomerMergeView();
			MainFinanceWindow.getViewManager().showView(customerView, data,
					isDependent, MergeAction.this);
			break;
		case TYPE_VENDORS:
			VendorMergeView vendorView = new VendorMergeView();
			MainFinanceWindow.getViewManager().showView(vendorView, data,
					isDependent, MergeAction.this);
			break;
		case TYPE_ACCOUNTS:
			AccountMergeView accountView = new AccountMergeView();
			MainFinanceWindow.getViewManager().showView(accountView, data,
					isDependent, MergeAction.this);
			break;
		case TYPE_ITEMS:
			ItemMergeView itemView = new ItemMergeView();
			MainFinanceWindow.getViewManager().showView(itemView, data,
					isDependent, MergeAction.this);
			break;
		case TYPE_CLASSES:
			ClassMergeView classView = new ClassMergeView();
			MainFinanceWindow.getViewManager().showView(classView, data,
					isDependent, MergeAction.this);
			break;
		case TYPE_LOCATIONS:
			LocationMergeView locationView = new LocationMergeView();
			MainFinanceWindow.getViewManager().showView(locationView, data,
					isDependent, MergeAction.this);
			break;
		default:
			break;
		}
	}

	@Override
	public ImageResource getBigImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		return null;
	}

	@Override
	public String getHistoryToken() {
		switch (type) {
		case TYPE_CUSTOMERS:
			return HistoryTokens.MERGECUSTOMERS;
		case TYPE_VENDORS:
			return HistoryTokens.MERGEVENDOR;
		case TYPE_ACCOUNTS:
			return HistoryTokens.MERGEACCOUNT;
		case TYPE_ITEMS:
			return "merge_item";
		case TYPE_CLASSES:
			return HistoryTokens.MERGECLASS;
		case TYPE_LOCATIONS:
			return HistoryTokens.MERGELOCATION;
		default:
			break;
		}
		return null;
	}

	@Override
	public String getHelpToken() {
		return getHistoryToken();
	}

	@Override
	public String getText() {
		switch (type) {
		case TYPE_CUSTOMERS:
			return messages.mergeCustomers(Global.get().customers());
		case TYPE_VENDORS:
			return messages.mergeVendors(Global.get().vendors());
		case TYPE_ACCOUNTS:
			return messages.mergeAccounts();
		case TYPE_ITEMS:
			return messages.mergeItems();
		case TYPE_CLASSES:
			return messages.mergeClass();
		case TYPE_LOCATIONS:
			return messages.mergeClass();
		default:
			break;
		}
		return null;
	}

	public static MergeAction customers() {
		return new MergeAction(TYPE_CUSTOMERS);
	}

	public static MergeAction vendors() {
		return new MergeAction(TYPE_VENDORS);
	}

	public static MergeAction accounts() {
		return new MergeAction(TYPE_ACCOUNTS);
	}

	public static MergeAction items() {
		return new MergeAction(TYPE_ITEMS);
	}

	public static MergeAction classes() {
		return new MergeAction(TYPE_CLASSES);
	}

	public static MergeAction locations() {
		return new MergeAction(TYPE_LOCATIONS);
	}

}
