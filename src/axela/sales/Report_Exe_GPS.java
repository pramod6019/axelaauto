//16 jan 2014, sn
package axela.sales;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_Exe_GPS extends Connect {

	public String StrSql = "", map = "", gps = "", data = "";
	public static String msg = "";
	public String emp_id = "", branch_id = "", gps_data = "", brand_id = "", region_id = "";
	public String team_id = "", exe_id = "";
	public String comp_id = "0";
	public String StrHTML = "", google_api_key = "";
	public String BranchAccess = "", dr_branch_id = "0";
	public String StrSearch = "", gps_marker = "";
	public String go = "", gps_time = "", gpstime = "";
	public String[] team_ids, exe_ids, brand_ids, region_ids, branch_ids;
	public String ExeGpsHtml = "";
	public String ExeAccess = "";
	public String marker = "", marker_content = "", no_data = "", no_data_msg = "";
	public int count = 0;
	public MIS_Check1 mischeck = new MIS_Check1();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_executive_gps", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				go = PadQuotes(request.getParameter("submit_button"));
				GetValues(request, response);
				// CheckForm();
				if (go.equals("Go")) {
					GetValues(request, response);
					CheckForm();
					StrSearch = BranchAccess.replaceAll("branch_id", "emp_branch_id").replace(")", "  OR emp_branch_id = 0)") + " " + ExeAccess;

					if (!exe_id.equals("")) {
						StrSearch = StrSearch + " AND emp_id IN (" + exe_id + ")";
						// SOP("TargetSearch" + StrSearch);
					}
					if (!brand_id.equals("") && branch_id.equals("")) {
						branch_id = ReturnBranchids(brand_id, comp_id);
					}
					if (!region_id.equals("")) {
						StrSearch += " AND enquiry_branch_id IN (SELECT branch_id FROM " + compdb(comp_id) + "axela_branch"
								+ " WHERE branch_region_id IN (" + region_id + ")) ";
					}
					if (!dr_branch_id.equals("")) {
						mischeck.exe_branch_id = branch_id;
						StrSearch = StrSearch + " AND  (emp_branch_id IN(" + branch_id + ") OR emp_id = 1 OR emp_id IN (SELECT empbr.emp_id"
								+ " FROM " + compdb(comp_id) + "axela_emp_branch empbr WHERE " + compdb(comp_id) + "axela_emp.emp_id = empbr.emp_id"
								+ " AND empbr.emp_branch_id IN(" + branch_id + ")))";
					}
					if (!team_id.equals("")) {
						mischeck.exe_branch_id = branch_id;
						StrSearch = StrSearch + " AND emp_id IN (SELECT teamtrans_emp_id FROM " + compdb(comp_id) + "axela_sales_team_exe WHERE teamtrans_team_id IN (" + team_id + "))";
					}
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						google_api_key = ExecuteQuery("SELECT config_google_api_key" + " FROM " + compdb(comp_id) + "axela_config ");
						ListExecutiveGPS();
					}
				} else {
					gps_time = DateToShortDate(kknow());

				}
			}
		} catch (Exception ex) {
			SOPError("AxelaAuto===" + this.getClass().getName());
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

		brand_id = RetrunSelectArrVal(request, "dr_principal");
		brand_ids = request.getParameterValues("dr_principal");
		region_id = RetrunSelectArrVal(request, "dr_region");
		region_ids = request.getParameterValues("dr_region");
		// SOP("region_id===----==" + region_id);
		branch_id = RetrunSelectArrVal(request, "dr_branch");
		branch_ids = request.getParameterValues("dr_branch");
		// SOP("branch id===----==" + branch_id);
		gpstime = PadQuotes(request.getParameter("txt_gps_time"));
		exe_id = RetrunSelectArrVal(request, "dr_executive");
		exe_ids = request.getParameterValues("dr_executive");
		team_id = RetrunSelectArrVal(request, "dr_team");
		team_ids = request.getParameterValues("dr_team");
		// SOP("exe_id===" + exe_id);
		// SOP("team_id===" + team_id);
	}

	protected void CheckForm() {
		msg = "";
		if (dr_branch_id.equals("0")) {
			msg += "<br>Select Branch!";
		}
		// SOP("gpstime = " + gpstime);
		// if (!gpstime.equals("")) {
		// if (!isValidDateFormatLong(gpstime)) {
		// SOP("valid = " + isValidDateFormatLong(gpstime));
		// msg = msg + "<br>Enter Valid GPS Time!";
		// } else {
		// if (Long.parseLong(ConvertLongDateToStr(gpstime)) > Long.parseLong(gettimebyzone(comp_id))) {
		// msg = msg + "<br>GPS time should not be greater than current time!";
		// }
		// }
		// }
		gps_time = gpstime;
		// SOP("gpstime = " + gpstime);
	}

	public void ListExecutiveGPS() {
		StrSql = "SELECT emp_id, CONCAT(emp_name,' (', emp_ref_no, ')') AS emp_name,"
				+ " COALESCE((SELECT CONCAT(gps_latitude,', ',gps_longitude, '-', gps_time)"
				+ " FROM " + compdb(comp_id) + "axela_emp_gps"
				+ " WHERE gps_emp_id = emp_id"
				+ " AND gps_latitude != ''"
				+ " AND gps_longitude != ''";
		if (!gpstime.equals("")) {
			StrSql = StrSql + " AND gps_time <= " + ConvertShortDateToStr(gpstime) + "";
		}
		StrSql = StrSql + " ORDER BY gps_id DESC LIMIT 1), 0) AS gps"
				+ " FROM " + compdb(comp_id) + "axela_emp"
				+ " WHERE emp_active = 1"
				+ StrSearch + ""
				+ " GROUP BY emp_id"
				+ " ORDER BY emp_name";
		// SOP("ListExecutiveGPS StrSql ====== " + StrSql);
		try {
			int totalcount = 0, nodatacount = 0;

			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				String[] parts = null;
				while (crs.next()) {
					// SOP("parts = " );
					totalcount++;
					map = "yes";
					parts = crs.getString("gps").split("-");
					if (!parts[0].equals("0")) {
						gps = "yes";
						marker = marker + "[" + parts[0] + "],\n";
						// marker_content = marker_content +
						// "['<h3>" + crs.getString("emp_name") + "</h3>'+"
						// + "'<p>" + strToLongDate(parts[1]) + "<p>'],\n";
						marker_content = marker_content
								+ "['<div style=line-height:1.35;overflow:hidden;white-space:nowrap;><b>"
								+ crs.getString("emp_name") + "<br>" + strToLongDate(parts[1]) + "</b><div>'],\n";
						gps_data = gps_data + crs.getString("emp_name") + ",";
						gps_marker = gps_marker + parts[0] + ";";
					} else {
						data = "no";
						nodatacount++;
						no_data = no_data + crs.getString("emp_name") + ", ";
					}
				}
				count = totalcount - nodatacount;
				if (gps.equals("yes")) {

					marker = marker.substring(0, marker.length() - 2);
					marker_content = marker_content.substring(0, marker_content.length() - 2);
					// ExeGpsHtml = GpsExecutives(gps_data, gps_marker, google_api_key);
				}
				if (data.equals("no")) {
					no_data = no_data.substring(0, no_data.length() - 2);
					no_data_msg = "No co-ordinates found !";
				}
			} else {
				map = "no";
			}
			// SOP("no_data = " + no_data);
			crs.close();
		} catch (Exception ex) {
			SOP("AxelaAuto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String GpsExecutives(String gps_data, String gps_marker, String google_api_key) {
		// SOP("gps_marker = " + gps_marker);
		// SOP("gps_data = " + gps_data);
		String[] token = gps_data.split(",");
		String[] markertoken = gps_marker.split(";");
		StringBuilder Str = new StringBuilder();
		Str.append("<table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
		for (int i = 1; i <= token.length; i++) {
			Str.append("<tr align=left>");
			Str.append("<td id=\"gps_" + i + "\"  name=\"gps_" + i + "\"  "
					+ " onClick=\"populatemap('" + markertoken[i - 1] + "', '" + google_api_key + "');\" "
					// + "onClick=\"javascript:populatemap("+marker+", "+marker_content+");\""
					+ ">" + token[i - 1] + "</td>\n");
			Str.append("</tr>");
		}
		Str.append("</table>");
		// SOP("Str = " + Str);
		return Str.toString();
	}

}
