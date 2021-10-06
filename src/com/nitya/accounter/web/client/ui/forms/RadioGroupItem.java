package com.nitya.accounter.web.client.ui.forms;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.externalization.AccounterMessages;
import com.nitya.accounter.web.client.ui.StyledPanel;

/**
 * 
 * @author Gajendra Choudhary
 * 
 */
public class RadioGroupItem extends FormItem<String> {

	protected static AccounterMessages messages = Global.get().messages();

	RadioButton radioButton;
	StyledPanel vPanel;
	StyledPanel styledPanel = new StyledPanel("panel");
	StyledPanel styledPanel1 = new StyledPanel("panel1");
	Label radioGroupTitle = new Label();
	boolean isVertical = true;
	LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<String, String>();
	List<RadioButton> radioButtonList = new ArrayList<RadioButton>();
	private String highLightedRadioButton;
	private ClickHandler clickHandler;
	private String grouoName;

	public RadioGroupItem(String title) {
		super(title, "RadioGroupItem");
		vPanel = new StyledPanel("readioGropuitem");
		setTitle(title);
		this.add(vPanel);
	}

	public RadioGroupItem() {
		this("");
	}

	public RadioGroupItem(String title, String groupName) {
		super(title, "RadioGroupItem");
		vPanel = new StyledPanel("vPanel");
		this.grouoName = groupName;
		setTitle(title);
		this.add(vPanel);
	}

	public void setGroupName(String groupName) {
		this.grouoName = groupName;
	}

	public void setValue(String... buttonTitles) {

		for (String string : buttonTitles) {
			if (this.grouoName == null) {
				this.grouoName = "radioButtonGroup";
			}
			radioButton = new RadioButton(this.grouoName, string);
			vPanel.add(radioButton);
			this.radioButtonList.add(radioButton);
			if (this.clickHandler != null) {
				this.radioButton.addClickHandler(clickHandler);
			}

		}

	}

	public void setValueMap(String title1, String title2) {

		if (this.grouoName == null) {
			this.grouoName = "radioButtonGroup";
		}
		radioButton = new RadioButton(this.grouoName, title1);
		if (this.clickHandler != null) {
			this.radioButton.addClickHandler(clickHandler);
		}
		this.radioButtonList.add(radioButton);
		vPanel.add(radioButton);

		radioButton = new RadioButton(this.grouoName, title2);
		if (this.clickHandler != null) {
			this.radioButton.addClickHandler(clickHandler);
		}
		vPanel.add(radioButton);
		this.radioButtonList.add(radioButton);

	}

	public void setValueMap(String title1, String title2, String title3) {

		radioButton = new RadioButton("radioButtonGroup", title1);
		if (this.clickHandler != null) {
			this.radioButton.addClickHandler(clickHandler);
		}
		this.radioButtonList.add(radioButton);
		vPanel.add(radioButton);

		radioButton = new RadioButton("radioButtonGroup", title2);
		if (this.clickHandler != null) {
			this.radioButton.addClickHandler(clickHandler);
		}
		this.radioButtonList.add(radioButton);
		vPanel.add(radioButton);

		radioButton = new RadioButton("radioButtonGroup", title3);
		if (this.clickHandler != null) {
			this.radioButton.addClickHandler(clickHandler);
		}
		vPanel.add(radioButton);
		this.radioButtonList.add(radioButton);

	}

	/**
	 * Set the Alignment of the RadioButtonGroupItem by default it shows in
	 * vertical manner
	 * 
	 * @param isVertical
	 */
	public void setVertical(boolean isVertical) {
		this.isVertical = isVertical;

	}

	@Override
	public String getValue() {

		for (RadioButton radioButton : this.radioButtonList) {
			if (radioButton.getValue() == true) {
				return radioButton.getText();
			}

		}
		return null;

	}

	public void setValueMap(LinkedHashMap<String, String> groupMap) {
		for (Map.Entry<String, String> map : groupMap.entrySet()) {
			this.linkedHashMap.put(map.getKey(), map.getValue());
		}

	}

	@Override
	public void setValue(String value) {
		this.highLightedRadioButton = value;
		for (RadioButton radioButton : this.radioButtonList) {
			if (radioButton.getText().equals(this.highLightedRadioButton)) {
				radioButton.setValue(true);
			}
		}
	}

	/**
	 * Set the visibility of title of RadioButtonGroupItem by default it is true
	 * You can make it disable by putting false
	 * 
	 * @param showTitle
	 */

	@Override
	public Widget getMainWidget() {
		this.vPanel.clear();
		if (this.isShowTitle()) {

			String title = this.getTitle();
			// radioGroupTitle.setText(title);
			this.vPanel.add(radioGroupTitle);
		}
		if (isVertical) {
			this.styledPanel.clear();
			for (RadioButton radioButton : this.radioButtonList) {
				if (radioButton.getText().equals(this.highLightedRadioButton)) {
					radioButton.setValue(true);
				}
				this.styledPanel.add(radioButton);

			}
			this.vPanel.add(styledPanel);
			return this.vPanel;
		} else {
			this.styledPanel.clear();
			for (RadioButton radioButton : this.radioButtonList) {
				if (radioButton.getText().equals(this.highLightedRadioButton)) {
					radioButton.setValue(true);
				}
				this.styledPanel.add(radioButton);

			}
			this.vPanel.add(styledPanel);
			return this.vPanel;
		}

	}

	@Override
	public void addClickHandler(ClickHandler handler) {
		this.clickHandler = handler;
		super.addClickHandler(handler);
	}

	public void setValues(ClickHandler handler, String... values) {
		this.clickHandler = handler;
		setValue(values);
	}

	@Override
	public void setDefaultValue(String defaultValue) {
		for (RadioButton radio : radioButtonList) {
			if (radio.getText().equalsIgnoreCase(defaultValue)) {
				radio.setValue(true);
				super.setDefaultValue(defaultValue);
				break;
			}

		}
	}

	@Override
	public void setEnabled(boolean b) {
		super.setEnabled(b);
		for (RadioButton btn : radioButtonList) {
			btn.setEnabled(b);
		}
	}
}
