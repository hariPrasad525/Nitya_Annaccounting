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

/**
 * this class is used to generate Invoice pdf using custom template files(odt
 * and docx format)
 * 
 * @author vimukti15
 * 
 */

public class InvoicePdfGeneration extends TransactionPDFGeneration {

	public InvoicePdfGeneration(Invoice invoice, BrandingTheme brandingTheme) {
		super(invoice, brandingTheme);
		this.setBrandingTheme(brandingTheme);
	}

	public IContext assignValues(IContext context, IXDocReport report) {

		try {
			Invoice invoice = (Invoice) getTransaction();
			Company company = getCompany();
			CompanyPreferences preferences = company.getPreferences();
			IImageProvider logo = new ClassPathImageProvider(
					InvoicePdfGeneration.class, getImage());
			IImageProvider footerImg = new ClassPathImageProvider(
					InvoicePdfGeneration.class, "templetes" + File.separator
							+ "footer-print-img.jpg");

			FieldsMetadata imgMetaData = new FieldsMetadata();
			imgMetaData.addFieldAsImage("logo");
			imgMetaData.addFieldAsImage("companyImg");
			report.setFieldsMetadata(imgMetaData);

			// assigning the original values
			DummyInvoice i = new DummyInvoice();
			String title = getBrandingTheme().getOverDueInvoiceTitle() == null ? "Invoice"
					: getBrandingTheme().getOverDueInvoiceTitle().toString();
			i.setTitle(title);
			i.setBillAddress(getBillingAddress());

			Contact contact = invoice.getContact();
			i.setContactName(contact != null ? contact.getName() : "");
			i.setContactNumber(contact != null ? contact.getBusinessPhone()
					: "");
			i.setContactEmail(contact != null ? contact.getEmail() : "");

			// Contact selectedContact = invoice.getContact();
			// if (selectedContact != null) {
			// billTo.setContactName(selectedContact.getName() != null ?
			// selectedContact
			// .getName().trim() : "");
			// } else {
			// billTo.setContactName("");
			// }
			//
			// billTo.setBill_customerName(invoice.getCustomer().getName().trim());
			Address billAddress = invoice.getBillingAddress();
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

			Address shippAdress = invoice.getShippingAdress();
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

			i.setInvoiceNumber(invoice.getNumber());
			i.setInvoiceDate(Utility.getDateInSelectedFormat(invoice.getDate()));

			Customer customer = invoice.getCustomer();
			// for customer details
			i.setCstNumber(customer.getCSTno() == null ? "" : customer
					.getCSTno());
			i.setServiceTaxRegistrationNumber(customer
					.getServiceTaxRegistrationNo() == null ? "" : customer
					.getServiceTaxRegistrationNo());
			i.setTaxpayerIdentificationNumber(customer.getTINNumber() == null ? ""
					: customer.getTINNumber());
			i.setTaxRegistrationNumber(customer.getVATRegistrationNumber() == null ? ""
					: customer.getVATRegistrationNumber());
			i.setCustomerTaxCode(customer.getTAXCode() == null ? "" : customer
					.getTAXCode().getName());

			// for primary curreny
			Currency currency = customer.getCurrency();
			if (currency != null)
				if (currency.getFormalName().trim().length() > 0) {
					i.setCurrency(currency.getFormalName().trim());
				}

			PaymentTerms paymentterm = invoice.getPaymentTerm();
			String payterm = paymentterm != null ? paymentterm.getName() : "";
			i.setTerms(payterm);

			i.setDueDate(Utility.getDateInSelectedFormat(invoice.getDueDate()));
			i.setShipAddress(getShippingAddress());

			ShippingMethod shipMtd = invoice.getShippingMethod();
			String shipMtdName = shipMtd != null ? shipMtd.getName() : "";
			i.setShippingMethod(shipMtdName);

			// for transactions

			FieldsMetadata headersMetaData = new FieldsMetadata();
			headersMetaData.addFieldAsList("item.name");
			headersMetaData.addFieldAsList("item.description");
			headersMetaData.addFieldAsList("item.quantity");
			headersMetaData.addFieldAsList("item.itemUnitPrice");
			headersMetaData.addFieldAsList("item.discount");
			headersMetaData.addFieldAsList("item.itemTotalPrice");
			// headersMetaData.addFieldAsList("item.className");
			headersMetaData.addFieldAsList("item.itemVatRate");
			headersMetaData.addFieldAsList("item.itemVatAmount");
			report.setFieldsMetadata(headersMetaData);
			List<ItemList> itemList = new ArrayList<ItemList>();
			List<TransactionItem> transactionItems = invoice
					.getTransactionItems();

			// double currencyFactor = invoice.getCurrencyFactor();
			String symbol = invoice.getCurrency().getSymbol();
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
				String discount = "";
				if (preferences.isTrackDiscounts()
						&& preferences.isDiscountPerDetailLine()) {
					discount = Utility.decimalConversation(item.getDiscount(),
							"");
				}

				TAXCode taxCode = item.getTaxCode();
				String vatRate = " ";
				if (taxCode != null) {
					double valRate1 = item.getTaxCode().getSalesTaxRate();
					vatRate = String.valueOf(valRate1) + " %";
				}

				String className = "";
				// if (preferences.isClassTrackingEnabled()) {
				// if (preferences.isClassPerDetailLine()) {
				// if (item.getAccounterClass() != null) {
				// className = forNullValue(item.getAccounterClass().getName());
				// }
				// }
				// }
				itemList.add(new ItemList(name, description, data.toString(),
						unitPrice,
						// className,
						discount, totalPrice, vatRate, vatAmount));
			}

			context.put("item", itemList);
			String total = Utility.decimalConversation(invoice.getTotal(),
					symbol);

			i.setTotal(total);
			String netAmount = Utility.decimalConversation(
					invoice.getNetAmount(), symbol);
			i.setNetAmount(netAmount);

			i.setPayment(Utility.decimalConversation(invoice.getPayments(),
					symbol));
			i.setBalancedue(Utility.decimalConversation(
					invoice.getBalanceDue(), symbol));
			String orderNum = invoice.getOrderNum() == null ? "" : invoice
					.getOrderNum().trim();
			i.setOrderNumber(orderNum);

			i.setMemo(invoice.getMemo());
			String termsNCondn = forNullValue(getBrandingTheme()
					.getTerms_And_Payment_Advice());

			if (termsNCondn.equalsIgnoreCase("(None Added)")) {
				termsNCondn = " ";
			}
			i.setAdviceTerms(termsNCondn);

			String paypalEmail = forNullValue(getBrandingTheme()
					.getPayPalEmailID());
			if (paypalEmail.equalsIgnoreCase("(None Added)")) {
				paypalEmail = " ";
			}
			i.setEmail(paypalEmail);

			i.setRegistrationAddress(getRegisteredAddress());
			Address regAddress1 = company.getRegisteredAddress();
			if (regAddress1 != null) {
				i.setRegAddress(regAddress1);
				i.regAddress.setAddress1(company.getTradingName() + "\n "
						+ forNullValue(regAddress1.getAddress1()));
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
			// if (preferences.isJobTrackingEnabled()) {
			// if (invoice.getJob() != null) {
			// i.setJob(invoice.getJob().getJobName());
			// } else {
			// i.setJob("");
			// }
			// } else {
			// i.setJob("");
			// }

			// if (preferences.isLocationTrackingEnabled()) {
			// Location location = invoice.getLocation();
			// if (location != null) {
			// i.setLocation(location.getName());
			// } else {
			// i.setLocation("");
			// }
			// } else {
			// i.setLocation("");
			// }
			i.setCustomerName(invoice.getCustomer().getName());

			List<TransactionItem> items = invoice.getTransactionItems();
			if (preferences.isTrackDiscounts()
					&& (preferences.isDiscountPerDetailLine() == false)) {
				double discount = 0;

				for (TransactionItem trItem : items) {
					if (trItem != null) {
						Double discountValue = trItem.getDiscount();
						if (discountValue != null) {
							discount = discountValue.doubleValue();
							if (discount != 0.0D)
								break;
							else
								continue;
						}
					}
				}

				String discountVal = Utility.decimalConversation(discount, "");
				i.setDiscount(discountVal + "%");

			} else {
				i.setDiscount("0%");
			}
			// if (preferences.isClassTrackingEnabled()
			// && (!preferences.isClassPerDetailLine() == false)) {
			// String accounterClass = "";
			// for (TransactionItem trItem : items) {
			// if (trItem.getAccounterClass() != null) {
			// if (trItem.getAccounterClass() != null) {
			// accounterClass = trItem.getAccounterClass()
			// .getclassName();
			// }
			// if (accounterClass != null) {
			// break;
			// } else {
			// continue;
			// }
			// }
			//
			// }
			// i.setClassName(accounterClass);
			// } else {
			// i.setClassName("");
			// }
			i.setTaxTotal(Utility.decimalConversation(invoice.getTaxTotal(),
					symbol));
			i.setDeliveryDate(Utility.getDateInSelectedFormat(invoice
					.getDeliverydate()));
			if (invoice.getShippingTerm() != null) {
				i.setShippingTerms(invoice.getShippingTerm().getName());
			} else {
				i.setShippingTerms("");
			}

			context.put("logo", logo);
			context.put("invoice", i);
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
		Invoice invoice = (Invoice) getTransaction();
		Contact selectedContact = invoice.getContact();
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
		Address bill = invoice.getBillingAddress();
		String customerName = forUnusedAddress(invoice.getCustomer().getName(),
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
		Invoice invoice = (Invoice) getTransaction();
		Address shpAdres = invoice.getShippingAdress();
		if (shpAdres != null) {
			shipAddress = forUnusedAddress(invoice.getCustomer().getName(),
					false)
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

	public class DummyInvoice {

		private String title;
		private String invoiceNumber;
		private String invoiceDate;
		private String orderNumber;
		private String currency;
		private String terms;
		private String dueDate;
		private String billAddress;
		private String shipAddress;
		private String shippingMethod;
		private String total;
		private String payment;
		private String balancedue;
		private String memo;
		private String adviceTerms;
		private String email;
		private String registrationAddress;
		private String discount;
		private String netAmount;
		private String customerName;
		// private String job;
		// private String className;
		// private String location;
		private String deliveryDate;
		private String shippingTerms;
		private String cstNumber;
		private String serviceTaxRegistrationNumber;
		private String taxpayerIdentificationNumber;
		private String taxRegistrationNumber;
		private String customerTaxCode;
		private Address billTo;
		private Address regAddress;
		private Address shipTo;
		private String taxTotal;
		private String contactName;
		private String contactNumber;
		private String contactEmail;

		public String getInvoiceNumber() {
			return invoiceNumber;
		}

		public void setInvoiceNumber(String invoiceNumber) {
			this.invoiceNumber = invoiceNumber;
		}

		public String getInvoiceDate() {
			return invoiceDate;
		}

		public void setInvoiceDate(String invoiceDate) {
			this.invoiceDate = invoiceDate;
		}

		public String getCurrency() {
			return currency;
		}

		public void setCurrency(String currency) {
			this.currency = currency;
		}

		public String getDueDate() {
			return dueDate;
		}

		public void setDueDate(String dueDate) {
			this.dueDate = dueDate;
		}

		public String getTotal() {
			return total;
		}

		public void setTotal(String total) {
			this.total = total;
		}

		public String getPayment() {
			return payment;
		}

		public void setPayment(String payment) {
			this.payment = payment;
		}

		public String getBalancedue() {
			return balancedue;
		}

		public void setBalancedue(String balancedue) {
			this.balancedue = balancedue;
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

		public String getShippingMethod() {
			return shippingMethod;
		}

		public void setShippingMethod(String shippingMethod) {
			this.shippingMethod = shippingMethod;
		}

		public String getBillAddress() {
			return billAddress;
		}

		public void setBillAddress(String billAddress) {
			this.billAddress = billAddress;
		}

		public String getShipAddress() {
			return shipAddress;
		}

		public void setShipAddress(String shipAddress) {
			this.shipAddress = shipAddress;
		}

		public String getTerms() {
			return terms;
		}

		public void setTerms(String terms) {
			this.terms = terms;
		}

		public String getAdviceTerms() {
			return adviceTerms;
		}

		public void setAdviceTerms(String adviceTerms) {
			this.adviceTerms = adviceTerms;
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

		public String getDiscount() {
			return discount;
		}

		public void setDiscount(String discount) {
			this.discount = discount;
		}

		public String getOrderNumber() {
			return orderNumber;
		}

		public void setOrderNumber(String orderNumber) {
			this.orderNumber = orderNumber;
		}

		public String getNetAmount() {
			return netAmount;
		}

		public void setNetAmount(String netAmount) {
			this.netAmount = netAmount;
		}

		// public String getJob() {
		// return job;
		// }
		//
		// public void setJob(String job) {
		// this.job = job;
		// }
		//
		// public String getClassName() {
		// return className;
		// }
		//
		// public void setClassName(String className) {
		// this.className = className;
		// }
		//
		// public String getLocation() {
		// return location;
		// }
		//
		// public void setLocation(String location) {
		// this.location = location;
		// }

		public String getDeliveryDate() {
			return deliveryDate;
		}

		public void setDeliveryDate(String deliveryDate) {
			this.deliveryDate = deliveryDate;
		}

		public String getShippingTerms() {
			return shippingTerms;
		}

		public void setShippingTerms(String shippingTerms) {
			this.shippingTerms = shippingTerms;
		}

		public String getCustomerName() {
			return customerName;
		}

		public void setCustomerName(String customerName) {
			this.customerName = customerName;
		}

		public String getCstNumber() {
			return cstNumber;
		}

		public void setCstNumber(String cstNumber) {
			this.cstNumber = cstNumber;
		}

		public String getServiceTaxRegistrationNumber() {
			return serviceTaxRegistrationNumber;
		}

		public void setServiceTaxRegistrationNumber(
				String serviceTaxRegistrationNumber) {
			this.serviceTaxRegistrationNumber = serviceTaxRegistrationNumber;
		}

		public String getTaxpayerIdentificationNumber() {
			return taxpayerIdentificationNumber;
		}

		public void setTaxpayerIdentificationNumber(
				String taxpayerIdentificationNumber) {
			this.taxpayerIdentificationNumber = taxpayerIdentificationNumber;
		}

		public String getTaxRegistrationNumber() {
			return taxRegistrationNumber;
		}

		public void setTaxRegistrationNumber(String taxRegistrationNumber) {
			this.taxRegistrationNumber = taxRegistrationNumber;
		}

		public String getCustomerTaxCode() {
			return customerTaxCode;
		}

		public void setCustomerTaxCode(String customerTaxCode) {
			this.customerTaxCode = customerTaxCode;
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
		String templeteName;
		if (getBrandingTheme().getInvoiceTempleteName().contains(
				"Classic Template")) {
			templeteName = "templetes" + File.separator + "InvoiceDocx.docx";
		} else {

			templeteName = ServerConfiguration.getAttachmentsDir() + "/"
					+ getCompany().getId() + "/" + "templateFiles" + "/"
					+ getBrandingTheme().getID() + "/"
					+ getBrandingTheme().getInvoiceTempleteName();
		}
		return templeteName;
	}

	@Override
	public String getFileName() {
		return "Invoice_" + getTransaction().getNumber();
	}

}
