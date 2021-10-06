package com.nitya.accounter.web.client.ui.combo;

import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.ValueCallBack;
import com.nitya.accounter.web.client.core.ClientLocation;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.ui.core.QuickAddDialog;
import com.nitya.accounter.web.client.ui.core.ViewManager;
import com.nitya.accounter.web.client.ui.vendors.ManageSupportListView;

/**
 * 
 * @author Lingarao.r
 * 
 */
public class LocationCombo extends CustomCombo<ClientLocation> {
	private ValueCallBack<ClientLocation> newLocationtHandler;

	public LocationCombo(String title) {
		super(title, true, 1, "LocationCombo");
		initCombo(getCompany().getLocations());
	}

	public LocationCombo(String title, boolean isAddNewRequire) {
		super(title, isAddNewRequire, 1, "LocationCombo");
		initCombo(getCompany().getLocations());
	}

	@Override
	protected String getDisplayName(ClientLocation object) {
		if (object != null)
			return object.getLocationName() != null ? object.getLocationName()
					: "";
		else
			return "";
	}

	@Override
	protected String getColumnData(ClientLocation object, int col) {
		switch (col) {
		case 0:
			return object.getLocationName();
		}
		return null;
	}

	@Override
	public String getDefaultAddNewCaption() {
		return Global.get().Location();
	}

	@Override
	public void onAddNew() {
		ManageSupportListView priceLevelDialog = new ManageSupportListView(
				IAccounterCore.LOCATION);
		priceLevelDialog.setVisible(false);
		priceLevelDialog.setCallback(createAddNewCallBack());
		priceLevelDialog.showAddEditGroupDialog(null);
	}

	/**
	 * @param valueCallBack
	 */

	public void addNewLocationHandler(
			ValueCallBack<ClientLocation> newLocationtHandler) {
		this.newLocationtHandler = newLocationtHandler;
	}

	@Override
	protected void selectionFaildOnClose() {
		final QuickAddDialog dialog = new QuickAddDialog("New "
				+ Global.get().Location());
		dialog.setDefaultText(textBox.getText());
		dialog.setListener(new QuickAddListenerImpl(this) {
			@Override
			public IAccounterCore getData(String text) {
				if (!isLocationExists(text, dialog)) {
					return super.getData(text);
				}
				return null;
			}
		});
		ViewManager.getInstance().showDialog(dialog);
	}

	protected boolean isLocationExists(String text, QuickAddDialog dialog) {
		for (ClientLocation location : getCompany().getLocations()) {
			if (location.getName().equals(text)) {
				if (location instanceof ClientLocation) {
					this.setComboItem((ClientLocation) location);
					dialog.hide();
				}
				return true;
			}
		}
		return false;
	}

	@Override
	protected void onAddAllInfo(String text) {
		ClientLocation clientLocation = new ClientLocation();
		clientLocation.setLocationName(textBox.getText());
		ManageSupportListView priceLevelDialog = new ManageSupportListView(
				IAccounterCore.LOCATION);
		priceLevelDialog.setVisible(false);
		priceLevelDialog.setCallback(createAddNewCallBack());
		priceLevelDialog.showAddEditGroupDialog(clientLocation);
	}

	@Override
	protected ClientLocation getQuickAddData(String text) {
		ClientLocation clientLocation = new ClientLocation();
		clientLocation.setLocationName(textBox.getText());
		return clientLocation;
	}

}
