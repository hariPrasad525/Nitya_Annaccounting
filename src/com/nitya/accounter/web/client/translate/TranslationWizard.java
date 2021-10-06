package com.nitya.accounter.web.client.translate;

import com.google.gwt.user.client.ui.FlowPanel;

public class TranslationWizard extends FlowPanel {

	public TranslationWizard() {
		StatusPanel statusPanel = new StatusPanel(TranslationWizard.this);
		this.add(statusPanel);
	}
}
