package com.vimukti.accounter.web.client.ui.combo;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.externalization.AccounterMessages2;
import com.vimukti.accounter.web.client.ui.Accounter;

/*
 This Combo can used for All Custom Combo SelectItems, like CustomerCombo,
 * Vendor,,,,etc.,
 * 
 * @author Malcom Fernandez
 */
public abstract class CustomCombo<T> extends DropDownCombo<T> {
	protected static AccounterMessages messages = Global.get().messages();
	protected static AccounterMessages2 messages2 = Global.get().messages2();

	public CustomCombo(String title, boolean isAddNewRequire, int noOfCols,
			String styleName) {
		super(title, isAddNewRequire, noOfCols, styleName);
		if (title != null) {
			int i = title.length();
			if (i > 5)
				if (title.substring(i - 1).equalsIgnoreCase("s"))
					title = title.replace("s", "");
				else if (title.substring(i - 4, i).equalsIgnoreCase(
						messages.name()))
					title = title.replace(messages.name(), "").toLowerCase();
			if (isAddNewRequire)
				super.setToolTip(messages
						.selectWhichWeHaveInOurCompanyOrAddNew(title));
			else
				super.setToolTip(messages.selectWhichWeHaveInOurCompany(title));
		}
	}

	public CustomCombo(String title, String styleName) {
		super(title, true, 1, styleName);
		if (title != null) {
			int i = title.length();
			if (i > 5)
				if (title.substring(i - 1).equalsIgnoreCase("s"))
					title = title.replace("s", "");
				else if (title.substring(i - 4, i).equalsIgnoreCase(
						messages.name()))
					title = title.replace(messages.name(), "").toLowerCase();
			super.setToolTip(messages
					.selectWhichWeHaveInOurCompanyOrAddNew(title));
		}
	}

	/*
	 * Override this method to do anything before the other overridden methods
	 * are called from constructor.
	 */
	protected void init() {
		super.init();
	}

	public List<T> getComboItems() {
		return comboItems;
	}

	protected AccounterAsyncCallback<Object> createAddNewCallBack() {
		AccounterAsyncCallback<Object> accounterAsyncCallback = new AccounterAsyncCallback<Object>() {

			@Override
			public void onResultSuccess(Object result) {
				if (result != null) {
					addItemThenfireEvent((T) result);
				} else {
					onFailure(null);
				}
			}

			@Override
			public void onException(AccounterException caught) {
				if (!GWT.isScript()) {
					caught.printStackTrace();
					Accounter.showError(messages.sorryFailedToAdd());
				}

			}
		};
		return accounterAsyncCallback;
	}

	// public void addChangeHandler() {
	// this.addChangeHandler(new ChangeHandler() {
	// @Override
	// public void onChange(ChangeEvent event) {
	//
	// int index = listbox.getSelectedIndex();
	//
	// IAccounterComboSelectionChangeHandler<T> handler = getHandler();
	//
	// switch (index) {
	//
	// case 1:
	// if (isAddNewRequire)
	// onAddNew();
	// else if (handler != null) {
	// handler.selectedComboBoxItem(comboItems.get(index - 1));
	// }
	//
	// break;
	//
	// case 0:
	//
	// if (handler != null) {
	// handler.selectedComboBoxItem(null);
	// }
	//
	// break;
	//
	// default:
	//
	// if (handler != null) {
	// try {
	// handler.selectedComboBoxItem(comboItems.get(index
	// - (isAddNewRequire ? 2 : 1)));
	// } catch (Exception e) {
	//
	// }
	// }
	//
	// if (grid != null) {
	// grid.remove(listbox);
	// setSelectedItem(0);
	// }
	// break;
	// }
	//
	// }
	// });
	// }
	// public void addChangeHandler() {
	// this.addChangeHandler(new ChangeHandler() {
	// @Override
	// public void onChange(ChangeEvent event) {
	//
	// int index = listbox.getSelectedIndex();
	//
	// IAccounterComboSelectionChangeHandler handler = getHandler();
	//
	// switch (index) {
	//
	// case 1:
	// if (isAddNewRequire)
	// onAddNew();
	// else if (handler != null) {
	// handler.selectedComboBoxItem(comboItems.get(index - 1));
	// }
	//
	// break;
	//
	// case 0:
	//
	// if (handler != null) {
	// handler.selectedComboBoxItem(null);
	// }
	//
	// break;
	//
	// default:
	//
	// if (handler != null) {
	// try {
	// handler.selectedComboBoxItem(comboItems.get(index
	// - (isAddNewRequire ? 2 : 1)));
	// } catch (Exception e) {
	//
	// }
	// }
	//
	// if (grid != null) {
	// grid.remove(listbox);
	// setSelectedItem(0);
	// }
	// break;
	// }
	//
	// }
	// });
	// }

	protected abstract String getDisplayName(T object);

	public interface Filter {
		boolean canAdd(IAccounterCore core);
	}

	public void setSelectedItem(int index) {
		if (index >= 0) {
			this.selectedObject = comboItems.get(index);
			setSelectedItem(this.selectedObject, index);
		}
	}

	public void setSelected(String itemName) {
		List<T> combo = comboItems;
		for (T item : combo) {
			if (getDisplayName(item).equals(itemName)) {
				this.selectedObject = item;
				this.setSelectedItem(comboItems.indexOf(item));
			}
		}

	}

	public void select(T obj) {
		List<T> combo = comboItems;
		for (T item : combo) {
			if (getDisplayName(item).equals(getDisplayName(obj))) {
				this.selectedObject = item;
				this.setSelectedItem(comboItems.indexOf(item));
			}
		}

	}

	protected ClientCompany getCompany() {
		return Accounter.getCompany();
	}

	@Override
	protected int getObjectIndex(T coreObject) {
		try {
			return comboItems.indexOf(Utility.getObject(
					(List<IAccounterCore>) comboItems,
					((IAccounterCore) coreObject).getID()));
		} catch (ClassCastException e) {
			// the combo items are not belongs to IAccounterCore type.
			return comboItems.indexOf(coreObject);
		}
	}

	// @Override
	// public void updateComboItem(T coreObject) {
	// for (T item : comboItems) {
	// if (((IAccounterCore) item).getID() == ((IAccounterCore) coreObject)
	// .getID()) {
	//
	// if (this.getSelectedValue() != null ? this.getSelectedValue()
	// .equals(item) : true) {
	// removeComboItem(item);
	// addItemThenfireEvent(coreObject);
	// } else {
	// removeComboItem(item);
	// addComboItem(coreObject);
	// }
	// break;
	// } else if (((IAccounterCore) coreObject).getID() != 0) {
	// addComboItem(coreObject);
	// break;
	// }
	//
	// // if((IAccounterCore) item.getSt)
	// }
	//
	// }

}
