package com.nitya.accounter.servlets;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.n3.nanoxml.XMLElement;
import net.n3.nanoxml.XMLWriter;

import org.hibernate.Session;

import com.nitya.accounter.core.AccounterThreadLocal;
import com.nitya.accounter.core.ClientConvertUtil;
import com.nitya.accounter.core.Company;
import com.nitya.accounter.core.User;
import com.nitya.accounter.main.CompanyPreferenceThreadLocal;
import com.nitya.accounter.main.ServerGlobal;
import com.nitya.accounter.utils.HibernateUtil;
import com.nitya.accounter.web.client.IGlobal;
import com.nitya.accounter.web.client.core.ClientCompanyPreferences;
import com.nitya.accounter.web.client.core.ClientUser;
import com.nitya.accounter.web.client.core.CountryPreferences;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.ui.Menu;
import com.nitya.accounter.web.client.ui.MenuBar;
import com.nitya.accounter.web.client.ui.MenuItem;
import com.nitya.accounter.web.server.util.ICountryPreferences;

public class NewMenuServlet extends BaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private IGlobal iGlobal;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		HttpSession session = req.getSession();

		if (session != null) {

			String emailId = (String) session.getAttribute(EMAIL_ID);

			Session hibernateSession = HibernateUtil.getCurrentSession();

			Company company = getCompany(req);

			try {

				resp.setContentType("text/xml");
				XMLWriter writer = new XMLWriter(resp.getOutputStream());
				generateXML(writer, company, emailId);

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				// if (hibernateSession.isOpen()) {
				// hibernateSession.close();
				// }
			}
		}

	}

	private void generateXML(XMLWriter writer, Company company, String emailId)
			throws IOException {
		iGlobal = new ServerGlobal();

		if (company == null) {
			return;
		}
		User user = null;
		Set<String> features = company.getCreatedBy().getClient()
				.getClientSubscription().getSubscription().getFeatures();
		Iterator<User> iterator = company.getUsers().iterator();

		while (iterator.hasNext()) {
			User next = iterator.next();
			if (next.getClient().getEmailId().endsWith(emailId)) {
				user = next;
				break;
			}
		}
		if (user == null) {
			return;
		}
		AccounterThreadLocal.set(user);

		ClientCompanyPreferences preferences = null;
		try {
			preferences = new ClientConvertUtil().toClientObject(
					company.getPreferences(), ClientCompanyPreferences.class);
			CompanyPreferenceThreadLocal.set(preferences);
		} catch (AccounterException e) {
			e.printStackTrace();
		}

		XMLElement mainElement = new XMLElement("AccounterMenu");

		addHeader(mainElement);

		ClientUser clientUser = null;

		try {
			clientUser = new ClientConvertUtil().toClientObject(user,
					ClientUser.class);
		} catch (AccounterException e) {
			e.printStackTrace();
		}

		createMenu(mainElement, preferences, clientUser,
				company.getCountryPreferences(), features);

		writer.write(mainElement);
	}

	private void createMenu(XMLElement mainElement,
			ClientCompanyPreferences preferences, ClientUser clientUser,
			ICountryPreferences iCountryPreferences, Set<String> features)
			throws IOException {

		MenuBar menuBar = new MenuBar();

		CountryPreferences countryPreferences = new CountryPreferences();
		countryPreferences.setCompanyFields(iCountryPreferences
				.getCompanyFields());
		countryPreferences.setCustomerFields(iCountryPreferences
				.getCustomerFields());
		countryPreferences.setPreferredCurrency(iCountryPreferences
				.getPreferredCurrency());
		countryPreferences.setSalesTaxAvailable(iCountryPreferences
				.isSalesTaxAvailable());
		countryPreferences.setServiceTaxAvailable(iCountryPreferences
				.isServiceTaxAvailable());
		countryPreferences.setStates(iCountryPreferences.getStates());
		countryPreferences
				.setTDSAvailable(iCountryPreferences.isTDSAvailable());
		countryPreferences
				.setVatAvailable(iCountryPreferences.isVatAvailable());
		countryPreferences.setVendorFields(iCountryPreferences
				.getVendorFields());

		menuBar.setPreferencesandPermissions(preferences, clientUser,
				countryPreferences, features);
		List<Menu> menus = menuBar.getMenus();

		generateMenu(mainElement, menus);
	}

	private void generateMenu(XMLElement mainElement, List<Menu> menus)
			throws IOException {

		for (Menu menu : menus) {

			XMLElement mainMenu = mainMenu(mainElement, menu.getTitle());

			for (MenuItem menuItem : menu.getMenuItems()) {

				String title = menuItem.getTitle();
				String urlToken = "company/accounter#" + menuItem.getUrlToken();
				String shortCut = menuItem.getShortcut();
				if (title == null) {
					separator(mainMenu);
				} else if (menuItem instanceof Menu) {

					Menu subMenu = (Menu) menuItem;

					XMLElement menu2 = mainMenu(mainMenu, subMenu.getTitle());

					for (MenuItem subMenuItem : subMenu.getMenuItems()) {

						String subTitle = subMenuItem.getTitle();
						String subUrlToken = "company/accounter#"
								+ subMenuItem.getUrlToken();
						String subshortCut = subMenuItem.getShortcut();

						if (subTitle == null) {
							separator(menu2);
						} else if (subMenuItem instanceof Menu) {

							Menu subMenu3 = (Menu) subMenuItem;

							XMLElement menu4 = mainMenu(menu2,
									subMenu3.getTitle());

							// XMLElement menu4 = subMenu(menu2,
							// subMenu3.getTitle(), "", "");

							for (MenuItem subMenuItem3 : subMenu3
									.getMenuItems()) {

								String subTitle3 = subMenuItem3.getTitle();
								String subUrlToken3 = "company/accounter#"
										+ subMenuItem3.getUrlToken();
								String subshortCut3 = subMenuItem3
										.getShortcut();

								if (subTitle3 == null) {
									separator(menu4);
								} else {
									menuItem(menu4, subTitle3, subUrlToken3,
											subshortCut3);
								}
							}
						}

						else {
							menuItem(menu2, subTitle, subUrlToken, subshortCut);
						}
					}

				} else {
					menuItem(mainMenu, title, urlToken, shortCut);
				}
			}

		}
	}

	// private void childSubMenu(XMLElement menu3, String subTitle2,
	// String subUrlToken2) {
	//
	// XMLElement element = new XMLElement("ChildSubMenu");
	// element.setAttribute("text", subTitle2);
	// element.setContent(subUrlToken2);
	//
	// menu3.addChild(element);
	// }

	private void addHeader(XMLElement mainElement) throws IOException {

		XMLElement logoutElement = new XMLElement("MenuItem");
		logoutElement.setAttribute("text", iGlobal.messages().logout());
		logoutElement.setContent("main/logout");

		XMLElement companiesElement = new XMLElement("MenuItem");
		companiesElement.setAttribute("text", iGlobal.messages().companies());
		companiesElement.setContent("main/companies");

		XMLElement passwordElement = new XMLElement("MenuItem");
		passwordElement.setAttribute("text", iGlobal.messages().userDetails());
		passwordElement.setContent("company/accounter#userDetails");

		mainElement.addChild(logoutElement);
		mainElement.addChild(companiesElement);
		mainElement.addChild(passwordElement);

	}

	private void menu(XMLElement mainElement, String text, String value,
			String shortcut) throws IOException {

		XMLElement element = new XMLElement("Menu");
		element.setAttribute("text", text);
		element.setAttribute("shortcut", shortcut);
		element.setContent(value);

		mainElement.addChild(element);
	}

	private void menu(XMLElement mainElement, String text,
			StringBuilder subMenuValue) throws IOException {

		menuItem(mainElement, text, subMenuValue.toString());
	}

	private XMLElement menuItem(XMLElement mainElement, String text,
			String value, String shortcut) throws IOException {

		XMLElement element = new XMLElement("MenuItem");
		element.setAttribute("text", text);
		element.setContent(value);
		element.setAttribute("shortcut", shortcut);

		mainElement.addChild(element);

		return element;
	}

	private XMLElement menuItem(XMLElement mainElement, String text,
			String value) throws IOException {

		XMLElement element = new XMLElement("MenuItem");
		element.setAttribute("text", text);
		element.setContent(value);
		mainElement.addChild(element);

		return element;
	}

	// private void subMenu(XMLElement mainElement, String text, String value)
	// throws IOException {
	//
	// XMLElement element = new XMLElement("SubMenu");
	// element.setAttribute("text", text);
	// element.setContent(value);
	//
	// mainElement.addChild(element);
	// }
	//
	// private XMLElement subMenu(XMLElement mainElement, String text,
	// String sortcut, String value) throws IOException {
	//
	// XMLElement element = new XMLElement("SubMenu");
	// element.setAttribute("text", text);
	// element.setAttribute("shortcut", sortcut);
	// element.setContent(value);
	//
	// mainElement.addChild(element);
	// return element;
	// }

	private void separator(XMLElement mainElement) throws IOException {

		XMLElement element = new XMLElement("Seperator");

		mainElement.addChild(element);
	}

	private XMLElement mainMenu(XMLElement mainElement, String text)
			throws IOException {

		XMLElement element = new XMLElement("Menu");
		element.setAttribute("text", text);
		mainElement.addChild(element);

		return element;

	}
}
