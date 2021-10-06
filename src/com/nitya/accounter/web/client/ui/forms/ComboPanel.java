package com.nitya.accounter.web.client.ui.forms;

import com.google.gwt.user.client.ui.HasEnabled;
import com.nitya.accounter.web.client.ui.StyledPanel;

public class ComboPanel extends StyledPanel implements HasEnabled {
	private boolean enabled=true;

	public ComboPanel(String styleName) {
		super(styleName);
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}
