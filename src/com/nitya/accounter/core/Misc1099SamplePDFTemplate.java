package com.nitya.accounter.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import com.nitya.accounter.utils.MiniTemplator;
import com.nitya.accounter.utils.MiniTemplator.TemplateSyntaxException;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.Client1099Form;
import com.nitya.accounter.web.client.externalization.AccounterMessages;

public class Misc1099SamplePDFTemplate {

	private static final String templateFileName = "templetes" + File.separator
			+ "1099SampleTemplate.html";
	ArrayList<Client1099Form> list;

	String payersAddress = "&nbsp;";
	String rents = "123456789";
	String royalties = "123456789;";
	String other_income = "123456789;";
	String fedral_incomeTax = "&nbsp;";
	String payer_fedral_identification_number = "123456789";
	String recepent_identification_number = "&nbsp;";
	String fishing_boats_procedds = "&nbsp;";
	String medical_health_payments = "&nbsp;";
	String recepent_name = "123456789";
	String nonemployee_compensation = "&nbsp;";
	String substitute_payment = "123456789";
	String street_adress = "&nbsp;";
	String crop_insurance_Proceeds = "&nbsp;";
	String city_zip = "&nbsp;";
	String account_number = "&nbsp;";
	String golden_parachute_payments = "&nbsp;";
	String gross_paidto_atorney = "&nbsp;";
	String section_409A_deferals = "&nbsp;";
	String section_409A_income = "&nbsp;";
	String state_tax = "&nbsp;";
	String payers_state_no = "&nbsp;";
	String state_income = "&nbsp;";

	int marginLeft;
	int marginRight;
	int marginTop;
	int marginBottom;
	Client1099Form form;

	public Misc1099SamplePDFTemplate(int horizontalValue, int verticalValue) {

		if (horizontalValue < 0) {
			marginLeft = -1 * horizontalValue;
		} else {
			marginRight = horizontalValue;
		}

		if (verticalValue < 0) {
			marginTop = -1 * verticalValue;
		} else {
			marginBottom = verticalValue;
		}
	}

	public String generatePDF() throws TemplateSyntaxException, IOException {

		String outPutString = "";
		MiniTemplator t;

		try {
			t = new MiniTemplator(templateFileName);
			externalizeStrings(t);

			if (payersAddress.length() > 0) {
				t.setVariable("payersAddress", payersAddress);
				t.addBlock("payersAddressB");
			}

			if (rents.length() > 0) {
				t.setVariable("rents", rents);
				t.addBlock("rentsB");
			}

			if (other_income.length() > 0) {
				t.setVariable("other_income", other_income);
				t.addBlock("other_incomeB");
			}

			if (royalties.length() > 0) {
				t.setVariable("royalties", royalties);
				t.addBlock("royaltiesB");
			}

			if (fedral_incomeTax.length() > 0) {
				t.setVariable("fedral_incomeTax", fedral_incomeTax);
				t.addBlock("fedral_incomeTaxB");
			}

			if (payer_fedral_identification_number.length() > 0) {
				t.setVariable("payer_fedral_identification_number",
						payer_fedral_identification_number);
				t.addBlock("payer_fedral_identification_numberB");
			}

			if (recepent_identification_number.length() > 0) {
				t.setVariable("recepent_identification_number",
						recepent_identification_number);
				t.addBlock("recepent_identification_numberB");
			}

			if (fishing_boats_procedds.length() > 0) {
				t.setVariable("fishing_boats_procedds", fishing_boats_procedds);
				t.addBlock("fishing_boats_proceddsB");
			}

			if (medical_health_payments.length() > 0) {
				t.setVariable("medical_health_payments",
						medical_health_payments);
				t.addBlock("medical_health_paymentsB");
			}

			if (recepent_name.length() > 0) {
				t.setVariable("recepent_name", recepent_name);
				t.addBlock("recepent_nameB");
			}

			if (nonemployee_compensation.length() > 0) {
				t.setVariable("nonemployee_compensation",
						nonemployee_compensation);
				t.addBlock("nonemployee_compensationB");
			}

			if (substitute_payment.length() > 0) {
				t.setVariable("substitute_payment", substitute_payment);
				t.addBlock("substitute_paymentB");
			}

			if (street_adress.length() > 0) {
				t.setVariable("street_adress", street_adress);
				t.addBlock("street_adressB");
			}

			if (crop_insurance_Proceeds.length() > 0) {
				t.setVariable("crop_insurance_Proceeds",
						crop_insurance_Proceeds);
				t.addBlock("crop_insurance_ProceedsB");
			}

			if (city_zip.length() > 0) {
				t.setVariable("city_zip", city_zip);
				t.addBlock("city_zipB");
			}

			if (account_number.length() > 0) {
				t.setVariable("account_number", account_number);
				t.addBlock("account_numberB");
			}

			if (golden_parachute_payments.length() > 0) {
				t.setVariable("golden_parachute_payments",
						golden_parachute_payments);
				t.addBlock("golden_parachute_paymentsB");
			}

			if (gross_paidto_atorney.length() > 0) {
				t.setVariable("gross_paidto_atorney", gross_paidto_atorney);
				t.addBlock("gross_paidto_atorneyB");
			}

			if (section_409A_deferals.length() > 0) {
				t.setVariable("section_409A_deferals", section_409A_deferals);
				t.addBlock("section_409A_deferalsB");
			}

			if (section_409A_income.length() > 0) {
				t.setVariable("section_409A_income", section_409A_income);
				t.addBlock("section_409A_incomeB");
			}

			if (state_tax.length() > 0) {
				t.setVariable("state_tax", state_tax);
				t.addBlock("state_taxB");
			}

			if (payers_state_no.length() > 0) {
				t.setVariable("payers_state_no", payers_state_no);
				t.addBlock("payers_state_noB");
			}

			if (state_income.length() > 0) {
				t.setVariable("state_income", state_income);
				t.addBlock("state_incomeB");
			}

			t.setVariable("marginleft", marginLeft);
			t.setVariable("margintop", marginTop);
			t.setVariable("marginbottom", marginBottom);
			t.setVariable("marginright", marginRight);
			t.addBlock("theme");

			System.out.println("string......" + t.getFileString());
			outPutString = t.getFileString();

		} catch (Exception e) {
			// TODO: handle exception
		}

		return outPutString;
	}

	private void externalizeStrings(MiniTemplator t) {
		AccounterMessages messages = Global.get().messages();
		Map<String, String> variables = t.getVariables();
		System.out.println(variables);
		t.setVariable("i18_adjustAlignmentAndReprint",
				messages.adjustAlignmentAndReprint());

	}

	public String getFileName() {
		return "1099Sample";
	}

}
