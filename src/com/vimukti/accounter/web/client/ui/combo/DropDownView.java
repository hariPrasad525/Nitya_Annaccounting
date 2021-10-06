package com.vimukti.accounter.web.client.ui.combo;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.view.client.ListDataProvider;
import com.vimukti.accounter.web.client.ui.core.AbstractView;

public class DropDownView<T> extends AbstractView<T> {
	private PopupPanel popup;
	private DropDownTable<T> dropDown;
	protected T selectedObject;
	protected ListDataProvider<T> dataProvider = new ListDataProvider<T>();

	protected DropDownCombo<T> combo;
	private boolean isAddNewRequire;

	public DropDownView() {
		// Nothing to do as it is created using GWT.create
	}

	public void init(final DropDownCombo<T> combo, List<T> comboItems,
			boolean isAddNewRequire) {
		this.isAddNewRequire = isAddNewRequire;
		this.combo = combo;
		dropDown = new DropDownTable<T>(combo) {

			@Override
			protected void onRowSelect(int row) {
				if (this.combo.comboItems.size() >= row) {
					selectedObject = this.combo.comboItems.get(row);
				}
			}

			@Override
			protected String getColumnValue(T object, int col) {
				if (object.equals("addNewCaption")) {
					if (combo.getNoOfCols() > 1)
						return (col == 1) ? combo.messages
								.comboDefaultAddNew(combo
										.getDefaultAddNewCaption()) : "  ";
					else
						return combo.messages.comboDefaultAddNew(combo
								.getDefaultAddNewCaption());
				}
				return combo.getColumnData(object, col);
			}
		};

		ScrollPanel panel = new ScrollPanel();
		panel.getElement().removeAttribute("style");
		panel.addStyleName("dropdownTable");
		panel.add(dropDown);

		List<T> list = new ArrayList<T>(comboItems);
		if (isAddNewRequire) {
			list.add(0, getAddNewRow());
		}
		dataProvider.setList(list);
		dataProvider.addDataDisplay(dropDown);

		popup = new PopupPanel(true) {
			@Override
			protected void onUnload() {
				super.onUnload();
				// dropDown.resetPopupStyle();
				combo.resetComboList();
			}

			@Override
			public void onBrowserEvent(Event event) {
				switch (DOM.eventGetType(event)) {
				case Event.ONMOUSEOUT:
					Element element = event.getTarget();
					if (this.getElement().equals(element)) {
						this.hide();
					}
					break;

				default:
					break;
				}
				super.onBrowserEvent(event);
			}
		};
		popup.add(panel);
		popup.setStyleName("popup");
		popup.getElement().setAttribute("id", "popuppaneldropdown");
		dropDown.getElement().getStyle().setCursor(Cursor.POINTER);
		combo.setKeyBoardHandler(new KeyPressHandler() {

			@Override
			public void onKeyPress(KeyPressEvent event) {
				if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_DOWN
						&& popup.isShowing()) {
					dropDown.setKeyboardSelected(0, true, true);

					// dropDown.setKeyboardSelected(++dropDown.selectedPosition,
					// true, true);
					// if (dropDown.selectedPosition == dropDown
					// .getDisplayedItems().size()) {
					// dropDown.selectedPosition = -1;
					// }
				} else if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_UP
						&& popup.isShowing()) {
					dropDown.setKeyboardSelected(dropDown.getDisplayedItems()
							.size() - 1, true, true);

					// int posi = dropDown.getDisplayedItems().size();
					// dropDown.setKeyboardSelected(posi
					// + (--dropDown.selectedPosition), true, true);
					// if ((dropDown.selectedPosition + dropDown
					// .getDisplayedItems().size()) == 0) {
					// dropDown.selectedPosition = 0;
					// }
				} else if ((event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE || event
						.getNativeEvent().getKeyCode() == KeyCodes.KEY_TAB)
						&& popup.isShowing()) {
					popup.hide();
				}
			}

		});
	}

	private T getAddNewRow() {
		return (T) "addNewCaption";
	}

	public boolean isShowing() {
		return popup.isShowing();
	}

	public void hide() {
		popup.hide();
	}

	public void show(int x, int y) {
		popup.setPopupPosition(x + 1, y);
		popup.show();
		int clientwidth = Window.getClientWidth();
		int popupWdth = popup.getWidget().getOffsetWidth();

		if ((x + popupWdth) > clientwidth) {
			x = x
					- (popup.getOffsetWidth() - combo.getMainWidget()
							.getOffsetWidth());
			popup.setPopupPosition(x + 1, y);
		}
	}

	public void setPopupPosition(int x, int y) {
		popup.setPopupPosition(x, y);
	}

	public void setList(List<T> newList) {
		if (this.isAddNewRequire) {
			newList.add(0, getAddNewRow());
		}
		int size = newList.size();
		dataProvider.setList(newList);
		dataProvider.refresh();
		dropDown.setRowCount(size);
		dropDown.setPageSize(size);
	}

	public void add(T obj) {
		dataProvider.getList().add(obj);
		dropDown.setRowCount(dataProvider.getList().size());
		dropDown.setPageSize(dropDown.getRowCount());
	}

	public void remove(T obj) {
		dataProvider.getList().remove(obj);
	}

	public void addEscapEventHandler(EscapEventHandler escapEventHandler) {
		dropDown.addEscapEventHandler(escapEventHandler);
	}

	@Override
	public void setFocus() {
	}

	@Override
	public void fitToSize(int height, int width) {

	}

	@Override
	public void onEdit() {

	}

	@Override
	public void printPreview() {

	}

	@Override
	public void print() {

	}

	public int updateIndex(int rowIndex) {
		return rowIndex;
	}

	@Override
	protected void init() {

	}
}
