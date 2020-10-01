//Saiman 4th April 2013
package axela.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Courtesy_Car_Check extends Connect {

	public String branch_id = "0";
	public String demo = "";
	public String msg = "";
	public String StrSql = "";
	public String comp_id = "0";
	public String StrHTML = "";
	public String courtesycar_courtesyveh_id = "0";
	public String courtesycar_time = "";
	public String courtesy_veh = "";

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		CheckSession(request, response);
		HttpSession session = request.getSession(true);
		comp_id = CNumeric(GetSession("comp_id", request));
		if (!comp_id.equals("0")) {
			branch_id = CNumeric(PadQuotes(request.getParameter("branch_id")));
			courtesy_veh = PadQuotes(request.getParameter("courtesy_veh"));
			demo = PadQuotes(request.getParameter("demo"));
			courtesycar_courtesyveh_id = PadQuotes(request.getParameter("veh_id"));
			courtesycar_time = PadQuotes(request.getParameter("courtesydate"));
			ConvertShortDateToStr(courtesycar_time);
			courtesycar_time = strToShortDate(ConvertShortDateToStr(courtesycar_time));
			if (!demo.equals("yes")) {
				StrHTML = PopulateVehicle();
			} else if (demo.equals("yes") && !branch_id.equals("0")) {
				StrHTML = CourtesyCarCalendar();
			} else if (courtesy_veh.equals("yes")) {
				StrHTML = new Call_Update().PopulateCourtesyVehicle(branch_id);
			}
		}
	}

	public String PopulateVehicle() {
		StringBuilder Str = new StringBuilder();
		String search = "";
		if (!branch_id.equals("0")) {
			search = " AND courtesyveh_branch_id = " + branch_id + "";
		}
		try {
			StrSql = "SELECT courtesyveh_id, courtesyveh_name, courtesyveh_regno"
					+ " FROM " + compdb(comp_id) + "axela_service_courtesy_vehicle"
					+ " WHERE 1 = 1" + search + ""
					+ " AND courtesyveh_active = 1"
					+ " ORDER BY courtesyveh_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\"> Select </option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("courtesyveh_id")).append(">");
				Str.append(crs.getString("courtesyveh_name")).append(" - ");
				Str.append(SplitRegNo(crs.getString("courtesyveh_regno"), 2)).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	protected String CourtesyCarCalendar() {
		StringBuilder Str = new StringBuilder();
		String search = "";
		if (courtesycar_time.equals("")) {
			Str.append("<br><br><center><font color=\"red\"><b>Select Courtesy Car Start Time!</b></font></center>");
			return Str.toString();
		}

		if (courtesycar_courtesyveh_id.equals("0")) {
			Str.append("<br><br><font color=\"red\"><b>Select Vehicle!</b></font>");
			return Str.toString();
		}

		if (!branch_id.equals("0")) {
			search += " AND courtesyveh_branch_id = " + branch_id + "";
		}

		if (!courtesycar_courtesyveh_id.equals("0")) {
			search += " AND courtesycar_courtesyveh_id = " + courtesycar_courtesyveh_id + "";
		}

		if (!courtesycar_time.equals("")) {
			search += " AND DATE_FORMAT(courtesycar_time_from, '%d/%m/%Y') = '" + courtesycar_time + "'";
		}

		try {
			StrSql = "SELECT courtesycar_id, courtesyveh_name, courtesyveh_regno,"
					+ " courtesycar_contact_name, courtesycar_time_from, courtesycar_time_to,"
					+ " courtesycar_contact_id, courtesycar_contact_name"
					+ " FROM " + compdb(comp_id) + "axela_service_courtesy_car"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_courtesy_vehicle ON courtesyveh_id = courtesycar_courtesyveh_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = courtesyveh_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id= courtesycar_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = courtesycar_contact_id"
					+ " WHERE 1 = 1" + search + ""
					+ " GROUP BY courtesycar_id"
					+ " ORDER BY courtesycar_time_from";
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				Str.append("<b>").append(courtesycar_time).append("</b>");
				Str.append("<table border=\"1\" cellspacing=0 cellpadding=0 class=\"listtable\">");
				while (crs.next()) {
					Str.append("<tr>\n<td valign=\"top\">");
					if (!crs.getString("courtesycar_time_from").equals("")
							&& !crs.getString("courtesycar_time_to").equals("")) {
						Str.append("<b>").append(strToLongDate(crs.getString("courtesycar_time_from")));
						Str.append(" - ").append(strToLongDate(crs.getString("courtesycar_time_to")));
						Str.append("</b>");
					}

					if (!crs.getString("courtesycar_id").equals("")) {
						Str.append("<br><b>Courtesy ID</b>: ").append(crs.getString("courtesycar_id"));
					}

					if (!crs.getString("courtesyveh_name").equals("")) {
						Str.append("<br><b>Vehcile</b>: ").append(crs.getString("courtesyveh_name"));
					}

					if (!crs.getString("courtesyveh_regno").equals("")) {
						Str.append("<br><b>Vehicle Reg No</b>: ").append(crs.getString("courtesyveh_regno"));
					}

					if (!crs.getString("courtesycar_contact_id").equals("")
							&& !crs.getString("courtesycar_contact_name").equals("")) {
						Str.append("<br><b>Contact Person</b> : <a href=../customer/customer-contact-list.jsp?contact_id=");
						Str.append(crs.getString("courtesycar_contact_id")).append("><b>");
						Str.append(crs.getString("courtesycar_contact_name")).append(" (");
						Str.append(crs.getString("courtesycar_contact_id")).append(")</b></a>");
					}
					Str.append("<br></td>\n</tr>\n");
				}
			} else {
				Str.append("<br><br><font color=\"red\"><b>No Courtesy(s) Found!</b></font>");
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
