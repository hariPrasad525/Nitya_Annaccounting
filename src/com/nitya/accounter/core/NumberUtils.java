package com.nitya.accounter.core;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.hibernate.FlushMode;
import org.hibernate.Query;
import org.hibernate.Session;

import com.nitya.accounter.utils.HibernateUtil;
import com.nitya.accounter.web.client.core.ClientAccount;
import com.nitya.accounter.web.client.ui.UIUtils;

public class NumberUtils {

	public static String getNextTransactionNumber(int transactionType,
			Company company) {

		return getNextTransactionNumber(transactionType, company.getId());

		// String prevNumber = getPreviousTransactionNumber(transactionType,
		// company);
		//
		// return getStringwithIncreamentedDigit(prevNumber);
	}

	public static String getPreviousTransactionNumber(int transactionType,
			Company company) {

		Query query = HibernateUtil.getCurrentSession()
				.getNamedQuery("getTransactionNumber.by.type")
				.setParameter("transactionType", transactionType)
				.setEntity("company", company);

		List list = query.list();
		if ((list.size() == 0)) {
			return "0";
		}
		long max = 0;
		String maxStr = "";
		for (int i = list.size() - 1; i >= 0; i--) {
			String num = (String) list.get(i);
			String string = num.replaceAll("[\\D]", "");
			if (string.length() > 0) {
				long p = 0;
				try {
					p = Long.parseLong(string);
				} catch (NumberFormatException e) {
					p = 0;
				}
				if (p > max) {
					max = p;
					maxStr = num;
				}
			}
			// else {
			// if (maxCount != 0) {
			// maxCount = maxCount - 1;
			// return getPreviousTransactionNumber(transactionType,
			// maxCount);
			// }
			// }
		}
		return max == 0 ? "0" : maxStr;
	}

	public static String getNextCheckNumber(long companyId, long accountId) {
		String prevNumber = getPreviousCheckNumber(companyId, accountId);

		return getStringwithIncreamentedDigit(prevNumber);
	}

	private static String getPreviousCheckNumber(long companyId, long accountId) {
		Query query = HibernateUtil.getCurrentSession()
				.getNamedQuery("getCheckNumber")
				.setLong("accountId", accountId)
				.setLong("companyId", companyId);

		List list = query.list();
		if ((list.size() == 0)) {
			return "0";
		}

		long max = list.get(0) != null ? Long.parseLong((String) list.get(0))
				: 0;
		for (int i = 1; i < list.size(); i++) {
			Long num = list.get(i) != null ? Long.parseLong(((String) list
					.get(i))) : 0;
			if (num > max) {
				max = num;
			}
		}

		return "" + max;
	}

	// public static String getNextVoucherNumber(Company company) {
	//
	// Session session = HibernateUtil.getCurrentSession();
	// Query query = session.getNamedQuery("get.Entry").setEntity("company",
	// company);
	// List list1 = query.list();
	//
	// if (list1.size() <= 0) {
	//
	// return getNextTransactionNumber(Transaction.TYPE_JOURNAL_ENTRY,
	// company);
	// }
	// query = session.getNamedQuery("getEntry.byId.andMax").setEntity(
	// "company", company);
	// List list = query.list();
	//
	// if (list != null) {
	// return getStringwithIncreamentedDigit(((String) list.get(0)));
	// } else
	// return "1";
	//
	// }

	public static String getNextFixedAssetNumber(Company company) {
		String prevNumber = getPreviousFixedAssetNumber(company);
		return getStringwithIncreamentedDigit(prevNumber);

	}

	private static String getPreviousFixedAssetNumber(Company company) {
		Session session = HibernateUtil.getCurrentSession();
		Query query = session.getNamedQuery("getassestNumber.from.FixedAsset")
				.setEntity("company", company);
		List<?> list = query.list();
		if (list.size() == 0) {
			return "0";
		}
		for (int i = list.size() - 1; i >= 0; i--) {
			String number = (String) list.get(i);
			if (number.replaceAll("//D", "").length() > 0) {
				return number;
			}

		}
		return null;
	}

