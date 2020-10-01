package axela.axelaauto_app;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class App_Preowned_Dash_CRMFollowup extends Connect {

	public String msg = "";
	public String emp_id = "0";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String StrSql = "";
	public String crmfollowupHTML = "";
	// public String accountdetail = "";
	// public String crm_id = "0";
	// public String delete = "";
	// public String psf_id = "";
	// // public int recperpage = 0;
	public String comp_id = "0";
	public String emp_uuid = "";
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
			HttpSession session = request.getSession(true);
			emp_id = CNumeric(session.getAttribute("emp_id") + "");
			comp_id = CNumeric(GetSession("comp_id", request));

			if (!emp_id.equals("0")) {
				BranchAccess = GetSession("BranchAccess", request);
				BranchAccess = BranchAccess.replace("branch_id", "preowned_branch_id");
				ExeAccess = GetSession("ExeAccess", request);
				ExeAccess = ExeAccess.replace("emp_id", "preowned_emp_id");
				msg = PadQuotes(request.getParameter("msg"));
				preowned_id = CNumeric(PadQuotes(request.getParameter("preowned_id")));
				precrmfollowup_id = CNumeric(PadQuotes(request.getParameter("precrmfollowup_id")));
				getPreownedDetails(response);
				crmfollowupHTML = ListCRMFollowup(response);
			}
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void getPreownedDetails(HttpServletResponse response) {
		try {
			StrSql = " SELECT preowned_emp_id, preowned_title, preowned_branch_id, preowned_preownedstatus_id "
					+ " FROM " + compdb(comp_id) + "axela_preowned"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id=preowned_branch_id  "
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp on emp_id = preowned_emp_id "
					+ " WHERE preowned_id = " + preowned_id
					+ BranchAccess
					+ ExeAccess + ""
					+ " GROUP BY preowned_id "
					+ " ORDER BY preowned_id desc ";
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

				response.sendRedirect(response.encodeRedirectURL("callurl" + "app-error.jsp?msg=Invalid Pre-Owned!"));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String ListCRMFollowup(HttpServletResponse response) {
		StringBuilder Str = new StringBuilder();
		StrSql = " SELECT precrmfollowup_id, precrmfollowup_preowned_id, precrmfollowup_followup_time, precrmfollowup_desc, "
				+ " precrmfollowup_entry_time, precrmfollowup_entry_id, precrmfollowup_modified_id, "
				+ " COALESCE(precrmfollowup_modified_time,'') AS precrmfollowup_modified_time, "
				+ " COALESCE(precrmfeedbacktype_name,'') AS precrmfeedbacktype_name,"
				+ " COALESCE (CASE WHEN precrmfollowup_satisfied = 0 THEN '' END,"
				+ " CASE WHEN precrmfollowup_satisfied = 1 THEN 'Satisfied' END,"
				+ " CASE WHEN precrmfollowup_satisfied = 2 THEN 'Dis-Satisfied' END,'') AS precrmfollowup_satisfied,"
				+ " COALESCE(CONCAT(e.emp_name,' (',e.emp_ref_no,')'),'') AS entry_by, "
				+ " COALESCE(CONCAT(m.emp_name,' (',m.emp_ref_no,')'),'') AS modified_by, "
				+ " COALESCE(CONCAT(c.emp_name,' (',c.emp_ref_no,')'),'') AS emp_name,"
				+ " precrmfollowupdays_daycount, precrmfollowupdays_desc, precrmfollowup_crm_emp_id"
				+ " FROM " + compdb(comp_id) + "axela_preowned_crmfollowup  "
				+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_crmfollowupdays ON precrmfollowup_precrmfollowupdays_id = precrmfollowupdays_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_preowned on preowned_id = precrmfollowup_preowned_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_emp AS c ON c.emp_id = precrmfollowup_crm_emp_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_emp AS e ON e.emp_id = precrmfollowup_entry_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_emp AS m ON m.emp_id = precrmfollowup_modified_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_preowned_crmfeedbacktype ON precrmfeedbacktype_id = precrmfollowup_precrmfeedbacktype_id"
				+ " WHERE 1 = 1 AND precrmfollowup_preowned_id = " + preowned_id + ""
				+ " ORDER BY precrmfollowup_id ";
		// SOP("StrSql======ListCRMFollowup======" + StrSql);
		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			int count = 0;
			if (crs.isBeforeFirst()) {
				Str.append("<div class=\"\">\n");
				while (crs.next()) {
					Str.append("<div class=\"col-md-9 col-xs-9\" style=\"margin-bottom:30px;\"><b>").append("Time: </b> ").append(strToLongDate(crs.getString("precrmfollowup_followup_time")))
							.append("<br>")
							.append("<b>Days: </b> ")
							.append(crs.getString("precrmfollowupdays_daycount")).append(crs.getString("precrmfollowupdays_desc")).append("<br>").append("<b>Feedback Type: </b>")
							.append(crs.getString("precrmfeedbacktype_name")).append("&nbsp&nbsp&nbsp")
							.append(crs.getString("precrmfollowup_satisfied"))
							.append("<br>").append("<b>Description: </b> ").append(crs.getString("precrmfollowup_desc")).append("<br>").append("<b>Pre-Owned CRM Executive: </b>")
							.append(crs.getString("emp_name"))
							.append("</div>\n")
							.append("<div class=\"col-md-3 col-xs-3\" onclick=\"callURL('app-preowned-crmfollowup-update.jsp?update=yes&preowned_id="
									+ crs.getInt("precrmfollowup_preowned_id") + ""
									+ "&precrmfollowup_id="
									+ crs.getInt("precrmfollowup_id")
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
