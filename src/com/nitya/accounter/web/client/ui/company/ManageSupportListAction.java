package com.nitya.accounter.web.client.ui.company;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.images.FinanceMenuImages;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.HistoryTokens;
import com.nitya.accounter.web.client.ui.MainFinanceWindow;
import com.nitya.accounter.web.client.ui.PaymentTermListDialog;
import com.nitya.accounter.web.client.ui.SalesTaxGroupListView;
import com.nitya.accounter.web.client.ui.SalesTaxGroupView;
import com.nitya.accounter.web.client.ui.SalesTaxItemsView;
import com.nitya.accounter.web.client.ui.WebsocketAccounterInitialiser;
import com.nitya.accounter.web.client.ui.core.Action;
import com.nitya.accounter.web.client.ui.core.BaseListView;
import com.nitya.accounter.web.client.ui.customers.CurrencyGroupListView;
import com.nitya.accounter.web.client.ui.vendors.ManageSupportListView;
import com.nitya.accounter.web.client.ui.win8.CompaniesPanel;

public class ManageSupportListAction extends Action {

	public static final int TYPE_PAYMENT_TERMS = 1;
	public static final int TYPE_CUSTOMER_GROUPS = 2;
	public static final int TYPE_VENDOR_GROUPS = 3;
	public static final int TYPE_ITEM_GROUPS = 4;
	public static final int TYPE_SHIPPING_METHODS = 5;
	public static final int TYPE_SHIPPING_TERMS = 6;
	public static final int TYPE_CLASSES = 7;
	public static final int TYPE_LOCATIONS = 8;
	public static final int TYPE_PRICE_LEVELS = 9;
	public static final int TYPE_CURRENCY_GROUPS = 10;
	public static final int TYPE_SALES_TAX_GROUPS = 11;
	public static final int TYPE_SALES_TAX_ITEMS = 12;
	public static final int TYPE_SALES_TAX_GROUP = 13;
	public static final int TYPE_COMPANIES = 14;

	private int type;

	public ManageSupportListAction(int type) {
		super();
		this.type = type;
	}

	@Override
	public void run() {
		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				BaseListView<? extends IAccounterCore> dialog = null;
				switch (type) {
				case TYPE_PAYMENT_TERMS:
					dialog = new PaymentTermListDialog();
					break;
				case TYPE_CUSTOMER_GROUPS:
					dialog = new ManageSupportListView(
							IAccounterCore.CUSTOMER_GROUP);
					break;
				case TYPE_VENDOR_GROUPS:
					dialog = new ManageSupportListView(
							IAccounterCore.VENDOR_GROUP);
					break;
				case TYPE_ITEM_GROUPS:
					dialog = new ManageSupportListView(
							IAccounterCore.ITEM_GROUP);
					break;
				case TYPE_SHIPPING_TERMS:
					dialog = new ManageSupportListView(
							IAccounterCore.SHIPPING_TERMS);
					break;
				case TYPE_SHIPPING_METHODS:
					dialog = new ManageSupportListView(
							IAccounterCore.SHIPPING_METHOD);
					break;
				case TYPE_CLASSES:
					dialog = new ManageSupportListView(
							IAccounterCore.ACCOUNTER_CLASS);
					break;
				case TYPE_LOCATIONS:
					dialog = new ManageSupportListView(IAccounterCore.LOCATION);
					break;
				case TYPE_PRICE_LEVELS:
					dialog = new ManageSupportListView(
							IAccounterCore.PRICE_LEVEL);
					break;
				case TYPE_CURRENCY_GROUPS:
					dialog = new CurrencyGroupListView();
					break;
				case TYPE_SALES_TAX_GROUPS:
					SalesTaxGroupListView view = new SalesTaxGroupListView();
					MainFinanceWindow.getViewManager().showView(view, data,
							isDependent, ManageSupportListAction.this);
					break;
				case TYPE_SALES_TAX_ITEMS:
					SalesTaxItemsView taxItemsView = new SalesTaxItemsView();
					MainFinanceWindow.getViewManager().showView(taxItemsView,
							data, isDependent, ManageSupportListAction.this);
					break;
				case TYPE_SALES_TAX_GROUP:
					SalesTaxGroupView salesTaxGroupView = new SalesTaxGroupView();
					MainFinanceWindow.getViewManager().showView(
							salesTaxGroupView, data, isDependent,
							ManageSupportListAction.this);
					break;
				case TYPE_COMPANIES:
					WebsocketAccounterInitialiser accounterInitializer = Accounter
							.getAccounterInitializer();
					CompaniesPanel companiesPanel = new CompaniesPanel(
							Accounter.getAccounterInitializer());
					accounterInitializer.showView(companiesPanel);
					break;
				}
				if (dialog != null) {
					MainFinanceWindow.getViewManager().showView(dialog, data,
							isDependent, ManageSupportListAction.this);
				}

			}

