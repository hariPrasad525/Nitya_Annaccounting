package com.vimukti.accounter.encryption;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.h2.tools.Server;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import com.vimukti.accounter.core.*;
import com.vimukti.accounter.mail.UsersMailSendar;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.utils.SecureUtils;

public class Encrypter extends Thread {
	private Logger log = Logger.getLogger(Encrypter.class);

	private long companyId;
	private byte[] csk;
	private byte[] d2;
	private byte[] s2;
	private byte[] companySecret;
	private String sessionId;
	private String emailId;
	private User user;

	private boolean isEncrypt;
	private static List<Class<?>> classes;

	public static void init() {
		classes = new ArrayList<Class<?>>();
		Logger logger = Logger.getLogger(Encrypter.class);
		logger.info("Initializing Encrypter...");
		// File core = getDirectoryFile();
		// File[] listFiles = core.listFiles();
		Class<?>[] allClasses = getClasses();
		logger.info("Total classes: " + allClasses.length);
		try {
			Class<?> create = CreatableObject.class;

			for (Class<?> cls : allClasses) {
				try {
					if (create.isAssignableFrom(cls)) {
						classes.add(cls);
					} else {
						// System.out.println(string);
					}
				} catch (Exception e) {
					logger.error(e.getMessage());
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	private static Class<?>[] getClasses() {
		Class<?>[] classes = new Class<?>[] { Account.class,
				AccountComparator.class, AccounterClass.class,
				AccounterServerConstants.class, AccounterThreadLocal.class,
				AccountTransaction.class, AccountTransactionByAccount.class,
				AccountTransactionByTaxCode.class,
				AccountTransactionItem.class, Activation.class, Activity.class,
				ActivityType.class, Address.class, AdjustmentReason.class,
				Advertisement.class, Attachment.class, AuditWriter.class,
				AuditWriterImpl.class, Bank.class, BankAccount.class,
				BankRule.class, Box.class, BrandingTheme.class, Budget.class,
				BudgetItem.class, BudgetReportTemplate.class,
				BuildAssembly.class, CashPurchase.class,
				CashSalePdfGeneration.class, CashSales.class,
				ChequeLayout.class, ChequePdfGenerator.class, Client.class,
				ClientConvertUtil.class, ClientPaypalDetails.class,
				ClientSubscription.class, CloneUtil.class, CloneUtil2.class,
				CommodityCode.class, Company.class, CompanyPreferences.class,
				Contact.class, CreatableObject.class, CreditCardCharge.class,
				CreditNotePdfGeneration.class, CreditNotePDFTemplete.class,
				CreditNoteTemplete.class, CreditRating.class,
				CreditsAndPayments.class, CSVReportTemplate.class,
				Currency.class, CurrencyRate.class, Customer.class,
				CustomerCreditMemo.class, CustomerGroup.class,
				CustomerPrePayment.class, CustomerRefund.class,
				CustomerStatementTemplate.class, CustomField.class,
				CustomFieldValue.class, DeleteReason.class, Depreciation.class,
				Developer.class, EmailAccount.class, Employee.class,
				EmployeeGroup.class, EmployeePayHeadComponent.class,
				EnterBill.class, Estimate.class, ETDSAnnexuresGenerator.class,
				EU.class, Exempted.class, Expense.class, Fax.class,
				FinanceDate.class, FinanceLogger.class, FiscalYear.class,
				FixedAsset.class, FixedAssetHistory.class,
				Form16ApdfTemplate.class, Form26QAnnexureGenerator.class,
				Form27EQAnnexureGenerator.class,
				Form27QAnnexureGenerator.class, IAccounterServerCore.class,
				IMActivation.class, IMUser.class, INamedObject.class,
				InventoryAssembly.class, InventoryAssemblyItem.class,
				InventoryPurchase.class, Invoice.class,
				InvoiceExcelTemplete.class, InvoicePdfGeneration.class,
				InvoicePDFTemplete.class, IRASCompanyInfo.class,
				IRASGeneralLedgerLineInfo.class, IRASInformation.class,
				IRASPurchaseLineInfo.class, IRASSupplyLineInfo.class,
				Item.class, ItemGroup.class, ITemplate.class,
				ItemReceipt.class, Job.class, JournalEntry.class,
				Location.class, LongUseType.class, MaintananceInfoUser.class,
				MakeDeposit.class, Measurement.class, MessageOrTask.class,
				Misc1099PDFTemplate.class, Misc1099SamplePDFTemplate.class,
				MISCInformationTemplate.class, MobileCookie.class, News.class,
				NominalCodeRange.class, NumberUtils.class,
				ObjectConvertUtil.class, PayBill.class,
				EmployeePaymentDetails.class, PayExpense.class, PayHead.class,
				PayHeadField.class, PaymentTerms.class, PayrollUnit.class,
				PayRun.class, PayStructure.class, PayStructureItem.class,
				PayTAX.class, PayTAXEntries.class, PayTDS.class, Phone.class,
				PortletConfiguration.class, PortletPageConfiguration.class,
				PriceLevel.class, PrintTemplete.class, PurchaseOrder.class,
				PurchaseOrderPdfGeneration.class, Quantity.class,
				QuotePdfGeneration.class, QuotePdfTemplate.class,
				ReceivePayment.class, ReceivePaymentPdfGeneration.class,
				ReceiveVAT.class, ReceiveVATEntries.class,
				Reconciliation.class, ReconciliationItem.class,
				RecurringTransaction.class, ReffereredObject.class,
				RememberMeKey.class, Reminder.class, ReportsGenerator.class,
				ReportTemplate.class, ResetPasswordToken.class,
				SalesOrder.class, SalesOrderPdfGeneration.class,
				SalesPerson.class, Server.class, ServerConvertUtil.class,
				ServerMaintanance.class, ShippingMethod.class,
				ShippingTerms.class, Statement.class, StatementRecord.class,
				StockAdjustment.class, StockTransfer.class,
				StockTransferItem.class, Subscription.class,
				SupportedUser.class, TAXAdjustment.class, TAXAgency.class,
				TAXCode.class, TAXGroup.class, TAXItem.class,
				TAXRateCalculation.class, TaxRates.class, TAXReturn.class,
				TAXReturnEntry.class, TDSChalanDetail.class,
				TDSCoveringLetterTemplate.class, TDSDeductorMasters.class,
				TDSInfo.class, TDSResponsiblePerson.class,
				TDSTransactionItem.class, TemplateBuilder.class,
				TransactionCreditsAndPayments.class,
				TransactionDepositItem.class, TransactionExpense.class,
				TransactionItem.class, TransactionLog.class,
				TransactionMakeDepositEntries.class, TransactionPayBill.class,
				TransactionPayExpense.class, TransactionPayTAX.class,
				TransactionReceivePayment.class, TransactionReceiveVAT.class,
				TransferFund.class, Unit.class, UnitOfMeasure.class,
				User.class, UserPermissions.class, UserPreferences.class,
				UserUtils.class, VATReturnBox.class, Vendor.class,
				VendorCreditMemo.class, VendorGroup.class,
				VendorPrePayment.class, Warehouse.class,
				WareHouseAllocation.class, WriteCheck.class, Employee.class,
				EmployeeGroup.class, PayHead.class, AttendancePayHead.class,
				ComputionPayHead.class, FlatRatePayHead.class,
				UserDefinedPayHead.class, PayrollUnit.class,
				AttendanceOrProductionType.class, ComputationSlab.class,
				ComputaionFormulaFunction.class, PayRun.class,
				EmployeePaymentDetails.class, AttendanceManagementItem.class,
				EmployeePayHeadComponent.class,
				AttendanceOrProductionItem.class, PayStructure.class,
				PayStructureItem.class };
		return classes;
	}

	public Encrypter(long companyId, String password, byte[] d2,
			String emailId, String sessionId) throws Exception {
		this.companyId = companyId;
		csk = EU.generatePBS(password);
		this.d2 = d2;
		this.emailId = emailId;
		this.sessionId = sessionId;
		s2 = EU.getKey(sessionId);
		user = getUser();
		this.isEncrypt = true;
	}

	public Encrypter(long companyId, byte[] companySecret, String password,
			long userId) {
		this.companyId = companyId;
		this.companySecret = companySecret;
		csk = EU.generatePBS(password);
		user = (User) HibernateUtil.getCurrentSession().get(User.class, userId);
	}

	@Override
	public void run() {
		try {
			List<List> lists = loadCoreObjects();
			updateAll(lists);
			log.info((isEncrypt ? "Encryption" : "Decryption")
					+ " is completed for companyID..:" + companyId
					+ ",and emailId:" + emailId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void updateAll(List<List> lists) {
		Session session = HibernateUtil.openSession();
		Transaction beginTransaction = session.beginTransaction();
		try {
			OnUpdateThreadLocal.set(true);
			for (List l : lists) {
				for (Object o : l) {
					CreatableObject co = (CreatableObject) o;
					co.setLastModifier(user);
					co.setLastModifiedDate(AccounterThreadLocal.currentTime());
					session.evict(o);
					session.update(o);
				}
			}
			user = (User) session.get(User.class, user.getID());
			AccounterThreadLocal.set(user);

			initChipher();

			beginTransaction.commit();
		} catch (Exception e) {
			beginTransaction.rollback();
			e.printStackTrace();
		} finally {
			if (session.isOpen()) {
				session.close();
			}
			session = HibernateUtil.openSession();
			try {
				Transaction transaction = session.beginTransaction();
				session.getNamedQuery("unlock.company")
						.setParameter("companyId", companyId).executeUpdate();
				transaction.commit();
			} catch (Exception e) {
			} finally {
				OnUpdateThreadLocal.set(false);
				session.close();
			}
		}
	}

	private void initChipher() throws Exception {
		Session session = HibernateUtil.getCurrentSession();
		Company company = getCompany();
		if (isEncrypt) {
			byte[] s3 = EU.generateSymetric();
			byte[] companySecret = EU.encrypt(s3, csk);

			String string = SecureUtils.createID(16);
			byte[] prk = EU.generatePBS(string);

			byte[] encryptedPass = EU.encrypt(csk, prk);
			company.setEncryptedPassword(encryptedPass);
			company.setSecretKey(companySecret);
			session.saveOrUpdate(company);

			byte[] userSecret = EU.encrypt(s3, EU.decrypt(d2, s2));
			EU.storeKey(s2, sessionId);
			user.setSecretKey(userSecret);
			session.saveOrUpdate(user);

			EU.createCipher(userSecret, d2, sessionId);

			log.info("Company password recovery key---" + string);
			sendCompanyPasswordRecoveryKey(emailId, string);
		} else {
			company.setEncryptedPassword(null);
			company.setSecretKey(null);
			Set<User> users = company.getUsers();
			for (User u : users) {
				u.setSecretKey(null);
				session.saveOrUpdate(u);
			}
			session.saveOrUpdate(company);
			EU.removeCipher();
		}

	}

	private User getUser() {
		Session session = HibernateUtil.getCurrentSession();
		Query query = session.getNamedQuery("getUser.by.mailId.and.companyId")
				.setParameter("emailId", emailId)
				.setParameter("companyId", companyId);
		return (User) query.uniqueResult();
	}

	private Company getCompany() {
		Session session = HibernateUtil.getCurrentSession();
		Object object = session.get(Company.class, companyId);
		return (Company) object;
	}

	private List<List> loadCoreObjects() throws Exception {

		Session session = HibernateUtil.openSession();
		try {
			if (isEncrypt) {
				EU.removeCipher();
			} else {
				EU.createCipher(companySecret, csk);
			}
			Company company = getCompany();
			log.info((isEncrypt ? "Encryption" : "Decryption")
					+ " is started (" + company.getTradingName() + ")");
			List<List> lists = new ArrayList<List>();
			// classes.clear();
			// classes.add("FixedAsset");
			for (Class<?> cls : classes) {
				Criteria criteria = session.createCriteria(cls);
				criteria.add(Restrictions.eq("company", company));
				try {
					List list = criteria.list();
					List list2 = new ArrayList();
					for (Object o : list) {
						o = HibernateUtil.initializeAndUnproxy(o);
						if (o instanceof Payee) {
							Payee p = (Payee) o;
							p.setContacts(new HashSet<Contact>(p.getContacts()));
							p.setAddress(new HashSet<Address>(p.getAddress()));
						} else if (o instanceof InventoryAssembly) {
							InventoryAssembly i = (InventoryAssembly) o;
							i.setComponents(new HashSet<InventoryAssemblyItem>(
									i.getComponents()));
						} else if (o instanceof Expense) {
							Expense e = (Expense) o;
							e.setTransactionExpenses(new ArrayList<TransactionExpense>(
									e.getTransactionExpenses()));
						} else if (o instanceof Invoice) {
							Invoice i = (Invoice) o;
							i.setTransactionReceivePayments(new HashSet<TransactionReceivePayment>(
									i.getTransactionReceivePayments()));
						} else if (o instanceof ReceivePayment) {
							ReceivePayment r = (ReceivePayment) o;
							r.setTransactionReceivePayment(new ArrayList<TransactionReceivePayment>(
									r.getTransactionReceivePayment()));
						} else if (o instanceof TransactionPayBill) {
							TransactionPayBill t = (TransactionPayBill) o;
							t.setTransactionCreditsAndPayments(new ArrayList<TransactionCreditsAndPayments>(
									t.getTransactionCreditsAndPayments()));
						} else if (o instanceof StockTransfer) {
							StockTransfer s = (StockTransfer) o;
							s.setStockTransferItems(new HashSet<StockTransferItem>(
									s.getStockTransferItems()));
						}

						if (o instanceof com.vimukti.accounter.core.Transaction) {
							com.vimukti.accounter.core.Transaction t = (com.vimukti.accounter.core.Transaction) o;
							t.setTransactionItems(new ArrayList<TransactionItem>(
									t.getTransactionItems()));
						}
						list2.add(o);
					}
					lists.add(list2);
				} catch (Exception e) {
					System.out.println(cls);
				}
			}
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return null;
	}

	public static void sendCompanyPasswordRecoveryKey(String emailId, String key) {
		try {
			UsersMailSendar.sendMailToUserWithPasswordRecoverKey(emailId, key);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
