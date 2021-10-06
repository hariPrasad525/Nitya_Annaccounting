package com.vimukti.accounter.web.client.ui.vat;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Label;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientTDSDeductorMasters;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.AddressDialog;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.core.EmailField;
import com.vimukti.accounter.web.client.ui.core.IntegerField;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class TDSDeductorDetailsView extends BaseView<ClientTDSDeductorMasters> {

	private TextItem deductorName;
	private TextItem branchName;
	private TextItem flatNo;
	private TextItem buildingName;
	private TextItem streetName;
	private TextItem areaName;
	private TextItem cityName;
	private IntegerField pinNumber;
	private IntegerField telephoneNumber;
	private IntegerField faxNumber;
	private SelectCombo addressChangeCombo;
	private EmailField email;
	private SelectCombo statusCombo;
	private SelectCombo deductorTypeOther;
	private SelectCombo govtState;
	private TextItem paoCode;
	private IntegerField paoRegistration;
	private TextItem ddoCode;
	private TextItem ddoRegistration;
	private SelectCombo ministryCombo;
	private TextItem ministryNameOtehr;
	private SelectCombo deductorTypeGovernment;
	protected String stateSelected;
	private boolean true_falseValue;
	protected String statusSelected;
	private String deductorTypeSelected;
	private TextItem stdNumber;
	private SelectCombo stateCombo;
	private TextItem panNumber;
	private TextItem tanNumber;
	private CheckboxItem addressSameBox;
	private boolean isViewInitialised;
	private TextAreaItem taxOfficeAddrItem;
	DynamicForm taxDynamicForm, otherDynamicForm;
	protected LinkedHashMap<Integer, ClientAddress> taxOfficeAddresses;
	private DynamicForm mainPanel;

	@Override
	public void init() {
		super.init();
		this.getElement().setId("TDSDeductorDetailsView");
		taxOfficeAddresses = new LinkedHashMap<Integer, ClientAddress>();
		createControls();

		if (data != null) {
			onEdit();
		}

	}

	private void createControls() {

		Label titleLabel = new Label(messages.deducatorMastersDetails());
		titleLabel.removeStyleName("gwt-Label");
		titleLabel.addStyleName("label-title");
		deductorName = new TextItem(messages.name(), "deductorName", 75);
		deductorName.setRequired(true);
		deductorName.setEnabled(!isInViewMode());

		branchName = new TextItem(messages.branchOrdivison(), "branchName", 75);
		branchName.setEnabled(!isInViewMode());

		flatNo = new TextItem(messages.flatNo(), "flatNo", 25);
		flatNo.setRequired(true);
		flatNo.setEnabled(!isInViewMode());

		buildingName = new TextItem(messages.nameOfPremisis(), "buildingName",
				25);
		buildingName.setEnabled(!isInViewMode());

		streetName = new TextItem(messages.streetOrRoadName(), "streetName", 25);
		streetName.setEnabled(!isInViewMode());

		areaName = new TextItem(messages.area(), "areaName", 25);
		areaName.setEnabled(!isInViewMode());

		cityName = new TextItem(messages.cityOrTown(), "cityName", 25);
		cityName.setEnabled(!isInViewMode());

		stateCombo = new SelectCombo(messages.state());
		stateCombo.initCombo(getStatesList());
		stateCombo.setEnabled(!isInViewMode());
		stateCombo.setRequired(true);
		stateCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						stateSelected = selectItem;
					}
				});

		pinNumber = new IntegerField(this, messages.postalCode(), 6);
		pinNumber.setEnabled(!isInViewMode());
		pinNumber.setRequired(true);

		addressChangeCombo = new SelectCombo(
				messages.hasAddressChangedSinceLastReturn());
		addressChangeCombo.initCombo(getYESNOList());
		addressChangeCombo.setEnabled(!isInViewMode());
		addressChangeCombo.setRequired(true);
		addressChangeCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						if (selectItem.equals(getYESNOList().get(1))) {
							true_falseValue = true;
						} else {
							true_falseValue = false;
						}
					}
				});
		stdNumber = new TextItem(messages.STDCode(), "stdNumber", 5);
		stdNumber.setEnabled(!isInViewMode());

		telephoneNumber = new IntegerField(this, messages.telephoneNo(), 10);
		telephoneNumber.setEnabled(!isInViewMode());

		faxNumber = new IntegerField(this, messages.faxNumber());
		faxNumber.setEnabled(!isInViewMode());

		panNumber = new TextItem(messages.panNumber(), "panNumber", 10);
		panNumber.setEnabled(!isInViewMode());
		panNumber.setRequired(true);

		tanNumber = new TextItem(messages.tanNumber(), "tanNumber", 10);
		tanNumber.setEnabled(!isInViewMode());
		tanNumber.setRequired(true);

		addressSameBox = new CheckboxItem(
				messages.addressSameForResponsiblePersonAlso(),
				"addressSameBox");
		addressSameBox.setEnabled(!isInViewMode());

		taxOfficeAddrItem = new TextAreaItem(messages.taxOfficeAddress(),
				"taxOfficeAddress");
		taxOfficeAddrItem.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				if (taxOfficeAddresses.isEmpty()) {
					taxOfficeAddresses.put(0, null);
				}
				new AddressDialog("", "", taxOfficeAddrItem, messages
						.taxOfficeAddress(), taxOfficeAddresses);
			}
		});
		email = new EmailField(messages.email(), 75);
		email.setRequired(true);
		// email.setHelpInformation(true);
		email.setEnabled(!isInViewMode());

		statusCombo = new SelectCombo(messages.status());
		statusCombo.initCombo(getStatusTypes());
		statusCombo.setEnabled(!isInViewMode());
		statusCombo.setRequired(true);
		statusCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						statusSelected = selectItem;

						if (statusSelected.equals(getStatusTypes().get(0))) {
							otherSelected();

						} else {
							governmentSelected();
						}
					}
				});

		deductorTypeOther = new SelectCombo(messages.deducatorType());
		deductorTypeOther.initCombo(getOthersList());
		deductorTypeOther.setSelectedItem(0);
		deductorTypeOther.setEnabled(!isInViewMode());
		deductorTypeOther.setRequired(true);
		deductorTypeOther
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						deductorTypeSelected = selectItem;
					}
				});

		deductorTypeGovernment = new SelectCombo(messages.deducatorType());
		deductorTypeGovernment.initCombo(getGovtList());
		deductorTypeGovernment.setSelectedItem(0);
		deductorTypeGovernment.setEnabled(isInViewMode());
		if ((getGovtList() != null) && (!getGovtList().isEmpty())) {
			deductorTypeGovernment.setComboItem(getGovtList().get(0));
		}
		deductorTypeGovernment.setRequired(true);
		deductorTypeGovernment
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						deductorTypeSelected = selectItem;
					}
				});

		govtState = new SelectCombo(messages.state());
		govtState.initCombo(getStatesList());
		govtState
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						stateSelected = selectItem;
					}
				});

		paoCode = new TextItem(messages.PAOCode(), "paoCode", 20);
		paoCode.setEnabled(!isInViewMode());

		paoRegistration = new IntegerField(this, messages.PAORegistration(), 7);
		paoRegistration.setEnabled(!isInViewMode());

		ddoCode = new TextItem(messages.ddoCode(), "telephoneNumber", 20);
		ddoCode.setEnabled(!isInViewMode());

		ddoRegistration = new TextItem(messages.ddoRegistrationNumber(),
				"ddoRegistration", 10);
		ddoRegistration.setEnabled(!isInViewMode());

		ministryCombo = new SelectCombo(messages.ministry());
		ministryCombo.initCombo(getMinistryType());
		ministryCombo.setSelectedItem(0);
		ministryCombo.setEnabled(isInViewMode());
		ministryCombo.setRequired(true);
		ministryCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						if (selectItem.equals(getMinistryType().get(57))) {
							ministryNameOtehr.setEnabled(false);
							ministryNameOtehr.setRequired(true);
						} else {
							ministryNameOtehr.setEnabled(true);
							ministryNameOtehr.setRequired(false);
						}
					}
				});

		ministryNameOtehr = new TextItem(messages.ministryName(),
				"ministryNameOtehr", 150);
		ministryNameOtehr.setEnabled(true);
		ministryNameOtehr.setRequired(false);

		paoCode.setEnabled(true);
		paoRegistration.setEnabled(true);
		ddoCode.setEnabled(true);
		ddoRegistration.setEnabled(true);
		ministryCombo.setEnabled(true);
		ministryNameOtehr.setEnabled(true);
		govtState.setEnabled(true);

		mainPanel = new DynamicForm("panel2");
		mainPanel.add(titleLabel);
		StyledPanel styledPanel = getMainPanel();
		taxDynamicForm = new DynamicForm("taxDynamicForm");
		otherDynamicForm = new DynamicForm("otherDynamicForm");
		if (styledPanel != null) {
			taxDynamicForm.add(deductorName, branchName, flatNo, buildingName,
					streetName, areaName, cityName, stateCombo, pinNumber,
					addressChangeCombo, stdNumber, telephoneNumber, faxNumber,
					email);
			styledPanel.add(taxDynamicForm);
			otherDynamicForm.add(statusCombo, deductorTypeOther,
					deductorTypeGovernment, govtState, paoCode,
					paoRegistration, ddoCode, ddoRegistration, ministryCombo,
					ministryNameOtehr, panNumber, tanNumber, addressSameBox,
					taxOfficeAddrItem);
			styledPanel.add(otherDynamicForm);
			mainPanel.add(styledPanel);
		} else {
			mainPanel.add(deductorName, branchName, flatNo, buildingName,
					streetName, areaName, cityName, stateCombo, pinNumber,
					addressChangeCombo, stdNumber, telephoneNumber, faxNumber,
					email);
			mainPanel.add(statusCombo, deductorTypeOther,
					deductorTypeGovernment, govtState, paoCode,
					paoRegistration, ddoCode, ddoRegistration, ministryCombo,
					ministryNameOtehr, panNumber, tanNumber, addressSameBox,
					taxOfficeAddrItem);
		}

		this.add(mainPanel);

		deductorTypeOther.hide();
		deductorTypeGovernment.hide();

		if (data != null) {
			updateControls();
		} else {
			updateControlsForNew();
		}

		isViewInitialised = true;

	}

	protected StyledPanel getMainPanel() {
		return new StyledPanel("panel1");
	}

	protected void governmentSelected() {
		deductorTypeOther.hide();
		deductorTypeGovernment.show();
		deductorTypeSelected = deductorTypeGovernment.getSelectedValue();
		paoCode.setEnabled(true);
		paoRegistration.setEnabled(true);
		ddoCode.setEnabled(true);
		ddoRegistration.setEnabled(true);
		ministryCombo.setEnabled(true);
		ministryNameOtehr.setEnabled(true);
		govtState.setEnabled(true);

		paoCode.setRequired(true);
		paoRegistration.setRequired(true);
		ddoCode.setRequired(true);
		ddoRegistration.setRequired(true);
		ministryCombo.setRequired(true);
		ministryNameOtehr.setRequired(true);
		govtState.setRequired(true);

	}

	protected void otherSelected() {
		deductorTypeOther.show();
		deductorTypeGovernment.hide();
		deductorTypeSelected = deductorTypeOther.getSelectedValue();
		paoCode.setEnabled(false);
		paoRegistration.setEnabled(false);
		ddoCode.setEnabled(false);
		ddoRegistration.setEnabled(false);
		ministryCombo.setEnabled(false);
		ministryNameOtehr.setEnabled(false);
		govtState.setEnabled(false);

		paoCode.setRequired(false);
		paoRegistration.setRequired(false);
		ddoCode.setRequired(false);
		ddoRegistration.setRequired(false);
		ministryCombo.setRequired(false);
		ministryNameOtehr.setRequired(false);
		govtState.setRequired(false);

	}

	private void updateControls() {
		deductorName.setValue(data.getDeductorName());
		branchName.setValue(data.getBranch());
		flatNo.setValue(data.getFlatNo());
		buildingName.setValue(data.getBuildingName());
		streetName.setValue(data.getRoadName());
		areaName.setValue(data.getArea());
		cityName.setValue(data.getCity());
		pinNumber.setValue(Long.toString(data.getPinCode()));
		telephoneNumber.setValue(Long.toString(data.getTelephoneNumber()));
		faxNumber.setValue(Long.toString(data.getFaxNo()));

		stateCombo.setSelected(data.getState());

		if (data.isAddressdChanged()) {
			addressChangeCombo.setSelected(getYESNOList().get(0));
		} else {
			addressChangeCombo.setSelected(getYESNOList().get(1));
		}

		email.setValue(data.getEmailID());

		stateSelected = data.getState();
		statusSelected = data.getStatus();
		deductorTypeSelected = data.getDeductorType();

		if (data.getStatus().equals(getStatusTypes().get(0))) {
			otherSelected();
			statusCombo.setSelected(data.getStatus());
			deductorTypeOther.setSelected(data.getDeductorType());
			deductorTypeOther.setVisible(true);

		} else {
			governmentSelected();
			statusCombo.setSelected(data.getStatus());
			deductorTypeGovernment.setValue(data.getDeductorType());
			paoCode.setValue(data.getPaoCode());
			paoRegistration.setValue(Long.toString(data.getPaoRegistration()));
			ddoCode.setValue(data.getDdoCode());
			ddoRegistration.setValue(data.getDdoRegistration());
			ministryCombo.setValue(data.getMinistryDeptName());
			ministryNameOtehr.setValue(data.getMinistryDeptOtherName());
			govtState.setSelected(data.getGovtState());

		}

		panNumber.setValue(data.getPanNumber());
		tanNumber.setValue(data.getTanNumber());
		addressSameBox.setValue(data.isAddressSameForResopsiblePerson());
		stdNumber.setValue(data.getStdCode());
		if (data.getTaxOfficeAddress() != null) {
			taxOfficeAddresses.put(7, data.getTaxOfficeAddress());
			taxOfficeAddrItem.setValue(getValidAddress(data
					.getTaxOfficeAddress()));
		}
	}

	protected String getValidAddress(ClientAddress address) {
		String toToSet = new String();
		if (address.getAddress1() != null && !address.getAddress1().isEmpty()) {
			toToSet = address.getAddress1().toString() + "\n";
		}

		if (address.getStreet() != null && !address.getStreet().isEmpty()) {
			toToSet += address.getStreet().toString() + "\n";
		}

		if (address.getCity() != null && !address.getCity().isEmpty()) {
			toToSet += address.getCity().toString() + "\n";
		}

		if (address.getStateOrProvinence() != null
				&& !address.getStateOrProvinence().isEmpty()) {
			toToSet += address.getStateOrProvinence() + "\n";
		}
		if (address.getZipOrPostalCode() != null
				&& !address.getZipOrPostalCode().isEmpty()) {
			toToSet += address.getZipOrPostalCode() + "\n";
		}
		if (address.getCountryOrRegion() != null
				&& !address.getCountryOrRegion().isEmpty()) {
			toToSet += address.getCountryOrRegion();
		}
		return toToSet;
	}

	private List<String> getMinistryType() {
		List<String> names = new ArrayList<String>();
		names = Utility.getMinistryType();
		return names;
	}

	private List<String> getOthersList() {

		List<String> names = new ArrayList<String>();
		for (int i = 8; i < 16; i++) {
			names.add(Utility.getDeductorTypes().get(i));
		}
		return names;
	}

	private List<String> getGovtList() {

		List<String> names = new ArrayList<String>();
		for (int i = 0; i < 8; i++) {
			names.add(Utility.getDeductorTypes().get(i));
		}
		return names;
	}

	private List<String> getStatusTypes() {
		List<String> names = new ArrayList<String>();
		names.add(messages.other());
		names.add(messages.government());
		return names;
	}

	@Override
	public void deleteFailed(AccounterException caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {
		// TODO Auto-generated method stub

	}

	@Override
	protected String getViewTitle() {
		return messages.particularOfDeducator();
	}

	@Override
	public List<DynamicForm> getForms() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = new ValidationResult();

		if (getMainPanel() != null) {
			result.add(taxDynamicForm.validate());
			result.add(otherDynamicForm.validate());
		} else {
			result.add(mainPanel.validate());
		}

		if (stateSelected == null) {
			result.addError(govtState, messages.pleaseSelect(messages.state()));
		}
		if (deductorTypeSelected == null) {
			result.addError(statusCombo,
					messages.pleaseSelect(messages.deducatorType()));
		}
		if (statusSelected == null) {
			result.addError(statusCombo,
					messages.pleaseSelect(messages.status()));
		}

		if (email.getValue() == null || email.getValue().equals("")
				|| email.getValue().length() == 0) {
			email.highlight();
			result.addError(email, messages.pleaseEnter(messages.email()));
		} else if (!UIUtils.isValidEmail(email.getValue())) {
			result.addError(email, messages.invalidEmail());
		}

		return result;

	}

	@Override
	public void saveAndUpdateView() {
		updateObject();

		saveOrUpdate(getData());

	}

	private void updateObject() {

		data.setDeductorName(deductorName.getValue());

		data.setBranch(branchName.getValue());

		data.setFlatNo(flatNo.getValue());

		data.setBuildingName(buildingName.getValue());

		data.setRoadName(streetName.getValue());

		data.setArea(areaName.getValue());

		data.setCity(cityName.getValue());

		data.setState(stateSelected);

		data.setPinCode(pinNumber.getNumber());

		if (telephoneNumber.getValue().length() > 0) {
			data.setTelephoneNumber(telephoneNumber.getNumber());
		} else {
			data.setTelephoneNumber(0);
		}

		if (faxNumber.getValue().length() > 0) {
			data.setFaxNo(faxNumber.getNumber());
		} else {
			data.setFaxNo(0);
		}

		data.setAddressdChanged(true_falseValue);

		data.setEmailID(email.getValue());

		data.setStatus(statusSelected);

		data.setDeductorType(deductorTypeSelected);

		if (statusSelected.equals(getStatusTypes().get(1))) {
			// if (paoCode.getValue().length() > 0) {
			data.setPaoCode(paoCode.getValue());
			// } else {
			// data.setPaoCode("");
			// }
			if (paoRegistration.getValue().length() > 0) {
				data.setPaoRegistration(paoRegistration.getNumber());
			} else {
				data.setPaoRegistration(0);
			}
			// if (ddoCode.getValue().length() > 0) {
			data.setDdoCode(ddoCode.getValue());
			// } else {
			// data.setDdoCode(0);
			// }
			// if (ddoRegistration.getValue().length() > 0) {
			data.setDdoRegistration(ddoRegistration.getValue());
			// } else {
			// data.setDdoRegistration(0);
			// }
			data.setMinistryDeptName(ministryCombo.getSelectedValue());
			data.setMinistryDeptOtherName(ministryNameOtehr.getValue());
			data.setGovtState(govtState.getSelectedValue());
		} else {
			data.setPaoCode("");
			data.setPaoRegistration(0);
			data.setDdoCode("");
			data.setDdoRegistration("");
			data.setMinistryDeptName("");
			data.setMinistryDeptOtherName("");
			data.setGovtState("");
		}

		data.setPanNumber(panNumber.getValue());
		data.setTanNumber(tanNumber.getValue());
		data.setAddressSameForResopsiblePerson(addressSameBox.getValue());
		// if (stdNumber.getValue().length() > 0) {
		data.setStdCode(stdNumber.getValue());
		// } else {
		// data.setStdCode(0);
		// }
		data.setTaxOfficeAddress(taxOfficeAddresses.get(7));
	}

	@Override
	public void saveFailed(AccounterException exception) {
		super.saveFailed(exception);

		AccounterException accounterException = exception;
		int errorCode = accounterException.getErrorCode();
		String errorString = AccounterExceptions.getErrorString(errorCode);
		Accounter.showError(errorString);

		updateObject();

	}

	@Override
	protected void initRPCService() {
		// super.initRPCService();

		Accounter.createHomeService().getDeductorMasterDetails(
				new AccounterAsyncCallback<ClientTDSDeductorMasters>() {

					@Override
					public void onException(AccounterException exception) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onResultSuccess(ClientTDSDeductorMasters result) {
						if (result != null) {
							data = result;
							if (isViewInitialised) {
								updateControls();
							}
						} else {
							data = new ClientTDSDeductorMasters();
							if (isViewInitialised) {
								updateControlsForNew();
							}
						}
						setData(data);
					}
				});

	}

	@Override
	public void setData(ClientTDSDeductorMasters data) {
		super.setData(data);
		if (data == null || data.getID() == 0) {
			this.setMode(EditMode.CREATE);
		} else {
			this.setMode(EditMode.EDIT);
		}
	}

	private void updateControlsForNew() {
		ClientCompany company = getCompany();
		deductorName.setValue(company.getName());
		cityName.setValue(company.getRegisteredAddress().getCity());
		streetName.setValue(company.getRegisteredAddress().getStreet());
		areaName.setValue(company.getRegisteredAddress().getCountryOrRegion());
		// govtState.setValue(company.getRegisteredAddress()
		// .getStateOrProvinence());
		pinNumber.setValue(company.getRegisteredAddress().getZipOrPostalCode());
		telephoneNumber.setValue(company.getPhone());
		faxNumber.setValue(company.getFax());
	}

	@Override
	public void onEdit() {

		AccounterAsyncCallback<Boolean> editCallBack = new AccounterAsyncCallback<Boolean>() {

			@Override
			public void onException(AccounterException caught) {
				Accounter.showError(caught.getMessage());
			}

			@Override
			public void onResultSuccess(Boolean result) {
				if (result)
					enableFormItems();
			}

		};
		if (rpcDoSerivce != null) {
			this.rpcDoSerivce.canEdit(AccounterCoreType.TDSDEDUCTORMASTER,
					data.getID(), editCallBack);
		}
	}

	protected void enableFormItems() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void createButtons() {
		// TODO Auto-generated method stub
		super.createButtons();
		saveAndNewButton.setVisible(false);
	}

	@Override
	protected boolean canDelete() {
		return false;
	}

}
