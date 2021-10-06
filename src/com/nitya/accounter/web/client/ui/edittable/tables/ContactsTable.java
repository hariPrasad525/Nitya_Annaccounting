/**
 * 
 */
package com.nitya.accounter.web.client.ui.edittable.tables;

import java.util.List;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.view.client.ProvidesKey;
import com.nitya.accounter.web.client.core.ClientContact;
import com.nitya.accounter.web.client.core.ValidationResult;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.UIUtils;
import com.nitya.accounter.web.client.ui.edittable.CheckboxEditColumn;
import com.nitya.accounter.web.client.ui.edittable.DeleteColumn;
import com.nitya.accounter.web.client.ui.edittable.EditTable;
import com.nitya.accounter.web.client.ui.edittable.RenderContext;
import com.nitya.accounter.web.client.ui.edittable.TextEditColumn;

/**
 * @author Prasanna Kumar G
 * 
 */
public abstract class ContactsTable extends EditTable<ClientContact> {

	public ContactsTable() {

	}

	/**
	 * The key provider that provides the unique ID of a contact.
	 */
	public static final ProvidesKey<ClientContact> KEY_PROVIDER = new ProvidesKey<ClientContact>() {
		public Object getKey(ClientContact item) {
			return item == null ? null : item.getID();
		}
	};

	protected void initColumns() {

		this.addColumn(new CheckboxEditColumn<ClientContact>() {

			@Override
			protected void onChangeValue(boolean value, ClientContact obj) {
				onSelectionChanged(obj, value);
			}

			@Override
			public IsWidget getHeader() {
				Label columnHeader = new Label(messages.primary());
				return columnHeader;
			}

			@Override
			public void render(IsWidget widget,
					RenderContext<ClientContact> context) {
				super.render(widget, context);
				((CheckBox) widget).setValue(context.getRow().isPrimary());
			}

			@Override
			public int getWidth() {
				return 46;
			}

			@Override
			public String getValueAsString(ClientContact row) {
				return "TODO";
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}
		});

		this.addColumn(new TextEditColumn<ClientContact>() {

			@Override
			protected void setValue(ClientContact row, String value) {
				row.setName(value);
			}

			@Override
			protected String getValue(ClientContact row) {
				return row.getName();
			}

			@Override
			public int getWidth() {
				return 140;
			}

			@Override
			protected String getColumnName() {
				return messages.contactName();
			}

			@Override
			public String getValueAsString(ClientContact row) {
				return messages.contactName()+" : "+getValue(row) ;
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}
		});

		this.addColumn(new TextEditColumn<ClientContact>() {

			@Override
			protected void setValue(ClientContact row, String value) {
				row.setTitle(value);
			}

			@Override
			protected String getValue(ClientContact row) {
				return row.getTitle();
			}

			@Override
			public int getWidth() {
				return 140;
			}

			@Override
			protected String getColumnName() {
				return messages.title();
			}

			@Override
			public String getValueAsString(ClientContact row) {
				return messages.title()+" : "+getValue(row);
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}
		});

		this.addColumn(new TextEditColumn<ClientContact>() {
			String value;

			@Override
			protected void setValue(ClientContact row, String value) {
				this.value = value;
				if (UIUtils.isValidPhone(value))
					row.setBusinessPhone(value);
				else {
					Accounter.showError(messages.invalidBusinessPhoneVal());
					row.setBusinessPhone("");
				}
			}

			@Override
			protected String getValue(ClientContact row) {
				return row.getBusinessPhone();
			}

			@Override
			public int getWidth() {
				return 100;
			}

			@Override
			protected String getColumnName() {
				return messages.businessPhone();
			}

			@Override
			public void render(IsWidget widget,
					RenderContext<ClientContact> context) {
				super.render(widget, context);
				final TextBox textBox = (TextBox) widget;
				textBox.addBlurHandler(new BlurHandler() {

					@Override
					public void onBlur(BlurEvent event) {
						if (value != null)
							if (UIUtils.isValidPhone(value)) {
								textBox.setValue(value);
								value = null;
							} else {
								textBox.setValue("");
							}
					}
				});
			}

			@Override
			public String getValueAsString(ClientContact row) {
				return messages.businessPhone()+" : "+getValue(row);
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}
		});

		this.addColumn(new TextEditColumn<ClientContact>() {
			String value;

			@Override
			protected void setValue(ClientContact row, String value) {
				this.value = value;
				if (UIUtils.isValidEmail(value))
					row.setEmail(value);
				else {
					Accounter.showError(messages.invalidEmail());
					row.setEmail("");
				}
			}

			@Override
			protected String getValue(ClientContact row) {
				return row.getEmail();
			}

			@Override
			public int getWidth() {
				return 140;
			}

			@Override
			protected String getColumnName() {
				return messages.email();
			}

			@Override
			public void render(IsWidget widget,
					RenderContext<ClientContact> context) {
				super.render(widget, context);
				final TextBox textBox = (TextBox) widget;
				textBox.addBlurHandler(new BlurHandler() {

					@Override
					public void onBlur(BlurEvent event) {
						if (value != null)
							if (UIUtils.isValidEmail(value)) {
								textBox.setValue(value);
								value = null;
							} else {
								textBox.setValue("");
							}
					}
				});
			}

			@Override
			public String getValueAsString(ClientContact row) {
				return messages.email()+" : "+getValue(row);
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}
		});

		this.addColumn(new DeleteColumn<ClientContact>());
	}

	private void onSelectionChanged(ClientContact obj, boolean isChecked) {

		List<ClientContact> records = getSelectedRecords(0);
		for (ClientContact contact : records) {
			int index = indexOf(contact);
			checkColumn(index, 0, false);
			contact.setPrimary(false);
		}

		int row = indexOf(obj);
		if (isChecked) {
			update(obj);
			obj.setPrimary(true);
		}
		super.checkColumn(row, 0, isChecked);
	}

	public int indexOf(ClientContact selectedObject) {
		return getAllRows().indexOf(selectedObject);
	}

	public List<ClientContact> getRecords() {
		return getAllRows();
	}

	public boolean isEmpty() {
		if (getAllRows().isEmpty())
			return true;
		for (int i = 0; i < getAllRows().size(); i++) {
			if (getAllRows().get(i).getTitle().isEmpty()
					&& getAllRows().get(i).getEmail().isEmpty()
					&& getAllRows().get(i).getDisplayName().isEmpty()
					&& getAllRows().get(i).getBusinessPhone().isEmpty()) {
				delete(getRecords().get(i));
				i = 0;
				continue;
			} else {
				if (getAllRows().isEmpty())
					return true;
				else
					continue;
			}
		}
		return false;
	}

	public void validate(ValidationResult result) {
		for (int i = 0; i < getAllRows().size(); i++) {
			for (int j = 0; j < getAllRows().size(); j++) {
				if (!getAllRows().get(i).isEmpty()) {
					if (i != j) {
						if (getAllRows().get(i).getTitle()
								.equals(getAllRows().get(j).getTitle())
								&& getAllRows().get(i).getEmail()
										.equals(getAllRows().get(j).getEmail())
								&& getAllRows()
										.get(i)
										.getDisplayName()
										.equals(getAllRows().get(j)
												.getDisplayName())
								&& getAllRows()
										.get(i)
										.getBusinessPhone()
										.equals(getAllRows().get(j)
												.getBusinessPhone()))
							result.addError(this,
									messages.youHaveEnteredduplicateContacts());
					}
				}
			}
		}
	}
}
