package com.nitya.accounter.core;

import org.json.JSONException;

import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.ui.core.DecimalUtil;

/**
 * 
 * Any transaction which is liable to VAT must be recorded in this
 * VATRateCalculation. It contains all the information about the transaction
 * item, the vat item used, the return box of the vat item, vat item's purchase
 * and sales liabilities accounts, the date of the transaction, the rate by
 * which this vat item effects the transaction item, the vat agency related to
 * that vat item and the {@link VATReturn} reference. All these references are
 * backed up here so as to maintain the record's references as they may get
 * changed on time.
 * 
 * @author Chandan
 * 
 */
public class TAXRateCalculation implements IAccounterServerCore {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9008613714032328895L;
	long id;
	private boolean isVATGroupEntry;
	double taxAmount;
	double lineTotal;
	Transaction transaction;
	TAXItem taxItem;
	FinanceDate transactionDate;
	private int version;
	TAXAgency taxAgency;
	double rate;
	VATReturnBox vatReturnBox;
	Account purchaseLiabilityAccount;
	Account salesLiabilityAccount;

	/**
	 * This reference to VATReturn is used to indicate whether this entry of
	 * VATRateCalculation is filed to any VATReturn.
	 */
	TAXReturn taxReturn;

	public TAXRateCalculation() {
	}

	public TAXRateCalculation(TAXItem taxItem, Transaction transacton,
			double lineTotal) {

		this.transaction = transacton;
		this.taxItem = taxItem;
		this.transactionDate = transacton.getDate();
		this.taxAgency = taxItem.getTaxAgency();
		this.rate = taxItem.getTaxRate();
		this.vatReturnBox = taxItem.getVatReturnBox();
		this.purchaseLiabilityAccount = this.taxAgency
				.getPurchaseLiabilityAccount();
		this.salesLiabilityAccount = this.taxAgency.getSalesLiabilityAccount();

		this.lineTotal = lineTotal * transacton.getCurrencyFactor();
		this.taxAmount = this.getCeilValueofTAX();
	}

	/**
	 * @return the isVATGroupEntry
	 */
	public boolean isVATGroupEntry() {
		return isVATGroupEntry;
	}

	/**
	 * @param isVATGroupEntry
	 *            the isVATGroupEntry to set
	 */
	public void setVATGroupEntry(boolean isVATGroupEntry) {
		this.isVATGroupEntry = isVATGroupEntry;
	}

	/**
	 * @return the id
	 */
	public long getID() {
		return id;
	}

	/**
	 * @return the vatAgency
	 */
	public TAXAgency getTaxAgency() {
		return taxAgency;
	}

	/**
	 * @param vatAgency
	 *            the vatAgency to set
	 */
	public void setTaxAgency(TAXAgency taxAgency) {
		this.taxAgency = taxAgency;
	}

	/**
	 * @return the rate
	 */
	public double getRate() {
		return rate;
	}

	/**
	 * @param rate
	 *            the rate to set
	 */
	public void setRate(double rate) {
		this.rate = rate;
	}

	/**
	 * @return the vatReturnBox
	 */
	public VATReturnBox getVatReturnBox() {
		return vatReturnBox;
	}

	/**
	 * @param vatReturnBox
	 *            the vatReturnBox to set
	 */
	public void setVatReturnBox(VATReturnBox vatReturnBox) {
		this.vatReturnBox = vatReturnBox;
	}

	/**
	 * @return the purchaseLiabilityAccount
	 */
	public Account getPurchaseLiabilityAccount() {
		return purchaseLiabilityAccount;
	}

	/**
	 * @param purchaseLiabilityAccount
	 *            the purchaseLiabilityAccount to set
	 */
	public void setPurchaseLiabilityAccount(Account purchaseLiabilityAccount) {
		this.purchaseLiabilityAccount = purchaseLiabilityAccount;
	}

	/**
	 * @return the salesLiabilityAccount
	 */
	public Account getSalesLiabilityAccount() {
		return salesLiabilityAccount;
	}

