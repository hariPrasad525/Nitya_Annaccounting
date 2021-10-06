package com.nitya.accounter.services;

import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * 
 * @author Devesh Satwani
 * 
 */
public class Service {

	private IAccounterService accounter;
	private IAccounterDAOService accounterDao;
	private IAccounterReportDAOService accounterReportDao;
	private IAccounterGUIDAOService accounterGUIDao;

	private IAccounterClientService accounterClientService;

	/**
	 * @return the accounterClientService
	 */
	public IAccounterClientService getAccounterClientService() {
		return accounterClientService;
	}

	private static ApplicationContext ctx;
	private static Service service;

	public void setAccounter(IAccounterService dao) {
		accounter = dao;
	}

	public void setAccounterDao(IAccounterDAOService dao) {
		accounterDao = dao;
	}

	public void setAccounterReportDao(IAccounterReportDAOService dao) {
		accounterReportDao = dao;
	}

	public void setAccounterGUIDao(IAccounterGUIDAOService dao) {
		accounterGUIDao = dao;
	}

	public void setAccounterClientService(IAccounterClientService clientService) {
		accounterClientService = clientService;
	}

	static {

		// System.setProperty("db.createOrUpdate", "create");
		ctx = new ClassPathXmlApplicationContext("finance-context.xml");
	}

	public static SessionFactory getSessionFactory() {
		return (SessionFactory) ctx.getBean("sessionFactory",
				SessionFactory.class);
	}

	public static HibernateTemplate getHibernateTemplate() {
		return (HibernateTemplate) ctx.getBean("hibernateTemplate",
				HibernateTemplate.class);

	}

	public static TransactionTemplate getTransactionTemplate() {
		return (TransactionTemplate) ctx.getBean("transactionTemplate",
				TransactionTemplate.class);
	}

	private Service() {

//		AccounterService accounter = new AccounterService();
//		accounter.setSessionFactory(getSessionFactory());
//		accounter.setHibernateTemplate(getHibernateTemplate());
		// accounter.setTransactionTemplate(getTransactionTemplate());

		AccounterDAOService accounterDao = new AccounterDAOService();
		accounterDao.setSessionFactory(getSessionFactory());
		accounterDao.setHibernateTemplate(getHibernateTemplate());
		// accounterDao.setTransactionTemplate(getTransactionTemplate());
		// accounterDao.setTransactionTemplate(getTransactionTemplate());

		AccounterReportDAOService accounterReportDao = new AccounterReportDAOService();
		accounterReportDao.setSessionFactory(getSessionFactory());
		accounterReportDao.setHibernateTemplate(getHibernateTemplate());
		accounterReportDao.setTransactionTemplate(getTransactionTemplate());
		accounterReportDao.setAccounterDao(accounterDao);

		AccounterGUIDAOService accounterGUIDao = new AccounterGUIDAOService();
		accounterGUIDao.setSessionFactory(getSessionFactory());
		accounterGUIDao.setHibernateTemplate(getHibernateTemplate());
		accounterGUIDao.setTransactionTemplate(getTransactionTemplate());
		accounterGUIDao.setAccounterDao(accounterDao);

		AccounterClientService clientService = new AccounterClientService();
		clientService.setSessionFactory(getSessionFactory());
		clientService.setHibernateTemplate(getHibernateTemplate());
		clientService.setTransactionTemplate(getTransactionTemplate());
		clientService.setAccounterDao(accounterDao);

		// accounter.setAccounterGUIDao(accounterGUIDao);
		// accounter.setAccounterDao(accounterDao);

		setAccounter(accounter);
		setAccounterDao(accounterDao);
		setAccounterReportDao(accounterReportDao);
		setAccounterGUIDao(accounterGUIDao);
		setAccounterClientService(clientService);

		service = this;

	}

	public IAccounterDAOService getAccounterDAOService() {
		return accounterDao;
	}

	public IAccounterService getAccounterCRUDService() {
		return accounter;
	}

	public IAccounterReportDAOService getAccounterReportDAOService() {
		return accounterReportDao;
	}

	public IAccounterGUIDAOService getAccounterGUIService() {
		return accounterGUIDao;
	}

	public static Service getInstance() {
		if (service == null) {
			service = new Service();
		}
		return service;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {

		throw new CloneNotSupportedException("Warning! Clone Not Supported!");

	}

}
