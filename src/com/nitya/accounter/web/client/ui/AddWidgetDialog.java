package com.nitya.accounter.web.client.ui;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.portlet.IPortletPage;
import com.nitya.accounter.web.client.ui.core.BaseDialog;
import com.nitya.accounter.web.client.ui.core.ViewManager;
import com.nitya.accounter.web.client.ui.core.WidgetCreator;

public class AddWidgetDialog extends BaseDialog {

	ArrayList<String> addablePortletList;

	@SuppressWarnings("unused")
	private PortalLayout portalLayout;
	@SuppressWarnings("unused")
	private Portlet portlet;
	private StyledPanel bodyLayout;
	@SuppressWarnings("unused")
	private WidgetCreator creator;

	public AddWidgetDialog(IPortletPage parent) {
		super(messages.addWidget());
		this.getElement().setId("AddWidgetDialog");

		addablePortletList = parent.getAddablePortletList();
		portalLayout = parent.getPortalLayout();

		creator = new WidgetCreator();
		createControl();

	}

	private void createControl() {

		setWidth("650px");
		// setIsModal(true);
		// setShowModalMask(true);
		// setModalMaskOpacity(10);
		// setShowEdges(false);
		// setHeaderStyle("widget");
		// setBackgroundColor("white");
		// setBorder("4px solid #595959");

		StyledPanel mainLayout = new StyledPanel("mainLayout");
		// mainLayout.setMargin(15);

		Label label = new Label();
		// label.setWidth100();
		if (addablePortletList.toArray().length > 0) {
			label.setText(messages.widgetsAvailable());
		} else {
			label.setText(messages.widgetsAlreadyOnYourHomepageLabel());
		}

		mainLayout.add(label);

		bodyLayout = new StyledPanel("bodyLayout");
		// bodyLayout.setOverflow(Overflow.AUTO);
		// bodyLayout.setSize("100%", "245");
		// bodyLayout.setBorder("1px solid silver");

		for (int i = 0; i < addablePortletList.toArray().length; i++) {
			@SuppressWarnings("unused")
			final String portalName = addablePortletList.get(i);
			// if (portalName.equals("BANKING_SUMMARY")) {
			// getBankingSummary(portalName);
			// } else if (portalName.equals("CREDIT_OVERVIEW")) {
			// getCreditOverview(portalName);
			// } else if (portalName.equals("DEBIT_OVERVIEW")) {
			// getDebitOverview(portalName);
			// } else if (portalName.equals("PROFIT_AND_LOSS")) {
			// getProfitAndLoss(portalName);
			// } else if (portalName.equals("LATEST_QUOTE")) {
			// getLatestQuote(portalName);
			// } else if (portalName.equals("EXPENSES")) {
			// getExpenses(portalName);
			// } else if (portalName.equals("NEW_CUSTOMER")) {
			// getNewCustomer(portalName);
			// } else if (portalName.equals("ITEM_PURCHASE")) {
			// getPurchaseItem(portalName);
			// } else if (portalName.equals("SALES_ITEM")) {
			// getSalesItem(portalName);
			// } else if (portalName.equals("FUND_TRANSFERED")) {
			// getFundTransfered(portalName);
			// } else if (portalName.equals("CASH_SALES")) {
			// getCashSales(portalName);
			// } else if (portalName.equals("CREDIT_AND_REFUNDS")) {
			// getCreditAndRefunds(portalName);
			// } else if (portalName.equals("NEW_VENDOR")) {
			// getNewVendor(portalName);
			// } else if (portalName.equals("BILL_PAID")) {
			// getBillPaid(portalName);
			// } else if (portalName.equals("CASH_PURCHASE")) {
			// getCashPurchase(portalName);
			// } else if (portalName.equals("CHECK_ISSUED")) {
			// getCheckIssued(portalName);
			// } else if (portalName.equals("DEPOSITE")) {
			// getDesposite(portalName);
			// } else if (portalName.equals("PAYMENT_RECEIVED")) {
			// getPaymentReceived(portalName);
			// } else if (portalName.equals("CREDIT_CARD_CHARGES")) {
			// getCreditCardCharges(portalName);
			// }
		}
		if (addablePortletList.toArray().length > 0) {
			mainLayout.add(bodyLayout);
		}
		add(mainLayout);
		ViewManager.getInstance().showDialog(this);
	}

