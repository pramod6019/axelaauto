package axela.sales;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Salesorder_Cancel extends Connect {

	public String StrHTML = "", StrPostponed = "";
	public String StrSearch = "";
	public String comp_id = "0";
	public String StrSql = "";
	public String branch_id = "0";
	public String so_notes = "";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String go = "";
	public String so_id = "0", so_active = "0";
	public String msg = "", status = "Add";
	public String so_cancelreason_id = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				// CheckPerm(comp_id, "emp_sales_order_cancel", request, response);
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				so_id = CNumeric(PadQuotes(request.getParameter("so_id")));
				so_active = CNumeric(PadQuotes(ExecuteQuery("SELECT so_active FROM " + compdb(comp_id) + "axela_sales_so WHERE so_id = " + so_id)));
				so_notes = PadQuotes(request.getParameter("notes"));
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulateCancelReason(String comp_id) {
		StringBuilder Str = new StringBuilder();
		Str.append("<select name=\"dr_cancel_reason\" class=\"form-control\" id=\"dr_cancel_reason\">");
		Str.append("<option value=\"0\">Select</option>\n");
		try {
			StrSql = "SELECT cancelreason_id, cancelreason_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_so_cancelreason"
					+ " ORDER BY cancelreason_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("cancelreason_id"));
				Str.append(StrSelectdrop(crs.getString("cancelreason_id"), so_cancelreason_id));
				Str.append(">").append(crs.getString("cancelreason_name")).append("</option>\n");
			}
			Str.append("</select>");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

}
