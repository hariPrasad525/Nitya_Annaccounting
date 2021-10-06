package com.vimukti.accounter.web.client.help;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.CustomButton.Face;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;

public class HelpPanel extends Composite implements HasText {
	private static HelpContentUiBinder uiBinder = GWT
			.create(HelpContentUiBinder.class);

	interface HelpContentUiBinder extends UiBinder<Widget, HelpPanel> {
	}

	public HelpPanel() {
		initWidget(uiBinder.createAndBindUi(this));
		setLabelText(Global.get().messages().help());
		Face upFace = button.getUpFace();
		upFace.setImage(new Image(Accounter.getFinanceImages().helpDialogIcon()));
		Face downFace = button.getDownFace();
		downFace.setImage(new Image(Accounter.getFinanceImages()
				.helpPannelIcon()));
//		verticalPannel.setWidth("100%");
	}

	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setText(String text) {
		// TODO Auto-generated method stub

	}

	@UiField
	Label helpLabel;

	private void setLabelText(String text) {
		helpLabel.setText(text);
	}

	@UiField
	FlowPanel verticalPannel;

	@UiField
	Frame frame;

	public void setHelpUrl(String url) {
		frame.setUrl(url);
	}

	@UiField
	FlowPanel horizontalhelpPanel;

	public FlowPanel getHorizontalPannel() {
		return horizontalhelpPanel;
	}

	public void setStyledPanel(FlowPanel horizontalPanel) {
		this.horizontalhelpPanel = horizontalPanel;
	}

	@UiField
	ToggleButton button;

	private boolean isHelpPanel;

	private boolean isRemoved;

	@UiHandler("button")
	void onButtonClick_1(ClickEvent event) {
		addPanel();
	}

	private void addPanel() {
		MainFinanceWindow.getViewManager().toggleHelpPanel(isHelpPanel());
	}

	public void setButtonPushed(boolean isPushed) {
		button.setDown(isPushed);
	}

	public boolean isButtonPushed() {
		return button.isDown();
	}

	public void setIsHelpPanel(boolean isHelpPanel) {
		if (isHelpPanel) {
			button.setEnabled(true);
			setButtonPushed(false);
		}
		this.isHelpPanel = isHelpPanel;
	}

	public boolean isHelpPanel() {
		return isHelpPanel;
	}

	public String getHelpUrl() {
		return frame.getUrl();
	}

	public void setIsRemoved(boolean isRemoved) {
		this.isRemoved = isRemoved;
	}

	public boolean isRemoved() {
		return isRemoved;
	}

	public void setButtonDisabled(boolean isEnable) {
		button.setEnabled(isEnable);
	}
}
