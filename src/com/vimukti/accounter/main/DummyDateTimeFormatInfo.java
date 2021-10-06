package com.vimukti.accounter.main;

import com.google.gwt.i18n.client.DateTimeFormatInfo;

public class DummyDateTimeFormatInfo implements DateTimeFormatInfo {

	private int weekendStart;
	private int weekendEnd;
	private String[] weekdaysShortStandalone;
	private String[] weekdaysShort;
	private String[] weekdaysNarrowStandalone;
	private String[] weekdaysNarrow;
	private String[] weekdaysFullStandalone;
	private String[] weekdaysFull;
	private String timeFormatShort;
	private String timeFormatMedium;
	private String timeFormatLong;
	private String timeFormatFull;
	private String timeFormat;
	private String[] quartersShort;
	private String[] quartersFull;
	private String[] monthsShortStandalone;
	private String[] monthsShort;
	private String[] monthsNarrowStandalone;
	private String[] monthsNarrow;
	private String[] monthsFullStandalone;
	private String[] monthsFull;
	private String formatYearQuarterShort;
	private String formatYearQuarterFull;
	private String formatYearMonthWeekdayDay;
	private String formatYearMonthNumDay;
	private String formatYearMonthNum;
	private String formatYearMonthFullDay;
	private String formatYearMonthFull;
	private String formatYearMonthAbbrevDay;
	private String formatYearMonthAbbrev;
	private String formatYear;
	private String formatMonthNumDay;
	private String formatMonthFullWeekdayDay;
	private String formatMonthFullDay;
	private String formatMonthFull;
	private String formatMonthAbbrevDay;
	private String formatHour24MinuteSecond;
	private String formatMinuteSecond;
	private String formatMonthAbbrev;
	private String formatHour24Minute;
	private String formatHour12MinuteSecond;
	private String formatHour12Minute;
	private String formatDay;
	private String[] ampms;
	private String dateFormat;
	private String dateFormatMedium;
	private String dateFormatLong;
	private String dateFormatFull;
	private String dateTime;
	private String dateTimeFull;
	private String dateTimeLong;
	private String dateFormatShort;
	private int firstDayOfTheWeek;
	private String[] erasShort;
	private String[] erasFull;
	private String dateTimeShort;
	private String dateTimeMedium;

	@Override
	public String[] ampms() {
		return ampms;
	}

	public void setAmpms(String[] ampms) {
		this.ampms = ampms;
	}

	@Override
	public String dateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	@Override
	public String dateFormatFull() {
		return dateFormatFull;
	}

	public void setDateFormatFull(String dateFormatFull) {
		this.dateFormatFull = dateFormatFull;
	}

	@Override
	public String dateFormatLong() {
		return dateFormatLong;
	}

	public void setDateFormatLong(String dateFormatLong) {
		this.dateFormatLong = dateFormatLong;
	}

	@Override
	public String dateFormatMedium() {
		return dateFormatMedium;
	}

	public void setdateFormatMedium(String dateFormatMedium) {
		this.dateFormatMedium = dateFormatMedium;
	}

	@Override
	public String dateFormatShort() {
		return dateFormatShort;
	}

	public void setDateFormatShort(String dateFormatShort) {
		this.dateFormatShort = dateFormatShort;
	}

