package com.vimukti.accounter.text;

import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.hibernate.Session;

import com.vimukti.accounter.servlets.BaseServlet;
import com.vimukti.accounter.utils.HibernateUtil;

public class MailHookServlet extends BaseServlet {

	private Logger logger = Logger.getLogger(MailHookServlet.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		// Read Json Request
		StringWriter writter = new StringWriter();
		IOUtils.copy(req.getReader(), writter);

		logger.info("Parsing mail request");
		Mail request = Mail.parse(writter.toString());

		// Open Hiberante Session
		Session session = HibernateUtil.openSession();
		try {
			logger.info("Processing text request");
			// Process Request
			TextRequestProcessor.getInstance().process(request);
		} finally {
			// FInally close it
			session.close();
		}

		logger.info("Sending response 200 to postmark");
		// Send status 200 if request succeed
		resp.setStatus(200);
	}
}
