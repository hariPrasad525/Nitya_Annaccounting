package com.vimukti.accounter.core;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;

public class EnterBillPdfGeneration extends TransactionPDFGeneration {

	public EnterBillPdfGeneration(EnterBill enterBill) {
		super(enterBill, null);
	}

	public IContext assignValues(IContext context, IXDocReport report) {

		try {
			EnterBill enterBill = (EnterBill) getTransaction();
			Company company = getCompany();
			DummyEnterBill i = new DummyEnterBill();
			i.setTitle("Bill");
			i.setVendorNBillingAddress(getBillingAddress());
			i.setNumber(enterBill.getNumber());
			i.setDeliveryDate(Utility.getDateInSelectedFormat(enterBill
					.getDeliveryDate()));
			i.setPayTerms(enterBill.getPaymentTerm().getName());
			i.setDate(Utility.getDateInSelectedFormat(enterBill.getDate()));

			Currency currency = enterBill.getCurrency();
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
			List<TransactionItem> transactionItems = enterBill
					.getTransactionItems();

			String symbol = enterBill.getCurrency().getSymbol();
			for (Iterator iterator = transactionItems.iterator(); iterator
					.hasNext();) {

				TransactionItem item = (TransactionItem) iterator.next();

				String description = forNullValue(item.getDescription());
				description = description.replaceAll("\n", "<br/>");

				StringBuffer data = new StringBuffer();
				Quantity quantity = item.getQuantity();
				if (quantity != null) {
					if (item.getItem() != null) {
						if (item.getItem().getType() == Item.TYPE_INVENTORY_PART
								|| item.getItem().getType() == Item.TYPE_INVENTORY_ASSEMBLY) {
							data.append(String.valueOf(quantity.getValue()));
							if (company.getPreferences().isUnitsEnabled()) {
								Unit unit = item.getQuantity().getUnit();
								if (unit != null) {
									data.append(" ");
									data.append(unit.getType());
								}
							}
						} else {
							data.append(String.valueOf(quantity.getValue()));
						}
					}
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
				String name = item.getItem() != null ? item.getItem().getName()
						: item.getAccount().getName();

				String discount = Utility.decimalConversation(
						item.getDiscount(), "");

				TAXCode taxCode = item.getTaxCode();
				String vatRate = " ";
				if (taxCode != null) {
					double rate = item.getTaxCode().getSalesTaxRate();
					vatRate = String.valueOf(rate) + " %";
				}
				itemList.add(new ItemList(name, description, data.toString(),
						unitPrice, discount, totalPrice, vatRate, vatAmount));
			}

			String total = Utility.decimalConversation(enterBill.getTotal(),
					symbol);

			i.setTotal(total);

			String subtotal = Utility.decimalConversation(
					enterBill.getNetAmount(), symbol);
			i.setNetAmount(subtotal);

			i.setMemo(enterBill.getMemo());

			i.setRegistrationAddress(getRegisteredAddress());

			context.put("bill", i);
			context.put("item", itemList);

			return context;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private String getBillingAddress() {
		// To get the selected contact name form Invoice
		String cname = "";
		EnterBill enterBill = (EnterBill) getTransaction();
		Contact selectedContact = enterBill.getContact();
		if (selectedContact != null) {
			cname = selectedContact.getName().trim();
		}

		String customerName = forUnusedAddress(enterBill.getVendor().getName(),
				false);
		StringBuffer contact = new StringBuffer();
		contact = contact.append(forUnusedAddress(cname, false) + customerName);
		return contact.toString();
	}

	public class DummyEnterBill {

		private String title;
		private String vendorNBillingAddress;
		private String number;
		private String date;
		private String deliveryDate;
		private String payTerms;
		private String currency;
		private String total;
		private String netAmount;
		private String memo;
		private String registrationAddress;

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

		public String getDeliveryDate() {
			return deliveryDate;
		}

		public void setDeliveryDate(String deliveryDate) {
			this.deliveryDate = deliveryDate;
		}

		public String getPayTerms() {
			return payTerms;
		}

		public void setPayTerms(String payTerms) {
			this.payTerms = payTerms;
		}

	}

	@Override
	public String getTemplateName() {
		return "templetes" + File.separator + "EnterBillOdt.odt";
	}

	@Override
	public String getFileName() {
		return "Bill_" + getTransaction().getNumber();
	}
}
