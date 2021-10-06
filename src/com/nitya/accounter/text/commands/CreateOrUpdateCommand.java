package com.nitya.accounter.text.commands;

import org.hibernate.Session;

import com.nitya.accounter.core.Activity;
import com.nitya.accounter.core.ActivityType;
import com.nitya.accounter.core.Company;
import com.nitya.accounter.core.CreatableObject;
import com.nitya.accounter.core.IAccounterServerCore;
import com.nitya.accounter.core.Quantity;
import com.nitya.accounter.web.client.exception.AccounterException;

public abstract class CreateOrUpdateCommand extends AbstractTextCommand {

	protected void saveOrUpdate(IAccounterServerCore serverObj)
			throws AccounterException {
		Company company = getCompany();
		if (serverObj instanceof CreatableObject) {
			((CreatableObject) serverObj).setCompany(company);
		}
		serverObj.selfValidate();
		Session session = getSession();
		boolean isAdd = serverObj.getID() == 0;
		// Save or Update Object
		session.saveOrUpdate(serverObj);

		// Create Activity
		Activity activity = new Activity(company, getUser(),
				(isAdd ? ActivityType.ADD : ActivityType.EDIT), serverObj);
		session.save(activity);

	}

	public class TransctionItem {

		private String item;
		private Double unitPrice;
		private Quantity quantity;
		private String tax;
		private String description;
		private Double amount;
		private String account;

		public String getDescription() {
			return description;
		}

		public String getTax() {
			return this.tax;
		}

		public Quantity getQuantity() {
			return this.quantity;
		}

		public Double getUnitPrice() {
			return this.unitPrice;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public void setTax(String tax) {
			this.tax = tax;
		}

		public void setQuantity(Quantity quantity) {
			this.quantity = quantity;
		}

		public void setUnitPrice(Double unitPrice) {
			this.unitPrice = unitPrice;
		}

		public Double getAmount() {
			return amount;
		}

		public void setAmount(Double amount) {
			this.amount = amount;
		}

		public String getAccount() {
			return account;
		}

		public void setAccount(String account) {
			this.account = account;
		}

		public String getItem() {
			return item;
		}

		public void setItem(String item) {
			this.item = item;
		}
	}
}
