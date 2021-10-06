package com.nitya.accounter.web.client.ui.company;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.nitya.accounter.core.Item;
import com.nitya.accounter.web.client.AccounterAsyncCallback;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientCompany;
import com.nitya.accounter.web.client.core.ClientCustomer;
import com.nitya.accounter.web.client.core.ClientItem;
import com.nitya.accounter.web.client.core.ConsultantsDetailsList;
import com.nitya.accounter.web.client.ui.*;
import com.nitya.accounter.web.client.ui.core.Action;
import com.nitya.accounter.web.client.ui.core.EditMode;
import com.nitya.accounter.web.client.ui.core.ViewManager;

/**
 * @author Raj Vimal modified by Rajesh.A
 */

public class NewConsultAction extends Action<ClientItem> {

    int type;
    int subType;
    private boolean forCustomer;
    private String itemName;
    private boolean fromCompany;
    private boolean isItemEditable;
    private boolean frmAnyView;
    private String quickAddText;

    public NewConsultAction() {
        super();
        fromCompany = true;
    }

    public NewConsultAction(boolean forCustomer) {
        super();
        this.forCustomer = forCustomer;
    }
    public NewConsultAction(String quickAddText) {
        super();
        //super.setToolTip(Global.get().Customer());
        this.quickAddText = quickAddText;

    }
    public NewConsultAction(ClientItem item,
                            AccounterAsyncCallback<Object> callback, boolean forCustomer) {
        super();
        this.catagory = messages.company();
        this.forCustomer = forCustomer;
        fromCompany = true;
        // this.baseView = baseView;
    }
    public NewConsultAction(ClientItem customer,
                               AccounterAsyncCallback<Object> callback) {
        super();
        this.catagory = Global.get().customer();
    }
    public NewConsultAction(boolean isGeneratedFromCustomer, int type2) {
        this();
        this.type = type2;
        this.forCustomer = isGeneratedFromCustomer;
    }

    @Override
    public void run() {
        runAsync(data, isDependent);
    }

    public void runAsync(final ClientItem data, final Boolean isDependent) {
        GWT.runAsync(new RunAsyncCallback() {

            public void onSuccess() {
                ClientCompany company = Accounter.getCompany();
                boolean sellServices = company.getPreferences()
                        .isSellServices();
                boolean sellProducts = company.getPreferences()
                        .isSellProducts();
                ItemView view = GWT.create(ItemView.class);
                view.setType(Item.TYPE_SERVICE);
                view.setSubType(Item.TYPE_CONSULTANT_PART);
                view.setGeneratedFromCustomer(forCustomer);
                view.setItemName(itemName);
                //view.setMode(EditMode.CREATE);
                MainFinanceWindow.getViewManager().showView(view, data,
                        isDependent, NewConsultAction.this);
                if (isItemEditable) {
                    view.onEdit();
                }else
                    view.setMode(EditMode.CREATE);

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

    public ImageResource getBigImage() {
        // NOTHING TO DO.
        return null;
    }

    public ImageResource getSmallImage() {
        return Accounter.getFinanceMenuImages().newItem();
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String getHistoryToken() {

        return HistoryTokens.NEW_CONSULTANT;

    }

    @Override
    public String getCatagory() {

        return messages.company();

    }

    @Override
    public String getHelpToken() {
        return "consaltant-item";
    }

    public void setItemText(String text) {
        this.itemName = text;

    }

    @Override
    public String getText() {
        return messages2.newConsultant();
    }

    public void setisItemEditable(boolean isItemViewEditable) {
        this.isItemEditable = isItemViewEditable;
    }

    public boolean isFrmAnyView() {
        return frmAnyView;
    }

    public void setFrmAnyView(boolean frmAnyView) {
        this.frmAnyView = frmAnyView;
    }

    public int getSubType() {
        return subType;
    }

    public void setSubType(int subType) {
        this.subType = subType;
    }
}
