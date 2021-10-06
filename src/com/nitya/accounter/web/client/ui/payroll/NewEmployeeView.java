package com.nitya.accounter.web.client.ui.payroll;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.nitya.accounter.web.client.AccounterAsyncCallback;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.AddNewButton;
import com.nitya.accounter.web.client.core.ClientAddress;
import com.nitya.accounter.web.client.core.ClientContact;
import com.nitya.accounter.web.client.core.ClientCustomFieldValue;
import com.nitya.accounter.web.client.core.ClientEmployee;
import com.nitya.accounter.web.client.core.ClientEmployeeAttendance;
import com.nitya.accounter.web.client.core.ClientEmployeeCompsensation;
import com.nitya.accounter.web.client.core.ClientEmployeeGroup;
import com.nitya.accounter.web.client.core.ClientEmployeeTax;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientPayee;
import com.nitya.accounter.web.client.core.CountryPreferences;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.core.ValidationResult;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.exception.AccounterExceptions;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.AddressDialog;
import com.nitya.accounter.web.client.ui.CustomFieldDialog;
import com.nitya.accounter.web.client.ui.GwtDisclosurePanel;
import com.nitya.accounter.web.client.ui.GwtTabPanel;
import com.nitya.accounter.web.client.ui.StyledPanel;
import com.nitya.accounter.web.client.ui.UIUtils;
import com.nitya.accounter.web.client.ui.combo.EmployeeGroupCombo;
import com.nitya.accounter.web.client.ui.combo.SelectCombo;
import com.nitya.accounter.web.client.ui.core.BaseView;
import com.nitya.accounter.web.client.ui.core.DateField;
import com.nitya.accounter.web.client.ui.core.DoubleField;
import com.nitya.accounter.web.client.ui.core.EditMode;
import com.nitya.accounter.web.client.ui.core.IntegerField;
import com.nitya.accounter.web.client.ui.core.ViewManager;
import com.nitya.accounter.web.client.ui.edittable.tables.EmployeeAttendanceTable;
import com.nitya.accounter.web.client.ui.forms.CheckboxItem;
import com.nitya.accounter.web.client.ui.forms.CustomFieldForm;
import com.nitya.accounter.web.client.ui.forms.DynamicForm;
import com.nitya.accounter.web.client.ui.forms.FormItem;
import com.nitya.accounter.web.client.ui.forms.PasswordItem;
import com.nitya.accounter.web.client.ui.forms.SelectItem;
import com.nitya.accounter.web.client.ui.forms.TextAreaItem;
import com.nitya.accounter.web.client.ui.forms.TextItem;

public class NewEmployeeView extends BaseView<ClientEmployee> {

	private DynamicForm basicInfoForm, empDetailsInfoForm,
			empOtherDetailsInfoForm, empOtherDetailsLeftForm,
			empOtherDetailsRightForm, attendanceForm;
	private TextItem nameItem, lnameItem, employeeIdItem, designationItem, panItem,
			bankNameItem, bankAccountNumberItem, bankBranchItem, contactNumberItem, emailItem, passportNumberItem,
			countryOfIssueItem, emplVisaNumberItem, empId, milesPerHour, advancePaymnts, foodAllowances, otherAllowances;
	private PasswordItem ssnItem;
	SelectItem locationItem;
	private DateField dateOfBirthItem, dateOfHire, passportExpiryDateItem,
			emplVisaNumberDateItem, lastDateItem, empPayrollDate;
	private StyledPanel mainPanel;
	private StyledPanel firstPanel, secondPanel;
	private EmployeeGroupCombo employeeGroupCombo;
	private final String[] genderTypes = { messages.unspecified(),
			messages.male(), messages.female() };
	private ArrayList<String> listOfgenders, listOfReaons;
	private SelectCombo genderSelect, reasonCombo;
	private TextAreaItem addrArea;
	private LinkedHashMap<Integer, ClientAddress> allAddresses;
	private CheckboxItem activeOrInactive;
	private final String[] reasons = { messages2.gotNewJobOffer(),
			messages2.quitWithOutAjob(), messages2.lackofPerformance(),
			messages2.disputesbetweenCoworkers(),
			messages2.nosatisfactionwithJob(), messages2.notenoughHours(),
			messages2.jobwasTemporary(), messages2.contractended(),
			messages2.workwasSeasonal(), messages2.betteropportunity(),
			messages2.seekinggrowth(), messages2.careerchange(),
			messages2.returnedtoSchool(), messages2.relocated(),
			messages2.raisedaFamily(), messages.other() };
	CustomFieldForm customFieldForm;
	Button addCustomFieldButton;
	CustomFieldDialog customFieldDialog;
	private GwtTabPanel tabSet;
	private ArrayList<DynamicForm> listforms;
	private EmployeeTaxTable employeTaxTable;
	private StyledPanel taxFlowPanel;
	private GwtDisclosurePanel taxDisclosurePanel;
	private EmployeCompensationTable employeeCompTable;
	private EmployeeAttendanceTable employeeAttendanceTable;
	private StyledPanel empCompFlowPanel;
	private GwtDisclosurePanel empCompDisclosurePanel;
	
	private PayStructureTable payTable;
	private DoubleField salaryAmountItem;
	private SelectItem payType;
	private SelectItem payFrequency;
	private DoubleField payadditionalAmount;

	private SelectItem filingStatus;
	private SelectItem taxResidencyType;
	private IntegerField noOfAllowances;
	private DoubleField additionalAmount;
	private CheckboxItem ssnTaxble;
	private CheckboxItem medicareTaxable;
	private CheckboxItem federalUnmploymentTax;
	private DynamicForm federTaxForm;
	private SelectItem sfilingStatus;
	private DoubleField sadditionalAmount;
	private IntegerField snoOfAllowances;
	private SelectItem staxResidencyType;
	private DynamicForm stateTaxForm;
	private TextItem routingNumberItem;
	private SelectItem stateItem;
	private DynamicForm cmpensationForm;
	private CheckboxItem stateUnmploymentTax;
	

	public NewEmployeeView() {
		this.getElement().setId("NewEmployeeView");
	}

	@Override
	public void init() {
		super.init();
		createControls();
		setSize("100%", "100%");
	}

	@Override
	public void initData() {
		if (getData() == null) {
			setData(new ClientEmployee());
		} else {
			initViewData(getData());
		}
		super.initData();
	}

