package com.vimukti.accounter.core.migration;

import java.util.List;

import org.hibernate.Query;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.AccountTransaction;
import com.vimukti.accounter.core.BuildAssembly;
import com.vimukti.accounter.core.CashPurchase;
import com.vimukti.accounter.core.CashSales;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.CreditCardCharge;
import com.vimukti.accounter.core.CustomerCreditMemo;
import com.vimukti.accounter.core.CustomerPrePayment;
import com.vimukti.accounter.core.CustomerRefund;
import com.vimukti.accounter.core.EmployeePayHeadComponent;
import com.vimukti.accounter.core.EmployeePaymentDetails;
import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.core.ItemReceipt;
import com.vimukti.accounter.core.JournalEntry;
import com.vimukti.accounter.core.MakeDeposit;
import com.vimukti.accounter.core.PayBill;
import com.vimukti.accounter.core.PayEmployee;
import com.vimukti.accounter.core.PayExpense;
import com.vimukti.accounter.core.PayRun;
import com.vimukti.accounter.core.PayTAX;
import com.vimukti.accounter.core.PurchaseOrder;
import com.vimukti.accounter.core.ReceivePayment;
import com.vimukti.accounter.core.ReceiveVAT;
import com.vimukti.accounter.core.StockAdjustment;
import com.vimukti.accounter.core.TAXAdjustment;
import com.vimukti.accounter.core.TAXRateCalculation;
import com.vimukti.accounter.core.TDSChalanDetail;
import com.vimukti.accounter.core.Transaction;
import com.vimukti.accounter.core.TransactionDepositItem;
import com.vimukti.accounter.core.TransactionItem;
import com.vimukti.accounter.core.TransactionMakeDepositEntries;
import com.vimukti.accounter.core.TransactionPayBill;
import com.vimukti.accounter.core.TransactionReceivePayment;
import com.vimukti.accounter.core.TransferFund;
import com.vimukti.accounter.core.VendorPrePayment;
import com.vimukti.accounter.core.WriteCheck;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.exception.AccounterException;

public class Migrator17 extends AbstractMigrator {

	@SuppressWarnings("unchecked")
	@Override
	public void migrate(Company company) throws AccounterException {
		log.info("Started Migrator17");
		Query query = getSession().getNamedQuery(
				"get.Incorrect.AccountTransactions").setEntity("company",
				company);
		List<AccountTransaction> list = query.list();
		if (list.isEmpty()) {
			// No Incorrect transaction. so, just return
			return;
		}
		for (AccountTransaction at : list) {
			Account wrongAccount = at.getAccount();
			// GET Correct Account with this name in the transaction company
			Query aQuery = getSession().getNamedQuery("get.Account.By.Name")
					.setEntity("company", company)
					.setParameter("name", wrongAccount.getName());
			Account correctAcc = (Account) aQuery.uniqueResult();
			if (correctAcc == null) {
				correctAcc = createAccount(company, wrongAccount.getType(),
						wrongAccount.getName(),
						wrongAccount.getCashFlowCategory());
			}
			correctTransaction(at.getTransaction(), correctAcc, wrongAccount);
		}
		log.info("Finished Migrator17");
	}

