package axela.sales;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Update_Kms extends Connect {

	public String StrHTML = "", StrPostponed = "";
	public String StrSearch = "";
	public String comp_id = "0";
	public String StrSql = "";
	public String branch_id = "0";
	public String salesgatepass_in_kms = "", salesgatepass_testdriveveh_id = "0", salesgatepass_id = "0";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String go = "";
	public String msg = "", status = "Add";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {

				branch_id = CNumeric(GetSession("emp_branch_id", request));
				CheckPerm(comp_id, "emp_testdrive_edit", request, response);
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				salesgatepass_id = CNumeric(PadQuotes(request.getParameter("salesgatepass_id")));
				PopulateFields(response);
				salesgatepass_testdriveveh_id = CNumeric(PadQuotes(request.getParameter("testdriveveh_id")));
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

	protected void GetValues(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		salesgatepass_in_kms = CNumeric(PadQuotes(request.getParameter("txt_salesgatepass_in_kms")));
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT"
					+ " salesgatepass_in_kms"
					+ " FROM " + compdb(comp_id) + "axela_sales_testdrive_gatepass"
					+ " WHERE 1 = 1"
					+ " AND salesgatepass_id = " + salesgatepass_id;
			// SOP("StrSql===pop==" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					salesgatepass_in_kms = crs.getString("salesgatepass_in_kms");
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Gate Pass!"));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

}
