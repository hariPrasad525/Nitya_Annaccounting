package com.nitya.accounter.web.client.ui.forms;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.nitya.accounter.web.client.ui.StyledPanel;

public class IpadCheckBox extends CheckBoxImpl {

	public IpadCheckBox(String title, String styleName) {
		super(title, styleName);
	}

	public IpadCheckBox() {
		super(null, null);
	}

	StyledPanel checkBoxPanel;

	private Boolean checkBoxValue = false;

	protected int checkBoxNumber;

	private Button yesButton;

	private Button noButton;

	@Override
	public Boolean getValue() {
		return checkBoxValue;
	}

	@Override
	public String getDisplayValue() {
		if (checkBoxValue) {
			return "YES";
		} else {
			return "NO";
		}

	}

	@Override
	public void setValue(Boolean value) {
		checkBoxValue = value;
		if (value) {
			noButton.removeStyleName("clicked");
			yesButton.addStyleName("clicked");
		} else {
			yesButton.removeStyleName("clicked");
			noButton.addStyleName("clicked");
		}

	}

	@Override
	public void setToolTip(String toolTip) {
		checkBoxPanel.setTitle(toolTip);
	}

	public void addChangeHandler(ValueChangeHandler<Boolean> ChangeHandler) {

	}

	@Override
	public Widget getMainWidget() {
		return this.checkBoxPanel;
	}

	@Override
	public void setEnabled(boolean b) {
		this.yesButton.setEnabled(b);
		this.noButton.setEnabled(b);
	}

	public boolean isChecked() {
		return getValue();
	}

	public void setTabIndex(int index) {

	}

	public void createControl(String title, String styleName) {
		createControl(title, styleName, null);

	}

	public void createControl(String title, String styleName, String description) {
		checkBoxPanel = new StyledPanel("checkBoxPanel");

		yesButton = new Button("YES");
		noButton = new Button("NO");

		checkBoxPanel.add(new Label(title));
		checkBoxPanel.add(yesButton);
		checkBoxPanel.add(noButton);

		if (description != null)
			checkBoxPanel.add(new Label(description));

		yesButton.addStyleName("yesButton");
		yesButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				checkBoxValue = true;
				noButton.removeStyleName("clicked");
				yesButton.addStyleName("clicked");
			}
		});

		noButton.addStyleName("noButton");
		noButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				checkBoxValue = false;
				yesButton.removeStyleName("clicked");
				noButton.addStyleName("clicked");
			}
		});

		this.add(checkBoxPanel);
		this.addStyleName("checkboxItem");

	}

	@Override
	public void setTitle(String string) {
		Label label = (Label) checkBoxPanel.getWidget(0);
		label.setText(string);
	}

}
