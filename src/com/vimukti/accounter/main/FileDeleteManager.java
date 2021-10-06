package com.vimukti.accounter.main;

import java.io.File;
import java.util.LinkedList;

public class FileDeleteManager {

	private static final int MAX_QUEUE_SIZE = 1000;
	private static LinkedList<File> queue = new LinkedList<File>();

	public synchronized static void deleteFile(File tempFile) {
		tempFile.deleteOnExit();
		queue.addLast(tempFile);
		if (queue.size() > MAX_QUEUE_SIZE) {
			File f = queue.poll();
			f.delete();
		}
	}

}
