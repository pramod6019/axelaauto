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

public class Report_Insurance_Followup extends Connect {

	public String StrHTML = "";
	public String StrSearch = "";
	public String StrSql = "";
	public String branch_id = "0", exe_id = "0";
	public String dr_branch_id = "0";
	// public String BranchAccess = "";
	public String comp_id = "0", dr_feedbacktype_id = "0", priority_id = "0";
	public String ExeAccess = "";
	public String go = "";
	public String starttime = "", start_time = "";
	public String endtime = "", end_time = "";
	public String insurenquiry_emp_id = "";
	public String insurenquiry_id = "";
	public String pending_followup = "";
	public String msg = "";
	public String insurfollowup_followuptype_id = "0";

	public String dr_followup_disp1_id = "0";
	public String dr_followup_disp2_id = "0";
	public String dr_followup_disp3_id = "0";
	public String dr_followup_disp4_id = "0";
	public String dr_followup_disp5_id = "0";

	public Report_Check reportexe = new Report_Check();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_report_access, emp_mis_access, emp_service_vehicle_access, emp_insurance_enquiry_access", request, response);
			if (!comp_id.equals("0")) {
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				// BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				go = PadQuotes(request.getParameter("submit_button"));
				start_time = DateToShortDate(kknow());
				end_time = DateToShortDate(kknow());
				GetValues(request, response);
				if (go.equals("Go")) {
					CheckForm();
					StrSearch = ExeAccess;
					// + BranchAccess.replace("branch_id", "insurenquiry_branch_id") +
					// "";

					// if (!dr_branch_id.equals("0")) {
					// StrSearch = StrSearch + "  and insurenquiry_branch_id = " +
					// dr_branch_id;
					// }

					if (!exe_id.equals("") && !exe_id.equals("0")) {
						StrSearch += " AND insurenquiry_emp_id = " + exe_id + "";
					}
					if (pending_followup.equals("0")) {
						StrSearch = StrSearch
								+ " AND SUBSTR(insurenquiryfollowup_followup_time,1,8) >= SUBSTR('" + starttime + "', 1, 8)"
								+ " AND SUBSTR(insurenquiryfollowup_followup_time,1,8) <= SUBSTR('" + endtime + "', 1, 8) ";
					}
					if (pending_followup.equals("1")) {
						StrSearch = StrSearch
								+ " AND SUBSTR(insurenquiryfollowup_followup_time,1,8) >= SUBSTR('" + starttime + "', 1, 8)"
								+ " AND SUBSTR(insurenquiryfollowup_followup_time,1,8) <= SUBSTR('" + endtime + "', 1, 8) "
								+ " AND insurenquiryfollowup_desc = ''"
								+ " AND SUBSTR(insurenquiryfollowup_followup_time,1,8) < SUBSTR('" + (ToLongDate(kknow())) + "',1,8) ";
					}
					if (!dr_feedbacktype_id.equals("0")) {
						StrSearch += " AND insurenquiryfollowup_feedbacktype_id = " + dr_feedbacktype_id;
					}
					if (!insurfollowup_followuptype_id.equals("0")) {
						StrSearch += " AND insurenquiryfollowup_followuptype_id =" + insurfollowup_followuptype_id;
					}
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					// if (msg.equals("")) {
					// StrHTML = FollowupDetails();
					// }
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

	public String FollowupDetails(String comp_id, String exe_id, String starttime, String endtime, String pending_followup, String dr_followup_disp1, String dr_followup_disp2,
			String dr_followup_disp3,
			String dr_followup_disp4, String dr_followup_disp5) {
		if (!exe_id.equals("") && !exe_id.equals("0")) {
			StrSearch += " AND insurenquiry_emp_id = " + exe_id + "";
		}
		SOP("pending_followup==" + pending_followup);
		if (pending_followup.equals("0")) {
			StrSearch += " AND SUBSTR(insurenquiryfollowup_followup_time,1,8) >= SUBSTR('" + starttime + "', 1, 8)"
					+ " AND SUBSTR(insurenquiryfollowup_followup_time,1,8) <= SUBSTR('" + endtime + "', 1, 8) ";
		}
		if (pending_followup.equals("1")) {
			StrSearch += " AND SUBSTR(insurenquiryfollowup_followup_time,1,8) >= SUBSTR('" + starttime + "', 1, 8)"
					+ " AND SUBSTR(insurenquiryfollowup_followup_time,1,8) <= SUBSTR('" + endtime + "', 1, 8) "
					+ " AND insurenquiryfollowup_disp1 = ''"
					+ " AND SUBSTR(insurenquiryfollowup_followup_time,1,8) < SUBSTR('" + (ToLongDate(kknow())) + "',1,8) ";
		}

		if (!dr_followup_disp1.equals("")) {
			StrSearch += " AND insurenquiryfollowup_disp1 = '" + dr_followup_disp1 + "'";
		}
		if (!dr_followup_disp2.equals("")) {
			StrSearch += " AND insurenquiryfollowup_disp2 = '" + dr_followup_disp2 + "'";
		}
		if (!dr_followup_disp3.equals("")) {
			StrSearch += " AND insurenquiryfollowup_disp3 = '" + dr_followup_disp3 + "'";
		}
		if (!dr_followup_disp4.equals("")) {
			StrSearch += " AND insurenquiryfollowup_disp4 = '" + dr_followup_disp4 + "'";
		}
		if (!dr_followup_disp5.equals("")) {
			StrSearch += " AND insurenquiryfollowup_disp5 = '" + dr_followup_disp5 + "'";
		}

		try {
			int count = 0;
			StringBuilder Str = new StringBuilder();

			StrSql = "SELECT "
					+ " insurenquiry_id, "
					+ " COALESCE (variant_name, '') AS variant_name, "
					// + " COALESCE (insurfeedbacktype_name, '') AS insurfeedbacktype_name, "
					+ " insurenquiryfollowup_followup_time, "
					+ " insurenquiryfollowup_disp1, "
					+ " insurenquiryfollowup_disp2, "
					+ " insurenquiryfollowup_disp3, "
					+ " insurenquiryfollowup_disp4, "
					+ " insurenquiryfollowup_disp5, "

					// + " insurenquiryfollowup_desc, "
					// + " COALESCE (insurfollowuptype_name, '') AS insurfollowuptype_name, "
					+ " REPLACE (insurenquiry_sale_date,SUBSTR(insurenquiry_sale_date, 1, 4),'2017') AS insurenquiry_sale_date, "
					+ " REPLACE (insurenquiry_renewal_date,SUBSTR(insurenquiry_renewal_date,1,4),'2017') AS insurenquiry_renewal_date, "
					+ " COALESCE (emp_id, 0) AS insurenquiry_emp_id,"
					+ " CONCAT(emp_name,' (',emp_ref_no,')') AS emp_name, "
					+ " insurenquiry_id, "
					+ " insurenquiry_reg_no, "
					+ " insurenquiryfollowup_id, "
					+ " customer_id, "
					+ " customer_name, "
					+ " contact_id, "
					+ " CONCAT(title_desc,' ',contact_fname,' ',contact_lname) AS contact_name,	 "
					+ " contact_mobile1, "
					+ " contact_mobile2, "
					+ " contact_email1, "
					+ " contact_email2 "
					+ " FROM " + compdb(comp_id) + "axela_insurance_enquiry "
					+ " INNER JOIN " + compdb(comp_id) + "axela_insurance_enquiry_followup ON insurenquiryfollowup_insurenquiry_id = insurenquiry_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = insurenquiry_customer_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = insurenquiry_contact_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id "
					// + " LEFT JOIN " + compdb(comp_id) + "axela_insurance_enquiry_feedback_type ON insurfeedbacktype_id = insurenquiryfollowup_feedbacktype_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = insurenquiry_emp_id "
					// + " INNER JOIN " + compdb(comp_id) + "axela_insurance_enquiry_followup_type ON insurfollowuptype_id = insurenquiryfollowup_followuptype_id "
					+ " INNER JOIN axelaauto.axela_preowned_variant ON variant_id = insurenquiry_variant_id "
					+ " INNER JOIN axelaauto.axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id "
					+ " WHERE 1 = 1 "
					+ StrSearch
					+ " ORDER BY insurenquiryfollowup_followup_time, insurenquiry_id "
					+ " LIMIT 1000 ";

			SOP("StrSql=========" + StrSql);

			CachedRowSet crs1 = processQuery(StrSql, 0);
			if (crs1.isBeforeFirst()) {
				Str.append("<div class=\"table-responsive table-bordered\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead>\n");
				Str.append("<tr>\n");
				Str.append("<th data-hide=\"phone\" align=center>#</th>\n");
				Str.append("<th data-toggle=\"true\">Enquiry ID</th>\n");
				Str.append("<th style=\"width:200px;\" data-hide=\"phone\" align=center>Customer</th>\n");
				Str.append("<th data-hide=\"phone, tablet\" align=center>Reg. No.</th>\n");
				Str.append("<th data-hide=\"phone, tablet\" align=center>Variant</th>\n");
				Str.append("<th align=center>Sale Date</th>\n");
				Str.append("<th align=center>Renewal Date</th>\n");
				// Str.append("<th data-hide=\"phone, tablet\" align=center>Feedback Type</th>\n");
				// Str.append("<th align=center>Feedback</th>\n");
				Str.append("<th align=center>Executive</th>\n");
				Str.append("<th align=center>Follow-up Time</th>\n");
				Str.append("<th align=center>Disposition</th>\n");

				Str.append("</tr>");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs1.next()) {
					count++;

					Str.append("<tr onmouseover='ShowCustomerInfo(" + crs1.getString("insurenquiry_id") + ")' onmouseout='HideCustomerInfo(" + crs1.getString("insurenquiry_id") + ");'");
					Str.append(" style='height:120px'>\n");
					Str.append("<td align=center>").append(count).append("</td>");
					Str.append("<td align=center><a href=../insurance/insurance-enquiry-list.jsp?insurenquiry_id=").append(crs1.getString("insurenquiry_id")).append(">")
							.append(crs1.getString("insurenquiry_id"))
							.append("</a></td>\n");
					Str.append("<td valign=\"top\" align=\"left\" nowrap>");
					Str.append("<a href=\"../customer/customer-list.jsp?customer_id=");
					Str.append(crs1.getString("customer_id")).append("\">");
					Str.append(crs1.getString("customer_name")).append("</a>");
					Str.append("<br><a href=\"../customer/customer-contact-list.jsp?contact_id=");
					Str.append(crs1.getString("contact_id")).append("\">");
					Str.append(crs1.getString("contact_name")).append("</a>");
					if (!crs1.getString("contact_mobile1").equals("")) {
						Str.append("<br>").append(SplitPhoneNoSpan(crs1.getString("contact_mobile1"), 5, "M", crs1.getString("insurenquiry_id")))
								.append(ClickToCall(crs1.getString("contact_mobile1"), comp_id));
					}
					if (!crs1.getString("contact_mobile2").equals("")) {
						Str.append("<br>").append(SplitPhoneNoSpan(crs1.getString("contact_mobile2"), 5, "M", crs1.getString("insurenquiry_id")))
								.append(ClickToCall(crs1.getString("contact_mobile2"), comp_id));
					}
					if (!crs1.getString("contact_email1").equals("")) {
						Str.append("<br><span class='customer_info customer_" + crs1.getString("insurenquiry_id") + "'  style='display: none;'><a href=\"mailto:")
								.append(crs1.getString("contact_email1")).append("\">");
						Str.append(crs1.getString("contact_email1")).append("</a></span>");
					}
					if (!crs1.getString("contact_email2").equals("")) {
						Str.append("<br><span class='customer_info customer_" + crs1.getString("insurenquiry_id") + "'  style='display: none;'><a href=\"mailto:")
								.append(crs1.getString("contact_email2")).append("\">");
						Str.append(crs1.getString("contact_email2")).append("</a></span>");
					}
					Str.append("</td>");
					Str.append("\n<td valign=\"top\" align=\"left\" nowrap><a href=\"../insurance/insurance-enquiry-list.jsp?insurenquiry_id=");
					Str.append(crs1.getString("insurenquiry_id")).append("\">").append(SplitRegNo(crs1.getString("insurenquiry_reg_no"), 2)).append("</a></td>");
					Str.append("<td align=left>").append(crs1.getString("variant_name")).append("</td>");
					Str.append("<td align=left>").append(strToShortDate(crs1.getString("insurenquiry_sale_date"))).append("</td>");
					Str.append("<td align=left>").append(strToShortDate(crs1.getString("insurenquiry_renewal_date"))).append("</td>");
					// Str.append("<td align=left>").append(crs1.getString("insurfeedbacktype_name")).append("</td>");
					// Str.append("<td align=left>").append(crs1.getString("insurenquiryfollowup_desc")).append("</td>");
					Str.append("<td align=left><a href=../portal/executive-summary.jsp?emp_id=")
							.append(crs1.getString("insurenquiry_emp_id")).append(">")
							.append(crs1.getString("emp_name")).append("</a></td>");
					Str.append("<td align=center><a href=\"javascript:remote=window.open('../insurance/insurance-enquiry-dash.jsp?insurenquiry_id=")
							.append(crs1.getString("insurenquiry_id")).append("#tabs-2")
							.append("','insurdash','');remote.focus();\">")
							.append(strToLongDate(crs1.getString("insurenquiryfollowup_followup_time"))).append("</a></td>\n");
					Str.append("<td align=left>");
					Str.append(crs1.getString("insurenquiryfollowup_disp1") + "<b>");
					if (!crs1.getString("insurenquiryfollowup_disp2").equals("")) {
						Str.append(" =>");
					}
					Str.append("</b><br>");
					Str.append(crs1.getString("insurenquiryfollowup_disp2") + "<b>");
					if (!crs1.getString("insurenquiryfollowup_disp3").equals("")) {
						Str.append(" =>");
					}
					Str.append("</b><br>");
					Str.append(crs1.getString("insurenquiryfollowup_disp3") + "<b>");
					if (!crs1.getString("insurenquiryfollowup_disp4").equals("")) {
						Str.append(" =>");
					}
					Str.append("</b><br>");
					Str.append(crs1.getString("insurenquiryfollowup_disp4") + "<b>");
					if (!crs1.getString("insurenquiryfollowup_disp5").equals("")) {
						Str.append(" =>");
					}
					Str.append("</b><br>");
					Str.append(crs1.getString("insurenquiryfollowup_disp5")).append("</td>");

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
		// if (insurfollowup_date.equals("")) {
		// msg = msg + "<br>Select Date!";
		// } else {
		// if (!isValidDateFormatShort(insurfollowup_date)) {
		// msg = msg + "<br>InValid Date!";
		// }
		// }

		// if (insurfollowup_followuptype_id.equals("0")) {
		// msg += "<br>Select Follow-up Type!";
		// }

	}

	public String PopulateDisposition() {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT insurdisposition_id, insurdisposition_name"
					+ " FROM " + compdb(comp_id) + "axela_insurance_disposition"
					+ " WHERE 1=1 ";
			StrSql += " GROUP BY insurdisposition_id"
					+ " ORDER BY insurdisposition_rank";
			// SOP("StrSql===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("insurdisposition_id"));
				Str.append(StrSelectdrop(crs.getString("insurdisposition_id"), dr_feedbacktype_id));
				Str.append(">").append(crs.getString("insurdisposition_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error In " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateFollowuptype() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT insurfollowuptype_id, insurfollowuptype_name"
					+ " FROM " + compdb(comp_id) + "axela_insurance_enquiry_followup_type"
					+ " ORDER BY insurfollowuptype_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("insurfollowuptype_id"));
				Str.append(StrSelectdrop(crs.getString("insurfollowuptype_id"), insurfollowup_followuptype_id));
				Str.append(">").append(crs.getString("insurfollowuptype_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
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

	public String PopulateInsurFollowupFields(String comp_id, String parentid, int divid) {

		StringBuilder Str = new StringBuilder();
		String fieldvalue = "";
		divid++;
		try {

			StrSql = " SELECT insurdisposition_id, insurdisposition_name,"
					+ " insurdisposition_comments,"
					+ " insurdisposition_instructions,"
					+ " insurdisposition_nextfollowup "
					+ " FROM " + compdb(comp_id) + "axela_insurance_disposition"
					+ " WHERE 1 = 1"
					+ " AND insurdisposition_active = 1"
					+ " AND insurdisposition_parent_id = " + parentid
					+ " GROUP BY insurdisposition_id"
					+ " ORDER BY insurdisposition_rank";
			// SOP("StrSql===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				if (divid == 1) {
					Str.append("<div class='form-element1 form-element-padding text-right'></div>");
				} else {
					Str.append("<div class='form-element1 form-element-padding text-right'>&nbsp; </div>");
				}
				crs.beforeFirst();

				if (divid == 1) {
					Str.append("<div class='form-element2 form-element-padding text-left'>");
					Str.append("Contactable");
				} else {
					String StrSql1 = " SELECT insurdisposition_mandatory, insurdisposition_name"
							+ " FROM " + compdb(comp_id) + "axela_insurance_disposition"
							+ " WHERE 1 = 1"
							+ " AND insurdisposition_active = 1"
							+ " AND insurdisposition_id = " + parentid;
					CachedRowSet crs1 = processQuery(StrSql1);
					if (crs1.isBeforeFirst()) {
						while (crs1.next()) {
							Str.append("<div class='form-element10 form-element-padding text-left'>");
							Str.append(crs1.getString("insurdisposition_name"));
							Str.append(": ");
						}
						crs1.close();
					}

				}

				Str.append("<select name=dr_followup_disp").append(divid).append(" id=dr_followup_disp").append(divid);
				Str.append(" onchange='hidefields(this.id);populatefolllowupfields(this.id,\"disprow" + divid + "\");'>");
				Str.append("<option value=\"\">Select</option>\n");
				while (crs.next()) {
					Str.append("<option" + " value=\"").append(crs.getString("insurdisposition_id")).append("\" ");
					Str.append(StrSelectdrop(crs.getString("insurdisposition_id"), fieldvalue));
					Str.append(">").append(crs.getString("insurdisposition_name")).append("</option>\n");
				}
				Str.append("</select></div>");
				crs.close();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

}
