package com.vimukti.accounter.web.client.ui.combo;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientTAXAgency;
import com.vimukti.accounter.web.client.core.ClientTAXItem;
import com.vimukti.accounter.web.client.ui.core.ActionCallback;
import com.vimukti.accounter.web.client.ui.vat.NewVatItemAction;

/**
 * @author Murali.A
 * 
 */
public class AdjustmentVATItemCombo extends CustomCombo<ClientTAXItem> {

	private ClientTAXAgency taxAgency;

	/**
	 * @param title
	 */
	public AdjustmentVATItemCombo(String title) {
		super(title, "AdjustmentVATItemCombo");
		initCombo(getVATItmes());
	}

	public AdjustmentVATItemCombo(String title, boolean isAddNewRequired) {
		super(title, isAddNewRequired, 1, "AdjustmentVATItemCombo");
		initCombo(getVATItmes());
	}

	public AdjustmentVATItemCombo(String title, ClientTAXAgency taxAgency) {
		super(title, "AdjustmentVATItemCombo");
		this.taxAgency = taxAgency;
		initCombo(getVATItmes());

	}

	/* This method returns the VATItems for a VATAgency */
	public List<ClientTAXItem> getVATItmesByVATAgncy(ClientTAXAgency taxAgency) {
		List<ClientTAXItem> vatItmsList = new ArrayList<ClientTAXItem>();
		if (taxAgency != null) {
			for (ClientTAXItem vatItem : getCompany().getActiveTaxItems()) {
				if (vatItem.getTaxAgency() == (taxAgency.getID())) {
					vatItmsList.add(vatItem);
				}
			}
		}
		return vatItmsList;
	}

	/* VATItmes whose 'isPercentage' is false, only allowed into the list */
	List<ClientTAXItem> getVATItmes() {
		List<ClientTAXItem> vatItmsList = new ArrayList<ClientTAXItem>();
		for (ClientTAXItem vatItem : getCompany().getActiveTaxItems()) {
			if (!vatItem.isPercentage()) {
				vatItmsList.add(vatItem);
			}
		}
		return vatItmsList;
	}

	/* VATItmes whose 'isPercentage' is true, only allowed into the list */
	public List<ClientTAXItem> getFilteredVATItems() {
		// List<ClientTAXItem> vatItmsList = new ArrayList<ClientTAXItem>();
		// for (ClientTAXItem vatItem : getCompany().getTaxItems()) {
		// if (vatItem.isPercentage()) {
		// vatItmsList.add(vatItem);
		// }
		// }
		return getCompany().getActiveTaxItems();
	}

	/*
	 * VATItmes whose 'isPercentage' is true,and "Sales" type VATItems only
	 * allowed into the list
	 */
	public List<ClientTAXItem> getSalesWithPrcntVATItems() {
		// List<ClientTAXItem> vatItmsList = new ArrayList<ClientTAXItem>();
		// for (ClientTAXItem vatItem : getFilteredVATItems()) {
		// if (vatItem.isSalesType()) {
		// vatItmsList.add(vatItem);
		// }
		// }
		return getFilteredVATItems();
	}

	/*
	 * VATItmes whose 'isPercentage' is true,and "Purchase" type VATItems only
	 * allowed into the list
	 */
	public List<ClientTAXItem> getPurchaseWithPrcntVATItems() {
		// List<ClientTAXItem> vatItmsList = new ArrayList<ClientTAXItem>();
		// for (ClientTAXItem vatItem : getFilteredVATItems()) {
		// if (!vatItem.isSalesType()) {
		// vatItmsList.add(vatItem);
		// }
		// }
		return getFilteredVATItems();
	}

	/*
	 * @see
	 * com.vimukti.accounter.web.client.ui.combo.CustomCombo#getDefaultAddNewCaption
	 * ()
	 */
	@Override
	public String getDefaultAddNewCaption() {
		return messages.taxItem();
	}

	/*
	 * @see
	 * com.vimukti.accounter.web.client.ui.combo.CustomCombo#getDisplayName(
	 * java.lang.Object)
	 */
	@Override
	protected String getDisplayName(ClientTAXItem object) {
		if (object != null)
			return object.getName() != null ? object.getName() : "";
		else
			return "";
	}

	/*
	 * @see
	 * com.vimukti.accounter.web.client.ui.combo.CustomCombo#getSelectItemType()
	 */

	/*
	 * @see com.vimukti.accounter.web.client.ui.combo.CustomCombo#onAddNew()
	 */
	@Override
	public void onAddNew() {
		NewVatItemAction action = new NewVatItemAction();
		action.setCallback(new ActionCallback<ClientTAXItem>() {

			@Override
			public void actionResult(ClientTAXItem result) {
				if (result.getName() != null || result.getDisplayName() != null) {
					if (taxAgency == null
							|| (taxAgency != null && result.getTaxAgency() == taxAgency
									.getID()))
						addItemThenfireEvent(result);
				}

			}
		});

		action.run(null, true);
	}

	@Override
	protected String getColumnData(ClientTAXItem object, int col) {
		switch (col) {
		case 0:
			return object.getName();
		}
		return null;
	}

	public void setTaxAgency(ClientTAXAgency taxAgency) {
		this.taxAgency = taxAgency;
	}

}
