package com.nitya.accounter.web.client.ui.forms;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Node;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ValidationResult;
import com.nitya.accounter.web.client.externalization.AccounterMessages;

public abstract class FormItem<T> extends FlowPanel implements HasEnabled {

	@Override
	public boolean isEnabled() {
		Widget widget = getMainWidget();
		if (widget instanceof HasEnabled) {
			return ((HasEnabled) widget).isEnabled();
		}
		return true;
	}

	public FormItem(String title, String styleName) {

		getElement().addClassName("formItem");

		if (styleName != null) {
			getElement().addClassName(styleName);
		}

		if (title != null) {
			this.title = title;
		}
		addLabel();
	}

	public void setStyle(String style) {
		getElement().addClassName(style);
	}

	@Override
	public void setEnabled(boolean enabled) {
		if (!enabled) {
			getElement().addClassName("disabled");
		} else {
			getElement().removeClassName("disabled");
		}
		Widget widget = getMainWidget();
		if (widget instanceof HasEnabled) {
			((HasEnabled) widget).setEnabled(enabled);
		}
	}

	protected static final AccounterMessages messages = Global.get().messages();

	private String title;
	// private String name;
	private T value;
	private List<Validator> validator = new ArrayList<Validator>();
	private boolean required;
	private Label label;

	private boolean showTitle = true;
	private T defaultValue;
	// private int columnSpan = 1;
	private boolean isHighlighted = false;
	private String titleStyleName;

	// private boolean ishelp = false;

	public T getValue() {
		return this.value;
	}

	public String getTitle() {
		return title;
	}

	public boolean isShowTitle() {
		return showTitle;
	}

	public String getDisplayValue() {
		return this.value.toString();

	}

	// public void setColSpan(int colSpan) {
	// this.columnSpan = colSpan;
	// }

	public void setShowTitle(boolean showTitle) {
		this.showTitle = showTitle;
	}

	public void highlight() {
		getMainWidget().addStyleName("highlightedFormItem");
		// getMainWidget().setTitle(messages.invalidValue());
		this.isHighlighted = true;

	}

	public boolean isHighLighted() {
		return isHighlighted;
	}

	// public String getValueField() {
	// return this.name;
	// }

	public void setTitle(String string) {
		this.title = string;
		if (label != null) {
			label.setText(this.title);
		}
	}

	public void setWidth(int width) {

	}

	public void setWidth(String width) {
	}

	// public void setName(String name) {
	// this.name = name;
	// }

	public void setValue(T value) {
		this.value = value;
	}

	public void setValidators(Validator validate) {
		this.getValidator().add(validate);
	}

	public void setRequired(boolean required) {
		if (required) {
			this.getValidator().add(new RequiredFieldValidator());
			if (label != null && !this.required) {
				label.addStyleName("requiredField");
				Element ele = DOM.createSpan();
				ele.setInnerText("*");
				ele.addClassName("star");
				DOM.appendChild(label.getElement(), ele);
			}
		} else {
			this.getValidator().clear();
			if (this.required) {
				removeLabelStar();
			}
		}
		this.required = required;
	}

	private void removeLabelStar() {
		if (label == null)
			return;
		else {
			Node parentNode = null;
			if (label.getElement().hasChildNodes())
				parentNode = label.getElement().getFirstChild().getParentNode();
			if (parentNode != null) {
				parentNode.removeChild(label.getElement().getChild(1));
			}
		}
	}

	public void show() {
		if (label != null) {
			label.setVisible(true);
		}
		getMainWidget().setVisible(true);
		this.setVisible(true);
	}

	public Label getLabelWidget() {
		return this.label;
	}

	public void hide() {
		if (label != null) {
			label.setVisible(false);
		}
		getMainWidget().setVisible(false);
	}

	public void addClickHandler(ClickHandler handler) {
		// getMainWid/get().addClickHandler(handler);

	}// TODO Auto-generated method stub

	public void setDefaultValue(T value) {
		defaultValue = value;
	}

