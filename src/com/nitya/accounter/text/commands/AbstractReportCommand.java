package com.nitya.accounter.text.commands;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import com.nitya.accounter.core.FinanceDate;
import com.nitya.accounter.main.ServerConfiguration;
import com.nitya.accounter.text.ITextData;
import com.nitya.accounter.text.ITextResponse;
import com.nitya.accounter.web.client.core.ReportInput;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.server.managers.ExportManager;

public abstract class AbstractReportCommand extends AbstractTextCommand {

	private FinanceDate startDate;
	private FinanceDate endDate;
	private String formate;

	@Override
	public boolean parse(ITextData data, ITextResponse respnse) {
		// Parsing the Start and End Dates
		parseDates(data, respnse);
		return true;
	}

	@Override
	public void process(ITextResponse respnse) {
		// adding File name to response
		addReportFileNameToResponse(respnse);
	}

	/**
	 * Parse The Start Date And End Date
	 * 
	 * @param data
	 * @param respnse
	 */
	public boolean parseDates(ITextData data, ITextResponse respnse) {
		// START DATE
		if (!data.isDate()) {
			respnse.addError("Invalid Date format for date field");
			return false;
		}
		// if next date is null,then set the default present date
		startDate = data.nextDate(new FinanceDate());

		// END DATE
		if (!data.isDate()) {
			respnse.addError("Invalid Date format for date field");
			return false;
		}
		// if next date is null,then set the default present date
		endDate = data.nextDate(new FinanceDate());
		// Export formate Pdf/Csv
		formate = data.nextString("");

		return true;
	}

	/**
	 * Export Report To File
	 * 
	 * @param respnse
	 * @param arrayList
	 * 
	 * @return {@link List} File Names
	 */
	protected void addReportFileNameToResponse(ITextResponse respnse,
			ReportInput... inputs) {
		ExportManager manager = getFinanceTool().getExportManager();
		// Checking Start Date is null or not.
		if (startDate == null) {
			startDate = new FinanceDate();
		}
		// Checking End Date is null or not.
		if (endDate == null) {
			endDate = new FinanceDate();
		}
		// Report Export Type
		int reportExportType = ReportInput.REPORT_EXPORT_TYPE_PDF;
		if (formate.toLowerCase().equals("csv")) {
			reportExportType = ReportInput.REPORT_EXPORT_TYPE_CSV;
		}
		List<ReportInput> reportInputs = Arrays.asList(inputs);
		try {
			List<String> exportReportToFiles = manager.exportReportToFile(
					getCompany().getId(), reportExportType, getReportType(),
					startDate.getDate(), endDate.getDate(), reportInputs);
			// adding File name to response
			String file = exportReportToFiles
					.get(exportReportToFiles.size() - 1);
			String tmpDir = ServerConfiguration.getTmpDir();
			File tmpFile = new File(tmpDir, file);
			respnse.addFile(tmpFile.getAbsolutePath());
		} catch (AccounterException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get the Report Type
	 * 
	 * @return
	 */
	public abstract int getReportType();
}
