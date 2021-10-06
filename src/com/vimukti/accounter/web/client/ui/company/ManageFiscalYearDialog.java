package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.vimukti.accounter.web.client.core.ClientFiscalYear;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.Accounter.AccounterType;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.core.ErrorDialogHandler;
import com.vimukti.accounter.web.client.ui.core.ViewManager;
import com.vimukti.accounter.web.client.ui.forms.LabelItem;

public class ManageFiscalYearDialog extends BaseDialog {

	private LabelItem manageFiscalyearLabel;
	private LabelItem descriptionLabel;
	private FiscalYearListGrid listOfperiods;
	private StyledPanel buttonVlayout;
	private Button newFiscalYearButton;
	private Button closeFiscalYearButton;
	private Button openFiscalYearButoon;
	private Button editFiscalYear;
	private Button deleteFiscalYear;
	private Button changeStartDate;
	private StyledPanel listHpanel;
	private StyledPanel mainVlayout;

	private StyledPanel buttonPanel;

	public ManageFiscalYearDialog(String title, String desc) {
		super(title, desc);
		this.getElement().setId("ManageFiscalYearDialog");
		createControls();
		// setWidth("550px");
	}

	public void createControls() {
		manageFiscalyearLabel = new LabelItem(messages.manageFiscalYear(),
				"manageFiscalyearLabel");

		descriptionLabel = new LabelItem(messages.toCloseBooksOrFascalYear(),
				"descriptionLabel");
		listOfperiods = new FiscalYearListGrid(this, false);
		listOfperiods.setTitle(messages.listOfPeriods());
		listHpanel = new StyledPanel("listHpanel");
		// listHpanel.setWidth("100%");
		listHpanel.add(listOfperiods);
		listHpanel.add(getButtonLayout());
		mainVlayout = new StyledPanel("mainVlayout");
		mainVlayout.add(descriptionLabel);
		mainVlayout.add(listHpanel);
		setBodyLayout(mainVlayout);
		initDataToFiscalYearList();
	}

	private void initDataToFiscalYearList() {
		listOfperiods.removeAllRecords();
		for (ClientFiscalYear clientFiscalYear : Accounter.getCompany()
				.getFiscalYears()) {
			listOfperiods.addData(clientFiscalYear);
		}
		// listOfperiods.sortList();
	}