	public void setToolTip(String toolTip) {

	}

	// public String getName() {
	// return this.name;
	//
	// }

	// void addWidgets(StyledPanel parent) {
	//
	// addLabelWidget(parent);
	// if (ishelp) {
	// addHelpImageWidget(parent);
	// } else {
	// parent.add(getMainWidget());
	// }
	// }

	// private void addHelpImageWidget(StyledPanel parent) {
	// StyledPanel hPanel = new StyledPanel();
	// hPanel.add(getMainWidget());
	// Image helpImg = new Image(Accounter.getFinanceImages().helpIcon());
	// helpImg.getElement().getStyle().setCursor(Cursor.POINTER);
	// helpImg.addMouseUpHandler(new MouseUpHandler() {
	//
	// @Override
	// public void onMouseUp(MouseUpEvent event) {
	// displayHelpMessage(event);
	// }
	// });
	// hPanel.add(helpImg);
	// parent.add(hPanel);
	// // Image helpImg = new Image("/images/icons/help.png");
	// // parent.add(helpImg,0);
	//
	// }

	// public void setHelpInformation(Boolean isHelp) {
	// this.ishelp = false;
	// }

	public abstract Widget getMainWidget();

	private void addLabel() {
		if (showTitle) {
			label = new Label(this.title);
			label.addStyleName("NoWrapping");
			if (this.titleStyleName != null && !this.titleStyleName.equals(""))
				label.addStyleName(titleStyleName);
			if (required) {
				label.addStyleName("requiredField");
				Element ele = DOM.createSpan();
				ele.setInnerText("*");
				ele.addClassName("star");
				DOM.appendChild(label.getElement(), ele);
			}
			this.add(label);
		}
	}

	public void setVisible(boolean visible) {
		super.setVisible(visible);
	}

	public void addChangeHandler(ChangeHandler changeHandler) {

	}

	public void addChangedHandler(ChangeHandler changeHandler) {

	}

	public void addFocusHandler(FocusHandler focusHandler) {

	}

	public void addBlurHandler(BlurHandler blurHandler) {

	}

	public void addKeyBoardHandler(KeyPressHandler keyPressHandler) {

	}

	public List<Validator> getValidator() {
		return validator;
	}

	public void setValidator(List<Validator> validator) {
		this.validator = validator;
	}

	public T getDefaultValue() {
		return defaultValue;
	}

	public boolean validate() {
		if (this.getValidator() != null) {
			int matchingStatus = 0;
			for (Validator validator : this.getValidator()) {

				if (validator.validate(this)) {
					matchingStatus++;
				}

			}
			if (matchingStatus == this.getValidator().size()) {
				return true;

			} else
				highlight();

			return false;

		}
		return true;

	}

	public void showValidated() {

		getMainWidget().removeStyleName("highlightedFormItem");
		getMainWidget().setTitle("");

	}

	/**
	 * call this method to set focus to Item
	 */
	public void setFocus() {
		Widget widget = getMainWidget();
		if (widget instanceof FocusWidget) {
			((FocusWidget) widget).setFocus(true);
		}
	}

	public static ValidationResult validate(FormItem<?>... items) {
		ValidationResult result = new ValidationResult();
		for (FormItem<?> item : items) {
			if (!item.validate()) {
				result.addError(item, messages.pleaseEnter(item.getTitle()));
			}
		}
		return result;
	}

	// public String helpMessage = messages.help();
	// public PopupPanel popupPanel;

	// public void displayHelpMessage(MouseUpEvent event) {
	// popupPanel = new PopupPanel(true);
	// Widget source = (Widget) event.getSource();
	// int x = source.getAbsoluteLeft() + 10;
	// int y = source.getAbsoluteTop() + 10;
	// popupPanel.setHeight("100px");
	// popupPanel.setWidth("100px");
	// popupPanel.setAutoHideEnabled(true);
	// popupPanel.add(helpContent());
	// popupPanel.setPopupPosition(x, y);
	// popupPanel.show();
	//
	// }

}
