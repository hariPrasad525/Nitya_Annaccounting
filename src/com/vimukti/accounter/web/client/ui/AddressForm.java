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
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;

/**
 * 
 * @author Venki.p
 * 
 */

public class AddressForm extends DynamicForm {
	LinkedHashMap<Integer, ClientAddress> allAddresses;
	ClientAddress toBeShown = null;
	public SelectCombo businessSelect;
	private TextAreaItem addrArea;

	public AddressForm(Set<ClientAddress> addresses) {
		super("addressForm");
		AccounterMessages messages = Global.get().messages();
		Label l1 = new Label(messages.enterAddress());
		allAddresses = new LinkedHashMap<Integer, ClientAddress>();

		setAddresses(addresses);

		businessSelect = new SelectCombo(messages.address());
		// businessSelect.setWidth(85);
		businessSelect.getMainWidget().removeStyleName("gwt-ListBox");
		businessSelect.initCombo(new ClientAddress().getAddressTypes());

		businessSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						new AddressDialog("", "", addrArea, businessSelect
								.getSelectedValue(), allAddresses);
					}
				});

		addrArea = new TextAreaItem("","addrArea");
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
			// businessSelect.setSelected(toBeShown.getAddressTypes().get(
			// toBeShown.getType()));
			businessSelect.setComboItem(UIUtils.getAddressesTypes(toBeShown
					.getType()));
			String toToSet = new String();
			if (toBeShown.getAddress1() != null
					&& !toBeShown.getAddress1().isEmpty()) {
				toToSet = toBeShown.getAddress1().toString() + "\n";
			}

			if (toBeShown.getStreet() != null
					&& !toBeShown.getStreet().isEmpty()) {
				toToSet += toBeShown.getStreet().toString() + "\n";
			}

			if (toBeShown.getCity() != null && !toBeShown.getCity().isEmpty()) {
				toToSet += toBeShown.getCity().toString() + "\n";
			}

			if (toBeShown.getStateOrProvinence() != null
					&& !toBeShown.getStateOrProvinence().isEmpty()) {
				toToSet += toBeShown.getStateOrProvinence() + "\n";
			}
			if (toBeShown.getZipOrPostalCode() != null
					&& !toBeShown.getZipOrPostalCode().isEmpty()) {
				toToSet += toBeShown.getZipOrPostalCode() + "\n";
			}
			if (toBeShown.getCountryOrRegion() != null
					&& !toBeShown.getCountryOrRegion().isEmpty()) {
				toToSet += toBeShown.getCountryOrRegion();
			}
			addrArea.setValue(toToSet);
		} else
			// businessSelect.setDefaultToFirstOption(Boolean.TRUE);
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
		String string = businessSelect.getSelectedValue();
		if (string == null) {
			return new HashSet<ClientAddress>();
		}
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
			allAddresses.put(
					UIUtils.getAddressType(businessSelect.getSelectedValue()),
					selectedAddress);
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

	public void setAddress(Collection<ClientAddress> addresses) {
		for (ClientAddress address : addresses) {
			this.allAddresses.put(address.getType(), address);
			addrArea.setValue(address.getAddress1() + "\n"
					+ address.getStreet() + "\n" + address.getCity() + "\n"
					+ address.getStateOrProvinence() + "\n"
					+ address.getZipOrPostalCode() + "\n"
					+ address.getCountryOrRegion());

			switch (address.getType()) {
			case ClientAddress.TYPE_BUSINESS:
				businessSelect.setSelected("1");
				break;

			case ClientAddress.TYPE_BILL_TO:
				businessSelect.setSelected("Bill To");
				break;
			case ClientAddress.TYPE_SHIP_TO:
				businessSelect.setSelected("Ship To");
				break;

			case ClientAddress.TYPE_WAREHOUSE:
				businessSelect.setSelected("2");
				break;

			case ClientAddress.TYPE_LEGAL:
				businessSelect.setSelected("3");
				break;

			case ClientAddress.TYPE_POSTAL:
				businessSelect.setSelected("4");
				break;
			case ClientAddress.TYPE_HOME:
				businessSelect.setSelected("5");
				break;

			default:
				businessSelect.setSelected("6");
				break;
			}

		}
	}

	public TextAreaItem getAddrArea() {
		return addrArea;
	}

	public void setAddrArea(TextAreaItem addrArea) {
		this.addrArea = addrArea;
	}
}
