package com.nitya.accounter.web.client.ui.company;

import java.util.List;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.ui.Label;
import com.nitya.accounter.web.client.core.ClientAccount;
import com.nitya.accounter.web.client.core.ClientChequeLayout;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.core.ValidationResult;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.ImageButton;
import com.nitya.accounter.web.client.ui.StyledPanel;
import com.nitya.accounter.web.client.ui.combo.BankAccountCombo;
import com.nitya.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.nitya.accounter.web.client.ui.core.AmountField;
import com.nitya.accounter.web.client.ui.core.BaseView;
import com.nitya.accounter.web.client.ui.forms.DynamicForm;
import com.nitya.accounter.web.client.ui.forms.LabelItem;
import com.nitya.accounter.web.client.ui.forms.TextItem;
import com.nitya.accounter.web.client.ui.forms.TextBoxItem.KeyPressListener;
import com.nitya.accounter.web.client.ui.widgets.ChequeLayoutWidget;

public class CheckPrintingSettingView extends BaseView<ClientChequeLayout> {

	private StyledPanel allPanel;
	private ChequeLayoutWidget widget;
	private ClientChequeLayout layout;
	private BankAccountCombo accountCombo;
	private TextItem signatureItem;
	private AmountField chequeWidth, chequeHeight, payeeTop, payeeLeft,
			payeeWidth, amountWordsTopOne, amountWordsLeftOne,
			amountWordsWidthOne, amountWordsTopTwo, amountWordsLeftTwo,
			amountWordsWidthTwo, amountFigTop, amountFigLeft, amountFigWidth,
			chequeDateTop, chequeDateLeft, chequeDateWidth, companyTop,
			companyLeft, companyWidth, signatoryTop, signatoryLeft,
			signatoryWidth;
	private Label noteLabel, pageFormatsLabel;

	private ClientAccount selectedAccount;

	@Override
	public void init() {
		super.init();
		this.getElement().setId("CheckPrintingSettingView");
		createControls();
	}

	@Override
	public void initData() {
		ClientAccount account = getCompany().getAccount(layout.getAccount());
		if (account != null) {
			accountCombo.setComboItem(account);
		}
		signatureItem.setValue(layout.getAuthorisedSignature());
		chequeWidth.setAmount(layout.getChequeWidth());
		chequeHeight.setAmount(layout.getChequeHeight());
		payeeTop.setAmount(layout.getPayeeNameTop());
		payeeLeft.setAmount(layout.getPayeeNameLeft());
		payeeWidth.setAmount(layout.getPayeeNameWidth());
		amountWordsTopOne.setAmount(layout.getAmountWordsLin1Top());
		amountWordsLeftOne.setAmount(layout.getAmountWordsLin1Left());
		amountWordsWidthOne.setAmount(layout.getAmountWordsLin1Width());
		amountWordsTopTwo.setAmount(layout.getAmountWordsLin2Top());
		amountWordsLeftTwo.setAmount(layout.getAmountWordsLin2Left());
		amountWordsWidthTwo.setAmount(layout.getAmountWordsLin2Width());
		amountFigTop.setAmount(layout.getAmountFigTop());
		amountFigLeft.setAmount(layout.getAmountFigLeft());
		amountFigWidth.setAmount(layout.getAmountFigWidth());
		chequeDateTop.setAmount(layout.getChequeDateTop());
		chequeDateLeft.setAmount(layout.getChequeDateLeft());
		chequeDateWidth.setAmount(layout.getChequeDateWidth());
		companyTop.setAmount(layout.getCompanyNameTop());
		companyLeft.setAmount(layout.getCompanyNameLeft());
		companyWidth.setAmount(layout.getCompanyNameWidth());
		signatoryTop.setAmount(layout.getSignatoryTop());
		signatoryLeft.setAmount(layout.getSignatoryLeft());
		signatoryWidth.setAmount(layout.getSignatoryWidth());
	}

