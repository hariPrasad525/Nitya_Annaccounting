package com.vimukti.accounter.web.client.ui.vat;

import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SimplePanel;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTDSChalanDetail;
import com.vimukti.accounter.web.client.core.ClientTDSTransactionItem;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.UIUtils;

public class TDSChallanItem extends SimplePanel {

	private final HTML html;
	ClientTDSChalanDetail challan;
	private AccounterMessages messages;

	public TDSChallanItem(ClientTDSChalanDetail challan) {
		this.challan = challan;
		this.html = new HTML();
		messages = Accounter.getMessages();
		this.add(html);
		initItem();
	}

	/**
	 * this method creates the html code for the array
	 * 
	 * @param challan
	 */
	private void initItem() {
		SafeHtmlBuilder sb = new SafeHtmlBuilder();
		sb.appendHtmlConstant("<br>");
		sb.appendHtmlConstant("<table width='100%' class='historyItem'>");

		sb.appendHtmlConstant("<tr>");
		sb.appendHtmlConstant("<td>");
		sb.appendEscaped(messages.challanSerialNo() + " : "
				+ challan.getChalanSerialNumber());
		sb.appendHtmlConstant("</td>");

		sb.appendHtmlConstant("<td>");
		sb.appendEscaped(messages.bankBSRCode() + " : "
				+ challan.getBankBsrCode());
		sb.appendHtmlConstant("</td>");

		sb.appendHtmlConstant("<td>");
		sb.appendEscaped(messages.natureOfPayment() + " : "
				+ challan.getPaymentSection());
		sb.appendHtmlConstant("</td>");
		sb.appendHtmlConstant("</tr>");

		sb.appendHtmlConstant("<tr>");
		sb.appendHtmlConstant("<td>");
		sb.appendEscaped(messages.dateOnTaxPaid()
				+ " : "
				+ UIUtils.getDateByCompanyType(new ClientFinanceDate(challan
						.getDateTaxPaid())));
		sb.appendHtmlConstant("</td>");

		sb.appendHtmlConstant("<td>");
		sb.appendEscaped(messages.paymentMethod()
				+ " : "
				+ (challan.getPaymentMethod() != null ? challan
						.getPaymentMethod() : ""));
		sb.appendHtmlConstant("</td>");

		if ((challan.getPaymentMethod() != null && challan.getPaymentMethod()
				.equals(Accounter.getMessages().cheque()))
				|| challan.getCheckNumber() != 0) {
			sb.appendHtmlConstant("<td>");
			sb.appendEscaped(messages.chequeNo() + " : "
					+ challan.getCheckNumber());
			sb.appendHtmlConstant("</td>");
		}
		sb.appendHtmlConstant("</tr>");

		sb.appendHtmlConstant("<tr>");
		sb.appendHtmlConstant("<td>");
		sb.appendEscaped(messages.incomeTax() + challan.getIncomeTaxAmount());
		sb.appendHtmlConstant("</td>");

		sb.appendHtmlConstant("<td>");
		sb.appendEscaped(messages.surchargePaid() + " : "
				+ challan.getSurchangePaidAmount());
		sb.appendHtmlConstant("</td>");

		sb.appendHtmlConstant("<td>");
		sb.appendEscaped(messages.educationCess() + " : "
				+ challan.getEducationCessAmount());
		sb.appendHtmlConstant("</td>");
		sb.appendHtmlConstant("</tr>");

		sb.appendHtmlConstant("<tr>");
		sb.appendHtmlConstant("<td>");
		sb.appendEscaped(messages.interestPaid() + " : "
				+ challan.getInterestPaidAmount());
		sb.appendHtmlConstant("</td>");

		sb.appendHtmlConstant("<td>");
		sb.appendEscaped(messages.otherAmountPaid() + " : "
				+ challan.getOtherAmount());
		sb.appendHtmlConstant("</td>");

		sb.appendHtmlConstant("<td>");
		sb.appendEscaped(messages.total() + " : " + challan.getTotal());
		sb.appendHtmlConstant("</td>");
		sb.appendHtmlConstant("</tr>");

		sb.appendHtmlConstant("</table>");

		sb.appendHtmlConstant("<table border='1'>");
		if (challan.getTdsTransactionItems().size() > 1) {
			appendItemsHeader(sb);
		}

		int srNo = 0;
		for (ClientTDSTransactionItem item : challan.getTdsTransactionItems()) {
			srNo++;
			appendItemDetails(sb, item, srNo);
		}
		sb.appendHtmlConstant("</table>");
		html.setHTML(sb.toSafeHtml());

	}

