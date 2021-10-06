package com.vimukti.accounter.web.client.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.user.client.ui.Label;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;

/**
 * 
 * @author Venki.p
 * 
 */

public class ShipToForm extends DynamicForm {
	protected static AccounterMessages messages = Global.get().messages();

	LinkedHashMap<Integer, ClientAddress> allAddresses;
	ClientAddress toBeShown = null;
	public SelectCombo businessSelect;
	public TextAreaItem addrArea;

	public ShipToForm(Set<ClientAddress> addresses) {
	
		super("ShipToForm");
		Label l1 = new Label(messages.enterAddress());
		allAddresses = new LinkedHashMap<Integer, ClientAddress>();

		setAddresses(addresses);
		businessSelect = new SelectCombo(messages.shipTo());
		businessSelect.getMainWidget().removeStyleName("gwt-ListBox");
		List<String> addressTypes = new ArrayList<String>();

		addressTypes.addAll(new ClientAddress().getAddressTypes());
		addressTypes.remove(messages.billTo());
		businessSelect.initCombo(addressTypes);
		businessSelect.setDefaultToFirstOption(true);

		addrArea = new TextAreaItem("Address Area","addrArea");
//		addrArea.setWidth(100);
		addrArea.setShowTitle(true);
		// addrArea.setDisabled(true);
		addrArea.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				new AddressDialog("", "", addrArea, businessSelect
						.getSelectedValue(), allAddresses);

			}
		});

		addrArea.addFocusHandler(new FocusHandler() {

			@Override
			public void onFocus(FocusEvent event) {
				new AddressDialog("", "", addrArea, businessSelect
						.getSelectedValue(), allAddresses);

			}
		});

		if (toBeShown != null) {
			businessSelect.setSelected(toBeShown.getAddressTypes().get(
					toBeShown.getType()));
			String toToSet = new String();
			if (toBeShown.getAddress1() != null) {
				toToSet = toBeShown.getAddress1().toString() + "\n";
			}

			if (toBeShown.getStreet() != null) {
				toToSet = toBeShown.getStreet().toString() + "\n";
			}

			if (toBeShown.getCity() != null) {
				toToSet += toBeShown.getCity().toString() + "\n";
			}

			if (toBeShown.getStateOrProvinence() != null) {
				toToSet += toBeShown.getStateOrProvinence() + "\n";
			}
			if (toBeShown.getZipOrPostalCode() != null) {
				toToSet += toBeShown.getZipOrPostalCode() + "\n";
			}
			if (toBeShown.getCountryOrRegion() != null) {
				toToSet += toBeShown.getCountryOrRegion();
			}
			addrArea.setValue(toToSet);
		} else
			businessSelect.setDefaultToFirstOption(Boolean.TRUE);
		add(businessSelect, addrArea);
	}

	private void setAddresses(Set<ClientAddress> addresses) {
		if (addresses != null) {
			Iterator it = addresses.iterator();
			while (it.hasNext()) {
				ClientAddress add = (ClientAddress) it.next();
				if (add.getIsSelected()) {
					toBeShown = add;
				}
				allAddresses.put(add.getType(), add);
				// System.out.println("Existing Address  Type " + add.getType()
				// + " Street is " + add.getStreet() + " Is Selected"
				// + add.getIsSelected());
			}
		}
	}

	public Set<ClientAddress> getAddresss() {
		ClientAddress selectedAddress = allAddresses.get(UIUtils
				.getAddressType(businessSelect.getSelectedValue()));
		if (selectedAddress != null) {
			selectedAddress.setIsSelected(true);
			allAddresses.put(
					UIUtils.getAddressType(businessSelect.getSelectedValue()),
					selectedAddress);
		}
		Collection add = allAddresses.values();
		Set<ClientAddress> toBeSet = new HashSet<ClientAddress>();
		Iterator it = add.iterator();
		while (it.hasNext()) {
			ClientAddress a = (ClientAddress) it.next();
			toBeSet.add(a);
			// System.out.println("Sending Address  Type " + a.getType()
			// + " Street is " + a.getStreet() + " Is Selected"
			// + a.getIsSelected());
		}
		return toBeSet;
	}

	public List<ClientAddress> getAddresssList() {
		ClientAddress selectedAddress = allAddresses.get(businessSelect
				.getSelectedValue());
		if (selectedAddress != null) {
			selectedAddress.setIsSelected(true);
			allAddresses.put(UIUtils.getAddressType((String) businessSelect
					.getSelectedValue()), selectedAddress);
		}
		Collection add = allAddresses.values();
		List<ClientAddress> toBeSet = new ArrayList<ClientAddress>();
		Iterator it = add.iterator();
		while (it.hasNext()) {
			ClientAddress a = (ClientAddress) it.next();
			toBeSet.add(a);
			// System.out.println("Sending Address  Type " + a.getType()
			// + " Street is " + a.getStreet() + " Is Selected"
			// + a.getIsSelected());
		}
		return toBeSet;
	}

	public void setSelectValueMap(List<String> linkedHashMap) {
		this.businessSelect.initCombo(linkedHashMap);
	}

	public void setAddress(List<ClientAddress> addresses) {
		if (!addresses.isEmpty()) {
			for (ClientAddress address : addresses) {
				this.allAddresses.put(address.getType(), address);
				if (address.getType() == ClientAddress.TYPE_SHIP_TO) {

					addrArea.setValue(getValidAddress(address));

					businessSelect.setSelected(messages.shipTo());
				}

			}
		} else {
			addrArea.setValue("");
			this.allAddresses.clear();
		}
	}

	public ClientAddress getAddress() {

		ClientAddress selectedAddress = allAddresses.get(UIUtils
				.getAddressType(businessSelect.getSelectedValue()));
		return selectedAddress;
	}

	public void setAddres(ClientAddress address) {

		addrArea.setValue(getValidAddress(address));
	}

	public void setListOfCustomerAdress(List<ClientAddress> addresses) {
		if (!addresses.isEmpty()) {
			for (ClientAddress address : addresses) {
				this.allAddresses.put(address.getType(), address);
			}
		}
	}

	public String getValidAddress(ClientAddress address) {
		String toToSet = new String();
		if (address.getAddress1() != null && !address.getAddress1().isEmpty()) {
			toToSet = address.getAddress1().toString() + "\n";
		}

		if (address.getStreet() != null && !address.getStreet().isEmpty()) {
			toToSet += address.getStreet().toString() + "\n";
		}

		if (address.getCity() != null && !address.getCity().isEmpty()) {
			toToSet += address.getCity().toString() + "\n";
		}

		if (address.getStateOrProvinence() != null
				&& !address.getStateOrProvinence().isEmpty()) {
			toToSet += address.getStateOrProvinence() + "\n";
		}
		if (address.getZipOrPostalCode() != null
				&& !address.getZipOrPostalCode().isEmpty()) {
			toToSet += address.getZipOrPostalCode() + "\n";
		}
		if (address.getCountryOrRegion() != null
				&& !address.getCountryOrRegion().isEmpty()) {
			toToSet += address.getCountryOrRegion();
		}
		return toToSet;
	}

	public void setTabIndex(int index) {
		addrArea.setTabIndex(index);
	}

	public void setTabIndexforShiptocombo(int index) {
		businessSelect.setTabIndex(index);
	}
}
