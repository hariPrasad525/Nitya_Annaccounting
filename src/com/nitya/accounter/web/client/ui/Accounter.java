package com.nitya.accounter.web.client.ui;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.nitya.accounter.web.client.AccounterAsyncCallback;
import com.nitya.accounter.web.client.ClientGlobal;
import com.nitya.accounter.web.client.CompanyAndFeatures;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.IAccounterCRUDServiceAsync;
import com.nitya.accounter.web.client.IAccounterCompanyInitializationServiceAsync;
import com.nitya.accounter.web.client.IAccounterExportCSVServiceAsync;
import com.nitya.accounter.web.client.IAccounterGETServiceAsync;
import com.nitya.accounter.web.client.IAccounterHomeViewServiceAsync;
import com.nitya.accounter.web.client.IAccounterPayrollServiceAsync;
import com.nitya.accounter.web.client.IAccounterReportServiceAsync;
import com.nitya.accounter.web.client.IAccounterWindowsHomeServiceAsync;
import com.nitya.accounter.web.client.IGlobal;
import com.nitya.accounter.web.client.ValueCallBack;
import com.nitya.accounter.web.client.core.AccounterCometInitializer;
import com.nitya.accounter.web.client.core.AccounterCommand;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.Client1099Form;
import com.nitya.accounter.web.client.core.ClientCompany;
import com.nitya.accounter.web.client.core.ClientCompanyPreferences;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientIssuePayment;
import com.nitya.accounter.web.client.core.ClientTransaction;
import com.nitya.accounter.web.client.core.ClientUser;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.core.TransactionMeterEventType;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.externalization.AccounterMessages;
import com.nitya.accounter.web.client.externalization.AccounterMessages2;
import com.nitya.accounter.web.client.images.FinanceImages;
import com.nitya.accounter.web.client.images.FinanceMenuImages;
import com.nitya.accounter.web.client.theme.ThemeImages;
import com.nitya.accounter.web.client.translate.TranslateServiceAsync;
import com.nitya.accounter.web.client.ui.core.AccounterDialog;
import com.nitya.accounter.web.client.ui.core.ErrorDialogHandler;
import com.nitya.accounter.web.client.ui.forms.CustomDialog;
import com.nitya.accounter.web.client.ui.win8.CompaniesPanel;
import com.nitya.accounter.web.client.uibinder.setup.SetupWizard;
import com.nitya.accounter.web.client.util.ChangeType;
import com.nitya.accounter.web.client.util.CoreEvent;

/**
 * 
 * 
 */
public class Accounter implements EntryPoint {
	private static boolean isShutdown = false;
	private static MainFinanceWindow mainWindow;
	protected ValueCallBack<Accounter> callback;
	private static AccounterInitialiser create;
	private static AccounterRPCInitialiser rpcInitialiser;
	private static Set<String> features;
	private static ClientFinanceDate endDate;
	private static PlaceController placeController;
	public static SetupWizard setupWizard;
	public static Header header;
	public static SimplePanel vpanel;

	private static ClientUser user = null;
	private static ClientCompany company = null;

	public final static String CI_SERVICE_ENTRY_POINT = "/do/accounter/ci/rpc/service";
	public final static String CRUD_SERVICE_ENTRY_POINT = "/do/accounter/crud/rpc/service";
	public final static String GET_SERVICE_ENTRY_POINT = "/do/accounter/get/rpc/service";
	public final static String HOME_SERVICE_ENTRY_POINT = "/do/accounter/home/rpc/service";
	public final static String REPORT_SERVICE_ENTRY_POINT = "/do/accounter/report/rpc/service";
	public final static String USER_MANAGEMENT_ENTRY_POINT = "/do/accounter/user/rpc/service";
	public static final String TRANSLATE_SERVICE_ENTRY_POINT = "/do/accounter/translate/rpc/service";
	public final static String EXPORT_CSV_SERVICE_ENTRY_POINT = "/do/accounter/exportcsv/rpc/service";
	public final static String WINDOW_RPC_SERVICE_ENTRY_POINT = "/do/accounter/windows/rpc/service";
	public final static String PAYROLL_ENTRY_POINT = "/do/accounter/payroll/rpc/service";

