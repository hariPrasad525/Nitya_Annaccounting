package com.vimukti.accounter.core.migration;


public class MakeDepositMigration {

	public void start() {/*
						 * System.out.println("Starting MakeDeposit Migration");
						 * Session session = HibernateUtil.openSession();
						 * Transaction hibernateTransaction =
						 * session.beginTransaction(); try { Criteria criteria =
						 * session.createCriteria(TransferFund.class);
						 * List<TransferFund> list = criteria.list(); for
						 * (TransferFund md : list) {
						 * List<TransactionMakeDeposit> tmdList = md
						 * .getTransactionMakeDeposit(); if (tmdList == null ||
						 * tmdList.isEmpty()) { continue; } if (tmdList.size()
						 * == 1) {
						 * md.setDepositFrom(tmdList.get(0).getAccount());
						 * session.saveOrUpdate(md); } else {
						 * TransactionMakeDeposit tmd1 = tmdList.get(0); double
						 * amount = md.getTotal() - tmd1.getAmount();
						 * md.setTotal(tmd1.getAmount());
						 * md.setDepositFrom(tmd1.getAccount()); Account
						 * depositIn = md.getDepositIn();
						 * depositIn.updateTotalBalance(-amount, 1);
						 * depositIn.setCurrenctBalance(-amount);
						 * TransactionMakeDeposit tmd2 = tmdList.get(1);
						 * TransferFund newMD = createNewMakeDeposit(md, tmd2);
						 * session.save(newMD); Set<AccountTransaction> listAT =
						 * md .getAccountTransactionEntriesList();
						 * Set<AccountTransaction> atToDelete = new
						 * HashSet<AccountTransaction>(); for
						 * (AccountTransaction at : listAT) { Account account =
						 * at.getAccount(); if (account.getID() ==
						 * md.getDepositIn().getID()) {
						 * at.setAmount(tmd1.getAmount());
						 * session.saveOrUpdate(at); } else if (account.getID()
						 * == tmd2.getAccount().getID()) { atToDelete.add(at); }
						 * } for (AccountTransaction at : atToDelete) {
						 * listAT.remove(at); session.delete(at); Account
						 * account = at.getAccount();
						 * account.updateTotalBalance(-at.getAmount(), 1);
						 * account.setCurrenctBalance(-at.getAmount()); }
						 * session.saveOrUpdate(md); } }
						 * hibernateTransaction.commit(); } catch (Exception e)
						 * { e.printStackTrace();
						 * hibernateTransaction.rollback(); } finally {
						 * session.close(); }
						 * System.out.println("MakeDeposit Migration Completed"
						 * );
						 */
	}

	// private TransferFund createNewMakeDeposit(TransferFund md,
	// TransactionMakeDeposit tmd2) {
	// AccounterThreadLocal.set(md.getCreatedBy());
	// TransferFund newMD = new TransferFund();
	// newMD.setCreatedBy(md.getCreatedBy());
	// newMD.setLastModifier(md.getLastModifier());
	// newMD.setCreatedDate(md.getCreatedDate());
	// newMD.setLastModifiedDate(md.getLastModifiedDate());
	// newMD.setCanVoidOrEdit(md.getCanVoidOrEdit());
	// newMD.setCashBackMemo(md.getCashBackMemo());
	// newMD.setCurrency(md.getCurrency());
	// newMD.setCurrencyFactor(md.getCurrencyFactor());
	// newMD.setIsDeposited(md.getIsDeposited());
	// newMD.setLocation(md.getLocation());
	// newMD.setMemo(md.getMemo());
	// newMD.setTotal(tmd2.getAmount());
	// newMD.setDepositIn(md.getDepositIn());
	// newMD.setDepositFrom(tmd2.getAccount());
	// newMD.setDate(md.getDate());
	// newMD.setCompany(md.getCompany());
	// return newMD;
	// }

}
