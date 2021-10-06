/**
 * 
 */
package com.vimukti.accounter.web.client.ui.company.options;

import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;

/**
 * @author vimukti36
 * 
 */
public class ClosingBooksOption extends AbstractPreferenceOption {

	CheckboxItem closingBooksCheckBox;
	StyledPanel mainPanel;

	/**
	 * Because this class has a default constructor, it can be used as a binder
	 * template. In other words, it can be used in other *.ui.xml files as
	 * follows: <ui:UiBinder xmlns:ui="urn:ui:com.google.gwt.uibinder"
	 * xmlns:g="urn:import:**user's package**">
	 * <g:**UserClassName**>Hello!</g:**UserClassName> </ui:UiBinder> Note that
	 * depending on the widget that is used, it may be necessary to implement
	 * HasHTML instead of HasText.
	 */
	public ClosingBooksOption() {
		super("");
		createControls();
	}

	public void createControls() {
		closingBooksCheckBox = new CheckboxItem(messages.closingthebooks(),
				"closingBooksCheckBox");
		mainPanel = new StyledPanel("closingBooksOption");
		mainPanel.add(closingBooksCheckBox);
		add(mainPanel);
	}

	@Override
	public String getTitle() {
		return messages.closingthebooks();
	}

	@Override
	public void onSave() {
		// TODO Auto-generated method stub

	}

	@Override
	public String getAnchor() {
		return messages.company();
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}

}
