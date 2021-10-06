package com.nitya.accounter.web.client.ui.company;

import com.nitya.accounter.web.client.ui.StyledPanel;
import com.nitya.accounter.web.client.ui.core.BaseDialog;
import com.nitya.accounter.web.client.ui.forms.DynamicForm;
import com.nitya.accounter.web.client.ui.forms.TextItem;

public class CountryRegionDialog extends BaseDialog {

	private DynamicForm form1;
	private DynamicForm form2;
	private StyledPanel bodyLayout;
	private TextItem countryRegionNameItem;

	public CountryRegionDialog(String title, String desc) {

		super(title, desc);
		this.getElement().setId("CountryRegionDialog");
		createControls();
	}

	private void createControls() {

		bodyLayout = new StyledPanel("bodyLayout");
		form1 = new DynamicForm("form1");
		form2 = new DynamicForm("form2");

		countryRegionNameItem = new TextItem(messages.countryRegionName(),
				"countryRegionNameItem");
		countryRegionNameItem.setRequired(true);

		TextItem a3CodeItem = new TextItem(messages.athreeCode(), "a3CodeItem");
		// a3CodeItem.setWrapTitle(false);

		TextItem a2CodeItem = new TextItem(messages.atwoCode(), "a2CodeItem");
		// a2CodeItem.setTitle(messages.atwoCode());
		// a2CodeItem.setWrapTitle(false);

		TextItem isoCodeItem = new TextItem(messages.isoCode(), "isoCodeItem");
		// isoCodeItem.setTitle(messages.isoCode());
		// isoCodeItem.setWrapTitle(false);

		form1.add(countryRegionNameItem);
		form2.add(a3CodeItem, a2CodeItem, isoCodeItem);
		// form2.setGroupTitle(messages.countryRegionCode());
		// form2.setIsGroup(true);

		bodyLayout.add(form1);
		bodyLayout.add(form2);
		// bodyLayout.setWidth("100%");
		// bodyLayout.setMargin(20);

		setBodyLayout(bodyLayout);

		// setWidth("350px");

	}

	@Override
	protected boolean onOK() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setFocus() {
		countryRegionNameItem.setFocus();

	}

}
