package com.vimukti.accounter.web.client.portlet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.user.client.ui.CheckBox;
import com.vimukti.accounter.web.client.core.ClientPortletConfiguration;
import com.vimukti.accounter.web.client.core.ClientPortletPageConfiguration;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;

public class PortletPageConfigureDialog extends
		BaseDialog<ClientPortletConfiguration> {
	private List<CheckBox> portletNameCheckBoxs;
	private final List<ClientPortletConfiguration> prefferedPortlets;
	private ClientPortletPageConfiguration config;
	private final PortletPage page;

	public PortletPageConfigureDialog(String text,
			ClientPortletPageConfiguration config, PortletPage page) {
		super(text);
		this.config = config;
		this.page = page;
		this.prefferedPortlets = PortletFactory.get()
				.getPrefferedConfiguration(page.getName())
				.getPortletConfigurations();
		this.getElement().setId("PortletPageConfigureDialog");
		createControl();
	}

	private void createControl() {
		StyledPanel portletNamesPanel = new StyledPanel("portletNamesPanel");
		portletNameCheckBoxs = new ArrayList<CheckBox>();
		for (int i = 0; i < prefferedPortlets.size(); i++) {
			CheckBox checkBox = new CheckBox(PortletFactory.get()
					.getPortletName(prefferedPortlets.get(i)));
			portletNameCheckBoxs.add(i, checkBox);
			portletNamesPanel.add(checkBox);
		}
		updateCheckBoxesData(getConfigSettingsData());
		this.bodyLayout.add(portletNamesPanel);
	}

	protected boolean onOK() {
		config.getPortletConfigurations().clear();
		getCheckBoxesData();
		page.haveToRefresh = true;
		page.updatePortletPage();
		return true;
	}

	private void getCheckBoxesData() {
		for (int i = 0; i < portletNameCheckBoxs.size(); i++) {
			if (portletNameCheckBoxs.get(i).getValue()) {
				config.getPortletConfigurations().add(prefferedPortlets.get(i));
			}
		}
	}

	private Map<ClientPortletConfiguration, Boolean> getConfigSettingsData() {
		Map<ClientPortletConfiguration, Boolean> notAddedConfigurations = new HashMap<ClientPortletConfiguration, Boolean>();
		Map<String, ClientPortletConfiguration> thisPageConfigMap = new HashMap<String, ClientPortletConfiguration>();
		List<ClientPortletConfiguration> thisPageConfigurations = config
				.getPortletConfigurations();
		for (ClientPortletConfiguration configuration : thisPageConfigurations) {
			thisPageConfigMap.put(configuration.getName(), configuration);
		}
		Iterator<ClientPortletConfiguration> iterator = prefferedPortlets
				.iterator();
		while (iterator.hasNext()) {
			ClientPortletConfiguration configuration = iterator.next();
			if (thisPageConfigMap.containsKey(configuration.getName())) {
				notAddedConfigurations.put(configuration, true);
			} else {
				notAddedConfigurations.put(configuration, false);
			}
		}
		return notAddedConfigurations;
	}

	@Override
	public void setFocus() {

	}

	public void updateCheckBoxesData(
			Map<ClientPortletConfiguration, Boolean> mapportletNameCheckBoxs) {
		Set<ClientPortletConfiguration> entries = mapportletNameCheckBoxs
				.keySet();
		Iterator<ClientPortletConfiguration> it = entries.iterator();
		while (it.hasNext()) {
			ClientPortletConfiguration entry = it.next();
			for (CheckBox checkBox : portletNameCheckBoxs) {
				if (checkBox.getText().equals(
						PortletFactory.get().getPortletName(entry))) {
					checkBox.setValue(mapportletNameCheckBoxs.get(entry));
				}
			}
		}
	}

	public ClientPortletPageConfiguration getConfig() {
		return config;
	}

	public void setConfig(ClientPortletPageConfiguration config) {
		this.config = config;
	}

	@Override
	protected boolean onCancel() {
		return true;
	}
}
