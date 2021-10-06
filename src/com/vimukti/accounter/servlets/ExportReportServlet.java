package com.vimukti.accounter.servlets;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.FontFactoryImp;
import com.lowagie.text.pdf.BaseFont;
import com.vimukti.accounter.core.CSVReportTemplate;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.ITemplate;
import com.vimukti.accounter.core.ReportsGenerator;
import com.vimukti.accounter.core.TemplateBuilder;
import com.vimukti.accounter.core.reports.generators.IReportGenerator;
import com.vimukti.accounter.main.CompanyPreferenceThreadLocal;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.server.FinanceTool;

public class ExportReportServlet extends BaseServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String style;

	@Override
	protected void service(HttpServletRequest arg0, HttpServletResponse arg1)
			throws ServletException, IOException {
		super.service(arg0, arg1);
	}

	public void exportReport(HttpServletRequest request,
			HttpServletResponse response) {

		ServletOutputStream sos = null;
		try {

			File propertyFile = new File("FinanceDir");
			if (!propertyFile.exists()) {
				System.err
						.println("Their is a No Folder For Style Sheet & Image");
			}

			ITemplate template = getTempleteObjByRequest(request);
			response.setContentType("application/csv");
			response.setHeader("Content-disposition", "attachment; filename="
					+ template.getFileName().trim().replace(" ", "") + ".csv");
			FontFactory.setFontImp(new FontFactoryImpEx());
			response.setCharacterEncoding("UTF-8");

			sos = response.getOutputStream();

			String templateBody = template.getBody();
			if (templateBody == null) {
				templateBody = "No records to show";
			}
			String header = template.getHeader() + "\r\n" + templateBody;
			sos.write(header.getBytes("UTF-8"));

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (sos != null)
					sos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private ITemplate getTempleteObjByRequest(HttpServletRequest request)
			throws IOException, AccounterException {
		Long companyID = (Long) request.getSession().getAttribute(COMPANY_ID);
		String companyName = getCompanyName(request);
		if (companyName == null)
			return null;

		FinanceTool financetool = new FinanceTool();
		Company company = financetool.getCompany(companyID);
		// int companyType = company.getAccountingType();

		TemplateBuilder.setCmpName(companyName);

		ClientCompanyPreferences clientCompanyPreferences = financetool
				.getCompanyManager().getClientCompanyPreferences(company);
		CompanyPreferenceThreadLocal.set(clientCompanyPreferences);

		ITemplate template = null;
		template = getReportTemplate(request, financetool);

		return template;

	}

	private ITemplate getReportTemplate(HttpServletRequest request,
			FinanceTool financeTool) throws IOException {

		long startDate = Long.parseLong(request.getParameter("startDate"));
		int reportType = Integer.parseInt(request.getParameter("reportType"));
		long endDate = Long.parseLong(request.getParameter("endDate"));
		String dateRangeHtml = request.getParameter("dateRangeHtml");
		String navigatedName = request.getParameter("navigatedName");
		String status = request.getParameter("status");
		String vendor = request.getParameter("vendorId");
		long vendorId = 0;
		int boxNo = 0;
		if (vendor != null) {
			vendorId = Long.parseLong(vendor);
		}
		String box = request.getParameter("boxNo");
		if (box != null) {
			boxNo = Integer.parseInt(box);
		}
		Long companyID = (Long) request.getSession().getAttribute(COMPANY_ID);

		Company company = financeTool.getCompany(companyID);
		ReportsGenerator generator = null;

		if (vendorId != 0) {
			generator = new ReportsGenerator(reportType, startDate, endDate,
					navigatedName, IReportGenerator.GENERATION_TYPE_CSV,
					vendorId, boxNo, company);

		} else if (status != null) {
			generator = new ReportsGenerator(reportType, startDate, endDate,
					navigatedName, IReportGenerator.GENERATION_TYPE_CSV,
					status, company, dateRangeHtml);
		} else {
			generator = new ReportsGenerator(reportType, startDate, endDate,
					navigatedName, IReportGenerator.GENERATION_TYPE_CSV,
					company, dateRangeHtml);
		}

		String gridTemplate = generator.generate(financeTool,
				IReportGenerator.GENERATION_TYPE_CSV);

		String reportName = generator.getReportNameByType(reportType);

		CSVReportTemplate template = new CSVReportTemplate(company, reportType,
				new String[] { gridTemplate, null, style, dateRangeHtml },
				reportName);
		/*  */

		return template;
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String companyName = getCompanyName(request);
		if (companyName == null)
			return;
		exportReport(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	public static class FontFactoryImpEx extends FontFactoryImp {

		private static Properties properties;
		private static String fontsPath = "./config/fonts/";

		FontFactoryImpEx() {
			properties = new Properties();
			try {
				properties.load(new FileInputStream(fontsPath
						+ "pd4fonts.properties"));
			} catch (Exception e) {

			}
		}

		@Override
		public Font getFont(String fontname, String encoding, boolean embedded,
				float size, int style, Color color) {
			try {
				String fileName = properties.getProperty(fontname);
				BaseFont bf = BaseFont.createFont(fontsPath + fileName,
						BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
				bf.setSubset(true);
				return new Font(bf, size, style, color);
			} catch (Exception e) {
				Font font = super.getFont(fontname, encoding, true, size,
						style, color);
				return font;
			}

		}
	}
}
