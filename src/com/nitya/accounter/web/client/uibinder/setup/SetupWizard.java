package com.nitya.accounter.web.client.uibinder.setup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.nitya.accounter.web.client.AccounterAsyncCallback;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.AccountsTemplate;
import com.nitya.accounter.web.client.core.ClientAddress;
import com.nitya.accounter.web.client.core.ClientCompanyPreferences;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.Features;
import com.nitya.accounter.web.client.core.TemplateAccount;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.externalization.AccounterMessages;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.CustomLabel;
import com.nitya.accounter.web.client.ui.StyledPanel;
import com.nitya.accounter.web.client.ui.Accounter.AccounterType;
import com.nitya.accounter.web.client.ui.core.AccounterDialog;
import com.nitya.accounter.web.client.ui.core.Calendar;
import com.nitya.accounter.web.client.ui.core.ErrorDialogHandler;

public class SetupWizard extends FlowPanel {
	
	private static final int START_PAGE = 0;
	private StyledPanel viewPanel;
	private StyledPanel buttonPanel, backNextButtonPanel;
	private StyledPanel progressPanel, viewButtonPanel;
	private Button backButton, nextButton, cancelBtn;
	private Button gotoButton;
	private ClientCompanyPreferences preferences;
	private Label progressHeader;
	private AsyncCallback<Boolean> callback;
	private int currentViewIndex = START_PAGE;
	private ArrayList<TemplateAccount> selectedAccounts = new ArrayList<TemplateAccount>();
	private FlexTable progressTable;
	private AccounterMessages messages = Global.get().messages();

	private List<AbstractSetupPage> allViewsList, showViewList;
	private List<Image> images;

	private AbstractSetupPage previousView;
	private AbstractSetupPage viewToShow;
	private Map<Integer, AccountsTemplate> accountsTemplates = new HashMap<Integer, AccountsTemplate>();
	private String password, passwordHint;

	public SetupWizard(final AsyncCallback<Boolean> callback,
			ClientCompanyPreferences preferences) {
		initSetup(callback, preferences);
	}

	private void initSetup(AsyncCallback<Boolean> callback,
			ClientCompanyPreferences cPeferences) {
		if (cPeferences == null) {
			preferences = new ClientCompanyPreferences();
			Accounter.createCompanyInitializationService().getCountry(
					new AsyncCallback<String>() {

						@Override
						public void onSuccess(String result) {
							preferences.setTradingAddress(new ClientAddress());
							preferences.getTradingAddress().setCountryOrRegion(
									result);
						}

						@Override
						public void onFailure(Throwable caught) {

						}
					});
		} else {
			cPeferences = preferences;
		}
		AbstractSetupPage.setPreferences(preferences);
		initViewsAndNamesList();
		creteControls();
		this.addStyleName("setup_panel");
		this.callback = callback;
	}

	private void initViewsAndNamesList() {
		allViewsList = new ArrayList<AbstractSetupPage>();
		allViewsList.add(new SetupStartPage(this));
		allViewsList.add(new SetupCompanyInfoPage());
	    allViewsList.add(new SetupTaxInfoPage());
		//allViewsList.add(new SetupCurrencyPage2());
		allViewsList.add(new SetupIndustrySelectionPage(this));
		// allViewsList.add(new SetupOrganisationSelectionPage());
		allViewsList.add(new SetupReferPage());
		allViewsList.add(new SetupSellTypeAndSalesTaxPage());
		allViewsList.add(new SetupUsingEstimatesAndStatementsPage());
		allViewsList.add(new SetupCurrencyPage());
		allViewsList.add(new SetupTrackBillsAndTimePage());
		allViewsList.add(new SetupSelectFiscalYrDatePage());
		allViewsList.add(new SetupSelectAccountsPage(this));
		// IF SETUP SKIP THEN ONLY THIS VIEW SHOULD SHOW..
		allViewsList.add(new SetupIndustrySelectionWithAccountsPage(this));
		if (Accounter.hasPermission(Features.ENCRYPTION)) {
			allViewsList.add(new SetupPremiumPage(this));
		}
		allViewsList.add(new SetupComplitionPage()); 
	}

