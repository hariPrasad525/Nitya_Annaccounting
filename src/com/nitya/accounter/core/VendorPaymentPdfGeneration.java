package com.nitya.accounter.core;

import java.io.File;

import com.nitya.accounter.web.client.Global;

import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.template.IContext;

public class VendorPaymentPdfGeneration extends TransactionPDFGeneration {

	public VendorPaymentPdfGeneration(VendorPrePayment payment) {
		super(payment, null);
	}

	public IContext assignValues(IContext context, IXDocReport report) {

		try {
			VendorPrePayment vendorPayment = (VendorPrePayment) getTransaction();
			DummyPayment template = new DummyPayment();
			template.setTitle(Global.get().Vendor() + " Prepayment");
			template.setName(vendorPayment.getVendor().getName());
			template.setVendorNBillingAddress(getBillingAddress());
			template.setRegisteredAddress(getRegisteredAddress());
			template.setMemo(vendorPayment.getMemo());
			template.setNumber(vendorPayment.getNumber());
			template.setDate(Utility.getDateInSelectedFormat(vendorPayment
					.getDate()));
			template.setPayMethod(vendorPayment.getPaymentMethod());
			template.setCheckNo(vendorPayment.getCheckNumber());
			template.setCurrency(vendorPayment.getVendor() != null ? vendorPayment
					.getVendor().getCurrency().getFormalName()
					: vendorPayment.getCurrency().getFormalName());
			String symbol = vendorPayment.getCurrency().getSymbol();
			template.setTdsTotal(Utility.decimalConversation(
					vendorPayment.getTdsTotal(), symbol));
			template.setVendorPayment(Utility.decimalConversation(
					vendorPayment.getTotal() - vendorPayment.getTdsTotal(),
					symbol));
			template.setTotal(Utility.decimalConversation(
					vendorPayment.getTotal(), symbol));
			context.put("payment", template);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return context;

	}

	private String getBillingAddress() {
		String cname = "";
		String phone = "";
		boolean hasPhone = false;
		VendorPrePayment vendorPayment = (VendorPrePayment) getTransaction();

		Address bill = vendorPayment.getAddress();
		String customerName = forUnusedAddress(vendorPayment.getVendor()
				.getName(), false);
		StringBuffer billAddress = new StringBuffer();
		if (bill != null) {
			billAddress = billAddress.append(forUnusedAddress(cname, false)
					+ customerName
					+ forUnusedAddress(bill.getAddress1(), false)
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
			StringBuffer contact = new StringBuffer();
			contact = contact.append(forUnusedAddress(cname, false)
					+ customerName);
			return contact.toString();
		}
		return "";
	}

	public class DummyPayment {

		private String title;
		private String name;
		private String registeredAddress;
		private String memo;
		private String date;
		private String payMethod;
		private String checkNo;
		private String currency;
		private String vendorPayment;
		private String vendorNBillingAddress;
		private String tdsTotal;
		private String total;

		private String number;

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

		public String getNumber() {
			return number;
		}

		public void setNumber(String number) {
			this.number = number;
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

		public String getVendorNBillingAddress() {
			return vendorNBillingAddress;
		}

		public void setVendorNBillingAddress(String vendorNBillingAddress) {
			this.vendorNBillingAddress = vendorNBillingAddress;
		}

		public String getTdsTotal() {
			return tdsTotal;
		}

		public void setTdsTotal(String tdsTotal) {
			this.tdsTotal = tdsTotal;
		}

		public String getVendorPayment() {
			return vendorPayment;
		}

		public void setVendorPayment(String vendorPayment) {
			this.vendorPayment = vendorPayment;
		}

		public String getTotal() {
			return total;
		}

		public void setTotal(String total) {
			this.total = total;
		}

	}

	@Override
	public String getTemplateName() {
		return "templetes" + File.separator + "VendorPaymentOdt.odt";
	}

	@Override
	public String getFileName() {
		return "Payment_" + getTransaction().getNumber();
	}
}
