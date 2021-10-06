package com.nitya.accounter.core.change;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.nitya.accounter.core.Account;
import com.nitya.accounter.core.ClientConvertUtil;
import com.nitya.accounter.core.IAccounterServerCore;
import com.nitya.accounter.core.Util;
import com.nitya.accounter.web.client.core.AccounterCommand;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.core.Utility;
import com.nitya.accounter.web.client.exception.AccounterException;

public class ChangeTracker {
	static Logger log = Logger.getLogger(Account.class);

	public static ThreadLocal<List<IAccounterCore>> clientobjects = new ThreadLocal<List<IAccounterCore>>();

	public static void put(IAccounterServerCore obj) {
		if (obj == null || obj.getID() == 0)
			return;
		IAccounterCore core;
		try {
			core = (IAccounterCore) new ClientConvertUtil().toClientObject(obj,
					Util.getClientClass(obj));
		} catch (AccounterException e) {
			e.printStackTrace();
			return;
		}
		put(core, false);
	}

	public static void put(IAccounterCore obj) {
		put(obj, false);
	}

	public static void put(AccounterCommand obj) {
		if (obj == null)
			return;
		log.info("Put Object:" + obj.toString());
		get().add(obj);
	}

	private static List<IAccounterCore> get() {
		List<IAccounterCore> list = clientobjects.get();
		if (list == null) {
			list = new ArrayList<IAccounterCore>();
			clientobjects.set(list);
		}
		return list;
	}

	public static IAccounterCore[] getChanges() {
		List<IAccounterCore> list = get();
		return list.toArray(new IAccounterCore[list.size()]);
	}

	public static void clearChanges() {
		get().clear();
	}

	public static void put(IAccounterServerCore serverObject,
			boolean isTransaction) {
		if (serverObject == null)
			return;

		Class<?> d = Util.getClientClass(serverObject);
		if (d == null)
			return;
		IAccounterCore core;
		try {
			core = (IAccounterCore) new ClientConvertUtil().toClientObject(
					serverObject, d);
		} catch (AccounterException e) {
			e.printStackTrace();
			return;
		}

		put(core, isTransaction);

	}

	public static void put(IAccounterCore core, boolean isTransaction) {
		if (core == null) {
			return;
		}
		log.info("Put Object:" + core.toString());

		AccounterCommand cmd = new AccounterCommand();
		cmd.setCommand(AccounterCommand.UPDATION_SUCCESS);

		if (!isTransaction)
			cmd.setData(core);

		cmd.setID(core.getID());
		cmd.setObjectType(core.getObjectType());
		Utility.updateClientList(cmd, get());
	}

	public static void init() {
		clientobjects.set(new ArrayList<IAccounterCore>());
	}
}
