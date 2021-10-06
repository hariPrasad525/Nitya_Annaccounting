package com.nitya.accounter.web.client.ui.forms;

import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.TextBox;

public class TextBoxItem extends TextBox {

	private KeyPressListener keyPressHandler;

	public TextBoxItem() {
		sinkEvents(Event.ONBLUR | Event.ONCHANGE | Event.ONKEYPRESS);

		this.addKeyPressHandler(new KeyPressHandler() {

			@Override
			public void onKeyPress(KeyPressEvent event) {
				if (keyPressHandler != null)
					keyPressHandler.onKeyPress(event.getNativeEvent()
							.getKeyCode());
			}
		});
	}

	public void onBrowserEvent(Event event) {
		switch (DOM.eventGetType(event)) {
		case Event.ONKEYPRESS:
			if (this.getValue().length() > 50) {
				this.setValue(this.getValue().substring(0, 49));
			}
			break;

		default:
			break;
		}
		super.onBrowserEvent(event);
	}

	public void setKeyPressHandler(KeyPressListener keyPressHandler) {
		this.keyPressHandler = keyPressHandler;
	}

	public interface KeyPressListener {
		public void onKeyPress(int keyCode);
	}
}
