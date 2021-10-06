package com.nitya.accounter.web.client.ui.settings;

import com.google.gwt.resources.client.ImageResource;
import com.nitya.accounter.web.client.ui.core.Action;
import com.nitya.accounter.web.client.ui.core.ViewManager;

public class AutomaticSequenceAction extends Action {

	public AutomaticSequenceAction() {
		super();
	}

	@Override
	public ImageResource getBigImage() {
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		return null;
	}

	// @Override
	// public ParentCanvas getView() {
	// return null;
	// }

	@Override
	public void run() {
		try {
			AutomaticSequenceDialog automaticSequenceDialog = new AutomaticSequenceDialog(
					messages.automaticSequencing(), "");
			ViewManager.getInstance().showDialog(automaticSequenceDialog);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	@Override
	public String getHistoryToken() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getHelpToken() {
		return "new-branding-theme";
	}

	@Override
	public String getText() {
		return messages.automaticSequencing();
	}

}
