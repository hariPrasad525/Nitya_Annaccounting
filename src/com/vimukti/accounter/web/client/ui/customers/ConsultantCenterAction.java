package com.vimukti.accounter.web.client.ui.customers;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

public class ConsultantCenterAction extends Action {

    private String quickAddText;
    private boolean isEditable;

    public ConsultantCenterAction() {
        super();
        //super.setToolTip(Global.get().Customer());
    }

    public ConsultantCenterAction(String quickAddText) {
        super();
        //super.setToolTip(Global.get().Customer());
        this.quickAddText = quickAddText;

    }

    public ConsultantCenterAction(ClientItem customer,
                                  AccounterAsyncCallback<Object> callback) {
        super();
        this.catagory = Global.get().customer();
    }

    public void run() {
        runAsync(data, isDependent);
    }

    public void runAsync(final Object data, final Boolean isDependent) {

        GWT.runAsync(new RunAsyncCallback() {

            public void onSuccess() {
                ConsultantCenterView view = new ConsultantCenterView();
                MainFinanceWindow.getViewManager().showView(view, data,
                        isDependent, ConsultantCenterAction.this);

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
//
//			}
//
//		});
    }

    public boolean isCustomerViewEditable() {
        return isEditable;
    }

    // @Override
    // public ParentCanvas getView() {
    // return this.view;
    // }

    public ImageResource getBigImage() {
        // NOTHING TO DO.
        return null;
    }

    public ImageResource getSmallImage() {
        return Accounter.getFinanceMenuImages().newCustomer();
    }

    // @Override
    // public String getImageUrl() {
    // return "/images/new_customer.png";
    // }

    @Override
    public String getHistoryToken() {
        return "consultantCenter";
    }


    @Override
    public String getHelpToken() {
        return "consaltant-center";
    }


    @Override
    public String getCatagory() {
        return Global.get().consultants();
    }

    @Override
    public String getText() {
        return "Consultant center";
    }

}
