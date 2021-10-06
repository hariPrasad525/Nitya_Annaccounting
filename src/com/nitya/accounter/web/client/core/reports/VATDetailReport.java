package com.nitya.accounter.web.client.core.reports;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.nitya.accounter.web.client.core.ClientTAXAgency;

public class VATDetailReport implements IsSerializable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String IRELAND_BOX1_VAT_CHARGED_ON_SUPPIES = "Box 1 Vat charged on supplies of Goods and Services";

	public static final String IRELAND_BOX2_VAT_DUE_ON_INTRA_EC_ACQUISITIONS = "Box 2 VAT due on Intra-EC acquisitions";

	public static final String IRELAND_BOX3_VAT_ON_SALES = "Box 3 T1 - VAT on Sales";

	public static final String IRELAND_BOX4_VAT_ON_PURCHASES = "Box 4 T2 - VAT on Purchases";

	public static final String IRELAND_BOX5_T3_T4_PAYMENT_DUE = "Box 5 T3/T4 (Payment Due if positive, Refund Owed if negative";

	public static final String IRELAND_BOX6_E1_GOODS_TO_EU = "Box 6 E1 Value of goods sent to other EU countries";

	public static final String IRELAND_BOX7_E2_GOODS_FROM_EU = "Box 7 E2 Value of goods received from other EU countries";

	public static final String IRELAND_BOX8_TOTAL_NET_SALES = "Box 8 Total Net Value of Sales";

	public static final String IRELAND_BOX9_TOTAL_NET_PURCHASES = "Box 9 Total Net Value of Purchases";

	public static final String IRELAND_BOX10_UNCATEGORISED = "Uncategorised Tax Amounts";

	LinkedHashMap<String, List<VATDetail>> entries;

	public VATDetailReport(int VATReturnType) {
		entries = new LinkedHashMap<String, List<VATDetail>>();

		if (VATReturnType == ClientTAXAgency.RETURN_TYPE_UK_VAT) {
			entries.put(VATSummary.UK_BOX1_VAT_DUE_ON_SALES,
					new ArrayList<VATDetail>());
			entries.put(VATSummary.UK_BOX2_VAT_DUE_ON_ACQUISITIONS,
					new ArrayList<VATDetail>());
			entries.put(VATSummary.UK_BOX3_TOTAL_OUTPUT,
					new ArrayList<VATDetail>());
			entries.put(VATSummary.UK_BOX4_VAT_RECLAMED_ON_PURCHASES,
					new ArrayList<VATDetail>());
			entries.put(VATSummary.UK_BOX5_NET_VAT, new ArrayList<VATDetail>());
			entries.put(VATSummary.UK_BOX6_TOTAL_NET_SALES,
					new ArrayList<VATDetail>());
			entries.put(VATSummary.UK_BOX7_TOTAL_NET_PURCHASES,
					new ArrayList<VATDetail>());
			entries.put(VATSummary.UK_BOX8_TOTAL_NET_SUPPLIES,
					new ArrayList<VATDetail>());
			entries.put(VATSummary.UK_BOX9_TOTAL_NET_ACQUISITIONS,
					new ArrayList<VATDetail>());
			entries.put(VATSummary.UK_BOX10_UNCATEGORISED,
					new ArrayList<VATDetail>());

		} else if (VATReturnType == ClientTAXAgency.RETURN_TYPE_IRELAND_VAT) {
			entries.put(VATDetailReport.IRELAND_BOX1_VAT_CHARGED_ON_SUPPIES,
					new ArrayList<VATDetail>());
			entries
					.put(
							VATDetailReport.IRELAND_BOX2_VAT_DUE_ON_INTRA_EC_ACQUISITIONS,
							new ArrayList<VATDetail>());
			entries.put(VATDetailReport.IRELAND_BOX3_VAT_ON_SALES,
					new ArrayList<VATDetail>());
			entries.put(VATDetailReport.IRELAND_BOX4_VAT_ON_PURCHASES,
					new ArrayList<VATDetail>());
			entries.put(VATDetailReport.IRELAND_BOX5_T3_T4_PAYMENT_DUE,
					new ArrayList<VATDetail>());
			entries.put(VATDetailReport.IRELAND_BOX6_E1_GOODS_TO_EU,
					new ArrayList<VATDetail>());
			entries.put(VATDetailReport.IRELAND_BOX7_E2_GOODS_FROM_EU,
					new ArrayList<VATDetail>());
			entries.put(VATDetailReport.IRELAND_BOX8_TOTAL_NET_SALES,
					new ArrayList<VATDetail>());
			entries.put(VATDetailReport.IRELAND_BOX9_TOTAL_NET_PURCHASES,
					new ArrayList<VATDetail>());
			entries.put(VATDetailReport.IRELAND_BOX10_UNCATEGORISED,
					new ArrayList<VATDetail>());
		}
	}

	public VATDetailReport() {

		entries = new LinkedHashMap<String, List<VATDetail>>();

		entries.put(VATSummary.UK_BOX1_VAT_DUE_ON_SALES,
				new ArrayList<VATDetail>());
		entries.put(VATSummary.UK_BOX2_VAT_DUE_ON_ACQUISITIONS,
				new ArrayList<VATDetail>());
		entries
				.put(VATSummary.UK_BOX3_TOTAL_OUTPUT,
						new ArrayList<VATDetail>());
		entries.put(VATSummary.UK_BOX4_VAT_RECLAMED_ON_PURCHASES,
				new ArrayList<VATDetail>());
		entries.put(VATSummary.UK_BOX5_NET_VAT, new ArrayList<VATDetail>());
		entries.put(VATSummary.UK_BOX6_TOTAL_NET_SALES,
				new ArrayList<VATDetail>());
		entries.put(VATSummary.UK_BOX7_TOTAL_NET_PURCHASES,
				new ArrayList<VATDetail>());
		entries.put(VATSummary.UK_BOX8_TOTAL_NET_SUPPLIES,
				new ArrayList<VATDetail>());
		entries.put(VATSummary.UK_BOX9_TOTAL_NET_ACQUISITIONS,
				new ArrayList<VATDetail>());
		entries.put(VATSummary.UK_BOX10_UNCATEGORISED,
				new ArrayList<VATDetail>());

		entries.put(VATDetailReport.IRELAND_BOX1_VAT_CHARGED_ON_SUPPIES,
				new ArrayList<VATDetail>());
		entries.put(
				VATDetailReport.IRELAND_BOX2_VAT_DUE_ON_INTRA_EC_ACQUISITIONS,
				new ArrayList<VATDetail>());
		entries.put(VATDetailReport.IRELAND_BOX3_VAT_ON_SALES,
				new ArrayList<VATDetail>());
		entries.put(VATDetailReport.IRELAND_BOX4_VAT_ON_PURCHASES,
				new ArrayList<VATDetail>());
		entries.put(VATDetailReport.IRELAND_BOX5_T3_T4_PAYMENT_DUE,
				new ArrayList<VATDetail>());
		entries.put(VATDetailReport.IRELAND_BOX6_E1_GOODS_TO_EU,
				new ArrayList<VATDetail>());
		entries.put(VATDetailReport.IRELAND_BOX7_E2_GOODS_FROM_EU,
				new ArrayList<VATDetail>());
		entries.put(VATDetailReport.IRELAND_BOX8_TOTAL_NET_SALES,
				new ArrayList<VATDetail>());
		entries.put(VATDetailReport.IRELAND_BOX9_TOTAL_NET_PURCHASES,
				new ArrayList<VATDetail>());
		entries.put(VATDetailReport.IRELAND_BOX10_UNCATEGORISED,
				new ArrayList<VATDetail>());

	}

	/**
	 * @return the entries
	 */
	public LinkedHashMap<String, List<VATDetail>> getEntries() {
		return entries;
	}

	/**
	 * @param entries
	 *            the entries to set
	 */
	public void setEntries(LinkedHashMap<String, List<VATDetail>> entries) {
		this.entries = entries;
	}

}
