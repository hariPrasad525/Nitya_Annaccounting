package com.vimukti.accounter.web.client.ui.forms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.Widget;

public class AutoCompleteItem extends FormItem<String> {

	private SuggestBox suggestBox = new SuggestBox();
	private String title;
	private HashMap<String, String> valueMap = new HashMap<String, String>();

	public AutoCompleteItem(String title, String emptyMessage) {
		super(title,"AutoCompleteItem");
		getComponent();
		this.add(suggestBox);
		this.addStyleName("autoCompleteItem");

		// TODO hide the drop down icon
		/*
		 * setShowPickerIcon(false); setEmptyPickListHeight(50);
		 * setHideEmptyPickList(false); setEmptyPickListMessage(emptyMessage);
		 * setTextBoxStyle("autoCompleteCombobox");
		 */
	}

	public AutoCompleteItem() {
		this("", "");
		this.addStyleName("autoCompleteItem");
	}

	public void setValueMap(HashMap<String, String> map) {
		this.valueMap = map;

	}

	@Override
	public String getValue() {
		return this.valueMap.get(suggestBox.getValue());
	}

	@Override
	public void setToolTip(String toolTip) {
		super.setToolTip(toolTip);
		suggestBox.setTitle(toolTip);
	}

	public Widget getComponent() {

		MultiWordSuggestOracle map = new MultiWordSuggestOracle();

		Set<String> keySet = this.valueMap.keySet();
		List<String> keyList = new ArrayList<String>();
		for (String key : keySet)
			keyList.add(key);
		Collections.sort(keyList);

		for (String key : keyList) {
			map.add(key.trim());
		}
		suggestBox = new SuggestBox(map);
		suggestBox.setText(this.title);
		suggestBox.addStyleName("suggestBox");
		return suggestBox;

	}

	@Override
	public void setValue(String setContactValue) {
		// TODO Auto-generated method stub

	}

	@Override
	public Widget getMainWidget() {
		return this.suggestBox;
	}

}
