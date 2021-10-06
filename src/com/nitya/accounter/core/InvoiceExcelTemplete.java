package com.nitya.accounter.core;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.nitya.accounter.utils.CSVWriter;

/**
 * this class is used to generate Invoice report in Excel format.
 * 
 * @author vimukti28
 * 
 */
public class InvoiceExcelTemplete {
	private BrandingTheme brandingTheme;
	private Invoice invoice;
	private Company company;

	public InvoiceExcelTemplete(Invoice invoice, BrandingTheme brandingTheme,
			Company company) {
		this.invoice = invoice;
		this.brandingTheme = brandingTheme;
		this.company = company;

	}

	public void generateToExcel() throws IOException {

		Address billaddress = invoice.getBillingAddress();

		Address shipaddress = invoice.getShippingAdress();

		Address registeredAddress = company.getRegisteredAddress();

		ArrayList<String> regisAddress = getAddress(registeredAddress, false);
		ArrayList<String> billAddress = getAddress(billaddress, true);
		ArrayList<String> shipAddress = getAddress(shipaddress, false);

		ArrayList<String> invoiceLabels = new ArrayList<String>();
		invoiceLabels.add("Invoice Number");
		invoiceLabels.add("Invoice Date");
		invoiceLabels.add("Order Number");
		invoiceLabels.add("Customer Number");

		ArrayList<String> invoiceValues = new ArrayList<String>();
		invoiceValues.add(invoice.getNumber());
		invoiceValues.add(invoice.getDate().toString());
		invoiceValues.add(invoice.getOrderNum());
		invoiceValues.add(invoice.getCustomer().getNumber());

		// for comparing the invoice labels and registered address sizes
		if (invoiceLabels.size() < regisAddress.size()) {
			while (invoiceLabels.size() < regisAddress.size()) {
				invoiceLabels.add("");
				invoiceValues.add("");
			}
		} else {
			while (regisAddress.size() < invoiceLabels.size()) {
				regisAddress.add("");
			}
		}

		// for comparing the bill to address and shipping to address
		if (billAddress.size() < shipAddress.size()) {
			while (billAddress.size() < shipAddress.size()) {
				billAddress.add("");

			}
		} else {
			while (shipAddress.size() < billAddress.size()) {
				shipAddress.add("");
			}
		}

		SalesPerson salesPerson = invoice.getSalesPerson();
		String salesname = salesPerson != null ? ((salesPerson.getFirstName() == null ? ""
				: salesPerson.getFirstName())
				+ "" + (salesPerson.getLastName() == null ? "" : salesPerson
				.getLastName()))
				: "";

		ShippingMethod shippingMethod = invoice.getShippingMethod();
		String shipName = shippingMethod != null ? shippingMethod.getName()
				: "";

		PaymentTerms paymentTerms = invoice.getPaymentTerm();
		String payment = paymentTerms != null ? paymentTerms.getName() : "";

		// boolean sales = salesname.trim().length() > 0 ? true : false;
		// boolean ship = shipName.trim().length() > 0 ? true : false;
		// boolean paymentterms = payment.trim().length() > 0 ? true : false;

		String dueDate = invoice.getDueDate().toString();

		String termsnPayment = forNullValue(brandingTheme
				.getTerms_And_Payment_Advice());

		String paypalEmail = forNullValue(brandingTheme.getPayPalEmailID());

		String vatStringLabel = getVendorString("VAT No: ", "Tax No: ");

		String vatStringValue = forNullValue(company.getPreferences()
				.getVATregistrationNumber());

		String sortCodeLabel = " Sort Code: ";
		String sortCodeValue = forNullValue(company.getSortCode());

		String bankAccountNumLabel = "Bank Account No: ";
		String bankAccountNumValue = forNullValue(company.getBankAccountNo());

		String comanyRegNum = checkIsNull(company.getRegistrationNumber());

		// InvoiceExcelTemplete excelTemplete = new
		// InvoiceExcelTemplete(invoice, brandingTheme);
		// excelTemplete.generateToExcel();

		String excelFileName = "d:\\csv\\Invoice_" + invoice.getNumber()
				+ ".csv";

		try {
			FileWriter f = new FileWriter(excelFileName);
			PrintWriter pw = new PrintWriter(f);
			CSVWriter csvWriter = new CSVWriter(pw, false, ',',
					System.getProperty("line.separator"));

			csvWriter.writeln(new String[] { "", "",
					brandingTheme.getOverDueInvoiceTitle(), "", "", "" });
			csvWriter.writeln();

			// third row
			for (int i = 0; i < regisAddress.size(); i++) {

				csvWriter.writeln(new String[] { "", regisAddress.get(i),
						"", "", invoiceLabels.get(i),
						invoiceValues.get(i) });
				// csvWriter.writeln();
			}

			csvWriter.writeln();

			csvWriter.writeln(new String[] { "", "Bill To", "", "", "Ship To",
					"" });

			csvWriter.writeln();

			// fifth row
			for (int i = 0; i < billAddress.size(); i++) {

				csvWriter.writeln(new String[] { billAddress.get(i), "",
						"", "", shipAddress.get(i), "" });

			}

			csvWriter.writeln();
			// for shipping, sales person and payment term labels

			csvWriter.writeln(new String[] { "Sales Person", "",
					"Shipping Method", "", "Payment Terms", "" });

			// for shipping, sales person and payment term values

			csvWriter.writeln(new String[] { salesname, "", shipName, "",
					payment, "" });

			csvWriter.writeln();

			csvWriter.writeln();
			// for displaying invoice details

			csvWriter.writeln(new String[] { "Description", "Qty",
					"Unit Price", "Total Price",
					getVendorString("VAT Rate", "Tax Rate"),
					getVendorString("VAT Amount", "Tax Amount") });

			List<TransactionItem> transactionItem = invoice
					.getTransactionItems();
			for (Iterator iterator = transactionItem.iterator(); iterator
					.hasNext();) {
				TransactionItem transItem = (TransactionItem) iterator.next();

				f.append(transItem.getDescription()
						+ ','
						+ transItem.getQuantity()
						+ ','
						+ transItem.getUnitPrice()
						+ ','
						+ transItem.getLineTotal()
						+ ','
						+ transItem.getTaxCode()
						+ (transItem.getVATfraction() != null ? ',' + transItem
								.getVATfraction() : "") + "\n");
				csvWriter.writeln(new String[] {
						transItem.getDescription(),
						String.valueOf(transItem.getQuantity().getValue()),
						String.valueOf(transItem.getUnitPrice()),
						String.valueOf(transItem.getLineTotal()),
						getDecimalsUsingMaxDecimals(transItem.getVATfraction(),
								null, 2),
						String.valueOf(transItem.getVATfraction()) });

			}

			csvWriter.writeln(new String[] { invoice.getMemo(), "", "", "",
					"Sub Total",
					largeAmountConversation(invoice.getNetAmount()) });

			csvWriter.writeln(new String[] {
					"",
					"",
					"",
					"",
					getVendorString("VAT Total", "Tax Total"),
					largeAmountConversation((invoice.getTotal() - invoice
							.getNetAmount())) });

			csvWriter.writeln(new String[] { "", "", "", "", "Total",
					largeAmountConversation(invoice.getTotal()) });

			csvWriter.writeln();

			csvWriter
					.writeln(new String[] { "DueDate", dueDate, "", "", "", "" });

			csvWriter.writeln();

			csvWriter
					.writeln(new String[] { termsnPayment, "", "", "", "", "" });

			csvWriter.writeln();

			csvWriter.writeln(new String[] { paypalEmail, "", "", "", "", "" });

			csvWriter.writeln();

			csvWriter.writeln(new String[] { vatStringLabel, vatStringValue,
					sortCodeLabel, sortCodeValue, bankAccountNumLabel,
					bankAccountNumValue });

			csvWriter.writeln();

			String regAddress = "";

			for (Iterator iterator = regisAddress.iterator(); iterator
					.hasNext();) {
				String string = (String) iterator.next();
				regAddress += string + ',';

			}

			csvWriter.writeln(new String[] { "company name", regAddress, "",
					"", "", "" });

			csvWriter.writeln();

			csvWriter.writeln(new String[] { "", "Company Registration No:",
					comanyRegNum, "", "", "" });

			csvWriter.writeln();

			csvWriter.close();

			System.err.println("completed... writing in csv format");

		} finally {

		}

	}

