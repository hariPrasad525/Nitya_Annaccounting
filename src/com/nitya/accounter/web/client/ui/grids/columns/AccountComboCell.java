package com.nitya.accounter.web.client.ui.grids.columns;

import java.util.ArrayList;

import com.google.gwt.cell.client.AbstractEditableCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.text.shared.SafeHtmlRenderer;
import com.google.gwt.text.shared.SimpleSafeHtmlRenderer;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.PopupPanel.PositionCallback;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientAccount;
import com.nitya.accounter.web.client.externalization.AccounterMessages;
import com.nitya.accounter.web.client.ui.StyledPanel;

public class AccountComboCell extends
		AbstractEditableCell<String, ClientAccount> {

	private static final int ESCAPE = 27;

	private final AccountComboTable table;
	private int offsetX = 10;
	private int offsetY = 10;
	private Object lastKey;
	private Element lastParent;
	private int lastIndex;
	private int lastColumn;
	private String lastValue;
	private PopupPanel panel;
	private final SafeHtmlRenderer<String> renderer;
	private ValueUpdater<String> valueUpdater;
	private boolean isAddNewRequired;

	/**
	 * Constructs a new AccountComboCell
	 * 
	 * @param accounts
	 * @param isAddNewRequired
	 */
	public AccountComboCell(ArrayList<ClientAccount> accounts,
			boolean isAddNewRequired) {
		this(accounts, isAddNewRequired, SimpleSafeHtmlRenderer.getInstance());
	}

	/**
	 * Constructs a new AccountComboCell
	 * 
	 * @param accounts
	 * @param isAddNewRequired
	 * @param renderer
	 *            a {@link SafeHtmlRenderer SafeHtmlRenderer<String>} instance
	 */
	public AccountComboCell(ArrayList<ClientAccount> accounts,
			boolean isAddNewRequired, SafeHtmlRenderer<String> renderer) {
		super("click", "keydown");
		if (renderer == null) {
			throw new IllegalArgumentException("renderer == null");
		}
		this.setAddNewRequired(isAddNewRequired);
		this.renderer = renderer;
		this.table = new AccountComboTable(accounts);
		this.panel = new PopupPanel(true, true) {
			@Override
			protected void onPreviewNativeEvent(NativePreviewEvent event) {
				if (Event.ONKEYUP == event.getTypeInt()) {
					if (event.getNativeEvent().getKeyCode() == ESCAPE) {
						// Dismiss when escape is pressed
						panel.hide();
					}
				}
			}
		};
		panel.addCloseHandler(new CloseHandler<PopupPanel>() {
			public void onClose(CloseEvent<PopupPanel> event) {
				lastKey = null;
				lastValue = null;
				lastIndex = -1;
				lastColumn = -1;
				if (lastParent != null && !event.isAutoClosed()) {
					// Refocus on the containing cell after the user selects a
					// value, but
					// not if the popup is auto closed.
					lastParent.focus();
				}
				lastParent = null;
			}
		});

		StyledPanel vPanel = new StyledPanel("vPanel");
		if (isAddNewRequired()) {
			vPanel.add(getAddNewLabel());
		}
		vPanel.add(table);
		panel.add(vPanel);

		// FIXME
		// Hide the panel and call valueUpdater.update when a date is selected
		// table.addValueChangeHandler(new ValueChangeHandler<ClientAccount>() {
		// public void onValueChange(ValueChangeEvent<ClientAccount> event) {
		// // Remember the values before hiding the popup.
		// Element cellParent = lastParent;
		// Date oldValue = lastValue;
		// Object key = lastKey;
		// int index = lastIndex;
		// int column = lastColumn;
		// panel.hide();
		//
		// // Update the cell and value updater.
		// Date date = event.getValue();
		// setViewData(key, date);
		// setValue(new Context(index, column, key), cellParent, oldValue);
		// if (valueUpdater != null) {
		// valueUpdater.update(date);
		// }
		// }
		// });
	}

	private Anchor getAddNewLabel() {
		AccounterMessages messages = Global.get().messages();
		Anchor addNew = new Anchor(messages.newPayee(messages.Account()));
		addNew.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

			}
		});
		return addNew;
	}

	@Override
	public boolean isEditing(Context context, Element parent, String value) {
		return lastKey != null && lastKey.equals(context.getKey());
	}

	@Override
	public void onBrowserEvent(Context context, Element parent, String value,
			NativeEvent event, ValueUpdater<String> valueUpdater) {
		super.onBrowserEvent(context, parent, value, event, valueUpdater);
		if ("click".equals(event.getType())) {
			onEnterKeyDown(context, parent, value, event, valueUpdater);
		}
	}

	@Override
	public void render(Context context, String value, SafeHtmlBuilder sb) {
		// Get the view data.
		Object key = context.getKey();
		ClientAccount viewData = getViewData(key);
		if (viewData != null && viewData.equals(value)) {
			clearViewData(key);
			viewData = null;
		}

		String s = null;
		if (viewData != null) {
			s = viewData.getName();
		} else if (value != null) {
			s = value;
		}
		if (s != null) {
			sb.append(renderer.render(s));
		}
	}

	@Override
	protected void onEnterKeyDown(Context context, Element parent,
			String value, NativeEvent event, ValueUpdater<String> valueUpdater) {
		this.lastKey = context.getKey();
		this.lastParent = parent;
		this.lastValue = value;
		this.lastIndex = context.getIndex();
		this.lastColumn = context.getColumn();
		this.valueUpdater = valueUpdater;

		// FIXME

		// ClientAccount viewData = getViewData(lastKey);
		// ClientAccount account = (viewData == null) ? lastValue : viewData;
		// table.setCurrentMonth(account);
		// table.setValue(account);
		panel.setPopupPositionAndShow(new PositionCallback() {
			public void setPosition(int offsetWidth, int offsetHeight) {
				panel.setPopupPosition(lastParent.getAbsoluteLeft() + offsetX,
						lastParent.getAbsoluteTop() + offsetY);
			}
		});
	}

	public boolean isAddNewRequired() {
		return isAddNewRequired;
	}

	public void setAddNewRequired(boolean isAddNewRequired) {
		this.isAddNewRequired = isAddNewRequired;
	}

}
