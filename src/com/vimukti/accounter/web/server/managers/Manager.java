package com.vimukti.accounter.web.server.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.dialect.EncryptedStringType;

import com.vimukti.accounter.core.AccounterServerConstants;
import com.vimukti.accounter.core.AttendancePayHead;
import com.vimukti.accounter.core.BankAccount;
import com.vimukti.accounter.core.Box;
import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.core.ClientConvertUtil;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.ComputionPayHead;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.FlatRatePayHead;
import com.vimukti.accounter.core.IAccounterServerCore;
import com.vimukti.accounter.core.InventoryAssembly;
import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.core.PayHead;
import com.vimukti.accounter.core.ServerConvertUtil;
import com.vimukti.accounter.core.TAXAdjustment;
import com.vimukti.accounter.core.TAXAgency;
import com.vimukti.accounter.core.TAXItem;
import com.vimukti.accounter.core.TAXReturn;
import com.vimukti.accounter.core.TAXReturnEntry;
import com.vimukti.accounter.core.Transaction;
import com.vimukti.accounter.core.User;
import com.vimukti.accounter.core.UserDefinedPayHead;
import com.vimukti.accounter.core.Util;
import com.vimukti.accounter.core.VATReturnBox;
import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAttendancePayHead;
import com.vimukti.accounter.web.client.core.ClientBankAccount;
import com.vimukti.accounter.web.client.core.ClientBox;
import com.vimukti.accounter.web.client.core.ClientComputionPayHead;
import com.vimukti.accounter.web.client.core.ClientEmployeePayHeadComponent;
import com.vimukti.accounter.web.client.core.ClientEmployeePaymentDetails;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientFlatRatePayHead;
import com.vimukti.accounter.web.client.core.ClientInventoryAssembly;
import com.vimukti.accounter.web.client.core.ClientPayHead;
import com.vimukti.accounter.web.client.core.ClientPayRun;
import com.vimukti.accounter.web.client.core.ClientPayStructure;
import com.vimukti.accounter.web.client.core.ClientPayStructureItem;
import com.vimukti.accounter.web.client.core.ClientTAXReturnEntry;
import com.vimukti.accounter.web.client.core.ClientTaxComputationPayHead;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientUser;
import com.vimukti.accounter.web.client.core.ClientUserDefinedPayHead;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Lists.OpenAndClosedOrders;
import com.vimukti.accounter.web.client.core.reports.SalesByCustomerDetail;
import com.vimukti.accounter.web.client.core.reports.TransactionHistory;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.core.Calendar;
import com.vimukti.accounter.web.server.FinanceTool;

public class Manager {
	protected Logger log = Logger.getLogger(FinanceTool.class);

	/**
	 * Returns the Current Company
	 * 
	 * @return
	 */
	public Company getCompany(long companyId) {
		Session session = HibernateUtil.getCurrentSession();
		return (Company) session.get(Company.class, companyId);
	}

	public boolean canEdit(IAccounterServerCore clonedObject,
			IAccounterCore clientObject) throws AccounterException {

		IAccounterServerCore serverObject = new ServerConvertUtil()
				.toServerObject(null, clientObject,
						HibernateUtil.getCurrentSession());
		if (!(clonedObject instanceof Transaction)
				|| !(((Transaction) clonedObject).getSaveStatus() == Transaction.STATUS_DRAFT)) {
			boolean isCreate = serverObject.getID() == 0;
			if (isCreate) {
				clonedObject.selfValidate();
			} else {
				serverObject.selfValidate();
			}
		}
		return serverObject.canEdit(clonedObject, false);

	}

	public <T extends IAccounterCore> T getServerObjectByName(
			AccounterCoreType type, String name, Company company)
			throws DAOException {

		Session session = HibernateUtil.getCurrentSession();

		// Class<?> serverClass = getClientEquivalentServerClass(type);

		if (type != null) {

			Query hibernateQuery = session
					.getNamedQuery(
							"unique.name." + type.getServerClassSimpleName())
					.setString(0, name).setEntity("company", company);

			List objects = hibernateQuery.list();

			if (objects != null && objects.get(0) != null) {

				return (T) objects.get(0);

			}

		}

		return null;

	}

