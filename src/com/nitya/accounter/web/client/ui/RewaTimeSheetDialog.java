package com.nitya.accounter.web.client.ui;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientRewaHrTimeSheet;
import com.nitya.accounter.web.client.ui.core.BaseDialog;
import com.nitya.accounter.web.client.ui.core.DateField;
import com.nitya.accounter.web.client.ui.core.ViewManager;
import com.nitya.accounter.web.client.ui.forms.DynamicForm;
import com.nitya.accounter.web.client.ui.forms.TextItem;
import com.nitya.accounter.web.client.ui.grids.RewaHrTimeSheetGrid;
import com.nitya.accounter.web.client.ui.widgets.DateValueChangeHandler;

public class RewaTimeSheetDialog extends BaseDialog<ClientRewaHrTimeSheet> {
	
	private RewaHrTimeSheetGrid rewaHrGrid;
	DynamicForm form;
	private DateField fromDateValue;
	private DateField toDateValue;
	private TextItem empName;
	private TextItem customerName;
	private TextItem activity;
	private Anchor attachments;
	
	
	public RewaTimeSheetDialog(ArrayList<ClientRewaHrTimeSheet> timeSheet) {
		super(messages2.timeSheet()+" for the week");
		this.getElement().setId("RewaTimeSheet");
		createControls(timeSheet);
		
		
	}

	private void createControls(ArrayList<ClientRewaHrTimeSheet> timeSheet) {
		StyledPanel bodyLayout = new StyledPanel("bodyLayout");
		VerticalPanel vPanel = new VerticalPanel();
    	HorizontalPanel hPanel = new HorizontalPanel();
		form = new DynamicForm("form");
		empName = new TextItem("EmpName", "empName");
		attachments = new Anchor("No Attachments available");
		if(timeSheet.get(0).getAttachment().length() > 2)
		{
			attachments.setText(timeSheet.get(0).getAttachment());
		}
		attachments.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				 Window.open("", "_blank", "");
				
			}
		});
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
						
						
//				    	RewaTimeSheetDialog dialog = new RewaTimeSheetDialog(result);
//				    
//						ViewManager.getInstance().showDialog(dialog);		
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
							
							
//					    	RewaTimeSheetDialog dialog = new RewaTimeSheetDialog(result);
//					    
//							ViewManager.getInstance().showDialog(dialog);		
							setRewaValues(result);
							
						}
						
					});
					
					
				}
			});
		customerName = new TextItem("CustomerName", "customerName");
		activity = new TextItem("Activity","activity");
		rewaHrGrid= new RewaHrTimeSheetGrid(false);
		attachments.setEnabled(false);

		form.add(fromDateValue,toDateValue,empName,customerName,activity);
		hPanel.add(form);
//		hPanel.add(attachments);
		vPanel.add(hPanel);
		vPanel.add(attachments);
		bodyLayout.add(vPanel);
		bodyLayout.add(rewaHrGrid);
		setBodyLayout(bodyLayout);
		setRewaValues(timeSheet);	
//		empName.setValue(timeSheet.get(1).getEmployee_name());
//		customerName.setValue(timeSheet.get(1).getProjectName());
//		activity.setValue(timeSheet.get(1).getProjectActivityName());
//		empName.setEnabled(false);
//		customerName.setEnabled(false);
//		activity.setEnabled(false);
//		rewaHrGrid.addRows(timeSheet);
		
	}
	
	public void setRewaValues(ArrayList<ClientRewaHrTimeSheet> timeSheet)
	{
		rewaHrGrid.clear();
		if(timeSheet.size() > 0)
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
	protected boolean onOK() {
		
		return true;
	}

	@Override
	public void setFocus() {
		
		
	}

}
