package com.vimukti.accounter.web.client.ui.reports;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Button;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.combo.CustomerCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.JobCombo;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.combo.VendorCombo;
import com.vimukti.accounter.web.client.ui.forms.DateItem;

public class CreateStatementToolBar extends ReportToolbar {
	DateItem fromItem;
	DateItem toItem;
	private CustomerCombo customerCombo;
	private VendorCombo vendorCombo;
	private ClientCustomer selectedCusotmer = null;
	private ClientVendor selectedVendor = null;
	private SelectCombo dateRangeItemCombo, viewSelect;
	private List<String> dateRangeItemList;
	private Button updateButton;
	private final boolean isVendor;
	public static final int VIEW_ALL = 0;
	public static final int VIEW_OPEN = 1;
	public static final int VIEW_OVERDUE = 2;
	public static final int VIEW_VOIDED = 3;
	public static final int VIEW_DRAFT = 4;
	protected JobCombo jobCombo;

	public CreateStatementToolBar(boolean isVendor,
			AbstractReportView reportView) {
		this.isVendor = isVendor;
		this.reportview = reportView;
		createControls();
	}

	@Override
	public ClientFinanceDate getEndDate() {
		return (endDate == null || endDate.getDate() == 0) ? new ClientFinanceDate()
				: toItem.getDate();
	}

