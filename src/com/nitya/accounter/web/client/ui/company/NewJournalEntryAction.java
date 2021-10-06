package com.nitya.accounter.web.client.ui.company;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.nitya.accounter.web.client.AccounterAsyncCallback;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientJournalEntry;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.MainFinanceWindow;
import com.nitya.accounter.web.client.ui.core.Action;

public class NewJournalEntryAction extends Action {

	public NewJournalEntryAction() {
		super();
		this.catagory = messages.company();
	}

	public NewJournalEntryAction(ClientJournalEntry journalEntry,
			AccounterAsyncCallback<Object> callback) {
		super();
		this.catagory = messages.company();
	}

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	public void runAsync(final Object data, final Boolean isEditable) {
		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				JournalEntryView view = GWT.create(JournalEntryView.class);

				MainFinanceWindow.getViewManager().showView(view, data,
						isEditable, NewJournalEntryAction.this);

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
		return Accounter.getFinanceMenuImages().newJournalEntry();
	}

	@Override
	public String getHistoryToken() {
		return "newJournalEntry";
	}

	@Override
	public String getHelpToken() {
		return "new_journal-entry";
	}

	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return messages.newJournalEntry();
	}
}