	/**
	 * @param salesLiabilityAccount
	 *            the salesLiabilityAccount to set
	 */
	public void setSalesLiabilityAccount(Account salesLiabilityAccount) {
		this.salesLiabilityAccount = salesLiabilityAccount;
	}

	/**
	 * @return the vatReturn
	 */
	public TAXReturn getTAXReturn() {
		return taxReturn;
	}

	/**
	 * @param vatReturn
	 *            the vatReturn to set
	 */
	public void setTAXReturn(TAXReturn vatReturn) {
		this.taxReturn = vatReturn;
	}

	/**
	 * @return the vatAmount
	 */
	public double getTAXAmount() {
		return taxAmount;
	}

	/**
	 * @param vatAmount
	 *            the vatAmount to set
	 */
	public void setTAXAmount(double vatAmount) {
		this.taxAmount = vatAmount;
	}

	/**
	 * 
	 * @return the lineTotal
	 */
	public double getLineTotal() {
		return lineTotal;
	}

	/**
	 * @param lineTotal
	 */
	public void setlineTotal(double lineTotal) {
		this.lineTotal = lineTotal;
	}

	/**
	 * @return the vatItem
	 */
	public TAXItem getTaxItem() {
		return taxItem;
	}

	/**
	 * @param vatItem
	 *            the vatItem to set
	 */
	public void setTaxItem(TAXItem taxItem) {
		this.taxItem = taxItem;
	}

	/**
	 * @return the transactionDate
	 */
	public FinanceDate getTransactionDate() {
		return transactionDate;
	}

	/**
	 * @param transactionDate
	 *            the transactionDate to set
	 */
	public void setTransactionDate(FinanceDate transactionDate) {
		this.transactionDate = transactionDate;
	}

	/**
	 * @return the transactionItem
	 */
	public Transaction getTransaction() {
		return transaction;
	}

	/**
	 * @param transactionItem
	 *            the transactionItem to set
	 */
	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {

		return true;
	}

	private double getCeilValueofTAX() {
		return this.lineTotal * this.rate / 100;
	}

	@Override
	public int getVersion() {
		return version;
	}

	@Override
	public void setVersion(int version) {
		this.version = version;

	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		// TODO Auto-generated method stub

	}

	@Override
	public int hashCode() {

		int hash = 7;
		long tID = getTransaction().getID();
		hash = 31 * hash + (int) (tID ^ (tID >>> 32));
		long iID = getTaxItem().getID();
		long bits = Double.doubleToLongBits(getTAXAmount());
		hash = (int) (iID ^ (iID >>> 32)) + (int) (bits ^ (bits >>> 32));
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof TAXRateCalculation)) {
			return false;
		}
		TAXRateCalculation other = (TAXRateCalculation) obj;
		if (this.getTransaction().getID() == other.getTransaction().getID()
				&& this.getTaxItem().getID() == other.getTaxItem().getID()
				&& this.getTaxAgency().getID() == other.getTaxAgency().getID()
				&& this.getVatReturnBox() != other.getVatReturnBox()
				&& this.getSalesLiabilityAccount() == other
						.getSalesLiabilityAccount()
				&& this.getPurchaseLiabilityAccount() == other
						.getPurchaseLiabilityAccount()
				&& DecimalUtil.isEquals(this.getRate(), other.getRate())
				&& DecimalUtil.isEquals(this.getLineTotal(),
						other.getLineTotal())
				&& DecimalUtil.isEquals(this.getTAXAmount(),
						other.getTAXAmount())
				&& this.isVATGroupEntry() == other.isVATGroupEntry()) {
			return true;
		}
		return false;
	}

	public void add(TAXRateCalculation trc) {
		this.lineTotal += trc.getLineTotal();
		this.taxAmount += trc.getTAXAmount();
	}

	@Override
	public void selfValidate() {
		// TODO Auto-generated method stub
		
	}

}
