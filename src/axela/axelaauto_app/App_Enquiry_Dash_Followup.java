package axela.axelaauto_app;
///*saiman 11th FEb 2013*/
// modified by divya,shilpashree

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import axela.sales.Enquiry_Dash_Methods;
import axela.sales.Enquiry_Quickadd;
import cloudify.connect.Connect;

public class App_Enquiry_Dash_Followup extends Connect {

	public String msg = "";
	public String emp_id = "0";
	public String branch_id = "0";
	public String enquiry_id = "0";
	public String enquiry_title = "";
	public String enquiry_enquirytype_id = "";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String StrSql = "";
	public String followupHTML = "";
	// public String accountdetail = "";
	public String followup_feedbacktype_id = "", followupfeedbacktype_id = "0";
	public String followup_time = "";
	public String followup_entry_id = "0";
	public String followup_entry_time = "";
	public String followup_desc = "";
	public String followupdesc_name = "";
	public String submitB = "";
	public String enquiry_status_id = "0";
	public String followup_emp_id = "0";
	public String followup_followupstatus_id = "0";
	public String followup_followuptype_id = "0";
	public String delete = "";
	public String followup_id = "0";
	public String branch_brand_id = "0";
	public String enquiry_option_id = "0";
	public String current_followup_time = "";
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
	public String enquiry_jlr_enquirystatus = "";
	public String enq_jlr_enquirystatus = "";
	public String jlr_enquirystatus = "";
	public String jlr_enquirystatus_update = "";

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
				BranchAccess = BranchAccess.replace("branch_id", "enquiry_branch_id");
				ExeAccess = GetSession("ExeAccess", request);
				ExeAccess = ExeAccess.replace("emp_id", "enquiry_emp_id");
				access = ReturnPerm(comp_id, "emp_enquiry_access", request);
				if (access.equals("0")) {
					response.sendRedirect("callurlapp-error.jsp?msg=Access denied. Please contact system administrator!");
				}
				// emp_all_exe = GetSession("emp_all_exe", request);
				// if (emp_all_exe.equals("1"))
				// {
				// ExeAccess = "";
				// }
				msg = PadQuotes(request.getParameter("msg"));
				enquiry_id = CNumeric(PadQuotes(request.getParameter("enquiry_id")));
				submitB = PadQuotes(request.getParameter("submit_button"));
				delete = PadQuotes(request.getParameter("Delete"));
				followup_id = CNumeric(PadQuotes(request.getParameter("followup_id")));
				getOpportunityDetails(response);
				followupHTML = ListFollowup();
				// accountdetail = ListAccountDetails();
				//
				// if ("yes".equals(delete) && !followup_id.equals("0") &&
				// emp_id.equals("1")) {
				// DeleteFields();
				// response.sendRedirect(response.encodeRedirectURL("callurlenquiry-dash-followup.jsp?enquiry_id="
				// + enquiry_id + ""));
				// }
				if ("yes".equals(submitB)) {
					GetValues(request);
					CheckForm();
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						followup_entry_id = emp_id;
						followup_entry_time = ToLongDate(kknow());
						if (status.equals("Add")) {
							AddFields();
						} else {
							UpdateFields();
						}
						// SetOpprPriority(enquiry_id);
						// if (followup_followuptype_id.equals("3")) {
						// response.sendRedirect(response.encodeRedirectURL("apptestdrive-update.jsp?add=yes&enquiry_id="
						// + enquiry_id + "&followup_time=" +
						// ConvertLongDateToStr(followup_time)
						// + "&msg=Follow-up added successfully!"));
						// } else {
						response.sendRedirect(response.encodeRedirectURL("callurlapp-enquiry-dash-followup.jsp?enquiry_id=" + enquiry_id + "&msg=Follow-up added successfully!"));
					} else {
						response.sendRedirect(response.encodeRedirectURL("showtoast" + msg));
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
			StrSql = " SELECT enquiry_id, enquiry_emp_id, enquiry_status_id, enquiry_title, enquiry_option_id,"
					+ " branch_brand_id, enquiry_enquirytype_id,"
					+ " COALESCE((SELECT followup_followup_time "
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_followup "
					+ " WHERE followup_desc=''"
					+ " AND followup_enquiry_id=" + enquiry_id + ""
					+ " ORDER BY followup_id desc limit 1),'') AS followup_followup_time"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = enquiry_customer_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id  "
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = enquiry_emp_id "
					+ " WHERE enquiry_id = " + enquiry_id
					+ BranchAccess
					+ ExeAccess;
			StrSql += " GROUP BY enquiry_id "
					+ " ORDER BY enquiry_id DESC ";

			// StrSql = "SELECT " + compdb(comp_id) + "axela_sales_enquiry.*, "
			// +
			// " COALESCE(enquirytype_name, '') AS enquirytype_name, enquiry_age_id,"
			// +
			// " customer_name, contact_title_id, contact_fname, contact_lname, "
			// +
			// " contact_mobile1, contact_mobile2,  contact_phone1, contact_phone2, contact_email1, contact_email2, contact_address, "
			// +
			// " contact_city_id, contact_pin, "
			// +
			// " branch_name, branch_code, branch_brand_id, followup_followup_time, stage_name, coalesce(soe_name,'') as soe_name,"
			// + " enquiry_evaluation"
			// + " FROM " + compdb(comp_id) + "axela_sales_enquiry"
			// + " INNER JOIN " + compdb(comp_id) +
			// "axela_customer_contact ON contact_id = enquiry_contact_id "
			// + " INNER JOIN " + compdb(comp_id) +
			// "axela_customer ON customer_id = enquiry_customer_id "
			// + " INNER JOIN " + compdb(comp_id) +
			// "axela_emp ON emp_id = enquiry_emp_id "
			// + " INNER JOIN " + compdb(comp_id) +
			// "axela_branch ON branch_id = enquiry_branch_id "
			// + " INNER JOIN " + compdb(comp_id) +
			// "axela_sales_enquiry_stage ON stage_id = enquiry_stage_id "
			// + " LEFT JOIN " + compdb(comp_id) +
			// "axela_soe ON soe_id = enquiry_soe_id "
			// + " LEFT JOIN " + compdb(comp_id) +
			// "axela_sales_enquiry_type ON enquirytype_id = enquiry_enquirytype_id"
			// + " WHERE enquiry_id=" + enquiry_id + BranchAccess
			// + ExeAccess + " " + " GROUP BY enquiry_id ";
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					enquiry_title = crs.getString("enquiry_title");
					followup_emp_id = crs.getString("enquiry_emp_id");
					enquiry_status_id = crs.getString("enquiry_status_id");
					current_followup_time = crs.getString("followup_followup_time");
					// currentfollowuptime =
					// strToLongDate(crs.getString("followup_followup_time"));
					enquiry_enquirytype_id = crs.getString("enquiry_enquirytype_id");
					branch_brand_id = crs.getString("branch_brand_id");
					enquiry_option_id = crs.getString("enquiry_option_id");
					// customer_ind_id = crs.getString("customer_ind_id");
				}
			} else {
				response.sendRedirect("../portal/error.jsp?msg=Invalid Opportunity!");
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
	// + " FROM " +compdb(comp_id) + "axela_sales_enquiry_followup "
	// + " INNER JOIN " + compdb(comp_id) +
	// "axela_sales_enquiry ON enquiry_id = followup_enquiry_id"
	// + " INNER JOIN " + compdb(comp_id) +
	// "axela_inventory_item ON item_id = enquiry_item_id"
	// + " INNER JOIN " +compdb(comp_id) +
	// "axela_customer ON customer_id = enquiry_customer_id"
	// + " INNER JOIN " +compdb(comp_id) +
	// "axela_customer_contact ON contact_id = enquiry_contact_id"
	// + " INNER JOIN " +compdb(comp_id) +
	// "axela_title ON title_id = contact_title_id"
	// + " WHERE followup_enquiry_id = " + enquiry_id
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
		StrSql = " SELECT * FROM (" + " (SELECT followup_id, followup_enquiry_id, followup_followup_time, followup_desc, "
				+ " followup_entry_time, followup_entry_id, followup_emp_id,"
				+ " followuptype_name, followup_modified_id, followup_modified_time,";
		if (branch_brand_id.equals("55")) {
			StrSql += " COALESCE (followupstatus_name, '') AS followupstatus_name,";
		}
		StrSql += " COALESCE(feedbacktype_name, '') AS feedbacktype_name,"
				+ " CONCAT(exe.emp_name,' (',exe.emp_ref_no,')') AS followup_by,"
				+ " CONCAT(entry.emp_name,' (',entry.emp_ref_no,')') AS entry_by," + " COALESCE(CONCAT(modified.emp_name,' (',modified.emp_ref_no,')'), '') AS modified_by  "
				+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_followup "
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = followup_enquiry_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_emp exe ON exe.emp_id = followup_emp_id "
				+ " INNER JOIN " + compdb(comp_id) + "axela_emp entry ON entry.emp_id = followup_entry_id "
				+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry_followup_feedback_type ON feedbacktype_id = followup_feedbacktype_id";
		if (branch_brand_id.equals("55")) {
			StrSql += " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry_followup_status on followupstatus_id = followup_followupstatus_id ";
		}
		StrSql += " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_followup_type ON followuptype_id = followup_followuptype_id "
				+ " LEFT JOIN " + compdb(comp_id) + "axela_emp modified ON modified.emp_id = followup_modified_id "
				+ " WHERE 1 = 1 "
				+ " AND followup_enquiry_id = " + enquiry_id + " )"
				+ " UNION "
				+ " (SELECT 0, crm_enquiry_id, crm_followup_time, crm_desc,"
				+ " crm_entry_time, crm_entry_id, crm_emp_id,  "
				+ " crmfeedbacktype_name, crm_modified_id, crm_modified_time, '',";
		if (branch_brand_id.equals("55")) {
			StrSql += " '',"; // for status
		}
		StrSql += " CONCAT(exe.emp_name,' (',exe.emp_ref_no,')') AS followup_by, "
				+ " CONCAT(entry.emp_name,' (',entry.emp_ref_no,')') AS entry_by,  "
				+ " COALESCE(CONCAT(modified.emp_name,' (',modified.emp_ref_no,')'), '') AS modified_by  "
				+ " FROM " + compdb(comp_id) + "axela_sales_crm "
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = crm_enquiry_id "
				+ " INNER JOIN " + compdb(comp_id) + "axela_emp exe ON exe.emp_id = crm_emp_id "
				+ " INNER JOIN " + compdb(comp_id) + "axela_emp entry ON entry.emp_id = crm_entry_id "
				+ " LEFT JOIN axela_sales_crm_feedbacktype ON crmfeedbacktype_id = crm_crmfeedbacktype_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_emp modified ON modified.emp_id = crm_modified_id "
				+ " WHERE 1 = 1"
				+ " AND crm_enquiry_id = " + enquiry_id + " AND crm_desc !='')" + " ) AS t "
				+ " ORDER BY followup_followup_time ";
		CachedRowSet crs = processQuery(StrSql, 0);
		String executive = "";
		String feedbackby = "";
		try {
			int count = 0;
			if (crs.isBeforeFirst()) {
				// Str.append("<table class=\"table table-responsive\">");
				while (crs.next()) {
					executive = ExecuteQuery("SELECT emp_name FROM " + compdb(comp_id) + "axela_emp WHERE emp_id='" + crs.getString("followup_emp_id") + "'");
					feedbackby = ExecuteQuery("SELECT emp_name FROM " + compdb(comp_id) + "axela_emp WHERE emp_id='" + crs.getString("followup_modified_id") + "'");
					count++;
					Str.append("<div>").append("<div class=\"col-md-12 col-xs-12\" style=\"border: 1px solid #8E44AD; \">").append("<br>").append("<b>").append("Time:")
							.append("</b>&nbsp;").append("<span style=\"color: #000\">").append(strToLongDate(crs.getString("followup_followup_time"))).append("</span><br><br>");
					Str.append("<b>").append("Follow-up Type:").append("</b>&nbsp;").append("<span style=\"color: #000\">").append(crs.getString("followuptype_name")).append("</span><br><br>");
					Str.append("<b>").append("Follow-up Description:").append("</b>&nbsp;").append("<span style=\"color: #000\">").append(crs.getString("followup_desc")).append("</span><br><br>");
					Str.append("<b>").append("Feedback Type:").append("</b>&nbsp;").append("<span style=\"color: #000\">").append(crs.getString("feedbacktype_name")).append("</span><br><br>");
					if (branch_brand_id.equals("55")) {
						Str.append("<b>").append("Status:").append("</b>&nbsp;").append("<span style=\"color: #000\">").append(crs.getString("followupstatus_name")).append("</span><br><br>");
					}
					// .append("<b>").append("Feedback Type:").append("</b>&nbsp;")
					// .append("<span style=\"color: #000\">")
					// .append(crs.getString("feedbacktype_name"))
					// .append("</span><br>")
					// Str.append("<b>").append("Follow-up Status:").append("</b>&nbsp;")
					// .append("<span style=\"color: #000\">")
					// .append(crs.getString("followupstatus_name"))
					// .append("</span><br>")

					Str.append("<b>").append("Sales Consultant:").append("</b>&nbsp;").append("<span style=\"color: #000\">").append(executive).append("</span><br><br>").append("<b>")
							.append("Feedback By:")
							.append("</b>&nbsp;").append("<span style=\"color: #000\">").append(feedbackby).append("</span><br><br>");
					Str.append("</div>").append("</div>");

					if (crs.getString("followup_desc").equals("")) {
						status = "Update";
					}
				}
				// Str.append("</table>");
				// SOP("Str------" + Str);
			} else {
				status = "Add";
				msg = "<br><br><font color=red><b>No follow-up found!</b></font>";
			}
			// / condition removed in according to provide followup for closed enq also
			if (status.equals("")) {
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
		followup_followupstatus_id = CNumeric(PadQuotes(request.getParameter("dr_followupstatus")));
		enq_jlr_enquirystatus = PadQuotes(request.getParameter("drop_enquiry_jlr_status"));
		jlr_enquirystatus = PadQuotes(request.getParameter("jlr_enquirystatus"));
		followup_desc = PadQuotes(request.getParameter("txt_followup_desc"));
		followup_followuptype_id = CNumeric(PadQuotes(request.getParameter("dr_followuptype")));
		followup_feedbacktype_id = CNumeric(PadQuotes(request.getParameter("dr_followup_feedbacktype_id")));
		followupdesc_id = CNumeric(PadQuotes(request.getParameter("dr_actiontaken")));
		followup_date = PadQuotes(request.getParameter("txt_followup_date"));
		temp_time = PadQuotes(request.getParameter("txt_followup_time"));
		if (!followup_date.equals("") && !temp_time.equals("")) {
			// followup_date = followup_date.replace('-', '/');
			day = followup_date.substring(0, 2);
			month = followup_date.substring(3, 5);
			year = followup_date.substring(6, 10);
			followup_date = day + "/" + month + "/" + year;
			followup_time = followup_date + " " + temp_time;
		}
	}

	protected void CheckForm() {
		msg = "";
		SOP("branch Brand Id===================" + branch_brand_id);
		int followupcount = 0;

		// if (!current_followup_time.equals("")) {
		// if (Long.parseLong(current_followup_time.substring(0, 8)) >
		// Long.parseLong(ToLongDate(kknow()).substring(0, 8))) {
		// msg = msg + "<br>This follow-up can be updated only on " +
		// strToShortDate(current_followup_time) + "";
		// }
		// }
		if (followup_emp_id.equals("0")) {
			msg = msg + "<br>No Sales Consultant allocated!";
		}

		followupcount = Integer.parseInt(ExecuteQuery("SELECT COUNT(followup_id)"
				+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_followup"
				+ " WHERE followup_enquiry_id = " + enquiry_id));

		if (status.equals("Update") &&
				followup_followupstatus_id.equals("0") && branch_brand_id.equals("55")) {
			msg = msg + "<br>Select Follow-up Status!";
		}
		if (status.equals("Update") && followup_desc.equals("")) {
			msg = msg + "<br>Enter Description!";
		}
		if (enquiry_status_id.equals("1")) {
			if (followup_time.equals("")) {
				msg = msg + "<br>Select Follow-up Time!";
			} else {
				if (!isValidDateFormatLong(followup_time)) {
					msg = msg + "<br>Enter Valid Follow-up Time!";
				} else {
					if (Long.parseLong(ConvertLongDateToStr(followup_time)) <= Long.parseLong(ToLongDate(kknow()))) {
						msg = msg + "<br>Follow-up time must be greater than " + strToLongDate(ToLongDate(kknow())) + "!";
					}
					// if
					// ((Integer.parseInt(ConvertLongDateToStr(followup_time).substring(8,
					// 10)) > 21) ||
					// (Integer.parseInt(ConvertLongDateToStr(followup_time).substring(8,
					// 10)) < 8)) {
					// msg = msg +
					// "<br>Follow-up time should be greater than 8am and less than 9pm!";
					// }
				}
			}
			if (!followup_time.equals("") &&
					Long.parseLong(ConvertLongDateToStr(followup_time)) >= Long.parseLong(ConvertLongDateToStr(AddDayMonthYearStr(ToLongDate(kknow()), 31, 0, 0, 0)))) {
				msg += "<br>Enquiry Follow-up time can't exceed 30 days!";
			}
			if (followup_followuptype_id.equals("0")) {
				msg = msg + "<br>Select Follow-up Type!";
			}
		}
		if ((branch_brand_id.equals("56") || branch_brand_id.equals("11")) && followupcount > 0) {
			if (enquiry_option_id.equals("0")) {
				msg += "<br>Colour is not Selected!";
			}
			msg += new Enquiry_Dash_Methods().CheckEnquiryFields(enquiry_id, branch_brand_id, comp_id);
		} else if (followupcount > 1) {
			if (enquiry_option_id.equals("0") && !branch_brand_id.equals("1") && !branch_brand_id.equals("200") && !branch_brand_id.equals("60")) {
				msg = msg + "<br>Colour is not Selected!";
			}
			msg = msg + new Enquiry_Dash_Methods().CheckEnquiryFields(enquiry_id, branch_brand_id, comp_id);
		}

		// if (branch_brand_id.equals("1") ||
		// branch_brand_id.equals("2")) {

		// if (enquiry_option_id.equals("0")) {
		// msg = msg + "<br>Colour is not Selected!";
		// }

		// }
		// if (msg.equals("") && (!followupdesc_id.equals("0") || !followup_desc.equals(""))) {
		// if (!followupdesc_id.equals("0") && !followup_desc.equals("")) {
		// followup_desc = followupdesc_name + "<br/>" + followup_desc;
		// } else if (!followupdesc_id.equals("0") && followup_desc.equals("")) {
		// followup_desc = followupdesc_name;
		// }
		// }

	}

	protected void UpdateFields() {
		try {
			if (msg.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry_followup"
						+ " SET "
						+ " followup_followupstatus_id = " + followup_followupstatus_id + ","
						+ " followup_feedbacktype_id = " + followup_feedbacktype_id + ","
						+ " followup_desc = '" + followup_desc + "',"
						+ " followup_modified_id = " + emp_id + " , "
						+ " followup_modified_time = '" + ToLongDate(kknow()) + "'"
						+ " WHERE followup_desc = ''"
						+ " AND followup_enquiry_id = " + enquiry_id;
				updateQuery(StrSql);
				if (branch_brand_id.equals("60")) {
					StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
							+ " SET"
							+ " enquiry_jlr_enquirystatus = '" + enq_jlr_enquirystatus + "'"
							+ " WHERE enquiry_id= " + enquiry_id;
					updateQuery(StrSql);

					if (!jlr_enquirystatus.equals(enq_jlr_enquirystatus)) {
						String history_actiontype = "Enquiry Status";
						StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
								+ " (history_enquiry_id,"
								+ " history_emp_id,"
								+ " history_datetime,"
								+ " history_actiontype,"
								+ " history_oldvalue,"
								+ " history_newvalue) "
								+ " VALUES ("
								+ " '" + enquiry_id + "',"
								+ " '" + emp_id + "',"
								+ " '" + ToLongDate(kknow()) + "',"
								+ " '" + history_actiontype + "',"
								+ " '" + jlr_enquirystatus + "',"
								+ " '" + enq_jlr_enquirystatus + "')";
						updateQuery(StrSql);
						jlr_enquirystatus_update = "no";
					}
				}
				if (enquiry_status_id.equals("1")) {
					// assigning the variable to add homevishit followup
					followupfeedbacktype_id = followup_feedbacktype_id;
					// setting the feedback type and desc empty for new followup to add
					AddFields();
					if (followupfeedbacktype_id.equals("9")) {
						String date = ToLongDate(kknow());
						new Enquiry_Quickadd()
								.AddCustomCRMFields(enquiry_id, date, "homevisit", comp_id);
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	// protected void DeleteFields() {
	// if (msg.equals("")) {
	// try {
	// StrSql = "Delete FROM " + compdb(comp_id) +
	// "axela_sales_enquiry_followup WHERE followup_id =" + followup_id + "";
	// updateQuery(StrSql);
	// } catch (Exception ex) {
	// SOPError("Axelaauto-APP====" + this.getClass().getName());
	// SOPError("Axelaauto-APP=====" + new
	// Exception().getStackTrace()[0].getMethodName() + ": " + ex);
	// }
	// }
	// }

	public void AddFields() {
		CheckForm();
		if (msg.equals("")) {
			try {
				if (!status.equals("add")) {
					followup_desc = "";
					followup_feedbacktype_id = "0";
				}

				StrSql = " INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_followup "
						+ " ( "
						+ " followup_enquiry_id, "
						+ " followup_emp_id, "
						+ " followup_followuptype_id, "
						+ " followup_followup_time, "
						+ " followup_feedbacktype_id, "
						+ " followup_desc, "
						+ " followup_entry_id, "
						+ " followup_entry_time, "
						+ " followup_trigger) "
						+ " values "
						+ "("
						+ "'" + enquiry_id + "',"
						+ "" + followup_emp_id + ", "
						+ "" + followup_followuptype_id + ", "
						+ " '" + ConvertLongDateToStr(followup_time) + "', "
						+ "" + followup_feedbacktype_id + ", "
						+ " '" + followup_desc + "', "
						+ "" + followup_entry_id + ", "
						+ " '" + followup_entry_time + "', "
						+ "0 "
						+ ")";
				updateQuery(StrSql);

				if (branch_brand_id.equals("60") && !jlr_enquirystatus_update.equals("no")) {
					StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
							+ " SET"
							+ " enquiry_jlr_enquirystatus = '" + enq_jlr_enquirystatus + "'"
							+ " WHERE enquiry_id = " + enquiry_id;
					updateQuery(StrSql);

					if (!jlr_enquirystatus.equals(enq_jlr_enquirystatus)) {
						String history_actiontype = "Enquiry Status";
						StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
								+ " (history_enquiry_id,"
								+ " history_emp_id,"
								+ " history_datetime,"
								+ " history_actiontype,"
								+ " history_oldvalue,"
								+ " history_newvalue) "
								+ " VALUES ("
								+ " '" + enquiry_id + "',"
								+ " '" + emp_id + "',"
								+ " '" + ToLongDate(kknow()) + "',"
								+ " '" + history_actiontype + "',"
								+ " '" + jlr_enquirystatus + "',"
								+ " '" + enq_jlr_enquirystatus + "')";
						updateQuery(StrSql);
					}
				}
				EnquiryPriorityUpdate(comp_id, enquiry_id);
			} catch (Exception ex) {
				SOPError("Axelaauto-APP====" + this.getClass().getName());
				SOPError("Axelaauto-APP===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}
	public String PopulateFollowuptype(String comp_id) {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value = 0> Select </option>");
		try {
			StrSql = "SELECT followuptype_id, followuptype_name " + "FROM  " + compdb(comp_id) + "axela_sales_enquiry_followup_type " + "WHERE 1=1 " + "order by followuptype_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {

				Str.append("<option value=").append(crs.getString("followuptype_id")).append("");
				Str.append(StrSelectdrop(crs.getString("followuptype_id"), followup_followuptype_id));
				Str.append(">").append(crs.getString("followuptype_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-APP=== " + this.getClass().getName());
			SOPError("Axelaauto-APP===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateFollowupDesc(String comp_id) {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value = 0> Select </option>");
		try {
			StrSql = "SELECT feedbacktype_id, feedbacktype_name " + "FROM  " + compdb(comp_id) + "axela_sales_enquiry_followup_feedback_type " + "WHERE 1=1 " + "ORDER BY feedbacktype_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("feedbacktype_id")).append("");
				Str.append(StrSelectdrop(crs.getString("feedbacktype_id"), followupdesc_id));
				Str.append(">").append(crs.getString("feedbacktype_name")).append("</option>\n");
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
	// + " FROM axela_sales_enquiry_followup_status" + " WHERE 1 = 1"
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

	// public String PopulateFeedbacktype() {
	// StringBuilder Str = new StringBuilder();
	// Str.append("<option value = 0> Select </option>");
	// try {
	// StrSql = "SELECT feedbacktype_id, feedbacktype_name"
	// + " FROM axela_sales_enquiry_followup_feedback_type"
	// + " WHERE 1 = 1" + " ORDER BY feedbacktype_name";
	// CachedRowSet crs =processQuery(StrSql, 0);
	// while (crs.next()) {
	//
	// Str.append("<option value=").append(crs.getString("feedbacktype_id"));
	// Str.append(StrSelectdrop(crs.getString("feedbacktype_id"),
	// followup_feedbacktype_id));
	// Str.append(">").append(crs.getString("feedbacktype_name")).append("</option>\n");
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
}
