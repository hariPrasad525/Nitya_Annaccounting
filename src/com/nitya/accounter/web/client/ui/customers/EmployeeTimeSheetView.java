package com.nitya.accounter.web.client.ui.customers;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SubmitButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.nitya.accounter.web.client.core.ClientEmployeeTimeSheet;
import com.nitya.accounter.web.client.core.ClientRewaHrTimeSheet;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.core.PaginationList;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.ui.AbstractBaseView;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.AddTimeSheetDialog;
import com.nitya.accounter.web.client.ui.RewaTimeSheetDialog;
import com.nitya.accounter.web.client.ui.StyledPanel;
import com.nitya.accounter.web.client.ui.core.ViewManager;
import com.nitya.accounter.web.client.ui.forms.DateItem;
import com.nitya.accounter.web.client.ui.forms.TextItem;
import com.nitya.accounter.web.client.ui.grids.EmployeeTimeSheetGridList;

public class EmployeeTimeSheetView extends AbstractBaseView<ClientRewaHrTimeSheet> {

	private EmployeeTimeSheetGridList empTimeGrid;
	private DateItem fromDateUI = new DateItem("fromDate", "fromDate");
	private DateItem toDateUI = new DateItem("toDate", "toDate");
	private Button addTimeSheet = new  Button(messages.addNew("Time Sheet"));
	private Button addEmpTimeSheet = new Button(messages.addNew("Rewa TimeSheet"));
	private Button paypal = new Button("paypal");
	
	  @Override
	    public void init() {
	        super.init();
	        this.getElement().setId("empTimeSheet");
	        createControls();

	    }
	  
    /**
     * 
     */
    private void createControls() {
    	StyledPanel mainPanel = new StyledPanel("timeSheet");
    	VerticalPanel vPanel = new VerticalPanel();
    	HorizontalPanel hPanel = new HorizontalPanel();
    	fromDateUI.getDateBox().setValue("");
    	toDateUI.getDateBox().setValue("");
    	fromDateUI.setEnabled(true);
//    	toDateUI.setEnabled(true);
    	hPanel.add(fromDateUI);
//    	hPanel.add(toDateUI);
    	hPanel.add(addEmpTimeSheet);
//    	hPanel.add(paypal);
    	hPanel.add(addTimeSheet);
    	 // paypal integration
    	paypal.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				 FormPanel form = new FormPanel();
	                form.setMethod(FormPanel.METHOD_POST);
	                form.setEncoding(FormPanel.ENCODING_URLENCODED);
	                form.setAction("/authorize_payment");
	                SubmitButton submit = new SubmitButton();
	                TextItem product = new TextItem("product/service", "test");
	                TextItem subTotal = new TextItem("Sub Total:", "test");
	                TextItem shipping = new TextItem("shipping", "test");
	                TextItem tax = new TextItem("Tax:", "test");
	                TextItem total = new TextItem("Total:", "test");
	                subTotal.textBox.setName("subtotal");
	                subTotal.textBox.setValue("100");
	                shipping.textBox.setName("shipping");
	                shipping.textBox.setValue("10");
	                tax.textBox.setName("tax");
	                tax.textBox.setValue("10");
	                total.textBox.setName("total");
	                total.textBox.setValue("120");
	                product.textBox.setName("product");
	                product.textBox.setValue("Next iPhone");
	                VerticalPanel vPanel1 = new VerticalPanel();  
	                submit.setText("checkout");
	                vPanel1.add(product);
	                vPanel1.add(subTotal);
	                vPanel1.add(shipping);
	                vPanel1.add(tax);
	                vPanel1.add(total);
	                vPanel1.add(submit);
	                form.add(vPanel1);
	                
	                mainPanel.add(form);
	                submit.addClickHandler(new ClickHandler() {
						
						@Override
						public void onClick(ClickEvent event) {
							
							form.submit();
						}
					});
	            	
	           	 add(mainPanel);
//				Window.open("http://localhost:9898/main/checkout.html", "", "");
				
			}
		});
    	   	// end of paypal integration
    	addEmpTimeSheet.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {

				Accounter.createGETService().getRewaTimeSheet(1676,null,null,new AsyncCallback<ArrayList<ClientRewaHrTimeSheet>>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
						Window.alert(caught+"");
						
					}

					@Override
					public void onSuccess(ArrayList<ClientRewaHrTimeSheet> result) {
					
				    	RewaTimeSheetDialog dialog = new RewaTimeSheetDialog(result);
				    
						ViewManager.getInstance().showDialog(dialog);		
					}
					
				});
				
			}
		});
    	
    	
    	addTimeSheet.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				showTimeSheetDialog(empTimeGrid.getAllRows());		
			}

		});
    	
    	vPanel.add(hPanel);
//    	fromDateUI.setValue(Calendar.getInstance().getTime());
//    	toDateUI.setValue(Calendar.getInstance().getTime());
    	mainPanel.add(vPanel);
    	empTimeGrid = new EmployeeTimeSheetGridList(false);
    	initValues(fromDateValue(),	toDateValue());
    	mainPanel.add(empTimeGrid);
    	
    	 add(mainPanel);
 		
	}
    
    private void showTimeSheetDialog(List<ClientEmployeeTimeSheet> list) {
    	AddTimeSheetDialog dialog = new AddTimeSheetDialog(
				empTimeGrid.getAllRows());
    	ViewManager.getInstance().showDialog(dialog);
	}

	private void initValues(long fromDate, long toDate) {
		Accounter.createGETService().getEmployeeTimeSheetValues(fromDate, toDate ,new AsyncCallback<PaginationList<ClientEmployeeTimeSheet>>() {
			
			@Override
			public void onSuccess(PaginationList<ClientEmployeeTimeSheet> result) {
//				empTimeGrid.setAllRows(result);
				empTimeGrid.clear();
				empTimeGrid.addRows(result);
				
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}
	protected StyledPanel getTopLayout() {
        StyledPanel topHLay = new StyledPanel("topHLay");
        topHLay.addStyleName("fields-panel");
        return topHLay;
    }

	@Override
	public void deleteFailed(AccounterException caught) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteSuccess(IAccounterCore result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected String getViewTitle() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}

	protected long toDateValue() {
		return fromDateUI.getDate().getDate();
	}

	protected long fromDateValue() {
		return toDateUI.getDate().getDate();
		
	}

}