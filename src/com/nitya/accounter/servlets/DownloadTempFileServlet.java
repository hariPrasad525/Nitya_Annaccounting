package com.nitya.accounter.servlets;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nitya.accounter.main.ServerConfiguration;
import com.nitya.accounter.utils.DeleteQueue;

public class DownloadTempFileServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int BUFSIZE = 1024;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String attachId = req.getParameter("attachmentId");
		String fileName = req.getParameter("filename");
		File file = new File(ServerConfiguration.getTmpDir(), attachId);
		if (!file.exists()) {
			return;
		}
		try {

			if (attachId != null) {

				int length = 0;
				ServletOutputStream op = resp.getOutputStream();
				resp.setHeader("Content-Disposition", "attachment; filename=\""
						+ fileName + "\"");
				resp.addHeader("Content-Description", " File Transfer");
				resp.addHeader("Content-Type", "application/octet-stream");
				resp.addHeader("Content-Transfer-Encoding", "binary");
				resp.addHeader("Expires", "0");
				resp.addHeader("Cache-Control",
						"must-revalidate, post-check=0, pre-check=0");
				resp.addHeader("Pragma", "public");
				byte[] bbuf = new byte[BUFSIZE];
				DataInputStream in = new DataInputStream(new FileInputStream(
						file));
				while ((in != null) && ((length = in.read(bbuf)) != -1)) {
					op.write(bbuf, 0, length);
				}
				in.close();
				op.flush();
				op.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DeleteQueue.get().add(file);
		}

	}
}
