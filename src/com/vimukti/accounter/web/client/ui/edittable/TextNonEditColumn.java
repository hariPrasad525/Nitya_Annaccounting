package com.vimukti.accounter.web.client.ui.edittable;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;

public class TextNonEditColumn<R> extends EditColumn<R> {

	@Override
	public int getWidth() {
		return 200;
	}

	@Override
	public int insertNewLineNumber() {
		return 1;
	}

	@Override
	public void render(IsWidget widget, RenderContext<R> context) {
		Label label = (Label) widget;
		label.setText(getValueAsString(context.getRow()));
	}

	@Override
	public IsWidget getWidget(RenderContext<R> context) {
		Label label = new Label();
		label.setText(getValueAsString(context.getRow()));
		return label;
	}

	public String getTextValue(R row) {
		return "";
	}

	@Override
	public String getValueAsString(R row) {
		return this.getTextValue(row);
	}

}
