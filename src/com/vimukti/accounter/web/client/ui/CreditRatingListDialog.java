package com.vimukti.accounter.web.client.ui;

import java.util.List;

import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientCreditRating;
import com.vimukti.accounter.web.client.core.ClientItemGroup;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.ui.core.GroupDialog;
import com.vimukti.accounter.web.client.ui.core.GroupDialogButtonsHandler;
import com.vimukti.accounter.web.client.ui.core.InputDialog;
import com.vimukti.accounter.web.client.ui.core.ViewManager;
import com.vimukti.accounter.web.client.ui.grids.DialogGrid.GridRecordClickHandler;

/**
 * 
 * @author V.L.Pavani
 * 
 */
public class CreditRatingListDialog extends GroupDialog<ClientCreditRating> {

	private GroupDialogButtonsHandler dialogButtonsHandler;
	ClientCreditRating creditRating;
	List<ClientCreditRating> creditRatings;
	private InputDialog inputDlg;

	public CreditRatingListDialog(String title, String descript) {
		super(title, descript);
		// setSize("400", "330");
		// setWidth("400px");
		initialise();
	}

	private void initialise() {
		getGrid().setType(AccounterCoreType.CREDIT_RATING);
		getGrid().addRecordClickHandler(
				new GridRecordClickHandler<ClientCreditRating>() {

					@Override
					public boolean onRecordClick(
							ClientCreditRating clientCreditRating, int column) {
						if (clientCreditRating != null)
							enableEditRemoveButtons(true);
						else
							enableEditRemoveButtons(false);
						return false;
					}
				});
		dialogButtonsHandler = new GroupDialogButtonsHandler() {

			public void onCloseButtonClick() {

			}

			public void onFirstButtonClick() {
				showAddEditGroupDialog(null);
			}

			public void onSecondButtonClick() {
				showAddEditGroupDialog((ClientCreditRating) listGridView
						.getSelection());

			}

			public void onThirdButtonClick() {

				deleteObject(getSelectedCreditGroup());
				if (creditRatings == null)
					enableEditRemoveButtons(false);

			}

		};
		addGroupButtonsHandler(dialogButtonsHandler);
		this.okbtn.setVisible(false);
	}

	public void createCreditRatings() {
		ClientCreditRating creditRating = new ClientCreditRating();
		creditRating.setName(inputDlg.getTextItems().get(0).getValue()
				.toString());

		saveOrUpdate(creditRating);
	}

	public long getSelectedCreditGroupId() {

		return ((ClientCreditRating) listGridView.getSelection()).getID();
	}

	public ClientCreditRating getSelectedCreditGroup() {

		return (ClientCreditRating) listGridView.getSelection();
	}

	public void showAddEditGroupDialog(ClientCreditRating rec) {
		creditRating = rec;
		String creditRateString = messages.creditRating();
		inputDlg = new InputDialog(this, messages.creditRating(), "",
				creditRateString) {
		};

		if (creditRating != null) {
			inputDlg.setTextItemValue(0, creditRating.getName());
		}

		ViewManager.getInstance().showDialog(this, inputDlg);
	}

	protected void EditCreditRatings() {
		creditRating.setName(inputDlg.getTextValueByIndex(0));
		saveOrUpdate(creditRating);
	}

	@Override
	public Object getGridColumnValue(ClientCreditRating obj, int index) {
		ClientCreditRating creditRating = (ClientCreditRating) obj;
		if (creditRating != null) {
			switch (index) {
			case 0:
				return creditRating.getName();
			}
		}
		return null;
	}

	@Override
	public String[] setColumns() {
		return new String[] { messages.name() };
	}

	@Override
	protected List<ClientCreditRating> getRecords() {
		return getCompany().getCreditRatings();
	}

	@Override
	protected ValidationResult validate() {
		ValidationResult result = new ValidationResult();
		String name = inputDlg.getTextItems().get(0).getValue().toString();

		if (creditRating != null) {
			ClientItemGroup clientItemGroup = company.getItemGroupByName(name);
			if (clientItemGroup != null
					&& clientItemGroup.getID() != creditRating.getID()) {
				result.addError(this, messages.alreadyExist());
			}
		} else {
			ClientCreditRating creditRating = company
					.getCreditRatingByName(name);
			if (creditRating != null) {
				result.addError(this, messages.creditRatingAlreadyExists());
			}
		}
		return result;
	}

	@Override
	protected boolean onOK() {

		if (creditRating != null) {
			EditCreditRatings();
			creditRating = null;
		} else {
			createCreditRatings();
			creditRating = null;
		}

		return true;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	@Override
	public String getHeaderStyle(int index) {
		return "name";
	}

	@Override
	public String getRowElementsStyle(int index) {
		return "nameValue";
	}

}
