package com.nitya.accounter.mobile.commands;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.hibernate.Session;

import com.nitya.accounter.core.AccounterThreadLocal;
import com.nitya.accounter.core.BrandingTheme;
import com.nitya.accounter.core.ClientConvertUtil;
import com.nitya.accounter.core.Company;
import com.nitya.accounter.core.EmailAccount;
import com.nitya.accounter.core.Invoice;
import com.nitya.accounter.core.Transaction;
import com.nitya.accounter.core.User;
import com.nitya.accounter.mail.UsersMailSendar;
import com.nitya.accounter.main.CompanyPreferenceThreadLocal;
import com.nitya.accounter.main.ServerLocal;
import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.Requirement;
import com.nitya.accounter.mobile.Result;
import com.nitya.accounter.mobile.requirements.ChangeListner;
import com.nitya.accounter.mobile.requirements.EmailAccountRequirement;
import com.nitya.accounter.mobile.requirements.StringRequirement;
import com.nitya.accounter.utils.HibernateUtil;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.IGlobal;
import com.nitya.accounter.web.client.core.ClientCompanyPreferences;
import com.nitya.accounter.web.client.core.ClientEmailAccount;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.server.FinanceTool;

public class SendInvoiceEmailCommand extends AbstractCommand {

	private static final String FROM_ADDRESS = "fromAddress";
	private static final String TO_ADDRESS = "toAddress";
	private static final String CC_ADDRESS = "ccAddress";
	private static final String SUBJECT = "subject";
	private static final String ATTACHMENT = "attachment";

	private Invoice invoice;

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		String string = context.getString();
		invoice = getServerObject(Invoice.class, Long.valueOf(string));
		if (invoice != null) {
			get(ATTACHMENT).setValue("Invoice_" + invoice.getNumber() + ".pdf");
		}
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return getMessages().sendInvoiceToMail();
	}

	@Override
	protected String getDetailsMessage() {
		return null;
	}

	@Override
	protected void setDefaultValues(Context context) {

	}

	@Override
	public String getSuccessMessage() {
		return getMessages().invoiceSentToYourEmail();
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		String toAdd = get(TO_ADDRESS).getValue();
		String ccAdd = get(CC_ADDRESS).getValue();
		String sub = get(SUBJECT).getValue();
		EmailAccount account = get(FROM_ADDRESS).getValue();
		ClientEmailAccount emailAccount = null;
		try {
			emailAccount = new ClientConvertUtil().toClientObject(account,
					ClientEmailAccount.class);
			sendPrintAndInvoiceToEmail(invoice, toAdd, emailAccount, ccAdd, sub);
		} catch (AccounterException e1) {
			e1.printStackTrace();
		}
		return null;
	}

	private void sendPrintAndInvoiceToEmail(Invoice invoice2,
			final String toAdd, final ClientEmailAccount emailAccount,
			final String ccAdd, final String sub) {
		Company company = getCompany();
		Set<BrandingTheme> brandingTheme = company.getBrandingTheme();
		Iterator<BrandingTheme> iterator = brandingTheme.iterator();
		if (!iterator.hasNext()) {
			return;
		}
		// Sending to mail in a separate thread.
		BrandingTheme next = iterator.next();
		final long brandingThemeId = next.getID();
		final long companyId = company.getID();
		final long invoiceId = invoice.getID();
		final String tradingName = company.getTradingName();
		final Locale locale = ServerLocal.get();
		final ClientCompanyPreferences clientCompanyPreferences = CompanyPreferenceThreadLocal
				.get();
		final User user = AccounterThreadLocal.get();
		final IGlobal iGlobal = Global.get();
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					HibernateUtil.openSession();
					ServerLocal.set(locale);
					Global.set(iGlobal);
					CompanyPreferenceThreadLocal.set(clientCompanyPreferences);
					AccounterThreadLocal.set(user);
					FinanceTool tool = new FinanceTool();
					List<String> createPdfFile = tool.createPdfFile(
							String.valueOf(invoiceId),
							Transaction.TYPE_INVOICE, brandingThemeId,
							companyId, new ClientFinanceDate(),
							new ClientFinanceDate());
					String content = getMessages().invoiceMailMessage(
							Global.get().Customer(), invoice.getNumber(),
							invoice.getDate().toClientFinanceDate());
					content = content.replaceAll("\n", "<br/>");
					UsersMailSendar.sendPdfMail(new File(createPdfFile.get(1)),
							tradingName, sub, content, emailAccount, toAdd,
							ccAdd);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					Session currentSession = HibernateUtil.getCurrentSession();
					if (currentSession.isOpen()) {
						currentSession.close();
					}
				}
			}
		}).start();

	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new EmailAccountRequirement(FROM_ADDRESS, getMessages()
				.pleaseEnter(getMessages().from()), getMessages().from(),
				false, true, new ChangeListner<EmailAccount>() {

					@Override
					public void onSelection(EmailAccount value) {
					}
				}) {

			@Override
			protected List<EmailAccount> getLists(Context context) {
				return new ArrayList<EmailAccount>(getCompany()
						.getEmailAccounts());
			}
		});
		list.add(new StringRequirement(TO_ADDRESS, getMessages().pleaseEnter(
				getMessages().to()), getMessages().to(), false, true));

		list.add(new StringRequirement(CC_ADDRESS, getMessages().pleaseEnter(
				getMessages().cc()), getMessages().cc(), true, true));

		list.add(new StringRequirement(SUBJECT, getMessages().pleaseEnter(
				getMessages().subject()), getMessages().subject(), true, true));

		StringRequirement stringRequirement = new StringRequirement(ATTACHMENT,
				getMessages().pleaseEnter(getMessages().attachmentName()),
				getMessages().attachmentName(), true, true);
		stringRequirement.setEditable(false);
		list.add(stringRequirement);

	}
}
