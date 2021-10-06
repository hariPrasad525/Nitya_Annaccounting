package com.vimukti.accounter.web.client.ui.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.impl.CldrImpl;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientAdvertisement;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.Features;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.help.HelpDialog;
import com.vimukti.accounter.web.client.help.HelpPanel;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.Accounter.AccounterType;
import com.vimukti.accounter.web.client.ui.HistoryToken;
import com.vimukti.accounter.web.client.ui.HistoryTokenUtils;
import com.vimukti.accounter.web.client.ui.ImageButton;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.TransactionMeterPanel;
import com.vimukti.accounter.web.client.ui.company.TransactionsCenterView;
import com.vimukti.accounter.web.client.ui.core.HistoryList.HistoryItem;
import com.vimukti.accounter.web.client.ui.forms.CustomDialog;
import com.vimukti.accounter.web.client.ui.settings.InventoryCentreView;

/**
 * 
 * 
 */

public class ViewManager extends FlowPanel {

	protected static AccounterMessages messages = Global.get().messages();

	private static final int VIEW_MANAGER_BODY_WIDTH = 1000;

	private static final int REQUIRED_SPACE = 350;

	/**
	 * This reference var. holds currently opened view. it is not only
	 * AbstractBaseView, it is may be AbstractReportView also
	 */
	public AbstractView<?> existingView;

	public final Map<String, Object> viewDataHistory = new HashMap<String, Object>();

	private MainFinanceWindow mainWindow;

	private final HistoryList views = new HistoryList();

	private ToolBar toolBar;

	private ActivityManager manager;

	private ImageButton previousButton;

	private ImageButton nextButton;

	private ImageButton printButton;

	private ImageButton exportButton;

	private ImageButton editButton;

	private ImageButton closeButton;

	// private ImageButton configButton;
	//
	// private ImageButton addCustomerButton;
	//
	// private ImageButton addVendorButton;

	private ImageButton searchButton;

	Label viewTitleLabel;

	private Map<String, String> keyValues = new HashMap<String, String>();
	ImageButton addNewButton;

	ButtonGroup group1;
	ButtonGroup group2;
	ButtonGroup group3;

	private SimplePanel viewHolder;

	ButtonGroup group4;
	ButtonGroup group5;
	ButtonGroup group6;
	ButtonGroup group7;
	ButtonGroup group8;

	public ViewManager() {

	}

	private void getAdvertisePanel(final StyledPanel rightPanel) {
		Accounter.createHomeService().getAdvertisements(
				new AsyncCallback<ArrayList<ClientAdvertisement>>() {

					@Override
					public void onSuccess(
							ArrayList<ClientAdvertisement> advertisements) {

						if ((advertisements != null)
								&& !(advertisements.isEmpty())) {
							for (ClientAdvertisement clientAdvertisement : advertisements) {
								String url = clientAdvertisement.getUrl();
								url = getReplacedURL(url);
								Frame frame = new Frame(url);
								frame.setSize(clientAdvertisement.getWidth()
										+ "px", clientAdvertisement.getHeight()
										+ "px");
								rightPanel.add(frame);
							}
						}
					}

					@Override
					public void onFailure(Throwable caught) {
					}
				});
	}

	protected String getReplacedURL(String url) {
		Set<Entry<String, String>> entrySet = keyValues.entrySet();
		for (Entry<String, String> entry : entrySet) {
			url = url.replaceAll("\\{" + entry.getKey() + "\\}",
					entry.getValue());
		}
		return url;
	}

	private HelpPanel helpPanel;

	private Widget createHelpPanel() {
		if (isHelpPanelEnabled) {
			helpPanel = new HelpPanel();
			helpPanel.setHelpUrl(this.getUrl());
			helpPanel.setIsHelpPanel(true);
			helpPanel.addStyleName("view_help_panel");
			return helpPanel;
		} else {
			return null;
		}
	}

	String url = "";

	private ButtonGroup group9;

	private ButtonGroup group10;

	private Button ipadMenuButton;

	private String getUrl() {
		return "http://help.annaccounting.com/" + url;
	}

	private void initializeActivityManager() {
		this.manager = new ActivityManager(new AccounterActivityMapper(),
				Accounter.getEventBus());
		manager.setDisplay(viewHolder);
	}

