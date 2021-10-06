package com.vimukti.accounter.web.client.ui.core;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientAttendanceOrProductionType;
import com.vimukti.accounter.web.client.core.ClientEmployeeGroup;
import com.vimukti.accounter.web.client.core.ClientPayrollUnit;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.AbstractBaseView;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.HistoryTokens;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.payroll.AttendanceManagementView;
import com.vimukti.accounter.web.client.ui.payroll.AttendanceOrProductionTypeListView;
import com.vimukti.accounter.web.client.ui.payroll.EmployeeGroupListDialog;
import com.vimukti.accounter.web.client.ui.payroll.EmployeeListView;
import com.vimukti.accounter.web.client.ui.payroll.NewClientAttendanceOrProductionDialog;
import com.vimukti.accounter.web.client.ui.payroll.NewEmployeeGroupDialog;
import com.vimukti.accounter.web.client.ui.payroll.NewEmployeeView;
import com.vimukti.accounter.web.client.ui.payroll.NewPayHeadView;
import com.vimukti.accounter.web.client.ui.payroll.NewPayRunView;
import com.vimukti.accounter.web.client.ui.payroll.NewPayStructureView;
import com.vimukti.accounter.web.client.ui.payroll.NewPayrollUnitDialog;
import com.vimukti.accounter.web.client.ui.payroll.PayEmployeeView;
import com.vimukti.accounter.web.client.ui.payroll.PayStructureListView;
import com.vimukti.accounter.web.client.ui.payroll.PayTaxView;
import com.vimukti.accounter.web.client.ui.payroll.PayheadListView;
import com.vimukti.accounter.web.client.ui.payroll.PayrollUnitListView;
import com.vimukti.accounter.web.client.ui.react.ReactBaseView;
import com.vimukti.accounter.web.client.ui.vendors.ManageSupportListView;

public class PayRollActions extends Action {

	public static final int NEW_EMPLOEE = 1;
	public static final int EMPLOYEE_LIST = 2;
	public static final int NEW_PAY_HEAD = 3;
	public static final int PAY_HEAD_LIST = 4;
	public static final int NEW_PAY_STRUCTURE = 5;
	public static final int PAY_STRUCTURE_LIST = 6;
	public static final int NEW_PAYROLL_UNIT = 7;
	public static final int PAY_ROLL_UNIT_LIST = 8;
	public static final int NEW_PAY_RUN = 9;
	public static final int NEW_EMP_GROUP = 10;
	public static final int EMP_GROUP_LIST = 11;
	private static final int ATTENDANCE_OR_PRODUCTION_TYPE = 12;
	private static final int ATTENDANCE_OR_PRODUCTION_TYPE_LIST = 13;
	private static final int NEW_PAY_EMPLOYEE = 14;
	private static final int NEW_PAY_TAX = 16;

	public static final int ATTENDANCE_MANAGEMENT = 15;
	private int type;
	private boolean fromEmployeeView;

	public PayRollActions(int type) {
		super();
		this.setType(type);
		this.catagory = messages.payroll();
	}

	@Override
	public String getText() {
		switch (getType()) {
		case NEW_EMPLOEE:
			return messages.newEmployee();
		case EMPLOYEE_LIST:
			return messages.employeeList();
		case NEW_PAY_HEAD:
			return messages.newPayHead();
		case PAY_HEAD_LIST:
			return messages.payheadList();
		case NEW_PAY_STRUCTURE:
			return messages.newPayee(messages.payStructure());
		case PAY_STRUCTURE_LIST:
			return messages.payStructureList();
		case NEW_PAY_RUN:
			return messages.newPayee(messages.payrun());
		case NEW_EMP_GROUP:
			return messages.employeeGroup();
		case EMP_GROUP_LIST:
			return messages.employeeGroupList();
		case NEW_PAYROLL_UNIT:
			return messages.newPayrollUnit();
		case PAY_ROLL_UNIT_LIST:
			return messages.payrollUnitList();
		case ATTENDANCE_OR_PRODUCTION_TYPE:
			return messages.attendanceOrProductionType();
		case ATTENDANCE_OR_PRODUCTION_TYPE_LIST:
			return messages.attendanceOrProductionTypeList();
		case NEW_PAY_EMPLOYEE:
			return messages.payEmployee();
		case NEW_PAY_TAX:
			return messages.payTax();	
		default:
			break;
		}
		return null;
	}

