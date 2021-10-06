package com.vimukti.accounter.web.client.ui.vat;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

public class TDSFiledDetailsAction extends Action {

	private int formType;
	private String ackNo;
	private int financialYearStart;
	private int financialYearEnd;
	private int quater;
	private long dateOfFiled;

	public TDSFiledDetailsAction() {
		super();
		this.catagory = messages.tds();
	}

	@Override
	public String getText() {
		return messages.tdsFiledDetails();
	}

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	public void runAsync(final Object data, final Boolean isDependent) {
		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				TDSFiledDetailsView view = new TDSFiledDetailsView(formType,
						ackNo, financialYearStart, financialYearEnd, quater,
						dateOfFiled);
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, TDSFiledDetailsAction.this);
			}

			public void onFailure(Throwable e) {
				Accounter.showError(Global.get().messages()
						.unableToshowtheview());
			}
		});
//		AccounterAsync.createAsync(new CreateViewAsyncCallback() {
//
//			@Override
//			public void onCreated() {
//				
//			}
//
//		});
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
	public String getHistoryToken() {
		return "tdsFiledDetails";
	}

	@Override
	public String getHelpToken() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setFormType(int formType) {
		this.formType = formType;
	}

	public void setAckNo(String ackNo) {
		this.ackNo = ackNo;
	}

	public void setFinancialYearStart(int financialYearStart) {
		this.financialYearStart = financialYearStart;
	}

	public void setFinancialYearEnd(int financialYearEnd) {
		this.financialYearEnd = financialYearEnd;
	}

	public void setQuater(int quater) {
		this.quater = quater;
	}

	public void setDateOfFiled(long dateOfFiled) {
		this.dateOfFiled = dateOfFiled;
	}

}