	private void initViewData(ClientEmployee data) {
		
		nameItem.setValue(data.getName());
		lnameItem.setValue(data.geteLastname()); 
		ssnItem.setValue(data.getSsn());
		dateOfBirthItem.setValue(new ClientFinanceDate(data.getDateOfBirth()));
		if (data.getGender() != -1) {
			genderSelect.setComboItem(genderTypes[data.getGender()]);
		}
		
		contactNumberItem.setValue(data.getPhoneNo());
		emailItem.setValue(data.getEmail());
		activeOrInactive.setValue(data.isActive());
		lastDateItem.setVisible(!activeOrInactive.getValue());
		reasonCombo.setVisible(!activeOrInactive.getValue());
		//panItem.setValue(data.getPanNumber());

		setAddresses(data.getAddress());

		ClientAddress toBeShown = allAddresses.get(ClientAddress.TYPE_BILL_TO);
		if (toBeShown != null) {
			String toToSet = new String();
			if (toBeShown.getAddress1() != null
					&& !toBeShown.getAddress1().isEmpty()) {
				toToSet = toBeShown.getAddress1().toString() + "\n";
			}

			if (toBeShown.getStreet() != null
					&& !toBeShown.getStreet().isEmpty()) {
				toToSet += toBeShown.getStreet().toString() + "\n";
			}

			if (toBeShown.getCity() != null && !toBeShown.getCity().isEmpty()) {
				toToSet += toBeShown.getCity().toString() + "\n";
			}

			if (toBeShown.getStateOrProvinence() != null
					&& !toBeShown.getStateOrProvinence().isEmpty()) {
				toToSet += toBeShown.getStateOrProvinence() + "\n";
			}
			if (toBeShown.getZipOrPostalCode() != null
					&& !toBeShown.getZipOrPostalCode().isEmpty()) {
				toToSet += toBeShown.getZipOrPostalCode() + "\n";
			}
			if (toBeShown.getCountryOrRegion() != null
					&& !toBeShown.getCountryOrRegion().isEmpty()) {
				toToSet += toBeShown.getCountryOrRegion();
			}
			addrArea.setValue(toToSet);
		}

		employeeIdItem.setValue(data.getNumber());
		dateOfHire.setValue(new ClientFinanceDate(data.getPayeeSince()));
		employeeGroupCombo.setGroupValue(data.getGroup());
		designationItem.setValue(data.getDesignation());
		locationItem.setValue(data.getLocation());
		bankAccountNumberItem.setValue(data.getBankAccountNo());
		bankBranchItem.setValue(data.getBankBranch());
		bankNameItem.setValue(data.getBankName());
		routingNumberItem.setValue(data.getRoutingNumber());
		
		/**passportNumberItem.setValue(data.getPassportNumber());
		passportExpiryDateItem.setValue(new ClientFinanceDate(data
				.getPassportExpiryDate()));
		lastDateItem.setValue(new ClientFinanceDate(data.getLastDate()));
		countryOfIssueItem.setValue(data.getCountryOfIssue()); 
		emplVisaNumberItem.setValue(data.getVisaNumber());*/
		
		if (data.getReasonType() != -1) {
			reasonCombo.setValue(reasons[data.getReasonType()]);
		}
		if(data.getEmployeeTaxes() != null) {
			for(ClientEmployeeTax tax:data.getEmployeeTaxes()) {
	            if(tax.getTaxType() == ClientEmployeeTax.TAX_TYPE_FEDERAL) {
					filingStatus.setItemValue(tax.getTaxFilingStatus()+"");
					additionalAmount.setNumber(tax.getAdditionalAmount());
					noOfAllowances.setNumber((long) tax.getTaxallowences());
					ssnTaxble.setValue(tax.isSSNTaxable());
					medicareTaxable.setValue(tax.isSSNTaxable());
					federalUnmploymentTax.setValue(tax.isTaxUnemployement());
					taxResidencyType.setItemValue(tax.getTaxResidencyType()+"");
	            } else {
	            	sfilingStatus.setItemValue(tax.getTaxFilingStatus()+"");
					sadditionalAmount.setNumber(tax.getAdditionalAmount());
					snoOfAllowances.setNumber((long) tax.getTaxallowences());
					stateUnmploymentTax.setValue(tax.isTaxUnemployement());
					staxResidencyType.setItemValue(tax.getTaxResidencyType()+"");
					stateItem.setValue(tax.getState());
	            }
			}
		}
		payadditionalAmount.setNumber(data.getAdditionalAmount());
		payType.setItemValue(data.getPayType()+"");
		payFrequency.setItemValue(data.getPayFrequency()+"");
		salaryAmountItem.setNumber(data.getSalary());
		
		
		/*
		 * ClientEmployeeAttendance empExp=(ClientEmployeeAttendance)
		 * data.getEmployeeAttendances(); milesPerHour.setValue(empExp.getMileshours());
		 * if (data.getEmployeeAttendances() != null) { for(ClientEmployeeAttendance
		 * attendance : data.getEmployeeAttendances()) {
		 * milesPerHour.setValue(attendance.getMileshours());
		 * 
		 * } }
		 */
		//ClientEmployeeAttendance employeeAttendance = new ClientEmployeeAttendance();
		//milesPerHour.setValue(employeeAttendance.getMileshours());
		
	}

	private void setAddresses(Set<ClientAddress> addresses) {
		if (addresses != null) {
			Iterator<ClientAddress> it = addresses.iterator();
			while (it.hasNext()) {
				ClientAddress addr = (ClientAddress) it.next();
				if (addr != null) {
					allAddresses.put(addr.getType(), addr);
				}
			}
		}
	}

	public void createCustomFieldControls() {
		if (data != null && data.getCustomFieldValues() != null) {
			customFieldForm.updateValues(data.getCustomFieldValues(),
					getCompany(), ClientPayee.TYPE_EMPLOYEE);
		}
		customFieldForm.createControls(getCompany(),
				data == null ? null : data.getCustomFieldValues(),
				ClientPayee.TYPE_EMPLOYEE);
		Set<ClientCustomFieldValue> customFieldValues = data == null ? new HashSet<ClientCustomFieldValue>()
				: data.getCustomFieldValues();
		Set<ClientCustomFieldValue> deleteCustomFieldValues = new HashSet<ClientCustomFieldValue>();
		for (ClientCustomFieldValue value : customFieldValues) {
			if (getCompany().getClientCustomField(value.getID()) == null) {
				deleteCustomFieldValues.add(value);
			}
		}

		for (ClientCustomFieldValue clientCustomFieldValue : deleteCustomFieldValues) {
			customFieldValues.remove(clientCustomFieldValue);
		}
		customFieldForm.setEnabled(!isInViewMode());
	}

