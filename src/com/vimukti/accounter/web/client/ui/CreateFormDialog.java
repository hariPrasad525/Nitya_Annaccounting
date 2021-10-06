//package com.vimukti.accounter.web.client.ui;
//
//import java.util.List;
//
//import com.google.gwt.core.client.GWT;
//import com.google.gwt.event.dom.client.ClickEvent;
//import com.google.gwt.event.dom.client.ClickHandler;
//import com.google.gwt.user.client.Window;
//import com.google.gwt.user.client.rpc.AccounterAsyncCallback;
//import com.google.gwt.user.client.rpc.ServiceDefTarget;
//import com.google.gwt.user.client.ui.Button;
//import com.google.gwt.user.client.ui.StyledPanel;
//import com.google.gwt.user.client.ui.Label;
//import com.google.gwt.user.client.ui.StyledPanel;
//import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;
//import com.vimukti.accounter.web.client.IAccounterHomeViewService;
//import com.vimukti.accounter.web.client.IAccounterHomeViewServiceAsync;
//import com.vimukti.accounter.web.client.core.ClientCustomer;
//import com.vimukti.accounter.web.client.core.ClientEstimate;
//import com.vimukti.accounter.web.client.core.Utility;
//import com.vimukti.accounter.web.client.ui.widgets.ListGrid;
//
//public class CreateFormDialog extends AbstractBaseDialog {
//
//	private ListGrid grid;
//	private ClientCustomer customer;
//
//	public CreateFormDialog(AbstractBaseView abstractParent, ClientCustomer customer) {
//		super(abstractParent);
//		this.customer= customer;
//		setTitle("Create Form");
//		setWidth("600");
//		createControl();
//		show();
//	}
//
//	private void createControl() {
//
//		StyledPanel mainLayout= new StyledPanel();
//		mainLayout.setSize("100%", "100%");
//		Label infoLabel= new Label();
//		infoLabel.setText("<b>Select a Quote or a Sales Order</b><br> <p> You can create your invoice based on information entered in a Quote or a Sales Order. Select the<br>document from which you want to create the invoice, and then click OK</p>");
//		mainLayout.add(infoLabel);
//		
//		grid = new ListGrid();
//
//		ListGridField field1 = new ListGridField("date", "Date");
//		ListGridField field2 = new ListGridField("no", "No.");
//		ListGridField field3 = new ListGridField("type", "Type");
//		ListGridField field4 = new ListGridField("customer_name", "Customer Name");
//		ListGridField field5 = new ListGridField("total", "Total");
//		
//		field3.setCellFormatter(new 
//				CellFormatter(){
//
//			public String format(Object value, ListGridRecord record,
//					int rowNum, int colNum) {
//				
//				int i =Integer.parseInt(record.getAttribute("type"));			
//				return Utility.getTransactionName(i);
//			}
//			
//		});
//
//		grid.setFields(field1, field2, field3, field4, field5);
//		grid.setHeight(250);
//		
//		mainLayout.add(grid);
//		
//		getGridData();
//		
//		StyledPanel helpButtonLayout= new StyledPanel();
////		helpButtonLayout.setAlign(Alignment.LEFT);
//		
//		Button helpButton= new Button("Help");
//		helpButton.addClickHandler(new ClickHandler(){
//
//			public void onClick(ClickEvent event) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//		});
//		helpButtonLayout.add(helpButton);
//		
//		StyledPanel okButtonLayout= new StyledPanel();
////		okButtonLayout.setAlign(Alignment.RIGHT);
////		okButtonLayout.setMembersMargin(5);
//		
//		Button okButton = new Button("OK");
//		okButton.addClickHandler(new ClickHandler(){
//
//			public void onClick(ClickEvent event) {
//				removeFromParent();
//			}
//			
//		});
//		okButtonLayout.add(okButton);
//		
//		Button cancelButton = new Button("Cancel");
//		cancelButton.addClickHandler(new ClickHandler(){
//
//			public void onClick(ClickEvent event) {
//				removeFromParent();
//			}
//			
//		});
//		okButtonLayout.add(cancelButton);
//		
//		StyledPanel buttonLayout = new StyledPanel();
////		buttonLayout.setMargin(10);
//		buttonLayout.add(helpButtonLayout);
//		buttonLayout.add(okButtonLayout);
//		
//		mainLayout.add(buttonLayout);
//		
//		add(mainLayout);
//	}
//
//	private void getGridData() {
//
//		final IAccounterHomeViewServiceAsync getService = (IAccounterHomeViewServiceAsync) GWT
//		.create(IAccounterHomeViewService.class);
//		((ServiceDefTarget) getService).setServiceEntryPoint(FinanceApplication.HOME_SERVICE_ENTRY_POINT);
//		
//		getService.getLatestQuotes(new AccounterAsyncCallback<ArrayList<ClientEstimate>>() {
//			public void onException(AccounterException caught) {
//				Window.alert("Get Failed:"+caught);
//			}
//
//			public void onSuccess(ArrayList<ClientEstimate> result) {
//				fillLatestQuoteGrid(result);
//				grid.show();
//			}
//
//		});				
//		
//	}
//
//	private void fillLatestQuoteGrid(List<ClientEstimate> result) {
//
//		ListGridRecord[] records = new ListGridRecord[result.size()];
//		ClientEstimate estimate;
//		for (int recordIndex = 0; recordIndex < records.length; ++recordIndex) {
//			estimate = result.get(recordIndex);
//			records[recordIndex] = new ListGridRecord();
//			records[recordIndex].setAttribute("estimate_id", String.valueOf(estimate.getID()));
//			records[recordIndex].setAttribute("date", estimate.getDate());
//			records[recordIndex].setAttribute("no", estimate.getNumber());
//			records[recordIndex].setAttribute("type", estimate.getType());
//			records[recordIndex].setAttribute("customer_name",FinanceApplication.getCompany().getCustomer(estimate.getCustomer()).getName());
//			records[recordIndex].setAttribute("total", estimate.getTotal());
//		}
//
//		grid.setRecords(records);
//		grid.fetchData();		
//	}
//
//}
