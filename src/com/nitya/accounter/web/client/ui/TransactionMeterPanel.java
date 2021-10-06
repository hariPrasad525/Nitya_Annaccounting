package com.nitya.accounter.web.client.ui;

import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.TransactionMeterEventType;
import com.nitya.accounter.web.client.externalization.AccounterMessages;
import com.nitya.accounter.web.client.util.CoreEvent;
import com.nitya.accounter.web.client.util.CoreEventHandler;

public class TransactionMeterPanel extends SimplePanel {
	protected static final AccounterMessages messages = Global.get().messages();
	private int transactionsCount = 0;
	private Label countLabel;
	private StyledPanel meterBar, meterbar1;
	private int maxTransactionCount;

	public TransactionMeterPanel() {
		transactionsCount = Accounter.getCompany().getTransactionsCount();
		maxTransactionCount = Accounter.getCompany().getTransactionsLimit();
		maxTransactionCount = maxTransactionCount < 10000? 10000 : maxTransactionCount;
		initGui();
		Type<CoreEventHandler<TransactionMeterEventType>> type = CoreEvent
				.getType(TransactionMeterEventType.class);
		Accounter.getEventBus().addHandler(type,
				new CoreEventHandler<TransactionMeterEventType>() {

					@Override
					public void onAdd(TransactionMeterEventType obj) {
						increase();
					}

					@Override
					public void onDelete(TransactionMeterEventType obj) {
						decrease();
					}

					@Override
					public void onChange(TransactionMeterEventType obj) {
						// TODO Auto-generated method stub

					}
				});
	}

	public TransactionMeterPanel(int transactionNo) {
		transactionsCount = transactionNo;
		initGui();
	}

	private void initGui() {
		VerticalPanel mainPanel = new VerticalPanel();
		countLabel = new Label(messages.transactionsUsed(
				transactionsCount > 0 ? transactionsCount : 0,
				maxTransactionCount));
		meterBar = new StyledPanel("transactionMeterBar");
		meterbar1 = new StyledPanel("inner_transactionMeterBar");
		mainPanel.add(countLabel);
		meterBar.add(meterbar1);
		addStyleToMeterbar();
		mainPanel.add(meterBar);
		mainPanel.addStyleName("transaction_meter");
		add(mainPanel);
	}

	public void increase() {
		transactionsCount++;
		updateLabel();
	}

	private void updateLabel() {
		addStyleToMeterbar();

		countLabel.setText(messages.transactionsUsed(
				transactionsCount > 0 ? transactionsCount : 0,
				maxTransactionCount));

	}

	public void decrease() {
		transactionsCount--;
		updateLabel();
	}

	public void addStyleToMeterbar() {
		int width = (int) (100 * transactionsCount / (float) maxTransactionCount);
		if (width > 100) {
			meterbar1.setWidth(100 + "%");
		} else {
			meterbar1.setWidth(width + "%");
		}
		int g = (maxTransactionCount * 60 / 100);
		int y = (maxTransactionCount * 80 / 100);
		if (transactionsCount <= g) {
			meterbar1.removeStyleName("inner_transactionMeterBar_red");
			meterbar1.removeStyleName("inner_transactionMeterBar_yellow");
			meterbar1.addStyleName("inner_transactionMeterBar_green");
		} else if (transactionsCount > g && transactionsCount < y) {
			meterbar1.removeStyleName("inner_transactionMeterBar_green");
			meterbar1.removeStyleName("inner_transactionMeterBar_red");
			meterbar1.addStyleName("inner_transactionMeterBar_yellow");
		} else {
			meterbar1.removeStyleName("inner_transactionMeterBar_green");
			meterbar1.removeStyleName("inner_transactionMeterBar_yellow");
			meterbar1.addStyleName("inner_transactionMeterBar_red");
		}
	}
}
