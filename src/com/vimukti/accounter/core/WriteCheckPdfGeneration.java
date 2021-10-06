package com.vimukti.accounter.core;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;

public class WriteCheckPdfGeneration extends TransactionPDFGeneration {

	public WriteCheckPdfGeneration(WriteCheck writeCheck) {
		super(writeCheck, null);
	}

	public IContext assignValues(IContext context, IXDocReport report) {

		try {

			WriteCheck writeCheck = (WriteCheck) getTransaction();
			DummyWriteCheck i = new DummyWriteCheck();
			i.setTitle("Check");
			i.setVendorNBillingAddress(getBillingAddress());
			i.setNumber(writeCheck.getNumber());
			i.setDate(Utility.getDateInSelectedFormat(writeCheck.getDate()));

			Currency currency = writeCheck.getCurrency();
			if (currency != null)
				if (currency.getFormalName().trim().length() > 0) {
					i.setCurrency(currency.getFormalName().trim());
				}

			FieldsMetadata headersMetaData = new FieldsMetadata();
			headersMetaData.addFieldAsList("item.name");
			headersMetaData.addFieldAsList("item.description");
			headersMetaData.addFieldAsList("item.quantity");
			headersMetaData.addFieldAsList("item.itemUnitPrice");
			headersMetaData.addFieldAsList("item.discount");
			headersMetaData.addFieldAsList("item.itemTotalPrice");
			headersMetaData.addFieldAsList("item.itemVatRate");
			headersMetaData.addFieldAsList("item.itemVatAmount");
			report.setFieldsMetadata(headersMetaData);
			List<ItemList> itemList = new ArrayList<ItemList>();
			List<TransactionItem> transactionItems = writeCheck
					.getTransactionItems();

			String symbol = writeCheck.getCurrency().getSymbol();
			double totalTax = 0.0D;
			for (Iterator iterator = transactionItems.iterator(); iterator
					.hasNext();) {

				TransactionItem item = (TransactionItem) iterator.next();

				String description = forNullValue(item.getDescription());
				description = description.replaceAll("\n", "<br/>");

				String qty = "";
				if (item.getQuantity() != null) {
					qty = String.valueOf(item.getQuantity().getValue());
				}
				String unitPrice = Utility.decimalConversation(
						item.getUnitPrice(), "");
				String totalPrice = Utility.decimalConversation(
						item.getLineTotal(), "");

				Double vaTfraction = item.getVATfraction();
				String vatAmount = " ";
				if (vaTfraction != null) {
					vatAmount = Utility.decimalConversation(
							item.getVATfraction(), "");
				}
				totalTax = totalTax + item.getVATfraction();
				String name = item.getAccount().getName();

				String discount = Utility.decimalConversation(
						item.getDiscount(), "");

				TAXCode taxCode = item.getTaxCode();
				String vatRate = " ";
				if (taxCode != null) {
					double rate = item.getTaxCode().getSalesTaxRate();
					vatRate = String.valueOf(rate) + " %";
				}
				itemList.add(new ItemList(name, description, qty, unitPrice,
						discount, totalPrice, vatRate, vatAmount));
			}

			String total = Utility.decimalConversation(writeCheck.getTotal(),
					symbol);

			i.setTotal(total);

			String subtotal = Utility.decimalConversation(
					writeCheck.getNetAmount(), symbol);
			i.setNetAmount(subtotal);

			i.setMemo(writeCheck.getMemo());
			i.setCheckNumber(writeCheck.getCheckNumber());
			i.setTaxTotal(Utility.decimalConversation(totalTax, symbol));
			i.setRegistrationAddress(getRegisteredAddress());

			context.put("check", i);
			context.put("item", itemList);

			return context;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private String getBillingAddress() {
		WriteCheck writeCheck = (WriteCheck) getTransaction();
		String string = writeCheck.getInFavourOf();
		return string;
	}

	public class DummyWriteCheck {

		private String title;
		private String vendorNBillingAddress;
		private String number;
		private String date;
		private String currency;
		private String total;
		private String netAmount;
		private String memo;
		private String registrationAddress;
		private String checkNumber;
		private String taxTotal;

		public String getCurrency() {
			return currency;
		}

		public void setCurrency(String currency) {
			this.currency = currency;
		}

		public String getTotal() {
			return total;
		}

		public void setTotal(String total) {
			this.total = total;
		}

		public String getNetAmount() {
			return netAmount;
		}

		public void setNetAmount(String netAmount) {
			this.netAmount = netAmount;
		}

		public String getMemo() {
			return memo;
		}

		public void setMemo(String memo) {
			this.memo = memo;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getRegistrationAddress() {
			return registrationAddress;
		}

		public void setRegistrationAddress(String registrationAddress) {
			this.registrationAddress = registrationAddress;
		}

		public String getVendorNBillingAddress() {
			return vendorNBillingAddress;
		}

		public void setVendorNBillingAddress(String vendorNBillingAddress) {
			this.vendorNBillingAddress = vendorNBillingAddress;
		}

		public String getNumber() {
			return number;
		}

		public void setNumber(String number) {
			this.number = number;
		}

		public String getDate() {
			return date;
		}

		public void setDate(String date) {
			this.date = date;
		}

		public String getCheckNumber() {
			return checkNumber;
		}

		public void setCheckNumber(String checkNumber) {
			this.checkNumber = checkNumber;
		}

		public String getTaxTotal() {
			return taxTotal;
		}

		public void setTaxTotal(String taxTotal) {
			this.taxTotal = taxTotal;
		}

	}

	@Override
	public String getTemplateName() {
		return "templetes" + File.separator + "WriteCheckOdt.odt";
	}

	@Override
	public String getFileName() {
		return "WriteCheck_" + getTransaction().getNumber();
	}
}
