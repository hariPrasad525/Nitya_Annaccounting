package com.nitya.accounter.web.client.ui.core;

import java.util.Date;

/**
 * this class will full fill the lack of java.util.Calendar class in GWT
 * 
 * @author sriramaraju
 * 
 */

public class Calendar implements Cloneable {

	public final static long DAY_MILLSEC = 1000 * 60 * 60 * 24;

	/**
	 * Field number for <code>get</code> and <code>set</code> indicating the
	 * era, e.g., AD or BC in the Julian calendar. This is a calendar-specific
	 * value; see subclass documentation.
	 * 
	 */
	public final static int ERA = 0;

	/**
	 * Field number for <code>get</code> and <code>set</code> indicating the
	 * year. This is a calendar-specific value; see subclass documentation.
	 */
	public final static int YEAR = 1;

	/**
	 * Field number for <code>get</code> and <code>set</code> indicating the
	 * month. This is a calendar-specific value. The first month of the year in
	 * the Gregorian and Julian calendars is <code>JANUARY</code> which is 0;
	 * the last depends on the number of months in a year.
	 * 
	 * @see #JANUARY
	 * @see #FEBRUARY
	 * @see #MARCH
	 * @see #APRIL
	 * @see #MAY
	 * @see #JUNE
	 * @see #JULY
	 * @see #AUGUST
	 * @see #SEPTEMBER
	 * @see #OCTOBER
	 * @see #NOVEMBER
	 * @see #DECEMBER
	 * @see #UNDECIMBER
	 */
	public final static int MONTH = 2;

	/**
	 * Field number for <code>get</code> and <code>set</code> indicating the
	 * week number within the current year. The first week of the year, as
	 * defined by <code>getFirstDayOfWeek()</code> and
	 * <code>getMinimalDaysInFirstWeek()</code>, has value 1. Subclasses define
	 * the value of <code>WEEK_OF_YEAR</code> for days before the first week of
	 * the year.
	 * 
	 * @see #getFirstDayOfWeek
	 * @see #getMinimalDaysInFirstWeek
	 */
	public final static int WEEK_OF_YEAR = 3;

	/**
	 * Field number for <code>get</code> and <code>set</code> indicating the
	 * week number within the current month. The first week of the month, as
	 * defined by <code>getFirstDayOfWeek()</code> and
	 * <code>getMinimalDaysInFirstWeek()</code>, has value 1. Subclasses define
	 * the value of <code>WEEK_OF_MONTH</code> for days before the first week of
	 * the month.
	 * 
	 * @see #getFirstDayOfWeek
	 * @see #getMinimalDaysInFirstWeek
	 */
	public final static int WEEK_OF_MONTH = 4;

	/**
	 * Field number for <code>get</code> and <code>set</code> indicating the day
	 * of the month. This is a synonym for <code>DAY_OF_MONTH</code>. The first
	 * day of the month has value 1.
	 * 
	 * @see #DAY_OF_MONTH
	 */
	public final static int DATE = 5;

	/**
	 * Field number for <code>get</code> and <code>set</code> indicating the day
	 * of the month. This is a synonym for <code>DATE</code>. The first day of
	 * the month has value 1.
	 * 
	 * @see #DATE
	 */
	public final static int DAY_OF_MONTH = 5;

	/**
	 * Field number for <code>get</code> and <code>set</code> indicating the day
	 * number within the current year. The first day of the year has value 1.
	 */
	public final static int DAY_OF_YEAR = 6;

	/**
	 * Field number for <code>get</code> and <code>set</code> indicating the day
	 * of the week. This field takes values <code>SUNDAY</code>,
	 * <code>MONDAY</code>, <code>TUESDAY</code>, <code>WEDNESDAY</code>,
	 * <code>THURSDAY</code>, <code>FRIDAY</code>, and <code>SATURDAY</code>.
	 * 
	 * @see #SUNDAY
	 * @see #MONDAY
	 * @see #TUESDAY
	 * @see #WEDNESDAY
	 * @see #THURSDAY
	 * @see #FRIDAY
	 * @see #SATURDAY
	 */
	public final static int DAY_OF_WEEK = 7;