	public void createControls() {

		String[] filter = { messages.open(), messages.overDue(), messages.all() };
		viewSelect = new SelectCombo(messages.currentView());
		viewSelect.initCombo(Arrays.asList(filter));
		viewSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						setFilter(selectItem);
						payeeData();
					}
				});
		setFilter(messages.open());
		String[] dateRangeArray = { messages.all(), messages.thisWeek(),
				messages.thisMonth(), messages.lastWeek(),
				messages.lastMonth(), messages.thisFinancialYear(),
				messages.lastFinancialYear(), messages.thisFinancialQuarter(),
				messages.lastFinancialQuarter(),
				messages.financialYearToDate(), messages.custom() };

		jobCombo = new JobCombo("Job", false);

		if (isVendor) {
			vendorCombo = new VendorCombo(
					messages.choose(Global.get().Vendor()), false);
			new StatementReport(true);
			vendorCombo
					.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientVendor>() {

						@Override
						public void selectedComboBoxItem(ClientVendor selectItem) {
							setPayeeId(selectItem.getID());

						}

					});

			if (getPayeeId() != 0) {
				vendorData(Accounter.getCompany().getVendor(getPayeeId()));
			}
		} else {
			customerCombo = new CustomerCombo(messages.choose(Global.get()
					.Customer()), false);
			new StatementReport(false);
			customerCombo
					.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientCustomer>() {

						@Override
						public void selectedComboBoxItem(
								ClientCustomer selectItem) {
							setPayeeId(selectItem.getID());
							createDisplayJobCombo(selectItem);
						}
					});
			if (getPayeeId() != 0) {
				customerData(Accounter.getCompany().getCustomer(getPayeeId()));
			}
		}

		dateRangeItemCombo = new SelectCombo(messages.dateRange());
		dateRangeItemList = new ArrayList<String>();
		for (int i = 0; i < dateRangeArray.length; i++) {
			dateRangeItemList.add(dateRangeArray[i]);
		}
		dateRangeItemCombo.initCombo(dateRangeItemList);
		dateRangeItemCombo.setComboItem(messages.thisMonth());
		// dateRangeChanged(dateRangeItemCombo.getSelectedValue());
		dateRangeItemCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						dateRangeChanged(dateRangeItemCombo.getSelectedValue());

					}
				});

		fromItem = new DateItem(messages.from(), "fromItem");

		toItem = new DateItem(messages.to(), "toItem");
		ClientFinanceDate date = Accounter.getCompany()
				.getCurrentFiscalYearEndDate();
		// .getLastandOpenedFiscalYearEndDate();

		// if (date != null)
		// toItem.setDatethanFireEvent(date);
		// else
		// toItem.setDatethanFireEvent(new ClientFinanceDate());

		toItem.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				startDate = fromItem.getValue();
				endDate = toItem.getValue();
				itemSelectionHandler.onItemSelectionChanged(TYPE_ACCRUAL,
						fromItem.getDate(), toItem.getDate());
			}
		});
		updateButton = new Button(messages.update());
		updateButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				setStartDate(fromItem.getDate());
				setEndDate(toItem.getDate());
				itemSelectionHandler.onItemSelectionChanged(TYPE_ACCRUAL,
						fromItem.getDate(), toItem.getDate());
				dateRangeItemCombo.setDefaultValue(messages.custom());
				dateRangeItemCombo.setComboItem(messages.custom());
				setSelectedDateRange(messages.custom());

			}
		});

		Button printButton = new Button(messages.print());
		printButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

			}

		});

		// if (UIUtils.isMSIEBrowser()) {
		// dateRangeItemCombo.setWidth("200px");
		// }
		if (this instanceof CreateJobIdToolBar) {
			addItems(customerCombo, jobCombo, dateRangeItemCombo, fromItem,
					toItem);
		} else {
			if (isVendor) {
				addItems(getViewSelect(), vendorCombo, dateRangeItemCombo,
						fromItem, toItem);
			} else {
				addItems(getViewSelect(), customerCombo, dateRangeItemCombo,
						fromItem, toItem);
			}
		}

		add(updateButton);
		// reportRequest();
	}

	private void setFilter(String open) {
		if (open.equalsIgnoreCase(messages.open())) {
			setViewId(VIEW_OPEN);
		} else if (open.equalsIgnoreCase(messages.voided())) {
			setViewId(VIEW_VOIDED);
		} else if (open.equalsIgnoreCase(messages.overDue())) {
			setViewId(VIEW_OVERDUE);
		} else if (open.equalsIgnoreCase(messages.all())) {
			setViewId(VIEW_ALL);
		} else if (open.equalsIgnoreCase(messages.drafts())) {
			setViewId(VIEW_DRAFT);
		}
	}

	protected void customerData(ClientCustomer selectItem) {
		if (selectItem != null) {
			selectedCusotmer = selectItem;
			ClientFinanceDate startDate = fromItem.getDate();
			ClientFinanceDate endDate = toItem.getDate();
			reportview.makeReportRequest(selectedCusotmer.getID(), startDate,
					endDate);
			reportview.removeEmptyStyle();
			customerCombo.setSelected(selectedCusotmer.getName());
		}
	}

	protected void vendorData(ClientVendor selectItem) {
		if (selectItem != null) {
			selectedVendor = selectItem;
			ClientFinanceDate startDate = fromItem.getDate();
			ClientFinanceDate endDate = toItem.getDate();
			reportview.makeReportRequest(selectedVendor.getID(), startDate,
					endDate);
			reportview.removeEmptyStyle();
			vendorCombo.setSelected(selectedVendor.getName());
		}
	}

	/*
	 * @see
	 * com.vimukti.accounter.web.client.ui.reports.ReportToolbar#changeDates
	 * (java.util.Date, java.util.Date)
	 */
	@Override
	public void changeDates(ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		fromItem.setValue(startDate);
		toItem.setValue(endDate);
		itemSelectionHandler.onItemSelectionChanged(0, startDate, endDate);
		if (isVendor) {
			if (selectedVendor != null) {
				reportview.makeReportRequest(selectedVendor.getID(),
						fromItem.getDate(), toItem.getDate());
			} else {
				reportview.addEmptyMessage(messages.noRecordsToShow());
			}
		} else {
			if (selectedCusotmer != null) {
				reportview.makeReportRequest(selectedCusotmer.getID(),
						startDate, endDate);
			} else {
				reportview.addEmptyMessage(messages.noRecordsToShow());
			}
		}
	}

	@Override
	public void setDefaultDateRange(String defaultDateRange) {
		dateRangeItemCombo.setDefaultValue(defaultDateRange);
		dateRangeItemCombo.setComboItem(defaultDateRange);
		dateRangeChanged(defaultDateRange);
	}

	@Override
	public void setDateRanageOptions(String... dateRanages) {
		dateRangeItemCombo.setValueMap(dateRanages);
	}

	@Override
	public void setStartAndEndDates(ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		if (startDate != null && endDate != null) {
			fromItem.setEnteredDate(startDate);
			toItem.setEnteredDate(endDate);
			setStartDate(startDate);
			setEndDate(endDate);
		}

	}

	public void reportRequest() {
		reportview.makeReportRequest(selectedCusotmer.getID(), startDate,
				endDate);
	}

	protected void createDisplayJobCombo(ClientCustomer selectItem) {

	}

	@Override
	protected void payeeData() {
		if (isVendor) {
			if (getPayeeId() != 0) {
				vendorData(Accounter.getCompany().getVendor(getPayeeId()));
				reportview.makeReportRequest(selectedVendor.getID(), startDate,
						endDate);
			}
		} else {
			if (getPayeeId() != 0) {
				customerData(Accounter.getCompany().getCustomer(getPayeeId()));
				reportview.makeReportRequest(selectedCusotmer.getID(),
						startDate, endDate);
			}
		}
	}

	public SelectCombo getViewSelect() {
		return viewSelect;
	}

	public void setViewSelect(SelectCombo viewSelect) {
		this.viewSelect = viewSelect;
	}

	public void setStatus(Integer status) {
		String string = null;
		if (status == VIEW_OPEN) {
			string = messages.open();
		} else if (status == VIEW_VOIDED) {
			string = messages.voided();
		} else if (status == VIEW_OVERDUE) {
			string = messages.overDue();
		} else if (status == VIEW_ALL) {
			string = messages.all();
		} else if (status == VIEW_DRAFT) {
			string = messages.drafts();
		}
		if (string == null) {
			return;
		}
		viewSelect.setComboItem(string);
		setFilter(string);
		payeeData();
	}
}
