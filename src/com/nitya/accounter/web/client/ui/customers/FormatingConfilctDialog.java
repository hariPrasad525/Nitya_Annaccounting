package com.nitya.accounter.web.client.ui.customers;

import com.nitya.accounter.web.client.ui.CustomLabel;
import com.nitya.accounter.web.client.ui.StyledPanel;
import com.nitya.accounter.web.client.ui.core.BaseDialog;
import com.nitya.accounter.web.client.ui.core.ViewManager;

/**
 * 
 * @author nitya10
 * 
 */
public class FormatingConfilctDialog extends BaseDialog {

	private CustomLabel previousHeadingLabel, previousLabel, priviousValue,
			nextLabel, nextValue, nextHeadingLabel;

	private StyledPanel prevoiusPanel, nextPanel;

	private String presentheading;

	private String nextSelectIndexValue;

	private String previousSelectedValue;

	private String previpousHeading;

	public FormatingConfilctDialog(String previpousHeading,
			String previousSelectedValue, String presentheading,
			String nextSelectIndexValue) {
		super(messages.formattingConflict(), "");
		this.getElement().setId("FormatingConfilctDialog");
		this.previousSelectedValue = previousSelectedValue;
		this.previpousHeading = previpousHeading;
		this.presentheading = presentheading;
		this.nextSelectIndexValue = nextSelectIndexValue;
		this.createControls();
	}

	/**
	 * Creating the body
	 */
	private void createControls() {

		StyledPanel mainPanel = new StyledPanel("mainPanel");
		prevoiusPanel = new StyledPanel("prevoiusPanel");
		previousHeadingLabel = new CustomLabel(
				messages.alreadyAssigned(previpousHeading));

		previousLabel = new CustomLabel(previpousHeading);
		priviousValue = new CustomLabel(previousSelectedValue);
		prevoiusPanel.add(previousLabel);
		prevoiusPanel.add(priviousValue);

		nextPanel = new StyledPanel("nextPanel");
		nextHeadingLabel = new CustomLabel(
				messages.attemptingToreassign(presentheading));
		nextLabel = new CustomLabel(presentheading);
		nextValue = new CustomLabel(nextSelectIndexValue);
		nextPanel.add(nextLabel);
		nextPanel.add(nextValue);

		mainPanel.add(previousHeadingLabel);
		mainPanel.add(prevoiusPanel);
		mainPanel.add(nextHeadingLabel);
		mainPanel.add(nextPanel);
		mainPanel
				.add(new CustomLabel(messages.headingReassign(presentheading)));
		setBodyLayout(mainPanel);
		ViewManager.getInstance().showDialog(this);
	}

	@Override
	protected boolean onOK() {

		return true;
	}

	@Override
	protected boolean onCancel() {

		return true;
	}

	@Override
	public void setFocus() {

	}

}
