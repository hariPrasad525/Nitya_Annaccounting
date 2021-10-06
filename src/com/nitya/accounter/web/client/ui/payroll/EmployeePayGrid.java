package com.nitya.accounter.web.client.ui.payroll;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.nitya.accounter.web.client.core.ClientAttendanceManagementItem;
import com.nitya.accounter.web.client.core.ClientAttendanceOrProductionItem;
import com.nitya.accounter.web.client.core.ClientAttendanceOrProductionType;
import com.nitya.accounter.web.client.core.ClientEmployee;
import com.nitya.accounter.web.client.core.ClientEmployeeCompsensation;
import com.nitya.accounter.web.client.core.ClientEmployeeGroup;
import com.nitya.accounter.web.client.core.ClientEmployeePayRunItem;
import com.nitya.accounter.web.client.core.ClientPayStructureDestination;
import com.nitya.accounter.web.client.core.ClientUserDefinedPayHead;
import com.nitya.accounter.web.client.core.ClientUserDefinedPayheadItem;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.core.PaginationList;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.UIUtils;
import com.nitya.accounter.web.client.ui.core.ICurrencyProvider;
import com.nitya.accounter.web.client.ui.edittable.AmountColumn;
import com.nitya.accounter.web.client.ui.edittable.TextNonEditColumn;
import com.nitya.accounter.web.client.ui.edittable.TreeColumn;
import com.nitya.accounter.web.client.ui.grids.TreeListGrid;

public class EmployeePayGrid extends TreeListGrid<ClientEmployeePayRunItem> {

	private boolean gotRecords;
	private ClientPayStructureDestination destination;

	private ICurrencyProvider currencyProvider;
	PaginationList<IAccounterCore> attendnceList;
	private TextNonEditColumn<ClientEmployeePayRunItem> childTitleColumn;
	private NewPayRunView view;

	public EmployeePayGrid(ICurrencyProvider currencyProvider) {
		this.currencyProvider = currencyProvider;
		loadData(null);
	}

	public void loadData(AsyncCallback<PaginationList<IAccounterCore>> asyncCallback) {
		Accounter.createPayrollService().getAttendanceProductionOrUserDefined(0, -1,
				new AsyncCallback<PaginationList<IAccounterCore>>() {

					@Override
					public void onSuccess(PaginationList<IAccounterCore> result) {
						gotRecords = true;
						EmployeePayGrid.this.attendnceList = result;
						if(asyncCallback != null)
						   asyncCallback.onSuccess(result);
					}

					@Override
					public void onFailure(Throwable caught) {

					}
				});
	}
	
	private PaginationList<Object> getAttendnceList(int employePayType){
		PaginationList<Object> list = new PaginationList<Object>();
		for (final Object accounterCore : this.attendnceList) {
			if (accounterCore instanceof ClientAttendanceOrProductionType) {
				final ClientAttendanceOrProductionType attendanceOrProduction = (ClientAttendanceOrProductionType) accounterCore;
			    if( employePayType ==  ClientEmployeeCompsensation.PAY_TYPE_HOURLY && attendanceOrProduction.getDisplayName() == ClientAttendanceOrProductionType.DEFAULT_HOUR) {
			    	list.add(accounterCore);
			    } else if( employePayType ==  ClientEmployeeCompsensation.PAY_TYPE_MILES && attendanceOrProduction.getDisplayName() == ClientAttendanceOrProductionType.DEFAULT_MILE) {
			    	list.add(accounterCore);
			    } else if( employePayType ==  ClientEmployeeCompsensation.PAY_TYPE_PER_WORK && attendanceOrProduction.getDisplayName() == ClientAttendanceOrProductionType.DEFAULT_WORK) {
			    	list.add(accounterCore);
			    } 
			}  else {
				list.add(accounterCore);
			} 
		}
		return list;
	}

