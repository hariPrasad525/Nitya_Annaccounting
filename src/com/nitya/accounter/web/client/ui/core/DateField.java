package com.nitya.accounter.web.client.ui.core;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.externalization.AccounterMessages;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.forms.DateItem;

public class DateField extends DateItem {
	private ClientFinanceDate startDate;

	private ClientFinanceDate endDate;
	protected static AccounterMessages messages = Global.get().messages();

	// private Date enteredDate;

	// private Date defaultDate;

	public DateField(final String name, String styleName) {
		super(name, styleName);
		addBlurHandler(getBlurHandler());
		addValueChangeHandler(getValueChangedHandler());
	}

	private ValueChangeHandler<String> getValueChangedHandler() {
		ValueChangeHandler<String> vHandler = new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				// String date = event.getValue().toString();
				// setEnteredDate(new Date(date));
			}
		};
		return vHandler;
	}

	public BlurHandler getBlurHandler() {

		BlurHandler blurHandler = new BlurHandler() {

			public void onBlur(BlurEvent event) {
				try {
					// enteredDate = getDate();

					if (startDate != null && getDate().compareTo(startDate) < 0)
						throw new Exception(messages.cantearlierThanStart());

					if (endDate != null && getDate().compareTo(endDate) > 0)
						throw new Exception(messages.cantbeAfterEnd());

					setEnteredDate(getDate());

				} catch (Exception e) {
					// if (enteredDate == null)
					Accounter.showError(messages.incorrectInformation());

				}
			}
		};
		return blurHandler;
	}

	public void setStartDate(ClientFinanceDate startDate) {
		this.startDate = startDate;
		super.setStartDate(this.startDate.getDateAsObject());
	}

	// public void setEnteredDate(Date enteredDate) {
	// if (enteredDate != null) {
	// this.enteredDate = enteredDate;
	// setValue(enteredDate);
	// }
	// }
	//
	// public Date getEnteredDate() {
	// return enteredDate;
	// }

	public ClientFinanceDate getEndDate() {
		return endDate;
	}

	public void setEndDate(ClientFinanceDate endDate) {
		this.endDate = endDate;
		super.setEndDate(this.endDate.getDateAsObject());
	}

	public ClientFinanceDate getStartDate() {
		return startDate;
	}

	// public void setDefaultDate(Date defaultDate) {
	// this.defaultDate = defaultDate;
	// setValue(defaultDate);
	// }
	//
	// public Date getDefaultDate() {
	// return defaultDate;
	// }

}
