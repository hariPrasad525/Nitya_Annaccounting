package com.nitya.accounter.web.client.ui.payroll;

import java.util.Arrays;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.nitya.accounter.web.client.core.ClientAttendanceManagementItem;
import com.nitya.accounter.web.client.core.ClientAttendanceOrProductionItem;
import com.nitya.accounter.web.client.core.ClientAttendanceOrProductionType;
import com.nitya.accounter.web.client.core.ClientEmployee;
import com.nitya.accounter.web.client.core.ClientEmployeeCompsensation;
import com.nitya.accounter.web.client.core.ClientEmployeeGroup;
import com.nitya.accounter.web.client.core.ClientPayHead;
import com.nitya.accounter.web.client.core.ClientPayStructureDestination;
import com.nitya.accounter.web.client.core.ClientUserDefinedPayHead;
import com.nitya.accounter.web.client.core.ClientUserDefinedPayheadItem;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.core.PaginationList;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.UIUtils;
import com.nitya.accounter.web.client.ui.core.BaseView;
import com.nitya.accounter.web.client.ui.core.Calendar;
import com.nitya.accounter.web.client.ui.core.ICurrencyProvider;
import com.nitya.accounter.web.client.ui.edittable.AmountColumn;
import com.nitya.accounter.web.client.ui.edittable.EditTable;
import com.nitya.accounter.web.client.ui.edittable.TextEditColumn;
import com.nitya.accounter.web.client.ui.grids.TreeListGrid;

