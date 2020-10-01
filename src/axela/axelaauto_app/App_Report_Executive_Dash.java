package axela.axelaauto_app;

//Manjur 13/05/2015
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import axela.portal.Header;
import cloudify.connect.Connect;
//import java.util.GregorianCalendar;

public class App_Report_Executive_Dash extends Connect {

	public String StrSql = "";
	public static String msg = "";
	public String comp_id = "0";
	public String emp_id = "", branch_id = "";
	public String exe_id = "";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public int enquirycount = 0;
	public int testdrives = 0;
	public int salesordercount = 0;
	public int deliverycount = 0;
	public String enquiry_count = "";
	public String StrSearch = "";
	public String emp_uuid = "";
	public String emp_all_exe = "0";
	public String access = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(PadQuotes(request.getParameter("comp_id")));
			emp_uuid = PadQuotes(request.getParameter("emp_uuid"));
			CheckAppSession(emp_uuid, comp_id, request);
			emp_id = CNumeric(session.getAttribute("emp_id") + "");
			new Header().UserActivity(emp_id, request.getRequestURI(), "1", comp_id);
			if (!emp_id.equals("0")) {
				// CheckPerm(comp_id, "emp_report_access, emp_mis_access", request, response);
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				// emp_all_exe = GetSession("emp_all_exe", request);
				// if (emp_all_exe.equals("1"))
				// {
				// ExeAccess = "";
				// }
				access = ReturnPerm(comp_id, "emp_report_access, emp_mis_access", request);
				if (access.equals("0"))
				{
					response.sendRedirect("callurl" + "app-error.jsp?msg=Access denied. Please contact system administrator!");
				}
				GetValues(request, response);
				if (exe_id.equals("0")) {
					exe_id = emp_id;
				}

				if (!exe_id.equals("")) {
					StrSearch = StrSearch + " AND emp_id IN (" + exe_id + ")";
				}
				StrSearch += BranchAccess
						+ ExeAccess;
				if (!exe_id.equals("0")) {
					ListEnquiryMonthly();
				}

			}
		} catch (Exception ex) {
			SOPError("Axelaauto==App" + this.getClass().getName());
			SOPError("Error IN " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		exe_id = CNumeric(PadQuotes(request.getParameter("dr_executive")));

	}

	public void ListEnquiryMonthly() {

		// SOP("count===" + i);
		// for (int i = 12; i > 0; i--) {
		// StrSql += " CONCAT(YEAR(DATE_SUB('" + dr_month + "', INTERVAL " + i + " month)),'-',MONTHNAME(DATE_SUB('" + dr_month + "', INTERVAL " + i + " month))) AS month, "
		StrSql = "  SELECT COALESCE((SELECT COUNT(enquiry_id)"
				+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id=enquiry_emp_id "
				// + " WHERE SUBSTR(enquiry_date,1,6)= DATE_FORMAT(DATE_SUB('" + dr_month + "', INTERVAL " + i + " month),'%Y%m'))"
				+ " WHERE SUBSTR(enquiry_date,1,6)= SUBSTR('" + ToLongDate(kknow()) + "',1,6)"
				+ StrSearch + "), 0) AS 'enquiry',"

				+ " (SELECT COALESCE((SELECT COUNT(so_id)"
				+ " FROM " + compdb(comp_id) + "axela_sales_so"
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = so_branch_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id=so_emp_id "
				+ " WHERE so_active=1 "
				// + " AND SUBSTR(so_date,1,6)=DATE_FORMAT(DATE_SUB('" + dr_month + "', INTERVAL " + i + " month),'%Y%m'))"
				+ " AND SUBSTR(so_date,1,6)= substr('" + ToLongDate(kknow()) + "',1,6)"
				+ StrSearch.replace("enquiry_model_id", "so_model_id") + "), 0)) AS 'salesorders',"

				+ " (SELECT COALESCE((SELECT COUNT( DISTINCT testdrive_id)"
				+ " FROM " + compdb(comp_id) + "axela_sales_testdrive"
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = testdrive_enquiry_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = enquiry_emp_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_team_exe dupteamexe ON dupteamexe.teamtrans_emp_id = enquiry_emp_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_team dupteam ON dupteam.team_id = dupteamexe.teamtrans_team_id"
				+ " AND dupteam.team_active = 1"
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch dupbranch ON dupbranch.branch_id = emp_branch_id"
				+ " AND emp_active = 1"
				+ " WHERE 1=1  "
				+ " AND testdrive_fb_taken = 1"
				// + " AND SUBSTR(testdrive_time,1,6)=DATE_FORMAT(DATE_SUB('" + dr_month + "', INTERVAL " + i + " month),'%Y%m'))"
				+ " AND SUBSTR(testdrive_time,1,6)= substr('" + ToLongDate(kknow()) + "',1,6)"
				+ StrSearch + "), 0)) AS 'testdrives',"

				+ " (SELECT COALESCE((SELECT COUNT(so_id)"
				+ " FROM " + compdb(comp_id) + "axela_sales_so"
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = so_branch_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id=so_emp_id "
				+ " WHERE so_active=1 "
				// + " AND SUBSTR(so_delivered_date,1,6)=DATE_FORMAT(DATE_SUB('" + dr_month + "', INTERVAL " + i + " month),'%Y%m'))"
				+ " AND SUBSTR(so_delivered_date,1,6) = SUBSTR('" + ToLongDate(kknow()) + "',1,6)"
				+ StrSearch + "), 0)) AS 'deliveries'";
		// }
		// SOP("StrSql-----ListEnquiryMonthly---------=" + StrSqlBreaker(StrSql));
		try {
			CachedRowSet crs = null;
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				// chart_data = "[";
				while (crs.next()) {
					enquirycount = crs.getInt("enquiry");
					testdrives = crs.getInt("testdrives");
					salesordercount = crs.getInt("salesorders");
					deliverycount = crs.getInt("deliveries");

				}

			} /*
			 * else { // NoChart = "<font color=red><b>No Data Found!</b></font>"; }
			 */
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}

	}

	public String PopulateSalesExecutives() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT emp_id, CONCAT(emp_name,' (',emp_ref_no,')') AS emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id=emp_id"
					+ " WHERE emp_active = '1'"
					+ " AND emp_sales='1'"
					+ ExeAccess;

			StrSql = StrSql + " GROUP BY emp_id ORDER BY emp_name";
			// SOP("PopulateSalesExecutives-----------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			// Str.append("<select name=dr_executive id=dr_executive class=textbox multiple=\"multiple\" size=10 style=\"width:250px\">");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id")).append("");
				Str.append(StrSelectdrop(crs.getString("emp_id"), exe_id));
				Str.append(">").append(crs.getString("emp_name")).append("</option> \n");
			}
			// Str.append("</select>");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto-App " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
}