	/**
	 * Field number for <code>get</code> and <code>set</code> indicating the
	 * ordinal number of the day of the week within the current month. Together
	 * with the <code>DAY_OF_WEEK</code> field, this uniquely specifies a day
	 * within a month. Unlike <code>WEEK_OF_MONTH</code> and
	 * <code>WEEK_OF_YEAR</code>, this field's value does <em>not</em> depend on
	 * <code>getFirstDayOfWeek()</code> or
	 * <code>getMinimalDaysInFirstWeek()</code>. <code>DAY_OF_MONTH 1</code>
	 * through <code>7</code> always correspond to <code>DAY_OF_WEEK_IN_MONTH
	 * 1</code>; <code>8</code> through <code>14</code> correspond to
	 * <code>DAY_OF_WEEK_IN_MONTH 2</code>, and so on.
	 * <code>DAY_OF_WEEK_IN_MONTH 0</code> indicates the week before
	 * <code>DAY_OF_WEEK_IN_MONTH 1</code>. Negative values count back from the
	 * end of the month, so the last Sunday of a month is specified as
	 * <code>DAY_OF_WEEK = SUNDAY, DAY_OF_WEEK_IN_MONTH = -1</code>. Because
	 * negative values count backward they will usually be aligned differently
	 * within the month than positive values. For example, if a month has 31
	 * days, <code>DAY_OF_WEEK_IN_MONTH -1</code> will overlap
	 * <code>DAY_OF_WEEK_IN_MONTH 5</code> and the end of <code>4</code>.
	 * 
	 * @see #DAY_OF_WEEK
	 * @see #WEEK_OF_MONTH
	 */
	public final static int DAY_OF_WEEK_IN_MONTH = 8;

	/**
	 * Field number for <code>get</code> and <code>set</code> indicating whether
	 * the <code>HOUR</code> is before or after noon. E.g., at 10:04:15.250 PM
	 * the <code>AM_PM</code> is <code>PM</code>.
	 * 
	 * @see #AM
	 * @see #PM
	 * @see #HOUR
	 */
	public final static int AM_PM = 9;

	/**
	 * Field number for <code>get</code> and <code>set</code> indicating the
	 * hour of the morning or afternoon. <code>HOUR</code> is used for the
	 * 12-hour clock (0 - 11). Noon and midnight are represented by 0, not by
	 * 12. E.g., at 10:04:15.250 PM the <code>HOUR</code> is 10.
	 * 
	 * @see #AM_PM
	 * @see #HOUR_OF_DAY
	 */
	public final static int HOUR = 10;

	/**
	 * Field number for <code>get</code> and <code>set</code> indicating the
	 * hour of the day. <code>HOUR_OF_DAY</code> is used for the 24-hour clock.
	 * E.g., at 10:04:15.250 PM the <code>HOUR_OF_DAY</code> is 22.
	 * 
	 * @see #HOUR
	 */
	public final static int HOUR_OF_DAY = 11;

	/**
	 * Field number for <code>get</code> and <code>set</code> indicating the
	 * minute within the hour. E.g., at 10:04:15.250 PM the <code>MINUTE</code>
	 * is 4.
	 */
	public final static int MINUTE = 12;

	/**
	 * Field number for <code>get</code> and <code>set</code> indicating the
	 * second within the minute. E.g., at 10:04:15.250 PM the
	 * <code>SECOND</code> is 15.
	 */
	public final static int SECOND = 13;

	/**
	 * Field number for <code>get</code> and <code>set</code> indicating the
	 * millisecond within the second. E.g., at 10:04:15.250 PM the
	 * <code>MILLISECOND</code> is 250.
	 */
	public final static int MILLISECOND = 14;

	/**
	 * Field number for <code>get</code> and <code>set</code> indicating the raw
	 * offset from GMT in milliseconds.
	 * <p>
	 * This field reflects the correct GMT offset value of the time zone of this
	 * <code>Calendar</code> if the <code>TimeZone</code> implementation
	 * subclass supports historical GMT offset changes.
	 */
	public final static int ZONE_OFFSET = 15;

	/**
	 * Field number for <code>get</code> and <code>set</code> indicating the
	 * daylight savings offset in milliseconds.
	 * <p>
	 * This field reflects the correct daylight saving offset value of the time
	 * zone of this <code>Calendar</code> if the <code>TimeZone</code>
	 * implementation subclass supports historical Daylight Saving Time schedule
	 * changes.
	 */
	public final static int DST_OFFSET = 16;

	/**
	 * The number of distinct fields recognized by <code>get</code> and
	 * <code>set</code>. Field numbers range from <code>0..FIELD_COUNT-1</code>.
	 */
	public final static int FIELD_COUNT = 17;

	/**
	 * Value of the {@link #DAY_OF_WEEK} field indicating Sunday.
	 */
	public final static int SUNDAY = 1;

	/**
	 * Value of the {@link #DAY_OF_WEEK} field indicating Monday.
	 */
	public final static int MONDAY = 2;

	/**
	 * Value of the {@link #DAY_OF_WEEK} field indicating Tuesday.
	 */
	public final static int TUESDAY = 3;

	/**
	 * Value of the {@link #DAY_OF_WEEK} field indicating Wednesday.
	 */
	public final static int WEDNESDAY = 4;

	/**
	 * Value of the {@link #DAY_OF_WEEK} field indicating Thursday.
	 */
	public final static int THURSDAY = 5;

	/**
	 * Value of the {@link #DAY_OF_WEEK} field indicating Friday.
	 */
	public final static int FRIDAY = 6;

