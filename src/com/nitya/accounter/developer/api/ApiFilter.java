package com.nitya.accounter.developer.api;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;

import com.nitya.accounter.core.Client;
import com.nitya.accounter.core.Company;
import com.nitya.accounter.core.Developer;
import com.nitya.accounter.developer.api.core.ApiResult;
import com.nitya.accounter.utils.HibernateUtil;

public class ApiFilter implements Filter {
	public static final String SIGNATURE = "Signature";
	private static final String ALGORITHM = "hmacSHA256";
	private static final String DATE_FORMAT = "yyyy.MM.dd G 'at' HH:mm:ss z";

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest req2, ServletResponse resp2,
			FilterChain arg2) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) req2;
		HttpServletResponse resp = (HttpServletResponse) resp2;
		String url = req.getQueryString();
		String signature = req.getParameter(SIGNATURE);
		if (signature == null) {
			sendFail(req, resp, "Signature must be present");
			return;
		}
		String signatureProperty = new String("&" + SIGNATURE + "="
				+ URLEncoder.encode(signature, "utf8"));
		String remainingUrl = url.replace(signatureProperty, "");
		String apiKey = req.getParameter("ApiKey");
		SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
		try {
			Date expire = format.parse(req.getParameter("Expire"));
			// TODO
		} catch (Exception e1) {
			sendFail(req, resp, "Wrong expire date formate");
			return;
		}
		Session session = HibernateUtil.openSession();
		try {
			Developer developer = getDeveloperByApiKey(apiKey);
			if (developer == null) {
				sendFail(req, resp, "Wrong API key.");
				return;
			}

			String secretKey = developer.getSecretKey();
			String sighned = doSigning(remainingUrl, secretKey);
			if (!sighned.equals(signature)) {
				sendFail(req, resp, "Signature not matched");
				return;
			}

			Client client = developer.getClient();
			String companyId = req.getParameter("CompanyId");
			if (companyId != null) {
				try {
					long id = Long.parseLong(companyId);
					Company company = getCompany(id, client);
					if (company == null) {
						sendFail(req, resp, "Wrong company id");
						return;
					}
					req.setAttribute("companyId", company.getID());
				} catch (Exception e) {
					sendFail(req, resp, "Company Id should be long");
					return;
				}
			}

			req.setAttribute("id", developer.getId());
			req.setAttribute("emailId", client.getEmailId());
			arg2.doFilter(req2, resp2);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session.isOpen()) {
				session.close();
			}
		}
	}

	private void sendFail(HttpServletRequest req, HttpServletResponse resp,
			String result) {
		try {
			ApiResult apiResult = new ApiResult();
			apiResult.setStatus(ApiResult.FAIL);
			apiResult.setResult(result);
			ApiSerializationFactory factory = ApiBaseServlet
					.getSerializationFactory(req);
			String string = factory.serialize(apiResult);
			ServletOutputStream outputStream;
			outputStream = resp.getOutputStream();
			outputStream.write(string.getBytes());
			outputStream.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Company getCompany(long id, Client client) {
		Session session = HibernateUtil.getCurrentSession();
		Object result = session
				.getNamedQuery("get.ServerCompany.by.companyId.and.client")
				.setLong("id", id).setParameter("client", client)
				.uniqueResult();
		return (Company) ((Object[]) result)[0];
	}

	private String doSigning(String url, String secretKeystr) {
		byte[] secretKeyBytes = secretKeystr.getBytes();
		SecretKeySpec keySpec = new SecretKeySpec(secretKeyBytes, ALGORITHM);
		try {
			Mac mac = Mac.getInstance(ALGORITHM);
			mac.init(keySpec);
			byte[] doFinal = mac.doFinal(url.getBytes());
			String string = new String(doFinal);
			return getURLDecode(URLEncoder.encode(string, "utf8"));
		} catch (Exception e) {
		}
		return url;
	}

	private Developer getDeveloperByApiKey(String apiKey) {
		Session session = HibernateUtil.getCurrentSession();
		return (Developer) session.getNamedQuery("get.developer.by.apiKey")
				.setParameter("apiKey", apiKey).uniqueResult();
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

	private String getURLDecode(String string) throws Exception {
		return URLDecoder.decode(string, "utf8");
	}
}
