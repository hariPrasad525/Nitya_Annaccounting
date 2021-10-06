package com.nitya.accounter.web.client.ui.company;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.nitya.accounter.web.client.AccounterAsyncCallback;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientBudget;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.MainFinanceWindow;
import com.nitya.accounter.web.client.ui.core.Action;

public class NewBudgetAction extends Action<ClientBudget> {

	boolean edit;

	public NewBudgetAction() {
		super();
		this.catagory = Global.get().messages().budget();
		super.setToolTip(Global.get().messages().budget());
	}

	public NewBudgetAction(ClientBudget budget,
			AccounterAsyncCallback<Object> callback) {
		super();
		this.catagory = Global.get().messages().budget();
	}

	@Override
	public void run() {

		if (listData != null) {
			runAsync(listData);
		} else {
			runAsync(data, isDependent);
		}
	}

	private void runAsync(final List<ClientBudget> listData) {

		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
					NewBudgetCellTableView view = new NewBudgetCellTableView(listData);
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, NewBudgetAction.this);
				
			}

			public void onFailure(Throwable e) {
				Accounter.showError(Global.get().messages()
						.unableToshowtheview());
			}
		});
//		AccounterAsync.createAsync(new CreateViewAsyncCallback() {
//
//			@Override
//			public void onCreated() {
//			
//
//			}
//
//		});

	}

	public void runAsync(final Object data, final Boolean isDependent) {

		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				NewBudgetCellTableView view = new NewBudgetCellTableView();
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, NewBudgetAction.this);
				
			}

			public void onFailure(Throwable e) {
				Accounter.showError(Global.get().messages()
						.unableToshowtheview());
			}
		});
//		AccounterAsync.createAsync(new CreateViewAsyncCallback() {
//
//			@Override
//			public void onCreated() {
//				
//
//			}
//
//		});
	}

	// @Override
	// public ParentCanvas getView() {
	// return this.view;
	// }

	public ImageResource getBigImage() {
		// NOTHING TO DO.
		return null;
	}

	public ImageResource getSmallImage() {
		return null;
	}

	// @Override
	// public String getImageUrl() {
	// return "/images/new_customer.png";
	// }

	@Override
	public String getHistoryToken() {
		return "newBudget";
	}

	@Override
	public String getHelpToken() {
		return "add-Budget";
	}

	public void runEdit(Object budgetItem) {
		edit = true;
		// runAsync(edit, budgetItem);

	}

	@Override
	public String getText() {
		return messages.newBudget();
	}
}
