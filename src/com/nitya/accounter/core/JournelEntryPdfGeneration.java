package com.nitya.accounter.core;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.nitya.accounter.main.ServerConfiguration;
import com.nitya.accounter.web.client.Global;

import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.images.ClassPathImageProvider;
import fr.opensagres.xdocreport.document.images.IImageProvider;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;

public class JournelEntryPdfGeneration extends TransactionPDFGeneration {

	public JournelEntryPdfGeneration(JournalEntry journalEntry,
			BrandingTheme brandingTheme) {
		super(journalEntry, brandingTheme);

	}

	public IContext assignValues(IContext context, IXDocReport report) {

		try {
			JournalEntry journalEntry = (JournalEntry) getTransaction();
			Company company = getCompany();
			IImageProvider logo = new ClassPathImageProvider(
					CashSalePdfGeneration.class, getImage());
			IImageProvider footerImg = new ClassPathImageProvider(
					CashSalePdfGeneration.class, "templetes" + File.separator
							+ "footer-print-img.jpg");

			FieldsMetadata imgMetaData = new FieldsMetadata();
			imgMetaData.addFieldAsImage("logo");
			imgMetaData.addFieldAsImage("companyImg");
			report.setFieldsMetadata(imgMetaData);

			// assigning the original values
			DummyJournalEntry i = new DummyJournalEntry();

			i.setTitle("Journal Entry");
			i.setJournalNumber(journalEntry.getNumber());
			i.setJournalDate(Utility.getDateInSelectedFormat(journalEntry
					.getDate()));
			i.setLocationName(journalEntry.getLocation() != null
					&& company.getPreferences().isLocationTrackingEnabled() ? Global
					.get().Location()
					+ journalEntry.getLocation().getLocationName() : "");
			i.setClassName(journalEntry.getAccounterClass() != null
					&& company.getPreferences().isClassTrackingEnabled() ? Global
					.get().messages().accounterClass()
					+ journalEntry.getAccounterClass().getclassName()
					: "");
			// for transactions
			FieldsMetadata headersMetaData = new FieldsMetadata();
			headersMetaData.addFieldAsList("item.account");
			headersMetaData.addFieldAsList("item.memo");
			headersMetaData.addFieldAsList("item.debit");
			headersMetaData.addFieldAsList("item.credit");

			report.setFieldsMetadata(headersMetaData);
			List<ItemList> itemList = new ArrayList<ItemList>();
			List<TransactionItem> transactionItems = journalEntry
					.getTransactionItems();
			double debitTotal = 0.0, creditTotal = 0.0;
			double currencyFactor = journalEntry.getCurrencyFactor();
			String symbol = journalEntry.getCurrency().getSymbol();
			for (Iterator iterator = transactionItems.iterator(); iterator
					.hasNext();) {
				TransactionItem item = (TransactionItem) iterator.next();
				String description = forNullValue(item.getDescription());
				description = description.replaceAll("\n", "<br/>");
				String accountName = item.getAccount().getName();
				String itemDescription = item.getDescription();
				double debit = 0.0, credit = 0.0;
				if (item.getLineTotal() > 0) {
					debit = item.getLineTotal();
				} else {
					credit = -1 * item.getLineTotal();
				}
				debitTotal += debit;
				creditTotal += credit;
				itemList.add(new ItemList(accountName, itemDescription, Utility
						.decimalConversation(debit, ""), Utility
						.decimalConversation(credit, "")));
			}
			String debittotal = Utility.decimalConversation(debitTotal
					/ currencyFactor, symbol);
			i.setDebitTotal(debittotal);
			String credittotal = Utility.decimalConversation(creditTotal
					/ currencyFactor, symbol);
			i.setCreditTotal(credittotal);
			i.setMemo(journalEntry.getMemo());
			i.setRegistrationAddress(getRegisteredAddress());

			context.put("logo", logo);
			context.put("JournalEntry", i);
			context.put("item", itemList);
			context.put("companyImg", footerImg);

			return context;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public class DummyJournalEntry {

		private String title;
		private String journalDate;
		private String debitTotal;
		private String creditTotal;
		private String journalNumber;
		private String locationName;
		private String className;
		private String memo;
		private String registrationAddress;

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getLocationName() {
			return locationName;
		}

		public void setLocationName(String locationName) {
			this.locationName = locationName;
		}

		public String getClassName() {
			return className;
		}

		public void setClassName(String className) {
			this.className = className;
		}

		public String getMemo() {
			return memo;
		}

		public void setMemo(String memo) {
			this.memo = memo;
		}

		public String getJournalDate() {
			return journalDate;
		}

		public void setJournalDate(String journalDate) {
			this.journalDate = journalDate;
		}

		public String getJournalNumber() {
			return journalNumber;
		}

		public void setJournalNumber(String journalNumber) {
			this.journalNumber = journalNumber;
		}

		public String getCreditTotal() {
			return creditTotal;
		}

		public void setCreditTotal(String creditTotal) {
			this.creditTotal = creditTotal;
		}

		public String getDebitTotal() {
			return debitTotal;
		}

		public void setDebitTotal(String debitTotal) {
			this.debitTotal = debitTotal;
		}

		public String getRegistrationAddress() {
			return registrationAddress;
		}

		public void setRegistrationAddress(String registrationAddress) {
			this.registrationAddress = registrationAddress;
		}

	}

	public class ItemList {

		private String account;
		private String memo;
		private String debit;
		private String credit;

		public ItemList(String account, String memo, String debit, String credit) {
			this.account = account;
			this.memo = memo;
			this.debit = debit;
			this.credit = credit;
		}

		public String getAccount() {
			return account;
		}

		public void setAccount(String account) {
			this.account = account;
		}

		public String getMemo() {
			return memo;
		}

		public void setMemo(String memo) {
			this.memo = memo;
		}

		public String getDebit() {
			return debit;
		}

		public void setDebit(String debit) {
			this.debit = debit;
		}

		public String getCredit() {
			return credit;
		}

		public void setCredit(String credit) {
			this.credit = credit;
		}
	}

	@Override
	public String getTemplateName() {
		BrandingTheme brandingTheme = getBrandingTheme();
		Company company = getCompany();
		if (brandingTheme.getPurchaseOrderTemplateName().contains(
				"Classic Template")) {
			return "templetes" + File.separator + "JournelEntryDocx.docx";
		}

		return ServerConfiguration.getAttachmentsDir() + "/" + company.getId()
				+ "/" + "templateFiles" + "/" + brandingTheme.getID() + "/"
				+ brandingTheme.getPurchaseOrderTemplateName();
	}

	@Override
	public String getFileName() {
		return "JournalEntry_" + getTransaction().getNumber();
	}
}
