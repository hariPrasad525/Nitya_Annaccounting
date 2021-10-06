package com.nitya.accounter.web.client.ui;

import java.util.List;
import java.util.function.Consumer;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.StackLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientCompanyPreferences;
import com.nitya.accounter.web.client.core.CountryPreferences;
import com.nitya.accounter.web.client.externalization.AccounterMessages;
import com.nitya.accounter.web.client.externalization.AccounterMessages2;
import com.nitya.accounter.web.client.ui.IMenuFactory.IMenu;
import com.nitya.accounter.web.client.ui.IMenuFactory.IMenuBar;

public class AccounterMenuBar extends FlowPanel {

	private ClientCompanyPreferences preferences = Global.get().preferences();
	private final AccounterMessages messages = Global.get().messages();
	private final AccounterMessages2 messages2 = Global.get().messages2();
	
	private final IMenuFactory factory;
	public static String oldToken;
	private MenuBar accounterMenuBar;
	private boolean firstTimeLoaded = false;

	public AccounterMenuBar(IMenuFactory factory) {
		this.factory = factory;

		setStyleName("MENU_BAR_BG");

		CountryPreferences countryPreferences = Accounter.getCompany().getCountryPreferences();

		accounterMenuBar = new MenuBar();
		accounterMenuBar.setPreferencesandPermissions(preferences, Accounter.getUser(), countryPreferences,
				Accounter.getFeatures());

		//generateMenu(accounterMenuBar.getMenus());
		mainMenu();
		//generatePanel(accounterMenuBar.getMenus());
	}
	
	public StackLayoutPanel getStackPanel() {
		return generatePanel(accounterMenuBar.getMenus());
	}
	
	private void mainMenu() {
		
		IMenuBar menuBar = factory.createMenuBar();
		DesktopCustomMenuBar mainMenuBar = (DesktopCustomMenuBar) menuBar;
		
		Command cmd1 = new Command() {
			@Override
			public void execute() {
				Location.assign("/main/companies?type=list");				
			}
		};
		mainMenuBar.addMenuItem(Global.get().messages().companies(), cmd1);
		
		
		boolean ispayRollOnly = Accounter.getCompany().getPreferences().isPayrollOnly();
		int isAttendanceOnly = Accounter.getUser().getPermissions().getAllowUsersToAttendance();
		
		UrlCommand cmd = new UrlCommand(HistoryTokens.DASHBOARD);
		//TODO:hardcoded label.
		mainMenuBar.addMenuItem("Home", cmd);
		
		/*
		 * if(isAttendanceOnly==1) { cmd = new UrlCommand(HistoryTokens.CLIENTPAYROLL);
		 * mainMenuBar.addMenuItem(messages2.clientPayroll(),cmd); }
		 */
		
		if(!ispayRollOnly && isAttendanceOnly==0) {
			cmd = new UrlCommand(HistoryTokens.CUSTOMERCENTRE);
			mainMenuBar.addMenuItem(messages.customerCentre(Global.get().Customer()), cmd);
			
			cmd = new UrlCommand(HistoryTokens.VENDORCENTRE);
			mainMenuBar.addMenuItem(messages.vendorCentre(Global.get().Vendor()), cmd);
		}
		
		if(isAttendanceOnly==0) {
		cmd = new UrlCommand(HistoryTokens.TRANSACTIONS_CENTER);
		mainMenuBar.addMenuItem(messages.transactionscenter(), cmd);
		
		cmd = new UrlCommand(HistoryTokens.REPORTHOME);
		mainMenuBar.addMenuItem(messages.reportsHome(), cmd);
		
		cmd = new UrlCommand(HistoryTokens.SETTINGS_COMPANYPREFERENCES);
		mainMenuBar.addMenuItem(messages.companyPreferences(), cmd);
		}
		add(menuBar);
		
	}

	private void generateMenu(List<Menu> menus) {

		IMenuBar menuBar = factory.createMenuBar();

		for (Menu menu : menus) {

			IMenu menuMain = factory.createMenu();

			for (MenuItem menuItem : menu.getMenuItems()) {

				String title = menuItem.getTitle();
				String urlToken = menuItem.getUrlToken();

				if (title == null) {
					menuMain.addSeparatorItem();
				} else if (menuItem instanceof Menu) {

					Menu subMenu = (Menu) menuItem;
					menuMain.addMenuItem(title, getSubMenu(subMenu));

				} else {
					UrlCommand cmd = new UrlCommand(urlToken);
					menuMain.addMenuItem(title, cmd);
				}
			}
			menuBar.addMenuItem(menu.getTitle(), menuMain);
		}
		add(menuBar);
	}

	private IMenu getSubMenu(Menu subMenu) {

		IMenu submenu = factory.createMenu();
		for (MenuItem subMenuItem : subMenu.getMenuItems()) {

			String subTitle = subMenuItem.getTitle();
			String subUrlToken = subMenuItem.getUrlToken();

			if (subTitle == null) {
				submenu.addSeparatorItem();
			} else if (subMenuItem instanceof Menu) {

				Menu anotherSubMenu = (Menu) subMenuItem;
				submenu.addMenuItem(subTitle, getSubMenu(anotherSubMenu));

			} else {
				UrlCommand cmd = new UrlCommand(subUrlToken);
				submenu.addMenuItem(subTitle, cmd);
			}
		}
		return submenu;
	}