	/**
	 * this method is used to get billing address and shipping address
	 */
	public ArrayList<String> getAddress(Address address, boolean val) {
		ArrayList<String> addressVector = new ArrayList<String>();

		if (val) {
			// this is used only for billing and shipping address
			String customerName = checkIsNull(invoice.getCustomer().getName());
			if (customerName.length() > 0) {
				addressVector.add(customerName);
			}
		}
		String add1 = checkIsNull(address.getAddress1());
		if (add1.length() > 0) {
			addressVector.add(add1);
		}
		String street = checkIsNull(address.getStreet());
		if (street.length() > 0) {
			addressVector.add(street);
		}
		String city = checkIsNull(address.getCity());
		if (city.length() > 0) {
			addressVector.add(city);
		}
		String state = checkIsNull(address.getStateOrProvinence());
		if (state.length() > 0) {
			addressVector.add(state);
		}
		String zip = checkIsNull(address.getZipOrPostalCode());
		if (zip.length() > 0) {
			addressVector.add(zip);
		}
		String country = checkIsNull(address.getCountryOrRegion());
		if (country.length() > 0) {
			addressVector.add(country);
		}

		return addressVector;
	}

	private String getVendorString(String forUk, String forUs) {
		/*
		 * return company.getAccountingType() == company.ACCOUNTING_TYPE_US ?
		 * forUs : forUk;
		 */;
		return forUk;
	}

	public String checkIsNull(String value) {
		return value == null ? "" : value;
	}

	public String forNullValue(String value) {
		return value != null ? value : "";
	}

	private String getDecimalsUsingMaxDecimals(double quantity, String amount,
			int maxDecimalPoint) {
		String qty = "";
		String max;
		if (maxDecimalPoint != 0) {
			if (amount == null)
				qty = String.valueOf(quantity);
			else
				qty = amount;
			max = qty.substring(qty.indexOf(".") + 1);
			if (maxDecimalPoint > max.length()) {
				for (int i = max.length(); maxDecimalPoint != i; i++) {
					qty = qty + "0";
				}
			}
		} else {
			qty = String.valueOf((long) quantity);
		}

		String temp = qty.contains(".") ? qty.replace(".", "-").split("-")[0]
				: qty;
		return insertCommas(temp)
				+ (qty.contains(".") ? "."
						+ qty.replace(".", "-").split("-")[1] : "");
	}

	private static String insertCommas(String str) {

		if (str.length() < 4) {
			return str;
		}
		return insertCommas(str.substring(0, str.length() - 3)) + ","
				+ str.substring(str.length() - 3, str.length());
	}

	private String largeAmountConversation(double amount) {
		String amt = Utility.decimalConversation(amount, invoice.getCurrency()
				.getSymbol());
		amt = getDecimalsUsingMaxDecimals(0.0, amt, 2);
		return (amt);
	}

}