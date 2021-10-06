package com.vimukti.accounter.servlets;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.zefer.pd4ml.PD4Constants;

import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.MISCInformationTemplate;
import com.vimukti.accounter.core.Misc1099PDFTemplate;
import com.vimukti.accounter.core.Misc1099SamplePDFTemplate;
import com.vimukti.accounter.core.TemplateBuilder;
import com.vimukti.accounter.core.Vendor;
import com.vimukti.accounter.main.CompanyPreferenceThreadLocal;
import com.vimukti.accounter.utils.Converter;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.Client1099Form;
import com.vimukti.accounter.web.server.FinanceTool;

public class MISCInfoServlet extends BaseServlet {

	private static final long serialVersionUID = 1L;

	// private StringBuilder outPutString;

	public void generatePDF(HttpServletRequest request,
			HttpServletResponse response, String companyName) {

		try {

			File propertyFile = new File("FinanceDir");
			if (!propertyFile.exists()) {
				System.err
						.println("Their is a No Folder For Style Sheet & Image");
			}
			converTempleteObjByRequest(request, response, companyName);
			// generateData();

			// System.err.println("Converter obj created");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
	}

	private void converTempleteObjByRequest(HttpServletRequest request,
			HttpServletResponse response, String companyName) throws Exception {

		Long companyID = (Long) request.getSession().getAttribute(COMPANY_ID);
		String fileName = "";
		StringBuilder outPutString = new StringBuilder();

		FinanceTool financetool = new FinanceTool();
		TemplateBuilder.setCmpName(companyName);

		Company company = financetool.getCompany(companyID);

		CompanyPreferenceThreadLocal.set(financetool.getCompanyManager()
				.getClientCompanyPreferences(company));

		String objectId = request.getParameter("objectId");

		String multipleId = request.getParameter("multipleIds");
		String[] ids = null;
		if (multipleId != null) {
			ids = multipleId.split(",");
		}

		Integer formType = Integer.parseInt(request.getParameter("type"));

		int horizontalValue = Integer.parseInt(request
				.getParameter("horizontalValue"));

		int verticalValue = Integer.parseInt(request
				.getParameter("verticalValue"));

		long vendorID = Long.parseLong(request.getParameter("vendorID"));
		Converter converter = null;
		if (formType == 1) {

			converter = new Converter(PD4Constants.LETTER);

			Vendor vendor1099 = (Vendor) financetool.getManager()
					.getServerObjectForid(AccounterCoreType.VENDOR, vendorID);

			Misc1099PDFTemplate miscHtmlTemplete = new Misc1099PDFTemplate(
					vendor1099, horizontalValue, verticalValue);
			fileName = miscHtmlTemplete.getFileName();
			outPutString = outPutString.append(miscHtmlTemplete.generatePDF());

		} else if (formType == 0) {

			ArrayList<Client1099Form> miscinfo = financetool
					.getVendorManager()
					.get1099Vendors(Integer.parseInt(objectId), company.getID());

			converter = new Converter(PD4Constants.LETTER);
			MISCInformationTemplate info = new MISCInformationTemplate(
					miscinfo, company);
			outPutString = outPutString.append(info.generateFile());
			fileName = info.getFileName();
		} else if (formType == 2) {

			ArrayList<Client1099Form> miscinfo = financetool.getVendorManager()
					.get1099Vendors(1, company.getID());

			converter = new Converter(PD4Constants.LETTER);
			Misc1099SamplePDFTemplate sampleTemplate = new Misc1099SamplePDFTemplate(
					horizontalValue, verticalValue);
			fileName = sampleTemplate.getFileName();
			outPutString = outPutString.append(sampleTemplate.generatePDF());
		}

		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "attachment; filename="
				+ fileName.replace(" ", "") + ".pdf");
		ServletOutputStream sos = response.getOutputStream();

		java.io.InputStream inputString = new ByteArrayInputStream(outPutString
				.toString().getBytes());
		InputStreamReader miscCreator = new InputStreamReader(inputString);
		converter.generatePdfDocuments(fileName, sos, miscCreator);
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String companyName = getCompanyName(request);
		if (companyName == null)
			return;
		generatePDF(request, response, companyName);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
