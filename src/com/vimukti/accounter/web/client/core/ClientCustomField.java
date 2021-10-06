package com.vimukti.accounter.web.client.core;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class ClientCustomField implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name;

	private boolean showCustomer;
	private boolean showVendor;
	private boolean showEmployee;
	private long id;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isShowCustomer() {
		return showCustomer;
	}

	public void setShowCustomer(boolean shoeCustomer) {
		this.showCustomer = shoeCustomer;
	}

	public boolean isShowVendor() {
		return showVendor;
	}

	public void setShowVendor(boolean showVendor) {
		this.showVendor = showVendor;
	}

	@Override
	public int getVersion() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setVersion(int version) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getDisplayName() {

		return "ClientCustomField";
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.CUSTOMFIELD;
	}

	@Override
	public void setID(long id) {
		this.id = id;
	}

	@Override
	public long getID() {

		return this.id;
	}

	public boolean isShowEmployee() {
		return showEmployee;
	}

	public void setShowEmployee(boolean showEmployee) {
		this.showEmployee = showEmployee;
	}

}
