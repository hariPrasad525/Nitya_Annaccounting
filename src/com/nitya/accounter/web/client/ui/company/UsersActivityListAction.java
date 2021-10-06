package com.nitya.accounter.web.client.ui.company;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientActivity;
import com.nitya.accounter.web.client.core.Features;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.MainFinanceWindow;
import com.nitya.accounter.web.client.ui.core.Action;

public class UsersActivityListAction extends Action<ClientActivity> {

	public UsersActivityListAction() {
		super();
		this.catagory = messages.company();
	}

	@Override
	public void run() {
		if (Accounter.hasPermission(Features.USER_ACTIVITY)) {
			runAsynce(data, isDependent);
		} else {
			if (!isCalledFromHistory) {
				Accounter.showSubscriptionWarning();
			}
		}
	}

	public void runAsynce(final Object data, final boolean isDependent) {
		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				UsersActivityListView view = new UsersActivityListView();
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, UsersActivityListAction.this);
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
//
//		});
	}

	@Override
	public ImageResource getBigImage() {
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		return null;
	}

	@Override
	public String getHistoryToken() {
		return "userActivity";
	}

	@Override
	public String getHelpToken() {
		return "users-activity-log";
	}

	@Override
	public String getText() {
		return messages.usersActivityLogTitle();
	}

}
