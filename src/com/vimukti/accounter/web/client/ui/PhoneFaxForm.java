package com.vimukti.accounter.web.client.ui;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientFax;
import com.vimukti.accounter.web.client.core.ClientPhone;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

/**
 * 
 * @author venki.p
 * 
 */
public class PhoneFaxForm extends DynamicForm {
	protected static AccounterMessages messages = Global.get().messages();

	private SelectCombo businessPhoneSelect, businessFaxSelect;
	public TextItem businessPhoneText;
	public TextItem businessFaxText;
	private LinkedHashMap<Integer, ClientPhone> allPhones;
	private LinkedHashMap<Integer, ClientFax> allFaxes;
	private ClientPhone toBeShownPhone = null;
	private ClientFax toBeShownFax = null;
	private WidgetWithErrors errorWidget;

	public PhoneFaxForm(Set<ClientPhone> phones, Set<ClientFax> faxes,
			WidgetWithErrors widget, String category) {
		super("phoneFaxForm");
		this.errorWidget = widget;
		allPhones = new LinkedHashMap<Integer, ClientPhone>();
		allFaxes = new LinkedHashMap<Integer, ClientFax>();
		setPhonesAndFaxes(phones, faxes);
		businessPhoneSelect = new SelectCombo(messages.phone());
		// businessPhoneSelect.setWidth(85);
		businessPhoneSelect.getMainWidget().removeStyleName("gwt-ListBox");
		businessPhoneSelect.initCombo(new ClientPhone().getPhoneTypes());
		businessPhoneSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						ClientPhone p = allPhones.get(UIUtils
								.getPhoneType(businessPhoneSelect
										.getSelectedValue()));
						if (p != null)
							businessPhoneText.setValue(p.getNumber());
						else
							businessPhoneText.setValue("");
					}
				});

		businessPhoneText = new TextItem(messages.phone(),"businessPhoneText");
		businessPhoneText.setToolTip(messages.phoneNumberOf(category));
		businessPhoneText.setToolTip(messages.giveOf(messages.phoneNumber(),
				category));
		// businessPhoneText.setShowTitle(false);
		businessPhoneText.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				if (event != null) {
					try {
						String ph = businessPhoneText.getValue().toString();
						if (ph.equals("")) {
							errorWidget.clearAllErrors();
							return;
						}
						if (!ph.equals("") && !UIUtils.isValidPhone(ph)) {
							// BaseView.errordata.setHTML("<li> "
							// + AccounterErrorType.INCORRECTINFORMATION
							// + ".");
							// BaseView.commentPanel.setVisible(true);
							errorWidget.addError(this,
									messages.incorrectInformation());
							// Accounter
							// .showError(AccounterErrorType.INCORRECTINFORMATION);
							businessPhoneText.setValue("");
						} else {
							// BaseView.errordata.setHTML("");
							// BaseView.commentPanel.setVisible(false);
							errorWidget.clearAllErrors();
							ClientPhone phone = new ClientPhone();
							phone.setType(UIUtils
									.getPhoneType(businessPhoneSelect
											.getSelectedValue()));
							phone.setNumber(ph);
							allPhones.put(UIUtils
									.getPhoneType(businessPhoneSelect
											.getDisplayValue().toString()),
									phone);
						}
					} catch (Exception e) {
					}
				}
			}
		});
		if (toBeShownPhone != null) {
			businessPhoneSelect.setComboItem(UIUtils
					.getPhoneTypes(toBeShownPhone.getType()));
			businessPhoneText.setValue(toBeShownPhone.getNumber());
		} else
			businessPhoneSelect.setDefaultToFirstOption(true);
		businessFaxSelect = new SelectCombo(messages.fax());
		businessFaxSelect.getMainWidget().removeStyleName("gwt-ListBox");
		businessFaxSelect.initCombo(new ClientFax().getFaxTypes());
		businessFaxSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						ClientFax f = allFaxes.get(UIUtils
								.getFaxType(businessFaxSelect
										.getSelectedValue()));
						if (f != null)
							businessFaxText.setValue(f.getNumber());
						else
							businessFaxText.setValue("");
					}
				});
		businessFaxText = new TextItem(messages.fax(),"businessFaxText");
		businessFaxText.setToolTip(messages.giveOf(messages.fax(), category));
		// businessFaxText.setShowTitle(false);

		businessFaxText.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				if (event != null) {
					String fx = businessFaxText.getValue().toString();
					if (fx.equals("")) {
						errorWidget.clearAllErrors();
						return;
					}
					if (!fx.equals("") && !UIUtils.isValidFax(fx)) {
						// BaseView.errordata
						// .setHTML("<li> "
						// + AccounterErrorType.INCORRECTINFORMATION
						// + ".");
						// BaseView.commentPanel.setVisible(true);
						errorWidget.addError(this,
								messages.incorrectInformation());
						// Accounter
						// .showError(AccounterErrorType.INCORRECTINFORMATION);
						businessFaxText.setValue("");
					} else {
						// BaseView.errordata.setHTML("");
						// BaseView.commentPanel.setVisible(false);

						errorWidget.clearAllErrors();
						ClientFax fax = new ClientFax();
						fax.setType(UIUtils.getFaxType(businessFaxSelect
								.getDisplayValue().toString()));
						fax.setNumber(fx);
						allFaxes.put(UIUtils.getFaxType(businessFaxSelect
								.getDisplayValue().toString()), fax);
					}
				}

			}
		});
		if (toBeShownFax != null) {
			businessFaxSelect.setComboItem(UIUtils.getFaXTypes(toBeShownFax
					.getType()));
			businessFaxText.setValue(toBeShownFax.getNumber() + "");
		} else
			businessFaxSelect.setDefaultToFirstOption(true);
		add(businessPhoneText, businessFaxText);

	}

	private void setPhonesAndFaxes(Set<ClientPhone> phones, Set<ClientFax> faxes) {

		if (phones != null) {
			for (ClientPhone pho : phones) {
				if (pho.getIsSelected()) {
					toBeShownPhone = pho;
				}
				allPhones.put(pho.getType(), pho);

				// System.out.println("Existing Phones  Type " + pho.getType()
				// + " number is " + pho.getNumber() + " Is Selected"
				// + pho.getIsSelected());
			}
		}
		if (faxes != null) {
			for (ClientFax fax : faxes) {
				if (fax.getIsSelected()) {
					toBeShownFax = fax;
				}
				allFaxes.put(fax.getType(), fax);

				// System.out.println("Existing Faxes  Type " + fax.getType()
				// + " number is " + fax.getNumber() + " Is Selected"
				// + fax.getIsSelected());
			}
		}

	}

	public Set<ClientPhone> getAllPhones() {
		ClientPhone selectedPhoneFromSelect = allPhones.get(UIUtils
				.getPhoneType(businessPhoneSelect.getSelectedValue()));
		if (selectedPhoneFromSelect != null) {
			selectedPhoneFromSelect.setIsSelected(true);
			allPhones.put(UIUtils.getPhoneType(businessPhoneSelect
					.getSelectedValue()), selectedPhoneFromSelect);

		}
		Collection<ClientPhone> pho = allPhones.values();
		Set<ClientPhone> toBeSetPhones = new HashSet<ClientPhone>();
		for (ClientPhone p : pho) {
			toBeSetPhones.add(p);
			// System.out.println("Sending Phones  Type " + p.getType()
			// + " number is " + p.getNumber() + " Is Selected"
			// + p.getIsSelected());
		}
		return toBeSetPhones;

	}

	public Set<ClientFax> getAllFaxes() {
		ClientFax selectedFaxFromSelect = allFaxes.get(UIUtils
				.getFaxType(businessFaxSelect.getSelectedValue()));
		if (selectedFaxFromSelect != null) {
			selectedFaxFromSelect.setIsSelected(true);
			allFaxes.put(selectedFaxFromSelect.getType(), selectedFaxFromSelect);
		}
		Collection<ClientFax> faxs = allFaxes.values();
		Set<ClientFax> toBeSetFaxes = new HashSet<ClientFax>();
		for (ClientFax f : faxs) {
			toBeSetFaxes.add(f);
			// System.out.println("Sending Fax  Type " + f.getType()
			// + " number is " + f.getNumber() + " Is Selected"
			// + f.getIsSelected());
		}
		return toBeSetFaxes;

	}
}
