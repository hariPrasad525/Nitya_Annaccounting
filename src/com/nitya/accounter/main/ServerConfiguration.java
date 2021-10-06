package com.nitya.accounter.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ServerConfiguration {

	private static String attachmentsDir;
	private static String logsDir;
	private static String adminpassword;
	private static String tmpDir;

	public static String getTmpDir() {
		return tmpDir;
	}

	private static String paypalIdentityId;
	private static boolean isUnderMaintanance;
	private static String sessionDbUrl;

	private static String mainServerDomain;
	private static String currentServerDomain;
	private static String chatUsername;
	private static String chatpassword;
	private static int mainServerPort;
	private static int sslPort;
	private static int consoleChatServerPort;
	private static int mobileChatServerPort;
	private static int mobileSSLChatServerPort;
	public static boolean isDebugMode;
	private static String validIP;
	private static String emailLogger;

	private static boolean enableConsoleChatServer;
	private static boolean enableIMChatServer;
	private static boolean enableMobileChatServer;
	private static String encryptTmpDir;
	private static boolean uploadToRackSpace;
	private static String attchmentContainerName;
	private static String uploadAttachmentEmailId;
	private static String uploadAttachmentPassword;
	private static boolean loadMessages;
	private static boolean isSandBoxPaypal;
	private static int gracePeriod;
	private static boolean isInLive;
	private static String paypalButtonId;
	private static String certificateAlias;
	private static String paypalApiUserName;
	private static String paypalApiPassword;
	private static String paypalApiSignature;
	private static String paypalApplicationId;

	private static String livePaypalapiUserName;
	private static String livePaypalapiPassword;
	private static String livePaypalapiSignature;
	private static boolean isDesktopApp;
	private static boolean isStartUpCompleted;
	private static boolean isSetupCompleted;
	private static boolean isStopSchduleMails;
	private static String setupStatus;
	private static String keyStore;
	private static String keyStorePassword;
	private static String keyPassword;

	private static boolean runStartupScripts;
	private static boolean isScriptRunnerCompleted;
	private static boolean isRunningScript;
	private static long welcomeMailDelayTime;
	private static String mobileKeyStore;

	public static String getAdminPassword() {
		return adminpassword;
	}

	public static String getAttachmentsDir(String companyDBName) {

		if (companyDBName == null) {
			// CompanyDBNmae NEver BE NUll IN MAIN SERVER
			return null;
		}
		return attachmentsDir + companyDBName;
	}

	public static String getMainServerDomain() {
		return mainServerDomain;
	}

	public static void init(String config) {
		PropertyParser prop = new PropertyParser();
		try {
			prop.loadFile(config == null ? "config/config.ini" : config);
			System.out.println("inside server configurations");
			// prop.loadFile(keyFile == null ? "collaberconfig.ini" : keyFile);
		} catch (FileNotFoundException e) {
			System.out.println("Unable to find server config file: " + e);
			// LOG.error("Unable to find server config file: ", e);
		} catch (IOException e) {
			// LOG.error("Unable to find server config file: ", e);
			System.out.println("Unable to find server config file: " + e);
		}

		try {
			mainServerDomain = prop.getProperty("mainServerDomain",
					"app.annaccounting.com");
			currentServerDomain = prop.getProperty("currentServerDomain",
					"app.annaccounting.com");

			mainServerPort = Integer.parseInt(prop.getProperty(
					"mainServerPort", "80"));

			sslPort = Integer.parseInt(prop.getProperty("sslPort", "8443"));

			consoleChatServerPort = Integer.parseInt(prop.getProperty(
					"consoleChatServer", "9085"));

			mobileChatServerPort = Integer.parseInt(prop.getProperty(
					"mobileChatServer", "9083"));

			mobileSSLChatServerPort = Integer.parseInt(prop.getProperty(
					"mobileSSLChatServer", "9084"));

			encryptTmpDir = prop.getProperty("encryptTmpDir", null);
			paypalButtonId = prop.getProperty("paypalButtonId", "No Id");
			certificateAlias = prop.getProperty("certificateAlias", "mykey");

			setValidIP(prop.getProperty("validIP", null));
			setEmailLogger(prop.getProperty("emailLogger", null));

			attchmentContainerName = prop.getProperty("attchmentContainerName",
					null);
			uploadToRackSpace = prop.getProperty("uploadToRackSpace", "false")
					.equalsIgnoreCase("true");

			uploadAttachmentEmailId = prop.getProperty(
					"uploadAttachmentEmailId", null);
			uploadAttachmentPassword = prop.getProperty(
					"uploadAttachmentPassword", null);
			// Delay for welcome mail
			int minutes = Integer.valueOf(prop.getProperty(
					"welcomeMailDelayTime", "0"));
			welcomeMailDelayTime = minutes * 60 * 1000;

			/*
			 * mobilePort = Integer.parseInt(prop.getProperty("mobilePort",
			 * "7990"));
			 */

			logsDir = prop.getProperty("logsdir", "logs");
			System.setProperty("logsDir", logsDir);

			if (mainServerDomain.length() < 5) {
				System.err
						.println("Invalid confiuration: mainServerDomain is invalid");
				System.exit(0);
			}

			attachmentsDir = prop.getProperty("attachmentsDir", "");

			if (!new File(attachmentsDir).exists()) {
				System.err.println("Invalid configuration for attachment dir");
				System.exit(0);
			}
			String databaseUrl = prop.getProperty("databaseUrl", null);
			String databaseDriver = prop.getProperty("databaseDriver", null);
			String username = prop.getProperty("username", null);
			String password = prop.getProperty("password", null);
			String dialect = prop.getProperty("dialect", null);
			String showSql = prop.getProperty("showsql", "false");
			;
			if (databaseUrl == null || username == null || password == null
					|| dialect == null) {
				System.err.println("Invalid configuration for database");
			}
			System.setProperty("db.driver", databaseDriver);
			System.setProperty("db.url", databaseUrl);
			System.setProperty("db.user", username);
			System.setProperty("db.pass", password);
			System.setProperty("dialect", dialect);
			System.setProperty("db.showsql", showSql);
			adminpassword = prop.getProperty("adminpassword", "");
			tmpDir = prop.getProperty("tmpDir",
					System.getProperty("java.io.tmpdir", ""));

			sessionDbUrl = prop.getProperty("sessionDbUrl", null);
			paypalIdentityId = prop.getProperty("paypalIdentityId", null);
			chatUsername = prop.getProperty("chatUsername", null);
			chatpassword = prop.getProperty("chatPassword", null);
			enableConsoleChatServer = prop.getProperty(
					"enableConsoleChatServer", "false")
					.equalsIgnoreCase("true");
			enableIMChatServer = prop
					.getProperty("enableIMChatServer", "false")
					.equalsIgnoreCase("true");
			enableMobileChatServer = prop.getProperty("enableMobileChatServer",
					"false").equalsIgnoreCase("true");

			loadMessages = prop.getProperty("loadMessages", "false")
					.equalsIgnoreCase("true");

			isStopSchduleMails = prop
					.getProperty("isStopSchduleMails", "false")
					.equalsIgnoreCase("true");

			isSandBoxPaypal = (prop.getProperty("isSandBoxPaypal", "false")
					.equalsIgnoreCase("true"));
			gracePeriod = Integer
					.parseInt(prop.getProperty("gracePeriod", "0"));
			isInLive = Boolean.parseBoolean(prop
					.getProperty("isInLive", "true"));

			/**
			 * read paypal api credentials
			 */
			paypalApiUserName = prop.getProperty("apiUserName", null);
			paypalApiPassword = prop.getProperty("apiPassword", null);
			paypalApiSignature = prop.getProperty("apiSignature", null);
			paypalApplicationId = prop.getProperty("applicationID", null);

			livePaypalapiUserName = prop.getProperty("livePaypalapiUserName",
					null);
			livePaypalapiPassword = prop.getProperty("livePaypalapiPassword",
					null);
			livePaypalapiSignature = prop.getProperty("livePaypalapiSignature",
					null);

			isDesktopApp = prop.getProperty("isDesktopApp", "true").equals(
					"true");

			runStartupScripts = prop.getProperty("runStartupScripts", "false")
					.equals("true");

		} catch (NumberFormatException ne) {
			System.err
					.println("Invalid configuration for some numeric options");
			System.exit(0);
		}
		keyStore = prop.getProperty("keyStore", null);
		mobileKeyStore = prop.getProperty("mobileKeyStore", null);
		keyStorePassword = prop.getProperty("keyStorePassword", null);
		keyPassword = prop.getProperty("keyPassword", null);

	}

	public static String getLivePaypalapiUserName() {
		return livePaypalapiUserName;
	}

	public static String getLivePaypalapiPassword() {
		return livePaypalapiPassword;
	}

	public static String getLivePaypalapiSignature() {
		return livePaypalapiSignature;
	}

	public static int getGracePeriod() {
		return gracePeriod;
	}

	public static boolean isLoadMessages() {
		return loadMessages;
	}

	public static String getAttachmentsDir() {
		return attachmentsDir;
	}

	public static boolean isUnderMaintainance() {
		return isUnderMaintanance;
	}

	public static void setUnderMaintainance(boolean value) {
		isUnderMaintanance = value;
	}

	/**
	 * @return
	 */
	public static String getAccountsDir() {
		return "config/accounts";
	}

	/**
	 * @return
	 */
	public static String getConfig() {
		return "config";
	}

	public static String getDefaultCompanyDir() {
		return "config/demo";
	}

	/**
	 * @return
	 */
	public static String getSessionDbUrl() {
		return sessionDbUrl;
	}

	/**
	 * @return
	 */
	public static String getServerCookieDomain() {
		String[] domainParts = getMainServerDomain().split("\\.");
		int size = domainParts.length;
		if (size < 2) {
			return "";
		}
		return '.' + domainParts[size - 2] + '.' + domainParts[size - 1];
	}

	/**
	 * @return the currentServerDomain
	 */
	public static String getCurrentServerDomain() {
		return currentServerDomain;
	}

	/**
	 * @return the chatUsername
	 */
	public static String getChatUsername() {
		return chatUsername;
	}

	/**
	 * @param chatUsername
	 *            the chatUsername to set
	 */
	public static void setChatUsername(String chatUsername) {
		ServerConfiguration.chatUsername = chatUsername;
	}

	/**
	 * @return the chatpassword
	 */
	public static String getChatpassword() {
		return chatpassword;
	}

	/**
	 * @param chatpassword
	 *            the chatpassword to set
	 */
	public static void setChatpassword(String chatpassword) {
		ServerConfiguration.chatpassword = chatpassword;
	}

	public static int getMainServerPort() {
		return mainServerPort;
	}

	public static String getMobileStore() {
		return "config/MobileStore";
	}

	public static String getFontsDir() {
		return "config/fonts";
	}

	public static boolean isEnableConsoleChatServer() {
		return enableConsoleChatServer;
	}

	public static boolean isEnableIMChatServer() {
		return enableIMChatServer;
	}

	public static boolean isEnableMobileChatServer() {
		return enableMobileChatServer;
	}

	public static int getConsoleChatServerPort() {
		return consoleChatServerPort;
	}

	public static int getMobileChatServerPort() {
		return mobileChatServerPort;
	}

	public static int getMobileSSLChatServerPort() {
		return mobileSSLChatServerPort;
	}

	public static String getEncryptTmpDir() {
		return encryptTmpDir;
	}

	public static boolean uploadToRackSpace() {
		return uploadToRackSpace;
	}

	public static String getAttachmentsContainerName() {
		return attchmentContainerName;
	}

	public static String getUploadAttachmentEmailId() {
		return uploadAttachmentEmailId;
	}

	public static String getUploadAttachmentPassword() {
		return uploadAttachmentPassword;
	}

	public static String getValidIP() {
		return validIP;
	}

	public static void setValidIP(String validIP) {
		ServerConfiguration.validIP = validIP;
	}

	public static String getEmailLogger() {
		return emailLogger;
	}

	public static void setEmailLogger(String emailLogger) {
		ServerConfiguration.emailLogger = emailLogger;
	}

	public static String getPaypalIdentityId() {
		return paypalIdentityId;
	}

	public static boolean isSandBoxPaypal() {
		return isSandBoxPaypal;
	}

	public static void setSandBoxPaypal(boolean isSandBoxPaypal) {
		ServerConfiguration.isSandBoxPaypal = isSandBoxPaypal;
	}

	public static boolean isInLive() {
		return isInLive;
	}

	public static String getPaypalButtonId() {
		return paypalButtonId;
	}

	public static String getCertificateAlias() {
		return certificateAlias;
	}

	public static String getPaypalApiUserName() {
		return paypalApiUserName;
	}

	public static String getPaypalApiPassword() {
		return paypalApiPassword;
	}

	public static String getPaypalApiSignature() {
		return paypalApiSignature;
	}

	public static String getPaypalApplicationID() {
		return paypalApplicationId;
	}

	/**
	 * @return the logsDir
	 */
	public static String getLogsDir() {
		return logsDir;
	}

	public static boolean isDesktopApp() {
		return isDesktopApp;
	}

	public static boolean isStartUpCompleted() {
		return isStartUpCompleted;
	}

	public static void completeStartup() {
		isStartUpCompleted = true;
	}

	public static String getLicenseKeystorePWD() {
		return "***REMOVED***";
	}

	public static String getLicenseAlias() {
		return "nitya";
	}

	public static boolean isSetupCompleted() {
		return setupStatus != null && setupStatus.equals("3");
	}

	public static void setSeupStatus(String value) {
		setupStatus = value;
	}

	public static String getSetupStatus() {
		return setupStatus;
	}

	public static String getKeyStore() {
		return keyStore;
	}

	public static String getKeyStorePassword() {
		return keyStorePassword;
	}

	public static String getKeyPassword() {
		return keyPassword;
	}

	public static boolean shouldRunStartupScripts() {
		return runStartupScripts;
	}

	public static boolean isScriptRunnerCompleted() {
		return isScriptRunnerCompleted;
	}

	public static void completeScriptRunner() {
		isScriptRunnerCompleted = true;
		isRunningScript = false;
	}

	public static boolean isRunningScript() {
		return isRunningScript;
	}

	public static void startScriptRunning() {
		isRunningScript = true;
	}

	public static long getWelcomeMailDelayTime() {
		return welcomeMailDelayTime;
	}

	public static String getMobileKeyStore() {
		return mobileKeyStore;
	}

	public static int getSslPort() {
		return sslPort;
	}
}
