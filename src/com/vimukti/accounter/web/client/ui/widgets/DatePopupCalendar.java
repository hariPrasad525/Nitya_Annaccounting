package com.vimukti.accounter.web.client.ui.widgets;

import java.util.Date;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLTable.Cell;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;
import com.google.gwt.user.client.ui.PopupPanel;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.util.DayAndMonthUtil;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class DatePopupCalendar extends PopupPanel {

	private Grid daysGrid;
	private DatePicker datePicker;
	private Button okButton;
	private Button cancelButton;
	private Button nextButton, prevButton;
	private Date displayMonth;
	final private StyledPanel panel;
	String months[][] = { { DayAndMonthUtil.jan(), DayAndMonthUtil.jul() },
			{ DayAndMonthUtil.feb(), DayAndMonthUtil.aug() },
			{ DayAndMonthUtil.mar(), DayAndMonthUtil.sep() },
			{ DayAndMonthUtil.apr(), DayAndMonthUtil.oct() },
			{ DayAndMonthUtil.mayS(), DayAndMonthUtil.nov() },
			{ DayAndMonthUtil.jun(), DayAndMonthUtil.dec() } };
	private int year;
	private String month;

	public DatePopupCalendar(DatePicker datePicker) {
		super(true);
		this.addStyleName("blue" + "-date-picker");
		this.daysGrid = new Grid(6, 4);
		this.datePicker = datePicker;
		panel = new StyledPanel("panel");

		this.add(panel);
		sinkEvents(Event.ONBLUR);
		nextButton(panel);
		drawButtons(panel);
		this.addStyleName("nextmonths-years");

	}

	@Override
	public void onBrowserEvent(Event event) {
		switch (DOM.eventGetType(event)) {
		case Event.ONBLUR:
			DatePopupCalendar.this.hide();
			break;
		default:
			break;

		}
		super.onBrowserEvent(event);
	}

	private void nextButton(StyledPanel panel) {
		prevButton = new Button("-");
//		prevButton.setWidth("60%");
		nextButton = new Button("+");
//		nextButton.setWidth("60%");
	}

	private void drawButtons(final StyledPanel panel) {
		AccounterMessages messages = Global.get().messages();
		okButton = new Button(messages.ok());
		okButton.addClickHandler(new ClickHandler() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(ClickEvent event) {
				if (datePicker.getSelectedDate() == null) {
					datePicker.setSelectedDate(new Date());
				}
				if (month == null) {
					month = String.valueOf(datePicker.getSelectedDate()
							.getMonth());
				}
				datePicker.getSelectedDate().setYear(getYear() - 1900);
				datePicker.getSelectedDate().setMonth(
						new DateUtills().evaluateMonthFormat(month) - 1);
				datePicker.synchronizeFromDate();
				datePicker.showPopup();
				hide();

			}
		});
		cancelButton = new Button(messages.cancel());

		cancelButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				hide();
			}
		});
		nextButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				panel.clear();
				int y = getYear();
				y = y + 10;
				setYear(y);
				drawGrid(panel);

			}
		});
		prevButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				panel.clear();
				int y = getYear();
				y = y - 10;
				setYear(y);
				drawGrid(panel);
			}
		});

	}

	private void drawGrid(StyledPanel panel) {

		daysGrid.setStyleName("blue" + "-" + "day-grid");
		CellFormatter cfJours = daysGrid.getCellFormatter();

		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 2; j++) {

				daysGrid.setText(i, j, months[i][j]);

				if (getDisplayMonth() != null) {
					if (DateUtills.getMonthNameByNumber(
							getDisplayMonth().getMonth() + 1).equals(
							daysGrid.getText(i, j))) {
						cfJours.addStyleName(i, j, "blue" + "-"
								+ "current-month-selected");
					} else {
						cfJours.removeStyleName(i, j, "blue" + "-"
								+ "current-month-selected");

					}
				}

			}

		}

		daysGrid.setWidget(0, 2, prevButton);
		daysGrid.setWidget(0, 3, nextButton);

		int k = this.getYear();
		for (int i = 1; i < 6; i++) {
			for (int j = 2; j < 4; j++) {
				if (getDisplayMonth() != null) {
					if (getDisplayMonth().getYear() + 1900 == k) {
						cfJours.addStyleName(i, j, "blue" + "-"
								+ "current-month-selected");
					} else {
						cfJours.removeStyleName(i, j, "blue" + "-"
								+ "current-month-selected");
					}
				}
				daysGrid.setText(i, j, k + "");
				k = k + 1;

			}

		}
		// setYear(k);

		this.daysGrid.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				Cell cell = daysGrid.getCellForEvent(event);
				int rowIndex = cell.getRowIndex();
				int cellIndex = cell.getCellIndex();
				CellFormatter cfJours = daysGrid.getCellFormatter();
				if (cellIndex < 2) {
					for (int i = 0; i < 6; i++) {
						for (int j = 0; j < 2; j++) {
							cfJours.removeStyleName(i, j, "blue" + "-"
									+ "current-month-selected");

						}
					}
					month = daysGrid.getText(rowIndex, cellIndex);

				} else {
					for (int i = 1; i < 6; i++) {
						for (int j = 2; j < 4; j++) {
							cfJours.removeStyleName(i, j, "blue" + "-"
									+ "current-month-selected");

						}
					}
					String n = daysGrid.getText(rowIndex, cellIndex).trim();
					if (!n.isEmpty() && !n.equals("+") && !n.equals("-")) {
						setYear(Integer.parseInt(n));
					}

				}
				if ((rowIndex == 0 && cellIndex == 2)
						|| (rowIndex == 0 && cellIndex == 3)) {

				} else {
					cfJours.addStyleName(rowIndex, cellIndex, "blue" + "-"
							+ "current-month-selected");
				}

			}

		});
		panel.add(daysGrid);
		StyledPanel h = new StyledPanel("h");

		// h.setSpacing(10);
//		h.setWidth("100%");
		h.add(okButton);

		// h.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		h.add(cancelButton);
		// h.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		// h.setCellHorizontalAlignment(okButton,
		// HasHorizontalAlignment.ALIGN_LEFT);
		// h.setCellHorizontalAlignment(cancelButton,
		// HasHorizontalAlignment.ALIGN_RIGHT);
		panel.add(h);
		h.addStyleName("prenextmonth-okcancel");

	}

	@Override
	public void show() {
		super.show();
		if (getDisplayMonth() != null) {
			setYear(getDisplayMonth().getYear() + 1900);
			month = DateUtills.getMonthNameByNumber(getDisplayMonth()
					.getMonth() + 1);
		}
		panel.clear();

		drawGrid(panel);

	}

	public int getYear() {
		return this.year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public Date getDisplayMonth() {
		return displayMonth;
	}

	public void setDisplayMonth(Date displayMonth) {
		this.displayMonth = displayMonth;
	}

}
