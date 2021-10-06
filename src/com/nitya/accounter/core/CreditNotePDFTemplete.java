package com.nitya.accounter.core;

import java.io.File;
import java.text.NumberFormat;
import java.util.Map;
import java.util.Set;

import com.nitya.accounter.main.ServerConfiguration;
import com.nitya.accounter.utils.MiniTemplator;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.externalization.AccounterMessages;

/**
 * this class is used to generate PDF file for Credit note Object using HTML
 * file
 * 
 * @author G.Srinivas
 * 
 */
public class CreditNotePDFTemplete implements PrintTemplete {
	private CustomerCreditMemo memo;
	private BrandingTheme brandingTheme;
	private String templateName;

	Company company;

	public String getTempleteName() {
		return "templetes" + File.separator + templateName + ".html";
	}

	public CreditNotePDFTemplete(CustomerCreditMemo memo,
			BrandingTheme brandingTheme, Company company, String templateName) {
		this.memo = memo;
		this.brandingTheme = brandingTheme;
		this.company = company;
		this.templateName = templateName;
	}

	public String getFileName() {
		return "CreditNote_" + memo.getNumber();
	}

	@Override
	public String getPdfData() {

		try {
			MiniTemplator t = new MiniTemplator(getTempleteName());
			externalizeStrings(t);

			// setting the theme styles
			t.setVariable("fontStyle", brandingTheme.getFont());
			t.setVariable("font", brandingTheme.getFontSize());
			t.setVariable(
					"bottomMargin",
					NumberFormat.getInstance().format(
							brandingTheme.getBottomMargin()));
			t.setVariable(
					"topMargin",
					NumberFormat.getInstance().format(
							brandingTheme.getTopMargin()));

			// setting logo Image
			if (brandingTheme.isShowLogo()) {
				String logoAlligment = getLogoAlignment();
				t.setVariable("getLogoAlignment", logoAlligment);
				String image = getImage().toString();
				t.setVariable("logoImage", image);
				t.addBlock("showlogo");
			}

			t.setVariable("creditNoteNumber", memo.getNumber());
			t.setVariable("creditNoteDate",
					Utility.getDateInSelectedFormat(memo.getDate()));

			// for title
			t.setVariable("title", brandingTheme.getCreditMemoTitle());

			// for primary curreny
			Currency currency = memo.getCustomer().getCurrency();
			if (currency != null)
				if (currency.getFormalName().trim().length() > 0) {
					t.setVariable("currency", currency.getFormalName().trim());

				}
			// for getting customer contact name
			String cname = "";
			String phone = "";
			Customer customer = memo.getCustomer();
			Set<Contact> contacts = customer.getContacts();
			for (Contact contact : contacts) {
				if (contact.isPrimary()) {
					cname = contact.getName().trim();
					if (contact.getBusinessPhone().trim().length() > 0)
						phone = contact.getBusinessPhone();
				}
			}
			// setting billing address
			Address bill = memo.getBillingAddress();
			String customerName = forUnusedAddress(memo.getCustomer().getName()
					.trim(), false);
			if (bill != null) {
				String customernameAddress = forUnusedAddress(cname, false)
						+ customerName
						+ forUnusedAddress(bill.getAddress1(), false)
						+ forUnusedAddress(bill.getStreet(), false)
						+ forUnusedAddress(bill.getCity(), false)
						+ forUnusedAddress(bill.getStateOrProvinence(), false)
						+ forUnusedAddress(bill.getZipOrPostalCode(), false)
						+ forUnusedAddress(bill.getCountryOrRegion(), false)
						+ forUnusedAddress("Phone : " + phone, false);

				if (customernameAddress.trim().length() > 0) {
					t.setVariable("customerNameNBillAddress",
							customernameAddress);
					t.addBlock("creditHead");

				}
			} else {
				t.setVariable("customerNameNBillAddress", customerName);
				t.addBlock("creditHead");
			}

			// t.setVariable("description", "Description");
			// t.setVariable("qty", "Qty");
			// t.setVariable("unitPrice", "Unit Price");
			// t.setVariable("totalPrice", "Total Price");

			// for checking to show column headings
			if (brandingTheme.isShowColumnHeadings()) {
				// for checking to show tax columns

				if (company.getPreferences().isTrackTax()
						&& brandingTheme.isShowTaxColumn()) {
					t.addBlock("vatBlock");
				}
				t.addBlock("showLabels");

			}

			// double currencyFactor = memo.getCurrencyFactor();
			String symbol = memo.getCurrency().getSymbol();
			// for displaying the credit item details
			if (!memo.getTransactionItems().isEmpty()) {

				for (TransactionItem item : memo.getTransactionItems()) {

					String description = forNullValue(item.getDescription());
					description = description.replace("\n", "<br/>");

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

					String name = "";
					if (item.type == TransactionItem.TYPE_ITEM)
						name = item.getItem().getName();
					if (item.type == TransactionItem.TYPE_ACCOUNT)
						name = item.getAccount().getName();

					t.setVariable("name", name);
					t.setVariable("discount",
							Utility.decimalConversation(item.getDiscount(), ""));

					t.setVariable("description", description);
					t.setVariable("quantity", data.toString());
					t.setVariable("unitPrice", unitPrice);
					t.setVariable("itemTotalPrice", totalPrice);
					if (company.getPreferences().isTrackTax()
							&& brandingTheme.isShowTaxColumn()) {
						String vatRate = "";
						double vat1 = item.getTaxCode().getSalesTaxRate();
						vatRate = String.valueOf(vat1) + " %";
						String vatAmount = item.getVATfraction() == null ? " "
								: Utility.decimalConversation(
										item.getVATfraction(), "");
						t.setVariable("itemVatRate", vatRate);
						t.setVariable("itemVatAmount", vatAmount);
						t.addBlock("vatValueBlock");
					}
					t.addBlock("itemRecord");
				}
			}
			// for displaying the total price details

			String memoVal = forNullValue(memo.getMemo());
			String subTotal = Utility.decimalConversation(memo.getNetAmount(),
					symbol);
			String vatTotal = Utility.decimalConversation(
					(memo.getTotal() - memo.getNetAmount()), symbol);
			String total = Utility.decimalConversation(memo.getTotal(), symbol);

			// if (memo.getMemo().trim().length() > 0) {
			t.setVariable("memoText", memoVal);
			// t.addBlock("memoblock");
			// }
			if (company.getPreferences().isTrackTax()) {
				t.setVariable("subTotal", subTotal);
				t.addBlock("subtotal");
				// if (brandingTheme.isShowVatColumn()) {
				// t.setVariable("vatlabel", "Tax ");
				// t.setVariable("vatTotalValue", vatTotal);
				// t.addBlock("VatTotal");
				// }
			}
			t.setVariable("total", total);
			t.addBlock("itemDetails");

			boolean hasTermsNpaypalId = false;
			String termsNCondn = forNullValue(
					brandingTheme.getTerms_And_Payment_Advice()).replace("\n",
					"<br/>");

			if (termsNCondn.equalsIgnoreCase("(None Added)")) {
				termsNCondn = "";
			}
			if (termsNCondn.trim().length() > 0) {
				hasTermsNpaypalId = true;
				t.setVariable("termsAndPaymentAdvice", termsNCondn);
				t.addBlock("termsAndAdvice");
			}

			String paypalEmail = forNullValue(brandingTheme.getPayPalEmailID());
			if (paypalEmail.equalsIgnoreCase("(None Added)")) {
				paypalEmail = "";
			}
			if (paypalEmail.trim().length() > 0) {
				hasTermsNpaypalId = true;
				t.setVariable("email", paypalEmail);
				t.addBlock("paypalemail");
			}
			if (hasTermsNpaypalId) {
				t.addBlock("termsNpaypalId");
			}

			String outPutString = t.getFileString();
			return outPutString;
		} catch (Exception e) {
			System.err.println("credit memeo err.......");
			e.printStackTrace();
		}
		return "";
	}

