//Shivaprasad 6/07/2015   
package axela.service;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Booking_Followup extends Connect {

	public String StrSql = "", StrHTML = "";
	public String msg = "", emp_id = "0", errormsg = "";
	public String comp_id = "0";
	public String emp_role_id = "0";
	public String savePath = "", importdocformat = "";
	public long docsize;
	public String[] docformat;
	public String displayform = "no";
	public String RefreshForm = "";
	public String fileName = "";
	public String str1[] = {"", "", "", "", "", "", "", "", ""};
	public String doc_value = "";
	public int propcount = 0;
	public String jc_entry_id = "0";
	public String jc_entry_date = "";
	public String BranchAccess = "";
	public String branch_id = "0", jc_branch_id = "0";
	public String branch_name = "";
	public String upload = "";
	public String similar_comm_no = "";
	public String jc_id = "0";
	public String vehkms_id = "0";
	public String jc_reg_no = "0", jc_ro_no = "", jc_no = "", error_msg = "";
	public String jc_error_msg = "";

	public int count = 0;
	public Connection conntx = null;
	public Statement stmttx = null;

	public void doPost(HttpServletRequest request, HttpServletResponse response) {

		HttpSession session = request.getSession(true);
		emp_id = CNumeric(GetSession("emp_id", request));
		comp_id = CNumeric(GetSession("comp_id", request));

		CheckSession(request, response);
		BranchAccess = CheckNull(session.getAttribute("BranchAccess"));
		CheckPerm(comp_id, "emp_service_vehicle_access", request, response);
		// jc_entry_id = emp_id;

		StrHTML = GetJobCardImportList();

	}
	public String GetJobCardImportList()
	{
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT branch_brand_id"
					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " WHERE 1=1"
					+ " " + BranchAccess
					+ " GROUP BY branch_brand_id"
					+ " ORDER BY branch_brand_id";
			// SOP("StrSql----------------" + StrSql);
			// SOP("BranchAccess---------------" + BranchAccess);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<div class=\"table-responsive\">\n");
			Str.append("<table class=\"table table-hover table-bordered table-responsive\" data-filter=\"#filter\">\n");
			Str.append("<thead><tr>\n");
			// Str.append("<th>Stock Import</th>");
			Str.append("</tr>\n");
			Str.append("</thead>\n");
			Str.append("<tbody>\n");
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					SOP("branch_brand_id---22-------" + crs.getString("branch_brand_id"));
					Str.append("<tr align=\"center\">\n");
					if (crs.getString("branch_brand_id").equals("2")) {
						Str.append("<td>");
						Str.append("<br><a href=booking-followup-maruti.jsp><b>Click here to Import Follow-Up Maruti</b></a>");
						Str.append("</td>");
					}
					/*
					 * else if (crs.getString("branch_brand_id").equals("6")) { Str.append("<td>"); Str.append(
					 * "<br><a href=jobcard-user-import-hyundai.jsp><b>Click Here To Import Hyundai Job Cards</b></a>" ); Str.append("</td>"); }
					 */
					// else if (crs.getString("branch_brand_id").equals("7")) {
					// Str.append("<td>");
					// Str.append("<br><a href=jobcard-user-import-ford.jsp><b>Click here to Import Ford Job Cards</b></a>");
					// Str.append("</td>");
					// }
					// else if (crs.getString("branch_brand_id").equals("9"))
					// {
					// Str.append("<td>");
					// Str.append("<br><a href=jobcard-user-import-honda.jsp><b>Click here to Import Honda Job Cards</b></a>");
					// Str.append("</td>");
					// }

					Str.append("</tr>\n");

				}
			}
			else
			{
				Str.append("<font color=\"red\"><b> No Branch Found!</b></font>");
			}
			Str.append("</tbody>\n");
			Str.append("</table>\n");
			Str.append("</div>\n");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("AxelaAuto=== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
			return "";
		}
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}
}