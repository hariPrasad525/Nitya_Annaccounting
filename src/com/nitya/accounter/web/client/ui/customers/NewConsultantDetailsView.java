package com.nitya.accounter.web.client.ui.customers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientRewaHrTimeSheet;
import com.nitya.accounter.web.client.core.ConsultantsDetailsList;
import com.nitya.accounter.web.client.core.Features;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.core.PaginationList;
import com.nitya.accounter.web.client.core.reports.TransactionHistory;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.GwtTabPanel;
import com.nitya.accounter.web.client.ui.StyledPanel;
import com.nitya.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.nitya.accounter.web.client.ui.combo.SelectCombo;
import com.nitya.accounter.web.client.ui.core.DateField;
import com.nitya.accounter.web.client.ui.forms.DynamicForm;
import com.nitya.accounter.web.client.ui.forms.TextItem;
import com.nitya.accounter.web.client.ui.grids.ConsultantTransactionsHistoryGrid;
import com.nitya.accounter.web.client.ui.grids.RewaHrTimeSheetGrid;
import com.nitya.accounter.web.client.ui.widgets.DateValueChangeHandler;

public class NewConsultantDetailsView extends AbstractPayeeCenterView<ConsultantsDetailsList>{

	 
	 private static final int TYPE_ESTIMATE = 7;
	    private static final int TYPE_INVOICE = 8;
	    private static final int TYPE_CAHSSALE = 1;
	    private static final int TYPE_RECEIVE_PAYMENT = 12;
	    private static final int TYPE_CREDITNOTE = 4;
	    private static final int TYPE_CUSTOMER_REFUND = 5;
	    private static final int TYPE_ALL_TRANSACTION = 100;
	    private static final int TYPE_WRITE_CHECK = 15;
	    private static final int TYPE_SALES_ORDER = 38;
	private ConsultantsDetailsList selectedConsultant= new ConsultantsDetailsList();
	private DynamicForm basicInfoForm, rightform;
	private StyledPanel rightVpPanel, dummyPanel;
	private GwtTabPanel tabSet;
	private StyledPanel transactionGridpanel;
	private ArrayList<DynamicForm> listforms;
	 private Map<Integer, String> transactiontypebyStatusMap;
	private ConsultantDetailsPanel detailsPanel;
	 private SelectCombo activeInActiveSelect, trasactionViewSelect,
     trasactionViewTypeSelect;
	 private ConsultantTransactionsHistoryGrid custHistoryGrid;
	 private RewaHrTimeSheetGrid rewaHrGrid;
	 private TextItem empName, customerName, activity;
	 private DateField fromDateValue, toDateValue;
	 private Anchor attachments;
	 ArrayList<ClientRewaHrTimeSheet> timeSheet;
	 
	public NewConsultantDetailsView() {
		this.getElement().setId("NewConsultantDetailsView");
	}
	
	@Override
	public void init() {
		super.init();
		 this.getElement().setId("newConsultantdetailsView1");
		createControls();
	}
	
	private void createControls() {
		 StyledPanel mainPanel = new StyledPanel("mainPanel");
//		 detailsPanel = new ConsultantDetailsPanel(selectedConsultant);
	
		tabSet = (GwtTabPanel) GWT.create(GwtTabPanel.class);
		Label title = new Label(messages2.consultantCenter());
		title.setStyleName("label-title");
		listforms = new ArrayList<DynamicForm>();		
		tabSet.add(getTabItem(getDetailsTab(), messages.details()), messages.details());
		tabSet.add(getTabItem(getTransactionsTab(), messages.transactions()), messages.transactions());
		tabSet.add(getTabItem(getTimesheetTab(), messages2.timeSheet()), messages2.timeSheet());
		tabSet.selectTab(0);
		mainPanel.add(title);
		tabSet.getPanel().setWidth("100%");
		mainPanel.add(tabSet.getPanel());
		mainPanel.setWidth("100%");
		add(mainPanel);
		
//		setSize("100%", "100%");
	}

	private StyledPanel getDetailsTab() {
		StyledPanel detailPanel = new StyledPanel(messages.details());
		DOM.setStyleAttribute(detailPanel.getElement(), "border","1px solid #ccc");
		
		detailPanel.add(getConsultantInfo());
		
		return detailPanel;
	}
	
	private CaptionPanel getTransactionsTab() {
		CaptionPanel transactionPanel = new CaptionPanel(messages.transactions());
		DOM.setStyleAttribute(transactionPanel.getElement(), "border","1px solid #ccc");
		
		transactionPanel.add(getTransactions());
		return transactionPanel;
	}
	
	private CaptionPanel getTimesheetTab() {
		CaptionPanel timesheetPanel = new CaptionPanel(messages2.timeSheet());
		DOM.setStyleAttribute(timesheetPanel.getElement(), "border","1px solid #ccc");
		
		timesheetPanel.add(getTimesheet());
		return timesheetPanel;
	}
	


