package com.nitya.accounter.web.client.ui.react;

import java.util.List;

import com.google.gwt.dom.client.Element;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.ui.StyledPanel;
import com.nitya.accounter.web.client.ui.core.BaseView;
import com.nitya.accounter.web.client.ui.forms.DynamicForm;
import com.nitya.accounter.web.client.ui.react.ReactGwt.GwtToReactHandler;
import com.nitya.accounter.web.client.ui.react.ReactGwt.ReactToGwtHandler;

public abstract class ReactBaseView<T extends IAccounterCore> extends BaseView<T> {

	protected StyledPanel mainPanel;
	protected String type;
	protected Element reactEle;
	protected GwtToReactHandler gwtToReactHandler;

	@Override
	public void init() {
		super.init();
		this.createControls();
	}
	
	@Override
	public void initData() {
		super.initData();
	}

	protected void renderReactView() {
		ReactGwt.renderElement(mainPanel.getElement(), getType(), this.getData(), null,
				new ReactToGwtHandler() {

					@Override
					public boolean execute(String action, Element element, Object data,
							GwtToReactHandler gwtToReactHandler) {
						return handleReactAction(action, element, data, gwtToReactHandler);
					}
				});
	}

	private void createControls() {
		mainPanel = new StyledPanel("ReactView");
		this.add(mainPanel);
	}

	@Override
	public void deleteFailed(AccounterException caught) {
	}

	@Override
	public void deleteSuccess(IAccounterCore result) {
	}

	@Override
	protected String getViewTitle() {
		return null;
	}

	@Override
	public List<DynamicForm> getForms() {
		return null;
	}

	@Override
	public void setFocus() {
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		this.renderReactView();
	}

	@Override
	protected void onUnload() {
		super.onUnload();
		this.gwtToReactHandler.execute("onUnload", mainPanel.getElement(), this.getData());
	}

	protected String getType() {
		return this.type;
	}

	protected boolean handleReactAction(String action, Element element, Object data,
			GwtToReactHandler gwtToReactHandler) {
		if (this.gwtToReactHandler == null) {
			this.gwtToReactHandler = gwtToReactHandler;
		}
		switch (action) {
		case "init":
			this.reactEle = element;
			mainPanel.getElement().appendChild(this.reactEle);
			return true;
		case "save":

			return true;
		case "cancel":

			return true;

		default:
			return true;
		}
	}

	@Override
	public void onSave(boolean reopen) {
		super.onSave(reopen);
		this.gwtToReactHandler.execute("save", this.reactEle, this.getData());
	}

}