	private static IAccounterCRUDServiceAsync crudService;
	private static IAccounterCompanyInitializationServiceAsync cIService;
	private static IAccounterGETServiceAsync getService;
	private static IAccounterHomeViewServiceAsync homeViewService;
	private static IAccounterReportServiceAsync reportService;
	private static IAccounterExportCSVServiceAsync exportCSVService;
	private static IAccounterWindowsHomeServiceAsync windowsService;
	private static IAccounterPayrollServiceAsync payrollService;

	private static AccounterMessages messages;
	private static AccounterMessages2 messages2;
	private static FinanceImages financeImages;
	private static FinanceMenuImages financeMenuImages;

	private static ThemeImages themeImages;
	private static ClientFinanceDate startDate;
	private static boolean isMacApp;
	private static boolean isIpadApp;
	private static TranslateServiceAsync translateService;
	private static AccounterCometInitializer cometInitializer;

	public static void loadCompany() {

		IAccounterCompanyInitializationServiceAsync cIService = createCompanyInitializationService();

		final AccounterAsyncCallback<CompanyAndFeatures> getCompanyCallback = new AccounterAsyncCallback<CompanyAndFeatures>() {
			public void onException(AccounterException caught) {
				showError(messages.unableToLoadCompany());
				caught.printStackTrace();
			}

			@Override
			public void onResultSuccess(CompanyAndFeatures result) {
				if (result == null) {
					gotCompany(null);
				}
				Set features = new HashSet(result.getFeatures());
				setFeatures(features);

				if (result.getClientCompany() != null)
					gotCompany(result.getClientCompany());
				else
					gotCompany(null);
			}

		};
		cIService.getCompany(getCompanyCallback);

	}

	public static void gotCompany(final ClientCompany company) {

		if (company == null || (!company.isConfigured())) {
			// and, now we are ready to start the application.
			removeLoadingImage();
			ClientCompanyPreferences preferences = null;
			if (company != null) {
				preferences = company.getPreferences();
			}
			// and, now we are ready to start the application.
			final ClientCompanyPreferences prefs = preferences;

			GWT.runAsync(new RunAsyncCallback() {

				@Override
				public void onSuccess() {
					removeLoadingImage();
					header = new Header();
					vpanel = new SimplePanel();
					vpanel.addStyleName("empty_menu_bar");
					setupWizard = new SetupWizard(new AsyncCallback<Boolean>() {
						@Override
						public void onSuccess(Boolean result) {
							RootPanel.get("mainWindow").remove(setupWizard);
							RootPanel.get("mainWindow").remove(header);
							RootPanel.get("mainWindow").remove(vpanel);
							if (result) {
								loadCompany();
							} else {
								loadCompaniesPanel();
							}
						}

						@Override
						public void onFailure(Throwable caught) {
							Accounter.showError(Accounter.messages
									.AccounterLoadingFailed());
						}
					}, prefs);
					RootPanel.get("mainWindow").add(header);
					RootPanel.get("mainWindow").add(vpanel);
					RootPanel.get("mainWindow").add(setupWizard);
				}

				@Override
				public void onFailure(Throwable reason) {
					// TODO Auto-generated method stub

				}
			});

		} else {
			GWT.runAsync(new RunAsyncCallback() {

				@Override
				public void onSuccess() {
					removeLoadingImage();
					Accounter.setCompany(company);
					Accounter.setUser(company.getLoggedInUser());
					startDate = company.getTransactionStartDate();
					endDate = company.getTransactionStartDate();

					initGUI();
				}

				@Override
				public void onFailure(Throwable reason) {
					// TODO Auto-generated method stub

				}
			});
		}

	}

	protected static void loadCompaniesPanel() {
		if (Accounter.isWin8App()) {
			WebsocketAccounterInitialiser initializer = Accounter
					.getAccounterInitializer();
			initializer.showView(new CompaniesPanel(initializer));
		} else {
			String companies = "/main/companies";
			Window.Location.assign(companies);
		}
	}

	public static ClientUser getUser() {
		return user;
	}

