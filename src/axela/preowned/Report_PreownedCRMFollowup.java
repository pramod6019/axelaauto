//divya 30th may 2013
package axela.preowned;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_PreownedCRMFollowup extends Connect {

	public String StrHTML = "";
	public String StrSearch = "";
	public String StrSql = "";
	public String dr_branch_id = "0", branch_id = "0";
	public String BranchAccess = "";
	public String starttime = "", start_time = "";
	public String endtime = "", end_time = "";
	public String precrmfollowupdays_id = "0";
	// public String crmdays = "0";
	public String precrmfollowup_precrmfeedbacktype_id = "0";
	public String ExeAccess = "";
	public String comp_id = "0";
	public String go = "";
	public String team_id = "";
	public String exe_id = "";
	public String precrmfollowup_satisfied = "0";
	public String precrmfollowupdays_precrmtype_id = "0";
	public String historydate = "";
	// // public String start_crmfollowup_date = "";
	public String preowned_emp_id = "";
	public String preowned_id = "";
	// public String enquiry_dmsno = "";
	public String pending_followup = "", satisfied = "";
	public String msg = "";
	public String emp_all_exe = "";
	public axela.sales.MIS_Check1 mis = new axela.sales.MIS_Check1();
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_report_access, emp_mis_access", request, response);
			if (!comp_id.equals("0")) {
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				emp_all_exe = CNumeric(GetSession("emp_all_exe", request));
				go = PadQuotes(request.getParameter("submit_button"));
				GetValues(request, response);
				CheckForm();
				if (go.equals("Go")) {
					if (!team_id.equals("0")) {
						StrSearch = StrSearch + " AND teamtrans_team_id = " + team_id;
					}
					if (!exe_id.equals("0")) {
						StrSearch = StrSearch + " AND teamtrans_emp_id = " + exe_id;
					}
					if (!precrmfollowupdays_precrmtype_id.equals("0")) {
						StrSearch = StrSearch + " AND precrmfollowupdays_precrmtype_id = " + precrmfollowupdays_precrmtype_id;
					}
					if (!precrmfollowupdays_id.equals("0")) {
						StrSearch = StrSearch + " AND precrmfollowup_precrmfollowupdays_id = " + precrmfollowupdays_id;
					}
					if (!precrmfollowup_precrmfeedbacktype_id.equals("0")) {
						StrSearch += " AND precrmfollowupdays_precrmtype_id = " + precrmfollowup_precrmfeedbacktype_id + "";
					}
					if (!precrmfollowup_satisfied.equals("0")) {
						// SOP("it sis coming");
						StrSearch += " AND precrmfollowup_satisfied = " + precrmfollowup_satisfied;
					}
					if (pending_followup.equals("0")) {
						if (!starttime.equals("")) {
							StrSearch = StrSearch + " AND substr(precrmfollowup_followup_time,1,8) >= substr('" + starttime + "',1,8)";
						}
						if (!endtime.equals("")) {
							StrSearch = StrSearch + " AND substr(precrmfollowup_followup_time,1,8) <= substr('" + endtime + "',1,8)";
						}
					}
					if (pending_followup.equals("1")) {
						if (!starttime.equals("")) {
							StrSearch = StrSearch + " AND substr(precrmfollowup_followup_time,1,8) >= substr('" + starttime + "',1,8)";
						}
						if (!endtime.equals("")) {
							StrSearch = StrSearch + " AND substr(precrmfollowup_followup_time,1,8) <= substr('" + endtime + "',1,8)";
						}
						StrSearch = StrSearch + " AND precrmfollowupdays_precrmtype_id = 0"
								+ " AND precrmfollowupdays_desc = ''"
								+ " AND substr(precrmfollowup_followup_time,1,8) < substr('" + (ToLongDate(kknow())) + "',1,8) ";
					}

					StrSearch += BranchAccess.replace("branch_id", "preowned_branch_id");

					StrSearch += ExeAccess.replace("emp_id", "precrmfollowup_crm_emp_id");

					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						StrHTML = PreownedCRMFollowup();
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if (branch_id.equals("0")) {
			dr_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
		} else {
			dr_branch_id = branch_id;
		}
		// dr_branch_id =
		// CNumeric(PadQuotes(request.getParameter("dr_branch")));
		precrmfollowupdays_id = CNumeric(PadQuotes(request.getParameter("dr_crmdays_id")));
		// SOP("precrmfollowupdays_id==111==" + precrmfollowupdays_id);
		starttime = PadQuotes(request.getParameter("txt_starttime"));
		endtime = PadQuotes(request.getParameter("txt_endtime"));
		if (starttime.equals("")) {
			starttime = ReportStartdate();
		}
		if (endtime.equals("")) {
			endtime = strToShortDate(ToShortDate(kknow()));
		}
		team_id = CNumeric(PadQuotes(request.getParameter("dr_team")));
		exe_id = CNumeric(PadQuotes(request.getParameter("dr_executive")));
		if (!PadQuotes(request.getParameter("txt_dr_crmdays_id")).equals("")) {
			precrmfollowupdays_id = CNumeric(PadQuotes(request.getParameter("txt_dr_crmdays_id")));
			// SOP("precrmfollowupdays_id==222==" + precrmfollowupdays_id);
		}

		// precrmfollowupdays_id = CNumeric(PadQuotes(request.getParameter("crmdays")));
		// SOP("precrmfollowupdays_id-------------s3------" + precrmfollowupdays_id);
		precrmfollowupdays_precrmtype_id = CNumeric(PadQuotes(request.getParameter("dr_precrmfollowupdays_precrmtype_id")));
		precrmfollowup_precrmfeedbacktype_id = CNumeric(PadQuotes(request.getParameter("dr_precrmfeedbacktype_id")));
		// precrmfollowup_satisfied =
		// CNumeric(PadQuotes(request.getParameter("dr_feedbacktype1")));
		pending_followup = PadQuotes(request.getParameter("chk_pending_followup"));
		precrmfollowup_satisfied = CNumeric(PadQuotes(request.getParameter("dr_crm_satisfied")));
		if (pending_followup.equals("on")) {
			pending_followup = "1";
		} else {
			pending_followup = "0";
		}
	}

	public String PreownedCRMFollowup() {
		try {
			int count = 0;
			StringBuilder Str = new StringBuilder();
			StrSql = " SELECT preowned_id, customer_id, customer_name, contact_id,"
					+ " COALESCE(CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname), '') AS contact_name,"
					+ " contact_mobile1, contact_mobile2, contact_phone1, contact_phone2,"
					+ " contact_email1, contact_email2,"
					+ " preowned_date, preowned_close_date, COALESCE(variant_name,'') AS variant_name, preownedstatus_name,"
					+ " precrmfollowup_followup_time, precrmfollowupdays_desc,"
					+ " COALESCE(precrmfeedbacktype_name,'') as precrmfeedbacktype_name,"
					+ " COALESCE(precrmfollowup_satisfied, 0) AS precrmfollowup_satisfied,"
					+ " COALESCE(e.emp_id, 0) as preowned_emp_id,"
					+ " COALESCE(concat(e.emp_name,' (', e.emp_ref_no, ')'), '') as preowned_emp_name,"
					+ " COALESCE(c.emp_id, 0) as precrmfollowup_crm_emp_id,"
					+ " COALESCE(concat(c.emp_name,' (', c.emp_ref_no, ')'), '') as emp_name,"
					+ " precrmfollowup_id, precrmfollowupdays_daycount, precrmfollowupdays_desc"
					+ " FROM " + compdb(comp_id) + "axela_preowned"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = preowned_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = preowned_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					// + " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_stage ON stage_id = enquiry_stage_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_status ON preownedstatus_id = preowned_preownedstatus_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_crmfollowup ON precrmfollowup_preowned_id = preowned_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_crmfollowupdays ON precrmfollowup_precrmfollowupdays_id = precrmfollowupdays_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp c ON c.emp_id = precrmfollowup_crm_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp e ON e.emp_id = preowned_emp_id"
					+ " LEFT JOIN axela_preowned_variant ON variant_id = preowned_variant_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id = preowned_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_preowned_crmfeedbacktype ON precrmfeedbacktype_id = precrmfollowupdays_precrmtype_id"
					+ " WHERE 1 = 1 ";
			if (!dr_branch_id.equals("0")) {
				StrSql += " AND preowned_branch_id = " + dr_branch_id;
			}
			StrSql += StrSearch
					+ " GROUP BY precrmfollowup_id ORDER BY preowned_id"
					+ " limit 1000";
			SOP("StrSql=====crm folwup====" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				Str.append("<div class=\"  table-bordered\">\n");
				Str.append("<table class=\"table table-hover  \" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th align=center>#</th>\n");
				Str.append("<th data-toggle=\"true\" align=center>Preowned ID</th>\n");
				Str.append("<th align=center>Customer</th>\n");
				Str.append("<th align=center>Date</th>\n");
				Str.append("<th data-hide=\"phone\" align=center>Variant</th>\n");
				// Str.append("<th data-hide=\"phone\" align=center>Stage</th>\n");
				Str.append("<th data-hide=\"phone\" align=center>Status</th>\n");
				Str.append("<th data-hide=\"phone\" align=center>Pre-Owned Consultant</th>\n");
				Str.append("<th data-hide=\"phone\" align=center>Preowned CRM Executives</th>\n");
				Str.append("<th data-hide=\"phone\" align=center>Preowned CRM Follow-up Date</th>\n");
				Str.append("<th data-hide=\"phone\" align=center>Preowned CRM Follow-up Days</th>\n");
				Str.append("<th data-hide=\"phone\" align=center>Preowned CRM Feedback Type</th>\n");
				Str.append("<th data-hide=\"phone\" align=center>Experience</th>\n");
				Str.append("<th data-hide=\"phone\" align=center>Description</th>\n");
				Str.append("</tr>");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					count++;
					Str.append("<tr align=center valign=top>\n");
					Str.append("<td align=center>").append(count).append("</td>");
					Str.append("<td align=center><a href=preowned-list.jsp?preowned_id=").append(crs.getString("preowned_id")).append(">").append(crs.getString("preowned_id")).append("</a></td>\n");
					Str.append("<td align=left>");
					Str.append("<a href=\"../customer/customer-list.jsp?customer_id=").append(crs.getString("customer_id")).append("\">");
					Str.append(crs.getString("customer_name")).append("</a>");
					Str.append("<br><a href=\"../customer/customer-contact-list.jsp?contact_id=").append(crs.getString("contact_id")).append("\">");
					Str.append(crs.getString("contact_name")).append("</a>");

					if (!crs.getString("contact_phone1").equals("")) {
						Str.append("<br>").append(SplitPhoneNo(crs.getString("contact_phone1"), 4, "T")).append(ClickToCall(crs.getString("contact_phone1"), comp_id));
					}

					if (!crs.getString("contact_phone2").equals("")) {
						Str.append("<br>").append(SplitPhoneNo(crs.getString("contact_phone2"), 4, "T")).append(ClickToCall(crs.getString("contact_phone2"), comp_id));
					}

					if (!crs.getString("contact_mobile1").equals("")) {
						Str.append("<br>").append(SplitPhoneNo(crs.getString("contact_mobile1"), 5, "M")).append(ClickToCall(crs.getString("contact_mobile1"), comp_id));
					}

					if (!crs.getString("contact_mobile2").equals("")) {
						Str.append("<br>").append(SplitPhoneNo(crs.getString("contact_mobile2"), 5, "M")).append(ClickToCall(crs.getString("contact_mobile2"), comp_id));
					}

					if (!crs.getString("contact_email1").equals("")) {
						Str.append("<br><a href=mailto:").append(crs.getString("contact_email1")).append(">");
						Str.append(crs.getString("contact_email1")).append("</a>");
					}

					if (!crs.getString("contact_email2").equals("")) {
						Str.append("<br><a href=mailto:").append(crs.getString("contact_email2")).append(">");
						Str.append(crs.getString("contact_email2")).append("</a>");
					}
					Str.append("</td>");
					Str.append("<td align=center>");
					Str.append(strToShortDate(crs.getString("preowned_date"))).append(" - ").append(strToShortDate(crs.getString("preowned_close_date")));
					Str.append("</td>");
					Str.append("<td align=left>").append(crs.getString("variant_name")).append("</td>");
					// Str.append("<td align=left>").append(crs.getString("stage_name")).append("</td>");
					Str.append("<td align=left>").append(crs.getString("preownedstatus_name")).append("</td>");
					Str.append("<td align=left><a href=../portal/executive-summary.jsp?emp_id=").append(crs.getString("preowned_emp_id")).append(">").append(crs.getString("preowned_emp_name"))
							.append("</a></td>");
					Str.append("<td align=left><a href=../portal/executive-summary.jsp?emp_id=").append(crs.getString("precrmfollowup_crm_emp_id")).append(">").append(crs.getString("emp_name"))
							.append("</a></td>");
					Str.append("<td align=center><a href=\"javascript:remote=window.open('preowned-dash.jsp?preowned_id=").append(crs.getString("preowned_id")).append("&precrmfollowup_id=")
							.append(crs.getString("precrmfollowup_id") + "#tabs-3").append("','preowneddash','');remote.focus();\">")
							.append(strToLongDate(crs.getString("precrmfollowup_followup_time")))
							.append("</a></td>\n");
					// Str.append("<td align=center><a href=enquiry-dash-crmfollowup.jsp?preowned_id=").append(crs.getString("preowned_id")).append("&crmfollowup_id=").append(crs.getString("crmfollowup_id")).append(">").append(strToShortDate(crs.getString("crmfollowup_followup_time"))).append("</a></td>\n");
					Str.append("<td align=left>").append(crs.getString("precrmfollowupdays_daycount")).append(crs.getString("precrmfollowupdays_desc")).append("</td>\n");
					Str.append("<td align=left>").append(crs.getString("precrmfeedbacktype_name")).append("</td>");
					if (crs.getString("precrmfollowup_satisfied").equals("1")) {
						satisfied = "Satisfied";
					} else if (crs.getString("precrmfollowup_satisfied").equals("2")) {
						satisfied = "Dis-Satisfied";
					} else if (crs.getString("precrmfollowup_satisfied").equals("0")) {
						satisfied = "";
					}

					Str.append("<td align=left>").append(satisfied).append("</td>");
					Str.append("<td align=left>").append(crs.getString("precrmfollowupdays_desc")).append("</td>");
					Str.append("</tr>");
				}
				Str.append("</tbody>\n");
				Str.append("</table>");
				Str.append("</div>\n");
			} else {
				Str.append("<font color=red><b>No Preowned CRM Follow-up found!</b></font>");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
	// public String PopulateTeam(String exe_branch_id, String comp_id) {
	// StringBuilder Str = new StringBuilder();
	// try {
	// StrSql = "SELECT team_id, team_name "
	// + " from " + compdb(comp_id) + "axela_sales_team"
	// + " WHERE 1=1 ";
	// if (!exe_branch_id.equals("0")) {
	// StrSql += " AND team_branch_id=" + exe_branch_id;
	// }
	// StrSql += " GROUP BY team_id"
	// + " ORDER BY team_name";
	// CachedRowSet crs = processQuery(StrSql, 0);
	// Str.append("<select name=\"dr_team\" class=\"form-control\" id=\"dr_team\" onchange=\"PopulateExecutives();\">");
	// Str.append("<option value=0>Select</option>");
	// while (crs.next()) {
	// Str.append("<option value=").append(crs.getString("team_id")).append("");
	// Str.append(StrSelectdrop(crs.getString("team_id"), team_id));
	// Str.append(">").append(crs.getString("team_name")).append("</option>\n)");
	// }
	// Str.append("</select>");
	// crs.close();
	// return Str.toString();
	// } catch (Exception ex) {
	// SOPError("Axelaauto===" + this.getClass().getName());
	// SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
	// return "";
	// }
	// }
	public String PopulatePreownedExecutives(String exe_team_id, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT emp_id, CONCAT(emp_name,' (',emp_ref_no,')') as emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_active = '1'  and emp_preowned='1' "
					+ " GROUP BY emp_id ORDER BY emp_name";
			// SOP("Queryy=======" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=\"dr_executive\" class=\"form-control\" id=\"dr_executive\">");
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id")).append("");
				Str.append(StrSelectdrop(crs.getString("emp_id"), exe_id));
				Str.append(">").append(crs.getString("emp_name")).append("</option> \n");
			}
			Str.append("</select>");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulatePreownedCRMDays(String comp_id, String dr_branch_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT precrmfollowupdays_id, concat(precrmfollowupdays_daycount, precrmfollowupdays_desc) as precrmfollowupdays_desc"
					+ " FROM " + compdb(comp_id) + "axela_preowned_crmfollowupdays"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = precrmfollowupdays_brand_id"
					+ " WHERE 1 = 1"
					+ " AND precrmfollowupdays_precrmtype_id = 1";
			if (!dr_branch_id.equals("0") && !dr_branch_id.equals("")) {
				StrSql += " AND branch_id = " + dr_branch_id + "";
			}
			// if (!precrmfollowupdays_precrmtype_id.equals("0")) {
			// StrSql += " AND precrmfollowupdays_precrmtype_id =" + precrmfollowupdays_precrmtype_id + "";
			// }
			StrSql += " GROUP BY precrmfollowupdays_id"
					+ " ORDER BY precrmfollowupdays_daycount";
			// SOP("StrSql-------------followup---" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select id=\"dr_precrmfollowupdays_id\" name=\"dr_precrmfollowupdays_id\" class=form-control onchange=\"coming();\">");
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("precrmfollowupdays_id")).append("");
				Str.append(StrSelectdrop(crs.getString("precrmfollowupdays_id"), precrmfollowupdays_id));
				Str.append(">").append(crs.getString("precrmfollowupdays_desc")).append("</option> \n");
			}
			Str.append("</select>");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulatePreownedCRMFeedbackType() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value = 0> Select </option>");
		try {
			StrSql = "SELECT precrmfeedbacktype_id, precrmfeedbacktype_name"
					+ " FROM " + compdb(comp_id) + "axela_preowned_crmfeedbacktype"
					+ " WHERE 1=1"
					+ " ORDER BY precrmfeedbacktype_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {

				Str.append("<option value=").append(crs.getString("precrmfeedbacktype_id")).append("");
				Str.append(StrSelectdrop(crs.getString("precrmfeedbacktype_id"), precrmfollowup_precrmfeedbacktype_id));
				Str.append(">").append(crs.getString("precrmfeedbacktype_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulatePreownedCRMSatisfied() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=0>Select</option>\n");
		Str.append("<option value=1").append(StrSelectdrop("1", precrmfollowup_satisfied)).append(">Satisfied</option>\n");
		Str.append("<option value=2").append(StrSelectdrop("2", precrmfollowup_satisfied)).append(">Dis-Satisfied</option>\n");
		return Str.toString();
	}

	protected void CheckForm() {
		msg = "";
		if (dr_branch_id.equals("0")) {
			msg = msg + "<br>Select Branch!";
		}

		if (starttime.equals("")) {
			msg = msg + "<br>Select Start Date!";
		}
		if (!starttime.equals("")) {
			if (isValidDateFormatShort(starttime)) {
				starttime = ConvertShortDateToStr(starttime);
				start_time = strToShortDate(starttime);
			} else {
				msg = msg + "<br>Enter Valid Start Date!";
				starttime = "";
			}
		}
		if (endtime.equals("")) {
			msg = msg + "<br>Select End Date!";
		}
		if (!endtime.equals("")) {
			if (isValidDateFormatShort(endtime)) {
				endtime = ConvertShortDateToStr(endtime);
				if (!starttime.equals("") && !endtime.equals("") && Long.parseLong(starttime) > Long.parseLong(endtime)) {
					msg = msg + "<br>Start Date should be less than End date!";
				}
				end_time = strToShortDate(endtime);
				// endtime = ToLongDate(AddHoursDate(StringToDate(endtime), 1,
				// 0, 0));
			} else {
				msg = msg + "<br>Enter Valid End Date!";
				endtime = "";
			}
		}
		if (precrmfollowupdays_precrmtype_id.equals("0")) {
			msg = msg + "<br>Select Type!";
		}

		// if (crmdays_crmtype_id.equals("0")) {
		// msg = msg + "<br>Select CRM Type!";
		// }

	}

	public String PopulatePreownedCRMType() {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT precrmtype_id, precrmtype_name"
					+ " FROM " + compdb(comp_id) + "axela_preowned_crm_type ORDER BY precrmtype_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("precrmtype_id")).append("");
				Str.append(StrSelectdrop(crs.getString("precrmtype_id"), precrmfollowupdays_precrmtype_id));
				Str.append(">").append(crs.getString("precrmtype_name")).append("</option>\n");
			}
			crs.close();
			// SOP("option===" + Str.toString());
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
}