	protected List<String> getSalesByCustomerRecordParents(
			SalesByCustomerDetail salesByCustomerDetail, long CompanyId) {
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		List<String> list = new ArrayList<String>();
		list.add(salesByCustomerDetail.getItemName());
		map.put(salesByCustomerDetail.getItemName(),
				salesByCustomerDetail.getDepth());
		if (salesByCustomerDetail.getParentItemId() != 0) {
			long parentId = salesByCustomerDetail.getParentItemId();
			while (parentId > 0) {
				Item parentItem = null;
				for (Item item : getCompany(CompanyId).getItems()) {
					if (item.getID() == parentId) {
						parentItem = item;
						list.add(item.getName());
						map.put(item.getName(), item.getDepth());
					}
				}
				if (parentItem != null && parentItem.getParentItem() != null) {
					parentId = parentItem.getParentItem().getID();
				} else {
					parentId = 0l;
				}
			}
		}
		salesByCustomerDetail.setItemsDepthMap(map);
		ArrayList<String> list2 = new ArrayList<String>();
		for (int ii = (list.size() - 1); ii >= 0; ii--) {
			list2.add(list.get(ii));
		}
		return list2;
	}

	public ArrayList<OpenAndClosedOrders> prepareQueryResult(List l) {
		Object[] object = null;
		Iterator iterator = l.iterator();
		List<OpenAndClosedOrders> queryResult = new ArrayList<OpenAndClosedOrders>();
		while ((iterator).hasNext()) {
			OpenAndClosedOrders openAndClosedOrder = new OpenAndClosedOrders();
			object = (Object[]) iterator.next();
			openAndClosedOrder.setTransactionID(((Long) object[0]).longValue());
			openAndClosedOrder.setTransactionType((Integer) object[1]);
			openAndClosedOrder.setNumber((String) object[2]);
			openAndClosedOrder.setAmount(object[3] != null ? (Double) object[3]
					: 0.0);
			openAndClosedOrder.setTransactionDate(new ClientFinanceDate(
					(Long) object[4]));
			openAndClosedOrder.setVendorOrCustomerName((String) object[5]);
			openAndClosedOrder.setStatus((Integer) object[6]);
			// openAndClosedOrder
			// .setQuantity(object[4] != null ? ((BigDecimal) object[4])
			// .intValue() : 0);

			queryResult.add(openAndClosedOrder);
		}
		return new ArrayList<OpenAndClosedOrders>(queryResult);
	}

