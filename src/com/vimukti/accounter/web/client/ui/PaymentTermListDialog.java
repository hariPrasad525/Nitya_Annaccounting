package com.vimukti.accounter.web.client.ui;

import java.util.List;

import com.vimukti.accounter.web.client.core.ClientPaymentTerms;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.company.AddPaymentTermDialog;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ActionCallback;
import com.vimukti.accounter.web.client.ui.core.BaseListView;
import com.vimukti.accounter.web.client.ui.core.ViewManager;
import com.vimukti.accounter.web.client.ui.grids.BaseListGrid;

/**
 * 
 * @author Mandeep Singh
 * @param <T>
 * 
 */
public class PaymentTermListDialog extends BaseListView<ClientPaymentTerms> {

	private AddPaymentTermDialog dialog;

	public PaymentTermListDialog() {
		super();
		this.getElement().setId("PaymentTermListDialog");
	}

	protected ClientPaymentTerms getSelectedPaymentTerms() {
		return (ClientPaymentTerms) grid.getSelection();
	}

	public void showAddEditTermDialog(ClientPaymentTerms paymentTerm) {
		dialog = new AddPaymentTermDialog(this, messages.addPaymentTermTitle(),
				messages.addPaymentTermTitleDesc());
		if (paymentTerm != null) {
			dialog.payTermText.setValue(paymentTerm.getName());
			dialog.dateDriven.setValue(paymentTerm.isDateDriven());
			dialog.disableForms(paymentTerm.isDateDriven());
			if (!paymentTerm.isDateDriven()) {
				dialog.netDueIn.setNumber(UIUtils.toLong(paymentTerm
						.getDueDays()));
				dialog.discountField
						.setAmount(paymentTerm.getDiscountPercent());
				dialog.discountDue.setNumber(UIUtils.toLong(paymentTerm
						.getIfPaidWithIn()));
			} else {
				dialog.netDueBefore.setNumber(UIUtils.toLong(paymentTerm
						.getDueDays()));
				dialog.discountPerField.setAmount(paymentTerm
						.getDiscountPercent());
				dialog.discountPaidBefore.setNumber(UIUtils.toLong(paymentTerm
						.getIfPaidWithIn()));

			}

			// dialog.descText.setValue(rec.getDescription());
			// if (rec.getDue() != 0) {
			// dialog.dueSelect.setValue(dialog.dueValues[rec.getDue() - 1]);
			// }
			// dialog.dayText.setValue(String.valueOf(rec.getDueDays()));
			// dialog.discText.setValue(String.valueOf(rec.getDiscountPercent()));
			// dialog.discDayText.setValue(String.valueOf(rec.getIfPaidWithIn()));
		}

		dialog.setCallback(new ActionCallback<ClientPaymentTerms>() {
			@Override
			public void actionResult(ClientPaymentTerms result) {
				initListCallback();
			}
		});

		ViewManager.getInstance().showDialog(dialog);
	}

	@Override
	public void initListCallback() {
		grid.removeAllRecords();
		grid.addRecords(getRecords());
	}

	protected ClientPaymentTerms getPaymentTerms() {
		ClientPaymentTerms paymentTerm = (ClientPaymentTerms) grid
				.getSelection();
		if (paymentTerm != null) {
			paymentTerm
					.setName(dialog.payTermText.getValue() != null ? dialog.payTermText
							.getValue().toString() : "");
			paymentTerm.setDateDriven(dialog.dateDriven.getValue());
			if (dialog.fixedDays.getValue()) {
				paymentTerm.setDueDays(UIUtils.toInt((this.dialog.netDueIn
						.getNumber() != null ? this.dialog.netDueIn.getNumber()
						: 0)));
				paymentTerm.setDiscountPercent(this.dialog.discountField
						.getAmount() != null ? this.dialog.discountField
						.getAmount() : 0);
				paymentTerm
						.setIfPaidWithIn(UIUtils.toInt((this.dialog.discountDue
								.getNumber() != null ? this.dialog.discountDue
								.getNumber() : 0)));
			} else {
				paymentTerm.setDueDays(UIUtils.toInt((this.dialog.netDueBefore
						.getNumber() != null ? this.dialog.netDueBefore
						.getNumber() : 0)));
				paymentTerm.setDiscountPercent(this.dialog.discountPerField
						.getAmount() != null ? this.dialog.discountPerField
						.getAmount() : 0);
				paymentTerm
						.setIfPaidWithIn(UIUtils.toInt((this.dialog.discountPaidBefore
								.getNumber() != null ? this.dialog.discountPaidBefore
								.getNumber() : 0)));
			}
		} else {
			paymentTerm = new ClientPaymentTerms();
			paymentTerm
					.setName(dialog.payTermText.getValue() != null ? dialog.payTermText
							.getValue().toString() : "");

			paymentTerm.setDateDriven(dialog.dateDriven.getValue());
			if (dialog.fixedDays.getValue()) {
				paymentTerm.setDueDays(UIUtils.toInt((this.dialog.netDueIn
						.getNumber() != null ? this.dialog.netDueIn.getNumber()
						: 0)));
				paymentTerm.setDiscountPercent(this.dialog.discountField
						.getAmount() != null ? this.dialog.discountField
						.getAmount() : 0);
				paymentTerm
						.setIfPaidWithIn(UIUtils.toInt((this.dialog.discountDue
								.getNumber() != null ? this.dialog.discountDue
								.getNumber() : 0)));
			} else {
				paymentTerm.setDueDays(UIUtils.toInt((this.dialog.netDueBefore
						.getNumber() != null ? this.dialog.netDueBefore
						.getNumber() : 0)));
				paymentTerm.setDiscountPercent(this.dialog.discountPerField
						.getAmount() != null ? this.dialog.discountPerField
						.getAmount() : 0);
				paymentTerm
						.setIfPaidWithIn(UIUtils.toInt((this.dialog.discountPaidBefore
								.getNumber() != null ? this.dialog.discountPaidBefore
								.getNumber() : 0)));
			}

		}
		return paymentTerm;
	}