	/**
	 * Value of the {@link #DAY_OF_WEEK} field indicating Saturday.
	 */
	public final static int SATURDAY = 7;

	/**
	 * Value of the {@link #MONTH} field indicating the first month of the year
	 * in the Gregorian and Julian calendars.
	 */
	public final static int JANUARY = 0;

	/**
	 * Value of the {@link #MONTH} field indicating the second month of the year
	 * in the Gregorian and Julian calendars.
	 */
	public final static int FEBRUARY = 1;

	/**
	 * Value of the {@link #MONTH} field indicating the third month of the year
	 * in the Gregorian and Julian calendars.
	 */
	public final static int MARCH = 2;

	/**
	 * Value of the {@link #MONTH} field indicating the fourth month of the year
	 * in the Gregorian and Julian calendars.
	 */
	public final static int APRIL = 3;

	/**
	 * Value of the {@link #MONTH} field indicating the fifth month of the year
	 * in the Gregorian and Julian calendars.
	 */
	public final static int MAY = 4;

	/**
	 * Value of the {@link #MONTH} field indicating the sixth month of the year
	 * in the Gregorian and Julian calendars.
	 */
	public final static int JUNE = 5;

	/**
	 * Value of the {@link #MONTH} field indicating the seventh month of the
	 * year in the Gregorian and Julian calendars.
	 */
	public final static int JULY = 6;

	/**
	 * Value of the {@link #MONTH} field indicating the eighth month of the year
	 * in the Gregorian and Julian calendars.
	 */
	public final static int AUGUST = 7;

	/**
	 * Value of the {@link #MONTH} field indicating the ninth month of the year
	 * in the Gregorian and Julian calendars.
	 */
	public final static int SEPTEMBER = 8;

	/**
	 * Value of the {@link #MONTH} field indicating the tenth month of the year
	 * in the Gregorian and Julian calendars.
	 */
	public final static int OCTOBER = 9;

	/**
	 * Value of the {@link #MONTH} field indicating the eleventh month of the
	 * year in the Gregorian and Julian calendars.
	 */
	public final static int NOVEMBER = 10;

	/**
	 * Value of the {@link #MONTH} field indicating the twelfth month of the
	 * year in the Gregorian and Julian calendars.
	 */
	public final static int DECEMBER = 11;

	/**
	 * Value of the {@link #MONTH} field indicating the thirteenth month of the
	 * year. Although <code>GregorianCalendar</code> does not use this value,
	 * lunar calendars do.
	 */
	public final static int UNDECIMBER = 12;

	/**
	 * Value of the {@link #AM_PM} field indicating the period of the day from
	 * midnight to just before noon.
	 */
	public final static int AM = 0;

	/**
	 * Value of the {@link #AM_PM} field indicating the period of the day from
	 * noon to just before midnight.
	 */
	public final static int PM = 1;

	/**
	 * getDisplayNames} indicating names in all styles, such as "January" and
	 * "Jan".
	 * 
	 * @see #SHORT
	 * @see #LONG
	 * @since 1.6
	 */
	public static final int ALL_STYLES = 0;

	/**
	 * getDisplayNames} indicating a short name, such as "Jan".
	 * 
	 * @see #LONG
	 * @since 1.6
	 */
	public static final int SHORT = 1;

	/**
	 * getDisplayNames} indicating a long name, such as "January".
	 * 
	 * @see #SHORT
	 * @since 1.6
	 */
	public static final int LONG = 2;

	public Date date = new Date();

	public static Calendar getInstance() {
		return new Calendar();
	}

	private Calendar() {
	}

	public void setTime(Date date) {
		this.date = new Date(date.getTime());
	}

	/**
	 * Returns the value of the given calendar field. In lenient mode, all
	 * calendar fields are normalized. In non-lenient mode, all calendar fields
	 * are validated and this method throws an exception if any calendar fields
	 * have out-of-range values. The normalization and validation are handled by
	 * the complete() method, which process is calendar system dependent.
	 * 
	 * @param property
	 * @return
	 */
	
	public int get(int property) {

		switch (property) {
		case MONTH:
			return this.date.getMonth();
		case DAY_OF_MONTH:
			return this.date.getDate();
		case DAY_OF_WEEK:
			return this.date.getDay() + 1;
		case HOUR_OF_DAY:
			return this.date.getHours();
		case HOUR:
			return this.date.getHours();
		case MINUTE:
			return this.date.getMinutes();
		case SECOND:
			return this.date.getSeconds();
		case YEAR:
			return this.date.getYear();
		case DAY_OF_YEAR: {
			int today = 0;

			for (int i = 0; i < this.get(MONTH); i++) {
				today = today + maxMonthDay(i);
			}
			return today + this.date.getDate();
		}
		case MILLISECOND: {
			long time = this.date.getTime();
			long woutmilli = time / 1000;
			return (int) (time - woutmilli);
		}
		default:
			break;
		}
		return 0;
	}

