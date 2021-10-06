package com.vimukti.accounter.servlets;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;

import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.main.upload.AttachmentFileServer;
import com.vimukti.accounter.utils.HibernateUtil;

public class DownloadAttachmentServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int BUFSIZE = 1024;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String attachmentId = req.getParameter("attachmentId");
		String name = req.getParameter("name");
		try {
			byte[] key = getEncryptionKey(req);
			InputStream in = AttachmentFileServer
					.getAttachmentStream(attachmentId, key);
			if (in != null) {
				int length = 0;
				ServletOutputStream op = resp.getOutputStream();
				resp.setHeader("Content-Disposition", "attachment; filename=\""
						+ name + "\"");
				byte[] bbuf = new byte[BUFSIZE];

				while ((in != null) && ((length = in.read(bbuf)) != -1)) {
					op.write(bbuf, 0, length);
				}
				in.close();
				op.flush();
				op.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private byte[] getEncryptionKey(HttpServletRequest req) {
		Long companyID = (Long) req.getSession().getAttribute(
				BaseServlet.COMPANY_ID);
		Session session = HibernateUtil.openSession();
		try {
			Company comapny = (Company) session.get(Company.class, companyID);
			if (comapny != null) {
				return comapny.getEncryptionKey().getBytes();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session.isOpen()) {
				session.close();
			}
		}
		return null;
	}
}
