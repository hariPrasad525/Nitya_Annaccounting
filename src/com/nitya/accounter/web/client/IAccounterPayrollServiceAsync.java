package com.nitya.accounter.web.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.nitya.accounter.web.client.core.ClientAttendanceManagementItem;
import com.nitya.accounter.web.client.core.ClientAttendanceOrProductionType;
import com.nitya.accounter.web.client.core.ClientEmployee;
import com.nitya.accounter.web.client.core.ClientEmployeeGroup;
import com.nitya.accounter.web.client.core.ClientEmployeePayHeadComponent;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientPayHead;
import com.nitya.accounter.web.client.core.ClientPayStructure;
import com.nitya.accounter.web.client.core.ClientPayStructureDestination;
import com.nitya.accounter.web.client.core.ClientPayStructureList;
import com.nitya.accounter.web.client.core.ClientPayrollTransactionPayTax;
import com.nitya.accounter.web.client.core.ClientPayrollUnit;
import com.nitya.accounter.web.client.core.ClientTransactionPayEmployee;
import com.nitya.accounter.web.client.core.ConsultantsDetailsList;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.core.PaginationList;
import com.nitya.accounter.web.client.core.reports.PaySheet;
import com.nitya.accounter.web.client.core.reports.PaySlipDetail;
import com.nitya.accounter.web.client.core.reports.PaySlipSummary;
import com.nitya.accounter.web.client.exception.AccounterException;

public interface IAccounterPayrollServiceAsync {

	public void getEmployees(boolean isActive, int start, int length,
			AsyncCallback<PaginationList<ClientEmployee>> callBack);
	
	public void getconsultantDetails(boolean isActive, int start, int lenght,
			AsyncCallback<PaginationList<ConsultantsDetailsList>> callBack)
			throws AccounterException;

	public void getPayheads(int start, int length,
			AsyncCallback<PaginationList<ClientPayHead>> callBack);

	public void getPayrollUnitsList(int start, int length,
			AsyncCallback<PaginationList<ClientPayrollUnit>> callBack);

	public void getEmployeePayHeadComponents(
			List<ClientAttendanceManagementItem> list,
			ClientPayStructureDestination selectItem,
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			Long noOfWorkingDays,
			AsyncCallback<ArrayList<ClientEmployeePayHeadComponent>> callback);

	public void getPayStructures(int start, int length,
			AsyncCallback<PaginationList<ClientPayStructureList>> callback);
	
	public void getAttendanceProductionTypes(
			int start,
			int length,
			AsyncCallback<PaginationList<ClientAttendanceOrProductionType>> asyncCallback);
	

	public void getAttendanceProductionOrUserDefined(
			int start,
			int length,
			AsyncCallback<PaginationList<IAccounterCore>> asyncCallback);

	public void getEmployeeGroups(
			AsyncCallback<ArrayList<ClientEmployeeGroup>> asyncCallback);

	public void getEmployeesAndGroups(
			AsyncCallback<List<ClientPayStructureDestination>> asyncCallback);

	public void getEmployeeAttendanceCurrentBal(long employee,
			long attendanceType, AsyncCallback<Long> asyncCallback);

	void getEmployeesByGroup(ClientPayStructureDestination employeeGroup,
			AsyncCallback<ArrayList<ClientEmployee>> asyncCallback);

	public void getPaySlipSummary(ClientFinanceDate start,
			ClientFinanceDate clientFinanceDate,
			AsyncCallback<ArrayList<PaySlipSummary>> asyncCallback);

	public void getPaySheet(ClientFinanceDate start, ClientFinanceDate end,
			AsyncCallback<ArrayList<PaySheet>> asyncCallback);

	void getPaySlipDetail(long employeeId, ClientFinanceDate start,
			ClientFinanceDate end,
			AsyncCallback<ArrayList<PaySlipDetail>> asyncCallback);

	public void getPayStructure(ClientPayStructureDestination selectItem,
			AsyncCallback<ClientPayStructure> asyncCallback);

	void getTransactionPayEmployeeList(
			ClientPayStructureDestination structureDestination,
			AsyncCallback<List<ClientTransactionPayEmployee>> callback);

	void getTransactionPayTaxList(
			ClientPayStructureDestination structureDestination,
			AsyncCallback<List<ClientPayrollTransactionPayTax>> callback);
}