	protected boolean defaultPresumtion(String eventTarget) {
		return eventTarget.contains("HTMLInputElement")
				|| eventTarget.contains("HTMLSelectElement")
				|| eventTarget.contains("HTMLTextAreaElement");
	}

	protected void historyChanged(String value) {

		// if (value.equals("dashBoard")) {
		// ipadMenuButton.setVisible(true);
		// } else {
		// ipadMenuButton.setVisible(false);
		// }

		if (value != null && views.current() != null
				&& value.equals(views.current().token)) {
			return;
		}

		HistoryToken token = null;
		try {
			token = new HistoryToken(value);
		} catch (Exception e) {
			// Unable to parse the token, done do anything
			e.printStackTrace();
		}
		// Check if it some thing we have kept alive
		HistoryItem item = getViewFromHistory(token.getToken());
		if (item != null && item.view != null) {
			// I think we have to remove this view from history.
			// views.list.remove(item);
			showView(item.view, item.action, true);
		} else {
			this.mainWindow.historyChanged(value);
		}
	}

	@SuppressWarnings("rawtypes")
	private void showView(final AbstractView<?> newview, final Action action,
			boolean shouldAskToSave) {
		if (this.existingView != null) {
			// We already have some view visible
			if (this.existingView instanceof IEditableView) {
				IEditableView editView = (IEditableView) existingView;
				if (shouldAskToSave && editView.isDirty()) {
					tryToClose(editView, new Command() {

						@Override
						public void execute() {
							// Called if the prev view can be closed
							existingView.removeFromParent();
							showNewView(newview, action);
						}
					});
				} else {
					// We can just remove it and put new one
					this.existingView.removeFromParent();
					showNewView(newview, action);
				}
			} else {
				// We can just remove it and put new one
				this.existingView.removeFromParent();
				showNewView(newview, action);
			}
		} else {
			showNewView(newview, action);
		}
	}

	private native void setStyleProperty(Style style, String name, String value) /*-{
		style[name] = value;
	}-*/;

