package com.vimukti.accounter.web.client.ui.settings;

import java.util.List;

import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.TextDecoration;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratedTabPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.forms.AmountLabel;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.LabelItem;
import com.vimukti.accounter.web.client.util.DayAndMonthUtil;

public class ConversionBalancesView extends BaseView {
	private HTML superHeaderHtml, headerHtml, footerCommentHtml;
	private StyledPanel headerPanel, bodyPanel, mainPanel, tabBodyPanel,
			footerPanel;
	private StyledPanel headerButtonPanel, footerButtonPanel,
			addNewButtonPanel;
	private Button addComparativeBalancesButton, conversionDateButton,
			saveButton, cancelButton, addNewButton;
	private LabelItem removeZeroBalance, showAllAccounts;
	private DynamicForm debit_creditForm, labelsForm, adjustmentsForm;
	private AmountLabel creditLabel, debitLabel, adjustmentLabel;
	private DecoratedTabPanel tabPanel;
	// private ListGrid grid;
	private Label account, debit, credit;
	private FlexTable tabFlexTable;

	public ConversionBalancesView(String endingDate, String year) {
		if (endingDate != null && year != null) {
			addNewTab(endingDate, year);
		}
	}

	@Override
	public void init() {
		super.init();
		this.getElement().setId("ConversionBalancesView");
		createControls();
	}

	public void addNewTab(String endingDate, String year) {
		createControls();
		tabPanel.add(getBodyControls(), DayAndMonthUtil.january() + year
				+ " _ " + endingDate);
	}

	private void createControls() {
		initControls();

		superHeaderHtml.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				new GeneralSettingsAction().run(null, false);
			}
		});

		conversionDateButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				try {
					new ConversionDateAction().run(null, false);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});

		bodyPanel.add(headerButtonPanel);
		try {
			tabPanel = new DecoratedTabPanel();
			tabPanel.add(getBodyControls(), Accounter.getCompany()
					.getCurrentFiscalYearStartDate().toString()
					+ " _ "
					+ Accounter.getCompany().getCurrentFiscalYearEndDate()
							.toString());

			bodyPanel.add(tabPanel);
		} catch (Exception e) {
			System.out.println(e.toString());
		}

		mainPanel.add(headerPanel);
		mainPanel.add(bodyPanel);

		add(mainPanel);
	}

	private void initControls() {
		headerPanel = new StyledPanel("headerPanel");
		bodyPanel = new StyledPanel("bodyPanel");
		mainPanel = new StyledPanel("mainPanel");

		headerButtonPanel = new StyledPanel("headerButtonPanel");
		// grid=new
		superHeaderHtml = new HTML("");
		superHeaderHtml.addMouseOverHandler(new MouseOverHandler() {

			@Override
			public void onMouseOver(MouseOverEvent event) {
				superHeaderHtml.getElement().getStyle()
						.setCursor(Cursor.POINTER);
				superHeaderHtml.getElement().getStyle()
						.setTextDecoration(TextDecoration.UNDERLINE);
			}
		});
		superHeaderHtml.addMouseOutHandler(new MouseOutHandler() {

			@Override
			public void onMouseOut(MouseOutEvent event) {
				superHeaderHtml.getElement().getStyle()
						.setTextDecoration(TextDecoration.NONE);
			}
		});
		headerHtml = new HTML(messages.conversionBalanaces());

		addComparativeBalancesButton = new Button(
				messages.addComparativeButton());
		conversionDateButton = new Button(messages.conversionDate());
		conversionDateButton.addStyleName("company-settings-html");
		headerButtonPanel.add(addComparativeBalancesButton);
		headerButtonPanel.add(conversionDateButton);
		headerPanel.add(superHeaderHtml);
		headerPanel.add(headerHtml);

	}

	private StyledPanel getBodyControls() {
		tabBodyPanel = new StyledPanel("tabBodyPanel");
		addNewButtonPanel = new StyledPanel("addNewButtonPanel");
		footerButtonPanel = new StyledPanel("footerButtonPanel");
		addNewButton = new Button(messages.addNewLine());
		saveButton = new Button(messages.save());
		cancelButton = new Button(messages.cancel());
		footerPanel = new StyledPanel("footerPanel");
		debit_creditForm = new DynamicForm("debit_creditForm");
		debitLabel = new AmountLabel(messages.totalDebits());
		creditLabel = new AmountLabel(messages.totalCredits());
		adjustmentLabel = new AmountLabel(messages.adjustments());
		tabFlexTable = new FlexTable();
		account = new Label(messages.Account());
		credit = new Label(messages.credit());
		debit = new Label(messages.debit());

		tabFlexTable.setWidget(0, 0, account);
		tabFlexTable.setWidget(0, 1, credit);
		tabFlexTable.setWidget(0, 2, debit);

		adjustmentLabel.setAmount(debitLabel.getAmount()
				- creditLabel.getAmount());
		removeZeroBalance = new LabelItem(messages.removeZeroBalances(),
				"removeZeroBalance");
		removeZeroBalance.addStyleName("falseHyperlink");
		removeZeroBalance.setShowTitle(false);
		removeZeroBalance.setEnabled(!isInViewMode());
		showAllAccounts = new LabelItem(messages.showAllAccounts(),
				"showAllAccounts");
		showAllAccounts.addStyleName("falseHyperlink");
		showAllAccounts.setShowTitle(false);
		showAllAccounts.setEnabled(!isInViewMode());
		footerCommentHtml = new HTML(messages.footerComment());
		footerCommentHtml.addStyleName("footer_comment");

		labelsForm = new DynamicForm("labelsForm");
		labelsForm.add(removeZeroBalance, showAllAccounts);
		addNewButtonPanel.add(addNewButton);
		addNewButtonPanel.add(labelsForm);

		debit_creditForm.add(debitLabel, creditLabel);

		adjustmentsForm = new DynamicForm("adjustmentsForm");
		adjustmentsForm.add(adjustmentLabel);

		footerButtonPanel.add(saveButton);
		footerButtonPanel.add(cancelButton);

		footerPanel.add(addNewButtonPanel);
		footerPanel.add(debit_creditForm);
		footerPanel.add(adjustmentsForm);
		footerPanel.add(footerCommentHtml);

		tabBodyPanel.add(tabFlexTable);
		tabBodyPanel.add(footerPanel);
		tabBodyPanel.add(footerButtonPanel);

		return tabBodyPanel;

	}

	@Override
	public void deleteFailed(AccounterException caught) {

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {

	}

	@Override
	protected String getViewTitle() {
		return messages.conversionBalance();
	}

	@Override
	public void fitToSize(int height, int width) {

	}

	@Override
	public List getForms() {
		return null;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean canDelete() {
		return false;
	}

	@Override
	protected boolean canVoid() {
		return false;
	}
}
