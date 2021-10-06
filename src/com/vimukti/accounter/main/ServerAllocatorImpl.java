/**
 * 
 */
package com.vimukti.accounter.main;

/**
 * @author Prasanna Kumar G
 * 
 */
public class ServerAllocatorImpl implements IServerAllocator {

	// @Override
	// public Server allocateServer(int companyType, String companyName,
	// String sourceAddress) {
	//
	// Session session = HibernateUtil.openSession();
	// Transaction transaction = null;
	// try {
	// transaction = session.beginTransaction();
	// List list = session.getNamedQuery("get.Prioritize.Servers").list();
	// if (!list.isEmpty()) {
	// return (Server) list.get(0);
	// }
	// Server server = new Server(
	// ServerConfiguration.getMainServerDomain());
	// session.save(server);
	// transaction.commit();
	// return server;
	// } catch (Exception e) {
	// e.printStackTrace();
	// if (transaction != null) {
	// transaction.rollback();
	// }
	// } finally {
	// session.close();
	// }
	// return null;
	// }

}
