// JEET 18 DEC 2014
package axela.accounting;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Reconcile extends Connect {

	public String StrSql = "";
	public String comp_id = "0";
	public String customer_id = "0";
	public String fromdate = "";
	public String todate = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_role_id,emp_acc_reconciliation_access", request, response);
			if (!comp_id.equals("0")) {
				fromdate = DateToShortDate(kknow());
				todate = DateToShortDate(kknow());
				customer_id = CNumeric(PadQuotes(request.getParameter("dr_customer")));
			}
		} catch (Exception ex) {
			SOP(" Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);

	}

	public String PopulateLedger() {
		StringBuilder Str = new StringBuilder();
		StrSql = "SELECT customer_id, customer_name"
				+ " FROM  " + compdb(comp_id) + "axela_customer"
				+ " WHERE customer_reconciliation = 1"
				+ " GROUP BY customer_id"
				+ " ORDER BY customer_name";
		// SOP("StrSql====" + StrSql);
		try {
			Str.append("<option value=\"0\">Select Ledger</option>");
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("customer_id"));
				Str.append(Selectdrop(Integer.parseInt(crs.getString("customer_id")), customer_id));
				Str.append(">").append(crs.getString("customer_name")).append("</option>");
			}
			return Str.toString();
		} catch (Exception ex) {
			SOP(" Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
}
