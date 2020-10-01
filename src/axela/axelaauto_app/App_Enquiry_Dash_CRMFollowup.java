package axela.axelaauto_app;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class App_Enquiry_Dash_CRMFollowup extends Connect {

	public String msg = "";
	public String emp_id = "0";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String StrSql = "";
	public String crmfollowupHTML = "";
	// public String accountdetail = "";
	// public String crm_id = "0";
	public String enquiry_id = "0";
	public String enquiry_title = "";
	public String enquiry_enquirytype_id = "";
	public String enquiry_emp_id = "";
	public String enquiry_branch_id = "";
	public String enquiry_status_id = "0";
	// public String delete = "";
	// public String psf_id = "";
	// // public int recperpage = 0;
	public String comp_id = "0";
	public String emp_uuid = "";
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession(true);
			emp_id = CNumeric(session.getAttribute("emp_id") + "");
			comp_id = CNumeric(GetSession("comp_id", request));

			if (!emp_id.equals("0")) {
				// recperpage = Integer.parseInt(session.getAttribute("emp_recperpage") + "");
				BranchAccess = GetSession("BranchAccess", request);
				BranchAccess = BranchAccess.replace("branch_id", "enquiry_branch_id");
				ExeAccess = GetSession("ExeAccess", request);
				ExeAccess = ExeAccess.replace("emp_id", "enquiry_emp_id");
				// CheckPerm("emp_opportunity_access", request, response);
				msg = PadQuotes(request.getParameter("msg"));
				enquiry_id = CNumeric(PadQuotes(request.getParameter("enquiry_id")));
				getOpportunityDetails(response);
				crmfollowupHTML = ListCRMFollowup(response);
				// accountdetail = ListAccountDetails();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void getOpportunityDetails(HttpServletResponse response) {
		try {
			StrSql = " SELECT enquiry_emp_id, enquiry_title, enquiry_enquirytype_id, enquiry_branch_id, enquiry_status_id " + " FROM " + compdb(comp_id) + "axela_sales_enquiry" + " INNER JOIN "
					+ compdb(comp_id) + "axela_branch ON branch_id=enquiry_branch_id  " + " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = enquiry_emp_id " + " WHERE enquiry_id = "
					+ enquiry_id + BranchAccess + ExeAccess + "" + " GROUP BY enquiry_id " + " ORDER BY enquiry_id DESC ";
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					enquiry_title = crs.getString("enquiry_title");
					enquiry_enquirytype_id = crs.getString("enquiry_enquirytype_id");
					enquiry_branch_id = crs.getString("enquiry_branch_id");
					enquiry_emp_id = crs.getString("enquiry_emp_id");
					enquiry_status_id = crs.getString("enquiry_status_id");
				}
			} else {
				response.sendRedirect("callurlapp-error.jsp?msg=Invalid Opportunity!");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String ListCRMFollowup(HttpServletResponse response) {
		StringBuilder Str = new StringBuilder();
		StrSql = " SELECT crm_id, crm_emp_id, crm_enquiry_id, crm_so_id, crm_followup_time, crm_desc, " + " crm_entry_time, crm_entry_id, crm_modified_id," + " crm_modified_time,"
				+ " COALESCE(crmfeedbacktype_name, '') AS crmfeedbacktype_name," + " crm_crmdays_id, crmdays_daycount, crmdays_desc, crmtype_id, crmtype_name," + " crm.emp_id as crmemp_id,"
				+ " COALESCE(CONCAT(crm.emp_name,' (',crm.emp_ref_no,')'),'') AS crmemp_name," + " coalesce(CONCAT(e.emp_name,' (',e.emp_ref_no,')'),'') AS entry_by, "
				+ " COALESCE(CONCAT(m.emp_name,' (',m.emp_ref_no,')'),'') AS modified_by "
				+ " FROM " + compdb(comp_id) + "axela_sales_crm"
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_crmdays ON crmdays_id = crm_crmdays_id"
				+ " INNER JOIN axela_sales_crm_type ON crmtype_id = crmdays_crmtype_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = crm_enquiry_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_emp AS crm ON crm.emp_id = crm_emp_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_emp AS e ON e.emp_id = crm_entry_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_emp AS m ON m.emp_id = crm_modified_id"
				+ " LEFT JOIN axela_sales_crm_feedbacktype ON crmfeedbacktype_id = crm_crmfeedbacktype_id"
				+ " WHERE 1 = 1 " // + " and crmdays_crmtype_id='1'"
				+ " AND crm_enquiry_id = " + enquiry_id + " GROUP BY crm_id" + " ORDER BY crmdays_crmtype_id, crm_followup_time  ";
		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			int count = 0;
			if (crs.isBeforeFirst()) {
				Str.append("<div class=\"\">\n");
				while (crs.next()) {
					Str.append("<div class=\"col-md-9 col-xs-9\" style=\"margin-bottom:30px;\"><b>").append("Time: </b> ").append(strToLongDate(crs.getString("crm_followup_time"))).append("<br>")
							.append("<b>Days: </b> ")
							.append(crs.getString("crmdays_daycount")).append(crs.getString("crmdays_desc")).append("<br>").append("<b>Feedback Type: </b>")
							.append(crs.getString("crmfeedbacktype_name"))
							.append("<br>").append("<b>Description: </b> ").append(crs.getString("crm_desc")).append("<br>").append("<b>CRM Executive: </b>").append(crs.getString("crmemp_name"))
							.append("</div>\n")
							.append("<div class=\"col-md-3 col-xs-3\" onclick=\"callURL('app-crm-update.jsp?update=yes&enquiry_id="
									+ crs.getInt("crm_enquiry_id") + ""
									+ "&crm_id="
									+ crs.getInt("crm_id")
									+ "')\"><img src=\"ifx/edit-text.png\" style=\"float:right; position:relative;\" right=\"20px\" width=\"30px\"></div>\n");
					// .append("</div>\n");
				}
				Str.append("</div>\n");
				crs.close();
			} else {
				// msg =
				// "<br><br><font color=red><b>No CRM Follow-up found!</b></font>";
			}

		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}
}