	public Set<ClientAddress> getAddresss() {
		ClientAddress selectedAddress = allAddresses.get(UIUtils
				.getAddressType("company"));
		if (selectedAddress != null) {
			selectedAddress.setIsSelected(true);
			allAddresses
					.put(UIUtils.getAddressType("company"), selectedAddress);
		}
		Collection<ClientAddress> add = allAddresses.values();
		Iterator<ClientAddress> it = add.iterator();
		while (it.hasNext()) {
			ClientAddress a = (ClientAddress) it.next();
			Set<ClientAddress> hashSet = new HashSet<ClientAddress>();
			hashSet.add(a);
			return hashSet;
			// toBeSet.add(a);
			// System.out.println("Sending Address  Type " + a.getType()
			// + " Street is " + a.getStreet() + " Is Selected"
			// + a.getIsSelected());
		}
		return null;
	}

	private void createControls() {
		allAddresses = new LinkedHashMap<Integer, ClientAddress>();

		tabSet = (GwtTabPanel) GWT.create(GwtTabPanel.class);
		Label title = new Label(messages.employee());
		title.setStyleName("label-title");
		listforms = new ArrayList<DynamicForm>();
		tabSet.add(getTabItem(getDetailsTab(), messages.details()), messages.details());
		tabSet.add(getTabItem(getEmployeementTab(),  messages2.employment()), messages2.employment());
		tabSet.add(getTabItem(getPay(), messages2.pay()), messages2.pay());
		tabSet.add(getTabItem(getTaxForm(), messages2.taxes()), messages2.taxes());
		tabSet.add(getTabItem(getBankDetails(), messages2.directDeposit()), messages2.directDeposit());
		// Need to check UserType weather Admin or other User
		tabSet.add(getTabItem(getEmpExpencesInfo(), messages2.empExpences()), messages2.empExpences());
		tabSet.selectTab(0);
		
		mainPanel = new StyledPanel("mainPanel");
		mainPanel.add(title);
		tabSet.getPanel().setWidth("100%");
		mainPanel.add(tabSet.getPanel());
//		mainPanel.add(getDetailsTab());
//		mainPanel.add(getGwtDisclosurePanel(messages2.employment(), getEmployeementTab()).getPanel());
//		mainPanel.add(getGwtDisclosurePanel(messages2.pay(), getPay()).getPanel());
//		mainPanel.add(getGwtDisclosurePanel(messages2.taxes(), getTaxForm()).getPanel());
		mainPanel.setWidth("100%");
		this.add(mainPanel);

		setSize("100%", "100%");

	}
	
	private StyledPanel getTabItem(Widget child, String title) {
		StyledPanel mainPanel = new StyledPanel(title);
		mainPanel.add(child);
		return mainPanel;
	}
	
