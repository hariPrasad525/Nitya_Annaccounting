package com.vimukti.accounter.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import com.vimukti.accounter.utils.MiniTemplator;
import com.vimukti.accounter.utils.MiniTemplator.TemplateSyntaxException;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.Client1099Form;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;

public class MISCInformationTemplate {

	ArrayList<Client1099Form> miscVendors;
	Company myCompany;
	double totalPayment = 0.00;
	private static final String templateFileName = "templetes" + File.separator
			+ "MISCInformation.html";

	public MISCInformationTemplate(ArrayList<Client1099Form> miscinfo,
			Company company) {
		miscVendors = miscinfo;
		myCompany = company;
	}

	public String generateFile() throws TemplateSyntaxException, IOException {

		String outPutString = "";
		MiniTemplator t;

		try {
			t = new MiniTemplator(templateFileName);
			externalizeStrings(t);

			t.setVariable("companyName", myCompany.getTradingName());
			t.addBlock("changeCompanyName");

			t.setVariable("companyAddress1",
					myCompany.getTradingAddress().street);
			t.addBlock("changeCompanyAddress1");

			t.setVariable("companyAddress2", myCompany.getTradingAddress().city);
			t.addBlock("changeCompanyAddress2");

			t.setVariable("companyAddress3",
					myCompany.getTradingAddress().countryOrRegion);
			t.addBlock("changeCompanyAddress3");

			t.setVariable("companyAddress4",
					myCompany.getTradingAddress().getZipOrPostalCode());
			t.addBlock("changeCompanyAddress4");

			t.setVariable("companyId", Long.toString(myCompany.getID()));
			t.addBlock("changeCompanyId");

			int i;
			for (i = 0; i < miscVendors.size(); i++) {

				t.setVariable("vendorName", miscVendors.get(i).getVendor()
						.getName());
				t.addBlock("changeVendorName");

				t.setVariable("VendorcompanyAddress1", miscVendors.get(i)
						.getVendor().getAddress().toString());
				t.addBlock("changeVendorCompanyAddress1");

				t.setVariable("VendorCompanyId",
						Long.toString(miscVendors.get(i).getVendor().getID()));
				t.addBlock("changeVendorCompanyId");

				t.setVariable("box1Value",
						Double.toString(miscVendors.get(i).getBox(1)));
				t.addBlock("changebox1Value");

				t.setVariable("box2Value",
						Double.toString(miscVendors.get(i).getBox(2)));
				t.addBlock("changebox2Value");

				t.setVariable("box3Value",
						Double.toString(miscVendors.get(i).getBox(3)));
				t.addBlock("changebox3Value");

				t.setVariable("box4Value",
						Double.toString(miscVendors.get(i).getBox(4)));
				t.addBlock("changebox4Value");

				t.setVariable("box5Value",
						Double.toString(miscVendors.get(i).getBox(5)));
				t.addBlock("changebox5Value");

				t.setVariable("box6Value",
						Double.toString(miscVendors.get(i).getBox(6)));
				t.addBlock("changebox6Value");

				t.setVariable("box7Value",
						Double.toString(miscVendors.get(i).getBox(7)));
				t.addBlock("changebox7Value");

				t.setVariable("box8Value",
						Double.toString(miscVendors.get(i).getBox(8)));
				t.addBlock("changebox8Value");

				t.setVariable("box9Value",
						Double.toString(miscVendors.get(i).getBox(9)));
				t.addBlock("changebox9Value");

				t.setVariable("box10Value",
						Double.toString(miscVendors.get(i).getBox(10)));
				t.addBlock("changebox10Value");

				t.addBlock("addRows");

				totalPayment = totalPayment
						+ miscVendors.get(i).getTotalAllPayments();
			}

			t.setVariable("noOfForms", miscVendors.size());
			t.addBlock("changenoOfForms");

			t.setVariable("totalPaymment", Double.toString(totalPayment));
			t.addBlock("changetotalPaymment");

			t.addBlock("theme");

			System.out.println("string......" + t.getFileString());
			outPutString = t.getFileString();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return outPutString;
	}

	private void externalizeStrings(MiniTemplator t) {
		AccounterMessages messages = Global.get().messages();
		Map<String, String> variables = t.getVariables();
		System.out.println(variables);
		t.setVariable("i18_Company_Address", messages.companyAddress());
		t.setVariable("i18_Company_Name", messages.companyName());
		t.setVariable("i18_Id_Number", messages.idNumber());
		t.setVariable("i18_1099_vendors_that_meet_threshold",
				messages.venodrsThatMeetThreshold(Global.get().vendors()));
		t.setVariable("i18_Recipient_Information",
				messages.receipientInformation());
		t.setVariable("i18_Payer_Information", messages.payerInformation());
		t.setVariable("i18_Box1", messages.boxNumber(1));
		t.setVariable("i18_Box2", messages.boxNumber(2));
		t.setVariable("i18_Box3", messages.boxNumber(3));
		t.setVariable("i18_Box4", messages.boxNumber(4));
		t.setVariable("i18_Box5", messages.boxNumber(5));
		t.setVariable("i18_Box6", messages.boxNumber(6));
		t.setVariable("i18_Box7", messages.boxNumber(7));
		t.setVariable("i18_Box8", messages.boxNumber(8));
		t.setVariable("i18_Box8", messages.boxNumber(9));
		t.setVariable("i18_Box10", messages.boxNumber(10));
		t.setVariable("i18_Summary_for_1099", messages.summaryFor1099());
		t.setVariable("i18_Total_number_of_forms",
				messages.totalNoOf1099Forms());
		t.setVariable("i18_Total_amount_reported",
				messages.totalAmountReported());
	}

	public String getFileName() {
		return "1099-InformationSheet";
	}

}
