package axela.sales;
// smitha nag 21 march 2013
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_TestDrive_Cust_Fb_Dash extends Connect {

	public String StrSql = "", TestDriveSql = "";
	public static String msg = "";
	public String starttime = "", start_time = "";
	public String endtime = "", end_time = "";
	public String emp_id = "", branch_id = "", brand_id = "", region_id = "";
	public String[] team_ids, exe_ids, model_ids, brand_ids, region_ids, branch_ids;
	public String team_id = "", exe_id = "", model_id = "";
	public String StrHTML = "";
	public String comp_id = "0";
	public String ExeAccess = "";
	public String BranchAccess = "", dr_branch_id = "0";
	public String TestDriveSearch = "";
	public int TotalTestDriveFB = 0;
	public int TotalTestDrive = 0;
	public String emp_all_exe = "";
	public MIS_Check1 mischeck = new MIS_Check1();
	public String go = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_mis_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				go = PadQuotes(request.getParameter("submit_button"));
				ExeAccess = GetSession("ExeAccess", request);
				emp_all_exe = CNumeric(GetSession("emp_all_exe", request));
				GetValues(request, response);
				CheckForm();
				if (go.equals("Go")) {
					TestDriveSearch = BranchAccess + ExeAccess;

					if (!brand_id.equals("")) {
						TestDriveSearch += " AND branch_brand_id IN (" + brand_id + ") ";
					}
					if (!region_id.equals("")) {
						TestDriveSearch += " AND branch_region_id IN (" + region_id + ") ";
					}
					if (!branch_id.equals("")) {
						mischeck.exe_branch_id = branch_id;
						TestDriveSearch += "AND branch_id IN (" + branch_id + ")";
					}
					if (!team_id.equals("")) {
						mischeck.exe_branch_id = branch_id;
						mischeck.branch_id = branch_id;
						TestDriveSearch = TestDriveSearch + " AND teamtrans_team_id in (" + team_id + ")";
					}
					if (!exe_id.equals("")) {
						TestDriveSearch = TestDriveSearch + " AND testdrive_emp_id in (" + exe_id + ")";
					}
					if (!model_id.equals("")) {
						TestDriveSearch = TestDriveSearch + " AND enquiry_model_id in (" + model_id + ")";
					}
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						TestDriveSql = "SELECT COUNT(testdrive_id) AS counttestdrive "
								+ " FROM " + compdb(comp_id) + "axela_sales_testdrive  "
								+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id=testdrive_enquiry_id "
								+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id "
								+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id=testdrive_emp_id  "
								+ " WHERE testdrive_client_fb_entry_id!=0  "
								+ " AND testdrive_time>='" + starttime + "' "
								+ " AND testdrive_time<'" + endtime + "' "
								+ TestDriveSearch + " ";
						// SOP("Cust FB Test Drive----" +TestDriveSql);
						CachedRowSet crs = processQuery(TestDriveSql, 0);
						while (crs.next()) {
							TotalTestDriveFB = crs.getInt("counttestdrive");
							// testdrive_ids=testdrive_ids+crs.getString("testdrive_id")+", ";
						}
						crs.close();
						TestDriveSql = "SELECT COUNT(testdrive_id) AS counttestdrive "
								+ " FROM " + compdb(comp_id) + "axela_sales_testdrive  "
								+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id=testdrive_enquiry_id "
								+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id "
								+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id=testdrive_emp_id  "
								+ " WHERE testdrive_fb_taken=1 "
								+ " AND testdrive_time >= substr('" + starttime + "',1,8)"
								+ " AND testdrive_time <= substr('" + endtime + "',1,8)"
								+ TestDriveSearch + " ";
						// SOP("Cust FB Test Drive----" + TestDriveSql);
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
			starttime = ReportStartdate();
		}
		if (endtime.equals("")) {
			endtime = strToShortDate(ToShortDate(kknow()));
		}
		if (branch_id.equals("0")) {
			dr_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
		} else {
			dr_branch_id = branch_id;
		}

		brand_id = RetrunSelectArrVal(request, "dr_principal");
		brand_ids = request.getParameterValues("dr_principal");
		region_id = RetrunSelectArrVal(request, "dr_region");
		region_ids = request.getParameterValues("dr_region");

		branch_id = RetrunSelectArrVal(request, "dr_branch");
		branch_ids = request.getParameterValues("dr_branch");
		exe_id = RetrunSelectArrVal(request, "dr_executive");
		exe_ids = request.getParameterValues("dr_executive");
		team_id = RetrunSelectArrVal(request, "dr_team");
		team_ids = request.getParameterValues("dr_team");
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
				// endtime = ToLongDate(AddHoursDate(StringToDate(endtime), 1,
				// 0, 0));
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
					+ " WHERE fb_active='1' GROUP BY group_name order by group_rank";
			// SOP("StrSql center ----/-----" + StrSql);
			CachedRowSet crs1 = processQuery(StrSql, 0);
			if (crs1.isBeforeFirst()) {

				Str.append("<div class=\"table-responsive table-bordered\">\n");
				Str.append("<table class=\"table table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
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
							+ " WHERE fb_type=1 AND fb_active='1' AND fb_group_id=" + crs1.getString("group_id") + ""
							+ " GROUP BY fb_id";
					// SOP("StrSql---------------------" + StrSql);
					CachedRowSet crs = processQuery(StrSql, 0);
					while (crs.next()) {
						points = 0;
						count++;
						Str.append("<tr align=center>\n");
						Str.append("<td align=center width=5%><b>" + count + "</b></td>\n");
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
										+ " AND fbtrans_testdrive_id in (" + TestDriveSql.replace("COUNT(testdrive_id) AS counttestdrive", "testdrive_id") + ")";
								// SOP("strsql......22222222222......" + StrSql);
								anscount = ExecuteQuery(StrSql);
								Str.append("&nbsp;=&nbsp;<b>" + getPercentage(TotalTestDriveFB, Integer.parseInt(anscount))
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
				Str.append("</table>");
				Str.append("</div>\n");
			} else {
				Str.append("<font color=red><b>No Questions(s) found!</b></font>");
			}
			crs1.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
}
