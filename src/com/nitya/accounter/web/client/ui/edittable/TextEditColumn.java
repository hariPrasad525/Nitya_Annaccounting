package com.nitya.accounter.web.client.ui.edittable;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;

public abstract class TextEditColumn<T> extends EditColumn<T> {

	private boolean updateFromGUI = false;
	private boolean isEnable = true;

	public TextEditColumn(boolean updateFromGUI) {
		this.updateFromGUI = updateFromGUI;
	}

	public TextEditColumn() {
		this(false);
	}

	protected TextBoxBase createWidget() {
		return new TextBox();
	}

	@Override
	public void updateFromGUI(IsWidget widget, T row) {
		if (updateFromGUI) {
			TextBoxBase box = (TextBoxBase) widget;
			String newValue = box.getText().trim();
			setValue(row, newValue);
		}
	}

	@Override
	public void render(IsWidget widget, RenderContext<T> context) {
		TextBoxBase box = (TextBoxBase) widget;
		String value = getValue(context.getRow());
		box.setEnabled(isEnable() && !context.isDesable());
		box.setText(value);
	}

	protected boolean isEnable() {
		return isEnable;
	}

	protected abstract String getValue(T row);

	protected abstract void setValue(T row, String value);

	@Override
	public IsWidget getWidget(final RenderContext<T> context) {
		final TextBoxBase textBox = createWidget();
		configure(textBox);
		textBox.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				String prevValue = getValue(context.getRow());
				if (prevValue == null) {
					prevValue = "";
				}
				String newValue = textBox.getText().trim();
				if (!prevValue.equals(newValue)) {
					setValue(context.getRow(), newValue);
				}

			}
		});
		return textBox;
	}

	protected void configure(TextBoxBase textBox) {
		textBox.addStyleName("textEdit");
	}

	@Override
	public int getWidth() {
		return -1;
	}

	public void setEnable(boolean b) {
		this.isEnable = b;
	}

}
