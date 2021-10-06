package com.nitya.accounter.web.client.ui.company;

import com.google.gwt.user.client.ui.CheckBox;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.ClientAddress;
import com.nitya.accounter.web.client.core.ClientSalesPerson;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.UIUtils;
import com.nitya.accounter.web.client.ui.grids.BaseListGrid;
import com.nitya.accounter.web.client.ui.grids.ListGrid;

public class SalesPersonListGrid extends BaseListGrid<ClientSalesPerson> {

	public SalesPersonListGrid() {
		super(false, false);
		this.getElement().setId("SalesPersonListGrid");
	}

	@Override
	protected Object getColumnValue(ClientSalesPerson salesPerson, int col) {
		switch (col) {
		case 0:
			return salesPerson.isActive();
		case 1:
			return salesPerson.getName();
		case 2:
			return getBillToAddress(salesPerson) != null ? getBillToAddress(
					salesPerson).getAddress1() : "";
		case 3:
			return getBillToAddress(salesPerson) != null ? getBillToAddress(
					salesPerson).getCity() : "";
		case 4:
			return getBillToAddress(salesPerson) != null ? getBillToAddress(
					salesPerson).getStateOrProvinence() : "";

		case 5:
			return getBillToAddress(salesPerson) != null ? getBillToAddress(
					salesPerson).getZipOrPostalCode() : "";

		case 6:
			return salesPerson.getPhoneNo();
			// Set<ClientPhone> phones = SalesPerson.getPhoneNumbers();
			// for (ClientPhone p : phones) {
			// if (p.getType() == ClientPhone.BUSINESS_PHONE_NUMBER) {
			// return p.getNumber();
			// }
			// }
			// break;
		case 7:
			return salesPerson.getFaxNo();
			// Set<ClientFax> faxes = SalesPerson.getFaxNumbers();
			// for (ClientFax f : faxes) {
			// if (f.getType() == ClientFax.TYPE_BUSINESS) {
			// return f.getNumber();
			// }
			// }
			// break;
			// case 8:
			// return DataUtils.getAmountAsString(SalesPerson.getBalance());

		case 8:
			// updateTotal(SalesPerson, true);
			return Accounter.getFinanceMenuImages().delete();
		default:
			break;
		}

		return null;
	}

	@Override
	protected String[] getColumns() {
		return new String[] { messages.active(), messages.salesPerson(),
				messages.address(), messages.city(), messages.state(),
				messages.zipCode(), messages.phone(), messages.fax(), " " };

	}

	@Override
	protected void onClick(ClientSalesPerson obj, int row, int col) {
		// List<ClientCustomer> customers = getRecords();
		// ClientCustomer customer = customers.get(row);
		if (!Accounter.getUser().canDoInvoiceTransactions())
			return;
		switch (col) {
		case 8:
			showWarnDialog(obj);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onValueChange(ClientSalesPerson obj, int col, Object value) {

	}

	private ClientAddress getBillToAddress(ClientSalesPerson salesPerson) {
		return salesPerson.getAddress();
	}

	@Override
	public void onDoubleClick(ClientSalesPerson obj) {
		if (Accounter.getUser().canDoInvoiceTransactions()) {
			new NewSalesperSonAction().run(obj, false);
		}
	}

	protected void executeDelete(ClientSalesPerson object) {
		deleteObject(object);

	}

	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_CHECK,
				ListGrid.COLUMN_TYPE_LINK, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_IMAGE };
	}

	// protected void updateTotal(ClientPayee customer, boolean add) {
	//
	// if (add)
	// total += customer.getBalance();
	// else
	// total -= customer.getBalance();
	//
	// }

	public Double getTotal() {
		return total;
	}

	@Override
	protected int getCellWidth(int index) {
		if (index == 8) {
			if (UIUtils.isMSIEBrowser())
				return 25;
			else
				return 15;
		}
		if (index == 0) {
			return 40;
		} else if (index == 1) {
			return 227;
		} else if (index == 2 || index == 3 || index == 4 || index == 5
				|| index == 6 || index == 7) {
			return 100;
		}
		return -1;
	}

	@Override
	protected int sort(ClientSalesPerson obj1, ClientSalesPerson obj2, int index) {
		switch (index) {
		case 1:
			return obj1.getName().compareTo(obj2.getName());
		case 2:

			String address1 = getBillToAddress(obj1) != null ? getBillToAddress(
					obj1).getAddress1()
					: "";
			String address2 = getBillToAddress(obj2) != null ? getBillToAddress(
					obj2).getAddress1()
					: "";
			return address1.compareTo(address2);

		case 3:

			String city1 = getBillToAddress(obj1) != null ? getBillToAddress(
					obj1).getCity() : "";
			String city2 = getBillToAddress(obj2) != null ? getBillToAddress(
					obj2).getCity() : "";
			return city1.compareTo(city2);

		case 4:
			String state1 = getBillToAddress(obj1) != null ? getBillToAddress(
					obj1).getStateOrProvinence() : "";
			String state2 = getBillToAddress(obj2) != null ? getBillToAddress(
					obj2).getStateOrProvinence() : "";
			return state1.compareTo(state2);

		case 5:
			String zip1 = getBillToAddress(obj1) != null ? getBillToAddress(
					obj1).getZipOrPostalCode() : "";
			String zip2 = getBillToAddress(obj2) != null ? getBillToAddress(
					obj2).getZipOrPostalCode() : "";
			return zip1.compareTo(zip2);
		case 6:
			String phone1 = getPhoneNumber(obj1);
			String phone2 = getPhoneNumber(obj2);
			return phone1.compareTo(phone2);
		case 7:
			String fax1 = getFaxNumber(obj1);
			String fax2 = getFaxNumber(obj2);
			return fax1.compareTo(fax2);
			// case 8:
			// Double bal1 = obj1.getBalance();
			// Double bal2 = obj2.getBalance();
			// return bal1.compareTo(bal2);

		default:
			break;
		}

		return 0;
	}

	private String getPhoneNumber(ClientSalesPerson salesPerson) {
		String phoneNo = "";
		if (salesPerson != null) {
			phoneNo = salesPerson.getPhoneNo();

		}
		return phoneNo != null ? phoneNo : "";
	}

	private String getFaxNumber(ClientSalesPerson salesPerson) {
		String faxNo = "";
		if (salesPerson != null) {
			faxNo = salesPerson.getFaxNo();

		}
		return faxNo;
	}

	public AccounterCoreType getType() {
		return AccounterCoreType.SALES_PERSON;
	}

	@Override
	public void addData(ClientSalesPerson obj) {
		super.addData(obj);
		((CheckBox) this.getWidget(currentRow, 0)).setEnabled(false);
	}

	@Override
	public void headerCellClicked(int colIndex) {
		super.headerCellClicked(colIndex);
		for (int i = 0; i < this.getTableRowCount(); i++) {
			((CheckBox) this.getWidget(i, 0)).setEnabled(false);
		}
	}

	@Override
	protected String[] setHeaderStyle() {
		return new String[] { "active", "salesperson", "address", "city",
				"state", "zipcode", "phone", "fax", "last-col" };
	}

	@Override
	protected String[] setRowElementsStyle() {
		return new String[] { "active-value", "salesperson-value",
				"address-value", "city-value", "state-value", "zipcode-value",
				"phone-value", "fax-value", "last-col-value" };
	}

}
