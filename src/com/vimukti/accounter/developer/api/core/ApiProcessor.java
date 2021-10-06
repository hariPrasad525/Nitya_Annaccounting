package com.vimukti.accounter.developer.api.core;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Query;
import org.hibernate.Session;

import com.gdevelop.gwt.syncrpc.CookieManager;
import com.gdevelop.gwt.syncrpc.SessionManager;
import com.gdevelop.gwt.syncrpc.SyncProxy;
import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.main.ServerConfiguration;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.exception.AccounterException;

public abstract class ApiProcessor {
	public static final String DATE_FORMAT = "yyyy.MM.dd G 'at' HH:mm:ss z";
	private SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
	private ApiResult result = new ApiResult();
	public long companyId;
	private Company company;

	public void beforeProcess(HttpServletRequest req, HttpServletResponse resp) {
		companyId = Long.parseLong(req.getParameter("CompanyId"));
	}

	public abstract void process(HttpServletRequest req,
			HttpServletResponse resp) throws Exception;

	@SuppressWarnings("unchecked")
	protected <T> T getS2sSyncProxy(final HttpServletRequest req, String uri,
			Class<?> clazz) {
		String url = "http://" + ServerConfiguration.getMainServerDomain()
				+ ":" + ServerConfiguration.getMainServerPort() + uri;
		return (T) SyncProxy.newProxyInstance(clazz, url, "",
				new SessionManager() {
					private CookieManager cookieManager = new CookieManager();

					@Override
					public HttpURLConnection openConnection(URL url)
							throws Exception {
						HttpURLConnection connection = (HttpURLConnection) url
								.openConnection();
						connection.addRequestProperty("isAPI", "Yes");
						connection.addRequestProperty("companyId", req
								.getAttribute("companyId").toString());
						connection.addRequestProperty("emailId", req
								.getAttribute("emailId").toString());
						connection.addRequestProperty("ApiKey",
								req.getParameter("ApiKey"));
						connection.addRequestProperty("authentication",
								req.getParameter("authentication"));
						return connection;
					}

					@Override
					public void handleResponseHeaders(
							HttpURLConnection connection) throws IOException {
						cookieManager.storeCookies(connection);
					}

					@Override
					public com.gdevelop.gwt.syncrpc.CookieManager getCookieManager() {
						return null;
					}
				});
	}

	protected Client getClient(String emailId) {
		Session session = HibernateUtil.getCurrentSession();
		Query namedQuery = session.getNamedQuery("getClient.by.mailId");
		namedQuery.setParameter("emailId", emailId);
		Client client = (Client) namedQuery.uniqueResult();
		return client;
	}

	public Company getCompany() {
		if (company != null) {
			return company;
		}
		Session session = HibernateUtil.getCurrentSession();
		company = (Company) session.get(Company.class, companyId);
		return company;
	}

	protected void checkPermission(String feature) throws AccounterException {
		Set<String> features = getCompany().getFeatures();
		if (!features.contains(feature)) {
			throw new AccounterException(
					AccounterException.ERROR_PERMISSION_DENIED);
		}
	}

	protected ClientFinanceDate getClientFinanceDate(String string) {
		if (string == null) {
			return null;
		}
		try {
			return new ClientFinanceDate(format.parse(string));
		} catch (ParseException e) {
			return null;
		}
	}

	protected void setStatus(int status) {
		result.setStatus(status);
	}

	protected void sendResult(Object object) {
		result.setStatus(ApiResult.SUCCESS);
		result.setResult(object);
	}

	public ApiResult getResult() {
		return result;
	}

	protected void sendFail(String errorMessage) {
		result.setStatus(ApiResult.FAIL);
		result.setResult(errorMessage);
	}

	protected int readInt(HttpServletRequest req, String name, int def)
			throws AccounterException {
		int type = def;
		String itemType = req.getParameter(name);
		if (itemType != null) {
			try {
				type = Integer.parseInt(itemType);
			} catch (Exception e) {
				throw new AccounterException(name + " is wrong formate");
			}
		}
		return type;
	}

	protected int readInt(HttpServletRequest req, String name)
			throws AccounterException {
		int type = 0;
		String itemType = req.getParameter(name);
		if (itemType == null) {
			throw new AccounterException(name + " is not present");
		}
		try {
			type = Integer.parseInt(itemType);
		} catch (Exception e) {
			throw new AccounterException(name + " is wrong formate");
		}
		return type;
	}

	protected String readString(HttpServletRequest req, String name, String def) {
		String itemType = req.getParameter(name);
		if (itemType == null) {
			itemType = def;
		}
		return itemType;
	}

	protected String readString(HttpServletRequest req, String name)
			throws AccounterException {
		String itemType = req.getParameter(name);
		if (itemType == null) {
			throw new AccounterException(name + " is not present");
		}
		return itemType;
	}

	protected long readLong(HttpServletRequest req, String name, long def)
			throws AccounterException {
		long type = def;
		String itemType = req.getParameter(name);
		if (itemType != null) {
			try {
				type = Long.parseLong(itemType);
			} catch (Exception e) {
				throw new AccounterException(name + " is wrong formate");
			}
		}
		return type;
	}

	protected long readLong(HttpServletRequest req, String name)
			throws AccounterException {
		long type = 0;
		String itemType = req.getParameter(name);
		if (itemType == null) {
			throw new AccounterException(name + " is not present");
		}
		try {
			type = Long.parseLong(itemType);
		} catch (Exception e) {
			throw new AccounterException(name + " is wrong formate");
		}
		return type;
	}

	protected Boolean readBoolean(HttpServletRequest req, String name)
			throws AccounterException {
		Boolean type = null;
		String itemType = req.getParameter(name);
		if (itemType != null) {
			try {
				type = Boolean.parseBoolean(itemType);
			} catch (Exception e) {
				throw new AccounterException(name + " is wrong formate");
			}
		}
		return type;
	}
}
