package com.nitya.accounter.servlets;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;

import com.lowagie.text.FontFactory;
import com.nitya.accounter.core.Company;
import com.nitya.accounter.core.FinanceDate;
import com.nitya.accounter.core.Form16ApdfTemplate;
import com.nitya.accounter.core.TDSChalanDetail;
import com.nitya.accounter.core.TDSCoveringLetterTemplate;
import com.nitya.accounter.core.TDSDeductorMasters;
import com.nitya.accounter.core.TDSResponsiblePerson;
import com.nitya.accounter.core.TemplateBuilder;
import com.nitya.accounter.core.Vendor;
import com.nitya.accounter.main.CompanyPreferenceThreadLocal;
import com.nitya.accounter.servlets.GeneratePDFservlet.FontFactoryImpEx;
import com.nitya.accounter.utils.HibernateUtil;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.server.FinanceTool;
import com.nitya.accounter.web.server.util.ExportUtils;

import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;

public class Form16ApdfGenerationServlet extends BaseServlet {

	/**
	 * this file is required to generate the form 16A pdf
	 */
	private static final long serialVersionUID = 1L;
	private String fileName;
	private Long companyID;
	private FinanceTool financetool;
	private Company company;
	private long vendorID;
	private int type;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String companyName = getCompanyName(req);
		if (companyName == null)
			return;
		try {
			generatePDF(req, resp, companyName);
		} catch (AccounterException e) {
			e.printStackTrace();
		}
	}

	private void generatePDF(HttpServletRequest req, HttpServletResponse resp,
			String companyName) throws AccounterException {
		try {
			ServletOutputStream sos = null;
			String vendorIDString = req.getParameter("vendorID");
			String dateRangeString = req.getParameter("datesRange");
			String[] split = dateRangeString.split("-");
			String fromDate = split[0];
			String toDate = split[1];
			String place = req.getParameter("place");
			String date = req.getParameter("printDate");
			String typeString = req.getParameter("type");

			type = Integer.parseInt(typeString);

			vendorID = Long.parseLong(vendorIDString);
			companyID = (Long) req.getSession().getAttribute(COMPANY_ID);
			String acknowledgementNo = req.getParameter("tdsCertificateNumber");
			financetool = new FinanceTool();
			TemplateBuilder.setCmpName(companyName);
			company = financetool.getCompany(companyID);
			CompanyPreferenceThreadLocal.set(financetool.getCompanyManager()
					.getClientCompanyPreferences(company));

			Session currentSession = HibernateUtil.getCurrentSession();
			Vendor vendor = (Vendor) currentSession.get(Vendor.class, vendorID);
			IXDocReport report = null;
			IContext context = null;
			TDSDeductorMasters tdsDeductorMasterDetails = company
					.getTdsDeductor();
			TDSResponsiblePerson tdsResposiblePerson = company
					.getTdsResposiblePerson();
			if (tdsDeductorMasterDetails == null || vendor == null
					|| tdsResposiblePerson == null) {
				return;
			}
			String templeteName = null;
			if (type == 0) {
				fileName = "Form16A";
				templeteName = "templetes" + File.separator + "Form16a.docx";
				ClientFinanceDate cEndDate = new ClientFinanceDate(toDate);
				ClientFinanceDate cStartDate = new ClientFinanceDate(fromDate);
				FinanceDate startDate = new FinanceDate(cStartDate);
				FinanceDate endDate = new FinanceDate(cEndDate);
				ArrayList<TDSChalanDetail> chalanList = financetool
						.getChalanList(startDate, endDate, acknowledgementNo,
								company.getID());

				Form16ApdfTemplate form16APdfGeneration = new Form16ApdfTemplate();

				form16APdfGeneration.setDateYear(dateRangeString);

				InputStream in = new BufferedInputStream(new FileInputStream(
						templeteName));

				try {
					report = XDocReportRegistry.getRegistry().loadReport(in,
							TemplateEngineKind.Velocity);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (XDocReportException e) {
					e.printStackTrace();
				}

				form16APdfGeneration.setSummaryPaymentDetails(chalanList);

				Map<Integer, ArrayList<TDSChalanDetail>> quartersList = new HashMap<Integer, ArrayList<TDSChalanDetail>>();
				for (int i = 0; i < 4; i++) {
					FinanceDate[] quarterDates = getQuarterDates(i);
					ArrayList<TDSChalanDetail> quarterList = new ArrayList<TDSChalanDetail>();
					if (quarterDates != null) {
						quarterList = financetool.getChalanList(
								quarterDates[0], quarterDates[1],
								acknowledgementNo, company.getID());
					}
					quartersList.put(i, quarterList);
				}
				form16APdfGeneration.setSummaryOfTaxDetails(quartersList);
				form16APdfGeneration.setVerificationDetails(
						tdsResposiblePerson, place, date);
				form16APdfGeneration.setVendorandCompanyData(vendor,
						tdsDeductorMasterDetails);
				context = report.createContext();
				context = form16APdfGeneration.assignValues(context, report);

			} else {
				fileName = "CoveringLetter" + vendor.getName();
				templeteName = "templetes" + File.separator
						+ "CoveringLetter.docx";
				TDSCoveringLetterTemplate coveringLetterTemp = new TDSCoveringLetterTemplate();
				coveringLetterTemp.setValues(vendor, company,
						tdsResposiblePerson);

				InputStream in = new BufferedInputStream(new FileInputStream(
						templeteName));

				try {
					report = XDocReportRegistry.getRegistry().loadReport(in,
							TemplateEngineKind.Velocity);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (XDocReportException e) {
					e.printStackTrace();
				}
				context = report.createContext();
				context = coveringLetterTemp.assignValues(context, report);
			}
			FontFactory.setFontImp(new FontFactoryImpEx());

			resp.setContentType("application/pdf");
			resp.setHeader("Content-disposition", "attachment; filename="
					+ fileName.replace(" ", "") + ".pdf");

			sos = resp.getOutputStream();

			ExportUtils.exportToPDF(report, context, sos, templeteName);
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.err.println("Pdf created");
	}

	private FinanceDate[] getQuarterDates(int quarter) {
		int finYear = new ClientFinanceDate().getYear();
		String frmDt = null;
		String toDt = null;
		switch (quarter) {
		case 0:
			frmDt = "04/01/" + Integer.toString(finYear);
			toDt = "06/30/" + Integer.toString(finYear);
			break;
		case 1:
			frmDt = "07/01/" + Integer.toString(finYear);
			toDt = "09/30/" + Integer.toString(finYear);
			break;
		case 2:
			frmDt = "10/01/" + Integer.toString(finYear);
			toDt = "12/31/" + Integer.toString(finYear);
			break;
		case 3:
			frmDt = "01/01/" + Integer.toString(finYear);
			toDt = "31/03/" + Integer.toString(finYear);
			break;
		default:
			break;

		}
		FinanceDate fromDate = new FinanceDate(frmDt);
		FinanceDate toDate = new FinanceDate(toDt);
		return new FinanceDate[] { fromDate, toDate };
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}

}
