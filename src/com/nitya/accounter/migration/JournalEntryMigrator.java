package com.nitya.accounter.migration;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ibm.icu.math.BigDecimal;
import com.nitya.accounter.core.Account;
import com.nitya.accounter.core.JournalEntry;
import com.nitya.accounter.core.Transaction;
import com.nitya.accounter.core.TransactionItem;

public class JournalEntryMigrator extends TransactionMigrator<JournalEntry> {

	@Override
	public JSONObject migrate(JournalEntry entry, MigratorContext context) throws JSONException {
		JSONObject jsonObject = super.migrate(entry, context);
		JSONObject external = new JSONObject();
		external.put("com.nitya.accounter.shared.common.JournalEntry", jsonObject);
		String _lid = context.getLocalIdProvider().getOrCreate(entry);
		jsonObject.put("@id", _lid);
		jsonObject.put("@_lid", _lid);

		// Journal Entry items
		JSONArray jeItems = new JSONArray();
		JSONObject items = new JSONObject();
		items.put("com.nitya.accounter.shared.common.JournalEntryItem", jeItems);
		for (TransactionItem item : entry.getTransactionItems()) {
			JSONObject jeItem = new JSONObject();
			// Journal Entry Item Account
			String _localid = context.getLocalIdProvider().getOrCreate(item);
			jeItem.put("@id", _localid);
			jeItem.put("@_lid", _localid);
			// Account setting
			Account account = item.getAccount();
			JSONObject inter = new JSONObject();
			JSONObject outter = new JSONObject();
			outter.put("com.nitya.accounter.shared.common.Account", inter);
			String localId = context.getLocalIdProvider().getOrCreate(account);
			inter.put("@_oid", context.get("Account", account.getID()));
			inter.put("@_lid", localId);
			jeItem.put("account", outter);

			jeItem.put("memo", item.getDescription());
			double total = item.getLineTotal();
			if (total > 0) {
				jeItem.put("type", "Debit");
				jeItem.put("debitAmount", BigDecimal.valueOf(total));
			} else {
				jeItem.put("type", "Credit");
				jeItem.put("creditAmount", BigDecimal.valueOf(total));
			}
			jeItems.put(jeItem);
		}
		jsonObject.put("journalEntryItems", items);
		return external;
	}

	public void addRestrictions(Criteria criteria) {
		criteria.add(Restrictions.eq("type", Transaction.TYPE_JOURNAL_ENTRY));
		criteria.add(Restrictions.eq("involvedAccount", null));
		criteria.add(Restrictions.eq("involvedPayee", null));
	}
}