	private StyledPanel getButtonLayout() {
		buttonVlayout = new StyledPanel("buttonVlayout");
		buttonVlayout.setStyleName("fisclaYearButtons");
		newFiscalYearButton = new Button();
		// newFiscalYearButton.setWidth("140px");
		newFiscalYearButton.setText(messages.newFiscalYear());
		newFiscalYearButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				CreateFiscalYearDialog dialog = new CreateFiscalYearDialog(
						messages.createFascalYear(), "", listOfperiods);
				ViewManager.getInstance().showDialog(dialog);
			}
		});
		closeFiscalYearButton = new Button();
		// closeFiscalYearButton.setWidth("140px");
		closeFiscalYearButton.setEnabled(false);
		closeFiscalYearButton.setText(messages.closeFiscalYear());
		closeFiscalYearButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				showCloseFiscalYearPopupPanel();
			}
		});
		openFiscalYearButoon = new Button();
		// openFiscalYearButoon.setWidth("140px");
		openFiscalYearButoon.setEnabled(false);
		openFiscalYearButoon.setText(messages.openFiscalYear());
		openFiscalYearButoon.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				final ClientFiscalYear clientFiscalYear = listOfperiods
						.getSelection();
				clientFiscalYear.setStatus(ClientFiscalYear.STATUS_OPEN);
				saveOrUpdate(clientFiscalYear);
			}
		});

		editFiscalYear = new Button();
		// editFiscalYear.setWidth("140px");
		editFiscalYear.setEnabled(false);
		editFiscalYear.setText(messages.editFiscalYear());
		editFiscalYear.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				editFiscalYear();
			}
		});
		deleteFiscalYear = new Button();
		// deleteFiscalYear.setWidth("140px");
		deleteFiscalYear.setText(messages.deleteFiscalYear());
		// deleteFiscalYear.setWidth("140px");
		// deleteFiscalYear.setEnabled(false);
		deleteFiscalYear.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				deleteFiscalYear();
			}
		});
		changeStartDate = new Button();
		// changeStartDate.setWidth("140px");
		changeStartDate.setText(messages.changeStartDate());
		changeStartDate.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ChangeFiscalYearStartDateDialog dialog = new ChangeFiscalYearStartDateDialog(
						messages.changeStartDate(), "", listOfperiods);
				ViewManager.getInstance().showDialog(dialog);
			}
		});

		// buttonVlayout.setWidth("160px");
		buttonVlayout.add(newFiscalYearButton);
		buttonVlayout.add(closeFiscalYearButton);
		buttonVlayout.add(openFiscalYearButoon);
		buttonVlayout.add(editFiscalYear);
		buttonVlayout.add(deleteFiscalYear);
		// buttonVlayout.add(changeStartDate);

		//
		// if (newFiscalYearButton.isEnabled()) {
		// newFiscalYearButton.getElement().getParentElement().setClassName(
		// "ibutton");
		// ThemesUtil.addDivToButton(newFiscalYearButton, FinanceApplication
		// .getThemeImages().button_right_blue_image(),
		// "ibutton-right-image");
		// } /*
		// * else {
		// * newFiscalYearButton.getElement().getParentElement().setClassName(
		// * "cancel-button"); ThemesUtil.addDivToButton(newFiscalYearButton,
		// * FinanceApplication .getThemeImages().button_right_gray_image(),
		// * "ibutton-right-image"); }
		// */
		// if (closeFiscalYearButton.isEnabled()) {
		// closeFiscalYearButton.getElement().getParentElement().setClassName(
		// "ibutton");
		// ThemesUtil.addDivToButton(closeFiscalYearButton, FinanceApplication
		// .getThemeImages().button_right_blue_image(),
		// "ibutton-right-image");
		// } /*
		// * else {
		// * closeFiscalYearButton.getElement().getParentElement().setClassName(
		// * "cancel-button"); ThemesUtil.addDivToButton(closeFiscalYearButton,
		// * FinanceApplication .getThemeImages().button_right_gray_image(),
		// * "ibutton-right-image"); }
		// */
		// if (openFiscalYearButoon.isEnabled()) {
		// openFiscalYearButoon.getElement().getParentElement().setClassName(
		// "ibutton");
		// ThemesUtil.addDivToButton(openFiscalYearButoon, FinanceApplication
		// .getThemeImages().button_right_blue_image(),
		// "ibutton-right-image");
		// } /*
		// * else {
		// * openFiscalYearButoon.getElement().getParentElement().setClassName(
		// * "cancel-button"); ThemesUtil.addDivToButton(openFiscalYearButoon,
		// * FinanceApplication .getThemeImages().button_right_gray_image(),
		// * "ibutton-right-image"); }
		// */
		// if (editFiscalYear.isEnabled()) {
		// editFiscalYear.getElement().getParentElement().setClassName(
		// "ibutton");
		// ThemesUtil.addDivToButton(editFiscalYear, FinanceApplication
		// .getThemeImages().button_right_blue_image(),
		// "ibutton-right-image");
		// } /*
		// * else { editFiscalYear.getElement().getParentElement().setClassName(
		// * "cancel-button"); ThemesUtil.addDivToButton(editFiscalYear,
		// * FinanceApplication .getThemeImages().button_right_gray_image(),
		// * "ibutton-right-image"); }
		// */
		// if (deleteFiscalYear.isEnabled()) {
		// deleteFiscalYear.getElement().getParentElement().setClassName(
		// "ibutton");
		// ThemesUtil.addDivToButton(deleteFiscalYear, FinanceApplication
		// .getThemeImages().button_right_blue_image(),
		// "ibutton-right-image");
		// } /*
		// * else {
		// deleteFiscalYear.getElement().getParentElement().setClassName(
		// * "cancel-button"); ThemesUtil.addDivToButton(deleteFiscalYear,
		// * FinanceApplication .getThemeImages().button_right_gray_image(),
		// * "ibutton-right-image"); }
		// */
		// if (changeStartDate.isEnabled()) {
		// changeStartDate.getElement().getParentElement().setClassName(
		// "ibutton");
		// ThemesUtil.addDivToButton(changeStartDate, FinanceApplication
		// .getThemeImages().button_right_blue_image(),
		// "ibutton-right-image");
		// } /*
		// * else {
		// changeStartDate.getElement().getParentElement().setClassName(
		// * "cancel-button"); ThemesUtil.addDivToButton(changeStartDate,
		// * FinanceApplication .getThemeImages().button_right_gray_image(),
		// * "ibutton-right-image"); }
		// */
		enableEditRemoveButtons(false);
		return buttonVlayout;

	}

	private void enableEditRemoveButtons(boolean b) {
		deleteFiscalYear.setEnabled(b);
		editFiscalYear.setEnabled(b);
	}

	private void showCloseFiscalYearPopupPanel() {
		Accounter.showWarning(messages.warnOfFascalYear(),
				AccounterType.WARNING, new ErrorDialogHandler() {

					@Override
					public boolean onYesClick() {
						final ClientFiscalYear clientFiscalYear = listOfperiods
								.getSelection();
						clientFiscalYear
								.setStatus(ClientFiscalYear.STATUS_CLOSE);
						saveOrUpdate(clientFiscalYear);
						return true;
					}

					@Override
					public boolean onNoClick() {

						return true;
					}

					@Override
					public boolean onCancelClick() {

						return true;
					}
				});
	}

	private void deleteFiscalYear() {
		if (listOfperiods.getSelection() != null) {
			// if (listOfperiods.getRecordByIndex(listOfperiods.getRowCount() -
			// 1) == listOfperiods
			// .getSelection()) {
			enableEditRemoveButtons(false);
			Accounter.deleteObject(this, listOfperiods.getSelection());
			// }
		} else {
			Accounter.showInformation(messages
					.selectAnyOfTheFiscalYearFromList());
		}
	}

	private void editFiscalYear() {
		if (listOfperiods.getSelection() != null
				&& listOfperiods.getSelection().getStatus() != ClientFiscalYear.STATUS_CLOSE) {

			// if (listOfperiods.getRecordByIndex(listOfperiods.getRowCount() -
			// 1) == listOfperiods
			// .getSelection()
			// || listOfperiods.getRecordByIndex(0) == listOfperiods
			// .getSelection()) {
			CreateFiscalYearDialog dialog = new CreateFiscalYearDialog(
					messages.editFiscalYear(), "", listOfperiods);
			ViewManager.getInstance().showDialog(dialog);
			enableEditRemoveButtons(false);
			// }

			// else {
			//
			// Accounter.showInformation(FinanceApplication
			// .constants().fiscalYearsCanBeEdited());
			// }

		} else if (listOfperiods.getSelection().getStatus() == ClientFiscalYear.STATUS_CLOSE)
			Accounter.showInformation(messages.youCannotEdiClosedFiscalYear());
	}

	public void showButtonsVisibility() {
		// for (int i = listOfperiods.getRowCount() - 1; i >= 0; i--) {
		// if (listOfperiods.getRecordByIndex(i).getStatus() ==
		// ClientFiscalYear.STATUS_CLOSE) {
		// if (listOfperiods.getSelection() == listOfperiods
		// .getRecordByIndex(i)) {
		//
		// closeFiscalYearButton.setEnabled(false);
		// openFiscalYearButoon.setEnabled(true);
		// break;
		// } else {
		// openFiscalYearButoon.setEnabled(false);
		// closeFiscalYearButton.setEnabled(false);
		// // break;
		// }
		//
		// }
		//
		// }
		closeFiscalYearButton.setEnabled(false);
		openFiscalYearButoon.setEnabled(false);
		editFiscalYear.setEnabled(false);
		deleteFiscalYear.setEnabled(false);
		for (int i = 0; i <= listOfperiods.getTableRowCount() - 1; i++) {
			if (listOfperiods.getRecordByIndex(i).getStatus() == ClientFiscalYear.STATUS_OPEN) {

				if (listOfperiods.getSelection() == listOfperiods
						.getRecordByIndex(i)) {

					openFiscalYearButoon.setEnabled(false);
					closeFiscalYearButton.setEnabled(true);

				} else if (i != 0
						&& listOfperiods.getSelection() == listOfperiods
								.getRecordByIndex(i - 1)) {
					openFiscalYearButoon.setEnabled(true);
					closeFiscalYearButton.setEnabled(false);

				}
				editFiscalYear.setEnabled(true);
				deleteFiscalYear.setEnabled(true);
				break;

			}
			if (listOfperiods.getSelectedRecordIndex() == listOfperiods
					.getTableRowCount() - 1
					&& listOfperiods.getSelection().getStatus() == ClientFiscalYear.STATUS_CLOSE) {
				openFiscalYearButoon.setEnabled(true);
				closeFiscalYearButton.setEnabled(false);
				editFiscalYear.setEnabled(true);
				deleteFiscalYear.setEnabled(true);
			}

		}
		// if (listOfperiods.getSelection() == listOfperiods
		// .getRecordByIndex(listOfperiods.getRowCount() - 1)
		// && listOfperiods.getRecords().size() > 1) {
		// // if (listOfperiods.getSelection().getStatus() ==
		// // ClientFiscalYear.STATUS_CLOSE) {
		// // openFiscalYearButoon.setEnabled(true);
		// // closeFiscalYearButton.setEnabled(false);
		// // } else {
		// // openFiscalYearButoon.setEnabled(false);
		// // closeFiscalYearButton.setEnabled(true);
		// // }
		//
		// deleteFiscalYear.setEnabled(true);
		// } else {
		// deleteFiscalYear.setEnabled(false);
		// }
		if (listOfperiods.getSelection() == listOfperiods.getRecordByIndex(0)) {

		}
	}

	@Override
	public void deleteFailed(AccounterException caught) {
		String errorString = AccounterExceptions.getErrorString(caught
				.getErrorCode());
		Accounter.showError(errorString);
	}

	@Override
	public void deleteSuccess(IAccounterCore result) {
		listOfperiods.deleteRecord(listOfperiods.getSelection());
		listOfperiods.removeAllRecords();
		listOfperiods.setRecords(company.getFiscalYears());
	}

	@Override
	public void saveSuccess(IAccounterCore object) {
		// ClientFiscalYear fiscalY = (ClientFiscalYear) object;
		enableEditRemoveButtons(false);
		initDataToFiscalYearList();
	}

	@Override
	protected boolean onOK() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
