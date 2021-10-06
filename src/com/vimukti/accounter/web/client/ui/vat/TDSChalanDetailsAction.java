package com.vimukti.accounter.web.client.ui.vat;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientTDSChalanDetail;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.HistoryTokens;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.SelectChallanTypeDialog;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ViewManager;

public class TDSChalanDetailsAction extends Action<ClientTDSChalanDetail> {

	private int type;

	private boolean isFromListView;

	public TDSChalanDetailsAction() {
		super();
		this.catagory = messages.tds();

	}

	@Override
	public ImageResource getBigImage() {
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().vatAdjustment();
	}

	// @Override
	// public ParentCanvas getView() {
	// return null;
	// }

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	public void runAsync(final ClientTDSChalanDetail data,
			final Boolean isDependent) {
		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				if (type == 0 && data == null) {
					SelectChallanTypeDialog dialog = new SelectChallanTypeDialog() {

						@Override
						protected boolean onCancel() {
							if (!isFromListView()) {
								return super.onCancel();
							}
							return true;
						}
					};
					dialog.setDependent(isDependent);
					dialog.setCallback(getCallback());
					ViewManager.getInstance().showDialog(dialog);
				} else {
					if (data != null) {
						type = data.getFormType();
					}
					TDSChalanDetailsView view = GWT
							.create(TDSChalanDetailsView.class);
					view.setFormTypeSeclected(type);
					MainFinanceWindow.getViewManager().showView(view, data,
							isDependent, TDSChalanDetailsAction.this);

				}

			}

			public void onFailure(Throwable e) {
				Accounter.showError(Global.get().messages()
						.unableToshowtheview());
			}
		});
		// AccounterAsync.createAsync(new CreateViewAsyncCallback() {
		//
		// @Override
		// public void onCreated() {
		//
		// }
		//
		// });
	}

	@Override
	public String getHistoryToken() {
		return HistoryTokens.CHALANDETAILS;
	}

	@Override
	public String getHelpToken() {
		return "challanDetails";
	}

	@Override
	public String getText() {
		return messages.challanDetails();
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public boolean isFromListView() {
		return isFromListView;
	}

	public void isFromListView(boolean isFromTransactionsCenter) {
		this.isFromListView = isFromTransactionsCenter;
	}

}
