package com.vimukti.accounter.web.client.comet;

import net.zschech.gwt.comet.client.CometSerializer;
import net.zschech.gwt.comet.client.SerialTypes;

import com.vimukti.accounter.web.client.core.AccounterCommand;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientBank;
import com.vimukti.accounter.web.client.core.ClientCreditRating;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientCustomerGroup;
import com.vimukti.accounter.web.client.core.ClientDepreciation;
import com.vimukti.accounter.web.client.core.ClientFiscalYear;
import com.vimukti.accounter.web.client.core.ClientFixedAsset;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientItemGroup;
import com.vimukti.accounter.web.client.core.ClientPaymentTerms;
import com.vimukti.accounter.web.client.core.ClientPriceLevel;
import com.vimukti.accounter.web.client.core.ClientSalesPerson;
import com.vimukti.accounter.web.client.core.ClientShippingMethod;
import com.vimukti.accounter.web.client.core.ClientShippingTerms;
import com.vimukti.accounter.web.client.core.ClientTAXAgency;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTAXGroup;
import com.vimukti.accounter.web.client.core.ClientTAXItem;
import com.vimukti.accounter.web.client.core.ClientTAXItemGroup;
import com.vimukti.accounter.web.client.core.ClientTAXReturn;
import com.vimukti.accounter.web.client.core.ClientVATReturnBox;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.ClientVendorGroup;

@SerialTypes({ ClientAccount.class, ClientCustomer.class, ClientItem.class,
		ClientTAXGroup.class, ClientCustomerGroup.class,
		ClientVendorGroup.class, ClientPaymentTerms.class,
		ClientShippingMethod.class, ClientShippingTerms.class,
		ClientPriceLevel.class, ClientItemGroup.class, ClientSalesPerson.class,
		ClientCreditRating.class, ClientFiscalYear.class, ClientVendor.class,
		ClientBank.class, AccounterCommand.class, ClientTAXAgency.class,
		ClientTAXCode.class, ClientTAXReturn.class, ClientVATReturnBox.class,
		ClientTAXGroup.class, ClientTAXItem.class, ClientTAXItemGroup.class,
		ClientFixedAsset.class, ClientDepreciation.class })
public abstract class AccounterCometSerializer extends CometSerializer {
}