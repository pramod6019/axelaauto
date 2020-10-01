// Ved (11 Feb 2013)
package axela.portal;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Activity extends Connect {

	public String emp_id = "0";
	public String branch_id = "0";
	public String team_id = "0";
	public String comp_id = "0";
	public String drop_ExeId = "0";
	public String ExeAccess = "";
	public String BranchAccess = "";
	public String exec = "";
	public String TodayDatePicker = "";
	public String TodayCal = "";
	public String d = "";
	public String Year = "";
	public String Month = "";
	public String Day = "";
	public String msg = "", team = "", executive = "";
	public String StrSql = "", StrHTML = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				CheckPerm(comp_id, "emp_activity_access", request, response);
				d = PadQuotes(request.getParameter("d"));
				msg = PadQuotes(request.getParameter("msg"));
				branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch_id")));
				team_id = CNumeric(PadQuotes(request.getParameter("dr_team")));
				team = PadQuotes(request.getParameter("team"));
				executive = PadQuotes(request.getParameter("executive"));
				if (branch_id.equals("0")) {
					branch_id = CNumeric(GetSession("emp_branch_id", request));
				}
				if (team.equals("yes")) {
					StrHTML = PopulateTeam(branch_id, team_id, comp_id);
				}
				if (executive.equals("yes")) {
					StrHTML = PopulateExe(branch_id, team_id);
				}
				if (team_id.equals("0")) {
					team_id = CNumeric(ExecuteQuery("SELECT teamtrans_team_id "
							+ " FROM " + compdb(comp_id) + "axela_sales_team_exe "
							+ " WHERE teamtrans_emp_id =" + emp_id + ""));
				}

				Date dt = kknow();
				TodayDatePicker = new SimpleDateFormat("MMMM dd, yyyy").format(dt);

				if (!d.equals("") && isValidDateFormatShort(d)) {
					Year = SplitYear(ConvertShortDateToStr(d));
					Month = SplitMonth(ConvertShortDateToStr(d));
					Day = SplitDate(ConvertShortDateToStr(d));
				} else {
					Year = SplitYear(ToLongDate(kknow()));
					Month = SplitMonth(ToLongDate(kknow()));
					Day = SplitDate(ToLongDate(kknow()));
				}
				TodayCal = Year + ", " + (Integer.parseInt(Month) - 1) + ", " + Day;
				drop_ExeId = CNumeric(PadQuotes(request.getParameter("dr_exe_id")));

				if (GetSession("activity_emp_id", request).equals("")) {
					exec = CNumeric(GetSession("emp_id", request));
					SetSession("activity_emp_id", exec, request);
				}
				if (!drop_ExeId.equals("0")) {
					exec = PadQuotes(request.getParameter("dr_exe_id"));
					SetSession("activity_emp_id", exec, request);
				}
				else {
					exec = GetSession("activity_emp_id", request) + "";
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public String PopulateBranches(String branch_id, String comp_id) {
		StringBuilder stringval = new StringBuilder();
		try {
			String SqlStr = "SELECT branch_id, branch_name, branch_code"
					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " WHERE branch_active = 1 "
					+ BranchAccess
					+ " ORDER BY branch_brand_id, branch_branchtype_id, branch_name";
			// SOP("SqlStr==branch=" + SqlStr);
			CachedRowSet crs = processQuery(SqlStr, 0);
			stringval.append("<option value =0>Select Branch</option>");
			while (crs.next()) {
				stringval.append("<option value=").append(crs.getString("branch_id")).append("");
				stringval.append(StrSelectdrop(crs.getString("branch_id"), branch_id));
				stringval.append(">").append(crs.getString("branch_name"))
						.append(" (").append(crs.getString("branch_code")).append(")</option>\n");
			}
			crs.close();
			return stringval.toString();
		} catch (Exception ex) {
			SOPError("AxelaAuto=== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
			return "";
		}
	}
	public String PopulateTeam(String branch_id, String team_id, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT team_id, team_name "
					+ " FROM " + compdb(comp_id) + "axela_sales_team "
					+ " WHERE 1 = 1 "
					+ " AND team_active = 1";
			if (!branch_id.equals("0")) {
				StrSql += " AND team_branch_id = " + branch_id + "";
			}
			StrSql += BranchAccess.replace("branch_id", "team_branch_id")
					+ " GROUP BY team_id "
					+ " ORDER BY team_name ";
			// SOP("StrSql------PopulateTeam-----" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=\"dr_team\" id=\"dr_team\" class=\"dropdown form-control\" onchange=\"PopulateExecutive();\">");
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("team_id")).append("");
				Str.append(StrSelectdrop(crs.getString("team_id"), team_id));
				Str.append(">").append(crs.getString("team_name")).append("</option> \n");
			}
			Str.append("</select>");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateExe(String branch_id, String team_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT emp_id, emp_name, emp_ref_no"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id = emp_id"
					+ " WHERE emp_active = '1' ";
			if (!branch_id.equals("0")) {
				StrSql = StrSql + " AND emp_branch_id = " + branch_id + "";
			}
			if (!team_id.equals("0")) {
				StrSql = StrSql + " AND teamtrans_team_id = " + team_id + "";
			}
			StrSql = StrSql + ExeAccess;
			StrSql = StrSql + " ORDER BY emp_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			// SOP("StrSql---exe---" + StrSql);
			Str.append("<select name=\"dr_exe_id\" id=\"dr_exe_id\" class=\"dropdown form-control\" onchange=\"javascript:document.act.submit();\">");
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id"));
				Str.append(StrSelectdrop(crs.getString("emp_id"), exec));
				Str.append(">").append(crs.getString("emp_name")).append(" (");
				Str.append(crs.getString("emp_ref_no")).append(")</option>\n");
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

	public String ExeImg(HttpServletRequest request) {
		String Img = "";
		Img = ExecuteQuery("SELECT emp_photo"
				+ " from " + compdb(comp_id) + "axela_emp"
				+ " where emp_id = " + exec + "");
		if (!Img.equals("")) {
			Img = "<img src=../Thumbnail.do?empphoto=" + Img + "&width=200>";
		} else {
			Img = "";
		}
		return Img;
	}
}
