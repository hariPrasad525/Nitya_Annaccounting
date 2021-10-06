package com.vimukti.accounter.listeners;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.vimukti.accounter.core.Property;
import com.vimukti.accounter.core.ServerMaintanance;
import com.vimukti.accounter.core.Subscription;
import com.vimukti.accounter.encryption.Encrypter;
import com.vimukti.accounter.main.MailLogTailerListener;
import com.vimukti.accounter.main.MessageLoader;
import com.vimukti.accounter.main.ServerConfiguration;
import com.vimukti.accounter.main.ServerGlobal;
import com.vimukti.accounter.setup.server.DatabaseManager;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.externalization.AccounterMessages2;
import com.vimukti.accounter.web.server.FinanceTool;

public class StartUpListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {

	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		ExecutorService executor = Executors.newSingleThreadExecutor();
		executor.execute(new Runnable() {

			@Override
			public void run() {
				try {
					if (!ServerConfiguration.isDesktopApp()) {
						Encrypter.init();
						Session session = HibernateUtil.openSession();
						try {
							ServerMaintanance maintanance = (ServerMaintanance) session
									.get(ServerMaintanance.class, 1L);
							if (maintanance != null) {
								ServerConfiguration
										.setUnderMaintainance(maintanance
												.isUnderMaintanance());
							}
							FinanceTool.createViews();

							if (ServerConfiguration.isLoadMessages()) {
								loadAccounterMessages();
							}

							loadSubscriptionFeatures();

						} finally {
							session.close();
						}
						Global.set(new ServerGlobal());

						// Creating Email Log listener
						createMailLogListener();

					} else {
						if (DatabaseManager.isDBConfigured()) {
							Session session = HibernateUtil.openSession();
							try {
								FinanceTool.createViews();

								if (ServerConfiguration.isLoadMessages()) {
									loadAccounterMessages();
								}

								loadSubscriptionFeatures();

								Property prop = (Property) session.get(
										Property.class, Property.SETUP_PAGE);
								ServerConfiguration
										.setSeupStatus(prop != null ? prop
												.getValue() : "0");

							} finally {
								session.close();
							}
							Global.set(new ServerGlobal());
						}
					}

					ServerConfiguration.completeStartup();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
	}

	private static void loadSubscriptionFeatures() {
		saveSubscription(Subscription.BEFORE_PAID_FETURE);
		saveSubscription(Subscription.FREE_CLIENT);
		saveSubscription(Subscription.PREMIUM_USER);
	}

	private static void saveSubscription(int type) {
		Session session = HibernateUtil.getCurrentSession();
		Subscription instance = Subscription.getInstance(type);
		if (instance == null) {
			Transaction beginTransaction = session.beginTransaction();
			instance = new Subscription();
			instance.setType(type);
			session.save(instance);
			beginTransaction.commit();
		}
	}

	private static void createMailLogListener() {

		String fileName = ServerConfiguration.getEmailLogger();
		if (fileName != null) {

			File file = new File(fileName);
			if (file.exists()) {
				new MailLogTailerListener(file);
			}

		}

	}

	private static void loadAccounterMessages() throws IOException {
		String fileName = AccounterMessages.class.getName();
		fileName = fileName.replace('.', '/');
		fileName = fileName + ".properties";

		String defaultFileName = (AccounterMessages.class.getName().replace(
				'.', '/')) + ".properties";
		ClassLoader classLoader = AccounterMessages.class.getClassLoader();
		InputStream is = null;
		InputStreamReader reader = null;

		String fileName2 = AccounterMessages2.class.getName();
		fileName2 = fileName2.replace('.', '/');
		fileName2 = fileName2 + ".properties";

		String defaultFileName2 = (AccounterMessages2.class.getName().replace(
				'.', '/')) + ".properties";
		ClassLoader classLoader2 = AccounterMessages2.class.getClassLoader();
		InputStream is2 = null;
		InputStreamReader reader2 = null;
		try {
			is2 = classLoader.getResourceAsStream(fileName2);
			if (is2 == null) {
				is2 = classLoader.getResourceAsStream(defaultFileName2);
				if (is2 == null) {
					throw new FileNotFoundException(
							"Could not find any properties files matching the given class AccounterMessages2.");
				}
			}
			new MessageLoader(is2).loadMessages();
			System.out.println("Completed the Inseting of messages 2..");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("UTF-8 encoding not found.", e);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (reader2 != null) {
				reader2.close();
			}
			if (is2 != null) {
				is2.close();
			}
		}
		try {
			is = classLoader.getResourceAsStream(fileName);
			if (is == null) {
				is = classLoader.getResourceAsStream(defaultFileName);
				if (is == null) {
					throw new FileNotFoundException(
							"Could not find any properties files matching the given class.");
				}
			}
			new MessageLoader(is).loadMessages();
			System.out.println("Completed the Inseting of messages..");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("UTF-8 encoding not found.", e);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				reader.close();
			}
			if (is != null) {
				is.close();
			}
		}

	}

}
