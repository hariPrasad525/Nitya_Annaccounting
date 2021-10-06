package com.vimukti.accounter.text.commands;

import org.hibernate.Session;

import com.vimukti.accounter.core.TAXCode;
import com.vimukti.accounter.core.TAXItem;
import com.vimukti.accounter.text.ITextData;
import com.vimukti.accounter.text.ITextResponse;
import com.vimukti.accounter.text.commands.transaction.AbstractTransactionCommand;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.exception.AccounterException;

public class TaxCodeCommand extends AbstractTransactionCommand {

	private String taxCodeName;
	private String description;
	private String taxitemOrGroupForSales;
	private String taxitemOrGroupForpurchase;

	@Override
	public boolean parse(ITextData data, ITextResponse respnse) {
		// Tax Code
		taxCodeName = data.nextString("");
		// Description
		description = data.nextString("");
		// Tax item or group for purchases
		taxitemOrGroupForpurchase = data.nextString("");
		// Tax item or group for Sales
		taxitemOrGroupForSales = data.nextString("");
		return true;
	}

	@Override
	public void process(ITextResponse respnse) throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		TAXCode taxCode = getObject(TAXCode.class, "name", taxCodeName);
		if (taxCode == null) {
			taxCode = new TAXCode();
		}
		taxCode.setName(taxCodeName);
		taxCode.setDescription(description);
		// taxitemOrGroupForpurchase
		TAXItem taxItem = getObject(TAXItem.class, "name",
				taxitemOrGroupForpurchase);
		if (taxItem == null) {
			taxItem = new TAXItem();
			taxItem.setName(taxitemOrGroupForpurchase);
			session.save(taxItem);
		}
		taxCode.setTAXItemGrpForPurchases(taxItem);
		// taxitemOrGroupForSales
		TAXItem salesTaxItem = getObject(TAXItem.class, "name",
				taxitemOrGroupForSales);
		if (salesTaxItem == null) {
			salesTaxItem = new TAXItem();
			salesTaxItem.setName(taxitemOrGroupForpurchase);
			session.save(salesTaxItem);
		}
		taxCode.setActive(true);
		taxCode.setTaxable(true);
		taxCode.setTAXItemGrpForSales(salesTaxItem);
		saveOrUpdate(taxCode);
	}
}