	private void correctTransaction(Transaction transaction,
			Account correctAcc, Account wrongAccount) throws AccounterException {
		transaction = HibernateUtil.initializeAndUnproxy(transaction);
		switch (transaction.getType()) {
		case Transaction.TYPE_CASH_SALES:
			CashSales sale = (CashSales) transaction;
			if (isEqual(sale.getDepositIn(), wrongAccount)) {
				sale.setDepositIn(correctAcc);
			}
			break;
		case Transaction.TYPE_CASH_PURCHASE:
			CashPurchase purchase = (CashPurchase) transaction;
			if (isEqual(purchase.getCashExpenseAccount(), wrongAccount)) {
				purchase.setCashExpenseAccount(correctAcc);
			}
			if (isEqual(purchase.getPayFrom(), wrongAccount)) {
				purchase.setPayFrom(correctAcc);
			}
			break;
		case Transaction.TYPE_CREDIT_CARD_CHARGE:
			CreditCardCharge charge = (CreditCardCharge) transaction;
			if (isEqual(charge.getPayFrom(), wrongAccount)) {
				charge.setPayFrom(correctAcc);
			}
			break;
		case Transaction.TYPE_BUILD_ASSEMBLY:
			BuildAssembly assembly = (BuildAssembly) transaction;
			correctItem(assembly.getInventoryAssembly(), correctAcc,
					wrongAccount);
			break;
		case Transaction.TYPE_CUSTOMER_CREDIT_MEMO:
			CustomerCreditMemo memo = (CustomerCreditMemo) transaction;
			if (isEqual(memo.getAccount(), wrongAccount)) {
				memo.setAccount(correctAcc);
			}
			break;
		case Transaction.TYPE_CUSTOMER_PRE_PAYMENT:
			CustomerPrePayment payment = (CustomerPrePayment) transaction;
			if (isEqual(payment.getDepositIn(), wrongAccount)) {
				payment.setDepositIn(correctAcc);
			}
			break;
		case Transaction.TYPE_CUSTOMER_REFUNDS:
			CustomerRefund refund = (CustomerRefund) transaction;
			if (isEqual(refund.getPayFrom(), wrongAccount)) {
				refund.setPayFrom(correctAcc);
			}
			break;
		case Transaction.TYPE_ITEM_RECEIPT:
			ItemReceipt receipt = (ItemReceipt) transaction;
			if (isEqual(receipt.getShipTo(), wrongAccount)) {
				receipt.setShipTo(correctAcc);
			}
			break;
		case Transaction.TYPE_JOURNAL_ENTRY:
			JournalEntry entry = (JournalEntry) transaction;
			if (isEqual(entry.getInvolvedAccount(), wrongAccount)) {
				entry.setInvolvedAccount(correctAcc);
			}
			break;
		case Transaction.TYPE_MAKE_DEPOSIT:
			MakeDeposit deposit = (MakeDeposit) transaction;
			if (isEqual(deposit.getDepositTo(), wrongAccount)) {
				deposit.setDepositTo(correctAcc);
			}
			for (TransactionDepositItem item : deposit
					.getTransactionDepositItems()) {
				if (isEqual(item.getAccount(), wrongAccount)) {
					item.setAccount(correctAcc);
				}
			}
			break;
		case Transaction.TYPE_PAY_BILL:
			PayBill pBill = (PayBill) transaction;
			if (isEqual(pBill.getPayFrom(), wrongAccount)) {
				pBill.setPayFrom(correctAcc);
			}
			for (TransactionPayBill tpb : pBill.getTransactionPayBill()) {
				if (isEqual(tpb.getDiscountAccount(), wrongAccount)) {
					tpb.setDiscountAccount(correctAcc);
				}
			}
			break;
		case Transaction.TYPE_PAY_EMPLOYEE:
			PayEmployee pe = (PayEmployee) transaction;
			if (isEqual(pe.getPayAccount(), wrongAccount)) {
				pe.setPayAccount(correctAcc);
			}
			break;
		case Transaction.TYPE_PAY_EXPENSE:
			PayExpense e = (PayExpense) transaction;
			if (isEqual(e.getPaidFrom(), wrongAccount)) {
				e.setPaidFrom(correctAcc);
			}
			break;
		case Transaction.TYPE_PAY_RUN:
			PayRun pr = (PayRun) transaction;
			for (EmployeePaymentDetails detail : pr.getPayEmployee()) {
				for (EmployeePayHeadComponent component : detail
						.getPayHeadComponents()) {
					if (isEqual(component.getPayHead().getAccount(),
							wrongAccount)) {
						component.getPayHead().setAccount(correctAcc);
					}
				}
			}
			break;
		case Transaction.TYPE_PAY_TAX:
			PayTAX pt = (PayTAX) transaction;
			if (isEqual(pt.getPayFrom(), wrongAccount)) {
				pt.setPayFrom(correctAcc);
			}
			break;
		case Transaction.TYPE_PURCHASE_ORDER:
			PurchaseOrder order = (PurchaseOrder) transaction;
			if (isEqual(order.getShipTo(), wrongAccount)) {
				order.setShipTo(correctAcc);
			}
			break;
		case Transaction.TYPE_RECEIVE_PAYMENT:
			ReceivePayment rp = (ReceivePayment) transaction;
			if (isEqual(rp.getDepositIn(), wrongAccount)) {
				rp.setDepositIn(correctAcc);
			}
			for (TransactionReceivePayment trp : rp
					.getTransactionReceivePayment()) {
				if (isEqual(trp.getDiscountAccount(), wrongAccount)) {
					trp.setDiscountAccount(correctAcc);
				}
				if (isEqual(trp.getWriteOffAccount(), wrongAccount)) {
					trp.setWriteOffAccount(correctAcc);
				}
			}
			break;
		case Transaction.TYPE_RECEIVE_TAX:
			ReceiveVAT vat = (ReceiveVAT) transaction;
			if (isEqual(vat.getDepositIn(), wrongAccount)) {
				vat.setDepositIn(correctAcc);
			}
			break;
		case Transaction.TYPE_STOCK_ADJUSTMENT:
			StockAdjustment adj = (StockAdjustment) transaction;
			if (isEqual(adj.getAdjustmentAccount(), wrongAccount)) {
				adj.setAdjustmentAccount(correctAcc);
			}
			break;
		case Transaction.TYPE_ADJUST_VAT_RETURN:
			TAXAdjustment tAdj = (TAXAdjustment) transaction;
			if (isEqual(tAdj.getAdjustmentAccount(), wrongAccount)) {
				tAdj.setAdjustmentAccount(correctAcc);
			}
			break;
		case Transaction.TYPE_TDS_CHALLAN:
			TDSChalanDetail tdsChal = (TDSChalanDetail) transaction;
			if (isEqual(tdsChal.getPayFrom(), wrongAccount)) {
				tdsChal.setPayFrom(correctAcc);
			}
			break;
		case Transaction.TYPE_TRANSFER_FUND:
			TransferFund tf = (TransferFund) transaction;
			if (isEqual(tf.getDepositIn(), wrongAccount)) {
				tf.setDepositIn(correctAcc);
			}
			if (isEqual(tf.getDepositFrom(), wrongAccount)) {
				tf.setDepositFrom(correctAcc);
			}
			break;
		case Transaction.TYPE_VENDOR_PAYMENT:
			VendorPrePayment vpp = (VendorPrePayment) transaction;
			if (isEqual(vpp.getPayFrom(), wrongAccount)) {
				vpp.setPayFrom(correctAcc);
			}
			break;
		case Transaction.TYPE_WRITE_CHECK:
			WriteCheck check = (WriteCheck) transaction;
			if (isEqual(check.getBankAccount(), wrongAccount)) {
				check.setBankAccount(correctAcc);
			}
			break;
		default:
			break;
		}
		for (TransactionItem item : transaction.getTransactionItems()) {
			correctTransactionItems(item, correctAcc, wrongAccount);
		}

		for (TAXRateCalculation trc : transaction
				.getTaxRateCalculationEntriesList()) {
			correctTAXRateCalculation(trc, correctAcc, wrongAccount);
		}

		TransactionMakeDepositEntries tmde = transaction
				.getTransactionMakeDepositEntries();
		if (tmde != null && isEqual(tmde.getAccount(), wrongAccount)) {
			tmde.setAccount(correctAcc);
		}

		migrate(transaction);

	}

