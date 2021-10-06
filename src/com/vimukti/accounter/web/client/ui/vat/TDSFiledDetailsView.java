package com.vimukti.accounter.web.client.ui.vat;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTDSChalanDetail;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.ButtonBar;
import com.vimukti.accounter.web.client.ui.core.CancelButton;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.LabelItem;

public class TDSFiledDetailsView extends BaseView {

	int formType;
	String ackNo;
	int financialYearStart;
	int financialYearEnd;
	int quater;
	long dateOfFiled;

	LabelItem formNoLabel;
	LabelItem ackNoLabel;
	LabelItem dateOfFiledLabel;
	LabelItem financialYearLabel;
	LabelItem assesmentYearLabel;
	LabelItem quaterLabel;

	StyledPanel mainPanel = new StyledPanel("mainPanel");

	public TDSFiledDetailsView(int formType, String ackNo,
			int financialYearStart, int financialYearEnd, int quater,
			long dateOfFiled) {
		super();
		this.formType = formType;
		this.ackNo = ackNo;
		this.financialYearStart = financialYearStart;
		this.financialYearEnd = financialYearEnd;
		this.quater = quater;
		this.dateOfFiled = dateOfFiled;
	}

	@Override
	public void init() {
		super.init();
		this.getElement().setId("TDSFiledDetailsView");
		createControls();
//		setSize("100%", "100%");
	}

	@Override
	public void initData() {
		Accounter.createHomeService().getTDSChallansForAckNo(ackNo,
				new AccounterAsyncCallback<ArrayList<ClientTDSChalanDetail>>() {

					@Override
					public void onException(AccounterException exception) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onResultSuccess(
							ArrayList<ClientTDSChalanDetail> result) {
						if (result != null) {
							showItems(result);
						}
					}
				});
	}

	protected void showItems(ArrayList<ClientTDSChalanDetail> challans) {

		for (ClientTDSChalanDetail challan : challans) {
			StyledPanel panel = new StyledPanel("panel");
			panel.add(new TDSChallanItem(challan));
			mainPanel.add(panel);
		}

	}

	private void createControls() {

		formNoLabel = new LabelItem(getFormTypes().get(formType - 1),
				"formnolabel");
		ackNoLabel = new LabelItem(messages.acknowledgmentNo(), "ackNoLabel");
		ackNoLabel.setValue(ackNo);
		dateOfFiledLabel = new LabelItem(messages.dateOfFiled(),
				"dateOfFiledLabel");
		dateOfFiledLabel.setValue(UIUtils
				.getDateByCompanyType(new ClientFinanceDate(dateOfFiled)));
		financialYearLabel = new LabelItem(messages.financialYear(),
				"financialYearLabel");
		financialYearLabel.setValue(Integer.toString(financialYearStart) + "-"
				+ Integer.toString(financialYearEnd));
		assesmentYearLabel = new LabelItem(messages.assessmentYear(),
				"assesmentYearLabel");
		assesmentYearLabel.setValue(Integer.toString(financialYearStart + 1)
				+ "-" + Integer.toString(financialYearEnd + 1));
		quaterLabel = new LabelItem(messages.period(), "quaterLabel");
		quaterLabel
				.setValue((String) getFinancialQuatersList().get(quater - 1));

		DynamicForm filedForm = new DynamicForm("filedForm");
		filedForm.add(ackNoLabel, dateOfFiledLabel, financialYearLabel,
				assesmentYearLabel, quaterLabel);
		mainPanel.add(formNoLabel.getMainWidget());
		mainPanel.add(filedForm);

		this.add(mainPanel);
	}

	private List<String> getFormTypes() {
		ArrayList<String> list = new ArrayList<String>();

		list.add(messages.form26Q());
		list.add(messages.form27Q());
		list.add(messages.form27EQ());
		return list;
	}

	@Override
	public void deleteFailed(AccounterException caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {
		// TODO Auto-generated method stub

	}

	@Override
	protected String getViewTitle() {
		return messages.tdsFiledDetails();
	}

	@Override
	public List getForms() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void createButtons() {
		this.cancelButton = new CancelButton(this);
		addButton(cancelButton);
	}

}