public class AttendanceManagementTable extends
		TreeListGrid<ClientAttendanceManagementItem> {

	private EmployeeColumn employeeColumn;
	private List<ClientAttendanceManagementItem> items;
	private ICurrencyProvider currencyProvider;
	private ClientPayStructureDestination destination;
	private boolean gotRecords = false;
	private BaseView view;

	public AttendanceManagementTable(ICurrencyProvider currencyProvider) {
		super();
		this.currencyProvider = currencyProvider;
	}

	@Override
	protected ClientAttendanceManagementItem getEmptyRow() {
		return new ClientAttendanceManagementItem();
	}

	@Override
	protected void initColumns() {
		Accounter.createPayrollService().getAttendanceProductionOrUserDefined(
				0, -1, new AsyncCallback<PaginationList<IAccounterCore>>() {

					@Override
					public void onSuccess(PaginationList<IAccounterCore> result) {
						gotRecords = true;
						AttendanceManagementTable.this
								.createAttendanceOrProductionTypeColumns(result);
						setAllRows(items);
						if (destination != null) {
							updateList(destination);
						}
					}

					@Override
					public void onFailure(Throwable caught) {

					}
				});

	}

	protected void createAttendanceOrProductionTypeColumns(
			PaginationList<IAccounterCore> result) {

		employeeColumn = new EmployeeColumn() {

			@Override
			public int getWidth() {
				return 100;
			}

			@Override
			protected boolean isEnable() {
				return false;
			}
		};

		this.addColumn(employeeColumn);

		this.addColumn(new TextEditColumn<ClientAttendanceManagementItem>() {

			@Override
			protected String getValue(ClientAttendanceManagementItem row) {
				return UIUtils.toStr(row.getAbscentDays());
			}

			@Override
			protected void setValue(ClientAttendanceManagementItem row,
					String value) {
				row.setAbscentDays(UIUtils.toDbl(value));
				update(row);
			}

			@Override
			protected String getColumnName() {
				return messages.abscent();
			}

			@Override
			public int getWidth() {
				return 80;
			}

			@Override
			public String getValueAsString(ClientAttendanceManagementItem row) {
				return getValue(row);
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}
		});

		for (final IAccounterCore accounterCore : result) {
			if (accounterCore instanceof ClientAttendanceOrProductionType) {
				final ClientAttendanceOrProductionType attendanceOrProduction = (ClientAttendanceOrProductionType) accounterCore;
				final ClientAttendanceOrProductionItem item = new ClientAttendanceOrProductionItem();
				item.setAttendanceOrProductionType(attendanceOrProduction
						.getID());
				item.setClientAttendanceOrProductionType(attendanceOrProduction);
				item.setValue(UIUtils.toDbl(0));
				this.addColumn(new TextEditColumn<ClientAttendanceManagementItem>() {

					@Override
					protected String getValue(ClientAttendanceManagementItem row) {
						if (attendanceOrProduction.getPeriodType() == ClientPayHead.CALCULATION_PERIOD_HOURS) {
							if (Arrays.asList(ClientEmployeeCompsensation.PAY_TYPE_ANNUAL, ClientEmployeeCompsensation.PAY_TYPE_HOURLY, ClientEmployeeCompsensation.PAY_TYPE_DAILY).contains(row.getEmployeePayType())) {
						     	return getWorkingDays() * 8 + "";
							}
						}
						List<ClientAttendanceOrProductionItem> attendanceOrProductionItems = row
								.getAttendanceOrProductionItems();
						for (ClientAttendanceOrProductionItem item : attendanceOrProductionItems) {
							if (item.getAttendanceOrProductionType() == accounterCore
									.getID()) {
								return UIUtils.toStr(item.getValue());
							}
						}
						return UIUtils.toStr(item.getValue());
					}

					@Override
					protected void setValue(ClientAttendanceManagementItem row,
							String value) {
						for (ClientAttendanceOrProductionItem item : row
								.getAttendanceOrProductionItems()) {
							if (item.getAttendanceOrProductionType() == accounterCore
									.getID()) {
								item.setValue(UIUtils.toDbl(value));
								update(row);
								return;
							}
							
						}
						item.setValue(UIUtils.toDbl(value));
						row.getAttendanceOrProductionItems().add(item);
						update(row);
					}

					@Override
					protected String getColumnName() {
						return accounterCore.getName();
					}

					@Override
					public int getWidth() {
						return 80;
					}

					@Override
					public String getValueAsString(
							ClientAttendanceManagementItem row) {
						return getValue(row);
					}

					@Override
					public int insertNewLineNumber() {
						return 1;
					}
				});
			} else {
				final ClientUserDefinedPayHead udPayheadItem = (ClientUserDefinedPayHead) accounterCore;
				final ClientUserDefinedPayheadItem item = new ClientUserDefinedPayheadItem();
				item.setPayHead(udPayheadItem);
				item.setValue(UIUtils.toDbl(0));
				this.addColumn(new AmountColumn<ClientAttendanceManagementItem>(
						currencyProvider, true) {

					@Override
					protected Double getAmount(
							ClientAttendanceManagementItem row) {
						List<ClientUserDefinedPayheadItem> attendanceOrProductionItems = row
								.getUserDefinedPayheads();
						for (ClientUserDefinedPayheadItem item : attendanceOrProductionItems) {
							if (item.getPayHeadID() == accounterCore.getID()) {
								return item.getValue();
							}
						}
						return 0.0;
					}

					@Override
					protected void setAmount(
							ClientAttendanceManagementItem row, Double value) {
						for (ClientUserDefinedPayheadItem item : row
								.getUserDefinedPayheads()) {
							if (item.getPayHeadID() == accounterCore.getID()) {
								item.setValue(value);
								update(row);
								return;
							}
						}
						item.setValue(value);
						row.getUserDefinedPayheads().add(item);
						update(row);
					}

					@Override
					public String getValueAsString(
							ClientAttendanceManagementItem row) {
						return getValue(row);
					}

					@Override
					public int insertNewLineNumber() {
						return 1;
					}

					@Override
					public int getWidth() {
						return 80;
					}

					@Override
					protected String getColumnName() {
						return accounterCore.getName();
					}
				});
			}
		}
	}
	
	private long getWorkingDays() {
		if(view == null) {
			return 0;
		}
		NewPayRunView payRunView = (NewPayRunView) view;
		return payRunView.getWorkingDays();
	}

	@Override
	protected boolean isInViewMode() {
		return false;
	}
	
	public void setView(BaseView view) {
		this.view = view;
	}

	public void updateList(ClientPayStructureDestination group) {
		employeeColumn.updateList(group);
	}
	

	public void setSelectedEmployeeOrGroup(
			ClientPayStructureDestination selectedValue) {
		clear();
		if (selectedValue instanceof ClientEmployee) {
			ClientEmployee clientEmployee = (ClientEmployee) selectedValue;
			ClientAttendanceManagementItem item = new ClientAttendanceManagementItem();
			item.setEmployee(clientEmployee.getID());
			item.setEmployeePayType(clientEmployee.getPayType());
			add(item);
		} else {
			ClientEmployeeGroup clientEmployeeGroup = (ClientEmployeeGroup) selectedValue;
			for (ClientEmployee employee : clientEmployeeGroup.getEmployees()) {
				ClientAttendanceManagementItem item = new ClientAttendanceManagementItem();
				item.setEmployee(employee.getID());
				item.setEmployeePayType(employee.getPayType());
				add(item);
			}
		}
	}

	public void setData(List<ClientAttendanceManagementItem> items) {
		this.items = items;
	}

	public void setDestination(ClientPayStructureDestination destination) {
		if (gotRecords) {
			updateList(destination);
		} else {
			this.destination = destination;
		}

	}

}
