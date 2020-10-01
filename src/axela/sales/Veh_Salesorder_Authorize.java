package axela.sales;
//aJIt 10th December, 2012

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Veh_Salesorder_Authorize extends Connect {

	public String emp_id = "0";
	public String comp_id = "0";
	public String updateB = "";
	public String StrSql = "";
	public String so_id = "0";
	public String so_auth_date = "";
	public String so_authdate = "";
	public String so_auth = "";
	public String so_no = "";
	public String BranchAccess = "", ExeAccess = "";
	public String so_auth_id = "0";
	public String so_authorized_by = "";
	public String link_customer_name = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_sales_order_authorize", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				updateB = PadQuotes(request.getParameter("update_button"));
				so_id = CNumeric(PadQuotes(request.getParameter("so_id")));

				if (!updateB.equals("Update Sales Order")) {
					PopulateFields(request, response);
				} else if (updateB.equals("Update Sales Order")) {
					// CheckPerm(comp_id, "emp_sales_order_authorize", request, response);
					GetValues(request, response);
					UpdateFields(request, response);
					response.sendRedirect(response.encodeRedirectURL("veh-salesorder-list.jsp?so_id=" + so_id + "&msg=Sales Order updated successfully!"));
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
		so_auth = CheckBoxValue(PadQuotes(request.getParameter("chk_so_auth")));
		so_auth_id = emp_id;
		so_auth_date = ToLongDate(kknow());
	}

	protected void PopulateFields(HttpServletRequest request, HttpServletResponse response) {
		try {
			StrSql = "SELECT so_auth_date, so_auth, so_auth_id, customer_name,"
					+ " CONCAT('SO', branch_code, so_no) AS so_no, customer_id"
					+ " FROM " + compdb(comp_id) + "axela_sales_so"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = so_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = so_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = so_emp_id"
					+ " WHERE so_id = " + so_id + BranchAccess + ExeAccess + "";
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					link_customer_name = "<a href=\"../customer/customer-list.jsp?customer_id=" + crs.getString("customer_id") + "\">"
							+ crs.getString("customer_name") + "</a>";
					so_no = crs.getString("so_no");
					so_auth = crs.getString("so_auth");
					so_auth_id = crs.getString("so_auth_id");
					if (!so_auth_id.equals("")) {
						so_authorized_by = Exename(comp_id, Integer.parseInt(so_auth_id));
					}
					so_auth_date = crs.getString("so_auth_date");
					so_authdate = strToLongDate(so_auth_date);
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Sales Order!"));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void UpdateFields(HttpServletRequest request, HttpServletResponse response) throws Exception {
		StrSql = " UPDATE " + compdb(comp_id) + "axela_sales_so"
				+ " SET"
				+ " so_auth = " + so_auth + ","
				+ " so_auth_id = " + so_auth_id + ","
				+ " so_auth_date = '" + so_auth_date + "'"
				+ " WHERE so_id = " + so_id;
		updateQuery(StrSql);
	}
}
