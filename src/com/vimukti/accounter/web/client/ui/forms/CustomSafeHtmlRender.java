package com.vimukti.accounter.web.client.ui.forms;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.text.shared.SafeHtmlRenderer;

public class CustomSafeHtmlRender implements SafeHtmlRenderer<SafeHtml> {
	private static CustomSafeHtmlRender instance;

	public static CustomSafeHtmlRender getInstance() {
		if (instance == null) {
			instance = new CustomSafeHtmlRender();
		}
		return instance;
	}

	private CustomSafeHtmlRender() {
	}

	@Override
	public SafeHtml render(SafeHtml object) {
		return object;
	}

	@Override
	public void render(SafeHtml object, SafeHtmlBuilder builder) {
		builder.append(object);
	}

}
