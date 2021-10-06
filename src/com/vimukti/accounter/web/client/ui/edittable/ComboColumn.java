package com.vimukti.accounter.web.client.ui.edittable;

import com.google.gwt.user.client.ui.IsWidget;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.core.ViewManager;

public abstract class ComboColumn<T, C extends IAccounterCore> extends
		EditColumn<T> {

	@Override
	public void render(IsWidget widget, RenderContext<T> context) {
		@SuppressWarnings("unchecked")
		ComboBox<T, C> box = (ComboBox<T, C>) widget;
		box.setDesable(!isEnable() || context.isDesable());
		T row = context.getRow();
		C value = getValue(row);
		box.setValue(value);
	}

	protected boolean isEnable() {
		return true;
	}

	protected abstract C getValue(T row);

	@Override
	public IsWidget getWidget(final RenderContext<T> context) {
		ComboBox<T, C> comboBox = new ComboBox<T, C>();
		final AbstractDropDownTable<C> displayTable = getDisplayTable(context
				.getRow());
		comboBox.setDropDown(displayTable);
		comboBox.setComboChangeHandler(new ComboChangeHandler<T, C>() {

			@Override
			public void onChange(T row, C newValue) {
				setValue(row, newValue);
			}

			@Override
			public void onAddNew(final String text) {
				NewItemDialog dialog = new NewItemDialog(messages.addNew(""),
						messages.Thenameyouentered(text)) {
					@Override
					protected boolean onOK() {
						displayTable.addNewItem(text);
						hide();
						return false;
					}
				};
				dialog.setModal(true);
				ViewManager.getInstance().showDialog(dialog);

			}

			// QuickAddDialog dialog = new QuickAddDialog("Add New Item") {
			// @Override
			// protected void onOkClick(String text) {
			// displayTable.addNewItem(text);
			// }
			//
			// };
			// dialog.setDefaultText(text);
			// dialog.setAddallinfoHide();
			// dialog.show();

		});
		comboBox.setRow(context.getRow());
		return comboBox;
	}

	protected abstract void setValue(T row, C newValue);

	public abstract AbstractDropDownTable<C> getDisplayTable(T row);

}
