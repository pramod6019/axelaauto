//sangita
package axela.insurance;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import axela.service.Report_Check;
import cloudify.connect.Connect;

public class Report_Field_Exe_Followup extends Connect {

	public String StrHTML = "";
	public String StrSearch = "";
	public String StrSql = "";
	public String branch_id = "0", exe_id = "0";
	public String dr_branch_id = "0";
	public String BranchAccess = "";
	public String comp_id = "0", dr_feedbacktype_id = "0", priority_id = "0";
	public String ExeAccess = "";
	public String go = "";
	public String starttime = "", start_time = "";
	public String endtime = "", end_time = "";
	public String veh_emp_id = "";
	public String veh_id = "";
	public String pending_followup = "";
	public String msg = "";
	public String insurfollowup_followuptype_id = "0", insurpolicy_field_emp_id = "0";
	public Report_Check reportexe = new Report_Check();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_report_access, emp_mis_access, emp_service_vehicle_access, emp_insurance_enquiry_access", request, response);
			if (!comp_id.equals("0")) {
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				go = PadQuotes(request.getParameter("submit_button"));
				start_time = DateToShortDate(kknow());
				end_time = DateToShortDate(kknow());
				GetValues(request, response);
				if (go.equals("Go")) {
					CheckForm();
					StrSearch = ExeAccess.replace("emp_id", "c.emp_id");
					// + BranchAccess.replace("branch_id", "veh_branch_id") +
					// "";

					if (!insurpolicy_field_emp_id.equals("") && !insurpolicy_field_emp_id.equals("0")) {
						StrSearch += " AND insurfollowup_insur_field_emp_id = " + insurpolicy_field_emp_id + "";
					}
					if (!dr_feedbacktype_id.equals("0")) {
						StrSearch += " AND insurfollowup_disposition_id =" + dr_feedbacktype_id;
					}
					if (pending_followup.equals("0")) {
						StrSearch = StrSearch + " and substr(insurfollowup_followup_time,1,8) >= SUBSTR('" + starttime + "', 1, 8)"
								+ " AND SUBSTR(insurfollowup_followup_time, 1, 8) <= SUBSTR('" + endtime + "', 1, 8) ";
					}
					if (pending_followup.equals("1")) {
						StrSearch = StrSearch + " AND SUBSTR(insurfollowup_followup_time,1,8) >= SUBSTR('" + starttime + "', 1, 8)"
								+ " AND SUBSTR(insurfollowup_followup_time, 1, 8) <= SUBSTR('" + endtime + "', 1, 8) "
								+ " AND insurfollowup_desc = ''";
					}

					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						StrHTML = ListData();
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		exe_id = PadQuotes(request.getParameter("dr_emp_id"));
		// if (branch_id.equals("0")) {
		// dr_branch_id =
		// CNumeric(PadQuotes(request.getParameter("dr_branch")));
		// } else {
		// dr_branch_id = branch_id;
		// }
		dr_feedbacktype_id = CNumeric(PadQuotes(request.getParameter("dr_feedbacktype_id")));
		insurpolicy_field_emp_id = CNumeric(PadQuotes(request.getParameter("dr_field_exe_id")));
		starttime = PadQuotes(request.getParameter("txt_starttime"));
		endtime = PadQuotes(request.getParameter("txt_endtime"));
		if (starttime.equals("")) {
			starttime = ReportStartdate();
		}

		if (endtime.equals("")) {
			endtime = strToShortDate(ToShortDate(kknow()));
		}
		pending_followup = PadQuotes(request.getParameter("chk_pending_followup"));
		if (pending_followup.equals("on")) {
			pending_followup = "1";
		} else {
			pending_followup = "0";
		}

	}

	public String ListData() {
		try {
			int count = 0;
			StringBuilder Str = new StringBuilder();
			StrSql = " SELECT "
					+ " insurfollowup_insurenquiry_id,"
					+ " customer_id,"
					+ " customer_name,"
					+ " contact_id,"
					+ " CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname) AS contact_name,"
					+ " contact_mobile1, contact_mobile2, contact_email1, contact_email2,"
					+ " insurenquiry_reg_no,"
					+ " variant_name,"
					+ " insurdisposition_name,"
					+ " insurfollowup_followup_time,"
					+ " followuptype_name, "
					+ " insurfollowup_insur_field_emp_id,"
					+ " emp_name, "
					+ " insurfollowup_disposition_id,"
					+ " insurfollowup_desc "
					+ " FROM " + compdb(comp_id) + "axela_emp "
					+ " INNER JOIN " + compdb(comp_id) + "axela_insurance_followup ON insurfollowup_insur_field_emp_id = emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_insurance_disposition ON insurdisposition_id = insurfollowup_disposition_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_insurance_enquiry ON insurenquiry_id = insurfollowup_insurenquiry_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = insurenquiry_customer_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON customer_id = contact_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN axelaauto.axela_preowned_variant ON variant_id = insurenquiry_variant_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_insurance_followup_type ON followuptype_id = insurfollowup_followuptype_id "
					+ " WHERE 1 = 1 "
					+ StrSearch
					+ " GROUP BY insurfollowup_id"
					+ " ORDER BY insurfollowup_followup_time DESC";
			SOP("StrSql=========" + StrSql);
			CachedRowSet crs1 = processQuery(StrSql, 0);
			if (crs1.isBeforeFirst()) {
				Str.append("<div class=\"table-responsive table-bordered\">\n");
				Str.append("<table class=\"table table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead>\n");
				Str.append("<tr>\n");
				Str.append("<th data-hide=\"phone\" align=center>#</th>\n");
				Str.append("<th data-toggle=\"true\">Insurance Enquiry ID</th>\n");
				Str.append("<th style=\"width:200px;\" data-hide=\"phone\" align=center>Customer</th>\n");
				Str.append("<th data-hide=\"phone, tablet\" align=center>Reg. No.</th>\n");
				Str.append("<th data-hide=\"phone, tablet\" align=center>Item</th>\n");
				Str.append("<th data-hide=\"phone, tablet\" align=center>Disposition</th>\n");

				Str.append("<th align=center>Field Executive</th>\n");
				Str.append("<th  align=center>Follow-up Time</th>\n");
				Str.append("<th data-hide=\"phone, tablet\" align=center>Follow-up Type</th>\n");
				Str.append("<th data-hide=\"phone, tablet\" align=center>Sale Date</th>\n");
				Str.append("<th data-hide=\"phone, tablet\" align=center>Renewal Date</th>\n");
				Str.append("<th data-hide=\"phone, tablet\" align=center>Description</th>\n");
				Str.append("</tr>");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs1.next()) {
					count++;
					Str.append("<tr onmouseover='ShowCustomerInfo(" + crs1.getString("insurfollowup_insurenquiry_id") + ")' onmouseout='HideCustomerInfo("
							+ crs1.getString("insurfollowup_insurenquiry_id") + ");'");
					Str.append(" style='height:200px'>\n");
					Str.append("<td align=center>").append(count).append("</td>");
					Str.append("<td align=center><a href=../insurance/insurance-enquiry-list.jsp?insurenquiry_id=").append(crs1.getString("insurfollowup_insurenquiry_id")).append(">")
							.append(crs1.getString("insurfollowup_insurenquiry_id")).append("</a></td>\n");
					Str.append("<td valign=\"top\" align=\"left\" nowrap>");
					Str.append("<a href=\"../customer/customer-list.jsp?customer_id=");
					Str.append(crs1.getString("customer_id")).append("\">");
					Str.append(crs1.getString("customer_name")).append("</a>");
					Str.append("<br><a href=\"../customer/customer-contact-list.jsp?contact_id=");
					Str.append(crs1.getString("contact_id")).append("\">");
					Str.append(crs1.getString("contact_name")).append("</a>");
					if (!crs1.getString("contact_mobile1").equals("")) {
						Str.append("<br>").append(SplitPhoneNoSpan(crs1.getString("contact_mobile1"), 5, "M", crs1.getString("insurfollowup_insurenquiry_id")))
								.append(ClickToCall(crs1.getString("contact_mobile1"), comp_id));
					}
					if (!crs1.getString("contact_mobile2").equals("")) {
						Str.append("<br>").append(SplitPhoneNoSpan(crs1.getString("contact_mobile2"), 5, "M", crs1.getString("insurfollowup_insurenquiry_id")))
								.append(ClickToCall(crs1.getString("contact_mobile2"), comp_id));
					}
					if (!crs1.getString("contact_email1").equals("")) {
						Str.append("<br><span class='customer_info customer_" + crs1.getString("insurfollowup_insurenquiry_id") + "'  style='display: none;'><a href=\"mailto:")
								.append(crs1.getString("contact_email1")).append("\">");
						Str.append(crs1.getString("contact_email1")).append("</a></span>");
					}
					if (!crs1.getString("contact_email2").equals("")) {
						Str.append("<br><span class='customer_info customer_" + crs1.getString("insurfollowup_insurenquiry_id") + "'  style='display: none;'><a href=\"mailto:")
								.append(crs1.getString("contact_email2")).append("\">");
						Str.append(crs1.getString("contact_email2")).append("</a></span>");
					}
					Str.append("</td>");
					Str.append("\n<td valign=\"top\" align=\"left\" nowrap><a href=\"../insurance/insurance-enquiry-list.jsp?insurenquiry_id=");
					Str.append(crs1.getString("insurfollowup_insurenquiry_id")).append("\">").append(SplitRegNo(crs1.getString("insurenquiry_reg_no"), 2)).append("</a></td>");
					Str.append("<td align=left>").append(crs1.getString("variant_name")).append("</td>");
					Str.append("<td align=left>").append(crs1.getString("insurdisposition_name")).append("</td>");
					Str.append("<td align=center><a href=../portal/executive-summary.jsp?emp_id=").append(crs1.getString("insurfollowup_insur_field_emp_id"))
							.append(">").append(crs1.getString("emp_name")).append("</a></td>");
					Str.append("<td align=center><a href=\"javascript:remote=window.open('../insurance/insurance-enquiry-dash.jsp?insurenquiry_id=")
							.append(crs1.getString("insurfollowup_insurenquiry_id")).append("#tabs-2")
							.append("','insurdash','');remote.focus();\">").append(strToLongDate(crs1.getString("insurfollowup_followup_time"))).append("</a></td>\n");
					Str.append("<td align=left>").append(crs1.getString("followuptype_name")).append("</td>");
					Str.append("<td align=left>").append("</td>");
					Str.append("<td align=left>").append("</td>");
					Str.append("<td align=left>").append(crs1.getString("insurfollowup_desc")).append("</td>");
					Str.append("</tr>");
				}
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
			} else {
				Str.append("<font color=red><b>No Insurance Follow-up found!</b></font>");
			}
			crs1.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
	protected void CheckForm() {
		msg = "";
		// if (dr_branch_id.equals("0")) {
		// msg = msg + "<br>Select Branch!";
		// }
		if (starttime.equals("")) {
			msg += "<br>Select Start Date!";
		}

		if (!starttime.equals("")) {
			if (isValidDateFormatShort(starttime)) {
				starttime = ConvertShortDateToStr(starttime);
				start_time = strToShortDate(starttime);
			} else {
				msg += "<br>Enter Valid Start Date!";
				starttime = "";
			}
		}

		if (endtime.equals("")) {
			msg += "<br>Select End Date!<br>";
		}

		if (!endtime.equals("")) {
			if (isValidDateFormatShort(endtime)) {
				endtime = ConvertShortDateToStr(endtime);
				if (!starttime.equals("") && !endtime.equals("") && Long.parseLong(starttime) > Long.parseLong(endtime)) {
					msg += "<br>Start Date should be less than End date!";
				}
				end_time = strToShortDate(endtime);
				// endtime = ToLongDate(AddHoursDate(StringToDate(endtime), 1,
				// 0, 0));
			} else {
				msg += "<br>Enter Valid End Date!";
				endtime = "";
			}
		}

	}

	public String PopulateFeedbackType(String comp_id, String dr_feedbacktype_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = " SELECT insurfeedbacktype_id, insurfeedbacktype_name"
					+ " FROM " + compdb(comp_id) + "axela_insurance_enquiry_feedback_type"
					+ " WHERE	1 = 1"
					+ " GROUP BY insurfeedbacktype_id"
					+ " ORDER BY insurfeedbacktype_name";
			// SOP("Feed===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			// SOP("dr_feedbacktype_id==" + dr_feedbacktype_id);
			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("insurfeedbacktype_id"));
				Str.append(StrSelectdrop(crs.getString("insurfeedbacktype_id"), dr_feedbacktype_id));
				Str.append(">").append(crs.getString("insurfeedbacktype_name")).append("</option>\n");
			}

			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulatePriority() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=\"0\"> Select </option>\n");
		try {
			StrSql = "SELECT priorityinsurfollowup_id, priorityinsurfollowup_name"
					+ " FROM " + compdb(comp_id) + "axela_insurance_followup_priority"
					+ " ORDER BY priorityinsurfollowup_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("priorityinsurfollowup_id"));
				Str.append(StrSelectdrop(crs.getString("priorityinsurfollowup_id"), priority_id));
				Str.append(">").append(crs.getString("priorityinsurfollowup_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateFollowuptype() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=\"0\"> Select </option>\n");
		try {
			StrSql = "SELECT followuptype_id, followuptype_name"
					+ " FROM " + compdb(comp_id) + "axela_insurance_followup_type"
					+ " ORDER BY followuptype_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("followuptype_id"));
				Str.append(StrSelectdrop(crs.getString("followuptype_id"), insurfollowup_followuptype_id));
				Str.append(">").append(crs.getString("followuptype_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateFieldExecutive(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') AS fieldemp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_fieldinsur = 1"
					// + " AND emp_active = 1"
					+ " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\">Select Executive</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id"));
				Str.append(Selectdrop(crs.getInt("emp_id"), insurpolicy_field_emp_id));
				Str.append(">").append(crs.getString("fieldemp_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

}
