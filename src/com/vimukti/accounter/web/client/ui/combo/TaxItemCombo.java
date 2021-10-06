package com.vimukti.accounter.web.client.ui.combo;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientTAXAgency;
import com.vimukti.accounter.web.client.core.ClientTAXItem;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.core.ActionCallback;
import com.vimukti.accounter.web.client.ui.vat.NewVatItemAction;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class TaxItemCombo extends CustomCombo<ClientTAXItem> {

	int type;

	public TaxItemCombo(String title, int type) {
		super(title, true, 1, "TaxItemCombo");
		this.type = type;
		initCombo(TaxItemsByType(getCompany().getActiveTaxItems(), this.type));
	}

	private List<ClientTAXItem> TaxItemsByType(
			ArrayList<ClientTAXItem> arrayList, int type2) {

		List<ClientTAXItem> taxItemList = new ArrayList<ClientTAXItem>();
		for (ClientTAXItem taxItem : arrayList) {

			if (type == ClientTAXAgency.TAX_TYPE_TDS) {
				if (getCompany().getTaxAgency(taxItem.getTaxAgency())
						.getTaxType() == ClientTAXAgency.TAX_TYPE_TDS) {
					taxItemList.add(taxItem);
				}

			} else if (type == ClientTAXAgency.TAX_TYPE_VAT) {
				if (getCompany().getTaxAgency(taxItem.getTaxAgency())
						.getTaxType() == ClientTAXAgency.TAX_TYPE_VAT) {
					taxItemList.add(taxItem);
				}
			}
		}
		return taxItemList;
	}

	@Override
	public String getDisplayName(ClientTAXItem object) {
		String displayName;

		if (object != null) {
			displayName = object.getName() != null ? object.getName() : "";

			displayName += " - " + object.getTaxRate();

			if (object.isPercentage())
				displayName += "%";
			return displayName;
		} else
			return "";
	}

	@Override
	protected String getColumnData(ClientTAXItem object, int col) {
		switch (col) {
		case 0:
			return getDisplayName(object);
		case 1:
			return DataUtils.getAmountAsStringInPrimaryCurrency(object
					.getTaxRate()) + "%";
		}
		return null;
	}

	@Override
	public String getDefaultAddNewCaption() {
		return messages.item();

	}

	@Override
	public void onAddNew() {
		// if (getCompany().getAccountingType() ==
		// ClientCompany.ACCOUNTING_TYPE_INDIA) {
		NewVatItemAction action = new NewVatItemAction();
		action.setCallback(new ActionCallback<ClientTAXItem>() {

			@Override
			public void actionResult(ClientTAXItem result) {

				addItemThenfireEvent(result);

			}
		});

		action.run(null, true);

		// }
	}
}
