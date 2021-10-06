package com.vimukti.accounter.web.client.ui.widgets;

import java.util.Date;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLTable.Cell;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.vimukti.accounter.web.client.ui.StyledPanel;

/**
 * Popup used by the datePicker. It represents a calendar and allows the user to
 * select a date. It is localizable thanks to the DateTimerFormat class(GWT
 * class) and the DateLocale class.
 * 
 * @author Zenika
 * @author Loïc Bertholet - Zenika
 */
public class PopupCalendar extends PopupPanel {

	private boolean leave;

	public boolean isLeave() {
		return leave;
	}

	public void setLeave(boolean leave) {
		this.leave = leave;
	}

	private String theme;
	private final DatePicker datePicker;
	private DateTimeFormat dayNameFormat;
	private DateTimeFormat monthFormat;
	private DateTimeFormat dayNumberFormat;
	private Label currentMonth;
	private Grid daysGrid;
	private Date displayedMonth;
	private DatePopupCalendar datePopupCalendar;

	{
		this.leave = true;
		this.theme = "blue";
		this.dayNameFormat = DateTimeFormat.getFormat("E");
		this.monthFormat = DateTimeFormat.getFormat("MMMM yyyy");
		this.dayNumberFormat = DateTimeFormat.getFormat("d");
		this.daysGrid = new Grid(7, 7);

	}

	/**
	 * Create a calendar popup. You have to call the displayMonth method to
	 * display the the popup.
	 * 
	 * @param datePicker
	 *            The date picker on which the popup is attached
	 */
	public PopupCalendar(DatePicker datePicker) {
		super(true);
		this.datePicker = datePicker;
		this.addStyleName(theme + "-date-picker");
		StyledPanel panel = new StyledPanel("panel");
		panel.setStyleName("datePickerMonthSelector");
		this.add(panel);

		sinkEvents(Event.ONBLUR);

		drawMonthLine(panel);
		drawWeekLine(panel);
		drawDayGrid(panel);

		datePopupCalendar = new DatePopupCalendar(this.datePicker);
	}

	/**
	 * Return the month displayed by the PopupCalendar.
	 * 
	 * @return a Date pointing to the month
	 */
	public Date getDisplayedMonth() {
		return displayedMonth;
	}

	/**
	 * Set the month which is display by the PopupCalendar.
	 * 
	 * @param displayedMonth
	 *            The Date to display
	 */
	public void setDisplayedMonth(Date displayedMonth) {
		this.displayedMonth = displayedMonth;
	}

	/**
	 * Return the theme used by the PopupCalendar.
	 * 
	 * @return Name of the theme
	 */
	public String getTheme() {
		return this.theme;
	}

	/**
	 * Set the theme used by the PopupCalendar.
	 * 
	 * @param theme
	 *            Name of the theme
	 */
	public void setTheme(String theme) {
		this.theme = theme;
		this.addStyleName(theme + "-date-picker");
	}

	/**
	 * Refresh the PopupCalendar and show it.
	 */
	public void displayMonth() {
		if (this.displayedMonth == null) {
			if (datePicker.getSelectedDate() != null)
				this.displayedMonth = datePicker.getSelectedDate();
			else {
				this.displayedMonth = new Date();
			}
		}

		datePopupCalendar.setDisplayMonth(this.displayedMonth);
		this.drawLabelMoisAnnee();
		this.drawDaysGridContent(this.displayedMonth);
		show();
	}

	/**
	 * This method is destined to be used by the DatePicker in case of focus
	 * lost. It creates a delay before the popup hides to allows the popup to
	 * catch a click and eventually update the Date of the DatePicker.
	 */
	public void hidePopupCalendar() {

		DeferredCommand.addCommand(new Command() {
			public void execute() {
				Timer t = new Timer() {
					public void run() {
						if (leave) {
							hide();
						} else {
							leave = true;
						}
					}
				};
				t.schedule(150);
			}

		});
	}

