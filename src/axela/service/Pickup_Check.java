/*Smitha nag 5th April - 9 april  2013*/
package axela.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Pickup_Check extends Connect {

	public String driver_id = "";
	public String multiple = "";
	public String branch_id = "", location_id = "";
	public String pickup = "";
	public String pickupdate = "";
	public String pickup_location = "";
	public String starttime = "";
	public String endtime = "";
	public String msg = "";
	public String emp_id = "";
	public String comp_id = "0";
	public String StrSql = "";
	public String StrHTML = "";
	public String StrSearch = "";

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		comp_id = CNumeric(GetSession("comp_id", request));
		if (!comp_id.equals("0")) {
			emp_id = CNumeric(GetSession("emp_id", request));
			branch_id = CNumeric(PadQuotes(request.getParameter("branch_id")));
			driver_id = CNumeric(PadQuotes(request.getParameter("driver_id")));
			location_id = CNumeric(PadQuotes(request.getParameter("location_id")));
			pickup = PadQuotes(request.getParameter("pickup"));
			pickupdate = PadQuotes(request.getParameter("pickupdate"));
			pickup_location = PadQuotes(request.getParameter("pickup_location"));
			multiple = PadQuotes(request.getParameter("multiple"));
			if (!pickupdate.equals("") && isValidDateFormatLong(pickupdate)) {
				starttime = ConvertLongDateToStr(pickupdate);
				endtime = ToLongDate(AddHoursDate(StringToDate(starttime), 1, 0, 0));
			}
			pickupdate = strToShortDate(ConvertShortDateToStr(pickupdate));
			if (!pickup.equals("yes")) {
				StrHTML = PopulateDriver();
			}
			if (pickup.equals("yes")) {
				StrHTML = PickUpCalendar();
			}
			if (!branch_id.equals("0") && multiple.equals("yes")) {
				StrHTML = PopulateDriver();
			}
			if (pickup_location.equals("yes")) {
				StrHTML = new Call_Update().PopulatePickUpLocation(branch_id, comp_id);
			}
		}
	}

	public String PopulateDriver() {

		StringBuilder Str = new StringBuilder();
		try {
			if (multiple.equals("yes")) {
				Str.append("<select name=dr_pickup_emp_id id=dr_pickup_emp_id class=form-control multiple=\"multiple\" size=10 style=\"width:250px\">");
			} else {
				Str.append("<select name=dr_pickup_emp_id id=dr_pickup_emp_id class=form-control onChange=\"PickupCheck()\" >");
				Str.append("<option value = 0>Select Driver</option>");
			}
			if (!branch_id.equals("") && !branch_id.equals("0")) {
				StrSearch = " AND emp_branch_id = " + branch_id + "";
			}
			if (!branch_id.equals("") && !branch_id.equals("0") && !pickup.equals("yes")) {
				StrSql = "SELECT emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name"
						+ " FROM " + compdb(comp_id) + "axela_emp"
						+ " WHERE emp_pickup_driver = 1"
						+ " AND emp_active = 1";
				if (!multiple.equals("yes")) {
					StrSql = StrSql + " AND emp_branch_id != 0 ";
				}
				StrSql = StrSql + StrSearch
						+ " GROUP BY emp_id"
						+ " ORDER BY emp_name";
				// SOP("StrSql = " + StrSql);
				CachedRowSet crs = processQuery(StrSql, 0);
				while (crs.next()) {
					Str.append("<option value=").append(crs.getString("emp_id")).append(">").append(crs.getString("emp_name")).append("</option> \n");
				}
				crs.close();
			}
			Str.append("</select>");
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	protected String PickUpCalendar() {
		StringBuilder Str = new StringBuilder();
		StrSearch = "";
		if (pickupdate.equals("")) {
			Str.append("<br><br><font color=\"red\"><b>Select Booking Time!</b></font>");
			return Str.toString();
		}

		if (branch_id.equals("0")) {
			StrSearch += " AND pickup_branch_id = " + branch_id + "";
			Str.append("<br><br><font color=\"red\"><b>Select Branch!</b></font>");
			return Str.toString();
		}

		if (driver_id.equals("0")) {
			StrSearch += " AND pickup_emp_id = " + driver_id + "";
			Str.append("<br><br><center><font color=\"red\"><b>Select Driver!</b><center></font>");
			return Str.toString();
		}

		if (!pickupdate.equals("")) {
			StrSearch += " AND date_format(pickup_time, '%d/%m/%Y') = '" + pickupdate + "'";
		}

		try {
			StrSql = "SELECT pickup_id,  branch_code, customer_name, pickup_time_to,"
					+ " pickup_time_from, contact_id,  emp_id, pickup_time, location_name,"
					+ " CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name,"
					+ " CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname) AS contact,"
					+ " contact_mobile1, COALESCE(veh_id, 0) AS veh_id,"
					+ " COALESCE(veh_reg_no, '') AS veh_reg_no"
					+ " FROM " + compdb(comp_id) + "axela_service_pickup"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = pickup_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = pickup_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_location ON location_id= pickup_location_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = pickup_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = pickup_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = pickup_veh_id"
					+ " WHERE pickup_emp_id = " + driver_id + StrSearch + ""
					+ " GROUP BY pickup_id"
					+ " ORDER BY pickup_time_from";
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				Str.append("<b>").append(pickupdate).append("</b><table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
				while (crs.next()) {
					Str.append("<tr>\n<td valign=top>");
					if (!crs.getString("pickup_time_from").equals("")) {
						Str.append("<b>").append(PeriodTime(crs.getString("pickup_time_from"), crs.getString("pickup_time_to"), "2")).append("</b>");
					} else {
						Str.append("<font color=\"red\"><b>Not Confirmed</b></font>");
					}
					Str.append("<br>Pickup Time: <a href=\"../service/pickup-list.jsp?pickup_id=").append(crs.getString("pickup_id")).append("\" target=\"_blank\">");
					Str.append(SplitHourMin(crs.getString("pickup_time"))).append("</a>");
					Str.append("<br>Place: ").append(crs.getString("location_name"));
					Str.append("<br>Driver/Technician: <a href=../portal/executive-summary.jsp?emp_id=").append(crs.getInt("emp_id")).append(">").append(crs.getString("emp_name")).append("</a>");
					if (!crs.getString("veh_id").equals("0")) {
						Str.append("<br>Vehicle: <a href=../service/vehicle-list.jsp?veh_id=").append(crs.getString("veh_id")).append("><b>").append(SplitRegNo(crs.getString("veh_reg_no"), 2))
								.append("</b></a>");
					}
					Str.append("<br>Contact: <a href=../customer/customer-contact-list.jsp?contact_id=").append(crs.getString("contact_id")).append("><b>").append(crs.getString("contact"))
							.append("</b></a>");
					Str.append("<br>Mobile: ").append(crs.getString("contact_mobile1"));
					Str.append("<br></td>\n</tr>\n");
				}
			} else {
				Str.append("<br><br><font color=\"red\"><b>No Pickup(s) found!</b></font>");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
