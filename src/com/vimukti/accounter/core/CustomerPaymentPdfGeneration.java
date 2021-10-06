package com.vimukti.accounter.core;

import java.io.File;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ui.DataUtils;

import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.template.IContext;

public class CustomerPaymentPdfGeneration extends TransactionPDFGeneration {

	public CustomerPaymentPdfGeneration(CustomerPrePayment prePayment) {
		super(prePayment, null);
	}

	public IContext assignValues(IContext context, IXDocReport report) {
		try {
			CustomerPrePayment prePayment = (CustomerPrePayment) getTransaction();
			DummyPayment template = new DummyPayment();
			template.setTitle(Global.get().Customer() + " Prepayment");
			template.setName(prePayment.getCustomer().getName());
			template.setNumber(prePayment.getNumber());
			template.setCustomerNAddress(getAddress());
			template.setRegisteredAddress(getRegisteredAddress());
			template.setDate(prePayment.getDate().toString());
			template.setPayMethod(prePayment.getPaymentMethod());
			template.setCheckNo(prePayment.getCheckNumber());
			template.setCustomerName(Global.get().Customer());
			Currency currency = prePayment.getCurrency();
			String symbol = currency.getSymbol();
			template.setAmount(DataUtils.getAmountAsStringInCurrency(
					prePayment.getTotal(), symbol));
			template.setCurrency(currency.getFormalName());
			template.setMemo(prePayment.getMemo());
			context.put("payment", template);
			return context;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private String getAddress() {
		// To get the selected contact name form Invoice
		String phone = "";
		boolean hasPhone = false;
		CustomerPrePayment prePayment = (CustomerPrePayment) getTransaction();
		Address bill = prePayment.getAddress();
		String customerName = forUnusedAddress(prePayment.getCustomer()
				.getName(), false);
		StringBuffer billAddress = new StringBuffer();
		if (bill != null) {
			billAddress = billAddress.append(forUnusedAddress(
					bill.getAddress1(), false)
					+ forUnusedAddress(bill.getStreet(), false)
					+ forUnusedAddress(bill.getCity(), false)
					+ forUnusedAddress(bill.getStateOrProvinence(), false)
					+ forUnusedAddress(bill.getZipOrPostalCode(), false)
					+ forUnusedAddress(bill.getCountryOrRegion(), false));
			if (hasPhone) {
				billAddress.append(forUnusedAddress("Phone : " + phone, false));
			}
			String billAddres = billAddress.toString();
			if (billAddres.trim().length() > 0) {
				return billAddres;
			}
		} else {
			return customerName;
		}
		return "";
	}

	public class DummyPayment {

		private String name;
		private String customerName;
		private String registeredAddress;
		private String date;
		private String payMethod;
		private String checkNo;
		private String amount;
		private String title;
		private String number;
		private String customerNAddress;
		private String memo;
		private String currency;

		public String getRegisteredAddress() {
			return registeredAddress;
		}

		public void setRegisteredAddress(String registeredAddress) {
			this.registeredAddress = registeredAddress;
		}

		public String getDate() {
			return date;
		}

		public void setDate(String date) {
			this.date = date;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getCustomerName() {
			return customerName;
		}

		public void setCustomerName(String customerName) {
			this.customerName = customerName;
		}

		public String getAmount() {
			return amount;
		}

		public void setAmount(String amount) {
			this.amount = amount;
		}

		public String getPayMethod() {
			return payMethod;
		}

		public void setPayMethod(String payMethod) {
			this.payMethod = payMethod;
		}

		public String getCheckNo() {
			return checkNo;
		}

		public void setCheckNo(String checkNo) {
			this.checkNo = checkNo;
		}

		public String getNumber() {
			return number;
		}

		public void setNumber(String number) {
			this.number = number;
		}

		public String getCustomerNAddress() {
			return customerNAddress;
		}

		public void setCustomerNAddress(String customerNAddress) {
			this.customerNAddress = customerNAddress;
		}

		public String getMemo() {
			return memo;
		}

		public void setMemo(String memo) {
			this.memo = memo;
		}

		public String getCurrency() {
			return currency;
		}

		public void setCurrency(String currency) {
			this.currency = currency;
		}

	}

	@Override
	public String getTemplateName() {
		return "templetes" + File.separator + "CustomerPaymentOdt.odt";
	}

	@Override
	public String getFileName() {
		return "Prepayment_" + getTransaction().getNumber();
	}
}
