package com.nitya.accounter.web.client.ui.customers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientItem;
import com.nitya.accounter.web.client.core.ClientQuantity;
import com.nitya.accounter.web.client.core.ClientRewaHrTimeSheet;
import com.nitya.accounter.web.client.core.ClientTransactionItem;
import com.nitya.accounter.web.client.core.ConsultantsDetailsList;
import com.nitya.accounter.web.client.core.Features;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.core.PaginationList;
import com.nitya.accounter.web.client.core.Lists.PayeeList;
import com.nitya.accounter.web.client.core.reports.TransactionHistory;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.ImageButton;
import com.nitya.accounter.web.client.ui.ItemView;
import com.nitya.accounter.web.client.ui.MainFinanceWindow;
import com.nitya.accounter.web.client.ui.RewaTimeSheetDialog;
import com.nitya.accounter.web.client.ui.StyledPanel;
import com.nitya.accounter.web.client.ui.UIUtils;
import com.nitya.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.nitya.accounter.web.client.ui.combo.SelectCombo;
import com.nitya.accounter.web.client.ui.company.NewConsultAction;
import com.nitya.accounter.web.client.ui.core.ActionFactory;
import com.nitya.accounter.web.client.ui.core.ButtonGroup;
import com.nitya.accounter.web.client.ui.core.IButtonContainer;
import com.nitya.accounter.web.client.ui.core.IPrintableView;
import com.nitya.accounter.web.client.ui.core.ViewManager;
import com.nitya.accounter.web.client.ui.forms.DynamicForm;
import com.nitya.accounter.web.client.ui.grids.BaseListGrid;
import com.nitya.accounter.web.client.ui.grids.ConsultantListGrid;
import com.nitya.accounter.web.client.ui.grids.ConsultantSelectionListener;
import com.nitya.accounter.web.client.ui.grids.ConsultantTransactionsHistoryGrid;

/*
 * @modified by Ravinder Rangamgari
 *
 *
 */

