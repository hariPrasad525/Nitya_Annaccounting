package com.vimukti.accounter.web.client.ui.company.options;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.forms.LabelItem;
import com.vimukti.accounter.web.client.ui.widgets.DateUtills;

/**
 * 
 * @author Lingarao.R
 * 
 */
public class CompanyDateFormateOption extends AbstractPreferenceOption {

	SelectCombo dateFormateComboBox;

	LabelItem exampleDateFomateLabel;

	LabelItem dateFormateDescriptionlabel;
	String[] dateFormates;

	interface CompanyDateFormateOptionUiBinder extends
			UiBinder<Widget, CompanyDateFormateOption> {
	}

	public CompanyDateFormateOption() {
		super("");
		createControls();
		initData();
	}

	@Override
	public String getTitle() {
		return messages.DateFormat();
	}

	@Override
	public void onSave() {
		getCompanyPreferences().setDateFormat(
				dateFormateComboBox.getSelectedValue());

	}

	@Override
	public String getAnchor() {
		return messages.DateFormat();
	}

	@Override
	public void createControls() {

		dateFormateComboBox = new SelectCombo(messages.DateFormat());
		dateFormateComboBox.addStyleName("header");
		dateFormates = new String[] { "ddMMyy", "MM/dd/yy", "dd/MM/yy",
				"ddMMyyyy", "MMddyyyy", "MMM-dd-yy", "MMMddyyyy", "dd/MM/yyyy",
				"MM/dd/yyyy", "dd/MMMM/yyyy", "MMMMddyyyy", "dd-MM-yyyy",
				"MM-dd-yyyy", "dd/MMM/yyyy", "MMM/dd/yyyy", };

		for (int i = 0; i < dateFormates.length; i++) {
			dateFormateComboBox.addItem(dateFormates[i]);
		}

		dateFormateDescriptionlabel = new LabelItem(messages.DateFormats(),
				"dateFormatDescPanel");
		dateFormateDescriptionlabel.setStyleName("organisation_comment");
		dateFormateComboBox.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				getExampleDateFormat(dateFormateComboBox.getSelectedIndex());
			}
		});

		exampleDateFomateLabel = new LabelItem(messages.Example(),
				"organisation_comment");
		add(dateFormateComboBox);
		add(exampleDateFomateLabel);
		add(dateFormateDescriptionlabel);
	}

	@Override
	public void initData() {
		dateFormateComboBox.setSelectedItem((getindex(getCompanyPreferences()
				.getDateFormat())));

	}

	/**
	 * 
	 * @param dateFormat
	 * @return
	 */
	private int getindex(String dateFormat) {
		for (int i = 0; i < dateFormates.length; i++) {
			if (dateFormat.equals(dateFormates[i])) {
				getExampleDateFormat(i);
				return i;
			}
		}
		return 0;
	}

	/**
	 * get example Label similarly to combo date formate
	 * 
	 * @param index
	 *            of combo Box
	 */
	private void getExampleDateFormat(int i) {
		switch (i) {
		case 0:
			exampleDateFomateLabel.setValue(messages.Example()
					+ DateUtills.getCurrentDateAsString("ddMMyy"));
			break;
		case 1:
			exampleDateFomateLabel.setValue(messages.Example()
					+ DateUtills.getCurrentDateAsString("MM/dd/yy"));
			break;
		case 2:
			exampleDateFomateLabel.setValue(messages.Example()
					+ DateUtills.getCurrentDateAsString("dd/MM/yy"));
			break;
		case 3:
			exampleDateFomateLabel.setValue(messages.Example()
					+ DateUtills.getCurrentDateAsString("ddMMyyyy"));
			break;
		case 4:
			exampleDateFomateLabel.setValue(messages.Example()
					+ DateUtills.getCurrentDateAsString("MMddyyyy"));
			break;
		case 5:
			exampleDateFomateLabel.setValue(messages.Example()
					+ DateUtills.getCurrentDateAsString("MMM-dd-yy"));
			break;
		case 6:
			exampleDateFomateLabel.setValue(messages.Example()
					+ DateUtills.getCurrentDateAsString("MMMddyyyy"));
			break;
		case 7:
			exampleDateFomateLabel.setValue(messages.Example()
					+ DateUtills.getCurrentDateAsString("dd/MM/yyyy"));
			break;
		case 8:
			exampleDateFomateLabel.setValue(messages.Example()
					+ DateUtills.getCurrentDateAsString("MM/dd/yyyy"));
			break;
		case 9:
			exampleDateFomateLabel.setValue(messages.Example()
					+ DateUtills.getCurrentDateAsString("dd/MMMM/yyyy"));
			break;
		case 10:
			exampleDateFomateLabel.setValue(messages.Example()
					+ DateUtills.getCurrentDateAsString("MMMMddyyyy"));
			break;
		case 11:
			exampleDateFomateLabel.setValue(messages.Example()
					+ DateUtills.getCurrentDateAsString("dd-MM-yyyy"));
			break;
		case 12:
			exampleDateFomateLabel.setValue(messages.Example()
					+ DateUtills.getCurrentDateAsString("MM-dd-yyyy"));
			break;
		case 13:
			exampleDateFomateLabel.setValue(messages.Example()
					+ DateUtills.getCurrentDateAsString("dd/MMM/yyyy"));
			break;
		case 14:
			exampleDateFomateLabel.setValue(messages.Example()
					+ DateUtills.getCurrentDateAsString("MMM/dd/yyyy"));
			break;
		}
	}
}
