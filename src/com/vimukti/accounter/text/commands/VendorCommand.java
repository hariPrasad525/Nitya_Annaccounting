package com.vimukti.accounter.text.commands;

import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.Payee;
import com.vimukti.accounter.core.Vendor;
import com.vimukti.accounter.text.ITextData;
import com.vimukti.accounter.text.ITextResponse;
import com.vimukti.accounter.web.client.exception.AccounterException;

/**
 * 
 * Creating Vendor Command below fields
 * 
 * Name,VendorSince,OpeningBalance,Address,Web Address,Email,Phone,Fax
 * 
 * @author vimukti10
 * 
 */
public class VendorCommand extends CreateOrUpdateCommand {

	private String vendorName;
	private FinanceDate vendorSince;
	private FinanceDate balanceAsOf;
	private double openingBalance;
	private Address address;
	private String webAddress;
	private String email;
	private String phone;
	private String fax;

	@Override
	public boolean parse(ITextData data, ITextResponse respnse) {
		// Vendor Name
		String name = data.nextString("");
		if (vendorName != null && !vendorName.equals(name)) {
			return false;
		}
		vendorName = name;
		if (!data.isDate()) {
			respnse.addError("Invalid Date format for date field");
			return false;
		}
		// if next date is null,then set the default present date
		vendorSince = data.nextDate(new FinanceDate());
		// Opening Balance
		if (!data.isDouble()) {
			respnse.addError("Invalid Double for Opening Balance");
			return false;
		}
		openingBalance = data.nextDouble(0);
		if (!data.isDate()) {
			respnse.addError("Invalid Date format for date field");
			return false;
		}
		// balance as of
		balanceAsOf = data.nextDate(new FinanceDate());
		// Adredd
		address = data.nextAddress(null);
		// Web Adress
		webAddress = data.nextString("");
		// EMAIL
		email = data.nextString("");
		// Phnoe
		phone = data.nextString("");
		// FAX
		fax = data.nextString("");

		return true;
	}

	@Override
	public void process(ITextResponse respnse) throws AccounterException {
		Vendor vendor = getObject(Vendor.class, "name", vendorName);
		if (vendor == null) {
			vendor = new Vendor();
			vendor.setType(Payee.TYPE_VENDOR);
		}
		vendor.setName(vendorName);
		vendor.setPayeeSince(vendorSince);
		vendor.setOpeningBalance(openingBalance);
		if (address != null) {
			vendor.getAddress().add(address);
		}
		vendor.setBalanceAsOf(balanceAsOf);
		vendor.setWebPageAddress(webAddress);
		vendor.setEmail(email);
		vendor.setPhoneNo(phone);
		vendor.setFaxNo(fax);

		saveOrUpdate(vendor);

	}
}
