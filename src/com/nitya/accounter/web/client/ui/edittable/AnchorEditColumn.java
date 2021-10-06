package com.nitya.accounter.web.client.ui.edittable;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.core.ICurrencyProvider;

public abstract class AnchorEditColumn<T> extends EditColumn<T> {

	private ICurrencyProvider currencyProvider;

	public AnchorEditColumn(ICurrencyProvider currencyProvider) {
		this.currencyProvider = currencyProvider;
	}

	@Override
	public int getWidth() {
		return -1;
	}

	@Override
	public void render(IsWidget widget, RenderContext<T> context) {
		SimplePanel panel = (SimplePanel) widget;
		Anchor anchor = (Anchor) panel.getWidget();
		String value = getValue(context.getRow());
		anchor.setText(value);
		if (isEnable() && !context.isDesable()) {
			panel.setStyleName("editTable_enable_anchor");
		} else {
			panel.setStyleName("editTable_disable_anchor");
		}
	}

	@Override
	public IsWidget getWidget(final RenderContext<T> context) {
		SimplePanel panel = new SimplePanel();
		Anchor anchor = new Anchor();
		configure(anchor);
		anchor.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (isEnable(context.getRow()) && !context.isDesable()) {
					AnchorEditColumn.this.onClick(context.getRow());
				}
			}
		});
		panel.add(anchor);
		return panel;
	}

	protected abstract boolean isEnable(T row);

	protected boolean isEnable() {
		return true;
	}

	protected abstract void onClick(T row);

	protected abstract String getValue(T row);

	protected void configure(Anchor anchor) {
		anchor.addStyleName("anchor");
	}

	/**
	 * set the Currency name to title.
	 * 
	 * @param name
	 * @return {@link String}
	 */
	protected String getColumnNameWithCurrency(String name) {
		String currencyName = Accounter.getCompany().getPrimaryCurrency()
				.getFormalName();
		if (currencyProvider.getTransactionCurrency() != null) {
			currencyName = currencyProvider.getTransactionCurrency()
					.getFormalName();
		}
		return messages.nameWithCurrency(name, currencyName);
	}
}
