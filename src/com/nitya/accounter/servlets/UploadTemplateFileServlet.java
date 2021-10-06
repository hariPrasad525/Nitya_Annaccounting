package com.nitya.accounter.servlets;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nitya.accounter.main.ServerConfiguration;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

/**
 * used to upload custom templates files
 * 
 */
public class UploadTemplateFileServlet extends BaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String INVOICE = "INVOICE";
	private static final String QUOTE = "QUOTE";
	private static final String CREDITNOTE = "CREDITNOTE";
	private static final String CASHSALE = "CASHSALE";
	private static final String PURCHASEORDER = "PURCHASEORDER";
	private static final String SALESORDER = "SALESORDER";

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Long companyID = (Long) request.getSession().getAttribute(COMPANY_ID);

		String theme = request.getParameter("themeId");
		if (theme == null)
			return;

		Long id = Long.valueOf(theme);
		long themeId = id.longValue();

		if (companyID == null)
			return;
		StringBuilder builder = new StringBuilder();
		MultipartRequest multi = new MultipartRequest(request,
				ServerConfiguration.getTmpDir(), 50 * 1024 * 1024,
				"ISO-8859-1", new DefaultFileRenamePolicy());
		response.setContentType("text/html");
		builder.append(processFile(multi, INVOICE, companyID, themeId));
		builder.append(';');
		builder.append(processFile(multi, CREDITNOTE, companyID, themeId));
		builder.append(';');
		builder.append(processFile(multi, QUOTE, companyID, themeId));
		builder.append(';');
		builder.append(processFile(multi, CASHSALE, companyID, themeId));
		builder.append(';');
		builder.append(processFile(multi, PURCHASEORDER, companyID, themeId));
		builder.append(';');
		builder.append(processFile(multi, SALESORDER, companyID, themeId));
		builder.append(';');

		response.getWriter().print(builder);
	}

	/**
	 * Process the file and return the fileName or emptyString if file is not
	 * there
	 * 
	 * @param multi
	 * @param invoice2
	 * @return
	 */
	private String processFile(MultipartRequest multi, String fileID,
			Long companyID, long themeId) {
		try {
			File file = multi.getFile(fileID);
			if (file != null) {
				String fileName = file.getName();
				File attachmentDir = new File(
						ServerConfiguration.getAttachmentsDir() + "/"
								+ companyID + "/templateFiles/" + themeId);
				if (!attachmentDir.exists()) {
					attachmentDir.mkdirs();
				}
				FileOutputStream fout;

				fout = new FileOutputStream(attachmentDir + File.separator
						+ fileName);

				InputStream in = new FileInputStream(file);
				OutputStream out = fout;
				byte[] buf = new byte[1024];
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				in.close();
				out.close();
				return fileName;
			} else {
				return " ";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
}
