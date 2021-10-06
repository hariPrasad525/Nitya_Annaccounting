package com.vimukti.accounter.core;

import java.io.File;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.vimukti.accounter.main.ServerConfiguration;
import com.vimukti.accounter.utils.MiniTemplator;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;

/**
 * this class is used to generate Invoice report in PDF format using HTML file
 * 
 */
public class InvoicePDFTemplete implements PrintTemplete {
	private final Invoice invoice;
	private final BrandingTheme brandingTheme;
	private final Company company;
	private final String templateName;
	private CompanyPreferences preferences;

	public InvoicePDFTemplete(Invoice invoice, BrandingTheme brandingTheme,
			Company company, String templateName) {
		this.invoice = invoice;
		this.brandingTheme = brandingTheme;
		this.company = company;
		this.templateName = templateName;
		preferences = company.getPreferences();
	}

	public String getTempleteName() {
		String tempName = getTempName(templateName);
		return "templetes" + File.separator + tempName + ".html";
	}

	private String getTempName(String templateName2) {

		if (templateName2.contains("Classic")) {
			return "ClassicInvoice";
		} else if (templateName2.contains("Plain")) {
			return "PlainInvoice";
		} else if (templateName2.contains("Professional")) {
			return "ProfessionalInvoice";
		} else if (templateName2.contains("Modern")) {
			return "ModernInvoice";
		}
		return "";

	}

