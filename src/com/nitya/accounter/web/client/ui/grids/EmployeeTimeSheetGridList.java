package com.nitya.accounter.web.client.ui.grids;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.IsWidget;
import com.nitya.accounter.web.client.core.ClientAttachment;
import com.nitya.accounter.web.client.core.ClientEmployeeTimeSheet;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientRewaHrTimeSheet;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.IDeleteCallback;
import com.nitya.accounter.web.client.ui.RewaTimeSheetDialog;
import com.nitya.accounter.web.client.ui.core.ViewManager;
import com.nitya.accounter.web.client.ui.edittable.AddInvoiceColumn;
import com.nitya.accounter.web.client.ui.edittable.DateColumn;
import com.nitya.accounter.web.client.ui.edittable.DeleteColumn;
import com.nitya.accounter.web.client.ui.edittable.EditTable;
import com.nitya.accounter.web.client.ui.edittable.RenderContext;
import com.nitya.accounter.web.client.ui.edittable.TextEditColumn;

public class EmployeeTimeSheetGridList extends EditTable<ClientEmployeeTimeSheet>{

	String allAttachmentsName= "";
	
	public EmployeeTimeSheetGridList(boolean isMultiSelectionEnable) {
		super();
	}

	@Override
	protected void initColumns() {
		TextEditColumn<ClientEmployeeTimeSheet> consultantNameColumn = new TextEditColumn<ClientEmployeeTimeSheet>() {

			@Override
			protected boolean isEnable() {
				return false;
			}

			@Override
			public int getWidth() {
				return 100;
			}

			@Override
			protected String getColumnName() {
				return messages2.consultantName();
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}

			@Override
			protected String getValue(ClientEmployeeTimeSheet row) {
				return row.getConsultantName();
			}

			@Override
			protected void setValue(ClientEmployeeTimeSheet row, String value) {
				
			}

			@Override
			public String getValueAsString(ClientEmployeeTimeSheet row) {
				return null;
			}
		};
		
		this.addColumn(consultantNameColumn);
		
		this.addColumn(new DateColumn<ClientEmployeeTimeSheet>() {

			@Override
			public int getWidth() {
				return 100;
			}

			@Override
			protected String getColumnName() {
				return messages.fromDate();
			}
			
			@Override
			protected ClientFinanceDate getValue(ClientEmployeeTimeSheet row) {
				return getFinanceDate(row,0);
			}


			@Override
			protected void setValue(ClientEmployeeTimeSheet row, ClientFinanceDate value) {
				
			}

			@Override
			public String getValueAsString(ClientEmployeeTimeSheet row) {
				return row.getFromDate()+"";
			}

			@Override
			public int insertNewLineNumber() {
				return 0;
			}
			
		});
	
		this.addColumn(new DateColumn<ClientEmployeeTimeSheet>(){


			@Override
			public int getWidth() {
				return 100;
			}
			
			@Override
			protected String getColumnName() {
				return messages.toDate();
			}
			
			@Override
			protected ClientFinanceDate getValue(ClientEmployeeTimeSheet row) {
				return  getFinanceDate(row,1);
			}

			@Override
			protected void setValue(ClientEmployeeTimeSheet row, ClientFinanceDate value) {
				
			}

			@Override
			public String getValueAsString(ClientEmployeeTimeSheet row) {
				return row.getToDate()+"";
			}

			@Override
			public int insertNewLineNumber() {
				return 0;
			}
			
		});
		
		TextEditColumn<ClientEmployeeTimeSheet> hoursColumn = new TextEditColumn<ClientEmployeeTimeSheet>() {

			@Override
			protected boolean isEnable() {
				return true;
			}

			@Override
			public int getWidth() {
				return 100;
			}

			@Override
			protected String getColumnName() {
				return messages2.hours();
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}

			@Override
			protected String getValue(ClientEmployeeTimeSheet row) {
//				if (row.getHours() != 0)
//				{
//					hoursColumn.setEnable(true);
//				}
				return row.getHours()+"";
			}

			@Override
			protected void setValue(ClientEmployeeTimeSheet row, String value) {
				row.setHours(Long.parseLong(value));
			}

			@Override
			public String getValueAsString(ClientEmployeeTimeSheet row) {
				return null;
			}
		};
		this.addColumn(hoursColumn);
		
		TextEditColumn<ClientEmployeeTimeSheet> attachmentsColumn = new TextEditColumn<ClientEmployeeTimeSheet>() {

			@Override
			protected boolean isEnable() {
				return false;
			}

			@Override
			public int getWidth() {
				return 100;
			}

			@Override
			protected String getColumnName() {
				return messages.attachments();
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}

			@Override
			protected String getValue(ClientEmployeeTimeSheet row) {
				if(row.getUploadFile() != null)
				{
				for(ClientAttachment attachment:row.getUploadFile() )
				{
					allAttachmentsName+= attachment.getName();
				}
				return allAttachmentsName;
				}
				else 
				return "No attachments Available";
			}

			@Override
			protected void setValue(ClientEmployeeTimeSheet row, String value) {
				
			}

			@Override
			public String getValueAsString(ClientEmployeeTimeSheet row) {
				return row.getAttachments().stream().toString();
			}
		};
		this.addColumn(attachmentsColumn);
		
		this.addColumn(new AddInvoiceColumn<ClientEmployeeTimeSheet>() {

			@Override
			public ImageResource getResource(ClientEmployeeTimeSheet row) {
	
				if(!row.isBilled())
				{
					return  super.getResource(row);
				}
				
				else
				 return Accounter.getFinanceImages().createAction();
			}
			
		});
		
		this.addColumn(new DeleteColumn<ClientEmployeeTimeSheet>() {
		
			@Override
			public ImageResource getResource(ClientEmployeeTimeSheet row) {
		
				return Accounter.getFinanceImages().delete();
		
			}
			
			@Override
			public IsWidget getWidget(final RenderContext<ClientEmployeeTimeSheet> context) {
				Image image = new Image();
				image.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
//						IDeleteCallback iDeleteCallback = new IDeleteCallback() {
//
//							@Override
//							public void deleteFailed(AccounterException caught) {
//								// TODO Auto-generated method stub
//								
//							}
//
//							@Override
//							public void deleteSuccess(IAccounterCore result) {
//								// TODO Auto-generated method stub
//								
//							}
//							
//						};
						// on click displaying the dialog box of the rewa hr timesheet
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
				

				return image;
			}
			
		});
	
	}

	@Override
	protected boolean isInViewMode() {
		return false;
	}


	private ClientFinanceDate getFinanceDate(ClientEmployeeTimeSheet row,int type) {
		ClientFinanceDate dateValue = new ClientFinanceDate();
		long date = row.getFromDate();
		if(type == 1)
		{
			date = row.getToDate();
		}
		
		dateValue.setYear(Integer.parseInt((date+"").substring(0,4)));
		dateValue.setMonth(Integer.parseInt((date+"").substring(4,6)));
		dateValue.setDay(Integer.parseInt((date+"").substring(6,8)));
		return dateValue;
	}

}
