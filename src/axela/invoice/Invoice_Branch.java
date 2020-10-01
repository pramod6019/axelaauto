package axela.invoice;
//aJIt 25th july
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cloudify.connect.Connect;

public class Invoice_Branch extends Connect {

	public String emp_id = "";
	public String comp_id = "0";
	public String chkPermMsg = "";
	public String go = "";
	public static String msg = "";
	public String StrSql = "";
	public String branch_id = "0", emp_branch_id = "0";
	public String rateclass_id = "";
	public String para = "";
	public String heading = "";
	public String emp_role_id = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0"))
			{
				emp_id = CNumeric(GetSession("emp_id", request));
				emp_branch_id = CNumeric(GetSession("emp_branch_id", request));
				emp_role_id = CNumeric(GetSession("emp_role_id", request));
				CheckPerm(comp_id, "emp_quote_add, emp_invoice_add", request, response);
				para = PadQuotes(request.getParameter("para"));
				go = PadQuotes(request.getParameter("go_button"));
				msg = PadQuotes(request.getParameter("msg"));

				if (emp_branch_id.equals("") || emp_branch_id.equals("0")) {
					branch_id = PadQuotes(request.getParameter("dr_branch_id"));
				} else {
					branch_id = emp_branch_id;
				}
				if (para.equals("quote")) {
					heading = "Quote Branch";
				}
				// else if (para.equals("invoice")) {
				// heading = "Invoice Branch";
				// }
				if (go.equals("GO") || !emp_branch_id.equals("0")) {
					GetValues(request, response);
					CheckForm();
					if (msg.equals("")) {
						SetSession("quote_branch_id", branch_id, request);
						SetSession("quote_rateclass_id", rateclass_id, request);
					}
					if (msg.equals("") && para.equals("quote")) {
						response.sendRedirect(response.encodeRedirectURL("../invoice/quote-update.jsp?add=yes"));
					}
					// else if (msg.equals("") && para.equals("invoice")) {
					// response.sendRedirect(response.encodeRedirectURL("invoice-update.jsp?add=yes"));
					// }
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

		StrSql = "SELECT rateclass_id from " + compdb(comp_id) + "axela_branch"
				+ " inner join " + compdb(comp_id) + "axela_rate_class on rateclass_id = branch_rateclass_id"
				+ " where branch_id = " + branch_id + "";
		// SOP("StrSql=rateclass_id=" + StrSqlBreaker(StrSql));
		rateclass_id = ExecuteQuery(StrSql);
	}

	protected void CheckForm() {
		msg = "";
		if (branch_id.equals("0") && emp_branch_id.equals("0")) {
			msg = "<br>Select Branch!";
		}
	}
}
