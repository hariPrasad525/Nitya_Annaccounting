package com.nitya.accounter.web.client.uibinder.setup;


import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.nitya.accounter.web.client.ui.Accounter;

public class SetupTaxInfoPage extends AbstractSetupPage {

	private static SetupTaxInfoPageUiBinder uiBinder = GWT.create(SetupTaxInfoPageUiBinder.class);

	@UiField
	Label headerLabel;
	@UiField
	Label federalTaxDetailsLabel;
	@UiField
	Label federalEmployeeein;
	@UiField
	Label stateEmployeeein;
	@UiField
	RadioButton isKnownEIN;
	@UiField
	RadioButton isUnknownEIN;
	@UiField
	TextBox empIdentityNo;
	@UiField
	Label stateLabel;
	@UiField
	ListBox stateListBox;
	@UiField
	RadioButton isKnownStateEAN;
	@UiField
	RadioButton isUnknownStateEAN;
	@UiField
	TextBox stateEmpAcno;
	
	//@UiField
	//ListBox country;
	
	/*
	@UiField
	Label primaryCurrenyLabel;
	@UiField
	ListBox primaryCurrencyListBox;
	@UiField
	CheckBox isMultiCurrencyAllowed;
	// @UiField
	StyledPanel currencyListGridPanel;
	// private CurrenciesGrid currenciesGrid;
	// private Set<ClientCurrency> currencySet;
	private List<ClientCurrency> currenciesList = new ArrayList<ClientCurrency>();

	*/
	
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
    
	
	interface SetupTaxInfoPageUiBinder extends UiBinder<Widget, SetupTaxInfoPage> {
		
	}

	public SetupTaxInfoPage() {
		initWidget(uiBinder.createAndBindUi(this));
		//button.setText(firstName);
		createControls();
	}

	public String getText() {
		//return button.getText();
		return "566";
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void createControls() {
		headerLabel.setText(messages2.enterYourTaxInfo());
		federalTaxDetailsLabel.setText(messages2.selectFederalTaxdetails());
		//setText(messages.selectIndustryInfoHTML1()
		federalEmployeeein.setText(messages2.federalEmployeeeinInfo());
		isKnownEIN.setText(messages2.yes2());
		// trackingTimeYes.setText(messages.yes());
		isUnknownEIN.setText(messages2.no2());
		stateLabel.setText(messages2.state2());
		
		stateEmployeeein.setText(messages2.selectStateTaxdetailsEANumber());
		isKnownStateEAN.setText(messages2.yes2());
		isUnknownStateEAN.setText(messages2.no2());
		
		// Add Items' categories
        for (int i = 0; i < states.length; i++) {
        	stateListBox.addItem(states[i]);
        }
        final ListBox contentList = new ListBox();
        contentList.setVisibleItemCount(5);

     // Add ChangeHandler to dropDownList 
        stateListBox.addChangeHandler(new ChangeHandler() {
 
				@Override
				public void onChange(ChangeEvent event) {
			          int Item = stateListBox.getSelectedIndex();
			          String[] listData = null;

			          // Clear the content list
			            contentList.clear();
			          // Set content 
			                      switch (Item) {
			                      case 0:
			                          listData = Item0;
			                          break;
			                      case 1:
			                          listData = Item1;
			                          break;
			                      case 2:
			                          listData = Item2;
			                          break;
			                      }
			                      for (int i = 0; i < listData.length; i++) {
			                          contentList.addItem(listData[i]);
			                      }
			                  }
              });
         isKnownEIN.setValue(true);
	     isKnownStateEAN.setValue(true);
	     if (isUnknownEIN.isChecked()) {
	        	empIdentityNo.setReadOnly(true);
			}
	     if (isUnknownStateEAN.isChecked()) {
	        	stateEmpAcno.setReadOnly(true);
			}
	}

	//--------
	
	@Override
	public boolean canShow() {
		return true;
	}
	
	@Override
	protected void onSave() {
		// TODO Auto-generated method stub
		if(empIdentityNo.getText().toString().contentEquals("")) {
			preferences.setEinNumber("0");
		}else {
			preferences.setEinNumber(empIdentityNo.getText().toString());
		}
		if(stateEmpAcno.getText().toString().contentEquals("")) {
			preferences.setStateEmpNo("0");
		}else {
			preferences.setStateEmpNo(stateEmpAcno.getText().toString());
		}
		preferences.setCity(stateListBox.getSelectedItemText().toString());
	}

	@Override
	protected void onLoad() {
		// TODO Auto-generated method stub
		
	      
	}

	@SuppressWarnings("deprecation")
	@Override
	protected boolean validate() {
		// TODO Auto-generated method stub
		if (isKnownEIN.isChecked()&&empIdentityNo.equals("")) {
			Accounter.showError(messages2.selectStateTaxdetailsEANumber());
			return false;
		} else {
			return true;
		}
		
	}

	@Override
	public String getViewName() {
		return messages2.taxInfo();
	}

}
