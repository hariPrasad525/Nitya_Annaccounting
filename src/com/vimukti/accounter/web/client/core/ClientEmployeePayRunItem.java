package com.vimukti.accounter.web.client.core;

import java.util.Arrays;
import java.util.List;

import com.vimukti.accounter.web.client.ui.Accounter;

public class ClientEmployeePayRunItem implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	ClientAttendanceManagementItem parentManageItem;
	
	ClientEmployeePayRunItem parentItem;
	
	long noWorkingDays = 0;
	

	public ClientEmployeePayRunItem getParentItem() {
		return parentItem;
	}

	public void setParentItem(ClientEmployeePayRunItem parentItem) {
		this.parentItem = parentItem;
	}

	List<ClientEmployeePayRunItem> childs;

	ClientUserDefinedPayheadItem userDefinedpayHead;

	ClientAttendanceOrProductionItem attentPayHead;

	public ClientEmployeePayRunItem(ClientEmployeePayRunItem parentItem, ClientAttendanceManagementItem parentManageItem, List<ClientEmployeePayRunItem> childs) {
		this.parentItem = parentItem;
		this.childs = childs;
		this.parentManageItem = parentManageItem;
	}

	public ClientEmployeePayRunItem(ClientEmployeePayRunItem parentItem, ClientAttendanceManagementItem parentManageItem,
			ClientUserDefinedPayheadItem userDefinedpayHead, ClientAttendanceOrProductionItem attentPayHead) {
		this.parentItem = parentItem;
		this.attentPayHead = attentPayHead;
		this.userDefinedpayHead = userDefinedpayHead;
		this.parentManageItem = parentManageItem;
	}

	public void setRate(double amount) {
		if (attentPayHead != null)
			this.attentPayHead.setValue(amount);
		else if (userDefinedpayHead != null)
			this.userDefinedpayHead.setValue(amount);
		else if (parentManageItem != null && this.childs == null)
			this.parentManageItem.setAbscentDays(amount);
	}

	public double getRate() {
		if (attentPayHead != null) {
			if (this.getAttentPayHead() != null
					&& this.getAttentPayHead().getClientAttendanceOrProductionType()
							.getPeriodType() == ClientPayHead.CALCULATION_PERIOD_HOURS) {
				if (Arrays
						.asList(ClientEmployeeCompsensation.PAY_TYPE_ANNUAL,
								ClientEmployeeCompsensation.PAY_TYPE_HOURLY,
								ClientEmployeeCompsensation.PAY_TYPE_DAILY)
						.contains(this.getParentManagItem().getEmployeePayType())) {
					return (double) (this.getNoWorkingDays() * 8);
				}
			}
			return this.attentPayHead.getValue();
		}
		else if (userDefinedpayHead != null)
			return this.userDefinedpayHead.getValue();
		else if (parentManageItem != null && this.childs != null)
			return Accounter.getCompany().getEmployee(this.parentManageItem.getEmployee()).getSalary();
		else if (parentManageItem != null && this.childs == null)
			return this.parentManageItem.getAbscentDays();
		return 0;
	}

	public double getTotal() {
		double salary = Accounter.getCompany().getEmployee(this.parentManageItem.getEmployee()).getSalary();
		if (attentPayHead != null)
			return this.attentPayHead.getValue() * salary;
		else if (userDefinedpayHead != null)
			return this.userDefinedpayHead.getValue();
		else if (parentManageItem != null && this.childs == null) {
			return -(this.parentManageItem.getAbscentDays() * salary);
		}
		else if (this.parentItem == null && parentManageItem != null && this.childs != null) {
			double total = 0.0;
			for (final ClientEmployeePayRunItem child : childs) {
				total += child.getTotal();
			}
			return total;
		}
		return 0.0;
	}

	public void setTotal(double amount) {
		if (attentPayHead != null)
			this.attentPayHead.setValue(amount);
		else if (userDefinedpayHead != null)
			this.userDefinedpayHead.setValue(amount);
		else if (this.parentItem != null && parentManageItem != null && this.childs != null)
			this.parentManageItem.setAbscentDays(amount);
	}

	public ClientAttendanceManagementItem getParentManagItem() {
		return parentManageItem;
	}

	public void setParentItem(ClientAttendanceManagementItem parentManageItem) {
		this.parentManageItem = parentManageItem;
	}

	public ClientUserDefinedPayheadItem getUserDefinedpayHead() {
		return userDefinedpayHead;
	}

	public void setUserDefinedpayHead(ClientUserDefinedPayheadItem userDefinedpayHead) {
		this.userDefinedpayHead = userDefinedpayHead;
	}

	public ClientAttendanceOrProductionItem getAttentPayHead() {
		return attentPayHead;
	}

	public void setAttentPayHead(ClientAttendanceOrProductionItem attentPayHead) {
		this.attentPayHead = attentPayHead;
	}

	public List<ClientEmployeePayRunItem> getChilds() {
		return childs;
	}

	public void setChilds(List<ClientEmployeePayRunItem> childs) {
		this.childs = childs;
	}


	@Override
	public int getVersion() {
		if (attentPayHead != null)
			return this.attentPayHead.getVersion();
		else if (userDefinedpayHead != null)
			return this.userDefinedpayHead.getVersion();
		else if (parentItem != null)
			return this.parentItem.getVersion();
		return 0;
	}

	@Override
	public void setVersion(int version) {
		if (attentPayHead != null)
			this.attentPayHead.setVersion(version);
		else if (userDefinedpayHead != null)
			this.userDefinedpayHead.setVersion(0);
		else if (parentItem != null)
			this.parentItem.setVersion(0);
	}

	@Override
	public String getName() {
		if (attentPayHead != null)
			this.attentPayHead.getName();
		else if (userDefinedpayHead != null)
			this.userDefinedpayHead.getName();
		else if (parentManageItem != null && this.childs == null)
			this.parentItem.getName();
		return "";
	}

	@Override
	public String getDisplayName() {
		if (attentPayHead != null)
			return this.attentPayHead.getClientAttendanceOrProductionType().getName();
		else if (userDefinedpayHead != null)
			return this.userDefinedpayHead.getPayHead().getName();
		else if (parentManageItem != null && this.childs == null)
				return Accounter.getMessages().abscent();
		return "";
	}

	@Override
	public AccounterCoreType getObjectType() {
		if (attentPayHead != null)
			this.attentPayHead.getObjectType();
		else if (userDefinedpayHead != null)
			this.userDefinedpayHead.getObjectType();
		else if (parentItem != null)
			this.parentItem.getObjectType();
		return null;
	}

	@Override
	public void setID(long id) {
		if (attentPayHead != null)
			this.attentPayHead.setID(id);
		else if (userDefinedpayHead != null)
			this.userDefinedpayHead.setID(id);
		else if (parentItem != null)
			this.parentItem.setID(id);
	}

	@Override
	public long getID() {
		if (attentPayHead != null)
			this.attentPayHead.getID();
		else if (userDefinedpayHead != null)
			this.userDefinedpayHead.getID();
		else if (parentItem != null)
			this.parentItem.getID();
		return 0;
	}
	
	public long getNoWorkingDays() {
		return noWorkingDays;
	}

	public void setNoWorkingDays(long noWorkingDays) {
		this.noWorkingDays = noWorkingDays;
	}

}
