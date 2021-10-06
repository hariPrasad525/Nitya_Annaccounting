package com.vimukti.accounter.core;

import java.util.List;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.json.JSONException;

import com.vimukti.accounter.core.change.ChangeTracker;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.exception.AccounterException;

public class BuildAssembly extends Transaction {

	private InventoryAssembly inventoryAssembly;
	private Double quantityToBuild;

	private static final long serialVersionUID = -5038297775075086681L;

	public BuildAssembly() {
		super();
		setType(TYPE_BUILD_ASSEMBLY);
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {

		if (number == null) {
			String number = NumberUtils.getNextTransactionNumber(
					Transaction.TYPE_BUILD_ASSEMBLY, getCompany());
			setNumber(number);
		}

		Double total = 0.0;

		for (InventoryAssemblyItem assemblyItem : inventoryAssembly
				.getComponents()) {

			Item inventoryItem = assemblyItem.getInventoryItem();
			TransactionItem transactionItem = new TransactionItem();
			transactionItem.setType(TransactionItem.TYPE_ITEM);
			transactionItem.setTransaction(this);
			Quantity quantity = assemblyItem.getQuantity().copy();
			quantity.setValue(quantity.getValue() * quantityToBuild);
			transactionItem.setQuantity(quantity);
			transactionItem.setUnitPrice(inventoryItem.getAverageCost());
			transactionItem.setItem(inventoryItem);
			transactionItem.setDescription(Global.get().messages()
					.buildAssembly());
			transactionItem.setWareHouse(assemblyItem.getWarehouse());
			transactionItem.setLineTotal(assemblyItem.getQuantity()
					.calculatePrice(inventoryItem.getAverageCost()));
			getTransactionItems().add(transactionItem);
			total += transactionItem.getLineTotal();
		}
		this.total = total;

		ChangeTracker.put(inventoryAssembly);
		return super.onSave(session);
	}

	@Override
	public void onEdit(Transaction clonedObject) throws AccounterException {
		super.onEdit(clonedObject);
		if (isDraftOrTemplate()) {
			return;
		}
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isPositiveTransaction() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isDebitTransaction() {
		return false;
	}

	@Override
	public int getTransactionCategory() {
		return Transaction.CATEGORY_CUSTOMER;
	}

	@Override
	public String toString() {
		return AccounterServerConstants.TYPE_BUILD_ASSEMBLY;
	}

	@Override
	public Payee getInvolvedPayee() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @return the inventoryAssembly
	 */
	public InventoryAssembly getInventoryAssembly() {
		return inventoryAssembly;
	}

	/**
	 * @param inventoryAssembly
	 *            the inventoryAssembly to set
	 */
	public void setInventoryAssembly(InventoryAssembly inventoryAssembly) {
		this.inventoryAssembly = inventoryAssembly;
	}

	/**
	 * @return the quantityToBuild
	 */
	public Double getQuantityToBuild() {
		return quantityToBuild;
	}

	/**
	 * @param quantityToBuild
	 *            the quantityToBuild to set
	 */
	public void setQuantityToBuild(Double quantityToBuild) {
		this.quantityToBuild = quantityToBuild;
	}

	@Override
	public void getEffects(ITransactionEffects e) {
		Quantity quantityToBuild = inventoryAssembly.getOnhandQty().copy();
		quantityToBuild.setValue(this.quantityToBuild);
		e.add(getInventoryAssembly(), quantityToBuild, getTotal(),
				inventoryAssembly.getWarehouse());
		e.addInventoryHistory(getInventoryAssembly(), quantityToBuild,
				getTotal(), inventoryAssembly.getWarehouse());
		for (TransactionItem tItem : getTransactionItems()) {
			Item item = tItem.getItem();
			e.add(item, tItem.getQuantity().reverse(), tItem.getLineTotal(),
					tItem.getWareHouse());
			tItem.addPurchasesEffects(e);
		}
	}

	@Override
	public void selfValidate() throws AccounterException {
		if (inventoryAssembly == null) {
			throw new AccounterException(AccounterException.ERROR_OBJECT_NULL,
					Global.get().messages().assemblyItem());
		} else if (!inventoryAssembly.isActive()) {
			throw new AccounterException(AccounterException.ERROR_ACTIVE_ITEM,
					Global.get().messages().assemblyItem());
		}
		if (quantityToBuild <= 0) {
			throw new AccounterException(
					AccounterException.ERROR_QUANTITY_ZERO_OR_NEGATIVE, Global
							.get().messages().quantityToBuild());
		}
	}

	@Override
	public List<Item> getInventoryUsed() {
		List<Item> inventoryUsed = super.getInventoryUsed();
		inventoryUsed.add(inventoryAssembly);
		return inventoryUsed;
	}
}
