/**
 *
 */
package com.nitya.accounter.web.client;

import com.nitya.accounter.web.client.core.ClientCustomer;
import com.nitya.accounter.web.client.core.ClientLocation;
import com.nitya.accounter.web.client.core.ClientVendor;
import com.nitya.accounter.web.client.i18n.AccounterNumberFormat;

/**
 * @author Prasanna Kumar G
 *
 */
public abstract class AbstractGlobal implements IGlobal {

	@Override
	public String Customer() {
		int referCustomers = preferences().getReferCustomers();
		switch (referCustomers) {
		case ClientCustomer.CUSTOMER:
			return messages().customer().trim();
		case ClientCustomer.CLIENT:
			return messages().Client().trim();
		case ClientCustomer.TENANT:
			return messages().Tenant().trim();
		case ClientCustomer.DONAR:
			return messages().Donar().trim();
		case ClientCustomer.GUEST:
			return messages().Guest().trim();
		case ClientCustomer.MEMBER:
			return messages().Member().trim();
		case ClientCustomer.PATITEINT:
			return messages().Patient().trim();
		default:
			return messages().customer().trim();
		}
	}

	@Override
	public String customer() {
		int referCustomers = preferences().getReferCustomers();
		switch (referCustomers) {
		case ClientCustomer.CUSTOMER:
			return messages().customer().trim();
		case ClientCustomer.CLIENT:
			return messages().client().trim();
		case ClientCustomer.TENANT:
			return messages().tenant().trim();
		case ClientCustomer.DONAR:
			return messages().donar().trim();
		case ClientCustomer.GUEST:
			return messages().guest().trim();
		case ClientCustomer.MEMBER:
			return messages().member().trim();
		case ClientCustomer.PATITEINT:
			return messages().patient().trim();
		default:
			return messages().customer().trim();
		}
	}

	@Override
	public String Vendor() {
		int referCustomers = preferences().getReferVendors();
		switch (referCustomers) {
		case ClientVendor.SUPPLIER:
			return messages().Supplier().trim();
		case ClientVendor.VENDOR:
			return messages().Vendor().trim();
		default:
			return messages().Vendor().trim();
		}
	}

	@Override
	public String vendor() {
		int referCustomers = preferences().getReferVendors();
		switch (referCustomers) {
		case ClientVendor.SUPPLIER:
			return messages().supplier().trim();
		case ClientVendor.VENDOR:
			return messages().vendor().trim();
		default:
			return messages().vendor().trim();
		}
	}

	@Override
	public String Location() {

		int locationTrackingId = (int) preferences().getLocationTrackingId();

		switch (locationTrackingId) {
		case ClientLocation.LOCATION:
			return messages().location().trim();
		case ClientLocation.BUSINESS:
			return messages().buisiness().trim();
		case ClientLocation.DEPARTMENT:
			return messages().department().trim();
		case ClientLocation.DIVISION:
			return messages().division().trim();
		case ClientLocation.PROPERTY:
			return messages().property().trim();
		case ClientLocation.STORE:
			return messages().store().trim();
		case ClientLocation.TERRITORY:
			return messages().territory().trim();
		default:
			return messages().location().trim();
		}
	}

	@Override
	public String vendors() {
		return Vendors();
	}

	@Override
	public String customers() {
		return Customers();
	}

	@Override
	public String Customers() {
		int referCustomers = preferences().getReferCustomers();
		switch (referCustomers) {
		case ClientCustomer.CUSTOMER:
			return messages().Customers().trim();
		case ClientCustomer.CLIENT:
			return messages().Clients().trim();
		case ClientCustomer.TENANT:
			return messages().Tenants().trim();
		case ClientCustomer.DONAR:
			return messages().Donars().trim();
		case ClientCustomer.GUEST:
			return messages().Guests().trim();
		case ClientCustomer.MEMBER:
			return messages().Members().trim();
		case ClientCustomer.PATITEINT:
			return messages().Patients().trim();
		default:
			return messages().Customers().trim();
		}
	}

	@Override
	public String Vendors() {
		int referCustomers = preferences().getReferVendors();
		switch (referCustomers) {
		case ClientVendor.SUPPLIER:
			return messages().suppliers().trim();
		case ClientVendor.VENDOR:
			return messages().Vendors().trim();
		default:
			return messages().Vendors().trim();
		}
	}

	@Override
	public String toCurrencyFormat(double amount, String currencyCode) {
		AccounterNumberFormat nf = getFormater();
		return nf.format(amount, currencyCode);
	}

	@Override
	public String consultants() {
		return messages2().itConsultant().trim();
	}
}
