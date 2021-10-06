package com.vimukti.accounter.text.commands;

import org.hibernate.Session;

import com.vimukti.accounter.core.TAXAgency;
import com.vimukti.accounter.core.TAXItem;
import com.vimukti.accounter.text.ITextData;
import com.vimukti.accounter.text.ITextResponse;
import com.vimukti.accounter.text.commands.transaction.AbstractTransactionCommand;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.exception.AccounterException;

/**
 * taxItemName; description; taxRate; taxAgencyName;
 * 
 * @author vimukti10
 * 
 */
public class TaxItemCommand extends AbstractTransactionCommand {
	private String taxItemName;
	private String description;
	private double taxRate;
	private String taxAgencyName;

	@Override
	public boolean parse(ITextData data, ITextResponse respnse) {
		// Tax Item Name
		taxItemName = data.nextString("");
		// Description
		description = data.nextString("");
		if (!data.isDouble()) {
			respnse.addError("Invalid Double for Tax Rate");
			return false;

		}
		taxRate = data.nextDouble(0);
		taxAgencyName = data.nextString("");
		return true;
	}

	@Override
	public void process(ITextResponse respnse) throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();

		TAXItem taxItem = getObject(TAXItem.class, "name", taxItemName);
		if (taxItem == null) {
			taxItem = new TAXItem();
		}
		taxItem.setName(taxItemName);
		taxItem.setDescription(description);
		taxItem.setActive(true);
		taxItem.setTaxRate(taxRate);

		TAXAgency taxAgency = getObject(TAXAgency.class, "name", taxAgencyName);
		if (taxAgency == null) {
			taxAgency = new TAXAgency();
			taxAgency.setName(taxAgencyName);
			session.save(taxAgency);
		}
		taxItem.setTaxAgency(taxAgency);
		saveOrUpdate(taxItem);
	}

}
