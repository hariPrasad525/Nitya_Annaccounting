package com.nitya.accounter.core;

import java.io.File;

import com.nitya.accounter.main.ServerConfiguration;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientTransaction;

import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.template.IContext;

public abstract class TransactionPDFGeneration {

	private Transaction transaction;

	private BrandingTheme brandingTheme;

	public TransactionPDFGeneration(Transaction transaction,
			BrandingTheme brandingTheme) {
		this.setTransaction(transaction);
		this.setBrandingTheme(brandingTheme);
	}

	public abstract IContext assignValues(IContext context, IXDocReport report);

	public Transaction getTransaction() {
		return transaction;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	public Company getCompany() {
		return transaction.getCompany();
	}

	public abstract String getTemplateName();

	public abstract String getFileName();

	public BrandingTheme getBrandingTheme() {
		return brandingTheme;
	}

	public void setBrandingTheme(BrandingTheme brandingTheme) {
		this.brandingTheme = brandingTheme;
	}

	protected String forUnusedAddress(String add, boolean isFooter) {
		if (isFooter) {
			if (add != null && !add.equals(""))
				return add + ", ";
		} else {
			if (add != null && !add.equals(""))
				return add + "\n";
		}
		return "";
	}

	protected String forNullValue(String value) {
		return value != null ? value : "";
	}

	protected String forZeroAmounts(String amount) {
		String[] amt = amount.replace(".", "-").split("-");
		if (amt[0].equals("0")) {
			return "";
		}
		return amount;
	}

	protected String getRegisteredAddress() {
		Company company = getCompany();
		String regestrationAddress = "";
		Address reg = company.getRegisteredAddress();

		if (reg != null)
			regestrationAddress = ("Registered Address: " + reg.getAddress1()
					+ forUnusedAddress(reg.getStreet(), true)
					+ forUnusedAddress(reg.getCity(), true)
					+ forUnusedAddress(reg.getStateOrProvinence(), true)
					+ forUnusedAddress(reg.getZipOrPostalCode(), true)
					+ forNullValue(reg.getCountryOrRegion()) + ".");

		regestrationAddress = (company.getTradingName() + " "
				+ regestrationAddress + ((company.getRegistrationNumber() != null && !company
				.getRegistrationNumber().equals("")) ? "\n Company Registration No: "
				+ company.getRegistrationNumber()
				: ""));
		String phoneStr = forNullValue(company.getPreferences().getPhone());
		if (phoneStr.trim().length() > 0) {
			regestrationAddress = regestrationAddress
					+ Global.get().messages().phone() + " : " + phoneStr + ",";
		}
		String website = forNullValue(company.getPreferences().getWebSite());

		if (website.trim().length() > 0) {
			regestrationAddress = regestrationAddress
					+ Global.get().messages().webSite() + " : " + website;
		}
		return regestrationAddress;

	}

	protected String getImage() {
		Company company = getCompany();
		BrandingTheme brandingTheme = getBrandingTheme();
		StringBuffer original = new StringBuffer();

		File file = new File(ServerConfiguration.getAttachmentsDir()
				+ File.separator + company.getId() + File.separator
				+ "thumbnail" + File.separator + brandingTheme.getFileName());
		if (file.exists()) {
			original.append(ServerConfiguration.getAttachmentsDir()
					+ File.separator + company.getId() + File.separator
					+ "thumbnail" + File.separator
					+ brandingTheme.getFileName());
		} else {
			original.append(ServerConfiguration.getAttachmentsDir()
					+ File.separator + company.getId() + File.separator
					+ brandingTheme.getFileName());
		}

		if (original.toString().contains("null")) {
			return "";
		}

		return original.toString();

	}

	protected String getStatusString(int status) {
		switch (status) {
		case ClientTransaction.STATUS_OPEN:
			return Global.get().messages().open();
		case ClientTransaction.STATUS_COMPLETED:
			return Global.get().messages().completed();
		case ClientTransaction.STATUS_CANCELLED:
			return Global.get().messages().cancelled();
		default:
			break;
		}
		return "";
	}

	public class ItemList {
		private String name;
		private String description;
		private String quantity;
		private String itemUnitPrice;
		private String discount;
		private String itemTotalPrice;
		private String itemVatRate;
		private String itemVatAmount;

		ItemList(String name, String description, String quantity,
				String itemUnitPrice, String discount, String itemTotalPrice,
				String itemVatRate, String itemVatAmount) {
			this.name = name;
			this.description = description;
			this.quantity = quantity;
			this.itemUnitPrice = itemUnitPrice;
			this.discount = discount;
			this.itemTotalPrice = itemTotalPrice;
			this.itemVatRate = itemVatRate;
			this.itemVatAmount = itemVatAmount;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getQuantity() {
			return quantity;
		}

		public void setQuantity(String quantity) {
			this.quantity = quantity;
		}

		public String getItemUnitPrice() {
			return itemUnitPrice;
		}

		public void setItemUnitPrice(String itemUnitPrice) {
			this.itemUnitPrice = itemUnitPrice;
		}

		public String getDiscount() {
			return discount;
		}

		public void setDiscount(String discount) {
			this.discount = discount;
		}

		public String getItemTotalPrice() {
			return itemTotalPrice;
		}

		public void setItemTotalPrice(String itemTotalPrice) {
			this.itemTotalPrice = itemTotalPrice;
		}

		public String getItemVatRate() {
			return itemVatRate;
		}

		public void setItemVatRate(String itemVatRate) {
			this.itemVatRate = itemVatRate;
		}

		public String getItemVatAmount() {
			return itemVatAmount;
		}

		public void setItemVatAmount(String itemVatAmount) {
			this.itemVatAmount = itemVatAmount;
		}

	}
}