	private void externalizeStrings(MiniTemplator t) {
		AccounterMessages messages = Global.get().messages();
		Map<String, String> variables = t.getVariables();
		System.out.println(variables);
		t.setVariable("i18_Credit_To", messages.creditTo());
		t.setVariable("i18_Credit_Note_Number", messages.creditNoteNo());
		t.setVariable("i18_Credit_Note_Date", messages.creditNoteDate());
		t.setVariable("i18_Currency", messages.currency());
		t.setVariable("i18_Name", messages.name());
		t.setVariable("i18_Description", messages.description());
		t.setVariable("i18_Qty", messages.qty());
		t.setVariable("i18_Unit_Price", messages.unitPrice());
		t.setVariable("i18_Discount", messages.discount());
		t.setVariable("i18_Total_Price", messages.totalPrice());
		t.setVariable("i18_TOTAL", messages.total());
		t.setVariable("i18_Sub_Total", messages.subTotal());
		t.setVariable("i18_VATRate", messages.vatRate());
		t.setVariable("i18_VATAmount", messages.tax());
		t.setVariable("i18_NetAmount", messages.netAmount());
	}

	public String forNullValue(String value) {
		return value != null ? value : "";
	}

	public String forUnusedAddress(String add, boolean isFooter) {
		if (isFooter) {
			if (add != null && !add.equals(""))
				return add + ",&nbsp;";
		} else {
			if (add != null && !add.equals(""))
				return add + "<br/>&nbsp;";
		}
		return "";
	}

	public String forZeroAmounts(String amount) {
		String[] amt = amount.replace(".", "-").split("-");
		if (amt[0].equals("0")) {
			return "";
		}
		return amount;
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

	private StringBuffer getImage() {
		StringBuffer original = new StringBuffer();
		// original.append("<img style='width:90px;height:90px'  src='file:///");
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
		return original;
	}

	@Override
	public String getFooter() {
		Location location = memo.getLocation();
		String regestrationAddress = "";
		Address reg = company.getRegisteredAddress();

		if (reg != null)
			regestrationAddress = ("&nbsp;Registered Address: "
					+ reg.getAddress1()
					+ forUnusedAddress(reg.getStreet(), true)
					+ forUnusedAddress(reg.getCity(), true)
					+ forUnusedAddress(reg.getStateOrProvinence(), true)
					+ forUnusedAddress(reg.getZipOrPostalCode(), true)
					+ forUnusedAddress(reg.getCountryOrRegion(), true) + ".");

		// TODO For setting the Contact Details
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

}
