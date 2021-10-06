package com.vimukti.accounter.core;

public class CreditNoteTemplete extends TemplateBuilder implements ITemplate {

	private CustomerCreditMemo memo;
	private int maxDecimalPoints;

	public CreditNoteTemplete(CustomerCreditMemo memo, String footerImageUrl,
			String stylefile) {
		super(memo.getCompany());
		this.memo = memo;
		this.maxDecimalPoints = getMaxDecimals(memo);
		imgUrl = footerImageUrl;
		style1 = stylefile;
		init();
	}

	/**
	 * HEADER
	 */
	@Override
	public void initHeader() {

		String cmpAdd = "<br/><br/><br/><br/><br/>";
		Address registeredAddress = company.getRegisteredAddress();
		if (registeredAddress != null) {
			cmpAdd = ("<br><font style=\"font-family:sans-serif; color:#504040;\">&nbsp;"
					+ forUnusedAddress(registeredAddress.getAddress1(), false)
					+ forUnusedAddress(registeredAddress.getStreet(), false)
					+ forUnusedAddress(registeredAddress.getCity(), false)
					+ forUnusedAddress(
							registeredAddress.getStateOrProvinence(), false)
					+ forUnusedAddress(registeredAddress.getZipOrPostalCode(),
							false)
					+ forUnusedAddress(registeredAddress.getCountryOrRegion(),
							false) + "</font></br>");
		}

		cmpAdd = ("<p><font style=\"font-family:sans-serif;\" size=\"6px\"><strong> "
				+ forNullValue(TemplateBuilder.getCmpName())
				+ "</strong></font>" + cmpAdd + "</p>");

		headerHtml = ("<table style=\"width: 100%; height: 100%;\" cellspacing=\"10\" ><tr><td style=\"vertical-align: top;\" align=\"left\"><div class=\"gwt-HTML\" style=\" margin-left: 43px;\">"
				+ cmpAdd
				+ "</div></td><td style=\"vertical-align: top;\" align=\"right\"><div class=\"gwt-HTML\" style=\" margin-right: 43px;\"><p ><font color=\"black\" style=\"font-family:sans-serif;\" size=\"6px\"><strong>Credit Note</strong></font></p><div><table style=\"width:306px;font-size: 13px; border-collapse: collapse; table-layout: fixed; font-family: sans-serif;\"><colgroup><col></colgroup><tr><td style=\"border: 1px ridge grey; padding: 6px; background: #f2f2f2; font-weight: bold; \"> Credit Note Number</td><td align=\"right\" width=\"55%\" style=\"border: 1px ridge grey; padding: 6px;\">"
				+ forNullValue(memo.getNumber())
				+ "</td></tr><tr><td style=\"border: 1px ridge grey; padding: 6px; background: #f2f2f2; font-weight: bold; \">Credit Note Date</td><td align=\"right\" width=\"55%\" style=\"border: 1px ridge grey; padding: 6px;\">"
				+ memo.getDate()
				+ "</td></tr><tr><td style=\"border: 1px ridge grey; padding: 6px; background: #f2f2f2; font-weight: bold; \">Customer Number</td><td align=\"right\" width=\"55%\" style=\"border: 1px ridge grey; padding: 6px;\">"
				+ forNullValue(memo.getCustomer().getNumber()) + "</td></tr></table></div></div></td></tr></table>");

	}

	public String getHeader() {
		return this.headerHtml;
	}

