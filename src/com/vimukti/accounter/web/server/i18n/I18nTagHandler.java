package com.vimukti.accounter.web.server.i18n;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

public class I18nTagHandler extends TagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String msg;

	@Override
	public int doStartTag() throws JspException {

		try {
			// Get the writer object for output.
			JspWriter out = pageContext.getOut();

			// Perform substr operation on string.
			out.print(ServerSideMessages.getMessage(msg));

		} catch (IOException e) {
			e.printStackTrace();
		}
		return SKIP_BODY;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
