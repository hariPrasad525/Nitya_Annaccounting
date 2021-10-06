package com.vimukti.accounter.utils;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.dialect.EncryptedStringType;
import org.hibernate.proxy.HibernateProxy;

import com.vimukti.accounter.main.ServerConfiguration;
import com.vimukti.accounter.setup.server.DatabaseManager;

public class HibernateUtil {

	static ThreadLocal<Session> threadLocalSession = new ThreadLocal<Session>();

	private static SessionFactory sessionFactory;

	public static Session openSession() {
		Session session = getSessionFactory().openSession();
		threadLocalSession.set(session);
		return session;
	}

	private static synchronized SessionFactory getSessionFactory() {
		if (sessionFactory != null) {
			return sessionFactory;
		}
		sessionFactory = buildSessionFactory();
		return sessionFactory;
	}

	private static SessionFactory buildSessionFactory() {
		if (ServerConfiguration.isDesktopApp()) {
			try {
				DatabaseManager.getInstance().loadDbConfig();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Configuration config = new Configuration();
		config.getTypeResolver().registerTypeOverride(
				EncryptedStringType.INSTANCE);
		addMapping(config);
		// config.setListener("pre-update", new PreUpdateEventListener() {
		//
		// /*
		//
		// */
		// private static final long serialVersionUID = 1L;
		//
		// @Override
		// public boolean onPreUpdate(PreUpdateEvent event) {
		// Object entity = event.getEntity();
		// if (entity instanceof CreatableObject) {
		// if (((CreatableObject) entity).getCompany().getId() !=
		// AccounterThreadLocal
		// .get().getCompany().getId()) {
		// return true;
		// }
		// }
		// return false;
		// }
		// });

		config.configure();

		return config.buildSessionFactory();
	}

	private static void addMapping(Configuration config) {
		config.addResource("mapping/common/finance-admin-mapping.xml");
		config.addResource("mapping/common/finance-mapping.xml");
		config.addResource("mapping/common/finance-new-mapping.xml");
		config.addResource("mapping/common/finance-transactions.xml");
		config.addResource("mapping/common/finance-translate-mapping.xml");
		config.addResource("mapping/common/usermanagment.xml");
		config.addResource("mapping/common/finance-migration.xml");

		if (!ServerConfiguration.isDesktopApp()) {
			loadFiles(config, DatabaseManager.POSTGRESQL);
		} else {
			String dbType = DatabaseManager.getInstance().getDbType();
			loadFiles(config, dbType);
		}

	}

	private static void loadFiles(Configuration config, String dbType) {
		config.addResource("mapping/" + dbType + "/queries.xml");
		config.addResource("mapping/" + dbType + "/finance-query.xml");
		config.addResource("mapping/" + dbType + "/finance-hql.xml");
		config.addResource("mapping/" + dbType + "/finance-reports.xml");
		config.addResource("mapping/" + dbType + "/finance-new-queries.xml");
		config.addResource("mapping/" + dbType + "/finance-search.xml");
		config.addResource("mapping/" + dbType + "/finance-triggers.xml");
		config.addResource("mapping/" + dbType + "/finance-payroll.xml");
	}

	public static Session getCurrentSession() {
		return threadLocalSession.get();
	}

	@SuppressWarnings("unchecked")
	public static <T> T initializeAndUnproxy(T entity) {
		if (entity == null) {
			throw new NullPointerException(
					"Entity passed for initialization is null");
		}

		Hibernate.initialize(entity);
		if (entity instanceof HibernateProxy) {
			entity = (T) ((HibernateProxy) entity)
					.getHibernateLazyInitializer().getImplementation();
		}
		return entity;
	}

}
