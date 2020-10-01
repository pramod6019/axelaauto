package axela.preowned;
/*sangita 1st july 2014*/

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Preowned_Dash_CRMFollowup extends Connect {

	public String msg = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String StrSql = "";
	public String followupHTML = "";
	public String customerdetail = "";
	public String precrmfollowup_id = "", crm = "";
	public String preowned_id = "0";
	public String preowned_title = "";
	public String preowned_enquirytype_id = "";
	public String preowned_emp_id = "";
	public String preowned_branch_id = "";
	public String preowned_status_id = "0";
	public String delete = "";
	public String preownedpsf_id = "", psf = "";

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
				delete = PadQuotes(request.getParameter("delete"));
				crm = PadQuotes(request.getParameter("crm"));
				psf = PadQuotes(request.getParameter("psf"));
				preownedpsf_id = CNumeric(PadQuotes(request.getParameter("preownedpsf_id")));
				precrmfollowup_id = CNumeric(PadQuotes(request.getParameter("precrmfollowup_id")));
				getPreownedDetails(response);
				followupHTML = ListCRMFollowup(response);
				customerdetail = ListCustomerDetails();

				if ("yes".equals(delete) && emp_id.equals("1")) {
					DeleteFields();
					if (crm.equals("yes")) {
						response.sendRedirect(response.encodeRedirectURL("preowned-dash-crmfollowup.jsp?preowned_id=" + preowned_id + "&msg=CRM Follow-up deleted successfully!"));
					} else if (psf.equals("yes")) {
						response.sendRedirect(response.encodeRedirectURL("preowned-dash-crmfollowup.jsp?preowned_id=" + preowned_id + "&msg=PSF deleted successfully!"));
					}
				}
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

	protected void getPreownedDetails(HttpServletResponse response) {
		try {
			StrSql = " SELECT preowned_emp_id, preowned_title, preowned_branch_id, preowned_preownedstatus_id "
					+ " FROM " + compdb(comp_id) + "axela_preowned"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id=preowned_branch_id  "
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp on emp_id = preowned_emp_id "
					+ " WHERE preowned_id = " + preowned_id + BranchAccess + ExeAccess + ""
					+ " GROUP BY preowned_id "
					+ " order by preowned_id desc ";
			// SOP("StrSql---" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					preowned_title = crs.getString("preowned_title");
					// preowned_enquirytype_id =
					// crs.getString("preowned_enquirytype_id");
					preowned_branch_id = crs.getString("preowned_branch_id");
					preowned_emp_id = crs.getString("preowned_emp_id");
					preowned_status_id = crs.getString("preowned_preownedstatus_id");
				}
			} else {
				response.sendRedirect("../portal/error.jsp?msg=Invalid Opportunity!");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String ListCustomerDetails() {
		StringBuilder Str = new StringBuilder();
		StrSql = " SELECT customer_id, customer_name, contact_id,"
				+ " CONCAT(title_desc,' ', contact_fname, ' ', contact_lname) AS contacts, contact_mobile1, variant_name"
				+ " FROM " + compdb(comp_id) + "axela_preowned_crmfollowup"
				+ " INNER JOIN " + compdb(comp_id) + "axela_preowned ON preowned_id = precrmfollowup_preowned_id"
				+ " INNER JOIN axela_preowned_variant on variant_id = preowned_variant_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = preowned_customer_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = preowned_contact_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
				+ " WHERE precrmfollowup_preowned_id = " + preowned_id
				+ " GROUP BY customer_id";
		// SOP("StrSql: " + StrSql);
		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			Str.append("<div class=\"portlet-body portlet-empty\">");
			Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
			while (crs.next()) {
				Str.append("<tr align=center>\n");
				Str.append("<td align=center>Customer: <a href=\"../customer/customer-list.jsp?customer_id=").append(crs.getString("customer_id")).append(" \">")
						.append(crs.getString("customer_name"))
						.append(" (").append(crs.getString("customer_id")).append(")</td>\n");
				Str.append("<td align=center>Contact: <a href=\"../customer/customer-contact-list.jsp?contact_id=").append(crs.getString("contact_id")).append(" \">")
						.append(crs.getString("contacts"))
						.append("</a></td>\n");
				Str.append("<td align=center>Mobile: ").append(crs.getString("contact_mobile1")).append("</td>\n");
				Str.append("<td align=center>Variant: ").append(crs.getString("variant_name")).append("</td>\n");
				Str.append("</tr>");
			}
			Str.append("</table>\n");
			Str.append("</div>");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String ListCRMFollowup(HttpServletResponse response) {
		StringBuilder Str = new StringBuilder();
		StrSql = " SELECT precrmfollowup_id, precrmfollowup_preowned_id, precrmfollowup_followup_time, precrmfollowup_desc, "
				+ " precrmfollowup_entry_time, precrmfollowup_entry_id, precrmfollowup_modified_id, "
				+ " COALESCE(precrmfollowup_modified_time,'') AS precrmfollowup_modified_time, "
				+ " COALESCE(precrmfeedbacktype_name,'') AS precrmfeedbacktype_name, "
				+ " COALESCE (CASE WHEN precrmfollowup_satisfied = 0 THEN '' END,"
				+ " CASE WHEN precrmfollowup_satisfied = 1 THEN 'Satisfied' END,"
				+ " CASE WHEN precrmfollowup_satisfied = 2 THEN 'Dis-Satisfied' END,'') AS precrmfollowup_satisfied,"
				+ " COALESCE(CONCAT(e.emp_name,' (',e.emp_ref_no,')'),'') AS entry_by, "
				+ " COALESCE(CONCAT(m.emp_name,' (',m.emp_ref_no,')'),'') AS modified_by, "
				+ " COALESCE(CONCAT(c.emp_name,' (',c.emp_ref_no,')'),'') AS emp_name,"
				+ " precrmfollowupdays_daycount, precrmfollowupdays_desc, precrmfollowup_crm_emp_id"
				+ " FROM " + compdb(comp_id) + "axela_preowned_crmfollowup  "
				+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_crmfollowupdays ON precrmfollowup_precrmfollowupdays_id = precrmfollowupdays_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_preowned ON preowned_id = precrmfollowup_preowned_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_emp AS c ON c.emp_id = precrmfollowup_crm_emp_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_emp AS e ON e.emp_id = precrmfollowup_entry_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_emp AS m ON m.emp_id = precrmfollowup_modified_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_preowned_crmfeedbacktype ON precrmfeedbacktype_id = precrmfollowup_precrmfeedbacktype_id"
				+ " WHERE 1 = 1 AND precrmfollowup_preowned_id = " + preowned_id + ""
				+ " ORDER BY precrmfollowup_id ";
		// SOP("StrSql--" + StrSql);
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
				Str.append("<th data-hide=\"phone\">CRM Follow-up Days</th>\n");
				Str.append("<th data-hide=\"phone\">CRM Feedback Type</th>\n");
				Str.append("<th width=\"10%\" data-hide=\"phone\">CRM Follow-up Description</th>\n");
				Str.append("<th data-hide=\"phone\">CRM Executive</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Entry by</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Update</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");

				while (crs.next()) {
					// precrmfollowup_id =
					// crs.getString("precrmfollowup_id");
					count++;
					Str.append("<tr>\n");
					Str.append("<td valign=top align=center >").append(count).append("</td>");
					Str.append("<td valign=top align=center >").append(strToLongDate(crs.getString("precrmfollowup_followup_time")));
					if (emp_id.equals("1")) {
						Str.append("<br><a href=\"preowned-dash-crmfollowup.jsp?delete=yes&crm=yes&preowned_id=").append(crs.getString("precrmfollowup_preowned_id"))
								.append("&precrmfollowup_id=").append(crs.getString("precrmfollowup_id")).append(" \">Delete CRM Follow-up</a>");
					}
					Str.append("</td>");
					Str.append("<td valign=top align=left>").append(crs.getString("precrmfollowupdays_daycount")).append(crs.getString("precrmfollowupdays_desc")).append("</td>\n");
					Str.append("<td valign=top align=center >").append(crs.getString("precrmfeedbacktype_name"));
					Str.append("<br>" + crs.getString("precrmfollowup_satisfied")).append("</td>");
					Str.append("<td valign=top align=left >").append(crs.getString("precrmfollowup_desc")).append("</td>");
					Str.append("<td valign=top align=left >").append("<a href=../portal/executive-summary.jsp?emp_id=").append(crs.getInt("precrmfollowup_crm_emp_id")).append(">")
							.append(crs.getString("emp_name")).append("</a>").append("</td>");
					Str.append("<td valign=top align=left >");
					if (!crs.getString("precrmfollowup_entry_id").equals("0") && crs.getString("precrmfollowup_modified_id").equals("0")) {
						Str.append("<a href=../portal/executive-summary.jsp?emp_id=").append(crs.getInt("precrmfollowup_entry_id")).append(">").append(crs.getString("entry_by")).append("</a>");
						Str.append("<br>").append(strToLongDate(crs.getString("precrmfollowup_entry_time"))).append("");
					}
					if (!crs.getString("precrmfollowup_modified_id").equals("0")) {
						Str.append("<a href=../portal/executive-summary.jsp?emp_id=").append(crs.getInt("precrmfollowup_modified_id")).append(">").append(crs.getString("modified_by"))
								.append("</a>");
						Str.append("<br>").append(strToLongDate(crs.getString("precrmfollowup_modified_time"))).append("");
					}
					Str.append("&nbsp;</td>");
					Str.append("<td valign=top align=left >");
					Str.append("<a href=../preowned/preowned-crmfollowup-update.jsp?update=yes&preowned_id=").append(crs.getInt("precrmfollowup_preowned_id")).append("&precrmfollowup_id=")
							.append(crs.getInt("precrmfollowup_id")).append(">Update CRM Follow-up</a>");
					Str.append("</td>");
					Str.append("</tr>");
				}
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");

				crs.close();
			} else {
				Str.append("<br><br><font color=red><b>No CRM Follow-up found!</b></font><br><br><br><br><br><br>");
			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	protected void DeleteFields() {
		try {
			if (crm.equals("yes")) {
				// SOP("delete crm");
				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_preowned_crmfollowup"
						+ " WHERE precrmfollowup_id =" + precrmfollowup_id + "";
				updateQuery(StrSql);
			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