	@Override
	public String getPdfData() {

		String outPutString = "";
		MiniTemplator t;

		try {
			t = new MiniTemplator(getTempleteName());

			externalizeStrings(t);

			String image = getImage();
			// setting the theme styles
			t.setVariable("fontStyle", brandingTheme.getFont());
			t.setVariable("font", brandingTheme.getFontSize());
			// setting logo Image
			if (brandingTheme.isShowLogo()) {
				String logoAlligment = getLogoAlignment();
				t.setVariable("getLogoAlignment", logoAlligment);
				t.setVariable("logoImage", image);
				t.addBlock("showlogo");
			}

			// setting invoice number
			String invNumber = forNullValue(invoice.getNumber());
			if (invNumber.trim().length() > 0) {
				t.setVariable("invoiceNumber", invNumber);
				t.addBlock("invNumberHead");
			}

			// setting invoice date
			String invDate = Utility.getDateInSelectedFormat(invoice.getDate());
			if (invDate.trim().length() > 0) {
				t.setVariable("invoiceDate", invDate);
				t.addBlock("invDateHead");
			}

			// setting invoice order number
			String invOrderNum = forNullValue(invoice.getOrderNum());
			if (invOrderNum.trim().length() > 0) {
				t.setVariable("invoiceOrderNumber", invOrderNum);
				t.addBlock("invOrderNumHead");
			}

			// setting payment terms
			PaymentTerms paymentterm = invoice.getPaymentTerm();
			String payterm = paymentterm != null ? paymentterm.getName() : "";
			if (payterm.trim().length() > 0) {
				t.setVariable("paymentTerms", payterm);
				t.addBlock("paymentTermsBlock");
			}

			// for primary curreny
			Currency currency = invoice.getCustomer().getCurrency();
			if (currency != null)
				if (currency.getFormalName().trim().length() > 0) {
					t.setVariable("currency", currency.getFormalName().trim());
					t.addBlock("currency");
				}

			// for DueDate
			t.setVariable("dueDate",
					Utility.getDateInSelectedFormat(invoice.getDueDate()));
			t.addBlock("dueDateDetails");

			// for customer VAT Registration Number
			String vatRegistrationNumber = invoice.getCustomer()
					.getVATRegistrationNumber();
			if (company.getCountryPreferences().isVatAvailable()
					&& company.getPreferences().isTrackTax()) {
				String val = vatRegistrationNumber == null ? " "
						: vatRegistrationNumber;
				if (val.trim().length() > 0) {
					t.setVariable("customerVATNumber", val);
					t.addBlock("customerVat");
				}
			}

			int orderLength = invoice.getOrderNum() != null ? invoice
					.getOrderNum().trim().length() : 0;
			if (orderLength > 0) {
				t.setVariable("orderNumber", invoice.getOrderNum());
				t.addBlock("orderBlock");
			}

			boolean doProductShipMents = preferences.isDoProductShipMents();
			if (doProductShipMents) {
				if (invoice.getShippingTerm() != null) {
					t.setVariable("shippingTerms", invoice.getShippingTerm()
							.getName());
					t.addBlock("shippingTerms");
				}
				String deliveryDate = Utility.getDateInSelectedFormat(invoice
						.getDeliverydate());
				if (deliveryDate != null) {
					t.setVariable("deliveryDate", deliveryDate);
					t.addBlock("deliveryDateBlock");
				}
			}

			t.setVariable("customerName", invoice.getCustomer().getName());

			List<TransactionItem> items = invoice.getTransactionItems();

			// if (preferences.isLocationTrackingEnabled()) {
			// if (invoice.getLocation() != null) {
			// t.setVariable("location", invoice.getLocation().getName());
			// t.addBlock("locationTracking");
			// }
			// }
			// if (preferences.isJobTrackingEnabled()) {
			// if (invoice.getJob() != null) {
			// t.setVariable("job", invoice.getJob().getJobName());
			// t.addBlock("jobTracking");
			// }
			// }
			// if (preferences.isClassTrackingEnabled()
			// && (preferences.isClassPerDetailLine() == false)) {
			// String accounterClass = "";
			// for (TransactionItem trItem : items) {
			// if (trItem.getAccounterClass() != null) {
			//
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
			// t.setVariable("class", accounterClass);
			// t.addBlock("classTracking");
			// }

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

				t.setVariable("outerDiscount", discountVal);
				t.addBlock("outerDiscount");

			}
			// for getting customer contact name
			String cname = "";
			String phone = "";
			boolean hasPhone = false;
			Customer customer = invoice.getCustomer();

			// To get the selected contact name form Invoice
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
			String customerName = forUnusedAddress(invoice.getCustomer()
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
					billAddress.append(forUnusedAddress("Phone : " + phone,
							false));
				}

				String billAddres = billAddress.toString();

				if (billAddres.trim().length() > 0) {
					t.setVariable("billingAddress", billAddres);
					t.addBlock("billhead");
				}
			} else {
				// If there is no Bill Address, then display only customer and
				// contact name
				StringBuffer contact = new StringBuffer();
				contact = contact.append(forUnusedAddress(cname, false)
						+ customerName);
				t.setVariable("billingAddress", contact.toString());
				t.addBlock("billhead");
			}

			// setting shipping address
			String shipAddress = "";
			Address shpAdres = invoice.getShippingAdress();
			if (shpAdres != null) {
				shipAddress = forUnusedAddress(invoice.getCustomer().getName(),
						false)
						+ forUnusedAddress(shpAdres.getAddress1(), false)
						+ forUnusedAddress(shpAdres.getStreet(), false)
						+ forUnusedAddress(shpAdres.getCity(), false)
						+ forUnusedAddress(shpAdres.getStateOrProvinence(),
								false)
						+ forUnusedAddress(shpAdres.getZipOrPostalCode(), false)
						+ forUnusedAddress(shpAdres.getCountryOrRegion(), false);
			}
			if (shipAddress.trim().length() > 0) {
				t.setVariable("shippingAddress", shipAddress);
				t.addBlock("shiphead");
			}

			// setting salesPerson name
			SalesPerson salesPerson = invoice.getSalesPerson();
			String salesPersname = salesPerson != null ? ((salesPerson
					.getFirstName() != null ? salesPerson.getFirstName() : "") + (salesPerson
					.getLastName() != null ? salesPerson.getLastName() : ""))
					: "";
			if (salesPersname.trim().length() > 0) {
				t.setVariable("salesPersonName", salesPersname);
				t.addBlock("salesperson");
				t.addBlock("salespersonhead");
			}