	/**
	 * Try to close the current
	 * 
	 * @param editView
	 * @param command
	 */
	private void tryToClose(final IEditableView editView, final Command command) {
		Accounter.showWarning(messages.W_106(),
				AccounterType.WARNINGWITHCANCEL, new ErrorDialogHandler() {

					@Override
					public boolean onCancelClick() {
						return true;
					}

					@Override
					public boolean onNoClick() {
						remove(existingView);
						command.execute();
						return true;
					}

					@Override
					public boolean onYesClick() {
						editView.onSave(false);
						command.execute();
						return true;
					}
				});
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void showNewView(AbstractView newview, Action action) {
		Object input = action.getInput();
		String token = action.getHistoryToken();

		if (existingView != null) {
			// Save history
			if (existingView instanceof ISavableView) {
				saveView(existingView);
			}
			existingView.removeFromParent();
		}
		existingView = newview;
		newview.getButtonBar().clear();
		if (newview.getManager() == null) {
			newview.setManager(this);
			// newview.setPreferences(Accounter.getCompany().getPreferences());
			if (input != null) {
				newview.setData(input);
			}

			newview.init();
			if (input == null && newview instanceof ISavableView) {
				Object object = viewDataHistory.get(action.getHistoryToken());
				if (object != null) {
					restoreView(newview, object);
				}
			}
			newview.initData();
		}

		if (input instanceof IAccounterCore) {
			token = ((HistoryTokenUtils) GWT.create(HistoryTokenUtils.class))
					.getTokenWithID(token, (IAccounterCore) input);
		}
		this.views.add(new HistoryItem(newview, action, token));
		History.newItem(token, false);

		if (existingView instanceof BaseView) {
			if (((BaseView<IAccounterCore>) existingView).isInViewMode()) {
				createViewTitle(action.getCatagory(), action.getViewModeText());
				// viewTitleLabel.setText(action.getCatagory() + "  >  "
				// + action.getViewModeText());
			} else {
				createViewTitle(action.getCatagory(), action.getText());
				// viewTitleLabel.setText(action.getCatagory() + "  >  "
				// + action.getText());
			}
		} else {
			createViewTitle(action.getCatagory(), action.getText());
			// viewTitleLabel.setText(action.getCatagory() + "  >  "
			// + action.getText());
		}
		if (exportButton != null)
			exportButton.setTitle(messages.clickThisTo(messages.exportToCSV(),
					existingView.getAction().getViewName()));
		if (printButton != null)
			printButton.setTitle(messages.clickThisTo(messages.print(),
					existingView.getAction().getViewName()));
		if (editButton != null)
			editButton.setTitle(messages.clickThisTo(messages.edit(),
					existingView.getAction().getViewName()));

		viewHolder.add(newview);
		updateButtons();
		existingView.updateButtons();

		Window.scrollTo(0, 0);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void restoreView(AbstractView view, Object object) {
		view.setStatus(AbstractView.STATUS_RESTORED);
		((ISavableView) view).restoreView(object);
	}

	@SuppressWarnings("rawtypes")
	private void saveView(AbstractView<?> view) {
		view.setStatus(AbstractView.STATUS_SAVED);
		viewDataHistory.put(view.getAction().getHistoryToken(),
				((ISavableView) view).saveView());
	}

	private void createViewTitle(String catagory, String viewModeText) {
		if (catagory != null && catagory.length() > 1) {
			viewTitleLabel.setText(catagory + "  >  " + viewModeText);
		} else {
			viewTitleLabel.setText(viewModeText);
		}

	}

	public void removeEditButton() {
		existingView.removeButton(group4, editButton);
	}

	// public void removeConfigButton() {
	// group5.remove(configButton);
	// }
	//
	// public void removeAddCustomerButton() {
	// group7.remove(addCustomerButton);
	// }
	//
	// public void removeAddVendorButton() {
	// group8.remove(addVendorButton);
	// }

	@SuppressWarnings("rawtypes")
	public void updateButtons() {
		addRequiredButtons();
		if (!isIpad() && searchButton == null) {
			searchButton = getSearchButton();
			existingView.getButtonBar().addPermanent(group6, searchButton);
		}
		existingView.removeButton(group4, editButton);
		if (existingView instanceof IEditableView
				&& ((IEditableView) existingView).canEdit()) {
			editButton = getEditButton();
			existingView.addButton(group4, editButton);
		}

		existingView.removeButton(group9, addNewButton);
		if ((existingView instanceof BaseListView)
				|| (existingView instanceof TransactionsCenterView)
				|| (existingView instanceof InventoryCentreView)) {
			String labelString;
			if (existingView instanceof TransactionsCenterView) {
				TransactionsCenterView centerView = (TransactionsCenterView) existingView;
				labelString = centerView.baseListView.getAddNewLabelString();
			} else if (existingView instanceof InventoryCentreView) {
				labelString = ((InventoryCentreView) existingView)
						.getAddNewLabelString();
			} else {
				labelString = ((BaseListView) existingView)
						.getAddNewLabelString();
			}
			existingView.removeButton(group9, addNewButton);
			if (labelString != null && !labelString.isEmpty()) {
				addNewButton = getAddNewButton();
				addNewButton.setText(labelString);
				existingView.addButton(group9, addNewButton);
			}
		}

		existingView.removeButton(group2, exportButton);
		existingView.removeButton(group2, printButton);

		if (existingView instanceof IPrintableView && !Accounter.isIpadApp()) {
			if (((IPrintableView) existingView).canExportToCsv()) {
				exportButton = getExportButton();
				existingView.addButton(group2, exportButton);
			}

			if (((IPrintableView) existingView).canPrint()) {
				printButton = createPrintButton();
				existingView.addButton(group2, printButton);
			}
		}
		// if (existingView instanceof DashBoardView) {
		// group5.add(configButton);
		// } else {
		// removeConfigButton();
		// }
		//
		// if (existingView instanceof CustomerCenterView
		// && Accounter.getUser().isCanDoUserManagement()) {
		// addCustomerButton.setText(messages.addNew(Global.get().Customer()));
		// group7.add(addCustomerButton);
		// } else {
		// removeAddCustomerButton();
		// }
		//
		// if (existingView instanceof VendorCenterView
		// && Accounter.getUser().isCanDoUserManagement()) {
		// addVendorButton.setText(messages.addNew(Global.get().Vendor()));
		// group8.add(addVendorButton);
		// } else {
		// removeAddVendorButton();
		// }
	}

	/**
	 * Checks in local history if that view is already open
	 * 
	 * @param token
	 * @return
	 */
	private HistoryItem getViewFromHistory(String token) {
		return views.getView(token);
	}

	public void closeCurrentView() {
		closeCurrentView(true);
	}

	/**
	 * Called when we want to remove current view and put previous view back
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void closeCurrentView(boolean restorePreviousView) {

		if (this.existingView == null) {
			return;
		}
		// If this is the last view, then do not close
		if (this.views.list.size() == 1) {
			return;
		}
		// Take the data and set to new view
		Object data = this.existingView.getData();

		this.existingView.removeFromParent();
		HistoryItem current = this.views.current();

		HistoryItem item = this.views.previous();

		// if (item.token.equals("dashBoard")) {
		// ipadMenuButton.setVisible(true);
		// } else {
		// ipadMenuButton.setVisible(false);
		// }

		if (restorePreviousView) {
			if (item.view == null) {
				item.action.isCalledFromHistory = true;
				item.action.run();
			} else {
				// Save history
				if (existingView instanceof ISavableView) {
					saveView(existingView);
				}
				existingView.removeFromParent();
				existingView.getButtonBar().clear();
				this.existingView = item.view;
				ActionCallback callback = current.action.getCallback();
				if (data != null && callback != null) {
					callback.actionResult(data);
				}

				if (item.view instanceof BaseView
						&& (((BaseView<IAccounterCore>) item.view)
								.isInViewMode())) {
					createViewTitle(item.action.getCatagory(),
							item.action.getViewModeText());
				} else {
					createViewTitle(item.action.getCatagory(),
							item.action.getText());
				}
				viewHolder.add(item.view);
				this.views.add(item);
				History.newItem(item.action.getHistoryToken(), false);
			}
		} else {
			this.views.add(item);
		}
		updateButtons();
		existingView.updateButtons();
	}

	/**
	 * Instantiating View Manager
	 * 
	 * @return
	 */
	public static ViewManager getInstance() {

		return MainFinanceWindow.getViewManager();

	}

	/**
	 * @return
	 */
	protected ClientCompany getCompany() {
		return Accounter.getCompany();
	}

	/**
	 * Set the width and height to the given values.
	 * 
	 * @param height
	 * @param width
	 */
	public void fitToSize(int height, int width) {

		if (existingView != null) {
			existingView.fitToSize(height, width);
		}
	}

	@SuppressWarnings("rawtypes")
	public void showView(AbstractView<?> view, Object data,
			Boolean isDependent, Action action) {
		if (!isDependent) {
			this.views.clear();
		}
		view.setAction(action);
		showHelp(action.getHelpToken());
		showView(view, action, !isDependent);
	}

	void initilizeToolBar() {

		group1 = new ButtonGroup();
		group1.getElement().setId("group1");

		group2 = new ButtonGroup();
		group2.getElement().setId("group2");

		group3 = new ButtonGroup();
		group3.getElement().setId("group3");

		group4 = new ButtonGroup();
		group4.getElement().setId("group4");

		group5 = new ButtonGroup();
		group5.getElement().setId("group5");

		group6 = new ButtonGroup();
		group6.getElement().setId("group6");

		group7 = new ButtonGroup();
		group7.getElement().setId("group7");

		group8 = new ButtonGroup();
		group8.getElement().setId("group8");

		group9 = new ButtonGroup();
		group9.getElement().setId("group9");

		group10 = new ButtonGroup();
		group10.getElement().setId("group10");

		viewTitleLabel = new Label(messages.dashBoard());
		viewTitleLabel.addStyleName("viewTitle");

		previousButton = getPreviousButton();

		nextButton = getNextButton();

		printButton = createPrintButton();

		exportButton = getExportButton();

		editButton = getEditButton();

		closeButton = getCloseButton();

		// searchButton = getSearchButton();

		addNewButton = getAddNewButton();

		ipadMenuButton = new Button("menu");
		ipadMenuButton.addStyleName("iPadMenu");
		ipadMenuButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ActionFactory.getIpadMenuAction().run();
			}
		});

