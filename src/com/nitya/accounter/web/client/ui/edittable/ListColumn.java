package com.nitya.accounter.web.client.ui.edittable;

import com.google.gwt.user.client.ui.IsWidget;
import com.nitya.accounter.web.client.core.IAccounterCore;

public abstract class ListColumn<T, C extends IAccounterCore> extends
		EditColumn<T> {

	private ListBox<T, C> listBox;

	@Override
	public int getWidth() {
		return 100;
	}

	@Override
	public void render(IsWidget widget, RenderContext<T> context) {
		@SuppressWarnings("unchecked")
		ListBox<T, C> box = (ListBox<T, C>) widget;
		box.setDesable(!isEnable() || context.isDesable());
		T row = context.getRow();
		C value = getValue(row);
		box.setValue(value);
	}

	protected abstract C getValue(T row);

	private boolean isEnable() {
		return true;
	}

	@Override
	public IsWidget getWidget(final RenderContext<T> context) {
		listBox = new ListBox<T, C>() {

			@Override
			protected void updateList(T row) {
				ListColumn.this.updateList(row);
			}

		};
		final AbstractDropDownTable<C> displayTable = getDisplayTable(context
				.getRow());
		listBox.setDropDown(displayTable);
		listBox.setComboChangeHandler(new ComboChangeHandler<T, C>() {

			@Override
			public void onChange(T row, C newValue) {
				setValue(row, newValue);
			}

			@Override
			public void onAddNew(String text) {
				setNewValue(context.getRow(), text);
			}

		});
		listBox.setRow(context.getRow());
		return listBox;
	}

	public void editComplete(T row) {
		listBox.editComplete();
	}

	protected abstract void updateList(T row);

	protected abstract void setNewValue(T row, String text);

	protected abstract void setValue(T row, C newValue);

	public abstract AbstractDropDownTable<C> getDisplayTable(T row);

}