	public ClientFinanceDate[] getMinimumAndMaximumTransactionDate(
			long companyId) throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		Query query = session.getNamedQuery(
				"getMinimumAndMaximumTransactionDate").setParameter(
				"companyId", companyId);
		List list = query.list();
		Object[] object = null;
		Iterator iterator = list.iterator();
		// List<Vendor> queryResult = new ArrayList<Vendor>();
		ClientFinanceDate startDate = new ClientFinanceDate();
		ClientFinanceDate endDate = new ClientFinanceDate();
		if ((iterator).hasNext()) {

			object = (Object[]) iterator.next();
			startDate = (object[0] == null ? new ClientFinanceDate()
					: new ClientFinanceDate((Long) object[0]));
			endDate = (object[1] == null ? new ClientFinanceDate()
					: new ClientFinanceDate((Long) object[1]));
		}
		return new ClientFinanceDate[] { startDate, endDate };
	}

	public Object getServerObjectForid(AccounterCoreType t, long id) {

		Session session = HibernateUtil.getCurrentSession();

		Class<?> serverClass = getClientEquivalentServerClass(t);

		if (serverClass != null) {

			return session.get(serverClass, id);

		}

		return null;
	}

	private Class<?> getClientEquivalentServerClass(AccounterCoreType type) {

		String clientClassName = type.getClientClassSimpleName();

		clientClassName = clientClassName.replaceAll("Client", "");

		Class<?> clazz = null;

		// FIXME if Class class1 if of another package other than,
		// com.vimukti.accounter.core

		try {
			String qualifiedName = "com.vimukti.accounter.core."
					+ clientClassName;
			clazz = Class.forName(qualifiedName);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return clazz;
	}

	@SuppressWarnings("unchecked")
	public <T extends IAccounterCore> T getObjectById(AccounterCoreType type,
			long id, long companyId) throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();

		Object serverObject = getServerObjectForid(type, id);

		Class<?> serverClass = getClientEquivalentServerClass(type);

		if (serverObject != null) {
			if (serverObject instanceof User) {
				return (T) ((User) serverObject).getClientUser().toUserInfo();
			}
			T t = null;
			if (serverObject instanceof InventoryAssembly) {
				t = (T) new ClientConvertUtil().toClientObject(serverObject,
						ClientInventoryAssembly.class);
			} else if (serverObject instanceof ComputionPayHead) {
				t = (T) new ClientConvertUtil().toClientObject(serverObject,
						ClientComputionPayHead.class);
			} else if (serverObject instanceof FlatRatePayHead) {
				t = (T) new ClientConvertUtil().toClientObject(serverObject,
						ClientFlatRatePayHead.class);
			} else if (serverObject instanceof AttendancePayHead) {
				t = (T) new ClientConvertUtil().toClientObject(serverObject,
						ClientAttendancePayHead.class);
			} else if (serverObject instanceof UserDefinedPayHead) {
				t = (T) new ClientConvertUtil().toClientObject(serverObject,
						ClientUserDefinedPayHead.class);
			} else if (serverObject instanceof BankAccount) {
				t = (T) new ClientConvertUtil().toClientObject(serverObject,
						ClientBankAccount.class);
			} else {

				t = (T) new ClientConvertUtil().toClientObject(serverObject,
						Util.getClientEqualentClass(serverClass));
			}

			if (t instanceof ClientPayStructure) {
				ClientPayStructure clientPayStructure = (ClientPayStructure) t;
				List<ClientPayStructureItem> items = clientPayStructure
						.getItems();
				for (ClientPayStructureItem item : items) {
					Object object = session.get(PayHead.class,
							item.getPayHead());
					ClientPayHead payHead = new ClientConvertUtil()
							.toClientObject(object, ClientPayHead.class);
					ClientPayHead clientPayHead = payHead;
					if (payHead.getCalculationType() == ClientPayHead.CALCULATION_TYPE_ON_ATTENDANCE
							|| payHead.getCalculationType() == ClientPayHead.CALCULATION_TYPE_ON_PRODUCTION) {
						clientPayHead = new ClientConvertUtil().toClientObject(
								payHead, ClientAttendancePayHead.class);
					} else if (payHead.getCalculationType() == ClientPayHead.CALCULATION_TYPE_AS_COMPUTED_VALUE) {
						clientPayHead = new ClientConvertUtil().toClientObject(
								payHead, ClientComputionPayHead.class);
					} else if (payHead.getCalculationType() == ClientPayHead.CALCULATION_TYPE_AS_TAX) {
						clientPayHead = new ClientConvertUtil().toClientObject(
								payHead, ClientTaxComputationPayHead.class);
					} else if (payHead.getCalculationType() == ClientPayHead.CALCULATION_TYPE_FLAT_RATE) {
						clientPayHead = new ClientConvertUtil().toClientObject(
								payHead, ClientFlatRatePayHead.class);
					} else if (payHead.getCalculationType() == ClientPayHead.CALCULATION_TYPE_AS_USER_DEFINED) {
						clientPayHead = new ClientConvertUtil().toClientObject(
								payHead, ClientUserDefinedPayHead.class);
					}
					item.setPayHead(clientPayHead.getID());
				}
				clientPayStructure.setItems(items);
				t = (T) clientPayStructure;
			}

			if (t instanceof ClientPayRun) {
				ClientPayRun payrun = (ClientPayRun) t;
				HashSet<ClientEmployeePaymentDetails> details = new HashSet<ClientEmployeePaymentDetails>();
				for (ClientEmployeePaymentDetails item : payrun
						.getPayEmployee()) {
					HashSet<ClientEmployeePayHeadComponent> headComponents = new HashSet<ClientEmployeePayHeadComponent>();
					for (ClientEmployeePayHeadComponent ephc : item
							.getPayHeadComponents()) {
						PayHead object = (PayHead) session.get(PayHead.class,
								ephc.getPayHead());
						ClientPayHead payHead = new ClientConvertUtil()
								.toClientObject(object, ClientPayHead.class);
						ClientPayHead clientPayHead = payHead;
						if (payHead.getCalculationType() == ClientPayHead.CALCULATION_TYPE_ON_ATTENDANCE) {
							clientPayHead = new ClientConvertUtil()
									.toClientObject(payHead,
											ClientAttendancePayHead.class);
						} else if (payHead.getCalculationType() == ClientPayHead.CALCULATION_TYPE_AS_COMPUTED_VALUE) {
							clientPayHead = new ClientConvertUtil()
									.toClientObject(payHead,
											ClientComputionPayHead.class);
						}  else if (payHead.getCalculationType() == ClientPayHead.CALCULATION_TYPE_AS_TAX) {
							clientPayHead = new ClientConvertUtil().toClientObject(
									payHead, ClientTaxComputationPayHead.class);
						} else if (payHead.getCalculationType() == ClientPayHead.CALCULATION_TYPE_FLAT_RATE) {
							clientPayHead = new ClientConvertUtil()
									.toClientObject(payHead,
											ClientFlatRatePayHead.class);
						} else if (payHead.getCalculationType() == ClientPayHead.CALCULATION_TYPE_AS_USER_DEFINED) {
							clientPayHead = new ClientConvertUtil()
									.toClientObject(payHead,
											ClientUserDefinedPayHead.class);
						}
						ephc.setPayHead(clientPayHead.getID());
						headComponents.add(ephc);
					}
					item.setPayHeadComponents(headComponents);
					details.add(item);
				}
				payrun.setPayEmployee(details);
				t = (T) payrun;
			}

			Company company = getCompany(companyId);
			Query query2 = session
					.getNamedQuery("getTAXRateCalculation.by.check.idandvatReturn");
			query2.setParameter("id", t.getID()).setEntity("company", company);
			List<?> list = query2.list();
			if (list != null && !list.isEmpty()
					&& t instanceof ClientTransaction) {
				((ClientTransaction) t).setCanEdit(false);
			}

			return t;

		}

		return null;
	}

	public <T extends IAccounterCore> ArrayList<T> getObjects(
			AccounterCoreType type, long companyId) throws DAOException,
			AccounterException {

		Session session = HibernateUtil.getCurrentSession();

		List<T> clientObjects = new ArrayList<T>();

		Class<?> serverClass = getClientEquivalentServerClass(type);

		if (serverClass != null) {

			Query hibernateQuery = session.getNamedQuery(
					"list." + type.getServerClassSimpleName()).setEntity(
					"company", getCompany(companyId));

			List objects = hibernateQuery.list();

			if (objects != null) {

				for (Object o : objects) {

					clientObjects.add((T) new ClientConvertUtil()
							.toClientObject(o,
									Util.getClientEqualentClass(serverClass)));
				}

			}

		}

		ArrayList<T> arrayList = new ArrayList<T>(clientObjects);
		return arrayList;

	}

	public <T extends IAccounterCore> T getObjectByName(AccounterCoreType type,
			String name, long companyId) throws DAOException,
			AccounterException {

		Session session = HibernateUtil.getCurrentSession();

		Class<?> serverClass = getClientEquivalentServerClass(type);

		if (serverClass != null) {

			Query hibernateQuery = session
					.getNamedQuery(
							"unique.name." + type.getServerClassSimpleName())
					.setParameter("name", name, EncryptedStringType.INSTANCE)
					.setEntity("company", getCompany(companyId));

			List objects = hibernateQuery.list();

			if (objects != null && objects.size() > 0 && objects.get(0) != null) {

				return (T) new ClientConvertUtil().toClientObject(
						objects.get(0),
						Util.getClientEqualentClass(serverClass));

			}

		}

		return null;

	}

	public void mergeOpeningBalanceEntries(
			List<TransactionHistory> queryResult, Set<String> payee,
			Map<String, TransactionHistory> openingBalnaceEntries) {

		for (String payeeName : payee) {
			int index = -1;
			for (int i = 0; i < queryResult.size(); i++) {
				TransactionHistory entry = queryResult.get(i);
				if (entry.getName().equals(payeeName)) {
					index = i;
					break;
				}
			}

			if (index != -1 && openingBalnaceEntries.containsKey(payeeName)) {
				queryResult.add(index, openingBalnaceEntries.get(payeeName));
			}
		}

	}

	public void updateClientUser(ClientUser clientUser, Client client) {
		clientUser.setFirstName(client.getFirstName());
		clientUser.setLastName(client.getLastName());
		clientUser.setFullName(client.getFullName());
		clientUser.setEmail(client.getEmailId());
	}

	protected FinanceDate getCurrentFiscalYearStartDate(Company company) {
		FinanceDate startDate = new FinanceDate();
		int firstMonth = company.getPreferences().getFiscalYearFirstMonth();
		if (firstMonth > startDate.getMonth()) {
			startDate.setYear(startDate.getYear() - 1);
		}
		startDate.setMonth(firstMonth);
		startDate.setDate(1);
		return startDate;
	}

	protected FinanceDate getCurrentFiscalYearEndDate(Company company) {
		FinanceDate startDate = new FinanceDate();
		startDate.setMonth(company.getPreferences().getFiscalYearFirstMonth());
		startDate.setDate(1);

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startDate.getAsDateObject());
		calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 1);
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
		calendar.set(Calendar.DATE,
				calendar.getActualMaximum(Calendar.DAY_OF_MONTH));

		FinanceDate endDate = new FinanceDate(calendar.getTime());
		return endDate;
	}

	protected List<Box> toServerBoxes(List<TAXReturnEntry> taxReturnEntries,
			TAXAgency taxAgency) {

		List<Box> boxes = createBoxes(taxAgency);
		for (TAXReturnEntry entry : taxReturnEntries) {
			if (entry.getTransaction().isTAXAdjustment()) {
				TAXAdjustment taxAdjustment = (TAXAdjustment) entry
						.getTransaction();
				VATReturnBox vb = taxAdjustment.getTaxItem().getVatReturnBox();
				for (Box b : boxes) {
					if (vb.getVatBox().equals(b.getName())
							|| (b.getName()
									.equals(AccounterServerConstants.UK_BOX10_UNCATEGORISED) && vb
									.getVatBox().equals("NONE"))) {

						if (taxAdjustment.isSales()) {
							if ((!taxAdjustment
									.getTaxItem()
									.getName()
									.equals(AccounterServerConstants.VAT_ITEM_EC_SALES_GOODS_ZERO_RATED))
									&& (!taxAdjustment
											.getTaxItem()
											.getName()
											.equals(AccounterServerConstants.VAT_ITEM_EC_SALES_SERVICES_ZERO_RATED))) {
								if (taxAdjustment.getIncreaseVATLine())
									b.setAmount(b.getAmount()
											+ taxAdjustment.getTotal());
								else
									b.setAmount(b.getAmount()
											- taxAdjustment.getTotal());
							} else {

								if (taxAdjustment.getIncreaseVATLine())
									b.setAmount(b.getAmount()
											+ taxAdjustment.getTotal());
								else
									b.setAmount(b.getAmount()
											- taxAdjustment.getTotal());
							}
						} else {
							double amount = 0;
							if (taxAdjustment.getIncreaseVATLine())
								amount = taxAdjustment.getTotal();
							else
								amount = -1 * taxAdjustment.getTotal();

							if (vb.getVatBox()
									.equals(AccounterServerConstants.UK_BOX2_VAT_DUE_ON_ACQUISITIONS)
									|| vb.getVatBox()
											.equals(AccounterServerConstants.UK_BOX1_VAT_DUE_ON_SALES))
								amount = -1 * amount;

							b.setAmount(b.getAmount() + amount);
						}
					}
				}

			} else if (entry.getTaxItem().getTaxAgency().getName()
					.equals(taxAgency.getName())) {

				VATReturnBox vb = entry.getTaxItem().getVatReturnBox();

				for (int j = 0; j < boxes.size(); j++) {

					Box box = boxes.get(j);

					if ((box).getName().equals(vb.getTotalBox())) {
						box.setAmount(box.getAmount() + entry.getNetAmount());

					} else if ((box).getName().equals(vb.getVatBox())) {
						if (vb.getVatBox()
								.equals(AccounterServerConstants.UK_BOX2_VAT_DUE_ON_ACQUISITIONS)
								|| (vb.getVatBox()
										.equals(AccounterServerConstants.UK_BOX4_VAT_RECLAMED_ON_PURCHASES) && !entry
										.isTAXGroupEntry())
								|| (vb.getVatBox()
										.equals(AccounterServerConstants.UK_BOX1_VAT_DUE_ON_SALES) && vb
										.getTotalBox()
										.equals(AccounterServerConstants.BOX_NONE)))
							box.setAmount(box.getAmount()
									+ (-1 * (entry.getTaxAmount())));

						else
							box.setAmount(box.getAmount()
									+ (entry.getTaxAmount()));

					}
					// if (box.getBoxNumber() == 4) {
					// box.setAmount(box.getAmount() + rcAmount);
					// rcAmount = 0;
					// }
					box.getTaxRateCalculations().add(null);
				}
			}
		}
		return boxes;
	}

	protected List<ClientBox> toBoxes(
			List<ClientTAXReturnEntry> taxReturnEntries, TAXAgency taxAgency) {

		List<ClientBox> boxes = createClientBoxes(taxAgency);
		Session session = HibernateUtil.getCurrentSession();
		for (ClientTAXReturnEntry entry : taxReturnEntries) {
			TAXItem taxItem = (TAXItem) session.get(TAXItem.class,
					entry.getTaxItem());
			Transaction transaction = (Transaction) session.get(
					Transaction.class, entry.getTransaction());
			if (transaction.isTAXAdjustment()) {
				TAXAdjustment taxAdjustment = (TAXAdjustment) transaction;
				VATReturnBox vb = taxAdjustment.getTaxItem().getVatReturnBox();
				for (ClientBox b : boxes) {
					if (vb.getVatBox().equals(b.getName())
							|| (b.getName()
									.equals(AccounterServerConstants.UK_BOX10_UNCATEGORISED) && vb
									.getVatBox().equals("NONE"))) {

						if (taxAdjustment.isSales()) {
							if ((!taxAdjustment
									.getTaxItem()
									.getName()
									.equals(AccounterServerConstants.VAT_ITEM_EC_SALES_GOODS_ZERO_RATED))
									&& (!taxAdjustment
											.getTaxItem()
											.getName()
											.equals(AccounterServerConstants.VAT_ITEM_EC_SALES_SERVICES_ZERO_RATED))) {
								if (taxAdjustment.getIncreaseVATLine())
									b.setAmount(b.getAmount()
											+ taxAdjustment.getTotal());
								else
									b.setAmount(b.getAmount()
											- taxAdjustment.getTotal());
							} else {

								if (taxAdjustment.getIncreaseVATLine())
									b.setAmount(b.getAmount()
											+ taxAdjustment.getTotal());
								else
									b.setAmount(b.getAmount()
											- taxAdjustment.getTotal());
							}
						} else {
							double amount = 0;
							if (taxAdjustment.getIncreaseVATLine())
								amount = taxAdjustment.getTotal();
							else
								amount = -1 * taxAdjustment.getTotal();

							if (vb.getVatBox()
									.equals(AccounterServerConstants.UK_BOX2_VAT_DUE_ON_ACQUISITIONS)
									|| vb.getVatBox()
											.equals(AccounterServerConstants.UK_BOX1_VAT_DUE_ON_SALES))
								amount = -1 * amount;

							b.setAmount(b.getAmount() + amount);
						}
					}
				}

			} else if (taxItem.getTaxAgency().getName()
					.equals(taxAgency.getName())) {

				VATReturnBox vb = taxItem.getVatReturnBox();

				for (int j = 0; j < boxes.size(); j++) {

					ClientBox box = boxes.get(j);

					if ((box).getName().equals(vb.getTotalBox())) {
						box.setAmount(box.getAmount() + entry.getNetAmount());

					} else if ((box).getName().equals(vb.getVatBox())) {
						if (vb.getVatBox()
								.equals(AccounterServerConstants.UK_BOX2_VAT_DUE_ON_ACQUISITIONS)
								|| (vb.getVatBox()
										.equals(AccounterServerConstants.UK_BOX4_VAT_RECLAMED_ON_PURCHASES) && !entry
										.isTAXGroupEntry())
								|| (vb.getVatBox()
										.equals(AccounterServerConstants.UK_BOX1_VAT_DUE_ON_SALES) && vb
										.getTotalBox()
										.equals(AccounterServerConstants.BOX_NONE)))
							box.setAmount(box.getAmount()
									+ (-1 * (entry.getTaxAmount())));

						else
							box.setAmount(box.getAmount()
									+ (entry.getTaxAmount()));

					}
					// if (box.getBoxNumber() == 4) {
					// box.setAmount(box.getAmount() + rcAmount);
					// rcAmount = 0;
					// }
					box.getTaxRateCalculations().add(null);
				}
			}
		}
		return boxes;
	}

	protected List<Box> createBoxes(TAXAgency vatAgency) {

		List<Box> boxes = new ArrayList<Box>();

		Box b1 = new Box();
		Box b2 = new Box();
		Box b3 = new Box();
		Box b4 = new Box();
		Box b5 = new Box();
		Box b6 = new Box();
		Box b7 = new Box();
		Box b8 = new Box();
		Box b9 = new Box();
		Box b10 = new Box();
		if (vatAgency.getVATReturn() == TAXReturn.VAT_RETURN_UK_VAT) {
			b1.setName(AccounterServerConstants.UK_BOX1_VAT_DUE_ON_SALES);
			b2.setName(AccounterServerConstants.UK_BOX2_VAT_DUE_ON_ACQUISITIONS);
			b3.setName(AccounterServerConstants.UK_BOX3_TOTAL_OUTPUT);
			b4.setName(AccounterServerConstants.UK_BOX4_VAT_RECLAMED_ON_PURCHASES);
			b5.setName(AccounterServerConstants.UK_BOX5_NET_VAT);
			b6.setName(AccounterServerConstants.UK_BOX6_TOTAL_NET_SALES);
			b7.setName(AccounterServerConstants.UK_BOX7_TOTAL_NET_PURCHASES);
			b8.setName(AccounterServerConstants.UK_BOX8_TOTAL_NET_SUPPLIES);
			b9.setName(AccounterServerConstants.UK_BOX9_TOTAL_NET_ACQUISITIONS);
			b10.setName(AccounterServerConstants.UK_BOX10_UNCATEGORISED);
		} else if (vatAgency.getVATReturn() == TAXReturn.VAT_RETURN_IRELAND) {
			b1.setName(AccounterServerConstants.IRELAND_BOX1_VAT_CHARGED_ON_SUPPIES);
			b2.setName(AccounterServerConstants.IRELAND_BOX2_VAT_DUE_ON_INTRA_EC_ACQUISITIONS);
			b3.setName(AccounterServerConstants.IRELAND_BOX3_VAT_ON_SALES);
			b4.setName(AccounterServerConstants.IRELAND_BOX4_VAT_ON_PURCHASES);
			b5.setName(AccounterServerConstants.IRELAND_BOX5_T3_T4_PAYMENT_DUE);
			b6.setName(AccounterServerConstants.IRELAND_BOX6_E1_GOODS_TO_EU);
			b7.setName(AccounterServerConstants.IRELAND_BOX7_E2_GOODS_FROM_EU);
			b8.setName(AccounterServerConstants.IRELAND_BOX8_TOTAL_NET_SALES);
			b9.setName(AccounterServerConstants.IRELAND_BOX9_TOTAL_NET_PURCHASES);
			b10.setName(AccounterServerConstants.IRELAND_BOX10_UNCATEGORISED);
		}
		b1.setBoxNumber(1);
		b2.setBoxNumber(2);
		b3.setBoxNumber(3);
		b4.setBoxNumber(4);
		b5.setBoxNumber(5);
		b6.setBoxNumber(6);
		b7.setBoxNumber(7);
		b8.setBoxNumber(8);
		b9.setBoxNumber(9);
		b10.setBoxNumber(10);

		boxes.add(b1);
		boxes.add(b2);
		boxes.add(b3);
		boxes.add(b4);
		boxes.add(b5);
		boxes.add(b6);
		boxes.add(b7);
		boxes.add(b8);
		boxes.add(b9);
		boxes.add(b10);
		return boxes;
	}

	protected List<ClientBox> createClientBoxes(TAXAgency vatAgency) {

		List<ClientBox> boxes = new ArrayList<ClientBox>();

		ClientBox b1 = new ClientBox();
		ClientBox b2 = new ClientBox();
		ClientBox b3 = new ClientBox();
		ClientBox b4 = new ClientBox();
		ClientBox b5 = new ClientBox();
		ClientBox b6 = new ClientBox();
		ClientBox b7 = new ClientBox();
		ClientBox b8 = new ClientBox();
		ClientBox b9 = new ClientBox();
		ClientBox b10 = new ClientBox();
		if (vatAgency.getVATReturn() == TAXReturn.VAT_RETURN_UK_VAT) {
			b1.setName(AccounterServerConstants.UK_BOX1_VAT_DUE_ON_SALES);
			b2.setName(AccounterServerConstants.UK_BOX2_VAT_DUE_ON_ACQUISITIONS);
			b3.setName(AccounterServerConstants.UK_BOX3_TOTAL_OUTPUT);
			b4.setName(AccounterServerConstants.UK_BOX4_VAT_RECLAMED_ON_PURCHASES);
			b5.setName(AccounterServerConstants.UK_BOX5_NET_VAT);
			b6.setName(AccounterServerConstants.UK_BOX6_TOTAL_NET_SALES);
			b7.setName(AccounterServerConstants.UK_BOX7_TOTAL_NET_PURCHASES);
			b8.setName(AccounterServerConstants.UK_BOX8_TOTAL_NET_SUPPLIES);
			b9.setName(AccounterServerConstants.UK_BOX9_TOTAL_NET_ACQUISITIONS);
			b10.setName(AccounterServerConstants.UK_BOX10_UNCATEGORISED);
		} else if (vatAgency.getVATReturn() == TAXReturn.VAT_RETURN_IRELAND) {
			b1.setName(AccounterServerConstants.IRELAND_BOX1_VAT_CHARGED_ON_SUPPIES);
			b2.setName(AccounterServerConstants.IRELAND_BOX2_VAT_DUE_ON_INTRA_EC_ACQUISITIONS);
			b3.setName(AccounterServerConstants.IRELAND_BOX3_VAT_ON_SALES);
			b4.setName(AccounterServerConstants.IRELAND_BOX4_VAT_ON_PURCHASES);
			b5.setName(AccounterServerConstants.IRELAND_BOX5_T3_T4_PAYMENT_DUE);
			b6.setName(AccounterServerConstants.IRELAND_BOX6_E1_GOODS_TO_EU);
			b7.setName(AccounterServerConstants.IRELAND_BOX7_E2_GOODS_FROM_EU);
			b8.setName(AccounterServerConstants.IRELAND_BOX8_TOTAL_NET_SALES);
			b9.setName(AccounterServerConstants.IRELAND_BOX9_TOTAL_NET_PURCHASES);
			b10.setName(AccounterServerConstants.IRELAND_BOX10_UNCATEGORISED);
		}
		b1.setBoxNumber(1);
		b2.setBoxNumber(2);
		b3.setBoxNumber(3);
		b4.setBoxNumber(4);
		b5.setBoxNumber(5);
		b6.setBoxNumber(6);
		b7.setBoxNumber(7);
		b8.setBoxNumber(8);
		b9.setBoxNumber(9);
		b10.setBoxNumber(10);

		boxes.add(b1);
		boxes.add(b2);
		boxes.add(b3);
		boxes.add(b4);
		boxes.add(b5);
		boxes.add(b6);
		boxes.add(b7);
		boxes.add(b8);
		boxes.add(b9);
		boxes.add(b10);
		return boxes;
	}
}
