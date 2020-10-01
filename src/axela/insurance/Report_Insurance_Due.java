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

public class Report_Insurance_Due extends Connect {

	public String StrHTML = "";
	public String StrSearch = "";
	public String comp_id = "0";
	public String StrSql = "";
	public String branch_id = "0", exe_id = "0";
	public String dr_branch_id = "0";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String go = "";
	public String date_type = "0";
	public String starttime = "", start_time = "";
	public String endtime = "", end_time = "";
	public String veh_emp_id = "";
	public String insurenquiry_id = "";
	// public String pending_followup = "";
	public String msg = "";
	public Report_Check reportexe = new Report_Check();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_report_access, emp_mis_access, emp_insurance_enquiry_access", request, response);
			if (!comp_id.equals("0")) {
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				go = PadQuotes(request.getParameter("submit_button"));
				GetValues(request, response);
				CheckForm();
				if (go.equals("Go")) {
					// StrSearch = ExeAccess.replace("emp_id", "emp_id");

					if (!exe_id.equals("") && !exe_id.equals("0")) {
						StrSearch += " AND insurenquiry_emp_id = " + exe_id + "";
					}

					if (date_type.equals("1")) {
						StrSearch += " AND SUBSTR(insurenquiry_sale_date, 5, 4) >= SUBSTR('" + starttime + "',5, 4) ";
						StrSearch += " AND SUBSTR(insurenquiry_sale_date, 5, 4) <= SUBSTR('" + endtime + "',5, 4) ";
					}
					if (date_type.equals("2")) {
						StrSearch += " AND SUBSTR(insurenquiry_renewal_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8) ";
						StrSearch += " AND SUBSTR(insurenquiry_renewal_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8) ";
					}

					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						StrHTML = FollowupDetails();
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
		exe_id = PadQuotes(request.getParameter("dr_emp_id"));

		date_type = CNumeric(PadQuotes(request.getParameter("dr_date_type")));
		starttime = PadQuotes(request.getParameter("txt_starttime"));
		endtime = PadQuotes(request.getParameter("txt_endtime"));
		if (starttime.equals("")) {
			starttime = ReportStartdate();
		}
		if (endtime.equals("")) {
			endtime = strToShortDate(ToShortDate(kknow()));
		}
		// pending_followup =
		// PadQuotes(request.getParameter("chk_pending_followup"));
		// if (pending_followup.equals("on")) {
		// pending_followup = "1";
		// } else {
		// pending_followup = "0";
		// }
	}

	public String FollowupDetails() {
		try {
			int count = 0;
			StringBuilder Str = new StringBuilder();

			StrSql = " SELECT"
					+ " insurenquiry_id,"
					+ " COALESCE (variant_name, '') AS variant_name,"
					+ " COALESCE (insurfollowup_followup_time,'') AS insurfollowup_followup_time,"
					+ " COALESCE (insurfollowup_desc, '') AS insurfollowup_desc,"
					+ " COALESCE (emp_id, '') AS insurfollowup_emp_id,"
					+ " COALESCE (CONCAT(emp_name,' (',	emp_ref_no,')'),'') AS insurfollowup_emp_name,"
					+ " insurenquiry_reg_no,"
					+ " insurfollowup_id,"
					+ " customer_id,"
					+ " customer_name,"
					+ " contact_id,"
					+ " CONCAT(title_desc,' ',contact_fname,' ',contact_lname) AS contact_name,"
					+ " contact_mobile1,"
					+ " contact_mobile2,"
					+ " contact_email1,"
					+ " contact_email2,"
					+ " insurenquiry_sale_date,"
					+ " insurenquiry_renewal_date,"
					+ " COALESCE (insurpolicy_id, '0') AS insurpolicy_id,"
					+ " COALESCE (insurpolicy_start_date, '') AS insurpolicy_start_date,"
					+ " COALESCE (insurpolicy_end_date, '') AS insurpolicy_end_date"
					+ " FROM " + compdb(comp_id) + "axela_insurance_enquiry"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = insurenquiry_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = insurenquiry_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " LEFT JOIN axelaauto.axela_preowned_variant ON variant_id = insurenquiry_variant_id"
					+ " LEFT JOIN axelaauto.axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_insurance_policy ON insurpolicy_insurenquiry_id = insurenquiry_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_insurance_followup ON insurfollowup_insurenquiry_id = insurenquiry_id"
					+ " AND insurfollowup_desc = ''"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp  ON emp_id = insurenquiry_emp_id"
					+ " WHERE 1 = 1"
					+ " " + StrSearch
					+ " GROUP BY insurenquiry_id"
					+ " ORDER BY insurenquiry_sale_date"
					+ " LIMIT 2000";

			SOP("StrSql======FollowupDetails===" + StrSqlBreaker(StrSql));
			CachedRowSet crs1 = processQuery(StrSql, 0);
			if (crs1.isBeforeFirst()) {
				Str.append("<div class=\"table-responsive table-bordered\">\n");
				Str.append("<table class=\"table table-responsive table-hover\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr align=center>\n");
				Str.append("<th data-hide=\"phone\" align=center>#</th>\n");
				Str.append("<th data-toggle=\"true\" align=center>Insurance Enquiry ID</th>\n");
				Str.append("<th align=center>Customer</th>\n");
				Str.append("<th data-hide=\"phone, tablet\" align=center>Reg. No.</th>\n");
				Str.append("<th data-hide=\"phone, tablet\" align=center>Variant</th>\n");
				Str.append("<th data-hide=\"phone, tablet\" align=center>Executive</th>\n");
				Str.append("<th data-hide=\"phone, tablet\" align=center>Sale Date</th>\n");
				Str.append("<th data-hide=\"phone, tablet\" align=center>Renewal Date</th>\n");
				Str.append("<th data-hide=\"phone, tablet\" align=center>Next Follow-up Time</th>\n");
				Str.append("<th data-hide=\"phone, tablet\" align=center>Insurance</th>\n");
				Str.append("<th data-hide=\"phone, tablet\" align=center>Description</th>\n");
				Str.append("</tr>");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs1.next()) {
					count++;
					Str.append("<tr align=center valign=top>\n");
					Str.append("<td align=center>").append(count).append("</td>");
					Str.append("<td align=center><a href=../insurance/insurance-enquiry-list.jsp?insurenquiry_id=").append(crs1.getString("insurenquiry_id")).append(">")
							.append(crs1.getString("insurenquiry_id")).append("</a></td>\n");
					Str.append("<td valign=\"top\" align=\"left\" nowrap>");
					Str.append("<a href=\"../customer/customer-list.jsp?customer_id=");
					Str.append(crs1.getString("customer_id")).append("\">");
					Str.append(crs1.getString("customer_name")).append("</a>");
					Str.append("<br><a href=\"../customer/customer-contact-list.jsp?contact_id=");
					Str.append(crs1.getString("contact_id")).append("\">");
					Str.append(crs1.getString("contact_name")).append("</a>");
					if (!crs1.getString("contact_mobile1").equals("")) {
						Str.append("<br>").append(SplitPhoneNo(crs1.getString("contact_mobile1"), 5, "M")).append(ClickToCall(crs1.getString("contact_mobile1"), comp_id));
					}

					if (!crs1.getString("contact_mobile2").equals("")) {
						Str.append("<br>").append(SplitPhoneNo(crs1.getString("contact_mobile2"), 5, "M")).append(ClickToCall(crs1.getString("contact_mobile2"), comp_id));
					}

					if (!crs1.getString("contact_email1").equals("")) {
						Str.append("<br><a href=mailto:").append(crs1.getString("contact_email1")).append(">").append(crs1.getString("contact_email1")).append("</a>");
					}

					if (!crs1.getString("contact_email2").equals("")) {
						Str.append("<br><a href=mailto:").append(crs1.getString("contact_email2")).append(">").append(crs1.getString("contact_email2")).append("</a>");
					}
					Str.append("</td>");
					Str.append("\n<td valign=\"top\" align=\"left\" nowrap><a href=\"../insurance/insurance-enquiry-list.jsp?insurenquiry_id=");
					Str.append(crs1.getString("insurenquiry_id")).append("\">").append(SplitRegNo(crs1.getString("insurenquiry_reg_no"), 2)).append("</a></td>");
					Str.append("<td align=left>");
					if (!crs1.getString("variant_name").equals(""))
					{
						Str.append(crs1.getString("variant_name"));
					}
					Str.append("</td>");
					Str.append("<td align=left><a href=../portal/executive-summary.jsp?emp_id=").append(crs1.getString("insurfollowup_emp_id")).append(">")
							.append(crs1.getString("insurfollowup_emp_name")).append("</a></td>");
					Str.append("<td align=center>").append(strToShortDate(crs1.getString("insurenquiry_sale_date"))).append("</td>");
					Str.append("<td align=center>").append(strToShortDate(crs1.getString("insurenquiry_renewal_date"))).append("</td>");

					Str.append("<td align=center><a href=\"javascript:remote=window.open('insurance-enquiry-dash.jsp?insurenquiry_id=").append(crs1.getString("insurenquiry_id")).append("#tabs-2>")
							.append("','insurdash','');remote.focus();\">").append(strToLongDate(crs1.getString("insurfollowup_followup_time"))).append("</a></td>\n");
					Str.append("<td align=center>");
					if (!crs1.getString("insurpolicy_start_date").equals("")) {
						Str.append("<a href=../insurance/insurance-list.jsp?insurpolicy_id=").append(crs1.getString("insurpolicy_id")).append(">")
								.append(strToShortDate(crs1.getString("insurpolicy_start_date")))
								.append("<br>-<br>").append(strToShortDate(crs1.getString("insurpolicy_end_date"))).append("</a>");
					} else {
						Str.append("-");
					}
					Str.append("</td>");
					Str.append("<td align=left>").append(crs1.getString("insurfollowup_desc")).append("</td>");
					Str.append("</tr>");
				}
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
			} else {
				Str.append("<font color=red><b>No Insurance Due found!</b></font>");
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
			} else {
				msg += "<br>Enter Valid End Date!";
				endtime = "";
			}
		}

	}

	public String PopulateDate() {

		StringBuilder Str = new StringBuilder();
		// Str.append("<option value=0>Select</option>");
		Str.append("<option value=1").append(StrSelectdrop("1", date_type)).append(">Sale Date</option>\n");
		Str.append("<option value=2").append(StrSelectdrop("2", date_type)).append(">Renewal Date</option>\n");
		return Str.toString();
	}
}