	private void appendItemDetails(SafeHtmlBuilder sb,
			ClientTDSTransactionItem item, int srNo) {

		sb.appendHtmlConstant("<tr>");

		sb.appendHtmlConstant("<td>");
		sb.appendEscaped(String.valueOf(srNo));
		sb.appendHtmlConstant("</td>");

		sb.appendHtmlConstant("<td>");
		sb.appendEscaped(String.valueOf(item.getDeducteeCode()));
		sb.appendHtmlConstant("</td>");

		sb.appendHtmlConstant("<td>");
		sb.appendEscaped(Accounter.getCompany().getVendor(item.getVendor())
				.getTaxId());
		sb.appendHtmlConstant("</td>");

		sb.appendHtmlConstant("<td>");
		sb.appendEscaped(Accounter.getCompany().getVendor(item.getVendor())
				.getName());
		sb.appendHtmlConstant("</td>");

		sb.appendHtmlConstant("<td>");
		sb.appendEscaped(UIUtils.getDateByCompanyType(new ClientFinanceDate(
				challan.getDateTaxPaid())));
		sb.appendHtmlConstant("</td>");

		sb.appendHtmlConstant("<td>");
		sb.appendEscaped(DataUtils.getAmountAsStringInPrimaryCurrency(item
				.getTotalAmount()));
		sb.appendHtmlConstant("</td>");

		sb.appendHtmlConstant("<td>");
		sb.appendEscaped(challan.isBookEntry() ? "Y" : "N");
		sb.appendHtmlConstant("</td>");

		sb.appendHtmlConstant("<td>");
		sb.appendEscaped(DataUtils.getAmountAsStringInPrimaryCurrency(item
				.getTdsAmount()));
		sb.appendHtmlConstant("</td>");

		sb.appendHtmlConstant("<td>");
		sb.appendEscaped(DataUtils.getAmountAsStringInPrimaryCurrency(item
				.getSurchargeAmount()));
		sb.appendHtmlConstant("</td>");

		sb.appendHtmlConstant("<td>");
		sb.appendEscaped(DataUtils.getAmountAsStringInPrimaryCurrency(item
				.getEduCess()));
		sb.appendHtmlConstant("</td>");

		sb.appendHtmlConstant("<td>");
		sb.appendEscaped(DataUtils.getAmountAsStringInPrimaryCurrency(item
				.getTotalTax()));
		sb.appendHtmlConstant("</td>");

		sb.appendHtmlConstant("<td>");
		sb.appendEscaped(DataUtils.getAmountAsStringInPrimaryCurrency(item
				.getTotalTax()));
		sb.appendHtmlConstant("</td>");

		sb.appendHtmlConstant("<td>");
		sb.appendEscaped(UIUtils.getDateByCompanyType(new ClientFinanceDate(
				item.getTransactionDate())));
		sb.appendHtmlConstant("</td>");

		sb.appendHtmlConstant("<td>");
		sb.appendEscaped(String.valueOf(item.getTaxRate()));
		sb.appendHtmlConstant("</td>");

		sb.appendHtmlConstant("<td>");
		sb.appendEscaped(item.getRemark());
		sb.appendHtmlConstant("</td>");

		sb.appendHtmlConstant("</tr>");

	}

	private void appendItemsHeader(SafeHtmlBuilder sb) {
		sb.appendHtmlConstant("<tr>");

		sb.appendHtmlConstant("<th>");
		sb.appendEscaped(messages.SRNo());
		sb.appendHtmlConstant("</th>");

		sb.appendHtmlConstant("<th>");
		sb.appendEscaped(messages.deducteeCode());
		sb.appendHtmlConstant("</th>");

		sb.appendHtmlConstant("<th>");
		sb.appendEscaped(messages.panOfTheDeductee());
		sb.appendHtmlConstant("</th>");

		sb.appendHtmlConstant("<th>");
		sb.appendEscaped(messages.nameOfTheDeductee());
		sb.appendHtmlConstant("</th>");

		sb.appendHtmlConstant("<th>");
		sb.appendEscaped(messages.dateOfPaymentOrCredit());
		sb.appendHtmlConstant("</th>");

		sb.appendHtmlConstant("<th>");
		sb.appendEscaped(messages.amountPaidOrCreditedRs());
		sb.appendHtmlConstant("</th>");

		sb.appendHtmlConstant("<th>");
		sb.appendEscaped(messages.paidByBookEntryOrOtherwise());
		sb.appendHtmlConstant("</th>");

		sb.appendHtmlConstant("<th>");
		sb.appendEscaped(messages.tdsRs());
		sb.appendHtmlConstant("</th>");

		sb.appendHtmlConstant("<th>");
		sb.appendEscaped(messages.surchargeRs());
		sb.appendHtmlConstant("</th>");

		sb.appendHtmlConstant("<th>");
		sb.appendEscaped(messages.educationCessRs());
		sb.appendHtmlConstant("</th>");

		sb.appendHtmlConstant("<th>");
		sb.appendEscaped(messages.totalTaxDeducted());
		sb.appendHtmlConstant("</th>");

		sb.appendHtmlConstant("<th>");
		sb.appendEscaped(messages.totalTaxDeposited());
		sb.appendHtmlConstant("</th>");

		sb.appendHtmlConstant("<th>");
		sb.appendEscaped(messages.dateOfDeduction());
		sb.appendHtmlConstant("</th>");

		sb.appendHtmlConstant("<th>");
		sb.appendEscaped(messages.rateAtWhichDeducted());
		sb.appendHtmlConstant("</th>");

		sb.appendHtmlConstant("<th>");
		sb.appendEscaped(messages.remarkTDSChallan());
		sb.appendHtmlConstant("</th>");

		sb.appendHtmlConstant("</tr>");
	}
}
