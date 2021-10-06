/**
 * 
 */
package com.vimukti.accounter.web.client.ui.company;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.FlowPanel;
import com.vimukti.accounter.web.client.ui.company.options.AbstractPreferenceOption;

/**
 * @author Prasanna Kumar G
 * 
 */
public class PreferencePage extends FlowPanel {

	private String title;
	public boolean canSave = false;

	private List<AbstractPreferenceOption> options = new ArrayList<AbstractPreferenceOption>();

	// private StyledPanel optionsPane = new StyledPanel();

	/**
	 * Creates new Instance
	 */
	public PreferencePage(String title) {
		this.title = title;
		// this.setHeight("350px");
		// optionsPane.setSize("100%", "100%");
		// this.add(this.optionsPane);
	}

	public void addPreferenceOption(AbstractPreferenceOption option) {
		add(option);
		this.options.add(option);
	}

	public void removePreferenceOption(AbstractPreferenceOption option) {
		remove(option);
		this.options.remove(option);
	}

	public void onSave() {
		for (AbstractPreferenceOption option : options) {
			if (option.isValidate()) {
				option.onSave();
				canSave = true;
			} else {
				canSave = false;
				return;
			}
		}
	}

	public void onLoad() {
		for (AbstractPreferenceOption option : options) {
			option.onLoad();
		}
	}

	public String getTitle() {
		return this.title;
	}

	/**
	 * 
	 */
	public List<AbstractPreferenceOption> getOptions() {
		return this.options;
	}

	/**
	 * @param option
	 */
	public void show(AbstractPreferenceOption option) {
		// ensureVisible(option);
	}

}