	public static String getNextAutoCustomerNumber(Company company) {

		Query query = HibernateUtil.getCurrentSession()
				.getNamedQuery("getCustomerNumber.orderBy.customerNumber")
				.setEntity("company", company);
		int count = 0;
		List list = query.list();
		String arr[] = (String[]) list.toArray(new String[list.size()]);
		Long longArr[] = new Long[arr.length];
		for (int iii = 0; iii < arr.length; iii++) {
			if (arr[iii].isEmpty()) {
				longArr[iii] = 0L;
			} else {
				if (isContainsChars(arr[iii])) {
					count++;
					continue;
				}
				longArr[iii] = Long.parseLong(arr[iii].trim());
			}
		}
		longArr = Arrays.copyOf(longArr, arr.length - count);
		Arrays.sort(longArr);

		if ((list.size() == 0)) {
			return "1";
		}
		Long number = longArr[0];
		for (int i = 0; i < longArr.length; i++) {
			Long num = longArr[i];
			while (number.equals(num)) {
				number++;
			}
		}

		return number.toString();

	}

	/**
	 * 
	 * @param autoIncrementNumber
	 * @return
	 */
	private static boolean isContainsChars(String autoIncrementNumber) {
		try {
			Long.parseLong(autoIncrementNumber);
		} catch (NumberFormatException e) {
			return true;
		}
		return false;
	}

	public static String getNextAutoVendorNumber(Company company) {

		Query query = HibernateUtil.getCurrentSession()
				.getNamedQuery("getVendorNumber.byId.andOrder")
				.setEntity("company", company);

		List list = query.list();
		String arr[] = (String[]) list.toArray(new String[list.size()]);
		long longArr[] = new long[arr.length];
		for (int iii = 0; iii < arr.length; iii++) {
			if (arr[iii].isEmpty()) {
				longArr[iii] = 0L;
			} else {
				if (isContainsChars(arr[iii])) {
					continue;
				}
				longArr[iii] = Long.parseLong(arr[iii].trim());
			}
		}
		Arrays.sort(longArr);

		if ((list.size() == 0)) {
			return "1";
		}
		long number = longArr[0];
		for (int i = 0; i < longArr.length; i++) {
			long num = longArr[i];
			while (number == num) {
				number++;
			}
		}

		return String.valueOf(number);

	}

	public static String getStringwithIncreamentedDigit(String prevNumber) {

		String incredNumber = "";
		if (prevNumber != null) {
			for (int i = prevNumber.length() - 1; i >= 0; i--) {
				char ch = prevNumber.charAt(i);

				if (incredNumber.length() > 0 && !Character.isDigit(ch)) {
					break;
				} else if (Character.isDigit(ch)) {
					incredNumber = ch + incredNumber;
				}
			}

		}
		if (incredNumber.length() > 0) {
			// incredNumber = new
			// StringBuffer(incredNumber).reverse().toString();
			prevNumber = prevNumber.replace(incredNumber,
					"" + (Long.parseLong(incredNumber) + 1));
		}
		return prevNumber;

	}

	public static String getStringwithIncreamentedDigitForCustomer(
			String prevNumber) {

		String incredNumber = "";
		if (prevNumber != null) {
			for (int i = prevNumber.length() - 1; i >= 0; i--) {
				char ch = prevNumber.charAt(i);

				if (incredNumber.length() > 0 && !Character.isDigit(ch)) {
					break;
				} else if (Character.isDigit(ch)) {
					incredNumber = ch + incredNumber;
				}
			}

		}
		if (incredNumber.length() > 0) {

			prevNumber = prevNumber
					.replace(
							incredNumber,
							""
									+ (new BigInteger(incredNumber)
											.add(BigInteger.valueOf(1))));

		}
		return prevNumber;

	}

	public static String getNextAccountNumber(long companyId, int accountType) {
		Session session = HibernateUtil.getCurrentSession();
		FlushMode flushMode = session.getFlushMode();
		session.setFlushMode(FlushMode.COMMIT);
		int subBaseType = UIUtils.getAccountSubBaseType(accountType);
		try {
			Query query = session.getNamedQuery("getNextAccountNumber")
					.setParameter("companyId", companyId)
					.setParameter("subbaseType", subBaseType);
			List<String> list = query.list();
			if (list.isEmpty()) {
				return String.valueOf(getMinimumRange(accountType));
			}
			Collections.sort(list);
			String maxNumber = (String) list.get(list.size() - 1);
			return NumberUtils.getStringwithIncreamentedDigit(maxNumber);
		} finally {
			session.setFlushMode(flushMode);
		}
	}

