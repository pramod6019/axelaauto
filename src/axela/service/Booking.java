package axela.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

/**
 *
 * @author Satish 25th march 2013
 */
public class Booking extends Connect {

	public String msg = "";
	public String starttime = "";
	public String start_time = "";
	public String endtime = "";
	public String end_time = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String StrHTML = "";
	public String StrSearch = "";
	public String smart = "";
	public String StrSql = "";
	public String branch_id = "0";
	public String dr_branch_id = "0";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String RefreshForm = "";
	public String ExportPerm = "";
	public String EnableSearch = "1";
	public String reportURL = "";
	public String ListLink = "<a href=booking-list.jsp?smart=yes>Click here to List Bookings</a>";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {

				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				CheckPerm(comp_id, "emp_service_vehicle_access", request, response);
				ExportPerm = ReturnPerm(comp_id, "emp_export_access", request);
				smart = PadQuotes(request.getParameter("smart"));

				if (!smart.equals("yes")) {
					GetValues(request, response);
					CheckForm();
					if (!starttime.equals("")) {
						StrSearch = " AND SUBSTR(booking_time, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)";
					}

					if (!endtime.equals("")) {
						StrSearch += " AND SUBSTR(booking_time, 1, 8) <= SUBSTR('" + endtime + "', 1, 8)";
					}

					if (!dr_branch_id.equals("0")) {
						StrSearch += " AND customer_branch_id = " + dr_branch_id + "";
					}

					StrSearch += " AND branch_active = '1' AND booking_active = '1'";
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}

					if (msg.equals("")) {
						SetSession("apptstrsql", StrSearch, request);
						StrHTML = ApptSummary(request);
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

		if (branch_id.equals("0")) {
			dr_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch_id")));
		} else {
			dr_branch_id = branch_id;
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
			msg += "<br>Select End Date!";
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

	public String ApptSummary(HttpServletRequest request) {
		int booking_count = 0;
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT COALESCE(branch_id, 0) AS branch_id,"
					+ " COALESCE(CONCAT(branch_name, ' (', branch_code, ')'), '') AS branchname,"
					+ " COUNT(booking_id) AS bookingcount"
					+ " FROM " + compdb(comp_id) + "axela_service_booking"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = booking_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = contact_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = customer_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = booking_veh_id"
					+ " WHERE 1 = 1" + StrSearch + ""
					+ ExeAccess.replace("emp_id", "booking_service_emp_id") + BranchAccess + ""
					+ " GROUP By branch_id"
					+ " ORDER BY branchname";
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				Str.append("<div class=\"portlet box  \"><div class=\"portlet-title\" style=\"text-align: center\">");
				Str.append("<div class=\"caption\" style=\"float: none\">Service Booking Summary</div></div>");
				Str.append("<div class=\"portlet-body portlet-empty\"><table class=\"table table-bordered table-hover\">");
				Str.append("<tr>\n");
				Str.append("<th data-toggle=\"true\">Branch</th>\n");
				Str.append("<th width=20% data-hide=\"phone, tablet\">Booking Count</th>\n");
				Str.append("</tr>\n");
				while (crs.next()) {
					booking_count += crs.getInt("bookingcount");
					Str.append("<tr>\n");
					Str.append("<td valign=\"top\" align=\"left\"><a href=\"../portal/branch-summary.jsp?branch_id=");
					Str.append(crs.getInt("branch_id")).append("\">").append(crs.getString("branchname")).append("</a></td>\n");
					Str.append("<td valign=\"top\" align=\"right\">").append(crs.getString("bookingcount")).append("</td>\n");
					Str.append("</tr>\n");
				}
				Str.append("<tr>\n");
				Str.append("<td align=\"right\"><b>Total: </b></td>\n");
				Str.append("<td align=\"right\"><b>").append(booking_count).append("</b></td>\n");
				Str.append("</tr>\n</table></div></div>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
}
