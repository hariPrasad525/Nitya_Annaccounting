package com.nitya.accounter.mobile.commands;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.nitya.accounter.core.EU;
import com.nitya.accounter.core.IMUser;
import com.nitya.accounter.core.MobileCookie;
import com.nitya.accounter.mobile.Command;
import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.MobileSession;
import com.nitya.accounter.mobile.Requirement;
import com.nitya.accounter.mobile.Result;
import com.nitya.accounter.utils.HibernateUtil;
import com.nitya.accounter.web.client.Global;

public class LogoutCommand extends Command {

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

	}

	@Override
	public Result run(Context context) {
		MobileSession ioSession = context.getIOSession();
		ioSession.setAuthentication(false);
		ioSession.setExpired(true);
		ioSession.removeAllMessages();

		Result result = new Result();
		result.add(Global.get().messages().youAreSuccessfullyLoggedOut());
		result.setCookie("No Cookie");

		Session hibernateSession = context.getHibernateSession();
		Transaction beginTransaction = hibernateSession.beginTransaction();

		MobileCookie mobileCookie = getMobileCookie(context.getNetworkId());
		if (mobileCookie != null) {
			hibernateSession.delete(mobileCookie);
		}

		IMUser imUser = getIMUser(context.getNetworkId(),
				context.getNetworkType());
		if (imUser != null) {
			hibernateSession.delete(imUser);
		}
		beginTransaction.commit();
		markDone();
		EU.removeCipher();
		EU.removeKey(ioSession.getId());
		result.setNextCommand("login");
		return result;
	}

	private MobileCookie getMobileCookie(String string) {
		Session session = HibernateUtil.getCurrentSession();
		return (MobileCookie) session.get(MobileCookie.class, string);
	}

	private IMUser getIMUser(String networkId, int networkType) {
		Session session = HibernateUtil.getCurrentSession();
		IMUser user = (IMUser) session.getNamedQuery("imuser.by.networkId")
				.setString("networkId", networkId)
				.setInteger("networkType", networkType).uniqueResult();
		return user;
	}
}
