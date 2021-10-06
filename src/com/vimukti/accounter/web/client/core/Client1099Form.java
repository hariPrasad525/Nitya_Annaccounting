package com.vimukti.accounter.web.client.core;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.vimukti.accounter.web.client.Global;

public class Client1099Form implements IAccounterCore {

	public static int TOTAL_1099_PAYMENTS = -1;
	public static int TOATAL_ALL_PAYMENTS = -2;
	public static int BOX_1 = 1;
	public static int BOX_2 = 2;
	public static int BOX_3 = 3;
	public static int BOX_4 = 4;
	public static int BOX_5 = 5;
	public static int BOX_6 = 6;
	public static int BOX_7 = 7;
	public static int BOX_8 = 8;
	public static int BOX_9 = 9;
	public static int BOX_10 = 10;
	public static int BOX_13 = 13;
	public static int BOX_14 = 14;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	long id;

	ClientVendor vendor;
	double[] boxes = new double[15];
	double total1099Payments;
	double totalAllPayments;
	boolean isSelected;

	private int version;

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public ClientVendor getVendor() {
		return vendor;
	}

	public void setVendor(ClientVendor vendor) {
		this.vendor = vendor;
	}

	public double getBox(int i) {

		return boxes[i];
	}

	public void setBox(int i, double box) {
		this.boxes[i] = box;
	}

	public double getTotal1099Payments() {
		total1099Payments = 0;
		for (double box : boxes) {
			total1099Payments += box;
		}
		return total1099Payments;
	}

	public void setTotal1099Payments(double total1099Payments) {
		this.total1099Payments = total1099Payments;
	}

	public double getTotalAllPayments() {
		return totalAllPayments;
	}

	public void setTotalAllPayments(double totalAllPayments) {
		this.totalAllPayments = totalAllPayments;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AccounterCoreType getObjectType() {
		return null;
	}

	@Override
	public void setID(long id) {
		this.id = id;

	}

	@Override
	public long getID() {
		return id;
	}

	public SafeHtml getVendorInformation() {
		final StringBuffer information = new StringBuffer();
		ClientAddress address = this.getAddress();
		information.append(vendor.getName()).append("<br>");
		if (address != null) {
			information.append(address.getAddressString());
		}
		String taxId = this.vendor.getTaxId();
		if (taxId != null && !taxId.equals(""))
			information.append(Global.get().messages().taxId()).append(taxId);
		return new SafeHtml() {

			@Override
			public String asString() {
				return information.toString();
			}
		};
	}

	private ClientAddress getAddress() {
		for (ClientAddress address : vendor.address) {
			if (address != null && address.type == ClientAddress.TYPE_BILL_TO) {
				return address;
			}
		}
		for (ClientAddress address : vendor.address) {
			if (address != null) {
				return address;
			}
		}
		return null;
	}

	@Override
	public int getVersion() {
		return version;
	}

	@Override
	public void setVersion(int version) {
		this.version = version;
	}

}
