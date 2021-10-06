package com.nitya.accounter.web.client.ui.customers;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.*;
import com.nitya.accounter.web.client.externalization.AccounterMessages;
import com.nitya.accounter.web.client.externalization.AccounterMessages2;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.StyledPanel;
import com.nitya.accounter.web.client.ui.UIUtils;
import com.nitya.accounter.web.client.ui.forms.AmountLabel;
import com.nitya.accounter.web.client.ui.forms.DynamicForm;
import com.nitya.accounter.web.client.ui.forms.LabelItem;
import com.nitya.accounter.web.client.ui.forms.LinkItem;

import java.util.Set;

public class ConsultantDetailsPanel extends FlowPanel {
    ConsultantsDetailsList clientItem;
    LabelItem name, email, currency, notes, address, phone;
    LinkItem clientName;
    AmountLabel balance;
    Label heading, custname;
    private ClientAddress payeeAddress;
    private DynamicForm leftform, rightform;
    protected static final AccounterMessages messages = Global.get().messages();
    protected static final AccounterMessages2 messages2 = Global.get().messages2();

    public ConsultantDetailsPanel(ConsultantsDetailsList clientItem) {
        this.clientItem = clientItem;
        createControls();
        showCustomerDetails(clientItem,0);
        setStyleName("customerDetailsPanel");
    }

    private void createControls() {

        name = new LabelItem(messages.name(), "name");

        email = new LabelItem(messages.email(), "email");

//        clientName = new LinkItem(messages.client(), "clientName");

        currency = new LabelItem(messages.currency(), "currency");

        notes = new LabelItem(messages.notes(), "notes");

        address = new LabelItem(messages.address(), "address");

        phone = new LabelItem(messages.phoneNumber(), "phone");

        payeeAddress = new ClientAddress();

//        openingBalance = new AmountLabel(messages.openingBalance(), new ClientCurrency());

        balance = new AmountLabel(messages.balance(), new ClientCurrency());

        leftform = new DynamicForm("leftForm");
        rightform = new DynamicForm("rightform");

        //removed clientName from the form
        leftform.add(name, balance, currency);

        rightform.add(email, phone, address);
        rightform.addStyleName("customers_detail_rightpanel");

        StyledPanel hp = new StyledPanel("panel");

        StyledPanel headingPanel = new StyledPanel("customers_detail_panel");

        heading = new Label(messages2.consultantItem());
        custname = new Label();
        custname.setText(messages.noPayeeSelected(Global.get().Customer()));

        headingPanel.add(heading);
        headingPanel.add(custname);
//        add(headingPanel);
        hp.add(leftform);
        hp.add(rightform);

        add(hp);
        hp.getElement().getParentElement().addClassName("details-Panel");
    }

    protected void showCustomerDetails(ConsultantsDetailsList clientItem,double remainingBalance) {
        if (clientItem != null) {
            custname.setText(clientItem.getName());
            name.setValue(clientItem.getName());

            email.setValue(clientItem.getEmail());

            balance.setAmount(remainingBalance);

            currency.setValue(clientItem.getCurrency());

//            clientName.setValue(clientItem.getClientName());

//            openingBalance.setAmount(clientItem.getOpeningBalance());

            notes.setValue(clientItem.getNotes());
            notes.getMainWidget().getElement().getParentElement()
                    .addClassName("customer-detail-notespanel");


            address.setValue(clientItem.getAddresss());
            phone.setValue(clientItem.getPhone());
            email.setValue(clientItem.getEmail());

        } else {
//            name.setValue("");
            balance.setAmount(0.00);
            currency.setValue("");
//            openingBalance.setAmount(0.00);
            notes.setValue("");
            address.setValue("");
            phone.setValue("");
            email.setValue("");
        }
    }

    public void setConsultantsDetailsList(ConsultantsDetailsList clientItem) {
        this.clientItem = clientItem;
        showCustomerDetails(clientItem,0);
    }

    public ConsultantsDetailsList getConsultantsDetailsList() {
        return clientItem;
    }


}
