package com.nitya.accounter.web.client.ui.edittable;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.IsWidget;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientAttachment;
import com.nitya.accounter.web.client.core.ClientEmployeeTimeSheet;
import com.nitya.accounter.web.client.core.ClientQuantity;
import com.nitya.accounter.web.client.core.ClientTransactionItem;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.core.ActionFactory;

public class AddInvoiceColumn<T> extends ImageEditColumn<T> {
	
	ClientTransactionItem consultantSelected = new ClientTransactionItem();

	@Override
	public ImageResource getResource(T row) {
	
		return Accounter.getFinanceImages().saveAndClose();
//		return Accounter.getFinanceImages().createAction();
	}

	@Override
	public IsWidget getWidget(final RenderContext<T> context) {
		Image image = new Image();
		image.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				
				ClientEmployeeTimeSheet empTest = (ClientEmployeeTimeSheet) context.getRow();
				
				if(!empTest.isBilled()) {
				  Accounter.createGETService().getClientTransactionItem(empTest.getItemId(), new AsyncCallback<ClientTransactionItem>() {

						@Override
						public void onSuccess(ClientTransactionItem result) {
							ClientQuantity quantity = new ClientQuantity();
		                	quantity.setUnit(empTest.getHours());
		                	quantity.setValue(empTest.getHours());
		    				result.setQuantity(quantity);
		    				result.setLineTotal(result.getUnitPrice());
//		    				result.setDescription("Invoice for the period "+empTest.getStartDate()+" to "+empTest.getEndDate());
		    				result.setEmpTimeSheet(empTest);
//		    				consultantSelected = result;	
//		    				consultantSelected.setEmployeeTimeSheetId(empTest.getID());
//		    				ActionFactory.getNewInvoiceAction().runNewInvoiceView(consultantSelected);
		    				
		    				InvoiceNavigation(result);
						}

						private void InvoiceNavigation(ClientTransactionItem selectedItem) {
							Global.get().preferences().setEmpTimeSheetId(empTest.getId());
			  				Accounter.createGETService().getTimeSheetAttachments(empTest.getId(),new AsyncCallback< ArrayList<ClientAttachment>>() {

								@Override
								public void onFailure(Throwable caught) {
									// TODO Auto-generated method stub
									
								}

								@Override
								public void onSuccess(ArrayList<ClientAttachment>  result) {
									
								ActionFactory.getNewInvoiceAction().runNewInvoiceView(selectedItem , result);
									
								}
							});
							
						}


						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub
							
						}
		    		});
				}
				else {
					Window.alert("The consultant "+empTest.getConsultantName()+" is already billed");
				}
			}
		});
		return image;
	}
	
	

	@Override
	public int getWidth() {
		return 20;
	}


	@Override
	public int insertNewLineNumber() {
		return 0;
	}
	
	@Override
	protected String getColumnName() {
		return messages.bill();
	}

	@Override
	public String getValueAsString(T row) {
		return "";
	}
	

}
