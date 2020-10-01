package axela.preowned;
//sangita

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
//import cloudify.connect.Connect_Pre Owned;

public class Preowned_Dash_EnquiryFollowup extends Connect {

	public String msg = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String branch_id = "0";
	public String preowned_id = "0";
	public String preowned_title = "";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String StrSql = "";
	public String followupHTML = "";
	public String customerdetail = "";
	public String preownedfollowup_preownedfeedbacktype_id = "";
	public String preownedfollowup_followup_time = "";
	public String preownedfollowup_entry_id = "0";
	public String preownedfollowup_entry_time = "";
	public String preownedfollowup_desc = "";
	public String submitB = "";
	public String preowned_preownedstatus_id = "0";
	public String preownedfollowup_emp_id = "0";
	public String preownedfollowup_preownedfollowuptype_id = "0";
	public String delete = "";
	public String preownedfollowup_id = "0";
	public String enquiry_id = "0";
	public String current_preownedfollowup_time = "";
	public String status = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0"))
			{
				emp_id = CNumeric(GetSession("emp_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				CheckPerm(comp_id, "emp_preowned_access", request, response);
				msg = PadQuotes(request.getParameter("msg"));
				preowned_id = CNumeric(PadQuotes(request.getParameter("preowned_id")));
//				SOP("preowned_id--------" + preowned_id);
				submitB = PadQuotes(request.getParameter("submit_button"));
				delete = PadQuotes(request.getParameter("Delete"));
				preownedfollowup_id = CNumeric(PadQuotes(request.getParameter("preownedfollowup_id")));
				enquiry_id = CNumeric(ExecuteQuery("SELECT preowned_enquiry_id FROM " + compdb(comp_id) + "axela_preowned WHERE preowned_id =" + preowned_id + ""));
//				SOP("enquiry_id--------" + enquiry_id);
				getOpportunityDetails(response);
				followupHTML = ListFollowup(enquiry_id, comp_id);
				customerdetail = ListCustomerDetails();

			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void getOpportunityDetails(HttpServletResponse response) {
		try {
			StrSql = " SELECT preowned_emp_id, preowned_preownedstatus_id, preowned_title, "
					+ " COALESCE((SELECT preownedfollowup_followup_time "
					+ " FROM " + compdb(comp_id) + "axela_preowned_followup "
					+ " WHERE preownedfollowup_desc='' and preownedfollowup_preowned_id=" + preowned_id + " ORDER BY preownedfollowup_id desc limit 1),'') AS preownedfollowup_followup_time"
					+ " FROM " + compdb(comp_id) + "axela_preowned"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = preowned_branch_id  "
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = preowned_emp_id "
					+ " WHERE preowned_id = " + preowned_id + BranchAccess + ExeAccess + ""
					+ " GROUP BY preowned_id "
					+ " ORDER BY preowned_id desc ";
			// SOP("StrSql---"+StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					preowned_title = crs.getString("preowned_title");
					preownedfollowup_emp_id = crs.getString("preowned_emp_id");
					preowned_preownedstatus_id = crs.getString("preowned_preownedstatus_id");
					current_preownedfollowup_time = strToLongDate(crs.getString("preownedfollowup_followup_time"));
				}
			} else {
				response.sendRedirect("../portal/error.jsp?msg=Invalid Pre Owned!");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String ListCustomerDetails() {
		StringBuilder Str = new StringBuilder();
		StrSql = " SELECT customer_id, customer_name, contact_id, "
				+ " CONCAT(title_desc,' ', contact_fname, ' ', contact_lname) AS contacts, contact_mobile1 "
				+ " FROM " + compdb(comp_id) + "axela_preowned_followup "
				+ " INNER JOIN " + compdb(comp_id) + "axela_preowned ON preowned_id = preownedfollowup_preowned_id "
				+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = preowned_customer_id "
				+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = preowned_contact_id "
				+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
				+ " WHERE preownedfollowup_preowned_id = " + preowned_id
				+ " GROUP BY customer_id";
		// SOP("StrSql: " + StrSql);
		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			Str.append("<div class=\"table-responsive table-bordered\">\n");
			Str.append("<table class=\"table table-responsive\" data-filter=\"#filter\">\n");
			Str.append("<tr>\n");
			Str.append("");
			while (crs.next()) {
				// Str.append("<tr align=center>\n");
				Str.append("<td align=center>Customer: <a href=\"../customer/customer-list.jsp?customer_id=").append(crs.getString("customer_id")).append(" \">")
						.append(crs.getString("customer_name"))
						.append(" (").append(crs.getString("customer_id")).append(")</td>\n");
				Str.append("<td align=center>Contact: <a href=\"../customer/customer-contact-list.jsp?contact_id=").append(crs.getString("contact_id")).append(" \">")
						.append(crs.getString("contacts"))
						.append("</a></td>\n");
				Str.append("<td align=center>Mobile: ").append(crs.getString("contact_mobile1")).append("</td>\n");
				Str.append("</tr>");
			}
			Str.append("</table>\n");
			Str.append("</div>\n");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String ListFollowup(String enquiry_id, String comp_id) throws SQLException,
			IOException {
		StringBuilder Str = new StringBuilder();
		if (!comp_id.equals("0"))
		{
			StrSql = " SELECT * FROM ("
					+ " (SELECT followup_id, followup_enquiry_id, followup_followup_time,followup_desc, "
					+ " followup_entry_time, followup_entry_id, followup_emp_id,"
					+ " followuptype_name, followup_modified_id, followup_modified_time,"
					+ " COALESCE(feedbacktype_name, '') AS feedbacktype_name,"
					+ " CONCAT(exe.emp_name,' (',exe.emp_ref_no,')') AS followup_by,"
					+ " CONCAT(entry.emp_name,' (',entry.emp_ref_no,')') AS entry_by,"
					+ " COALESCE(CONCAT(modified.emp_name,' (',modified.emp_ref_no,')'), '') AS modified_by  "
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_followup "
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = followup_enquiry_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp exe ON exe.emp_id = followup_emp_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp entry ON entry.emp_id = followup_entry_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_followup_type ON followuptype_id = followup_followuptype_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry_followup_feedback_type ON feedbacktype_id = followup_feedbacktype_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp modified ON modified.emp_id = followup_modified_id"
					+ " WHERE 1 = 1"
					+ " AND followup_enquiry_id = " + enquiry_id
					+ " )"
					+ " UNION "
					+ " (SELECT 0, crm_enquiry_id, crm_followup_time, crm_desc,"
					+ " crm_entry_time, crm_entry_id, crm_emp_id, 'CRM Follow-up',"
					+ " crm_modified_id, crm_modified_time,"
					+ " COALESCE(crmfeedbacktype_name,'') AS crmfeedbacktype_name, CONCAT(exe.emp_name,' (',exe.emp_ref_no,')') AS followup_by,"
					+ " CONCAT(entry.emp_name,' (',entry.emp_ref_no,')') AS entry_by,"
					+ " COALESCE(CONCAT(modified.emp_name,' (',modified.emp_ref_no,')'), '') AS modified_by  "
					+ " FROM " + compdb(comp_id) + "axela_sales_crm "
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = crm_enquiry_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp exe ON exe.emp_id = crm_emp_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp entry ON entry.emp_id = crm_entry_id "
					+ " LEFT JOIN axela_sales_crm_feedbacktype ON crmfeedbacktype_id = crm_crmfeedbacktype_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp modified ON modified.emp_id = crm_modified_id "
					+ " WHERE 1 = 1 and crm_enquiry_id = " + enquiry_id
					+ " AND crm_desc !='')" + " ) AS t "
					+ " ORDER BY followup_followup_time ";
			SOP("StrSql---------ListFollowup---------" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			try {
				int count = 0;
				if (crs.isBeforeFirst()) {
					Str.append("<div class=\"table-responsive table-bordered table-hover\">\n");
					Str.append("<table class=\"table table-bordered table-responsive\" data-filter=\"#filter\">");
					Str.append("<thead>\n");
					Str.append("<tr align=center>\n");
					Str.append("<th width=5% data-toggle=\"true\">#</th>\n");
					Str.append("<th>Time</th>\n");
					Str.append("<th width=\"10%\">Follow-up Type</th>\n");
					Str.append("<th data-hide=\"phone\">Follow-up Description</th>\n");
					Str.append("<th width=\"10%\" data-hide=\"phone\">Feedback Type</th>\n");
					Str.append("<th data-hide=\"phone\">Pre-Owned Consultant</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Feedback by</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Entry by</th>\n");
					Str.append("</tr>\n");
					Str.append("</thead>\n");
					Str.append("<tbody>\n");
					while (crs.next()) {
						count++;
						Str.append("<tr>\n");
						Str.append("<td valign=top align=center >").append(count).append("</td>");
						Str.append("<td valign=top align=left >")
								.append(strToLongDate(crs.getString("followup_followup_time")));
						if (emp_id.equals("1") && !crs.getString("followup_id").equals("0")) {
							Str.append("<br><a href=\"enquiry-dash.jsp?Delete=yes&enquiry_id=")
									.append(crs.getString("followup_enquiry_id"))
									.append("&followup_id=")
									.append(crs.getString("followup_id"))
									.append(" \">Delete Follow-up</a>");
						}
						Str.append("</td>");
						Str.append("<td valign=top align=left >").append(crs.getString("followuptype_name")).append("</td>");
						Str.append("<td valign=top align=left >").append(crs.getString("followup_desc")).append("</td>");
						Str.append("<td valign=top align=left >").append(crs.getString("feedbacktype_name")).append("</td>");

						Str.append("<td valign=top align=left >");
						if (!crs.getString("followup_emp_id").equals("0")) {
							Str.append("<a href=../portal/executive-summary.jsp?emp_id=")
									.append(crs.getString("followup_emp_id"))
									.append(">").append(crs.getString("followup_by"))
									.append("</a>");

						}
						Str.append("&nbsp;</td>");
						Str.append("<td valign=top align=left >");
						if (!crs.getString("followup_modified_id").equals("0")) {
							Str.append("<a href=../portal/executive-summary.jsp?emp_id=")
									.append(crs.getString("followup_modified_id"))
									.append(">").append(crs.getString("modified_by")).append("</a>");
							Str.append("<br>").append(strToLongDate(crs.getString("followup_modified_time")));
						}
						Str.append("&nbsp;</td>");
						Str.append("<td valign=top align=left >");
						if (!crs.getString("followup_entry_id").equals("0")) {
							Str.append("<a href=../portal/executive-summary.jsp?emp_id=")
									.append(crs.getString("followup_entry_id"))
									.append(">").append(crs.getString("entry_by")).append("</a>");
							Str.append("<br>").append(strToLongDate(crs.getString("followup_entry_time")));
						}
						Str.append("&nbsp;</td>");
						Str.append("</tr>");
					}
					Str.append("</tbody>\n");
					Str.append("</table>\n");
					Str.append("</div>\n");
				} else {
					Str.append("<center><br><font color=red><b>No follow-up found!</b></font></center>");
					// Str.append(msg);
				}
				crs.close();
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				return "";
			}
		}
		return Str.toString();
	}

}
