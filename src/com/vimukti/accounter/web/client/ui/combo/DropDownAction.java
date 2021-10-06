package com.vimukti.accounter.web.client.ui.combo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class DropDownAction<T> extends Action<T> {

	protected DropDownView<T> view;

	public DropDownAction() {
		super();
	}

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	public void runAsync(final Object data, final Boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {
				view = GWT.create(DropDownView.class);
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, DropDownAction.this);

			}

		});
	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return null;
	}

	@Override
	public String getHistoryToken() {
		return "dropDownAction";
	}

	@Override
	public String getHelpToken() {
		return "dropDownAction";
	}

	@Override
	public String getText() {
		return messages.newAccount();
	}
}
