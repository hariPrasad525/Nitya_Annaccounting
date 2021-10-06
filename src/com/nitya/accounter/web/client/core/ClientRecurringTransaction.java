package com.nitya.accounter.web.client.core;

import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.externalization.AccounterMessages;

public class ClientRecurringTransaction implements IAccounterCore {
	private static final long serialVersionUID = 1L;

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

	public final static int DUE_DATE_OF_THE_CURRENT_MONTH = 2;

	/*-------------
	 * Action options
	 *-------------*/
	public final static int ACTION_SAVE_DRAFT = 0;

	public final static int ACTION_APPROVE = 1;

	public final static int ACTION_APPROVE_SEND = 2;

	private long startDate;

	private long endDate; // Optional

	/*
	 * If user selects stop after these many occurrences.
	 */
	private int occurencesCount;

	private int occurencesCompleted;

	private int dueDateValue;

	private int dueDateType;

	private long nextScheduleOn;

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
	private int endDateType;

	// If yearly interval selected, we need selected month
	private int month;

	private int version;

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
	private long prevScheduleOn;

	private long id;

	/**
	 * {@link #ACTION_SAVE_DRAFT}, {@link #ACTION_APPROVE},
	 * {@link #ACTION_APPROVE_SEND}
	 */
	private int actionType;
	private String name;
	private ClientTransaction transaction;
	private boolean stopped;

	public ClientRecurringTransaction() {
		// TODO Auto-generated constructor stub
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

	@Override
	public String getDisplayName() {
		return name;
	}

	public int getDueDateType() {
		return dueDateType;
	}

	public int getDueDateValue() {
		return dueDateValue;
	}

	public long getEndDate() {
		return endDate;
	}

	public String getFrequencyString() {
		// TODO implement later if needed.
		return toString();
	}

	public long getId() {
		return id;
	}

	@Override
	public long getID() {
		return id;
	}

	// private long getInitialDateForNextSchedule() {
	// return nextScheduleOn == 0 ? getStartDate() : nextScheduleOn;
	// }

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

	public long getNextScheduleOn() {
		return nextScheduleOn;
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.RECURRING_TRANSACTION;
	}

	public int getOccurencesCompleted() {
		return occurencesCompleted;
	}

	public int getOccurencesCount() {
		return occurencesCount;
	}

	public long getPrevScheduleOn() {
		return prevScheduleOn;
	}

	public long getStartDate() {
		return startDate;
	}

	public int getType() {
		return type;
	}

	// @Override
	// public boolean canEdit(IAccounterServerCore clientObject)
	// throws AccounterException {
	// // TODO
	// return true;
	// }

	public int getWeekDay() {
		return weekDay;
	}

	public int getWeekOfMonth() {
		return weekOfMonth;
	}

	public boolean isUnbilledChargesEnabled() {
		return unbilledChargesEnabled;
	}

	// private boolean isValidScheduleTime(ClientFinanceDate date) {
	// if (getEndDate() == 0) {
	// return true;
	// }
	// // TODO need to add another condition, date may equal.
	// return date.before(new ClientFinanceDate(getEndDate()));
	// }

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

	public void setEndDate(long endDate) {
		this.endDate = endDate;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Override
	public void setID(long id) {
		this.id = id;

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

	public void setNextScheduleOn(long nextScheduleOn) {
		this.nextScheduleOn = nextScheduleOn;
	}

	public void setOccurencesCompleted(int occurencesCompleted) {
		this.occurencesCompleted = occurencesCompleted;
	}

	public void setOccurencesCount(int occurencesCount) {
		this.occurencesCount = occurencesCount;
	}

	public void setPrevScheduleOn(long prevScheduleOn) {
		this.prevScheduleOn = prevScheduleOn;
	}

	public void setStartDate(long startDate) {
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
		// sb.append(name).append(" - ");
		AccounterMessages messages = Global.get().messages();
		sb.append(messages.every());
		sb.append(intervalPeriod).append(' ');
		switch (intervalType) {
		case INTERVAL_TYPE_DAILY:
			sb.append(messages.dayOfs());
			break;
		case INTERVAL_TYPE_WEEKLY:
			sb.append(messages.weekOfs());
			break;
		case INTERVAL_TYPE_MONTHLY_DAY:
		case INTERVAL_TYPE_MONTHLY_WEEK:
			sb.append(messages.monthOfs());
			break;
		case INTERVAL_TYPE_YEARLY:
			sb.append(messages.yearOfs());
		default:
			sb.append("-");
			break;
		}
		return sb.toString();
	}

	public void setEndDateType(int endDateType) {
		this.endDateType = endDateType;
	}

	public int getEndDateType() {
		return endDateType;
	}

	public void setStopped(boolean stopped) {
		this.stopped = stopped;
	}

	public boolean isStopped() {
		return stopped;
	}

	@Override
	public int getVersion() {
		return version;
	}

	@Override
	public void setVersion(int version) {
		this.version = version;

	}

	public ClientTransaction getTransaction() {
		return transaction;
	}

	public void setTransaction(ClientTransaction transaction) {
		this.transaction = transaction;
	}

	public boolean isAlertWhenEnded() {
		return alertWhenEnded;
	}

	public void setAlertWhenEnded(boolean alertWhenEnded) {
		this.alertWhenEnded = alertWhenEnded;
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

}
