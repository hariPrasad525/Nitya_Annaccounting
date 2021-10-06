package com.nitya.accounter.web.client.ui.combo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.externalization.AccounterMessages;
import com.nitya.accounter.web.client.ui.forms.CustomComboItem;

public abstract class DropDownCombo<T> extends CustomComboItem {

	protected IAccounterComboSelectionChangeHandler<T> handler;

	protected AccounterMessages messages = Global.get().messages();
	private boolean isAddNewRequire;
	private int cols;
	private DropDownView popup;
	// ListGrid grid = null;

	protected List<T> comboItems = new ArrayList<T>();

	protected T selectedObject;

	List<T> maincomboItems = new ArrayList<T>();

	public DropDownCombo(String title, boolean isAddNewRequire, int noOfcols,
			String styleName) {
		super(title, styleName);
		createControls(title, isAddNewRequire, noOfcols);
		init();
	}

	private void createControls(String title, boolean isAddNewRequire,
			int noOfcols) {
		this.isAddNewRequire = isAddNewRequire;
		this.cols = noOfcols;
		setTitle(title);
		this.addStyleName("custom-combo");
		this.addStyleName("dropdown-button");

		// this.setWidth("99.5%");
		popup = GWT.create(DropDownView.class);
		popup.init(this, comboItems, isAddNewRequire);

		ClickHandler clickHandler = new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				if (isEnabled()) {
					showPopup();
				}
			}
		};
		addClickHandler(clickHandler);

		popup.addEscapEventHandler(new EscapEventHandler() {

			@Override
			public void onEscap(KeyDownEvent event) {
				popup.hide();
				textBox.setFocus(true);
			}
		});
		textBox.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				switch (event.getNativeKeyCode()) {
				case KeyCodes.KEY_ENTER:
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
				case KeyCodes.KEY_ESCAPE:
					break;
				case KeyCodes.KEY_BACKSPACE:
					String text = textBox.getText();
					if (!text.isEmpty()) {
						text = text.substring(0, text.length() - 1);
					}
					textBox.setText(text);
				default:
					selectFirstItem();
				}
			}
		});
		textBox.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				// Scheduler.get().scheduleFixedDelay(new RepeatingCommand() {
				//
				// @Override
				// public boolean execute() {
				Scheduler.get().scheduleDeferred(new ScheduledCommand() {

					@Override
					public void execute() {
						if (popup.isShowing()) {
							Scheduler.get().scheduleDeferred(this);
							return;
						}
						if (selectedObject == null
								|| !getValue().equals(
										getFullDisplayName(selectedObject))) {
							// setValue("");
							// selectedObject = null;
							setRelatedComboItem((String) getValue());
						}
					}
				});

				// return false;
				// }
				// }, 100);// We need to do it after a delay as clicking on the
				// // popup also causes blur
			}
		});

		this.removeStyleName("gwt-TextBox");
	}

	protected void selectFirstItem() {
		String val1 = textBox.getText().toLowerCase();
		List<T> autocompleteItems = getMatchedComboItems(val1);
		if (!autocompleteItems.isEmpty() && !val1.isEmpty()) {
			String text = textBox.getText().toLowerCase();
			String displayValue = getDisplayName(autocompleteItems.get(0));
			textBox.setText(displayValue);
			int len = displayValue.length() - text.length();
			int pos = displayValue.toLowerCase().indexOf(text);
			if (len > 0) {
				textBox.setSelectionRange(pos + text.length(), len);
			}
		}
		comboItems.clear();
		comboItems.addAll(autocompleteItems);
		popup.setList(autocompleteItems);
		showPopup();
		textBox.setFocus(true);

	}

	protected void showPopup() {
		if (!DropDownCombo.this.isEnabled())
			return;

		if (!isAddNewRequire && comboItems.isEmpty())
			return;
		int x = getMainWidget().getAbsoluteLeft();
		int y = getMainWidget().getAbsoluteTop() + 27;
		//

		popup.show(x, y);

	}

	void resetComboList() {
		if (!maincomboItems.isEmpty()) {
			initCombo(maincomboItems);
		}
	}

	/**
	 * Override this method to do anything before the other overridden methods
	 * are called from constructor.
	 */
	protected void init() {
	}

	public void setWidth(String width) {
		super.setWidth(width);

	}

	/**
	 * "" Should be Called only Once For Re-Initializing use, setComboItems
	 * 
	 * @param list
	 */
	public void initCombo(List<T> list) {

		comboItems.clear();
		if (list != null) {
			if (shouldSortData()) {
				Collections.sort(list, new Comparator<T>() {

					@Override
					public int compare(T obj1, T obj2) {
						String name = getDisplayName(obj1) != null ? getDisplayName(obj1).toLowerCase(): "";
						String name1 = getDisplayName(obj2) !=null ? getDisplayName(obj2).toLowerCase() : "";
						return name.compareToIgnoreCase(name1);
					}
				});
			}
			comboItems = new ArrayList<T>(list);
		}
		List<T> newList = new ArrayList<T>(comboItems);
		popup.setList(newList);
		setComboItem(selectedObject);
		maincomboItems.clear();
		maincomboItems.addAll(comboItems);

	}

	protected boolean shouldSortData() {
		return true;
	}

	public List<T> getComboItems() {
		return comboItems;
	}

	public void addItem(T object) {
		// checks the object is conains in comboitems
		checkObject(object);
	}

	private void checkObject(T object) {
		if (object == null)
			return;

		if (!comboItems.contains(object)) {
			comboItems.add(object);
			popup.add(object);

		}

	}

	/**
	 * This method is invoked from AbstractBaseView to setPreviousOutput
	 * 
	 * @param obj
	 */

	public void addItemThenfireEvent(T obj) {
		this.initCombo(new ArrayList<T>(this.comboItems));
		setComboItem(obj);
		if (handler != null) {
			handler.selectedComboBoxItem(obj);
		}
	}

	@Override
	public void setValue(Object value) {

		if (value == null) {
			super.setValue("");
		} else {
			if (!value.equals(messages
					.comboDefaultAddNew(getDefaultAddNewCaption())))
				super.setValue(value);
		}
	}

	/**
	 * This method is invoked when a new item is added to the listbox
	 * 
	 * @param object
	 *            -- object to be added
	 * @param addIfNotinList
	 */

	public void setComboItem(T obj) {
		if (selectedObject == obj) {
			return;
		}
		selectedObject = obj;
		if (comboItems != null && obj != null) {
			addComboItem(obj);
			int index = comboItems.indexOf(obj);
			if (isAddNewRequire)
				setSelectedItem(obj, index + 1);
			else
				setSelectedItem(obj, index);
		} else {
			addItem(obj);
			setValue("");
		}

	}

	/**
	 * This method will be invoked when a new item about to add in effect of
	 * "addNewXXX" option
	 * 
	 * @param obj
	 */
	public void addComboItem(T obj) {

		if (!hasItem(obj)) {
			addItem(obj);
		}
	}

	public boolean hasItem(T object) {
		return this.comboItems.contains(object);
	}

	public IAccounterComboSelectionChangeHandler<T> getHandler() {
		return handler;
	}

	public void addSelectionChangeHandler(
			IAccounterComboSelectionChangeHandler<T> handler) {
		this.handler = handler;
	}

	protected abstract String getColumnData(T object, int col);

	protected abstract String getDisplayName(T object);

	public void setNoColumns(int cols) {
		this.cols = cols;
	}

	public abstract String getDefaultAddNewCaption();

	public abstract void onAddNew();

	public void changeValue(int rowIndex) {
		rowIndex = this.popup.updateIndex(rowIndex);
		// int index = listbox.getSelectedIndex();

		IAccounterComboSelectionChangeHandler<T> handler = getHandler();
		if (rowIndex > 0)
			selectedObject = comboItems.get(rowIndex
					- (isAddNewRequire ? 1 : 0));
		switch (rowIndex) {
		case -1:
			if (popup.isShowing())
				popup.hide();
			setSelectedItem(selectedObject, rowIndex);
			break;
		case 0:
			if (isAddNewRequire) {
				if (popup.isShowing())
					popup.hide();
				// setValue(getDefaultAddNewCaption());
				onAddNew();
			} else {
				selectedObject = comboItems.get(rowIndex
						- (isAddNewRequire ? 1 : 0));
				if (handler != null) {
					handler.selectedComboBoxItem(selectedObject);
				}
				setSelectedItem(selectedObject, rowIndex);
				if (popup.isShowing())
					popup.hide();
			}

			break;

		default:

			if (handler != null) {
				selectedObject = comboItems.get(rowIndex
						- (isAddNewRequire ? 1 : 0));
				handler.selectedComboBoxItem(selectedObject);
				setSelectedItem(selectedObject, rowIndex);
				if (popup.isShowing())
					popup.hide();

			}

			break;
		}

		if (handler == null) {
			if (popup.isShowing())
				popup.hide();
			setSelectedItem(selectedObject, rowIndex);
		}
		// if (grid != null) {
		// grid.remove(getMainWidget());
		// setSelectedItem(selectedObject, 0);
		// if (popup.isShowing())
		// popup.hide();
		// }
	}

	protected void setSelectedItem(T obj, int row) {

		if (obj == null) {
			setValue("");
			return;
		}

		String displayName = getFullDisplayName(obj);

		setValue(displayName);
	}

	private String getFullDisplayName(T obj) {
		String displayName = "";
		if (cols == 0) {
			displayName = getDisplayName(obj);
		} else {
			for (int i = 0; i < cols; i++) {
				displayName += getColumnData(obj, i);
				if (i < cols - 1)
					displayName += " - ";
			}
		}
		return displayName;
	}

	// public void setGrid(ListGrid grid) {
	// this.grid = grid;
	// }

	public void removeComboItem(T obj) {

		int index = getObjectIndex(obj);
		if (index >= 0) {
			T selectedValue = getSelectedValue();
			comboItems.remove(index);
			popup.remove(obj);
			if (!comboItems.contains(selectedValue)) {
				// select the first one
				if (!comboItems.isEmpty()) {
					setSelectedItem(comboItems.get(0), isAddNewRequire ? 1 : 0);
				} else {
					setSelectedItem(null, 0); // row optional here.
				}
			}

		}
     }

	protected int getObjectIndex(T coreObject) {
		return comboItems.indexOf(coreObject);
	}

	public void updateComboItem(T coreObject) {
		if (getObjectIndex(coreObject) != -1) {
			removeComboItem(coreObject);
			addComboItem(coreObject);
		}
	}

	public T getSelectedValue() {
		return selectedObject;
		}
	@Override
	public void setEnabled(boolean b) {
		if (!b) {
			this.addStyleName("disabled");
			this.getMainWidget().addStyleName("dropdown-disabled");
		} else {
			this.removeStyleName("disabled");
			this.getMainWidget().removeStyleName("dropdown-disabled");
		}
		super.setEnabled(b);
	}

	public void setWidth(int width) {
		// this.getMainWidget().setWidth(width + "%");
	}

	public void setPopupWidth(String width) {
		// this.panel.setWidth(width);
	}

	private void setRelatedComboItem(final String value) {
		List<T> combos;
		if (this instanceof ClassListCombo) {
			String[] split = value.split(":");
			combos = getComboitemsByName(split[split.length - 1]);
		} else {
			combos = getComboitemsByName(value);
		}
		if (!combos.isEmpty()) {
			int index = comboItems.indexOf(combos.get(0));
			if (isAddNewRequire) {
				index++;
			}
			changeValue(index);
		} else {
			if (!value.isEmpty()) {
				selectionFaildOnClose();
			}
			selectNone();
		}
	}

	/**
	 * this will be called when {@link #isAddNewRequire} value is true, and
	 * noting has been selected for the entered text on close.
	 */
	protected void selectNone() {
		textBox.setValue("");
		selectedObject = null;
	}

	/**
	 * this will be called when {@link #isAddNewRequire} value is true, and
	 * noting has been selected for the entered text on close.
	 */
	protected void selectionFaildOnClose() {
		// NOTHING TO DO HERE
	}

	private List<T> getMatchedComboItems(String val) {
		List<T> autocompleteItems = new ArrayList<T>();
		for (T t : maincomboItems) {
			String displayName = getDisplayName(t);
			displayName = displayName.toLowerCase();
			if (displayName.startsWith(val)) {
				autocompleteItems.add(t);
			}
		}
		return autocompleteItems;
	}

	protected List<T> getComboitemsByName(String value) {
		List<T> autocompleteItems = new ArrayList<T>();
		for (T t : maincomboItems) {
			String displayName = getDisplayName(t);
			if (displayName.equalsIgnoreCase(value)) {
				autocompleteItems.add(t);
			}
		}
		return autocompleteItems;
	}

	private String getDisplayNameForAccountVatCode(T obj) {
		String displayName = "";
		for (int i = 0; i < cols; i++) {
			displayName += getColumnData(obj, i);
			if (i < cols - 1)
				displayName += " - ";
		}
		return displayName;
	}

	protected void onAddAllInfo(String text) {

	}

	protected T getQuickAddData(String text) {
		return null;
	}

	public int getNoOfCols() {
		return cols;
	}
}
