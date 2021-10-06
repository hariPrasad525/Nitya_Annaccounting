package com.nitya.accounter.web.client.ui.widgets;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.TextBox;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.ui.Accounter;

/**
 * Main class of the DatePicker. It extends the TextBox widget and manages a
 * Date object. When it is clicked, it opens a PopupCalendar on which we can
 * select a new date. <br>
 * Example of use : <br>
 * <code>
 * DatePicker datePicker = new DatePicker();<br>
 * RootPanel.get().add(datePicker);<br>
 * </code> You can specify a theme (see the CSS file DatePickerStyle.css) and
 * the date to initialize the date picker. Enjoy xD
 * 
 * @author Zenika
 * @author Lo�c Bertholet - Zenika
 * @author James Heggs (jheggs@axonbirch.com)
 * 
 */
public class DatePicker extends TextBox implements ClickHandler, ChangeHandler,
		KeyUpHandler, FocusHandler, MouseWheelHandler {

	private PopupCalendar popup;
	private Date selectedDate;
	// the oldest date that can be selected
	private Date oldestDate;
	// the youngest date that can be selected
	private Date youngestDate;
	private DateTimeFormat dateFormatter;

	private List<ChangeHandler> changeHandlers;
	private DateValueChangeHandler handler;

	{

		/**
		 * Set the date format according to the company preferencess
		 */
		setDateFormatter();

		popup = new PopupCalendar(this);

		changeHandlers = new ArrayList<ChangeHandler>();
	}

	/**
	 * Default constructor. It creates a DatePicker which shows the current
	 * month.
	 */
	public DatePicker() {
		super();

		setText(DateUtills.getDateAsString(System.currentTimeMillis()));
		this.addStyleName("date-field-textbox");

		sinkEvents(Event.ONCLICK);
		sinkEvents(Event.ONBLUR);
		// addClickHandler(this);
		addChangeHandler(this);
		// addKeyPressHandler(this);
		addFocusHandler(this);
		addMouseWheelHandler(this);
		addKeyUpHandler(this);

		// addBlurHandler(this);
	}

	private void setDateFormatter() {
		dateFormatter = DateTimeFormat.getFormat(Accounter.getCompany()
				.getPreferences().getDateFormat());
	}

	/**
	 * Create a DatePicker which show a specific Date.
	 * 
	 * @param selectedDate
	 *            Date to show
	 */
	public DatePicker(Date selectedDate) {
		this();
		this.selectedDate = selectedDate;
		synchronizeFromDate();
	}

	/**
	 * Create a DatePicker which uses a specific theme.
	 * 
	 * @param theme
	 *            Theme name
	 */
	public DatePicker(String theme) {
		this();
		setTheme(theme);
	}

	/**
	 * Create a DatePicker which specifics date and theme.
	 * 
	 * @param selectedDate
	 *            Date to show
	 * @param theme
	 *            Theme name
	 */
	public DatePicker(Date selectedDate, String theme) {
		this(selectedDate);
		setTheme(theme);
	}

	/**
	 * Return the Date contained in the DatePicker.
	 * 
	 * @return The Date
	 */
	public Date getSelectedDate() {
		return this.selectedDate;
	}

	/**
	 * Set the Date of the datePicker and synchronize it with the display.
	 * 
	 * @param value
	 */
	public void setSelectedDate(Date value) {
		this.selectedDate = value;

		synchronizeFromDate();

		// fireChange();
	}

	/**
	 * Return the theme name.
	 * 
	 * @return Theme name
	 */
	public String getTheme() {
		return popup.getTheme();
	}

	/**
	 * Set the theme name.
	 * 
	 * @param theme
	 *            Theme name
	 */
	public void setTheme(String theme) {
		popup.setTheme(theme);
	}

	/**
	 * @see com.google.gwt.user.client.ui.TextBoxBase#onBrowserEvent(com.google.gwt.user.client.Event)
	 */
	public void onBrowserEvent(Event event) {
		switch (DOM.eventGetType(event)) {
		case Event.ONBLUR:
			parseDate();
			popup.hidePopupCalendar();
			break;
		// case Event.ONCLICK:
		// this.setText("");
		default:
			break;
		}
		super.onBrowserEvent(event);
	}

	/**
	 * @see ClickHandler#onClick(ClickEvent)
	 */
	public void onClick(ClickEvent event) {
		showPopup();
	}

	/**
	 * @see ChangeHandler#onChange(ChangeEvent)
	 */
	public void onChange(ChangeEvent event) {
		parseDate();
	}

	/**
	 * @see KeyPressHandler#onKeyPress(KeyPressEvent)
	 */
	public void onKeyPress(KeyPressEvent event) {
		int keyCode = event.getNativeEvent().getKeyCode();

		switch (keyCode) {
		case KeyCodes.KEY_ENTER:
			parseDate();
			showPopup();
			break;
		case KeyCodes.KEY_ESCAPE:
			popup.hide();
			break;
		case KeyCodes.KEY_UP:
			processIncrementDate(this.getCursorPos());
			break;
		case KeyCodes.KEY_DOWN:
			processDecrementDate(this.getCursorPos());
			break;
		default:
			break;
		}

	}

	@SuppressWarnings("deprecation")
	private void processDecrementDate(int cursorPos) {

		String dateFormat = Accounter.getCompany().getPreferences()
				.getDateFormat();

		if (dateFormat.equals("dd/MMM/yyyy")) {
			if (cursorPos == 0 || cursorPos == 1) {
				selectedDate.setDate(selectedDate.getDate() - 1);

			} else if (cursorPos == 3 || cursorPos == 4 || cursorPos == 5) {
				selectedDate.setMonth(selectedDate.getMonth() - 1);

			} else if (cursorPos >= 7) {
				selectedDate.setYear(selectedDate.getYear() - 1);
			}
		} else if ((dateFormat.equals("dd/MM/yyyy"))
				|| (dateFormat.equals("dd-MM-yyyy"))) {
			if (cursorPos == 0 || cursorPos == 1) {
				selectedDate.setDate(selectedDate.getDate() - 1);

			} else if (cursorPos == 3 || cursorPos == 4) {
				selectedDate.setMonth(selectedDate.getMonth() - 1);

			} else if (cursorPos >= 6) {
				selectedDate.setYear(selectedDate.getYear() - 1);
			}
		} else if ((dateFormat.equals("MM/dd/yyyy"))
				|| ((dateFormat.equals("MM-dd-yyyy")))) {
			if (cursorPos == 0 || cursorPos == 1) {
				selectedDate.setMonth(selectedDate.getMonth() - 1);

			} else if (cursorPos == 3 || cursorPos == 4) {

				selectedDate.setDate(selectedDate.getDate() - 1);

			} else if (cursorPos >= 6) {
				selectedDate.setYear(selectedDate.getYear() - 1);
			}
		} else if (dateFormat.equals("MMM/dd/yyyy")) {
			if (cursorPos <= 2) {
				selectedDate.setMonth(selectedDate.getMonth() - 1);

			} else if (cursorPos == 4 || cursorPos == 5) {

				selectedDate.setDate(selectedDate.getDate() - 1);

			} else if (cursorPos >= 7) {
				selectedDate.setYear(selectedDate.getYear() - 1);
			}
		} else if ((dateFormat.equals("dd/MMMM/yyyy"))
				|| (dateFormat.equals("dd-MMMM-yyyy"))) {
			if (cursorPos <= 2) {
				selectedDate.setDate(selectedDate.getDate() - 1);
			} else if (cursorPos > 2 && cursorPos < 10) {
				selectedDate.setMonth(selectedDate.getMonth() - 1);
			} else if (cursorPos >= 10) {
				selectedDate.setYear(selectedDate.getYear() - 1);
			}
		} else if ((dateFormat.equals("ddMMyyyy"))
				|| (dateFormat.equals("MMddyyyy"))) {
			if (cursorPos < 2) {
				selectedDate.setDate(selectedDate.getDate() - 1);
			} else if (cursorPos > 2 && cursorPos < 4) {
				selectedDate.setMonth(selectedDate.getMonth() - 1);
			} else if (cursorPos >= 4) {
				selectedDate.setYear(selectedDate.getYear() - 1);
			}
		} else if (dateFormat.equals("MMddyyyy")) {
			if (cursorPos < 2) {
				selectedDate.setMonth(selectedDate.getMonth() - 1);
			} else if (cursorPos > 2 && cursorPos < 4) {
				selectedDate.setDate(selectedDate.getDate() - 1);
			} else if (cursorPos >= 4) {
				selectedDate.setYear(selectedDate.getYear() - 1);
			}

		} else if (dateFormat.equals("MMMMddyyyy")) {
			if (cursorPos < 7) {
				selectedDate.setMonth(selectedDate.getMonth() - 1);
			} else if (cursorPos >= 7 && cursorPos < 10) {
				selectedDate.setDate(selectedDate.getDate() - 1);
			} else if (cursorPos >= 11) {
				selectedDate.setYear(selectedDate.getYear() - 1);
			}

		}

		synchronizeFromDate();
		this.setCursorPos(cursorPos);
		showPopup();
	}

	@SuppressWarnings("deprecation")
	private void processIncrementDate(int cursorPos) {

		String dateFormat = Accounter.getCompany().getPreferences()
				.getDateFormat();

		if ((dateFormat.equals("dd/MMM/yyyy"))
				|| (dateFormat.equals("dd-MMM-yyyy"))) {
			if (cursorPos == 0 || cursorPos == 1) {
				selectedDate.setDate(selectedDate.getDate() + 1);

			} else if (cursorPos == 3 || cursorPos == 4 || cursorPos == 5) {
				selectedDate.setMonth(selectedDate.getMonth() + 1);

			} else if (cursorPos >= 7) {
				selectedDate.setYear(selectedDate.getYear() + 1);
			}
		} else if ((dateFormat.equals("dd/MM/yyyy"))
				|| (dateFormat.equals("dd-MM-yyyy"))) {
			if (cursorPos == 0 || cursorPos == 1) {
				selectedDate.setDate(selectedDate.getDate() + 1);

			} else if (cursorPos == 3 || cursorPos == 4) {
				selectedDate.setMonth(selectedDate.getMonth() + 1);

			} else if (cursorPos >= 6) {
				selectedDate.setYear(selectedDate.getYear() + 1);
			}
		} else if ((dateFormat.equals("MM/dd/yyyy"))
				|| (dateFormat.equals("MM-dd-yyyy"))) {
			if (cursorPos == 0 || cursorPos == 1) {
				selectedDate.setMonth(selectedDate.getMonth() + 1);

			} else if (cursorPos == 3 || cursorPos == 4) {

				selectedDate.setDate(selectedDate.getDate() + 1);

			} else if (cursorPos >= 6) {
				selectedDate.setYear(selectedDate.getYear() + 1);
			}
		} else if (dateFormat.equals("MMM/dd/yyyy")) {
			if (cursorPos <= 2) {
				selectedDate.setMonth(selectedDate.getMonth() + 1);

			} else if (cursorPos == 4 || cursorPos == 5) {

				selectedDate.setDate(selectedDate.getDate() + 1);

			} else if (cursorPos >= 7) {
				selectedDate.setYear(selectedDate.getYear() + 1);
			}
		} else if ((dateFormat.equals("dd/MMMM/yyyy"))
				|| (dateFormat.equals("dd-MMMM-yyyy"))) {
			if (cursorPos <= 2) {
				selectedDate.setDate(selectedDate.getDate() + 1);
			} else if (cursorPos > 2 && cursorPos < 10) {
				selectedDate.setMonth(selectedDate.getMonth() + 1);
			} else if (cursorPos >= 10) {
				selectedDate.setYear(selectedDate.getYear() + 1);
			}

		} else if (dateFormat.equals("ddMMyyyy")) {
			if (cursorPos <= 2) {
				selectedDate.setDate(selectedDate.getDate() + 1);
			} else if (cursorPos > 2 && cursorPos < 5) {
				selectedDate.setMonth(selectedDate.getMonth() + 1);
			} else if (cursorPos > 5) {
				selectedDate.setYear(selectedDate.getYear() + 1);
			}
		} else if (dateFormat.equals("MMddyyyy")) {
			if (cursorPos < 2) {
				selectedDate.setMonth(selectedDate.getMonth() + 1);
			} else if (cursorPos > 2 && cursorPos < 4) {

				selectedDate.setDate(selectedDate.getDate() + 1);
			} else if (cursorPos >= 4) {
				selectedDate.setYear(selectedDate.getYear() + 1);
			}

		} else if (dateFormat.equals("MMMMddyyyy")) {
			if (cursorPos < 7) {
				selectedDate.setMonth(selectedDate.getMonth() + 1);
			} else if (cursorPos >= 7 && cursorPos < 10) {
				selectedDate.setDate(selectedDate.getDate() + 1);
			} else if (cursorPos >= 11) {
				selectedDate.setYear(selectedDate.getYear() + 1);
			}
		}

		synchronizeFromDate();
		this.setCursorPos(cursorPos);
		showPopup();
	}

	/**
	 * Display the date in the DatePicker.
	 */
	public void synchronizeFromDate() {
		if (this.selectedDate != null) {

			this.setText(dateFormatter.format(this.selectedDate));

			if (handler != null)
				handler.onDateValueChange(new ClientFinanceDate(
						this.selectedDate));
		} else {

		}
	}

	/**
	 * Display the PopupCalendar.
	 */
	public void showPopup() {
		if (this.selectedDate != null) {
			popup.setDisplayedMonth(this.selectedDate);
		}
		int x = this.getAbsoluteLeft();
		int y = this.getAbsoluteTop() + 22;
		// this.setWidth(this.getOffsetWidth() + "px");
		popup.setPopupPosition(x + 1, y);
		popup.show();
		int clientwidth = Window.getClientWidth();
		int popupWdth = popup.getWidget().getOffsetWidth();
		if ((x + popupWdth) > clientwidth) {
			x = x - (popup.getOffsetWidth() - this.getOffsetWidth());
			popup.setPopupPosition(x + 1, y);
		}
		// popup.setHeight(Math.min(this.getOffsetHeight(), 200) + "px");
		popup.displayMonth();
		doAfterShowPopup(popup);
	}

	/**
	 * Call when the calendar pop-up is shown. Can be overwritten.
	 * 
	 * @param popup
	 */
	protected void doAfterShowPopup(PopupCalendar popup) {
		// nothing special to do

		/*
		 * popup position can be updated so that it is displayed "above" the
		 * TextBox
		 */
		// popup.setPopupPosition(popup.getPopupLeft(), popup.getPopupTop() -
		// 150);

		/*
		 * there is also a known bug if the DatePicker is in a modal MyGWT
		 * Dialog - it may be deprecated
		 */
		// DOM.setIntStyleAttribute(popup.getElement(), "zIndex",
		// MyDOM.getZIndex());
		// Element p = DOM.getParent(popup.getElement());
		// int index = DOM.getChildIndex(p, popup.getElement());
		// p = DOM.getChild(p, --index);
		// DOM.setIntStyleAttribute(p, "zIndex", MyDOM.getZIndex() - 2);
	}

	/**
	 * Parse the date entered in the DatePicker.
	 */
	private void parseDate() {
		if (getText() == null || getText().length() == 0) {
			// selectedDate = popup.getDisplayedMonth();
			this.selectedDate = null;
			return;
		} else {
			try {
				// Date parsedDate = dateFormatter.parse(getText());
				Date parsedDate = DateUtills.parseDate(getText());

				if (canBeSelected(parsedDate))
					selectedDate = parsedDate;

			} catch (Exception e) {
				// Do something ?
			}

		}
		synchronizeFromDate();
	}

	/**
	 * Return true if the selectedDay is between datepicker's interval dates.
	 * 
	 * @param selectedDay
	 * @return boolean
	 */
	public boolean canBeSelected(Date selectedDay) {

		if (this.getOldestDate() != null
				&& selectedDay.after(this.getOldestDate()))
			return false;

		if (this.getYoungestDate() != null
				&& !DatePickerUtils.addDays(selectedDay, 1).after(
						this.getYoungestDate()))
			return false;

		return true;
	}

	public Date getOldestDate() {
		return oldestDate;
	}

	public void setOldestDate(Date oldestDate) {
		this.oldestDate = oldestDate;
	}

	public Date getYoungestDate() {
		return youngestDate;
	}

	public void setYoungestDate(Date youngestDate) {
		this.youngestDate = youngestDate;
	}

	/**
	 * @see com.google.gwt.user.client.ui.TextBoxBase#addChangeHandler(ChangeHandler)
	 */
	@Override
	public HandlerRegistration addChangeHandler(ChangeHandler handler) {
		HandlerRegistration handlerRegistration = super
				.addChangeHandler(handler);
		if (changeHandlers == null) {
			changeHandlers = new ArrayList<ChangeHandler>();
		}
		changeHandlers.add(handler);
		return handlerRegistration;
	}

	/**
	 * Warns {@link ChangeHandler} from {@link #changeHandlers} that a change
	 * happened without giving a {@link ChangeEvent}.
	 */
	// protected void fireChange() {
	// for (ChangeHandler changeHandler : changeHandlers) {
	// changeHandler.onChange(null);
	// }
	// }

	public void addDateValueChangeHandler(DateValueChangeHandler changeHandler) {
		this.handler = changeHandler;
	}

	@Override
	public void onFocus(FocusEvent arg0) {

		showPopup();

	}

	@Override
	public void onMouseWheel(MouseWheelEvent event) {
		if (this.isEnabled()) {
			if (event.isNorth()) {
				processDecrementDate(this.getCursorPos());
			} else if (event.isSouth()) {
				processIncrementDate(this.getCursorPos());
			}
		}

	}

	@Override
	public void setTabIndex(int index) {
		super.setTabIndex(index);
	}

	public void setDate(Date dateAsObject) {
		selectedDate = dateAsObject;
		this.setText(dateFormatter.format(this.selectedDate));
	}

	@Override
	public void onKeyUp(KeyUpEvent event) {
		int keyCode = event.getNativeEvent().getKeyCode();

		switch (keyCode) {
		case KeyCodes.KEY_ENTER:
			parseDate();
			showPopup();
			break;
		case KeyCodes.KEY_ESCAPE:
			popup.hide();
			break;
		case KeyCodes.KEY_UP:
			processIncrementDate(this.getCursorPos());
			break;
		case KeyCodes.KEY_DOWN:
			processDecrementDate(this.getCursorPos());
			break;
		default:
			break;
		}
	}

}
