package com.vimukti.accounter.main;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Appender;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import com.vimukti.accounter.mobile.AccounterChatServer;
import com.vimukti.accounter.mobile.AccounterMobileException;
import com.vimukti.accounter.mobile.MobileServer;
import com.vimukti.accounter.mobile.store.CommandsFactory;
import com.vimukti.accounter.mobile.store.PatternStore;

public class ServerMain extends Main {
	private static boolean isDebug;

	public static void main(String[] args) throws Exception {

		isDebug = checkDebugMode(args);
		ServerConfiguration.isDebugMode = isDebug;
		String configFile = getArgument(args, "-config");
		ServerConfiguration.init(configFile);
		initLogger();

		// if (ServerConfiguration.isDesktopApp()) {
		// openInBrowser();
		// }

		JettyServer.start(ServerConfiguration.getMainServerPort(),
				ServerConfiguration.getKeyStore(),
				ServerConfiguration.getKeyStorePassword(),
				ServerConfiguration.getKeyPassword(),
				ServerConfiguration.getSslPort());

		stratChatServers();

		JettyServer.jettyServer.join();
	}

	private static void stratChatServers() throws AccounterMobileException {
		boolean isEnableCommands = ServerConfiguration
				.isEnableConsoleChatServer();

		if (ServerConfiguration.isEnableIMChatServer()) {
			new AccounterChatServer().start();
			isEnableCommands = true;
		}

		if (ServerConfiguration.isEnableMobileChatServer()) {
			new MobileServer().strat();
			isEnableCommands = true;
		}

		if (isEnableCommands) {
			CommandsFactory.INSTANCE.reload();
			PatternStore.INSTANCE.reload();
		}
	}

	private static void openInBrowser() {
		if (!java.awt.Desktop.isDesktopSupported()) {
			return;
		}

		java.awt.Desktop desktop = java.awt.Desktop.getDesktop();

		if (!desktop.isSupported(java.awt.Desktop.Action.BROWSE)) {
			return;
		}

		try {
			java.net.URI uri = new java.net.URI("http://localhost/site/home");
			desktop.browse(uri);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void initLogger() {
		File logsdir = new File(ServerConfiguration.getLogsDir());
		if (!logsdir.exists()) {
			logsdir.mkdirs();
		}
		String path = new File(logsdir, "serverlog").getAbsolutePath();
		try {
			if (isDebug) {
				BasicConfigurator.configure(new ConsoleAppender());
			} else {
				Layout layout = new PatternLayout(
						"%d{dd MMM yyyy HH:mm:ss} %p [%c{1}] - %m %n");
				Logger.getRootLogger().addAppender(
						new DailyRollingFileAppender(layout, path,
								"'.'yyyy-MM-dd-a"));
				Appender appender = Logger.getRootLogger().getAppender(
						"console");
				appender.setLayout(layout);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		Logger.getRootLogger().setLevel(Level.INFO);
		Logger.getLogger("org.hibernate").setLevel(Level.INFO);
		Logger.getLogger("com.vimukti").setLevel(Level.INFO);

	}

}