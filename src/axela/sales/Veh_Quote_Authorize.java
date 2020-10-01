package axela.sales;
//aJIt 10th December, 2012

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Veh_Quote_Authorize extends Connect {

	public String emp_id = "0";
	public String comp_id = "0";
	public String updateB = "";
	public String StrSql = "";
	public String quote_id = "0";
	public String quote_auth_date = "";
	public String quote_authdate = "";
	public String quote_auth = "";
	public String quote_no = "";
	public String BranchAccess = "", ExeAccess = "";
	public String quote_auth_id = "0";
	public String quote_authorized_by = "";
	public String link_customer_name = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_sales_quote_authorize", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				updateB = PadQuotes(request.getParameter("update_button"));
				quote_id = CNumeric(PadQuotes(request.getParameter("quote_id")));

				if (!updateB.equals("Update Quote")) {
					PopulateFields(request, response);
				} else if (updateB.equals("Update Quote")) {
					if (ReturnPerm(comp_id, "emp_sales_quote_authorize", request).equals("1")) {
						GetValues(request, response);
						UpdateFields(request, response);
						response.sendRedirect("veh-quote-list.jsp?quote_id=" + quote_id + "&msg=Quote updated successfully!");
					} else {
						response.sendRedirect(AccessDenied());
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
		quote_auth = CheckBoxValue(PadQuotes(request.getParameter("chk_quote_auth")));
		quote_auth_id = emp_id;
		quote_auth_date = ToLongDate(kknow());
	}

	protected void PopulateFields(HttpServletRequest request, HttpServletResponse response) {
		try {
			StrSql = "SELECT quote_auth_date, quote_auth, quote_auth_id, customer_name,"
					+ " CONCAT('QT', branch_code, quote_no) AS quote_no, customer_id"
					+ " FROM " + compdb(comp_id) + "axela_sales_quote"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = quote_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = quote_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = quote_emp_id"
					+ " WHERE quote_id = " + quote_id + BranchAccess + ExeAccess + "";
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					link_customer_name = "<a href=\"../customer/customer-list.jsp?customer_id=" + crs.getString("customer_id") + "\">"
							+ crs.getString("customer_name") + "</a>";
					quote_no = crs.getString("quote_no");
					quote_auth = crs.getString("quote_auth");
					quote_auth_id = crs.getString("quote_auth_id");
					if (!quote_auth_id.equals("0")) {
						quote_authorized_by = Exename(comp_id, Integer.parseInt(quote_auth_id));
					}
					quote_auth_date = crs.getString("quote_auth_date");
					quote_authdate = strToLongDate(quote_auth_date);
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Quote"));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void UpdateFields(HttpServletRequest request, HttpServletResponse response) throws Exception {
		StrSql = " UPDATE " + compdb(comp_id) + "axela_sales_quote"
				+ " SET"
				+ " quote_auth = " + quote_auth + ","
				+ " quote_auth_id = " + quote_auth_id + ","
				+ " quote_auth_date = '" + quote_auth_date + "'"
				+ " WHERE quote_id = " + quote_id + "";
		updateQuery(StrSql);
	}
}