	/**
	 * BODY
	 */
	@Override
	public void initBody() {
		String addressHtml;
		String itemsHtml;

		String billAdrs = "<div align=\"left\">&nbsp;"
				+ forUnusedAddress(memo.getCustomer().getName(), false)
				+ "</div>";
		Address bill = memo.getBillingAddress();
		if (bill != null) {
			billAdrs = "<div align=\"left\">&nbsp;"
					+ forUnusedAddress(memo.getCustomer().getName(), false)
					+ forUnusedAddress(bill.getAddress1(), false)
					+ forUnusedAddress(bill.getStreet(), false)
					+ forUnusedAddress(bill.getCity(), false)
					+ forUnusedAddress(bill.getStateOrProvinence(), false)
					+ forUnusedAddress(bill.getZipOrPostalCode(), false)
					+ bill.getCountryOrRegion() + "</div>";
		}

		addressHtml = "<table style=\"width: 100%; height: 100%;\" ><tr><td style=\"vertical-align: top;\" align=\"left\"><table style=\"width: 280px; height: 100%;\" class=\"gridHeader\"><colgroup><col></colgroup><tr><td class=\"gridHeaderBackGround\"><center>Credit To</center></td></tr><tr><td align=\"left\" style=\"vertical-align: top;padding: 6px;height:105px;\">"
				+ billAdrs + "</td></tr></table>";

		String recordsHtml = "<tr class=\"item-row\"><td class=\"description\"><div></div></td><td class=\"qty\"><div></div></td><td><div class=\"cost\"></div></td><td><div class=\"price\"></div></td><td class=\"vatRate\"><span ></span></td><td class=\"vatAmount\"><div></div></td></tr>";
		if (!memo.getTransactionItems().isEmpty()) {
			recordsHtml = "";
			for (TransactionItem item : memo.getTransactionItems()) {
				recordsHtml = recordsHtml
						+ "<tr class=\"item-row\"><td style=\"padding: 6px;\" class=\"description\"><div>"
						+ forNullValue(item.getDescription())
						+ "</div></td><td style=\"padding: 6px;\" align=\"right\" class=\"qty\"><div>"
						+ (forZeroAmounts(getDecimalsUsingMaxDecimals(item
								.getQuantity().getValue(), null,
								maxDecimalPoints)))
						+ "</div></td><td style=\"padding: 6px;\" align=\"right\"><div class=\"cost\">"
						+ (forZeroAmounts(largeAmountConversation(item
								.getUnitPrice())))
						+ "</div></td><td style=\"padding: 6px;\" align=\"right\"><div class=\"price\">"
						+ largeAmountConversation(item.getLineTotal())
						+ (company.getPreferences().isTrackTax() ? "</div></td><td style=\"padding: 6px;\" align=\"right\" class=\"vatRate\"><span >"
								+ Utility.getVATItemRate(item.getTaxCode(),
										true)
								+ "%</span></td><td style=\"padding: 6px;\" align=\"right\" class=\"vatAmount\"><div>"
								+ item.getVATfraction() == null ? " "
								: getDecimalsUsingMaxDecimals(
										item.getVATfraction(), null, 2)
								: "") + "</div></td></tr>";
			}
		}
		itemsHtml = ("<table id=\"items\"><tr><th>Description</th><th>Qty</th><th>Unit Price</th><th>Total Price</th><th>VAT Rate</th><th>VAT Amount</th></tr>"
				+ recordsHtml
				+ "</table><table id=\"totals\"><tr><td class=\"blank\" style=\"padding: 5px;\">"
				+ forNullValue(memo.getMemo())
				+ "</td><td class=\"total-line\" >&nbsp;&nbsp;Sub Total</td><td class=\"total-value\"><div id=\"subtotal\">"
				+ largeAmountConversation(memo.getNetAmount())
				+ "&nbsp;&nbsp;</div></td></tr><tr><td class=\"blank\" ></td> <td class=\"total-line\" >&nbsp;&nbsp;VAT Total</td><td class=\"total-value\"><div id=\"total\">"
				+ largeAmountConversation(memo.getTotal() - memo.getNetAmount())
				+ "&nbsp;&nbsp;</div></td></tr><tr><td class=\"blank\" > </td><td class=\"total-line balance\">&nbsp;&nbsp;TOTAL</td><td class=\"total-value balance\"><div id=\"due\">"
				+ largeAmountConversation(memo.getTotal()) + "&nbsp;&nbsp;</div></td></tr></table>");

		bodyHtml = addressHtml + itemsHtml;

	}

