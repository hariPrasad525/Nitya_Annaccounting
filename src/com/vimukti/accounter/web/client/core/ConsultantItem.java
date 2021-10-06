package com.vimukti.accounter.web.client.core;

import com.vimukti.accounter.web.client.core.reports.BaseReport;

import java.util.Set;

public class ConsultantItem extends BaseReport implements IAccounterCore,
        IAccountable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static final int TYPE_SERVICE = 1;
    public static final int TYPE_INVENTORY_PART = 2;

    public static final int TYPE_NON_INVENTORY_PART = 3;
    public static final int TYPE_INVENTORY_ASSEMBLY = 4;
    public static final int TYPE_OTHER_CHARGE = 5;
    public static final int TYPE_SUBTOTAL = 6;
    public static final int TYPE_GROUP = 7;
    public static final int TYPE_DISCOUNT = 8;
    public static final int TYPE_PAYMENT = 9;
    public static final int TYPE_SALES_TAX_ITEM = 10;
    public static final int TYPE_SALES_TAX_GROUP = 11;
    public static final int TYPE_CONSULTANT_PART = 12;

    long id;

    int type;
    String name;
    long itemGroup;
    int version;
    String currency;

    long clientItemGroup;
    boolean isActive;
    private double balance;

    Set<ClientItemStatus> itemStatuses;

    private String phoneNo;

    private String faxNo;

    protected double openingBalance = 0D;

    private Set<ClientAddress> address;

    public ConsultantItem() {
    }



    public void setItemGroup(long itemGroup) {
        this.itemGroup = itemGroup;
    }

    /**
     * @return the type
     */
    public int getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * @return the uPCorSKU
     */


    /**
     * @return the isActive
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * @param isActive the isActive to set
     */
    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    /**
     * @return the isISellThisItem
     */


    /**
     * @param id
     *            the id to set
     */

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDisplayName() {

        return name;
    }

    @Override
    public String getName() {
        return name;
    }


    public long getItemGroup() {
        return this.itemGroup;
    }

    @Override
    public AccounterCoreType getObjectType() {

        return AccounterCoreType.ITEM;
    }

    // public void setVatCode(String vatcode) {
    // this.VATCode = vatcode;
    // }
    //
    // public String getVatCode() {
    // return this.VATCode;
    // }

    @Override
    public long getID() {
        return this.id;
    }

    @Override
    public void setID(long id) {
        this.id = id;

    }

    public ConsultantItem clone() {
        ConsultantItem item = this.clone();
        return item;

    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ConsultantItem) {
            ConsultantItem item = (ConsultantItem) obj;
            if (this.getID() == item.getID())
                return true;
        }
        return false;
    }

    public double getOpeningBalance() {
        return openingBalance;
    }

    public void setOpeningBalance(double openingBalance) {
        this.openingBalance = openingBalance;
    }


    public Set<ClientAddress> getAddress() {
        return address;
    }

    public void setAddress(Set<ClientAddress> address) {
        this.address = address;
    }

    public void setClientItemGroup(long id) {
        this.clientItemGroup = id;
    }

    public long getClientItemGroup() {
        return clientItemGroup;
    }

    public int getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(int version) {
        this.version = version;
    }
}
