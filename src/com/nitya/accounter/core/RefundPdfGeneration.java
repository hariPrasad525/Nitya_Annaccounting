package com.nitya.accounter.core;

import java.io.File;

import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.ui.DataUtils;

import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.template.IContext;

public class RefundPdfGeneration extends TransactionPDFGeneration {

	public RefundPdfGeneration(CustomerRefund refund) {
		super(refund, null);
	}

	public IContext assignValues(IContext context, IXDocReport report) {
		try {
			CustomerRefund refund = (CustomerRefund) getTransaction();

			CustomerRefundTemplate template = new CustomerRefundTemplate();

			template.setTitle(Global.get().Customer() + " Refund");
			template.setPayTo(refund.getPayTo().getName());
			template.setPayFrom(refund.getPayFrom().getName());
			template.setNumber(refund.getNumber());
			template.setCustomerNAddress(getAddress());
			template.setRegisteredAddress(getRegisteredAddress());
			template.setDate(refund.getDate().toString());
			template.setPaymentMethod(refund.getPaymentMethod());
			template.setChequeOrRefNo(refund.getCheckNumber());

			Currency currency = refund.getCurrency();
			String symbol = currency.getSymbol();

			template.setRefundAmount(DataUtils.getAmountAsStringInCurrency(
					refund.getTotal(), symbol));
			template.setMemo(refund.getMemo());
			template.setCurrency(currency.getFormalName());

			context.put("refund", template);
			return context;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private String getAddress() {
		CustomerRefund refund = (CustomerRefund) getTransaction();
		Address bill = refund.getAddress();
		StringBuffer billAddress = new StringBuffer();
		if (bill != null) {
			billAddress = billAddress.append(forUnusedAddress(
					bill.getAddress1(), false)
					+ forUnusedAddress(bill.getStreet(), false)
					+ forUnusedAddress(bill.getCity(), false)
					+ forUnusedAddress(bill.getStateOrProvinence(), false)
					+ forUnusedAddress(bill.getZipOrPostalCode(), false)
					+ forNullValue(bill.getCountryOrRegion()));
			String billAddres = billAddress.toString();

			if (billAddres.trim().length() > 0) {
				return billAddres;
			}
		}
		return "";
	}

	public class CustomerRefundTemplate {

		private String payTo;
		private String payFrom;
		private String registeredAddress;
		private String date;
		private String paymentMethod;
		private String chequeOrRefNo;
		private String refundAmount;
		private String title;
		private String memo;
		private String currency;
		private String customerNAddress;
		private String number;

		public String getPayTo() {
			return payTo;
		}

		public void setPayTo(String payTo) {
			this.payTo = payTo;
		}

		public String getPayFrom() {
			return payFrom;
		}

		public void setPayFrom(String payFrom) {
			this.payFrom = payFrom;
		}

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

		public String getPaymentMethod() {
			return paymentMethod;
		}

		public void setPaymentMethod(String paymentMethod) {
			this.paymentMethod = paymentMethod;
		}

		public String getChequeOrRefNo() {
			return chequeOrRefNo;
		}

		public void setChequeOrRefNo(String chequeOrRefNo) {
			this.chequeOrRefNo = chequeOrRefNo;
		}

		public String getRefundAmount() {
			return refundAmount;
		}

		public void setRefundAmount(String refundAmount) {
			this.refundAmount = refundAmount;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
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

		public String getCustomerNAddress() {
			return customerNAddress;
		}

		public void setCustomerNAddress(String customerNAddress) {
			this.customerNAddress = customerNAddress;
		}

		public String getNumber() {
			return number;
		}

		public void setNumber(String number) {
			this.number = number;
		}

	}

	@Override
	public String getTemplateName() {
		return "templetes" + File.separator + "RefundOdt.odt";
	}

	@Override
	public String getFileName() {
		return "Refund_" + getTransaction().getNumber();
	}

}
