package axela.preowned;
// smitha nag 21 march 2013
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Preowned_TestDrive_Cust_Fb_Dash extends Connect {

	public String StrSql = "", TestDriveSql = "";
	public static String msg = "";
	public String starttime = "", start_time = "";
	public String endtime = "", end_time = "";
	public String emp_id = "", branch_id = "";
	public String StrHTML = "";
	public String ExeAccess = "";
	public String comp_id = "0";
	public String BranchAccess = "", dr_branch_id = "0";
	public String TestDriveSearch = "";
	public int TotalTestDriveFB = 0;
	public int TotalTestDrive = 0;
	public String go = "";
	public String brand_id = "", region_id = "", team_id = "", exe_id = "", model_id = "";
	public String[] brand_ids, region_ids, branch_ids, team_ids, exe_ids, model_ids;
	public axela.preowned.MIS_Check mischeck = new axela.preowned.MIS_Check();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_report_access, emp_mis_access", request, response);
			HttpSession session = request.getSession(true);

			if (!comp_id.equals("0"))
			{
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				go = PadQuotes(request.getParameter("submit_button"));
				ExeAccess = GetSession("ExeAccess", request);
				GetValues(request, response);

				if (go.equals("Go")) {
					CheckForm();
					if (!brand_id.equals("")) {
						TestDriveSearch += " AND branch_brand_id IN (" + brand_id + ")";
					}
					if (!region_id.equals("")) {
						TestDriveSearch += " AND branch_region_id IN (" + region_id + ")";
					}
					if (!branch_id.equals("")) {
						mischeck.exe_branch_id = branch_id;
						TestDriveSearch += " and enquiry_branch_id IN (" + branch_id + ")";
					}
					if (!exe_id.equals("")) {
						TestDriveSearch += " and testdrive_emp_id IN (" + exe_id + ")";
					}
					if (!model_id.equals("")) {
						TestDriveSearch += " and enquiry_model_id IN (" + model_id + ")";
					}
					if (!team_id.equals("")) {
						mischeck.exe_branch_id = branch_id;
						mischeck.branch_id = branch_id;
						TestDriveSearch = TestDriveSearch + " AND preownedteamtrans_team_id IN (" + team_id + ") ";
					}
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						TestDriveSql = "SELECT COUNT(testdrive_id) AS counttestdrive "
								+ " FROM " + compdb(comp_id) + "axela_preowned_testdrive  "
								+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = testdrive_enquiry_id "
								+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id "
								+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id = testdrive_emp_id  ";
						if (!team_id.equals("")) {
							TestDriveSql += " INNER JOIN " + compdb(comp_id) + "axela_preowned_team_exe ON preownedteamtrans_emp_id = testdrive_emp_id ";
						}
						TestDriveSql += " WHERE testdrive_client_fb_entry_id != 0"
								+ " AND SUBSTR(testdrive_time, 1, 8) >= SUBSTR('" + starttime + "', 1, 8) "
								+ " AND SUBSTR(testdrive_time, 1, 8) <= SUBSTR('" + endtime + "', 1, 8) "
								+ TestDriveSearch + " ";
						// SOP("Cust FB TestDrive----" + TestDriveSql);
						CachedRowSet crs = processQuery(TestDriveSql, 0);
						while (crs.next()) {
							TotalTestDriveFB = crs.getInt("counttestdrive");
							// testdrive_ids=testdrive_ids+crs.getString("testdrive_id")+", ";
						}
						crs.close();
						TestDriveSql = "SELECT COUNT(testdrive_id) AS counttestdrive "
								+ " FROM " + compdb(comp_id) + "axela_preowned_testdrive  "
								+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = testdrive_enquiry_id "
								+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id "
								+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id = testdrive_emp_id  ";
						if (!team_id.equals("")) {
							TestDriveSql += " INNER JOIN " + compdb(comp_id) + "axela_preowned_team_exe ON preownedteamtrans_emp_id = testdrive_emp_id ";
						}
						TestDriveSql += " WHERE testdrive_fb_taken=1 "
								+ " AND SUBSTR(testdrive_time, 1, 8) >= SUBSTR('" + starttime + "', 1, 8) "
								+ " AND SUBSTR(testdrive_time, 1, 8) <= SUBSTR('" + endtime + "', 1, 8) "
								+ TestDriveSearch + " ";
						// SOP("Cust FB TestDrive222----" + TestDriveSql);
						crs = processQuery(TestDriveSql, 0);
						while (crs.next()) {
							TotalTestDrive = crs.getInt("counttestdrive");
						}
						crs.close();
						StrHTML = ListFeedback(request);
					}
				}
			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void GetValues(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		starttime = PadQuotes(request.getParameter("txt_starttime"));
		endtime = PadQuotes(request.getParameter("txt_endtime"));
		if (starttime.equals("")) {
			start_time = ReportStartdate();
		}
		if (endtime.equals("")) {
			end_time = strToShortDate(ToShortDate(kknow()));
		}
		if (branch_id.equals("0")) {
			dr_branch_id = PadQuotes(request.getParameter("dr_branch"));
		} else {
			dr_branch_id = branch_id;
		}
		brand_id = RetrunSelectArrVal(request, "dr_principal");
		brand_ids = request.getParameterValues("dr_principal");
		region_id = RetrunSelectArrVal(request, "dr_region");
		region_ids = request.getParameterValues("dr_region");
		branch_id = RetrunSelectArrVal(request, "dr_branch");
		branch_ids = request.getParameterValues("dr_branch");
		team_id = RetrunSelectArrVal(request, "dr_preownedteam");
		team_ids = request.getParameterValues("dr_preownedteam");
		exe_id = RetrunSelectArrVal(request, "dr_executive");
		exe_ids = request.getParameterValues("dr_executive");
		model_id = RetrunSelectArrVal(request, "dr_model");
		model_ids = request.getParameterValues("dr_model");
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
				if (!starttime.equals("") && !endtime.equals("") && Long.parseLong(starttime) > Long.parseLong(endtime)) {
					msg = msg + "<br>Start Date should be less than End date!";
				}
				end_time = strToShortDate(endtime);
				// endtime = ToLongDate(AddHoursDate(StringToDate(endtime), 1, 0, 0));
			} else {
				msg = msg + "<br>Enter Valid End Date!";
				endtime = "";
			}
		}
	}

	public String ListFeedback(HttpServletRequest request) {
		StrSql = "";
		String anscount = "";
		int count = 0;
		int points = 0;
		int totalpoints = 0;
		try {
			StringBuilder Str = new StringBuilder();
			StrSql = "SELECT group_id, group_name "
					+ " FROM " + compdb(comp_id) + "axela_sales_testdrive_client_fb_group "
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_testdrive_client_fb ON fb_group_id=group_id"
					+ " WHERE fb_active = '1'"
					+ " GROUP BY group_name"
					+ " ORDER BY group_rank";
			// SOP("StrSql center --" + StrSql);
			CachedRowSet crs1 = processQuery(StrSql, 0);
			if (crs1.isBeforeFirst()) {
				Str.append("<div class=\"table-responsive table-bordered table-hover\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr align=center>\n");
				Str.append("<td colspan=2 align=center><b>Total Test Drives: " + TotalTestDrive + "</b></td>\n");
				Str.append("</tr>");
				Str.append("<tr align=center>\n");
				Str.append("<td colspan=2 align=center><b>Total Customer Test Drive Feedbacks: " + TotalTestDriveFB + "</b></td>\n");
				Str.append("</tr>");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs1.next()) {
					count = 0;
					Str.append("<tr align=center>\n");
					Str.append("<td colspan=2 align=left><b><font color=red>" + crs1.getString("group_name") + ":</font></b></td>\n");
					Str.append("</tr>");
					StrSql = "SELECT fb_id, fb_query, fb_option1, fb_option2, fb_option3, fb_option4, fb_option5,"
							+ " fb_option_points1, fb_option_points2, fb_option_points3, fb_option_points4, fb_option_points5 "
							+ " FROM " + compdb(comp_id) + "axela_sales_testdrive_client_fb "
							+ " WHERE fb_type = 1"
							+ " AND fb_active = '1'"
							+ " AND fb_group_id=" + crs1.getString("group_id") + ""
							+ " GROUP BY fb_id";
					// SOP("StrSql==" + StrSql);
					CachedRowSet crs = processQuery(StrSql, 0);
					while (crs.next()) {
						points = 0;
						count++;
						Str.append("<tr align=center>\n");
						Str.append("<td align=center width=5%><b>" + count + ".</b></td>\n");
						Str.append("<td align=left>" + crs.getString("fb_query") + "</td>\n");
						Str.append("</tr>");
						Str.append("<tr align=center>\n");
						Str.append("<td align=left>&nbsp;</td><td align=left>\n");
						for (int i = 1; i <= 5; i++) {
							if (!crs.getString("fb_option" + i + "").equals("")) {
								Str.append("&nbsp;" + crs.getString("fb_option" + i + "") + "");
								StrSql = "SELECT COUNT(fbtrans_id) "
										+ " FROM " + compdb(comp_id) + "axela_sales_testdrive_client_fb_trans "
										+ " WHERE fbtrans_fb_id=" + crs.getString("fb_id")
										+ " AND fbtrans_option" + i + "=1 "
										+ " AND fbtrans_testdrive_id IN (" + TestDriveSql.replace("count(testdrive_id) as counttestdrive", "testdrive_id") + ")";
								// SOP("strsql..-------------." + StrSql);
								anscount = ExecuteQuery(StrSql);
								Str.append("&nbsp;=&nbsp;<b>" + getPercentage(Integer.parseInt(anscount), TotalTestDriveFB)
										+ "%&nbsp;&nbsp;" + Integer.parseInt(crs.getString("fb_option_points" + i + "")) * Integer.parseInt(anscount) + "</b><br>");
								points = points + Integer.parseInt(crs.getString("fb_option_points" + i + "")) * Integer.parseInt(anscount);
								totalpoints = totalpoints + Integer.parseInt(crs.getString("fb_option_points" + i + "")) * Integer.parseInt(anscount);
							}
						}
						Str.append("Total Points: <b>" + points + "</b>");
						Str.append("</td>");
						Str.append("</tr>");
					}
					crs.close();
				}
				Str.append("<tr><td colspan=2 align=center><b>Total Points: " + totalpoints + "</b></td></tr>");
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
			} else {
				Str.append("<font color=red><b>No Questions(s) found!</b></font>");
			}
			crs1.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateModel(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "select model_id, model_name  "
					+ " from " + compdb(comp_id) + "axela_inventory_item_model  "
					+ " inner join " + compdb(comp_id) + "axela_inventory_item on item_model_id=model_id "
					+ " where 1=1 and model_active = '1'"
					// + " and model_sales = '1'"
					+ " group by model_id order by model_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			// SOP("StrSql in PopulateCountry==========" + StrSql);
			while (crs.next()) {
				Str.append("<option value=" + crs.getString("model_id") + "");
				Str.append(ArrSelectdrop(crs.getInt("model_id"), model_ids));
				Str.append(">" + crs.getString("model_name") + "</option>\n");
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
