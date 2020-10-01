package axela.axelaauto_app;
///*saiman 11th FEb 2013*/
// modified by divya,shilpashree

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class App_Preowned_Dash_Followup extends Connect {

	public String msg = "";
	public String emp_id = "0";
	public String branch_id = "0";
	public String preowned_id = "0";
	public String preowned_title = "";
	public String preowned_preownedtype_id = "";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String StrSql = "";
	public String followupHTML = "";
	// public String accountdetail = "";
	public String followup_feedbacktype_id = "";
	public String followup_time = "";
	public String preownedfollowup_entry_id = "0";
	public String preownedfollowup_entry_time = "";
	public String followup_desc = "";
	public String followupdesc_name = "";
	public String submitB = "";
	public String preowned_status_id = "0";
	public String followup_emp_id = "0";
	public String followup_followupstatus_id = "0";
	public String followup_followuptype_id = "0";
	public String delete = "";
	public String followup_id = "0";
	public String branch_brand_id = "0";
	public String preowned_option_id = "0";
	public String current_followup_time = "";
	public String preowned_preownedstatus_id = "0";
	public String preownedfollowup_emp_id = "0";
	public String current_preownedfollowup_time = "";
	public String currentfollowuptime = "", followup_date = "", temp_time = "";
	// public String customer_ind_id = "0";
	public String status = "";
	// public int recperpage = 0;
	public String day = "";
	public String month = "";
	public String year = "";
	public String comp_id = "0";
	public String emp_uuid = "", access = "";
	public String followupdesc_id = "0", emp_all_exe = "";
	public String preownedfollowup_desc = "";
	public String preownedfollowup_preownedfollowuptype_id = "0";
	public String preownedfollowup_preownedfeedbacktype_id = "0", preownedfollowup_id = "0";
	public String preownedfollowup_followup_time = "";
	public String preownedfollowup_followup_date = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession(true);
			// comp_id = CNumeric(PadQuotes(request.getParameter("comp_id")));
			// emp_uuid = PadQuotes(request.getParameter("emp_uuid"));
			// CheckAppSession(emp_uuid, comp_id, request);
			emp_id = CNumeric(session.getAttribute("emp_id") + "");
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!emp_id.equals("0")) {
				// recperpage =
				// Integer.parseInt(session.getAttribute("emp_recperpage") +
				// "");
				BranchAccess = GetSession("BranchAccess", request);
				BranchAccess = BranchAccess.replace("branch_id", "preowned_branch_id");
				ExeAccess = GetSession("ExeAccess", request);
				ExeAccess = ExeAccess.replace("emp_id", "preowned_emp_id");
				access = ReturnPerm(comp_id, "emp_preowned_access", request);
				if (access.equals("0")) {
					response.sendRedirect("callurlapp-error.jsp?msg=Access denied. Please contact system administrator!");
				}
				// emp_all_exe = GetSession("emp_all_exe", request);
				// if (emp_all_exe.equals("1"))
				// {
				// ExeAccess = "";
				// }
				msg = PadQuotes(request.getParameter("msg"));
				preowned_id = CNumeric(PadQuotes(request.getParameter("preowned_id")));
				submitB = PadQuotes(request.getParameter("submit_button"));
				delete = PadQuotes(request.getParameter("Delete"));
				followup_id = CNumeric(PadQuotes(request.getParameter("followup_id")));
				preownedfollowup_id = CNumeric(PadQuotes(request.getParameter("preownedfollowup_id")));
				getOpportunityDetails(response);
				followupHTML = ListFollowup();
				// accountdetail = ListAccountDetails();
				//
				// if ("yes".equals(delete) && !followup_id.equals("0") &&
				// emp_id.equals("1")) {
				// DeleteFields();
				// response.sendRedirect(response.encodeRedirectURL("callurlpreowned-dash-followup.jsp?preowned_id="
				// + preowned_id + ""));
				// }
				if ("yes".equals(submitB)) {
					GetValues(request);
					CheckForm();
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						preownedfollowup_entry_id = emp_id;
						preownedfollowup_entry_time = ToLongDate(kknow());
						if (status.equals("Add")) {
							AddFields();
						} else {
							UpdateFields();
						}
						// SetOpprPriority(preowned_id);
						// if (followup_followuptype_id.equals("3")) {
						// response.sendRedirect(response.encodeRedirectURL("apptestdrive-update.jsp?add=yes&preowned_id="
						// + preowned_id + "&followup_time=" +
						// ConvertLongDateToStr(followup_time)
						// + "&msg=Follow-up added successfully!"));
						// } else {
						response.sendRedirect(response.encodeRedirectURL("callurlapp-preowned-dash-followup.jsp?preowned_id=" + preowned_id
								+ "&msg=Follow-up added successfully!"));
					}
				}
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
			StrSql = " SELECT preowned_emp_id,  preowned_preownedstatus_id, preowned_title, "
					+ " coalesce((SELECT preownedfollowup_followup_time "
					+ " FROM " + compdb(comp_id) + "axela_preowned_followup "
					+ " WHERE preownedfollowup_desc='' AND preownedfollowup_preowned_id=" + preowned_id + " ORDER BY preownedfollowup_id desc limit 1),'') as preownedfollowup_followup_time"
					+ " FROM " + compdb(comp_id) + "axela_preowned"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id = preowned_branch_id  "
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp on emp_id = preowned_emp_id "
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
				response.sendRedirect("callurlapp-error.jsp?msg=Access denied. Please contact system administrator!");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-APP===" + this.getClass().getName());
			SOPError("Axelaauto-APP=== " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	// public String ListAccountDetails() {
	// String bgcolor = "";
	// StringBuilder Str = new StringBuilder();
	// StrSql = " SELECT customer_id, customer_name, customer_dnd, contact_id,"
	// +
	// " CONCAT(title_desc,' ', contact_fname, ' ', contact_lname) AS contacts, contact_mobile1, item_name"
	// + " FROM " +compdb(comp_id) + "axela_sales_preowned_followup "
	// + " INNER JOIN " + compdb(comp_id) +
	// "axela_sales_preowned ON preowned_id = followup_preowned_id"
	// + " INNER JOIN " + compdb(comp_id) +
	// "axela_inventory_item ON item_id = preowned_item_id"
	// + " INNER JOIN " +compdb(comp_id) +
	// "axela_customer ON customer_id = preowned_customer_id"
	// + " INNER JOIN " +compdb(comp_id) +
	// "axela_customer_contact ON contact_id = preowned_contact_id"
	// + " INNER JOIN " +compdb(comp_id) +
	// "axela_title ON title_id = contact_title_id"
	// + " WHERE followup_preowned_id = " + preowned_id
	// + " GROUP BY customer_id";
	// CachedRowSet crs =processQuery(StrSql, 0);
	// try {
	// Str.append("<table width=100% border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
	// while (crs.next()) {
	// if (crs.getString("account_dnd").equals("1")) {
	// bgcolor = "#ffcccc";
	// } else {
	// bgcolor = "";
	// }
	// Str.append("<tr align=center bgcolor=" + bgcolor + ">\n");
	// Str.append(
	// "<td align=center>Account: <a href=\"../account/account-list.jsp?account_id=")
	// .append(crs.getString("account_id")).append(" \">")
	// .append(crs.getString("account_name")).append(" (")
	// .append(crs.getString("account_id")).append(")</td>\n");
	// Str.append(
	// "<td align=center>Contact: <a href=\"../account/account-contact-list.jsp?contact_id=")
	// .append(crs.getString("contact_id")).append(" \">")
	// .append(crs.getString("contacts")).append("</a></td>\n");
	// Str.append("<td align=center>Mobile: ")
	// .append(crs.getString("contact_mobile1"))
	// .append("</td>\n");
	// Str.append("<td align=center>Variant: ")
	// .append(crs.getString("item_name")).append("</td>\n");
	// Str.append("</tr>");
	// }
	// Str.append("</table>\n");
	// crs.close();
	// } catch (Exception ex) {
	// SOPError("Axelaauto-APP===" + this.getClass().getName());
	// SOPError("Axelaauto-APP===" + new
	// Exception().getStackTrace()[0].getMethodName() + ": " + ex);
	// return "";
	// }
	// return Str.toString();
	// }

	public String ListFollowup() {
		StringBuilder Str = new StringBuilder();
		StrSql = " SELECT preownedfollowup_id, preownedfollowup_preowned_id, preownedfollowup_followup_time, preownedfollowup_desc, "
				+ " preownedfollowup_entry_time, preownedfollowup_entry_id,"
				+ " coalesce(preownedfeedbacktype_name,'') as preownedfeedbacktype_name,"
				+ " COALESCE(preownedfollowuptype_name,'') AS preownedfollowuptype_name, CONCAT(emp_name,' (',emp_ref_no,')') as entry_by"
				+ " FROM " + compdb(comp_id) + "axela_preowned_followup "
				+ " INNER JOIN " + compdb(comp_id) + "axela_preowned on preowned_id = preownedfollowup_preowned_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_emp on emp_id = preownedfollowup_entry_id "
				+ " LEFT JOIN " + compdb(comp_id) + "axela_preowned_followup_type on preownedfollowuptype_id = preownedfollowup_preownedfollowuptype_id "
				+ " LEFT JOIN " + compdb(comp_id) + "axela_preowned_followup_feedback_type on preownedfeedbacktype_id = preownedfollowup_preownedfeedbacktype_id"
				+ " WHERE 1 = 1 AND preownedfollowup_preowned_id = " + preowned_id;
		// SOP("StrSql111--" + StrSqlBreaker(StrSql));
		CachedRowSet crs = processQuery(StrSql, 0);
		String executive = "";
		String feedbackby = "";
		try {
			int count = 0;
			if (crs.isBeforeFirst()) {
				// Str.append("<table class=\"table table-responsive\">");
				while (crs.next()) {
					// executive = ExecuteQuery("SELECT emp_name FROM " + compdb(comp_id) + "axela_emp WHERE emp_id='" + crs.getString("preownedfollowup_emp_id") + "'");
					// feedbackby = ExecuteQuery("SELECT emp_name FROM " + compdb(comp_id) + "axela_emp WHERE emp_id='" + crs.getString("followup_modified_id") + "'");
					count++;
					Str.append("<div>").append("<div class=\"col-md-12 col-xs-12\" style=\"border: 1px solid #8E44AD; \">").append("<br>").append("<b>").append("Time:")
							.append("</b>&nbsp;").append("<span style=\"color: #000\">").append(strToLongDate(crs.getString("preownedfollowup_followup_time"))).append("</span><br><br>");
					Str.append("<b>").append("Follow-up Type:").append("</b>&nbsp;").append("<span style=\"color: #000\">").append(crs.getString("preownedfollowuptype_name"))
							.append("</span><br><br>");
					Str.append("<b>").append("Follow-up Description:").append("</b>&nbsp;").append("<span style=\"color: #000\">").append(crs.getString("preownedfollowup_desc"))
							.append("</span><br><br>");
					Str.append("<b>").append("Feedback Type:").append("</b>&nbsp;").append("<span style=\"color: #000\">").append(crs.getString("preownedfeedbacktype_name"))
							.append("</span><br><br>");

					// .append("<b>").append("Feedback Type:").append("</b>&nbsp;")
					// .append("<span style=\"color: #000\">")
					// .append(crs.getString("feedbacktype_name"))
					// .append("</span><br>")
					// Str.append("<b>").append("Follow-up Status:").append("</b>&nbsp;")
					// .append("<span style=\"color: #000\">")
					// .append(crs.getString("followupstatus_name"))
					// .append("</span><br>")

					// Str.append("<b>").append("Sales Consultant:").append("</b>&nbsp;").append("<span style=\"color: #000\">").append(executive).append("</span><br><br>").append("<b>");
					// .append("Feedback By:")
					// .append("</b>&nbsp;").append("<span style=\"color: #000\">").append(feedbackby).append("</span><br><br>");
					Str.append("</div>").append("</div>");

					if (crs.getString("preownedfollowup_desc").equals("")) {
						status = "Update";
					}
				}
				// Str.append("</table>");
				// SOP("Str------" + Str);
			} else {
				status = "Add";
				msg = "<br><br><font color=red><b>No follow-up found!</b></font>";
			}
			if (status.equals("") && preowned_status_id.equals("1")) {
				status = "Add";
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App=== " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		// SOP("str===" + Str.toString());
		return Str.toString();
	}

	protected void GetValues(HttpServletRequest request) {
		preownedfollowup_desc = PadQuotes(request.getParameter("txt_preownedfollowup_desc"));
		preownedfollowup_preownedfollowuptype_id = PadQuotes(request.getParameter("dr_followuptype"));
		preownedfollowup_preownedfeedbacktype_id = PadQuotes(request.getParameter("dr_feedbacktype"));
		temp_time = PadQuotes(request.getParameter("txt_followup_time"));
		preownedfollowup_followup_date = PadQuotes(request.getParameter("txt_followup_date"));
		if (!preownedfollowup_followup_date.equals("") && !temp_time.equals("")) {
			// followup_date = followup_date.replace('-', '/');
			day = preownedfollowup_followup_date.substring(0, 2);
			month = preownedfollowup_followup_date.substring(3, 5);
			year = preownedfollowup_followup_date.substring(6, 10);
			preownedfollowup_followup_date = day + "/" + month + "/" + year;
			preownedfollowup_followup_time = preownedfollowup_followup_date + " " + temp_time;
		}
	}

	protected void CheckForm() {
		msg = "";
		if (preownedfollowup_emp_id.equals("0")) {
			msg = msg + "<br>No Pre-Owned Consultant allocated!";
		}
		if (status.equals("Update") && preownedfollowup_preownedfeedbacktype_id.equals("0")) {
			msg = msg + "<br>Select Feedback Type!";
		}
		if (status.equals("Update") && preownedfollowup_desc.equals("")) {
			msg = msg + "<br>Enter Description!";
		}
		if (preowned_preownedstatus_id.equals("1")) {
			SOP("preownedfollowup_followup_time==============" + preownedfollowup_followup_time);
			if (preownedfollowup_followup_time.equals("")) {
				msg = msg + "<br>Select Follow-up Time!";
			} else {
				if (!isValidDateFormatLong(preownedfollowup_followup_time)) {
					msg = msg + "<br>Enter Valid Follow-up Time!";
				} else {
					if (Long.parseLong(ConvertLongDateToStr(preownedfollowup_followup_time)) <= Long.parseLong(ToLongDate(kknow()))) {
						msg = msg + "<br>Follow-up time must be greater than " + strToLongDate(ToLongDate(kknow())) + "!";
					}
					if ((Integer.parseInt(ConvertLongDateToStr(preownedfollowup_followup_time).substring(8, 10)) > 21)
							|| (Integer.parseInt(ConvertLongDateToStr(preownedfollowup_followup_time).substring(8, 10)) < 8)) {
						msg = msg + "<br>Follow-up time should be greater than 8am and less than 9pm!";
					}
				}
			}
			if (preownedfollowup_preownedfollowuptype_id.equals("0")) {
				msg = msg + "<br>Select Follow-up Type!";
			}
		}
	}

	protected void UpdateFields() {
		if (msg.equals("")) {
			try {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_preowned_followup"
						+ " SET "
						+ " preownedfollowup_preownedfeedbacktype_id = " + preownedfollowup_preownedfeedbacktype_id + ","
						+ " preownedfollowup_desc = '" + preownedfollowup_desc + "'"
						+ " WHERE preownedfollowup_desc='' AND preownedfollowup_preowned_id = " + preowned_id;
				// SOP("StrSql in UpdateFields: " + StrSql);
				updateQuery(StrSql);
				if (preowned_preownedstatus_id.equals("1")) {
					AddFields();
				}
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void DeleteFields() {
		if (msg.equals("")) {
			try {
				StrSql = "Delete FROM " + compdb(comp_id) + "axela_preowned_followup WHERE preownedfollowup_id =" + preownedfollowup_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	public void AddFields() {
		CheckForm();
		if (msg.equals("")) {
			try {
				StrSql = " insert into " + compdb(comp_id) + "axela_preowned_followup "
						+ " ( "
						+ " preownedfollowup_preowned_id, "
						+ " preownedfollowup_emp_id, "
						+ " preownedfollowup_preownedfollowuptype_id, "
						+ " preownedfollowup_preownedfeedbacktype_id, "
						+ " preownedfollowup_followup_time, "
						+ " preownedfollowup_desc, "
						+ " preownedfollowup_entry_id, "
						+ " preownedfollowup_entry_time, "
						+ " preownedfollowup_trigger) "
						+ " values "
						+ "("
						+ "'" + preowned_id + "',"
						+ "" + preownedfollowup_emp_id + ", "
						+ "" + preownedfollowup_preownedfollowuptype_id + ", "
						+ " 0, "
						+ " '" + ConvertLongDateToStr(preownedfollowup_followup_time) + "', "
						+ " '', "
						+ "" + preownedfollowup_entry_id + ", "
						+ " '" + preownedfollowup_entry_time + "', "
						+ "0 "
						+ ")";
				// SOP("StrSql=="+StrSql);
				updateQuery(StrSql);

			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	public String PopulateFollowuptype() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value = 0> Select </option>");
		try {
			StrSql = "SELECT preownedfollowuptype_id, preownedfollowuptype_name"
					+ " FROM  " + compdb(comp_id) + "axela_preowned_followup_type"
					+ " WHERE 1=1"
					+ " ORDER BY preownedfollowuptype_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {

				Str.append("<option value=").append(crs.getString("preownedfollowuptype_id")).append("");
				Str.append(StrSelectdrop(crs.getString("preownedfollowuptype_id"), preownedfollowup_preownedfollowuptype_id));
				Str.append(">").append(crs.getString("preownedfollowuptype_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateFollowupDesc(String comp_id) {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value = 0> Select </option>");
		try {
			StrSql = "SELECT followupdesc_id, followupdesc_name " + "FROM  " + compdb(comp_id) + "axela_sales_preowned_followup_desc " + "WHERE 1=1 " + "ORDER BY followupdesc_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("followupdesc_id")).append("");
				Str.append(StrSelectdrop(crs.getString("followupdesc_id"), followupdesc_id));
				Str.append(">").append(crs.getString("followupdesc_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-APP=== " + this.getClass().getName());
			SOPError("Axelaauto-APP=== " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	// public String PopulateFollowupStatus() {
	// StringBuilder Str = new StringBuilder();
	// Str.append("<option value = 0> Select </option>");
	// try {
	// StrSql = "SELECT followupstatus_id, followupstatus_name"
	// + " FROM axela_sales_preowned_followup_status" + " WHERE 1 = 1"
	// + " ORDER BY followupstatus_name";
	// CachedRowSet crs =processQuery(StrSql, 0);
	// while (crs.next()) {
	// Str.append("<option value=").append(crs.getString("followupstatus_id"));
	// Str.append(StrSelectdrop(crs.getString("followupstatus_id"),
	// followup_followupstatus_id));
	// Str.append(">").append(crs.getString("followupstatus_name")).append("</option>\n");
	// }
	// crs.close();
	// } catch (Exception ex) {
	// SOPError("Silverarrows-App===" + this.getClass().getName());
	// SOPError("Silverarrows-App===" + new
	// Exception().getStackTrace()[0].getMethodName() + ": " + ex);
	// return "";
	// }
	// return Str.toString();
	// }

	public String PopulateFeedbacktype() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value = 0> Select </option>");
		try {
			StrSql = "SELECT preownedfeedbacktype_id, preownedfeedbacktype_name "
					+ " FROM  " + compdb(comp_id) + "axela_preowned_followup_feedback_type "
					+ " WHERE 1=1 "
					+ " ORDER BY preownedfeedbacktype_name";
			// SOP("StrSql===========" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {

				Str.append("<option value=").append(crs.getString("preownedfeedbacktype_id"));
				Str.append(StrSelectdrop(crs.getString("preownedfeedbacktype_id"), preownedfollowup_preownedfeedbacktype_id));
				Str.append(">").append(crs.getString("preownedfeedbacktype_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}
}
