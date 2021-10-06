package com.nitya.accounter.web.client.ui;
///**
// * 
// */
//package com.nitya.accounter.web.client.ui;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//import com.google.gwt.user.client.ui.FlexTable;
//import com.google.gwt.user.client.ui.HTML;
//import com.google.gwt.user.client.ui.HasHorizontalAlignment;
//import com.google.gwt.user.client.ui.StyledPanel;
//import com.google.gwt.user.client.ui.StyledPanel;
//import com.nitya.accounter.web.client.Global;
//import com.nitya.accounter.web.client.core.ClientAddress;
//import com.nitya.accounter.web.client.core.ClientCompany;
//import com.nitya.accounter.web.client.core.ClientCustomer;
//import com.nitya.accounter.web.client.core.ClientCustomerCreditMemo;
//import com.nitya.accounter.web.client.core.ClientFinanceDate;
//import com.nitya.accounter.web.client.core.ClientTAXCode;
//import com.nitya.accounter.web.client.core.ClientTAXGroup;
//import com.nitya.accounter.web.client.core.ClientTAXItem;
//import com.nitya.accounter.web.client.core.ClientTAXItemGroup;
//import com.nitya.accounter.web.client.core.ClientTransactionItem;
//import com.nitya.accounter.web.client.ui.customers.CustomerCreditMemoView;
//import com.nitya.accounter.web.client.ui.grids.AbstractTransactionGrid;
//
///**
// * @author Murali.A This class creates the printing template of CreditMemo
// */
//public class CustomerCreditMemoPrintLayout extends StyledPanel {
//
//	private String decimalCharacter = Global.get().preferences()
//			.getDecimalCharacter();
//
//	private ClientCustomerCreditMemo creditMemo;
//	private CustomerCreditMemoView view;
//	private StyledPanel vatPanel;
//	private Double vatTotal;
//	private double totalAmount;
//	private Double taxTotal;
//
//	public CustomerCreditMemoPrintLayout(ClientCustomerCreditMemo creditMemo) {
//		this.creditMemo = creditMemo;
//	}
//
//	public void setView(CustomerCreditMemoView view) {
//		this.view = view;
//	}
//
//	public void print() {
//		PrintUtils.print(this);
//	}
//
//	public void createTemplate() {
//		StyledPanel mainVPanel = new StyledPanel();
//		PrintTemplateUtils util = new PrintTemplateUtils();
//
//		List<String> removeHeaderBackground = new ArrayList<String>();
//
//		HTML lab1 = new HTML(messages.creaditHTML());
//
//		StyledPanel labeldateNoLayout = new StyledPanel();
//		labeldateNoLayout.setSize("auto", "100%");
//		labeldateNoLayout.add(lab1);
//
//		Map<String, String> dateNumMap = getMap(new String[] {
//				messages.customerName(Global.get().Customer()),
//				Accounter.getCompany().getCustomer(creditMemo.getCustomer())
//						.getName(),
//				messages.creditDate(),
//				UIUtils.dateFormat(new ClientFinanceDate(creditMemo
//						.getTransactionDate())),
//				messages.creditNo(), creditMemo.getNumber() + "" });
//
//		StyledPanel datepanel = new StyledPanel();
//		datepanel.setSize("300px", "150px");
//		FlexTable table = util.getWidget1(3, 2, dateNumMap, true,
//				removeHeaderBackground);
//		table.setSize("100%", "100%");
//		datepanel.add(table);
//
//		StyledPanel adressHPanel = new StyledPanel();
//		adressHPanel.setSize("100%", "100%");
//		adressHPanel.setSpacing(10);
//		adressHPanel.getElement().setAttribute("cellpaddding", "1");
//
//		ClientCustomer customer = Accounter.getCompany().getCustomer(
//				creditMemo.getCustomer());
//		String billAdrs = "<br/><br/><br/><br/><br/>";
//		Set<ClientAddress> bill = customer.getAddress();
//		for (ClientAddress add : bill) {
//			if (add.getType() == ClientAddress.TYPE_BILL_TO) {
//				if (add != null) {
//					billAdrs = "<p align=\"left\"><font size=\"3\">"
//							+ add.getAddress1() + "<br/>" + add.getStreet()
//							+ "<br/>" + add.getCity() + "<br/>"
//							+ add.getStateOrProvinence() + "<br/>"
//							+ add.getZipOrPostalCode() + "<br/>"
//							+ add.getCountryOrRegion() + "</font></p>";
//				}
//			}
//		}
//
//		Map<String, String> billAdrsMap = getMap(messages
//				.customerBillTo(Global.get().Customer()), billAdrs);
//		FlexTable billToTable = util.getWidget(2, 1, billAdrsMap, true);
//		billToTable.setSize("280px", "150px");
//		adressHPanel.add(billToTable);
//		adressHPanel.setCellHorizontalAlignment(billToTable,
//				HasHorizontalAlignment.ALIGN_LEFT);
//
//		StyledPanel reasonPanel = new StyledPanel();
//		reasonPanel.setSize("100%", "100%");
//
//		String memo = creditMemo.getMemo() + "<br/>";
//		Map<String, String> reasonMap = getMap(messages
//				.reasonForIssue(), memo);
//		FlexTable reasonTabel = util.getWidget(2, 1, reasonMap, true);
//		reasonTabel.setSize("100%", "75px");
//		reasonPanel.add(reasonTabel);
//		// StyledPanel customerNameHPanel = new StyledPanel();
//		// customerNameHPanel.setSize("100%", "100%");
//		// customerNameHPanel.setSpacing(10);
//		// customerNameHPanel.getElement().setAttribute("cellpaddding", "1");
//		// ClientCustomer customer =
//		// FinanceApplication.getCompany().getCustomer(
//		// creditMemo.getCustomer());
//		// String custName = customer != null ? customer.getName() : " ";
//		// Map<String, String> custNameMap = getMap("Customer", custName);
//		// FlexTable custNameTable = util.getWidget(2, 1, custNameMap, true);
//		// custNameTable.setSize("220px", "100px");
//		// customerNameHPanel.add(custNameTable);
//		//
//		// Map<String, String> projectMap = getMap(new String[] { "P.O. NO.",
//		// "&nbsp;", "Project", "&nbsp;" });
//		//
//		// StyledPanel projectPanel = new StyledPanel();
//		// projectPanel.setSize("100%", "100%");
//		// FlexTable projectTable = util.getWidget(2, 2, projectMap, true);
//		// projectTable.setSize("220px", "100px");
//		// projectTable.getElement().setAttribute("align", "right");
//		// customerNameHPanel.add(projectTable);
//
//		Map<List<String>, Map<Integer, List<String>>> gridData = getGridDataMap();
//		StyledPanel gridPanel = new StyledPanel();
//		FlexTable grid = util.getGridWidget(gridData, true);
//		grid.setSize("auto", "72px");
//		gridPanel.add(grid);
//		gridPanel.setSize("100%", "100%");
//
//		if (Accounter.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
//			vatPanel = new StyledPanel();
//
//			vatPanel.setSize("100%", "100%");
//			List<String> footer1Headers = new ArrayList<String>();
//			footer1Headers.add("VAT%");
//			footer1Headers.add(messages.vatAmount());
//
//			FlexTable vatTabl = util.getFooterWidget(
//					getVATDetailsMapForPrinting(), footer1Headers);
//			vatTabl.getElement().setAttribute("style",
//					"border-left:1px solid #000;border-bottom:1px solid #000;");
//			vatTabl.setSize("100%", "100%");
//			// vatPanel.add(vatTabl);
//		}
//
//		double lineTotal = view.getGridForPrinting().getTotal();
//
//		if (Accounter.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
//			vatTotal = view.getGridForPrinting().getVatTotal();
//			totalAmount = lineTotal + vatTotal;
//
//			Map<String, String> footer2Map = new LinkedHashMap<String, String>();
//			footer2Map = getMap(
//					"Sub Total",
//					"<p align=\"right\">"
//							+ DataUtils.getAmountAsString(lineTotal) + "</p>",
//					"VAT Total",
//					"<p align=\"right\">"
//							+ DataUtils.getAmountAsString(vatTotal) + "</p>",
//					"Total",
//					"<p align=\"right\">"
//							+ DataUtils.getAmountAsString(totalAmount) + "</p>");
//			// footer2Map.put("0",
//			// "<strong>Sub Total</strong>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
//			// + DataUtils.getAmountAsString(lineTotal));
//			// footer2Map.put("1",
//			// "<strong>VAT Total</strong>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
//			// + DataUtils.getAmountAsString(vatTotal));
//			// footer2Map
//			// .put(
//			// "2",
//			// "<strong>Total</strong>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
//			// + DataUtils.getAmountAsString(totalAmount));
//			FlexTable totalsTabel = util.getWidget1(3, 2, footer2Map, true,
//					removeHeaderBackground);
//			totalsTabel.setSize("50%", "50%");
//			vatPanel.add(totalsTabel);
//			vatPanel.setCellHorizontalAlignment(totalsTabel,
//					HasHorizontalAlignment.ALIGN_RIGHT);
//
//		} else {
//			taxTotal = view.getSalesTax();
//			totalAmount = lineTotal + taxTotal;
//
//			Map<String, String> footer2Map = new LinkedHashMap<String, String>();
//			footer2Map = getMap(
//					"Sub Total",
//					"<p align=\"right\">"
//							+ DataUtils.getAmountAsString(lineTotal) + "</p>",
//					"Total Tax",
//					"<p align=\"right\">"
//							+ DataUtils.getAmountAsString(taxTotal) + "</p>",
//					"Total",
//					"<p align=\"right\">"
//							+ DataUtils.getAmountAsString(totalAmount) + "</p>");
//
//			// footer2Map.put("0",
//			// "<strong>Sub Total</strong>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
//			// + DataUtils.getAmountAsString(lineTotal));
//			// footer2Map.put("1",
//			// "<strong>Total Tax</strong>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp"
//			// + DataUtils.getAmountAsString(taxTotal));
//			// footer2Map
//			// .put(
//			// "2",
//			// "<strong>Total</strong>&nbsp;&nbsp;&nbsp;&nbsp&nbsp;&nbsp;&nbsp&nbsp;&nbsp;&nbsp"
//			// + DataUtils.getAmountAsString(totalAmount));
//			FlexTable totalsTable = util.getWidget1(3, 2, footer2Map, true,
//					removeHeaderBackground);
//			totalsTable.setCellPadding(3);
//			totalsTable.setSize("50%", "50%");
//			vatPanel.add(totalsTable);
//			vatPanel.setCellHorizontalAlignment(totalsTable,
//					HasHorizontalAlignment.ALIGN_RIGHT);
//		}
//		gridPanel.add(vatPanel);
//
//		mainVPanel.add(labeldateNoLayout);
//		labeldateNoLayout.getElement().getParentElement()
//				.setAttribute("align", "right");
//		mainVPanel.add(datepanel);
//		datepanel.getElement().getParentElement()
//				.setAttribute("align", "right");
//		adressHPanel.setStyleName(messages.shiftBottom());
//		mainVPanel.add(adressHPanel);
//
//		reasonPanel.setStyleName(messages.shiftBottom());
//		mainVPanel.add(reasonPanel);
//		// customerNameHPanel.setStyleName("ShiftBottom");
//		// mainVPanel.add(customerNameHPanel);
//		// projectPanel.setStyleName("ShiftBottom");
//		// mainVPanel.add(projectPanel);
//		gridPanel.setStyleName(messages.shiftBottom());
//		mainVPanel.add(gridPanel);
//
//		add(mainVPanel);
//		setSize("100%", "100%");
//	}
//
//	public Map<Integer, Map<String, String>> getVATDetailsMapForPrinting() {
//		Map<Integer, Map<String, String>> vatMap = new HashMap<Integer, Map<String, String>>();
//		Map<String, String> vat = new HashMap<String, String>();
//		;
//		double totalVATAmount = 0.0;
//		int r = 0;
//		for (ClientTransactionItem rec : creditMemo.getTransactionItems()) {
//			if (rec.getTaxCode() != 0) {
//				vat = new HashMap<String, String>();
//				String taxCodeWidRate = getTAXCodeName(rec.getTaxCode())
//						+ "@"
//						+ DataUtils.getNumberAsPercentString(getVATRate(rec)
//								+ "");
//				double vatAmount = getVATAmount(rec.getTaxCode(), rec);
//				totalVATAmount += vatAmount;
//				vat.put(taxCodeWidRate, amountAsString(vatAmount));
//				vatMap.put(r++, vat);
//			}
//		}
//		vat = new HashMap<String, String>();
//
//		vat.put(messages.totalVAT(),
//				amountAsString(totalVATAmount));
//
//		vatMap.put(r++, vat);
//		return vatMap;
//	}
//
//	public double getVATRate(ClientTransactionItem rec) {
//		double vatRate = 0.0;
//
//		long TAXCodeID = rec.getTaxCode();
//		if (TAXCodeID != 0) {
//			// Checking the selected object is VATItem or VATGroup.
//			// If it is VATItem,the we should get 'VATRate',otherwise
//			// 'GroupRate
//			try {
//				if (getCompany().getTAXItemGroup(
//						getCompany().getTAXCode(TAXCodeID)
//								.getTAXItemGrpForSales()) instanceof ClientTAXItem) {
//					// The selected one is VATItem,so get 'VATRate' from
//					// 'VATItem'
//					vatRate = ((ClientTAXItem) getCompany().getTAXItemGroup(
//							getCompany().getTAXCode(TAXCodeID)
//									.getTAXItemGrpForSales())).getTaxRate();
//				} else {
//					// The selected one is VATGroup,so get 'GroupRate' from
//					// 'VATGroup'
//					vatRate = ((ClientTAXGroup) getCompany().getTAXItemGroup(
//							getCompany().getTAXCode(TAXCodeID)
//									.getTAXItemGrpForSales())).getGroupRate();
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		return vatRate;
//	}
//
//	protected String getTAXCodeName(long taxCode) {
//		ClientTAXCode t = null;
//		if (taxCode != 0)
//			t = getCompany().getTAXCode(taxCode);
//		return t != null ? t.getName() : "";
//	}
//
//	public double getVATAmount(long TAXCodeID, ClientTransactionItem record) {
//
//		double vatRate = 0.0;
//		if (TAXCodeID != 0) {
//			// Checking the selected object is VATItem or VATGroup.
//			// If it is VATItem,the we should get 'VATRate',otherwise 'GroupRate
//			try {
//				ClientTAXItemGroup item = getCompany().getTAXItemGroup(
//						getCompany().getTAXCode(TAXCodeID)
//								.getTAXItemGrpForSales());
//				if (item == null) {
//					vatRate = 0.0;
//				} else if (item instanceof ClientTAXItem) {
//					// The selected one is VATItem,so get 'VATRate' from
//					// 'VATItem'
//					vatRate = ((ClientTAXItem) item).getTaxRate();
//				} else {
//					// The selected one is VATGroup,so get 'GroupRate' from
//					// 'VATGroup'
//					vatRate = ((ClientTAXGroup) item).getGroupRate();
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//
//		Double vat = 0.0;
//		if (transactionView.isShowPriceWithVat()) {
//			vat = ((ClientTransactionItem) record).getLineTotal()
//					- (100 * (((ClientTransactionItem) record).getLineTotal() / (100 + vatRate)));
//		} else {
//			vat = ((ClientTransactionItem) record).getLineTotal() * vatRate
//					/ 100;
//		}
//		vat = UIUtils.getRoundValue(vat);
//		return vat.doubleValue();
//	}
//
//	private ClientCompany getCompany() {
//		return Accounter.getCompany();
//	}
//
//	public String amountAsString(Double amount) {
//		return DataUtils.getAmountAsString(amount);
//	}
//
//	private Map<String, String> getMap(String... args) {
//		Map<String, String> dataMap = new LinkedHashMap<String, String>();
//		for (int i = 0; i < args.length; i++) {
//			if (i % 2 == 0)
//				dataMap.put(args[i], "");
//			else {
//				dataMap.put(args[i - 1], args[i]);
//			}
//
//		}
//		return dataMap;
//	}
//
//	private Map<List<String>, Map<Integer, List<String>>> getGridDataMap() {
//
//		AbstractTransactionGrid<ClientTransactionItem> grid = view
//				.getGridForPrinting();
//		List<String> headerNames = Arrays.asList(grid
//				.getColumnNamesForPrinting());
//
//		Map<List<String>, Map<Integer, List<String>>> gridData = new LinkedHashMap<List<String>, Map<Integer, List<String>>>();
//		int i = 0;
//		List<String> row;
//		Map<Integer, List<String>> rows = new LinkedHashMap<Integer, List<String>>();
//		for (ClientTransactionItem item : grid.getRecords()) {
//			row = new ArrayList<String>();
//			for (int col = 0; col < headerNames.size(); col++) {
//				String val = grid.getColumnValueForPrinting(item, col);
//				if (val != null && val.length() > 9) {
//					String split[] = val.split(" ");
//					val = "";
//					for (int j = 0; j < split.length; j++) {
//						if (split[j].length() > 9)
//							val = val
//									+ "  "
//									+ splitStringBy9(split[j].substring(8),
//											split[j].substring(0, 8));
//						else
//							val = val + "  " + split[j];
//					}
//				}
//				row.add(val);
//			}
//			rows.put(new Integer(i), row);
//			i++;
//		}
//		gridData.put(headerNames, rows);
//		return gridData;
//	}
//
//	public String splitStringBy9(String val, String valueTobesend) {
//		int length = val.length();
//		int l = 8;
//		if (length > 9) {
//			valueTobesend = valueTobesend + "  " + val.substring(0, 8);
//			valueTobesend = valueTobesend + "  "
//					+ splitStringBy9(val.substring(l++), valueTobesend);
//		} else {
//			valueTobesend = valueTobesend + "  " + val;
//		}
//		return valueTobesend;
//	}
//}
