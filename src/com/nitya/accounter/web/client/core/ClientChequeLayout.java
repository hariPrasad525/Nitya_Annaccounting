package com.nitya.accounter.web.client.core;

public class ClientChequeLayout implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int version;
	private long id;

	private long account;
	private String authorisedSignature = "";
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

	@Override
	public int getVersion() {
		return version;
	}

	@Override
	public void setVersion(int version) {
		this.version = version;
	}

	@Override
	public String getName() {
		return authorisedSignature;
	}

	@Override
	public String getDisplayName() {
		return authorisedSignature;
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.CHEQUE_LAYOUT;
	}

	@Override
	public void setID(long id) {
		this.id = id;
	}

	@Override
	public long getID() {
		return id;
	}

	public long getAccount() {
		return account;
	}

	public void setAccount(long account) {
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

	public ClientChequeLayout clone() {
		ClientChequeLayout layout = new ClientChequeLayout();
		layout.setVersion(this.getVersion());
		layout.setID(this.getID());
		layout.setAccount(this.getAccount());
		layout.setAuthorisedSignature(this.getAuthorisedSignature());
		layout.setChequeHeight(this.getChequeHeight());
		layout.setChequeWidth(this.getChequeWidth());
		layout.setPayeeNameTop(this.getPayeeNameTop());
		layout.setPayeeNameLeft(this.getPayeeNameLeft());
		layout.setPayeeNameWidth(this.getPayeeNameWidth());
		layout.setAmountWordsLin1Top(this.getAmountWordsLin1Top());
		layout.setAmountWordsLin1Left(this.getAmountWordsLin1Left());
		layout.setAmountWordsLin1Width(this.getAmountWordsLin1Width());
		layout.setAmountWordsLin2Top(this.getAmountWordsLin2Top());
		layout.setAmountWordsLin2Left(this.getAmountWordsLin2Left());
		layout.setAmountWordsLin2Width(this.getAmountWordsLin2Width());
		layout.setAmountFigTop(this.getAmountFigTop());
		layout.setAmountFigLeft(this.getAmountFigLeft());
		layout.setAmountFigWidth(this.getAmountFigWidth());
		layout.setChequeDateTop(this.getChequeDateTop());
		layout.setChequeDateLeft(this.getChequeDateLeft());
		layout.setChequeDateWidth(this.getChequeDateWidth());
		layout.setCompanyNameTop(this.getCompanyNameTop());
		layout.setCompanyNameLeft(this.getCompanyNameLeft());
		layout.setCompanyNameWidth(this.getCompanyNameWidth());
		layout.setSignatoryTop(this.getSignatoryTop());
		layout.setSignatoryLeft(this.getSignatoryLeft());
		layout.setSignatoryWidth(this.getSignatoryWidth());
		return layout;
	}
}
