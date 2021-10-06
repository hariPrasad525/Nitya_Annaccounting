package com.vimukti.accounter.web.client.ui;

import java.util.HashMap;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.portlet.DragAndDropEnabler;
import com.vimukti.accounter.web.client.portlet.DragAndDropInput;
import com.vimukti.accounter.web.client.portlet.PortletColumn;
import com.vimukti.accounter.web.client.portlet.PortletPage;
import com.vimukti.accounter.web.client.ui.core.ButtonGroup;
import com.vimukti.accounter.web.client.ui.core.IButtonContainer;

public class DashBoardView extends BaseHomeView implements IButtonContainer {

	private PortletPage page;

	public DashBoardView() {

	}

	@Override
	public void init() {
		super.init();
		add(createControl());

	}

	private Widget createControl() {
		page = new PortletPage(PortletPage.DASHBOARD);
		new Timer() {

			@Override
			public void run() {
				enableDragAndDrop();
			}
		}.schedule(10 * 1000);

		return getPage();
	}

	private void enableDragAndDrop() {
		GWT.runAsync(new RunAsyncCallback() {

			@Override
			public void onSuccess() {
				HashMap<Widget, Widget> portlets = new HashMap<Widget, Widget>();
				for (PortletColumn column : page.getColumns()) {
					for (Portlet portlet : column.getPortlets()) {
						portlets.put((Widget) portlet, portlet.getHeader());
					}
				}
				DragAndDropInput input = new DragAndDropInput(page, page
						.getColumns(), portlets,
						new Callback<Boolean, Boolean>() {

							@Override
							public void onSuccess(Boolean result) {
								page.onDragEnd();
							}

							@Override
							public void onFailure(Boolean reason) {
							}
						});

				DragAndDropEnabler dNdEnabler = new DragAndDropEnabler(input);
				dNdEnabler.enable();

			}

			@Override
			public void onFailure(Throwable reason) {
				// TODO Auto-generated method stub

			}
		});
	}

	@Override
	protected void onAttach() {
		getPage().refreshWidgets();
		super.onAttach();
	}

	@Override
	protected void onUnload() {
		super.onUnload();
	}

	public void refreshWidgetData() {
	}

	public void showGettingStarted() {
	}

	public void hideGettingStarted() {
	}

	public PortletPage getPage() {
		return page;
	}

	public void setPage(PortletPage page) {
		this.page = page;
	}

	@Override
	public void addButtons(ButtonGroup group) {
		final ImageButton configButton = new ImageButton(
				messages.configurePortlets(), Accounter.getFinanceImages()
						.portletPageSettings(), "settings");
		configButton.addStyleName("settingsButton");
		configButton.getElement().setId("configButton");
		configButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				getPage().createSettingsDialog().showRelativeTo(configButton);
			}
		});
		configButton.getElement().setAttribute("data-icon", "settings");
		addButton(group, configButton);
	}

}