	// private void getFundTransfered(final String portalName) {
	// WidgetRow fundTransferedRow = new WidgetRow();
	// //
	// fundTransferedRow.getImageLabel().setIcon("/images/icons/fund-transfered.png");
	// fundTransferedRow.getInfoLabel().setText("FUND TRANSFERED");
	//
	// fundTransferedRow.getAddButton().addClickHandler(
	// new ClickHandler() {
	//
	// @Override
	// public void onClick(ClickEvent event) {
	// portlet = creator.getFundTransferedWidget();
	// // portlet.addCloseClickHandler(new CloseClickHandler() {
	// //
	// // public void onCloseClick(CloseClientEvent event) {
	// //
	// // addablePortletList.add(portlet.getName());
	// // portlet.destroy();
	// //
	// // }
	// //
	// // });
	// portalLayout.addPortlet(portlet);
	// addablePortletList.remove(portalName);
	// portalLayout.setPreference();
	// AddWidgetDialog.this.removeFromParent();
	// }
	//
	// });
	// bodyLayout.add(fundTransferedRow);
	// }
	//
	// private void getCreditCardCharges(final String portalName) {
	// WidgetRow creditCardChargesRow = new WidgetRow();
	// // creditCardChargesRow.getImageLabel().setIcon(
	// // "/images/icons/creditcard-charges.png");
	// creditCardChargesRow.getInfoLabel().setText("CREDIT CARD CHARGES");
	//
	// creditCardChargesRow.getAddButton().addClickHandler(
	// new ClickHandler() {
	//
	// public void onClick(
	// ClickEvent event) {
	// portlet = creator.getCreditCardChargesWidget();
	// // portlet.addCloseClickHandler(new CloseClickHandler() {
	// //
	// // public void onCloseClick(CloseClientEvent event) {
	// //
	// // addablePortletList.add(portlet.getName());
	// // portlet.destroy();
	// //
	// // }
	// //
	// // });
	// portalLayout.addPortlet(portlet);
	// addablePortletList.remove(portalName);
	// portalLayout.setPreference();
	// AddWidgetDialog.this.removeFromParent();
	// }
	//
	// });
	// bodyLayout.add(creditCardChargesRow);
	// }
	//
	// private void getDesposite(final String portalName) {
	//
	// WidgetRow despositeRow = new WidgetRow();
	// // despositeRow.getImageLabel().setIcon("/images/icons/deposite.png");
	// despositeRow.getInfoLabel().setText("DEPOSITE");
	//
	// despositeRow.getAddButton().addClickHandler(
	// new ClickHandler() {
	//
	// public void onClick(
	// ClickEvent event) {
	// portlet = creator.getDepositeWidget();
	// // portlet.addCloseClickHandler(new CloseClickHandler() {
	// //
	// // public void onCloseClick(CloseClientEvent event) {
	// //
	// // addablePortletList.add(portlet.getName());
	// // portlet.destroy();
	// //
	// // }
	// //
	// // });
	// portalLayout.addPortlet(portlet);
	// addablePortletList.remove(portalName);
	// portalLayout.setPreference();
	// AddWidgetDialog.this.removeFromParent();
	// }
	//
	// });
	// bodyLayout.add(despositeRow);
	// }
	//
	// private void getCheckIssued(final String portalName) {
	//
	// WidgetRow checkIssuedRow = new WidgetRow();
	// //
	// checkIssuedRow.getImageLabel().setIcon("/images/icons/check-issued.png");
	// checkIssuedRow.getInfoLabel().setText("CHECK ISSUED");
	//
	// checkIssuedRow.getAddButton().addClickHandler(
	// new ClickHandler() {
	//
	// public void onClick(
	// ClickEvent event) {
	// portlet = creator.getCheckIssuedWidget();
	// // portlet.addCloseClickHandler(new CloseClickHandler() {
	// //
	// // public void onCloseClick(CloseClientEvent event) {
	// //
	// // addablePortletList.add(portlet.getName());
	// // portlet.destroy();
	// //
	// // }
	// //
	// // });
	// portalLayout.addPortlet(portlet);
	// addablePortletList.remove(portalName);
	// portalLayout.setPreference();
	// AddWidgetDialog.this.removeFromParent();
	// }
	//
	// });
	// bodyLayout.add(checkIssuedRow);
	// }
	//
	// private void getCashPurchase(final String portalName) {
	// WidgetRow cashPurchaseRow = new WidgetRow();
	// //
	// cashPurchaseRow.getImageLabel().setIcon("/images/icons/cash-purchase.png");
	// cashPurchaseRow.getInfoLabel().setText("CASH PURCHASE");
	//
	// cashPurchaseRow.getAddButton().addClickHandler(
	// new ClickHandler() {
	//
	// public void onClick(
	// ClickEvent event) {
	// portlet = creator.getCashPurchaseWidget();
	// // portlet.addCloseClickHandler(new CloseClickHandler() {
	// //
	// // public void onCloseClick(CloseClientEvent event) {
	// //
	// // addablePortletList.add(portlet.getName());
	// // portlet.destroy();
	// //
	// // }
	// //
	// // });
	// portalLayout.addPortlet(portlet);
	// addablePortletList.remove(portalName);
	// portalLayout.setPreference();
	// AddWidgetDialog.this.removeFromParent();
	// }
	//
	// });
	// bodyLayout.add(cashPurchaseRow);
	// }
	//
	// private void getBillPaid(final String portalName) {
	// WidgetRow billPaidRow = new WidgetRow();
	// // billPaidRow.getImageLabel().setIcon("/images/icons/bill-paid.png");
	// billPaidRow.getInfoLabel().setText("BILL PAID");
	//
	// billPaidRow.getAddButton().addClickHandler(
	// new ClickHandler() {
	//
	// public void onClick(
	// ClickEvent event) {
	// portlet = creator.getBillsPaidWidget();
	// // portlet.addCloseClickHandler(new CloseClickHandler() {
	// //
	// // public void onCloseClick(CloseClientEvent event) {
	// //
	// // addablePortletList.add(portlet.getName());
	// // portlet.destroy();
	// //
	// // }
	// //
	// // });
	// portalLayout.addPortlet(portlet);
	// addablePortletList.remove(portalName);
	// portalLayout.setPreference();
	// AddWidgetDialog.this.removeFromParent();
	// }
	//
	// });
	// bodyLayout.add(billPaidRow);
	// }
	//
	// private void getNewVendor(final String portalName) {
	// WidgetRow newVendorRow = new WidgetRow();
	// // newVendorRow.getImageLabel().setIcon("/images/icons/new-vendor.png");
	// newVendorRow.getInfoLabel().setText("NEW VENDOR");
	//
	// newVendorRow.getAddButton().addClickHandler(
	// new ClickHandler() {
	//
	// public void onClick(
	// ClickEvent event) {
	// portlet = creator.getNewVendorWidget();
	// // portlet.addCloseClickHandler(new CloseClickHandler() {
	// //
	// // public void onCloseClick(CloseClientEvent event) {
	// //
	// // addablePortletList.add(portlet.getName());
	// // portlet.destroy();
	// //
	// // }
	// //
	// // });
	// portalLayout.addPortlet(portlet);
	// addablePortletList.remove(portalName);
	// portalLayout.setPreference();
	// AddWidgetDialog.this.removeFromParent();
	// }
	//
	// });
	// bodyLayout.add(newVendorRow);
	// }
	//
	// private void getCreditAndRefunds(final String portalName) {
	// WidgetRow creditAndRefundsRow = new WidgetRow();
	// // creditAndRefundsRow.getImageLabel().setIcon(
	// // "/images/icons/credits&refunds.png");
	// creditAndRefundsRow.getInfoLabel().setText("CREDIT AND REFUNDS");
	//
	// creditAndRefundsRow.getAddButton().addClickHandler(
	// new ClickHandler() {
	//
	// public void onClick(
	// ClickEvent event) {
	// portlet = creator.getCreditAndRefundWidget();
	// // portlet.addCloseClickHandler(new CloseClickHandler() {
	// //
	// // public void onCloseClick(CloseClientEvent event) {
	// //
	// // addablePortletList.add(portlet.getName());
	// // portlet.destroy();
	// //
	// // }
	// //
	// // });
	// portalLayout.addPortlet(portlet);
	// addablePortletList.remove(portalName);
	// portalLayout.setPreference();
	// AddWidgetDialog.this.removeFromParent();
	// }
	//
	// });
	// bodyLayout.add(creditAndRefundsRow);
	// }
	//
	// private void getCashSales(final String portalName) {
	//
	// WidgetRow cashSalesRow = new WidgetRow();
	// // cashSalesRow.getImageLabel().setIcon("/images/icons/cash-sale.png");
	// cashSalesRow.getInfoLabel().setText("CASH SALES");
	//
	// cashSalesRow.getAddButton().addClickHandler(
	// new ClickHandler() {
	//
	// public void onClick(
	// ClickEvent event) {
	// portlet = creator.getCashSaleWidget();
	// // portlet.addCloseClickHandler(new CloseClickHandler() {
	// //
	// // public void onCloseClick(CloseClientEvent event) {
	// //
	// // addablePortletList.add(portlet.getName());
	// // portlet.destroy();
	// //
	// // }
	// //
	// // });
	// portalLayout.addPortlet(portlet);
	// addablePortletList.remove(portalName);
	// portalLayout.setPreference();
	// AddWidgetDialog.this.removeFromParent();
	// }
	//
	// });
	// bodyLayout.add(cashSalesRow);
	// }
	//
	// private void getPaymentReceived(final String portalName) {
	// WidgetRow paymentReceivedRow = new WidgetRow();
	// // paymentReceivedRow.getImageLabel()
	// // .setIcon("/images/icons/payment-received.png");
	// paymentReceivedRow.getInfoLabel().setText("PAYMENT RECEIVED");
	//
	// paymentReceivedRow.getAddButton().addClickHandler(
	// new ClickHandler() {
	//
	// public void onClick(
	// ClickEvent event) {
	// portlet = creator.getPaymentRecievedWidget();
	// // portlet.addCloseClickHandler(new CloseClickHandler() {
	// //
	// // public void onCloseClick(CloseClientEvent event) {
	// //
	// // addablePortletList.add(portlet.getName());
	// // portlet.destroy();
	// //
	// // }
	// //
	// // });
	// portalLayout.addPortlet(portlet);
	// addablePortletList.remove(portalName);
	// portalLayout.setPreference();
	// AddWidgetDialog.this.removeFromParent();
	// }
	//
	// });
	// bodyLayout.add(paymentReceivedRow);
	// }
	//
	// private void getSalesItem(final String portalName) {
	//
	// WidgetRow salesItemRow = new WidgetRow();
	// // salesItemRow.getImageLabel().setIcon("/images/icons/sales-items.png");
	// salesItemRow.getInfoLabel().setText("SALES ITEM");
	//
	// salesItemRow.getAddButton().addClickHandler(
	// new ClickHandler() {
	//
	// public void onClick(
	// ClickEvent event) {
	// portlet = creator.getSalesItemWidget();
	// // portlet.addCloseClickHandler(new CloseClickHandler() {
	// //
	// // public void onCloseClick(CloseClientEvent event) {
	// //
	// // addablePortletList.add(portlet.getName());
	// // portlet.destroy();
	// //
	// // }
	// //
	// // });
	// portalLayout.addPortlet(portlet);
	// addablePortletList.remove(portalName);
	// portalLayout.setPreference();
	// AddWidgetDialog.this.removeFromParent();
	// }
	//
	// });
	// bodyLayout.add(salesItemRow);
	// }
	//
	// private void getPurchaseItem(final String portalName) {
	// WidgetRow purchaseItemRow = new WidgetRow();
	// //
	// purchaseItemRow.getImageLabel().setIcon("/images/icons/Item-Purchase.png");
	// purchaseItemRow.getInfoLabel().setText("ITEM PURCHASE");
	//
	// purchaseItemRow.getAddButton().addClickHandler(
	// new ClickHandler() {
	//
	// public void onClick(
	// ClickEvent event) {
	// portlet = creator.getPurchaseItemWidget();
	// // portlet.addCloseClickHandler(new CloseClickHandler() {
	// //
	// // public void onCloseClick(CloseClientEvent event) {
	// //
	// // addablePortletList.add(portlet.getName());
	// // portlet.destroy();
	// //
	// // }
	// //
	// // });
	// portalLayout.addPortlet(portlet);
	// addablePortletList.remove(portalName);
	// portalLayout.setPreference();
	// AddWidgetDialog.this.removeFromParent();
	// }
	//
	// });
	// bodyLayout.add(purchaseItemRow);
	// }
	//
	// private void getNewCustomer(final String portalName) {
	// WidgetRow newCustomerRow = new WidgetRow();
	// //
	// newCustomerRow.getImageLabel().setIcon("/images/icons/new-customer.png");
	// newCustomerRow.getInfoLabel().setText("NEW CUSTOMER");
	//
	// newCustomerRow.getAddButton().addClickHandler(
	// new ClickHandler() {
	//
	// public void onClick(
	// ClickEvent event) {
	// portlet = creator.getNewCustomerWidget();
	// // portlet.addCloseClickHandler(new CloseClickHandler() {
	// //
	// // public void onCloseClick(CloseClientEvent event) {
	// //
	// // addablePortletList.add(portlet.getName());
	// // portlet.destroy();
	// //
	// // }
	// //
	// // });
	// portalLayout.addPortlet(portlet);
	// addablePortletList.remove(portalName);
	// portalLayout.setPreference();
	// AddWidgetDialog.this.removeFromParent();
	// }
	//
	// });
	// bodyLayout.add(newCustomerRow);
	// }
	//
	// private void getExpenses(final String portalName) {
	// WidgetRow expensesRow = new WidgetRow();
	// // expensesRow.getImageLabel().setIcon("/images/icons/expenses.png");
	// expensesRow.getInfoLabel().setText("EXPENSES");
	//
	// expensesRow.getAddButton().addClickHandler(
	// new ClickHandler() {
	//
	// public void onClick(
	// ClickEvent event) {
	// portlet = creator.getExpensesWidget();
	// // portlet.addCloseClickHandler(new CloseClickHandler() {
	// //
	// // public void onCloseClick(CloseClientEvent event) {
	// //
	// // addablePortletList.add(portlet.getName());
	// // portlet.destroy();
	// //
	// // }
	// //
	// // });
	// portalLayout.addPortlet(portlet);
	// addablePortletList.remove(portalName);
	// portalLayout.setPreference();
	// AddWidgetDialog.this.removeFromParent();
	// }
	//
	// });
	// bodyLayout.add(expensesRow);
	// }
	//
	// private void getLatestQuote(final String portalName) {
	// WidgetRow latestQuoteRow = new WidgetRow();
	// //
	// latestQuoteRow.getImageLabel().setIcon("/images/icons/latest-quote.png");
	// latestQuoteRow.getInfoLabel().setText("LATEST QUOTE");
	//
	// latestQuoteRow.getAddButton().addClickHandler(
	// new ClickHandler() {
	//
	// public void onClick(
	// ClickEvent event) {
	// portlet = creator.getLatestQuoteWidget();
	// // portlet.addCloseClickHandler(new CloseClickHandler() {
	// //
	// // public void onCloseClick(CloseClientEvent event) {
	// // addablePortletList.add(portlet.getName());
	// // portlet.destroy();
	// //
	// // }
	// //
	// // });
	// portalLayout.addPortlet(portlet);
	// addablePortletList.remove(portalName);
	// portalLayout.setPreference();
	// AddWidgetDialog.this.removeFromParent();
	// }
	//
	// });
	// bodyLayout.add(latestQuoteRow);
	// }
	//
	// private void getProfitAndLoss(final String portalName) {
	// WidgetRow profitAndLossRow = new WidgetRow();
	// // profitAndLossRow.getImageLabel().setIcon(
	// // "/images/icons/profit_loss.png");
	//
	// profitAndLossRow.getInfoLabel().setText("PROFIT AND LOSS");
	// profitAndLossRow.getAddButton().addClickHandler(
	// new ClickHandler() {
	//
	// public void onClick(
	// ClickEvent event) {
	// portlet = creator.getProfitAndLossWidget();
	// // portlet.addCloseClickHandler(new CloseClickHandler() {
	// //
	// // public void onCloseClick(CloseClientEvent event) {
	// // addablePortletList.add(portlet.getName());
	// // portlet.destroy();
	// //
	// // }
	// //
	// // });
	// portalLayout.addPortlet(portlet);
	// addablePortletList.remove(portalName);
	// portalLayout.setPreference();
	// AddWidgetDialog.this.removeFromParent();
	// }
	//
	// });
	// bodyLayout.add(profitAndLossRow);
	// }
	//
	// private void getDebitOverview(final String portalName) {
	// WidgetRow debitOverViewRow = new WidgetRow();
	// //
	// debitOverViewRow.getImageLabel().setIcon("/images/icons/debit-overview.png");
	// debitOverViewRow.getInfoLabel().setText("DEBIT OVERVIEW");
	//
	// debitOverViewRow.getAddButton().addClickHandler(
	// new ClickHandler() {
	//
	// public void onClick(
	// ClickEvent event) {
	// portlet = creator.getDebitOverviewWidget();
	// // portlet.addCloseClickHandler(new CloseClickHandler() {
	// //
	// // public void onCloseClick(CloseClientEvent event) {
	// // addablePortletList.add(portlet.getName());
	// // portlet.destroy();
	// //
	// // }
	// //
	// // });
	// portalLayout.addPortlet(portlet);
	// addablePortletList.remove(portalName);
	// portalLayout.setPreference();
	// AddWidgetDialog.this.removeFromParent();
	// }
	//
	// });
	// bodyLayout.add(debitOverViewRow);
	// }
	//
	// private void getCreditOverview(final String portalName) {
	// WidgetRow creditOverViewRow = new WidgetRow();
	// //
	// creditOverViewRow.getImageLabel().setIcon("/images/icons/credit-overview.png");
	//
	// creditOverViewRow.getInfoLabel().setText("CREDIT OVERVIEW");
	//
	// creditOverViewRow.getAddButton().addClickHandler(
	// new ClickHandler() {
	//
	// public void onClick(
	// ClickEvent event) {
	// portlet = creator.getCreditOverviewWidget();
	// // portlet.addCloseClickHandler(new CloseClickHandler() {
	// //
	// // public void onCloseClick(CloseClientEvent event) {
	// // addablePortletList.add(portlet.getName());
	// // portlet.destroy();
	// //
	// // }
	// //
	// // });
	// portalLayout.addPortlet(portlet);
	// addablePortletList.remove(portalName);
	// portalLayout.setPreference();
	// AddWidgetDialog.this.removeFromParent();
	// }
	//
	// });
	// bodyLayout.add(creditOverViewRow);
	// }
	//
	// public void getBankingSummary(final String portalName) {
	// WidgetRow bankingSummaryRow = new WidgetRow();
	// //
	// bankingSummaryRow.getImageLabel().setIcon("/images/icons/banking.png");
	// bankingSummaryRow.getInfoLabel().setText("BANKING SUMMARY");
	//
	// bankingSummaryRow.getAddButton().addClickHandler(
	// new ClickHandler() {
	//
	// public void onClick(
	// ClickEvent event) {
	// portlet = creator
	// .getBankingSummaryWidget();
	// // portlet
	// // .addCloseClickHandler(new CloseClickHandler() {
	// //
	// // public void onCloseClick(
	// // CloseClientEvent event) {
	// // addablePortletList
	// // .add(portlet
	// // .getName());
	// // portlet.destroy();
	// // }
	// //
	// // });
	// portalLayout.addPortlet(portlet);
	// addablePortletList.remove(portalName);
	// portalLayout.setPreference();
	// AddWidgetDialog.this.removeFromParent();
	// }
	//
	// });
	//
	// bodyLayout.add(bankingSummaryRow);
	// }

