package com.nitya.accounter.web.client.ui.vat;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.ui.AbstractBaseView;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.StyledPanel;

public class ManageVATView extends AbstractBaseView {

	Button vatButton;

	public ManageVATView() {

	}

	@Override
	public void init() {
		super.init();
		createControls();
	}

	@Override
	public void initData() {
		super.initData();
	}

	@Override
	public void saveAndUpdateView() {
		super.saveAndUpdateView();
	}

	private void createControls() {

		Label stLabel = new Label(messages.thisWndHelpYoyManageVATTasks());
		Label vatLabel = new Label(messages.fileVATReturnNow());

		CaptionPanel capAssignPanel = new CaptionPanel(messages.getStarted());
		CaptionPanel capPayVatPanel = new CaptionPanel(messages.payVATOwing());
		CaptionPanel capTaskPanel = new CaptionPanel(messages.relatedTask());

		vatButton = new Button(messages.fileVATReturn());
		Button closeButton = new Button(messages.close());
		Button helpButton = new Button(messages.help());

		final Image icon = new Image();
		icon.setResource(Accounter.getFinanceImages().justifyLeft());

		Hyperlink assignlink = new Hyperlink(messages.assignVatCodesToItems(),
				"foo");
		Hyperlink viewLink = new Hyperlink(messages.viewVATItems(), "link1");
		Hyperlink openLink = new Hyperlink(messages.openVATCodeList(), "link2");
		Hyperlink adjLink = new Hyperlink(messages.adjustVATDue(), "link3");

		StyledPanel vPanel = new StyledPanel("vPanel");

		StyledPanel assignPanel = new StyledPanel("assignPanel");
		StyledPanel payVatPanel = new StyledPanel("payVatPanel");
		StyledPanel taskPanel = new StyledPanel("taskPanel");

//		assignPanel.setSize("600px", "100px");
//		payVatPanel.setSize("600px", "100px");
//		taskPanel.setSize("600px", "300px");

		assignPanel.add(icon);
		assignPanel.add(assignlink);
		capAssignPanel.add(assignPanel);

		vatLabel.setStyleName("requiredField");
		payVatPanel.add(vatLabel);
		payVatPanel.add(vatButton);
		capPayVatPanel.add(payVatPanel);

		taskPanel.add(viewLink);
		taskPanel.add(openLink);
		taskPanel.add(adjLink);
		taskPanel.add(closeButton);
		taskPanel.add(helpButton);
		capTaskPanel.add(taskPanel);

		vPanel.add(stLabel);
		vPanel.add(capAssignPanel);
		vPanel.add(capPayVatPanel);
		vPanel.add(capTaskPanel);

		add(vPanel);
	}

	/**
	 * call this method to set focus in View
	 */
	@Override
	public void setFocus() {
		this.vatButton.setFocus(true);
	}

	@Override
	public void deleteFailed(AccounterException caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void fitToSize(int height, int width) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEdit() {
		// TODO Auto-generated method stub

	}

	@Override
	public void print() {
		// TODO Auto-generated method stub

	}

	@Override
	public void printPreview() {
		// NOTHING TO DO.
	}

	@Override
	protected String getViewTitle() {
		return messages.manageVatCodes();
	}

}
