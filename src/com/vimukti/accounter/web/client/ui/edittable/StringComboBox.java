package com.vimukti.accounter.web.client.ui.edittable;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;

public class StringComboBox<T> extends FlowPanel implements
		RowSelectHandler<String> {
	private T row;
	private String value;
	private PopupPanel popupPanel;
	private TextBox textBox;
	private ComboChangeHandler<T, String> changeHandler;
	private StringListDropDownTable dropDown;
	private ScrollPanel scrollPanel;
	private SimplePanel downarrowpanel;

	public StringComboBox() {
		this.addStyleName("comboBox");
		textBox = new TextBox();
		textBox.sinkEvents(0);// Check and remove
		this.add(textBox);
		downarrowpanel = new SimplePanel();

		downarrowpanel.addStyleName("downarrow-button");

		downarrowpanel.addDomHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (textBox.isEnabled())
					showPopup();
			}
		}, ClickEvent.getType());
		this.add(downarrowpanel);

		textBox.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				showPopup();
			}
		});
		textBox.addKeyDownHandler(new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent event) {
				switch (event.getNativeEvent().getKeyCode()) {
				case KeyCodes.KEY_TAB:
					popupPanel.hide();
					editComplete();
					break;
				case KeyCodes.KEY_DOWN:
					textBox.setText("");
					showPopup();
					dropDown.downKeyPress();
					break;
				case KeyCodes.KEY_UP:
					textBox.setText("");
					showPopup();
					dropDown.upKeyPress();
					return;
				}
			}
		});
		textBox.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				switch (event.getNativeKeyCode()) {
				case KeyCodes.KEY_ENTER:
					popupPanel.hide();
					break;
				case KeyCodes.KEY_BACKSPACE:
				case KeyCodes.KEY_DELETE:
				case KeyCodes.KEY_UP:
				case KeyCodes.KEY_DOWN:
				case KeyCodes.KEY_ALT:
				case KeyCodes.KEY_CTRL:
				case KeyCodes.KEY_END:
				case KeyCodes.KEY_HOME:
				case KeyCodes.KEY_LEFT:
				case KeyCodes.KEY_PAGEDOWN:
				case KeyCodes.KEY_PAGEUP:
				case KeyCodes.KEY_RIGHT:
				case KeyCodes.KEY_SHIFT:
					break;
				case KeyCodes.KEY_ESCAPE:
					popupPanel.hide();
					break;
				default:
					showPopup();
					dropDown.updateSelection(textBox.getText().toLowerCase());
				}
			}
		});
		popupPanel = new PopupPanel(true, false);
		popupPanel.addCloseHandler(new CloseHandler<PopupPanel>() {

			@Override
			public void onClose(CloseEvent<PopupPanel> event) {
				editComplete();
			}
		});
		scrollPanel = new ScrollPanel();
		popupPanel.add(scrollPanel);
	}

	private void editComplete() {
		String text = textBox.getText();
		String filteredValue = dropDown.getFilteredValue(text.toLowerCase());
		if (filteredValue != null) {
			setValue(filteredValue);
			if (changeHandler != null && value != null) {
				changeHandler.onChange(row, value);
			}
		} else {
			if (changeHandler != null) {
				if (!text.isEmpty()) {
					textBox.setText("");
					changeHandler.onAddNew(text);
				}
			}
		}
	}

	private void showPopup() {
		if (popupPanel.isShowing()) {
			return;
		}
		scrollPanel.clear();
		scrollPanel.add(dropDown);
		dropDown.addRowSelectHandler(this);
		dropDown.selectRow(value, false);
		int x = textBox.getAbsoluteLeft();
		int y = textBox.getAbsoluteTop() + textBox.getOffsetHeight();

		int clientwidth = Window.getClientWidth();
		int clientHeight = Window.getClientHeight();

		int popupWdth = popupPanel.getOffsetWidth();
		int popupHeight = popupPanel.getOffsetHeight();

		popupPanel.getElement().setAttribute("style",
				"min-width:" + textBox.getOffsetWidth() + "px");
		scrollPanel.addStyleName("combo-scroll-panel");
		// popupPanel.setHeight("100px");

		if ((x + popupWdth) > clientwidth) {
			x = x - (popupPanel.getOffsetWidth() - this.getOffsetWidth());
		}

		if ((y + popupHeight) > clientHeight) {
			y = y - (popupPanel.getOffsetHeight() - textBox.getOffsetHeight());
		}

		popupPanel.setPopupPosition(x, y);
		popupPanel.show();
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		if (value != null) {
			textBox.setText(value);
		} else {
			textBox.setText("");
		}
		this.value = value;
	}

	public void setComboChangeHandler(
			ComboChangeHandler<T, String> changeHandler) {
		this.changeHandler = changeHandler;
	}

	public StringListDropDownTable getDropDown() {
		return dropDown;
	}

	public void setDropDown(StringListDropDownTable table) {
		this.dropDown = table;
	}

	public void setDesable(boolean desable) {
		textBox.setEnabled(!desable);
		if (desable) {
			downarrowpanel.addStyleName("editTable_disable");
		} else {
			downarrowpanel.removeStyleName("editTable_disable");
		}
	}

	public T getRow() {
		return row;
	}

	public void setRow(T row) {
		this.row = row;
	}

	@Override
	public void onRowSelect(String selectedObj, boolean isClicked) {
		value = selectedObj;
		if (selectedObj != null) {
			String text = textBox.getText().toLowerCase();
			String displayValue = selectedObj;
			textBox.setText(displayValue);
			int len = displayValue.length() - text.length();
			int pos = displayValue.toLowerCase().indexOf(text);
			if (len > 0) {
				textBox.setSelectionRange(pos + text.length(), len);
			}
			if (isClicked) {
				if (popupPanel.isShowing()) {
					popupPanel.hide();
				} else {
					editComplete();
				}
			}
		} else {
			if (isClicked) {
				popupPanel.hide();
			}
		}
	}

}
