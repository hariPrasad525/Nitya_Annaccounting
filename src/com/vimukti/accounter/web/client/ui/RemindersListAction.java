package com.vimukti.accounter.web.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientReminder;
import com.vimukti.accounter.web.client.ui.core.Action;

public class RemindersListAction extends Action<ClientReminder> {

	public RemindersListAction() {
		this.catagory = messages.company();
	}

	@Override
	public void run() {
		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				RemindersListView view = new RemindersListView();

				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, RemindersListAction.this);
				
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
//			}
//		});
	}

	@Override
	public ImageResource getBigImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getHistoryToken() {
		return "recurringReminders";
	}

	@Override
	public String getHelpToken() {
		return "reminders-list";
	}

	@Override
	public String getText() {
		return messages.remindersList();
	}
}