	/*
	 * If the payment term is a new one,check whether is there any otherone with
	 * same name. If editing already exited one,check wheder the given name
	 * changed or not,if changed,check whether new name existed in the list or
	 * not.
	 * 
	 * @return true -if the payment term is already exists
	 */
	private boolean validateName(String name) {
		ClientPaymentTerms paymentTermsByName = getCompany()
				.getPaymentTermsByName(name);
		ClientPaymentTerms paymentTerm = (ClientPaymentTerms) grid
				.getSelection();
		return (paymentTerm == null && paymentTermsByName == null)
				|| (paymentTerm != null && !(paymentTerm.getName()
						.equalsIgnoreCase(name) ? true
						: paymentTermsByName == null));
	}

	public Object getGridColumnValue(ClientPaymentTerms paymentTerms, int index) {
		if (paymentTerms != null) {
			switch (index) {
			case 0:
				return paymentTerms.getName();
			case 1:
				return paymentTerms.getDescription();
			case 2:
				return paymentTerms.getDueDays();
			case 3:
				return Accounter.getFinanceImages().delete();
			}
		}
		return null;
	}

	public String[] setColumns() {
		return new String[] { messages.name(), messages.description(),
				messages.dueDays(), messages.delete() };
	}

	public String[] getHeaderStyle() {
		return new String[] { "name", "description", "dueDays", "delete" };
	}

	public String[] getRowElementsStyle() {
		return new String[] { "nameValue", "descriptionValue", "dueDaysValue",
				"deleteValue" };
	}

	protected List<ClientPaymentTerms> getRecords() {
		return getCompany().getPaymentsTerms();
	}

	@Override
	public ValidationResult validate() {

		ValidationResult result = new ValidationResult();
		if (grid.getSelection() != null) {
			if (validateName(dialog.payTermText.getValue() != null ? dialog.payTermText
					.getValue().toString() : "")) {
				result.addError(this, messages.paytermsAlreadyExists());
			}
		} else {
			String value = dialog.payTermText.getValue();
			ClientPaymentTerms clientPaymentTerms = getCompany()
					.getPaymentTermsByName(value);
			if (clientPaymentTerms != null) {
				result.addError(this, messages.paytermsAlreadyExists());
			}
		}
		return result;
	}

	public boolean onOK() {
		ClientPaymentTerms paymentTerms = getPaymentTerms();
		if (paymentTerms != null) {
			saveOrUpdate(paymentTerms);
		} else {
			saveOrUpdate(paymentTerms);
		}
		return true;
	}

	@Override
	public void setFocus() {
	}

	@Override
	public void updateInGrid(ClientPaymentTerms objectTobeModified) {
	}

	@Override
	protected void initGrid() {
		grid = new BaseListGrid<ClientPaymentTerms>(false) {

			@Override
			protected int[] setColTypes() {
				return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_DATE,
						COLUMN_TYPE_TEXT, COLUMN_TYPE_IMAGE };
			}

			@Override
			protected String[] setHeaderStyle() {
				return PaymentTermListDialog.this.getHeaderStyle();
			}

			@Override
			protected String[] setRowElementsStyle() {
				return PaymentTermListDialog.this.getRowElementsStyle();
			}

			@Override
			protected void executeDelete(ClientPaymentTerms object) {
				deleteObject(object);
			}

			@Override
			protected Object getColumnValue(ClientPaymentTerms obj, int index) {
				return PaymentTermListDialog.this
						.getGridColumnValue(obj, index);
			}

			@Override
			public void onDoubleClick(ClientPaymentTerms obj) {
				showAddEditTermDialog(obj);
			}

			@Override
			protected void onClick(ClientPaymentTerms obj, int row, int col) {
				if (col == 3) {
					showWarnDialog(obj);
				}
			}

			@Override
			protected String[] getColumns() {
				return PaymentTermListDialog.this.setColumns();
			}

			@Override
			protected int getCellWidth(int index) {
				if (index == 0) {
					return 200;
				}
				if (index == 2) {
					return 80;
				} else if (index == 3) {
					return 40;
				}
				return -1;
			}
		};
		grid.init();
	}

	@Override
	protected String getListViewHeading() {
		return messages.managePaymentTerm();
	}

	@Override
	protected Action getAddNewAction() {
		showAddEditTermDialog(null);
		return null;
	}

	@Override
	protected String getAddNewLabelString() {
		return messages.addaNew(messages.paymentTerm());
	}

	@Override
	protected String getViewTitle() {
		return messages.paymentTermList();
	}

	@Override
	protected SelectCombo getSelectItem() {
		return null;
	}
}