	public static ClientCompany getCompany() {
		if (company != null) {
			return company;
		} else {
			return null;
		}
	}

	public static void setUser(ClientUser u) {
		user = u;
	}

	public static void setCompany(ClientCompany c) {
		company = c;
	}

	private static void initGUI() {
		// Setting currency format
		reloadMacMenu();
		initiateComet();
		mainWindow = new MainFinanceWindow();
		RootPanel.get("mainWindow").add(mainWindow);

	}

	public static void initiateComet() {
		cometInitializer = GWT.create(AccounterCometInitializer.class);
		cometInitializer.initiateComet();
	}

	public static AccounterCometInitializer getComet() {
		return cometInitializer;
	}

	public static void reset() {
		boolean isTouch = false;/* isTablet */
		IMenuFactory menuFactory = null;
		if (isTouch) {
			menuFactory = new TouchMenuFactory();
		} else {
			menuFactory = new DesktopMenuFactory();
		}
		if (!isWin8App()) {
			AccounterMenuBar menubar = new AccounterMenuBar(menuFactory);
			mainWindow.remove(1);
			mainWindow.insert(menubar, 1);
		}
		reloadMacMenu();
	}

	private native static void reloadMacMenu() /*-{
		$wnd.MacReload();
	}-*/;

	public static MainFinanceWindow getMainFinanceWindow() {
		return mainWindow;
	}

	public static IAccounterCompanyInitializationServiceAsync createCompanyInitializationService() {
		if (cIService == null) {
			cIService = rpcInitialiser.createCompanyInitializationService();
		}

		return cIService;

	}

	public static IAccounterCRUDServiceAsync createCRUDService() {
		if (crudService == null) {
			crudService = rpcInitialiser.createCRUDService();
		}

		return crudService;

	}

	public static IAccounterGETServiceAsync createGETService() {
		if (getService == null) {
			getService = rpcInitialiser.createGETService();
		}
		return getService;
	}

	public static IAccounterHomeViewServiceAsync createHomeService() {
		if (homeViewService == null) {
			homeViewService = rpcInitialiser.createHomeService();
		}
		return homeViewService;
	}

	/**
	 * For Export csv file
	 * 
	 * @return
	 */
	public static IAccounterExportCSVServiceAsync createExportCSVService() {
		if (exportCSVService == null) {
			exportCSVService = rpcInitialiser.createExportCSVService();
		}
		return exportCSVService;
	}

	public static IAccounterReportServiceAsync createReportService() {
		if (reportService == null) {
			reportService = rpcInitialiser.createReportService();
		}
		return reportService;
	}

	public static TranslateServiceAsync createTranslateService() {
		if (translateService == null) {
			translateService = rpcInitialiser.createTranslateService();
		}
		return translateService;
	}

	public static IAccounterPayrollServiceAsync createPayrollService() {
		if (payrollService == null) {
			payrollService = rpcInitialiser.createPayrollService();
		}
		return payrollService;
	}

	public static ClientFinanceDate getStartDate() {
		return startDate;
	}

	public ClientFinanceDate getEndDate() {
		return endDate;
	}

	public static AccounterMessages getMessages() {
		if (messages == null) {
			try {
				messages = (AccounterMessages) GWT
						.create(AccounterMessages.class);
			} catch (Exception e) {
				System.err.println(e);
			}
		}
		return messages;
	}

	public static AccounterMessages2 getMessages2() {
		if (messages2 == null) {
			try {
				messages2 = (AccounterMessages2) GWT
						.create(AccounterMessages2.class);
			} catch (Exception e) {
				System.err.println(e);
			}
		}
		return messages2;
	}

	public static FinanceImages getFinanceImages() {
		if (financeImages == null) {
			financeImages = (FinanceImages) GWT.create(FinanceImages.class);
		}
		return financeImages;
	}

	public static FinanceMenuImages getFinanceMenuImages() {
		if (financeMenuImages == null) {
			financeMenuImages = (FinanceMenuImages) GWT
					.create(FinanceMenuImages.class);
		}
		return financeMenuImages;
	}

