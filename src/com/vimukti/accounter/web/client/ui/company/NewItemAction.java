package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.ui.*;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ViewManager;

/**
 * 
 * @author Raj Vimal modified by Rajesh.A
 */

public class NewItemAction extends Action<ClientItem> {

	int type;
	private boolean forCustomer;
	private String itemName;
	private boolean fromCompany;
	private boolean isItemEditable;
	private boolean frmAnyView;

	public NewItemAction() {
		super();
		fromCompany = true;
	}

	public NewItemAction(boolean forCustomer) {
		super();
		this.forCustomer = forCustomer;
	}

	public NewItemAction(ClientItem item,
			AccounterAsyncCallback<Object> callback, boolean forCustomer) {
		super();
		this.catagory = messages.company();
		this.forCustomer = forCustomer;
		fromCompany = true;
		// this.baseView = baseView;
	}

	public NewItemAction(boolean isGeneratedFromCustomer, int type2) {
		this();
		this.type = type2;
		this.forCustomer = isGeneratedFromCustomer;
	}

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	public void runAsync(final ClientItem data, final Boolean isDependent) {
		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				ClientCompany company = Accounter.getCompany();
				boolean sellServices = company.getPreferences()
						.isSellServices();
				boolean sellProducts = company.getPreferences()
						.isSellProducts();
				if (sellProducts && sellServices) {
					if (type == 0 && data == null) {
						SelectItemTypeDialog dialog = new SelectItemTypeDialog(
								forCustomer);
						dialog.setFrmAnyView(isFrmAnyView());
						dialog.setDependent(isDependent);
						dialog.setCallback(getCallback());
						dialog.setItemname(itemName);
						ViewManager.getInstance().showDialog(dialog);
					} else {
						if (data != null) {
							type = data.getType();
						}
						ItemView view = GWT.create(ItemView.class);
						view.setType(type);
						view.setGeneratedFromCustomer(forCustomer);
						view.setItemName(itemName);
						MainFinanceWindow.getViewManager().showView(view, data,
								isDependent, NewItemAction.this);
						if (isItemEditable) {
							view.onEdit();
						}
					}
				} else if (sellServices) {
					ItemView view = GWT.create(ItemView.class);
					view.setType(Item.TYPE_SERVICE);
					view.setGeneratedFromCustomer(forCustomer);
					view.setItemName(itemName);
					MainFinanceWindow.getViewManager().showView(view, data,
							isDependent, NewItemAction.this);
					if (isItemEditable) {
						view.onEdit();
					}
				} else if (sellProducts) {
					if (Accounter.getCompany().getPreferences()
							.isInventoryEnabled()) {
						if (type == 0 && data == null) {
							SelectItemTypeDialog dialog = new SelectItemTypeDialog(
									forCustomer);
							dialog.setFrmAnyView(isFrmAnyView());
							dialog.setDependent(isDependent);
							dialog.setCallback(getCallback());
							dialog.setItemname(itemName);
							ViewManager.getInstance().showDialog(dialog);
						} else {
							if (data != null) {
								type = data.getType();
							}
							ItemView view = GWT.create(ItemView.class);
							view.setType(type);
							view.setGeneratedFromCustomer(forCustomer);
							view.setItemName(itemName);
							MainFinanceWindow.getViewManager().showView(view,
									data, isDependent, NewItemAction.this);
							if (isItemEditable) {
								view.onEdit();
							}
						}

					} else {
						ItemView view = GWT.create(ItemView.class);
						view.setType(ClientItem.TYPE_NON_INVENTORY_PART);
						view.setGeneratedFromCustomer(forCustomer);
						view.setItemName(itemName);
						MainFinanceWindow.getViewManager().showView(view, data,
								isDependent, NewItemAction.this);
						if (isItemEditable) {
							view.onEdit();
						}
					}

				}

			}

			public void onFailure(Throwable e) {
				Accounter.showError(Global.get().messages()
						.unableToshowtheview());
			}
		});
		// AccounterAsync.createAsync(new CreateViewAsyncCallback() {
		//
		// @Override
		// public void onCreated() {
		//
		// }
		//
		// });
	}

	public ImageResource getBigImage() {
		// NOTHING TO DO.
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().newItem();
	}

	public void setType(int type) {
		this.type = type;
	}

	@Override
	public String getHistoryToken() {

		if (type == ClientItem.TYPE_INVENTORY_PART) {
			return "inventoryItem";
		} else if (forCustomer) {
			return "newItemCustomer";
		}
		return "newItemSupplier";

	}

	@Override
	public String getCatagory() {
		if (fromCompany) {
			return messages.company();
		} else if (forCustomer) {
			return Global.get().Customer();
		} else {
			return Global.get().Vendor();
		}
	}

	@Override
	public String getHelpToken() {
		return "customer-item";
	}

	public void setItemText(String text) {
		this.itemName = text;

	}

	@Override
	public String getText() {
		if (type == ClientItem.TYPE_INVENTORY_PART) {
			return messages.newInventoryItem();
		} else if (forCustomer) {
			return messages.newPayeeItem(Global.get().Customer());
		}
		return messages.newPayeeItem(Global.get().Vendor());
	}

	public void setisItemEditable(boolean isItemViewEditable) {
		this.isItemEditable = isItemViewEditable;
	}

	public boolean isFrmAnyView() {
		return frmAnyView;
	}

	public void setFrmAnyView(boolean frmAnyView) {
		this.frmAnyView = frmAnyView;
	}
}
