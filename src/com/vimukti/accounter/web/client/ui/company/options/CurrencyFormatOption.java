package com.vimukti.accounter.web.client.ui.company.options;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.vimukti.accounter.web.client.ui.company.CurrencyFormatDialog;
import com.vimukti.accounter.web.client.ui.core.ViewManager;
import com.vimukti.accounter.web.client.ui.forms.LabelItem;

public class CurrencyFormatOption extends AbstractPreferenceOption {

	LabelItem currencyFormatLabel;

	Button currencyFormatButton;

	public CurrencyFormatOption() {
		super("");
		createControls();
	}

	@Override
	public String getTitle() {
		return messages.currencyFormat();
	}

	@Override
	public String getAnchor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createControls() {
		currencyFormatLabel = new LabelItem(messages.currencyFormat(), "header");
		currencyFormatButton = new Button(messages.changeCurrencyFormat());
		currencyFormatButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				CurrencyFormatDialog dialog = new CurrencyFormatDialog(messages
						.currencyFormat());
				ViewManager.getInstance().showDialog(dialog);
			}
		});
		add(currencyFormatLabel);
		add(currencyFormatButton);
	}

	@Override
	public void onSave() {
	}

	@Override
	public void initData() {
	}

}