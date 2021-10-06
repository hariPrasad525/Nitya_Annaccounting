package com.vimukti.accounter.core;

import java.util.Calendar;
import java.util.Date;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;

public class FinanceDate {
	/**
	 * The number of days in a 400 year cycle.
	 */
	private static final int DAYS_PER_CYCLE = 146097;
	/**
	 * The number of days from year zero to year 1970. There are five 400 year
	 * cycles from year zero to 2000. There are 7 leap years from 1970 to 2000.
	 */
	static final long DAYS_0000_TO_1970 = (DAYS_PER_CYCLE * 5L) - (30L * 365L + 7L);

	int year, month, day;
	private int dayOfWeek;

	public FinanceDate(long date) {
		initDate(date);
	}

	// public long getStringAsLong(String stringdate) {
	// String string[] = stringdate.split("-");
	// this.year = string[0];
	// this.month = string[1];
	// this.day = string[2];
	//
	// return Long.parseLong(this.year + this.month + this.day);
	//
	// }

	public FinanceDate(String stringdate) {
		this(new Date(stringdate));
	}

	public FinanceDate() {
		initDate(new Date());
	}

	public void initDate(long date) {
		String stringValue = String.valueOf(date);
		this.year = Integer.parseInt(stringValue.substring(0, 4));
		this.month = Integer.parseInt(stringValue.substring(4, 6));
		this.day = Integer.parseInt(stringValue.substring(6, 8));
	}

	public FinanceDate(Date date) {
		initDate(date);
	}

	public void initDate(Date date) {
		this.year = date.getYear() + 1900;
		this.month = date.getMonth() + 1;
		this.day = date.getDate();

		// this.month = this.month.length() == 1 ? "0" + this.month
		// : this.month;
		// this.day = this.day.length() == 1 ? "0" + this.day : this.day;

	}

	public int getYear() {
		return this.year - 1900;
	}

	public void setYear(int year) {
		this.year = year + 1900;
	}

	public int getMonth() {
		return this.month - 1;
	}

	public void setMonth(int month) {
		this.month = month;
		Date dt = new Date(this.year - 1900, 0, this.day);
		dt.setMonth(this.month);

		synchornizeDate(dt);
	}

	public int getDay() {
		return getAsDateObject().getDay();
	}

	public void setDate(int day) {
		this.day = day;

		Date dt = new Date(this.year - 1900, this.month, 0);
		dt.setDate(this.day);

		synchornizeDate(dt);
	}

	public long toEpochDay() {
		long y = year;
		long m = month;
		long total = 0;
		total += 365 * y;
		if (y >= 0) {
			total += (y + 3) / 4 - (y + 99) / 100 + (y + 399) / 400;
		} else {
			total -= y / -4 - y / -100 + y / -400;
		}
		total += ((367 * m - 362) / 12);
		total += day - 1;
		if (m > 2) {
			total--;
			if (isLeapYear(year) == false) {
				total--;
			}
		}
		return total - DAYS_0000_TO_1970;
	}

	public boolean isLeapYear(long prolepticYear) {
		return ((prolepticYear & 3) == 0) && ((prolepticYear % 100) != 0 || (prolepticYear % 400) == 0);
	}

	private void synchornizeDate(Date dt) {
		this.year = dt.getYear() + 1900;
		this.month = dt.getMonth() + 1;
		this.day = dt.getDate();

	}

