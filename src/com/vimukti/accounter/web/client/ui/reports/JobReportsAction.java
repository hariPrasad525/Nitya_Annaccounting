package com.vimukti.accounter.web.client.ui.reports;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

public class JobReportsAction extends Action {

	public static final int TYPE_ESTIMATES_BY_JOB = 1;
	public static final int TYPE_UNBILLED_COST = 2;
	public static final int TYPE_PROFITABILITY_SUMMARY = 3;
	public static final int TYPE_PROFITABILITY_DETAILS = 4;
	public static final int TYPE_PROFIT_AND_LOSS = 5;

	private int type;

	public JobReportsAction(int type) {
		super();
		this.type = type;
		this.catagory = messages.report();
	}

	@Override
	public void run() {
		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				AbstractReportView report = null;

				switch (type) {
				case TYPE_ESTIMATES_BY_JOB:
					report = new EstimatesByJobReport();
					break;
				case TYPE_UNBILLED_COST:
					report = new UnBilledCostsByJobReport();
					break;
				case TYPE_PROFITABILITY_SUMMARY:
					report = new JobProfitabilitySummaryReport();
					break;
				case TYPE_PROFITABILITY_DETAILS:
					report = new JobProfitabilityDetailReport();
					break;
				case TYPE_PROFIT_AND_LOSS:
					report = new ProfitAndLossByLocationReport(3);
					break;
				}

				if (report != null) {
					MainFinanceWindow.getViewManager().showView(report, data,
							isDependent, JobReportsAction.this);
				}
			}

			public void onFailure(Throwable e) {
				Accounter.showError(Global.get().messages()
						.unableToshowtheview());
			}
		});

	}

	@Override
	public ImageResource getBigImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().reports();
	}

	@Override
	public String getHistoryToken() {
		switch (type) {
		case TYPE_ESTIMATES_BY_JOB:
			return "Estimatebyjob";
		case TYPE_UNBILLED_COST:
			return "UnbilledCostsByJob";
		case TYPE_PROFITABILITY_SUMMARY:
			return "jobProfitabilitySummary";
		case TYPE_PROFITABILITY_DETAILS:
			return "jobProfitabilityDetail";
		case TYPE_PROFIT_AND_LOSS:
			return "ProfitAndLossbyJob";
		}
		return null;
	}

	@Override
	public String getHelpToken() {
		switch (type) {
		case TYPE_ESTIMATES_BY_JOB:
			return "Estimate-by-job";
		case TYPE_UNBILLED_COST:
			return "Unbilled-Costs-By-Job";
		case TYPE_PROFITABILITY_SUMMARY:
			return "Job-Profitability-Summary";
		case TYPE_PROFITABILITY_DETAILS:
			return "Job-Profitability-Detail";
		case TYPE_PROFIT_AND_LOSS:
			return "Profit-Loss-by-Job";
		}
		return null;
	}

	@Override
	public String getText() {
		switch (type) {
		case TYPE_ESTIMATES_BY_JOB:
			return messages.estimatesbyJob();
		case TYPE_UNBILLED_COST:
			return messages.unbilledCostsByJob();
		case TYPE_PROFITABILITY_SUMMARY:
			return messages.jobProfitabilitySummary();
		case TYPE_PROFITABILITY_DETAILS:
			return messages.jobProfitabilityDetail();
		case TYPE_PROFIT_AND_LOSS:
			return messages.profitAndLossByJob();
		}
		return null;
	}

	public static JobReportsAction estimatesByJob() {
		return new JobReportsAction(TYPE_ESTIMATES_BY_JOB);
	}

	public static JobReportsAction unbilledCost() {
		return new JobReportsAction(TYPE_UNBILLED_COST);
	}

	public static JobReportsAction profitabilitySummary() {
		return new JobReportsAction(TYPE_PROFITABILITY_SUMMARY);
	}

	public static JobReportsAction profitabilityDetail() {
		return new JobReportsAction(TYPE_PROFITABILITY_DETAILS);
	}

	public static JobReportsAction profitAndLoss() {
		return new JobReportsAction(TYPE_PROFIT_AND_LOSS);
	}

}
