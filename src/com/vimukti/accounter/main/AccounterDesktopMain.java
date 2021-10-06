package com.vimukti.accounter.main;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.browser.OpenWindowListener;
import org.eclipse.swt.browser.StatusTextEvent;
import org.eclipse.swt.browser.StatusTextListener;
import org.eclipse.swt.browser.WindowEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.vimukti.accounter.core.Subscription;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.externalization.AccounterMessages2;

public class AccounterDesktopMain extends Main {

	private static Boolean isDebug;
	private static TrayItem item;
	private static Browser browser;
	protected static boolean isBrowserFocused;
	private static boolean blinkControl;
	private static int port;
	private static Menu defaultMenu;
	private static Menu menuBar;
	private static Shell windowShell, shell;
	private static MenuItem fileMenuHeader;
	public static final String LOCAL_HOST = "127.0.0.1";
	private static final String MENU = "Menu";
	private static final String SEPERATOR = "Seperator";
	private static final String MENU_ITEM = "MenuItem";
	private static Thread blinker;

	public static void main(String[] args) {
		isDebug = checkDebugMode(args);
		ServerConfiguration.isDebugMode = isDebug;

		try {
			// Load the configuration from the file
			String configFile = getArgument(args, "-config");
			ServerConfiguration.init(configFile);

			// Show the System Tray
			// SystemTrayIcon trayIcon = new SystemTrayIcon();

			// Run Jetty Server
			port = generateRandomNumber();
			System.out.println(port);
			JettyServer.start(port, null, null, null, 0);

			// Open URL in Browser
			// trayIcon.openBrowser(trayIcon.getUrl());

			Display display = new Display();
			shell = new Shell(display);
			windowShell = shell;
			shell.setMaximized(true);
			shell.setText("Anna Accounting Local");
			final Image image = new Image(null, "./Accounter_logo.png");
			shell.setImage(image);
			menuBar = new Menu(shell, SWT.BAR);
			defaultMenu = new Menu(shell, SWT.DROP_DOWN);

			createMenu(shell);

			GridLayout layout = new GridLayout(1, false);
			layout.marginWidth = 0;
			layout.marginHeight = 0;

			shell.setLayout(layout);

			String os = System.getProperty("os.name");

			if (os.toLowerCase().contains("windows")) {
				initXULRunner();
				browser = new Browser(shell, SWT.MOZILLA);
			} else {
				// initXULRunner();
				browser = new Browser(shell, SWT.NONE);
			}

			// Use Mozilla browser
			new BrowserFunction(browser, "MacReload") {

				@Override
				public Object function(final Object[] arguments) {
					resetMenu();
					return null;
				}
			};

			browser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
					1, 1));

			final Label label = new Label(shell, SWT.LEFT);
			label.setLayoutData(new GridData(SWT.FILL, SWT.LEFT, true, false,
					1, 1));
			label.setText("Status");

			// Browser browser = new Browser(shell, SWT.NONE);
			browser.addStatusTextListener(new StatusTextListener() {

				@Override
				public void changed(StatusTextEvent arg0) {
					label.setText(arg0.text);
				}
			});
			browser.addLocationListener(new LocationListener() {
				final String[] accept = { "127.0.0.1:" + port, "about:blank" };

				@Override
				public void changing(LocationEvent event) {
					String location = event.location;
					for (String str : accept) {
						if (location.contains(str)) {
							return;
						}
					}
					Program.launch(location);
					event.doit = false;
				}

				@Override
				public void changed(LocationEvent event) {
					String location = event.location;
					if (!location.contains("/accounter")) {
						resetMenu();
					}

				}
			});
			browser.addOpenWindowListener(new OpenWindowListener() {

				@Override
				public void open(WindowEvent event) {
					event.browser = browser;
				}
			});
			// Navigate to local url
			browser.setUrl(getUrl());

