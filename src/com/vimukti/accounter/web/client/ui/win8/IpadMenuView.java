package com.vimukti.accounter.web.client.ui.win8;

import java.util.List;
import java.util.Set;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientUser;
import com.vimukti.accounter.web.client.core.CountryPreferences;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.Menu;
import com.vimukti.accounter.web.client.ui.MenuBar;
import com.vimukti.accounter.web.client.ui.MenuItem;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.ButtonBar;

public class IpadMenuView extends BaseView {

	private final ClientCompanyPreferences preferences = Global.get()
			.preferences();

	private StyledPanel subMenuPanel;

	private StyledPanel mainMenuPanel;

	@Override
	public void init() {
		super.init();
		CountryPreferences countryPreferences = Accounter.getCompany()
				.getCountryPreferences();
		setPreferencesandPermissions(preferences, Accounter.getUser(),
				countryPreferences, Accounter.getFeatures());

	}

	public void setPreferencesandPermissions(
			ClientCompanyPreferences preferences, ClientUser clientUser,
			CountryPreferences countryPreferences, Set<String> features) {

		getMenu(countryPreferences);
	}

	private void getMenu(CountryPreferences countryPreferences) {

		mainMenuPanel = new StyledPanel("mainMenu");
		subMenuPanel = new StyledPanel("subMenuPanel");

		MenuBar accounterMenuBar = new MenuBar();
		accounterMenuBar.setPreferencesandPermissions(preferences,
				Accounter.getUser(), countryPreferences,
				Accounter.getFeatures());

		List<Menu> menus = accounterMenuBar.getMenus();

		createMenus(menus);

		this.add(mainMenuPanel);
		this.add(subMenuPanel);

		addStyleName("mainMenuView");

	}

	private void createMenus(List<Menu> menus) {

		for (int i = 0; i < menus.size(); i++) {

			final Menu menu = (Menu) menus.get(i);

			if (i == 0) {
				subMenuPanel.add(addMenuItems(menu.getMenuItems()));
			}

			final Button button = new Button(menu.getTitle());
			button.setStyleName("menuName");
			button.addMouseOverHandler(new MouseOverHandler() {

				@Override
				public void onMouseOver(MouseOverEvent event) {
					button.addStyleName("mouseOver");
				}
			});
			button.addMouseOutHandler(new MouseOutHandler() {

				@Override
				public void onMouseOut(MouseOutEvent event) {
					if (button.getStyleName().endsWith("mouseOver")) {
						button.removeStyleName("mouseOver");
					}
				}
			});

			button.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {

					for (int i = 0; i < mainMenuPanel.getWidgetCount(); i++) {
						Button button = (Button) mainMenuPanel.getWidget(i);
						button.removeStyleName("onClick");
					}
					button.addStyleName("onClick");
					subMenuPanel.clear();
					subMenuPanel.add(addMenuItems(menu.getMenuItems()));
				}
			});

			mainMenuPanel.add(button);
		}

	}

	protected StyledPanel addMenuItems(List<MenuItem> menuItems) {

		final StyledPanel settingdListForm = new StyledPanel("subMenuForm");

		for (int i = 0; i < menuItems.size(); i++) {
			MenuItem menuItem = menuItems.get(i);

			if (menuItem.getTitle() != null) {
				if (menuItem.getUrlToken() != null) {
					W8MenuItem companySettingsItem = new W8MenuItem(
							menuItem.getTitle(), "", menuItem.getUrlToken());
					settingdListForm.add(companySettingsItem);
				} else {
					settingdListForm.add(getSubmenu(menuItem));
				}
			}
		}

		return settingdListForm;
	}

	private StyledPanel getSubmenu(MenuItem menuItem) {

		final StyledPanel panel = new StyledPanel("childpanel");
		Menu menu = (Menu) menuItem;
		Label label = new Label(menu.getTitle());
		label.addStyleName("subMenuChild");
		panel.add(label);
		List<MenuItem> menuItems = menu.getMenuItems();
		for (int i = 0; i < menuItems.size(); i++) {
			MenuItem item = menuItems.get(i);

			if (item.getTitle() != null) {
				if (item.getUrlToken() != null) {
					W8MenuItem companySettingsItem = new W8MenuItem(
							item.getTitle(), "", item.getUrlToken());
					panel.add(companySettingsItem);
				} else {
					panel.add(getSubmenu(item));
				}
			}
		}

		return panel;

	}

	@Override
	public void deleteFailed(AccounterException caught) {
	}

	@Override
	public void deleteSuccess(IAccounterCore result) {

	}

	@Override
	public String getViewTitle() {
		return "Menus";
	}

	@Override
	public List getForms() {
		return null;
	}

	@Override
	public void setFocus() {

	}

	@Override
	protected void createButtons() {

	}

}
