package com.nitya.accounter.web.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.ui.core.Action;

public class BuildAssemblyAction extends Action {

	public BuildAssemblyAction() {
		super();
		this.catagory = messages.inventory();
	}

	@Override
	public String getText() {
		return messages.buildAssembly();
	}

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	private void runAsync(final Object data, final boolean isDependent) {
		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				BuildAssemblyView view = new BuildAssemblyView();
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, BuildAssemblyAction.this);
				
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
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		return null;
	}

	@Override
	public String getHistoryToken() {
		return "buildassembly";
	}

	@Override
	public String getHelpToken() {
		return null;
	}

}