		if (!isIpad()) {
			group1.add(previousButton);
			group1.add(nextButton);
			previousButton.getElement().setAttribute(
					"lang",
					((CldrImpl) GWT.create(CldrImpl.class)).isRTL() ? "ar"
							: "en");
			nextButton.getElement().setAttribute(
					"lang",
					((CldrImpl) GWT.create(CldrImpl.class)).isRTL() ? "ar"
							: "en");
		}

		group1.add(viewTitleLabel);
		// addRequiredButtons();
		// existingView.addButton(group4,editButton);
		// existingView.addButton(group9,addNewButton);
		// if (!Accounter.isIpadApp()) {
		// group2.add(exportButton);
		// group2.add(printButton);
		// }
		group3.add(closeButton);
		// group5.add(configButton);
		if (isIpad()) {
			group5.add(ipadMenuButton);
		}

		exportButton.getElement().setAttribute("lang",
				((CldrImpl) GWT.create(CldrImpl.class)).isRTL() ? "ar" : "en");
		printButton.getElement().setAttribute("lang",
				((CldrImpl) GWT.create(CldrImpl.class)).isRTL() ? "ar" : "en");

		toolBar.add(group1);
		StyledPanel buttonsPanel = new StyledPanel("buttonsPanel");

		buttonsPanel.add(group5);
		buttonsPanel.add(group2);
		buttonsPanel.add(group9);
		buttonsPanel.add(group4);
		buttonsPanel.add(group10);
		buttonsPanel.add(group7);
		buttonsPanel.add(group8);
		buttonsPanel.add(group6);
		buttonsPanel.add(group3);

