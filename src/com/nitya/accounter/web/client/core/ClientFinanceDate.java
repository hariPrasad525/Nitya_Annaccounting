package com.nitya.accounter.web.client.core;

import java.io.Serializable;
import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * 
 * @author kumar kasimala
 * 
 */

public class ClientFinanceDate implements Comparable<ClientFinanceDate>,
		IsSerializable, Serializable, Cloneable {

	int year, month, day;

	public ClientFinanceDate(Date date) {
		initDate(date);
	}

	private void initDate(Date date) {
		if (date != null) {
			this.year = (date.getYear() + 1900);
			this.month = (date.getMonth() + 1);
			this.day = date.getDate();

		}
	}

	public ClientFinanceDate(long date) {
		initDate(date);
	}

	public ClientFinanceDate() {
		initDate(new Date());
	}

	private void initDate(long date) {
		if (date == 0) {
			return;
		}
		String stringValue = String.valueOf(date);
		this.year = Integer.parseInt(stringValue.substring(0, 4));
		this.month = Integer.parseInt(stringValue.substring(4, 6));
		this.day = Integer.parseInt(stringValue.substring(6, 8));
	}

	public int getYear() {
		return this.year;
	}

	public int getMonth() {
		return this.month;
	}

	public long getDate() {

		return (year * 10000) + (month * 100) + day;
	}

	public Date getDateAsObject() {
		Date date = new Date(this.year - 1900, this.month - 1, this.day);
		date.setHours(00);
		date.setMinutes(00);
		date.setSeconds(00);
		return date;
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
	 * @see java.util.Calendars
	 */
	public ClientFinanceDate(int year, int month, int date) {
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
	 * @see java.util.Calendar hrs, min)</code> or
	 *      <code>GregorianCalendar(year + 1900,
	 * month, date, hrs, min)</code>.
	 */

	public ClientFinanceDate(int year, int month, int date, int hrs, int min) {
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
	 * @see java.util.Calendar F * hrs, min, sec)</code> or
	 *      <code>GregorianCalendar(year + 1900,
	 * month, date, hrs, min, sec)</code>.
	 */
	public ClientFinanceDate(int year, int month, int date, int hrs, int min,
			int sec) {
		this(new Date(year - 1900, month - 1, date, hrs, min, sec));
	}

	public ClientFinanceDate(String text) {
		if (text != null) {
			String[] ymd = text.contains("/") ? text.split("/") : text
					.contains("-") ? text.split("-") : text.split(" ");
			if (ymd[0].length() > 2) {
				initDate(Long.parseLong(ymd[0] + ymd[1] + ymd[2]));
			} else if (ymd[0].length() <= 2) {
				initDate(Long.parseLong(ymd[2] + ymd[1] + ymd[0]));
			}
		}
	}

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
	public boolean before(ClientFinanceDate when) {
		if (when == null) {
			return false;
		}
		return this.getDate() < when.getDate();
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
	public boolean after(ClientFinanceDate when) {
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
	public int compareTo(ClientFinanceDate anotherDate) {
		return (this.getDate() < anotherDate.getDate() ? -1
				: (this.getDate() == anotherDate.getDate() ? 0 : 1));

	}

	public int hashCode() {
		return (int) this.getDate();
	}

	public String toString() {
		return (day < 10 ? "0" + this.day : this.day) + "/"
				+ (this.month < 10 ? "0" + this.month : this.month) + "/"
				+ this.year;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public void setDay(int date) {
		this.day = date;
	}

	private void synchornizeDate(Date dt) {
		this.year = dt.getYear() + 1900;
		this.month = dt.getMonth() + 1;
		this.day = dt.getDate();
	}

	public int getDay() {
		return day;
	}

	public void setYear(int year) {
		this.year = year;
	}

	/**
	 * @return
	 */
	public boolean isEmpty() {
		return year == 0 && month == 0 && day == 0;
	}

	public ClientFinanceDate clone() {
		ClientFinanceDate financeDate = this.clone();
		return financeDate;
	}

	public int getDayOfWeek() {
		// TODO Auto-generated method stub
		return 0;
	}

	public static ClientFinanceDate emptyDate() {
		ClientFinanceDate date = new ClientFinanceDate();
		date.setDay(0);
		date.setMonth(0);
		date.setYear(0);
		return date;
	}

	public static boolean compareMonthAndYear(ClientFinanceDate transactionDate) {
		ClientFinanceDate presentDate = new ClientFinanceDate();
		if (transactionDate.getYear() == presentDate.getYear()
				&& transactionDate.getMonth() == presentDate.getMonth()) {
			return true;
		}
		return false;
	}

}
