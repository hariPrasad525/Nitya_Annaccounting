package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ViewManager;
import com.vimukti.accounter.web.client.ui.customers.AddMessageOrTaskDialog;

public abstract class AddMessageOrTaskAction extends Action {

	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void run() {
		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {

				AddMessageOrTaskDialog dialog = new AddMessageOrTaskDialog(
						messages.messagesAndTasks()) {

					@Override
					protected void onSuccess() {
						updateData();
					};
				};
				ViewManager.getInstance().showDialog(dialog);
			}

			public void onFailure(Throwable e) {
				Accounter.showError(Global.get().messages()
						.unableToshowtheview());
			}
		});

	}

	@Override
	public String getHistoryToken() {
		return "messageOrTask";
	}

	@Override
	public String getHelpToken() {
		// TODO Auto-generated method stub
		return null;
	}

	public abstract void updateData();

}