		toolBar.add(buttonsPanel);

		toolBar.addStyleName("group-toolbar");
	}

	public ImageButton getAddNewButton() {

		addNewButton = new ImageButton("", Accounter.getFinanceImages()
				.createAction(), "add");
		addNewButton.getElement().setId("addNewButton");
		addNewButton.addClickHandler(new ClickHandler() {

			@SuppressWarnings({ "rawtypes", "unchecked" })
			@Override
			public void onClick(ClickEvent event) {
				BaseListView baseListView;
				Action action;
				if (existingView instanceof TransactionsCenterView) {
					TransactionsCenterView centerView = (TransactionsCenterView) existingView;
					baseListView = centerView.baseListView;
					action = baseListView.getAddNewAction();
				} else if (existingView instanceof InventoryCentreView) {
					action = ((InventoryCentreView) existingView)
							.getAddNewAction();
				} else {
					baseListView = (BaseListView) existingView;
					action = baseListView.getAddNewAction();
				}
				if (action != null) {
					action.run(null, false);
				}
			}

		});
		return addNewButton;

	}

	public ImageButton getSearchButton() {

		searchButton = new ImageButton("Search", Accounter.getFinanceImages()
				.searchButton(), "find");
		searchButton.setTitle(messages.clickThisTo(messages.open(),
				messages.search()));
		searchButton.getElement().setId("searchButton");
		searchButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				// ActionFactory.getSearchInputAction().run();
				String historyToken = ActionFactory.getSearchInputAction()
						.getHistoryToken();
				History.newItem(historyToken, false);
				Accounter.getMainFinanceWindow().historyChanged(historyToken);
			}
		});
		return searchButton;
	}

	public ImageButton getEditButton() {
		editButton = new ImageButton(messages.edit(), Accounter
				.getFinanceImages().editIcon(), "edit");
		editButton.getElement().setId("editButton");
		editButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				createViewTitle(existingView.getAction().getCatagory(),
						existingView.getAction().getCatagory());
				existingView.onEdit();
			}
		});
		return editButton;

	}

	public ImageButton getExportButton() {
		exportButton = new ImageButton(messages.exportToCSV(), Accounter
				.getFinanceImages().exportIcon(), "savelocal");
		exportButton.getElement().setId("exportButton");
		exportButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				existingView.exportToCsv();

			}
		});
		return exportButton;

	}

	public ImageButton createPrintButton() {
		printButton = new ImageButton(messages.print(), Accounter
				.getFinanceImages().Print1Icon(), "download");
		printButton.getElement().setId("printButton");
		printButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				existingView.print();

			}
		});
		return printButton;

	}

	public ImageButton getNextButton() {
		nextButton = new ImageButton(Accounter.getFinanceImages().nextIcon(),
				"next");
		nextButton.getElement().setId("nextButton");
		nextButton.setTitle(messages.clickThisToOpen(messages.next()));
		nextButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				History.forward();
			}
		});
		return nextButton;

	}

	public ImageButton getPreviousButton() {
		previousButton = new ImageButton(Accounter.getFinanceImages()
				.previousIcon(), "previous");
		previousButton.getElement().setId("previousButton");
		previousButton.setTitle(messages.clickThisToOpen(messages.previous()));
		previousButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				History.back();
			}
		});
		return previousButton;

	}

	public ImageButton getCloseButton() {
		closeButton = new ImageButton(Accounter.getFinanceImages()
				.closeButton(), "cancel");
		closeButton.setTitle(messages.clickThisTo(messages.close(),
				messages.view()));
		closeButton.getElement().setId("closeButton");
		closeButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				if (existingDialog != null) {
					existingDialog.cancel();
				} else {
					existingView.cancel();
				}
			}
		});
		return closeButton;

	}

	public void addRequiredButtons() {
		clearGroup(group10);

		if (existingView instanceof IButtonContainer) {
			IButtonContainer view = (IButtonContainer) existingView;
			view.addButtons(group10);
		}
	}

	public void clearGroup(ButtonGroup group) {
		existingView.getButtonBar().clear(group);
	}

	public void toggleHelpPanel(boolean isHelpPanel) {
		if (!isPanelEnabled() && !isHelpPanel) {
			return;
		}
		if (!isHelpPanel) {
			HelpPanel prevhelpPanel = helpPanel;
			helpPanel.removeFromParent();
			helpDialog.removeFromParent();
			helpPanel = (HelpPanel) createHelpPanel();
			if (helpPanel != null) {
				this.add(helpPanel);
				// this.setCellWidth(helpPanel, "50%");
			} else {
				this.add(prevhelpPanel);
			}
			// this.setCellWidth(helpPanel, "50%");
		} else {
			helpPanel.removeFromParent();
			helpPanel.setIsHelpPanel(false);
			createHelpDialog();
		}
	}

	private boolean isPanelEnabled() {
		if (Window.getClientWidth() - VIEW_MANAGER_BODY_WIDTH > REQUIRED_SPACE) {
			return true;
		}
		return false;
	}

	private HelpDialog helpDialog;

	private boolean isHelpPanelEnabled = true;

	private void createHelpDialog() {
		helpPanel.setIsHelpPanel(false);
		if (helpDialog != null) {
			helpDialog.removeFromParent();
		}
		helpDialog = new HelpDialog(helpPanel);
		helpDialog.show();
	}

	public HelpPanel getHelpPanel() {
		return helpPanel;
	}

	public void setHelpPanel(HelpPanel helpPanel) {
		this.helpPanel = helpPanel;
	}

	public void showHelp(String helpTopic) {
		url = helpTopic;
		if (!isHelpPanelEnabled) {
			if (helpPanel != null) {
				helpPanel.removeFromParent();
				if (helpDialog != null) {
					helpDialog.removeFromParent();
				}
				helpPanel.setIsRemoved(true);
			}
			return;
		}
		if (helpTopic == null || helpTopic.isEmpty()) {
			return;
		}

		if (helpPanel == null) {
			return;
		}
		helpPanel.setHelpUrl(getUrl());
	}

	public void addRemoveHelpPanel() {
		if (!isHelpPanelEnabled) {
			Window.open("http://help.annaccounting.com", "_blank", "");
			return;
		}

		if (helpPanel == null) {
			helpPanel = (HelpPanel) createHelpPanel();
			if (isPanelEnabled()) {
				this.add(helpPanel);
				// this.setCellWidth(helpPanel, "50%");
			} else {
				helpPanel.setButtonDisabled(false);
				helpPanel.setButtonPushed(true);
				createHelpDialog();
			}
			return;
		}

		if (helpDialog != null) {
			helpDialog.removeFromParent();
		}
		if (helpPanel != null) {
			helpPanel.removeFromParent();
		}
		if (helpPanel.isRemoved()) {
			if (!helpPanel.isHelpPanel()) {
				helpPanel = (HelpPanel) createHelpPanel();
			}
			if (isPanelEnabled()) {
				this.add(helpPanel);
				// this.setCellWidth(helpPanel, "50%");
				helpPanel.setIsHelpPanel(true);
			} else {
				helpPanel.setButtonDisabled(false);
				helpPanel.setButtonPushed(true);
				createHelpDialog();
			}
			helpPanel.setIsRemoved(false);
			helpPanel.setHelpUrl(getUrl());
		} else {
			helpPanel.setIsRemoved(true);
		}
	}

	public void setHelpPannelEnabled(boolean isEnabled) {
		this.isHelpPanelEnabled = isEnabled;
	}

	public boolean isHelpPanelEnabled() {
		return this.isHelpPanelEnabled;
	}

	public void createView(MainFinanceWindow financeWindow, Widget leftMenu) {
		keyValues.put("ispaid", getCompany().isPaid() ? "Yes" : "No");
		// for bookkeeping value
		keyValues.put("bookKeeping", getCompany().isBookKeeping() ? "Yes"
				: "No");
		keyValues.put("emailId", getCompany().getLoggedInUser().getEmail());
		this.mainWindow = financeWindow;
		StyledPanel mainPanel = new StyledPanel("mainPanel");

		StyledPanel rightPanel = createRightPanel();
		StyledPanel leftPanel = new StyledPanel("");
		leftPanel.addStyleName("view_manager_body");
		// leftPanel.setWidth("100%");
		this.viewHolder = new SimplePanel();
		viewHolder.addStyleName("viewholder");
		
		Window.addResizeHandler(new ResizeHandler() {
			@Override
			public void onResize(ResizeEvent event) {
				viewHolder.setHeight((Window.getClientHeight() - 145)+"px");
			}
		});

		History.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {

				historyChanged(event.getValue());
			}
		});
		// handleBackSpaceEvent();
		this.toolBar = new ToolBar();
		leftPanel.add(toolBar);
		leftPanel.add(viewHolder);
		mainPanel.add(leftMenu);
		mainPanel.add(leftPanel);
		leftPanel.getElement().getParentElement().addClassName("view_manager");

		if (rightPanel != null) {
			mainPanel.add(rightPanel);
		}
		this.addStyleName("main_manager");
		this.add(mainPanel);
		initilizeToolBar();
		initializeActivityManager();
	}

	protected StyledPanel createRightPanel() {
		StyledPanel panel = new StyledPanel("rightPanel");
		if (!Accounter.hasPermission(Features.TRANSACTIONS)) {
			TransactionMeterPanel meterPanel = new TransactionMeterPanel();
			panel.add(meterPanel);
		}
		getAdvertisePanel(panel);
		panel.addStyleName("frame_manager");
		

		return panel;
	}

	public boolean isIpad() {
		return false;
	}

	public void showDialogRelativeTo(CustomDialog dialog, UIObject obj) {
		dialog.addCloseHandler(new CloseHandler<PopupPanel>() {

			@Override
			public void onClose(CloseEvent<PopupPanel> event) {
				existingDialog = null;
				existingView.getButtonBar().clear();
				addRequiredButtons();
				existingView.updateButtons();
			}
		});
		dialog.showRelativeTo(obj);
		dialog.getButtonBar().show();
	}

	CustomDialog existingDialog = null;

	public void showDialog(CustomDialog dialog) {
		if (!dialog.isViewDialog()) {
			dialog.center();
			return;
		}
		if (existingDialog != null) {
			showDialog(existingDialog, dialog);
			return;
		}
		dialog.addCloseHandler(new CloseHandler<PopupPanel>() {

			@Override
			public void onClose(CloseEvent<PopupPanel> event) {
				existingDialog = null;
				existingView.getButtonBar().clear();
				addRequiredButtons();
				existingView.updateButtons();
			}
		});
		dialog.center();
		dialog.getButtonBar().show();
		existingDialog = dialog;
	}

	public void showDialog(final CustomDialog parent, CustomDialog child) {
		if (!child.isViewDialog()) {
			child.center();
			return;
		}
		child.addCloseHandler(new CloseHandler<PopupPanel>() {

			@Override
			public void onClose(CloseEvent<PopupPanel> event) {
				existingDialog = parent;
				existingDialog.getButtonBar().clear();
				existingDialog.updateButtons();
				existingDialog.getButtonBar().show();
			}
		});
		child.center();
		child.getButtonBar().show();
		existingDialog = child;
	}

}
