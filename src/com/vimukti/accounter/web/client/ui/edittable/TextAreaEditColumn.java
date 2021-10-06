package com.vimukti.accounter.web.client.ui.edittable;

import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBoxBase;

public abstract class TextAreaEditColumn<T> extends TextEditColumn<T> {

	@Override
	protected TextBoxBase createWidget() {
		final TextArea textArea = new TextArea();
		textArea.addKeyDownHandler(new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent event) {
//				textArea.setHeight("2px");
				updateHeight(textArea.getElement());
			}
		});
		return textArea;
	}

	private native void updateHeight(Element text)/*-{
		if (text.scrollHeight > text.clientHeight) {
			text.style.height = text.scrollHeight + "px";
		}

	}-*/;

}
