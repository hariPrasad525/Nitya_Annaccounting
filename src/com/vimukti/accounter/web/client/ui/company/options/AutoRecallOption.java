/**
 * 
 */
package com.vimukti.accounter.web.client.ui.company.options;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;

/**
 * @author vimukti36
 * 
 */
public class AutoRecallOption extends AbstractPreferenceOption {

	CheckboxItem enableAutoRecallCheckBox;
	StyledPanel mainpanel;

	/**
	 * Because this class has a default constructor, it can be used as a binder
	 * template. In other words, it can be used in other *.ui.xml files as
	 * follows: <ui:UiBinder xmlns:ui="urn:ui:com.google.gwt.uibinder"
	 * xmlns:g="urn:import:**user's package**">
	 * <g:**UserClassName**>Hello!</g:**UserClassName> </ui:UiBinder> Note that
	 * depending on the widget that is used, it may be necessary to implement
	 * HasHTML instead of HasText.
	 */
	public AutoRecallOption() {
		super("");
		createControls();
	}

	@Override
	public String getTitle() {
		return messages.AutoRecall();
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
	public void createControls() {
		enableAutoRecallCheckBox.setTitle(messages.enableAutoRecall());
		enableAutoRecallCheckBox.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub

			}
		});

		mainpanel.add(enableAutoRecallCheckBox);

	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}

}
