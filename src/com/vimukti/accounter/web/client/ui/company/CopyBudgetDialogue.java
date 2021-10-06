package com.vimukti.accounter.web.client.ui.company;

import java.util.List;

import com.vimukti.accounter.web.client.core.ClientBudget;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;

public class CopyBudgetDialogue extends BaseDialog {

	SelectCombo selectBudget;
	List<ClientBudget> budgetList;

	ClientBudget budgetName;

	public CopyBudgetDialogue(String title, String desc) {
		super(title, desc);
		this.getElement().setId("CopyBudgetDialogue");
		createControls();
	}

	public CopyBudgetDialogue(String budgetTitle, String string,
			List<ClientBudget> listData) {
		super(budgetTitle, string);
		this.getElement().setId("CopyBudgetDialogue");
		budgetList = listData;
		createControls();
	}

	private void createControls() {
		StyledPanel verticalPanel = new StyledPanel("verticalPanel");

		selectBudget = new SelectCombo(messages.CopyfromExistingBudget());

		for (ClientBudget budget : budgetList) {
			selectBudget.addComboItem(budget.getBudgetName());
		}

		selectBudget
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						for (ClientBudget budget : budgetList) {
							if (selectBudget.getSelectedValue().equals(
									budget.getBudgetName())) {
								budgetName = budget;
								break;
							}
						}

					}

				});

		DynamicForm budgetInfoForm = UIUtils.form(messages
				.chartOfAccountsInformation());
		// budgetInfoForm.setWidth("100%");

		budgetInfoForm.add(selectBudget);

		StyledPanel horizontalPanel = new StyledPanel("horizontalPanel");
		horizontalPanel.add(budgetInfoForm);

		verticalPanel.add(horizontalPanel);

		setBodyLayout(verticalPanel);
	}

	@Override
	protected boolean onCancel() {
		return true;
	}

	@Override
	protected boolean onOK() {

		if (budgetName != null)
			getCallback().actionResult(budgetName);
		return true;
	}

	@Override
	public void setFocus() {
		selectBudget.setFocus();

	}

	@Override
	public boolean isViewDialog() {
		// TODO Auto-generated method stub
		return false;
	}
}