	public Date getAsDateObject() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(this.year, this.month - 1, this.day);
		Date financeDate = calendar.getTime();
		financeDate.setHours(00);
		financeDate.setMinutes(00);
		financeDate.setSeconds(00);
		return financeDate;

	}

	/**
	 * Allocates a <code>Date</code> object and initializes it so that it
	 * represents midnight, local time, at the beginning of the day specified by
	 * the <code>year</code>, <code>month</code>, and <code>date</code>
	 * arguments.
	 * 
	 * @param year
	 *            the year minus 1900.
	 * @param month
	 *            the month between 0-11.
	 * @param date
	 *            the day of the month between 1-31.
	 * @see java.util.Calendar
	 */
	public FinanceDate(int year, int month, int date) {
		this(year, month, date, 0, 0, 0);
	}

	/**
	 * Allocates a <code>Date</code> object and initializes it so that it
	 * represents the instant at the start of the minute specified by the
	 * <code>year</code>, <code>month</code>, <code>date</code>,
	 * <code>hrs</code>, and <code>min</code> arguments, in the local time zone.
	 * 
	 * @param year
	 *            the year minus 1900.
	 * @param month
	 *            the month between 0-11.
	 * @param date
	 *            the day of the month between 1-31.
	 * @param hrs
	 *            the hours between 0-23.
	 * @param min
	 *            the minutes between 0-59.
	 * @see java.util.Calendar
	 */
	public FinanceDate(int year, int month, int date, int hrs, int min) {
		this(year, month, date, hrs, min, 0);
	}

	/**
	 * Allocates a <code>Date</code> object and initializes it so that it
	 * represents the instant at the start of the second specified by the
	 * <code>year</code>, <code>month</code>, <code>date</code>,
	 * <code>hrs</code>, <code>min</code>, and <code>sec</code> arguments, in
	 * the local time zone.
	 * 
	 * @param year
	 *            the year minus 1900.
	 * @param month
	 *            the month between 0-11.
	 * @param date
	 *            the day of the month between 1-31.
	 * @param hrs
	 *            the hours between 0-23.
	 * @param min
	 *            the minutes between 0-59.
	 * @param sec
	 *            the seconds between 0-59.
	 */
	public FinanceDate(int year, int month, int date, int hrs, int min, int sec) {
		this(new Date(year, month, date, hrs, min, sec));
	}

	/**
	 * Allocates a <code>Date</code> object and initializes it so that it
	 * represents the date and time indicated by the string <code>s</code>,
	 * which is interpreted as if by the {@link Date#parse} method.
	 * 
	 * @param s
	 *            a string representation of the date.
	 * @see java.text.DateFormat
	 * @see java.util.Date#parse(java.lang.String)
	 */
	// public FinanceDate(String s) {
	// this(parse(s));
	// }
	//
	// private static long parse(String s) {
	// return 0;
	// }

	/**
	 * Creates new Instance
	 */
	public FinanceDate(ClientFinanceDate clientDate) {
		this.year = clientDate.getYear();
		this.month = clientDate.getMonth();
		this.day = clientDate.getDay();
	}

	/**
	 * Determines the date and time based on the arguments. The arguments are
	 * interpreted as a year, month, day of the month, hour of the day, minute
	 * within the hour, and second within the minute, exactly as for the
	 * <tt>Date</tt> constructor with six arguments, except that the arguments
	 * are interpreted relative to UTC rather than to the local time zone. The
	 * time indicated is returned represented as the distance, measured in
	 * milliseconds, of that time from the epoch (00:00:00 GMT on January 1,
	 * 1970).
	 * 
	 * @param year
	 *            the year minus 1900.
	 * @param month
	 *            the month between 0-11.
	 * @param date
	 *            the day of the month between 1-31.
	 * @param hrs
	 *            the hours between 0-23.
	 * @param min
	 *            the minutes between 0-59.
	 * @param sec
	 *            the seconds between 0-59.
	 * @return the number of milliseconds since January 1, 1970, 00:00:00 GMT
	 *         for the date and time specified by the arguments.
	 * @see java.util.Calendar
	 */
	public static long UTC(int year, int month, int date, int hrs, int min, int sec) {

		return 0;
	}

	/**
	 * Sets the day of the month of this <tt>Date</tt> object to the specified
	 * value. This <tt>Date</tt> object is modified so that it represents a
	 * point in time within the specified day of the month, with the year,
	 * month, hour, minute, and second the same as before, as interpreted in the
	 * local time zone. If the date was April 30, for example, and the date is
	 * set to 31, then it will be treated as if it were on May 1, because April
	 * has only 30 days.
	 * 
	 * @param date
	 *            the day of the month value between 1-31.
	 * @see java.util.Calendar
	 * @deprecated As of JDK version 1.1, replaced by
	 *             <code>Calendar.set(Calendar.DAY_OF_MONTH, int date)</code>.
	 */
	// @Deprecated
	// public void setDate(int date) {
	// getCalendarDate().setDayOfMonth(date);
	// }

	/**
	 * Returns the day of the week represented by this date. The returned value
	 * (<tt>0</tt> = Sunday, <tt>1</tt> = Monday, <tt>2</tt> = Tuesday,
	 * <tt>3</tt> = Wednesday, <tt>4</tt> = Thursday, <tt>5</tt> = Friday,
	 * <tt>6</tt> = Saturday) represents the day of the week that contains or
	 * begins with the instant in time represented by this <tt>Date</tt> object,
	 * as interpreted in the local time zone.
	 * 
	 * @return the day of the week represented by this date.
	 * @see java.util.Calendar
	 * @deprecated As of JDK version 1.1, replaced by
	 *             <code>Calendar.get(Calendar.DAY_OF_WEEK)</code>.
	 */
	// @Deprecated
	// public int getDay() {
	// return normalize().getDayOfWeek() - gcal.SUNDAY;
	// }

	/**
	 * Returns the hour represented by this <tt>Date</tt> object. The returned
	 * value is a number (<tt>0</tt> through <tt>23</tt>) representing the hour
	 * within the day that contains or begins with the instant in time
	 * represented by this <tt>Date</tt> object, as interpreted in the local
	 * time zone.
	 * 
	 * @return the hour represented by this date.
	 * @see java.util.Calendar
	 * @deprecated As of JDK version 1.1, replaced by
	 *             <code>Calendar.get(Calendar.HOUR_OF_DAY)</code>.
	 */

	/**
	 * Sets the hour of this <tt>Date</tt> object to the specified value. This
	 * <tt>Date</tt> object is modified so that it represents a point in time
	 * within the specified hour of the day, with the year, month, date, minute,
	 * and second the same as before, as interpreted in the local time zone.
	 * 
	 * @param hours
	 *            the hour value.
	 * @see java.util.Calendar
	 * @deprecated As of JDK version 1.1, replaced by
	 *             <code>Calendar.set(Calendar.HOUR_OF_DAY, int hours)</code>.
	 */

	/**
	 * Returns the number of minutes past the hour represented by this date, as
	 * interpreted in the local time zone. The value returned is between
	 * <code>0</code> and <code>59</code>.
	 * 
	 * @return the number of minutes past the hour represented by this date.
	 * @see java.util.Calendar
	 * @deprecated As of JDK version 1.1, replaced by
	 *             <code>Calendar.get(Calendar.MINUTE)</code>.
	 */

	/**
	 * Sets the minutes of this <tt>Date</tt> object to the specified value.
	 * This <tt>Date</tt> object is modified so that it represents a point in
	 * time within the specified minute of the hour, with the year, month, date,
	 * hour, and second the same as before, as interpreted in the local time
	 * zone.
	 * 
	 * @param minutes
	 *            the value of the minutes.
	 * @see java.util.Calendar
	 * @deprecated As of JDK version 1.1, replaced by
	 *             <code>Calendar.set(Calendar.MINUTE, int minutes)</code>.
	 */

	/**
	 * Returns the number of seconds past the minute represented by this date.
	 * The value returned is between <code>0</code> and <code>61</code>. The
	 * values <code>60</code> and <code>61</code> can only occur on those Java
	 * Virtual Machines that take leap seconds into account.
	 * 
	 * @return the number of seconds past the minute represented by this date.
	 * @see java.util.Calendar
	 * @deprecated As of JDK version 1.1, replaced by
	 *             <code>Calendar.get(Calendar.SECOND)</code>.
	 */

	/**
	 * Sets this <code>Date</code> object to represent a point in time that is
	 * <code>time</code> milliseconds after January 1, 1970 00:00:00 GMT.
	 * 
	 * @param time
	 *            the number of milliseconds.
	 */

	/**
	 * Tests if this date is before the specified date.
	 * 
	 * @param when
	 *            a date.
	 * @return <code>true</code> if and only if the instant of time represented
	 *         by this <tt>Date</tt> object is strictly earlier than the instant
	 *         represented by <tt>when</tt>; <code>false</code> otherwise.
	 * @exception NullPointerException
	 *                if <code>when</code> is null.
	 */
	public boolean before(FinanceDate when) {
		return getDate() < when.getDate();
	}

	/**
	 * Tests if this date is after the specified date.
	 * 
	 * @param when
	 *            a date.
	 * @return <code>true</code> if and only if the instant represented by this
	 *         <tt>Date</tt> object is strictly later than the instant
	 *         represented by <tt>when</tt>; <code>false</code> otherwise.
	 * @exception NullPointerException
	 *                if <code>when</code> is null.
	 */
	public boolean after(FinanceDate when) {
		return this.getDate() > when.getDate();
	}

	/**
	 * Compares two dates for equality. The result is <code>true</code> if and
	 * only if the argument is not <code>null</code> and is a <code>Date</code>
	 * object that represents the same point in time, to the millisecond, as
	 * this object.
	 * <p>
	 * Thus, two <code>Date</code> objects are equal if and only if the
	 * <code>getTime</code> method returns the same <code>long</code> value for
	 * both.
	 * 
	 * @param obj
	 *            the object to compare with.
	 * @return <code>true</code> if the objects are the same; <code>false</code>
	 *         otherwise.
	 * @see java.util.Date#getTime()
	 */
	public boolean equals(Object obj) {
		return this.hashCode() == obj.hashCode();
	}

	/**
	 * Compares two Dates for ordering.
	 * 
	 * @param anotherDate
	 *            the <code>Date</code> to be compared.
	 * @return the value <code>0</code> if the argument Date is equal to this
	 *         Date; a value less than <code>0</code> if this Date is before the
	 *         Date argument; and a value greater than <code>0</code> if this
	 *         Date is after the Date argument.
	 * @since 1.2
	 * @exception NullPointerException
	 *                if <code>anotherDate</code> is null.
	 */
	public int compareTo(FinanceDate anotherDate) {
		return (this.getDate() < anotherDate.getDate() ? -1 : (this.getDate() == anotherDate.getDate() ? 0 : 1));

	}

	public int hashCode() {
		return (int) this.getDate();
	}

	public String toString() {
		return this.day + "/" + this.month + "/" + this.year;
	}

	public String toString(String seperator) {
		return this.day + seperator + this.month + seperator + this.year;
	}

	public void set(FinanceDate time) {
		this.year = time.year;
		this.month = time.month;
		this.day = time.day;

	}

	public long getDate() {
		return (year * 10000) + (month * 100) + day;
	}

	public void initDate(int year, int month, int day) {
		this.year = year;
		this.month = month;
		this.day = day;
	}

	public final void clear() {
		this.year = 0;
		this.month = 0;
		this.day = 0;
	}

	public ClientFinanceDate toClientFinanceDate() {
		ClientFinanceDate clientFinanceDate = new ClientFinanceDate();
		clientFinanceDate.setYear(year);
		clientFinanceDate.setMonth(month);
		clientFinanceDate.setDay(day);
		return clientFinanceDate;
	}

	/**
	 * @param serverTime
	 * @param clientTzOffset
	 *            in minutes
	 * @return
	 */
	public static Date clientTimeAtServer(Date serverTime, int clientTzOffset) {
		// the formula is ClientTimeAtServer = ServerTime + ClientTimezoneOffset
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(serverTime);
		calendar.add(Calendar.MINUTE, clientTzOffset);
		return calendar.getTime();
	}

	/**
	 * @return the dayOfWeek
	 */
	public int getDayOfWeek() {
		return dayOfWeek;
	}

	/**
	 * @param dayOfWeek
	 *            the dayOfWeek to set
	 */
	public void setDayOfWeek(int dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}
}
