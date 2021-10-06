package com.nitya.accounter.main;

import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.SecurityHandler;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.LocalConnector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.server.session.AbstractSessionManager;
import org.eclipse.jetty.server.session.JDBCSessionIdManager;
import org.eclipse.jetty.server.session.JDBCSessionManager;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.server.ssl.SslConnector;
import org.eclipse.jetty.server.ssl.SslSelectChannelConnector;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.webapp.WebAppContext;

public class JettyServer {
	public static Server jettyServer;

	public static void start(int port, String keyStore,
			String keyStorePassword, String keyPassword, int sslport)
			throws Exception {
		jettyServer = new Server();
		Connector connector = new SelectChannelConnector();
		connector.setPort(port);

		SessionHandler sessionHandler = new SessionHandler();

		SecurityHandler securityHandler = new ConstraintSecurityHandler();

		WebAppContext webappcontext = new WebAppContext(sessionHandler,
				securityHandler, null, null);

		webappcontext.setContextPath("/");
		webappcontext.setInitParameter("useFileMappedBuffer", "false");
		LocalConnector wsConnector = new LocalConnector();
		webappcontext.setAttribute("wsConnector", wsConnector);
		// webappcontext.setWar("webapp");

		webappcontext.getSessionHandler().getSessionManager()
				.setSessionIdPathParameterName("none");

		Resource resource = Resource.newClassPathResource("war");

		webappcontext.setBaseResource(resource);

		if (keyStore == null) {
			jettyServer
					.setConnectors(new Connector[] { connector, wsConnector });
		} else {
			jettyServer.setConnectors(new Connector[] {
					connector,
					makeSSLConnector(keyStore, keyStorePassword, keyPassword,
							sslport), wsConnector });
		}
		webappcontext.setClassLoader(JettyServer.class.getClassLoader());

		webappcontext.setAttribute("documentDomain",
				ServerConfiguration.getMainServerDomain());

		// for max post data
		webappcontext.getServletContext().getContextHandler()
				.setMaxFormContentSize(10000000);

		// HandlerCollection handlers = new HandlerCollection();
		// handlers.setHandlers(new Handler[] { webappcontext,
		// new DefaultHandler() });
		jettyServer.setHandler(webappcontext);

		if (ServerConfiguration.getSessionDbUrl() != null) {
			setJDBCSessionManager(webappcontext.getSessionHandler());
		}

		jettyServer.start();

	}

	/**
	 * <New class="org.mortbay.jetty.security.SslSocketConnector"> <Set
	 * name="Port">8443</Set> <Set name="maxIdleTime">30000</Set> <Set
	 * name="keystore"><SystemProperty name="jetty.home" default="."
	 * />/etc/keystore</Set> <Set
	 * name="password">OBF:1vny1zlo1x8e1vnw1vn61x8g1zlu1vn4</Set> <Set
	 * name="keyPassword">OBF:1u2u1wml1z7s1z7a1wnl1u2g</Set> <Set
	 * name="truststore"><SystemProperty name="jetty.home" default="."
	 * />/etc/keystore</Set> <Set
	 * name="trustPassword">OBF:1vny1zlo1x8e1vnw1vn61x8g1zlu1vn4</Set> </New>
	 * 
	 * @param keyStorePath
	 * @param password
	 * @param keyPassword
	 * 
	 * @return
	 */
	private static SslConnector makeSSLConnector(String keyStorePath,
			String password, String keyPassword, int sslPort) {
		SslContextFactory contextFactory = new SslContextFactory();
		contextFactory.setKeyStorePath(keyStorePath);
		contextFactory.setKeyStorePassword(password);
		contextFactory.setKeyManagerPassword(keyPassword);
		SslConnector connector = new SslSelectChannelConnector(contextFactory);
		connector.setPort(sslPort);
		return connector;
	}

	/**
	 * @param sessionHandler
	 */
	private static void setJDBCSessionManager(SessionHandler sessionHandler) {

		// ----------JDBCSessionIdManager-----------
		JDBCSessionIdManager idMgr = new JDBCSessionIdManager(jettyServer);
		String hostName = ServerConfiguration.getCurrentServerDomain();
		idMgr.setWorkerName(hostName.replaceAll("\\.", "_"));
		idMgr.setDriverInfo(System.getProperty("db.driver"),
				ServerConfiguration.getSessionDbUrl());
		idMgr.setScavengeInterval(60);
		jettyServer.setSessionIdManager(idMgr);

		// -----------JDBCSessionManager-------------
		JDBCSessionManager jdbcMgr = new JDBCSessionManager();
		AbstractSessionManager sessionManager = (AbstractSessionManager) sessionHandler
				.getSessionManager();

		sessionManager.getSessionCookieConfig().setDomain(
				ServerConfiguration.getServerCookieDomain());

		jdbcMgr.setSessionTrackingModes(sessionManager
				.getDefaultSessionTrackingModes());

		jdbcMgr.setHttpOnly(sessionManager.getHttpOnly());
		jdbcMgr.setCheckingRemoteSessionIdEncoding(sessionManager
				.isCheckingRemoteSessionIdEncoding());
		jdbcMgr.setMaxInactiveInterval(sessionManager.getMaxInactiveInterval());
		jdbcMgr.setNodeIdInSessionId(sessionManager.isNodeIdInSessionId());
		jdbcMgr.setRefreshCookieAge(sessionManager.getRefreshCookieAge());
		// jdbcMgr.setSaveInterval(sessionHandler.getS)
		jdbcMgr.getSessionCookieConfig().setSecure(
				sessionManager.getSessionCookieConfig().isSecure());
		jdbcMgr.getSessionCookieConfig().setComment(
				sessionManager.getSessionCookieConfig().getComment());
		jdbcMgr.getSessionCookieConfig().setDomain(
				sessionManager.getSessionCookieConfig().getDomain());
		jdbcMgr.getSessionCookieConfig().setMaxAge(
				sessionManager.getSessionCookieConfig().getMaxAge());
		jdbcMgr.getSessionCookieConfig().setName(
				sessionManager.getSessionCookieConfig().getName());
		jdbcMgr.getSessionCookieConfig().setPath(
				sessionManager.getSessionCookieConfig().getPath());

		jdbcMgr.setSessionIdManager(jettyServer.getSessionIdManager());

		sessionHandler.setSessionManager(jdbcMgr);

	}

	public static void stop() {
		try {
			jettyServer.stop();
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

}
