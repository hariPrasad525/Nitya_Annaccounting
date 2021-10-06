package com.nitya.accounter.web.client.ui.vendors;

import com.nitya.accounter.web.client.AccounterAsyncCallback;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.core.ValidationResult;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.StyledPanel;
import com.nitya.accounter.web.client.ui.banking.WriteChecksAction;
import com.nitya.accounter.web.client.ui.core.BaseDialog;
import com.nitya.accounter.web.client.ui.core.ViewManager;
import com.nitya.accounter.web.client.ui.forms.DynamicForm;
import com.nitya.accounter.web.client.ui.forms.RadioGroupItem;

public class SelectExpenseType extends BaseDialog {
	RadioGroupItem typeRadio;
	private final String CHECK = messages.check();
	private final String CREDIT_CARD = messages.creditCard();

	private final String CASH = messages.cash();
	private final String EMPLOYEE = messages.employee();

	// private ViewConfiguration configuration;

	public SelectExpenseType() {
		super(messages.recordExpenses(), "");
		this.getElement().setId("SelectExpenseType");
		setText(messages.recordExpenses());
		createControls();
	}

	public SelectExpenseType(AccounterAsyncCallback<IAccounterCore> callBack) {
		super(messages.recordExpenses(), "");
		this.getElement().setId("SelectExpenseType");
		setText(messages.recordExpenses());
		createControls();
		ViewManager.getInstance().showDialog(this);
	}

	private void createControls() {
		typeRadio = new RadioGroupItem();
		typeRadio.setShowTitle(false);
		if (Global.get().preferences().isHaveEpmloyees()
				&& Global.get().preferences().isTrackEmployeeExpenses()) {
			typeRadio.setValue(EMPLOYEE, CREDIT_CARD, CASH);
		} else {
			typeRadio.setValue(CREDIT_CARD, CASH);
		}

		// setting the default value
		typeRadio.setValue(new String(CREDIT_CARD));

		DynamicForm typeForm = new DynamicForm("typeForm");
		typeForm.setWidth("100%");
		// typeForm.setIsGroup(true);
		//
		// typeForm.setGroupTitle(messages
		// .selectHowYouPaidForExpense());
		typeForm.add(typeRadio);

		StyledPanel mainVLay = new StyledPanel("mainVLay");
		// mainVLay.setSize("100%", "100%");
		mainVLay.add(typeForm);

		// okbtn.setWidth("60px");
		// cancelBtn.setWidth("60px");

		setBodyLayout(mainVLay);
		// setWidth("300px");

	}

	public void setFocus() {
		cancelBtn.setFocus(true);
	}

	@Override
	protected ValidationResult validate() {
		ValidationResult result = new ValidationResult();
		String radio = typeRadio.getValue().toString();
		if (!radio.equals(EMPLOYEE) && !radio.equals(CHECK)
				&& !radio.equals(CREDIT_CARD) && !radio.equals(CASH)
				&& !radio.equals(EMPLOYEE)) {
			result.addError(this, messages.pleaseSelectExpenseType());
		}
		return result;
	}

	@Override
	protected boolean onOK() {
		if (typeRadio.getValue() != null) {
			String radio = typeRadio.getValue().toString();
			if (radio.equals(EMPLOYEE)) {
				new EmployeeExpenseAction().run(null, false);
			} else if (radio.equals(CHECK)) {
				new WriteChecksAction().run(null, false);
			} else if (radio.equals(CREDIT_CARD)) {
				new CreditCardExpenseAction().run(null, false);
			} else if (radio.equals(CASH)) {
				new CashExpenseAction().run(null, false);
			} else {
				Accounter.showError(messages.pleaseSelectExpenseType());
			}

		}
		this.removeFromParent();
		return true;
	}

	@Override
	public boolean isViewDialog() {
		// TODO Auto-generated method stub
		return false;
	}
}
