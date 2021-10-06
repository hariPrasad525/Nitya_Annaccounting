package com.vimukti.accounter.web.client.ui.edittable;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.IsWidget;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.ui.widgets.DatePicker;
import com.vimukti.accounter.web.client.ui.widgets.DateValueChangeHandler;

public abstract class DateColumn<T> extends EditColumn<T> {

	/**
	 * To hold the date format of DatePicker
	 */
	private final DateTimeFormat format;

	public DateColumn() {
		this(Global.get().preferences().getDateFormat());
	}

	public DateColumn(String dateFormat) {
		format = DateTimeFormat.getFormat(dateFormat);
	}

	@Override
	public int getWidth() {
		return -1;
	}

	@Override
	public void render(IsWidget widget, RenderContext<T> context) {
		DatePicker box = (DatePicker) widget;
		ClientFinanceDate value = getValue(context.getRow());
		box.setEnabled(isEnable() && !context.isDesable());
		if (value == null || value.getDate() == 0) {
			box.setSelectedDate(new ClientFinanceDate().getDateAsObject());
		} else {
			box.setSelectedDate(value.getDateAsObject());
		}
	}

	protected boolean isEnable() {
		return true;
	}

	@Override
	public IsWidget getWidget(final RenderContext<T> context) {
		final DatePicker datePicker = new DatePicker();
		configure(datePicker);
		datePicker.addDateValueChangeHandler(new DateValueChangeHandler() {

			@Override
			public void onDateValueChange(ClientFinanceDate date) {
				ClientFinanceDate prevValue = getValue(context.getRow());
				if (prevValue == null) {
					prevValue = new ClientFinanceDate();
				}
				ClientFinanceDate newValue = new ClientFinanceDate(datePicker
						.getSelectedDate());
				if (!prevValue.equals(newValue)) {
					setValue(context.getRow(), newValue);
				}
			}
		});
		// datePicker.set(new DateBox.DefaultFormat(format));
		// datePicker.setValue(new ClientFinanceDate().getDateAsObject());
		return datePicker;
	}

	protected void configure(DatePicker datePicker) {
		datePicker.addStyleName("textEdit");
	}

	protected abstract ClientFinanceDate getValue(T row);

	protected abstract void setValue(T row, ClientFinanceDate value);

}