public class ConsultantCenterView<T> extends
        AbstractPayeeCenterView<ConsultantsDetailsList> implements IPrintableView,
        IButtonContainer {
    private static final int TYPE_ESTIMATE = 7;
    private static final int TYPE_INVOICE = 8;
    private static final int TYPE_CAHSSALE = 1;
    private static final int TYPE_RECEIVE_PAYMENT = 12;
    private static final int TYPE_CREDITNOTE = 4;
    private static final int TYPE_CUSTOMER_REFUND = 5;
    private static final int TYPE_ALL_TRANSACTION = 100;
    private static final int TYPE_WRITE_CHECK = 15;
    private static final int TYPE_SALES_ORDER = 38;
    private ConsultantsDetailsList selectedItem;
    private List<PayeeList> listOfCustomers;
//    private List<TransactionHistory> records;
    protected BaseListGrid grid;
    private ConsultantDetailsPanel detailsPanel;
    private ConsultantListGrid itemsListGrid;
    private SelectCombo activeInActiveSelect, trasactionViewSelect,
            trasactionViewTypeSelect;
    private StyledPanel transactionGridpanel;
    private ConsultantTransactionsHistoryGrid custHistoryGrid;
    private Map<Integer, String> transactiontypebyStatusMap;
    private boolean isActiveAccounts = true;
    private StyledPanel deleteButtonPanel;
    private Button transactionButton;
    private StyledPanel rightVpPanel, dummyPanel;
    ClientTransactionItem consultantSelected = new ClientTransactionItem();

    public ConsultantCenterView() {
    	  super.init();
    	  
    	 
    }

    @Override
    public void onEdit() {
        NewConsultAction newConsultantAction = new NewConsultAction(false);
        newConsultantAction.setisItemEditable(true);
        ClientItem item = getCompany().getItem(this.selectedItem.getId());
        item.setEmail(this.selectedItem.getEmail());
        item.setConsultantAddress(this.selectedItem.getAddresss());
        item.setPhone(this.selectedItem.getPhone());
        item.setClientItemGroup(item.getClientItemGroup());
        newConsultantAction.setType(ClientItem.TYPE_SERVICE);
        newConsultantAction.setSubType(ItemView.TYPE_CONSULTANT_PART);
        newConsultantAction.run(item, false);
    }

    @Override
    public void init() {
        super.init();
        this.getElement().setId("CustomerCenterView");
        creatControls();

    }

    private void creatControls() {

        StyledPanel mainPanel = new StyledPanel("customerCenter");

        StyledPanel leftVpPanel = new StyledPanel("leftPanel");

        viewTypeCombo();
        DynamicForm viewform = new DynamicForm("viewform");
        viewform.setStyleName("filterPanel");
        viewform.add(activeInActiveSelect);
        leftVpPanel.add(viewform);
        itemsListGrid = new ConsultantListGrid(false);
        itemsListGrid.init();
        
        initItemsListGrid();
        leftVpPanel.add(itemsListGrid);
        itemsListGrid.setStyleName("cusotmerCentrGrid");

        rightVpPanel = new StyledPanel("rightPanel");
        dummyPanel = new StyledPanel("dummyPanel");

        detailsPanel = new ConsultantDetailsPanel(selectedItem);
        rightVpPanel.add(detailsPanel);
        //itemsListGrid.setSelection(ConsultantsDetailsList);
        itemsListGrid.setConsultantSelectionListener(new ConsultantSelectionListener() {
            @Override
            public void consultantSelected() {
                OncusotmerSelected();
            }
        });
        transactionViewSelectCombo();
        transactionViewTypeSelectCombo();
        transactionDateRangeSelector();
        DynamicForm transactionViewform = new DynamicForm("transactionViewform");

        transactionViewform.add(trasactionViewSelect, trasactionViewTypeSelect,
                dateRangeSelector);

        transactionGridpanel = new StyledPanel("transactionGridpanel");
        transactionGridpanel.add(transactionViewform);
        custHistoryGrid = new ConsultantTransactionsHistoryGrid() {
            @Override
            public void initListData() {
                OncusotmerSelected();
            }
        };
        custHistoryGrid.init();
        custHistoryGrid.addEmptyMessage(messages.pleaseSelectAnyPayee(Global
                .get().Customer()));
        int pageSize = getPageSize();
        
        itemsListGrid.addRangeChangeHandler2(new RangeChangeEvent.Handler() {

            @Override
            public void onRangeChange(RangeChangeEvent event) {
                onPageChange(event.getNewRange().getStart(), event
                        .getNewRange().getLength());
            }
        });
        
        custHistoryGrid.addRangeChangeHandler2(new RangeChangeEvent.Handler() {

            @Override
            public void onRangeChange(RangeChangeEvent event) {
                onPageChange(event.getNewRange().getStart(), event
                        .getNewRange().getLength());
            }
        });
        SimplePager pager = new SimplePager(SimplePager.TextLocation.CENTER,
                (SimplePager.Resources) GWT.create(SimplePager.Resources.class), false, pageSize * 2,
                true);
        pager.setDisplay(custHistoryGrid);
        updateRecordsCount(0, 0, 0);
        if (Accounter.isIpadApp()) {

            transactionButton = new Button(messages.transaction());
            transactionButton.addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    /*ActionFactory.getCustomerTransactionListHistory(
                            selectedItem).run();*/
                }
            });

        } else {
            dummyPanel.add(transactionGridpanel);
            dummyPanel.add(custHistoryGrid);
            dummyPanel.add(pager);
        }
        rightVpPanel.add(dummyPanel);
        Label labelTitle = new Label(messages.customerCentre(Global.get()
                .consultants()));
        labelTitle.setStyleName("label-title");
        mainPanel.add(leftVpPanel);
        mainPanel.add(rightVpPanel);
        deleteButtonPanel = new StyledPanel("deleteButtonPanel");
        add(labelTitle);
        add(deleteButtonPanel);
        add(mainPanel);

    }

    public void updateRecordsCount(int start, int length, int total) {
        custHistoryGrid.updateRange(new Range(start, getPageSize()));
        custHistoryGrid.setRowCount(total, (start + length) == total);
    }
 
    public void updateRecordsCountInItems(int start, int length, int total) {
    	itemsListGrid.updateRange(new Range(start, getPageSize()));
    	itemsListGrid.setRowCount(total, (start + length) == total);
    }
    
    private void viewTypeCombo() {
        if (activeInActiveSelect == null) {
            activeInActiveSelect = new SelectCombo(messages.show());

            List<String> activetypeList = new ArrayList<String>();
            activetypeList.add(messages.active());
            activetypeList.add(messages.inActive());
            activeInActiveSelect.initCombo(activetypeList);
            activeInActiveSelect.setComboItem(messages.active());
            activeInActiveSelect
                    .addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

                        @Override
                        public void selectedComboBoxItem(String selectItem) {
                            if (activeInActiveSelect.getSelectedValue() != null) {
                                if (activeInActiveSelect.getSelectedValue()
                                        .toString()
                                        .equalsIgnoreCase(messages.active())) {
                                    refreshActiveinActiveList(true);
                                } else {
                                    refreshActiveinActiveList(false);

                                }

                            }
                        }

                    });
        }
    }

    private void refreshActiveinActiveList(boolean isActivelist) {
        itemsListGrid.setSelection(null);
        detailsPanel.custname.setText(messages.noPayeeSelected(Global.get().Customer()));
        this.selectedItem = null;
        OncusotmerSelected();
        isActiveAccounts = isActivelist;
        initItemsListGrid();
    }

    private void transactionViewSelectCombo() {
        if (trasactionViewSelect == null) {
            trasactionViewSelect = new SelectCombo(messages.currentView());

            List<String> transactionTypeList = new ArrayList<String>();
            transactionTypeList.add(messages.allTransactions());
            transactionTypeList.add(messages.invoices());
            if (getPreferences().isDoyouwantEstimates()) {
                transactionTypeList.add(messages.quotes());
            }
            if (getCompany().getPreferences().isDelayedchargesEnabled()) {
                transactionTypeList.add(messages.Charges());
                transactionTypeList.add(messages.credits());
            }
            transactionTypeList.add(messages.cashSales());
            transactionTypeList.add(messages.receivedPayments());
            transactionTypeList.add(messages.CustomerCreditNotes());
//            transactionTypeList.add(messages.customerRefunds(Global.get()
//                    .Customer()));
            transactionTypeList.add(messages.checks());
            if (getPreferences().isSalesOrderEnabled()) {
                transactionTypeList.add(messages.salesOrders());
            }
            trasactionViewSelect.initCombo(transactionTypeList);
            trasactionViewSelect.setComboItem(messages.allTransactions());
            trasactionViewSelect
                    .addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

                        @Override
                        public void selectedComboBoxItem(String selectItem) {
                            if (trasactionViewSelect.getSelectedValue() != null) {
                                getMessagesList();
                                callRPC(0, getPageSize());
                            }

                        }

                    });
        }

    }

    private void transactionViewTypeSelectCombo() {
        if (trasactionViewTypeSelect == null) {
            trasactionViewTypeSelect = new SelectCombo(messages.type());
            getMessagesList();
            trasactionViewTypeSelect
                    .addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

                        @Override
                        public void selectedComboBoxItem(String selectItem) {
                            if (trasactionViewTypeSelect.getSelectedValue() != null) {
                                callRPC(0, getPageSize());
                            }

                        }

                    });
        }

    }

    private void getMessagesList() {
        transactiontypebyStatusMap = new HashMap<Integer, String>();
        String selectedValue = trasactionViewSelect.getSelectedValue();
        if (selectedValue.equalsIgnoreCase(messages.allTransactions())) {
            transactiontypebyStatusMap.put(TransactionHistory.ALL_TRANSACTIONS,
                    messages.allTransactions());
        } else if (selectedValue.equalsIgnoreCase(messages.invoices())) {
            transactiontypebyStatusMap.put(TransactionHistory.ALL_INVOICES,
                    messages.getallInvoices());
            transactiontypebyStatusMap.put(TransactionHistory.OPENED_INVOICES,
                    messages.getOpendInvoices());
            transactiontypebyStatusMap.put(
                    TransactionHistory.OVER_DUE_INVOICES,
                    messages.overDueInvoices());
            if (Accounter.hasPermission(Features.DRAFTS)) {
                transactiontypebyStatusMap.put(
                        TransactionHistory.DRAFT_INVOICES,
                        messages.draftTransaction(messages.invoices()));
            }
        } else if (selectedValue.equalsIgnoreCase(messages.cashSales())) {
            transactiontypebyStatusMap.put(TransactionHistory.ALL_CASHSALES,
                    messages.all() + " " + messages.cashSales());
            if (Accounter.hasPermission(Features.DRAFTS)) {
                transactiontypebyStatusMap.put(
                        TransactionHistory.DRAFT_CASHSALES,
                        messages.draftTransaction(messages.cashSales()));
            }

        } else if (selectedValue.equalsIgnoreCase(messages.quotes())) {

            transactiontypebyStatusMap.put(TransactionHistory.ALL_QUOTES,
                    messages.allQuotes());
            if (Accounter.hasPermission(Features.DRAFTS)) {
                transactiontypebyStatusMap.put(TransactionHistory.DRAFT_QUOTES,
                        messages.draftTransaction(messages.quotes()));
            }
        } else if (selectedValue.equalsIgnoreCase(messages.credits())) {
            transactiontypebyStatusMap.put(TransactionHistory.ALL_CREDITS,
                    messages.allCredits());
            if (Accounter.hasPermission(Features.DRAFTS)) {
                transactiontypebyStatusMap.put(
                        TransactionHistory.DRAFT_CREDITS,
                        messages.draftTransaction(messages.credits()));
            }

        } else if (selectedValue.equalsIgnoreCase(messages.Charges())) {

            transactiontypebyStatusMap.put(TransactionHistory.ALL_CHARGES,
                    messages.allCahrges());
            if (Accounter.hasPermission(Features.DRAFTS)) {
                transactiontypebyStatusMap.put(
                        TransactionHistory.DRAFT_CHARGES,
                        messages.draftTransaction(messages.Charges()));
            }

        } else if (selectedValue.equalsIgnoreCase(messages.receivedPayments())) {
            transactiontypebyStatusMap.put(
                    TransactionHistory.ALL_RECEIVEDPAYMENTS, messages.all()
                            + " " + messages.receivedPayments());
            transactiontypebyStatusMap.put(
                    TransactionHistory.RECEV_PAY_BY_CASH,
                    messages.receivedPaymentsbyCash());
            transactiontypebyStatusMap.put(
                    TransactionHistory.RECEV_PAY_BY_CHEQUE,
                    messages.receivedPaymentsbyCheque());
            transactiontypebyStatusMap.put(
                    TransactionHistory.RECEV_PAY_BY_CREDITCARD,
                    messages.receivedPaymentsbyCreditCard());
            transactiontypebyStatusMap.put(
                    TransactionHistory.RECEV_PAY_BY_DIRECT_DEBIT,
                    messages.receivedPaymentsbyDirectDebit());
            transactiontypebyStatusMap.put(
                    TransactionHistory.RECEV_PAY_BY_MASTERCARD,
                    messages.receivedPaymentsbyMastercard());
            transactiontypebyStatusMap.put(
                    TransactionHistory.RECEV_PAY_BY_ONLINE,
                    messages.receivedPaymentsbyOnlineBanking());
            transactiontypebyStatusMap.put(
                    TransactionHistory.RECEV_PAY_BY_STANDING_ORDER,
                    messages.receivedPaymentsbyStandingOrder());
            transactiontypebyStatusMap.put(
                    TransactionHistory.RECEV_PAY_BY_MAESTRO,
                    messages.receivedPaymentsbySwitchMaestro());

        } else if (selectedValue.equalsIgnoreCase(messages
                .CustomerCreditNotes())) {
            transactiontypebyStatusMap.put(TransactionHistory.ALL_CREDITMEMOS,
                    messages.allCreditMemos());
            if (Accounter.hasPermission(Features.DRAFTS)) {
                transactiontypebyStatusMap.put(
                        TransactionHistory.DRAFT_CREDITMEMOS,
                        messages.draftTransaction(messages.creditNote()));
            }
            // transactiontypebyStatusMap.put(
            // TransactionHistory.OPEND_CREDITMEMOS,
            // messages.openCreditMemos());
        }
//         else if (selectedValue.equalsIgnoreCase(messages
//                .customerRefunds(Global.get().Customer()))) {
//            transactiontypebyStatusMap.put(
//                    TransactionHistory.REFUNDS_BY_CREDITCARD,
//                    messages.refundsByCreditCard());
//            transactiontypebyStatusMap.put(TransactionHistory.REFUNDS_BYCASH,
//                    messages.refundsByCash());
//            transactiontypebyStatusMap.put(TransactionHistory.REFUNDS_BYCHEQUE,
//                    messages.refundsByCheck());
//
//            transactiontypebyStatusMap.put(
//                    TransactionHistory.ALL_CUSTOMER_REFUNDS,
//                    messages.allCustomerRefunds());
//            if (Accounter.hasPermission(Features.DRAFTS)) {
//                transactiontypebyStatusMap.put(
//                        TransactionHistory.DRAFT_CUSTOMER_REFUNDS, messages
//                                .draftTransaction(messages
//                                        .customerRefunds(Global.get()
//                                                .Customer())));
//            }
//        }
        else if (trasactionViewSelect.getSelectedValue().equalsIgnoreCase(
                messages.cheques())) {
            transactiontypebyStatusMap.put(TransactionHistory.ALL_CHEQUES,
                    messages.allcheques());
            if (Accounter.hasPermission(Features.DRAFTS)) {
                transactiontypebyStatusMap.put(
                        TransactionHistory.DRAFT_CHEQUES,
                        messages.draftTransaction(messages.cheques()));
            }
        } else if (trasactionViewSelect.getSelectedValue().equalsIgnoreCase(
                messages.salesOrders())) {
            transactiontypebyStatusMap.put(TransactionHistory.ALL_SALES_ORDERS,
                    messages.all());
            transactiontypebyStatusMap.put(
                    TransactionHistory.COMPLETED_SALES_ORDERS,
                    messages.completed());
            transactiontypebyStatusMap.put(
                    TransactionHistory.OPEN_SALES_ORDERS, messages.open());

        }
        List<String> typeList = new ArrayList<String>(
                transactiontypebyStatusMap.values());
        Collections.sort(typeList, new Comparator<String>() {

            @Override
            public int compare(String entry1, String entry2) {
                return entry1.compareTo(entry2);
            }

        });
        trasactionViewTypeSelect.initCombo(typeList);
        trasactionViewTypeSelect.setComboItem(typeList.get(0));
    }


    private void initItemsListGrid() {
        Accounter.createHomeService().getConsultantItemsDetails(
                new AsyncCallback<PaginationList<ConsultantsDetailsList>>() {

                    @Override
                    public void onSuccess(PaginationList<ConsultantsDetailsList> result) {
                        itemsListGrid.removeAllRecords();
                        if (result.size() == 0) {
                            itemsListGrid.addEmptyMessage(messages
                                    .youDontHaveAny(Global.get().consultants()));
                        } else {
                            itemsListGrid.setRecords(result);
                            updateRecordsCountInItems(result.getStart(),
                                    result.size(), result.getTotalCount());
                            //selectedItem=itemsListGrid.getRecordByIndex(0);
                        }
                    }

                    @Override
                    public void onFailure(Throwable caught) {
                        // TODO Auto-generated method stub
                    }
                });

        //itemsListGrid.setRecords(result);
    }

    private void OncusotmerSelected() {
        this.selectedItem = itemsListGrid.getSelection();
        if (Accounter.isIpadApp()) {
            rightVpPanel.add(transactionButton);
            transactionButton.setText(messages2
                    .transactionListFor(selectedItem.getName()));
        }
//        detailsPanel.showCustomerDetails(selectedItem,0);
        custHistoryGrid.setSelectedCustomer(selectedItem);
        MainFinanceWindow.getViewManager().updateButtons();
        showButtonBar();
        callRPC(0, getPageSize());
    }

    @Override
    public void onClose() {
        Accounter.showError("");
        super.onClose();

    }

    @Override
    protected String getViewTitle() {
        return messages.payees(Global.get().Customer());
    }

    @Override
    public void deleteSuccess(IAccounterCore result) {
        Iterator<PayeeList> iterator = listOfCustomers.iterator();
        while (iterator.hasNext()) {
            PayeeList next = iterator.next();
            if (next.getID() == result.getID()) {
                iterator.remove();
            }
        }
    }

    @Override
    public void deleteFailed(AccounterException caught) {

    }

    public void setSelectedCustomer(ConsultantsDetailsList selectedCustomer) {
        this.selectedItem = selectedItem;
    }

    public ConsultantsDetailsList getSelectedCustomer() {
        return selectedItem;
    }

    @Override
    protected void callRPC(int start, int length) {
        custHistoryGrid.removeAllRecords();
        TransactionHistory records =new  TransactionHistory();
        if (selectedItem != null) {
            Accounter.createReportService().getConsultantTransactionsList(
                    selectedItem.getId(), getTransactionType(),
                    getTransactionStatusType(), getStartDate(), getEndDate(),
                    start, length,
                    new AsyncCallback<PaginationList<TransactionHistory>>() {

                        @Override
                        public void onFailure(Throwable caught) {
                            caught.printStackTrace();
                        }

                        @Override
                        public void onSuccess(
                        		PaginationList<TransactionHistory> result) {
                            custHistoryGrid.removeAllRecords();
                            if (records != null) {
                            	double remainingBal = 0.0;
                            	for(TransactionHistory results : result)
                            	{
                            		remainingBal += (int) results.getInvoicedAmount();
                            	}
                                custHistoryGrid.setRecords(result);
                                detailsPanel.showCustomerDetails(selectedItem,remainingBal);
                                  
                            }
                            updateRecordsCount(result.getStart(),
                                    result.size(), result.getTotalCount());
                            if (result.size() == 0) {
                                custHistoryGrid.addEmptyMessage(messages
                                        .thereAreNo(messages.transactions()));
                            }

                        }
                    });
            Accounter.createGETService().getClientTransactionItem(selectedItem.getId(), new AsyncCallback<ClientTransactionItem>() {
    	

				@Override
				public void onSuccess(ClientTransactionItem result) {
					ClientQuantity quantity = new ClientQuantity();
                	quantity.setUnit(1);
                	quantity.setValue(1);
    				result.setQuantity(quantity);
    				result.setLineTotal(result.getUnitPrice());
    				consultantSelected = result;	
				}

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					
				}
    		});

        } else {
            custHistoryGrid.removeAllRecords();
            custHistoryGrid.addEmptyMessage(messages.thereAreNo(messages
                    .transactions()));

        }
    }

    private int getTransactionStatusType() {
        if (trasactionViewTypeSelect.getSelectedValue() != null) {
            Set<Integer> keySet = transactiontypebyStatusMap.keySet();
            for (Integer integerKey : keySet) {
                String entrystring = transactiontypebyStatusMap.get(integerKey);
                if (trasactionViewTypeSelect.getSelectedValue().equals(
                        entrystring)) {
                    return integerKey;
                }
            }
        }
        return TransactionHistory.ALL_INVOICES;

    }

    private int getTransactionType() {
        String selectedValue = trasactionViewSelect.getSelectedValue();
        if (selectedValue.equalsIgnoreCase(messages.invoices())) {

            return TYPE_INVOICE;
        } else if (selectedValue.equalsIgnoreCase(messages.cashSales())) {
            return TYPE_CAHSSALE;
        } else if (selectedValue.equalsIgnoreCase(messages.receivedPayments())) {
            return TYPE_RECEIVE_PAYMENT;
        } else if (selectedValue.equalsIgnoreCase(messages
                .CustomerCreditNotes())) {
            return TYPE_CREDITNOTE;
        } else if (selectedValue.equalsIgnoreCase(messages.quotes())
                || selectedValue.equalsIgnoreCase(messages.credits())
                || selectedValue.equalsIgnoreCase(messages.Charges())) {
            return TYPE_ESTIMATE;
//        } else if (selectedValue.equalsIgnoreCase(messages
//                .customerRefunds(Global.get().Customer()))) {
//            return TYPE_CUSTOMER_REFUND;
        } else if (selectedValue.equalsIgnoreCase(messages.cheques())) {
            return TYPE_WRITE_CHECK;
        } else if (selectedValue.equalsIgnoreCase(messages.salesOrders())) {
            return TYPE_SALES_ORDER;
        }
        return TYPE_ALL_TRANSACTION;

    }

    @Override
    public void restoreView(HashMap<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return;
        }
        String activeInactive = (String) map.get("activeInActive");
        activeInActiveSelect.setComboItem(activeInactive);
        if (activeInactive.equalsIgnoreCase(messages.active())) {
            refreshActiveinActiveList(true);
        } else {
            refreshActiveinActiveList(false);

        }

        String currentView = (String) map.get("currentView");
        trasactionViewSelect.setComboItem(currentView);
        if (currentView != null) {
            getMessagesList();
        }

        String transctionType = (String) map.get("transactionType");
        trasactionViewTypeSelect.setComboItem(transctionType);

        String dateRange1 = (String) map.get("dateRange");
        dateRangeSelector.setComboItem(dateRange1);
        if (dateRange1 != null) {
            dateRangeChanged(dateRange1);
        }
        ConsultantsDetailsList object = (ConsultantsDetailsList) map.get("clientItemSelection");
        itemsListGrid.setSelection(object);

        String customer = (String) map.get("selectedCustomer");

        if (customer != null && !(customer.isEmpty())) {
            //selectedItem = getCompany().getCustomerByName(customer);
        }
        if (this.selectedItem != null) {
            itemsListGrid.setSelection(selectedItem);
            OncusotmerSelected();
        } else {
            callRPC(0, getPageSize());
        }
    }

    @Override
    public HashMap<String, Object> saveView() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("activeInActive", activeInActiveSelect.getSelectedValue());
        map.put("currentView", trasactionViewSelect.getSelectedValue());
        map.put("transactionType", trasactionViewTypeSelect.getSelectedValue());
        map.put("dateRange", dateRangeSelector.getSelectedValue());
        map.put("selectedCustomer", selectedItem == null ? ""
                : selectedItem.getName());
        ConsultantsDetailsList selection = itemsListGrid.getSelection();
        map.put("clientItemSelection", selection);
        return map;
    }

    @Override
    public boolean canPrint() {
        return false;
    }

    @Override
    public boolean canExportToCsv() {
        return true;
    }

    @Override
    public void exportToCsv() {
        if (selectedItem != null) {
           Accounter.createExportCSVService()
                    .getConsultantTransactionsListExportCsv(selectedItem,
                            getTransactionType(), getTransactionStatusType(),
                            getStartDate(), getEndDate(),
                            new AsyncCallback<String>() {

                                @Override
                                public void onSuccess(String id) {
                                    UIUtils.downloadFileFromTemp(
                                            trasactionViewSelect
                                                    .getSelectedValue()
                                                    + " of "
                                                    + selectedItem
                                                    .getName() + ".csv",
                                            id);
                                }

                                @Override
                                public void onFailure(Throwable caught) {
                                    caught.printStackTrace();
                                }
                            });
        } else {
            Accounter.showError(messages.pleaseSelect(Global.get().Customer()));
        }
    }

    @Override
    public boolean canEdit() {
        if (selectedItem != null
                && Accounter.getUser().isCanDoUserManagement()) {
            return true;
        }
        return false;
    }

    public boolean isDirty() {
        return false;
    }
    

    @Override
    public void addButtons(ButtonGroup group) {
    	  ImageButton timesheetButton = new ImageButton(messages2.timeSheet(), Accounter.getFinanceImages()
  				.createAction(), "add");

    	  timesheetButton.addStyleName("settingsButton");
    	  timesheetButton.getElement().setId("showTimesheetButton");
    	  addButton(group, timesheetButton);
    	  timesheetButton.addClickHandler(new ClickHandler() {

              @Override
              public void onClick(ClickEvent event) {
          		Accounter.createGETService().getRewaTimeSheet(selectedItem.getId(),null,null,new AsyncCallback<ArrayList<ClientRewaHrTimeSheet>>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
						
						
					}

					@Override
					public void onSuccess(ArrayList<ClientRewaHrTimeSheet> result) {
						
				    	RewaTimeSheetDialog dialog = new RewaTimeSheetDialog(result);
				    
						ViewManager.getInstance().showDialog(dialog);		
					}
					
				});
//  	            ActionFactory.getEmployeeTimeSheetAction();

              }
          });
    	  
        
        ImageButton addInvoiceButton = new ImageButton(messages.addNew(messages.invoice()), Accounter.getFinanceImages()
				.createAction(), "add");

        addInvoiceButton.addStyleName("settingsButton");
        addInvoiceButton.getElement().setId("addCustomerInvoiceButton");
        addInvoiceButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
           	
	            ActionFactory.getNewInvoiceAction().runNewInvoiceView(consultantSelected);

            }
        });
        addInvoiceButton.getElement().setAttribute("data-icon", "add");
        addButton(group, addInvoiceButton);
    	

        ImageButton addCustomerButton = null;

        addCustomerButton = new ImageButton(messages.addNew(Global.get()
                .consultants()), Accounter.getFinanceImages()
                .portletPageSettings(), "add");

        addCustomerButton.addStyleName("settingsButton");
        addCustomerButton.getElement().setId("addCustomerButton");
        addCustomerButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                ActionFactory.getNewConsultantAction().run();
            }
        });

        addCustomerButton.getElement().setAttribute("data-icon", "add");
        addButton(group, addCustomerButton);

    }

}