	private StyledPanel getTaxForm() {
		StyledPanel mainPanel = new StyledPanel("taxes");
		
		CaptionPanel federalTaxCaption = new CaptionPanel(messages2.taxTypeFederal());
		DOM.setStyleAttribute(federalTaxCaption.getElement(), "border","1px solid #ccc");
		
		filingStatus = new SelectItem(messages2.taxFilingStatus(), "taxFilingStatus");
		filingStatus.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				
			}
		});
		filingStatus.setValueMap(getFilingStatusMap());
		filingStatus.setEnabled(!isInViewMode());
		filingStatus.setRequired(true);
		
		noOfAllowances = new IntegerField(this, messages2.taxAllowances());
		
		taxResidencyType = new SelectItem(messages2.taxResidencyType(), "taxResidencyType");
		taxResidencyType.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				
			}
		});
		taxResidencyType.setValueMap(getTaxResidencyType());
		taxResidencyType.setRequired(true);
		ssnTaxble = new CheckboxItem(messages2.isSSNTaxable(), "ssnTaxble");
		medicareTaxable = new CheckboxItem(messages2.medicareTaxable(), "medicareTaxable");
		federalUnmploymentTax = new CheckboxItem(messages2.federalUnmploymentTax(), "federalUnmploymentTax");
		
		additionalAmount = new DoubleField(this, messages2.additionalAmount());
		
		federTaxForm = new DynamicForm("federTaxForm");
		federTaxForm.add(filingStatus,noOfAllowances, taxResidencyType, ssnTaxble, medicareTaxable, federalUnmploymentTax,additionalAmount );
		federalTaxCaption.add(federTaxForm);
		
		CaptionPanel stateTax = new CaptionPanel(messages2.state());
		DOM.setStyleAttribute(stateTax.getElement(), "border", "1px solid #ccc");
		
		sfilingStatus = new SelectItem(messages2.taxFilingStatus(), "taxFilingStatus");
		sfilingStatus.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				
			}
		});
		sfilingStatus.setEnabled(!isInViewMode());
		
		stateItem = new SelectItem(messages.state(), "stateSelection");
		stateItem.setEnabled(!isInViewMode());
		getStates(stateItem);
		stateItem.setRequired(true);
		sfilingStatus.setValueMap(getFilingStatusMap());
		sfilingStatus.setEnabled(!isInViewMode());
		
		snoOfAllowances = new IntegerField(this, messages2.taxAllowances());
		
		staxResidencyType = new SelectItem(messages2.taxResidencyType(), "taxResidencyType");
		staxResidencyType.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				
			}
		});
		staxResidencyType.setValueMap(getTaxResidencyType());
		staxResidencyType.setRequired(true);
		sadditionalAmount = new DoubleField(this, messages2.additionalAmount());
		stateUnmploymentTax = new CheckboxItem(messages2.stateUnmploymentTax(), "stateUnmployementTax");
		
		stateTaxForm = new DynamicForm("federTaxForm");
		stateTaxForm.add(stateItem, sfilingStatus,snoOfAllowances, staxResidencyType,sadditionalAmount, stateUnmploymentTax);
		stateTax.add(stateTaxForm);
		
		mainPanel.add(federalTaxCaption);
		mainPanel.add(stateTax);
		
		/**CaptionPanel bank = new CaptionPanel(messages.bank());
		DOM.setStyleAttribute(bank.getElement(), "border", "1px solid #ccc");
		bank.add(getBankDetails());
		mainPanel.add(bank); */
		
		enableOrDisableItem(sadditionalAmount, staxResidencyType, ssnTaxble, snoOfAllowances, stateUnmploymentTax, sfilingStatus, stateItem, sfilingStatus, additionalAmount, 
				medicareTaxable, ssnTaxble, federalUnmploymentTax,  taxResidencyType, noOfAllowances, filingStatus);
		
		return mainPanel;
	}
	
	private void enableOrDisableItem(FormItem... items) {
		for(FormItem item:items) {
			item.setEnabled(!isInViewMode());
		}
	}

	private CaptionPanel getPay() {
		CaptionPanel payCaption = new CaptionPanel();
		DOM.setStyleAttribute(payCaption.getElement(), "border",
				"1px solid #ccc");
		
		salaryAmountItem = new DoubleField(this, messages2.salary());
		salaryAmountItem.setEnabled(!isInViewMode());
		salaryAmountItem.setRequired(true);
		
		payType = new SelectItem(messages2.payType(), "payTypeSelect");
		payType.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				
			}
		});
		
		payadditionalAmount = new DoubleField(this, messages2.additionalAmount());
		
		payType.setValueMap(getPayTypeMap());
		payType.setEnabled(!isInViewMode());
		payType.setRequired(true);
		payFrequency = new SelectItem(messages2.payFrequency(), "payFreSelect");
		payFrequency.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				
			}
		});
		payFrequency.setValueMap(getPayFrequency());
		payFrequency.setEnabled(!isInViewMode());
		payFrequency.setRequired(true);

		cmpensationForm = new DynamicForm("compensationForm");
		cmpensationForm.add( payType, salaryAmountItem, payFrequency);
		payCaption.add(cmpensationForm);
		
		enableOrDisableItem(payType, salaryAmountItem, payadditionalAmount, payFrequency);
		return payCaption;
	}
	
	private LinkedHashMap<String, String> getPayTypeMap(){
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		map.put(messages.daily(), ClientEmployeeCompsensation.PAY_TYPE_DAILY+"");
		map.put(messages.annual(), ClientEmployeeCompsensation.PAY_TYPE_ANNUAL+"");
		map.put(messages2.payTypeHourly(), ClientEmployeeCompsensation.PAY_TYPE_HOURLY+"");
		map.put(messages2.payTypeMiles(), ClientEmployeeCompsensation.PAY_TYPE_MILES+"");
		map.put(messages2.payTypePerWork(), ClientEmployeeCompsensation.PAY_TYPE_PER_WORK+"");
		map.put(messages2.payTypePerPeriod(), ClientEmployeeCompsensation.PAY_TYPE_PER_PERIOD+"");
		return map;
	}
	
	private int getKeyValue(LinkedHashMap<String, String>  map, String val) {
//		for(String key : map.values()) {
			if(val != null) {
				return UIUtils.toInt(val);
			}
//		}
		return 0;
	}
	
	private LinkedHashMap<String, String> getPayFrequency(){
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		map.put(messages.weekly(), ClientEmployeeCompsensation.PAY_FREQUENCY_WEEKLY+"");
		map.put(messages2.payFrequencyBiWeekly(), ClientEmployeeCompsensation.PAY_FREQUENCY_BI_WEEKLY+"");
		map.put(messages2.payFrequencySemiMonthly(), ClientEmployeeCompsensation.PAY_FREQUENCY_SEMI_MONTHLY+"");
		map.put(messages.monthly(), ClientEmployeeCompsensation.PAY_FREQUENCY_MONTHLY+"");
		return map;
	}

	private LinkedHashMap<String, String> getFilingStatusMap(){
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		map.put(messages2.taxFilingStatusSingle(), ClientEmployeeTax.FILING_STATUS_SINGLE+"");
		map.put(messages2.taxFilingStatusMarried(),ClientEmployeeTax.FILING_STATUS_MARRIED+"");
		map.put(messages2.taxFilingStatusMarriedJointly(), ClientEmployeeTax.FILING_STATUS_MARRIED_JOINTLY+"");
		map.put(messages2.taxFilingStatusMarriedSeparate(), ClientEmployeeTax.FILING_STATUS_MARRIED_SEPARATE+"");
		return map;
	}
	private LinkedHashMap<String, String> getTaxResidencyType(){
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		map.put(messages2.taxResidencyTypeResident(), ClientEmployeeTax.TAX_RESIDENCY+"");
		map.put(messages2.taxResidencyTypeNonResident(), ClientEmployeeTax.TAX_RESIDENCY_NON+"");
		return map;
	}
	
	private void getStates(SelectItem item) {
		Accounter.createCompanyInitializationService().getCountryPreferences(
				Accounter.getCompany().getCountry(), new AsyncCallback<CountryPreferences>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(CountryPreferences result) {
						if (result != null) {
							String val = item.getValue();
						   item.setValueMap(result.getStates());
						   item.setValue(val);
						} 
					}
				});
	}

	private CaptionPanel getDetailsTab() {
		CaptionPanel detailPanel = new CaptionPanel(messages.details());
		DOM.setStyleAttribute(detailPanel.getElement(), "border","1px solid #ccc");
		
//		firstPanel = new StyledPanel("firstPanel");
//		firstPanel.add(getEmpBasicInfo());
//		firstPanel.setWidth("100%");
		detailPanel.add(getEmpBasicInfo());
		return detailPanel;
	}
	private CaptionPanel getExpensesTab() {
		CaptionPanel expensesPanel = new CaptionPanel(messages.details());
		DOM.setStyleAttribute(expensesPanel.getElement(), "border","1px solid #ccc");
		
//		firstPanel = new StyledPanel("firstPanel");
//		firstPanel.add(getEmpBasicInfo());
//		firstPanel.setWidth("100%");
		expensesPanel.add(getEmpBasicInfo());
		return expensesPanel;
	}

	private StyledPanel getEmployeementTab() {
		secondPanel = new StyledPanel("secondPanel");
		secondPanel.add(getEmpDetails());
		//secondPanel.add(getEmpOtherDetailsInfo());
		createCustomFieldControls();		
		secondPanel.setWidth("100%");
		return secondPanel;
	}


	private CaptionPanel getEmpOtherDetailsInfo() {
		CaptionPanel empOtherDetailsInfo = new CaptionPanel(
				messages.employeeDetailsInfo());
		DOM.setStyleAttribute(empOtherDetailsInfo.getElement(), "border", "1px solid #ccc");
		passportNumberItem = new TextItem(messages.passportNumber(),
				"passportNumberItem");
		passportNumberItem.setEnabled(!isInViewMode());
		passportExpiryDateItem = new DateField(messages.passportExpiryDate(),
				"passportExpiryDateItem");
		passportExpiryDateItem.setEnteredDate(new ClientFinanceDate());
		passportExpiryDateItem.setEnabled(!isInViewMode());
		countryOfIssueItem = new TextItem(messages.countryOfIssue(),
				"countryOfIssueItem");
		countryOfIssueItem.setEnabled(!isInViewMode());
		emplVisaNumberItem = new TextItem(messages.visaNumber(),
				"emplVisaNumberItem");
		emplVisaNumberItem.setEnabled(!isInViewMode());
		emplVisaNumberDateItem = new DateField(messages.visaExpiryDate(),
				"emplVisaNumberDateItem");
		emplVisaNumberDateItem.setEnteredDate(new ClientFinanceDate());
		emplVisaNumberDateItem.setEnabled(!isInViewMode());
		empOtherDetailsInfoForm = new DynamicForm("empOtherDetailsInfoForm");

		empOtherDetailsInfoForm.add(passportNumberItem, passportExpiryDateItem,
				countryOfIssueItem, emplVisaNumberItem, emplVisaNumberDateItem);
		// empOtherDetailsLeftForm.add(addCustomFieldButton);
		// empOtherDetailsLeftForm.add(customFieldForm);

//		empOtherDetailsInfoForm.add(empOtherDetailsLeftForm);

		empOtherDetailsInfo.setContentWidget(empOtherDetailsInfoForm);
		
	
		return empOtherDetailsInfo;
	}
	
	private DynamicForm getBankDetails() {
		bankAccountNumberItem = new TextItem(messages.bankAccountNumber(),
				"bankAccountNumberItem");
		bankAccountNumberItem.setEnabled(!isInViewMode());
		
		
		bankNameItem = new TextItem(messages.bankName(), "bankNameItem");
		bankNameItem.setEnabled(!isInViewMode());
		bankBranchItem = new TextItem(messages.bankBranch(), "bankBranchItem");
		bankBranchItem.setEnabled(!isInViewMode());
		routingNumberItem = new TextItem(messages2.routingNumber(), "rountingNumber");
		routingNumberItem.setEnabled(!isInViewMode());

		DynamicForm bankForm = new DynamicForm("empOtherDetailsLeftForm");
		bankForm.add(bankAccountNumberItem, bankNameItem, bankBranchItem, routingNumberItem);
		
		return bankForm;
	}

	private CaptionPanel getEmpDetails() {
		CaptionPanel employeeDetailsInfo = new CaptionPanel(
				messages.employeeDetailsInfo());
		DOM.setStyleAttribute(employeeDetailsInfo.getElement(), "border",
				"1px solid #ccc");
		empDetailsInfoForm = new DynamicForm("empDetailsInfoForm");

		employeeIdItem = new TextItem(messages.employeeID(), "employeeIdItem");
		employeeIdItem.setEnabled(!isInViewMode());
		dateOfHire = new DateField(messages.dateofHire(), "dateOfHire");
		dateOfHire.setEnteredDate(new ClientFinanceDate());
		dateOfHire.setEnabled(!isInViewMode());
		panItem = new TextItem(messages.panOrEinNumber(), "panItem");
		panItem.setRequired(true);
		panItem.setEnabled(!isInViewMode());
		
		activeOrInactive = new CheckboxItem(messages.active(),
				"activeOrInactive");
		activeOrInactive.setValue(true);
		activeOrInactive.setEnabled(!isInViewMode());
		
		reasonCombo = new SelectCombo(messages2.reasonForInactive());
		reasonCombo.setRequired(true);
		reasonCombo.setComboItem(reasons[0]);
		reasonCombo.setEnabled(!isInViewMode());
		listOfReaons = new ArrayList<String>();
		for (int i = 0; i < reasons.length; i++) {
			listOfReaons.add(reasons[i]);
		}
		reasonCombo.initCombo(listOfReaons);
		
		lastDateItem = new DateField(messages2.lastDate(), "employeeLastDate");
		lastDateItem.setEnteredDate(new ClientFinanceDate());
		lastDateItem.setEnabled(!isInViewMode());
		lastDateItem.setVisible(!activeOrInactive.getValue());
		reasonCombo.setVisible(!activeOrInactive.getValue());
		
		activeOrInactive.addChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				lastDateItem.setVisible(!activeOrInactive.getValue());
				reasonCombo.setVisible(!activeOrInactive.getValue());

			}
		});
		
		employeeGroupCombo = new EmployeeGroupCombo(messages.employeeGroup(),
				true);
		employeeGroupCombo.setEnabled(!isInViewMode());
		designationItem = new TextItem(messages.designation(),
				"designationItem");
		designationItem.setEnabled(!isInViewMode());
		locationItem = new SelectItem(messages.workingLocation(), "locationItem");
		locationItem.setEnabled(!isInViewMode());
		locationItem.setRequired(true);
		getStates(locationItem);
		addCustomFieldButton = new Button();
		addCustomFieldButton.setText(messages.ManageCustomFields());
		addCustomFieldButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				customFieldDialog = new CustomFieldDialog(NewEmployeeView.this,
						messages.CustomField(), messages.ManageCustomFields());
				ViewManager.getInstance().showDialog(customFieldDialog);
			}
		});
		addCustomFieldButton.setEnabled(!isInViewMode());
		customFieldForm = UIUtils.CustomFieldsform(messages.terms());

		empDetailsInfoForm.add(employeeIdItem, activeOrInactive, dateOfHire, lastDateItem, reasonCombo, 
				employeeGroupCombo, designationItem, locationItem);
		empDetailsInfoForm.add(addCustomFieldButton);
		empDetailsInfoForm.add(customFieldForm);
		employeeDetailsInfo.setContentWidget(empDetailsInfoForm);
		return employeeDetailsInfo;
	}

	private StyledPanel getEmpBasicInfo() {
		
		StyledPanel mainVLay = new StyledPanel("tablePanel");
		
		CaptionPanel employeeBasicInfo = new CaptionPanel(
				messages.employeeBasicInfo());
		DOM.setStyleAttribute(employeeBasicInfo.getElement(), "border",
				"1px solid #cccccc"); 

		basicInfoForm = new DynamicForm("basicInfoForm");
		DOM.setStyleAttribute(basicInfoForm.getElement(), "border",
				"1px solid #cccccc");

		//nameItem = new TextItem(messages.name(), "nameItem");
		nameItem = new TextItem(messages.firstName(), "nameItem");
		nameItem.setRequired(true);
		nameItem.setEnabled(!isInViewMode());
		
		lnameItem = new TextItem(messages.lastName(), "lnameItem");
		lnameItem.setRequired(true);
		lnameItem.setEnabled(!isInViewMode());
		
		//ssnItem = new TextItem(messages2.ssn(), "ssnItem");
		ssnItem = new PasswordItem(messages2.ssn());
		ssnItem.setRequired(true);
		ssnItem.setEnabled(!isInViewMode()); 

		dateOfBirthItem = new DateField(messages.dateofBirth(),
				"dateOfBirthItem");
		dateOfBirthItem.setEnteredDate(new ClientFinanceDate());
		dateOfBirthItem.setEnabled(!isInViewMode());
		
		genderSelect = new SelectCombo(messages.gender());
		genderSelect.setEnabled(!isInViewMode());
		listOfgenders = new ArrayList<String>();
		for (int i = 0; i < genderTypes.length; i++) {
			listOfgenders.add(genderTypes[i]);
		}
		genderSelect.initCombo(listOfgenders);

		activeOrInactive = new CheckboxItem(messages.active(),
				"activeOrInactive");
		activeOrInactive.setValue(true);
		activeOrInactive.setEnabled(!isInViewMode());

		reasonCombo = new SelectCombo(messages2.reasonForInactive());
		reasonCombo.setRequired(true);
		reasonCombo.setComboItem(reasons[0]);
		reasonCombo.setEnabled(!isInViewMode());
		listOfReaons = new ArrayList<String>();
		for (int i = 0; i < reasons.length; i++) {
			listOfReaons.add(reasons[i]);
		}
		reasonCombo.initCombo(listOfReaons);

		lastDateItem = new DateField(messages2.lastDate(), "employeeLastDate");
		lastDateItem.setEnteredDate(new ClientFinanceDate());
		lastDateItem.setEnabled(!isInViewMode());
		lastDateItem.setVisible(!activeOrInactive.getValue());
		reasonCombo.setVisible(!activeOrInactive.getValue());
		
		contactNumberItem = new TextItem(messages.contactNumber(),
				"contactNumberItem");
		contactNumberItem.setEnabled(!isInViewMode());
		emailItem = new TextItem(messages.email(), "emailItem");
		emailItem.setEnabled(!isInViewMode());
		addrArea = new TextAreaItem(messages.address(), "addrArea");
		addrArea.setRequired(true);
		addrArea.setDisabled(isInViewMode());
		addrArea.setEnabled(!isInViewMode());
		addrArea.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				new AddressDialog("", "", addrArea, "Bill to", allAddresses);

			}
		});

		addrArea.addFocusHandler(new FocusHandler() {

			@Override
			public void onFocus(FocusEvent event) {
				new AddressDialog("", "", addrArea, "Bill to", allAddresses);

			}
		});

		activeOrInactive.addChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				lastDateItem.setVisible(!activeOrInactive.getValue());
				reasonCombo.setVisible(!activeOrInactive.getValue());

			}
		});
		basicInfoForm.add(nameItem, lnameItem, ssnItem, dateOfBirthItem, genderSelect,
				 contactNumberItem, emailItem, addrArea);

		mainVLay.add(basicInfoForm);
		
		//mainVLay.add(getTablePanel());
		
		return mainVLay;
	}
	
	private StyledPanel getEmpExpencesInfo() {
		
		StyledPanel empExpLay = new StyledPanel("tablePanel");
		
		CaptionPanel employeeExpInfo = new CaptionPanel(
				messages2.empExpences());
		DOM.setStyleAttribute(employeeExpInfo.getElement(), "border",
				"1px solid #cccccc"); 

		attendanceForm = new DynamicForm("attendanceForm");
		DOM.setStyleAttribute(attendanceForm.getElement(), "border",
				"1px solid #cccccc");

		  employeeAttendanceTable = new EmployeeAttendanceTable();
		  employeeAttendanceTable.setEnabled(!isInViewMode());
		  
		  attendanceForm.add(employeeAttendanceTable);
		  attendanceForm.add(getAttendanceAddNewButton());
		
		empExpLay.add(attendanceForm);
		
		return empExpLay;
	}
	
	private GwtDisclosurePanel getGwtDisclosurePanel(String title, Widget child) {
		StyledPanel panel = new StyledPanel("tablePanel");
		GwtDisclosurePanel disclosurePanel = (GwtDisclosurePanel) GWT
				.create(GwtDisclosurePanel.class);
		disclosurePanel.setTitle(title);
		panel.add(child);
		disclosurePanel.setContent(panel);
		return disclosurePanel;
	}
	
	private FlowPanel getTablePanel() {
		
		StyledPanel mainVLay = new StyledPanel("tablePanel");
		
		employeTaxTable = new EmployeeTaxTable() {
			@Override
			protected boolean isInViewMode() {
				return NewEmployeeView.this.isInViewMode();
			}
		};

		employeTaxTable.setEnabled(!isInViewMode());

		this.taxFlowPanel = new StyledPanel("taxFlowPanel");

		taxDisclosurePanel = (GwtDisclosurePanel) GWT
				.create(GwtDisclosurePanel.class);
		taxDisclosurePanel.setTitle(messages2.itemizebyTax());
		taxFlowPanel.add(employeTaxTable);
		taxFlowPanel.add(getTaxAddNewButton());
		taxDisclosurePanel.setContent(taxFlowPanel);
		
		employeeCompTable = new EmployeCompensationTable() {
			@Override
			protected boolean isInViewMode() {
				return NewEmployeeView.this.isInViewMode();
			}
		};
		employeeCompTable.setEnabled(!isInViewMode());

		this.empCompFlowPanel = new StyledPanel("empCompFlowPanel");
		empCompFlowPanel.add(employeeCompTable);
		empCompFlowPanel.add(geCmpAddNewButton());
		empCompDisclosurePanel = (GwtDisclosurePanel) GWT
				.create(GwtDisclosurePanel.class);
		empCompDisclosurePanel.setTitle(messages2.itemizebyCompensation());
		empCompDisclosurePanel.setContent(empCompFlowPanel);
		
		mainVLay.add(taxDisclosurePanel.getPanel());
		mainVLay.add(empCompDisclosurePanel.getPanel());
		
		return mainVLay;
	}

	@Override
	public void saveAndUpdateView() {
		updateData();
		saveOrUpdate(getData());
	}

	@Override
	public void saveFailed(AccounterException exception) {
		super.saveFailed(exception);
		AccounterException accounterException = exception;
		String errorString = AccounterExceptions
				.getErrorString(accounterException);
		Accounter.showError(errorString);
	}
	
	protected AddNewButton getTaxAddNewButton() {
		AddNewButton itemTableButton = new AddNewButton(
				messages.addNew(messages2.employeeTax()));
		itemTableButton.setEnabled(!isInViewMode());
		itemTableButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ClientEmployeeTax tax = new ClientEmployeeTax();
				tax.setIsSSNTaxable(true);
				tax.setMedicareTaxable(true);
				tax.setTaxFilingStatus(1);
				NewEmployeeView.this.employeTaxTable.add(tax);
			}
		});
		return itemTableButton;
	}
	
	protected AddNewButton getAttendanceAddNewButton() {
		AddNewButton newEmpAttendceTableButton = new AddNewButton(
				messages.addNew(""));
		newEmpAttendceTableButton.setEnabled(!isInViewMode());
		newEmpAttendceTableButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ClientEmployeeAttendance newEmpAttendce = new ClientEmployeeAttendance();
				newEmpAttendce.setPayrollDate(null);
				newEmpAttendce.setMileshours("");
				newEmpAttendce.setAdvances("");
				newEmpAttendce.setFoodAllowances("");
				newEmpAttendce.setOtherAllowances("");
				NewEmployeeView.this.employeeAttendanceTable.add(newEmpAttendce);
			}
		});
		return newEmpAttendceTableButton;
	}

	protected AddNewButton geCmpAddNewButton() {
		AddNewButton accountTableButton = new AddNewButton(
				messages2.addNewcompensation());
		accountTableButton.setEnabled(!isInViewMode());
		accountTableButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ClientEmployeeCompsensation comp = new ClientEmployeeCompsensation();
				comp.setCompType(1);
				comp.setPayType(1);
				NewEmployeeView.this.employeeCompTable.add(comp);
			}
		});
		return accountTableButton;
	}

	private void updateData() {

		customFieldForm.updateValues(data.getCustomFieldValues(), getCompany(),
				ClientPayee.TYPE_EMPLOYEE);

		data.setName(nameItem.getValue());
		data.seteLastname(lnameItem.getValue());
		data.setSsn(ssnItem.getValue());

		data.setAddress(getAddresss());
		data.setBankAccountNo(bankAccountNumberItem.getValue());
		data.setBankName(bankNameItem.getValue());
		data.setBankBranch(bankBranchItem.getValue());
		data.setRoutingNumber(routingNumberItem.getValue());
		
		data.setActive(activeOrInactive.getValue());
		data.setPhoneNo(contactNumberItem.getValue());
		data.setPanNumber(panItem.getValue());
		

		data.setDateOfBirth(dateOfBirthItem.getValue().getDate());
		data.setPayeeSince(dateOfHire.getValue().getDate());

		data.setDesignation(designationItem.getValue());
		data.setEmail(emailItem.getValue());
		data.setContacts(new HashSet<ClientContact>());
		long groupId = 0;
		ClientEmployeeGroup selectedGroup = employeeGroupCombo
				.getSelectedValue();
		if (selectedGroup != null) {
			groupId = selectedGroup.getID();
		}
		data.setGroup(groupId);
		data.setLocation(locationItem.getValue());
		data.setNumber(employeeIdItem.getValue());
		
		/**data.setPassportExpiryDate(passportExpiryDateItem.getValue().getDate());
		data.setPassportNumber(passportNumberItem.getValue());
		data.setVisaNumber(emplVisaNumberItem.getValue());
		data.setVisaExpiryDate(emplVisaNumberDateItem.getValue().getDate()); 
		data.setCountryOfIssue(countryOfIssueItem.getValue());*/
		
		data.setLastDate(lastDateItem.getValue().getDate());
		data.setReasonType(getReasonType());
		int genderType = -1;
		String selectedValue = genderSelect.getSelectedValue();
		if (selectedValue != null) {
			if (selectedValue.equals(messages.unspecified())) {
				genderType = 0;
			} else if (selectedValue.equals(messages.male())) {
				genderType = 1;
			} else if (selectedValue.equals(messages.female())) {
				genderType = 2;
			}
		}
		data.setGender(genderType);
		
		if(data.getEmployeeTaxes() == null || data.getEmployeeTaxes() != null && data.getEmployeeTaxes().isEmpty()) {
			ClientEmployeeTax federalEmployeeTax = new ClientEmployeeTax();
			federalEmployeeTax.setTaxFilingStatus(getKeyValue(getFilingStatusMap(), filingStatus.getValue()));
			federalEmployeeTax.setAdditionalAmount(UIUtils.toDbl(additionalAmount.getNumber()));
			federalEmployeeTax.setTaxallowences(UIUtils.toInt(noOfAllowances.getNumber()));
			federalEmployeeTax.setIsSSNTaxable(ssnTaxble.isChecked());
			federalEmployeeTax.setMedicareTaxable(medicareTaxable.isChecked());
			federalEmployeeTax.setTaxResidencyType(getKeyValue(getTaxResidencyType(), taxResidencyType.getValue()));
			federalEmployeeTax.setTaxUnemployement(federalUnmploymentTax.isChecked());
			federalEmployeeTax.setTaxType(ClientEmployeeTax.TAX_TYPE_FEDERAL);
			
			ClientEmployeeTax clientEmployeeTax = new ClientEmployeeTax();
			clientEmployeeTax.setTaxFilingStatus(getKeyValue(getFilingStatusMap(), sfilingStatus.getValue()));
			clientEmployeeTax.setAdditionalAmount(UIUtils.toDbl(sadditionalAmount.getNumber()));
			clientEmployeeTax.setTaxallowences(UIUtils.toInt(snoOfAllowances.getNumber()));
			clientEmployeeTax.setTaxResidencyType(getKeyValue(getTaxResidencyType(), staxResidencyType.getValue()));
			clientEmployeeTax.setTaxUnemployement(stateUnmploymentTax.isChecked());
			clientEmployeeTax.setState(stateItem.getValue());
			clientEmployeeTax.setTaxType(ClientEmployeeTax.TAX_TYPE_STATE);
			
			List<ClientEmployeeTax> list = new ArrayList<ClientEmployeeTax>();
			list.add(federalEmployeeTax);
			list.add(clientEmployeeTax);
			data.setEmployeeTaxes(list);
		} else if (data.getEmployeeTaxes() != null) {
			for(ClientEmployeeTax tax : data.getEmployeeTaxes()) {
				 if(tax.getTaxType() == ClientEmployeeTax.TAX_TYPE_FEDERAL) {
					 tax.setTaxFilingStatus(getKeyValue(getFilingStatusMap(), filingStatus.getValue()));
					 tax.setAdditionalAmount(UIUtils.toDbl(additionalAmount.getNumber()));
					 tax.setTaxallowences(UIUtils.toInt(noOfAllowances.getNumber()));
					 tax.setIsSSNTaxable(ssnTaxble.isChecked());
					 tax.setMedicareTaxable(medicareTaxable.isChecked());
					 tax.setTaxResidencyType(getKeyValue(getTaxResidencyType(), taxResidencyType.getValue()));
					 tax.setTaxUnemployement(federalUnmploymentTax.isChecked());
					 tax.setTaxType(ClientEmployeeTax.TAX_TYPE_FEDERAL);
				 } else {
					 tax.setTaxFilingStatus(getKeyValue(getFilingStatusMap(), sfilingStatus.getValue()));
					 tax.setAdditionalAmount(UIUtils.toDbl(sadditionalAmount.getNumber()));
					 tax.setTaxallowences(UIUtils.toInt(snoOfAllowances.getNumber()));
					 tax.setTaxResidencyType(getKeyValue(getTaxResidencyType(), staxResidencyType.getValue()));
					 tax.setTaxUnemployement(stateUnmploymentTax.isChecked());
					 tax.setState(stateItem.getValue());
					 tax.setTaxType(ClientEmployeeTax.TAX_TYPE_STATE);
				 }
			}
		}

		data.setAdditionalAmount(payadditionalAmount.getNumber());
		
		data.setPayType(getKeyValue(getPayTypeMap(), payType.getValue()));
		data.setPayFrequency(getKeyValue(getPayFrequency(), payFrequency.getValue()));
		
		data.setSalary(salaryAmountItem.getNumber());
		
		
		data.setEmployeeAttendances(this.employeeAttendanceTable.getAllRows());
		
		/*
		 * ClientEmployeeAttendance employeeAttendance = new ClientEmployeeAttendance();
		 * employeeAttendance.setPayrollDate(empPayrollDate.getValue().toString());
		 * employeeAttendance.setAdvances(advancePaymnts.getValue());
		 * employeeAttendance.setOtherAllowances(otherAllowances.getValue());
		 * employeeAttendance.setFoodAllowances(foodAllowances.getValue());
		 * employeeAttendance.setMileshours(milesPerHour.getValue());
		 */
		 
		 
		 
		 
	}

	private int getReasonType() {
		int reasonType = -1;
		String selectedValue = reasonCombo.getSelectedValue();
		if (selectedValue != null) {
			if (selectedValue.equals(messages2.gotNewJobOffer())) {
				reasonType = 0;
			} else if (selectedValue.equals(messages2.quitWithOutAjob())) {
				reasonType = 1;
			} else if (selectedValue.equals(messages2.lackofPerformance())) {
				reasonType = 2;
			} else if (selectedValue.equals(messages2
					.disputesbetweenCoworkers())) {
				reasonType = 3;
			} else if (selectedValue.equals(messages2.nosatisfactionwithJob())) {
				reasonType = 4;
			} else if (selectedValue.equals(messages2.notenoughHours())) {
				reasonType = 5;
			} else if (selectedValue.equals(messages2.jobwasTemporary())) {
				reasonType = 6;
			} else if (selectedValue.equals(messages2.contractended())) {
				reasonType = 7;
			} else if (selectedValue.equals(messages2.workwasSeasonal())) {
				reasonType = 8;
			} else if (selectedValue.equals(messages2.betteropportunity())) {
				reasonType = 9;
			} else if (selectedValue.equals(messages2.seekinggrowth())) {
				reasonType = 10;
			} else if (selectedValue.equals(messages2.careerchange())) {
				reasonType = 11;
			} else if (selectedValue.equals(messages2.returnedtoSchool())) {
				reasonType = 12;
			} else if (selectedValue.equals(messages2.relocated())) {
				reasonType = 13;
			} else if (selectedValue.equals(messages2.raisedaFamily())) {
				reasonType = 14;
			} else if (selectedValue.equals(messages.other())) {
				reasonType = 15;
			}

		}
		return reasonType;

	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = new ValidationResult();
		result.add(basicInfoForm.validate());
		result.add(empDetailsInfoForm.validate());
		result.add(cmpensationForm.validate());
		result.add(federTaxForm.validate());
		result.add(stateTaxForm.validate());
		String name = nameItem.getValue();
		ClientEmployee employee = getCompany().getEmployeeByName(name);
		if (employee != null && !(this.getData().getID() == employee.getID())) {
			result.addError(nameItem, messages.alreadyExist());
			return result;
		}

		return result;
	}

	@Override
	public void onEdit() {
		AccounterAsyncCallback<Boolean> editCallBack = new AccounterAsyncCallback<Boolean>() {

			@Override
			public void onException(AccounterException caught) {
				int errorCode = caught.getErrorCode();

				Accounter.showError(AccounterExceptions
						.getErrorString(errorCode));
			}

			@Override
			public void onResultSuccess(Boolean result) {
				enableFormItems();
			}

		};

		this.rpcDoSerivce.canEdit(AccounterCoreType.EMPLOYEE, data.getID(),
				editCallBack);

	}

	@Override
	public void deleteFailed(AccounterException caught) {
		// TODO Auto-generated method stub

	}

	private void enableFormItems() {
		setMode(EditMode.EDIT);
		nameItem.setEnabled(!isInViewMode());
		lnameItem.setEnabled(!isInViewMode());
		ssnItem.setEnabled(!isInViewMode());
		employeeIdItem.setEnabled(!isInViewMode());
		designationItem.setEnabled(!isInViewMode());
		panItem.setEnabled(!isInViewMode());
		bankNameItem.setEnabled(!isInViewMode());
		bankAccountNumberItem.setEnabled(!isInViewMode());
		bankBranchItem.setEnabled(!isInViewMode());
		routingNumberItem.setEnabled(!isInViewMode());
		locationItem.setEnabled(!isInViewMode());
		contactNumberItem.setEnabled(!isInViewMode());
		emailItem.setEnabled(!isInViewMode());
		/**passportNumberItem.setEnabled(!isInViewMode());
		countryOfIssueItem.setEnabled(!isInViewMode());
		passportExpiryDateItem.setEnabled(!isInViewMode());
		emplVisaNumberDateItem.setEnabled(!isInViewMode());
		emplVisaNumberItem.setEnabled(!isInViewMode());*/
		dateOfBirthItem.setEnabled(!isInViewMode());
		dateOfHire.setEnabled(!isInViewMode());
		employeeGroupCombo.setEnabled(!isInViewMode());
		genderSelect.setEnabled(!isInViewMode());
		addrArea.setEnabled(!isInViewMode());
		activeOrInactive.setEnabled(!isInViewMode());
		lastDateItem.setEnabled(!isInViewMode());
		reasonCombo.setEnabled(!isInViewMode());
		customFieldForm.setEnabled(!isInViewMode());
		addCustomFieldButton.setEnabled(!isInViewMode());
		
		milesPerHour.setEnabled(!isInViewMode());
		foodAllowances.setEnabled(!isInViewMode());
		otherAllowances.setEnabled(!isInViewMode());
		advancePaymnts.setEnabled(!isInViewMode());
		empPayrollDate.setEnabled(!isInViewMode());
		employeeAttendanceTable.setEnabled(!isInViewMode());
		
		enableOrDisableItem(sadditionalAmount, staxResidencyType, ssnTaxble, snoOfAllowances, stateUnmploymentTax, sfilingStatus, stateItem, sfilingStatus, additionalAmount, 
				medicareTaxable, ssnTaxble, federalUnmploymentTax,  taxResidencyType, noOfAllowances, filingStatus);
		
		enableOrDisableItem(salaryAmountItem, payType, payadditionalAmount, payFrequency, routingNumberItem);
		
		super.onEdit();

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {
		// TODO Auto-generated method stub

	}

	@Override
	protected String getViewTitle() {
		return messages.newEmployee();
	}

	@Override
	public List<DynamicForm> getForms() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setFocus() {
		nameItem.setFocus();
	}

}