	private void createControls() {
		if (getData() != null) {
			this.selectedAccount = getCompany().getAccount(
					getData().getAccount());
		}
		resetLayout(false);
		// Loading default cheque layout
		allPanel = new StyledPanel("allPanel");
		StyledPanel panel = new StyledPanel("panel");

		accountCombo = new BankAccountCombo(messages.bankAccount());
		accountCombo.setRequired(true);
		accountCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {

					@Override
					public void selectedComboBoxItem(ClientAccount selectItem) {
						CheckPrintingSettingView.this.selectedAccount = selectItem;
						resetLayout(true);
					}
				});
		signatureItem = new TextItem(messages.authorisedSignatory(),
				"signatureItem");
		signatureItem.setValue(layout == null ? "" : layout
				.getAuthorisedSignature());
		signatureItem.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				widget.setAuthoritySignatory(signatureItem.getValue());
			}
		});

		DynamicForm form = new DynamicForm("cheque_print_text");
		// form.setNumCols(4);
		form.add(accountCombo, signatureItem);
		widget = new ChequeLayoutWidget(layout);

		StyledPanel pageFormatPanel = new StyledPanel("pageFormatPanel");
		pageFormatsLabel = new Label(messages.pageFormats());
		noteLabel = new Label(messages.noteAllDimensions());
		pageFormatsLabel.addStyleName("cheque_print_text");
		noteLabel.addStyleName("cheque_print_text");
		pageFormatPanel.add(pageFormatsLabel);
		pageFormatPanel.add(noteLabel);

		DynamicForm sizesForm = new DynamicForm("sizesForm");
		LabelItem topLabelItem = new LabelItem(messages.top(),
				"cheque_print_text");
		topLabelItem.setValue(messages.top());
		LabelItem leftLabelItem = new LabelItem(messages.left(),
				"cheque_print_text");
		leftLabelItem.setValue(messages.left());
		LabelItem widthLabelItem = new LabelItem(messages.width(),
				"cheque_print_text");
		widthLabelItem.setValue(messages.width());
		widthLabelItem.addStyleName("cheque_print_text");
		// sizesForm.setWidth("100%");

		chequeWidth = new AmountField(messages.chequeWidth(), this);
		addHandelers(chequeWidth, new ValueChangeHandler() {

			@Override
			public void onChange(double value) {
				widget.setChequeWidth(value);
				chequeWidth.setAmount(value);
				adjustChequeWidthFields();
			}

		});
		chequeHeight = new AmountField(messages.chequeHeight(), this);
		addHandelers(chequeHeight, new ValueChangeHandler() {

			@Override
			public void onChange(double value) {
				widget.setChequeHeight(value);
				chequeHeight.setAmount(value);
				adjustChequeTopFields();
			}

		});

		LabelItem payeeLabel = new LabelItem(messages.nameOfPayee(),
				"payeeLabel");
		payeeTop = new AmountField("", this);
		addHandelers(payeeTop, new ValueChangeHandler() {

			@Override
			public void onChange(double value) {
				if (topValidation(value)) {
					widget.setPayeeNameTop(value);
					payeeTop.setAmount(value);
				} else {
					payeeTop.setAmount(layout.getPayeeNameTop());
				}
			}

		});
		payeeLeft = new AmountField("", this);
		addHandelers(payeeLeft, new ValueChangeHandler() {

			@Override
			public void onChange(double value) {
				if (widthValidation(payeeWidth.getAmount() + value)) {
					widget.setPayeeNameLeft(value);
					payeeLeft.setAmount(value);
				} else {
					payeeLeft.setAmount(layout.getPayeeNameLeft());
				}
			}

		});
		payeeWidth = new AmountField("", this);
		addHandelers(payeeWidth, new ValueChangeHandler() {

			@Override
			public void onChange(double value) {
				if (widthValidation(payeeLeft.getAmount() + value)) {
					widget.setPayeeNameWidth(value);
					payeeWidth.setAmount(value);
				} else {
					payeeWidth.setAmount(layout.getPayeeNameWidth());
				}
			}

		});

		LabelItem amountWordsLineOne = new LabelItem(
				messages.amountInWordsLineOne(), "amountWordsLineOne");
		amountWordsTopOne = new AmountField("", this);
		addHandelers(amountWordsTopOne, new ValueChangeHandler() {

			@Override
			public void onChange(double value) {
				if (topValidation(value)) {
					widget.setAmountWordsLin1Top(value);
					amountWordsTopOne.setAmount(value);
				} else {
					amountWordsTopOne.setAmount(layout.getAmountWordsLin1Top());
				}
			}
		});

		amountWordsLeftOne = new AmountField("", this);
		addHandelers(amountWordsLeftOne, new ValueChangeHandler() {

			@Override
			public void onChange(double value) {
				if (widthValidation(amountWordsWidthOne.getAmount() + value)) {
					widget.setAmountWordsLin1Left(value);
					amountWordsLeftOne.setAmount(value);
				} else {
					amountWordsLeftOne.setAmount(layout
							.getAmountWordsLin1Left());
				}
			}

		});
		amountWordsWidthOne = new AmountField("", this);
		addHandelers(amountWordsWidthOne, new ValueChangeHandler() {

			@Override
			public void onChange(double value) {
				if (widthValidation(amountWordsLeftOne.getAmount() + value)) {
					widget.setAmountWordsLin1Width(value);
					amountWordsWidthOne.setAmount(value);
				} else {
					amountWordsWidthOne.setAmount(layout
							.getAmountWordsLin1Width());
				}
			}

		});

		LabelItem amountWordsLineTwo = new LabelItem(
				messages.amountInWordsLineTwo(), "amountWordsLineTwo");
		amountWordsTopTwo = new AmountField("", this);
		addHandelers(amountWordsTopTwo, new ValueChangeHandler() {

			@Override
			public void onChange(double value) {
				if (topValidation(value)) {
					widget.setAmountWordsLin2Top(value);
					amountWordsTopTwo.setAmount(value);
				} else {
					amountWordsTopTwo.setAmount(layout.getAmountWordsLin2Top());
				}
			}

		});
		amountWordsLeftTwo = new AmountField("", this);
		addHandelers(amountWordsLeftTwo, new ValueChangeHandler() {

			@Override
			public void onChange(double value) {
				if (widthValidation(amountWordsWidthTwo.getAmount() + value)) {
					widget.setAmountWordsLin2Left(value);
					amountWordsLeftTwo.setAmount(value);
				} else {
					amountWordsLeftTwo.setAmount(layout
							.getAmountWordsLin2Left());
				}
			}

		});
		amountWordsWidthTwo = new AmountField("", this);
		addHandelers(amountWordsWidthTwo, new ValueChangeHandler() {

			@Override
			public void onChange(double value) {
				if (widthValidation(amountWordsLeftTwo.getAmount() + value)) {
					widget.setAmountWordsLin2Width(value);
					amountWordsWidthTwo.setAmount(value);
				} else {
					amountWordsWidthTwo.setAmount(layout
							.getAmountWordsLin2Width());
				}
			}

		});

		LabelItem amountFigLabel = new LabelItem(messages.amountInFigures(),
				"amountFigLabel");
		amountFigTop = new AmountField("", this);
		addHandelers(amountFigTop, new ValueChangeHandler() {

			@Override
			public void onChange(double value) {
				if (topValidation(value)) {
					widget.setAmountFigTop(value);
					amountFigTop.setAmount(value);
				} else {
					amountFigTop.setAmount(layout.getAmountFigTop());
				}
			}

		});
		amountFigLeft = new AmountField("", this);
		addHandelers(amountFigLeft, new ValueChangeHandler() {

			@Override
			public void onChange(double value) {
				if (widthValidation(amountFigWidth.getAmount() + value)) {
					widget.setAmountFigLeft(value);
					amountFigLeft.setAmount(value);
				} else {
					amountFigLeft.setAmount(layout.getAmountFigLeft());
				}
			}

		});
		amountFigWidth = new AmountField("", this);
		addHandelers(amountFigWidth, new ValueChangeHandler() {

			@Override
			public void onChange(double value) {
				if (widthValidation(amountFigLeft.getAmount() + value)) {
					widget.setAmountFigWidth(value);
					amountFigWidth.setAmount(value);
				} else {
					amountFigWidth.setAmount(layout.getAmountFigWidth());
				}
			}

		});

		LabelItem chequeDateLabel = new LabelItem(messages.chequeDate(),
				"chequeDateLabel");
		chequeDateTop = new AmountField("", this);
		addHandelers(chequeDateTop, new ValueChangeHandler() {

			@Override
			public void onChange(double value) {
				if (topValidation(value)) {
					widget.setChequeDateTop(value);
					chequeDateTop.setAmount(value);
				} else {
					chequeDateTop.setAmount(layout.getChequeDateTop());
				}
			}

		});
		chequeDateLeft = new AmountField("", this);
		addHandelers(chequeDateLeft, new ValueChangeHandler() {

			@Override
			public void onChange(double value) {
				if (widthValidation(chequeDateWidth.getAmount() + value)) {
					widget.setChequeDateLeft(value);
					chequeDateLeft.setAmount(value);
				} else {
					chequeDateLeft.setAmount(layout.getChequeDateLeft());
				}
			}

		});
		chequeDateWidth = new AmountField("", this);
		addHandelers(chequeDateWidth, new ValueChangeHandler() {

			@Override
			public void onChange(double value) {
				if (widthValidation(chequeDateLeft.getAmount() + value)) {
					widget.setChequeDateWidth(value);
					chequeDateWidth.setAmount(value);
				} else {
					chequeDateWidth.setAmount(layout.getChequeDateWidth());
				}
			}

		});

		LabelItem companyLabel = new LabelItem(messages.forCompany(),
				"companyLabel");
		companyTop = new AmountField("", this);
		addHandelers(companyTop, new ValueChangeHandler() {

			@Override
			public void onChange(double value) {
				if (topValidation(value)) {
					widget.setCompanyNameTop(value);
					companyTop.setAmount(value);
				} else {
					companyTop.setAmount(layout.getCompanyNameTop());
				}
			}

		});
		companyLeft = new AmountField("", this);
		addHandelers(companyLeft, new ValueChangeHandler() {

			@Override
			public void onChange(double value) {
				if (widthValidation(companyWidth.getAmount() + value)) {
					widget.setCompanyNameLeft(value);
					companyLeft.setAmount(value);
				} else {
					companyLeft.setAmount(layout.getCompanyNameLeft());
				}

			}

		});
		companyWidth = new AmountField("", this);
		addHandelers(companyWidth, new ValueChangeHandler() {

			@Override
			public void onChange(double value) {
				if (widthValidation(companyLeft.getAmount() + value)) {
					widget.setCompanyNameWidth(value);
					companyWidth.setAmount(value);
				} else {
					companyWidth.setAmount(layout.getCompanyNameWidth());
				}
			}

		});

		LabelItem signatoryLabel = new LabelItem(
				messages.authorisedSignatory(), "signatoryLabel");
		signatoryTop = new AmountField("", this);
		addHandelers(signatoryTop, new ValueChangeHandler() {

			@Override
			public void onChange(double value) {
				if (topValidation(value)) {
					widget.setSignatoryTop(value);
					signatoryTop.setAmount(value);
				} else {
					signatoryTop.setAmount(layout.getSignatoryTop());
				}
			}

		});
		signatoryLeft = new AmountField("", this);
		addHandelers(signatoryLeft, new ValueChangeHandler() {

			@Override
			public void onChange(double value) {
				if (widthValidation(signatoryWidth.getAmount() + value)) {
					widget.setSignatoryLeft(value);
					signatoryLeft.setAmount(value);
				} else {
					signatoryLeft.setAmount(layout.getSignatoryLeft());
				}
			}

		});
		signatoryWidth = new AmountField("", this);
		addHandelers(signatoryWidth, new ValueChangeHandler() {

			@Override
			public void onChange(double value) {
				if (widthValidation(signatoryLeft.getAmount() + value)) {
					widget.setSignatoryWidth(value);
					signatoryWidth.setAmount(value);
				} else {
					signatoryWidth.setAmount(layout.getSignatoryWidth());
				}
			}

		});

		LabelItem item = new LabelItem("", "item");
		item.setValue("");

		StyledPanel allFields = new StyledPanel("allFields");

		DynamicForm boldForms = new DynamicForm("boldForms");
		boldForms.add(item, topLabelItem, leftLabelItem, widthLabelItem);

		DynamicForm payeeForm = new DynamicForm("fourForm");
		payeeForm.add(payeeLabel, payeeTop, payeeLeft, payeeWidth);

		DynamicForm amountForm = new DynamicForm("fourForm");
		amountForm.add(amountWordsLineOne, amountWordsTopOne,
				amountWordsLeftOne, amountWordsWidthOne);

		DynamicForm amountForm1 = new DynamicForm("fourForm");
		amountForm1.add(amountWordsLineTwo, amountWordsTopTwo,
				amountWordsLeftTwo, amountWordsWidthTwo);

		DynamicForm amountForm2 = new DynamicForm("fourForm");
		amountForm2.add(amountFigLabel, amountFigTop, amountFigLeft,
				amountFigWidth);

		DynamicForm amountForm3 = new DynamicForm("fourForm");
		amountForm3.add(chequeDateLabel, chequeDateTop, chequeDateLeft,
				chequeDateWidth);

		DynamicForm amountForm4 = new DynamicForm("fourForm");
		amountForm4.add(companyLabel, companyTop, companyLeft, companyWidth);

		DynamicForm amountForm5 = new DynamicForm("fourForm");
		amountForm5.add(signatoryLabel, signatoryTop, signatoryLeft,
				signatoryWidth);

		allFields.add(boldForms);
		allFields.add(payeeForm);
		allFields.add(amountForm);
		allFields.add(amountForm1);
		allFields.add(amountForm2);
		allFields.add(amountForm3);
		allFields.add(amountForm4);
		allFields.add(amountForm5);

		DynamicForm chequeForm = new DynamicForm("chequeForm");
		// chequeForm.setNumCols(2);
		chequeForm.add(chequeWidth, chequeHeight);
		chequeForm.addStyleName("cheque_form");

		pageFormatPanel.addStyleName("pageformatpanel");
		// panel.setWidth("100%");
		panel.addStyleName("checkprintbox");
		panel.add(pageFormatPanel);
		panel.add(allFields);
		panel.add(chequeForm);

		Label lab1 = new Label(messages.chequePrintSetting());
		lab1.setStyleName("label-title");

		allPanel.add(form);
		allPanel.add(widget);
		allPanel.add(panel);

		this.add(lab1);
		this.add(allPanel);
		// allPanel.setWidth("100%");

		ImageButton saveButton = new ImageButton(messages.saveAndClose(),
				Accounter.getFinanceImages().saveAndClose(), "save");
		saveButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				onSave(false);
			}
		});
		saveButton.addStyleName("saveAndClose-Btn");
		getButtonBar().add(saveButton);

		ImageButton resetButton = new ImageButton(messages.reset(), Accounter
				.getFinanceImages().saveAndClose(), "save");
		resetButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				resetLayout(true);
			}
		});
		resetButton.addStyleName("saveAndClose-Btn");
		getButtonBar().add(resetButton);
	}

	protected void adjustChequeTopFields() {
		Double amount = chequeHeight.getAmount();
		if (payeeTop.getAmount() > amount) {
			payeeTop.setAmount(amount);
			widget.setPayeeNameTop(amount);
		}
		if (amountWordsTopOne.getAmount() > amount) {
			amountWordsTopOne.setAmount(amount);
			widget.setAmountWordsLin1Top(amount);
		}
		if (amountWordsTopTwo.getAmount() > amount) {
			amountWordsTopTwo.setAmount(amount);
			widget.setAmountWordsLin2Top(amount);
		}
		if (amountFigTop.getAmount() > amount) {
			amountFigTop.setAmount(amount);
			widget.setAmountFigTop(amount);
		}
		if (chequeDateTop.getAmount() > amount) {
			chequeDateTop.setAmount(amount);
			widget.setChequeDateTop(amount);
		}
		if (companyTop.getAmount() > amount) {
			companyTop.setAmount(amount);
			widget.setCompanyNameTop(amount);
		}
		if (signatoryTop.getAmount() > amount) {
			signatoryTop.setAmount(amount);
			widget.setSignatoryTop(amount);
		}
	}

	protected void adjustChequeWidthFields() {
		Double amount = chequeWidth.getAmount();
		if (payeeWidth.getAmount() + payeeLeft.getAmount() > amount) {
			payeeWidth.setAmount(0.0);
			widget.setPayeeNameWidth(0.0);
			payeeLeft.setAmount(amount);
			widget.setPayeeNameLeft(amount);
		}

		if (amountWordsWidthOne.getAmount() + amountWordsLeftOne.getAmount() > amount) {
			amountWordsWidthOne.setAmount(0.0);
			widget.setAmountWordsLin1Width(0.0);
			amountWordsLeftOne.setAmount(amount);
			widget.setAmountWordsLin1Left(amount);
		}

		if (amountWordsWidthTwo.getAmount() + amountWordsLeftTwo.getAmount() > amount) {
			amountWordsWidthTwo.setAmount(0.0);
			widget.setAmountWordsLin2Width(0.0);
			amountWordsLeftTwo.setAmount(amount);
			widget.setAmountWordsLin2Left(amount);
		}

		if (amountFigLeft.getAmount() + amountFigWidth.getAmount() > amount) {
			amountFigLeft.setAmount(amount);
			widget.setAmountFigLeft(amount);
			amountFigWidth.setAmount(0.0);
			widget.setAmountFigWidth(0.0);
		}
		if (chequeDateLeft.getAmount() + chequeDateWidth.getAmount() > amount) {
			chequeDateLeft.setAmount(amount);
			widget.setChequeDateLeft(amount);
			chequeDateWidth.setAmount(0.0);
			widget.setChequeDateWidth(0.0);
		}
		if (companyLeft.getAmount() + companyWidth.getAmount() > amount) {
			companyLeft.setAmount(amount);
			widget.setCompanyNameLeft(amount);
			companyWidth.setAmount(0.0);
			widget.setCompanyNameWidth(0.0);
		}
		if (signatoryLeft.getAmount() + signatoryWidth.getAmount() > amount) {
			signatoryLeft.setAmount(amount);
			widget.setSignatoryLeft(amount);
			signatoryWidth.setAmount(0.0);
			widget.setSignatoryWidth(0.0);
		}
	}

	protected void resetLayout(boolean isResetWidget) {
		ClientChequeLayout checkLayout;
		boolean hasLayout = true;
		if (this.selectedAccount == null) {
			checkLayout = getCompany().getCheckLayout(0);
			hasLayout = false;
		} else {
			checkLayout = getCompany().getCheckLayout(
					this.selectedAccount.getID());
			if (checkLayout == null) {
				checkLayout = getCompany().getCheckLayout(0);
				hasLayout = false;
			}
		}
		this.layout = (ClientChequeLayout) checkLayout.clone();
		if (!hasLayout) {
			this.layout.setID(0);
		}
		if (isResetWidget) {
			widget.setChequeLayout(layout);
			initData();
		}
	}

	@Override
	public void deleteFailed(AccounterException caught) {
	}

	@Override
	public void deleteSuccess(IAccounterCore result) {

	}

	@Override
	protected String getViewTitle() {
		return messages.chequePrintSetting();
	}

	@Override
	public List getForms() {
		return null;
	}

	@Override
	public void setFocus() {
	}

	private void addHandelers(final AmountField src,
			final ValueChangeHandler handler) {
		src.setKeyPressHandler(new KeyPressListener() {

			@Override
			public void onKeyPress(int keyCode) {
				Double amount = Double.valueOf(src.textBox.getText());
				if (keyCode == KeyCodes.KEY_UP) {
					amount = amount + 0.1;
					handler.onChange(amount);
				} else if (keyCode == KeyCodes.KEY_DOWN) {
					amount = amount - 0.1;
					if (amount <= 0) {
						amount = 0d;
					}
					handler.onChange(amount);
				}
			}
		});

		src.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				handler.onChange(src.getAmount());
			}
		});
	}

	interface ValueChangeHandler {
		void onChange(double value);
	}

	@Override
	protected boolean isSaveButtonAllowed() {
		return false;
	}

	@Override
	public ValidationResult validate() {
		ValidationResult validate = super.validate();
		ClientAccount selectedValue = accountCombo.getSelectedValue();
		if (selectedValue == null) {
			validate.addError(accountCombo,
					messages.pleaseSelect(messages.bankAccount()));
		}
		return validate;
	}

	private boolean topValidation(double value) {
		return chequeHeight.getAmount() >= value;
	}

	private boolean widthValidation(double value) {
		return chequeWidth.getAmount() >= value;
	}

	@Override
	public void saveAndUpdateView() {
		layout.setAccount(selectedAccount.getID());
		layout.setAuthorisedSignature(signatureItem.getValue());
		saveOrUpdate(layout);
	}
}
