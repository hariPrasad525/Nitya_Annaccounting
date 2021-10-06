package com.nitya.accounter.web.client.ui.company.options;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.nitya.accounter.web.client.ui.combo.SelectCombo;
import com.nitya.accounter.web.client.ui.forms.LabelItem;
import com.nitya.accounter.web.client.ui.forms.RadioGroupItem;
import com.nitya.accounter.web.client.ui.forms.TextItem;


public class TaxInformationOptions extends AbstractPreferenceOption {

	LabelItem taxInfoLabel;
	LabelItem federalTaxDetailsLabel;
	LabelItem federalEmployeeein;
	LabelItem stateEmployeeein;
	RadioGroupItem einRadioGrp;
	TextItem empIdentityNo;
	LabelItem stateLabel;
	SelectCombo stateListBox;
	RadioGroupItem eanRadioGrp; 
	TextItem stateEmpAcno;
	
	private List<String> statesList;
	final String[] states = { "Alabama", "Alaska", "Arizona", "Arkansas", "California",
	        "Colorado", "Connecticut", "Delaware", "Florida", "Georgia",
	        "Hawaii", "Idaho", "Illinois", "Indiana", "Iowa", "Kansas",
	        "Kentucky", "Louisiana", "Maine", "Maryland", "Massachusetts",
	        "Michigan", "Minnesota", "Mississippi", "Missouri", "Montana",
	        "Nebraska", "Nevada", "New Hampshire", "New Jersey", "New Mexico",
	        "New York", "North Carolina", "North Dakota", "Ohio", "Oklahoma",
	        "Oregon", "Pennsylvania", "Rhode Island", "South Carolina", "South Dakota",
	        "Tennessee", "Texas", "Utah", "Vermont", "Virginia", "Washington", "West Virginia",
	        "Wisconsin", "Wyoming" };
	
    final String[] Item0 = { "Item 0,0", "Item 0,1", "Item 0,2" };
    final String[] Item1 = { "Item 1,0", "Item 1,1", "Item 1,2" };
    final String[] Item2 = { "Item 2,0", "Item 2,1", "Item 2,2" };
    

	/*
	 * interface TaxInformationOptionsUiBinder extends UiBinder<Widget,
	 * TaxInformationOptions> { }
	 */

	public TaxInformationOptions() {
		super("");
		createControls();
		initData();
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return messages.taxInformationOptions();
	}

	@Override
	public void onSave() {
		// TODO Auto-generated method stub
		if(empIdentityNo.getValue().toString().contentEquals("0")) {
			getCompanyPreferences().setEinNumber("0");
		}else {
			getCompanyPreferences().setEinNumber(empIdentityNo.getValue().toString());
		}
	
		if(stateEmpAcno.getValue().toString().contentEquals("0")) {
			getCompanyPreferences().setStateEmpNo("0");
		}else {
			getCompanyPreferences().setStateEmpNo(stateEmpAcno.getValue().toString());
		}
		
		 getCompanyPreferences().setCity(stateListBox.getSelectedValue());
		
	}

	@Override
	public String getAnchor() {
		// TODO Auto-generated method stub
		return messages.taxInformationOptions();
	}

	@Override
	public void createControls() {
		// TODO Auto-generated method stub
		taxInfoLabel = new LabelItem(
				messages.taxInformationOptions(), "header");
		add(taxInfoLabel);
		
		federalTaxDetailsLabel = new LabelItem(messages2.selectFederalTaxdetails(), "federal_tax_details"); 
		add(federalTaxDetailsLabel); 
		federalEmployeeein = new LabelItem(messages2.federalEmployeeeinInfo(),"federal_employee_info"); 
		add(federalEmployeeein);
		 
		einRadioGrp = new RadioGroupItem();
		einRadioGrp.setGroupName("einRadioGrp");
		einRadioGrp.addStyleName("terminologycustomerradio");
		einRadioGrp.setShowTitle(false);

		einRadioGrp.setValueMap(messages2.yes2(), messages2.no2());
		einRadioGrp.setValue(messages.Vendor());
		add(einRadioGrp);
		
		empIdentityNo = new TextItem("EIN", "header"); 
		add(empIdentityNo); 
				
		stateListBox = new SelectCombo(
				messages2.state2());
		stateListBox.addStyleName("header");
		statesList = new ArrayList<String>();
		// fiscalStartsList = null;
		for (int i = 0; i < states.length; i++) {
			statesList.add(states[i]);
			stateListBox.addItem(states[i]);
		}
		add(stateListBox);
		stateEmployeeein = new LabelItem(messages2.selectStateTaxdetailsEANumber(),"state_employee_info"); 
		add(stateEmployeeein);
		eanRadioGrp = new RadioGroupItem();
		eanRadioGrp.setGroupName("eanRadioGrp");
		eanRadioGrp.addStyleName("terminologycustomerradio");
		eanRadioGrp.setShowTitle(false);

		eanRadioGrp.setValueMap(messages2.yes2(), messages2.no2());
		eanRadioGrp.setValue(messages.Vendor());
		add(eanRadioGrp);
		 
		stateEmpAcno = new TextItem("EAN", "header");  
		add(stateEmpAcno);
		
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

		int stateIndex = Arrays.asList(states).lastIndexOf(getCompanyPreferences().getCity());
		stateListBox.setSelectedItem(stateIndex);
		
		String einNum = getCompanyPreferences().getStateEmpNo();
		if(einNum.contentEquals("")) {
			empIdentityNo.setValue("0");
			einRadioGrp.setValue(messages2.no2());
		}else {
			empIdentityNo.setValue(getCompanyPreferences().getEinNumber());
			einRadioGrp.setValue(messages2.yes2());
		}
		String eanNum = getCompanyPreferences().getStateEmpNo();
		if(eanNum.contentEquals("")) {
			stateEmpAcno.setValue("0");
			eanRadioGrp.setValue(messages2.no2());
		}else {
			stateEmpAcno.setValue(getCompanyPreferences().getStateEmpNo());
			eanRadioGrp.setValue(messages2.yes2());
		}
	
		
	}
}
