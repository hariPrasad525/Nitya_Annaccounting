package com.nitya.accounter.web.client.ui.edittable;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientCustomField;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.IDeleteCallback;
import com.nitya.accounter.web.client.ui.Accounter.AccounterType;
import com.nitya.accounter.web.client.ui.core.ErrorDialogHandler;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class CustomFieldTable extends EditTable<ClientCustomField> {

	public CustomFieldTable() {
	}

	@Override
	protected void initColumns() {

		this.addColumn(new TextEditColumn<ClientCustomField>() {

			@Override
			protected void setValue(ClientCustomField row, String value) {
				row.setName(value);
			}

			@Override
			protected String getValue(ClientCustomField row) {
				return row.getName();
			}

			@Override
			public int getWidth() {
				return 160;
			}

			@Override
			protected String getColumnName() {
				return messages.name();
			}

			@Override
			public String getValueAsString(ClientCustomField row) {
				return messages.name() + " : " + getValue(row);
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}
		});
		this.addColumn(new CheckboxEditColumn<ClientCustomField>() {

			@Override
			protected void onChangeValue(boolean value, ClientCustomField obj) {
				onSelectionChanged(obj, value);
			}

			@Override
			public IsWidget getHeader() {
				Label columnHeader = new Label(Global.get().Customer());
				return columnHeader;
			}

			@Override
			public void render(IsWidget widget,
					RenderContext<ClientCustomField> context) {
				((CheckBox) widget).setValue(context.getRow().isShowCustomer());

			}

			@Override
			public String getValueAsString(ClientCustomField row) {
				return "TODO";
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}
		});
		this.addColumn(new CheckboxEditColumn<ClientCustomField>() {

			@Override
			protected void onChangeValue(boolean value, ClientCustomField obj) {
				onSelectionChanged1(obj, value);
			}

			@Override
			public IsWidget getHeader() {
				Label columnHeader = new Label(Global.get().Vendor());
				return columnHeader;
			}

			@Override
			public void render(IsWidget widget,
					RenderContext<ClientCustomField> context) {
				// super.render(widget, context);
				((CheckBox) widget).setValue(context.getRow().isShowVendor());
			}

			@Override
			public String getValueAsString(ClientCustomField row) {
				return "TODO";
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}
		});
		this.addColumn(new CheckboxEditColumn<ClientCustomField>() {

			@Override
			protected void onChangeValue(boolean value, ClientCustomField obj) {
				onSelectionChanged2(obj, value);
			}

			@Override
			public IsWidget getHeader() {
				Label columnHeader = new Label(messages.employee());
				return columnHeader;
			}

			@Override
			public void render(IsWidget widget,
					RenderContext<ClientCustomField> context) {
				// super.render(widget, context);
				((CheckBox) widget).setValue(context.getRow().isShowEmployee());
			}

			@Override
			public String getValueAsString(ClientCustomField row) {
				return "TODO";
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}
		});

		this.addColumn(new DeleteColumn<ClientCustomField>());

	}

	@Override
	public void delete(ClientCustomField row) {
		if (row.getName() == null || row.getName().isEmpty()) {
			deleteRow(row);
		} else {
			showWarningDialog(row);
		}
	}

	private void showWarningDialog(final ClientCustomField row) {

		Accounter.showWarning(messages.areYouwantToDeletecustomField(),

		AccounterType.WARNING, new ErrorDialogHandler() {

			@Override
			public boolean onCancelClick() {
				// NOTHING TO DO.
				return false;
			}

			@Override
			public boolean onNoClick() {
				return true;
			}

			@Override
			public boolean onYesClick() {
				deleteRow(row);
				return true;
			}

		});

	}

	private void deleteRow(ClientCustomField row) {
		deleteCustomField(row);
		super.delete(row);
	}

	protected void deleteCustomField(ClientCustomField row) {
		Accounter.deleteObject(new IDeleteCallback() {

			@Override
			public void deleteSuccess(IAccounterCore result) {
				System.out.println();
				Accounter.getCompany().deleteCustomField(result.getID());
			}

			@Override
			public void deleteFailed(AccounterException caught) {
				System.out.println();
			}
		}, row);
	}

	private void onSelectionChanged(ClientCustomField obj, boolean value) {

		obj.setShowCustomer(value);
		super.checkColumn(getAllRows().indexOf(obj), 1, value);

	}

	private void onSelectionChanged1(ClientCustomField obj, boolean value) {
		obj.setShowVendor(value);
		super.checkColumn(getAllRows().indexOf(obj), 2, value);
	}

	private void onSelectionChanged2(ClientCustomField obj, boolean value) {
		obj.setShowEmployee(value);
		super.checkColumn(getAllRows().indexOf(obj), 3, value);
	}

	@Override
	protected boolean isInViewMode() {
		// TODO Auto-generated method stub
		return false;
	}

}
