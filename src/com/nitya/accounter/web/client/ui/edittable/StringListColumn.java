package com.nitya.accounter.web.client.ui.edittable;

import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;

public abstract class StringListColumn<T> extends EditColumn<T> {

	@Override
	public int getWidth() {
		return 140;
	}

	@Override
	public void render(IsWidget widget, RenderContext<T> context) {
		StringComboBox<T> box = (StringComboBox<T>) widget;
		box.setDesable(context.isDesable());
		T row = context.getRow();
		String value = getValue(row);
		box.setValue(value);
	}

	protected abstract String getValue(T row);

	@Override
	public IsWidget getWidget(RenderContext<T> context) {
		StringComboBox<T> comboBox = new StringComboBox<T>();
		final StringListDropDownTable displayTable = new StringListDropDownTable(
				getData()) {

			@Override
			public List<String> getTotalRowsData() {
				return getData();
			}
		};
		comboBox.setDropDown(displayTable);
		comboBox.setComboChangeHandler(new ComboChangeHandler<T, String>() {

			@Override
			public void onChange(T row, String newValue) {
				setValue(row, newValue);
			}

			@Override
			public void onAddNew(final String text) {

			}

		});
		comboBox.setRow(context.getRow());
		return comboBox;
	}

	protected abstract List<String> getData();

	protected abstract void setValue(T row, String newValue);

}
