/**
 * 
 */
package com.nitya.accounter.mobile;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Prasanna Kumar G
 * 
 */
public class AccounterTimer {

	public static AccounterTimer INSTANCE = new AccounterTimer();

	private Timer timer = new Timer();

	/**
	 * Schedules the specified task for execution at the specified time. If the
	 * time is in the past, the task is scheduled for immediate execution.
	 * 
	 * @param task
	 *            task to be scheduled.
	 * @param time
	 *            time at which task is to be executed.
	 * @throws IllegalArgumentException
	 *             if <tt>time.getTime()</tt> is negative.
	 * @throws IllegalStateException
	 *             if task was already scheduled or cancelled, timer was
	 *             cancelled, or timer thread terminated.
	 */
	public void schedule(TimerTask task, Date time) {
		timer.schedule(task, time);
	}

	/**
	 * Schedules the specified task for execution after the specified delay.
	 * 
	 * @param task
	 *            task to be scheduled.
	 * @param delay
	 *            delay in milliseconds before task is to be executed.
	 * @throws IllegalArgumentException
	 *             if <tt>delay</tt> is negative, or
	 *             <tt>delay + System.currentTimeMillis()</tt> is negative.
	 * @throws IllegalStateException
	 *             if task was already scheduled or cancelled, or timer was
	 *             cancelled.
	 */
	public void schedule(TimerTask task, long delay) {
		timer.schedule(task, delay);
	}

	/**
	 * Schedules the specified task for repeated <i>fixed-delay execution</i>,
	 * beginning at the specified time. Subsequent executions take place at
	 * approximately regular intervals, separated by the specified period.
	 * 
	 * <p>
	 * In fixed-delay execution, each execution is scheduled relative to the
	 * actual execution time of the previous execution. If an execution is
	 * delayed for any reason (such as garbage collection or other background
	 * activity), subsequent executions will be delayed as well. In the long
	 * run, the frequency of execution will generally be slightly lower than the
	 * reciprocal of the specified period (assuming the system clock underlying
	 * <tt>Object.wait(long)</tt> is accurate).
	 * 
	 * <p>
	 * Fixed-delay execution is appropriate for recurring activities that
	 * require "smoothness." In other words, it is appropriate for activities
	 * where it is more important to keep the frequency accurate in the short
	 * run than in the long run. This includes most animation tasks, such as
	 * blinking a cursor at regular intervals. It also includes tasks wherein
	 * regular activity is performed in response to human input, such as
	 * automatically repeating a character as long as a key is held down.
	 * 
	 * @param task
	 *            task to be scheduled.
	 * @param firstTime
	 *            First time at which task is to be executed.
	 * @param period
	 *            time in milliseconds between successive task executions.
	 * @throws IllegalArgumentException
	 *             if <tt>time.getTime()</tt> is negative.
	 * @throws IllegalStateException
	 *             if task was already scheduled or cancelled, timer was
	 *             cancelled, or timer thread terminated.
	 */
	public void schedule(TimerTask task, Date firstTime, long period) {
		timer.schedule(task, firstTime, period);
	}

	/**
	 * Schedules the specified task for repeated <i>fixed-delay execution</i>,
	 * beginning after the specified delay. Subsequent executions take place at
	 * approximately regular intervals separated by the specified period.
	 * 
	 * <p>
	 * In fixed-delay execution, each execution is scheduled relative to the
	 * actual execution time of the previous execution. If an execution is
	 * delayed for any reason (such as garbage collection or other background
	 * activity), subsequent executions will be delayed as well. In the long
	 * run, the frequency of execution will generally be slightly lower than the
	 * reciprocal of the specified period (assuming the system clock underlying
	 * <tt>Object.wait(long)</tt> is accurate).
	 * 
	 * <p>
	 * Fixed-delay execution is appropriate for recurring activities that
	 * require "smoothness." In other words, it is appropriate for activities
	 * where it is more important to keep the frequency accurate in the short
	 * run than in the long run. This includes most animation tasks, such as
	 * blinking a cursor at regular intervals. It also includes tasks wherein
	 * regular activity is performed in response to human input, such as
	 * automatically repeating a character as long as a key is held down.
	 * 
	 * @param task
	 *            task to be scheduled.
	 * @param delay
	 *            delay in milliseconds before task is to be executed.
	 * @param period
	 *            time in milliseconds between successive task executions.
	 * @throws IllegalArgumentException
	 *             if <tt>delay</tt> is negative, or
	 *             <tt>delay + System.currentTimeMillis()</tt> is negative.
	 * @throws IllegalStateException
	 *             if task was already scheduled or cancelled, timer was
	 *             cancelled, or timer thread terminated.
	 */
	public void schedule(TimerTask task, long delay, long period) {
		timer.schedule(task, delay, period);
	}

