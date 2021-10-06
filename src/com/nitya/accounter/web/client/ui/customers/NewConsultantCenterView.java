package com.nitya.accounter.web.client.ui.customers;

import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.Range;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientTransactionItem;
import com.nitya.accounter.web.client.core.ConsultantsDetailsList;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.core.PaginationList;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.ImageButton;
import com.nitya.accounter.web.client.ui.StyledPanel;
import com.nitya.accounter.web.client.ui.core.ActionFactory;
import com.nitya.accounter.web.client.ui.core.ButtonGroup;
import com.nitya.accounter.web.client.ui.core.IButtonContainer;
import com.nitya.accounter.web.client.ui.core.IPrintableView;
import com.nitya.accounter.web.client.ui.grids.ConsultantListGrid;
import com.nitya.accounter.web.client.ui.grids.ConsultantSelectionListener;

public class NewConsultantCenterView extends AbstractPayeeCenterView<ConsultantsDetailsList> implements IPrintableView,IButtonContainer{

	private PaginationList<ConsultantsDetailsList> consultantDetails;
	public static final int DEFAULT_PAGE_SIZE = 25;
	private ConsultantListGrid grid;
	 private ConsultantsDetailsList selectedItem = new ConsultantsDetailsList();
	ClientTransactionItem consultantSelected = new ClientTransactionItem();
	public  NewConsultantCenterView()
	{
		 this.getElement().setId("consultantCenterView1");
	}
	
	  @Override
	    public void init() {
			
		  super.init();
	        this.getElement().setId("newConsultantCenterView1");
	        creatControls();

	    }
	
	public void creatControls() {
		StyledPanel mainPanel = new StyledPanel("consultantCenterViewPanel1");
    	HorizontalPanel hPanel = new HorizontalPanel();
    	
		 grid = new ConsultantListGrid(false);
		 grid.init();
		 initConsultantDetails();
		 int pageSize = getPageSize();
		 grid.setConsultantSelectionListener(new ConsultantSelectionListener() {
	            @Override
	            public void consultantSelected() {
	            
	                OncusotmerSelected();
	            }
	        });
		  hPanel.add(grid);
		  SimplePager pager = new SimplePager(SimplePager.TextLocation.CENTER,
	                (SimplePager.Resources) GWT.create(SimplePager.Resources.class), false, pageSize * 2,
	                true);
	        pager.setDisplay(grid);
	        updateRecordsCount(0, 0, 0);
	        VerticalPanel vp = new VerticalPanel();
	        vp.add(hPanel);
	        vp.add(pager);
		  mainPanel.add(vp);
		  add(mainPanel);
		
	}

	private void initConsultantDetails() {
		 Accounter.createHomeService().getConsultantItemsDetails(
	                new AsyncCallback<PaginationList<ConsultantsDetailsList>>() {

	                    @Override
	                    public void onSuccess(PaginationList<ConsultantsDetailsList> result) {
	                    	grid.removeAllRecords();
	                        if (result.size() == 0) {
	                        	grid.addEmptyMessage(messages
	                                    .youDontHaveAny(Global.get().consultants()));
	                        } else {
	                        	consultantDetails = result;
	                        	grid.setRecords(result);
	                            updateRecordsCount(result.getStart(),
	                            		grid.getTableRowCount(), result.getTotalCount());
	                            //selectedItem=itemsListGrid.getRecordByIndex(0);
	                        }
	                    }

	                    @Override
	                    public void onFailure(Throwable caught) {
	                        // TODO Auto-generated method stub
	                    }
	                });
	}
	
	

	private void OncusotmerSelected() {
		 selectedItem = grid.getSelection();
//		 NewConsultantCenterViewAction action = new NewConsultantCenterViewAction();
//		    action.run(grid.getSelectedItem(), false);
		 ActionFactory.getNewConsultantDetails(selectedItem).run();
	}

	@Override
	public void deleteFailed(AccounterException caught) {
		
	}
	
	
	@Override
	public void fitToSize(int height, int width) {
		super.fitToSize(height, width);
	}
	

	@Override
	protected String getViewTitle() {
		// TODO Auto-generated method stub
		return messages2.consultantCenter();
	}


	protected int getPageSize() {
		return DEFAULT_PAGE_SIZE;
	}

    public void updateRecordsCount(int start, int length, int total) {
        grid.updateRange(new Range(start, getPageSize()));
        grid.setRowCount(total, (start + length) == total);
    }

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteSuccess(IAccounterCore result) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
    public void addButtons(ButtonGroup group) {
    
    	  
        
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
	public HashMap<String, Object> saveView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void restoreView(HashMap<String, Object> viewDate) {
		// TODO Auto-generated method stub
		
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

	@Override
	protected void callRPC(int start, int length) {
		// TODO Auto-generated method stub
		
	}
}

