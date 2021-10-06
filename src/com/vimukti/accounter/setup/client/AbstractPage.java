package com.vimukti.accounter.setup.client;

import com.google.gwt.core.client.Callback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public abstract class AbstractPage extends VerticalPanel {

	public static final int MAX_PAGES = 3;
	protected SetupHome setupHome;

	public AbstractPage() {
		setStyleName("page");
	}

	protected abstract Widget createControls();

	public abstract int getPageNo();

	public abstract String getPageTitle();

	public abstract void validate(ValidationResult result);

	protected abstract void savePage(Callback<Boolean, Throwable> callback);

	public void saveData(final Callback<Boolean, ValidationResult> callback) {
		final ValidationResult result = new ValidationResult();
		validate(result);
		if (!result.haveErrors() && !result.haveWarnings()) {
			savePage(new Callback<Boolean, Throwable>() {

				@Override
				public void onFailure(Throwable reason) {
					result.addError(this, reason.getMessage());
					callback.onFailure(result);
				}

				@Override
				public void onSuccess(Boolean result) {
					callback.onSuccess(true);
				}
			});
		} else {
			callback.onFailure(result);
		}
	}

	public static AbstractPage pageByNo(SetupHome home, int currentPage) {
		AbstractPage page = null;
		switch (currentPage) {
		case 1:
			page = new DatabaseDetailsPage();
			break;
		case 2:
			page = new LicenseDetailsPage();
			break;
		case 3:
			page = new AccountDetailsPage();
			break;
		default:
			break;
		}
		if (page != null) {
			page.setupHome = home;
			HTML html = new HTML(page.getPageTitle());
			html.setStyleName("pageTitle");
			page.add(html);
			page.add(page.createControls());

		}
		return page;
	}

	public boolean isFirstPage() {
		return getPageNo() == 1;
	}

	public boolean isLastPage() {
		return MAX_PAGES == getPageNo();
	}

	protected Widget createTitle(String title) {
		return createTitle(title, false);
	}

	protected Widget createTitle(String title, boolean isRequired) {
		String str = "<span>" + title + "</span>";
		if (isRequired) {
			str = str + "<span class='required'>*</span>";
		}
		HTML html = new HTML(str);
		html.setStyleName("title");
		return html;
	}

	protected Widget addTag(Widget mainWidget, String tag) {
		FlowPanel pane = new FlowPanel();
		pane.add(mainWidget);
		HTML html = new HTML(tag);
		html.setStyleName("tag");
		pane.add(html);
		return pane;
	}

}
