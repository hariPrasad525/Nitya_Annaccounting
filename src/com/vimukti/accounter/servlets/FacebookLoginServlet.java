package com.vimukti.accounter.servlets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.vimukti.accounter.utils.StringUtils;

public class FacebookLoginServlet extends ThirdPartySignupServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String code = req.getParameter("code");
		if (StringUtils.isNotBlankStr(code)) {
			String authURL = Facebook.getAuthURL(code);
			URL url = new URL(authURL);
			try {
				String result = readURL(url);
				String accessToken = null;
				Integer expires = null;
				String[] pairs = result.split("&");
				for (String pair : pairs) {
					String[] kv = pair.split("=");
					if (kv.length != 2) {
						throw new RuntimeException("Unexpected auth response");
					} else {
						if (kv[0].equals("access_token")) {
							accessToken = kv[1];
						}
						if (kv[0].equals("expires")) {
							expires = Integer.valueOf(kv[1]);
						}
					}
				}
				if (accessToken != null && expires != null) {
					JSONObject userDetails = new JSONObject(
							urlToString(new URL(
									"https://graph.facebook.com/me?access_token="
											+ accessToken)));
					String id = userDetails.getString("id");
					String firstName = userDetails.getString("first_name");
					String lastName = userDetails.getString("last_name");
					String email = userDetails.getString("email");

					// resp.sendRedirect("http://www.onmydoorstep.com.au/");
					loginForUser(email,firstName,lastName, req, resp);
				} else {
					throw new RuntimeException(
							"Access token and expires not found");
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else{
			try{
			redirectExternal(req, resp, Facebook.getLoginRedirectURL());
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}

	private String readURL(URL url) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		InputStream is = url.openStream();
		int r;
		while ((r = is.read()) != -1) {
			baos.write(r);
		}
		return new String(baos.toByteArray());
	}

	public void destroy() {
	}

	/**
	 * Reads the contents of the given URL and returns it as a string.
	 * 
	 * @param url
	 * @return
	 */
	public static String urlToString(URL url) throws IOException {
		StringBuffer sb = new StringBuffer("");
		InputStream is = url.openStream();
		int n = 0;
		do {
			n = is.read();
			if (n >= 0) {
				sb.append((char) n);
			}
		} while (n >= 0);
		is.close();
		return sb.toString();
	}

}