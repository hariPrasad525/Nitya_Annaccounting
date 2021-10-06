package com.nitya.accounter.core.migration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Appender;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.hibernate.Query;
import org.hibernate.Session;

import com.nitya.accounter.core.AccounterThreadLocal;
import com.nitya.accounter.core.Company;
import com.nitya.accounter.core.User;
import com.nitya.accounter.main.ServerConfiguration;
import com.nitya.accounter.main.ServerGlobal;
import com.nitya.accounter.main.ServerLocal;
import com.nitya.accounter.utils.HibernateUtil;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.server.FinanceTool;

public class MigrationMain {
	static Logger log = Logger.getLogger(MigrationMain.class);

	public static void main(String[] args) throws IOException {
		ServerConfiguration.init(null);
		initLogger();
		Session session = HibernateUtil.openSession();
		FinanceTool.createViews();
		Global.set(new ServerGlobal());
		ServerLocal.set(Locale.ENGLISH);
		log.info("Started Migration of All Companies!!");
		long currentCompany = 0l;
		while (true) {
			Query query = session.getNamedQuery("getNextCompany").setParameter(
					"companyId", currentCompany);
			query.setMaxResults(1);
			Company company = (Company) query.uniqueResult();
			if (company == null) {
				break;
			}
			List<User> usersList = new ArrayList<User>(company.getUsers());
			if (!usersList.isEmpty()) {
				AccounterThreadLocal.set(usersList.get(0));
			}
			currentCompany = company.getID();
			log.info("Migrating Company : " + currentCompany);
			MigrationUtil.migrate(company);
		}
		log.info("Done Migration of All Companies!!");
	}

	protected static String getArgument(String[] args, String name) {
		for (int i = 0; i < args.length - 1; i++) {
			String arg = args[i];
			if (arg.equals(name)) {
				return args[i + 1];
			}
		}
		return null;
	}

	private static void initLogger() {
		File logsdir = new File("logs");
		if (!logsdir.exists()) {
			logsdir.mkdirs();
		}
		String path = new File(logsdir, "serverlog").getAbsolutePath();
		try {
			Layout layout = new PatternLayout(
					"%d{dd MMM yyyy HH:mm:ss} %p [%c{1}] - %m %n");
			Logger.getRootLogger().addAppender(
					new DailyRollingFileAppender(layout, path,
							"'.'yyyy-MM-dd-a"));
			Appender appender = Logger.getRootLogger().getAppender("console");
			appender.setLayout(layout);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Logger.getRootLogger().setLevel(Level.INFO);
		Logger.getLogger("org.hibernate").setLevel(Level.INFO);
		Logger.getLogger("com.vimukti").setLevel(Level.INFO);

	}

}