	public void setSelectedEmployeeOrGroup(ClientPayStructureDestination selectedValue) {
		if(this.destination == selectedValue) {
			return;
		}
		
		clear();
		this.destination = selectedValue;
		if (selectedValue instanceof ClientEmployee) {
			ClientAttendanceManagementItem item = new ClientAttendanceManagementItem();
			item.setEmployee(selectedValue.getID());
			item.setEmployeePayType(((ClientEmployee) selectedValue).getPayType());
			ClientEmployeePayRunItem parentItem = new ClientEmployeePayRunItem(null, item, null);
			List<ClientEmployeePayRunItem> childs = this.getChildRows(parentItem, item, getAttendnceList(item.getEmployeePayType()));
			parentItem.setChilds(childs);
			this.addParentRow(parentItem, childs);
		} else {
			ClientEmployeeGroup clientEmployeeGroup = (ClientEmployeeGroup) selectedValue;
			for (ClientEmployee employee : clientEmployeeGroup.getEmployees()) {
				ClientAttendanceManagementItem item = new ClientAttendanceManagementItem();
				item.setEmployee(employee.getID());
				item.setEmployeePayType(employee.getPayType());
				ClientEmployeePayRunItem parentItem = new ClientEmployeePayRunItem(null, item, null);
				List<ClientEmployeePayRunItem> childs = this.getChildRows(parentItem, item, getAttendnceList(item.getEmployeePayType()));
				parentItem.setChilds(childs);
				this.addParentRow(parentItem, childs);
			}
		}

	}
	
	private IAccounterCore getAttendnceType(long id, boolean isAttend){
		for (final IAccounterCore accounterCore : this.attendnceList) {
			if (accounterCore instanceof ClientAttendanceOrProductionType && isAttend) {
				if(((ClientAttendanceOrProductionType) accounterCore).getID() == id) {
					return accounterCore;
				}
			} else if (accounterCore instanceof ClientUserDefinedPayHead) {
				if(accounterCore.getID() == id) {
					return accounterCore;
				}
			}
		}
		return null;
	}

	private List<ClientEmployeePayRunItem> getChildRows(ClientEmployeePayRunItem parentItem2,
			ClientAttendanceManagementItem parentItem, List<?> attendnceList) {
		List<ClientEmployeePayRunItem> childs = new ArrayList<ClientEmployeePayRunItem>();
		ClientEmployeePayRunItem absentItem = new ClientEmployeePayRunItem(parentItem2, parentItem, null, null);
		childs.add(absentItem);
		for (final Object accounterCore : attendnceList) {
			if (accounterCore instanceof ClientAttendanceOrProductionType) {
				final ClientAttendanceOrProductionType attendanceOrProduction = (ClientAttendanceOrProductionType) accounterCore;
				final ClientAttendanceOrProductionItem item = new ClientAttendanceOrProductionItem();
				item.setAttendanceOrProductionType(attendanceOrProduction.getID());
				item.setClientAttendanceOrProductionType(attendanceOrProduction);
				item.setValue(UIUtils.toDbl(0));
				item.setID(attendanceOrProduction.getID());
				parentItem.getAttendanceOrProductionItems().add(item);
				childs.add(new ClientEmployeePayRunItem(parentItem2, parentItem, null, item));
			} else if (accounterCore instanceof ClientAttendanceOrProductionItem) {
				final ClientAttendanceOrProductionItem item = (ClientAttendanceOrProductionItem) accounterCore;
				IAccounterCore obj = this.getAttendnceType(item.getAttendanceOrProductionType(), true);
				if(obj != null)
					item.setClientAttendanceOrProductionType((ClientAttendanceOrProductionType) obj);
				childs.add(new ClientEmployeePayRunItem(parentItem2, parentItem, null, item));
			} else if (accounterCore instanceof ClientUserDefinedPayHead) {
				final ClientUserDefinedPayHead udPayheadItem = (ClientUserDefinedPayHead) accounterCore;
				final ClientUserDefinedPayheadItem item = new ClientUserDefinedPayheadItem();
				item.setPayHead(udPayheadItem);
				item.setID(udPayheadItem.getID());
				item.setValue(UIUtils.toDbl(0));
				parentItem.getUserDefinedPayheads().add(item);
				childs.add(new ClientEmployeePayRunItem(parentItem2, parentItem, item, null));
			} else if (accounterCore instanceof ClientUserDefinedPayheadItem) {
				final ClientUserDefinedPayheadItem item = (ClientUserDefinedPayheadItem) accounterCore;
				IAccounterCore obj = this.getAttendnceType(item.getPayHeadID(), false);
				if(obj != null)
					item.setPayHead((ClientUserDefinedPayHead) obj);
				childs.add(new ClientEmployeePayRunItem(parentItem2, parentItem, item, null));
			}
		}
		return childs;
	}

