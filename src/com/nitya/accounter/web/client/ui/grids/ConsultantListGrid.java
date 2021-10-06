package com.nitya.accounter.web.client.ui.grids;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.CheckBox;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.ClientCurrency;
import com.nitya.accounter.web.client.core.ConsultantsDetailsList;
import com.nitya.accounter.web.client.core.Utility;
import com.nitya.accounter.web.client.ui.DataUtils;
import com.nitya.accounter.web.client.ui.ItemListView;
import com.nitya.accounter.web.client.ui.customers.NewConsultantCenterViewAction;

public class ConsultantListGrid extends BaseListGrid<ConsultantsDetailsList> {

    private ClientCurrency currency = getCompany().getPrimaryCurrency();
    private boolean isGeneratedFromCus;
    private int subType;
    private ConsultantSelectionListener consultantSelectionListener;
    private ConsultantsDetailsList selectedItem;

    public ConsultantListGrid(boolean isMultiSelectionEnable) {
        super(isMultiSelectionEnable);
        this.getElement().setId("ItemsListGrid");
    }

    public ConsultantListGrid(boolean isMultiSelectionEnable,
                              boolean isGeneratedFromCus) {
        this(isMultiSelectionEnable);
        this.isGeneratedFromCus = isGeneratedFromCus;
    }

    @Override
    protected int[] setColTypes() {
        return new int[]{
                ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_DECIMAL_TEXT};
    }

    @Override
    protected int getCellWidth(int index) {
        if (index == 0)
            return 120;
        else if (index == 1)
            return 80;

        return -1;
    }

    ;

    protected void updateTotal(ConsultantsDetailsList clientItem, boolean add) {

        if (add) {
            if (clientItem.isActive())
                total += clientItem.getAmount();
            else
                total += clientItem.getAmount();
        } else
            total -= clientItem.getAmount();
    }

    @Override
    public Double getTotal() {
        return total;
    }

    @Override
    public void setTotal() {
        this.total = 0.0D;
    }

    @Override
    protected Object getColumnValue(ConsultantsDetailsList obj, int col) {
        switch (col) {
            case 0:
                return obj.getName() != null ? getspaces(obj) : "";
            case 1:
                return DataUtils.amountAsStringWithCurrency(
                        obj.getAmount(), currency) != null ? DataUtils
                        .amountAsStringWithCurrency(obj.getAmount(),
                                currency) : "";
            default:
                return null;
        }
    }

    /**
     * @param obj
     * @return
     */
    private String getspaces(ConsultantsDetailsList obj) {
        // Element element = this.getCellByWidget(view.getGrid().getWidget(1))
        // .getElement();
        // Element element = view.getGrid().cellFormatter.getElement(view
        // .getGrid().getCurrentRow(), view.getGrid().getCurrentColumn());
        //int depth = obj.getDepth();
        // // for adding space
        // element.addClassName("depth" + depth);
        //cellFormatter.addStyleName(currentRow, currentCol, "depth" + depth);
        return obj.getName();
    }


    @Override
    protected String[] getColumns() {

        return new String[]{messages.name(), messages.balance()};

    }


    @Override
    public void onDoubleClick(ConsultantsDetailsList obj) {

    }


    protected void executeDelete(final ConsultantsDetailsList recordToBeDeleted) {
    }

    @Override
    protected int sort(ConsultantsDetailsList item1, ConsultantsDetailsList item2, int index) {
        if (ItemListView.isPurchaseType && ItemListView.isSalesType) {
            if (index == 1) {
                Double price1 = item1.getAmount();
                Double price2 = item2.getAmount();
                return price1.compareTo(price2);
            }
        }
        return 0;
    }


    @Override
    public void addData(ConsultantsDetailsList obj) {
        super.addData(obj);
    }

    @Override
    public void headerCellClicked(int colIndex) {
        super.headerCellClicked(colIndex);
        for (int i = 0; i < this.getTableRowCount(); i++) {
            ((CheckBox) this.getWidget(i, 0)).setEnabled(false);
        }
    }

    @Override
    protected String[] setHeaderStyle() {
        return new String[]{"consultantName", "balance"};
    }

    @Override
    protected String[] setRowElementsStyle() {
        return new String[]{"consultantName-value", "balance-value"};
    }

    public void setConsultantSelectionListener(
            ConsultantSelectionListener customerSelectionListener) {
        this.consultantSelectionListener = customerSelectionListener;
        
    }

    @Override
    protected void onClick(ConsultantsDetailsList obj, int row, int col) {
   
    	if (!Utility.isUserHavePermissions(AccounterCoreType.ITEM)) {
    		return;
        }
        setSelectedItem(obj);
        consultantSelectionListener.consultantSelected();

    }

    public ConsultantsDetailsList getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(ConsultantsDetailsList selectedItem) {
        this.selectedItem = selectedItem;
       
//        ActionFactory.getNewConsultantDetails().run(selectedItem, false);
    }


}