			// setting shippingmethod name
			ShippingMethod shipMtd = invoice.getShippingMethod();
			String shipMtdName = shipMtd != null ? shipMtd.getName() : "";

			if (shipMtdName.trim().length() > 0) {
				t.setVariable("shippingMethodName", shipMtdName);
				t.addBlock("shippingmethod");
				t.addBlock("shippingmethodhead");
				t.addBlock("salesNShipping");
			}
			// for checking the show Column Headings
			if (brandingTheme.isShowColumnHeadings()) {

				if (company.getPreferences().isTrackTax()
						&& brandingTheme.isShowTaxColumn()) {
					t.addBlock("vatBlock");
				}
				if (company.getPreferences().isTrackDiscounts()
						&& preferences.isDiscountPerDetailLine()) {

					t.addBlock("discountBlock");
				}
				// if (preferences.isClassTrackingEnabled()) {
				// if (preferences.isClassPerDetailLine()) {
				// t.addBlock("classBlock");
				// }
				// }

				t.addBlock("showLabels");
			}

			// setting item description quantity, unit price, total price and
			// vat details
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

				String unitPrice = null;
				unitPrice = Utility
						.decimalConversation(item.getUnitPrice(), "");
				String totalPrice = Utility.decimalConversation(
						item.getLineTotal(), "");

				String vatAmount = item.getVATfraction() == null ? "" : Utility
						.decimalConversation(item.getVATfraction(), "");

				String name = item.getItem() != null ? item.getItem().getName()
						: item.getAccount().getName();
				t.setVariable("name", name);
				t.setVariable("description", description);
				t.setVariable("quantity", data.toString());
				t.setVariable("itemUnitPrice", unitPrice);

				if (company.getPreferences().isTrackDiscounts()
						&& preferences.isDiscountPerDetailLine()) {
					// if Discounts is enabled in Company Preferences, then
					// only we need to show Discount Column
					t.setVariable("discount",
							Utility.decimalConversation(item.getDiscount(), ""));
					t.addBlock("discountValueBlock");
				}

				// if (preferences.isClassTrackingEnabled()) {
				// if (preferences.isClassPerDetailLine()) {
				// if (item.getAccounterClass() != null) {
				// t.setVariable("itemClass", item.getAccounterClass()
				// .getName());
				// t.addBlock("classValueBlock");
				// }
				// }
				// }
				t.setVariable("itemTotalPrice", totalPrice);

