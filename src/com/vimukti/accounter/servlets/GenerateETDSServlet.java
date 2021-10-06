package com.vimukti.accounter.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vimukti.accounter.core.Form26QAnnexureGenerator;
import com.vimukti.accounter.core.Form27EQAnnexureGenerator;
import com.vimukti.accounter.core.Form27QAnnexureGenerator;
import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTDSChalanDetail;
import com.vimukti.accounter.web.client.core.ClientTDSDeductorMasters;
import com.vimukti.accounter.web.client.core.ClientTDSResponsiblePerson;
import com.vimukti.accounter.web.server.FinanceTool;

public class GenerateETDSServlet extends BaseServlet {

	/**
	 * this servlet is used to generate the eTDs filling text file
	 */
	private static final long serialVersionUID = 1L;
	private ClientTDSDeductorMasters tdsDeductorMasterDetails;
	private ClientTDSResponsiblePerson responsiblePersonDetails;
	private String formNo;
	private String quater;
	private String fromDate;
	private String toDate;
	private String startYear;
	private String endYear;
	private List<ClientTDSChalanDetail> chalanList;
	private String panList;
	private String codeList;
	private String remarkList;
	private String grossingUpList;

	/**
	 * call the rpc based on the parameter being passed from UI. and get the
	 * data list to be printed.
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String companyName = getCompanyName(request);
		if (companyName == null)
			return;

		formNo = request.getParameter("formNo");
		quater = request.getParameter("quater");
		fromDate = request.getParameter("fromDate");
		toDate = request.getParameter("toDate");
		startYear = request.getParameter("startYear");
		endYear = request.getParameter("endYear");
		panList = request.getParameter("panList");
		codeList = request.getParameter("codeList");
		remarkList = request.getParameter("remarkList");
		grossingUpList = request.getParameter("grossingUpList");

		FinanceTool financetool = new FinanceTool();

		try {
			chalanList = financetool.getChalanList(Integer.parseInt(formNo),
					Integer.parseInt(quater),
					new ClientFinanceDate(Long.valueOf(fromDate)),
					new ClientFinanceDate(Long.valueOf(toDate)),
					Integer.parseInt(startYear), Integer.parseInt(endYear),
					getCompany(request).getId());
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (DAOException e) {
			e.printStackTrace();
		}

		try {
			tdsDeductorMasterDetails = financetool
					.getTDSDeductorMasterDetails(getCompany(request).getId());
		} catch (DAOException e) {
			e.printStackTrace();
		}

		try {
			responsiblePersonDetails = financetool
					.getResponsiblePersonDetails(getCompany(request).getId());
		} catch (DAOException e) {
			e.printStackTrace();
		}

		generateTextFile(request, response, companyName, chalanList,
				tdsDeductorMasterDetails, responsiblePersonDetails);

	}

	/**
	 * generate the text file by calling the respective form generator class and
	 * print them to the text file.
	 * 
	 * @param request
	 * @param response
	 * @param companyName
	 * @param chalanList2
	 * @param tdsDeductorMasterDetails2
	 * @param responsiblePersonDetails2
	 * @throws IOException
	 */
	private void generateTextFile(HttpServletRequest request,
			HttpServletResponse response, String companyName,
			List<ClientTDSChalanDetail> chalanList2,
			ClientTDSDeductorMasters tdsDeductorMasterDetails2,
			ClientTDSResponsiblePerson responsiblePersonDetails2)
			throws IOException {

		String generateFile = null;
		int FormNo = Integer.parseInt(formNo);
		if (FormNo == 1) {
			Form26QAnnexureGenerator form26Q = new Form26QAnnexureGenerator(
					tdsDeductorMasterDetails2, responsiblePersonDetails2,
					getCompany(request), panList, codeList, remarkList);
			form26Q.setFormDetails(formNo, quater, startYear, endYear);
			form26Q.setChalanDetailsList(chalanList2);
			generateFile = form26Q.generateFile();

		} else if (FormNo == 2) {
			Form27QAnnexureGenerator form26Q = new Form27QAnnexureGenerator(
					tdsDeductorMasterDetails2, responsiblePersonDetails2,
					getCompany(request), panList, codeList, remarkList,
					grossingUpList);
			form26Q.setFormDetails(formNo, quater, startYear, endYear);
			form26Q.setChalanDetailsList(chalanList2);
			generateFile = form26Q.generateFile();

		} else if (FormNo == 3) {
			Form27EQAnnexureGenerator form26Q = new Form27EQAnnexureGenerator(
					tdsDeductorMasterDetails2, responsiblePersonDetails2,
					getCompany(request), panList, codeList, remarkList);
			form26Q.setFormDetails(formNo, quater, startYear, endYear);
			form26Q.setChalanDetailsList(chalanList2);
			generateFile = form26Q.generateFile();

		}

		ServletOutputStream op = response.getOutputStream();
		response.setHeader("Content-Disposition", "attachment; filename=\""
				+ generateFileName(formNo, quater) + "\"");
		byte[] bytes = generateFile.getBytes();
		op.write(bytes);
		op.flush();

	}

	/**
	 * generate the file name based on the formNO and quarter selected
	 * 
	 * @param formNo
	 * @param quater
	 * @return
	 */
	private String generateFileName(String formNo, String quater) {

		String formName = null;
		int FormNo = Integer.parseInt(formNo);
		if (FormNo == 1) {
			formName = "26Q";
		} else if (FormNo == 2) {
			formName = "27Q";
		} else if (FormNo == 3) {
			formName = "27EQ";
		}

		String QuaterName = null;
		int quaterNO = Integer.parseInt(quater);
		if (quaterNO == 1) {
			QuaterName = "RQ1";
		} else if (quaterNO == 2) {
			QuaterName = "RQ2";
		} else if (quaterNO == 3) {
			QuaterName = "RQ3";
		} else if (quaterNO == 4) {
			QuaterName = "RQ4";
		}

		return formName + QuaterName;
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