	private StyledPanel getConsultantInfo() {
		StyledPanel mainVLay = new StyledPanel("tablePanel");
		
		CaptionPanel consutlantBasicInfo = new CaptionPanel(
				messages2.consultantItem());
		DOM.setStyleAttribute(consutlantBasicInfo.getElement(), "border",
				"1px solid #cccccc"); 
		basicInfoForm = new DynamicForm("basicInfoForm");
		DOM.setStyleAttribute(basicInfoForm.getElement(), "border",
				"1px solid #cccccc");
		detailsPanel = new ConsultantDetailsPanel(selectedConsultant);
		rightVpPanel = new StyledPanel("rightPanel");
		rightVpPanel.add(detailsPanel);
		 mainVLay.add(rightVpPanel);
		return mainVLay;
	}
	
	private StyledPanel getTransactions() {
		StyledPanel mainVLay = new StyledPanel("tablePanel");
		
		CaptionPanel consutlantBasicInfo = new CaptionPanel(
				messages2.consultantItem());
//		DOM.setStyleAttribute(consutlantBasicInfo.getElement(), "border",
//				"1px solid #cccccc"); 
		  dummyPanel = new StyledPanel("dummyPanel");
		 custHistoryGrid = new ConsultantTransactionsHistoryGrid() {
	            @Override
	            public void initListData() {
//	                OncusotmerSelected();
	            }
	        };
	        transactionViewSelectCombo();
	        transactionViewTypeSelectCombo();
	        transactionDateRangeSelector();
	        DynamicForm transactionViewform = new DynamicForm("transactionViewform");
	        HorizontalPanel hp = new HorizontalPanel();
	        hp.add(trasactionViewSelect);
	        hp.add(trasactionViewTypeSelect);
	        hp.add(dateRangeSelector);
	        transactionViewform.add(trasactionViewSelect,trasactionViewTypeSelect,
	        		dateRangeSelector);
	        transactionGridpanel = new StyledPanel("transactionGridpanel");
	        transactionGridpanel.add(transactionViewform);
	        custHistoryGrid.init();
	        custHistoryGrid.addEmptyMessage(messages.pleaseSelectAnyPayee(Global
	                .get().Customer()));
	        int pageSize = getPageSize();
	        custHistoryGrid.addRangeChangeHandler2(new RangeChangeEvent.Handler() {

	            @Override
	            public void onRangeChange(RangeChangeEvent event) {
	                onPageChange(event.getNewRange().getStart(), event
	                        .getNewRange().getLength());
	            }
	        });
	        callRPC(0, getPageSize());
	        SimplePager pager = new SimplePager(SimplePager.TextLocation.CENTER,
	                (SimplePager.Resources) GWT.create(SimplePager.Resources.class), false, pageSize * 2,
	                true);
	        pager.setDisplay(custHistoryGrid);
	        updateRecordsCount(0, 0, 0);
	        dummyPanel.add(transactionGridpanel);
            dummyPanel.add(custHistoryGrid);
            dummyPanel.add(pager);
            mainVLay.add(transactionGridpanel);
            mainVLay.add(custHistoryGrid);
		mainVLay.add(pager);
		return mainVLay;
	}
	

	private StyledPanel getTimesheet() {
		StyledPanel mainVLay = new StyledPanel("tablePanel");

		
		empName = new TextItem("EmpName", "empName");
		attachments = new Anchor("No Attachments available");
  		Accounter.createGETService().getRewaTimeSheet(selectedConsultant.getId(),null,null,new AsyncCallback<ArrayList<ClientRewaHrTimeSheet>>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
				
				
			}

			@Override
			public void onSuccess(ArrayList<ClientRewaHrTimeSheet> result) {
				
		    	timeSheet = result;
			}
			
		});
