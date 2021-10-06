package com.nitya.accounter.utils;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A Queue used to delete files after some fixed Delay
 * 
 * 
 */
public class DeleteQueue {

	/**
	 * Fixed delay the file will be deleted after
	 */
	public static final long DELAY = 60 * 60 * 1000;

	private static DeleteQueue queue = null;

	private Timer timer = null;

	public static DeleteQueue get() {
		if (queue == null) {
			queue = new DeleteQueue();
		}
		return queue;
	}

	public DeleteQueue() {
		timer = new Timer("Delete Queue", true);
	}

	/**
	 * Adds a files to queue to delete after delay
	 * 
	 * @param file
	 */
	public void add(File file) {
		addInternal(file);
	}

	/**
	 * Adds a files to queue to delete after delay
	 * 
	 * @param fPath
	 */
	public void add(String fPath) {
		addInternal(new File(fPath));
	}

	/**
	 * Deletes given file after some filed delay
	 * 
	 * @param file
	 */
	private void addInternal(final File file) {
		if (!file.exists()) {
			return;
		}
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				file.deleteOnExit();
			}
		}, DELAY);
	}
}
