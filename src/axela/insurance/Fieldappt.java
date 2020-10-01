package axela.insurance;
//Dilip Kumar 13th APR 2013

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Fieldappt extends Connect {

	public String msg = "";
	public String starttime = "";
	public String start_time = "";
	public String endtime = "";
	public String end_time = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String StrHTML = "";
	public String StrSql = "";
	public String branch_id = "0";
	public String dr_emp_id = "0";
	public String BranchAccess = "";
	public String StrSearch = "";
	public String smart = "";
	public String ExportPerm = "";
	public String EnableSearch = "1";
	public String ListLink = "<a href=fieldappt-list.jsp?smart=yes>Click here to List Field Appointment</a>";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_field_appointment_access", request, response);
			if (!comp_id.equals("0")) {
				BranchAccess = GetSession("BranchAccess", request);
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				emp_id = CNumeric(GetSession("emp_id", request));
				// ExportPerm = ReturnPerm(comp_id, "emp_export_access", request);
				smart = PadQuotes(request.getParameter("smart"));
				// String str = "9690960778111";
				// SOP("=="+str.substring(0, 10));

				if (!smart.equals("yes")) {
					GetValues(request, response);
					CheckForm();

					if (!starttime.equals("")) {
						StrSearch = " AND substr(fieldappt_appttime,1,8) >= SUBSTR('" + starttime + "',1,8) ";
					}
					if (!endtime.equals("")) {
						StrSearch = StrSearch + " AND substr(fieldappt_appttime,1,8) <= SUBSTR('" + endtime + "',1,8) ";
					}
					if (!dr_emp_id.equals("0")) {
						StrSearch = StrSearch + " and fieldappt_emp_id=" + dr_emp_id;
					}
					StrSearch = StrSearch + " AND emp_active = '1' ";

					if (!msg.equals("")) {
						msg = "<br>Error!" + msg;
					}
					if (msg.equals("")) {
						// SOP("StrSearch===" + StrSearch);
						SetSession("fieldaptstrsql", StrSearch, request);
						StrHTML = FieldApptSummary(request);
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
		starttime = PadQuotes(request.getParameter("txt_starttime"));
		endtime = PadQuotes(request.getParameter("txt_endtime"));
		if (starttime.equals("")) {
			starttime = ReportStartdate();
		}
		if (endtime.equals("")) {
			endtime = strToShortDate(ToShortDate(kknow()));
		}
		dr_emp_id = PadQuotes(request.getParameter("dr_emp_id"));
		// SOP("dr_emp_id==" + dr_emp_id);
		if (dr_emp_id.equals("")) {
			dr_emp_id = emp_id;
		}
		// SOP("dr_emp_id==" + dr_emp_id);
	}

	protected void CheckForm() {
		msg = "";
		if (starttime.equals("")) {
			msg = msg + "<br>Select Start Date!";
		} else {
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
		} else {
			if (isValidDateFormatShort(endtime)) {
				endtime = ConvertShortDateToStr(endtime);
				end_time = strToShortDate(endtime);
			} else {
				msg = msg + "<br>Enter Valid End Date!";
				endtime = "";
			}
		}

		if (!starttime.equals("") && !endtime.equals("") && isValidDateFormatShort(strToShortDate(starttime)) && isValidDateFormatShort(strToShortDate(endtime))) {
			if (Long.parseLong(starttime) > Long.parseLong(endtime)) {
				msg = msg + "<br>Start Date should be less than End date!";
			}
		}
	}

	public String FieldApptSummary(HttpServletRequest request) {
		StringBuilder Str = new StringBuilder();
		int fieldapptinsurenquirycount = 0;

		try {
			StrSql = "SELECT "
					+ " emp_id, "
					+ " CONCAT(emp_name,' (',emp_ref_no,')') AS empname, "
					+ " COUNT(fieldappt_insurenquiry_id) AS fieldapptinsurenquirycount "
					+ " FROM " + compdb(comp_id) + "axela_insurance_fieldappt "
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = fieldappt_emp_id "
					+ " WHERE	1 = 1 "
					+ StrSearch
					+ BranchAccess
					+ " GROUP BY emp_id "
					+ " ORDER BY empname ";

			// SOP("StrSql====" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				Str.append("<div class=\"portlet box  \"><div class=\"portlet-title\" style=\"text-align: center\">");
				Str.append("<div class=\"caption\" style=\"float: none\">Field Appointment Summary</div></div>");
				Str.append("<div class=\"portlet-body portlet-empty\"><table class=\"table table-bordered table-hover\">");
				Str.append("<tr>\n");
				Str.append("<th style=\"width:50%; text-align:center\">Executive &nbsp;&nbsp;&nbsp;</th>\n");
				Str.append("<th style=\"width:50%; text-align:center\">Appointment Count</th>\n");
				Str.append("</tr>\n");
				while (crs.next()) {
					fieldapptinsurenquirycount = fieldapptinsurenquirycount + crs.getInt("fieldapptinsurenquirycount");
					Str.append("<tr>\n");
					Str.append("<td align=left><a href=\"../portal/executive-summary.jsp?emp_id=")
							.append(crs.getInt("emp_id")).append("\">")
							.append(crs.getString("empname"))
							.append("</a>&nbsp;&nbsp;&nbsp;</td>\n");
					Str.append("<td align=right>").append(crs.getString("fieldapptinsurenquirycount")).append("</td>");
					Str.append("</tr>");
				}
				Str.append("<tr>\n");
				Str.append("<td align=left><b>Total: &nbsp;&nbsp;&nbsp;</b></td>\n");
				Str.append("<td align=right><b>").append(fieldapptinsurenquirycount).append("</b></td>\n");
				Str.append("</tr></table></div></div>");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateFieldExecutive(String comp_id, HttpServletRequest request) {
		StringBuilder Str = new StringBuilder();
		emp_id = CNumeric(GetSession("emp_id", request));
		try {
			StrSql = " SELECT emp_id, CONCAT(emp_name,' (',emp_ref_no,')') AS emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp "
					+ " WHERE 1=1"
					+ " AND emp_fieldinsur = 1"
					+ " AND emp_active = 1";
			StrSql += " GROUP BY emp_id "
					+ " ORDER BY emp_name ";
			// ////SOP("PopulateInsurExecutive-==" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select Executive</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id")).append("");
				Str.append(Selectdrop(crs.getInt("emp_id"), dr_emp_id));
				Str.append(">").append(crs.getString("emp_name")).append("</option> \n");
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
