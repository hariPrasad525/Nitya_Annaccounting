package com.vimukti.accounter.web.client.ui.fixedassets;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientFixedAsset;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.widgets.DateUtills;
import com.vimukti.accounter.web.client.ui.widgets.DateValueChangeHandler;

/**
 * @author B.Srinivasa Rao
 */
public class DisposingRegisteredItemView extends SellingRegisteredItemView {
	private DateField dateDisposed;
	private ArrayList<DynamicForm> listforms;

	public DisposingRegisteredItemView() {
	}

	@Override
	public void init() {
		super.init();
	}

	/**
	 * This overwritten method is using for getting DynamicForm of Disposing
	 * View
	 * 
	 * @return
	 */
	@Override
	protected DynamicForm getDetailForm() {
		dateDisposed = new DateField(messages.dateDisposed(),"dateDisposed");
		dateDisposed.setEnteredDate(new ClientFinanceDate());
		yearValue = String.valueOf(dateDisposed.getYear() + 1900);
		dateDisposed.addDateValueChangeHandler(new DateValueChangeHandler() {

			@Override
			public void onDateValueChange(ClientFinanceDate date) {
				dateSelected();

			}
		});
		DynamicForm detailForm = new DynamicForm("detailForm");
		detailForm.add(dateDisposed);
		return detailForm;
	}

	protected void createDisposingObject() {
		ClientFixedAsset disposingAsset = getSellOrDisposeObject();
		saveOrUpdate(disposingAsset);
	}

	@Override
	protected void okClicked() {
		createDisposingObject();
	}

	@Override
	public void saveAndUpdateView() {
		getJournalViewObjet(getPreparedTempAssetObject());
	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = new ValidationResult();
		if (QuestionItem.getValue().equals(allDepOption)) {
			if (!AccounterValidator.isNullValue(dateItemCombo.getValue())) {
				result.addError(dateItemCombo, messages.pleaseSelectDate());
			}
		}
		ClientFixedAsset asset = data;
		if (asset != null)
			if (AccounterValidator.isValidSellorDisposeDate(
					new ClientFinanceDate(asset.getPurchaseDate()),
					getSoldorDisposedDateField().getEnteredDate())) {
				result.addError(
						dateItemCombo,
						messages.datesold()
								+ " "
								+ messages.conditionalMsg(messages
										.purchaseDate())
								+ "  ("
								+ DateUtills
										.getDateAsString(new ClientFinanceDate(
												asset.getPurchaseDate()))
								+ "  )");
			}
		if (asset != null)
			if (AccounterValidator.isValidSellorDisposeDate(
					new ClientFinanceDate(getPreferences()
							.getStartOfFiscalYear()),
					getSoldorDisposedDateField().getEnteredDate())) {
				result.addError(
						dateItemCombo,
						messages.datesold()
								+ " "
								+ messages.conditionalMsg(messages.fiscalYear())
								+ "  ("
								+ DateUtills
										.getDateAsString(new ClientFinanceDate(
												getPreferences()
														.getStartOfFiscalYear()))
								+ "  )");
			}
		return result;
	}

	@Override
	protected DateField getSoldorDisposedDateField() {
		return dateDisposed;
	}

	@Override
	public List<DynamicForm> getForms() {

		return listforms;
	}

	/**
	 * call this method to set focus in View
	 */
	@Override
	public void setFocus() {
		this.notesArea.setFocus();
	}

	@Override
	public void deleteFailed(AccounterException caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {

	}

	@Override
	public void fitToSize(int height, int width) {
		super.fitToSize(height, width);
	}

}