	public void creteControls() {
		try {
			StyledPanel topPanel = new StyledPanel("topPanel");
			viewPanel = new StyledPanel("viewPanel");
			progressPanel = new StyledPanel("progressPanel");
			viewButtonPanel = new StyledPanel("viewButtonPanel");
			backNextButtonPanel = new StyledPanel("backNextButtonPanel");
			progressHeader = new Label(messages.setupProgress());

			progressPanel.add(progressHeader);
			progressHeader.addStyleName("progress_header");

			buttonPanel = new StyledPanel("buttonPanel");
			buttonPanel.setVisible(false);

			viewButtonPanel.add(viewPanel);
			viewButtonPanel.add(buttonPanel);
			buttonPanel.setStyleName("back_next_buttons");

		 	topPanel.add(progressPanel);
			topPanel.add(viewButtonPanel);

			viewPanel.addStyleName("view_panel");

			this.add(topPanel);

			progressPanel.getElement().getParentElement()
					.setClassName("progress_panel_hide");

			backButton = new Button(messages.back());
			nextButton = new Button(messages.next());
			gotoButton = new Button(messages.gotoAccounter());
			cancelBtn = new Button(messages.cancel());

			backButton.setVisible(false);
			nextButton.setVisible(false);
			gotoButton.setVisible(false);

			backNextButtonPanel.add(backButton);
			backNextButtonPanel.add(nextButton);
			backNextButtonPanel.add(gotoButton);
			buttonPanel.add(cancelBtn);
			// buttonPanel.setCellHorizontalAlignment(cancelBtn,
			// HasAlignment.ALIGN_LEFT);
			buttonPanel.add(backNextButtonPanel);
			// buttonPanel.setCellHorizontalAlignment(backNextButtonPanel,
			// HasAlignment.ALIGN_RIGHT);
			// buttonPanel.setWidth("100%");
			loadIndustriesDefaultAccounts();

			gotoButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					gotoButton.setEnabled(false);
					showLoadingImage();
					setStartDateOfFiscalYear();
					Accounter.createCompanyInitializationService()
							.initalizeCompany(preferences, password,
									passwordHint, selectedAccounts, callback);

				}

			});
			nextButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent arg0) {
					showView(true);
				}
			});

			backButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent arg0) {
					if (currentViewIndex != START_PAGE) {
						showView(false);
						if (currentViewIndex == 0) {
							removeProgressPanel();
						}
					} else {
						viewPanel.remove(viewToShow);
						removeProgressPanel();
					}
				}
			});

			cancelBtn.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					cancel();
				}
			});

			previousView = null;
			showStartPage();
		} catch (Exception e) {
			System.err.println(e);
		}

	}

	private static native void showLoadingImage() /*-{
		var footer = $wnd.document.getElementById('mainFooter');
		var appVersions = $wnd.document.getElementById('appVersions');
		var parent = $wnd.document.getElementById('loadingWrapper');
		if (parent) {
			parent.style.visibility = 'visible';
		}
		if (footer) {
			footer.style.visibility = 'hidden';
		}
		if (appVersions) {
			appVersions.style.visibility = 'hidden';
		}
	}-*/;

	/**
	 * 
	 */
	private void loadIndustriesDefaultAccounts() {
		Accounter.createCompanyInitializationService().getAccountsTemplate(
				new AccounterAsyncCallback<ArrayList<AccountsTemplate>>() {

					@Override
					public void onException(AccounterException exception) {
						// TODO
						System.out.println("");
					}

					@Override
					public void onResultSuccess(
							ArrayList<AccountsTemplate> result) {
						setIndustryDefaultAccounts(result);
					}

				});

	}

	private void showStartPage() {
		try {
			viewToShow = allViewsList.get(0);
			this.viewPanel.add(viewToShow);
		} catch (Exception e) {
			System.err.println(e);
		}
	}

	private void setStartDateOfFiscalYear() {
		ClientFinanceDate currentDate = new ClientFinanceDate();
		int fiscalYearFirstMonth = preferences.getFiscalYearFirstMonth();
		ClientFinanceDate fiscalYearStartDate = new ClientFinanceDate(
				(int) currentDate.getYear(), fiscalYearFirstMonth, 1);
		Calendar endCal = Calendar.getInstance();
		endCal.setTime(fiscalYearStartDate.getDateAsObject());
		endCal.set(Calendar.MONTH, endCal.get(Calendar.MONTH) + 11);
		endCal.set(Calendar.DATE,
				endCal.getActualMaximum(Calendar.DAY_OF_MONTH));
		ClientFinanceDate fiscalYearEndDate = new ClientFinanceDate(
				endCal.getTime());

		preferences.setStartOfFiscalYear(fiscalYearStartDate.getDate());
		preferences.setEndOfFiscalYear(fiscalYearEndDate);
		preferences.setDepreciationStartDate(currentDate.getDate());
	}

	protected void showView(boolean isNext) {
		previousView = viewToShow;
		if (previousView == null) {
			return;
		}
		if (isNext) {
			if (!previousView.validate()) {
				return;
			}
			previousView.onSave();
			if (currentViewIndex > 0) {
				images.get(currentViewIndex).addStyleName("tick_show");

			}
			if (!isLastView()) {
				currentViewIndex++;
			}

		} else {
			if (currentViewIndex > 1) {
				images.get(currentViewIndex - 1).removeStyleName("tick_show");
			}
			currentViewIndex--;
		}

		this.viewPanel.remove(previousView);
		viewToShow = showViewList.get(currentViewIndex);

		if (viewToShow == null) {
			return;
		}

		if (isLastView()) {
			backNextButtonPanel.addStyleName("back_GoToPanel");
		} else {
			backNextButtonPanel.addStyleName("back_NextPanel");
		}
		this.viewPanel.add(viewToShow);
		// checking button display related conditions
		if (isFirstView()) {
			buttonPanel.setVisible(true);
			if (!isLastView()) {
				nextButton.setVisible(true);
				gotoButton.setVisible(false);
			} else {
				gotoButton.setVisible(true);
				nextButton.setVisible(false);
			}
			backButton.setVisible(true);
		} else {
			buttonPanel.setVisible(false);
		}

	}

	public void initProgessPanel() {
		progressTable = new FlexTable();
		for (int iii = 0; iii < images.size(); iii++) {
			if (showViewList.get(iii).isShowProgressPanel()) {
				CustomLabel label = new CustomLabel(showViewList.get(iii)
						.getViewName());
				
			//	CustomLabel label = new CustomLabel("TEEGALA");
				images.set(iii, new Image(Accounter.getFinanceImages()
						.tickMark()));
				images.get(iii).addStyleName("tick_hidden");
				progressTable.setWidget(iii, 0, images.get(iii));
				progressTable.setWidget(iii, 1, label);
			} 
		}  

		progressPanel.add(progressTable);
		progressTable.addStyleName("progress_panel_data");
		progressPanel.getElement().getParentElement()
				.setClassName("progress_panel_show");
	}

	/**
	 * @return
	 */
	private boolean isFirstView() {
		return currentViewIndex != START_PAGE;
	}

	private void removeProgressPanel() {
		progressPanel.remove(progressTable);
		progressPanel.getElement().getParentElement()
				.setClassName("progress_panel_hide");
	}

	/**
	 * @return
	 */
	private boolean isLastView() {
		return currentViewIndex == showViewList.size() - 1;
	}

	/**
	 * @return
	 */
	public List<TemplateAccount> getIndustryDefaultAccounts() {
		int industryType = preferences.getIndustryType();
		AccountsTemplate accountsTemplate = this.accountsTemplates
				.get(industryType);
		List<TemplateAccount> accounts = accountsTemplate.getAccounts();
		if (accounts != null) {
			return accounts;
		}
		return new ArrayList<TemplateAccount>();
	}

	public Map<Integer, AccountsTemplate> getAllIndustiesWithAccounts() {
		return accountsTemplates;
	}

	/**
	 * @return
	 */
	private void setIndustryDefaultAccounts(
			List<AccountsTemplate> accountesTemplates) {
		for (AccountsTemplate accountTemplate : accountesTemplates) {
			this.accountsTemplates.put(accountTemplate.getType(),
					accountTemplate);
		}
	}


	/**
	 * @param account
	 * @param value
	 */
	public void setSelectedAccountsList(
			ArrayList<TemplateAccount> selectedAccounts) {
		this.selectedAccounts = selectedAccounts;
	}

	/**
	 * @param account
	 * @param value
	 */
	public List<TemplateAccount> getSelectedAccountsList() {
		return this.selectedAccounts;
	}

	/**
	 * Cancels the Setup
	 */
	public void cancel() {
		new AccounterDialog(messages.setupCancelMessgae(),
				AccounterType.WARNING, new ErrorDialogHandler() {

					@Override
					public boolean onYesClick() {
						try {
							callback.onSuccess(false);
						} catch (Exception e) {
							return false;
						}
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

	public void initInterview() {
		showViewList = new ArrayList<AbstractSetupPage>();
		images = new ArrayList<Image>();
		for (int i = 0; i < allViewsList.size(); i++) {
			if (allViewsList.get(i).canShow()) {
				images.add(new Image());
				showViewList.add(allViewsList.get(i));
			}
		}
		initProgessPanel();
		showView(true);
	}

	public void setPassword(String string) {
		password = string;
	}

	public void setPasswordHint(String string) {
		this.passwordHint = string;
	}

}
