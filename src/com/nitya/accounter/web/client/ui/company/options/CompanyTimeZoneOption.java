/**
 * 
 */
package com.nitya.accounter.web.client.ui.company.options;

import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.nitya.accounter.web.client.ui.CoreUtils;
import com.nitya.accounter.web.client.ui.combo.SelectCombo;

/**
 * @author Administrator
 * 
 */
public class CompanyTimeZoneOption extends AbstractPreferenceOption {

	SelectCombo timeZoneListBox;
	private List<String> timezones;

	interface CompanyTimeZoneOptionUiBinder extends
			UiBinder<Widget, CompanyTimeZoneOption> {
	}

	/**
	 * Because this class has a default constructor, it can be used as a binder
	 * template. In other words, it can be used in other *.ui.xml files as
	 * follows: <ui:UiBinder xmlns:ui="urn:ui:com.google.gwt.uibinder"
	 * xmlns:g="urn:import:**user's package**">
	 * <g:**UserClassName**>Hello!</g:**UserClassName> </ui:UiBinder> Note that
	 * depending on the widget that is used, it may be necessary to implement
	 * HasHTML instead of HasText.
	 */
	public CompanyTimeZoneOption() {
		super("");
		createControls();
		initData();
	}

	private boolean isTimeZonesLoaded;

	@Override
	public void createControls() {
		timeZoneListBox = new SelectCombo(messages.timezone());
		timeZoneListBox.addStyleName("header");
		timeZoneListBox.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (!isTimeZonesLoaded) {
					isTimeZonesLoaded = true;
					timezones = CoreUtils.getTimeZonesAsList();
					String value = timeZoneListBox.getSelectedValue();
					timeZoneListBox.removeComboItem(value);
					for (String tz : timezones) {
						timeZoneListBox.addItem(tz);
					}
					timeZoneListBox.setSelectedItem(timezones.indexOf(value));
				}
			}
		});
		add(timeZoneListBox);

	}

	private String getDefaultTzOffsetStr() {
		Date date = new Date();
		DateTimeFormat tzFormat = DateTimeFormat.getFormat("z");
		return tzFormat.format(date);
	}

	@Override
	public String getAnchor() {
		return messages.company();
	}

	@Override
	public String getTitle() {
		return messages.timezone();
	}

	@Override
	public void initData() {
		String timezone;
		if (getCompanyPreferences().getTimezone() != ""
				&& getCompanyPreferences().getTimezone() != null) {
			timezone = getCompanyPreferences().getTimezone();
		} else {
			timezone = getDefaultTzOffsetStr();
		}
		timeZoneListBox.addItem(timezone);
		this.timeZoneListBox.setSelectedItem(0);
	}

	@Override
	public void onSave() {
		if (timeZoneListBox.getSelectedIndex() != -1 && timezones != null)
			getCompanyPreferences().setTimezone(
					timezones.get(timeZoneListBox.getSelectedIndex()));
	}
}
