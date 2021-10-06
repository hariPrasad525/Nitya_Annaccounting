package com.nitya.accounter.admin.client;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.nitya.accounter.web.client.ui.StyledPanel;
import com.nitya.accounter.web.client.ui.core.Action;
import com.nitya.accounter.web.client.ui.core.ToolBar;

public class AdminViewManger extends FlowPanel {
	private ToolBar toolBar;
	StyledPanel simplepanel;
	StyledPanel vpanel;
	static AdminViewManger adminViewManger;

	public AdminViewManger() {
		createControls();
	}

	private void createControls() {
		vpanel = new StyledPanel("vpanel");
		vpanel.addStyleName("view_manager_body");
		simplepanel = new StyledPanel("simplepanel");
		simplepanel.addStyleName("viewholder");
		this.toolBar = new ToolBar();
		vpanel.add(toolBar);
		vpanel.add(new Label("Accounter_Test"));
		vpanel.add(simplepanel);
		this.add(vpanel);

	}

	public void showView(final AdminAbstractView<?> view, final Action action,
			boolean shouldAskToSave) {
		Object input = action.getInput();
		if (view.getManager() == null) {
			view.setManager(this);
			if (input != null) {
				view.setData(input);
			}
			view.init();
			view.initData();
			simplepanel.add(view);
		}
	}

	public void closeCurrentView() {
		// TODO Auto-generated method stub

	}

}
