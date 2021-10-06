package com.nitya.accounter.web.client.translate;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.RootPanel;

public class TranslationEntryPoint implements EntryPoint {

	public static TranslateServiceAsync translateService;

	private static final String TRANSLATE_SERVICE_ENTRY_POINT = "/do/translate/rpc/service";

	@Override
	public void onModuleLoad() {

		if (translateService == null) {
			translateService = (TranslateServiceAsync) GWT
					.create(TranslateService.class);
			((ServiceDefTarget) translateService)
					.setServiceEntryPoint(TRANSLATE_SERVICE_ENTRY_POINT);
		}
		TranslationWizard translationWizard = new TranslationWizard();
//		translationWizard.setWidth("100%");
		RootPanel.get("translationPanel").add(translationWizard);

	}

}
