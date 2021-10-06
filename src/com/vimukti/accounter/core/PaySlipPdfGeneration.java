package com.vimukti.accounter.core;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import com.vimukti.accounter.web.client.core.reports.PaySlipDetail;
import com.vimukti.accounter.web.server.FinanceTool;

import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;

public class PaySlipPdfGeneration extends TransactionPDFGeneration {

	private FinanceDate startDate;
	private FinanceDate endDate;
	private LinkedHashMap<Integer, Address> allAddresses = new LinkedHashMap<Integer, Address>();
	private Employee employee;

	public PaySlipPdfGeneration(Employee employee, FinanceDate startDate,
			FinanceDate endDate) {
		super(null, null);
		this.startDate = startDate;
		this.employee = employee;
		this.endDate = endDate;
	}

	public IContext assignValues(IContext context, IXDocReport report) {
		try {
			Company company = employee.getCompany();
			DummyEmployee i = new DummyEmployee();
			String name = employee != null ? employee.getName() : "";
			i.setName(name);
			i.setNumber(employee.getNumber() != null ? employee.getNumber()
					: "");
			String design = employee.getDesignation() != null ? employee
					.getDesignation() : "";
			i.setDesignation(design);
			String bankAccount = employee.getBankAccountNo() != null ? employee
					.getBankAccountNo() : "";
			i.setBankAccountNo(bankAccount);
			String location = employee.getLocation() != null ? employee
					.getLocation() : "";
			i.setLocation(location);
			String panNumber = employee.getPANno() != null ? employee
					.getPANno() : "";
			i.setPanNumber(panNumber);
			Set<Address> address = employee.getAddress();
			setAddress(address);
			Address toBeShown = allAddresses.get(Address.TYPE_BILL_TO);
			i.setAddress(getAddress(toBeShown));
			FieldsMetadata headersMetaData = new FieldsMetadata();
			headersMetaData.addFieldAsList("detail.earnName");
			headersMetaData.addFieldAsList("detail.earnAmount");
			headersMetaData.addFieldAsList("detail.deductName");
			headersMetaData.addFieldAsList("detail.deductAmount");
			report.setFieldsMetadata(headersMetaData);

			FinanceTool tool = new FinanceTool();
			List<Detail> earns = new ArrayList<Detail>();
			List<Paydetail> paydetails = new ArrayList<PaySlipPdfGeneration.Paydetail>();
			List<PaySlipDetail> paySlipDetails = tool.getPayrollManager()
					.getPaySlipDetail(employee.getID(), startDate, endDate,
							company.getID());
			double earnings = 0;
			double deductions = 0;
			FinanceDate date = null;
			for (PaySlipDetail detail : paySlipDetails) {
				date = new FinanceDate(detail.getPayDate().getDate());
				if (detail.getType() == 2) {
					earns.add(new Detail(detail.getName(), Utility
							.decimalConversation(detail.getAmount(), ""), "-",
							"-"));
					earnings = earnings + detail.getAmount();
				} else if (detail.getType() == 3) {
					paydetails.add(new Paydetail(detail.getName(), Utility
							.decimalConversation(detail.getAmount(), "")));
					deductions = deductions + detail.getAmount();
				}
			}

			for (Paydetail deduction : paydetails) {
				boolean added = false;
				for (Detail earning : earns) {
					if (earning.getDeductName().equals("-")) {
						earning.setDeductName(deduction.getDeductName());
						earning.setDeductAmount(deduction.getDeductAmount());
						added = true;
						break;
					}
				}
				if (!added) {
					earns.add(new Detail("-", "-", deduction.getDeductName(),
							deduction.getDeductAmount()));
				}
			}
			i.setPayDay(Utility.getDateInSelectedFormat(date));
			i.setPayPeriod(getPayPeriod(startDate, endDate));
			i.setTotalEarnings(Utility.decimalConversation(earnings, ""));
			i.setTotalDeductions(Utility.decimalConversation(deductions, ""));
			i.setTakeHome(Utility
					.decimalConversation(earnings - deductions, ""));
			context.put("employe", i);
			context.put("detail", earns);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return context;

	}

	private String getPayPeriod(FinanceDate startDate2, FinanceDate endDate2) {
		return Utility.getDateInSelectedFormat(startDate2) + " to "
				+ Utility.getDateInSelectedFormat(endDate2);
	}

	private String getAddress(Address toBeShown) {
		String address = "";
		if (toBeShown != null) {
			address = forUnusedAddress(toBeShown.getAddress1(), false)
					+ forUnusedAddress(toBeShown.getStreet(), false)
					+ forUnusedAddress(toBeShown.getCity(), false)
					+ forUnusedAddress(toBeShown.getStateOrProvinence(), false)
					+ forUnusedAddress(toBeShown.getZipOrPostalCode(), false)
					+ forUnusedAddress(toBeShown.getCountryOrRegion(), false);
		}
		if (address.trim().length() > 0) {
			return address;
		}
		return "";
	}

	public String forUnusedAddress(String add, boolean isFooter) {
		if (isFooter) {
			if (add != null && !add.equals(""))
				return ", " + add;
		} else {
			if (add != null && !add.equals(""))
				return add + "\n";
		}
		return "";
	}

	private void setAddress(Set<Address> address) {
		if (address != null) {
			Iterator<Address> it = address.iterator();
			while (it.hasNext()) {
				Address addr = (Address) it.next();
				if (addr != null) {
					allAddresses.put(addr.getType(), addr);
				}
			}
		}
	}

	public class Paydetail {

		private String deductName;
		private String deductAmount;

		Paydetail(String deductName, String deductAmount) {
			this.deductName = deductName;
			this.deductAmount = deductAmount;
		}

		public String getDeductName() {
			return deductName;
		}

		public void setDeductName(String deductName) {
			this.deductName = deductName;
		}

		public String getDeductAmount() {
			return deductAmount;
		}

		public void setDeductAmount(String deductAmount) {
			this.deductAmount = deductAmount;
		}

	}

	public class DummyEmployee {

		private String name;
		private String number;
		private String designation;
		private String location;
		private String bankAccountNo;
		private String pfAccountNo;
		private String panNumber;
		private String totalEarnings;
		private String totalDeductions;
		private String takeHome;
		private String address;
		private String payDay;
		private String payPeriod;

		public String getDesignation() {
			return designation;
		}

		public void setDesignation(String designation) {
			this.designation = designation;
		}

		public String getLocation() {
			return location;
		}

		public void setLocation(String location) {
			this.location = location;
		}

		public String getBankAccountNo() {
			return bankAccountNo;
		}

		public void setBankAccountNo(String bankAccountNo) {
			this.bankAccountNo = bankAccountNo;
		}

		public String getPfAccountNo() {
			return pfAccountNo;
		}

		public void setPfAccountNo(String pfAccountNo) {
			this.pfAccountNo = pfAccountNo;
		}

		public String getPanNumber() {
			return panNumber;
		}

		public void setPanNumber(String panNumber) {
			this.panNumber = panNumber;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getNumber() {
			return number;
		}

		public void setNumber(String number) {
			this.number = number;
		}

		public String getTotalEarnings() {
			return totalEarnings;
		}

		public void setTotalEarnings(String totalEarnings) {
			this.totalEarnings = totalEarnings;
		}

		public String getTotalDeductions() {
			return totalDeductions;
		}

		public void setTotalDeductions(String totalDeductions) {
			this.totalDeductions = totalDeductions;
		}

		public String getTakeHome() {
			return takeHome;
		}

		public void setTakeHome(String takeHome) {
			this.takeHome = takeHome;
		}

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

		public String getPayDay() {
			return payDay;
		}

		public void setPayDay(String payDay) {
			this.payDay = payDay;
		}

		public String getPayPeriod() {
			return payPeriod;
		}

		public void setPayPeriod(String payPeriod) {
			this.payPeriod = payPeriod;
		}
	}

	public class Detail {

		private String earnName;
		private String earnAmount;
		private String deductName;
		private String deductAmount;

		Detail(String earnName, String earnAmount, String deductName,
				String deductAmount) {
			this.earnName = earnName;
			this.earnAmount = earnAmount;
			this.deductName = deductName;
			this.deductAmount = deductAmount;
		}

		public String getEarnName() {
			return earnName;
		}

		public void setEarnName(String earnName) {
			this.earnName = earnName;
		}

		public String getEarnAmount() {
			return earnAmount;
		}

		public void setEarnAmount(String earnAmount) {
			this.earnAmount = earnAmount;
		}

		public String getDeductName() {
			return deductName;
		}

		public void setDeductName(String deductName) {
			this.deductName = deductName;
		}

		public String getDeductAmount() {
			return deductAmount;
		}

		public void setDeductAmount(String deductAmount) {
			this.deductAmount = deductAmount;
		}

	}

	@Override
	public String getTemplateName() {
		return "templetes" + File.separator + "payslip.odt";
	}

	@Override
	public String getFileName() {
		return "PaySlip_" + employee.getNumber();
	}

}