	@SuppressWarnings("unused")
	private class WidgetRow extends FlowPanel {

		private Label imageLabel;
		private Button addButton;
		private Label infoLabel;

		public WidgetRow() {
			creteRow();
		}

		private void creteRow() {
			StyledPanel layout = new StyledPanel("layout");
			// layout.setWidth100();
			// layout.setBorder("1px solid silver");
			// layout.setHeight("75px");
			// layout.setMembersMargin(10);
			imageLabel = new Label();
			// imageLabel.setIconWidth(170);
			// imageLabel.setIconHeight(110);
			// imageLabel.setAlign(Alignment.CENTER);
			layout.add(imageLabel);

			StyledPanel layout2 = new StyledPanel("layout2");
			infoLabel = new Label();
			addButton = new Button("Add");

			layout2.add(infoLabel);
			layout2.add(addButton);
			addButton.setEnabled(true);
			layout.add(layout2);

			add(layout);
		}

		public Label getImageLabel() {
			return imageLabel;
		}

		public void setImageLabel(Label imageLabel) {
			this.imageLabel = imageLabel;
		}

		public Button getAddButton() {
			return addButton;
		}

		public void setAddButton(Button addButton) {
			this.addButton = addButton;
		}

		public Label getInfoLabel() {
			return infoLabel;
		}

		public void setInfoLabel(Label infoLabel) {
			this.infoLabel = infoLabel;
		}
	}

	@Override
	protected boolean onOK() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void saveFailed(AccounterException exception) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
