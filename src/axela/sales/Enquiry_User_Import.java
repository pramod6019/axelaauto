//Shivaprasad 6/07/2015   
package axela.sales;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Enquiry_User_Import extends Connect {

	public String StrSql = "", StrHTML = "";
	public String msg = "", emp_id = "0";
	public String comp_id = "0";
	public String emp_role_id = "0";
	public String BranchAccess = "";
	public String branch_id = "0";
	public String branch_name = "";
	public String error_msg = "";

	public int count = 0;
	public Connection conntx = null;
	public Statement stmttx = null;

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		CheckSession(request, response);
		HttpSession session = request.getSession(true);
		emp_id = CNumeric(GetSession("emp_id", request));
		comp_id = CNumeric(GetSession("comp_id", request));
		CheckPerm(comp_id, "emp_enquiry_access", request, response);
		if (!comp_id.equals("0")) {
			BranchAccess = GetSession("BranchAccess", request);
			StrHTML = GetEnquiryImportList();
		}
	}
	public String GetEnquiryImportList()
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
			Str.append("</tr>\n");
			Str.append("</thead>\n");
			Str.append("<tbody>\n");
			Str.append("<tr align=\"center\">\n");
			Str.append("<td>");
			Str.append("<br><a href=enquiry-user-import-general.jsp><b>Click here to Import General Enquiries</b></a>");
			Str.append("</td>");
			Str.append("</tr>\n");
			if (crs.isBeforeFirst()) {

				while (crs.next()) {
					// SOP("enquiry-import---22-----------" + crs.getString("branch_brand_id"));
					if (crs.getString("branch_brand_id").equals("2")) { // maruti
						Str.append("<tr align=\"center\">\n");
						Str.append("<td>");
						Str.append("<br><a href=enquiry-user-import-maruti.jsp><b>Click here to Import Maruti Suzuki Enquiries</b></a>");
						Str.append("</td>");
						Str.append("</tr>\n");
					}
					if (crs.getString("branch_brand_id").equals("2") && emp_id.equals("1")) { // maruti Old Enquiries
						Str.append("<tr align=\"center\">\n");
						Str.append("<td>");
						Str.append("<br><a href=enquiry-user-import-maruti-temp.jsp><b>Click here to Import Maruti Old Enquiries</b></a>");
						Str.append("</td>");
						Str.append("</tr>\n");
					}
					if (crs.getString("branch_brand_id").equals("57")) { // maserati
						Str.append("<tr align=\"center\">\n");
						Str.append("<td>");
						Str.append("<br><a href=enquiry-user-import-maserati.jsp><b>Click here to Import Maserati Enquiries</b></a>");
						Str.append("</td>");
						Str.append("</tr>\n");
					}
					if (crs.getString("branch_brand_id").equals("52")) { // Audi
						Str.append("<tr align=\"center\">\n");
						Str.append("<td>");
						Str.append("<br><a href=enquiry-user-import-audi.jsp><b>Click here to Import Audi Enquiries</b></a>");
						Str.append("</td>");
						Str.append("</tr>\n");
					}
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