	/**
	 * Adds or subtracts the specified amount of time to the given calendar
	 * field, based on the calendar's rules. For example, to subtract 5 days
	 * from the current time of the calendar, you can achieve it by calling:
	 * 
	 * 
	 * add(Calendar.DAY_OF_MONTH, -5).
	 * 
	 * @param property
	 * @param i
	 */
	
	public void add(int property, int i) {
		switch (property) {
		case MONTH:
			this.date.setMonth(date.getMonth() + i);
			break;
		case DAY_OF_MONTH:
			this.date.setDate(date.getDate() + i);
			break;
		case DAY_OF_WEEK:
			this.date.setDate(date.getDate() + i);
			break;
		case HOUR_OF_DAY:
			this.date.setHours(date.getHours() + i);
			break;
		case HOUR:
			this.date.setHours(date.getHours() + i);
			break;
		case MINUTE:
			this.date.setMinutes(date.getMinutes() + i);
			break;
		case SECOND:
			this.date.setSeconds(date.getSeconds() + i);
			break;
		case DAY_OF_YEAR:
			this.date.setDate(date.getDate() + i);
			break;
		case YEAR:
			this.date.setYear(date.getYear() + i);
			break;
		case MILLISECOND:
			this.date.setTime(this.date.getTime() + i);
		default:
			break;
		}

	}

	/**
	 * Sets the given calendar field to the given value. The value is not
	 * interpreted by this method regardless of the leniency mode.
	 * 
	 * @param property
	 * @param i
	 */
	
	public void set(int property, int i) {
		switch (property) {
		case MONTH:
			this.date.setMonth(i);
			break;
		case DAY_OF_MONTH:
			this.date.setDate(i);
			break;
		case DAY_OF_WEEK: {
			if (i < 1 || i > 7)
				return;
			this.date.setDate(this.date.getDate() - (get(DAY_OF_WEEK) - i));
			break;
		}
		case HOUR_OF_DAY:
			this.date.setHours(i);
			break;
		case HOUR:
			this.date.setHours(i);
			break;
		case MINUTE:
			this.date.setMinutes(i);
			break;
		case SECOND:
			this.date.setSeconds(i);
			break;
		case YEAR:
			this.date.setYear(i);
			break;
		case MILLISECOND: {
			long time = this.date.getTime();
			long woutmilli = time / 1000;
			time = woutmilli * 1000;
			this.date.setTime(time + i);
			break;
		}
		default:
			break;
		}

	}

	public Date getTime() {
		return new Date(this.date.getTime());
	}

	public Calendar myClone() {
		Calendar instance = new Calendar();
		instance.setTime(new Date(this.date.getTime()));
		return instance;
	}

	public boolean isLeapYear(int year) {
		if ((year % 4 == 0) && (year % 100 != 0)) {
			return true;
		} else if (year % 400 == 0)
			return true;
		return false;
	}

	
	public int getActualMaximum(int field) {
		switch (field) {
		case DAY_OF_MONTH:
			return maxMonthDay(this.date.getMonth());
		case DAY_OF_YEAR:
			if (isLeapYear(this.date.getYear()))
				return 366;
			else
				return 365;
		default:
			break;
		}
		return 0;
	}

	
	private int maxMonthDay(int month) {
		switch (month) {
		case 0:
			return 31;
		case 1:
			if (isLeapYear(this.date.getYear()))
				return 29;
			else
				return 28;
		case 2:
			return 31;
		case 3:
			return 30;
		case 4:
			return 31;
		case 5:
			return 30;
		case 6:
			return 31;
		case 7:
			return 31;
		case 8:
			return 30;
		case 9:
			return 31;
		case 10:
			return 30;
		case 11:
			return 31;
		default:
			break;
		}
		return 0;
	}

	public boolean after(Calendar d2) {
		if (d2.getTime().getTime() < this.date.getTime())
			return true;
		return false;
	}

	/**
	 * Compares two Dates for ordering.
	 * 
	 * @param anotherDate
	 *            the <code>Date</code> to be compared.
	 * @return the value <code>0</code> if the argument date1 is equal to this
	 *         date2; a value less than <code>0</code> if this date1 is before
	 *         the date2 argument; and a value greater than <code>0</code> if
	 *         this date1 is after the date2 argument.
	 * @since 1.2
	 * @exception NullPointerException
	 *                if <code>anotherDate</code> is null.
	 */
	public static short compare(Date date1, Date date2) {
		if (date1.getTime() == date2.getTime())
			return 0;
		else if (date1.getTime() > date2.getTime())
			return 1;
		else
			return -1;
	}

	public long getTimeInMillis() {
		return this.date.getTime();
	}
}
