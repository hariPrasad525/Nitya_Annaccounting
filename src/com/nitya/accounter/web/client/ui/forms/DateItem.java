package com.nitya.accounter.web.client.ui.forms;

import java.util.Date;

import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.ui.StyledPanel;
import com.nitya.accounter.web.client.ui.widgets.DatePicker;
import com.nitya.accounter.web.client.ui.widgets.DateValueChangeHandler;

public class DateItem extends FormItem<ClientFinanceDate> {

	StyledPanel datePanel = new StyledPanel("dateItem_datePanel");

	// TextBox textBox;
	boolean enableTextFieldView;
	private ChangeHandler changeHandler;
	private DateValueChangeHandler handler;
	private final DatePicker datePicker = new DatePicker();

	// FinanceImages images = GWT.create(FinanceImages.class);

	public void setUseTextField(boolean useTextField) {
		this.enableTextFieldView = useTextField;

	}

	public DateItem(String text, String styleName) {
		super(text, styleName);
		setUseTextField(true);
		Label dateImg = new Label();
		dateImg.setStyleName("calendar-picker");
		dateImg.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				datePicker.fireEvent(event);
				if (!datePicker.isEnabled())
					return;
				datePicker.showPopup();
			}
		});
		this.addStyleName("dateItem");
		datePanel.add(datePicker);
		datePanel.add(dateImg);
		this.add(datePanel);

	}

	@Override
	public void setToolTip(String toolTip) {
		super.setToolTip(toolTip);
		datePicker.setTitle(toolTip);
	}

	@Override
	public void setValue(ClientFinanceDate value) {
		if (value != null && !value.equals("")) {
			super.setValue(value);
			setValue(value.getDateAsObject());
		}

	}

	public void setValue(Date date) {
		if (date != null && !date.equals("")) {
			this.datePicker.setSelectedDate(date);
		}
	}

	public void setEnteredDate(Date value) {
		if (value != null && !value.equals("")) {
			setValue(value);
			this.datePicker.setSelectedDate(value);
		}

	}

	public void setEnteredDate(ClientFinanceDate value) {
		if (value != null && !value.equals("")) {
			setValue(value.getDateAsObject());
			this.datePicker.setSelectedDate(value.getDateAsObject());
		}

	}

	public long getTime() {
		return new ClientFinanceDate(this.datePicker.getSelectedDate())
				.getDate();
	}

	public void setDatethanFireEvent(ClientFinanceDate date) {
		setEnteredDate(date);
		if (handler != null)
			handler.onDateValueChange(date);
	}

	@Override
	public ClientFinanceDate getValue() {
		return new ClientFinanceDate(datePicker.getSelectedDate());
	}

	public ClientFinanceDate getEnteredDate() {
		return new ClientFinanceDate(this.datePicker.getSelectedDate());
	}

	@Override
	public String getDisplayValue() {
		if (enableTextFieldView) {
			return datePicker.getValue().toString();
		} else
			return datePicker.getValue().toString();
	}

	public ClientFinanceDate getDate() {
		return new ClientFinanceDate(this.datePicker.getSelectedDate());
	}

	public void addValueChangeHandler(ValueChangeHandler<String> changedHandler) {
		this.datePicker.addValueChangeHandler(changedHandler);

	}

	public void focusInItem() {

	}

	@Override
	public Widget getMainWidget() {
		return datePanel;
	}

	public void setEndDate(Date ed) {
		datePicker.setOldestDate(ed);
	}

	public void setStartDate(Date value) {
		datePicker.setYoungestDate(value);
	}

	public ChangeHandler getChangeHandler() {
		return this.changeHandler;
	}

	public TextBox getDateBox() {
		return datePicker;
	}

	@Override
	public void setEnabled(boolean b) {
		this.datePicker.setEnabled(b);
		if (!b) {
			this.addStyleName("disabled");
			this.datePicker.addStyleName("disable-TextField");
		} else {
			this.removeStyleName("disabled");
			this.datePicker.removeStyleName("disable-TextField");
			this.datePicker.addStyleName("gwt-TextBox");
		}

	}

	// public long getTime() {
	// try {
	// return new Date(this.datePicker.getText()).getTime();
	// } catch (Exception e) {
	// return 0;
	// }
	// }

	public void addStyleName(String style) {
		this.datePicker.addStyleName(style);

	}

	public void removeStyleName(String style) {
		this.datePicker.removeStyleName(style);

	}

	public void addDateValueChangeHandler(DateValueChangeHandler changeHandler) {
		this.datePicker.addDateValueChangeHandler(changeHandler);
	}

	public int getYear() {
		ClientFinanceDate date = getDate();
		return date.getYear();
	}

	public int getMonth() {
		ClientFinanceDate date = getDate();
		return date.getMonth();
	}

	public int getDay() {
		ClientFinanceDate date = getDate();
		return date.getDay();
	}

	public boolean isLeapYear(int year) {
		return (year % 400 == 0) || ((year % 4 == 0) && (year % 100 != 0));
	}

	public void addClickHandler(ClickHandler handler) {
		datePicker.addClickHandler(handler);
	}

	public void addEmpty_Date_FieldStyle() {
		datePicker.addStyleName("empty_date_field");
	}

	public void removeEmpty_Date_FieldStyle() {
		datePicker.removeStyleName("empty_date_field");
	}

	public void setTabIndex(int index) {
		datePicker.setTabIndex(index);
	}

	public void setDateWithNoEvent(ClientFinanceDate date) {
		datePicker.setDate(date.getDateAsObject());
	}
}
