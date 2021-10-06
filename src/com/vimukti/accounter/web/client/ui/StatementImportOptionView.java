package com.vimukti.accounter.web.client.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientStatement;
import com.vimukti.accounter.web.client.core.ClientStatementRecord;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.ViewManager;
import com.vimukti.accounter.web.client.ui.customers.FormatingConfilctDialog;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.widgets.DateUtills;

/**
 * The first time you import your statement you need to coordinate the fields in
 * your file to the fields that displays.
 * 
 * @author vimukti10
 * 
 */
public class StatementImportOptionView extends BaseView<ClientStatement> {

	private CustomLabel statusLabel;
	private List<String[]> importStatementData;
	private static final int UNASSIGNED = 0;
	private static final int DATE = 1;
	private static final int DESCRIPTION = 2;
	private static final int REFERENCENO = 3;
	private static final int SPENT = 4;
	private static final int RECEIVED = 5;
	private static final int CLOSINGBALANCE = 6;
	private Map<String, Integer> map = new HashMap<String, Integer>();
	private long accountId;
	private StyledPanel controlsPanel;
	private int currentStatementRecord = 1;
	Anchor nextHyperLink, previousHyperLink;
	private Label dateFormat_Label;
	private String selectedDateFormat;
	private ListBox dateFormatList;
	Map<Integer, String> comboIndexesMap = new HashMap<Integer, String>();
	Map<Integer, List<String>> valuesMap = new HashMap<Integer, List<String>>();
	/**
	 * Remove from comboIndexesMap
	 */
	private int key = 0;

	public StatementImportOptionView(long accountId) {
		super();
		this.setImportStatementData(importStatementData);
		this.accountId = accountId;

	}

	@Override
	public void init() {
		super.init();
		this.getElement().setId("StatementImportOptionView");
		createControls();
	}

	@Override
	public void initData() {
		if (data == null) {
			setData(new ClientStatement());
		}
	}