	public static ThemeImages getThemeImages() {
		if (themeImages == null) {
			themeImages = (ThemeImages) GWT.create(ThemeImages.class);
		}
		return themeImages;
	}

	@Override
	public void onModuleLoad() {

		boolean isAdmin = JNSI.getIsAdmin("isAdmin");
		IGlobal global = GWT.create(ClientGlobal.class);
		Global.set(global);
		// loadFeatures();
		if (isAdmin) {
			return;
		}

		eventBus = new SimpleEventBus();
		placeController = new PlaceController(eventBus);
		rpcInitialiser = GWT.create(AccounterRPCInitialiser.class);
		create = GWT.create(AccounterInitialiser.class);
		create.initalize();
		isIpadApp = create.isIpad();
	}

	private void loadFeatures() {
		Object obj = JNSI.getFeatures();
		setFeatures(new HashSet<String>());
		String string = obj.toString().trim();
		if (string.isEmpty()) {
			return;
		}
		String[] split = string.split(",");
		for (String s : split) {
			getFeatures().add(s);
		}
	}

	public String getUserDisplayName() {
		return Accounter.getCompany().getDisplayName();
	}

	public String getCompanyName() {
		return Accounter.getCompany().getName();
	}

	public static boolean isLoggedInFromDomain() {
		return false;
	}

	private static CustomDialog expireDialog;

	public enum AccounterType {
		ERROR, WARNING, WARNINGWITHCANCEL, INFORMATION, SUBSCRIPTION;
	}

	static native void removeLoadingImage() /*-{
		var parent = $wnd.document.getElementById('loadingWrapper');
		if (parent) {
			parent.style.visibility = 'hidden';
		}
		var footer = $wnd.document.getElementById('mainFooter');
		var appVersions = $wnd.document.getElementById('appVersions');
		if (footer) {
			footer.style.visibility = 'visible';
		}
		if (appVersions) {
			appVersions.style.visibility = 'visible';
		}
	}-*/;

	/**
	 * 
	 * @param mesg
	 *            Default value:"Warning"
	 * @param mesgeType
	 *            Default value:"Warning"
	 * @param dialogType
	 *            Default OK
	 */
	public static void showError(String msg) {
		new AccounterDialog(msg, AccounterType.ERROR);
	}

	public static void showWarning(String mesg, AccounterType typeOfMesg) {

		new AccounterDialog(mesg, typeOfMesg).show();

	}

	public static void showWarning(String msg, AccounterType typeOfMesg,
			ErrorDialogHandler handler) {

		new AccounterDialog(msg, typeOfMesg, handler).show();
	}

	public static void showInformation(String msg) {

		new AccounterDialog(msg, AccounterType.INFORMATION).show();
	}

	public static void showSubscriptionWarning() {
		new AccounterDialog(
				"This feature is available only in premium versions.",
				AccounterType.SUBSCRIPTION).show();
	}

	private static EventBus eventBus;

