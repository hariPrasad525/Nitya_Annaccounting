package com.nitya.accounter.web.client.ui.combo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.nitya.accounter.web.client.core.ClientTAXAgency;
import com.nitya.accounter.web.client.core.ClientTAXGroup;
import com.nitya.accounter.web.client.core.ClientTAXItem;
import com.nitya.accounter.web.client.core.ClientTAXItemGroup;
import com.nitya.accounter.web.client.ui.core.ActionCallback;
import com.nitya.accounter.web.client.ui.core.ViewManager;
import com.nitya.accounter.web.client.ui.customers.TaxDialog;

/**
 * @author Murali.A
 * 
 */
public class VATItemCombo extends CustomCombo<ClientTAXItemGroup> {

	/**
	 * @param title
	 */
	public VATItemCombo(String title) {
		super(title, "VATItemCombo");
		initCombo(getVATItmes());
	}

	public VATItemCombo(String title, boolean isAddNewRequired) {
		super(title, isAddNewRequired, 1, "VATItemCombo");
		initCombo(getVATItmes());
	}

	public VATItemCombo(String title, ClientTAXAgency taxAgency) {
		super(title, "VATItemCombo");
		initCombo(getVATItmes());

	}

	/* This method returns the VATItems for a VATAgency */
	public List<ClientTAXItemGroup> getVATItmesByVATAgncy(
			ClientTAXAgency taxAgency) {
		List<ClientTAXItemGroup> vatItmsList = new ArrayList<ClientTAXItemGroup>();
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
	List<ClientTAXItemGroup> getVATItmes() {
		List<ClientTAXItemGroup> vatItmsList = new ArrayList<ClientTAXItemGroup>();
		ArrayList<ClientTAXItemGroup> taxItemGroups = getCompany()
				.getTaxItemGroups();
		taxItemGroups.addAll(getCompany().getActiveTaxItems());
		for (ClientTAXItemGroup vatItem : taxItemGroups) {
			if (!vatItem.isPercentage()) {
				vatItmsList.add(vatItem);
			}
		}
		return vatItmsList;
	}

	/* VATItmes whose 'isPercentage' is true, only allowed into the list */
	public List<ClientTAXItemGroup> getFilteredVATItems(boolean salesItems) {
		List<ClientTAXItemGroup> vatItmsList = new ArrayList<ClientTAXItemGroup>();
		ArrayList<ClientTAXGroup> taxGroups = getCompany().getTaxGroups();
		Iterator<ClientTAXGroup> iterator = taxGroups.iterator();
		while (iterator.hasNext()) {
			List<ClientTAXItem> taxItems = iterator.next().getTaxItems();
			for (ClientTAXItem taxitem : taxItems) {
				ClientTAXAgency taxAgency = getCompany().getTaxAgency(
						taxitem.getTaxAgency());
				if (taxAgency != null) {
					if (taxAgency.getTaxType() == ClientTAXAgency.TAX_TYPE_TDS) {
						iterator.remove();
					}
				}
			}
		}

		vatItmsList.addAll(taxGroups);
		vatItmsList.addAll(getCompany().getActiveTaxItemsWithOutTDS());

		// for (ClientTAXItem vatItem : getCompany().getActiveTaxItems()) {
		// if (vatItem.isPercentage()) {
		// ClientTAXAgency taxAgency = getCompany().getTaxAgency(
		// vatItem.getTaxAgency());
		// if (salesItems) {
		// if (taxAgency.getSalesLiabilityAccount() != 0) {
		// vatItmsList.add(vatItem);
		// }
		// }
		// if (!salesItems) {
		// if (taxAgency.getPurchaseLiabilityAccount() != 0
		// && taxAgency.getTaxType() != ClientTAXAgency.TAX_TYPE_TDS) {
		// vatItmsList.add(vatItem);
		// }
		// }
		//
		// }
		// }
		// vatItmsList.addAll(taxItemGroups);

		return vatItmsList;
	}

	/*
	 * VATItmes whose 'isPercentage' is true,and "Sales" type VATItems only
	 * allowed into the list
	 */
	public List<ClientTAXItemGroup> getSalesWithPrcntVATItems() {
		// List<ClientTAXItemGroup> vatItmsList = new
		// ArrayList<ClientTAXItemGroup>();
		// for (ClientTAXItemGroup vatItem : getFilteredVATItems()) {
		// if (vatItem.isSalesType()) {
		// vatItmsList.add(vatItem);
		// }
		// }
		return getFilteredVATItems(true);
	}

	/*
	 * VATItmes whose 'isPercentage' is true,and "Purchase" type VATItems only
	 * allowed into the list
	 */
	public List<ClientTAXItemGroup> getPurchaseWithPrcntVATItems() {
		// List<ClientTAXItemGroup> vatItmsList = new
		// ArrayList<ClientTAXItemGroup>();
		// for (ClientTAXItemGroup vatItem : getFilteredVATItems()) {
		// if (!vatItem.isSalesType()) {
		// vatItmsList.add(vatItem);
		// }
		// }
		return getFilteredVATItems(false);
	}

	/*
	 * @see
	 * com.nitya.accounter.web.client.ui.combo.CustomCombo#getDefaultAddNewCaption
	 * ()
	 */
	@Override
	public String getDefaultAddNewCaption() {
		return messages.taxItem();
	}

	/*
	 * @see
	 * com.nitya.accounter.web.client.ui.combo.CustomCombo#getDisplayName(
	 * java.lang.Object)
	 */
	@Override
	protected String getDisplayName(ClientTAXItemGroup object) {
		if (object != null)
			return object.getName() != null ? object.getName() : "";
		else
			return "";
	}

	/*
	 * @see
	 * com.nitya.accounter.web.client.ui.combo.CustomCombo#getSelectItemType()
	 */

	/*
	 * @see com.nitya.accounter.web.client.ui.combo.CustomCombo#onAddNew()
	 */
	@Override
	public void onAddNew() {
		TaxDialog dialog = new TaxDialog();
		dialog.setCallback(new ActionCallback<ClientTAXItemGroup>() {

			@Override
			public void actionResult(ClientTAXItemGroup result) {
				addToCombo(result);
			}
		});
		ViewManager.getInstance().showDialog(dialog);
		// NewVatItemAction action = ActionFactory.getNewVatItemAction();
		// action.setCallback(new ActionCallback<ClientTAXItem>() {
		//
		// @Override
		// public void actionResult(ClientTAXItem result) {
		// if (result.getName() != null || result.getDisplayName() != null)
		// addItemThenfireEvent(result);
		//
		// }
		// });
		//
		// action.run(null, true);
	}

	protected void addToCombo(ClientTAXItemGroup result) {
		if (result instanceof ClientTAXItem) {
			ClientTAXItem item = (ClientTAXItem) result;
			ClientTAXAgency taxAgency = getCompany().getTaxAgency(
					item.getTaxAgency());
			if (taxAgency != null
					&& taxAgency.getTaxType() != ClientTAXAgency.TAX_TYPE_TDS) {
				addItemThenfireEvent(result);
			}
		} else {
			ClientTAXGroup group = (ClientTAXGroup) result;
			boolean donnotAdd = false;
			for (ClientTAXItem item : group.getTaxItems()) {
				ClientTAXAgency taxAgency = getCompany().getTaxAgency(
						item.getTaxAgency());
				if (taxAgency != null
						&& taxAgency.getTaxType() == ClientTAXAgency.TAX_TYPE_TDS) {
					donnotAdd = true;
				}
			}
			if (!donnotAdd) {
				addItemThenfireEvent(result);
			}
		}

	}

	@Override
	protected String getColumnData(ClientTAXItemGroup object, int col) {
		switch (col) {
		case 0:
			return object.getName();
		}
		return null;
	}

	public void syncronize(ClientTAXItemGroup selectItem) {
		if (getComboitemsByName(getDisplayName(selectItem)).isEmpty()) {
			addComboItem(selectItem);
		}
	}

}
