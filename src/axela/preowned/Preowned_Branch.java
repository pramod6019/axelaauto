package axela.preowned;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cloudify.connect.Connect;

public class Preowned_Branch extends Connect {

	public String go = "";
	public String comp_id = "0";
	public static String msg = "";
	public String StrSql = "";
	public String branch_id = "", emp_branch_id = "";
	public String para = "";
	public String heading = "";
	public String preowned_contact_id = "0";
	public String enquiry_id = "0";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0"))
			{
				emp_branch_id = CNumeric(GetSession("emp_branch_id", request));
				CheckPerm(comp_id, "emp_preowned_add", request, response);
				para = PadQuotes(request.getParameter("para"));
				go = PadQuotes(request.getParameter("go_button"));
				msg = PadQuotes(request.getParameter("msg"));
				preowned_contact_id = CNumeric(PadQuotes(request.getParameter("contact_id")));
				enquiry_id = CNumeric(PadQuotes(request.getParameter("enquiry_id")));
				if (para.equals("preowned")) {
					heading = "Pre-Owned Branch";
				}
				if (go.equals("GO") || !emp_branch_id.equals("0")) {
					GetValues(request, response);
					CheckForm();
					if (msg.equals("")) {
						SetSession("preowned_branch_id", branch_id, request);
						if (para.equals("preowned")) {
							if (!preowned_contact_id.equals("0") && !enquiry_id.equals("0")) {
								response.sendRedirect(response.encodeRedirectURL("preowned-quickadd.jsp?contact_id=" + preowned_contact_id + "&enquiry_id=" + enquiry_id));
							} else {
								response.sendRedirect(response.encodeRedirectURL("preowned-quickadd.jsp"));
							}
						}
					}
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