			public void onFailure(Throwable e) {
				Accounter.showError(Global.get().messages()
						.unableToshowtheview());
			}
		});
	}

	@Override
	public ImageResource getBigImage() {
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		FinanceMenuImages financeMenuImages = Accounter.getFinanceMenuImages();
		switch (type) {
		case TYPE_PAYMENT_TERMS:
			return financeMenuImages.paymentTermsList();
		case TYPE_CUSTOMER_GROUPS:
			return financeMenuImages.customers();
		case TYPE_VENDOR_GROUPS:
			return financeMenuImages.vendors();
		case TYPE_ITEM_GROUPS:
			return financeMenuImages.items();
		case TYPE_SHIPPING_TERMS:
			return financeMenuImages.shippingTermList();
		case TYPE_SHIPPING_METHODS:
			return financeMenuImages.shippingMethodsList();
		case TYPE_PRICE_LEVELS:
			return financeMenuImages.priceLevelList();
		case TYPE_CURRENCY_GROUPS:
			break;
		}
		return null;
	}

	@Override
	public String getHistoryToken() {
		switch (type) {
		case TYPE_PAYMENT_TERMS:
			return "paymentTerms";
		case TYPE_CUSTOMER_GROUPS:
			return "customerGroupList";
		case TYPE_VENDOR_GROUPS:
			return "vendorGroupList";
		case TYPE_ITEM_GROUPS:
			return "itemGroupList";
		case TYPE_SHIPPING_TERMS:
			return "shippingTermsList";
		case TYPE_SHIPPING_METHODS:
			return "shippingMethodsList";
		case TYPE_CLASSES:
			return "accounter-Class-List";
		case TYPE_LOCATIONS:
			return "location-group-list";
		case TYPE_PRICE_LEVELS:
			return "priceLevels";
		case TYPE_CURRENCY_GROUPS:
			return "currencyGroupList";
		case TYPE_SALES_TAX_GROUPS:
			if (Accounter.getUser().canDoInvoiceTransactions()) {
				return HistoryTokens.MANAGESALESTAXGROUP;
			}
			return HistoryTokens.SALESTAXGROUPsalesTaxGroup;
		case TYPE_SALES_TAX_ITEMS:
			if (Accounter.getUser().canDoInvoiceTransactions()) {
				return HistoryTokens.MANAGESALESTAXITEMS;
			}
			return HistoryTokens.SALESTAXITEMS;
		case TYPE_SALES_TAX_GROUP:
			return HistoryTokens.SALES_TAX_GROUP;
		case TYPE_COMPANIES:
			return HistoryTokens.COMPANIES;
		}
		return null;
	}

	@Override
	public String getHelpToken() {
		switch (type) {
		case TYPE_PAYMENT_TERMS:
			return "payment_tem-list";
		case TYPE_CUSTOMER_GROUPS:
			return "customer-group";
		case TYPE_VENDOR_GROUPS:
			return "vendor_group-list";
		case TYPE_ITEM_GROUPS:
			return "item-group";
		case TYPE_SHIPPING_TERMS:
			return "shipping_term-list";
		case TYPE_SHIPPING_METHODS:
			return "shipping_method-list";
		case TYPE_CLASSES:
			return "accounter-Class-List";
		case TYPE_LOCATIONS:
			return "location-group-list";
		case TYPE_PRICE_LEVELS:
			return "price_level-list";
		case TYPE_CURRENCY_GROUPS:
			return "currency-group";
		case TYPE_SALES_TAX_GROUPS:
			return "sales_tax-group";
		case TYPE_SALES_TAX_ITEMS:
			return "pay_sales-tax";
		case TYPE_SALES_TAX_GROUP:
			return HistoryTokens.SALES_TAX_GROUP;
		case TYPE_COMPANIES:
			return "companies";
		}
		return null;
	}

	@Override
	public String getText() {
		switch (type) {
		case TYPE_PAYMENT_TERMS:
			return messages.paymentTermList();
		case TYPE_CUSTOMER_GROUPS:
			return messages.payeeGroupList(Global.get().Customer());
		case TYPE_VENDOR_GROUPS:
			return Global.get().messages()
					.payeeGroupList(Global.get().Vendor());
		case TYPE_ITEM_GROUPS:
			return messages.itemGroupList();
		case TYPE_SHIPPING_TERMS:
			return messages.shippingTermList();
		case TYPE_SHIPPING_METHODS:
			return messages.shippingMethodList();
		case TYPE_CLASSES:
			return messages.accounterClassList();
		case TYPE_LOCATIONS:
			return messages.locationGroupList(Global.get().Location());
		case TYPE_PRICE_LEVELS:
			return messages.priceLevelList();
		case TYPE_CURRENCY_GROUPS:
			messages.currencyList();
		case TYPE_SALES_TAX_GROUPS:
			String text;
			if (Accounter.getUser().canDoInvoiceTransactions())
				text = messages.manageSalesTaxGroups();
			else
				text = messages.salesTaxGroups();
			return text;
		case TYPE_SALES_TAX_ITEMS:
			String constant;
			if (Accounter.getUser().canDoInvoiceTransactions())
				constant = messages.manageSalesItems();
			else
				constant = messages.salesTaxItems();
			return constant;
		case TYPE_SALES_TAX_GROUP:
			return messages.taxGroup();
		case TYPE_COMPANIES:
			return messages.companies();
		}
		return null;
	}

	public static ManageSupportListAction paymentTerms() {
		return new ManageSupportListAction(TYPE_PAYMENT_TERMS);
	}

	public static ManageSupportListAction customerGroups() {
		return new ManageSupportListAction(TYPE_CUSTOMER_GROUPS);
	}

	public static ManageSupportListAction vendorGroups() {
		return new ManageSupportListAction(TYPE_VENDOR_GROUPS);
	}

	public static ManageSupportListAction itemGroups() {
		return new ManageSupportListAction(TYPE_ITEM_GROUPS);
	}

	public static ManageSupportListAction shippingTerms() {
		return new ManageSupportListAction(TYPE_SHIPPING_TERMS);
	}

	public static ManageSupportListAction shippingMethods() {
		return new ManageSupportListAction(TYPE_SHIPPING_METHODS);
	}

	public static ManageSupportListAction classes() {
		return new ManageSupportListAction(TYPE_CLASSES);
	}

	public static ManageSupportListAction priceLevels() {
		return new ManageSupportListAction(TYPE_PRICE_LEVELS);
	}

	public static ManageSupportListAction locations() {
		return new ManageSupportListAction(TYPE_LOCATIONS);
	}

	public static ManageSupportListAction currencyGroups() {
		return new ManageSupportListAction(TYPE_CURRENCY_GROUPS);
	}

	public static ManageSupportListAction salesTaxGroups() {
		return new ManageSupportListAction(TYPE_SALES_TAX_GROUPS);
	}

	public static ManageSupportListAction salesTaxItems() {
		return new ManageSupportListAction(TYPE_SALES_TAX_ITEMS);
	}

	public static ManageSupportListAction salesTaxGroup() {
		return new ManageSupportListAction(TYPE_SALES_TAX_GROUP);
	}

	public static ManageSupportListAction companies() {
		return new ManageSupportListAction(TYPE_COMPANIES);
	}
}