//		if(timeSheet.get(0).getAttachment().length() > 2)
//		{
//			attachments.setText(timeSheet.get(0).getAttachment());
//		}
//		attachments.addClickHandler(new ClickHandler() {
//			
//			@Override
//			public void onClick(ClickEvent event) {
//				 Window.open("", "_blank", "");
//				
//			}
//		});
		 fromDateValue = new DateField("From Date", "fromDateValue");
		 fromDateValue.getDateBox().setValue("");
		 fromDateValue.addDateValueChangeHandler(new DateValueChangeHandler() {
			
			@Override
			public void onDateValueChange(ClientFinanceDate date) {
				if (fromDateValue == null)
				{
					Window.alert("Enter To Date");
				}
				else
				Accounter.createGETService().getRewaTimeSheet(timeSheet.get(1).getEmpId(),date.getDateAsObject(),toDateValue.getDate().getDateAsObject(),new AsyncCallback<ArrayList<ClientRewaHrTimeSheet>>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
						Window.alert(caught+"");
						
					}

					@Override
					public void onSuccess(ArrayList<ClientRewaHrTimeSheet> result) {	
						setRewaValues(result);
						
					}
					
				});
				
				
			}
		});
		 toDateValue = new DateField("To Date", "toDateValue");
		 toDateValue.getDateBox().setValue("");
		 toDateValue.addDateValueChangeHandler(new DateValueChangeHandler() {
				
				@Override
				public void onDateValueChange(ClientFinanceDate date) {
					if (fromDateValue == null)
					{
						Window.alert("Enter From Date");
					}
					else
					Accounter.createGETService().getRewaTimeSheet(timeSheet.get(1).getEmpId(),fromDateValue.getDate().getDateAsObject(),date.getDateAsObject(),new AsyncCallback<ArrayList<ClientRewaHrTimeSheet>>() {

						@Override
						public void onFailure(Throwable caught) {
							caught.printStackTrace();
							Window.alert(caught+"");
							
						}

						@Override
						public void onSuccess(ArrayList<ClientRewaHrTimeSheet> result) {
		
							setRewaValues(result);
							
						}
						
					});
					
					
				}
			});
		customerName = new TextItem("CustomerName", "customerName");
		activity = new TextItem("Activity","activity");
		rewaHrGrid= new RewaHrTimeSheetGrid(false);
		attachments.setEnabled(false);
		empName.setEnabled(false);
		customerName.setEnabled(false);
		DynamicForm form = new DynamicForm("form");
		form.add(fromDateValue,toDateValue,empName,customerName,activity);
		
//		hPanel.add(attachments);
//		vPanel.add(hPanel);
//		vPanel.add(attachments);
//		bodyLayout.add(vPanel);
//		bodyLayout.add(rewaHrGrid);
//		setBodyLayout(bodyLayout);
//		setRewaValues(timeSheet);	
		mainVLay.add(form);
		mainVLay.add(rewaHrGrid);
		return mainVLay;
	}
	
	
	
	private StyledPanel getTabItem(Widget child, String title) {
		StyledPanel mainPanel = new StyledPanel(title);
		mainPanel.add(child);
		return mainPanel;
	}
	
	//method do be completed for displaying the details of the consultant
	private void initViewData(ConsultantsDetailsList data) {
		detailsPanel.showCustomerDetails(data, data.getBalance());
		
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
           
        }
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
    
	@Override
	protected String getViewTitle() {
		// TODO Auto-generated method stub
		return "Consultant Details";
	}



	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}

	 public void updateRecordsCount(int start, int length, int total) {
	        custHistoryGrid.updateRange(new Range(start, 25));
	        custHistoryGrid.setRowCount(total, (start + length) == total);
	    }

	@Override
	public void deleteFailed(AccounterException caught) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteSuccess(IAccounterCore result) {
		// TODO Auto-generated method stub
		
	}

	public void setConsultantDetails(ConsultantsDetailsList details) {
		
		selectedConsultant = details;
	}

	@Override
	public HashMap<String, Object> saveView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void restoreView(HashMap<String, Object> viewDate) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean canPrint() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canExportToCsv() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canEdit() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isDirty() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void setRewaValues(ArrayList<ClientRewaHrTimeSheet> timeSheet)
	{
		rewaHrGrid.clear();
		if(timeSheet != null && timeSheet.size() > 0 )
		{	
		empName.setValue(timeSheet.get(0).getEmployee_name());
		customerName.setValue(timeSheet.get(0).getProjectName());
		activity.setValue(timeSheet.get(0).getProjectActivityName());
		empName.setEnabled(false);
		customerName.setEnabled(false);
		activity.setEnabled(false);
		rewaHrGrid.addRows(timeSheet);
		}
		else 
		{
			rewaHrGrid.addEmptyRowAtLast();
		}
	}
	
	@Override
	 protected void callRPC(int start, int length) {
        custHistoryGrid.removeAllRecords();
        TransactionHistory records =new  TransactionHistory();
        if (selectedConsultant != null) {
            Accounter.createReportService().getConsultantTransactionsList(
            		selectedConsultant.getId(), getTransactionType(),
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
                                detailsPanel.showCustomerDetails(selectedConsultant,remainingBal);
                                  
                            }
                            updateRecordsCount(result.getStart(),
                                    result.size(), result.getTotalCount());
                            if (result.size() == 0) {
                                custHistoryGrid.addEmptyMessage(messages
                                        .thereAreNo(messages.transactions()));
                            }

                        }
                    });

        } else {
            custHistoryGrid.removeAllRecords();
            custHistoryGrid.addEmptyMessage(messages.thereAreNo(messages
                    .transactions()));

        }
    }
	 


}

