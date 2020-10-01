package axela.sales;
//aJIt 25th july
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Sales_Branch extends Connect {

	public String emp_id = "";
	public String comp_id = "0";
	public String chkPermMsg = "";
	public String go = "";
	public static String msg = "";
	public String StrSql = "";
	public String branch_id = "", emp_branch_id = "";
	public String rateclass_id = "";
	public String para = "";
	public String heading = "";
	public String emp_role_id = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_quote_add, emp_sales_order_add", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				emp_branch_id = CNumeric(GetSession("emp_branch_id", request));
				para = PadQuotes(request.getParameter("para"));
				go = PadQuotes(request.getParameter("go_button"));
				msg = PadQuotes(request.getParameter("msg"));
				if (para.equals("quote")) {
					heading = "Quote Branch";
				} else if (para.equals("so")) {
					heading = "Sales Order Branch";
				}
				PopulateConfigDetails();
				if (go.equals("GO") || !emp_branch_id.equals("0")) {
					GetValues(request, response);
					CheckForm();
					if (msg.equals("")) {
						SetSession("sales_branch_id", branch_id, request);
						SetSession("sales_rateclass_id	", rateclass_id, request);
					}
					if (msg.equals("") && para.equals("quote")) {
						response.sendRedirect(response.encodeRedirectURL("quote-update.jsp?add=yes"));
					} else if (msg.equals("") && para.equals("so")) {
						response.sendRedirect(response.encodeRedirectURL("salesorder-update.jsp?add=yes"));
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
		StrSql = "SELECT rateclass_id from " + compdb(comp_id) + "axela_branch"
				+ " inner join " + compdb(comp_id) + "axela_rate_class on rateclass_id = branch_rateclass_id	"
				+ " where branch_id = " + branch_id + "";
		rateclass_id = ExecuteQuery(StrSql);
	}

	protected void CheckForm() {
		msg = "";
		if (branch_id.equals("0") && emp_branch_id.equals("0")) {
			msg = "<br>Select Branch!";
		}
	}

	public void PopulateConfigDetails() {
		StrSql = "SELECT emp_role_id "
				+ " from " + compdb(comp_id) + "axela_config, " + compdb(comp_id) + "axela_emp "
				+ " where emp_id = " + emp_id;
		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			while (crs.next()) {
				emp_role_id = crs.getString("emp_role_id");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