	@Override
	public String dateTime(String timePattern, String datePattern) {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	@Override
	public String dateTimeFull(String timePattern, String datePattern) {
		return dateTimeFull;
	}

	public void setDateTimeFull(String dateTimeFull) {
		this.dateTimeFull = dateTimeFull;
	}

	@Override
	public String dateTimeLong(String timePattern, String datePattern) {
		return dateTimeLong;
	}

	public void setDateTimeLong(String dateTimeLong) {
		this.dateTimeLong = dateTimeLong;
	}

	@Override
	public String dateTimeMedium(String timePattern, String datePattern) {
		return dateTimeMedium;
	}

	public void setDateTimeMedium(String dateTimeMedium) {
		this.dateTimeMedium = dateTimeMedium;
	}

	@Override
	public String dateTimeShort(String timePattern, String datePattern) {
		return dateTimeShort;
	}

	public void setdateTimeShort(String dateTimeShort) {
		this.dateTimeShort = dateTimeShort;
	}

	@Override
	public String[] erasFull() {
		return erasFull;
	}

	public void setErasFull(String[] erasFull) {
		this.erasFull = erasFull;
	}

	@Override
	public String[] erasShort() {
		return erasShort;
	}

	public void seterasShort(String[] erasShort) {
		this.erasShort = erasShort;
	}

	@Override
	public int firstDayOfTheWeek() {
		return firstDayOfTheWeek;
	}

	public void setFirstDayOfTheWeek(int firstDayOfTheWeek) {
		this.firstDayOfTheWeek = firstDayOfTheWeek;
	}

	@Override
	public String formatDay() {
		return formatDay;
	}

	public void setFormatDay(String formatDay) {
		this.formatDay = formatDay;
	}

	@Override
	public String formatHour12Minute() {
		return formatHour12Minute;
	}

	public void setFormatHour12Minute(String formatHour12Minute) {
		this.formatHour12Minute = formatHour12Minute;
	}

	@Override
	public String formatHour12MinuteSecond() {
		return formatHour12MinuteSecond;
	}

	public void setFormatHour12MinuteSecond(String formatHour12MinuteSecond) {
		this.formatHour12MinuteSecond = formatHour12MinuteSecond;
	}

	@Override
	public String formatHour24Minute() {
		return formatHour24Minute;
	}

	public void setFormatHour24Minute(String formatHour24Minute) {
		this.formatHour24Minute = formatHour24Minute;
	}

	@Override
	public String formatHour24MinuteSecond() {
		return formatHour24MinuteSecond;
	}

	public void setFormatHour24MinuteSecond(String formatHour24MinuteSecond) {
		this.formatHour24MinuteSecond = formatHour24MinuteSecond;
	}

	@Override
	public String formatMinuteSecond() {
		return formatMinuteSecond;
	}

	public void setFormatMinuteSecond(String formatMinuteSecond) {
		this.formatMinuteSecond = formatMinuteSecond;
	}

	@Override
	public String formatMonthAbbrev() {
		return formatMonthAbbrev;
	}

	public void setFormatMonthAbbrev(String formatMonthAbbrev) {
		this.formatMonthAbbrev = formatMonthAbbrev;
	}

	@Override
	public String formatMonthAbbrevDay() {
		return formatMonthAbbrevDay;
	}

	public void setFormatMonthAbbrevDay(String formatMonthAbbrevDay) {
		this.formatMonthAbbrevDay = formatMonthAbbrevDay;
	}

	@Override
	public String formatMonthFull() {
		return formatMonthFull;
	}

	public void setFormatMonthFull(String formatMonthFull) {
		this.formatMonthFull = formatMonthFull;
	}

	@Override
	public String formatMonthFullDay() {
		return formatMonthFullDay;
	}

	public void setFormatMonthFullDay(String formatMonthFullDay) {
		this.formatMonthFullDay = formatMonthFullDay;
	}

	@Override
	public String formatMonthFullWeekdayDay() {
		return formatMonthFullWeekdayDay;
	}

	public void setFormatMonthFullWeekdayDay(String formatMonthFullWeekdayDay) {
		this.formatMonthFullWeekdayDay = formatMonthFullWeekdayDay;
	}

	@Override
	public String formatMonthNumDay() {
		return formatMonthNumDay;
	}

	public void setFormatMonthNumDay(String formatMonthNumDay) {
		this.formatMonthNumDay = formatMonthNumDay;
	}

	@Override
	public String formatYear() {
		return formatYear;
	}

	public void setFormatYear(String formatYear) {
		this.formatYear = formatYear;
	}

	@Override
	public String formatYearMonthAbbrev() {
		return formatYearMonthAbbrev;
	}

	public void setFormatYearMonthAbbrev(String formatYearMonthAbbrev) {
		this.formatYearMonthAbbrev = formatYearMonthAbbrev;
	}

	@Override
	public String formatYearMonthAbbrevDay() {
		return formatYearMonthAbbrevDay;
	}

	public void setFormatYearMonthAbbrevDay(String formatYearMonthAbbrevDay) {
		this.formatYearMonthAbbrevDay = formatYearMonthAbbrevDay;
	}

	@Override
	public String formatYearMonthFull() {
		return formatYearMonthFull;
	}

	public void setFormatYearMonthFull(String formatYearMonthFull) {
		this.formatYearMonthFull = formatYearMonthFull;
	}

	@Override
	public String formatYearMonthFullDay() {
		return formatYearMonthFullDay;
	}

	public void setFormatYearMonthFullDay(String formatYearMonthFullDay) {
		this.formatYearMonthFullDay = formatYearMonthFullDay;
	}

	@Override
	public String formatYearMonthNum() {
		return formatYearMonthNum;
	}

	public void setFormatYearMonthNum(String formatYearMonthNum) {
		this.formatYearMonthNum = formatYearMonthNum;
	}

	@Override
	public String formatYearMonthNumDay() {
		return formatYearMonthNumDay;
	}

	public void setFormatYearMonthNumDay(String formatYearMonthNumDay) {
		this.formatYearMonthNumDay = formatYearMonthNumDay;
	}

	@Override
	public String formatYearMonthWeekdayDay() {
		return formatYearMonthWeekdayDay;
	}

	public void setFormatYearMonthWeekdayDay(String formatYearMonthWeekdayDay) {
		this.formatYearMonthWeekdayDay = formatYearMonthWeekdayDay;
	}

	@Override
	public String formatYearQuarterFull() {
		return formatYearQuarterFull;
	}

	public void setFormatYearQuarterFull(String formatYearQuarterFull) {
		this.formatYearQuarterFull = formatYearQuarterFull;
	}

	@Override
	public String formatYearQuarterShort() {
		return formatYearQuarterShort;
	}

	public void setFormatYearQuarterShort(String formatYearQuarterShort) {
		this.formatYearQuarterShort = formatYearQuarterShort;
	}

	@Override
	public String[] monthsFull() {
		return monthsFull;
	}

	public void setMonthsFull(String[] monthsFull) {
		this.monthsFull = monthsFull;
	}

	@Override
	public String[] monthsFullStandalone() {
		return monthsFullStandalone;
	}

	public void setMonthsFullStandalone(String[] monthsFullStandalone) {
		this.monthsFullStandalone = monthsFullStandalone;
	}

	@Override
	public String[] monthsNarrow() {
		return monthsNarrow;
	}

	public void setMonthsNarrow(String[] monthsNarrow) {
		this.monthsNarrow = monthsNarrow;
	}

	@Override
	public String[] monthsNarrowStandalone() {
		return monthsNarrowStandalone;
	}

	public void setMonthsNarrowStandalone(String[] monthsNarrowStandalone) {
		this.monthsNarrowStandalone = monthsNarrowStandalone;
	}

	@Override
	public String[] monthsShort() {
		return monthsShort;
	}

	public void setMonthsShort(String[] monthsShort) {
		this.monthsShort = monthsShort;
	}

	@Override
	public String[] monthsShortStandalone() {
		return monthsShortStandalone;
	}

	public void setMonthsShortStandalone(String[] monthsShortStandalone) {
		this.monthsShortStandalone = monthsShortStandalone;
	}

	@Override
	public String[] quartersFull() {
		return quartersFull;
	}

	public void setQuartersFull(String[] quartersFull) {
		this.quartersFull = quartersFull;
	}

	@Override
	public String[] quartersShort() {
		return quartersShort;
	}

	public void setQuartersShort(String[] quartersShort) {
		this.quartersShort = quartersShort;
	}

	@Override
	public String timeFormat() {
		return timeFormat;
	}

	public void setTimeFormat(String timeFormat) {
		this.timeFormat = timeFormat;
	}

	@Override
	public String timeFormatFull() {
		return timeFormatFull;
	}

	public void setTimeFormatFull(String timeFormatFull) {
		this.timeFormatFull = timeFormatFull;
	}

	@Override
	public String timeFormatLong() {
		return timeFormatLong;
	}

	public void setTimeFormatLong(String timeFormatLong) {
		this.timeFormatLong = timeFormatLong;
	}

	@Override
	public String timeFormatMedium() {
		return timeFormatMedium;
	}

	public void setTimeFormatMedium(String timeFormatMedium) {
		this.timeFormatMedium = timeFormatMedium;
	}

	@Override
	public String timeFormatShort() {
		return timeFormatShort;
	}

	public void setTimeFormatShort(String timeFormatShort) {
		this.timeFormatShort = timeFormatShort;
	}

	@Override
	public String[] weekdaysFull() {
		return weekdaysFull;
	}

	public void setWeekdaysFull(String[] weekdaysFull) {
		this.weekdaysFull = weekdaysFull;
	}

	@Override
	public String[] weekdaysFullStandalone() {
		return weekdaysFullStandalone;
	}

	public void setWeekdaysFullStandalone(String[] weekdaysFullStandalone) {
		this.weekdaysFullStandalone = weekdaysFullStandalone;
	}

	@Override
	public String[] weekdaysNarrow() {
		return weekdaysNarrow;
	}

	public void setWeekdaysNarrow(String[] weekdaysNarrow) {
		this.weekdaysNarrow = weekdaysNarrow;
	}

	@Override
	public String[] weekdaysNarrowStandalone() {
		return weekdaysNarrowStandalone;
	}

	public void setWeekdaysNarrowStandalone(String[] weekdaysNarrowStandalone) {
		this.weekdaysNarrowStandalone = weekdaysNarrowStandalone;
	}

	@Override
	public String[] weekdaysShort() {
		return weekdaysShort;
	}

	public void setWeekdaysShort(String[] weekdaysShort) {
		this.weekdaysShort = weekdaysShort;
	}

	@Override
	public String[] weekdaysShortStandalone() {
		return weekdaysShortStandalone;
	}

	public void setWeekdaysShortStandalone(String[] weekdaysShortStandalone) {
		this.weekdaysShortStandalone = weekdaysShortStandalone;
	}

	@Override
	public int weekendEnd() {
		return weekendEnd;
	}

	public void setWeekendEnd(int weekendEnd) {
		this.weekendEnd = weekendEnd;
	}

	@Override
	public int weekendStart() {
		return weekendStart;
	}

	public void setWeekendStart(int weekendStart) {
		this.weekendStart = weekendStart;

	}

}
