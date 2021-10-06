package com.nitya.accounter.web.client.ui.customers;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ConsultantsDetailsList;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.MainFinanceWindow;
import com.nitya.accounter.web.client.ui.core.Action;

public class NewConsultantCenterViewAction extends Action<ConsultantsDetailsList> {

	private ConsultantsDetailsList details = new ConsultantsDetailsList();
	
	public NewConsultantCenterViewAction(ConsultantsDetailsList consultantDetails) {
		super();
		details = consultantDetails;
	}

	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void run() {
		  runAsync(data, isDependent);
		
	}

	private void runAsync(Object data, boolean isDependent) {
		 GWT.runAsync(new RunAsyncCallback() {

	            public void onSuccess() {
	            	NewConsultantDetailsView view = new NewConsultantDetailsView();
	            	view.setConsultantDetails(details);
	                  MainFinanceWindow.getViewManager().showView(view, data,
	                          isDependent, NewConsultantCenterViewAction.this);
	                 
	            }

	            public void onFailure(Throwable e) {
	                Accounter.showError(Global.get().messages()
	                        .unableToshowtheview());
	            }
	        });

	    }
		


	@Override
	public ImageResource getBigImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCatagory() {
		return Global.get().consultants();
	}
	
	@Override
	public String getHistoryToken() {
		// TODO Auto-generated method stub
		return "ConsultantDetails";
	}

	@Override
	public String getHelpToken() {
		// TODO Auto-generated method stub
		return "consultant-details";
	}

}