	/**
	 * Draw the monthLine with contains navigations buttons (change the month
	 * and the year) and displayed the displayed month.
	 * 
	 * @param panel
	 *            The panel contained in the popup
	 */
	private void drawMonthLine(Panel panel) {
		Grid monthLine = new Grid(1, 5);
		monthLine.setStyleName(theme + "-" + "month-line");
		CellFormatter monthCellFormatter = monthLine.getCellFormatter();

		Label previousYear = new Label("«");
		previousYear.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				leave = false;
				PopupCalendar.this.changeMonth(-1);
			}
		});
		monthLine.setWidget(0, 0, previousYear);
		Label previousMonth = new Label("Prev");
		previousMonth.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				leave = false;
				PopupCalendar.this.changeMonth(-1);
			};
		});
		previousMonth.addStyleName("prenextyear-label");
		monthLine.setWidget(0, 1, previousMonth);
		// monthCellFormatter.setWidth(0, 2, "80%");
		currentMonth = new Label();
		currentMonth.addStyleName("prenextyear-label");
		// currentMonth.addStyleName("currentmonth");
		currentMonth.addMouseOverHandler(new MouseOverHandler() {

			@Override
			public void onMouseOver(MouseOverEvent event) {
				currentMonth.addStyleName("currentmonth-hover");
			}
		});
		currentMonth.addMouseOutHandler(new MouseOutHandler() {

			@Override
			public void onMouseOut(MouseOutEvent event) {
				currentMonth.removeStyleName("currentmonth-hover");
			}
		});
		currentMonth.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				leave = false;
				int x = getAbsoluteLeft();
				int y = getAbsoluteTop() + 22;
				// this.setWidth(this.getOffsetWidth() + "px");
				if (!datePopupCalendar.isShowing()) {

					datePopupCalendar.setPopupPosition(x + 1, y + 30);
					// datePopupCalendar.setWidth(getOffsetWidth() + "px");
					// datePopupCalendar.setHeight(getOffsetHeight() - 55 +
					// "px");
					datePopupCalendar.show();
				}
			}
		});
		monthLine.setWidget(0, 2, currentMonth);
		Label nextMonth = new Label("Next");
		nextMonth.addStyleName("prenextyear-label");
		nextMonth.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				leave = false;
				PopupCalendar.this.changeMonth(1);
			};
		});
		monthLine.setWidget(0, 3, nextMonth);
		Label nextYear = new Label("»");
		nextYear.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				leave = false;
				PopupCalendar.this.changeMonth(1);
			}
		});
		monthLine.setWidget(0, 4, nextYear);
		panel.add(monthLine);
	}

	/**
	 * Draw the week line which displays first letter of week days. example : S
	 * M T ....etc
	 * 
	 * @param panel
	 *            The panel contained in the popup
	 */
	private void drawWeekLine(Panel panel) {
		Grid weekLine = new Grid(1, 7);
		weekLine.setStyleName(theme + "-" + "week-line");
		Date weekFirstday = DatePickerUtils.getWeekFirstDay();
		for (int i = 0; i < 7; i++) {
			weekLine.setText(0, i, dayNameFormat.format(DatePickerUtils
					.addDays(weekFirstday, i)));
		}
		panel.add(weekLine);
	}

	/**
	 * Display the grid which contains the days. When a day is clicked, it
	 * updates the Date contained in the DatePicker.
	 * 
	 * @param panel
	 *            The panel contained in the popup
	 */
	private void drawDayGrid(Panel panel) {

		this.daysGrid.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				Cell cell = daysGrid.getCellForEvent(event);
				int rowIndex = cell.getRowIndex();
				int cellIndex = cell.getCellIndex();
				Date selectedDay = DatePickerUtils.addDays(
						getDaysGridOrigin(displayedMonth), rowIndex * 7
								+ cellIndex);
				if (datePicker.canBeSelected(selectedDay)) {
					datePicker.setSelectedDate(selectedDay);
					datePicker.synchronizeFromDate();
					PopupCalendar.this.hide();
					leave = true;
				}
			}
		});

		daysGrid.setStyleName(theme + "-" + "day-grid");
		panel.add(daysGrid);
	}

	/**
	 * Update the Label which shows the displayed month (in the month line).
	 */
	private void drawLabelMoisAnnee() {
		currentMonth.setText(monthFormat.format(this.displayedMonth));
	}

	/**
	 * Draw the days into the days grid. Days drawn are the days of the
	 * displayed month and few days after and before the displayed month.
	 * 
	 * @param displayedMonth
	 *            Date of the displayed month
	 */
	private void drawDaysGridContent(Date displayedMonth) {
		CellFormatter cfJours = daysGrid.getCellFormatter();
		Date cursor = this.getDaysGridOrigin(displayedMonth);
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 7; j++) {
				daysGrid.setText(i, j, dayNumberFormat.format(cursor));
				cfJours.removeStyleName(i, j, theme + "-" + "selected");
				cfJours.removeStyleName(i, j, theme + "-"
						+ "current-month-selected");
				cfJours.removeStyleName(i, j, theme + "-" + "other-day");
				cfJours.removeStyleName(i, j, theme + "-"
						+ "current-month-other-day");
				cfJours.removeStyleName(i, j, theme + "-" + "week-end");
				cfJours.removeStyleName(i, j, theme + "-"
						+ "current-month-week-end");
				cfJours.removeStyleName(i, j, theme + "-" + "cant-be-selected");

				if (!datePicker.canBeSelected(cursor))
					cfJours.addStyleName(i, j, theme + "-" + "cant-be-selected");
				else if (this.displayedMonth != null
						&& DatePickerUtils.areEquals(this.displayedMonth,
								cursor))
					if (displayedMonth.getMonth() == cursor.getMonth())
						cfJours.addStyleName(i, j, theme + "-"
								+ "current-month-selected");
					else
						cfJours.addStyleName(i, j, theme + "-" + "selected");
				else if (DatePickerUtils.isInWeekEnd(cursor))
					if (displayedMonth.getMonth() == cursor.getMonth())
						cfJours.addStyleName(i, j, theme + "-"
								+ "current-month-week-end");
					else
						cfJours.addStyleName(i, j, theme + "-" + "week-end");
				else if (displayedMonth.getMonth() == cursor.getMonth())
					cfJours.addStyleName(i, j, theme + "-"
							+ "current-month-other-day");
				else
					cfJours.addStyleName(i, j, theme + "-" + "other-day");

				cursor = DatePickerUtils.addDays(cursor, 1);
			}
		}
	}

	/**
	 * Change the displayed month.
	 * 
	 * @param i
	 *            Number of month to add to the displayed month
	 */
	protected void changeMonth(int i) {
		this.displayedMonth = DatePickerUtils.addMonths(this.displayedMonth, i);
		this.displayMonth();
	}

	/**
	 * Return the first day to display. If the month first day is after the 5th
	 * day of the week, it return the first day of the week. Else, it returns
	 * the first day of the week before.
	 * 
	 * @param displayedMonth
	 * @return The first day to display in the grid
	 */
	private Date getDaysGridOrigin(Date displayedMonth) {
		int currentYear = displayedMonth.getYear();
		int currentMonth = displayedMonth.getMonth();
		CellFormatter cfJours = daysGrid.getCellFormatter();
		Date monthFirstDay = new Date(currentYear, currentMonth, 1);
		int indice = DatePickerUtils.getWeekDayIndex(monthFirstDay);
		Date origineTableau;
		if (indice > 4) {
			origineTableau = DatePickerUtils.getWeekFirstDay(monthFirstDay);
		} else {
			origineTableau = DatePickerUtils.getWeekFirstDay(DatePickerUtils
					.addDays(monthFirstDay, -7));
		}
		return origineTableau;
	}

	// @Override
	// public void onKeyPress(KeyPressEvent event) {
	//
	// System.out.println("Yes comming");
	// NativeEvent nativeEvent = event.getNativeEvent();
	// if (nativeEvent.getKeyCode() == KeyCodes.KEY_UP) {
	//
	// if (y != 0) {
	// --y;
	//
	// updateFocus(x, y);
	// }
	//
	// } else if (nativeEvent.getKeyCode() == KeyCodes.KEY_DOWN)
	//
	// {
	//
	// ++y;
	// updateFocus(x, y);
	//
	// } else if (nativeEvent.getKeyCode() == KeyCodes.KEY_LEFT) {
	//
	// if (x != 0) {
	// --x;
	// updateFocus(x, y);
	// }
	// } else if (nativeEvent.getKeyCode() == KeyCodes.KEY_RIGHT) {
	//
	// ++x;
	// updateFocus(x, y);
	// }
	//
	// }

}