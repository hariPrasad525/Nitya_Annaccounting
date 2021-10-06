package com.nitya.accounter.web.client.comet;

import com.nitya.accounter.web.client.core.AccounterCommand;
import com.nitya.accounter.web.client.core.ClientAccount;
import com.nitya.accounter.web.client.core.ClientBank;
import com.nitya.accounter.web.client.core.ClientCreditRating;
import com.nitya.accounter.web.client.core.ClientCustomer;
import com.nitya.accounter.web.client.core.ClientCustomerGroup;
import com.nitya.accounter.web.client.core.ClientDepreciation;
import com.nitya.accounter.web.client.core.ClientFiscalYear;
import com.nitya.accounter.web.client.core.ClientFixedAsset;
import com.nitya.accounter.web.client.core.ClientItem;
import com.nitya.accounter.web.client.core.ClientItemGroup;
import com.nitya.accounter.web.client.core.ClientPaymentTerms;
import com.nitya.accounter.web.client.core.ClientPriceLevel;
import com.nitya.accounter.web.client.core.ClientSalesPerson;
import com.nitya.accounter.web.client.core.ClientShippingMethod;
import com.nitya.accounter.web.client.core.ClientShippingTerms;
import com.nitya.accounter.web.client.core.ClientTAXAgency;
import com.nitya.accounter.web.client.core.ClientTAXCode;
import com.nitya.accounter.web.client.core.ClientTAXGroup;
import com.nitya.accounter.web.client.core.ClientTAXItem;
import com.nitya.accounter.web.client.core.ClientTAXItemGroup;
import com.nitya.accounter.web.client.core.ClientTAXReturn;
import com.nitya.accounter.web.client.core.ClientVATReturnBox;
import com.nitya.accounter.web.client.core.ClientVendor;
import com.nitya.accounter.web.client.core.ClientVendorGroup;

import net.zschech.gwt.comet.client.CometSerializer;
import net.zschech.gwt.comet.client.SerialTypes;

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