			browser.getShell().addShellListener(new ShellListener() {
				@Override
				public void shellActivated(ShellEvent arg0) {
					isBrowserFocused = true;
					blinkControl = false;
				}

				@Override
				public void shellDeactivated(ShellEvent arg0) {
					isBrowserFocused = false;
				}

				@Override
				public void shellClosed(ShellEvent arg0) {
				}

				@Override
				public void shellDeiconified(ShellEvent arg0) {
				}

				@Override
				public void shellIconified(ShellEvent arg0) {
				}
			});

			// browser.setUrl("http://defbiz.bizantra.com");
			// browser.setUrl("http://youtube.com");

			shell.setVisible(true);
			shell.open();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
			display.close();
			JettyServer.stop();

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}

	}

	protected static String getUrl() {
		StringBuilder builder = new StringBuilder();
		builder.append("http://");
		builder.append(LOCAL_HOST);
		builder.append(":");
		builder.append(port);
		return builder.toString();
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

	private static void initXULRunner() {
		try {
			File file = new File("xulrunner");
			String path = file.getAbsolutePath();
			System.out.println(path);
			System.setProperty("org.eclipse.swt.browser.XULRunnerPath", path); //$NON-NLS-1$
		} catch (Exception e) {
			// log the exception
			e.printStackTrace();
		}

	}

	private static void createMenu(final Shell shell) {
		Display display = Display.getCurrent();
		final Tray tray = display.getSystemTray();
		item = new TrayItem(tray, SWT.NONE);
		final Image image = new Image(null, "./Accounter_logo.png");

		item.setImage(image);

		shell.setMenuBar(menuBar);

		fileMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
		fileMenuHeader.setText("&Anna Accounting");

		fileMenuHeader.setMenu(defaultMenu);

		new MenuItem(defaultMenu, SWT.SEPARATOR);

		MenuItem fileExitItem = new MenuItem(defaultMenu, SWT.PUSH);
		fileExitItem.setText("&Exit");
		fileExitItem.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event arg0) {
				tray.dispose();
				shell.dispose();
			}
		});

		MenuItem helpMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
		helpMenuHeader.setText("&Help");

		final Menu menu = new Menu(shell, SWT.POP_UP);
		MenuItem menuItem = new MenuItem(menu, SWT.PUSH);

		menuItem.setText("About");
		menuItem.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event event) {
				MessageBox messageBox = new MessageBox(shell,
						SWT.ICON_INFORMATION);
				messageBox.setText("Information");
				messageBox.setMessage("Anna Accounting");
				messageBox.open();
			}
		});
		menuItem = new MenuItem(menu, SWT.PUSH);
		menuItem.setText("Exit");
		menuItem.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event event) {
				tray.dispose();
				shell.dispose();
			}
		});

		item.addListener(SWT.MenuDetect, new Listener() {
			public void handleEvent(org.eclipse.swt.widgets.Event event) {
				menu.setVisible(true);
			}
		});
		item.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				browser.getShell().setMaximized(true);
				browser.getShell().setFocus();
				isBrowserFocused = true;
				blinkControl = false;
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}
		});

	}

	public static void resetMenu() {
		removePrevMenus();
		String menuDoc = getMenuDocument();
		if (menuDoc != null && !menuDoc.isEmpty()) {
			System.out.println(menuDoc);
			createAccounterDesktopMenu(menuDoc, windowShell);
		}
	}

	private static void removePrevMenus() {
		// TODO Delete all menus other than default
		int count = menuBar.getItemCount();
		for (int x = 1; x < count - 1; x++) {
			MenuItem item = menuBar.getItem(1);
			System.out.println("deleting: " + item.getText());
			item.dispose();
		}
		count = defaultMenu.getItemCount();
		for (int x = 0; x < count - 2; x++) {
			MenuItem item = defaultMenu.getItem(0);
			System.out.println("deleting: " + item.getText());
			item.dispose();
		}
	}

	public static String getMenuDocument() {
		URLConnection openConnection = null;
		StringBuffer buffer = null;
		try {
			URL url = new URL(getUrl() + "/desktop/mac/newmenu");
			openConnection = url.openConnection();
			openConnection.setDoOutput(true);
			String cookie = (String) browser
					.evaluate("return document.cookie;");
			openConnection.setRequestProperty("Cookie", cookie);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					openConnection.getInputStream()));
			buffer = new StringBuffer();
			for (String s = in.readLine(); s != null; s = in.readLine()) {
				buffer.append(s);
			}
			in.close();
			return buffer.toString();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private static Menu createAccounterDesktopMenu(String xml, Shell shell) {
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder builder = null;
		try {
			builder = builderFactory.newDocumentBuilder();
			Document doc = builder.parse(new ByteArrayInputStream(xml
					.getBytes("UTF-8")));
			Element menuElement = (Element) doc.getFirstChild();

			NodeList childNodes = menuElement.getChildNodes();
			int index = 1;
			for (int x = 0; x < childNodes.getLength(); x++) {
				Element node = (Element) childNodes.item(x);
				if (node.getTagName().equals(MENU)) {
					createMainMenu(menuBar, node, shell, index++);
				} else if (node.getTagName().equals(MENU_ITEM)) {
					createMenuItem(defaultMenu, node, 0);
				}
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private static MenuItem createMainMenu(Menu parent, Element menuElement,
			Shell shell, int index) {
		MenuItem menuHeader = new MenuItem(parent, SWT.CASCADE, index);
		menuHeader.setText(menuElement.getAttribute("text"));
		Menu menu = new Menu(shell, SWT.DROP_DOWN);
		NodeList childNodes = menuElement.getChildNodes();
		for (int x = 0; x < childNodes.getLength(); x++) {
			Element node = (Element) childNodes.item(x);
			if (node.getTagName().equals(MENU_ITEM)) {
				createMenuItem(menu, node, x);
			} else if (node.getTagName().equals(SEPERATOR)) {
				new MenuItem(menu, SWT.SEPARATOR, x);
			} else if (node.getTagName().equals(MENU)) {
				createMainMenu(menu, node, shell, x);
			}
		}
		menuHeader.setMenu(menu);
		return menuHeader;
	}

	private static MenuItem createMenuItem(Menu menu, final Element node,
			int index) {
		MenuItem menuItem = new MenuItem(menu, SWT.PUSH, index);
		menuItem.setText(node.getAttribute("text"));
		menuItem.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				browser.setUrl(getUrl() + "/" + node.getTextContent());
			}

		});
		return menuItem;
	}

	private static int generateRandomNumber() {
		Random random = new Random();
		int port = random.nextInt(64535);
		port = port + 1000;
		return port;
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

	public static void userLockedOrDeleted() {

		Display.getDefault().asyncExec(new Runnable() {

			@Override
			public void run() {
				System.out.println("showing lockout Page");
				if (browser != null && !browser.isDisposed()) {
					System.out.println("showing lockout Page");
					browser.setUrl(getUrl() + "/lockOrDeleteUser");
				}
			}
		});
	}

	public static void blink() {
		blinkControl = true;
		if ((blinker != null && blinker.getState().equals(
				Thread.State.TIMED_WAITING))
				|| isBrowserFocused) {
			return;
		}
		blinker = new Thread() {
			private boolean alternator = false;
			private Image bizantra = new Image(null, "./bizantraicon.ico");
			private Image bizantrablink = new Image(null,
					"./bizantrablinkicon.png");

			@Override
			public void run() {
				while (blinkControl) {
					try {
						sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					Display.getDefault().asyncExec(new Runnable() {

						@Override
						public void run() {
							if (alternator) {
								item.setImage(bizantra);
								alternator = !alternator;
							} else {
								item.setImage(bizantrablink);
								alternator = !alternator;
							}
						}
					});
				}
				Display.getDefault().asyncExec(new Runnable() {

					@Override
					public void run() {
						item.setImage(bizantra);
					}
				});
			}
		};
		blinker.start();
	}
}