	private long getWorkingDays() {
		if (view == null) {
			return 0;
		}
		NewPayRunView payRunView = (NewPayRunView) view;
		return payRunView.getWorkingDays();
	}

	public void setView(NewPayRunView view) {
		this.view = view;
	}

	@Override
	protected void initColumns() {

		this.addColumn(new TreeColumn<>(new TextNonEditColumn<ClientEmployeePayRunItem>() {

			@Override
			protected String getColumnName() {
				return messages.employee();
			}

			@Override
			public String getValueAsString(ClientEmployeePayRunItem row) {
				return Accounter.getCompany().getEmployee(row.getParentManagItem().getEmployee()).getDisplayName();
			}

		}, new TextNonEditColumn<ClientEmployeePayRunItem>() {
			@Override
			public String getTextValue(ClientEmployeePayRunItem row) {
				return row.getDisplayName();
			}
		}));

		this.addColumn(new TreeColumn<ClientEmployeePayRunItem>(
				new AmountColumn<ClientEmployeePayRunItem>(this.currencyProvider, true) {

					@Override
					protected Double getAmount(ClientEmployeePayRunItem row) {
						row.setNoWorkingDays(getWorkingDays());
						return row.getRate();
					}

					@Override
					protected void setAmount(ClientEmployeePayRunItem row, Double value) {
						row.setRate(value);
						EmployeePayGrid.this.update(row);
					}

					@Override
					public String getValueAsString(ClientEmployeePayRunItem row) {
						return this.getAmount(row) + "";
					}

					@Override
					public int insertNewLineNumber() {
						return 1;
					}

					@Override
					protected String getColumnName() {
						return messages.rate();
					}
				}));

		this.addColumn(new TreeColumn<ClientEmployeePayRunItem>(
				new AmountColumn<ClientEmployeePayRunItem>(this.currencyProvider, true) {

					@Override
					protected Double getAmount(ClientEmployeePayRunItem row) {
						return row.getTotal();
					}

					@Override
					protected void setAmount(ClientEmployeePayRunItem row, Double value) {
						row.setTotal(value);
					}

					@Override
					public String getValueAsString(ClientEmployeePayRunItem row) {
						return this.getAmount(row) + "";
					}

					@Override
					public int insertNewLineNumber() {
						return 1;
					}

					@Override
					protected String getColumnName() {
						return messages.total();
					}

					@Override
					protected boolean isEnable() {
						return false;
					}
				}));

	}

	public List<ClientAttendanceManagementItem> getMainAllRows() {
		List<ClientAttendanceManagementItem> items = new ArrayList<ClientAttendanceManagementItem>();
		for (ClientEmployeePayRunItem parent : this.getAllParents()) {
			items.add(parent.getParentManagItem());
		}
		return items;
	}

	@Override
	protected boolean isInViewMode() {
		return false;
	}

	public void setData(List<ClientAttendanceManagementItem> attendanceItems) {
		clear();
		for (ClientAttendanceManagementItem att : attendanceItems) {
			ClientEmployeePayRunItem parentItem = new ClientEmployeePayRunItem(null, att, null);
			List<ClientEmployeePayRunItem> childs = this.getChildRows(parentItem, att,
					att.getAttendanceOrProductionItems());
			childs.addAll(this.getChildRows(parentItem, att, att.getUserDefinedPayheads()));
			parentItem.setChilds(childs);
			this.addParentRow(parentItem, childs);
		}
	}

	public void setDestination(ClientPayStructureDestination selectedValue) {
		this.destination = selectedValue;
	}
}
