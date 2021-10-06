package com.vimukti.accounter.web.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientCustomField;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.customers.CustomerView;
import com.vimukti.accounter.web.client.ui.edittable.CustomFieldTable;
import com.vimukti.accounter.web.client.ui.payroll.NewEmployeeView;
import com.vimukti.accounter.web.client.ui.vendors.VendorView;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class CustomFieldDialog extends BaseDialog {

	public CustomFieldTable customFieldTable;
	public Button addNew;
	private AbstractBaseView parentView;

	private List<ClientCustomField> allRows = new ArrayList<ClientCustomField>();

	public CustomFieldDialog(AbstractBaseView view, String title,
			String description) {
		super(title, description);
		this.getElement().setId("CustomFieldDialog");
		this.parentView = view;

		createControls();
		// setPopupPosition(200, 200);
		initData();

	}

	private void initData() {
		customFieldTable.addRows(getCompany().getCustomFields());
	}

	public void createControls() {
		customFieldTable = new CustomFieldTable() {
		};

		StyledPanel layout = new StyledPanel("layout");
		customFieldTable.addStyleName("customfileddialog");
		Label customFieldTableTitle = new Label(Global.get().messages2()
				.table(messages.CustomField()));
		customFieldTableTitle.addStyleName("editTableTitle");
		layout.add(customFieldTableTitle);
		layout.add(customFieldTable);
		setBodyLayout(layout);
	}

	@Override
	protected void createButtons(StyledPanel footer) {
		addNew = new Button();
		addNew.setText(messages.add());
		addNew.getElement().setAttribute("data-icon", "add");
		addNew.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				customFieldTable.setEnabled(true);
				ClientCustomField customField = new ClientCustomField();
				customFieldTable.add(customField);

			}
		});
		addButton(footer, addNew);
		super.createButtons(footer);
		okbtn.setText(messages.save());
	}

	@SuppressWarnings("unchecked")
	@Override
	protected boolean onOK() {

		allRows = customFieldTable.getAllRows();
		ArrayList<ClientCustomField> arrayList = new ArrayList<ClientCustomField>(
				allRows);
		ArrayList<ClientCustomField> customFields = new ArrayList<ClientCustomField>(
				getCompany().getCustomFields());
		ArrayList<ClientCustomField> customFields2 = getCompany()
				.getCustomFields();
		for (ClientCustomField f : customFields) {
			boolean canDelete = true;
			for (ClientCustomField ff : allRows) {
				if (f.getName().equals(ff.getName())) {
					saveOrUpdate(ff);
					arrayList.remove(ff);
					canDelete = false;
					break;
				}
			}
			if (canDelete) {
				delete(f);
				customFields2.remove(f);
			}
		}

		for (ClientCustomField f : arrayList) {
			if (f.getName() != null && !f.getName().isEmpty()) {
				customFields2.add(f);
				saveOrUpdate(f);
			} else {
				delete(f);
				customFieldTable.delete(f);
			}
		}
		if (parentView instanceof CustomerView) {
			((CustomerView) parentView).createCustomFieldControls();
		} else if (parentView instanceof NewEmployeeView) {
			((NewEmployeeView) parentView).createCustomFieldControls();
		} else {
			((VendorView) parentView).createCustomFieldControls();
		}

		return true;

	}

	private void delete(ClientCustomField field) {
		Accounter.deleteObject(new IDeleteCallback() {

			@Override
			public void deleteSuccess(IAccounterCore result) {
				System.out.println();
				company.deleteCustomField(result.getID());
				if (parentView instanceof CustomerView) {
					((CustomerView) parentView).createCustomFieldControls();
				} else {
					((VendorView) parentView).createCustomFieldControls();
				}
			}

			@Override
			public void deleteFailed(AccounterException caught) {
				System.out.println();
			}
		}, field);
	}

	@Override
	public void setFocus() {

	}

	@Override
	protected boolean onCancel() {
		return true;
	}

	@Override
	protected ValidationResult validate() {
		return super.validate();
		// ValidationResult v = new ValidationResult();
		// List<ClientCustomField> allRows = customFieldTable.getAllRows();
		// for (ClientCustomField c : allRows) {
		//
		// }
		// return v;
	}

}