	/**
	 * Schedules the specified task for repeated <i>fixed-rate execution</i>,
	 * beginning at the specified time. Subsequent executions take place at
	 * approximately regular intervals, separated by the specified period.
	 * 
	 * <p>
	 * In fixed-rate execution, each execution is scheduled relative to the
	 * scheduled execution time of the initial execution. If an execution is
	 * delayed for any reason (such as garbage collection or other background
	 * activity), two or more executions will occur in rapid succession to
	 * "catch up." In the long run, the frequency of execution will be exactly
	 * the reciprocal of the specified period (assuming the system clock
	 * underlying <tt>Object.wait(long)</tt> is accurate).
	 * 
	 * <p>
	 * Fixed-rate execution is appropriate for recurring activities that are
	 * sensitive to <i>absolute</i> time, such as ringing a chime every hour on
	 * the hour, or running scheduled maintenance every day at a particular
	 * time. It is also appropriate for recurring activities where the total
	 * time to perform a fixed number of executions is important, such as a
	 * countdown timer that ticks once every second for ten seconds. Finally,
	 * fixed-rate execution is appropriate for scheduling multiple repeating
	 * timer tasks that must remain synchronized with respect to one another.
	 * 
	 * @param task
	 *            task to be scheduled.
	 * @param firstTime
	 *            First time at which task is to be executed.
	 * @param period
	 *            time in milliseconds between successive task executions.
	 * @throws IllegalArgumentException
	 *             if <tt>time.getTime()</tt> is negative.
	 * @throws IllegalStateException
	 *             if task was already scheduled or cancelled, timer was
	 *             cancelled, or timer thread terminated.
	 */
	public void scheduleAtFixedRate(TimerTask task, Date firstTime, long period) {
		timer.scheduleAtFixedRate(task, firstTime, period);
	}

	/**
	 * Schedules the specified task for repeated <i>fixed-rate execution</i>,
	 * beginning after the specified delay. Subsequent executions take place at
	 * approximately regular intervals, separated by the specified period.
	 * 
	 * <p>
	 * In fixed-rate execution, each execution is scheduled relative to the
	 * scheduled execution time of the initial execution. If an execution is
	 * delayed for any reason (such as garbage collection or other background
	 * activity), two or more executions will occur in rapid succession to
	 * "catch up." In the long run, the frequency of execution will be exactly
	 * the reciprocal of the specified period (assuming the system clock
	 * underlying <tt>Object.wait(long)</tt> is accurate).
	 * 
	 * <p>
	 * Fixed-rate execution is appropriate for recurring activities that are
	 * sensitive to <i>absolute</i> time, such as ringing a chime every hour on
	 * the hour, or running scheduled maintenance every day at a particular
	 * time. It is also appropriate for recurring activities where the total
	 * time to perform a fixed number of executions is important, such as a
	 * countdown timer that ticks once every second for ten seconds. Finally,
	 * fixed-rate execution is appropriate for scheduling multiple repeating
	 * timer tasks that must remain synchronized with respect to one another.
	 * 
	 * @param task
	 *            task to be scheduled.
	 * @param delay
	 *            delay in milliseconds before task is to be executed.
	 * @param period
	 *            time in milliseconds between successive task executions.
	 * @throws IllegalArgumentException
	 *             if <tt>delay</tt> is negative, or
	 *             <tt>delay + System.currentTimeMillis()</tt> is negative.
	 * @throws IllegalStateException
	 *             if task was already scheduled or cancelled, timer was
	 *             cancelled, or timer thread terminated.
	 */
	public void scheduleAtFixedRate(TimerTask task, long delay, long period) {
		timer.scheduleAtFixedRate(task, delay, period);
	}

	/**
	 * Removes all cancelled tasks from this timer's task queue. <i>Calling this
	 * method has no effect on the behavior of the timer</i>, but eliminates the
	 * references to the cancelled tasks from the queue. If there are no
	 * external references to these tasks, they become eligible for garbage
	 * collection.
	 * 
	 * <p>
	 * Most programs will have no need to call this method. It is designed for
	 * use by the rare application that cancels a large number of tasks. Calling
	 * this method trades time for space: the runtime of the method may be
	 * proportional to n + c log n, where n is the number of tasks in the queue
	 * and c is the number of cancelled tasks.
	 * 
	 * <p>
	 * Note that it is permissible to call this method from within a a task
	 * scheduled on this timer.
	 * 
	 * @return the number of tasks removed from the queue.
	 * @since 1.5
	 */
	public int purge() {
		return timer.purge();
	}

}
