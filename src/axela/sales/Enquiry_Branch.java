package axela.sales;
//divya 16th may

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cloudify.connect.Connect;

public class Enquiry_Branch extends Connect {

	public String go = "";
	public String comp_id = "0";
	public static String msg = "";
	public String StrSql = "";
	public String branch_id = "", emp_branch_id = "";
	public String para = "";
	public String enquiry_contact_id = "0";
	public String heading = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_enquiry_add", request, response);
			if (!comp_id.equals("0")) {
				emp_branch_id = CNumeric(GetSession("emp_branch_id", request));
				para = PadQuotes(request.getParameter("para"));
				go = PadQuotes(request.getParameter("go_button"));
				msg = PadQuotes(request.getParameter("msg"));
				enquiry_contact_id = CNumeric(PadQuotes(request.getParameter("contact_id")));
				if (para.equals("enquiry")) {
					heading = "Enquiry Branch";
				}
				if (go.equals("GO") || !emp_branch_id.equals("0")) {
					GetValues(request, response);
					CheckForm();
					if (msg.equals("")) {
						SetSession("enquiry_branch_id", branch_id, request);
						if (para.equals("enquiry")) {
							if (!enquiry_contact_id.equals("0")) {
								response.sendRedirect(response.encodeRedirectURL("enquiry-quickadd.jsp?contact_id=" + enquiry_contact_id));
							} else {
								response.sendRedirect(response.encodeRedirectURL("enquiry-quickadd.jsp"));
							}
						}
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if (!emp_branch_id.equals("0")) {
			branch_id = emp_branch_id;
		} else {
			branch_id = PadQuotes(request.getParameter("dr_branch_id"));
		}
	}

	protected void CheckForm() {
		msg = "";
		if (branch_id.equals("0") && emp_branch_id.equals("0")) {
			msg = "<br>Select Branch!";
		}
	}
}
