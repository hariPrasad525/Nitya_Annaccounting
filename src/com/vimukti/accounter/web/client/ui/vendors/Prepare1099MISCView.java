/**
 * 
 */
package com.vimukti.accounter.web.client.ui.vendors;

import java.util.ArrayList;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SingleSelectionModel;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.Client1099Form;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.AbstractBaseView;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.GwtDisclosurePanel;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.ActionCallback;
import com.vimukti.accounter.web.client.ui.core.ViewManager;
import com.vimukti.accounter.web.client.ui.forms.AmountLabel;
import com.vimukti.accounter.web.client.ui.forms.ClickableSafeHtmlCell;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.reports.MISC1099TransactionDetailAction;

public class Prepare1099MISCView extends AbstractBaseView {
	private String[] boxes;
	private final int[] boxNums = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10,
			13, 14 };
	private int totalNoOf1099Forms;
	private double totalAll1099Payments;
	int vendorComboSelected;
	private ListDataProvider<Client1099Form> listDataProvider;

	private GwtDisclosurePanel disclosurePanel;
	private StyledPanel setupPanel, amountPanel, preview1099panel,
			companyAddressPanel, einNumPanel;
	private StyledPanel companyInfopanel, setVendorsPanel, setAccountsPanel;
	private Label setVendor, addAccount;
	private Label noOf1099FormsLabel;
	private Label companyInfoText, einInfoText;
	private AmountLabel total1099AmountLabel;
	private DynamicForm amountForm;
	private CellTable<Client1099Form> cellTable;
	private SelectCombo selectComboItem;
	private ArrayList<String> arrayList;
	private HTML changeVendorHtml, changeAccountsHtml, companyInfoHtml,
			einInfoHtml;
	StyledPanel mainPanel;

	int horizontalValue;
	int verticalValue;
	private SingleSelectionModel<Client1099Form> selectionModel;

	public Prepare1099MISCView() {
		this.getElement().setId("prepare1099MISCView");
	}

	@Override
	public void init() {
		boxes = new String[] { messages.boxNumber(1) + "\n(600.00)",
				messages.boxNumber(2) + "\n(10.00)",
				messages.boxNumber(3) + "\n(600.00)",
				messages.boxNumber(4) + "\n(0.00)",
				messages.boxNumber(5) + "\n(0.00)",
				messages.boxNumber(6) + "\n(600.00)",
				messages.boxNumber(7) + "\n(600.00)",
				messages.boxNumber(8) + "\n(10.00)",
				messages.boxNumber(9) + "\n(5000.00)",
				messages.boxNumber(10) + "\n(600.00)",
				messages.boxNumber(13) + "\n(0.00)",
				messages.boxNumber(14) + "\n(0.00)" };

		this.createControl();

	}

	@Override
	public void setFocus() {
		this.selectComboItem.setFocus();

	}

	@Override
	public void fitToSize(int height, int width) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEdit() {
		// TODO Auto-generated method stub

	}

	@Override
	public void printPreview() {
		// TODO Auto-generated method stub

	}

	@Override
	public void print() {
		// TODO Auto-generated method stub

	}

	public void createControl() {
		totalAll1099Payments = 0;

		mainPanel = new StyledPanel("mainPanel");

		mainPanel.add(getSetupPanel());
		mainPanel.add(getPreview1099());
		mainPanel.add(getPrintSetUp().getPanel());
		mainPanel.add(getEndButtons());

		this.add(mainPanel);

	}

	ClientVendor vendor = null;

	public CellTable<Client1099Form> get1099InformationGrid() {

		cellTable = new CellTable<Client1099Form>();

		selectionModel = new SingleSelectionModel<Client1099Form>();
		cellTable.setSelectionModel(selectionModel,
				DefaultSelectionEventManager
						.<Client1099Form> createCheckboxManager());

		CheckboxCell checkboxCell = new CheckboxCell();
		Column<Client1099Form, Boolean> checkBoxColumn = new Column<Client1099Form, Boolean>(
				checkboxCell) {

			@Override
			public Boolean getValue(Client1099Form object) {
				return selectionModel.isSelected(object);
			}
		};

		ClickableSafeHtmlCell informationLink = new ClickableSafeHtmlCell();

		Column<Client1099Form, SafeHtml> informationColumn = new Column<Client1099Form, SafeHtml>(
				informationLink) {

			@Override
			public SafeHtml getValue(Client1099Form object) {
				vendor = object.getVendor();
				return object.getVendorInformation();
			}
		};
		informationColumn
				.setFieldUpdater(new FieldUpdater<Client1099Form, SafeHtml>() {

					@Override
					public void update(int index, Client1099Form object,
							SafeHtml value) {
						new NewVendorAction().run(vendor, false);
					}
				});

		Column<Client1099Form, String> total1099PaymentsColumn = new Column<Client1099Form, String>(
				new ClickableTextCell()) {

			@Override
			public String getValue(Client1099Form object) {
				double total1099Payments = object.getTotal1099Payments();
				return total1099Payments != 0 ? "" + total1099Payments : "";
			}
		};
		total1099PaymentsColumn
				.setFieldUpdater(new FieldUpdater<Client1099Form, String>() {

					@Override
					public void update(int index, Client1099Form object,
							String value) {
						MISC1099TransactionDetailAction action = new MISC1099TransactionDetailAction();
						action.setBoxNo(Client1099Form.TOTAL_1099_PAYMENTS);
						action.setVendorId(object.getVendor().getID());
						action.run();
					}
				});

		Column<Client1099Form, String> totalAllPaymentsColumn = new Column<Client1099Form, String>(
				new ClickableTextCell()) {

			@Override
			public String getValue(Client1099Form object) {
				double totalAllPayments = object.getTotalAllPayments();
				return totalAllPayments != 0 ? "" + totalAllPayments : "";
			}
		};
		totalAllPaymentsColumn
				.setFieldUpdater(new FieldUpdater<Client1099Form, String>() {

					@Override
					public void update(int index, Client1099Form object,
							String value) {
						MISC1099TransactionDetailAction action = new MISC1099TransactionDetailAction();
						action.setBoxNo(Client1099Form.TOATAL_ALL_PAYMENTS);
						action.setVendorId(object.getVendor().getID());
						action.run();
					}
				});

		cellTable.addColumn(checkBoxColumn, messages.select());
		cellTable.addColumn(informationColumn,
				messages.payeeInformation(Global.get().Vendor()));
		addBoxColumnsToCellTable(cellTable);
		cellTable.addColumn(total1099PaymentsColumn,
				messages.total1099Payments());
		cellTable
				.addColumn(totalAllPaymentsColumn, messages.totalAllPayments());

		return cellTable;
	}

	private ClientAccount getAccountByBoxNum(int i) {
		ArrayList<ClientAccount> activeAccounts = getCompany()
				.getActiveAccounts();
		for (ClientAccount clientAccount : activeAccounts) {
			if (clientAccount.getBoxNumber() == i) {
				return clientAccount;
			}
		}
		return null;
	}

	private void addBoxColumnsToCellTable(CellTable<Client1099Form> cellTable) {
		for (int i = 0; i < boxNums.length; i++) {

			final int boxNum = boxNums[i];
			ClientAccount clientAccount = getAccountByBoxNum(boxNum);
			if (clientAccount != null) {
				Column<Client1099Form, String> boxCell = new Column<Client1099Form, String>(
						new ClickableTextCell()) {

					@Override
					public String getValue(Client1099Form object) {
						double box = object.getBox(boxNum);
						return box != 0 ? "" + box : "";
					}
				};
				boxCell.setFieldUpdater(new FieldUpdater<Client1099Form, String>() {

					@Override
					public void update(int index, Client1099Form object,
							String value) {
						MISC1099TransactionDetailAction action = new MISC1099TransactionDetailAction();
						action.setBoxNo(boxNum);
						action.setVendorId(object.getVendor().getID());
						action.run();
					}
				});
				cellTable.addColumn(boxCell, boxes[i]);
			}
		}
	}

	private Widget getSetupPanel() {

		disclosurePanel = (GwtDisclosurePanel) GWT
				.create(GwtDisclosurePanel.class);
		disclosurePanel.setTitle(messages.setupVendorsAndAccounts(Global.get()
				.vendors()));
		// disclosurePanel.setOpen(true);

		setVendorsPanel = new StyledPanel("setVendorsPanel");
		setAccountsPanel = new StyledPanel("setAccountsPanel");

		setVendor = new Label(getSelectedVendorsNum() + " "
				+ messages.vendorsSelected(Global.get().vendors()));

		addAccount = new Label(getSelectedAccountsNum() + " "
				+ messages.accountsSelected());
		changeVendorHtml = new HTML(messages.changePayees(Global.get()
				.vendors()));
		changeVendorHtml.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				final SelectItemsTo1099Dialog<ClientVendor> selectVendorsTo1099Dialog = new SelectItemsTo1099Dialog<ClientVendor>(
						messages.vendorsSelected(Global.get().vendors()),
						messages.SelectVendorsToTrack1099(Global.get()
								.vendors()));
				ArrayList<ClientVendor> vendors = getCompany().getVendors();
				ArrayList<ClientVendor> tempSelectedItemsList = new ArrayList<ClientVendor>();

				tempSelectedItemsList.addAll(Utility.filteredList(
						new ListFilter<ClientVendor>() {

							@Override
							public boolean filter(ClientVendor e) {
								return e.isTrackPaymentsFor1099();
							}
						}, vendors));
				selectVendorsTo1099Dialog
						.setSelectedItems(tempSelectedItemsList);

				selectVendorsTo1099Dialog.setAvailableItems(getCompany()
						.getVendors());

				selectVendorsTo1099Dialog
						.setCallBack(new ActionCallback<ArrayList<ClientVendor>>() {

							@Override
							public void actionResult(
									ArrayList<ClientVendor> result) {
								for (ClientVendor vendor : selectVendorsTo1099Dialog.tempSelectedItemsList) {
									vendor.setTrackPaymentsFor1099(true);
									saveOrUpdate(vendor);
								}
								for (ClientVendor vendor : selectVendorsTo1099Dialog.tempAvailItemsList) {
									vendor.setTrackPaymentsFor1099(false);
									saveOrUpdate(vendor);
								}

							}
						});
				ViewManager.getInstance().showDialog(selectVendorsTo1099Dialog);
			}
		});

		changeAccountsHtml = new HTML("<b>"
				+ messages.changePayees(messages.Accounts()) + "</b>");
		changeAccountsHtml.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				String assignAccounts = messages.assignAccounts();
				AssignAccountsTo1099Dialog assignAccountsTo1099Dialog = new AssignAccountsTo1099Dialog(
						assignAccounts, assignAccounts);
				assignAccountsTo1099Dialog
						.setCallback(new ActionCallback<int[]>() {

							@Override
							public void actionResult(int[] result) {
								refreshView();
							}
						});
				ViewManager.getInstance()
						.showDialog(assignAccountsTo1099Dialog);
			}
		});

		setVendorsPanel.add(setVendor);
		setVendorsPanel.add(changeVendorHtml);

		setAccountsPanel.add(addAccount);
		setAccountsPanel.add(changeAccountsHtml);

		Label infoLable = new Label(messages.MISCInfo());

		infoLable.setWordWrap(true);

		setupPanel = new StyledPanel("setupPanel");
		// setupPanel.setSize("100%", "100%");
		setupPanel.add(infoLable);
		setupPanel.add(setVendorsPanel);
		setupPanel.add(setAccountsPanel);
		disclosurePanel.setContent(setupPanel);

		return disclosurePanel.getPanel();
	}

	private int getSelectedVendorsNum() {
		ArrayList<ClientVendor> list = new ArrayList<ClientVendor>();
		for (ClientVendor vendor : getCompany().getActiveVendors()) {
			if (vendor.isTrackPaymentsFor1099())
				list.add(vendor);
		}
		return list.size();
	}

	private int getSelectedAccountsNum() {
		ArrayList<ClientAccount> list = new ArrayList<ClientAccount>();
		for (ClientAccount clientAccount : getCompany().getAccounts()) {
			if (clientAccount.getBoxNumber() != 0)
				list.add(clientAccount);
		}
		return list.size();
	}

	private void refreshView() {
		this.remove(mainPanel);
		createControl();
	}

	private StyledPanel getPreview1099() {
		Label label = new Label(messages.preview1099Informaion());

		companyAddressPanel = new StyledPanel("companyAddressPanel");
		companyInfoText = new Label(messages.companyInformation());
		final ClientAddress address = getCompany().getRegisteredAddress();
		if (address != null) {
			SafeHtml safeHtml = new SafeHtml() {

				@Override
				public String asString() {
					return address.getAddressString();
				}
			};
			companyInfoHtml = new HTML(safeHtml);
			companyAddressPanel.add(companyInfoText);
			companyAddressPanel.add(companyInfoHtml);
		}

		einNumPanel = new StyledPanel("einNumPanel");
		einInfoText = new Label(messages.ein());
		String ein = getCompany().getEin();
		if (ein != null) {
			einInfoHtml = new HTML(ein);
			einNumPanel.add(einInfoText);
			einNumPanel.add(einInfoHtml);
		}

		companyInfopanel = new StyledPanel("companyInfopanel");
		// companyInfopanel.setSize("100%", "100%");
		companyInfopanel.add(companyAddressPanel);
		companyInfopanel.add(einNumPanel);

		amountPanel = new StyledPanel("amountPanel");
		amountPanel.addStyleName("boldtext");

		amountForm = new DynamicForm("amountForm");
		noOf1099FormsLabel = new Label(messages.totalNoOf1099Forms()
				+ totalNoOf1099Forms);

		total1099AmountLabel = new AmountLabel(messages.totalAll1099Payments());
		total1099AmountLabel.setAmount(totalAll1099Payments);

		amountForm.add(total1099AmountLabel);

		amountPanel.add(noOf1099FormsLabel);
		amountPanel.add(amountForm);

		arrayList = new ArrayList<String>();
		arrayList
				.add(messages.venodrsThatMeetThreshold(Global.get().vendors()));
		arrayList.add(messages.vendorsBelowThreshold(Global.get().vendors()));
		arrayList.add(messages.non1099Vendors(Global.get().vendors()));

		selectComboItem = new SelectCombo(messages.show());
		selectComboItem.initCombo(arrayList);
		selectComboItem.setSelected(arrayList.get(0));
		selectComboItem
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						initializeList(selectComboItem.getSelectedValue());

					}

				});

		DynamicForm dynamicForm = new DynamicForm("selectComboItem");
		dynamicForm.add(selectComboItem);

		preview1099panel = new StyledPanel("preview1099panel");
		preview1099panel.add(label);
		preview1099panel.add(companyInfopanel);
		preview1099panel.add(dynamicForm);
		preview1099panel.add(get1099InformationGrid());
		preview1099panel.add(amountPanel);
		initializeList(arrayList.get(0));
		return preview1099panel;

	}

	int selected;

	protected void initializeList(String selectedValue) {
		selected = -1;
		for (int i = 0; i < arrayList.size(); i++) {
			if (arrayList.get(i) == selectedValue) {
				selected = i;
				break;
			}
		}
		listDataProvider = new ListDataProvider<Client1099Form>();

		Accounter.get1099FormInformation(
				new AsyncCallback<ArrayList<Client1099Form>>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
					}

					@Override
					public void onSuccess(ArrayList<Client1099Form> result) {
						if (result.isEmpty()) {
							vendor = null;
						}
						listDataProvider.getList().addAll(result);
						setTotalAmountFields(result);
					}
				}, selected);

		listDataProvider.addDataDisplay(cellTable);

	}

	protected void setTotalAmountFields(ArrayList<Client1099Form> result) {
		totalAll1099Payments = 0;
		totalNoOf1099Forms = 0;
		if (selected == 0) {
			totalNoOf1099Forms = result.size();

			for (Client1099Form client1099Form : result) {
				totalAll1099Payments += client1099Form.getTotal1099Payments();
			}
		}
		noOf1099FormsLabel.setText(messages.totalNoOf1099Forms()
				+ totalNoOf1099Forms);
		total1099AmountLabel.setAmount(totalAll1099Payments);
	}

	private GwtDisclosurePanel getPrintSetUp() {
		GwtDisclosurePanel disclosurePanel = (GwtDisclosurePanel) GWT
				.create(GwtDisclosurePanel.class);
		disclosurePanel.setTitle(messages.printAlignmentAndSetup());

		Button printSample = new Button(messages.printSample());
		printSample.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (vendor == null) {
					Accounter.showError(messages.noTransactionAddedToMISCForm());
					return;
				}
				long vendorId = vendor.getID();
				int type = 2;
				long objectID = 1;
				long brandingThemeID = 1;
				UIUtils.downloadMISCForm(objectID, type, brandingThemeID,
						vendorId, horizontalValue, verticalValue);
			}

		});
		Label blankLabel = new Label(messages.loadEmptyPaper());
		Label adjustLabel = new Label(messages.MISCAdjustLabelText());

		Label sampleInfoLabel = new Label(messages.sampleInfoText());

		Label sampleInfoLabel2 = new Label(messages.sampleInfoLabelText());

		HTML verLabel = new HTML("<B>" + messages.vertical() + "</B>");
		HTML horLabel = new HTML("<B> " + messages.horizantal() + " </B>");

		Label infoLabel = new Label(messages.MISCInfoLabelText());

		// HTML alignmentSelected = new HTML("<B> Horizantal :</B>"
		// + horizontalValue + "<B> Vertical :</B>" + verticalValue);

		Grid advancedOptions = new Grid(8, 2);
		advancedOptions.setCellSpacing(6);
		advancedOptions.setWidget(0, 0, blankLabel);
		advancedOptions.setHTML(0, 1, "");
		advancedOptions.setWidget(1, 0, printSample);
		advancedOptions.setHTML(1, 1, "");
		advancedOptions.setWidget(2, 0, sampleInfoLabel);
		advancedOptions.setHTML(2, 1, "");
		advancedOptions.setWidget(3, 0, adjustLabel);
		advancedOptions.setHTML(3, 1, "");
		advancedOptions.setWidget(4, 0, verLabel);
		advancedOptions.setWidget(4, 1, getListBox(true, 1));
		advancedOptions.setWidget(5, 0, horLabel);
		advancedOptions.setWidget(5, 1, getListBox(true, 2));
		advancedOptions.setWidget(6, 0, sampleInfoLabel2);
		advancedOptions.setHTML(6, 1, "");
		advancedOptions.setWidget(7, 0, infoLabel);
		advancedOptions.setHTML(7, 1, "");

		disclosurePanel.add(advancedOptions);
		return disclosurePanel;
	}

	private StyledPanel getEndButtons() {

		Button print1099 = new Button(messages.printOn1099Form());
		Button printInfo = new Button(messages.printOnInformationSheet());
		Button cancel = new Button(messages.cancel());

		StyledPanel panel = new StyledPanel("prepare1099form");
		panel.add(print1099);
		panel.add(printInfo);
		panel.add(cancel);

		print1099.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				for (Client1099Form element : listDataProvider.getList()) {

					if (selectionModel.isSelected(element)) {
						long vendorId = element.getVendor().getID();
						int type = 1;
						long objectID = 1;
						long brandingThemeID = 1;
						UIUtils.downloadMISCForm(objectID, type,
								brandingThemeID, vendorId, horizontalValue,
								verticalValue);
					}
				}

			}
		});
		printInfo.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (listDataProvider.getList().size() > 0) {
					int type = 0;
					UIUtils.downloadMISCForm(selected, type, 1, 1,
							horizontalValue, verticalValue);
				}
			}
		});
		cancel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				cancel();
			}
		});

		return panel;
	}

	ListBox getListBox(boolean dropdown, final int box) {
		final ListBox widget = new ListBox();
		widget.addStyleName("demo-ListBox");
		widget.addItem("-60");
		widget.addItem("-50");
		widget.addItem("-40");
		widget.addItem("-30");
		widget.addItem("-20");
		widget.addItem("-10");
		widget.addItem("0");
		widget.addItem("10");
		widget.addItem("20");
		widget.addItem("30");
		widget.addItem("40");
		widget.addItem("50");
		widget.addItem("60");
		if (!dropdown)
			widget.setVisibleItemCount(3);

		widget.setSelectedIndex(7);
		widget.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(
					com.google.gwt.event.dom.client.ChangeEvent event) {
				int index = widget.getSelectedIndex();
				if (box == 1)
					horizontalValue = Integer.parseInt(widget
							.getItemText(index));
				else
					verticalValue = Integer.parseInt(widget.getItemText(index));

			}
		});

		return widget;
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
		// TODO Auto-generated method stub
		return null;
	}

}