	/**
	 * FOOTER
	 */
	@Override
	public void initFooter() {

		String regAdd = "&nbsp;";

		Address tradingAddress = company.getTradingAddress();
		if (tradingAddress != null) {
			regAdd = (",&nbsp;Register Address: "
					+ forUnusedAddress(tradingAddress.getAddress1(), true)
					+ forUnusedAddress(tradingAddress.getStreet(), true)
					+ forUnusedAddress(tradingAddress.getCity(), true)
					+ forUnusedAddress(tradingAddress.getStateOrProvinence(),
							true)
					+ forUnusedAddress(tradingAddress.getZipOrPostalCode(),
							true) + tradingAddress.getCountryOrRegion() + ".");
		}

		regAdd = (company.getTradingName() + regAdd + ((company
				.getRegistrationNumber() != null && !company
				.getRegistrationNumber().equals("")) ? "<br/>Company Registration No: "
				+ company.getRegistrationNumber()
				: ""));

		footerHtml = ("<table style=\"width: 100%; height: 100%; table-layout:fixed;\" border=\"0\" ><tr><td><table style=\"width: 100%; height: 100%; font-family:sans-serif; color:#505050;\" ><tr><td style=\"vertical-align: top;\" align=\"left\" colspan=\"2\"><table style=\"font-size:13px; width: 105%; height: 100%; margin-left:56px;  border-color: #E1E1E1; border-collapse:collapse;\" border=\"1\"><colgroup><col></colgroup><tr><td><center>VAT No : "
				+ forNullValue(company.getPreferences()
						.getVATregistrationNumber())
				+ "</center></td><td><center>Sort Code : "
				+ forNullValue(company.getSortCode())
				+ "</center></td><td><center>Bank Account No :"
				+ forNullValue(company.getBankAccountNo())
				+ "</center></td></tr></table></td></tr><tr><td style=\"vertical-align: top;\" align=\"center\"><table  style=\"width: 100%; height: 100%; padding:3.5px\" ><tr><td align=\"right\" style=\"vertical-align: top;\"<table style=\"margin-left:60px;font-size:13px;width: 95%; height: 37px;  border : 1px solid #E1E1E1;\"><colgroup><col></colgroup><tr><td><center>"
				+ regAdd + "</center></td> </tr>/tbody></table></td></tr></table></td></tr></table></td><td width=\"10%\" style=\"vertical-align: bottom;\"><div align=\"right\" style=\"margin-bottom:14px;\"><img src=\"footerimage\" width=\"67.5px\" height=\"25px\"></div></td></tr></table>");

	}

	public String forNullValue(String value) {
		return value != null ? value : "";
	}

	@Override
	public String getBody() {
		String body = "<html><head><link rel='stylesheet' type='text/css' href='"
				+ style1
				+ "' media='all'></link></head><body>"
				+ this.bodyHtml
				+ "</body></html>";
		body = body.replace("<br>", "<br/>");
		return body;
	}

	@Override
	public String getFooter() {
		this.footerHtml = this.footerHtml.replace("footerimage", imgUrl);
		return this.footerHtml;
	}

	/*
	 * For Max DecimalPoints
	 */
	private int getMaxDecimals(CustomerCreditMemo memo) {
		String qty;
		String max;
		int temp = 0;
		for (TransactionItem item : memo.getTransactionItems()) {
			qty = String.valueOf(item.getQuantity());
			max = qty.substring(qty.indexOf(".") + 1);
			if (!max.equals("0")) {
				if (temp < max.length()) {
					temp = max.length();
				}
			}

		}
		return temp;
	}

	private String getDecimalsUsingMaxDecimals(double quantity, String amount,
			int maxDecimalPoint) {
		String qty = "";
		String max;
		if (maxDecimalPoint != 0) {
			if (amount == null)
				qty = String.valueOf(quantity);
			else
				qty = amount;
			max = qty.substring(qty.indexOf(".") + 1);
			if (maxDecimalPoint > max.length()) {
				for (int i = max.length(); maxDecimalPoint != i; i++) {
					qty = qty + "0";
				}
			}
		} else {
			qty = String.valueOf((long) quantity);
		}

		String temp = qty.contains(".") ? qty.replace(".", "-").split("-")[0]
				: qty;
		return insertCommas(temp)
				+ (qty.contains(".") ? "."
						+ qty.replace(".", "-").split("-")[1] : "");
	}

	private static String insertCommas(String str) {

		if (str.length() < 4) {
			return str;
		}
		return insertCommas(str.substring(0, str.length() - 3)) + ","
				+ str.substring(str.length() - 3, str.length());
	}

	@Override
	public String getFileName() {
		return "CreditNote_Templete";
	}

	private String largeAmountConversation(double amount) {
		String amt = Utility.decimalConversation(amount, memo.getCurrency()
				.getSymbol());
		amt = getDecimalsUsingMaxDecimals(0.0, amt, 2);
		return (amt);
	}

	public String forZeroAmounts(String amount) {
		String[] amt = amount.replace(".", "-").split("-");
		if (amt[0].equals("0")) {
			return "";
		}
		return amount;
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
}