	public static void showMessage(String message) {
		if (expireDialog != null) {
			expireDialog.removeFromParent();
		}
		expireDialog = new CustomDialog();
		expireDialog.setText(message);
		StyledPanel vPanel = new StyledPanel("vPanel");
		HTML data = new HTML("<p>" + message + "</p");
		vPanel.add(data);
		Button loginBtn = new Button(getMessages().login());
		loginBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				String loginUrl = "/main/login";
				if (isWin8App()) {
					loginUrl = "/default.html";
				}
				Window.Location.assign(loginUrl);
			}
		});
		vPanel.add(loginBtn);
		loginBtn.setEnabled(true);
		loginBtn.getElement().getParentElement().addClassName("expiredButton");
		expireDialog.add(vPanel);
		expireDialog.center();
	}

	public static EventBus getEventBus() {
		if (eventBus == null) {
			eventBus = new SimpleEventBus();
		}
		return eventBus;
	}

	public static <D extends IAccounterCore> void inviteUser(
			final ISaveCallback source, final D coreObj) {
		final AccounterAsyncCallback<Long> inviteUserCallBack = new AccounterAsyncCallback<Long>() {

			public void onException(AccounterException caught) {
				source.saveFailed(caught);
				caught.printStackTrace();
			}

			public void onResultSuccess(Long result) {
				coreObj.setID(result);
				company.processUpdateOrCreateObject(coreObj);
				source.saveSuccess(coreObj);
			}
		};
		if (coreObj.getID() == 0) {
			Accounter.createCRUDService().inviteUser(coreObj,
					inviteUserCallBack);
		} else {
			Accounter.createCRUDService().updateUser(coreObj,
					inviteUserCallBack);
		}
		// } else {
		// Accounter.createCRUDService().updateUser((IAccounterCore) coreObj,
		// transactionCallBack);
		// }
	}

	public static <D extends IAccounterCore> void createOrUpdate(
			final ISaveCallback source, final D coreObj) {
		final AccounterAsyncCallback<Long> transactionCallBack = new AccounterAsyncCallback<Long>() {

			public void onException(AccounterException caught) {
				source.saveFailed(caught);
				caught.printStackTrace();
			}

			public void onResultSuccess(Long result) {
				if (coreObj instanceof ClientTransaction
						&& coreObj.getID() == 0) {
					Accounter.getEventBus().fireEvent(
							new CoreEvent<TransactionMeterEventType>(
									ChangeType.ADD,
									new TransactionMeterEventType()));
				}
				if (coreObj.getID() != 0) {
					coreObj.setVersion(coreObj.getVersion() + 1);
				}
				coreObj.setID(result);
				company.processUpdateOrCreateObject(coreObj);
				source.saveSuccess(coreObj);
			}

		};
		if (coreObj.getID() == 0) {
			Accounter.createCRUDService().create(coreObj, transactionCallBack);
		} else {
			Accounter.createCRUDService().update(coreObj, transactionCallBack);
		}
	}

	public static <D extends IAccounterCore> void deleteUser(
			final IDeleteCallback source, final D data) {
		AccounterAsyncCallback<Boolean> transactionCallBack = new AccounterAsyncCallback<Boolean>() {

			public void onException(AccounterException exception) {
				source.deleteFailed(exception);
			}

			public void onResultSuccess(Boolean result) {
				getCompany().processDeleteObject(data.getObjectType(),
						data.getID());
				source.deleteSuccess(data);
			}

		};
		Accounter.createCRUDService().deleteUser(data,
				Accounter.getUser().getEmail(), transactionCallBack);
	}

	public static <D extends IAccounterCore> void deleteObject(
			final IDeleteCallback source, final D data) {
		AccounterAsyncCallback<Boolean> transactionCallBack = new AccounterAsyncCallback<Boolean>() {

			public void onException(AccounterException exception) {
				source.deleteFailed(exception);
			}

			public void onResultSuccess(Boolean result) {
				if (result != null && result) {
					if (data instanceof ClientTransaction) {
						if (ClientFinanceDate
								.compareMonthAndYear(new ClientFinanceDate(
										((ClientTransaction) data)
												.getTransactionDate()))) {
							Accounter.getEventBus().fireEvent(
									new CoreEvent<TransactionMeterEventType>(
											ChangeType.DELETE,
											new TransactionMeterEventType()));
						}
					}
				}
				getCompany().processDeleteObject(data.getObjectType(),
						data.getID());
				source.deleteSuccess(data);
			}

		};
		Accounter.createCRUDService().delete(data.getObjectType(),
				data.getID(), transactionCallBack);
	}

	public static void voidTransaction(final ISaveCallback source,
			final AccounterCoreType coreType, final long transactionsID) {

		// currentrequestedWidget = widget;
		AccounterAsyncCallback<Boolean> callback = new AccounterAsyncCallback<Boolean>() {

			@Override
			public void onException(AccounterException exception) {
				getCompany().processCommand(exception);
				source.saveFailed(exception);
			}

			@Override
			public void onResultSuccess(Boolean result) {

				if (result) {
					AccounterCommand cmd = new AccounterCommand();
					cmd.setCommand(AccounterCommand.UPDATION_SUCCESS);
					cmd.setData(null);
					cmd.setID(transactionsID);
					cmd.setObjectType(coreType);
					getCompany().processUpdateOrCreateObject(cmd);
					// FIXME We may need to pass the actual object or we need
					// the interface to know that we will not give any object
					source.saveSuccess(cmd);
				}

			}
		};
		Accounter.createCRUDService().voidTransaction(coreType, transactionsID,
				callback);
	}

	public static void updateCompany(final ISaveCallback callback,
			final ClientCompany clientCompany) {
		boolean useAccountNumbers = clientCompany.getPreferences()
				.getUseAccountNumbers();
		AccounterAsyncCallback<Long> transactionCallBack = new AccounterAsyncCallback<Long>() {

			public void onException(AccounterException caught) {
				callback.saveFailed(caught);
			}

			public void onResultSuccess(Long result) {

				if (result != null) {
					getCompany().processUpdateOrCreateObject(clientCompany);
				}
				callback.saveSuccess(clientCompany);
			}

		};
		Accounter.createCRUDService().updateCompany(clientCompany,
				transactionCallBack);

	}

	public static PlaceController placeController() {
		return placeController;
	}

	public static <D extends IAccounterCore> void updateUser(
			final ISaveCallback source, final D coreObj) {
		final AccounterAsyncCallback<Long> transactionCallBack = new AccounterAsyncCallback<Long>() {

			public void onException(AccounterException caught) {
				source.saveFailed(caught);
				caught.printStackTrace();
			}

			public void onResultSuccess(Long result) {
				coreObj.setID(result);
				company.processUpdateOrCreateObject(coreObj);
				source.saveSuccess(coreObj);
			}
		};
		Accounter.createCRUDService().updateUser(coreObj, transactionCallBack);
	}

	/*
	 * private void initializeIsMacApp() { String cookie =
	 * Cookies.getCookie("Nativeapp"); if (cookie != null) { setMacApp(true); }
	 * }
	 * 
	 * public static boolean isMacApp() { return isMacApp; }
	 * 
	 * public void setMacApp(boolean isMacApp) { Accounter.isMacApp = isMacApp;
	 * }
	 */

	public static void get1099FormInformation(
			AsyncCallback<ArrayList<Client1099Form>> myCallback, int selected) {
		Accounter.createCRUDService().get1099Vendors(selected, myCallback);
	}

	public static void get1099InformationByVendor(
			AsyncCallback<Client1099Form> myCallback, long vendorId) {
		Accounter.createCRUDService().get1099InformationByVendor(vendorId,
				myCallback);
	}

	public static void doCreateIssuePaymentEffect(final ISaveCallback source,
			final ClientIssuePayment obj) {
		final AccounterAsyncCallback<Boolean> transactionCallBack = new AccounterAsyncCallback<Boolean>() {

			public void onException(AccounterException caught) {
				source.saveFailed(caught);
				caught.printStackTrace();
			}

			public void onResultSuccess(Boolean result) {
				company.processUpdateOrCreateObject(obj);
				source.saveSuccess(obj);
			}

		};
		Accounter.createCRUDService().doCreateIssuePaymentEffect(obj,
				transactionCallBack);
	}

	public static boolean hasPermission(String feature) {
		return getFeatures().contains(feature) || true;
	}

	public static boolean isShutdown() {
		return isShutdown;
	}

	public static boolean isIpadApp() {
		return isIpadApp;
	}

	public static void setShutdown(boolean isShutdown) {
		Accounter.isShutdown = isShutdown;
	}

	public static Set<String> getFeatures() {
		return features;
	}

	public static IAccounterWindowsHomeServiceAsync createWindowsRPCService() {
		if (windowsService == null) {
			WebsocketAccounterRPCInitialiser rpcInitialiser = GWT
					.create(WebsocketAccounterRPCInitialiser.class);
			windowsService = rpcInitialiser.createWindowsRPCService();
		}

		return windowsService;
	}

	/**
	 * @param features
	 *            the features to set
	 */
	public static void setFeatures(Set<String> feature) {
		features = feature;
	}

	public static boolean isWin8App() {
		return windowsService != null;
	}

	public static WebsocketAccounterInitialiser getAccounterInitializer() {
		return (WebsocketAccounterInitialiser) create;
	}
}
