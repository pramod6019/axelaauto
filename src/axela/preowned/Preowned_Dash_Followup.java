package axela.preowned;
//sangita

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
//import cloudify.connect.Connect_Pre Owned;

public class Preowned_Dash_Followup extends Connect {

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
				submitB = PadQuotes(request.getParameter("submit_button"));
				delete = PadQuotes(request.getParameter("Delete"));
				preownedfollowup_id = CNumeric(PadQuotes(request.getParameter("preownedfollowup_id")));
				getOpportunityDetails(response);
				followupHTML = ListFollowup();
				customerdetail = ListCustomerDetails();

				if ("yes".equals(delete) && !preownedfollowup_id.equals("0") && emp_id.equals("1")) {
					DeleteFields();
					response.sendRedirect(response.encodeRedirectURL("preowned-dash-followup.jsp?preowned_id=" + preowned_id + ""));
				}
				if ("Submit".equals(submitB)) {
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
						// if (preownedfollowup_preownedfollowuptype_id.equals("3")) {
						// response.sendRedirect(response.encodeRedirectURL("testdrive-update.jsp?add=yes&preowned_id=" + preowned_id + "&msg=Follow-up added successfully!"));
						// } else {
						response.sendRedirect(response.encodeRedirectURL("preowned-dash-followup.jsp?preowned_id=" + preowned_id + "&msg=Follow-up added successfully!"));
						// }
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

	protected void getOpportunityDetails(HttpServletResponse response) {
		try {
			StrSql = " SELECT preowned_emp_id, preowned_preownedstatus_id, preowned_title, "
					+ " coalesce((SELECT preownedfollowup_followup_time "
					+ " FROM " + compdb(comp_id) + "axela_preowned_followup "
					+ " WHERE preownedfollowup_desc='' and preownedfollowup_preowned_id=" + preowned_id + " ORDER BY preownedfollowup_id desc limit 1),'') as preownedfollowup_followup_time"
					+ " FROM " + compdb(comp_id) + "axela_preowned"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id = preowned_branch_id  "
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp on emp_id = preowned_emp_id "
					+ " WHERE preowned_id = " + preowned_id + BranchAccess + ExeAccess + ""
					+ " group by preowned_id "
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
		StrSql = " SELECT customer_id, customer_name, contact_id,"
				+ " CONCAT(title_desc,' ', contact_fname, ' ', contact_lname) AS contacts,"
				+ " contact_mobile1, contact_mobile2 "
				+ " FROM " + compdb(comp_id) + "axela_preowned_followup "
				+ " INNER JOIN " + compdb(comp_id) + "axela_preowned on preowned_id = preownedfollowup_preowned_id "
				+ " INNER JOIN " + compdb(comp_id) + "axela_customer on customer_id = preowned_customer_id "
				+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact on contact_id = preowned_contact_id "
				+ " INNER JOIN " + compdb(comp_id) + "axela_title on title_id = contact_title_id"
				+ " WHERE preownedfollowup_preowned_id = " + preowned_id
				+ " group by customer_id";
		// SOP("StrSql: " + StrSql);
		CachedRowSet crs = processQuery(StrSql, 0);
		try {

			Str.append("<div class=\"portlet box\">");
			Str.append("<div class=\"portlet-title\" style=\"text-align: center\">");
			Str.append("<div class=\"caption\" style=\"float: none\">Follow-up</div>");
			Str.append("</div>");
			Str.append("<div class=\"portlet-body portlet-empty\">");
			Str.append("<div class=\"tab-pane\" id=\"\">");
			while (crs.next()) {
				Str.append("<div class=\"container-fluid \">");
				Str.append("<div class=\"form-body col-md-6 col-sm-6\">");
				Str.append("<div class=\"form-group\">");
				Str.append("<label class=\"text-right col-md-6 control-label\" style=\"top: -1px;\">Customer: &nbsp;</label>");
				Str.append("<a href=\"../customer/customer-list.jsp?customer_id=" + crs.getString("customer_id") + "\">" + crs.getString("customer_name") + "</a>");
				Str.append("</div>");
				Str.append("</div>");

				Str.append("<div class=\"form-body col-md-6 col-sm-6\">");
				Str.append("<div class=\"form-group\">");
				Str.append("<label class=\"text-right col-md-2 control-label\" style=\"top: -1px;\">Contact: &nbsp;</label>");
				Str.append("<a href=\"../customer/customer-contact-list.jsp?contact_id=" + crs.getString("contact_id") + "\">" + crs.getString("contacts") + "</a>");
				Str.append("</div>");
				Str.append("</div>");
				Str.append("</div>");

				//
				Str.append("<div class=\"container-fluid \">");
				Str.append("<div class=\"form-body col-md-6 col-sm-6\">");
				Str.append("<div class=\"form-group\">");
				Str.append("<label class=\" text-right col-md-6 control-label\" style=\"top: -1px;\">Mobile1: &nbsp;</label>");
				Str.append(crs.getString("contact_mobile1")).append(ClickToCall(crs.getString("contact_mobile1"), comp_id));
				Str.append("</div>");
				Str.append("</div>");

				Str.append("<div class=\"form-body col-md-6 col-sm-6\">");
				Str.append("<div class=\"form-group\">");
				Str.append("<label class=\" text-right col-md-2 control-label\" style=\"top: -1px;\">Mobile2: &nbsp;</label>");
				Str.append(crs.getString("contact_mobile2"));
				if (!crs.getString("contact_mobile2").equals("")) {
					Str.append(ClickToCall(crs.getString("contact_mobile2"), comp_id));
				}
				Str.append("</div>");
				Str.append("</div>");
				Str.append("</div>");
			}
			Str.append("</div>");
			Str.append("</div>");
			Str.append("</div>");

			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

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
				+ " WHERE 1 = 1 and preownedfollowup_preowned_id = " + preowned_id;
		// SOP("StrSql111--" + StrSqlBreaker(StrSql));
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
				Str.append("<th data-hide=\"phone, tablet\">Entry By</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");

				while (crs.next()) {
					count++;

					Str.append("<tr>\n");
					Str.append("<td valign=top align=center >").append(count).append("</td>");
					Str.append("<td valign=top align=left >").append(strToLongDate(crs.getString("preownedfollowup_followup_time")));

					if (emp_id.equals("1") && !crs.getString("preownedfollowup_id").equals("0")) {
						Str.append("<br><a href=\"enquiry-dash.jsp?Delete=yes&preowned_id=").append(crs.getString("preownedfollowup_preowned_id")).append("&preownedfollowup_id=")
								.append(crs.getString("preownedfollowup_id")).append(" \">Delete Follow-up</a>");
					}

					Str.append("</td>");
					Str.append("<td valign=top align=left >").append(crs.getString("preownedfollowuptype_name")).append("</td>");
					Str.append("<td valign=top align=left >").append(crs.getString("preownedfollowup_desc")).append("</td>");
					Str.append("<td valign=top align=left >").append(crs.getString("preownedfeedbacktype_name")).append("</td>");

					Str.append("<td valign=top align=left >");
					if (!crs.getString("preownedfollowup_entry_id").equals("0")) {
						Str.append("<a href=../portal/executive-summary.jsp?emp_id=").append(crs.getInt("preownedfollowup_entry_id")).append(">").append(crs.getString("entry_by")).append("</a>");
						Str.append("<br>").append(strToLongDate(crs.getString("preownedfollowup_entry_time"))).append("");
					}
					Str.append("&nbsp;</td>");

					Str.append("</tr>");

					if (crs.getString("preownedfollowup_desc").equals("")) {
						status = "Update";
					}
				}
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
			} else {
				status = "Add";
				msg = "<br><br><font color=red><b>No follow-up found!</b></font>";
			}
			if (status.equals("") && preowned_preownedstatus_id.equals("1")) {
				status = "Add";
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	protected void GetValues(HttpServletRequest request) {
		preownedfollowup_desc = PadQuotes(request.getParameter("txt_preownedfollowup_desc"));
		preownedfollowup_preownedfollowuptype_id = PadQuotes(request.getParameter("dr_followuptype"));
		preownedfollowup_preownedfeedbacktype_id = PadQuotes(request.getParameter("dr_feedbacktype"));
		preownedfollowup_followup_time = PadQuotes(request.getParameter("txt_followup_time"));
		// SOP("followup_time===" + followup_time);
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
						+ " WHERE preownedfollowup_desc='' and preownedfollowup_preowned_id = " + preowned_id;
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
				StrSql = " INSERT INTO " + compdb(comp_id) + "axela_preowned_followup "
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
						+ " VALUES "
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

	public String PopulateFeedbacktype() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value = 0> Select </option>");
		try {
			StrSql = "SELECT preownedfeedbacktype_id, preownedfeedbacktype_name "
					+ " FROM  " + compdb(comp_id) + "axela_preowned_followup_feedback_type "
					+ " WHERE 1=1 "
					+ " ORDER BY preownedfeedbacktype_name";
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