	private static int getMinimumRange(int accountType) {

		switch (accountType) {

		case ClientAccount.TYPE_CASH:
		case ClientAccount.TYPE_BANK:
		case ClientAccount.TYPE_ACCOUNT_RECEIVABLE:
		case ClientAccount.TYPE_OTHER_CURRENT_ASSET:
		case ClientAccount.TYPE_INVENTORY_ASSET:
			return NominalCodeRange.RANGE_OTHER_CURRENT_ASSET_MIN;
		case ClientAccount.TYPE_FIXED_ASSET:
			return NominalCodeRange.RANGE_FIXED_ASSET_MIN;
		case ClientAccount.TYPE_OTHER_ASSET:
			return NominalCodeRange.RANGE_OTHER_ASSET_MIN;
		case ClientAccount.TYPE_ACCOUNT_PAYABLE:
		case ClientAccount.TYPE_CREDIT_CARD:
		case ClientAccount.TYPE_OTHER_CURRENT_LIABILITY:
		case ClientAccount.TYPE_PAYROLL_LIABILITY:
			return NominalCodeRange.RANGE_OTER_CURRENT_LIABILITY_MIN;
		case ClientAccount.TYPE_LONG_TERM_LIABILITY:
			return NominalCodeRange.RANGE_LONGTERM_LIABILITY_MIN;
		case ClientAccount.TYPE_EQUITY:
			return NominalCodeRange.RANGE_EQUITY_MIN;
		case ClientAccount.TYPE_INCOME:
		case ClientAccount.TYPE_OTHER_INCOME:
			return NominalCodeRange.RANGE_INCOME_MIN;
		case ClientAccount.TYPE_COST_OF_GOODS_SOLD:
			return NominalCodeRange.RANGE_COST_OF_GOODS_SOLD_MIN;
		case ClientAccount.TYPE_EXPENSE:
			return NominalCodeRange.RANGE_EXPENSE_MIN;
		case ClientAccount.TYPE_OTHER_EXPENSE:
			return NominalCodeRange.RANGE_OTHER_EXPENSE_MIN;
		default:
			return 0;
		}
	}

	@SuppressWarnings("unchecked")
	public static String getNextTransactionNumber(int transactionType,
			long company) {

		Session session = HibernateUtil.getCurrentSession();
		String lastNum = ((String) session
				.getNamedQuery("get.Last.Transaction.Number")
				.setParameter("transactionType", transactionType)
				.setParameter("companyId", company).uniqueResult());
		if (lastNum == null) {
			return "1";
		}
		char[] lastNumber = lastNum.toCharArray();
		List<String> list = session
				.getNamedQuery("get.Transaction.Number.by.Number.Id")
				.setParameter("transactionType", transactionType)
				.setParameter("companyId", company).list();

		int nonCharIndex = -1;
		for (int i = lastNumber.length - 1; i >= 0; i--) {
			if (!Character.isDigit(lastNumber[i])) {
				nonCharIndex = i;
				break;
			}
		}

		String preNumber = lastNum;
		long max = 0;
		label: for (String s : list) {
			if (s.isEmpty()) {
				continue;
			}
			char[] charArray = s.toCharArray();
			for (int idx = 0; idx < charArray.length; idx++) {
				char ch = charArray[idx];
				if (idx <= nonCharIndex) {
					if (ch != lastNumber[idx]) {
						continue label;
					}
				} else {
					if (!Character.isDigit(ch)) {
						continue label;
					}
				}
			}
			if (nonCharIndex != s.length() - 1) {
				long parseInt = Long.parseLong(s.substring(nonCharIndex + 1));
				if (parseInt > max) {
					max = parseInt;
				}
			}
		}

		StringBuffer buff = new StringBuffer();
		if (nonCharIndex != -1) {
			buff.append(preNumber.substring(0, nonCharIndex + 1));
		}
		buff.append(max + 1);
		return buff.toString();
	}

}
