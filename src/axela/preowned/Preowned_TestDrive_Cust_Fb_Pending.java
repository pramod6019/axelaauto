package axela.preowned;
// sn, 15 june 2013
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Preowned_TestDrive_Cust_Fb_Pending extends Connect {

	public String StrSql = "";
	// public String starttime = "", start_time = "";
	// public String endtime = "", end_time = "";
	public static String msg = "";
	public String emp_id = "", branch_id = "0";
	public String StrHTML = "";
	public String comp_id = "0";
	public String BranchAccess = "";
	public String StrSearch = "", enquiry_Model = "", item_Model = "";
	public String go = "";
	public String ExeAccess = "";
	public String brand_id = "", region_id = "", team_id = "", exe_id = "";
	public String[] brand_ids, region_ids, branch_ids, team_ids, exe_ids;
	public axela.preowned.MIS_Check mischeck = new axela.preowned.MIS_Check();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {

				emp_id = CNumeric(GetSession("emp_id", request));
				CheckPerm(comp_id, "emp_report_access, emp_mis_access, emp_testdrive_access", request, response);
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				go = PadQuotes(request.getParameter("submit_button"));
				GetValues(request, response);
				if (go.equals("Go")) {

					CheckForm();
					StrSearch = BranchAccess + " " + ExeAccess;
					if (!brand_id.equals("")) {
						StrSearch = StrSearch + " AND branch_brand_id IN (" + brand_id + ")";
					}
					if (!region_id.equals("")) {
						StrSearch = StrSearch + " AND branch_region_id IN (" + region_id + ")";
					}
					if (!branch_id.equals("")) {
						mischeck.exe_branch_id = branch_id;
						StrSearch = StrSearch + " AND branch_id IN (" + branch_id + ")";
					}
					if (!team_id.equals("")) {
						mischeck.exe_branch_id = branch_id;
						mischeck.branch_id = branch_id;
						StrSearch = StrSearch + " AND preownedteamtrans_team_id IN (" + team_id + ") ";
					}
					if (!exe_id.equals("")) {
						StrSearch = StrSearch + " AND emp_id IN (" + exe_id + ")";
					}
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						StrHTML = ListTestDriveCustFbPending();
					}
				}
			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void GetValues(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		brand_id = RetrunSelectArrVal(request, "dr_principal");
		brand_ids = request.getParameterValues("dr_principal");
		region_id = RetrunSelectArrVal(request, "dr_region");
		region_ids = request.getParameterValues("dr_region");
		branch_id = RetrunSelectArrVal(request, "dr_branch");
		branch_ids = request.getParameterValues("dr_branch");
		team_id = RetrunSelectArrVal(request, "dr_preownedteam");
		team_ids = request.getParameterValues("dr_preownedteam");
		exe_id = RetrunSelectArrVal(request, "dr_executive");
		exe_ids = request.getParameterValues("dr_executive");
	}

	protected void CheckForm() {
		msg = "";
		if (branch_id.equals("")) {
			msg += "<br>Select Branch!";
		}
	}

	public String ListTestDriveCustFbPending() {
		StringBuilder Str = new StringBuilder();

		StrSql = "SELECT customer_id, customer_name, enquiry_id, enquiry_contact_id, testdrive_emp_id,"
				+ " testdrive_id, testdrive_time, testdrive_time_from, testdrive_time_to,"
				+ " CONCAT(emp_name,' (',emp_ref_no,')') AS emp_name,"
				+ " contact_mobile1, contact_mobile2, contact_email1, contact_email2,"
				+ " CONCAT(title_desc,' ',contact_fname,' ', contact_lname) AS contactname"
				+ " FROM " + compdb(comp_id) + "axela_preowned_testdrive"
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id= testdrive_enquiry_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON enquiry_customer_id = customer_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = enquiry_emp_id";
		if (!team_id.equals("")) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_preowned_team_exe ON preownedteamtrans_emp_id = emp_id ";
		}
		StrSql += " WHERE testdrive_fb_taken = 1"
				+ " AND testdrive_client_fb_entry_id = 0"
				+ " AND branch_active = '1' " + StrSearch;
		if (!branch_id.equals("0") && !branch_id.equals("")) {
			StrSql += " AND enquiry_branch_id IN (" + branch_id + ")";
		}
		StrSql += " GROUP BY testdrive_id"
				+ " ORDER BY emp_name, testdrive_time "
				+ " LIMIT 500";

		// SOP("Customer PendingFeedback======" + StrSql);

		try {
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				int count = 0;
				Str.append("<div class=\"table-responsive table-bordered\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th data-hide=\"phone\">#</th>\n");
				Str.append("<th data-toggle=\"true\">Customer</th>\n");
				Str.append("<th>Opportunity No.</th>\n");
				Str.append("<th data-hide=\"phone\">Test Drive ID</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Time</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Pre-Owned Consultant</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");

				while (crs.next()) {
					count++;
					Str.append("<tr>\n");
					Str.append("<td align= center valign=top height=20>").append(count).append("</td>");
					Str.append("<td valign=top align=left >");
					Str.append("<a href=\"../customer/customer-list.jsp?customer_id=");
					Str.append(crs.getString("customer_id")).append("\">");
					Str.append(crs.getString("customer_name")).append("</a>");
					Str.append("<br><a href=\"../customer/customer-contact-list.jsp?contact_id=");
					Str.append(crs.getString("enquiry_contact_id")).append("\">");
					Str.append(crs.getString("contactname")).append("</a>");
					if (!crs.getString("contact_mobile1").equals("")) {
						Str.append("<br>").append(SplitPhoneNo(crs.getString("contact_mobile1"), 5, "M"));
					}
					if (!crs.getString("contact_mobile2").equals("")) {
						Str.append("<br>").append(SplitPhoneNo(crs.getString("contact_mobile2"), 5, "M"));
					}
					if (!crs.getString("contact_email1").equals("")) {
						Str.append("<br><a href=mailto:").append(crs.getString("contact_email1")).append(">").append(crs.getString("contact_email1")).append("</a>");
					}
					if (!crs.getString("contact_email2").equals("")) {
						Str.append("<br><a href=mailto:").append(crs.getString("contact_email2")).append(">").append(crs.getString("contact_email2")).append("</a>");
					}
					Str.append("</td>");
					Str.append("<td valign=top align=left>");
					Str.append("<a href=\"../sales/enquiry-dash-opportunity.jsp?enquiry_id=");
					Str.append(crs.getString("enquiry_id")).append("\">");
					Str.append(crs.getString("enquiry_id")).append("</a>");
					Str.append("</td>");
					Str.append("<td valign=top align=left><a href=testdrive-list.jsp?testdrive_id=");
					Str.append(crs.getString("testdrive_id")).append(">").append(crs.getString("testdrive_id")).append("</a></td>");
					Str.append("<td valign=top align=left >");
					if (!crs.getString("testdrive_time_to").equals("")) {
						Str.append("");
						Str.append(PeriodTime(crs.getString("testdrive_time_from"), crs.getString("testdrive_time_to"), "1")).append("");
					} else {
						Str.append("").append(strToLongDate(crs.getString("testdrive_time"))).append("");
					}
					Str.append("</td>");
					Str.append("<td valign=top align=left ><a href=../portal/executive-list.jsp?emp_id=");
					Str.append(crs.getInt("testdrive_emp_id")).append(">").append(crs.getString("emp_name")).append("</a></td>\n");
					Str.append("</tr>");
				}
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
			} else {
				Str.append("<br><br><center><font color=red><b>No Customer Test Drive Feedback Pending</b></font></center>");
			}
			crs.close();

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}
	public String PopulateTeam() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT team_id, team_name "
					+ " FROM " + compdb(comp_id) + "axela_sales_team "
					+ " WHERE team_branch_id=" + branch_id + " "
					+ " GROUP BY team_id "
					+ " ORDER BY team_name ";
			// SOP("PopulateTeam query ==== " + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("team_id")).append("");
				Str.append(ArrSelectdrop(crs.getInt("team_id"), team_ids));
				Str.append(">").append(crs.getString("team_name")).append("</option> \n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

}
