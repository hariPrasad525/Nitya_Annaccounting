package com.nitya.accounter.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nitya.accounter.mail.UsersMailSendar;
import com.nitya.accounter.main.ServerLocal;
import com.nitya.accounter.web.client.Global;

public class SupportServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String view = "/WEB-INF/support.jsp";

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("isRTL",
				ServerLocal.get().equals(new Locale("ar", "", "")));
		String string = (String) request.getParameter("ajax");
		if (string == null || !Boolean.valueOf(string)) {
			if (sendMailToSupport(request)) {
				request.setAttribute("message", Global.get().messages()
						.weHaveReceivedYourInformation());
			} else {
				request.setAttribute("errormessage", Global.get().messages()
						.pleaseEnterValidDetails());
			}
			RequestDispatcher dispatcher = getServletContext()
					.getRequestDispatcher(view);
			dispatcher.forward(request, response);
		} else {
			String name = request.getParameter("name");
			String email = request.getParameter("email");
			String comment = request.getParameter("comment");
			PrintWriter out = response.getWriter();
			response.setContentType("text/text");
			if (mail(name, email, comment)) {
				out.write("success");
			} else {
				out.write("fail");
			}
		}
	}

	private boolean mail(String name, String email, String comment) {
		if (comment == null || comment.isEmpty()) {
			return false;
		}

		UsersMailSendar.sendMailToSupport(name, email, Global.get().messages()
				.aFeedbackMessage(), comment);
		return true;
	}

	private boolean sendMailToSupport(HttpServletRequest request) {

		String name = request.getParameter("name");
		String emailId = request.getParameter("emailId");
		String subject = request.getParameter("subject");
		String message = request.getParameter("message");

		if (name == null || name.isEmpty() || emailId == null
				|| emailId.isEmpty() || subject == null || subject.isEmpty()
				|| message == null || message.isEmpty()) {
			return false;
		}

		UsersMailSendar.sendMailToSupport(name, emailId, subject, message);
		return true;
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher dispatcher = getServletContext()
				.getRequestDispatcher(view);
		dispatcher.forward(request, response);
	}
}
