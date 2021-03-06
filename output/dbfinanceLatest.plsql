PGDMP         %    	            w           finance    11.1    11.1 %   ?           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                       false            ?           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                       false            ?           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                       false            ?           1262    16393    finance    DATABASE     e   CREATE DATABASE finance WITH TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'C' LC_CTYPE = 'C';
    DROP DATABASE finance;
             postgres    false                       1255    28910    changedpasswordcount()    FUNCTION     ?  CREATE FUNCTION public.changedpasswordcount() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
				 DECLARE
				 total integer;
				BEGIN
				IF(TG_OP = 'UPDATE') THEN
					IF ( OLD.PASSWORD != NEW.PASSWORD) THEN
						total=NEW.CHANGED_PASSWORD;
		                NEW.CHANGED_PASSWORD =total+1;
		                RETURN NEW;
	                END IF;
                END IF;
                 RETURN NULL;
				END;
				$$;
 -   DROP FUNCTION public.changedpasswordcount();
       public       postgres    false                       1255    28916    clientupdate()    FUNCTION     5  CREATE FUNCTION public.clientupdate() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
				DECLARE
					  clientid      bigint;
				BEGIN
				clientid=NEW.ID;
				NEW.PREMIUM_COMPANIES= (SELECT COUNT(*) FROM COMPANY C LEFT JOIN USERS U ON U.ID=C.CREATED_BY LEFT JOIN CLIENT CL2 ON CL2.ID=U.CLIENT_ID LEFT JOIN CLIENT_SUBSCRIPTION CS ON CL2.CLIENT_SUBSCRIPTION=CS.ID LEFT JOIN SUBSCRIPTION S ON S.ID=CS.SUBSCRIPTION_ID WHERE S.TYPE=3 AND C.ID IN (SELECT COMPANY_ID FROM USERS U2 WHERE U2.CLIENT_ID=clientid AND U2.IS_DELETED=FALSE));
				 RETURN NEW;
				END;
				$$;
 %   DROP FUNCTION public.clientupdate();
       public       postgres    false            ?           1255    28906    companiescount()    FUNCTION     ?  CREATE FUNCTION public.companiescount() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
				 DECLARE
				       userID          bigint;
				       clientID        bigint;
				BEGIN 
				IF (TG_OP ='INSERT') THEN
					  userID=NEW.CREATED_BY;
					  clientID=(select client_id from users  u where u.id=userID);
					  UPDATE CLIENT SET COMPANIES=COMPANIES +1  WHERE id = clientID;
					  RETURN NEW;
			    END IF;
                RETURN NULL;
				END;
				$$;
 '   DROP FUNCTION public.companiescount();
       public       postgres    false            	           1255    28903    create_subscriptions()    FUNCTION     k  CREATE FUNCTION public.create_subscriptions() RETURNS boolean
    LANGUAGE plpgsql
    AS $$DECLARE
				R RECORD;
				_ID	BIGINT;
				BEGIN
				FOR R IN SELECT * FROM CLIENT
				LOOP
					IF R.CLIENT_SUBSCRIPTION IS NULL THEN INSERT INTO CLIENT_SUBSCRIPTION (
					SUBSCRIPTION_ID, LAST_MODIFIED, CREATED_DATE, EXPIRED_DATE, PREMIUM_TYPE) VALUES
					(1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, (DATE('NOW') + INTERVAL '2 YEAR'), 0)
					RETURNING ID INTO _ID ;
					
					UPDATE CLIENT
					SET    CLIENT_SUBSCRIPTION = _ID
					WHERE  CLIENT.ID = R.ID;  
				END IF;
				END LOOP;
				RETURN
				TRUE;
				END;
				$$;
 -   DROP FUNCTION public.create_subscriptions();
       public       postgres    false            ?           1255    28886    delete_company(bigint)    FUNCTION     ?=  CREATE FUNCTION public.delete_company(cid bigint) RETURNS boolean
    LANGUAGE plpgsql
    AS $$begin

		SET CONSTRAINTS ALL DEFERRED;
		
		DELETE FROM company_fields
		WHERE  company_id = cid;

		DELETE FROM payee_fields
		USING  payee
		WHERE  payee_id = payee.id
			   AND payee.company_id = cid;

		DELETE FROM cheque_layout
		WHERE  company_id = cid;

		DELETE FROM attachments
		WHERE  company_id = cid;

		DELETE FROM message_or_task
		WHERE  company_id = cid;

		DELETE FROM account_amounts
		USING  account
		WHERE  account_id = account.id
			   AND account.company_id = cid;

		DELETE FROM account_transaction
		WHERE  company_id = cid;
		
		DELETE FROM payee_update
		WHERE company_id = cid;
		
		DELETE FROM item_update
		WHERE company_id = cid;

		DELETE FROM adjustment_reason
		WHERE  company_id = cid;

		DELETE FROM reconciliation_item
		USING  reconciliation
		WHERE  reconciliation_id = reconciliation.id
			   AND reconciliation.company_id = cid;

		DELETE FROM reconciliation
		WHERE  company_id = cid;

		DELETE FROM bank_account
		USING  account
		WHERE  bank_account.id = account.id
			   AND account.company_id = cid;

		DELETE FROM bank
		WHERE  company_id = cid;

		DELETE FROM branding_theme
		WHERE  company_id = cid;

		DELETE FROM budgetitem
		WHERE  company_id = cid;

		DELETE FROM budget
		WHERE  company_id = cid;

		DELETE FROM cash_purchase_orders
		USING  TRANSACTION 
		WHERE  cash_purchase_orders.cash_purchase_id=TRANSACTION.id
			   and TRANSACTION.company_id = cid;
		
		DELETE FROM cash_purchase_estimates
		USING  TRANSACTION 
		WHERE  cash_purchase_estimates.cash_purchase_id=TRANSACTION.id
			   and TRANSACTION.company_id = cid;
			   
		DELETE FROM cash_purchase
		USING  TRANSACTION
		WHERE  cash_purchase.id = TRANSACTION.id
			   AND TRANSACTION.company_id = cid;

		DELETE FROM cash_sales
		USING  TRANSACTION
		WHERE  cash_sales.id = TRANSACTION.id
			   AND TRANSACTION.company_id = cid;

		DELETE FROM credit_card_charges
		USING  TRANSACTION
		WHERE  credit_card_charges.id = TRANSACTION.id
			   AND TRANSACTION.company_id = cid;

		DELETE FROM creditrating
		WHERE  company_id = cid;

		DELETE FROM customer_credit_memo
		USING  TRANSACTION
		WHERE  customer_credit_memo.id = TRANSACTION.id
			   AND TRANSACTION.company_id = cid;

		DELETE FROM customer_group
		WHERE  company_id = cid;

		DELETE FROM customer_prepayment
		USING  TRANSACTION
		WHERE  customer_prepayment.id = TRANSACTION.id
			   AND TRANSACTION.company_id = cid;

		DELETE FROM customer_refund
		USING  TRANSACTION
		WHERE  customer_refund.id = TRANSACTION.id
			   AND TRANSACTION.company_id = cid;

		DELETE FROM customfield
		WHERE  company_id = cid;

		DELETE FROM depreciation
		WHERE  company_id = cid;

		DELETE FROM enter_bill_orders
		USING  TRANSACTION
		WHERE  enter_bill_id = TRANSACTION.id
		       AND TRANSACTION.company_id = cid; 
		
		DELETE FROM enter_bill
		USING  TRANSACTION
		WHERE  enter_bill.id = TRANSACTION.id
			   AND TRANSACTION.company_id = cid;

		DELETE FROM enterbill_estimates
		USING  estimate,
			   TRANSACTION
		WHERE  enterbill_estimates.elt = estimate.id
			   AND TRANSACTION.id = estimate.id
			   AND TRANSACTION.company_id = cid;

		DELETE FROM fiscal_year
		WHERE  company_id = cid;

		DELETE FROM fixed_asset
		WHERE  company_id = cid;

		DELETE FROM fixed_asset_history
		WHERE  company_id = cid;
		
		DELETE FROM invoice_estimates
		USING  estimate,
			   TRANSACTION
		WHERE  invoice_estimates.estimate_id = estimate.id
			   AND TRANSACTION.id = estimate.id
			   AND TRANSACTION.company_id = cid;

		DELETE FROM invoice
		USING  TRANSACTION
		WHERE  invoice.id = TRANSACTION.id
			   AND TRANSACTION.company_id = cid;

		DELETE FROM estimate
		USING  TRANSACTION
		WHERE  estimate.id = TRANSACTION.id
			   AND TRANSACTION.company_id = cid;

		DELETE FROM item_receipt
		USING  TRANSACTION
		WHERE  item_receipt.id = TRANSACTION.id
			   AND TRANSACTION.company_id = cid;


		DELETE FROM itemgroup
		WHERE  company_id = cid;

		DELETE FROM journal_entry
		USING  TRANSACTION
		WHERE  journal_entry.id = TRANSACTION.id
			   AND TRANSACTION.company_id = cid;

		DELETE FROM location
		WHERE  company_id = cid;

		DELETE FROM measurement
		WHERE  company_id = cid;

		DELETE FROM pay_expense
		USING  account
		WHERE  account_id = account.id
			   AND account.company_id = cid;

		DELETE FROM payee_address
		USING  payee
		WHERE  payee_address.payee_id = payee.id
			   AND payee.company_id = cid;

		DELETE FROM payee_contact
		USING  payee
		WHERE  payee_contact.payee_id = payee.id
			   AND payee.company_id = cid;

		DELETE FROM payee_customfields
		USING  payee
		WHERE  payee_customfields.payee_id = payee.id
			   AND payee.company_id = cid;

		DELETE FROM paymentterms
		WHERE  company_id = cid;

		DELETE FROM pricelevel
		WHERE  company_id = cid;

		DELETE FROM purchase_order
		USING  TRANSACTION
		WHERE  purchase_order.id = TRANSACTION.id
			   AND TRANSACTION.company_id = cid;

		DELETE FROM recurring_transaction
		WHERE  company_id = cid;

		DELETE FROM reminder
		WHERE  company_id = cid;

		DELETE FROM sales_person
		WHERE  company_id = cid;

		DELETE FROM shippingmethod
		WHERE  company_id = cid;

		DELETE FROM shippingterms
		WHERE  company_id = cid;

		DELETE FROM stock_adjustment
		WHERE  company_id = cid;
		
		DELETE FROM build_assembly
		USING  TRANSACTION
		WHERE  build_assembly.id = TRANSACTION.id
		       AND TRANSACTION.company_id = cid; 

		DELETE FROM inventory_assembly_item
		USING  item
		WHERE  inventory_assembly_item_id = item.id
			   AND item.company_id = cid;

		DELETE FROM inventory_assembly
		USING  item
		WHERE  inventory_assembly.id = item.id
			   AND item.company_id = cid;

		DELETE FROM item
		WHERE  company_id = cid;


		DELETE FROM stock_transfer_item
		WHERE  company_id = cid;

		DELETE FROM tax_adjustment
		USING  TRANSACTION
		WHERE  tax_adjustment.id = TRANSACTION.id
			   AND TRANSACTION.company_id = cid;

		DELETE FROM tax_code
		WHERE  company_id = cid;

		DELETE FROM tax_group_tax_item
		USING  tax_group
		WHERE  tax_group_id = tax_group.id
			   AND tax_group.company_id = cid;

		DELETE FROM tax_group
		WHERE  company_id = cid;

		DELETE FROM tax_item
		WHERE  company_id = cid;

		DELETE FROM tax_item_groups
		WHERE  company_id = cid;

		DELETE FROM tax_rate_calculation
		USING  TRANSACTION
		WHERE  transaction_id = TRANSACTION.id
			   AND TRANSACTION.company_id = cid;

		DELETE FROM transaction_pay_tax
		USING  tax_return,
			   TRANSACTION
		WHERE  tax_return.id = transaction_pay_tax.vat_return_id
			   AND TRANSACTION.id = tax_return.id
			   AND TRANSACTION.company_id = cid;

		DELETE FROM tax_return
		USING  TRANSACTION
		WHERE  tax_return.id = TRANSACTION.id
			   AND TRANSACTION.company_id = cid;

		DELETE FROM tax_return_entry
		USING  TRANSACTION
		WHERE  tax_return_id = TRANSACTION.id
			   AND TRANSACTION.company_id = cid;

		DELETE FROM taxagency
		WHERE  company_id = cid;

		DELETE FROM taxrates
		WHERE  company_id = cid;

		DELETE FROM transaction_credits_and_payments
		USING  credits_and_payments,
			   TRANSACTION
		WHERE  transaction_credits_and_payments.credits_and_payments_id =
					  credits_and_payments.id
			   AND credits_and_payments.transaction_id = TRANSACTION.id
			   AND TRANSACTION.company_id = cid;
		
		DELETE FROM transaction_credits_and_payments
		USING  transaction_paybill
		WHERE  transaction_credits_and_payments.transaction_paybill_id =
					  transaction_paybill.id
			   AND transaction_paybill.company_id = cid;

		DELETE FROM transaction_credits_and_payments
		USING  transaction_paybill
		WHERE  transaction_credits_and_payments.transaction_paybill_id =
					  transaction_paybill.id
			   AND transaction_paybill.company_id = cid;

		DELETE FROM credits_and_payments
		USING  TRANSACTION
		WHERE  transaction_id = TRANSACTION.id
			   AND TRANSACTION.company_id = cid;

		DELETE FROM transaction_history
		USING  TRANSACTION
		WHERE  transaction_id = TRANSACTION.id
			   AND TRANSACTION.company_id = cid;
			   
		DELETE FROM inventory_purchase
		USING  transaction_item,
		       TRANSACTION
		WHERE  transaction_item_id = transaction_item.id
		       AND TRANSACTION.id = transaction_item.transaction_id
		       AND TRANSACTION.company_id = cid;  

		DELETE FROM transaction_item
		USING  TRANSACTION
		WHERE  transaction_id = TRANSACTION.id
			   AND TRANSACTION.company_id = cid;

		DELETE FROM transaction_deposit_item
		USING  make_deposit,
			   TRANSACTION
		WHERE  transaction_deposit_item.make_deposit_id = make_deposit.id
			   AND TRANSACTION.id = make_deposit.id
			   AND TRANSACTION.company_id = cid;
		
		DELETE FROM deposit_estimates
		USING  TRANSACTION 
		WHERE  deposit_estimates.deposit_id=TRANSACTION.id
			   and TRANSACTION.company_id = cid;
		
		DELETE FROM make_deposit
		USING  TRANSACTION
		WHERE  make_deposit.id = TRANSACTION.id
			   AND TRANSACTION.company_id = cid;

		DELETE FROM transfer_fund
		USING  TRANSACTION
		WHERE  transfer_fund.id = TRANSACTION.id
			   AND TRANSACTION.company_id = cid;

		DELETE FROM transaction_make_deposit_entries
		USING  TRANSACTION
		WHERE  transaction_id = TRANSACTION.id
			   AND TRANSACTION.company_id = cid;

		DELETE FROM transaction_pay_tax
		USING  pay_tax,
			   TRANSACTION
		WHERE  pay_tax_id = pay_tax.id
			   AND TRANSACTION.id = pay_tax.id
			   AND TRANSACTION.company_id = cid;

		DELETE FROM pay_tax
		USING  TRANSACTION
		WHERE  pay_tax.id = TRANSACTION.id
			   AND TRANSACTION.company_id = cid;

		DELETE FROM transaction_paybill
		USING  pay_bill,
			   TRANSACTION
		WHERE  paybill_id = pay_bill.id
			   AND TRANSACTION.id = pay_bill.id
			   AND TRANSACTION.company_id = cid;

		DELETE FROM pay_bill
		USING  TRANSACTION
		WHERE  pay_bill.id = TRANSACTION.id
			   AND TRANSACTION.company_id = cid;
			   
		DELETE FROM vendor_payment
		USING  TRANSACTION
		WHERE  vendor_payment.id = TRANSACTION.id
			   AND TRANSACTION.company_id = cid;

		DELETE FROM transaction_credits_and_payments
		USING  transaction_receive_payment,
			   TRANSACTION
		WHERE  transaction_receive_payment.id =
					  transaction_credits_and_payments.transaction_receive_payment_id
			   AND transaction_receive_payment.transaction_id = TRANSACTION.id
			   AND TRANSACTION.company_id = cid;

		DELETE FROM transaction_receive_payment
		USING  TRANSACTION
		WHERE  transaction_id = TRANSACTION.id
			   AND TRANSACTION.company_id = cid;

		DELETE FROM receive_payment
		USING  TRANSACTION
		WHERE  receive_payment.id = TRANSACTION.id
			   AND TRANSACTION.company_id = cid;

		DELETE FROM transaction_receive_vat
		USING  receive_vat,
			   TRANSACTION
		WHERE  transaction_receive_vat.receive_vat_id = receive_vat.id
			   AND TRANSACTION.id = receive_vat.id
			   AND TRANSACTION.company_id = cid;

		DELETE FROM receive_vat
		USING  TRANSACTION
		WHERE  receive_vat.id = TRANSACTION.id
			   AND TRANSACTION.company_id = cid;
			   
		DELETE FROM tdsdeductormasters
		WHERE  company_id = cid;
		
		DELETE FROM tdsresponsibleperson
		WHERE  company_id = cid;  
		
		DELETE FROM tdstransactionitem
		USING  TRANSACTION
		WHERE  transaction_id = TRANSACTION.id
		       AND TRANSACTION.company_id = cid; 
		       
		DELETE FROM tds_chalan_detail
		USING  TRANSACTION
		WHERE  tds_chalan_detail.id = TRANSACTION.id
		       AND TRANSACTION.company_id = cid;  

		DELETE FROM unit
		WHERE  company_id = cid;

		DELETE FROM unit_of_measure
		WHERE  company_id = cid;

		DELETE FROM nominal_code_range
		WHERE  company_id = cid;

		DELETE FROM vatreturnbox
		WHERE  company_id = cid;

		DELETE FROM vendor
		WHERE  company_id = cid;

		DELETE FROM vendor_credit_memo
		USING  TRANSACTION
		WHERE  vendor_credit_memo.id = TRANSACTION.id
			   AND TRANSACTION.company_id = cid;

		DELETE FROM vendor_group
		WHERE  company_id = cid;

		DELETE FROM warehouse
		WHERE  company_id = cid;
		
		DELETE FROM write_checks_estimates
		USING  TRANSACTION 
		WHERE  write_checks_estimates.write_checks_id=TRANSACTION.id
			   and TRANSACTION.company_id = cid;

		DELETE FROM write_checks
		USING  TRANSACTION
		WHERE  write_checks.id = TRANSACTION.id
			   AND TRANSACTION.company_id = cid;
		
		DELETE FROM job
		WHERE  company_id = cid;

		DELETE FROM customer
		WHERE  company_id = cid;

		DELETE FROM portlet_configuration_portletdata
		USING  portlet_configuration,
			   portlet_page_configuration,
			   users
		WHERE  portlet_configuration.id = portlet_configuration_portletdata.id
			   AND portlet_configuration.portlet_config_id =
				   portlet_page_configuration.id
			   AND portlet_page_configuration.user_id = users.id
			   AND users.company_id = cid;

		DELETE FROM portlet_configuration
		USING  portlet_page_configuration,
			   users
		WHERE  portlet_configuration.portlet_config_id = portlet_page_configuration.id
			   AND portlet_page_configuration.user_id = users.id
			   AND users.company_id = cid;

		DELETE FROM portlet_page_configuration
		USING  users
		WHERE  user_id = users.id
			   AND users.company_id = cid;
	
		DELETE FROM attendance_or_production_items
		USING ATTENDANCE_MANAGEMENT_ITEM AMI
		LEFT JOIN PAYEE E ON AMI.EMPLOYEE =E.ID 
		WHERE E.COMPANY_ID = cid;

		DELETE FROM ATTENDANCE_MANAGEMENT_ITEM
		USING PAYEE E
		WHERE ATTENDANCE_MANAGEMENT_ITEM.EMPLOYEE =E.ID
		AND E.COMPANY_ID = cid;
		
		DELETE FROM PAY_ROLL_UNIT 
		WHERE COMPANY_ID = cid;
		
		DELETE FROM PAY_STRUCTURE_ITEM
		USING PAY_STRUCTURE 
		WHERE PAY_STRUCTURE_ITEM.PAY_STRUCTURE =PAY_STRUCTURE.ID
		AND PAY_STRUCTURE.COMPANY_ID = cid;
		
		DELETE FROM ATTENDANCE_PAYHEAD 
		USING PAY_HEAD PH
		WHERE PH.ID = ATTENDANCE_PAYHEAD.ID
		AND PH.COMPANY_ID =cid;

		DELETE FROM USER_DEFINED_PAY_HEAD 
		USING PAY_HEAD PH
		WHERE PH.ID = USER_DEFINED_PAY_HEAD.ID
		AND PH.COMPANY_ID =cid;
		
		DELETE FROM COMPUTATION_SLABS 
		USING PAY_HEAD PH
		WHERE PH.ID = COMPUTATION_SLABS.PAY_HEAD_ID
		AND PH.COMPANY_ID =cid;

		DELETE FROM FORMULA_FUNCTION
		USING PAY_HEAD PH
		WHERE PH.ID = FORMULA_FUNCTION.PAY_HEAD_ID
		AND PH.COMPANY_ID =cid; 
		
		DELETE FROM COMPUTATION_PAY_HEAD 
		USING PAY_HEAD PH
		WHERE PH.ID = COMPUTATION_PAY_HEAD.ID
		AND PH.COMPANY_ID =cid;

		DELETE FROM FLAT_RATE_PAY_HEAD 
		USING PAY_HEAD PH
		WHERE PH.ID = FLAT_RATE_PAY_HEAD.ID
		AND PH.COMPANY_ID =cid;
		
		DELETE FROM PAY_STRUCTURE_ITEM 
		USING PAY_HEAD PH
		WHERE PH.ID = PAY_STRUCTURE_ITEM.PAY_HEAD
		AND PH.COMPANY_ID =cid;

		DELETE FROM EMPLOYEE_PAYHEAD_COMPONENT
                USING PAY_HEAD PH
		WHERE PH.ID = EMPLOYEE_PAYHEAD_COMPONENT.PAY_HEAD
		AND PH.COMPANY_ID =cid;
		
		DELETE FROM PAY_HEAD 
		WHERE COMPANY_ID = cid; 

		DELETE FROM ATTENDANCE_OR_PRODUCTION_TPE
		WHERE COMPANY_ID = cid;
		
		DELETE FROM PAY_STRUCTURE 
		WHERE COMPANY_ID=cid;
		
		DELETE FROM EMPLOYEE_PAYMENT_DETAILS
		USING PAYEE
		WHERE PAYEE.ID=EMPLOYEE_PAYMENT_DETAILS.EMPLOYEE
		AND PAYEE.COMPANY_ID = cid;
		
		DELETE FROM TRANSACTION_PAY_EMPLOYEE
		WHERE COMPANY_ID = cid;
		
		DELETE FROM PAY_EMPLOYEE
		USING TRANSACTION T
		WHERE PAY_EMPLOYEE.ID = T.ID
		AND T.COMPANY_ID = cid;	
		
		DELETE FROM PAY_RUN
		USING TRANSACTION T
		WHERE PAY_RUN.ID = T.ID
		AND T.company_id = cid;	
		
		DELETE FROM EMPLOYEE
		USING PAYEE P 
		WHERE P.COMPANY_ID = cid AND P.ID = EMPLOYEE.ID;
		
		DELETE FROM EMPLOYEE_GROUP
		WHERE COMPANY_ID = cid;
		
		DELETE FROM account
		WHERE  company_id = cid;
		
		DELETE FROM TRANSACTION
		WHERE  company_id = cid;

		DELETE FROM accounter_class
		WHERE  company_id = cid;

		DELETE FROM payee
		WHERE  company_id = cid;

		DELETE FROM users
		WHERE  company_id = cid;

		DELETE FROM currency
		WHERE  company_id = cid;
		
		DELETE FROM activity
		WHERE  company_id = cid;

		DELETE FROM email_account
		WHERE  company_id = cid;  
		
		DELETE from email_template
		WHERE  company_id = cid;
		       
		DELETE FROM company
		WHERE  id = cid;  

		return
		true;
		end;$$;
 1   DROP FUNCTION public.delete_company(cid bigint);
       public       postgres    false                       1255    28908    lastclientupdate()    FUNCTION     (  CREATE FUNCTION public.lastclientupdate() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
				BEGIN
				IF(TG_OP = 'UPDATE') THEN
						NEW.UPDATE_DATE = (SELECT ( CAST(TO_CHAR((SELECT CURRENT_TIMESTAMP), 'YYYYMMDD')AS  BIGINT)));
						RETURN NEW;
				END IF;
				  RETURN NULL;
				END;
				$$;
 )   DROP FUNCTION public.lastclientupdate();
       public       postgres    false                       1255    28914     nooftransactionpercompanycount()    FUNCTION       CREATE FUNCTION public.nooftransactionpercompanycount() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
				 DECLARE
					  totalTransactions bigint;
					  companyId  bigint;
				 BEGIN
				 IF(TG_OP = 'INSERT') THEN
					 companyId=NEW.COMPANY_ID;
					 totalTransactions = (SELECT COUNT(*) FROM TRANSACTION WHERE COMPANY_ID=companyId);
					 UPDATE COMPANY SET TRANSACTIONS_COUNT= totalTransactions WHERE ID=companyId;
                     RETURN NEW;
                  END IF;
                 RETURN NULL;
				END;
				$$;
 7   DROP FUNCTION public.nooftransactionpercompanycount();
       public       postgres    false                       1255    28912    noofuserspercompanycount()    FUNCTION     ?  CREATE FUNCTION public.noofuserspercompanycount() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
				 DECLARE
				  totalCompanies bigint;
				   companyId  bigint;
				BEGIN
				 IF(TG_OP = 'INSERT') THEN
				     companyId=NEW.COMPANY_ID;
					 totalCompanies = (SELECT COUNT(*) FROM USERS WHERE COMPANY_ID=companyId);
					 UPDATE COMPANY SET NUMBER_OF_USERS= totalCompanies WHERE ID=companyId;
					 RETURN NEW;
			     END IF;
                 RETURN NULL;
				END;
				$$;
 1   DROP FUNCTION public.noofuserspercompanycount();
       public       postgres    false            
           1255    28904    transactionscreatedcount()    FUNCTION     ?  CREATE FUNCTION public.transactionscreatedcount() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
	DECLARE
		       userId         bigint;
		       clientId       bigint;
	BEGIN
	    userId=NEW.CREATED_BY;
		clientId=(select client_id from users u where u.id=userId);
        IF (TG_OP ='INSERT') THEN
           UPDATE CLIENT SET TRANSACTIONS_CREATED=TRANSACTIONS_CREATED +1  WHERE id = clientId;
		   RETURN NEW;
        END IF;
           RETURN NULL;
	END;
		$$;
 1   DROP FUNCTION public.transactionscreatedcount();
       public       postgres    false                       1255    28920    transactionsupdatecount()    FUNCTION     ?  CREATE FUNCTION public.transactionsupdatecount() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
	DECLARE
		       userId         bigint;
		       clientId       bigint;
	BEGIN
	     userId=NEW.CREATED_BY;
		 clientId=(select client_id from users u where u.id=userId);
        IF (TG_OP ='UPDATE') THEN
         UPDATE CLIENT SET TRANSACTIONS_UPDATE=TRANSACTIONS_UPDATE +1  WHERE id = clientId;
		 RETURN NEW;
        END IF;
         RETURN NULL;
	END;
		$$;
 0   DROP FUNCTION public.transactionsupdatecount();
       public       postgres    false                       1255    28918    userinsert()    FUNCTION     \  CREATE FUNCTION public.userinsert() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
				DECLARE
					  clientid      bigint;
				BEGIN
				clientid=NEW.CLIENT_ID;
				UPDATE CLIENT CL SET PREMIUM_COMPANIES= (SELECT COUNT(*) FROM COMPANY C LEFT JOIN USERS U ON U.ID=C.CREATED_BY LEFT JOIN CLIENT CL2 ON CL2.ID=U.CLIENT_ID LEFT JOIN CLIENT_SUBSCRIPTION CS ON CL2.CLIENT_SUBSCRIPTION=CS.ID LEFT JOIN SUBSCRIPTION S ON S.ID=CS.SUBSCRIPTION_ID WHERE S.TYPE=3 AND C.ID IN (SELECT COMPANY_ID FROM USERS U2 WHERE U2.CLIENT_ID=CL.ID AND U2.IS_DELETED=FALSE))WHERE CL.ID=clientid;
				 RETURN NEW;
				END;
				$$;
 #   DROP FUNCTION public.userinsert();
       public       postgres    false            ?            1259    24578    account    TABLE     	  CREATE TABLE public.account (
    id bigint NOT NULL,
    company_id bigint,
    a_type integer,
    base_type integer,
    sub_base_type integer,
    group_type integer,
    a_number bytea,
    name bytea NOT NULL,
    is_active boolean,
    parent_id bigint,
    cashflow_category integer,
    opening_balance double precision,
    as_of bigint,
    is_cash_account boolean,
    comment bytea,
    credit_limit double precision,
    card_number bytea,
    is_increase boolean,
    current_balance double precision,
    total_balance_in_account_currency double precision,
    total_balance double precision,
    statement_balance double precision,
    statement_imported_date bigint,
    is_opening_balance_editable boolean,
    hierarchy character varying(255),
    version integer,
    flow bytea,
    paypal_email bytea,
    last_check_number bytea,
    account_currency bigint NOT NULL,
    currency_factor double precision,
    created_by bigint NOT NULL,
    last_modifier bigint NOT NULL,
    created_date timestamp without time zone NOT NULL,
    last_modified_date timestamp without time zone NOT NULL,
    linked_id bigint,
    is_default boolean,
    box_number integer,
    paypaltoken character varying(255),
    paypalsecretkey character varying(255),
    enddate bigint
);
    DROP TABLE public.account;
       public         postgres    false            ?            1259    24598    account_amounts    TABLE     ?   CREATE TABLE public.account_amounts (
    account_id bigint NOT NULL,
    amount double precision,
    month integer NOT NULL
);
 #   DROP TABLE public.account_amounts;
       public         postgres    false            ?            1259    24576    account_id_seq    SEQUENCE     w   CREATE SEQUENCE public.account_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 %   DROP SEQUENCE public.account_id_seq;
       public       postgres    false    197            ?           0    0    account_id_seq    SEQUENCE OWNED BY     A   ALTER SEQUENCE public.account_id_seq OWNED BY public.account.id;
            public       postgres    false    196            ?            1259    24605    account_transaction    TABLE     ?  CREATE TABLE public.account_transaction (
    id bigint NOT NULL,
    company_id bigint NOT NULL,
    transaction_id bigint,
    account_id bigint,
    amount double precision,
    created_by bigint NOT NULL,
    last_modifier bigint NOT NULL,
    created_date timestamp without time zone NOT NULL,
    last_modified_date timestamp without time zone NOT NULL,
    update_account boolean
);
 '   DROP TABLE public.account_transaction;
       public         postgres    false            ?            1259    24603    account_transaction_id_seq    SEQUENCE     ?   CREATE SEQUENCE public.account_transaction_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 1   DROP SEQUENCE public.account_transaction_id_seq;
       public       postgres    false    202            ?           0    0    account_transaction_id_seq    SEQUENCE OWNED BY     Y   ALTER SEQUENCE public.account_transaction_id_seq OWNED BY public.account_transaction.id;
            public       postgres    false    201            ?            1259    24589    accounter_class    TABLE     ?  CREATE TABLE public.accounter_class (
    id bigint NOT NULL,
    company_id bigint,
    class_name bytea NOT NULL,
    path character varying(255),
    parent_id bigint,
    version integer,
    parent_count integer,
    created_by bigint NOT NULL,
    last_modifier bigint NOT NULL,
    created_date timestamp without time zone NOT NULL,
    last_modified_date timestamp without time zone NOT NULL
);
 #   DROP TABLE public.accounter_class;
       public         postgres    false            ?            1259    24587    accounter_class_id_seq    SEQUENCE        CREATE SEQUENCE public.accounter_class_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 -   DROP SEQUENCE public.accounter_class_id_seq;
       public       postgres    false    199            ?           0    0    accounter_class_id_seq    SEQUENCE OWNED BY     Q   ALTER SEQUENCE public.accounter_class_id_seq OWNED BY public.accounter_class.id;
            public       postgres    false    198            ?            1259    24613 
   activation    TABLE     ?   CREATE TABLE public.activation (
    id bigint NOT NULL,
    email_id character varying(255),
    token character varying(255),
    sign_up_date timestamp without time zone
);
    DROP TABLE public.activation;
       public         postgres    false            ?            1259    24611    activation_id_seq    SEQUENCE     z   CREATE SEQUENCE public.activation_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 (   DROP SEQUENCE public.activation_id_seq;
       public       postgres    false    204            ?           0    0    activation_id_seq    SEQUENCE OWNED BY     G   ALTER SEQUENCE public.activation_id_seq OWNED BY public.activation.id;
            public       postgres    false    203            ?            1259    24624    activity    TABLE     ?  CREATE TABLE public.activity (
    id bigint NOT NULL,
    company_id bigint NOT NULL,
    object_id bigint,
    time_stamp timestamp without time zone,
    user_name bytea,
    activity_type integer,
    object_type integer,
    name bytea,
    transaction_date bigint,
    object_status integer,
    amount double precision,
    data_type bytea,
    description bytea,
    currency character varying(255),
    audithistory text
);
    DROP TABLE public.activity;
       public         postgres    false            ?            1259    24622    activity_id_seq    SEQUENCE     x   CREATE SEQUENCE public.activity_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 &   DROP SEQUENCE public.activity_id_seq;
       public       postgres    false    206            ?           0    0    activity_id_seq    SEQUENCE OWNED BY     C   ALTER SEQUENCE public.activity_id_seq OWNED BY public.activity.id;
            public       postgres    false    205            ?            1259    24635    adjustment_reason    TABLE     <  CREATE TABLE public.adjustment_reason (
    id bigint NOT NULL,
    company_id bigint NOT NULL,
    section bytea,
    name bytea,
    created_by bigint NOT NULL,
    last_modifier bigint NOT NULL,
    created_date timestamp without time zone NOT NULL,
    last_modified_date timestamp without time zone NOT NULL
);
 %   DROP TABLE public.adjustment_reason;
       public         postgres    false            ?            1259    24633    adjustment_reason_id_seq    SEQUENCE     ?   CREATE SEQUENCE public.adjustment_reason_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 /   DROP SEQUENCE public.adjustment_reason_id_seq;
       public       postgres    false    208            ?           0    0    adjustment_reason_id_seq    SEQUENCE OWNED BY     U   ALTER SEQUENCE public.adjustment_reason_id_seq OWNED BY public.adjustment_reason.id;
            public       postgres    false    207            ?            1259    24646    admin_templates    TABLE     ?   CREATE TABLE public.admin_templates (
    id bigint NOT NULL,
    name character varying(255),
    subject text,
    body text
);
 #   DROP TABLE public.admin_templates;
       public         postgres    false            ?            1259    24644    admin_templates_id_seq    SEQUENCE        CREATE SEQUENCE public.admin_templates_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 -   DROP SEQUENCE public.admin_templates_id_seq;
       public       postgres    false    210            ?           0    0    admin_templates_id_seq    SEQUENCE OWNED BY     Q   ALTER SEQUENCE public.admin_templates_id_seq OWNED BY public.admin_templates.id;
            public       postgres    false    209            ?            1259    24657 
   admin_user    TABLE       CREATE TABLE public.admin_user (
    id bigint NOT NULL,
    name character varying(255),
    email_id character varying(255),
    password character varying(255),
    type_of_user integer,
    status character varying(255),
    is_permissions_given boolean
);
    DROP TABLE public.admin_user;
       public         postgres    false            ?            1259    24655    admin_user_id_seq    SEQUENCE     z   CREATE SEQUENCE public.admin_user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 (   DROP SEQUENCE public.admin_user_id_seq;
       public       postgres    false    212            ?           0    0    admin_user_id_seq    SEQUENCE OWNED BY     G   ALTER SEQUENCE public.admin_user_id_seq OWNED BY public.admin_user.id;
            public       postgres    false    211            ?            1259    24668    advertisement    TABLE     ?   CREATE TABLE public.advertisement (
    id bigint NOT NULL,
    url character varying(255),
    height double precision,
    width double precision
);
 !   DROP TABLE public.advertisement;
       public         postgres    false            ?            1259    24666    advertisement_id_seq    SEQUENCE     }   CREATE SEQUENCE public.advertisement_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 +   DROP SEQUENCE public.advertisement_id_seq;
       public       postgres    false    214            ?           0    0    advertisement_id_seq    SEQUENCE OWNED BY     M   ALTER SEQUENCE public.advertisement_id_seq OWNED BY public.advertisement.id;
            public       postgres    false    213            ?            1259    24676    attachments    TABLE     ?  CREATE TABLE public.attachments (
    id bigint NOT NULL,
    attachment_id character varying(255) NOT NULL,
    name bytea NOT NULL,
    size bigint NOT NULL,
    company_id bigint,
    version integer,
    created_by bigint NOT NULL,
    last_modifier bigint NOT NULL,
    created_date timestamp without time zone NOT NULL,
    last_modified_date timestamp without time zone NOT NULL,
    transaction_id bigint NOT NULL
);
    DROP TABLE public.attachments;
       public         postgres    false            ?            1259    24674    attachments_id_seq    SEQUENCE     {   CREATE SEQUENCE public.attachments_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 )   DROP SEQUENCE public.attachments_id_seq;
       public       postgres    false    216            ?           0    0    attachments_id_seq    SEQUENCE OWNED BY     I   ALTER SEQUENCE public.attachments_id_seq OWNED BY public.attachments.id;
            public       postgres    false    215            ?            1259    24687    attendance_management_item    TABLE     ?   CREATE TABLE public.attendance_management_item (
    id bigint NOT NULL,
    employee bigint,
    abscent_days double precision,
    attendance_management bigint,
    idx integer
);
 .   DROP TABLE public.attendance_management_item;
       public         postgres    false            ?            1259    24685 !   attendance_management_item_id_seq    SEQUENCE     ?   CREATE SEQUENCE public.attendance_management_item_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 8   DROP SEQUENCE public.attendance_management_item_id_seq;
       public       postgres    false    218            ?           0    0 !   attendance_management_item_id_seq    SEQUENCE OWNED BY     g   ALTER SEQUENCE public.attendance_management_item_id_seq OWNED BY public.attendance_management_item.id;
            public       postgres    false    217            ?            1259    24693    attendance_or_production_items    TABLE     ?   CREATE TABLE public.attendance_or_production_items (
    attendance_management_item_id bigint NOT NULL,
    attendance_type bigint,
    value double precision,
    attendance_or_production_items_index integer NOT NULL
);
 2   DROP TABLE public.attendance_or_production_items;
       public         postgres    false            ?            1259    24700    attendance_or_production_tpe    TABLE     g  CREATE TABLE public.attendance_or_production_tpe (
    id bigint NOT NULL,
    company_id bigint,
    type integer,
    name bytea,
    unit bigint,
    period_type integer,
    created_by bigint NOT NULL,
    last_modifier bigint NOT NULL,
    created_date timestamp without time zone NOT NULL,
    last_modified_date timestamp without time zone NOT NULL
);
 0   DROP TABLE public.attendance_or_production_tpe;
       public         postgres    false            ?            1259    24698 #   attendance_or_production_tpe_id_seq    SEQUENCE     ?   CREATE SEQUENCE public.attendance_or_production_tpe_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 :   DROP SEQUENCE public.attendance_or_production_tpe_id_seq;
       public       postgres    false    221            ?           0    0 #   attendance_or_production_tpe_id_seq    SEQUENCE OWNED BY     k   ALTER SEQUENCE public.attendance_or_production_tpe_id_seq OWNED BY public.attendance_or_production_tpe.id;
            public       postgres    false    220            ?            1259    24709    attendance_payhead    TABLE     ?   CREATE TABLE public.attendance_payhead (
    id bigint NOT NULL,
    attendance_type integer,
    pay_head bigint,
    calculation_period integer,
    per_day_calculation_basis integer,
    production_type bigint
);
 &   DROP TABLE public.attendance_payhead;
       public         postgres    false            ?            1259    24716    bank    TABLE     ?   CREATE TABLE public.bank (
    id bigint NOT NULL,
    company_id bigint NOT NULL,
    name bytea NOT NULL,
    version integer
);
    DROP TABLE public.bank;
       public         postgres    false            ?            1259    24725    bank_account    TABLE     ?   CREATE TABLE public.bank_account (
    id bigint NOT NULL,
    bank_id bigint,
    bank_account_type integer,
    bank_account_number bytea,
    last_reconcilation_date bigint
);
     DROP TABLE public.bank_account;
       public         postgres    false            ?            1259    24714    bank_id_seq    SEQUENCE     t   CREATE SEQUENCE public.bank_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 "   DROP SEQUENCE public.bank_id_seq;
       public       postgres    false    224            ?           0    0    bank_id_seq    SEQUENCE OWNED BY     ;   ALTER SEQUENCE public.bank_id_seq OWNED BY public.bank.id;
            public       postgres    false    223            ?            1259    24735    branding_theme    TABLE     ?  CREATE TABLE public.branding_theme (
    id bigint NOT NULL,
    company_id bigint NOT NULL,
    theme_name bytea NOT NULL,
    page_size_type integer,
    top_margin double precision,
    bottom_margin double precision,
    margin_measurement_type integer,
    address_padding double precision,
    font bytea,
    font_size bytea,
    overdue_invoice_title bytea,
    credit_memo_title bytea,
    statement_title bytea,
    quote_title bytea,
    is_show_tax_number boolean,
    is_show_column_heading boolean,
    is_show_price_and_quantity boolean,
    file_name bytea,
    is_show_tax_column boolean,
    is_show_registered_address boolean,
    is_show_logo boolean,
    is_logo_added boolean,
    is_custom_file boolean,
    paypal_email_id bytea,
    logo_alignment_type integer,
    contact_details bytea,
    terms_and_payment_advice bytea,
    created_by bigint NOT NULL,
    last_modifier bigint NOT NULL,
    created_date timestamp without time zone NOT NULL,
    last_modified_date timestamp without time zone NOT NULL,
    is_default boolean,
    version integer,
    invoice_templete_name bytea NOT NULL,
    creditnote_templete_name bytea NOT NULL,
    quote_templete_name bytea NOT NULL,
    cashsale_templete_name bytea NOT NULL,
    cashsale_title bytea,
    purchaseorder_templete_name bytea NOT NULL,
    purchaseorder_title bytea,
    salesorder_templete_name bytea NOT NULL,
    salesorder_title bytea
);
 "   DROP TABLE public.branding_theme;
       public         postgres    false            ?            1259    24733    branding_theme_id_seq    SEQUENCE     ~   CREATE SEQUENCE public.branding_theme_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 ,   DROP SEQUENCE public.branding_theme_id_seq;
       public       postgres    false    227            ?           0    0    branding_theme_id_seq    SEQUENCE OWNED BY     O   ALTER SEQUENCE public.branding_theme_id_seq OWNED BY public.branding_theme.id;
            public       postgres    false    226            ?            1259    24746    budget    TABLE     ?   CREATE TABLE public.budget (
    id bigint NOT NULL,
    company_id bigint NOT NULL,
    budget_name bytea,
    financial_year character varying(255)
);
    DROP TABLE public.budget;
       public         postgres    false            ?            1259    24744    budget_id_seq    SEQUENCE     v   CREATE SEQUENCE public.budget_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 $   DROP SEQUENCE public.budget_id_seq;
       public       postgres    false    229            ?           0    0    budget_id_seq    SEQUENCE OWNED BY     ?   ALTER SEQUENCE public.budget_id_seq OWNED BY public.budget.id;
            public       postgres    false    228            ?            1259    24757 
   budgetitem    TABLE     k  CREATE TABLE public.budgetitem (
    id bigint NOT NULL,
    company_id bigint NOT NULL,
    account bigint,
    january_amount double precision,
    february_amount double precision,
    march_amount double precision,
    april_amount double precision,
    may_amount double precision,
    june_amount double precision,
    july_amount double precision,
    august_amount double precision,
    spetember_amount double precision,
    october_amount double precision,
    november_amount double precision,
    december_amount double precision,
    total_amount double precision,
    budget_id bigint,
    idx integer
);
    DROP TABLE public.budgetitem;
       public         postgres    false            ?            1259    24755    budgetitem_id_seq    SEQUENCE     z   CREATE SEQUENCE public.budgetitem_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 (   DROP SEQUENCE public.budgetitem_id_seq;
       public       postgres    false    231            ?           0    0    budgetitem_id_seq    SEQUENCE OWNED BY     G   ALTER SEQUENCE public.budgetitem_id_seq OWNED BY public.budgetitem.id;
            public       postgres    false    230            ?            1259    24763    build_assembly    TABLE     ?   CREATE TABLE public.build_assembly (
    id bigint NOT NULL,
    inventory_assembly bigint,
    quantity_to_build double precision
);
 "   DROP TABLE public.build_assembly;
       public         postgres    false            ?            1259    24768    cash_purchase    TABLE     ?  CREATE TABLE public.cash_purchase (
    id bigint NOT NULL,
    vendor_id bigint,
    cp_is_primary boolean,
    cp_name bytea,
    cp_title bytea,
    cp_business_phone bytea,
    cp_email bytea,
    vendor_address_type integer,
    vendor_address_address1 bytea,
    vendor_address_street bytea,
    vendor_address_city bytea,
    vendor_address_state bytea,
    vendor_address_zip bytea,
    vendor_address_country bytea,
    vendor_address_is_selected boolean,
    phone bytea,
    payfrom_account_id bigint,
    cash_expense_account_id bigint,
    employee bigint,
    check_number bytea,
    delivery_date bigint,
    expense_status integer
);
 !   DROP TABLE public.cash_purchase;
       public         postgres    false            ?            1259    24776    cash_purchase_estimates    TABLE     o   CREATE TABLE public.cash_purchase_estimates (
    cash_purchase_id bigint NOT NULL,
    elt bigint NOT NULL
);
 +   DROP TABLE public.cash_purchase_estimates;
       public         postgres    false            ?            1259    24781    cash_purchase_orders    TABLE     ?   CREATE TABLE public.cash_purchase_orders (
    cash_purchase_id bigint NOT NULL,
    purchase_order_id bigint NOT NULL,
    vtx integer NOT NULL
);
 (   DROP TABLE public.cash_purchase_orders;
       public         postgres    false            ?            1259    24794    cash_sale_orders    TABLE     ?   CREATE TABLE public.cash_sale_orders (
    cashsale_id bigint NOT NULL,
    estimate_id bigint NOT NULL,
    vtx integer NOT NULL
);
 $   DROP TABLE public.cash_sale_orders;
       public         postgres    false            ?            1259    24786 
   cash_sales    TABLE     )  CREATE TABLE public.cash_sales (
    id bigint NOT NULL,
    customer_id bigint,
    cs_is_primary boolean,
    cs_name bytea,
    cs_title bytea,
    cs_business_phone bytea,
    cs_email bytea,
    billing_address_type integer,
    billing_address1 bytea,
    billing_address_street bytea,
    billing_address_city bytea,
    billing_address_state bytea,
    billing_address_zip bytea,
    billing_address_country bytea,
    billing_address_is_selected boolean,
    shipping_address_type integer,
    shipping_address1 bytea,
    shipping_address_street bytea,
    shipping_address_city bytea,
    shipping_address_state bytea,
    shipping_address_zip bytea,
    shipping_address_country bytea,
    shipping_address_is_selected boolean,
    phone bytea,
    check_number bytea,
    sales_person_id bigint,
    deposit_in_account_id bigint,
    shipping_terms_id bigint,
    shipping_method_id bigint,
    delivery_date bigint,
    price_level_id bigint,
    sales_tax double precision,
    discount_total double precision,
    rounding_total double precision
);
    DROP TABLE public.cash_sales;
       public         postgres    false            ?            1259    24801    cheque_layout    TABLE     ?  CREATE TABLE public.cheque_layout (
    id bigint NOT NULL,
    company_id bigint NOT NULL,
    created_by bigint NOT NULL,
    last_modifier bigint NOT NULL,
    created_date timestamp without time zone NOT NULL,
    last_modified_date timestamp without time zone NOT NULL,
    version integer,
    account_id bigint,
    authorised_signature bytea,
    cheque_height double precision,
    cheque_width double precision,
    payeename_top double precision,
    payeename_left double precision,
    payeename_width double precision,
    amount_words_line1_top double precision,
    amount_words_line1_left double precision,
    amount_words_line1_width double precision,
    amount_words_line2_top double precision,
    amount_words_line2_left double precision,
    amount_words_line2_width double precision,
    amount_fig_top double precision,
    amount_fig_left double precision,
    amount_fig_width double precision,
    cheque_date_top double precision,
    cheque_date_left double precision,
    cheque_date_width double precision,
    company_name_top double precision,
    company_name_left double precision,
    company_name_width double precision,
    signatory_top double precision,
    signatory_left double precision,
    signatory_width double precision
);
 !   DROP TABLE public.cheque_layout;
       public         postgres    false            ?            1259    24799    cheque_layout_id_seq    SEQUENCE     }   CREATE SEQUENCE public.cheque_layout_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 +   DROP SEQUENCE public.cheque_layout_id_seq;
       public       postgres    false    239            ?           0    0    cheque_layout_id_seq    SEQUENCE OWNED BY     M   ALTER SEQUENCE public.cheque_layout_id_seq OWNED BY public.cheque_layout.id;
            public       postgres    false    238            ?            1259    24812    client    TABLE     =  CREATE TABLE public.client (
    id bigint NOT NULL,
    first_name character varying(255),
    last_name character varying(255),
    full_name character varying(255),
    email_id character varying(255),
    password character varying(255),
    password_reovery_key bytea,
    is_active boolean,
    phone_number character varying(255),
    country character varying(255),
    is_subscribed_to_news_letter boolean,
    is_require_password_reset boolean,
    login_count integer,
    last_login_time bigint,
    is_deleted boolean,
    is_email_bounced boolean,
    created_date bigint,
    transactions_created integer,
    transactions_update integer,
    companies integer,
    update_date bigint,
    opened_companies integer,
    changed_password integer,
    transaction_created_from_device integer,
    transaction_update_from_device integer,
    companies_count_from_device integer,
    loggedin_count_from_device integer,
    premium_companies integer,
    subscription_type integer,
    is_premium_done boolean,
    client_subscription bigint,
    license_purchase bigint
);
    DROP TABLE public.client;
       public         postgres    false            ?            1259    24810    client_id_seq    SEQUENCE     v   CREATE SEQUENCE public.client_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 $   DROP SEQUENCE public.client_id_seq;
       public       postgres    false    241            ?           0    0    client_id_seq    SEQUENCE OWNED BY     ?   ALTER SEQUENCE public.client_id_seq OWNED BY public.client.id;
            public       postgres    false    240            ?            1259    24823    client_languages    TABLE     {   CREATE TABLE public.client_languages (
    client_id bigint NOT NULL,
    language_code character varying(255) NOT NULL
);
 $   DROP TABLE public.client_languages;
       public         postgres    false            ?            1259    24830    client_paypal_details    TABLE     ?  CREATE TABLE public.client_paypal_details (
    id bigint NOT NULL,
    first_name character varying(255),
    last_name character varying(255),
    payment_status character varying(255),
    payer_email character varying(255),
    payment_gross double precision,
    mc_currency character varying(255),
    address_country character varying(255),
    client_emailid character varying(255),
    mc_gross character varying(255),
    protection_eligibility character varying(255),
    payer_id character varying(255),
    payment_date character varying(255),
    pdt_payment character varying(255),
    charset character varying(255),
    nooption_selection character varying(255),
    nmc_fee character varying(255),
    notify_version character varying(255),
    subscriber_id character varying(255),
    custom character varying(255),
    payer_status character varying(255),
    business character varying(255),
    verify_sign character varying(255),
    option_name character varying(255),
    txn_id character varying(255),
    payment_type character varying(255),
    payer_business_name character varying(255),
    btn_id character varying(255),
    receiver_email character varying(255),
    payment_fee character varying(255),
    receiver_id character varying(255),
    txn_type character varying(255),
    item_name character varying(255),
    item_number character varying(255),
    resident_country character varying(255),
    transaction_subject character varying(255),
    ipn_trackid character varying(255)
);
 )   DROP TABLE public.client_paypal_details;
       public         postgres    false            ?            1259    24828    client_paypal_details_id_seq    SEQUENCE     ?   CREATE SEQUENCE public.client_paypal_details_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 3   DROP SEQUENCE public.client_paypal_details_id_seq;
       public       postgres    false    244            ?           0    0    client_paypal_details_id_seq    SEQUENCE OWNED BY     ]   ALTER SEQUENCE public.client_paypal_details_id_seq OWNED BY public.client_paypal_details.id;
            public       postgres    false    243            ?            1259    24841    client_subscription    TABLE     ?  CREATE TABLE public.client_subscription (
    id bigint NOT NULL,
    subscription_id bigint NOT NULL,
    last_modified timestamp without time zone,
    created_date timestamp without time zone,
    expired_date timestamp without time zone,
    graceperiod_date timestamp without time zone,
    premium_type integer,
    duration_type integer,
    paypal_profile_id character varying(255)
);
 '   DROP TABLE public.client_subscription;
       public         postgres    false            ?            1259    24839    client_subscription_id_seq    SEQUENCE     ?   CREATE SEQUENCE public.client_subscription_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 1   DROP SEQUENCE public.client_subscription_id_seq;
       public       postgres    false    246            ?           0    0    client_subscription_id_seq    SEQUENCE OWNED BY     Y   ALTER SEQUENCE public.client_subscription_id_seq OWNED BY public.client_subscription.id;
            public       postgres    false    245            ?            1259    24849    commodity_code    TABLE     X   CREATE TABLE public.commodity_code (
    id bigint NOT NULL,
    name bytea NOT NULL
);
 "   DROP TABLE public.commodity_code;
       public         postgres    false            ?            1259    24847    commodity_code_id_seq    SEQUENCE     ~   CREATE SEQUENCE public.commodity_code_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 ,   DROP SEQUENCE public.commodity_code_id_seq;
       public       postgres    false    248            ?           0    0    commodity_code_id_seq    SEQUENCE OWNED BY     O   ALTER SEQUENCE public.commodity_code_id_seq OWNED BY public.commodity_code.id;
            public       postgres    false    247            ?            1259    24860    company    TABLE     f  CREATE TABLE public.company (
    id bigint NOT NULL,
    is_configured boolean,
    created_date timestamp without time zone,
    created_by bigint,
    company_email_for_customers bytea,
    encryption_key character varying(255),
    encrypted_password bytea,
    password_recovery_key bytea,
    transaction_limit integer,
    contact_support boolean,
    secret_key bytea,
    is_locked boolean,
    is_deleted boolean,
    ein character varying(255),
    firstmonth_of_fiscalyear integer,
    firstmonth_of_incometaxyear integer,
    tax_form integer,
    books_colsing_date bigint,
    closing_date_warningtype integer,
    enable_account_numbers boolean,
    customer_type integer,
    enable_autorecall boolean,
    restart_setup_interviews boolean,
    fiscal_year_starting integer,
    industry integer,
    book_keeping boolean,
    number_of_users integer,
    transactions_count integer,
    class_count integer,
    trading_name character varying(255) NOT NULL,
    timezone character varying(255),
    company_email bytea,
    decimal_char character varying(255),
    digit_group_char character varying(255),
    digit_grouping_format character varying(255),
    currency_format character varying(255),
    tax_id character varying(255),
    legalname character varying(255),
    phone bytea,
    active_inventory_scheme integer,
    fax bytea,
    website bytea,
    show_legal_name boolean,
    show_registered_address boolean,
    decimal_number integer,
    trading_address_type integer,
    trading_address1 bytea NOT NULL,
    trading_street bytea NOT NULL,
    trading_city bytea NOT NULL,
    trading_state bytea NOT NULL,
    trading_zip bytea NOT NULL,
    trading_country character varying(100) NOT NULL,
    trading_is_selected boolean NOT NULL,
    cp_log_space_used double precision NOT NULL,
    cp_start_of_fiscal_year bigint NOT NULL,
    cp_end_of_fiscal_year bigint NOT NULL,
    cp_fiscal_year_start_date bigint NOT NULL,
    cp_depreciation_start_date bigint NOT NULL,
    cp_prevent_posting_date bigint,
    date_format character varying(255) NOT NULL,
    cp_default_shipping_term bigint,
    cp_default_annual_interest_rate integer NOT NULL,
    cp_default_minimum_finance_charge double precision NOT NULL,
    cp_grace_days integer NOT NULL,
    vat_registration_number bytea NOT NULL,
    vat_reporting_period integer NOT NULL,
    ending_period_for_vat_reporting integer NOT NULL,
    vat_tax_agency_name bytea NOT NULL,
    cp_flags bigint NOT NULL,
    cp_fiscal_year_first_month integer,
    cp_industry_type integer,
    cp_organization_type integer,
    cp_refer_customers integer,
    cp_refer_vendors integer,
    location_tracking_id bigint,
    default_tax_code bigint,
    primary_currency bigint,
    cp_rounding_method integer,
    cp_rounding_limit double precision,
    cp_rounding_account bigint,
    password_hint character varying(255),
    bank_account_no bytea,
    sort_code bytea,
    item_count integer,
    cash_discount_account bigint,
    accounts_receivable_id bigint,
    accounts_round_id bigint,
    accounts_payable_id bigint,
    opening_balances_id bigint,
    retained_earnings_id bigint,
    other_cash_income_id bigint,
    other_cash_expense_id bigint,
    cash_discounts_given bigint,
    cash_discounts_taken bigint,
    tax_liability_account_id bigint,
    vat_filed_liability_account_id bigint,
    pending_item_receipts_account_id bigint,
    exchange_loss_or_gain_account bigint,
    cost_of_goods_sold bigint,
    salaries_payable_account bigint,
    tds_deductor bigint,
    tds_responsible_person bigint,
    version integer,
    service_item_default_income_account bytea,
    service_item_default_expense_account bytea,
    non_inventory_item_default_income_account bytea,
    non_inventory_item_default_expense_account bytea,
    uk_service_item_default_income_account bytea,
    uk_service_item_default_expense_account bytea,
    uk_non_inventory_item_default_income_account bytea,
    uk_non_inventory_item_default_expense_account bytea,
    registration_number bytea,
    default_measurement bigint,
    default_warehouse bigint,
    registred_address_type integer,
    registred_address1 bytea NOT NULL,
    registred_street bytea NOT NULL,
    registred_city bytea NOT NULL,
    registred_state bytea NOT NULL,
    registred_zip bytea NOT NULL,
    registred_country bytea NOT NULL,
    registred_is_selected boolean NOT NULL,
    show_payroll_only boolean
);
    DROP TABLE public.company;
       public         postgres    false            ?            1259    24869    company_fields    TABLE     }   CREATE TABLE public.company_fields (
    company_id bigint NOT NULL,
    field_value bytea,
    field_name bytea NOT NULL
);
 "   DROP TABLE public.company_fields;
       public         postgres    false            ?            1259    24858    company_id_seq    SEQUENCE     w   CREATE SEQUENCE public.company_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 %   DROP SEQUENCE public.company_id_seq;
       public       postgres    false    250            ?           0    0    company_id_seq    SEQUENCE OWNED BY     A   ALTER SEQUENCE public.company_id_seq OWNED BY public.company.id;
            public       postgres    false    249            ?            1259    24877    computation_pay_head    TABLE     ?   CREATE TABLE public.computation_pay_head (
    id bigint NOT NULL,
    computation_type integer,
    calculation_period integer
);
 (   DROP TABLE public.computation_pay_head;
       public         postgres    false            ?            1259    24882    computation_slabs    TABLE     ?   CREATE TABLE public.computation_slabs (
    pay_head_id bigint NOT NULL,
    effective_from bigint,
    from_amount double precision,
    to_amount double precision,
    slab_type integer,
    value double precision,
    slab_index integer NOT NULL
);
 %   DROP TABLE public.computation_slabs;
       public         postgres    false                       1259    24910    credit_card_charges    TABLE       CREATE TABLE public.credit_card_charges (
    id bigint NOT NULL,
    vendor_id bigint,
    ccc_is_primary boolean,
    ccc_name bytea,
    ccc_title bytea,
    ccc_business_phone bytea,
    ccc_email bytea,
    vendor_address_type integer,
    vendor_address_street bytea,
    vendor_address_city bytea,
    vendor_address_state bytea,
    vendor_address_zip bytea,
    vendor_address_country bytea,
    vendor_address_is_selected boolean,
    phone bytea,
    payfrom_account_id bigint,
    check_number bytea,
    delivery_date bigint
);
 '   DROP TABLE public.credit_card_charges;
       public         postgres    false            ?            1259    24889    creditrating    TABLE     Z  CREATE TABLE public.creditrating (
    id bigint NOT NULL,
    company_id bigint NOT NULL,
    name bytea NOT NULL,
    version integer,
    created_by bigint NOT NULL,
    last_modifier bigint NOT NULL,
    created_date timestamp without time zone NOT NULL,
    last_modified_date timestamp without time zone NOT NULL,
    is_default boolean
);
     DROP TABLE public.creditrating;
       public         postgres    false            ?            1259    24887    creditrating_id_seq    SEQUENCE     |   CREATE SEQUENCE public.creditrating_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 *   DROP SEQUENCE public.creditrating_id_seq;
       public       postgres    false    255            ?           0    0    creditrating_id_seq    SEQUENCE OWNED BY     K   ALTER SEQUENCE public.creditrating_id_seq OWNED BY public.creditrating.id;
            public       postgres    false    254                       1259    24900    credits_and_payments    TABLE     ?   CREATE TABLE public.credits_and_payments (
    id bigint NOT NULL,
    memo bytea,
    credit_amount double precision,
    balance double precision,
    transaction_id bigint,
    payee_id bigint,
    version integer DEFAULT 0
);
 (   DROP TABLE public.credits_and_payments;
       public         postgres    false                        1259    24898    credits_and_payments_id_seq    SEQUENCE     ?   CREATE SEQUENCE public.credits_and_payments_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 2   DROP SEQUENCE public.credits_and_payments_id_seq;
       public       postgres    false    257            ?           0    0    credits_and_payments_id_seq    SEQUENCE OWNED BY     [   ALTER SEQUENCE public.credits_and_payments_id_seq OWNED BY public.credits_and_payments.id;
            public       postgres    false    256                       1259    24920    currency    TABLE       CREATE TABLE public.currency (
    id bigint NOT NULL,
    company_id bigint,
    name character varying(255) NOT NULL,
    symbol character varying(255),
    formal_name character varying(255),
    version integer,
    accounts_receivable_id bigint,
    accounts_payable_id bigint
);
    DROP TABLE public.currency;
       public         postgres    false                       1259    24918    currency_id_seq    SEQUENCE     x   CREATE SEQUENCE public.currency_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 &   DROP SEQUENCE public.currency_id_seq;
       public       postgres    false    260            ?           0    0    currency_id_seq    SEQUENCE OWNED BY     C   ALTER SEQUENCE public.currency_id_seq OWNED BY public.currency.id;
            public       postgres    false    259                       1259    24929    customer    TABLE     v  CREATE TABLE public.customer (
    id bigint NOT NULL,
    company_id bigint NOT NULL,
    balance_as_of bigint,
    number bytea,
    sales_person_id bigint,
    credit_limit double precision,
    price_level_id bigint,
    credit_rating_id bigint,
    shipping_method_id bigint,
    payment_term_id bigint,
    customer_group_id bigint,
    tax_group_id bigint,
    curret_due double precision,
    over_due_one_to_thiry_days double precision,
    over_due_thirty_one_to_sixty_days double precision,
    over_due_sixty_one_to_ninty_days double precision,
    over_due_over_ninty_days double precision,
    over_due_total_balance double precision,
    average_days_to_pay integer,
    average_days_to_pay_ytd integer,
    month_to_date double precision,
    year_to_date double precision,
    last_year double precision,
    life_time_sales double precision,
    deduct_tds boolean
);
    DROP TABLE public.customer;
       public         postgres    false                       1259    24937    customer_credit_memo    TABLE     ?  CREATE TABLE public.customer_credit_memo (
    id bigint NOT NULL,
    customer_id bigint,
    ccm_is_primary boolean,
    ccm_name bytea,
    ccm_title bytea,
    ccm_business_phone bytea,
    ccm_email bytea,
    billing_address_type integer,
    billing_address1 bytea,
    billing_address_street bytea,
    billing_address_city bytea,
    billing_address_state bytea,
    billing_address_zip bytea,
    billing_address_country bytea,
    billing_address_is_selected boolean,
    phone bytea,
    sales_person_id bigint,
    account_id bigint,
    price_level_id bigint,
    sales_tax double precision,
    discount_total double precision,
    balance_due double precision
);
 (   DROP TABLE public.customer_credit_memo;
       public         postgres    false                       1259    24947    customer_group    TABLE     \  CREATE TABLE public.customer_group (
    id bigint NOT NULL,
    company_id bigint NOT NULL,
    name bytea NOT NULL,
    version integer,
    created_by bigint NOT NULL,
    last_modifier bigint NOT NULL,
    created_date timestamp without time zone NOT NULL,
    last_modified_date timestamp without time zone NOT NULL,
    is_default boolean
);
 "   DROP TABLE public.customer_group;
       public         postgres    false                       1259    24945    customer_group_id_seq    SEQUENCE     ~   CREATE SEQUENCE public.customer_group_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 ,   DROP SEQUENCE public.customer_group_id_seq;
       public       postgres    false    264            ?           0    0    customer_group_id_seq    SEQUENCE OWNED BY     O   ALTER SEQUENCE public.customer_group_id_seq OWNED BY public.customer_group.id;
            public       postgres    false    263            	           1259    24956    customer_prepayment    TABLE     ?  CREATE TABLE public.customer_prepayment (
    id bigint NOT NULL,
    depositin_id bigint,
    customer_id bigint,
    balance_due double precision,
    check_number bytea,
    cpp_address_type integer,
    cpp_address_address1 bytea,
    cpp_address_street bytea,
    cpp_address_city bytea,
    cpp_address_state bytea,
    cpp_address_zip bytea,
    cpp_address_country bytea,
    cpp_address_is_selected boolean
);
 '   DROP TABLE public.customer_prepayment;
       public         postgres    false            
           1259    24964    customer_refund    TABLE     ?  CREATE TABLE public.customer_refund (
    id bigint NOT NULL,
    customer_id bigint,
    cr__address_type integer,
    cr_address_address1 bytea,
    cr__address_street bytea,
    cr__address_city bytea,
    cr__address_state bytea,
    cr__address_zip bytea,
    cr__address_country bytea,
    cr__address_is_selected boolean,
    payfrom_account_id bigint,
    is_to_be_printed boolean,
    is_paid boolean,
    payments double precision,
    balance_due double precision,
    check_number bytea
);
 #   DROP TABLE public.customer_refund;
       public         postgres    false                       1259    24974    customfield    TABLE     ?   CREATE TABLE public.customfield (
    id bigint NOT NULL,
    company_id bigint NOT NULL,
    name bytea,
    showcustomer boolean,
    showvendor boolean,
    showemployee boolean
);
    DROP TABLE public.customfield;
       public         postgres    false                       1259    24972    customfield_id_seq    SEQUENCE     {   CREATE SEQUENCE public.customfield_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 )   DROP SEQUENCE public.customfield_id_seq;
       public       postgres    false    268            ?           0    0    customfield_id_seq    SEQUENCE OWNED BY     I   ALTER SEQUENCE public.customfield_id_seq OWNED BY public.customfield.id;
            public       postgres    false    267                       1259    24985    delete_reason    TABLE     ?   CREATE TABLE public.delete_reason (
    id bigint NOT NULL,
    deleted_date bigint,
    email_id character varying(255),
    reason text
);
 !   DROP TABLE public.delete_reason;
       public         postgres    false                       1259    24983    delete_reason_id_seq    SEQUENCE     }   CREATE SEQUENCE public.delete_reason_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 +   DROP SEQUENCE public.delete_reason_id_seq;
       public       postgres    false    270            ?           0    0    delete_reason_id_seq    SEQUENCE OWNED BY     M   ALTER SEQUENCE public.delete_reason_id_seq OWNED BY public.delete_reason.id;
            public       postgres    false    269                       1259    24994    deposit_estimates    TABLE     c   CREATE TABLE public.deposit_estimates (
    deposit_id bigint NOT NULL,
    elt bigint NOT NULL
);
 %   DROP TABLE public.deposit_estimates;
       public         postgres    false                       1259    25001    depreciation    TABLE     ?   CREATE TABLE public.depreciation (
    id bigint NOT NULL,
    company_id bigint NOT NULL,
    status integer,
    depreciate_from bigint,
    depreciate_to bigint,
    fixed_asset_id bigint,
    depreciation_for integer
);
     DROP TABLE public.depreciation;
       public         postgres    false                       1259    24999    depreciation_id_seq    SEQUENCE     |   CREATE SEQUENCE public.depreciation_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 *   DROP SEQUENCE public.depreciation_id_seq;
       public       postgres    false    273            ?           0    0    depreciation_id_seq    SEQUENCE OWNED BY     K   ALTER SEQUENCE public.depreciation_id_seq OWNED BY public.depreciation.id;
            public       postgres    false    272                       1259    25009 	   developer    TABLE     ?  CREATE TABLE public.developer (
    id bigint NOT NULL,
    client bigint,
    api_key character varying(255),
    secret_key character varying(255),
    application_name character varying(255),
    description text,
    integrationurl character varying(255),
    applicationtype character varying(255),
    applicationuse character varying(255),
    developer_email_id character varying(255),
    contact character varying(255),
    succees_requests bigint,
    failure_requests bigint
);
    DROP TABLE public.developer;
       public         postgres    false                       1259    25007    developer_id_seq    SEQUENCE     y   CREATE SEQUENCE public.developer_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 '   DROP SEQUENCE public.developer_id_seq;
       public       postgres    false    275            ?           0    0    developer_id_seq    SEQUENCE OWNED BY     E   ALTER SEQUENCE public.developer_id_seq OWNED BY public.developer.id;
            public       postgres    false    274                       1259    25020    email_account    TABLE     ?  CREATE TABLE public.email_account (
    id bigint NOT NULL,
    company_id bigint NOT NULL,
    created_by bigint NOT NULL,
    last_modifier bigint NOT NULL,
    created_date timestamp without time zone NOT NULL,
    last_modified_date timestamp without time zone NOT NULL,
    version integer,
    email_id bytea,
    password bytea,
    mail_server bytea,
    port_number integer,
    is_ssl boolean
);
 !   DROP TABLE public.email_account;
       public         postgres    false                       1259    25018    email_account_id_seq    SEQUENCE     }   CREATE SEQUENCE public.email_account_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 +   DROP SEQUENCE public.email_account_id_seq;
       public       postgres    false    277            ?           0    0    email_account_id_seq    SEQUENCE OWNED BY     M   ALTER SEQUENCE public.email_account_id_seq OWNED BY public.email_account.id;
            public       postgres    false    276                       1259    25031    email_template    TABLE     ?   CREATE TABLE public.email_template (
    id bigint NOT NULL,
    company_id bigint NOT NULL,
    email_template_name bytea,
    email_body bytea,
    version integer
);
 "   DROP TABLE public.email_template;
       public         postgres    false                       1259    25029    email_template_id_seq    SEQUENCE     ~   CREATE SEQUENCE public.email_template_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 ,   DROP SEQUENCE public.email_template_id_seq;
       public       postgres    false    279            ?           0    0    email_template_id_seq    SEQUENCE OWNED BY     O   ALTER SEQUENCE public.email_template_id_seq OWNED BY public.email_template.id;
            public       postgres    false    278                       1259    25040    employee    TABLE     ?  CREATE TABLE public.employee (
    id bigint NOT NULL,
    number character varying(255),
    employee_group bigint,
    designation character varying(255),
    location character varying(255),
    date_of_birth bigint,
    gender integer,
    passport_number character varying(255),
    country_of_issue character varying(255),
    passport_expiry_date bigint,
    last_date bigint,
    reason_type integer,
    visa_number character varying(255),
    visa_expiry_date bigint,
    company_id bigint,
    salary double precision,
    pay_type integer,
    pay_frequency integer,
    addidtional_amount double precision,
    last_name character varying(255),
    ssn character varying(255)
);
    DROP TABLE public.employee;
       public         postgres    false            ?           1259    45689    employee_compensation    TABLE     ?  CREATE TABLE public.employee_compensation (
    id bigint NOT NULL,
    company_id bigint NOT NULL,
    version integer,
    created_by bigint NOT NULL,
    last_modifier bigint NOT NULL,
    created_date timestamp without time zone NOT NULL,
    last_modified_date timestamp without time zone NOT NULL,
    comp_type integer,
    salary double precision,
    pay_type integer,
    pay_frequency integer,
    addidtional_amount double precision,
    employee_cmp_id bigint
);
 )   DROP TABLE public.employee_compensation;
       public         postgres    false            ?           1259    45687    employee_compensation_id_seq    SEQUENCE     ?   CREATE SEQUENCE public.employee_compensation_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 3   DROP SEQUENCE public.employee_compensation_id_seq;
       public       postgres    false    498            ?           0    0    employee_compensation_id_seq    SEQUENCE OWNED BY     ]   ALTER SEQUENCE public.employee_compensation_id_seq OWNED BY public.employee_compensation.id;
            public       postgres    false    497                       1259    25050    employee_group    TABLE       CREATE TABLE public.employee_group (
    id bigint NOT NULL,
    company_id bigint,
    created_by bigint NOT NULL,
    last_modifier bigint NOT NULL,
    created_date timestamp without time zone NOT NULL,
    last_modified_date timestamp without time zone NOT NULL,
    name bytea
);
 "   DROP TABLE public.employee_group;
       public         postgres    false                       1259    25048    employee_group_id_seq    SEQUENCE     ~   CREATE SEQUENCE public.employee_group_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 ,   DROP SEQUENCE public.employee_group_id_seq;
       public       postgres    false    282            ?           0    0    employee_group_id_seq    SEQUENCE OWNED BY     O   ALTER SEQUENCE public.employee_group_id_seq OWNED BY public.employee_group.id;
            public       postgres    false    281                       1259    25061    employee_payhead_component    TABLE     ?   CREATE TABLE public.employee_payhead_component (
    id bigint NOT NULL,
    pay_head bigint,
    version integer,
    employee_payment_details_id bigint,
    rate double precision,
    empyeer_deduct double precision
);
 .   DROP TABLE public.employee_payhead_component;
       public         postgres    false                       1259    25059 !   employee_payhead_component_id_seq    SEQUENCE     ?   CREATE SEQUENCE public.employee_payhead_component_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 8   DROP SEQUENCE public.employee_payhead_component_id_seq;
       public       postgres    false    284            ?           0    0 !   employee_payhead_component_id_seq    SEQUENCE OWNED BY     g   ALTER SEQUENCE public.employee_payhead_component_id_seq OWNED BY public.employee_payhead_component.id;
            public       postgres    false    283                       1259    25069    employee_payment_details    TABLE     ?   CREATE TABLE public.employee_payment_details (
    id bigint NOT NULL,
    version integer,
    pay_run_id bigint,
    employee bigint
);
 ,   DROP TABLE public.employee_payment_details;
       public         postgres    false                       1259    25067    employee_payment_details_id_seq    SEQUENCE     ?   CREATE SEQUENCE public.employee_payment_details_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 6   DROP SEQUENCE public.employee_payment_details_id_seq;
       public       postgres    false    286            ?           0    0    employee_payment_details_id_seq    SEQUENCE OWNED BY     c   ALTER SEQUENCE public.employee_payment_details_id_seq OWNED BY public.employee_payment_details.id;
            public       postgres    false    285            ?           1259    45697    employee_tax    TABLE     ?  CREATE TABLE public.employee_tax (
    id bigint NOT NULL,
    company_id bigint NOT NULL,
    version integer,
    created_by bigint NOT NULL,
    last_modifier bigint NOT NULL,
    created_date timestamp without time zone NOT NULL,
    last_modified_date timestamp without time zone NOT NULL,
    tax_type integer,
    tax_filing_status integer,
    tax_allowances double precision,
    ssn_taxable boolean,
    medicare_taxable boolean,
    tax_unemployment boolean,
    addidtional_amount double precision,
    tax_residency_type integer,
    employee_tax_id bigint,
    tax_agency bigint,
    tax_state character varying(255),
    idx integer
);
     DROP TABLE public.employee_tax;
       public         postgres    false            ?           1259    45695    employee_tax_id_seq    SEQUENCE     |   CREATE SEQUENCE public.employee_tax_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 *   DROP SEQUENCE public.employee_tax_id_seq;
       public       postgres    false    500            ?           0    0    employee_tax_id_seq    SEQUENCE OWNED BY     K   ALTER SEQUENCE public.employee_tax_id_seq OWNED BY public.employee_tax.id;
            public       postgres    false    499                        1259    25080 
   enter_bill    TABLE     ?  CREATE TABLE public.enter_bill (
    id bigint NOT NULL,
    vendor_id bigint,
    eb_is_primary boolean,
    eb_name bytea,
    eb_title bytea,
    eb_business_phone bytea,
    eb_email bytea,
    vendor_address_type integer,
    vendor_address_address1 bytea,
    vendor_address_street bytea,
    vendor_address_city bytea,
    vendor_address_state bytea,
    vendor_address_zip bytea,
    vendor_address_country bytea,
    vendor_address_is_selected boolean,
    phone bytea,
    payment_term_id bigint,
    due_date bigint,
    delivery_date bigint,
    is_paid boolean,
    payments double precision,
    balance_due double precision,
    discount_date bigint,
    item_receipt_id bigint
);
    DROP TABLE public.enter_bill;
       public         postgres    false            !           1259    25088    enter_bill_orders    TABLE     ?   CREATE TABLE public.enter_bill_orders (
    enter_bill_id bigint NOT NULL,
    purchase_order_id bigint NOT NULL,
    vtx integer NOT NULL
);
 %   DROP TABLE public.enter_bill_orders;
       public         postgres    false                       1259    25075    enterbill_estimates    TABLE     h   CREATE TABLE public.enterbill_estimates (
    enter_bill_id bigint NOT NULL,
    elt bigint NOT NULL
);
 '   DROP TABLE public.enterbill_estimates;
       public         postgres    false            "           1259    25093    estimate    TABLE     ?  CREATE TABLE public.estimate (
    id bigint NOT NULL,
    customer_id bigint,
    est_is_primary boolean,
    est_name bytea,
    est_title bytea,
    est_business_phone bytea,
    est_email bytea,
    estimate_address_type integer,
    estimate_address_address1 bytea,
    estimate_address_street bytea,
    estimate_address_city bytea,
    estimate_address_state bytea,
    estimate_address_zip bytea,
    estimate_address_country bytea,
    estimate_address_is_selected boolean,
    shipping_address_type integer,
    shipping_address1 bytea,
    shipping_address_street bytea,
    shipping_address_city bytea,
    shipping_address_state bytea,
    shipping_address_zip bytea,
    shipping_address_country bytea,
    shipping_address_is_selected boolean,
    phone bytea,
    estimatetype integer,
    reffering_transaction_type integer,
    sales_person_id bigint,
    payment_terms_id bigint,
    shipping_terms_id bigint,
    shipping_method_id bigint,
    expiration_date bigint,
    customer_order_number bytea,
    delivery_date bigint,
    price_level_id bigint,
    sales_tax double precision,
    used_invoice bigint,
    used_cashsales bigint
);
    DROP TABLE public.estimate;
       public         postgres    false            #           1259    25101    expense    TABLE     >  CREATE TABLE public.expense (
    id bigint NOT NULL,
    bill_from bytea,
    bill_date bigint,
    status integer,
    submitted_date bigint,
    payment_due_date bigint,
    reporting_date bigint,
    amount_due double precision,
    paid_amount double precision,
    category integer,
    is_authorised boolean
);
    DROP TABLE public.expense;
       public         postgres    false            $           1259    25109    features    TABLE     j   CREATE TABLE public.features (
    feature_id bigint NOT NULL,
    feature_name character varying(255)
);
    DROP TABLE public.features;
       public         postgres    false            &           1259    25114    fiscal_year    TABLE     ?  CREATE TABLE public.fiscal_year (
    id bigint NOT NULL,
    company_id bigint NOT NULL,
    start_date bigint,
    end_date bigint,
    status integer,
    is_current_fiscal_year boolean,
    version integer,
    created_by bigint NOT NULL,
    last_modifier bigint NOT NULL,
    created_date timestamp without time zone NOT NULL,
    last_modified_date timestamp without time zone NOT NULL,
    is_default boolean
);
    DROP TABLE public.fiscal_year;
       public         postgres    false            %           1259    25112    fiscal_year_id_seq    SEQUENCE     {   CREATE SEQUENCE public.fiscal_year_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 )   DROP SEQUENCE public.fiscal_year_id_seq;
       public       postgres    false    294            ?           0    0    fiscal_year_id_seq    SEQUENCE OWNED BY     I   ALTER SEQUENCE public.fiscal_year_id_seq OWNED BY public.fiscal_year.id;
            public       postgres    false    293            (           1259    25122    fixed_asset    TABLE     ?  CREATE TABLE public.fixed_asset (
    id bigint NOT NULL,
    company_id bigint NOT NULL,
    name bytea NOT NULL,
    asset_number bytea,
    asset_account_id bigint,
    purchase_date bigint,
    purchase_price double precision,
    description bytea,
    asset_type bytea,
    depreciation_rate double precision,
    depreciation_method integer,
    version integer,
    depreciation_expense_account_id bigint,
    accumulated_depreciation_account_id bigint,
    accumulated_depreciation_amount double precision,
    status integer,
    book_value double precision,
    opening_balance_for_fiscalyear double precision,
    is_sold_or_disposed boolean,
    sold_or_disposed_date bigint,
    sale_account_id bigint,
    sale_price double precision,
    no_depreciation boolean,
    depreciation_till_date bigint,
    notes bytea,
    loss_or_gain_disposal_account_id bigint,
    total_capital_gain_account_id bigint,
    loss_or_gain double precision,
    created_by bigint NOT NULL,
    last_modifier bigint NOT NULL,
    created_date timestamp without time zone NOT NULL,
    last_modified_date timestamp without time zone NOT NULL,
    total_capital_gain_amount double precision,
    fixed_asset_id bigint,
    idx integer
);
    DROP TABLE public.fixed_asset;
       public         postgres    false            *           1259    25133    fixed_asset_history    TABLE       CREATE TABLE public.fixed_asset_history (
    id bigint NOT NULL,
    company_id bigint NOT NULL,
    action_date bigint,
    action_type bytea,
    user_name bytea,
    details bytea,
    journal_entry_id bigint,
    fixed_asset_id bigint,
    last_history integer
);
 '   DROP TABLE public.fixed_asset_history;
       public         postgres    false            )           1259    25131    fixed_asset_history_id_seq    SEQUENCE     ?   CREATE SEQUENCE public.fixed_asset_history_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 1   DROP SEQUENCE public.fixed_asset_history_id_seq;
       public       postgres    false    298            ?           0    0    fixed_asset_history_id_seq    SEQUENCE OWNED BY     Y   ALTER SEQUENCE public.fixed_asset_history_id_seq OWNED BY public.fixed_asset_history.id;
            public       postgres    false    297            '           1259    25120    fixed_asset_id_seq    SEQUENCE     {   CREATE SEQUENCE public.fixed_asset_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 )   DROP SEQUENCE public.fixed_asset_id_seq;
       public       postgres    false    296            ?           0    0    fixed_asset_id_seq    SEQUENCE OWNED BY     I   ALTER SEQUENCE public.fixed_asset_id_seq OWNED BY public.fixed_asset.id;
            public       postgres    false    295            +           1259    25142    flat_rate_pay_head    TABLE     ?   CREATE TABLE public.flat_rate_pay_head (
    id bigint NOT NULL,
    calculation_period integer,
    pay_frequency integer,
    pay_type integer
);
 &   DROP TABLE public.flat_rate_pay_head;
       public         postgres    false            ,           1259    25147    formula_function    TABLE     ?   CREATE TABLE public.formula_function (
    pay_head_id bigint NOT NULL,
    funcion_type integer,
    pay_head bigint,
    attendance_type bigint,
    function_index integer NOT NULL
);
 $   DROP TABLE public.formula_function;
       public         postgres    false            B           1259    25256    i_m_user    TABLE     ?   CREATE TABLE public.i_m_user (
    id bigint NOT NULL,
    client bigint,
    network_id character varying(255),
    network_type integer
);
    DROP TABLE public.i_m_user;
       public         postgres    false            A           1259    25254    i_m_user_id_seq    SEQUENCE     x   CREATE SEQUENCE public.i_m_user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 &   DROP SEQUENCE public.i_m_user_id_seq;
       public       postgres    false    322            ?           0    0    i_m_user_id_seq    SEQUENCE OWNED BY     C   ALTER SEQUENCE public.i_m_user_id_seq OWNED BY public.i_m_user.id;
            public       postgres    false    321            .           1259    25154    im_activation    TABLE     ?   CREATE TABLE public.im_activation (
    id bigint NOT NULL,
    network_id character varying(255),
    emailid character varying(255),
    tocken character varying(255)
);
 !   DROP TABLE public.im_activation;
       public         postgres    false            -           1259    25152    im_activation_id_seq    SEQUENCE     }   CREATE SEQUENCE public.im_activation_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 +   DROP SEQUENCE public.im_activation_id_seq;
       public       postgres    false    302            ?           0    0    im_activation_id_seq    SEQUENCE OWNED BY     M   ALTER SEQUENCE public.im_activation_id_seq OWNED BY public.im_activation.id;
            public       postgres    false    301            /           1259    25163    inventory_assembly    TABLE     f   CREATE TABLE public.inventory_assembly (
    id bigint NOT NULL,
    is_purhased_this_item boolean
);
 &   DROP TABLE public.inventory_assembly;
       public         postgres    false            1           1259    25170    inventory_assembly_item    TABLE       CREATE TABLE public.inventory_assembly_item (
    id bigint NOT NULL,
    inventory_item_id bigint NOT NULL,
    qty_value double precision,
    qty_unit bigint,
    discription bytea,
    ware_house bigint,
    line_total double precision,
    inventory_assembly_item_id bigint
);
 +   DROP TABLE public.inventory_assembly_item;
       public         postgres    false            0           1259    25168    inventory_assembly_item_id_seq    SEQUENCE     ?   CREATE SEQUENCE public.inventory_assembly_item_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 5   DROP SEQUENCE public.inventory_assembly_item_id_seq;
       public       postgres    false    305            ?           0    0    inventory_assembly_item_id_seq    SEQUENCE OWNED BY     a   ALTER SEQUENCE public.inventory_assembly_item_id_seq OWNED BY public.inventory_assembly_item.id;
            public       postgres    false    304            3           1259    25181    inventory_history    TABLE     ?  CREATE TABLE public.inventory_history (
    id bigint NOT NULL,
    company_id bigint NOT NULL,
    item bigint NOT NULL,
    transaction bigint NOT NULL,
    payee bigint,
    qty_value double precision,
    qty_unit bigint,
    unit_price double precision,
    asset_value double precision,
    created_by bigint NOT NULL,
    last_modifier bigint NOT NULL,
    created_date timestamp without time zone NOT NULL,
    last_modified_date timestamp without time zone NOT NULL,
    warehouse bigint
);
 %   DROP TABLE public.inventory_history;
       public         postgres    false            2           1259    25179    inventory_history_id_seq    SEQUENCE     ?   CREATE SEQUENCE public.inventory_history_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 /   DROP SEQUENCE public.inventory_history_id_seq;
       public       postgres    false    307            ?           0    0    inventory_history_id_seq    SEQUENCE OWNED BY     U   ALTER SEQUENCE public.inventory_history_id_seq OWNED BY public.inventory_history.id;
            public       postgres    false    306            8           1259    25203    invoice    TABLE     ?  CREATE TABLE public.invoice (
    id bigint NOT NULL,
    customer_id bigint,
    invoice_is_primary boolean,
    invoice_name bytea,
    invoice_title bytea,
    invoice_business_phone bytea,
    invoice_email bytea,
    billing_address_type integer,
    billing_address1 bytea,
    billing_address_street bytea,
    billing_address_city bytea,
    billing_address_state bytea,
    billing_address_zip bytea,
    billing_address_country bytea,
    billing_address_is_selected boolean,
    shipping_address_type integer,
    shipping_address1 bytea,
    shipping_address_street bytea,
    shipping_address_city bytea,
    shipping_address_state bytea,
    shipping_address_zip bytea,
    shipping_address_country bytea,
    shipping_address_is_selected boolean,
    phone bytea,
    sales_person_id bigint,
    payment_terms_id bigint,
    shipping_terms_id bigint,
    shipping_method_id bigint,
    due_date bigint,
    delivery_date bigint,
    sales_order_no bytea,
    discount_total double precision,
    price_level_id bigint,
    sales_tax_amount double precision,
    payments double precision,
    balance_due double precision,
    is_paid boolean,
    is_edited boolean,
    discount_date bigint,
    rounding_total double precision
);
    DROP TABLE public.invoice;
       public         postgres    false            ;           1259    25218    item    TABLE     ?  CREATE TABLE public.item (
    id bigint NOT NULL,
    company_id bigint NOT NULL,
    type integer,
    name bytea NOT NULL,
    path character varying(255),
    upc_or_sku bytea,
    weight integer,
    itemgroup_id bigint,
    is_active boolean,
    is_sell_item boolean,
    sales_description bytea,
    sales_price double precision,
    average_cost double precision,
    income_account_id bigint,
    min_qty_value double precision,
    min_unit bigint,
    max_qty_value double precision,
    max_unit bigint,
    measurement bigint,
    warehouse bigint,
    opening_balance double precision,
    is_taxable boolean,
    is_commission_itme boolean,
    is_buy_item boolean,
    is_subitem_of boolean,
    purchase_description bytea,
    purchase_prise double precision,
    expense_account_id bigint,
    preffered_vendor bigint,
    tax_code bigint,
    vendor_item_number bytea,
    satndard_cost double precision,
    version integer,
    created_by bigint NOT NULL,
    last_modifier bigint NOT NULL,
    created_date timestamp without time zone NOT NULL,
    last_modified_date timestamp without time zone NOT NULL,
    is_default boolean,
    as_of_date bigint,
    assets_account_id bigint,
    re_order_point_value double precision,
    re_order_point_unit bigint,
    parent_id bigint,
    depth integer,
    on_hand_qty_value double precision,
    on_hand_qty_unit bigint,
    item_total_value double precision
);
    DROP TABLE public.item;
       public         postgres    false            a           1259    25414    payee    TABLE       CREATE TABLE public.payee (
    id bigint NOT NULL,
    company_id bigint NOT NULL,
    name bytea NOT NULL,
    payee_since bigint,
    file_as bytea,
    version integer,
    currency bigint NOT NULL,
    currency_factor double precision,
    type integer,
    balance double precision,
    opening_balance double precision,
    payment_method bytea,
    bank_account_no bytea,
    bank_name bytea,
    bank_branch bytea,
    phone_number bytea,
    fax_number bytea,
    email_id bytea,
    web_page_address bytea,
    is_active boolean,
    memo bytea,
    created_by bigint NOT NULL,
    last_modifier bigint NOT NULL,
    created_date timestamp without time zone NOT NULL,
    last_modified_date timestamp without time zone NOT NULL,
    vat_registration_number bytea,
    tax_code_id bigint,
    tds_tax_item_id bigint,
    is_opening_balance_editable boolean,
    is_default boolean,
    pan_number bytea,
    cst_number bytea,
    service_tax_registration_number bytea,
    tin_number bytea,
    routing_number bytea
);
    DROP TABLE public.payee;
       public         postgres    false            ?           1259    25600    purchase_order    TABLE     P  CREATE TABLE public.purchase_order (
    id bigint NOT NULL,
    vendor_id bigint,
    ship_to_id bigint,
    purchase_order_date bigint,
    to_be_printed boolean,
    to_be_emailed boolean,
    eb_is_primary boolean,
    eb_name bytea,
    eb_title bytea,
    eb_business_phone bytea,
    eb_email bytea,
    vendor_address_type integer,
    vendor_address_address1 bytea,
    vendor_address_street bytea,
    vendor_address_city bytea,
    vendor_address_state bytea,
    vendor_address_zip bytea,
    vendor_address_country bytea,
    vendor_address_is_selected boolean,
    phone bytea,
    payment_term_id bigint,
    delivery_date bigint,
    dispatch_date bigint,
    shipping_address_type integer,
    shipping_address_address1 bytea,
    shipping_address_street bytea,
    shipping_address_city bytea,
    shipping_address_state bytea,
    shipping_address_zip bytea,
    shipping_address_country bytea,
    shipping_address_is_selected boolean,
    shipping_terms_id bigint,
    shipping_method_id bigint,
    purchase_order_number bytea,
    used_bill bigint,
    used_cashpurchase bigint
);
 "   DROP TABLE public.purchase_order;
       public         postgres    false            ?           1259    25888    transaction    TABLE     n  CREATE TABLE public.transaction (
    id bigint NOT NULL,
    company_id bigint,
    statement_record_id bigint,
    version integer,
    t_date bigint,
    t_type integer,
    number bytea,
    save_status integer,
    is_automatic_transaction boolean,
    is_validated boolean,
    payment_method bytea,
    memo bytea,
    reference bytea,
    sub_total double precision,
    total_taxable_amount double precision,
    total_non_taxable_amount double precision,
    status integer,
    can_void_or_edit boolean,
    is_deposited boolean,
    total double precision,
    net_amount double precision,
    created_by bigint NOT NULL,
    last_modifier bigint NOT NULL,
    created_date timestamp without time zone NOT NULL,
    last_modified_date timestamp without time zone NOT NULL,
    currency bigint,
    currency_factor double precision,
    credits_and_payments_id bigint,
    is_default boolean,
    transaction_accounter_class bigint,
    transaction_make_deposit_entry_id bigint,
    location_id bigint,
    job_id bigint,
    last_activity bigint,
    fixed_asset_id bigint,
    idx integer,
    transaction_idx integer
);
    DROP TABLE public.transaction;
       public         postgres    false            ?           1259    25944    transaction_item    TABLE       CREATE TABLE public.transaction_item (
    id bigint NOT NULL,
    version integer,
    type integer,
    item_id bigint,
    customer_id bigint,
    job_id bigint,
    account_id bigint,
    tax_code bigint,
    description bytea,
    ware_house bigint,
    qty_value double precision,
    qty_unit bigint,
    unit_price double precision,
    discount double precision,
    line_total double precision,
    update_amount double precision,
    is_taxable boolean,
    is_billable boolean,
    transaction_id bigint,
    vat_fraction double precision,
    invoiced double precision,
    back_order double precision,
    referring_transaction_item_id bigint,
    trans_item_accounter_class bigint,
    amounts_include_tax boolean,
    effecting_account bigint,
    idx integer
);
 $   DROP TABLE public.transaction_item;
       public         postgres    false            ?           1259    26025    unit    TABLE     ?   CREATE TABLE public.unit (
    id bigint NOT NULL,
    company_id bigint NOT NULL,
    type character varying(255),
    factor double precision,
    is_default boolean,
    measurement_id bigint
);
    DROP TABLE public.unit;
       public         postgres    false            ?           1259    26084    vendor    TABLE     C  CREATE TABLE public.vendor (
    id bigint NOT NULL,
    company_id bigint NOT NULL,
    account_number bytea,
    balance_as_of bigint,
    tax_id bytea,
    is_trackpayments_for_1099 boolean,
    is_tdsapplicable boolean,
    expense_account_id bigint,
    credit_limit double precision,
    shipping_method_id bigint,
    payment_terms_id bigint,
    vendor_group_id bigint,
    federal_taxid bytea,
    curret_due double precision,
    over_due_one_to_thiry_days double precision,
    over_due_thirty_one_to_sixty_days double precision,
    over_due_sixty_one_to_ninty_days double precision,
    over_due_over_ninty_days double precision,
    over_due_total_balance double precision,
    month_to_date double precision,
    year_to_date double precision,
    last_year double precision,
    life_time_purchases double precision
);
    DROP TABLE public.vendor;
       public         postgres    false            ?           1259    26092    vendor_credit_memo    TABLE       CREATE TABLE public.vendor_credit_memo (
    id bigint NOT NULL,
    vendor_id bigint,
    vcm_is_primary boolean,
    vcm_name bytea,
    vcm_title bytea,
    vcm_business_phone bytea,
    vcm_email bytea,
    phone bytea,
    balance_due double precision
);
 &   DROP TABLE public.vendor_credit_memo;
       public         postgres    false            ?           1259    28893    inventory_history_view    VIEW     i	  CREATE VIEW public.inventory_history_view AS
 SELECT t.id AS transaction_id,
    ti.id AS transaction_item_id,
    it.company_id AS company,
    it.id AS item_id,
    t.t_date AS transaction_date,
    t.t_type AS transaction_type,
    t.number AS transaction_number,
        CASE
            WHEN (t.t_type = ANY (ARRAY[1, 4, 8])) THEN (- ti.qty_value)
            ELSE ti.qty_value
        END AS qty_value,
    ti.qty_unit,
    (ti.unit_price * t.currency_factor) AS cost,
    ti.description,
    it.name AS item_name,
    it.on_hand_qty_value AS item_total_quantity,
    it.on_hand_qty_unit AS item_units,
    it.preffered_vendor,
    it.sales_price AS item_sale_price,
    p.id AS payee_id,
    p.name AS payee_name,
    it.type AS item_type,
    it.re_order_point_value,
    it.re_order_point_unit,
    it.purchase_description AS item_purchase_description,
    it.as_of_date AS asofdate,
    it.measurement AS item_measurement,
    un.type AS unit
   FROM (((((((((((((((public.item it
     LEFT JOIN public.transaction_item ti ON (((ti.type = 1) AND (it.type = ANY (ARRAY[2, 4])) AND (ti.item_id = it.id))))
     LEFT JOIN public.transaction t ON ((ti.transaction_id = t.id)))
     LEFT JOIN public.invoice i ON ((i.id = t.id)))
     LEFT JOIN public.cash_sales cs ON ((cs.id = t.id)))
     LEFT JOIN public.customer_credit_memo ccm ON ((ccm.id = t.id)))
     LEFT JOIN public.enter_bill eb ON ((eb.id = t.id)))
     LEFT JOIN public.vendor_credit_memo vcm ON ((vcm.id = t.id)))
     LEFT JOIN public.cash_purchase cp ON ((cp.id = t.id)))
     LEFT JOIN public.credit_card_charges cc ON ((cc.id = t.id)))
     LEFT JOIN public.estimate e ON (((e.id = t.id) AND (e.estimatetype = 6))))
     LEFT JOIN public.purchase_order po ON ((po.id = t.id)))
     LEFT JOIN public.customer c ON (((c.id = cs.customer_id) OR (c.id = ccm.customer_id) OR (c.id = i.customer_id) OR (c.id = e.customer_id))))
     LEFT JOIN public.vendor v ON (((v.id = cp.vendor_id) OR (v.id = vcm.vendor_id) OR (v.id = eb.vendor_id) OR ((v.id = cc.vendor_id) AND (cc.vendor_id IS NOT NULL)))))
     LEFT JOIN public.payee p ON (((p.id = c.id) OR (p.id = v.id))))
     LEFT JOIN public.unit un ON (((un.measurement_id = it.measurement) AND (un.is_default = true))))
  WHERE ((ti.id IS NOT NULL) AND ((t.t_type = ANY (ARRAY[1, 2, 4, 6, 8, 14, 22, 26, 27, 36, 7])) AND (t.save_status <> ALL (ARRAY[201, 202]))))
  ORDER BY t.t_date, t.id;
 )   DROP VIEW public.inventory_history_view;
       public       postgres    false    453    481    481    480    470    470    470    453    453    453    453    453    453    453    443    443    443    443    443    443    386    353    353    315    315    315    315    315    315    315    315    315    315    315    315    315    312    312    290    290    290    288    288    262    262    261    258    258    236    236    233    233            5           1259    25189    inventory_purchase    TABLE     ?   CREATE TABLE public.inventory_purchase (
    id bigint NOT NULL,
    version integer,
    qty_value double precision,
    qty_unit bigint,
    purchase_cost double precision,
    effecting_account bigint NOT NULL,
    transaction_item_id bigint
);
 &   DROP TABLE public.inventory_purchase;
       public         postgres    false            ?           1259    28888    inventory_purchase_history_view    VIEW     ]  CREATE VIEW public.inventory_purchase_history_view AS
 SELECT t.company_id,
    t.id AS transaction_id,
    ti.id AS transaction_item_id,
    (ti.unit_price * t.currency_factor) AS unit_price,
    ti.qty_value AS quantity,
    ti.qty_unit,
    it.id AS item_id,
    t.t_date AS transaction_date,
    ( SELECT u.factor
           FROM public.unit u
          WHERE (u.id = ti.qty_unit)) AS unit_factor,
    ( SELECT u1.factor
           FROM (public.unit u1
             LEFT JOIN public.unit u2 ON ((u1.measurement_id = u2.measurement_id)))
          WHERE ((u2.id = ti.qty_unit) AND (u1.is_default = true))) AS default_unit_factor
   FROM ((public.transaction_item ti
     LEFT JOIN public.transaction t ON ((t.id = ti.transaction_id)))
     LEFT JOIN public.item it ON (((ti.type = 1) AND (it.type = ANY (ARRAY[2, 4])) AND (it.id = ti.item_id))))
  WHERE ((t.save_status <> ALL (ARRAY[201, 202, 204])) AND ((t.t_type = ANY (ARRAY[2, 6, 4, 26, 27])) OR
        CASE
            WHEN (t.t_type = 36) THEN (ti.qty_value >= (0)::double precision)
            ELSE false
        END) AND (it.type = ANY (ARRAY[2, 4])));
 2   DROP VIEW public.inventory_purchase_history_view;
       public       postgres    false    443    443    443    453    453    453    443    443    443    315    470    470    453    453    453    453    470    470    315            4           1259    25187    inventory_purchase_id_seq    SEQUENCE     ?   CREATE SEQUENCE public.inventory_purchase_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 0   DROP SEQUENCE public.inventory_purchase_id_seq;
       public       postgres    false    309            ?           0    0    inventory_purchase_id_seq    SEQUENCE OWNED BY     W   ALTER SEQUENCE public.inventory_purchase_id_seq OWNED BY public.inventory_purchase.id;
            public       postgres    false    308            7           1259    25197    inventory_remap_task    TABLE     ?   CREATE TABLE public.inventory_remap_task (
    id bigint NOT NULL,
    company_id bigint NOT NULL,
    item_id bigint NOT NULL,
    user_id bigint NOT NULL
);
 (   DROP TABLE public.inventory_remap_task;
       public         postgres    false            6           1259    25195    inventory_remap_task_id_seq    SEQUENCE     ?   CREATE SEQUENCE public.inventory_remap_task_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 2   DROP SEQUENCE public.inventory_remap_task_id_seq;
       public       postgres    false    311            ?           0    0    inventory_remap_task_id_seq    SEQUENCE OWNED BY     [   ALTER SEQUENCE public.inventory_remap_task_id_seq OWNED BY public.inventory_remap_task.id;
            public       postgres    false    310            9           1259    25211    invoice_estimates    TABLE     ?   CREATE TABLE public.invoice_estimates (
    invoice_id bigint NOT NULL,
    estimate_id bigint NOT NULL,
    vtx integer NOT NULL
);
 %   DROP TABLE public.invoice_estimates;
       public         postgres    false            :           1259    25216    item_id_seq    SEQUENCE     t   CREATE SEQUENCE public.item_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 "   DROP SEQUENCE public.item_id_seq;
       public       postgres    false    315            ?           0    0    item_id_seq    SEQUENCE OWNED BY     ;   ALTER SEQUENCE public.item_id_seq OWNED BY public.item.id;
            public       postgres    false    314            >           1259    25238    item_receipt    TABLE     K  CREATE TABLE public.item_receipt (
    id bigint NOT NULL,
    vendor_id bigint,
    ship_to_id bigint,
    purchase_order_date bigint,
    to_be_printed boolean,
    to_be_emailed boolean,
    eb_is_primary boolean,
    eb_name bytea,
    eb_title bytea,
    eb_business_phone bytea,
    eb_email bytea,
    vendor_address_type integer,
    vendor_address_address1 bytea,
    vendor_address_street bytea,
    vendor_address_city bytea,
    vendor_address_state bytea,
    vendor_address_zip bytea,
    vendor_address_country bytea,
    vendor_address_is_selected boolean,
    phone bytea,
    payment_term_id bigint,
    due_date bigint,
    delivery_date bigint,
    shipping_address_type integer,
    shipping_address_address1 bytea,
    shipping_address_street bytea,
    shipping_address_city bytea,
    shipping_address_state bytea,
    shipping_address_zip bytea,
    shipping_address_country bytea,
    shipping_address_is_selected boolean,
    shipping_terms_id bigint,
    shipping_method_id bigint,
    purchase_order_id bigint,
    is_billed boolean,
    balance_due double precision
);
     DROP TABLE public.item_receipt;
       public         postgres    false            @           1259    25248    item_update    TABLE     ?  CREATE TABLE public.item_update (
    id bigint NOT NULL,
    company_id bigint NOT NULL,
    item bigint,
    transaction bigint,
    ware_house bigint,
    qty_value double precision,
    qty_unit bigint,
    unit_price double precision,
    created_by bigint NOT NULL,
    last_modifier bigint NOT NULL,
    created_date timestamp without time zone NOT NULL,
    last_modified_date timestamp without time zone NOT NULL
);
    DROP TABLE public.item_update;
       public         postgres    false            ?           1259    25246    item_update_id_seq    SEQUENCE     {   CREATE SEQUENCE public.item_update_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 )   DROP SEQUENCE public.item_update_id_seq;
       public       postgres    false    320            ?           0    0    item_update_id_seq    SEQUENCE OWNED BY     I   ALTER SEQUENCE public.item_update_id_seq OWNED BY public.item_update.id;
            public       postgres    false    319            =           1259    25229 	   itemgroup    TABLE     W  CREATE TABLE public.itemgroup (
    id bigint NOT NULL,
    company_id bigint NOT NULL,
    name bytea NOT NULL,
    version integer,
    created_by bigint NOT NULL,
    last_modifier bigint NOT NULL,
    created_date timestamp without time zone NOT NULL,
    last_modified_date timestamp without time zone NOT NULL,
    is_default boolean
);
    DROP TABLE public.itemgroup;
       public         postgres    false            <           1259    25227    itemgroup_id_seq    SEQUENCE     y   CREATE SEQUENCE public.itemgroup_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 '   DROP SEQUENCE public.itemgroup_id_seq;
       public       postgres    false    317            ?           0    0    itemgroup_id_seq    SEQUENCE OWNED BY     E   ALTER SEQUENCE public.itemgroup_id_seq OWNED BY public.itemgroup.id;
            public       postgres    false    316            D           1259    25264    job    TABLE     %  CREATE TABLE public.job (
    id bigint NOT NULL,
    company_id bigint NOT NULL,
    customer_id bigint,
    parent_job_id bigint,
    job_name bytea,
    job_status bytea,
    job_type bigint,
    job_active boolean,
    start_date bigint,
    project_enddate bigint,
    end_date bigint
);
    DROP TABLE public.job;
       public         postgres    false            C           1259    25262 
   job_id_seq    SEQUENCE     s   CREATE SEQUENCE public.job_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 !   DROP SEQUENCE public.job_id_seq;
       public       postgres    false    324            ?           0    0 
   job_id_seq    SEQUENCE OWNED BY     9   ALTER SEQUENCE public.job_id_seq OWNED BY public.job.id;
            public       postgres    false    323            ?           1259    28898    jobs_transactions_view    VIEW     ?	  CREATE VIEW public.jobs_transactions_view AS
 SELECT t.company_id,
    p.id AS customer_id,
    p.name AS customer_name,
    j.id AS job_id,
    j.job_name,
    t.id AS transaction_id,
    t.t_type,
    t.t_date,
    t.number AS t_number,
    ti.description AS ti_desc,
        CASE
            WHEN ((ti.type = 1) AND (it.type = ANY (ARRAY[2, 4])) AND (t.t_type = ANY (ARRAY[1, 4, 8]))) THEN ( SELECT sum(ip.purchase_cost) AS sum
               FROM public.inventory_purchase ip
              WHERE (ip.transaction_item_id = ti.id))
            WHEN ((t.t_type = 15) AND (ti.amounts_include_tax = true)) THEN (('-1'::integer)::double precision * ((ti.line_total - ti.vat_fraction) * t.currency_factor))
            WHEN ((t.t_type = 15) AND (ti.amounts_include_tax = false)) THEN (('-1'::integer)::double precision * (ti.line_total * t.currency_factor))
            ELSE '0'::double precision
        END AS cost,
        CASE
            WHEN (ti.amounts_include_tax = true) THEN
            CASE
                WHEN (t.t_type = ANY (ARRAY[1, 8])) THEN (ti.line_total - (ti.vat_fraction * t.currency_factor))
                WHEN (t.t_type = 4) THEN (('-1'::integer)::double precision * ((ti.line_total - ti.vat_fraction) * t.currency_factor))
                ELSE NULL::double precision
            END
            ELSE
            CASE
                WHEN (t.t_type = 4) THEN (('-1'::integer)::double precision * (ti.line_total * t.currency_factor))
                WHEN (t.t_type = ANY (ARRAY[1, 8])) THEN (ti.line_total * t.currency_factor)
                ELSE NULL::double precision
            END
        END AS revenue,
    ti.effecting_account AS revenue_account,
        CASE
            WHEN ((ti.type = 1) AND (it.type = ANY (ARRAY[2, 4])) AND (t.t_type = ANY (ARRAY[1, 4, 8]))) THEN ( SELECT ip.effecting_account
               FROM public.inventory_purchase ip
              WHERE (ip.transaction_item_id = ti.id)
             LIMIT 1)
            WHEN (t.t_type = 15) THEN ti.effecting_account
            ELSE NULL::bigint
        END AS cost_account
   FROM (((((public.transaction_item ti
     LEFT JOIN public.item it ON ((it.id = ti.item_id)))
     LEFT JOIN public.transaction t ON ((t.id = ti.transaction_id)))
     LEFT JOIN public.job j ON (((ti.job_id = j.id) OR (t.job_id = j.id))))
     LEFT JOIN public.customer c ON ((c.id = j.customer_id)))
     LEFT JOIN public.payee p ON ((p.id = c.id)))
  WHERE ((j.id IS NOT NULL) AND (t.save_status <> 204) AND ((t.t_type = ANY (ARRAY[1, 4, 8, 15])) OR ti.is_billable));
 )   DROP VIEW public.jobs_transactions_view;
       public       postgres    false    324    443    443    453    443    443    353    453    453    353    453    453    453    453    324    324    315    315    443    309    309    309    261    453    453    453    443    443    443    453            E           1259    25273    journal_entry    TABLE     ?   CREATE TABLE public.journal_entry (
    id bigint NOT NULL,
    debit_total double precision,
    credit_total double precision,
    journal_entry_type integer,
    balance_due double precision,
    payee_id bigint,
    account_id bigint
);
 !   DROP TABLE public.journal_entry;
       public         postgres    false            G           1259    25280    key    TABLE     ?   CREATE TABLE public.key (
    id bigint NOT NULL,
    key character varying(255),
    usage_order integer,
    usage_count integer
);
    DROP TABLE public.key;
       public         postgres    false            F           1259    25278 
   key_id_seq    SEQUENCE     s   CREATE SEQUENCE public.key_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 !   DROP SEQUENCE public.key_id_seq;
       public       postgres    false    327            ?           0    0 
   key_id_seq    SEQUENCE OWNED BY     9   ALTER SEQUENCE public.key_id_seq OWNED BY public.key.id;
            public       postgres    false    326            H           1259    25286    key_messages    TABLE     a   CREATE TABLE public.key_messages (
    message_id bigint NOT NULL,
    key_id bigint NOT NULL
);
     DROP TABLE public.key_messages;
       public         postgres    false            I           1259    25291    language    TABLE     ?   CREATE TABLE public.language (
    code character varying(255) NOT NULL,
    name character varying(255),
    tooltip character varying(255)
);
    DROP TABLE public.language;
       public         postgres    false            K           1259    25301    license    TABLE     e  CREATE TABLE public.license (
    id bigint NOT NULL,
    client_id bigint,
    license_text text NOT NULL,
    server_id character varying(255) NOT NULL,
    organisation character varying(255),
    expires_on timestamp without time zone,
    purchased_on timestamp without time zone,
    is_active boolean,
    version integer,
    no_of_users integer
);
    DROP TABLE public.license;
       public         postgres    false            J           1259    25299    license_id_seq    SEQUENCE     w   CREATE SEQUENCE public.license_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 %   DROP SEQUENCE public.license_id_seq;
       public       postgres    false    331            ?           0    0    license_id_seq    SEQUENCE OWNED BY     A   ALTER SEQUENCE public.license_id_seq OWNED BY public.license.id;
            public       postgres    false    330            M           1259    25312    license_purchase    TABLE     ?   CREATE TABLE public.license_purchase (
    id bigint NOT NULL,
    client_id bigint NOT NULL,
    type integer,
    paypal_sub_id character varying(255),
    purchase_date timestamp without time zone,
    expired_date timestamp without time zone
);
 $   DROP TABLE public.license_purchase;
       public         postgres    false            L           1259    25310    license_purchase_id_seq    SEQUENCE     ?   CREATE SEQUENCE public.license_purchase_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 .   DROP SEQUENCE public.license_purchase_id_seq;
       public       postgres    false    333            ?           0    0    license_purchase_id_seq    SEQUENCE OWNED BY     S   ALTER SEQUENCE public.license_purchase_id_seq OWNED BY public.license_purchase.id;
            public       postgres    false    332            ?           1259    46039    live_tax_rate    TABLE     3  CREATE TABLE public.live_tax_rate (
    id bigint NOT NULL,
    version integer,
    name character varying(255),
    tax_filing_status character varying(255),
    tax_type character varying(255),
    tax_country character varying(255),
    tax_year character varying(255),
    tax_std_deductions bigint
);
 !   DROP TABLE public.live_tax_rate;
       public         postgres    false            ?           1259    46037    live_tax_rate_id_seq    SEQUENCE     }   CREATE SEQUENCE public.live_tax_rate_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 +   DROP SEQUENCE public.live_tax_rate_id_seq;
       public       postgres    false    502            ?           0    0    live_tax_rate_id_seq    SEQUENCE OWNED BY     M   ALTER SEQUENCE public.live_tax_rate_id_seq OWNED BY public.live_tax_rate.id;
            public       postgres    false    501            ?           1259    46080    live_tax_rate_range    TABLE     "  CREATE TABLE public.live_tax_rate_range (
    id bigint NOT NULL,
    version integer,
    start_rate double precision,
    end_rate double precision,
    rate_range double precision,
    live_tax_rate_id bigint,
    live_tax_rate_range_id bigint,
    plus_more boolean,
    idx integer
);
 '   DROP TABLE public.live_tax_rate_range;
       public         postgres    false            ?           1259    46078    live_tax_rate_range_id_seq    SEQUENCE     ?   CREATE SEQUENCE public.live_tax_rate_range_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 1   DROP SEQUENCE public.live_tax_rate_range_id_seq;
       public       postgres    false    504                        0    0    live_tax_rate_range_id_seq    SEQUENCE OWNED BY     Y   ALTER SEQUENCE public.live_tax_rate_range_id_seq OWNED BY public.live_tax_rate_range.id;
            public       postgres    false    503            O           1259    25320    local_message    TABLE     ?   CREATE TABLE public.local_message (
    id bigint NOT NULL,
    message_id bigint NOT NULL,
    language character varying(255) NOT NULL,
    value text,
    votes integer,
    is_approved boolean,
    client bigint,
    created_date date NOT NULL
);
 !   DROP TABLE public.local_message;
       public         postgres    false            N           1259    25318    local_message_id_seq    SEQUENCE     }   CREATE SEQUENCE public.local_message_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 +   DROP SEQUENCE public.local_message_id_seq;
       public       postgres    false    335                       0    0    local_message_id_seq    SEQUENCE OWNED BY     M   ALTER SEQUENCE public.local_message_id_seq OWNED BY public.local_message.id;
            public       postgres    false    334            Q           1259    25331    location    TABLE     2  CREATE TABLE public.location (
    id bigint NOT NULL,
    company_id bigint NOT NULL,
    location_name bytea,
    version integer,
    location_company_name bytea,
    location_company_legal_name bytea,
    location_address_email_id bytea,
    location_address_phone bytea,
    location_address_type integer,
    location_address_address1 bytea,
    location_address_street bytea,
    location_address_city bytea,
    location_address_state bytea,
    location_address_zip bytea,
    location_address_country bytea,
    location_address_is_selected boolean
);
    DROP TABLE public.location;
       public         postgres    false            P           1259    25329    location_id_seq    SEQUENCE     x   CREATE SEQUENCE public.location_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 &   DROP SEQUENCE public.location_id_seq;
       public       postgres    false    337                       0    0    location_id_seq    SEQUENCE OWNED BY     C   ALTER SEQUENCE public.location_id_seq OWNED BY public.location.id;
            public       postgres    false    336            R           1259    25340    maintanance_user    TABLE     Y   CREATE TABLE public.maintanance_user (
    user_email character varying(255) NOT NULL
);
 $   DROP TABLE public.maintanance_user;
       public         postgres    false            S           1259    25345    make_deposit    TABLE     T   CREATE TABLE public.make_deposit (
    id bigint NOT NULL,
    deposit_to bigint
);
     DROP TABLE public.make_deposit;
       public         postgres    false            U           1259    25352    measurement    TABLE     ?   CREATE TABLE public.measurement (
    id bigint NOT NULL,
    company_id bigint NOT NULL,
    name character varying(255),
    description character varying(255)
);
    DROP TABLE public.measurement;
       public         postgres    false            T           1259    25350    measurement_id_seq    SEQUENCE     {   CREATE SEQUENCE public.measurement_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 )   DROP SEQUENCE public.measurement_id_seq;
       public       postgres    false    341                       0    0    measurement_id_seq    SEQUENCE OWNED BY     I   ALTER SEQUENCE public.measurement_id_seq OWNED BY public.measurement.id;
            public       postgres    false    340            V           1259    25361    members    TABLE     d   CREATE TABLE public.members (
    member_id bigint NOT NULL,
    email_id character varying(255)
);
    DROP TABLE public.members;
       public         postgres    false            X           1259    25366    message    TABLE     s   CREATE TABLE public.message (
    id bigint NOT NULL,
    value text,
    comment text,
    is_not_used boolean
);
    DROP TABLE public.message;
       public         postgres    false            W           1259    25364    message_id_seq    SEQUENCE     w   CREATE SEQUENCE public.message_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 %   DROP SEQUENCE public.message_id_seq;
       public       postgres    false    344                       0    0    message_id_seq    SEQUENCE OWNED BY     A   ALTER SEQUENCE public.message_id_seq OWNED BY public.message.id;
            public       postgres    false    343            Z           1259    25379    message_or_task    TABLE     ?  CREATE TABLE public.message_or_task (
    id bigint NOT NULL,
    company_id bigint NOT NULL,
    type integer,
    system_created boolean,
    content bytea,
    content_type integer,
    action_token bytea,
    date bigint,
    version integer,
    created_by bigint NOT NULL,
    last_modifier bigint NOT NULL,
    created_on timestamp without time zone NOT NULL,
    last_modified_on timestamp without time zone NOT NULL,
    to_user_id bigint
);
 #   DROP TABLE public.message_or_task;
       public         postgres    false            Y           1259    25377    message_or_task_id_seq    SEQUENCE        CREATE SEQUENCE public.message_or_task_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 -   DROP SEQUENCE public.message_or_task_id_seq;
       public       postgres    false    346                       0    0    message_or_task_id_seq    SEQUENCE OWNED BY     Q   ALTER SEQUENCE public.message_or_task_id_seq OWNED BY public.message_or_task.id;
            public       postgres    false    345            [           1259    25388    mobile_cookie    TABLE     e   CREATE TABLE public.mobile_cookie (
    cookie character varying(255) NOT NULL,
    client bigint
);
 !   DROP TABLE public.mobile_cookie;
       public         postgres    false            ]           1259    25395    news    TABLE     ?   CREATE TABLE public.news (
    id bigint NOT NULL,
    sqldate timestamp without time zone,
    sticky boolean,
    url character varying(255),
    body character varying(255),
    title character varying(255)
);
    DROP TABLE public.news;
       public         postgres    false            \           1259    25393    news_id_seq    SEQUENCE     t   CREATE SEQUENCE public.news_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 "   DROP SEQUENCE public.news_id_seq;
       public       postgres    false    349                       0    0    news_id_seq    SEQUENCE OWNED BY     ;   ALTER SEQUENCE public.news_id_seq OWNED BY public.news.id;
            public       postgres    false    348            _           1259    25406    nominal_code_range    TABLE     ?   CREATE TABLE public.nominal_code_range (
    id bigint NOT NULL,
    account_sub_base_type integer NOT NULL,
    minimum integer NOT NULL,
    maximum integer NOT NULL,
    company_id bigint
);
 &   DROP TABLE public.nominal_code_range;
       public         postgres    false            ^           1259    25404    nominal_code_range_id_seq    SEQUENCE     ?   CREATE SEQUENCE public.nominal_code_range_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 0   DROP SEQUENCE public.nominal_code_range_id_seq;
       public       postgres    false    351                       0    0    nominal_code_range_id_seq    SEQUENCE OWNED BY     W   ALTER SEQUENCE public.nominal_code_range_id_seq OWNED BY public.nominal_code_range.id;
            public       postgres    false    350            m           1259    25488    pay_bill    TABLE     ?  CREATE TABLE public.pay_bill (
    id bigint NOT NULL,
    payfrom_id bigint,
    bill_due_onorbefore bigint,
    vendor_id bigint,
    tds_taxitem bigint,
    tds_total double precision,
    is_to_be_printed boolean,
    ending_balance double precision,
    unused_amount double precision,
    vendor_balance double precision,
    paybill_address_type integer,
    paybill_address_address1 bytea,
    paybill_address_street bytea,
    paybill_address_city bytea,
    paybill_address_state bytea,
    paybill_address_zip bytea,
    paybill_address_country bytea,
    paybill_address_is_selected boolean,
    check_number bytea,
    is_amount_include_tds boolean
);
    DROP TABLE public.pay_bill;
       public         postgres    false            n           1259    25496    pay_employee    TABLE     ?   CREATE TABLE public.pay_employee (
    id bigint NOT NULL,
    pay_from_id bigint,
    employee_id bigint,
    employee_group_id bigint
);
     DROP TABLE public.pay_employee;
       public         postgres    false            o           1259    25501    pay_expense    TABLE     y   CREATE TABLE public.pay_expense (
    id bigint NOT NULL,
    account_id bigint,
    reference_or_cheque_number bytea
);
    DROP TABLE public.pay_expense;
       public         postgres    false            q           1259    25511    pay_head    TABLE     O  CREATE TABLE public.pay_head (
    id bigint NOT NULL,
    company_id bigint NOT NULL,
    created_by bigint NOT NULL,
    last_modifier bigint NOT NULL,
    created_date timestamp without time zone NOT NULL,
    last_modified_date timestamp without time zone NOT NULL,
    name bytea NOT NULL,
    type integer,
    rounding_method integer,
    name_to_appear_on_payslip character varying(255),
    calculation_type integer,
    is_affect_net_salary boolean,
    account bigint,
    liability_account bigint,
    asset_account bigint,
    is_default boolean,
    is_emper_deduct boolean
);
    DROP TABLE public.pay_head;
       public         postgres    false            p           1259    25509    pay_head_id_seq    SEQUENCE     x   CREATE SEQUENCE public.pay_head_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 &   DROP SEQUENCE public.pay_head_id_seq;
       public       postgres    false    369                       0    0    pay_head_id_seq    SEQUENCE OWNED BY     C   ALTER SEQUENCE public.pay_head_id_seq OWNED BY public.pay_head.id;
            public       postgres    false    368            s           1259    25522    pay_roll_unit    TABLE     W  CREATE TABLE public.pay_roll_unit (
    id bigint NOT NULL,
    company_id bigint,
    created_by bigint NOT NULL,
    last_modifier bigint NOT NULL,
    created_date timestamp without time zone NOT NULL,
    last_modified_date timestamp without time zone NOT NULL,
    symbol bytea,
    formal_name bytea,
    no_of_decimal_places integer
);
 !   DROP TABLE public.pay_roll_unit;
       public         postgres    false            r           1259    25520    pay_roll_unit_id_seq    SEQUENCE     }   CREATE SEQUENCE public.pay_roll_unit_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 +   DROP SEQUENCE public.pay_roll_unit_id_seq;
       public       postgres    false    371            	           0    0    pay_roll_unit_id_seq    SEQUENCE OWNED BY     M   ALTER SEQUENCE public.pay_roll_unit_id_seq OWNED BY public.pay_roll_unit.id;
            public       postgres    false    370            t           1259    25531    pay_run    TABLE     :  CREATE TABLE public.pay_run (
    id bigint NOT NULL,
    company_id bigint NOT NULL,
    employee bigint,
    employee_group bigint,
    pay_period_startdate bigint,
    pay_period_enddate bigint,
    noof_working_days double precision NOT NULL,
    payments double precision,
    balance_due double precision
);
    DROP TABLE public.pay_run;
       public         postgres    false            v           1259    25538    pay_structure    TABLE     <  CREATE TABLE public.pay_structure (
    id bigint NOT NULL,
    company_id bigint,
    created_by bigint NOT NULL,
    last_modifier bigint NOT NULL,
    created_date timestamp without time zone NOT NULL,
    last_modified_date timestamp without time zone NOT NULL,
    employee bigint,
    employee_group bigint
);
 !   DROP TABLE public.pay_structure;
       public         postgres    false            u           1259    25536    pay_structure_id_seq    SEQUENCE     }   CREATE SEQUENCE public.pay_structure_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 +   DROP SEQUENCE public.pay_structure_id_seq;
       public       postgres    false    374            
           0    0    pay_structure_id_seq    SEQUENCE OWNED BY     M   ALTER SEQUENCE public.pay_structure_id_seq OWNED BY public.pay_structure.id;
            public       postgres    false    373            x           1259    25546    pay_structure_item    TABLE     ?   CREATE TABLE public.pay_structure_item (
    id bigint NOT NULL,
    pay_structure bigint,
    pay_head bigint,
    rate double precision,
    effective_from bigint,
    idx integer
);
 &   DROP TABLE public.pay_structure_item;
       public         postgres    false            w           1259    25544    pay_structure_item_id_seq    SEQUENCE     ?   CREATE SEQUENCE public.pay_structure_item_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 0   DROP SEQUENCE public.pay_structure_item_id_seq;
       public       postgres    false    376                       0    0    pay_structure_item_id_seq    SEQUENCE OWNED BY     W   ALTER SEQUENCE public.pay_structure_item_id_seq OWNED BY public.pay_structure_item.id;
            public       postgres    false    375            y           1259    25552    pay_tax    TABLE     ?   CREATE TABLE public.pay_tax (
    id bigint NOT NULL,
    returns_due_on_or_before bigint,
    payfrom_account_id bigint,
    tax_agency_id bigint,
    is_edited boolean,
    check_number bytea
);
    DROP TABLE public.pay_tax;
       public         postgres    false            b           1259    25423    payee_address    TABLE        CREATE TABLE public.payee_address (
    payee_id bigint NOT NULL,
    type integer NOT NULL,
    address1 bytea NOT NULL,
    street bytea NOT NULL,
    city bytea NOT NULL,
    state bytea NOT NULL,
    zip bytea NOT NULL,
    country bytea NOT NULL,
    is_selected boolean NOT NULL
);
 !   DROP TABLE public.payee_address;
       public         postgres    false            c           1259    25431    payee_contact    TABLE     ?   CREATE TABLE public.payee_contact (
    payee_id bigint NOT NULL,
    is_primary boolean NOT NULL,
    name bytea NOT NULL,
    title bytea NOT NULL,
    business_phone bytea NOT NULL,
    email bytea NOT NULL
);
 !   DROP TABLE public.payee_contact;
       public         postgres    false            e           1259    25441    payee_customfields    TABLE     ?   CREATE TABLE public.payee_customfields (
    id bigint NOT NULL,
    value bytea,
    payee_id bigint NOT NULL,
    customfield_id bigint NOT NULL
);
 &   DROP TABLE public.payee_customfields;
       public         postgres    false            d           1259    25439    payee_customfields_id_seq    SEQUENCE     ?   CREATE SEQUENCE public.payee_customfields_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 0   DROP SEQUENCE public.payee_customfields_id_seq;
       public       postgres    false    357                       0    0    payee_customfields_id_seq    SEQUENCE OWNED BY     W   ALTER SEQUENCE public.payee_customfields_id_seq OWNED BY public.payee_customfields.id;
            public       postgres    false    356            f           1259    25450    payee_fields    TABLE     ?   CREATE TABLE public.payee_fields (
    payee_id bigint NOT NULL,
    payee_field_value bytea,
    payee_field_name bytea NOT NULL
);
     DROP TABLE public.payee_fields;
       public         postgres    false            `           1259    25412    payee_id_seq    SEQUENCE     u   CREATE SEQUENCE public.payee_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 #   DROP SEQUENCE public.payee_id_seq;
       public       postgres    false    353                       0    0    payee_id_seq    SEQUENCE OWNED BY     =   ALTER SEQUENCE public.payee_id_seq OWNED BY public.payee.id;
            public       postgres    false    352            h           1259    25460    payee_update    TABLE     [  CREATE TABLE public.payee_update (
    id bigint NOT NULL,
    company_id bigint NOT NULL,
    payee bigint,
    transaction bigint,
    amount double precision,
    created_by bigint NOT NULL,
    last_modifier bigint NOT NULL,
    created_date timestamp without time zone NOT NULL,
    last_modified_date timestamp without time zone NOT NULL
);
     DROP TABLE public.payee_update;
       public         postgres    false            g           1259    25458    payee_update_id_seq    SEQUENCE     |   CREATE SEQUENCE public.payee_update_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 *   DROP SEQUENCE public.payee_update_id_seq;
       public       postgres    false    360                       0    0    payee_update_id_seq    SEQUENCE OWNED BY     K   ALTER SEQUENCE public.payee_update_id_seq OWNED BY public.payee_update.id;
            public       postgres    false    359            j           1259    25468    paymentterms    TABLE     ?  CREATE TABLE public.paymentterms (
    id bigint NOT NULL,
    company_id bigint NOT NULL,
    name bytea NOT NULL,
    description bytea,
    due integer,
    due_days integer,
    discount_percent double precision,
    if_paid_within integer,
    version integer,
    created_by bigint NOT NULL,
    last_modifier bigint NOT NULL,
    created_date timestamp without time zone NOT NULL,
    last_modified_date timestamp without time zone NOT NULL,
    is_default boolean,
    is_date_driven boolean
);
     DROP TABLE public.paymentterms;
       public         postgres    false            i           1259    25466    paymentterms_id_seq    SEQUENCE     |   CREATE SEQUENCE public.paymentterms_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 *   DROP SEQUENCE public.paymentterms_id_seq;
       public       postgres    false    362                       0    0    paymentterms_id_seq    SEQUENCE OWNED BY     K   ALTER SEQUENCE public.paymentterms_id_seq OWNED BY public.paymentterms.id;
            public       postgres    false    361            l           1259    25479    paypal_transaction    TABLE       CREATE TABLE public.paypal_transaction (
    id bigint NOT NULL,
    company_id bigint NOT NULL,
    accountid bigint,
    date character varying(255),
    timezone character varying(255),
    type character varying(255),
    email character varying(255),
    buyername character varying(255),
    transactionid character varying(255),
    transactionstatus character varying(255),
    grossamount character varying(255),
    paypalfees character varying(255),
    netamount character varying(255),
    currencycode character varying(255)
);
 &   DROP TABLE public.paypal_transaction;
       public         postgres    false            k           1259    25477    paypal_transaction_id_seq    SEQUENCE     ?   CREATE SEQUENCE public.paypal_transaction_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 0   DROP SEQUENCE public.paypal_transaction_id_seq;
       public       postgres    false    364                       0    0    paypal_transaction_id_seq    SEQUENCE OWNED BY     W   ALTER SEQUENCE public.paypal_transaction_id_seq OWNED BY public.paypal_transaction.id;
            public       postgres    false    363            {           1259    25562    portlet_configuration    TABLE     ?   CREATE TABLE public.portlet_configuration (
    id bigint NOT NULL,
    portlet_name character varying(255),
    column_no integer,
    portlet_config_id bigint,
    portlet_configuration_id integer
);
 )   DROP TABLE public.portlet_configuration;
       public         postgres    false            z           1259    25560    portlet_configuration_id_seq    SEQUENCE     ?   CREATE SEQUENCE public.portlet_configuration_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 3   DROP SEQUENCE public.portlet_configuration_id_seq;
       public       postgres    false    379                       0    0    portlet_configuration_id_seq    SEQUENCE OWNED BY     ]   ALTER SEQUENCE public.portlet_configuration_id_seq OWNED BY public.portlet_configuration.id;
            public       postgres    false    378            |           1259    25568 !   portlet_configuration_portletdata    TABLE     ?   CREATE TABLE public.portlet_configuration_portletdata (
    id bigint NOT NULL,
    value character varying(255),
    key character varying(255) NOT NULL
);
 5   DROP TABLE public.portlet_configuration_portletdata;
       public         postgres    false            ~           1259    25578    portlet_page_configuration    TABLE     ?   CREATE TABLE public.portlet_page_configuration (
    id bigint NOT NULL,
    page_name character varying(255) NOT NULL,
    user_id bigint NOT NULL,
    columns integer
);
 .   DROP TABLE public.portlet_page_configuration;
       public         postgres    false            }           1259    25576 !   portlet_page_configuration_id_seq    SEQUENCE     ?   CREATE SEQUENCE public.portlet_page_configuration_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 8   DROP SEQUENCE public.portlet_page_configuration_id_seq;
       public       postgres    false    382                       0    0 !   portlet_page_configuration_id_seq    SEQUENCE OWNED BY     g   ALTER SEQUENCE public.portlet_page_configuration_id_seq OWNED BY public.portlet_page_configuration.id;
            public       postgres    false    381            ?           1259    25586 
   pricelevel    TABLE     ?  CREATE TABLE public.pricelevel (
    id bigint NOT NULL,
    company_id bigint NOT NULL,
    name bytea NOT NULL,
    percentage double precision,
    is_p_decrease_by_this_percent boolean,
    version integer,
    created_by bigint NOT NULL,
    last_modifier bigint NOT NULL,
    created_date timestamp without time zone NOT NULL,
    last_modified_date timestamp without time zone NOT NULL,
    is_default boolean
);
    DROP TABLE public.pricelevel;
       public         postgres    false                       1259    25584    pricelevel_id_seq    SEQUENCE     z   CREATE SEQUENCE public.pricelevel_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 (   DROP SEQUENCE public.pricelevel_id_seq;
       public       postgres    false    384                       0    0    pricelevel_id_seq    SEQUENCE OWNED BY     G   ALTER SEQUENCE public.pricelevel_id_seq OWNED BY public.pricelevel.id;
            public       postgres    false    383            ?           1259    25595    property    TABLE     [   CREATE TABLE public.property (
    id bigint NOT NULL,
    value character varying(255)
);
    DROP TABLE public.property;
       public         postgres    false            ?           1259    25608    receive_payment    TABLE     ?  CREATE TABLE public.receive_payment (
    id bigint NOT NULL,
    customer_id bigint,
    amount double precision,
    customer_balance double precision,
    account_id bigint,
    check_number bytea,
    unused_credits double precision,
    unused_payments double precision,
    total_cash_discount double precision,
    total_write_off double precision,
    total_applied_credits double precision,
    tds_total double precision,
    credits_and_payments_id bigint
);
 #   DROP TABLE public.receive_payment;
       public         postgres    false            ?           1259    25616    receive_vat    TABLE     ?   CREATE TABLE public.receive_vat (
    id bigint NOT NULL,
    returns_due_on_or_before bigint,
    deposit_in_account_id bigint,
    tax_agency_id bigint,
    ending_balance double precision,
    is_edited boolean,
    check_number bytea
);
    DROP TABLE public.receive_vat;
       public         postgres    false            ?           1259    25626    reconciliation    TABLE     !  CREATE TABLE public.reconciliation (
    id bigint NOT NULL,
    company_id bigint NOT NULL,
    account bigint,
    start_date bigint,
    end_date bigint,
    opening_balance double precision,
    closing_balance double precision,
    reconciliation_date bigint,
    statement bigint
);
 "   DROP TABLE public.reconciliation;
       public         postgres    false            ?           1259    25624    reconciliation_id_seq    SEQUENCE     ~   CREATE SEQUENCE public.reconciliation_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 ,   DROP SEQUENCE public.reconciliation_id_seq;
       public       postgres    false    390                       0    0    reconciliation_id_seq    SEQUENCE OWNED BY     O   ALTER SEQUENCE public.reconciliation_id_seq OWNED BY public.reconciliation.id;
            public       postgres    false    389            ?           1259    25634    reconciliation_item    TABLE     ?   CREATE TABLE public.reconciliation_item (
    id bigint NOT NULL,
    amount double precision,
    transaction_number bytea,
    transaction_type integer,
    transaction_date bigint,
    reconciliation_id bigint,
    transaction_id bigint
);
 '   DROP TABLE public.reconciliation_item;
       public         postgres    false            ?           1259    25632    reconciliation_item_id_seq    SEQUENCE     ?   CREATE SEQUENCE public.reconciliation_item_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 1   DROP SEQUENCE public.reconciliation_item_id_seq;
       public       postgres    false    392                       0    0    reconciliation_item_id_seq    SEQUENCE OWNED BY     Y   ALTER SEQUENCE public.reconciliation_item_id_seq OWNED BY public.reconciliation_item.id;
            public       postgres    false    391            ?           1259    25645    recurring_transaction    TABLE     ?  CREATE TABLE public.recurring_transaction (
    id bigint NOT NULL,
    company_id bigint,
    occurrences_count integer,
    occurrences_completed integer,
    interval_type integer,
    interval_period integer,
    due_date_type integer,
    due_date_value integer,
    action_type integer,
    name bytea,
    end_date_type integer,
    stopped boolean,
    start_date bigint,
    end_date bigint,
    next_schedule_on bigint,
    prev_schedule_on bigint,
    transaction_id bigint NOT NULL,
    created_by bigint NOT NULL,
    last_modifier bigint NOT NULL,
    created_date timestamp without time zone NOT NULL,
    last_modified_date timestamp without time zone NOT NULL,
    week_day integer,
    day_of_month integer,
    week_of_month integer,
    month_of_yr integer,
    unbilled_charges boolean,
    alert_when_ended boolean,
    notify_created_transaction boolean,
    days_in_advance integer,
    days_before_remind integer,
    recurring_type integer,
    status integer
);
 )   DROP TABLE public.recurring_transaction;
       public         postgres    false            ?           1259    25643    recurring_transaction_id_seq    SEQUENCE     ?   CREATE SEQUENCE public.recurring_transaction_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 3   DROP SEQUENCE public.recurring_transaction_id_seq;
       public       postgres    false    394                       0    0    recurring_transaction_id_seq    SEQUENCE OWNED BY     ]   ALTER SEQUENCE public.recurring_transaction_id_seq OWNED BY public.recurring_transaction.id;
            public       postgres    false    393            ?           1259    25658    remember_me_key    TABLE     ?   CREATE TABLE public.remember_me_key (
    id bigint NOT NULL,
    remember_key character varying(255),
    email_id character varying(255),
    client_key character varying(255),
    server_key bytea
);
 #   DROP TABLE public.remember_me_key;
       public         postgres    false            ?           1259    25656    remember_me_key_id_seq    SEQUENCE        CREATE SEQUENCE public.remember_me_key_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 -   DROP SEQUENCE public.remember_me_key_id_seq;
       public       postgres    false    396                       0    0    remember_me_key_id_seq    SEQUENCE OWNED BY     Q   ALTER SEQUENCE public.remember_me_key_id_seq OWNED BY public.remember_me_key.id;
            public       postgres    false    395            ?           1259    25669    reminder    TABLE     v  CREATE TABLE public.reminder (
    id bigint NOT NULL,
    company_id bigint NOT NULL,
    recurring_transaction bigint,
    transaction_date bigint,
    is_valid boolean,
    version integer,
    created_by bigint NOT NULL,
    last_modifier bigint NOT NULL,
    created_on timestamp without time zone NOT NULL,
    last_modified_on timestamp without time zone NOT NULL
);
    DROP TABLE public.reminder;
       public         postgres    false            ?           1259    25667    reminder_id_seq    SEQUENCE     x   CREATE SEQUENCE public.reminder_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 &   DROP SEQUENCE public.reminder_id_seq;
       public       postgres    false    398                       0    0    reminder_id_seq    SEQUENCE OWNED BY     C   ALTER SEQUENCE public.reminder_id_seq OWNED BY public.reminder.id;
            public       postgres    false    397            ?           1259    25677    reset_password_token    TABLE     {   CREATE TABLE public.reset_password_token (
    id bigint NOT NULL,
    token character varying(255),
    user_id bigint
);
 (   DROP TABLE public.reset_password_token;
       public         postgres    false            ?           1259    25675    reset_password_token_id_seq    SEQUENCE     ?   CREATE SEQUENCE public.reset_password_token_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 2   DROP SEQUENCE public.reset_password_token_id_seq;
       public       postgres    false    400                       0    0    reset_password_token_id_seq    SEQUENCE OWNED BY     [   ALTER SEQUENCE public.reset_password_token_id_seq OWNED BY public.reset_password_token.id;
            public       postgres    false    399            ?           1259    25685    sales_person    TABLE     ?  CREATE TABLE public.sales_person (
    id bigint NOT NULL,
    company_id bigint NOT NULL,
    title bytea,
    first_name bytea,
    middle_name1 bytea,
    middle_name2 bytea,
    middle_name3 bytea,
    last_name bytea,
    version integer,
    suffix bytea,
    job_title bytea,
    expense_account_id bigint,
    gender bytea,
    date_of_birth bigint,
    date_of_hire bigint,
    date_of_lastreview bigint,
    date_of_release bigint,
    is_active boolean,
    memo bytea,
    file_as bytea,
    phone_no bytea,
    fax_no bytea,
    email bytea,
    web_page_address bytea,
    type integer,
    address1 bytea,
    street bytea,
    city bytea,
    state bytea,
    zip bytea,
    country bytea,
    is_selected boolean,
    created_by bigint NOT NULL,
    last_modified_by bigint NOT NULL,
    created_on timestamp without time zone NOT NULL,
    last_modified_on timestamp without time zone NOT NULL
);
     DROP TABLE public.sales_person;
       public         postgres    false            ?           1259    25683    sales_person_id_seq    SEQUENCE     |   CREATE SEQUENCE public.sales_person_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 *   DROP SEQUENCE public.sales_person_id_seq;
       public       postgres    false    402                       0    0    sales_person_id_seq    SEQUENCE OWNED BY     K   ALTER SEQUENCE public.sales_person_id_seq OWNED BY public.sales_person.id;
            public       postgres    false    401            ?           1259    26102    vendor_group    TABLE     Z  CREATE TABLE public.vendor_group (
    id bigint NOT NULL,
    company_id bigint NOT NULL,
    name bytea NOT NULL,
    version integer,
    created_by bigint NOT NULL,
    last_modifier bigint NOT NULL,
    created_date timestamp without time zone NOT NULL,
    last_modified_date timestamp without time zone NOT NULL,
    is_default boolean
);
     DROP TABLE public.vendor_group;
       public         postgres    false            ?           1259    28876    sales_purchases_view    VIEW     ?  CREATE VIEW public.sales_purchases_view AS
 SELECT t.company_id,
    t.id AS transaction_id,
    c.id AS customer_id,
    v.id AS vendor_id,
    p.name,
        CASE
            WHEN ((t.t_type = 1) OR (t.t_type = 4) OR (t.t_type = 8)) THEN cg.name
            WHEN ((t.t_type = 2) OR (t.t_type = 6) OR (t.t_type = 14)) THEN vg.name
            ELSE NULL::bytea
        END AS group_name,
    it.type AS item_type,
    ig.name AS item_group,
    t.t_type AS type,
    t.t_date AS date,
    t.number,
    t.memo,
        CASE
            WHEN (t.t_type = 8) THEN i.due_date
            WHEN (t.t_type = 6) THEN eb.due_date
            ELSE NULL::bigint
        END AS due_date,
    pt.name AS payment_terms,
        CASE
            WHEN (ti.item_id IS NOT NULL) THEN it.name
            WHEN (ti.account_id IS NOT NULL) THEN a.name
            ELSE NULL::bytea
        END AS item_name,
        CASE
            WHEN ((t.t_type = 4) OR (t.t_type = 14)) THEN (('-1'::integer)::double precision * ti.qty_value)
            ELSE ti.qty_value
        END AS quantity,
    ti.qty_unit AS unit,
    (ti.unit_price * t.currency_factor) AS unit_price,
    ti.discount,
        CASE
            WHEN ((t.t_type = 4) OR (t.t_type = 14)) THEN ((('-1'::integer)::double precision * ti.line_total) * t.currency_factor)
            ELSE (ti.line_total * t.currency_factor)
        END AS amount,
        CASE
            WHEN (t.t_type = 8) THEN t.number
            ELSE NULL::bytea
        END AS so_or_quote_no,
        CASE
            WHEN (t.t_type = 1) THEN cs.delivery_date
            WHEN (t.t_type = 8) THEN i.delivery_date
            WHEN (t.t_type = 2) THEN cp.delivery_date
            WHEN (t.t_type = 6) THEN eb.delivery_date
            ELSE NULL::bigint
        END AS shipment_or_delivery_date,
    it.sales_description,
    it.purchase_description,
    t.save_status,
    t.reference,
    t.currency_factor,
    it.parent_id,
    it.depth,
    it.path
   FROM ((((((((((((((((public.transaction t
     LEFT JOIN public.cash_sales cs ON ((t.id = cs.id)))
     LEFT JOIN public.customer_credit_memo ccm ON ((t.id = ccm.id)))
     LEFT JOIN public.invoice i ON ((t.id = i.id)))
     LEFT JOIN public.cash_purchase cp ON ((t.id = cp.id)))
     LEFT JOIN public.vendor_credit_memo vcm ON ((t.id = vcm.id)))
     LEFT JOIN public.enter_bill eb ON ((t.id = eb.id)))
     LEFT JOIN public.transaction_item ti ON ((t.id = ti.transaction_id)))
     LEFT JOIN public.customer c ON (((c.id = cs.customer_id) OR (c.id = ccm.customer_id) OR (c.id = i.customer_id))))
     LEFT JOIN public.vendor v ON (((v.id = cp.vendor_id) OR (v.id = vcm.vendor_id) OR (v.id = eb.vendor_id))))
     LEFT JOIN public.payee p ON (((p.id = c.id) OR (p.id = v.id))))
     LEFT JOIN public.paymentterms pt ON (((pt.id = eb.payment_term_id) OR (pt.id = i.payment_terms_id))))
     LEFT JOIN public.customer_group cg ON ((cg.id = c.customer_group_id)))
     LEFT JOIN public.vendor_group vg ON ((vg.id = v.vendor_group_id)))
     LEFT JOIN public.item it ON ((it.id = ti.item_id)))
     LEFT JOIN public.itemgroup ig ON ((ig.id = it.itemgroup_id)))
     LEFT JOIN public.account a ON ((a.id = ti.account_id)))
  WHERE ((t.save_status = 203) AND ((ti.item_id IS NOT NULL) OR (ti.account_id IS NOT NULL)))
  ORDER BY t.id;
 '   DROP VIEW public.sales_purchases_view;
       public       postgres    false    443    483    483    481    481    480    480    453    453    453    453    453    453    453    453    443    443    443    443    443    443    443    443    362    362    353    353    317    317    315    315    315    315    315    315    315    315    315    312    312    312    312    312    288    288    288    288    288    264    264    262    262    261    261    236    236    236    233    233    233    197    197            ?           1259    25696    server_maintanance    TABLE     e   CREATE TABLE public.server_maintanance (
    id bigint NOT NULL,
    is_under_maintanance boolean
);
 &   DROP TABLE public.server_maintanance;
       public         postgres    false            ?           1259    25694    server_maintanance_id_seq    SEQUENCE     ?   CREATE SEQUENCE public.server_maintanance_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 0   DROP SEQUENCE public.server_maintanance_id_seq;
       public       postgres    false    404                       0    0    server_maintanance_id_seq    SEQUENCE OWNED BY     W   ALTER SEQUENCE public.server_maintanance_id_seq OWNED BY public.server_maintanance.id;
            public       postgres    false    403            ?           1259    25704    shippingmethod    TABLE     s  CREATE TABLE public.shippingmethod (
    id bigint NOT NULL,
    company_id bigint NOT NULL,
    name bytea NOT NULL,
    description bytea,
    version integer,
    created_by bigint NOT NULL,
    last_modifier bigint NOT NULL,
    created_date timestamp without time zone NOT NULL,
    last_modified_date timestamp without time zone NOT NULL,
    is_default boolean
);
 "   DROP TABLE public.shippingmethod;
       public         postgres    false            ?           1259    25702    shippingmethod_id_seq    SEQUENCE     ~   CREATE SEQUENCE public.shippingmethod_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 ,   DROP SEQUENCE public.shippingmethod_id_seq;
       public       postgres    false    406                       0    0    shippingmethod_id_seq    SEQUENCE OWNED BY     O   ALTER SEQUENCE public.shippingmethod_id_seq OWNED BY public.shippingmethod.id;
            public       postgres    false    405            ?           1259    25715    shippingterms    TABLE     r  CREATE TABLE public.shippingterms (
    id bigint NOT NULL,
    company_id bigint NOT NULL,
    name bytea NOT NULL,
    description bytea,
    version integer,
    created_by bigint NOT NULL,
    last_modifier bigint NOT NULL,
    created_date timestamp without time zone NOT NULL,
    last_modified_date timestamp without time zone NOT NULL,
    is_default boolean
);
 !   DROP TABLE public.shippingterms;
       public         postgres    false            ?           1259    25713    shippingterms_id_seq    SEQUENCE     }   CREATE SEQUENCE public.shippingterms_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 +   DROP SEQUENCE public.shippingterms_id_seq;
       public       postgres    false    408                       0    0    shippingterms_id_seq    SEQUENCE OWNED BY     M   ALTER SEQUENCE public.shippingterms_id_seq OWNED BY public.shippingterms.id;
            public       postgres    false    407            ?           1259    25726 	   statement    TABLE     >  CREATE TABLE public.statement (
    id bigint NOT NULL,
    company_id bigint NOT NULL,
    importted_date bigint,
    start_date bigint,
    end_date bigint,
    starting_balance double precision,
    closing_balance double precision,
    is_reconciled boolean,
    account_id bigint,
    reconciliation_id bigint
);
    DROP TABLE public.statement;
       public         postgres    false            ?           1259    25724    statement_id_seq    SEQUENCE     y   CREATE SEQUENCE public.statement_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 '   DROP SEQUENCE public.statement_id_seq;
       public       postgres    false    410                       0    0    statement_id_seq    SEQUENCE OWNED BY     E   ALTER SEQUENCE public.statement_id_seq OWNED BY public.statement.id;
            public       postgres    false    409            ?           1259    25734    statement_record    TABLE     V  CREATE TABLE public.statement_record (
    id bigint NOT NULL,
    company_id bigint,
    statement_date bigint,
    description bytea,
    reference_number bytea,
    spent_amount double precision,
    receive_amount double precision,
    closing_balance double precision,
    is_matched boolean,
    statement_id bigint,
    idx integer
);
 $   DROP TABLE public.statement_record;
       public         postgres    false            ?           1259    25732    statement_record_id_seq    SEQUENCE     ?   CREATE SEQUENCE public.statement_record_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 .   DROP SEQUENCE public.statement_record_id_seq;
       public       postgres    false    412                       0    0    statement_record_id_seq    SEQUENCE OWNED BY     S   ALTER SEQUENCE public.statement_record_id_seq OWNED BY public.statement_record.id;
            public       postgres    false    411            ?           1259    25743    stock_adjustment    TABLE     [  CREATE TABLE public.stock_adjustment (
    id bigint NOT NULL,
    company_id bigint NOT NULL,
    warehouse bigint NOT NULL,
    adjustment_account bigint NOT NULL,
    created_by bigint NOT NULL,
    last_modifier bigint NOT NULL,
    created_on timestamp without time zone NOT NULL,
    last_modified_on timestamp without time zone NOT NULL
);
 $   DROP TABLE public.stock_adjustment;
       public         postgres    false            ?           1259    25750    stock_transfer_item    TABLE     ?   CREATE TABLE public.stock_transfer_item (
    id bigint NOT NULL,
    company_id bigint NOT NULL,
    item bigint,
    qty_value double precision,
    qty_unit bigint,
    warehouse_transfer bigint
);
 '   DROP TABLE public.stock_transfer_item;
       public         postgres    false            ?           1259    25748    stock_transfer_item_id_seq    SEQUENCE     ?   CREATE SEQUENCE public.stock_transfer_item_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 1   DROP SEQUENCE public.stock_transfer_item_id_seq;
       public       postgres    false    415                        0    0    stock_transfer_item_id_seq    SEQUENCE OWNED BY     Y   ALTER SEQUENCE public.stock_transfer_item_id_seq OWNED BY public.stock_transfer_item.id;
            public       postgres    false    414            ?           1259    25758    subscription    TABLE     O   CREATE TABLE public.subscription (
    id bigint NOT NULL,
    type integer
);
     DROP TABLE public.subscription;
       public         postgres    false            ?           1259    25756    subscription_id_seq    SEQUENCE     |   CREATE SEQUENCE public.subscription_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 *   DROP SEQUENCE public.subscription_id_seq;
       public       postgres    false    417            !           0    0    subscription_id_seq    SEQUENCE OWNED BY     K   ALTER SEQUENCE public.subscription_id_seq OWNED BY public.subscription.id;
            public       postgres    false    416            ?           1259    25764    supported_user    TABLE     U   CREATE TABLE public.supported_user (
    email_id character varying(255) NOT NULL
);
 "   DROP TABLE public.supported_user;
       public         postgres    false            ?           1259    25782    tax_adjustment    TABLE       CREATE TABLE public.tax_adjustment (
    id bigint NOT NULL,
    increase_vat_line boolean,
    is_filed boolean,
    is_sales boolean,
    balance_due double precision,
    adjustment_account bigint,
    tax_item bigint,
    tax_agency_id bigint,
    company_id bigint
);
 "   DROP TABLE public.tax_adjustment;
       public         postgres    false            ?           1259    25789    tax_code    TABLE     r  CREATE TABLE public.tax_code (
    id bigint NOT NULL,
    company_id bigint NOT NULL,
    version integer,
    name bytea NOT NULL,
    sales_tax_rate double precision,
    purchase_tax_rate double precision,
    description bytea,
    istaxable boolean,
    isactive boolean,
    is_default boolean,
    taxitemgroup_purchases bigint,
    taxitemgroup_sales bigint
);
    DROP TABLE public.tax_code;
       public         postgres    false            ?           1259    25787    tax_code_id_seq    SEQUENCE     x   CREATE SEQUENCE public.tax_code_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 &   DROP SEQUENCE public.tax_code_id_seq;
       public       postgres    false    424            "           0    0    tax_code_id_seq    SEQUENCE OWNED BY     C   ALTER SEQUENCE public.tax_code_id_seq OWNED BY public.tax_code.id;
            public       postgres    false    423            ?           1259    46490    tax_computation_pay_head    TABLE     c   CREATE TABLE public.tax_computation_pay_head (
    id bigint NOT NULL,
    live_tax_rate bigint
);
 ,   DROP TABLE public.tax_computation_pay_head;
       public         postgres    false            ?           1259    25798 	   tax_group    TABLE     {   CREATE TABLE public.tax_group (
    id bigint NOT NULL,
    company_id bigint NOT NULL,
    group_rate double precision
);
    DROP TABLE public.tax_group;
       public         postgres    false            ?           1259    25803    tax_group_tax_item    TABLE     ?   CREATE TABLE public.tax_group_tax_item (
    tax_item_id bigint NOT NULL,
    tax_group_id bigint NOT NULL,
    vtx integer NOT NULL
);
 &   DROP TABLE public.tax_group_tax_item;
       public         postgres    false            ?           1259    25808    tax_item    TABLE     ?   CREATE TABLE public.tax_item (
    id bigint NOT NULL,
    company_id bigint NOT NULL,
    tax_agency bigint NOT NULL,
    tax_rate double precision,
    vat_return_box bigint
);
    DROP TABLE public.tax_item;
       public         postgres    false            ?           1259    25815    tax_item_groups    TABLE     ?  CREATE TABLE public.tax_item_groups (
    id bigint NOT NULL,
    company_id bigint NOT NULL,
    name bytea NOT NULL,
    version integer,
    description bytea,
    is_active boolean,
    created_by bigint NOT NULL,
    last_modifier bigint NOT NULL,
    created_date timestamp without time zone NOT NULL,
    last_modified_date timestamp without time zone NOT NULL,
    is_percentage boolean,
    is_default boolean
);
 #   DROP TABLE public.tax_item_groups;
       public         postgres    false            ?           1259    25813    tax_item_groups_id_seq    SEQUENCE        CREATE SEQUENCE public.tax_item_groups_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 -   DROP SEQUENCE public.tax_item_groups_id_seq;
       public       postgres    false    429            #           0    0    tax_item_groups_id_seq    SEQUENCE OWNED BY     Q   ALTER SEQUENCE public.tax_item_groups_id_seq OWNED BY public.tax_item_groups.id;
            public       postgres    false    428            ?           1259    25826    tax_rate_calculation    TABLE     ?  CREATE TABLE public.tax_rate_calculation (
    id bigint NOT NULL,
    vat_group_entry boolean,
    vat_amount double precision,
    line_total double precision,
    transaction_id bigint,
    tax_item_id bigint,
    transaction_date bigint,
    tax_agency_id bigint,
    rate double precision,
    vat_return_box_id bigint,
    purchase_liability_account_id bigint,
    sales_liability_account_id bigint,
    tax_return bigint
);
 (   DROP TABLE public.tax_rate_calculation;
       public         postgres    false            ?           1259    25824    tax_rate_calculation_id_seq    SEQUENCE     ?   CREATE SEQUENCE public.tax_rate_calculation_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 2   DROP SEQUENCE public.tax_rate_calculation_id_seq;
       public       postgres    false    431            $           0    0    tax_rate_calculation_id_seq    SEQUENCE OWNED BY     [   ALTER SEQUENCE public.tax_rate_calculation_id_seq OWNED BY public.tax_rate_calculation.id;
            public       postgres    false    430            ?           1259    25832 
   tax_return    TABLE     ?   CREATE TABLE public.tax_return (
    id bigint NOT NULL,
    start_date bigint,
    end_date bigint,
    tax_agency bigint,
    balance double precision,
    tax_total double precision,
    company_id bigint
);
    DROP TABLE public.tax_return;
       public         postgres    false            ?           1259    25839    tax_return_entry    TABLE     ?  CREATE TABLE public.tax_return_entry (
    id bigint NOT NULL,
    transaction_id bigint,
    taxitem_id bigint,
    taxagency_id bigint,
    tax_return_id bigint,
    grass_amount double precision,
    net_amount double precision,
    tax_amount double precision,
    version integer,
    is_tax_group_entry boolean,
    transaction_date bigint,
    transaction_type integer,
    transaction_category integer,
    tei integer
);
 $   DROP TABLE public.tax_return_entry;
       public         postgres    false            ?           1259    25837    tax_return_entry_id_seq    SEQUENCE     ?   CREATE SEQUENCE public.tax_return_entry_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 .   DROP SEQUENCE public.tax_return_entry_id_seq;
       public       postgres    false    434            %           0    0    tax_return_entry_id_seq    SEQUENCE OWNED BY     S   ALTER SEQUENCE public.tax_return_entry_id_seq OWNED BY public.tax_return_entry.id;
            public       postgres    false    433            ?           1259    25769 	   taxagency    TABLE     E  CREATE TABLE public.taxagency (
    id bigint NOT NULL,
    company_id bigint NOT NULL,
    payment_term bigint,
    sales_account_id bigint,
    purchase_account_id bigint,
    filed_liability_account bigint,
    vat_return integer,
    tax_type integer,
    tax_filing_frequency integer,
    last_tax_return_date bigint
);
    DROP TABLE public.taxagency;
       public         postgres    false            ?           1259    25776    taxrates    TABLE     ?   CREATE TABLE public.taxrates (
    id bigint NOT NULL,
    rate double precision,
    as_of bigint,
    version integer,
    company_id bigint
);
    DROP TABLE public.taxrates;
       public         postgres    false            ?           1259    25774    taxrates_id_seq    SEQUENCE     x   CREATE SEQUENCE public.taxrates_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 &   DROP SEQUENCE public.taxrates_id_seq;
       public       postgres    false    421            &           0    0    taxrates_id_seq    SEQUENCE OWNED BY     C   ALTER SEQUENCE public.taxrates_id_seq OWNED BY public.taxrates.id;
            public       postgres    false    420            ?           1259    25878    tds_chalan_detail    TABLE     ?  CREATE TABLE public.tds_chalan_detail (
    id bigint NOT NULL,
    payfrom_id bigint,
    incometaxamount double precision,
    surchangepaidamount double precision,
    educationcessamount double precision,
    interestpaidamount double precision,
    penaltypaidamount double precision,
    otheramount double precision,
    paymentsection bytea,
    bankchalannumber bigint,
    checknumber bigint,
    bankbsrcode bytea,
    bookentry boolean,
    datetaxpaid bigint,
    chalanperiod integer,
    chalanserialnumber bigint,
    formtype integer,
    assesmentyearstart integer,
    assesmentyearend integer,
    isfiled boolean,
    etdsfillingacknowledgementno bytea,
    acknowledgementdate bigint,
    fromdate bigint,
    todate bigint
);
 %   DROP TABLE public.tds_chalan_detail;
       public         postgres    false            ?           1259    25847    tdsdeductormasters    TABLE     <  CREATE TABLE public.tdsdeductormasters (
    id bigint NOT NULL,
    company_id bigint NOT NULL,
    deductorname bytea,
    branch bytea,
    flatno bytea,
    buildingname bytea,
    roadname bytea,
    area bytea,
    city bytea,
    state bytea,
    pincode bigint,
    addressdchanged boolean,
    telephonenumber bigint,
    faxno bigint,
    emailid bytea,
    status bytea,
    deductortype bytea,
    govtstate bytea,
    paocode bytea,
    paoregistration bigint,
    ddocode bytea,
    ddoregistration bytea,
    ministrydeptname bytea,
    ministrydeptothername bytea,
    tannumber bytea,
    pannumber bytea,
    stdcode bytea,
    isaddresssameforresopsibleperson boolean,
    taxoffice_address_type integer,
    taxoffice_address_address1 character varying(100),
    taxoffice_address_street character varying(255),
    taxoffice_address_city character varying(255),
    taxoffice_address_state character varying(255),
    taxoffice_address_zip character varying(255),
    taxoffice_address_country character varying(255),
    taxoffice_address_is_selected boolean
);
 &   DROP TABLE public.tdsdeductormasters;
       public         postgres    false            ?           1259    25845    tdsdeductormasters_id_seq    SEQUENCE     ?   CREATE SEQUENCE public.tdsdeductormasters_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 0   DROP SEQUENCE public.tdsdeductormasters_id_seq;
       public       postgres    false    436            '           0    0    tdsdeductormasters_id_seq    SEQUENCE OWNED BY     W   ALTER SEQUENCE public.tdsdeductormasters_id_seq OWNED BY public.tdsdeductormasters.id;
            public       postgres    false    435            ?           1259    25858    tdsresponsibleperson    TABLE     g  CREATE TABLE public.tdsresponsibleperson (
    id bigint NOT NULL,
    company_id bigint NOT NULL,
    responsiblename bytea,
    designation bytea NOT NULL,
    branch bytea,
    flatno bytea,
    buildingname bytea,
    street bytea,
    area bytea,
    city bytea,
    statename bytea,
    pincode bigint,
    addresschanged boolean,
    telephonenumber bigint,
    faxno bigint,
    emailaddress bytea,
    financialyear bytea,
    assesmentyear bytea,
    returntype integer,
    existingtdsassesse boolean,
    pannumber bytea,
    panregistrationnumber bigint,
    mobilenumber bigint,
    stdcode bigint
);
 (   DROP TABLE public.tdsresponsibleperson;
       public         postgres    false            ?           1259    25856    tdsresponsibleperson_id_seq    SEQUENCE     ?   CREATE SEQUENCE public.tdsresponsibleperson_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 2   DROP SEQUENCE public.tdsresponsibleperson_id_seq;
       public       postgres    false    438            (           0    0    tdsresponsibleperson_id_seq    SEQUENCE OWNED BY     [   ALTER SEQUENCE public.tdsresponsibleperson_id_seq OWNED BY public.tdsresponsibleperson.id;
            public       postgres    false    437            ?           1259    25869    tdstransactionitem    TABLE     ?  CREATE TABLE public.tdstransactionitem (
    id bigint NOT NULL,
    vendor_id bigint,
    total_amount double precision,
    tds_amount double precision,
    surcharge_amount double precision,
    edu_cess double precision,
    total_tax double precision,
    tax_rate double precision,
    transaction_date bigint,
    transaction_id bigint,
    deductee_code integer,
    remark bytea,
    tds_challandetail_id bigint,
    idx integer
);
 &   DROP TABLE public.tdstransactionitem;
       public         postgres    false            ?           1259    25867    tdstransactionitem_id_seq    SEQUENCE     ?   CREATE SEQUENCE public.tdstransactionitem_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 0   DROP SEQUENCE public.tdstransactionitem_id_seq;
       public       postgres    false    440            )           0    0    tdstransactionitem_id_seq    SEQUENCE OWNED BY     W   ALTER SEQUENCE public.tdstransactionitem_id_seq OWNED BY public.tdstransactionitem.id;
            public       postgres    false    439            ?           1259    25899     transaction_credits_and_payments    TABLE     M  CREATE TABLE public.transaction_credits_and_payments (
    id bigint NOT NULL,
    date bigint,
    memo bytea,
    amount_to_use double precision,
    transaction_receive_payment_id bigint,
    transaction_paybill_id bigint,
    credits_and_payments_id bigint,
    is_void boolean,
    version integer DEFAULT 0,
    idx integer
);
 4   DROP TABLE public.transaction_credits_and_payments;
       public         postgres    false            ?           1259    25897 '   transaction_credits_and_payments_id_seq    SEQUENCE     ?   CREATE SEQUENCE public.transaction_credits_and_payments_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 >   DROP SEQUENCE public.transaction_credits_and_payments_id_seq;
       public       postgres    false    445            *           0    0 '   transaction_credits_and_payments_id_seq    SEQUENCE OWNED BY     s   ALTER SEQUENCE public.transaction_credits_and_payments_id_seq OWNED BY public.transaction_credits_and_payments.id;
            public       postgres    false    444            ?           1259    25911    transaction_deposit_item    TABLE     ?  CREATE TABLE public.transaction_deposit_item (
    id bigint NOT NULL,
    version integer,
    received_from_id bigint,
    account_id bigint,
    total double precision,
    refernce_no bytea,
    customer_id bigint,
    job_id bigint,
    is_billable boolean,
    transaction_id bigint,
    description bytea,
    trans_item_accounter_class bigint,
    payment_method bytea,
    make_deposit_id bigint,
    idx integer
);
 ,   DROP TABLE public.transaction_deposit_item;
       public         postgres    false            ?           1259    25909    transaction_deposit_item_id_seq    SEQUENCE     ?   CREATE SEQUENCE public.transaction_deposit_item_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 6   DROP SEQUENCE public.transaction_deposit_item_id_seq;
       public       postgres    false    447            +           0    0    transaction_deposit_item_id_seq    SEQUENCE OWNED BY     c   ALTER SEQUENCE public.transaction_deposit_item_id_seq OWNED BY public.transaction_deposit_item.id;
            public       postgres    false    446            ?           1259    25922    transaction_expense    TABLE       CREATE TABLE public.transaction_expense (
    id bigint NOT NULL,
    type integer,
    item_id bigint,
    account_id bigint,
    description bytea,
    quantity double precision,
    unit_price double precision,
    amount double precision,
    expense_id bigint,
    idx integer
);
 '   DROP TABLE public.transaction_expense;
       public         postgres    false            ?           1259    25920    transaction_expense_id_seq    SEQUENCE     ?   CREATE SEQUENCE public.transaction_expense_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 1   DROP SEQUENCE public.transaction_expense_id_seq;
       public       postgres    false    449            ,           0    0    transaction_expense_id_seq    SEQUENCE OWNED BY     Y   ALTER SEQUENCE public.transaction_expense_id_seq OWNED BY public.transaction_expense.id;
            public       postgres    false    448            ?           1259    25933    transaction_history    TABLE     ?   CREATE TABLE public.transaction_history (
    id bigint NOT NULL,
    company_id bigint,
    type integer,
    user_name bytea,
    "time" timestamp without time zone,
    description bytea,
    transaction_id bigint,
    idx integer
);
 '   DROP TABLE public.transaction_history;
       public         postgres    false            ?           1259    25931    transaction_history_id_seq    SEQUENCE     ?   CREATE SEQUENCE public.transaction_history_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 1   DROP SEQUENCE public.transaction_history_id_seq;
       public       postgres    false    451            -           0    0    transaction_history_id_seq    SEQUENCE OWNED BY     Y   ALTER SEQUENCE public.transaction_history_id_seq OWNED BY public.transaction_history.id;
            public       postgres    false    450            ?           1259    25963    transaction_paybill    TABLE     -  CREATE TABLE public.transaction_paybill (
    id bigint NOT NULL,
    company_id bigint,
    version integer,
    due_date bigint,
    enter_bill_id bigint,
    original_amount double precision,
    amount_due double precision,
    discount_date bigint,
    discount_id bigint,
    cash_discount double precision,
    applied_credits double precision,
    payment double precision,
    tds_amount double precision,
    paybill_id bigint,
    is_void boolean,
    bill_number bytea,
    journal_entry_id bigint,
    transaction_id bigint,
    idx integer
);
 '   DROP TABLE public.transaction_paybill;
       public         postgres    false            ?           1259    26015    transfer_fund    TABLE     ?   CREATE TABLE public.transfer_fund (
    id bigint NOT NULL,
    deposit_in_id bigint,
    deposit_from_id bigint,
    cash_back_account_id bigint,
    cash_back_memo bytea,
    cash_back_amount double precision
);
 !   DROP TABLE public.transfer_fund;
       public         postgres    false            ?           1259    26111    vendor_payment    TABLE       CREATE TABLE public.vendor_payment (
    id bigint NOT NULL,
    payfrom_id bigint,
    vendor_id bigint,
    tds_taxitem bigint,
    tds_total double precision,
    is_to_be_printed boolean,
    ending_balance double precision,
    unused_amount double precision,
    vendor_balance double precision,
    paybill_address_type integer,
    paybill_address_address1 bytea,
    paybill_address_street bytea,
    paybill_address_city bytea,
    paybill_address_state bytea,
    paybill_address_zip bytea,
    paybill_address_country bytea,
    paybill_address_is_selected boolean,
    check_number bytea,
    is_amount_include_tds boolean
);
 "   DROP TABLE public.vendor_payment;
       public         postgres    false            ?           1259    26143    write_checks    TABLE     e  CREATE TABLE public.write_checks (
    id bigint NOT NULL,
    pay_to_type integer,
    in_favour_of bytea,
    account_id bigint,
    customer_id bigint,
    vendor_id bigint,
    tax_agency_id bigint,
    writecheck_address_type integer,
    writecheck_address_address1 bytea,
    writecheck_address_street bytea,
    wcheck_address_city bytea,
    wcheck_address_state bytea,
    wcheck_address_zip bytea,
    wcheck_address_country bytea,
    wcheck_address_is_selected boolean,
    amount double precision,
    to_be_printed boolean,
    sales_person_id bigint,
    in_words bytea,
    check_number bytea
);
     DROP TABLE public.write_checks;
       public         postgres    false            ?           1259    28881    transaction_history_view    VIEW     ?  CREATE VIEW public.transaction_history_view AS
 SELECT t.company_id,
    t.id AS transaction_id,
    c.id AS customer_id,
    v.id AS vendor_id,
        CASE
            WHEN ((t.t_type = 1) OR (t.t_type = 4) OR (t.t_type = 5) OR (t.t_type = 8) OR (t.t_type = 12) OR (t.t_type = 10) OR ((t.t_type = 15) AND (wc.customer_id IS NOT NULL)) OR ((t.t_type = 16) AND (p.type = 1)) OR (t.t_type = 29)) THEN p.name
            WHEN ((t.t_type = 2) OR (t.t_type = 6) OR (t.t_type = 11) OR (t.t_type = 25) OR (t.t_type = 14) OR (t.t_type = 3) OR (t.t_type = 10) OR ((t.t_type = 15) AND (wc.vendor_id IS NOT NULL)) OR ((t.t_type = 16) AND (p.type = 2)) OR (t.t_type = 26) OR (t.t_type = 27)) THEN p.name
            ELSE NULL::bytea
        END AS name,
    t.t_type AS type,
    t.t_date AS date,
    t.number,
        CASE
            WHEN ((t.t_type = 1) OR (t.t_type = 3) OR (t.t_type = 8) OR (t.t_type = 2) OR (t.t_type = 6) OR (t.t_type = 27) OR (t.t_type = 10)) THEN (t.total * t.currency_factor)
            WHEN ((t.t_type = 4) OR (t.t_type = 14) OR (t.t_type = 29)) THEN ((('-1'::integer)::double precision * t.total) * t.currency_factor)
            WHEN (t.t_type = 15) THEN
            CASE
                WHEN (wc.pay_to_type = 1) THEN ((('-1'::integer)::double precision * t.total) * t.currency_factor)
                WHEN (wc.pay_to_type = 2) THEN (t.total * t.currency_factor)
                ELSE (0.0)::double precision
            END
            WHEN (t.t_type = 16) THEN
            CASE
                WHEN (p.type = 2) THEN ((('-1'::integer)::double precision * t.total) * t.currency_factor)
                ELSE (t.total * t.currency_factor)
            END
            ELSE (0.0)::double precision
        END AS invoiced_or_transasction_amount,
        CASE
            WHEN ((t.t_type = 1) OR (t.t_type = 3) OR (t.t_type = 2) OR (t.t_type = 26) OR (t.t_type = 27)) THEN (t.total * t.currency_factor)
            WHEN ((t.t_type = 5) OR (t.t_type = 15) OR (t.t_type = 11) OR (t.t_type = 25) OR (t.t_type = 12)) THEN ((('-1'::integer)::double precision * t.total) * t.currency_factor)
            ELSE (0.0)::double precision
        END AS paid_amount,
        CASE
            WHEN ((t.t_type = 1) OR (t.t_type = 3) OR (t.t_type = 5) OR (t.t_type = 8) OR (t.t_type = 2) OR (t.t_type = 14) OR (t.t_type = 15) OR (t.t_type = 27)) THEN t.total
            WHEN (t.t_type = 11) THEN
            CASE
                WHEN (( SELECT sum((tpb_1.cash_discount * t.currency_factor)) AS sum
                   FROM public.transaction_paybill tpb_1
                  WHERE ((tpb_1.transaction_id = t.id) AND (tpb_1.discount_id IS NOT NULL) AND (tpb_1.cash_discount > (0.0)::double precision))) IS NULL) THEN t.total
                ELSE (t.total + ( SELECT sum((tpb_1.cash_discount * t.currency_factor)) AS sum
                   FROM public.transaction_paybill tpb_1
                  WHERE ((tpb_1.transaction_id = t.id) AND (tpb_1.discount_id IS NOT NULL) AND (tpb_1.cash_discount > (0.0)::double precision))))
            END
            WHEN (t.t_type = 25) THEN (t.total * t.currency_factor)
            WHEN (t.t_type = 16) THEN
            CASE
                WHEN (p.type = 1) THEN (t.total * t.currency_factor)
                ELSE (0.0)::double precision
            END
            ELSE (0.0)::double precision
        END AS debit,
        CASE
            WHEN ((t.t_type = 1) OR (t.t_type = 3) OR (t.t_type = 4) OR (t.t_type = 2) OR (t.t_type = 10) OR (t.t_type = 6) OR (t.t_type = 15) OR (t.t_type = 27) OR (t.t_type = 29)) THEN (t.total * t.currency_factor)
            WHEN (t.t_type = 12) THEN (((rp.amount + rp.total_cash_discount) + rp.total_write_off) * t.currency_factor)
            WHEN (t.t_type = 16) THEN
            CASE
                WHEN (p.type = 2) THEN ((('-1'::integer)::double precision * t.total) * t.currency_factor)
                ELSE (0.0)::double precision
            END
            ELSE (0.0)::double precision
        END AS credit,
        CASE
            WHEN (t.t_type = 12) THEN (rp.total_cash_discount * t.currency_factor)
            WHEN (t.t_type = 11) THEN ( SELECT sum((tpb_1.cash_discount * t.currency_factor)) AS sum
               FROM public.transaction_paybill tpb_1
              WHERE (tpb_1.transaction_id = t.id))
            ELSE (0.0)::double precision
        END AS discount,
        CASE
            WHEN (t.t_type = 8) THEN i.due_date
            WHEN (t.t_type = 6) THEN eb.due_date
            ELSE NULL::bigint
        END AS due_date,
    pt.name AS payment_terms,
    t.save_status,
    t.reference,
        CASE
            WHEN (t.t_type = 25) THEN NULL::bytea
            ELSE t.memo
        END AS memo,
        CASE
            WHEN (t.t_type = 12) THEN (rp.total_write_off * t.currency_factor)
            ELSE (0.0)::double precision
        END AS write_off,
    t.status AS payment_status
   FROM ((((((((((((((((((((public.transaction t
     LEFT JOIN public.cash_sales cs ON ((t.id = cs.id)))
     LEFT JOIN public.credit_card_charges ccc ON ((t.id = ccc.id)))
     LEFT JOIN public.customer_credit_memo ccm ON ((t.id = ccm.id)))
     LEFT JOIN public.invoice i ON ((t.id = i.id)))
     LEFT JOIN public.customer_refund cr ON ((t.id = cr.id)))
     LEFT JOIN public.receive_payment rp ON ((t.id = rp.id)))
     LEFT JOIN public.cash_purchase cp ON ((t.id = cp.id)))
     LEFT JOIN public.vendor_credit_memo vcm ON ((t.id = vcm.id)))
     LEFT JOIN public.enter_bill eb ON ((t.id = eb.id)))
     LEFT JOIN public.pay_bill pb ON ((t.id = pb.id)))
     LEFT JOIN public.transaction_paybill tpb ON ((tpb.transaction_id = pb.id)))
     LEFT JOIN public.vendor_payment vp ON ((t.id = vp.id)))
     LEFT JOIN public.transfer_fund tf ON ((t.id = tf.id)))
     LEFT JOIN public.write_checks wc ON ((t.id = wc.id)))
     LEFT JOIN public.journal_entry je ON (((je.id = t.id) AND (( SELECT py.type
           FROM public.payee py
          WHERE ((py.id = je.payee_id) AND (je.payee_id IS NOT NULL))) = ANY (ARRAY[2, 1])))))
     LEFT JOIN public.customer_prepayment cpp ON ((cpp.id = t.id)))
     LEFT JOIN public.customer c ON (((c.id = cs.customer_id) OR (c.id = ccm.customer_id) OR (c.id = i.customer_id) OR (c.id = cr.customer_id) OR (c.id = rp.customer_id) OR (c.id = wc.customer_id) OR (c.id = je.payee_id) OR (c.id = cpp.customer_id))))
     LEFT JOIN public.paymentterms pt ON (((pt.id = eb.payment_term_id) OR (pt.id = i.payment_terms_id))))
     LEFT JOIN public.vendor v ON (((v.id = cp.vendor_id) OR (v.id = vcm.vendor_id) OR (v.id = eb.vendor_id) OR (v.id = pb.vendor_id) OR (v.id = vp.vendor_id) OR (v.id = ccc.vendor_id) OR (v.id = wc.vendor_id) OR (v.id = je.payee_id))))
     LEFT JOIN public.payee p ON (((p.id = c.id) OR (p.id = v.id) OR (p.id = je.payee_id))))
  ORDER BY t.id;
 +   DROP VIEW public.transaction_history_view;
       public       postgres    false    484    312    312    312    325    481    258    261    262    262    265    484    490    490    265    266    266    288    490    490    288    288    288    312    325    353    353    353    362    362    365    365    387    387    387    387    387    443    443    443    443    443    443    443    233    233    443    443    443    443    236    457    457    457    236    258    468    480    481            ?           1259    25886    transaction_id_seq    SEQUENCE     {   CREATE SEQUENCE public.transaction_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 )   DROP SEQUENCE public.transaction_id_seq;
       public       postgres    false    443            .           0    0    transaction_id_seq    SEQUENCE OWNED BY     I   ALTER SEQUENCE public.transaction_id_seq OWNED BY public.transaction.id;
            public       postgres    false    442            ?           1259    25942    transaction_item_id_seq    SEQUENCE     ?   CREATE SEQUENCE public.transaction_item_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 .   DROP SEQUENCE public.transaction_item_id_seq;
       public       postgres    false    453            /           0    0    transaction_item_id_seq    SEQUENCE OWNED BY     S   ALTER SEQUENCE public.transaction_item_id_seq OWNED BY public.transaction_item.id;
            public       postgres    false    452            ?           1259    25955     transaction_make_deposit_entries    TABLE     ?   CREATE TABLE public.transaction_make_deposit_entries (
    id bigint NOT NULL,
    type integer,
    transaction_id bigint,
    account_id bigint,
    amount double precision,
    balance double precision
);
 4   DROP TABLE public.transaction_make_deposit_entries;
       public         postgres    false            ?           1259    25953 '   transaction_make_deposit_entries_id_seq    SEQUENCE     ?   CREATE SEQUENCE public.transaction_make_deposit_entries_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 >   DROP SEQUENCE public.transaction_make_deposit_entries_id_seq;
       public       postgres    false    455            0           0    0 '   transaction_make_deposit_entries_id_seq    SEQUENCE OWNED BY     s   ALTER SEQUENCE public.transaction_make_deposit_entries_id_seq OWNED BY public.transaction_make_deposit_entries.id;
            public       postgres    false    454            ?           1259    25974    transaction_pay_employee    TABLE     N  CREATE TABLE public.transaction_pay_employee (
    id bigint NOT NULL,
    company_id bigint,
    version integer,
    pay_run_id bigint,
    original_amount double precision,
    amount_due double precision,
    payment double precision,
    pay_employee_id bigint,
    is_void boolean,
    transaction_id bigint,
    idx integer
);
 ,   DROP TABLE public.transaction_pay_employee;
       public         postgres    false            ?           1259    25972    transaction_pay_employee_id_seq    SEQUENCE     ?   CREATE SEQUENCE public.transaction_pay_employee_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 6   DROP SEQUENCE public.transaction_pay_employee_id_seq;
       public       postgres    false    459            1           0    0    transaction_pay_employee_id_seq    SEQUENCE OWNED BY     c   ALTER SEQUENCE public.transaction_pay_employee_id_seq OWNED BY public.transaction_pay_employee.id;
            public       postgres    false    458            ?           1259    25982    transaction_pay_expense    TABLE     ?   CREATE TABLE public.transaction_pay_expense (
    id bigint NOT NULL,
    expense_id bigint,
    payment double precision,
    pay_expense_id bigint,
    idx integer
);
 +   DROP TABLE public.transaction_pay_expense;
       public         postgres    false            ?           1259    25980    transaction_pay_expense_id_seq    SEQUENCE     ?   CREATE SEQUENCE public.transaction_pay_expense_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 5   DROP SEQUENCE public.transaction_pay_expense_id_seq;
       public       postgres    false    461            2           0    0    transaction_pay_expense_id_seq    SEQUENCE OWNED BY     a   ALTER SEQUENCE public.transaction_pay_expense_id_seq OWNED BY public.transaction_pay_expense.id;
            public       postgres    false    460            ?           1259    25990    transaction_pay_tax    TABLE       CREATE TABLE public.transaction_pay_tax (
    id bigint NOT NULL,
    tax_agency_id bigint,
    amount_to_pay double precision,
    tax_due double precision,
    vat_return_id bigint,
    filed_date bigint,
    pay_tax_id bigint,
    version integer,
    idx integer
);
 '   DROP TABLE public.transaction_pay_tax;
       public         postgres    false            ?           1259    25988    transaction_pay_tax_id_seq    SEQUENCE     ?   CREATE SEQUENCE public.transaction_pay_tax_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 1   DROP SEQUENCE public.transaction_pay_tax_id_seq;
       public       postgres    false    463            3           0    0    transaction_pay_tax_id_seq    SEQUENCE OWNED BY     Y   ALTER SEQUENCE public.transaction_pay_tax_id_seq OWNED BY public.transaction_pay_tax.id;
            public       postgres    false    462            ?           1259    25961    transaction_paybill_id_seq    SEQUENCE     ?   CREATE SEQUENCE public.transaction_paybill_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 1   DROP SEQUENCE public.transaction_paybill_id_seq;
       public       postgres    false    457            4           0    0    transaction_paybill_id_seq    SEQUENCE OWNED BY     Y   ALTER SEQUENCE public.transaction_paybill_id_seq OWNED BY public.transaction_paybill.id;
            public       postgres    false    456            ?           1259    25998    transaction_receive_payment    TABLE       CREATE TABLE public.transaction_receive_payment (
    id bigint NOT NULL,
    version integer,
    due_date bigint,
    invoice_id bigint,
    invoice_amount double precision,
    discount_date bigint,
    discount_id bigint,
    cash_discount double precision,
    write_off_id bigint,
    write_off double precision,
    applied_credits double precision,
    payment double precision,
    transaction_id bigint,
    is_void boolean,
    customer_refund_id bigint,
    journal_entry_id bigint,
    number bytea,
    idx integer
);
 /   DROP TABLE public.transaction_receive_payment;
       public         postgres    false            ?           1259    25996 "   transaction_receive_payment_id_seq    SEQUENCE     ?   CREATE SEQUENCE public.transaction_receive_payment_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 9   DROP SEQUENCE public.transaction_receive_payment_id_seq;
       public       postgres    false    465            5           0    0 "   transaction_receive_payment_id_seq    SEQUENCE OWNED BY     i   ALTER SEQUENCE public.transaction_receive_payment_id_seq OWNED BY public.transaction_receive_payment.id;
            public       postgres    false    464            ?           1259    26009    transaction_receive_vat    TABLE       CREATE TABLE public.transaction_receive_vat (
    id bigint NOT NULL,
    tax_agency_id bigint,
    amount_to_receive double precision,
    tax_due double precision,
    tax_return_id bigint,
    receive_vat_id bigint,
    version integer,
    idx integer
);
 +   DROP TABLE public.transaction_receive_vat;
       public         postgres    false            ?           1259    26007    transaction_receive_vat_id_seq    SEQUENCE     ?   CREATE SEQUENCE public.transaction_receive_vat_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 5   DROP SEQUENCE public.transaction_receive_vat_id_seq;
       public       postgres    false    467            6           0    0    transaction_receive_vat_id_seq    SEQUENCE OWNED BY     a   ALTER SEQUENCE public.transaction_receive_vat_id_seq OWNED BY public.transaction_receive_vat.id;
            public       postgres    false    466            ?           1259    26023    unit_id_seq    SEQUENCE     t   CREATE SEQUENCE public.unit_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 "   DROP SEQUENCE public.unit_id_seq;
       public       postgres    false    470            7           0    0    unit_id_seq    SEQUENCE OWNED BY     ;   ALTER SEQUENCE public.unit_id_seq OWNED BY public.unit.id;
            public       postgres    false    469            ?           1259    26033    unit_of_measure    TABLE     ?   CREATE TABLE public.unit_of_measure (
    id bigint NOT NULL,
    company_id bigint NOT NULL,
    type integer,
    name bytea NOT NULL,
    abbreviation bytea,
    version integer
);
 #   DROP TABLE public.unit_of_measure;
       public         postgres    false            ?           1259    26031    unit_of_measure_id_seq    SEQUENCE        CREATE SEQUENCE public.unit_of_measure_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 -   DROP SEQUENCE public.unit_of_measure_id_seq;
       public       postgres    false    472            8           0    0    unit_of_measure_id_seq    SEQUENCE OWNED BY     Q   ALTER SEQUENCE public.unit_of_measure_id_seq OWNED BY public.unit_of_measure.id;
            public       postgres    false    471            ?           1259    46495    user_defined_pay_head    TABLE     F   CREATE TABLE public.user_defined_pay_head (
    id bigint NOT NULL
);
 )   DROP TABLE public.user_defined_pay_head;
       public         postgres    false            ?           1259    26055    user_defined_payhead_items    TABLE     ?   CREATE TABLE public.user_defined_payhead_items (
    user_defined_payhead_item_id bigint NOT NULL,
    pay_head bigint,
    value double precision,
    user_defined_payhead_item_index integer NOT NULL
);
 .   DROP TABLE public.user_defined_payhead_items;
       public         postgres    false            ?           1259    26067    user_permissions    TABLE     ?  CREATE TABLE public.user_permissions (
    id bigint NOT NULL,
    type_of_bank_reconcilation integer,
    type_of_invoices_bills integer,
    type_of_paybills_payments integer,
    type_of_company_settings_lock_dates integer,
    type_of_view_reports integer,
    type_of_manage_accounts integer,
    type_of_inventory_warehouse integer,
    type_of_save_as_drafts integer,
    type_invoices_and_payments integer
);
 $   DROP TABLE public.user_permissions;
       public         postgres    false            ?           1259    26065    user_permissions_id_seq    SEQUENCE     ?   CREATE SEQUENCE public.user_permissions_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 .   DROP SEQUENCE public.user_permissions_id_seq;
       public       postgres    false    477            9           0    0    user_permissions_id_seq    SEQUENCE OWNED BY     S   ALTER SEQUENCE public.user_permissions_id_seq OWNED BY public.user_permissions.id;
            public       postgres    false    476            ?           1259    26044    users    TABLE     W  CREATE TABLE public.users (
    id bigint NOT NULL,
    client_id bigint NOT NULL,
    company_id bigint,
    user_role character varying(255),
    can_do_user_management boolean,
    is_deleted boolean,
    is_active boolean,
    is_admin boolean,
    secret_key bytea,
    user_permissions_id bigint,
    unique_id character varying(255)
);
    DROP TABLE public.users;
       public         postgres    false            ?           1259    26042    users_id_seq    SEQUENCE     u   CREATE SEQUENCE public.users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 #   DROP SEQUENCE public.users_id_seq;
       public       postgres    false    474            :           0    0    users_id_seq    SEQUENCE OWNED BY     =   ALTER SEQUENCE public.users_id_seq OWNED BY public.users.id;
            public       postgres    false    473            ?           1259    26075    vatreturnbox    TABLE     ?   CREATE TABLE public.vatreturnbox (
    id bigint NOT NULL,
    company_id bigint NOT NULL,
    name bytea NOT NULL,
    vat_box character varying(255),
    total_box bytea,
    vat_return_type integer
);
     DROP TABLE public.vatreturnbox;
       public         postgres    false            ?           1259    26073    vatreturnbox_id_seq    SEQUENCE     |   CREATE SEQUENCE public.vatreturnbox_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 *   DROP SEQUENCE public.vatreturnbox_id_seq;
       public       postgres    false    479            ;           0    0    vatreturnbox_id_seq    SEQUENCE OWNED BY     K   ALTER SEQUENCE public.vatreturnbox_id_seq OWNED BY public.vatreturnbox.id;
            public       postgres    false    478            ?           1259    26100    vendor_group_id_seq    SEQUENCE     |   CREATE SEQUENCE public.vendor_group_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 *   DROP SEQUENCE public.vendor_group_id_seq;
       public       postgres    false    483            <           0    0    vendor_group_id_seq    SEQUENCE OWNED BY     K   ALTER SEQUENCE public.vendor_group_id_seq OWNED BY public.vendor_group.id;
            public       postgres    false    482            ?           1259    26121    vote    TABLE     i   CREATE TABLE public.vote (
    id integer NOT NULL,
    local_message_id bigint,
    client_id bigint
);
    DROP TABLE public.vote;
       public         postgres    false            ?           1259    26119    vote_id_seq    SEQUENCE     ?   CREATE SEQUENCE public.vote_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 "   DROP SEQUENCE public.vote_id_seq;
       public       postgres    false    486            =           0    0    vote_id_seq    SEQUENCE OWNED BY     ;   ALTER SEQUENCE public.vote_id_seq OWNED BY public.vote.id;
            public       postgres    false    485            ?           1259    26129 	   warehouse    TABLE     ?  CREATE TABLE public.warehouse (
    id bigint NOT NULL,
    company_id bigint NOT NULL,
    name bytea NOT NULL,
    warehouse_code bytea NOT NULL,
    is_default_warehouse boolean NOT NULL,
    ddi_number bytea,
    mobile_number bytea,
    created_by bigint NOT NULL,
    last_modifier bigint NOT NULL,
    created_date timestamp without time zone NOT NULL,
    last_modified_date timestamp without time zone NOT NULL,
    type integer,
    address1 bytea,
    street bytea,
    city bytea,
    state bytea,
    zip bytea,
    country bytea,
    is_selected boolean,
    is_primary boolean,
    contact__name bytea,
    contact__title bytea,
    _business_phone bytea,
    contact_email bytea
);
    DROP TABLE public.warehouse;
       public         postgres    false            ?           1259    26127    warehouse_id_seq    SEQUENCE     y   CREATE SEQUENCE public.warehouse_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 '   DROP SEQUENCE public.warehouse_id_seq;
       public       postgres    false    488            >           0    0    warehouse_id_seq    SEQUENCE OWNED BY     E   ALTER SEQUENCE public.warehouse_id_seq OWNED BY public.warehouse.id;
            public       postgres    false    487            ?           1259    26138    warehouse_transfer    TABLE     w   CREATE TABLE public.warehouse_transfer (
    id bigint NOT NULL,
    from_warehouse bigint,
    to_warehouse bigint
);
 &   DROP TABLE public.warehouse_transfer;
       public         postgres    false            ?           1259    26151    write_checks_estimates    TABLE     m   CREATE TABLE public.write_checks_estimates (
    write_checks_id bigint NOT NULL,
    elt bigint NOT NULL
);
 *   DROP TABLE public.write_checks_estimates;
       public         postgres    false                       2604    24581 
   account id    DEFAULT     h   ALTER TABLE ONLY public.account ALTER COLUMN id SET DEFAULT nextval('public.account_id_seq'::regclass);
 9   ALTER TABLE public.account ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    197    196    197                       2604    24608    account_transaction id    DEFAULT     ?   ALTER TABLE ONLY public.account_transaction ALTER COLUMN id SET DEFAULT nextval('public.account_transaction_id_seq'::regclass);
 E   ALTER TABLE public.account_transaction ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    201    202    202                       2604    24592    accounter_class id    DEFAULT     x   ALTER TABLE ONLY public.accounter_class ALTER COLUMN id SET DEFAULT nextval('public.accounter_class_id_seq'::regclass);
 A   ALTER TABLE public.accounter_class ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    198    199    199                       2604    24616    activation id    DEFAULT     n   ALTER TABLE ONLY public.activation ALTER COLUMN id SET DEFAULT nextval('public.activation_id_seq'::regclass);
 <   ALTER TABLE public.activation ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    203    204    204                       2604    24627    activity id    DEFAULT     j   ALTER TABLE ONLY public.activity ALTER COLUMN id SET DEFAULT nextval('public.activity_id_seq'::regclass);
 :   ALTER TABLE public.activity ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    205    206    206                        2604    24638    adjustment_reason id    DEFAULT     |   ALTER TABLE ONLY public.adjustment_reason ALTER COLUMN id SET DEFAULT nextval('public.adjustment_reason_id_seq'::regclass);
 C   ALTER TABLE public.adjustment_reason ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    207    208    208            !           2604    24649    admin_templates id    DEFAULT     x   ALTER TABLE ONLY public.admin_templates ALTER COLUMN id SET DEFAULT nextval('public.admin_templates_id_seq'::regclass);
 A   ALTER TABLE public.admin_templates ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    209    210    210            "           2604    24660    admin_user id    DEFAULT     n   ALTER TABLE ONLY public.admin_user ALTER COLUMN id SET DEFAULT nextval('public.admin_user_id_seq'::regclass);
 <   ALTER TABLE public.admin_user ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    211    212    212            #           2604    24671    advertisement id    DEFAULT     t   ALTER TABLE ONLY public.advertisement ALTER COLUMN id SET DEFAULT nextval('public.advertisement_id_seq'::regclass);
 ?   ALTER TABLE public.advertisement ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    213    214    214            $           2604    24679    attachments id    DEFAULT     p   ALTER TABLE ONLY public.attachments ALTER COLUMN id SET DEFAULT nextval('public.attachments_id_seq'::regclass);
 =   ALTER TABLE public.attachments ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    216    215    216            %           2604    24690    attendance_management_item id    DEFAULT     ?   ALTER TABLE ONLY public.attendance_management_item ALTER COLUMN id SET DEFAULT nextval('public.attendance_management_item_id_seq'::regclass);
 L   ALTER TABLE public.attendance_management_item ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    217    218    218            &           2604    24703    attendance_or_production_tpe id    DEFAULT     ?   ALTER TABLE ONLY public.attendance_or_production_tpe ALTER COLUMN id SET DEFAULT nextval('public.attendance_or_production_tpe_id_seq'::regclass);
 N   ALTER TABLE public.attendance_or_production_tpe ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    221    220    221            '           2604    24719    bank id    DEFAULT     b   ALTER TABLE ONLY public.bank ALTER COLUMN id SET DEFAULT nextval('public.bank_id_seq'::regclass);
 6   ALTER TABLE public.bank ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    224    223    224            (           2604    24738    branding_theme id    DEFAULT     v   ALTER TABLE ONLY public.branding_theme ALTER COLUMN id SET DEFAULT nextval('public.branding_theme_id_seq'::regclass);
 @   ALTER TABLE public.branding_theme ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    226    227    227            )           2604    24749 	   budget id    DEFAULT     f   ALTER TABLE ONLY public.budget ALTER COLUMN id SET DEFAULT nextval('public.budget_id_seq'::regclass);
 8   ALTER TABLE public.budget ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    228    229    229            *           2604    24760    budgetitem id    DEFAULT     n   ALTER TABLE ONLY public.budgetitem ALTER COLUMN id SET DEFAULT nextval('public.budgetitem_id_seq'::regclass);
 <   ALTER TABLE public.budgetitem ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    230    231    231            +           2604    24804    cheque_layout id    DEFAULT     t   ALTER TABLE ONLY public.cheque_layout ALTER COLUMN id SET DEFAULT nextval('public.cheque_layout_id_seq'::regclass);
 ?   ALTER TABLE public.cheque_layout ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    239    238    239            ,           2604    24815 	   client id    DEFAULT     f   ALTER TABLE ONLY public.client ALTER COLUMN id SET DEFAULT nextval('public.client_id_seq'::regclass);
 8   ALTER TABLE public.client ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    241    240    241            -           2604    24833    client_paypal_details id    DEFAULT     ?   ALTER TABLE ONLY public.client_paypal_details ALTER COLUMN id SET DEFAULT nextval('public.client_paypal_details_id_seq'::regclass);
 G   ALTER TABLE public.client_paypal_details ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    244    243    244            .           2604    24844    client_subscription id    DEFAULT     ?   ALTER TABLE ONLY public.client_subscription ALTER COLUMN id SET DEFAULT nextval('public.client_subscription_id_seq'::regclass);
 E   ALTER TABLE public.client_subscription ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    246    245    246            /           2604    24852    commodity_code id    DEFAULT     v   ALTER TABLE ONLY public.commodity_code ALTER COLUMN id SET DEFAULT nextval('public.commodity_code_id_seq'::regclass);
 @   ALTER TABLE public.commodity_code ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    248    247    248            0           2604    24863 
   company id    DEFAULT     h   ALTER TABLE ONLY public.company ALTER COLUMN id SET DEFAULT nextval('public.company_id_seq'::regclass);
 9   ALTER TABLE public.company ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    249    250    250            1           2604    24892    creditrating id    DEFAULT     r   ALTER TABLE ONLY public.creditrating ALTER COLUMN id SET DEFAULT nextval('public.creditrating_id_seq'::regclass);
 >   ALTER TABLE public.creditrating ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    255    254    255            2           2604    24903    credits_and_payments id    DEFAULT     ?   ALTER TABLE ONLY public.credits_and_payments ALTER COLUMN id SET DEFAULT nextval('public.credits_and_payments_id_seq'::regclass);
 F   ALTER TABLE public.credits_and_payments ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    257    256    257            4           2604    24923    currency id    DEFAULT     j   ALTER TABLE ONLY public.currency ALTER COLUMN id SET DEFAULT nextval('public.currency_id_seq'::regclass);
 :   ALTER TABLE public.currency ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    260    259    260            5           2604    24950    customer_group id    DEFAULT     v   ALTER TABLE ONLY public.customer_group ALTER COLUMN id SET DEFAULT nextval('public.customer_group_id_seq'::regclass);
 @   ALTER TABLE public.customer_group ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    264    263    264            6           2604    24977    customfield id    DEFAULT     p   ALTER TABLE ONLY public.customfield ALTER COLUMN id SET DEFAULT nextval('public.customfield_id_seq'::regclass);
 =   ALTER TABLE public.customfield ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    267    268    268            7           2604    24988    delete_reason id    DEFAULT     t   ALTER TABLE ONLY public.delete_reason ALTER COLUMN id SET DEFAULT nextval('public.delete_reason_id_seq'::regclass);
 ?   ALTER TABLE public.delete_reason ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    269    270    270            8           2604    25004    depreciation id    DEFAULT     r   ALTER TABLE ONLY public.depreciation ALTER COLUMN id SET DEFAULT nextval('public.depreciation_id_seq'::regclass);
 >   ALTER TABLE public.depreciation ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    273    272    273            9           2604    25012    developer id    DEFAULT     l   ALTER TABLE ONLY public.developer ALTER COLUMN id SET DEFAULT nextval('public.developer_id_seq'::regclass);
 ;   ALTER TABLE public.developer ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    275    274    275            :           2604    25023    email_account id    DEFAULT     t   ALTER TABLE ONLY public.email_account ALTER COLUMN id SET DEFAULT nextval('public.email_account_id_seq'::regclass);
 ?   ALTER TABLE public.email_account ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    277    276    277            ;           2604    25034    email_template id    DEFAULT     v   ALTER TABLE ONLY public.email_template ALTER COLUMN id SET DEFAULT nextval('public.email_template_id_seq'::regclass);
 @   ALTER TABLE public.email_template ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    279    278    279            ?           2604    45692    employee_compensation id    DEFAULT     ?   ALTER TABLE ONLY public.employee_compensation ALTER COLUMN id SET DEFAULT nextval('public.employee_compensation_id_seq'::regclass);
 G   ALTER TABLE public.employee_compensation ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    497    498    498            <           2604    25053    employee_group id    DEFAULT     v   ALTER TABLE ONLY public.employee_group ALTER COLUMN id SET DEFAULT nextval('public.employee_group_id_seq'::regclass);
 @   ALTER TABLE public.employee_group ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    281    282    282            =           2604    25064    employee_payhead_component id    DEFAULT     ?   ALTER TABLE ONLY public.employee_payhead_component ALTER COLUMN id SET DEFAULT nextval('public.employee_payhead_component_id_seq'::regclass);
 L   ALTER TABLE public.employee_payhead_component ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    284    283    284            >           2604    25072    employee_payment_details id    DEFAULT     ?   ALTER TABLE ONLY public.employee_payment_details ALTER COLUMN id SET DEFAULT nextval('public.employee_payment_details_id_seq'::regclass);
 J   ALTER TABLE public.employee_payment_details ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    285    286    286            ?           2604    45700    employee_tax id    DEFAULT     r   ALTER TABLE ONLY public.employee_tax ALTER COLUMN id SET DEFAULT nextval('public.employee_tax_id_seq'::regclass);
 >   ALTER TABLE public.employee_tax ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    500    499    500            ?           2604    25117    fiscal_year id    DEFAULT     p   ALTER TABLE ONLY public.fiscal_year ALTER COLUMN id SET DEFAULT nextval('public.fiscal_year_id_seq'::regclass);
 =   ALTER TABLE public.fiscal_year ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    293    294    294            @           2604    25125    fixed_asset id    DEFAULT     p   ALTER TABLE ONLY public.fixed_asset ALTER COLUMN id SET DEFAULT nextval('public.fixed_asset_id_seq'::regclass);
 =   ALTER TABLE public.fixed_asset ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    295    296    296            A           2604    25136    fixed_asset_history id    DEFAULT     ?   ALTER TABLE ONLY public.fixed_asset_history ALTER COLUMN id SET DEFAULT nextval('public.fixed_asset_history_id_seq'::regclass);
 E   ALTER TABLE public.fixed_asset_history ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    297    298    298            J           2604    25259    i_m_user id    DEFAULT     j   ALTER TABLE ONLY public.i_m_user ALTER COLUMN id SET DEFAULT nextval('public.i_m_user_id_seq'::regclass);
 :   ALTER TABLE public.i_m_user ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    321    322    322            B           2604    25157    im_activation id    DEFAULT     t   ALTER TABLE ONLY public.im_activation ALTER COLUMN id SET DEFAULT nextval('public.im_activation_id_seq'::regclass);
 ?   ALTER TABLE public.im_activation ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    301    302    302            C           2604    25173    inventory_assembly_item id    DEFAULT     ?   ALTER TABLE ONLY public.inventory_assembly_item ALTER COLUMN id SET DEFAULT nextval('public.inventory_assembly_item_id_seq'::regclass);
 I   ALTER TABLE public.inventory_assembly_item ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    305    304    305            D           2604    25184    inventory_history id    DEFAULT     |   ALTER TABLE ONLY public.inventory_history ALTER COLUMN id SET DEFAULT nextval('public.inventory_history_id_seq'::regclass);
 C   ALTER TABLE public.inventory_history ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    307    306    307            E           2604    25192    inventory_purchase id    DEFAULT     ~   ALTER TABLE ONLY public.inventory_purchase ALTER COLUMN id SET DEFAULT nextval('public.inventory_purchase_id_seq'::regclass);
 D   ALTER TABLE public.inventory_purchase ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    308    309    309            F           2604    25200    inventory_remap_task id    DEFAULT     ?   ALTER TABLE ONLY public.inventory_remap_task ALTER COLUMN id SET DEFAULT nextval('public.inventory_remap_task_id_seq'::regclass);
 F   ALTER TABLE public.inventory_remap_task ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    311    310    311            G           2604    25221    item id    DEFAULT     b   ALTER TABLE ONLY public.item ALTER COLUMN id SET DEFAULT nextval('public.item_id_seq'::regclass);
 6   ALTER TABLE public.item ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    315    314    315            I           2604    25251    item_update id    DEFAULT     p   ALTER TABLE ONLY public.item_update ALTER COLUMN id SET DEFAULT nextval('public.item_update_id_seq'::regclass);
 =   ALTER TABLE public.item_update ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    320    319    320            H           2604    25232    itemgroup id    DEFAULT     l   ALTER TABLE ONLY public.itemgroup ALTER COLUMN id SET DEFAULT nextval('public.itemgroup_id_seq'::regclass);
 ;   ALTER TABLE public.itemgroup ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    317    316    317            K           2604    25267    job id    DEFAULT     `   ALTER TABLE ONLY public.job ALTER COLUMN id SET DEFAULT nextval('public.job_id_seq'::regclass);
 5   ALTER TABLE public.job ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    323    324    324            L           2604    25283    key id    DEFAULT     `   ALTER TABLE ONLY public.key ALTER COLUMN id SET DEFAULT nextval('public.key_id_seq'::regclass);
 5   ALTER TABLE public.key ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    326    327    327            M           2604    25304 
   license id    DEFAULT     h   ALTER TABLE ONLY public.license ALTER COLUMN id SET DEFAULT nextval('public.license_id_seq'::regclass);
 9   ALTER TABLE public.license ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    331    330    331            N           2604    25315    license_purchase id    DEFAULT     z   ALTER TABLE ONLY public.license_purchase ALTER COLUMN id SET DEFAULT nextval('public.license_purchase_id_seq'::regclass);
 B   ALTER TABLE public.license_purchase ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    333    332    333            ?           2604    46042    live_tax_rate id    DEFAULT     t   ALTER TABLE ONLY public.live_tax_rate ALTER COLUMN id SET DEFAULT nextval('public.live_tax_rate_id_seq'::regclass);
 ?   ALTER TABLE public.live_tax_rate ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    501    502    502            ?           2604    46083    live_tax_rate_range id    DEFAULT     ?   ALTER TABLE ONLY public.live_tax_rate_range ALTER COLUMN id SET DEFAULT nextval('public.live_tax_rate_range_id_seq'::regclass);
 E   ALTER TABLE public.live_tax_rate_range ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    503    504    504            O           2604    25323    local_message id    DEFAULT     t   ALTER TABLE ONLY public.local_message ALTER COLUMN id SET DEFAULT nextval('public.local_message_id_seq'::regclass);
 ?   ALTER TABLE public.local_message ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    335    334    335            P           2604    25334    location id    DEFAULT     j   ALTER TABLE ONLY public.location ALTER COLUMN id SET DEFAULT nextval('public.location_id_seq'::regclass);
 :   ALTER TABLE public.location ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    336    337    337            Q           2604    25355    measurement id    DEFAULT     p   ALTER TABLE ONLY public.measurement ALTER COLUMN id SET DEFAULT nextval('public.measurement_id_seq'::regclass);
 =   ALTER TABLE public.measurement ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    340    341    341            R           2604    25369 
   message id    DEFAULT     h   ALTER TABLE ONLY public.message ALTER COLUMN id SET DEFAULT nextval('public.message_id_seq'::regclass);
 9   ALTER TABLE public.message ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    343    344    344            S           2604    25382    message_or_task id    DEFAULT     x   ALTER TABLE ONLY public.message_or_task ALTER COLUMN id SET DEFAULT nextval('public.message_or_task_id_seq'::regclass);
 A   ALTER TABLE public.message_or_task ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    346    345    346            T           2604    25398    news id    DEFAULT     b   ALTER TABLE ONLY public.news ALTER COLUMN id SET DEFAULT nextval('public.news_id_seq'::regclass);
 6   ALTER TABLE public.news ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    349    348    349            U           2604    25409    nominal_code_range id    DEFAULT     ~   ALTER TABLE ONLY public.nominal_code_range ALTER COLUMN id SET DEFAULT nextval('public.nominal_code_range_id_seq'::regclass);
 D   ALTER TABLE public.nominal_code_range ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    350    351    351            [           2604    25514    pay_head id    DEFAULT     j   ALTER TABLE ONLY public.pay_head ALTER COLUMN id SET DEFAULT nextval('public.pay_head_id_seq'::regclass);
 :   ALTER TABLE public.pay_head ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    368    369    369            \           2604    25525    pay_roll_unit id    DEFAULT     t   ALTER TABLE ONLY public.pay_roll_unit ALTER COLUMN id SET DEFAULT nextval('public.pay_roll_unit_id_seq'::regclass);
 ?   ALTER TABLE public.pay_roll_unit ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    371    370    371            ]           2604    25541    pay_structure id    DEFAULT     t   ALTER TABLE ONLY public.pay_structure ALTER COLUMN id SET DEFAULT nextval('public.pay_structure_id_seq'::regclass);
 ?   ALTER TABLE public.pay_structure ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    373    374    374            ^           2604    25549    pay_structure_item id    DEFAULT     ~   ALTER TABLE ONLY public.pay_structure_item ALTER COLUMN id SET DEFAULT nextval('public.pay_structure_item_id_seq'::regclass);
 D   ALTER TABLE public.pay_structure_item ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    375    376    376            V           2604    25417    payee id    DEFAULT     d   ALTER TABLE ONLY public.payee ALTER COLUMN id SET DEFAULT nextval('public.payee_id_seq'::regclass);
 7   ALTER TABLE public.payee ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    352    353    353            W           2604    25444    payee_customfields id    DEFAULT     ~   ALTER TABLE ONLY public.payee_customfields ALTER COLUMN id SET DEFAULT nextval('public.payee_customfields_id_seq'::regclass);
 D   ALTER TABLE public.payee_customfields ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    356    357    357            X           2604    25463    payee_update id    DEFAULT     r   ALTER TABLE ONLY public.payee_update ALTER COLUMN id SET DEFAULT nextval('public.payee_update_id_seq'::regclass);
 >   ALTER TABLE public.payee_update ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    359    360    360            Y           2604    25471    paymentterms id    DEFAULT     r   ALTER TABLE ONLY public.paymentterms ALTER COLUMN id SET DEFAULT nextval('public.paymentterms_id_seq'::regclass);
 >   ALTER TABLE public.paymentterms ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    362    361    362            Z           2604    25482    paypal_transaction id    DEFAULT     ~   ALTER TABLE ONLY public.paypal_transaction ALTER COLUMN id SET DEFAULT nextval('public.paypal_transaction_id_seq'::regclass);
 D   ALTER TABLE public.paypal_transaction ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    363    364    364            _           2604    25565    portlet_configuration id    DEFAULT     ?   ALTER TABLE ONLY public.portlet_configuration ALTER COLUMN id SET DEFAULT nextval('public.portlet_configuration_id_seq'::regclass);
 G   ALTER TABLE public.portlet_configuration ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    378    379    379            `           2604    25581    portlet_page_configuration id    DEFAULT     ?   ALTER TABLE ONLY public.portlet_page_configuration ALTER COLUMN id SET DEFAULT nextval('public.portlet_page_configuration_id_seq'::regclass);
 L   ALTER TABLE public.portlet_page_configuration ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    381    382    382            a           2604    25589    pricelevel id    DEFAULT     n   ALTER TABLE ONLY public.pricelevel ALTER COLUMN id SET DEFAULT nextval('public.pricelevel_id_seq'::regclass);
 <   ALTER TABLE public.pricelevel ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    384    383    384            b           2604    25629    reconciliation id    DEFAULT     v   ALTER TABLE ONLY public.reconciliation ALTER COLUMN id SET DEFAULT nextval('public.reconciliation_id_seq'::regclass);
 @   ALTER TABLE public.reconciliation ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    390    389    390            c           2604    25637    reconciliation_item id    DEFAULT     ?   ALTER TABLE ONLY public.reconciliation_item ALTER COLUMN id SET DEFAULT nextval('public.reconciliation_item_id_seq'::regclass);
 E   ALTER TABLE public.reconciliation_item ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    391    392    392            d           2604    25648    recurring_transaction id    DEFAULT     ?   ALTER TABLE ONLY public.recurring_transaction ALTER COLUMN id SET DEFAULT nextval('public.recurring_transaction_id_seq'::regclass);
 G   ALTER TABLE public.recurring_transaction ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    393    394    394            e           2604    25661    remember_me_key id    DEFAULT     x   ALTER TABLE ONLY public.remember_me_key ALTER COLUMN id SET DEFAULT nextval('public.remember_me_key_id_seq'::regclass);
 A   ALTER TABLE public.remember_me_key ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    396    395    396            f           2604    25672    reminder id    DEFAULT     j   ALTER TABLE ONLY public.reminder ALTER COLUMN id SET DEFAULT nextval('public.reminder_id_seq'::regclass);
 :   ALTER TABLE public.reminder ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    397    398    398            g           2604    25680    reset_password_token id    DEFAULT     ?   ALTER TABLE ONLY public.reset_password_token ALTER COLUMN id SET DEFAULT nextval('public.reset_password_token_id_seq'::regclass);
 F   ALTER TABLE public.reset_password_token ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    400    399    400            h           2604    25688    sales_person id    DEFAULT     r   ALTER TABLE ONLY public.sales_person ALTER COLUMN id SET DEFAULT nextval('public.sales_person_id_seq'::regclass);
 >   ALTER TABLE public.sales_person ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    402    401    402            i           2604    25699    server_maintanance id    DEFAULT     ~   ALTER TABLE ONLY public.server_maintanance ALTER COLUMN id SET DEFAULT nextval('public.server_maintanance_id_seq'::regclass);
 D   ALTER TABLE public.server_maintanance ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    403    404    404            j           2604    25707    shippingmethod id    DEFAULT     v   ALTER TABLE ONLY public.shippingmethod ALTER COLUMN id SET DEFAULT nextval('public.shippingmethod_id_seq'::regclass);
 @   ALTER TABLE public.shippingmethod ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    406    405    406            k           2604    25718    shippingterms id    DEFAULT     t   ALTER TABLE ONLY public.shippingterms ALTER COLUMN id SET DEFAULT nextval('public.shippingterms_id_seq'::regclass);
 ?   ALTER TABLE public.shippingterms ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    408    407    408            l           2604    25729    statement id    DEFAULT     l   ALTER TABLE ONLY public.statement ALTER COLUMN id SET DEFAULT nextval('public.statement_id_seq'::regclass);
 ;   ALTER TABLE public.statement ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    410    409    410            m           2604    25737    statement_record id    DEFAULT     z   ALTER TABLE ONLY public.statement_record ALTER COLUMN id SET DEFAULT nextval('public.statement_record_id_seq'::regclass);
 B   ALTER TABLE public.statement_record ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    411    412    412            n           2604    25753    stock_transfer_item id    DEFAULT     ?   ALTER TABLE ONLY public.stock_transfer_item ALTER COLUMN id SET DEFAULT nextval('public.stock_transfer_item_id_seq'::regclass);
 E   ALTER TABLE public.stock_transfer_item ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    415    414    415            o           2604    25761    subscription id    DEFAULT     r   ALTER TABLE ONLY public.subscription ALTER COLUMN id SET DEFAULT nextval('public.subscription_id_seq'::regclass);
 >   ALTER TABLE public.subscription ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    417    416    417            q           2604    25792    tax_code id    DEFAULT     j   ALTER TABLE ONLY public.tax_code ALTER COLUMN id SET DEFAULT nextval('public.tax_code_id_seq'::regclass);
 :   ALTER TABLE public.tax_code ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    423    424    424            r           2604    25818    tax_item_groups id    DEFAULT     x   ALTER TABLE ONLY public.tax_item_groups ALTER COLUMN id SET DEFAULT nextval('public.tax_item_groups_id_seq'::regclass);
 A   ALTER TABLE public.tax_item_groups ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    428    429    429            s           2604    25829    tax_rate_calculation id    DEFAULT     ?   ALTER TABLE ONLY public.tax_rate_calculation ALTER COLUMN id SET DEFAULT nextval('public.tax_rate_calculation_id_seq'::regclass);
 F   ALTER TABLE public.tax_rate_calculation ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    431    430    431            t           2604    25842    tax_return_entry id    DEFAULT     z   ALTER TABLE ONLY public.tax_return_entry ALTER COLUMN id SET DEFAULT nextval('public.tax_return_entry_id_seq'::regclass);
 B   ALTER TABLE public.tax_return_entry ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    434    433    434            p           2604    25779    taxrates id    DEFAULT     j   ALTER TABLE ONLY public.taxrates ALTER COLUMN id SET DEFAULT nextval('public.taxrates_id_seq'::regclass);
 :   ALTER TABLE public.taxrates ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    420    421    421            u           2604    25850    tdsdeductormasters id    DEFAULT     ~   ALTER TABLE ONLY public.tdsdeductormasters ALTER COLUMN id SET DEFAULT nextval('public.tdsdeductormasters_id_seq'::regclass);
 D   ALTER TABLE public.tdsdeductormasters ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    436    435    436            v           2604    25861    tdsresponsibleperson id    DEFAULT     ?   ALTER TABLE ONLY public.tdsresponsibleperson ALTER COLUMN id SET DEFAULT nextval('public.tdsresponsibleperson_id_seq'::regclass);
 F   ALTER TABLE public.tdsresponsibleperson ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    437    438    438            w           2604    25872    tdstransactionitem id    DEFAULT     ~   ALTER TABLE ONLY public.tdstransactionitem ALTER COLUMN id SET DEFAULT nextval('public.tdstransactionitem_id_seq'::regclass);
 D   ALTER TABLE public.tdstransactionitem ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    440    439    440            x           2604    25891    transaction id    DEFAULT     p   ALTER TABLE ONLY public.transaction ALTER COLUMN id SET DEFAULT nextval('public.transaction_id_seq'::regclass);
 =   ALTER TABLE public.transaction ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    442    443    443            y           2604    25902 #   transaction_credits_and_payments id    DEFAULT     ?   ALTER TABLE ONLY public.transaction_credits_and_payments ALTER COLUMN id SET DEFAULT nextval('public.transaction_credits_and_payments_id_seq'::regclass);
 R   ALTER TABLE public.transaction_credits_and_payments ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    444    445    445            {           2604    25914    transaction_deposit_item id    DEFAULT     ?   ALTER TABLE ONLY public.transaction_deposit_item ALTER COLUMN id SET DEFAULT nextval('public.transaction_deposit_item_id_seq'::regclass);
 J   ALTER TABLE public.transaction_deposit_item ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    447    446    447            |           2604    25925    transaction_expense id    DEFAULT     ?   ALTER TABLE ONLY public.transaction_expense ALTER COLUMN id SET DEFAULT nextval('public.transaction_expense_id_seq'::regclass);
 E   ALTER TABLE public.transaction_expense ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    448    449    449            }           2604    25936    transaction_history id    DEFAULT     ?   ALTER TABLE ONLY public.transaction_history ALTER COLUMN id SET DEFAULT nextval('public.transaction_history_id_seq'::regclass);
 E   ALTER TABLE public.transaction_history ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    450    451    451            ~           2604    25947    transaction_item id    DEFAULT     z   ALTER TABLE ONLY public.transaction_item ALTER COLUMN id SET DEFAULT nextval('public.transaction_item_id_seq'::regclass);
 B   ALTER TABLE public.transaction_item ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    452    453    453                       2604    25958 #   transaction_make_deposit_entries id    DEFAULT     ?   ALTER TABLE ONLY public.transaction_make_deposit_entries ALTER COLUMN id SET DEFAULT nextval('public.transaction_make_deposit_entries_id_seq'::regclass);
 R   ALTER TABLE public.transaction_make_deposit_entries ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    454    455    455            ?           2604    25977    transaction_pay_employee id    DEFAULT     ?   ALTER TABLE ONLY public.transaction_pay_employee ALTER COLUMN id SET DEFAULT nextval('public.transaction_pay_employee_id_seq'::regclass);
 J   ALTER TABLE public.transaction_pay_employee ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    458    459    459            ?           2604    25985    transaction_pay_expense id    DEFAULT     ?   ALTER TABLE ONLY public.transaction_pay_expense ALTER COLUMN id SET DEFAULT nextval('public.transaction_pay_expense_id_seq'::regclass);
 I   ALTER TABLE public.transaction_pay_expense ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    460    461    461            ?           2604    25993    transaction_pay_tax id    DEFAULT     ?   ALTER TABLE ONLY public.transaction_pay_tax ALTER COLUMN id SET DEFAULT nextval('public.transaction_pay_tax_id_seq'::regclass);
 E   ALTER TABLE public.transaction_pay_tax ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    462    463    463            ?           2604    25966    transaction_paybill id    DEFAULT     ?   ALTER TABLE ONLY public.transaction_paybill ALTER COLUMN id SET DEFAULT nextval('public.transaction_paybill_id_seq'::regclass);
 E   ALTER TABLE public.transaction_paybill ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    456    457    457            ?           2604    26001    transaction_receive_payment id    DEFAULT     ?   ALTER TABLE ONLY public.transaction_receive_payment ALTER COLUMN id SET DEFAULT nextval('public.transaction_receive_payment_id_seq'::regclass);
 M   ALTER TABLE public.transaction_receive_payment ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    464    465    465            ?           2604    26012    transaction_receive_vat id    DEFAULT     ?   ALTER TABLE ONLY public.transaction_receive_vat ALTER COLUMN id SET DEFAULT nextval('public.transaction_receive_vat_id_seq'::regclass);
 I   ALTER TABLE public.transaction_receive_vat ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    467    466    467            ?           2604    26028    unit id    DEFAULT     b   ALTER TABLE ONLY public.unit ALTER COLUMN id SET DEFAULT nextval('public.unit_id_seq'::regclass);
 6   ALTER TABLE public.unit ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    470    469    470            ?           2604    26036    unit_of_measure id    DEFAULT     x   ALTER TABLE ONLY public.unit_of_measure ALTER COLUMN id SET DEFAULT nextval('public.unit_of_measure_id_seq'::regclass);
 A   ALTER TABLE public.unit_of_measure ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    472    471    472            ?           2604    26070    user_permissions id    DEFAULT     z   ALTER TABLE ONLY public.user_permissions ALTER COLUMN id SET DEFAULT nextval('public.user_permissions_id_seq'::regclass);
 B   ALTER TABLE public.user_permissions ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    476    477    477            ?           2604    26047    users id    DEFAULT     d   ALTER TABLE ONLY public.users ALTER COLUMN id SET DEFAULT nextval('public.users_id_seq'::regclass);
 7   ALTER TABLE public.users ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    473    474    474            ?           2604    26078    vatreturnbox id    DEFAULT     r   ALTER TABLE ONLY public.vatreturnbox ALTER COLUMN id SET DEFAULT nextval('public.vatreturnbox_id_seq'::regclass);
 >   ALTER TABLE public.vatreturnbox ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    479    478    479            ?           2604    26105    vendor_group id    DEFAULT     r   ALTER TABLE ONLY public.vendor_group ALTER COLUMN id SET DEFAULT nextval('public.vendor_group_id_seq'::regclass);
 >   ALTER TABLE public.vendor_group ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    483    482    483            ?           2604    26124    vote id    DEFAULT     b   ALTER TABLE ONLY public.vote ALTER COLUMN id SET DEFAULT nextval('public.vote_id_seq'::regclass);
 6   ALTER TABLE public.vote ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    486    485    486            ?           2604    26132    warehouse id    DEFAULT     l   ALTER TABLE ONLY public.warehouse ALTER COLUMN id SET DEFAULT nextval('public.warehouse_id_seq'::regclass);
 ;   ALTER TABLE public.warehouse ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    487    488    488            ?           2606    24602 $   account_amounts account_amounts_pkey 
   CONSTRAINT     q   ALTER TABLE ONLY public.account_amounts
    ADD CONSTRAINT account_amounts_pkey PRIMARY KEY (account_id, month);
 N   ALTER TABLE ONLY public.account_amounts DROP CONSTRAINT account_amounts_pkey;
       public         postgres    false    200    200            ?           2606    24586    account account_pkey 
   CONSTRAINT     R   ALTER TABLE ONLY public.account
    ADD CONSTRAINT account_pkey PRIMARY KEY (id);
 >   ALTER TABLE ONLY public.account DROP CONSTRAINT account_pkey;
       public         postgres    false    197            ?           2606    24610 ,   account_transaction account_transaction_pkey 
   CONSTRAINT     j   ALTER TABLE ONLY public.account_transaction
    ADD CONSTRAINT account_transaction_pkey PRIMARY KEY (id);
 V   ALTER TABLE ONLY public.account_transaction DROP CONSTRAINT account_transaction_pkey;
       public         postgres    false    202            ?           2606    24597 $   accounter_class accounter_class_pkey 
   CONSTRAINT     b   ALTER TABLE ONLY public.accounter_class
    ADD CONSTRAINT accounter_class_pkey PRIMARY KEY (id);
 N   ALTER TABLE ONLY public.accounter_class DROP CONSTRAINT accounter_class_pkey;
       public         postgres    false    199            ?           2606    24621    activation activation_pkey 
   CONSTRAINT     X   ALTER TABLE ONLY public.activation
    ADD CONSTRAINT activation_pkey PRIMARY KEY (id);
 D   ALTER TABLE ONLY public.activation DROP CONSTRAINT activation_pkey;
       public         postgres    false    204            ?           2606    24632    activity activity_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.activity
    ADD CONSTRAINT activity_pkey PRIMARY KEY (id);
 @   ALTER TABLE ONLY public.activity DROP CONSTRAINT activity_pkey;
       public         postgres    false    206            ?           2606    24643 (   adjustment_reason adjustment_reason_pkey 
   CONSTRAINT     f   ALTER TABLE ONLY public.adjustment_reason
    ADD CONSTRAINT adjustment_reason_pkey PRIMARY KEY (id);
 R   ALTER TABLE ONLY public.adjustment_reason DROP CONSTRAINT adjustment_reason_pkey;
       public         postgres    false    208            ?           2606    24654 $   admin_templates admin_templates_pkey 
   CONSTRAINT     b   ALTER TABLE ONLY public.admin_templates
    ADD CONSTRAINT admin_templates_pkey PRIMARY KEY (id);
 N   ALTER TABLE ONLY public.admin_templates DROP CONSTRAINT admin_templates_pkey;
       public         postgres    false    210            ?           2606    24665    admin_user admin_user_pkey 
   CONSTRAINT     X   ALTER TABLE ONLY public.admin_user
    ADD CONSTRAINT admin_user_pkey PRIMARY KEY (id);
 D   ALTER TABLE ONLY public.admin_user DROP CONSTRAINT admin_user_pkey;
       public         postgres    false    212            ?           2606    24673     advertisement advertisement_pkey 
   CONSTRAINT     ^   ALTER TABLE ONLY public.advertisement
    ADD CONSTRAINT advertisement_pkey PRIMARY KEY (id);
 J   ALTER TABLE ONLY public.advertisement DROP CONSTRAINT advertisement_pkey;
       public         postgres    false    214            ?           2606    24684    attachments attachments_pkey 
   CONSTRAINT     Z   ALTER TABLE ONLY public.attachments
    ADD CONSTRAINT attachments_pkey PRIMARY KEY (id);
 F   ALTER TABLE ONLY public.attachments DROP CONSTRAINT attachments_pkey;
       public         postgres    false    216            ?           2606    24692 :   attendance_management_item attendance_management_item_pkey 
   CONSTRAINT     x   ALTER TABLE ONLY public.attendance_management_item
    ADD CONSTRAINT attendance_management_item_pkey PRIMARY KEY (id);
 d   ALTER TABLE ONLY public.attendance_management_item DROP CONSTRAINT attendance_management_item_pkey;
       public         postgres    false    218            ?           2606    24697 B   attendance_or_production_items attendance_or_production_items_pkey 
   CONSTRAINT     ?   ALTER TABLE ONLY public.attendance_or_production_items
    ADD CONSTRAINT attendance_or_production_items_pkey PRIMARY KEY (attendance_management_item_id, attendance_or_production_items_index);
 l   ALTER TABLE ONLY public.attendance_or_production_items DROP CONSTRAINT attendance_or_production_items_pkey;
       public         postgres    false    219    219            ?           2606    24708 >   attendance_or_production_tpe attendance_or_production_tpe_pkey 
   CONSTRAINT     |   ALTER TABLE ONLY public.attendance_or_production_tpe
    ADD CONSTRAINT attendance_or_production_tpe_pkey PRIMARY KEY (id);
 h   ALTER TABLE ONLY public.attendance_or_production_tpe DROP CONSTRAINT attendance_or_production_tpe_pkey;
       public         postgres    false    221            ?           2606    24713 *   attendance_payhead attendance_payhead_pkey 
   CONSTRAINT     h   ALTER TABLE ONLY public.attendance_payhead
    ADD CONSTRAINT attendance_payhead_pkey PRIMARY KEY (id);
 T   ALTER TABLE ONLY public.attendance_payhead DROP CONSTRAINT attendance_payhead_pkey;
       public         postgres    false    222            ?           2606    24732    bank_account bank_account_pkey 
   CONSTRAINT     \   ALTER TABLE ONLY public.bank_account
    ADD CONSTRAINT bank_account_pkey PRIMARY KEY (id);
 H   ALTER TABLE ONLY public.bank_account DROP CONSTRAINT bank_account_pkey;
       public         postgres    false    225            ?           2606    24724    bank bank_pkey 
   CONSTRAINT     L   ALTER TABLE ONLY public.bank
    ADD CONSTRAINT bank_pkey PRIMARY KEY (id);
 8   ALTER TABLE ONLY public.bank DROP CONSTRAINT bank_pkey;
       public         postgres    false    224            ?           2606    24743 "   branding_theme branding_theme_pkey 
   CONSTRAINT     `   ALTER TABLE ONLY public.branding_theme
    ADD CONSTRAINT branding_theme_pkey PRIMARY KEY (id);
 L   ALTER TABLE ONLY public.branding_theme DROP CONSTRAINT branding_theme_pkey;
       public         postgres    false    227            ?           2606    24754    budget budget_pkey 
   CONSTRAINT     P   ALTER TABLE ONLY public.budget
    ADD CONSTRAINT budget_pkey PRIMARY KEY (id);
 <   ALTER TABLE ONLY public.budget DROP CONSTRAINT budget_pkey;
       public         postgres    false    229            ?           2606    24762    budgetitem budgetitem_pkey 
   CONSTRAINT     X   ALTER TABLE ONLY public.budgetitem
    ADD CONSTRAINT budgetitem_pkey PRIMARY KEY (id);
 D   ALTER TABLE ONLY public.budgetitem DROP CONSTRAINT budgetitem_pkey;
       public         postgres    false    231            ?           2606    24767 "   build_assembly build_assembly_pkey 
   CONSTRAINT     `   ALTER TABLE ONLY public.build_assembly
    ADD CONSTRAINT build_assembly_pkey PRIMARY KEY (id);
 L   ALTER TABLE ONLY public.build_assembly DROP CONSTRAINT build_assembly_pkey;
       public         postgres    false    232            ?           2606    24780 4   cash_purchase_estimates cash_purchase_estimates_pkey 
   CONSTRAINT     ?   ALTER TABLE ONLY public.cash_purchase_estimates
    ADD CONSTRAINT cash_purchase_estimates_pkey PRIMARY KEY (cash_purchase_id, elt);
 ^   ALTER TABLE ONLY public.cash_purchase_estimates DROP CONSTRAINT cash_purchase_estimates_pkey;
       public         postgres    false    234    234            ?           2606    24785 .   cash_purchase_orders cash_purchase_orders_pkey 
   CONSTRAINT        ALTER TABLE ONLY public.cash_purchase_orders
    ADD CONSTRAINT cash_purchase_orders_pkey PRIMARY KEY (cash_purchase_id, vtx);
 X   ALTER TABLE ONLY public.cash_purchase_orders DROP CONSTRAINT cash_purchase_orders_pkey;
       public         postgres    false    235    235            ?           2606    24775     cash_purchase cash_purchase_pkey 
   CONSTRAINT     ^   ALTER TABLE ONLY public.cash_purchase
    ADD CONSTRAINT cash_purchase_pkey PRIMARY KEY (id);
 J   ALTER TABLE ONLY public.cash_purchase DROP CONSTRAINT cash_purchase_pkey;
       public         postgres    false    233            ?           2606    24798 &   cash_sale_orders cash_sale_orders_pkey 
   CONSTRAINT     r   ALTER TABLE ONLY public.cash_sale_orders
    ADD CONSTRAINT cash_sale_orders_pkey PRIMARY KEY (cashsale_id, vtx);
 P   ALTER TABLE ONLY public.cash_sale_orders DROP CONSTRAINT cash_sale_orders_pkey;
       public         postgres    false    237    237            ?           2606    24793    cash_sales cash_sales_pkey 
   CONSTRAINT     X   ALTER TABLE ONLY public.cash_sales
    ADD CONSTRAINT cash_sales_pkey PRIMARY KEY (id);
 D   ALTER TABLE ONLY public.cash_sales DROP CONSTRAINT cash_sales_pkey;
       public         postgres    false    236            ?           2606    24809     cheque_layout cheque_layout_pkey 
   CONSTRAINT     ^   ALTER TABLE ONLY public.cheque_layout
    ADD CONSTRAINT cheque_layout_pkey PRIMARY KEY (id);
 J   ALTER TABLE ONLY public.cheque_layout DROP CONSTRAINT cheque_layout_pkey;
       public         postgres    false    239            ?           2606    24822    client client_email_id_key 
   CONSTRAINT     Y   ALTER TABLE ONLY public.client
    ADD CONSTRAINT client_email_id_key UNIQUE (email_id);
 D   ALTER TABLE ONLY public.client DROP CONSTRAINT client_email_id_key;
       public         postgres    false    241            ?           2606    24827 &   client_languages client_languages_pkey 
   CONSTRAINT     z   ALTER TABLE ONLY public.client_languages
    ADD CONSTRAINT client_languages_pkey PRIMARY KEY (client_id, language_code);
 P   ALTER TABLE ONLY public.client_languages DROP CONSTRAINT client_languages_pkey;
       public         postgres    false    242    242            ?           2606    24838 0   client_paypal_details client_paypal_details_pkey 
   CONSTRAINT     n   ALTER TABLE ONLY public.client_paypal_details
    ADD CONSTRAINT client_paypal_details_pkey PRIMARY KEY (id);
 Z   ALTER TABLE ONLY public.client_paypal_details DROP CONSTRAINT client_paypal_details_pkey;
       public         postgres    false    244            ?           2606    24820    client client_pkey 
   CONSTRAINT     P   ALTER TABLE ONLY public.client
    ADD CONSTRAINT client_pkey PRIMARY KEY (id);
 <   ALTER TABLE ONLY public.client DROP CONSTRAINT client_pkey;
       public         postgres    false    241            ?           2606    24846 ,   client_subscription client_subscription_pkey 
   CONSTRAINT     j   ALTER TABLE ONLY public.client_subscription
    ADD CONSTRAINT client_subscription_pkey PRIMARY KEY (id);
 V   ALTER TABLE ONLY public.client_subscription DROP CONSTRAINT client_subscription_pkey;
       public         postgres    false    246            ?           2606    24857 "   commodity_code commodity_code_pkey 
   CONSTRAINT     `   ALTER TABLE ONLY public.commodity_code
    ADD CONSTRAINT commodity_code_pkey PRIMARY KEY (id);
 L   ALTER TABLE ONLY public.commodity_code DROP CONSTRAINT commodity_code_pkey;
       public         postgres    false    248            ?           2606    24876 "   company_fields company_fields_pkey 
   CONSTRAINT     t   ALTER TABLE ONLY public.company_fields
    ADD CONSTRAINT company_fields_pkey PRIMARY KEY (company_id, field_name);
 L   ALTER TABLE ONLY public.company_fields DROP CONSTRAINT company_fields_pkey;
       public         postgres    false    251    251            ?           2606    24868    company company_pkey 
   CONSTRAINT     R   ALTER TABLE ONLY public.company
    ADD CONSTRAINT company_pkey PRIMARY KEY (id);
 >   ALTER TABLE ONLY public.company DROP CONSTRAINT company_pkey;
       public         postgres    false    250            ?           2606    24881 .   computation_pay_head computation_pay_head_pkey 
   CONSTRAINT     l   ALTER TABLE ONLY public.computation_pay_head
    ADD CONSTRAINT computation_pay_head_pkey PRIMARY KEY (id);
 X   ALTER TABLE ONLY public.computation_pay_head DROP CONSTRAINT computation_pay_head_pkey;
       public         postgres    false    252            ?           2606    24886 (   computation_slabs computation_slabs_pkey 
   CONSTRAINT     {   ALTER TABLE ONLY public.computation_slabs
    ADD CONSTRAINT computation_slabs_pkey PRIMARY KEY (pay_head_id, slab_index);
 R   ALTER TABLE ONLY public.computation_slabs DROP CONSTRAINT computation_slabs_pkey;
       public         postgres    false    253    253            ?           2606    24917 ,   credit_card_charges credit_card_charges_pkey 
   CONSTRAINT     j   ALTER TABLE ONLY public.credit_card_charges
    ADD CONSTRAINT credit_card_charges_pkey PRIMARY KEY (id);
 V   ALTER TABLE ONLY public.credit_card_charges DROP CONSTRAINT credit_card_charges_pkey;
       public         postgres    false    258            ?           2606    24897    creditrating creditrating_pkey 
   CONSTRAINT     \   ALTER TABLE ONLY public.creditrating
    ADD CONSTRAINT creditrating_pkey PRIMARY KEY (id);
 H   ALTER TABLE ONLY public.creditrating DROP CONSTRAINT creditrating_pkey;
       public         postgres    false    255            ?           2606    24909 .   credits_and_payments credits_and_payments_pkey 
   CONSTRAINT     l   ALTER TABLE ONLY public.credits_and_payments
    ADD CONSTRAINT credits_and_payments_pkey PRIMARY KEY (id);
 X   ALTER TABLE ONLY public.credits_and_payments DROP CONSTRAINT credits_and_payments_pkey;
       public         postgres    false    257            ?           2606    24928    currency currency_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.currency
    ADD CONSTRAINT currency_pkey PRIMARY KEY (id);
 @   ALTER TABLE ONLY public.currency DROP CONSTRAINT currency_pkey;
       public         postgres    false    260            ?           2606    24944 .   customer_credit_memo customer_credit_memo_pkey 
   CONSTRAINT     l   ALTER TABLE ONLY public.customer_credit_memo
    ADD CONSTRAINT customer_credit_memo_pkey PRIMARY KEY (id);
 X   ALTER TABLE ONLY public.customer_credit_memo DROP CONSTRAINT customer_credit_memo_pkey;
       public         postgres    false    262            ?           2606    24955 "   customer_group customer_group_pkey 
   CONSTRAINT     `   ALTER TABLE ONLY public.customer_group
    ADD CONSTRAINT customer_group_pkey PRIMARY KEY (id);
 L   ALTER TABLE ONLY public.customer_group DROP CONSTRAINT customer_group_pkey;
       public         postgres    false    264            ?           2606    24936    customer customer_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.customer
    ADD CONSTRAINT customer_pkey PRIMARY KEY (id);
 @   ALTER TABLE ONLY public.customer DROP CONSTRAINT customer_pkey;
       public         postgres    false    261            ?           2606    24963 ,   customer_prepayment customer_prepayment_pkey 
   CONSTRAINT     j   ALTER TABLE ONLY public.customer_prepayment
    ADD CONSTRAINT customer_prepayment_pkey PRIMARY KEY (id);
 V   ALTER TABLE ONLY public.customer_prepayment DROP CONSTRAINT customer_prepayment_pkey;
       public         postgres    false    265            ?           2606    24971 $   customer_refund customer_refund_pkey 
   CONSTRAINT     b   ALTER TABLE ONLY public.customer_refund
    ADD CONSTRAINT customer_refund_pkey PRIMARY KEY (id);
 N   ALTER TABLE ONLY public.customer_refund DROP CONSTRAINT customer_refund_pkey;
       public         postgres    false    266            ?           2606    24982    customfield customfield_pkey 
   CONSTRAINT     Z   ALTER TABLE ONLY public.customfield
    ADD CONSTRAINT customfield_pkey PRIMARY KEY (id);
 F   ALTER TABLE ONLY public.customfield DROP CONSTRAINT customfield_pkey;
       public         postgres    false    268            ?           2606    24993     delete_reason delete_reason_pkey 
   CONSTRAINT     ^   ALTER TABLE ONLY public.delete_reason
    ADD CONSTRAINT delete_reason_pkey PRIMARY KEY (id);
 J   ALTER TABLE ONLY public.delete_reason DROP CONSTRAINT delete_reason_pkey;
       public         postgres    false    270            ?           2606    24998 (   deposit_estimates deposit_estimates_pkey 
   CONSTRAINT     s   ALTER TABLE ONLY public.deposit_estimates
    ADD CONSTRAINT deposit_estimates_pkey PRIMARY KEY (deposit_id, elt);
 R   ALTER TABLE ONLY public.deposit_estimates DROP CONSTRAINT deposit_estimates_pkey;
       public         postgres    false    271    271            ?           2606    25006    depreciation depreciation_pkey 
   CONSTRAINT     \   ALTER TABLE ONLY public.depreciation
    ADD CONSTRAINT depreciation_pkey PRIMARY KEY (id);
 H   ALTER TABLE ONLY public.depreciation DROP CONSTRAINT depreciation_pkey;
       public         postgres    false    273            ?           2606    25017    developer developer_pkey 
   CONSTRAINT     V   ALTER TABLE ONLY public.developer
    ADD CONSTRAINT developer_pkey PRIMARY KEY (id);
 B   ALTER TABLE ONLY public.developer DROP CONSTRAINT developer_pkey;
       public         postgres    false    275            ?           2606    25028     email_account email_account_pkey 
   CONSTRAINT     ^   ALTER TABLE ONLY public.email_account
    ADD CONSTRAINT email_account_pkey PRIMARY KEY (id);
 J   ALTER TABLE ONLY public.email_account DROP CONSTRAINT email_account_pkey;
       public         postgres    false    277            ?           2606    25039 "   email_template email_template_pkey 
   CONSTRAINT     `   ALTER TABLE ONLY public.email_template
    ADD CONSTRAINT email_template_pkey PRIMARY KEY (id);
 L   ALTER TABLE ONLY public.email_template DROP CONSTRAINT email_template_pkey;
       public         postgres    false    279                       2606    45694 0   employee_compensation employee_compensation_pkey 
   CONSTRAINT     n   ALTER TABLE ONLY public.employee_compensation
    ADD CONSTRAINT employee_compensation_pkey PRIMARY KEY (id);
 Z   ALTER TABLE ONLY public.employee_compensation DROP CONSTRAINT employee_compensation_pkey;
       public         postgres    false    498            ?           2606    25058 "   employee_group employee_group_pkey 
   CONSTRAINT     `   ALTER TABLE ONLY public.employee_group
    ADD CONSTRAINT employee_group_pkey PRIMARY KEY (id);
 L   ALTER TABLE ONLY public.employee_group DROP CONSTRAINT employee_group_pkey;
       public         postgres    false    282                       2606    25066 :   employee_payhead_component employee_payhead_component_pkey 
   CONSTRAINT     x   ALTER TABLE ONLY public.employee_payhead_component
    ADD CONSTRAINT employee_payhead_component_pkey PRIMARY KEY (id);
 d   ALTER TABLE ONLY public.employee_payhead_component DROP CONSTRAINT employee_payhead_component_pkey;
       public         postgres    false    284                       2606    25074 6   employee_payment_details employee_payment_details_pkey 
   CONSTRAINT     t   ALTER TABLE ONLY public.employee_payment_details
    ADD CONSTRAINT employee_payment_details_pkey PRIMARY KEY (id);
 `   ALTER TABLE ONLY public.employee_payment_details DROP CONSTRAINT employee_payment_details_pkey;
       public         postgres    false    286            ?           2606    25047    employee employee_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.employee
    ADD CONSTRAINT employee_pkey PRIMARY KEY (id);
 @   ALTER TABLE ONLY public.employee DROP CONSTRAINT employee_pkey;
       public         postgres    false    280                       2606    45702    employee_tax employee_tax_pkey 
   CONSTRAINT     \   ALTER TABLE ONLY public.employee_tax
    ADD CONSTRAINT employee_tax_pkey PRIMARY KEY (id);
 H   ALTER TABLE ONLY public.employee_tax DROP CONSTRAINT employee_tax_pkey;
       public         postgres    false    500            	           2606    25092 (   enter_bill_orders enter_bill_orders_pkey 
   CONSTRAINT     v   ALTER TABLE ONLY public.enter_bill_orders
    ADD CONSTRAINT enter_bill_orders_pkey PRIMARY KEY (enter_bill_id, vtx);
 R   ALTER TABLE ONLY public.enter_bill_orders DROP CONSTRAINT enter_bill_orders_pkey;
       public         postgres    false    289    289                       2606    25087    enter_bill enter_bill_pkey 
   CONSTRAINT     X   ALTER TABLE ONLY public.enter_bill
    ADD CONSTRAINT enter_bill_pkey PRIMARY KEY (id);
 D   ALTER TABLE ONLY public.enter_bill DROP CONSTRAINT enter_bill_pkey;
       public         postgres    false    288                       2606    25079 ,   enterbill_estimates enterbill_estimates_pkey 
   CONSTRAINT     z   ALTER TABLE ONLY public.enterbill_estimates
    ADD CONSTRAINT enterbill_estimates_pkey PRIMARY KEY (enter_bill_id, elt);
 V   ALTER TABLE ONLY public.enterbill_estimates DROP CONSTRAINT enterbill_estimates_pkey;
       public         postgres    false    287    287                       2606    25100    estimate estimate_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.estimate
    ADD CONSTRAINT estimate_pkey PRIMARY KEY (id);
 @   ALTER TABLE ONLY public.estimate DROP CONSTRAINT estimate_pkey;
       public         postgres    false    290                       2606    25108    expense expense_pkey 
   CONSTRAINT     R   ALTER TABLE ONLY public.expense
    ADD CONSTRAINT expense_pkey PRIMARY KEY (id);
 >   ALTER TABLE ONLY public.expense DROP CONSTRAINT expense_pkey;
       public         postgres    false    291                       2606    25119    fiscal_year fiscal_year_pkey 
   CONSTRAINT     Z   ALTER TABLE ONLY public.fiscal_year
    ADD CONSTRAINT fiscal_year_pkey PRIMARY KEY (id);
 F   ALTER TABLE ONLY public.fiscal_year DROP CONSTRAINT fiscal_year_pkey;
       public         postgres    false    294                       2606    25141 ,   fixed_asset_history fixed_asset_history_pkey 
   CONSTRAINT     j   ALTER TABLE ONLY public.fixed_asset_history
    ADD CONSTRAINT fixed_asset_history_pkey PRIMARY KEY (id);
 V   ALTER TABLE ONLY public.fixed_asset_history DROP CONSTRAINT fixed_asset_history_pkey;
       public         postgres    false    298                       2606    25130    fixed_asset fixed_asset_pkey 
   CONSTRAINT     Z   ALTER TABLE ONLY public.fixed_asset
    ADD CONSTRAINT fixed_asset_pkey PRIMARY KEY (id);
 F   ALTER TABLE ONLY public.fixed_asset DROP CONSTRAINT fixed_asset_pkey;
       public         postgres    false    296                       2606    25146 *   flat_rate_pay_head flat_rate_pay_head_pkey 
   CONSTRAINT     h   ALTER TABLE ONLY public.flat_rate_pay_head
    ADD CONSTRAINT flat_rate_pay_head_pkey PRIMARY KEY (id);
 T   ALTER TABLE ONLY public.flat_rate_pay_head DROP CONSTRAINT flat_rate_pay_head_pkey;
       public         postgres    false    299                       2606    25151 &   formula_function formula_function_pkey 
   CONSTRAINT     }   ALTER TABLE ONLY public.formula_function
    ADD CONSTRAINT formula_function_pkey PRIMARY KEY (pay_head_id, function_index);
 P   ALTER TABLE ONLY public.formula_function DROP CONSTRAINT formula_function_pkey;
       public         postgres    false    300    300            1           2606    25261    i_m_user i_m_user_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.i_m_user
    ADD CONSTRAINT i_m_user_pkey PRIMARY KEY (id);
 @   ALTER TABLE ONLY public.i_m_user DROP CONSTRAINT i_m_user_pkey;
       public         postgres    false    322                       2606    25162     im_activation im_activation_pkey 
   CONSTRAINT     ^   ALTER TABLE ONLY public.im_activation
    ADD CONSTRAINT im_activation_pkey PRIMARY KEY (id);
 J   ALTER TABLE ONLY public.im_activation DROP CONSTRAINT im_activation_pkey;
       public         postgres    false    302                       2606    25178 4   inventory_assembly_item inventory_assembly_item_pkey 
   CONSTRAINT     r   ALTER TABLE ONLY public.inventory_assembly_item
    ADD CONSTRAINT inventory_assembly_item_pkey PRIMARY KEY (id);
 ^   ALTER TABLE ONLY public.inventory_assembly_item DROP CONSTRAINT inventory_assembly_item_pkey;
       public         postgres    false    305                       2606    25167 *   inventory_assembly inventory_assembly_pkey 
   CONSTRAINT     h   ALTER TABLE ONLY public.inventory_assembly
    ADD CONSTRAINT inventory_assembly_pkey PRIMARY KEY (id);
 T   ALTER TABLE ONLY public.inventory_assembly DROP CONSTRAINT inventory_assembly_pkey;
       public         postgres    false    303                       2606    25186 (   inventory_history inventory_history_pkey 
   CONSTRAINT     f   ALTER TABLE ONLY public.inventory_history
    ADD CONSTRAINT inventory_history_pkey PRIMARY KEY (id);
 R   ALTER TABLE ONLY public.inventory_history DROP CONSTRAINT inventory_history_pkey;
       public         postgres    false    307            !           2606    25194 *   inventory_purchase inventory_purchase_pkey 
   CONSTRAINT     h   ALTER TABLE ONLY public.inventory_purchase
    ADD CONSTRAINT inventory_purchase_pkey PRIMARY KEY (id);
 T   ALTER TABLE ONLY public.inventory_purchase DROP CONSTRAINT inventory_purchase_pkey;
       public         postgres    false    309            #           2606    25202 .   inventory_remap_task inventory_remap_task_pkey 
   CONSTRAINT     l   ALTER TABLE ONLY public.inventory_remap_task
    ADD CONSTRAINT inventory_remap_task_pkey PRIMARY KEY (id);
 X   ALTER TABLE ONLY public.inventory_remap_task DROP CONSTRAINT inventory_remap_task_pkey;
       public         postgres    false    311            '           2606    25215 (   invoice_estimates invoice_estimates_pkey 
   CONSTRAINT     s   ALTER TABLE ONLY public.invoice_estimates
    ADD CONSTRAINT invoice_estimates_pkey PRIMARY KEY (invoice_id, vtx);
 R   ALTER TABLE ONLY public.invoice_estimates DROP CONSTRAINT invoice_estimates_pkey;
       public         postgres    false    313    313            %           2606    25210    invoice invoice_pkey 
   CONSTRAINT     R   ALTER TABLE ONLY public.invoice
    ADD CONSTRAINT invoice_pkey PRIMARY KEY (id);
 >   ALTER TABLE ONLY public.invoice DROP CONSTRAINT invoice_pkey;
       public         postgres    false    312            )           2606    25226    item item_pkey 
   CONSTRAINT     L   ALTER TABLE ONLY public.item
    ADD CONSTRAINT item_pkey PRIMARY KEY (id);
 8   ALTER TABLE ONLY public.item DROP CONSTRAINT item_pkey;
       public         postgres    false    315            -           2606    25245    item_receipt item_receipt_pkey 
   CONSTRAINT     \   ALTER TABLE ONLY public.item_receipt
    ADD CONSTRAINT item_receipt_pkey PRIMARY KEY (id);
 H   ALTER TABLE ONLY public.item_receipt DROP CONSTRAINT item_receipt_pkey;
       public         postgres    false    318            /           2606    25253    item_update item_update_pkey 
   CONSTRAINT     Z   ALTER TABLE ONLY public.item_update
    ADD CONSTRAINT item_update_pkey PRIMARY KEY (id);
 F   ALTER TABLE ONLY public.item_update DROP CONSTRAINT item_update_pkey;
       public         postgres    false    320            +           2606    25237    itemgroup itemgroup_pkey 
   CONSTRAINT     V   ALTER TABLE ONLY public.itemgroup
    ADD CONSTRAINT itemgroup_pkey PRIMARY KEY (id);
 B   ALTER TABLE ONLY public.itemgroup DROP CONSTRAINT itemgroup_pkey;
       public         postgres    false    317            3           2606    25272    job job_pkey 
   CONSTRAINT     J   ALTER TABLE ONLY public.job
    ADD CONSTRAINT job_pkey PRIMARY KEY (id);
 6   ALTER TABLE ONLY public.job DROP CONSTRAINT job_pkey;
       public         postgres    false    324            5           2606    25277     journal_entry journal_entry_pkey 
   CONSTRAINT     ^   ALTER TABLE ONLY public.journal_entry
    ADD CONSTRAINT journal_entry_pkey PRIMARY KEY (id);
 J   ALTER TABLE ONLY public.journal_entry DROP CONSTRAINT journal_entry_pkey;
       public         postgres    false    325            9           2606    25290    key_messages key_messages_pkey 
   CONSTRAINT     l   ALTER TABLE ONLY public.key_messages
    ADD CONSTRAINT key_messages_pkey PRIMARY KEY (message_id, key_id);
 H   ALTER TABLE ONLY public.key_messages DROP CONSTRAINT key_messages_pkey;
       public         postgres    false    328    328            7           2606    25285    key key_pkey 
   CONSTRAINT     J   ALTER TABLE ONLY public.key
    ADD CONSTRAINT key_pkey PRIMARY KEY (id);
 6   ALTER TABLE ONLY public.key DROP CONSTRAINT key_pkey;
       public         postgres    false    327            ;           2606    25298    language language_pkey 
   CONSTRAINT     V   ALTER TABLE ONLY public.language
    ADD CONSTRAINT language_pkey PRIMARY KEY (code);
 @   ALTER TABLE ONLY public.language DROP CONSTRAINT language_pkey;
       public         postgres    false    329            =           2606    25309    license license_pkey 
   CONSTRAINT     R   ALTER TABLE ONLY public.license
    ADD CONSTRAINT license_pkey PRIMARY KEY (id);
 >   ALTER TABLE ONLY public.license DROP CONSTRAINT license_pkey;
       public         postgres    false    331            ?           2606    25317 &   license_purchase license_purchase_pkey 
   CONSTRAINT     d   ALTER TABLE ONLY public.license_purchase
    ADD CONSTRAINT license_purchase_pkey PRIMARY KEY (id);
 P   ALTER TABLE ONLY public.license_purchase DROP CONSTRAINT license_purchase_pkey;
       public         postgres    false    333            	           2606    46047     live_tax_rate live_tax_rate_pkey 
   CONSTRAINT     ^   ALTER TABLE ONLY public.live_tax_rate
    ADD CONSTRAINT live_tax_rate_pkey PRIMARY KEY (id);
 J   ALTER TABLE ONLY public.live_tax_rate DROP CONSTRAINT live_tax_rate_pkey;
       public         postgres    false    502                       2606    46085 ,   live_tax_rate_range live_tax_rate_range_pkey 
   CONSTRAINT     j   ALTER TABLE ONLY public.live_tax_rate_range
    ADD CONSTRAINT live_tax_rate_range_pkey PRIMARY KEY (id);
 V   ALTER TABLE ONLY public.live_tax_rate_range DROP CONSTRAINT live_tax_rate_range_pkey;
       public         postgres    false    504            A           2606    25328     local_message local_message_pkey 
   CONSTRAINT     ^   ALTER TABLE ONLY public.local_message
    ADD CONSTRAINT local_message_pkey PRIMARY KEY (id);
 J   ALTER TABLE ONLY public.local_message DROP CONSTRAINT local_message_pkey;
       public         postgres    false    335            C           2606    25339    location location_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.location
    ADD CONSTRAINT location_pkey PRIMARY KEY (id);
 @   ALTER TABLE ONLY public.location DROP CONSTRAINT location_pkey;
       public         postgres    false    337            E           2606    25344 &   maintanance_user maintanance_user_pkey 
   CONSTRAINT     l   ALTER TABLE ONLY public.maintanance_user
    ADD CONSTRAINT maintanance_user_pkey PRIMARY KEY (user_email);
 P   ALTER TABLE ONLY public.maintanance_user DROP CONSTRAINT maintanance_user_pkey;
       public         postgres    false    338            G           2606    25349    make_deposit make_deposit_pkey 
   CONSTRAINT     \   ALTER TABLE ONLY public.make_deposit
    ADD CONSTRAINT make_deposit_pkey PRIMARY KEY (id);
 H   ALTER TABLE ONLY public.make_deposit DROP CONSTRAINT make_deposit_pkey;
       public         postgres    false    339            I           2606    25360    measurement measurement_pkey 
   CONSTRAINT     Z   ALTER TABLE ONLY public.measurement
    ADD CONSTRAINT measurement_pkey PRIMARY KEY (id);
 F   ALTER TABLE ONLY public.measurement DROP CONSTRAINT measurement_pkey;
       public         postgres    false    341            O           2606    25387 $   message_or_task message_or_task_pkey 
   CONSTRAINT     b   ALTER TABLE ONLY public.message_or_task
    ADD CONSTRAINT message_or_task_pkey PRIMARY KEY (id);
 N   ALTER TABLE ONLY public.message_or_task DROP CONSTRAINT message_or_task_pkey;
       public         postgres    false    346            K           2606    25374    message message_pkey 
   CONSTRAINT     R   ALTER TABLE ONLY public.message
    ADD CONSTRAINT message_pkey PRIMARY KEY (id);
 >   ALTER TABLE ONLY public.message DROP CONSTRAINT message_pkey;
       public         postgres    false    344            M           2606    25376    message message_value_key 
   CONSTRAINT     U   ALTER TABLE ONLY public.message
    ADD CONSTRAINT message_value_key UNIQUE (value);
 C   ALTER TABLE ONLY public.message DROP CONSTRAINT message_value_key;
       public         postgres    false    344            Q           2606    25392     mobile_cookie mobile_cookie_pkey 
   CONSTRAINT     b   ALTER TABLE ONLY public.mobile_cookie
    ADD CONSTRAINT mobile_cookie_pkey PRIMARY KEY (cookie);
 J   ALTER TABLE ONLY public.mobile_cookie DROP CONSTRAINT mobile_cookie_pkey;
       public         postgres    false    347            S           2606    25403    news news_pkey 
   CONSTRAINT     L   ALTER TABLE ONLY public.news
    ADD CONSTRAINT news_pkey PRIMARY KEY (id);
 8   ALTER TABLE ONLY public.news DROP CONSTRAINT news_pkey;
       public         postgres    false    349            U           2606    25411 *   nominal_code_range nominal_code_range_pkey 
   CONSTRAINT     h   ALTER TABLE ONLY public.nominal_code_range
    ADD CONSTRAINT nominal_code_range_pkey PRIMARY KEY (id);
 T   ALTER TABLE ONLY public.nominal_code_range DROP CONSTRAINT nominal_code_range_pkey;
       public         postgres    false    351            g           2606    25495    pay_bill pay_bill_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.pay_bill
    ADD CONSTRAINT pay_bill_pkey PRIMARY KEY (id);
 @   ALTER TABLE ONLY public.pay_bill DROP CONSTRAINT pay_bill_pkey;
       public         postgres    false    365            i           2606    25500    pay_employee pay_employee_pkey 
   CONSTRAINT     \   ALTER TABLE ONLY public.pay_employee
    ADD CONSTRAINT pay_employee_pkey PRIMARY KEY (id);
 H   ALTER TABLE ONLY public.pay_employee DROP CONSTRAINT pay_employee_pkey;
       public         postgres    false    366            k           2606    25508    pay_expense pay_expense_pkey 
   CONSTRAINT     Z   ALTER TABLE ONLY public.pay_expense
    ADD CONSTRAINT pay_expense_pkey PRIMARY KEY (id);
 F   ALTER TABLE ONLY public.pay_expense DROP CONSTRAINT pay_expense_pkey;
       public         postgres    false    367            m           2606    25519    pay_head pay_head_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.pay_head
    ADD CONSTRAINT pay_head_pkey PRIMARY KEY (id);
 @   ALTER TABLE ONLY public.pay_head DROP CONSTRAINT pay_head_pkey;
       public         postgres    false    369            o           2606    25530     pay_roll_unit pay_roll_unit_pkey 
   CONSTRAINT     ^   ALTER TABLE ONLY public.pay_roll_unit
    ADD CONSTRAINT pay_roll_unit_pkey PRIMARY KEY (id);
 J   ALTER TABLE ONLY public.pay_roll_unit DROP CONSTRAINT pay_roll_unit_pkey;
       public         postgres    false    371            q           2606    25535    pay_run pay_run_pkey 
   CONSTRAINT     R   ALTER TABLE ONLY public.pay_run
    ADD CONSTRAINT pay_run_pkey PRIMARY KEY (id);
 >   ALTER TABLE ONLY public.pay_run DROP CONSTRAINT pay_run_pkey;
       public         postgres    false    372            u           2606    25551 *   pay_structure_item pay_structure_item_pkey 
   CONSTRAINT     h   ALTER TABLE ONLY public.pay_structure_item
    ADD CONSTRAINT pay_structure_item_pkey PRIMARY KEY (id);
 T   ALTER TABLE ONLY public.pay_structure_item DROP CONSTRAINT pay_structure_item_pkey;
       public         postgres    false    376            s           2606    25543     pay_structure pay_structure_pkey 
   CONSTRAINT     ^   ALTER TABLE ONLY public.pay_structure
    ADD CONSTRAINT pay_structure_pkey PRIMARY KEY (id);
 J   ALTER TABLE ONLY public.pay_structure DROP CONSTRAINT pay_structure_pkey;
       public         postgres    false    374            w           2606    25559    pay_tax pay_tax_pkey 
   CONSTRAINT     R   ALTER TABLE ONLY public.pay_tax
    ADD CONSTRAINT pay_tax_pkey PRIMARY KEY (id);
 >   ALTER TABLE ONLY public.pay_tax DROP CONSTRAINT pay_tax_pkey;
       public         postgres    false    377            Y           2606    25430     payee_address payee_address_pkey 
   CONSTRAINT     ?   ALTER TABLE ONLY public.payee_address
    ADD CONSTRAINT payee_address_pkey PRIMARY KEY (payee_id, type, address1, street, city, state, zip, country, is_selected);
 J   ALTER TABLE ONLY public.payee_address DROP CONSTRAINT payee_address_pkey;
       public         postgres    false    354    354    354    354    354    354    354    354    354            [           2606    25438     payee_contact payee_contact_pkey 
   CONSTRAINT     ?   ALTER TABLE ONLY public.payee_contact
    ADD CONSTRAINT payee_contact_pkey PRIMARY KEY (payee_id, is_primary, name, title, business_phone, email);
 J   ALTER TABLE ONLY public.payee_contact DROP CONSTRAINT payee_contact_pkey;
       public         postgres    false    355    355    355    355    355    355            ]           2606    25449 *   payee_customfields payee_customfields_pkey 
   CONSTRAINT     h   ALTER TABLE ONLY public.payee_customfields
    ADD CONSTRAINT payee_customfields_pkey PRIMARY KEY (id);
 T   ALTER TABLE ONLY public.payee_customfields DROP CONSTRAINT payee_customfields_pkey;
       public         postgres    false    357            _           2606    25457    payee_fields payee_fields_pkey 
   CONSTRAINT     t   ALTER TABLE ONLY public.payee_fields
    ADD CONSTRAINT payee_fields_pkey PRIMARY KEY (payee_id, payee_field_name);
 H   ALTER TABLE ONLY public.payee_fields DROP CONSTRAINT payee_fields_pkey;
       public         postgres    false    358    358            W           2606    25422    payee payee_pkey 
   CONSTRAINT     N   ALTER TABLE ONLY public.payee
    ADD CONSTRAINT payee_pkey PRIMARY KEY (id);
 :   ALTER TABLE ONLY public.payee DROP CONSTRAINT payee_pkey;
       public         postgres    false    353            a           2606    25465    payee_update payee_update_pkey 
   CONSTRAINT     \   ALTER TABLE ONLY public.payee_update
    ADD CONSTRAINT payee_update_pkey PRIMARY KEY (id);
 H   ALTER TABLE ONLY public.payee_update DROP CONSTRAINT payee_update_pkey;
       public         postgres    false    360            c           2606    25476    paymentterms paymentterms_pkey 
   CONSTRAINT     \   ALTER TABLE ONLY public.paymentterms
    ADD CONSTRAINT paymentterms_pkey PRIMARY KEY (id);
 H   ALTER TABLE ONLY public.paymentterms DROP CONSTRAINT paymentterms_pkey;
       public         postgres    false    362            e           2606    25487 *   paypal_transaction paypal_transaction_pkey 
   CONSTRAINT     h   ALTER TABLE ONLY public.paypal_transaction
    ADD CONSTRAINT paypal_transaction_pkey PRIMARY KEY (id);
 T   ALTER TABLE ONLY public.paypal_transaction DROP CONSTRAINT paypal_transaction_pkey;
       public         postgres    false    364            y           2606    25567 0   portlet_configuration portlet_configuration_pkey 
   CONSTRAINT     n   ALTER TABLE ONLY public.portlet_configuration
    ADD CONSTRAINT portlet_configuration_pkey PRIMARY KEY (id);
 Z   ALTER TABLE ONLY public.portlet_configuration DROP CONSTRAINT portlet_configuration_pkey;
       public         postgres    false    379            {           2606    25575 H   portlet_configuration_portletdata portlet_configuration_portletdata_pkey 
   CONSTRAINT     ?   ALTER TABLE ONLY public.portlet_configuration_portletdata
    ADD CONSTRAINT portlet_configuration_portletdata_pkey PRIMARY KEY (id, key);
 r   ALTER TABLE ONLY public.portlet_configuration_portletdata DROP CONSTRAINT portlet_configuration_portletdata_pkey;
       public         postgres    false    380    380            }           2606    25583 :   portlet_page_configuration portlet_page_configuration_pkey 
   CONSTRAINT     x   ALTER TABLE ONLY public.portlet_page_configuration
    ADD CONSTRAINT portlet_page_configuration_pkey PRIMARY KEY (id);
 d   ALTER TABLE ONLY public.portlet_page_configuration DROP CONSTRAINT portlet_page_configuration_pkey;
       public         postgres    false    382                       2606    25594    pricelevel pricelevel_pkey 
   CONSTRAINT     X   ALTER TABLE ONLY public.pricelevel
    ADD CONSTRAINT pricelevel_pkey PRIMARY KEY (id);
 D   ALTER TABLE ONLY public.pricelevel DROP CONSTRAINT pricelevel_pkey;
       public         postgres    false    384            ?           2606    25599    property property_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.property
    ADD CONSTRAINT property_pkey PRIMARY KEY (id);
 @   ALTER TABLE ONLY public.property DROP CONSTRAINT property_pkey;
       public         postgres    false    385            ?           2606    25607 "   purchase_order purchase_order_pkey 
   CONSTRAINT     `   ALTER TABLE ONLY public.purchase_order
    ADD CONSTRAINT purchase_order_pkey PRIMARY KEY (id);
 L   ALTER TABLE ONLY public.purchase_order DROP CONSTRAINT purchase_order_pkey;
       public         postgres    false    386            ?           2606    25615 $   receive_payment receive_payment_pkey 
   CONSTRAINT     b   ALTER TABLE ONLY public.receive_payment
    ADD CONSTRAINT receive_payment_pkey PRIMARY KEY (id);
 N   ALTER TABLE ONLY public.receive_payment DROP CONSTRAINT receive_payment_pkey;
       public         postgres    false    387            ?           2606    25623    receive_vat receive_vat_pkey 
   CONSTRAINT     Z   ALTER TABLE ONLY public.receive_vat
    ADD CONSTRAINT receive_vat_pkey PRIMARY KEY (id);
 F   ALTER TABLE ONLY public.receive_vat DROP CONSTRAINT receive_vat_pkey;
       public         postgres    false    388            ?           2606    25642 ,   reconciliation_item reconciliation_item_pkey 
   CONSTRAINT     j   ALTER TABLE ONLY public.reconciliation_item
    ADD CONSTRAINT reconciliation_item_pkey PRIMARY KEY (id);
 V   ALTER TABLE ONLY public.reconciliation_item DROP CONSTRAINT reconciliation_item_pkey;
       public         postgres    false    392            ?           2606    25631 "   reconciliation reconciliation_pkey 
   CONSTRAINT     `   ALTER TABLE ONLY public.reconciliation
    ADD CONSTRAINT reconciliation_pkey PRIMARY KEY (id);
 L   ALTER TABLE ONLY public.reconciliation DROP CONSTRAINT reconciliation_pkey;
       public         postgres    false    390            ?           2606    25653 0   recurring_transaction recurring_transaction_pkey 
   CONSTRAINT     n   ALTER TABLE ONLY public.recurring_transaction
    ADD CONSTRAINT recurring_transaction_pkey PRIMARY KEY (id);
 Z   ALTER TABLE ONLY public.recurring_transaction DROP CONSTRAINT recurring_transaction_pkey;
       public         postgres    false    394            ?           2606    25655 >   recurring_transaction recurring_transaction_transaction_id_key 
   CONSTRAINT     ?   ALTER TABLE ONLY public.recurring_transaction
    ADD CONSTRAINT recurring_transaction_transaction_id_key UNIQUE (transaction_id);
 h   ALTER TABLE ONLY public.recurring_transaction DROP CONSTRAINT recurring_transaction_transaction_id_key;
       public         postgres    false    394            ?           2606    25666 $   remember_me_key remember_me_key_pkey 
   CONSTRAINT     b   ALTER TABLE ONLY public.remember_me_key
    ADD CONSTRAINT remember_me_key_pkey PRIMARY KEY (id);
 N   ALTER TABLE ONLY public.remember_me_key DROP CONSTRAINT remember_me_key_pkey;
       public         postgres    false    396            ?           2606    25674    reminder reminder_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.reminder
    ADD CONSTRAINT reminder_pkey PRIMARY KEY (id);
 @   ALTER TABLE ONLY public.reminder DROP CONSTRAINT reminder_pkey;
       public         postgres    false    398            ?           2606    25682 .   reset_password_token reset_password_token_pkey 
   CONSTRAINT     l   ALTER TABLE ONLY public.reset_password_token
    ADD CONSTRAINT reset_password_token_pkey PRIMARY KEY (id);
 X   ALTER TABLE ONLY public.reset_password_token DROP CONSTRAINT reset_password_token_pkey;
       public         postgres    false    400            ?           2606    25693    sales_person sales_person_pkey 
   CONSTRAINT     \   ALTER TABLE ONLY public.sales_person
    ADD CONSTRAINT sales_person_pkey PRIMARY KEY (id);
 H   ALTER TABLE ONLY public.sales_person DROP CONSTRAINT sales_person_pkey;
       public         postgres    false    402            ?           2606    25701 *   server_maintanance server_maintanance_pkey 
   CONSTRAINT     h   ALTER TABLE ONLY public.server_maintanance
    ADD CONSTRAINT server_maintanance_pkey PRIMARY KEY (id);
 T   ALTER TABLE ONLY public.server_maintanance DROP CONSTRAINT server_maintanance_pkey;
       public         postgres    false    404            ?           2606    25712 "   shippingmethod shippingmethod_pkey 
   CONSTRAINT     `   ALTER TABLE ONLY public.shippingmethod
    ADD CONSTRAINT shippingmethod_pkey PRIMARY KEY (id);
 L   ALTER TABLE ONLY public.shippingmethod DROP CONSTRAINT shippingmethod_pkey;
       public         postgres    false    406            ?           2606    25723     shippingterms shippingterms_pkey 
   CONSTRAINT     ^   ALTER TABLE ONLY public.shippingterms
    ADD CONSTRAINT shippingterms_pkey PRIMARY KEY (id);
 J   ALTER TABLE ONLY public.shippingterms DROP CONSTRAINT shippingterms_pkey;
       public         postgres    false    408            ?           2606    25731    statement statement_pkey 
   CONSTRAINT     V   ALTER TABLE ONLY public.statement
    ADD CONSTRAINT statement_pkey PRIMARY KEY (id);
 B   ALTER TABLE ONLY public.statement DROP CONSTRAINT statement_pkey;
       public         postgres    false    410            ?           2606    25742 &   statement_record statement_record_pkey 
   CONSTRAINT     d   ALTER TABLE ONLY public.statement_record
    ADD CONSTRAINT statement_record_pkey PRIMARY KEY (id);
 P   ALTER TABLE ONLY public.statement_record DROP CONSTRAINT statement_record_pkey;
       public         postgres    false    412            ?           2606    25747 &   stock_adjustment stock_adjustment_pkey 
   CONSTRAINT     d   ALTER TABLE ONLY public.stock_adjustment
    ADD CONSTRAINT stock_adjustment_pkey PRIMARY KEY (id);
 P   ALTER TABLE ONLY public.stock_adjustment DROP CONSTRAINT stock_adjustment_pkey;
       public         postgres    false    413            ?           2606    25755 ,   stock_transfer_item stock_transfer_item_pkey 
   CONSTRAINT     j   ALTER TABLE ONLY public.stock_transfer_item
    ADD CONSTRAINT stock_transfer_item_pkey PRIMARY KEY (id);
 V   ALTER TABLE ONLY public.stock_transfer_item DROP CONSTRAINT stock_transfer_item_pkey;
       public         postgres    false    415            ?           2606    25763    subscription subscription_pkey 
   CONSTRAINT     \   ALTER TABLE ONLY public.subscription
    ADD CONSTRAINT subscription_pkey PRIMARY KEY (id);
 H   ALTER TABLE ONLY public.subscription DROP CONSTRAINT subscription_pkey;
       public         postgres    false    417            ?           2606    25768 "   supported_user supported_user_pkey 
   CONSTRAINT     f   ALTER TABLE ONLY public.supported_user
    ADD CONSTRAINT supported_user_pkey PRIMARY KEY (email_id);
 L   ALTER TABLE ONLY public.supported_user DROP CONSTRAINT supported_user_pkey;
       public         postgres    false    418            ?           2606    25786 "   tax_adjustment tax_adjustment_pkey 
   CONSTRAINT     `   ALTER TABLE ONLY public.tax_adjustment
    ADD CONSTRAINT tax_adjustment_pkey PRIMARY KEY (id);
 L   ALTER TABLE ONLY public.tax_adjustment DROP CONSTRAINT tax_adjustment_pkey;
       public         postgres    false    422            ?           2606    25797    tax_code tax_code_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.tax_code
    ADD CONSTRAINT tax_code_pkey PRIMARY KEY (id);
 @   ALTER TABLE ONLY public.tax_code DROP CONSTRAINT tax_code_pkey;
       public         postgres    false    424                       2606    46494 6   tax_computation_pay_head tax_computation_pay_head_pkey 
   CONSTRAINT     t   ALTER TABLE ONLY public.tax_computation_pay_head
    ADD CONSTRAINT tax_computation_pay_head_pkey PRIMARY KEY (id);
 `   ALTER TABLE ONLY public.tax_computation_pay_head DROP CONSTRAINT tax_computation_pay_head_pkey;
       public         postgres    false    505            ?           2606    25802    tax_group tax_group_pkey 
   CONSTRAINT     V   ALTER TABLE ONLY public.tax_group
    ADD CONSTRAINT tax_group_pkey PRIMARY KEY (id);
 B   ALTER TABLE ONLY public.tax_group DROP CONSTRAINT tax_group_pkey;
       public         postgres    false    425            ?           2606    25807 *   tax_group_tax_item tax_group_tax_item_pkey 
   CONSTRAINT     w   ALTER TABLE ONLY public.tax_group_tax_item
    ADD CONSTRAINT tax_group_tax_item_pkey PRIMARY KEY (tax_group_id, vtx);
 T   ALTER TABLE ONLY public.tax_group_tax_item DROP CONSTRAINT tax_group_tax_item_pkey;
       public         postgres    false    426    426            ?           2606    25823 $   tax_item_groups tax_item_groups_pkey 
   CONSTRAINT     b   ALTER TABLE ONLY public.tax_item_groups
    ADD CONSTRAINT tax_item_groups_pkey PRIMARY KEY (id);
 N   ALTER TABLE ONLY public.tax_item_groups DROP CONSTRAINT tax_item_groups_pkey;
       public         postgres    false    429            ?           2606    25812    tax_item tax_item_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.tax_item
    ADD CONSTRAINT tax_item_pkey PRIMARY KEY (id);
 @   ALTER TABLE ONLY public.tax_item DROP CONSTRAINT tax_item_pkey;
       public         postgres    false    427            ?           2606    25831 .   tax_rate_calculation tax_rate_calculation_pkey 
   CONSTRAINT     l   ALTER TABLE ONLY public.tax_rate_calculation
    ADD CONSTRAINT tax_rate_calculation_pkey PRIMARY KEY (id);
 X   ALTER TABLE ONLY public.tax_rate_calculation DROP CONSTRAINT tax_rate_calculation_pkey;
       public         postgres    false    431            ?           2606    25844 &   tax_return_entry tax_return_entry_pkey 
   CONSTRAINT     d   ALTER TABLE ONLY public.tax_return_entry
    ADD CONSTRAINT tax_return_entry_pkey PRIMARY KEY (id);
 P   ALTER TABLE ONLY public.tax_return_entry DROP CONSTRAINT tax_return_entry_pkey;
       public         postgres    false    434            ?           2606    25836    tax_return tax_return_pkey 
   CONSTRAINT     X   ALTER TABLE ONLY public.tax_return
    ADD CONSTRAINT tax_return_pkey PRIMARY KEY (id);
 D   ALTER TABLE ONLY public.tax_return DROP CONSTRAINT tax_return_pkey;
       public         postgres    false    432            ?           2606    25773    taxagency taxagency_pkey 
   CONSTRAINT     V   ALTER TABLE ONLY public.taxagency
    ADD CONSTRAINT taxagency_pkey PRIMARY KEY (id);
 B   ALTER TABLE ONLY public.taxagency DROP CONSTRAINT taxagency_pkey;
       public         postgres    false    419            ?           2606    25781    taxrates taxrates_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.taxrates
    ADD CONSTRAINT taxrates_pkey PRIMARY KEY (id);
 @   ALTER TABLE ONLY public.taxrates DROP CONSTRAINT taxrates_pkey;
       public         postgres    false    421            ?           2606    25885 (   tds_chalan_detail tds_chalan_detail_pkey 
   CONSTRAINT     f   ALTER TABLE ONLY public.tds_chalan_detail
    ADD CONSTRAINT tds_chalan_detail_pkey PRIMARY KEY (id);
 R   ALTER TABLE ONLY public.tds_chalan_detail DROP CONSTRAINT tds_chalan_detail_pkey;
       public         postgres    false    441            ?           2606    25855 *   tdsdeductormasters tdsdeductormasters_pkey 
   CONSTRAINT     h   ALTER TABLE ONLY public.tdsdeductormasters
    ADD CONSTRAINT tdsdeductormasters_pkey PRIMARY KEY (id);
 T   ALTER TABLE ONLY public.tdsdeductormasters DROP CONSTRAINT tdsdeductormasters_pkey;
       public         postgres    false    436            ?           2606    25866 .   tdsresponsibleperson tdsresponsibleperson_pkey 
   CONSTRAINT     l   ALTER TABLE ONLY public.tdsresponsibleperson
    ADD CONSTRAINT tdsresponsibleperson_pkey PRIMARY KEY (id);
 X   ALTER TABLE ONLY public.tdsresponsibleperson DROP CONSTRAINT tdsresponsibleperson_pkey;
       public         postgres    false    438            ?           2606    25877 *   tdstransactionitem tdstransactionitem_pkey 
   CONSTRAINT     h   ALTER TABLE ONLY public.tdstransactionitem
    ADD CONSTRAINT tdstransactionitem_pkey PRIMARY KEY (id);
 T   ALTER TABLE ONLY public.tdstransactionitem DROP CONSTRAINT tdstransactionitem_pkey;
       public         postgres    false    440            ?           2606    25908 F   transaction_credits_and_payments transaction_credits_and_payments_pkey 
   CONSTRAINT     ?   ALTER TABLE ONLY public.transaction_credits_and_payments
    ADD CONSTRAINT transaction_credits_and_payments_pkey PRIMARY KEY (id);
 p   ALTER TABLE ONLY public.transaction_credits_and_payments DROP CONSTRAINT transaction_credits_and_payments_pkey;
       public         postgres    false    445            ?           2606    25919 6   transaction_deposit_item transaction_deposit_item_pkey 
   CONSTRAINT     t   ALTER TABLE ONLY public.transaction_deposit_item
    ADD CONSTRAINT transaction_deposit_item_pkey PRIMARY KEY (id);
 `   ALTER TABLE ONLY public.transaction_deposit_item DROP CONSTRAINT transaction_deposit_item_pkey;
       public         postgres    false    447            ?           2606    25930 ,   transaction_expense transaction_expense_pkey 
   CONSTRAINT     j   ALTER TABLE ONLY public.transaction_expense
    ADD CONSTRAINT transaction_expense_pkey PRIMARY KEY (id);
 V   ALTER TABLE ONLY public.transaction_expense DROP CONSTRAINT transaction_expense_pkey;
       public         postgres    false    449            ?           2606    25941 ,   transaction_history transaction_history_pkey 
   CONSTRAINT     j   ALTER TABLE ONLY public.transaction_history
    ADD CONSTRAINT transaction_history_pkey PRIMARY KEY (id);
 V   ALTER TABLE ONLY public.transaction_history DROP CONSTRAINT transaction_history_pkey;
       public         postgres    false    451            ?           2606    25952 &   transaction_item transaction_item_pkey 
   CONSTRAINT     d   ALTER TABLE ONLY public.transaction_item
    ADD CONSTRAINT transaction_item_pkey PRIMARY KEY (id);
 P   ALTER TABLE ONLY public.transaction_item DROP CONSTRAINT transaction_item_pkey;
       public         postgres    false    453            ?           2606    25960 F   transaction_make_deposit_entries transaction_make_deposit_entries_pkey 
   CONSTRAINT     ?   ALTER TABLE ONLY public.transaction_make_deposit_entries
    ADD CONSTRAINT transaction_make_deposit_entries_pkey PRIMARY KEY (id);
 p   ALTER TABLE ONLY public.transaction_make_deposit_entries DROP CONSTRAINT transaction_make_deposit_entries_pkey;
       public         postgres    false    455            ?           2606    25979 6   transaction_pay_employee transaction_pay_employee_pkey 
   CONSTRAINT     t   ALTER TABLE ONLY public.transaction_pay_employee
    ADD CONSTRAINT transaction_pay_employee_pkey PRIMARY KEY (id);
 `   ALTER TABLE ONLY public.transaction_pay_employee DROP CONSTRAINT transaction_pay_employee_pkey;
       public         postgres    false    459            ?           2606    25987 4   transaction_pay_expense transaction_pay_expense_pkey 
   CONSTRAINT     r   ALTER TABLE ONLY public.transaction_pay_expense
    ADD CONSTRAINT transaction_pay_expense_pkey PRIMARY KEY (id);
 ^   ALTER TABLE ONLY public.transaction_pay_expense DROP CONSTRAINT transaction_pay_expense_pkey;
       public         postgres    false    461            ?           2606    25995 ,   transaction_pay_tax transaction_pay_tax_pkey 
   CONSTRAINT     j   ALTER TABLE ONLY public.transaction_pay_tax
    ADD CONSTRAINT transaction_pay_tax_pkey PRIMARY KEY (id);
 V   ALTER TABLE ONLY public.transaction_pay_tax DROP CONSTRAINT transaction_pay_tax_pkey;
       public         postgres    false    463            ?           2606    25971 ,   transaction_paybill transaction_paybill_pkey 
   CONSTRAINT     j   ALTER TABLE ONLY public.transaction_paybill
    ADD CONSTRAINT transaction_paybill_pkey PRIMARY KEY (id);
 V   ALTER TABLE ONLY public.transaction_paybill DROP CONSTRAINT transaction_paybill_pkey;
       public         postgres    false    457            ?           2606    25896    transaction transaction_pkey 
   CONSTRAINT     Z   ALTER TABLE ONLY public.transaction
    ADD CONSTRAINT transaction_pkey PRIMARY KEY (id);
 F   ALTER TABLE ONLY public.transaction DROP CONSTRAINT transaction_pkey;
       public         postgres    false    443            ?           2606    26006 <   transaction_receive_payment transaction_receive_payment_pkey 
   CONSTRAINT     z   ALTER TABLE ONLY public.transaction_receive_payment
    ADD CONSTRAINT transaction_receive_payment_pkey PRIMARY KEY (id);
 f   ALTER TABLE ONLY public.transaction_receive_payment DROP CONSTRAINT transaction_receive_payment_pkey;
       public         postgres    false    465            ?           2606    26014 4   transaction_receive_vat transaction_receive_vat_pkey 
   CONSTRAINT     r   ALTER TABLE ONLY public.transaction_receive_vat
    ADD CONSTRAINT transaction_receive_vat_pkey PRIMARY KEY (id);
 ^   ALTER TABLE ONLY public.transaction_receive_vat DROP CONSTRAINT transaction_receive_vat_pkey;
       public         postgres    false    467            ?           2606    26022     transfer_fund transfer_fund_pkey 
   CONSTRAINT     ^   ALTER TABLE ONLY public.transfer_fund
    ADD CONSTRAINT transfer_fund_pkey PRIMARY KEY (id);
 J   ALTER TABLE ONLY public.transfer_fund DROP CONSTRAINT transfer_fund_pkey;
       public         postgres    false    468            ?           2606    26041 $   unit_of_measure unit_of_measure_pkey 
   CONSTRAINT     b   ALTER TABLE ONLY public.unit_of_measure
    ADD CONSTRAINT unit_of_measure_pkey PRIMARY KEY (id);
 N   ALTER TABLE ONLY public.unit_of_measure DROP CONSTRAINT unit_of_measure_pkey;
       public         postgres    false    472            ?           2606    26030    unit unit_pkey 
   CONSTRAINT     L   ALTER TABLE ONLY public.unit
    ADD CONSTRAINT unit_pkey PRIMARY KEY (id);
 8   ALTER TABLE ONLY public.unit DROP CONSTRAINT unit_pkey;
       public         postgres    false    470                       2606    46499 0   user_defined_pay_head user_defined_pay_head_pkey 
   CONSTRAINT     n   ALTER TABLE ONLY public.user_defined_pay_head
    ADD CONSTRAINT user_defined_pay_head_pkey PRIMARY KEY (id);
 Z   ALTER TABLE ONLY public.user_defined_pay_head DROP CONSTRAINT user_defined_pay_head_pkey;
       public         postgres    false    506            ?           2606    26059 :   user_defined_payhead_items user_defined_payhead_items_pkey 
   CONSTRAINT     ?   ALTER TABLE ONLY public.user_defined_payhead_items
    ADD CONSTRAINT user_defined_payhead_items_pkey PRIMARY KEY (user_defined_payhead_item_id, user_defined_payhead_item_index);
 d   ALTER TABLE ONLY public.user_defined_payhead_items DROP CONSTRAINT user_defined_payhead_items_pkey;
       public         postgres    false    475    475            ?           2606    26072 &   user_permissions user_permissions_pkey 
   CONSTRAINT     d   ALTER TABLE ONLY public.user_permissions
    ADD CONSTRAINT user_permissions_pkey PRIMARY KEY (id);
 P   ALTER TABLE ONLY public.user_permissions DROP CONSTRAINT user_permissions_pkey;
       public         postgres    false    477            ?           2606    26052    users users_pkey 
   CONSTRAINT     N   ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);
 :   ALTER TABLE ONLY public.users DROP CONSTRAINT users_pkey;
       public         postgres    false    474            ?           2606    26054    users users_unique_id_key 
   CONSTRAINT     Y   ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_unique_id_key UNIQUE (unique_id);
 C   ALTER TABLE ONLY public.users DROP CONSTRAINT users_unique_id_key;
       public         postgres    false    474            ?           2606    26083    vatreturnbox vatreturnbox_pkey 
   CONSTRAINT     \   ALTER TABLE ONLY public.vatreturnbox
    ADD CONSTRAINT vatreturnbox_pkey PRIMARY KEY (id);
 H   ALTER TABLE ONLY public.vatreturnbox DROP CONSTRAINT vatreturnbox_pkey;
       public         postgres    false    479            ?           2606    26099 *   vendor_credit_memo vendor_credit_memo_pkey 
   CONSTRAINT     h   ALTER TABLE ONLY public.vendor_credit_memo
    ADD CONSTRAINT vendor_credit_memo_pkey PRIMARY KEY (id);
 T   ALTER TABLE ONLY public.vendor_credit_memo DROP CONSTRAINT vendor_credit_memo_pkey;
       public         postgres    false    481            ?           2606    26110    vendor_group vendor_group_pkey 
   CONSTRAINT     \   ALTER TABLE ONLY public.vendor_group
    ADD CONSTRAINT vendor_group_pkey PRIMARY KEY (id);
 H   ALTER TABLE ONLY public.vendor_group DROP CONSTRAINT vendor_group_pkey;
       public         postgres    false    483            ?           2606    26118 "   vendor_payment vendor_payment_pkey 
   CONSTRAINT     `   ALTER TABLE ONLY public.vendor_payment
    ADD CONSTRAINT vendor_payment_pkey PRIMARY KEY (id);
 L   ALTER TABLE ONLY public.vendor_payment DROP CONSTRAINT vendor_payment_pkey;
       public         postgres    false    484            ?           2606    26091    vendor vendor_pkey 
   CONSTRAINT     P   ALTER TABLE ONLY public.vendor
    ADD CONSTRAINT vendor_pkey PRIMARY KEY (id);
 <   ALTER TABLE ONLY public.vendor DROP CONSTRAINT vendor_pkey;
       public         postgres    false    480            ?           2606    26126    vote vote_pkey 
   CONSTRAINT     L   ALTER TABLE ONLY public.vote
    ADD CONSTRAINT vote_pkey PRIMARY KEY (id);
 8   ALTER TABLE ONLY public.vote DROP CONSTRAINT vote_pkey;
       public         postgres    false    486            ?           2606    26137    warehouse warehouse_pkey 
   CONSTRAINT     V   ALTER TABLE ONLY public.warehouse
    ADD CONSTRAINT warehouse_pkey PRIMARY KEY (id);
 B   ALTER TABLE ONLY public.warehouse DROP CONSTRAINT warehouse_pkey;
       public         postgres    false    488            ?           2606    26142 *   warehouse_transfer warehouse_transfer_pkey 
   CONSTRAINT     h   ALTER TABLE ONLY public.warehouse_transfer
    ADD CONSTRAINT warehouse_transfer_pkey PRIMARY KEY (id);
 T   ALTER TABLE ONLY public.warehouse_transfer DROP CONSTRAINT warehouse_transfer_pkey;
       public         postgres    false    489                       2606    26155 2   write_checks_estimates write_checks_estimates_pkey 
   CONSTRAINT     ?   ALTER TABLE ONLY public.write_checks_estimates
    ADD CONSTRAINT write_checks_estimates_pkey PRIMARY KEY (write_checks_id, elt);
 \   ALTER TABLE ONLY public.write_checks_estimates DROP CONSTRAINT write_checks_estimates_pkey;
       public         postgres    false    491    491                       2606    26150    write_checks write_checks_pkey 
   CONSTRAINT     \   ALTER TABLE ONLY public.write_checks
    ADD CONSTRAINT write_checks_pkey PRIMARY KEY (id);
 H   ALTER TABLE ONLY public.write_checks DROP CONSTRAINT write_checks_pkey;
       public         postgres    false    490            >           2620    53944    client changedpasswordcount    TRIGGER     ?   CREATE TRIGGER changedpasswordcount AFTER UPDATE ON public.client FOR EACH ROW EXECUTE PROCEDURE public.changedpasswordcount();
 4   DROP TRIGGER changedpasswordcount ON public.client;
       public       postgres    false    529    241            ?           2620    53947    client clientupdate    TRIGGER     p   CREATE TRIGGER clientupdate AFTER UPDATE ON public.client FOR EACH ROW EXECUTE PROCEDURE public.clientupdate();
 ,   DROP TRIGGER clientupdate ON public.client;
       public       postgres    false    241    526            @           2620    53942    company companiescount    TRIGGER     u   CREATE TRIGGER companiescount AFTER INSERT ON public.company FOR EACH ROW EXECUTE PROCEDURE public.companiescount();
 /   DROP TRIGGER companiescount ON public.company;
       public       postgres    false    508    250            =           2620    53943    client lastclientupdate    TRIGGER     y   CREATE TRIGGER lastclientupdate BEFORE UPDATE ON public.client FOR EACH ROW EXECUTE PROCEDURE public.lastclientupdate();
 0   DROP TRIGGER lastclientupdate ON public.client;
       public       postgres    false    523    241            B           2620    53946 *   transaction nooftransactionpercompanycount    TRIGGER     ?   CREATE TRIGGER nooftransactionpercompanycount AFTER INSERT ON public.transaction FOR EACH ROW EXECUTE PROCEDURE public.nooftransactionpercompanycount();
 C   DROP TRIGGER nooftransactionpercompanycount ON public.transaction;
       public       postgres    false    525    443            D           2620    53945    users noofuserspercompanycount    TRIGGER     ?   CREATE TRIGGER noofuserspercompanycount AFTER INSERT ON public.users FOR EACH ROW EXECUTE PROCEDURE public.noofuserspercompanycount();
 7   DROP TRIGGER noofuserspercompanycount ON public.users;
       public       postgres    false    474    524            A           2620    53941 $   transaction transactionscreatedcount    TRIGGER     ?   CREATE TRIGGER transactionscreatedcount AFTER INSERT ON public.transaction FOR EACH ROW EXECUTE PROCEDURE public.transactionscreatedcount();
 =   DROP TRIGGER transactionscreatedcount ON public.transaction;
       public       postgres    false    443    522            C           2620    53949 #   transaction transactionsupdatecount    TRIGGER     ?   CREATE TRIGGER transactionsupdatecount BEFORE UPDATE ON public.transaction FOR EACH ROW EXECUTE PROCEDURE public.transactionsupdatecount();
 <   DROP TRIGGER transactionsupdatecount ON public.transaction;
       public       postgres    false    528    443            E           2620    53948    users userinsert    TRIGGER     k   CREATE TRIGGER userinsert AFTER INSERT ON public.users FOR EACH ROW EXECUTE PROCEDURE public.userinsert();
 )   DROP TRIGGER userinsert ON public.users;
       public       postgres    false    474    527            /           2606    45703 (   employee_compensation fk11e12a4b622c1275    FK CONSTRAINT     ?   ALTER TABLE ONLY public.employee_compensation
    ADD CONSTRAINT fk11e12a4b622c1275 FOREIGN KEY (company_id) REFERENCES public.company(id) DEFERRABLE INITIALLY DEFERRED;
 R   ALTER TABLE ONLY public.employee_compensation DROP CONSTRAINT fk11e12a4b622c1275;
       public       postgres    false    498    250    4309            0           2606    45708 (   employee_compensation fk11e12a4b75104d98    FK CONSTRAINT     ?   ALTER TABLE ONLY public.employee_compensation
    ADD CONSTRAINT fk11e12a4b75104d98 FOREIGN KEY (employee_cmp_id) REFERENCES public.employee(id) DEFERRABLE INITIALLY DEFERRED;
 R   ALTER TABLE ONLY public.employee_compensation DROP CONSTRAINT fk11e12a4b75104d98;
       public       postgres    false    280    498    4349            2           2606    45718 (   employee_compensation fk11e12a4b9e5a0e30    FK CONSTRAINT     ?   ALTER TABLE ONLY public.employee_compensation
    ADD CONSTRAINT fk11e12a4b9e5a0e30 FOREIGN KEY (last_modifier) REFERENCES public.users(id) DEFERRABLE INITIALLY DEFERRED;
 R   ALTER TABLE ONLY public.employee_compensation DROP CONSTRAINT fk11e12a4b9e5a0e30;
       public       postgres    false    498    4585    474            1           2606    45713 (   employee_compensation fk11e12a4bf1ae8cde    FK CONSTRAINT     ?   ALTER TABLE ONLY public.employee_compensation
    ADD CONSTRAINT fk11e12a4bf1ae8cde FOREIGN KEY (created_by) REFERENCES public.users(id) DEFERRABLE INITIALLY DEFERRED;
 R   ALTER TABLE ONLY public.employee_compensation DROP CONSTRAINT fk11e12a4bf1ae8cde;
       public       postgres    false    498    4585    474                       2606    27436    job fk11f9d622c1275    FK CONSTRAINT     ?   ALTER TABLE ONLY public.job
    ADD CONSTRAINT fk11f9d622c1275 FOREIGN KEY (company_id) REFERENCES public.company(id) DEFERRABLE INITIALLY DEFERRED;
 =   ALTER TABLE ONLY public.job DROP CONSTRAINT fk11f9d622c1275;
       public       postgres    false    4309    324    250                       2606    27446    job fk11f9d65fbb12a    FK CONSTRAINT     ?   ALTER TABLE ONLY public.job
    ADD CONSTRAINT fk11f9d65fbb12a FOREIGN KEY (parent_job_id) REFERENCES public.job(id) DEFERRABLE INITIALLY DEFERRED;
 =   ALTER TABLE ONLY public.job DROP CONSTRAINT fk11f9d65fbb12a;
       public       postgres    false    324    4403    324                       2606    27441    job fk11f9ddfe06a7f    FK CONSTRAINT     ?   ALTER TABLE ONLY public.job
    ADD CONSTRAINT fk11f9ddfe06a7f FOREIGN KEY (customer_id) REFERENCES public.customer(id) DEFERRABLE INITIALLY DEFERRED;
 =   ALTER TABLE ONLY public.job DROP CONSTRAINT fk11f9ddfe06a7f;
       public       postgres    false    324    4325    261            ?           2606    28281 "   tax_return_entry fk12080b763880555    FK CONSTRAINT     ?   ALTER TABLE ONLY public.tax_return_entry
    ADD CONSTRAINT fk12080b763880555 FOREIGN KEY (transaction_id) REFERENCES public.transaction(id) DEFERRABLE INITIALLY DEFERRED;
 L   ALTER TABLE ONLY public.tax_return_entry DROP CONSTRAINT fk12080b763880555;
       public       postgres    false    434    443    4553            ?           2606    28296 "   tax_return_entry fk12080b7bcd926f5    FK CONSTRAINT     ?   ALTER TABLE ONLY public.tax_return_entry
    ADD CONSTRAINT fk12080b7bcd926f5 FOREIGN KEY (taxagency_id) REFERENCES public.taxagency(id) DEFERRABLE INITIALLY DEFERRED;
 L   ALTER TABLE ONLY public.tax_return_entry DROP CONSTRAINT fk12080b7bcd926f5;
       public       postgres    false    434    419    4523            ?           2606    28291 "   tax_return_entry fk12080b7d7293ef5    FK CONSTRAINT     ?   ALTER TABLE ONLY public.tax_return_entry
    ADD CONSTRAINT fk12080b7d7293ef5 FOREIGN KEY (taxitem_id) REFERENCES public.tax_item(id) DEFERRABLE INITIALLY DEFERRED;
 L   ALTER TABLE ONLY public.tax_return_entry DROP CONSTRAINT fk12080b7d7293ef5;
       public       postgres    false    434    427    4535            ?           2606    28286 !   tax_return_entry fk12080b7f5cc2cc    FK CONSTRAINT     ?   ALTER TABLE ONLY public.tax_return_entry
    ADD CONSTRAINT fk12080b7f5cc2cc FOREIGN KEY (tax_return_id) REFERENCES public.tax_return(id) DEFERRABLE INITIALLY DEFERRED;
 K   ALTER TABLE ONLY public.tax_return_entry DROP CONSTRAINT fk12080b7f5cc2cc;
       public       postgres    false    434    432    4541                       2606    27511    measurement fk14466f9c622c1275    FK CONSTRAINT     ?   ALTER TABLE ONLY public.measurement
    ADD CONSTRAINT fk14466f9c622c1275 FOREIGN KEY (company_id) REFERENCES public.company(id) DEFERRABLE INITIALLY DEFERRED;
 H   ALTER TABLE ONLY public.measurement DROP CONSTRAINT fk14466f9c622c1275;
       public       postgres    false    250    4309    341            ?           2606    26831    customfield fk16be2809622c1275    FK CONSTRAINT     ?   ALTER TABLE ONLY public.customfield
    ADD CONSTRAINT fk16be2809622c1275 FOREIGN KEY (company_id) REFERENCES public.company(id) DEFERRABLE INITIALLY DEFERRED;
 H   ALTER TABLE ONLY public.customfield DROP CONSTRAINT fk16be2809622c1275;
       public       postgres    false    250    4309    268            6           2606    26346 !   branding_theme fk17493365622c1275    FK CONSTRAINT     ?   ALTER TABLE ONLY public.branding_theme
    ADD CONSTRAINT fk17493365622c1275 FOREIGN KEY (company_id) REFERENCES public.company(id) DEFERRABLE INITIALLY DEFERRED;
 K   ALTER TABLE ONLY public.branding_theme DROP CONSTRAINT fk17493365622c1275;
       public       postgres    false    4309    250    227            8           2606    26356 !   branding_theme fk174933659e5a0e30    FK CONSTRAINT     ?   ALTER TABLE ONLY public.branding_theme
    ADD CONSTRAINT fk174933659e5a0e30 FOREIGN KEY (last_modifier) REFERENCES public.users(id) DEFERRABLE INITIALLY DEFERRED;
 K   ALTER TABLE ONLY public.branding_theme DROP CONSTRAINT fk174933659e5a0e30;
       public       postgres    false    474    4585    227            7           2606    26351 !   branding_theme fk17493365f1ae8cde    FK CONSTRAINT     ?   ALTER TABLE ONLY public.branding_theme
    ADD CONSTRAINT fk17493365f1ae8cde FOREIGN KEY (created_by) REFERENCES public.users(id) DEFERRABLE INITIALLY DEFERRED;
 K   ALTER TABLE ONLY public.branding_theme DROP CONSTRAINT fk17493365f1ae8cde;
       public       postgres    false    227    474    4585            u           2606    27941 (   recurring_transaction fk1923903c622c1275    FK CONSTRAINT     ?   ALTER TABLE ONLY public.recurring_transaction
    ADD CONSTRAINT fk1923903c622c1275 FOREIGN KEY (company_id) REFERENCES public.company(id) DEFERRABLE INITIALLY DEFERRED;
 R   ALTER TABLE ONLY public.recurring_transaction DROP CONSTRAINT fk1923903c622c1275;
       public       postgres    false    394    250    4309            t           2606    27936 (   recurring_transaction fk1923903c63880555    FK CONSTRAINT     ?   ALTER TABLE ONLY public.recurring_transaction
    ADD CONSTRAINT fk1923903c63880555 FOREIGN KEY (transaction_id) REFERENCES public.transaction(id) DEFERRABLE INITIALLY DEFERRED;
 R   ALTER TABLE ONLY public.recurring_transaction DROP CONSTRAINT fk1923903c63880555;
       public       postgres    false    394    443    4553            w           2606    27951 (   recurring_transaction fk1923903c9e5a0e30    FK CONSTRAINT     ?   ALTER TABLE ONLY public.recurring_transaction
    ADD CONSTRAINT fk1923903c9e5a0e30 FOREIGN KEY (last_modifier) REFERENCES public.users(id) DEFERRABLE INITIALLY DEFERRED;
 R   ALTER TABLE ONLY public.recurring_transaction DROP CONSTRAINT fk1923903c9e5a0e30;
       public       postgres    false    394    474    4585            v           2606    27946 (   recurring_transaction fk1923903cf1ae8cde    FK CONSTRAINT     ?   ALTER TABLE ONLY public.recurring_transaction
    ADD CONSTRAINT fk1923903cf1ae8cde FOREIGN KEY (created_by) REFERENCES public.users(id) DEFERRABLE INITIALLY DEFERRED;
 R   ALTER TABLE ONLY public.recurring_transaction DROP CONSTRAINT fk1923903cf1ae8cde;
       public       postgres    false    394    474    4585            5           2606    26341    bank_account fk1979bf0a15dee523    FK CONSTRAINT     ?   ALTER TABLE ONLY public.bank_account
    ADD CONSTRAINT fk1979bf0a15dee523 FOREIGN KEY (id) REFERENCES public.account(id) DEFERRABLE INITIALLY DEFERRED;
 I   ALTER TABLE ONLY public.bank_account DROP CONSTRAINT fk1979bf0a15dee523;
       public       postgres    false    225    197    4243            4           2606    26336    bank_account fk1979bf0a8beaf03f    FK CONSTRAINT     ?   ALTER TABLE ONLY public.bank_account
    ADD CONSTRAINT fk1979bf0a8beaf03f FOREIGN KEY (bank_id) REFERENCES public.bank(id) DEFERRABLE INITIALLY DEFERRED;
 I   ALTER TABLE ONLY public.bank_account DROP CONSTRAINT fk1979bf0a8beaf03f;
       public       postgres    false    224    225    4273            ?           2606    27021    features fk1d52d43d590d0726    FK CONSTRAINT     ?   ALTER TABLE ONLY public.features
    ADD CONSTRAINT fk1d52d43d590d0726 FOREIGN KEY (feature_id) REFERENCES public.subscription(id) DEFERRABLE INITIALLY DEFERRED;
 E   ALTER TABLE ONLY public.features DROP CONSTRAINT fk1d52d43d590d0726;
       public       postgres    false    417    292    4519            t           2606    26656 $   computation_slabs fk1db48ec19b5089b6    FK CONSTRAINT     ?   ALTER TABLE ONLY public.computation_slabs
    ADD CONSTRAINT fk1db48ec19b5089b6 FOREIGN KEY (pay_head_id) REFERENCES public.computation_pay_head(id) DEFERRABLE INITIALLY DEFERRED;
 N   ALTER TABLE ONLY public.computation_slabs DROP CONSTRAINT fk1db48ec19b5089b6;
       public       postgres    false    252    4313    253            3           2606    26331    bank fk1efe3c622c1275    FK CONSTRAINT     ?   ALTER TABLE ONLY public.bank
    ADD CONSTRAINT fk1efe3c622c1275 FOREIGN KEY (company_id) REFERENCES public.company(id) DEFERRABLE INITIALLY DEFERRED;
 ?   ALTER TABLE ONLY public.bank DROP CONSTRAINT fk1efe3c622c1275;
       public       postgres    false    224    250    4309            ?           2606    28131    taxrates fk1f2492c8622c1275    FK CONSTRAINT     ?   ALTER TABLE ONLY public.taxrates
    ADD CONSTRAINT fk1f2492c8622c1275 FOREIGN KEY (company_id) REFERENCES public.company(id) DEFERRABLE INITIALLY DEFERRED;
 E   ALTER TABLE ONLY public.taxrates DROP CONSTRAINT fk1f2492c8622c1275;
       public       postgres    false    421    250    4309            ?           2606    28166    tax_code fk1fdc9a212d1a5f9a    FK CONSTRAINT     ?   ALTER TABLE ONLY public.tax_code
    ADD CONSTRAINT fk1fdc9a212d1a5f9a FOREIGN KEY (taxitemgroup_purchases) REFERENCES public.tax_item_groups(id) DEFERRABLE INITIALLY DEFERRED;
 E   ALTER TABLE ONLY public.tax_code DROP CONSTRAINT fk1fdc9a212d1a5f9a;
       public       postgres    false    424    429    4537            ?           2606    28161    tax_code fk1fdc9a21622c1275    FK CONSTRAINT     ?   ALTER TABLE ONLY public.tax_code
    ADD CONSTRAINT fk1fdc9a21622c1275 FOREIGN KEY (company_id) REFERENCES public.company(id) DEFERRABLE INITIALLY DEFERRED;
 E   ALTER TABLE ONLY public.tax_code DROP CONSTRAINT fk1fdc9a21622c1275;
       public       postgres    false    424    250    4309            ?           2606    28171    tax_code fk1fdc9a21bc51fe14    FK CONSTRAINT     ?   ALTER TABLE ONLY public.tax_code
    ADD CONSTRAINT fk1fdc9a21bc51fe14 FOREIGN KEY (taxitemgroup_sales) REFERENCES public.tax_item_groups(id) DEFERRABLE INITIALLY DEFERRED;
 E   ALTER TABLE ONLY public.tax_code DROP CONSTRAINT fk1fdc9a21bc51fe14;
       public       postgres    false    424    429    4537            ?           2606    28196    tax_item fk1fdf6747622c1275    FK CONSTRAINT     ?   ALTER TABLE ONLY public.tax_item
    ADD CONSTRAINT fk1fdf6747622c1275 FOREIGN KEY (company_id) REFERENCES public.company(id) DEFERRABLE INITIALLY DEFERRED;
 E   ALTER TABLE ONLY public.tax_item DROP CONSTRAINT fk1fdf6747622c1275;
       public       postgres    false    427    250    4309            ?           2606    28211    tax_item fk1fdf6747880103e1    FK CONSTRAINT     ?   ALTER TABLE ONLY public.tax_item
    ADD CONSTRAINT fk1fdf6747880103e1 FOREIGN KEY (id) REFERENCES public.tax_item_groups(id) DEFERRABLE INITIALLY DEFERRED;
 E   ALTER TABLE ONLY public.tax_item DROP CONSTRAINT fk1fdf6747880103e1;
       public       postgres    false    427    429    4537            ?           2606    28206    tax_item fk1fdf6747971bcdc4    FK CONSTRAINT     ?   ALTER TABLE ONLY public.tax_item
    ADD CONSTRAINT fk1fdf6747971bcdc4 FOREIGN KEY (tax_agency) REFERENCES public.taxagency(id) DEFERRABLE INITIALLY DEFERRED;
 E   ALTER TABLE ONLY public.tax_item DROP CONSTRAINT fk1fdf6747971bcdc4;
       public       postgres    false    427    419    4523            ?           2606    28201    tax_item fk1fdf674798eaf569    FK CONSTRAINT     ?   ALTER TABLE ONLY public.tax_item
    ADD CONSTRAINT fk1fdf674798eaf569 FOREIGN KEY (vat_return_box) REFERENCES public.vatreturnbox(id) DEFERRABLE INITIALLY DEFERRED;
 E   ALTER TABLE ONLY public.tax_item DROP CONSTRAINT fk1fdf674798eaf569;
       public       postgres    false    427    479    4593                       2606    27496     local_message fk21cf9ef33bf6b1d7    FK CONSTRAINT     ?   ALTER TABLE ONLY public.local_message
    ADD CONSTRAINT fk21cf9ef33bf6b1d7 FOREIGN KEY (language) REFERENCES public.language(code) DEFERRABLE INITIALLY DEFERRED;
 J   ALTER TABLE ONLY public.local_message DROP CONSTRAINT fk21cf9ef33bf6b1d7;
       public       postgres    false    4411    335    329                       2606    27486     local_message fk21cf9ef358dbf25b    FK CONSTRAINT     ?   ALTER TABLE ONLY public.local_message
    ADD CONSTRAINT fk21cf9ef358dbf25b FOREIGN KEY (client) REFERENCES public.client(id) DEFERRABLE INITIALLY DEFERRED;
 J   ALTER TABLE ONLY public.local_message DROP CONSTRAINT fk21cf9ef358dbf25b;
       public       postgres    false    4299    241    335                       2606    27491     local_message fk21cf9ef381719453    FK CONSTRAINT     ?   ALTER TABLE ONLY public.local_message
    ADD CONSTRAINT fk21cf9ef381719453 FOREIGN KEY (message_id) REFERENCES public.message(id) DEFERRABLE INITIALLY DEFERRED;
 J   ALTER TABLE ONLY public.local_message DROP CONSTRAINT fk21cf9ef381719453;
       public       postgres    false    335    344    4427            ?           2606    27331    item fk2273131b6c23e8    FK CONSTRAINT     ?   ALTER TABLE ONLY public.item
    ADD CONSTRAINT fk2273131b6c23e8 FOREIGN KEY (parent_id) REFERENCES public.item(id) DEFERRABLE INITIALLY DEFERRED;
 ?   ALTER TABLE ONLY public.item DROP CONSTRAINT fk2273131b6c23e8;
       public       postgres    false    315    4393    315            ?           2606    27286    item fk2273134545f135    FK CONSTRAINT     ?   ALTER TABLE ONLY public.item
    ADD CONSTRAINT fk2273134545f135 FOREIGN KEY (itemgroup_id) REFERENCES public.itemgroup(id) DEFERRABLE INITIALLY DEFERRED;
 ?   ALTER TABLE ONLY public.item DROP CONSTRAINT fk2273134545f135;
       public       postgres    false    315    317    4395            ?           2606    27276    item fk2273134e7fe0da    FK CONSTRAINT     ?   ALTER TABLE ONLY public.item
    ADD CONSTRAINT fk2273134e7fe0da FOREIGN KEY (min_unit) REFERENCES public.unit(id) DEFERRABLE INITIALLY DEFERRED;
 ?   ALTER TABLE ONLY public.item DROP CONSTRAINT fk2273134e7fe0da;
       public       postgres    false    315    470    4581            ?           2606    27271    item fk227313622c1275    FK CONSTRAINT     ?   ALTER TABLE ONLY public.item
    ADD CONSTRAINT fk227313622c1275 FOREIGN KEY (company_id) REFERENCES public.company(id) DEFERRABLE INITIALLY DEFERRED;
 ?   ALTER TABLE ONLY public.item DROP CONSTRAINT fk227313622c1275;
       public       postgres    false    4309    250    315            ?           2606    27336    item fk2273136f1934cb    FK CONSTRAINT     ?   ALTER TABLE ONLY public.item
    ADD CONSTRAINT fk2273136f1934cb FOREIGN KEY (income_account_id) REFERENCES public.account(id) DEFERRABLE INITIALLY DEFERRED;
 ?   ALTER TABLE ONLY public.item DROP CONSTRAINT fk2273136f1934cb;
       public       postgres    false    315    197    4243            ?           2606    27296    item fk227313984db8a1    FK CONSTRAINT     ?   ALTER TABLE ONLY public.item
    ADD CONSTRAINT fk227313984db8a1 FOREIGN KEY (warehouse) REFERENCES public.warehouse(id) DEFERRABLE INITIALLY DEFERRED;
 ?   ALTER TABLE ONLY public.item DROP CONSTRAINT fk227313984db8a1;
       public       postgres    false    4605    488    315            ?           2606    27316    item fk2273139e5a0e30    FK CONSTRAINT     ?   ALTER TABLE ONLY public.item
    ADD CONSTRAINT fk2273139e5a0e30 FOREIGN KEY (last_modifier) REFERENCES public.users(id) DEFERRABLE INITIALLY DEFERRED;
 ?   ALTER TABLE ONLY public.item DROP CONSTRAINT fk2273139e5a0e30;
       public       postgres    false    474    4585    315            ?           2606    27321    item fk227313b7b51814    FK CONSTRAINT     ?   ALTER TABLE ONLY public.item
    ADD CONSTRAINT fk227313b7b51814 FOREIGN KEY (tax_code) REFERENCES public.tax_code(id) DEFERRABLE INITIALLY DEFERRED;
 ?   ALTER TABLE ONLY public.item DROP CONSTRAINT fk227313b7b51814;
       public       postgres    false    315    4529    424            ?           2606    27291    item fk227313b85e7d08    FK CONSTRAINT     ?   ALTER TABLE ONLY public.item
    ADD CONSTRAINT fk227313b85e7d08 FOREIGN KEY (max_unit) REFERENCES public.unit(id) DEFERRABLE INITIALLY DEFERRED;
 ?   ALTER TABLE ONLY public.item DROP CONSTRAINT fk227313b85e7d08;
       public       postgres    false    315    470    4581            ?           2606    27311    item fk227313bd4af465    FK CONSTRAINT     ?   ALTER TABLE ONLY public.item
    ADD CONSTRAINT fk227313bd4af465 FOREIGN KEY (preffered_vendor) REFERENCES public.vendor(id) DEFERRABLE INITIALLY DEFERRED;
 ?   ALTER TABLE ONLY public.item DROP CONSTRAINT fk227313bd4af465;
       public       postgres    false    4595    480    315            ?           2606    27266    item fk227313be679a66    FK CONSTRAINT     ?   ALTER TABLE ONLY public.item
    ADD CONSTRAINT fk227313be679a66 FOREIGN KEY (on_hand_qty_unit) REFERENCES public.unit(id) DEFERRABLE INITIALLY DEFERRED;
 ?   ALTER TABLE ONLY public.item DROP CONSTRAINT fk227313be679a66;
       public       postgres    false    315    470    4581            ?           2606    27306    item fk227313cf909fd3    FK CONSTRAINT     ?   ALTER TABLE ONLY public.item
    ADD CONSTRAINT fk227313cf909fd3 FOREIGN KEY (measurement) REFERENCES public.measurement(id) DEFERRABLE INITIALLY DEFERRED;
 ?   ALTER TABLE ONLY public.item DROP CONSTRAINT fk227313cf909fd3;
       public       postgres    false    4425    315    341            ?           2606    27281    item fk227313d23efa79    FK CONSTRAINT     ?   ALTER TABLE ONLY public.item
    ADD CONSTRAINT fk227313d23efa79 FOREIGN KEY (re_order_point_unit) REFERENCES public.unit(id) DEFERRABLE INITIALLY DEFERRED;
 ?   ALTER TABLE ONLY public.item DROP CONSTRAINT fk227313d23efa79;
       public       postgres    false    315    470    4581            ?           2606    27326    item fk227313dfc96b1    FK CONSTRAINT     ?   ALTER TABLE ONLY public.item
    ADD CONSTRAINT fk227313dfc96b1 FOREIGN KEY (assets_account_id) REFERENCES public.account(id) DEFERRABLE INITIALLY DEFERRED;
 >   ALTER TABLE ONLY public.item DROP CONSTRAINT fk227313dfc96b1;
       public       postgres    false    197    315    4243            ?           2606    27341    item fk227313e2aa5abc    FK CONSTRAINT     ?   ALTER TABLE ONLY public.item
    ADD CONSTRAINT fk227313e2aa5abc FOREIGN KEY (expense_account_id) REFERENCES public.account(id) DEFERRABLE INITIALLY DEFERRED;
 ?   ALTER TABLE ONLY public.item DROP CONSTRAINT fk227313e2aa5abc;
       public       postgres    false    315    197    4243            ?           2606    27301    item fk227313f1ae8cde    FK CONSTRAINT     ?   ALTER TABLE ONLY public.item
    ADD CONSTRAINT fk227313f1ae8cde FOREIGN KEY (created_by) REFERENCES public.users(id) DEFERRABLE INITIALLY DEFERRED;
 ?   ALTER TABLE ONLY public.item DROP CONSTRAINT fk227313f1ae8cde;
       public       postgres    false    474    315    4585            3           2606    45723    employee_tax fk232e427a622c1275    FK CONSTRAINT     ?   ALTER TABLE ONLY public.employee_tax
    ADD CONSTRAINT fk232e427a622c1275 FOREIGN KEY (company_id) REFERENCES public.company(id) DEFERRABLE INITIALLY DEFERRED;
 I   ALTER TABLE ONLY public.employee_tax DROP CONSTRAINT fk232e427a622c1275;
       public       postgres    false    500    4309    250            4           2606    45728    employee_tax fk232e427a916d3913    FK CONSTRAINT     ?   ALTER TABLE ONLY public.employee_tax
    ADD CONSTRAINT fk232e427a916d3913 FOREIGN KEY (employee_tax_id) REFERENCES public.employee(id) DEFERRABLE INITIALLY DEFERRED;
 I   ALTER TABLE ONLY public.employee_tax DROP CONSTRAINT fk232e427a916d3913;
       public       postgres    false    500    4349    280            7           2606    45788    employee_tax fk232e427a971bcdc4    FK CONSTRAINT     ?   ALTER TABLE ONLY public.employee_tax
    ADD CONSTRAINT fk232e427a971bcdc4 FOREIGN KEY (tax_agency) REFERENCES public.taxagency(id) DEFERRABLE INITIALLY DEFERRED;
 I   ALTER TABLE ONLY public.employee_tax DROP CONSTRAINT fk232e427a971bcdc4;
       public       postgres    false    419    4523    500            6           2606    45738    employee_tax fk232e427a9e5a0e30    FK CONSTRAINT     ?   ALTER TABLE ONLY public.employee_tax
    ADD CONSTRAINT fk232e427a9e5a0e30 FOREIGN KEY (last_modifier) REFERENCES public.users(id) DEFERRABLE INITIALLY DEFERRED;
 I   ALTER TABLE ONLY public.employee_tax DROP CONSTRAINT fk232e427a9e5a0e30;
       public       postgres    false    500    4585    474            5           2606    45733    employee_tax fk232e427af1ae8cde    FK CONSTRAINT     ?   ALTER TABLE ONLY public.employee_tax
    ADD CONSTRAINT fk232e427af1ae8cde FOREIGN KEY (created_by) REFERENCES public.users(id) DEFERRABLE INITIALLY DEFERRED;
 I   ALTER TABLE ONLY public.employee_tax DROP CONSTRAINT fk232e427af1ae8cde;
       public       postgres    false    500    4585    474            ?           2606    28156 !   tax_adjustment fk24bbd6a1274bc854    FK CONSTRAINT     ?   ALTER TABLE ONLY public.tax_adjustment
    ADD CONSTRAINT fk24bbd6a1274bc854 FOREIGN KEY (id) REFERENCES public.transaction(id) DEFERRABLE INITIALLY DEFERRED;
 K   ALTER TABLE ONLY public.tax_adjustment DROP CONSTRAINT fk24bbd6a1274bc854;
       public       postgres    false    422    443    4553            ?           2606    28146 !   tax_adjustment fk24bbd6a1622c1275    FK CONSTRAINT     ?   ALTER TABLE ONLY public.tax_adjustment
    ADD CONSTRAINT fk24bbd6a1622c1275 FOREIGN KEY (company_id) REFERENCES public.company(id) DEFERRABLE INITIALLY DEFERRED;
 K   ALTER TABLE ONLY public.tax_adjustment DROP CONSTRAINT fk24bbd6a1622c1275;
       public       postgres    false    422    250    4309            ?           2606    28151 !   tax_adjustment fk24bbd6a187003963    FK CONSTRAINT     ?   ALTER TABLE ONLY public.tax_adjustment
    ADD CONSTRAINT fk24bbd6a187003963 FOREIGN KEY (adjustment_account) REFERENCES public.account(id) DEFERRABLE INITIALLY DEFERRED;
 K   ALTER TABLE ONLY public.tax_adjustment DROP CONSTRAINT fk24bbd6a187003963;
       public       postgres    false    422    197    4243            ?           2606    28136 !   tax_adjustment fk24bbd6a1b7bab260    FK CONSTRAINT     ?   ALTER TABLE ONLY public.tax_adjustment
    ADD CONSTRAINT fk24bbd6a1b7bab260 FOREIGN KEY (tax_item) REFERENCES public.tax_item(id) DEFERRABLE INITIALLY DEFERRED;
 K   ALTER TABLE ONLY public.tax_adjustment DROP CONSTRAINT fk24bbd6a1b7bab260;
       public       postgres    false    422    427    4535            ?           2606    28141 !   tax_adjustment fk24bbd6a1c368d6ac    FK CONSTRAINT     ?   ALTER TABLE ONLY public.tax_adjustment
    ADD CONSTRAINT fk24bbd6a1c368d6ac FOREIGN KEY (tax_agency_id) REFERENCES public.taxagency(id) DEFERRABLE INITIALLY DEFERRED;
 K   ALTER TABLE ONLY public.tax_adjustment DROP CONSTRAINT fk24bbd6a1c368d6ac;
       public       postgres    false    422    419    4523            d           2606    27856 !   purchase_order fk26456270274bc854    FK CONSTRAINT     ?   ALTER TABLE ONLY public.purchase_order
    ADD CONSTRAINT fk26456270274bc854 FOREIGN KEY (id) REFERENCES public.transaction(id) DEFERRABLE INITIALLY DEFERRED;
 K   ALTER TABLE ONLY public.purchase_order DROP CONSTRAINT fk26456270274bc854;
       public       postgres    false    386    443    4553            h           2606    27876 !   purchase_order fk26456270338bd6bb    FK CONSTRAINT     ?   ALTER TABLE ONLY public.purchase_order
    ADD CONSTRAINT fk26456270338bd6bb FOREIGN KEY (payment_term_id) REFERENCES public.paymentterms(id) DEFERRABLE INITIALLY DEFERRED;
 K   ALTER TABLE ONLY public.purchase_order DROP CONSTRAINT fk26456270338bd6bb;
       public       postgres    false    386    362    4451            e           2606    27861 !   purchase_order fk264562703ea5facf    FK CONSTRAINT     ?   ALTER TABLE ONLY public.purchase_order
    ADD CONSTRAINT fk264562703ea5facf FOREIGN KEY (used_cashpurchase) REFERENCES public.cash_purchase(id) DEFERRABLE INITIALLY DEFERRED;
 K   ALTER TABLE ONLY public.purchase_order DROP CONSTRAINT fk264562703ea5facf;
       public       postgres    false    386    233    4285            c           2606    27851 !   purchase_order fk264562704f937563    FK CONSTRAINT     ?   ALTER TABLE ONLY public.purchase_order
    ADD CONSTRAINT fk264562704f937563 FOREIGN KEY (used_bill) REFERENCES public.enter_bill(id) DEFERRABLE INITIALLY DEFERRED;
 K   ALTER TABLE ONLY public.purchase_order DROP CONSTRAINT fk264562704f937563;
       public       postgres    false    386    288    4359            g           2606    27871 !   purchase_order fk26456270777205a4    FK CONSTRAINT     ?   ALTER TABLE ONLY public.purchase_order
    ADD CONSTRAINT fk26456270777205a4 FOREIGN KEY (ship_to_id) REFERENCES public.account(id) DEFERRABLE INITIALLY DEFERRED;
 K   ALTER TABLE ONLY public.purchase_order DROP CONSTRAINT fk26456270777205a4;
       public       postgres    false    386    197    4243            b           2606    27846 !   purchase_order fk26456270891a177f    FK CONSTRAINT     ?   ALTER TABLE ONLY public.purchase_order
    ADD CONSTRAINT fk26456270891a177f FOREIGN KEY (vendor_id) REFERENCES public.vendor(id) DEFERRABLE INITIALLY DEFERRED;
 K   ALTER TABLE ONLY public.purchase_order DROP CONSTRAINT fk26456270891a177f;
       public       postgres    false    386    480    4595            f           2606    27866 !   purchase_order fk2645627097edd458    FK CONSTRAINT     ?   ALTER TABLE ONLY public.purchase_order
    ADD CONSTRAINT fk2645627097edd458 FOREIGN KEY (shipping_terms_id) REFERENCES public.shippingterms(id) DEFERRABLE INITIALLY DEFERRED;
 K   ALTER TABLE ONLY public.purchase_order DROP CONSTRAINT fk2645627097edd458;
       public       postgres    false    386    408    4509            a           2606    27841 !   purchase_order fk26456270ad0a95dc    FK CONSTRAINT     ?   ALTER TABLE ONLY public.purchase_order
    ADD CONSTRAINT fk26456270ad0a95dc FOREIGN KEY (shipping_method_id) REFERENCES public.shippingmethod(id) DEFERRABLE INITIALLY DEFERRED;
 K   ALTER TABLE ONLY public.purchase_order DROP CONSTRAINT fk26456270ad0a95dc;
       public       postgres    false    386    406    4507            ?           2606    27346    itemgroup fk26e87f2c622c1275    FK CONSTRAINT     ?   ALTER TABLE ONLY public.itemgroup
    ADD CONSTRAINT fk26e87f2c622c1275 FOREIGN KEY (company_id) REFERENCES public.company(id) DEFERRABLE INITIALLY DEFERRED;
 F   ALTER TABLE ONLY public.itemgroup DROP CONSTRAINT fk26e87f2c622c1275;
       public       postgres    false    250    4309    317                        2606    27356    itemgroup fk26e87f2c9e5a0e30    FK CONSTRAINT     ?   ALTER TABLE ONLY public.itemgroup
    ADD CONSTRAINT fk26e87f2c9e5a0e30 FOREIGN KEY (last_modifier) REFERENCES public.users(id) DEFERRABLE INITIALLY DEFERRED;
 F   ALTER TABLE ONLY public.itemgroup DROP CONSTRAINT fk26e87f2c9e5a0e30;
       public       postgres    false    317    4585    474            ?           2606    27351    itemgroup fk26e87f2cf1ae8cde    FK CONSTRAINT     ?   ALTER TABLE ONLY public.itemgroup
    ADD CONSTRAINT fk26e87f2cf1ae8cde FOREIGN KEY (created_by) REFERENCES public.users(id) DEFERRABLE INITIALLY DEFERRED;
 F   ALTER TABLE ONLY public.itemgroup DROP CONSTRAINT fk26e87f2cf1ae8cde;
       public       postgres    false    4585    317    474                       2606    28676    unit fk27d1842e4ff095    FK CONSTRAINT     ?   ALTER TABLE ONLY public.unit
    ADD CONSTRAINT fk27d1842e4ff095 FOREIGN KEY (measurement_id) REFERENCES public.measurement(id) DEFERRABLE INITIALLY DEFERRED;
 ?   ALTER TABLE ONLY public.unit DROP CONSTRAINT fk27d1842e4ff095;
       public       postgres    false    341    470    4425                       2606    28671    unit fk27d184622c1275    FK CONSTRAINT     ?   ALTER TABLE ONLY public.unit
    ADD CONSTRAINT fk27d184622c1275 FOREIGN KEY (company_id) REFERENCES public.company(id) DEFERRABLE INITIALLY DEFERRED;
 ?   ALTER TABLE ONLY public.unit DROP CONSTRAINT fk27d184622c1275;
       public       postgres    false    250    470    4309                       2606    26221 &   account_transaction fk28362f8c622c1275    FK CONSTRAINT     ?   ALTER TABLE ONLY public.account_transaction
    ADD CONSTRAINT fk28362f8c622c1275 FOREIGN KEY (company_id) REFERENCES public.company(id) DEFERRABLE INITIALLY DEFERRED;
 P   ALTER TABLE ONLY public.account_transaction DROP CONSTRAINT fk28362f8c622c1275;
       public       postgres    false    4309    250    202                       2606    26211 &   account_transaction fk28362f8c63880555    FK CONSTRAINT     ?   ALTER TABLE ONLY public.account_transaction
    ADD CONSTRAINT fk28362f8c63880555 FOREIGN KEY (transaction_id) REFERENCES public.transaction(id) DEFERRABLE INITIALLY DEFERRED;
 P   ALTER TABLE ONLY public.account_transaction DROP CONSTRAINT fk28362f8c63880555;
       public       postgres    false    202    4553    443                       2606    26231 &   account_transaction fk28362f8c9e5a0e30    FK CONSTRAINT     ?   ALTER TABLE ONLY public.account_transaction
    ADD CONSTRAINT fk28362f8c9e5a0e30 FOREIGN KEY (last_modifier) REFERENCES public.users(id) DEFERRABLE INITIALLY DEFERRED;
 P   ALTER TABLE ONLY public.account_transaction DROP CONSTRAINT fk28362f8c9e5a0e30;
       public       postgres    false    202    4585    474                       2606    26216 &   account_transaction fk28362f8ce5fcf475    FK CONSTRAINT     ?   ALTER TABLE ONLY public.account_transaction
    ADD CONSTRAINT fk28362f8ce5fcf475 FOREIGN KEY (account_id) REFERENCES public.account(id) DEFERRABLE INITIALLY DEFERRED;
 P   ALTER TABLE ONLY public.account_transaction DROP CONSTRAINT fk28362f8ce5fcf475;
       public       postgres    false    4243    197    202                       2606    26226 &   account_transaction fk28362f8cf1ae8cde    FK CONSTRAINT     ?   ALTER TABLE ONLY public.account_transaction
    ADD CONSTRAINT fk28362f8cf1ae8cde FOREIGN KEY (created_by) REFERENCES public.users(id) DEFERRABLE INITIALLY DEFERRED;
 P   ALTER TABLE ONLY public.account_transaction DROP CONSTRAINT fk28362f8cf1ae8cde;
       public       postgres    false    474    202    4585                        2606    28801    vote fk284aea384ae49f    FK CONSTRAINT     ?   ALTER TABLE ONLY public.vote
    ADD CONSTRAINT fk284aea384ae49f FOREIGN KEY (client_id) REFERENCES public.client(id) DEFERRABLE INITIALLY DEFERRED;
 ?   ALTER TABLE ONLY public.vote DROP CONSTRAINT fk284aea384ae49f;
       public       postgres    false    241    486    4299                       2606    28796    vote fk284aeaeb6d0f6a    FK CONSTRAINT     ?   ALTER TABLE ONLY public.vote
    ADD CONSTRAINT fk284aeaeb6d0f6a FOREIGN KEY (local_message_id) REFERENCES public.local_message(id) DEFERRABLE INITIALLY DEFERRED;
 ?   ALTER TABLE ONLY public.vote DROP CONSTRAINT fk284aeaeb6d0f6a;
       public       postgres    false    4417    486    335            ?           2606    28031    statement fk29cf394f622c1275    FK CONSTRAINT     ?   ALTER TABLE ONLY public.statement
    ADD CONSTRAINT fk29cf394f622c1275 FOREIGN KEY (company_id) REFERENCES public.company(id) DEFERRABLE INITIALLY DEFERRED;
 F   ALTER TABLE ONLY public.statement DROP CONSTRAINT fk29cf394f622c1275;
       public       postgres    false    410    250    4309            ?           2606    28036    statement fk29cf394fe274d89f    FK CONSTRAINT     ?   ALTER TABLE ONLY public.statement
    ADD CONSTRAINT fk29cf394fe274d89f FOREIGN KEY (reconciliation_id) REFERENCES public.reconciliation(id) DEFERRABLE INITIALLY DEFERRED;
 F   ALTER TABLE ONLY public.statement DROP CONSTRAINT fk29cf394fe274d89f;
       public       postgres    false    410    390    4489            ?           2606    28026    statement fk29cf394fe5fcf475    FK CONSTRAINT     ?   ALTER TABLE ONLY public.statement
    ADD CONSTRAINT fk29cf394fe5fcf475 FOREIGN KEY (account_id) REFERENCES public.account(id) DEFERRABLE INITIALLY DEFERRED;
 F   ALTER TABLE ONLY public.statement DROP CONSTRAINT fk29cf394fe5fcf475;
       public       postgres    false    410    197    4243            ?           2606    26961 $   enter_bill_orders fk29d90b1620610806    FK CONSTRAINT     ?   ALTER TABLE ONLY public.enter_bill_orders
    ADD CONSTRAINT fk29d90b1620610806 FOREIGN KEY (enter_bill_id) REFERENCES public.enter_bill(id) DEFERRABLE INITIALLY DEFERRED;
 N   ALTER TABLE ONLY public.enter_bill_orders DROP CONSTRAINT fk29d90b1620610806;
       public       postgres    false    289    4359    288            ?           2606    26966 $   enter_bill_orders fk29d90b1639750592    FK CONSTRAINT     ?   ALTER TABLE ONLY public.enter_bill_orders
    ADD CONSTRAINT fk29d90b1639750592 FOREIGN KEY (purchase_order_id) REFERENCES public.purchase_order(id) DEFERRABLE INITIALLY DEFERRED;
 N   ALTER TABLE ONLY public.enter_bill_orders DROP CONSTRAINT fk29d90b1639750592;
       public       postgres    false    4483    289    386            ?           2606    27211 '   inventory_remap_task fk2c6298be1e282cdf    FK CONSTRAINT     ?   ALTER TABLE ONLY public.inventory_remap_task
    ADD CONSTRAINT fk2c6298be1e282cdf FOREIGN KEY (item_id) REFERENCES public.item(id) DEFERRABLE INITIALLY DEFERRED;
 Q   ALTER TABLE ONLY public.inventory_remap_task DROP CONSTRAINT fk2c6298be1e282cdf;
       public       postgres    false    315    311    4393            ?           2606    27206 '   inventory_remap_task fk2c6298be622c1275    FK CONSTRAINT     ?   ALTER TABLE ONLY public.inventory_remap_task
    ADD CONSTRAINT fk2c6298be622c1275 FOREIGN KEY (company_id) REFERENCES public.company(id) DEFERRABLE INITIALLY DEFERRED;
 Q   ALTER TABLE ONLY public.inventory_remap_task DROP CONSTRAINT fk2c6298be622c1275;
       public       postgres    false    250    4309    311            ?           2606    27216 '   inventory_remap_task fk2c6298be9745d1df    FK CONSTRAINT     ?   ALTER TABLE ONLY public.inventory_remap_task
    ADD CONSTRAINT fk2c6298be9745d1df FOREIGN KEY (user_id) REFERENCES public.users(id) DEFERRABLE INITIALLY DEFERRED;
 Q   ALTER TABLE ONLY public.inventory_remap_task DROP CONSTRAINT fk2c6298be9745d1df;
       public       postgres    false    311    4585    474            k           2606    27891 "   receive_payment fk2d07f06a274bc854    FK CONSTRAINT     ?   ALTER TABLE ONLY public.receive_payment
    ADD CONSTRAINT fk2d07f06a274bc854 FOREIGN KEY (id) REFERENCES public.transaction(id) DEFERRABLE INITIALLY DEFERRED;
 L   ALTER TABLE ONLY public.receive_payment DROP CONSTRAINT fk2d07f06a274bc854;
       public       postgres    false    387    443    4553            i           2606    27881 "   receive_payment fk2d07f06aac60fbcf    FK CONSTRAINT     ?   ALTER TABLE ONLY public.receive_payment
    ADD CONSTRAINT fk2d07f06aac60fbcf FOREIGN KEY (credits_and_payments_id) REFERENCES public.credits_and_payments(id) DEFERRABLE INITIALLY DEFERRED;
 L   ALTER TABLE ONLY public.receive_payment DROP CONSTRAINT fk2d07f06aac60fbcf;
       public       postgres    false    387    257    4319            l           2606    27896 "   receive_payment fk2d07f06adfe06a7f    FK CONSTRAINT     ?   ALTER TABLE ONLY public.receive_payment
    ADD CONSTRAINT fk2d07f06adfe06a7f FOREIGN KEY (customer_id) REFERENCES public.customer(id) DEFERRABLE INITIALLY DEFERRED;
 L   ALTER TABLE ONLY public.receive_payment DROP CONSTRAINT fk2d07f06adfe06a7f;
       public       postgres    false    387    261    4325            j           2606    27886 "   receive_payment fk2d07f06ae5fcf475    FK CONSTRAINT     ?   ALTER TABLE ONLY public.receive_payment
    ADD CONSTRAINT fk2d07f06ae5fcf475 FOREIGN KEY (account_id) REFERENCES public.account(id) DEFERRABLE INITIALLY DEFERRED;
 L   ALTER TABLE ONLY public.receive_payment DROP CONSTRAINT fk2d07f06ae5fcf475;
       public       postgres    false    387    197    4243                       2606    28761    vendor_group fk2e46e4a8622c1275    FK CONSTRAINT     ?   ALTER TABLE ONLY public.vendor_group
    ADD CONSTRAINT fk2e46e4a8622c1275 FOREIGN KEY (company_id) REFERENCES public.company(id) DEFERRABLE INITIALLY DEFERRED;
 I   ALTER TABLE ONLY public.vendor_group DROP CONSTRAINT fk2e46e4a8622c1275;
       public       postgres    false    483    4309    250                       2606    28771    vendor_group fk2e46e4a89e5a0e30    FK CONSTRAINT     ?   ALTER TABLE ONLY public.vendor_group
    ADD CONSTRAINT fk2e46e4a89e5a0e30 FOREIGN KEY (last_modifier) REFERENCES public.users(id) DEFERRABLE INITIALLY DEFERRED;
 I   ALTER TABLE ONLY public.vendor_group DROP CONSTRAINT fk2e46e4a89e5a0e30;
       public       postgres    false    4585    474    483                       2606    28766    vendor_group fk2e46e4a8f1ae8cde    FK CONSTRAINT     ?   ALTER TABLE ONLY public.vendor_group
    ADD CONSTRAINT fk2e46e4a8f1ae8cde FOREIGN KEY (created_by) REFERENCES public.users(id) DEFERRABLE INITIALLY DEFERRED;
 I   ALTER TABLE ONLY public.vendor_group DROP CONSTRAINT fk2e46e4a8f1ae8cde;
       public       postgres    false    474    483    4585            !           2606    28806    warehouse fk2f074aa3622c1275    FK CONSTRAINT     ?   ALTER TABLE ONLY public.warehouse
    ADD CONSTRAINT fk2f074aa3622c1275 FOREIGN KEY (company_id) REFERENCES public.company(id) DEFERRABLE INITIALLY DEFERRED;
 F   ALTER TABLE ONLY public.warehouse DROP CONSTRAINT fk2f074aa3622c1275;
       public       postgres    false    250    488    4309            #           2606    28816    warehouse fk2f074aa39e5a0e30    FK CONSTRAINT     ?   ALTER TABLE ONLY public.warehouse
    ADD CONSTRAINT fk2f074aa39e5a0e30 FOREIGN KEY (last_modifier) REFERENCES public.users(id) DEFERRABLE INITIALLY DEFERRED;
 F   ALTER TABLE ONLY public.warehouse DROP CONSTRAINT fk2f074aa39e5a0e30;
       public       postgres    false    474    488    4585            "           2606    28811    warehouse fk2f074aa3f1ae8cde    FK CONSTRAINT     ?   ALTER TABLE ONLY public.warehouse
    ADD CONSTRAINT fk2f074aa3f1ae8cde FOREIGN KEY (created_by) REFERENCES public.users(id) DEFERRABLE INITIALLY DEFERRED;
 F   ALTER TABLE ONLY public.warehouse DROP CONSTRAINT fk2f074aa3f1ae8cde;
       public       postgres    false    474    488    4585            x           2606    26676 '   credits_and_payments fk303ef05a63880555    FK CONSTRAINT     ?   ALTER TABLE ONLY public.credits_and_payments
    ADD CONSTRAINT fk303ef05a63880555 FOREIGN KEY (transaction_id) REFERENCES public.transaction(id) DEFERRABLE INITIALLY DEFERRED;
 Q   ALTER TABLE ONLY public.credits_and_payments DROP CONSTRAINT fk303ef05a63880555;
       public       postgres    false    257    443    4553            y           2606    26681 '   credits_and_payments fk303ef05ab2fc5555    FK CONSTRAINT     ?   ALTER TABLE ONLY public.credits_and_payments
    ADD CONSTRAINT fk303ef05ab2fc5555 FOREIGN KEY (payee_id) REFERENCES public.payee(id) DEFERRABLE INITIALLY DEFERRED;
 Q   ALTER TABLE ONLY public.credits_and_payments DROP CONSTRAINT fk303ef05ab2fc5555;
       public       postgres    false    257    353    4439            ?           2606    28481 #   transaction_item fk30714bf41e282cdf    FK CONSTRAINT     ?   ALTER TABLE ONLY public.transaction_item
    ADD CONSTRAINT fk30714bf41e282cdf FOREIGN KEY (item_id) REFERENCES public.item(id) DEFERRABLE INITIALLY DEFERRED;
 M   ALTER TABLE ONLY public.transaction_item DROP CONSTRAINT fk30714bf41e282cdf;
       public       postgres    false    4393    453    315            ?           2606    28511 #   transaction_item fk30714bf446ae6ef6    FK CONSTRAINT     ?   ALTER TABLE ONLY public.transaction_item
    ADD CONSTRAINT fk30714bf446ae6ef6 FOREIGN KEY (qty_unit) REFERENCES public.unit(id) DEFERRABLE INITIALLY DEFERRED;
 M   ALTER TABLE ONLY public.transaction_item DROP CONSTRAINT fk30714bf446ae6ef6;
       public       postgres    false    453    4581    470            ?           2606    28506 #   transaction_item fk30714bf46360f0a7    FK CONSTRAINT     ?   ALTER TABLE ONLY public.transaction_item
    ADD CONSTRAINT fk30714bf46360f0a7 FOREIGN KEY (effecting_account) REFERENCES public.account(id) DEFERRABLE INITIALLY DEFERRED;
 M   ALTER TABLE ONLY public.transaction_item DROP CONSTRAINT fk30714bf46360f0a7;
       public       postgres    false    197    453    4243            ?           2606    28466 #   transaction_item fk30714bf463880555    FK CONSTRAINT     ?   ALTER TABLE ONLY public.transaction_item
    ADD CONSTRAINT fk30714bf463880555 FOREIGN KEY (transaction_id) REFERENCES public.transaction(id) DEFERRABLE INITIALLY DEFERRED;
 M   ALTER TABLE ONLY public.transaction_item DROP CONSTRAINT fk30714bf463880555;
       public       postgres    false    443    453    4553            ?           2606    28516 #   transaction_item fk30714bf46da97c61    FK CONSTRAINT     ?   ALTER TABLE ONLY public.transaction_item
    ADD CONSTRAINT fk30714bf46da97c61 FOREIGN KEY (trans_item_accounter_class) REFERENCES public.accounter_class(id) DEFERRABLE INITIALLY DEFERRED;
 M   ALTER TABLE ONLY public.transaction_item DROP CONSTRAINT fk30714bf46da97c61;
       public       postgres    false    4245    199    453            ?           2606    28491 #   transaction_item fk30714bf49495a8bc    FK CONSTRAINT     ?   ALTER TABLE ONLY public.transaction_item
    ADD CONSTRAINT fk30714bf49495a8bc FOREIGN KEY (ware_house) REFERENCES public.warehouse(id) DEFERRABLE INITIALLY DEFERRED;
 M   ALTER TABLE ONLY public.transaction_item DROP CONSTRAINT fk30714bf49495a8bc;
       public       postgres    false    488    453    4605            ?           2606    28486 "   transaction_item fk30714bf4aa7eff5    FK CONSTRAINT     ?   ALTER TABLE ONLY public.transaction_item
    ADD CONSTRAINT fk30714bf4aa7eff5 FOREIGN KEY (job_id) REFERENCES public.job(id) DEFERRABLE INITIALLY DEFERRED;
 L   ALTER TABLE ONLY public.transaction_item DROP CONSTRAINT fk30714bf4aa7eff5;
       public       postgres    false    324    453    4403            ?           2606    28471 #   transaction_item fk30714bf4b7b51814    FK CONSTRAINT     ?   ALTER TABLE ONLY public.transaction_item
    ADD CONSTRAINT fk30714bf4b7b51814 FOREIGN KEY (tax_code) REFERENCES public.tax_code(id) DEFERRABLE INITIALLY DEFERRED;
 M   ALTER TABLE ONLY public.transaction_item DROP CONSTRAINT fk30714bf4b7b51814;
       public       postgres    false    453    424    4529            ?           2606    28501 #   transaction_item fk30714bf4caa74743    FK CONSTRAINT     ?   ALTER TABLE ONLY public.transaction_item
    ADD CONSTRAINT fk30714bf4caa74743 FOREIGN KEY (referring_transaction_item_id) REFERENCES public.transaction_item(id) DEFERRABLE INITIALLY DEFERRED;
 M   ALTER TABLE ONLY public.transaction_item DROP CONSTRAINT fk30714bf4caa74743;
       public       postgres    false    453    453    4563            ?           2606    28496 #   transaction_item fk30714bf4dfe06a7f    FK CONSTRAINT     ?   ALTER TABLE ONLY public.transaction_item
    ADD CONSTRAINT fk30714bf4dfe06a7f FOREIGN KEY (customer_id) REFERENCES public.customer(id) DEFERRABLE INITIALLY DEFERRED;
 M   ALTER TABLE ONLY public.transaction_item DROP CONSTRAINT fk30714bf4dfe06a7f;
       public       postgres    false    261    453    4325            ?           2606    28476 #   transaction_item fk30714bf4e5fcf475    FK CONSTRAINT     ?   ALTER TABLE ONLY public.transaction_item
    ADD CONSTRAINT fk30714bf4e5fcf475 FOREIGN KEY (account_id) REFERENCES public.account(id) DEFERRABLE INITIALLY DEFERRED;
 M   ALTER TABLE ONLY public.transaction_item DROP CONSTRAINT fk30714bf4e5fcf475;
       public       postgres    false    453    197    4243            n           2606    27906    receive_vat fk3075f8d172fffee    FK CONSTRAINT     ?   ALTER TABLE ONLY public.receive_vat
    ADD CONSTRAINT fk3075f8d172fffee FOREIGN KEY (deposit_in_account_id) REFERENCES public.account(id) DEFERRABLE INITIALLY DEFERRED;
 G   ALTER TABLE ONLY public.receive_vat DROP CONSTRAINT fk3075f8d172fffee;
       public       postgres    false    388    197    4243            o           2606    27911    receive_vat fk3075f8d274bc854    FK CONSTRAINT     ?   ALTER TABLE ONLY public.receive_vat
    ADD CONSTRAINT fk3075f8d274bc854 FOREIGN KEY (id) REFERENCES public.transaction(id) DEFERRABLE INITIALLY DEFERRED;
 G   ALTER TABLE ONLY public.receive_vat DROP CONSTRAINT fk3075f8d274bc854;
       public       postgres    false    388    443    4553            m           2606    27901    receive_vat fk3075f8dc368d6ac    FK CONSTRAINT     ?   ALTER TABLE ONLY public.receive_vat
    ADD CONSTRAINT fk3075f8dc368d6ac FOREIGN KEY (tax_agency_id) REFERENCES public.taxagency(id) DEFERRABLE INITIALLY DEFERRED;
 G   ALTER TABLE ONLY public.receive_vat DROP CONSTRAINT fk3075f8dc368d6ac;
       public       postgres    false    388    419    4523            ?           2606    28191 %   tax_group_tax_item fk3404985b5a7f706c    FK CONSTRAINT     ?   ALTER TABLE ONLY public.tax_group_tax_item
    ADD CONSTRAINT fk3404985b5a7f706c FOREIGN KEY (tax_item_id) REFERENCES public.tax_item(id) DEFERRABLE INITIALLY DEFERRED;
 O   ALTER TABLE ONLY public.tax_group_tax_item DROP CONSTRAINT fk3404985b5a7f706c;
       public       postgres    false    426    427    4535            ?           2606    28186 %   tax_group_tax_item fk3404985bcd4ba408    FK CONSTRAINT     ?   ALTER TABLE ONLY public.tax_group_tax_item
    ADD CONSTRAINT fk3404985bcd4ba408 FOREIGN KEY (tax_group_id) REFERENCES public.tax_group(id) DEFERRABLE INITIALLY DEFERRED;
 O   ALTER TABLE ONLY public.tax_group_tax_item DROP CONSTRAINT fk3404985bcd4ba408;
       public       postgres    false    426    425    4531                       2606    27476    license fk34bc1021384ae49f    FK CONSTRAINT     ?   ALTER TABLE ONLY public.license
    ADD CONSTRAINT fk34bc1021384ae49f FOREIGN KEY (client_id) REFERENCES public.client(id) DEFERRABLE INITIALLY DEFERRED;
 D   ALTER TABLE ONLY public.license DROP CONSTRAINT fk34bc1021384ae49f;
       public       postgres    false    331    241    4299                       2606    46510 -   user_defined_payhead_items fk351e75bf23a62f66    FK CONSTRAINT     ?   ALTER TABLE ONLY public.user_defined_payhead_items
    ADD CONSTRAINT fk351e75bf23a62f66 FOREIGN KEY (pay_head) REFERENCES public.user_defined_pay_head(id) DEFERRABLE INITIALLY DEFERRED;
 W   ALTER TABLE ONLY public.user_defined_payhead_items DROP CONSTRAINT fk351e75bf23a62f66;
       public       postgres    false    506    475    4623                       2606    28706 -   user_defined_payhead_items fk351e75bf746af92a    FK CONSTRAINT     ?   ALTER TABLE ONLY public.user_defined_payhead_items
    ADD CONSTRAINT fk351e75bf746af92a FOREIGN KEY (user_defined_payhead_item_id) REFERENCES public.attendance_management_item(id) DEFERRABLE INITIALLY DEFERRED;
 W   ALTER TABLE ONLY public.user_defined_payhead_items DROP CONSTRAINT fk351e75bf746af92a;
       public       postgres    false    4265    475    218            0           2606    27596    payee_fields fk3648d7b0b2fc5555    FK CONSTRAINT     ?   ALTER TABLE ONLY public.payee_fields
    ADD CONSTRAINT fk3648d7b0b2fc5555 FOREIGN KEY (payee_id) REFERENCES public.payee(id) DEFERRABLE INITIALLY DEFERRED;
 I   ALTER TABLE ONLY public.payee_fields DROP CONSTRAINT fk3648d7b0b2fc5555;
       public       postgres    false    353    358    4439            ?           2606    26816 "   customer_refund fk385d6af9274bc854    FK CONSTRAINT     ?   ALTER TABLE ONLY public.customer_refund
    ADD CONSTRAINT fk385d6af9274bc854 FOREIGN KEY (id) REFERENCES public.transaction(id) DEFERRABLE INITIALLY DEFERRED;
 L   ALTER TABLE ONLY public.customer_refund DROP CONSTRAINT fk385d6af9274bc854;
       public       postgres    false    4553    266    443            ?           2606    26821 "   customer_refund fk385d6af968a08e82    FK CONSTRAINT     ?   ALTER TABLE ONLY public.customer_refund
    ADD CONSTRAINT fk385d6af968a08e82 FOREIGN KEY (payfrom_account_id) REFERENCES public.account(id) DEFERRABLE INITIALLY DEFERRED;
 L   ALTER TABLE ONLY public.customer_refund DROP CONSTRAINT fk385d6af968a08e82;
       public       postgres    false    4243    197    266            ?           2606    26826 "   customer_refund fk385d6af9dfe06a7f    FK CONSTRAINT     ?   ALTER TABLE ONLY public.customer_refund
    ADD CONSTRAINT fk385d6af9dfe06a7f FOREIGN KEY (customer_id) REFERENCES public.customer(id) DEFERRABLE INITIALLY DEFERRED;
 L   ALTER TABLE ONLY public.customer_refund DROP CONSTRAINT fk385d6af9dfe06a7f;
       public       postgres    false    266    4325    261            ?           2606    26786 !   customer_group fk3b0af2be622c1275    FK CONSTRAINT     ?   ALTER TABLE ONLY public.customer_group
    ADD CONSTRAINT fk3b0af2be622c1275 FOREIGN KEY (company_id) REFERENCES public.company(id) DEFERRABLE INITIALLY DEFERRED;
 K   ALTER TABLE ONLY public.customer_group DROP CONSTRAINT fk3b0af2be622c1275;
       public       postgres    false    4309    264    250            ?           2606    26796 !   customer_group fk3b0af2be9e5a0e30    FK CONSTRAINT     ?   ALTER TABLE ONLY public.customer_group
    ADD CONSTRAINT fk3b0af2be9e5a0e30 FOREIGN KEY (last_modifier) REFERENCES public.users(id) DEFERRABLE INITIALLY DEFERRED;
 K   ALTER TABLE ONLY public.customer_group DROP CONSTRAINT fk3b0af2be9e5a0e30;
       public       postgres    false    4585    264    474            ?           2606    26791 !   customer_group fk3b0af2bef1ae8cde    FK CONSTRAINT     ?   ALTER TABLE ONLY public.customer_group
    ADD CONSTRAINT fk3b0af2bef1ae8cde FOREIGN KEY (created_by) REFERENCES public.users(id) DEFERRABLE INITIALLY DEFERRED;
 K   ALTER TABLE ONLY public.customer_group DROP CONSTRAINT fk3b0af2bef1ae8cde;
       public       postgres    false    264    4585    474            ?           2606    28301 %   tdsdeductormasters fk3b378348622c1275    FK CONSTRAINT     ?   ALTER TABLE ONLY public.tdsdeductormasters
    ADD CONSTRAINT fk3b378348622c1275 FOREIGN KEY (company_id) REFERENCES public.company(id) DEFERRABLE INITIALLY DEFERRED;
 O   ALTER TABLE ONLY public.tdsdeductormasters DROP CONSTRAINT fk3b378348622c1275;
       public       postgres    false    436    250    4309            ?           2606    28401 3   transaction_credits_and_payments fk3d29e73b33659a02    FK CONSTRAINT     ?   ALTER TABLE ONLY public.transaction_credits_and_payments
    ADD CONSTRAINT fk3d29e73b33659a02 FOREIGN KEY (transaction_paybill_id) REFERENCES public.transaction_paybill(id) DEFERRABLE INITIALLY DEFERRED;
 ]   ALTER TABLE ONLY public.transaction_credits_and_payments DROP CONSTRAINT fk3d29e73b33659a02;
       public       postgres    false    445    457    4567            ?           2606    28396 3   transaction_credits_and_payments fk3d29e73b8d37ed0d    FK CONSTRAINT     ?   ALTER TABLE ONLY public.transaction_credits_and_payments
    ADD CONSTRAINT fk3d29e73b8d37ed0d FOREIGN KEY (transaction_receive_payment_id) REFERENCES public.transaction_receive_payment(id) DEFERRABLE INITIALLY DEFERRED;
 ]   ALTER TABLE ONLY public.transaction_credits_and_payments DROP CONSTRAINT fk3d29e73b8d37ed0d;
       public       postgres    false    445    465    4575            ?           2606    28391 3   transaction_credits_and_payments fk3d29e73bac60fbcf    FK CONSTRAINT     ?   ALTER TABLE ONLY public.transaction_credits_and_payments
    ADD CONSTRAINT fk3d29e73bac60fbcf FOREIGN KEY (credits_and_payments_id) REFERENCES public.credits_and_payments(id) DEFERRABLE INITIALLY DEFERRED;
 ]   ALTER TABLE ONLY public.transaction_credits_and_payments DROP CONSTRAINT fk3d29e73bac60fbcf;
       public       postgres    false    445    257    4319            ?           2606    27101 &   fixed_asset_history fk4159985a33118956    FK CONSTRAINT     ?   ALTER TABLE ONLY public.fixed_asset_history
    ADD CONSTRAINT fk4159985a33118956 FOREIGN KEY (fixed_asset_id) REFERENCES public.fixed_asset(id) DEFERRABLE INITIALLY DEFERRED;
 P   ALTER TABLE ONLY public.fixed_asset_history DROP CONSTRAINT fk4159985a33118956;
       public       postgres    false    4369    296    298            ?           2606    27091 &   fixed_asset_history fk4159985a408f6290    FK CONSTRAINT     ?   ALTER TABLE ONLY public.fixed_asset_history
    ADD CONSTRAINT fk4159985a408f6290 FOREIGN KEY (journal_entry_id) REFERENCES public.journal_entry(id) DEFERRABLE INITIALLY DEFERRED;
 P   ALTER TABLE ONLY public.fixed_asset_history DROP CONSTRAINT fk4159985a408f6290;
       public       postgres    false    4405    325    298            ?           2606    27096 &   fixed_asset_history fk4159985a622c1275    FK CONSTRAINT     ?   ALTER TABLE ONLY public.fixed_asset_history
    ADD CONSTRAINT fk4159985a622c1275 FOREIGN KEY (company_id) REFERENCES public.company(id) DEFERRABLE INITIALLY DEFERRED;
 P   ALTER TABLE ONLY public.fixed_asset_history DROP CONSTRAINT fk4159985a622c1275;
       public       postgres    false    250    298    4309            [           2606    27811 '   portlet_configuration fk42c53317cd0f285    FK CONSTRAINT     ?   ALTER TABLE ONLY public.portlet_configuration
    ADD CONSTRAINT fk42c53317cd0f285 FOREIGN KEY (portlet_config_id) REFERENCES public.portlet_page_configuration(id) DEFERRABLE INITIALLY DEFERRED;
 Q   ALTER TABLE ONLY public.portlet_configuration DROP CONSTRAINT fk42c53317cd0f285;
       public       postgres    false    379    382    4477            W           2606    26511 #   client_languages fk42e3c887384ae49f    FK CONSTRAINT     ?   ALTER TABLE ONLY public.client_languages
    ADD CONSTRAINT fk42e3c887384ae49f FOREIGN KEY (client_id) REFERENCES public.client(id) DEFERRABLE INITIALLY DEFERRED;
 M   ALTER TABLE ONLY public.client_languages DROP CONSTRAINT fk42e3c887384ae49f;
       public       postgres    false    241    242    4299            V           2606    26506 #   client_languages fk42e3c887838948f3    FK CONSTRAINT     ?   ALTER TABLE ONLY public.client_languages
    ADD CONSTRAINT fk42e3c887838948f3 FOREIGN KEY (language_code) REFERENCES public.language(code) DEFERRABLE INITIALLY DEFERRED;
 M   ALTER TABLE ONLY public.client_languages DROP CONSTRAINT fk42e3c887838948f3;
       public       postgres    false    242    329    4411            ?           2606    26931 &   enterbill_estimates fk430f76ab20610806    FK CONSTRAINT     ?   ALTER TABLE ONLY public.enterbill_estimates
    ADD CONSTRAINT fk430f76ab20610806 FOREIGN KEY (enter_bill_id) REFERENCES public.enter_bill(id) DEFERRABLE INITIALLY DEFERRED;
 P   ALTER TABLE ONLY public.enterbill_estimates DROP CONSTRAINT fk430f76ab20610806;
       public       postgres    false    4359    287    288            ?           2606    26936 &   enterbill_estimates fk430f76abb0901b5a    FK CONSTRAINT     ?   ALTER TABLE ONLY public.enterbill_estimates
    ADD CONSTRAINT fk430f76abb0901b5a FOREIGN KEY (elt) REFERENCES public.estimate(id) DEFERRABLE INITIALLY DEFERRED;
 P   ALTER TABLE ONLY public.enterbill_estimates DROP CONSTRAINT fk430f76abb0901b5a;
       public       postgres    false    4363    287    290                       2606    28661     transfer_fund fk43706699274bc854    FK CONSTRAINT     ?   ALTER TABLE ONLY public.transfer_fund
    ADD CONSTRAINT fk43706699274bc854 FOREIGN KEY (id) REFERENCES public.transaction(id) DEFERRABLE INITIALLY DEFERRED;
 J   ALTER TABLE ONLY public.transfer_fund DROP CONSTRAINT fk43706699274bc854;
       public       postgres    false    4553    468    443                       2606    28656     transfer_fund fk43706699308d799c    FK CONSTRAINT     ?   ALTER TABLE ONLY public.transfer_fund
    ADD CONSTRAINT fk43706699308d799c FOREIGN KEY (deposit_in_id) REFERENCES public.account(id) DEFERRABLE INITIALLY DEFERRED;
 J   ALTER TABLE ONLY public.transfer_fund DROP CONSTRAINT fk43706699308d799c;
       public       postgres    false    468    197    4243                       2606    28666     transfer_fund fk43706699a86f5937    FK CONSTRAINT     ?   ALTER TABLE ONLY public.transfer_fund
    ADD CONSTRAINT fk43706699a86f5937 FOREIGN KEY (deposit_from_id) REFERENCES public.account(id) DEFERRABLE INITIALLY DEFERRED;
 J   ALTER TABLE ONLY public.transfer_fund DROP CONSTRAINT fk43706699a86f5937;
       public       postgres    false    197    468    4243                       2606    28651     transfer_fund fk43706699dea009c1    FK CONSTRAINT     ?   ALTER TABLE ONLY public.transfer_fund
    ADD CONSTRAINT fk43706699dea009c1 FOREIGN KEY (cash_back_account_id) REFERENCES public.account(id) DEFERRABLE INITIALLY DEFERRED;
 J   ALTER TABLE ONLY public.transfer_fund DROP CONSTRAINT fk43706699dea009c1;
       public       postgres    false    4243    468    197            $           2606    27536     mobile_cookie fk45675ca158dbf25b    FK CONSTRAINT     ?   ALTER TABLE ONLY public.mobile_cookie
    ADD CONSTRAINT fk45675ca158dbf25b FOREIGN KEY (client) REFERENCES public.client(id) DEFERRABLE INITIALLY DEFERRED;
 J   ALTER TABLE ONLY public.mobile_cookie DROP CONSTRAINT fk45675ca158dbf25b;
       public       postgres    false    4299    241    347            &           2606    27546    payee fk4863b28147d4a2c    FK CONSTRAINT     ?   ALTER TABLE ONLY public.payee
    ADD CONSTRAINT fk4863b28147d4a2c FOREIGN KEY (tax_code_id) REFERENCES public.tax_code(id) DEFERRABLE INITIALLY DEFERRED;
 A   ALTER TABLE ONLY public.payee DROP CONSTRAINT fk4863b28147d4a2c;
       public       postgres    false    353    4529    424            (           2606    27556    payee fk4863b284c56a230    FK CONSTRAINT     ?   ALTER TABLE ONLY public.payee
    ADD CONSTRAINT fk4863b284c56a230 FOREIGN KEY (tds_tax_item_id) REFERENCES public.tax_item(id) DEFERRABLE INITIALLY DEFERRED;
 A   ALTER TABLE ONLY public.payee DROP CONSTRAINT fk4863b284c56a230;
       public       postgres    false    4535    353    427            '           2606    27551    payee fk4863b28622c1275    FK CONSTRAINT     ?   ALTER TABLE ONLY public.payee
    ADD CONSTRAINT fk4863b28622c1275 FOREIGN KEY (company_id) REFERENCES public.company(id) DEFERRABLE INITIALLY DEFERRED;
 A   ALTER TABLE ONLY public.payee DROP CONSTRAINT fk4863b28622c1275;
       public       postgres    false    353    250    4309            )           2606    27561    payee fk4863b2869f64da7    FK CONSTRAINT     ?   ALTER TABLE ONLY public.payee
    ADD CONSTRAINT fk4863b2869f64da7 FOREIGN KEY (currency) REFERENCES public.currency(id) DEFERRABLE INITIALLY DEFERRED;
 A   ALTER TABLE ONLY public.payee DROP CONSTRAINT fk4863b2869f64da7;
       public       postgres    false    353    4323    260            +           2606    27571    payee fk4863b289e5a0e30    FK CONSTRAINT     ?   ALTER TABLE ONLY public.payee
    ADD CONSTRAINT fk4863b289e5a0e30 FOREIGN KEY (last_modifier) REFERENCES public.users(id) DEFERRABLE INITIALLY DEFERRED;
 A   ALTER TABLE ONLY public.payee DROP CONSTRAINT fk4863b289e5a0e30;
       public       postgres    false    353    474    4585            *           2606    27566    payee fk4863b28f1ae8cde    FK CONSTRAINT     ?   ALTER TABLE ONLY public.payee
    ADD CONSTRAINT fk4863b28f1ae8cde FOREIGN KEY (created_by) REFERENCES public.users(id) DEFERRABLE INITIALLY DEFERRED;
 A   ALTER TABLE ONLY public.payee DROP CONSTRAINT fk4863b28f1ae8cde;
       public       postgres    false    4585    353    474            ?           2606    28586 )   transaction_pay_expense fk49eebf8029fc095    FK CONSTRAINT     ?   ALTER TABLE ONLY public.transaction_pay_expense
    ADD CONSTRAINT fk49eebf8029fc095 FOREIGN KEY (expense_id) REFERENCES public.expense(id) DEFERRABLE INITIALLY DEFERRED;
 S   ALTER TABLE ONLY public.transaction_pay_expense DROP CONSTRAINT fk49eebf8029fc095;
       public       postgres    false    4365    461    291            ?           2606    28581 *   transaction_pay_expense fk49eebf808d47192e    FK CONSTRAINT     ?   ALTER TABLE ONLY public.transaction_pay_expense
    ADD CONSTRAINT fk49eebf808d47192e FOREIGN KEY (pay_expense_id) REFERENCES public.pay_expense(id) DEFERRABLE INITIALLY DEFERRED;
 T   ALTER TABLE ONLY public.transaction_pay_expense DROP CONSTRAINT fk49eebf808d47192e;
       public       postgres    false    4459    461    367                       2606    28696    users fk4d495e8384ae49f    FK CONSTRAINT     ?   ALTER TABLE ONLY public.users
    ADD CONSTRAINT fk4d495e8384ae49f FOREIGN KEY (client_id) REFERENCES public.client(id) DEFERRABLE INITIALLY DEFERRED;
 A   ALTER TABLE ONLY public.users DROP CONSTRAINT fk4d495e8384ae49f;
       public       postgres    false    241    4299    474            
           2606    28686    users fk4d495e8622c1275    FK CONSTRAINT     ?   ALTER TABLE ONLY public.users
    ADD CONSTRAINT fk4d495e8622c1275 FOREIGN KEY (company_id) REFERENCES public.company(id) DEFERRABLE INITIALLY DEFERRED;
 A   ALTER TABLE ONLY public.users DROP CONSTRAINT fk4d495e8622c1275;
       public       postgres    false    250    474    4309                       2606    28691    users fk4d495e8d7cf483e    FK CONSTRAINT     ?   ALTER TABLE ONLY public.users
    ADD CONSTRAINT fk4d495e8d7cf483e FOREIGN KEY (user_permissions_id) REFERENCES public.user_permissions(id) DEFERRABLE INITIALLY DEFERRED;
 A   ALTER TABLE ONLY public.users DROP CONSTRAINT fk4d495e8d7cf483e;
       public       postgres    false    4591    477    474            !           2606    26241 $   adjustment_reason fk4d50a556622c1275    FK CONSTRAINT     ?   ALTER TABLE ONLY public.adjustment_reason
    ADD CONSTRAINT fk4d50a556622c1275 FOREIGN KEY (company_id) REFERENCES public.company(id) DEFERRABLE INITIALLY DEFERRED;
 N   ALTER TABLE ONLY public.adjustment_reason DROP CONSTRAINT fk4d50a556622c1275;
       public       postgres    false    4309    208    250            #           2606    26251 $   adjustment_reason fk4d50a5569e5a0e30    FK CONSTRAINT     ?   ALTER TABLE ONLY public.adjustment_reason
    ADD CONSTRAINT fk4d50a5569e5a0e30 FOREIGN KEY (last_modifier) REFERENCES public.users(id) DEFERRABLE INITIALLY DEFERRED;
 N   ALTER TABLE ONLY public.adjustment_reason DROP CONSTRAINT fk4d50a5569e5a0e30;
       public       postgres    false    208    474    4585            "           2606    26246 $   adjustment_reason fk4d50a556f1ae8cde    FK CONSTRAINT     ?   ALTER TABLE ONLY public.adjustment_reason
    ADD CONSTRAINT fk4d50a556f1ae8cde FOREIGN KEY (created_by) REFERENCES public.users(id) DEFERRABLE INITIALLY DEFERRED;
 N   ALTER TABLE ONLY public.adjustment_reason DROP CONSTRAINT fk4d50a556f1ae8cde;
       public       postgres    false    208    474    4585            	           2606    28681 "   unit_of_measure fk4e9c4471622c1275    FK CONSTRAINT     ?   ALTER TABLE ONLY public.unit_of_measure
    ADD CONSTRAINT fk4e9c4471622c1275 FOREIGN KEY (company_id) REFERENCES public.company(id) DEFERRABLE INITIALLY DEFERRED;
 L   ALTER TABLE ONLY public.unit_of_measure DROP CONSTRAINT fk4e9c4471622c1275;
       public       postgres    false    250    472    4309            ?           2606    27196 %   inventory_purchase fk4fa0a0a446ae6ef6    FK CONSTRAINT     ?   ALTER TABLE ONLY public.inventory_purchase
    ADD CONSTRAINT fk4fa0a0a446ae6ef6 FOREIGN KEY (qty_unit) REFERENCES public.unit(id) DEFERRABLE INITIALLY DEFERRED;
 O   ALTER TABLE ONLY public.inventory_purchase DROP CONSTRAINT fk4fa0a0a446ae6ef6;
       public       postgres    false    470    309    4581            ?           2606    27191 %   inventory_purchase fk4fa0a0a46360f0a7    FK CONSTRAINT     ?   ALTER TABLE ONLY public.inventory_purchase
    ADD CONSTRAINT fk4fa0a0a46360f0a7 FOREIGN KEY (effecting_account) REFERENCES public.account(id) DEFERRABLE INITIALLY DEFERRED;
 O   ALTER TABLE ONLY public.inventory_purchase DROP CONSTRAINT fk4fa0a0a46360f0a7;
       public       postgres    false    197    309    4243            ?           2606    27201 %   inventory_purchase fk4fa0a0a478f5db52    FK CONSTRAINT     ?   ALTER TABLE ONLY public.inventory_purchase
    ADD CONSTRAINT fk4fa0a0a478f5db52 FOREIGN KEY (transaction_item_id) REFERENCES public.transaction_item(id) DEFERRABLE INITIALLY DEFERRED;
 O   ALTER TABLE ONLY public.inventory_purchase DROP CONSTRAINT fk4fa0a0a478f5db52;
       public       postgres    false    453    309    4563            2           2606    27606    payee_update fk504391c0622c1275    FK CONSTRAINT     ?   ALTER TABLE ONLY public.payee_update
    ADD CONSTRAINT fk504391c0622c1275 FOREIGN KEY (company_id) REFERENCES public.company(id) DEFERRABLE INITIALLY DEFERRED;
 I   ALTER TABLE ONLY public.payee_update DROP CONSTRAINT fk504391c0622c1275;
       public       postgres    false    360    250    4309            1           2606    27601    payee_update fk504391c0675b6f2b    FK CONSTRAINT     ?   ALTER TABLE ONLY public.payee_update
    ADD CONSTRAINT fk504391c0675b6f2b FOREIGN KEY (payee) REFERENCES public.payee(id) DEFERRABLE INITIALLY DEFERRED;
 I   ALTER TABLE ONLY public.payee_update DROP CONSTRAINT fk504391c0675b6f2b;
       public       postgres    false    360    353    4439            5           2606    27621    payee_update fk504391c09e5a0e30    FK CONSTRAINT     ?   ALTER TABLE ONLY public.payee_update
    ADD CONSTRAINT fk504391c09e5a0e30 FOREIGN KEY (last_modifier) REFERENCES public.users(id) DEFERRABLE INITIALLY DEFERRED;
 I   ALTER TABLE ONLY public.payee_update DROP CONSTRAINT fk504391c09e5a0e30;
       public       postgres    false    4585    474    360            4           2606    27616    payee_update fk504391c0a6ec8e17    FK CONSTRAINT     ?   ALTER TABLE ONLY public.payee_update
    ADD CONSTRAINT fk504391c0a6ec8e17 FOREIGN KEY (transaction) REFERENCES public.transaction(id) DEFERRABLE INITIALLY DEFERRED;
 I   ALTER TABLE ONLY public.payee_update DROP CONSTRAINT fk504391c0a6ec8e17;
       public       postgres    false    360    4553    443            3           2606    27611    payee_update fk504391c0f1ae8cde    FK CONSTRAINT     ?   ALTER TABLE ONLY public.payee_update
    ADD CONSTRAINT fk504391c0f1ae8cde FOREIGN KEY (created_by) REFERENCES public.users(id) DEFERRABLE INITIALLY DEFERRED;
 I   ALTER TABLE ONLY public.payee_update DROP CONSTRAINT fk504391c0f1ae8cde;
       public       postgres    false    360    474    4585            ?           2606    26771 '   customer_credit_memo fk50a48bdf274bc854    FK CONSTRAINT     ?   ALTER TABLE ONLY public.customer_credit_memo
    ADD CONSTRAINT fk50a48bdf274bc854 FOREIGN KEY (id) REFERENCES public.transaction(id) DEFERRABLE INITIALLY DEFERRED;
 Q   ALTER TABLE ONLY public.customer_credit_memo DROP CONSTRAINT fk50a48bdf274bc854;
       public       postgres    false    262    443    4553            ?           2606    26781 '   customer_credit_memo fk50a48bdf4c74beae    FK CONSTRAINT     ?   ALTER TABLE ONLY public.customer_credit_memo
    ADD CONSTRAINT fk50a48bdf4c74beae FOREIGN KEY (sales_person_id) REFERENCES public.sales_person(id) DEFERRABLE INITIALLY DEFERRED;
 Q   ALTER TABLE ONLY public.customer_credit_memo DROP CONSTRAINT fk50a48bdf4c74beae;
       public       postgres    false    4503    262    402            ?           2606    26766 '   customer_credit_memo fk50a48bdf9a3059ec    FK CONSTRAINT     ?   ALTER TABLE ONLY public.customer_credit_memo
    ADD CONSTRAINT fk50a48bdf9a3059ec FOREIGN KEY (price_level_id) REFERENCES public.pricelevel(id) DEFERRABLE INITIALLY DEFERRED;
 Q   ALTER TABLE ONLY public.customer_credit_memo DROP CONSTRAINT fk50a48bdf9a3059ec;
       public       postgres    false    262    4479    384            ?           2606    26776 '   customer_credit_memo fk50a48bdfdfe06a7f    FK CONSTRAINT     ?   ALTER TABLE ONLY public.customer_credit_memo
    ADD CONSTRAINT fk50a48bdfdfe06a7f FOREIGN KEY (customer_id) REFERENCES public.customer(id) DEFERRABLE INITIALLY DEFERRED;
 Q   ALTER TABLE ONLY public.customer_credit_memo DROP CONSTRAINT fk50a48bdfdfe06a7f;
       public       postgres    false    261    4325    262            ?           2606    26761 '   customer_credit_memo fk50a48bdfe5fcf475    FK CONSTRAINT     ?   ALTER TABLE ONLY public.customer_credit_memo
    ADD CONSTRAINT fk50a48bdfe5fcf475 FOREIGN KEY (account_id) REFERENCES public.account(id) DEFERRABLE INITIALLY DEFERRED;
 Q   ALTER TABLE ONLY public.customer_credit_memo DROP CONSTRAINT fk50a48bdfe5fcf475;
       public       postgres    false    4243    262    197            ~           2606    26706    currency fk50f1e01132175d8d    FK CONSTRAINT     ?   ALTER TABLE ONLY public.currency
    ADD CONSTRAINT fk50f1e01132175d8d FOREIGN KEY (accounts_receivable_id) REFERENCES public.account(id) DEFERRABLE INITIALLY DEFERRED;
 E   ALTER TABLE ONLY public.currency DROP CONSTRAINT fk50f1e01132175d8d;
       public       postgres    false    260    4243    197            }           2606    26701    currency fk50f1e011622c1275    FK CONSTRAINT     ?   ALTER TABLE ONLY public.currency
    ADD CONSTRAINT fk50f1e011622c1275 FOREIGN KEY (company_id) REFERENCES public.company(id) DEFERRABLE INITIALLY DEFERRED;
 E   ALTER TABLE ONLY public.currency DROP CONSTRAINT fk50f1e011622c1275;
       public       postgres    false    4309    260    250                       2606    26711    currency fk50f1e011cf7d8e79    FK CONSTRAINT     ?   ALTER TABLE ONLY public.currency
    ADD CONSTRAINT fk50f1e011cf7d8e79 FOREIGN KEY (accounts_payable_id) REFERENCES public.account(id) DEFERRABLE INITIALLY DEFERRED;
 E   ALTER TABLE ONLY public.currency DROP CONSTRAINT fk50f1e011cf7d8e79;
       public       postgres    false    4243    260    197            ?           2606    26756    customer fk52c76fde338bd6bb    FK CONSTRAINT     ?   ALTER TABLE ONLY public.customer
    ADD CONSTRAINT fk52c76fde338bd6bb FOREIGN KEY (payment_term_id) REFERENCES public.paymentterms(id) DEFERRABLE INITIALLY DEFERRED;
 E   ALTER TABLE ONLY public.customer DROP CONSTRAINT fk52c76fde338bd6bb;
       public       postgres    false    4451    261    362            ?           2606    26751    customer fk52c76fde42db20f8    FK CONSTRAINT     ?   ALTER TABLE ONLY public.customer
    ADD CONSTRAINT fk52c76fde42db20f8 FOREIGN KEY (customer_group_id) REFERENCES public.customer_group(id) DEFERRABLE INITIALLY DEFERRED;
 E   ALTER TABLE ONLY public.customer DROP CONSTRAINT fk52c76fde42db20f8;
       public       postgres    false    264    4329    261            ?           2606    26746    customer fk52c76fde4c74beae    FK CONSTRAINT     ?   ALTER TABLE ONLY public.customer
    ADD CONSTRAINT fk52c76fde4c74beae FOREIGN KEY (sales_person_id) REFERENCES public.sales_person(id) DEFERRABLE INITIALLY DEFERRED;
 E   ALTER TABLE ONLY public.customer DROP CONSTRAINT fk52c76fde4c74beae;
       public       postgres    false    402    4503    261            ?           2606    26741    customer fk52c76fde610348fe    FK CONSTRAINT     ?   ALTER TABLE ONLY public.customer
    ADD CONSTRAINT fk52c76fde610348fe FOREIGN KEY (id) REFERENCES public.payee(id) DEFERRABLE INITIALLY DEFERRED;
 E   ALTER TABLE ONLY public.customer DROP CONSTRAINT fk52c76fde610348fe;
       public       postgres    false    353    261    4439            ?           2606    26726    customer fk52c76fde622c1275    FK CONSTRAINT     ?   ALTER TABLE ONLY public.customer
    ADD CONSTRAINT fk52c76fde622c1275 FOREIGN KEY (company_id) REFERENCES public.company(id) DEFERRABLE INITIALLY DEFERRED;
 E   ALTER TABLE ONLY public.customer DROP CONSTRAINT fk52c76fde622c1275;
       public       postgres    false    4309    261    250            ?           2606    26721    customer fk52c76fde9a3059ec    FK CONSTRAINT     ?   ALTER TABLE ONLY public.customer
    ADD CONSTRAINT fk52c76fde9a3059ec FOREIGN KEY (price_level_id) REFERENCES public.pricelevel(id) DEFERRABLE INITIALLY DEFERRED;
 E   ALTER TABLE ONLY public.customer DROP CONSTRAINT fk52c76fde9a3059ec;
       public       postgres    false    384    261    4479            ?           2606    26731    customer fk52c76fdead0a95dc    FK CONSTRAINT     ?   ALTER TABLE ONLY public.customer
    ADD CONSTRAINT fk52c76fdead0a95dc FOREIGN KEY (shipping_method_id) REFERENCES public.shippingmethod(id) DEFERRABLE INITIALLY DEFERRED;
 E   ALTER TABLE ONLY public.customer DROP CONSTRAINT fk52c76fdead0a95dc;
       public       postgres    false    4507    406    261            ?           2606    26736    customer fk52c76fdecd4ba408    FK CONSTRAINT     ?   ALTER TABLE ONLY public.customer
    ADD CONSTRAINT fk52c76fdecd4ba408 FOREIGN KEY (tax_group_id) REFERENCES public.tax_group(id) DEFERRABLE INITIALLY DEFERRED;
 E   ALTER TABLE ONLY public.customer DROP CONSTRAINT fk52c76fdecd4ba408;
       public       postgres    false    4531    261    425            ?           2606    26716    customer fk52c76fdee8300e32    FK CONSTRAINT     ?   ALTER TABLE ONLY public.customer
    ADD CONSTRAINT fk52c76fdee8300e32 FOREIGN KEY (credit_rating_id) REFERENCES public.creditrating(id) DEFERRABLE INITIALLY DEFERRED;
 E   ALTER TABLE ONLY public.customer DROP CONSTRAINT fk52c76fdee8300e32;
       public       postgres    false    261    255    4317            %           2606    26261    attachments fk54475f90622c1275    FK CONSTRAINT     ?   ALTER TABLE ONLY public.attachments
    ADD CONSTRAINT fk54475f90622c1275 FOREIGN KEY (company_id) REFERENCES public.company(id) DEFERRABLE INITIALLY DEFERRED;
 H   ALTER TABLE ONLY public.attachments DROP CONSTRAINT fk54475f90622c1275;
       public       postgres    false    4309    216    250            $           2606    26256    attachments fk54475f9063880555    FK CONSTRAINT     ?   ALTER TABLE ONLY public.attachments
    ADD CONSTRAINT fk54475f9063880555 FOREIGN KEY (transaction_id) REFERENCES public.transaction(id) DEFERRABLE INITIALLY DEFERRED;
 H   ALTER TABLE ONLY public.attachments DROP CONSTRAINT fk54475f9063880555;
       public       postgres    false    216    4553    443            '           2606    26271    attachments fk54475f909e5a0e30    FK CONSTRAINT     ?   ALTER TABLE ONLY public.attachments
    ADD CONSTRAINT fk54475f909e5a0e30 FOREIGN KEY (last_modifier) REFERENCES public.users(id) DEFERRABLE INITIALLY DEFERRED;
 H   ALTER TABLE ONLY public.attachments DROP CONSTRAINT fk54475f909e5a0e30;
       public       postgres    false    474    4585    216            &           2606    26266    attachments fk54475f90f1ae8cde    FK CONSTRAINT     ?   ALTER TABLE ONLY public.attachments
    ADD CONSTRAINT fk54475f90f1ae8cde FOREIGN KEY (created_by) REFERENCES public.users(id) DEFERRABLE INITIALLY DEFERRED;
 H   ALTER TABLE ONLY public.attachments DROP CONSTRAINT fk54475f90f1ae8cde;
       public       postgres    false    216    474    4585            ?           2606    28251 &   tax_rate_calculation fk559c0bfe1a90f65    FK CONSTRAINT     ?   ALTER TABLE ONLY public.tax_rate_calculation
    ADD CONSTRAINT fk559c0bfe1a90f65 FOREIGN KEY (purchase_liability_account_id) REFERENCES public.account(id) DEFERRABLE INITIALLY DEFERRED;
 P   ALTER TABLE ONLY public.tax_rate_calculation DROP CONSTRAINT fk559c0bfe1a90f65;
       public       postgres    false    431    197    4243            ?           2606    28256 '   tax_rate_calculation fk559c0bfe5a7f706c    FK CONSTRAINT     ?   ALTER TABLE ONLY public.tax_rate_calculation
    ADD CONSTRAINT fk559c0bfe5a7f706c FOREIGN KEY (tax_item_id) REFERENCES public.tax_item(id) DEFERRABLE INITIALLY DEFERRED;
 Q   ALTER TABLE ONLY public.tax_rate_calculation DROP CONSTRAINT fk559c0bfe5a7f706c;
       public       postgres    false    431    427    4535            ?           2606    28231 '   tax_rate_calculation fk559c0bfe63880555    FK CONSTRAINT     ?   ALTER TABLE ONLY public.tax_rate_calculation
    ADD CONSTRAINT fk559c0bfe63880555 FOREIGN KEY (transaction_id) REFERENCES public.transaction(id) DEFERRABLE INITIALLY DEFERRED;
 Q   ALTER TABLE ONLY public.tax_rate_calculation DROP CONSTRAINT fk559c0bfe63880555;
       public       postgres    false    431    443    4553            ?           2606    28261 '   tax_rate_calculation fk559c0bfe941a7bbf    FK CONSTRAINT     ?   ALTER TABLE ONLY public.tax_rate_calculation
    ADD CONSTRAINT fk559c0bfe941a7bbf FOREIGN KEY (vat_return_box_id) REFERENCES public.vatreturnbox(id) DEFERRABLE INITIALLY DEFERRED;
 Q   ALTER TABLE ONLY public.tax_rate_calculation DROP CONSTRAINT fk559c0bfe941a7bbf;
       public       postgres    false    431    479    4593            ?           2606    28241 '   tax_rate_calculation fk559c0bfec368d6ac    FK CONSTRAINT     ?   ALTER TABLE ONLY public.tax_rate_calculation
    ADD CONSTRAINT fk559c0bfec368d6ac FOREIGN KEY (tax_agency_id) REFERENCES public.taxagency(id) DEFERRABLE INITIALLY DEFERRED;
 Q   ALTER TABLE ONLY public.tax_rate_calculation DROP CONSTRAINT fk559c0bfec368d6ac;
       public       postgres    false    431    419    4523            ?           2606    28246 '   tax_rate_calculation fk559c0bfed0f6101a    FK CONSTRAINT     ?   ALTER TABLE ONLY public.tax_rate_calculation
    ADD CONSTRAINT fk559c0bfed0f6101a FOREIGN KEY (tax_return) REFERENCES public.tax_return(id) DEFERRABLE INITIALLY DEFERRED;
 Q   ALTER TABLE ONLY public.tax_rate_calculation DROP CONSTRAINT fk559c0bfed0f6101a;
       public       postgres    false    431    432    4541            ?           2606    28236 '   tax_rate_calculation fk559c0bfefa01503a    FK CONSTRAINT     ?   ALTER TABLE ONLY public.tax_rate_calculation
    ADD CONSTRAINT fk559c0bfefa01503a FOREIGN KEY (sales_liability_account_id) REFERENCES public.account(id) DEFERRABLE INITIALLY DEFERRED;
 Q   ALTER TABLE ONLY public.tax_rate_calculation DROP CONSTRAINT fk559c0bfefa01503a;
       public       postgres    false    431    197    4243            ?           2606    26851    depreciation fk59f2a58d33118956    FK CONSTRAINT     ?   ALTER TABLE ONLY public.depreciation
    ADD CONSTRAINT fk59f2a58d33118956 FOREIGN KEY (fixed_asset_id) REFERENCES public.fixed_asset(id) DEFERRABLE INITIALLY DEFERRED;
 I   ALTER TABLE ONLY public.depreciation DROP CONSTRAINT fk59f2a58d33118956;
       public       postgres    false    296    273    4369            ?           2606    26846    depreciation fk59f2a58d622c1275    FK CONSTRAINT     ?   ALTER TABLE ONLY public.depreciation
    ADD CONSTRAINT fk59f2a58d622c1275 FOREIGN KEY (company_id) REFERENCES public.company(id) DEFERRABLE INITIALLY DEFERRED;
 I   ALTER TABLE ONLY public.depreciation DROP CONSTRAINT fk59f2a58d622c1275;
       public       postgres    false    4309    250    273            %           2606    27541 %   nominal_code_range fk5cb657ae622c1275    FK CONSTRAINT     ?   ALTER TABLE ONLY public.nominal_code_range
    ADD CONSTRAINT fk5cb657ae622c1275 FOREIGN KEY (company_id) REFERENCES public.company(id) DEFERRABLE INITIALLY DEFERRED;
 O   ALTER TABLE ONLY public.nominal_code_range DROP CONSTRAINT fk5cb657ae622c1275;
       public       postgres    false    4309    351    250            ?           2606    28041 #   statement_record fk6214dd41622c1275    FK CONSTRAINT     ?   ALTER TABLE ONLY public.statement_record
    ADD CONSTRAINT fk6214dd41622c1275 FOREIGN KEY (company_id) REFERENCES public.company(id) DEFERRABLE INITIALLY DEFERRED;
 M   ALTER TABLE ONLY public.statement_record DROP CONSTRAINT fk6214dd41622c1275;
       public       postgres    false    412    250    4309            ?           2606    28046 #   statement_record fk6214dd41e9e68a75    FK CONSTRAINT     ?   ALTER TABLE ONLY public.statement_record
    ADD CONSTRAINT fk6214dd41e9e68a75 FOREIGN KEY (statement_id) REFERENCES public.statement(id) DEFERRABLE INITIALLY DEFERRED;
 M   ALTER TABLE ONLY public.statement_record DROP CONSTRAINT fk6214dd41e9e68a75;
       public       postgres    false    412    410    4511            ?           2606    26921 +   employee_payment_details fk627dad38517e624e    FK CONSTRAINT     ?   ALTER TABLE ONLY public.employee_payment_details
    ADD CONSTRAINT fk627dad38517e624e FOREIGN KEY (pay_run_id) REFERENCES public.pay_run(id) DEFERRABLE INITIALLY DEFERRED;
 U   ALTER TABLE ONLY public.employee_payment_details DROP CONSTRAINT fk627dad38517e624e;
       public       postgres    false    286    372    4465            ?           2606    26926 +   employee_payment_details fk627dad38b3a43ae1    FK CONSTRAINT     ?   ALTER TABLE ONLY public.employee_payment_details
    ADD CONSTRAINT fk627dad38b3a43ae1 FOREIGN KEY (employee) REFERENCES public.employee(id) DEFERRABLE INITIALLY DEFERRED;
 U   ALTER TABLE ONLY public.employee_payment_details DROP CONSTRAINT fk627dad38b3a43ae1;
       public       postgres    false    4349    286    280                        2606    27516    members fk635a54f9d611fcad    FK CONSTRAINT     ?   ALTER TABLE ONLY public.members
    ADD CONSTRAINT fk635a54f9d611fcad FOREIGN KEY (member_id) REFERENCES public.client_subscription(id) DEFERRABLE INITIALLY DEFERRED;
 D   ALTER TABLE ONLY public.members DROP CONSTRAINT fk635a54f9d611fcad;
       public       postgres    false    246    342    4305            ]           2606    26541    company fk6372c85d1ae1fdad    FK CONSTRAINT     ?   ALTER TABLE ONLY public.company
    ADD CONSTRAINT fk6372c85d1ae1fdad FOREIGN KEY (accounts_round_id) REFERENCES public.account(id) DEFERRABLE INITIALLY DEFERRED;
 D   ALTER TABLE ONLY public.company DROP CONSTRAINT fk6372c85d1ae1fdad;
       public       postgres    false    250    197    4243            k           2606    26611    company fk6372c85d2cbf1a7c    FK CONSTRAINT     ?   ALTER TABLE ONLY public.company
    ADD CONSTRAINT fk6372c85d2cbf1a7c FOREIGN KEY (other_cash_income_id) REFERENCES public.account(id) DEFERRABLE INITIALLY DEFERRED;
 D   ALTER TABLE ONLY public.company DROP CONSTRAINT fk6372c85d2cbf1a7c;
       public       postgres    false    4243    250    197            d           2606    26576    company fk6372c85d32175d8d    FK CONSTRAINT     ?   ALTER TABLE ONLY public.company
    ADD CONSTRAINT fk6372c85d32175d8d FOREIGN KEY (accounts_receivable_id) REFERENCES public.account(id) DEFERRABLE INITIALLY DEFERRED;
 D   ALTER TABLE ONLY public.company DROP CONSTRAINT fk6372c85d32175d8d;
       public       postgres    false    197    250    4243            b           2606    26566    company fk6372c85d3811344    FK CONSTRAINT     ?   ALTER TABLE ONLY public.company
    ADD CONSTRAINT fk6372c85d3811344 FOREIGN KEY (primary_currency) REFERENCES public.currency(id) DEFERRABLE INITIALLY DEFERRED;
 C   ALTER TABLE ONLY public.company DROP CONSTRAINT fk6372c85d3811344;
       public       postgres    false    250    260    4323            n           2606    26626    company fk6372c85d4cdf6ea1    FK CONSTRAINT     ?   ALTER TABLE ONLY public.company
    ADD CONSTRAINT fk6372c85d4cdf6ea1 FOREIGN KEY (salaries_payable_account) REFERENCES public.account(id) DEFERRABLE INITIALLY DEFERRED;
 D   ALTER TABLE ONLY public.company DROP CONSTRAINT fk6372c85d4cdf6ea1;
       public       postgres    false    250    4243    197            ^           2606    26546    company fk6372c85d61f600c3    FK CONSTRAINT     ?   ALTER TABLE ONLY public.company
    ADD CONSTRAINT fk6372c85d61f600c3 FOREIGN KEY (cash_discount_account) REFERENCES public.account(id) DEFERRABLE INITIALLY DEFERRED;
 D   ALTER TABLE ONLY public.company DROP CONSTRAINT fk6372c85d61f600c3;
       public       postgres    false    250    197    4243            [           2606    26531    company fk6372c85d66f682fb    FK CONSTRAINT     ?   ALTER TABLE ONLY public.company
    ADD CONSTRAINT fk6372c85d66f682fb FOREIGN KEY (tax_liability_account_id) REFERENCES public.account(id) DEFERRABLE INITIALLY DEFERRED;
 D   ALTER TABLE ONLY public.company DROP CONSTRAINT fk6372c85d66f682fb;
       public       postgres    false    250    197    4243            f           2606    26586    company fk6372c85d7c4dcf34    FK CONSTRAINT     ?   ALTER TABLE ONLY public.company
    ADD CONSTRAINT fk6372c85d7c4dcf34 FOREIGN KEY (vat_filed_liability_account_id) REFERENCES public.account(id) DEFERRABLE INITIALLY DEFERRED;
 D   ALTER TABLE ONLY public.company DROP CONSTRAINT fk6372c85d7c4dcf34;
       public       postgres    false    250    197    4243            \           2606    26536    company fk6372c85d81b950ef    FK CONSTRAINT     ?   ALTER TABLE ONLY public.company
    ADD CONSTRAINT fk6372c85d81b950ef FOREIGN KEY (tds_deductor) REFERENCES public.tdsdeductormasters(id) DEFERRABLE INITIALLY DEFERRED;
 D   ALTER TABLE ONLY public.company DROP CONSTRAINT fk6372c85d81b950ef;
       public       postgres    false    250    436    4545            p           2606    26636    company fk6372c85d84106475    FK CONSTRAINT     ?   ALTER TABLE ONLY public.company
    ADD CONSTRAINT fk6372c85d84106475 FOREIGN KEY (pending_item_receipts_account_id) REFERENCES public.account(id) DEFERRABLE INITIALLY DEFERRED;
 D   ALTER TABLE ONLY public.company DROP CONSTRAINT fk6372c85d84106475;
       public       postgres    false    4243    250    197            g           2606    26591    company fk6372c85d87f5f0a7    FK CONSTRAINT     ?   ALTER TABLE ONLY public.company
    ADD CONSTRAINT fk6372c85d87f5f0a7 FOREIGN KEY (tds_responsible_person) REFERENCES public.tdsresponsibleperson(id) DEFERRABLE INITIALLY DEFERRED;
 D   ALTER TABLE ONLY public.company DROP CONSTRAINT fk6372c85d87f5f0a7;
       public       postgres    false    438    250    4547            l           2606    26616    company fk6372c85d98c47ad5    FK CONSTRAINT     ?   ALTER TABLE ONLY public.company
    ADD CONSTRAINT fk6372c85d98c47ad5 FOREIGN KEY (default_measurement) REFERENCES public.measurement(id) DEFERRABLE INITIALLY DEFERRED;
 D   ALTER TABLE ONLY public.company DROP CONSTRAINT fk6372c85d98c47ad5;
       public       postgres    false    4425    250    341            o           2606    26631    company fk6372c85d9e66a0e4    FK CONSTRAINT     ?   ALTER TABLE ONLY public.company
    ADD CONSTRAINT fk6372c85d9e66a0e4 FOREIGN KEY (opening_balances_id) REFERENCES public.account(id) DEFERRABLE INITIALLY DEFERRED;
 D   ALTER TABLE ONLY public.company DROP CONSTRAINT fk6372c85d9e66a0e4;
       public       postgres    false    4243    250    197            e           2606    26581    company fk6372c85da26f1d1    FK CONSTRAINT     ?   ALTER TABLE ONLY public.company
    ADD CONSTRAINT fk6372c85da26f1d1 FOREIGN KEY (exchange_loss_or_gain_account) REFERENCES public.account(id) DEFERRABLE INITIALLY DEFERRED;
 C   ALTER TABLE ONLY public.company DROP CONSTRAINT fk6372c85da26f1d1;
       public       postgres    false    250    197    4243            m           2606    26621    company fk6372c85da27c2b4c    FK CONSTRAINT     ?   ALTER TABLE ONLY public.company
    ADD CONSTRAINT fk6372c85da27c2b4c FOREIGN KEY (cash_discounts_given) REFERENCES public.account(id) DEFERRABLE INITIALLY DEFERRED;
 D   ALTER TABLE ONLY public.company DROP CONSTRAINT fk6372c85da27c2b4c;
       public       postgres    false    4243    197    250            q           2606    26641    company fk6372c85da32f9096    FK CONSTRAINT     ?   ALTER TABLE ONLY public.company
    ADD CONSTRAINT fk6372c85da32f9096 FOREIGN KEY (cash_discounts_taken) REFERENCES public.account(id) DEFERRABLE INITIALLY DEFERRED;
 D   ALTER TABLE ONLY public.company DROP CONSTRAINT fk6372c85da32f9096;
       public       postgres    false    250    197    4243            c           2606    26571    company fk6372c85da60d261c    FK CONSTRAINT     ?   ALTER TABLE ONLY public.company
    ADD CONSTRAINT fk6372c85da60d261c FOREIGN KEY (cp_rounding_account) REFERENCES public.account(id) DEFERRABLE INITIALLY DEFERRED;
 D   ALTER TABLE ONLY public.company DROP CONSTRAINT fk6372c85da60d261c;
       public       postgres    false    250    197    4243            Z           2606    26526    company fk6372c85db19cec52    FK CONSTRAINT     ?   ALTER TABLE ONLY public.company
    ADD CONSTRAINT fk6372c85db19cec52 FOREIGN KEY (default_tax_code) REFERENCES public.tax_code(id) DEFERRABLE INITIALLY DEFERRED;
 D   ALTER TABLE ONLY public.company DROP CONSTRAINT fk6372c85db19cec52;
       public       postgres    false    4529    424    250            i           2606    26601    company fk6372c85db1a5391e    FK CONSTRAINT     ?   ALTER TABLE ONLY public.company
    ADD CONSTRAINT fk6372c85db1a5391e FOREIGN KEY (retained_earnings_id) REFERENCES public.account(id) DEFERRABLE INITIALLY DEFERRED;
 D   ALTER TABLE ONLY public.company DROP CONSTRAINT fk6372c85db1a5391e;
       public       postgres    false    250    197    4243            j           2606    26606    company fk6372c85dbf1f769b    FK CONSTRAINT     ?   ALTER TABLE ONLY public.company
    ADD CONSTRAINT fk6372c85dbf1f769b FOREIGN KEY (cost_of_goods_sold) REFERENCES public.account(id) DEFERRABLE INITIALLY DEFERRED;
 D   ALTER TABLE ONLY public.company DROP CONSTRAINT fk6372c85dbf1f769b;
       public       postgres    false    197    250    4243            Y           2606    26521    company fk6372c85dc7b23c7    FK CONSTRAINT     ?   ALTER TABLE ONLY public.company
    ADD CONSTRAINT fk6372c85dc7b23c7 FOREIGN KEY (other_cash_expense_id) REFERENCES public.account(id) DEFERRABLE INITIALLY DEFERRED;
 C   ALTER TABLE ONLY public.company DROP CONSTRAINT fk6372c85dc7b23c7;
       public       postgres    false    250    197    4243            a           2606    26561    company fk6372c85dca4e42a1    FK CONSTRAINT     ?   ALTER TABLE ONLY public.company
    ADD CONSTRAINT fk6372c85dca4e42a1 FOREIGN KEY (cp_default_shipping_term) REFERENCES public.shippingterms(id) DEFERRABLE INITIALLY DEFERRED;
 D   ALTER TABLE ONLY public.company DROP CONSTRAINT fk6372c85dca4e42a1;
       public       postgres    false    250    408    4509            `           2606    26556    company fk6372c85dcf7d8e79    FK CONSTRAINT     ?   ALTER TABLE ONLY public.company
    ADD CONSTRAINT fk6372c85dcf7d8e79 FOREIGN KEY (accounts_payable_id) REFERENCES public.account(id) DEFERRABLE INITIALLY DEFERRED;
 D   ALTER TABLE ONLY public.company DROP CONSTRAINT fk6372c85dcf7d8e79;
       public       postgres    false    250    197    4243            h           2606    26596    company fk6372c85ddb606c23    FK CONSTRAINT     ?   ALTER TABLE ONLY public.company
    ADD CONSTRAINT fk6372c85ddb606c23 FOREIGN KEY (default_warehouse) REFERENCES public.warehouse(id) DEFERRABLE INITIALLY DEFERRED;
 D   ALTER TABLE ONLY public.company DROP CONSTRAINT fk6372c85ddb606c23;
       public       postgres    false    4605    488    250            _           2606    26551    company fk6372c85df1ae8cde    FK CONSTRAINT     ?   ALTER TABLE ONLY public.company
    ADD CONSTRAINT fk6372c85df1ae8cde FOREIGN KEY (created_by) REFERENCES public.users(id) DEFERRABLE INITIALLY DEFERRED;
 D   ALTER TABLE ONLY public.company DROP CONSTRAINT fk6372c85df1ae8cde;
       public       postgres    false    250    474    4585            P           2606    26476     cheque_layout fk646a0f0844bae519    FK CONSTRAINT     ?   ALTER TABLE ONLY public.cheque_layout
    ADD CONSTRAINT fk646a0f0844bae519 FOREIGN KEY (account_id) REFERENCES public.bank_account(id) DEFERRABLE INITIALLY DEFERRED;
 J   ALTER TABLE ONLY public.cheque_layout DROP CONSTRAINT fk646a0f0844bae519;
       public       postgres    false    225    4275    239            Q           2606    26481     cheque_layout fk646a0f08622c1275    FK CONSTRAINT     ?   ALTER TABLE ONLY public.cheque_layout
    ADD CONSTRAINT fk646a0f08622c1275 FOREIGN KEY (company_id) REFERENCES public.company(id) DEFERRABLE INITIALLY DEFERRED;
 J   ALTER TABLE ONLY public.cheque_layout DROP CONSTRAINT fk646a0f08622c1275;
       public       postgres    false    4309    250    239            S           2606    26491     cheque_layout fk646a0f089e5a0e30    FK CONSTRAINT     ?   ALTER TABLE ONLY public.cheque_layout
    ADD CONSTRAINT fk646a0f089e5a0e30 FOREIGN KEY (last_modifier) REFERENCES public.users(id) DEFERRABLE INITIALLY DEFERRED;
 J   ALTER TABLE ONLY public.cheque_layout DROP CONSTRAINT fk646a0f089e5a0e30;
       public       postgres    false    239    474    4585            R           2606    26486     cheque_layout fk646a0f08f1ae8cde    FK CONSTRAINT     ?   ALTER TABLE ONLY public.cheque_layout
    ADD CONSTRAINT fk646a0f08f1ae8cde FOREIGN KEY (created_by) REFERENCES public.users(id) DEFERRABLE INITIALLY DEFERRED;
 J   ALTER TABLE ONLY public.cheque_layout DROP CONSTRAINT fk646a0f08f1ae8cde;
       public       postgres    false    239    4585    474                       2606    27466    key_messages fk68b8174c3eafb9d3    FK CONSTRAINT     ?   ALTER TABLE ONLY public.key_messages
    ADD CONSTRAINT fk68b8174c3eafb9d3 FOREIGN KEY (key_id) REFERENCES public.key(id) DEFERRABLE INITIALLY DEFERRED;
 I   ALTER TABLE ONLY public.key_messages DROP CONSTRAINT fk68b8174c3eafb9d3;
       public       postgres    false    327    4407    328                       2606    27471    key_messages fk68b8174c81719453    FK CONSTRAINT     ?   ALTER TABLE ONLY public.key_messages
    ADD CONSTRAINT fk68b8174c81719453 FOREIGN KEY (message_id) REFERENCES public.message(id) DEFERRABLE INITIALLY DEFERRED;
 I   ALTER TABLE ONLY public.key_messages DROP CONSTRAINT fk68b8174c81719453;
       public       postgres    false    328    4427    344            ?           2606    28616 -   transaction_receive_payment fk6a470e9408f6290    FK CONSTRAINT     ?   ALTER TABLE ONLY public.transaction_receive_payment
    ADD CONSTRAINT fk6a470e9408f6290 FOREIGN KEY (journal_entry_id) REFERENCES public.journal_entry(id) DEFERRABLE INITIALLY DEFERRED;
 W   ALTER TABLE ONLY public.transaction_receive_payment DROP CONSTRAINT fk6a470e9408f6290;
       public       postgres    false    465    4405    325            ?           2606    28626 -   transaction_receive_payment fk6a470e942d65475    FK CONSTRAINT     ?   ALTER TABLE ONLY public.transaction_receive_payment
    ADD CONSTRAINT fk6a470e942d65475 FOREIGN KEY (invoice_id) REFERENCES public.invoice(id) DEFERRABLE INITIALLY DEFERRED;
 W   ALTER TABLE ONLY public.transaction_receive_payment DROP CONSTRAINT fk6a470e942d65475;
       public       postgres    false    4389    465    312            ?           2606    28606 ,   transaction_receive_payment fk6a470e962aa184    FK CONSTRAINT     ?   ALTER TABLE ONLY public.transaction_receive_payment
    ADD CONSTRAINT fk6a470e962aa184 FOREIGN KEY (transaction_id) REFERENCES public.receive_payment(id) DEFERRABLE INITIALLY DEFERRED;
 V   ALTER TABLE ONLY public.transaction_receive_payment DROP CONSTRAINT fk6a470e962aa184;
       public       postgres    false    4485    465    387            ?           2606    28621 -   transaction_receive_payment fk6a470e966e25edc    FK CONSTRAINT     ?   ALTER TABLE ONLY public.transaction_receive_payment
    ADD CONSTRAINT fk6a470e966e25edc FOREIGN KEY (customer_refund_id) REFERENCES public.customer_refund(id) DEFERRABLE INITIALLY DEFERRED;
 W   ALTER TABLE ONLY public.transaction_receive_payment DROP CONSTRAINT fk6a470e966e25edc;
       public       postgres    false    465    266    4333            ?           2606    28611 -   transaction_receive_payment fk6a470e9b8e9ce53    FK CONSTRAINT     ?   ALTER TABLE ONLY public.transaction_receive_payment
    ADD CONSTRAINT fk6a470e9b8e9ce53 FOREIGN KEY (write_off_id) REFERENCES public.account(id) DEFERRABLE INITIALLY DEFERRED;
 W   ALTER TABLE ONLY public.transaction_receive_payment DROP CONSTRAINT fk6a470e9b8e9ce53;
       public       postgres    false    465    197    4243            ?           2606    28631 -   transaction_receive_payment fk6a470e9f5e540e1    FK CONSTRAINT     ?   ALTER TABLE ONLY public.transaction_receive_payment
    ADD CONSTRAINT fk6a470e9f5e540e1 FOREIGN KEY (discount_id) REFERENCES public.account(id) DEFERRABLE INITIALLY DEFERRED;
 W   ALTER TABLE ONLY public.transaction_receive_payment DROP CONSTRAINT fk6a470e9f5e540e1;
       public       postgres    false    197    465    4243            <           2606    46515 (   user_defined_pay_head fk6a916ac12d16b99e    FK CONSTRAINT     ?   ALTER TABLE ONLY public.user_defined_pay_head
    ADD CONSTRAINT fk6a916ac12d16b99e FOREIGN KEY (id) REFERENCES public.pay_head(id) DEFERRABLE INITIALLY DEFERRED;
 R   ALTER TABLE ONLY public.user_defined_pay_head DROP CONSTRAINT fk6a916ac12d16b99e;
       public       postgres    false    506    4461    369            9           2606    27641 %   paypal_transaction fk6c164372622c1275    FK CONSTRAINT     ?   ALTER TABLE ONLY public.paypal_transaction
    ADD CONSTRAINT fk6c164372622c1275 FOREIGN KEY (company_id) REFERENCES public.company(id) DEFERRABLE INITIALLY DEFERRED;
 O   ALTER TABLE ONLY public.paypal_transaction DROP CONSTRAINT fk6c164372622c1275;
       public       postgres    false    4309    364    250            6           2606    27626    paymentterms fk6da15341622c1275    FK CONSTRAINT     ?   ALTER TABLE ONLY public.paymentterms
    ADD CONSTRAINT fk6da15341622c1275 FOREIGN KEY (company_id) REFERENCES public.company(id) DEFERRABLE INITIALLY DEFERRED;
 I   ALTER TABLE ONLY public.paymentterms DROP CONSTRAINT fk6da15341622c1275;
       public       postgres    false    4309    362    250            8           2606    27636    paymentterms fk6da153419e5a0e30    FK CONSTRAINT     ?   ALTER TABLE ONLY public.paymentterms
    ADD CONSTRAINT fk6da153419e5a0e30 FOREIGN KEY (last_modifier) REFERENCES public.users(id) DEFERRABLE INITIALLY DEFERRED;
 I   ALTER TABLE ONLY public.paymentterms DROP CONSTRAINT fk6da153419e5a0e30;
       public       postgres    false    362    4585    474            7           2606    27631    paymentterms fk6da15341f1ae8cde    FK CONSTRAINT     ?   ALTER TABLE ONLY public.paymentterms
    ADD CONSTRAINT fk6da15341f1ae8cde FOREIGN KEY (created_by) REFERENCES public.users(id) DEFERRABLE INITIALLY DEFERRED;
 I   ALTER TABLE ONLY public.paymentterms DROP CONSTRAINT fk6da15341f1ae8cde;
       public       postgres    false    4585    474    362            ?           2606    26876 !   email_template fk7295c5dd622c1275    FK CONSTRAINT     ?   ALTER TABLE ONLY public.email_template
    ADD CONSTRAINT fk7295c5dd622c1275 FOREIGN KEY (company_id) REFERENCES public.company(id) DEFERRABLE INITIALLY DEFERRED;
 K   ALTER TABLE ONLY public.email_template DROP CONSTRAINT fk7295c5dd622c1275;
       public       postgres    false    4309    250    279                       2606    27376    item_receipt fk74f154ec274bc854    FK CONSTRAINT     ?   ALTER TABLE ONLY public.item_receipt
    ADD CONSTRAINT fk74f154ec274bc854 FOREIGN KEY (id) REFERENCES public.transaction(id) DEFERRABLE INITIALLY DEFERRED;
 I   ALTER TABLE ONLY public.item_receipt DROP CONSTRAINT fk74f154ec274bc854;
       public       postgres    false    4553    443    318                       2606    27391    item_receipt fk74f154ec338bd6bb    FK CONSTRAINT     ?   ALTER TABLE ONLY public.item_receipt
    ADD CONSTRAINT fk74f154ec338bd6bb FOREIGN KEY (payment_term_id) REFERENCES public.paymentterms(id) DEFERRABLE INITIALLY DEFERRED;
 I   ALTER TABLE ONLY public.item_receipt DROP CONSTRAINT fk74f154ec338bd6bb;
       public       postgres    false    362    318    4451                       2606    27371    item_receipt fk74f154ec39750592    FK CONSTRAINT     ?   ALTER TABLE ONLY public.item_receipt
    ADD CONSTRAINT fk74f154ec39750592 FOREIGN KEY (purchase_order_id) REFERENCES public.purchase_order(id) DEFERRABLE INITIALLY DEFERRED;
 I   ALTER TABLE ONLY public.item_receipt DROP CONSTRAINT fk74f154ec39750592;
       public       postgres    false    318    386    4483                       2606    27386    item_receipt fk74f154ec777205a4    FK CONSTRAINT     ?   ALTER TABLE ONLY public.item_receipt
    ADD CONSTRAINT fk74f154ec777205a4 FOREIGN KEY (ship_to_id) REFERENCES public.account(id) DEFERRABLE INITIALLY DEFERRED;
 I   ALTER TABLE ONLY public.item_receipt DROP CONSTRAINT fk74f154ec777205a4;
       public       postgres    false    4243    318    197                       2606    27366    item_receipt fk74f154ec891a177f    FK CONSTRAINT     ?   ALTER TABLE ONLY public.item_receipt
    ADD CONSTRAINT fk74f154ec891a177f FOREIGN KEY (vendor_id) REFERENCES public.vendor(id) DEFERRABLE INITIALLY DEFERRED;
 I   ALTER TABLE ONLY public.item_receipt DROP CONSTRAINT fk74f154ec891a177f;
       public       postgres    false    4595    318    480                       2606    27381    item_receipt fk74f154ec97edd458    FK CONSTRAINT     ?   ALTER TABLE ONLY public.item_receipt
    ADD CONSTRAINT fk74f154ec97edd458 FOREIGN KEY (shipping_terms_id) REFERENCES public.shippingterms(id) DEFERRABLE INITIALLY DEFERRED;
 I   ALTER TABLE ONLY public.item_receipt DROP CONSTRAINT fk74f154ec97edd458;
       public       postgres    false    318    408    4509                       2606    27361    item_receipt fk74f154ecad0a95dc    FK CONSTRAINT     ?   ALTER TABLE ONLY public.item_receipt
    ADD CONSTRAINT fk74f154ecad0a95dc FOREIGN KEY (shipping_method_id) REFERENCES public.shippingmethod(id) DEFERRABLE INITIALLY DEFERRED;
 I   ALTER TABLE ONLY public.item_receipt DROP CONSTRAINT fk74f154ecad0a95dc;
       public       postgres    false    4507    318    406            9           2606    26361    budget fk756da345622c1275    FK CONSTRAINT     ?   ALTER TABLE ONLY public.budget
    ADD CONSTRAINT fk756da345622c1275 FOREIGN KEY (company_id) REFERENCES public.company(id) DEFERRABLE INITIALLY DEFERRED;
 C   ALTER TABLE ONLY public.budget DROP CONSTRAINT fk756da345622c1275;
       public       postgres    false    229    4309    250            ?           2606    26886    employee fk75c8d6ae49b05fba    FK CONSTRAINT     ?   ALTER TABLE ONLY public.employee
    ADD CONSTRAINT fk75c8d6ae49b05fba FOREIGN KEY (employee_group) REFERENCES public.employee_group(id) DEFERRABLE INITIALLY DEFERRED;
 E   ALTER TABLE ONLY public.employee DROP CONSTRAINT fk75c8d6ae49b05fba;
       public       postgres    false    280    4351    282            ?           2606    26891    employee fk75c8d6ae610348fe    FK CONSTRAINT     ?   ALTER TABLE ONLY public.employee
    ADD CONSTRAINT fk75c8d6ae610348fe FOREIGN KEY (id) REFERENCES public.payee(id) DEFERRABLE INITIALLY DEFERRED;
 E   ALTER TABLE ONLY public.employee DROP CONSTRAINT fk75c8d6ae610348fe;
       public       postgres    false    353    4439    280            ?           2606    26881    employee fk75c8d6ae622c1275    FK CONSTRAINT     ?   ALTER TABLE ONLY public.employee
    ADD CONSTRAINT fk75c8d6ae622c1275 FOREIGN KEY (company_id) REFERENCES public.company(id) DEFERRABLE INITIALLY DEFERRED;
 E   ALTER TABLE ONLY public.employee DROP CONSTRAINT fk75c8d6ae622c1275;
       public       postgres    false    280    250    4309            U           2606    26501    client fk76a5e7cb77a14a7c    FK CONSTRAINT     ?   ALTER TABLE ONLY public.client
    ADD CONSTRAINT fk76a5e7cb77a14a7c FOREIGN KEY (license_purchase) REFERENCES public.license_purchase(id) DEFERRABLE INITIALLY DEFERRED;
 C   ALTER TABLE ONLY public.client DROP CONSTRAINT fk76a5e7cb77a14a7c;
       public       postgres    false    4415    333    241            T           2606    26496    client fk76a5e7cb9c75e4de    FK CONSTRAINT     ?   ALTER TABLE ONLY public.client
    ADD CONSTRAINT fk76a5e7cb9c75e4de FOREIGN KEY (client_subscription) REFERENCES public.client_subscription(id) DEFERRABLE INITIALLY DEFERRED;
 C   ALTER TABLE ONLY public.client DROP CONSTRAINT fk76a5e7cb9c75e4de;
       public       postgres    false    4305    241    246                       2606    28786 !   vendor_payment fk77bf35ef19efdc30    FK CONSTRAINT     ?   ALTER TABLE ONLY public.vendor_payment
    ADD CONSTRAINT fk77bf35ef19efdc30 FOREIGN KEY (payfrom_id) REFERENCES public.account(id) DEFERRABLE INITIALLY DEFERRED;
 K   ALTER TABLE ONLY public.vendor_payment DROP CONSTRAINT fk77bf35ef19efdc30;
       public       postgres    false    484    197    4243                       2606    28791 !   vendor_payment fk77bf35ef274bc854    FK CONSTRAINT     ?   ALTER TABLE ONLY public.vendor_payment
    ADD CONSTRAINT fk77bf35ef274bc854 FOREIGN KEY (id) REFERENCES public.transaction(id) DEFERRABLE INITIALLY DEFERRED;
 K   ALTER TABLE ONLY public.vendor_payment DROP CONSTRAINT fk77bf35ef274bc854;
       public       postgres    false    484    443    4553                       2606    28776 !   vendor_payment fk77bf35ef78884c3b    FK CONSTRAINT     ?   ALTER TABLE ONLY public.vendor_payment
    ADD CONSTRAINT fk77bf35ef78884c3b FOREIGN KEY (tds_taxitem) REFERENCES public.tax_item(id) DEFERRABLE INITIALLY DEFERRED;
 K   ALTER TABLE ONLY public.vendor_payment DROP CONSTRAINT fk77bf35ef78884c3b;
       public       postgres    false    4535    484    427                       2606    28781 !   vendor_payment fk77bf35ef891a177f    FK CONSTRAINT     ?   ALTER TABLE ONLY public.vendor_payment
    ADD CONSTRAINT fk77bf35ef891a177f FOREIGN KEY (vendor_id) REFERENCES public.vendor(id) DEFERRABLE INITIALLY DEFERRED;
 K   ALTER TABLE ONLY public.vendor_payment DROP CONSTRAINT fk77bf35ef891a177f;
       public       postgres    false    4595    484    480            ?           2606    28306 '   tdsresponsibleperson fk787e1028622c1275    FK CONSTRAINT     ?   ALTER TABLE ONLY public.tdsresponsibleperson
    ADD CONSTRAINT fk787e1028622c1275 FOREIGN KEY (company_id) REFERENCES public.company(id) DEFERRABLE INITIALLY DEFERRED;
 Q   ALTER TABLE ONLY public.tdsresponsibleperson DROP CONSTRAINT fk787e1028622c1275;
       public       postgres    false    438    250    4309            u           2606    26661    creditrating fk792265d6622c1275    FK CONSTRAINT     ?   ALTER TABLE ONLY public.creditrating
    ADD CONSTRAINT fk792265d6622c1275 FOREIGN KEY (company_id) REFERENCES public.company(id) DEFERRABLE INITIALLY DEFERRED;
 I   ALTER TABLE ONLY public.creditrating DROP CONSTRAINT fk792265d6622c1275;
       public       postgres    false    4309    255    250            w           2606    26671    creditrating fk792265d69e5a0e30    FK CONSTRAINT     ?   ALTER TABLE ONLY public.creditrating
    ADD CONSTRAINT fk792265d69e5a0e30 FOREIGN KEY (last_modifier) REFERENCES public.users(id) DEFERRABLE INITIALLY DEFERRED;
 I   ALTER TABLE ONLY public.creditrating DROP CONSTRAINT fk792265d69e5a0e30;
       public       postgres    false    4585    255    474            v           2606    26666    creditrating fk792265d6f1ae8cde    FK CONSTRAINT     ?   ALTER TABLE ONLY public.creditrating
    ADD CONSTRAINT fk792265d6f1ae8cde FOREIGN KEY (created_by) REFERENCES public.users(id) DEFERRABLE INITIALLY DEFERRED;
 I   ALTER TABLE ONLY public.creditrating DROP CONSTRAINT fk792265d6f1ae8cde;
       public       postgres    false    4585    474    255            +           2606    26291 1   attendance_or_production_items fk7f27c920411c3a74    FK CONSTRAINT     ?   ALTER TABLE ONLY public.attendance_or_production_items
    ADD CONSTRAINT fk7f27c920411c3a74 FOREIGN KEY (attendance_type) REFERENCES public.attendance_or_production_tpe(id) DEFERRABLE INITIALLY DEFERRED;
 [   ALTER TABLE ONLY public.attendance_or_production_items DROP CONSTRAINT fk7f27c920411c3a74;
       public       postgres    false    221    219    4269            *           2606    26286 1   attendance_or_production_items fk7f27c920ebfa5ba5    FK CONSTRAINT     ?   ALTER TABLE ONLY public.attendance_or_production_items
    ADD CONSTRAINT fk7f27c920ebfa5ba5 FOREIGN KEY (attendance_management_item_id) REFERENCES public.attendance_management_item(id) DEFERRABLE INITIALLY DEFERRED;
 [   ALTER TABLE ONLY public.attendance_or_production_items DROP CONSTRAINT fk7f27c920ebfa5ba5;
       public       postgres    false    219    4265    218            -           2606    28866 )   write_checks_estimates fk7f2e89d74748f1bd    FK CONSTRAINT     ?   ALTER TABLE ONLY public.write_checks_estimates
    ADD CONSTRAINT fk7f2e89d74748f1bd FOREIGN KEY (write_checks_id) REFERENCES public.write_checks(id) DEFERRABLE INITIALLY DEFERRED;
 S   ALTER TABLE ONLY public.write_checks_estimates DROP CONSTRAINT fk7f2e89d74748f1bd;
       public       postgres    false    4609    490    491            .           2606    28871 )   write_checks_estimates fk7f2e89d7b0901b5a    FK CONSTRAINT     ?   ALTER TABLE ONLY public.write_checks_estimates
    ADD CONSTRAINT fk7f2e89d7b0901b5a FOREIGN KEY (elt) REFERENCES public.estimate(id) DEFERRABLE INITIALLY DEFERRED;
 S   ALTER TABLE ONLY public.write_checks_estimates DROP CONSTRAINT fk7f2e89d7b0901b5a;
       public       postgres    false    491    4363    290            ?           2606    27996 !   shippingmethod fk8128180f622c1275    FK CONSTRAINT     ?   ALTER TABLE ONLY public.shippingmethod
    ADD CONSTRAINT fk8128180f622c1275 FOREIGN KEY (company_id) REFERENCES public.company(id) DEFERRABLE INITIALLY DEFERRED;
 K   ALTER TABLE ONLY public.shippingmethod DROP CONSTRAINT fk8128180f622c1275;
       public       postgres    false    406    250    4309            ?           2606    28006 !   shippingmethod fk8128180f9e5a0e30    FK CONSTRAINT     ?   ALTER TABLE ONLY public.shippingmethod
    ADD CONSTRAINT fk8128180f9e5a0e30 FOREIGN KEY (last_modifier) REFERENCES public.users(id) DEFERRABLE INITIALLY DEFERRED;
 K   ALTER TABLE ONLY public.shippingmethod DROP CONSTRAINT fk8128180f9e5a0e30;
       public       postgres    false    406    474    4585            ?           2606    28001 !   shippingmethod fk8128180ff1ae8cde    FK CONSTRAINT     ?   ALTER TABLE ONLY public.shippingmethod
    ADD CONSTRAINT fk8128180ff1ae8cde FOREIGN KEY (created_by) REFERENCES public.users(id) DEFERRABLE INITIALLY DEFERRED;
 K   ALTER TABLE ONLY public.shippingmethod DROP CONSTRAINT fk8128180ff1ae8cde;
       public       postgres    false    406    474    4585            ,           2606    27576     payee_address fk81bf61bdb2fc5555    FK CONSTRAINT     ?   ALTER TABLE ONLY public.payee_address
    ADD CONSTRAINT fk81bf61bdb2fc5555 FOREIGN KEY (payee_id) REFERENCES public.payee(id) DEFERRABLE INITIALLY DEFERRED;
 J   ALTER TABLE ONLY public.payee_address DROP CONSTRAINT fk81bf61bdb2fc5555;
       public       postgres    false    4439    353    354            <           2606    27656    pay_bill fk820c355e19efdc30    FK CONSTRAINT     ?   ALTER TABLE ONLY public.pay_bill
    ADD CONSTRAINT fk820c355e19efdc30 FOREIGN KEY (payfrom_id) REFERENCES public.account(id) DEFERRABLE INITIALLY DEFERRED;
 E   ALTER TABLE ONLY public.pay_bill DROP CONSTRAINT fk820c355e19efdc30;
       public       postgres    false    197    4243    365            =           2606    27661    pay_bill fk820c355e274bc854    FK CONSTRAINT     ?   ALTER TABLE ONLY public.pay_bill
    ADD CONSTRAINT fk820c355e274bc854 FOREIGN KEY (id) REFERENCES public.transaction(id) DEFERRABLE INITIALLY DEFERRED;
 E   ALTER TABLE ONLY public.pay_bill DROP CONSTRAINT fk820c355e274bc854;
       public       postgres    false    4553    443    365            :           2606    27646    pay_bill fk820c355e78884c3b    FK CONSTRAINT     ?   ALTER TABLE ONLY public.pay_bill
    ADD CONSTRAINT fk820c355e78884c3b FOREIGN KEY (tds_taxitem) REFERENCES public.tax_item(id) DEFERRABLE INITIALLY DEFERRED;
 E   ALTER TABLE ONLY public.pay_bill DROP CONSTRAINT fk820c355e78884c3b;
       public       postgres    false    4535    427    365            ;           2606    27651    pay_bill fk820c355e891a177f    FK CONSTRAINT     ?   ALTER TABLE ONLY public.pay_bill
    ADD CONSTRAINT fk820c355e891a177f FOREIGN KEY (vendor_id) REFERENCES public.vendor(id) DEFERRABLE INITIALLY DEFERRED;
 E   ALTER TABLE ONLY public.pay_bill DROP CONSTRAINT fk820c355e891a177f;
       public       postgres    false    480    4595    365            F           2606    27706    pay_head fk820edf3755e69d03    FK CONSTRAINT     ?   ALTER TABLE ONLY public.pay_head
    ADD CONSTRAINT fk820edf3755e69d03 FOREIGN KEY (liability_account) REFERENCES public.account(id) DEFERRABLE INITIALLY DEFERRED;
 E   ALTER TABLE ONLY public.pay_head DROP CONSTRAINT fk820edf3755e69d03;
       public       postgres    false    369    197    4243            E           2606    27701    pay_head fk820edf37622c1275    FK CONSTRAINT     ?   ALTER TABLE ONLY public.pay_head
    ADD CONSTRAINT fk820edf37622c1275 FOREIGN KEY (company_id) REFERENCES public.company(id) DEFERRABLE INITIALLY DEFERRED;
 E   ALTER TABLE ONLY public.pay_head DROP CONSTRAINT fk820edf37622c1275;
       public       postgres    false    369    250    4309            I           2606    27721    pay_head fk820edf379e5a0e30    FK CONSTRAINT     ?   ALTER TABLE ONLY public.pay_head
    ADD CONSTRAINT fk820edf379e5a0e30 FOREIGN KEY (last_modifier) REFERENCES public.users(id) DEFERRABLE INITIALLY DEFERRED;
 E   ALTER TABLE ONLY public.pay_head DROP CONSTRAINT fk820edf379e5a0e30;
       public       postgres    false    369    474    4585            D           2606    27696    pay_head fk820edf37a36d03e6    FK CONSTRAINT     ?   ALTER TABLE ONLY public.pay_head
    ADD CONSTRAINT fk820edf37a36d03e6 FOREIGN KEY (asset_account) REFERENCES public.account(id) DEFERRABLE INITIALLY DEFERRED;
 E   ALTER TABLE ONLY public.pay_head DROP CONSTRAINT fk820edf37a36d03e6;
       public       postgres    false    369    197    4243            H           2606    27716    pay_head fk820edf37cfb26235    FK CONSTRAINT     ?   ALTER TABLE ONLY public.pay_head
    ADD CONSTRAINT fk820edf37cfb26235 FOREIGN KEY (account) REFERENCES public.account(id) DEFERRABLE INITIALLY DEFERRED;
 E   ALTER TABLE ONLY public.pay_head DROP CONSTRAINT fk820edf37cfb26235;
       public       postgres    false    369    197    4243            G           2606    27711    pay_head fk820edf37f1ae8cde    FK CONSTRAINT     ?   ALTER TABLE ONLY public.pay_head
    ADD CONSTRAINT fk820edf37f1ae8cde FOREIGN KEY (created_by) REFERENCES public.users(id) DEFERRABLE INITIALLY DEFERRED;
 E   ALTER TABLE ONLY public.pay_head DROP CONSTRAINT fk820edf37f1ae8cde;
       public       postgres    false    369    474    4585            N           2606    26466 #   cash_sale_orders fk8241aad1146d3634    FK CONSTRAINT     ?   ALTER TABLE ONLY public.cash_sale_orders
    ADD CONSTRAINT fk8241aad1146d3634 FOREIGN KEY (cashsale_id) REFERENCES public.cash_sales(id) DEFERRABLE INITIALLY DEFERRED;
 M   ALTER TABLE ONLY public.cash_sale_orders DROP CONSTRAINT fk8241aad1146d3634;
       public       postgres    false    236    4291    237            O           2606    26471 #   cash_sale_orders fk8241aad12843ee3f    FK CONSTRAINT     ?   ALTER TABLE ONLY public.cash_sale_orders
    ADD CONSTRAINT fk8241aad12843ee3f FOREIGN KEY (estimate_id) REFERENCES public.estimate(id) DEFERRABLE INITIALLY DEFERRED;
 M   ALTER TABLE ONLY public.cash_sale_orders DROP CONSTRAINT fk8241aad12843ee3f;
       public       postgres    false    4363    290    237            C           2606    27691    pay_expense fk83382781274bc854    FK CONSTRAINT     ?   ALTER TABLE ONLY public.pay_expense
    ADD CONSTRAINT fk83382781274bc854 FOREIGN KEY (id) REFERENCES public.transaction(id) DEFERRABLE INITIALLY DEFERRED;
 H   ALTER TABLE ONLY public.pay_expense DROP CONSTRAINT fk83382781274bc854;
       public       postgres    false    367    443    4553            B           2606    27686    pay_expense fk83382781e5fcf475    FK CONSTRAINT     ?   ALTER TABLE ONLY public.pay_expense
    ADD CONSTRAINT fk83382781e5fcf475 FOREIGN KEY (account_id) REFERENCES public.account(id) DEFERRABLE INITIALLY DEFERRED;
 H   ALTER TABLE ONLY public.pay_expense DROP CONSTRAINT fk83382781e5fcf475;
       public       postgres    false    197    367    4243            s           2606    26651 '   computation_pay_head fk83b15ecf2d16b99e    FK CONSTRAINT     ?   ALTER TABLE ONLY public.computation_pay_head
    ADD CONSTRAINT fk83b15ecf2d16b99e FOREIGN KEY (id) REFERENCES public.pay_head(id) DEFERRABLE INITIALLY DEFERRED;
 Q   ALTER TABLE ONLY public.computation_pay_head DROP CONSTRAINT fk83b15ecf2d16b99e;
       public       postgres    false    369    252    4461            ?           2606    28096 &   stock_transfer_item fk83bc295e46ae6ef6    FK CONSTRAINT     ?   ALTER TABLE ONLY public.stock_transfer_item
    ADD CONSTRAINT fk83bc295e46ae6ef6 FOREIGN KEY (qty_unit) REFERENCES public.unit(id) DEFERRABLE INITIALLY DEFERRED;
 P   ALTER TABLE ONLY public.stock_transfer_item DROP CONSTRAINT fk83bc295e46ae6ef6;
       public       postgres    false    415    470    4581            ?           2606    28086 &   stock_transfer_item fk83bc295e622c1275    FK CONSTRAINT     ?   ALTER TABLE ONLY public.stock_transfer_item
    ADD CONSTRAINT fk83bc295e622c1275 FOREIGN KEY (company_id) REFERENCES public.company(id) DEFERRABLE INITIALLY DEFERRED;
 P   ALTER TABLE ONLY public.stock_transfer_item DROP CONSTRAINT fk83bc295e622c1275;
       public       postgres    false    415    250    4309            ?           2606    28091 &   stock_transfer_item fk83bc295e778bf4a3    FK CONSTRAINT     ?   ALTER TABLE ONLY public.stock_transfer_item
    ADD CONSTRAINT fk83bc295e778bf4a3 FOREIGN KEY (warehouse_transfer) REFERENCES public.warehouse_transfer(id) DEFERRABLE INITIALLY DEFERRED;
 P   ALTER TABLE ONLY public.stock_transfer_item DROP CONSTRAINT fk83bc295e778bf4a3;
       public       postgres    false    415    489    4607            ?           2606    28081 &   stock_transfer_item fk83bc295ea036ee2b    FK CONSTRAINT     ?   ALTER TABLE ONLY public.stock_transfer_item
    ADD CONSTRAINT fk83bc295ea036ee2b FOREIGN KEY (item) REFERENCES public.item(id) DEFERRABLE INITIALLY DEFERRED;
 P   ALTER TABLE ONLY public.stock_transfer_item DROP CONSTRAINT fk83bc295ea036ee2b;
       public       postgres    false    415    315    4393            ?           2606    28446 &   transaction_expense fk8448c3b71e282cdf    FK CONSTRAINT     ?   ALTER TABLE ONLY public.transaction_expense
    ADD CONSTRAINT fk8448c3b71e282cdf FOREIGN KEY (item_id) REFERENCES public.item(id) DEFERRABLE INITIALLY DEFERRED;
 P   ALTER TABLE ONLY public.transaction_expense DROP CONSTRAINT fk8448c3b71e282cdf;
       public       postgres    false    315    449    4393            ?           2606    28451 %   transaction_expense fk8448c3b729fc095    FK CONSTRAINT     ?   ALTER TABLE ONLY public.transaction_expense
    ADD CONSTRAINT fk8448c3b729fc095 FOREIGN KEY (expense_id) REFERENCES public.expense(id) DEFERRABLE INITIALLY DEFERRED;
 O   ALTER TABLE ONLY public.transaction_expense DROP CONSTRAINT fk8448c3b729fc095;
       public       postgres    false    291    449    4365            ?           2606    28441 &   transaction_expense fk8448c3b7e5fcf475    FK CONSTRAINT     ?   ALTER TABLE ONLY public.transaction_expense
    ADD CONSTRAINT fk8448c3b7e5fcf475 FOREIGN KEY (account_id) REFERENCES public.account(id) DEFERRABLE INITIALLY DEFERRED;
 P   ALTER TABLE ONLY public.transaction_expense DROP CONSTRAINT fk8448c3b7e5fcf475;
       public       postgres    false    197    449    4243            ?           2606    26861     email_account fk8641d44a622c1275    FK CONSTRAINT     ?   ALTER TABLE ONLY public.email_account
    ADD CONSTRAINT fk8641d44a622c1275 FOREIGN KEY (company_id) REFERENCES public.company(id) DEFERRABLE INITIALLY DEFERRED;
 J   ALTER TABLE ONLY public.email_account DROP CONSTRAINT fk8641d44a622c1275;
       public       postgres    false    4309    277    250            ?           2606    26871     email_account fk8641d44a9e5a0e30    FK CONSTRAINT     ?   ALTER TABLE ONLY public.email_account
    ADD CONSTRAINT fk8641d44a9e5a0e30 FOREIGN KEY (last_modifier) REFERENCES public.users(id) DEFERRABLE INITIALLY DEFERRED;
 J   ALTER TABLE ONLY public.email_account DROP CONSTRAINT fk8641d44a9e5a0e30;
       public       postgres    false    4585    277    474            ?           2606    26866     email_account fk8641d44af1ae8cde    FK CONSTRAINT     ?   ALTER TABLE ONLY public.email_account
    ADD CONSTRAINT fk8641d44af1ae8cde FOREIGN KEY (created_by) REFERENCES public.users(id) DEFERRABLE INITIALLY DEFERRED;
 J   ALTER TABLE ONLY public.email_account DROP CONSTRAINT fk8641d44af1ae8cde;
       public       postgres    false    277    474    4585            {           2606    26691 %   credit_card_charges fk87c7176274bc854    FK CONSTRAINT     ?   ALTER TABLE ONLY public.credit_card_charges
    ADD CONSTRAINT fk87c7176274bc854 FOREIGN KEY (id) REFERENCES public.transaction(id) DEFERRABLE INITIALLY DEFERRED;
 O   ALTER TABLE ONLY public.credit_card_charges DROP CONSTRAINT fk87c7176274bc854;
       public       postgres    false    258    4553    443            |           2606    26696 %   credit_card_charges fk87c717668a08e82    FK CONSTRAINT     ?   ALTER TABLE ONLY public.credit_card_charges
    ADD CONSTRAINT fk87c717668a08e82 FOREIGN KEY (payfrom_account_id) REFERENCES public.account(id) DEFERRABLE INITIALLY DEFERRED;
 O   ALTER TABLE ONLY public.credit_card_charges DROP CONSTRAINT fk87c717668a08e82;
       public       postgres    false    4243    258    197            z           2606    26686 %   credit_card_charges fk87c7176891a177f    FK CONSTRAINT     ?   ALTER TABLE ONLY public.credit_card_charges
    ADD CONSTRAINT fk87c7176891a177f FOREIGN KEY (vendor_id) REFERENCES public.vendor(id) DEFERRABLE INITIALLY DEFERRED;
 O   ALTER TABLE ONLY public.credit_card_charges DROP CONSTRAINT fk87c7176891a177f;
       public       postgres    false    258    4595    480            2           2606    26326 %   attendance_payhead fk8911f3122d16b99e    FK CONSTRAINT     ?   ALTER TABLE ONLY public.attendance_payhead
    ADD CONSTRAINT fk8911f3122d16b99e FOREIGN KEY (id) REFERENCES public.pay_head(id) DEFERRABLE INITIALLY DEFERRED;
 O   ALTER TABLE ONLY public.attendance_payhead DROP CONSTRAINT fk8911f3122d16b99e;
       public       postgres    false    4461    369    222            0           2606    26316 %   attendance_payhead fk8911f3127ebcab9a    FK CONSTRAINT     ?   ALTER TABLE ONLY public.attendance_payhead
    ADD CONSTRAINT fk8911f3127ebcab9a FOREIGN KEY (pay_head) REFERENCES public.pay_head(id) DEFERRABLE INITIALLY DEFERRED;
 O   ALTER TABLE ONLY public.attendance_payhead DROP CONSTRAINT fk8911f3127ebcab9a;
       public       postgres    false    4461    222    369            1           2606    26321 %   attendance_payhead fk8911f312b2c74ea4    FK CONSTRAINT     ?   ALTER TABLE ONLY public.attendance_payhead
    ADD CONSTRAINT fk8911f312b2c74ea4 FOREIGN KEY (production_type) REFERENCES public.attendance_or_production_tpe(id) DEFERRABLE INITIALLY DEFERRED;
 O   ALTER TABLE ONLY public.attendance_payhead DROP CONSTRAINT fk8911f312b2c74ea4;
       public       postgres    false    4269    222    221            ?           2606    26841 $   deposit_estimates fk8acda7aa69721787    FK CONSTRAINT     ?   ALTER TABLE ONLY public.deposit_estimates
    ADD CONSTRAINT fk8acda7aa69721787 FOREIGN KEY (deposit_id) REFERENCES public.make_deposit(id) DEFERRABLE INITIALLY DEFERRED;
 N   ALTER TABLE ONLY public.deposit_estimates DROP CONSTRAINT fk8acda7aa69721787;
       public       postgres    false    339    4423    271            ?           2606    26836 $   deposit_estimates fk8acda7aab0901b5a    FK CONSTRAINT     ?   ALTER TABLE ONLY public.deposit_estimates
    ADD CONSTRAINT fk8acda7aab0901b5a FOREIGN KEY (elt) REFERENCES public.estimate(id) DEFERRABLE INITIALLY DEFERRED;
 N   ALTER TABLE ONLY public.deposit_estimates DROP CONSTRAINT fk8acda7aab0901b5a;
       public       postgres    false    271    4363    290            ?           2606    28521 3   transaction_make_deposit_entries fk905faa3f63880555    FK CONSTRAINT     ?   ALTER TABLE ONLY public.transaction_make_deposit_entries
    ADD CONSTRAINT fk905faa3f63880555 FOREIGN KEY (transaction_id) REFERENCES public.transaction(id) DEFERRABLE INITIALLY DEFERRED;
 ]   ALTER TABLE ONLY public.transaction_make_deposit_entries DROP CONSTRAINT fk905faa3f63880555;
       public       postgres    false    4553    455    443            ?           2606    28526 3   transaction_make_deposit_entries fk905faa3fe5fcf475    FK CONSTRAINT     ?   ALTER TABLE ONLY public.transaction_make_deposit_entries
    ADD CONSTRAINT fk905faa3fe5fcf475 FOREIGN KEY (account_id) REFERENCES public.account(id) DEFERRABLE INITIALLY DEFERRED;
 ]   ALTER TABLE ONLY public.transaction_make_deposit_entries DROP CONSTRAINT fk905faa3fe5fcf475;
       public       postgres    false    4243    455    197            W           2606    27791 %   pay_structure_item fk91e2e3166071234c    FK CONSTRAINT     ?   ALTER TABLE ONLY public.pay_structure_item
    ADD CONSTRAINT fk91e2e3166071234c FOREIGN KEY (pay_structure) REFERENCES public.pay_structure(id) DEFERRABLE INITIALLY DEFERRED;
 O   ALTER TABLE ONLY public.pay_structure_item DROP CONSTRAINT fk91e2e3166071234c;
       public       postgres    false    376    374    4467            V           2606    27786 %   pay_structure_item fk91e2e3167ebcab9a    FK CONSTRAINT     ?   ALTER TABLE ONLY public.pay_structure_item
    ADD CONSTRAINT fk91e2e3167ebcab9a FOREIGN KEY (pay_head) REFERENCES public.pay_head(id) DEFERRABLE INITIALLY DEFERRED;
 O   ALTER TABLE ONLY public.pay_structure_item DROP CONSTRAINT fk91e2e3167ebcab9a;
       public       postgres    false    376    369    4461            R           2606    27766     pay_structure fk9685625c49b05fba    FK CONSTRAINT     ?   ALTER TABLE ONLY public.pay_structure
    ADD CONSTRAINT fk9685625c49b05fba FOREIGN KEY (employee_group) REFERENCES public.employee_group(id) DEFERRABLE INITIALLY DEFERRED;
 J   ALTER TABLE ONLY public.pay_structure DROP CONSTRAINT fk9685625c49b05fba;
       public       postgres    false    374    282    4351            Q           2606    27761     pay_structure fk9685625c622c1275    FK CONSTRAINT     ?   ALTER TABLE ONLY public.pay_structure
    ADD CONSTRAINT fk9685625c622c1275 FOREIGN KEY (company_id) REFERENCES public.company(id) DEFERRABLE INITIALLY DEFERRED;
 J   ALTER TABLE ONLY public.pay_structure DROP CONSTRAINT fk9685625c622c1275;
       public       postgres    false    374    250    4309            U           2606    27781     pay_structure fk9685625c9e5a0e30    FK CONSTRAINT     ?   ALTER TABLE ONLY public.pay_structure
    ADD CONSTRAINT fk9685625c9e5a0e30 FOREIGN KEY (last_modifier) REFERENCES public.users(id) DEFERRABLE INITIALLY DEFERRED;
 J   ALTER TABLE ONLY public.pay_structure DROP CONSTRAINT fk9685625c9e5a0e30;
       public       postgres    false    374    474    4585            S           2606    27771     pay_structure fk9685625cb3a43ae1    FK CONSTRAINT     ?   ALTER TABLE ONLY public.pay_structure
    ADD CONSTRAINT fk9685625cb3a43ae1 FOREIGN KEY (employee) REFERENCES public.employee(id) DEFERRABLE INITIALLY DEFERRED;
 J   ALTER TABLE ONLY public.pay_structure DROP CONSTRAINT fk9685625cb3a43ae1;
       public       postgres    false    374    4349    280            T           2606    27776     pay_structure fk9685625cf1ae8cde    FK CONSTRAINT     ?   ALTER TABLE ONLY public.pay_structure
    ADD CONSTRAINT fk9685625cf1ae8cde FOREIGN KEY (created_by) REFERENCES public.users(id) DEFERRABLE INITIALLY DEFERRED;
 J   ALTER TABLE ONLY public.pay_structure DROP CONSTRAINT fk9685625cf1ae8cde;
       public       postgres    false    374    474    4585                       2606    28726    vendor fk96b199482aacca64    FK CONSTRAINT     ?   ALTER TABLE ONLY public.vendor
    ADD CONSTRAINT fk96b199482aacca64 FOREIGN KEY (vendor_group_id) REFERENCES public.vendor_group(id) DEFERRABLE INITIALLY DEFERRED;
 C   ALTER TABLE ONLY public.vendor DROP CONSTRAINT fk96b199482aacca64;
       public       postgres    false    483    480    4599                       2606    28741    vendor fk96b19948610348fe    FK CONSTRAINT     ?   ALTER TABLE ONLY public.vendor
    ADD CONSTRAINT fk96b19948610348fe FOREIGN KEY (id) REFERENCES public.payee(id) DEFERRABLE INITIALLY DEFERRED;
 C   ALTER TABLE ONLY public.vendor DROP CONSTRAINT fk96b19948610348fe;
       public       postgres    false    4439    480    353                       2606    28721    vendor fk96b19948622c1275    FK CONSTRAINT     ?   ALTER TABLE ONLY public.vendor
    ADD CONSTRAINT fk96b19948622c1275 FOREIGN KEY (company_id) REFERENCES public.company(id) DEFERRABLE INITIALLY DEFERRED;
 C   ALTER TABLE ONLY public.vendor DROP CONSTRAINT fk96b19948622c1275;
       public       postgres    false    250    480    4309                       2606    28731    vendor fk96b19948ad0a95dc    FK CONSTRAINT     ?   ALTER TABLE ONLY public.vendor
    ADD CONSTRAINT fk96b19948ad0a95dc FOREIGN KEY (shipping_method_id) REFERENCES public.shippingmethod(id) DEFERRABLE INITIALLY DEFERRED;
 C   ALTER TABLE ONLY public.vendor DROP CONSTRAINT fk96b19948ad0a95dc;
       public       postgres    false    406    480    4507                       2606    28736    vendor fk96b19948aea76b12    FK CONSTRAINT     ?   ALTER TABLE ONLY public.vendor
    ADD CONSTRAINT fk96b19948aea76b12 FOREIGN KEY (payment_terms_id) REFERENCES public.paymentterms(id) DEFERRABLE INITIALLY DEFERRED;
 C   ALTER TABLE ONLY public.vendor DROP CONSTRAINT fk96b19948aea76b12;
       public       postgres    false    362    480    4451                       2606    28746    vendor fk96b19948e2aa5abc    FK CONSTRAINT     ?   ALTER TABLE ONLY public.vendor
    ADD CONSTRAINT fk96b19948e2aa5abc FOREIGN KEY (expense_account_id) REFERENCES public.account(id) DEFERRABLE INITIALLY DEFERRED;
 C   ALTER TABLE ONLY public.vendor DROP CONSTRAINT fk96b19948e2aa5abc;
       public       postgres    false    480    197    4243            ?           2606    27026    fiscal_year fk9722721e622c1275    FK CONSTRAINT     ?   ALTER TABLE ONLY public.fiscal_year
    ADD CONSTRAINT fk9722721e622c1275 FOREIGN KEY (company_id) REFERENCES public.company(id) DEFERRABLE INITIALLY DEFERRED;
 H   ALTER TABLE ONLY public.fiscal_year DROP CONSTRAINT fk9722721e622c1275;
       public       postgres    false    294    4309    250            ?           2606    27036    fiscal_year fk9722721e9e5a0e30    FK CONSTRAINT     ?   ALTER TABLE ONLY public.fiscal_year
    ADD CONSTRAINT fk9722721e9e5a0e30 FOREIGN KEY (last_modifier) REFERENCES public.users(id) DEFERRABLE INITIALLY DEFERRED;
 H   ALTER TABLE ONLY public.fiscal_year DROP CONSTRAINT fk9722721e9e5a0e30;
       public       postgres    false    294    474    4585            ?           2606    27031    fiscal_year fk9722721ef1ae8cde    FK CONSTRAINT     ?   ALTER TABLE ONLY public.fiscal_year
    ADD CONSTRAINT fk9722721ef1ae8cde FOREIGN KEY (created_by) REFERENCES public.users(id) DEFERRABLE INITIALLY DEFERRED;
 H   ALTER TABLE ONLY public.fiscal_year DROP CONSTRAINT fk9722721ef1ae8cde;
       public       postgres    false    4585    294    474                       2606    27421    item_update fk97fab7546ae6ef6    FK CONSTRAINT     ?   ALTER TABLE ONLY public.item_update
    ADD CONSTRAINT fk97fab7546ae6ef6 FOREIGN KEY (qty_unit) REFERENCES public.unit(id) DEFERRABLE INITIALLY DEFERRED;
 G   ALTER TABLE ONLY public.item_update DROP CONSTRAINT fk97fab7546ae6ef6;
       public       postgres    false    470    320    4581            	           2606    27401    item_update fk97fab75622c1275    FK CONSTRAINT     ?   ALTER TABLE ONLY public.item_update
    ADD CONSTRAINT fk97fab75622c1275 FOREIGN KEY (company_id) REFERENCES public.company(id) DEFERRABLE INITIALLY DEFERRED;
 G   ALTER TABLE ONLY public.item_update DROP CONSTRAINT fk97fab75622c1275;
       public       postgres    false    250    320    4309            
           2606    27406    item_update fk97fab759495a8bc    FK CONSTRAINT     ?   ALTER TABLE ONLY public.item_update
    ADD CONSTRAINT fk97fab759495a8bc FOREIGN KEY (ware_house) REFERENCES public.warehouse(id) DEFERRABLE INITIALLY DEFERRED;
 G   ALTER TABLE ONLY public.item_update DROP CONSTRAINT fk97fab759495a8bc;
       public       postgres    false    488    320    4605                       2606    27426    item_update fk97fab759e5a0e30    FK CONSTRAINT     ?   ALTER TABLE ONLY public.item_update
    ADD CONSTRAINT fk97fab759e5a0e30 FOREIGN KEY (last_modifier) REFERENCES public.users(id) DEFERRABLE INITIALLY DEFERRED;
 G   ALTER TABLE ONLY public.item_update DROP CONSTRAINT fk97fab759e5a0e30;
       public       postgres    false    320    474    4585                       2606    27396    item_update fk97fab75a036ee2b    FK CONSTRAINT     ?   ALTER TABLE ONLY public.item_update
    ADD CONSTRAINT fk97fab75a036ee2b FOREIGN KEY (item) REFERENCES public.item(id) DEFERRABLE INITIALLY DEFERRED;
 G   ALTER TABLE ONLY public.item_update DROP CONSTRAINT fk97fab75a036ee2b;
       public       postgres    false    315    320    4393                       2606    27416    item_update fk97fab75a6ec8e17    FK CONSTRAINT     ?   ALTER TABLE ONLY public.item_update
    ADD CONSTRAINT fk97fab75a6ec8e17 FOREIGN KEY (transaction) REFERENCES public.transaction(id) DEFERRABLE INITIALLY DEFERRED;
 G   ALTER TABLE ONLY public.item_update DROP CONSTRAINT fk97fab75a6ec8e17;
       public       postgres    false    443    320    4553                       2606    27411    item_update fk97fab75f1ae8cde    FK CONSTRAINT     ?   ALTER TABLE ONLY public.item_update
    ADD CONSTRAINT fk97fab75f1ae8cde FOREIGN KEY (created_by) REFERENCES public.users(id) DEFERRABLE INITIALLY DEFERRED;
 G   ALTER TABLE ONLY public.item_update DROP CONSTRAINT fk97fab75f1ae8cde;
       public       postgres    false    474    320    4585            ?           2606    28461 %   transaction_history fk993dff3622c1275    FK CONSTRAINT     ?   ALTER TABLE ONLY public.transaction_history
    ADD CONSTRAINT fk993dff3622c1275 FOREIGN KEY (company_id) REFERENCES public.company(id) DEFERRABLE INITIALLY DEFERRED;
 O   ALTER TABLE ONLY public.transaction_history DROP CONSTRAINT fk993dff3622c1275;
       public       postgres    false    250    451    4309            ?           2606    28456 %   transaction_history fk993dff363880555    FK CONSTRAINT     ?   ALTER TABLE ONLY public.transaction_history
    ADD CONSTRAINT fk993dff363880555 FOREIGN KEY (transaction_id) REFERENCES public.transaction(id) DEFERRABLE INITIALLY DEFERRED;
 O   ALTER TABLE ONLY public.transaction_history DROP CONSTRAINT fk993dff363880555;
       public       postgres    false    443    451    4553            E           2606    26421 '   cash_purchase_orders fk9a38eed739750592    FK CONSTRAINT     ?   ALTER TABLE ONLY public.cash_purchase_orders
    ADD CONSTRAINT fk9a38eed739750592 FOREIGN KEY (purchase_order_id) REFERENCES public.purchase_order(id) DEFERRABLE INITIALLY DEFERRED;
 Q   ALTER TABLE ONLY public.cash_purchase_orders DROP CONSTRAINT fk9a38eed739750592;
       public       postgres    false    386    4483    235            F           2606    26426 '   cash_purchase_orders fk9a38eed7baae8a66    FK CONSTRAINT     ?   ALTER TABLE ONLY public.cash_purchase_orders
    ADD CONSTRAINT fk9a38eed7baae8a66 FOREIGN KEY (cash_purchase_id) REFERENCES public.cash_purchase(id) DEFERRABLE INITIALLY DEFERRED;
 Q   ALTER TABLE ONLY public.cash_purchase_orders DROP CONSTRAINT fk9a38eed7baae8a66;
       public       postgres    false    4285    235    233            X           2606    26516 %   client_subscription fk9a5d0511e76239f    FK CONSTRAINT     ?   ALTER TABLE ONLY public.client_subscription
    ADD CONSTRAINT fk9a5d0511e76239f FOREIGN KEY (subscription_id) REFERENCES public.subscription(id) DEFERRABLE INITIALLY DEFERRED;
 O   ALTER TABLE ONLY public.client_subscription DROP CONSTRAINT fk9a5d0511e76239f;
       public       postgres    false    246    417    4519            ?           2606    27046    fixed_asset fk9b07625622c1275    FK CONSTRAINT     ?   ALTER TABLE ONLY public.fixed_asset
    ADD CONSTRAINT fk9b07625622c1275 FOREIGN KEY (company_id) REFERENCES public.company(id) DEFERRABLE INITIALLY DEFERRED;
 G   ALTER TABLE ONLY public.fixed_asset DROP CONSTRAINT fk9b07625622c1275;
       public       postgres    false    4309    296    250            ?           2606    27061    fixed_asset fk9b076259832924    FK CONSTRAINT     ?   ALTER TABLE ONLY public.fixed_asset
    ADD CONSTRAINT fk9b076259832924 FOREIGN KEY (asset_account_id) REFERENCES public.account(id) DEFERRABLE INITIALLY DEFERRED;
 F   ALTER TABLE ONLY public.fixed_asset DROP CONSTRAINT fk9b076259832924;
       public       postgres    false    197    296    4243            ?           2606    27066    fixed_asset fk9b076259bf222c3    FK CONSTRAINT     ?   ALTER TABLE ONLY public.fixed_asset
    ADD CONSTRAINT fk9b076259bf222c3 FOREIGN KEY (loss_or_gain_disposal_account_id) REFERENCES public.account(id) DEFERRABLE INITIALLY DEFERRED;
 G   ALTER TABLE ONLY public.fixed_asset DROP CONSTRAINT fk9b076259bf222c3;
       public       postgres    false    4243    296    197            ?           2606    27086    fixed_asset fk9b076259e5a0e30    FK CONSTRAINT     ?   ALTER TABLE ONLY public.fixed_asset
    ADD CONSTRAINT fk9b076259e5a0e30 FOREIGN KEY (last_modifier) REFERENCES public.users(id) DEFERRABLE INITIALLY DEFERRED;
 G   ALTER TABLE ONLY public.fixed_asset DROP CONSTRAINT fk9b076259e5a0e30;
       public       postgres    false    4585    474    296            ?           2606    27056    fixed_asset fk9b07625a4f8adc7    FK CONSTRAINT     ?   ALTER TABLE ONLY public.fixed_asset
    ADD CONSTRAINT fk9b07625a4f8adc7 FOREIGN KEY (fixed_asset_id) REFERENCES public.depreciation(id) DEFERRABLE INITIALLY DEFERRED;
 G   ALTER TABLE ONLY public.fixed_asset DROP CONSTRAINT fk9b07625a4f8adc7;
       public       postgres    false    4341    273    296            ?           2606    27041    fixed_asset fk9b07625b489c463    FK CONSTRAINT     ?   ALTER TABLE ONLY public.fixed_asset
    ADD CONSTRAINT fk9b07625b489c463 FOREIGN KEY (total_capital_gain_account_id) REFERENCES public.account(id) DEFERRABLE INITIALLY DEFERRED;
 G   ALTER TABLE ONLY public.fixed_asset DROP CONSTRAINT fk9b07625b489c463;
       public       postgres    false    197    296    4243            ?           2606    27051    fixed_asset fk9b07625c1d2676d    FK CONSTRAINT     ?   ALTER TABLE ONLY public.fixed_asset
    ADD CONSTRAINT fk9b07625c1d2676d FOREIGN KEY (sale_account_id) REFERENCES public.account(id) DEFERRABLE INITIALLY DEFERRED;
 G   ALTER TABLE ONLY public.fixed_asset DROP CONSTRAINT fk9b07625c1d2676d;
       public       postgres    false    4243    197    296            ?           2606    27081    fixed_asset fk9b07625c598d6ae    FK CONSTRAINT     ?   ALTER TABLE ONLY public.fixed_asset
    ADD CONSTRAINT fk9b07625c598d6ae FOREIGN KEY (depreciation_expense_account_id) REFERENCES public.account(id) DEFERRABLE INITIALLY DEFERRED;
 G   ALTER TABLE ONLY public.fixed_asset DROP CONSTRAINT fk9b07625c598d6ae;
       public       postgres    false    296    197    4243            ?           2606    27071    fixed_asset fk9b07625ead7486a    FK CONSTRAINT     ?   ALTER TABLE ONLY public.fixed_asset
    ADD CONSTRAINT fk9b07625ead7486a FOREIGN KEY (accumulated_depreciation_account_id) REFERENCES public.account(id) DEFERRABLE INITIALLY DEFERRED;
 G   ALTER TABLE ONLY public.fixed_asset DROP CONSTRAINT fk9b07625ead7486a;
       public       postgres    false    197    296    4243            ?           2606    27076    fixed_asset fk9b07625f1ae8cde    FK CONSTRAINT     ?   ALTER TABLE ONLY public.fixed_asset
    ADD CONSTRAINT fk9b07625f1ae8cde FOREIGN KEY (created_by) REFERENCES public.users(id) DEFERRABLE INITIALLY DEFERRED;
 G   ALTER TABLE ONLY public.fixed_asset DROP CONSTRAINT fk9b07625f1ae8cde;
       public       postgres    false    296    474    4585            ?           2606    28311 %   tdstransactionitem fk9d31648e63880555    FK CONSTRAINT     ?   ALTER TABLE ONLY public.tdstransactionitem
    ADD CONSTRAINT fk9d31648e63880555 FOREIGN KEY (transaction_id) REFERENCES public.transaction(id) DEFERRABLE INITIALLY DEFERRED;
 O   ALTER TABLE ONLY public.tdstransactionitem DROP CONSTRAINT fk9d31648e63880555;
       public       postgres    false    440    443    4553            ?           2606    28316 %   tdstransactionitem fk9d31648e6ad0cf08    FK CONSTRAINT     ?   ALTER TABLE ONLY public.tdstransactionitem
    ADD CONSTRAINT fk9d31648e6ad0cf08 FOREIGN KEY (tds_challandetail_id) REFERENCES public.tds_chalan_detail(id) DEFERRABLE INITIALLY DEFERRED;
 O   ALTER TABLE ONLY public.tdstransactionitem DROP CONSTRAINT fk9d31648e6ad0cf08;
       public       postgres    false    440    441    4551            ?           2606    28321 %   tdstransactionitem fk9d31648e891a177f    FK CONSTRAINT     ?   ALTER TABLE ONLY public.tdstransactionitem
    ADD CONSTRAINT fk9d31648e891a177f FOREIGN KEY (vendor_id) REFERENCES public.vendor(id) DEFERRABLE INITIALLY DEFERRED;
 O   ALTER TABLE ONLY public.tdstransactionitem DROP CONSTRAINT fk9d31648e891a177f;
       public       postgres    false    440    480    4595            A           2606    27681    pay_employee fk9e4b6c85274bc854    FK CONSTRAINT     ?   ALTER TABLE ONLY public.pay_employee
    ADD CONSTRAINT fk9e4b6c85274bc854 FOREIGN KEY (id) REFERENCES public.transaction(id) DEFERRABLE INITIALLY DEFERRED;
 I   ALTER TABLE ONLY public.pay_employee DROP CONSTRAINT fk9e4b6c85274bc854;
       public       postgres    false    443    366    4553            ?           2606    27671    pay_employee fk9e4b6c85473c4941    FK CONSTRAINT     ?   ALTER TABLE ONLY public.pay_employee
    ADD CONSTRAINT fk9e4b6c85473c4941 FOREIGN KEY (pay_from_id) REFERENCES public.account(id) DEFERRABLE INITIALLY DEFERRED;
 I   ALTER TABLE ONLY public.pay_employee DROP CONSTRAINT fk9e4b6c85473c4941;
       public       postgres    false    197    366    4243            @           2606    27676    pay_employee fk9e4b6c85a2fd387f    FK CONSTRAINT     ?   ALTER TABLE ONLY public.pay_employee
    ADD CONSTRAINT fk9e4b6c85a2fd387f FOREIGN KEY (employee_id) REFERENCES public.employee(id) DEFERRABLE INITIALLY DEFERRED;
 I   ALTER TABLE ONLY public.pay_employee DROP CONSTRAINT fk9e4b6c85a2fd387f;
       public       postgres    false    4349    280    366            >           2606    27666    pay_employee fk9e4b6c85b696bf58    FK CONSTRAINT     ?   ALTER TABLE ONLY public.pay_employee
    ADD CONSTRAINT fk9e4b6c85b696bf58 FOREIGN KEY (employee_group_id) REFERENCES public.employee_group(id) DEFERRABLE INITIALLY DEFERRED;
 I   ALTER TABLE ONLY public.pay_employee DROP CONSTRAINT fk9e4b6c85b696bf58;
       public       postgres    false    366    4351    282            ?           2606    28216 "   tax_item_groups fk9f02f5cc622c1275    FK CONSTRAINT     ?   ALTER TABLE ONLY public.tax_item_groups
    ADD CONSTRAINT fk9f02f5cc622c1275 FOREIGN KEY (company_id) REFERENCES public.company(id) DEFERRABLE INITIALLY DEFERRED;
 L   ALTER TABLE ONLY public.tax_item_groups DROP CONSTRAINT fk9f02f5cc622c1275;
       public       postgres    false    429    250    4309            ?           2606    28226 "   tax_item_groups fk9f02f5cc9e5a0e30    FK CONSTRAINT     ?   ALTER TABLE ONLY public.tax_item_groups
    ADD CONSTRAINT fk9f02f5cc9e5a0e30 FOREIGN KEY (last_modifier) REFERENCES public.users(id) DEFERRABLE INITIALLY DEFERRED;
 L   ALTER TABLE ONLY public.tax_item_groups DROP CONSTRAINT fk9f02f5cc9e5a0e30;
       public       postgres    false    429    474    4585            ?           2606    28221 "   tax_item_groups fk9f02f5ccf1ae8cde    FK CONSTRAINT     ?   ALTER TABLE ONLY public.tax_item_groups
    ADD CONSTRAINT fk9f02f5ccf1ae8cde FOREIGN KEY (created_by) REFERENCES public.users(id) DEFERRABLE INITIALLY DEFERRED;
 L   ALTER TABLE ONLY public.tax_item_groups DROP CONSTRAINT fk9f02f5ccf1ae8cde;
       public       postgres    false    429    474    4585            ?           2606    27236    invoice fk9fa1cf0d274bc854    FK CONSTRAINT     ?   ALTER TABLE ONLY public.invoice
    ADD CONSTRAINT fk9fa1cf0d274bc854 FOREIGN KEY (id) REFERENCES public.transaction(id) DEFERRABLE INITIALLY DEFERRED;
 D   ALTER TABLE ONLY public.invoice DROP CONSTRAINT fk9fa1cf0d274bc854;
       public       postgres    false    443    4553    312            ?           2606    27246    invoice fk9fa1cf0d4c74beae    FK CONSTRAINT     ?   ALTER TABLE ONLY public.invoice
    ADD CONSTRAINT fk9fa1cf0d4c74beae FOREIGN KEY (sales_person_id) REFERENCES public.sales_person(id) DEFERRABLE INITIALLY DEFERRED;
 D   ALTER TABLE ONLY public.invoice DROP CONSTRAINT fk9fa1cf0d4c74beae;
       public       postgres    false    4503    402    312            ?           2606    27251    invoice fk9fa1cf0d97edd458    FK CONSTRAINT     ?   ALTER TABLE ONLY public.invoice
    ADD CONSTRAINT fk9fa1cf0d97edd458 FOREIGN KEY (shipping_terms_id) REFERENCES public.shippingterms(id) DEFERRABLE INITIALLY DEFERRED;
 D   ALTER TABLE ONLY public.invoice DROP CONSTRAINT fk9fa1cf0d97edd458;
       public       postgres    false    4509    408    312            ?           2606    27221    invoice fk9fa1cf0d9a3059ec    FK CONSTRAINT     ?   ALTER TABLE ONLY public.invoice
    ADD CONSTRAINT fk9fa1cf0d9a3059ec FOREIGN KEY (price_level_id) REFERENCES public.pricelevel(id) DEFERRABLE INITIALLY DEFERRED;
 D   ALTER TABLE ONLY public.invoice DROP CONSTRAINT fk9fa1cf0d9a3059ec;
       public       postgres    false    312    384    4479            ?           2606    27226    invoice fk9fa1cf0dad0a95dc    FK CONSTRAINT     ?   ALTER TABLE ONLY public.invoice
    ADD CONSTRAINT fk9fa1cf0dad0a95dc FOREIGN KEY (shipping_method_id) REFERENCES public.shippingmethod(id) DEFERRABLE INITIALLY DEFERRED;
 D   ALTER TABLE ONLY public.invoice DROP CONSTRAINT fk9fa1cf0dad0a95dc;
       public       postgres    false    4507    406    312            ?           2606    27231    invoice fk9fa1cf0daea76b12    FK CONSTRAINT     ?   ALTER TABLE ONLY public.invoice
    ADD CONSTRAINT fk9fa1cf0daea76b12 FOREIGN KEY (payment_terms_id) REFERENCES public.paymentterms(id) DEFERRABLE INITIALLY DEFERRED;
 D   ALTER TABLE ONLY public.invoice DROP CONSTRAINT fk9fa1cf0daea76b12;
       public       postgres    false    312    4451    362            ?           2606    27241    invoice fk9fa1cf0ddfe06a7f    FK CONSTRAINT     ?   ALTER TABLE ONLY public.invoice
    ADD CONSTRAINT fk9fa1cf0ddfe06a7f FOREIGN KEY (customer_id) REFERENCES public.customer(id) DEFERRABLE INITIALLY DEFERRED;
 D   ALTER TABLE ONLY public.invoice DROP CONSTRAINT fk9fa1cf0ddfe06a7f;
       public       postgres    false    4325    312    261                       2606    27501    location fk9ff58fb5622c1275    FK CONSTRAINT     ?   ALTER TABLE ONLY public.location
    ADD CONSTRAINT fk9ff58fb5622c1275 FOREIGN KEY (company_id) REFERENCES public.company(id) DEFERRABLE INITIALLY DEFERRED;
 E   ALTER TABLE ONLY public.location DROP CONSTRAINT fk9ff58fb5622c1275;
       public       postgres    false    4309    250    337            ?           2606    26856    developer fka148f7aa58dbf25b    FK CONSTRAINT     ?   ALTER TABLE ONLY public.developer
    ADD CONSTRAINT fka148f7aa58dbf25b FOREIGN KEY (client) REFERENCES public.client(id) DEFERRABLE INITIALLY DEFERRED;
 F   ALTER TABLE ONLY public.developer DROP CONSTRAINT fka148f7aa58dbf25b;
       public       postgres    false    275    241    4299            ?           2606    28541 &   transaction_paybill fka36b3c0e20610806    FK CONSTRAINT     ?   ALTER TABLE ONLY public.transaction_paybill
    ADD CONSTRAINT fka36b3c0e20610806 FOREIGN KEY (enter_bill_id) REFERENCES public.enter_bill(id) DEFERRABLE INITIALLY DEFERRED;
 P   ALTER TABLE ONLY public.transaction_paybill DROP CONSTRAINT fka36b3c0e20610806;
       public       postgres    false    288    457    4359            ?           2606    28536 &   transaction_paybill fka36b3c0e408f6290    FK CONSTRAINT     ?   ALTER TABLE ONLY public.transaction_paybill
    ADD CONSTRAINT fka36b3c0e408f6290 FOREIGN KEY (journal_entry_id) REFERENCES public.journal_entry(id) DEFERRABLE INITIALLY DEFERRED;
 P   ALTER TABLE ONLY public.transaction_paybill DROP CONSTRAINT fka36b3c0e408f6290;
       public       postgres    false    325    457    4405            ?           2606    28551 &   transaction_paybill fka36b3c0e4e05c155    FK CONSTRAINT     ?   ALTER TABLE ONLY public.transaction_paybill
    ADD CONSTRAINT fka36b3c0e4e05c155 FOREIGN KEY (paybill_id) REFERENCES public.pay_bill(id) DEFERRABLE INITIALLY DEFERRED;
 P   ALTER TABLE ONLY public.transaction_paybill DROP CONSTRAINT fka36b3c0e4e05c155;
       public       postgres    false    365    4455    457            ?           2606    28546 &   transaction_paybill fka36b3c0e622c1275    FK CONSTRAINT     ?   ALTER TABLE ONLY public.transaction_paybill
    ADD CONSTRAINT fka36b3c0e622c1275 FOREIGN KEY (company_id) REFERENCES public.company(id) DEFERRABLE INITIALLY DEFERRED;
 P   ALTER TABLE ONLY public.transaction_paybill DROP CONSTRAINT fka36b3c0e622c1275;
       public       postgres    false    250    457    4309            ?           2606    28531 &   transaction_paybill fka36b3c0e69504cc6    FK CONSTRAINT     ?   ALTER TABLE ONLY public.transaction_paybill
    ADD CONSTRAINT fka36b3c0e69504cc6 FOREIGN KEY (transaction_id) REFERENCES public.pay_bill(id) DEFERRABLE INITIALLY DEFERRED;
 P   ALTER TABLE ONLY public.transaction_paybill DROP CONSTRAINT fka36b3c0e69504cc6;
       public       postgres    false    365    457    4455            ?           2606    28556 &   transaction_paybill fka36b3c0ef5e540e1    FK CONSTRAINT     ?   ALTER TABLE ONLY public.transaction_paybill
    ADD CONSTRAINT fka36b3c0ef5e540e1 FOREIGN KEY (discount_id) REFERENCES public.account(id) DEFERRABLE INITIALLY DEFERRED;
 P   ALTER TABLE ONLY public.transaction_paybill DROP CONSTRAINT fka36b3c0ef5e540e1;
       public       postgres    false    4243    197    457            ?           2606    28596 &   transaction_pay_tax fka37892d353d2ca4e    FK CONSTRAINT     ?   ALTER TABLE ONLY public.transaction_pay_tax
    ADD CONSTRAINT fka37892d353d2ca4e FOREIGN KEY (pay_tax_id) REFERENCES public.pay_tax(id) DEFERRABLE INITIALLY DEFERRED;
 P   ALTER TABLE ONLY public.transaction_pay_tax DROP CONSTRAINT fka37892d353d2ca4e;
       public       postgres    false    4471    463    377            ?           2606    28601 &   transaction_pay_tax fka37892d398d004ca    FK CONSTRAINT     ?   ALTER TABLE ONLY public.transaction_pay_tax
    ADD CONSTRAINT fka37892d398d004ca FOREIGN KEY (vat_return_id) REFERENCES public.tax_return(id) DEFERRABLE INITIALLY DEFERRED;
 P   ALTER TABLE ONLY public.transaction_pay_tax DROP CONSTRAINT fka37892d398d004ca;
       public       postgres    false    4541    463    432            ?           2606    28591 &   transaction_pay_tax fka37892d3c368d6ac    FK CONSTRAINT     ?   ALTER TABLE ONLY public.transaction_pay_tax
    ADD CONSTRAINT fka37892d3c368d6ac FOREIGN KEY (tax_agency_id) REFERENCES public.taxagency(id) DEFERRABLE INITIALLY DEFERRED;
 P   ALTER TABLE ONLY public.transaction_pay_tax DROP CONSTRAINT fka37892d3c368d6ac;
       public       postgres    false    4523    463    419            |           2606    27976    sales_person fka4d19a08622c1275    FK CONSTRAINT     ?   ALTER TABLE ONLY public.sales_person
    ADD CONSTRAINT fka4d19a08622c1275 FOREIGN KEY (company_id) REFERENCES public.company(id) DEFERRABLE INITIALLY DEFERRED;
 I   ALTER TABLE ONLY public.sales_person DROP CONSTRAINT fka4d19a08622c1275;
       public       postgres    false    402    250    4309            }           2606    27981    sales_person fka4d19a08de9a3134    FK CONSTRAINT     ?   ALTER TABLE ONLY public.sales_person
    ADD CONSTRAINT fka4d19a08de9a3134 FOREIGN KEY (last_modified_by) REFERENCES public.users(id) DEFERRABLE INITIALLY DEFERRED;
 I   ALTER TABLE ONLY public.sales_person DROP CONSTRAINT fka4d19a08de9a3134;
       public       postgres    false    402    474    4585                       2606    27991    sales_person fka4d19a08e2aa5abc    FK CONSTRAINT     ?   ALTER TABLE ONLY public.sales_person
    ADD CONSTRAINT fka4d19a08e2aa5abc FOREIGN KEY (expense_account_id) REFERENCES public.account(id) DEFERRABLE INITIALLY DEFERRED;
 I   ALTER TABLE ONLY public.sales_person DROP CONSTRAINT fka4d19a08e2aa5abc;
       public       postgres    false    402    197    4243            ~           2606    27986    sales_person fka4d19a08f1ae8cde    FK CONSTRAINT     ?   ALTER TABLE ONLY public.sales_person
    ADD CONSTRAINT fka4d19a08f1ae8cde FOREIGN KEY (created_by) REFERENCES public.users(id) DEFERRABLE INITIALLY DEFERRED;
 I   ALTER TABLE ONLY public.sales_person DROP CONSTRAINT fka4d19a08f1ae8cde;
       public       postgres    false    402    474    4585            ?           2606    28326 $   tds_chalan_detail fka5761ff719efdc30    FK CONSTRAINT     ?   ALTER TABLE ONLY public.tds_chalan_detail
    ADD CONSTRAINT fka5761ff719efdc30 FOREIGN KEY (payfrom_id) REFERENCES public.account(id) DEFERRABLE INITIALLY DEFERRED;
 N   ALTER TABLE ONLY public.tds_chalan_detail DROP CONSTRAINT fka5761ff719efdc30;
       public       postgres    false    441    197    4243            ?           2606    28331 $   tds_chalan_detail fka5761ff7274bc854    FK CONSTRAINT     ?   ALTER TABLE ONLY public.tds_chalan_detail
    ADD CONSTRAINT fka5761ff7274bc854 FOREIGN KEY (id) REFERENCES public.transaction(id) DEFERRABLE INITIALLY DEFERRED;
 N   ALTER TABLE ONLY public.tds_chalan_detail DROP CONSTRAINT fka5761ff7274bc854;
       public       postgres    false    441    443    4553            ?           2606    27146 )   inventory_assembly_item fka6ee00946ae6ef6    FK CONSTRAINT     ?   ALTER TABLE ONLY public.inventory_assembly_item
    ADD CONSTRAINT fka6ee00946ae6ef6 FOREIGN KEY (qty_unit) REFERENCES public.unit(id) DEFERRABLE INITIALLY DEFERRED;
 S   ALTER TABLE ONLY public.inventory_assembly_item DROP CONSTRAINT fka6ee00946ae6ef6;
       public       postgres    false    470    4581    305            ?           2606    27141 )   inventory_assembly_item fka6ee00947a69c6e    FK CONSTRAINT     ?   ALTER TABLE ONLY public.inventory_assembly_item
    ADD CONSTRAINT fka6ee00947a69c6e FOREIGN KEY (inventory_assembly_item_id) REFERENCES public.inventory_assembly(id) DEFERRABLE INITIALLY DEFERRED;
 S   ALTER TABLE ONLY public.inventory_assembly_item DROP CONSTRAINT fka6ee00947a69c6e;
       public       postgres    false    4379    303    305            ?           2606    27136 )   inventory_assembly_item fka6ee0099495a8bc    FK CONSTRAINT     ?   ALTER TABLE ONLY public.inventory_assembly_item
    ADD CONSTRAINT fka6ee0099495a8bc FOREIGN KEY (ware_house) REFERENCES public.warehouse(id) DEFERRABLE INITIALLY DEFERRED;
 S   ALTER TABLE ONLY public.inventory_assembly_item DROP CONSTRAINT fka6ee0099495a8bc;
       public       postgres    false    488    305    4605            ?           2606    27131 )   inventory_assembly_item fka6ee00999b6d0fc    FK CONSTRAINT     ?   ALTER TABLE ONLY public.inventory_assembly_item
    ADD CONSTRAINT fka6ee00999b6d0fc FOREIGN KEY (inventory_item_id) REFERENCES public.item(id) DEFERRABLE INITIALLY DEFERRED;
 S   ALTER TABLE ONLY public.inventory_assembly_item DROP CONSTRAINT fka6ee00999b6d0fc;
       public       postgres    false    315    4393    305            p           2606    27916 !   reconciliation fka892b6e3622c1275    FK CONSTRAINT     ?   ALTER TABLE ONLY public.reconciliation
    ADD CONSTRAINT fka892b6e3622c1275 FOREIGN KEY (company_id) REFERENCES public.company(id) DEFERRABLE INITIALLY DEFERRED;
 K   ALTER TABLE ONLY public.reconciliation DROP CONSTRAINT fka892b6e3622c1275;
       public       postgres    false    390    250    4309            q           2606    27921 !   reconciliation fka892b6e3cfb26235    FK CONSTRAINT     ?   ALTER TABLE ONLY public.reconciliation
    ADD CONSTRAINT fka892b6e3cfb26235 FOREIGN KEY (account) REFERENCES public.account(id) DEFERRABLE INITIALLY DEFERRED;
 K   ALTER TABLE ONLY public.reconciliation DROP CONSTRAINT fka892b6e3cfb26235;
       public       postgres    false    390    197    4243            ?           2606    28111    taxagency fka8b93f5011d2bd2b    FK CONSTRAINT     ?   ALTER TABLE ONLY public.taxagency
    ADD CONSTRAINT fka8b93f5011d2bd2b FOREIGN KEY (payment_term) REFERENCES public.paymentterms(id) DEFERRABLE INITIALLY DEFERRED;
 F   ALTER TABLE ONLY public.taxagency DROP CONSTRAINT fka8b93f5011d2bd2b;
       public       postgres    false    419    362    4451            ?           2606    28121    taxagency fka8b93f505f952188    FK CONSTRAINT     ?   ALTER TABLE ONLY public.taxagency
    ADD CONSTRAINT fka8b93f505f952188 FOREIGN KEY (sales_account_id) REFERENCES public.account(id) DEFERRABLE INITIALLY DEFERRED;
 F   ALTER TABLE ONLY public.taxagency DROP CONSTRAINT fka8b93f505f952188;
       public       postgres    false    419    197    4243            ?           2606    28126    taxagency fka8b93f50610348fe    FK CONSTRAINT     ?   ALTER TABLE ONLY public.taxagency
    ADD CONSTRAINT fka8b93f50610348fe FOREIGN KEY (id) REFERENCES public.payee(id) DEFERRABLE INITIALLY DEFERRED;
 F   ALTER TABLE ONLY public.taxagency DROP CONSTRAINT fka8b93f50610348fe;
       public       postgres    false    419    353    4439            ?           2606    28106    taxagency fka8b93f50622c1275    FK CONSTRAINT     ?   ALTER TABLE ONLY public.taxagency
    ADD CONSTRAINT fka8b93f50622c1275 FOREIGN KEY (company_id) REFERENCES public.company(id) DEFERRABLE INITIALLY DEFERRED;
 F   ALTER TABLE ONLY public.taxagency DROP CONSTRAINT fka8b93f50622c1275;
       public       postgres    false    419    250    4309            ?           2606    28116    taxagency fka8b93f50633eca73    FK CONSTRAINT     ?   ALTER TABLE ONLY public.taxagency
    ADD CONSTRAINT fka8b93f50633eca73 FOREIGN KEY (purchase_account_id) REFERENCES public.account(id) DEFERRABLE INITIALLY DEFERRED;
 F   ALTER TABLE ONLY public.taxagency DROP CONSTRAINT fka8b93f50633eca73;
       public       postgres    false    419    197    4243            ?           2606    28101    taxagency fka8b93f50e9576ec    FK CONSTRAINT     ?   ALTER TABLE ONLY public.taxagency
    ADD CONSTRAINT fka8b93f50e9576ec FOREIGN KEY (filed_liability_account) REFERENCES public.account(id) DEFERRABLE INITIALLY DEFERRED;
 E   ALTER TABLE ONLY public.taxagency DROP CONSTRAINT fka8b93f50e9576ec;
       public       postgres    false    419    197    4243            ?           2606    27181 $   inventory_history fkaa14447146ae6ef6    FK CONSTRAINT     ?   ALTER TABLE ONLY public.inventory_history
    ADD CONSTRAINT fkaa14447146ae6ef6 FOREIGN KEY (qty_unit) REFERENCES public.unit(id) DEFERRABLE INITIALLY DEFERRED;
 N   ALTER TABLE ONLY public.inventory_history DROP CONSTRAINT fkaa14447146ae6ef6;
       public       postgres    false    307    4581    470            ?           2606    27161 $   inventory_history fkaa144471622c1275    FK CONSTRAINT     ?   ALTER TABLE ONLY public.inventory_history
    ADD CONSTRAINT fkaa144471622c1275 FOREIGN KEY (company_id) REFERENCES public.company(id) DEFERRABLE INITIALLY DEFERRED;
 N   ALTER TABLE ONLY public.inventory_history DROP CONSTRAINT fkaa144471622c1275;
       public       postgres    false    307    4309    250            ?           2606    27151 $   inventory_history fkaa144471675b6f2b    FK CONSTRAINT     ?   ALTER TABLE ONLY public.inventory_history
    ADD CONSTRAINT fkaa144471675b6f2b FOREIGN KEY (payee) REFERENCES public.payee(id) DEFERRABLE INITIALLY DEFERRED;
 N   ALTER TABLE ONLY public.inventory_history DROP CONSTRAINT fkaa144471675b6f2b;
       public       postgres    false    4439    307    353            ?           2606    27166 $   inventory_history fkaa144471984db8a1    FK CONSTRAINT     ?   ALTER TABLE ONLY public.inventory_history
    ADD CONSTRAINT fkaa144471984db8a1 FOREIGN KEY (warehouse) REFERENCES public.warehouse(id) DEFERRABLE INITIALLY DEFERRED;
 N   ALTER TABLE ONLY public.inventory_history DROP CONSTRAINT fkaa144471984db8a1;
       public       postgres    false    488    4605    307            ?           2606    27186 $   inventory_history fkaa1444719e5a0e30    FK CONSTRAINT     ?   ALTER TABLE ONLY public.inventory_history
    ADD CONSTRAINT fkaa1444719e5a0e30 FOREIGN KEY (last_modifier) REFERENCES public.users(id) DEFERRABLE INITIALLY DEFERRED;
 N   ALTER TABLE ONLY public.inventory_history DROP CONSTRAINT fkaa1444719e5a0e30;
       public       postgres    false    4585    307    474            ?           2606    27156 $   inventory_history fkaa144471a036ee2b    FK CONSTRAINT     ?   ALTER TABLE ONLY public.inventory_history
    ADD CONSTRAINT fkaa144471a036ee2b FOREIGN KEY (item) REFERENCES public.item(id) DEFERRABLE INITIALLY DEFERRED;
 N   ALTER TABLE ONLY public.inventory_history DROP CONSTRAINT fkaa144471a036ee2b;
       public       postgres    false    4393    315    307            ?           2606    27176 $   inventory_history fkaa144471a6ec8e17    FK CONSTRAINT     ?   ALTER TABLE ONLY public.inventory_history
    ADD CONSTRAINT fkaa144471a6ec8e17 FOREIGN KEY (transaction) REFERENCES public.transaction(id) DEFERRABLE INITIALLY DEFERRED;
 N   ALTER TABLE ONLY public.inventory_history DROP CONSTRAINT fkaa144471a6ec8e17;
       public       postgres    false    4553    307    443            ?           2606    27171 $   inventory_history fkaa144471f1ae8cde    FK CONSTRAINT     ?   ALTER TABLE ONLY public.inventory_history
    ADD CONSTRAINT fkaa144471f1ae8cde FOREIGN KEY (created_by) REFERENCES public.users(id) DEFERRABLE INITIALLY DEFERRED;
 N   ALTER TABLE ONLY public.inventory_history DROP CONSTRAINT fkaa144471f1ae8cde;
       public       postgres    false    307    4585    474            (           2606    26276 -   attendance_management_item fkab122c39342f2ae1    FK CONSTRAINT     ?   ALTER TABLE ONLY public.attendance_management_item
    ADD CONSTRAINT fkab122c39342f2ae1 FOREIGN KEY (attendance_management) REFERENCES public.pay_run(id) DEFERRABLE INITIALLY DEFERRED;
 W   ALTER TABLE ONLY public.attendance_management_item DROP CONSTRAINT fkab122c39342f2ae1;
       public       postgres    false    4465    218    372            )           2606    26281 -   attendance_management_item fkab122c39b3a43ae1    FK CONSTRAINT     ?   ALTER TABLE ONLY public.attendance_management_item
    ADD CONSTRAINT fkab122c39b3a43ae1 FOREIGN KEY (employee) REFERENCES public.employee(id) DEFERRABLE INITIALLY DEFERRED;
 W   ALTER TABLE ONLY public.attendance_management_item DROP CONSTRAINT fkab122c39b3a43ae1;
       public       postgres    false    4349    280    218            ?           2606    28421 +   transaction_deposit_item fkad5ed015381e9d55    FK CONSTRAINT     ?   ALTER TABLE ONLY public.transaction_deposit_item
    ADD CONSTRAINT fkad5ed015381e9d55 FOREIGN KEY (received_from_id) REFERENCES public.payee(id) DEFERRABLE INITIALLY DEFERRED;
 U   ALTER TABLE ONLY public.transaction_deposit_item DROP CONSTRAINT fkad5ed015381e9d55;
       public       postgres    false    447    4439    353            ?           2606    28431 +   transaction_deposit_item fkad5ed0153ba00678    FK CONSTRAINT     ?   ALTER TABLE ONLY public.transaction_deposit_item
    ADD CONSTRAINT fkad5ed0153ba00678 FOREIGN KEY (make_deposit_id) REFERENCES public.make_deposit(id) DEFERRABLE INITIALLY DEFERRED;
 U   ALTER TABLE ONLY public.transaction_deposit_item DROP CONSTRAINT fkad5ed0153ba00678;
       public       postgres    false    447    339    4423            ?           2606    28406 +   transaction_deposit_item fkad5ed01563880555    FK CONSTRAINT     ?   ALTER TABLE ONLY public.transaction_deposit_item
    ADD CONSTRAINT fkad5ed01563880555 FOREIGN KEY (transaction_id) REFERENCES public.transaction(id) DEFERRABLE INITIALLY DEFERRED;
 U   ALTER TABLE ONLY public.transaction_deposit_item DROP CONSTRAINT fkad5ed01563880555;
       public       postgres    false    447    443    4553            ?           2606    28436 +   transaction_deposit_item fkad5ed0156da97c61    FK CONSTRAINT     ?   ALTER TABLE ONLY public.transaction_deposit_item
    ADD CONSTRAINT fkad5ed0156da97c61 FOREIGN KEY (trans_item_accounter_class) REFERENCES public.accounter_class(id) DEFERRABLE INITIALLY DEFERRED;
 U   ALTER TABLE ONLY public.transaction_deposit_item DROP CONSTRAINT fkad5ed0156da97c61;
       public       postgres    false    4245    447    199            ?           2606    28416 *   transaction_deposit_item fkad5ed015aa7eff5    FK CONSTRAINT     ?   ALTER TABLE ONLY public.transaction_deposit_item
    ADD CONSTRAINT fkad5ed015aa7eff5 FOREIGN KEY (job_id) REFERENCES public.job(id) DEFERRABLE INITIALLY DEFERRED;
 T   ALTER TABLE ONLY public.transaction_deposit_item DROP CONSTRAINT fkad5ed015aa7eff5;
       public       postgres    false    447    324    4403            ?           2606    28426 +   transaction_deposit_item fkad5ed015dfe06a7f    FK CONSTRAINT     ?   ALTER TABLE ONLY public.transaction_deposit_item
    ADD CONSTRAINT fkad5ed015dfe06a7f FOREIGN KEY (customer_id) REFERENCES public.customer(id) DEFERRABLE INITIALLY DEFERRED;
 U   ALTER TABLE ONLY public.transaction_deposit_item DROP CONSTRAINT fkad5ed015dfe06a7f;
       public       postgres    false    4325    447    261            ?           2606    28411 +   transaction_deposit_item fkad5ed015e5fcf475    FK CONSTRAINT     ?   ALTER TABLE ONLY public.transaction_deposit_item
    ADD CONSTRAINT fkad5ed015e5fcf475 FOREIGN KEY (account_id) REFERENCES public.account(id) DEFERRABLE INITIALLY DEFERRED;
 U   ALTER TABLE ONLY public.transaction_deposit_item DROP CONSTRAINT fkad5ed015e5fcf475;
       public       postgres    false    447    197    4243            ?           2606    28576 +   transaction_pay_employee fkae67d466517e624e    FK CONSTRAINT     ?   ALTER TABLE ONLY public.transaction_pay_employee
    ADD CONSTRAINT fkae67d466517e624e FOREIGN KEY (pay_run_id) REFERENCES public.pay_run(id) DEFERRABLE INITIALLY DEFERRED;
 U   ALTER TABLE ONLY public.transaction_pay_employee DROP CONSTRAINT fkae67d466517e624e;
       public       postgres    false    4465    372    459            ?           2606    28566 +   transaction_pay_employee fkae67d466622c1275    FK CONSTRAINT     ?   ALTER TABLE ONLY public.transaction_pay_employee
    ADD CONSTRAINT fkae67d466622c1275 FOREIGN KEY (company_id) REFERENCES public.company(id) DEFERRABLE INITIALLY DEFERRED;
 U   ALTER TABLE ONLY public.transaction_pay_employee DROP CONSTRAINT fkae67d466622c1275;
       public       postgres    false    459    250    4309            ?           2606    28571 +   transaction_pay_employee fkae67d4666d40f306    FK CONSTRAINT     ?   ALTER TABLE ONLY public.transaction_pay_employee
    ADD CONSTRAINT fkae67d4666d40f306 FOREIGN KEY (pay_employee_id) REFERENCES public.pay_employee(id) DEFERRABLE INITIALLY DEFERRED;
 U   ALTER TABLE ONLY public.transaction_pay_employee DROP CONSTRAINT fkae67d4666d40f306;
       public       postgres    false    459    366    4457            ?           2606    28561 +   transaction_pay_employee fkae67d466f71f05ed    FK CONSTRAINT     ?   ALTER TABLE ONLY public.transaction_pay_employee
    ADD CONSTRAINT fkae67d466f71f05ed FOREIGN KEY (transaction_id) REFERENCES public.pay_employee(id) DEFERRABLE INITIALLY DEFERRED;
 U   ALTER TABLE ONLY public.transaction_pay_employee DROP CONSTRAINT fkae67d466f71f05ed;
       public       postgres    false    4457    459    366            J           2606    27726     pay_roll_unit fkafa4fd0f622c1275    FK CONSTRAINT     ?   ALTER TABLE ONLY public.pay_roll_unit
    ADD CONSTRAINT fkafa4fd0f622c1275 FOREIGN KEY (company_id) REFERENCES public.company(id) DEFERRABLE INITIALLY DEFERRED;
 J   ALTER TABLE ONLY public.pay_roll_unit DROP CONSTRAINT fkafa4fd0f622c1275;
       public       postgres    false    371    250    4309            L           2606    27736     pay_roll_unit fkafa4fd0f9e5a0e30    FK CONSTRAINT     ?   ALTER TABLE ONLY public.pay_roll_unit
    ADD CONSTRAINT fkafa4fd0f9e5a0e30 FOREIGN KEY (last_modifier) REFERENCES public.users(id) DEFERRABLE INITIALLY DEFERRED;
 J   ALTER TABLE ONLY public.pay_roll_unit DROP CONSTRAINT fkafa4fd0f9e5a0e30;
       public       postgres    false    371    474    4585            K           2606    27731     pay_roll_unit fkafa4fd0ff1ae8cde    FK CONSTRAINT     ?   ALTER TABLE ONLY public.pay_roll_unit
    ADD CONSTRAINT fkafa4fd0ff1ae8cde FOREIGN KEY (created_by) REFERENCES public.users(id) DEFERRABLE INITIALLY DEFERRED;
 J   ALTER TABLE ONLY public.pay_roll_unit DROP CONSTRAINT fkafa4fd0ff1ae8cde;
       public       postgres    false    371    474    4585            !           2606    27521 "   message_or_task fkb0a6ee09622c1275    FK CONSTRAINT     ?   ALTER TABLE ONLY public.message_or_task
    ADD CONSTRAINT fkb0a6ee09622c1275 FOREIGN KEY (company_id) REFERENCES public.company(id) DEFERRABLE INITIALLY DEFERRED;
 L   ALTER TABLE ONLY public.message_or_task DROP CONSTRAINT fkb0a6ee09622c1275;
       public       postgres    false    346    250    4309            #           2606    27531 "   message_or_task fkb0a6ee099e5a0e30    FK CONSTRAINT     ?   ALTER TABLE ONLY public.message_or_task
    ADD CONSTRAINT fkb0a6ee099e5a0e30 FOREIGN KEY (last_modifier) REFERENCES public.users(id) DEFERRABLE INITIALLY DEFERRED;
 L   ALTER TABLE ONLY public.message_or_task DROP CONSTRAINT fkb0a6ee099e5a0e30;
       public       postgres    false    4585    474    346            "           2606    27526 "   message_or_task fkb0a6ee09f1ae8cde    FK CONSTRAINT     ?   ALTER TABLE ONLY public.message_or_task
    ADD CONSTRAINT fkb0a6ee09f1ae8cde FOREIGN KEY (created_by) REFERENCES public.users(id) DEFERRABLE INITIALLY DEFERRED;
 L   ALTER TABLE ONLY public.message_or_task DROP CONSTRAINT fkb0a6ee09f1ae8cde;
       public       postgres    false    4585    474    346            .           2606    27586 %   payee_customfields fkb0ff692159038d55    FK CONSTRAINT     ?   ALTER TABLE ONLY public.payee_customfields
    ADD CONSTRAINT fkb0ff692159038d55 FOREIGN KEY (customfield_id) REFERENCES public.customfield(id) DEFERRABLE INITIALLY DEFERRED;
 O   ALTER TABLE ONLY public.payee_customfields DROP CONSTRAINT fkb0ff692159038d55;
       public       postgres    false    4335    268    357            /           2606    27591 %   payee_customfields fkb0ff6921b2fc5555    FK CONSTRAINT     ?   ALTER TABLE ONLY public.payee_customfields
    ADD CONSTRAINT fkb0ff6921b2fc5555 FOREIGN KEY (payee_id) REFERENCES public.payee(id) DEFERRABLE INITIALLY DEFERRED;
 O   ALTER TABLE ONLY public.payee_customfields DROP CONSTRAINT fkb0ff6921b2fc5555;
       public       postgres    false    357    353    4439            ,           2606    26296 /   attendance_or_production_tpe fkb3257ec9609b9228    FK CONSTRAINT     ?   ALTER TABLE ONLY public.attendance_or_production_tpe
    ADD CONSTRAINT fkb3257ec9609b9228 FOREIGN KEY (unit) REFERENCES public.pay_roll_unit(id) DEFERRABLE INITIALLY DEFERRED;
 Y   ALTER TABLE ONLY public.attendance_or_production_tpe DROP CONSTRAINT fkb3257ec9609b9228;
       public       postgres    false    4463    371    221            -           2606    26301 /   attendance_or_production_tpe fkb3257ec9622c1275    FK CONSTRAINT     ?   ALTER TABLE ONLY public.attendance_or_production_tpe
    ADD CONSTRAINT fkb3257ec9622c1275 FOREIGN KEY (company_id) REFERENCES public.company(id) DEFERRABLE INITIALLY DEFERRED;
 Y   ALTER TABLE ONLY public.attendance_or_production_tpe DROP CONSTRAINT fkb3257ec9622c1275;
       public       postgres    false    250    221    4309            /           2606    26311 /   attendance_or_production_tpe fkb3257ec99e5a0e30    FK CONSTRAINT     ?   ALTER TABLE ONLY public.attendance_or_production_tpe
    ADD CONSTRAINT fkb3257ec99e5a0e30 FOREIGN KEY (last_modifier) REFERENCES public.users(id) DEFERRABLE INITIALLY DEFERRED;
 Y   ALTER TABLE ONLY public.attendance_or_production_tpe DROP CONSTRAINT fkb3257ec99e5a0e30;
       public       postgres    false    474    4585    221            .           2606    26306 /   attendance_or_production_tpe fkb3257ec9f1ae8cde    FK CONSTRAINT     ?   ALTER TABLE ONLY public.attendance_or_production_tpe
    ADD CONSTRAINT fkb3257ec9f1ae8cde FOREIGN KEY (created_by) REFERENCES public.users(id) DEFERRABLE INITIALLY DEFERRED;
 Y   ALTER TABLE ONLY public.attendance_or_production_tpe DROP CONSTRAINT fkb3257ec9f1ae8cde;
       public       postgres    false    474    221    4585            ?           2606    28276    tax_return fkb431e864274bc854    FK CONSTRAINT     ?   ALTER TABLE ONLY public.tax_return
    ADD CONSTRAINT fkb431e864274bc854 FOREIGN KEY (id) REFERENCES public.transaction(id) DEFERRABLE INITIALLY DEFERRED;
 G   ALTER TABLE ONLY public.tax_return DROP CONSTRAINT fkb431e864274bc854;
       public       postgres    false    432    443    4553            ?           2606    28266    tax_return fkb431e864622c1275    FK CONSTRAINT     ?   ALTER TABLE ONLY public.tax_return
    ADD CONSTRAINT fkb431e864622c1275 FOREIGN KEY (company_id) REFERENCES public.company(id) DEFERRABLE INITIALLY DEFERRED;
 G   ALTER TABLE ONLY public.tax_return DROP CONSTRAINT fkb431e864622c1275;
       public       postgres    false    432    250    4309            ?           2606    28271    tax_return fkb431e864971bcdc4    FK CONSTRAINT     ?   ALTER TABLE ONLY public.tax_return
    ADD CONSTRAINT fkb431e864971bcdc4 FOREIGN KEY (tax_agency) REFERENCES public.taxagency(id) DEFERRABLE INITIALLY DEFERRED;
 G   ALTER TABLE ONLY public.tax_return DROP CONSTRAINT fkb431e864971bcdc4;
       public       postgres    false    432    419    4523            *           2606    28851    write_checks fkb592e8eb274bc854    FK CONSTRAINT     ?   ALTER TABLE ONLY public.write_checks
    ADD CONSTRAINT fkb592e8eb274bc854 FOREIGN KEY (id) REFERENCES public.transaction(id) DEFERRABLE INITIALLY DEFERRED;
 I   ALTER TABLE ONLY public.write_checks DROP CONSTRAINT fkb592e8eb274bc854;
       public       postgres    false    443    490    4553            ,           2606    28861    write_checks fkb592e8eb4c74beae    FK CONSTRAINT     ?   ALTER TABLE ONLY public.write_checks
    ADD CONSTRAINT fkb592e8eb4c74beae FOREIGN KEY (sales_person_id) REFERENCES public.sales_person(id) DEFERRABLE INITIALLY DEFERRED;
 I   ALTER TABLE ONLY public.write_checks DROP CONSTRAINT fkb592e8eb4c74beae;
       public       postgres    false    402    4503    490            )           2606    28846    write_checks fkb592e8eb891a177f    FK CONSTRAINT     ?   ALTER TABLE ONLY public.write_checks
    ADD CONSTRAINT fkb592e8eb891a177f FOREIGN KEY (vendor_id) REFERENCES public.vendor(id) DEFERRABLE INITIALLY DEFERRED;
 I   ALTER TABLE ONLY public.write_checks DROP CONSTRAINT fkb592e8eb891a177f;
       public       postgres    false    480    490    4595            '           2606    28836    write_checks fkb592e8ebc368d6ac    FK CONSTRAINT     ?   ALTER TABLE ONLY public.write_checks
    ADD CONSTRAINT fkb592e8ebc368d6ac FOREIGN KEY (tax_agency_id) REFERENCES public.taxagency(id) DEFERRABLE INITIALLY DEFERRED;
 I   ALTER TABLE ONLY public.write_checks DROP CONSTRAINT fkb592e8ebc368d6ac;
       public       postgres    false    419    490    4523            +           2606    28856    write_checks fkb592e8ebdfe06a7f    FK CONSTRAINT     ?   ALTER TABLE ONLY public.write_checks
    ADD CONSTRAINT fkb592e8ebdfe06a7f FOREIGN KEY (customer_id) REFERENCES public.customer(id) DEFERRABLE INITIALLY DEFERRED;
 I   ALTER TABLE ONLY public.write_checks DROP CONSTRAINT fkb592e8ebdfe06a7f;
       public       postgres    false    261    490    4325            (           2606    28841    write_checks fkb592e8ebe5fcf475    FK CONSTRAINT     ?   ALTER TABLE ONLY public.write_checks
    ADD CONSTRAINT fkb592e8ebe5fcf475 FOREIGN KEY (account_id) REFERENCES public.account(id) DEFERRABLE INITIALLY DEFERRED;
 I   ALTER TABLE ONLY public.write_checks DROP CONSTRAINT fkb592e8ebe5fcf475;
       public       postgres    false    197    490    4243            ?           2606    26971    estimate fkb9d6152819f21c6b    FK CONSTRAINT     ?   ALTER TABLE ONLY public.estimate
    ADD CONSTRAINT fkb9d6152819f21c6b FOREIGN KEY (used_cashsales) REFERENCES public.cash_sales(id) DEFERRABLE INITIALLY DEFERRED;
 E   ALTER TABLE ONLY public.estimate DROP CONSTRAINT fkb9d6152819f21c6b;
       public       postgres    false    4291    290    236            ?           2606    26991    estimate fkb9d61528274bc854    FK CONSTRAINT     ?   ALTER TABLE ONLY public.estimate
    ADD CONSTRAINT fkb9d61528274bc854 FOREIGN KEY (id) REFERENCES public.transaction(id) DEFERRABLE INITIALLY DEFERRED;
 E   ALTER TABLE ONLY public.estimate DROP CONSTRAINT fkb9d61528274bc854;
       public       postgres    false    4553    290    443            ?           2606    27001    estimate fkb9d615284c74beae    FK CONSTRAINT     ?   ALTER TABLE ONLY public.estimate
    ADD CONSTRAINT fkb9d615284c74beae FOREIGN KEY (sales_person_id) REFERENCES public.sales_person(id) DEFERRABLE INITIALLY DEFERRED;
 E   ALTER TABLE ONLY public.estimate DROP CONSTRAINT fkb9d615284c74beae;
       public       postgres    false    402    4503    290            ?           2606    27006    estimate fkb9d6152884e959b3    FK CONSTRAINT     ?   ALTER TABLE ONLY public.estimate
    ADD CONSTRAINT fkb9d6152884e959b3 FOREIGN KEY (used_invoice) REFERENCES public.invoice(id) DEFERRABLE INITIALLY DEFERRED;
 E   ALTER TABLE ONLY public.estimate DROP CONSTRAINT fkb9d6152884e959b3;
       public       postgres    false    312    4389    290            ?           2606    27011    estimate fkb9d6152897edd458    FK CONSTRAINT     ?   ALTER TABLE ONLY public.estimate
    ADD CONSTRAINT fkb9d6152897edd458 FOREIGN KEY (shipping_terms_id) REFERENCES public.shippingterms(id) DEFERRABLE INITIALLY DEFERRED;
 E   ALTER TABLE ONLY public.estimate DROP CONSTRAINT fkb9d6152897edd458;
       public       postgres    false    4509    408    290            ?           2606    26976    estimate fkb9d615289a3059ec    FK CONSTRAINT     ?   ALTER TABLE ONLY public.estimate
    ADD CONSTRAINT fkb9d615289a3059ec FOREIGN KEY (price_level_id) REFERENCES public.pricelevel(id) DEFERRABLE INITIALLY DEFERRED;
 E   ALTER TABLE ONLY public.estimate DROP CONSTRAINT fkb9d615289a3059ec;
       public       postgres    false    384    4479    290            ?           2606    26981    estimate fkb9d61528ad0a95dc    FK CONSTRAINT     ?   ALTER TABLE ONLY public.estimate
    ADD CONSTRAINT fkb9d61528ad0a95dc FOREIGN KEY (shipping_method_id) REFERENCES public.shippingmethod(id) DEFERRABLE INITIALLY DEFERRED;
 E   ALTER TABLE ONLY public.estimate DROP CONSTRAINT fkb9d61528ad0a95dc;
       public       postgres    false    290    406    4507            ?           2606    26986    estimate fkb9d61528aea76b12    FK CONSTRAINT     ?   ALTER TABLE ONLY public.estimate
    ADD CONSTRAINT fkb9d61528aea76b12 FOREIGN KEY (payment_terms_id) REFERENCES public.paymentterms(id) DEFERRABLE INITIALLY DEFERRED;
 E   ALTER TABLE ONLY public.estimate DROP CONSTRAINT fkb9d61528aea76b12;
       public       postgres    false    290    4451    362            ?           2606    26996    estimate fkb9d61528dfe06a7f    FK CONSTRAINT     ?   ALTER TABLE ONLY public.estimate
    ADD CONSTRAINT fkb9d61528dfe06a7f FOREIGN KEY (customer_id) REFERENCES public.customer(id) DEFERRABLE INITIALLY DEFERRED;
 E   ALTER TABLE ONLY public.estimate DROP CONSTRAINT fkb9d61528dfe06a7f;
       public       postgres    false    261    4325    290                       2606    27506    make_deposit fkba78792d274bc854    FK CONSTRAINT     ?   ALTER TABLE ONLY public.make_deposit
    ADD CONSTRAINT fkba78792d274bc854 FOREIGN KEY (id) REFERENCES public.transaction(id) DEFERRABLE INITIALLY DEFERRED;
 I   ALTER TABLE ONLY public.make_deposit DROP CONSTRAINT fkba78792d274bc854;
       public       postgres    false    4553    443    339            ?           2606    26806 &   customer_prepayment fkbef0ad04274bc854    FK CONSTRAINT     ?   ALTER TABLE ONLY public.customer_prepayment
    ADD CONSTRAINT fkbef0ad04274bc854 FOREIGN KEY (id) REFERENCES public.transaction(id) DEFERRABLE INITIALLY DEFERRED;
 P   ALTER TABLE ONLY public.customer_prepayment DROP CONSTRAINT fkbef0ad04274bc854;
       public       postgres    false    443    265    4553            ?           2606    26801 &   customer_prepayment fkbef0ad0427ce4a9f    FK CONSTRAINT     ?   ALTER TABLE ONLY public.customer_prepayment
    ADD CONSTRAINT fkbef0ad0427ce4a9f FOREIGN KEY (depositin_id) REFERENCES public.account(id) DEFERRABLE INITIALLY DEFERRED;
 P   ALTER TABLE ONLY public.customer_prepayment DROP CONSTRAINT fkbef0ad0427ce4a9f;
       public       postgres    false    4243    265    197            ?           2606    26811 &   customer_prepayment fkbef0ad04dfe06a7f    FK CONSTRAINT     ?   ALTER TABLE ONLY public.customer_prepayment
    ADD CONSTRAINT fkbef0ad04dfe06a7f FOREIGN KEY (customer_id) REFERENCES public.customer(id) DEFERRABLE INITIALLY DEFERRED;
 P   ALTER TABLE ONLY public.customer_prepayment DROP CONSTRAINT fkbef0ad04dfe06a7f;
       public       postgres    false    4325    261    265            ?           2606    26911 -   employee_payhead_component fkc04b6c75458ec9b1    FK CONSTRAINT     ?   ALTER TABLE ONLY public.employee_payhead_component
    ADD CONSTRAINT fkc04b6c75458ec9b1 FOREIGN KEY (employee_payment_details_id) REFERENCES public.employee_payment_details(id) DEFERRABLE INITIALLY DEFERRED;
 W   ALTER TABLE ONLY public.employee_payhead_component DROP CONSTRAINT fkc04b6c75458ec9b1;
       public       postgres    false    284    4355    286            ?           2606    26916 -   employee_payhead_component fkc04b6c757ebcab9a    FK CONSTRAINT     ?   ALTER TABLE ONLY public.employee_payhead_component
    ADD CONSTRAINT fkc04b6c757ebcab9a FOREIGN KEY (pay_head) REFERENCES public.pay_head(id) DEFERRABLE INITIALLY DEFERRED;
 W   ALTER TABLE ONLY public.employee_payhead_component DROP CONSTRAINT fkc04b6c757ebcab9a;
       public       postgres    false    284    369    4461                       2606    26206 "   account_amounts fkc18da6c9e5fcf475    FK CONSTRAINT     ?   ALTER TABLE ONLY public.account_amounts
    ADD CONSTRAINT fkc18da6c9e5fcf475 FOREIGN KEY (account_id) REFERENCES public.account(id) DEFERRABLE INITIALLY DEFERRED;
 L   ALTER TABLE ONLY public.account_amounts DROP CONSTRAINT fkc18da6c9e5fcf475;
       public       postgres    false    197    4243    200                       2606    28756 %   vendor_credit_memo fkc3d9e649274bc854    FK CONSTRAINT     ?   ALTER TABLE ONLY public.vendor_credit_memo
    ADD CONSTRAINT fkc3d9e649274bc854 FOREIGN KEY (id) REFERENCES public.transaction(id) DEFERRABLE INITIALLY DEFERRED;
 O   ALTER TABLE ONLY public.vendor_credit_memo DROP CONSTRAINT fkc3d9e649274bc854;
       public       postgres    false    4553    481    443                       2606    28751 %   vendor_credit_memo fkc3d9e649891a177f    FK CONSTRAINT     ?   ALTER TABLE ONLY public.vendor_credit_memo
    ADD CONSTRAINT fkc3d9e649891a177f FOREIGN KEY (vendor_id) REFERENCES public.vendor(id) DEFERRABLE INITIALLY DEFERRED;
 O   ALTER TABLE ONLY public.vendor_credit_memo DROP CONSTRAINT fkc3d9e649891a177f;
       public       postgres    false    4595    481    480            ^           2606    27826    pricelevel fkc6643bdb622c1275    FK CONSTRAINT     ?   ALTER TABLE ONLY public.pricelevel
    ADD CONSTRAINT fkc6643bdb622c1275 FOREIGN KEY (company_id) REFERENCES public.company(id) DEFERRABLE INITIALLY DEFERRED;
 G   ALTER TABLE ONLY public.pricelevel DROP CONSTRAINT fkc6643bdb622c1275;
       public       postgres    false    384    250    4309            `           2606    27836    pricelevel fkc6643bdb9e5a0e30    FK CONSTRAINT     ?   ALTER TABLE ONLY public.pricelevel
    ADD CONSTRAINT fkc6643bdb9e5a0e30 FOREIGN KEY (last_modifier) REFERENCES public.users(id) DEFERRABLE INITIALLY DEFERRED;
 G   ALTER TABLE ONLY public.pricelevel DROP CONSTRAINT fkc6643bdb9e5a0e30;
       public       postgres    false    384    474    4585            _           2606    27831    pricelevel fkc6643bdbf1ae8cde    FK CONSTRAINT     ?   ALTER TABLE ONLY public.pricelevel
    ADD CONSTRAINT fkc6643bdbf1ae8cde FOREIGN KEY (created_by) REFERENCES public.users(id) DEFERRABLE INITIALLY DEFERRED;
 G   ALTER TABLE ONLY public.pricelevel DROP CONSTRAINT fkc6643bdbf1ae8cde;
       public       postgres    false    384    474    4585            ]           2606    27821 -   portlet_page_configuration fkc6698dab9745d1df    FK CONSTRAINT     ?   ALTER TABLE ONLY public.portlet_page_configuration
    ADD CONSTRAINT fkc6698dab9745d1df FOREIGN KEY (user_id) REFERENCES public.users(id) DEFERRABLE INITIALLY DEFERRED;
 W   ALTER TABLE ONLY public.portlet_page_configuration DROP CONSTRAINT fkc6698dab9745d1df;
       public       postgres    false    382    474    4585                       2606    27431    i_m_user fkc96d8d3358dbf25b    FK CONSTRAINT     ?   ALTER TABLE ONLY public.i_m_user
    ADD CONSTRAINT fkc96d8d3358dbf25b FOREIGN KEY (client) REFERENCES public.client(id) DEFERRABLE INITIALLY DEFERRED;
 E   ALTER TABLE ONLY public.i_m_user DROP CONSTRAINT fkc96d8d3358dbf25b;
       public       postgres    false    322    241    4299                        2606    28636 *   transaction_receive_vat fkc9bdf78cb02eec58    FK CONSTRAINT     ?   ALTER TABLE ONLY public.transaction_receive_vat
    ADD CONSTRAINT fkc9bdf78cb02eec58 FOREIGN KEY (receive_vat_id) REFERENCES public.receive_vat(id) DEFERRABLE INITIALLY DEFERRED;
 T   ALTER TABLE ONLY public.transaction_receive_vat DROP CONSTRAINT fkc9bdf78cb02eec58;
       public       postgres    false    388    467    4487                       2606    28641 *   transaction_receive_vat fkc9bdf78cc368d6ac    FK CONSTRAINT     ?   ALTER TABLE ONLY public.transaction_receive_vat
    ADD CONSTRAINT fkc9bdf78cc368d6ac FOREIGN KEY (tax_agency_id) REFERENCES public.taxagency(id) DEFERRABLE INITIALLY DEFERRED;
 T   ALTER TABLE ONLY public.transaction_receive_vat DROP CONSTRAINT fkc9bdf78cc368d6ac;
       public       postgres    false    419    467    4523                       2606    28646 )   transaction_receive_vat fkc9bdf78cf5cc2cc    FK CONSTRAINT     ?   ALTER TABLE ONLY public.transaction_receive_vat
    ADD CONSTRAINT fkc9bdf78cf5cc2cc FOREIGN KEY (tax_return_id) REFERENCES public.tax_return(id) DEFERRABLE INITIALLY DEFERRED;
 S   ALTER TABLE ONLY public.transaction_receive_vat DROP CONSTRAINT fkc9bdf78cf5cc2cc;
       public       postgres    false    432    467    4541            I           2606    26441    cash_sales fkca27b4e0172fffee    FK CONSTRAINT     ?   ALTER TABLE ONLY public.cash_sales
    ADD CONSTRAINT fkca27b4e0172fffee FOREIGN KEY (deposit_in_account_id) REFERENCES public.account(id) DEFERRABLE INITIALLY DEFERRED;
 G   ALTER TABLE ONLY public.cash_sales DROP CONSTRAINT fkca27b4e0172fffee;
       public       postgres    false    236    4243    197            J           2606    26446    cash_sales fkca27b4e0274bc854    FK CONSTRAINT     ?   ALTER TABLE ONLY public.cash_sales
    ADD CONSTRAINT fkca27b4e0274bc854 FOREIGN KEY (id) REFERENCES public.transaction(id) DEFERRABLE INITIALLY DEFERRED;
 G   ALTER TABLE ONLY public.cash_sales DROP CONSTRAINT fkca27b4e0274bc854;
       public       postgres    false    236    4553    443            L           2606    26456    cash_sales fkca27b4e04c74beae    FK CONSTRAINT     ?   ALTER TABLE ONLY public.cash_sales
    ADD CONSTRAINT fkca27b4e04c74beae FOREIGN KEY (sales_person_id) REFERENCES public.sales_person(id) DEFERRABLE INITIALLY DEFERRED;
 G   ALTER TABLE ONLY public.cash_sales DROP CONSTRAINT fkca27b4e04c74beae;
       public       postgres    false    236    4503    402            M           2606    26461    cash_sales fkca27b4e097edd458    FK CONSTRAINT     ?   ALTER TABLE ONLY public.cash_sales
    ADD CONSTRAINT fkca27b4e097edd458 FOREIGN KEY (shipping_terms_id) REFERENCES public.shippingterms(id) DEFERRABLE INITIALLY DEFERRED;
 G   ALTER TABLE ONLY public.cash_sales DROP CONSTRAINT fkca27b4e097edd458;
       public       postgres    false    236    408    4509            G           2606    26431    cash_sales fkca27b4e09a3059ec    FK CONSTRAINT     ?   ALTER TABLE ONLY public.cash_sales
    ADD CONSTRAINT fkca27b4e09a3059ec FOREIGN KEY (price_level_id) REFERENCES public.pricelevel(id) DEFERRABLE INITIALLY DEFERRED;
 G   ALTER TABLE ONLY public.cash_sales DROP CONSTRAINT fkca27b4e09a3059ec;
       public       postgres    false    236    4479    384            H           2606    26436    cash_sales fkca27b4e0ad0a95dc    FK CONSTRAINT     ?   ALTER TABLE ONLY public.cash_sales
    ADD CONSTRAINT fkca27b4e0ad0a95dc FOREIGN KEY (shipping_method_id) REFERENCES public.shippingmethod(id) DEFERRABLE INITIALLY DEFERRED;
 G   ALTER TABLE ONLY public.cash_sales DROP CONSTRAINT fkca27b4e0ad0a95dc;
       public       postgres    false    4507    406    236            K           2606    26451    cash_sales fkca27b4e0dfe06a7f    FK CONSTRAINT     ?   ALTER TABLE ONLY public.cash_sales
    ADD CONSTRAINT fkca27b4e0dfe06a7f FOREIGN KEY (customer_id) REFERENCES public.customer(id) DEFERRABLE INITIALLY DEFERRED;
 G   ALTER TABLE ONLY public.cash_sales DROP CONSTRAINT fkca27b4e0dfe06a7f;
       public       postgres    false    236    4325    261                        2606    26236    activity fkcbf1e30f622c1275    FK CONSTRAINT     ?   ALTER TABLE ONLY public.activity
    ADD CONSTRAINT fkcbf1e30f622c1275 FOREIGN KEY (company_id) REFERENCES public.company(id) DEFERRABLE INITIALLY DEFERRED;
 E   ALTER TABLE ONLY public.activity DROP CONSTRAINT fkcbf1e30f622c1275;
       public       postgres    false    4309    250    206            \           2606    27816 4   portlet_configuration_portletdata fkcc101476ee3f583c    FK CONSTRAINT     ?   ALTER TABLE ONLY public.portlet_configuration_portletdata
    ADD CONSTRAINT fkcc101476ee3f583c FOREIGN KEY (id) REFERENCES public.portlet_configuration(id) DEFERRABLE INITIALLY DEFERRED;
 ^   ALTER TABLE ONLY public.portlet_configuration_portletdata DROP CONSTRAINT fkcc101476ee3f583c;
       public       postgres    false    380    379    4473            ?           2606    27121 #   formula_function fkcded46b1411c3a74    FK CONSTRAINT     ?   ALTER TABLE ONLY public.formula_function
    ADD CONSTRAINT fkcded46b1411c3a74 FOREIGN KEY (attendance_type) REFERENCES public.attendance_or_production_tpe(id) DEFERRABLE INITIALLY DEFERRED;
 M   ALTER TABLE ONLY public.formula_function DROP CONSTRAINT fkcded46b1411c3a74;
       public       postgres    false    4269    300    221            ?           2606    27111 #   formula_function fkcded46b17ebcab9a    FK CONSTRAINT     ?   ALTER TABLE ONLY public.formula_function
    ADD CONSTRAINT fkcded46b17ebcab9a FOREIGN KEY (pay_head) REFERENCES public.pay_head(id) DEFERRABLE INITIALLY DEFERRED;
 M   ALTER TABLE ONLY public.formula_function DROP CONSTRAINT fkcded46b17ebcab9a;
       public       postgres    false    300    4461    369            ?           2606    27116 #   formula_function fkcded46b19b5089b6    FK CONSTRAINT     ?   ALTER TABLE ONLY public.formula_function
    ADD CONSTRAINT fkcded46b19b5089b6 FOREIGN KEY (pay_head_id) REFERENCES public.computation_pay_head(id) DEFERRABLE INITIALLY DEFERRED;
 M   ALTER TABLE ONLY public.formula_function DROP CONSTRAINT fkcded46b19b5089b6;
       public       postgres    false    300    4313    252            8           2606    46086 &   live_tax_rate_range fkcf8c0de56992f26d    FK CONSTRAINT     ?   ALTER TABLE ONLY public.live_tax_rate_range
    ADD CONSTRAINT fkcf8c0de56992f26d FOREIGN KEY (live_tax_rate_id) REFERENCES public.live_tax_rate(id) DEFERRABLE INITIALLY DEFERRED;
 P   ALTER TABLE ONLY public.live_tax_rate_range DROP CONSTRAINT fkcf8c0de56992f26d;
       public       postgres    false    502    504    4617            9           2606    46136 &   live_tax_rate_range fkcf8c0de5ceb696af    FK CONSTRAINT     ?   ALTER TABLE ONLY public.live_tax_rate_range
    ADD CONSTRAINT fkcf8c0de5ceb696af FOREIGN KEY (live_tax_rate_range_id) REFERENCES public.live_tax_rate(id) DEFERRABLE INITIALLY DEFERRED;
 P   ALTER TABLE ONLY public.live_tax_rate_range DROP CONSTRAINT fkcf8c0de5ceb696af;
       public       postgres    false    4617    504    502            ?           2606    27126 %   inventory_assembly fkd175d989a0058033    FK CONSTRAINT     ?   ALTER TABLE ONLY public.inventory_assembly
    ADD CONSTRAINT fkd175d989a0058033 FOREIGN KEY (id) REFERENCES public.item(id) DEFERRABLE INITIALLY DEFERRED;
 O   ALTER TABLE ONLY public.inventory_assembly DROP CONSTRAINT fkd175d989a0058033;
       public       postgres    false    4393    303    315            ?           2606    28061 #   stock_adjustment fkd2b15a56274bc854    FK CONSTRAINT     ?   ALTER TABLE ONLY public.stock_adjustment
    ADD CONSTRAINT fkd2b15a56274bc854 FOREIGN KEY (id) REFERENCES public.transaction(id) DEFERRABLE INITIALLY DEFERRED;
 M   ALTER TABLE ONLY public.stock_adjustment DROP CONSTRAINT fkd2b15a56274bc854;
       public       postgres    false    413    443    4553            ?           2606    28051 #   stock_adjustment fkd2b15a56622c1275    FK CONSTRAINT     ?   ALTER TABLE ONLY public.stock_adjustment
    ADD CONSTRAINT fkd2b15a56622c1275 FOREIGN KEY (company_id) REFERENCES public.company(id) DEFERRABLE INITIALLY DEFERRED;
 M   ALTER TABLE ONLY public.stock_adjustment DROP CONSTRAINT fkd2b15a56622c1275;
       public       postgres    false    413    250    4309            ?           2606    28056 #   stock_adjustment fkd2b15a5687003963    FK CONSTRAINT     ?   ALTER TABLE ONLY public.stock_adjustment
    ADD CONSTRAINT fkd2b15a5687003963 FOREIGN KEY (adjustment_account) REFERENCES public.account(id) DEFERRABLE INITIALLY DEFERRED;
 M   ALTER TABLE ONLY public.stock_adjustment DROP CONSTRAINT fkd2b15a5687003963;
       public       postgres    false    413    197    4243            ?           2606    28066 #   stock_adjustment fkd2b15a56984db8a1    FK CONSTRAINT     ?   ALTER TABLE ONLY public.stock_adjustment
    ADD CONSTRAINT fkd2b15a56984db8a1 FOREIGN KEY (warehouse) REFERENCES public.warehouse(id) DEFERRABLE INITIALLY DEFERRED;
 M   ALTER TABLE ONLY public.stock_adjustment DROP CONSTRAINT fkd2b15a56984db8a1;
       public       postgres    false    413    488    4605            ?           2606    28076 #   stock_adjustment fkd2b15a569e5a0e30    FK CONSTRAINT     ?   ALTER TABLE ONLY public.stock_adjustment
    ADD CONSTRAINT fkd2b15a569e5a0e30 FOREIGN KEY (last_modifier) REFERENCES public.users(id) DEFERRABLE INITIALLY DEFERRED;
 M   ALTER TABLE ONLY public.stock_adjustment DROP CONSTRAINT fkd2b15a569e5a0e30;
       public       postgres    false    413    474    4585            ?           2606    28071 #   stock_adjustment fkd2b15a56f1ae8cde    FK CONSTRAINT     ?   ALTER TABLE ONLY public.stock_adjustment
    ADD CONSTRAINT fkd2b15a56f1ae8cde FOREIGN KEY (created_by) REFERENCES public.users(id) DEFERRABLE INITIALLY DEFERRED;
 M   ALTER TABLE ONLY public.stock_adjustment DROP CONSTRAINT fkd2b15a56f1ae8cde;
       public       postgres    false    413    474    4585            <           2606    26376 !   build_assembly fkd387c8b7274bc854    FK CONSTRAINT     ?   ALTER TABLE ONLY public.build_assembly
    ADD CONSTRAINT fkd387c8b7274bc854 FOREIGN KEY (id) REFERENCES public.transaction(id) DEFERRABLE INITIALLY DEFERRED;
 K   ALTER TABLE ONLY public.build_assembly DROP CONSTRAINT fkd387c8b7274bc854;
       public       postgres    false    4553    232    443            =           2606    26381 !   build_assembly fkd387c8b7f04fb4e6    FK CONSTRAINT     ?   ALTER TABLE ONLY public.build_assembly
    ADD CONSTRAINT fkd387c8b7f04fb4e6 FOREIGN KEY (inventory_assembly) REFERENCES public.inventory_assembly(id) DEFERRABLE INITIALLY DEFERRED;
 K   ALTER TABLE ONLY public.build_assembly DROP CONSTRAINT fkd387c8b7f04fb4e6;
       public       postgres    false    303    232    4379                       2606    26186 "   accounter_class fkd602e053622c1275    FK CONSTRAINT     ?   ALTER TABLE ONLY public.accounter_class
    ADD CONSTRAINT fkd602e053622c1275 FOREIGN KEY (company_id) REFERENCES public.company(id) DEFERRABLE INITIALLY DEFERRED;
 L   ALTER TABLE ONLY public.accounter_class DROP CONSTRAINT fkd602e053622c1275;
       public       postgres    false    199    250    4309                       2606    26191 "   accounter_class fkd602e05391eed093    FK CONSTRAINT     ?   ALTER TABLE ONLY public.accounter_class
    ADD CONSTRAINT fkd602e05391eed093 FOREIGN KEY (parent_id) REFERENCES public.accounter_class(id) DEFERRABLE INITIALLY DEFERRED;
 L   ALTER TABLE ONLY public.accounter_class DROP CONSTRAINT fkd602e05391eed093;
       public       postgres    false    199    199    4245                       2606    26201 "   accounter_class fkd602e0539e5a0e30    FK CONSTRAINT     ?   ALTER TABLE ONLY public.accounter_class
    ADD CONSTRAINT fkd602e0539e5a0e30 FOREIGN KEY (last_modifier) REFERENCES public.users(id) DEFERRABLE INITIALLY DEFERRED;
 L   ALTER TABLE ONLY public.accounter_class DROP CONSTRAINT fkd602e0539e5a0e30;
       public       postgres    false    474    199    4585                       2606    26196 "   accounter_class fkd602e053f1ae8cde    FK CONSTRAINT     ?   ALTER TABLE ONLY public.accounter_class
    ADD CONSTRAINT fkd602e053f1ae8cde FOREIGN KEY (created_by) REFERENCES public.users(id) DEFERRABLE INITIALLY DEFERRED;
 L   ALTER TABLE ONLY public.accounter_class DROP CONSTRAINT fkd602e053f1ae8cde;
       public       postgres    false    199    474    4585            ?           2606    27106 %   flat_rate_pay_head fkda0509702d16b99e    FK CONSTRAINT     ?   ALTER TABLE ONLY public.flat_rate_pay_head
    ADD CONSTRAINT fkda0509702d16b99e FOREIGN KEY (id) REFERENCES public.pay_head(id) DEFERRABLE INITIALLY DEFERRED;
 O   ALTER TABLE ONLY public.flat_rate_pay_head DROP CONSTRAINT fkda0509702d16b99e;
       public       postgres    false    4461    369    299            ?           2606    28011     shippingterms fkdb42e079622c1275    FK CONSTRAINT     ?   ALTER TABLE ONLY public.shippingterms
    ADD CONSTRAINT fkdb42e079622c1275 FOREIGN KEY (company_id) REFERENCES public.company(id) DEFERRABLE INITIALLY DEFERRED;
 J   ALTER TABLE ONLY public.shippingterms DROP CONSTRAINT fkdb42e079622c1275;
       public       postgres    false    408    250    4309            ?           2606    28021     shippingterms fkdb42e0799e5a0e30    FK CONSTRAINT     ?   ALTER TABLE ONLY public.shippingterms
    ADD CONSTRAINT fkdb42e0799e5a0e30 FOREIGN KEY (last_modifier) REFERENCES public.users(id) DEFERRABLE INITIALLY DEFERRED;
 J   ALTER TABLE ONLY public.shippingterms DROP CONSTRAINT fkdb42e0799e5a0e30;
       public       postgres    false    408    474    4585            ?           2606    28016     shippingterms fkdb42e079f1ae8cde    FK CONSTRAINT     ?   ALTER TABLE ONLY public.shippingterms
    ADD CONSTRAINT fkdb42e079f1ae8cde FOREIGN KEY (created_by) REFERENCES public.users(id) DEFERRABLE INITIALLY DEFERRED;
 J   ALTER TABLE ONLY public.shippingterms DROP CONSTRAINT fkdb42e079f1ae8cde;
       public       postgres    false    408    474    4585            ?           2606    28176    tax_group fkdbf090ab622c1275    FK CONSTRAINT     ?   ALTER TABLE ONLY public.tax_group
    ADD CONSTRAINT fkdbf090ab622c1275 FOREIGN KEY (company_id) REFERENCES public.company(id) DEFERRABLE INITIALLY DEFERRED;
 F   ALTER TABLE ONLY public.tax_group DROP CONSTRAINT fkdbf090ab622c1275;
       public       postgres    false    425    250    4309            ?           2606    28181    tax_group fkdbf090ab880103e1    FK CONSTRAINT     ?   ALTER TABLE ONLY public.tax_group
    ADD CONSTRAINT fkdbf090ab880103e1 FOREIGN KEY (id) REFERENCES public.tax_item_groups(id) DEFERRABLE INITIALLY DEFERRED;
 F   ALTER TABLE ONLY public.tax_group DROP CONSTRAINT fkdbf090ab880103e1;
       public       postgres    false    425    429    4537            ?           2606    27016    expense fkdcc05438274bc854    FK CONSTRAINT     ?   ALTER TABLE ONLY public.expense
    ADD CONSTRAINT fkdcc05438274bc854 FOREIGN KEY (id) REFERENCES public.transaction(id) DEFERRABLE INITIALLY DEFERRED;
 D   ALTER TABLE ONLY public.expense DROP CONSTRAINT fkdcc05438274bc854;
       public       postgres    false    443    4553    291                       2606    26161    account fke49f160d622c1275    FK CONSTRAINT     ?   ALTER TABLE ONLY public.account
    ADD CONSTRAINT fke49f160d622c1275 FOREIGN KEY (company_id) REFERENCES public.company(id) DEFERRABLE INITIALLY DEFERRED;
 D   ALTER TABLE ONLY public.account DROP CONSTRAINT fke49f160d622c1275;
       public       postgres    false    197    4309    250                       2606    26166    account fke49f160d914588d8    FK CONSTRAINT     ?   ALTER TABLE ONLY public.account
    ADD CONSTRAINT fke49f160d914588d8 FOREIGN KEY (parent_id) REFERENCES public.account(id) DEFERRABLE INITIALLY DEFERRED;
 D   ALTER TABLE ONLY public.account DROP CONSTRAINT fke49f160d914588d8;
       public       postgres    false    197    4243    197                       2606    26156    account fke49f160d9acc1779    FK CONSTRAINT     ?   ALTER TABLE ONLY public.account
    ADD CONSTRAINT fke49f160d9acc1779 FOREIGN KEY (account_currency) REFERENCES public.currency(id) DEFERRABLE INITIALLY DEFERRED;
 D   ALTER TABLE ONLY public.account DROP CONSTRAINT fke49f160d9acc1779;
       public       postgres    false    260    197    4323                       2606    26181    account fke49f160d9e5a0e30    FK CONSTRAINT     ?   ALTER TABLE ONLY public.account
    ADD CONSTRAINT fke49f160d9e5a0e30 FOREIGN KEY (last_modifier) REFERENCES public.users(id) DEFERRABLE INITIALLY DEFERRED;
 D   ALTER TABLE ONLY public.account DROP CONSTRAINT fke49f160d9e5a0e30;
       public       postgres    false    474    197    4585                       2606    26176    account fke49f160db55b4109    FK CONSTRAINT     ?   ALTER TABLE ONLY public.account
    ADD CONSTRAINT fke49f160db55b4109 FOREIGN KEY (linked_id) REFERENCES public.account(id) DEFERRABLE INITIALLY DEFERRED;
 D   ALTER TABLE ONLY public.account DROP CONSTRAINT fke49f160db55b4109;
       public       postgres    false    197    4243    197                       2606    26171    account fke49f160df1ae8cde    FK CONSTRAINT     ?   ALTER TABLE ONLY public.account
    ADD CONSTRAINT fke49f160df1ae8cde FOREIGN KEY (created_by) REFERENCES public.users(id) DEFERRABLE INITIALLY DEFERRED;
 D   ALTER TABLE ONLY public.account DROP CONSTRAINT fke49f160df1ae8cde;
       public       postgres    false    197    4585    474                       2606    27456     journal_entry fke894c12a274bc854    FK CONSTRAINT     ?   ALTER TABLE ONLY public.journal_entry
    ADD CONSTRAINT fke894c12a274bc854 FOREIGN KEY (id) REFERENCES public.transaction(id) DEFERRABLE INITIALLY DEFERRED;
 J   ALTER TABLE ONLY public.journal_entry DROP CONSTRAINT fke894c12a274bc854;
       public       postgres    false    325    443    4553                       2606    27461     journal_entry fke894c12ab2fc5555    FK CONSTRAINT     ?   ALTER TABLE ONLY public.journal_entry
    ADD CONSTRAINT fke894c12ab2fc5555 FOREIGN KEY (payee_id) REFERENCES public.payee(id) DEFERRABLE INITIALLY DEFERRED;
 J   ALTER TABLE ONLY public.journal_entry DROP CONSTRAINT fke894c12ab2fc5555;
       public       postgres    false    325    4439    353                       2606    27451     journal_entry fke894c12ae5fcf475    FK CONSTRAINT     ?   ALTER TABLE ONLY public.journal_entry
    ADD CONSTRAINT fke894c12ae5fcf475 FOREIGN KEY (account_id) REFERENCES public.account(id) DEFERRABLE INITIALLY DEFERRED;
 J   ALTER TABLE ONLY public.journal_entry DROP CONSTRAINT fke894c12ae5fcf475;
       public       postgres    false    325    4243    197            &           2606    28831 $   warehouse_transfer fkeb2d607274bc854    FK CONSTRAINT     ?   ALTER TABLE ONLY public.warehouse_transfer
    ADD CONSTRAINT fkeb2d607274bc854 FOREIGN KEY (id) REFERENCES public.transaction(id) DEFERRABLE INITIALLY DEFERRED;
 N   ALTER TABLE ONLY public.warehouse_transfer DROP CONSTRAINT fkeb2d607274bc854;
       public       postgres    false    4553    489    443            $           2606    28821 $   warehouse_transfer fkeb2d6072facbe4c    FK CONSTRAINT     ?   ALTER TABLE ONLY public.warehouse_transfer
    ADD CONSTRAINT fkeb2d6072facbe4c FOREIGN KEY (from_warehouse) REFERENCES public.warehouse(id) DEFERRABLE INITIALLY DEFERRED;
 N   ALTER TABLE ONLY public.warehouse_transfer DROP CONSTRAINT fkeb2d6072facbe4c;
       public       postgres    false    4605    489    488            %           2606    28826 $   warehouse_transfer fkeb2d6074b3723dd    FK CONSTRAINT     ?   ALTER TABLE ONLY public.warehouse_transfer
    ADD CONSTRAINT fkeb2d6074b3723dd FOREIGN KEY (to_warehouse) REFERENCES public.warehouse(id) DEFERRABLE INITIALLY DEFERRED;
 N   ALTER TABLE ONLY public.warehouse_transfer DROP CONSTRAINT fkeb2d6074b3723dd;
       public       postgres    false    489    488    4605            ?           2606    26951    enter_bill fkeddd392e274bc854    FK CONSTRAINT     ?   ALTER TABLE ONLY public.enter_bill
    ADD CONSTRAINT fkeddd392e274bc854 FOREIGN KEY (id) REFERENCES public.transaction(id) DEFERRABLE INITIALLY DEFERRED;
 G   ALTER TABLE ONLY public.enter_bill DROP CONSTRAINT fkeddd392e274bc854;
       public       postgres    false    443    288    4553            ?           2606    26956    enter_bill fkeddd392e338bd6bb    FK CONSTRAINT     ?   ALTER TABLE ONLY public.enter_bill
    ADD CONSTRAINT fkeddd392e338bd6bb FOREIGN KEY (payment_term_id) REFERENCES public.paymentterms(id) DEFERRABLE INITIALLY DEFERRED;
 G   ALTER TABLE ONLY public.enter_bill DROP CONSTRAINT fkeddd392e338bd6bb;
       public       postgres    false    288    362    4451            ?           2606    26946    enter_bill fkeddd392e891a177f    FK CONSTRAINT     ?   ALTER TABLE ONLY public.enter_bill
    ADD CONSTRAINT fkeddd392e891a177f FOREIGN KEY (vendor_id) REFERENCES public.vendor(id) DEFERRABLE INITIALLY DEFERRED;
 G   ALTER TABLE ONLY public.enter_bill DROP CONSTRAINT fkeddd392e891a177f;
       public       postgres    false    288    4595    480            ?           2606    26941    enter_bill fkeddd392ea936e12e    FK CONSTRAINT     ?   ALTER TABLE ONLY public.enter_bill
    ADD CONSTRAINT fkeddd392ea936e12e FOREIGN KEY (item_receipt_id) REFERENCES public.item_receipt(id) DEFERRABLE INITIALLY DEFERRED;
 G   ALTER TABLE ONLY public.enter_bill DROP CONSTRAINT fkeddd392ea936e12e;
       public       postgres    false    288    4397    318            ?           2606    27256 $   invoice_estimates fkeeb5ad792843ee3f    FK CONSTRAINT     ?   ALTER TABLE ONLY public.invoice_estimates
    ADD CONSTRAINT fkeeb5ad792843ee3f FOREIGN KEY (estimate_id) REFERENCES public.estimate(id) DEFERRABLE INITIALLY DEFERRED;
 N   ALTER TABLE ONLY public.invoice_estimates DROP CONSTRAINT fkeeb5ad792843ee3f;
       public       postgres    false    290    4363    313            ?           2606    27261 $   invoice_estimates fkeeb5ad7942d65475    FK CONSTRAINT     ?   ALTER TABLE ONLY public.invoice_estimates
    ADD CONSTRAINT fkeeb5ad7942d65475 FOREIGN KEY (invoice_id) REFERENCES public.invoice(id) DEFERRABLE INITIALLY DEFERRED;
 N   ALTER TABLE ONLY public.invoice_estimates DROP CONSTRAINT fkeeb5ad7942d65475;
       public       postgres    false    313    4389    312            ;           2606    26371    budgetitem fkf3d2ebd844247a5f    FK CONSTRAINT     ?   ALTER TABLE ONLY public.budgetitem
    ADD CONSTRAINT fkf3d2ebd844247a5f FOREIGN KEY (budget_id) REFERENCES public.budget(id) DEFERRABLE INITIALLY DEFERRED;
 G   ALTER TABLE ONLY public.budgetitem DROP CONSTRAINT fkf3d2ebd844247a5f;
       public       postgres    false    231    229    4279            :           2606    26366    budgetitem fkf3d2ebd8622c1275    FK CONSTRAINT     ?   ALTER TABLE ONLY public.budgetitem
    ADD CONSTRAINT fkf3d2ebd8622c1275 FOREIGN KEY (company_id) REFERENCES public.company(id) DEFERRABLE INITIALLY DEFERRED;
 G   ALTER TABLE ONLY public.budgetitem DROP CONSTRAINT fkf3d2ebd8622c1275;
       public       postgres    false    4309    231    250                       2606    27481 "   license_purchase fkf41ab5f384ae49f    FK CONSTRAINT     ?   ALTER TABLE ONLY public.license_purchase
    ADD CONSTRAINT fkf41ab5f384ae49f FOREIGN KEY (client_id) REFERENCES public.client(id) DEFERRABLE INITIALLY DEFERRED;
 L   ALTER TABLE ONLY public.license_purchase DROP CONSTRAINT fkf41ab5f384ae49f;
       public       postgres    false    4299    241    333            ;           2606    46505 +   tax_computation_pay_head fkf5876b032d16b99e    FK CONSTRAINT     ?   ALTER TABLE ONLY public.tax_computation_pay_head
    ADD CONSTRAINT fkf5876b032d16b99e FOREIGN KEY (id) REFERENCES public.pay_head(id) DEFERRABLE INITIALLY DEFERRED;
 U   ALTER TABLE ONLY public.tax_computation_pay_head DROP CONSTRAINT fkf5876b032d16b99e;
       public       postgres    false    369    505    4461            :           2606    46500 +   tax_computation_pay_head fkf5876b03c3e203e1    FK CONSTRAINT     ?   ALTER TABLE ONLY public.tax_computation_pay_head
    ADD CONSTRAINT fkf5876b03c3e203e1 FOREIGN KEY (live_tax_rate) REFERENCES public.live_tax_rate(id) DEFERRABLE INITIALLY DEFERRED;
 U   ALTER TABLE ONLY public.tax_computation_pay_head DROP CONSTRAINT fkf5876b03c3e203e1;
       public       postgres    false    502    505    4617            @           2606    26396     cash_purchase fkf7279c8d274bc854    FK CONSTRAINT     ?   ALTER TABLE ONLY public.cash_purchase
    ADD CONSTRAINT fkf7279c8d274bc854 FOREIGN KEY (id) REFERENCES public.transaction(id) DEFERRABLE INITIALLY DEFERRED;
 J   ALTER TABLE ONLY public.cash_purchase DROP CONSTRAINT fkf7279c8d274bc854;
       public       postgres    false    443    233    4553            A           2606    26401     cash_purchase fkf7279c8d68a08e82    FK CONSTRAINT     ?   ALTER TABLE ONLY public.cash_purchase
    ADD CONSTRAINT fkf7279c8d68a08e82 FOREIGN KEY (payfrom_account_id) REFERENCES public.account(id) DEFERRABLE INITIALLY DEFERRED;
 J   ALTER TABLE ONLY public.cash_purchase DROP CONSTRAINT fkf7279c8d68a08e82;
       public       postgres    false    197    233    4243            ?           2606    26391     cash_purchase fkf7279c8d891a177f    FK CONSTRAINT     ?   ALTER TABLE ONLY public.cash_purchase
    ADD CONSTRAINT fkf7279c8d891a177f FOREIGN KEY (vendor_id) REFERENCES public.vendor(id) DEFERRABLE INITIALLY DEFERRED;
 J   ALTER TABLE ONLY public.cash_purchase DROP CONSTRAINT fkf7279c8d891a177f;
       public       postgres    false    480    233    4595            >           2606    26386     cash_purchase fkf7279c8da133fb48    FK CONSTRAINT     ?   ALTER TABLE ONLY public.cash_purchase
    ADD CONSTRAINT fkf7279c8da133fb48 FOREIGN KEY (cash_expense_account_id) REFERENCES public.account(id) DEFERRABLE INITIALLY DEFERRED;
 J   ALTER TABLE ONLY public.cash_purchase DROP CONSTRAINT fkf7279c8da133fb48;
       public       postgres    false    197    233    4243            B           2606    26406     cash_purchase fkf7279c8de72dca7e    FK CONSTRAINT     ?   ALTER TABLE ONLY public.cash_purchase
    ADD CONSTRAINT fkf7279c8de72dca7e FOREIGN KEY (employee) REFERENCES public.users(id) DEFERRABLE INITIALLY DEFERRED;
 J   ALTER TABLE ONLY public.cash_purchase DROP CONSTRAINT fkf7279c8de72dca7e;
       public       postgres    false    474    233    4585            C           2606    26411 *   cash_purchase_estimates fkf9789af9b0901b5a    FK CONSTRAINT     ?   ALTER TABLE ONLY public.cash_purchase_estimates
    ADD CONSTRAINT fkf9789af9b0901b5a FOREIGN KEY (elt) REFERENCES public.estimate(id) DEFERRABLE INITIALLY DEFERRED;
 T   ALTER TABLE ONLY public.cash_purchase_estimates DROP CONSTRAINT fkf9789af9b0901b5a;
       public       postgres    false    290    4363    234            D           2606    26416 *   cash_purchase_estimates fkf9789af9baae8a66    FK CONSTRAINT     ?   ALTER TABLE ONLY public.cash_purchase_estimates
    ADD CONSTRAINT fkf9789af9baae8a66 FOREIGN KEY (cash_purchase_id) REFERENCES public.cash_purchase(id) DEFERRABLE INITIALLY DEFERRED;
 T   ALTER TABLE ONLY public.cash_purchase_estimates DROP CONSTRAINT fkf9789af9baae8a66;
       public       postgres    false    234    4285    233                       2606    28716    vatreturnbox fkfab41192622c1275    FK CONSTRAINT     ?   ALTER TABLE ONLY public.vatreturnbox
    ADD CONSTRAINT fkfab41192622c1275 FOREIGN KEY (company_id) REFERENCES public.company(id) DEFERRABLE INITIALLY DEFERRED;
 I   ALTER TABLE ONLY public.vatreturnbox DROP CONSTRAINT fkfab41192622c1275;
       public       postgres    false    250    479    4309            x           2606    27956    reminder fkfbcb072622c1275    FK CONSTRAINT     ?   ALTER TABLE ONLY public.reminder
    ADD CONSTRAINT fkfbcb072622c1275 FOREIGN KEY (company_id) REFERENCES public.company(id) DEFERRABLE INITIALLY DEFERRED;
 D   ALTER TABLE ONLY public.reminder DROP CONSTRAINT fkfbcb072622c1275;
       public       postgres    false    398    250    4309            y           2606    27961    reminder fkfbcb07271af9a42    FK CONSTRAINT     ?   ALTER TABLE ONLY public.reminder
    ADD CONSTRAINT fkfbcb07271af9a42 FOREIGN KEY (recurring_transaction) REFERENCES public.recurring_transaction(id) DEFERRABLE INITIALLY DEFERRED;
 D   ALTER TABLE ONLY public.reminder DROP CONSTRAINT fkfbcb07271af9a42;
       public       postgres    false    398    394    4493            {           2606    27971    reminder fkfbcb0729e5a0e30    FK CONSTRAINT     ?   ALTER TABLE ONLY public.reminder
    ADD CONSTRAINT fkfbcb0729e5a0e30 FOREIGN KEY (last_modifier) REFERENCES public.users(id) DEFERRABLE INITIALLY DEFERRED;
 D   ALTER TABLE ONLY public.reminder DROP CONSTRAINT fkfbcb0729e5a0e30;
       public       postgres    false    398    474    4585            z           2606    27966    reminder fkfbcb072f1ae8cde    FK CONSTRAINT     ?   ALTER TABLE ONLY public.reminder
    ADD CONSTRAINT fkfbcb072f1ae8cde FOREIGN KEY (created_by) REFERENCES public.users(id) DEFERRABLE INITIALLY DEFERRED;
 D   ALTER TABLE ONLY public.reminder DROP CONSTRAINT fkfbcb072f1ae8cde;
       public       postgres    false    398    474    4585            O           2606    27751    pay_run fkfbf01e34274bc854    FK CONSTRAINT     ?   ALTER TABLE ONLY public.pay_run
    ADD CONSTRAINT fkfbf01e34274bc854 FOREIGN KEY (id) REFERENCES public.transaction(id) DEFERRABLE INITIALLY DEFERRED;
 D   ALTER TABLE ONLY public.pay_run DROP CONSTRAINT fkfbf01e34274bc854;
       public       postgres    false    372    443    4553            N           2606    27746    pay_run fkfbf01e3449b05fba    FK CONSTRAINT     ?   ALTER TABLE ONLY public.pay_run
    ADD CONSTRAINT fkfbf01e3449b05fba FOREIGN KEY (employee_group) REFERENCES public.employee_group(id) DEFERRABLE INITIALLY DEFERRED;
 D   ALTER TABLE ONLY public.pay_run DROP CONSTRAINT fkfbf01e3449b05fba;
       public       postgres    false    372    282    4351            M           2606    27741    pay_run fkfbf01e34622c1275    FK CONSTRAINT     ?   ALTER TABLE ONLY public.pay_run
    ADD CONSTRAINT fkfbf01e34622c1275 FOREIGN KEY (company_id) REFERENCES public.company(id) DEFERRABLE INITIALLY DEFERRED;
 D   ALTER TABLE ONLY public.pay_run DROP CONSTRAINT fkfbf01e34622c1275;
       public       postgres    false    372    250    4309            P           2606    27756    pay_run fkfbf01e34b3a43ae1    FK CONSTRAINT     ?   ALTER TABLE ONLY public.pay_run
    ADD CONSTRAINT fkfbf01e34b3a43ae1 FOREIGN KEY (employee) REFERENCES public.employee(id) DEFERRABLE INITIALLY DEFERRED;
 D   ALTER TABLE ONLY public.pay_run DROP CONSTRAINT fkfbf01e34b3a43ae1;
       public       postgres    false    372    280    4349            Y           2606    27801    pay_tax fkfbf02354274bc854    FK CONSTRAINT     ?   ALTER TABLE ONLY public.pay_tax
    ADD CONSTRAINT fkfbf02354274bc854 FOREIGN KEY (id) REFERENCES public.transaction(id) DEFERRABLE INITIALLY DEFERRED;
 D   ALTER TABLE ONLY public.pay_tax DROP CONSTRAINT fkfbf02354274bc854;
       public       postgres    false    377    443    4553            Z           2606    27806    pay_tax fkfbf0235468a08e82    FK CONSTRAINT     ?   ALTER TABLE ONLY public.pay_tax
    ADD CONSTRAINT fkfbf0235468a08e82 FOREIGN KEY (payfrom_account_id) REFERENCES public.account(id) DEFERRABLE INITIALLY DEFERRED;
 D   ALTER TABLE ONLY public.pay_tax DROP CONSTRAINT fkfbf0235468a08e82;
       public       postgres    false    377    197    4243            X           2606    27796    pay_tax fkfbf02354c368d6ac    FK CONSTRAINT     ?   ALTER TABLE ONLY public.pay_tax
    ADD CONSTRAINT fkfbf02354c368d6ac FOREIGN KEY (tax_agency_id) REFERENCES public.taxagency(id) DEFERRABLE INITIALLY DEFERRED;
 D   ALTER TABLE ONLY public.pay_tax DROP CONSTRAINT fkfbf02354c368d6ac;
       public       postgres    false    377    419    4523            r           2606    26646 !   company_fields fkfd31f4db622c1275    FK CONSTRAINT     ?   ALTER TABLE ONLY public.company_fields
    ADD CONSTRAINT fkfd31f4db622c1275 FOREIGN KEY (company_id) REFERENCES public.company(id) DEFERRABLE INITIALLY DEFERRED;
 K   ALTER TABLE ONLY public.company_fields DROP CONSTRAINT fkfd31f4db622c1275;
       public       postgres    false    250    251    4309            r           2606    27926 &   reconciliation_item fkfd684aef63880555    FK CONSTRAINT     ?   ALTER TABLE ONLY public.reconciliation_item
    ADD CONSTRAINT fkfd684aef63880555 FOREIGN KEY (transaction_id) REFERENCES public.transaction(id) DEFERRABLE INITIALLY DEFERRED;
 P   ALTER TABLE ONLY public.reconciliation_item DROP CONSTRAINT fkfd684aef63880555;
       public       postgres    false    392    443    4553            s           2606    27931 &   reconciliation_item fkfd684aefe274d89f    FK CONSTRAINT     ?   ALTER TABLE ONLY public.reconciliation_item
    ADD CONSTRAINT fkfd684aefe274d89f FOREIGN KEY (reconciliation_id) REFERENCES public.reconciliation(id) DEFERRABLE INITIALLY DEFERRED;
 P   ALTER TABLE ONLY public.reconciliation_item DROP CONSTRAINT fkfd684aefe274d89f;
       public       postgres    false    392    390    4489            -           2606    27581     payee_contact fkfedee9e9b2fc5555    FK CONSTRAINT     ?   ALTER TABLE ONLY public.payee_contact
    ADD CONSTRAINT fkfedee9e9b2fc5555 FOREIGN KEY (payee_id) REFERENCES public.payee(id) DEFERRABLE INITIALLY DEFERRED;
 J   ALTER TABLE ONLY public.payee_contact DROP CONSTRAINT fkfedee9e9b2fc5555;
       public       postgres    false    4439    353    355            ?           2606    26896     employee_group fkff7fd8e622c1275    FK CONSTRAINT     ?   ALTER TABLE ONLY public.employee_group
    ADD CONSTRAINT fkff7fd8e622c1275 FOREIGN KEY (company_id) REFERENCES public.company(id) DEFERRABLE INITIALLY DEFERRED;
 J   ALTER TABLE ONLY public.employee_group DROP CONSTRAINT fkff7fd8e622c1275;
       public       postgres    false    4309    282    250            ?           2606    26906     employee_group fkff7fd8e9e5a0e30    FK CONSTRAINT     ?   ALTER TABLE ONLY public.employee_group
    ADD CONSTRAINT fkff7fd8e9e5a0e30 FOREIGN KEY (last_modifier) REFERENCES public.users(id) DEFERRABLE INITIALLY DEFERRED;
 J   ALTER TABLE ONLY public.employee_group DROP CONSTRAINT fkff7fd8e9e5a0e30;
       public       postgres    false    474    4585    282            ?           2606    26901     employee_group fkff7fd8ef1ae8cde    FK CONSTRAINT     ?   ALTER TABLE ONLY public.employee_group
    ADD CONSTRAINT fkff7fd8ef1ae8cde FOREIGN KEY (created_by) REFERENCES public.users(id) DEFERRABLE INITIALLY DEFERRED;
 J   ALTER TABLE ONLY public.employee_group DROP CONSTRAINT fkff7fd8ef1ae8cde;
       public       postgres    false    474    4585    282            ?           2606    28346    transaction fkfff466be29a77274    FK CONSTRAINT     ?   ALTER TABLE ONLY public.transaction
    ADD CONSTRAINT fkfff466be29a77274 FOREIGN KEY (statement_record_id) REFERENCES public.statement_record(id) DEFERRABLE INITIALLY DEFERRED;
 H   ALTER TABLE ONLY public.transaction DROP CONSTRAINT fkfff466be29a77274;
       public       postgres    false    4513    412    443            ?           2606    28366    transaction fkfff466be33118956    FK CONSTRAINT     ?   ALTER TABLE ONLY public.transaction
    ADD CONSTRAINT fkfff466be33118956 FOREIGN KEY (fixed_asset_id) REFERENCES public.fixed_asset(id) DEFERRABLE INITIALLY DEFERRED;
 H   ALTER TABLE ONLY public.transaction DROP CONSTRAINT fkfff466be33118956;
       public       postgres    false    443    296    4369            ?           2606    28356    transaction fkfff466be622c1275    FK CONSTRAINT     ?   ALTER TABLE ONLY public.transaction
    ADD CONSTRAINT fkfff466be622c1275 FOREIGN KEY (company_id) REFERENCES public.company(id) DEFERRABLE INITIALLY DEFERRED;
 H   ALTER TABLE ONLY public.transaction DROP CONSTRAINT fkfff466be622c1275;
       public       postgres    false    443    250    4309            ?           2606    28341    transaction fkfff466be7ab4e4d2    FK CONSTRAINT     ?   ALTER TABLE ONLY public.transaction
    ADD CONSTRAINT fkfff466be7ab4e4d2 FOREIGN KEY (transaction_make_deposit_entry_id) REFERENCES public.transaction_make_deposit_entries(id) DEFERRABLE INITIALLY DEFERRED;
 H   ALTER TABLE ONLY public.transaction DROP CONSTRAINT fkfff466be7ab4e4d2;
       public       postgres    false    443    455    4565            ?           2606    28351    transaction fkfff466be82f57b2c    FK CONSTRAINT     ?   ALTER TABLE ONLY public.transaction
    ADD CONSTRAINT fkfff466be82f57b2c FOREIGN KEY (last_activity) REFERENCES public.activity(id) DEFERRABLE INITIALLY DEFERRED;
 H   ALTER TABLE ONLY public.transaction DROP CONSTRAINT fkfff466be82f57b2c;
       public       postgres    false    206    443    4253            ?           2606    28386    transaction fkfff466be9e5a0e30    FK CONSTRAINT     ?   ALTER TABLE ONLY public.transaction
    ADD CONSTRAINT fkfff466be9e5a0e30 FOREIGN KEY (last_modifier) REFERENCES public.users(id) DEFERRABLE INITIALLY DEFERRED;
 H   ALTER TABLE ONLY public.transaction DROP CONSTRAINT fkfff466be9e5a0e30;
       public       postgres    false    474    443    4585            ?           2606    28361    transaction fkfff466beaa7eff5    FK CONSTRAINT     ?   ALTER TABLE ONLY public.transaction
    ADD CONSTRAINT fkfff466beaa7eff5 FOREIGN KEY (job_id) REFERENCES public.job(id) DEFERRABLE INITIALLY DEFERRED;
 G   ALTER TABLE ONLY public.transaction DROP CONSTRAINT fkfff466beaa7eff5;
       public       postgres    false    443    324    4403            ?           2606    28336    transaction fkfff466beac60fbcf    FK CONSTRAINT     ?   ALTER TABLE ONLY public.transaction
    ADD CONSTRAINT fkfff466beac60fbcf FOREIGN KEY (credits_and_payments_id) REFERENCES public.credits_and_payments(id) DEFERRABLE INITIALLY DEFERRED;
 H   ALTER TABLE ONLY public.transaction DROP CONSTRAINT fkfff466beac60fbcf;
       public       postgres    false    443    257    4319            ?           2606    28376    transaction fkfff466beb384fcf5    FK CONSTRAINT     ?   ALTER TABLE ONLY public.transaction
    ADD CONSTRAINT fkfff466beb384fcf5 FOREIGN KEY (transaction_accounter_class) REFERENCES public.accounter_class(id) DEFERRABLE INITIALLY DEFERRED;
 H   ALTER TABLE ONLY public.transaction DROP CONSTRAINT fkfff466beb384fcf5;
       public       postgres    false    443    199    4245            ?           2606    28381    transaction fkfff466beb799c71f    FK CONSTRAINT     ?   ALTER TABLE ONLY public.transaction
    ADD CONSTRAINT fkfff466beb799c71f FOREIGN KEY (location_id) REFERENCES public.location(id) DEFERRABLE INITIALLY DEFERRED;
 H   ALTER TABLE ONLY public.transaction DROP CONSTRAINT fkfff466beb799c71f;
       public       postgres    false    443    337    4419            ?           2606    28371    transaction fkfff466bef1ae8cde    FK CONSTRAINT     ?   ALTER TABLE ONLY public.transaction
    ADD CONSTRAINT fkfff466bef1ae8cde FOREIGN KEY (created_by) REFERENCES public.users(id) DEFERRABLE INITIALLY DEFERRED;
 H   ALTER TABLE ONLY public.transaction DROP CONSTRAINT fkfff466bef1ae8cde;
       public       postgres    false    443    474    4585           