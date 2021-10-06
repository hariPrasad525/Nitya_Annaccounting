package com.vimukti.accounter.migration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.TAXRateCalculation;
import com.vimukti.accounter.core.Transaction;

public class MigratorContext {

	private Map<String, Long> ids = new HashMap<String, Long>();
	private Company company;
	private Map<String, List<Long>> childrenMap = new HashMap<>();
	private Map<String, String> childrenNames = new HashMap<>();
	private int lastAccNumber = 1055;
	private int taxAgencyNo = 1;
	private TransactionMigrationContext currentTrasMigrationContext;
	private LocalIdProvider localIdProvider;

	private long contactNo = 1L;
	private String maxTXNNumber;

	// Ecgine TAXRateCalculations by TXN
	private Map<Long, Map<Long, Long>> newTaxrateCalculationIDS = new HashMap<Long, Map<Long, Long>>();

	private Map<Integer, Set<String>> transactionNumbers = new HashMap<>();
	private Set<TAXRateCalculation> taxRateCalculations = new HashSet<>();
	private Map<Long, List<Tuple>> taxRateCalculationsByTXN = new HashMap<>();

	public void put(String name, Map<Long, Long> migrateAccounts) {
		for (Entry<Long, Long> oldId : migrateAccounts.entrySet()) {
			ids.put(name + oldId.getKey(), oldId.getValue());
		}
	}

	public void put(String name, Long oldId, Long newID) {
		ids.put(name + oldId, newID);
	}

	public Long get(String name, long id) {
		return ids.get(name + id);
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public void setAdminRole(String key, long roleID) {
		ids.put(key, roleID);
	}

	public Long getAdminRole() {
		return ids.get("AdminRole");
	}

	public Map<String, List<Long>> getChildrenMap() {
		return childrenMap;
	}

	public int getNextAccountNumber() {
		return ++lastAccNumber;
	}

	public String getNextTaxAgencyNumber() {
		return String.valueOf(taxAgencyNo++);
	}

	public TransactionMigrationContext getCurrentTrasMigrationContext() {
		return currentTrasMigrationContext;
	}

	public void setCurrentTrasMigrationContext(
			TransactionMigrationContext currentTrasMigrationContext) {
		this.currentTrasMigrationContext = currentTrasMigrationContext;
	}

	public long getNextContactNo() {
		return contactNo++;
	}

	public LocalIdProvider getLocalIdProvider() {
		return localIdProvider;
	}

	public void setLocalIdProvider(LocalIdProvider localIdProvider) {
		this.localIdProvider = localIdProvider;
	}

	public void putChilderName(String identity, String childrenName) {
		this.childrenNames.put(identity, childrenName);
	}

	public String getChildrenName(String identity) {
		return childrenNames.get(identity);
	}

	public Map<Integer, Set<String>> getTransactionNumbers() {
		return transactionNumbers;
	}

	public String getMaxTXNNumber() {
		return maxTXNNumber;
	}

	public void setMaxTXNNumber(String maxTXNNumber) {
		this.maxTXNNumber = maxTXNNumber;
	}

	public boolean isDuplicateTXNNumber(int type, String tXNNumber) {
		Set<String> txns = transactionNumbers.get(type);
		if (txns != null && txns.contains(tXNNumber)) {
			return true;
		}
		return false;
	}

	public void putNewTaxrateCalculationIDS(Long txnID, Long taxItem,
			Long taxrateCalculationID) {
		Map<Long, Long> map = new HashMap<>();
		map.put(taxItem, taxrateCalculationID);
		newTaxrateCalculationIDS.put(txnID, map);
	}

	public Long getTaxrateCalculation(Transaction transaction, long id) {
		return newTaxrateCalculationIDS.get(transaction).get(id);
	}

	public void setTaxRateCalculations(
			Set<TAXRateCalculation> TaxRateCalculations) {
		this.taxRateCalculations = TaxRateCalculations;

	}

	public Set<TAXRateCalculation> getTaxRateCalculations() {
		return taxRateCalculations;
	}

	public List<Tuple> get(Long ecgineTXNId) {
		return taxRateCalculationsByTXN.get(ecgineTXNId);
	}

	public void put(Long ecgineTXNId, Long taxItemID, Long taxRateCalculationID) {
		List<Tuple> list = null;
		if (taxRateCalculationsByTXN.isEmpty()) {
			list = new ArrayList<>();
			taxRateCalculationsByTXN.put(ecgineTXNId, list);
		} else {
			list = taxRateCalculationsByTXN.get(ecgineTXNId);
			if (list == null) {
				list = new ArrayList<>();
				taxRateCalculationsByTXN.put(ecgineTXNId, list);
			}
		}
		list.add(new Tuple(taxItemID, taxRateCalculationID));
	}

	class Tuple {
		private Long taxItemID;
		private Long taxRateCalculationID;

		public Tuple(Long taxItemID, Long taxRateCalculationID) {
			this.taxItemID = taxItemID;
			this.taxRateCalculationID = taxRateCalculationID;
		}

		public Long getTaxItemID() {
			return taxItemID;
		}

		public Long getTaxRateCalculationID() {
			return taxRateCalculationID;
		}
	}
}
