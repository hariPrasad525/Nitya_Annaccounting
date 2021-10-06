package com.vimukti.accounter.main;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListenerAdapter;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.vimukti.accounter.utils.HibernateUtil;

public class MailLogTailerListener extends TailerListenerAdapter implements
		Runnable {

	private Tailer tailer;

	public MailLogTailerListener(File logFile) {
		tailer = new Tailer(logFile, this, 1000);
		Thread thread = new Thread(this);
		thread.setDaemon(true);
		thread.start();
	}

	Pattern pattern = Pattern.compile(".+<(.+?)>.+status=\\[(\\w+?)\\] (.+)");

	@Override
	public void handle(String line) {

		Matcher matcher = pattern.matcher(line);
		boolean matches = matcher.matches();
		if (matches) {
			String emailID = matcher.group(1);
			String status = matcher.group(2);
			String reason = matcher.group(3);
			if (status.equals("bounced") || status.equals("deffered")) {
				Session currentSession = HibernateUtil.openSession();
				Transaction beginTransaction = currentSession
						.beginTransaction();
				try {

					currentSession.getNamedQuery("update.client.emailStatus")
							.setParameter("email", emailID).executeUpdate();
					beginTransaction.commit();
				} catch (HibernateException h) {
					h.printStackTrace();
					if (beginTransaction != null) {
						beginTransaction.rollback();
					}
				} finally {
					if (currentSession != null)
						currentSession.close();

				}
			}
		}

	}

	@Override
	public void run() {
		tailer.run();
	}

	public void stop() {
		tailer.stop();
	}
}