				if (company.getPreferences().isTrackTax()
						&& brandingTheme.isShowTaxColumn()) {
					if (item.getTaxCode() != null) {
						String vatRate = "";
						double vat1 = item.getTaxCode().getSalesTaxRate();
						vatRate = String.valueOf(vat1) + " %";
						t.setVariable("itemVatRate", vatRate);
					}

					t.setVariable("itemVatAmount", vatAmount);
					t.addBlock("vatValueBlock");
				}
				t.addBlock("invoiceRecord");
			}

			// for displaying sub total, vat total, total
			String subtotal = Utility.decimalConversation(
					invoice.getNetAmount(), "");
			if (company.getPreferences().isTrackTax()) {

				t.setVariable("subTotal", subtotal);
				t.addBlock("subtotal");
				if (brandingTheme.isShowTaxColumn()) {
					t.setVariable("vatTotal", Utility.decimalConversation(
							(invoice.getTaxTotal()), ""));
					t.addBlock("VatTotal");
				}
			}

			String total = Utility.decimalConversation(invoice.getTotal(),
					symbol);
			t.setVariable("total", total);
			// if (invoice.getMemo().trim().length() > 0) {
			t.setVariable("blankText", invoice.getMemo());
			// t.addBlock("memoblock");
			// }
			t.setVariable("payment",
					Utility.decimalConversation(invoice.getPayments(), symbol));
			// t.setVariable("netAmount",
			// Utility.decimalConversation(invoice.getNetAmount(), symbol));
			t.setVariable("balancedue", Utility.decimalConversation(
					invoice.getBalanceDue(), symbol));
			t.addBlock("itemDetails");

			// for displaying the Terms and conditions
			boolean hasTermsNpaypalId = false;
			String termsNCondn = forNullValue(
					brandingTheme.getTerms_And_Payment_Advice()).replace("\n",
					"<br/>");
			if (getMessage(termsNCondn).trim().length() > 0) {
				hasTermsNpaypalId = true;
				t.setVariable("termsAndPaymentAdvice", termsNCondn);
				t.addBlock("termsAndAdvice");
			}

			// for displaying the paypal Id
			String paypalEmail = forNullValue(brandingTheme.getPayPalEmailID());
			if (getMessage(paypalEmail).trim().length() > 0) {
				hasTermsNpaypalId = true;
				t.setVariable("email", paypalEmail);
				t.addBlock("paypalemail");
			}

			if (hasTermsNpaypalId) {
				t.addBlock("termsNpaypalId");
			}

			t.setVariable(
					"bottomMargin",
					NumberFormat.getInstance().format(
							brandingTheme.getBottomMargin()));
			t.setVariable(
					"topMargin",
					NumberFormat.getInstance().format(
							brandingTheme.getTopMargin()));
			t.setVariable("title", brandingTheme.getOverDueInvoiceTitle());
			if (brandingTheme.isShowLogo()) {
				t.addBlock("logo");
			}
			t.addBlock("theme");
			outPutString = t.getFileString();
			return outPutString;

		} catch (Exception e) {
			System.err.println("invoice err......");
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * to compare value for Paypal_Id and Terms_and_conditions
	 * 
	 * @param msg
	 * @return
	 */
	private String getMessage(String msg) {
		if (msg.equalsIgnoreCase("(None Added)")) {
			msg = "";
		}
		return msg;
	}

	private void externalizeStrings(MiniTemplator t) {
		AccounterMessages messages = Global.get().messages();
		t.setVariable("i18_Invoice_Number", messages.invoiceNo());
		t.setVariable("i18_Invoice_Date", messages.invoiceDate());
		t.setVariable("i18_Order_Number", messages.orderNumber());
		t.setVariable("i18_Bill_To", messages.billTo());
		t.setVariable("i18_Ship_To", messages.shipTo());
		t.setVariable("i18_Sales_Person", messages.salesPerson());
		t.setVariable("i18_Shipping_Method", messages.shippingMethod());
		t.setVariable("i18_Payment_Terms", messages.paymentTerms());
		t.setVariable("i18_Due_Date", messages.dueDate());
		t.setVariable("i18_Customer_TAX_Registration_Number",
				messages.customerTaxRegNo(messages.customer()));
		t.setVariable("i18_Currency", messages.currency());
		t.setVariable("i18_Name", messages.name());
		t.setVariable("i18_Description", messages.description());
		t.setVariable("i18_Qty", messages.qty());
		t.setVariable("i18_Unit_Price", messages.unitPrice());
		t.setVariable("i18_Discount", messages.discount());
		t.setVariable("i18_Total_Price", messages.totalPrice());
		t.setVariable("i18_TOTAL", messages.total());
		t.setVariable("i18_Payments", messages.payments());
		t.setVariable("i18_Balance_Due", messages.balanceDue());
		t.setVariable("i18_Sub_Total", messages.subTotal());
		t.setVariable("i18_tax", messages.tax());
		t.setVariable("i18_NetAmount", messages.netAmount());
		t.setVariable("i18_VATRate", messages.taxCode());
		t.setVariable("i18_VATAmount", messages.tax());
		t.setVariable("i18_Order_Number", messages.orderNumber());
		t.setVariable("i18_Delivery_Date", messages.deliveryDate());
		t.setVariable("i18_Shipping_Terms", messages.shippingTerms());
		t.setVariable("i18_Customer", messages.customer());
		// t.setVariable("i18_Job", messages.job());
		// t.setVariable("i18_Class", messages.className());
		// t.setVariable("i18_Location", messages.location());
		Map<String, String> variables = t.getVariables();
		System.out.println(variables);
	}

	public String forUnusedAddress(String add, boolean isFooter) {
		if (isFooter) {
			if (add != null && !add.equals(""))
				return ", " + add;
		} else {
			if (add != null && !add.equals(""))
				return add + "<br/>";
		}
		return "";
	}

	public String forNullValue(String value) {
		return value != null ? value : "";
	}

	private String getLogoAlignment() {
		String logoAlignment = null;
		if (brandingTheme.getLogoAlignmentType() == 1) {
			logoAlignment = "left";
		} else {
			logoAlignment = "right";
		}
		return logoAlignment;
	}

	public String getImage() {

		StringBuffer original = new StringBuffer();
		// String imagesDomain = "/do/downloadFileFromFile?";

		// original.append("<img style='width:90px;height:90px' src='file:///");
		File file = new File(ServerConfiguration.getAttachmentsDir()
				+ File.separator + company.getId() + File.separator
				+ "thumbnail" + File.separator + brandingTheme.getFileName());
		if (file.exists()) {
			original.append("<img src='file:///");
			original.append(ServerConfiguration.getAttachmentsDir() + "/"
					+ company.getId() + "/" + "thumbnail" + "/"
					+ brandingTheme.getFileName());
			original.append("'/>");
		} else {
			original.append("<img src='file:///");
			original.append(ServerConfiguration.getAttachmentsDir() + "/"
					+ company.getId() + "/" + brandingTheme.getFileName());
			original.append("'/>");
		}

		if (original.toString().contains("null")) {
			return "";
		}
		return original.toString();

	}

	@Override
	public String getFileName() {
		return "Invoice_" + this.invoice.getNumber();
	}

	@Override
	public String getFooter() {
		Location location = invoice.getLocation();
		String regestrationAddress = "";
		Address reg = company.getRegisteredAddress();

		if (reg != null)
			regestrationAddress = (reg.getAddress1()
					+ forFooterAddress(reg.getStreet(), false)
					+ forFooterAddress(reg.getCity(), false)
					+ forFooterAddress(reg.getStateOrProvinence(), false)
					+ forFooterAddress(reg.getCountryOrRegion(), false) + forFooterAddress(
					reg.getZipOrPostalCode(), true));

		String contactDetails = forNullValue(brandingTheme.getContactDetails());
		if (contactDetails.contains("(None Added)")) {
			contactDetails = "";
		}
		regestrationAddress = (contactDetails
				+ "<br/><hr width = 100%>&nbsp;&nbsp;&nbsp;"
				+ (location == null ? company.getTradingName() : location
						.getCompanyName() == null ? company.getTradingName()
						: location.getCompanyName())
				+ "<br/>&nbsp;&nbsp;&nbsp;"
				+ (location == null ? regestrationAddress : location
						.getAddress() == null ? regestrationAddress : location
						.getAddress()) + ((company.getRegistrationNumber() != null && !company
				.getRegistrationNumber().equals("")) ? "<br/>Company Registration No: "
				+ company.getRegistrationNumber()
				: ""));

		String phoneStr = forNullValue(company.getPreferences().getPhone());
		if (phoneStr.trim().length() > 0) {
			regestrationAddress = regestrationAddress
					+ Global.get().messages().phone() + " : " + phoneStr + ",";
		}
		String website = forNullValue(company.getPreferences().getWebSite());

		if (website.trim().length() > 0) {
			regestrationAddress = regestrationAddress
					+ Global.get().messages().webSite() + " : " + website;
		}
		return regestrationAddress;
	}

	private String forFooterAddress(String value, boolean isFooter) {
		if (isFooter) {
			if (value != null && !value.equals("")) {
				return value + ".";
			}
		} else if (value != null && !value.equals("")) {
			return value + " , ";
		}

		return "";
	}
}
