package com.vimukti.accounter.web.client.ui;

import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.vimukti.accounter.web.client.IAccounterCRUDServiceAsync;
import com.vimukti.accounter.web.client.IAccounterCompanyInitializationServiceAsync;
import com.vimukti.accounter.web.client.IAccounterExportCSVServiceAsync;
import com.vimukti.accounter.web.client.IAccounterGETServiceAsync;
import com.vimukti.accounter.web.client.IAccounterHomeViewServiceAsync;
import com.vimukti.accounter.web.client.IAccounterPayrollServiceAsync;
import com.vimukti.accounter.web.client.IAccounterReportServiceAsync;
import com.vimukti.accounter.web.client.IAccounterWindowsHomeServiceAsync;
import com.vimukti.accounter.web.client.rpc.WebSocketRpcRequestBuilder;
import com.vimukti.accounter.web.client.translate.TranslateServiceAsync;

public class WebsocketAccounterRPCInitialiser extends AccounterRPCInitialiser {

	public IAccounterCompanyInitializationServiceAsync createCompanyInitializationService() {
		IAccounterCompanyInitializationServiceAsync cIService = super
				.createCompanyInitializationService();
		((ServiceDefTarget) cIService)
				.setRpcRequestBuilder(new WebSocketRpcRequestBuilder());

		return cIService;

	}

	@Override
	public IAccounterPayrollServiceAsync createPayrollService() {
		IAccounterPayrollServiceAsync payrollService = super
				.createPayrollService();
		((ServiceDefTarget) payrollService)
				.setRpcRequestBuilder(new WebSocketRpcRequestBuilder());
		return payrollService;
	}

	public IAccounterCRUDServiceAsync createCRUDService() {
		IAccounterCRUDServiceAsync crudService = super.createCRUDService();
		((ServiceDefTarget) crudService)
				.setRpcRequestBuilder(new WebSocketRpcRequestBuilder());

		return crudService;

	}

	public IAccounterGETServiceAsync createGETService() {
		IAccounterGETServiceAsync getService = super.createGETService();
		((ServiceDefTarget) getService)
				.setRpcRequestBuilder(new WebSocketRpcRequestBuilder());
		return getService;
	}

	public IAccounterHomeViewServiceAsync createHomeService() {
		IAccounterHomeViewServiceAsync homeViewService = super
				.createHomeService();
		((ServiceDefTarget) homeViewService)
				.setRpcRequestBuilder(new WebSocketRpcRequestBuilder());
		return homeViewService;
	}

	public IAccounterExportCSVServiceAsync createExportCSVService() {
		IAccounterExportCSVServiceAsync exportCSVService = super
				.createExportCSVService();
		((ServiceDefTarget) exportCSVService)
				.setRpcRequestBuilder(new WebSocketRpcRequestBuilder());
		return exportCSVService;
	}

	public IAccounterReportServiceAsync createReportService() {
		IAccounterReportServiceAsync reportService = super
				.createReportService();
		((ServiceDefTarget) reportService)
				.setRpcRequestBuilder(new WebSocketRpcRequestBuilder());
		return reportService;
	}

	public TranslateServiceAsync createTranslateService() {
		TranslateServiceAsync translateService = super.createTranslateService();
		((ServiceDefTarget) translateService)
				.setRpcRequestBuilder(new WebSocketRpcRequestBuilder());
		return translateService;
	}

	public IAccounterWindowsHomeServiceAsync createWindowsRPCService() {
		IAccounterWindowsHomeServiceAsync windowsService = super
				.createWindowsRPCService();
		((ServiceDefTarget) windowsService)
				.setRpcRequestBuilder(new WebSocketRpcRequestBuilder());

		return windowsService;
	}
}
