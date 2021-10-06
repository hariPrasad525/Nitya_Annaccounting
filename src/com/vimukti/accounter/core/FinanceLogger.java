package com.vimukti.accounter.core;

import org.apache.log4j.Logger;
import org.json.JSONException;

import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCommand;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;

public class FinanceLogger implements IAccounterServerCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(FinanceLogger.class);
	private static ThreadLocal<FinanceLogger> cache = new ThreadLocal<FinanceLogger>();

	public FinanceLogger() {
		strBuffer = new StringBuffer();
	}

	private long id;
	private String description;
	private String logMessge;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLogMessge() {
		return logMessge;
	}

	public void setLogMessge(String logMessge) {
		this.logMessge = logMessge;
	}

	private StringBuffer strBuffer;

	private int version;

	public void startLogger(IAccounterCore data, String createdBy, int comand) {
		getInstance();
		String cmdType = (comand == AccounterCommand.CREATION_SUCCESS ? "Creation of "
				: (comand == AccounterCommand.UPDATION_SUCCESS ? "Updation of "
						: (comand == AccounterCommand.DELETION_SUCCESS ? "Deletion of "
								: "")));
		if (data != null)
			this.description = cmdType
					+ ObjectConvertUtil
							.getServerEqivalentClass(data.getClass())
							.getSimpleName()
					+ ":"
					+ (data instanceof ClientTransaction ? "Number "
							+ ((ClientTransaction) data).getNumber() : data
							.getName());
		else
			this.description = cmdType;

		log.info("---------------------------------------------" + description
				+ "-------------------------------------------------\n");
		log(description);
	}

	public void closeLogger() {
		log("Logging is completed");

		log.info("--------------------------------------------- End of Logging-------------------------------------------------\n");

		this.setLogMessge(this.strBuffer.toString());

		HibernateUtil.getCurrentSession().save(cache.get());
		cache.set(null);
	}

	public static void log(String description, String... objects) {

		for (int i = 0; i < objects.length; i++) {
			String s = objects[i];
			if (s != null) {
				description = description.trim().replace("{" + i + "}",
						objects[i]);
			}
		}
		getInstance().strBuffer.append("Operation :" + description + "\n");
		log.info(description + "\n");
	}

	public static void log(String description) {
		getInstance().strBuffer.append("Operation :" + description + "\n");
		log.info(description + "\n");
	}

	public static FinanceLogger getInstance() {
		if (cache.get() == null)
			cache.set(new FinanceLogger());
		return cache.get();
	}

	@Override
	public long getID() {
		return 0;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public int getVersion() {
		return version;
	}

	@Override
	public void setVersion(int version) {
		this.version = version;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		AccounterMessages messages = Global.get().messages();

		w.put(messages.type(), messages.financeLogger()).gap();
	}

	@Override
	public void selfValidate() {
		// TODO Auto-generated method stub
		
	}

}
