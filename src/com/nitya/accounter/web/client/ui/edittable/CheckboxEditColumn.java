package com.nitya.accounter.web.client.ui.edittable;

import java.util.List;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.IsWidget;

public abstract class CheckboxEditColumn<T> extends EditColumn<T> {

	@Override
	public int getWidth() {
		return 15;
	}

	@Override
	public IsWidget getHeader() {
		CheckBox box = new CheckBox();
		box.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				boolean value = event.getValue();
				List<T> allRows = getTable().getAllRows();
				for (int x = 1; x <= allRows.size(); x++) {
					IsWidget widget = getTable().getWidget(x, 0);
					if (widget instanceof CheckBox) {
						CheckBox checkedWidget = (CheckBox) widget;
						if (checkedWidget.getValue() != value) {
							checkedWidget.setValue(value);
							onChangeValue(value, allRows.get(x - 1));

						}
					}
				}
			}
		});
		box.setEnabled(isEnable() && !getTable().isDisabled());
		return box;
	}

	@Override
	public void render(IsWidget widget, RenderContext<T> context) {
		CheckBox box = (CheckBox) widget;
		box.setEnabled(isEnable() && !context.isDesable());
	}

	protected boolean isEnable() {
		return true;
	}

	@Override
	public IsWidget getWidget(final RenderContext<T> context) {
		CheckBox box = new CheckBox();
		box.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				boolean value = event.getValue();
				onChangeValue(value, context.getRow());
			}
		});
		return box;
	}

	protected abstract void onChangeValue(boolean value, T row);

}
