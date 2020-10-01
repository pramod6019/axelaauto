// Dilip Kumar P
package axela.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Advisor_Cal extends Connect {

	public String add = "", StrHTML = "", emp_id = "0";
	public String submitB = "";
	public String ExeAccess = "";
	public String comp_id = "0";
	public String BranchAccess = "";
	public String StrSql = "";
	public static String msg = "";
	public String[] item_model_ids, booking_service_emp_ids;
	public String booking_time_from = strToShortDate(ToShortDate(kknow())), starttime;
	public String booking_time_to = strToShortDate(ToShortDate(kknow())), endtime;
	public String branch_id = "0";
	public String booking_branch_id = "0";
	public String booking_service_emp_id = "0", item_model_id = "0";
	public int colspan = 0;
	public String QueryString = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);

			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_service_jobcard_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				ExeAccess = GetSession("ExeAccess", request);
				BranchAccess = GetSession("BranchAccess", request);
				branch_id = CNumeric(GetSession("emp_branch_id", request));

				if (branch_id.equals("0")) {
					booking_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
				} else {
					booking_branch_id = branch_id;
				}
				submitB = PadQuotes(request.getParameter("submit_button"));
				if (submitB.equals("Submit")) {
					GetValues(request, response);
					CheckForm();
					if (msg.equals("")) {
						StrHTML = AdvisorCalendar(request);
					} else {
						msg = "Error!" + msg;
					}
				}
			}

		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		item_model_id = RetrunSelectArrVal(request, "dr_model");
		item_model_ids = request.getParameterValues("dr_model");
		booking_service_emp_id = RetrunSelectArrVal(request, "dr_executive");
		booking_service_emp_ids = request.getParameterValues("dr_executive");
		booking_time_from = PadQuotes(request.getParameter("txt_booking_time_from"));
		booking_time_to = PadQuotes(request.getParameter("txt_booking_time_to"));
		if (booking_time_from.equals("")) {
			booking_time_from = strToShortDate(ToShortDate(kknow()));
		}
		if (booking_time_to.equals("")) {
			booking_time_to = strToShortDate(ToShortDate(kknow()));
		}
	}

	protected void CheckForm() {
		msg = "";
		if (booking_branch_id.equals("0")) {
			msg = "<br>Select Branch!";
		}
		if (booking_time_from.equals("")) {
			msg = msg + "<br>Select Start Date!";
		}
		if (!booking_time_from.equals("") && !isValidDateFormatShort(booking_time_from)) {
			msg = msg + "<br>Enter valid Start date!";
		}
		if (booking_time_to.equals("")) {
			msg = msg + "<br>Select End Date!";
		}
		if (!booking_time_to.equals("") && !isValidDateFormatShort(booking_time_to)) {
			msg = msg + "<br>Enter valid End date!";
		}
		if (!booking_time_from.equals("")
				&& isValidDateFormatShort(booking_time_from)
				&& !booking_time_to.equals("")
				&& isValidDateFormatShort(booking_time_to)) {
			starttime = ConvertShortDateToStr(booking_time_from);
			endtime = ConvertShortDateToStr(booking_time_to);
			if (Long.parseLong(starttime) > Long.parseLong(endtime)) {
				msg = msg + "<br>Start Date should be less than End date!";
			}
		}
		if (getDaysBetween(ConvertShortDateToStr(booking_time_from), ConvertShortDateToStr(booking_time_to)) > 31) {
			msg = msg + "<br>Leave Duration should be less than 1 month!";
		}
	}

	public String AdvisorCalendar(HttpServletRequest request) {
		StringBuilder Str = new StringBuilder();
		Date date;
		String today;
		String StrSearch = "", search = "";

		search = BranchAccess;
		if (!item_model_id.equals("")) {
			search += " AND item_model_id IN (" + item_model_id + ")";
		}
		if (!booking_service_emp_id.equals("")) {
			search += " AND booking_service_emp_id IN (" + booking_service_emp_id + ")";
		}
		if (!booking_branch_id.equals("") && !booking_branch_id.equals("0")) {
			search += " AND booking_branch_id = " + booking_branch_id + "";
		}
		try {
			Str.append("<div class=\"table-responsive\">\n");
			Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
			Str.append("<thead><tr>\n");
			Str.append("<th height=20><b>Days</b></th>\n");
			Str.append("<th><b>Booking Details</b></th>\n");
			Str.append("</tr>");
			Str.append("</thead>");
			Str.append("<tbody>");
			double days = getDaysBetween(starttime, endtime);
			for (int i = 0; i <= days; i++) {
				date = AddHoursDate(new SimpleDateFormat("dd/MM/yyyy").parse(FormatDate(starttime, "dd/MM/yyyy")), Double.parseDouble(i + ""), 0.0, 0.0);
				StrSearch = " AND booking_time >= " + ToLongDate(date) + "";
				if (isSunday(ToLongDate(date))) {
					Str.append("<tr align=left bgcolor=pink>\n");
				} else {
					Str.append("<tr align=left>\n");
				}
				today = ConvertLongDate(ToLongDate(date));
				date = AddHoursDate(new SimpleDateFormat("dd/MM/yyyy").parse(FormatDate(starttime, "dd/MM/yyyy")), Double.parseDouble(i + 1 + ""), 0.0, 0.0);
				StrSearch = StrSearch + " AND booking_time < " + ToLongDate(date) + "";
				StrSql = "SELECT booking_id, branch_code, booking_time, veh_id, veh_reg_no, item_name,"
						+ " emp_id, CONCAT(emp_name,' (', emp_ref_no, ')') AS emp_name"
						+ " FROM " + compdb(comp_id) + "axela_service_booking"
						+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = booking_veh_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = veh_variant_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = booking_branch_id "
						+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = booking_service_emp_id"
						+ " WHERE 1 = 1";
				StrSql = StrSql + search + StrSearch;
				StrSql = StrSql + " GROUP BY booking_id ORDER BY booking_time";
				// SOP("AdvisorCalendar======" + StrSqlBreaker(StrSql));
				CachedRowSet crs = processQuery(StrSql, 0);
				Str.append("<td valign=top width=15%>").append(today).append("</td>\n");
				if (crs.isBeforeFirst()) {
					Str.append("<td valign=top><table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">"
							+ "<tr>");
					while (crs.next()) {
						Str.append("<td valign=top>").append("<a href=../service/booking-list.jsp?booking_id=").append(crs.getInt("booking_id")).append(">")
								.append(FormatDate(crs.getString("booking_time"), "HH:mm:ss")).append("</a>");
						Str.append("<br><a href=../service/vehicle-list.jsp?veh_id=").append(crs.getInt("veh_id")).append(">").append(SplitRegNo(crs.getString("veh_reg_no"), 2)).append("</a>");
						Str.append("<br>").append(crs.getString("item_name"));
						Str.append("<br><a href=../portal/executive-summary.jsp?emp_id=").append(crs.getInt("emp_id")).append(">").append(crs.getString("emp_name")).append("</a>");
						Str.append("<br></td>");
					}
					Str.append("</tr></table></td>");
				} else {
					Str.append("<td valign=top align=center></td></tr>");
				}
				crs.close();
			}
			Str.append("</tbody>");
			Str.append("</table>");
			Str.append("</div>");
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateExecutive(String branch_id, String comp_id) {
		try {
			StringBuilder Str = new StringBuilder();
			StrSql = "SELECT emp_id, CONCAT(emp_name,' (', emp_ref_no, ')') AS emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE 1 = 1"
					+ " AND emp_active = '1'"
					+ " AND (emp_branch_id = 0 OR emp_branch_id = " + branch_id + ")"
					+ " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			// SOP("PopulateExecutive======" + StrSqlBreaker(StrSql));
			Str.append("<select name=dr_executive id=dr_executive class='form-control multiselect-dropdown' multiple=\"multiple\" size=10 style=\"width:250px\">");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id")).append("");
				Str.append(ArrSelectdrop(crs.getInt("emp_id"), booking_service_emp_ids));
				Str.append(">").append(crs.getString("emp_name")).append("</option> \n");
			}
			Str.append("</select>");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateModel() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT model_id, model_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item_model"
					+ " ORDER BY model_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("model_id")).append("");
				Str.append(ArrSelectdrop(crs.getInt("model_id"), item_model_ids));
				Str.append(">").append(crs.getString("model_name")).append("</option>\n");
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
