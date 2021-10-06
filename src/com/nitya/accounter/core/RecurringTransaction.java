package com.nitya.accounter.core;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.CallbackException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.dialect.EncryptedStringType;
import org.json.JSONException;

import com.nitya.accounter.utils.HibernateUtil;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.externalization.AccounterMessages;
import com.nitya.accounter.web.server.FinanceTool;

/**
 * Contains a referring transaction and has information for scheduling
 * procedure. <br>
 * <br>
 * 
 * <b>Note:</b> before updating this instance in database make sure the
 * {@link #stopped} status. This recurring transaction only stops at
 * {@link #scheduleAgain()}.
 * 
 * @author nitya3
 * 
 */
public class RecurringTransaction extends CreatableObject implements
		IAccounterServerCore, INamedObject {

	private static final long serialVersionUID = 1L;
	private static final int DAYS_PER_WEEK = 7;

	/*-------------------------
	 * Recurring types
	 *---------------------------*/
	public final static int RECURRING_SCHEDULED = 0;
	public final static int RECURRING_REMINDER = 1;
	public final static int RECURRING_UNSCHEDULED = 2;

	/*-----------------------
	 * Interval types
	 *-------------------------*/
	public final static int INTERVAL_TYPE_DAILY = 0;
	public final static int INTERVAL_TYPE_WEEKLY = 1;
	/**
	 * this option is for selecting a day in month.
	 */
	public final static int INTERVAL_TYPE_MONTHLY_DAY = 2;
	/**
	 * this option is for selecting a week in month, like first, second, third,
	 * fourth, and last.
	 */
	public final static int INTERVAL_TYPE_MONTHLY_WEEK = 3;
	public final static int INTERVAL_TYPE_YEARLY = 4;

	/*----------------
	 * due date options
	 *---------------*/
	public final static int DUE_DATE_DAYS_AFTER_THE_INVOICE = 0;
	public final static int DUE_DATE_OF_THE_FOLLOWING_MONTH = 1;
	/**
	 * Not applicable for {@link #INTERVAL_TYPE_DAILY}/
	 * {@link #INTERVAL_TYPE_WEEKLY} selection.
	 */
	public final static int DUE_DATE_OF_THE_CURRENT_MONTH = 2;

	/*------------------
	 * End date type optons
	 *----------------------*/
	public final static int END_DATE_NONE = 0;
	public final static int END_DATE_OCCURRENCES = 1;
	public final static int END_DATE_DATE = 2;

	/*-------------
	 * Action options
	 *-------------*/
	public final static int ACTION_SAVE_DRAFT = 0;
	public final static int ACTION_APPROVE = 1;
	public final static int ACTION_APPROVE_SEND = 2;

	/*-------------
	 * STATUS
	 *-------------*/

	public static final int STATUS_SUCCESS = 1;
	public static final int STATUS_FAILED = 2;

	private FinanceDate startDate;
	private FinanceDate endDate; // Optional

	/*
	 * If user selects stop after these many occurrences.
	 */
	private int occurencesCount;
	private int occurencesCompleted;
	private int endDateType;

	private int dueDateValue;
	private int dueDateType;

	private FinanceDate nextScheduleOn;

	/**
	 * this is the value which represents n in "every n days/weeks/months"
	 */
	private int intervalPeriod;

	private int intervalType;
	// If weekly interval selected, we need selected week
	private int weekDay;

	// If monthly interval selected, we need dayOfMonth or weekOfMonth
	private int dayOfMonth;
	private int weekOfMonth;

	// If yearly interval selected, we need selected month
	private int month;

	/**
	 * Include unbilled changes when these transactions[like invoices] are
	 * updated.
	 */
	private boolean unbilledChargesEnabled;

	/**
	 * Is it necessary to alert the user when recurring occurrences completed or
	 * crossed the end date.
	 */
	private boolean alertWhenEnded;

	/**
	 * To store whether need to notify about automatically created transactions
	 * or not if recurring type is schedule.
	 */
	private boolean notifyCreatedTransaction;

	/**
	 * create the instance of this recurring template in advance to specified
	 * days. <br>
	 */
	private int daysInAdvanceToCreate;

	/**
	 * Need to remind the user these many days before when recurring type is
	 * reminder.
	 */
	private int daysBeforeToRemind;
	/**
	 * type of recurring. Schedule, Remainder, or NoShcedule[just template]
	 */
	private int type;

	/**
	 * previous schedule date
	 */
	private FinanceDate prevScheduleOn;

	/**
	 * {@link #ACTION_SAVE_DRAFT}, {@link #ACTION_APPROVE},
	 * {@link #ACTION_APPROVE_SEND}
	 */
	private int actionType;

	private String name;

	private Transaction transaction;

	private Set<Reminder> reminders = new HashSet<Reminder>();

	/**
	 * This field indicates this transaction status. if this field is false, it
	 * won't create next schedule.
	 */
	private boolean stopped;

	private int status;

	public RecurringTransaction() {

	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();

		if (!UserUtils.canDoThis(RecurringTransaction.class)) {
			throw new AccounterException(
					AccounterException.ERROR_DONT_HAVE_PERMISSION);
		}

		RecurringTransaction data = (RecurringTransaction) clientObject;
		Query query = session.getNamedQuery("getRecurringTransaction.by.Name")
				.setEntity("company", data.getCompany())
				.setLong("id", data.getID())
				.setParameter("name", data.name, EncryptedStringType.INSTANCE);
		List list = query.list();
		if (list != null && !list.isEmpty()) {
			throw new AccounterException(AccounterException.ERROR_NAME_CONFLICT);
		}

		return true;
	}

	public int getActionType() {
		return actionType;
	}

	public int getDayOfMonth() {
		return dayOfMonth;
	}

	public int getDaysInAdvanceToCreate() {
		return daysInAdvanceToCreate;
	}

	public int getDueDateType() {
		return dueDateType;
	}

	public int getDueDateValue() {
		return dueDateValue;
	}

	public FinanceDate getEndDate() {
		return endDate;
	}

	private FinanceDate getInitialDateForNextSchedule() {
		return nextScheduleOn == null ? startDate : nextScheduleOn;
	}

	private FinanceDate getInitialDateForPreviousSchedule() {
		return prevScheduleOn == null ? startDate : prevScheduleOn;
	}

	public int getIntervalPeriod() {
		return intervalPeriod;
	}

	public int getIntervalType() {
		return intervalType;
	}

	public int getMonth() {
		return month;
	}

	public String getName() {
		return name;
	}

	/**
	 * includes daysInAdvance
	 * 
	 * @return
	 */
	public FinanceDate getNextScheduleOn() {
		return nextScheduleOn;
	}

	public int getOccurencesCompleted() {
		return occurencesCompleted;
	}

	public int getOccurencesCount() {
		return occurencesCount;
	}

	/**
	 * Gives schedule iterator by {@link #intervalType} <br>
	 * This is a factory method to create ScheduleIterator.
	 * 
	 * @return
	 */
	private ScheduleIterator createScheduleIterator() {
		switch (intervalType) {
		case INTERVAL_TYPE_DAILY:
			return new DayScheduler();
		case INTERVAL_TYPE_WEEKLY:
			return new WeekScheduler();
		case INTERVAL_TYPE_MONTHLY_DAY:
			return new MonthSchedulerForDay();
		case INTERVAL_TYPE_MONTHLY_WEEK:
			return new MonthSchedulerForWeek();
		case INTERVAL_TYPE_YEARLY:
			return new YearlyScheduler();
		default:
			break;
		}
		return null;
	}

	public FinanceDate getStartDate() {
		return startDate;
	}

	public int getType() {
		return type;
	}

	public int getWeekDay() {
		return weekDay;
	}

	public int getWeekOfMonth() {
		return weekOfMonth;
	}

	public boolean isUnbilledChargesEnabled() {
		return unbilledChargesEnabled;
	}

	private boolean isValidScheduleTime(FinanceDate date) {
		if (endDateType == END_DATE_NONE) {
			return true;
		}

		if (endDateType == END_DATE_OCCURRENCES) {
			return occurencesCompleted < occurencesCount;
		}

		if (endDateType == END_DATE_DATE) {
			return date.before(endDate) || date.equals(endDate);
		}

		return false;
	}

	/**
	 * <b>null</b>, if this recurring transaction {@link #stopped}
	 * 
	 * @return
	 */
	private Date next() {
		return isStopped() ? null : createScheduleIterator().next();
	}

	@Override
	public boolean onDelete(Session session) throws CallbackException {

		return false;
	}

	public void setActionType(int actionType) {
		this.actionType = actionType;
	}

	public void setDayOfMonth(int dayOfMonth) {
		this.dayOfMonth = dayOfMonth;
	}

	public void setDaysInAdvanceToCreate(int daysInAdvanceToCreate) {
		this.daysInAdvanceToCreate = daysInAdvanceToCreate;
	}

	public void setDueDateType(int dueDateType) {
		this.dueDateType = dueDateType;
	}

	public void setDueDateValue(int dueDateValue) {
		this.dueDateValue = dueDateValue;
	}

	public void setEndDate(FinanceDate endDate) {
		this.endDate = endDate;
	}

	public void setIntervalPeriod(int intervalPeriod) {
		this.intervalPeriod = intervalPeriod;
	}

	public void setIntervalType(int intervalType) {
		this.intervalType = intervalType;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNextScheduleOn(FinanceDate nextScheduleOn) {
		this.nextScheduleOn = nextScheduleOn;
	}

	public void setOccurencesCompleted(int occurencesCompleted) {
		this.occurencesCompleted = occurencesCompleted;
	}

	public void setOccurencesCount(int occurencesCount) {
		this.occurencesCount = occurencesCount;
	}

	public void setStartDate(FinanceDate startDate) {
		this.startDate = startDate;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setUnbilledChargesEnabled(boolean unbilledChangesEnabled) {
		this.unbilledChargesEnabled = unbilledChangesEnabled;
	}

	public void setWeekDay(int weekDay) {
		this.weekDay = weekDay;
	}

	public void setWeekOfMonth(int weekOfMonth) {
		this.weekOfMonth = weekOfMonth;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(name).append(Global.get().messages().every());
		sb.append(intervalPeriod).append(' ');
		switch (intervalType) {
		case INTERVAL_TYPE_DAILY:
			sb.append(Global.get().messages().days());
			break;
		case INTERVAL_TYPE_WEEKLY:
			sb.append(Global.get().messages().weeks());
			break;
		case INTERVAL_TYPE_MONTHLY_DAY:
		case INTERVAL_TYPE_MONTHLY_WEEK:
			sb.append(Global.get().messages().months());
			break;
		case INTERVAL_TYPE_YEARLY:
			sb.append(Global.get().messages().years());
		default:
			sb.append("-");
			break;
		}
		return sb.toString();
	}

	public void setPrevScheduleOn(FinanceDate prevScheduleOn) {
		this.prevScheduleOn = prevScheduleOn;
	}

	public FinanceDate getPrevScheduleOn() {
		return prevScheduleOn;
	}

	public void setEndDateType(int endDateType) {
		this.endDateType = endDateType;
	}

	public int getEndDateType() {
		return endDateType;
	}

	/**
	 * Daily scheduler.
	 * 
	 * @author nitya3
	 * 
	 */
	private class DayScheduler implements ScheduleIterator {

		@Override
		public Date next() {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(getInitialDateForNextSchedule().getAsDateObject());
			// This is condition is placed because for the first time no need to
			// add interval period.
			if (isScheduled()) {
				calendar.add(Calendar.DATE, intervalPeriod);
			} else {
				// add the daysInAdvanceToCreate days from actual date.
				// calendar.add(Calendar.DAY_OF_MONTH, daysInAdvanceToCreate);
			}
			return isValidScheduleTime(new FinanceDate(calendar.getTime())) ? calendar
					.getTime() : null;
		}

		@Override
		public Date previous() {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(getInitialDateForPreviousSchedule()
					.getAsDateObject());
			// This is condition is placed because for the first time no need to
			// add interval period.
			if (isScheduled()) {
				calendar.add(Calendar.DATE, -intervalPeriod);
			} else {
				// add the daysInAdvanceToCreate days from actual date.
				// calendar.add(Calendar.DAY_OF_MONTH, daysInAdvanceToCreate);
			}
			return isValidScheduleTime(new FinanceDate(calendar.getTime())) ? calendar
					.getTime() : null;
		}
	}

	/**
	 * Month scheduler for a specific day of the month.
	 * 
	 * @author nitya3
	 * 
	 */
	private class MonthSchedulerForDay implements ScheduleIterator {

		@Override
		public Date next() {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(getInitialDateForNextSchedule().getAsDateObject());
			int lastDayOfMonth = dayOfMonth;
			if (lastDayOfMonth == -1) {
				lastDayOfMonth = calendar
						.getActualMaximum(Calendar.DAY_OF_MONTH);
			}
			while (calendar.get(Calendar.DAY_OF_MONTH) != lastDayOfMonth) {
				calendar.add(Calendar.DAY_OF_MONTH, 1);
			}

			if (isScheduled()) {
				calendar.add(Calendar.MONTH, intervalPeriod);
			} else {
				// not scheduled yet, so we need to reduce the
				// daysInAdvanced from the actual date
				// calendar.add(Calendar.DAY_OF_MONTH, daysInAdvanceToCreate);
			}

			return isValidScheduleTime(new FinanceDate(calendar.getTime())) ? calendar
					.getTime() : null;
		}

		@Override
		public Date previous() {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(getInitialDateForPreviousSchedule()
					.getAsDateObject());
			int lastDayOfMonth = dayOfMonth;
			if (lastDayOfMonth == -1) {
				lastDayOfMonth = calendar
						.getActualMaximum(Calendar.DAY_OF_MONTH);
			}
			while (calendar.get(Calendar.DAY_OF_MONTH) != lastDayOfMonth) {
				calendar.add(Calendar.DAY_OF_MONTH, -1);
			}

			if (isScheduled()) {
				calendar.add(Calendar.MONTH, -intervalPeriod);
			} else {
				// not scheduled yet, so we need to reduce the
				// daysInAdvanced from the actual date
				// calendar.add(Calendar.DAY_OF_MONTH, daysInAdvanceToCreate);
			}

			return isValidScheduleTime(new FinanceDate(calendar.getTime())) ? calendar
					.getTime() : null;
		}

	}

	/**
	 * for every month on n-th week.
	 * 
	 * @author nitya3
	 * 
	 */
	private class MonthSchedulerForWeek implements ScheduleIterator {

		@Override
		public Date next() {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(getInitialDateForNextSchedule().getAsDateObject());

			while (calendar.get(Calendar.DAY_OF_WEEK) != weekDay) {
				calendar.add(Calendar.DAY_OF_MONTH, 1);
			}

			while (calendar.get(Calendar.WEEK_OF_MONTH) != weekOfMonth) {
				calendar.add(Calendar.DAY_OF_MONTH, DAYS_PER_WEEK);
			}

			if (isScheduled()) {
				calendar.set(Calendar.DATE, 1);
				calendar.add(Calendar.MONTH, intervalPeriod);

				int month;

				do {
					month = calendar.get(Calendar.MONTH);
					while (calendar.get(Calendar.DAY_OF_WEEK) != weekDay) {
						calendar.add(Calendar.DAY_OF_MONTH, 1);
					}

					while (calendar.get(Calendar.WEEK_OF_MONTH) != weekOfMonth) {
						calendar.add(Calendar.DAY_OF_MONTH, DAYS_PER_WEEK);
						if (calendar.get(Calendar.MONTH) != month)
							break; // month has been changed.
					}
				} while (calendar.get(Calendar.MONTH) != month);
			}

			if (!isScheduled()) {
				// not yet scheduled, reduce the daysInAdvance from the actual
				// date.
				// calendar.add(Calendar.DAY_OF_MONTH, daysInAdvanceToCreate);
			}

			return isValidScheduleTime(new FinanceDate(calendar.getTime())) ? calendar
					.getTime() : null;
		}

		@Override
		public Date previous() {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(getInitialDateForPreviousSchedule()
					.getAsDateObject());

			while (calendar.get(Calendar.DAY_OF_WEEK) != weekDay) {
				calendar.add(Calendar.DAY_OF_MONTH, -1);
			}

			while (calendar.get(Calendar.WEEK_OF_MONTH) != weekOfMonth) {
				calendar.add(Calendar.DAY_OF_MONTH, -DAYS_PER_WEEK);
			}

			if (isScheduled()) {
				calendar.set(Calendar.DATE, 1);
				calendar.add(Calendar.MONTH, -intervalPeriod);

				int month;

				do {
					month = calendar.get(Calendar.MONTH);
					while (calendar.get(Calendar.DAY_OF_WEEK) != weekDay) {
						calendar.add(Calendar.DAY_OF_MONTH, -1);
					}

					while (calendar.get(Calendar.WEEK_OF_MONTH) != weekOfMonth) {
						calendar.add(Calendar.DAY_OF_MONTH, -DAYS_PER_WEEK);
						if (calendar.get(Calendar.MONTH) != month)
							break; // month has been changed.
					}
				} while (calendar.get(Calendar.MONTH) != month);
			}

			if (!isScheduled()) {
				// not yet scheduled, reduce the daysInAdvance from the actual
				// date.
				// calendar.add(Calendar.DAY_OF_MONTH, daysInAdvanceToCreate);
			}

			return isValidScheduleTime(new FinanceDate(calendar.getTime())) ? calendar
					.getTime() : null;
		}

	}

	/**
	 * yearly scheduler, every year on particular day of a month.
	 * 
	 * @author nitya3
	 * 
	 */
	private class YearlyScheduler implements ScheduleIterator {

		@Override
		public Date next() {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(getInitialDateForNextSchedule().getAsDateObject());
			int lastDayOfMonth = dayOfMonth;
			if (lastDayOfMonth == -1) {
				lastDayOfMonth = calendar
						.getActualMaximum(Calendar.DAY_OF_MONTH);
			}
			while (calendar.get(Calendar.DAY_OF_MONTH) != lastDayOfMonth) {
				calendar.add(Calendar.DAY_OF_MONTH, 1);
			}

			while (calendar.get(Calendar.MONTH) != month) {
				calendar.add(Calendar.MONTH, 1);
			}

			if (isScheduled()) {
				calendar.add(Calendar.YEAR, intervalPeriod);
			} else {
				// not yet scheduled, reduce the daysInAdvance from actual date
				// calendar.add(Calendar.DAY_OF_MONTH, daysInAdvanceToCreate);
			}

			return isValidScheduleTime(new FinanceDate(calendar.getTime())) ? calendar
					.getTime() : null;
		}

		@Override
		public Date previous() {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(getInitialDateForPreviousSchedule()
					.getAsDateObject());
			int lastDayOfMonth = dayOfMonth;
			if (lastDayOfMonth == -1) {
				lastDayOfMonth = calendar
						.getActualMaximum(Calendar.DAY_OF_MONTH);
			}
			while (calendar.get(Calendar.DAY_OF_MONTH) != lastDayOfMonth) {
				calendar.add(Calendar.DAY_OF_MONTH, -1);
			}

			while (calendar.get(Calendar.MONTH) != month) {
				calendar.add(Calendar.MONTH, -1);
			}

			if (isScheduled()) {
				calendar.add(Calendar.YEAR, -intervalPeriod);
			} else {
				// not yet scheduled, reduce the daysInAdvance from actual date
				// calendar.add(Calendar.DAY_OF_MONTH, daysInAdvanceToCreate);
			}

			return isValidScheduleTime(new FinanceDate(calendar.getTime())) ? calendar
					.getTime() : null;
		}
	}

	/**
	 * This is for scheduling.
	 * 
	 * @author nitya3
	 * 
	 */
	private interface ScheduleIterator {

		/**
		 * Gives next scheduling date/time.
		 * 
		 * @return date for next schedule. <b>null</b> if there is no next
		 *         schedule.
		 */
		Date next();

		/**
		 * Gives previous scheduling date/time.
		 * 
		 * @return date for previous schedule. <b>null</b> if there is no
		 *         previous schedule.
		 */
		Date previous();
	}

	/**
	 * Week scheduler.
	 * 
	 * @author nitya3
	 * 
	 */
	private class WeekScheduler implements ScheduleIterator {

		@Override
		public Date next() {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(getInitialDateForNextSchedule().getAsDateObject());

			while (calendar.get(Calendar.DAY_OF_WEEK) != weekDay) {
				calendar.add(Calendar.DAY_OF_MONTH, 1);
			}

			if (isScheduled()) {
				// 1week = 7 days.
				calendar.add(Calendar.DATE, intervalPeriod * DAYS_PER_WEEK);
			} else {
				// not yet scheduled, reduce the daysInAdvance from the actual
				// date
				// calendar.add(Calendar.DAY_OF_MONTH, daysInAdvanceToCreate);
			}
			return isValidScheduleTime(new FinanceDate(calendar.getTime())) ? calendar
					.getTime() : null;
		}

		@Override
		public Date previous() {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(getInitialDateForPreviousSchedule()
					.getAsDateObject());

			while (calendar.get(Calendar.DAY_OF_WEEK) != weekDay) {
				calendar.add(Calendar.DAY_OF_MONTH, -1);
			}

			if (isScheduled()) {
				// 1week = 7 days.
				calendar.add(Calendar.DATE, -intervalPeriod * DAYS_PER_WEEK);
			} else {
				// not yet scheduled, reduce the daysInAdvance from the actual
				// date
				// calendar.add(Calendar.DAY_OF_MONTH, daysInAdvanceToCreate);
			}
			return isValidScheduleTime(new FinanceDate(calendar.getTime())) ? calendar
					.getTime() : null;
		}
	}

	/**
	 * 
	 * @return
	 */
	private boolean isScheduled() {
		// if next schedule date is available then this recurring transaction
		// has been scheduled.
		return nextScheduleOn != null;
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {
		if (type != RECURRING_UNSCHEDULED && !isStopped()) {
			// looks new recurring is going to be created.
			do {
				scheduleAgain2();

				// if nextscheduleOn <= current date then iterate again
				// NOTE: checking with server current time for now, it has to be
				// changed to client time.
			} while (nextScheduleOn != null
					&& !nextScheduleOn.after(new FinanceDate()));
		}
		// Need to clone the transaction object because it is coming from
		// transaction view we need to save a transaction template.
		try {
			transaction = FinanceTool.createDuplicateTransaction(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		transaction.setNumber("");
		transaction.setSaveStatus(Transaction.STATUS_TEMPLATE);
		return super.onSave(session);
	}

	@Override
	public boolean onUpdate(Session session) throws CallbackException {
		if (OnUpdateThreadLocal.get()) {
			return false;
		}
		if (type != RECURRING_UNSCHEDULED && !isStopped()) {
			if (occurencesCompleted == 0) {
				nextScheduleOn = null;
			}
			do {
				Date nextDate = next();
				if (nextDate == null) {
					// can't schedule next again.
					nextScheduleOn = null;
					stopped = true;
				} else {
					nextScheduleOn = new FinanceDate(nextDate);
				}

			} while (nextScheduleOn == null
					|| !nextScheduleOn.after(new FinanceDate()));
		}
		transaction.setCompany(getCompany());
		return super.onUpdate(session);
	}

	/**
	 * @return <b>true</b> if successfully scheduled. <b>false</b> if unable to
	 *         create next schedule [recurring might be completed].
	 */
	public boolean scheduleAgain() {
		prevScheduleOn = nextScheduleOn;
		return scheduleAgain2();
	}

	/**
	 * helper method for {@link #scheduleAgain()}
	 * 
	 * @return
	 */
	private boolean scheduleAgain2() {
		if (isScheduled()) {
			// checks only if nextScheduleOn available. nextScheduleOn will be
			// updated next below.
			occurencesCompleted++;
		}

		Date nextDate = next();
		if (nextDate == null) {
			// can't schedule next again.
			nextScheduleOn = null;
			stopped = true;
			return false;
		}

		nextScheduleOn = new FinanceDate(nextDate);
		return true;
	}

	/**
	 * 
	 * @return
	 */
	public boolean schedulePrevious() {
		if (isScheduled()) {
			// checks only if nextScheduleOn available. nextScheduleOn will be
			// updated next below.
			occurencesCompleted--;
		}
		nextScheduleOn = prevScheduleOn;

		Date prevDate = isStopped() ? null : createScheduleIterator()
				.previous();
		if (prevDate == null) {
			// can't schedule next again.
			prevScheduleOn = null;
			return false;
		}

		prevScheduleOn = new FinanceDate(prevDate);
		return true;
	}

	/**
	 * Returns original transaction date for next scheduled transaction, as
	 * {@link #nextScheduleOn} includes {@link #daysInAdvanceToCreate}.
	 * 
	 * @return {@link #nextScheduleOn} + {@link #daysInAdvanceToCreate}
	 */
	public FinanceDate getNextScheduledTransactionDate() {
		// As nextScheduleOn includes daysInAdvance, we need to add those
		// daysInAdvance value to the nextScheduleOn to get original
		// nextScheduledTransaction date
		if (nextScheduleOn != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(nextScheduleOn.getAsDateObject());
			if (type == RECURRING_SCHEDULED) {
				calendar.add(Calendar.DAY_OF_MONTH, daysInAdvanceToCreate);
			}
			return new FinanceDate(calendar.getTime());
		}
		return null;
	}

	public void setStopped(boolean stopped) {
		this.stopped = stopped;
	}

	public boolean isStopped() {
		return stopped;
	}

	/**
	 * Gives next transaction due date.
	 * 
	 * @return
	 */
	public FinanceDate getNextTransactionDueDate() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(nextScheduleOn.getAsDateObject());

		switch (dueDateType) {
		case DUE_DATE_DAYS_AFTER_THE_INVOICE:
			cal.add(Calendar.DAY_OF_MONTH, dueDateValue);
			break;
		case DUE_DATE_OF_THE_CURRENT_MONTH:
			cal.set(Calendar.DAY_OF_MONTH, dueDateValue);
		case DUE_DATE_OF_THE_FOLLOWING_MONTH:
			cal.add(Calendar.MONTH, 1);
			cal.set(Calendar.DAY_OF_MONTH, dueDateValue);
		default:
			return null;
		}

		return new FinanceDate(cal.getTime());
	}

	public boolean canHaveDueDate() {
		int transactionType = transaction.getType();
		return transactionType == Transaction.TYPE_INVOICE
				|| transactionType == Transaction.TYPE_ENTER_BILL;
	}

	public Transaction getTransaction() {
		return transaction;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
		if (transaction != null)
			transaction.setRecurringTransaction(this);
	}

	public boolean isAlertWhenEnded() {
		return alertWhenEnded;
	}

	public void setAlertWhenEnded(boolean alertWhenEnded) {
		this.alertWhenEnded = alertWhenEnded;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		// if (getSaveStatus() == STATUS_DRAFT) {
		// return;
		// }

		AccounterMessages messages = Global.get().messages();
		w.put(messages.name(), this.name);
		w.put(messages.recurringType(), this.type).gap();
		w.put(messages.intervalType(), this.intervalType);
		w.put(messages.daysInAdvance(), this.daysInAdvanceToCreate).gap();
		w.put(messages.startDate(), this.startDate.toString());
		w.put(messages.endDateType(), this.endDateType).gap();
		w.put(messages.onSpecificWeek(), this.weekOfMonth);
		w.put(messages.onSpecificDay(), this.dayOfMonth).gap();
		w.put(messages.remindMe(), this.daysBeforeToRemind);

	}

	public Set<Reminder> getReminders() {
		return reminders;
	}

	public void setReminders(Set<Reminder> reminders) {
		this.reminders = reminders;
	}

	public int getDaysBeforeToRemind() {
		return daysBeforeToRemind;
	}

	public void setDaysBeforeToRemind(int daysBeforeToRemind) {
		this.daysBeforeToRemind = daysBeforeToRemind;
	}

	public boolean isNotifyCreatedTransaction() {
		return notifyCreatedTransaction;
	}

	public void setNotifyCreatedTransaction(boolean notifyCreatedTransaction) {
		this.notifyCreatedTransaction = notifyCreatedTransaction;
	}

	@Override
	public int getObjType() {
		return IAccounterCore.RECURING_TRANSACTION;
	}

	@Override
	public void selfValidate() throws AccounterException {
		if (name == null || name.trim().isEmpty()) {
			throw new AccounterException(AccounterException.ERROR_NAME_NULL,
					Global.get().messages().recurring());
		}
		if ((type < 0)) {
			throw new AccounterException(AccounterException.ERROR_NAME_NULL,
					Global.get().messages().recurringType());
		}
		if ((intervalType < 0)) {
			throw new AccounterException(AccounterException.ERROR_NAME_NULL,
					Global.get().messages().intervalType());
		}

		if ((intervalPeriod < 0)) {
			throw new AccounterException(AccounterException.ERROR_NAME_NULL,
					Global.get().messages().interval());
		}

		if ((this.startDate == null)) {
			throw new AccounterException(AccounterException.ERROR_NAME_NULL,
					Global.get().messages().startDate());
		}
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
