package com.nitya.accounter.web.client.ui.customers;

import com.nitya.accounter.web.client.core.ClientRewaHrTimeSheet;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.ui.AbstractBaseView;
import com.nitya.accounter.web.client.ui.grids.RewaHrTimeSheetGrid;

public class RewaHrTimeSheetView extends AbstractBaseView<ClientRewaHrTimeSheet>{

	private RewaHrTimeSheetGrid rewaHrTimeSheetGrid;
	
	  @Override
	    public void init() {
	        super.init();
	        this.getElement().setId("rewaHrTimeSheet");
	        createControls();

	    }
	
	private void createControls() {
		rewaHrTimeSheetGrid = new RewaHrTimeSheetGrid(false);
		
		
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

}