	private StackLayoutPanel generatePanel(List<Menu> menus) {

		// Create a new stack layout panel.
		StackLayoutPanel stackPanel = new StackLayoutPanel(Unit.PX);
		stackPanel.setWidth("230px");
		stackPanel.addStyleName("leftMenuAccordin");
		stackPanel.setHeight("360px");
		//stackPanel.setHeight((Window.getClientHeight() - 100)+"px");
		
		int headerHeight = 40;
		int totalDefaultHeight = menus.size()* headerHeight;
		
		Window.addResizeHandler(new ResizeHandler() {
			@Override
			public void onResize(ResizeEvent event) {
				//	stackPanel.setHeight((Window.getClientHeight() - 100)+"px");
				setStackPanelHeight(stackPanel, totalDefaultHeight);
			}
		});
		
		stackPanel.addBeforeSelectionHandler(new BeforeSelectionHandler<Integer>() {
			
			@Override
			public void onBeforeSelection(BeforeSelectionEvent<Integer> event) {
				if (AccounterMenuBar.this.firstTimeLoaded) {
					return;
				} else {
					event.cancel();
				}
			}
		});
		
		stackPanel.addSelectionHandler(new SelectionHandler<Integer>() {
			
			@Override
			public void onSelection(SelectionEvent<Integer> event) {
				setStackPanelHeight(stackPanel, totalDefaultHeight);
				stackPanel.getHeaderWidget(event.getSelectedItem()).addStyleName("header-selected");
				stackPanel.getHeaderWidget(event.getSelectedItem()).addStyleName("fa-arrow-down");				
			}
		});
		
		for (Menu menu : menus) {
			ScrollPanel spanel = new ScrollPanel();
			VerticalPanel vPanel = new VerticalPanel();
			vPanel.addStyleName("menu-item-content");
			for (MenuItem menuItem : menu.getMenuItems()) {

				String title = menuItem.getTitle();
				String urlToken = menuItem.getUrlToken();

				if (title == null) {
					// 
				} else if (menuItem instanceof Menu) {
					Menu subMenu = (Menu) menuItem;
					vPanel.add(generateTree(subMenu, title, stackPanel, totalDefaultHeight));
				} else {
					Hyperlink link = new Hyperlink(title, false, urlToken);
					vPanel.add(link);
				}
			}
			spanel.add(vPanel);
			stackPanel.add(spanel, menu.getTitle(), headerHeight);
			stackPanel.getHeaderWidget(0).addStyleName("fas fa-building");
		}
		
		//add(stackPanel);

		stackPanel.ensureDebugId("cwStackLayoutPanel");
		this.firstTimeLoaded = true;
		return stackPanel;
	}

	private DisclosurePanel generateTree(Menu submenu, String title, StackLayoutPanel stackPanel, int totalDefaultHeight) {
		
		DisclosurePanel advancedDisclosure = new DisclosurePanel(title);
		advancedDisclosure.setAnimationEnabled(true);
		advancedDisclosure.ensureDebugId("cwDisclosurePanel");
		VerticalPanel mailPanel = new VerticalPanel();
		mailPanel.addStyleName("menu-item-content");
		//Tree mailPanel = new Tree();
		//mailPanel.setAnimationEnabled(true);
		//TreeItem section = parent == null ? mailPanel.addTextItem(title) : parent.addTextItem(title);
		mailPanel.setStyleName("tree-item-parent");
		for (MenuItem subMenuItem : submenu.getMenuItems()) {

			String subTitle = subMenuItem.getTitle();
			String subUrlToken = subMenuItem.getUrlToken();

			if (subTitle == null) {
				// submenu.addSeparatorItem();
			} else if (subMenuItem instanceof Menu) {

				Menu anotherSubMenu = (Menu) subMenuItem;
				mailPanel.add(generateTree(anotherSubMenu, subTitle, stackPanel, totalDefaultHeight));

			} else {
				Hyperlink link = new Hyperlink(subTitle, false, subUrlToken);
				mailPanel.add(link);
			}
		}
		
		advancedDisclosure.setContent(mailPanel);
		advancedDisclosure.addCloseHandler(new CloseHandler<DisclosurePanel>() {
			
			@Override
			public void onClose(CloseEvent<DisclosurePanel> event) {
				new Timer() {
					@Override
					public void run() {
						setStackPanelHeight(stackPanel, totalDefaultHeight - 11);
					}
				}.schedule(300);
			}
		});
		
		advancedDisclosure.addOpenHandler(new OpenHandler<DisclosurePanel>() {
			
			@Override
			public void onOpen(OpenEvent<DisclosurePanel> event) {
				new Timer() {

					@Override
					public void run() {
						setStackPanelHeight(stackPanel, totalDefaultHeight + 11);
					}
				}.schedule(300);
			}
		});

		return advancedDisclosure;
	}
	
	private void setStackPanelHeight(StackLayoutPanel stackPanel, int totalDefaultHeight) {
		if (stackPanel.getVisibleWidget() != null) {
			ScrollPanel scrollPanel = (ScrollPanel) stackPanel.getVisibleWidget();
			if (scrollPanel.getWidget().getOffsetHeight() > 0) {
			   stackPanel.setHeight(scrollPanel.getWidget().getOffsetHeight() + totalDefaultHeight +"px");
			}
		}
	}

	public ClientCompanyPreferences getPreferences() {
		return preferences;
	}

	public void setPreferences(ClientCompanyPreferences preferences) {
		this.preferences = preferences;
	}

}