//sangita
package axela.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_Vehicle_Service_Due extends Connect {

	public String StrHTML = "";
	public String StrSearch = "";
	public String comp_id = "0";
	public String StrSql = "";
	public String branch_id = "0", empservice_id = "0", veh_branch_id = "0";
	public String dr_branch_id = "0";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String go = "";
	public String veh_kms = "", start_time = "";
	public String months = "", end_time = "";
	public String veh_emp_id = "";
	public String veh_id = "";
	public String msg = "";
	public Report_Check reportexe = new Report_Check();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				CheckPerm(comp_id, "emp_report_access, emp_mis_access, emp_service_vehicle_access, emp_service_insurance_access", request, response);
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				go = PadQuotes(request.getParameter("submit_button"));
				GetValues(request, response);
				CheckForm();
				if (go.equals("Go")) {
					StrSearch = ExeAccess.replace("emp_id", "s.emp_id");

					if (!veh_branch_id.equals("0")) {
						StrSearch += " AND veh_branch_id = " + veh_branch_id + "";
					}

					if (!empservice_id.equals("") && !empservice_id.equals("0")) {
						StrSearch += " AND veh_crmemp_id = " + empservice_id + "";
					}

					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						StrHTML = ServiceDetails();
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
		empservice_id = CNumeric(PadQuotes(request.getParameter("dr_emp_id")));
		veh_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch_id")));
		veh_kms = CNumeric(PadQuotes(request.getParameter("txt_veh_kms")));
		if (veh_kms.equals("0")) {
			veh_kms = "9100";
		}
		months = CNumeric(PadQuotes(request.getParameter("txt_months")));
		if (months.equals("0")) {
			months = "11";
		}

	}

	public String ServiceDetails() {
		try {
			int count = 0;
			StringBuilder Str = new StringBuilder();
			// String curryear = ToLongDate(kknow()).substring(0, 4);
			// String duedate = "CONCAT(DATE_FORMAT(DATE_SUB(concat('" + ToLongDate(kknow()).substring(0, 4) + "',substr(veh_sale_date, 5, 4),'000000'),INTERVAL 1 DAY),'%Y%m%d'),'000000')";
			StrSql = " select veh_id, variant_name,"
					+ " COALESCE(ser.emp_id, '') as veh_emp_id,"
					+ " COALESCE(concat(ser.emp_name,' (', ser.emp_ref_no, ')'), '') as vehemp_name, "
					+ " COALESCE(crm.emp_id, '') as veh_crmemp_id,"
					+ " COALESCE(concat(crm.emp_name,' (', crm.emp_ref_no, ')'), '') as crmemp_name, "
					// + " COALESCE(CONCAT(branch_id, ' (', branch_name, ')'), '') AS  branch_name,"
					+ " branch_id, branch_name,"
					+ " veh_id, veh_reg_no, customer_id, customer_name, contact_id, veh_iacs,"
					+ " CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname) AS contact_name,"
					+ " contact_mobile1, contact_mobile2, contact_email1, contact_email2,"
					+ " veh_chassis_no, veh_engine_no, veh_modelyear,"
					+ " COALESCE(veh_calservicedate, '') AS veh_calservicedate,"
					+ " COALESCE(veh_kms, 0) AS veh_kms, COALESCE(veh_cal_kms, 0) AS veh_cal_kms,"
					+ " COALESCE(veh_lastservice, '') AS veh_lastservice, COALESCE(veh_lastservice_kms, 0) AS veh_lastservice_kms,"
					+ " COALESCE(veh_service_duedate, '') AS duedate, veh_sale_date, COALESCE(veh_service_duekms, 0) AS duekms"
					+ " from " + compdb(comp_id) + "axela_service_veh"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = veh_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = veh_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN axelaauto.axela_preowned_variant ON variant_id = veh_variant_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = veh_branch_id"
					+ " INNER JOIN axelaauto.axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
					+ " left join " + compdb(comp_id) + "axela_emp ser on ser.emp_id = veh_emp_id"
					+ " left join " + compdb(comp_id) + "axela_emp crm on crm.emp_id = veh_crmemp_id"
					+ " where 1=1"
					// + " and substr(veh_service_duedate,5,4) >= substr('" + starttime + "', 5, 4)"
					// + " and substr(veh_service_duedate,5,4) <= substr('" + endtime + "', 5, 4)"
					+ " AND (DATE_FORMAT(DATE_ADD(veh_lastservice,INTERVAL " + months + " MONTH),'%Y%m%d%h%i%s') >= '" + ToLongDate(kknow()) + "'"
					+ " OR veh_cal_kms - veh_lastservice_kms >= " + veh_kms + ")"
					+ " " + StrSearch
					+ " GROUP BY veh_id"
					+ " order by substr(veh_service_duedate, 5, 4)"
					+ " LIMIT 1000";

			CachedRowSet crs1 = processQuery(StrSql, 0);
			if (crs1.isBeforeFirst()) {
				Str.append("<div class=\"table-responsive table-bordered\">\n");
				Str.append("<table class=\"table table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th data-toggle=\"true\" align=center>#</th>\n");
				Str.append("<th align=center>Vehicle ID</th>\n");
				Str.append("<th align=center>Customer</th>\n");
				Str.append("<th data-hide=\"phone\" align=center>Variant</th>\n");
				Str.append("<th data-hide=\"phone\" align=center>Reg. No.</th>\n");
				Str.append("<th data-hide=\"phone, tablet\" align=center>Chassis Number</th>\n");
				Str.append("<th data-hide=\"phone, tablet\" align=center>Engine No.</th>\n");
				Str.append("<th data-hide=\"phone, tablet\" align=center>Year</th>\n");
				Str.append("<th data-hide=\"phone, tablet\" align=center>Sale Date</th>\n");
				Str.append("<th data-hide=\"phone, tablet\" align=center>Last Service</th>\n");
				Str.append("<th data-hide=\"phone, tablet\" align=center>Kms</th>\n");
				Str.append("<th data-hide=\"phone, tablet\" align=center>Cal.Service</th>\n");
				Str.append("<th data-hide=\"phone, tablet\" align=center>Service Due</th>\n");
				Str.append("<th data-hide=\"phone, tablet\" align=center>Servive Executive</th>\n");
				Str.append("<th data-hide=\"phone, tablet\" align=center>CRM Executive</th>\n");
				Str.append("<th data-hide=\"phone, tablet\" align=center>Branch</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs1.next()) {
					count++;
					Str.append("<tr align=center valign=top>\n");
					Str.append("<td align=center>").append(count).append("</td>");
					Str.append("<td align=center><a href=vehicle-list.jsp?veh_id=").append(crs1.getString("veh_id")).append(">").append(crs1.getString("veh_id")).append("</a></td>\n");
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
					Str.append("<td align=left>").append(crs1.getString("variant_name")).append("</td>");
					Str.append("\n<td valign=\"top\" align=\"left\" nowrap><a href=\"../service/vehicle-list.jsp?veh_id=");
					Str.append(crs1.getString("veh_id")).append("\">").append(SplitRegNo(crs1.getString("veh_reg_no"), 2)).append("</a></td>");
					Str.append("<td align=\"center\" valign=\"top\">").append(crs1.getString("veh_chassis_no"));
					Str.append("</td>");
					Str.append("<td align=\"center\" valign=\"top\">").append(crs1.getString("veh_engine_no"));
					if (crs1.getString("veh_iacs").equals("1")) {
						Str.append("<br/><font color=\"red\"><b>IACS</b></font>");
					}
					Str.append("</td>");
					Str.append("<td align=\"right\" valign=\"top\">").append(crs1.getString("veh_modelyear"));
					Str.append("</td>");
					Str.append("<td align=center>").append(strToShortDate(crs1.getString("veh_sale_date"))).append("</td>");
					Str.append("<td align=center>").append(strToShortDate(crs1.getString("veh_lastservice")));
					if (!crs1.getString("veh_lastservice_kms").equals("0")) {
						Str.append("<br>").append(IndFormat(crs1.getString("veh_lastservice_kms"))).append(" Kms");
					} else {
						Str.append("");
					}
					Str.append("</td>");
					Str.append("<td align=right>").append(IndFormat(crs1.getString("veh_kms"))).append("</td>");
					Str.append("<td align=center>").append(strToShortDate(crs1.getString("veh_calservicedate")));
					Str.append("<br>").append(IndFormat(crs1.getString("veh_cal_kms"))).append(" Kms").append("</td>");
					Str.append("<td align=center>").append(strToShortDate(crs1.getString("duedate")));
					if (!crs1.getString("duekms").equals("0")) {
						Str.append("<br>").append(IndFormat(crs1.getString("duekms"))).append(" Kms");
					} else {
						Str.append("");
					}
					Str.append("</td>");
					Str.append("<td align=left><a href=../portal/executive-summary.jsp?emp_id=").append(crs1.getString("veh_emp_id")).append(">")
							.append(crs1.getString("vehemp_name")).append("</a></td>");
					Str.append("<td align=left><a href=../portal/executive-summary.jsp?emp_id=").append(crs1.getString("veh_crmemp_id")).append(">")
							.append(crs1.getString("crmemp_name")).append("</a></td>");
					Str.append("<td align=left><a href=../portal/branch-summary.jsp?branch_id=").append(crs1.getString("branch_id")).append(">")
							.append(crs1.getString("branch_name")).append("</a></td>");
					Str.append("</tr>");
				}
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
			} else {
				Str.append("<font color=red><b>No Service Due found!</b></font>");
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
		if (veh_branch_id.equals("0")) {
			msg = msg + "<br>Select Branch!";
		}
		// if (veh_kms.equals("0")) {
		// msg += "<br>Select Start Date!";
		// }

		// if (months.equals("")) {
		// msg += "<br>Select End Date!<br>";
		// }

	}

	public String PopulaServiceExecutives(String empservice_id, String comp_id, String ExeAccess) {
		StringBuilder Str = new StringBuilder();
		try {

			StrSql = "SELECT emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_active = 1"
					+ " AND emp_service = 1"
					+ ExeAccess + ""
					+ " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<select name=\"dr_emp_id\" id=\"dr_emp_id\" class=\"form-control\">\n");
			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id"));
				Str.append(Selectdrop(crs.getInt("emp_id"), empservice_id));
				Str.append(">").append(crs.getString("emp_name")).append("</option>\n");
			}
			Str.append("</select>\n");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateBranch() {
		StringBuilder Str = new StringBuilder();
		try {

			StrSql = "SELECT branch_id, branch_name"
					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " WHERE branch_active = 1"
					+ " AND branch_branchtype_id IN (3)"
					+ " GROUP BY branch_id"
					+ " ORDER BY branch_brand_id, branch_branchtype_id, branch_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			// Str.append("<select name=\"dr_branch_id\" id=\"dr_branch_id\" class=\"selectbox\" onChange=\"ExeCheck();\">\n");
			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("branch_id"));
				Str.append(Selectdrop(crs.getInt("branch_id"), veh_branch_id));
				Str.append(">").append(crs.getString("branch_name")).append("</option>\n");
			}
			Str.append("</select>\n");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

}
