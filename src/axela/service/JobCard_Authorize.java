package axela.service;
//divya 8th April, 2013

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class JobCard_Authorize extends Connect {

	public String emp_id = "0";
	public String comp_id = "0";
	public String updateB = "";
	public String StrSql = "";
	public String jc_id = "0";
	public String jc_auth_date = "";
	public String jc_authdate = "";
	public String jc_auth = "";
	public String jc_no = "";
	public String BranchAccess = "", ExeAccess = "";
	public String jc_auth_id = "0";
	public String jc_authorized_by = "";
	public String link_customer_name = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_service_jobcard_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				updateB = PadQuotes(request.getParameter("update_button"));
				jc_id = CNumeric(PadQuotes(request.getParameter("jc_id")));

				if (!"yes".equals(updateB)) {
					PopulateFields(request, response);
				} else if ("yes".equals(updateB)) {
					if (ReturnPerm(comp_id, "emp_service_jobcard_edit", request).equals("1")) {
						GetValues(request, response);
						UpdateFields(request, response);
						response.sendRedirect(response.encodeRedirectURL("jobcard-list.jsp?jc_id=" + jc_id + "&msg=Job Card updated successfully!"));
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
		jc_auth = CheckBoxValue(PadQuotes(request.getParameter("chk_jc_auth")));
		jc_auth_id = emp_id;
		jc_auth_date = ToLongDate(kknow());
	}

	protected void PopulateFields(HttpServletRequest request, HttpServletResponse response) {
		try {
			StrSql = "SELECT jc_auth_date, jc_auth, jc_auth_id,"
					+ " CONCAT('JC', branch_code, jc_no) AS jc_no,"
					+ " customer_name, customer_id"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = jc_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = jc_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = jc_emp_id"
					+ " WHERE jc_id = " + jc_id + BranchAccess + ExeAccess + "";
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					link_customer_name = "<a href=\"../customer/customer-list.jsp?customer_id=" + crs.getString("customer_id") + "\">" + crs.getString("customer_name") + "</a>";
					jc_no = crs.getString("jc_no");
					jc_auth = crs.getString("jc_auth");
					jc_auth_id = crs.getString("jc_auth_id");
					if (!jc_auth_id.equals("")) {
						jc_authorized_by = Exename(comp_id, Integer.parseInt(jc_auth_id));
					}
					jc_auth_date = crs.getString("jc_auth_date");
					jc_authdate = strToLongDate(jc_auth_date);
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
		StrSql = " UPDATE " + compdb(comp_id) + "axela_service_jc"
				+ " SET"
				+ " jc_auth = " + jc_auth + ","
				+ " jc_auth_id = " + jc_auth_id + ","
				+ " jc_auth_date = '" + jc_auth_date + "'"
				+ " WHERE jc_id = " + jc_id;
		updateQuery(StrSql);
	}
}
