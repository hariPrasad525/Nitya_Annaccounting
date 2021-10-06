package com.vimukti.accounter.core;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.vimukti.accounter.main.ServerConfiguration;

import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.images.ClassPathImageProvider;
import fr.opensagres.xdocreport.document.images.IImageProvider;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;

public class CashSalePdfGeneration extends TransactionPDFGeneration {

	public CashSalePdfGeneration(CashSales sale, BrandingTheme brandingTheme) {
		super(sale, brandingTheme);
	}

	public IContext assignValues(IContext context, IXDocReport report) {

		try {

			IImageProvider logo = new ClassPathImageProvider(
					CashSalePdfGeneration.class, getImage());
			IImageProvider footerImg = new ClassPathImageProvider(
					CashSalePdfGeneration.class, "templetes" + File.separator
							+ "footer-print-img.jpg");

			FieldsMetadata imgMetaData = new FieldsMetadata();
			imgMetaData.addFieldAsImage("logo");
			imgMetaData.addFieldAsImage("companyImg");
			report.setFieldsMetadata(imgMetaData);

			// assigning the original values
			DummyCashSale i = new DummyCashSale();
			BrandingTheme brandingTheme = getBrandingTheme();
			CashSales sale = (CashSales) getTransaction();
			Company company = getCompany();
			String title = brandingTheme.getCashSaleTitle() == null ? "Cash Sale"
					: brandingTheme.getCashSaleTitle().toString();

			i.setTitle(title);
			i.setCustomerNameNBillAddress(getBillingAddress());
			i.setCustomerName(sale.getCustomer() != null ? sale.getCustomer()
					.getName() : "");
			Address billAddress = sale.getBillingAddress();
			if (billAddress != null) {
				i.setBillTo(billAddress);
				i.billTo.setAddress1(forNullValue(billAddress.getAddress1()));
				i.billTo.setStreet(forNullValue(billAddress.getStreet()));
				i.billTo.setCity(forNullValue(billAddress.getCity()));
				i.billTo.setStateOrProvinence(forNullValue(billAddress
						.getStateOrProvinence()));
				i.billTo.setZipOrPostalCode(forNullValue(billAddress
						.getZipOrPostalCode()));
				i.billTo.setCountryOrRegion(forNullValue(billAddress
						.getCountryOrRegion()));
			} else {
				i.setBillTo(new Address());
				i.billTo.setAddress1("");
				i.billTo.setStreet("");
				i.billTo.setCity("");
				i.billTo.setStateOrProvinence("");
				i.billTo.setZipOrPostalCode("");
				i.billTo.setCountryOrRegion("");
			}
			i.setSaleNumber(sale.getNumber());
			i.setDeliveryDate(Utility.getDateInSelectedFormat(sale.getDate()));
			i.setShipAddress(getShippingAddress());

			Contact contact = sale.getContact();
			i.setContactName(contact != null ? contact.getName() : "");
			i.setContactNumber(contact != null ? contact.getBusinessPhone()
					: "");
			i.setContactEmail(contact != null ? contact.getEmail() : "");

			Address shippAdress = sale.getShippingAdress();
			if (shippAdress != null) {
				i.setShipTo(shippAdress);
				// i.shipTo.setShip_customerName(invoice.getCustomer().getName()
				// .trim());
				i.shipTo.setAddress1(forNullValue(shippAdress.getAddress1()));
				i.shipTo.setStreet(forNullValue(shippAdress.getStreet()));
				i.shipTo.setCity(forNullValue(shippAdress.getCity()));
				i.shipTo.setStateOrProvinence(forNullValue(shippAdress
						.getStateOrProvinence()));
				i.shipTo.setCountryOrRegion(forNullValue(shippAdress
						.getCountryOrRegion()));
				i.shipTo.setZipOrPostalCode(forNullValue(shippAdress
						.getZipOrPostalCode()));
			} else {
				i.setShipTo(new Address());
				i.shipTo.setAddress1("");
				i.shipTo.setStreet("");
				i.shipTo.setCity("");
				i.shipTo.setStateOrProvinence("");
				i.shipTo.setCountryOrRegion("");
				i.shipTo.setZipOrPostalCode("");
			}

			// for primary curreny
			Currency currency = sale.getCustomer().getCurrency();
			if (currency != null)
				if (currency.getFormalName().trim().length() > 0) {
					i.setCurrency(currency.getFormalName().trim());
				}

			i.setPaymentMethod(forNullValue(sale.getPaymentMethod()));

			ShippingMethod shipMtd = sale.getShippingMethod();
			String shipMtdName = shipMtd != null ? shipMtd.getName() : "";
			i.setShippingMethod(shipMtdName);

			ShippingTerms terms = sale.getShippingTerm();
			String shipTerm = terms != null ? terms.getName() : "";
			i.setShippingTerms(shipTerm);

			// for transactions

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
			List<TransactionItem> transactionItems = sale.getTransactionItems();

			// double currencyFactor = sale.getCurrencyFactor();
			String symbol = sale.getCurrency().getSymbol();
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

			String total = Utility.decimalConversation(sale.getTotal(), symbol);

			i.setTotal(total);

			String subtotal = Utility.decimalConversation(sale.getNetAmount(),
					symbol);
			i.setNetAmount(subtotal);

			i.setTaxTotal(Utility.decimalConversation(sale.getTaxTotal(),
					symbol));
			i.setMemo(sale.getMemo());

			String paypalEmail = forNullValue(brandingTheme.getPayPalEmailID());
			if (paypalEmail.equalsIgnoreCase("(None Added)")) {
				paypalEmail = " ";
			}
			i.setEmail(paypalEmail);

			i.setRegistrationAddress(getRegisteredAddress());
			Address regAddress1 = company.getRegisteredAddress();
			if (regAddress1 != null) {
				i.setRegAddress(regAddress1);
				i.regAddress
						.setAddress1(forNullValue(regAddress1.getAddress1()));
				i.regAddress.setStreet(forNullValue(regAddress1.getStreet()));
				i.regAddress.setCity(forNullValue(regAddress1.getCity()));
				i.regAddress.setStateOrProvinence(forNullValue(regAddress1
						.getStateOrProvinence()));
				i.regAddress.setCountryOrRegion(forNullValue(regAddress1
						.getCountryOrRegion()));
				i.regAddress.setZipOrPostalCode(forNullValue(regAddress1
						.getZipOrPostalCode()));
			} else {
				i.setRegAddress(new Address());
				i.regAddress.setAddress1(company.getTradingName());
				i.regAddress.setStreet(company.getRegistrationNumber());
				i.regAddress.setCity("");
				i.regAddress.setStateOrProvinence(company.getPreferences()
						.getPhone());
				i.regAddress.setCountryOrRegion(company.getCountry());
				i.regAddress.setZipOrPostalCode("");
			}

			context.put("logo", logo);
			context.put("sale", i);
			context.put("item", itemList);
			context.put("companyImg", footerImg);

			return context;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private String getBillingAddress() {
		// To get the selected contact name form Invoice
		String cname = "";
		String phone = "";
		boolean hasPhone = false;
		CashSales sale = (CashSales) getTransaction();
		Contact selectedContact = sale.getContact();
		if (selectedContact != null) {
			cname = selectedContact.getName().trim();
			if (selectedContact.getBusinessPhone().trim().length() > 0)
				phone = selectedContact.getBusinessPhone();
			if (phone.trim().length() > 0) {
				// If phone variable has value, then only we need to display
				// the text 'phone'
				hasPhone = true;
			}
		}

		// setting billing address
		Address bill = sale.getBillingAddress();
		String customerName = forUnusedAddress(sale.getCustomer().getName(),
				false);
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
			// If there is no Bill Address, then display only customer and
			// contact name
			StringBuffer contact = new StringBuffer();
			contact = contact.append(forUnusedAddress(cname, false)
					+ customerName);
			return contact.toString();
		}
		return "";
	}

	private String getShippingAddress() {
		// setting shipping address
		String shipAddress = "";
		CashSales sale = (CashSales) getTransaction();
		Address shpAdres = sale.getShippingAdress();
		if (shpAdres != null) {
			shipAddress = forUnusedAddress(sale.getCustomer().getName(), false)
					+ forUnusedAddress(shpAdres.getAddress1(), false)
					+ forUnusedAddress(shpAdres.getStreet(), false)
					+ forUnusedAddress(shpAdres.getCity(), false)
					+ forUnusedAddress(shpAdres.getStateOrProvinence(), false)
					+ forUnusedAddress(shpAdres.getZipOrPostalCode(), false)
					+ forUnusedAddress(shpAdres.getCountryOrRegion(), false);
		}
		if (shipAddress.trim().length() > 0) {
			return shipAddress;
		}
		return "";
	}

	public class DummyCashSale {

		private String title;
		private String saleNumber;
		private String deliveryDate;
		private String currency;
		private String paymentMethod;
		private String shippingMethod;
		private String shippingTerms;
		private String customerName;
		private String customerNameNBillAddress;
		private String shipAddress;
		private String total;
		private String netAmount;
		private String memo;
		private String email;
		private String registrationAddress;
		private Address billTo;
		private Address regAddress;
		private Address shipTo;
		private String taxTotal;
		private String contactName;
		private String contactNumber;
		private String contactEmail;

		public String getSaleNumber() {
			return saleNumber;
		}

		public void setSaleNumber(String saleNumber) {
			this.saleNumber = saleNumber;
		}

		public String getCurrency() {
			return currency;
		}

		public void setCurrency(String currency) {
			this.currency = currency;
		}

		public String getCustomerNameNBillAddress() {
			return customerNameNBillAddress;
		}

		public void setCustomerNameNBillAddress(String customerNameNBillAddress) {
			this.customerNameNBillAddress = customerNameNBillAddress;
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

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
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

		public String getDeliveryDate() {
			return deliveryDate;
		}

		public void setDeliveryDate(String deliveryDate) {
			this.deliveryDate = deliveryDate;
		}

		public String getPaymentMethod() {
			return paymentMethod;
		}

		public void setPaymentMethod(String paymentMethod) {
			this.paymentMethod = paymentMethod;
		}

		public String getShippingMethod() {
			return shippingMethod;
		}

		public void setShippingMethod(String shippingMethod) {
			this.shippingMethod = shippingMethod;
		}

		public String getShippingTerms() {
			return shippingTerms;
		}

		public void setShippingTerms(String shippingTerms) {
			this.shippingTerms = shippingTerms;
		}

		public String getShipAddress() {
			return shipAddress;
		}

		public void setShipAddress(String shipAddress) {
			this.shipAddress = shipAddress;
		}

		public String getCustomerName() {
			return customerName;
		}

		public void setCustomerName(String customerName) {
			this.customerName = customerName;
		}

		public Address getBillTo() {
			return billTo;
		}

		public void setBillTo(Address billTo) {
			this.billTo = billTo;
		}

		public Address getRegAddress() {
			return regAddress;
		}

		public void setRegAddress(Address regAddress) {
			this.regAddress = regAddress;
		}

		public Address getShipTo() {
			return shipTo;
		}

		public void setShipTo(Address shipTo) {
			this.shipTo = shipTo;
		}

		public String getTaxTotal() {
			return taxTotal;
		}

		public void setTaxTotal(String taxTotal) {
			this.taxTotal = taxTotal;
		}

		public String getContactName() {
			return contactName;
		}

		public void setContactName(String contactName) {
			this.contactName = contactName;
		}

		public String getContactNumber() {
			return contactNumber;
		}

		public void setContactNumber(String contactNumber) {
			this.contactNumber = contactNumber;
		}

		public String getContactEmail() {
			return contactEmail;
		}

		public void setContactEmail(String contactEmail) {
			this.contactEmail = contactEmail;
		}

	}

	@Override
	public String getTemplateName() {
		BrandingTheme brandingTheme = getBrandingTheme();
		if (brandingTheme.getCashSaleTemplateName()
				.contains("Classic Template")) {
			return "templetes" + File.separator + "CashSaleOdt.odt";
		}
		return ServerConfiguration.getAttachmentsDir() + "/"
				+ getCompany().getId() + "/" + "templateFiles" + "/"
				+ brandingTheme.getID() + "/"
				+ brandingTheme.getCashSaleTemplateName();
	}

	@Override
	public String getFileName() {
		return "CashSale_" + getTransaction().getNumber();
	}
}