	/**
	 * Creating the view by using Controls
	 */
	private void createControls() {

		StyledPanel mainVLay = new StyledPanel("mainVLay");

		Label titleLabel = new Label("Statement Import Options");
		titleLabel.setStyleName("label-title");
		mainVLay.add(titleLabel);

		Label label = new Label("Statement lines imported from your file...");
		mainVLay.add(label);

		nextHyperLink = new Anchor("Next");
		nextHyperLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				currentStatementRecord++;
				statusLabel.setText("Statement line " + currentStatementRecord
						+ " of " + (importStatementData.size() - 1));
				setBody();
			}

		});

		previousHyperLink = new Anchor("Previous");
		previousHyperLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				currentStatementRecord--;
				statusLabel.setText("Statement line " + currentStatementRecord
						+ " of " + (importStatementData.size() - 1));
				setBody();

			}
		});

		statusLabel = new CustomLabel("Statement line "
				+ currentStatementRecord + " of "
				+ (importStatementData.size() - 1));
		StyledPanel anchorPanel = new StyledPanel("anchorPanel");
		anchorPanel.add(statusLabel);
		anchorPanel.add(previousHyperLink);
		anchorPanel.add(nextHyperLink);
		mainVLay.add(anchorPanel);

		controlsPanel = new StyledPanel("controlsPanel");
		setBody();

		mainVLay.add(controlsPanel);

		// mainVLay.setSize("100%", "100%");

		StyledPanel datePanel = new StyledPanel("datePanel");
		dateFormat_Label = new Label(messages.DateFormat());
		dateFormatList = new ListBox(false);
		String[] dateFormates = new String[] { "ddMMyy", "MM/dd/yy",
				"dd/MM/yy", "ddMMyyyy", "MMddyyyy", "MMM-dd-yy", "MMMddyyyy",
				"dd/MM/yyyy", "MM/dd/yyyy", "dd/MMMM/yyyy", "MMMMddyyyy",
				"dd-MM-yyyy", "MM-dd-yyyy", "dd/MMM/yyyy", "MMM/dd/yyyy", };

		for (int i = 0; i < dateFormates.length; i++) {
			dateFormatList.addItem(dateFormates[i]);
		}

		dateFormatList.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				selectedDateFormat = getSelectedDateFormat(dateFormatList
						.getSelectedIndex());
			}
		});
		dateFormatList.setSelectedIndex(1);
		selectedDateFormat = getSelectedDateFormat(1);
		datePanel.add(dateFormat_Label);
		datePanel.add(dateFormatList);

		mainVLay.add(datePanel);

		this.add(mainVLay);
		// setSize("100%", "100%");
	}

	/**
 * 
 */
	private void setBody() {
		controlsPanel.clear();
		comboIndexesMap.clear();
		for (int i = 0; i < getImportStatementData().get(0).length; i++) {
			StyledPanel valuesPanel = new StyledPanel("valuesPanel");
			final String head = getImportStatementData().get(0)[i];
			final String value = getImportStatementData().get(
					currentStatementRecord)[i];
			CustomLabel headingLabel = new CustomLabel(head);
			CustomLabel valuesLabel = new CustomLabel(value);
			final ListBox selectionBox = new ListBox();
			selectionBox.addChangeHandler(new ChangeHandler() {
				@Override
				public void onChange(ChangeEvent event) {
					final int selectedComboIndex = selectionBox
							.getSelectedIndex();
					setvalues(head, value, selectedComboIndex);
					//
					if (comboIndexesMap.isEmpty()) {
						comboIndexesMap.put(selectedComboIndex, value);
						List<String> list = new ArrayList<String>();
						list.add(head);
						list.add(value);
						valuesMap.put(selectedComboIndex, list);
					} else {
						// selected combo index is date
						if (selectedComboIndex == 1 && (!head.contains("Date"))) {
							selectionBox.setSelectedIndex(0);
							Accounter
									.showInformation("This column does not contain valid values for  Date");
						} else {
							// if combo selection is multiple or not.
							if (comboIndexesMap.containsKey(selectedComboIndex)) {
								List<String> list = valuesMap
										.get(selectedComboIndex);
								FormatingConfilctDialog confilctDialog = new FormatingConfilctDialog(
										list.get(0), list.get(1), head, value) {
									// When click the OK button on format
									// dialog
									protected boolean onOK() {
										Set<Entry<Integer, String>> entrySet = comboIndexesMap
												.entrySet();
										for (Entry<Integer, String> entry : entrySet) {
											if (entry.getValue().equals(value)) {
												comboIndexesMap.put(
														entry.getKey(), "0");
												key = entry.getKey().intValue();
												break;
											}
										}
										comboIndexesMap.put(selectedComboIndex,
												value);
										List<String> list = new ArrayList<String>();
										list.add(head);
										list.add(value);
										valuesMap.put(selectedComboIndex, list);
										setSelectedValue(selectionBox);
										comboIndexesMap.remove(key);
										return true;
									};

									// Cancel clicked
									protected boolean onCancel() {
										selectionBox.setSelectedIndex(0);
										return true;
									};
								};
								ViewManager.getInstance().showDialog(
										confilctDialog);

							} else {
								List<String> list = new ArrayList<String>();
								list.add(head);
								list.add(value);
								valuesMap.put(selectedComboIndex, list);
								comboIndexesMap.put(selectedComboIndex, value);
							}
						}
					}
				}
			});

			selectionBox.addItem("Unassigned");
			selectionBox.addItem(messages.date());
			selectionBox.addItem(messages.description());
			selectionBox.addItem(messages.referenceNo());
			selectionBox.addItem("Spent");
			selectionBox.addItem("Received");
			selectionBox.addItem(messages.ClosingBalance());
			if (!map.isEmpty()) {
				int index = map.get(head) == null ? 0 : map.get(head);
				if (index == 0) {
					selectionBox.setSelectedIndex(0);
				} else {
					selectionBox.setSelectedIndex(index);
				}
			}
			valuesPanel.add(headingLabel);
			valuesPanel.add(valuesLabel);
			valuesPanel.add(selectionBox);
			controlsPanel.add(valuesPanel);
			controlsPanel.addStyleName("statement_import_options");
		}
		// disable the previous link
		if (currentStatementRecord == 1) {
			previousHyperLink.setVisible(false);
		} else {
			previousHyperLink.setVisible(true);
		}
		// disable the next link
		if (currentStatementRecord == importStatementData.size() - 1) {
			nextHyperLink.setVisible(false);
		} else {
			nextHyperLink.setVisible(true);
		}
	}

	/**
	 * change the value for particular list box item
	 * 
	 * @param selectionBox
	 * @param isOkcliecked
	 */
	private void setSelectedValue(ListBox selectionBox) {
		for (int x = 0; x < controlsPanel.getWidgetCount(); x++) {
			StyledPanel panel = (StyledPanel) controlsPanel.getWidget(x);
			ListBox listBox = (ListBox) panel.getWidget(2);
			if (listBox != selectionBox
					&& listBox.getSelectedIndex() == selectionBox
							.getSelectedIndex()) {
				listBox.setSelectedIndex(0);
				break;
			}

		}
	}

	/**
	 * mapping the selected index
	 * 
	 * @param head
	 * @param value
	 * @param selectedIndex
	 */
	private void setvalues(String head, String value, int selectedIndex) {

		switch (selectedIndex) {
		case UNASSIGNED:
			map.put(head, selectedIndex);
			break;
		case DATE:
			map.put(head, selectedIndex);
			break;
		case REFERENCENO:
			map.put(head, selectedIndex);
			break;
		case SPENT:
			map.put(head, selectedIndex);
			break;
		case RECEIVED:
			map.put(head, selectedIndex);
			break;
		case DESCRIPTION:
			map.put(head, selectedIndex);
			break;
		case CLOSINGBALANCE:
			map.put(head, selectedIndex);
			break;

		default:
			map.put(head, selectedIndex);
			break;
		}
	}

	/**
	 * Creating the Statement Object
	 */
	public void saveObject() {

		List<ClientStatementRecord> records = new ArrayList<ClientStatementRecord>();
		for (int i = 1; i < importStatementData.size(); i++) {
			ClientStatementRecord st = new ClientStatementRecord();
			st.setCompanyId(getCompany().getID());
			for (int j = 0; j < importStatementData.get(i).length; j++) {
				String val = importStatementData.get(i)[j];
				String head = importStatementData.get(0)[j];
				compare(val, head, st);
			}
			records.add(st);
		}
		double openingBalance = 0.0;
		data.setImporttedDate(new ClientFinanceDate());
		if (records.get(0).getSpentAmount() > 0) {
			openingBalance = records.get(0).getClosingBalance()
					+ records.get(0).getSpentAmount();
		} else {
			openingBalance = records.get(0).getClosingBalance()
					- records.get(0).getReceivedAmount();
		}
		data.setStartBalance(openingBalance);
		data.setClosingBalance(records.get(records.size() - 1)
				.getClosingBalance());
		data.setStartDate(records.get(0).getStatementDate());
		data.setEndDate(records.get(records.size() - 1).getStatementDate());
		data.setAccount(accountId);
		data.setStatementList(records);
		data.setCompanyId(getCompany().getID());
	}

	/**
	 * 
	 * 
	 * @param val
	 * @param head
	 * @param rec
	 */
	private void compare(String val, String head, ClientStatementRecord rec) {
		int value = (int) (map.get(head) == null ? 0 : map.get(head));
		// System.out.println("value:" + value + "head:" + head);

		switch (value) {
		case UNASSIGNED:
			break;
		case DATE:
			if (head.contains(messages.date())) {
				ClientFinanceDate date = DateUtills.getSelectedFormatDate(val,
						selectedDateFormat);
				// ClientFinanceDate clientFinanceDate = new ClientFinanceDate(
				// new Date(val));
				rec.setStatementDate(date);
			}
			break;
		case REFERENCENO:
			if (head.contains("Chq./Ref.No") || head.contains("Cheq/Ref")
					|| head.contains(messages.referenceNo())
					|| head.contains(messages.checkNo())) {
				rec.setReferenceNumber(val);
			}
			break;
		case SPENT:
			if (head.contains("Spent") || head.contains(messages.amount())
					|| head.contains("Withdrawal")) {
				rec.setSpentAmount(Double
						.parseDouble(val.trim().length() == 0 ? "0.0" : val));

			}
			break;
		case RECEIVED:
			if (head.contains("Received") || head.contains(messages.amount())
					|| head.contains("Deposit")) {
				rec.setReceivedAmount(Double
						.parseDouble(val.trim().length() == 0 ? "0.0" : val));
			}
			break;
		case DESCRIPTION:
			if (head.contains(messages.description())) {
				rec.setDescription(val);
			}
			break;
		case CLOSINGBALANCE:
			if (head.contains(messages.ClosingBalance())) {
				rec.setClosingBalance(Double
						.parseDouble(val.trim().length() == 0 ? "0.0" : val));
			}
			break;

		default:
			break;
		}

	}

	public List<String[]> getImportStatementData() {
		return importStatementData;
	}

	public void setImportStatementData(List<String[]> importStatementData) {
		this.importStatementData = importStatementData;
	}

	@Override
	public ClientStatement saveView() {
		ClientStatement saveView = super.saveView();
		if (saveView != null) {
			saveObject();
		}
		return saveView;
	}

	@Override
	public void saveFailed(AccounterException exception) {
		super.saveFailed(exception);
		AccounterException accounterException = exception;
		int errorCode = accounterException.getErrorCode();
		String errorString = AccounterExceptions.getErrorString(errorCode);
		Accounter.showError(errorString);
	}

	@Override
	public void saveSuccess(IAccounterCore result) {
		if (result != null) {
			super.saveSuccess(result);
		} else {
			saveFailed(new AccounterException());
		}
	}

	@Override
	public void saveAndUpdateView() {
		saveObject();
		saveOrUpdate(getData());
	}

	@Override
	public void deleteFailed(AccounterException caught) {

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {

	}

	@Override
	protected String getViewTitle() {
		return null;
	}

	@Override
	public List<DynamicForm> getForms() {
		return null;
	}

	@Override
	public void setFocus() {

	}

	/**
	 * get selected date format
	 * 
	 * @param index
	 *            of combo Box
	 */
	private String getSelectedDateFormat(int i) {
		switch (i) {
		case 0:
			return "ddMMyy";

		case 1:
			return "MM/dd/yy";

		case 2:
			return "dd/MM/yy";

		case 3:
			return "ddMMyyyy";

		case 4:
			return "MMddyyyy";

		case 5:
			return "MMM-dd-yy";

		case 6:
			return "MMMddyyyy";

		case 7:
			return "dd/MM/yyyy";

		case 8:
			return "MM/dd/yyyy";

		case 9:
			return "dd/MMMM/yyyy";

		case 10:
			return "MMMMddyyyy";

		case 11:
			return "dd-MM-yyyy";

		case 12:
			return "MM-dd-yyyy";

		case 13:
			return "dd/MMM/yyyy";

		case 14:
			return "MMM/dd/yyyy";

		}
		return null;
	}
}
