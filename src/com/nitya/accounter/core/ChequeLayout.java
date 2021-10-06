package com.nitya.accounter.core;

import org.json.JSONException;

import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.exception.AccounterException;

public class ChequeLayout extends CreatableObject implements
		IAccounterServerCore, INamedObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BankAccount account;
	private String authorisedSignature;
	private double chequeHeight;
	private double chequeWidth;
	private double payeeNameTop;
	private double payeeNameLeft;
	private double payeeNameWidth;
	private double amountWordsLin1Top;
	private double amountWordsLin1Left;
	private double amountWordsLin1Width;
	private double amountWordsLin2Top;
	private double amountWordsLin2Left;
	private double amountWordsLin2Width;
	private double amountFigTop;
	private double amountFigLeft;
	private double amountFigWidth;
	private double chequeDateTop;
	private double chequeDateLeft;
	private double chequeDateWidth;
	private double companyNameTop;
	private double companyNameLeft;
	private double companyNameWidth;
	private double signatoryTop;
	private double signatoryLeft;
	private double signatoryWidth;

	public ChequeLayout() {
		super();
	}

	public BankAccount getAccount() {
		return account;
	}

	public void setAccount(BankAccount account) {
		this.account = account;
	}

	public String getAuthorisedSignature() {
		return authorisedSignature;
	}

	public void setAuthorisedSignature(String authorisedSignature) {
		this.authorisedSignature = authorisedSignature;
	}

	public double getChequeHeight() {
		return chequeHeight;
	}

	public void setChequeHeight(double chequeHeight) {
		this.chequeHeight = chequeHeight;
	}

	public double getChequeWidth() {
		return chequeWidth;
	}

	public void setChequeWidth(double chequeWidth) {
		this.chequeWidth = chequeWidth;
	}

	public double getPayeeNameTop() {
		return payeeNameTop;
	}

	public void setPayeeNameTop(double payeeNameTop) {
		this.payeeNameTop = payeeNameTop;
	}

	public double getPayeeNameLeft() {
		return payeeNameLeft;
	}

	public void setPayeeNameLeft(double payeeNameLeft) {
		this.payeeNameLeft = payeeNameLeft;
	}

	public double getPayeeNameWidth() {
		return payeeNameWidth;
	}

	public void setPayeeNameWidth(double payeeNameWidth) {
		this.payeeNameWidth = payeeNameWidth;
	}

	public double getAmountWordsLin1Top() {
		return amountWordsLin1Top;
	}

	public void setAmountWordsLin1Top(double amountWordsLin1Top) {
		this.amountWordsLin1Top = amountWordsLin1Top;
	}

	public double getAmountWordsLin1Left() {
		return amountWordsLin1Left;
	}

	public void setAmountWordsLin1Left(double amountWordsLin1Left) {
		this.amountWordsLin1Left = amountWordsLin1Left;
	}

	public double getAmountWordsLin1Width() {
		return amountWordsLin1Width;
	}

	public void setAmountWordsLin1Width(double amountWordsLin1Width) {
		this.amountWordsLin1Width = amountWordsLin1Width;
	}

	public double getAmountWordsLin2Top() {
		return amountWordsLin2Top;
	}

	public void setAmountWordsLin2Top(double amountWordsLin2Top) {
		this.amountWordsLin2Top = amountWordsLin2Top;
	}

	public double getAmountWordsLin2Left() {
		return amountWordsLin2Left;
	}

	public void setAmountWordsLin2Left(double amountWordsLin2Left) {
		this.amountWordsLin2Left = amountWordsLin2Left;
	}

	public double getAmountWordsLin2Width() {
		return amountWordsLin2Width;
	}

	public void setAmountWordsLin2Width(double amountWordsLin2Width) {
		this.amountWordsLin2Width = amountWordsLin2Width;
	}

	public double getAmountFigTop() {
		return amountFigTop;
	}

	public void setAmountFigTop(double amountFigTop) {
		this.amountFigTop = amountFigTop;
	}

	public double getAmountFigLeft() {
		return amountFigLeft;
	}

	public void setAmountFigLeft(double amountFigLeft) {
		this.amountFigLeft = amountFigLeft;
	}

	public double getAmountFigWidth() {
		return amountFigWidth;
	}

	public void setAmountFigWidth(double amountFigWidth) {
		this.amountFigWidth = amountFigWidth;
	}

	public double getChequeDateTop() {
		return chequeDateTop;
	}

	public void setChequeDateTop(double chequeDateTop) {
		this.chequeDateTop = chequeDateTop;
	}

	public double getChequeDateLeft() {
		return chequeDateLeft;
	}

	public void setChequeDateLeft(double chequeDateLeft) {
		this.chequeDateLeft = chequeDateLeft;
	}

	public double getChequeDateWidth() {
		return chequeDateWidth;
	}

	public void setChequeDateWidth(double chequeDateWidth) {
		this.chequeDateWidth = chequeDateWidth;
	}

	public double getCompanyNameTop() {
		return companyNameTop;
	}

	public void setCompanyNameTop(double companyNameTop) {
		this.companyNameTop = companyNameTop;
	}

	public double getCompanyNameLeft() {
		return companyNameLeft;
	}

	public void setCompanyNameLeft(double companyNameLeft) {
		this.companyNameLeft = companyNameLeft;
	}

	public double getCompanyNameWidth() {
		return companyNameWidth;
	}

	public void setCompanyNameWidth(double companyNameWidth) {
		this.companyNameWidth = companyNameWidth;
	}

	public double getSignatoryTop() {
		return signatoryTop;
	}

	public void setSignatoryTop(double signatoryTop) {
		this.signatoryTop = signatoryTop;
	}

	public double getSignatoryLeft() {
		return signatoryLeft;
	}

	public void setSignatoryLeft(double signatoryLeft) {
		this.signatoryLeft = signatoryLeft;
	}

	public double getSignatoryWidth() {
		return signatoryWidth;
	}

	public void setSignatoryWidth(double signatoryWidth) {
		this.signatoryWidth = signatoryWidth;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {
		return true;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
	}

	@Override
	public String getName() {
		return getAuthorisedSignature();
	}

	@Override
	public void setName(String name) {
		this.setAuthorisedSignature(name);
	}

	@Override
	public int getObjType() {
		return IAccounterCore.CHECK_LAYOUT;
	}

	@Override
	public void selfValidate() throws AccounterException {
		if (this.account == null) {
			throw new AccounterException(AccounterException.ERROR_NAME_NULL,
					Global.get().messages().account());
		}

	}

}
