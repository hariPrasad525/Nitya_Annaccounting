package com.nitya.accounter.web.client.ui.combo;

import java.util.List;

import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientAccount;
import com.nitya.accounter.web.client.core.Utility;
import com.nitya.accounter.web.client.ui.company.NewAccountAction;
import com.nitya.accounter.web.client.ui.core.ActionCallback;

public class TDSSectionCombo extends CustomCombo<ClientAccount> {

	private List<Integer> accounTypes;
	private final boolean useAccountNumbers;

	public TDSSectionCombo(String title) {
		this(title, true);
	}

	public TDSSectionCombo(String title, boolean b) {
		super(title, b, 3, "tdsSectionCombo");
		this.useAccountNumbers = Global.get().preferences()
				.getUseAccountNumbers();
	}

	@Override
	public String getDefaultAddNewCaption() {
		return messages.paymentMethod();
	}

	public void init() {

	}

	@Override
	protected String getDisplayName(ClientAccount object) {
		if (object != null)
			return object.getDisplayName() != null ? object.getDisplayName()
					: object.getName();
		else
			return "";
	}

	@Override
	public void onAddNew() {
		NewAccountAction action = new NewAccountAction();
		action.setCallback(new ActionCallback<ClientAccount>() {

			@Override
			public void actionResult(ClientAccount result) {
				if (result.getIsActive())
					addItemThenfireEvent(result);
			}
		});

		action.run(null, true);
	}

	/**
	 * @param accountList
	 * 
	 */
	public void setAccountTypes(List<Integer> accounTypes) {
		this.accounTypes = accounTypes;
	}

	/**
	 * @return the accountTypes
	 */
	public List<Integer> getAccountTypes() {
		return accounTypes;
	}

	@Override
	protected String getColumnData(ClientAccount object, int col) {

		switch (col) {
		case 0:
			if (useAccountNumbers) {
				return object.getNumber();
			} else {
				return "";
			}
		case 1:
			return getDisplayName(object);
		case 2:
			return Utility.getAccountTypeString(object.getType());
		default:
			break;
		}
		return null;
	}
}