	@Override
	public void run() {
		GWT.runAsync(new RunAsyncCallback() {

			@Override
			public void onSuccess() {
				PayRollActions.this.showNewView();
			}

			@Override
			public void onFailure(Throwable reason) {
				Accounter.showError(Global.get().messages()
						.unableToshowtheview());
			}
		});
	}

	protected void showNewView() {
		AbstractBaseView<? extends IAccounterCore> view = null;
		BaseDialog<? extends IAccounterCore> dialog = null;
		switch (getType()) {
		case NEW_EMPLOEE:
			view = new NewEmployeeView();
			break;
		case EMPLOYEE_LIST:
			view = new EmployeeListView();
			break;
		case NEW_PAY_HEAD:
			view = new NewPayHeadView();
			break;
		case PAY_HEAD_LIST:
			view = new PayheadListView();
			break;
		case ATTENDANCE_MANAGEMENT:
			view = new AttendanceManagementView();
			break;
		case NEW_PAY_STRUCTURE:
			view = new NewPayStructureView();
			break;
		case PAY_STRUCTURE_LIST:
			view = new PayStructureListView();
			break;
		case NEW_PAY_RUN:
			view = new NewPayRunView();
			break;
		case NEW_EMP_GROUP:
			dialog = new NewEmployeeGroupDialog(messages.newEmployeeGroup(),
					(ClientEmployeeGroup) data, isFromEmployeeView());
			break;
		case EMP_GROUP_LIST:
			view = new ManageSupportListView(IAccounterCore.EMPLOYEE_GROUP);
			break;
		case NEW_PAYROLL_UNIT:
			NewPayrollUnitDialog newPayrollUnitDialog = new NewPayrollUnitDialog(
					messages.newPayrollUnit(), isFromEmployeeView());
			newPayrollUnitDialog.setData((ClientPayrollUnit) data);
			dialog = newPayrollUnitDialog;
			break;
		case PAY_ROLL_UNIT_LIST:
			view = new PayrollUnitListView();
			break;
		case ATTENDANCE_OR_PRODUCTION_TYPE:
			dialog = new NewClientAttendanceOrProductionDialog(
					messages.attendanceOrProductionType(),
					(ClientAttendanceOrProductionType) data);
			break;
		case ATTENDANCE_OR_PRODUCTION_TYPE_LIST:
			view = new AttendanceOrProductionTypeListView();
			break;
		case NEW_PAY_EMPLOYEE:
			view = new PayEmployeeView();
			break;
		case NEW_PAY_TAX:
			view = new PayTaxView();
			/*
			 * view = new ReactBaseView<IAccounterCore>() { };
			 */
			break;	
		default:
			break;
		}

		if (view != null) {
			MainFinanceWindow.getViewManager().showView(view, data,
					isDependent, PayRollActions.this);
		}

		if (dialog != null) {
			ViewManager.getInstance().showDialog(dialog);
			dialog.setCallback(getCallback());
		}
	}

	@Override
	public ImageResource getBigImage() {
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		return null;
	}

	@Override
	public String getHistoryToken() {
		switch (getType()) {
		case NEW_EMPLOEE:
			return HistoryTokens.NEWEMPLOYEE;
		case EMPLOYEE_LIST:
			return HistoryTokens.EMPLOYEELIST;
		case NEW_PAY_HEAD:
			return HistoryTokens.NEWPAYHEAD;
		case PAY_HEAD_LIST:
			return HistoryTokens.PAYHEADLIST;
		case NEW_PAY_STRUCTURE:
			return HistoryTokens.NEW_PAYSTRUCTURE;
		case PAY_STRUCTURE_LIST:
			return HistoryTokens.PAY_STRUCTURE_LIST;
		case NEW_PAY_RUN:
			return HistoryTokens.NEW_PAYRUN;
		case NEW_EMP_GROUP:
			return HistoryTokens.NEWEMPLOYEEGROUP;
		case EMP_GROUP_LIST:
			return HistoryTokens.EMPLOYEEGROUPLIST;
		case NEW_PAYROLL_UNIT:
			return HistoryTokens.NEWPAYROLLUNIT;
		case PAY_ROLL_UNIT_LIST:
			return HistoryTokens.PAYROLLUNITLIST;
		case ATTENDANCE_OR_PRODUCTION_TYPE:
			return HistoryTokens.ATTENDANCE_PRODUCTION_TYPE;
		case ATTENDANCE_OR_PRODUCTION_TYPE_LIST:
			return HistoryTokens.ATTENDANCE_PRODUCTION_TYPE_LIST;
		case NEW_PAY_EMPLOYEE:
			return HistoryTokens.PAY_EMPLOYEE;
		case NEW_PAY_TAX:
			return HistoryTokens.PAY_TAX;	
		case ATTENDANCE_MANAGEMENT:
			return HistoryTokens.ATTENDANCE_MANAGEMENT;
		default:
			break;
		}
		return null;
	}

