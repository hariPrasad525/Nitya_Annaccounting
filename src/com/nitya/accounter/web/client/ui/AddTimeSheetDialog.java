package com.nitya.accounter.web.client.ui;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.user.client.Timer;
import com.nitya.accounter.web.client.core.ClientAttachment;
import com.nitya.accounter.web.client.core.ClientEmployeeTimeSheet;
import com.nitya.accounter.web.client.core.ClientItem;
import com.nitya.accounter.web.client.core.ClientTransactionItem;
import com.nitya.accounter.web.client.ui.combo.ItemCombo;
import com.nitya.accounter.web.client.ui.core.ActionFactory;
import com.nitya.accounter.web.client.ui.core.BaseDialog;
import com.nitya.accounter.web.client.ui.core.DateField;
import com.nitya.accounter.web.client.ui.forms.DynamicForm;
import com.nitya.accounter.web.client.ui.forms.TextItem;

public class AddTimeSheetDialog extends BaseDialog<ClientItem>{

	
	private ItemCombo itemCombo;
	private DateField fromDateValue;
	private DateField toDateValue;
	private TextItem hours;
	
	private ClientEmployeeTimeSheet empTimeSheet;
	protected TransactionAttachmentPanel transactionAttachmentPanel;
	ClientTransactionItem consultantSelected; 
	 ArrayList<ClientAttachment> attachments = null;
	DynamicForm form;
	
	public AddTimeSheetDialog(List<ClientEmployeeTimeSheet> list) {
		super(messages.addaNew("Time Sheet"));
		this.getElement().setId("TimeSheetDialog");
		createControls();
//		this.data = data;
//		initData();
	}

	@Override
	protected boolean onOK() {
//		  if(!validateForm())
//		  {
//			  ValidationResult result = new ValidationResult();
//			  result.addError(hours,
//					"Add only upcoming time sheets");
//			  return true; 
//		  }
			
			updateItem();
			empTimeSheet.setUploadFile(transactionAttachmentPanel.getAttachments());
			Set<ClientAttachment> attachmentSet = new HashSet<ClientAttachment>(transactionAttachmentPanel.getAttachments());
			empTimeSheet.setAttachments(attachmentSet);
			saveOrUpdate(empTimeSheet);
			 Timer timer = new Timer()
		        {
		            @Override
		            public void run()
		            {
		            	ActionFactory.getEmployeeTimeSheetAction().run();
		            }
		        };

		      timer.schedule(500);
			
			
			 return true; 
	}

	private void updateItem() {
		empTimeSheet = new ClientEmployeeTimeSheet();
		empTimeSheet.setConsultantName(itemCombo.getDisplayValue());
		empTimeSheet.setItemId(itemCombo.getSelectedValue().getID());
		empTimeSheet.setCompanyId(Accounter.getCompany().getID());
		empTimeSheet.setHours(Long.parseLong(hours.getValue()));
		empTimeSheet.setBilled(false);
		empTimeSheet.setFromDate(fromDateValue.getValue().getDate());
		empTimeSheet.setToDate(toDateValue.getValue().getDate());
		empTimeSheet.setCompanyId(Accounter.getCompany().getID());
//		empTimeSheet.setUploadFile(transactionAttachmentPanel.getAttachments());
		
		
	}


	protected void saveOrUpdate(ClientEmployeeTimeSheet empTimeSheet2) {
		Accounter.createOrUpdate(this, empTimeSheet2);
		
	}
// Method to check todays date
//	private boolean validateForm() {
//		Date today = new Date();
//		today.setHours(0);
//		long todayDate;
//		if (today.getMonth() < 9)
//		{
//			todayDate = Long.parseLong((today.getYear()+1900+"0"+(today.getMonth()+1)+""+today.getDate()));
//		}
//		else
//		{
//			todayDate = Long.parseLong((today.getYear()+1900+""+(today.getMonth()+1)+""+today.getDate()));
//		}
//		if( fromDateValue.getValue().getDate() < todayDate || toDateValue.getValue().getDate() < todayDate)
//		{
//
//			return false;
//		}
//		return true;
//	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected boolean onCancel() {
		return true;
	}

	
	private void createControls(){
		StyledPanel bodyLayout = new StyledPanel("bodyLayout");
		form = new DynamicForm("form");
		 itemCombo = new ItemCombo(messages2.consultantName(),1);
		 fromDateValue = new DateField("From Date", "fromDateValue");
		 fromDateValue.getDateBox().setValue("");
		 toDateValue = new DateField("To Date", "toDateValue");
		 toDateValue.getDateBox().setValue("");
		 hours = new TextItem("Hours", "hours");
		 attachments = new ArrayList<ClientAttachment>();
		 transactionAttachmentPanel = new  TransactionAttachmentPanel() {
			
			@Override
			protected void saveAttachment(ClientAttachment attachment) {
				AddTimeSheetDialog.this.saveAttachment(attachment);
				attachments.add(attachment);
			}
			
			@Override
			public boolean isInViewMode() {
			
				return false;
			}
		};
//		transactionAttachmentPanel.uploadForm.addSubmitCompleteHandler(new SubmitCompleteHandler() {
//
//			@Override
//			public void onSubmitComplete(SubmitCompleteEvent event) {
//				String text = transactionAttachmentPanel.getBrowseFileAnchor().getText();
//				String results = event.getResults();
//				String[] split = results.split("#");
//				if (split.length != 2) {
//					return;// Failed
//				}
//				ClientAttachment attachment = new ClientAttachment();
//				attachment.setCreatedBy(Accounter.getUser().getID());
//				attachment.setAttachmentId(split[0]);
//				attachment.setName(text);
//				attachment.setSize(Long.parseLong(split[1]));
//
//				transactionAttachmentPanel.getBrowseFileAnchor().setText(messages.attachaFile());// Choose
//																	// File
//				// Hide that button
////				uploadButton.setVisible(false);
////				attachmentTable.add(getAttachmentField(attachment));
//				attachments.add(attachment);
//				saveAttachment(attachment);
//			}
//		});


//		 hours.textBox.addKeyPressHandler(new KeyPressHandler() {
//			
//			@Override
//			public void onKeyPress(KeyPressEvent event) {
//				 String input = hours.textBox.getText();
//			        if (!input.matches("[0-9]*")) {
//			            // show some error
//			            return;
//			        }
//			}
//		});
		form.add(itemCombo,fromDateValue,toDateValue,hours);
		bodyLayout.add(form);
		bodyLayout.add(hours);
		bodyLayout.add(transactionAttachmentPanel);
		setBodyLayout(bodyLayout);
	}

	protected void saveAttachment(ClientAttachment attachment) {
		// TODO Auto-generated method stub
		saveSuccess(attachment);
		System.out.println();
	}

}
