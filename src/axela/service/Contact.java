package axela.service;
//divya 3rd July 2012

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cloudify.connect.Connect;

public class Contact extends Connect {
	// ////// List page links

	public String LinkHeader = "";
	public String search = "";
	public String txt_search = "91-";
	public String cli = "";
	public String comp_id = "0";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				// CheckPerm(comp_id, "emp_contact_access", request, response);
				search = PadQuotes(request.getParameter("search_button"));
				txt_search = PadQuotes(request.getParameter("txt_search"));
				cli = PadQuotes(request.getParameter("From"));

				if (txt_search.equals("")) {
					txt_search = "91-";
				}

				if (!cli.equals("")) {
					// if (ReturnPerm(comp_id, "emp_contact_access", request).equals("1")) {
					txt_search = cli;
					// } else {
					// response.sendRedirect(response.encodeRedirectURL(AccessDenied()));
					// }
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}
}
