package com.vimukti.accounter.web.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.vimukti.accounter.web.client.IAccounterCRUDService;
import com.vimukti.accounter.web.client.IAccounterCRUDServiceAsync;
import com.vimukti.accounter.web.client.IAccounterCompanyInitializationService;
import com.vimukti.accounter.web.client.IAccounterCompanyInitializationServiceAsync;
import com.vimukti.accounter.web.client.IAccounterExportCSVService;
import com.vimukti.accounter.web.client.IAccounterExportCSVServiceAsync;
import com.vimukti.accounter.web.client.IAccounterGETService;
import com.vimukti.accounter.web.client.IAccounterGETServiceAsync;
import com.vimukti.accounter.web.client.IAccounterHomeViewService;
import com.vimukti.accounter.web.client.IAccounterHomeViewServiceAsync;
import com.vimukti.accounter.web.client.IAccounterPayrollService;
import com.vimukti.accounter.web.client.IAccounterPayrollServiceAsync;
import com.vimukti.accounter.web.client.IAccounterReportService;
import com.vimukti.accounter.web.client.IAccounterReportServiceAsync;
import com.vimukti.accounter.web.client.IAccounterWindowsHomeService;
import com.vimukti.accounter.web.client.IAccounterWindowsHomeServiceAsync;
import com.vimukti.accounter.web.client.translate.TranslateService;
import com.vimukti.accounter.web.client.translate.TranslateServiceAsync;

public class AccounterRPCInitialiser {

	public IAccounterCompanyInitializationServiceAsync createCompanyInitializationService() {
		IAccounterCompanyInitializationServiceAsync cIService = (IAccounterCompanyInitializationServiceAsync) GWT
				.create(IAccounterCompanyInitializationService.class);
		((ServiceDefTarget) cIService)
				.setServiceEntryPoint(Accounter.CI_SERVICE_ENTRY_POINT);

		return cIService;

	}

	public IAccounterCRUDServiceAsync createCRUDService() {
		IAccounterCRUDServiceAsync crudService = (IAccounterCRUDServiceAsync) GWT
				.create(IAccounterCRUDService.class);
		((ServiceDefTarget) crudService)
				.setServiceEntryPoint(Accounter.CRUD_SERVICE_ENTRY_POINT);

		return crudService;

	}

	public IAccounterGETServiceAsync createGETService() {
		IAccounterGETServiceAsync getService = (IAccounterGETServiceAsync) GWT
				.create(IAccounterGETService.class);
		((ServiceDefTarget) getService)
				.setServiceEntryPoint(Accounter.GET_SERVICE_ENTRY_POINT);
		return getService;
	}

	public IAccounterHomeViewServiceAsync createHomeService() {
		IAccounterHomeViewServiceAsync homeViewService = (IAccounterHomeViewServiceAsync) GWT
				.create(IAccounterHomeViewService.class);
		((ServiceDefTarget) homeViewService)
				.setServiceEntryPoint(Accounter.HOME_SERVICE_ENTRY_POINT);
		return homeViewService;
	}

	public IAccounterExportCSVServiceAsync createExportCSVService() {
		IAccounterExportCSVServiceAsync exportCSVService = (IAccounterExportCSVServiceAsync) GWT
				.create(IAccounterExportCSVService.class);
		((ServiceDefTarget) exportCSVService)
				.setServiceEntryPoint(Accounter.EXPORT_CSV_SERVICE_ENTRY_POINT);
		return exportCSVService;
	}

	public IAccounterReportServiceAsync createReportService() {
		IAccounterReportServiceAsync reportService = (IAccounterReportServiceAsync) GWT
				.create(IAccounterReportService.class);
		((ServiceDefTarget) reportService)
				.setServiceEntryPoint(Accounter.REPORT_SERVICE_ENTRY_POINT);
		return reportService;
	}

	public TranslateServiceAsync createTranslateService() {
		TranslateServiceAsync translateService = (TranslateServiceAsync) GWT
				.create(TranslateService.class);
		((ServiceDefTarget) translateService)
				.setServiceEntryPoint(Accounter.TRANSLATE_SERVICE_ENTRY_POINT);
		return translateService;
	}

	public IAccounterWindowsHomeServiceAsync createWindowsRPCService() {
		IAccounterWindowsHomeServiceAsync windowsService = (IAccounterWindowsHomeServiceAsync) GWT
				.create(IAccounterWindowsHomeService.class);
		((ServiceDefTarget) windowsService)
				.setServiceEntryPoint(Accounter.WINDOW_RPC_SERVICE_ENTRY_POINT);
		return windowsService;
	}

	public IAccounterPayrollServiceAsync createPayrollService() {
		IAccounterPayrollServiceAsync payrollService = (IAccounterPayrollServiceAsync) GWT
				.create(IAccounterPayrollService.class);
		((ServiceDefTarget) payrollService)
				.setServiceEntryPoint(Accounter.PAYROLL_ENTRY_POINT);
		return payrollService;
	}
}