	private void correctTAXRateCalculation(TAXRateCalculation trc,
			Account correctAcc, Account wrongAccount) {
		if (isEqual(trc.getPurchaseLiabilityAccount(), wrongAccount)) {
			trc.setPurchaseLiabilityAccount(correctAcc);
			trc.getTaxAgency().setPurchaseLiabilityAccount(correctAcc);
		}
		if (isEqual(trc.getSalesLiabilityAccount(), wrongAccount)) {
			trc.setSalesLiabilityAccount(correctAcc);
			trc.getTaxAgency().setSalesLiabilityAccount(correctAcc);
		}
	}

	private boolean isEqual(Account payFrom, Account wrongAccount) {
		if (payFrom == null) {
			return true;
		}
		return payFrom.getID() == wrongAccount.getID();
	}

	private void correctTransactionItems(TransactionItem tItem,
			Account correctAcc, Account wrongAccount) {
		switch (tItem.getType()) {
		case TransactionItem.TYPE_ACCOUNT:
			if (tItem.getAccount().getID() == wrongAccount.getID()) {
				tItem.setAccount(correctAcc);
			}
			break;
		case TransactionItem.TYPE_ITEM:
			correctItem(tItem.getItem(), correctAcc, wrongAccount);
		}
	}

	private void correctItem(Item item, Account correctAcc, Account wrongAccount) {
		if (item.getIncomeAccount() != null) {
			if (item.getIncomeAccount().getID() == wrongAccount.getID()) {
				item.setIncomeAccount(correctAcc);
			}
		} else if (item.getExpenseAccount() != null) {
			if (item.getExpenseAccount().getID() == wrongAccount.getID()) {
				item.setExpenseAccount(correctAcc);
			}
		} else if (item.getAssestsAccount() != null) {
			if (item.getAssestsAccount().getID() == wrongAccount.getID()) {
				item.setAssestsAccount(correctAcc);
			}
		}
	}

	private Account createAccount(Company company, int type, String name,
			int cashFlowCategory) {
		Account account = new Account(type, getNextAccountNumber(
				company.getId(), type), name, cashFlowCategory);
		account.setCompany(company);
		getSession().saveOrUpdate(account);
		return account;
	}

	@Override
	public int getVersion() {
		return 17;
	}

}