	@Override
	public String getHelpToken() {
		switch (getType()) {
		case NEW_EMPLOEE:
			return "new-employee";
		case EMPLOYEE_LIST:
			return "employee-list";
		case NEW_PAY_HEAD:
			return "new-payhead";
		case PAY_HEAD_LIST:
			return "payhead-list";
		case NEW_PAY_STRUCTURE:
			return "new_pay_structure";
		case PAY_STRUCTURE_LIST:
			return "paystructure-list";
		case NEW_PAY_RUN:
			return "new_payrun";
		case NEW_EMP_GROUP:
			return "new-employee-group";
		case EMP_GROUP_LIST:
			return "employee-group-list";
		case NEW_PAYROLL_UNIT:
			return "new-payroll-unit";
		case PAY_ROLL_UNIT_LIST:
			return "payroll-unit-list";
		case ATTENDANCE_OR_PRODUCTION_TYPE:
			return "attendance-or-productiontype";
		case ATTENDANCE_OR_PRODUCTION_TYPE_LIST:
			return "attendance_or_production_typelist";
		case NEW_PAY_EMPLOYEE:
			return "pay-employee";
		case NEW_PAY_TAX:
			return "pay-tax";	
		default:
			break;
		}
		return null;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public static PayRollActions newPayEmployeeAction() {
		return new PayRollActions(NEW_PAY_EMPLOYEE);
	}
	
	public static PayRollActions newPayTaxAction() {
		return new PayRollActions(NEW_PAY_TAX);
	}

	public static PayRollActions newEmployeeAction() {
		return new PayRollActions(NEW_EMPLOEE);
	}

	public static PayRollActions employeeListAction() {
		return new PayRollActions(EMPLOYEE_LIST);
	}

	public static PayRollActions newEmployeeGroupAction() {
		return new PayRollActions(NEW_EMP_GROUP);
	}

	public static PayRollActions employeeGroupListAction() {
		return new PayRollActions(EMP_GROUP_LIST);
	}

	public static PayRollActions newPayStructureAction() {
		return new PayRollActions(NEW_PAY_STRUCTURE);
	}

	public static PayRollActions payStructureListAction() {
		return new PayRollActions(PAY_STRUCTURE_LIST);
	}

	public static PayRollActions newPayRunAction() {
		return new PayRollActions(NEW_PAY_RUN);
	}

	public static PayRollActions newPayRollUnitAction() {
		return new PayRollActions(NEW_PAYROLL_UNIT);
	}

	public static PayRollActions payRollUnitListAction() {
		return new PayRollActions(PAY_ROLL_UNIT_LIST);
	}

	public static PayRollActions newPayHeadAction() {
		return new PayRollActions(NEW_PAY_HEAD);
	}

	public static PayRollActions payHeadListAction() {
		return new PayRollActions(PAY_HEAD_LIST);
	}

	public static PayRollActions newAttendanceProductionTypeAction() {
		return new PayRollActions(ATTENDANCE_OR_PRODUCTION_TYPE);
	}

	public static PayRollActions attendanceProductionTypeList() {
		return new PayRollActions(ATTENDANCE_OR_PRODUCTION_TYPE_LIST);
	}

	public void setFromEmployeeView(boolean fromEmployeeView) {
		this.fromEmployeeView = fromEmployeeView;
	}

	public boolean isFromEmployeeView() {
		return fromEmployeeView;
	}
}
