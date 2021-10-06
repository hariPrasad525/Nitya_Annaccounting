package com.nitya.accounter.web.client.ui.settings;

import java.util.List;

import com.nitya.accounter.web.client.core.ClientEmployeeAttendance;
import com.nitya.accounter.web.client.core.ClientTransactionItem;
import com.nitya.accounter.web.client.ui.core.ICurrencyProvider;
import com.nitya.accounter.web.client.ui.edittable.EditTable;

public abstract class AbstractEmpAttendanceTable extends EditTable<ClientEmployeeAttendance> {
	
	private String payrollDate;
	private String employeeId;
	private String milesPerHour;
	private String advances;
	private String foodAllowence;
	private String otherAllowances;
	
	
	
	public void updateTotals() {

		List<ClientEmployeeAttendance> allrecords = getAllRows();
		payrollDate = null;
		employeeId = null;
		milesPerHour = null;
		advances = null;
		foodAllowence = null;
		otherAllowances = null;

		/*for (ClientEmployeeAttendance record : allrecords) {
			if (record.getEmployeeId() != null || record.getId() != 0) {
				int type = record.getID();

				if (type == 0) {
					continue;
				}
				if (record.getDiscount() != null) {
					totaldiscount += record.getDiscount();
				}

				Double lineTotalAmt = record.getLineTotal();
				if (lineTotalAmt != null) {
					lineTotal += lineTotalAmt;
				}

				if (record != null && record.isTaxable()) {
					// ClientTAXItem taxItem = getCompany().getTAXItem(
					// citem.getTaxCode());
					// if (taxItem != null) {
					// totalVat += taxItem.getTaxRate() / 100 * lineTotalAmt;
					// }
					taxableLineTotal += lineTotalAmt;

					double taxAmount = getVATAmount(record.getTaxCode(), record);
					if (isShowPriceWithVat()) {
						lineTotal -= taxAmount;
					}
					record.setVATfraction(taxAmount);
					totalTax += record.getVATfraction();

				}
			} else {

			}

			// super.update(record);
			// totalVat += citem.getVATfraction();
		}

		// if (getCompany().getPreferences().isChargeSalesTax()) {
		grandTotal = totalTax + lineTotal;
		// } else {
		// grandTotal = totallinetotal;
		// totalValue = grandTotal;
		// }
		// if (getCompany().getPreferences().isRegisteredForVAT()) {

		// grandTotal = totallinetotal;
		// totalValue = grandTotal + totalTax;
		// }
		// } else {
		// grandTotal = totallinetotal;
		// totalValue = grandTotal;
		// }

		updateNonEditableItems();
*/
	}
}
